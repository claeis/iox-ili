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
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.generator.Iligml20Generator;
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
import ch.interlis.iom_j.iligml.LinkPool;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iox.IoxReader;
import ch.interlis.iox_j.IoxSyntaxException;

public class Xtf24Reader implements IoxReader {
	private TransferDescription td;
	private javax.xml.stream.XMLEventReader reader=null;
	private IoxFactoryCollection factory=new  ch.interlis.iox_j.DefaultIoxFactoryCollection();
	private LinkPool linkPool=new LinkPool();
	private Iterator<IomObject> linkIterator;
	private java.io.InputStream inputFile=null;
	
	private Topic currentTopic=null;
	private IomObject iomObj=null;
	private Integer transferkind=0;
	
	// ili content
	private HashMap<QName, Topic> iliTopics=null;
    private HashMap<QName, Viewable> iliClasses=null;
    private HashMap<Viewable, HashMap<QName, Element>> iliProperties=null;
    
    // segmentType
    private enum SegmentType {
        C1,C2,C3,A1,A2,R,MULTICOORD
    }
    
    // state
    private int state = START;
    private static final int START=0;
    private static final int START_DOCUMENT=1;
    private static final int INSIDE_TRANSFER=2;
    private static final int INSIDE_HEADERSECTION=3;
    private static final int INSIDE_MODELS=4;
    private static final int INSIDE_MODEL=5;
    private static final int END_MODEL=6;
    private static final int INSIDE_SENDER=7;
    private static final int END_SENDER=8;
    private static final int INSIDE_COMMENTS=9;
    private static final int END_COMMENTS=10;
    private static final int END_MODELS=11;
    private static final int END_HEADERSECTION=12;
    private static final int INSIDE_DATASECTION=13;
    private static final int INSIDE_BASKET=14;
    private static final int END_DELETE=15;
    private static final int PRE_ENDBASKET=16;
    private static final int END_BASKET=17;
    private static final int END_DATASECTION=18;
    private static final int END_TRANSFER=19;
    // enum
    private static final String SEGMENTTYPE_COORD="COORD";
    private static final String SEGMENTTYPE_ARC="ARC";
    // namespace
    private static final String NAMESPACE_ILIXMLBASE="http://www.interlis.ch/xtf/2.4/";
    private static final String NAMESPACE_ILIXMLBASE_INTERLIS=NAMESPACE_ILIXMLBASE+"INTERLIS";
    private static final String NAMESPACE_METAATTR = "ili2.ilixtf24.namespaceName";
    private static final String NAMESPACE_GEOM="http://www.interlis.ch/geometry/1.0";
    private static final String NAMESPACE_XMLSCHEMA="http://www.w3.org/2001/XMLSchema-instance";
	// qnames xml
    private static final QName QNAME_XML_HEADERSECTION=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"headersection");
    private static final QName QNAME_XML_SENDER=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"sender");
    private static final QName QNAME_XML_COMMENTS=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"comments");
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
    private static final QName QNAME_GEOM_BOUNDARY = new QName(NAMESPACE_GEOM, "boundary");
    private static final QName QNAME_GEOM_ORIENTABLECURVE = new QName(NAMESPACE_GEOM, "orientablecurve");
	private static final QName QNAME_GEOM_COMPOSITECURVE = new QName(NAMESPACE_GEOM, "compositecurve");
	private static final QName QNAME_GEOM_SURFACE = new QName(NAMESPACE_GEOM, "surface");
	private static final QName QNAME_GEOM_MULTISURFACE = new QName(NAMESPACE_GEOM, "multisurface");
	private static final QName QNAME_GEOM_AREA = new QName(NAMESPACE_GEOM, "area");
	private static final QName QNAME_GEOM_MULTIAREA = new QName(NAMESPACE_GEOM, "multiarea");
	private static final QName QNAME_GEOM_EXTERIOR = new QName(NAMESPACE_GEOM, "exterior");
	private static final QName QNAME_GEOM_INTERIOR = new QName(NAMESPACE_GEOM, "interior");
    
    // header information
    private ArrayList<String> models=new ArrayList<String>();
    private String sender=null;
    private String comment=null;
    
    /** Creates a new reader.
     * @param in
     * @throws IoxException
     */
	public Xtf24Reader(java.io.InputStream in) throws IoxException{
		init(in);
	}
	
	/** Creates a new reader.
	 * @param in Input reader to read from
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
	
	/** Sets the model file.
	 * @param td
	 */
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
        if(state==PRE_ENDBASKET && linkIterator!=null){
			if(linkIterator.hasNext()){
				IomObject assocObj=linkIterator.next();
				return new ch.interlis.iox_j.ObjectEvent(assocObj);
			}
        	linkIterator=null;
			state=END_BASKET;
			return new ch.interlis.iox_j.EndBasketEvent();
        }
		while(reader.hasNext()){
			javax.xml.stream.events.XMLEvent event=null;
			try{
				event=reader.nextEvent();
				EhiLogger.debug(event.toString());
			}catch(javax.xml.stream.XMLStreamException ex){
				throw new IoxException(ex);
			}
			// start
			if(state==START){ // first XML event has to be start document
				if(event.isStartDocument()){
					state=START_DOCUMENT;
				}else{
					throw new IoxSyntaxException(event2msgtext(event));
				}
			// start document
			}else if(state==START_DOCUMENT){
                if(event.isStartElement()){
                    StartElement element = (StartElement) event;
                    if(element.getName().equals(QNAME_ILI_TRANSFER)){
                        state=INSIDE_TRANSFER;
                        return new ch.interlis.iox_j.StartTransferEvent();
                    }else{
                    	throw new IoxSyntaxException(event2msgtext(event));
                    }
                }else if(event.isCharacters()){
            		try {
            			event=skipSpaces(event);
					} catch (XMLStreamException ex) {
						throw new IoxException(ex);
					}
            		continue;
                }else if(event.isEndDocument() || event.isEndElement()){
                	// != start transfer
                	throw new IoxSyntaxException(event2msgtext(event));
                }
            // start transfer
            }else if(state==INSIDE_TRANSFER){
                if(event.isStartElement()){
                	if(event.asStartElement().getName().equals(QNAME_XML_HEADERSECTION)){
                		state=INSIDE_HEADERSECTION;
                		continue;
                	}else{
                		// expected header section
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isEndElement()){
                	// expected 1 modelname
                	throw new IoxSyntaxException(event2msgtext(event));
                }
                if(event.isCharacters()){
            		try {
						event=skipSpaces(event);
					} catch (XMLStreamException ex) {
						throw new IoxException(ex);
					}
            		continue;
                }
            // start header section
			}else if(state==INSIDE_HEADERSECTION){
                if(event.isStartElement()){
                	if(event.asStartElement().getName().equals(QNAME_XML_MODELS)){
                		state=INSIDE_MODELS;
                		continue;
                	}else{
                		// expected models
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isEndElement()){
                	// expected 1 model name
                	throw new IoxSyntaxException(event2msgtext(event));
                }else if(event.isCharacters()){
            		try {
						event=skipSpaces(event);
					} catch (XMLStreamException ex) {
						throw new IoxException(ex);
					}
            		continue;
                }
            // start models
            }else if(state==INSIDE_MODELS){
                if(event.isStartElement()){
                	if(event.asStartElement().getName().equals(QNAME_XML_MODEL)){
                		state=INSIDE_MODEL;
                		continue;
                	}else{
                		// expected model
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isEndElement()){
                	// expected 1 model name
                	throw new IoxSyntaxException(event2msgtext(event));
                }else if(event.isCharacters()){
            		try {
						event=skipSpaces(event);
					} catch (XMLStreamException ex){
						throw new IoxException(ex);
					}
            		continue;
                }
            // start model
            }else if(state==INSIDE_MODEL){
                if(event.isStartElement()){
                	// != model name
                	throw new IoxSyntaxException(event2msgtext(event));
                }else if(event.isEndElement()){
                	if(event.asEndElement().getName().equals(QNAME_XML_MODEL)){
                		state=END_MODEL;
                		continue;
                	}else{
                		// != end model
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isCharacters()){
                    Characters character = (Characters) event;
                    if(character.getData()==null || character.getData().length()==0){
                    	throw new IoxException("expected modelname.");
                    }else{
                    	models.add(character.getData());
                    	continue;
                    }
                }
            // end model
            }else if(state==END_MODEL){
                if(event.isStartElement()){
                	if(event.asStartElement().getName().equals(QNAME_XML_MODEL)){
                		state=INSIDE_MODEL;
                		continue;
                	}else{
                		// != model
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isEndElement()){
                	if(event.asEndElement().getName().equals(QNAME_XML_MODELS)){
                		state=END_MODELS;
                		continue;
                	}else{
                		// != end models
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isCharacters()){
            		try {
						event=skipSpaces(event);
					} catch (XMLStreamException ex) {
						throw new IoxException(ex);
					}
            		continue;
                }
            // end models
            }else if(state==END_MODELS){
            	if(event.isStartElement()){
					if(models.size()==0){
						// no model defined
						throw new IoxException("expected at least 1 model.");
					}
					if(event.asStartElement().getName().equals(QNAME_XML_SENDER)){
						state=INSIDE_SENDER;
						continue;
					}else if(event.asStartElement().getName().equals(QNAME_XML_COMMENTS)){
						state=INSIDE_COMMENTS;
						continue;
					}else{
						throw new IoxSyntaxException(event2msgtext(event));
					}
				}else if(event.isEndElement()){
					if(models.size()==0){
						// no model defined
						throw new IoxException("expected at least 1 model.");
					}
					if(event.asEndElement().getName().equals(QNAME_XML_HEADERSECTION)){
						state=END_HEADERSECTION;
						continue;
					}else{
						// != header section
						throw new IoxSyntaxException(event2msgtext(event));
					}
				}else if(event.isCharacters()){
            		try {
            			event=skipSpaces(event);
            		} catch (XMLStreamException ex) {
            			throw new IoxException(ex);
            		}
            		continue;
            	}
            // start sender
            }else if(state==INSIDE_SENDER){
                if(event.isStartElement()){
                	// != sender name
                	throw new IoxSyntaxException(event2msgtext(event));
                }else if(event.isEndElement()){
                	if(event.asEndElement().getName().equals(QNAME_XML_SENDER)){
                		state=END_SENDER;
                		continue;
                	}else{
                		// != end sender
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isCharacters()){
                    Characters character = (Characters) event;
                    if(character.getData()==null || character.getData().length()==0){
                    	throw new IoxException("expected sendername.");
                    }else{
                    	sender=character.getData();
                    	continue;
                    }
                }
            // start comments
            }else if(state==INSIDE_COMMENTS){
                if(event.isStartElement()){
                	// != comments name
                	throw new IoxSyntaxException(event2msgtext(event));
                }else if(event.isEndElement()){
                	if(event.asEndElement().getName().equals(QNAME_XML_COMMENTS)){
                		state=END_COMMENTS;
                		continue;
                	}else{
                		// != end comments
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isCharacters()){
                    Characters character = (Characters) event;
                    if(character.getData()==null || character.getData().length()==0){
                    	throw new IoxException("expected a comment.");
                    }else{
                    	comment=character.getData();
                    	continue;
                    }
                }
            // end sender
            }else if(state==END_SENDER){
               if(event.isStartElement()){
                	if(event.asStartElement().getName().equals(QNAME_XML_COMMENTS)){
                		if(comment==null){
                			state=INSIDE_COMMENTS;
                			continue;
                		}else{
                			// dublicated comments
                			throw new IoxException("comments already defined.");
                		}
                	}else{
                		// != comments
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else  if(event.isEndElement()){
                	if(sender!=null){
	                	state=END_HEADERSECTION;
	                	continue;
	                }else{
	                	// dublicated sender
	            		throw new IoxException("sender defined, but empty.");
	            	}
                }else if(event.isCharacters()){
            		try {
						event=skipSpaces(event);
					} catch (XMLStreamException ex) {
						throw new IoxException(ex);
					}
            		continue;
                }
            // end comments
            }else if(state==END_COMMENTS){
                if(event.isStartElement()){
                	if(event.asStartElement().getName().equals(QNAME_XML_SENDER)){
                		if(sender==null){
                			state=INSIDE_SENDER;
                			continue;
                		}else{
                			// dublicate sender
                			throw new IoxException("sender already defined.");
                		}
                	}else{
                		// != sender
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isEndElement()){
                	if(comment!=null){
                		state=END_HEADERSECTION;
                		continue;
                	}else{
                		// comment defined, but empty
                		throw new IoxException("comments defined, but empty.");
                	}
                }else if(event.isCharacters()){
            		try {
						event=skipSpaces(event);
					} catch (XMLStreamException ex) {
						throw new IoxException(ex);
					}
            		continue;
                }
            // end header section
            }else if(state==END_HEADERSECTION){
                if(event.isStartElement()){
                	if(event.asStartElement().getName().equals(QNAME_XML_DATASECTION)){
                		state=INSIDE_DATASECTION;
                		if(models.size()==0){
                			// no model defined.
                    		throw new IoxException("expected at least 1 model.");
                    	}
                		continue;
                	}else{
                		// != inside data section
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isEndElement()){
                	// expected data section
                	throw new IoxException("expected data section");
                }else if(event.isCharacters()){
            		try {
						event=skipSpaces(event);
					} catch (XMLStreamException ex) {
						throw new IoxException(ex);
					}
            		continue;
                }
            // start data section
            }else if(state==INSIDE_DATASECTION){
            	// start basket
                if(event.isStartElement()){
                	StartElement element = (StartElement) event;
                	// create new basket
                	currentTopic=getIliTopic(element.getName());
                	if(currentTopic==null){
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                	QName gmlId = QNAME_ILI_BID;
                	Attribute bid = element.getAttributeByName(gmlId);
                	if(bid!=null){
                		state=INSIDE_BASKET;
                		ch.interlis.iox_j.StartBasketEvent newObj=new ch.interlis.iox_j.StartBasketEvent(currentTopic.getScopedName(), bid.getValue());
                		ch.interlis.iox_j.StartBasketEvent bidObj=setBasketCodingContent(element, newObj);
                		return bidObj;
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isEndElement()){
                	if(event.asEndElement().getName().equals(QNAME_XML_DATASECTION)){
                		// end data section
                		state=END_DATASECTION;
                		continue;
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isCharacters()){
            		try {
						event=skipSpaces(event);
					} catch (XMLStreamException ex) {
						throw new IoxException(ex);
					}
            		continue;
                }
            // start object inside basket
            }else if(state==INSIDE_BASKET){
            	if(event.isStartElement()){
            		if(event.asStartElement().getName().equals(QNAME_ILI_DELETE)){
            			IomObject objToDelete=null;
        				// delete Object
            			if(event.asStartElement().getAttributeByName(QNAME_ILI_TID)!=null){
            				objToDelete=createIomObject("delete", event.asStartElement().getAttributeByName(QNAME_ILI_TID).getValue());
            				state=END_DELETE;
            			}else{
            				throw new IoxException("ili:delete object needs tid");
            			}
            			return new ch.interlis.iox_j.ObjectEvent(objToDelete);
            		}
                	Viewable viewable=null;
                    StartElement element = (StartElement) event;
                	String topicName=currentTopic.getName();
                	String className=element.getName().getLocalPart();
                	QName extendedQName=new QName(element.getName().getNamespaceURI(), topicName+"."+className);
                	viewable=getIliClass(extendedQName);
                	if(viewable==null){
                		viewable=getIliClass(element.getName());
                	}
                    if(viewable==null){
                    	throw new IoxSyntaxException(event2msgtext(event));
                    }
                    Attribute oid = element.getAttributeByName(QNAME_ILI_TID);
                    if(oid!=null){
                    	try{
                    		iomObj=createIomObject(viewable.getScopedName(), oid.getValue());
                    	}catch(IoxSyntaxException ioxEx){
                    		throw new IoxSyntaxException(event2msgtext(event)+"\n"+ioxEx);
                    	}
                		try{
							iomObj=readObject(event, viewable, iomObj);
							setAdditionalObjectInfo(element, iomObj);
							event=reader.nextEvent(); // <object>
	                    	return new ch.interlis.iox_j.ObjectEvent(iomObj);
						}catch(IoxSyntaxException ex){								
							throw new IoxSyntaxException(event2msgtext(event)+"\n"+ex);
						}catch(XMLStreamException e){
							throw new IoxException(e);
						}
                    }else{
                    	throw new IoxSyntaxException(event2msgtext(event));
                    }
                }else if(event.isEndElement()){
                	state=PRE_ENDBASKET;
                	ArrayList<IomObject> objList= associationBuilder();
                	if(objList!=null){
	            		linkIterator = objList.iterator();
	            		if(linkIterator!=null && linkIterator.hasNext()){
	            			IomObject assocObj=linkIterator.next();
	            			return new ch.interlis.iox_j.ObjectEvent(assocObj);
	            		}
                	}
                	linkIterator=null;
                	state=END_BASKET;
                    // return end basket
                	return new ch.interlis.iox_j.EndBasketEvent();
                }else if(event.isCharacters()){
                	try {
						event=skipSpaces(event);
					} catch (XMLStreamException ex) {
						throw new IoxException(ex);
					}
            		continue;
                }
            }else if(state==END_DELETE){
            	if(event.isEndElement()){
            		state=INSIDE_BASKET;
            		if(event.asEndElement().getName().equals(QNAME_ILI_DELETE)){
            			continue;
            		}else{
            			throw new IoxException("expected rolename and role reference tid");
            		}
            	}else{
            		throw new IoxException("ili:delete references are not yet implemented.");
            	}
            // pre end basket
            }else if(state==PRE_ENDBASKET){
            	throw new IllegalStateException("state=PRE_ENDBASKET");
            // end basket
            }else if(state==END_BASKET){
            	if(event.isStartElement()){
                	// start basket
                	state=INSIDE_DATASECTION;
                	continue;
                }else if(event.isEndElement()){
                	// end data section
                	state=END_DATASECTION;
                	continue;
                }else if(event.isCharacters()){
                	try {
						event=skipSpaces(event);
						XMLEvent peekEvent=reader.peek();
						if(peekEvent.isStartElement()){
							// start basket
		                	state=INSIDE_DATASECTION;
		                	continue;
						}
					} catch (XMLStreamException ex) {
						throw new IoxException(ex);
					}
            		continue;
                }
            // end data section
            }else if(state==END_DATASECTION){
            	if(event.isStartElement()){
            		throw new IoxSyntaxException(event2msgtext(event));
                }else if(event.isEndElement()){
                	// end transfer
                	state=END_TRANSFER;
                	return new ch.interlis.iox_j.EndTransferEvent();
                }else if(event.isCharacters()){
                	try {
						event=skipSpaces(event);
					} catch (XMLStreamException ex) {
						throw new IoxException(ex);
					}
            		continue;
                }
            }
		}
		return null;
	}

	private IomObject setAdditionalObjectInfo(StartElement element, IomObject iomObj) {
		Attribute operation = element.getAttributeByName(QNAME_ILI_OPERATION);
        if(operation!=null){
        	if(transferkind!=0){
            	Attribute attrValue=(Attribute) operation;
            	if(attrValue.getValue().equals("INSERT")){
            		iomObj.setobjectoperation(IomConstants.IOM_OP_INSERT); // op(0=insert)
            	}else if(attrValue.getValue().equals("UPDATE")){
            		iomObj.setobjectoperation(IomConstants.IOM_OP_UPDATE); // op(1=update)
            	}else if(attrValue.getValue().equals("DELETE")){
            		iomObj.setobjectoperation(IomConstants.IOM_OP_DELETE); // op(2=delete)
            	}
        	}
        }
        Attribute consistency = element.getAttributeByName(QNAME_ILI_CONSISTENCY); // not to define in role
        if(consistency!=null){
        	Attribute attrValue=(Attribute) consistency;
        	if(attrValue.getValue().equals("COMPLETE")){
        		iomObj.setobjectoperation(IomConstants.IOM_COMPLETE); // 0=COMPLETE
        	}else if(attrValue.getValue().equals("INCOMPLETE")){
        		iomObj.setobjectoperation(IomConstants.IOM_INCOMPLETE); // 1=INCOMPLETE
        	}
        }
        Attribute orderPos = element.getAttributeByName(QNAME_ILI_ORDERPOS);
        if(orderPos!=null){
        	Attribute attrValue=(Attribute) orderPos;
        	if(attrValue.getValue()!=null){
        		iomObj.setobjectreforderpos(Long.parseLong(attrValue.getValue()));
        	}
        }
        return iomObj;
	}

	private ch.interlis.iox_j.StartBasketEvent setBasketCodingContent(StartElement element, ch.interlis.iox_j.StartBasketEvent startBasketEvent) throws IoxException {
		Iterator codingObjIter=element.getAttributes();
		String[] genericAndConcreteDomains=null;
		while(codingObjIter.hasNext()){
			Attribute codingObj=(Attribute) codingObjIter.next();
			if(codingObj.getName().equals(QNAME_ILI_STARTSTATE)){
				startBasketEvent.setStartstate(codingObj.getValue());
			}else if(codingObj.getName().equals(QNAME_ILI_ENDSTATE)){
				startBasketEvent.setEndstate(codingObj.getValue());
			}else if(codingObj.getName().equals(QNAME_ILI_KIND)){
				if(codingObj.getValue().equals("FULL")){
					startBasketEvent.setKind(IomConstants.IOM_FULL);
					transferkind=0;
				}else if(codingObj.getValue().equals("UPDATE")){
					startBasketEvent.setKind(IomConstants.IOM_UPDATE);
					transferkind=1;
				}else if(codingObj.getValue().equals("INITIAL")){
					startBasketEvent.setKind(IomConstants.IOM_INITIAL);
					transferkind=2;
				}
			}else if(codingObj.getName().equals(QNAME_ILI_DOMAIN)){
				String domainValue=codingObj.getValue();
				genericAndConcreteDomains=domainValue.split(" "); // genericAndConcreteDomains.getValues(genericDomain=concreteDomain)
				for(String singleDomain : genericAndConcreteDomains){
					String[] domains=singleDomain.split("=");
					startBasketEvent.addDomain(domains[0], domains[1]);
				}
			}else if(codingObj.getName().equals(QNAME_ILI_CONSISTENCY)){
				if(codingObj.getValue().equals("COMPLETE")){
					startBasketEvent.setConsistency(IomConstants.IOM_COMPLETE);
				}else if(codingObj.getValue().equals("INCOMPLETE")){
					startBasketEvent.setConsistency(IomConstants.IOM_INCOMPLETE);
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
	
	/** Read Characters of characterEvent and check if whitespace. Return current event
	 * @param event
	 * @return XMLEvent event
	 * @throws XMLStreamException
	 * @throws IoxSyntaxException
	 */
	private XMLEvent skipSpaces(XMLEvent event) throws XMLStreamException, IoxSyntaxException {
		XMLEvent peekEvent=event;
        while(peekEvent.isCharacters()){
            Characters characters = (Characters) peekEvent;
            if(!characters.isWhiteSpace()){
				throw new IoxSyntaxException(event2msgtext(peekEvent));
            }
            peekEvent=reader.peek();
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
        while(event.isCharacters()){
            Characters characters = (Characters) event;
            if(!characters.isWhiteSpace()){
				throw new IoxSyntaxException(event2msgtext(event));
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
    private void fillIliMaps(){
    	iliTopics=new HashMap<QName, Topic>();
    	iliClasses=new HashMap<QName, Viewable>();
    	iliProperties=new HashMap<Viewable, HashMap<QName, Element>>();
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
				String modelNameSpace=model.getMetaValue(NAMESPACE_METAATTR);
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
    				String nameSpace=model.getMetaValue(NAMESPACE_METAATTR);
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
					Iterator<ViewableTransferElement> elementIter=Iligml20Generator.getAttributesAndRoles2(viewable);
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
    		fillIliMaps();
    	}
    	return iliTopics.get(qName);
    }
    
    /** Get Viewable of ili file
     * @param qName
     * @return Viewable
     */
    private Viewable getIliClass(QName qName){
    	if(iliClasses==null){
    		fillIliMaps();
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
    		fillIliMaps();
    	}
    	HashMap<QName, Element> iliPropIter=iliProperties.get(aClass);
    	return iliPropIter.get(qName);
    }
    
    /** Prepare Association
     * @return ArrayList<IomObject>
     * @throws IoxException
     */
    private ArrayList<IomObject> associationBuilder() throws IoxException {
    	ArrayList<IomObject> assocIomObjects=new ArrayList<IomObject>();
    	Iterator<IomObject> linkIterator=linkPool.getLinkObjects();
    	if(linkIterator==null){
    		return null;
    	}
    	if(linkIterator.hasNext()){
    		IomObject assocLink=linkIterator.next();
    		assocIomObjects.add(assocLink);
    	}
	    return assocIomObjects;
	}
    
    /** Read Events inside object
     * @param event
     * @param aClass
     * @param iomObj
     * @return IomObject
     * @throws IoxException
     * @throws XMLStreamException
     */
    private IomObject readObject(XMLEvent event, Viewable aClass, IomObject iomObj) throws XMLStreamException, IoxException {
        String attrName=null;
        while(reader.hasNext()){
            try{
                event=reader.nextEvent(); // <class>
                event=skipSpacesAndGetNextEvent(event);
            }catch(javax.xml.stream.XMLStreamException ex){
                throw new IoxException(ex);
            }
            if(attrName!=null){
                throw new IllegalStateException("attrName != null");
            }
            if(event.isStartElement()){
            	StartElement element = (StartElement) event;
            	QName qName=element.getName();
            	Element prop=getIliProperty(aClass, qName);
            	if(prop==null){
        			throw new IoxSyntaxException("unexpected element: "+qName.getLocalPart());
            	}else{
            		attrName=prop.getName();
	            	XMLEvent peekEvent2=reader.peek();
	            	if(!peekEvent2.isEndElement()){
	            		event=reader.nextEvent(); // <class>
	            	}
	                XMLEvent peekEvent=reader.peek();
	                if(!peekEvent.isEndElement()){
	                	// current event is an attribute
	                	event=skipSpacesAndGetNextEvent(event);
	                }
	                if(event.isCharacters() && prop instanceof AttributeDef){ // primitive attribute
	            		Characters character = (Characters) event;
	                    String characterValue=character.getData();
	                    if(characterValue.contains("OTHERS")){
	                    	throw new IoxException("OTHERS not yet implemented.");
	                    }
	            		iomObj.setattrvalue(attrName, characterValue);
	            		event=reader.nextEvent(); // <characters>
	            		if(event.isEndElement()){ // check if end attribute
	            			attrName=null;
	            		}else{
	            			throw new IoxSyntaxException(event2msgtext(event));
	            		}
	                }else if(event.isEndElement()){
	                	attrName=null;
	                	continue;
	                }else if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_GEOM_COORD)){
	            		event=reader.nextEvent(); // <coords>
	                    event=skipSpacesAndGetNextEvent(event);
	                    if(!event.isStartElement()){
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                    IomObject returnedSegment=prepareSegment(event, SEGMENTTYPE_COORD);
	                    if(returnedSegment.getattrcount()==0){
	                    	throw new IoxException("expected coord. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
						iomObj.addattrobj(attrName, returnedSegment);
	                    event=reader.nextEvent(); // <coord>
	                    event=reader.nextEvent(); // </coord>
	                    event=skipSpacesAndGetNextEvent(event); // <characters>
	                    if(event.isEndElement()){
	                        attrName=null;
	                    }else{
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                }else if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_GEOM_MULTICOORD)){
	                    if(!event.isStartElement()){
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	            		event=reader.nextEvent(); // <multicoord> --> <characters>
	            		event=skipSpacesAndGetNextEvent(event); // <characters> --> <coord>
	            		if(!event.isStartElement()){
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	            		IomObject returnedObj=prepareSequence(event);
	                    if(returnedObj.getattrcount()==0){
	                    	throw new IoxException("expected multicoord. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
	                    iomObj.addattrobj(attrName, returnedObj);
	                    event=reader.nextEvent(); // <coord> --> <characters>
	                    event=skipSpacesAndGetNextEvent(event); // <characters> --> <multicoord>
	                    event=reader.nextEvent(); // <multicoord> --> <characters>
	                    event=skipSpacesAndGetNextEvent(event); // <characters> --> </multicoord>
	                    if(event.isEndElement()){
	                        attrName=null;
	                        return iomObj;
	                    }else{
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                }else if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_GEOM_POLYLINE)){
	                    IomObject polyline = preparePolyline(event);
	                    if(polyline.getattrcount()==0){
	                    	throw new IoxException("expected polyline. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
	                    iomObj.addattrobj(attrName, polyline);
	                    event=reader.nextEvent(); // <polyline>
	                    event=skipSpacesAndGetNextEvent(event);
	                    if(event.isEndElement()){ // </attribute>
	                        attrName=null;
	                        return iomObj;
	                    }else{
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                }else if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_GEOM_MULTIPOLYLINE)){
	                    IomObject multiPolyline = prepareMultiPolyline(event);
	                    if(multiPolyline.getattrcount()==0){
	                    	throw new IoxException("expected multipolyline. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
	                    iomObj.addattrobj(attrName, multiPolyline);
	                    event=reader.nextEvent(); // <multipolyline>
	                    event=skipSpacesAndGetNextEvent(event);
	                    if(event.isEndElement()){ // </attribute>
	                        attrName=null;
	                        return iomObj;
	                    }else{
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                }else if((event.isStartElement() && event.asStartElement().getName().equals(QNAME_GEOM_LINESTRING))
	                		|| (event.isStartElement() && event.asStartElement().getName().equals(QNAME_GEOM_ORIENTABLECURVE))
	                		|| (event.isStartElement() && event.asStartElement().getName().equals(QNAME_GEOM_COMPOSITECURVE))){
	                	throw new IoxException("unsupported geometry "+event.asStartElement().getName().getLocalPart());
	                }else if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_GEOM_SURFACE)){
	                    // SURFACE (surface/area)
	                	IomObject multiSurface=createIomObject("MULTISURFACE", null);
	                	IomObject surface=prepareSurface(event);
	                	if(surface.getattrcount()==0){
	                    	throw new IoxException("expected surface. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
	                	multiSurface.addattrobj("surface", surface);
	                	if(!event.isStartElement()){
	                		throw new IoxSyntaxException(event2msgtext(event));
	                	}
	                	event=reader.nextEvent(); // <surface>
	                	event=skipSpacesAndGetNextEvent(event);
	                	iomObj.addattrobj(attrName, multiSurface);
	                    return iomObj;
	                }else if(event.isStartElement() && (event.asStartElement().getName().equals(QNAME_GEOM_MULTISURFACE) || event.asStartElement().getName().equals(QNAME_GEOM_MULTIAREA))){
	                    // MULTISURFACE (surfaces and/or areas)
	                	IomObject multisurface=prepareMultiSurface(event);
	                	if(multisurface.getattrcount()==0){
	                    	throw new IoxException("expected multisurface. unexpected event: "+event.asStartElement().getName().getLocalPart());
	                    }
	                    iomObj.addattrobj(attrName, multisurface);
	                    if(!event.isStartElement()){
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                    event=reader.nextEvent(); // <surface>
	                    event=skipSpacesAndGetNextEvent(event);
	                    return iomObj;
	                }else{
            			Element attrElement=getIliProperty(aClass, qName);
            			if(attrElement instanceof RoleDef){
            				RoleDef role=(RoleDef) attrElement;
            				if(role.getContainer()!=null){
            					Container elementContainer=role.getContainer();
            					if(elementContainer instanceof AssociationDef){
            						AssociationDef association=(AssociationDef) elementContainer;
            						if(association instanceof Viewable){
            							Viewable viewable=(Viewable) association;
                						iomObj=readReference(aClass,iomObj, element, role,association);
                						event=reader.nextEvent(); // <role>
                						event=reader.nextEvent(); // </role>
                						attrName=null;
            						}
            					}
                			}
            			}else{
            				// structure
    	            		QName subQName=((StartElement) event).getName();
    	            		Viewable iliStruct=getIliClass(subQName);
    	            		if(iliStruct==null){
    	                    	String xmlCollection = collectXMLElement(reader,event); // <blackbox></blackbox>
    	            			iomObj.setattrvalue(attrName, xmlCollection);
    	            			attrName=null;
    	            		}else{
    		                	IomObject structObj=createIomObject(iliStruct.getName(), null);
    		                    iomObj.addattrobj(attrName, readObject(event, iliStruct, structObj));
    		                    if(!event.isStartElement()){
    		                    	throw new IoxSyntaxException(event2msgtext(event));
    		                    }
    		                    event=reader.nextEvent(); // <structure>
    		                    event=skipSpacesAndGetNextEvent(event);
    		                    if(!event.isEndElement()){
    		                    	throw new IoxSyntaxException(event2msgtext(event));
    		                    }
    		                    event=reader.nextEvent(); // </structure>
    		                    event=skipSpacesAndGetNextEvent(event);
    		                    if(!event.isEndElement()){
    		                    	throw new IoxSyntaxException(event2msgtext(event));
    		                    }
    		                    return iomObj;
    	            		}
            			}
	                }
            	}
            }else if(event.isEndElement() && attrName==null){
                return iomObj;
            }else if(event.isEndElement() && attrName!=null){
                event=skipSpacesAndGetNextEvent(event);
                if(!event.isEndElement()){
                	throw new IoxSyntaxException(event2msgtext(event));
                }
                event=reader.nextEvent(); //</attribute>
                attrName=null;
            }else{
            	throw new IoxSyntaxException(event2msgtext(event));
            }
        }
        return null;
    }

	private IomObject readReference(Viewable aclass,IomObject iomObj, StartElement element, RoleDef role, AssociationDef association) throws IoxException{
		String refOid=element.getAttributeByName(QNAME_ILI_REF).getValue();
		if(refOid.length()<=1){
			throw new IoxException("unexpected reference format "+refOid);
		}
		if(aclass.isExtending(association)){
			iomObj.addattrobj(role.getName(),"REF").setobjectrefoid(refOid);
			IomObject anObject=iomObj.getattrobj(role.getName(), 0);
			setAdditionalObjectInfo(element, anObject);
		}else{
			// tests, if this association has no link object,
			// but is embedded in a association end object.
			if(association.isLightweight()){
				// tests, if the association that this role is an end of,
				// is embedded into the object that this role points to.
				if(isEmbeddedRole(role)){
					// embedded
					iomObj.addattrobj(role.getName(),"REF").setobjectrefoid(refOid);
					IomObject aObject=iomObj.getattrobj(role.getName(), 0);
					setAdditionalObjectInfo(element, aObject);
				}else{
					// ignore
				}
			}else{
				linkPool.addAssocLink(association, role,iomObj.getobjectoid(),refOid);
			}
		}
		return iomObj;
	}

	private boolean isEmbeddedRole(RoleDef role) {
		return role.getOppEnd().isAssociationEmbedded();
	}   
    
    /** Prepare multisurface
     * @param event
     * @return IomObject multisurface
     * @throws IoxException
     * @throws XMLStreamException
     */
    private IomObject prepareMultiSurface(XMLEvent event) throws IoxException, XMLStreamException {
    	IomObject multiSurface=createIomObject("MULTISURFACE", null);
    	event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
    	while(event.isStartElement() && (event.asStartElement().getName().equals(QNAME_GEOM_SURFACE) || event.asStartElement().getName().equals(QNAME_GEOM_AREA))){
    		multiSurface.addattrobj("surface", prepareSurface(event));
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
    private IomObject prepareSurface(XMLEvent event) throws IoxException, XMLStreamException {
    	IomObject surface=createIomObject("SURFACE", null);
    	event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
    	while(event.isStartElement() && (event.asStartElement().getName().equals(QNAME_GEOM_INTERIOR) || event.asStartElement().getName().equals(QNAME_GEOM_EXTERIOR))){
	    	surface.addattrobj("boundary", prepareBoundary(event));
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
    private IomObject prepareBoundary(XMLEvent event) throws IoxException, XMLStreamException {
    	IomObject boundary=createIomObject("BOUNDARY", null);
    	event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
    	while(event.isStartElement() && event.asStartElement().getName().equals(QNAME_GEOM_POLYLINE)){
	    	boundary.addattrobj("polyline", preparePolyline(event));
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
    private IomObject prepareMultiPolyline(XMLEvent event) throws IoxException, XMLStreamException {
    	IomObject multiPolyline=createIomObject("MULTIPOLYLINE", null);
    	event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
    	while(event.isStartElement() && event.asStartElement().getName().equals(QNAME_GEOM_POLYLINE)){
	    	multiPolyline.addattrobj("polyline", preparePolyline(event));
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
    private IomObject preparePolyline(XMLEvent event) throws IoxException, XMLStreamException {
    	IomObject polyline=createIomObject("POLYLINE", null);
		event=reader.nextEvent();
		event=skipSpacesAndGetNextEvent(event);
		polyline.addattrobj("sequence", prepareSequence(event));
    	return polyline;
    }
    
    /** Prepare sequence
     * @param event
     * @return IomObject sequence
     * @throws XMLStreamException
     * @throws IoxException
     */
	private IomObject prepareSequence(XMLEvent event) throws XMLStreamException, IoxException {
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
				sequence.addattrobj("segment", prepareSegment(event, segmentType));
				event=reader.nextEvent();
				if(!event.isCharacters()){
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
	private IomObject prepareSegment(XMLEvent event, String segmentType) throws IoxException, XMLStreamException {
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
					case R: // ignore
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