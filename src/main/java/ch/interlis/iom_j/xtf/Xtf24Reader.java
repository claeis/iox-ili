package ch.interlis.iom_j.xtf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.generator.Iligml20Generator;
import ch.interlis.ili2c.metamodel.DataModel;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.ili2c.metamodel.ViewableTransferElement;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iox.IoxReader;
import ch.interlis.iox_j.IoxSyntaxException;

public class Xtf24Reader implements IoxReader {
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
    private static final int END_BASKET=19;
    private static final int END_OBJECT=20;
    private static final int INSIDE_CLASS=21;
    private static final int END_MEMBER=22;
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
//				System.out.println(event.toString());
//				EhiLogger.debug(event.toString());
			}catch(javax.xml.stream.XMLStreamException ex){
				throw new IoxException(ex);
			}
			// start
			if(state==START){
				if(event.isStartDocument()){
					state=START_DOCUMENT;
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
							iomObj=readObject(event, viewable, iomObj, srsDimension);
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
                return new ch.interlis.iox_j.EndTransferEvent();
            }
		}
		return null;
	}
	
	private IomObject readObject(XMLEvent event, Viewable viewable, IomObject iomObj, Integer srsDimension) throws IoxException, XMLStreamException {
		return iomObj;
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
}