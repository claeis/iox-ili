package ch.interlis.iom_j.xtf;

import java.awt.Event;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.Container;
import ch.interlis.ili2c.metamodel.DataModel;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.View;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.ili2c.metamodel.ViewableTransferElement;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.xtf.impl.XtfWriterAlt;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iox.IoxReader;
import ch.interlis.iox_j.IoxIliReader;
import ch.interlis.iox_j.IoxSyntaxException;

public class Xtf24Reader implements IoxReader ,IoxIliReader{
	private XMLEventReader reader=null;
	private IoxFactoryCollection factory=new  ch.interlis.iox_j.DefaultIoxFactoryCollection();
	private java.io.InputStream inputFile=null;
    private ArrayList<String> models=new ArrayList<String>();
	private Topic currentTopic=null;
	private Viewable viewable=null;
	private TransferDescription td;
	private int state = START;
	
    // state
    private static final int START=0;
    private static final int AFTER_STARTTRANSFER=1;
    private static final int AFTER_STARTBASKET=2;
    private static final int AFTER_OBJECT=3;
    private static final int AFTER_ENDBASKET=4;
    private static final int AFTER_ENDTRANSFER=5;
    
    // ili elements
    private HashMap<QName, Topic> iliTopics=null;
    private HashMap<QName, Viewable> iliClasses=null;
    private HashMap<Viewable, HashMap<QName, Element>> iliProperties=null;
    
    // segmentType
    private enum SegmentType {
    	C1,C2,C3,A1,A2,R,MULTICOORD
    }
    
    // enum
    private static final String SEGMENTTYPE_COORD="COORD";
    private static final String SEGMENTTYPE_ARC="ARC";
    // namespace
    private static final String NAMESPACE_ILIXMLBASE="http://www.interlis.ch/xtf/2.4/";
    private static final String NAMESPACE_ILIXMLBASE_INTERLIS=NAMESPACE_ILIXMLBASE+"INTERLIS";
    private static final String NAMESPACE_GEOM="http://www.interlis.ch/geometry/1.0";
    private static final String NAMESPACE_XMLSCHEMA="http://www.w3.org/2001/XMLSchema-instance";
	// qnames xml
    private static final QName QNAME_XML_HEADERSECTION=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"headersection");
    private static final QName QNAME_XML_HEADERSECTION_SENDER=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"sender");
    private static final QName QNAME_XML_HEADERSECTION_COMMENT=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"comments");
    private static final QName QNAME_XML_DATASECTION=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"datasection");
    private static final QName QNAME_XML_MODELS=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"models");
    private static final QName QNAME_XML_MODEL=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"model");
    // qnames ili
    private static final QName QNAME_ILI_BID = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "bid");
    private static final QName QNAME_ILI_TID = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "tid");
    private static final QName QNAME_ILI_ORDERPOS = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "order_pos");
    private static final QName QNAME_ILI_OPERATION = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "operation");
    private static final QName QNAME_ILI_TRANSFER=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"transfer");
    private static final QName QNAME_ILI_REF = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "ref");
    private static final QName QNAME_ILI_DELETE=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"delete");
    private static final QName QNAME_ILI_STARTSTATE=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"startstate");
    private static final QName QNAME_ILI_ENDSTATE=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"endstate");
    private static final QName QNAME_ILI_KIND=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"kind");
    private static final QName QNAME_ILI_DOMAIN=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"domains");
    private static final QName QNAME_ILI_CONSISTENCY=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"consistency");
    // qnames geom
    private static final QName QNAME_GEOM_COORD = new QName(NAMESPACE_GEOM, "coord");
    private static final QName QNAME_GEOM_ARC = new QName(NAMESPACE_GEOM, "arc");
    private static final QName QNAME_GEOM_MULTICOORD = new QName(NAMESPACE_GEOM, "multicoord");
    private static final QName QNAME_GEOM_POLYLINE = new QName(NAMESPACE_GEOM, "polyline");
    private static final QName QNAME_GEOM_MULTIPOLYLINE = new QName(NAMESPACE_GEOM, "multipolyline");
    private static final QName QNAME_GEOM_LINESTRING = new QName(NAMESPACE_GEOM, "linestring");
    private static final QName QNAME_GEOM_ORIENTABLECURVE = new QName(NAMESPACE_GEOM, "orientablecurve");
	private static final QName QNAME_GEOM_COMPOSITECURVE = new QName(NAMESPACE_GEOM, "compositecurve");
	private static final QName QNAME_GEOM_SURFACE = new QName(NAMESPACE_GEOM, "surface");
	private static final QName QNAME_GEOM_MULTISURFACE = new QName(NAMESPACE_GEOM, "multisurface");
	private static final QName QNAME_GEOM_AREA = new QName(NAMESPACE_GEOM, "area");
	private static final QName QNAME_GEOM_MULTIAREA = new QName(NAMESPACE_GEOM, "multiarea");
	private static final QName QNAME_GEOM_EXTERIOR = new QName(NAMESPACE_GEOM, "exterior");
	private static final QName QNAME_GEOM_INTERIOR = new QName(NAMESPACE_GEOM, "interior");
    
    /** Creates a new reader.
     * @param in Input stream to read from
     * @throws IoxException
     */
	public Xtf24Reader(java.io.InputStream in) throws IoxException{
		init(in);
	}
	
	/** Creates a new reader.
	 * @param in Input stream reader to read from
	 * @throws IoxException
	 */
	public Xtf24Reader(java.io.InputStreamReader in) throws IoxException{
	}
	
	/** Creates a new reader.
	 * @param xtffile File to read from
	 * @throws IoxException
	 */
	public Xtf24Reader(java.io.File xtffile) throws IoxException{
		try{
			inputFile=new java.io.FileInputStream(xtffile);
			init(inputFile);
		}catch(java.io.IOException ex){
			throw new IoxException(ex);
		}
	}
    public static IoxReader createReader(java.io.File xtffile) throws IoxException{
        javax.xml.stream.XMLInputFactory inputFactory = javax.xml.stream.XMLInputFactory.newInstance();
        XMLEventReader reader=null;
        java.io.FileInputStream in=null;
        javax.xml.stream.events.XMLEvent event=null;
        try {
            in=new java.io.FileInputStream(xtffile);
            reader = inputFactory.createXMLEventReader(in);
            event=reader.nextEvent();
            while(event!=null && !event.isStartElement()){
                event=reader.nextEvent();
            }
        } catch (XMLStreamException e) {
            throw new IoxException(e);
        } catch (FileNotFoundException e) {
            throw new IoxException(e);
        }finally {
            if(reader!=null) {
                try {
                    reader.close();
                } catch (XMLStreamException e) {
                }
                reader=null;
            }
            if(in!=null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
                in=null;
            }
        }
        if(event!=null) {
            String ns=event.asStartElement().getName().getNamespaceURI();
            if(ns.equals(NAMESPACE_ILIXMLBASE_INTERLIS)) {
                return new Xtf24Reader(xtffile);
            }else if(ns.equals(Xtf23Reader.NAMESPACE_ILIXMLBASE)) {
                return new XtfReader(xtffile);
            }else if(ns.equals(XtfWriterAlt.ili22Ns)) {
                return new XtfReader(xtffile);
            }else {
                throw new IoxException("unexpected namesapce "+ns);
            }
        }
        return new XtfReader(xtffile);
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
	
	/** Sets the Interlis model.
	 * @param td
	 */
	@Override
	public void setModel(TransferDescription td){
		this.td=td;
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

	/** Iterator through all events of document
	 * the minimum requirements to represent a document has to include:
	 * -a header section
	 * -at least 1 model in header section
	 * -a data section
	 */
	@Override
	public IoxEvent read() throws IoxException {
		IomObject iomObj=null;
		try {
			javax.xml.stream.events.XMLEvent event=null;
			if(state==START){
				// after start
				event=reader.nextEvent();
				if(event.isStartDocument()){
					; // skip start document event
				}else {
					throw new IoxSyntaxException(event2msgtext(event));
				}
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
				// after start document
				if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_ILI_TRANSFER)){
					; // skip start transfer event
                }else {
                	throw new IoxSyntaxException(event2msgtext(event));
                }
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
				XtfStartTransferEvent startTransferEvent=null;
            	// after start transfer
                if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_HEADERSECTION)){
                	startTransferEvent=new XtfStartTransferEvent();
					event=readHeaderSection(event, startTransferEvent); // start header section
                }else {
                	throw new IoxSyntaxException(event2msgtext(event));
                }
                event=reader.nextEvent();
                event=skipSpacesAndGetNextEvent(event);
                // after header section
                if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_DATASECTION)){ // start data section
                	; // skip start data section event
	        	}else {
	        		throw new IoxSyntaxException(event2msgtext(event)); // not data section
	        	}
                // after end header section
            	HashMap<String,IomObject> modelx=new HashMap<String,IomObject>();
        		for(String modelName:models){
                    IomObject model=createIomObject(ch.interlis.iom_j.xtf.impl.MyHandler.HEADER_OBJECT_MODELENTRY,hsNextOid());
        			model.setattrvalue(ch.interlis.iom_j.xtf.impl.MyHandler.HEADER_OBJECT_MODELENTRY_NAME,modelName);
        			modelx.put(model.getobjectoid(),model);
        		}
        		startTransferEvent.setHeaderObjects(modelx); // header additional info
                state=AFTER_STARTTRANSFER;
                return startTransferEvent; // return start transfer
            }
			
			// after start data section
			if(state==AFTER_STARTTRANSFER) {
				event=reader.nextEvent(); // after start transfer
				event=skipSpacesAndGetNextEvent(event);
                if(event.isStartElement()){ // start basket
                	state=AFTER_STARTBASKET;
                	return createBasket(event); // create new basket
                }else if(event.isEndElement()){
					state=AFTER_ENDBASKET;
					// see processing below
                }else{
            		throw new IoxSyntaxException(event2msgtext(event));
            	}
			}
			
			// after start basket
			if(state==AFTER_STARTBASKET) {
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
				if(event.isStartElement()) { // start object
					state=AFTER_OBJECT;
					iomObj=readObject(event, iomObj); // read object
					setOperation(event.asStartElement(), iomObj);
					setConsistency(event.asStartElement(), iomObj);
                	return new ch.interlis.iox_j.ObjectEvent(iomObj); // return object
                }else if(event.isEndElement()) { // end basket
                	state=AFTER_ENDBASKET;
		        	return new ch.interlis.iox_j.EndBasketEvent(); // return end basket
                }
			}
			
			// after object
			if(state==AFTER_OBJECT) {
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
				if(event.isStartElement()){
					iomObj=readObject(event, iomObj); // read object
					setOperation(event.asStartElement(), iomObj);
					setConsistency(event.asStartElement(), iomObj);
                	return new ch.interlis.iox_j.ObjectEvent(iomObj); // return object
		        }else if(event.isEndElement()){
					state=AFTER_ENDBASKET; // close basket
		        	return new ch.interlis.iox_j.EndBasketEvent();
		        }
			}
			
			// after end basket
			if(state==AFTER_ENDBASKET) {
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
		    	if(event.isStartElement()){
		    		state=AFTER_STARTBASKET;
		        	return createBasket(event); // create new basket
		        }else if(event.isEndElement()) {
		        	if(event.asEndElement().getName().equals(QNAME_XML_DATASECTION)){ // end data section
		        		event=reader.nextEvent();
		        		event=skipSpacesAndGetNextEvent(event);
		        	}
		        	if(event.asEndElement().getName().equals(QNAME_ILI_TRANSFER)) { // end transfer
						event=reader.nextEvent();
						event=skipSpacesAndGetNextEvent(event);
						if(event.isEndDocument()) { // end document
							state=AFTER_ENDTRANSFER;
							return new ch.interlis.iox_j.EndTransferEvent(); // return end transfer
						}else {
							throw new IoxSyntaxException(event2msgtext(event));
						}
					}else {
						throw new IoxSyntaxException(event2msgtext(event));
					}
		        }
			}
			// after end transfer
			if(state==AFTER_ENDTRANSFER) {
				throw new IoxSyntaxException(event2msgtext(event));
			}
		}catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
		return null;
	}
	
	private IomObject readObject(XMLEvent event, IomObject iomObj) throws IoxException {
		try {
			if(event.isStartElement()) {
				// DeleteObject
				if(event.asStartElement().getName().equals(QNAME_ILI_DELETE)) {
					return deleteObject(event);
				}
				// IomObject
		        StartElement element = (StartElement) event;
		    	String topicName=currentTopic.getName();
		    	String className=element.getName().getLocalPart();
		    	QName extendedQName=new QName(element.getName().getNamespaceURI(), topicName+"."+className);
		    	viewable=getIliClass(extendedQName);
		    	if(viewable==null){
		    		viewable=getIliClass(element.getName());
		    	}
		        if(viewable==null){
		        	throw new IoxException("class or association "+element.getName().getLocalPart()+" not found");
		        }
		        Attribute oid = element.getAttributeByName(QNAME_ILI_TID);
		        if(element.getAttributeByName(QNAME_ILI_BID) != null){
		        	throw new IoxSyntaxException(event2msgtext(event));
		        }
		    	try {
		    		// create IomObject
		    		if(oid!=null){
		    			iomObj=createIomObject(viewable.getScopedName(), oid.getValue());
		    		}else{
		    			iomObj=createIomObject(viewable.getScopedName(), null);
		    		}
		    	}catch(IoxSyntaxException ioxEx){
		    		throw ioxEx;
		    	}
		    	event=reader.nextEvent(); // after create iomObj
                event=skipSpacesAndGetNextEvent(event);
                // attributes
		    	while(reader.hasNext() && !event.isEndElement()){
		    		iomObj=readAttribute(event, iomObj); // read object attribute
	        		event=reader.nextEvent();
	        		event=skipSpacesAndGetNextEvent(event);
	        	}
			}
			if(event.isEndElement()){
				return iomObj;
			}else if(event.isCharacters()){
				throw new IoxSyntaxException(event2msgtext(event));
			}
		}catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
		return iomObj;
	}
	
	private IoxEvent createBasket(javax.xml.stream.events.XMLEvent event) throws XMLStreamException, IoxException {
		event=skipSpacesAndGetNextEvent(event);
		StartElement element = (StartElement) event;
		currentTopic=getIliTopic(element.getName());
		if(currentTopic==null){
			throw new IoxSyntaxException(event2msgtext(event));
		}
		QName gmlId = QNAME_ILI_BID;
		Attribute bid = element.getAttributeByName(gmlId);
		if(bid!=null){
			ch.interlis.iox_j.StartBasketEvent newObj=new ch.interlis.iox_j.StartBasketEvent(currentTopic.getScopedName(), bid.getValue());
			newObj=setState(element, newObj);
			newObj=setIncrementalKind(element, newObj);
			newObj=setDomain(element, newObj);
			newObj=setConsistency(element, newObj);
			return newObj;
		}else{
			throw new IoxSyntaxException(event2msgtext(event));
		}
	}
	
	private IomObject deleteObject(XMLEvent event) throws IoxException {
		IomObject objToDelete=null;
		try {
			if(event.asStartElement().getAttributeByName(QNAME_ILI_TID)!=null){
				objToDelete=createIomObject(QNAME_ILI_DELETE.getLocalPart(), event.asStartElement().getAttributeByName(QNAME_ILI_TID).getValue());
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
				if(event.isEndElement()){
		    		state=AFTER_STARTBASKET;
		    		if(!event.asEndElement().getName().equals(QNAME_ILI_DELETE)){
		    			throw new IoxException("expected rolename and role reference tid");
		    		}
		    	}else{
		    		throw new IoxException("ili:delete references are not yet implemented.");
		    	}
			}else{
				throw new IoxException("ili:delete object needs tid");
			}
		}catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
		return objToDelete;
	}

	private XMLEvent readHeaderSection(XMLEvent startElementHeaderSection, XtfStartTransferEvent xtfEvent) throws IoxException {
		try {
			XMLEvent event=null;
			// header section
            if(startElementHeaderSection.asStartElement().getName().equals(QNAME_XML_HEADERSECTION)){
            	; // skip start header section event
            }else {
            	throw new IoxSyntaxException(event2msgtext(event));
            }
            event=reader.nextEvent();
            event=skipSpacesAndGetNextEvent(event);
			// start models
			if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_MODELS)){
				; // skip start models event
			}else {
				throw new IoxSyntaxException(event2msgtext(event));
			}
			event=readHeaderSectionModels(event);
			if(event.isEndElement()) {
				// end header section
				if(event.asEndElement().getName().equals(QNAME_XML_HEADERSECTION)){
					if(models.size()==0) {
						throw new IoxException("expected at least 1 model.");
					}
					return event;
				}else {
					throw new IoxSyntaxException(event2msgtext(event));
				}
			}
			int senderCount=0;
			int commentCount=0;
			while(event.isStartElement() && (event.asStartElement().getName().equals(QNAME_XML_HEADERSECTION_SENDER) || event.isStartElement() && event.asStartElement().getName().equals(QNAME_XML_HEADERSECTION_COMMENT))){
				if(event.asStartElement().getName().equals(QNAME_XML_HEADERSECTION_SENDER)){
					if(senderCount==0) {
						// start sender
						event=reader.nextEvent();
						event=skipCommentary(event);
						StringBuffer value=new StringBuffer();
						event=readSimpleContent(event,value);
						xtfEvent.setSender(value.toString());
						if(value!=null && value.toString().isEmpty()) {
							throw new IoxException("sender defined, but empty.");
						}
						// end sender
						if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_HEADERSECTION_SENDER)) {
							; // skip end header section event
						}else {
							throw new IoxSyntaxException(event2msgtext(event));
						}
						event=reader.nextEvent();
						event=skipSpacesAndGetNextEvent(event);
					}else if(senderCount>1) {
						throw new IoxSyntaxException(event2msgtext(event));
					}
					senderCount+=1;
		        }else if(event.asStartElement().getName().equals(QNAME_XML_HEADERSECTION_COMMENT)){
		        	if(commentCount==0) {
		        		// start comment
		        		event=reader.nextEvent();
		        		event=skipCommentary(event);
		        		StringBuffer value=new StringBuffer();
		        		event=readSimpleContent(event,value);
		        		xtfEvent.setComment(value.toString());
		        		if(value!=null && value.toString().isEmpty()) {
							throw new IoxException("comments defined, but empty.");
						}
		        		// end comment
		        		if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_HEADERSECTION_COMMENT)) {
		        			; // skip end header section event
		        		}else {
		        			throw new IoxSyntaxException(event2msgtext(event));
		        		}
		        		event=reader.nextEvent();
		        		event=skipSpacesAndGetNextEvent(event);
		        	}else if(commentCount>1) {
		        		throw new IoxSyntaxException(event2msgtext(event));
					}
					commentCount+=1;
		        }else {
		        	throw new IoxSyntaxException(event2msgtext(event));
		        }
			}
			if(event.isEndElement()) {
				// end header section
				if(event.asEndElement().getName().equals(QNAME_XML_HEADERSECTION)){
					return event;
				}else {
					throw new IoxSyntaxException(event2msgtext(event));
				}
			}else {
				throw new IoxSyntaxException(event2msgtext(event));
			}
		}catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
	}

	private XMLEvent readHeaderSectionModels(XMLEvent startElementModels) throws IoxException, XMLStreamException {
		XMLEvent event=null;
		// start models
		if(startElementModels.isStartElement() && startElementModels.asStartElement().getName().equals(QNAME_XML_MODELS)){
			; // skip start models event
		}else {
			throw new IoxSyntaxException(event2msgtext(event));
		}
		event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
		if(event.isEndElement()){
			throw new IoxSyntaxException(event2msgtext(event));
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
			throw new IoxSyntaxException(event2msgtext(event));
		}
		return event;
	}
	
	private XMLEvent readModel(XMLEvent startElementModel) throws IoxException, XMLStreamException {
		XMLEvent event=null;
		if(startElementModel.isStartElement() && startElementModel.asStartElement().getName().equals(QNAME_XML_MODEL)) {
			; // skip start model event
		}else {
 			throw new IoxSyntaxException(event2msgtext(event)); 
		}
		event=reader.nextEvent();
		
		if(event.isCharacters()) {
	    	// add model to models
	        models.add(event.asCharacters().getData());
	        event=reader.nextEvent(); // end characters
	        event=skipSpacesAndGetNextEvent(event);
		}
		
        // end element Model
 		if(event.isEndElement() && event.asEndElement().getName().equals(QNAME_XML_MODEL)) {
 			; // skip end model event
 		}else {
 			throw new IoxSyntaxException(event2msgtext(event)); 
 		}
 		event=reader.nextEvent();
 		event=skipSpacesAndGetNextEvent(event);
 		return event;
	}
	
	private XMLEvent skipCommentary(XMLEvent event) throws IoxSyntaxException, XMLStreamException {
		 while(event.getEventType()==XMLEvent.COMMENT){
			 event=reader.nextEvent();
	     }
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
	
	private IomObject setOperation(StartElement element, IomObject iomObj) throws IoxException {
		Attribute operation = element.getAttributeByName(QNAME_ILI_OPERATION);
        if(operation!=null){
        	String attrValue=operation.getValue();
        	if(attrValue.equals("INSERT")){
        		iomObj.setobjectoperation(IomConstants.IOM_OP_INSERT); // op(0=insert)
        	}else if(attrValue.equals("UPDATE")){
        		iomObj.setobjectoperation(IomConstants.IOM_OP_UPDATE); // op(1=update)
        	}else if(attrValue.equals("DELETE")){
        		iomObj.setobjectoperation(IomConstants.IOM_OP_DELETE); // op(2=delete)
        	}else{
				throw new IoxSyntaxException("unexpected operation <"+attrValue+">");
        	}
        }
        return iomObj;
	}

	private IomObject setConsistency(StartElement element, IomObject iomObj) throws IoxException {
        Attribute consistency = element.getAttributeByName(QNAME_ILI_CONSISTENCY); // not to define in role
        if(consistency!=null){
        	String attrValue=consistency.getValue();
        	if(attrValue.equals("COMPLETE")){
        		iomObj.setobjectoperation(IomConstants.IOM_COMPLETE); // 0=COMPLETE
        	}else if(attrValue.equals("INCOMPLETE")){
        		iomObj.setobjectoperation(IomConstants.IOM_INCOMPLETE); // 1=INCOMPLETE
        	}else{
				throw new IoxSyntaxException("unexpected consistency <"+attrValue+">");
        	}
        }
        return iomObj;
	}
	
	private ch.interlis.iox_j.StartBasketEvent setState(StartElement element, ch.interlis.iox_j.StartBasketEvent startBasketEvent) throws IoxException {
		Iterator codingObjIter=element.getAttributes();
		while(codingObjIter.hasNext()){
			Attribute codingObj=(Attribute) codingObjIter.next();
			if(codingObj.getName().equals(QNAME_ILI_STARTSTATE)){
				startBasketEvent.setStartstate(codingObj.getValue());
			}else if(codingObj.getName().equals(QNAME_ILI_ENDSTATE)){
				startBasketEvent.setEndstate(codingObj.getValue());
			}
		}
		return startBasketEvent;
	}
	
	private ch.interlis.iox_j.StartBasketEvent setIncrementalKind(StartElement element, ch.interlis.iox_j.StartBasketEvent startBasketEvent) throws IoxException {
		Iterator codingObjIter=element.getAttributes();
		while(codingObjIter.hasNext()){
			Attribute codingObj=(Attribute) codingObjIter.next();
			if(codingObj.getName().equals(QNAME_ILI_KIND)){
				if(codingObj.getValue().equals("FULL")){
					startBasketEvent.setKind(IomConstants.IOM_FULL);
				}else if(codingObj.getValue().equals("UPDATE")){
					startBasketEvent.setKind(IomConstants.IOM_UPDATE);
				}else if(codingObj.getValue().equals("INITIAL")){
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
			if(codingObj.getName().equals(QNAME_ILI_DOMAIN)){
				Map<String,String> domains=parseDomains(codingObj.getValue());
				for(String genericDomain:domains.keySet()) {
                    startBasketEvent.addDomain(genericDomain, domains.get(genericDomain));
				}
			}
		}
		return startBasketEvent;
	}
    public static Map<String,String> parseDomains(String domainValue) {
        HashMap<String,String> ret=new HashMap<String,String>();
        if(domainValue==null || domainValue.trim().length()==0) {
            return ret;
        }
        String genericAndConcreteDomains[]=domainValue.split(" ");
        for(String singleDomain : genericAndConcreteDomains){
            String[] domains=singleDomain.split("=");
            ret.put(domains[0], domains[1]);
        }
        return ret;
    }
	
	private ch.interlis.iox_j.StartBasketEvent setConsistency(StartElement element, ch.interlis.iox_j.StartBasketEvent startBasketEvent) throws IoxException {
		Iterator codingObjIter=element.getAttributes();
		while(codingObjIter.hasNext()){
			Attribute codingObj=(Attribute) codingObjIter.next();
			if(codingObj.getName().equals(QNAME_ILI_CONSISTENCY)){
				if(codingObj.getValue().equals("COMPLETE")){
					startBasketEvent.setConsistency(IomConstants.IOM_COMPLETE);
				}else if(codingObj.getValue().equals("INCOMPLETE")){
					startBasketEvent.setConsistency(IomConstants.IOM_INCOMPLETE);
				}
			}
		}
		return startBasketEvent;
	}

	private static XMLEvent collectXMLElement(XMLEventReader reader, XMLEvent event, java.io.StringWriter strw) throws XMLStreamException {
        XMLOutputFactory xmloutputf = XMLOutputFactory.newInstance();
        xmloutputf.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES,true);
        XMLEventFactory xmlef = XMLEventFactory.newInstance();
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
        return event;
    }
	
	private XMLEvent skipSpacesAndGetNextEvent(XMLEvent event) throws XMLStreamException, IoxSyntaxException {
		while(event.isCharacters() || event.getEventType()==XMLEvent.COMMENT){
        	if(event.isCharacters() && event.getEventType()!=XMLEvent.COMMENT) {
	            Characters characters = (Characters) event;
	            if(!characters.isWhiteSpace()){
					throw new IoxSyntaxException(event2msgtext(event));
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
	private String event2msgtext(XMLEvent event){
		String output="";
		if(event instanceof StartElement){
			 output="unexpected start element "+event.asStartElement().getName().getLocalPart();
		}else if(event instanceof EndElement){
			output="unexpected end element "+event.asEndElement().getName().getLocalPart();
		}else if(event instanceof Characters){
			output="unexpected characters "+((Characters) event).getData();
		}
		return output;
	}
	
	/** Iterate through ili file
	 */
    private void setupNameMapping(){
    	iliTopics=new HashMap<QName, Topic>();
    	iliClasses=new HashMap<QName, Viewable>();
    	iliProperties=new HashMap<Viewable, HashMap<QName, Element>>();
    	if(td==null){
    		return;
    	}
		Iterator tdIterator = td.iterator();
		// iliTd
		while(tdIterator.hasNext()){
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
				String localTopicPart=topic.getName();
				String modelNameSpace=model.getXmlns();
				if(modelNameSpace==null){
					modelNameSpace=NAMESPACE_ILIXMLBASE+model.getName();
				}
				QName topicQName=new QName(modelNameSpace, localTopicPart);
				iliTopics.put(topicQName, topic);
				// iliClass
				Iterator classIter=topic.iterator();
		    	while(classIter.hasNext()){
		    		Object classObj=classIter.next();
		    		if(!(classObj instanceof Viewable)){
    					continue;
    				}
		    		// iliView
		    		if(classObj instanceof View){
		    			if(topic.isViewTopic()){
							View view=(View) classObj;
							if(!view.isTransient()){
							}else{
								continue;
							}
		    			}else{
		    				continue;
		    			}
					}
		    		Viewable viewable = (Viewable) classObj;
		    		String localClassPart=viewable.getName();
    				String nameSpace=model.getXmlns();
    				if(nameSpace==null){
    					nameSpace=NAMESPACE_ILIXMLBASE+model.getName();
    				}
    				QName classQName=new QName(nameSpace, localClassPart);
    				HashMap<QName, Element> transferElements=null;
    				if(iliTopics.containsKey(classQName) || iliClasses.containsKey(classQName)){
    					// conflict
    					localClassPart=topic.getName()+"."+viewable.getName();
    					QName extendedClassQName=new QName(nameSpace, localClassPart);
    					iliClasses.put(extendedClassQName, viewable);
    				}else{
	    				iliClasses.put(classQName, viewable);
    				}
    				// iliProperties
					Iterator<ViewableTransferElement> elementIter=viewable.getAttributesAndRoles2();
					transferElements=new HashMap<QName, Element>();
					Element element=null;
					while (elementIter.hasNext()){
						ViewableTransferElement obj = (ViewableTransferElement) elementIter.next();
						if(obj.obj instanceof Element){
							element=(Element) obj.obj;
							String elementName=element.getName();
		    				QName eleQName=new QName(nameSpace, elementName);
			    			transferElements.put(eleQName, element);
						}
					}
					iliProperties.put(viewable, transferElements);
		    	}
			}
		}
    }
    
    /** Get Topic of ili file
     * @param qName
     * @return Topic
     */
    private Topic getIliTopic(QName qName){
    	if(iliTopics==null){
    		setupNameMapping();
    	}
    	return iliTopics.get(qName);
    }
    
    /** Get Viewable of ili file
     * @param qName
     * @return Viewable
     */
    private Viewable getIliClass(QName qName){
    	if(iliClasses==null){
    		setupNameMapping();
    	}
    	return iliClasses.get(qName);
    }
    
    /** Get Element of ili file
     * @param aClass
     * @param qName
     * @return Element
     * @throws IoxException
     */
    private Element getIliProperty(Viewable aClass, QName qName) throws IoxException{
    	if(iliProperties==null){
    		setupNameMapping();
    	}
    	HashMap<QName, Element> iliPropIter=iliProperties.get(aClass);
    	return iliPropIter.get(qName);
    }
        
    /** Read Events inside object
     * @param event
     * @param iomObj
     * @return IomObject
     * @throws IoxException
     * @throws XMLStreamException
     */
    private IomObject readAttribute(XMLEvent event, IomObject iomObj) throws XMLStreamException, IoxException {
	     try {   
    		String attrName=null;
            if(event.isStartElement()){
            	// object contains attribute
            	StartElement element = (StartElement) event;
            	QName qName=element.getName();
            	Element prop=getIliProperty(viewable, qName);
            	if(prop==null){
            		throw new IoxSyntaxException("unexpected element: "+qName.getLocalPart());
            	}
            	// attribute
        		attrName=prop.getName();
		    	if(attrName!=null){
		    		if(prop instanceof RoleDef){
        				RoleDef role=(RoleDef) prop;
        				if(role.getContainer()!=null){
        					Container elementContainer=role.getContainer();
        					if(elementContainer instanceof AssociationDef){
        						AssociationDef association=(AssociationDef) elementContainer;
        						if(!(association instanceof Viewable)) {
            						viewable=(Viewable) association;
        						}
        						iomObj=readReference(viewable,iomObj, element, role,association);
        						event=reader.nextEvent(); // after role
        						event=skipSpacesAndGetNextEvent(event);
        						attrName=null;
        					}
            			}
        			}else {
        				event=reader.nextEvent(); // after start attribute
        				event=skipCommentary(event);
        			}
		    	}else {
		    		throw new IoxSyntaxException(event2msgtext(event));
		    	}
		    	if(!reader.peek().isEndElement()) {
	    			event=skipSpacesAndGetNextEvent(event);
		    	}
                if(event.isCharacters() && prop instanceof AttributeDef){ // primitive attribute
            		Characters character = (Characters) event;
                    String characterValue=character.getData();
                    if(characterValue.contains("OTHERS")){
                    	throw new IoxException("OTHERS not yet implemented.");
                    }
            		iomObj.setattrvalue(attrName, characterValue);
            		event=reader.nextEvent(); // after characters
            		if(event.isEndElement()){ // end attribute
            			attrName=null;
            		}else{
            			throw new IoxSyntaxException(event2msgtext(event));
            		}
                }
                if(event.isStartElement()) { // object
	                if(event.asStartElement().getName().equals(QNAME_GEOM_COORD)){
	            		event=reader.nextEvent(); // <coords>
	                    event=skipSpacesAndGetNextEvent(event);
	                    if(!event.isStartElement()){
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                    IomObject returnedSegment=readSegment(event, SEGMENTTYPE_COORD);
	                    if(returnedSegment.getattrcount()==0){
	                    	throw new IoxException("expected coord. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
						iomObj.addattrobj(attrName, returnedSegment);
	                    event=reader.nextEvent(); // <coord>
	                    event=skipSpacesAndGetNextEvent(event);
	                    if(event.isEndElement()){
	                        attrName=null;
	                    }else{
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                }else if(event.asStartElement().getName().equals(QNAME_GEOM_MULTICOORD)){
	                    if(!event.isStartElement()){
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	            		event=reader.nextEvent(); // <multicoord>
	            		event=skipSpacesAndGetNextEvent(event);
	            		if(!event.isStartElement()){
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	            		IomObject returnedObj=readSequence(event);
	                    if(returnedObj.getattrcount()==0){
	                    	throw new IoxException("expected multicoord. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
	                    iomObj.addattrobj(attrName, returnedObj);
	                    event=reader.nextEvent(); // <coord>
	                    event=skipSpacesAndGetNextEvent(event);
	                    if(event.isEndElement()){
	                        attrName=null;
	                    }else{
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                }else if(event.asStartElement().getName().equals(QNAME_GEOM_POLYLINE)){
	                    IomObject polyline = readPolyline(event);
	                    if(polyline.getattrcount()==0){
	                    	throw new IoxException("expected polyline. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
	                    iomObj.addattrobj(attrName, polyline);
	                    event=reader.nextEvent(); // <polyline>
	                    event=skipSpacesAndGetNextEvent(event);
	                    if(event.isEndElement()){ // </attribute>
	                        attrName=null;
	                    }else{
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                }else if(event.asStartElement().getName().equals(QNAME_GEOM_MULTIPOLYLINE)){
	                    IomObject multiPolyline = readMultiPolyline(event);
	                    if(multiPolyline.getattrcount()==0){
	                    	throw new IoxException("expected multipolyline. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
	                    iomObj.addattrobj(attrName, multiPolyline);
	                    event=reader.nextEvent(); // <multipolyline>
	                    event=skipSpacesAndGetNextEvent(event);
	                    if(event.isEndElement()){ // </attribute>
	                        attrName=null;
	                    }else{
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                }else if((event.asStartElement().getName().equals(QNAME_GEOM_LINESTRING))
	                		|| (event.asStartElement().getName().equals(QNAME_GEOM_ORIENTABLECURVE))
	                		|| (event.asStartElement().getName().equals(QNAME_GEOM_COMPOSITECURVE))){
	                	throw new IoxException("unsupported geometry "+event.asStartElement().getName().getLocalPart());
	                }else if(event.asStartElement().getName().equals(QNAME_GEOM_SURFACE)){
	                    // SURFACE (surface/area)
	                	IomObject multiSurface=createIomObject("MULTISURFACE", null);
	                	IomObject surface=readSurface(event);
	                	if(surface.getattrcount()==0){
	                    	throw new IoxException("expected surface. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
	                	multiSurface.addattrobj("surface", surface);
	                	if(!event.isStartElement()){
	                		throw new IoxSyntaxException(event2msgtext(event));
	                	}
	                	iomObj.addattrobj(attrName, multiSurface);
	                	event=reader.nextEvent(); // <surface>
	                	event=skipSpacesAndGetNextEvent(event);
	                	if(event.isEndElement()){
	                        attrName=null;
	                    }else{
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                }else if((event.asStartElement().getName().equals(QNAME_GEOM_MULTISURFACE) || event.asStartElement().getName().equals(QNAME_GEOM_MULTIAREA))){
	                    // MULTISURFACE (surfaces and/or areas)
	                	IomObject multisurface=readMultiSurface(event);
	                	if(multisurface.getattrcount()==0){
	                    	throw new IoxException("expected multisurface. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
	                    if(!event.isStartElement()){
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                    iomObj.addattrobj(attrName, multisurface);
	                    event=reader.nextEvent(); // <surface>
	                    event=skipSpacesAndGetNextEvent(event);
	                    if(event.isEndElement()){ // </attribute>
	                        attrName=null;
	                    }else{
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                }else{
	            		QName subQName=((StartElement) event).getName();
	            		viewable=getIliClass(subQName);
	            		if(viewable==null){
	            			// blackBox
	            			java.io.StringWriter strw=new java.io.StringWriter();
	                    	event=collectXMLElement(reader,event, strw); // <blackbox></blackbox>
	            			iomObj.setattrvalue(attrName, strw.toString());
	            			if(!event.isEndElement()){
		                    	throw new IoxSyntaxException(event2msgtext(event));
		                    }
	            		}else {
	            			// structure
		                	IomObject structObj=createIomObject(viewable.getName(), null);
		                    iomObj.addattrobj(attrName, readObject(event, structObj));
		                    if(!event.isStartElement()){
		                    	throw new IoxSyntaxException(event2msgtext(event));
		                    }
	            		}
	            		event=reader.nextEvent();
	                    event=skipSpacesAndGetNextEvent(event);
        			}
            	}else if(event.isEndElement()) {
		            if(attrName!=null){
		            	attrName=null;
		            }
		            if(attrName==null){
		                return iomObj;
		            }
            	}
            }
	    }catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
        return iomObj;
    }

	private IomObject readReference(Viewable aclass,IomObject iomObj, StartElement element, RoleDef role, AssociationDef association) throws IoxException, XMLStreamException{
		String refOid=element.getAttributeByName(QNAME_ILI_REF).getValue();
		if(refOid.length()<=1){
			throw new IoxException("unexpected reference value <"+refOid+">");
		}
		Attribute attrRefBid=element.getAttributeByName(QNAME_ILI_BID);
		String refBid=null;
		if(attrRefBid!=null) {
			refBid=attrRefBid.getValue();
			if(refBid.length()<=1){
				throw new IoxException("unexpected reference value <"+refBid+">");
			}
		}
		Long orderPos=null;
        Attribute orderPosAttr = element.getAttributeByName(QNAME_ILI_ORDERPOS);
        if(orderPosAttr!=null){
        	String orderPosVal=orderPosAttr.getValue();
        	if(orderPosVal!=null){
        		try {
					orderPos=Long.parseLong(orderPosVal);
				} catch (NumberFormatException e) {
					throw new IoxSyntaxException("unexpected orderPos <"+orderPosVal+">",e);
				}
        	}
        }
		if(aclass.isExtending(association)){
			// role of a standalone association
			iomObj.addattrobj(role.getName(),"REF").setobjectrefoid(refOid);
			if(refBid!=null) {
				iomObj.addattrobj(role.getName(),"REF").setobjectrefbid(refBid); // set reference
			}
			IomObject anObject=iomObj.getattrobj(role.getName(), 0);
			if(orderPos!=null){
				anObject.setobjectreforderpos(orderPos);
			}
			if(refBid!=null) {
				anObject.setobjectrefbid(refBid);
			}
		}else{
			// embedded role
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
	            iomObj.addattrobj(role.getName(), readObject(event, iomObj));
	        } else {
	            iomObj.addattrobj(role.getName(),"REF").setobjectrefoid(refOid);
	            IomObject aObject=iomObj.getattrobj(role.getName(), 0);
	            if(orderPos!=null){
	                aObject.setobjectreforderpos(orderPos);
	            }
	            if(refBid!=null) {
	                aObject.setobjectrefbid(refBid);
	            }	            
	        }
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
    	IomObject multiSurface=createIomObject("MULTISURFACE", null);
    	event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
    	while(event.isStartElement() && (event.asStartElement().getName().equals(QNAME_GEOM_SURFACE) || event.asStartElement().getName().equals(QNAME_GEOM_AREA))){
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
    	IomObject surface=createIomObject("SURFACE", null);
    	event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
    	while(event.isStartElement() && (event.asStartElement().getName().equals(QNAME_GEOM_INTERIOR) || event.asStartElement().getName().equals(QNAME_GEOM_EXTERIOR))){
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
    	IomObject boundary=createIomObject("BOUNDARY", null);
    	event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
    	while(event.isStartElement() && event.asStartElement().getName().equals(QNAME_GEOM_POLYLINE)){
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
    	IomObject multiPolyline=createIomObject("MULTIPOLYLINE", null);
    	event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
    	while(event.isStartElement() && event.asStartElement().getName().equals(QNAME_GEOM_POLYLINE)){
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
    	IomObject polyline=createIomObject("POLYLINE", null);
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
		IomObject sequence=createIomObject("SEGMENTS", null);
		if(event.isStartElement()){
			while(event.isStartElement()){
				String segmentType=null;
				if(event.asStartElement().getName().equals(QNAME_GEOM_COORD)){
					segmentType=SEGMENTTYPE_COORD;
				}else if(event.asStartElement().getName().equals(QNAME_GEOM_ARC)){
					segmentType=SEGMENTTYPE_ARC;
				}
				event=reader.nextEvent();
				event=skipSpacesAndGetNextEvent(event);
				sequence.addattrobj("segment", readSegment(event, segmentType));
				event=reader.nextEvent();
				if(!event.isCharacters() && event.getEventType()!=XMLEvent.COMMENT){
					throw new IoxSyntaxException(event2msgtext(event));
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
		SegmentType upperCaseTypeName=null;
		while(!event.isEndElement()){
	        if(event.isStartElement()){
	        	String segmentTypeName=event.asStartElement().getName().getLocalPart();
				event=reader.nextEvent();
				if(event.isEndElement()){
					throw new IoxException("expected coord");
				}
				if(!event.isCharacters()){
					throw new IoxSyntaxException(event2msgtext(event));
				}
				upperCaseTypeName = SegmentType.valueOf(segmentTypeName.toUpperCase());
				switch(upperCaseTypeName){
					case C1: segment.setattrvalue("C1", event.asCharacters().getData());
						break;
					case C2: segment.setattrvalue("C2", event.asCharacters().getData());
						break;
					case C3: segment.setattrvalue("C3", event.asCharacters().getData());
						break;
					case A1: segment.setattrvalue("A1", event.asCharacters().getData());
						break;
					case A2: segment.setattrvalue("A2", event.asCharacters().getData());
						break;
					case MULTICOORD: segment.setattrvalue("multicoord", event.asCharacters().getData());
						break;
					case R: segment.setattrvalue("R", event.asCharacters().getData());
						break;
					default: throw new IoxSyntaxException(event2msgtext(event));
				}
	        }
	        event=reader.nextEvent();
	        if(event.isStartElement()){
				throw new IoxSyntaxException(event2msgtext(event));
	        }
			event=reader.nextEvent();
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
}