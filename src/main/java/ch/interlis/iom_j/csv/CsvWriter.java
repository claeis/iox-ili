package ch.interlis.iom_j.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import ch.interlis.ili2c.metamodel.DataModel;
import ch.interlis.ili2c.metamodel.LocalAttribute;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.EndBasketEvent;
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iox.StartBasketEvent;
import ch.interlis.iox.StartTransferEvent;
import ch.interlis.iox_j.ObjectEvent;

public class CsvWriter implements ch.interlis.iox.IoxWriter {
	// td
	private static TransferDescription td=null;
	// state
    private static final int START=0;
    private static final int INSIDE_LINEFEED=1;
    private static final int INSIDE_CARRIAGERETURN=2;
    private static final int END_LINEFEED=3;
    private static final int END_CARRIAGERETURN=4;
	// csv
	private static final String HEADER_PRESENT="present";
	private static final String HEADER_ABSENT="absent";
	private static final char DEFAULT_DELIMITER='\"';
	private static final char DEFAULT_RECORD_DELIMITER=',';
	private static final char NEWLINE_CARRIAGERETURN='\r';
	private static final char NEWLINE_LINEFEED='\n';
	// writer
	private BufferedWriter writer=null;
	// defined by user
	private static String userDefined_Header=null;
	private static String userDefined_Delimiter=null;
	private static String userDefined_Record_Delimiter=null;
	// current delimiter
	private static char currentDelimiter;
	private static char currentRecordDelimiter;
	// first line of file
	private boolean firstLineInFile=true; // could be header/first attrValues
	// header
	private boolean headerIsAlreadyDefined=false; // set header once
	private String[] headerAttrNames=null;

	/** create new CsvWriter
	 * @param file
	 * @throws IoxException
	 */
	public CsvWriter(File file)throws IoxException{
		FileWriter fileWriter=null;
		if(file!=null) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				throw new IoxException("path to create file not found",e1);
			}
			try {
				fileWriter=new FileWriter(file);
				init(file, fileWriter);
			} catch (IOException e) {
				throw new IoxException("could not create file",e);
			}
		}
	}
	
	/** initialize writer
	 * @param in
	 * @param fileWriter
	 */
	private void init(File in, FileWriter fileWriter) {
		writer=new BufferedWriter(fileWriter);
	}
    
	/** find appropriate viewable in model/models
	 * @param iomObj
	 * @return
	 */
	private Viewable findViewable(IomObject iomObj) {
		Viewable ret=null;
		String tag=iomObj.getobjecttag();
		String[] elements=tag.split("\\.");
		String viewable=elements[elements.length-1];
		// td is set
		if(td!=null) {
			List<HashMap<String,Viewable>> allModels=setupNameMapping();
			for(HashMap<String,Viewable> map : allModels) {
				ret= map.get(viewable);
				if(ret!=null) {
					return ret;
				}
			}
		}
		return null;
	}
	
	/** check attrvalues with attrnames
	 * @param viewable
	 * @param iomObj
	 * @return
	 */
	private String[] getValidAttributes(Viewable viewable, IomObject iomObj){
		// iliAttributes
		String[] attrs=new String[iomObj.getattrcount()];
		int count=0;
		if(viewable==null) {
			for(int i=0;i<iomObj.getattrcount();i++) {
				String attribute=iomObj.getattrname(i);
				attrs[count]=attribute;
				count+=1;
			}
		}else {
			Iterator viewableIter=viewable.getAttributes();
			while(viewableIter.hasNext()) {
				Object attrObj=viewableIter.next();
				if(attrObj instanceof LocalAttribute) {
					LocalAttribute localAttr= (LocalAttribute)attrObj;
					String iliAttrName=localAttr.getName();
					if(iomObj.getattrvaluecount(iliAttrName)>0) {
						attrs[count]=localAttr.getName();
						count+=1;
					}
				}
			}
		}
		return attrs;
	}

	/** Iterate through ili file and set all models with class names and appropriate class object.
	 */
    private List<HashMap<String, Viewable>> setupNameMapping(){
    	List<HashMap<String, Viewable>> allModels=new ArrayList<HashMap<String, Viewable>>();
    	HashMap<String, Viewable> allClassesOfModel=null;
		Iterator tdIterator = td.iterator();
		while(tdIterator.hasNext()){
			allClassesOfModel=new HashMap<String, Viewable>();
			Object modelObj = tdIterator.next();
			if(!(modelObj instanceof DataModel)){
				continue;
			}
			// iliModel
			DataModel model = (DataModel) modelObj;
			Iterator modelIterator = model.iterator();
			while(modelIterator.hasNext()){
				Object topicObj = modelIterator.next();
				if(!(topicObj instanceof Topic)){
					continue;
				}
				// iliTopic
				Topic topic = (Topic) topicObj;
				// iliClass
				Iterator classIter=topic.iterator();
		    	while(classIter.hasNext()){
		    		Object classObj=classIter.next();
		    		if(!(classObj instanceof Viewable)){
    					continue;
    				}
		    		Viewable viewable = (Viewable) classObj;
	    			allClassesOfModel.put(viewable.getName(), viewable);
		    	}
			}
			allModels.add(0,allClassesOfModel);
		}
		return allModels;
    }
    
    /** writes objectvalues of objects via iox-Events
     */
    @Override
	public void write(IoxEvent event) throws IoxException {
		if(event instanceof StartTransferEvent){
		}else if(event instanceof StartBasketEvent){
			if(userDefined_Delimiter==null) {
				currentDelimiter=CsvWriter.DEFAULT_DELIMITER;
			}else {
				if(userDefined_Delimiter.length()>1) {
				   throw new IoxException("userDefined delimiter "+userDefined_Delimiter+" not valid char");
				}
				currentDelimiter=userDefined_Delimiter.charAt(0);
			}
			if(userDefined_Record_Delimiter==null) {
				currentRecordDelimiter=CsvWriter.DEFAULT_RECORD_DELIMITER;
			}else {
				if(userDefined_Record_Delimiter.length()>1) {
					throw new IoxException("userDefined record delimiter "+userDefined_Record_Delimiter+" not valid char");
				}
				currentRecordDelimiter=userDefined_Record_Delimiter.charAt(0);
			}
		}else if(event instanceof ObjectEvent){
			ObjectEvent obj=(ObjectEvent) event;
			IomObject iomObj=(IomObject)obj.getIomObject();
			if(td!=null) {
				// check if class exists in model/s
				Viewable resultViewableHeader=findViewable(iomObj);
				if(resultViewableHeader!=null) {
	            	if(userDefinedHeaderIsSet() && !headerIsAlreadyDefined) {
	            		headerAttrNames=getValidAttributes(resultViewableHeader, iomObj);
	    				try {
							writeHeader(headerAttrNames);
							headerIsAlreadyDefined=true;
							firstLineInFile=false;
						} catch (IOException e) {
							throw new IoxException("attributes in model not found",e);
						}
	            	}
	            	// content
	            	headerAttrNames=getValidAttributes(resultViewableHeader, iomObj);
	            	 if(headerAttrNames[0]==null) {
	            		 throw new IoxException("attrnames of "+iomObj.getobjecttag()+" not valid");
	            	  }
	            	// attrvalues of attrnames (header set/ header not set)
	            	String[] validAttrValues=getValidAttrValues(headerAttrNames, iomObj);
	            	if(validAttrValues[0]==null) {
	            		throw new IoxException("attrvalues of "+iomObj.toString()+" not equal to "+headerAttrNames);
	            	}
	            	try {
	            		if(!firstLineInFile) {
	            			writer.newLine();
	            		}else {
	            			firstLineInFile=false;
	            		}
	            		writeLine(validAttrValues);
	            	} catch (IOException e) {
						throw new IoxException(e);
	            	}
	            }else {
	            	throw new IoxException("class "+iomObj.getobjecttag()+" in model not found");
	            }
			}else {
				// no model set
				// check if class exists in model/s
            	if(userDefinedHeaderIsSet() && !headerIsAlreadyDefined) {
            		headerAttrNames=getValidAttributes(null, iomObj);
    				try {
						writeHeader(headerAttrNames);
						headerIsAlreadyDefined=true;
						firstLineInFile=false;
					} catch (IOException e) {
						throw new IoxException(e);
					}
            	}
            	if(!userDefinedHeaderIsSet() && !headerIsAlreadyDefined) {
            		headerAttrNames=getValidAttributes(null, iomObj);
            		if(headerAttrNames==null) {
            			headerIsAlreadyDefined=true;
            		}
            	}
            	// content
            	String[] validAttrValues=getValidAttrValues(headerAttrNames, iomObj);
            	if(validAttrValues[0]==null) {
            		throw new IoxException("attrnames of "+iomObj.toString()+" not equal to "+headerAttrNames);
            	}
            	try {
            		if(!firstLineInFile) {
            			writer.newLine();
            		}else {
            			firstLineInFile=false;
            		}
	            	writeLine(validAttrValues);
            	} catch (IOException e) {
					throw new IoxException(e);
            	}
            }
		}else if(event instanceof EndBasketEvent){
		}else if(event instanceof EndTransferEvent){
			close();
		}else{
			throw new IoxException("unknown event type "+event.getClass().getName());
		}
	}
    
    /** get attribute values which are valid to write to file
     * @param validHeaderAttrs
     * @param currentIomObject
     * @return
     */
    private String[] getValidAttrValues(String[] validHeaderAttrs, IomObject currentIomObject) {
    	String[] attrValues=null;
    	String attrValue=null;
    	// td!=null && header is defined
    	if(headerIsAlreadyDefined) {
    		int count=0;
    		attrValues=new String[validHeaderAttrs.length];
	    	for (int i=0;i<validHeaderAttrs.length;i++) {
	    		attrValue=currentIomObject.getattrvalue(validHeaderAttrs[i]);
	    		if(attrValue!=null) {
	    			attrValues[count]=attrValue;
	    			count+=1;
	    		}
	    	}
	    	return attrValues;
    	}else {
    		// td!=null && no header defined
    		int count=0;
    		attrValues=new String[currentIomObject.getattrcount()];
	    	for (int i=0;i<currentIomObject.getattrcount();i++) {
	    		attrValue=currentIomObject.getattrvalue(currentIomObject.getattrname(i));
	    		if(attrValue!=null) {
	    			attrValues[count]=attrValue;
	    			count+=1;
	    		}
	    	}
	    	return attrValues;
    	}
	}

    /** write header attribute names to file
     * @param attrNames
     * @throws IOException
     */
	private void writeHeader(String[] attrNames) throws IOException {
    	boolean firstName=true;
    	for (String name : attrNames) {
    		if(!firstName){
        		writer.write(currentRecordDelimiter);
        	}
    		firstName=false;
            writer.write(quoteformatter(name));
        }
	}

	/** replace value content of quote with csv format
	 * @param value
	 * @return
	 */
	private String quoteformatter(String value) {
        String result = value;
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;
    }
	
	/** write each line in file
	 * @param attrValues
	 * @throws IOException
	 * @throws IoxException
	 */
    private void writeLine(String[] attrValues) throws IOException, IoxException {
        boolean first = true;
        for (String value : attrValues){
            if (!first) {
                writer.write(currentRecordDelimiter);
            }
        	// iterate through each char
        	writer.write(currentDelimiter);
        	String newValue=quoteformatter(value);
            writeChars(newValue);
            writer.write(currentDelimiter);
            first = false;
        }
    }
    
    /** check each char on special characters and/or write down char to file
     * @param attrValue
     * @throws IOException
     */
	private void writeChars(String attrValue) throws IOException {
		int state=START;
		StringBuilder constructedValue=new StringBuilder();
		for(int i=0;i<attrValue.length();i++) {
			char currentChar=attrValue.charAt(i);
			
			if(state==INSIDE_LINEFEED) {
				if(currentChar==NEWLINE_CARRIAGERETURN) {
					// ignore and not write to file.
					state=END_LINEFEED;
				}else {
					state=START;
				}
			}else if(state==INSIDE_CARRIAGERETURN) {
				if(currentChar==NEWLINE_LINEFEED) {
					// ignore and not write to file.
					state=END_CARRIAGERETURN;
				}else {
					state=START;
				}
			}else {
				state=START;
			}
			
			if(state==START) {
				if(currentChar==NEWLINE_LINEFEED){
					writer.write(constructedValue.toString());
					constructedValue.delete(0, constructedValue.length());
					writer.newLine();
					state=INSIDE_LINEFEED;
				} else if(currentChar==NEWLINE_CARRIAGERETURN) {
					writer.write(constructedValue.toString());
					constructedValue.delete(0, constructedValue.length());
					writer.newLine();
					state=INSIDE_CARRIAGERETURN;
				}else {
					constructedValue.append(currentChar);
				}
			}else {
				state=START;
			}
		}
		writer.write(constructedValue.toString());
	}

	/** set model/models
	 * @param td
	 */
	public void setModel(TransferDescription td) {
		CsvWriter.td=td;
	}
	
	/** close writer and delete saved data
	 */
	@Override
	public void close() throws IoxException {
		if(writer!=null){
			try {
				writer.close();
			} catch (IOException e) {
				throw new IoxException(e);
			}
			writer=null;
		}
		userDefined_Record_Delimiter=null;
		userDefined_Delimiter=null;
		headerAttrNames=null;
		userDefined_Header=null;
		currentDelimiter='\0';
		currentRecordDelimiter='\0';
		td=null;
		
	}
	
	@Override
	public void flush() throws IoxException {
	}
	
	@Override
	public void setFactory(IoxFactoryCollection factory) throws IoxException {
	}
	
	@Override
	public IoxFactoryCollection getFactory() throws IoxException {
		return null;
	}
	
	@Override
	public IomObject createIomObject(String type, String oid) throws IoxException {
		return null;
	}
	
	/** get delimiter of user set
	 * @return
	 */
	public String getDelimiter() {
		if(userDefined_Delimiter!=null){
			return userDefined_Delimiter;
		}
		return null;
	}
	
	/** user set delimiter
	 * @param delimiter
	 */
	public void setDelimiter(String delimiter) {
		CsvWriter.userDefined_Delimiter = delimiter;
	}
	
	/** get record delimiter
	 * @return
	 */
	public String getRecordDelimiter() {
		if(userDefined_Record_Delimiter!=null){
			return userDefined_Delimiter;
		}
		return null;
	}
	
	public void setRecordDelimiter(String recordDelimiter) {
		CsvWriter.userDefined_Record_Delimiter = recordDelimiter;
	}
	
	private static Boolean userDefinedHeaderIsSet() throws IoxException{
		if(userDefined_Header!=null){
			if(userDefined_Header.contains(HEADER_PRESENT)){
				return true;
			}else if(userDefined_Header.contains(HEADER_ABSENT)){
				return false;
			}else{
				throw new IoxException("expected present or absent, unexpected "+userDefined_Header);
			}
		}
		return false;
	}
	
	/**
	 * set header is present or header is absent.
	 * @param headerState
	 */
	public void setHeader(String headerState){
		CsvWriter.userDefined_Header = headerState;
	}
}