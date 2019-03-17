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
import ch.interlis.iox_j.IoxSyntaxException;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class Xtf23ReaderDataTest {

	private final static String TEST_IN="src/test/data/Xtf23Reader/dataSection/";
	
	// prueft ob eine leere DataSection gelesen werden kann.
	@Test
	public void testDatasection_Empty_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"EmptyDataSection.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// prueft ob ein leerer Basket gelesen werden kann.
	@Test
	public void testBasket_Empty_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"EmptyBasket.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		IoxEvent event=reader.read();
		assertTrue(event instanceof StartBasketEvent);
		StartBasketEvent basket=(StartBasketEvent) event;
		assertEquals("DataTest1.TopicA", basket.getType());
		assertEquals("b1", basket.getBid());
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// prueft ob mehrere Baskets erstellt werden koennen.
	@Test
	public void testMultiBasket_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"MultiBaskets.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		IoxEvent event=reader.read();
		assertTrue(event instanceof StartBasketEvent);
		StartBasketEvent basket=(StartBasketEvent) event;
		assertEquals("DataTest1.TopicA", basket.getType());
		assertEquals("b1", basket.getBid());
		assertTrue(reader.read() instanceof  EndBasketEvent);
		event=reader.read();
		assertTrue(event instanceof StartBasketEvent);
		basket=(StartBasketEvent) event;
		assertEquals("DataTest1.TopicA", basket.getType());
		assertEquals("b2", basket.getBid());
		assertTrue(reader.read() instanceof  EndBasketEvent);
		event=reader.read();
		assertTrue(event instanceof StartBasketEvent);
		basket=(StartBasketEvent) event;
		assertEquals("DataTest1.TopicA", basket.getType());
		assertEquals("b3", basket.getBid());
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// prueft ob ein leeres Objekt gelesen werden kann.
	@Test
	public void testEmptyObjects_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"EmptyObjects.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
	    IoxEvent event = reader.read();
	    assertTrue(event instanceof  ObjectEvent);
	    IomObject iomObject = ((ObjectEvent) event).getIomObject();
	    
	    // DataTest1.TopicA.ClassA oid oid1 {}
        assertEquals("DataTest1.TopicA.ClassA", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        // DataTest1.TopicA.ClassA oid oid2 {}
        event = reader.read();
        assertTrue(event instanceof  ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        assertEquals("DataTest1.TopicA.ClassA", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
        
        // DataTest1.TopicA.ClassA oid oid3 {}
        event = reader.read();
        assertTrue(event instanceof  ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        assertEquals("DataTest1.TopicA.ClassA", iomObject.getobjecttag());
        assertEquals("oid3", iomObject.getobjectoid());
	         
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Boolean Werte ohne Fehler erstellt werden koennen.
	@Test
	public void testBooleanDataTypes_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"BooleanType.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		IoxEvent event=reader.read(); // DataTest1.TopicA.ClassC oid oidC {attrBoolean1 true, attrBoolean2 false}
		assertTrue(event instanceof ObjectEvent);
		ObjectEvent obj1=(ObjectEvent) event;
		assertEquals("DataTest1.TopicA.ClassC", obj1.getIomObject().getobjecttag());
		assertEquals("oidC", obj1.getIomObject().getobjectoid());
		assertEquals("true", obj1.getIomObject().getattrvalue("attrBoolean1"));
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Texte ohne Fehler gelesen werden koennen.
	@Test
	public void testTextType_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"TextTypes.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
	    IoxEvent event = reader.read();
	    assertTrue(event instanceof  ObjectEvent);
	    IomObject iomObject = ((ObjectEvent) event).getIomObject();
	    
		// DataTest1.TopicA.ClassA oid oidA {attrMText m text, attrName Randomname, attrText normal text, attrUri http://www.interlis.ch}
        assertEquals("DataTest1.TopicA.ClassA", iomObject.getobjecttag());
        assertEquals("oidA", iomObject.getobjectoid());		
        
        assertEquals("m text", iomObject.getattrvalue("attrMText"));
        assertEquals("Randomname", iomObject.getattrvalue("attrName"));
        assertEquals("\"normal text", iomObject.getattrvalue("attrText"));
        assertEquals("http://www.interlis.ch", iomObject.getattrvalue("attrUri"));
        
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
    // Es wird getestet ob Texte ohne Fehler gelesen werden koennen.
    @Test
    public void testTextTypes_WithEmptyLine_Ok()  throws Iox2jtsException, IoxException {
        Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"TextTypesWithEmptyLine.xtf"));
        assertTrue(reader.read() instanceof  StartTransferEvent);
        assertTrue(reader.read() instanceof  StartBasketEvent);
        
        IoxEvent event = reader.read();
        assertTrue(event instanceof  ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // DataTest1.TopicA.ClassA oid oidA {attrLine1 null, attrLine2 " "}
        assertEquals("DataTest1.TopicA.ClassA", iomObject.getobjecttag());
        assertEquals("oidA", iomObject.getobjectoid());     
        
        assertEquals(null, iomObject.getattrvalue("attrLine1"));
        assertEquals(" ", iomObject.getattrvalue("attrLine2"));
        
        assertTrue(reader.read() instanceof  EndBasketEvent);
        assertTrue(reader.read() instanceof  EndTransferEvent);
        reader.close();
        reader=null;
    }
	
	// Es wird getestet ob Aufzaehlungen ohne Fehler erstellt werden koennen.
	@Test
	public void testEnumerationType_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"EnumerationTypes.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		IoxEvent event = reader.read();
		assertTrue(event instanceof  ObjectEvent);
		IomObject iomObject = ((ObjectEvent) event).getIomObject();
		// DataTest1.TopicA.ClassF oid oidF1 {attrF1 rot.dunkelrot, attrF2 unten, attrF3 Werktage.Montag, attrF4 Werktage.Dienstag}
		assertEquals("DataTest1.TopicA.ClassF", iomObject.getobjecttag());
        assertEquals("oidF1", iomObject.getobjectoid());
        
        assertEquals("rot.dunkelrot", iomObject.getattrvalue("attrF1"));
        assertEquals("unten", iomObject.getattrvalue("attrF2"));
        assertEquals("Werktage.Montag", iomObject.getattrvalue("attrF3"));
        assertEquals("Werktage.Dienstag", iomObject.getattrvalue("attrF4"));
        
        // DataTest1.TopicA.ClassF oid oidF2 {attrF1 gruen.hellgruen, attrF2 mitte, attrF3 Werktage.Sonntag, attrF4 Werktage.Samstag}
        event = reader.read();
        iomObject = ((ObjectEvent) event).getIomObject();
        assertEquals("DataTest1.TopicA.ClassF", iomObject.getobjecttag());
        assertEquals("oidF2", iomObject.getobjectoid());
        
        assertEquals("gruen.hellgruen", iomObject.getattrvalue("attrF1"));
        assertEquals("mitte", iomObject.getattrvalue("attrF2"));
        assertEquals("Werktage.Sonntag", iomObject.getattrvalue("attrF3"));
        assertEquals("Werktage.Samstag", iomObject.getattrvalue("attrF4"));
        
        // DataTest1.TopicA.ClassF oid oidF3 {attrG1 rot, attrG2 rot.dunkelrot, attrG3 rot}
        event = reader.read();
        iomObject = ((ObjectEvent) event).getIomObject();
        assertEquals("DataTest1.TopicA.ClassF", iomObject.getobjecttag());
        assertEquals("oidF3", iomObject.getobjectoid());
        
        assertEquals("rot", iomObject.getattrvalue("attrG1"));
        assertEquals("rot.dunkelrot", iomObject.getattrvalue("attrG2"));
        assertEquals("rot", iomObject.getattrvalue("attrG3"));
        
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Oid's ohne Fehler erstellt werden koennen.
	@Test
	public void testOidType_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"OidTypes.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		IoxEvent event = reader.read();
		assertTrue(event instanceof  ObjectEvent);
		IomObject iomObject = ((ObjectEvent) event).getIomObject();
		assertEquals("DataTest1.TopicA.ClassH", iomObject.getobjecttag());
		assertEquals("oidH", iomObject.getobjectoid());

		assertEquals("5kidok-_", iomObject.getattrvalue("attrH1"));
		assertEquals("igjH-m_", iomObject.getattrvalue("attrH2"));
		assertEquals("1234", iomObject.getattrvalue("attrH3"));
		assertEquals("Interlis12345", iomObject.getattrvalue("attrH4"));
		assertEquals("123e4567-e89b-12d3-a456-426655440000", iomObject.getattrvalue("attrH5"));
		
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob die OID= mit einer Value eine Fehlermeldung ausgiebt.
	@Test
	public void testOidType_Fail()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"OidTypesFail.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		try{
			reader.read();
			fail();
		}catch(IoxSyntaxException ex){
			assertTrue((ex).getMessage().contains("Unexpected XML event 12345 found."));
		}
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Datum und Zeit Typen ohne Fehler erstellt werden koennen.
	@Test
	public void testDateAndTimeType_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"DateTimeTypes.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
	    IoxEvent event = reader.read();
	    assertTrue(event instanceof  ObjectEvent);
	    IomObject iomObject = ((ObjectEvent) event).getIomObject();
	    
	    // DataTest1.TopicA.ClassI oid oidI {attrI1 2005-12-31T23:59:59.999, attrI2 2002-01-01T00:00:00.000, attrI3 2002-12-10, attrI4 12:23:47.111}
	    assertEquals("DataTest1.TopicA.ClassI", iomObject.getobjecttag());
	    assertEquals("oidI", iomObject.getobjectoid());
	    
	    assertEquals("2005-12-31T23:59:59.999", iomObject.getattrvalue("attrI1"));
	    assertEquals("2002-01-01T00:00:00.000", iomObject.getattrvalue("attrI2"));
	    assertEquals("2002-12-10", iomObject.getattrvalue("attrI3"));
	    assertEquals("12:23:47.111", iomObject.getattrvalue("attrI4"));
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Black Box Typen ohne Fehler erstellt werden koennen.
	@Test
	public void testBlackBoxType_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"BlackBoxTypes.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		IoxEvent event = reader.read();
		assertTrue(event instanceof  ObjectEvent);
		IomObject iomObject = ((ObjectEvent) event).getIomObject();
        assertEquals("AAAA",iomObject.getattrvalue("attrBin"));
        assertEquals("<anyXml xmlns=\"http://www.interlis.ch/INTERLIS2.3\"></anyXml>",iomObject.getattrvalue("attrXml"));
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Nummern ohne Fehler erstellt werden koennen.
	@Test
	public void testNumericDataTypes_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"NumericTypes.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event = reader.read();
		assertTrue(event instanceof  ObjectEvent);
		IomObject iomObject = ((ObjectEvent) event).getIomObject();
		
		// DataTest1.TopicA.ClassB oid oidB {attrNrDec 6.15, attrWertExakt 6.15, attrWertNormal 6.15}
        assertEquals("DataTest1.TopicA.ClassB", iomObject.getobjecttag());
        assertEquals("oidB", iomObject.getobjectoid());
        
        assertEquals("6.15", iomObject.getattrvalue("attrNrDec"));
        assertEquals("6.15", iomObject.getattrvalue("attrWertExakt"));
        assertEquals("6.15", iomObject.getattrvalue("attrWertNormal"));
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	// Es wird getestet ob Ausrichtungen ohne Fehler erstellt werden koennen.
	@Test
	public void testAlignmentDataTypes_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"AlignmentTypes.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
		
		// DataTest1.TopicA.ClassD oid oidD {attrH Center, attrV Cap}
        assertEquals("DataTest1.TopicA.ClassD", iomObject.getobjecttag());
        assertEquals("oidD", iomObject.getobjectoid());
        
        assertEquals("Center", iomObject.getattrvalue("attrH"));
        assertEquals("Cap", iomObject.getattrvalue("attrV"));
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob formatierte Werte ohne Fehler erstellt werden koennen.
	@Test
	public void testFormattedDataTypes_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"FormattedType.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);

        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        // DataTest1.TopicA.ClassE oid oidE {attrDate 2003-02-03, attrDateTime 2007-12-31T23:59:59.999, attrTime 23:59:59.999}
        assertEquals("DataTest1.TopicA.ClassE", iomObject.getobjecttag());
        assertEquals("oidE", iomObject.getobjectoid());
        
        assertEquals("2003-02-03", iomObject.getattrvalue("attrDate"));
        assertEquals("2007-12-31T23:59:59.999", iomObject.getattrvalue("attrDateTime"));
        assertEquals("23:59:59.999", iomObject.getattrvalue("attrTime"));
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();

        // DataTest1.TopicA.ClassK oid oidK {attrK1 2003-02-03}
        assertEquals("DataTest1.TopicA.ClassK", iomObject.getobjecttag());
        assertEquals("oidK", iomObject.getobjectoid());
        
        assertEquals("2003-02-03", iomObject.getattrvalue("attrK1"));
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Strukturen ohne Fehler erstellt werden koennen.
	@Test
	public void testStructureType_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"Structures.xtf"));
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
        
        // DataTest1.TopicA.ClassB oid oidL {attrA DataTest1.TopicA.StructA {attrB DataTest1.TopicA.StructB {attrC DataTest1.TopicA.StructC {attr1 textC1, attr2 textC2, role1 -> oid1 REF {}}, attrC2 textC2, roleC1 -> oid1 REF {}}, attrB2 textC2, roleB1 -> oid1 REF {}}, attrA2 textC2, roleA1 -> oid1 REF {}}
        assertEquals("DataTest1.TopicA.ClassB", iomObject.getobjecttag());
        assertEquals("oidL", iomObject.getobjectoid());
        
        // RoleA1
        IomObject roleA1 = iomObject.getattrobj("roleA1", 0);
        assertNotNull(roleA1);
        assertEquals("oidA1", roleA1.getobjectrefoid());
        assertEquals("REF", roleA1.getobjecttag());
        
        // AttrA
        IomObject attrA = iomObject.getattrobj("attrA", 0);
        assertEquals("DataTest1.TopicA.StructA", attrA.getobjecttag());
        assertNotNull(attrA);
        
        
        // AttrA -> RoleB1 
        IomObject roleB1 = attrA.getattrobj("roleB1", 0);
        assertEquals("oidB1", roleB1.getobjectrefoid());
        assertEquals("REF", roleB1.getobjecttag());
        
        // AttrA -> attrB2
        assertEquals("textB2", attrA.getattrvalue("attrB2"));
        
        // AttrA -> attrB
        IomObject attrB = attrA.getattrobj("attrB", 0);
        assertNotNull(attrB);
        
        // AttrA -> attrB -> roleC1 
        IomObject roleC1 = attrB.getattrobj("roleC1", 0);
        assertEquals("oidC1", roleC1.getobjectrefoid());
        assertEquals("REF", roleC1.getobjecttag());
        
        // AttrA -> attrB -> attrC
        IomObject attrC = attrB.getattrobj("attrC", 0);
        assertNotNull(attrC);
        
        // AttrA -> attrB -> attrC -> role1
        IomObject role1 = attrC.getattrobj("role1", 0);
        assertNotNull(role1);
        assertEquals("oidStr1", role1.getobjectrefoid());
        assertEquals("REF", role1.getobjecttag());        
        
        // AttrA -> attrB -> attrC -> attr2
        assertEquals("textStr2", attrC.getattrvalue("attr2"));
        
        // AttrA -> attrB -> attrC -> attr1
        assertEquals("textStr1", attrC.getattrvalue("attr1"));
        
        // AttrA -> attrB -> attrC2
        assertEquals("textC2", attrB.getattrvalue("attrC2"));
        
        // IomObject -> AttrA2
        assertEquals("textA2", iomObject.getattrvalue("attrA2"));
        
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Strukturen mit Attributen/Rollen und Referenzen ohne Fehler erstellt werden koennen.
	@Test
	public void testStructure2Type_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"Structures2.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		// 1. ObjectEvent
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // DataTest1.TopicA.ClassA oid oid1 {}
        assertEquals("DataTest1.TopicA.ClassA", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        // 2. ObjectEvent
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // DataTest1.TopicA.ClassL oid oidL {attrA DataTest1.TopicA.StructA {attrB2 textC2, attrB3 COORD {C1 480001, C2 70001, C3 5001}, roleB1 -> oid1 REF {}}, attrA2 textC2, attrA3 COORD {C1 480002, C2 70002, C3 5002}, attrA4 COORD {C1 480003, C2 70003, C3 5003}, roleA1 -> oid1 REF {}, roleA2 -> oid1 REF {}}
        assertEquals("DataTest1.TopicA.ClassL", iomObject.getobjecttag());
        assertEquals("oidL", iomObject.getobjectoid());
		
        // RoleA2
        IomObject roleA2 = iomObject.getattrobj("roleA2", 0);
        assertNotNull(roleA2);
        assertEquals("oid1", roleA2.getobjectrefoid());
        assertEquals("REF", roleA2.getobjecttag());
        
        // RoleA1
        IomObject roleA1 = iomObject.getattrobj("roleA1", 0);
        assertNotNull(roleA1);
        assertEquals("oid1", roleA1.getobjectrefoid());
        assertEquals("REF", roleA1.getobjecttag());
        
        // AttrA4
        IomObject attrA4 = iomObject.getattrobj("attrA4", 0);
        // AttrA4 -> C1, C2, C3
        assertEquals("480003", attrA4.getattrvalue("C1"));
        assertEquals("70003", attrA4.getattrvalue("C2"));
        assertEquals("5003", attrA4.getattrvalue("C3"));
 
        // AttrA3
        IomObject attrA3 = iomObject.getattrobj("attrA3", 0);
        // AttrA3 -> C1, C2, C3
        assertEquals("480002", attrA3.getattrvalue("C1"));
        assertEquals("70002", attrA3.getattrvalue("C2"));
        assertEquals("5002", attrA3.getattrvalue("C3"));
        
        // AttrA -> roleB1
        IomObject attrA = iomObject.getattrobj("attrA", 0);
        IomObject roleB1 = attrA.getattrobj("roleB1", 0);
        assertNotNull(roleB1);
        assertEquals("oid1", roleB1.getobjectrefoid());
        assertEquals("REF", roleB1.getobjecttag());
        
        // AttrA -> attrB3
        IomObject attrB3 = attrA.getattrobj("attrB3", 0);
        // AttrB3 -> C1, C2, C3
        assertEquals("480001", attrB3.getattrvalue("C1"));
        assertEquals("70001", attrB3.getattrvalue("C2"));
        assertEquals("5001", attrB3.getattrvalue("C3"));
        
        // AttrA -> attrA2
        assertEquals("textC2", iomObject.getattrvalue("attrA2"));
        
        assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Referenzen auf eine OID und/oder eine Klasse ohne Fehler referenzieren.
	@Test
	public void testReferenceAttrType_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"References.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
	    // 1. ObjectEvent
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // DataTest1.TopicA.ClassM oid oidM {attrM1 textM1}
        assertEquals("DataTest1.TopicA.ClassM", iomObject.getobjecttag());
        assertEquals("oidM", iomObject.getobjectoid());
        
        assertEquals("textM1", iomObject.getattrvalue("attrM1"));
        
        // 2. ObjectEvent
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // DataTest1.TopicA.ClassL oid oidL {attrA DataTest1.TopicA.StructA {attrB DataTest1.TopicA.StructB {attrC DataTest1.TopicA.StructC {ref1 -> oidM REF {}, ref2 -> DataTest1.TopicA.ClassM REF {}}}}}
        assertEquals("DataTest1.TopicA.ClassL", iomObject.getobjecttag());
        assertEquals("oidL", iomObject.getobjectoid());
        
        // attrB
        IomObject attrA = iomObject.getattrobj("attrA", 0);
        IomObject attrB = attrA.getattrobj("attrB", 0);
        IomObject attrC = attrB.getattrobj("attrC", 0);
        
        IomObject ref1 = attrC.getattrobj("ref1", 0);
        assertNotNull(ref1);
        assertEquals("oidM", ref1.getobjectrefoid());
        assertEquals("REF", ref1.getobjecttag());
        
        IomObject ref2 = attrC.getattrobj("ref2", 0);
        assertNotNull(ref2);
        assertEquals("DataTest1.TopicA.ClassM", ref2.getobjectrefoid());
        assertEquals("REF", ref2.getobjecttag());
        
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Attributepfade ohne Fehler erstellt werden koennen.
	@Test
	public void testAttributePath_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"AttrpathType.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // DataTest1.TopicB.ClassAP1 oid oid1 {attr1 attribute1}
        assertEquals("DataTest1.TopicB.ClassAP1", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        assertEquals("attribute1", iomObject.getattrvalue("attr1"));
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Coordinaten ohne Fehler erstellt werden koennen.
	@Test
	public void testCoords_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"Coord.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); 
		
	    // 1. ObjectEvent
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        assertEquals("DataTest1.TopicB.Coord2D", iomObject.getobjecttag());
        assertEquals("oid2D", iomObject.getobjectoid());
        
        IomObject attr2 = iomObject.getattrobj("attr2", 0);
        assertEquals("480000.000", attr2.getattrvalue("C1"));
        assertEquals("70000.000", attr2.getattrvalue("C2"));
        
        // 2. ObjectEvent
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();

        assertEquals("DataTest1.TopicB.Coord3D", iomObject.getobjecttag());
        assertEquals("oid3D", iomObject.getobjectoid());
        
        IomObject attr3 = iomObject.getattrobj("attr3", 0);
        assertEquals("480000.000", attr3.getattrvalue("C1"));
        assertEquals("70000.000", attr3.getattrvalue("C2"));
        assertEquals("5000.000", attr3.getattrvalue("C3"));

		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob eine Gerade vom Typ Polyline ohne Fehler gelesen werden kann.
	@Test
	public void testPolylinesWithStraights_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"PolylineWithStraights.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        assertEquals("DataTest1.TopicB.ClassN", iomObject.getobjecttag());
        assertEquals("oidN", iomObject.getobjectoid());
        
        // DataTest1.TopicB.ClassN oid oidN {attrN1 POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.000, C2 70000.000, C3 5000.000}, COORD {C1 490000.000, C2 80000.000, C3 5000.000}]}}}
        IomObject attrN1 = iomObject.getattrobj("attrN1", 0);
        assertNotNull(attrN1);
        IomObject sequence = attrN1.getattrobj("sequence", 0);
        assertNotNull(sequence);
        IomObject firstSegment = sequence.getattrobj("segment", 0);
        assertNotNull(firstSegment);
        
        assertEquals("480000.000", firstSegment.getattrvalue("C1"));
        assertEquals("70000.000", firstSegment.getattrvalue("C2"));
        assertEquals("5000.000", firstSegment.getattrvalue("C3"));        
        
        IomObject secondSegment = sequence.getattrobj("segment", 1);
        assertNotNull(secondSegment);
        
        assertEquals("490000.000", secondSegment.getattrvalue("C1"));
        assertEquals("80000.000", secondSegment.getattrvalue("C2"));
        assertEquals("5000.000", secondSegment.getattrvalue("C3"));        
        
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Bogen vom Typ Polyline ohne Fehler gelesen werden kann.
	@Test
	public void testPolylinesWithArcs_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"PolylineWithArcs.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        assertEquals("DataTest1.TopicB.ClassO", iomObject.getobjecttag());
        assertEquals("oidO", iomObject.getobjectoid());
        
        // DataTest1.TopicB.ClassO oid oidO {attrO1 POLYLINE {sequence SEGMENTS {segment ARC {A1 480005.000, A2 70005.000, C1 480000.000, C2 70000.000, C3 5000.000}}}}
        IomObject attrO1 = iomObject.getattrobj("attrO1", 0);
        assertNotNull(attrO1);
        IomObject sequence = attrO1.getattrobj("sequence", 0);
        assertNotNull(sequence);
        IomObject segment = sequence.getattrobj("segment", 0);
        assertNotNull(segment);
        
        assertEquals("480005.000", segment.getattrvalue("A1"));
        assertEquals("70005.000", segment.getattrvalue("A2"));
        assertEquals("5000.000", segment.getattrvalue("C3"));
        assertEquals("480000.000", segment.getattrvalue("C1"));
        assertEquals("70000.000", segment.getattrvalue("C2"));
        
		assertTrue(reader.read() instanceof EndBasketEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Bogen mit einem Radius vom Typ Polyline ohne Fehler gelesen werden kann.
	@Test
	public void testPolylinesWithArcsRadius_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"PolylineWithArcsRadius.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        assertEquals("DataTest1.TopicB.ClassO", iomObject.getobjecttag());
        assertEquals("oidO", iomObject.getobjectoid());
        
        // DataTest1.TopicB.ClassO oid oidO {attrO1 POLYLINE {sequence SEGMENTS {segment ARC {A1 480001.000, A2 70001.000, C1 480000.000, C2 70000.000, C3 5000.000, R 45}}}}
        IomObject attrO1 = iomObject.getattrobj("attrO1", 0);
        assertNotNull(attrO1);
        IomObject sequence = attrO1.getattrobj("sequence", 0);
        assertNotNull(sequence);
        IomObject segment = sequence.getattrobj("segment", 0);
        assertNotNull(segment);
		
        assertEquals("480001.000", segment.getattrvalue("A1"));
        assertEquals("70001.000", segment.getattrvalue("A2"));
        assertEquals("5000.000", segment.getattrvalue("C3"));
        assertEquals("480000.000", segment.getattrvalue("C1"));
        assertEquals("70000.000", segment.getattrvalue("C2"));
        assertEquals("45", segment.getattrvalue("R"));
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Surface mit mehreren boundaries ohne Fehler gelesen werden kann.
	@Test
	public void testSurface_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"Surface.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        assertEquals("DataTest1.TopicB.ClassQ", iomObject.getobjecttag());
        assertEquals("oidQ", iomObject.getobjectoid());
        
        IomObject formQ = iomObject.getattrobj("formQ", 0);
        assertNotNull(formQ);

        assertEquals("MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.011, C2 70000.011, C3 5000.011}, COORD {C1 490000.012, C2 80000.012, C3 5000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.021, C2 70000.021, C3 5000.021}, COORD {C1 490000.022, C2 80000.022, C3 5000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.031, A2 70000.031, C1 480000.031, C2 70000.031, C3 5000.031, R 31}, COORD {C1 480000.032, C2 70000.032, C3 5000.032}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.041, C2 70000.041, C3 5000.041}, COORD {C1 490000.042, C2 80000.042, C3 5000.042}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.211, C2 70000.211, C3 5000.211}, COORD {C1 490000.212, C2 80000.212, C3 5000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.221, C2 70000.221, C3 5000.221}, COORD {C1 490000.222, C2 80000.222, C3 5000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.231, A2 70000.231, C1 480000.231, C2 70000.231, C3 5000.231, R 31}, COORD {C1 480000.232, C2 70000.232, C3 5000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.241, C2 70000.241, C3 5000.241}, COORD {C1 490000.242, C2 80000.242, C3 5000.242}]}}]}]}}",
                formQ.toString());

		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Surface mit mehreren boundaries und mehreren Kommentaren ohne Fehler gelesen werden kann.
	@Test
	public void testCommentary_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"CommentsInFile.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        assertEquals("DataTest1.TopicB.ClassQ", iomObject.getobjecttag());
        assertEquals("oidQ", iomObject.getobjectoid());
        
        IomObject formQ = iomObject.getattrobj("formQ", 0);
        assertNotNull(formQ);
        
        assertEquals("MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.011, C2 70000.011, C3 5000.011}, COORD {C1 490000.012, C2 80000.012, C3 5000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.021, C2 70000.021, C3 5000.021}, COORD {C1 490000.022, C2 80000.022, C3 5000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.031, A2 70000.031, C1 480000.031, C2 70000.031, C3 5000.031, R 31}, COORD {C1 480000.032, C2 70000.032, C3 5000.032}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.041, C2 70000.041, C3 5000.041}, COORD {C1 490000.042, C2 80000.042, C3 5000.042}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.211, C2 70000.211, C3 5000.211}, COORD {C1 490000.212, C2 80000.212, C3 5000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.221, C2 70000.221, C3 5000.221}, COORD {C1 490000.222, C2 80000.222, C3 5000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.231, A2 70000.231, C1 480000.231, C2 70000.231, C3 5000.231, R 31}, COORD {C1 480000.232, C2 70000.232, C3 5000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.241, C2 70000.241, C3 5000.241}, COORD {C1 490000.242, C2 80000.242, C3 5000.242}]}}]}]}}",
                formQ.toString());
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}

	// Es wird getestet ob eine Area mit mehreren boundaries ohne Fehler gelesen werden kann.
	@Test
	public void testArea_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"Area.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);

        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        assertEquals("DataTest1.TopicB.ClassR", iomObject.getobjecttag());
        assertEquals("oidR", iomObject.getobjectoid());
        
        IomObject formR = iomObject.getattrobj("formR", 0);
        assertNotNull(formR);

        assertEquals("MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.011, C2 70000.011, C3 5000.011}, COORD {C1 490000.012, C2 80000.012, C3 5000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.021, C2 70000.021, C3 5000.021}, COORD {C1 490000.022, C2 80000.022, C3 5000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.031, A2 70000.031, C1 480000.031, C2 70000.031, C3 5000.031, R 31}, COORD {C1 480000.032, C2 70000.032, C3 5000.032}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.041, C2 70000.041, C3 5000.041}, COORD {C1 490000.042, C2 80000.042, C3 5000.042}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.211, C2 70000.211, C3 5000.211}, COORD {C1 490000.212, C2 80000.212, C3 5000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.221, C2 70000.221, C3 5000.221}, COORD {C1 490000.222, C2 80000.222, C3 5000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.231, A2 70000.231, C1 480000.231, C2 70000.231, C3 5000.231, R 31}, COORD {C1 480000.232, C2 70000.232, C3 5000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.241, C2 70000.241, C3 5000.241}, COORD {C1 490000.242, C2 80000.242, C3 5000.242}]}}]}]}}",
                formR.toString());
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob eine View, innerhalb eines TopicView, welche nicht transient ist, ohne Fehlermeldung gelesen werden kann.
	@Test
	public void testView_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"View.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		IoxEvent event=reader.read(); // DataTest1.TopicB.ClassA oid oidA {attr1 text}
		assertTrue(event instanceof  ObjectEvent);
		ObjectEvent objEvent=(ObjectEvent)event;
		IomObject iomObj=objEvent.getIomObject();
		assertEquals("text", iomObj.getattrvalue("attr1"));
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		event=reader.read(); // DataTest1.AdditionalTopicA.AdditionalClassA oid oidB {attr1 text, attr2 te}
		assertTrue(event instanceof  ObjectEvent);
		objEvent=(ObjectEvent)event;
		iomObj=objEvent.getIomObject();
		assertEquals("text", iomObj.getattrvalue("attr1"));
		assertEquals("te", iomObj.getattrvalue("attr2"));
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn bei einer Surface keine Linien definiert wurden.
	@Test
	public void testSurfaceNoLinesFound_Fail()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"UndefinedSurface.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent); // no lines found in polygon of Form.
		assertTrue(reader.read() instanceof  StartBasketEvent);
		try{
			reader.read();
			fail();
		}catch(IoxException ex){
			assertTrue((ex).getMessage().contains("expected surface"));
    		assertTrue(ex instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn A1 und A2 bei einer ARC fehlen.
	@Test
	public void testMissingCoord_Fail()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"MissingCoord.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent); // missing arc pos values
		assertTrue(reader.read() instanceof  StartBasketEvent);
		try{
			reader.read();
			fail();
		}catch(IoxException ex){
			assertTrue((ex).getMessage().contains("expected coord"));
    		assertTrue(ex instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Attribute mit den Selben Namen in unterschiedlichen Klassen ohne Fehler erstellt werden koennen.
	@Test
	public void testSameAttrNamesInDifClasses_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"SameAttrNames.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		IoxEvent event=reader.read(); // DataTest1.TopicC.ClassA oid oid1 {attrA textOid1}
		assertTrue(event instanceof  ObjectEvent);
		IomObject iomObj1=((ObjectEvent) event).getIomObject();
		String className=iomObj1.getobjecttag();
		assertEquals("DataTest1.TopicC.ClassA", className);
		String oid=iomObj1.getobjectoid();
		assertEquals("oid1", oid);
		String attrValue=iomObj1.getattrvalue("attrA");
		assertEquals("textOid1", attrValue);
		IoxEvent event2=reader.read(); // DataTest1.TopicC.ClassB oid oid2 {attrA textOid2}
		assertTrue(event2 instanceof  ObjectEvent);
		IomObject iomObj2=((ObjectEvent) event2).getIomObject();
		String className2=iomObj2.getobjecttag();
		assertEquals("DataTest1.TopicC.ClassB", className2);
		String oid2=iomObj2.getobjectoid();
		assertEquals("oid2", oid2);
		String attrValue2=iomObj2.getattrvalue("attrA");
		assertEquals("textOid2", attrValue2);
		IoxEvent event3=reader.read(); // DataTest1.TopicC.ClassC oid oid3 {attrA textOid3}
		assertTrue(event3 instanceof  ObjectEvent);
		IomObject iomObj3=((ObjectEvent) event3).getIomObject();
		String className3=iomObj3.getobjecttag();
		assertEquals("DataTest1.TopicC.ClassC", className3);
		String oid3=iomObj3.getobjectoid();
		assertEquals("oid3", oid3);
		String attrValue3=iomObj3.getattrvalue("attrA");
		assertEquals("textOid3", attrValue3);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Klassen mit den Selben Namen in unterschiedlichen Topics ohne Fehler erstellt werden koennen.
	@Test
	public void testSameClassNamesInDifTopics_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"SameClassNames.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent);
		IomObject iomObj1=((ObjectEvent) event).getIomObject();
		String className=iomObj1.getobjecttag();
		assertEquals("DataTest1.TopicD1.ClassA", className);
		String oid=iomObj1.getobjectoid();
		assertEquals("oid1", oid);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		IoxEvent event2=reader.read();
		assertTrue(event2 instanceof  ObjectEvent);
		IomObject iomObj2=((ObjectEvent) event2).getIomObject();
		String className2=iomObj2.getobjecttag();
		assertEquals("DataTest1.TopicD2.ClassA", className2);
		String oid2=iomObj2.getobjectoid();
		assertEquals("oid2", oid2);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		IoxEvent event3=reader.read();
		assertTrue(event3 instanceof  ObjectEvent);
		IomObject iomObj3=((ObjectEvent) event3).getIomObject();
		String className3=iomObj3.getobjecttag();
		assertEquals("DataTest1.TopicD3.ClassA", className3);
		String oid3=iomObj3.getobjectoid();
		assertEquals("oid3", oid3);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob eine Klasse mit den Selben Namen wie das Topic hat ohne Fehler gelesen werden kann.
	@Test
	public void testTopicNameLikeClassName_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"TopicNameLikeClassName.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		IoxEvent event2=reader.read();
		assertTrue(event2 instanceof  ObjectEvent);
		IomObject iomObj2=((ObjectEvent) event2).getIomObject();
		String className2=iomObj2.getobjecttag();
		assertEquals("DataTest1.TopicE.TopicE", className2);
		String oid2=iomObj2.getobjectoid();
		assertEquals("oid1", oid2);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		IoxEvent event3=reader.read();
		assertTrue(event3 instanceof  ObjectEvent);
		IomObject iomObj3=((ObjectEvent) event3).getIomObject();
		String className3=iomObj3.getobjecttag();
		assertEquals("DataTest1.TopicE.TopicE", className3);
		String oid3=iomObj3.getobjectoid();
		assertEquals("oid2", oid3);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Alle Attribute, Klassen und Topics haben die Selben Namen.
	@Test
	public void testAttrClassTopicNameSame_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"SameAttrClassTopicNames.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		IoxEvent event=reader.read(); // DataTest1.TopicF.TopicF oid oid1 {TopicF textOid1}
		assertTrue(event instanceof  ObjectEvent);
		IomObject iomObj1=((ObjectEvent) event).getIomObject();
		String className=iomObj1.getobjecttag();
		assertEquals("DataTest1.TopicF.TopicF", className);
		String oid=iomObj1.getobjectoid();
		assertEquals("oid1", oid);
		String attrValue=iomObj1.getattrvalue("TopicF");
		assertEquals("textOid1", attrValue);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		IoxEvent event2=reader.read(); // DataTest1.TopicF.TopicF oid oid2 {TopicF textOid2}
		assertTrue(event2 instanceof  ObjectEvent);
		IomObject iomObj2=((ObjectEvent) event2).getIomObject();
		String className2=iomObj2.getobjecttag();
		assertEquals("DataTest1.TopicF.TopicF", className2);
		String oid2=iomObj2.getobjectoid();
		assertEquals("oid2", oid2);
		String attrValue2=iomObj2.getattrvalue("TopicF");
		assertEquals("textOid2", attrValue2);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
}