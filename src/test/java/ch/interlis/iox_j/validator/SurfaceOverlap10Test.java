package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

public class SurfaceOverlap10Test {

	private TransferDescription td=null;
	// OID
	private final static String OBJ_OID1 ="o1";
	// MODEL.TOPIC
	private final static String ILI_TOPIC="SurfaceOverlapTest10.Topic";
	// CLASS
	private final static String ILI_CLASSLINETABLE=ILI_TOPIC+".LineTable";
	private final static String ILI_CLASSFLAECHENTABLE=ILI_TOPIC+".FlaechenTable";
	// START BASKET EVENT
	private final static String BASKET_ID1="b1";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili1cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/SurfaceOverlapTest10.ili", FileEntryKind.ILIMODELFILE);
		ili1cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili1cConfig);
		assertNotNull(td);
	}

	//############################################################/
	//########## SUCCESSFUL TESTS ################################/
	//############################################################/
	////////////////// START FlaechentypSurfaceWithoutOverlap /////
	// there are 2 innerboundaries which dont touch each other. Expected: ok.
	@Test
	public void surfaceWithoutOverlaps2d_noOverlapOfInnerboundaries_OK(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surfaceWithoutOverlaps2d", "MULTISURFACE");
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
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// there are 2 innerboundaries which touch each other on 1 point. Expected: ok.
	@Test
	public void surfaceWithoutOverlaps2d_touchOfInnerboundariesOn1Point_OK(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surfaceWithoutOverlaps2d", "MULTISURFACE");
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
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}	
	
	// 1 point of innerboundary touch the outerboundary. Expected ok.
	@Test
	public void surfaceWithoutOverlaps2d_Oberlabed1PointTogether_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surfaceWithoutOverlaps2d", "MULTISURFACE");
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
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// 1 point of innerboundary touch the outerboundary. Expected ok.
	@Test
	public void area2D_Oberlabed1PointTogether_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// there are 2 innerboundaries which dont touch each other. Expected: ok.
	@Test
	public void area2D_noOverlapOfInnerboundaries_Expexted_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// there are 2 innerboundaries which touch each other on 1 point. Expected: ok.
	@Test
	public void area2D_touchOfInnerboundariesOn1Point_Expexted_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// 1 point of innerboundary touch the outerboundary. Expected ok.
	@Test
	public void area3D_Oberlabed1PointTogether_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("areaWithoutOverlaps3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		endSegment.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		startSegment2.setattrvalue("C3", "5000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		endSegment2.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		startSegment3.setattrvalue("C3", "5000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		endSegment3.setattrvalue("C3", "5000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "480000.000");
		startSegmentInner.setattrvalue("C2", "70000.000");
		startSegmentInner.setattrvalue("C3", "5000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "500000.000");
		endSegmentInner.setattrvalue("C2", "78000.000");
		endSegmentInner.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "500000.000");
		startSegment2Inner.setattrvalue("C2", "78000.000");
		startSegment2Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "505000.000");
		endSegment2Inner.setattrvalue("C2", "78000.000");
		endSegment2Inner.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "505000.000");
		startSegment3Inner.setattrvalue("C2", "78000.000");
		startSegment3Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "480000.000");
		endSegment3Inner.setattrvalue("C2", "70000.000");
		endSegment3Inner.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// there are 2 innerboundaries which dont touch each other. Expected: ok.
	@Test
	public void area3D_noOverlapOfInnerboundaries_Expexted_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("areaWithoutOverlaps3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "850000.000");
		endSegment.setattrvalue("C2", "191500.000");
		endSegment.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "850000.000");
		startSegment2.setattrvalue("C2", "191500.000");
		startSegment2.setattrvalue("C3", "5000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "480000.000");
		endSegment2.setattrvalue("C2", "310000.000");
		endSegment2.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "480000.000");
		startSegment3.setattrvalue("C2", "310000.000");
		startSegment3.setattrvalue("C3", "5000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		endSegment3.setattrvalue("C3", "5000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "486000.000");
		startSegmentInner.setattrvalue("C2", "199000.000");
		startSegmentInner.setattrvalue("C3", "5000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "486000.000");
		endSegmentInner.setattrvalue("C2", "201000.000");
		endSegmentInner.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "486000.000");
		startSegment2Inner.setattrvalue("C2", "201000.000");
		startSegment2Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "488000.000");
		endSegment2Inner.setattrvalue("C2", "201000.000");
		endSegment2Inner.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "488000.000");
		startSegment3Inner.setattrvalue("C2", "201000.000");
		startSegment3Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "486000.000");
		endSegment3Inner.setattrvalue("C2", "199000.000");
		endSegment3Inner.setattrvalue("C3", "5000.000");
		// 2. inner boundary
		IomObject innerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner2=polylineValueInner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		startSegmentInner2.setattrvalue("C1", "490500.000");
		startSegmentInner2.setattrvalue("C2", "199000.000");
		startSegmentInner2.setattrvalue("C3", "5000.000");
		IomObject endSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		endSegmentInner2.setattrvalue("C1", "490500.000");
		endSegmentInner2.setattrvalue("C2", "201000.000");
		endSegmentInner2.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner2=polylineValue2Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		startSegment2Inner2.setattrvalue("C1", "490500.000");
		startSegment2Inner2.setattrvalue("C2", "201000.000");
		startSegment2Inner2.setattrvalue("C3", "5000.000");
		IomObject endSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		endSegment2Inner2.setattrvalue("C1", "489500.000");
		endSegment2Inner2.setattrvalue("C2", "200000.000");
		endSegment2Inner2.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner2=polylineValue3Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		startSegment3Inner2.setattrvalue("C1", "489500.000");
		startSegment3Inner2.setattrvalue("C2", "200000.000");
		startSegment3Inner2.setattrvalue("C3", "5000.000");
		IomObject endSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		endSegment3Inner2.setattrvalue("C1", "490500.000");
		endSegment3Inner2.setattrvalue("C2", "199000.000");
		endSegment3Inner2.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// there are 2 innerboundaries which touch each other on 1 point. Expected: ok.
	@Test
	public void area3D_touchOfInnerboundariesOn1Point_Expexted_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("areaWithoutOverlaps3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "850000.000");
		endSegment.setattrvalue("C2", "191500.000");
		endSegment.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "850000.000");
		startSegment2.setattrvalue("C2", "191500.000");
		startSegment2.setattrvalue("C3", "5000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "480000.000");
		endSegment2.setattrvalue("C2", "310000.000");
		endSegment2.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "480000.000");
		startSegment3.setattrvalue("C2", "310000.000");
		startSegment3.setattrvalue("C3", "5000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		endSegment3.setattrvalue("C3", "5000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "486000.000");
		startSegmentInner.setattrvalue("C2", "199000.000");
		startSegmentInner.setattrvalue("C3", "5000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "486000.000");
		endSegmentInner.setattrvalue("C2", "201000.000");
		endSegmentInner.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "486000.000");
		startSegment2Inner.setattrvalue("C2", "201000.000");
		startSegment2Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "488000.000");
		endSegment2Inner.setattrvalue("C2", "201000.000");
		endSegment2Inner.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "488000.000");
		startSegment3Inner.setattrvalue("C2", "201000.000");
		startSegment3Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "486000.000");
		endSegment3Inner.setattrvalue("C2", "199000.000");
		endSegment3Inner.setattrvalue("C3", "5000.000");
		// 2. inner boundary
		IomObject innerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner2=polylineValueInner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		startSegmentInner2.setattrvalue("C1", "488000.000");
		startSegmentInner2.setattrvalue("C2", "201000.000");
		startSegmentInner2.setattrvalue("C3", "5000.000");
		IomObject endSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		endSegmentInner2.setattrvalue("C1", "490500.000");
		endSegmentInner2.setattrvalue("C2", "201000.000");
		endSegmentInner2.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner2=polylineValue2Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		startSegment2Inner2.setattrvalue("C1", "490500.000");
		startSegment2Inner2.setattrvalue("C2", "201000.000");
		startSegment2Inner2.setattrvalue("C3", "5000.000");
		IomObject endSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		endSegment2Inner2.setattrvalue("C1", "489500.000");
		endSegment2Inner2.setattrvalue("C2", "200000.000");
		endSegment2Inner2.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner2=polylineValue3Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		startSegment3Inner2.setattrvalue("C1", "489500.000");
		startSegment3Inner2.setattrvalue("C2", "200000.000");
		startSegment3Inner2.setattrvalue("C3", "5000.000");
		IomObject endSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		endSegment3Inner2.setattrvalue("C1", "488000.000");
		endSegment3Inner2.setattrvalue("C2", "201000.000");
		endSegment3Inner2.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// there are 2 innerboundaries which dont touch each other. Expected: ok.
	@Test
	public void surfaceWithoutOverlaps3d_noOverlapOfInnerboundaries_OK(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surfaceWithoutOverlaps3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "850000.000");
		endSegment.setattrvalue("C2", "191500.000");
		endSegment.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "850000.000");
		startSegment2.setattrvalue("C2", "191500.000");
		startSegment2.setattrvalue("C3", "5000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "480000.000");
		endSegment2.setattrvalue("C2", "310000.000");
		endSegment2.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "480000.000");
		startSegment3.setattrvalue("C2", "310000.000");
		startSegment3.setattrvalue("C3", "5000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		endSegment3.setattrvalue("C3", "5000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "486000.000");
		startSegmentInner.setattrvalue("C2", "199000.000");
		startSegmentInner.setattrvalue("C3", "5000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "486000.000");
		endSegmentInner.setattrvalue("C2", "201000.000");
		endSegmentInner.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "486000.000");
		startSegment2Inner.setattrvalue("C2", "201000.000");
		startSegment2Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "488000.000");
		endSegment2Inner.setattrvalue("C2", "201000.000");
		endSegment2Inner.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "488000.000");
		startSegment3Inner.setattrvalue("C2", "201000.000");
		startSegment3Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "486000.000");
		endSegment3Inner.setattrvalue("C2", "199000.000");
		endSegment3Inner.setattrvalue("C3", "5000.000");
		// 2. inner boundary
		IomObject innerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner2=polylineValueInner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		startSegmentInner2.setattrvalue("C1", "490500.000");
		startSegmentInner2.setattrvalue("C2", "199000.000");
		startSegmentInner2.setattrvalue("C3", "5000.000");
		IomObject endSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		endSegmentInner2.setattrvalue("C1", "490500.000");
		endSegmentInner2.setattrvalue("C2", "201000.000");
		endSegmentInner2.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner2=polylineValue2Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		startSegment2Inner2.setattrvalue("C1", "490500.000");
		startSegment2Inner2.setattrvalue("C2", "201000.000");
		startSegment2Inner2.setattrvalue("C3", "5000.000");
		IomObject endSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		endSegment2Inner2.setattrvalue("C1", "489500.000");
		endSegment2Inner2.setattrvalue("C2", "200000.000");
		endSegment2Inner2.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner2=polylineValue3Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		startSegment3Inner2.setattrvalue("C1", "489500.000");
		startSegment3Inner2.setattrvalue("C2", "200000.000");
		startSegment3Inner2.setattrvalue("C3", "5000.000");
		IomObject endSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		endSegment3Inner2.setattrvalue("C1", "490500.000");
		endSegment3Inner2.setattrvalue("C2", "199000.000");
		endSegment3Inner2.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// there are 2 innerboundaries which touch each other on 1 point. Expected: ok.
	@Test
	public void surfaceWithoutOverlaps3d_touchOfInnerboundariesOn1Point_OK(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surfaceWithoutOverlaps3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "850000.000");
		endSegment.setattrvalue("C2", "191500.000");
		endSegment.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "850000.000");
		startSegment2.setattrvalue("C2", "191500.000");
		startSegment2.setattrvalue("C3", "5000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "480000.000");
		endSegment2.setattrvalue("C2", "310000.000");
		endSegment2.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "480000.000");
		startSegment3.setattrvalue("C2", "310000.000");
		startSegment3.setattrvalue("C3", "5000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		endSegment3.setattrvalue("C3", "5000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "486000.000");
		startSegmentInner.setattrvalue("C2", "199000.000");
		startSegmentInner.setattrvalue("C3", "5000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "486000.000");
		endSegmentInner.setattrvalue("C2", "201000.000");
		endSegmentInner.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "486000.000");
		startSegment2Inner.setattrvalue("C2", "201000.000");
		startSegment2Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "488000.000");
		endSegment2Inner.setattrvalue("C2", "201000.000");
		endSegment2Inner.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "488000.000");
		startSegment3Inner.setattrvalue("C2", "201000.000");
		startSegment3Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "486000.000");
		endSegment3Inner.setattrvalue("C2", "199000.000");
		endSegment3Inner.setattrvalue("C3", "5000.000");
		// 2. inner boundary
		IomObject innerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner2=polylineValueInner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		startSegmentInner2.setattrvalue("C1", "488000.000");
		startSegmentInner2.setattrvalue("C2", "201000.000");
		startSegmentInner2.setattrvalue("C3", "5000.000");
		IomObject endSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		endSegmentInner2.setattrvalue("C1", "490500.000");
		endSegmentInner2.setattrvalue("C2", "201000.000");
		endSegmentInner2.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner2=polylineValue2Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		startSegment2Inner2.setattrvalue("C1", "490500.000");
		startSegment2Inner2.setattrvalue("C2", "201000.000");
		startSegment2Inner2.setattrvalue("C3", "5000.000");
		IomObject endSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		endSegment2Inner2.setattrvalue("C1", "489500.000");
		endSegment2Inner2.setattrvalue("C2", "200000.000");
		endSegment2Inner2.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner2=polylineValue3Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		startSegment3Inner2.setattrvalue("C1", "489500.000");
		startSegment3Inner2.setattrvalue("C2", "200000.000");
		startSegment3Inner2.setattrvalue("C3", "5000.000");
		IomObject endSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		endSegment3Inner2.setattrvalue("C1", "488000.000");
		endSegment3Inner2.setattrvalue("C2", "201000.000");
		endSegment3Inner2.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}	
	
	// 1 point of innerboundary touch the outerboundary. Expected ok.
	@Test
	public void surfaceWithoutOverlaps3d_Oberlabed1PointTogether_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surfaceWithoutOverlaps3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		endSegment.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		startSegment2.setattrvalue("C3", "5000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		endSegment2.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		startSegment3.setattrvalue("C3", "5000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		endSegment3.setattrvalue("C3", "5000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "480000.000");
		startSegmentInner.setattrvalue("C2", "70000.000");
		startSegmentInner.setattrvalue("C3", "5000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "500000.000");
		endSegmentInner.setattrvalue("C2", "78000.000");
		endSegmentInner.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "500000.000");
		startSegment2Inner.setattrvalue("C2", "78000.000");
		startSegment2Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "505000.000");
		endSegment2Inner.setattrvalue("C2", "78000.000");
		endSegment2Inner.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "505000.000");
		startSegment3Inner.setattrvalue("C2", "78000.000");
		startSegment3Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "480000.000");
		endSegment3Inner.setattrvalue("C2", "70000.000");
		endSegment3Inner.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void lines2D_polylineTypeStraights_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "70000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480010.000");
		coord.setattrvalue("C2", "70000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480020.000");
		coord.setattrvalue("C2", "70000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480030.000");
		coord.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	@Test
	public void lines3D_polylineTypeStraights_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps3d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480010.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480020.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480030.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	@Test
	public void lines2D_polylineTypeArcs_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps2dArc", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "780000.000");
		coord.setattrvalue("C2", "70000.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("C1", "580000.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("A1", "480000.000");
		coord.setattrvalue("A2", "80000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	@Test
	public void lines3D_polylineTypeArcs_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps3dArc", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "780000.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("C1", "580000.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord.setattrvalue("A1", "480000.000");
		coord.setattrvalue("A2", "80000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	@Test
	public void lines2D_polylineTypeStraightsArcs_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps2dArcStraights", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "780000.000");
		coord.setattrvalue("C2", "70000.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("C1", "580000.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("A1", "490000.000");
		coord.setattrvalue("A2", "75000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "500020.000");
		coord.setattrvalue("C2", "70000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480030.000");
		coord.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	
	@Test
	public void lines3D_polylineTypeStraightsArcs_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps3dArcStraights", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "780000.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("C1", "650010.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord.setattrvalue("A1", "480010.000");
		coord.setattrvalue("A2", "80000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "600020.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "580030.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	//############################################################/
	//########## FAILING TESTS ###################################/
	//############################################################/

	// 4 edges are cut. Expected: Errs(failed to validate polygon).
	@Test
	public void surfaceWithoutOverlaps2d_Overlaps_ExpectedFailToValidatePolygon_BecauseOverlapOf2Lines_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surfaceWithoutOverlaps2d", "MULTISURFACE");
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
		endSegmentInner.setattrvalue("C2", "80000.000");
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
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("failed to validate polygon", logger.getErrs().get(0).getEventMsg());
	}
	
	// intersection of 2 innerboundary and outboundary. Expected: Errs(failed to validate polygon - intersections)
	@Test
	public void surfaceWithoutOverlaps2d_Overlaps_ExpectedIntersectionErrs_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surfaceWithoutOverlaps2d", "MULTISURFACE");
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
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("failed to validate polygon", logger.getErrs().get(0).getEventMsg());
	}
	
	// intersection of the 2 innerboundaries. Expected: failed to validate polygon - invalid ring lines.
	@Test
	public void surfaceWithoutOverlaps2d_oberlabOfInnerboundariesFail_ExpextedInvalidRingLines_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surfaceWithoutOverlaps2d", "MULTISURFACE");
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
		// 2. inner boundary
		IomObject innerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner2=polylineValueInner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		startSegmentInner2.setattrvalue("C1", "480000.000");
		startSegmentInner2.setattrvalue("C2", "70000.000");
		IomObject endSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		endSegmentInner2.setattrvalue("C1", "500000.000");
		endSegmentInner2.setattrvalue("C2", "78000.000");
		// polyline 2
		IomObject polylineValue2Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner2=polylineValue2Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		startSegment2Inner2.setattrvalue("C1", "500000.000");
		startSegment2Inner2.setattrvalue("C2", "78000.000");
		IomObject endSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		endSegment2Inner2.setattrvalue("C1", "505000.000");
		endSegment2Inner2.setattrvalue("C2", "78000.000");
		// polyline 3
		IomObject polylineValue3Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner2=polylineValue3Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		startSegment3Inner2.setattrvalue("C1", "505000.000");
		startSegment3Inner2.setattrvalue("C2", "78000.000");
		IomObject endSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		endSegment3Inner2.setattrvalue("C1", "480000.000");
		endSegment3Inner2.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("failed to validate polygon", logger.getErrs().get(0).getEventMsg());
	}
	
	// 4 edges are cut. Expected: Errs(failed to validate polygon).
	@Test
	public void surfaceWithoutOverlaps3d_Overlaps_ExpectedFailToValidatePolygon_BecauseOverlapOf2Lines_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surfaceWithoutOverlaps3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		endSegment.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		startSegment2.setattrvalue("C3", "5000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		endSegment2.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		startSegment3.setattrvalue("C3", "5000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		endSegment3.setattrvalue("C3", "5000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "480000.000");
		startSegmentInner.setattrvalue("C2", "70000.000");
		startSegmentInner.setattrvalue("C3", "5000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "500000.000");
		endSegmentInner.setattrvalue("C2", "80000.000");
		endSegmentInner.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "500000.000");
		startSegment2Inner.setattrvalue("C2", "78000.000");
		startSegment2Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "505000.000");
		endSegment2Inner.setattrvalue("C2", "78000.000");
		endSegment2Inner.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "505000.000");
		startSegment3Inner.setattrvalue("C2", "78000.000");
		startSegment3Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "480000.000");
		endSegment3Inner.setattrvalue("C2", "70000.000");
		endSegment3Inner.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("failed to validate polygon", logger.getErrs().get(0).getEventMsg());
	}
	
	// intersection of 2 innerboundary and outboundary. Expected: Errs(failed to validate polygon - intersections)
	@Test
	public void surfaceWithoutOverlaps3d_Overlaps_ExpectedIntersectionErrs_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surfaceWithoutOverlaps3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		endSegment.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		startSegment2.setattrvalue("C3", "5000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		endSegment2.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		startSegment3.setattrvalue("C3", "5000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		endSegment3.setattrvalue("C3", "5000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "480000.000");
		startSegmentInner.setattrvalue("C2", "70000.000");
		startSegmentInner.setattrvalue("C3", "5000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "485000.000");
		endSegmentInner.setattrvalue("C2", "71000.000");
		endSegmentInner.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "485000.000");
		startSegment2Inner.setattrvalue("C2", "71000.000");
		startSegment2Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "505000.000");
		endSegment2Inner.setattrvalue("C2", "78000.000");
		endSegment2Inner.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "505000.000");
		startSegment3Inner.setattrvalue("C2", "78000.000");
		startSegment3Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "480000.000");
		endSegment3Inner.setattrvalue("C2", "70000.000");
		endSegment3Inner.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("failed to validate polygon", logger.getErrs().get(0).getEventMsg());
	}
	
	// intersection of the 2 innerboundaries. Expected: failed to validate polygon - invalid ring lines.
	@Test
	public void surfaceWithoutOverlaps3d_oberlabOfInnerboundariesFail_ExpextedInvalidRingLines_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surfaceWithoutOverlaps3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		endSegment.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		startSegment2.setattrvalue("C3", "5000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		endSegment2.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		startSegment3.setattrvalue("C3", "5000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		endSegment3.setattrvalue("C3", "5000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "480000.000");
		startSegmentInner.setattrvalue("C2", "70000.000");
		startSegmentInner.setattrvalue("C3", "5000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "500000.000");
		endSegmentInner.setattrvalue("C2", "78000.000");
		endSegmentInner.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "500000.000");
		startSegment2Inner.setattrvalue("C2", "78000.000");
		startSegment2Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "505000.000");
		endSegment2Inner.setattrvalue("C2", "78000.000");
		endSegment2Inner.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "505000.000");
		startSegment3Inner.setattrvalue("C2", "78000.000");
		startSegment3Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "480000.000");
		endSegment3Inner.setattrvalue("C2", "70000.000");
		endSegment3Inner.setattrvalue("C3", "5000.000");
		// 2. inner boundary
		IomObject innerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner2=polylineValueInner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		startSegmentInner2.setattrvalue("C1", "480000.000");
		startSegmentInner2.setattrvalue("C2", "70000.000");
		startSegmentInner2.setattrvalue("C3", "5000.000");
		IomObject endSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		endSegmentInner2.setattrvalue("C1", "500000.000");
		endSegmentInner2.setattrvalue("C2", "78000.000");
		endSegmentInner2.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner2=polylineValue2Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		startSegment2Inner2.setattrvalue("C1", "500000.000");
		startSegment2Inner2.setattrvalue("C2", "78000.000");
		startSegment2Inner2.setattrvalue("C3", "5000.000");
		IomObject endSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		endSegment2Inner2.setattrvalue("C1", "505000.000");
		endSegment2Inner2.setattrvalue("C2", "78000.000");
		endSegment2Inner2.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner2=polylineValue3Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		startSegment3Inner2.setattrvalue("C1", "505000.000");
		startSegment3Inner2.setattrvalue("C2", "78000.000");
		startSegment3Inner2.setattrvalue("C3", "5000.000");
		IomObject endSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		endSegment3Inner2.setattrvalue("C1", "480000.000");
		endSegment3Inner2.setattrvalue("C2", "70000.000");
		endSegment3Inner2.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("failed to validate polygon", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void lines2D_polylineTypeStraightsLineIntersection_Fail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "70000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480010.000");
		coord.setattrvalue("C2", "70000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480010.000");
		coord.setattrvalue("C2", "70010.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480005.000");
		coord.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("Attribute lineWithoutOverlaps2d has an invalid self-intersection at (480005.0, 70000.0)", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void lines3D_polylineTypeStraightsLineIntersection_Fail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps3d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480010.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480010.000");
		coord.setattrvalue("C2", "70010.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480005.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("Attribute lineWithoutOverlaps3d has an invalid self-intersection at (480005.0, 70000.0)", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void lines2D_polylineTypeArcsLineIntersection_Fail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps2dArc", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "70000.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("A1", "482000.000");
		coord.setattrvalue("A2", "71500.000");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "73000.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("A1", "483000.000");
		coord.setattrvalue("A2", "72000.000");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "71000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("Attribute lineWithoutOverlaps2dArc has an invalid self-intersection at (480000.0, 73000.0), coord2 (481484.5360824742, 70340.20618556702)", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void lines3D_polylineTypeArcsLineIntersection_Fail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps3dArc", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("A1", "482000.000");
		coord.setattrvalue("A2", "71500.000");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "73000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("A1", "483000.000");
		coord.setattrvalue("A2", "72000.000");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "71000.000");
		coord.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("Attribute lineWithoutOverlaps3dArc has an invalid self-intersection at (480000.0, 73000.0), coord2 (481484.5360824742, 70340.20618556702)", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void lines2D_polylineTypeStraightsArcsLineIntersection_Fail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps2dArcStraights", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "72000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "486000.000");
		coord.setattrvalue("C2", "72000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "482500.000");
		coord.setattrvalue("C2", "71500.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("C1", "484500.000");
		coord.setattrvalue("C2", "71500.000");
		coord.setattrvalue("A1", "483500.000");
		coord.setattrvalue("A2", "73500.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(2,logger.getErrs().size());
		assertEquals("Attribute lineWithoutOverlaps2dArcStraights has an invalid self-intersection at (484724.74487139157, 72000.0), coord2 (482275.25512860843, 72000.0)", logger.getErrs().get(0).getEventMsg());
		assertEquals("Attribute lineWithoutOverlaps2dArcStraights has an invalid self-intersection at (482500.0, 71500.0), coord2 (484670.0, 71810.0)", logger.getErrs().get(1).getEventMsg());
	}
	
	@Test
	public void lines3D_polylineTypeStraightsArcsLineIntersection_Fail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps3dArcStraights", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "72000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "486000.000");
		coord.setattrvalue("C2", "72000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "482500.000");
		coord.setattrvalue("C2", "71500.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("C1", "484500.000");
		coord.setattrvalue("C2", "71500.000");
		coord.setattrvalue("C3", "5000.000");
		coord.setattrvalue("A1", "483500.000");
		coord.setattrvalue("A2", "73500.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(2,logger.getErrs().size());
		assertEquals("Attribute lineWithoutOverlaps3dArcStraights has an invalid self-intersection at (484724.74487139157, 72000.0), coord2 (482275.25512860843, 72000.0)", logger.getErrs().get(0).getEventMsg());
		assertEquals("Attribute lineWithoutOverlaps3dArcStraights has an invalid self-intersection at (482500.0, 71500.0), coord2 (484670.0, 71810.0)", logger.getErrs().get(1).getEventMsg());
	}
	
	// 4 edges are cut. Expected: Errs(failed to validate polygon).
	@Test
	public void area2D_ExpectedFailToValidatePolygon_BecauseOverlapOf2Lines_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
		endSegmentInner.setattrvalue("C2", "80000.000");
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
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("failed to validate polygon", logger.getErrs().get(0).getEventMsg());
	}
	
	// intersection of the 2 innerboundaries. Expected: failed to validate polygon - invalid ring lines.
	@Test
	public void area2D_oberlabOfInnerboundariesFail_ExpextedInvalidRingLines_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
		// 2. inner boundary
		IomObject innerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner2=polylineValueInner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		startSegmentInner2.setattrvalue("C1", "480000.000");
		startSegmentInner2.setattrvalue("C2", "70000.000");
		IomObject endSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		endSegmentInner2.setattrvalue("C1", "500000.000");
		endSegmentInner2.setattrvalue("C2", "78000.000");
		// polyline 2
		IomObject polylineValue2Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner2=polylineValue2Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		startSegment2Inner2.setattrvalue("C1", "500000.000");
		startSegment2Inner2.setattrvalue("C2", "78000.000");
		IomObject endSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		endSegment2Inner2.setattrvalue("C1", "505000.000");
		endSegment2Inner2.setattrvalue("C2", "78000.000");
		// polyline 3
		IomObject polylineValue3Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner2=polylineValue3Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		startSegment3Inner2.setattrvalue("C1", "505000.000");
		startSegment3Inner2.setattrvalue("C2", "78000.000");
		IomObject endSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		endSegment3Inner2.setattrvalue("C1", "480000.000");
		endSegment3Inner2.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("failed to validate polygon", logger.getErrs().get(0).getEventMsg());
	}	
	
	// 4 edges are cut. Expected: Errs(failed to validate polygon).
	@Test
	public void area3D_ExpectedFailToValidatePolygon_BecauseOverlapOf2Lines_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("areaWithoutOverlaps3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		endSegment.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		startSegment2.setattrvalue("C3", "5000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		endSegment2.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		startSegment3.setattrvalue("C3", "5000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		endSegment3.setattrvalue("C3", "5000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "480000.000");
		startSegmentInner.setattrvalue("C2", "70000.000");
		startSegmentInner.setattrvalue("C3", "5000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "500000.000");
		endSegmentInner.setattrvalue("C2", "80000.000");
		endSegmentInner.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "500000.000");
		startSegment2Inner.setattrvalue("C2", "78000.000");
		startSegment2Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "505000.000");
		endSegment2Inner.setattrvalue("C2", "78000.000");
		endSegment2Inner.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "505000.000");
		startSegment3Inner.setattrvalue("C2", "78000.000");
		startSegment3Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "480000.000");
		endSegment3Inner.setattrvalue("C2", "70000.000");
		endSegment3Inner.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("failed to validate polygon", logger.getErrs().get(0).getEventMsg());
	}
	
	// intersection of the 2 innerboundaries. Expected: failed to validate polygon - invalid ring lines.
	@Test
	public void area3D_oberlabOfInnerboundariesFail_ExpextedInvalidRingLines_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("areaWithoutOverlaps3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		endSegment.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		startSegment2.setattrvalue("C3", "5000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		endSegment2.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		startSegment3.setattrvalue("C3", "5000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		endSegment3.setattrvalue("C3", "5000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "480000.000");
		startSegmentInner.setattrvalue("C2", "70000.000");
		startSegmentInner.setattrvalue("C3", "5000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "500000.000");
		endSegmentInner.setattrvalue("C2", "78000.000");
		endSegmentInner.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "500000.000");
		startSegment2Inner.setattrvalue("C2", "78000.000");
		startSegment2Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "505000.000");
		endSegment2Inner.setattrvalue("C2", "78000.000");
		endSegment2Inner.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "505000.000");
		startSegment3Inner.setattrvalue("C2", "78000.000");
		startSegment3Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "480000.000");
		endSegment3Inner.setattrvalue("C2", "70000.000");
		endSegment3Inner.setattrvalue("C3", "5000.000");
		// 2. inner boundary
		IomObject innerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner2=polylineValueInner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		startSegmentInner2.setattrvalue("C1", "480000.000");
		startSegmentInner2.setattrvalue("C2", "70000.000");
		startSegmentInner2.setattrvalue("C3", "5000.000");
		IomObject endSegmentInner2=segmentsInner2.addattrobj("segment", "COORD");
		endSegmentInner2.setattrvalue("C1", "500000.000");
		endSegmentInner2.setattrvalue("C2", "78000.000");
		endSegmentInner2.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner2=polylineValue2Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		startSegment2Inner2.setattrvalue("C1", "500000.000");
		startSegment2Inner2.setattrvalue("C2", "78000.000");
		startSegment2Inner2.setattrvalue("C3", "5000.000");
		IomObject endSegment2Inner2=segments2Inner2.addattrobj("segment", "COORD");
		endSegment2Inner2.setattrvalue("C1", "505000.000");
		endSegment2Inner2.setattrvalue("C2", "78000.000");
		endSegment2Inner2.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3Inner2 = innerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner2=polylineValue3Inner2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		startSegment3Inner2.setattrvalue("C1", "505000.000");
		startSegment3Inner2.setattrvalue("C2", "78000.000");
		startSegment3Inner2.setattrvalue("C3", "5000.000");
		IomObject endSegment3Inner2=segments3Inner2.addattrobj("segment", "COORD");
		endSegment3Inner2.setattrvalue("C1", "480000.000");
		endSegment3Inner2.setattrvalue("C2", "70000.000");
		endSegment3Inner2.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("failed to validate polygon", logger.getErrs().get(0).getEventMsg());
	}
	
	// intersection of 2 innerboundary and outboundary.
	@Test
	public void area3D_IntersectionOfBoundaries_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("areaWithoutOverlaps3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		endSegment.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		startSegment2.setattrvalue("C3", "5000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		endSegment2.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "550000.000");
		startSegment3.setattrvalue("C2", "90000.000");
		startSegment3.setattrvalue("C3", "5000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		endSegment3.setattrvalue("C3", "5000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "480000.000");
		startSegmentInner.setattrvalue("C2", "70000.000");
		startSegmentInner.setattrvalue("C3", "5000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "485000.000");
		endSegmentInner.setattrvalue("C2", "71000.000");
		endSegmentInner.setattrvalue("C3", "5000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "485000.000");
		startSegment2Inner.setattrvalue("C2", "71000.000");
		startSegment2Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "505000.000");
		endSegment2Inner.setattrvalue("C2", "78000.000");
		endSegment2Inner.setattrvalue("C3", "5000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "505000.000");
		startSegment3Inner.setattrvalue("C2", "78000.000");
		startSegment3Inner.setattrvalue("C3", "5000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "480000.000");
		endSegment3Inner.setattrvalue("C2", "70000.000");
		endSegment3Inner.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("failed to validate AREA SurfaceOverlapTest10.Topic.FlaechenTable.areaWithoutOverlaps3d", logger.getErrs().get(1).getEventMsg());
	}
	
	// intersection of 2 innerboundary and outboundary.
	@Test
	public void area2D_IntersectionOfBoundaries_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OBJ_OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
		validator.validate(new StartBasketEvent("SurfaceOverlapTest10.Topic","b1"));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("failed to validate polygon", logger.getErrs().get(0).getEventMsg());
	}
}