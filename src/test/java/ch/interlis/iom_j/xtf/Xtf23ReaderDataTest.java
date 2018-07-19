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

public class Xtf23ReaderDataTest {

	private final static String TEST_IN="src/test/data/Xtf23Reader/dataSection/";
	
	// prueft ob eine leere DataSection erstellt werden kann.
	@Test
	public void testDatasection_Empty_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"EmptyDataSection.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// prueft ob ein leerer Basket erstellt werden kann.
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
	
	// prueft ob ein leeres Objekt erstellt werden kann.
	@Test
	public void testEmptyObjects_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"EmptyObjects.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof ObjectEvent); // DataTest1.TopicA.ClassA oid oid1 {}
		assertTrue(reader.read() instanceof ObjectEvent); // DataTest1.TopicA.ClassA oid oid2 {}
		assertTrue(reader.read() instanceof ObjectEvent); // DataTest1.TopicA.ClassA oid oid3 {}
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
		assertEquals("false", obj1.getIomObject().getattrvalue("attrBoolean2"));
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
		// DataTest1.TopicA.ClassA oid oidA {attrMText m text, attrName Randomname, attrText normal text, attrUri http://www.interlis.ch}
		assertTrue(reader.read() instanceof  ObjectEvent);
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
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicA.ClassF oid oidF1 {attrF1 rot.dunkelrot, attrF2 unten, attrF3 Werktage.Montag, attrF4 Werktage.Dienstag}
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicA.ClassF oid oidF2 {attrF1 gruen.hellgruen, attrF2 mitte, attrF3 Werktage.Sonntag, attrF4 Werktage.Samstag}
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicA.ClassF oid oidF3 {attrG1 rot, attrG2 rot.dunkelrot, attrG3 rot}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicA.ClassH oid oidH {attrH1 5kidok-_, attrH2 igjH-m_, attrH3 1234, attrH4 Interlis12345, attrH5 123e4567-e89b-12d3-a456-426655440000}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Datum und Zeit Typen ohne Fehler erstellt werden koennen.
	@Test
	public void testDateAndTimeType_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"DateTimeTypes.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicA.ClassI oid oidI {attrI1 2005-12-31T23:59:59.999, attrI2 2002-01-01T00:00:00.000, attrI3 2002-12-10, attrI4 12:23:47.111}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicA.ClassJ oid oidJ {attrBin <?xml version="1.0" encoding="UTF-8"?><BINBLBOX xmlns="http://www.interlis.ch/INTERLIS2.3">text123</BINBLBOX>, attrXml <?xml version="1.0" encoding="UTF-8"?><XMLBLBOX xmlns="http://www.interlis.ch/INTERLIS2.3"><xmlAttr1><xmlAttr2><xmlAttr3>attr1</xmlAttr3></xmlAttr2></xmlAttr1></XMLBLBOX>}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicA.ClassB oid oidB {attrNrDec 6.15, attrWertExakt 6.15, attrWertNormal 6.15}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicA.ClassD oid oidD {attrH Center, attrV Cap}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicA.ClassE oid oidE {attrDate 2003-02-03, attrDateTime 2007-12-31T23:59:59.999, attrTime 23:59:59.999}
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicA.ClassK oid oidK {attrK1 2003-02-03}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicA.ClassA oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicA.ClassL oid oidL {attrA DataTest1.TopicA.StructA {attrB DataTest1.TopicA.StructB {attrC DataTest1.TopicA.StructC {attr1 textC1, attr2 textC2, role1 -> oid1 REF {}}, attrC2 textC2, roleC1 -> oid1 REF {}}, attrB2 textC2, roleB1 -> oid1 REF {}}, attrA2 textC2, roleA1 -> oid1 REF {}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Strukturen mit Unterstrukturen, Attributen und Referenzen ohne Fehler erstellt werden koennen.
	@Test
	public void testStructure2Type_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"Structures2.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicA.ClassA oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicA.ClassL oid oidL {attrA DataTest1.TopicA.StructA {attrB2 textC2, attrB3 COORD {C1 480001, C2 70001, C3 5001}, roleB1 -> oid1 REF {}}, attrA2 textC2, attrA3 COORD {C1 480002, C2 70002, C3 5002}, attrA4 COORD {C1 480003, C2 70003, C3 5003}, roleA1 -> oid1 REF {}, roleA2 -> oid1 REF {}}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicA.ClassM oid oidM {attrM1 textM1}
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicA.ClassL oid oidL {attrA DataTest1.TopicA.StructA {attrB DataTest1.TopicA.StructB {attrC DataTest1.TopicA.StructC {ref1 -> oidM REF {}, ref2 -> DataTest1.TopicA.ClassM REF {}}}}}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicB.ClassAP1 oid oid1 {attr1 attribute1}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicB.Coord1D oid oid1D {attr1 COORD {C1 480000.000}}
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicB.Coord2D oid oid2D {attr2 COORD {C1 480000.000, C2 70000.000}}
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicB.Coord3D oid oid3D {attr3 COORD {C1 480000.000, C2 70000.000, C3 5000.000}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob eine Gerade vom Typ Polyline ohne Fehler erstellt werden kann.
	@Test
	public void testPolylinesWithStraights_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"PolylineWithStraights.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicB.ClassN oid oidN {attrN1 POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.000, C2 70000.000, C3 5000.000}, COORD {C1 490000.000, C2 80000.000, C3 5000.000}]}}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Bogen vom Typ Polyline ohne Fehler erstellt werden kann.
	@Test
	public void testPolylinesWithArcs_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"PolylineWithArcs.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicB.ClassO oid oidO {attrO1 POLYLINE {sequence SEGMENTS {segment ARC {A1 480005.000, A2 70005.000, C1 480000.000, C2 70000.000, C3 5000.000}}}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Bogen mit einem Radius vom Typ Polyline ohne Fehler erstellt werden kann.
	@Test
	public void testPolylinesWithArcsRadius_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"PolylineWithArcsRadius.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicB.ClassO oid oidO {attrO1 POLYLINE {sequence SEGMENTS {segment ARC {A1 480001.000, A2 70001.000, C1 480000.000, C2 70000.000, C3 5000.000, R 45}}}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Surface mit mehreren boundaries ohne Fehler erstellt werden kann.
	@Test
	public void testSurface_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"Surface.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);  // return DataTest1.TopicB.ClassQ oid oidQ {formQ MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.011, C1 480000.011, C2 70000.011}, COORD {C3 5000.012, C1 490000.012, C2 80000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.021, C1 480000.021, C2 70000.021}, COORD {C3 5000.022, C1 490000.022, C2 80000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.031, A1 480000.031, A2 70000.031, r 31, C1 480000.031, C2 70000.031}, COORD {C3 5000.032, C1 480000.032, C2 70000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.041, C1 480000.041, C2 70000.041}, COORD {C3 5000.042, C1 490000.042, C2 80000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.211, C1 480000.211, C2 70000.211}, COORD {C3 5000.212, C1 490000.212, C2 80000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.221, C1 480000.221, C2 70000.221}, COORD {C3 5000.222, C1 490000.222, C2 80000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.231, A1 480000.231, A2 70000.231, r 31, C1 480000.231, C2 70000.231}, COORD {C3 5000.232, C1 480000.232, C2 70000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.241, C1 480000.241, C2 70000.241}, COORD {C3 5000.242, C1 490000.242, C2 80000.242}]}}]}]}}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Surface mit mehreren boundaries und mehreren Kommentaren ohne Fehler erstellt werden kann.
	@Test
	public void testCommentary_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"CommentsInFile.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);  // return DataTest1.TopicB.ClassQ oid oidQ {formQ MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.011, C1 480000.011, C2 70000.011}, COORD {C3 5000.012, C1 490000.012, C2 80000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.021, C1 480000.021, C2 70000.021}, COORD {C3 5000.022, C1 490000.022, C2 80000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.031, A1 480000.031, A2 70000.031, r 31, C1 480000.031, C2 70000.031}, COORD {C3 5000.032, C1 480000.032, C2 70000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.041, C1 480000.041, C2 70000.041}, COORD {C3 5000.042, C1 490000.042, C2 80000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.211, C1 480000.211, C2 70000.211}, COORD {C3 5000.212, C1 490000.212, C2 80000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.221, C1 480000.221, C2 70000.221}, COORD {C3 5000.222, C1 490000.222, C2 80000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.231, A1 480000.231, A2 70000.231, r 31, C1 480000.231, C2 70000.231}, COORD {C3 5000.232, C1 480000.232, C2 70000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.241, C1 480000.241, C2 70000.241}, COORD {C3 5000.242, C1 490000.242, C2 80000.242}]}}]}]}}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}

	// Es wird getestet ob eine Area mit mehreren boundaries ohne Fehler erstellt werden kann.
	@Test
	public void testArea_Ok()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"Area.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicB.ClassQ oid oidQ {formQ MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.011, C2 70000.011, C3 5000.011}, COORD {C1 490000.012, C2 80000.012, C3 5000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.021, C2 70000.021, C3 5000.021}, COORD {C1 490000.022, C2 80000.022, C3 5000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.031, A2 70000.031, C1 480000.031, C2 70000.031, C3 5000.031, R 31}, COORD {C1 480000.032, C2 70000.032, C3 5000.032}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.041, C2 70000.041, C3 5000.041}, COORD {C1 490000.042, C2 80000.042, C3 5000.042}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.211, C2 70000.211, C3 5000.211}, COORD {C1 490000.212, C2 80000.212, C3 5000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.221, C2 70000.221, C3 5000.221}, COORD {C1 490000.222, C2 80000.222, C3 5000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.231, A2 70000.231, C1 480000.231, C2 70000.231, C3 5000.231, R 31}, COORD {C1 480000.232, C2 70000.232, C3 5000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.241, C2 70000.241, C3 5000.241}, COORD {C1 490000.242, C2 80000.242, C3 5000.242}]}}]}]}}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob eine View, innerhalb eines TopicView, welche nicht transient ist, ohne Fehlermeldung erstellt werden kann.
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
	
	// Es wird getestet ob eine Klasse mit den Selben Namen wie das Topic hat ohne Fehler erstellt werden kann.
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