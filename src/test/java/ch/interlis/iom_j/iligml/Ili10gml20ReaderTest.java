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
	public void testEmptyTransfer_ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"EmptyTransfer.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testEmptyBasket_ok() throws IoxException {
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
	public void testEmptyObjects_ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"EmptyObjects.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); //Ili10.TopicF.TableF1 oid x21 {}
		assertTrue(reader.read() instanceof  ObjectEvent); //Ili10.TopicF.TableF1 oid x20 {}
		assertTrue(reader.read() instanceof  ObjectEvent); //Ili10.TopicF.TableF0 oid x10 {}
		assertTrue(reader.read() instanceof  ObjectEvent); //Ili10.TopicF.TableF0 oid x11 {}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testPrimitiveDataType_ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"PrimitiveAttrs.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent); //("Test1.TopicA","bid1"));
		assertTrue(reader.read() instanceof  ObjectEvent); //Ili10.TopicH.TableA1 oid x10 {myText text example}
		assertTrue(reader.read() instanceof  ObjectEvent); //Ili10.TopicH.TableB1 oid x11 {myNumber 10}
		assertTrue(reader.read() instanceof  ObjectEvent); //Ili10.TopicH.TableD1 oid x13 {myAlignment left}
		assertTrue(reader.read() instanceof  ObjectEvent); //Ili10.TopicH.TableF1 oid x15 {myEnumeration eins}
		assertTrue(reader.read() instanceof  ObjectEvent); //Ili10.TopicH.TableG1 oid x16 {myEnumTree eins.vier}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testSurface_ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Surface.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); //Ili10.TopicA.TableA oid x10 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence [ARC {A2 110.0, A1 110.0, C1 115.0, C2 108.0}, SEGMENTS {segment [COORD {C1 120.0, C2 110.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 110.0, C2 110.0}]}]}}}}}
		assertTrue(reader.read() instanceof  ObjectEvent); //Ili10.TopicA.TableA oid x11 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 115.0, C2 115.0}, COORD {C1 115.0, C2 120.0}, COORD {C1 112.0, C2 120.0}, COORD {C1 110.0, C2 110.0}]}}}}}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testArea_ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Area.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);  //Ili10.TopicB.TableB oid x10 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence [ARC {A2 110.0, A1 110.0, C1 115.0, C2 108.0}, SEGMENTS {segment [COORD {C1 120.0, C2 110.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 110.0, C2 110.0}]}]}}}}}
		assertTrue(reader.read() instanceof  ObjectEvent); //Ili10.TopicB.TableB oid x11 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 115.0, C2 115.0}, COORD {C1 115.0, C2 120.0}, COORD {C1 112.0, C2 120.0}, COORD {C1 110.0, C2 110.0}]}}}}}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testReference_ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Reference.gml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Ili10.TopicG.ClassG2 oid x20 {attrRefG1 -> x10 REF {}}
		IomObject iomObjG2=((ObjectEvent) event).getIomObject();
		String refOidG2=iomObjG2.getattrobj("attrRefG1", 0).getobjectrefoid();
		assertEquals("x10", refOidG2);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Ili10.TopicG.ClassG1 oid x10 {}
		IomObject iomObjG11=((ObjectEvent) event).getIomObject();
		assertEquals("x10", iomObjG11.getobjectoid());
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Ili10.TopicG.ClassG3 oid x30 {attrRefG1 -> x10 REF {}}
		IomObject iomObjG5=((ObjectEvent) event).getIomObject();
		String refOidG5=iomObjG5.getattrobj("attrRefG1", 0).getobjectrefoid();
		assertEquals("x10", refOidG5);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Ili10.TopicG.ClassG1 oid x11 {}
		IomObject iomObjA=((ObjectEvent) event).getIomObject();
		int attrCountA=iomObjA.getattrcount();
		assertEquals(0, attrCountA);
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testWrongTopEleName_fail() throws IoxException {
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
	
	@Test
	public void testWrongTopEleNamespace_fail() throws IoxException {
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
	public void testUndefinedSurface_fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"UndefinedSurface.gml"));
		reader.setModel(td);
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
	public void testUndefinedArea_fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"UndefinedArea.gml"));
		reader.setModel(td);
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
}