package ch.interlis.iox_j.validator;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
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

	private LogCollector validateObjects(IomObject... objects) {
		ValidationConfig modelConfig = new ValidationConfig();
		return validateObjects(modelConfig, objects);
	}

	private LogCollector validateObjects(ValidationConfig modelConfig, IomObject... objects) {
		LogCollector logger = new LogCollector();
		LogEventFactory errFactory = new LogEventFactory();
		Settings settings = new Settings();
		//settings.setValue(Validator.CONFIG_DEBUG_XTFOUT,"src/test/data/validator/Area23Test.xtf");

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

	// prueft ob ein Polygon erstellt werden kann.
	@Test
	public void onePolygon_1Boundary_Area_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("480000.000", "70000.000"),
						IomObjectHelper.createCoord("500000.000", "80000.000"),
						IomObjectHelper.createCoord("550000.000", "90000.000"),
						IomObjectHelper.createCoord("480000.000", "70000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		assertThat(logger.getErrs(), is(empty()));
	}
	
	// prueft ob ein Polygon erstellt werden kann, wenn sie sich selber an 1 Punkt beruehrt.
	@Test
	public void onePolygon_1Boundary_OuterboundaryTouchesOwnLineOn1Point_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
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

	// prueft ob ein Polygon, bestehend aus 2 Innerboundaries welche sich
	// nicht beruehren und 1 Outerboundary erstellt werden kann.
	@Test
	public void onePolygon_3Boundaries_2Innerboundaries_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
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
	
	// prueft ob ein Polygon, bestehend aus 2 Innerboundaries welche sich an einem
	// Punkt beruehren und einem Outerboundary erstellt werden kann.
	@Test
	public void onePolygon_3Boundaries_InnerBoundariesTouchesEachOtherOn1Point_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
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
	
	// prueft ob ein Polygon, bestehend aus 1 Outerboundary und 1 Innerboundary erstellt werden kann.
	@Test
	public void onePolygon_2Boundaries_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("480000.000", "70000.000"),
						IomObjectHelper.createCoord("500000.000", "80000.000"),
						IomObjectHelper.createCoord("550000.000", "90000.000"),
						IomObjectHelper.createCoord("480000.000", "70000.000")),
				// inner boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "77000.000"),
						IomObjectHelper.createCoord("500000.000", "78000.000"),
						IomObjectHelper.createCoord("505000.000", "78000.000"),
						IomObjectHelper.createCoord("500000.000", "77000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		assertThat(logger.getErrs(), is(empty()));
	}
	
	// prueft ob eine Fehlermeldung ausgegeben werden kann, wenn die Innerboundary, die Outerboundary
	// an einem Punkt auf der Linie beruehrt.
	@Test
	public void onePolygon_2Boundaries_InnerPointTouchesOuterBoundaryLine_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000")),
				// inner boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("550000.000", "100000.000"),
						IomObjectHelper.createCoord("580000.000", "150000.000"),
						IomObjectHelper.createCoord("520000.000", "150000.000"),
						IomObjectHelper.createCoord("550000.000", "100000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Intersection coord1 (550000.000, 100000.000), tids o1, o1",
				"Intersection coord1 (550000.000, 100000.000), tids o1, o1");
	}
	
	// prueft, ob 2 Polygone die sich ueberlappen mit ausgeschalteter
	// AREA Topologyvalidierung akzeptiert werden.
	@Test
	public void twoPolygon_OverlapEachOther_TopValidationOff_Ok(){
		Iom_jObject polygon1=new Iom_jObject(ILI_CLASSD, OID1);
		polygon1.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createBoundary(
						IomObjectHelper.createCoord("480000.000", "77000.000"),
						IomObjectHelper.createCoord("550000.000", "77000.000"),
						IomObjectHelper.createCoord("550000.000", "78000.000"),
						IomObjectHelper.createCoord("480000.000", "77000.000"))));
		Iom_jObject polygon2=new Iom_jObject(ILI_CLASSD, OID2);
		polygon2.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createBoundary(
						IomObjectHelper.createCoord("500000.000", "70000.000"),
						IomObjectHelper.createCoord("500000.000", "80000.000"),
						IomObjectHelper.createCoord("505000.000", "80000.000"),
						IomObjectHelper.createCoord("500000.000", "70000.000"))));

		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.AREA_OVERLAP_VALIDATION, ValidationConfig.OFF);
		LogCollector logger = validateObjects(modelConfig, polygon1, polygon2);

		// Asserts
		assertThat(logger.getErrs(), is(empty()));
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn 2 Polygone sich an 4 Stellen ueberlappen.
	@Test
	public void twoPolygon_OverlapEachOther_Fail(){
		Iom_jObject polygon1=new Iom_jObject(ILI_CLASSD, OID1);
		polygon1.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createBoundary(
						IomObjectHelper.createCoord("480000.000", "77000.000"),
						IomObjectHelper.createCoord("550000.000", "77000.000"),
						IomObjectHelper.createCoord("550000.000", "78000.000"),
						IomObjectHelper.createCoord("480000.000", "77000.000"))));
		Iom_jObject polygon2=new Iom_jObject(ILI_CLASSD, OID2);
		polygon2.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createBoundary(
						IomObjectHelper.createCoord("500000.000", "70000.000"),
						IomObjectHelper.createCoord("500000.000", "80000.000"),
						IomObjectHelper.createCoord("505000.000", "80000.000"),
						IomObjectHelper.createCoord("500000.000", "70000.000"))));

		LogCollector logger = validateObjects(polygon1, polygon2);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Intersection coord1 (500000.000, 77000.000), tids o1, o2",
				"Intersection coord1 (503500.000, 77000.000), tids o1, o2",
				"Intersection coord1 (500000.000, 77285.714), tids o1, o2",
				"Intersection coord1 (503669.065, 77338.129), tids o1, o2",
				"failed to validate AREA Datatypes23.Topic.ClassD.area2d");
	}
    @Test
    public void twoPolygon_OneInsideOther_Fail(){
        Iom_jObject polygon1=new Iom_jObject(ILI_CLASSD, OID1);
        polygon1.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
                // outer boundary
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("480000.000", "70000.000"),
                        IomObjectHelper.createCoord("550000.000", "70000.000"),
                        IomObjectHelper.createCoord("550000.000", "80000.000"),
                        IomObjectHelper.createCoord("480000.000", "80000.000"),
                        IomObjectHelper.createCoord("480000.000", "70000.000"))));
        Iom_jObject polygon2=new Iom_jObject(ILI_CLASSD, OID2);
        polygon2.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("500000.000", "72000.000"),
                        IomObjectHelper.createCoord("505000.000", "72000.000"),
                        IomObjectHelper.createCoord("505000.000", "78000.000"),
                        IomObjectHelper.createCoord("500000.000", "78000.000"),
                        IomObjectHelper.createCoord("500000.000", "72000.000"))));

        LogCollector logger = validateObjects(polygon1, polygon2);

        // Asserts
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "polygons overlay tid1 o1, tid2 o2",
                "failed to validate AREA Datatypes23.Topic.ClassD.area2d");
    }
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn die Outerboundary des zweiten Polygons,
	// die Outer und Innerboundary des ersten Polygons ueberschneidet.
	@Test
	public void twoPolygon_Polygon1OverlapsPolygon2InnerAndOuterBoundary_Fail(){
		Iom_jObject polygon1=new Iom_jObject(ILI_CLASSD, OID1);
		polygon1.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000")),
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("520000.000", "120000.000"),
						IomObjectHelper.createCoord("580000.000", "120000.000"),
						IomObjectHelper.createCoord("580000.000", "180000.000"),
						IomObjectHelper.createCoord("520000.000", "180000.000"),
						IomObjectHelper.createCoord("520000.000", "120000.000"))));
		Iom_jObject polygon2=new Iom_jObject(ILI_CLASSD, OID2);
		polygon2.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("540000.000", "250000.000"),
						IomObjectHelper.createCoord("560000.000", "250000.000"),
						IomObjectHelper.createCoord("550000.000", "150000.000"),
						IomObjectHelper.createCoord("540000.000", "250000.000"))));

		LogCollector logger = validateObjects(polygon1, polygon2);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Intersection coord1 (545000.000, 200000.000), tids o1, o2",
				"Intersection coord1 (555000.000, 200000.000), tids o1, o2",
				"Intersection coord1 (547000.000, 180000.000), tids o1, o2",
				"Intersection coord1 (553000.000, 180000.000), tids o1, o2",
				"failed to validate AREA Datatypes23.Topic.ClassD.area2d");
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn die Innerboundary,
	// die Flaeche der Outerboundary unterteilt.
	@Test
	public void onePolygon_InnerboundaryDevidesOuterboundary_Fail(){
		Iom_jObject polygon1=new Iom_jObject(ILI_CLASSD, OID1);
		polygon1.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000")),
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("580000.000", "120000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000"))));

		LogCollector logger = validateObjects(polygon1);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"superfluous outerboundary tid o1",
				"superfluous outerboundary tid o1",
				"multipolygon detected");
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn 2 Polygone exakt
	// uebereinander liegen.
	@Test
	public void twoPolygon_OverlayExactlyOnEachOther_Fail(){
		Iom_jObject polygon1=new Iom_jObject(ILI_CLASSD, OID1);
		polygon1.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000"))));
		Iom_jObject polygon2=new Iom_jObject(ILI_CLASSD, OID2);
		polygon2.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000"))));

		LogCollector logger = validateObjects(polygon1, polygon2);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"polygons overlay tid1 o1, tid2 o2",
				"failed to validate AREA Datatypes23.Topic.ClassD.area2d");
	}
	
	// prueft ob ein Polygon mit einem Kreisbogen erstellt werden kann.
	@Test
	public void onePolygon_1Boundary_AreaWithArc_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createBoundary(
						IomObjectHelper.createCoord("480000.000", "70000.000"),
						IomObjectHelper.createCoord("480001.000", "70001.000"),
						// Arc
						IomObjectHelper.createArc("480000.000", "300000.000", "480000.000", "70000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		assertThat(logger.getErrs(), is(empty()));
	}
	
	// prueft, ob 2 Poygone erstellt werden koennen, wenn beide Polygone
	// sich an einem Punkt beruehren.
	@Test
	public void twoPolygon_PolygonTouchesOtherPolygon_On1Point_Ok(){
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objAreaSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000"))));
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSD, OID2);
		objAreaSuccess2.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("650000.000", "200000.000"),
						IomObjectHelper.createCoord("650000.000", "250000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"))));

		LogCollector logger = validateObjects(objAreaSuccess, objAreaSuccess2);

		// Asserts
		assertThat(logger.getErrs(), is(empty()));
	}
	
	// prueft, ob 2 Polygone erstellt werden koennen, wenn 2 Polygone sich auf einer Linie an
	// drei Punkten beruehren. Beide Punkte liegen aufeinander und haben beide dieselben Segmentpunkte.
	@Test
	public void twoPolygon_LineOverlayOnLineOfOtherArea_OnSame3Points_Ok(){
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objAreaSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("540000.000", "100000.000"),
						IomObjectHelper.createCoord("540000.000", "150000.000"),
						IomObjectHelper.createCoord("540000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000"))));
		Iom_jObject objArea2Success=new Iom_jObject(ILI_CLASSD, OID2);
		objArea2Success.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("580000.000", "100000.000"),
						IomObjectHelper.createCoord("540000.000", "100000.000"),
						IomObjectHelper.createCoord("540000.000", "150000.000"),
						IomObjectHelper.createCoord("540000.000", "200000.000"),
						IomObjectHelper.createCoord("580000.000", "100000.000"))));

		LogCollector logger = validateObjects(objAreaSuccess, objArea2Success);

		// Asserts
		assertThat(logger.getErrs(), is(empty()));
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn 2 Polygone sich auf einer Linie an
	// zwei Punkten beruehren. Beide Punkte liegen aufeinander.
	// Die zweite Polygon hat auf der Selben Linie, einen Segmentpunkt mehr.
	@Test
	public void twoPolygon_LineOverlayOnLineOfOtherArea_OnDifferentPoints_Fail(){
		Iom_jObject objArea1=new Iom_jObject(ILI_CLASSD, OID1);
		objArea1.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("540000.000", "100000.000"),
						IomObjectHelper.createCoord("540000.000", "150000.000"),
						IomObjectHelper.createCoord("540000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000"))));
		Iom_jObject objArea2=new Iom_jObject(ILI_CLASSD, OID2);
		objArea2.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("580000.000", "100000.000"),
						IomObjectHelper.createCoord("540000.000", "100000.000"),
						IomObjectHelper.createCoord("540000.000", "150000.000"),
						IomObjectHelper.createCoord("540000.000", "160000.000"),
						IomObjectHelper.createCoord("540000.000", "200000.000"),
						IomObjectHelper.createCoord("580000.000", "100000.000"))));

		LogCollector logger = validateObjects(objArea1, objArea2);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Overlay coord1 (540000.000, 150000.000), coord2 (540000.000, 160000.000), tids o1, o2",
				"Overlay coord1 (540000.000, 160000.000), coord2 (540000.000, 200000.000), tids o1, o2",
				"failed to validate AREA Datatypes23.Topic.ClassD.area2d");
	}
	
	// prueft, ob 2 Polygone (mit je einem Arc) die genau uebereinanderliegen, als Fehler gemeldet wird
	@Test
	public void twoPolygon_WithArcsLieExactlyOnEachOther_Fail(){
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objAreaSuccess.addattrobj("area3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000", "5000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000", "5000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000", "5000.000"),
						IomObjectHelper.createArc("540000.000", "160000.000", "500000.000", "100000.000", "5000.000"))));
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSD, OID2);
		objAreaSuccess2.addattrobj("area3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000", "5000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000", "5000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000", "5000.000"),
						IomObjectHelper.createArc("540000.000", "160000.000", "500000.000", "100000.000", "5000.000"))));

		LogCollector logger = validateObjects(objAreaSuccess, objAreaSuccess2);

		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"polygons overlay tid1 o1, tid2 o2",
				"failed to validate AREA Datatypes23.Topic.ClassD.area3d");
	}
	
	// prueft, ob 2 Polygone erstellt werden koennen, wenn 2 Polygone mit je einem Arc,
	// welche uebereinander liegen und sich nicht an den gleichen Punkten beruehren, erstellt werden koennen.
	@Test
	public void twoPolygon_2ArcsLieNotExactlyOnEachOther_1ArcHas1PointMore_Fail(){
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objAreaSuccess.addattrobj("area3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("499996.000", "100000.000", "5000.000"),
						IomObjectHelper.createCoord("500000.000", "100005.000", "5000.000"),
						IomObjectHelper.createArc("500004.000", "100003.000", "500000.000", "99995.000", "5000.000"),
						IomObjectHelper.createCoord("499996.000", "100000.000", "5000.000"))));
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSD, OID2);
		objAreaSuccess2.addattrobj("area3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("499996.000", "100000.000", "5000.000"),
						IomObjectHelper.createCoord("500000.000", "100005.000", "5000.000"),
						IomObjectHelper.createArc("500004.000", "99997.000", "500000.000", "99995.000", "5000.000"),
						IomObjectHelper.createCoord("499996.000", "100000.000", "5000.000"))));

		LogCollector logger = validateObjects(objAreaSuccess, objAreaSuccess2);

		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"polygons overlay tid1 o1, tid2 o2",
				"failed to validate AREA Datatypes23.Topic.ClassD.area3d");
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird,
	// wenn auf einem "geraden Segment" der OuterBoundary, ein "gerades Segment" der InnerBoundary liegt.
	// Beide Segmente teilen die selben Anfangs und Endpunkte.
	@Test
	public void onePolygon_2Boundaries_LineOfInnerOverlayLineOfOuterBoundary_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000")),
				// inner boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("550000.000", "150000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Overlay coord1 (500000.000, 100000.000), coord2 (600000.000, 100000.000), tids o1, o1");
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird,
	// wenn auf einem "geraden Segment" der OuterBoundary, ein "gerades Segment" der InnerBoundary liegt.
	// Beide Segmente teilen die selben Anfangs und Endpunkte.
	// Dazu wird die Innerboundary nicht geschlossen. Es soll nur die overlay Meldung ausgegeben werden.
	@Test
	public void onePolygon_2Boundaries_LineOfInnerOverlayLineOfOuterBoundary_IgnoreInvalidRinglines_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000")),
				// inner boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("520000.000", "150000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("580000.000", "150000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Overlay coord1 (500000.000, 100000.000), coord2 (600000.000, 100000.000), tids o1, o1");
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird,
	// wenn auf einem "geraden Segment" der OuterBoundary, ein "gerades Segment" der InnerBoundary liegt.
	// Beide Segmente teilen die selben Anfangs und Endpunkte.
	// Dazu wird die Innerboundary nicht geschlossen.
	@Test
	public void onePolygon_2Boundaries_LineOfInnerOverlayLineOfOuterBoundary_InnerboundaryNotClosed_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000")),
				// inner boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("550000.000", "150000.000"),
						IomObjectHelper.createCoord("520000.000", "180000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Overlay coord1 (500000.000, 100000.000), coord2 (500000.000, 200000.000), tids o1, o1");
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird,
	// wenn auf einem "geraden Segment" der OuterBoundary, ein "gerades Segment" der InnerBoundary liegt.
	// Beide Segmente teilen die selben Anfangspunkte, jedoch nicht die Selben Endpunkte.
	// Dazu liegt ein Punkt der Innerboundary-Linie, auf einer Linie der Outerboundary.
	@Test
	public void onePolygon_2Boundaries_LineOfInnerOverlayLineOfOuterBoundary_PointOnXLine_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000")),
				// inner boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("550000.000", "150000.000"),
						IomObjectHelper.createCoord("550000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Intersection coord1 (550000.000, 200000.000), tids o1, o1",
				"Overlay coord1 (500000.000, 200000.000), coord2 (550000.000, 200000.000), tids o1, o1");
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird,
	// wenn auf zwei "geraden Segmenten" der OuterBoundary, 2 "gerade Segmente" der InnerBoundary liegen.
	// die Segmente der ersten overlay, teilen sich dieselben Anfangs und Endpunkte.
	// die Segmente der zweiten overlay, teilen sich dieselben Anfangspunkte, jedoch unterschiedliche Endpunkte.
	// Dazu liegt ein Punkt der Innerboundary-Linie, auf einer Linie der Outerboundary.
	@Test
	public void onePolygon_2Boundaries_2LinesOfInnerOverlayOuterboundaryLines_PointOnLine_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000")),
				// inner boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("550000.000", "150000.000"),
						IomObjectHelper.createCoord("550000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Intersection coord1 (550000.000, 200000.000), tids o1, o1",
				"Overlay coord1 (500000.000, 200000.000), coord2 (550000.000, 200000.000), tids o1, o1",
				"Overlay coord1 (500000.000, 100000.000), coord2 (500000.000, 200000.000), tids o1, o1");
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn Dangles erstellt wurden.
	@Test
	public void onePolygon_1Boundary_Dangles_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("520000.000", "150000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"dangle tid o1");
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird,
	// wenn sich 2 gerade Linien einer innerboundary, mit 2 geraden Linien einer anderen innerboundary ueberschneiden.
	@Test
	public void onePolygon_2Boundaries_TwoInnerboundaryLinesCutEachOther_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("480000.000", "70000.000"),
						IomObjectHelper.createCoord("700000.000", "70000.000"),
						IomObjectHelper.createCoord("700000.000", "250000.000"),
						IomObjectHelper.createCoord("480000.000", "250000.000"),
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
	
	// prueft, ob eine Fehlermeldung ausgegeben wird,
	// wenn sich die Innerboundary mit der Outerboundary ueberschneidet.
	@Test
	public void onePolygon_2Boundaries_InnerIntersectOuterboundary_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
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
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn diese aus 3d Koordinaten besteht,
	// obwohl im Modell nur 2d Koordinaten definiert wurden.
	@Test
	public void onePolygon_1Boundary_2dWith3dImplementation_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
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
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn diese aus 2d Koordinaten besteht,
	// obwohl im Model nur 3d Koordinaten definiert wurden.
	@Test
	public void onePolygon_1Boundary_3dWith2dImplementation_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area3d", IomObjectHelper.createPolygonFromBoundaries(
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
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn die Value eines Segments nicht gueltig ist.
	@Test
	public void onePolygon_1Boundary_3dInvalidValueRange_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("4800000.000", "700000.000", "10000.000"),
						IomObjectHelper.createCoord("500000.000", "80000.000", "1500.000"),
						IomObjectHelper.createCoord("550000.000", "90000.000", "2000.000"),
						IomObjectHelper.createCoord("4800000.000", "700000.000", "10000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"value 4800000.000 is out of range in attribute area3d",
				"value 700000.000 is out of range in attribute area3d",
				"value 10000.000 is out of range in attribute area3d");
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn die Polygon mit einem ungueltigen Kreisbogen erstellt wird.
	@Test
	public void onePolygon_1Boundary_3dWithARCInvalidValueRange_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("480000.000", "70000.000", "1000.000"),
						IomObjectHelper.createCoord("500000.000", "80000.000", "1500.000"),
						IomObjectHelper.createCoord("550000.000", "90000.000", "2000.000"),
						IomObjectHelper.createArc("4800000.000", "700000.000", "480000.000", "70000.000", "1000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"value 4800000.000 is out of range in attribute area3d",
				"value 700000.000 is out of range in attribute area3d");
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn der Type area nicht vom Type Multisurface ist.
	@Test
	public void onePolygon_1Boundary_InvalidAreaType_Fail(){
		Iom_jObject objAreaMultisurface=new Iom_jObject(ILI_CLASSD, OID1);
		IomObject areaValue=objAreaMultisurface.addattrobj("area2d", "AREA");

		LogCollector logger = validateObjects(objAreaMultisurface);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"unexpected Type AREA; MULTISURFACE expected");
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

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"invalid number of surfaces in COMPLETE basket");
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
		segments.addattrobj("segment", IomObjectHelper.createCoord("480000.000", "70000.000"));
		segments.addattrobj("segment", IomObjectHelper.createCoord("500000.000", "80000.000"));
		segments.addattrobj("segment", IomObjectHelper.createCoord("500000.000", "80000.000")); // illegal; repeated COORD
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		segments2.addattrobj("segment", IomObjectHelper.createCoord("500000.000", "80000.000"));
		segments2.addattrobj("segment", IomObjectHelper.createCoord("550000.000", "90000.000"));
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		segments3.addattrobj("segment", IomObjectHelper.createCoord("550000.000", "90000.000"));
		segments3.addattrobj("segment", IomObjectHelper.createCoord("480000.000", "70000.000"));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"duplicate coord at (500000.0, 80000.0, NaN)",
				"duplicate coord at (500000.0, 80000.0, NaN)",
				"duplicate coord at (500000.0, 80000.0, NaN)");
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn die Polygone sich an unterschiedlichen Stellen beruehren.
	@Test
	public void twoPolygon_IntersectEachOther_On2Points_Fail(){
		// polygon1
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("540000.000", "100000.000"),
						IomObjectHelper.createCoord("540000.000", "150000.000"),
						IomObjectHelper.createCoord("540000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000"))));
		// polygon2
		Iom_jObject objSurfaceSuccess2=new Iom_jObject(ILI_CLASSD, OID2);
		objSurfaceSuccess2.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("580000.000", "100000.000"),
						IomObjectHelper.createCoord("540000.000", "100000.000"),
						IomObjectHelper.createCoord("540000.000", "200000.000"),
						IomObjectHelper.createCoord("580000.000", "100000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess, objSurfaceSuccess2);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Overlay coord1 (540000.000, 100000.000), coord2 (540000.000, 150000.000), tids o1, o2",
				"Overlay coord1 (540000.000, 150000.000), coord2 (540000.000, 200000.000), tids o1, o2",
				"failed to validate AREA Datatypes23.Topic.ClassD.area2d");
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
				"missing outerboundary in area2d of object o1.");
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn
	// eine Linie der Innerboundary auf einer Linie der Outerboundary liegt.
	// Das Segment der Innerboundary Linie, welches die Outerboundary Linie ueberschneidet,
	// unterscheidet sich zum Outerboundary Segment, nur ueber die Y Koordinate.
	@Test
	public void onePolygon_2Boundaries_OverlayOf2Lines_InnerLineYCoordToLow_Fail(){
		//EhiLogger.getInstance().setTraceFilter(false);
		Iom_jObject geometricFigure=new Iom_jObject(ILI_CLASSD, OID1);
		geometricFigure.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
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
	// die Outerboundaries der beiden Polygone uebereinander liegen
	// und das zweite Polygon eine Innerboundary besitzt.
	@Test
	public void twoPolygon_2OuterboundariesOverlayOnEachOther_Fail(){
		Iom_jObject polygon1=new Iom_jObject(ILI_CLASSD, OID1);
		polygon1.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000"))));
		Iom_jObject polygon2=new Iom_jObject(ILI_CLASSD, OID2);
		polygon2.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000"))));

		LogCollector logger = validateObjects(polygon1, polygon2);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"polygons overlay tid1 o1, tid2 o2",
				"failed to validate AREA Datatypes23.Topic.ClassD.area2d");
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn
	// das 2.te Polygon, auf dem 1.ten Polygon liegt.
	// das dritte Polygon liegt innerhalb der envelope Flaeche, jedoch nicht auf einer anderen Linie.
	// das vierte Polygon liegt ausserhalb der envelope Flaeche und uerberdeckt keine anderen Linien.
	@Test
	public void fourPolygon_2PolygonsOverlayEachOtherOn1Line_Fail(){
		Iom_jObject polygon1=new Iom_jObject(ILI_CLASSD, OID1);
		polygon1.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("550000.000", "160000.000"),
						IomObjectHelper.createCoord("560000.000", "150000.000"),
						IomObjectHelper.createCoord("550000.000", "140000.000"),
						IomObjectHelper.createCoord("540000.000", "150000.000"),
						IomObjectHelper.createCoord("550000.000", "160000.000"))));
		Iom_jObject polygon2=new Iom_jObject(ILI_CLASSD, OID2);
		polygon2.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("550000.000", "160000.000"),
						IomObjectHelper.createCoord("560000.000", "150000.000"),
						IomObjectHelper.createCoord("550000.000", "140000.000"),
						IomObjectHelper.createCoord("540000.000", "150000.000"),
						IomObjectHelper.createCoord("550000.000", "160000.000"))));
		Iom_jObject polygon3=new Iom_jObject(ILI_CLASSD, OID3);
		polygon3.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("540000.000", "160000.000"),
						IomObjectHelper.createCoord("542000.000", "158000.000"),
						IomObjectHelper.createCoord("542000.000", "156000.000"),
						IomObjectHelper.createCoord("540000.000", "160000.000"))));
		Iom_jObject polygon4=new Iom_jObject(ILI_CLASSD, OID4);
		polygon4.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				// outer boundary
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("520000.000", "200000.000"),
						IomObjectHelper.createCoord("520000.000", "180000.000"),
						IomObjectHelper.createCoord("500000.000", "180000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"))));

		LogCollector logger = validateObjects(polygon1, polygon2, polygon3, polygon4);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"polygons overlay tid1 o1, tid2 o2",
				"failed to validate AREA Datatypes23.Topic.ClassD.area2d");
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
		externalObject.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
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
		Iom_jObject internalObject=new Iom_jObject(ILI_CLASSD, OID2);
		internalObject.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
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
	
	// prueft, ob der richtige Pfad: Model.Topic.Klasse in der Fehlermeldung einer
	// intersection ausgegeben wird.
	@Test
	public void checkClassPath_OfIntersectionMessage_False(){
		EhiLogger.getInstance().setTraceFilter(false);
		Iom_jObject objAreaSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objAreaSuccess.addattrobj("area3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000", "5000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000", "5000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000", "5000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000", "5000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000", "5000.000"))));
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSD, OID2);
		objAreaSuccess2.addattrobj("area3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "80000.000", "5000.000"),
						IomObjectHelper.createCoord("600000.000", "80000.000", "5000.000"),
						IomObjectHelper.createCoord("550000.000", "110000.000", "5000.000"),
						IomObjectHelper.createCoord("500000.000", "80000.000", "5000.000"))));

		LogCollector logger = validateObjects(objAreaSuccess, objAreaSuccess2);

		assertEquals(3, logger.getErrs().size());
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
		objAreaSuccess.addattrobj("area3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000", "5000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000", "5000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000", "5000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000", "5000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000", "5000.000"))));
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSD, OID2);
		objAreaSuccess2.addattrobj("area3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "80000.000", "5000.000"),
						IomObjectHelper.createCoord("600000.000", "80000.000", "5000.000"),
						IomObjectHelper.createCoord("550000.000", "110000.000", "5000.000"),
						IomObjectHelper.createCoord("500000.000", "80000.000", "5000.000"))));

		LogCollector logger = validateObjects(objAreaSuccess, objAreaSuccess2);

        assertEquals(3, logger.getErrs().size());
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
		objAreaSuccess.addattrobj("area3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000", "5000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000", "5000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000", "5000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000", "5000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000", "5000.000"))));
		Iom_jObject objAreaSuccess2=new Iom_jObject(ILI_CLASSD, OID2);
		objAreaSuccess2.addattrobj("area3d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "80000.000", "5000.000"),
						IomObjectHelper.createCoord("600000.000", "80000.000", "5000.000"),
						IomObjectHelper.createArc("550000.000", "110000.000", "500000.000", "80000.000", "5000.000"))));

		LogCollector logger = validateObjects(objAreaSuccess, objAreaSuccess2);

		assertEquals(2, logger.getErrs().size());
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
		obj1.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000")),
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("540000.000", "140000.000"),
						IomObjectHelper.createCoord("560000.000", "140000.000"),
						IomObjectHelper.createCoord("560000.000", "160000.000"),
						IomObjectHelper.createCoord("540000.000", "160000.000"),
						IomObjectHelper.createCoord("540000.000", "140000.000"))));
		Iom_jObject obj2=new Iom_jObject(ILI_CLASSD, OID2);
		obj2.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("540000.000", "140000.000"),
						IomObjectHelper.createCoord("560000.000", "140000.000"),
						IomObjectHelper.createCoord("560000.000", "160000.000"),
						IomObjectHelper.createCoord("540000.000", "160000.000"),
						IomObjectHelper.createCoord("540000.000", "140000.000"))));

		LogCollector logger = validateObjects(obj1, obj2);

		// Asserts
		assertThat(logger.getErrs(), is(empty()));
	}
    // prueft, ob die Validierung einen Fehler meldet, wenn ein Polygon,
    // innerhalb eines anderen Polygons liegt.
    @Test
    public void twoPolygon_Polygon2OverlapsPolygon1_Fail(){
        Iom_jObject obj1=new Iom_jObject(ILI_CLASSD, OID1);
        obj1.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000"))));
        Iom_jObject obj2=new Iom_jObject(ILI_CLASSD, OID2);
        obj2.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("540000.000", "140000.000"),
						IomObjectHelper.createCoord("560000.000", "140000.000"),
						IomObjectHelper.createCoord("560000.000", "160000.000"),
						IomObjectHelper.createCoord("540000.000", "160000.000"),
						IomObjectHelper.createCoord("540000.000", "140000.000"))));

        LogCollector logger = validateObjects(obj1, obj2);

        // Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"polygons overlay tid1 o1, tid2 o2",
				"failed to validate AREA Datatypes23.Topic.ClassD.area2d");
    }
    // prueft, ob die Validierung keinen Fehler meldet, wenn ein Polygon,
    // genau ueber einem InnerBoundary eines anderen Polygons liegt.
    @Test
    public void twoPolygonWithArc_Polygon2ExactlyOverInnerBoundaryOfPolygon1_Ok() throws Exception{
        IomObject outerBoundary = IomObjectHelper.createMultiplePolylineBoundary(
                IomObjectHelper.createCoord("500000.000", "100000.000"),
                IomObjectHelper.createCoord("600000.000", "100000.000"),
                IomObjectHelper.createCoord("600000.000", "200000.000"),
                IomObjectHelper.createCoord("500000.000", "200000.000"),
                IomObjectHelper.createCoord("500000.000", "100000.000"));
        IomObject innerBoundary = IomObjectHelper.createMultiplePolylineBoundary(
                IomObjectHelper.createCoord("540000.000", "140000.000"),
                IomObjectHelper.createArc("555000.000", "140100.000", "560000.000", "140000.000"),
                IomObjectHelper.createCoord("560000.000", "160000.000"),
                IomObjectHelper.createCoord("540000.000", "160000.000"),
                IomObjectHelper.createCoord("540000.000", "140000.000"));

        Iom_jObject obj1=new Iom_jObject(ILI_CLASSD, OID1);
        obj1.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(outerBoundary, innerBoundary));
        Iom_jObject obj2=new Iom_jObject(ILI_CLASSD, OID2);
        obj2.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(innerBoundary));

        LogCollector logger = validateObjects(obj1, obj2);

        // Asserts
        assertThat(logger.getErrs(), is(empty()));
    }
	
	// es wird eine Fehlermeldung erwartet, da ein Polygon, nicht genau ueber einem InnerBoundary einer anderen Polygon liegt.
	@Test
	public void twoPolygon_Polygon2OverlapsInnerBoundaryOfPolygon1_False(){
		Iom_jObject obj1=new Iom_jObject(ILI_CLASSD, OID1);
		obj1.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("500000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "100000.000"),
						IomObjectHelper.createCoord("600000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "200000.000"),
						IomObjectHelper.createCoord("500000.000", "100000.000")),
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("540000.000", "140000.000"),
						IomObjectHelper.createCoord("560000.000", "140000.000"),
						IomObjectHelper.createCoord("560000.000", "160000.000"),
						IomObjectHelper.createCoord("540000.000", "160000.000"),
						IomObjectHelper.createCoord("540000.000", "140000.000"))));
		Iom_jObject obj2=new Iom_jObject(ILI_CLASSD, OID2);
		obj2.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("540000.000", "140000.000"),
						IomObjectHelper.createCoord("560000.000", "140000.000"),
						IomObjectHelper.createCoord("580000.000", "180000.000"),
						IomObjectHelper.createCoord("540000.000", "160000.000"),
						IomObjectHelper.createCoord("540000.000", "140000.000"))));

		LogCollector logger = validateObjects(obj1, obj2);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"polygons overlay tid1 o1, tid2 o2",
				"failed to validate AREA Datatypes23.Topic.ClassD.area2d");
	}
	
	// prueft ob ein Polygon korrekt auf 3 Stellen nach dem Komma gerundet wird
	// und dabei keine Fehler festgestellt werden.
	@Test
	public void onePolygon_1Boundary_Area_RoundingUpTo3DecPoints_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("479999.9995", "70000.000"),
						IomObjectHelper.createCoord("500000.000", "80000.000"),
						IomObjectHelper.createCoord("550000.000", "90000.0000"),
						IomObjectHelper.createCoord("479999.9995", "70000.000"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		assertThat(logger.getErrs(), is(empty()));
	}
	
	// prueft ob die Koordinaten richtig abgerundet werden.
	@Test
	public void onePolygon_1Boundary_Area_RoundingDown_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("480000.0004", "70000.0004"),
						IomObjectHelper.createCoord("500000.000", "80000.000"),
						IomObjectHelper.createCoord("550000.000", "90000.000"),
						IomObjectHelper.createCoord("480000.0004", "70000.0004"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		assertThat(logger.getErrs(), is(empty()));
	}
	
	// prueft ob das Aufrunden der Koordinaten zu einer intersection fuehrt.
	@Test
	public void onePolygon_1Boundary_Area_RoundingUp_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
				IomObjectHelper.createMultiplePolylineBoundary(
						IomObjectHelper.createCoord("480000.0004", "70000.0004"),
						IomObjectHelper.createCoord("500000.000", "80000.000"),
						IomObjectHelper.createCoord("550000.000", "90000.000"),
						IomObjectHelper.createCoord("480000.0005", "70000.0005"))));

		LogCollector logger = validateObjects(objSurfaceSuccess);

		// Asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Intersection coord1 (480000.003, 70000.002), tids o1, o1");
	}
	
    @Test
    public void notApolygon_Fail(){
        Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
        objSurfaceSuccess.setattrvalue("area2d", "3");

        LogCollector logger = validateObjects(objSurfaceSuccess);

        // Asserts
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "The value <3> is not a Polygon in attribute area2d");
    }

    @Test
    public void checkWithSurfaceTopologyInvalid() {
        Iom_jObject objectD1InvalidPolygon = new Iom_jObject(ILI_CLASSD, OID1);
        objectD1InvalidPolygon.addattrobj("area2d", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("700000", "90000"),
                        IomObjectHelper.createCoord("750000", "90000"))));

        Iom_jObject objectD2 = new Iom_jObject(ILI_CLASSD, OID2);
        objectD2.addattrobj("area2d", IomObjectHelper.createRectangleGeometry("500000", "70000", "600000", "80000"));

        Iom_jObject objectD3 = new Iom_jObject(ILI_CLASSD, OID3);
        objectD3.addattrobj("area2d", IomObjectHelper.createRectangleGeometry("550000", "75000", "650000", "85000"));

        LogCollector logger = validateObjects(objectD1InvalidPolygon, objectD2, objectD3);

        // Asserts
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "dangle tid o1",
                "no polygon");

        LogCollectorAssertions.AssertAllEventMessages(logger.getInfo(),
                "assume unknown external objects",
                "first validation pass...",
                "AREA topology of attribute area2d not validated, validation of SURFACE topology failed in attribute area2d",
                "second validation pass...");
    }
}
