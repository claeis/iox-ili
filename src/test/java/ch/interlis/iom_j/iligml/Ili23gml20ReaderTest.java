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
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	//@Test // Not yet implemented
	public void testReference_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Reference.gml"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testArea_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Area.gml"));
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
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
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
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	//@Test // Not yet implemented
	public void testAttributePath_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"AttributePath.gml"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	//@Test // Not yet implemented
	public void testFormattedTypes_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"FormattedTypes.gml"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	//@Test // Not yet implemented
	public void testBlackBoxTypes_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"BlackBoxTypes.gml"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testStructures_Ok() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Structures.gml"));
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
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); //ClassC oid x10 {attr1 POLYLINE {sequence SEGMENTS {segment [COORD {C1 481000.000, C2 70000.000}, COORD {C1 490000.000, C2 71000.000}]}}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void testUnsupportedGeometry_Fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"UnsupportedGeometry.gml"));
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
		assertTrue(reader.read() instanceof  StartTransferEvent); //IllegalArgumentException("illegal argument obj (=null)");
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
		try{
			reader.read();
			fail();
		}catch(IoxException ex){
			
		}
		reader.close();
		reader=null;
	}
	
	//@Test // Not yet implemented
	public void testUndefinedReference_Fail() throws IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"UndefinedReference.gml"));
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