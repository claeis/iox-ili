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
	private LinkPool linkPool=new LinkPool();
    private Iterator<IomObject> linkIterator;
	private java.io.InputStream inputFile=null;
	private javax.xml.stream.XMLEventReader reader=null;
	private IoxFactoryCollection factory=new  ch.interlis.iox_j.DefaultIoxFactoryCollection();
	private TransferDescription td;
	private HashMap<QName, Topic> iliTopics=null;
    private HashMap<QName, Viewable> iliClasses=null;
    private HashMap<Viewable, HashMap<QName, Element>> iliProperties=null;
    private ArrayList<String> models=new ArrayList<String>();
    private String sender=null;
    private String comment=null;
    private Topic topic=null;
    
	// events
    // start
    private int state = START;
    private static final int START=0;
    private static final int START_DOCUMENT=1;
    private static final int INSIDE_TRANSFER=2;
    // headersection
    private static final int INSIDE_HEADERSECTION=3;
    private static final int INSIDE_MODELS=4;
    private static final int INSIDE_MODEL=11;
    private static final int INSIDE_SENDER=13;
    private static final int END_SENDER=14;
    private static final int INSIDE_COMMENTS=15;
    private static final int END_COMMENTS=16;
    private static final int END_MODEL=12;
    private static final int END_MODELS=5;
    private static final int END_HEADERSECTION=6;
    // datasection
    private static final int INSIDE_DATASECTION=7;
    private static final int INSIDE_BASKET=18;
    private static final int INSIDE_CLASS=21;
    private static final int END_CLASS=22;
    private static final int END_BASKET=19;
    private static final int END_DATASECTION=8;
    // end
    private static final int END_TRANSFER=9;
    private static final int END_DOCUMENT=10;
    // namespace
    private static final String NAMESPACE_ILIXMLBASE="http://www.interlis.ch/xtf/2.4/";
    private static final String NAMESPACE_ILIXMLBASE_INTERLIS=NAMESPACE_ILIXMLBASE+"INTERLIS";
    private static final String METAATTR_NAMESPACE = "ili2.ilixtf24.namespaceName";
    private static final String NAMESPACE_GEOM="http://www.interlis.ch/geometry/1.0";
    private static final String NAMESPACE_XMLSCHEMA="http://www.w3.org/2001/XMLSchema-instance";
	// qnames
    private static final QName QNAME_ILI_TRANSFER=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"transfer");
    private static final QName QNAME_ILI_GEOM=new QName(NAMESPACE_GEOM, "geom");
    private static final QName QNAME_XML_HEADERSECTION=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"headersection");
    private static final QName QNAME_XML_SENDER=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"sender");
    private static final QName QNAME_XML_COMMENTS=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"comments");
    private static final QName QNAME_XML_DATASECTION=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"datasection");
    private static final QName QNAME_XML_MODELS=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"models");
    private static final QName QNAME_XML_MODEL=new QName(NAMESPACE_ILIXMLBASE_INTERLIS,"model");
    private static final QName QNAME_XML_SCHEMA=new QName(NAMESPACE_XMLSCHEMA,"xsi");
    private static final QName QNAME_BID = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "bid");
    private static final QName QNAME_TID = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "tid");
    
	private static final QName QNAME_POINT = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "point");
	private static final QName QNAME_CURVE = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "curve");
	private static final QName QNAME_LINESTRING = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "linestring");
	private static final QName QNAME_ORIENTABLECURVE = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "orientablecurve");
	private static final QName QNAME_COMPOSITECURVE = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "compositecurve");
	private static final QName QNAME_POLYGON = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "polygon");
	private static final QName QNAME_HREF = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "href");
	private static final QName QNAME_SEGMENTS = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "segments");
	private static final QName INTERPOLATIION_LINEAR = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "linear");
	private static final QName INTERPOLATIION_CIRCULARARC3POINTS = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "circulararc3points");
	private static final QName QNAME_POS = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "pos");
	private static final QName QNAME_EXTERIOR = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "exterior");
	private static final QName QNAME_INTERIOR = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "interior");
	private static final QName QNAME_CURVEMEMBER = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "curvemember");
	private static final QName QNAME_RING = new QName(NAMESPACE_ILIXMLBASE_INTERLIS, "ring");
    
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
	
	private void init(java.io.InputStream in) throws IoxException{
		javax.xml.stream.XMLInputFactory inputFactory = javax.xml.stream.XMLInputFactory.newInstance();
		try{
			reader=inputFactory.createXMLEventReader(in);
		}catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
	}
	
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

	@Override
	public IoxEvent read() throws IoxException {
		IomObject iomObj=null;
		while(reader.hasNext()){
			javax.xml.stream.events.XMLEvent event=null;
			try{
				event=reader.nextEvent();
				System.out.println(event.toString());
				EhiLogger.debug(event.toString());
			}catch(javax.xml.stream.XMLStreamException ex){
				throw new IoxException(ex);
			}
			// start
			if(state==START){
				System.out.println("START");
				if(event.isStartDocument()){
					state=START_DOCUMENT;
					System.out.println("return new ch.interlis.iox_j.StartDocumentEvent();");
				}else{
					throw new IoxSyntaxException(event2msgtext(event));
				}
			// start document
			}else if(state==START_DOCUMENT){
                if(event.isEndDocument()){
                    state=END_DOCUMENT;
                }else if(event.isStartElement()){
                    StartElement element = (StartElement) event;
                    if(element.getName().equals(QNAME_ILI_TRANSFER)){
                        state=INSIDE_TRANSFER;
                        System.out.println("return new ch.interlis.iox_j.StartTransferEvent();");
                        return new ch.interlis.iox_j.StartTransferEvent();
                    }else{
                    	throw new IoxSyntaxException(event2msgtext(event));
                    }
                }else if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }
            // start transfer
            }else if(state==INSIDE_TRANSFER){
                if(event.isEndElement()){
                    state=END_TRANSFER;
                    return new ch.interlis.iox_j.EndTransferEvent();
                }else if(event.isStartElement()){
                	if(event.asStartElement().getName().equals(QNAME_XML_HEADERSECTION)){
                		state=INSIDE_HEADERSECTION;
                		continue;
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }
            // start header section
            }else if(state==INSIDE_HEADERSECTION){
                if(event.isEndElement()){
                	throw new IoxSyntaxException(event2msgtext(event));
                }else if(event.isStartElement()){
                	if(event.asStartElement().getName().equals(QNAME_XML_MODELS)){
                		state=INSIDE_MODELS;
                		continue;
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }
            // start models
            }else if(state==INSIDE_MODELS){
                if(event.isEndElement()){
                	if(event.asEndElement().getName().equals(QNAME_XML_MODELS)){
                		state=END_MODELS;
                		continue;
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isStartElement()){
                	if(event.asStartElement().getName().equals(QNAME_XML_MODEL)){
                		state=INSIDE_MODEL;
                		continue;
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }
            // start model
            }else if(state==INSIDE_MODEL){
                if(event.isEndElement()){
                	if(event.asEndElement().getName().equals(QNAME_XML_MODEL)){
                		state=END_MODEL;
                		continue;
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isStartElement()){
                	throw new IoxSyntaxException(event2msgtext(event));
                }else if(event.isCharacters()){
                    Characters character = (Characters) event;
                    if(character.getData()==null || character.getData().length()==0){
                    	throw new IoxSyntaxException("expected modelname. "+event2msgtext(event));
                    }else{
                    	models.add(character.getData());
                    	System.out.println("Character Element Model: models.add("+character.getData()+")");
                    	continue;
                    }
                }
            // end model
            }else if(state==END_MODEL){
                if(event.isEndElement()){
                	if(event.asEndElement().getName().equals(QNAME_XML_MODELS)){
                		state=END_MODELS;
                		continue;
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isStartElement()){
                	if(event.asStartElement().getName().equals(QNAME_XML_MODEL)){
                		state=INSIDE_MODEL;
                		continue;
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }
            // end models
            }else if(state==END_MODELS){
                if(event.isEndElement()){
                	if(event.asEndElement().getName().equals(QNAME_XML_HEADERSECTION)){
                		if(models.size()==0){
                			throw new IoxSyntaxException("expected at least 1 Model. "+event2msgtext(event));
                		}else{
                			state=END_HEADERSECTION;
                			continue;
                		}
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isStartElement()){
                	if(event.asStartElement().getName().equals(QNAME_XML_SENDER)){
                		if(models.size()==0){
                			throw new IoxSyntaxException("expected at least 1 Model. "+event2msgtext(event));
                		}else{
                			state=INSIDE_SENDER;
                			continue;
                		}
                	}else if(event.asStartElement().getName().equals(QNAME_XML_COMMENTS)){
                		if(models.size()==0){
                			throw new IoxSyntaxException("expected at least 1 Model. "+event2msgtext(event));
                		}else{
                			state=INSIDE_COMMENTS;
                			continue;
                		}
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }
            // start sender
            }else if(state==INSIDE_SENDER){
                if(event.isEndElement()){
                	if(event.asEndElement().getName().equals(QNAME_XML_SENDER)){
                		state=END_SENDER;
                		continue;
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isStartElement()){
                	throw new IoxSyntaxException(event2msgtext(event));
                }else if(event.isCharacters()){
                    Characters character = (Characters) event;
                    if(character.getData()==null || character.getData().length()==0){
                    	throw new IoxSyntaxException("expected sendername. "+event2msgtext(event));
                    }else{
                    	sender=character.getData();
                    	System.out.println("Character Element Sender: sender=("+character.getData()+")");
                    	continue;
                    }
                }
            // start comments
            }else if(state==INSIDE_COMMENTS){
                if(event.isEndElement()){
                	if(event.asEndElement().getName().equals(QNAME_XML_COMMENTS)){
                		state=END_COMMENTS;
                		continue;
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isStartElement()){
                	throw new IoxSyntaxException(event2msgtext(event));
                }else if(event.isCharacters()){
                    Characters character = (Characters) event;
                    if(character.getData()==null || character.getData().length()==0){
                    	throw new IoxSyntaxException("expected a comment. "+event2msgtext(event));
                    }else{
                    	comment=character.getData();
                    	System.out.println("Character Element Comments: comment=("+character.getData()+")");
                    	continue;
                    }
                }
            // end sender
            }else if(state==END_SENDER){
                if(event.isEndElement()){
                	state=END_HEADERSECTION;
                	continue;
                }else if(event.isStartElement()){
                	if(event.asStartElement().getName().equals(QNAME_XML_COMMENTS)){
                		if(comment==null){
                			state=INSIDE_COMMENTS;
                			continue;
                		}else{
                			throw new IoxSyntaxException("comments dublicated. "+event2msgtext(event));
                		}
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }
            // end commments
            }else if(state==END_COMMENTS){
                if(event.isEndElement()){
                	state=END_HEADERSECTION;
                	continue;
                }else if(event.isStartElement()){
                	if(event.asStartElement().getName().equals(QNAME_XML_SENDER)){
                		if(sender==null){
                			state=INSIDE_SENDER;
                			continue;
                		}else{
                			throw new IoxSyntaxException("sender dublicated. "+event2msgtext(event));
                		}
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }
            // end header section
            }else if(state==END_HEADERSECTION){
                if(event.isEndElement()){
                	throw new IoxSyntaxException(event2msgtext(event));
                }else if(event.isStartElement()){
                	if(event.asStartElement().getName().equals(QNAME_XML_DATASECTION)){
                		// start data section
                		state=INSIDE_DATASECTION;
                		continue;
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }
            }else if(state==INSIDE_DATASECTION){
                if(event.isEndElement()){
                	if(event.asEndElement().getName().equals(QNAME_XML_DATASECTION)){
                		// end data section
                		state=END_DATASECTION;
                		continue;
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                	}
                }else if(event.isStartElement()){
                	// start basket
                    StartElement element = (StartElement) event;
                    topic=getIliTopic(element.getName());
                    if(topic==null){
                    	throw new IoxSyntaxException(event2msgtext(event));
                    }
                    QName gmlId = QNAME_BID;
                    Attribute bid = event.asStartElement().getAttributeByName(gmlId);
                    if(bid!=null){
                        state=INSIDE_BASKET;
                        System.out.println("return new ch.interlis.iox_j.StartBasketEvent("+topic.getScopedName().toString()+", "+bid.getValue()+");");
                        return new ch.interlis.iox_j.StartBasketEvent(topic.getScopedName(), bid.getValue());
                    }else{
                    	throw new IoxSyntaxException(event2msgtext(event));
                    }
                }else if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }
            }else if(state==INSIDE_BASKET){
                if(event.isEndElement()){
                	if(iomObj!=null){
                        // return object
                		System.out.println("return new IomObject("+iomObj.toString()+");");
                    	return new ch.interlis.iox_j.ObjectEvent(iomObj);
                    }
                }else if(event.isStartElement()){
                	// start object
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
                    Attribute oid = event.asStartElement().getAttributeByName(qName);
                    if(oid!=null){
                    	try{
                    		iomObj=createIomObject(viewable.getScopedName(), oid.getValue());
                    		System.out.println("create new IomObject("+iomObj.toString()+")");
                    	}catch(IoxSyntaxException ioxEx){
                    		throw new IoxSyntaxException(event2msgtext(event)+"\n"+ioxEx);
                    	}
                		try{
							iomObj=readObject(event, viewable, iomObj);
							System.out.println("return new IomObject("+iomObj.toString()+");");
	                    	return new ch.interlis.iox_j.ObjectEvent(iomObj);
						}catch(IoxSyntaxException ex){								
							throw new IoxSyntaxException(event2msgtext(event)+"\n"+ex);
						}catch(XMLStreamException e){
							throw new IoxException(e);
						}
                    }else{
                    	throw new IoxSyntaxException(event2msgtext(event));
                    }
                }else if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                    XMLEvent peekEvent=null;
                    try {
						peekEvent=reader.peek();
					} catch (XMLStreamException ex) {
						 throw new IoxSyntaxException(ex);
					}
                    if(peekEvent.isEndElement()){
                    	if(iomObj==null){
                    		state=END_BASKET;
                    		continue;
                    	}
                    }
                }else{
                	throw new IoxSyntaxException(event2msgtext(event));
                }
            }else if(state==END_BASKET){
                if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }else if(event.isEndElement()){
                	// end basket
                	state=END_DATASECTION;
                	System.out.println("return new ch.interlis.iox_j.EndBasketEvent();");
                    return new ch.interlis.iox_j.EndBasketEvent();
                }else if(event.isStartElement()){
                	// start object
                	state=INSIDE_DATASECTION;
                	continue;
                }
            // end data section
            }else if(state==END_DATASECTION){
                if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                    XMLEvent peekEvent=null;
                    try {
						peekEvent=reader.peek();
					} catch (XMLStreamException ex) {
						 throw new IoxSyntaxException(ex);
					}
                    if(peekEvent.isEndElement()){
                    	// end datasection
                    	state=END_TRANSFER;
                    	continue;
                    }else if(peekEvent.isStartElement()){
                    	// start basket
                    	state=INSIDE_DATASECTION;
                    	continue;
                    }
                }else if(event.isStartElement() || event.isEndElement()){
                	throw new IoxSyntaxException(event2msgtext(event));
                }
            // end transfer
            }else if(state==END_TRANSFER){
                state=END_DOCUMENT;
                System.out.println("return new ch.interlis.iox_j.EndTransferEvent();");
                System.out.println("return new ch.interlis.iox_j.EndDocumentEvent();");
                System.out.println("END");
                return new ch.interlis.iox_j.EndTransferEvent();
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
                event=skipSpaces(event);
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
	                	event=skipSpaces(event);
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
            		}else if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_POINT)){
	            		event=reader.nextEvent(); // <Point>
	                    event=skipSpaces(event);
	                    if(!event.isStartElement()){
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
						iomObj.addattrobj(attrName, prepareSegments(event));
	                    event=reader.nextEvent(); // <posList>
	                    event=skipSpaces(event);
	                    if(event.isEndElement()){
	                        attrName=null;
	                        return iomObj;
	                    }else{
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                }else if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_CURVE)){
	                    IomObject polyline = readCurve(event);
	                    iomObj.addattrobj(attrName, polyline);
	                    event=reader.nextEvent(); // <Curve>
	                    event=skipSpaces(event);
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
	                	IomObject subIomObj=createIomObject("MULTISURFACE", null);
	                    iomObj.addattrobj(attrName, readPolygon(event, subIomObj));
	                    if(!event.isStartElement()){
	                    	throw new IoxSyntaxException(event2msgtext(event));
	                    }
	                    event=reader.nextEvent(); // <Polygon>
	                    event=skipSpaces(event);
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
    		                    event=skipSpaces(event);
    		                    if(!event.isEndElement()){
    		                    	throw new IoxSyntaxException(event2msgtext(event));
    		                    }
    		                    event=reader.nextEvent(); // </structure>
    		                    event=skipSpaces(event);
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
                event=skipSpaces(event);
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

    private IomObject readCurve(XMLEvent event) throws IoxException, XMLStreamException {
        if(!event.asStartElement().getName().equals(QNAME_CURVE)){
        	throw new IoxSyntaxException(event2msgtext(event));
        }
        event=reader.nextEvent(); // <Curve>
        event=skipSpaces(event);
        if(!event.asStartElement().getName().equals(QNAME_SEGMENTS)){
        	throw new IoxSyntaxException(event2msgtext(event));
        }
        IomObject polylineObj=createIomObject("POLYLINE", null);
        if(!event.isStartElement()){
        	throw new IoxSyntaxException(event2msgtext(event));
        }
        event=reader.nextEvent(); // <segments>
        event=skipSpaces(event);
        QName interpolationType=new QName("interpolation");
        String interpolationValue="";
        // linestringsegment
        while(event.isStartElement()){
            StartElement element = (StartElement) event;
            // interpolationType
            if(element.getAttributeByName(interpolationType)!=null){
            	interpolationValue = element.getAttributeByName(interpolationType).getValue();
            }
            if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_LINEAR)){
            	if(!event.isStartElement()){
            		throw new IoxSyntaxException(event2msgtext(event));
                }
            	// straights
        		event=reader.nextEvent(); // <linear>
                event=skipSpaces(event);
            	polylineObj.addattrobj("sequence", prepareSegments(event));
            	if(!event.isStartElement()){
            		throw new IoxSyntaxException(event2msgtext(event));
                }
            	event=reader.nextEvent(); // <posList>
                event=skipSpaces(event);
//            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_GEODESIC)){
//            	srsDimension=srsDimensionValidation(srsDimension, event);
            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_CIRCULARARC3POINTS)){
                // arcs
                polylineObj.addattrobj("sequence", readArcSegment(event));
                if(!event.isStartElement()){
                	throw new IoxSyntaxException(event2msgtext(event));
                }
                event=reader.nextEvent(); // <segments>/<linestring>
                event=skipSpaces(event);
//            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_CIRCULARARC2POINTSWITHBULGE)){
//            	srsDimension=srsDimensionValidation(srsDimension, event);
//            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_CIRCULARARCCENTERPOINTWITHRADIUS)){
//            	srsDimension=srsDimensionValidation(srsDimension, event);
//            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_ELLIPTICAL)){
//            	srsDimension=srsDimensionValidation(srsDimension, event);
//            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_CLOTHOID)){
//            	srsDimension=srsDimensionValidation(srsDimension, event);
//            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_CONIC)){
//            	srsDimension=srsDimensionValidation(srsDimension, event);
//            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_POLYNOMIALSPLINE)){
//            	srsDimension=srsDimensionValidation(srsDimension, event);
//            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_CUBICSPLINE)){
//            	srsDimension=srsDimensionValidation(srsDimension, event);
//            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_RATIONALSPLINE)){
//            	srsDimension=srsDimensionValidation(srsDimension, event);
            }else{
            	throw new IoxSyntaxException("unsupported interpolation "+interpolationValue);
            }
            if(event.isEndElement()){
	            event=reader.nextEvent(); // </segments>
	            event=skipSpaces(event);
            }else{
            	// ignore event
            }
        }
        return polylineObj;
    }

	private IomObject prepareSegments(XMLEvent event) throws IoxException, XMLStreamException {
		IomObject segments=createIomObject("SEGMENTS", null);
        if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_POS)){
        	if(!event.isStartElement()){
        		throw new IoxSyntaxException(event2msgtext(event));
            } 
        	event=reader.nextEvent(); // <posList>
            if(event.isCharacters()){
	            Characters posData = (Characters) event;
                // dimension C1,C2,C3
                String[] linearSegments = posData.getData().split(" "); // will be trimmed in further code
                for(int i=0;i<linearSegments.length;i++){
                	ArrayList<String>segmentList=new ArrayList<String>();
                	if(linearSegments.length==1){
                		if((i+2)>linearSegments.length){
                			 throw new IoxSyntaxException("missing coord values for 1d Coord");
                		}
                		segmentList.add(linearSegments[i]);
            			IomObject linearSegment=readLineSegment(segmentList);
            			segments.addattrobj("segment", linearSegment);
                	}else if(linearSegments.length==2){
                		if((i+2)>linearSegments.length){
               			 	throw new IoxSyntaxException("missing coord values for 2d Coord");
                		}
                		segmentList.add(linearSegments[i]);
                		i=i+1;
                		segmentList.add(linearSegments[i]);
            			IomObject linearSegment=readLineSegment(segmentList);
            			segments.addattrobj("segment", linearSegment);
                	}else if(linearSegments.length==3){
                		if((i+3)>linearSegments.length){
               			 	throw new IoxSyntaxException("missing coord values for 3d Coord");
                		}
                		segmentList.add(linearSegments[i]);
                		i=i+1;
                		segmentList.add(linearSegments[i]);
                		i=i+1;
                		segmentList.add(linearSegments[i]);
            			IomObject linearSegment=readLineSegment(segmentList);
            			segments.addattrobj("segment", linearSegment);
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                    }
                }
                event=reader.nextEvent(); // <characters>
                if(!event.isEndElement()){
                	throw new IoxSyntaxException(event2msgtext(event));
                }
                event=reader.nextEvent(); // </posList>
                event=skipSpaces(event);
                return segments;
            }
        }
		return null;
	}

	private IomObject readArcSegment(XMLEvent event) throws IoxException, XMLStreamException{
		if(!event.isStartElement()){
			throw new IoxSyntaxException(event2msgtext(event));
        }
		event=reader.nextEvent(); // circular3PointArc
        event=skipSpaces(event);
        if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_POS)){ // posList
        	IomObject arcObj=createIomObject("ARC", null);
            event=reader.nextEvent();
            if(event.isCharacters()){
	            Characters posData = (Characters) event;
                // dimension C1,C2,C3,A1,A2
                String[] arcPositions = posData.getData().split(" "); // will be trimmed in further code
                if(arcPositions.length<4){
                    throw new IoxSyntaxException("missing arc pos values");
                }
                if(arcPositions.length>0){
                    arcObj.setattrvalue("A1", arcPositions[0]);
                }
                if(arcPositions.length>1){
                    arcObj.setattrvalue("A2", arcPositions[1]);
                }
                if(arcPositions.length>2){
                    arcObj.setattrvalue("C1", arcPositions[2]);
                }
                if(arcPositions.length>3){
                    arcObj.setattrvalue("C2", arcPositions[3]);
                }
                if(arcPositions.length>4){
                    arcObj.setattrvalue("C3", arcPositions[4]);
                }
                if(arcPositions.length>5){
                    throw new IoxSyntaxException("too many arc pos values");
                }
                event=reader.nextEvent();
                if(!event.isEndElement()){  // </pos>
                	throw new IoxSyntaxException(event2msgtext(event));
                }
                event=reader.nextEvent();
                event=skipSpaces(event);
                if(!event.isEndElement()){ // </Point>
                	throw new IoxSyntaxException(event2msgtext(event));
                }
                return arcObj;
            }else{
            	throw new IoxSyntaxException(event2msgtext(event));
            }
        }else if(event.isEndElement()){
            throw new IoxSyntaxException("missing arc pos element");
        }else{
        	throw new IoxSyntaxException(event2msgtext(event));
        }
	}

	private IomObject readLineSegment(ArrayList<String> coords) throws IoxException{
		IomObject coordObj=createIomObject("COORD", null);
		for(String coord:coords){
			coord=coord.trim();
		}
		if(coords.size()<1){
            throw new IoxSyntaxException("missing coord pos values");
        }
        if(coords.size()>0){
        	coordObj.setattrvalue("C1", coords.get(0));
        }
        if(coords.size()>1){
        	coordObj.setattrvalue("C2", coords.get(1));
        }
        if(coords.size()>2){
        	coordObj.setattrvalue("C3", coords.get(2));
        }
        if(coords.size()>5){
        	throw new IoxSyntaxException("too many coord pos values");
        }
		return coordObj;
	}

	private IomObject readPolygon(XMLEvent event, IomObject iomObj) throws IoxException, XMLStreamException { // attrName, MULTISIRFACE
    	IomObject boundaryObj=createIomObject("BOUNDARY", null);
    	IomObject surfaceObj=createIomObject("SURFACE", null);
    	IomObject polygonObj=createIomObject("MULTISURFACE", null);
    	if(!event.isStartElement()){
    		throw new IoxSyntaxException(event2msgtext(event));
        }
    	event=reader.nextEvent(); // <polygon>
        event=skipSpaces(event);
        if(!event.isStartElement()){
        	throw new IoxSyntaxException(event2msgtext(event));
        }
        int exteriorExist=0; // first boundary has to be an outerBoundary (exterior)
        while(event.isStartElement() && (event.asStartElement().getName().equals(QNAME_EXTERIOR) || event.asStartElement().getName().equals(QNAME_INTERIOR))){
        	if(event.asStartElement().getName().equals(QNAME_EXTERIOR)){
        		exteriorExist=exteriorExist+1;
        	}else if(event.asStartElement().getName().equals(QNAME_INTERIOR)){
        		if(exteriorExist==0){
            		throw new IoxSyntaxException("no exterior ring found");
            	}
        	}
        	if(!event.isStartElement()){
        		throw new IoxSyntaxException(event2msgtext(event));
            }
        	event=reader.nextEvent(); // <exterior/interior>
            event=skipSpaces(event);
            
        	boundaryObj=readBoundary(event, surfaceObj);
        	surfaceObj.addattrobj("boundary", boundaryObj);
        	if(!event.isStartElement()){
        		throw new IoxSyntaxException(event2msgtext(event));
            }
        	event=reader.nextEvent(); // <Ring>
        	event=skipSpaces(event);
        }
        if(exteriorExist==0){
        	throw new IoxSyntaxException("no lines found in polygon of "+iomObj.getobjecttag());
        }
        polygonObj.addattrobj("surface", surfaceObj);
        return polygonObj;
    }
	
    private IomObject readBoundary(XMLEvent event, IomObject surfaceObj) throws IoxException, XMLStreamException {
        IomObject boundaryObj=createIomObject("BOUNDARY", null);
        if(!event.asStartElement().getName().equals(QNAME_RING)){
        	throw new IoxSyntaxException(event2msgtext(event));
        }
        event=reader.nextEvent(); // <Ring>
        event=skipSpaces(event);
        while(event.isStartElement() && event.asStartElement().getName().equals(QNAME_CURVEMEMBER)){
        	if(!event.isStartElement()){
        		throw new IoxSyntaxException(event2msgtext(event));
            }
        	event=reader.nextEvent(); // <curveMember>
            event=skipSpaces(event);
        	boundaryObj.addattrobj("polyline", readCurve(event));
        	if(!event.isStartElement()){
        		throw new IoxSyntaxException(event2msgtext(event));
            }
        	event=reader.nextEvent(); // <Curve>
            event=skipSpaces(event);
        }
        if(!event.isEndElement()){
        	throw new IoxSyntaxException(event2msgtext(event));
        }
        event=reader.nextEvent(); // </curveMember>
        event=skipSpaces(event);
        if(!event.isEndElement()){
        	throw new IoxSyntaxException(event2msgtext(event));
        }
        event=reader.nextEvent(); // </Ring>
        event=skipSpaces(event);
		return boundaryObj;
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