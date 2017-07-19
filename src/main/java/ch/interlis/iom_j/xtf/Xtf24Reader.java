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
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.ili2c.metamodel.ViewableTransferElement;
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
	private Topic topic=null;
	private IomObject iomObj=null;
	
	// ili content
	private HashMap<QName, Topic> iliTopics=null;
    private HashMap<QName, Viewable> iliClasses=null;
    private HashMap<Viewable, HashMap<QName, Element>> iliProperties=null;
    
    // coords
    public enum SegmentType {
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
    private static final int END_BASKET=15;
    private static final int END_DATASECTION=16;
    private static final int END_TRANSFER=17;
    // namespace
    private static final String NAMESPACE_ILIXMLBASE="http://www.interlis.ch/xtf/2.4/";
    private static final String NAMESPACE_ILIXMLBASE_INTERLIS=NAMESPACE_ILIXMLBASE+"INTERLIS";
    private static final String METAATTR_NAMESPACE = "ili2.ilixtf24.namespaceName";
    private static final String NAMESPACE_GEOM="http://www.interlis.ch/geometry/1.0";
    private static final String NAMESPACE_XMLSCHEMA="http://www.w3.org/2001/XMLSchema-instance";
	// qnames
    private static final QName QNAME_ILI_TRANSFER=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"transfer");
    private static final QName QNAME_XML_HEADERSECTION=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"headersection");
    private static final QName QNAME_XML_SENDER=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"sender");
    private static final QName QNAME_XML_COMMENTS=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"comments");
    private static final QName QNAME_XML_DATASECTION=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"datasection");
    private static final QName QNAME_XML_MODELS=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"models");
    private static final QName QNAME_XML_MODEL=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"model");
    private static final QName QNAME_XML_SCHEMA=new QName(NAMESPACE_XMLSCHEMA,"xsi");
    private static final QName QNAME_BID = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "bid");
    private static final QName QNAME_TID = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "tid");
    
    private static final String SEGMENTTYPE_COORD="COORD";
    private static final String SEGMENTTYPE_ARC="ARC";
    
	private static final QName QNAME_GEOM_COORD = new QName(NAMESPACE_GEOM, "coord");
	private static final QName QNAME_GEOM_ARC = new QName(NAMESPACE_GEOM, "arc");
	private static final QName QNAME_GEOM_C1 = new QName(NAMESPACE_GEOM, "c1");
	private static final QName QNAME_GEOM_C2 = new QName(NAMESPACE_GEOM, "c2");
	private static final QName QNAME_GEOM_C3 = new QName(NAMESPACE_GEOM, "c3");
	private static final QName QNAME_GEOM_A1 = new QName(NAMESPACE_GEOM, "a1");
	private static final QName QNAME_GEOM_A2 = new QName(NAMESPACE_GEOM, "a2");
	private static final QName QNAME_GEOM_R = new QName(NAMESPACE_GEOM, "r");
	private static final QName QNAME_MULTICOORD = new QName(NAMESPACE_GEOM, "multicoord");
	
	private static final QName QNAME_POLYLINE = new QName(NAMESPACE_GEOM, "polyline");
	private static final QName QNAME_MULTIPOLYLINE = new QName(NAMESPACE_GEOM, "multipolyline");
	private static final QName QNAME_LINESTRING = new QName(NAMESPACE_GEOM, "linestring");
	private static final QName QNAME_BOUNDARY = new QName(NAMESPACE_GEOM, "boundary");
	
	private static final QName QNAME_ORIENTABLECURVE = new QName(NAMESPACE_GEOM, "orientablecurve");
	private static final QName QNAME_COMPOSITECURVE = new QName(NAMESPACE_GEOM, "compositecurve");
	private static final QName QNAME_POLYGON = new QName(NAMESPACE_GEOM, "polygon");
	private static final QName QNAME_HREF = new QName(NAMESPACE_GEOM, "href");
	private static final QName QNAME_SEGMENTS = new QName(NAMESPACE_GEOM, "segments");
	private static final QName INTERPOLATIION_LINEAR = new QName(NAMESPACE_GEOM, "linear");
	private static final QName QNAME_EXTERIOR = new QName(NAMESPACE_GEOM, "exterior");
	private static final QName QNAME_INTERIOR = new QName(NAMESPACE_GEOM, "interior");
	private static final QName QNAME_CURVEMEMBER = new QName(NAMESPACE_GEOM, "curvemember");
	private static final QName QNAME_RING = new QName(NAMESPACE_GEOM, "ring");
	
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
		while(reader.hasNext()){
			javax.xml.stream.events.XMLEvent event=null;
			try{
				event=reader.nextEvent();
				if(event.isCharacters()){
					System.out.println("CharacterEvent: "+event.toString());
				}else if(event.isStartElement()){
					System.out.println("StartElementEvent: "+event.toString());
				}else if(event.isEndElement()){
					System.out.println("EndElementEvent: "+event.toString());
				}
				EhiLogger.debug(event.toString());
				System.out.println();
			}catch(javax.xml.stream.XMLStreamException ex){
				throw new IoxException(ex);
			}

			// start
			// first XML event has to be start document
			if(state==START){
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
                	topic=getIliTopic(element.getName());
                	if(topic==null){
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                	QName gmlId = QNAME_BID;
                	Attribute bid = element.getAttributeByName(gmlId);
                	if(bid!=null){
                		state=INSIDE_BASKET;
                		return new ch.interlis.iox_j.StartBasketEvent(topic.getScopedName(), bid.getValue());
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
                	Viewable viewable=null;
                    StartElement element = (StartElement) event;
                	String topicName=topic.getName();
                	String className=element.getName().getLocalPart();
                	QName extendedQName=new QName(element.getName().getNamespaceURI(), topicName+"."+className);
                	viewable=getIliClass(extendedQName);
                	if(viewable==null){
                		viewable=getIliClass(element.getName());
                	}
                    if(viewable==null){
                    	throw new IoxSyntaxException(event2msgtext(event));
                    }
                    Integer srsDimension=null;
                    QName qName = QNAME_TID;
                    Attribute oid = element.getAttributeByName(qName);
                    if(oid!=null){
                    	try{
                    		iomObj=createIomObject(viewable.getScopedName(), oid.getValue());
                    	}catch(IoxSyntaxException ioxEx){
                    		throw new IoxSyntaxException(event2msgtext(event)+"\n"+ioxEx);
                    	}
                		try{
							iomObj=readObject(event, viewable, iomObj);
							event=reader.nextEvent(); // <object>
							System.out.println("return "+iomObj.toString());
							System.out.println();
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
	
	private String event2msgtext(XMLEvent event){
		String output="";
		if(event instanceof StartElement){
			 output="unexpected start element "+event.asStartElement().getName().getLocalPart();
		}else if(event instanceof EndElement){
			output="unexpected end element "+event.asEndElement().getName().getLocalPart();
		}else if(event instanceof Characters){
			output="unexpected characters "+((Characters) event).getData();
		}
		System.out.println("return Exception: "+output);
		return output;
	}
	
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
				String modelNameSpace=model.getMetaValue(METAATTR_NAMESPACE);
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
    				Viewable viewable = (Viewable) classObj;
    				String localClassPart=viewable.getName();
    				String nameSpace=model.getMetaValue(METAATTR_NAMESPACE);
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
    
    private Topic getIliTopic(QName qName){
    	if(iliTopics==null){
    		fillIliMaps();
    	}
    	return iliTopics.get(qName);
    }
    
    private Viewable getIliClass(QName qName){
    	if(iliClasses==null){
    		fillIliMaps();
    	}
    	return iliClasses.get(qName);
    }
    
    private Element getIliProperty(Viewable aClass, QName qName) throws IoxException{
    	if(iliProperties==null){
    		fillIliMaps();
    	}
    	HashMap<QName, Element> iliPropIter=iliProperties.get(aClass);
    	return iliPropIter.get(qName);
    }
    
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
    
    private IomObject readObject(XMLEvent event, Viewable aClass, IomObject iomObj) throws IoxException, XMLStreamException {
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
						iomObj.addattrobj(attrName, prepareSegment(event, SEGMENTTYPE_COORD));
	                    event=reader.nextEvent(); // <coord>
	                    event=reader.nextEvent(); // </coord>
	                    event=skipSpacesAndGetNextEvent(event); // <characters>
	                    if(event.isEndElement()){
	                        attrName=null;
	                    }else{
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                }else if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_MULTICOORD)){
	                    if(!event.isStartElement()){
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	            		event=reader.nextEvent(); // <multicoord> --> <characters>
	            		event=skipSpacesAndGetNextEvent(event); // <characters> --> <coord>
	            		if(!event.isStartElement()){
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                    iomObj.addattrobj(attrName, prepareSequence(event));
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
	                }else if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_POLYLINE)){
	                    IomObject polyline = preparePolyline(event);
	                    iomObj.addattrobj(attrName, polyline);
	                    event=reader.nextEvent(); // <polyline>
	                    event=skipSpacesAndGetNextEvent(event);
	                    if(event.isEndElement()){ // </attribute>
	                        attrName=null;
	                        return iomObj;
	                    }else{
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                }else if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_MULTIPOLYLINE)){
	                    IomObject multiPolyline = prepareMultiPolyline(event);
	                    iomObj.addattrobj(attrName, multiPolyline);
	                    event=reader.nextEvent(); // <multipolyline>
	                    event=skipSpacesAndGetNextEvent(event);
	                    if(event.isEndElement()){ // </attribute>
	                        attrName=null;
	                        return iomObj;
	                    }else{
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                }else if((event.isStartElement() && event.asStartElement().getName().equals(QNAME_LINESTRING))
	                		|| (event.isStartElement() && event.asStartElement().getName().equals(QNAME_ORIENTABLECURVE))
	                		|| (event.isStartElement() && event.asStartElement().getName().equals(QNAME_COMPOSITECURVE))){
	                	throw new IoxSyntaxException("unsupported geometry "+event.asStartElement().getName().getLocalPart());
	                }else if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_POLYGON)){
	                    // surface/area MULTISURFACE
//	                	IomObject subIomObj=createIomObject("MULTISURFACE", null);
//	                    iomObj.addattrobj(attrName, readPolygon(event, subIomObj));
//	                    if(!event.isStartElement()){
//	                    	throw new IoxSyntaxException(event2msgtext(event));
//	                    }
//	                    event=reader.nextEvent(); // <Polygon>
//	                    event=skipSpacesAndGetNextEvent(event);
//	                    return iomObj;
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
    	                    	String xmlCollection = collectXMLElement(reader,event);
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

	private IomObject readReference(Viewable aclass,IomObject iomObj, StartElement element, RoleDef role, AssociationDef association) throws IoxSyntaxException{
		String refOid=element.getAttributeByName(QNAME_HREF).getValue();
		if(refOid.length()<=1 || !refOid.startsWith("#")){
			throw new IoxSyntaxException("unexpected reference format "+refOid);
		}
		refOid=refOid.substring(1);
		if(aclass.isExtending(association)){
			iomObj.addattrobj(role.getName(),"REF").setobjectrefoid(refOid);			
		}else{
			// tests, if this association has no link object,
			// but is embedded in a association end object.
			if(association.isLightweight()){
				// tests, if the association that this role is an end of,
				// is embedded into the object that this role points to.
				if(isEmbeddedRole(role)){
					// embedded
					iomObj.addattrobj(role.getName(),"REF").setobjectrefoid(refOid);
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

//    private IomObject prepareBoundary(XMLEvent event) throws IoxException, XMLStreamException {
//    	IomObject boundary=createIomObject("BOUNDARY", null);
//    	event=reader.nextEvent(); // <multipolyline> --> <characters>
//		event=skipSpacesAndGetNextEvent(event); // <characters> --> <polyline>
//    	while(event.isStartElement() && event.asStartElement().getName().equals(QNAME_POLYLINE)){
//	    	boundary.addattrobj("polyline", preparePolyline(event));
//	    	event=reader.nextEvent(); // <coord> --> <characters>
//	    	event=skipSpacesAndGetNextEvent(event); // <characters> --> </attr>
//    	}
//    	return boundary;
//    }
    
    private IomObject prepareMultiPolyline(XMLEvent event) throws IoxException, XMLStreamException {
    	IomObject multiPolyline=createIomObject("MULTIPOLYLINE", null);
    	event=reader.nextEvent(); // <multipolyline> --> <characters>
		event=skipSpacesAndGetNextEvent(event); // <characters> --> <polyline>
    	while(event.isStartElement() && event.asStartElement().getName().equals(QNAME_POLYLINE)){
	    	multiPolyline.addattrobj("polyline", preparePolyline(event));
	    	event=reader.nextEvent(); // <coord> --> <characters>
	    	event=skipSpacesAndGetNextEvent(event); // <characters> --> </attr>
    	}
    	return multiPolyline;
    }
    
    private IomObject preparePolyline(XMLEvent event) throws IoxException, XMLStreamException {
    	IomObject polyline=createIomObject("POLYLINE", null);
		event=reader.nextEvent(); // <polyline> --> <characters>
		event=skipSpacesAndGetNextEvent(event); // <characters> --> <sequence>
		polyline.addattrobj("sequence", prepareSequence(event));
    	return polyline;
    }
    
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
				event=reader.nextEvent(); // <coord> --> <characters>
				event=skipSpacesAndGetNextEvent(event); // <coord> --> <coordType>
				sequence.addattrobj("segment", prepareSegment(event, segmentType));
				event=reader.nextEvent(); // <coordType> --> <characters>
				if(!event.isCharacters()){
					throw new IoxSyntaxException(event2msgtext(event));
				}
				event=skipSpacesAndGetNextEvent(event); // <characters> --> (<coord> || </coord>)
			}
		}
		return sequence;
	}
    
	private IomObject prepareSegment(XMLEvent event, String segmentType) throws IoxException, XMLStreamException {
		if(segmentType==null){
			throw new IoxSyntaxException("expected segment type");
		}
		IomObject segment=createIomObject(segmentType, null);
		while(!event.isEndElement()){
	        if(event.isStartElement()){
	        	String segmentTypeName=event.asStartElement().getName().getLocalPart();
				event=reader.nextEvent(); // <coordType> --> <characters>
				if(!event.isCharacters()){
					throw new IoxSyntaxException(event2msgtext(event));
				}
				SegmentType upperCaseTypeName = SegmentType.valueOf(segmentTypeName.toUpperCase());
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
					case R: segment.setattrvalue("r", event.asCharacters().getData());
						break;
					case MULTICOORD: segment.setattrvalue("multicoord", event.asCharacters().getData());
						break;
					default: throw new IoxSyntaxException(event2msgtext(event));
				}
	        }
	        event=reader.nextEvent(); // <characters> --> </coordType>
	        if(event.isStartElement()){
				throw new IoxSyntaxException(event2msgtext(event));
	        }
			event=reader.nextEvent(); // </coordType> --> <characters>
			event=skipSpacesAndGetNextEvent(event); // <characters> --> (<coordType> || </coord>)
		}
		return segment;
	}

//	private IomObject readPolygon(XMLEvent event, IomObject iomObj) throws IoxException, XMLStreamException { // attrName, MULTISIRFACE
//    	IomObject boundaryObj=createIomObject("BOUNDARY", null);
//    	IomObject surfaceObj=createIomObject("SURFACE", null);
//    	IomObject polygonObj=createIomObject("MULTISURFACE", null);
//    	if(!event.isStartElement()){
//    		throw new IoxSyntaxException(event2msgtext(event));
//        }
//    	event=reader.nextEvent(); // <polygon>
//        event=skipSpacesAndGetNextEvent(event);
//        if(!event.isStartElement()){
//        	throw new IoxSyntaxException(event2msgtext(event));
//        }
//        int exteriorExist=0; // first boundary has to be an outerBoundary (exterior)
//        while(event.isStartElement() && (event.asStartElement().getName().equals(QNAME_EXTERIOR) || event.asStartElement().getName().equals(QNAME_INTERIOR))){
//        	if(event.asStartElement().getName().equals(QNAME_EXTERIOR)){
//        		exteriorExist=exteriorExist+1;
//        	}else if(event.asStartElement().getName().equals(QNAME_INTERIOR)){
//        		if(exteriorExist==0){
//            		throw new IoxSyntaxException("no exterior ring found");
//            	}
//        	}
//        	if(!event.isStartElement()){
//        		throw new IoxSyntaxException(event2msgtext(event));
//            }
//        	event=reader.nextEvent(); // <exterior/interior>
//            event=skipSpacesAndGetNextEvent(event);
//            
//        	boundaryObj=readBoundary(event, surfaceObj);
//        	surfaceObj.addattrobj("boundary", boundaryObj);
//        	if(!event.isStartElement()){
//        		throw new IoxSyntaxException(event2msgtext(event));
//            }
//        	event=reader.nextEvent(); // <Ring>
//        	event=skipSpacesAndGetNextEvent(event);
//        }
//        if(exteriorExist==0){
//        	throw new IoxSyntaxException("no lines found in polygon of "+iomObj.getobjecttag());
//        }
//        polygonObj.addattrobj("surface", surfaceObj);
//        return polygonObj;
//    }
	
//    private IomObject readBoundary(XMLEvent event, IomObject surfaceObj) throws IoxException, XMLStreamException {
//        IomObject boundaryObj=createIomObject("BOUNDARY", null);
//        if(!event.asStartElement().getName().equals(QNAME_RING)){
//        	throw new IoxSyntaxException(event2msgtext(event));
//        }
//        event=reader.nextEvent(); // <Ring>
//        event=skipSpacesAndGetNextEvent(event);
//        while(event.isStartElement() && event.asStartElement().getName().equals(QNAME_CURVEMEMBER)){
//        	if(!event.isStartElement()){
//        		throw new IoxSyntaxException(event2msgtext(event));
//            }
//        	event=reader.nextEvent(); // <curveMember>
//        	event=skipSpacesAndGetNextEvent(event);
//        	boundaryObj.addattrobj("polyline", readPolyline(event));
//        	if(!event.isStartElement()){
//        		throw new IoxSyntaxException(event2msgtext(event));
//            }
//        	event=reader.nextEvent(); // <Curve>
//        	event=skipSpacesAndGetNextEvent(event);
//        }
//        if(!event.isEndElement()){
//        	throw new IoxSyntaxException(event2msgtext(event));
//        }
//        event=reader.nextEvent(); // </curveMember>
//        event=skipSpacesAndGetNextEvent(event);
//        if(!event.isEndElement()){
//        	throw new IoxSyntaxException(event2msgtext(event));
//        }
//        event=reader.nextEvent(); // </Ring>
//        event=skipSpacesAndGetNextEvent(event);
//		return boundaryObj;
//	}
    
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