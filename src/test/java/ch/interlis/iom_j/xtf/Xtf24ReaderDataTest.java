package ch.interlis.iom_j.xtf;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Before;
import org.junit.Ignore;
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
import ch.interlis.iox_j.jts.Iox2jtsException;

public class Xtf24ReaderDataTest {

	private final static String TEST_IN="src/test/data/Xtf24Reader/dataSection";
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
		FileEntry fileEntry=new FileEntry(TEST_IN+"/DataTest1.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	// Es wird getestet ob Texte ohne Fehler gelesen werden koennen.
	@Test
	public void testTextType_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"TextTypes.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        assertEquals("DataTest1.TopicA.ClassA", iomObject.getobjecttag());
        assertEquals("oidA", iomObject.getobjectoid());
        
        // return DataTest1.TopicA.ClassA oid oidA {attrText normal text, attrUri http://www.interlis.ch, attrName Randomname, attrMText m text}
        assertEquals("normal text", iomObject.getattrvalue("attrText"));
        assertEquals("http://www.interlis.ch", iomObject.getattrvalue("attrUri"));
        assertEquals("Randomname", iomObject.getattrvalue("attrName"));
        assertEquals("m text", iomObject.getattrvalue("attrMText"));
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Texte ohne Fehler auf einer Linie gelesen werden koennen.
	@Test
	public void xml1Line_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Xml1Line.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
        
		IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        //DataTest1.TopicA.ClassA oid oidA {attrName Randomname}
        assertEquals("DataTest1.TopicA.ClassA", iomObject.getobjecttag());
        assertEquals("oidA", iomObject.getobjectoid());
        
        assertEquals("Randomname", iomObject.getattrvalue("attrName"));
        
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Attribute mit den Selben Namen in unterschiedlichen Klassen ohne Fehler erstellt werden koennen.
	@Test
	public void testSameAttrNamesInDifClasses_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"SameAttrNames.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		
		assertTrue(reader.read() instanceof  StartBasketEvent);
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent);
		IomObject iomObj1=((ObjectEvent) event).getIomObject();
		String className=iomObj1.getobjecttag();
		assertEquals("DataTest1.TopicC.ClassA", className);
		String oid=iomObj1.getobjectoid();
		assertEquals("oid1", oid);
		String attrValue=iomObj1.getattrvalue("attrA");
		assertEquals("textOid1", attrValue);
		
		IoxEvent event2=reader.read();
		assertTrue(event2 instanceof  ObjectEvent);
		IomObject iomObj2=((ObjectEvent) event2).getIomObject();
		String className2=iomObj2.getobjecttag();
		assertEquals("DataTest1.TopicC.ClassB", className2);
		String oid2=iomObj2.getobjectoid();
		assertEquals("oid2", oid2);
		String attrValue2=iomObj2.getattrvalue("attrA");
		assertEquals("textOid2", attrValue2);
		
		IoxEvent event3=reader.read();
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
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"SameClassNames.xml"));
		reader.setModel(td);
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
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"TopicNameLikeClassName.xml"));
		reader.setModel(td);
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
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"SameAttrClassTopicNames.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);

		assertTrue(reader.read() instanceof  StartBasketEvent);
		IoxEvent event=reader.read();
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
		IoxEvent event2=reader.read();
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
	
	// Es wird getestet ob Aufzaehlungen ohne Fehler erstellt werden koennen.
	@Test
	public void testEnumerationType_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"EnumerationTypes.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        //DataTest1.TopicA.ClassF oid oidF1 {attrF4 Werktage.Dienstag, attrF3 Werktage.Montag, attrF2 unten, attrF1 rot.dunkelrot}
        assertEquals("DataTest1.TopicA.ClassF", iomObject.getobjecttag());
        assertEquals("oidF1", iomObject.getobjectoid());
        
        assertEquals("Werktage.Dienstag", iomObject.getattrvalue("attrF4"));
        assertEquals("Werktage.Montag", iomObject.getattrvalue("attrF3"));
        assertEquals("unten", iomObject.getattrvalue("attrF2"));
        assertEquals("rot.dunkelrot", iomObject.getattrvalue("attrF1"));
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
		
        //DataTest1.TopicA.ClassF oid oidF2 {attrF4 Werktage.Samstag, attrF3 Werktage.Sonntag, attrF2 mitte, attrF1 gruen.hellgruen}
        assertEquals("DataTest1.TopicA.ClassF", iomObject.getobjecttag());
        assertEquals("oidF2", iomObject.getobjectoid());
        
        assertEquals("Werktage.Samstag", iomObject.getattrvalue("attrF4"));
        assertEquals("Werktage.Sonntag", iomObject.getattrvalue("attrF3"));
        assertEquals("mitte", iomObject.getattrvalue("attrF2"));
        assertEquals("gruen.hellgruen", iomObject.getattrvalue("attrF1"));
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();

        //DataTest1.TopicA.ClassG oid oidG1 {attrG3 rot, attrG2 rot.dunkelrot, attrG1 rot}
        assertEquals("DataTest1.TopicA.ClassG", iomObject.getobjecttag());
        assertEquals("oidG1", iomObject.getobjectoid());
        
        assertEquals("rot", iomObject.getattrvalue("attrG3"));
        assertEquals("rot.dunkelrot", iomObject.getattrvalue("attrG2"));
        assertEquals("rot", iomObject.getattrvalue("attrG1"));
        
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Oid's ohne Fehler erstellt werden koennen.
	@Test
	public void testOidType_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"OidTypes.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        //DataTest1.TopicA.ClassH oid oidH {attrH5 123e4567-e89b-12d3-a456-426655440000, attrH4 Interlis12345, attrH3 1234, attrH2 igjH-m_, attrH1 5kidok-_}
        assertEquals("DataTest1.TopicA.ClassH", iomObject.getobjecttag());
        assertEquals("oidH", iomObject.getobjectoid());
        
        assertEquals("123e4567-e89b-12d3-a456-426655440000", iomObject.getattrvalue("attrH5"));
        assertEquals("Interlis12345", iomObject.getattrvalue("attrH4"));
        assertEquals("1234", iomObject.getattrvalue("attrH3"));
        assertEquals("igjH-m_", iomObject.getattrvalue("attrH2"));
        assertEquals("5kidok-_", iomObject.getattrvalue("attrH1"));
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Datum und Zeit Typen ohne Fehler erstellt werden koennen.
	@Test
	public void testDateAndTimeType_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"DateTimeTypes.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        //DataTest1.TopicA.ClassI oid oidI {attrI4 12:23:47.111, attrI3 2002-12-10, attrI2 2002-01-01T00:00:00.000, attrI1 2005-12-31T23:59:59.999}
        assertEquals("DataTest1.TopicA.ClassI", iomObject.getobjecttag());
        assertEquals("oidI", iomObject.getobjectoid());
        
        assertEquals("12:23:47.111", iomObject.getattrvalue("attrI4"));
        assertEquals("2002-12-10", iomObject.getattrvalue("attrI3"));
        assertEquals("2002-01-01T00:00:00.000", iomObject.getattrvalue("attrI2"));
        assertEquals("2005-12-31T23:59:59.999", iomObject.getattrvalue("attrI1"));
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Black Box Typen ohne Fehler erstellt werden koennen.
	@Test
	public void testBlackBoxType_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"BlackBoxTypes.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		IoxEvent event = reader.read();
		assertTrue(event instanceof  ObjectEvent); // return DataTest1.TopicA.ClassJ oid oidJ {attrBin text123, attrXml <?xml version="1.0" encoding="UTF-8"?><xmlAttr1 xmlns="http://www.interlis.ch/xtf/2.4/DataTest1"><xmlAttr2><xmlAttr3>attr1</xmlAttr3></xmlAttr2></xmlAttr1>}
		IomObject iomObj=((ObjectEvent) event).getIomObject();
		assertEquals("FFFF",iomObj.getattrvalue("attrBin"));
        assertEquals("<xmlAttr1 xmlns=\"http://www.interlis.ch/xtf/2.4/DataTest1\"><xmlAttr2><xmlAttr3>attr1</xmlAttr3></xmlAttr2></xmlAttr1>",iomObj.getattrvalue("attrXml"));
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Nummern ohne Fehler erstellt werden koennen.
	@Test
	public void testNumericDataTypes_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"NumericTypes.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        //DataTest1.TopicA.ClassB oid oidB {attrWertNormal 6.15, attrWertExakt 6.15, attrNrDec 6.15}
        assertEquals("DataTest1.TopicA.ClassB", iomObject.getobjecttag());
        assertEquals("oidB", iomObject.getobjectoid());
        
        assertEquals("6.15", iomObject.getattrvalue("attrWertNormal"));
        assertEquals("6.15", iomObject.getattrvalue("attrWertExakt"));
        assertEquals("6.15", iomObject.getattrvalue("attrNrDec"));
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Boolean Werte ohne Fehler erstellt werden koennen.
	@Test
	public void testBooleanDataTypes_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"BooleanType.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        // return DataTest1.TopicA.ClassC oid oidC {attrBoolean2 false, attrBoolean1 true}
        assertEquals("DataTest1.TopicA.ClassC", iomObject.getobjecttag());
        assertEquals("oidC", iomObject.getobjectoid());
        
        assertEquals("false", iomObject.getattrvalue("attrBoolean2"));
        assertEquals("true", iomObject.getattrvalue("attrBoolean1"));
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Ausrichtungen ohne Fehler erstellt werden koennen.
	@Test
	public void testAlignmentDataTypes_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"AlignmentTypes.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        // DataTest1.TopicA.ClassD oid oidD {attrV Cap, attrH Center}
        assertEquals("DataTest1.TopicA.ClassD", iomObject.getobjecttag());
        assertEquals("oidD", iomObject.getobjectoid());
        
        assertEquals("Cap", iomObject.getattrvalue("attrV"));
        assertEquals("Center", iomObject.getattrvalue("attrH"));
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob formatierte Werte ohne Fehler erstellt werden koennen.
	@Test
	public void testFormattedDataTypes_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"FormattedType.xml"));
		reader.setModel(td);
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
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Structures.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        // DataTest1.TopicA.ClassL oid oidS {attrL1 DataTest1.TopicA.StructA {attrS1 text}}
        assertEquals("DataTest1.TopicA.ClassL", iomObject.getobjecttag());
        assertEquals("oidS", iomObject.getobjectoid());
        
        IomObject attrL1 = iomObject.getattrobj("attrL1", 0);
        assertNotNull(attrL1);
        
        assertEquals("text", attrL1.getattrvalue("attrS1"));
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob Attributepfade ohne Fehler erstellt werden koennen.
	@Test
	public void testAttributePath_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"AttrpathType.xml"));
		reader.setModel(td);
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
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Coord.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		// 1. ObjectEvent
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        // DataTest1.TopicB.Coord1D oid oid1D {attr1 COORD {C1 480000.000}}
        assertEquals("DataTest1.TopicB.Coord1D", iomObject.getobjecttag());
        assertEquals("oid1D", iomObject.getobjectoid());
        
        IomObject attr1 = iomObject.getattrobj("attr1", 0);
        assertNotNull(attr1);
        assertEquals("480000.000", attr1.getattrvalue("C1"));
        
        // 2. ObjectEvent
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();

        // DataTest1.TopicB.Coord2D oid oid2D {attr2 COORD {C1 480000.000, C2 70000.000}}
        assertEquals("DataTest1.TopicB.Coord2D", iomObject.getobjecttag());
        assertEquals("oid2D", iomObject.getobjectoid());
        
        IomObject attr2 = iomObject.getattrobj("attr2", 0);
        assertNotNull(attr1);
        assertEquals("480000.000", attr2.getattrvalue("C1"));
        assertEquals("70000.000", attr2.getattrvalue("C2"));
        
        // 3. ObjectEvent
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();

        // DataTest1.TopicB.Coord3D oid oid3D {attr3 COORD {C3 5000.000, C1 480000.000, C2 70000.000}}
        assertEquals("DataTest1.TopicB.Coord3D", iomObject.getobjecttag());
        assertEquals("oid3D", iomObject.getobjectoid());
        
        IomObject attr3 = iomObject.getattrobj("attr3", 0);
        assertNotNull(attr1);
        assertEquals("5000.000", attr3.getattrvalue("C3"));
        assertEquals("480000.000", attr3.getattrvalue("C1"));
        assertEquals("70000.000", attr3.getattrvalue("C2"));
        
        
        // 4. ObjectEvent
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();

        // DataTest1.TopicB.MultiCoord oid mOid {attr4 SEGMENTS {segment [COORD {C3 5000.111, C1 480000.111, C2 70000.111}, COORD {C3 5000.222, C1 480000.222, C2 70000.222}, COORD {C3 5000.333, C1 480000.333, C2 70000.333}, COORD {C3 5000.444, C1 480000.444, C2 70000.444}, COORD {C3 5000.555, C1 480000.555, C2 70000.555}]}}
        assertEquals("DataTest1.TopicB.MultiCoord", iomObject.getobjecttag());
        assertEquals("mOid", iomObject.getobjectoid());
                
        IomObject attr4 = iomObject.getattrobj("attr4", 0);
        assertNotNull(attr4);
        IomObject segment1 = attr4.getattrobj("segment", 0);
        assertNotNull(segment1);
        assertEquals("5000.111", segment1.getattrvalue("C3"));
        assertEquals("480000.111", segment1.getattrvalue("C1"));
        assertEquals("70000.111", segment1.getattrvalue("C2"));
        
        IomObject segment2 = attr4.getattrobj("segment", 1);
        assertNotNull(segment2);
        assertEquals("5000.222", segment2.getattrvalue("C3"));
        assertEquals("480000.222", segment2.getattrvalue("C1"));
        assertEquals("70000.222", segment2.getattrvalue("C2"));
        
        IomObject segment3 = attr4.getattrobj("segment", 2);
        assertNotNull(segment3);
        assertEquals("5000.333", segment3.getattrvalue("C3"));
        assertEquals("480000.333", segment3.getattrvalue("C1"));
        assertEquals("70000.333", segment3.getattrvalue("C2"));
        
        IomObject segment4 = attr4.getattrobj("segment", 3);
        assertNotNull(segment4);
        assertEquals("5000.444", segment4.getattrvalue("C3"));
        assertEquals("480000.444", segment4.getattrvalue("C1"));
        assertEquals("70000.444", segment4.getattrvalue("C2"));
        
        IomObject segment5 = attr4.getattrobj("segment", 4);
        assertNotNull(segment5);
        assertEquals("5000.555", segment5.getattrvalue("C3"));
        assertEquals("480000.555", segment5.getattrvalue("C1"));
        assertEquals("70000.555", segment5.getattrvalue("C2"));
        
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob eine Gerade vom Typ Polyline ohne Fehler erstellt werden koennen.
	@Test
	public void testPolylinesWithStraights_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"PolylineWithStraights.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        // DataTest1.TopicB.ClassN oid oidN {attrN1 POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 490000.000, C2 80000.000}]}}}
        assertEquals("DataTest1.TopicB.ClassN", iomObject.getobjecttag());
        assertEquals("oidN", iomObject.getobjectoid());
        
        IomObject attrN1 = iomObject.getattrobj("attrN1", 0);
        assertNotNull(attrN1);
        IomObject sequence = attrN1.getattrobj("sequence", 0);
        assertNotNull(sequence);
        IomObject coord1 = sequence.getattrobj("segment", 0);
        assertNotNull(coord1);
        assertEquals("5000.000", coord1.getattrvalue("C3"));
        assertEquals("480000.000", coord1.getattrvalue("C1"));
        assertEquals("70000.000", coord1.getattrvalue("C2"));
        
        IomObject coord2 = sequence.getattrobj("segment", 1);
        assertNotNull(coord2);
        assertEquals("5000.000", coord2.getattrvalue("C3"));
        assertEquals("490000.000", coord2.getattrvalue("C1"));
        assertEquals("80000.000", coord2.getattrvalue("C2"));
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Bogen vom Typ Polyline ohne Fehler erstellt werden koennen.
	@Test
	public void testPolylinesWithArcs_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"PolylineWithArcs.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        // DataTest1.TopicB.ClassO oid oidO {attrO1 POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.000, A1 480000.000, A2 70000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}]}}}
        assertEquals("DataTest1.TopicB.ClassO", iomObject.getobjecttag());
        assertEquals("oidO", iomObject.getobjectoid());
        
        IomObject attrO1 = iomObject.getattrobj("attrO1", 0);
        assertNotNull(attrO1);
        IomObject sequence = attrO1.getattrobj("sequence", 0);
        assertNotNull(sequence);
        IomObject segment = sequence.getattrobj("segment", 0);
        assertNotNull(segment);
        
        assertEquals("480000.000", segment.getattrvalue("A1"));
        assertEquals("70000.000", segment.getattrvalue("A2"));
        assertEquals("5000.000", segment.getattrvalue("C3"));
        assertEquals("480000.000", segment.getattrvalue("C1"));
        assertEquals("70000.000", segment.getattrvalue("C2"));
        
        IomObject segment1 = sequence.getattrobj("segment", 1);
        assertNotNull(segment1);
        assertEquals("5000.000", segment1.getattrvalue("C3"));
        assertEquals("480000.000", segment1.getattrvalue("C1"));
        assertEquals("70000.000", segment1.getattrvalue("C2"));
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Bogen mit einem Radius vom Typ Polyline ohne Fehler erstellt werden koennen.
	@Test
	public void testPolylinesWithArcsRadius_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"PolylineWithArcsRadius.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        // DataTest1.TopicB.ClassO oid oidO {attrO1 POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.000, A1 480000.000, A2 70000.000, r 45, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}]}}}
        assertEquals("DataTest1.TopicB.ClassO", iomObject.getobjecttag());
        assertEquals("oidO", iomObject.getobjectoid());
        
        IomObject attrO1 = iomObject.getattrobj("attrO1", 0);
        assertNotNull(attrO1);
        IomObject sequence = attrO1.getattrobj("sequence", 0);
        assertNotNull(sequence);
        IomObject segment = sequence.getattrobj("segment", 0);
        assertNotNull(segment);
        
        assertEquals("480000.000", segment.getattrvalue("A1"));
        assertEquals("70000.000", segment.getattrvalue("A2"));
        assertEquals("5000.000", segment.getattrvalue("C3"));
        assertEquals("480000.000", segment.getattrvalue("C1"));
        assertEquals("70000.000", segment.getattrvalue("C2"));
        assertEquals("45", segment.getattrvalue("R"));// Im IomObj fehlt es..
		
        IomObject segment1 = sequence.getattrobj("segment", 1);
        assertNotNull(segment1);
        assertEquals("5000.000", segment1.getattrvalue("C3"));
        assertEquals("480000.000", segment1.getattrvalue("C1"));
        assertEquals("70000.000", segment1.getattrvalue("C2"));
        
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob eine Gerade mit einem Bogen vom Typ Polyline ohne Fehler erstellt werden koennen.
	@Test
	public void testMultiPolyline_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"MultiPolyline.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        // DataTest1.TopicB.ClassPM oid oidP {attrPM MULTIPOLYLINE {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.011, C1 480000.011, C2 70000.011}, COORD {C3 5000.012, C1 490000.012, C2 80000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.021, C1 480000.021, C2 70000.021}, COORD {C3 5000.022, C1 490000.022, C2 80000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.031, A1 480000.031, A2 70000.031, r 31, C1 480000.031, C2 70000.031}, COORD {C3 5000.032, C1 480000.032, C2 70000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.041, C1 480000.041, C2 70000.041}, COORD {C3 5000.042, C1 490000.042, C2 80000.042}]}}]}}
        assertEquals("DataTest1.TopicB.ClassPM", iomObject.getobjecttag());
        assertEquals("oidP", iomObject.getobjectoid());
        
        IomObject attrPm = iomObject.getattrobj("attrPM", 0);
        assertNotNull(attrPm);
        
        assertEquals("MULTIPOLYLINE {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.011, C2 70000.011, C3 5000.011}, COORD {C1 490000.012, C2 80000.012, C3 5000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.021, C2 70000.021, C3 5000.021}, COORD {C1 490000.022, C2 80000.022, C3 5000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.031, A2 70000.031, C1 480000.031, C2 70000.031, C3 5000.031, R 31}, COORD {C1 480000.032, C2 70000.032, C3 5000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.041, C2 70000.041, C3 5000.041}, COORD {C1 490000.042, C2 80000.042, C3 5000.042}]}}]}",
                attrPm.toString()); // Radius fehlt noch im iomObject
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Surface mit mehreren boundaries ohne Fehler erstellt werden kann.
	@Test
	public void testSurface_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Surface.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        // DataTest1.TopicB.ClassQ oid oidQ {formQ MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.011, C1 480000.011, C2 70000.011}, COORD {C3 5000.012, C1 490000.012, C2 80000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.021, C1 480000.021, C2 70000.021}, COORD {C3 5000.022, C1 490000.022, C2 80000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.031, A1 480000.031, A2 70000.031, r 31, C1 480000.031, C2 70000.031}, COORD {C3 5000.032, C1 480000.032, C2 70000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.041, C1 480000.041, C2 70000.041}, COORD {C3 5000.042, C1 490000.042, C2 80000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.211, C1 480000.211, C2 70000.211}, COORD {C3 5000.212, C1 490000.212, C2 80000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.221, C1 480000.221, C2 70000.221}, COORD {C3 5000.222, C1 490000.222, C2 80000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.231, A1 480000.231, A2 70000.231, r 31, C1 480000.231, C2 70000.231}, COORD {C3 5000.232, C1 480000.232, C2 70000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.241, C1 480000.241, C2 70000.241}, COORD {C3 5000.242, C1 490000.242, C2 80000.242}]}}]}]}}}
        assertEquals("DataTest1.TopicB.ClassQ", iomObject.getobjecttag());
        assertEquals("oidQ", iomObject.getobjectoid());
		
        IomObject formQ = iomObject.getattrobj("formQ", 0);
        assertNotNull(formQ);
        
        assertEquals("MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.011, C2 70000.011, C3 5000.011}, COORD {C1 490000.012, C2 80000.012, C3 5000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.021, C2 70000.021, C3 5000.021}, COORD {C1 490000.022, C2 80000.022, C3 5000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.031, A2 70000.031, C1 480000.031, C2 70000.031, C3 5000.031, R 31}, COORD {C1 480000.032, C2 70000.032, C3 5000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.041, C2 70000.041, C3 5000.041}, COORD {C1 490000.042, C2 80000.042, C3 5000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.211, C2 70000.211, C3 5000.211}, COORD {C1 490000.212, C2 80000.212, C3 5000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.221, C2 70000.221, C3 5000.221}, COORD {C1 490000.222, C2 80000.222, C3 5000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.231, A2 70000.231, C1 480000.231, C2 70000.231, C3 5000.231, R 31}, COORD {C1 480000.232, C2 70000.232, C3 5000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.241, C2 70000.241, C3 5000.241}, COORD {C1 490000.242, C2 80000.242, C3 5000.242}]}}]}]}}",
                formQ.toString()); // Radius fehlt noch im iomObject
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Surface mit mehreren boundaries und diversen Kommentaren ohne Fehler erstellt werden kann.
	@Test
	public void testCommentary_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"CommentsInFile.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        // return DataTest1.TopicB.ClassQM oid oidC {formQ MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.011, C1 480000.011, C2 70000.011}, COORD {C3 5000.012, C1 490000.012, C2 80000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.021, C1 480000.021, C2 70000.021}, COORD {C3 5000.022, C1 490000.022, C2 80000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.031, A1 480000.031, A2 70000.031, r 31, C1 480000.031, C2 70000.031}, COORD {C3 5000.032, C1 480000.032, C2 70000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.041, C1 480000.041, C2 70000.041}, COORD {C3 5000.042, C1 490000.042, C2 80000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.211, C1 480000.211, C2 70000.211}, COORD {C3 5000.212, C1 490000.212, C2 80000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.221, C1 480000.221, C2 70000.221}, COORD {C3 5000.222, C1 490000.222, C2 80000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.231, A1 480000.231, A2 70000.231, r 31, C1 480000.231, C2 70000.231}, COORD {C3 5000.232, C1 480000.232, C2 70000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.241, C1 480000.241, C2 70000.241}, COORD {C3 5000.242, C1 490000.242, C2 80000.242}]}}]}]}}}
        assertEquals("DataTest1.TopicB.ClassQM", iomObject.getobjecttag());
        assertEquals("oidC", iomObject.getobjectoid());
        
        IomObject formQ = iomObject.getattrobj("formQM", 0);
        assertNotNull(formQ);
        
        assertEquals("MULTISURFACE {surface [SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.011, C2 70000.011, C3 5000.011}, COORD {C1 490000.012, C2 80000.012, C3 5000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.021, C2 70000.021, C3 5000.021}, COORD {C1 490000.022, C2 80000.022, C3 5000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.031, A2 70000.031, C1 480000.031, C2 70000.031, C3 5000.031, R 31}, COORD {C1 480000.032, C2 70000.032, C3 5000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.041, C2 70000.041, C3 5000.041}, COORD {C1 490000.042, C2 80000.042, C3 5000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.211, C2 70000.211, C3 5000.211}, COORD {C1 490000.212, C2 80000.212, C3 5000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.221, C2 70000.221, C3 5000.221}, COORD {C1 490000.222, C2 80000.222, C3 5000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.231, A2 70000.231, C1 480000.231, C2 70000.231, C3 5000.231, R 31}, COORD {C1 480000.232, C2 70000.232, C3 5000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.241, C2 70000.241, C3 5000.241}, COORD {C1 490000.242, C2 80000.242, C3 5000.242}]}}]}]}, SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.011, C2 70000.011, C3 5000.011}, COORD {C1 490000.012, C2 80000.012, C3 5000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.021, C2 70000.021, C3 5000.021}, COORD {C1 490000.022, C2 80000.022, C3 5000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.031, A2 70000.031, C1 480000.031, C2 70000.031, C3 5000.031, R 31}, COORD {C1 480000.032, C2 70000.032, C3 5000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.041, C2 70000.041, C3 5000.041}, COORD {C1 490000.042, C2 80000.042, C3 5000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.211, C2 70000.211, C3 5000.211}, COORD {C1 490000.212, C2 80000.212, C3 5000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.221, C2 70000.221, C3 5000.221}, COORD {C1 490000.222, C2 80000.222, C3 5000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.231, A2 70000.231, C1 480000.231, C2 70000.231, C3 5000.231, R 31}, COORD {C1 480000.232, C2 70000.232, C3 5000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.241, C2 70000.241, C3 5000.241}, COORD {C1 490000.242, C2 80000.242, C3 5000.242}]}}]}]}, SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.011, C2 70000.011, C3 5000.011}, COORD {C1 490000.012, C2 80000.012, C3 5000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.021, C2 70000.021, C3 5000.021}, COORD {C1 490000.022, C2 80000.022, C3 5000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.031, A2 70000.031, C1 480000.031, C2 70000.031, C3 5000.031, R 31}, COORD {C1 480000.032, C2 70000.032, C3 5000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.041, C2 70000.041, C3 5000.041}, COORD {C1 490000.042, C2 80000.042, C3 5000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.211, C2 70000.211, C3 5000.211}, COORD {C1 490000.212, C2 80000.212, C3 5000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.221, C2 70000.221, C3 5000.221}, COORD {C1 490000.222, C2 80000.222, C3 5000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.231, A2 70000.231, C1 480000.231, C2 70000.231, C3 5000.231, R 31}, COORD {C1 480000.232, C2 70000.232, C3 5000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.241, C2 70000.241, C3 5000.241}, COORD {C1 490000.242, C2 80000.242, C3 5000.242}]}}]}]}]}",
                formQ.toString()); // Radius fehlt noch im iomObject
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}

	// Es wird getestet ob eine Area mit mehreren boundaries ohne Fehler erstellt werden kann.
	@Test
	public void testArea_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Area.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        // return DataTest1.TopicB.ClassR oid oidR {formR MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.011, C1 480000.011, C2 70000.011}, COORD {C3 5000.012, C1 490000.012, C2 80000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.021, C1 480000.021, C2 70000.021}, COORD {C3 5000.022, C1 490000.022, C2 80000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.031, A1 480000.031, A2 70000.031, r 31, C1 480000.031, C2 70000.031}, COORD {C3 5000.032, C1 480000.032, C2 70000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.041, C1 480000.041, C2 70000.041}, COORD {C3 5000.042, C1 490000.042, C2 80000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.211, C1 480000.211, C2 70000.211}, COORD {C3 5000.212, C1 490000.212, C2 80000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.221, C1 480000.221, C2 70000.221}, COORD {C3 5000.222, C1 490000.222, C2 80000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.231, A1 480000.231, A2 70000.231, r 31, C1 480000.231, C2 70000.231}, COORD {C3 5000.232, C1 480000.232, C2 70000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.241, C1 480000.241, C2 70000.241}, COORD {C3 5000.242, C1 490000.242, C2 80000.242}]}}]}]}}}
        assertEquals("DataTest1.TopicB.ClassR", iomObject.getobjecttag());
        assertEquals("oidR", iomObject.getobjectoid());
        
        IomObject formQ = iomObject.getattrobj("formR", 0);
        assertNotNull(formQ);
        
        assertEquals("MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.011, C2 70000.011, C3 5000.011}, COORD {C1 490000.012, C2 80000.012, C3 5000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.021, C2 70000.021, C3 5000.021}, COORD {C1 490000.022, C2 80000.022, C3 5000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.031, A2 70000.031, C1 480000.031, C2 70000.031, C3 5000.031, R 31}, COORD {C1 480000.032, C2 70000.032, C3 5000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.041, C2 70000.041, C3 5000.041}, COORD {C1 490000.042, C2 80000.042, C3 5000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.211, C2 70000.211, C3 5000.211}, COORD {C1 490000.212, C2 80000.212, C3 5000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.221, C2 70000.221, C3 5000.221}, COORD {C1 490000.222, C2 80000.222, C3 5000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.231, A2 70000.231, C1 480000.231, C2 70000.231, C3 5000.231, R 31}, COORD {C1 480000.232, C2 70000.232, C3 5000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.241, C2 70000.241, C3 5000.241}, COORD {C1 490000.242, C2 80000.242, C3 5000.242}]}}]}]}}",
                formQ.toString()); // Radius fehlt noch im iomObject
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob mehrere Surface mit mehreren boundaries ohne Fehler erstellt werden koennen.
	@Test
	public void testMultiSurface_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"MultiSurface.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        // return DataTest1.TopicB.ClassQM oid oidC {formQM MULTISURFACE {surface [SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.011, C1 480000.011, C2 70000.011}, COORD {C3 5000.012, C1 490000.012, C2 80000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.021, C1 480000.021, C2 70000.021}, COORD {C3 5000.022, C1 490000.022, C2 80000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.031, A1 480000.031, A2 70000.031, r 31, C1 480000.031, C2 70000.031}, COORD {C3 5000.032, C1 480000.032, C2 70000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.041, C1 480000.041, C2 70000.041}, COORD {C3 5000.042, C1 490000.042, C2 80000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.211, C1 480000.211, C2 70000.211}, COORD {C3 5000.212, C1 490000.212, C2 80000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.221, C1 480000.221, C2 70000.221}, COORD {C3 5000.222, C1 490000.222, C2 80000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.231, A1 480000.231, A2 70000.231, r 31, C1 480000.231, C2 70000.231}, COORD {C3 5000.232, C1 480000.232, C2 70000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.241, C1 480000.241, C2 70000.241}, COORD {C3 5000.242, C1 490000.242, C2 80000.242}]}}]}]}, SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.011, C1 480000.011, C2 70000.011}, COORD {C3 5000.012, C1 490000.012, C2 80000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.021, C1 480000.021, C2 70000.021}, COORD {C3 5000.022, C1 490000.022, C2 80000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.031, A1 480000.031, A2 70000.031, r 31, C1 480000.031, C2 70000.031}, COORD {C3 5000.032, C1 480000.032, C2 70000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.041, C1 480000.041, C2 70000.041}, COORD {C3 5000.042, C1 490000.042, C2 80000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.211, C1 480000.211, C2 70000.211}, COORD {C3 5000.212, C1 490000.212, C2 80000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.221, C1 480000.221, C2 70000.221}, COORD {C3 5000.222, C1 490000.222, C2 80000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.231, A1 480000.231, A2 70000.231, r 31, C1 480000.231, C2 70000.231}, COORD {C3 5000.232, C1 480000.232, C2 70000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.241, C1 480000.241, C2 70000.241}, COORD {C3 5000.242, C1 490000.242, C2 80000.242}]}}]}]}, SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.011, C1 480000.011, C2 70000.011}, COORD {C3 5000.012, C1 490000.012, C2 80000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.021, C1 480000.021, C2 70000.021}, COORD {C3 5000.022, C1 490000.022, C2 80000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.031, A1 480000.031, A2 70000.031, r 31, C1 480000.031, C2 70000.031}, COORD {C3 5000.032, C1 480000.032, C2 70000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.041, C1 480000.041, C2 70000.041}, COORD {C3 5000.042, C1 490000.042, C2 80000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.211, C1 480000.211, C2 70000.211}, COORD {C3 5000.212, C1 490000.212, C2 80000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.221, C1 480000.221, C2 70000.221}, COORD {C3 5000.222, C1 490000.222, C2 80000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.231, A1 480000.231, A2 70000.231, r 31, C1 480000.231, C2 70000.231}, COORD {C3 5000.232, C1 480000.232, C2 70000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.241, C1 480000.241, C2 70000.241}, COORD {C3 5000.242, C1 490000.242, C2 80000.242}]}}]}]}]}}
        assertEquals("DataTest1.TopicB.ClassQM", iomObject.getobjecttag());
        assertEquals("oidC", iomObject.getobjectoid());
        
        IomObject formQ = iomObject.getattrobj("formQM", 0);
        assertNotNull(formQ);
        
        assertEquals("MULTISURFACE {surface [SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.011, C2 70000.011, C3 5000.011}, COORD {C1 490000.012, C2 80000.012, C3 5000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.021, C2 70000.021, C3 5000.021}, COORD {C1 490000.022, C2 80000.022, C3 5000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.031, A2 70000.031, C1 480000.031, C2 70000.031, C3 5000.031, R 31}, COORD {C1 480000.032, C2 70000.032, C3 5000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.041, C2 70000.041, C3 5000.041}, COORD {C1 490000.042, C2 80000.042, C3 5000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.211, C2 70000.211, C3 5000.211}, COORD {C1 490000.212, C2 80000.212, C3 5000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.221, C2 70000.221, C3 5000.221}, COORD {C1 490000.222, C2 80000.222, C3 5000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.231, A2 70000.231, C1 480000.231, C2 70000.231, C3 5000.231, R 31}, COORD {C1 480000.232, C2 70000.232, C3 5000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.241, C2 70000.241, C3 5000.241}, COORD {C1 490000.242, C2 80000.242, C3 5000.242}]}}]}]}, SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.011, C2 70000.011, C3 5000.011}, COORD {C1 490000.012, C2 80000.012, C3 5000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.021, C2 70000.021, C3 5000.021}, COORD {C1 490000.022, C2 80000.022, C3 5000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.031, A2 70000.031, C1 480000.031, C2 70000.031, C3 5000.031, R 31}, COORD {C1 480000.032, C2 70000.032, C3 5000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.041, C2 70000.041, C3 5000.041}, COORD {C1 490000.042, C2 80000.042, C3 5000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.211, C2 70000.211, C3 5000.211}, COORD {C1 490000.212, C2 80000.212, C3 5000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.221, C2 70000.221, C3 5000.221}, COORD {C1 490000.222, C2 80000.222, C3 5000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.231, A2 70000.231, C1 480000.231, C2 70000.231, C3 5000.231, R 31}, COORD {C1 480000.232, C2 70000.232, C3 5000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.241, C2 70000.241, C3 5000.241}, COORD {C1 490000.242, C2 80000.242, C3 5000.242}]}}]}]}, SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.011, C2 70000.011, C3 5000.011}, COORD {C1 490000.012, C2 80000.012, C3 5000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.021, C2 70000.021, C3 5000.021}, COORD {C1 490000.022, C2 80000.022, C3 5000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.031, A2 70000.031, C1 480000.031, C2 70000.031, C3 5000.031, R 31}, COORD {C1 480000.032, C2 70000.032, C3 5000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.041, C2 70000.041, C3 5000.041}, COORD {C1 490000.042, C2 80000.042, C3 5000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.211, C2 70000.211, C3 5000.211}, COORD {C1 490000.212, C2 80000.212, C3 5000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.221, C2 70000.221, C3 5000.221}, COORD {C1 490000.222, C2 80000.222, C3 5000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.231, A2 70000.231, C1 480000.231, C2 70000.231, C3 5000.231, R 31}, COORD {C1 480000.232, C2 70000.232, C3 5000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.241, C2 70000.241, C3 5000.241}, COORD {C1 490000.242, C2 80000.242, C3 5000.242}]}}]}]}]}",
                formQ.toString()); // Radius fehlt noch im iomObject
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob mehrere Areas mit mehreren boundaries ohne Fehler erstellt werden koennen.
	@Test
	public void testMultiArea_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"MultiArea.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        // return DataTest1.TopicB.ClassRM oid oidRM {formRM MULTISURFACE {surface [SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.011, C1 480000.011, C2 70000.011}, COORD {C3 5000.012, C1 490000.012, C2 80000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.021, C1 480000.021, C2 70000.021}, COORD {C3 5000.022, C1 490000.022, C2 80000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.031, A1 480000.031, A2 70000.031, r 31, C1 480000.031, C2 70000.031}, COORD {C3 5000.032, C1 480000.032, C2 70000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.041, C1 480000.041, C2 70000.041}, COORD {C3 5000.042, C1 490000.042, C2 80000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.211, C1 480000.211, C2 70000.211}, COORD {C3 5000.212, C1 490000.212, C2 80000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.221, C1 480000.221, C2 70000.221}, COORD {C3 5000.222, C1 490000.222, C2 80000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.231, A1 480000.231, A2 70000.231, r 31, C1 480000.231, C2 70000.231}, COORD {C3 5000.232, C1 480000.232, C2 70000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.241, C1 480000.241, C2 70000.241}, COORD {C3 5000.242, C1 490000.242, C2 80000.242}]}}]}]}, SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.011, C1 480000.011, C2 70000.011}, COORD {C3 5000.012, C1 490000.012, C2 80000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.021, C1 480000.021, C2 70000.021}, COORD {C3 5000.022, C1 490000.022, C2 80000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.031, A1 480000.031, A2 70000.031, r 31, C1 480000.031, C2 70000.031}, COORD {C3 5000.032, C1 480000.032, C2 70000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.041, C1 480000.041, C2 70000.041}, COORD {C3 5000.042, C1 490000.042, C2 80000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.211, C1 480000.211, C2 70000.211}, COORD {C3 5000.212, C1 490000.212, C2 80000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.221, C1 480000.221, C2 70000.221}, COORD {C3 5000.222, C1 490000.222, C2 80000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.231, A1 480000.231, A2 70000.231, r 31, C1 480000.231, C2 70000.231}, COORD {C3 5000.232, C1 480000.232, C2 70000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.241, C1 480000.241, C2 70000.241}, COORD {C3 5000.242, C1 490000.242, C2 80000.242}]}}]}]}, SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.011, C1 480000.011, C2 70000.011}, COORD {C3 5000.012, C1 490000.012, C2 80000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.021, C1 480000.021, C2 70000.021}, COORD {C3 5000.022, C1 490000.022, C2 80000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.031, A1 480000.031, A2 70000.031, r 31, C1 480000.031, C2 70000.031}, COORD {C3 5000.032, C1 480000.032, C2 70000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.041, C1 480000.041, C2 70000.041}, COORD {C3 5000.042, C1 490000.042, C2 80000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.211, C1 480000.211, C2 70000.211}, COORD {C3 5000.212, C1 490000.212, C2 80000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.221, C1 480000.221, C2 70000.221}, COORD {C3 5000.222, C1 490000.222, C2 80000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {C3 5000.231, A1 480000.231, A2 70000.231, r 31, C1 480000.231, C2 70000.231}, COORD {C3 5000.232, C1 480000.232, C2 70000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.241, C1 480000.241, C2 70000.241}, COORD {C3 5000.242, C1 490000.242, C2 80000.242}]}}]}]}]}}
        assertEquals("DataTest1.TopicB.ClassRM", iomObject.getobjecttag());
        assertEquals("oidRM", iomObject.getobjectoid());
        
        IomObject formRM = iomObject.getattrobj("formRM", 0);
        assertNotNull(formRM);
        
        assertEquals("MULTISURFACE {surface [SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.011, C2 70000.011, C3 5000.011}, COORD {C1 490000.012, C2 80000.012, C3 5000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.021, C2 70000.021, C3 5000.021}, COORD {C1 490000.022, C2 80000.022, C3 5000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.031, A2 70000.031, C1 480000.031, C2 70000.031, C3 5000.031, R 31}, COORD {C1 480000.032, C2 70000.032, C3 5000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.041, C2 70000.041, C3 5000.041}, COORD {C1 490000.042, C2 80000.042, C3 5000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.211, C2 70000.211, C3 5000.211}, COORD {C1 490000.212, C2 80000.212, C3 5000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.221, C2 70000.221, C3 5000.221}, COORD {C1 490000.222, C2 80000.222, C3 5000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.231, A2 70000.231, C1 480000.231, C2 70000.231, C3 5000.231, R 31}, COORD {C1 480000.232, C2 70000.232, C3 5000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.241, C2 70000.241, C3 5000.241}, COORD {C1 490000.242, C2 80000.242, C3 5000.242}]}}]}]}, SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.011, C2 70000.011, C3 5000.011}, COORD {C1 490000.012, C2 80000.012, C3 5000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.021, C2 70000.021, C3 5000.021}, COORD {C1 490000.022, C2 80000.022, C3 5000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.031, A2 70000.031, C1 480000.031, C2 70000.031, C3 5000.031, R 31}, COORD {C1 480000.032, C2 70000.032, C3 5000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.041, C2 70000.041, C3 5000.041}, COORD {C1 490000.042, C2 80000.042, C3 5000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.211, C2 70000.211, C3 5000.211}, COORD {C1 490000.212, C2 80000.212, C3 5000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.221, C2 70000.221, C3 5000.221}, COORD {C1 490000.222, C2 80000.222, C3 5000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.231, A2 70000.231, C1 480000.231, C2 70000.231, C3 5000.231, R 31}, COORD {C1 480000.232, C2 70000.232, C3 5000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.241, C2 70000.241, C3 5000.241}, COORD {C1 490000.242, C2 80000.242, C3 5000.242}]}}]}]}, SURFACE {boundary [BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.011, C2 70000.011, C3 5000.011}, COORD {C1 490000.012, C2 80000.012, C3 5000.012}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.021, C2 70000.021, C3 5000.021}, COORD {C1 490000.022, C2 80000.022, C3 5000.022}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.031, A2 70000.031, C1 480000.031, C2 70000.031, C3 5000.031, R 31}, COORD {C1 480000.032, C2 70000.032, C3 5000.032}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.041, C2 70000.041, C3 5000.041}, COORD {C1 490000.042, C2 80000.042, C3 5000.042}]}}]}, BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.211, C2 70000.211, C3 5000.211}, COORD {C1 490000.212, C2 80000.212, C3 5000.212}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.221, C2 70000.221, C3 5000.221}, COORD {C1 490000.222, C2 80000.222, C3 5000.222}]}}, POLYLINE {sequence SEGMENTS {segment [ARC {A1 480000.231, A2 70000.231, C1 480000.231, C2 70000.231, C3 5000.231, R 31}, COORD {C1 480000.232, C2 70000.232, C3 5000.232}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.241, C2 70000.241, C3 5000.241}, COORD {C1 490000.242, C2 80000.242, C3 5000.242}]}}]}]}]}",
                formRM.toString()); // Radius fehlt noch im iomObject
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob eine View, innerhalb eines TopicView, welche nicht transient ist, ohne Fehlermeldung erstellt werden kann.
	@Test
	public void testView_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"View.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();

        // DataTest1.TopicB.ClassA oid oidA {attr1 text}
        assertEquals("DataTest1.TopicB.ClassA", iomObject.getobjecttag());
        assertEquals("oidA", iomObject.getobjectoid());
        
        assertEquals("text", iomObject.getattrvalue("attr1"));
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent); 
		
		event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();

        // DataTest1.AdditionalTopicA.AdditionalClassA oid oidB {attr2 te, attr1 text}
        assertEquals("DataTest1.AdditionalTopicA.AdditionalClassA", iomObject.getobjecttag());
        assertEquals("oidB", iomObject.getobjectoid());
        
        assertEquals("text", iomObject.getattrvalue("attr1"));	
        assertEquals("te", iomObject.getattrvalue("attr2"));  
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob eine View, welche nicht von einer TopicView ist, nicht transient ist, ohne Fehlermeldung erstellt werden kann.
	@Test
	public void testViewNotOfTopicView_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"NotTopicView.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent); // DataTest1.TopicB, bidA
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicB.ClassA oid oidA {attr1 text2}
		try{
			reader.read(); // TopicB != VIEW TOPIC. View1 Failed.
			fail();
		}catch(IoxException ex){
			assertTrue((ex).getMessage().contains("View1"));
		}
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob eine Enumeration: OTHERS erstellt werden kann.
	@Test
	public void testEnumerationOthers_ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"EnumerationOthers.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
        assertTrue(reader.read() instanceof  ObjectEvent);
        assertTrue(reader.read() instanceof  EndBasketEvent);
        assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob eine Enumeration: enumeration.OTHERS erstellt werden kann.
	@Test
	public void testSubEnumerationOthers_ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"subEnumerationOthers.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
        assertTrue(reader.read() instanceof  ObjectEvent);
        assertTrue(reader.read() instanceof  EndBasketEvent);
        assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob eine View, welche von einer TopicView ist, jedoch nicht transient, ohne Fehlermeldung erstellt werden kann.
	@Test
	public void testViewIsTransient_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"ViewIsTransient.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent); // DataTest1.TopicB, bidA
		assertTrue(reader.read() instanceof  ObjectEvent); // DataTest1.TopicB.ClassA oid oidA {attr1 text}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent); // AdditionalClassC == TRANSIENT
		try{
			reader.read();
			fail();
		}catch(IoxException ex){
			assertTrue((ex).getMessage().contains("AdditionalClassC"));
		}
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn eine im XML definierte Geometrie, noch nicht implementiert wurde.
	@Test
	public void testUnsupportedGeometry_Fail()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"UnsupportedGeometry.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); // unsupported geometry OrientableCurve
		assertTrue(reader.read() instanceof  StartBasketEvent);
		try{
			reader.read();
			fail();
		}catch(IoxException ex){
			assertTrue((ex).getMessage().contains("unsupported geometry orientablecurve"));
    		assertTrue(ex instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn bei einer Surface keine Linien definiert wurden.
	@Test
	public void testSurfaceNoLinesFound_Fail()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"UndefinedSurface.xml"));
		reader.setModel(td);
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
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, A1 und A2 bei einer ARC fehlen.
	@Test
	public void testMissingCoord_Fail()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"MissingCoord.xml"));
		reader.setModel(td);
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
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn an einer Stelle unerwuenschte Zeichen vorkommen.
	@Test
	public void testUnexpectedCharacters_Fail()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"UnexpectedCharacters.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); // SyntaxException: unexpected characters: unexpectedText
		assertTrue(reader.read() instanceof  StartBasketEvent);
		try{
			reader.read();
			fail();
		}catch(IoxException ex){
		    EhiLogger.logError(ex);
			assertTrue((ex).getMessage().contains(CHAR_ELE_FAIL+"attrBoolean1"));
    		assertTrue(ex instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
}