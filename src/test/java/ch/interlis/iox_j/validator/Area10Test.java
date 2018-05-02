package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
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
	
	// Es wird getestet ob eine Area fehlerfrei erstellt werden kann.
	@Test
	public void area_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
		IomObject endSegment2=segments.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		IomObject endSegment3=segments.addattrobj("segment", "COORD");
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
	
	// prueft, dass der Validator auch mit Linientabellen (wie in der ITF-Datei vorhanden) umgehen kann
	@Test
	public void areaLineTable_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
		IomObject georef=objSurfaceSuccess.addattrobj("areaWithoutOverlaps2d", "COORD");
		georef.setattrvalue("C1", "480005.000");
		georef.setattrvalue("C2", "70005.000");
		IomObject objPolyline = new Iom_jObject(ILI_CLASSFLAECHENTABLE+"_areaWithoutOverlaps2d", OID1);
		// polyline
		IomObject polylineValue = objPolyline.addattrobj("_itf_geom_FlaechenTable", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		IomObject endSegment2=segments.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		IomObject endSegment3=segments.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_DO_ITF_LINETABLES, Validator.CONFIG_DO_ITF_LINETABLES_DO);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objPolyline));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft ob ein Polygon erstellt werden kann, wenn 1 Punkt der Innerboundary den Outerboundary beruehrt.
	@Test
	public void areaInnerTouchesOuterboundaryOn1Point_Ok(){
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

	// prueft ob ein Polygon, bestehend aus 2 Innerboundaries welche sich nicht beruehren und einer Outerboundary, erstellt werden kann.
	@Test
	public void areaWith2Innerboundaries_Ok(){
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

	// prueft ob 2 Polygone erstellt werden koennen, wenn sie sich auf einer Linie an drei Punkten beruehren.
	// Beide Linien liegen aufeinander und haben beide dieselben Segmentpunkte.
	@Ignore("expected: intersection of 2 lines, actual: Intersection overlap of these lines.")
	@Test
	public void areaTwoLinesOnEachOtherOnSame3Points_Ok(){
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
		IomObject multisurfaceValue=objAreaSuccess.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID2);
		IomObject multisurfaceValue2=objAreaSuccess2.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
		validator.validate(new ObjectEvent(objAreaSuccess));
		validator.validate(new ObjectEvent(objAreaSuccess2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob ein Polygon erstellt werden kann, wenn die 2 Polygone aus einem Kreisbogen sich auf einer Linie an den gleichen Punkten beruehren.
	// Beide Kreisboegen liegen aufeinander und haben beide dieselben Segmentpunkte.
	@Ignore("expected: 3 intersections, actual: 3 intersection overlap null.")
	@Test
	public void areaTwoArcsOnEachOtherOnSamePoints_Fail(){
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
		{
			IomObject multisurfaceValue=objAreaSuccess.addattrobj("areaWithoutOverlaps3d", "MULTISURFACE");
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
			// polyline
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
			// arc
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue3.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "200000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject arcSegment=segments.addattrobj("segment", "ARC");
				arcSegment.setattrvalue("A1", "550000.000");
				arcSegment.setattrvalue("A2", "150000.000");
				arcSegment.setattrvalue("C1", "500000.000");
				arcSegment.setattrvalue("C2", "100000.000");
				arcSegment.setattrvalue("C3", "5000.000");
			}
		}
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID2);
		{
			IomObject multisurfaceValue=objAreaSuccess2.addattrobj("areaWithoutOverlaps3d", "MULTISURFACE");
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
			// polyline
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
			// arc
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			{
				IomObject segments=polylineValue3.addattrobj("sequence", "SEGMENTS");
				IomObject startSegment=segments.addattrobj("segment", "COORD");
				startSegment.setattrvalue("C1", "600000.000");
				startSegment.setattrvalue("C2", "200000.000");
				startSegment.setattrvalue("C3", "5000.000");
				IomObject arcSegment=segments.addattrobj("segment", "ARC");
				arcSegment.setattrvalue("A1", "550000.000");
				arcSegment.setattrvalue("A2", "150000.000");
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
		// Asserts
		assertTrue(logger.getErrs().size()==4);
		assertEquals("intersection tids o1, o1", logger.getErrs().get(0).getEventMsg());
		assertEquals("intersection tids o1, o1", logger.getErrs().get(1).getEventMsg());
		assertEquals("intersection tids o1, o1", logger.getErrs().get(2).getEventMsg());
		assertEquals("failed to validate AREA Datatypes10.Topic.FlaechenTable.areaWithoutOverlaps3d", logger.getErrs().get(3).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn 2 Polygone
	// (mit je 4 Linien) genau aufeinander liegen.
	@Ignore("expected: 4 intersections, actual: 4 intersection overlap null.")
	@Test
	public void areaBothAreasOnEachOtherWithSamePoints_Fail(){
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
		assertTrue(logger.getErrs().size()==5);
		assertEquals("intersection tids o1, o1", logger.getErrs().get(0).getEventMsg());
		assertEquals("intersection tids o1, o1", logger.getErrs().get(1).getEventMsg());
		assertEquals("intersection tids o1, o1", logger.getErrs().get(2).getEventMsg());
		assertEquals("intersection tids o1, o1", logger.getErrs().get(3).getEventMsg());
		assertEquals("failed to validate AREA Datatypes10.Topic.FlaechenTable.areaWithoutOverlaps2d", logger.getErrs().get(4).getEventMsg());
	}
	
	// Es wird getestet, ob eine Polygon mit einem Kreisbogen erstellt werden kann.
	@Test
	public void areaWithArc_Ok(){
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
		IomObject multisurfaceValue=objAreaSuccess.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
		validator.validate(new ObjectEvent(objAreaSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Polygon mit einem Kreisbogen erstellt werden kann,
	// wenn die Ring Lines unmoeglich stimmen koennen.
	@Ignore("java.lang.IllegalArgumentException: Points of LinearRing do not form a closed linestring")
	@Test
	public void areaWithArc_InvalidRingLines_Fail(){
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
		IomObject multisurfaceValue=objAreaSuccess.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
		arcSegment.setattrvalue("C1", "480010.000");
		arcSegment.setattrvalue("C2", "70010.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objAreaSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==3);
		assertEquals("invald ring line", logger.getErrs().get(0).getEventMsg());
		assertEquals("invald ring line", logger.getErrs().get(1).getEventMsg());
		assertEquals("failed to validate polygon", logger.getErrs().get(2).getEventMsg());
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird,
	// wenn auf einem "geraden Segment" der OuterBoundary, ein "gerades Segment" der InnerBoundary liegt.
	// Beide Segmente teilen die selben Anfangs und Endpunkte.
	@Ignore("expected: 2 messages, actual: 2 different messages: 2 intersection tid messages, 2 intersection overlap messages")
	@Test
	public void lineOfInnerBoundaryLiesOverLineOfOuterBoundary_Fail(){
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
		assertTrue(logger.getErrs().size()==4);
		assertEquals("intersection tids o1, o1", logger.getErrs().get(0).getEventMsg());
		assertEquals("intersection tids o1, o1", logger.getErrs().get(1).getEventMsg());
		assertEquals("failed to validate polygon", logger.getErrs().get(2).getEventMsg());
		assertEquals("failed to validate AREA Datatypes10.Topic.FlaechenTable.areaWithoutOverlaps2d", logger.getErrs().get(3).getEventMsg());
	}
	
	// Es soll getestet werden, ob eine Intersection Fehlermeldung ausgegeben wird,
	// wenn sich 2 gerade Linien einer innerboundary, mit 2 geraden Linien einer anderen innerboundary ueberschneiden.
	//@Ignore("expected: 2 messages, actual: 2 different messages: 2 intersection tid messages, 2 intersection overlap messages")
	@Test
	public void twoInnerboundaryLinesCutEachOther_Fail(){
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
		assertTrue(logger.getErrs().size()==4);
		assertEquals("intersection tids o1, o1", logger.getErrs().get(0).getEventMsg());
		assertEquals("intersection tids o1, o1", logger.getErrs().get(1).getEventMsg());
		assertEquals("failed to validate polygon", logger.getErrs().get(2).getEventMsg());
		assertEquals("failed to validate AREA Datatypes10.Topic.FlaechenTable.areaWithoutOverlaps2d", logger.getErrs().get(3).getEventMsg());
	}	
	
	// Es wird getestet, ob ein Polygon erstellt werden kann, wenn die 2 Polygone sich auf einer Linie an drei Punkten beruehren.
	// area1: x--------x--------x
	// area2: x-----------------x
	// Beide Areas liegen aufeinander, area2 hat keinen Mittelpunkt.
	@Test
	public void areaIntersectOn2Points_Fail(){
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
	
	// prueft ob 2 Polygone, mit je einem Kreisbogen erstellt werden koennen.
	// beide Polygone liegen bis auf einen Segmentpunkt exakt aufeinander.
	@Test
	public void twoArcPointsNotExactlyOnEachOther_Fail(){
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
		{
			IomObject multisurfaceValue=objAreaSuccess.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
		}
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID2);
		{
			IomObject multisurfaceValue=objAreaSuccess2.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
				arcSegment.setattrvalue("A1", "540005.000");
				arcSegment.setattrvalue("A2", "160005.000");
				arcSegment.setattrvalue("C1", "500000.000");
				arcSegment.setattrvalue("C2", "100000.000");
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
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("failed to validate AREA Datatypes10.Topic.FlaechenTable.areaWithoutOverlaps2d", logger.getErrs().get(0).getEventMsg());
	}
	
	// pruefe ob die Fehlermeldung ausgegeben wird, wenn die outerboundary nicht existiert.
	@Test
	public void missingOuterBoundaryInArea_False(){
		// external object
		Iom_jObject externalObject=new Iom_jObject(ILI_CLASSFLAECHENTABLE, OID1);
		IomObject externalMultisurface=externalObject.addattrobj("areaWithoutOverlaps2d", "MULTISURFACE");
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
		assertEquals("missing outerboundary in areaWithoutOverlaps2d of object o1.", logger.getErrs().get(0).getEventMsg());
	}
}