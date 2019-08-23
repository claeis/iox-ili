package ch.interlis.iom_j.xtf;

import static org.junit.Assert.*;
import java.io.File;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.EndBasketEvent;
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxEvent;
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
	
	// Es wird getestet ob ein xtf file mit Textzeichen zwischen den Zeilen erkannt wird und eine Fehlermeldung ausgegeben wird.
	@Test
	public void testTextBetweenLines_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"TextBetweenLines.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		try{
			reader.read();
			fail();
		}catch(IoxException ex){
			assertTrue((ex).getMessage().contains(CHAR_ELE_FAIL+"text12345"));
    		assertTrue(ex instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein xtf file auf 1 Linie ohne Fehler gelesen werden kann.
	@Test
	public void testXML1Line_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Xml1Line.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// In diesem Test sollen Kommentare innerhalb der events erstellt werden.
	// Dabei sollen die Kommentare ignoriert, beziehungsweise gelesen und nicht interpretiert werden.
	@Test
	public void testComments_Ok() throws Iox2jtsException, IoxException {
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
	
	// Es wird getestet ob ein Delete Object mit einer tid als referenz erstellt werden kann.
	@Test
	public void testDeleteObject_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"DeleteObject.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); // Test1
		assertTrue(reader.read() instanceof  StartBasketEvent); // Test1.TopicA, bid1
		assertTrue(reader.read() instanceof  ObjectEvent); // delete oid=22
		assertTrue(reader.read() instanceof  ObjectEvent); // Test1.TopicA.ClassA oid 22 {}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Delete Object ohne tid erstellt werden kann.
	@Test
	public void testDeleteObjectNoTid_Fail()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"DeleteObjectNoTid.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); // Test1
		assertTrue(reader.read() instanceof  StartBasketEvent); // Test1.TopicA, bid1
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("ili:delete object needs tid"));
	        assertTrue(ioxEx instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Object mit einem Basket welcher: StartState und EndState als Parameter beinhalten.
	@Test
	public void testStartEndState_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"StartAndEndState.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		
		StartBasketEvent startBasket=(StartBasketEvent) reader.read(); // Test1.TopicA, bid1
		String startstate=startBasket.getStartstate();
		assertEquals("state1", startstate); // startstate=state1
		String endstate=startBasket.getEndstate();
		assertEquals("state2", endstate); // endstate=state2
		assertTrue(reader.read() instanceof  ObjectEvent); // Test1.TopicA.ClassA oid 12 {}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		
		StartBasketEvent startBasket3=(StartBasketEvent) reader.read(); // Test1.TopicA, bid1
		String startstate3=startBasket3.getStartstate();
		assertEquals("state2", startstate3); // startstate=state2
		String endstate4=startBasket3.getEndstate();
		assertEquals("state3", endstate4); // endstate=state3
		assertTrue(reader.read() instanceof  ObjectEvent); // Test1.TopicA.ClassA oid 23 {}
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
	
	// In diesem Test soll getestet werden, ob kind aus den Transferinformationen innerhalb von StartBasketEvent gesetzt werden kann.
	@Test
	public void testBasketWithTransferKind_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"BasketWithTransferKind.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		
		StartBasketEvent startBasket=(StartBasketEvent) reader.read();
		int transferKind=startBasket.getKind();
		assertEquals(0, transferKind); // transfer kind: 0 == FULL
		assertTrue(reader.read() instanceof  EndBasketEvent);
		
		startBasket=(StartBasketEvent) reader.read();
		transferKind=startBasket.getKind();
		assertEquals(1, transferKind); // transfer kind: 1 == UPDATE
		assertTrue(reader.read() instanceof  EndBasketEvent);
		
		startBasket=(StartBasketEvent) reader.read();
		transferKind=startBasket.getKind();
		assertEquals(2, transferKind); // transfer kind: 2 == INITIAL
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// In diesem Test soll getestet werden, ob die domains auf den Transferinformationen innerhalb von StartBasketEvent gesetzt werden koennen.
	@Test
	public void testBasketWithDomains_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"BasketWithDomains.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		ch.interlis.iox_j.StartBasketEvent startBasket=(ch.interlis.iox_j.StartBasketEvent) reader.read();
		Map<String, String> transferDomain=startBasket.getDomains();

		assertTrue(transferDomain.containsKey("Test1.TopicA.DOMAIN1"));
		assertEquals("Test1.TopicA.DOMAIN2",transferDomain.get("Test1.TopicA.DOMAIN1"));

		assertTrue(transferDomain.containsKey("Test1.TopicA.DOMAIN3"));
		assertEquals("Test1.TopicA.DOMAIN4",transferDomain.get("Test1.TopicA.DOMAIN3"));
		
		assertTrue(transferDomain.containsKey("Test1.TopicB.DOMAIN5"));
		assertEquals("Test1.TopicB.DOMAIN6",transferDomain.get("Test1.TopicB.DOMAIN5"));
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// In diesem Test soll getestet werden, ob constency aus den Transferinformationen innerhalb von StartBasketEvent gesetzt werden kann.
	@Test
	public void testBasketWithConsistency_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"BasketWithConsistency.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		
		StartBasketEvent startBasket=(StartBasketEvent) reader.read();
		int transferConsistency=startBasket.getConsistency();
		assertEquals(0, transferConsistency); // 0 == COMPLETE
		assertTrue(reader.read() instanceof  EndBasketEvent);
		
		startBasket=(StartBasketEvent) reader.read();
		transferConsistency=startBasket.getConsistency();
		assertEquals(1, transferConsistency); // 1 == INCOMPLETE
		assertTrue(reader.read() instanceof  EndBasketEvent);
		
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// In diesem Test soll getestet werden, ob die operationen: INSERT, UPDATE und DELETE aus den Objectinformationen innerhalb dem Object gesetzt werden koennen.
	// Da das Erste Objekt einen transferkind=FULL hat, werden keine operationen dem Objekt uebergeben. Die Standard Einstellung 0 bleibt bestehen.
	@Test
	public void testObjectOperationMode_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"ObjectOperationMode.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent);
		IomObject objectValue=((ObjectEvent) event).getIomObject();
		int operationMode=objectValue.getobjectoperation();
		assertEquals(0, operationMode); // 0 == INSERT
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent);
		objectValue=((ObjectEvent) event).getIomObject();
		operationMode=objectValue.getobjectoperation();
		assertEquals(1, operationMode); // 1 == UPDATE
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent);
		objectValue=((ObjectEvent) event).getIomObject();
		operationMode=objectValue.getobjectoperation();
		assertEquals(2, operationMode); // 2 == DELETE
		assertTrue(reader.read() instanceof  EndBasketEvent);
		
		assertTrue(reader.read() instanceof  StartBasketEvent);
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent);
		objectValue=((ObjectEvent) event).getIomObject();
		operationMode=objectValue.getobjectoperation();
		assertEquals(0, operationMode); // 0 == INSERT
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent);
		objectValue=((ObjectEvent) event).getIomObject();
		operationMode=objectValue.getobjectoperation();
		assertEquals(1, operationMode); // 1 == UPDATE
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent);
		objectValue=((ObjectEvent) event).getIomObject();
		operationMode=objectValue.getobjectoperation();
		assertEquals(2, operationMode); // 2 == DELETE
		assertTrue(reader.read() instanceof  EndBasketEvent);
		
		assertTrue(reader.read() instanceof  StartBasketEvent);
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent);
		objectValue=((ObjectEvent) event).getIomObject();
		operationMode=objectValue.getobjectoperation();
		assertEquals(0, operationMode); // 0 == INSERT
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent);
		objectValue=((ObjectEvent) event).getIomObject();
		operationMode=objectValue.getobjectoperation();
		assertEquals(1, operationMode); // 1 == UPDATE
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent);
		objectValue=((ObjectEvent) event).getIomObject();
		operationMode=objectValue.getobjectoperation();
		assertEquals(2, operationMode); // 2 == DELETE
		assertTrue(reader.read() instanceof  EndBasketEvent);
		
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// In diesem Test soll getestet werden, ob eine SyntaxException ausgegeben wird,
	// wenn DataSection nicht erstellt wurde.
	@Test
	public void testNoDataSectionDefined_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"NoDataSectionDefined.xml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains(END_ELE_FAIL+"transfer"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
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