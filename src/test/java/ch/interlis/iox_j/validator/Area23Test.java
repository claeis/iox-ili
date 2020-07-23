package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iom_j.xtf.XtfWriter;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.logging.LogEventImpl;

public class Area23Test {

	private TransferDescription td=null;
	// OID
	private final static String OID1 ="o1";
	private final static String OID2 ="o2";
	private final static String OID3 ="o3";
	private final static String OID4 ="o4";
	// MODEL.TOPIC
	private final static String ILI_TOPIC="Datatypes23.Topic";
	// CLASS
	private final static String ILI_CLASSD=ILI_TOPIC+".ClassD";
	// START BASKET EVENT
	private final static String BID="b1";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/Datatypes23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}

	// prueft ob ein Polygon erstellt werden kann.
	@Test
	public void onePolygon_1Boundary_Area_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft ob ein Polygon erstellt werden kann, wenn sie sich selber an 1 Punkt beruehrt.
	@Test
	public void onePolygon_1Boundary_OuterboundaryTouchesOwnLineOn1Point_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "480000.000");
		startSegmentInner.setattrvalue("C2", "70000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "500000.000");
		endSegmentInner.setattrvalue("C2", "78000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "500000.000");
		startSegment2Inner.setattrvalue("C2", "78000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "505000.000");
		endSegment2Inner.setattrvalue("C2", "78000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "505000.000");
		startSegment3Inner.setattrvalue("C2", "78000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "480000.000");
		endSegment3Inner.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// prueft ob ein Polygon, bestehend aus 2 Innerboundaries welche sich
	// nicht beruehren und 1 Outerboundary erstellt werden kann.
	@Test
	public void onePolygon_3Boundaries_2Innerboundaries_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "850000.000");
		endSegment.setattrvalue("C2", "191500.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "850000.000");
		startSegment2.setattrvalue("C2", "191500.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "480000.000");
		endSegment2.setattrvalue("C2", "310000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "480000.000");
		startSegment3.setattrvalue("C2", "310000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "486000.000");
		startSegmentInner.setattrvalue("C2", "199000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "486000.000");
		endSegmentInner.setattrvalue("C2", "201000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "486000.000");
		startSegment2Inner.setattrvalue("C2", "201000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "488000.000");
		endSegment2Inner.setattrvalue("C2", "201000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "488000.000");
		startSegment3Inner.setattrvalue("C2", "201000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "486000.000");
		endSegment3Inner.setattrvalue("C2", "199000.000");
		// 2. inner boundary
		IomObject innerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner2=polylineValueInner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		startSegmentInner2.setattrvalue("C1", "490500.000");
		startSegmentInner2.setattrvalue("C2", "199000.000");
		IomObject endSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		endSegmentInner2.setattrvalue("C1", "490500.000");
		endSegmentInner2.setattrvalue("C2", "201000.000");
		// polyline 2
		IomObject polylineValue2Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner2=polylineValue2Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		startSegment2Inner2.setattrvalue("C1", "490500.000");
		startSegment2Inner2.setattrvalue("C2", "201000.000");
		IomObject endSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		endSegment2Inner2.setattrvalue("C1", "489500.000");
		endSegment2Inner2.setattrvalue("C2", "200000.000");
		// polyline 3
		IomObject polylineValue3Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner2=polylineValue3Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		startSegment3Inner2.setattrvalue("C1", "489500.000");
		startSegment3Inner2.setattrvalue("C2", "200000.000");
		IomObject endSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		endSegment3Inner2.setattrvalue("C1", "490500.000");
		endSegment3Inner2.setattrvalue("C2", "199000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft ob ein Polygon, bestehend aus 2 Innerboundaries welche sich an einem
	// Punkt beruehren und einem Outerboundary erstellt werden kann.
	@Test
	public void onePolygon_3Boundaries_InnerBoundariesTouchesEachOtherOn1Point_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "850000.000");
		endSegment.setattrvalue("C2", "191500.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "850000.000");
		startSegment2.setattrvalue("C2", "191500.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "480000.000");
		endSegment2.setattrvalue("C2", "310000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "480000.000");
		startSegment3.setattrvalue("C2", "310000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "486000.000");
		startSegmentInner.setattrvalue("C2", "199000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "486000.000");
		endSegmentInner.setattrvalue("C2", "201000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "486000.000");
		startSegment2Inner.setattrvalue("C2", "201000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "488000.000");
		endSegment2Inner.setattrvalue("C2", "201000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "488000.000");
		startSegment3Inner.setattrvalue("C2", "201000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "486000.000");
		endSegment3Inner.setattrvalue("C2", "199000.000");
		// 2. inner boundary
		IomObject innerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner2=polylineValueInner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		startSegmentInner2.setattrvalue("C1", "488000.000");
		startSegmentInner2.setattrvalue("C2", "201000.000");
		IomObject endSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		endSegmentInner2.setattrvalue("C1", "490500.000");
		endSegmentInner2.setattrvalue("C2", "201000.000");
		// polyline 2
		IomObject polylineValue2Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner2=polylineValue2Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		startSegment2Inner2.setattrvalue("C1", "490500.000");
		startSegment2Inner2.setattrvalue("C2", "201000.000");
		IomObject endSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		endSegment2Inner2.setattrvalue("C1", "489500.000");
		endSegment2Inner2.setattrvalue("C2", "200000.000");
		// polyline 3
		IomObject polylineValue3Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner2=polylineValue3Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		startSegment3Inner2.setattrvalue("C1", "489500.000");
		startSegment3Inner2.setattrvalue("C2", "200000.000");
		IomObject endSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		endSegment3Inner2.setattrvalue("C1", "488000.000");
		endSegment3Inner2.setattrvalue("C2", "201000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft ob ein Polygon, bestehend aus 1 Outerboundary und 1 Innerboundary erstellt werden kann.
	@Test
	public void onePolygon_2Boundaries_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "500000.000");
		startSegmentInner.setattrvalue("C2", "77000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "500000.000");
		endSegmentInner.setattrvalue("C2", "78000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "500000.000");
		startSegment2Inner.setattrvalue("C2", "78000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "505000.000");
		endSegment2Inner.setattrvalue("C2", "78000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "505000.000");
		startSegment3Inner.setattrvalue("C2", "78000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "500000.000");
		endSegment3Inner.setattrvalue("C2", "77000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft ob eine Fehlermeldung ausgegeben werden kann, wenn die Innerboundary, die Outerboundary
	// an einem Punkt auf der Linie beruehrt.
	@Test
	public void onePolygon_2Boundaries_InnerPointTouchesOuterBoundaryLine_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "500000.000");
		startSegment.setattrvalue("C2", "100000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "600000.000");
		endSegment.setattrvalue("C2", "100000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "600000.000");
		startSegment2.setattrvalue("C2", "100000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "600000.000");
		endSegment2.setattrvalue("C2", "200000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "600000.000");
		startSegment3.setattrvalue("C2", "200000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "500000.000");
		endSegment3.setattrvalue("C2", "200000.000");
		// polyline 4
		IomObject polylineValue4 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments4=polylineValue4.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment4=segments4.addattrobj("segment", "COORD");
		startSegment4.setattrvalue("C1", "500000.000");
		startSegment4.setattrvalue("C2", "200000.000");
		IomObject endSegment4=segments4.addattrobj("segment", "COORD");
		endSegment4.setattrvalue("C1", "500000.000");
		endSegment4.setattrvalue("C2", "100000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "550000.000");
		startSegmentInner.setattrvalue("C2", "100000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "580000.000");
		endSegmentInner.setattrvalue("C2", "150000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "580000.000");
		startSegment2Inner.setattrvalue("C2", "150000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "520000.000");
		endSegment2Inner.setattrvalue("C2", "150000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "520000.000");
		startSegment3Inner.setattrvalue("C2", "150000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "550000.000");
		endSegment3Inner.setattrvalue("C2", "100000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(2,logger.getErrs().size());
		assertEquals("Intersection coord1 (550000.000, 100000.000), tids o1, o1", logger.getErrs().get(0).getEventMsg());
		assertEquals("Intersection coord1 (550000.000, 100000.000), tids o1, o1", logger.getErrs().get(1).getEventMsg());
	}
	
	// prueft, ob 2 Polygone die sich ueberlappen mit ausgeschalteter
	// AREA Topologyvalidierung akzeptiert werden.
	@Test
	public void twoPolygon_OverlapEachOther_TopValidationOff_Ok(){
		Iom_jObject polygon1=new Iom_jObject(ILI_CLASSD, OID1);
		{
			IomObject multisurface=polygon1.addattrobj("area2d", "MULTISURFACE");
			IomObject surface = multisurface.addattrobj("surface", "SURFACE");
			// outer boundary
			IomObject outerBoundary = surface.addattrobj("boundary", "BOUNDARY");
			IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
			IomObject seg0=segments.addattrobj("segment", "COORD");
			seg0.setattrvalue("C1", "480000.000");
			seg0.setattrvalue("C2", "77000.000");
			IomObject seg1=segments.addattrobj("segment", "COORD");
			seg1.setattrvalue("C1", "550000.000");
			seg1.setattrvalue("C2", "77000.000");
			IomObject seg2=segments.addattrobj("segment", "COORD");
			seg2.setattrvalue("C1", "550000.000");
			seg2.setattrvalue("C2", "78000.000");
			IomObject seg3=segments.addattrobj("segment", "COORD");
			seg3.setattrvalue("C1", "480000.000");
			seg3.setattrvalue("C2", "77000.000");
		}
		Iom_jObject polygon2=new Iom_jObject(ILI_CLASSD, OID2);
		{
			IomObject multisurface=polygon2.addattrobj("area2d", "MULTISURFACE");
			IomObject surface = multisurface.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surface.addattrobj("boundary", "BOUNDARY");
			IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
			IomObject seg0=segments.addattrobj("segment", "COORD");
			seg0.setattrvalue("C1", "500000.000");
			seg0.setattrvalue("C2", "70000.000");
			IomObject seg1=segments.addattrobj("segment", "COORD");
			seg1.setattrvalue("C1", "500000.000");
			seg1.setattrvalue("C2", "80000.000");
			IomObject seg2=segments.addattrobj("segment", "COORD");
			seg2.setattrvalue("C1", "505000.000");
			seg2.setattrvalue("C2", "80000.000");
			IomObject seg3=segments.addattrobj("segment", "COORD");
			seg3.setattrvalue("C1", "500000.000");
			seg3.setattrvalue("C2", "70000.000");
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.AREA_OVERLAP_VALIDATION, ValidationConfig.OFF);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(polygon1));
		validator.validate(new ObjectEvent(polygon2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn 2 Polygone sich an 4 Stellen ueberlappen.
	@Test
	public void twoPolygon_OverlapEachOther_Fail(){
		Iom_jObject polygon1=new Iom_jObject(ILI_CLASSD, OID1);
		{
			IomObject multisurface=polygon1.addattrobj("area2d", "MULTISURFACE");
			IomObject surface = multisurface.addattrobj("surface", "SURFACE");
			// outer boundary
			IomObject outerBoundary = surface.addattrobj("boundary", "BOUNDARY");
			IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
			IomObject seg0=segments.addattrobj("segment", "COORD");
			seg0.setattrvalue("C1", "480000.000");
			seg0.setattrvalue("C2", "77000.000");
			IomObject seg1=segments.addattrobj("segment", "COORD");
			seg1.setattrvalue("C1", "550000.000");
			seg1.setattrvalue("C2", "77000.000");
			IomObject seg2=segments.addattrobj("segment", "COORD");
			seg2.setattrvalue("C1", "550000.000");
			seg2.setattrvalue("C2", "78000.000");
			IomObject seg3=segments.addattrobj("segment", "COORD");
			seg3.setattrvalue("C1", "480000.000");
			seg3.setattrvalue("C2", "77000.000");
		}
		Iom_jObject polygon2=new Iom_jObject(ILI_CLASSD, OID2);
		{
			IomObject multisurface=polygon2.addattrobj("area2d", "MULTISURFACE");
			IomObject surface = multisurface.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surface.addattrobj("boundary", "BOUNDARY");
			IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
			IomObject seg0=segments.addattrobj("segment", "COORD");
			seg0.setattrvalue("C1", "500000.000");
			seg0.setattrvalue("C2", "70000.000");
			IomObject seg1=segments.addattrobj("segment", "COORD");
			seg1.setattrvalue("C1", "500000.000");
			seg1.setattrvalue("C2", "80000.000");
			IomObject seg2=segments.addattrobj("segment", "COORD");
			seg2.setattrvalue("C1", "505000.000");
			seg2.setattrvalue("C2", "80000.000");
			IomObject seg3=segments.addattrobj("segment", "COORD");
			seg3.setattrvalue("C1", "500000.000");
			seg3.setattrvalue("C2", "70000.000");
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(polygon1));
		validator.validate(new ObjectEvent(polygon2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==5);
		assertEquals("Intersection coord1 (500000.000, 77000.000), tids o1, o2", logger.getErrs().get(0).getEventMsg());
		assertEquals("Intersection coord1 (503500.000, 77000.000), tids o1, o2", logger.getErrs().get(1).getEventMsg());
		assertEquals("Intersection coord1 (500000.000, 77285.714), tids o1, o2", logger.getErrs().get(2).getEventMsg());
		assertEquals("Intersection coord1 (503669.065, 77338.129), tids o1, o2", logger.getErrs().get(3).getEventMsg());
		assertEquals("failed to validate AREA Datatypes23.Topic.ClassD.area2d", logger.getErrs().get(4).getEventMsg());
	}
    @Test
    public void twoPolygon_OneInsideOther_Fail(){
        Iom_jObject polygon1=new Iom_jObject(ILI_CLASSD, OID1);
        {
            IomObject multisurface=polygon1.addattrobj("area2d", "MULTISURFACE");
            IomObject surface = multisurface.addattrobj("surface", "SURFACE");
            // outer boundary
            IomObject outerBoundary = surface.addattrobj("boundary", "BOUNDARY");
            IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
            IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
            IomObject seg0=segments.addattrobj("segment", "COORD");
            seg0.setattrvalue("C1", "480000.000");
            seg0.setattrvalue("C2", "70000.000");
            IomObject seg1=segments.addattrobj("segment", "COORD");
            seg1.setattrvalue("C1", "550000.000");
            seg1.setattrvalue("C2", "70000.000");
            IomObject seg2=segments.addattrobj("segment", "COORD");
            seg2.setattrvalue("C1", "550000.000");
            seg2.setattrvalue("C2", "80000.000");
            IomObject seg3=segments.addattrobj("segment", "COORD");
            seg3.setattrvalue("C1", "480000.000");
            seg3.setattrvalue("C2", "80000.000");
            IomObject seg4=segments.addattrobj("segment", "COORD");
            seg4.setattrvalue("C1", "480000.000");
            seg4.setattrvalue("C2", "70000.000");
        }
        Iom_jObject polygon2=new Iom_jObject(ILI_CLASSD, OID2);
        {
            IomObject multisurface=polygon2.addattrobj("area2d", "MULTISURFACE");
            IomObject surface = multisurface.addattrobj("surface", "SURFACE");
            IomObject outerBoundary = surface.addattrobj("boundary", "BOUNDARY");
            IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
            IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
            IomObject seg0=segments.addattrobj("segment", "COORD");
            seg0.setattrvalue("C1", "500000.000");
            seg0.setattrvalue("C2", "72000.000");
            IomObject seg1=segments.addattrobj("segment", "COORD");
            seg1.setattrvalue("C1", "505000.000");
            seg1.setattrvalue("C2", "72000.000");
            IomObject seg2=segments.addattrobj("segment", "COORD");
            seg2.setattrvalue("C1", "505000.000");
            seg2.setattrvalue("C2", "78000.000");
            IomObject seg3=segments.addattrobj("segment", "COORD");
            seg3.setattrvalue("C1", "500000.000");
            seg3.setattrvalue("C2", "78000.000");
            IomObject seg4=segments.addattrobj("segment", "COORD");
            seg4.setattrvalue("C1", "500000.000");
            seg4.setattrvalue("C2", "72000.000");
        }
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
        validator.validate(new ObjectEvent(polygon1));
        validator.validate(new ObjectEvent(polygon2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(2,logger.getErrs().size());
        assertEquals("polygons overlay tid1 o1, tid2 o2", logger.getErrs().get(0).getEventMsg());
        assertEquals("failed to validate AREA Datatypes23.Topic.ClassD.area2d", logger.getErrs().get(1).getEventMsg());
    }
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn die Outerboundary des zweiten Polygons,
	// die Outer und Innerboundary des ersten Polygons ueberschneidet.
	@Test
	public void twoPolygon_Polygon1OverlapsPolygon2InnerAndOuterBoundary_Fail(){
		Iom_jObject polygon1=new Iom_jObject(ILI_CLASSD, OID1);
		{
			IomObject multisurface=polygon1.addattrobj("area2d", "MULTISURFACE");
			IomObject surface = multisurface.addattrobj("surface", "SURFACE");
			// outer boundary
			IomObject outerBoundary = surface.addattrobj("boundary", "BOUNDARY");
			{
				// polyline 1
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "500000.000");
					startSegments.setattrvalue("C2", "100000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "600000.000");
					endSegments.setattrvalue("C2", "100000.000");
				}
				// polyline 2
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "600000.000");
					startSegments.setattrvalue("C2", "100000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "600000.000");
					endSegments.setattrvalue("C2", "200000.000");
				}
				// polyline 3
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "600000.000");
					startSegments.setattrvalue("C2", "200000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "500000.000");
					endSegments.setattrvalue("C2", "200000.000");
				}
				// polyline 4
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "500000.000");
					startSegments.setattrvalue("C2", "200000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "500000.000");
					endSegments.setattrvalue("C2", "100000.000");
				}
			}
			// outer boundary
			IomObject innerBoundary = surface.addattrobj("boundary", "BOUNDARY");
			{
				// polyline 1
				{
					IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "520000.000");
					startSegments.setattrvalue("C2", "120000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "580000.000");
					endSegments.setattrvalue("C2", "120000.000");
				}
				// polyline 2
				{
					IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "580000.000");
					startSegments.setattrvalue("C2", "120000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "580000.000");
					endSegments.setattrvalue("C2", "180000.000");
				}
				// polyline 3
				{
					IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "580000.000");
					startSegments.setattrvalue("C2", "180000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "520000.000");
					endSegments.setattrvalue("C2", "180000.000");
				}
				// polyline 4
				{
					IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "520000.000");
					startSegments.setattrvalue("C2", "180000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "520000.000");
					endSegments.setattrvalue("C2", "120000.000");
				}
			}
		}
		Iom_jObject polygon2=new Iom_jObject(ILI_CLASSD, OID2);
		{
			IomObject multisurface=polygon2.addattrobj("area2d", "MULTISURFACE");
			IomObject surface = multisurface.addattrobj("surface", "SURFACE");
			// outer boundary
			IomObject outerBoundary = surface.addattrobj("boundary", "BOUNDARY");
			{
				// polyline 1
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "540000.000");
					startSegments.setattrvalue("C2", "250000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "560000.000");
					endSegments.setattrvalue("C2", "250000.000");
				}
				// polyline 2
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "560000.000");
					startSegments.setattrvalue("C2", "250000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "550000.000");
					endSegments.setattrvalue("C2", "150000.000");
				}
				// polyline 3
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "550000.000");
					startSegments.setattrvalue("C2", "150000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "540000.000");
					endSegments.setattrvalue("C2", "250000.000");
				}
			}
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(polygon1));
		validator.validate(new ObjectEvent(polygon2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==5);
		assertEquals("Intersection coord1 (545000.000, 200000.000), tids o1, o2", logger.getErrs().get(0).getEventMsg());
		assertEquals("Intersection coord1 (555000.000, 200000.000), tids o1, o2", logger.getErrs().get(1).getEventMsg());
		assertEquals("Intersection coord1 (547000.000, 180000.000), tids o1, o2", logger.getErrs().get(2).getEventMsg());
		assertEquals("Intersection coord1 (553000.000, 180000.000), tids o1, o2", logger.getErrs().get(3).getEventMsg());
		assertEquals("failed to validate AREA Datatypes23.Topic.ClassD.area2d", logger.getErrs().get(4).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn die Innerboundary,
	// die Flaeche der Outerboundary unterteilt.
	@Test
	public void onePolygon_InnerboundaryDevidesOuterboundary_Fail(){
		Iom_jObject polygon1=new Iom_jObject(ILI_CLASSD, OID1);
			IomObject multisurface=polygon1.addattrobj("area2d", "MULTISURFACE");
			IomObject surface = multisurface.addattrobj("surface", "SURFACE");
			// outer boundary
			IomObject outerBoundary = surface.addattrobj("boundary", "BOUNDARY");
			{
			// polyline 1
			{
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegments=segments.addattrobj("segment", "COORD");
				startSegments.setattrvalue("C1", "500000.000");
				startSegments.setattrvalue("C2", "100000.000");
				IomObject endSegments=segments.addattrobj("segment", "COORD");
				endSegments.setattrvalue("C1", "600000.000");
				endSegments.setattrvalue("C2", "100000.000");
			}
			// polyline 2
			{
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegments=segments.addattrobj("segment", "COORD");
				startSegments.setattrvalue("C1", "600000.000");
				startSegments.setattrvalue("C2", "100000.000");
				IomObject endSegments=segments.addattrobj("segment", "COORD");
				endSegments.setattrvalue("C1", "600000.000");
				endSegments.setattrvalue("C2", "200000.000");
			}
			// polyline 3
			{
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegments=segments.addattrobj("segment", "COORD");
				startSegments.setattrvalue("C1", "600000.000");
				startSegments.setattrvalue("C2", "200000.000");
				IomObject endSegments=segments.addattrobj("segment", "COORD");
				endSegments.setattrvalue("C1", "500000.000");
				endSegments.setattrvalue("C2", "200000.000");
			}
			// polyline 4
			{
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegments=segments.addattrobj("segment", "COORD");
				startSegments.setattrvalue("C1", "500000.000");
				startSegments.setattrvalue("C2", "200000.000");
				IomObject endSegments=segments.addattrobj("segment", "COORD");
				endSegments.setattrvalue("C1", "500000.000");
				endSegments.setattrvalue("C2", "100000.000");
			}
		}
		// outer boundary
		IomObject innerBoundary = surface.addattrobj("boundary", "BOUNDARY");
		{
			// polyline 1
			{
				IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegments=segments.addattrobj("segment", "COORD");
				startSegments.setattrvalue("C1", "500000.000");
				startSegments.setattrvalue("C2", "100000.000");
				IomObject endSegments=segments.addattrobj("segment", "COORD");
				endSegments.setattrvalue("C1", "580000.000");
				endSegments.setattrvalue("C2", "120000.000");
			}
			// polyline 2
			{
				IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegments=segments.addattrobj("segment", "COORD");
				startSegments.setattrvalue("C1", "580000.000");
				startSegments.setattrvalue("C2", "120000.000");
				IomObject endSegments=segments.addattrobj("segment", "COORD");
				endSegments.setattrvalue("C1", "600000.000");
				endSegments.setattrvalue("C2", "200000.000");
			}
			// polyline 3
			{
				IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegments=segments.addattrobj("segment", "COORD");
				startSegments.setattrvalue("C1", "600000.000");
				startSegments.setattrvalue("C2", "200000.000");
				IomObject endSegments=segments.addattrobj("segment", "COORD");
				endSegments.setattrvalue("C1", "500000.000");
				endSegments.setattrvalue("C2", "100000.000");
			}
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(polygon1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==3);
		assertEquals("superfluous outerboundary tid o1", logger.getErrs().get(0).getEventMsg());
		assertEquals("superfluous outerboundary tid o1", logger.getErrs().get(1).getEventMsg());
        assertEquals("multipolygon detected", logger.getErrs().get(2).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn 2 Polygone exakt
	// uebereinander liegen.
	@Test
	public void twoPolygon_OverlayExactlyOnEachOther_Fail(){
		Iom_jObject polygon1=new Iom_jObject(ILI_CLASSD, OID1);
		{
			IomObject multisurfaceValue=polygon1.addattrobj("area2d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline 1
			IomObject polyline1 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polyline1.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "100000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "100000.000");
			}
			// polyline 2
			IomObject polyline2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polyline2.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "100000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "200000.000");
			}
			// polyline 3
			IomObject polyline3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polyline3.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "200000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "200000.000");
			}
			// polyline 4
			IomObject polyline4 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polyline4.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "200000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "100000.000");
			}
		}
		Iom_jObject polygon2=new Iom_jObject(ILI_CLASSD, OID2);
		{
			IomObject multisurfaceValue=polygon2.addattrobj("area2d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline 1
			IomObject polyline1 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polyline1.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "100000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "100000.000");
			}
			// polyline 2
			IomObject polyline2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polyline2.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "100000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "200000.000");
			}
			// polyline 3
			IomObject polyline3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polyline3.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "200000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "200000.000");
			}
			// polyline 4
			IomObject polyline4 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polyline4.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "200000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "100000.000");
			}
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        //settings.setValue(Validator.CONFIG_DEBUG_XTFOUT,"src/test/data/validator/twoPolygon_OverlayExactlyOnEachOther_Fail.xtf");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(polygon1));
		validator.validate(new ObjectEvent(polygon2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("polygons overlay tid1 o1, tid2 o2", logger.getErrs().get(0).getEventMsg());
		assertEquals("failed to validate AREA Datatypes23.Topic.ClassD.area2d", logger.getErrs().get(1).getEventMsg());
	}
	
	// prueft ob ein Polygon mit einem Kreisbogen erstellt werden kann.
	@Test
	public void onePolygon_1Boundary_AreaWithArc_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "480001.000");
		endSegment.setattrvalue("C2", "70001.000");
		// Arc
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "480000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "480000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft, ob 2 Poygone erstellt werden koennen, wenn beide Polygone
	// sich an einem Punkt beruehren.
	@Test
	public void twoPolygon_PolygonTouchesOtherPolygon_On1Point_Ok(){
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objAreaSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "500000.000");
		startSegment.setattrvalue("C2", "100000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "600000.000");
		endSegment.setattrvalue("C2", "100000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "600000.000");
		startSegment2.setattrvalue("C2", "100000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "600000.000");
		endSegment2.setattrvalue("C2", "200000.000");
		// polyline 3
		IomObject polylineValue4 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments4=polylineValue4.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment4=segments4.addattrobj("segment", "COORD");
		startSegment4.setattrvalue("C1", "600000.000");
		startSegment4.setattrvalue("C2", "200000.000");
		IomObject endSegment4=segments4.addattrobj("segment", "COORD");
		endSegment4.setattrvalue("C1", "500000.000");
		endSegment4.setattrvalue("C2", "200000.000");
		// polyline 4
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "500000.000");
		startSegment3.setattrvalue("C2", "200000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "500000.000");
		endSegment3.setattrvalue("C2", "100000.000");
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSD, OID2);
		IomObject multisurfaceValue2=objAreaSuccess2.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "600000.000");
		startSegmentInner.setattrvalue("C2", "200000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "650000.000");
		endSegmentInner.setattrvalue("C2", "200000.000");
		// polyline 2
		IomObject polylineValue2Inner = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "650000.000");
		startSegment2Inner.setattrvalue("C2", "200000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "650000.000");
		endSegment2Inner.setattrvalue("C2", "250000.000");
		// polyline 3
		IomObject polylineValue5 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments5=polylineValue5.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment5=segments5.addattrobj("segment", "COORD");
		startSegment5.setattrvalue("C1", "650000.000");
		startSegment5.setattrvalue("C2", "250000.000");
		IomObject endSegment5=segments5.addattrobj("segment", "COORD");
		endSegment5.setattrvalue("C1", "600000.000");
		endSegment5.setattrvalue("C2", "200000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objAreaSuccess));
		validator.validate(new ObjectEvent(objAreaSuccess2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft, ob 2 Polygone erstellt werden koennen, wenn 2 Polygone sich auf einer Linie an
	// drei Punkten beruehren. Beide Punkte liegen aufeinander und haben beide dieselben Segmentpunkte.
	@Test
	public void twoPolygon_LineOverlayOnLineOfOtherArea_OnSame3Points_Ok(){
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		{
			IomObject multisurfaceValue=objAreaSuccess.addattrobj("area2d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			// outer boundary
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline 1
			{
				IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "100000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "540000.000");
				endSegment.setattrvalue("C2", "100000.000");
			}
			// polyline 2
			{
				IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "540000.000");
				startSegment.setattrvalue("C2", "100000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "540000.000");
				endSegment.setattrvalue("C2", "150000.000");
			}
			// polyline 3
			{
				IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "540000.000");
				startSegment.setattrvalue("C2", "150000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "540000.000");
				endSegment.setattrvalue("C2", "200000.000");
			}
			// polyline 4
			{
				IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "540000.000");
				startSegment.setattrvalue("C2", "200000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "100000.000");
			}
		}
		Iom_jObject objArea2Success=new Iom_jObject(ILI_CLASSD, OID2);
		{
			IomObject multisurfaceValue=objArea2Success.addattrobj("area2d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			// outer boundary
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			
			// polyline 1
			{
				IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "580000.000");
				startSegment.setattrvalue("C2", "100000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "540000.000");
				endSegment.setattrvalue("C2", "100000.000");
			}
			// polyline 2
			{
				IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "540000.000");
				startSegment.setattrvalue("C2", "100000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "540000.000");
				endSegment.setattrvalue("C2", "150000.000");
			}
			// polyline 3
			{
				IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "540000.000");
				startSegment.setattrvalue("C2", "150000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "540000.000");
				endSegment.setattrvalue("C2", "200000.000");
			}
			// polyline 4
			{
				IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "540000.000");
				startSegment.setattrvalue("C2", "200000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "580000.000");
				endSegment.setattrvalue("C2", "100000.000");
			}
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objAreaSuccess));
		validator.validate(new ObjectEvent(objArea2Success));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn 2 Polygone sich auf einer Linie an
	// zwei Punkten beruehren. Beide Punkte liegen aufeinander.
	// Die zweite Polygon hat auf der Selben Linie, einen Segmentpunkt mehr.
	@Test
	public void twoPolygon_LineOverlayOnLineOfOtherArea_OnDifferentPoints_Fail(){
		Iom_jObject objArea1=new Iom_jObject(ILI_CLASSD, OID1);
			{
			IomObject multisurfaceValue=objArea1.addattrobj("area2d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			// outer boundary
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			{
				// polyline
				IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
				{
					IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
					IomObject startSegment=segments.addattrobj("segment", "COORD");
					startSegment.setattrvalue("C1", "500000.000");
					startSegment.setattrvalue("C2", "100000.000");
					IomObject endSegment=segments.addattrobj("segment", "COORD");
					endSegment.setattrvalue("C1", "540000.000");
					endSegment.setattrvalue("C2", "100000.000");
				}
				// polyline 2
				IomObject polyline2Value = outerBoundary.addattrobj("polyline", "POLYLINE");
				{
					IomObject segments=polyline2Value.addattrobj("sequence", "SEGMENTS");
					IomObject startSegment=segments.addattrobj("segment", "COORD");
					startSegment.setattrvalue("C1", "540000.000");
					startSegment.setattrvalue("C2", "100000.000");
					IomObject endSegment=segments.addattrobj("segment", "COORD");
					endSegment.setattrvalue("C1", "540000.000");
					endSegment.setattrvalue("C2", "150000.000");
				}
				// polyline 3
				IomObject polyline3Value = outerBoundary.addattrobj("polyline", "POLYLINE");
				{
					IomObject segments=polyline3Value.addattrobj("sequence", "SEGMENTS");
					IomObject startSegment=segments.addattrobj("segment", "COORD");
					startSegment.setattrvalue("C1", "540000.000");
					startSegment.setattrvalue("C2", "150000.000");
					IomObject endSegment=segments.addattrobj("segment", "COORD");
					endSegment.setattrvalue("C1", "540000.000");
					endSegment.setattrvalue("C2", "200000.000");
				}
				// polyline 4
				IomObject polyline4Value = outerBoundary.addattrobj("polyline", "POLYLINE");
				{
					IomObject segments=polyline4Value.addattrobj("sequence", "SEGMENTS");
					IomObject startSegment=segments.addattrobj("segment", "COORD");
					startSegment.setattrvalue("C1", "540000.000");
					startSegment.setattrvalue("C2", "200000.000");
					IomObject endSegment=segments.addattrobj("segment", "COORD");
					endSegment.setattrvalue("C1", "500000.000");
					endSegment.setattrvalue("C2", "100000.000");
				}
			}
		}
		Iom_jObject objArea2=new Iom_jObject(ILI_CLASSD, OID2);
			{
			IomObject multisurfaceValue=objArea2.addattrobj("area2d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			// outer boundary
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			{
				// polyline
				IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
				{
					IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
					IomObject startSegment=segments.addattrobj("segment", "COORD");
					startSegment.setattrvalue("C1", "580000.000");
					startSegment.setattrvalue("C2", "100000.000");
					IomObject endSegment=segments.addattrobj("segment", "COORD");
					endSegment.setattrvalue("C1", "540000.000");
					endSegment.setattrvalue("C2", "100000.000");
				}
				// polyline 2
				IomObject polyline2Value = outerBoundary.addattrobj("polyline", "POLYLINE");
				{
					IomObject segments=polyline2Value.addattrobj("sequence", "SEGMENTS");
					IomObject startSegment=segments.addattrobj("segment", "COORD");
					startSegment.setattrvalue("C1", "540000.000");
					startSegment.setattrvalue("C2", "100000.000");
					IomObject endSegment=segments.addattrobj("segment", "COORD");
					endSegment.setattrvalue("C1", "540000.000");
					endSegment.setattrvalue("C2", "150000.000");
				}
				// polyline 3
				IomObject polyline3Value = outerBoundary.addattrobj("polyline", "POLYLINE");
				{
					IomObject segments=polyline3Value.addattrobj("sequence", "SEGMENTS");
					IomObject startSegment=segments.addattrobj("segment", "COORD");
					startSegment.setattrvalue("C1", "540000.000");
					startSegment.setattrvalue("C2", "150000.000");
					IomObject endSegment=segments.addattrobj("segment", "COORD");
					endSegment.setattrvalue("C1", "540000.000");
					endSegment.setattrvalue("C2", "160000.000");
				}
				// polyline 4
				IomObject polyline4Value = outerBoundary.addattrobj("polyline", "POLYLINE");
				{
					IomObject segments=polyline4Value.addattrobj("sequence", "SEGMENTS");
					IomObject startSegment=segments.addattrobj("segment", "COORD");
					startSegment.setattrvalue("C1", "540000.000");
					startSegment.setattrvalue("C2", "160000.000");
					IomObject endSegment=segments.addattrobj("segment", "COORD");
					endSegment.setattrvalue("C1", "540000.000");
					endSegment.setattrvalue("C2", "200000.000");
				}
				// polyline 5
				IomObject polyline5Value = outerBoundary.addattrobj("polyline", "POLYLINE");
				{
					IomObject segments=polyline5Value.addattrobj("sequence", "SEGMENTS");
					IomObject startSegment=segments.addattrobj("segment", "COORD");
					startSegment.setattrvalue("C1", "540000.000");
					startSegment.setattrvalue("C2", "200000.000");
					IomObject endSegment=segments.addattrobj("segment", "COORD");
					endSegment.setattrvalue("C1", "580000.000");
					endSegment.setattrvalue("C2", "100000.000");
				}
			}
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objArea1));
		validator.validate(new ObjectEvent(objArea2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==3);
		assertEquals("Overlay coord1 (540000.000, 150000.000), coord2 (540000.000, 160000.000), tids o1, o2", logger.getErrs().get(0).getEventMsg());
		assertEquals("Overlay coord1 (540000.000, 160000.000), coord2 (540000.000, 200000.000), tids o1, o2", logger.getErrs().get(1).getEventMsg());
		assertEquals("failed to validate AREA Datatypes23.Topic.ClassD.area2d", logger.getErrs().get(2).getEventMsg());
	}
	
	// prueft, ob 2 Polygone (mit je einem Arc) die genau uebereinanderliegen, als Fehler gemeldet wird
	@Test
	public void twoPolygon_WithArcsLieExactlyOnEachOther_Fail(){
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		{
			IomObject multisurfaceValue=objAreaSuccess.addattrobj("area3d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "100000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "100000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue2.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "100000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "200000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue3.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "200000.000");
				startSegment.setattrvalue("C3", "5000.000");
				// Arc
				IomObject arcSegment=segments.addattrobj("segment", "ARC");
				arcSegment.setattrvalue("A1", "540000.000");
				arcSegment.setattrvalue("A2", "160000.000");
				arcSegment.setattrvalue("C1", "500000.000");
				arcSegment.setattrvalue("C2", "100000.000");
				arcSegment.setattrvalue("C3", "5000.000");
			}
		}
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSD, OID2);
		{
			IomObject multisurfaceValue=objAreaSuccess2.addattrobj("area3d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "100000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "100000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue2.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "100000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "200000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue3.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "200000.000");
				startSegment.setattrvalue("C3", "5000.000");
				// Arc
				IomObject arcSegment=segments.addattrobj("segment", "ARC");
				arcSegment.setattrvalue("A1", "540000.000");
				arcSegment.setattrvalue("A2", "160000.000");
				arcSegment.setattrvalue("C1", "500000.000");
				arcSegment.setattrvalue("C2", "100000.000");
				arcSegment.setattrvalue("C3", "5000.000");
			}
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		//settings.setValue(Validator.CONFIG_DEBUG_XTFOUT,"src/test/data/validator/twoPolygon_WithArcsLieExactlyOnEachOther_Fail.xtf");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objAreaSuccess));
		validator.validate(new ObjectEvent(objAreaSuccess2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		assertTrue(logger.getErrs().size()==2);
		assertEquals("polygons overlay tid1 o1, tid2 o2", logger.getErrs().get(0).getEventMsg());
		assertEquals("failed to validate AREA Datatypes23.Topic.ClassD.area3d", logger.getErrs().get(1).getEventMsg());
	}
	
	// prueft, ob 2 Polygone erstellt werden koennen, wenn 2 Polygone mit je einem Arc,
	// welche uebereinander liegen und sich nicht an den gleichen Punkten beruehren, erstellt werden koennen.
	@Test
	public void twoPolygon_2ArcsLieNotExactlyOnEachOther_1ArcHas1PointMore_Fail(){
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		{
			IomObject multisurfaceValue=objAreaSuccess.addattrobj("area3d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "499996.000");
				startSegment.setattrvalue("C2", "100000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "100005.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue2.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "100005.000");
				startSegment.setattrvalue("C3", "5000.000");
				// Arc
				IomObject arcSegment=segments.addattrobj("segment", "ARC");
				arcSegment.setattrvalue("A1", "500004.000");
				arcSegment.setattrvalue("A2", "100003.000");
				arcSegment.setattrvalue("C1", "500000.000");
				arcSegment.setattrvalue("C2", "99995.000");
				arcSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue3.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "99995.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "499996.000");
				endSegment.setattrvalue("C2", "100000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
		}
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSD, OID2);
		{
			IomObject multisurfaceValue=objAreaSuccess2.addattrobj("area3d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "499996.000");
				startSegment.setattrvalue("C2", "100000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "100005.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue2.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "100005.000");
				startSegment.setattrvalue("C3", "5000.000");
				// Arc
				IomObject arcSegment=segments.addattrobj("segment", "ARC");
				arcSegment.setattrvalue("A1", "500004.000");
				arcSegment.setattrvalue("A2", "99997.000");
				arcSegment.setattrvalue("C1", "500000.000");
				arcSegment.setattrvalue("C2", "99995.000");
				arcSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue3.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "99995.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "499996.000");
				endSegment.setattrvalue("C2", "100000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        //settings.setValue(Validator.CONFIG_DEBUG_XTFOUT,"src/test/data/validator/twoPolygon_2ArcsLieNotExactlyOnEachOther_1ArcHas1PointMore_Fail.xtf");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objAreaSuccess));
		validator.validate(new ObjectEvent(objAreaSuccess2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		assertTrue(logger.getErrs().size()==2);
		assertEquals("polygons overlay tid1 o1, tid2 o2", logger.getErrs().get(0).getEventMsg());
		assertEquals("failed to validate AREA Datatypes23.Topic.ClassD.area3d", logger.getErrs().get(1).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird,
	// wenn auf einem "geraden Segment" der OuterBoundary, ein "gerades Segment" der InnerBoundary liegt.
	// Beide Segmente teilen die selben Anfangs und Endpunkte.
	@Test
	public void onePolygon_2Boundaries_LineOfInnerOverlayLineOfOuterBoundary_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "500000.000");
		startSegment.setattrvalue("C2", "100000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "600000.000");
		endSegment.setattrvalue("C2", "100000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "600000.000");
		startSegment2.setattrvalue("C2", "100000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "600000.000");
		endSegment2.setattrvalue("C2", "200000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "600000.000");
		startSegment3.setattrvalue("C2", "200000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "500000.000");
		endSegment3.setattrvalue("C2", "200000.000");
		// polyline 4
		IomObject polylineValue4 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments4=polylineValue4.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment4=segments4.addattrobj("segment", "COORD");
		startSegment4.setattrvalue("C1", "500000.000");
		startSegment4.setattrvalue("C2", "200000.000");
		IomObject endSegment4=segments4.addattrobj("segment", "COORD");
		endSegment4.setattrvalue("C1", "500000.000");
		endSegment4.setattrvalue("C2", "100000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "500000.000");
		startSegmentInner.setattrvalue("C2", "100000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "600000.000");
		endSegmentInner.setattrvalue("C2", "100000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "600000.000");
		startSegment2Inner.setattrvalue("C2", "100000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "550000.000");
		endSegment2Inner.setattrvalue("C2", "150000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "550000.000");
		startSegment3Inner.setattrvalue("C2", "150000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "500000.000");
		endSegment3Inner.setattrvalue("C2", "100000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("Overlay coord1 (500000.000, 100000.000), coord2 (600000.000, 100000.000), tids o1, o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird,
	// wenn auf einem "geraden Segment" der OuterBoundary, ein "gerades Segment" der InnerBoundary liegt.
	// Beide Segmente teilen die selben Anfangs und Endpunkte.
	// Dazu wird die Innerboundary nicht geschlossen. Es soll nur die overlay Meldung ausgegeben werden.
	@Test
	public void onePolygon_2Boundaries_LineOfInnerOverlayLineOfOuterBoundary_IgnoreInvalidRinglines_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "500000.000");
		startSegment.setattrvalue("C2", "100000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "600000.000");
		endSegment.setattrvalue("C2", "100000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "600000.000");
		startSegment2.setattrvalue("C2", "100000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "600000.000");
		endSegment2.setattrvalue("C2", "200000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "600000.000");
		startSegment3.setattrvalue("C2", "200000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "500000.000");
		endSegment3.setattrvalue("C2", "200000.000");
		// polyline 4
		IomObject polylineValue4 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments4=polylineValue4.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment4=segments4.addattrobj("segment", "COORD");
		startSegment4.setattrvalue("C1", "500000.000");
		startSegment4.setattrvalue("C2", "200000.000");
		IomObject endSegment4=segments4.addattrobj("segment", "COORD");
		endSegment4.setattrvalue("C1", "500000.000");
		endSegment4.setattrvalue("C2", "100000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "520000.000");
		startSegmentInner.setattrvalue("C2", "150000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "500000.000");
		endSegmentInner.setattrvalue("C2", "100000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "500000.000");
		startSegment2Inner.setattrvalue("C2", "100000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "600000.000");
		endSegment2Inner.setattrvalue("C2", "100000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "600000.000");
		startSegment3Inner.setattrvalue("C2", "100000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "580000.000");
		endSegment3Inner.setattrvalue("C2", "150000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("Overlay coord1 (500000.000, 100000.000), coord2 (600000.000, 100000.000), tids o1, o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird,
	// wenn auf einem "geraden Segment" der OuterBoundary, ein "gerades Segment" der InnerBoundary liegt.
	// Beide Segmente teilen die selben Anfangs und Endpunkte.
	// Dazu wird die Innerboundary nicht geschlossen.
	@Test
	public void onePolygon_2Boundaries_LineOfInnerOverlayLineOfOuterBoundary_InnerboundaryNotClosed_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "500000.000");
		startSegment.setattrvalue("C2", "100000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "600000.000");
		endSegment.setattrvalue("C2", "100000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "600000.000");
		startSegment2.setattrvalue("C2", "100000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "600000.000");
		endSegment2.setattrvalue("C2", "200000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "600000.000");
		startSegment3.setattrvalue("C2", "200000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "500000.000");
		endSegment3.setattrvalue("C2", "200000.000");
		// polyline 4
		IomObject polylineValue4 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments4=polylineValue4.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment4=segments4.addattrobj("segment", "COORD");
		startSegment4.setattrvalue("C1", "500000.000");
		startSegment4.setattrvalue("C2", "200000.000");
		IomObject endSegment4=segments4.addattrobj("segment", "COORD");
		endSegment4.setattrvalue("C1", "500000.000");
		endSegment4.setattrvalue("C2", "100000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		{
		// polyline 1
			IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
			IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
			startSegmentInner.setattrvalue("C1", "500000.000");
			startSegmentInner.setattrvalue("C2", "200000.000");
			IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
			endSegmentInner.setattrvalue("C1", "500000.000");
			endSegmentInner.setattrvalue("C2", "100000.000");
		}
		// polyline 2
		{
			IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
			IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
			startSegmentInner.setattrvalue("C1", "500000.000");
			startSegmentInner.setattrvalue("C2", "100000.000");
			IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
			endSegmentInner.setattrvalue("C1", "550000.000");
			endSegmentInner.setattrvalue("C2", "150000.000");
		}
		// polyline 3
		{
			IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
			IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
			startSegmentInner.setattrvalue("C1", "550000.000");
			startSegmentInner.setattrvalue("C2", "150000.000");
			IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
			endSegmentInner.setattrvalue("C1", "520000.000");
			endSegmentInner.setattrvalue("C2", "180000.000");
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("Overlay coord1 (500000.000, 100000.000), coord2 (500000.000, 200000.000), tids o1, o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird,
	// wenn auf einem "geraden Segment" der OuterBoundary, ein "gerades Segment" der InnerBoundary liegt.
	// Beide Segmente teilen die selben Anfangspunkte, jedoch nicht die Selben Endpunkte.
	// Dazu liegt ein Punkt der Innerboundary-Linie, auf einer Linie der Outerboundary.
	@Test
	public void onePolygon_2Boundaries_LineOfInnerOverlayLineOfOuterBoundary_PointOnXLine_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline 1
		{
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "500000.000");
			startSegment.setattrvalue("C2", "100000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "600000.000");
			endSegment.setattrvalue("C2", "100000.000");
		}
		// polyline 2
		{
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "600000.000");
			startSegment.setattrvalue("C2", "100000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "600000.000");
			endSegment.setattrvalue("C2", "200000.000");
		}
		// polyline 3
		{
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "600000.000");
			startSegment.setattrvalue("C2", "200000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "500000.000");
			endSegment.setattrvalue("C2", "200000.000");
		}
		// polyline 4
		{
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "500000.000");
			startSegment.setattrvalue("C2", "200000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "500000.000");
			endSegment.setattrvalue("C2", "100000.000");
		}
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline 1
		{
			IomObject polylineValue = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "500000.000");
			startSegment.setattrvalue("C2", "200000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "550000.000");
			endSegment.setattrvalue("C2", "150000.000");
		}
		// polyline 2
		{
			IomObject polylineValue = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "550000.000");
			startSegment.setattrvalue("C2", "150000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "550000.000");
			endSegment.setattrvalue("C2", "200000.000");
		}
		// polyline 3
		{
			IomObject polylineValue = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "550000.000");
			startSegment.setattrvalue("C2", "200000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "500000.000");
			endSegment.setattrvalue("C2", "200000.000");
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(2,logger.getErrs().size());
		assertEquals("Intersection coord1 (550000.000, 200000.000), tids o1, o1", logger.getErrs().get(0).getEventMsg());
		assertEquals("Overlay coord1 (500000.000, 200000.000), coord2 (550000.000, 200000.000), tids o1, o1", logger.getErrs().get(1).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird,
	// wenn auf zwei "geraden Segmenten" der OuterBoundary, 2 "gerade Segmente" der InnerBoundary liegen.
	// die Segmente der ersten overlay, teilen sich dieselben Anfangs und Endpunkte.
	// die Segmente der zweiten overlay, teilen sich dieselben Anfangspunkte, jedoch unterschiedliche Endpunkte.
	// Dazu liegt ein Punkt der Innerboundary-Linie, auf einer Linie der Outerboundary.
	@Test
	public void onePolygon_2Boundaries_2LinesOfInnerOverlayOuterboundaryLines_PointOnLine_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline 1
		{
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "500000.000");
			startSegment.setattrvalue("C2", "100000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "600000.000");
			endSegment.setattrvalue("C2", "100000.000");
		}
		// polyline 2
		{
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "600000.000");
			startSegment.setattrvalue("C2", "100000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "600000.000");
			endSegment.setattrvalue("C2", "200000.000");
		}
		// polyline 3
		{
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "600000.000");
			startSegment.setattrvalue("C2", "200000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "500000.000");
			endSegment.setattrvalue("C2", "200000.000");
		}
		// polyline 4
		{
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "500000.000");
			startSegment.setattrvalue("C2", "200000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "500000.000");
			endSegment.setattrvalue("C2", "100000.000");
		}
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline 1
		{
			IomObject polylineValue = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "500000.000");
			startSegment.setattrvalue("C2", "100000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "550000.000");
			endSegment.setattrvalue("C2", "150000.000");
		}
		// polyline 2
		{
			IomObject polylineValue = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "550000.000");
			startSegment.setattrvalue("C2", "150000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "550000.000");
			endSegment.setattrvalue("C2", "200000.000");
		}
		// polyline 3
		{
			IomObject polylineValue = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "550000.000");
			startSegment.setattrvalue("C2", "200000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "500000.000");
			endSegment.setattrvalue("C2", "200000.000");
		}
		// polyline 4
		{
			IomObject polylineValue = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "500000.000");
			startSegment.setattrvalue("C2", "200000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "500000.000");
			endSegment.setattrvalue("C2", "100000.000");
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(3,logger.getErrs().size());
		assertEquals("Intersection coord1 (550000.000, 200000.000), tids o1, o1", logger.getErrs().get(0).getEventMsg());
		assertEquals("Overlay coord1 (500000.000, 200000.000), coord2 (550000.000, 200000.000), tids o1, o1", logger.getErrs().get(1).getEventMsg());
		assertEquals("Overlay coord1 (500000.000, 100000.000), coord2 (500000.000, 200000.000), tids o1, o1", logger.getErrs().get(2).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn Dangles erstellt wurden.
	@Test
	public void onePolygon_1Boundary_Dangles_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "500000.000");
		startSegment.setattrvalue("C2", "100000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "600000.000");
		endSegment.setattrvalue("C2", "100000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "600000.000");
		startSegment2.setattrvalue("C2", "100000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "600000.000");
		endSegment2.setattrvalue("C2", "200000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "600000.000");
		startSegment3.setattrvalue("C2", "200000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "500000.000");
		endSegment3.setattrvalue("C2", "200000.000");
		// polyline 4
		IomObject polylineValue4 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments4=polylineValue4.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment4=segments4.addattrobj("segment", "COORD");
		startSegment4.setattrvalue("C1", "500000.000");
		startSegment4.setattrvalue("C2", "200000.000");
		IomObject endSegment4=segments4.addattrobj("segment", "COORD");
		endSegment4.setattrvalue("C1", "500000.000");
		endSegment4.setattrvalue("C2", "100000.000");
		// polyline 5
		IomObject polylineValue5 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments5=polylineValue5.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment5=segments5.addattrobj("segment", "COORD");
		startSegment5.setattrvalue("C1", "500000.000");
		startSegment5.setattrvalue("C2", "100000.000");
		IomObject endSegment5=segments5.addattrobj("segment", "COORD");
		endSegment5.setattrvalue("C1", "520000.000");
		endSegment5.setattrvalue("C2", "150000.000");
		// polyline 6
		IomObject polylineValue6 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments6=polylineValue6.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment6=segments6.addattrobj("segment", "COORD");
		startSegment6.setattrvalue("C1", "520000.000");
		startSegment6.setattrvalue("C2", "150000.000");
		IomObject endSegment6=segments5.addattrobj("segment", "COORD");
		endSegment6.setattrvalue("C1", "530000.000");
		endSegment6.setattrvalue("C2", "160000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("dangle tid o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird,
	// wenn sich 2 gerade Linien einer innerboundary, mit 2 geraden Linien einer anderen innerboundary ueberschneiden.
	@Test
	public void onePolygon_2Boundaries_TwoInnerboundaryLinesCutEachOther_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "700000.000");
		endSegment.setattrvalue("C2", "70000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "700000.000");
		startSegment2.setattrvalue("C2", "70000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "700000.000");
		endSegment2.setattrvalue("C2", "250000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "700000.000");
		startSegment3.setattrvalue("C2", "250000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "250000.000");
		// polyline 4
		IomObject polylineValue4 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments4=polylineValue4.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment4=segments4.addattrobj("segment", "COORD");
		startSegment4.setattrvalue("C1", "480000.000");
		startSegment4.setattrvalue("C2", "250000.000");
		IomObject endSegment4=segments4.addattrobj("segment", "COORD");
		endSegment4.setattrvalue("C1", "480000.000");
		endSegment4.setattrvalue("C2", "70000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "560000.000");
		startSegmentInner.setattrvalue("C2", "170000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "680000.000");
		endSegmentInner.setattrvalue("C2", "90000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "680000.000");
		startSegment2Inner.setattrvalue("C2", "90000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "680000.000");
		endSegment2Inner.setattrvalue("C2", "230000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "680000.000");
		startSegment3Inner.setattrvalue("C2", "230000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "560000.000");
		endSegment3Inner.setattrvalue("C2", "170000.000");
		// 2. inner boundary
		IomObject innerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner2=polylineValueInner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		startSegmentInner2.setattrvalue("C1", "500000.000");
		startSegmentInner2.setattrvalue("C2", "90000.000");
		IomObject endSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		endSegmentInner2.setattrvalue("C1", "600000.000");
		endSegmentInner2.setattrvalue("C2", "170000.000");
		// polyline 2
		IomObject polylineValue2Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner2=polylineValue2Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		startSegment2Inner2.setattrvalue("C1", "600000.000");
		startSegment2Inner2.setattrvalue("C2", "170000.000");
		IomObject endSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		endSegment2Inner2.setattrvalue("C1", "500000.000");
		endSegment2Inner2.setattrvalue("C2", "230000.000");
		// polyline 3
		IomObject polylineValue3Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner2=polylineValue3Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		startSegment3Inner2.setattrvalue("C1", "500000.000");
		startSegment3Inner2.setattrvalue("C2", "230000.000");
		IomObject endSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		endSegment3Inner2.setattrvalue("C1", "500000.000");
		endSegment3Inner2.setattrvalue("C2", "90000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(2,logger.getErrs().size());
		assertEquals("Intersection coord1 (581818.182, 155454.545), tids o1, o1", logger.getErrs().get(0).getEventMsg());
		assertEquals("Intersection coord1 (581818.182, 180909.091), tids o1, o1", logger.getErrs().get(1).getEventMsg());
	}	
	
	// prueft, ob eine Fehlermeldung ausgegeben wird,
	// wenn sich die Innerboundary mit der Outerboundary ueberschneidet.
	@Test
	public void onePolygon_2Boundaries_InnerIntersectOuterboundary_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "480000.000");
		startSegmentInner.setattrvalue("C2", "70000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "485000.000");
		endSegmentInner.setattrvalue("C2", "71000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "485000.000");
		startSegment2Inner.setattrvalue("C2", "71000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "505000.000");
		endSegment2Inner.setattrvalue("C2", "78000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "505000.000");
		startSegment3Inner.setattrvalue("C2", "78000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "480000.000");
		endSegment3Inner.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("Intersection coord1 (491666.667, 73333.333), tids o1, o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn diese aus 3d Koordinaten besteht,
	// obwohl im Modell nur 2d Koordinaten definiert wurden.
	@Test
	public void onePolygon_1Boundary_2dWith3dImplementation_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "1000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Wrong COORD structure, C3 not expected", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn diese aus 2d Koordinaten besteht,
	// obwohl im Model nur 3d Koordinaten definiert wurden.
	@Test
	public void onePolygon_1Boundary_3dWith2dImplementation_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "1000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		endSegment.setattrvalue("C3", "1500.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		startSegment2.setattrvalue("C3", "1500.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		endSegment2.setattrvalue("C3", "2000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		startSegment3.setattrvalue("C3", "2000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Wrong COORD structure, C3 expected", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn die Value eines Segments nicht gueltig ist.
	@Test
	public void onePolygon_1Boundary_3dInvalidValueRange_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "4800000.000");
		startSegment.setattrvalue("C2", "700000.000");
		startSegment.setattrvalue("C3", "10000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		endSegment.setattrvalue("C3", "1500.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		startSegment2.setattrvalue("C3", "1500.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		endSegment2.setattrvalue("C3", "2000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		startSegment3.setattrvalue("C3", "2000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "4800000.000");
		endSegment3.setattrvalue("C2", "700000.000");
		endSegment3.setattrvalue("C3", "10000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==6);
		assertEquals("value 4800000.000 is out of range in attribute area3d", logger.getErrs().get(0).getEventMsg());
		assertEquals("value 700000.000 is out of range in attribute area3d", logger.getErrs().get(1).getEventMsg());
		assertEquals("value 10000.000 is out of range in attribute area3d", logger.getErrs().get(2).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn die Polygon mit einem ungueltigen Kreisbogen erstellt wird.
	@Test
	public void onePolygon_1Boundary_3dWithARCInvalidValueRange_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "1000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		endSegment.setattrvalue("C3", "1500.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		startSegment2.setattrvalue("C3", "1500.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		endSegment2.setattrvalue("C3", "2000.000");
		// arc 1
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		startSegment3.setattrvalue("C3", "2000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "ARC");
		endSegment3.setattrvalue("A1", "4800000.000");
		endSegment3.setattrvalue("A2", "700000.000");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		endSegment3.setattrvalue("C3", "1000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("value 4800000.000 is out of range in attribute area3d", logger.getErrs().get(0).getEventMsg());
		assertEquals("value 700000.000 is out of range in attribute area3d", logger.getErrs().get(1).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn der Type area nicht vom Type Multisurface ist.
	@Test
	public void onePolygon_1Boundary_InvalidAreaType_Fail(){
		Iom_jObject objAreaMultisurface=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject areaValue=objAreaMultisurface.addattrobj("area2d", "AREA");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objAreaMultisurface));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("unexpected Type AREA; MULTISURFACE expected", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn die Polygon Komplett ist, jedoch aus 2 Polygonen besteht.
	@Test
	public void onePolygon_1Boundary_CompleteWith2Areas_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		multisurfaceValue.setobjectconsistency(IomConstants.IOM_COMPLETE);
		IomObject areaValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = areaValue.addattrobj("boundary", "BOUNDARY");
		IomObject areaValue2 = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = areaValue2.addattrobj("boundary", "BOUNDARY");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid number of surfaces in COMPLETE basket", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn 2 Coords nacheinander die selbe Position haben.
	@Test
	public void onePolygon_1Boundary_RepeatedCoordCoordinates_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		endSegment=segments.addattrobj("segment", "COORD"); // illegal; repeated COORD
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==3);
		assertEquals("duplicate coord at (500000.0, 80000.0, NaN)", logger.getErrs().get(0).getEventMsg());
		assertEquals("duplicate coord at (500000.0, 80000.0, NaN)", logger.getErrs().get(1).getEventMsg());
		assertEquals("duplicate coord at (500000.0, 80000.0, NaN)", logger.getErrs().get(2).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn die Polygone sich an unterschiedlichen Stellen beruehren.
	@Test
	public void twoPolygon_IntersectEachOther_On2Points_Fail(){
		// polygon1
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "500000.000");
		startSegment.setattrvalue("C2", "100000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "540000.000");
		endSegment.setattrvalue("C2", "100000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "540000.000");
		startSegment2.setattrvalue("C2", "100000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "540000.000");
		endSegment2.setattrvalue("C2", "150000.000");
		// polyline 3
		IomObject polylineValue4 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments4=polylineValue4.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment4=segments4.addattrobj("segment", "COORD");
		startSegment4.setattrvalue("C1", "540000.000");
		startSegment4.setattrvalue("C2", "150000.000");
		IomObject endSegment4=segments4.addattrobj("segment", "COORD");
		endSegment4.setattrvalue("C1", "540000.000");
		endSegment4.setattrvalue("C2", "200000.000");
		// polyline 4
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "540000.000");
		startSegment3.setattrvalue("C2", "200000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "500000.000");
		endSegment3.setattrvalue("C2", "100000.000");
		// polygon2
		Iom_jObject objSurfaceSuccess2=new Iom_jObject(ILI_CLASSD, OID2);
		IomObject multisurfaceValue2=objSurfaceSuccess2.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "580000.000");
		startSegmentInner.setattrvalue("C2", "100000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "540000.000");
		endSegmentInner.setattrvalue("C2", "100000.000");
		// polyline 2
		IomObject polylineValue2Inner = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "540000.000");
		startSegment2Inner.setattrvalue("C2", "100000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "540000.000");
		endSegment2Inner.setattrvalue("C2", "200000.000");
		// polyline 4
		IomObject polylineValue3Inner = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "540000.000");
		startSegment3Inner.setattrvalue("C2", "200000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "580000.000");
		endSegment3Inner.setattrvalue("C2", "100000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new ObjectEvent(objSurfaceSuccess2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==3);
		assertEquals("Overlay coord1 (540000.000, 100000.000), coord2 (540000.000, 150000.000), tids o1, o2", logger.getErrs().get(0).getEventMsg());
		assertEquals("Overlay coord1 (540000.000, 150000.000), coord2 (540000.000, 200000.000), tids o1, o2", logger.getErrs().get(1).getEventMsg());
		assertEquals("failed to validate AREA Datatypes23.Topic.ClassD.area2d", logger.getErrs().get(2).getEventMsg());
	}
	
	// pruefe ob eine Fehlermeldung ausgegeben wird, wenn die outerboundary nicht existiert.
	@Test
	public void onePolygon_MissingOuterBoundary_False(){
		// external object
		Iom_jObject externalObject=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject externalMultisurface=externalObject.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = externalMultisurface.addattrobj("surface", "SURFACE");
		{
			// outer boundary of external object
			//IomObject outerBoundaryExternalObj = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline 1
			IomObject polyline1 = surfaceValue.addattrobj("polyline", "POLYLINE");
			IomObject segments=polyline1.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "500000.000");
			startSegment.setattrvalue("C2", "100000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "600000.000");
			endSegment.setattrvalue("C2", "100000.000");
			// polyline 2
			IomObject polyline2 = surfaceValue.addattrobj("polyline", "POLYLINE");
			IomObject segments2=polyline2.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2=segments2.addattrobj("segment", "COORD");
			startSegment2.setattrvalue("C1", "600000.000");
			startSegment2.setattrvalue("C2", "100000.000");
			IomObject endSegment2=segments2.addattrobj("segment", "COORD");
			endSegment2.setattrvalue("C1", "600000.000");
			endSegment2.setattrvalue("C2", "200000.000");
			// polyline 3
			IomObject polyline3 = surfaceValue.addattrobj("polyline", "POLYLINE");
			IomObject segments3=polyline3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3=segments3.addattrobj("segment", "COORD");
			startSegment3.setattrvalue("C1", "600000.000");
			startSegment3.setattrvalue("C2", "200000.000");
			IomObject endSegment3=segments3.addattrobj("segment", "COORD");
			endSegment3.setattrvalue("C1", "500000.000");
			endSegment3.setattrvalue("C2", "200000.000");
			// polyline 4
			IomObject polyline4 = surfaceValue.addattrobj("polyline", "POLYLINE");
			IomObject segments4=polyline4.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment4=segments4.addattrobj("segment", "COORD");
			startSegment4.setattrvalue("C1", "500000.000");
			startSegment4.setattrvalue("C2", "200000.000");
			IomObject endSegment4=segments4.addattrobj("segment", "COORD");
			endSegment4.setattrvalue("C1", "500000.000");
			endSegment4.setattrvalue("C2", "100000.000");
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(externalObject));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getWarn().size()==0);
		assertTrue(logger.getErrs().size()==1);
		assertEquals("missing outerboundary in area2d of object o1.", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn
	// eine Linie der Innerboundary auf einer Linie der Outerboundary liegt.
	// Das Segment der Innerboundary Linie, welches die Outerboundary Linie ueberschneidet,
	// unterscheidet sich zum Outerboundary Segment, nur ueber die Y Koordinate.
	@Test
	public void onePolygon_2Boundaries_OverlayOf2Lines_InnerLineYCoordToLow_Fail(){
		//EhiLogger.getInstance().setTraceFilter(false);
		Iom_jObject geometricFigure=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurface=geometricFigure.addattrobj("area2d", "MULTISURFACE");
		IomObject surface = multisurface.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surface.addattrobj("boundary", "BOUNDARY");
		// polyline: left(bottom to top)
		{
			IomObject left_BottomToTop = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=left_BottomToTop.addattrobj("sequence", "SEGMENTS");
			IomObject segBottom=segments.addattrobj("segment", "COORD");
			segBottom.setattrvalue("C1", "500000.000");
			segBottom.setattrvalue("C2", "100000.000");
			IomObject segTop=segments.addattrobj("segment", "COORD");
			segTop.setattrvalue("C1", "500000.000");
			segTop.setattrvalue("C2", "300000.000");
		}
		// polyline: right(topLeft to centerRight)
		{
			IomObject right_LeftTopToCenterRight = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=right_LeftTopToCenterRight.addattrobj("sequence", "SEGMENTS");
			IomObject segTopLeft=segments.addattrobj("segment", "COORD");
			segTopLeft.setattrvalue("C1", "500000.000");
			segTopLeft.setattrvalue("C2", "300000.000");
			IomObject segCenterRight=segments.addattrobj("segment", "COORD");
			segCenterRight.setattrvalue("C1", "700000.000");
			segCenterRight.setattrvalue("C2", "200000.000");
		}
		// polyline: left(centerRight to leftBottom)
		{
			IomObject right_CenterToLeftBottom = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=right_CenterToLeftBottom.addattrobj("sequence", "SEGMENTS");
			IomObject segCenterRight=segments.addattrobj("segment", "COORD");
			segCenterRight.setattrvalue("C1", "700000.000");
			segCenterRight.setattrvalue("C2", "200000.000");
			IomObject segLeftBottom=segments.addattrobj("segment", "COORD");
			segLeftBottom.setattrvalue("C1", "500000.000");
			segLeftBottom.setattrvalue("C2", "100000.000");
		}
		// inner boundary
		IomObject innerBoundary = surface.addattrobj("boundary", "BOUNDARY");
		// inner boundary polyline: left(bottom to top)
		{
			IomObject inner_Left_BottomToTop = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Left_BottomToTop.addattrobj("sequence", "SEGMENTS");
			IomObject innerSegBottom=segments.addattrobj("segment", "COORD");
			innerSegBottom.setattrvalue("C1", "500000.000");
			innerSegBottom.setattrvalue("C2", "100000.000");
			IomObject innerSegTop=segments.addattrobj("segment", "COORD");
			innerSegTop.setattrvalue("C1", "500000.000");
			innerSegTop.setattrvalue("C2", "290000.000");
		}
		// inner boundary polyline: cross(leftTop to center)
		{
			IomObject inner_Cross_LeftTopToCenter = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Cross_LeftTopToCenter.addattrobj("sequence", "SEGMENTS");
			IomObject innerSegTopLeft=segments.addattrobj("segment", "COORD");
			innerSegTopLeft.setattrvalue("C1", "500000.000");
			innerSegTopLeft.setattrvalue("C2", "290000.000");
			IomObject innerSegCenter=segments.addattrobj("segment", "COORD");
			innerSegCenter.setattrvalue("C1", "600000.000");
			innerSegCenter.setattrvalue("C2", "200000.000");
		}
		// inner boundary polyline: cross(center to leftBottom)
		{
			IomObject inner_Cross_CenterToBottomLeft = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Cross_CenterToBottomLeft.addattrobj("sequence", "SEGMENTS");
			IomObject innerSegCenter=segments.addattrobj("segment", "COORD");
			innerSegCenter.setattrvalue("C1", "600000.000");
			innerSegCenter.setattrvalue("C2", "200000.000");
			IomObject innerSegBottomLeft=segments.addattrobj("segment", "COORD");
			innerSegBottomLeft.setattrvalue("C1", "500000.000");
			innerSegBottomLeft.setattrvalue("C2", "100000.000");
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(geometricFigure));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(2,logger.getErrs().size());
		assertEquals("Overlay coord1 (500000.000, 100000.000), coord2 (500000.000, 290000.000), tids o1, o1", logger.getErrs().get(0).getEventMsg());
		assertEquals("Intersection coord1 (500000.000, 290000.000), tids o1, o1", logger.getErrs().get(1).getEventMsg());
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn
	// die Outerboundaries der beiden Polygone uebereinander liegen
	// und das zweite Polygon eine Innerboundary besitzt.
	@Test
	public void twoPolygon_2OuterboundariesOverlayOnEachOther_Fail(){
		Iom_jObject polygon1=new Iom_jObject(ILI_CLASSD, OID1);
		{
			IomObject multisurface=polygon1.addattrobj("area2d", "MULTISURFACE");
			IomObject surface = multisurface.addattrobj("surface", "SURFACE");
			// outer boundary
			IomObject outerBoundary = surface.addattrobj("boundary", "BOUNDARY");
			{
				// polyline 1
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "500000.000");
					startSegments.setattrvalue("C2", "100000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "600000.000");
					endSegments.setattrvalue("C2", "100000.000");
				}
				// polyline 2
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "600000.000");
					startSegments.setattrvalue("C2", "100000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "600000.000");
					endSegments.setattrvalue("C2", "200000.000");
				}
				// polyline 3
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "600000.000");
					startSegments.setattrvalue("C2", "200000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "500000.000");
					endSegments.setattrvalue("C2", "200000.000");
				}
				// polyline 4
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "500000.000");
					startSegments.setattrvalue("C2", "200000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "500000.000");
					endSegments.setattrvalue("C2", "100000.000");
				}
			}
		}
		Iom_jObject polygon2=new Iom_jObject(ILI_CLASSD, OID2);
		{
			IomObject multisurface=polygon2.addattrobj("area2d", "MULTISURFACE");
			IomObject surface = multisurface.addattrobj("surface", "SURFACE");
			// outer boundary
			IomObject outerBoundary = surface.addattrobj("boundary", "BOUNDARY");
			{
				// polyline 1
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "500000.000");
					startSegments.setattrvalue("C2", "100000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "600000.000");
					endSegments.setattrvalue("C2", "100000.000");
				}
				// polyline 2
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "600000.000");
					startSegments.setattrvalue("C2", "100000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "600000.000");
					endSegments.setattrvalue("C2", "200000.000");
				}
				// polyline 3
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "600000.000");
					startSegments.setattrvalue("C2", "200000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "500000.000");
					endSegments.setattrvalue("C2", "200000.000");
				}
				// polyline 4
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "500000.000");
					startSegments.setattrvalue("C2", "200000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "500000.000");
					endSegments.setattrvalue("C2", "100000.000");
				}
			}
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(polygon1));
		validator.validate(new ObjectEvent(polygon2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("polygons overlay tid1 o1, tid2 o2", logger.getErrs().get(0).getEventMsg());
		assertEquals("failed to validate AREA Datatypes23.Topic.ClassD.area2d", logger.getErrs().get(1).getEventMsg());
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn
	// das 2.te Polygon, auf dem 1.ten Polygon liegt.
	// das dritte Polygon liegt innerhalb der envelope Flaeche, jedoch nicht auf einer anderen Linie.
	// das vierte Polygon liegt ausserhalb der envelope Flaeche und uerberdeckt keine anderen Linien.
	@Test
	public void fourPolygon_2PolygonsOverlayEachOtherOn1Line_Fail(){
		Iom_jObject polygon1=new Iom_jObject(ILI_CLASSD, OID1);
		{
			IomObject multisurface=polygon1.addattrobj("area2d", "MULTISURFACE");
			IomObject surface = multisurface.addattrobj("surface", "SURFACE");
			// outer boundary
			IomObject outerBoundary = surface.addattrobj("boundary", "BOUNDARY");
			{
				// polyline 1
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "550000.000");
					startSegments.setattrvalue("C2", "160000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "560000.000");
					endSegments.setattrvalue("C2", "150000.000");
				}
				// polyline 2
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "560000.000");
					startSegments.setattrvalue("C2", "150000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "550000.000");
					endSegments.setattrvalue("C2", "140000.000");
				}
				// polyline 3
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "550000.000");
					startSegments.setattrvalue("C2", "140000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "540000.000");
					endSegments.setattrvalue("C2", "150000.000");
				}
				// polyline 4
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "540000.000");
					startSegments.setattrvalue("C2", "150000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "550000.000");
					endSegments.setattrvalue("C2", "160000.000");
				}
			}
		}
		Iom_jObject polygon2=new Iom_jObject(ILI_CLASSD, OID2);
		{
			IomObject multisurface=polygon2.addattrobj("area2d", "MULTISURFACE");
			IomObject surface = multisurface.addattrobj("surface", "SURFACE");
			// outer boundary
			IomObject outerBoundary = surface.addattrobj("boundary", "BOUNDARY");
			{
				// polyline 1
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "550000.000");
					startSegments.setattrvalue("C2", "160000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "560000.000");
					endSegments.setattrvalue("C2", "150000.000");
				}
				// polyline 2
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "560000.000");
					startSegments.setattrvalue("C2", "150000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "550000.000");
					endSegments.setattrvalue("C2", "140000.000");
				}
				// polyline 3
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "550000.000");
					startSegments.setattrvalue("C2", "140000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "540000.000");
					endSegments.setattrvalue("C2", "150000.000");
				}
				// polyline 4
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "540000.000");
					startSegments.setattrvalue("C2", "150000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "550000.000");
					endSegments.setattrvalue("C2", "160000.000");
				}
			}
		}
		Iom_jObject polygon3=new Iom_jObject(ILI_CLASSD, OID3);
		{
			IomObject multisurface=polygon3.addattrobj("area2d", "MULTISURFACE");
			IomObject surface = multisurface.addattrobj("surface", "SURFACE");
			// outer boundary
			IomObject outerBoundary = surface.addattrobj("boundary", "BOUNDARY");
			{
				// polyline 1
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "540000.000");
					startSegments.setattrvalue("C2", "160000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "542000.000");
					endSegments.setattrvalue("C2", "158000.000");
				}
				// polyline 2
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "542000.000");
					startSegments.setattrvalue("C2", "158000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "542000.000");
					endSegments.setattrvalue("C2", "156000.000");
				}
				// polyline 3
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "542000.000");
					startSegments.setattrvalue("C2", "156000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "540000.000");
					endSegments.setattrvalue("C2", "160000.000");
				}
			}
		}
		Iom_jObject polygon4=new Iom_jObject(ILI_CLASSD, OID4);
		{
			IomObject multisurface=polygon4.addattrobj("area2d", "MULTISURFACE");
			IomObject surface = multisurface.addattrobj("surface", "SURFACE");
			// outer boundary
			IomObject outerBoundary = surface.addattrobj("boundary", "BOUNDARY");
			{
				// polyline 1
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "500000.000");
					startSegments.setattrvalue("C2", "200000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "520000.000");
					endSegments.setattrvalue("C2", "200000.000");
				}
				// polyline 2
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "520000.000");
					startSegments.setattrvalue("C2", "200000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "520000.000");
					endSegments.setattrvalue("C2", "180000.000");
				}
				// polyline 3
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "520000.000");
					startSegments.setattrvalue("C2", "180000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "500000.000");
					endSegments.setattrvalue("C2", "180000.000");
				}
				// polyline 4
				{
					IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
					IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
					IomObject startSegments=segments.addattrobj("segment", "COORD");
					startSegments.setattrvalue("C1", "500000.000");
					startSegments.setattrvalue("C2", "180000.000");
					IomObject endSegments=segments.addattrobj("segment", "COORD");
					endSegments.setattrvalue("C1", "500000.000");
					endSegments.setattrvalue("C2", "200000.000");
				}
			}
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(polygon1));
		validator.validate(new ObjectEvent(polygon2));
		validator.validate(new ObjectEvent(polygon3));
		validator.validate(new ObjectEvent(polygon4));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("polygons overlay tid1 o1, tid2 o2", logger.getErrs().get(0).getEventMsg());
		assertEquals("failed to validate AREA Datatypes23.Topic.ClassD.area2d", logger.getErrs().get(1).getEventMsg());
	}
	
	// prueft ob die folgende Situation erstellt werden kann.
	// Es wird ein aeusseres Objekt (externalObject) mit einem aeusseren und einem inneren Rand erstellt.
	// Danach wird ein zweites, inneres Objekt (internalObject) mit nur einem aeusseren Rand erstellt 
	// (innerhalb des inneren Randes des aeusseren Objekts).
	// Das innere Objekt beruehrt den inneren Rand des aeusseren Objektes auf einer Strecke.
	@Test
	public void twoPolygon_Polygon2InsideInnerBoundaryOfPolygon1_OverlayOn1Line_Ok(){
		// external object
		Iom_jObject externalObject=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject externalMultisurface=externalObject.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = externalMultisurface.addattrobj("surface", "SURFACE");
		{
			// outer boundary of external object
			IomObject outerBoundaryExternalObj = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline 1
			IomObject polyline1 = outerBoundaryExternalObj.addattrobj("polyline", "POLYLINE");
			IomObject segments=polyline1.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "500000.000");
			startSegment.setattrvalue("C2", "100000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "600000.000");
			endSegment.setattrvalue("C2", "100000.000");
			// polyline 2
			IomObject polyline2 = outerBoundaryExternalObj.addattrobj("polyline", "POLYLINE");
			IomObject segments2=polyline2.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2=segments2.addattrobj("segment", "COORD");
			startSegment2.setattrvalue("C1", "600000.000");
			startSegment2.setattrvalue("C2", "100000.000");
			IomObject endSegment2=segments2.addattrobj("segment", "COORD");
			endSegment2.setattrvalue("C1", "600000.000");
			endSegment2.setattrvalue("C2", "200000.000");
			// polyline 3
			IomObject polyline3 = outerBoundaryExternalObj.addattrobj("polyline", "POLYLINE");
			IomObject segments3=polyline3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3=segments3.addattrobj("segment", "COORD");
			startSegment3.setattrvalue("C1", "600000.000");
			startSegment3.setattrvalue("C2", "200000.000");
			IomObject endSegment3=segments3.addattrobj("segment", "COORD");
			endSegment3.setattrvalue("C1", "500000.000");
			endSegment3.setattrvalue("C2", "200000.000");
			// polyline 4
			IomObject polyline4 = outerBoundaryExternalObj.addattrobj("polyline", "POLYLINE");
			IomObject segments4=polyline4.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment4=segments4.addattrobj("segment", "COORD");
			startSegment4.setattrvalue("C1", "500000.000");
			startSegment4.setattrvalue("C2", "200000.000");
			IomObject endSegment4=segments4.addattrobj("segment", "COORD");
			endSegment4.setattrvalue("C1", "500000.000");
			endSegment4.setattrvalue("C2", "100000.000");
		}
		// inner boundary of external object
		IomObject innerBoundaryExternalObj = surfaceValue.addattrobj("boundary", "BOUNDARY");
		{
			// polyline 1
			IomObject polyline1 = innerBoundaryExternalObj.addattrobj("polyline", "POLYLINE");
			IomObject segmentsInner=polyline1.addattrobj("sequence", "SEGMENTS");
			IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
			startSegmentInner.setattrvalue("C1", "545000.000");
			startSegmentInner.setattrvalue("C2", "110000.000");
			IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
			endSegmentInner.setattrvalue("C1", "585000.000");
			endSegmentInner.setattrvalue("C2", "185000.000");
			// polyline 2
			IomObject polyline2 = innerBoundaryExternalObj.addattrobj("polyline", "POLYLINE");
			IomObject segments2Inner=polyline2.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
			startSegment2Inner.setattrvalue("C1", "585000.000");
			startSegment2Inner.setattrvalue("C2", "185000.000");
			IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
			endSegment2Inner.setattrvalue("C1", "515000.000");
			endSegment2Inner.setattrvalue("C2", "185000.000");
			// polyline 3
			IomObject polyline3 = innerBoundaryExternalObj.addattrobj("polyline", "POLYLINE");
			IomObject segments3Inner=polyline3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
			startSegment3Inner.setattrvalue("C1", "515000.000");
			startSegment3Inner.setattrvalue("C2", "185000.000");
			IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
			endSegment3Inner.setattrvalue("C1", "545000.000");
			endSegment3Inner.setattrvalue("C2", "110000.000");
		}
		// internal object
		Iom_jObject internalObject=new Iom_jObject(ILI_CLASSD, OID2);
		IomObject internalMultisurface=internalObject.addattrobj("area2d", "MULTISURFACE");
		IomObject internalSurface = internalMultisurface.addattrobj("surface", "SURFACE");
		// outer boundary of internal object
		IomObject outerBoundaryInternalObj = internalSurface.addattrobj("boundary", "BOUNDARY");
		{
			// polyline 1
			IomObject polyline1 = outerBoundaryInternalObj.addattrobj("polyline", "POLYLINE");
			IomObject segments=polyline1.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "545000.000");
			startSegment.setattrvalue("C2", "130000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "585000.000");
			endSegment.setattrvalue("C2", "185000.000");
			// polyline 2
			IomObject polyline2 = outerBoundaryInternalObj.addattrobj("polyline", "POLYLINE");
			IomObject segments2=polyline2.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2=segments2.addattrobj("segment", "COORD");
			startSegment2.setattrvalue("C1", "585000.000");
			startSegment2.setattrvalue("C2", "185000.000");
			IomObject endSegment2=segments2.addattrobj("segment", "COORD");
			endSegment2.setattrvalue("C1", "515000.000");
			endSegment2.setattrvalue("C2", "185000.000");
			// polyline 3
			IomObject polyline3 = outerBoundaryInternalObj.addattrobj("polyline", "POLYLINE");
			IomObject segments3=polyline3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3=segments3.addattrobj("segment", "COORD");
			startSegment3.setattrvalue("C1", "515000.000");
			startSegment3.setattrvalue("C2", "185000.000");
			IomObject endSegment3=segments3.addattrobj("segment", "COORD");
			endSegment3.setattrvalue("C1", "545000.000");
			endSegment3.setattrvalue("C2", "130000.000");
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(externalObject));
		validator.validate(new ObjectEvent(internalObject));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft, ob der richtige Pfad: Model.Topic.Klasse in der Fehlermeldung einer
	// intersection ausgegeben wird.
	@Test
	public void checkClassPath_OfIntersectionMessage_False(){
		EhiLogger.getInstance().setTraceFilter(false);
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		{
			IomObject multisurfaceValue=objAreaSuccess.addattrobj("area3d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "100000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "100000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue2.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "100000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "200000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue3.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "200000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "200000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 4
			IomObject polylineValue4 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue4.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "200000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "100000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
		}
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSD, OID2);
		{
			IomObject multisurfaceValue=objAreaSuccess2.addattrobj("area3d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "80000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "80000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue2.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "80000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "550000.000");
				endSegment.setattrvalue("C2", "110000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue3.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "550000.000");
				startSegment.setattrvalue("C2", "110000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "80000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objAreaSuccess));
		validator.validate(new ObjectEvent(objAreaSuccess2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		assertTrue(logger.getErrs().size()==3);
		LogEventImpl eventImpl=(LogEventImpl) logger.getErrs().get(0);
		assertEquals("Datatypes23.Topic.ClassD",eventImpl.getSourceObjectTag());
		LogEventImpl eventImpl2=(LogEventImpl) logger.getErrs().get(1);
		assertEquals("Datatypes23.Topic.ClassD",eventImpl2.getSourceObjectTag());
	}
	
	// prueft, ob die richtige Objekt ID in der Fehlermeldung einer
	// intersection ausgegeben wird.
	@Test
	public void checkOid_OfIntersectionMessage_False(){
		EhiLogger.getInstance().setTraceFilter(false);
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		{
			IomObject multisurfaceValue=objAreaSuccess.addattrobj("area3d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "100000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "100000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue2.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "100000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "200000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue3.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "200000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "200000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 4
			IomObject polylineValue4 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue4.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "200000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "100000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
		}
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSD, OID2);
		{
			IomObject multisurfaceValue=objAreaSuccess2.addattrobj("area3d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "80000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "80000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue2.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "80000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "550000.000");
				endSegment.setattrvalue("C2", "110000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue3.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "550000.000");
				startSegment.setattrvalue("C2", "110000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "80000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objAreaSuccess));
		validator.validate(new ObjectEvent(objAreaSuccess2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		assertTrue(logger.getErrs().size()==3);
		LogEventImpl eventImpl=(LogEventImpl) logger.getErrs().get(0);
		assertEquals("o1",eventImpl.getSourceObjectXtfId());
		LogEventImpl eventImpl2=(LogEventImpl) logger.getErrs().get(1);
		assertEquals("o1",eventImpl2.getSourceObjectXtfId());
		LogEventImpl eventImpl3=(LogEventImpl) logger.getErrs().get(2);
		assertEquals(null,eventImpl3.getSourceObjectXtfId());
	}
	
	// prueft, ob die richtige Objekt ID in der Fehlermeldung einer
	// intersection ausgegeben wird.
	@Test
	public void checkOid_OfArcIntersectionMessage_False(){
		EhiLogger.getInstance().setTraceFilter(false);
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		{
			IomObject multisurfaceValue=objAreaSuccess.addattrobj("area3d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "100000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "100000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue2.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "100000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "200000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue3.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "200000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "200000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 4
			IomObject polylineValue4 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue4.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "200000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "100000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
		}
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSD, OID2);
		{
			IomObject multisurfaceValue=objAreaSuccess2.addattrobj("area3d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "80000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "80000.000");
				endSegment.setattrvalue("C3", "5000.000");
			}
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue2.addattrobj("sequence", "SEGMENTS");
			{
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "80000.000");
				startSegment.setattrvalue("C3", "5000.000");
			}
			{
				IomObject arcSegment=segments.addattrobj("segment", "ARC");
				arcSegment.setattrvalue("A1", "550000.000");
				arcSegment.setattrvalue("A2", "110000.000");
				arcSegment.setattrvalue("C1", "500000.000");
				arcSegment.setattrvalue("C2", "80000.000");
				arcSegment.setattrvalue("C3", "5000.000");
			}
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objAreaSuccess));
		validator.validate(new ObjectEvent(objAreaSuccess2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		assertTrue(logger.getErrs().size()==2);
		LogEventImpl eventImpl=(LogEventImpl) logger.getErrs().get(0);
		assertEquals("o1",eventImpl.getSourceObjectXtfId());
		LogEventImpl eventImpl2=(LogEventImpl) logger.getErrs().get(1);
		assertEquals(null,eventImpl2.getSourceObjectXtfId());
	}
	
	// prueft, ob die Validierung keinen Fehler meldet, wenn ein Polygon,
	// genau ueber einem InnerBoundary eines anderen Polygons liegt.
	@Test
	public void twoPolygon_Polygon2ExactlyOverInnerBoundaryOfPolygon1_Ok(){
		Iom_jObject obj1=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurface=obj1.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurface.addattrobj("surface", "SURFACE");
		{
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			{
				// polyline 1
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "100000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "100000.000");
			}
			{
				// polyline 2
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "100000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "200000.000");
			}
			{
				// polyline 3
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "200000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "200000.000");
			}
			{
				// polyline 4
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "200000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "100000.000");
			}
			IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			{
				// polyline 1
				IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "540000.000");
				startSegment.setattrvalue("C2", "140000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "560000.000");
				endSegment.setattrvalue("C2", "140000.000");
			}
			{
				// polyline 2
				IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "560000.000");
				startSegment.setattrvalue("C2", "140000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "560000.000");
				endSegment.setattrvalue("C2", "160000.000");
			}
			{
				// polyline 3
				IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "560000.000");
				startSegment.setattrvalue("C2", "160000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "540000.000");
				endSegment.setattrvalue("C2", "160000.000");
			}
			{
				// polyline 4
				IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "540000.000");
				startSegment.setattrvalue("C2", "160000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "540000.000");
				endSegment.setattrvalue("C2", "140000.000");
			}
		}
		Iom_jObject obj2=new Iom_jObject(ILI_CLASSD, OID2);
		IomObject multisurface2=obj2.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue2 = multisurface2.addattrobj("surface", "SURFACE");
		{
			IomObject outerBoundary = surfaceValue2.addattrobj("boundary", "BOUNDARY");
			{
				// polyline 1
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "540000.000");
				startSegment.setattrvalue("C2", "140000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "560000.000");
				endSegment.setattrvalue("C2", "140000.000");
			}
			{
				// polyline 2
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "560000.000");
				startSegment.setattrvalue("C2", "140000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "560000.000");
				endSegment.setattrvalue("C2", "160000.000");
			}
			{
				// polyline 3
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "560000.000");
				startSegment.setattrvalue("C2", "160000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "540000.000");
				endSegment.setattrvalue("C2", "160000.000");
			}
			{
				// polyline 4
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "540000.000");
				startSegment.setattrvalue("C2", "160000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "540000.000");
				endSegment.setattrvalue("C2", "140000.000");
			}
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
    // prueft, ob die Validierung einen Fehler meldet, wenn ein Polygon,
    // innerhalb eines anderen Polygons liegt.
    @Test
    @Ignore("testcase for ilivalidator#219")
    public void twoPolygon_Polygon2OverlapsPolygon1_Fail(){
        Iom_jObject obj1=new Iom_jObject(ILI_CLASSD, OID1);
        IomObject multisurface=obj1.addattrobj("area2d", "MULTISURFACE");
        IomObject surfaceValue = multisurface.addattrobj("surface", "SURFACE");
        {
            IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
            {
                // polyline 1
                IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
                IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
                IomObject startSegment=segments.addattrobj("segment", "COORD");
                startSegment.setattrvalue("C1", "500000.000");
                startSegment.setattrvalue("C2", "100000.000");
                IomObject endSegment=segments.addattrobj("segment", "COORD");
                endSegment.setattrvalue("C1", "600000.000");
                endSegment.setattrvalue("C2", "100000.000");
            }
            {
                // polyline 2
                IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
                IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
                IomObject startSegment=segments.addattrobj("segment", "COORD");
                startSegment.setattrvalue("C1", "600000.000");
                startSegment.setattrvalue("C2", "100000.000");
                IomObject endSegment=segments.addattrobj("segment", "COORD");
                endSegment.setattrvalue("C1", "600000.000");
                endSegment.setattrvalue("C2", "200000.000");
            }
            {
                // polyline 3
                IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
                IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
                IomObject startSegment=segments.addattrobj("segment", "COORD");
                startSegment.setattrvalue("C1", "600000.000");
                startSegment.setattrvalue("C2", "200000.000");
                IomObject endSegment=segments.addattrobj("segment", "COORD");
                endSegment.setattrvalue("C1", "500000.000");
                endSegment.setattrvalue("C2", "200000.000");
            }
            {
                // polyline 4
                IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
                IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
                IomObject startSegment=segments.addattrobj("segment", "COORD");
                startSegment.setattrvalue("C1", "500000.000");
                startSegment.setattrvalue("C2", "200000.000");
                IomObject endSegment=segments.addattrobj("segment", "COORD");
                endSegment.setattrvalue("C1", "500000.000");
                endSegment.setattrvalue("C2", "100000.000");
            }
        }
        Iom_jObject obj2=new Iom_jObject(ILI_CLASSD, OID2);
        IomObject multisurface2=obj2.addattrobj("area2d", "MULTISURFACE");
        IomObject surfaceValue2 = multisurface2.addattrobj("surface", "SURFACE");
        {
            IomObject outerBoundary = surfaceValue2.addattrobj("boundary", "BOUNDARY");
            {
                // polyline 1
                IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
                IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
                IomObject startSegment=segments.addattrobj("segment", "COORD");
                startSegment.setattrvalue("C1", "540000.000");
                startSegment.setattrvalue("C2", "140000.000");
                IomObject endSegment=segments.addattrobj("segment", "COORD");
                endSegment.setattrvalue("C1", "560000.000");
                endSegment.setattrvalue("C2", "140000.000");
            }
            {
                // polyline 2
                IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
                IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
                IomObject startSegment=segments.addattrobj("segment", "COORD");
                startSegment.setattrvalue("C1", "560000.000");
                startSegment.setattrvalue("C2", "140000.000");
                IomObject endSegment=segments.addattrobj("segment", "COORD");
                endSegment.setattrvalue("C1", "560000.000");
                endSegment.setattrvalue("C2", "160000.000");
            }
            {
                // polyline 3
                IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
                IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
                IomObject startSegment=segments.addattrobj("segment", "COORD");
                startSegment.setattrvalue("C1", "560000.000");
                startSegment.setattrvalue("C2", "160000.000");
                IomObject endSegment=segments.addattrobj("segment", "COORD");
                endSegment.setattrvalue("C1", "540000.000");
                endSegment.setattrvalue("C2", "160000.000");
            }
            {
                // polyline 4
                IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
                IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
                IomObject startSegment=segments.addattrobj("segment", "COORD");
                startSegment.setattrvalue("C1", "540000.000");
                startSegment.setattrvalue("C2", "160000.000");
                IomObject endSegment=segments.addattrobj("segment", "COORD");
                endSegment.setattrvalue("C1", "540000.000");
                endSegment.setattrvalue("C2", "140000.000");
            }
        }
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
        validator.validate(new ObjectEvent(obj1));
        validator.validate(new ObjectEvent(obj2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
    }
    // prueft, ob die Validierung keinen Fehler meldet, wenn ein Polygon,
    // genau ueber einem InnerBoundary eines anderen Polygons liegt.
    @Test
    public void twoPolygonWithArc_Polygon2ExactlyOverInnerBoundaryOfPolygon1_Ok() throws Exception{
        IomObject innerBoundary = null;
        Iom_jObject obj1=new Iom_jObject(ILI_CLASSD, OID1);
        IomObject multisurface=obj1.addattrobj("area2d", "MULTISURFACE");
        IomObject surfaceValue = multisurface.addattrobj("surface", "SURFACE");
        {
            IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
            {
                // polyline 1
                IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
                IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
                IomObject startSegment=segments.addattrobj("segment", "COORD");
                startSegment.setattrvalue("C1", "500000.000");
                startSegment.setattrvalue("C2", "100000.000");
                IomObject endSegment=segments.addattrobj("segment", "COORD");
                endSegment.setattrvalue("C1", "600000.000");
                endSegment.setattrvalue("C2", "100000.000");
            }
            {
                // polyline 2
                IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
                IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
                IomObject startSegment=segments.addattrobj("segment", "COORD");
                startSegment.setattrvalue("C1", "600000.000");
                startSegment.setattrvalue("C2", "100000.000");
                IomObject endSegment=segments.addattrobj("segment", "COORD");
                endSegment.setattrvalue("C1", "600000.000");
                endSegment.setattrvalue("C2", "200000.000");
            }
            {
                // polyline 3
                IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
                IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
                IomObject startSegment=segments.addattrobj("segment", "COORD");
                startSegment.setattrvalue("C1", "600000.000");
                startSegment.setattrvalue("C2", "200000.000");
                IomObject endSegment=segments.addattrobj("segment", "COORD");
                endSegment.setattrvalue("C1", "500000.000");
                endSegment.setattrvalue("C2", "200000.000");
            }
            {
                // polyline 4
                IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
                IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
                IomObject startSegment=segments.addattrobj("segment", "COORD");
                startSegment.setattrvalue("C1", "500000.000");
                startSegment.setattrvalue("C2", "200000.000");
                IomObject endSegment=segments.addattrobj("segment", "COORD");
                endSegment.setattrvalue("C1", "500000.000");
                endSegment.setattrvalue("C2", "100000.000");
            }
            innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
            {
                // polyline 1
                IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
                IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
                IomObject startSegment=segments.addattrobj("segment", "COORD");
                startSegment.setattrvalue("C1", "540000.000");
                startSegment.setattrvalue("C2", "140000.000");
                IomObject endSegment=segments.addattrobj("segment", "ARC");
                endSegment.setattrvalue("A1", "555000.000");
                //endSegment.setattrvalue("A2", "139800.000");
                endSegment.setattrvalue("A2", "140100.000");
                endSegment.setattrvalue("C1", "560000.000");
                endSegment.setattrvalue("C2", "140000.000");
            }
            {
                // polyline 2
                IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
                IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
                IomObject startSegment=segments.addattrobj("segment", "COORD");
                startSegment.setattrvalue("C1", "560000.000");
                startSegment.setattrvalue("C2", "140000.000");
                IomObject endSegment=segments.addattrobj("segment", "COORD");
                endSegment.setattrvalue("C1", "560000.000");
                endSegment.setattrvalue("C2", "160000.000");
            }
            {
                // polyline 3
                IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
                IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
                IomObject startSegment=segments.addattrobj("segment", "COORD");
                startSegment.setattrvalue("C1", "560000.000");
                startSegment.setattrvalue("C2", "160000.000");
                IomObject endSegment=segments.addattrobj("segment", "COORD");
                endSegment.setattrvalue("C1", "540000.000");
                endSegment.setattrvalue("C2", "160000.000");
            }
            {
                // polyline 4
                IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
                IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
                IomObject startSegment=segments.addattrobj("segment", "COORD");
                startSegment.setattrvalue("C1", "540000.000");
                startSegment.setattrvalue("C2", "160000.000");
                IomObject endSegment=segments.addattrobj("segment", "COORD");
                endSegment.setattrvalue("C1", "540000.000");
                endSegment.setattrvalue("C2", "140000.000");
            }
        }
        Iom_jObject obj2=new Iom_jObject(ILI_CLASSD, OID2);
        IomObject multisurface2=obj2.addattrobj("area2d", "MULTISURFACE");
        IomObject surfaceValue2 = multisurface2.addattrobj("surface", "SURFACE");
        {
            surfaceValue2.addattrobj("boundary", innerBoundary);
        }
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        //settings.setValue(Validator.CONFIG_DEBUG_XTFOUT, "src/test/data/validator/out.xtf");
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
        validator.validate(new ObjectEvent(obj1));
        validator.validate(new ObjectEvent(obj2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==0);        
    }
	
	// es wird eine Fehlermeldung erwartet, da ein Polygon, nicht genau ueber einem InnerBoundary einer anderen Polygon liegt.
	@Test
	public void twoPolygon_Polygon2OverlapsInnerBoundaryOfPolygon1_False(){
		Iom_jObject obj1=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurface=obj1.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurface.addattrobj("surface", "SURFACE");
		{
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			{
				// polyline 1
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "100000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "100000.000");
			}
			{
				// polyline 2
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "100000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "600000.000");
				endSegment.setattrvalue("C2", "200000.000");
			}
			{
				// polyline 3
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "200000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "200000.000");
			}
			{
				// polyline 4
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "500000.000");
				startSegment.setattrvalue("C2", "200000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "500000.000");
				endSegment.setattrvalue("C2", "100000.000");
			}
			IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			{
				// polyline 1
				IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "540000.000");
				startSegment.setattrvalue("C2", "140000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "560000.000");
				endSegment.setattrvalue("C2", "140000.000");
			}
			{
				// polyline 2
				IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "560000.000");
				startSegment.setattrvalue("C2", "140000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "560000.000");
				endSegment.setattrvalue("C2", "160000.000");
			}
			{
				// polyline 3
				IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "560000.000");
				startSegment.setattrvalue("C2", "160000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "540000.000");
				endSegment.setattrvalue("C2", "160000.000");
			}
			{
				// polyline 4
				IomObject polyline = innerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "540000.000");
				startSegment.setattrvalue("C2", "160000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "540000.000");
				endSegment.setattrvalue("C2", "140000.000");
			}
		}
		Iom_jObject obj2=new Iom_jObject(ILI_CLASSD, OID2);
		IomObject multisurface2=obj2.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue2 = multisurface2.addattrobj("surface", "SURFACE");
		{
			IomObject outerBoundary = surfaceValue2.addattrobj("boundary", "BOUNDARY");
			{
				// polyline 1
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "540000.000");
				startSegment.setattrvalue("C2", "140000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "560000.000");
				endSegment.setattrvalue("C2", "140000.000");
			}
			{
				// polyline 2
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "560000.000");
				startSegment.setattrvalue("C2", "140000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "580000.000");
				endSegment.setattrvalue("C2", "180000.000");
			}
			{
				// polyline 3
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "580000.000");
				startSegment.setattrvalue("C2", "180000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "540000.000");
				endSegment.setattrvalue("C2", "160000.000");
			}
			{
				// polyline 4
				IomObject polyline = outerBoundary.addattrobj("polyline", "POLYLINE");
				IomObject segments=polyline.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "540000.000");
				startSegment.setattrvalue("C2", "160000.000");
				IomObject endSegment=segments.addattrobj("segment", "COORD");
				endSegment.setattrvalue("C1", "540000.000");
				endSegment.setattrvalue("C2", "140000.000");
			}
		}
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("polygons overlay tid1 o1, tid2 o2", logger.getErrs().get(0).getEventMsg());
		assertEquals("failed to validate AREA Datatypes23.Topic.ClassD.area2d", logger.getErrs().get(1).getEventMsg());
	}
	
	// prueft ob ein Polygon korrekt auf 3 Stellen nach dem Komma gerundet wird
	// und dabei keine Fehler festgestellt werden.
	@Test
	public void onePolygon_1Boundary_Area_RoundingUpTo3DecPoints_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue1=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue1.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "479999.9995");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.0000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "479999.9995");
		endSegment3.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft ob die Koordinaten richtig abgerundet werden.
	@Test
	public void onePolygon_1Boundary_Area_RoundingDown_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue1=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue1.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.0004");
		startSegment.setattrvalue("C2", "70000.0004");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.0004");
		endSegment3.setattrvalue("C2", "70000.0004");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft ob das Aufrunden der Koordinaten zu einer intersection fuehrt.
	@Test
	public void onePolygon_1Boundary_Area_RoundingUp_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject multisurfaceValue1=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue1.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.0004");
		startSegment.setattrvalue("C2", "70000.0004");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.0005");
		endSegment3.setattrvalue("C2", "70000.0005");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("Intersection coord1 (480000.003, 70000.002), tids o1, o1", logger.getErrs().get(0).getEventMsg());
	}
	
    @Test
    public void notApolygon_Fail(){
        Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
        objSurfaceSuccess.setattrvalue("area2d", "3");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
        validator.validate(new ObjectEvent(objSurfaceSuccess));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("The value <3> is not a Polygon in attribute area2d", logger.getErrs().get(0).getEventMsg());
    }
}