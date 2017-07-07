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
import ch.interlis.iom.IomObject;
import ch.interlis.iox.*;

public class Ili23gml20ReaderTest {

	private final static String TEST_IN="src/test/data/Ili23gml20Reader";
	private TransferDescription td=null;
	
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"/Ili23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	@Test
	public void testEmptyTransfer_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"EmptyTransfer.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testSameTopicClassNames_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"TopicClassSameTest.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // TopicZ.TopicZ
		IomObject iomObjA=((ObjectEvent) event).getIomObject();
		String attrtag=iomObjA.getobjecttag();
		assertEquals("Ili23.TopicZ.TopicZ", attrtag);
		
		String oid=iomObjA.getobjectoid();
		assertEquals("x10", oid);
		
		String attrValue=iomObjA.getattrvalue("attr1");
		assertEquals("text", attrValue);
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testArea_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Area.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassG oid x10 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence [SEGMENTS {segment [COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}]}, ARC {A2 310000.000, A1 489000.000, C3 5000.000, C1 481000.000, C2 71000.000}]}}}}}
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassG oid x11 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}]}}}}}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testEmptyBasket_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"EmptyBasket.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent); //("Test1.TopicF","bid1"));
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testSurface_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Surface.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassF oid x10 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence [SEGMENTS {segment [COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}]}, ARC {A2 310000.000, A1 489000.000, C3 5000.000, C1 481000.000, C2 71000.000}]}}}}}
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassF oid x11 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}]}}}}}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testSurface2Boundaries_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Surface2Boundaries.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassF oid x10 {Form MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline POLYLINE {sequence [SEGMENTS {segment [COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}]}, ARC {A2 310000.000, A1 489000.000, C3 5000.000, C1 481000.000, C2 71000.000}]}}, BOUNDARY {polyline POLYLINE {sequence [SEGMENTS {segment [COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}, COORD {C3 5000.000, C1 480000.000, C2 70000.000}]}, ARC {A2 310000.000, A1 489000.000, C3 5000.000, C1 481000.000, C2 71000.000}]}}]}}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testEmptyObjects_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"EmptyObjects.gml"));
		reader.setModel(td);
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
	public void testCoords_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Coord.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassB oid x10 {attr1 SEGMENTS {segment [COORD {C1 480000.000, C2 70000.000}, COORD {C1 480000.000, C2 70000.000}, COORD {C1 480000.000, C2 70000.000}, COORD {C1 480000.000, C2 70000.000}, COORD {C1 480000.000, C2 70000.000}]}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testPolylinesWithArcs_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"PolylineWithArc.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassD oid x11 {attr1 POLYLINE {sequence ARC {A2 70000.000, A1 481000.000, C1 490000.000, C2 71000.000}}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testPolylinesWithArcsAndStraights_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"PolylineWithArcAndStraights.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassD oid x10 {attr1 POLYLINE {sequence [SEGMENTS {segment [COORD {C1 481000.000, C2 70000.000}, COORD {C1 490000.000, C2 71000.000}]}, ARC {A2 70000.000, A1 481000.000, C1 490000.000, C2 71000.000}]}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testPrimitiveDataType_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"PrimitiveAttrs.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassA1 oid x10 {myText text example}
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassB1 oid x11 {myNumber 10}
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassC1 oid x12 {myBoolean true}
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassD1 oid x13 {myAlignment left}
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassE1 oid x14 {myFormatted 10.05.2017}
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassF1 oid x15 {myEnumeration eins}
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassG1 oid x16 {myEnumTree eins.vier}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testEnumerationType_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"PrimitiveAttrsEnumeration.gml"));
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
	
	@Test
	public void testTextType_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"TextTypes.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testNumericDataTypes_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"NumericDataTypes.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testAttributePath_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"AttributePath.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testFormattedTypes_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"FormattedTypes.gml"));
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
	
	@Test
	public void testBlackBoxTypes_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"BlackBoxTypes.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassXml oid x11 {attrXml <?xml version="1.0" encoding="UTF-8"?><xmlElement xmlns="http://www.interlis.ch/ILIGML-2.0/Ili23">xmlAttribute</xmlElement>}
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassBin oid x10 {attrBin [A-Z/a-z/+/1-9/]}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testStructures_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Structures.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassA oid o1 {attr1 StructA {attr2 someText}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testPolylines_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Polyline.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassC oid x10 {attr1 POLYLINE {sequence SEGMENTS {segment [COORD {C1 481000.000, C2 70000.000}, COORD {C1 490000.000, C2 71000.000}]}}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Embedded
	@Test
	public void testEmbedded_1Assoc_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Embedded.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Ili23.Mensch.Mann oid oidM {}
		IomObject iomObjA=((ObjectEvent) event).getIomObject();
		int attrCountA=iomObjA.getattrcount();
		assertEquals(0, attrCountA);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Ili23.Mensch.Frau oid oidF {bezMann -> oidM REF {}}
		IomObject iomObjB=((ObjectEvent) event).getIomObject();
		String refOidB=iomObjB.getattrobj("bezMann", 0).getobjectrefoid();
		assertEquals("oidM", refOidB);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Embedded 2 associations
	@Test
	public void testEmbedded_2Assoc_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Embedded1to1.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Ili23.Atom.Proton oid oidP {electronD -> oidN REF {}}
		IomObject iomObjF=((ObjectEvent) event).getIomObject();
		String refOidF=iomObjF.getattrobj("electronD", 0).getobjectrefoid();
		assertEquals("oidN", refOidF);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Ili23.Atom.Neutron oid oidN {electronA -> oidP REF {}}
		IomObject iomObjM=((ObjectEvent) event).getIomObject();
		String refOidM=iomObjM.getattrobj("electronA", 0).getobjectrefoid();
		assertEquals("oidP", refOidM);
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Stand Alone
	@Test
	public void testStandalone_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"StandAlone.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Ili23.Schule.Kinder oid oidK {}
		IomObject iomObjK=((ObjectEvent) event).getIomObject();
		int attrCountK=iomObjK.getattrcount();
		assertEquals(0, attrCountK);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Ili23.Schule.Lehrer oid oidL {}
		IomObject iomObjL=((ObjectEvent) event).getIomObject();
		int attrCountL=iomObjL.getattrcount();
		assertEquals(0, attrCountL);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); //Ili23.Schule.Beziehung {bezLehrer -> oidL REF {}, bezKinder -> oidK REF {}}
		IomObject iomObjA=((ObjectEvent) event).getIomObject();
		int attrCountA=iomObjA.getattrcount();
		assertEquals(2, attrCountA);
		String refOidK=iomObjA.getattrobj("bezLehrer", 0).getobjectrefoid();
		assertEquals("oidL", refOidK);
		String refOidL=iomObjA.getattrobj("bezKinder", 0).getobjectrefoid();
		assertEquals("oidK", refOidL);
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Embedded && Stand Alone
	@Test
	public void testEmbeddedAndStandAlone_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"RefEmbeddedWithTableBetween.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Ili23.TopicC.ClassA11 oid o1 {}
		IomObject iomObjA11=((ObjectEvent) event).getIomObject();
		int attrCountA11=iomObjA11.getattrcount();
		assertEquals(0, attrCountA11);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Ili23.TopicC.ClassB11 oid o2 {}
		IomObject iomObjB11=((ObjectEvent) event).getIomObject();
		int attrCountB11=iomObjB11.getattrcount();
		assertEquals(0, attrCountB11);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Ili23.TopicC.assocAB oid y1 {assoA11 -> o1 REF {}, assoB11 -> o2 REF {}}
		IomObject iomObjA=((ObjectEvent) event).getIomObject();
		int attrCountA=iomObjA.getattrcount();
		assertEquals(2, attrCountA);
		String refOidL=iomObjA.getattrobj("assoA11", 0).getobjectrefoid();
		assertEquals("o1", refOidL);
		String refOidK=iomObjA.getattrobj("assoB11", 0).getobjectrefoid();
		assertEquals("o2", refOidK);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Ili23.TopicC.assocAB {assoA11 -> o2 REF {}, assoB11 -> o1 REF {}}
		IomObject iomObjB=((ObjectEvent) event).getIomObject();
		int attrCountB=iomObjB.getattrcount();
		assertEquals(2, attrCountB);
		String refOidL2=iomObjB.getattrobj("assoA11", 0).getobjectrefoid();
		assertEquals("o2", refOidL2);
		String refOidK2=iomObjB.getattrobj("assoB11", 0).getobjectrefoid();
		assertEquals("o1", refOidK2);
		
		assertTrue(reader.read() instanceof  EndBasketEvent); 
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Partial Embedded && Stand Alone
	@Test
	public void testPartialEmbeddedAndStandAlone_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"RefPartialEmbeddedWithTableBetween.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Ili23.TopicE.ClassE1 oid o1 {}
		IomObject iomObjE1=((ObjectEvent) event).getIomObject();
		int attrCountE1=iomObjE1.getattrcount();
		assertEquals(0, attrCountE1);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Ili23.TopicE.ClassE2 oid o2 {}
		IomObject iomObjE2=((ObjectEvent) event).getIomObject();
		int attrCountE2=iomObjE2.getattrcount();
		assertEquals(0, attrCountE2);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Ili23.TopicE.assoE12 oid e12 {assoE1 -> o1 REF {}, assoE2 -> o2 REF {}}
		IomObject iomObjA=((ObjectEvent) event).getIomObject();
		int attrCountA=iomObjA.getattrcount();
		assertEquals(2, attrCountA);
		String refOidE1=iomObjA.getattrobj("assoE1", 0).getobjectrefoid();
		assertEquals("o1", refOidE1);
		String refOidE2=iomObjA.getattrobj("assoE2", 0).getobjectrefoid();
		assertEquals("o2", refOidE2);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Ili23.TopicE.assoE12 {assoE1 -> o2 REF {}, assoE2 -> o1 REF {}}
		IomObject iomObjB=((ObjectEvent) event).getIomObject();
		int attrCountB=iomObjB.getattrcount();
		assertEquals(2, attrCountB);
		String refOidE12=iomObjB.getattrobj("assoE1", 0).getobjectrefoid();
		assertEquals("o2", refOidE12);
		String refOidE22=iomObjB.getattrobj("assoE2", 0).getobjectrefoid();
		assertEquals("o1", refOidE22);
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testUnsupportedGeometry_Fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"UnsupportedGeometry.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); // unsupported geometry OrientableCurve
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
	public void testInterpolationNotValid_Fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Interpolation.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); // unsupported interpolation unknown
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
	public void testUnexpectedEndElement_Fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"UndefinedSurface.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); // unexpected end element polygon
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
	public void testPolygonNoLinesFound_Fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"UndefinedSurface2.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); // no lines found in polygon of Form.
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
	public void testMissingLineCoords_Fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"MissingLineCoords.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); // missing coord for 2d values
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
	public void testTooManyLineCoords_Fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"TooManyLineCoords.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); // missing coord values for 2d Coord
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
	public void testMissingArcCoords_Fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"MissingArcCoords.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); // missing arc pos values
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
	public void testTooManyArcCoords_Fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"TooManyArcCoords.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); // too many arc pos values
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
	public void testWrongBoundaryOrder_Fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"WrongBoundaryOrder.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); // Syntax error: no exterior ring found
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
	public void testUndefinedArea_Fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"UndefinedArea.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); // SyntaxException: unexpected end element Polygon
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
	public void testWrongTopEleNamespace_Fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"WrongTopEleNamespace.gml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ex){
			
		}
		reader.close();
		reader=null;
	}
	
	@Test
	public void testUnexpectedCharacters_Fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"UnexpectedCharacters.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); // SyntaxException: unexpected characters: unexpectedText
		try{
			reader.read();
			fail();
		}catch(IoxException ex){
			
		}
		reader.close();
		reader=null;
	}
	
	@Test
	public void testUnexpectedEvent_Fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"UnexpectedEvent.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); //IoxSyntaxException: unexpected element: attr1;
		assertTrue(reader.read() instanceof  StartBasketEvent);
		try{
			reader.read();
			fail();
		}catch(IllegalArgumentException ex){
			
		}
		reader.close();
		reader=null;
	}
	
	@Test
	public void testSrsDimension_Fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"SrsDimension.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent); // syntax error. no dimension defined.
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
	public void testWrongTopEleName_Fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"WrongTopEleName.gml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ex){
			
		}
		reader.close();
		reader=null;
	}
}