package ch.interlis.iom_j.csv;

import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;

import ch.ehi.basics.settings.Settings;
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

	private static final String MODEL3_ATTR_STATE = "state";
	private static final String MODEL3_ATTR_ABBREVIATION = "abbreviation";
	private static final String MODEL3_ATTR_ID = "id";
	private TransferDescription td=null;
    private static final String TEST_OUT="build";
	private static final String TEST_IN="src/test/data/CsvReader";
	private static final String ATTRIBUTE1="attr1";
	private static final String ATTRIBUTE2="attr2";
	private static final String ATTRIBUTE3="attr3";
	private static final String ATTRIBUTE4="attr4";
	private static final String ATTRIBUTE5="attr5";
	
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"/model3.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	// Es wird getestet ob die Attributenamen einzigartig sind und innerhalb vom Objekt richtig hinzugefuegt wurden.
	@Test
	public void attrNames_Ok() throws IoxException, FileNotFoundException, Ili2cFailure{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"/model6.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td2=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		
		CsvReader reader=new CsvReader(new File(TEST_IN,"TextType2.csv"));
		reader.setModel(td2);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
			IomObject iomObj=((ObjectEvent)event).getIomObject();
			assertTrue(iomObj.getattrvaluecount(ATTRIBUTE1)==1);
			assertTrue(iomObj.getattrvaluecount(ATTRIBUTE2)==1);
			assertTrue(iomObj.getattrvaluecount(ATTRIBUTE3)==1);
			assertTrue(iomObj.getattrvaluecount("attr4")==1);
			assertTrue(iomObj.getattrvaluecount("attr5")==1);
			assertTrue(iomObj.getattrvaluecount("attr6")==1);
			assertTrue(iomObj.getattrvaluecount("attr7")==1);
			assertTrue(iomObj.getattrvaluecount("attr8")==1);
			assertTrue(iomObj.getattrvaluecount("attr9")==1);
			assertTrue(iomObj.getattrvaluecount("attr10")==1);
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob der Name des Models (Der Name des CSV Files) stimmt.
	// Es wird getestet ob der Name des Topics (Per Default: Topic) stimmt.
	// Es wird getestet ob der Name der Klasse (Class + count=1) stimmt.
	@Test
	public void modelName_TopicName_ClassName_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"TextType.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
			IomObject iomObj=((ObjectEvent)event).getIomObject();
			assertTrue(iomObj.getobjecttag().contains("TextType"));
			assertTrue(iomObj.getobjecttag().contains("Topic"));
			assertTrue(iomObj.getobjecttag().contains("Class1"));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}

	// Der Benutzer setzt ein Model.
	// Es wird getestet ob der Name des Models (Name des Models: model.ili) stimmt.
	// Es wird getestet ob der Name des Topics (Topic: Topic12) stimmt.
	// Es wird getestet ob der Name der Klasse (Class: Class12) stimmt.
	@Test
	public void modelName_TopicName_ClassName_SetModel_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"TextType.csv"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
			IomObject iomObj=((ObjectEvent)event).getIomObject();
			assertEquals("model3.Topic12.Class1",iomObj.getobjecttag());
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Der Benutzer gibt 3 models an.
	// Es wird getestet ob der Name des Models (Name des Models: BundesModel.ili) stimmt.
	// Es wird getestet ob der Name des Topics (Topic: Topic) stimmt.
	// Es wird getestet ob der Name der Klasse (Class: Class1) stimmt.
	@Test
    public void multipleFiles_ModelName_TopicName_ClassName_SetModel__Ok() throws IoxException, FileNotFoundException, Ili2cFailure{
		// ili-datei lesen
		TransferDescription tdM=null;
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntryConditionClass=new FileEntry(TEST_IN+"/model3.ili", FileEntryKind.ILIMODELFILE); // first input model
		ili2cConfig.addFileEntry(fileEntryConditionClass);
		FileEntry fileEntry=new FileEntry(TEST_IN+"/model5.ili", FileEntryKind.ILIMODELFILE); // second input model
		ili2cConfig.addFileEntry(fileEntry);
		FileEntry fileEntry2=new FileEntry(TEST_IN+"/BundesModel.ili", FileEntryKind.ILIMODELFILE); // third input model
		ili2cConfig.addFileEntry(fileEntry2);
		tdM=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(tdM);
		
 		CsvReader reader=new CsvReader(new File(TEST_IN,"TextType3.csv"));
		reader.setModel(tdM);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertTrue(iomObj.getobjecttag().contains("BundesModel"));
        	assertTrue(iomObj.getobjecttag().contains("Topic"));
        	assertTrue(iomObj.getobjecttag().contains("Class1"));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob der Name des Models(Name der CSV Datei), der Topicname und der Klassenname richtig erstellt werden.
	@Test
    public void objectName_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"TextType.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			assertEquals("TextType.Topic.Class1",iomObj.getobjecttag());
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob die Basket ID richtig ausgegeben wird.
	@Test
    public void basketId_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"TextType.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		IoxEvent event = reader.read();
		if(event instanceof StartBasketEvent){
			StartBasketEvent basketEvent = (StartBasketEvent) event;
			assertEquals("b1", basketEvent.getBid());
		}
		assertTrue(reader.read() instanceof ObjectEvent);
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob die Objekt ID richtig ausgegeben wird.
	@Test
    public void objectOid_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"TextType.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			assertTrue(iomObj.getobjectoid().equals("o2"));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet die Anzahl der Attribute innerhalb des IomObjektes mit der Anzahl der Attribute innerhalb des CSV File uebereinstimmen.
	@Test
    public void attrCount_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"TextType.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			assertTrue(iomObj.getattrcount()==3);
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob die Attribute des IomObjektes die richtigen Werte haben.
	@Test
    public void attrValues_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"TextType.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("AU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Australia", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	// Es wird getestet ob die Datei mit dem gegebenen Character Encoding gelesen wird
	@Test
    public void encoding_Ok() throws IoxException, FileNotFoundException{
		Settings settings=new Settings();
		settings.setValue(CsvReader.ENCODING, "UTF-8");
		CsvReader reader=new CsvReader(new File(TEST_IN,"TextTypeUTF8.csv"),settings);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("AU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("\u0402\u00A2", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
    // Es wird getestet ob das BOM bei UTF-8 encoding ueberlesen wird
    @Test
    public void utf8_bom_Ok() throws IoxException, IOException{
        final String FILE_NAME="TextTypeUTF8_BOM.csv";
        // write file
        BufferedOutputStream writer=new BufferedOutputStream(new FileOutputStream(new File(TEST_OUT,FILE_NAME)));
        writer.write(0xEF);
        writer.write(0xBB);
        writer.write(0xBF);
        writer.write('\"');
        writer.write('1');
        writer.write('0');
        writer.write('\"');
        writer.write(',');
        writer.write('\"');
        writer.write('A');
        writer.write('U');
        writer.write('\"');
        writer.write('\n');
        writer.close();
        writer=null;
        
        Settings settings=new Settings();
        settings.setValue(CsvReader.ENCODING, "UTF-8");
        CsvReader reader=new CsvReader(new File(TEST_OUT,FILE_NAME),settings);
        assertTrue(reader.read() instanceof StartTransferEvent);
        assertTrue(reader.read() instanceof StartBasketEvent);
        IoxEvent event=reader.read();
        if(event instanceof ObjectEvent){
            IomObject iomObj=((ObjectEvent)event).getIomObject();
            assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
            assertEquals("AU", iomObj.getattrvalue(ATTRIBUTE2));
        }
        assertTrue(reader.read() instanceof EndBasketEvent);
        assertTrue(reader.read() instanceof EndTransferEvent);
        reader.close();
        reader=null;
    }
	// Es wird getestet ob ein fehlender Wert als UNDEFINED gelesen wird
	@Test
    public void attrValueUndefined_Ok() throws IoxException, FileNotFoundException{
		Settings settings=new Settings();
		CsvReader reader=new CsvReader(new File(TEST_IN,"TextTypeUndefined.csv"),settings);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("AU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals(null, iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob mehrere Records mit Anfuehrungszeichen erstellt werden koennen.
	@Test
    public void newLineWithDelimiters_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"NewLineCRLF.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("AU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Australia", iomObj.getattrvalue(ATTRIBUTE3));
		}
		event = reader.read();
		if(event instanceof ObjectEvent){
	        IomObject iomObj=((ObjectEvent)event).getIomObject();
	        assertEquals("11", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("BU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Bustralia", iomObj.getattrvalue(ATTRIBUTE3));
		}
		event = reader.read();
		if(event instanceof ObjectEvent){
	        IomObject iomObj=((ObjectEvent)event).getIomObject();
	        assertEquals("12", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("CU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Custralia", iomObj.getattrvalue(ATTRIBUTE3));
		}
		event = reader.read();
		if(event instanceof ObjectEvent){
	        IomObject iomObj=((ObjectEvent)event).getIomObject();
	        assertEquals("13", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("DU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Dustralia", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob mehrere Records ohne Anfuehrungszeichen erstellt werden koennen.
	@Test
    public void newLineWithoutDelimiters_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"NewLineReturn.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("AU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Australia", iomObj.getattrvalue(ATTRIBUTE3));
		}
		event = reader.read();
		if(event instanceof ObjectEvent){
	        IomObject iomObj=((ObjectEvent)event).getIomObject();
	        assertEquals("11", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("BU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Bustralia", iomObj.getattrvalue(ATTRIBUTE3));
		}
		event = reader.read();
		if(event instanceof ObjectEvent){
	        IomObject iomObj=((ObjectEvent)event).getIomObject();
	        assertEquals("12", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("CU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Custralia", iomObj.getattrvalue(ATTRIBUTE3));
		}
		event = reader.read();
		if(event instanceof ObjectEvent){
	        IomObject iomObj=((ObjectEvent)event).getIomObject();
	        assertEquals("13", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("DU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Dustralia", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Attributewert mit Komma, Anfuehrungszeichen und einer neuen Zeile erstellt werden kann.
	@Test
    public void recordIncludesLineReturn_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"RecordWithLineReturn.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("AU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("A,u\"s"+reader.getLineSeparator()+"tralia", iomObj.getattrvalue(ATTRIBUTE3));
		}
		event = reader.read();
		if(event instanceof ObjectEvent){
	        IomObject iomObj=((ObjectEvent)event).getIomObject();
	        assertEquals("11", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("\\r", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("\\n", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Der Benutzer setzt den Headerparameter: present. Somit muss die erste Zeile ignoriert werden.
	@Test
    public void headerPresent_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"HeaderPresent.csv"));
		reader.setFirstLineIsHeader(true);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue("id"));
        	assertEquals("AU", iomObj.getattrvalue("abbreviation"));
        	assertEquals("Australia", iomObj.getattrvalue("state"));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Der Benutzer setzt der Headerparameter: present. Die Attribute sollen laut definition importiert werden.
	// Das model wird gesetzt.
	@Test
    public void headerPresentSpecialHeaderNames_SetModel_Ok() throws IoxException, FileNotFoundException, Ili2cFailure{
		// compile model
		
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"/model3.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td1=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td1);
		
		CsvReader reader=new CsvReader(new File(TEST_IN,"HeaderPresentNames.csv"));
		reader.setModel(td1);
		assertTrue(reader.read() instanceof StartTransferEvent);
		reader.setFirstLineIsHeader(true);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue("id"));
        	assertEquals("AU", iomObj.getattrvalue("abbreviation"));
        	assertEquals("Australia", iomObj.getattrvalue("state"));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Der Benutzer setzt der Headerparameter: present. Die Attribute sollen laut definition importiert werden.
	@Test
    public void headerPresentSpecialHeaderNames_Ok() throws IoxException, FileNotFoundException, Ili2cFailure{
		CsvReader reader=new CsvReader(new File(TEST_IN,"HeaderPresentNames.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		reader.setFirstLineIsHeader(true);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue("id"));
        	assertEquals("AU", iomObj.getattrvalue("abbreviation"));
        	assertEquals("Australia", iomObj.getattrvalue("state"));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Der Benutzer setzt der Headerparameter: absent. Somit wird kein Header erstellt.
	@Test
    public void headerAbsent_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"HeaderAbsent.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		reader.setFirstLineIsHeader(false);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("AU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Australia", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Der Benutzer setzt nur die Record-Delimiter nach seinem Ermessen.
	@Test
    public void setUserDefinedRecordDelimiter_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"RecordDelimiter.csv"));
		reader.setValueSeparator('?');
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("AU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Australia", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Der Benutzer setzt nur die Delimiter nach seinem Ermessen.
	@Test
    public void setUserDefinedDelimiter_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"Delimiter.csv"));
		reader.setValueDelimiter('%');
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("AU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Australia", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Der Benutzer setzt die Record-Delimiter und die Delimiter nach seinem Ermessen.
	@Test
    public void setUserDefinedRecordDelimiterAndDelimiter_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"RecordDelimiterAndDelimiter.csv"));
		reader.setValueSeparator('&');
		reader.setValueDelimiter('%');
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("AU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Australia", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}

    // Es wird getestet ob mehrere Anfuehrungszeichen innerhalb der Attributwerte zulaessig sind.
    @Test
    public void serevalDoubleQuotes_Ok() throws FileNotFoundException, IoxException {
    	CsvReader reader=new CsvReader(new File(TEST_IN,"TextWithSerevalDoubleQuotes.csv"));
    	assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("12", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("AU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Aust\"\"ralia", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
    }

    // Es wird getestet ob Anfuehrungszeichen innerhalb der Attributwerte zulaessig sind.
    @Test
    public void doubleQuotesInValue_Ok() throws FileNotFoundException, IoxException {
    	CsvReader reader=new CsvReader(new File(TEST_IN,"TextWithDoubleQuotesInColumn.csv"));
    	assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("13", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("AU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Aus\"tralia", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
    }

    // Es wird getestet ob Anfuehrungszeichen und Kommas innerhalb der Attributwerte zulaessig sind.
    @Test
    public void doubleQuotesAndCommaInValue_Ok() throws FileNotFoundException, IoxException {
    	CsvReader reader=new CsvReader(new File(TEST_IN,"TextWithDoubleQuotesAndCommaInColumn.csv"));
    	assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("14", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("AU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Aus,trali\"a", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
    }
    
    // Es wird getestet ob Anfuehrungszeichen ausserhalb und innerhalb der Attributwerte zulaessig sind.
    @Test
    public void doubleQuotes_Ok() throws Exception{
    	CsvReader reader=new CsvReader(new File(TEST_IN,"TextWithDoubleQuotes.csv"));
    	assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
    	IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("11", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("AU", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Aus\"tralia", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
    }
    
    // Der Benutzer setzt einen Header und ein Model.
 	@Test
     public void attrsEqualIliClass_SetModelAndHeader_Ok() throws IoxException, FileNotFoundException{
 		CsvReader reader=new CsvReader(new File(TEST_IN,"HeaderPresent.csv"));
 		reader.setModel(td);
 		assertTrue(reader.read() instanceof StartTransferEvent);
 		reader.setFirstLineIsHeader(true);
 		assertTrue(reader.read() instanceof StartBasketEvent);
 		IoxEvent event = reader.read();
 		if(event instanceof ObjectEvent){
         	IomObject iomObj=((ObjectEvent)event).getIomObject();
         	assertEquals("10", iomObj.getattrvalue(MODEL3_ATTR_ID));
        	assertEquals("AU", iomObj.getattrvalue(MODEL3_ATTR_ABBREVIATION));
        	assertEquals("Australia", iomObj.getattrvalue(MODEL3_ATTR_STATE));
 		}
 		assertTrue(reader.read() instanceof EndBasketEvent);
 		assertTrue(reader.read() instanceof EndTransferEvent);
 		reader.close();
 		reader=null;
 	}
    // Der Benutzer setzt einen Header und ein Model. Im Header haben die Attribute eine andere Gross-/kleinschreibung als im Modell.
 	// Der CsvReader muss die Namen gem. Modell verwenden/liefern.
 	@Test
     public void attrsSimpilarIliClass_SetModelAndHeader_Ok() throws IoxException, FileNotFoundException{
 		CsvReader reader=new CsvReader(new File(TEST_IN,"HeaderSimilar.csv"));
 		reader.setModel(td);
 		reader.setFirstLineIsHeader(true);
 		assertTrue(reader.read() instanceof StartTransferEvent);
 		assertTrue(reader.read() instanceof StartBasketEvent);
 		IoxEvent event = reader.read();
 		if(event instanceof ObjectEvent){
         	IomObject iomObj=((ObjectEvent)event).getIomObject();
         	assertEquals("10", iomObj.getattrvalue(MODEL3_ATTR_ID));
        	assertEquals("AU", iomObj.getattrvalue(MODEL3_ATTR_ABBREVIATION));
        	assertEquals("Australia", iomObj.getattrvalue(MODEL3_ATTR_STATE));
 		}
 		assertTrue(reader.read() instanceof EndBasketEvent);
 		assertTrue(reader.read() instanceof EndTransferEvent);
 		reader.close();
 		reader=null;
 	}
 	
 	// Der Benutzer setzt ein Model.
 	@Test
    public void attrCountEqualIliClass_SetModel_Ok() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"HeaderAbsent.csv"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event = reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(MODEL3_ATTR_ID));
        	assertEquals("AU", iomObj.getattrvalue(MODEL3_ATTR_ABBREVIATION));
        	assertEquals("Australia", iomObj.getattrvalue(MODEL3_ATTR_STATE));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
 	
	// Der Benutzer gibt 3 models an. Zuerst wird nach der Anzahl der Attribute innerhalb des BundesModel.ili gesucht.
	// Dort finden sich die Attribute, deshalb sucht er nicht mehr weiter. In den beiden anderen models, wuerde er auch
 	// die gesuchten Attribute finden.
	// resultat == BundesModel.ili.
	@Test
    public void setMultipleModels_GetFirstModelData_Ok() throws IoxException, FileNotFoundException, Ili2cFailure{
		// ili-datei lesen
		TransferDescription tdM=null;
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntryConditionClass=new FileEntry(TEST_IN+"/BundesModel.ili", FileEntryKind.ILIMODELFILE); // first input model
		ili2cConfig.addFileEntry(fileEntryConditionClass);
		FileEntry fileEntry=new FileEntry(TEST_IN+"/KantonModel.ili", FileEntryKind.ILIMODELFILE); // second input model
		ili2cConfig.addFileEntry(fileEntry);
		FileEntry fileEntry2=new FileEntry(TEST_IN+"/StadtModel.ili", FileEntryKind.ILIMODELFILE); // third input model
		ili2cConfig.addFileEntry(fileEntry2);
		ili2cConfig.setAutoCompleteModelList(false);
		tdM=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(tdM);
		
 		CsvReader reader=new CsvReader(new File(TEST_IN,"TextType.csv"));
		reader.setModel(tdM);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertTrue(iomObj.getattrcount()==3);
        	assertTrue(iomObj.getobjecttag().contains("StadtModel"));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Der Benutzer gibt 3 models an. Zuerst wird nach den namen der Attribute innerhalb des StadtModel.ili gesucht.
	// Falls dieser nichts findet, wird nach der KantonModel.ili gesucht. Falls auch dieser nichts findet,
	// wird nach der BundesModel.ili gesucht.
	// resultat == StadtModel.ili.
	@Test
    public void setMultipleModels_GetFirstModelData_SetHeader_Ok() throws IoxException, FileNotFoundException, Ili2cFailure{
		// ili-datei lesen
		TransferDescription tdM=null;
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntryConditionClass=new FileEntry(TEST_IN+"/StadtModel.ili", FileEntryKind.ILIMODELFILE); // first input model
		ili2cConfig.addFileEntry(fileEntryConditionClass);
		FileEntry fileEntry=new FileEntry(TEST_IN+"/KantonModel.ili", FileEntryKind.ILIMODELFILE); // second input model
		ili2cConfig.addFileEntry(fileEntry);
		FileEntry fileEntry2=new FileEntry(TEST_IN+"/BundesModel.ili", FileEntryKind.ILIMODELFILE); // third input model
		ili2cConfig.addFileEntry(fileEntry2);
		tdM=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(tdM);
		
 		CsvReader reader=new CsvReader(new File(TEST_IN,"HeaderPresent.csv"));
		reader.setModel(tdM);
		reader.setFirstLineIsHeader(true);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertTrue(iomObj.getobjecttag().contains("StadtModel"));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Der Benutzer gibt 3 models an. Zuerst wird nach den Attributen innerhalb des model5.ili gesucht.
	// Falls dieser nichts findet, wird nach der model3.ili gesucht. Falls auch dieser nichts findet,
	// wird nach der BundesModel.ili gesucht.
	// resultat == BundesModel.ili.
	@Test
    public void setMultipleModels_GetLastModelData_SetHeader_Ok() throws IoxException, FileNotFoundException, Ili2cFailure{
		// ili-datei lesen
		TransferDescription tdM=null;
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntryConditionClass=new FileEntry(TEST_IN+"/model5.ili", FileEntryKind.ILIMODELFILE); // first input model
		ili2cConfig.addFileEntry(fileEntryConditionClass);
		FileEntry fileEntry=new FileEntry(TEST_IN+"/model3.ili", FileEntryKind.ILIMODELFILE); // second input model
		ili2cConfig.addFileEntry(fileEntry);
		FileEntry fileEntry2=new FileEntry(TEST_IN+"/BundesModel.ili", FileEntryKind.ILIMODELFILE); // third input model
		ili2cConfig.addFileEntry(fileEntry2);
		tdM=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(tdM);
 		CsvReader reader=new CsvReader(new File(TEST_IN,"HeaderPresent3.csv"));
		reader.setModel(tdM);
		assertTrue(reader.read() instanceof StartTransferEvent);
		reader.setFirstLineIsHeader(true);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			assertTrue(iomObj.getattrcount()==6);
			assertTrue(iomObj.getobjecttag().contains("BundesModel"));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird eine Csv-Datei gelesen, welche die folgenden Attribute beinhaltet:
	// - idname
	// - textname
	// - doublename
	// - the_geom
	// --
	// Nun werden die Attribute-Werte, nach den Attribute-Namen welche im Model definiert sind,
	// aus der Csv-Datei herausgelesen:
	// - idname
	// - textname
	// - doublename
	// - the_geom
	// --
	// Erwartung: SUCCESS.
	@Test
	public void limitedSelection_Ok() throws Exception {
		// reader test
		CsvReader reader=null;
		TransferDescription tdM=null;
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntryConditionClass=new FileEntry(TEST_IN+"/CsvModelAttributesLimited.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntryConditionClass);
		tdM=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(tdM);
		try {
			reader=new CsvReader(new File(TEST_IN,"AttributesLimited.csv"));
			reader.setFirstLineIsHeader(true);
			reader.setModel(tdM);
			assertTrue(reader.read() instanceof StartTransferEvent);
			assertTrue(reader.read() instanceof StartBasketEvent);
			IoxEvent event=reader.read();
			if(event instanceof ObjectEvent){
	        	IomObject iomObj=((ObjectEvent)event).getIomObject();
	        	assertTrue(iomObj.getattrcount()==4);
	        	assertTrue(iomObj.getattrvalue("doublename").equals("54321"));
	        	assertTrue(iomObj.getattrvalue("idname").equals("1"));
	        	assertTrue(iomObj.getattrvalue("textname").equals("text1"));
	        	assertTrue(iomObj.getattrvalue("the_geom").equals("COORD {C1 -0.5332351148239034, C2 0.7382312503416462}"));
			}
			assertTrue(reader.read() instanceof EndBasketEvent);
			assertTrue(reader.read() instanceof EndTransferEvent);
		}finally {
			if(reader!=null) {
		    	reader.close();
				reader=null;
	    	}
		}
	}
	
 	// Der Benutzer setzt ein Model. Der Attribute Count wird in keiner Klasse des Models gefunden.
 	@Test
    public void attrCountNotEqualIliClass_SetModel_Fail() throws IoxException, FileNotFoundException{
		CsvReader reader=new CsvReader(new File(TEST_IN,"HeaderAbsent2.csv"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof StartTransferEvent);
		try{
    		reader.read();
    		fail();
    	}catch(IoxException ex){
    		assertTrue(ex.getMessage().contains("attributes size of first line: 4 not found in iliModel: model3"));
    	}
		reader.close();
		reader=null;
	}
 	
 	// Der Benutzer setzt ein Model. Es werden mehrere Klassen mit der gleichen Anzahl an Attributen innerhalb des gesetzten Models gefunden.
 	@Test
    public void multipleClassesFound_SetModel_Fail() throws IoxException, FileNotFoundException, Ili2cFailure{
 	// compile model
	Configuration ili2cConfig=new Configuration();
	FileEntry fileEntry=new FileEntry(TEST_IN+"/model4.ili", FileEntryKind.ILIMODELFILE);
	ili2cConfig.addFileEntry(fileEntry);
	TransferDescription td2=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
	assertNotNull(td2);
 		
		CsvReader reader=new CsvReader(new File(TEST_IN,"HeaderAbsent2.csv"));
		reader.setModel(td2);
		assertTrue(reader.read() instanceof StartTransferEvent);
		try{
    		reader.read();
    		fail();
    	}catch(IoxException ex){
    		assertTrue(ex.getMessage().contains("multiple class candidates:"));
    		assertTrue(ex.getMessage().contains("Class1"));
    		assertTrue(ex.getMessage().contains("Class2"));
    		assertTrue(ex.getMessage().contains("Class3"));
    	}
		reader.close();
		reader=null;
	}
 	
 	// Der Benutzer setzt ein Model. Die Anzahl der Attribute koennen nicht innerhalb des gesetzten Models gefunden werden.
 	@Test
    public void attrCountNotEqualIliClass_Fail() throws IoxException, FileNotFoundException, Ili2cFailure{
 		// compile model
 		TransferDescription tdM=null;
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"/model.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		tdM=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(tdM);
 		
 		CsvReader reader=new CsvReader(new File(TEST_IN,"HeaderAbsent3.csv"));
		reader.setModel(tdM);
		assertTrue(reader.read() instanceof StartTransferEvent);
    	try{
    		reader.read();
    		fail();
    	}catch(IoxException ex){
    		assertTrue(ex.getMessage().contains("attributes size of first line: 6 not found in iliModel: model"));
    	}
		reader.close();
		reader=null;
	}
	
	// Der Benutzer setzt das Model und den Header. Die Attribute koennen nicht innerhalb des gesetzten Models gefunden werden.
	@Test
    public void attrsNotFound_SetModelAndHeader_Fail() throws IoxException, FileNotFoundException, Ili2cFailure{
 		// compile model
 		TransferDescription tdM=null;
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"/model3.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		tdM=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(tdM);
 		
 		CsvReader reader=new CsvReader(new File(TEST_IN,"HeaderPresent2.csv"));
		reader.setModel(tdM);
		assertTrue(reader.read() instanceof StartTransferEvent);
		reader.setFirstLineIsHeader(true);
    	try{
    		reader.read();
    		fail();
    	}catch(IoxException ex){
    		assertTrue(ex.getMessage().contains("attributes of headerrecord:"));
    		assertTrue(ex.getMessage().contains(ATTRIBUTE1));
    		assertTrue(ex.getMessage().contains(ATTRIBUTE2));
    		assertTrue(ex.getMessage().contains(ATTRIBUTE3));
    		assertTrue(ex.getMessage().contains(ATTRIBUTE4));
    		assertTrue(ex.getMessage().contains(ATTRIBUTE5));
    		assertTrue(ex.getMessage().contains("not found in iliModel: model3"));
    	}
		reader.close();
		reader=null;
	}
	
	// Der Benutzer setzt das Model. Die Anzahl der Attribute konnten innerhalb des gesetzten Models nicht gefunden werden.
	@Test
    public void attrCountNotFound_SetModel_Fail() throws IoxException, FileNotFoundException, Ili2cFailure{
 		// compile model
 		TransferDescription tdM=null;
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"/model2.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		tdM=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(tdM);
 		
 		CsvReader reader=new CsvReader(new File(TEST_IN,"HeaderAbsent3.csv"));
		reader.setModel(tdM);
		assertTrue(reader.read() instanceof StartTransferEvent);
    	try{
    		reader.read();
    		fail();
    	}catch(IoxException ex){
    		assertTrue(ex.getMessage().contains("attributes size of first line: 6 not found in iliModel: model2"));
    	}
		reader.close();
		reader=null;
	}
	
	// Der Benutzer setzt der Headerparameter: present. Die Attribute koennen im gesetzten model nicht gefunden werden.
	@Test
    public void attrNamesNotFoundInModel_Fail() throws IoxException, FileNotFoundException, Ili2cFailure{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"/model3.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td1=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td1);
		
		CsvReader reader=new CsvReader(new File(TEST_IN,"AttrNamesNotFoundInModel.csv"));
		reader.setModel(td1);
		assertTrue(reader.read() instanceof StartTransferEvent);
		reader.setFirstLineIsHeader(true);
		try{
    		reader.read();
    		fail();
    	}catch(IoxException ex){
    		assertTrue(ex.getMessage().contains("attributes of headerrecord: [id2, abbreviation3, country] not found in iliModel: model"));
    	}
		reader.close();
		reader=null;
	}
	
	// Der Benutzer setzt der Headerparameter: present. Die Werte auf der 5ten Zeile haben einen Wert zuviel.
	// Dieser wird nicht herausgeschrieben. Dafuer soll eine Warnung dem Benutzer angezeigt werden.
	// Das Modell wird gesetzt.
	@Test
    public void numberOfAttrsNotEqual_SetModel_SetHeader_Warn() throws IoxException, FileNotFoundException, Ili2cFailure{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"/model3.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td1=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td1);
		
		CsvReader reader=new CsvReader(new File(TEST_IN,"NumberOfAttrsNotEqual.csv"));
		reader.setModel(td1);
		assertTrue(reader.read() instanceof StartTransferEvent);
		reader.setFirstLineIsHeader(true);
		assertTrue(reader.read() instanceof StartBasketEvent);
		assertTrue(reader.read() instanceof ObjectEvent);
		assertTrue(reader.read() instanceof ObjectEvent);
		assertTrue(reader.read() instanceof ObjectEvent);
		assertTrue(reader.read() instanceof ObjectEvent);
		IoxEvent event=reader.read();
		assertTrue(event instanceof ObjectEvent);
			IomObject iomObj=((ObjectEvent)event).getIomObject();
			assertTrue(iomObj.getattrcount()==3);
	    	assertEquals("14", iomObj.getattrvalue("id"));
	    	assertEquals("AU", iomObj.getattrvalue("abbreviation"));
	    	assertEquals("Australia", iomObj.getattrvalue("state"));
		assertTrue(reader.read() instanceof ObjectEvent);
		assertTrue(reader.read() instanceof EndBasketEvent);
 		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Der Benutzer setzt der Headerparameter: absent. Die Werte auf der 5ten Zeile haben einen Wert zuviel.
	// Dieser wird nicht herausgeschrieben. Dafuer soll eine Warnung dem Benutzer angezeigt werden.
	// Das Model wird gesetzt.
	@Test
    public void numberOfAttrsNotEqual_SetModel_Warn() throws IoxException, FileNotFoundException, Ili2cFailure{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"/model3.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td1=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td1);
		
		CsvReader reader=new CsvReader(new File(TEST_IN,"NumberOfAttrsNotEqual2.csv"));
		reader.setModel(td1);
		assertTrue(reader.read() instanceof StartTransferEvent);
		reader.setFirstLineIsHeader(false);
		assertTrue(reader.read() instanceof StartBasketEvent);
		assertTrue(reader.read() instanceof ObjectEvent);
		assertTrue(reader.read() instanceof ObjectEvent);
		assertTrue(reader.read() instanceof ObjectEvent);
		assertTrue(reader.read() instanceof ObjectEvent);
		IoxEvent event=reader.read();
		assertTrue(event instanceof ObjectEvent);
			IomObject iomObj=((ObjectEvent)event).getIomObject();
			assertTrue(iomObj.getattrcount()==3);
	    	assertEquals("14", iomObj.getattrvalue("id"));
	    	assertEquals("AU", iomObj.getattrvalue("abbreviation"));
	    	assertEquals("Australia", iomObj.getattrvalue("state"));
		assertTrue(reader.read() instanceof ObjectEvent);
		assertTrue(reader.read() instanceof EndBasketEvent);
 		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Der Benutzer setzt der Headerparameter: present. Die Attributenamen stimmen nicht mit den Modellen ueberein.
	// Somit kann kein passendes Modell gefunden werden.
	// Das Model wird gesetzt.
	@Test
	public void modelClassNotFound_Fail() throws Exception {
		// reader test
		CsvReader reader=null;
		TransferDescription tdM=null;
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntryConditionClass=new FileEntry(TEST_IN+"/CsvModelAttributesLimited.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntryConditionClass);
		tdM=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(tdM);
		try {
			reader=new CsvReader(new File(TEST_IN,"AttributesLimited2.csv"));
			reader.setFirstLineIsHeader(true);
			reader.setModel(tdM);
			assertTrue(reader.read() instanceof StartTransferEvent);
			assertTrue(reader.read() instanceof StartBasketEvent);
			IoxEvent event=reader.read();
			fail();
		}catch(Exception e) {
			assertTrue(e.getMessage().contains("attributes of headerrecord: [idname2, textname, the_geom] not found in iliModel: CsvModelAttributesLimited"));
		}finally {
			if(reader!=null) {
		    	reader.close();
				reader=null;
	    	}
		}
	}
}