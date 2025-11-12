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

    private final static String TOPIC_POINT_IN_POINTS = MODEL + ".PointInPoints";
    private final static String CLASS_POINT_IN_POINTS_TEST = TOPIC_POINT_IN_POINTS + ".TestCase";
    private final static String STRUCT_POINT_IN_POINTS_POINT = TOPIC_POINT_IN_POINTS + ".Point";

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
}
