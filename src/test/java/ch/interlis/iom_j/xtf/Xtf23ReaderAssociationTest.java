package ch.interlis.iom_j.xtf;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Test;

import ch.interlis.iom.IomObject;
import ch.interlis.iox.EndBasketEvent;
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.ObjectEvent;
import ch.interlis.iox.StartBasketEvent;
import ch.interlis.iox.StartTransferEvent;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class Xtf23ReaderAssociationTest {

	private final static String TEST_IN="src/test/data/Xtf23Reader/associations";
	
	// prueft, ob eine eingebettete Referenz gelesen werden kann.
	@Test
	public void embedded_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"Embedded_0to1.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        //Association.Mensch.Mann oid oid1 {}
        assertEquals("Association.Mensch.Mann", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
		
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Mensch.Frau oid oid2 {bezMann -> oid1 REF {}}
        assertEquals("Association.Mensch.Frau", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());		
        
        IomObject bezMann = iomObject.getattrobj("bezMann", 0);
        assertNotNull(bezMann);
        assertEquals("oid1", bezMann.getobjectrefoid());
        assertEquals("REF", bezMann.getobjecttag());
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
    @Test
    public void embeddedAssociationWithAttributes_Ok() throws Iox2jtsException, IoxException {
        Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"EmbeddedAssociationWithAttributes.xtf"));
        assertTrue(reader.read() instanceof  StartTransferEvent);
        assertTrue(reader.read() instanceof  StartBasketEvent);
        
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        //Association.Mensch.Mann oid oid1 {}
        assertEquals("Model.Topic.ClassA", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        assertEquals("Model.Topic.ClassB", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());     
        
        IomObject association = iomObject.getattrobj("rolle_A", 0);
        assertNotNull(association);
        assertEquals("12", association.getattrvalue("attr_Assoc"));
        
        assertTrue(reader.read() instanceof  EndBasketEvent);
        assertTrue(reader.read() instanceof  EndTransferEvent);
        reader.close();
        reader=null;
    }
	
	// prueft, ob eine eingebettete Referenz mit einer REF (oid, bid) gelesen werden kann.
	@Test
	public void embedded_ClassPathRef_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"Embedded_0to1_OidAndBid.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Mensch.Mann oid oid1 {}
        assertEquals("Association.Mensch.Mann", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Mensch.Frau oid oid2 {bezMann -> oid1 REF {}}
        assertEquals("Association.Mensch.Frau", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());     
        
        IomObject bezMann = iomObject.getattrobj("bezMann", 0);
        assertNotNull(bezMann);
        assertEquals("oid1", bezMann.getobjectrefoid());
        assertEquals("REF", bezMann.getobjecttag());
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// prueft, ob eine stand-alone Beziehung mit Attributen gelesen werden kann.
	@Test
	public void standAlone_WithAttributes_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"StandAlone_WithAttributes.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Mensch.Mann oid oid1 {}
        assertEquals("Association.Mensch.Mann", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Mensch.Frau oid oid2 {}
        assertEquals("Association.Mensch.Frau", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());     
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Mensch.Beziehung {attr1 text1, attr2 text2, attr3 text3, bezFrau -> oid2 REF {}, bezMann -> oid1 REF {}}
        assertEquals("Association.Mensch.Beziehung", iomObject.getobjecttag());
        
        assertEquals("text1", iomObject.getattrvalue("attr1"));
        assertEquals("text2", iomObject.getattrvalue("attr2"));
        assertEquals("text3", iomObject.getattrvalue("attr3"));
        IomObject bezMann = iomObject.getattrobj("bezMann", 0);
        assertNotNull(bezMann);
        assertEquals("oid1", bezMann.getobjectrefoid());
        assertEquals("REF", bezMann.getobjecttag());
        
        IomObject bezFrau = iomObject.getattrobj("bezFrau", 0);
        assertNotNull(bezFrau);
        assertEquals("oid2", bezFrau.getobjectrefoid());
        assertEquals("REF", bezFrau.getobjecttag());
		
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// prueft, ob eine SETORDERPOS Klasse gelesen werden kann.
	@Test
	public void setOrderPos_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"SetOrderPos.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Mensch.Mann1 oid oid1 {}
        assertEquals("Association.Mensch.Mann1", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Mensch.Frau2 oid oid2 {}
        assertEquals("Association.Mensch.Frau2", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid()); 
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Mensch.Mann3 oid oid3 {}
        assertEquals("Association.Mensch.Mann3", iomObject.getobjecttag());
        assertEquals("oid3", iomObject.getobjectoid());
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Mensch.Frau4 oid oid4 {}
        assertEquals("Association.Mensch.Frau4", iomObject.getobjecttag());
        assertEquals("oid4", iomObject.getobjectoid());
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Mensch.SETORDERPOS oid oid5 {role1 -> oid1, ORDER_POS 1 REF {}, role2 -> oid2, ORDER_POS 2 REF {}, role3 -> oid3, ORDER_POS 3 REF {}, role4 -> oid4, ORDER_POS 4 REF {}}
        assertEquals("Association.Mensch.SETORDERPOS", iomObject.getobjecttag());
        assertEquals("oid5", iomObject.getobjectoid());
        
        // Role 1
        IomObject role1 = iomObject.getattrobj("role1", 0);
        assertNotNull(role1);
        assertEquals("1", String.valueOf(role1.getobjectreforderpos()));
        assertEquals("oid1", role1.getobjectrefoid());
        assertEquals("bid", role1.getobjectrefbid());
        assertEquals("REF", role1.getobjecttag());
        
        // Role 2
        IomObject role2 = iomObject.getattrobj("role2", 0);
        assertNotNull(role2);
        assertEquals("2", String.valueOf(role2.getobjectreforderpos()));
        assertEquals("oid2", role2.getobjectrefoid());
        assertEquals("bid", role2.getobjectrefbid());
        assertEquals("REF", role2.getobjecttag());
        
        // Role 3
        IomObject role3 = iomObject.getattrobj("role3", 0);
        assertNotNull(role3);
        assertEquals("3", String.valueOf(role3.getobjectreforderpos()));
        assertEquals("oid3", role3.getobjectrefoid());
        assertEquals("bid", role3.getobjectrefbid());
        assertEquals("REF", role3.getobjecttag());
        
        // Role 4
        IomObject role4 = iomObject.getattrobj("role4", 0);
        assertNotNull(role4);
        assertEquals("4", String.valueOf(role4.getobjectreforderpos()));
        assertEquals("oid4", role4.getobjectrefoid());
        assertEquals("bid", role4.getobjectrefbid());
        assertEquals("REF", role4.getobjecttag());
		 
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// prueft, ob innerhalb einer eingebetteten Referenz eine Reihenfolge-Positionierung gelesen werden kann.
	@Test
	public void embedded_1to1_OrderPos_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"Embedded_1to1_OrderPos.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Transport.Schiff oid oid1 {}
        assertEquals("Association.Transport.Schiff", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Transport.Hafen oid oid2 {bezSchiff -> oid1, ORDER_POS 6 REF {}}
        assertEquals("Association.Transport.Hafen", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
        
        IomObject bezSchiff = iomObject.getattrobj("bezSchiff", 0);
        assertNotNull(bezSchiff);
        assertEquals("6", String.valueOf(bezSchiff.getobjectreforderpos()));
        assertEquals("oid1", bezSchiff.getobjectrefoid());
        assertEquals("REF", bezSchiff.getobjecttag());
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// prueft, ob eine Stand-Alone Beziehung gelesen werden kann.
	@Test
	public void standAlone_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"StandAlone.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Baum.Ast oid oid1 {}
        assertEquals("Association.Baum.Ast", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Baum.Blatt oid oid2 {}
        assertEquals("Association.Baum.Blatt", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
                
        // Association.Baum.Beziehung {bezAst -> oid1 REF {}, bezBlatt -> oid2 REF {}}
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        IomObject bezAst = iomObject.getattrobj("bezAst", 0);
        assertNotNull(bezAst);
        assertEquals("oid1", bezAst.getobjectrefoid());
        assertEquals("REF", bezAst.getobjecttag());

		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// prueft, ob Kommentare innerhalb von Associations richtig erkannt werden.
	@Test
	public void commentsInsideAssociation_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"CommentsInsideAssociation.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Fail1.ClassA oid oid1 {}
        assertEquals("Association.Fail1.ClassA", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Fail1.ClassB oid oid2 {}
        assertEquals("Association.Fail1.ClassB", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
		
        // Association.Fail1.Beziehung {roleA -> oid1 REF {}, roleB -> oid2 REF {}}
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        assertEquals("Association.Fail1.Beziehung", iomObject.getobjecttag());
        
        // roleA
        IomObject bezAst = iomObject.getattrobj("roleA", 0);
        assertNotNull(bezAst);
        assertEquals("oid1", bezAst.getobjectrefoid());
        assertEquals("REF", bezAst.getobjecttag());
        
        // roleB
        IomObject bezBlatt = iomObject.getattrobj("roleB", 0);
        assertNotNull(bezBlatt);
        assertEquals("oid2", bezBlatt.getobjectrefoid());
        assertEquals("REF", bezBlatt.getobjecttag()); 
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// prueft, ob beide Rollen auf die Klasse: ClassA mit der oid1 referenzieren.
	// Die Rolle roleB sollte auf die Klasse: ClassB mit der oid2 schauen.
	// Dieser Fehlerfall soll 1 zu 1 uebergeben werden und erst im ili-validator als Fehler ausgegeben werden.
	@Test
	public void sameTargetClass_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"SameTargetClass.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Fail1.ClassA oid oid1 {}
        assertEquals("Association.Fail1.ClassA", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Fail1.ClassB oid oid2 {}
        assertEquals("Association.Fail1.ClassB", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
        
        // Association.Fail1.Beziehung {roleA -> oid1 REF {}, roleB -> oid1 REF {}}
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        assertEquals("Association.Fail1.Beziehung", iomObject.getobjecttag());
        
        // roleA
        IomObject bezAst = iomObject.getattrobj("roleA", 0);
        assertNotNull(bezAst);
        assertEquals("oid1", bezAst.getobjectrefoid());
        assertEquals("REF", bezAst.getobjecttag());
        
        // roleB
        IomObject bezBlatt = iomObject.getattrobj("roleB", 0);
        assertNotNull(bezBlatt);
        assertEquals("oid1", bezBlatt.getobjectrefoid());
        assertEquals("REF", bezBlatt.getobjecttag());
		
 
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
		
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn ein Delete Object erstellt wird.
	@Test
	public void deleteObjectWithRef_Fail()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"Embedded_1to1_DeleteRef.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Autos.Auto oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Autos.Garage oid oid2 {bezAuto -> oid1 REF {}}
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("delete references are not yet implemented."));
	        assertTrue(ioxEx instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
}