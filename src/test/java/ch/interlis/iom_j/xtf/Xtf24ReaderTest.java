package ch.interlis.iom_j.xtf;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Before;
import org.junit.Test;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iox.EndBasketEvent;
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.ObjectEvent;
import ch.interlis.iox.StartBasketEvent;
import ch.interlis.iox.StartTransferEvent;
import ch.interlis.iox_j.IoxSyntaxException;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class Xtf24ReaderTest {

	private final static String TEST_IN="src/test/data/Xtf24Reader";
	private static final String SPACE=" ";
	private static final String START_ELE_FAIL="unexpected start element"+SPACE;
	private static final String END_ELE_FAIL="unexpected end element"+SPACE;
	private static final String CHAR_ELE_FAIL="unexpected characters"+SPACE;
	private TransferDescription td=null;
	
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"/Test1.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	// In diesem Test werden die folgenden Events auf ihre funktionalitaet getestet:
	// starttransfer, startdatasection, enddatasection, endtransfer, enddocument
	@Test
	public void testTransfer_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"EmptyTransfer.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// In diesem Test sollen Kommentare innerhalb der events erstellt werden.
	// Dabei sollen die Kommentare ignoriert, beziehungsweise gelesen und nicht interpretiert werden.
	@Test
	public void testComments_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"CommentsInFile.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// In diesem Test soll getestet werden, ob die topic (basketname, basket ID)
	// mit den richtigen Daten erstellt wird.
	@Test
	public void testEmptyBasket_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"EmptyBasket.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// In diesem Test soll getestet werden, ob die topic (basketname, basket ID)
	// mehrere Male erstellt werden kann, ohne dass dabei eine Fehlermeldung entsteht.
	@Test
	public void testMultipleBaskets_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"MultipleBaskets.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob leere Objekte ohne Syntaxfehler erstellt werden koennen.
	@Test
	public void testEmptyObjects_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"EmptyObjects.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); // Test1
		assertTrue(reader.read() instanceof  StartBasketEvent); // Test1.TopicA, bid1
		assertTrue(reader.read() instanceof  ObjectEvent); // Test1.TopicA.ClassA oid x21 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Test1.TopicA.ClassA oid x20 {}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob mehrere Baskets mit je 2 Objekten erstellt werden koennen.
	@Test
	public void testMultipleBasketsAndObjects_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"MultipleBasketsAndObjects.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); // Test1
		assertTrue(reader.read() instanceof  StartBasketEvent); // Test1.TopicA, bid1
		assertTrue(reader.read() instanceof  ObjectEvent); // Test1.TopicA.ClassA oid x21 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Test1.TopicA.ClassA oid x20 {}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent); // Test1.TopicB, bid2
		assertTrue(reader.read() instanceof  ObjectEvent); // Test1.TopicB.ClassB oid x31 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Test1.TopicB.ClassB oid x30 {}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent); // Test1.TopicC, bid3
		assertTrue(reader.read() instanceof  ObjectEvent); // Test1.TopicC.ClassC oid x41 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Test1.TopicC.ClassC oid x40 {}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
//	@Test
//	public void testSameTopicClassNames_Ok()  throws Iox2jtsException, IoxException {
//		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"TopicClassSameTest.xml"));
//		reader.setModel(td);
//		assertTrue(reader.read() instanceof  StartTransferEvent);
//		assertTrue(reader.read() instanceof  StartBasketEvent);
//		
//		IoxEvent event=reader.read();
//		assertTrue(event instanceof  ObjectEvent); // TopicZ.TopicZ
//		IomObject iomObjA=((ObjectEvent) event).getIomObject();
//		String attrtag=iomObjA.getobjecttag();
//		assertEquals("Ili23.TopicZ.TopicZ", attrtag);
//		
//		String oid=iomObjA.getobjectoid();
//		assertEquals("x10", oid);
//		
//		String attrValue=iomObjA.getattrvalue("attr1");
//		assertEquals("text", attrValue);
//		
//		assertTrue(reader.read() instanceof  EndBasketEvent);
//		assertTrue(reader.read() instanceof  EndTransferEvent);
//		reader.close();
//		reader=null;
//	}
	
	// In diesem Test soll getestet werden, ob eine SyntaxException ausgegeben wird,
	// wenn DataSection nicht erstellt wurde.
	@Test
	public void testNoDataSectionDefined_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"NoDataSectionDefined.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("expected data section"));
	        assertTrue(ioxEx instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
	
	// In diesem Test soll getestet werden, ob eine SyntaxException ausgegeben wird,
	// wenn DataSection mehrere Male hintereinander erstellt wurden.
	@Test
	public void testMultipleDataSectionsDefined_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"MultipleDataSectionDefined.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains(START_ELE_FAIL+"datasection"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// In diesem Test soll getestet werden, ob eine SyntaxException ausgegeben wird,
	// wenn die Basket Id falsch definiert wurde.
	@Test
	public void testWrongBasketId_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"WrongBasketId.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains(START_ELE_FAIL+"TopicA"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// In diesem Test soll getestet werden, ob eine SyntaxException ausgegeben wird,
	// wenn die Object Id falsch definiert wurde.
	@Test
	public void testWrongObjectId_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"WrongObjectId.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains(START_ELE_FAIL+"ClassA"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// In diesem Test wird der xtf Namespace http://www.interlis.ch/xtf/2.4/ falsch geschrieben.
	// http://www.interlis.ch/xtf/2.3/. Dies soll zu einem Syntaxfehler fuehren.
	@Test
	public void testWrongTopEleNamespace_Fail() throws IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"WrongTopEleNamespace.xml"));
		reader.setModel(td);
		try{
			reader.read();
	        fail();
	    }catch(IoxException ioxEx){
	        assertTrue((ioxEx).getMessage().contains(START_ELE_FAIL+"transfer"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
	    }
		reader.close();
		reader=null;
	}
	
	// In diesem Test soll nach dem Element: Transfer, ein Text definiert werden.
	// Welcher zu einem Syntaxfehler fuehren soll.
	@Test
	public void testUnexpectedCharacter_Fail() throws IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"UnexpectedCharacter.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof StartTransferEvent);
		try{
			reader.read();
	        fail();
	    }catch(IoxException ioxEx){
	        assertTrue((ioxEx).getMessage().contains(CHAR_ELE_FAIL+"failText"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
	    }
		reader.close();
		reader=null;
	}
	
	// In diesem Test soll getestet werden, ob eine SyntaxException ausgegeben wird,
	// wenn nach dem TransferEvent, noch einmal einen TransferEvent gestartet wird.
	@Test
	public void testUnexpectedEvent_Fail()  throws IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"UnexpectedEvent.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains(START_ELE_FAIL+"transfer"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// In diesem Test wird anstelle von transfer, transfe geschrieben. Dieser Event soll zu einer SyntaxException fuehren.
	@Test
	public void testWrongTopEleName_Fail()  throws IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"WrongTopEleName.xml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains(START_ELE_FAIL+"transfe"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
}