package ch.interlis.iom_j.itf.impl.jtsext.io;

import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;
import com.vividsolutions.jts.geom.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class WKTWriterJtsextTest {
    private final JtsextGeometryFactory factory = new JtsextGeometryFactory();

    @Test
    public void emptyPoint() {
        assertWkt("POINT EMPTY", factory.createPoint((Coordinate) null));
    }

    @Test
    public void point() {
        assertWkt("POINT (1 2)", factory.createPoint(new Coordinate(1, 2)));
    }

    @Test
    public void emptyMultiPoint() {
        assertWkt("MULTIPOINT EMPTY", factory.createMultiPoint(new Coordinate[0]));
    }

    @Test
    public void multiPoint() {
        assertWkt("MULTIPOINT ((1 2), (3 4))",
                factory.createMultiPoint(new Coordinate[]{
                        new Coordinate(1, 2),
                        new Coordinate(3, 4)}));
    }

    @Test
    public void emptyLineString() {
        assertWkt("LINESTRING EMPTY", factory.createLineString(new Coordinate[0]));
    }

    @Test
    public void lineString() {
        assertWkt("LINESTRING (1 2, 3 4)",
                factory.createLineString(new Coordinate[]{
                        new Coordinate(1, 2),
                        new Coordinate(3, 4)}));
    }

    @Test
    public void emptyMultiLineString() {
        assertWkt("MULTILINESTRING EMPTY", factory.createMultiLineString(new LineString[0]));
    }

    @Test
    public void multiLineString() {
        assertWkt("MULTILINESTRING ((1 2, 3 4), (5 6, 7 8))",
                factory.createMultiLineString(new LineString[]{
                        factory.createLineString(new Coordinate[]{
                                new Coordinate(1, 2),
                                new Coordinate(3, 4)}),
                        factory.createLineString(new Coordinate[]{
                                new Coordinate(5, 6),
                                new Coordinate(7, 8)})}));
    }

    @Test
    public void compoundCurveEmpty() {
        assertWkt("COMPOUNDCURVE EMPTY", factory.createCompoundCurve(Collections.<CurveSegment>emptyList()));
    }

    @Test
    public void compoundCurveOneArc() {
        assertWkt("COMPOUNDCURVE (CIRCULARSTRING (1 2, 3 4, 5 6))",
                factory.createCompoundCurve(Collections.singletonList(
                        (CurveSegment) new ArcSegment(
                                new Coordinate(1, 2),
                                new Coordinate(3, 4),
                                new Coordinate(5, 6)))));
    }

    @Test
    public void compoundCurveTwoArcs() {
        assertWkt("COMPOUNDCURVE (CIRCULARSTRING (1 2, 3 4, 5 6), CIRCULARSTRING (5 6, 7 8, 9 10))",
                factory.createCompoundCurve(Arrays.asList(
                        (CurveSegment) new ArcSegment(
                                new Coordinate(1, 2),
                                new Coordinate(3, 4),
                                new Coordinate(5, 6)),
                        new ArcSegment(
                                new Coordinate(5, 6),
                                new Coordinate(7, 8),
                                new Coordinate(9, 10)))));
    }

    @Test
    public void compoundCurveStraitsAndArcs() {
        assertWkt("COMPOUNDCURVE ((1 2, 3 4, 1 2), CIRCULARSTRING (1 2, 3 4, 5 6), CIRCULARSTRING (5 6, 7 8, 9 10))",
                factory.createCompoundCurve(Arrays.asList(
                        new StraightSegment(
                                new Coordinate(1, 2),
                                new Coordinate(3, 4)),
                        new StraightSegment(
                                new Coordinate(3, 4),
                                new Coordinate(1, 2)),
                        new ArcSegment(
                                new Coordinate(1, 2),
                                new Coordinate(3, 4),
                                new Coordinate(5, 6)),
                        new ArcSegment(
                                new Coordinate(5, 6),
                                new Coordinate(7, 8),
                                new Coordinate(9, 10)))));
    }

    @Test
    public void compoundCurveStraits() {
        assertWkt("COMPOUNDCURVE ((1 2, 3 4, 5 6, 7 8))",
                factory.createCompoundCurve(Arrays.asList(
                        (CurveSegment) new StraightSegment(
                                new Coordinate(1, 2),
                                new Coordinate(3, 4)),
                        new StraightSegment(
                                new Coordinate(3, 4),
                                new Coordinate(5, 6)),
                        new StraightSegment(
                                new Coordinate(5, 6),
                                new Coordinate(7, 8)))));
    }

    @Test
    public void multiCurve() {
        assertWkt("MULTICURVE (COMPOUNDCURVE ((1 2, 3 4, 5 7, 7 12)), COMPOUNDCURVE ((9 5, 12 8), CIRCULARSTRING (12 8, 13 10, 15 11)))",
                factory.createMultiLineString(new LineString[]{
                        factory.createCompoundCurve(Arrays.asList(
                                (CurveSegment) new StraightSegment(
                                        new Coordinate(1, 2),
                                        new Coordinate(3, 4)),
                                new StraightSegment(
                                        new Coordinate(3, 4),
                                        new Coordinate(5, 7)),
                                new StraightSegment(
                                        new Coordinate(5, 7),
                                        new Coordinate(7, 12)))),
                        factory.createCompoundCurve(Arrays.asList(
                                new StraightSegment(
                                        new Coordinate(9, 5),
                                        new Coordinate(12, 8)),
                                new ArcSegment(
                                        new Coordinate(12, 8),
                                        new Coordinate(13, 10),
                                        new Coordinate(15, 11))))}));
    }

    @Test
    public void emptyPolygon() {
        assertWkt("POLYGON EMPTY", factory.createPolygon(null, null));
    }

    @Test
    public void polygon() {
        assertWkt("POLYGON ((1 2, 3 4, 5 6, 1 2))",
                factory.createPolygon(
                        factory.createLinearRing(new Coordinate[]{
                                new Coordinate(1, 2),
                                new Coordinate(3, 4),
                                new Coordinate(5, 6),
                                new Coordinate(1, 2)})));
    }

    @Test
    public void polygonWithHole() {
        assertWkt("POLYGON ((1 2, 3 4, 5 6, 1 2), (2 3, 4 5, 6 7, 2 3))",
                factory.createPolygon(
                        factory.createLinearRing(new Coordinate[]{
                                new Coordinate(1, 2),
                                new Coordinate(3, 4),
                                new Coordinate(5, 6),
                                new Coordinate(1, 2)}),
                        new LinearRing[]{
                                factory.createLinearRing(new Coordinate[]{
                                        new Coordinate(2, 3),
                                        new Coordinate(4, 5),
                                        new Coordinate(6, 7),
                                        new Coordinate(2, 3)})}));
    }

    @Test
    public void emptyMultiPolygon() {
        assertWkt("MULTIPOLYGON EMPTY", factory.createMultiPolygon(null));
    }

    @Test
    public void multiPolygon() {
        assertWkt("MULTIPOLYGON (((1 2, 3 4, 5 6, 1 2)), ((7 8, 9 10, 11 12, 7 8)))",
                factory.createMultiPolygon(new Polygon[]{
                        factory.createPolygon(
                                factory.createLinearRing(new Coordinate[]{
                                        new Coordinate(1, 2),
                                        new Coordinate(3, 4),
                                        new Coordinate(5, 6),
                                        new Coordinate(1, 2)})),
                        factory.createPolygon(
                                factory.createLinearRing(new Coordinate[]{
                                        new Coordinate(7, 8),
                                        new Coordinate(9, 10),
                                        new Coordinate(11, 12),
                                        new Coordinate(7, 8)}))}));
    }

    @Test
    public void curvePolygon() {
        assertWkt("CURVEPOLYGON ((10 1, 11 5, 19 6, 10 1), COMPOUNDCURVE ((11 2, 16 5), CIRCULARSTRING (16 5, 14 5, 11 2)))",
                factory.createCurvePolygon(
                        factory.createLinearRing(new Coordinate[]{
                                new Coordinate(10, 1),
                                new Coordinate(11, 5),
                                new Coordinate(19, 6),
                                new Coordinate(10, 1)}),
                        new LinearRing[]{
                                factory.createCompoundCurveRing(
                                        factory.createCompoundCurve(Arrays.asList(
                                                new StraightSegment(
                                                        new Coordinate(11, 2),
                                                        new Coordinate(16, 5)),
                                                new ArcSegment(
                                                        new Coordinate(16, 5),
                                                        new Coordinate(14, 5),
                                                        new Coordinate(11, 2)))))}));
    }

    private void assertWkt(String expected, Geometry geometry) {
        WKTWriterJtsext writer = new WKTWriterJtsext();
        String wkt = writer.write(geometry);
        assertEquals(expected, wkt);
    }
}
