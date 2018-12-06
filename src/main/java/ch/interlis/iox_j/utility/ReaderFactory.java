package ch.interlis.iox_j.utility;

import java.io.File;
import ch.interlis.iom_j.csv.CsvReader;
import ch.interlis.iom_j.iligml.Iligml20Reader;
import ch.interlis.iom_j.itf.ItfReader2;
import ch.interlis.iom_j.xtf.Xtf23Reader;
import ch.interlis.iom_j.xtf.Xtf24Reader;
import ch.interlis.iom_j.xtf.XtfReader;
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
		
        reader=new Xtf23Reader(inputFile);
        IoxEvent event2=null;
		try{
			event2=reader.read();
			if(event2!=null){
		        reader=new Xtf23Reader(inputFile);
				return reader;
			}
		}catch(IoxException ex){
		}
		
        reader=new Xtf24Reader(inputFile);
        IoxEvent event3=null;
		try{
			event3=reader.read();
			if(event3!=null){
		        reader=new Xtf24Reader(inputFile);
				return reader;
			}
		}catch(IoxException ex){
		}
		
        reader=new Iligml20Reader(inputFile);
        IoxEvent event4=null;
		try{
			event4=reader.read();
			if(event4!=null){
		        reader=new Iligml20Reader(inputFile);
				return reader;
			}
		}catch(IoxException ex){
		}
		
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
		
		// no appropriate reader found
		throw new IoxException("no reader found");
	}
}