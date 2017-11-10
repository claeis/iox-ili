package ch.interlis.iom_j.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.ehi.basics.view.GenericFileFilter;
import ch.interlis.ili2c.metamodel.DataModel;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iox.IoxReader;
import ch.interlis.iox_j.IoxIliReader;

public class CsvReader implements IoxReader,IoxIliReader {
	// state
	private int state;
	private static final int START=0;
	private static final int INSIDE_TRANSFER=1;
	private static final int INSIDE_BASKET=2;
	private static final int INSIDE_OBJECT=3;
	private static final int INSIDE_RECORD_DELIMITER=4;
	private static final int INSIDE_DELIMITER=5;
	private static final int INSIDE_NO_DELIMITER=6;
	private static final int END_DELIMITER=7;
	private static final int END_RECORD_DELIMITER=8;
	private static final int INSIDE_NEWLINE=9;
	private static final int END_OBJECT=10;
	private static final int END_BASKET=11;
	private static final int END_TRANSFER=12;
	private static final int END=13;
	// csv parameter
	private static final String HEADER_PRESENT="present";
	private static final String HEADER_ABSENT="absent";
	private static final String DEFAULT_DELIMITER="\"";
	private static final String DEFAULT_RECORD_DELIMITER=",";
	private static final char NEWLINE_CARRIAGERETURN='\r';
	private static final char NEWLINE_LINEFEED='\n';
	// iox reader parameter
	private IoxFactoryCollection factory;
	private FileReader inputReader=null;
	private FileReader inputReaderBasket=null;
	private TransferDescription td=null;
	private File inputFile=null;
	private int increasingNumber=1;
	private BufferedReader reader = null;
	private BufferedReader basketReader = null;
	private String headerDefinition=null;
	private String userDefined_Delimiter=null;
	private String userDefined_Record_Delimiter=null;
	private List<String> headerAttributes=new ArrayList<String>();
	private int valueCountOfFirstLine=0;
	private boolean skipLine=false;
	// model, topic, class
	private String modelName=null;
	private String topicName="Topic";
	private String className="Class"+increasingNumber;
	// delimiter
	private char currentDelimiter;
	private char currentRecordDelimiter;
	// ili elements
	private HashMap<Viewable, Topic> iliTopics=null;
	private HashMap<Viewable, Model> iliClasses=null;
	private ArrayList<HashMap<Viewable, Model>> listOfIliClasses=null;
	
	public CsvReader(File csvFile)throws IoxException{
		state=START;
		try{
			inputFile=new File(csvFile.getPath());
			inputReader=new FileReader(inputFile);
			inputReaderBasket=new FileReader(inputFile);
			init(inputReader, inputReaderBasket);
		}catch(java.io.IOException ex){
			throw new IoxException(ex);
		}
	}
	
	private void init(FileReader in, FileReader basketIn) throws IoxException, FileNotFoundException{
		reader= new BufferedReader(in);
		basketReader= new BufferedReader(basketIn);
		factory=new ch.interlis.iox_j.DefaultIoxFactoryCollection();
	}
	
	public void setModel(TransferDescription td){
		this.td=td;
	}

	/**
	 * read the optional header and the records of csv file.
	 * @return IoxEvent
	 * @throws IoxException
	 */
	@Override
    public IoxEvent read() throws IoxException{
		Map<String, String> csvAttributes=null;
		IomObject iomObj = null;
		if(state==START){
			state=INSIDE_TRANSFER;
			return new ch.interlis.iox_j.StartTransferEvent();
		}
		if(state==INSIDE_TRANSFER){
			currentDelimiter=(char) getDelimiter().charAt(0);
			currentRecordDelimiter=(char) getRecordDelimiter().charAt(0);
			state=INSIDE_BASKET;
		}
		if(state==INSIDE_BASKET){
			state=INSIDE_OBJECT;
			// check if td is set
			if(td==null){
				// get model name
				modelName=getNameOfFile(inputFile);
				// check if header is defined
				if(isHeaderDefined()){
					// ignore first record
					skipLine=true;
					try {
						headerAttributes = readChars(basketReader, currentRecordDelimiter, currentDelimiter);
						valueCountOfFirstLine=headerAttributes.size();
					} catch (IOException e) {
						throw new IoxException(e);
					}
				}else {
					try {
						valueCountOfFirstLine = readChars(basketReader, currentRecordDelimiter, currentDelimiter).size();
					} catch (IOException e) {
						throw new IoxException(e);
					}
				}
			}else{
				// get ili model data
		    	setupNameMapping();
		    	
				// check if header is defined
				if(isHeaderDefined()){
					try {
						headerAttributes = readChars(basketReader, currentRecordDelimiter, currentDelimiter);
						Viewable aClassName=getViewableByAttributeNames(headerAttributes);
						if(aClassName==null){
							throw new IoxException("attributes of headerrecord: "+headerAttributes.toString()+" not found in iliModel: "+modelName);
						}else{
							className=aClassName.getName();
							topicName=iliTopics.get(aClassName).getName();
							if(topicName==null) {
								topicName="Topic";
							}
							valueCountOfFirstLine=headerAttributes.size();
							skipLine=true;
						}
					} catch (IOException e) {
						throw new IoxException(e);
					}
				}else {
					try {
						List<String> values = readChars(basketReader, currentRecordDelimiter, currentDelimiter);
						valueCountOfFirstLine=values.size();
						Viewable aClassName=getViewableByAttributeCount(valueCountOfFirstLine);
						if(aClassName==null){
							throw new IoxException("attributes size of first line: "+valueCountOfFirstLine+" not found in iliModel: "+modelName);
						}else{
							className=aClassName.getName();
							topicName=iliTopics.get(aClassName).getName();
							if(topicName==null) {
								topicName="Topic";
							}
						}
					} catch (IOException e) {
						throw new IoxException(e);
					}
				}
			}
			state=INSIDE_OBJECT;
			String bid="b"+getCount();
			return new ch.interlis.iox_j.StartBasketEvent(modelName+"."+topicName, bid);
		}
		if(state==INSIDE_OBJECT){
			try {
				while(reader.ready()){
					iomObj=createIomObject(modelName+"."+topicName+"."+className, null);
					csvAttributes=new HashMap<String, String>();
					// read every record.
					List<String> record = readChars(reader, currentRecordDelimiter, currentDelimiter);
					// if header defined, skip this line.
					if(skipLine==true) {
						skipLine=false;
						continue; // next line
					}
					if(isHeaderDefined()){
						// add all attribute-names and attribute-values
						if(headerAttributes!=null) {
							for(int i=0;i<headerAttributes.size();i++) {
								csvAttributes.put(headerAttributes.get(i), record.get(i));
							}
							if(td!=null) {
								// filter attribute-values of model defined attribute-names
								if(iliAttrs!=null) {
									for(int i=0;i<iliAttrs.size();i++) {
										iomObj.setattrvalue(iliAttrs.get(i), csvAttributes.get(iliAttrs.get(i)));
									}
								}
							}else {
								// get attribute-names
								if(csvAttributes!=null) {
									for (Map.Entry<String,String> entry : csvAttributes.entrySet()) {
										iomObj.setattrvalue(entry.getKey(), entry.getValue());
									}
								}
							}
						}
					}else {
						if(valueCountOfFirstLine==0) {
							int count=1;
							for(String attribute:record) {
								iomObj.setattrvalue("attr"+count, attribute);
								count+=1;
							}
							valueCountOfFirstLine=iomObj.getattrcount();	
						}else {
							// compare attrCount
							if(record.size()!=valueCountOfFirstLine) {
								// warning
							}
							int count=1;
							for(int i=0;i<valueCountOfFirstLine;i++) {
								String attribute=record.get(i);
								iomObj.setattrvalue("attr"+count, attribute);
								count+=1;
							}
						}
					}
					return new ch.interlis.iox_j.ObjectEvent(iomObj);
				}
			} catch (IOException e) {
				throw new IoxException(e);
			}
			state=END_BASKET;
		}
		if(state==END_BASKET){
			state=END_TRANSFER;
			return new ch.interlis.iox_j.EndBasketEvent();
		}
		if(state==END_TRANSFER){
			state=END;
			return new ch.interlis.iox_j.EndTransferEvent();
		}
        return null;
    }

    /** Iterate through ili file
	 */
    private void setupNameMapping(){
    	iliTopics=new HashMap<Viewable, Topic>();
    	listOfIliClasses=new ArrayList<HashMap<Viewable, Model>>();
		Iterator tdIterator = td.iterator();
		while(tdIterator.hasNext()){
			iliClasses=new HashMap<Viewable, Model>();
			Object modelObj = tdIterator.next();
			if(!(modelObj instanceof DataModel)){
				continue;
			}
			// iliModel
			DataModel model = (DataModel) modelObj;
			modelName=model.getName();
			Iterator modelIterator = model.iterator();
			while(modelIterator.hasNext()){
				Object topicObj = modelIterator.next();
				if(!(topicObj instanceof Topic)){
					continue;
				}
				// iliTopic
				Topic topic = (Topic) topicObj;
				topicName=topic.getName();
				// iliClass
				Iterator classIter=topic.iterator();
		    	while(classIter.hasNext()){
		    		Object classObj=classIter.next();
		    		if(!(classObj instanceof Viewable)){
    					continue;
    				}
		    		Viewable viewable = (Viewable) classObj;
	    			iliClasses.put(viewable, model);
	    			iliTopics.put(viewable, topic);
		    	}
			}
			listOfIliClasses.add(iliClasses);
		}
    }
    
    List<String> iliAttrs=null;
	private String lineSeparator=System.getProperty("line.separator");
    /** Get Viewable of model, where attributes of records are equal to attribute in model class
     * @return Viewable
     * @throws IoxException 
     */
    private Viewable getViewableByAttributeNames(List<String> headerAttrs) throws IoxException{
    	List<String> foundClasses=null;
    	Viewable viewable=null;
    	if(iliClasses==null){
    		setupNameMapping();
    	}
    	foundClasses=new ArrayList<String>();
    	// first last model file.
    	for(HashMap<Viewable, Model> mapIliClasses : listOfIliClasses){
    		for(Viewable iliViewable : mapIliClasses.keySet()){
    			iliAttrs=new ArrayList<String>();
    			Iterator attrIter=iliViewable.getAttributes();
    			while(attrIter.hasNext()){
    				Element attribute=(Element) attrIter.next();
    				iliAttrs.add(attribute.getName());
    			}
    			// check if all model attributes are contained in defined header
				if(iliAttrs.containsAll(headerAttrs)){
					viewable=iliViewable;
					modelName=mapIliClasses.get(iliViewable).getName();
					foundClasses.add(viewable.getScopedName());
				}
    		}
    	}
    	if(foundClasses.size()>1) {
    		throw new IoxException("several possible classes were found: "+foundClasses.toString());
    	}else if(foundClasses.size()==1){
    		return viewable;
    	}
    	return null;
    }
	
    /** Get Viewable of model, where record attribute-count is equal to attribute-count of model class
     * @return Viewable
     * @throws IoxException 
     */
    private Viewable getViewableByAttributeCount(Integer attrCountOfRecord) throws IoxException{
    	int attrCountOfMatchedClasses=0;
    	Viewable matchedIliViewable=null;
    	StringBuilder multipleMatchedClasses = new StringBuilder();
    	if(iliClasses==null){
    		setupNameMapping();
    	}
    	String comma="";
    	// first last model file.
    	for(HashMap<Viewable, Model> mapIliClasses : listOfIliClasses){
    		for(Viewable iliViewable : mapIliClasses.keySet()){
    			List<String> iliAttrs=new ArrayList<String>();
    			Iterator attrIter=iliViewable.getAttributes();
    			while(attrIter.hasNext()){
    				Element attribute=(Element) attrIter.next();
    				iliAttrs.add(attribute.getName());
    			}
    			if(iliAttrs.size()==attrCountOfRecord){
    				modelName=mapIliClasses.get(iliViewable).getName();
					attrCountOfMatchedClasses+=1;
    				matchedIliViewable=iliViewable;
    				multipleMatchedClasses.append(comma);
    				multipleMatchedClasses.append(matchedIliViewable.getName());
    				comma=",";
    			}
    		}
    	}
    	if(attrCountOfMatchedClasses==1){
    		return matchedIliViewable;
    	}else if(attrCountOfMatchedClasses>1){
    		throw new IoxException("multiple class candidates: "+multipleMatchedClasses.toString());
    	}else{
    		return null;
    	}
    }
    
	/**
	 * read the path of input csv file and get the single name of csv file.
	 * @return modelname
	 * @throws IoxException
	 */
	private String getNameOfFile(File file) throws IoxException{
		// get path of csv file
		String path=file.getName();
		if(path!=null){
			String fileName=GenericFileFilter.stripFileExtension(path);
			return fileName;
		}else{
			throw new IoxException("expected csv file");
		}
	}

	/**
	 * read chars of each record and returns a list of attributes and values of each record.
	 * @param bufferedReader
	 * @param separator
	 * @param limiter
	 * @return List<String>
	 * @throws IOException
	 * @throws IoxException
	 */
	private List<String> readChars(BufferedReader bufferedReader, char separator, char limiter) throws IOException, IoxException{
        List<String> result = new ArrayList<String>();
        StringBuffer currentValue = new StringBuffer();
        // buffered reader is empty/null/!has next
        if (!bufferedReader.ready()){
        	return result;
        }
        int value=0;
        // reads to the end of the stream 
        while((value = bufferedReader.read()) != -1){
           // converts int to character
           char currentChar = (char)value;
           if(state==INSIDE_OBJECT){
	           if(currentChar==currentDelimiter){
	        	   state=INSIDE_RECORD_DELIMITER;
	           }else{
	        	   state=INSIDE_NO_DELIMITER;
	           }
	           if(state==INSIDE_RECORD_DELIMITER){
	        	   state=INSIDE_DELIMITER;
	        	   continue;
	           }
           }
           if(state==INSIDE_DELIMITER){
        	   if(currentChar==currentDelimiter){
        		   state=END_DELIMITER;
        		   continue;
        	   }else{
        		   currentValue.append(currentChar);
        		   continue;
        	   }
           }
           // no delimiter defined
           if(state==INSIDE_NO_DELIMITER){
        	   if(currentChar==currentDelimiter){
        		   state=END_DELIMITER;
        		   continue;
        	   }else if(currentChar==currentRecordDelimiter){
        		   result.add(currentValue.toString());
        		   int charCount=currentValue.length();
    			   currentValue.delete(0, charCount);
    			   state=INSIDE_OBJECT;
    			   continue;
        	   }else if(currentChar==NEWLINE_LINEFEED){
        		   state=INSIDE_NEWLINE;
        	   }else if(currentChar==NEWLINE_CARRIAGERETURN){
        		   continue;
        	   }else{
        		   currentValue.append(currentChar);
        		   continue;
        	   }
           }
           if(state==INSIDE_NEWLINE){
        	   // CRLF no delimiter defined
	           result.add(currentValue.toString());
			   int charCount=currentValue.length();
			   currentValue.delete(0, charCount);
			   state=END_OBJECT;
           }
           if(state==END_DELIMITER){
        	   if(currentChar==currentDelimiter){
        		   currentValue.append(currentChar);
        		   state=INSIDE_DELIMITER;
        		   continue;
        	   }else if(currentChar==currentRecordDelimiter){
        		   result.add(currentValue.toString());
        		   int charCount=currentValue.length();
    			   currentValue.delete(0, charCount);
        		   state=END_RECORD_DELIMITER;
        		   continue;
        	   }else if(currentChar==NEWLINE_CARRIAGERETURN){
        		   result.add(currentValue.toString());
        		   int charCount=currentValue.length();
    			   currentValue.delete(0, charCount);
        		   state=END_OBJECT;
        		   continue;
        	   }else if(currentChar==NEWLINE_LINEFEED){
        		   result.add(currentValue.toString());
        		   int charCount=currentValue.length();
    			   currentValue.delete(0, charCount);
        		   state=END_OBJECT;
        	   }else{
        		   currentValue.append(currentChar);
        		   continue;
        	   }
           }
           if(state==END_RECORD_DELIMITER){
        	   if(currentChar==currentDelimiter){
        		   state=INSIDE_DELIMITER;
        		   continue;
        	   }else if(currentChar==NEWLINE_CARRIAGERETURN || currentChar==NEWLINE_LINEFEED){
        		   state=END_OBJECT;
        	   }else{
        		   state=INSIDE_NO_DELIMITER;
        		   currentValue.append(currentChar);
        		   continue;
        	   }
           }
           if(state==END_OBJECT){
        	   state=INSIDE_OBJECT;
        	   return result;
           }
           currentValue.append(currentChar);
       }
       // end of records
       result.add(currentValue.toString());
       state=END_BASKET;
	   return result;
    }

    @Override
	public IomObject createIomObject(String type, String oid)throws IoxException{
    	if(oid==null){
			oid="o"+getCount();
    	}
		return factory.createIomObject(type, oid);
	}
    
    /**
     * increase count is used to increase object/basket id's.
     * @return
     */
    private String getCount(){
    	int count=increasingNumber;
    	increasingNumber+=1;
    	return String.valueOf(count);
    }
	
	@Override
	public void close() throws IoxException{
		if(headerAttributes!=null || headerAttributes.size()>0) {
			headerAttributes.clear();
			headerAttributes=null;
		}
		modelName=null;
		topicName=null;
		className=null;
		iliTopics=null;
		iliClasses=null;
		listOfIliClasses=null;
		try {
			reader.close();
		} catch (IOException e2) {
			throw new IoxException(e2);
		}
		reader=null;
		try {
			basketReader.close();
		} catch (IOException e1) {
			throw new IoxException(e1);
		}
		basketReader=null;
		if(inputFile!=null){
			inputFile=null;
		}
		userDefined_Delimiter=DEFAULT_DELIMITER;
		userDefined_Record_Delimiter=DEFAULT_RECORD_DELIMITER;
		currentDelimiter=DEFAULT_DELIMITER.charAt(0);
		currentRecordDelimiter=DEFAULT_RECORD_DELIMITER.charAt(0);
		try {
			inputReader.close();
		} catch (IOException e) {
			throw new IoxException(e);
		}
		inputReader=null;
		try {
			inputReaderBasket.close();
		} catch (IOException e) {
			throw new IoxException(e);
		}
		inputReaderBasket=null;
		td=null;
		increasingNumber=1;
		headerDefinition=null;
		valueCountOfFirstLine=0;
	}
	
	@Override
	public IoxFactoryCollection getFactory() throws IoxException{
		return factory;
	}
	
	@Override
	public void setFactory(IoxFactoryCollection factory) throws IoxException{
		this.factory=factory;
	}
	
	private Boolean isHeaderDefined() throws IoxException{
		if(headerDefinition!=null){
			if(headerDefinition.contains(HEADER_PRESENT)){
				return true;
			}else if(headerDefinition.contains(HEADER_ABSENT)){
				return false;
			}else{
				throw new IoxException("expected present or absent, unexpected "+headerDefinition);
			}
		}
		return false;
	}
	
	/**
	 * set header is present or header is absent.
	 * @param headerState
	 */
	public void setHeader(String headerState){
		headerDefinition = headerState;
	}
	
	private String getDelimiter() throws IoxException{
		if(userDefined_Delimiter!=null){
			return userDefined_Delimiter;
		}else{
			return DEFAULT_DELIMITER;
		}
	}
	
	/**
	 * set user defined delimiter.
	 * @param definition
	 */
	public void setDelimiter(String definition){
		userDefined_Delimiter = definition;
	}
	
	private String getRecordDelimiter() throws IoxException{
		if(userDefined_Record_Delimiter!=null){
			return userDefined_Record_Delimiter;
		}else{
			return DEFAULT_RECORD_DELIMITER;
		}
	}
	
	/**
	 * set user defined record delimiter.
	 * @param definition
	 */
	public void setRecordDelimiter(String definition){
		userDefined_Record_Delimiter = definition;
	}

	public String getLineSeparator() {
		return lineSeparator;
	}
}