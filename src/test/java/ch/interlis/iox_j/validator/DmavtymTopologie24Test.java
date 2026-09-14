package ch.interlis.iox_j.validator;

import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.IoxException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class DmavtymTopologie24Test {
    private final static String MODEL = "DMAVTYM_Topologie_Function24";
    private final static String TOPIC = MODEL + ".Topic";
    private final static String CLASSA = TOPIC + ".ClassA";
    private final static String CLASS_SURFACE = TOPIC + ".SurfaceClass";
    private final static String CLASS_LINE = TOPIC + ".LineClass";
    private final static String CLASS_COMPARE_LINES = TOPIC + ".CompareLinesClass";
    private final static String CLASS_COMPARE_SURFACES = TOPIC + ".CompareSurfacesClass";
    private final static String CLASS_POINT = TOPIC + ".PointClass";
    private final static String CLASS_BAG_OF_LINES = TOPIC + ".BagOfLinesClass";
    private final static String STRUCT_LINE = TOPIC + ".LineStructure";
    private final static String CLASS_BAG_OF_DIRECT_LINES = TOPIC + ".BagOfDirectLinesClass";
    private final static String CLASS_MULTI_SURFACE = TOPIC + ".MultiSurfaceClass";
    private final static String CLASS_BAG_OF_SURFACES = TOPIC + ".BagOfSurfacesClass";
    private final static String STRUCT_SURFACE = TOPIC + ".SurfaceStructure";
    private final static String CLASS_BAG_OF_DIRECT_SURFACES = TOPIC + ".BagOfDirectSurfacesClass";
    private final static String CLASS_MULTI_SURFACE_TOLERANCE = TOPIC + ".MultiSurfaceToleranceClass";
    private final static String CLASS_BAG_OF_SURFACES_TOLERANCE = TOPIC + ".BagOfSurfacesToleranceClass";
    private final static String CLASS_BAG_OF_LINES_TOLERANCE = TOPIC + ".BagOfLinesToleranceClass";
    private final static String CLASS_BAG_OF_DIRECT_SURFACES_TOLERANCE = TOPIC + ".BagOfDirectSurfacesToleranceClass";

    private final static String TOPIC_POINT_IN_POINTS = MODEL + ".PointInPoints";
    private final static String CLASS_POINT_IN_POINTS_TEST = TOPIC_POINT_IN_POINTS + ".TestCase";
    private final static String STRUCT_POINT_IN_POINTS_POINT = TOPIC_POINT_IN_POINTS + ".Point";

    private final static String TOPIC_GEOMETRIC_FILTER = MODEL + ".GeometricFilter";
    private final static String CLASS_GEOMETRIC_FILTER_SURFACE = TOPIC_GEOMETRIC_FILTER + ".FilterSurfaceClass";
    private final static String CLASS_GEOMETRIC_FILTER_TEST = TOPIC_GEOMETRIC_FILTER + ".FilterTestCase";

    private TransferDescription td;

    @Before
    public void setUp() {
        Configuration ili2cConfig = new Configuration();
        FileEntry topologieV1_0_Ili = new FileEntry("src/test/data/validator/DMAVTYM_Topologie_V1_0.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(topologieV1_0_Ili);

        FileEntry topologieV1_1_Ili = new FileEntry("src/test/data/validator/DMAVTYM_Topologie_V1_1.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(topologieV1_1_Ili);

        FileEntry objectPoolIli = new FileEntry("src/test/data/validator/ObjectPool_V1_0.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(objectPoolIli);

        FileEntry elementsIli = new FileEntry("src/test/data/validator/Elements_V1_0.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(elementsIli);

        FileEntry modelIli = new FileEntry("src/test/data/validator/DMAVTYM_Topologie_Function24.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(modelIli);

        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }

    @Test
    public void coversSingleLine() {
        Iom_jObject iomObj = new Iom_jObject(CLASSA, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));
        iomObj.addattrobj("lines", IomObjectHelper.createMultiPolyline(
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("10", "30"),
                        IomObjectHelper.createCoord("30", "30"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversSingleLineReversed() {
        Iom_jObject iomObj = new Iom_jObject(CLASSA, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));
        iomObj.addattrobj("lines", IomObjectHelper.createMultiPolyline(
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("30", "30"),
                        IomObjectHelper.createCoord("10", "30"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversSingleArc() {
        Iom_jObject iomObj = new Iom_jObject(CLASSA, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createArc("22", "22", "10", "30"),
                        IomObjectHelper.createCoord("10", "10"))));
        iomObj.addattrobj("lines", IomObjectHelper.createMultiPolyline(
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createArc("22", "22", "10", "30"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversSingleArcReversed() {
        Iom_jObject iomObj = new Iom_jObject(CLASSA, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createArc("22", "22", "10", "30"),
                        IomObjectHelper.createCoord("10", "10"))));
        iomObj.addattrobj("lines", IomObjectHelper.createMultiPolyline(
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("10", "30"),
                        IomObjectHelper.createArc("22", "22", "30", "10"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversSingleArcDifferentMidPoint() {
        Iom_jObject iomObj = new Iom_jObject(CLASSA, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createArc("23", "19", "10", "30"),
                        IomObjectHelper.createCoord("10", "10"))));
        iomObj.addattrobj("lines", IomObjectHelper.createMultiPolyline(
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createArc("19", "23", "10", "30"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversPolygonWithHolesMultipleLines() {
        Iom_jObject iomObj = new Iom_jObject(CLASSA, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createCoord("30", "15"),
                        IomObjectHelper.createArc("25", "25", "15", "30"),
                        IomObjectHelper.createCoord("10", "30"),
                        IomObjectHelper.createCoord("10", "10")),
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("20", "20"),
                        IomObjectHelper.createCoord("15", "20"),
                        IomObjectHelper.createCoord("15", "25"),
                        IomObjectHelper.createArc("22", "22", "25", "15"),
                        IomObjectHelper.createCoord("20", "15"),
                        IomObjectHelper.createCoord("20", "20"))));
        iomObj.addattrobj("lines", IomObjectHelper.createMultiPolyline(
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("10", "30")),
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("30", "15"),
                        IomObjectHelper.createArc("25", "25", "15", "30")),
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("15", "20"),
                        IomObjectHelper.createCoord("15", "25"),
                        IomObjectHelper.createArc("22", "22", "25", "15")),
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("20", "20"),
                        IomObjectHelper.createCoord("20", "15"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversDuplicateLine() {
        Iom_jObject iomObj = new Iom_jObject(CLASSA, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createArc("20", "27", "10", "30"),
                        IomObjectHelper.createCoord("10", "10"))));
        iomObj.addattrobj("lines", IomObjectHelper.createMultiPolyline(
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("10", "30")),
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("10", "30"),
                        IomObjectHelper.createArc("20", "27", "30", "10")),
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("10", "30"),
                        IomObjectHelper.createArc("27", "20", "30", "10"),
                        IomObjectHelper.createCoord("10", "10"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        LogCollectorAssertions.AssertAllEventMessages(logger.getWarn(),
                "MultiLineAttr contains duplicate line segment: (10.0 10.0, 10.0 30.0).",
                "MultiLineAttr contains duplicate line segment: CIRCULARSTRING (10.0 30.0, 27.0 20.0, 30.0 10.0).",
                "MultiLineAttr contains duplicate line segment: (10.0 10.0, 10.0 30.0).",
                "MultiLineAttr contains duplicate line segment: CIRCULARSTRING (10.0 30.0, 27.0 20.0, 30.0 10.0).");
    }

    @Test
    public void coversMissingdSurface() {
        Iom_jObject iomObj = new Iom_jObject(CLASSA, "o1");
        iomObj.addattrobj("lines", IomObjectHelper.createMultiPolyline(
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("10", "30"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.ClassA.linesCoverSurface_V1_0 is not true.",
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.ClassA.linesCoverSurface_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversMissingMultiline() {
        Iom_jObject iomObj = new Iom_jObject(CLASSA, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.ClassA.linesCoverSurface_V1_0 is not true.",
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.ClassA.linesCoverSurface_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversInvalidSegments() {
        Iom_jObject iomObj = new Iom_jObject(CLASSA, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));
        iomObj.addattrobj("lines", IomObjectHelper.createMultiPolyline(
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("100", "100"),
                        IomObjectHelper.createCoord("142", "142")),
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("100", "100"),
                        IomObjectHelper.createArc("130", "170", "200", "200"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "MultiLineAttr contains unmatched line segment: (100.0 100.0, 142.0 142.0).",
                "MultiLineAttr contains unmatched line segment: CIRCULARSTRING (100.0 100.0, 130.0 170.0, 200.0 200.0).",
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.ClassA.linesCoverSurface_V1_0 is not true.",
                "MultiLineAttr contains unmatched line segment: (100.0 100.0, 142.0 142.0).",
                "MultiLineAttr contains unmatched line segment: CIRCULARSTRING (100.0 100.0, 130.0 170.0, 200.0 200.0).",
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.ClassA.linesCoverSurface_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversCollinearSegment() {
        Iom_jObject iomObj = new Iom_jObject(CLASSA, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));
        iomObj.addattrobj("lines", IomObjectHelper.createMultiPolyline(
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("10", "15"),
                        IomObjectHelper.createCoord("10", "30"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "MultiLineAttr contains unmatched line segment: (10.0 15.0, 10.0 30.0).",
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.ClassA.linesCoverSurface_V1_0 is not true.",
                "MultiLineAttr contains unmatched line segment: (10.0 15.0, 10.0 30.0).",
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.ClassA.linesCoverSurface_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversBagOfLineStructures() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_LINES, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));
        iomObj.addattrobj("lineStructures", createLineStructure(IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "30"),
                IomObjectHelper.createCoord("30", "30"))));
        iomObj.addattrobj("lineStructures", createLineStructure(IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("30", "30"),
                IomObjectHelper.createCoord("30", "10"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversBagOfLineStructuresUnmatched() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_LINES, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));
        iomObj.addattrobj("lineStructures", createLineStructure(IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "30"),
                IomObjectHelper.createCoord("30", "30"))));
        iomObj.addattrobj("lineStructures", createLineStructure(IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("100", "100"),
                IomObjectHelper.createCoord("200", "200"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "MultiLineAttr contains unmatched line segment: (100.0 100.0, 200.0 200.0).",
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.BagOfLinesClass.linesCoverSurface_V1_0 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    private IomObject createLineStructure(IomObject line) {
        Iom_jObject lineStruct = new Iom_jObject(STRUCT_LINE, null);
        lineStruct.addattrobj("line", line);
        return lineStruct;
    }

    @Test
    public void coversBagOfDirectLines() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_DIRECT_LINES, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "30"),
                IomObjectHelper.createCoord("30", "30")));
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("30", "30"),
                IomObjectHelper.createCoord("30", "10")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversBagOfDirectLinesUnmatched() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_DIRECT_LINES, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "30"),
                IomObjectHelper.createCoord("30", "30")));
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("100", "100"),
                IomObjectHelper.createCoord("200", "200")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "MultiLineAttr contains unmatched line segment: (100.0 100.0, 200.0 200.0).",
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.BagOfDirectLinesClass.linesCoverSurface_V1_0 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversEmptyBagOfLineStructures() {
        // BAG {0..*} without elements: the MultiLineObject argument is UNDEFINED, the constraint is skipped
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_LINES, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversMultiSurface() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_MULTI_SURFACE, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createMultiPolygon(
                IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"),
                IomObjectHelper.createRectangleGeometry("50", "50", "70", "70")));
        iomObj.addattrobj("lines", IomObjectHelper.createMultiPolyline(
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("10", "30"),
                        IomObjectHelper.createCoord("30", "30")),
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("50", "70"),
                        IomObjectHelper.createCoord("70", "70"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversMultiSurfaceSecondPolygonOnly() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_MULTI_SURFACE, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createMultiPolygon(
                IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"),
                IomObjectHelper.createRectangleGeometry("50", "50", "70", "70")));
        iomObj.addattrobj("lines", IomObjectHelper.createMultiPolyline(
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("50", "70"),
                        IomObjectHelper.createCoord("70", "70"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversMultiSurfaceUnmatched() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_MULTI_SURFACE, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createMultiPolygon(
                IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"),
                IomObjectHelper.createRectangleGeometry("50", "50", "70", "70")));
        iomObj.addattrobj("lines", IomObjectHelper.createMultiPolyline(
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("10", "30"),
                        IomObjectHelper.createCoord("30", "30")),
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("30", "30"),
                        IomObjectHelper.createCoord("50", "50"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "MultiLineAttr contains unmatched line segment: (30.0 30.0, 50.0 50.0).",
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.MultiSurfaceClass.linesCoverSurface_V1_0 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversBagOfSurfaces() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_SURFACES, "o1");
        iomObj.addattrobj("surfaceStructures", createSurfaceStructure(IomObjectHelper.createRectangleGeometry("10", "10", "30", "30")));
        iomObj.addattrobj("surfaceStructures", createSurfaceStructure(IomObjectHelper.createRectangleGeometry("50", "50", "70", "70")));
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "30"),
                IomObjectHelper.createCoord("30", "30")));
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("50", "70"),
                IomObjectHelper.createCoord("70", "70")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversBagOfSurfacesUnmatched() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_SURFACES, "o1");
        iomObj.addattrobj("surfaceStructures", createSurfaceStructure(IomObjectHelper.createRectangleGeometry("10", "10", "30", "30")));
        iomObj.addattrobj("surfaceStructures", createSurfaceStructure(IomObjectHelper.createRectangleGeometry("50", "50", "70", "70")));
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("50", "70"),
                IomObjectHelper.createCoord("70", "70")));
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("100", "100"),
                IomObjectHelper.createCoord("200", "200")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "MultiLineAttr contains unmatched line segment: (100.0 100.0, 200.0 200.0).",
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.BagOfSurfacesClass.linesCoverSurface_V1_0 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversBagOfDirectSurfaces() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_DIRECT_SURFACES, "o1");
        iomObj.addattrobj("surfaces", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));
        iomObj.addattrobj("surfaces", IomObjectHelper.createRectangleGeometry("50", "50", "70", "70"));
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "30"),
                IomObjectHelper.createCoord("30", "30")));
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("50", "70"),
                IomObjectHelper.createCoord("70", "70")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversBagOfDirectSurfacesUnmatched() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_DIRECT_SURFACES, "o1");
        iomObj.addattrobj("surfaces", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));
        iomObj.addattrobj("surfaces", IomObjectHelper.createRectangleGeometry("50", "50", "70", "70"));
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("30", "30"),
                IomObjectHelper.createCoord("50", "50")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "MultiLineAttr contains unmatched line segment: (30.0 30.0, 50.0 50.0).",
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.BagOfDirectSurfacesClass.linesCoverSurface_V1_0 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversEmptyBagOfSurfaces() {
        // BAG {0..*} without elements: there is no surface that could cover the lines
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_SURFACES, "o1");
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "30"),
                IomObjectHelper.createCoord("30", "30")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.BagOfSurfacesClass.linesCoverSurface_V1_0 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    private IomObject createSurfaceStructure(IomObject surface) {
        Iom_jObject surfaceStruct = new Iom_jObject(STRUCT_SURFACE, null);
        surfaceStruct.addattrobj("surface", surface);
        return surfaceStruct;
    }

    @Test
    public void coversFromXtf() throws IoxException {
        LogCollector logger = ValidatorTestHelper.validateObjectsFromXtf24(td, new File("src/test/data/validator/DMAVTYM_Topologie.xtf"));
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithTolerance() {
        Iom_jObject surface1 = new Iom_jObject(CLASS_SURFACE, "o1");
        surface1.addattrobj("surface", IomObjectHelper.createRectangleGeometry("100", "100", "130", "130"));

        Iom_jObject line = new Iom_jObject(CLASS_LINE, "o2");
        line.addattrobj("line", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10.001", "10"),
                IomObjectHelper.createCoord("9.999", "30.001"),
                IomObjectHelper.createCoord("29.999", "30")));

        Iom_jObject surface2 = new Iom_jObject(CLASS_SURFACE, "o3");
        surface2.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, surface1, line, surface2);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceLineOutsideTolerance() {
        Iom_jObject surface1 = new Iom_jObject(CLASS_SURFACE, "o1");
        surface1.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));

        Iom_jObject line = new Iom_jObject(CLASS_LINE, "o2");
        line.addattrobj("line", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10.003", "15"),
                IomObjectHelper.createCoord("9.999", "30.001"),
                IomObjectHelper.createCoord("20", "30")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, surface1, line);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.LineClass.lineCoversSurface_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceMissingSurface() {
        Iom_jObject line = new Iom_jObject(CLASS_LINE, "o1");
        line.addattrobj("line", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10.003", "15"),
                IomObjectHelper.createCoord("9.999", "30.001")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, line);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.LineClass.lineCoversSurface_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceMissingLine() {
        Iom_jObject surface = new Iom_jObject(CLASS_SURFACE, "o1");
        surface.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));

        Iom_jObject line = new Iom_jObject(CLASS_LINE, "o2");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, surface, line);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.LineClass.lineCoversSurface_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceArc() {
        Iom_jObject surface = new Iom_jObject(CLASS_SURFACE, "o1");
        surface.addattrobj("surface", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createArc("23", "19", "10", "30"),
                        IomObjectHelper.createCoord("10", "10"))));

        Iom_jObject line = new Iom_jObject(CLASS_LINE, "o2");
        line.addattrobj("line", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("30", "10"),
                IomObjectHelper.createArc("23.001", "19.001", "10", "30")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, surface, line);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceInterior() {
        Iom_jObject surface = new Iom_jObject(CLASS_SURFACE, "o1");
        surface.addattrobj("surface", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createRectangleBoundary("10", "10", "30", "30"),
                IomObjectHelper.createRectangleBoundary("20", "20", "25", "25")));

        Iom_jObject line = new Iom_jObject(CLASS_LINE, "o2");
        line.addattrobj("line", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("20.001", "20"),
                IomObjectHelper.createCoord("25", "19.999")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, surface, line);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceLineTooShort() {
        Iom_jObject surface = new Iom_jObject(CLASS_SURFACE, "o1");
        surface.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));

        Iom_jObject line = new Iom_jObject(CLASS_LINE, "o2");
        line.addattrobj("line", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "10"),
                IomObjectHelper.createCoord("10", "29")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, surface, line);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.LineClass.lineCoversSurface_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceDifferentArcs() {
        Iom_jObject surface = new Iom_jObject(CLASS_SURFACE, "o1");
        surface.addattrobj("surface", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createArc("23", "19", "10", "30"),
                        IomObjectHelper.createCoord("10", "10"))));

        Iom_jObject line = new Iom_jObject(CLASS_LINE, "o2");
        line.addattrobj("line", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("30", "10"),
                IomObjectHelper.createArc("23.005", "19", "10", "30")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, surface, line);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.LineClass.lineCoversSurface_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceMultiSurface() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_MULTI_SURFACE_TOLERANCE, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createMultiPolygon(
                IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"),
                IomObjectHelper.createRectangleGeometry("50", "50", "70", "70")));
        iomObj.addattrobj("lines", IomObjectHelper.createMultiPolyline(
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("50.001", "70"),
                        IomObjectHelper.createCoord("70", "69.999"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceMultiSurfaceUnmatched() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_MULTI_SURFACE_TOLERANCE, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createMultiPolygon(
                IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"),
                IomObjectHelper.createRectangleGeometry("50", "50", "70", "70")));
        iomObj.addattrobj("lines", IomObjectHelper.createMultiPolyline(
                IomObjectHelper.createPolyline(
                        IomObjectHelper.createCoord("30", "30"),
                        IomObjectHelper.createCoord("50", "50"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.MultiSurfaceToleranceClass.linesCoverSurface_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceBagOfSurfaces() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_SURFACES_TOLERANCE, "o1");
        iomObj.addattrobj("surfaceStructures", createSurfaceStructure(IomObjectHelper.createRectangleGeometry("10", "10", "30", "30")));
        iomObj.addattrobj("surfaceStructures", createSurfaceStructure(IomObjectHelper.createRectangleGeometry("50", "50", "70", "70")));
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "30.001"),
                IomObjectHelper.createCoord("30", "30")));
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("50", "70"),
                IomObjectHelper.createCoord("69.999", "70")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceBagOfSurfacesUnmatched() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_SURFACES_TOLERANCE, "o1");
        iomObj.addattrobj("surfaceStructures", createSurfaceStructure(IomObjectHelper.createRectangleGeometry("10", "10", "30", "30")));
        iomObj.addattrobj("surfaceStructures", createSurfaceStructure(IomObjectHelper.createRectangleGeometry("50", "50", "70", "70")));
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("50", "70"),
                IomObjectHelper.createCoord("70", "70")));
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("100", "100"),
                IomObjectHelper.createCoord("200", "200")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.BagOfSurfacesToleranceClass.linesCoverSurface_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceBagOfDirectSurfaces() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_DIRECT_SURFACES_TOLERANCE, "o1");
        iomObj.addattrobj("surfaces", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));
        iomObj.addattrobj("surfaces", IomObjectHelper.createRectangleGeometry("50", "50", "70", "70"));
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "30.001"),
                IomObjectHelper.createCoord("30", "30")));
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("50", "70"),
                IomObjectHelper.createCoord("69.999", "70")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceBagOfDirectSurfacesUnmatched() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_DIRECT_SURFACES_TOLERANCE, "o1");
        iomObj.addattrobj("surfaces", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));
        iomObj.addattrobj("surfaces", IomObjectHelper.createRectangleGeometry("50", "50", "70", "70"));
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("30", "30"),
                IomObjectHelper.createCoord("50", "50")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.BagOfDirectSurfacesToleranceClass.linesCoverSurface_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceEmptyBagOfSurfaces() {
        // BAG {0..*} without elements: there is no surface that could cover the lines
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_SURFACES_TOLERANCE, "o1");
        iomObj.addattrobj("lines", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "30"),
                IomObjectHelper.createCoord("30", "30")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.BagOfSurfacesToleranceClass.linesCoverSurface_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceBagOfLineStructures() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_LINES_TOLERANCE, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));
        iomObj.addattrobj("lineStructures", createLineStructure(IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "30.001"),
                IomObjectHelper.createCoord("30", "30"))));
        iomObj.addattrobj("lineStructures", createLineStructure(IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("30", "30"),
                IomObjectHelper.createCoord("29.999", "10"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceBagOfLineStructuresOnDifferentRings() {
        // every line structure is checked on its own: one lies on the outer, one on the inner ring
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_LINES_TOLERANCE, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createRectangleBoundary("10", "10", "30", "30"),
                IomObjectHelper.createRectangleBoundary("15", "15", "20", "20")));
        iomObj.addattrobj("lineStructures", createLineStructure(IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "30"),
                IomObjectHelper.createCoord("30", "30"))));
        iomObj.addattrobj("lineStructures", createLineStructure(IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("15", "15"),
                IomObjectHelper.createCoord("15", "20"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceBagOfLineStructuresUnmatched() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_LINES_TOLERANCE, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));
        iomObj.addattrobj("lineStructures", createLineStructure(IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "30"),
                IomObjectHelper.createCoord("30", "30"))));
        iomObj.addattrobj("lineStructures", createLineStructure(IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("100", "100"),
                IomObjectHelper.createCoord("200", "200"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.BagOfLinesToleranceClass.linesCoverSurface_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceEmptyBagOfLineStructures() {
        // BAG {0..*} without elements: the MultiLineObject argument is UNDEFINED, the constraint is skipped
        Iom_jObject iomObj = new Iom_jObject(CLASS_BAG_OF_LINES_TOLERANCE, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversWithToleranceFromXtf() throws IoxException {
        LogCollector logger = ValidatorTestHelper.validateObjectsFromXtf24(td, new File("src/test/data/validator/DMAVTYM_Topologie_Tolerance.xtf"));
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversLineWithTolerance() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_COMPARE_LINES, "o1");
        iomObj.addattrobj("line1", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "10"),
                IomObjectHelper.createCoord("10", "30"),
                IomObjectHelper.createCoord("30", "30"),
                IomObjectHelper.createCoord("30", "10")));
        iomObj.addattrobj("line2", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10.001", "10"),
                IomObjectHelper.createCoord("9.999", "30.001"),
                IomObjectHelper.createCoord("29.999", "30")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversLineWithToleranceMissingSegment() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_COMPARE_LINES, "o1");
        iomObj.addattrobj("line1", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10.001", "10"),
                IomObjectHelper.createCoord("10", "30"),
                IomObjectHelper.createCoord("30", "30")));
        iomObj.addattrobj("line2", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "10"),
                IomObjectHelper.createCoord("9.999", "30.001"),
                IomObjectHelper.createCoord("29.999", "30"),
                IomObjectHelper.createCoord("30", "10")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.CompareLinesClass.lineCoversLine_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversLineWithToleranceArc() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_COMPARE_LINES, "o1");
        iomObj.addattrobj("line1", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "10"),
                IomObjectHelper.createCoord("30", "10"),
                IomObjectHelper.createArc("23", "19", "10", "30"),
                IomObjectHelper.createCoord("30", "30")));

        iomObj.addattrobj("line2", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("29.999", "10"),
                IomObjectHelper.createArc("23.001", "19.001", "10", "30")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversLineArcDifferentMidPoint() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_COMPARE_LINES, "o1");
        iomObj.addattrobj("line1", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "10"),
                IomObjectHelper.createCoord("30", "10"),
                IomObjectHelper.createArc("23", "19", "10", "30")));
        iomObj.addattrobj("line2", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("30", "10"),
                IomObjectHelper.createArc("19", "23.001", "10", "30")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void geometryEqualityRectangle() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_COMPARE_SURFACES, "o1");
        iomObj.setattrvalue("tolerance", "0.002");
        iomObj.addattrobj("surface1", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));
        iomObj.addattrobj("surface2", IomObjectHelper.createRectangleGeometry("10", "9.999", "30", "30.001"));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void geometryEqualityDifferentStartPoint() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_COMPARE_SURFACES, "o1");
        iomObj.setattrvalue("tolerance", "0.002");
        iomObj.addattrobj("surface1", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "30"),
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createCoord("30", "30"),
                        IomObjectHelper.createCoord("10", "30"))));
        iomObj.addattrobj("surface2", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "9.999"),
                        IomObjectHelper.createCoord("30", "10.001"),
                        IomObjectHelper.createCoord("30", "30.001"),
                        IomObjectHelper.createCoord("10", "30"),
                        IomObjectHelper.createCoord("10", "9.999"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void geometryEqualityArcs() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_COMPARE_SURFACES, "o1");
        iomObj.setattrvalue("tolerance", "0.002");
        iomObj.addattrobj("surface1", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createArc("23", "19", "10", "30"),
                        IomObjectHelper.createCoord("10", "10"))));
        iomObj.addattrobj("surface2", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "9.999"),
                        IomObjectHelper.createCoord("30", "10.001"),
                        IomObjectHelper.createArc("23.001", "19", "10", "30"),
                        IomObjectHelper.createCoord("10", "9.999"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void geometryEqualityDifferentArcs() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_COMPARE_SURFACES, "o1");
        iomObj.setattrvalue("tolerance", "0.002");
        iomObj.addattrobj("surface1", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createArc("23", "19", "10", "30"),
                        IomObjectHelper.createCoord("10", "10"))));
        iomObj.addattrobj("surface2", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createArc("19", "19", "10", "30"),
                        IomObjectHelper.createCoord("10", "10"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.CompareSurfacesClass.geometrySameControlPoints_V1_1 is not true.",
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.CompareSurfacesClass.geometrySpatiallyEquals_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void geometryEqualityAdditionalPoint() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_COMPARE_SURFACES, "o1");
        iomObj.setattrvalue("tolerance", "0.002");
        iomObj.addattrobj("surface1", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "30"),
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createCoord("30", "30"),
                        IomObjectHelper.createCoord("10", "30"))));
        iomObj.addattrobj("surface2", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "30"),
                        IomObjectHelper.createCoord("10", "20"),
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createCoord("30", "30"),
                        IomObjectHelper.createCoord("10", "30"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.CompareSurfacesClass.geometrySameControlPoints_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void geometryEqualityDifferentInnerRings() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_COMPARE_SURFACES, "o1");
        iomObj.setattrvalue("tolerance", "0.002");
        iomObj.addattrobj("surface1", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createRectangleBoundary("10", "10", "30", "30"),
                IomObjectHelper.createRectangleBoundary("20", "20", "25", "25")));
        iomObj.addattrobj("surface2", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createRectangleBoundary("10", "10", "30", "30"),
                IomObjectHelper.createRectangleBoundary("22", "20", "25", "25")));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.CompareSurfacesClass.geometrySameControlPoints_V1_1 is not true.",
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.CompareSurfacesClass.geometrySpatiallyEquals_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void pointIsInsideSurface() {
        Iom_jObject point1 = new Iom_jObject(CLASS_POINT, "o1");
        point1.addattrobj("point", IomObjectHelper.createCoord("22", "20"));
        Iom_jObject point2 = new Iom_jObject(CLASS_POINT, "o2");
        point2.addattrobj("point", IomObjectHelper.createCoord("30", "25"));
        Iom_jObject point3 = new Iom_jObject(CLASS_POINT, "o3");
        point3.addattrobj("point", IomObjectHelper.createCoord("30", "30"));

        Iom_jObject surface = new Iom_jObject(CLASS_SURFACE, "o4");
        surface.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, point1, point2, point3, surface);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void pointIsInsideSurfaceOutside() {
        Iom_jObject point = new Iom_jObject(CLASS_POINT, "o1");
        point.addattrobj("point", IomObjectHelper.createCoord("30.001", "20"));

        Iom_jObject surface = new Iom_jObject(CLASS_SURFACE, "o2");
        surface.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, point, surface);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.PointClass.pointIsInsideSurface_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void pointIsInsideSurfaceInsideTolerance() {
        Iom_jObject point = new Iom_jObject(CLASS_POINT, "o1");
        point.addattrobj("point", IomObjectHelper.createCoord("10.003", "10.002"));

        Iom_jObject surface = new Iom_jObject(CLASS_SURFACE, "o2");
        surface.addattrobj("surface", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createCoord("30", "20"),
                        IomObjectHelper.createCoord("10", "10"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, point, surface);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void pointIsInsideSurfaceOutsideTolerance() {
        Iom_jObject point = new Iom_jObject(CLASS_POINT, "o1");
        point.addattrobj("point", IomObjectHelper.createCoord("10.004", "10.003"));

        Iom_jObject surface = new Iom_jObject(CLASS_SURFACE, "o2");
        surface.addattrobj("surface", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createCoord("30", "20"),
                        IomObjectHelper.createCoord("10", "10"))));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, point, surface);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.PointClass.pointIsInsideSurface_V1_1 is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void pointInPoints() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_POINT_IN_POINTS_TEST, "test1");
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("10", "10"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("20", "20"));
        iomObj.setattrvalue("attribute", "pointAttr");
        iomObj.addattrobj("pointAttr", IomObjectHelper.createCoord("10.00049", "9.9995"));
        iomObj.setattrvalue("expected", "true");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_POINT_IN_POINTS, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void pointInPointsNoMatch() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_POINT_IN_POINTS_TEST, "test1");
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("99", "99"));
        iomObj.setattrvalue("attribute", "pointAttr");
        iomObj.addattrobj("pointAttr", IomObjectHelper.createCoord("10", "10"));
        iomObj.setattrvalue("expected", "false");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_POINT_IN_POINTS, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void pointInPointsSurface() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_POINT_IN_POINTS_TEST, "test1");
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("10", "10"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("10", "30"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("30", "30"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("30", "10"));
        iomObj.setattrvalue("attribute", "surfaceAttr");
        iomObj.addattrobj("surfaceAttr", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));
        iomObj.setattrvalue("expected", "true");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_POINT_IN_POINTS, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void pointInPointsSurfaceWithArcs() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_POINT_IN_POINTS_TEST, "test1");
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("10", "10"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("10", "30"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("30", "10"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("15", "15"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("15", "25"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("25", "15"));
        iomObj.setattrvalue("attribute", "surfaceAttr");
        iomObj.addattrobj("surfaceAttr", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createArc("23", "19", "10", "30"),
                        IomObjectHelper.createCoord("10", "10")),
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("15", "15"),
                        IomObjectHelper.createCoord("25", "15"),
                        IomObjectHelper.createArc("21", "21", "15", "25"),
                        IomObjectHelper.createCoord("15", "15"))));
        iomObj.setattrvalue("expected", "true");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_POINT_IN_POINTS, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void pointInPointsSurfaceWithHoles() {
        // Coordinates from the hole are not in the reference points therefore expected is false
        Iom_jObject iomObj = new Iom_jObject(CLASS_POINT_IN_POINTS_TEST, "test1");
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("10", "10"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("10", "30"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("30", "30"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("30", "10"));
        iomObj.setattrvalue("attribute", "surfaceAttr");
        iomObj.addattrobj("surfaceAttr", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createRectangleBoundary("10", "10", "30", "30"),
                IomObjectHelper.createRectangleBoundary("15", "15", "25", "25")));
        iomObj.setattrvalue("expected", "false");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_POINT_IN_POINTS, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void pointInPointsPolyline() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_POINT_IN_POINTS_TEST, "test1");
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("10", "10"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("20", "42"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("30", "99"));
        iomObj.setattrvalue("attribute", "lineAttr");
        iomObj.addattrobj("lineAttr", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "10"),
                IomObjectHelper.createCoord("20", "42"),
                IomObjectHelper.createCoord("30", "99")));
        iomObj.setattrvalue("expected", "true");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_POINT_IN_POINTS, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void pointInPointsPolylineWithArcs() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_POINT_IN_POINTS_TEST, "test1");
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("10", "10"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("10", "30"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("30", "10"));
        iomObj.setattrvalue("attribute", "lineAttr");
        iomObj.addattrobj("lineAttr", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("10", "10"),
                IomObjectHelper.createCoord("30", "10"),
                IomObjectHelper.createArc("23", "19", "10", "30")));
        iomObj.setattrvalue("expected", "true");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_POINT_IN_POINTS, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void pointInPointsMultiPolyline() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_POINT_IN_POINTS_TEST, "test1");
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("10", "10"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("20", "42"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("10", "50"));
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("20", "99"));
        iomObj.setattrvalue("attribute", "multiLineAttr");
        iomObj.addattrobj("multiLineAttr", IomObjectHelper.createMultiPolyline(
                IomObjectHelper.createPolyline(
                    IomObjectHelper.createCoord("10", "10"),
                    IomObjectHelper.createCoord("20", "42")),
                IomObjectHelper.createPolyline(
                    IomObjectHelper.createCoord("10", "50"),
                    IomObjectHelper.createCoord("20", "99"))));
        iomObj.setattrvalue("expected", "true");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_POINT_IN_POINTS, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void pointInPointsUndefinedReferencePoints() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_POINT_IN_POINTS_TEST, "test1");
        iomObj.setattrvalue("attribute", "pointAttr");
        iomObj.addattrobj("pointAttr", IomObjectHelper.createCoord("10", "10"));
        iomObj.setattrvalue("expected", "false");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_POINT_IN_POINTS, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void pointInPointsUndefinedInputPoints() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_POINT_IN_POINTS_TEST, "test1");
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("10", "10"));
        iomObj.setattrvalue("attribute", "pointAttr");
        iomObj.setattrvalue("expected", "true");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_POINT_IN_POINTS, iomObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void pointInPointsFail() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_POINT_IN_POINTS_TEST, "test1");
        iomObj.addattrobj("referencePoints", createPointInPointsPoint("99", "99"));
        iomObj.setattrvalue("attribute", "pointAttr");
        iomObj.addattrobj("pointAttr", IomObjectHelper.createCoord("0", "0"));
        iomObj.setattrvalue("expected", "true");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_POINT_IN_POINTS, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.PointInPoints.TestCase.pointInPoints is not true.",
                "Mandatory Constraint DMAVTYM_Topologie_Function24.PointInPoints.TestCase.pointInPointsWithValuesOfPath is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    private IomObject createPointInPointsPoint(String x, String y) {
        Iom_jObject point = new Iom_jObject(STRUCT_POINT_IN_POINTS_POINT, null);
        point.addattrobj("geometry", IomObjectHelper.createCoord(x, y));
        return point;
    }

    @Test
    public void geometricFilterPointInsideSingleSurface() {
        // Create a surface object
        Iom_jObject surfaceObj = new Iom_jObject(CLASS_GEOMETRIC_FILTER_SURFACE, "s1");
        surfaceObj.setattrvalue("id", "Surface1");
        surfaceObj.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));

        // Create a test with a point inside the surface - should match 1 object
        Iom_jObject testObj = new Iom_jObject(CLASS_GEOMETRIC_FILTER_TEST, "test1");
        testObj.addattrobj("testPoint", IomObjectHelper.createCoord("20", "20"));
        testObj.setattrvalue("expectedCount", "1");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_GEOMETRIC_FILTER, surfaceObj, testObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void geometricFilterPointOutsideSurface() {
        // Create a surface object
        Iom_jObject surfaceObj = new Iom_jObject(CLASS_GEOMETRIC_FILTER_SURFACE, "s1");
        surfaceObj.setattrvalue("id", "Surface1");
        surfaceObj.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));

        // Create a test with a point outside the surface - should match 0 objects
        Iom_jObject testObj = new Iom_jObject(CLASS_GEOMETRIC_FILTER_TEST, "test1");
        testObj.addattrobj("testPoint", IomObjectHelper.createCoord("50", "50"));
        testObj.setattrvalue("expectedCount", "0");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_GEOMETRIC_FILTER, surfaceObj, testObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void geometricFilterPointOnBoundary() {
        // Create a surface object
        Iom_jObject surfaceObj = new Iom_jObject(CLASS_GEOMETRIC_FILTER_SURFACE, "s1");
        surfaceObj.setattrvalue("id", "Surface1");
        surfaceObj.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));

        // Create a test with a point on the boundary - should match 1 object (with tolerance)
        Iom_jObject testObj = new Iom_jObject(CLASS_GEOMETRIC_FILTER_TEST, "test1");
        testObj.addattrobj("testPoint", IomObjectHelper.createCoord("10", "20"));
        testObj.setattrvalue("expectedCount", "1");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_GEOMETRIC_FILTER, surfaceObj, testObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void geometricFilterPointInsideMultipleSurfaces() {
        // Create multiple surface objects
        Iom_jObject surface1 = new Iom_jObject(CLASS_GEOMETRIC_FILTER_SURFACE, "s1");
        surface1.setattrvalue("id", "Surface1");
        surface1.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "50", "50"));

        Iom_jObject surface2 = new Iom_jObject(CLASS_GEOMETRIC_FILTER_SURFACE, "s2");
        surface2.setattrvalue("id", "Surface2");
        surface2.addattrobj("surface", IomObjectHelper.createRectangleGeometry("20", "20", "60", "60"));

        Iom_jObject surface3 = new Iom_jObject(CLASS_GEOMETRIC_FILTER_SURFACE, "s3");
        surface3.setattrvalue("id", "Surface3");
        surface3.addattrobj("surface", IomObjectHelper.createRectangleGeometry("100", "100", "150", "150"));

        // Point at (30, 30) is inside surface1 and surface2 but not surface3
        Iom_jObject testObj = new Iom_jObject(CLASS_GEOMETRIC_FILTER_TEST, "test1");
        testObj.addattrobj("testPoint", IomObjectHelper.createCoord("30", "30"));
        testObj.setattrvalue("expectedCount", "2");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_GEOMETRIC_FILTER, surface1, surface2, surface3, testObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void geometricFilterPointOutsideAllSurfaces() {
        // Create multiple surface objects
        Iom_jObject surface1 = new Iom_jObject(CLASS_GEOMETRIC_FILTER_SURFACE, "s1");
        surface1.setattrvalue("id", "Surface1");
        surface1.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));

        Iom_jObject surface2 = new Iom_jObject(CLASS_GEOMETRIC_FILTER_SURFACE, "s2");
        surface2.setattrvalue("id", "Surface2");
        surface2.addattrobj("surface", IomObjectHelper.createRectangleGeometry("40", "40", "60", "60"));

        // Point at (100, 100) is outside all surfaces
        Iom_jObject testObj = new Iom_jObject(CLASS_GEOMETRIC_FILTER_TEST, "test1");
        testObj.addattrobj("testPoint", IomObjectHelper.createCoord("100", "100"));
        testObj.setattrvalue("expectedCount", "0");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_GEOMETRIC_FILTER, surface1, surface2, testObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void geometricFilterNoSurfaces() {
        // Create a test with no surface objects - should match 0 objects
        Iom_jObject testObj = new Iom_jObject(CLASS_GEOMETRIC_FILTER_TEST, "test1");
        testObj.addattrobj("testPoint", IomObjectHelper.createCoord("20", "20"));
        testObj.setattrvalue("expectedCount", "0");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_GEOMETRIC_FILTER, testObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void geometricFilterSurfaceWithArc() {
        // Create a surface with an arc
        Iom_jObject surfaceObj = new Iom_jObject(CLASS_GEOMETRIC_FILTER_SURFACE, "s1");
        surfaceObj.setattrvalue("id", "Surface1");
        surfaceObj.addattrobj("surface", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("10", "10"),
                        IomObjectHelper.createCoord("30", "10"),
                        IomObjectHelper.createArc("22", "22", "10", "30"),
                        IomObjectHelper.createCoord("10", "10"))));

        // Point inside the curved surface
        Iom_jObject testObj = new Iom_jObject(CLASS_GEOMETRIC_FILTER_TEST, "test1");
        testObj.addattrobj("testPoint", IomObjectHelper.createCoord("15", "15"));
        testObj.setattrvalue("expectedCount", "1");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_GEOMETRIC_FILTER, surfaceObj, testObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void geometricFilterSurfaceWithHole() {
        // Create a surface with a hole (outer boundary and inner boundary)
        Iom_jObject surfaceObj = new Iom_jObject(CLASS_GEOMETRIC_FILTER_SURFACE, "s1");
        surfaceObj.setattrvalue("id", "Surface1");
        surfaceObj.addattrobj("surface", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createRectangleBoundary("10", "10", "50", "50"),
                IomObjectHelper.createRectangleBoundary("20", "20", "40", "40")));

        // Point inside the hole - should not match
        Iom_jObject testObj1 = new Iom_jObject(CLASS_GEOMETRIC_FILTER_TEST, "test1");
        testObj1.addattrobj("testPoint", IomObjectHelper.createCoord("30", "30"));
        testObj1.setattrvalue("expectedCount", "0");

        // Point in the surface but outside the hole - should match
        Iom_jObject testObj2 = new Iom_jObject(CLASS_GEOMETRIC_FILTER_TEST, "test2");
        testObj2.addattrobj("testPoint", IomObjectHelper.createCoord("15", "15"));
        testObj2.setattrvalue("expectedCount", "1");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_GEOMETRIC_FILTER, surfaceObj, testObj1, testObj2);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void geometricFilterPointAtCorner() {
        // Create a surface object
        Iom_jObject surfaceObj = new Iom_jObject(CLASS_GEOMETRIC_FILTER_SURFACE, "s1");
        surfaceObj.setattrvalue("id", "Surface1");
        surfaceObj.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));

        // Create a test with a point at the corner - should match 1 object (with tolerance)
        Iom_jObject testObj = new Iom_jObject(CLASS_GEOMETRIC_FILTER_TEST, "test1");
        testObj.addattrobj("testPoint", IomObjectHelper.createCoord("10", "10"));
        testObj.setattrvalue("expectedCount", "1");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_GEOMETRIC_FILTER, surfaceObj, testObj);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void geometricFilterMultiSurface() {
        Iom_jObject surfaceObj = new Iom_jObject(CLASS_GEOMETRIC_FILTER_SURFACE, "s1");
        surfaceObj.setattrvalue("id", "MultiSurface1");
        surfaceObj.addattrobj("surface", IomObjectHelper.createMultiPolygon(
                IomObjectHelper.createPolygonFromBoundaries(
                        IomObjectHelper.createRectangleBoundary("10", "10", "70", "70"),
                        IomObjectHelper.createRectangleBoundary("20", "20", "60", "60")),
                IomObjectHelper.createRectangleGeometry("30", "30", "50", "50")));

        // Point inside the multi-polygon
        Iom_jObject testObj1 = new Iom_jObject(CLASS_GEOMETRIC_FILTER_TEST, "test1");
        testObj1.addattrobj("testPoint", IomObjectHelper.createCoord("40", "40"));
        testObj1.setattrvalue("expectedCount", "1");

        // Point outside the multi-polygon
        Iom_jObject testObj2 = new Iom_jObject(CLASS_GEOMETRIC_FILTER_TEST, "test2");
        testObj2.addattrobj("testPoint", IomObjectHelper.createCoord("25", "25"));
        testObj2.setattrvalue("expectedCount", "0");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_GEOMETRIC_FILTER, surfaceObj, testObj1, testObj2);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }
}
