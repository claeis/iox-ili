package ch.interlis.iox_j.validator;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;

/**
 * EXISTENCE CONSTRAINT of a POLYLINE/SURFACE/AREA attribute against a COORD attribute
 */
public class ExistenceConstraintsGeometry23Test {

    private static final String ILI_FILE = "src/test/data/validator/ExistenceConstraintsGeometry23.ili";
    private static final String TOPIC = "ExistenceConstraintsGeometry23.Topic";
    private static final String PUNKT = TOPIC + ".Punkt";
    private static final String PUNKT_ERWEITERT = TOPIC + ".PunktErweitert";
    private static final String PUNKT3D = TOPIC + ".Punkt3d";
    private static final String LINIE = TOPIC + ".Linie";
    private static final String LINIE3D = TOPIC + ".Linie3d";
    private static final String LINIE_DOMAIN = TOPIC + ".LinieDomain";
    private static final String LINIE_ALTERNATIV = TOPIC + ".LinieAlternativ";
    private static final String FLAECHE = TOPIC + ".Flaeche";
    private static final String GEBIET = TOPIC + ".Gebiet";

    private TransferDescription td = null;

    @Before
    public void setUp() throws Exception {
        td = ValidatorTestHelper.compileIliFile(ILI_FILE);
    }

    private static IomObject createObject(String tag, String oid, IomObject geometry) {
        Iom_jObject obj = new Iom_jObject(tag, oid);
        obj.addattrobj("Geometrie", geometry);
        return obj;
    }

    private static IomObject punkt(String oid, String c1, String c2) {
        return createObject(PUNKT, oid, IomObjectHelper.createCoord(c1, c2));
    }

    private static IomObject punktErweitert(String oid, String c1, String c2) {
        return createObject(PUNKT_ERWEITERT, oid, IomObjectHelper.createCoord(c1, c2));
    }

    private static IomObject punkt3d(String oid, String c1, String c2, String c3) {
        return createObject(PUNKT3D, oid, IomObjectHelper.createCoord(c1, c2, c3));
    }

    private static IomObject linie(String oid, IomObject... segments) {
        return createObject(LINIE, oid, IomObjectHelper.createPolyline(segments));
    }

    private static IomObject linieAlternativ(String oid, IomObject geometry, IomObject referenz) {
        IomObject obj = createObject(LINIE_ALTERNATIV, oid, geometry);
        if (referenz != null) {
            obj.addattrobj("Referenz", referenz);
        }
        return obj;
    }

    private static void assertNoErrors(LogCollector logger) {
        assertEquals(logger.getErrs().toString(), 0, logger.getErrs().size());
    }

    private static void assertSingleError(LogCollector logger, String expectedMsg) {
        assertEquals(logger.getErrs().toString(), 1, logger.getErrs().size());
        assertEquals(expectedMsg, logger.getErrs().get(0).getEventMsg());
    }

    // Data of the issue: line with 5 vertices, 6 points of which 5 are the vertices.
    @Test
    public void polylineAllVerticesExistInSeveralPoints_Ok() {
        IomObject line = linie("l1",
                IomObjectHelper.createCoord("500000.000", "100000.000"),
                IomObjectHelper.createCoord("500100.000", "100050.000"),
                IomObjectHelper.createCoord("500200.000", "100020.000"),
                IomObjectHelper.createCoord("500300.000", "100000.000"),
                IomObjectHelper.createCoord("500400.000", "100010.000"));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                line,
                punkt("p1", "500100.000", "100050.000"),
                punkt("p2", "500200.000", "100020.000"),
                punkt("p3", "500999.000", "100999.000"),
                punkt("p4", "500300.000", "100000.000"),
                punkt("p5", "500000.000", "100000.000"),
                punkt("p6", "500400.000", "100010.000"));
        assertNoErrors(logger);
    }

    @Test
    public void polylineOneVertexMissing_Fail() {
        IomObject line = linie("l1",
                IomObjectHelper.createCoord("500000.000", "100000.000"),
                IomObjectHelper.createCoord("500100.000", "100050.000"),
                IomObjectHelper.createCoord("500200.000", "100020.000"),
                IomObjectHelper.createCoord("500300.000", "100000.000"),
                IomObjectHelper.createCoord("500400.000", "100010.000"));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                line,
                punkt("p1", "500100.000", "100050.000"),
                punkt("p2", "500200.000", "100020.000"),
                punkt("p3", "500999.000", "100999.000"),
                punkt("p5", "500000.000", "100000.000"),
                punkt("p6", "500400.000", "100010.000"));
        assertSingleError(logger, "Existence constraint ExistenceConstraintsGeometry23.Topic.Linie.Constraint1 is violated! The value of the attribute Geometrie of ExistenceConstraintsGeometry23.Topic.Linie was not found in the condition class.");
    }

    @Test
    public void twoPolylinesOneWithMissingVertex_Fail() {
        IomObject okLine = linie("l1",
                IomObjectHelper.createCoord("500000.000", "100000.000"),
                IomObjectHelper.createCoord("500100.000", "100050.000"));
        IomObject badLine = linie("l2",
                IomObjectHelper.createCoord("500100.000", "100050.000"),
                IomObjectHelper.createCoord("500200.000", "100020.000"));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                okLine,
                badLine,
                punkt("p1", "500000.000", "100000.000"),
                punkt("p2", "500100.000", "100050.000"));
        assertSingleError(logger, "Existence constraint ExistenceConstraintsGeometry23.Topic.Linie.Constraint1 is violated! The value of the attribute Geometrie of ExistenceConstraintsGeometry23.Topic.Linie was not found in the condition class.");
    }

    // The intermediate point of an arc is not a control point and does not need to exist in the condition class.
    @Test
    public void polylineWithArcAllControlPointsExist_Ok() {
        IomObject line = linie("l1",
                IomObjectHelper.createCoord("500000.000", "100000.000"),
                IomObjectHelper.createArc("500010.000", "100010.000", "500020.000", "100000.000"),
                IomObjectHelper.createCoord("500030.000", "100000.000"));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                line,
                punkt("p1", "500000.000", "100000.000"),
                punkt("p3", "500020.000", "100000.000"),
                punkt("p4", "500030.000", "100000.000"));
        assertNoErrors(logger);
    }

    @Test
    public void polylineWithArcEndPointMissing_Fail() {
        IomObject line = linie("l1",
                IomObjectHelper.createCoord("500000.000", "100000.000"),
                IomObjectHelper.createArc("500010.000", "100010.000", "500020.000", "100000.000"),
                IomObjectHelper.createCoord("500030.000", "100000.000"));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                line,
                punkt("p1", "500000.000", "100000.000"),
                punkt("p2", "500010.000", "100010.000"),
                punkt("p4", "500030.000", "100000.000"));
        assertSingleError(logger, "Existence constraint ExistenceConstraintsGeometry23.Topic.Linie.Constraint1 is violated! The value of the attribute Geometrie of ExistenceConstraintsGeometry23.Topic.Linie was not found in the condition class.");
    }

    @Test
    public void polylineVerticesExistInSubclassOfConditionClass_Ok() {
        IomObject line = linie("l1",
                IomObjectHelper.createCoord("500000.000", "100000.000"),
                IomObjectHelper.createCoord("500100.000", "100050.000"),
                IomObjectHelper.createCoord("500200.000", "100020.000"));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                line,
                punkt("p1", "500000.000", "100000.000"),
                punktErweitert("p2", "500100.000", "100050.000"),
                punktErweitert("p3", "500200.000", "100020.000"));
        assertNoErrors(logger);
    }

    @Test
    public void polylineVerticesWithDifferentNumberFormat_Ok() {
        IomObject line = linie("l1",
                IomObjectHelper.createCoord("500000.000", "100000.000"),
                IomObjectHelper.createCoord("500100.000", "100050.500"));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                line,
                punkt("p1", "500000", "100000.0"),
                punkt("p2", "500100.0", "100050.50"));
        assertNoErrors(logger);
    }

    @Test
    public void polyline3dAllVerticesExist_Ok() {
        IomObject line = createObject(LINIE3D, "l1", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("500000.000", "100000.000", "400.000"),
                IomObjectHelper.createCoord("500100.000", "100050.000", "410.000")));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                line,
                punkt3d("p1", "500000.000", "100000.000", "400.000"),
                punkt3d("p2", "500100.000", "100050.000", "410.000"));
        assertNoErrors(logger);
    }

    @Test
    public void polyline3dVertexWithDifferentHeight_Fail() {
        IomObject line = createObject(LINIE3D, "l1", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("500000.000", "100000.000", "400.000"),
                IomObjectHelper.createCoord("500100.000", "100050.000", "410.000")));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                line,
                punkt3d("p1", "500000.000", "100000.000", "400.000"),
                punkt3d("p2", "500100.000", "100050.000", "999.000"));
        assertSingleError(logger, "Existence constraint ExistenceConstraintsGeometry23.Topic.Linie3d.Constraint1 is violated! The value of the attribute Geometrie of ExistenceConstraintsGeometry23.Topic.Linie3d was not found in the condition class.");
    }

    @Test
    public void polyline3dWithArcAllControlPointsExist_Ok() {
        IomObject line = createObject(LINIE3D, "l1", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("500000.000", "100000.000", "400.000"),
                IomObjectHelper.createArc("500010.000", "100010.000", "500020.000", "100000.000", "410.000")));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                line,
                punkt3d("p1", "500000.000", "100000.000", "400.000"),
                punkt3d("p3", "500020.000", "100000.000", "410.000"));
        assertNoErrors(logger);
    }

    @Test
    public void polyline3dWithArcEndPointWithDifferentHeight_Fail() {
        IomObject line = createObject(LINIE3D, "l1", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("500000.000", "100000.000", "400.000"),
                IomObjectHelper.createArc("500010.000", "100010.000", "500020.000", "100000.000", "410.000")));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                line,
                punkt3d("p1", "500000.000", "100000.000", "400.000"),
                punkt3d("p3", "500020.000", "100000.000", "999.000"));
        assertSingleError(logger, "Existence constraint ExistenceConstraintsGeometry23.Topic.Linie3d.Constraint1 is violated! The value of the attribute Geometrie of ExistenceConstraintsGeometry23.Topic.Linie3d was not found in the condition class.");
    }

    @Test
    public void polylineDomainAllVerticesExist_Ok() {
        IomObject line = createObject(LINIE_DOMAIN, "l1", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("500000.000", "100000.000"),
                IomObjectHelper.createCoord("500100.000", "100050.000")));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                line,
                punkt("p1", "500000.000", "100000.000"),
                punkt("p2", "500100.000", "100050.000"));
        assertNoErrors(logger);
    }

    @Test
    public void polylineDomainOneVertexMissing_Fail() {
        IomObject line = createObject(LINIE_DOMAIN, "l1", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("500000.000", "100000.000"),
                IomObjectHelper.createCoord("500100.000", "100050.000")));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                line,
                punkt("p1", "500000.000", "100000.000"));
        assertSingleError(logger, "Existence constraint ExistenceConstraintsGeometry23.Topic.LinieDomain.Constraint1 is violated! The value of the attribute Geometrie of ExistenceConstraintsGeometry23.Topic.LinieDomain was not found in the condition class.");
    }

    @Test
    public void polylineAlternativeSatisfiedByPolylinePath_Ok() {
        IomObject line = linieAlternativ("l1", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("500000.000", "100000.000"),
                IomObjectHelper.createCoord("500100.000", "100050.000")), null);
        IomObject other = linieAlternativ("l2", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("500000.000", "100000.000"),
                IomObjectHelper.createCoord("500200.000", "100020.000")), IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("500000.000", "100000.000"),
                IomObjectHelper.createCoord("500100.000", "100050.000")));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                line,
                other,
                punkt("p1", "500000.000", "100000.000"),
                punkt("p2", "500200.000", "100020.000"));
        assertNoErrors(logger);
    }

    @Test
    public void polylineAlternativeNoneSatisfied_Fail() {
        IomObject line = linieAlternativ("l1", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("500000.000", "100000.000"),
                IomObjectHelper.createCoord("500100.000", "100050.000")), null);
        IomObject other = linieAlternativ("l2", IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("500000.000", "100000.000"),
                IomObjectHelper.createCoord("500200.000", "100020.000")), IomObjectHelper.createPolyline(
                IomObjectHelper.createCoord("500000.000", "100000.000"),
                IomObjectHelper.createCoord("500300.000", "100000.000")));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                line,
                other,
                punkt("p1", "500000.000", "100000.000"),
                punkt("p2", "500200.000", "100020.000"));
        assertSingleError(logger, "Existence constraint ExistenceConstraintsGeometry23.Topic.LinieAlternativ.Constraint1 is violated! The value of the attribute Geometrie of ExistenceConstraintsGeometry23.Topic.LinieAlternativ was not found in the condition class.");
    }

    @Test
    public void surfaceAllBoundaryPointsExist_Ok() {
        IomObject surface = createObject(FLAECHE, "s1", IomObjectHelper.createRectangleGeometry("500000.000", "100000.000", "500100.000", "100100.000"));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                surface,
                punkt("p1", "500000.000", "100000.000"),
                punkt("p2", "500000.000", "100100.000"),
                punkt("p3", "500100.000", "100100.000"),
                punkt("p4", "500100.000", "100000.000"));
        assertNoErrors(logger);
    }

    @Test
    public void surfaceOneBoundaryPointMissing_Fail() {
        IomObject surface = createObject(FLAECHE, "s1", IomObjectHelper.createRectangleGeometry("500000.000", "100000.000", "500100.000", "100100.000"));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                surface,
                punkt("p1", "500000.000", "100000.000"),
                punkt("p2", "500000.000", "100100.000"),
                punkt("p4", "500100.000", "100000.000"));
        assertSingleError(logger, "Existence constraint ExistenceConstraintsGeometry23.Topic.Flaeche.Constraint1 is violated! The value of the attribute Geometrie of ExistenceConstraintsGeometry23.Topic.Flaeche was not found in the condition class.");
    }

    @Test
    public void surfaceWithHoleAllBoundaryPointsExist_Ok() {
        IomObject surface = createObject(FLAECHE, "s1", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createRectangleBoundary("500000.000", "100000.000", "500100.000", "100100.000"),
                IomObjectHelper.createRectangleBoundary("500020.000", "100020.000", "500040.000", "100040.000")));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                surface,
                punkt("p1", "500000.000", "100000.000"),
                punkt("p2", "500000.000", "100100.000"),
                punkt("p3", "500100.000", "100100.000"),
                punkt("p4", "500100.000", "100000.000"),
                punkt("p5", "500020.000", "100020.000"),
                punkt("p6", "500020.000", "100040.000"),
                punkt("p7", "500040.000", "100040.000"),
                punkt("p8", "500040.000", "100020.000"));
        assertNoErrors(logger);
    }

    @Test
    public void surfaceWithHoleInnerBoundaryPointMissing_Fail() {
        IomObject surface = createObject(FLAECHE, "s1", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createRectangleBoundary("500000.000", "100000.000", "500100.000", "100100.000"),
                IomObjectHelper.createRectangleBoundary("500020.000", "100020.000", "500040.000", "100040.000")));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                surface,
                punkt("p1", "500000.000", "100000.000"),
                punkt("p2", "500000.000", "100100.000"),
                punkt("p3", "500100.000", "100100.000"),
                punkt("p4", "500100.000", "100000.000"),
                punkt("p5", "500020.000", "100020.000"),
                punkt("p6", "500020.000", "100040.000"),
                punkt("p8", "500040.000", "100020.000"));
        assertSingleError(logger, "Existence constraint ExistenceConstraintsGeometry23.Topic.Flaeche.Constraint1 is violated! The value of the attribute Geometrie of ExistenceConstraintsGeometry23.Topic.Flaeche was not found in the condition class.");
    }

    @Test
    public void surfaceWithSeveralBoundaryPolylinesAllPointsExist_Ok() {
        IomObject boundary = IomObjectHelper.createMultiplePolylineBoundary(
                IomObjectHelper.createCoord("500000.000", "100000.000"),
                IomObjectHelper.createCoord("500000.000", "100100.000"),
                IomObjectHelper.createCoord("500100.000", "100100.000"),
                IomObjectHelper.createCoord("500100.000", "100000.000"),
                IomObjectHelper.createCoord("500000.000", "100000.000"));
        IomObject surface = createObject(FLAECHE, "s1", IomObjectHelper.createPolygonFromBoundaries(boundary));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                surface,
                punkt("p1", "500000.000", "100000.000"),
                punkt("p2", "500000.000", "100100.000"),
                punkt("p3", "500100.000", "100100.000"),
                punkt("p4", "500100.000", "100000.000"));
        assertNoErrors(logger);
    }

    @Test
    public void areaAllBoundaryPointsExist_Ok() {
        IomObject area = createObject(GEBIET, "a1", IomObjectHelper.createRectangleGeometry("500000.000", "100000.000", "500100.000", "100100.000"));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                area,
                punkt("p1", "500000.000", "100000.000"),
                punkt("p2", "500000.000", "100100.000"),
                punkt("p3", "500100.000", "100100.000"),
                punkt("p4", "500100.000", "100000.000"));
        assertNoErrors(logger);
    }

    @Test
    public void areaOneBoundaryPointMissing_Fail() {
        IomObject area = createObject(GEBIET, "a1", IomObjectHelper.createRectangleGeometry("500000.000", "100000.000", "500100.000", "100100.000"));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC,
                area,
                punkt("p1", "500000.000", "100000.000"),
                punkt("p2", "500000.000", "100100.000"),
                punkt("p3", "500100.000", "100100.000"));
        assertSingleError(logger, "Existence constraint ExistenceConstraintsGeometry23.Topic.Gebiet.Constraint1 is violated! The value of the attribute Geometrie of ExistenceConstraintsGeometry23.Topic.Gebiet was not found in the condition class.");
    }

    // Geometry without a condition object at all.
    @Test
    public void polylineNoPointsAtAll_Fail() {
        IomObject line = linie("l1",
                IomObjectHelper.createCoord("500000.000", "100000.000"),
                IomObjectHelper.createCoord("500100.000", "100050.000"));
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, line);
        assertSingleError(logger, "Existence constraint ExistenceConstraintsGeometry23.Topic.Linie.Constraint1 is violated! The value of the attribute Geometrie of ExistenceConstraintsGeometry23.Topic.Linie was not found in the condition class.");
    }
}
