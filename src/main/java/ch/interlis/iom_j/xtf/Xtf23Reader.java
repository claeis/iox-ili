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

import ch.ehi.basics.tools.StringUtility;
import ch.ehi.basics.types.OutParam;
import ch.interlis.ili2c.metamodel.AssociationDef;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iox.IoxReader;
import ch.interlis.iox_j.IoxIliReader;
import ch.interlis.iox_j.IoxSyntaxException;
import ch.interlis.iox_j.StartTransferEvent;

public class Xtf23Reader implements IoxReader ,IoxIliReader{
    private class XtfModel {
        public XtfModel(String name, String version,String uri) {
            super();
            this.name = name;
            this.version = version;
            this.uri=uri;
        }
        private String name;
        private String version;
        private String uri;
        public String getName() {
            return name;
        }
        public String getVersion() {
            return version;
        }
        public String getUri() {
            return uri;
        }
    }
    private XMLEventReader reader=null;
	private IoxFactoryCollection factory=new  ch.interlis.iox_j.DefaultIoxFactoryCollection();
	private java.io.InputStream inputFile=null;
	private int oidSpaceSize=0;
	private List<XtfModel> models=new ArrayList<XtfModel>();
    private java.util.Set<String> filterTopics=null;
    private boolean isXtf23Reader0_compatible=true;
    private boolean ili22=false;
	private int state = START;

	// state
	private static final int START=0;
	private static final int AFTER_STARTTRANSFER=1;
	private static final int AFTER_STARTBASKET=2;
	private static final int AFTER_OBJECT=3;
	private static final int AFTER_ENDBASKET=4;
	private static final int AFTER_ENDTRANSFER=5;
	
	// namespace
	public static final String NAMESPACE_XTF23="http://www.interlis.ch/INTERLIS2.3";
    public static final String NAMESPACE_XTF22="http://www.interlis.ch/INTERLIS2.2";
    @Deprecated
    public static final String NAMESPACE_ILIXMLBASE=NAMESPACE_XTF23;
    
    // segmentType
    private enum SegmentType {C1,C2,C3,A1,A2,R}

    // modelnames
    private String currentTopicName=null;

    // headersection
    private static final String INTERLIS_VERSION_23 = "2.3";
    private static final String INTERLIS_VERSION_22 = "2.2";
    private static final String NAME_XML_HEADERSECTION = "HEADERSECTION";
    private static final QName QNAME_XML_HEADERSECTION_SENDER = new QName("SENDER");
    private static final QName QNAME_XML_HEADERSECTION_VERSION = new QName("VERSION");
    private static final String NAME_XML_MODELS = "MODELS";
    private static final String NAME_XML_MODEL = "MODEL";
    private static final QName QNAME_XML_MODEL_NAME = new QName("NAME");
    private static final QName QNAME_XML_MODEL_VERSION = new QName("VERSION");
    private static final QName QNAME_XML_MODEL_URI = new QName("URI");
    private static final QName QNAME_XML_OIDSPACES = new QName(NAMESPACE_XTF23, "OIDSPACES");
    private static final QName QNAME_XML_OIDSPACE = new QName(NAMESPACE_XTF23, "OIDSPACE");
    private static final QName QNAME_XML_OIDSPACE_OIDDOMAIN = new QName("OIDDOMAIN");
    private static final QName QNAME_XML_OIDSPACE_NAME = new QName("NAME");
    private static final String QNAME_XML_ENTRIES = "ENTRIES";
    private static final String QNAME_XML_COMMENT = "COMMENT";
    private static final String NAME_XML_ALIAS = "ALIAS";
    private static final String QNAME_XML_ENTRIES_TAGENTRY = "TAGENTRY";
    private static final String QNAME_XML_ENTRIES_VALENTRY = "VALENTRY";
    private static final String QNAME_XML_ENTRIES_DELENTRY = "DELENTRY";
    private static final QName QNAME_XML_ENTRIES_VALUE_ATTR = new QName("ATTR");
    private static final QName QNAME_XML_ENTRIES_VALUE_FROM = new QName("FROM");
    private static final QName QNAME_XML_ENTRIES_VALUE_TO = new QName("TO");
    private static final QName QNAME_XML_ENTRIES_VALUE_TAG = new QName("TAG");
    private static final QName QNAME_XML_ENTRIES_VALUE_FOR = new QName("FOR");
    // topic
    private static final QName QNAME_XML_TOPIC_BID = new QName("BID");
    private static final QName QNAME_XML_TOPIC_KIND=new QName("KIND");
    private static final String QNAME_XML_TOPIC_KIND_FULL = "FULL";
    private static final String QNAME_XML_TOPIC_KIND_UPDATE = "UPDATE";
    private static final String QNAME_XML_TOPIC_KIND_INITIAL = "INITIAL";
    private static final QName QNAME_XML_TOPIC_STARTSTATE=new QName("STARTSTATE");
    private static final QName QNAME_XML_TOPIC_ENDSTATE=new QName("ENDSTATE");
    private static final QName QNAME_XML_CONSISTENCY=new QName("CONSISTENCY");
    private static final String QNAME_XML_TOPIC_CONSISTENCY_COMPLETE = "COMPLETE";
    private static final String QNAME_XML_TOPIC_CONSISTENCY_INCOMPLETE = "INCOMPLETE";
    private static final String QNAME_XML_TOPIC_CONSISTENCY_INCONSISTENT = "INCONSISTENT";
    private static final String QNAME_XML_TOPIC_CONSISTENCY_ADAPTED = "ADAPTED";
    private static final String NAME_XML_DATASECTION = "DATASECTION";
    // object
    private static final QName QNAME_XML_OID = new QName("OID");
    private static final QName QNAME_XML_OBJECT_TID = new QName("TID");
    private static final QName QNAME_XML_OBJECT_OPERATION = new QName("OPERATION");   
    private static final String XML_OPERATION_INSERT = "INSERT";
    private static final String XML_OPERATION_DELETE="DELETE";
    // others
    private static final QName QNAME_XML_ORDERPOS = new QName("ORDER_POS");
    private static final String NAME_XML_TRANSFER="TRANSFER";
    private static final QName QNAME_XML_REF = new QName("REF");
    private static final QName QNAME_XML_DOMAIN=new QName("DOMAINS");
    private static final String NAME_XML_DELETE="DELETE";
    // interlis types
    private static final String QNAME_XML_BINBLBOX = "BINBLBOX";
    private static final String QNAME_XML_XMLBLBOX = "XMLBLBOX";
    private static final String QNAME_XML_COORD = "COORD";
    private static final String QNAME_XML_ARC = "ARC";
    private static final String QNAME_XML_POLYLINE = "POLYLINE";
    private static final String QNAME_XML_BOUNDARY = "BOUNDARY";
    private static final String QNAME_XML_SURFACE = "SURFACE";
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

    private javax.xml.stream.events.XMLEvent readAheadEvent=null;
	@Override
	public IoxEvent read() throws IoxException {
		try {
			if(state==START){
				readAheadEvent=reader.nextEvent();
				// after start
				if(readAheadEvent.isStartDocument()){
				    readAheadEvent=nextEvent(readAheadEvent); // skip start document
				}else {
					throw new IoxSyntaxException(unexpectedXmlEvent2msg(readAheadEvent));
				}
				if(readAheadEvent.isStartElement() && (readAheadEvent.asStartElement().getName().equals(new QName(NAMESPACE_XTF23,NAME_XML_TRANSFER)) || readAheadEvent.asStartElement().getName().equals(new QName(NAMESPACE_XTF22,NAME_XML_TRANSFER)))){
				    if(readAheadEvent.asStartElement().getName().getNamespaceURI().equals(NAMESPACE_XTF22)) {
                        ili22=true;
				    }else {
				        ili22=false;
				    }
				    readAheadEvent=nextEvent(readAheadEvent); // skip start transfer element
                }else {
                	throw new IoxSyntaxException(unexpectedXmlEvent2msg(readAheadEvent));
                }
				// after start transfer
				XtfStartTransferEvent startTransferEvent=null;
                if(readAheadEvent.isStartElement() && readAheadEvent.asStartElement().getName().equals(getQName(NAME_XML_HEADERSECTION))){
                	startTransferEvent=new XtfStartTransferEvent();
					readAheadEvent=readHeaderSection(readAheadEvent, startTransferEvent); // header section
                }else {
                	throw new IoxSyntaxException(unexpectedXmlEvent2msg(readAheadEvent));
                }
            	HashMap<String,IomObject> modelx=new HashMap<String,IomObject>();
        		for(XtfModel xtfModel:models){
                    IomObject model=createIomObject(ch.interlis.iom_j.xtf.impl.MyHandler.HEADER_OBJECT_MODELENTRY,hsNextOid());
        			model.setattrvalue(ch.interlis.iom_j.xtf.impl.MyHandler.HEADER_OBJECT_MODELENTRY_NAME,xtfModel.getName());
                    if(xtfModel.getVersion()!=null)model.setattrvalue(ch.interlis.iom_j.xtf.impl.MyHandler.HEADER_OBJECT_MODELENTRY_VERSION,xtfModel.getVersion());
                    if(xtfModel.getUri()!=null)model.setattrvalue(ch.interlis.iom_j.xtf.impl.MyHandler.HEADER_OBJECT_MODELENTRY_URI,xtfModel.getUri());
        			modelx.put(model.getobjectoid(),model);
        		}
        		startTransferEvent.setHeaderObjects(modelx);
                state=AFTER_STARTTRANSFER;
                return startTransferEvent; // return start transfer
            }
			
			// after start transfer
			if(state==AFTER_STARTTRANSFER) {
                // after header section
                if(readAheadEvent.isStartElement() && readAheadEvent.asStartElement().getName().equals(getQName(NAME_XML_DATASECTION))){ // start data section
                    readAheadEvent=nextEvent(readAheadEvent); // skip start data section event
                }else {
                    throw new IoxSyntaxException(unexpectedXmlEvent2msg(readAheadEvent));
                }
                if(readAheadEvent.isStartElement()){
                	state=AFTER_STARTBASKET;
                	OutParam<IoxEvent> startBasketOut=new OutParam<IoxEvent>();
		        	readAheadEvent=readStartBasket(readAheadEvent,startBasketOut);
		        	return startBasketOut.value;
		        }else if(readAheadEvent.isEndElement()){
					state=AFTER_ENDBASKET;
					// see processing below
	        	}else{
	        		throw new IoxSyntaxException(unexpectedXmlEvent2msg(readAheadEvent));
	        	}
			}
			
			// after start basket
			if(state==AFTER_STARTBASKET) {
				if(readAheadEvent.isStartElement()) { // start object
                    StartElement startObjEle=readAheadEvent.asStartElement();
					state=AFTER_OBJECT;
					OutParam<IomObject> iomObjOut=new OutParam<IomObject>();
					readAheadEvent=readObject(readAheadEvent, iomObjOut); // read object;
					IomObject iomObj=iomObjOut.value;
					setOperation(startObjEle, iomObj);
					setConsistency(startObjEle, iomObj);
					return new ch.interlis.iox_j.ObjectEvent(iomObj); // return object
				}else if(readAheadEvent.isEndElement()) {
				    assert(readAheadEvent.asEndElement().getName().equals(getQName(currentTopicName)));
				    readAheadEvent=nextEvent(readAheadEvent);
					state=AFTER_ENDBASKET;
		        	return new ch.interlis.iox_j.EndBasketEvent(); // return end basket
				}else {
                    throw new IoxSyntaxException(unexpectedXmlEvent2msg(readAheadEvent));
				}
			}
			
			// after object
			if(state==AFTER_OBJECT) {
				if(readAheadEvent.isStartElement()){
				    StartElement startObjEle=readAheadEvent.asStartElement();
                    OutParam<IomObject> iomObjOut=new OutParam<IomObject>();
                    readAheadEvent=readObject(readAheadEvent, iomObjOut); // read object;
                    IomObject iomObj=iomObjOut.value;
					setOperation(startObjEle, iomObj);
					setConsistency(startObjEle, iomObj);
					return new ch.interlis.iox_j.ObjectEvent(iomObj); // return object
		        }else if(readAheadEvent.isEndElement()) {
		            assert(readAheadEvent.asEndElement().getName().equals(getQName(currentTopicName)));
	                readAheadEvent=nextEvent(readAheadEvent);
					state=AFTER_ENDBASKET;
		        	return new ch.interlis.iox_j.EndBasketEvent(); // return end basket
		        }else {
                    throw new IoxSyntaxException(unexpectedXmlEvent2msg(readAheadEvent));
		        }
			}
			
			// after end basket
			if(state==AFTER_ENDBASKET) {
		    	if(readAheadEvent.isStartElement()){
		    		state=AFTER_STARTBASKET;
		    		OutParam<IoxEvent> startBasketOut=new OutParam<IoxEvent>();
		        	readAheadEvent=readStartBasket(readAheadEvent,startBasketOut); // create new basket
		        	return startBasketOut.value;
		        }else if(readAheadEvent.isEndElement()) {
		        	if(readAheadEvent.asEndElement().getName().equals(getQName(NAME_XML_DATASECTION))){ // end data section
		        		readAheadEvent=nextEvent(readAheadEvent);
		        	}
		        	if(readAheadEvent.asEndElement().getName().equals(getQName(NAME_XML_TRANSFER))) { // end transfer
						readAheadEvent=nextEvent(readAheadEvent);
						if(readAheadEvent.isEndDocument()) { // end document
							state=AFTER_ENDTRANSFER;
							return new ch.interlis.iox_j.EndTransferEvent(); // return end transfer
						}else {
							throw new IoxSyntaxException(unexpectedXmlEvent2msg(readAheadEvent));
						}
					}else {
						throw new IoxSyntaxException(unexpectedXmlEvent2msg(readAheadEvent));
					}
		        }
			}
			// after end transfer
			if(state==AFTER_ENDTRANSFER) {
			    if(isXtf23Reader0_compatible) {
			        return null;
			    }
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(readAheadEvent));
			}
		}catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
		return null;
	}

	private QName getQName(String localName) {
        return new QName(ili22?NAMESPACE_XTF22:NAMESPACE_XTF23,localName);
    }

    private XMLEvent readStartBasket(javax.xml.stream.events.XMLEvent event,OutParam<IoxEvent> startBasketOut) throws IoxException, XMLStreamException {
		StartElement element = (StartElement) event;
		// topic name
		currentTopicName=getTopicFromQName(element.asStartElement().getName());
		if(currentTopicName==null) {
			throw new IoxException("missing topic name");
		}
		Attribute bid = element.getAttributeByName(QNAME_XML_TOPIC_BID);
		// create basket
		ch.interlis.iox_j.StartBasketEvent newObj=new ch.interlis.iox_j.StartBasketEvent(currentTopicName, stripX(bid.getValue()));
		newObj=setState(element, newObj);
		newObj=setConsistency(element, newObj);
		newObj=setDomain(element, newObj);
		newObj=setIncrementalKind(element, newObj);
		startBasketOut.value=newObj;
		event=nextEvent(event);
		return event; // return basket
	}
	
	private String getTopicFromQName(QName scopeName) {
        String iliQName=scopeName.getLocalPart();
        return iliQName;
	}
	private String getViewableFromQName(QName scopeName) {
		String iliQName=scopeName.getLocalPart();
		return iliQName;
	}

	private XMLEvent readHeaderSection(XMLEvent event, XtfStartTransferEvent xtfEvent) throws XMLStreamException, IoxException{
		// header section version
		Attribute version=event.asStartElement().getAttributeByName(QNAME_XML_HEADERSECTION_VERSION);
        if(version!=null && (version.getValue().equals(INTERLIS_VERSION_23)||version.getValue().equals(INTERLIS_VERSION_22))){
        	xtfEvent.setVersion(version.getValue());
        }else {
        	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
        }
        // header section sender
    	Attribute sender=event.asStartElement().getAttributeByName(QNAME_XML_HEADERSECTION_SENDER);
        if(sender!=null){
        	xtfEvent.setSender(sender.getValue());
        }else {
        	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
        }
		event=nextEvent(event);
		// start models
		if(event.isStartElement() && event.asStartElement().getName().equals(getQName(NAME_XML_MODELS))){
			event=readHeaderSectionModels(xtfEvent,event);
		}else if(!isXtf23Reader0_compatible){
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		if(event.isStartElement() && event.asStartElement().getName().equals(getQName(NAME_XML_ALIAS))) {
            event=readHeaderSectionAlias(event);
		}
		if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_OIDSPACES)) {
            event=readHeaderSectionOidSpaces(event,xtfEvent);
		}
		if(event.isStartElement() && event.asStartElement().getName().equals(getQName(QNAME_XML_COMMENT))) {
            event=readHeaderSectionComment(xtfEvent);
		}
        if(event.isEndElement()) {
            // end header section
            assert(event.asEndElement().getName().equals(getQName(NAME_XML_HEADERSECTION)));
        }else {
            throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
        }
		event=nextEvent(event);
		return event;
	}
	
	private XMLEvent readHeaderSectionModels(XtfStartTransferEvent xtfEvent,XMLEvent startElementModels) throws IoxException, XMLStreamException {
		XMLEvent event=null;
		if(startElementModels.isStartElement() && startElementModels.asStartElement().getName().equals(getQName(NAME_XML_MODELS))){ // start models
			; // skip start models event
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		event=nextEvent(event);
		models=new ArrayList<XtfModel>();
		
		// start model
		while(event.isStartElement() && event.asStartElement().getName().equals(getQName(NAME_XML_MODEL))) {
			event=readHeaderSectionModel(event);
		}
		
		// end Model(s)
		if(event.isEndElement() && event.asEndElement().getName().equals(getQName(NAME_XML_MODELS))) {
			if(models.size()==0) {
				throw new IoxException("expected at least 1 model.");
			}
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		event=nextEvent(event);
		return event;
	}
	
	private XMLEvent readHeaderSectionModel(XMLEvent startElementModel) throws IoxException, XMLStreamException {
		XMLEvent event=null;
		// start model name
		Attribute name=startElementModel.asStartElement().getAttributeByName(QNAME_XML_MODEL_NAME);
		Attribute version=startElementModel.asStartElement().getAttributeByName(QNAME_XML_MODEL_VERSION);
		Attribute uri=startElementModel.asStartElement().getAttributeByName(QNAME_XML_MODEL_URI);
        if(name!=null && version!=null && uri!=null){
        	// add model to models
            XtfModel xtfModel=new XtfModel(name.getValue(),version.getValue(),uri.getValue());
        	models.add(xtfModel);
        	
        }else if(name!=null && isXtf23Reader0_compatible){
                // add model to models
                XtfModel xtfModel=new XtfModel(name.getValue(),version!=null?version.getValue():null,uri!=null?uri.getValue():null);
                models.add(xtfModel);
        }else {
        	throw new IoxSyntaxException(unexpectedXmlEvent2msg(startElementModel));
        }
        event=nextEvent(event);
        
        // end element Model
 		if(event.isEndElement() && event.asEndElement().getName().equals(getQName(NAME_XML_MODEL))) {
 			; // skip end model event
 		}else {
 			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event)); 
 		}
 		event=nextEvent(event);
 		return event;
	}
	
	private XMLEvent readHeaderSectionAlias(XMLEvent event) throws IoxException, XMLStreamException {
		// start alias
		if(event.isStartElement()){
			assert(event.asStartElement().getName().equals(getQName(NAME_XML_ALIAS)));
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		event=nextEvent(event);
		
		// start entries
        while(event.isStartElement() && event.asStartElement().getName().equals(getQName(QNAME_XML_ENTRIES))) {
            event = readAliasEntries(event);
        }
    	if(event.isStartElement() && !event.asStartElement().getName().equals(getQName(QNAME_XML_ENTRIES))) {
    		throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
    	}
    	
    	// end alias
		if(event.isEndElement()) {
			assert(event.asEndElement().getName().equals(getQName(NAME_XML_ALIAS)));
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		event=nextEvent(event);
		return event;
	}

	private XMLEvent readAliasEntries(XMLEvent event) throws IoxSyntaxException, XMLStreamException, IoxException {
		// start entries
		Attribute entryFor=event.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_FOR);
    	if(entryFor!=null){
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
    	event=nextEvent(event);
		
		// start entry
		while(event.isStartElement() &&
			 (event.asStartElement().getName().equals(getQName(QNAME_XML_ENTRIES_TAGENTRY)) ||
			  event.asStartElement().getName().equals(getQName(QNAME_XML_ENTRIES_VALENTRY)) ||
			  event.asStartElement().getName().equals(getQName(QNAME_XML_ENTRIES_DELENTRY)))){
			event=readAliasEntry(event);
		}
		
		// end entries
		if(event.isEndElement() && event.asEndElement().getName().equals(getQName(QNAME_XML_ENTRIES))){
			; // skip end entries event
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		event=nextEvent(event);
		return event;
	}

	private XMLEvent readAliasEntry(XMLEvent startElementAliasEntry) throws IoxSyntaxException, IoxException, XMLStreamException {
		XMLEvent event=null;
		if(startElementAliasEntry.isStartElement() && startElementAliasEntry.asStartElement().getName().equals(getQName(QNAME_XML_ENTRIES_TAGENTRY))){
			// start tag entry
			if(startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_FROM)!=null &&
				startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_TO)!=null){
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
			event=nextEvent(event);
			// end tag entry
			if(event.isEndElement() && event.asEndElement().getName().equals(getQName(QNAME_XML_ENTRIES_TAGENTRY))){
				; // skip end tagentry event
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
			event=nextEvent(event);
		}else if(startElementAliasEntry.isStartElement() && startElementAliasEntry.asStartElement().getName().equals(getQName(QNAME_XML_ENTRIES_VALENTRY))){
			// start val entry
			if(startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_TAG)!=null &&
					startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_ATTR)!=null &&
					startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_FROM)!=null &&
					startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_TO)!=null){
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(startElementAliasEntry));
			}
			event=nextEvent(event);
			
			// end val entry
			if(event.isEndElement() && event.asEndElement().getName().equals(getQName(QNAME_XML_ENTRIES_VALENTRY))){
				; // skip end valentry event
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
			event=nextEvent(event);
		}else if(startElementAliasEntry.isStartElement() && startElementAliasEntry.asStartElement().getName().equals(getQName(QNAME_XML_ENTRIES_DELENTRY))){
			// start del entry
			if(startElementAliasEntry.asStartElement().getAttributeByName(QNAME_XML_ENTRIES_VALUE_TAG)!=null) {
				; // skip start entries event
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(startElementAliasEntry));
			}
			event=nextEvent(event);
			
			// end del entry
			if(event.isEndElement() && event.asEndElement().getName().equals(getQName(QNAME_XML_ENTRIES_DELENTRY))){
				; // skip end delentry event
			}else {
				throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
			}
			event=nextEvent(event);
		}
		return event;
	}
	
	private XMLEvent readHeaderSectionOidSpaces(XMLEvent event,XtfStartTransferEvent xtfEvent) throws XMLStreamException, IoxException {
	    event=nextEvent(event);
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
		if(event.isEndElement()) {
			assert(event.asEndElement().getName().equals(QNAME_XML_OIDSPACES)); // skip end oidspaces event
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));  
		}
		event=nextEvent(event);
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
		event=nextEvent(event);
		
		// end oid space
		if(event.isEndElement()){
			assert(event.asEndElement().getName().equals(QNAME_XML_OIDSPACE)); // skip end oidspace event
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		event=nextEvent(event);
		return event;
	}
	
	private XMLEvent readHeaderSectionComment(XtfStartTransferEvent xtfEvent) throws IoxSyntaxException, XMLStreamException {
		// start comment
		XMLEvent event=reader.nextEvent();
		event=skipCommentary(event);
		StringBuffer value=new StringBuffer();
		event=readSimpleContent(event,value);
		xtfEvent.setComment(value.toString());
		// end comment
		if(event.isEndElement()) {
			assert(event.asEndElement().getName().equals(getQName(QNAME_XML_COMMENT)));
		}else {
			throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		}
		event=nextEvent(event);
		return event;
	}

	private XMLEvent skipCommentary(XMLEvent event) throws XMLStreamException {
		 while(event.getEventType()==XMLEvent.COMMENT){
			 event=reader.nextEvent();
	     }
	     return event;
	}

	private XMLEvent readObject(XMLEvent event, OutParam<IomObject> iomObjOut) throws IoxException {
		try {
	    	if(!event.isStartElement()){
                throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	    	}
            Attribute oid = event.asStartElement().getAttributeByName(QNAME_XML_OBJECT_TID);
            if(event.asStartElement().getAttributeByName(QNAME_XML_TOPIC_BID) != null){
                throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
            }
            // DeleteObject
            if(event.asStartElement().getName().equals(getQName(NAME_XML_DELETE))) {
                event = readDelete(event, iomObjOut);
                return event;
            }else{
                // IomObject
                String currentViewableName=getViewableFromQName(event.asStartElement().getName());
                if(currentViewableName==null) {
                    throw new IoxException("missing table name");
                }
                if(oid!=null){ // create object
                    iomObjOut.value=createIomObject(currentViewableName, stripX(oid.getValue()));
                }else{
                    iomObjOut.value=createIomObject(currentViewableName, null);
                }
                iomObjOut.value.setobjectline(event.getLocation().getLineNumber());
                event=nextEvent(event);
                // event is startElement of attribute or endElement of object 
            }
            while(event.isStartElement()){
                event=readAttribute(event, iomObjOut.value); // read object attribute
                // event is startElement of new attribute or endElement of object 
            }
            event=nextEvent(event);
            // event is startElement of new object or endElement of basket 
	    	if(event.isEndElement() || event.isStartElement()) { 
	    		return event; // return object
	        }else{
	        	throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
	        }
		}catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
	}

	private XMLEvent readDelete(XMLEvent event, OutParam<IomObject> iomObjOut) throws IoxException, XMLStreamException {
		final Attribute attrTid = event.asStartElement().getAttributeByName(QNAME_XML_OBJECT_TID);
        if(attrTid!=null){
			// create delete object
			iomObjOut.value=createIomObject(Iom_jObject.DELETE, stripX(attrTid.getValue()));
			event=nextEvent(event); // after start delete object
			if(event.isEndElement()){ // end delete object
				state=AFTER_STARTBASKET;
				if(!event.asEndElement().getName().equals(getQName(NAME_XML_DELETE))){
					throw new IoxException("expected rolename and role reference tid");
				}
			}else{
				throw new IoxException("delete references are not yet implemented.");
			}
			event=nextEvent(event);
		}else{
			throw new IoxException("delete object needs tid");
		}
		return event;
	}

	private IomObject setConsistency(StartElement element, IomObject iomObj) throws IoxException {
        Attribute consistency = element.getAttributeByName(QNAME_XML_CONSISTENCY);
        if(consistency!=null){
        	String attrValue=consistency.getValue();
        	if(attrValue.equals(QNAME_XML_TOPIC_CONSISTENCY_COMPLETE)){
        		iomObj.setobjectoperation(IomConstants.IOM_COMPLETE); // 0=COMPLETE
        	}else if(attrValue.equals(QNAME_XML_TOPIC_CONSISTENCY_INCOMPLETE)){
        		iomObj.setobjectoperation(IomConstants.IOM_INCOMPLETE); // 1=INCOMPLETE
        	}else if(attrValue.equals(QNAME_XML_TOPIC_CONSISTENCY_INCONSISTENT)){
        		iomObj.setobjectoperation(IomConstants.IOM_INCONSISTENT); // 2=INCONSISTENT
        	}else if(attrValue.equals(QNAME_XML_TOPIC_CONSISTENCY_ADAPTED)){
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
        	if(attrValue.equals(XML_OPERATION_INSERT)){
        		iomObj.setobjectoperation(IomConstants.IOM_OP_INSERT); // op(0=insert)
        	}else if(attrValue.equals(QNAME_XML_TOPIC_KIND_UPDATE)){
        		iomObj.setobjectoperation(IomConstants.IOM_OP_UPDATE); // op(1=update)
        	}else if(attrValue.equals(XML_OPERATION_DELETE)){
        		iomObj.setobjectoperation(IomConstants.IOM_OP_DELETE); // op(2=delete)
        	}else{
				throw new IoxException("unexpected operation <"+attrValue+">");
        	}
        }
        return iomObj;
	}

	private ch.interlis.iox_j.StartBasketEvent setState(StartElement element, ch.interlis.iox_j.StartBasketEvent startBasketEvent) throws IoxException {
		Iterator codingObjIter=element.getAttributes();
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
				if(codingObj.getValue().equals(QNAME_XML_TOPIC_KIND_FULL)){
					startBasketEvent.setKind(IomConstants.IOM_FULL);
				}else if(codingObj.getValue().equals(QNAME_XML_TOPIC_KIND_UPDATE)){
					startBasketEvent.setKind(IomConstants.IOM_UPDATE);
				}else if(codingObj.getValue().equals(QNAME_XML_TOPIC_KIND_INITIAL)){
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
				if(codingObj.getValue().equals(QNAME_XML_TOPIC_CONSISTENCY_COMPLETE)){
					startBasketEvent.setConsistency(IomConstants.IOM_COMPLETE); // 0=COMPLETE
				}else if(codingObj.getValue().equals(QNAME_XML_TOPIC_CONSISTENCY_INCOMPLETE)){
					startBasketEvent.setConsistency(IomConstants.IOM_INCOMPLETE); // 1=INCOMPLETE
				}else if(codingObj.getValue().equals(QNAME_XML_TOPIC_CONSISTENCY_INCONSISTENT)){
					startBasketEvent.setConsistency(IomConstants.IOM_INCONSISTENT); // 2=INCONSISTENT
				}else if(codingObj.getValue().equals(QNAME_XML_TOPIC_CONSISTENCY_ADAPTED)){
					startBasketEvent.setConsistency(IomConstants.IOM_ADAPTED); // 3=ADAPTED
				}else {
					throw new IoxException("unexpected consistency <"+codingObj.getValue()+">");
				}
			}
		}
		return startBasketEvent;
	}

	private XMLEvent collectXMLElement(XMLEvent event, java.io.StringWriter strw) throws XMLStreamException, IoxSyntaxException {
        XMLOutputFactory xmloutputf = XMLOutputFactory.newInstance();
        xmloutputf.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES,true);
        XMLEventFactory.newInstance();
        XMLEventWriter xmlw = xmloutputf.createXMLEventWriter(strw); 
        //xmlw.add(xmlef.createStartDocument());
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
        //xmlw.add(xmlef.createEndDocument());
        xmlw.flush();
        xmlw.close();
        event = nextEvent(event);
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
	
    private XMLEvent nextEvent(XMLEvent event) throws XMLStreamException, IoxSyntaxException {
        event=reader.nextEvent();
        while(event!=null && (event.isCharacters() || event.getEventType()==XMLEvent.COMMENT)){
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
	/** Read Characters of characterEvent and check if whitespace. Return nextEvent()
	 * @param event
	 * @return XMLEvent event
	 * @throws XMLStreamException
	 * @throws IoxSyntaxException
	 */
	private XMLEvent skipSpaces(XMLEvent event) throws XMLStreamException, IoxSyntaxException {
        while(event!=null && (event.isCharacters() || event.getEventType()==XMLEvent.COMMENT)){
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
    private XMLEvent skipSpacesAndStopAtContent(XMLEvent event) throws XMLStreamException, IoxSyntaxException {
        while(event!=null && (event.isCharacters() || event.getEventType()==XMLEvent.COMMENT)){
            if(event.isCharacters()) {
                Characters characters = (Characters) event;
                if(!characters.isWhiteSpace()){
                    return event;
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
		}else {
            output.append(actual.toString());
		}
		output.append(" found.");
		return output.toString();
	}
    
    private XMLEvent readAttribute(XMLEvent event, IomObject iomObj) throws IoxException {
    	try {
    		String attrName=null;
	    	StartElement startElement = event.asStartElement();
	    	// reference
	    	// element is startElement of attribute
	    	if(startElement.getAttributeByName(QNAME_XML_REF)!=null){
            	event=readReference(iomObj, startElement, startElement.getName().getLocalPart());
            	// event is startElement of next attribute or endElement of object
            }else if(startElement.getAttributeByName(QNAME_XML_OID)!=null) {
            	Attribute oidAttr=startElement.getAttributeByName(QNAME_XML_OID);
            	attrName=startElement.getName().getLocalPart();
            	iomObj.setattrvalue(attrName, oidAttr.getValue());
            	event=nextEvent(event);
                event=nextEvent(event);
    		}else {
	            // attribute
		    	attrName=startElement.getName().getLocalPart();
		    	if(attrName==null){
		    		throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
		    	}
		    	event=reader.nextEvent();
		    	// event is content or startElement of struct or endElement of attribute
	            if(event.isCharacters()){ 
	            	StringBuffer value=new StringBuffer();
	            	event=readSimpleContent(event,value);
	            	// event is endElement of attribute
	                if(event.isEndElement()) {
	                    // ok, is content and not only white space
	                    iomObj.setattrvalue(attrName, value.toString());
	                }
	            }
	            if(!event.isStartElement() && !event.isEndElement()) {
	                event=nextEvent(event);
	            }
	            if(event.isStartElement()) {
	                if(event.isStartElement() && (event.asStartElement().getName().equals(getQName(QNAME_XML_COORD)) || event.asStartElement().getName().equals(getQName(QNAME_XML_ARC)))){
	                    // COORD
	                	String segmentType=null;
	                	if(event.asStartElement().getName().equals(getQName(QNAME_XML_COORD))) {
	                    	segmentType=Iom_jObject.COORD;
	                    }else if(event.asStartElement().getName().equals(getQName(QNAME_XML_ARC))) {
	                    	segmentType=Iom_jObject.ARC;
	                    }
	                    OutParam<IomObject> returnedSegment=new OutParam<IomObject>();
	                    event=readSegment(event, segmentType,returnedSegment);
	                    if(returnedSegment.value.getattrcount()==0){
	                    	throw new IoxException("expected coord. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
						iomObj.addattrobj(attrName, returnedSegment.value);
	                }else if(event.isStartElement() && event.asStartElement().getName().equals(getQName(QNAME_XML_POLYLINE))){
	                    // POLYLINE
	                    OutParam<IomObject> polylineOut=new OutParam<IomObject>();
	                    event=readPolyline(event,polylineOut);
	                    IomObject polyline = polylineOut.value;
	                    iomObj.addattrobj(attrName, polyline);
	                }else if(event.isStartElement() && event.asStartElement().getName().equals(getQName(QNAME_XML_SURFACE))){
	                    // SURFACE
	                    // SURFACE (surface/area)
	                	IomObject multiSurface=createIomObject(Iom_jObject.MULTISURFACE, null);
                        OutParam<IomObject> surfaceOut=new OutParam<IomObject>();
                        event=readSurface(event,surfaceOut);
	                	IomObject surface=surfaceOut.value;
	                	if(surface.getattrcount()==0){
	                    	throw new IoxException("expected surface");
	                    }
	                	multiSurface.addattrobj(Iom_jObject.MULTISURFACE_SURFACE, surface);
	                	iomObj.addattrobj(attrName, multiSurface);
	                }else if(event.isStartElement() && (event.asStartElement().getName().equals(getQName(QNAME_XML_XMLBLBOX)))){
                        event=nextEvent(event); // skip XMLBLBOX
	                    // BLACKBOX (binary and/or xml)
	                	java.io.StringWriter strw=new java.io.StringWriter();
	                	event=collectXMLElement(event, strw); // start BLACKBOX
	                	assert(event.isEndElement() && event.asEndElement().getName().equals(getQName(QNAME_XML_XMLBLBOX)));
	        			iomObj.setattrvalue(attrName, strw.toString());
                        event=nextEvent(event); // skip end of XMLBLBOX
                    }else if(event.isStartElement() && (event.asStartElement().getName().equals(getQName(QNAME_XML_BINBLBOX))) ){
                        event=reader.nextEvent(); // skip BLBOX
                        StringBuffer value=new StringBuffer();
                        event=readSimpleContent(event,value);
                        assert(event.isEndElement() && event.asEndElement().getName().equals(getQName(QNAME_XML_BINBLBOX)));
                        iomObj.setattrvalue(attrName, value.toString());
                        event=nextEvent(event); // skip end of XMLBLBOX
	                }else{
	    				// structure
	                    do {
	                        OutParam<IomObject> iomObjOut=new OutParam<IomObject>();
	                        event=readObject(event, iomObjOut); // read object;
	                        IomObject structObj=iomObjOut.value;
	                        iomObj.addattrobj(attrName, structObj);
	                    }while(event.isStartElement());
	                }
		    	}
	            // event is endElement of attribute 
                if(!event.isEndElement()){
                    throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
                }
                event=nextEvent(event);
	            // event is startElement of next attribute or endElement of object
            }
    	}catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
    	return event;
    }

	private XMLEvent readReference(IomObject iomObj, StartElement element, String attrName) throws IoxException, XMLStreamException{
	    
	    String refOid=null;
	    final Attribute refAttr = element.getAttributeByName(QNAME_XML_REF);
        if (refAttr != null) {
	        refOid=stripX(refAttr.getValue());
	        if(refOid.length()==0){
	            throw new IoxException("unexpected reference value <"+refOid+">");
	        }		    
		}

		Attribute attrRefBid=element.getAttributeByName(QNAME_XML_TOPIC_BID);
		String refBid=null;
		if(attrRefBid!=null) {
			refBid=stripX(attrRefBid.getValue());
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
        XMLEvent event = nextEvent(element);
        // event is startElement of linkObj or endElement of attribute
        IomObject refObject=null;
        if (event.isStartElement()) {
            element = (StartElement) event;
            OutParam<IomObject> iomObjOut=new OutParam<IomObject>();
            event=readObject(event, iomObjOut); // read object;
            refObject=iomObjOut.value;
            iomObj.addattrobj(attrName, refObject);
        } else {
            refObject=iomObj.addattrobj(attrName,Iom_jObject.REF);
        }
        event = nextEvent(event);
        // event is startElement of next attribute or endElement of object
        if(event.isStartElement() || event.isEndElement()) {
            ;
        }else {
            throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
        }
        refObject.setobjectrefoid(refOid); // set reference
        if(orderPos!=null){
            refObject.setobjectreforderpos(orderPos);
        }
        if(refBid!=null) {
            refObject.setobjectrefbid(refBid);
        }            

		return event;
	}
    
    /** Prepare surface
     * @param event
     * @return IomObject surface
     * @throws IoxException
     * @throws XMLStreamException
     */
    private XMLEvent readSurface(XMLEvent event,OutParam<IomObject> surfaceOut) throws IoxException, XMLStreamException {
        assert(event.isStartElement() && event.asStartElement().getName().equals(getQName(QNAME_XML_SURFACE)));
    	IomObject surface=createIomObject(Iom_jObject.SURFACE, null);
    	surfaceOut.value=surface;
    	event=nextEvent(event);
    	while(event.isStartElement() && (event.asStartElement().getName().equals(getQName(QNAME_XML_BOUNDARY)))){
    	    OutParam<IomObject> boundaryOut=new OutParam<IomObject>();
    	    event=readBoundary(event,boundaryOut);
	    	surface.addattrobj(Iom_jObject.SURFACE_BOUNDARY, boundaryOut.value);
    	}
        assert(event.isEndElement() && event.asEndElement().getName().equals(getQName(QNAME_XML_SURFACE)));
        event=nextEvent(event);
    	return event;
    }
    
    /** Prepare boundary
     * @param event
     * @return IomObject boundary
     * @throws IoxException
     * @throws XMLStreamException
     */
    private XMLEvent readBoundary(XMLEvent event,OutParam<IomObject> boundaryOut) throws IoxException, XMLStreamException {
        assert(event.isStartElement() && event.asStartElement().getName().equals(getQName(QNAME_XML_BOUNDARY)));
    	IomObject boundary=createIomObject(Iom_jObject.BOUNDARY, null);
    	boundaryOut.value=boundary;
    	event=nextEvent(event);
    	while(event.isStartElement() && event.asStartElement().getName().equals(getQName(QNAME_XML_POLYLINE))){
            OutParam<IomObject> polylineOut=new OutParam<IomObject>();
            event=readPolyline(event,polylineOut);
	    	boundary.addattrobj(Iom_jObject.BOUNDARY_POLYLINE, polylineOut.value);
    	}
        if(boundary.getattrcount()==0){
            throw new IoxException("at least one POLYLINE expected");
        }
        assert(event.isEndElement() && event.asEndElement().getName().equals(getQName(QNAME_XML_BOUNDARY)));
        event=nextEvent(event);
    	return event;
    }
    
    /** Prepare polyline
     * @param event
     * @return IomObject polyline
     * @throws IoxException
     * @throws XMLStreamException
     */
    private XMLEvent readPolyline(XMLEvent event,OutParam<IomObject> polylineOut) throws IoxException, XMLStreamException {
        assert(event.isStartElement() && event.asStartElement().getName().equals(getQName(QNAME_XML_POLYLINE)));
    	IomObject polyline=createIomObject(Iom_jObject.POLYLINE, null);
		event=nextEvent(event);
		if(event.isStartElement()) {
	        OutParam<IomObject> seqOut=new OutParam<IomObject>();
	        event=readSequence(event,seqOut);
	        polyline.addattrobj(Iom_jObject.POLYLINE_SEQUENCE, seqOut.value);
		}
        if(polyline.getattrcount()==0){
            throw new IoxException("at least one COORD expected");
        }
		polylineOut.value=polyline;
        assert(event.isEndElement() && event.asEndElement().getName().equals(getQName(QNAME_XML_POLYLINE)));
        event=nextEvent(event);
    	return event;
    }
    
    /** Prepare sequence
     * @param event
     * @return IomObject sequence
     * @throws XMLStreamException
     * @throws IoxException
     */
	private XMLEvent readSequence(XMLEvent event,OutParam<IomObject> seqOut) throws XMLStreamException, IoxException {
        assert(event.isStartElement() && event.asStartElement().getName().equals(getQName(QNAME_XML_COORD)));
		IomObject sequence=createIomObject(Iom_jObject.SEGMENTS, null);
        seqOut.value=sequence;
        while(event.isStartElement()){
            String segmentType=null;
            if(event.asStartElement().getName().equals(getQName(QNAME_XML_COORD))){
                segmentType=Iom_jObject.COORD;
            }else if(event.asStartElement().getName().equals(getQName(QNAME_XML_ARC))){
                segmentType=Iom_jObject.ARC;
            }
            OutParam<IomObject> segment=new OutParam<IomObject>();
            event=readSegment(event, segmentType,segment);
            sequence.addattrobj(Iom_jObject.SEGMENTS_SEGMENT, segment.value);
        }
        assert(event.isEndElement() && event.asEndElement().getName().equals(getQName(QNAME_XML_POLYLINE)));
		return event;
	}
    
	/** Prepare segment
	 * @param event
	 * @param segmentType
	 * @return IomObject segment
	 * @throws IoxException
	 * @throws XMLStreamException
	 */
	private XMLEvent readSegment(XMLEvent event, String segmentType,OutParam<IomObject> segmentOut) throws IoxException, XMLStreamException {
        if(!event.isStartElement()){
            throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
        }
		if(segmentType==null){
			throw new IoxException("expected segment type");
		}
		IomObject segment=createIomObject(segmentType, null);
		segmentOut.value=segment;
		SegmentType segTypeName=null;
        event=nextEvent(event);
		while(event.isStartElement()){
            String segmentTypeName=event.asStartElement().getName().getLocalPart();
            segTypeName = SegmentType.valueOf(segmentTypeName);
            event=reader.nextEvent();
            if(!event.isCharacters()){
                throw new IoxSyntaxException("expected coord");
            }
            StringBuffer value=new StringBuffer();
            event=readSimpleContent(event,value);
            if(StringUtility.purge(value.toString())==null) {
                throw new IoxSyntaxException("expected coord");
            }
            // event is endElement of attribute
            if(!event.isEndElement()) {
                throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
            }
            switch(segTypeName){
                // could have an 1-n events of chars.
                case C1: segment.setattrvalue(Iom_jObject.COORD_C1, value.toString());
                    break;
                case C2: segment.setattrvalue(Iom_jObject.COORD_C2, value.toString());
                    break;
                case C3: segment.setattrvalue(Iom_jObject.COORD_C3, value.toString());
                    break;
                case A1: segment.setattrvalue(Iom_jObject.ARC_A1, value.toString());
                    break;
                case A2: segment.setattrvalue(Iom_jObject.ARC_A2, value.toString());
                    break;
                case R: segment.setattrvalue(Iom_jObject.ARC_R, value.toString());
                    break;
                default: throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
            }
	        event=nextEvent(event);
	        // event is end COORD or start C2
		}
		// skip end COORD
        if(!event.isEndElement()){
            throw new IoxSyntaxException(unexpectedXmlEvent2msg(event));
        }
        event=nextEvent(event);
		return event;
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
        if(isIli22()) {
            return XTF_22;
        }
        return XTF_23;
    }
    public boolean isIli22() {
        return ili22;
    }
    private String stripX(String oid){
        if(oid==null){
            return null;
        }
        if(!ili22){
            return oid;
        }
        // ASSERT: isIli22
        if (!oid.startsWith("x")){
            // leading 'x' is mandatory
          throw new IllegalArgumentException ("OID requires a leading 'x'");
          //return oid;
        }
        // remove leading 'x' to get real OID
        return oid.substring(1);
    }

    public void setStrict(boolean b) {
        isXtf23Reader0_compatible=false;
    }
    
}