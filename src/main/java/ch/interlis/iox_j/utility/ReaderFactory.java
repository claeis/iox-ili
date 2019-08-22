package ch.interlis.iox_j.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import ch.interlis.iom_j.csv.CsvReader;
import ch.interlis.iom_j.iligml.Iligml20Reader;
import ch.interlis.iom_j.itf.ItfReader2;
import ch.interlis.iom_j.xtf.Xtf23Reader;
import ch.interlis.iom_j.xtf.Xtf24Reader;
import ch.interlis.iom_j.xtf.XtfReader;
import ch.interlis.iom_j.xtf.impl.XtfWriterAlt;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxReader;
import ch.interlis.iox_j.logging.LogEventFactory;

public class ReaderFactory{
	
	public IoxReader createReader(File inputFile, LogEventFactory errFact) throws IoxException {
		IoxReader reader=null;
		reader=new ItfReader2(inputFile, null, false);
		IoxEvent event=null;
		try{
			event=reader.read();
			if(event!=null){
				reader=new ItfReader2(inputFile, null, false);
				return reader;
			}
		}catch(IoxException ex){
		}
		
		
		String ns=getXMLNamespace(inputFile);
		if(ns!=null) {
		    if(ns.equals(XtfReader.XMLNS_XTF22) || ns.equals(XtfReader.XMLNS_XTF23)) {
	            reader=new XtfReader(inputFile);
                return reader;
		    }else if(ns.equals(Xtf24Reader.XMLNS_XTF24)) {
	            reader=new Xtf24Reader(inputFile);
                return reader;
            }else if(ns.equals(Iligml20Reader.XMLNS_ILIGML)) {
                reader=new Iligml20Reader(inputFile);
                return reader;
		    }
	        throw new IoxException("unknown xml file");
		}
		
		if(IoxUtility.isCsvFilename(inputFile.getName())) {
	        reader=new CsvReader(inputFile);
	        IoxEvent event5=null;
	        try{
	            event5=reader.read();
	            if(event5!=null){
	                reader=new CsvReader(inputFile);
	                return reader;
	            }
	        }catch(IoxException ex){
	        }
		}
		
		// no appropriate reader found
		throw new IoxException("no reader found");
	}

    public String getXMLNamespace(File inputFile) {
        javax.xml.stream.XMLInputFactory inputFactory = javax.xml.stream.XMLInputFactory.newInstance();
        XMLEventReader reader=null;
        java.io.FileInputStream in=null;
        javax.xml.stream.events.XMLEvent event=null;
        try {
            in=new java.io.FileInputStream(inputFile);
            reader = inputFactory.createXMLEventReader(in);
            event=reader.nextEvent();
            while(event!=null && !event.isStartElement()){
                event=reader.nextEvent();
            }
        } catch (XMLStreamException e) {
            return null;
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
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
            return ns;
        }
        return null;
    }
}