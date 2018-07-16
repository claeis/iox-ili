package ch.interlis.iom_j.xtf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import ch.interlis.ili2c.metamodel.AssociationDef;
import ch.interlis.ili2c.metamodel.Container;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iox.IoxReader;
import ch.interlis.iox_j.IoxIliReader;
import ch.interlis.iox_j.IoxSyntaxException;

public class Xtf23Reader implements IoxReader ,IoxIliReader{
	private XMLEventReader reader=null;
	private IoxFactoryCollection factory=new  ch.interlis.iox_j.DefaultIoxFactoryCollection();
	private java.io.InputStream inputFile=null;
	private int oidSpaceSize=0;
	private ArrayList<String> models=null;
	private int state = START;

	// state
	private static final int START=0;
	private static final int AFTER_STARTTRANSFER=1;
	private static final int AFTER_STARTBASKET=2;
	private static final int AFTER_OBJECT=3;
	private static final int AFTER_ENDBASKET=4;
	private static final int AFTER_ENDTRANSFER=5;
	private static final int END=6;
	
	// namespace
	private static final String NAMESPACE_ILIXMLBASE="http://www.interlis.ch/INTERLIS2.3";
    
    // segmentType
    private enum SegmentType {C1,C2,C3,A1,A2,R}

    // modelnames
    private String currentModelName=null;
    private String currentTopicName=null;
    private String currentViewableName=null;

    // headersection
    private static final String INTERLIS_VERSION_23 = "2.3";
    private static final QName QNAME_XML_HEADERSECTION = new QName(NAMESPACE_ILIXMLBASE, "HEADERSECTION");
    private static final QName QNAME_XML_HEADERSECTION_SENDER = new QName("SENDER");
    private static final QName QNAME_XML_HEADERSECTION_VERSION = new QName("VERSION");
    private static final QName QNAME_XML_MODELS = new QName(NAMESPACE_ILIXMLBASE, "MODELS");
    private static final QName QNAME_XML_MODEL = new QName(NAMESPACE_ILIXMLBASE, "MODEL");
    private static final QName QNAME_XML_MODEL_NAME = new QName("NAME");
    private static final QName QNAME_XML_MODEL_VERSION = new QName("VERSION");
    private static final QName QNAME_XML_MODEL_URI = new QName("URI");
    private static final QName QNAME_XML_OIDSPACES = new QName(NAMESPACE_ILIXMLBASE, "OIDSPACES");
    private static final QName QNAME_XML_OIDSPACE = new QName(NAMESPACE_ILIXMLBASE, "OIDSPACE");
    private static final QName QNAME_XML_OIDSPACE_OIDDOMAIN = new QName("OIDDOMAIN");
    private static final QName QNAME_XML_OIDSPACE_NAME = new QName("NAME");
    private static final QName QNAME_XML_ENTRIES = new QName(NAMESPACE_ILIXMLBASE, "ENTRIES");
    private static final QName QNAME_XML_COMMENT = new QName(NAMESPACE_ILIXMLBASE, "COMMENT");
    private static final QName QNAME_XML_ALIAS = new QName(NAMESPACE_ILIXMLBASE, "ALIAS");
    private static final QName QNAME_XML_ENTRIES_TAGENTRY = new QName(NAMESPACE_ILIXMLBASE, "TAGENTRY");
    private static final QName QNAME_XML_ENTRIES_VALENTRY = new QName(NAMESPACE_ILIXMLBASE, "VALENTRY");
    private static final QName QNAME_XML_ENTRIES_DELENTRY = new QName(NAMESPACE_ILIXMLBASE, "DELENTRY");
    private static final QName QNAME_XML_ENTRIES_VALUE_ATTR = new QName("ATTR");
    private static final QName QNAME_XML_ENTRIES_VALUE_FROM = new QName("FROM");
    private static final QName QNAME_XML_ENTRIES_VALUE_TO = new QName("TO");
    private static final QName QNAME_XML_ENTRIES_VALUE_TAG = new QName("TAG");
    private static final QName QNAME_XML_ENTRIES_VALUE_FOR = new QName("FOR");
    
    // topic
    private static final QName QNAME_XML_TOPIC_BID = new QName("BID");
    private static final QName QNAME_XML_TOPIC_KIND=new QName("KIND");
    private static final QName QNAME_XML_TOPIC_KIND_FULL = new QName(NAMESPACE_ILIXMLBASE, "FULL");
    private static final QName QNAME_XML_TOPIC_KIND_UPDATE = new QName(NAMESPACE_ILIXMLBASE, "UPDATE");
    private static final QName QNAME_XML_TOPIC_KIND_INITIAL = new QName(NAMESPACE_ILIXMLBASE, "INITIAL");
    private static final QName QNAME_XML_TOPIC_STARTSTATE=new QName("STARTSTATE");
    private static final QName QNAME_XML_TOPIC_ENDSTATE=new QName("ENDSTATE");
    private static final QName QNAME_XML_TOPIC_CONSISTENCY=new QName("CONSISTENCY");
    private static final QName QNAME_XML_TOPIC_CONSISTENCY_COMPLETE = new QName(NAMESPACE_ILIXMLBASE, "COMPLETE");
    private static final QName QNAME_XML_TOPIC_CONSISTENCY_INCOMPLETE = new QName(NAMESPACE_ILIXMLBASE, "INCOMPLETE");
    private static final QName QNAME_XML_TOPIC_CONSISTENCY_INCONSISTENT = new QName(NAMESPACE_ILIXMLBASE, "INCONSISTENT");
    private static final QName QNAME_XML_TOPIC_CONSISTENCY_ADAPTED = new QName(NAMESPACE_ILIXMLBASE, "ADAPTED");
    private static final QName QNAME_XML_DATASECTION = new QName(NAMESPACE_ILIXMLBASE, "DATASECTION");
    // object
    private static final QName QNAME_XML_CLASS_TID = new QName("TID");
    private static final QName QNAME_XML_CLASS_OPERATION = new QName("OPERATION");   
    // others
    private static final QName QNAME_XML_ORDERPOS = new QName("ORDER_POS");
    private static final QName QNAME_XML_TRANSFER=new QName(NAMESPACE_ILIXMLBASE,"TRANSFER");
    private static final QName QNAME_XML_REF = new QName("REF");
    private static final QName QNAME_XML_DOMAIN=new QName("DOMAINS");
    private static final QName QNAME_XML_INSERT = new QName(NAMESPACE_ILIXMLBASE, "INSERT");
    private static final QName QNAME_XML_DELETE=new QName(NAMESPACE_ILIXMLBASE,"DELETE");
    // interlis types
    private static final QName QNAME_XML_BINBLBOX = new QName(NAMESPACE_ILIXMLBASE, "BINBLBOX");
    private static final QName QNAME_XML_XMLBLBOX = new QName(NAMESPACE_ILIXMLBASE, "XMLBLBOX");
    private static final QName QNAME_XML_COORD = new QName(NAMESPACE_ILIXMLBASE, "COORD");
    private static final QName QNAME_XML_ARC = new QName(NAMESPACE_ILIXMLBASE, "ARC");
    private static final QName QNAME_XML_POLYLINE = new QName(NAMESPACE_ILIXMLBASE, "POLYLINE");
    private static final QName QNAME_XML_MULTIPOLYLINE = new QName(NAMESPACE_ILIXMLBASE, "MULTIPOLYLINE");
    private static final QName QNAME_XML_BOUNDARY = new QName(NAMESPACE_ILIXMLBASE, "BOUNDARY");
    private static final QName QNAME_XML_SURFACE = new QName(NAMESPACE_ILIXMLBASE, "SURFACE");
    private static final QName QNAME_XML_MULTISURFACE = new QName(NAMESPACE_ILIXMLBASE, "MULTISURFACE");
    private static final QName QNAME_XML_AREA = new QName(NAMESPACE_ILIXMLBASE, "AREA");
    private static final QName QNAME_XML_MULTIAREA = new QName(NAMESPACE_ILIXMLBASE, "MULTIAREA");
    private static final QName QNAME_XML_SEGMENTS = new QName(NAMESPACE_ILIXMLBASE, "SEGMENTS");
    private static final QName QNAME_XML_SEGMENTS_COORD = new QName(NAMESPACE_ILIXMLBASE, "COORD");
    private static final QName QNAME_XML_SEGMENTS_ARC = new QName(NAMESPACE_ILIXMLBASE, "ARC");
    private static final QName QNAME_XML_SEGMENT_C1 = new QName(NAMESPACE_ILIXMLBASE, "C1");
    private static final QName QNAME_XML_SEGMENT_C2 = new QName(NAMESPACE_ILIXMLBASE, "C2");
    private static final QName QNAME_XML_SEGMENT_C3 = new QName(NAMESPACE_ILIXMLBASE, "C3");
    private static final QName QNAME_XML_SEGMENT_A1 = new QName(NAMESPACE_ILIXMLBASE, "A1");
    private static final QName QNAME_XML_SEGMENT_A2 = new QName(NAMESPACE_ILIXMLBASE, "A2");
    private static final QName QNAME_XML_SEGMENT_R = new QName(NAMESPACE_ILIXMLBASE, "R");
        
    /** Creates a new XTF23 reader.
     * @param in
     * @throws IoxException
     */
	public Xtf23Reader(java.io.InputStream in) throws IoxException{
		init(in);
	}
	
	/** Creates a new XTF23 reader.
	 * @param in Input reader to read from
	 * @throws IoxException
	 */
	public Xtf23Reader(java.io.InputStreamReader in) throws IoxException{
	}
	
	/** Creates a new XTF23 reader.
	 * @param xtffile File to read from
	 * @throws IoxException
	 */
	public Xtf23Reader(java.io.File xtffile) throws IoxException{
		try{
			inputFile=new java.io.FileInputStream(xtffile);
			init(inputFile);
		}catch(java.io.IOException ex){
			throw new IoxException(ex);
		}
	}
	
	/** Initialize reader.
	 * @param in
	 * @throws IoxException
	 */
	private void init(java.io.InputStream in) throws IoxException{
		javax.xml.stream.XMLInputFactory inputFactory = javax.xml.stream.XMLInputFactory.newInstance();
		try{
			reader=inputFactory.createXMLEventReader(in);
		}catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
	}

	@Override
	public void close() throws IoxException {
		reader=null;
		if(inputFile!=null){
			try{
				inputFile.close();
			}catch(java.io.IOException ex){
				throw new IoxException(ex);
			}
			inputFile=null;
		}
	}

	@Override
	public IoxEvent read() throws IoxException {
		try {
			while(reader.hasNext()){
				javax.xml.stream.events.XMLEvent event=null;
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
				if(state==START){
					// start document
					if(event.isStartDocument()){
						event=reader.nextEvent();
						event=skipSpacesAndGetNextEvent(event);
					}else {
						throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
					}
					// start transfer
					if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_TRANSFER)){
						event=reader.nextEvent();
						event=skipSpacesAndGetNextEvent(event);
	                }else {
	                	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	                }
	            	// header section
	                if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_HEADERSECTION)){
						event=readHeaderSection(event);
	                }else {
	                	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	                }
	                // data section
	                XtfStartTransferEvent ret=null;
	                if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_DATASECTION)){
	                	HashMap<String,IomObject> modelx=new HashMap<String,IomObject>();
		        		ret=new XtfStartTransferEvent();
		        		for(String modelName:models){
		                    IomObject model=createIomObject(ch.interlis.iom_j.xtf.impl.MyHandler.HEADER_OBJECT_MODELENTRY,hsNextOid());
		        			model.setattrvalue(ch.interlis.iom_j.xtf.impl.MyHandler.HEADER_OBJECT_MODELENTRY_NAME,modelName);
		        			modelx.put(model.getobjectoid(),model);
		        		}
		        		ret.setHeaderObjects(modelx);
	                }else {
	                	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	                }
	                state=AFTER_STARTTRANSFER;
	                return ret;
	            }
				if(state==AFTER_STARTTRANSFER) {
					// start basket
			        if(event.isStartElement()){
			        	StartElement element = (StartElement) event;
						currentModelName=getModelName(element.asStartElement().getName().getLocalPart());
						if(currentModelName==null) {
							throw new IoxException("missing model name");
						}
						currentTopicName=getTopicNameOfModel(element.asStartElement().getName().getLocalPart());
						if(currentTopicName==null) {
							throw new IoxException("missing topic name");
						}
			        	QName basketId = QNAME_XML_TOPIC_BID;
			        	Attribute bid = element.getAttributeByName(basketId);
		        		ch.interlis.iox_j.StartBasketEvent newObj=new ch.interlis.iox_j.StartBasketEvent(currentModelName+"."+currentTopicName, bid.getValue());
		        		ch.interlis.iox_j.StartBasketEvent bidObj=setBasketCodingContent(element, newObj);
		        		state=AFTER_STARTBASKET;
		        		return bidObj;
			        }else if(event.isEndElement()){
			        	state=AFTER_ENDBASKET;
		        	}else{
		        		throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		        	}
				}
				
				// after start basket
				if(state==AFTER_STARTBASKET) {
					if(event.isStartElement()) {
						IoxEvent ret=readObject(event); // start object
						return ret;
					}else if(event.isEndElement()) {
						if(event.asEndElement().getName().equals(new QName(NAMESPACE_ILIXMLBASE, currentModelName+"."+currentTopicName))){
							// return end basket
							state=AFTER_STARTTRANSFER;
				        	return new ch.interlis.iox_j.EndBasketEvent();
						}
					}
				}
				
				// after object
				if(state==AFTER_OBJECT) {
					if(event.isStartElement()){
			        	// start object
			        	state=AFTER_STARTBASKET;
			        	continue;
			        }else if(event.isEndElement()){
			        	if(event.asEndElement().getName().equals(new QName(NAMESPACE_ILIXMLBASE, currentModelName+"."+currentTopicName))){
				        	event=reader.nextEvent(); // close end basket
							event=skipSpacesAndGetNextEvent(event);
							state=AFTER_ENDBASKET;
				        	return new ch.interlis.iox_j.EndBasketEvent();
						}
			        }
				}
				// after end basket
				if(state==AFTER_ENDBASKET) {
			    	if(event.isStartElement()){
			        	// after start transfer
			        	state=AFTER_STARTTRANSFER;
			        	continue;
			        }else if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_DATASECTION)){
			        	event=reader.nextEvent(); // close end datasection
						event=skipSpacesAndGetNextEvent(event);
			        	// end transfer
						state=AFTER_ENDTRANSFER;
			        }
				}
				// after end transfer
				if(state==AFTER_ENDTRANSFER) {
			    	if(event.isEndElement()){
			        	// end transfer
			        	if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_TRANSFER)){
							event=reader.nextEvent(); // close end transfer
							event=skipSpacesAndGetNextEvent(event);
							state=END;
			        	}else {
			        		throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			        	}
			    	}else {
		        		throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		        	}
				}
	            // end
				if(state==END){
					if(event.isEndDocument()){
						return new ch.interlis.iox_j.EndTransferEvent();
	                }else {
	                	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	                }
	            }
			}
		}catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
		return null;
	}
	
	private String getModelName(String scopeName) {
		String[] parts=scopeName.split("\\.");
		Object obj1=parts[0];
		return obj1.toString();
	}
	private String getTopicNameOfModel(String scopeName) {
		String[] parts=scopeName.split("\\.");
		Object obj1=parts[1];
		return obj1.toString();
	}
	private String getViewableFromQName(QName scopeName) {
		String[] parts=scopeName.getLocalPart().split("\\.");
		Object obj1=parts[2];
		return obj1.toString();
	}

	private XMLEvent readHeaderSection(XMLEvent startElementHeaderSection) throws XMLStreamException, IoxException{
		// start header section
		Attribute version=startElementHeaderSection.asStartElement().getAttributeByName(QNAME_XML_HEADERSECTION_VERSION);
        if(version!=null && version.getValue().equals(INTERLIS_VERSION_23)){
        	// valid
        }else {
        	throw new IoxSyntaxException(unexpectedXmlEvent2msg(startElementHeaderSection));
        }
    	Attribute sender=startElementHeaderSection.asStartElement().getAttributeByName(QNAME_XML_HEADERSECTION_SENDER);
        if(sender!=null){
        	// valid
        }else {
        	throw new IoxSyntaxException(unexpectedXmlEvent2msg(startElementHeaderSection));
        }
		XMLEvent event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
		
		// start models
		if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_MODELS)){
			event=readHeaderSectionModels(event);
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		
		if(event.isEndElement()) {
			// end header section
			if(event.asEndElement().getName().equals(QNAME_XML_HEADERSECTION)){
				if(models.size()==0) {
					throw new IoxException("expected at least 1 model.");
				}
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
				return event;
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
		}else if(event.isStartElement()) {
			// start alias
			if(event.asStartElement().getName().equals(QNAME_XML_ALIAS)){
				event=readHeaderSectionAlias(event);
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		
		if(event.isEndElement()) {
			// end header section
			if(event.asEndElement().getName().equals(QNAME_XML_HEADERSECTION)){
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
				return event;
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
		}else if(event.isStartElement()) {
			// start oid spaces
			if(event.asStartElement().getName().equals(QNAME_XML_OIDSPACES)){
				event=readHeaderSectionOidSpaces(event);
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		
		// end header section
		if(event.isEndElement()) {
			if(event.asEndElement().getName().equals(QNAME_XML_HEADERSECTION)){
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
				return event;
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
		}else if(event.isStartElement()) {
			// start comment
			if(event.asStartElement().getName().equals(QNAME_XML_COMMENT)){
				event=readHeaderSectionComment(event);
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		
		if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_HEADERSECTION)){
			event=reader.nextEvent();
			event=skipSpacesAndGetNextEvent(event);
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		return event;
	}
	
	private XMLEvent readHeaderSectionModels(XMLEvent startElementModels) throws IoxException, XMLStreamException {
		XMLEvent event=null;
		// start models
		if(startElementModels.isStartElement() && startElementModels.asStartElement().getName().equals(QNAME_XML_MODELS)){
			event=reader.nextEvent();
			event=skipSpacesAndGetNextEvent(event);
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		models=new ArrayList<String>();
		
		// start model
		while(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_MODEL)) {
			event=readModel(event);
		}
		
		// end Model(s)
		if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_MODELS)) {
			if(models.size()==0) {
				throw new IoxException("expected at least 1 model.");
			}
			event=reader.nextEvent();
			event=skipSpacesAndGetNextEvent(event);
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		return event;
	}
	
	private XMLEvent readModel(XMLEvent startElementModel) throws IoxException, XMLStreamException {
		XMLEvent event=null;
		// start model name
		Attribute name=startElementModel.asStartElement().getAttributeByName(QNAME_XML_MODEL_NAME);
		Attribute version=startElementModel.asStartElement().getAttributeByName(QNAME_XML_MODEL_VERSION);
		Attribute uri=startElementModel.asStartElement().getAttributeByName(QNAME_XML_MODEL_URI);
        if(name!=null && version!=null && uri!=null){
        	// add model to models
        	models.add(startElementModel.asStartElement().getAttributeByName(QNAME_XML_MODEL_NAME).getValue());
        	event=reader.nextEvent();
        	event=skipSpacesAndGetNextEvent(event);
        }else {
        	throw new IoxSyntaxException(unexpectedXmlEvent2msg(startElementModel));
        }
        
        // end element Model
 		if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_MODEL)) {
 			event=reader.nextEvent();
        	event=skipSpacesAndGetNextEvent(event);      	
 		}else {
 			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event)); 
 		}
 		return event;
	}
	
	private XMLEvent readHeaderSectionAlias(XMLEvent startElementAlias) throws IoxException, XMLStreamException {
		XMLEvent event=null;
		// start alias
		if(startElementAlias.isStartElement() && startElementAlias.asStartElement().getName().equals(QNAME_XML_ALIAS)){
			event=reader.nextEvent();
			event=skipSpacesAndGetNextEvent(event);
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		
		// end alias
		if(event.isEndElement()) {
			if(event.asEndElement().getName().equals(QNAME_XML_ALIAS)) {
				return event;
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
		}	
		
		// start entries
    	if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_ENTRIES)) {
    		while(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_ENTRIES)) {
	    		event = readAliasEntries(event);
    		}
    	}else{
    		throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
    	}
    	
    	// end alias
		if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_ALIAS)) {
			event=reader.nextEvent();
    		event=skipSpacesAndGetNextEvent(event);
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		return event;
	}

	private XMLEvent readAliasEntries(XMLEvent startElementAliasEntries) throws IoxSyntaxException, XMLStreamException, IoxException {
		XMLEvent event=null;
		// start entries
		Attribute entryFor=startElementAliasEntries.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_FOR);
    	if(entryFor!=null){
    		event=reader.nextEvent();
    		event=skipSpacesAndGetNextEvent(event);
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(startElementAliasEntries));
		}
		
		// end entries
		if(event.isEndElement()) {
			if(event.asEndElement().getName().equals(QNAME_XML_ENTRIES)) {
				event=reader.nextEvent();
	    		event=skipSpacesAndGetNextEvent(event);
				return event;
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
		}
		
		// start entry
		while(event.isStartElement() &&
			 (event.asStartElement().getName().equals(QNAME_XML_ENTRIES_TAGENTRY) ||
			  event.asStartElement().getName().equals(QNAME_XML_ENTRIES_VALENTRY) ||
			  event.asStartElement().getName().equals(QNAME_XML_ENTRIES_DELENTRY))){
			event=readAliasEntry(event);
		}
		
		// end entries
		if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_ENTRIES)){
			event=reader.nextEvent();
			event=skipSpacesAndGetNextEvent(event);
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		return event;
	}

	private XMLEvent readAliasEntry(XMLEvent startElementAliasEntry) throws IoxSyntaxException, IoxException, XMLStreamException {
		XMLEvent event=null;
		if(startElementAliasEntry.isStartElement() && startElementAliasEntry.asStartElement().getName().equals(QNAME_XML_ENTRIES_TAGENTRY)){
			// start tag entry
			if(startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_FROM)!=null &&
				startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_TO)!=null){
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
			// end tag entry
			if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_ENTRIES_TAGENTRY)){
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
		}else if(startElementAliasEntry.isStartElement() && startElementAliasEntry.asStartElement().getName().equals(QNAME_XML_ENTRIES_VALENTRY)){
			// start val entry
			if(startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_TAG)!=null &&
					startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_ATTR)!=null &&
					startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_FROM)!=null &&
					startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_TO)!=null){
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(startElementAliasEntry));
			}
			
			// end val entry
			if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_ENTRIES_VALENTRY)){
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
		}else if(startElementAliasEntry.isStartElement() && startElementAliasEntry.asStartElement().getName().equals(QNAME_XML_ENTRIES_DELENTRY)){
			// start del entry
			if(startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_TAG)!=null) {
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(startElementAliasEntry));
			}
			
			// end del entry
			if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_ENTRIES_DELENTRY)){
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
		}
		return event;
	}
	
	private XMLEvent readHeaderSectionOidSpaces(XMLEvent startElementOidSpaces) throws XMLStreamException, IoxException {
		// start oid space(s)
		XMLEvent event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
		
		// oid space
		if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_OIDSPACE)) {
			while(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_OIDSPACE)) {
				event=readOidSpace(event);
			}
			if(oidSpaceSize==0) {
				throw new IoxException("expected at least 1 oid space");
			}
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		
		// end oid space(s)
		if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_OIDSPACES)) {
			event=reader.nextEvent();
			event=skipSpacesAndGetNextEvent(event);
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));  
		}
		return event;
	}

	private XMLEvent readOidSpace(XMLEvent startElementOidSpace) throws IoxSyntaxException, IoxException, XMLStreamException {
		XMLEvent event=null;
		// start oid space
		if(startElementOidSpace.asStartElement().getAttributeByName(QNAME_XML_OIDSPACE_NAME)!=null ||
		   startElementOidSpace.asStartElement().getAttributeByName(QNAME_XML_OIDSPACE_OIDDOMAIN)!=null){
			oidSpaceSize+=1;
			event=reader.nextEvent();
			event=skipSpacesAndGetNextEvent(event);
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(startElementOidSpace));
		}
		
		// end oid space
		if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_OIDSPACE)){
			event=reader.nextEvent();
			event=skipSpacesAndGetNextEvent(event);
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		return event;
	}
	
	private XMLEvent readHeaderSectionComment(XMLEvent startElementComment) throws IoxSyntaxException, XMLStreamException {
		// start comment
		XMLEvent event=reader.nextEvent();
		event=skipCommentary(event);
		StringBuffer value=new StringBuffer();
		event=readSimpleContent(event,value);
		// end comment
		if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_COMMENT)) {
			event=reader.nextEvent();
			event=skipSpacesAndGetNextEvent(event);
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		return event;
	}

	private XMLEvent skipCommentary(XMLEvent event) throws IoxSyntaxException, XMLStreamException {
		 while(event.getEventType()==XMLEvent.COMMENT){
			 event=reader.nextEvent();
	     }
	     return event;
	}

	private IoxEvent readObject(XMLEvent event) throws IoxException {
		try {
			IomObject iomObj=null;
			while(reader.hasNext() && state!=AFTER_OBJECT){
		    	if(event.isStartElement()){
		    		if(event.asStartElement().getName().equals(QNAME_XML_DELETE)){
		    			// delete Object
		    			return deleteObject(event);
		    		}
		    		// create viewable
		            Attribute oid = event.asStartElement().getAttributeByName(QNAME_XML_CLASS_TID);
		            if(event.asStartElement().getAttributeByName(QNAME_XML_TOPIC_BID) != null){
		            	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		            }
		            currentViewableName=getViewableFromQName(event.asStartElement().getName());
		            if(currentViewableName==null) {
		            	throw new IoxException("missing table name");
		            }
		            // create object
		        	try{
		        		if(oid!=null){
		        			iomObj=createIomObject(currentModelName+"."+currentTopicName+"."+currentViewableName, oid.getValue());
		        		}else{
		        			iomObj=createIomObject(currentModelName+"."+currentTopicName+"."+currentViewableName, null);
		        		}
		        	}catch(IoxSyntaxException ioxEx){
		        		throw ioxEx;
		        	}
	        		// read object attributes
	        		iomObj=readAttribute(event, currentViewableName, iomObj);
	        		// set additional infos
					setAdditionalObjectInfo(event.asStartElement(), iomObj);
					event=reader.nextEvent(); // <object>
                	return new ch.interlis.iox_j.ObjectEvent(iomObj);
		        }else if(event.isEndElement()){
		        	event=skipSpacesAndGetNextEvent(event);
		        	continue;
		        }else if(event.isCharacters()){
		        	event=skipSpacesAndGetNextEvent(event);
		        	continue;
		        }
		    }
		}catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
		return null;
	}

	private IoxEvent deleteObject(XMLEvent event) throws IoxException {
		IomObject objToDelete=null;
		try {
			if(event.asStartElement().getAttributeByName(QNAME_XML_CLASS_TID)!=null){
				objToDelete=createIomObject(QNAME_XML_DELETE.getLocalPart(), event.asStartElement().getAttributeByName(QNAME_XML_CLASS_TID).getValue());
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
				if(event.isEndElement()){
		    		state=AFTER_STARTBASKET;
		    		if(!event.asEndElement().getName().equals(QNAME_XML_DELETE)){
		    			throw new IoxException("expected rolename and role reference tid");
		    		}
		    	}else{
		    		throw new IoxException("delete references are not yet implemented.");
		    	}
			}else{
				throw new IoxException("delete object needs tid");
			}
		}catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
		return new ch.interlis.iox_j.ObjectEvent(objToDelete);
	}

	private IomObject setAdditionalObjectInfo(StartElement element, IomObject iomObj) throws IoxException {
		Attribute operation = element.getAttributeByName(QNAME_XML_CLASS_OPERATION);
        if(operation!=null){
        	String attrValue=operation.getValue();
        	if(attrValue.equals(QNAME_XML_INSERT.getLocalPart())){
        		iomObj.setobjectoperation(IomConstants.IOM_OP_INSERT); // op(0=insert)
        	}else if(attrValue.equals(QNAME_XML_TOPIC_KIND_UPDATE.getLocalPart())){
        		iomObj.setobjectoperation(IomConstants.IOM_OP_UPDATE); // op(1=update)
        	}else if(attrValue.equals(QNAME_XML_DELETE.getLocalPart())){
        		iomObj.setobjectoperation(IomConstants.IOM_OP_DELETE); // op(2=delete)
        	}else{
				throw new IoxException("unexpected operation <"+attrValue+">");
        	}
        }
        Attribute consistency = element.getAttributeByName(QNAME_XML_TOPIC_CONSISTENCY);
        if(consistency!=null){
        	String attrValue=consistency.getValue();
        	if(attrValue.equals(QNAME_XML_TOPIC_CONSISTENCY_COMPLETE.getLocalPart())){
        		iomObj.setobjectoperation(IomConstants.IOM_COMPLETE); // 0=COMPLETE
        	}else if(attrValue.equals(QNAME_XML_TOPIC_CONSISTENCY_INCOMPLETE.getLocalPart())){
        		iomObj.setobjectoperation(IomConstants.IOM_INCOMPLETE); // 1=INCOMPLETE
        	}else if(attrValue.equals(QNAME_XML_TOPIC_CONSISTENCY_INCONSISTENT.getLocalPart())){
        		iomObj.setobjectoperation(IomConstants.IOM_INCONSISTENT); // 2=INCONSISTENT
        	}else if(attrValue.equals(QNAME_XML_TOPIC_CONSISTENCY_ADAPTED.getLocalPart())){
        		iomObj.setobjectoperation(IomConstants.IOM_ADAPTED); // 3=ADAPTED
        	}else{
				throw new IoxException("unexpected consistency <"+attrValue+">");
        	}
        }
        return iomObj;
	}

	private ch.interlis.iox_j.StartBasketEvent setBasketCodingContent(StartElement element, ch.interlis.iox_j.StartBasketEvent startBasketEvent) throws IoxException {
		Iterator codingObjIter=element.getAttributes();
		String[] genericAndConcreteDomains=null;
		while(codingObjIter.hasNext()){
			Attribute codingObj=(Attribute) codingObjIter.next();
			if(codingObj.getName().equals(QNAME_XML_TOPIC_STARTSTATE)){
				startBasketEvent.setStartstate(codingObj.getValue());
			}else if(codingObj.getName().equals(QNAME_XML_TOPIC_ENDSTATE)){
				startBasketEvent.setEndstate(codingObj.getValue());
			}else if(codingObj.getName().equals(QNAME_XML_TOPIC_KIND)){
				if(codingObj.getValue().equals(QNAME_XML_TOPIC_KIND_FULL.getLocalPart())){
					startBasketEvent.setKind(IomConstants.IOM_FULL);
				}else if(codingObj.getValue().equals(QNAME_XML_TOPIC_KIND_UPDATE.getLocalPart())){
					startBasketEvent.setKind(IomConstants.IOM_UPDATE);
				}else if(codingObj.getValue().equals(QNAME_XML_TOPIC_KIND_INITIAL.getLocalPart())){
					startBasketEvent.setKind(IomConstants.IOM_INITIAL);
				}
			}else if(codingObj.getName().equals(QNAME_XML_DOMAIN)) { //QNAME_XML_TOPIC_TOPICS
				String domainValue=codingObj.getValue();
				genericAndConcreteDomains=domainValue.split("\\,"); // genericAndConcreteDomains.getValues(genericDomain=concreteDomain)
				for(String singleDomain : genericAndConcreteDomains){
					startBasketEvent.addDomain(singleDomain, element.getName().getLocalPart());
				}
			}else if(codingObj.getName().equals(QNAME_XML_TOPIC_CONSISTENCY)){
				if(codingObj.getValue().equals(QNAME_XML_TOPIC_CONSISTENCY_COMPLETE.getLocalPart())){
					startBasketEvent.setConsistency(IomConstants.IOM_COMPLETE); // 0=COMPLETE
				}else if(codingObj.getValue().equals(QNAME_XML_TOPIC_CONSISTENCY_INCOMPLETE.getLocalPart())){
					startBasketEvent.setConsistency(IomConstants.IOM_INCOMPLETE); // 1=INCOMPLETE
				}else if(codingObj.getValue().equals(QNAME_XML_TOPIC_CONSISTENCY_INCONSISTENT.getLocalPart())){
					startBasketEvent.setConsistency(IomConstants.IOM_INCONSISTENT); // 2=INCONSISTENT
				}else if(codingObj.getValue().equals(QNAME_XML_TOPIC_CONSISTENCY_ADAPTED.getLocalPart())){
					startBasketEvent.setConsistency(IomConstants.IOM_ADAPTED); // 3=ADAPTED
				}else {
					throw new IoxException("unexpected consistency <"+codingObj.getValue()+">");
				}
			}
		}
		return startBasketEvent;
	}

	private static String collectXMLElement(XMLEventReader reader, XMLEvent event) throws XMLStreamException {
        XMLOutputFactory xmloutputf = XMLOutputFactory.newInstance();
        xmloutputf.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES,true);
        XMLEventFactory xmlef = XMLEventFactory.newInstance();
        java.io.StringWriter strw=new java.io.StringWriter();
        XMLEventWriter xmlw = xmloutputf.createXMLEventWriter(strw); 
        xmlw.add(xmlef.createStartDocument());
        xmlw.add(event);
        int inHeader = 1;
        while(reader.hasNext()){
            event = reader.nextEvent();
            xmlw.add(event);
            switch (event.getEventType()) {
	            case XMLStreamConstants.START_ELEMENT:
	                inHeader++;
	                break;
	            case XMLStreamConstants.END_ELEMENT:
	                inHeader--;
	                break;
	            } // end switch
            if(inHeader==0 && event.getEventType()==XMLStreamConstants.END_ELEMENT){
                break;
            }
            if (inHeader < 0) {
                throw new IllegalStateException("inHeader < 0");
            }
        } // end while
        xmlw.add(xmlef.createEndDocument());
        xmlw.flush();
        xmlw.close();
        return strw.toString();
    }
	
	private XMLEvent readSimpleContent(XMLEvent event,StringBuffer value) throws XMLStreamException, IoxSyntaxException {
        while(event.isCharacters()){
            Characters characters = (Characters) event;
            value.append(characters.getData());
            event=reader.nextEvent();
        }
        return event;
    }
	
	/** Read Characters of characterEvent and check if whitespace. Return nextEvent()
	 * @param event
	 * @return XMLEvent event
	 * @throws XMLStreamException
	 * @throws IoxSyntaxException
	 */
	private XMLEvent skipSpacesAndGetNextEvent(XMLEvent event) throws XMLStreamException, IoxSyntaxException {
        while(event.isCharacters() || event.getEventType()==XMLEvent.COMMENT){
        	if(event.isCharacters()) {
	            Characters characters = (Characters) event;
	            if(!characters.isWhiteSpace()){
					throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	            }
        	}
            event=reader.nextEvent();
        }
        return event;
    }
	
	/** Return appropriate error message
	 * @param event
	 * @return String output
	 */
	private String unexpectedXmlEvent2msg(XMLEvent actual){
		StringBuilder output=new StringBuilder();
		output.append("Unexpected XML event ");
		if(actual instanceof StartElement){
			output.append(actual.asStartElement().getName().getLocalPart());
		}else if(actual instanceof EndElement){
			output.append(actual.asEndElement().getName().getLocalPart());
		}else if(actual instanceof Characters){
			output.append(actual.asCharacters().getData());
		}
		output.append(" found.");
		return output.toString();
	}
    
    private IomObject readAttribute(XMLEvent event, String aClass, IomObject iomObj) throws IoxException {
    	try {
	    	String attrName=null;
	    	while(reader.hasNext()){
	            try{
	                event=reader.nextEvent();
	                event=skipSpacesAndGetNextEvent(event);
	            }catch(javax.xml.stream.XMLStreamException ex){
	                throw new IoxException(ex);
	            }
	            if(attrName!=null){
	                throw new IllegalStateException("attrName != null");
	            }else {
	            	if(event.isStartElement()){
		            	StartElement element = (StartElement) event;
		            	attrName=element.getName().getLocalPart(); // <set attribute name>
		            	XMLEvent peekEvent2=reader.peek();
		            	if(!peekEvent2.isEndElement()){
		            		event=reader.nextEvent();
		            	}
		                XMLEvent peekEvent=reader.peek();
		                if(!peekEvent.isEndElement()){
		                	event=skipSpacesAndGetNextEvent(event);
		                }
	            	}
	            }
	            if(event.isCharacters()){
	            	Characters character = (Characters) event;
	            	String characterValue=character.getData();
	            	iomObj.setattrvalue(attrName, characterValue);
	            	event=reader.nextEvent(); // <close attribute value>
	            	if(event.isEndElement()){
	            		attrName=null;
	            		continue;
	            	}else{
	            		throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	            	}
	            }else if(event.isStartElement()) {
	                if(event.isStartElement() && (event.asStartElement().getName().equals(QNAME_XML_COORD) || event.asStartElement().getName().equals(QNAME_XML_ARC))){
	                	String segmentType=null;
	                	if(event.asStartElement().getName().equals(QNAME_XML_COORD)) {
	                    	segmentType=QNAME_XML_SEGMENTS_COORD.getLocalPart();
	                    }else if(event.asStartElement().getName().equals(QNAME_XML_ARC)) {
	                    	segmentType=QNAME_XML_SEGMENTS_ARC.getLocalPart();
	                    }
	                	event=reader.nextEvent(); // COORD
	                	event=skipSpacesAndGetNextEvent(event);
	                    if(!event.isStartElement()){
	                    	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	                    }
	                    IomObject returnedSegment=readSegment(event, segmentType);
	                    if(returnedSegment.getattrcount()==0){
	                    	throw new IoxException("expected coord. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
						iomObj.addattrobj(attrName, returnedSegment);
	                    event=reader.nextEvent(); // <coord>
	                    event=skipSpacesAndGetNextEvent(event);
	                    if(event.isEndElement()){
		            		attrName=null;
		            		continue;
		            	}else{
		            		throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		            	}
	                }else if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_POLYLINE)){
	                    IomObject polyline = readPolyline(event);
	                    if(polyline.getattrcount()==0){
	                    	throw new IoxException("expected polyline. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
	                    iomObj.addattrobj(attrName, polyline);
	                    event=reader.nextEvent(); // <polyline>
	                    event=skipSpacesAndGetNextEvent(event);
	                    if(event.isEndElement()){
		            		attrName=null;
		            		continue;
		            	}else{
		            		throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		            	}
	                }else if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_MULTIPOLYLINE)){
	                    IomObject multiPolyline = readMultiPolyline(event);
	                    if(multiPolyline.getattrcount()==0){
	                    	throw new IoxException("expected multipolyline. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
	                    iomObj.addattrobj(attrName, multiPolyline);
	                    event=reader.nextEvent(); // <multipolyline>
	                    event=skipSpacesAndGetNextEvent(event);
	                    if(event.isEndElement()){
		            		attrName=null;
		            		continue;
		            	}else{
		            		throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		            	}
	                }else if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_SURFACE)){
	                    // SURFACE (surface/area)
	                	IomObject multiSurface=createIomObject(QNAME_XML_MULTISURFACE.getLocalPart(), null);
	                	IomObject surface=readSurface(event);
	                	if(surface.getattrcount()==0){
	                    	throw new IoxException("expected surface. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
	                	multiSurface.addattrobj("surface", surface);
	                	if(!event.isStartElement()){
	                		throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	                	}
	                	event=reader.nextEvent(); // <surface>
	                	event=skipSpacesAndGetNextEvent(event);
	                	iomObj.addattrobj(attrName, multiSurface);
	                	if(event.isEndElement()){
		            		attrName=null;
		            		continue;
		            	}else{
		            		throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		            	}
	                }else if(event.isStartElement() && (event.asStartElement().getName().equals(QNAME_XML_MULTISURFACE) || event.asStartElement().getName().equals(QNAME_XML_MULTIAREA))){
	                    // MULTISURFACE (surfaces and/or areas)
	                	IomObject multisurface=readMultiSurface(event);
	                	if(multisurface.getattrcount()==0){
	                    	throw new IoxException("expected multisurface. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
	                    iomObj.addattrobj(attrName, multisurface);
	                    if(!event.isStartElement()){
	                    	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	                    }
	                    event=reader.nextEvent(); // <surface>
	                    event=skipSpacesAndGetNextEvent(event);
	                    if(event.isEndElement()){
		            		attrName=null;
		            		continue;
		            	}else{
		            		throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		            	}
	                }else if(event.isStartElement() && (event.asStartElement().getName().equals(QNAME_XML_BINBLBOX) || event.asStartElement().getName().equals(QNAME_XML_XMLBLBOX))){
	                    // BLACKBOX (binary and/or xml)
	                	String xmlCollection = collectXMLElement(reader,event); // <blackbox></blackbox>
            			iomObj.setattrvalue(attrName, xmlCollection);
	                    if(!event.isStartElement()){
	                    	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	                    }
	                    event=reader.nextEvent();
	                    event=skipSpacesAndGetNextEvent(event);
	                    if(event.isEndElement()){
		            		attrName=null;
		            		continue;
		            	}else{
		            		throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		            	}
	                }else if(event.isStartElement() && (event.asStartElement().getAttributeByName(QNAME_XML_REF)!=null)){
	                    // ref
	                	iomObj=readReference(iomObj, event.asStartElement(), event.asStartElement().getName().getLocalPart(), null);
	                    if(!event.isStartElement()){
	                    	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	                    }
	                    event=reader.nextEvent();
	                    event=skipSpacesAndGetNextEvent(event);
	                    if(event.isEndElement()){
		            		attrName=null;
		            		continue;
		            	}else{
		            		throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		            	}
	                }else{
            			if(event instanceof RoleDef){
            				RoleDef role=(RoleDef) event;
            				if(role.getContainer()!=null){
            					Container elementContainer=role.getContainer();
            					if(elementContainer instanceof AssociationDef){
            						AssociationDef association=(AssociationDef) elementContainer;
            						if(association instanceof Viewable){
                						iomObj=readReference(iomObj, event.asStartElement(), role.getName(),association);
                						event=reader.nextEvent(); // <role>
                						event=reader.nextEvent(); // </role>
                						attrName=null;
            						}
            					}
                			}
            			}else{
            				// structure
		                	IomObject structObj=createIomObject(event.asStartElement().getName().getLocalPart(), null);
		                    iomObj.addattrobj(attrName, readAttribute(event, event.asStartElement().getName().getLocalPart(), structObj));
		                    if(event.isStartElement()){
		                    	XMLEvent peekEvent2=reader.peek();
				            	if(!peekEvent2.isEndElement()){
				            		event=reader.nextEvent(); // <class>
				            	}
				                XMLEvent peekEvent=reader.peek();
				                if(!peekEvent.isEndElement()){
				                	// current event is an attribute
				                	event=skipSpacesAndGetNextEvent(event);
				                }else {
				                	event=reader.nextEvent(); // <class>
				                	attrName=null;
				                	continue;
				                }
		                    }else {
		                    	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		                    }
		                    return iomObj;
            			}
	                }
	            }
	            if(event.isEndElement() && attrName==null){
	            	event=skipSpacesAndGetNextEvent(event); // <characters>
	                return iomObj;
	            }
	            if(event.isEndElement() && attrName!=null){
	            	event=skipSpacesAndGetNextEvent(event); // <characters>
	                attrName=null;
	                continue;
	            }
	            if(event instanceof Comment){
	                continue;
	            }else{
	            	throw new IoxException("element not exist");
	            }
	    	}
    	}catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
		return iomObj;
        
    }

	private IomObject readReference(IomObject iomObj, StartElement element, String attrName, AssociationDef association) throws IoxException{
		String refOid=element.getAttributeByName(QNAME_XML_REF).getValue();
		if(refOid.length()<=1){
			throw new IoxException("unexpected reference format "+refOid);
		}
		Long orderPos=null;
        Attribute orderPosAttr = element.getAttributeByName(QNAME_XML_ORDERPOS);
        if(orderPosAttr!=null){
        	String orderPosVal=orderPosAttr.getValue();
        	if(orderPosVal!=null){
        		try {
					orderPos=Long.parseLong(orderPosVal);
				} catch (NumberFormatException e) {
					throw new IoxException("unexpected orderPos <"+orderPosVal+">",e);
				}
        	}
        }
		iomObj.addattrobj(attrName,QNAME_XML_REF.getLocalPart()).setobjectrefoid(refOid); // set reference
		IomObject aObject=iomObj.getattrobj(attrName, 0);
		if(orderPos!=null){
			aObject.setobjectreforderpos(orderPos);
		}
		return iomObj;
	}
    
    /** Prepare multisurface
     * @param event
     * @return IomObject multisurface
     * @throws IoxException
     * @throws XMLStreamException
     */
    private IomObject readMultiSurface(XMLEvent event) throws IoxException, XMLStreamException {
    	IomObject multiSurface=createIomObject(QNAME_XML_MULTISURFACE.getLocalPart(), null);
    	event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
    	while(event.isStartElement() && (event.asStartElement().getName().equals(QNAME_XML_SURFACE) || event.asStartElement().getName().equals(QNAME_XML_AREA))){
    		multiSurface.addattrobj("surface", readSurface(event));
	    	event=reader.nextEvent();
	    	event=skipSpacesAndGetNextEvent(event);
    	}
    	return multiSurface;
    }
    
    /** Prepare surface
     * @param event
     * @return IomObject surface
     * @throws IoxException
     * @throws XMLStreamException
     */
    private IomObject readSurface(XMLEvent event) throws IoxException, XMLStreamException {
    	IomObject surface=createIomObject(QNAME_XML_SURFACE.getLocalPart(), null);
    	event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
    	while(event.isStartElement() && (event.asStartElement().getName().equals(QNAME_XML_BOUNDARY))){
	    	surface.addattrobj("boundary", readBoundary(event));
	    	event=reader.nextEvent();
	    	event=skipSpacesAndGetNextEvent(event);
    	}
    	return surface;
    }
    
    /** Prepare boundary
     * @param event
     * @return IomObject boundary
     * @throws IoxException
     * @throws XMLStreamException
     */
    private IomObject readBoundary(XMLEvent event) throws IoxException, XMLStreamException {
    	IomObject boundary=createIomObject(QNAME_XML_BOUNDARY.getLocalPart(), null);
    	event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
    	while(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_POLYLINE)){
	    	boundary.addattrobj("polyline", readPolyline(event));
	    	event=reader.nextEvent();
	    	event=skipSpacesAndGetNextEvent(event);
    	}
    	return boundary;
    }
    
    /** Prepare multipolyline
     * @param event
     * @return IomObject multipolyline
     * @throws IoxException
     * @throws XMLStreamException
     */
    private IomObject readMultiPolyline(XMLEvent event) throws IoxException, XMLStreamException {
    	IomObject multiPolyline=createIomObject(QNAME_XML_MULTIPOLYLINE.getLocalPart(), null);
    	event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
    	while(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_POLYLINE)){
	    	multiPolyline.addattrobj("polyline", readPolyline(event));
	    	event=reader.nextEvent();
	    	event=skipSpacesAndGetNextEvent(event);
    	}
    	return multiPolyline;
    }
    
    /** Prepare polyline
     * @param event
     * @return IomObject polyline
     * @throws IoxException
     * @throws XMLStreamException
     */
    private IomObject readPolyline(XMLEvent event) throws IoxException, XMLStreamException {
    	IomObject polyline=createIomObject(QNAME_XML_POLYLINE.getLocalPart(), null);
		event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
		polyline.addattrobj("sequence", readSequence(event));
    	return polyline;
    }
    
    /** Prepare sequence
     * @param event
     * @return IomObject sequence
     * @throws XMLStreamException
     * @throws IoxException
     */
	private IomObject readSequence(XMLEvent event) throws XMLStreamException, IoxException {
		IomObject sequence=createIomObject(QNAME_XML_SEGMENTS.getLocalPart(), null);
		if(event.isStartElement()){
			while(event.isStartElement()){
				String segmentType=null;
				if(event.asStartElement().getName().equals(QNAME_XML_COORD)){
					segmentType=QNAME_XML_SEGMENTS_COORD.getLocalPart();
				}else if(event.asStartElement().getName().equals(QNAME_XML_ARC)){
					segmentType=QNAME_XML_SEGMENTS_ARC.getLocalPart();
				}
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
				sequence.addattrobj("segment", readSegment(event, segmentType));
				event=reader.nextEvent();
				if(!event.isCharacters()){
					throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
				}
				event=skipSpacesAndGetNextEvent(event);
			}
		}
		return sequence;
	}
    
	/** Prepare segment
	 * @param event
	 * @param segmentType
	 * @return IomObject segment
	 * @throws IoxException
	 * @throws XMLStreamException
	 */
	private IomObject readSegment(XMLEvent event, String segmentType) throws IoxException, XMLStreamException {
		if(segmentType==null){
			throw new IoxException("expected segment type");
		}
		IomObject segment=createIomObject(segmentType, null);
		SegmentType segTypeName=null;
		while(!event.isEndElement()){
	        if(event.isStartElement()){
	        	String segmentTypeName=event.asStartElement().getName().getLocalPart();
				event=reader.nextEvent();
				if(event.isEndElement()){
					throw new IoxException("expected coord");
				}
				if(!event.isCharacters()){
					throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
				}
				segTypeName = SegmentType.valueOf(segmentTypeName);
				switch(segTypeName){
					// could have an 1-n events of chars.
					case C1: segment.setattrvalue(QNAME_XML_SEGMENT_C1.getLocalPart(), event.asCharacters().getData());
						break;
					case C2: segment.setattrvalue(QNAME_XML_SEGMENT_C2.getLocalPart(), event.asCharacters().getData());
						break;
					case C3: segment.setattrvalue(QNAME_XML_SEGMENT_C3.getLocalPart(), event.asCharacters().getData());
						break;
					case A1: segment.setattrvalue(QNAME_XML_SEGMENT_A1.getLocalPart(), event.asCharacters().getData());
						break;
					case A2: segment.setattrvalue(QNAME_XML_SEGMENT_A2.getLocalPart(), event.asCharacters().getData());
						break;
					case R: segment.setattrvalue(QNAME_XML_SEGMENT_R.getLocalPart(), event.asCharacters().getData());
						break;
					default: throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
				}
	        }
	        event=reader.nextEvent();
	        if(event.isStartElement()){
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	        }
			event=reader.nextEvent(); // end --> start coord
			event=skipSpacesAndGetNextEvent(event);
		}
		return segment;
	}
	private int hsOid=0;
	private String hsNextOid(){
		hsOid++;
		return Integer.toString(hsOid);
	}
    
	@Override
	public IomObject createIomObject(String type, String oid) throws IoxException {
		return factory.createIomObject(type, oid);
	}

	@Override
	public IoxFactoryCollection getFactory() throws IoxException {
		return factory;
	}

	@Override
	public void setFactory(IoxFactoryCollection factory) throws IoxException {
		this.factory=factory;
	}

	@Override
	public void setModel(TransferDescription td) {
		// not to implement
	}
}