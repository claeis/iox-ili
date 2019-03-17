package ch.interlis.iom_j.csv;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;

public class CsvWriterTest {
	private TransferDescription td=null;
	private static final String TEST_IN="src/test/data/CsvWriter";
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
	public void normalTest_Ok() throws IoxException, IOException{
		final String FILENAME="object_ModelSet_NoHeader_Ok.csv";
		CsvWriter writer=null;
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.write(new StartTransferEvent());
			writer.setModel(td);
			writer.setWriteHeader(false);
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schweiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
       	assertEquals("\"10\",\"Bern\",\"Schweiz\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header mit present gesetzt wird
	@Test
	public void object_ModelSet_SetHeaderPresent_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILENAME="object_ModelSet_SetHeaderPresent_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.write(new StartTransferEvent());
			writer.setModel(td);
			writer.setWriteHeader(true);
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schweiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
       	assertEquals("\"id\",\"stadt\",\"land\"", line);
       	line=reader.readLine();
       	assertEquals("\"10\",\"Bern\",\"Schweiz\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header mit absent gesetzt wird
	@Test
	public void object_ModelSet_SetHeaderAbsent_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILENAME="object_ModelSet_SetHeaderAbsent_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.write(new StartTransferEvent());
			writer.setModel(td);
			writer.setWriteHeader(false);
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schweiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
       	assertEquals("\"10\",\"Bern\",\"Schweiz\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell nicht gesetzt wird
	// - Der Header mit present gesetzt wird
	@Test
	public void object_NoModelSet_SetHeaderPresent_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILENAME="object_NoModelSet_SetHeaderPresent_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.write(new StartTransferEvent());
			writer.setWriteHeader(true);
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schweiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
       	assertEquals("\"id\",\"land\",\"stadt\"", line);
       	line=reader.readLine();
       	assertEquals("\"10\",\"Schweiz\",\"Bern\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
		if(file.exists()) {
			file.delete();
		}
	}
	@Test
	public void encoding_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILE_NAME = "encoding_Ok.csv";
		try {
			Settings settings=new Settings();
			settings.setValue(CsvReader.ENCODING, "UTF8");
			writer=new CsvWriter(new File(TEST_IN,FILE_NAME),settings);
			writer.write(new StartTransferEvent());
			writer.setWriteHeader(false);
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(LAND, "\u0402\u00A2");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILE_NAME)),"UTF-8"));
		String line=reader.readLine();
       	assertEquals("\"\u0402\u00A2\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILE_NAME);
		if(file.exists()) {
			file.delete();
		}
	}
	@Test
	public void attributeDefaultOrdering_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILE_NAME = "attributeDefaultOrdering_Ok.csv";
		try {
			Settings settings=new Settings();
			settings.setValue(CsvReader.ENCODING, "UTF8");
			writer=new CsvWriter(new File(TEST_IN,FILE_NAME),settings);
			writer.write(new StartTransferEvent());
			writer.setWriteHeader(false);
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schweiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILE_NAME)),"UTF-8"));
		String line=reader.readLine();
       	assertEquals("\"10\",\"Schweiz\",\"Bern\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILE_NAME);
		if(file.exists()) {
			file.delete();
		}
	}
	@Test
	public void attributeDefinedOrdering_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILE_NAME = "attributeDefinedOrdering_Ok.csv";
		try {
			Settings settings=new Settings();
			settings.setValue(CsvReader.ENCODING, "UTF8");
			writer=new CsvWriter(new File(TEST_IN,FILE_NAME),settings);
			writer.write(new StartTransferEvent());
			writer.setWriteHeader(false);
			writer.setAttributes(new String[] {ID,STADT,LAND});
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schweiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILE_NAME)),"UTF-8"));
		String line=reader.readLine();
       	assertEquals("\"10\",\"Bern\",\"Schweiz\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILE_NAME);
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell nicht gesetzt wird
	// - Der Header mit absent gesetzt wird
	@Test
	public void object_NoModelSet_SetHeaderAbsent_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILENAME="object_NoModelSet_SetHeaderAbsent_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.write(new StartTransferEvent());
			writer.setWriteHeader(false);
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schweiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
       	assertEquals("\"10\",\"Schweiz\",\"Bern\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell nicht gesetzt wird
	// - Der Header nicht gesetzt wird
	@Test
	public void object_NoModelSet_NoHeader_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILENAME="object_NoModelSet_NoHeader_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.setWriteHeader(false);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schweiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
       	assertEquals("\"10\",\"Schweiz\",\"Bern\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - 3 Modelle gesetzt werden
	// - Der Header mit present gesetzt wird
	@Test
	public void object_SetMultipleModels_SetHeaderPresent_Ok() throws IoxException, Ili2cFailure, IOException{
		// ili-datei lesen
		TransferDescription tdM=null;
		final String FILENAME="object_SetMultipleModels_SetHeaderPresent_Ok.csv";
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
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
       	assertEquals("\"id2\",\"stadt2\"", line);
       	line=reader.readLine();
       	assertEquals("\"10\",\"Bern\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header mit present gesetzt wird
	// - Mehrere Objekte erstellt werden
	@Test
	public void multipleObjects_ModelSet_SetHeaderPresent_Ok() throws IoxException, FileNotFoundException, IOException{
		CsvWriter writer=null;
		final String FILENAME="multipleObjects_ModelSet_SetHeaderPresent_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.write(new StartTransferEvent());
			writer.setModel(td);
			writer.setWriteHeader(true);
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			IomObject iomObj2=new Iom_jObject("model.Topic1.Class1","oid2");
			iomObj2.setattrvalue(ID, "11");
			iomObj2.setattrvalue(STADT, "Zuerich");
			iomObj2.setattrvalue(LAND, "Deutschland");
			writer.write(new ObjectEvent(iomObj2));
			IomObject iomObj3=new Iom_jObject("model.Topic1.Class1","oid3");
			iomObj3.setattrvalue(ID, "12");
			iomObj3.setattrvalue(STADT, "Luzern");
			iomObj3.setattrvalue(LAND, "Italien");
			writer.write(new ObjectEvent(iomObj3));
			IomObject iomObj4=new Iom_jObject("model.Topic1.Class1","oid4");
			iomObj4.setattrvalue(ID, "13");
			iomObj4.setattrvalue(STADT, "Genf");
			iomObj4.setattrvalue(LAND, "Oesterreich");
			writer.write(new ObjectEvent(iomObj4));
			IomObject iomObj5=new Iom_jObject("model.Topic1.Class1","oid5");
			iomObj5.setattrvalue(ID, "14");
			iomObj5.setattrvalue(STADT, "Chur");
			iomObj5.setattrvalue(LAND, "Spanien");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
       	assertEquals("\"id\",\"stadt\",\"land\"", line);
       	line=reader.readLine();
       	assertEquals("\"10\",\"Bern\",\"Schweiz\"", line);
       	line=reader.readLine();
       	assertEquals("\"11\",\"Zuerich\",\"Deutschland\"", line);
       	line=reader.readLine();
       	assertEquals("\"12\",\"Luzern\",\"Italien\"", line);
       	line=reader.readLine();
       	assertEquals("\"13\",\"Genf\",\"Oesterreich\"", line);
       	line=reader.readLine();
       	assertEquals("\"14\",\"Chur\",\"Spanien\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Mehrere Objekte erstellt werden
	@Test
	public void multipleObjects_ModelSet_NoHeaderSet_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILENAME="multipleObjects_ModelSet_NoHeaderSet_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.write(new StartTransferEvent());
			writer.setModel(td);
			writer.setWriteHeader(false);
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			IomObject iomObj2=new Iom_jObject("model.Topic1.Class1","oid2");
			iomObj2.setattrvalue(ID, "11");
			iomObj2.setattrvalue(STADT, "Zuerich");
			iomObj2.setattrvalue(LAND, "Deutschland");
			writer.write(new ObjectEvent(iomObj2));
			IomObject iomObj3=new Iom_jObject("model.Topic1.Class1","oid3");
			iomObj3.setattrvalue(ID, "12");
			iomObj3.setattrvalue(STADT, "Luzern");
			iomObj3.setattrvalue(LAND, "Italien");
			writer.write(new ObjectEvent(iomObj3));
			IomObject iomObj4=new Iom_jObject("model.Topic1.Class1","oid4");
			iomObj4.setattrvalue(ID, "13");
			iomObj4.setattrvalue(STADT, "Genf");
			iomObj4.setattrvalue(LAND, "Oesterreich");
			writer.write(new ObjectEvent(iomObj4));
			IomObject iomObj5=new Iom_jObject("model.Topic1.Class1","oid5");
			iomObj5.setattrvalue(ID, "14");
			iomObj5.setattrvalue(STADT, "Chur");
			iomObj5.setattrvalue(LAND, "Spanien");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
       	assertEquals("\"10\",\"Bern\",\"Schweiz\"", line);
       	line=reader.readLine();
       	assertEquals("\"11\",\"Zuerich\",\"Deutschland\"", line);
       	line=reader.readLine();
       	assertEquals("\"12\",\"Luzern\",\"Italien\"", line);
       	line=reader.readLine();
       	assertEquals("\"13\",\"Genf\",\"Oesterreich\"", line);
       	line=reader.readLine();
       	assertEquals("\"14\",\"Chur\",\"Spanien\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell nicht gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Mehrere Objekte erstellt werden
	@Test
	public void multipleObjects_NoModelSet_NoHeaderSet_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILENAME="multipleObjects_NoModelSet_NoHeaderSet_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.setWriteHeader(false);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schweiz");
			writer.write(new ObjectEvent(iomObj));
			IomObject iomObj2=new Iom_jObject("model.Topic1.Class1","oid2");
			iomObj2.setattrvalue(ID, "11");
			iomObj2.setattrvalue(STADT, "Zuerich");
			iomObj2.setattrvalue(LAND, "Deutschland");
			writer.write(new ObjectEvent(iomObj2));
			IomObject iomObj3=new Iom_jObject("model.Topic1.Class1","oid3");
			iomObj3.setattrvalue(ID, "12");
			iomObj3.setattrvalue(STADT, "Luzern");
			iomObj3.setattrvalue(LAND, "Italien");
			writer.write(new ObjectEvent(iomObj3));
			IomObject iomObj4=new Iom_jObject("model.Topic1.Class1","oid4");
			iomObj4.setattrvalue(ID, "13");
			iomObj4.setattrvalue(STADT, "Genf");
			iomObj4.setattrvalue(LAND, "Oesterreich");
			writer.write(new ObjectEvent(iomObj4));
			IomObject iomObj5=new Iom_jObject("model.Topic1.Class1","oid5");
			iomObj5.setattrvalue(ID, "14");
			iomObj5.setattrvalue(STADT, "Chur");
			iomObj5.setattrvalue(LAND, "Spanien");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
       	assertEquals("\"10\",\"Schweiz\",\"Bern\"", line);
       	line=reader.readLine();
       	assertEquals("\"11\",\"Deutschland\",\"Zuerich\"", line);
       	line=reader.readLine();
       	assertEquals("\"12\",\"Italien\",\"Luzern\"", line);
       	line=reader.readLine();
       	assertEquals("\"13\",\"Oesterreich\",\"Genf\"", line);
       	line=reader.readLine();
       	assertEquals("\"14\",\"Spanien\",\"Chur\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header mit present gesetzt wird
	// - Komma Zeichen innerhalb des Textes geschrieben sind
	@Test
	public void commaInText_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILENAME="commaInText_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.setModel(td);
			writer.setWriteHeader(true);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "1,0");
			iomObj.setattrvalue(STADT, "Be,rn");
			iomObj.setattrvalue(LAND, "S,chweiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
       	assertEquals("\"id\",\"stadt\",\"land\"", line);
       	line=reader.readLine();
       	assertEquals("\"1,0\",\"Be,rn\",\"S,chweiz\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
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
	public void setDelimiter_setRecordDelimiter_NoModelSet_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILENAME="setDelimiter_setRecordDelimiter_NoModelSet_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.setWriteHeader(false);
			writer.setValueDelimiter('|');
			writer.setValueSeparator('\\');
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schweiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
       	assertEquals("|10|\\|Schweiz|\\|Bern|", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell nicht gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Delimiter gesetzt wird
	@Test
	public void setDelimiter_NoModelSet_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILENAME="setDelimiter_NoModelSet_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.setValueDelimiter('|');
			writer.setWriteHeader(false);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schweiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
       	assertEquals("|10|,|Schweiz|,|Bern|", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell nicht gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Record Delimiter gesetzt wird
	@Test
	public void setRecordDelimiter_NoModelSet_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILENAME="setRecordDelimiter_NoModelSet_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.setWriteHeader(false);
			writer.setValueSeparator('\\');
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schweiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
       	assertEquals("\"10\"\\\"Schweiz\"\\\"Bern\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
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
		final String FILENAME="setDelimiter_setRecordDelimiter_ModelSet_DelimitersInText_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.setValueDelimiter('\\');
			writer.setValueSeparator('\\');
			writer.setModel(td);
			writer.setWriteHeader(false);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "B\\ern");
			iomObj.setattrvalue(LAND, "Schweiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
		assertEquals("\\10\\\\\\B\\\\ern\\\\\\Schweiz\\",line); // FIXME CsvReader fails
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
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
		final String FILENAME="setDelimiter_DelimiterInText_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.setValueDelimiter('|');
			writer.setModel(td);
			writer.setWriteHeader(false);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schw|eiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
		assertEquals("|10|,|Bern|,|Schw||eiz|", line); // FIXME CsvReader fails
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
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
	public void setRecordDelimiter_RecordDelimiterInText_ModelSet_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILENAME="setRecordDelimiter_RecordDelimiterInText_ModelSet_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.setValueSeparator('\\');
			writer.setModel(td);
			writer.setWriteHeader(false);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schw\\eiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
		assertEquals("\"10\"\\\"Bern\"\\\"Schw\\eiz\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
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
	public void setDelimiter_setRecordDelimiter_HeaderSet_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILENAME="setDelimiter_setRecordDelimiter_HeaderSet_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.setValueDelimiter('|');
			writer.setValueSeparator('\\');
			writer.setModel(td);
			writer.setWriteHeader(true);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schweiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
		assertEquals("|id|\\|stadt|\\|land|", line);
		line=reader.readLine();
		assertEquals("|10|\\|Bern|\\|Schweiz|", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header mit present gesetzt wird
	// - Delimiter gesetzt wird
	@Test
	public void setDelimiter_HeaderSet_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILENAME="setDelimiter_HeaderSet_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.setValueDelimiter('|');
			writer.setModel(td);
			writer.setWriteHeader(true);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schweiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
		assertEquals("|id|,|stadt|,|land|", line);
		line=reader.readLine();
		assertEquals("|10|,|Bern|,|Schweiz|", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Record Delimiter gesetzt wird
	@Test
	public void setRecordDelimiter_ModelSet_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILENAME="setRecordDelimiter_ModelSet_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.setValueSeparator('\\');
			writer.setModel(td);
			writer.setWriteHeader(false);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schweiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
		assertEquals("\"10\"\\\"Bern\"\\\"Schweiz\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Mitten im Text ein Anfuehrungszeichen geschrieben ist
	@Test
	public void quoteMarkInText() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILENAME="quoteMarkInText.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.setModel(td);
			writer.setWriteHeader(false);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern");
			iomObj.setattrvalue(LAND, "Schw\"eiz");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
		assertEquals("\"10\",\"Bern\",\"Schw\"\"eiz\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
		if(file.exists()) {
			file.delete();
		}
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn:
	// - Das Modell gesetzt wird
	// - Der Header nicht gesetzt wird
	// - Carriage-Return innerhalb des Textes geschrieben ist
	@Test
	public void newlineInText_Ok() throws IoxException, IOException{
		CsvWriter writer=null;
		final String FILENAME="carriageReturnInText_Ok.csv";
		try {
			writer=new CsvWriter(new File(TEST_IN,FILENAME));
			writer.setWriteHeader(false);
			writer.setModel(td);
			writer.write(new StartTransferEvent());
			writer.write(new StartBasketEvent("model.Topic1","bid1"));
			IomObject iomObj=new Iom_jObject("model.Topic1.Class1","oid1");
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern"+System.getProperty("line.separator")+"Zuerich");
			iomObj.setattrvalue(LAND, "Schweiz"+System.getProperty("line.separator")+"Deutschland");
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
		java.io.LineNumberReader reader=new java.io.LineNumberReader(new java.io.InputStreamReader(new java.io.FileInputStream(new File(TEST_IN,FILENAME)),"UTF-8"));
		String line=reader.readLine();
		assertEquals("\"10\",\"Bern", line);
		line=reader.readLine();
		assertEquals("Zuerich\",\"Schweiz", line);
		line=reader.readLine();
		assertEquals("Deutschland\"", line);
		reader.close();
		reader=null;
		// delete created file
		File file=new File(TEST_IN,FILENAME);
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
			assertTrue(e.getMessage().contains("could not create file"));
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
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern\rZuerich");
			iomObj.setattrvalue(LAND, "Schweiz\rDeutschland");
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
			iomObj.setattrvalue(ID, "10");
			iomObj.setattrvalue(STADT, "Bern\rZuerich");
			iomObj.setattrvalue(LAND, "Schweiz\rDeutschland");
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