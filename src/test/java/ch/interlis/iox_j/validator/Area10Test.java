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

public class Area10Test {

	private TransferDescription td=null;
	// OID
	private final static String OID1 ="o1";
	private final static String OID2 ="o2";
	// MODEL.TOPIC
	private final static String ILI_TOPIC="Datatypes10.Topic";
	// CLASS
	private final static String ILI_CLASSFLAECHENTABLE=ILI_TOPIC+".FlaechenTable";
	// START BASKET EVENT
	private final static String BID="b1";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili1cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/Datatypes10.ili", FileEntryKind.ILIMODELFILE);
		ili1cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili1cConfig);
		assertNotNull(td);
	}

	//############################################################/
	//########## SUCCESSFUL TESTS ################################/
	//############################################################/
	
	// Es wird getestet, ob ein Bereich erstellt werden kann, wenn 1 Punkt des Innerboundary den Outerboundary ber�hrt.
	@Test
	public void area2dInnerTouchesOuterboundaryOn1Point_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Es wird getestet, ob ein Bereich erstellt werden kann, wenn er 2 Innerboundaries hat, welche sich nicht ber�hren.
	@Test
	public void area2dWith2Innerboundaries_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Es wird getestet, ob ein Bereich erstellt werden kann, wenn der Innerboundary den Outerboundary an einem Punkt ber�hrt.
	@Test
	public void area3dInnerTouchesOuterboundaryOn1Point_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Es wird getestet, ob ein Bereich erstellt werden kann, wenn die 2 Bereiche sich auf einer Linie an drei Punkten ber�hren.
	// Senkrecht: x--------x--------x  Beide Punkte liegen aufeinander und habe beide dieselben Segmentpunkte.
	@Test
	public void area2dIntersectIn1LineOnSame3Points_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
		Iom_jObject objSurfaceSuccess2=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID2);
		IomObject multisurfaceValue2=objSurfaceSuccess2.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
		endSegment2Inner.setattrvalue("C2", "150000.000");
		// polyline 3
		IomObject polylineValue5 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments5=polylineValue5.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment5=segments5.addattrobj("segment", "COORD");
		startSegment5.setattrvalue("C1", "540000.000");
		startSegment5.setattrvalue("C2", "150000.000");
		IomObject endSegment5=segments5.addattrobj("segment", "COORD");
		endSegment5.setattrvalue("C1", "540000.000");
		endSegment5.setattrvalue("C2", "200000.000");
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
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob ein Bereich erstellt werden kann, wenn die 2 Bereiche aus einem Kreisbogen sich auf einer Linie an den gleichen Punkten ber�hren.
	// x--------x--------x  Beide Kreisboegen liegen aufeinander und haben beide dieselben Segmentpunkte.
	@Test
	public void area3dArcsBothAllSegmentPointsSame_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
		{
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("areaWithoutOverlaps3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "480001.000");
		endSegment.setattrvalue("C2", "70001.000");
		endSegment.setattrvalue("C3", "5000.000");
		// Arc
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "480000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "480000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		arcSegment.setattrvalue("C3", "5000.000");
		}
		Iom_jObject objSurfaceSuccess2=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID2);
		{
		IomObject multisurfaceValue=objSurfaceSuccess2.addattrobj("areaWithoutOverlaps3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "480001.000");
		endSegment.setattrvalue("C2", "70001.000");
		endSegment.setattrvalue("C3", "5000.000");
		// Arc
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "480000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "480000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		arcSegment.setattrvalue("C3", "5000.000");
		}
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
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob ein Bereich erstellt werden kann, wenn die 2 Bereiche genau aufeinander liegen.
	// x--------x--------x  Alle Punkte liegen aufeinander.
	@Test
	public void area2dBothAreasSamePoints_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
		Iom_jObject objSurfaceSuccess2=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID2);
		IomObject multisurfaceValue2=objSurfaceSuccess2.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "500000.000");
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
		endSegment2Inner.setattrvalue("C2", "150000.000");
		// polyline 3
		IomObject polylineValue5 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments5=polylineValue5.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment5=segments5.addattrobj("segment", "COORD");
		startSegment5.setattrvalue("C1", "540000.000");
		startSegment5.setattrvalue("C2", "150000.000");
		IomObject endSegment5=segments5.addattrobj("segment", "COORD");
		endSegment5.setattrvalue("C1", "540000.000");
		endSegment5.setattrvalue("C2", "200000.000");
		// polyline 4
		IomObject polylineValue3Inner = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "540000.000");
		startSegment3Inner.setattrvalue("C2", "200000.000");
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
		validator.validate(new ObjectEvent(objSurfaceSuccess2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob ein Bereich erstellt werden kann, wenn sich die 2 Innerboundaries nicht ber�hren.
	@Test
	public void area3dWith2Innerboundaries_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob ein Bereich erstellt werden kann, wenn der Innerboundary die Outerboundary an einem Punkt ber�hrt. 
	@Test
	public void area3dInnerboundaryTouchesOuterboundaryOn1Point_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob ein Bereich erstellt werden kann, wenn die Linien des Bereiches sich �berschneiden.
	@Test
	public void area2dOverlapOf2Lines_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("failed to validate polygon", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob ein Bereich erstellt werden kann, wenn sich die 2 Innerboundaries �berschneiden.
	@Test
	public void area2dInnerboundariesOverlapEachOther_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("failed to validate polygon", logger.getErrs().get(0).getEventMsg());
	}	
	
	// Es wird getestet, ob ein Bereich erstellt werden kann, wenn sich 2 Linien an 4 Ecken �berschneiden.
	@Test
	public void area3dOverlapOf2Lines_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("failed to validate polygon", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob ein Bereich erstellt werden kann, wenn sich 2 Innerboundaries des Bereiches �berschneiden.
	@Test
	public void area3dIntersectionOfInnerboundaries_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("failed to validate polygon", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob ein Bereich erstellt werden kann, wenn sich die Innerboundary mit der Outerboundary �berschneidet.
	@Test
	public void area3dInnerboundaryIntersectOuterboundary_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("failed to validate polygon", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob ein 2d Bereich erstellt werden kann, wenn der Innerboundary die Outerboundary �berschneidet.
	@Test
	public void area2dInnerboundaryIntersectOuterboundary_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("failed to validate polygon", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob ein Bereich erstellt werden kann, wenn die 2 Bereiche sich auf einer Linie an drei Punkten ber�hren.
	// area1: x--------x--------x
	// area2: x-----------------x
	// Beide Areas liegen aufeinander, area2 hat keinen Mittelpunkt.
	@Test
	public void area2dIntersectOn2Points_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
		Iom_jObject objSurfaceSuccess2=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID2);
		IomObject multisurfaceValue2=objSurfaceSuccess2.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("failed to validate AREA Datatypes10.Topic.FlaechenTable.areaWithoutOverlaps2d", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob ein Bereich erstellt werden kann, wenn die 2 Bereiche aus einem Kreisbogen sich auf einer Linie an 1 Punkt nicht ber�hren.
	// x--------x--------x  Beide Kreisboegen liegen aufeinander und unterscheiden sich nur von 1 Segment der 2.ten Area.
	@Test
	public void area3d1AreaArcPointOther_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
		{
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("areaWithoutOverlaps3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "480001.000");
		endSegment.setattrvalue("C2", "70001.000");
		endSegment.setattrvalue("C3", "5000.000");
		// Arc
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "480000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "480000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		arcSegment.setattrvalue("C3", "5000.000");
		}
		Iom_jObject objSurfaceSuccess2=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID2);
		{
		IomObject multisurfaceValue=objSurfaceSuccess2.addattrobj("areaWithoutOverlaps3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500001.000");
		endSegment.setattrvalue("C2", "70001.000");
		endSegment.setattrvalue("C3", "5000.000");
		// Arc
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "480000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "480000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		arcSegment.setattrvalue("C3", "5000.000");
		}
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("failed to validate AREA Datatypes10.Topic.FlaechenTable.areaWithoutOverlaps3d", logger.getErrs().get(0).getEventMsg());
	}
}