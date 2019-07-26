package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;
import org.junit.Before;
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
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

public class Surface23Test {

	private TransferDescription td=null;
	// OID
	private final static String OID1 ="o1";
	private final static String OID2 ="o2";
	// MODEL.TOPIC
	private final static String ILI_TOPIC="Datatypes23.Topic";
	// CLASS
	private final static String ILI_CLASSC=ILI_TOPIC+".ClassC";
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
	
	// prueft, ob eine Surface erstellt werden kann.
	@Test
	public void surface_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface2d", "MULTISURFACE");
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
	
	// prueft, ob eine Surface, welche sich selber an 1 Segmentpunkt schneidet erstellt werden kann.
	@Test
	public void surfaceSelfCuttingOn1Point_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface2d", "MULTISURFACE");
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

	// prueft, ob eine Surface erstellt werden kann, wenn sie 2 Innerboundaries besitzt,
	// welche sich nicht ueberschneiden.
	@Test
	public void surface2Innerboundaries_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface2d", "MULTISURFACE");
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
	
	// prueft, ob eine Surface erstellt werden kann, wenn sie 2 Innerboundaries besitzt,
	// welche sich nur an einem Punkt beruehren.
	@Test
	public void twoInnerboundaries_TouchEachOtherOn1Point_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface2d", "MULTISURFACE");
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
	
	// prueft, ob eine Surface mit einem Kreisbogen erstellt werden kann.
	@Test
	public void surfaceWithArc_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
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
		// polyline
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
		// Arc
		{
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "600000.000");
			startSegment.setattrvalue("C2", "200000.000");
			IomObject arcSegment=segments.addattrobj("segment", "ARC");
			arcSegment.setattrvalue("A1", "540000.000");
			arcSegment.setattrvalue("A2", "160000.000");
			arcSegment.setattrvalue("C1", "500000.000");
			arcSegment.setattrvalue("C2", "100000.000");
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
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn
	// die outer boundary von einer Linie nicht vollstaendig geschlossen wird.
	@Test
	public void surface_BoundaryNotClosed_Dangles_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface2d", "MULTISURFACE");
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
			endSegment.setattrvalue("C1", "505000.000");
			endSegment.setattrvalue("C2", "105000.000");
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
		assertEquals(4,logger.getErrs().size());
		assertEquals("dangle tid o1", logger.getErrs().get(0).getEventMsg());
		assertEquals("dangle tid o1", logger.getErrs().get(1).getEventMsg());
		assertEquals("dangle tid o1", logger.getErrs().get(2).getEventMsg());
        assertEquals("no polygon", logger.getErrs().get(3).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn
	// die outer boundary von einem Kreisbogen nicht vollstaendig geschlossen wird.
	// die Fehlermeldung mit den Dangles soll ausgegeben werden und nicht: lineRing not closed.
	@Test
	public void surfaceWithArc_BoundaryNotClosed_Dangles_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface2d", "MULTISURFACE");
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
		// polyline 3 with Arc
		IomObject polyline3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		{
			IomObject segments=polyline3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "600000.000");
			startSegment.setattrvalue("C2", "200000.000");
			IomObject arcSegment=segments.addattrobj("segment", "ARC");
			arcSegment.setattrvalue("A1", "540000.000");
			arcSegment.setattrvalue("A2", "160000.000");
			arcSegment.setattrvalue("C1", "505000.000");
			arcSegment.setattrvalue("C2", "105000.000");
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
		assertTrue(logger.getErrs().size()==4);
		assertEquals("dangle tid o1", logger.getErrs().get(0).getEventMsg());
		assertEquals("dangle tid o1", logger.getErrs().get(1).getEventMsg());
		assertEquals("dangle tid o1", logger.getErrs().get(2).getEventMsg());
		assertEquals("no polygon", logger.getErrs().get(3).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird,
	// wenn eine Linie der InnerBoundary auf einer Linie der OuterBoundary liegt
	// und beide Boundaries vollstaendig geschlossen werden.
	@Test
	public void innerBoundaryOverlayOuterBoundaryLine_BoundariesAreClosed_Fail(){
		EhiLogger.getInstance().setTraceFilter(false);
		Iom_jObject geometricFigure=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurface=geometricFigure.addattrobj("surface2d", "MULTISURFACE");
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
		// polyline: top(left to right)
		{
			IomObject top_LeftToRight = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=top_LeftToRight.addattrobj("sequence", "SEGMENTS");
			IomObject segLeft=segments.addattrobj("segment", "COORD");
			segLeft.setattrvalue("C1", "500000.000");
			segLeft.setattrvalue("C2", "300000.000");
			IomObject segRight=segments.addattrobj("segment", "COORD");
			segRight.setattrvalue("C1", "700000.000");
			segRight.setattrvalue("C2", "300000.000");
		}
		// polyline: right(top to bottom)
		{
			IomObject right_TopToBottom = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=right_TopToBottom.addattrobj("sequence", "SEGMENTS");
			IomObject segTop=segments.addattrobj("segment", "COORD");
			segTop.setattrvalue("C1", "700000.000");
			segTop.setattrvalue("C2", "300000.000");
			IomObject segBottom=segments.addattrobj("segment", "COORD");
			segBottom.setattrvalue("C1", "700000.000");
			segBottom.setattrvalue("C2", "100000.000");
		}
		// polyline: bottom(right to left)
		{
			IomObject bottom_RightToLeft = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=bottom_RightToLeft.addattrobj("sequence", "SEGMENTS");
			IomObject segRight=segments.addattrobj("segment", "COORD");
			segRight.setattrvalue("C1", "700000.000");
			segRight.setattrvalue("C2", "100000.000");
			IomObject segLeft=segments.addattrobj("segment", "COORD");
			segLeft.setattrvalue("C1", "500000.000");
			segLeft.setattrvalue("C2", "100000.000");
		}
		// inner boundary
		IomObject innerBoundary = surface.addattrobj("boundary", "BOUNDARY");
		// inner boundary polyline: cross(center to leftTop)
		{
			IomObject inner_Cross_CenterToLeftTop = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Cross_CenterToLeftTop.addattrobj("sequence", "SEGMENTS");
			IomObject innerSegCenter=segments.addattrobj("segment", "COORD");
			innerSegCenter.setattrvalue("C1", "600000.000");
			innerSegCenter.setattrvalue("C2", "200000.000");
			IomObject innerSegLeftTop=segments.addattrobj("segment", "COORD");
			innerSegLeftTop.setattrvalue("C1", "500000.000");
			innerSegLeftTop.setattrvalue("C2", "300000.000");
		}
		// inner boundary polyline: left(top to bottom)
		{
			IomObject inner_Left_TopToBottom = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Left_TopToBottom.addattrobj("sequence", "SEGMENTS");
			IomObject innerSegTop=segments.addattrobj("segment", "COORD");
			innerSegTop.setattrvalue("C1", "500000.000");
			innerSegTop.setattrvalue("C2", "300000.000");
			IomObject innerSegBottom=segments.addattrobj("segment", "COORD");
			innerSegBottom.setattrvalue("C1", "500000.000");
			innerSegBottom.setattrvalue("C2", "100000.000");
		}
		// inner boundary polyline: cross(leftBottom to center)
		{
			IomObject inner_Cross_TopLeftToCenter = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Cross_TopLeftToCenter.addattrobj("sequence", "SEGMENTS");
			IomObject innerSegTopLeft=segments.addattrobj("segment", "COORD");
			innerSegTopLeft.setattrvalue("C1", "500000.000");
			innerSegTopLeft.setattrvalue("C2", "100000.000");
			IomObject innerSegCenter=segments.addattrobj("segment", "COORD");
			innerSegCenter.setattrvalue("C1", "600000.000");
			innerSegCenter.setattrvalue("C2", "200000.000");
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
		assertEquals(1,logger.getErrs().size());
		assertEquals("Overlay coord1 (500000.000, 100000.000), coord2 (500000.000, 300000.000), tids o1, o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft ob eine Overlay Fehlermeldung ausgegeben wird,
	// wenn eine Linie der InnerBoundary auf einer Linie der OuterBoundary liegt
	// und die InnerBoundary nicht vollstaendig geschlossen ist.
	@Test
	public void innerBoundary_Overlay_OuterBoundaryLine_AndIsNotClosed_Fail(){
		//EhiLogger.getInstance().setTraceFilter(false);
		Iom_jObject geometricFigure=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurface=geometricFigure.addattrobj("surface2d", "MULTISURFACE");
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
		// polyline: top(left to right)
		{
			IomObject top_LeftToRight = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=top_LeftToRight.addattrobj("sequence", "SEGMENTS");
			IomObject segLeft=segments.addattrobj("segment", "COORD");
			segLeft.setattrvalue("C1", "500000.000");
			segLeft.setattrvalue("C2", "300000.000");
			IomObject segRight=segments.addattrobj("segment", "COORD");
			segRight.setattrvalue("C1", "700000.000");
			segRight.setattrvalue("C2", "300000.000");
		}
		// polyline: right(top to bottom)
		{
			IomObject right_TopToBottom = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=right_TopToBottom.addattrobj("sequence", "SEGMENTS");
			IomObject segTop=segments.addattrobj("segment", "COORD");
			segTop.setattrvalue("C1", "700000.000");
			segTop.setattrvalue("C2", "300000.000");
			IomObject segBottom=segments.addattrobj("segment", "COORD");
			segBottom.setattrvalue("C1", "700000.000");
			segBottom.setattrvalue("C2", "100000.000");
		}
		// polyline: bottom(right to left)
		{
			IomObject bottom_RightToLeft = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=bottom_RightToLeft.addattrobj("sequence", "SEGMENTS");
			IomObject segRight=segments.addattrobj("segment", "COORD");
			segRight.setattrvalue("C1", "700000.000");
			segRight.setattrvalue("C2", "100000.000");
			IomObject segLeft=segments.addattrobj("segment", "COORD");
			segLeft.setattrvalue("C1", "500000.000");
			segLeft.setattrvalue("C2", "100000.000");
		}
		// inner boundary
		IomObject innerBoundary = surface.addattrobj("boundary", "BOUNDARY");
		// inner boundary polyline: cross(center to leftTop)
		{
			IomObject inner_Cross_CenterToLeftTop = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Cross_CenterToLeftTop.addattrobj("sequence", "SEGMENTS");
			IomObject innerSegCenter=segments.addattrobj("segment", "COORD");
			innerSegCenter.setattrvalue("C1", "600000.000");
			innerSegCenter.setattrvalue("C2", "200000.000");
			IomObject innerSegLeftTop=segments.addattrobj("segment", "COORD");
			innerSegLeftTop.setattrvalue("C1", "500000.000");
			innerSegLeftTop.setattrvalue("C2", "300000.000");
		}
		// inner boundary polyline: left(top to bottom)
		{
			IomObject inner_Left_TopToBottom = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Left_TopToBottom.addattrobj("sequence", "SEGMENTS");
			IomObject innerSegTop=segments.addattrobj("segment", "COORD");
			innerSegTop.setattrvalue("C1", "500000.000");
			innerSegTop.setattrvalue("C2", "300000.000");
			IomObject innerSegBottom=segments.addattrobj("segment", "COORD");
			innerSegBottom.setattrvalue("C1", "500000.000");
			innerSegBottom.setattrvalue("C2", "100000.000");
		}
		// inner boundary polyline: cross(leftBottom to center)
		{
			IomObject inner_Cross_TopLeftToCenter = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Cross_TopLeftToCenter.addattrobj("sequence", "SEGMENTS");
			IomObject innerSegTopLeft=segments.addattrobj("segment", "COORD");
			innerSegTopLeft.setattrvalue("C1", "500000.000");
			innerSegTopLeft.setattrvalue("C2", "100000.000");
			IomObject innerSegCenter=segments.addattrobj("segment", "COORD");
			innerSegCenter.setattrvalue("C1", "600000.000");
			innerSegCenter.setattrvalue("C2", "190000.000");
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
		assertEquals(1,logger.getErrs().size());
		assertEquals("Overlay coord1 (500000.000, 100000.000), coord2 (500000.000, 300000.000), tids o1, o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn
	// eine Linie der Innerboundary auf einer Linie der Outerboundary liegt.
	// Das Segment der Innerboundary Linie, welches die Outerboundary Linie ueberschneidet,
	// unterscheidet sich zum Outerboundary Segment, nur ueber die Y Koordinate.
	@Test
	public void overlayOf2Lines_InnerLineYCoordToLow_Fail(){
		EhiLogger.getInstance().setTraceFilter(false);
		Iom_jObject geometricFigure=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurface=geometricFigure.addattrobj("surface2d", "MULTISURFACE");
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
	// eine Linie der Innerboundary auf einer Linie der Outerboundary liegt.
	// Das Segment der Innerboundary Linie, welches die Outerboundary Linie ueberschneidet,
	// unterscheidet sich zum Outerboundary Segment, nur ueber die X Koordinate.
	@Test
	public void overlayOf2Lines_InnerLineXCoordToHigh_Fail(){
		EhiLogger.getInstance().setTraceFilter(false);
		Iom_jObject geometricFigure=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurface=geometricFigure.addattrobj("surface2d", "MULTISURFACE");
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
		// polyline: top(left to right)
		{
			IomObject top_LeftToRight = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=top_LeftToRight.addattrobj("sequence", "SEGMENTS");
			IomObject segLeft=segments.addattrobj("segment", "COORD");
			segLeft.setattrvalue("C1", "500000.000");
			segLeft.setattrvalue("C2", "300000.000");
			IomObject segRight=segments.addattrobj("segment", "COORD");
			segRight.setattrvalue("C1", "700000.000");
			segRight.setattrvalue("C2", "300000.000");
		}
		// polyline: right(top to bottom)
		{
			IomObject right_TopToBottom = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=right_TopToBottom.addattrobj("sequence", "SEGMENTS");
			IomObject segTop=segments.addattrobj("segment", "COORD");
			segTop.setattrvalue("C1", "700000.000");
			segTop.setattrvalue("C2", "300000.000");
			IomObject segBottom=segments.addattrobj("segment", "COORD");
			segBottom.setattrvalue("C1", "700000.000");
			segBottom.setattrvalue("C2", "100000.000");
		}
		// polyline: bottom(right to left)
		{
			IomObject bottom_RightToLeft = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=bottom_RightToLeft.addattrobj("sequence", "SEGMENTS");
			IomObject segRight=segments.addattrobj("segment", "COORD");
			segRight.setattrvalue("C1", "700000.000");
			segRight.setattrvalue("C2", "100000.000");
			IomObject segLeft=segments.addattrobj("segment", "COORD");
			segLeft.setattrvalue("C1", "500000.000");
			segLeft.setattrvalue("C2", "100000.000");
		}
		// inner boundary
		IomObject innerBoundary = surface.addattrobj("boundary", "BOUNDARY");
		// inner boundary polyline: cross(center to leftTop)
		{
			IomObject inner_Cross_CenterToLeftTop = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Cross_CenterToLeftTop.addattrobj("sequence", "SEGMENTS");
			IomObject innerSegCenter=segments.addattrobj("segment", "COORD");
			innerSegCenter.setattrvalue("C1", "600000.000");
			innerSegCenter.setattrvalue("C2", "200000.000");
			IomObject innerSegLeftTop=segments.addattrobj("segment", "COORD");
			innerSegLeftTop.setattrvalue("C1", "510000.000");
			innerSegLeftTop.setattrvalue("C2", "300000.000");
		}
		// inner boundary polyline: left(top to bottom)
		{
			IomObject inner_Left_TopToBottom = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Left_TopToBottom.addattrobj("sequence", "SEGMENTS");
			IomObject innerSegTop=segments.addattrobj("segment", "COORD");
			innerSegTop.setattrvalue("C1", "500000.000");
			innerSegTop.setattrvalue("C2", "300000.000");
			IomObject innerSegBottom=segments.addattrobj("segment", "COORD");
			innerSegBottom.setattrvalue("C1", "500000.000");
			innerSegBottom.setattrvalue("C2", "100000.000");
		}
		// inner boundary polyline: cross(leftBottom to center)
		{
			IomObject inner_Cross_TopLeftToCenter = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Cross_TopLeftToCenter.addattrobj("sequence", "SEGMENTS");
			IomObject innerSegTopLeft=segments.addattrobj("segment", "COORD");
			innerSegTopLeft.setattrvalue("C1", "500000.000");
			innerSegTopLeft.setattrvalue("C2", "100000.000");
			IomObject innerSegCenter=segments.addattrobj("segment", "COORD");
			innerSegCenter.setattrvalue("C1", "600000.000");
			innerSegCenter.setattrvalue("C2", "200000.000");
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
		assertEquals("Overlay coord1 (500000.000, 100000.000), coord2 (500000.000, 300000.000), tids o1, o1", logger.getErrs().get(0).getEventMsg());
		assertEquals("Intersection coord1 (510000.000, 300000.000), tids o1, o1", logger.getErrs().get(1).getEventMsg());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn
	// eine Linie der Innerboundary auf einer Linie der Outerboundary liegt.
	// Das Segment der Innerboundary Linie, welches die Outerboundary Linie ueberschneidet,
	// unterscheidet sich zum Outerboundary Segment, ueber die X und Y Koordinate.
	@Test
	public void overlayOf2Lines_InnerLineDiffXAndYCoords_Fail(){
		EhiLogger.getInstance().setTraceFilter(false);
		Iom_jObject geometricFigure=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurface=geometricFigure.addattrobj("surface2d", "MULTISURFACE");
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
			innerSegTop.setattrvalue("C2", "300000.000");
		}
		// inner boundary polyline: cross(leftTop to center)
		{
			IomObject inner_Cross_LeftTopToCenter = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Cross_LeftTopToCenter.addattrobj("sequence", "SEGMENTS");
			IomObject innerSegTopLeft=segments.addattrobj("segment", "COORD");
			innerSegTopLeft.setattrvalue("C1", "510000.000");
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
		assertEquals(1,logger.getErrs().size());
		assertEquals("Overlay coord1 (500000.000, 100000.000), coord2 (500000.000, 300000.000), tids o1, o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird,
	// wenn der End-Segment-Punkt einer geraden linie der Innerboundary zu weit unterhalb der Node liegt, 
	// Beide Segmente teilen die selben Anfangspunkte.
	@Test
	public void lineOfInnerBoundary_HasWrongEndSegment_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface2d", "MULTISURFACE");
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
		startSegment2Inner.setattrvalue("C2", "80000.000");
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
		assertEquals("dangle tid o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob eine Intersection Fehlermeldung ausgegeben wird,
	// wenn 2 Innerboundaries einander Ueberschneiden.
	@Test
	public void twoInnerboundaryLines_IntersectEachOther_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface2d", "MULTISURFACE");
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
	
	// Es soll getestet werden, ob eine Intersection Fehlermeldung ausgegeben wird,
	// wenn die Outerboundary die Innerboundary Ueberschneidet.
	@Test
	public void innerboundary_IntersectOuterboundary_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface2d", "MULTISURFACE");
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
	
	// prueft, ob eine Surface erstellt werden kann, wenn
	// der Typ der Surface nicht vom Typ Multisurface ist.
	@Test
	public void invalidSurfaceType_Fail(){
		Iom_jObject objNotMultisurface=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject surfaceValue=objNotMultisurface.addattrobj("surface2d", "SURFACE");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objNotMultisurface));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("unexpected Type SURFACE; MULTISURFACE expected", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob eine Surface erstellt werden kann, wenn
	// die Surface zwar Komplett ist, jedoch 2 Surfaces erstellt wurden.
	@Test
	public void completeWith2Surfaces_Fail(){
		Iom_jObject objCompleteMultisurface=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurfaceValue=objCompleteMultisurface.addattrobj("surface2d", "MULTISURFACE");
		multisurfaceValue.setobjectconsistency(IomConstants.IOM_COMPLETE);
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		IomObject surfaceValue2 = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objCompleteMultisurface));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid number of surfaces in COMPLETE basket", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob eine Surface erstellt werden kann,
	// wenn sie 2d ist und in 3d erstellt wird.
	@Test
	public void surface2dWith3dImplementation_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface2d", "MULTISURFACE");
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
	
	// prueft, ob eine Surface erstellt werden kann,
	// wenn sie 3d ist und in 2d erstellt wird.
	@Test
	public void surface3dWith2dImplementation_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface3d", "MULTISURFACE");
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
	
	// prueft, ob eine Surface erstellt werden kann, wenn
	// das Startsegment der ersten Polyline eine ungueltige Zahl ist.
	@Test
	public void surfaceInvalidValueRange_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface3d", "MULTISURFACE");
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
		assertEquals("value 4800000.000 is out of range in attribute surface3d", logger.getErrs().get(0).getEventMsg());
		assertEquals("value 700000.000 is out of range in attribute surface3d", logger.getErrs().get(1).getEventMsg());
		assertEquals("value 10000.000 is out of range in attribute surface3d", logger.getErrs().get(2).getEventMsg());
	}
	
	// prueft, ob 2 Polygone erstellt werden koennen, wenn 2 Polygone mit je einem Arc,
	// welche uebereinander liegen und sich an den gleichen Punkten beruehren, erstellt werden koennen.
	@Test
	public void twoPolygon_WithArcsLieExactlyOnEachOther_Ok(){
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		{
			IomObject multisurfaceValue=objAreaSuccess.addattrobj("surface3d", "MULTISURFACE");
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
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSC, OID2);
		{
			IomObject multisurfaceValue=objAreaSuccess2.addattrobj("surface3d", "MULTISURFACE");
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
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objAreaSuccess));
		validator.validate(new ObjectEvent(objAreaSuccess2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft, ob 2 Polygone erstellt werden koennen, wenn 2 Polygone mit je einem Arc,
	// welche uebereinander liegen und sich nicht an den gleichen Punkten beruehren, erstellt werden koennen.
	@Test
	public void twoPolygon_2ArcsLieNotExactlyOnEachOther_Ok(){
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		{
			IomObject multisurfaceValue=objAreaSuccess.addattrobj("surface3d", "MULTISURFACE");
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
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSC, OID2);
		{
			IomObject multisurfaceValue=objAreaSuccess2.addattrobj("surface3d", "MULTISURFACE");
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
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objAreaSuccess));
		validator.validate(new ObjectEvent(objAreaSuccess2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft, ob eine Surface erstellt werden kann, wenn
	// das Startsegment des ersten Kreisbogens eine ungueltige Zahl ist.
	@Test
	public void surfaceWithARCInvalidValueRange_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface3d", "MULTISURFACE");
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
		assertEquals("value 4800000.000 is out of range in attribute surface3d", logger.getErrs().get(0).getEventMsg());
		assertEquals("value 700000.000 is out of range in attribute surface3d", logger.getErrs().get(1).getEventMsg());
	}
	
	// prueft, ob die Fehlermeldung ausgegeben wird, wenn die outerboundary nicht existiert.
	@Test
	public void missingOuterBoundaryInSurface_False(){
		// external object
		Iom_jObject externalObject=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject externalMultisurface=externalObject.addattrobj("surface2d", "MULTISURFACE");
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
		assertEquals("missing outerboundary in surface2d of object o1.", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft ob die folgende Situation erstellt werden kann.
	// Es wird ein aeusseres Objekt (externalObject) mit einem aeusseren und einem inneren Rand erstellt.
	// Danach wird ein zweites, inneres Objekt (internalObject) mit nur einem aeusseren Rand erstellt 
	// (innerhalb des inneren Randes des aeusseren Objekts).
	// Das innere Objekt beruehrt den inneren Rand des aeusseren Objektes auf einer Strecke.
	@Test
	public void twoPolygon_Polygon2InsideInnerBoundaryOfPolygon1_OverlayOn1Line_Ok(){
		// external object
		Iom_jObject externalObject=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject externalMultisurface=externalObject.addattrobj("surface2d", "MULTISURFACE");
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
		Iom_jObject internalObject=new Iom_jObject(ILI_CLASSC, OID2);
		IomObject internalMultisurface=internalObject.addattrobj("surface2d", "MULTISURFACE");
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
}