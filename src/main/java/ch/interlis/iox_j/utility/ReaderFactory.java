package ch.interlis.iox_j.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
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
import ch.interlis.iox_j.validator.InterlisFunction;

public class ReaderFactory{
    public static final String CONFIG_CUSTOM_READERS="ch.interlis.iox_j.utility.customReaders";
    @Deprecated
    public IoxReader createReader(File inputFile, LogEventFactory errFact) throws IoxException {
        return createReader(inputFile,errFact,null);
    }
	public IoxReader createReader(File inputFile, LogEventFactory errFact,Settings settings) throws IoxException {
	    if(settings==null) {
	        settings=new Settings();
	    }
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
	    List<Class> customReaders=(List<Class>)settings.getTransientObject(CONFIG_CUSTOM_READERS);
	    if(customReaders!=null) {
	        for(Class customReaderClass:customReaders) {
	            try {
	                Constructor<? extends IoxReader> cnstr=(Constructor<? extends IoxReader>) getConstructorForArgs(customReaderClass, new Class[]{ File.class,LogEventFactory.class,Settings.class });
	                if(cnstr==null) {
	                    // ignore
	                    EhiLogger.logAdaption(customReaderClass.getName()+" ignored; no matching constructor");
	                    continue;
	                }
	                reader=cnstr.newInstance(inputFile,errFact,settings);
	            } catch (InstantiationException e) {
	                throw new IoxException("failed to create reader "+customReaderClass.getName(),e);
	            } catch (IllegalAccessException e) {
	                throw new IoxException("failed to create reader "+customReaderClass.getName(),e);
	            } catch (IllegalArgumentException e) {
	                throw new IoxException("failed to create reader "+customReaderClass.getName(),e);
	            } catch (InvocationTargetException e) {
	                if(e.getTargetException() instanceof IoxException) {
	                    // ignore
	                }else {
	                    throw new IoxException("failed to create reader "+customReaderClass.getName(),e.getTargetException());
	                }
	            }
	            return reader;
	        }
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
	public Constructor<?> getConstructorForArgs(Class<?> klass, Class[] args)
    {
        //Get all the constructors from given class
        Constructor<?>[] constructors = klass.getConstructors();

        for(Constructor<?> constructor : constructors)
        {
            //Walk through all the constructors, matching parameter amount and parameter types with given types (args)
            Class<?>[] types = constructor.getParameterTypes();
            if(types.length == args.length)
            {               
                boolean argumentsMatch = true;
                for(int i = 0; i < args.length; i++)
                {
                    //Note that the types in args must be in same order as in the constructor if the checking is done this way
                    if(!types[i].isAssignableFrom(args[i]))
                    {
                        argumentsMatch = false;
                        break;
                    }
                }

                if(argumentsMatch)
                {
                    //We found a matching constructor, return it
                    return constructor;
                }
            }
        }

        //No matching constructor
        return null;
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