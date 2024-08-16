package ch.interlis.iox_j.validator;

import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
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
    private TransferDescription td;

    @Before
    public void setUp() {
        Configuration ili2cConfig = new Configuration();
        FileEntry functionIli = new FileEntry("src/test/data/validator/DMAVTYM_Topologie_V1_0.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(functionIli);

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
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.ClassA.linesCoverSurface is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversMissingMultiline() {
        Iom_jObject iomObj = new Iom_jObject(CLASSA, "o1");
        iomObj.addattrobj("surface", IomObjectHelper.createRectangleGeometry("10", "10", "30", "30"));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.ClassA.linesCoverSurface is not true.");
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
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.ClassA.linesCoverSurface is not true.");
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
                "Mandatory Constraint DMAVTYM_Topologie_Function24.Topic.ClassA.linesCoverSurface is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void coversFromXtf() throws IoxException {
        LogCollector logger = ValidatorTestHelper.validateObjectsFromXtf24(td, new File("src/test/data/validator/DMAVTYM_Topologie.xtf"));
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }
}
