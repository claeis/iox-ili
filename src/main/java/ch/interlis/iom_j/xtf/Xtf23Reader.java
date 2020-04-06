package ch.interlis.iom_j.xtf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import ch.interlis.ili2c.metamodel.AssociationDef;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.TransferDescription;
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
    private java.util.Set<String> filterTopics=null;
	private int state = START;

	// state
	private static final int START=0;
	private static final int AFTER_STARTTRANSFER=1;
	private static final int AFTER_STARTBASKET=2;
	private static final int AFTER_OBJECT=3;
	private static final int AFTER_ENDBASKET=4;
	private static final int AFTER_ENDTRANSFER=5;
	
	// namespace
	public static final String NAMESPACE_ILIXMLBASE="http://www.interlis.ch/INTERLIS2.3";
    
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
    private static final QName QNAME_XML_CONSISTENCY=new QName("CONSISTENCY");
    private static final QName QNAME_XML_TOPIC_CONSISTENCY_COMPLETE = new QName(NAMESPACE_ILIXMLBASE, "COMPLETE");
    private static final QName QNAME_XML_TOPIC_CONSISTENCY_INCOMPLETE = new QName(NAMESPACE_ILIXMLBASE, "INCOMPLETE");
    private static final QName QNAME_XML_TOPIC_CONSISTENCY_INCONSISTENT = new QName(NAMESPACE_ILIXMLBASE, "INCONSISTENT");
    private static final QName QNAME_XML_TOPIC_CONSISTENCY_ADAPTED = new QName(NAMESPACE_ILIXMLBASE, "ADAPTED");
    private static final QName QNAME_XML_DATASECTION = new QName(NAMESPACE_ILIXMLBASE, "DATASECTION");
    // object
    private static final QName QNAME_XML_OID = new QName("OID");
    private static final QName QNAME_XML_OBJECT_TID = new QName("TID");
    private static final QName QNAME_XML_OBJECT_OPERATION = new QName("OPERATION");   
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
    private static final QName QNAME_XML_BOUNDARY = new QName(NAMESPACE_ILIXMLBASE, "BOUNDARY");
    private static final QName QNAME_XML_SURFACE = new QName(NAMESPACE_ILIXMLBASE, "SURFACE");
    private static final QName QNAME_XML_MULTISURFACE = new QName(NAMESPACE_ILIXMLBASE, "MULTISURFACE");
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
     * @param in Input stream to read from
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
	 * @param in Input stream to read from
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
			javax.xml.stream.events.XMLEvent event=null;
			if(state==START){
				event=reader.nextEvent();
				// after start
				if(event.isStartDocument()){
					; // skip start document event
				}else {
					throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
				}
				// after start document
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
				if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_TRANSFER)){
					; // skip start transfer event
                }else {
                	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
                }
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
				// after start transfer
				XtfStartTransferEvent startTransferEvent=null;
                if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_HEADERSECTION)){
                	startTransferEvent=new XtfStartTransferEvent();
					event=readHeaderSection(event, startTransferEvent); // header section
                }else {
                	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
                }
                // after header section
                if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_DATASECTION)){ // start data section
                	; // skip start data section event
                }else {
	        		throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	        	}
                
            	HashMap<String,IomObject> modelx=new HashMap<String,IomObject>();
        		for(String modelName:models){
                    IomObject model=createIomObject(ch.interlis.iom_j.xtf.impl.MyHandler.HEADER_OBJECT_MODELENTRY,hsNextOid());
        			model.setattrvalue(ch.interlis.iom_j.xtf.impl.MyHandler.HEADER_OBJECT_MODELENTRY_NAME,modelName);
        			modelx.put(model.getobjectoid(),model);
        		}
        		startTransferEvent.setHeaderObjects(modelx);
                state=AFTER_STARTTRANSFER;
                return startTransferEvent; // return start transfer
            }
			
			// after start transfer
			if(state==AFTER_STARTTRANSFER) {
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
                if(event.isStartElement()){
                	state=AFTER_STARTBASKET;
		        	return readStartBasket(event);
		        }else if(event.isEndElement()){
					state=AFTER_ENDBASKET;
					// see processing below
	        	}else{
	        		throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	        	}
			}
			
			// after start basket
			if(state==AFTER_STARTBASKET) {
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
				if(event.isStartElement()) { // start object
					state=AFTER_OBJECT;
					IomObject iomObj=null;
					iomObj=readObject(event, iomObj); // read object;
					setOperation(event.asStartElement(), iomObj);
					setConsistency(event.asStartElement(), iomObj);
					return new ch.interlis.iox_j.ObjectEvent(iomObj); // return object
				}
				if(event.asEndElement().getName().equals(new QName(NAMESPACE_ILIXMLBASE, currentModelName+"."+currentTopicName))){
					state=AFTER_ENDBASKET;
		        	return new ch.interlis.iox_j.EndBasketEvent(); // return end basket
				}
			}
			
			// after object
			if(state==AFTER_OBJECT) {
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
				if(event.isStartElement()){
					IomObject iomObj=null;
					iomObj=readObject(event, iomObj);
					setOperation(event.asStartElement(), iomObj);
					setConsistency(event.asStartElement(), iomObj);
					return new ch.interlis.iox_j.ObjectEvent(iomObj); // return object
		        }else if(event.asEndElement().getName().equals(new QName(NAMESPACE_ILIXMLBASE, currentModelName+"."+currentTopicName))){ // close basket
					state=AFTER_ENDBASKET;
		        	return new ch.interlis.iox_j.EndBasketEvent(); // return end basket
		        }
			}
			
			// after end basket
			if(state==AFTER_ENDBASKET) {
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
		    	if(event.isStartElement()){
		    		state=AFTER_STARTBASKET;
		        	return readStartBasket(event); // create new basket
		        }else if(event.isEndElement()) {
		        	if(event.asEndElement().getName().equals(QNAME_XML_DATASECTION)){ // end data section
		        		event=reader.nextEvent();
		        		event=skipSpacesAndGetNextEvent(event);
		        	}
		        	if(event.asEndElement().getName().equals(QNAME_XML_TRANSFER)) { // end transfer
						event=reader.nextEvent();
						event=skipSpacesAndGetNextEvent(event);
						if(event.isEndDocument()) { // end document
							state=AFTER_ENDTRANSFER;
							return new ch.interlis.iox_j.EndTransferEvent(); // return end transfer
						}else {
							throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
						}
					}else {
						throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
					}
		        }
			}
			// after end transfer
			if(state==AFTER_ENDTRANSFER) {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
		}catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
		return null;
	}

	private IoxEvent readStartBasket(javax.xml.stream.events.XMLEvent event) throws IoxException, XMLStreamException {
		StartElement element = (StartElement) event;
		// model name
		currentModelName=getModelName(element.asStartElement().getName().getLocalPart());
		if(currentModelName==null) {
			throw new IoxException("missing model name");
		}
		// topic name
		currentTopicName=getTopicNameOfModel(element.asStartElement().getName().getLocalPart());
		if(currentTopicName==null) {
			throw new IoxException("missing topic name");
		}
		QName basketId = QNAME_XML_TOPIC_BID;
		Attribute bid = element.getAttributeByName(basketId);
		// create basket
		ch.interlis.iox_j.StartBasketEvent newObj=new ch.interlis.iox_j.StartBasketEvent(currentModelName+"."+currentTopicName, bid.getValue());
		newObj=setState(element, newObj);
		newObj=setConsistency(element, newObj);
		newObj=setDomain(element, newObj);
		newObj=setIncrementalKind(element, newObj);
		return newObj; // return basket
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

	private XMLEvent readHeaderSection(XMLEvent startElementHeaderSection, XtfStartTransferEvent xtfEvent) throws XMLStreamException, IoxException{
		// header section version
		Attribute version=startElementHeaderSection.asStartElement().getAttributeByName(QNAME_XML_HEADERSECTION_VERSION);
        if(version!=null && version.getValue().equals(INTERLIS_VERSION_23)){
        	xtfEvent.setVersion(version.getValue());
        }else {
        	throw new IoxSyntaxException(unexpectedXmlEvent2msg(startElementHeaderSection));
        }
        // header section sender
    	Attribute sender=startElementHeaderSection.asStartElement().getAttributeByName(QNAME_XML_HEADERSECTION_SENDER);
        if(sender!=null){
        	xtfEvent.setSender(sender.getValue());
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
				event=readHeaderSectionOidSpaces(event, xtfEvent);
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
				event=readHeaderSectionComment(event, xtfEvent);
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_HEADERSECTION)){ // end header section
			; // skip end header section event
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
		return event;
	}
	
	private XMLEvent readHeaderSectionModels(XMLEvent startElementModels) throws IoxException, XMLStreamException {
		XMLEvent event=null;
		if(startElementModels.isStartElement() && startElementModels.asStartElement().getName().equals(QNAME_XML_MODELS)){ // start models
			; // skip start models event
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
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
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
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
        }else {
        	throw new IoxSyntaxException(unexpectedXmlEvent2msg(startElementModel));
        }
        event=reader.nextEvent();
        event=skipSpacesAndGetNextEvent(event);
        
        // end element Model
 		if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_MODEL)) {
 			; // skip end model event
 		}else {
 			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event)); 
 		}
 		event=reader.nextEvent();
 		event=skipSpacesAndGetNextEvent(event);      	
 		return event;
	}
	
	private XMLEvent readHeaderSectionAlias(XMLEvent startElementAlias) throws IoxException, XMLStreamException {
		XMLEvent event=null;
		// start alias
		if(startElementAlias.isStartElement() && startElementAlias.asStartElement().getName().equals(QNAME_XML_ALIAS)){
			; // skip start alias event
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
		
		// end alias
		if(event.isEndElement()) {
			if(event.asEndElement().getName().equals(QNAME_XML_ALIAS)) {
				; // skip end alias event
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
			; // skip end alias event
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
		return event;
	}

	private XMLEvent readAliasEntries(XMLEvent startElementAliasEntries) throws IoxSyntaxException, XMLStreamException, IoxException {
		XMLEvent event=null;
		// start entries
		Attribute entryFor=startElementAliasEntries.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_FOR);
    	if(entryFor!=null){
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(startElementAliasEntries));
		}
    	event=reader.nextEvent();
    	event=skipSpacesAndGetNextEvent(event);
		
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
			; // skip end entries event
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
		return event;
	}

	private XMLEvent readAliasEntry(XMLEvent startElementAliasEntry) throws IoxSyntaxException, IoxException, XMLStreamException {
		XMLEvent event=null;
		if(startElementAliasEntry.isStartElement() && startElementAliasEntry.asStartElement().getName().equals(QNAME_XML_ENTRIES_TAGENTRY)){
			// start tag entry
			if(startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_FROM)!=null &&
				startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_TO)!=null){
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
			event=reader.nextEvent();
			event=skipSpacesAndGetNextEvent(event);
			// end tag entry
			if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_ENTRIES_TAGENTRY)){
				; // skip end tagentry event
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
			event=reader.nextEvent();
			event=skipSpacesAndGetNextEvent(event);
		}else if(startElementAliasEntry.isStartElement() && startElementAliasEntry.asStartElement().getName().equals(QNAME_XML_ENTRIES_VALENTRY)){
			// start val entry
			if(startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_TAG)!=null &&
					startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_ATTR)!=null &&
					startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_FROM)!=null &&
					startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_TO)!=null){
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(startElementAliasEntry));
			}
			event=reader.nextEvent();
			event=skipSpacesAndGetNextEvent(event);
			
			// end val entry
			if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_ENTRIES_VALENTRY)){
				; // skip end valentry event
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
			event=reader.nextEvent();
			event=skipSpacesAndGetNextEvent(event);
		}else if(startElementAliasEntry.isStartElement() && startElementAliasEntry.asStartElement().getName().equals(QNAME_XML_ENTRIES_DELENTRY)){
			// start del entry
			if(startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_TAG)!=null) {
				; // skip start entries event
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(startElementAliasEntry));
			}
			event=reader.nextEvent();
			event=skipSpacesAndGetNextEvent(event);
			
			// end del entry
			if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_ENTRIES_DELENTRY)){
				; // skip end delentry event
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
			event=reader.nextEvent();
			event=skipSpacesAndGetNextEvent(event);
		}
		return event;
	}
	
	private XMLEvent readHeaderSectionOidSpaces(XMLEvent startElementOidSpaces, XtfStartTransferEvent xtfEvent) throws XMLStreamException, IoxException {
		// start oid space(s)
		XMLEvent event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
		
		// oid space
		if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_OIDSPACE)) {
			List<OidSpace> oidSpaces=new ArrayList<OidSpace>();
			while(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_OIDSPACE)) {
				event=readOidSpace(event, oidSpaces);
			}
			if(oidSpaceSize==0) {
				throw new IoxException("expected at least 1 oid space");
			}
			xtfEvent.setOidSpaces(oidSpaces);
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		
		// end oid space(s)
		if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_OIDSPACES)) {
			; // skip end oidspaces event
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));  
		}
		event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
		return event;
	}

	private XMLEvent readOidSpace(XMLEvent startElementOidSpace, List<OidSpace> oidSpaces) throws IoxSyntaxException, IoxException, XMLStreamException {
		XMLEvent event=null;
		// start oid space
		if(startElementOidSpace.asStartElement().getAttributeByName(QNAME_XML_OIDSPACE_NAME)!=null ||
		   startElementOidSpace.asStartElement().getAttributeByName(QNAME_XML_OIDSPACE_OIDDOMAIN)!=null){
			Attribute oidSpace=startElementOidSpace.asStartElement().getAttributeByName(QNAME_XML_OIDSPACE_OIDDOMAIN);
			OidSpace oidSpaceObj=new OidSpace("oidSpace"+oidSpaceSize, oidSpace.getValue());
			oidSpaces.add(oidSpaceSize, oidSpaceObj);
			oidSpaceSize+=1;
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(startElementOidSpace));
		}
		event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
		
		// end oid space
		if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_OIDSPACE)){
			; // skip end oidspace event
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
		return event;
	}
	
	private XMLEvent readHeaderSectionComment(XMLEvent startElementComment, XtfStartTransferEvent xtfEvent) throws IoxSyntaxException, XMLStreamException {
		// start comment
		XMLEvent event=reader.nextEvent();
		event=skipCommentary(event);
		StringBuffer value=new StringBuffer();
		event=readSimpleContent(event,value);
		xtfEvent.setComment(value.toString());
		// end comment
		if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_COMMENT)) {
			; // skip end comment event
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
		return event;
	}

	private XMLEvent skipCommentary(XMLEvent event) throws XMLStreamException {
		 while(event.getEventType()==XMLEvent.COMMENT){
			 event=reader.nextEvent();
	     }
	     return event;
	}

	private IomObject readObject(XMLEvent event, IomObject iomObj) throws IoxException {
		try {
	    	if(event.isStartElement()){
	            Attribute oid = event.asStartElement().getAttributeByName(QNAME_XML_OBJECT_TID);
	            if(event.asStartElement().getAttributeByName(QNAME_XML_TOPIC_BID) != null){
	            	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	            }
	            // DeleteObject
	            if(event.asStartElement().getName().equals(QNAME_XML_DELETE)) {
	            	iomObj = readDelete(event, iomObj);
	    			event=reader.nextEvent();
	    			return iomObj;
	    		}else{
	    			// IomObject
		            currentViewableName=getViewableFromQName(event.asStartElement().getName());
		            if(currentViewableName==null) {
		            	throw new IoxException("missing table name");
		            }
		            event=reader.nextEvent(); // after start object
		            event=skipSpacesAndGetNextEvent(event);
		            
	        		if(oid!=null){ // create object
	        			iomObj=createIomObject(currentModelName+"."+currentTopicName+"."+currentViewableName, oid.getValue());
	        		}else{
	        			iomObj=createIomObject(currentModelName+"."+currentTopicName+"."+currentViewableName, null);
	        		}
	    		}
	        	while(reader.hasNext() && !event.isEndElement()){
	        		iomObj=readAttribute(event, iomObj); // read object attribute
	        		event=reader.nextEvent();
	        		event=skipSpacesAndGetNextEvent(event);
	        	}
	    	}
	    	if(event.isEndElement()) { // end object
	    		return iomObj; // return object
	        }else if(event.isCharacters()){
	        	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	        }
		}catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
		return null;
	}

	private IomObject readDelete(XMLEvent event, IomObject iomObj) throws IoxException, XMLStreamException {
		if(event.asStartElement().getAttributeByName(QNAME_XML_OBJECT_TID)!=null){
			// create delete object
			iomObj=createIomObject(QNAME_XML_DELETE.getLocalPart(), event.asStartElement().getAttributeByName(QNAME_XML_OBJECT_TID).getValue());
			event=reader.nextEvent(); // after start delete object
			event=skipSpacesAndGetNextEvent(event);
			if(event.isEndElement()){ // end delete object
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
		return iomObj; // return delete object
	}

	private IomObject setConsistency(StartElement element, IomObject iomObj) throws IoxException {
        Attribute consistency = element.getAttributeByName(QNAME_XML_CONSISTENCY);
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
	
	private IomObject setOperation(StartElement element, IomObject iomObj) throws IoxException {
		Attribute operation = element.getAttributeByName(QNAME_XML_OBJECT_OPERATION);
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
        return iomObj;
	}

	private ch.interlis.iox_j.StartBasketEvent setState(StartElement element, ch.interlis.iox_j.StartBasketEvent startBasketEvent) throws IoxException {
		Iterator codingObjIter=element.getAttributes();
		String[] genericAndConcreteDomains=null;
		while(codingObjIter.hasNext()){
			Attribute codingObj=(Attribute) codingObjIter.next();
			if(codingObj.getName().equals(QNAME_XML_TOPIC_STARTSTATE)){
				startBasketEvent.setStartstate(codingObj.getValue());
			}else if(codingObj.getName().equals(QNAME_XML_TOPIC_ENDSTATE)){
				startBasketEvent.setEndstate(codingObj.getValue());
			}
		}
		return startBasketEvent;
	}
	
	private ch.interlis.iox_j.StartBasketEvent setIncrementalKind(StartElement element, ch.interlis.iox_j.StartBasketEvent startBasketEvent) throws IoxException {
		Iterator codingObjIter=element.getAttributes();
		while(codingObjIter.hasNext()){
			Attribute codingObj=(Attribute) codingObjIter.next();
			if(codingObj.getName().equals(QNAME_XML_TOPIC_KIND)){
				if(codingObj.getValue().equals(QNAME_XML_TOPIC_KIND_FULL.getLocalPart())){
					startBasketEvent.setKind(IomConstants.IOM_FULL);
				}else if(codingObj.getValue().equals(QNAME_XML_TOPIC_KIND_UPDATE.getLocalPart())){
					startBasketEvent.setKind(IomConstants.IOM_UPDATE);
				}else if(codingObj.getValue().equals(QNAME_XML_TOPIC_KIND_INITIAL.getLocalPart())){
					startBasketEvent.setKind(IomConstants.IOM_INITIAL);
				}
			}
		}
		return startBasketEvent;
	}
	
	private ch.interlis.iox_j.StartBasketEvent setDomain(StartElement element, ch.interlis.iox_j.StartBasketEvent startBasketEvent) throws IoxException {
		Iterator codingObjIter=element.getAttributes();
		String[] genericAndConcreteDomains=null;
		while(codingObjIter.hasNext()){
			Attribute codingObj=(Attribute) codingObjIter.next();
			if(codingObj.getName().equals(QNAME_XML_DOMAIN)) { //QNAME_XML_TOPIC_TOPICS
				String domainValue=codingObj.getValue();
				genericAndConcreteDomains=domainValue.split("\\,"); // genericAndConcreteDomains.getValues(genericDomain=concreteDomain)
				for(String singleDomain : genericAndConcreteDomains){
					startBasketEvent.addDomain(singleDomain, element.getName().getLocalPart());
				}
			}
		}
		return startBasketEvent;
	}
	
	private ch.interlis.iox_j.StartBasketEvent setConsistency(StartElement element, ch.interlis.iox_j.StartBasketEvent startBasketEvent) throws IoxException {
		Iterator codingObjIter=element.getAttributes();
		while(codingObjIter.hasNext()){
			Attribute codingObj=(Attribute) codingObjIter.next();
			if(codingObj.getName().equals(QNAME_XML_CONSISTENCY)){
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

	private static XMLEvent collectXMLElement(XMLEventReader xmlReader, XMLEvent event, java.io.StringWriter strw) throws XMLStreamException {
        XMLOutputFactory xmloutputf = XMLOutputFactory.newInstance();
        xmloutputf.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES,true);
        XMLEventFactory xmlef = XMLEventFactory.newInstance();
        XMLEventWriter xmlw = xmloutputf.createXMLEventWriter(strw); 
        //xmlw.add(xmlef.createStartDocument());
        xmlw.add(event);
        int inHeader = 1;
        while(xmlReader.hasNext()){
            event = xmlReader.nextEvent();
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
        //xmlw.add(xmlef.createEndDocument());
        xmlw.flush();
        xmlw.close();
        return event;
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
    
    private IomObject readAttribute(XMLEvent startAttributeEvent, IomObject iomObj) throws IoxException {
    	try {
    		XMLEvent event=null;
    		String attrName=null;
	    	StartElement element = (StartElement) startAttributeEvent;
	    	// reference
	    	if(element.getAttributeByName(QNAME_XML_REF)!=null){
            	iomObj=readReference(iomObj, startAttributeEvent.asStartElement(), startAttributeEvent.asStartElement().getName().getLocalPart(), null);
                if(!startAttributeEvent.isStartElement()){
                	throw new IoxSyntaxException(unexpectedXmlEvent2msg(startAttributeEvent));
                }
                event=reader.nextEvent();
        		event=skipSpacesAndGetNextEvent(event);
        		if(event.isEndElement()) {
        			return iomObj;
        		}
            }else if(element.getAttributeByName(QNAME_XML_OID)!=null) {
            	Attribute oidAttr=element.getAttributeByName(QNAME_XML_OID);
            	attrName=element.getName().getLocalPart();
            	iomObj.setattrvalue(attrName, oidAttr.getValue());
            	event=reader.nextEvent();
            	event=skipCommentary(event);
        		if(event.isCharacters()){ // are characters
        			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
        		}
    		}else {
	            // attribute
		    	attrName=element.getName().getLocalPart();
		    	if(attrName!=null){
		    		; // skip
		    	}else {
		    		throw new IoxSyntaxException(unexpectedXmlEvent2msg(startAttributeEvent));
		    	}
		    	event=reader.nextEvent(); // start attribute
		    	event=skipCommentary(event);
		    	if (event.isCharacters()) {
		    	    Characters characters = (Characters) event;
		    	    // Check has character a new line or tab..
	                if(!reader.peek().isEndElement() && characters.isWhiteSpace()) {
	                    event=skipSpacesAndGetNextEvent(event);
	                }
		    	}

		    	// characters
	            if(event.isCharacters()){ 
	            	StringBuffer value=new StringBuffer();
	            	event=readSimpleContent(event,value);
	            	iomObj.setattrvalue(attrName, value.toString());
	            }else if(event.isStartElement()) {
	            	// object
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
	                }else if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_POLYLINE)){
	                    IomObject polyline = readPolyline(event);
	                    if(polyline.getattrcount()==0){
	                    	throw new IoxException("expected polyline. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
	                    iomObj.addattrobj(attrName, polyline);
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
	                	iomObj.addattrobj(attrName, multiSurface);
	                }else if(event.isStartElement() && (event.asStartElement().getName().equals(QNAME_XML_XMLBLBOX))){
                        event=reader.nextEvent(); // skip XMLBLBOX
                        event=skipSpacesAndGetNextEvent(event);
	                    // BLACKBOX (binary and/or xml)
	                	java.io.StringWriter strw=new java.io.StringWriter();
	                	event=collectXMLElement(reader,event, strw); // start BLACKBOX
	        			iomObj.setattrvalue(attrName, strw.toString());
	                    if(!event.isEndElement()){
	                    	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	                    }
                        event=reader.nextEvent(); // skip end of XMLBLBOX
                        event=skipSpacesAndGetNextEvent(event);
                    }else if(event.isStartElement() && (event.asStartElement().getName().equals(QNAME_XML_BINBLBOX)) ){
                        event=reader.nextEvent(); // skip BLBOX
                        StringBuffer value=new StringBuffer();
                        event=readSimpleContent(event,value);
                        iomObj.setattrvalue(attrName, value.toString());
                        if(!event.isEndElement()){
                            throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
                        }
	                }else{
	    				// structure
	                	IomObject structObj=createIomObject(event.asStartElement().getName().getLocalPart(), null);
	                    iomObj.addattrobj(attrName, readObject(event, structObj));
	                    if(!event.isStartElement()){
	                    	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	                    }
	                }
	                event=reader.nextEvent(); // after start object
	                event=skipSpacesAndGetNextEvent(event);
		    	}
            }
            if(event.isEndElement() && attrName!=null){ // end object
                attrName=null;
            }
            if(event.isEndElement() && attrName==null){ // end object
            	return iomObj; // return object
	        }
    	}catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
    	return iomObj;
    }

	private IomObject readReference(IomObject iomObj, StartElement element, String attrName, AssociationDef association) throws IoxException, XMLStreamException{
	    String refOid=null;
	    if (element.getAttributeByName(QNAME_XML_REF) != null) {
	        refOid=element.getAttributeByName(QNAME_XML_REF).getValue();
	        if(refOid.length()==0){
	            throw new IoxException("unexpected reference value <"+refOid+">");
	        }		    
		}

		Attribute attrRefBid=element.getAttributeByName(QNAME_XML_TOPIC_BID);
		String refBid=null;
		if(attrRefBid!=null) {
			refBid=attrRefBid.getValue();
			if(refBid.length()==0){
				throw new IoxException("unexpected reference BID value <"+refBid+">");
			}
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
        XMLEvent peek = reader.peek();
        XMLEvent event = null;
        if (peek.isCharacters()) {
            event = reader.nextEvent();
            event = skipSpacesAndGetNextEvent(event);
        } else if (peek.isStartElement()) {
            event = reader.nextEvent();
        }
        if (event != null && event.isStartElement()) {
            element = (StartElement) event;
            iomObj.addattrobj(attrName, readObject(event, iomObj));
        } else {
            IomObject aObject=iomObj.addattrobj(attrName,QNAME_XML_REF.getLocalPart());
            aObject.setobjectrefoid(refOid); // set reference
            if(orderPos!=null){
                aObject.setobjectreforderpos(orderPos);
            }
            if(refBid!=null) {
                aObject.setobjectrefbid(refBid);
            }            
        }

		return iomObj;
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
				if(!event.isCharacters() && event.getEventType()!=XMLEvent.COMMENT){
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
	        event=reader.nextEvent(); // after start coord
	        if(event.isStartElement()){
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	        }
			event=reader.nextEvent(); // after end coord
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
		// not implemented
	}

    @Override
    public void setTopicFilter(String[] topicNames) {
        this.filterTopics=new java.util.HashSet<String>();
        for(String topicName:topicNames) {
            filterTopics.add(topicName);
        }
    }

    @Override
    public String getMimeType() {
        return XTF_23;
    }
}