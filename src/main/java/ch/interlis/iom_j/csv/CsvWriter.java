package ch.interlis.iom_j.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ch.ehi.basics.settings.Settings;
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
	private TransferDescription td=null;
	// state
    private static final int START=0;
    private static final int INSIDE_LINEFEED=1;
    private static final int INSIDE_CARRIAGERETURN=2;
    private static final int END_LINEFEED=3;
    private static final int END_CARRIAGERETURN=4;
	// csv
	private static final char DEFAULT_VALUE_DELIMITER='\"';
	private static final char DEFAULT_VALUE_SEPARATOR=',';
	private static final char NEWLINE_CARRIAGERETURN='\r';
	private static final char NEWLINE_LINEFEED='\n';
	// writer
	private BufferedWriter writer=null;
	// defined by user
	private boolean doHeader=true;
	// current delimiter
	private Character currentValueDelimiter=DEFAULT_VALUE_DELIMITER;
	private char currentValueSeparator=DEFAULT_VALUE_SEPARATOR;
	// first line of file
	private boolean firstObj=true;
	private String[] headerAttrNames=null;

	/** create new CsvWriter
	 * @param file
	 * @throws IoxException
	 */
	public CsvWriter(File file)throws IoxException{
		this(file,null);
	}
	public CsvWriter(File file,Settings settings)throws IoxException{
		if(file!=null) {
			String encoding=null;
			if(settings!=null) {
				encoding=settings.getValue(CsvReader.ENCODING);
			}
			if(encoding==null) {
				encoding=Charset.defaultCharset().name();
			}
			try {
				writer=new BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(file),encoding));
			} catch (IOException e) {
				throw new IoxException("could not create file",e);
			}
		}
	}
	    
	/** find appropriate viewable in model/models
	 * @param iomObj
	 * @return
	 * @throws IoxException 
	 */
	private Viewable findViewable(IomObject iomObj) throws IoxException {
		List<Viewable>foundIliClasses=null;
		Viewable ret=null;
		String tag=iomObj.getobjecttag();
		String[] elements=tag.split("\\.");
		String viewable=elements[elements.length-1];
		// td is set
		if(td!=null) {
			foundIliClasses=new ArrayList<Viewable>();
			List<HashMap<String,Viewable>> allModels=setupNameMapping();
			for(HashMap<String,Viewable> map : allModels) {
				ret= map.get(viewable);
				if(ret!=null) {
					foundIliClasses.add(ret);
				}
			}
			if(foundIliClasses.size()>1) {
	    		throw new IoxException("several possible classes were found: "+foundIliClasses.toString());
	    	}else if(foundIliClasses.size()==1){
	    		return foundIliClasses.get(0);
	    	}
		}
		return null;
	}
	
	/** check attrvalues with attrnames
	 * @param viewable
	 * @param iomObj
	 * @return
	 */
	private String[] getAttributeNames(IomObject iomObj){
		// iliAttributes
		String[] attrs=new String[iomObj.getattrcount()];
		int count=0;
		for(int i=0;i<iomObj.getattrcount();i++) {
			String attribute=iomObj.getattrname(i);
			attrs[count]=attribute;
			count+=1;
		}
		java.util.Arrays.sort(attrs);
		return attrs;
	}
	private String[] getAttributeNames(Viewable viewable){
		// iliAttributes
		ArrayList<String> attrs=new ArrayList<String>();
		Iterator viewableIter=viewable.getAttributes();
		while(viewableIter.hasNext()) {
			Object attrObj=viewableIter.next();
			if(attrObj instanceof LocalAttribute) {
				LocalAttribute localAttr= (LocalAttribute)attrObj;
				String iliAttrName=localAttr.getName();
				attrs.add(iliAttrName);
			}
		}
		return attrs.toArray(new String[attrs.size()]);
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
		}else if(event instanceof ObjectEvent){
			ObjectEvent obj=(ObjectEvent) event;
			IomObject iomObj=(IomObject)obj.getIomObject();
			// first obj?
			if(firstObj) {
				// get list of attr names
				if(td!=null) {
					Viewable resultViewableHeader=findViewable(iomObj);
					if(resultViewableHeader==null) {
						throw new IoxException("class "+iomObj.getobjecttag()+" in model not found");
					}
		    		headerAttrNames=getAttributeNames(resultViewableHeader);
				}else {
					if(headerAttrNames==null) {
			    		headerAttrNames=getAttributeNames(iomObj);
					}
				}
	    		if(doHeader) {
					try {
						writeHeader(headerAttrNames);
					} catch (IOException e) {
						throw new IoxException(e);
					}
	    		}
	    		firstObj=false;
			}
        	String[] validAttrValues=getAttributeValues(headerAttrNames, iomObj);
        	try {
        		writeRecord(validAttrValues);
        	} catch (IOException e) {
				throw new IoxException(e);
        	}
        	
        	
		}else if(event instanceof EndBasketEvent){
		}else if(event instanceof EndTransferEvent){
			close();
		}else{
			throw new IoxException("unknown event type "+event.getClass().getName());
		}
	}
    
    /** get attribute values which are valid to write to file
     * @param attrNames
     * @param currentIomObject
     * @return
     */
    private String[] getAttributeValues(String[] attrNames, IomObject currentIomObject) {
    	String[] attrValues=new String[attrNames.length];
    	for (int i=0;i<attrNames.length;i++) {
    		String attrValue=currentIomObject.getattrvalue(attrNames[i]);
			attrValues[i]=attrValue;
    	}
    	return attrValues;
	}

    /** write header attribute names to file
     * @param attrNames
     * @throws IOException
     */
	private void writeHeader(String[] attrNames) throws IOException {
    	boolean firstName=true;
    	for (String name : attrNames) {
    		if(!firstName){
        		writer.write(currentValueSeparator);
        	}
    		firstName=false;
        	if(currentValueDelimiter!=null)writer.write(currentValueDelimiter);
            writer.write(escapequotes(name));
        	if(currentValueDelimiter!=null)writer.write(currentValueDelimiter);
        }
		writer.newLine();
	}

	/** replace value content of quote with csv format
	 * @param value
	 * @return
	 */
	private String escapequotes(String value) {
		if(value==null) {
			return "";
		}
		if(currentValueDelimiter==null) {
			return value;
		}
		String quote=currentValueDelimiter.toString();
        String result = value;
        if (result.contains(quote)) {
            result = result.replace(quote, quote+quote);
        }
        return result;
    }
	
	/** write each line in file
	 * @param attrValues
	 * @throws IOException
	 * @throws IoxException
	 */
    private void writeRecord(String[] attrValues) throws IOException, IoxException {
        boolean first = true;
        for (String value : attrValues){
            if (!first) {
                writer.write(currentValueSeparator);
            }
        	// iterate through each char
        	if(currentValueDelimiter!=null)writer.write(currentValueDelimiter);
        	String newValue=escapequotes(value);
            writeChars(newValue);
        	if(currentValueDelimiter!=null)writer.write(currentValueDelimiter);
            first = false;
        }
		writer.newLine();
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
		if(headerAttrNames!=null) {
			throw new IllegalStateException("attributes must not be set");
		}
		this.td=td;
	}
	public void setAttributes(String [] attr)
	{
		if(td!=null) {
			throw new IllegalStateException("ili-model must not be set");
		}
		headerAttrNames=attr.clone();
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
	public Character getValueDelimiter() {
		return currentValueDelimiter;
	}
	
	/** set value delimiter
	 * @param delimiter maybe null to write without delimiter
	 */
	public void setValueDelimiter(Character delimiter) {
		currentValueDelimiter = delimiter;
	}
	
	/** get separator
	 * @return
	 */
	public char getValueSeparator() {
		return currentValueSeparator;
	}
	public void setValueSeparator(char separator) {
		currentValueSeparator=separator;
	}
		
	/**
	 * set header is present or header is absent.
	 * @param headerState
	 */
	public void setWriteHeader(boolean headerState){
		doHeader = headerState;
	}
}