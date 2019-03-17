package ch.interlis.iom_j.xtf;

import static org.junit.Assert.*;
import java.io.File;
import java.util.Map;
import org.junit.Test;
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

public class Xtf23ReaderTest {

	private final static String TEST_IN="src/test/data/Xtf23Reader";

	// prueft ob das Dokument ohne Fehler validiert werden kann.
	@Test
	public void transferElement_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"ValidTransferElement.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein xtf file mit Textzeichen zwischen den Zeilen erkannt wird und eine Fehlermeldung ausgegeben wird.
	@Test
	public void testTextBetweenLines_Fail() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"TextBetweenLines.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		try{
			reader.read();
			fail();
		}catch(IoxException ex){
			assertTrue((ex).getMessage().contains("Unexpected XML event abcdefg"));
    		assertTrue(ex instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein xtf file auf 1 Linie ohne Fehler gelesen werden kann.
	@Test
	public void testXML1Line_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"Xml1Line.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn end-TRANSFER-tag
	// falsch geschrieben wurde: 'TRANSFERS'.
	@Test
	public void test_WrongSpelledEndTransferElement_False() throws IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"WrongSpelledEndTransferElement.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		try {
			reader.read();
			fail();
		}catch(IoxException ex) {
			assertTrue((ex).getMessage().contains("The end-tag for element type \"TRANSFER\" must end with a '>' delimiter."));
			assertTrue(ex instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn das start-TRANSFER-tag
	// falsch geschrieben wurde: 'TRANSFERS'.
	@Test
	public void test_WrongSpelledStartTransferElement_False() throws IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"WrongSpelledStartTransferElement.xtf"));
		try {
			reader.read();
			fail();
		}catch(IoxException ex) {
			assertTrue((ex).getMessage().contains("Unexpected XML event TRANSFERS found."));
			assertTrue(ex instanceof IoxException);
		}finally {
			reader.close();
			reader=null;
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn beim start-TRANSFER-tag: 'RIVELLA'
	// und beim end-TRANSFER-tag: 'RIVELLAS' geschrieben wurde.
	@Test
	public void test_CompleteOtherSpelledStartTransferElement_False() throws IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"CompleteOtherSpelledStartTransferElement.xtf"));
		try {
			reader.read();
			fail();
		}catch(IoxException ex) {
			assertTrue((ex).getMessage().contains("Unexpected XML event RIVELLA found."));
			assertTrue(ex instanceof IoxException);
		}finally {
			reader.close();
			reader=null;
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn das start-TRANSFER-tag
	// und das end-TRANSFER-tag falsch geschrieben wurde: 'TRANSFERS'.
	// Zusaetzlich stimmt das start-tag mit dem end-tag ueberein.
	@Test
	public void test_WrongSpelledStartAndEndTransferElement_False() throws IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"WrongSpelledStartAndEndTransferElement.xtf"));
		try {
			reader.read();
			fail();
		}catch(IoxException ex) {
			assertTrue((ex).getMessage().contains("Unexpected XML event TRANSFERS found."));
			assertTrue(ex instanceof IoxException);
		}finally {
			reader.close();
			reader=null;
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn der Text
	// vom start-TRANSFER-tag und vom end-TRANSFER-tag klein geschrieben wurde.
	@Test
	public void test_WrongCaseSensitiveTransferElement_False() throws IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"WrongCaseSensitiveTransferElement.xtf"));
		try {
			reader.read();
			fail();
		}catch(IoxException ex) {
			assertTrue((ex).getMessage().contains("Unexpected XML event transfer found."));
			assertTrue(ex instanceof IoxException);
		}finally {
			reader.close();
			reader=null;
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn das end-tag: TRANSFER
	// nicht mit einem '>' geschlossen wird.
	@Test
	public void test_InvalidFormatOfTransferElement_False() throws IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"InvalidFormatOfTransferElement.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		try {
			reader.read();
			fail();
		}catch(IoxException ex) {
			assertTrue((ex).getMessage().contains("XML document structures must start and end within the same entity."));
			assertTrue(ex instanceof IoxException);
		}finally {
			reader.close();
			reader=null;
		}
	}
	
	// Es wird getestet ob ein Delete Object mit einer tid als referenz erstellt werden kann.
	@Test
	public void test_DeleteObject_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"DeleteObject.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		// b1
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // DataTest1.TopicA.ClassA oid oidA {}
        assertEquals("DataTest1.TopicA.ClassA", iomObject.getobjecttag());
        assertEquals("oidA", iomObject.getobjectoid());
		
		//assertTrue(reader.read() instanceof  ObjectEvent); 
		assertTrue(reader.read() instanceof  EndBasketEvent);
		// b2
		assertTrue(reader.read() instanceof  StartBasketEvent);

        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // DataTest1.TopicA.ClassA oid oidA {}
        assertEquals("DataTest1.TopicC.ClassC", iomObject.getobjecttag());
        assertEquals("oidC", iomObject.getobjectoid());
		
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Objecte mit Consistencies erstellt werden kann.
	@Test
	public void test_Consistency_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"ObjectConsistencyMode.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // DataTest1.TopicA.ClassA oid oid1 {}
        assertEquals("DataTest1.TopicA.ClassA", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        

        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // DataTest1.TopicA.ClassA oid oid2 {}
        assertEquals("DataTest1.TopicA.ClassA", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // DataTest1.TopicA.ClassA oid oid3 {}
        assertEquals("DataTest1.TopicA.ClassA", iomObject.getobjecttag());
        assertEquals("oid3", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // DataTest1.TopicA.ClassA oid oid4 {}
        assertEquals("DataTest1.TopicA.ClassA", iomObject.getobjecttag());
        assertEquals("oid4", iomObject.getobjectoid());

		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Delete Object ohne tid erstellt werden kann.
	@Test
	public void test_DeleteObjectNoTid_Fail()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"DeleteObjectNoTid.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent); // Test1
		assertTrue(reader.read() instanceof  StartBasketEvent); // Test1.TopicA, bid1
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("delete object needs tid"));
	        assertTrue(ioxEx instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Object mit einem Basket welcher: StartState und EndState als Parameter beinhaltet.
	@Test
	public void test_StartEndState_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"StartAndEndState.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		StartBasketEvent startBasket=(StartBasketEvent) reader.read();
		String startstate=startBasket.getStartstate();
		assertEquals("state1", startstate); // startstate=state1
		String endstate=startBasket.getEndstate();
		assertEquals("state2", endstate); // endstate=state2
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// In diesem Test soll getestet werden, ob kind aus den Transferinformationen innerhalb von StartBasketEvent gesetzt werden kann.
	@Test
	public void test_BasketWithTransferKind_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"BasketWithTransferKind.xtf"));
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
	public void test_BasketWithDomains_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"BasketWithTopics.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		ch.interlis.iox_j.StartBasketEvent startBasket=(ch.interlis.iox_j.StartBasketEvent) reader.read();
		Map<String, String> transferDomain=startBasket.getDomains();
		assertTrue(transferDomain.containsKey("Test1.TopicA"));
		assertEquals("DataTest1.TopicA",transferDomain.get("Test1.TopicA"));
		assertTrue(transferDomain.containsKey("Test1.TopicB"));
		assertEquals("DataTest1.TopicA",transferDomain.get("Test1.TopicB"));
		assertTrue(transferDomain.containsKey("Test1.TopicC"));
		assertEquals("DataTest1.TopicA",transferDomain.get("Test1.TopicC"));
		assertTrue(transferDomain.containsKey("Test1.TopicD"));
		assertEquals("DataTest1.TopicA",transferDomain.get("Test1.TopicD"));
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// In diesem Test soll getestet werden, ob constency aus den Transferinformationen innerhalb von StartBasketEvent gesetzt werden kann.
	@Test
	public void test_BasketWithConsistency_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"BasketWithConsistency.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		StartBasketEvent startBasket=(StartBasketEvent) reader.read();
		int transferConsistency=startBasket.getConsistency();
		assertEquals(0, transferConsistency); // 0 == COMPLETE
		assertTrue(reader.read() instanceof  EndBasketEvent);
		startBasket=(StartBasketEvent) reader.read();
		transferConsistency=startBasket.getConsistency();
		assertEquals(1, transferConsistency); // 1 == INCOMPLETE
		assertTrue(reader.read() instanceof  EndBasketEvent);
		startBasket=(StartBasketEvent) reader.read();
		transferConsistency=startBasket.getConsistency();
		assertEquals(2, transferConsistency); // 2 == CONSISTENCY
		assertTrue(reader.read() instanceof  EndBasketEvent);
		startBasket=(StartBasketEvent) reader.read();
		transferConsistency=startBasket.getConsistency();
		assertEquals(3, transferConsistency); // 3 == ADAPTED
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// In diesem Test soll getestet werden, ob die operationen: INSERT, UPDATE und DELETE aus den Objectinformationen innerhalb dem Object gesetzt werden koennen.
	// Da das erste Objekt einen transferkind=FULL hat, werden keine operationen dem Objekt uebergeben. Die Standard Einstellung 0 bleibt bestehen.
	@Test
	public void test_ObjectOperationMode_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"ObjectOperationMode.xtf"));
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
}