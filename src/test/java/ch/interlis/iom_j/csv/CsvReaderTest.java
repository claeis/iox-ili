package ch.interlis.iom_j.csv;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import org.junit.Test;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;

public class CsvReaderTest {

	private final static String TEST_IN="src/test/data/CsvReader";
	private TransferDescription td=null;
	
	//@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"/model.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	// Es wird getestet ob die Attributenamen einzigartig sind und innerhalb vom Objekt richtig hinzugefuegt wurden.
	@Test
	public void CSV_AttrNames_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"TextType.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
			IomObject iomObj=((ObjectEvent)event).getIomObject();
			int attr1=iomObj.getattrvaluecount("attr1");
			int attr2=iomObj.getattrvaluecount("attr2");
			int attr3=iomObj.getattrvaluecount("attr3");
			assertTrue(attr1==1);
			assertTrue(attr2==1);
			assertTrue(attr3==1);
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob der Name des Models(Name der CSV Datei), der Topicname und der Klassenname richtig erstellt werden.
	@Test
    public void CSV_ObjectName_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"TextType.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			assertTrue(iomObj!=null);
			assertTrue(iomObj.getobjecttag().equals("TextType.Topic.Class1"));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob die Objekt ID richtig ausgegeben wird.
	@Test
    public void CSV_ObjectOid_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"TextType.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			assertTrue(iomObj!=null);
			assertTrue(iomObj.getobjectoid().equals("o2"));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet die Anzahl der Attribute innerhalb des IomObjektes mit der Anzahl der Attribute innerhalb des CSV File uebereinstimmen.
	@Test
    public void CSV_AttrCount_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"TextType.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			assertTrue(iomObj!=null);
			assertTrue(iomObj.getattrcount()==3);
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob die Attribute des IomObjektes die richtigen Werte haben.
	@Test
    public void CSV_AttrValues_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"TextType.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			// attr1
			String attrValue=iomObj.getattrvalue("attr1");
			assertEquals(attrValue,"10");
			// attr2
			String attrValue2=iomObj.getattrvalue("attr2");
			assertEquals(attrValue2,"AU");
			// attr3
			String attrValue3=iomObj.getattrvalue("attr3");
			assertEquals(attrValue3,"Australia");
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob mehrere Records mit Anfuehrungszeichen erstellt werden koennen.
	@Test
    public void CSV_NewLineWithDelimiters_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"NewLineCRLF.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			// attr1
			String attrValue=iomObj.getattrvalue("attr1");
			assertEquals(attrValue,"10");
			// attr2
			String attrValue2=iomObj.getattrvalue("attr2");
			assertEquals(attrValue2,"AU");
			// attr3
			String attrValue3=iomObj.getattrvalue("attr3");
			assertEquals(attrValue3,"Australia");
		}
		event = reader.read();
		if(event instanceof ObjectEvent){
	        IomObject iomObj2=((ObjectEvent)event).getIomObject();
			// attr4
			String attrValue4=iomObj2.getattrvalue("attr1");
			assertEquals(attrValue4,"11");
			// attr5
			String attrValue5=iomObj2.getattrvalue("attr2");
			assertEquals(attrValue5,"BU");
			// attr6
			String attrValue6=iomObj2.getattrvalue("attr3");
			assertEquals(attrValue6,"Bustralia");
		}
		event = reader.read();
		if(event instanceof ObjectEvent){
	        IomObject iomObj3=((ObjectEvent)event).getIomObject();
			// attr7
			String attrValue7=iomObj3.getattrvalue("attr1");
			assertEquals(attrValue7,"12");
			// attr8
			String attrValue8=iomObj3.getattrvalue("attr2");
			assertEquals(attrValue8,"CU");
			// attr9
			String attrValue9=iomObj3.getattrvalue("attr3");
			assertEquals(attrValue9,"Custralia");
		}
		event = reader.read();
		if(event instanceof ObjectEvent){
	        IomObject iomObj4=((ObjectEvent)event).getIomObject();
			// attr10
			String attrValue10=iomObj4.getattrvalue("attr1");
			assertEquals(attrValue10,"13");
			// attr11
			String attrValue11=iomObj4.getattrvalue("attr2");
			assertEquals(attrValue11,"DU");
			// attr12
			String attrValue12=iomObj4.getattrvalue("attr3");
			assertEquals(attrValue12,"Dustralia");
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob mehrere Records ohne Anfuehrungszeichen erstellt werden koennen.
	@Test
    public void CSV_NewLineWithoutDelimiters_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"NewLineReturn.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	// attr1
			String attrValue=iomObj.getattrvalue("attr1");
			assertEquals(attrValue,"10");
			// attr2
			String attrValue2=iomObj.getattrvalue("attr2");
			assertEquals(attrValue2,"AU");
			// attr3
			String attrValue3=iomObj.getattrvalue("attr3");
			assertEquals(attrValue3,"Australia");
		}
		event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj2=((ObjectEvent)event).getIomObject();
			// attr4
			String attrValue4=iomObj2.getattrvalue("attr1");
			assertEquals(attrValue4,"11");
			// attr5
			String attrValue5=iomObj2.getattrvalue("attr2");
			assertEquals(attrValue5,"BU");
			// attr6
			String attrValue6=iomObj2.getattrvalue("attr3");
			assertEquals(attrValue6,"Bustralia");
		}
		event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj3=((ObjectEvent)event).getIomObject();
			// attr7
			String attrValue7=iomObj3.getattrvalue("attr1");
			assertEquals(attrValue7,"12");
			// attr8
			String attrValue8=iomObj3.getattrvalue("attr2");
			assertEquals(attrValue8,"CU");
			// attr9
			String attrValue9=iomObj3.getattrvalue("attr3");
			assertEquals(attrValue9,"Custralia");
		}
		event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj4=((ObjectEvent)event).getIomObject();
			// attr10
			String attrValue10=iomObj4.getattrvalue("attr1");
			assertEquals(attrValue10,"13");
			// attr11
			String attrValue11=iomObj4.getattrvalue("attr2");
			assertEquals(attrValue11,"DU");
			// attr12
			String attrValue12=iomObj4.getattrvalue("attr3");
			assertEquals(attrValue12,"Dustralia");
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Attributewert mit Komma, Anfuehrungszeichen und einer neuen Zeile erstellt werden kann.
	@Test
    public void CSV_RecordIncludesLineReturn_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"RecordWithLineReturn.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	// attr1
			String attrValue=iomObj.getattrvalue("attr1");
			assertEquals(attrValue,"10");
			// attr2
			String attrValue2=iomObj.getattrvalue("attr2");
			assertEquals(attrValue2,"AU");
			// attr3
			String attrValue3=iomObj.getattrvalue("attr3");
			assertEquals(attrValue3,"A,u\"s"+"\r\n"+"tralia");
		}
		event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj2=((ObjectEvent)event).getIomObject();
			// attr4
			String attrValue4=iomObj2.getattrvalue("attr1");
			assertEquals(attrValue4,"11");
			// attr5
			String attrValue5=iomObj2.getattrvalue("attr2");
			assertEquals(attrValue5,"\\r");
			// attr6
			String attrValue6=iomObj2.getattrvalue("attr3");
			assertEquals(attrValue6,"\\n");
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Der Benutzer setzt der Headerparameter: present. Somit kein Header erstellt und muss ignoriert werden.
	@Test
    public void CSV_HeaderPresent_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"HeaderPresent.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		reader.setHeader("present");
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			// attr1
			String attrValue=iomObj.getattrvalue("attr1");
			assertEquals(attrValue,"10");
			// attr2
			String attrValue2=iomObj.getattrvalue("attr2");
			assertEquals(attrValue2,"AU");
			// attr3
			String attrValue3=iomObj.getattrvalue("attr3");
			assertEquals(attrValue3,"Australia");
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Der Benutzer setzt der Headerparameter: absent. Somit wird kein Header erstellt.
	@Test
    public void CSV_HeaderAbsent_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"HeaderAbsent.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		reader.setHeader("absent");
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			// attr1
			String attrValue=iomObj.getattrvalue("attr1");
			assertEquals(attrValue,"10");
			// attr2
			String attrValue2=iomObj.getattrvalue("attr2");
			assertEquals(attrValue2,"AU");
			// attr3
			String attrValue3=iomObj.getattrvalue("attr3");
			assertEquals(attrValue3,"Australia");
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Der Benutzer setzt nur die Record-Delimiter nach seinem Ermessen.
	@Test
    public void CSV_SetOwnRecordDelimiter_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"RecordDelimiter.csv"));
		reader.setRecordDelimiter("?");
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			// attr1
			String attrValue=iomObj.getattrvalue("attr1");
			assertEquals(attrValue,"10");
			// attr2
			String attrValue2=iomObj.getattrvalue("attr2");
			assertEquals(attrValue2,"AU");
			// attr3
			String attrValue3=iomObj.getattrvalue("attr3");
			assertEquals(attrValue3,"Australia");
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Der Benutzer setzt nur die Delimiter nach seinem Ermessen.
	@Test
    public void CSV_SetOwnDelimiter_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"Delimiter.csv"));
		CsvReader.setDelimiter("%");
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			// attr1
			String attrValue=iomObj.getattrvalue("attr1");
			assertEquals(attrValue,"10");
			// attr2
			String attrValue2=iomObj.getattrvalue("attr2");
			assertEquals(attrValue2,"AU");
			// attr3
			String attrValue3=iomObj.getattrvalue("attr3");
			assertEquals(attrValue3,"Australia");
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Der Benutzer setzt die Record-Delimiter und die Delimiter nach seinem Ermessen.
	@Test
    public void CSV_SetOwnRecordDelimiterAndDelimiter_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"RecordDelimiterAndDelimiter.csv"));
		reader.setRecordDelimiter("&");
		CsvReader.setDelimiter("%");
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			// attr1
			String attrValue=iomObj.getattrvalue("attr1");
			assertEquals(attrValue,"10");
			// attr2
			String attrValue2=iomObj.getattrvalue("attr2");
			assertEquals(attrValue2,"AU");
			// attr3
			String attrValue3=iomObj.getattrvalue("attr3");
			assertEquals(attrValue3,"Australia");
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}

    // Es wird getestet ob mehrere Anfuehrungszeichen innerhalb der Attributwerte zulaessig sind.
    @Test
    public void CSV_SerevalDoubleQuotes_Ok() throws FileNotFoundException, IoxException {
    	CsvReader reader=new CsvReader(new File(TEST_IN,"TextWithSerevalDoubleQuotes.csv"));
    	assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			// attr1
			String attrValue=iomObj.getattrvalue("attr1");
			assertEquals(attrValue,"12");
			// attr2
			String attrValue2=iomObj.getattrvalue("attr2");
			assertEquals(attrValue2,"AU");
			// attr3
			String attrValue3=iomObj.getattrvalue("attr3");
			assertEquals(attrValue3,"Aust\"\"ralia");
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
    }

    // Es wird getestet ob Anfuehrungszeichen innerhalb der Attributwerte zulaessig sind.
    @Test
    public void CSV_DoubleQuotesInValue_Ok() throws FileNotFoundException, IoxException {
    	CsvReader reader=new CsvReader(new File(TEST_IN,"TextWithDoubleQuotesInColumn.csv"));
    	assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			// attr1
			String attrValue=iomObj.getattrvalue("attr1");
			assertEquals(attrValue,"13");
			// attr2
			String attrValue2=iomObj.getattrvalue("attr2");
			assertEquals(attrValue2,"AU");
			// attr3
			String attrValue3=iomObj.getattrvalue("attr3");
			assertEquals(attrValue3,"Aus\"tralia");
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
    }

    // Es wird getestet ob Anfuehrungszeichen und Kommas innerhalb der Attributwerte zulaessig sind.
    @Test
    public void CSV_DoubleQuotesAndCommaInValue_Ok() throws FileNotFoundException, IoxException {
    	CsvReader reader=new CsvReader(new File(TEST_IN,"TextWithDoubleQuotesAndCommaInColumn.csv"));
    	assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			// attr1
			String attrValue=iomObj.getattrvalue("attr1");
			assertEquals(attrValue,"14");
			// attr2
			String attrValue2=iomObj.getattrvalue("attr2");
			assertEquals(attrValue2,"AU");
			// attr3
			String attrValue3=iomObj.getattrvalue("attr3");
			assertEquals(attrValue3,"Aus,trali\"a");
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
    }
    
    // Es wird getestet ob Anfuehrungszeichen ausserhalb und innerhalb der Attributwerte zulaessig sind.
    @Test
    public void CSV_DoubleQuotes_Ok() throws Exception{
    	CsvReader reader=new CsvReader(new File(TEST_IN,"TextWithDoubleQuotes.csv"));
    	assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
    	IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			// attr1
			String attrValue=iomObj.getattrvalue("attr1");
			assertEquals(attrValue,"11");
			// attr2
			String attrValue2=iomObj.getattrvalue("attr2");
			assertEquals(attrValue2,"AU");
			// attr3
			String attrValue3=iomObj.getattrvalue("attr3");
			assertEquals(attrValue3,"Aus\"tralia");
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
    }
    
    // Der Benutzer setzt einen Parameter im Header, welcher ungueltig ist.
    @Test
    public void CSV_HeaderDefinitionNotValid_Fail() throws IoxException, FileNotFoundException{
    	CsvReader reader=new CsvReader(new File(TEST_IN,"HeaderPresent.csv"));
    	reader.setHeader("started");
    	assertTrue(reader.read() instanceof StartTransferEvent);
    	try{
    		reader.read();
    		fail();
    	}catch(IoxException ex){
    		ex.getMessage().contains("expected present or absent, unexpected started");
    	}
    	reader.close();
    	reader=null;
    }
}