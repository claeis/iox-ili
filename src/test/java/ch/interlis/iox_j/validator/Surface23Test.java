package ch.interlis.iox_j.validator;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
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

	private LogCollector validateObjects(IomObject... objects) {
		ValidationConfig modelConfig = new ValidationConfig();
		return validateObjects(modelConfig,objects);
	}
    private LogCollector validateObjects(ValidationConfig modelConfig,IomObject... objects) {
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        //settings.setValue(Validator.CONFIG_DEBUG_XTFOUT,"src/test/data/validator/Surface23Test.xtf");

        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC, BID));
        for (IomObject obj : objects) {
            validator.validate(new ObjectEvent(obj));
        }
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());

        return logger;
    }

	// prueft, ob eine Surface erstellt werden kann.
	@Test
	public void surface_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		objSurfaceSuccess.addattrobj("surface2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("480000.000", "70000.000"),
						IomObjectHelper.createCoord("500000.000", "80000.000"),
						IomObjectHelper.createCoord("550000.000", "90000.000"),
						IomObjectHelper.createCoord("480000.000", "70000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		assertThat(logger.getErrs(), is(empty()));
	}
	
	// prueft, ob eine Surface, welche sich selber an 1 Segmentpunkt schneidet erstellt werden kann.
	@Test
	public void surfaceSelfCuttingOn1Point_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		objSurfaceSuccess.addattrobj("surface2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("480000.000", "70000.000"),
						IomObjectHelper.createCoord("500000.000", "80000.000"),
						IomObjectHelper.createCoord("550000.000", "90000.000"),
						IomObjectHelper.createCoord("480000.000", "70000.000")),
				// inner boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("480000.000", "70000.000"),
						IomObjectHelper.createCoord("500000.000", "78000.000"),
						IomObjectHelper.createCoord("505000.000", "78000.000"),
						IomObjectHelper.createCoord("480000.000", "70000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		assertThat(logger.getErrs(), is(empty()));
	}
    @Test
    public void surfaceInnerBoundaryBeforeOuterBoundary_Fail(){
        Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
        objSurfaceSuccess.addattrobj("surface2d", IomObjectHelper.createPolygonFromBoundaries(
                // inner boundary
                IomObjectHelper.createMultiplePolylineBoundary(
                        IomObjectHelper.createCoord("480000.000", "70000.000"),
                        IomObjectHelper.createCoord("500000.000", "78000.000"),
                        IomObjectHelper.createCoord("505000.000", "78000.000"),
                        IomObjectHelper.createCoord("480000.000", "70000.000")),
                // outer boundary
                IomObjectHelper.createMultiplePolylineBoundary(
                        IomObjectHelper.createCoord("480000.000", "70000.000"),
                        IomObjectHelper.createCoord("500000.000", "80000.000"),
                        IomObjectHelper.createCoord("550000.000", "90000.000"),
                        IomObjectHelper.createCoord("480000.000", "70000.000"))
                ));

        LogCollector logger = validateObjects(objSurfaceSuccess);

        // Asserts
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "first boundary not shell (480000.0, 70000.0, NaN)");
    }
    @Test
    public void surfaceSelfCuttingOn1Point_OnePolyline_Fail(){
        Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
        objSurfaceSuccess.addattrobj("surface2d", IomObjectHelper.createPolygonFromBoundaries(
                // outer boundary
                IomObjectHelper.createMultiplePolylineBoundary(
                        IomObjectHelper.createCoord("480000.000", "70000.000"),
                        IomObjectHelper.createCoord("500000.000", "80000.000"),
                        IomObjectHelper.createCoord("550000.000", "90000.000"),
                        IomObjectHelper.createCoord("480000.000", "70000.000"),
                // inner boundary
                        IomObjectHelper.createCoord("500000.000", "78000.000"),
                        IomObjectHelper.createCoord("505000.000", "78000.000"),
                        IomObjectHelper.createCoord("480000.000", "70000.000"))));

        ValidationConfig modelConfig=new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.SIMPLE_BOUNDARY,ValidationConfig.TRUE);
        LogCollector logger = validateObjects(modelConfig,objSurfaceSuccess);

        // Asserts
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "not a simple boundary (480000.0, 70000.0, NaN)");
    }

	// prueft, ob eine Surface erstellt werden kann, wenn sie 2 Innerboundaries besitzt,
	// welche sich nicht ueberschneiden.
	@Test
	public void surface2Innerboundaries_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		objSurfaceSuccess.addattrobj("surface2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("480000.000", "70000.000"),
						IomObjectHelper.createCoord("850000.000", "191500.000"),
						IomObjectHelper.createCoord("480000.000", "310000.000"),
						IomObjectHelper.createCoord("480000.000", "70000.000")),
				// inner boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("486000.000", "199000.000"),
						IomObjectHelper.createCoord("486000.000", "201000.000"),
						IomObjectHelper.createCoord("488000.000", "201000.000"),
						IomObjectHelper.createCoord("486000.000", "199000.000")),
				// 2. inner boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("490500.000", "199000.000"),
						IomObjectHelper.createCoord("490500.000", "201000.000"),
						IomObjectHelper.createCoord("489500.000", "200000.000"),
						IomObjectHelper.createCoord("490500.000", "199000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		assertThat(logger.getErrs(), is(empty()));
	}
	
	// prueft, ob eine Surface erstellt werden kann, wenn sie 2 Innerboundaries besitzt,
	// welche sich nur an einem Punkt beruehren.
	@Test
	public void twoInnerboundaries_TouchEachOtherOn1Point_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		objSurfaceSuccess.addattrobj("surface2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("480000.000", "70000.000"),
						IomObjectHelper.createCoord("850000.000", "191500.000"),
						IomObjectHelper.createCoord("480000.000", "310000.000"),
						IomObjectHelper.createCoord("480000.000", "70000.000")),
				// inner boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("486000.000", "199000.000"),
						IomObjectHelper.createCoord("486000.000", "201000.000"),
						IomObjectHelper.createCoord("488000.000", "201000.000"),
						IomObjectHelper.createCoord("486000.000", "199000.000")),
				// 2. inner boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("488000.000", "201000.000"),
						IomObjectHelper.createCoord("490500.000", "201000.000"),
						IomObjectHelper.createCoord("489500.000", "200000.000"),
						IomObjectHelper.createCoord("488000.000", "201000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		assertThat(logger.getErrs(), is(empty()));
	}
	
	// prueft, ob eine Surface mit einem Kreisbogen erstellt werden kann.
	@Test
	public void surfaceWithArc_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		objSurfaceSuccess.addattrobj("surface2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createArc("540000.000", "160000.000", "500000.000", "100000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		assertThat(logger.getErrs(), is(empty()));
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn
	// die outer boundary von einer Linie nicht vollstaendig geschlossen wird.
	@Test
	public void surface_BoundaryNotClosed_Dangles_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		objSurfaceSuccess.addattrobj("surface2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("505000.000", "105000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"dangle tid o1",
				"dangle tid o1",
				"dangle tid o1",
				"no polygon");
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn
	// die outer boundary von einem Kreisbogen nicht vollstaendig geschlossen wird.
	// die Fehlermeldung mit den Dangles soll ausgegeben werden und nicht: lineRing not closed.
	@Test
	public void surfaceWithArc_BoundaryNotClosed_Dangles_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		objSurfaceSuccess.addattrobj("surface2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createArc("540000.000", "160000.000", "505000.000", "105000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"dangle tid o1",
				"dangle tid o1",
				"dangle tid o1",
				"no polygon");
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird,
	// wenn eine Linie der InnerBoundary auf einer Linie der OuterBoundary liegt
	// und beide Boundaries vollstaendig geschlossen werden.
	@Test
	public void innerBoundaryOverlayOuterBoundaryLine_BoundariesAreClosed_Fail(){
		EhiLogger.getInstance().setTraceFilter(false);
		Iom_jObject geometricFigure=new Iom_jObject(ILI_CLASSC, OID1);
		geometricFigure.addattrobj("surface2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("500000.000", "300000.000"),
						IomObjectHelper.createCoord("700000.000", "300000.000"),
						IomObjectHelper.createCoord("700000.000", "100000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000")),
				// inner boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "300000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"))));

		LogCollector logger = validateObjects(geometricFigure);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Overlay coord1 (500000.000, 100000.000), coord2 (500000.000, 300000.000), tids o1, o1");
	}
	
	// prueft ob eine Overlay Fehlermeldung ausgegeben wird,
	// wenn eine Linie der InnerBoundary auf einer Linie der OuterBoundary liegt
	// und die InnerBoundary nicht vollstaendig geschlossen ist.
	@Test
	public void innerBoundary_Overlay_OuterBoundaryLine_AndIsNotClosed_Fail(){
		//EhiLogger.getInstance().setTraceFilter(false);
		Iom_jObject geometricFigure=new Iom_jObject(ILI_CLASSC, OID1);
		geometricFigure.addattrobj("surface2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("500000.000", "300000.000"),
						IomObjectHelper.createCoord("700000.000", "300000.000"),
						IomObjectHelper.createCoord("700000.000", "100000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000")),
				// inner boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "300000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "190000.000"))));

		LogCollector logger = validateObjects(geometricFigure);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Overlay coord1 (500000.000, 100000.000), coord2 (500000.000, 300000.000), tids o1, o1");
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn
	// eine Linie der Innerboundary auf einer Linie der Outerboundary liegt.
	// Das Segment der Innerboundary Linie, welches die Outerboundary Linie ueberschneidet,
	// unterscheidet sich zum Outerboundary Segment, nur ueber die Y Koordinate.
	@Test
	public void overlayOf2Lines_InnerLineYCoordToLow_Fail(){
		EhiLogger.getInstance().setTraceFilter(false);
		Iom_jObject geometricFigure=new Iom_jObject(ILI_CLASSC, OID1);
		geometricFigure.addattrobj("surface2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("500000.000", "300000.000"),
						IomObjectHelper.createCoord("700000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000")),
				// inner boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("500000.000", "290000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000"))));

		LogCollector logger = validateObjects(geometricFigure);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Overlay coord1 (500000.000, 100000.000), coord2 (500000.000, 290000.000), tids o1, o1",
				"Intersection coord1 (500000.000, 290000.000), tids o1, o1");
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
		surface.addattrobj("boundary", IomObjectHelper.createMultiplePolylineBoundary(
				IomObjectHelper.createCoord("500000.000", "100000.000"),
				IomObjectHelper.createCoord("500000.000", "300000.000"),
				IomObjectHelper.createCoord("700000.000", "300000.000"),
				IomObjectHelper.createCoord("700000.000", "100000.000"),
				IomObjectHelper.createCoord("500000.000", "100000.000")));
		// inner boundary
		IomObject innerBoundary = surface.addattrobj("boundary", "BOUNDARY");
		// inner boundary polyline: cross(center to leftTop)
		{
			IomObject inner_Cross_CenterToLeftTop = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Cross_CenterToLeftTop.addattrobj("sequence", "SEGMENTS");
			segments.addattrobj("segment", IomObjectHelper.createCoord("600000.000", "200000.000"));
			segments.addattrobj("segment", IomObjectHelper.createCoord("510000.000", "300000.000"));
		}
		// inner boundary polyline: left(top to bottom)
		{
			IomObject inner_Left_TopToBottom = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Left_TopToBottom.addattrobj("sequence", "SEGMENTS");
			segments.addattrobj("segment", IomObjectHelper.createCoord("500000.000", "300000.000"));
			segments.addattrobj("segment", IomObjectHelper.createCoord("500000.000", "100000.000"));
		}
		// inner boundary polyline: cross(leftBottom to center)
		{
			IomObject inner_Cross_TopLeftToCenter = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Cross_TopLeftToCenter.addattrobj("sequence", "SEGMENTS");
			segments.addattrobj("segment", IomObjectHelper.createCoord("500000.000", "100000.000"));
			segments.addattrobj("segment", IomObjectHelper.createCoord("600000.000", "200000.000"));
		}

		LogCollector logger = validateObjects(geometricFigure);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Overlay coord1 (500000.000, 100000.000), coord2 (500000.000, 300000.000), tids o1, o1",
				"Intersection coord1 (510000.000, 300000.000), tids o1, o1");
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
		surface.addattrobj("boundary", IomObjectHelper.createMultiplePolylineBoundary(
				IomObjectHelper.createCoord("500000.000", "100000.000"),
				IomObjectHelper.createCoord("500000.000", "300000.000"),
				IomObjectHelper.createCoord("700000.000", "200000.000"),
				IomObjectHelper.createCoord("500000.000", "100000.000")));
		// inner boundary
		IomObject innerBoundary = surface.addattrobj("boundary", "BOUNDARY");
		// inner boundary polyline: left(bottom to top)
		{
			IomObject inner_Left_BottomToTop = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Left_BottomToTop.addattrobj("sequence", "SEGMENTS");
			segments.addattrobj("segment", IomObjectHelper.createCoord("500000.000", "100000.000"));
			segments.addattrobj("segment", IomObjectHelper.createCoord("500000.000", "300000.000"));
		}
		// inner boundary polyline: cross(leftTop to center)
		{
			IomObject inner_Cross_LeftTopToCenter = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Cross_LeftTopToCenter.addattrobj("sequence", "SEGMENTS");
			segments.addattrobj("segment", IomObjectHelper.createCoord("510000.000", "290000.000"));
			segments.addattrobj("segment", IomObjectHelper.createCoord("600000.000", "200000.000"));
		}
		// inner boundary polyline: cross(center to leftBottom)
		{
			IomObject inner_Cross_CenterToBottomLeft = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=inner_Cross_CenterToBottomLeft.addattrobj("sequence", "SEGMENTS");
			segments.addattrobj("segment", IomObjectHelper.createCoord("600000.000", "200000.000"));
			segments.addattrobj("segment", IomObjectHelper.createCoord("500000.000", "100000.000"));
		}

		LogCollector logger = validateObjects(geometricFigure);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Overlay coord1 (500000.000, 100000.000), coord2 (500000.000, 300000.000), tids o1, o1");
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
		surfaceValue.addattrobj("boundary", IomObjectHelper.createMultiplePolylineBoundary(
				IomObjectHelper.createCoord("480000.000", "70000.000"),
				IomObjectHelper.createCoord("500000.000", "80000.000"),
				IomObjectHelper.createCoord("550000.000", "90000.000"),
				IomObjectHelper.createCoord("480000.000", "70000.000")));
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		segmentsInner.addattrobj("segment", IomObjectHelper.createCoord("480000.000", "70000.000"));
		segmentsInner.addattrobj("segment", IomObjectHelper.createCoord("500000.000", "78000.000"));
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		segments2Inner.addattrobj("segment", IomObjectHelper.createCoord("500000.000", "80000.000"));
		segments2Inner.addattrobj("segment", IomObjectHelper.createCoord("505000.000", "78000.000"));
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		segments3Inner.addattrobj("segment", IomObjectHelper.createCoord("505000.000", "78000.000"));
		segments3Inner.addattrobj("segment", IomObjectHelper.createCoord("480000.000", "70000.000"));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"dangle tid o1");
	}
	
	// Es soll getestet werden, ob eine Intersection Fehlermeldung ausgegeben wird,
	// wenn 2 Innerboundaries einander Ueberschneiden.
	@Test
	public void twoInnerboundaryLines_IntersectEachOther_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		objSurfaceSuccess.addattrobj("surface2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("480000.000", "70000.000"),
						IomObjectHelper.createCoord("500000.000", "80000.000"),
						IomObjectHelper.createCoord("550000.000", "90000.000"),
						IomObjectHelper.createCoord("480000.000", "70000.000")),
				// inner boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("560000.000", "170000.000"),
						IomObjectHelper.createCoord("680000.000", "90000.000"),
						IomObjectHelper.createCoord("680000.000", "230000.000"),
						IomObjectHelper.createCoord("560000.000", "170000.000")),
				// 2. inner boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "90000.000"),
						IomObjectHelper.createCoord("600000.000", "170000.000"),
						IomObjectHelper.createCoord("500000.000", "230000.000"),
						IomObjectHelper.createCoord("500000.000", "90000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Intersection coord1 (581818.182, 155454.545), tids o1, o1",
				"Intersection coord1 (581818.182, 180909.091), tids o1, o1");
	}	
	
	// Es soll getestet werden, ob eine Intersection Fehlermeldung ausgegeben wird,
	// wenn die Outerboundary die Innerboundary Ueberschneidet.
	@Test
	public void innerboundary_IntersectOuterboundary_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		objSurfaceSuccess.addattrobj("surface2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("480000.000", "70000.000"),
						IomObjectHelper.createCoord("500000.000", "80000.000"),
						IomObjectHelper.createCoord("550000.000", "90000.000"),
						IomObjectHelper.createCoord("480000.000", "70000.000")),
				// inner boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("480000.000", "70000.000"),
						IomObjectHelper.createCoord("485000.000", "71000.000"),
						IomObjectHelper.createCoord("505000.000", "78000.000"),
						IomObjectHelper.createCoord("480000.000", "70000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Intersection coord1 (491666.667, 73333.333), tids o1, o1");
	}
	
	// prueft, ob eine Surface erstellt werden kann, wenn
	// der Typ der Surface nicht vom Typ Multisurface ist.
	@Test
	public void invalidSurfaceType_Fail(){
		Iom_jObject objNotMultisurface=new Iom_jObject(ILI_CLASSC, OID1);
		IomObject surfaceValue=objNotMultisurface.addattrobj("surface2d", "SURFACE");

		LogCollector logger = validateObjects(objNotMultisurface);

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

		LogCollector logger = validateObjects(objCompleteMultisurface);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"invalid number of surfaces in COMPLETE basket");
	}
	
	// prueft, ob eine Surface erstellt werden kann,
	// wenn sie 2d ist und in 3d erstellt wird.
	@Test
	public void surface2dWith3dImplementation_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		objSurfaceSuccess.addattrobj("surface2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("480000.000", "70000.000", "1000.000"),
						IomObjectHelper.createCoord("500000.000", "80000.000"),
						IomObjectHelper.createCoord("550000.000", "90000.000"),
						IomObjectHelper.createCoord("480000.000", "70000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Wrong COORD structure, C3 not expected");
	}
	
	// prueft, ob eine Surface erstellt werden kann,
	// wenn sie 3d ist und in 2d erstellt wird.
	@Test
	public void surface3dWith2dImplementation_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		objSurfaceSuccess.addattrobj("surface3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("480000.000", "70000.000", "1000.000"),
						IomObjectHelper.createCoord("500000.000", "80000.000", "1500.000"),
						IomObjectHelper.createCoord("550000.000", "90000.000", "2000.000"),
						IomObjectHelper.createCoord("480000.000", "70000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Wrong COORD structure, C3 expected");
	}
	
	// prueft, ob eine Surface erstellt werden kann, wenn
	// das Startsegment der ersten Polyline eine ungueltige Zahl ist.
	@Test
	public void surfaceInvalidValueRange_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		objSurfaceSuccess.addattrobj("surface3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("4800000.000", "700000.000", "10000.000"),
						IomObjectHelper.createCoord("500000.000", "80000.000", "1500.000"),
						IomObjectHelper.createCoord("550000.000", "90000.000", "2000.000"),
						IomObjectHelper.createCoord("4800000.000", "700000.000", "10000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"value 4800000.000 is out of range in attribute surface3d",
				"value 700000.000 is out of range in attribute surface3d",
				"value 10000.000 is out of range in attribute surface3d");
	}
	
	// prueft, ob 2 Polygone erstellt werden koennen, wenn 2 Polygone mit je einem Arc,
	// welche uebereinander liegen und sich an den gleichen Punkten beruehren, erstellt werden koennen.
	@Test
	public void twoPolygon_WithArcsLieExactlyOnEachOther_Ok(){
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		objAreaSuccess.addattrobj("surface3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000", "5000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000", "5000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000", "5000.000"),
						IomObjectHelper.createArc("540000.000", "160000.000", "500000.000", "100000.000", "5000.000"))));
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSC, OID2);
		objAreaSuccess2.addattrobj("surface3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000", "5000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000", "5000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000", "5000.000"),
						IomObjectHelper.createArc("540000.000", "160000.000", "500000.000", "100000.000", "5000.000"))));

		LogCollector logger = validateObjects(objAreaSuccess, objAreaSuccess2);

		assertThat(logger.getErrs(), is(empty()));
	}
	
	// prueft, ob 2 Polygone erstellt werden koennen, wenn 2 Polygone mit je einem Arc,
	// welche uebereinander liegen und sich nicht an den gleichen Punkten beruehren, erstellt werden koennen.
	@Test
	public void twoPolygon_2ArcsLieNotExactlyOnEachOther_Ok(){
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		objAreaSuccess.addattrobj("surface3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("499996.000", "100000.000", "5000.000"),
						IomObjectHelper.createCoord("500000.000", "100005.000", "5000.000"),
						IomObjectHelper.createArc("500004.000", "100003.000", "500000.000", "99995.000", "5000.000"),
						IomObjectHelper.createCoord("499996.000", "100000.000", "5000.000"))));
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSC, OID2);
		objAreaSuccess2.addattrobj("surface3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("499996.000", "100000.000", "5000.000"),
						IomObjectHelper.createCoord("500000.000", "100005.000", "5000.000"),
						IomObjectHelper.createArc("500004.000", "99997.000", "500000.000", "99995.000", "5000.000"),
						IomObjectHelper.createCoord("499996.000", "100000.000", "5000.000"))));

		LogCollector logger = validateObjects(objAreaSuccess, objAreaSuccess2);

		assertThat(logger.getErrs(), is(empty()));
	}
	
	// prueft, ob eine Surface erstellt werden kann, wenn
	// das Startsegment des ersten Kreisbogens eine ungueltige Zahl ist.
	@Test
	public void surfaceWithARCInvalidValueRange_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSC, OID1);
		objSurfaceSuccess.addattrobj("surface3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("480000.000", "70000.000", "1000.000"),
						IomObjectHelper.createCoord("500000.000", "80000.000", "1500.000"),
						IomObjectHelper.createCoord("550000.000", "90000.000", "2000.000"),
						IomObjectHelper.createArc("4800000.000", "700000.000", "480000.000", "70000.000", "1000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"value 4800000.000 is out of range in attribute surface3d",
				"value 700000.000 is out of range in attribute surface3d");
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
			segments.addattrobj("segment", IomObjectHelper.createCoord("500000.000", "100000.000"));
			segments.addattrobj("segment", IomObjectHelper.createCoord("600000.000", "100000.000"));
			// polyline 2
			IomObject polyline2 = surfaceValue.addattrobj("polyline", "POLYLINE");
			IomObject segments2=polyline2.addattrobj("sequence", "SEGMENTS");
			segments2.addattrobj("segment", IomObjectHelper.createCoord("600000.000", "100000.000"));
			segments2.addattrobj("segment", IomObjectHelper.createCoord("600000.000", "200000.000"));
			// polyline 3
			IomObject polyline3 = surfaceValue.addattrobj("polyline", "POLYLINE");
			IomObject segments3=polyline3.addattrobj("sequence", "SEGMENTS");
			segments3.addattrobj("segment", IomObjectHelper.createCoord("600000.000", "200000.000"));
			segments3.addattrobj("segment", IomObjectHelper.createCoord("500000.000", "200000.000"));
			// polyline 4
			IomObject polyline4 = surfaceValue.addattrobj("polyline", "POLYLINE");
			IomObject segments4=polyline4.addattrobj("sequence", "SEGMENTS");
			segments4.addattrobj("segment", IomObjectHelper.createCoord("500000.000", "200000.000"));
			segments4.addattrobj("segment", IomObjectHelper.createCoord("500000.000", "100000.000"));
		}

		LogCollector logger = validateObjects(externalObject);

		// Asserts
		assertThat(logger.getWarn(), is(empty()));
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"missing outerboundary in surface2d of object o1.");
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
		externalObject.addattrobj("surface2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary of external object
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000")),
				// inner boundary of external object
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("545000.000", "110000.000"),
						IomObjectHelper.createCoord("585000.000", "185000.000"),
						IomObjectHelper.createCoord("515000.000", "185000.000"),
						IomObjectHelper.createCoord("545000.000", "110000.000"))));
		// internal object
		Iom_jObject internalObject=new Iom_jObject(ILI_CLASSC, OID2);
		internalObject.addattrobj("surface2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary of internal object
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("545000.000", "130000.000"),
						IomObjectHelper.createCoord("585000.000", "185000.000"),
						IomObjectHelper.createCoord("515000.000", "185000.000"),
						IomObjectHelper.createCoord("545000.000", "130000.000"))));

		LogCollector logger = validateObjects(externalObject, internalObject);

		// Asserts
		assertThat(logger.getErrs(), is(empty()));
	}
}