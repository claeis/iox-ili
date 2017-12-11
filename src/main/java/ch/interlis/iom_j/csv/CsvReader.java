package ch.interlis.iom_j.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.ehi.basics.settings.Settings;
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
	private static final char DEFAULT_VALUE_DELIMITER='\"';
	private static final char DEFAULT_VALUE_SEPARATOR=',';
	private static final char NEWLINE_CARRIAGERETURN='\r';
	private static final char NEWLINE_LINEFEED='\n';
	public static final String ENCODING = "ch.interlis.iom_j.csv.encoding";
	// iox reader parameter
	private IoxFactoryCollection factory;
	private TransferDescription td=null;
	private File inputFile=null;
	private int nextId=1;
	private BufferedReader reader = null;
	private boolean firstLineIsHeader=false;
	private List<String> headerAttributes=null;;
	private List<String> pendingValues=null;
	// model, topic, class
	private String modelName=null;
	private String topicName="Topic";
	private String className="Class"+nextId;
	// delimiter
	private char currentValueDelimiter=DEFAULT_VALUE_DELIMITER;
	private char currentValueSeparator=DEFAULT_VALUE_SEPARATOR;
	// ili elements
	private HashMap<Viewable, Topic> iliTopics=null;
	private HashMap<Viewable, Model> iliClasses=null;
	private ArrayList<HashMap<Viewable, Model>> listOfIliClasses=null;
	
	public CsvReader(File csvFile)throws IoxException
	{
		this(csvFile,null);
	}
	public CsvReader(File csvFile,Settings settings)throws IoxException{
		state=START;
		try{
			inputFile=csvFile;
			String encoding=null;
			if(settings!=null) {
				encoding=settings.getValue(ENCODING);
			}
			if(encoding==null) {
				encoding=Charset.defaultCharset().name();
			}
			reader=new BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(inputFile),encoding));
			init();
		}catch(java.io.IOException ex){
			throw new IoxException(ex);
		}
	}
	
	private void init() throws IoxException, FileNotFoundException{
		factory=new ch.interlis.iox_j.DefaultIoxFactoryCollection();
	}
	
	public void setModel(TransferDescription td){
		this.td=td;
	}
	public String[] getAttributes() {
		return headerAttributes.toArray(new String[headerAttributes.size()]);
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
			state=INSIDE_BASKET;
		}
		if(state==INSIDE_BASKET){
			// if ili-model is given
			if(td!=null){
				// get ili model data
		    	setupNameMapping();
		    	
				if(firstLineIsHeader){
					try {
						headerAttributes = readValues(reader, currentValueSeparator, currentValueDelimiter);
						Viewable aClassName=getViewableByAttributeNames(headerAttributes);
						if(aClassName==null){
							throw new IoxException("attributes of headerrecord: "+headerAttributes.toString()+" not found in iliModel: "+modelName);
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
				}else {
					try {
						pendingValues = readValues(reader, currentValueSeparator, currentValueDelimiter);
						int valueCount = pendingValues.size();
						headerAttributes=new ArrayList<String>();
						Viewable aClassName=getViewableByAttributeCount(valueCount,headerAttributes);
						if(aClassName==null){
							throw new IoxException("attributes size of first line: "+valueCount+" not found in iliModel: "+modelName);
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
			}else{
				// get model name
				modelName=getNameOfFile(inputFile);
				// check if header is defined
				if(firstLineIsHeader){
					try {
						headerAttributes = readValues(reader, currentValueSeparator, currentValueDelimiter);
					} catch (IOException e) {
						throw new IoxException(e);
					}
				}else {
					try {
						pendingValues = readValues(reader, currentValueSeparator, currentValueDelimiter);
					} catch (IOException e) {
						throw new IoxException(e);
					}
					int valueCount = pendingValues.size();
					headerAttributes=new ArrayList<String>();
					for(int i=1;i<=valueCount;i++) {
						headerAttributes.add("attr"+i);
					}
					
				}
			}
			state=INSIDE_OBJECT;
			String bid="b"+getNewId();
			return new ch.interlis.iox_j.StartBasketEvent(modelName+"."+topicName, bid);
		}
		if(state==INSIDE_OBJECT){
			try {
				List<String> record = null;
				if(pendingValues!=null) {
					record=pendingValues;
					pendingValues=null;
				}else {
					record = readValues(reader, currentValueSeparator, currentValueDelimiter);
				}
				if(record!=null && record.size()>0) {
					iomObj=createIomObject(modelName+"."+topicName+"."+className, null);
					for(int i=0;i<headerAttributes.size();i++) {
						String value=null;
						if(record.size()>i) {
							value=record.get(i);
						}
						if(value!=null && value.length()>0) {
							iomObj.setattrvalue(headerAttributes.get(i), value);
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
    			ArrayList iliAttrs=new ArrayList<String>();
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
    private Viewable getViewableByAttributeCount(Integer attrCountOfRecord,List<String> iliAttrs) throws IoxException{
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
    			iliAttrs.clear();
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
	private List<String> readValues(BufferedReader bufferedReader, char separator, char limiter) throws IOException, IoxException{
        List<String> result = new ArrayList<String>();
        StringBuffer currentValue = new StringBuffer();
        // buffered reader is empty/null/!has next
        if (!bufferedReader.ready()){
        	return result;
        }
        int readChar=0;
        int state=INSIDE_OBJECT;
        // reads to the end of the stream 
        while((readChar = bufferedReader.read()) != -1){
           // converts int to character
           char currentChar = (char)readChar;
           if(state==INSIDE_OBJECT){
	           if(currentChar==currentValueDelimiter){
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
        	   if(currentChar==currentValueDelimiter){
        		   state=END_DELIMITER;
        		   continue;
        	   }else{
        		   currentValue.append(currentChar);
        		   continue;
        	   }
           }
           // no delimiter defined
           if(state==INSIDE_NO_DELIMITER){
        	   if(currentChar==currentValueDelimiter){
        		   state=END_DELIMITER;
        		   continue;
        	   }else if(currentChar==currentValueSeparator){
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
        	   if(currentChar==currentValueDelimiter){
        		   currentValue.append(currentChar);
        		   state=INSIDE_DELIMITER;
        		   continue;
        	   }else if(currentChar==currentValueSeparator){
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
        	   if(currentChar==currentValueDelimiter){
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
        	   return result;
           }
           currentValue.append(currentChar);
       }
       // end of records
       result.add(currentValue.toString());
	   return result;
    }

    @Override
	public IomObject createIomObject(String type, String oid)throws IoxException{
    	if(oid==null){
			oid="o"+getNewId();
    	}
		return factory.createIomObject(type, oid);
	}
    
    /**
     * increase count is used to increase object/basket id's.
     * @return
     */
    private String getNewId(){
    	int count=nextId;
    	nextId+=1;
    	return String.valueOf(count);
    }
	
	@Override
	public void close() throws IoxException{
		if(reader!=null) {
			try {
				reader.close();
			} catch (IOException e2) {
				throw new IoxException(e2);
			}
			reader=null;
		}
	}
	
	@Override
	public IoxFactoryCollection getFactory() throws IoxException{
		return factory;
	}
	
	@Override
	public void setFactory(IoxFactoryCollection factory) throws IoxException{
		this.factory=factory;
	}
		
	/**
	 * set header is present or header is absent.
	 * @param headerState
	 */
	public void setFirstLineIsHeader(boolean headerState){
		firstLineIsHeader = headerState;
	}
	
	private char getValueDelimiter() throws IoxException{
		return currentValueDelimiter;
	}
	
	/**
	 * set user defined delimiter.
	 * @param definition
	 */
	public void setValueDelimiter(char definition){
		currentValueDelimiter = definition;
	}
	
	private char getValueSeparator() throws IoxException{
		return currentValueSeparator;
	}
	
	/**
	 * set user defined record delimiter.
	 * @param definition
	 */
	public void setValueSeparator(char definition){
		currentValueSeparator = definition;
	}

	public String getLineSeparator() {
		return lineSeparator;
	}
}