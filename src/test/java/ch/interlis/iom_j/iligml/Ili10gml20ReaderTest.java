package ch.interlis.iom_j.iligml;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Before;
import org.junit.Test;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iox.*;

public class Ili10gml20ReaderTest {

	private final static String TEST_IN="src/test/data/Ili10gml20Reader";
	private TransferDescription td=null;
	
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"/Ili10.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	@Test
	public void testEmptyTransfer() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"EmptyTransfer.gml"));
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testWrongTopEleName() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"WrongTopEleName.gml"));
		try{
			reader.read();
			fail();
		}catch(IoxException ex){
			
		}
		reader.close();
		reader=null;
	}
	
	@Test
	public void testWrongTopEleNamespace() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"WrongTopEleNamespace.gml"));
		try{
			reader.read();
			fail();
		}catch(IoxException ex){
			
		}
		reader.close();
		reader=null;
	}
	
	@Test
	public void testEmptyBasket() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"EmptyBasket.gml"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent); //("Test1.TopicF","bid1"));
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testEmptyObjects() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"EmptyObjects.gml"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent); //("Test1.TopicF","bid1"));
		assertTrue(reader.read() instanceof  ObjectEvent); //(new Iom_jObject("Test1.TopicF.TableF1","x21")));
		assertTrue(reader.read() instanceof  ObjectEvent); //(new Iom_jObject("Test1.TopicF.TableF1","x20")));
		assertTrue(reader.read() instanceof  ObjectEvent); //(new Iom_jObject("Test1.TopicF.TableF0","x10")));
		assertTrue(reader.read() instanceof  ObjectEvent); //(new Iom_jObject("Test1.TopicF.TableF0","x11"))); 
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testPrimitiveDataType() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"PrimitiveAttrs.gml"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent); //("Test1.TopicA","bid1"));
		assertTrue(reader.read() instanceof  ObjectEvent); //TableA oid x10 {myText text example}
		assertTrue(reader.read() instanceof  ObjectEvent); //TableB oid x11 {myNumber 10}
		assertTrue(reader.read() instanceof  ObjectEvent); //TableC oid x12 {myBoolean true}
		assertTrue(reader.read() instanceof  ObjectEvent); //TableD oid x13 {myAlignment left}
		assertTrue(reader.read() instanceof  ObjectEvent); //TableE oid x14 {myFormatted 10.05.2017}
		assertTrue(reader.read() instanceof  ObjectEvent); //TableF oid x15 {myEnumeration one}
		assertTrue(reader.read() instanceof  ObjectEvent); //TableG oid x16 {myEnumTree one.one}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testUndefinedSurface() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"UndefinedSurface.gml"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		try{
			reader.read();
			fail();
		}catch(IoxException ex){
			
		}
		reader.close();
		reader=null;
	}
	
	@Test
	public void testSurface() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Surface.gml"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent); //("Test1.TopicA","bid1"));
		assertTrue(reader.read() instanceof  ObjectEvent); //(new Iom_jObject("Test1.TopicA.TableA","x10")));
		assertTrue(reader.read() instanceof  ObjectEvent); //(new Iom_jObject("Test1.TopicA.TableA","x11")));
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testUndefinedArea() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"UndefinedArea.gml"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		try{
			reader.read();
			fail();
		}catch(IoxException ex){
			
		}
		reader.close();
		reader=null;
	}
	
	@Test
	public void testArea() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Area.gml"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent); //("Test1.TopicA","bid1"));
		assertTrue(reader.read() instanceof  ObjectEvent);  //(new Iom_jObject("Test1.TopicA.TableA","x10")));
		assertTrue(reader.read() instanceof  ObjectEvent); //(new Iom_jObject("Test1.TopicA.TableA","x11")));
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testUndefinedReference() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"UndefinedReference.gml"));
		reader.close();
		reader=null;
	}
	
	@Test
	public void testReference() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Reference.gml"));
		reader.close();
		reader=null;
	}
}