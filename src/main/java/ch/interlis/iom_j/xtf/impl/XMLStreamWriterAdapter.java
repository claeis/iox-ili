package ch.interlis.iom_j.xtf.impl;

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.*;

public class XMLStreamWriterAdapter {
	private XMLStreamWriterAdapter(){}
	public static void add(XMLStreamWriter streamWriter, XMLEvent event) throws XMLStreamException {
        int type = event.getEventType();
        switch(type){
            case XMLEvent.DTD:{
                DTD dtd = (DTD)event ;
                streamWriter.writeDTD(dtd.getDocumentTypeDeclaration());
                break;
            }
            case XMLEvent.START_DOCUMENT :{
                StartDocument startDocument = (StartDocument)event ;
                streamWriter.writeStartDocument(startDocument.getCharacterEncodingScheme(), startDocument.getVersion());
                break;
            }
            case XMLEvent.START_ELEMENT :{
                StartElement startElement = event.asStartElement() ;
                QName qname = startElement.getName();
                streamWriter.writeStartElement(qname.getPrefix(), qname.getLocalPart(), qname.getNamespaceURI());
                
                Iterator iterator = startElement.getNamespaces();
                while(iterator.hasNext()){
                    Namespace namespace = (Namespace)iterator.next();
                    streamWriter.writeNamespace(namespace.getPrefix(), namespace.getNamespaceURI());
                }

                Iterator attributes = startElement.getAttributes();
                while(attributes.hasNext()){
                    Attribute attribute = (Attribute)attributes.next();
                    QName name = attribute.getName();
                    streamWriter.writeAttribute(name.getPrefix(), name.getNamespaceURI(), 
                                                name.getLocalPart(),attribute.getValue());
                }
                break;
            }
            case XMLEvent.NAMESPACE:{
                Namespace namespace = (Namespace)event;
                streamWriter.writeNamespace(namespace.getPrefix(), namespace.getNamespaceURI());
                break ;
            }
            case XMLEvent.COMMENT: {
                Comment comment = (Comment)event ;
                streamWriter.writeComment(comment.getText());
                break;
            }
            case XMLEvent.PROCESSING_INSTRUCTION:{
                ProcessingInstruction processingInstruction = (ProcessingInstruction)event ;
                streamWriter.writeProcessingInstruction(processingInstruction.getTarget(), processingInstruction.getData());
                break;
            }
            case XMLEvent.CHARACTERS:{
                Characters characters = event.asCharacters();
                //check if the CHARACTERS are CDATA
                if(characters.isCData()){
                    streamWriter.writeCData(characters.getData());
                }
                else{
                    streamWriter.writeCharacters(characters.getData());
                }
                break;
            }
            case XMLEvent.ENTITY_REFERENCE:{
                EntityReference entityReference = (EntityReference)event ;
                streamWriter.writeEntityRef(entityReference.getName());
                break;
            }
            case XMLEvent.ATTRIBUTE:{
                Attribute attribute = (Attribute)event;
                QName qname = attribute.getName();
                streamWriter.writeAttribute(qname.getPrefix(), qname.getNamespaceURI(), qname.getLocalPart(),attribute.getValue());
                break;
            }
            case XMLEvent.CDATA:{
                Characters characters = (Characters)event;
                if(characters.isCData()){
                    streamWriter.writeCData(characters.getData());
                }
                break;
            }
            
            case XMLEvent.END_ELEMENT:{
                streamWriter.writeEndElement();
                break;
            }
            case XMLEvent.END_DOCUMENT:{
                streamWriter.writeEndDocument();
                break;
            }
            default:
            	throw new XMLStreamException("Unknown Event type = " + type);
        };
        
    }


}
