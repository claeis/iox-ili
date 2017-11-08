package ch.interlis.iom_j.csv;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;

public class CsvWriterTest {
	private TransferDescription td=null;
	private static final String TEST_IN="src/test/data/CsvWriter";
	private static final String ATTRIBUTE1="attr1";
	private static final String ATTRIBUTE2="attr2";
	private static final String ATTRIBUTE3="attr3";
	private static final String ID="id";
	private static final String STADT="stadt";
	private static final String LAND="land";
	
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"/model.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header nicht gesetzt wird
	@Test
	public void normalTest_Ok() throws IoxException, FileNotFoundException{
		final String OBJECT_MODEL_SET_NO_HEADER_OK_CSV = "object_ModelSet_NoHeader_Ok.csv";

		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,OBJECT_MODEL_SET_NO_HEADER_OK_CSV));
			writer.write(new StartTransferEvent());
			writer.setModel(td);
			writer.setWriteHeader(false);
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern");
			iomObj.setattrvalue("land", "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		CsvReader reader=new CsvReader(new File(TEST_IN,OBJECT_MODEL_SET_NO_HEADER_OK_CSV));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Bern", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Schweiz", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,OBJECT_MODEL_SET_NO_HEADER_OK_CSV);
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header mit present gesetzt wird
	@Test
	public void object_ModelSet_SetHeaderPresent_Ok() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"object_ModelSet_SetHeaderPresent_Ok.csv"));
			writer.write(new StartTransferEvent());
			writer.setModel(td);
			writer.setWriteHeader(true);
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern");
			iomObj.setattrvalue("land", "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		CsvReader reader=new CsvReader(new File(TEST_IN,"object_ModelSet_SetHeaderPresent_Ok.csv"));
		reader.setModel(td);
		reader.setHeader("present");
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ID));
        	assertEquals("Bern", iomObj.getattrvalue(STADT));
        	assertEquals("Schweiz", iomObj.getattrvalue(LAND));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"object_ModelSet_SetHeaderPresent_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header mit absent gesetzt wird
	@Test
	public void object_ModelSet_SetHeaderAbsent_Ok() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"object_ModelSet_SetHeaderAbsent_Ok.csv"));
			writer.write(new StartTransferEvent());
			writer.setModel(td);
			writer.setWriteHeader(false);
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern");
			iomObj.setattrvalue("land", "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		CsvReader reader=new CsvReader(new File(TEST_IN,"object_ModelSet_SetHeaderAbsent_Ok.csv"));
		reader.setModel(td);
		reader.setHeader("absent");
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Bern", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Schweiz", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"object_ModelSet_SetHeaderAbsent_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell nicht gesetzt wird
	// - Der Header mit present gesetzt wird
	@Test
	public void object_NoModelSet_SetHeaderPresent_Ok() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"object_NoModelSet_SetHeaderPresent_Ok.csv"));
			writer.write(new StartTransferEvent());
			writer.setWriteHeader(true);
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern");
			iomObj.setattrvalue("land", "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		CsvReader reader=new CsvReader(new File(TEST_IN,"object_NoModelSet_SetHeaderPresent_Ok.csv"));
		reader.setHeader("present");
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue("id"));
        	assertEquals("Bern", iomObj.getattrvalue("stadt"));
        	assertEquals("Schweiz", iomObj.getattrvalue("land"));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"object_NoModelSet_SetHeaderPresent_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell nicht gesetzt wird
	// - Der Header mit absent gesetzt wird
	@Test
	public void object_NoModelSet_SetHeaderAbsent_Ok() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"object_NoModelSet_SetHeaderAbsent_Ok.csv"));
			writer.write(new StartTransferEvent());
			writer.setWriteHeader(false);
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern");
			iomObj.setattrvalue("land", "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		CsvReader reader=new CsvReader(new File(TEST_IN,"object_NoModelSet_SetHeaderAbsent_Ok.csv"));
		reader.setHeader("absent");
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Schweiz", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Bern", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"object_NoModelSet_SetHeaderAbsent_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell nicht gesetzt wird
	// - Der Header nicht gesetzt wird
	@Test
	public void object_NoModelSet_NoHeader_Ok() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"object_NoModelSet_NoHeader_Ok.csv"));
			writer.setWriteHeader(false);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern");
			iomObj.setattrvalue("land", "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		CsvReader reader=new CsvReader(new File(TEST_IN,"object_NoModelSet_NoHeader_Ok.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Schweiz", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Bern", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"object_NoModelSet_NoHeader_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - 3 Modelle gesetzt werden
	// - Der Header mit present gesetzt wird
	@Test
	public void object_SetMultipleModels_SetHeaderPresent_Ok() throws IoxException, FileNotFoundException, Ili2cFailure{
		// ili-datei lesen
		TransferDescription tdM=null;
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntryConditionClass=new FileEntry(TEST_IN+"/StadtModel4.ili", FileEntryKind.ILIMODELFILE); // first input model
		ili2cConfig.addFileEntry(fileEntryConditionClass);
		FileEntry fileEntry=new FileEntry(TEST_IN+"/KantonsModel2.ili", FileEntryKind.ILIMODELFILE); // second input model
		ili2cConfig.addFileEntry(fileEntry);
		FileEntry fileEntry2=new FileEntry(TEST_IN+"/LandModel3.ili", FileEntryKind.ILIMODELFILE); // third input model
		ili2cConfig.addFileEntry(fileEntry2);
		tdM=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(tdM);
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"object_SetMultipleModels_SetHeaderPresent_Ok.csv"));
			writer.write(new StartTransferEvent());
			writer.setModel(tdM);
			writer.setWriteHeader(true);
			writer.write(new StartBasketEvent("KantonsModel2.Topic2","bid1"));
			IomObject iomObj=new Iom_jObject("KantonsModel2.Topic2.Class2","oid1");
			iomObj.setattrvalue("id2", "10");
			iomObj.setattrvalue("stadt2", "Bern");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
				writer=null;
			}
		}
		CsvReader reader=new CsvReader(new File(TEST_IN,"object_SetMultipleModels_SetHeaderPresent_Ok.csv"));
		reader.setHeader("absent");
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("id2", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("stadt2", iomObj.getattrvalue(ATTRIBUTE2));
		}
		event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Bern", iomObj.getattrvalue(ATTRIBUTE2));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"object_SetMultipleModels_SetHeaderPresent_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header mit present gesetzt wird
	// - Mehrere Objekte erstellt werden
	@Test
	public void multipleObjects_ModelSet_SetHeaderPresent_Ok() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"multipleObjects_ModelSet_SetHeaderPresent_Ok.csv"));
			writer.write(new StartTransferEvent());
			writer.setModel(td);
			writer.setWriteHeader(true);
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern");
			iomObj.setattrvalue("land", "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			IomObject iomObj2=new Iom_jObject("model.Topic1.Class1","oid2");
			iomObj2.setattrvalue("id", "11");
			iomObj2.setattrvalue("stadt", "Zuerich");
			iomObj2.setattrvalue("land", "Deutschland");
			writer.write(new ObjectEvent(iomObj2));
			IomObject iomObj3=new Iom_jObject("model.Topic1.Class1","oid3");
			iomObj3.setattrvalue("id", "12");
			iomObj3.setattrvalue("stadt", "Luzern");
			iomObj3.setattrvalue("land", "Italien");
			writer.write(new ObjectEvent(iomObj3));
			IomObject iomObj4=new Iom_jObject("model.Topic1.Class1","oid4");
			iomObj4.setattrvalue("id", "13");
			iomObj4.setattrvalue("stadt", "Genf");
			iomObj4.setattrvalue("land", "Oesterreich");
			writer.write(new ObjectEvent(iomObj4));
			IomObject iomObj5=new Iom_jObject("model.Topic1.Class1","oid5");
			iomObj5.setattrvalue("id", "14");
			iomObj5.setattrvalue("stadt", "Chur");
			iomObj5.setattrvalue("land", "Spanien");
			writer.write(new ObjectEvent(iomObj5));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		IoxEvent event=null;
		CsvReader reader=new CsvReader(new File(TEST_IN,"multipleObjects_ModelSet_SetHeaderPresent_Ok.csv"));
		reader.setHeader("present");
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		event=reader.read();
		if(event instanceof ObjectEvent){
			{
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ID));
        	assertEquals("Bern", iomObj.getattrvalue(STADT));
        	assertEquals("Schweiz", iomObj.getattrvalue(LAND));
			}
			event=reader.read();
			{
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("11", iomObj.getattrvalue(ID));
        	assertEquals("Zuerich", iomObj.getattrvalue(STADT));
        	assertEquals("Deutschland", iomObj.getattrvalue(LAND));
			}
			event=reader.read();
			{
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("12", iomObj.getattrvalue(ID));
        	assertEquals("Luzern", iomObj.getattrvalue(STADT));
        	assertEquals("Italien", iomObj.getattrvalue(LAND));
			}
			event=reader.read();
			{
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("13", iomObj.getattrvalue(ID));
        	assertEquals("Genf", iomObj.getattrvalue(STADT));
        	assertEquals("Oesterreich", iomObj.getattrvalue(LAND));
			}
			event=reader.read();
			{
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("14", iomObj.getattrvalue(ID));
        	assertEquals("Chur", iomObj.getattrvalue(STADT));
        	assertEquals("Spanien", iomObj.getattrvalue(LAND));
			}
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"multipleObjects_ModelSet_SetHeaderPresent_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Mehrere Objekte erstellt werden
	@Test
	public void multipleObjects_ModelSet_NoHeaderSet_Ok() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"multipleObjects_ModelSet_NoHeaderSet_Ok.csv"));
			writer.write(new StartTransferEvent());
			writer.setModel(td);
			writer.setWriteHeader(false);
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern");
			iomObj.setattrvalue("land", "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			IomObject iomObj2=new Iom_jObject("model.Topic1.Class1","oid2");
			iomObj2.setattrvalue("id", "11");
			iomObj2.setattrvalue("stadt", "Zuerich");
			iomObj2.setattrvalue("land", "Deutschland");
			writer.write(new ObjectEvent(iomObj2));
			IomObject iomObj3=new Iom_jObject("model.Topic1.Class1","oid3");
			iomObj3.setattrvalue("id", "12");
			iomObj3.setattrvalue("stadt", "Luzern");
			iomObj3.setattrvalue("land", "Italien");
			writer.write(new ObjectEvent(iomObj3));
			IomObject iomObj4=new Iom_jObject("model.Topic1.Class1","oid4");
			iomObj4.setattrvalue("id", "13");
			iomObj4.setattrvalue("stadt", "Genf");
			iomObj4.setattrvalue("land", "Oesterreich");
			writer.write(new ObjectEvent(iomObj4));
			IomObject iomObj5=new Iom_jObject("model.Topic1.Class1","oid5");
			iomObj5.setattrvalue("id", "14");
			iomObj5.setattrvalue("stadt", "Chur");
			iomObj5.setattrvalue("land", "Spanien");
			writer.write(new ObjectEvent(iomObj5));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		IoxEvent event=null;
		CsvReader reader=new CsvReader(new File(TEST_IN,"multipleObjects_ModelSet_NoHeaderSet_Ok.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		event=reader.read();
		if(event instanceof ObjectEvent){
			{
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Bern", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Schweiz", iomObj.getattrvalue(ATTRIBUTE3));
			}
			event=reader.read();
			{
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("11", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Zuerich", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Deutschland", iomObj.getattrvalue(ATTRIBUTE3));
			}
			event=reader.read();
			{
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("12", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Luzern", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Italien", iomObj.getattrvalue(ATTRIBUTE3));
			}
			event=reader.read();
			{
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("13", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Genf", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Oesterreich", iomObj.getattrvalue(ATTRIBUTE3));
			}
			event=reader.read();
			{
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("14", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Chur", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Spanien", iomObj.getattrvalue(ATTRIBUTE3));
			}
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"multipleObjects_ModelSet_NoHeaderSet_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell nicht gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Mehrere Objekte erstellt werden
	@Test
	public void multipleObjects_NoModelSet_NoHeaderSet_Ok() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"multipleObjects_NoModelSet_NoHeaderSet_Ok.csv"));
			writer.setWriteHeader(false);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern");
			iomObj.setattrvalue("land", "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			IomObject iomObj2=new Iom_jObject("model.Topic1.Class1","oid2");
			iomObj2.setattrvalue("id", "11");
			iomObj2.setattrvalue("stadt", "Zuerich");
			iomObj2.setattrvalue("land", "Deutschland");
			writer.write(new ObjectEvent(iomObj2));
			IomObject iomObj3=new Iom_jObject("model.Topic1.Class1","oid3");
			iomObj3.setattrvalue("id", "12");
			iomObj3.setattrvalue("stadt", "Luzern");
			iomObj3.setattrvalue("land", "Italien");
			writer.write(new ObjectEvent(iomObj3));
			IomObject iomObj4=new Iom_jObject("model.Topic1.Class1","oid4");
			iomObj4.setattrvalue("id", "13");
			iomObj4.setattrvalue("stadt", "Genf");
			iomObj4.setattrvalue("land", "Oesterreich");
			writer.write(new ObjectEvent(iomObj4));
			IomObject iomObj5=new Iom_jObject("model.Topic1.Class1","oid5");
			iomObj5.setattrvalue("id", "14");
			iomObj5.setattrvalue("stadt", "Chur");
			iomObj5.setattrvalue("land", "Spanien");
			writer.write(new ObjectEvent(iomObj5));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		IoxEvent event=null;
		CsvReader reader=new CsvReader(new File(TEST_IN,"multipleObjects_NoModelSet_NoHeaderSet_Ok.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		event=reader.read();
		if(event instanceof ObjectEvent){
			{
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Schweiz", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Bern", iomObj.getattrvalue(ATTRIBUTE3));
			}
			event=reader.read();
			{
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("11", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Deutschland", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Zuerich", iomObj.getattrvalue(ATTRIBUTE3));
			}
			event=reader.read();
			{
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("12", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Italien", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Luzern", iomObj.getattrvalue(ATTRIBUTE3));
			}
			event=reader.read();
			{
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("13", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Oesterreich", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Genf", iomObj.getattrvalue(ATTRIBUTE3));
			}
			event=reader.read();
			{
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("14", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Spanien", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Chur", iomObj.getattrvalue(ATTRIBUTE3));
			}
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"multipleObjects_NoModelSet_NoHeaderSet_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header mit present gesetzt wird
	// - Komma Zeichen innerhalb des Textes geschrieben sind
	@Test
	public void commaInText_Ok() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"commaInText_Ok.csv"));
			writer.setModel(td);
			writer.setWriteHeader(true);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "1,0");
			iomObj.setattrvalue("stadt", "Be,rn");
			iomObj.setattrvalue("land", "S,chweiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		CsvReader reader=new CsvReader(new File(TEST_IN,"commaInText_Ok.csv"));
		reader.setHeader("present");
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("1,0", iomObj.getattrvalue(ID));
        	assertEquals("Be,rn", iomObj.getattrvalue(STADT));
        	assertEquals("S,chweiz", iomObj.getattrvalue(LAND));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"commaInText_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell nicht gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Delimiter gesetzt wird
	// - Record Delimiter gesetzt wird
	@Test
	public void setDelimiter_setRecordDelimiter_NoModelSet_Ok() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"setDelimiter_setRecordDelimiter_NoModelSet_Ok.csv"));
			writer.setWriteHeader(false);
			writer.setValueDelimiter('|');
			writer.setValueSeparator('\\');
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern");
			iomObj.setattrvalue("land", "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		CsvReader reader=new CsvReader(new File(TEST_IN,"setDelimiter_setRecordDelimiter_NoModelSet_Ok.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("|10|\\|Schweiz|\\|Bern|", iomObj.getattrvalue(ATTRIBUTE1));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"setDelimiter_setRecordDelimiter_NoModelSet_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell nicht gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Delimiter gesetzt wird
	@Test
	public void setDelimiter_NoModelSet_Ok() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"setDelimiter_NoModelSet_Ok.csv"));
			writer.setValueDelimiter('|');
			writer.setWriteHeader(false);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern");
			iomObj.setattrvalue("land", "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		CsvReader reader=new CsvReader(new File(TEST_IN,"setDelimiter_NoModelSet_Ok.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("|10|", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("|Schweiz|", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("|Bern|", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"setDelimiter_NoModelSet_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell nicht gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Record Delimiter gesetzt wird
	@Test
	public void setRecordDelimiter_NoModelSet_Ok() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"setRecordDelimiter_NoModelSet_Ok.csv"));
			writer.setWriteHeader(false);
			writer.setValueSeparator('\\');
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern");
			iomObj.setattrvalue("land", "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		CsvReader reader=new CsvReader(new File(TEST_IN,"setRecordDelimiter_NoModelSet_Ok.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10\\\"Schweiz\\\"Bern", iomObj.getattrvalue(ATTRIBUTE1));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"setRecordDelimiter_NoModelSet_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Delimiter gesetzt wird
	// - Record Delimiter gesetzt wird
	// - Record Delimiter innerhalb des Textes geschrieben sind
	// - Delimiter innerhalb des Textes geschrieben sind
	@Test
	public void setDelimiter_setRecordDelimiter_ModelSet_DelimitersInText_Ok() throws Exception{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"setDelimiter_setRecordDelimiter_ModelSet_DelimitersInText_Ok.csv"));
			writer.setValueDelimiter('\\');
			writer.setValueSeparator('\\');
			writer.setModel(td);
			writer.setWriteHeader(false);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "B\\ern");
			iomObj.setattrvalue("land", "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		BufferedReader reader=new BufferedReader(new FileReader(new File(TEST_IN,"setDelimiter_setRecordDelimiter_ModelSet_DelimitersInText_Ok.csv")));
		String line=reader.readLine();
		assertEquals("\\10\\\\\\B\\\\ern\\\\\\Schweiz\\",line); // FIXME CsvReader fails
		assertEquals(null,reader.readLine());
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"setDelimiter_setRecordDelimiter_ModelSet_DelimitersInText_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Delimiter gesetzt wird
	// - Delimiter innerhalb des Textes geschrieben sind
	@Test
	public void setDelimiter_DelimiterInText_Ok() throws Exception{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"setDelimiter_DelimiterInText_Ok.csv"));
			writer.setValueDelimiter('|');
			writer.setModel(td);
			writer.setWriteHeader(false);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern");
			iomObj.setattrvalue("land", "Schw|eiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		BufferedReader reader=new BufferedReader(new FileReader(new File(TEST_IN,"setDelimiter_DelimiterInText_Ok.csv")));
       	assertEquals("|10|,|Bern|,|Schw||eiz|", reader.readLine()); // FIXME CsvReader fails
       	assertEquals(null, reader.readLine());
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"setDelimiter_DelimiterInText_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Record Delimiter gesetzt wird
	// - Record Delimiter innerhalb des Textes geschrieben sind
	@Test
	public void setRecordDelimiter_RecordDelimiterInText_ModelSet_Ok() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"setRecordDelimiter_RecordDelimiterInText_ModelSet_Ok.csv"));
			writer.setValueSeparator('\\');
			writer.setModel(td);
			writer.setWriteHeader(false);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern");
			iomObj.setattrvalue("land", "Schw\\eiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		CsvReader reader=new CsvReader(new File(TEST_IN,"setRecordDelimiter_RecordDelimiterInText_ModelSet_Ok.csv"));
		reader.setRecordDelimiter("\\");
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Bern", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Schw\\eiz", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"setRecordDelimiter_RecordDelimiterInText_ModelSet_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header gesetzt wird
	// - Delimiter gesetzt wird
	// - Record Delimiter gesetzt wird
	@Test
	public void setDelimiter_setRecordDelimiter_HeaderSet_Ok() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"setDelimiter_setRecordDelimiter_HeaderSet_Ok.csv"));
			writer.setValueDelimiter('|');
			writer.setValueSeparator('\\');
			writer.setModel(td);
			writer.setWriteHeader(false);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern");
			iomObj.setattrvalue("land", "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		CsvReader reader=new CsvReader(new File(TEST_IN,"setDelimiter_setRecordDelimiter_HeaderSet_Ok.csv"));
		reader.setRecordDelimiter("\\");
		reader.setDelimiter("|");
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Bern", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Schweiz", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"setDelimiter_setRecordDelimiter_HeaderSet_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header mit present gesetzt wird
	// - Delimiter gesetzt wird
	@Test
	public void setDelimiter_HeaderSet_Ok() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"setDelimiter_HeaderSet_Ok.csv"));
			writer.setValueDelimiter('|');
			writer.setModel(td);
			writer.setWriteHeader(true);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern");
			iomObj.setattrvalue("land", "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		CsvReader reader=new CsvReader(new File(TEST_IN,"setDelimiter_HeaderSet_Ok.csv"));
		reader.setDelimiter("|");
		reader.setHeader("present");
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ID));
        	assertEquals("Bern", iomObj.getattrvalue(STADT));
        	assertEquals("Schweiz", iomObj.getattrvalue(LAND));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"setDelimiter_HeaderSet_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Record Delimiter gesetzt wird
	@Test
	public void setRecordDelimiter_ModelSet_Ok() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"setRecordDelimiter_ModelSet_Ok.csv"));
			writer.setValueSeparator('\\');
			writer.setModel(td);
			writer.setWriteHeader(false);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern");
			iomObj.setattrvalue("land", "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		CsvReader reader=new CsvReader(new File(TEST_IN,"setRecordDelimiter_ModelSet_Ok.csv"));
		reader.setRecordDelimiter("\\");
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Bern", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Schweiz", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"setRecordDelimiter_ModelSet_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Mitten im Text ein Anfuehrungszeichen geschrieben ist
	@Test
	public void quoteMarkInText() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"quoteMarkInText.csv"));
			writer.setModel(td);
			writer.setWriteHeader(false);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern");
			iomObj.setattrvalue("land", "Schw\"eiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		CsvReader reader=new CsvReader(new File(TEST_IN,"quoteMarkInText.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Bern", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Schw\"eiz", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"quoteMarkInText.csv");
		if(file.exists()) {
			file.delete();
		}
	}

	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Carriage-Return und Line-Feed innerhalb des Textes geschrieben sind
	@Test
	public void carriageReturnLineFeedInText_Ok() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"carriageReturnLineFeedInText_Ok.csv"));
			writer.setModel(td);
			writer.setWriteHeader(false);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern\n\rZuerich");
			iomObj.setattrvalue("land", "Schweiz\n\rDeutschland");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		CsvReader reader=new CsvReader(new File(TEST_IN,"carriageReturnLineFeedInText_Ok.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Bern\r\nZuerich", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Schweiz\r\nDeutschland", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"carriageReturnLineFeedInText_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Carriage-Return innerhalb des Textes geschrieben ist
	@Test
	public void carriageReturnInText_Ok() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"carriageReturnInText_Ok.csv"));
			writer.setWriteHeader(false);
			writer.setModel(td);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern\r\nZuerich");
			iomObj.setattrvalue("land", "Schweiz\r\nDeutschland");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    	}
		}
		CsvReader reader=new CsvReader(new File(TEST_IN,"carriageReturnInText_Ok.csv"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof StartBasketEvent);
		IoxEvent event=reader.read();
		if(event instanceof ObjectEvent){
        	IomObject iomObj=((ObjectEvent)event).getIomObject();
        	assertEquals("10", iomObj.getattrvalue(ATTRIBUTE1));
        	assertEquals("Bern\r\nZuerich", iomObj.getattrvalue(ATTRIBUTE2));
        	assertEquals("Schweiz\r\nDeutschland", iomObj.getattrvalue(ATTRIBUTE3));
		}
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,"carriageReturnInText_Ok.csv");
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Model gesetzt wird
	// - Der Pfad in den die Datei erstellt werden soll, nicht existiert.	
	@Test
	public void pathtoCsvFileNotFound_Fail() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File("src/test/data/CsvWriter2","pathtoCsvFileNotFound_Fail.csv"));
			writer.setModel(td);
		}catch(Exception e) {
			assertTrue(e.getMessage().contains("path to create file not found"));
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    		// delete created file
	    		File file=new File(TEST_IN,"pathtoCsvFileNotFound_Fail.csv");
	    		if(file.exists()) {
	    			file.delete();
	    		}
	    	}
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Model gesetzt wird
	// - Die Attribute innerhalb des Models nicht gefunden werden
	@Test
	public void headerAttributesInModelNotFound_Fail() throws IoxException, FileNotFoundException {
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"headerAttributesInModelNotFound_Fail.csv"));
			writer.setModel(td);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("name", "10");
			iomObj.setattrvalue("vorname", "Bern");
			iomObj.setattrvalue("geburtstag", "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}catch(Exception e) {
			assertTrue(e.getMessage().contains("attrnames of model.Topic1.Class1 not valid"));
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    		// delete created file
	    		File file=new File(TEST_IN,"headerAttributesInModelNotFound_Fail.csv");
	    		if(file.exists()) {
	    			file.delete();
	    		}
	    	}
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Model gesetzt wird.
	// - Die Attribute-Namen des ersten Objektes gefunden werden.
	// - Die Attribute-Namen des zweiten Objektes nicht gefunden werden koennen.
	@Test
	public void attributesInModelNotFound_Fail() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		IomObject iomObj2=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"attributesInModelNotFound_Fail.csv"));
			writer.setModel(td);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern\rZuerich");
			iomObj.setattrvalue("land", "Schweiz\rDeutschland");
			writer.write(new ObjectEvent(iomObj));
			iomObj2=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj2.setattrvalue("name", "10");
			iomObj2.setattrvalue("vorname", "Bern");
			iomObj2.setattrvalue("geburtstag", "Schweiz");
			writer.write(new ObjectEvent(iomObj2));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}catch(Exception e) {
			assertTrue(e.getMessage().contains("attrnames of"));
			assertTrue(e.getMessage().contains(iomObj2.getobjecttag()));
			assertTrue(e.getMessage().contains("not valid"));
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    		// delete created file
	    		File file=new File(TEST_IN,"attributesInModelNotFound_Fail.csv");
	    		if(file.exists()) {
	    			file.delete();
	    		}
	    	}
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Model gesetzt wird.
	// - Die Klasse innerhalb des Modells nicht gefunden werden kann.
	@Test
	public void classInModelNotFound_Fail() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		IomObject iomObj=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"classInModelNotFound_Fail.csv"));
			writer.setModel(td);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			iomObj=new Iom_jObject("model.Topic1.ClassX","oid1");
			iomObj.setattrvalue("name", "10");
			iomObj.setattrvalue("vorname", "Bern");
			iomObj.setattrvalue("geburtstag", "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}catch(Exception e) {
			assertTrue(e.getMessage().contains("class"));
			assertTrue(e.getMessage().contains(iomObj.getobjecttag()));
			assertTrue(e.getMessage().contains("in model not found"));
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    		// delete created file
	    		File file=new File(TEST_IN,"classInModelNotFound_Fail.csv");
	    		if(file.exists()) {
	    			file.delete();
	    		}
	    	}
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Model nicht gesetzt wird.
	// - Die Attribute-Namen des ersten Objektes gefunden werden.
	// - Die Attribute-Namen des zweiten Objektes nicht gefunden werden koennen.
	@Test
	public void attributesNotFound_NoModel_Fail() throws IoxException, FileNotFoundException{
		CsvWriter writer=null;
		IomObject iomObj2=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,"attributesNotFound_NoModel_Fail.csv"));
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue("id", "10");
			iomObj.setattrvalue("stadt", "Bern\rZuerich");
			iomObj.setattrvalue("land", "Schweiz\rDeutschland");
			writer.write(new ObjectEvent(iomObj));
			iomObj2=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj2.setattrvalue("name", "10");
			iomObj2.setattrvalue("vorname", "Bern");
			iomObj2.setattrvalue("geburtstag", "Schweiz");
			writer.write(new ObjectEvent(iomObj2));
			writer.write(new EndBasketEvent());
			writer.write(new EndTransferEvent());
		}catch(IoxException e) {
			assertTrue(e.getMessage().contains("attrnames"));
			assertTrue(e.getMessage().contains("not equal to"));
		}finally {
	    	if(writer!=null) {
	    		try {
					writer.close();
				} catch (IoxException e) {
					throw new IoxException(e);
				}
	    		writer=null;
	    		// delete created file
	    		File file=new File(TEST_IN,"attributesNotFound_NoModel_Fail.csv");
	    		if(file.exists()) {
	    			file.delete();
	    		}
	    	}
		}
	}
	
}