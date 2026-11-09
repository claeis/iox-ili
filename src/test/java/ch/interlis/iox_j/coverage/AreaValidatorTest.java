package ch.interlis.iox_j.coverage;

import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.itf.impl.jtsext.io.WKTWriterJtsext;
import ch.interlis.iom_j.itf.impl.jtsext.noding.AreaValidator;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.IoxIntersectionException;
import ch.interlis.iox_j.IoxInvalidDataException;
import ch.interlis.iox_j.jts.Iox2jtsext;
import ch.interlis.iox_j.validator.IomObjectHelper;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.collection.IsArrayContainingInAnyOrder.arrayContainingInAnyOrder;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class AreaValidatorTest {
    private static final double EPSILON = 1e-9;

    @Test
    public void disjoint() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 30, 30))),
                toJts("B", p(rect(50, 50, 70, 70))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void oneCommonPoint() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 30, 30))),
                toJts("B", p(rect(30, 30, 50, 50))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void fourPolygonsAroundOneCommonPoint() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 20, 20))),
                toJts("B", p(rect(20, 10, 30, 20))),
                toJts("C", p(rect(10, 20, 20, 30))),
                toJts("D", p(rect(20, 20, 30, 30))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void oneCommonStraightCWCW() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), c(30, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 10), c(10, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void oneCommonStraightCCWCCW() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(30, 10), c(10, 30), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(10, 30), c(30, 10), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void oneCommonStraightCWCCW() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), c(30, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(10, 30), c(30, 10), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void oneCommonArc() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), a(17, 17, 30, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 10), a(17, 17, 10, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void twoCommonPointsConnectedByDifferentArc() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), a(17, 17, 30, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 10), a(23, 23, 10, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void twoCommonPointsConnectedByArcsWithSameRadiusOnDifferentCircles() throws IoxException {
        // both arcs connect (10,10) and (18,10) with radius 5: A takes the long way round the circle with centre
        // (14,13), B the short way round the circle with centre (14,7); A covers B above B's arc
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(18, 10), a(14, 18, 10, 10)))),
                toJts("B", p(b(c(10, 10), a(14, 12, 18, 10), c(30, 10), c(30, 30), c(0, 30), c(0, 10), c(10, 10)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (18 10, 14 18, 10 10)))",
                "B: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (10 10, 14 12, 18 10)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void oneCommonArcWithDifferentMidPoints() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), a(15, 20, 30, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 10), a(20, 15, 10, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void oneCommonArcSplitIntoTwoArcs() throws IoxException {
        // all arc points on the circle with centre (10, 10) and radius 25
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 35), a(25, 30, 35, 10), c(10, 10)))),
                toJts("B", p(b(c(35, 10), a(34, 17, 30, 25), a(25, 30, 10, 35), c(40, 40), c(35, 10)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (10 35, 25 30, 35 10)))",
                "B: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (35 10, 34 17, 30 25), CIRCULARSTRING (30 25, 25 30, 10 35)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "Overlay coord1 (10.000, 35.000), coord2 (30.000, 25.000), tids A, B",
                "Overlay coord1 (30.000, 25.000), coord2 (35.000, 10.000), tids A, B");
    }

    @Test
    public void arcSegmentsOverlapIntersectionsAtControlPoints() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), a(23, 23, 30, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 10), a(17, 17, 10, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (10 30, 23 23, 30 10)))",
                "B: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (30 10, 17 17, 10 30)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void arcSegmentsOverlapIntersectionsAtControlPointsOverlapLowerThanMaxOverlap() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), a(21, 20, 30, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 10), a(19, 20, 10, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (10 30, 21 20, 30 10)))",
                "B: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (30 10, 19 20, 10 30)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void arcAndStraightSegmentWithSameControlPointsOverlapLowerThanMaxOverlap() throws IoxException {
        // the arc bulges 2 into B; although this is less than maxOverlap 3, the two segments do not correspond
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), a(20, 12, 30, 10), c(30, 0), c(10, 0), c(10, 10)))),
                toJts("B", p(rect(10, 10, 30, 20))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (10 10, 20 12, 30 10)))",
                "B: MULTICURVE (COMPOUNDCURVE ((30 10, 10 10)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void arcAndStraightSegmentWithSameControlPointsShortChordOverlap() throws IoxException {
        // the chord (0.08) is shorter than CompoundCurve.ARC_MIN_DIRECTION_PT; the arc bulges 0.001 into B
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(0, 0), c(10, 0), c(10, 5), a(10.001, 5.04, 10, 5.08), c(10, 10), c(0, 10), c(0, 0)))),
                toJts("B", p(b(c(10, 0), c(20, 0), c(20, 10), c(10, 10), c(10, 5.08), c(10, 5), c(10, 0)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (10 5, 10.001 5.04, 10 5.08)))",
                "B: MULTICURVE (COMPOUNDCURVE ((10 5.08, 10 5)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void arcAndStraightSegmentWithSameControlPointsShortChordGap() throws IoxException {
        // the chord (0.08) is shorter than CompoundCurve.ARC_MIN_DIRECTION_PT; the arc bulges 0.001 into A
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(0, 0), c(10, 0), c(10, 5), a(9.999, 5.04, 10, 5.08), c(10, 10), c(0, 10), c(0, 0)))),
                toJts("B", p(b(c(10, 0), c(10, 5), c(10, 5.08), c(10, 10), c(20, 10), c(20, 0), c(10, 0)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void arcTangentToStraightSegmentsAtBothControlPoints() throws IoxException {
        // the arc of B (circle with centre (0,0) and radius 10) leaves A's corners (0,10) and (10,0) along A's edges
        // and bends into A
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(0, 0, 10, 10))),
                toJts("B", p(b(c(0, 10), a(6, 8, 10, 0), c(20, 0), c(20, 20), c(0, 20), c(0, 10)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((0 10, 10 10, 10 0)))",
                "B: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (0 10, 6 8, 10 0)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void arcTangentToStraightSegmentsAtBothControlPointsLv95() throws IoxException {
        // as arcTangentToStraightSegmentsAtBothControlPoints with a radius of 2; the rounding of the LV95
        // coordinates turns the tangents of the arc against A's edges
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(2600000, 1200000, 2600002, 1200002))),
                toJts("B", p(b(c(2600000, 1200002), a(2600001.2, 1200001.6, 2600002, 1200000), c(2600004, 1200000),
                        c(2600004, 1200004), c(2600000, 1200004), c(2600000, 1200002)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((2600000 1200002, 2600002 1200002, 2600002 1200000)))",
                "B: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (2600000 1200002, 2600001.2 1200001.6, 2600002 1200000)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void arcTangentToStraightSegmentBendingAwayLv95() throws IoxException {
        // the arc of B (circle with centre (2600000, 1200001) and radius 1) leaves A's node (2600000, 1200000) along
        // A's edge to (2600002, 1200000) and bends away from A; the rounding of the LV95 coordinates turns the
        // tangent of the arc against A's edge
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(2599999, 1199999), c(2600002, 1199999), c(2600002, 1200000), c(2600000, 1200000),
                        c(2599999, 1200000), c(2599999, 1199999)))),
                toJts("B", p(b(c(2599999, 1200000), c(2600000, 1200000), a(2600000.6, 1200000.2, 2600000.8, 1200000.4),
                        c(2600000.8, 1200002), c(2599999, 1200002), c(2599999, 1200000)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void oneCommonArcWithMidPointsRoundedDifferently() throws IoxException {
        // both mid points are on the circle with centre (0, 0) and radius 50, B's only up to the rounding to mm;
        // the arcs do not correspond, B's arc lies inside A by less than a mm
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(0, 0), c(50, 0), a(30, 40, 0, 50), c(0, 0)))),
                toJts("B", p(b(c(0, 50), a(35.355, 35.355, 50, 0), c(60, 60), c(0, 50)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (50 0, 30 40, 0 50)))",
                "B: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (0 50, 35.355 35.355, 50 0)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void polygonBetweenTwoArcsWithSameControlPoints() throws IoxException {
        // A is a lens bounded by two arcs with the same control points
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(5, 10), a(10, 11, 15, 10), a(10, 9, 5, 10)))),
                toJts("B", p(b(c(5, 10), c(5, 20), c(15, 20), c(15, 10), a(10, 11, 5, 10)))),
                toJts("C", p(b(c(5, 10), a(10, 9, 15, 10), c(15, 0), c(5, 0), c(5, 10)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void sharedEdgeWithTwoSegmentPolygon() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), c(30, 30), a(20, 20, 30, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 10), a(20, 20, 30, 30)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void twoCommonPointsSecondPolygonInsideConcaveArc() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), c(30, 30), a(20, 20, 30, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), a(27, 20, 30, 10), c(23, 14), c(23, 26), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void arcSegmentsOverlapOneIntersectionAtControlPoint() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), a(22, 22, 25, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 15), a(18, 18, 10, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (10 30, 22 22, 25 10)))",
                "B: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (30 15, 18 18, 10 30)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "Intersection overlap 6.406026886681445, coord1 (10.000, 30.000), coord2 (24.750, 15.250), tids A, B");
    }

    @Test
    public void arcSegmentsOverlapOneIntersectionAtControlPointOverlapLowerThanMaxOverlap() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), a(20, 20, 25, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 15), a(20, 20, 10, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void arcSegmentsOverlapOneIntersectionAtControlPointConcave() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), a(23, 23, 29, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 11), a(22, 22, 10, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void arcSegmentsOverlapTwoIntersections() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 25), a(23, 23, 25, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 15), a(17, 17, 15, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (10 25, 23 23, 25 10)))",
                "B: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (30 15, 17 17, 15 30)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "Intersection overlap 8.48528137423857, coord1 (13.821, 26.179), coord2 (26.179, 13.821), tids A, B");
    }

    @Test
    public void arcSegmentsOverlapTwoIntersectionsOverlapLowerThanMaxOverlap() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 25), a(21, 21, 25, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 15), a(20, 20, 15, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (10 25, 21 21, 25 10)))",
                "B: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (30 15, 20 20, 15 30)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "Intersection overlap 1.4142135623730923, coord1 (17.163, 23.710), coord2 (23.710, 17.163), tids A, B");
    }

    @Test
    public void arcSegmentsOverlapTwoIntersectionsOverlapLowerThanMaxOverlapConcave() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 25), a(23, 23, 25, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 10), a(22, 22, 10, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (10 25, 23 23, 25 10)))",
                "B: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (30 10, 22 22, 10 30)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "Intersection overlap 1.4142135623730994, coord1 (17.159, 26.072), coord2 (26.072, 17.159), tids A, B");
    }

    @Test
    public void arcSegmentsTouch() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 25), a(20, 20, 25, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 15), a(20, 20, 15, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (10 25, 20 20, 25 10)))",
                "B: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (30 15, 20 20, 15 30)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "Intersection coord1 (20.000, 20.000), tids A, B");
    }

    @Test
    public void arcSegmentsOverlapCollinear() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), a(15, 20, 30, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(20, 15), a(15, 20, 10, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE (CIRCULARSTRING (10 30, 15 20, 30 10)))",
                "B: MULTICURVE (COMPOUNDCURVE ((30 30, 20 15), CIRCULARSTRING (20 15, 15 20, 10 30)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "Overlay coord1 (10.000, 30.000), coord2 (20.000, 15.000), tids A, B",
                "Intersection coord1 (20.000, 15.000), tids A, B");
    }

    @Test
    public void straightSegmentOverlapCollinear() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), c(30, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(20, 20), c(10, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((10 30, 30 10)))",
                "B: MULTICURVE (COMPOUNDCURVE ((30 30, 20 20, 10 30)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "Overlay coord1 (10.000, 30.000), coord2 (20.000, 20.000), tids A, B",
                "Intersection coord1 (20.000, 20.000), tids A, B");
    }

    @Test
    public void straightSegmentOverlapCollinearNoControlPointShared() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), c(30, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(25, 15), c(15, 25), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((10 30, 30 10)))",
                "B: MULTICURVE (COMPOUNDCURVE ((30 30, 25 15, 15 25, 30 30)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "Overlay coord1 (15.000, 25.000), coord2 (25.000, 15.000), tids A, B",
                "Intersection coord1 (25.000, 15.000), tids A, B",
                "Intersection coord1 (15.000, 25.000), tids A, B");
    }

    @Test
    public void multipleSharedSegments() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), c(15, 25), c(20, 20), a(26, 16, 30, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 10), a(26, 16, 20, 20), c(15, 25), c(10, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void multipleSharedSegmentsNotSameControlPoints() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), c(15, 25), c(20, 20), c(30, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 10), c(25, 15), c(15, 25), c(10, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((15 25, 20 20, 30 10)))",
                "B: MULTICURVE (COMPOUNDCURVE ((30 10, 25 15, 15 25)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "Overlay coord1 (15.000, 25.000), coord2 (20.000, 20.000), tids A, B",
                "Overlay coord1 (25.000, 15.000), coord2 (30.000, 10.000), tids A, B",
                "Overlay coord1 (20.000, 20.000), coord2 (25.000, 15.000), tids A, B");
    }

    @Test
    public void multipleSegmentsOverlapIntersectionsAtControlPoints() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), c(15, 25), c(21, 21), c(26, 16), c(30, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 10), c(24, 14), c(19, 19), c(15, 25), c(10, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((15 25, 21 21, 26 16, 30 10)))",
                "B: MULTICURVE (COMPOUNDCURVE ((30 10, 24 14, 19 19, 15 25)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void multipleSegmentsOverlapIntersectionNotAtControlPoints() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 25), c(17, 25), c(22, 22), c(25, 17), c(25, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 15), c(23, 15), c(18, 18), c(15, 23), c(15, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((10 25, 17 25, 22 22, 25 17, 25 10)))",
                "B: MULTICURVE (COMPOUNDCURVE ((30 15, 23 15, 18 18, 15 23, 15 30)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "Intersection coord1 (15.000, 25.000), tids A, B",
                "Intersection coord1 (25.000, 15.000), tids A, B");
    }

    @Test
    public void misplacedControlPoint() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), c(20, 20), c(30, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 10), c(20, 20), c(11, 28), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((10 30, 20 20)))",
                "B: MULTICURVE (COMPOUNDCURVE ((20 20, 11 28, 30 30)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "Intersection coord1 (11.905, 28.095), tids A, B");
    }

    @Test
    public void linearRingStartPointBetweenInvalidSegments() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), c(20, 20), c(30, 10), c(10, 10)))),
                toJts("B", p(b(c(18, 18), c(15, 30), c(30, 30), c(30, 10), c(18, 18)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((10 30, 20 20, 30 10)))",
                "B: MULTICURVE (COMPOUNDCURVE ((18 18, 15 30)), COMPOUNDCURVE ((30 10, 18 18)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "Intersection coord1 (16.667, 23.333), tids A, B");
    }

    @Test
    public void segmentOverlapsAndCollinearSegments() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), c(25, 30), c(25, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 10), c(20, 10), c(20, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((10 30, 25 30, 25 10, 10 10)))",
                "B: MULTICURVE (COMPOUNDCURVE ((30 10, 20 10, 20 30, 30 30)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "Overlay coord1 (20.000, 30.000), coord2 (25.000, 30.000), tids A, B",
                "Overlay coord1 (20.000, 10.000), coord2 (25.000, 10.000), tids A, B",
                "Intersection coord1 (20.000, 10.000), tids A, B",
                "Intersection coord1 (20.000, 30.000), tids A, B",
                "Intersection coord1 (25.000, 10.000), tids A, B",
                "Intersection coord1 (25.000, 30.000), tids A, B");
    }

    @Test
    public void narrowGaps() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 29), c(15, 25), c(19, 20), c(25, 15), c(30, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 10), c(25, 15), c(15, 25), c(10, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void congruentPolygons() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 40, 40))),
                toJts("B", p(rect(40, 40, 10, 10))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((10 10, 10 40, 40 40, 40 10, 10 10)))",
                "B: MULTICURVE (COMPOUNDCURVE ((40 40, 40 10, 10 10, 10 40, 40 40)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void congruentPolygonsWithHole() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 40, 40), rect(20, 30, 30, 20))),
                toJts("B", p(rect(40, 40, 10, 10), rect(30, 20, 20, 30))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((10 10, 10 40, 40 40, 40 10, 10 10)), COMPOUNDCURVE ((20 30, 20 20, 30 20, 30 30, 20 30)))",
                "B: MULTICURVE (COMPOUNDCURVE ((40 40, 40 10, 10 10, 10 40, 40 40)), COMPOUNDCURVE ((30 20, 30 30, 20 30, 20 20, 30 20)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void congruentPolygonsWithArcs() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), a(13, 23, 20, 20), a(27, 17, 30, 10), c(10, 10)))),
                toJts("B", p(b(c(10, 10), c(10, 30), a(13, 23, 20, 20), a(27, 17, 30, 10), c(10, 10)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((10 10, 10 30), CIRCULARSTRING (10 30, 13 23, 20 20), CIRCULARSTRING (20 20, 27 17, 30 10), (30 10, 10 10)))",
                "B: MULTICURVE (COMPOUNDCURVE ((10 10, 10 30), CIRCULARSTRING (10 30, 13 23, 20 20), CIRCULARSTRING (20 20, 27 17, 30 10), (30 10, 10 10)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void polygonFullyInside() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 40, 40))),
                toJts("B", p(rect(20, 20, 30, 30))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "B: MULTICURVE (COMPOUNDCURVE ((20 20, 20 30, 30 30, 30 20, 20 20)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void polygonFullyInsideArcLongerThanSemicircle() throws IoxException {
        // A runs clockwise from (25,20) via (13,20) to (20,25), 289 degrees round (19,19), and back straight;
        // the mid point is 199 degrees from the start point
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(25, 20), a(13, 20, 20, 25), c(25, 20)))),
                toJts("B", p(rect(17, 15, 19, 17))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "B: MULTICURVE (COMPOUNDCURVE ((17 15, 17 17, 19 17, 19 15, 17 15)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void polygonWithTwoSegmentsFullyInside() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 30, 30))),
                toJts("B", p(b(c(15, 13), c(15, 27), a(22, 20, 15, 13)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "B: MULTICURVE (COMPOUNDCURVE ((15 13, 15 27), CIRCULARSTRING (15 27, 22 20, 15 13)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void polygonOnOtherwiseValidPolygons() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 25, 40))),
                toJts("B", p(rect(25, 10, 40, 40))),
                toJts("C", p(rect(20, 20, 30, 30))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "C: MULTICURVE (COMPOUNDCURVE ((20 20, 20 30, 30 30, 30 20, 20 20)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 C",
                "polygons overlay tid1 B, tid2 C");
    }

    @Test
    public void twoTouchingPolygonsFullyInside() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 30, 30))),
                toJts("B", p(rect(15, 15, 20, 25))),
                toJts("C", p(rect(20, 15, 25, 25))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "B: MULTICURVE (COMPOUNDCURVE ((15 15, 15 25, 20 25)), COMPOUNDCURVE ((20 15, 15 15)))",
                "C: MULTICURVE (COMPOUNDCURVE ((20 25, 25 25, 25 15, 20 15)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B",
                "polygons overlay tid1 A, tid2 C");
    }

    @Test
    public void polygonFillsHole() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 40, 40), rect(20, 30, 30, 20))),
                toJts("B", p(rect(20, 20, 30, 30))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void polygonWithTwoSegmentsFillsHole() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 30, 30), b(c(15, 13), c(15, 27), a(22, 20, 15, 13)))),
                toJts("B", p(b(c(15, 13), c(15, 27), a(22, 20, 15, 13)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void polygonWithHoleOverlapAllBoundaries() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 27, 30), rect(13, 13, 23, 27))),
                toJts("B", p(rect(20, 17, 30, 23))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((27 30, 27 10)), COMPOUNDCURVE ((23 27, 23 13)))",
                "B: MULTICURVE (COMPOUNDCURVE ((20 23, 30 23)), COMPOUNDCURVE ((30 17, 20 17)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "Intersection coord1 (27.000, 17.000), tids A, B",
                "Intersection coord1 (27.000, 23.000), tids A, B",
                "Intersection coord1 (23.000, 17.000), tids A, B",
                "Intersection coord1 (23.000, 23.000), tids A, B");
    }

    @Test
    public void polygonWithHoleOverlapInnerBoundaryOneEdge() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 30, 30), rect(13, 13, 24, 27))),
                toJts("B", p(rect(17, 17, 27, 23))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((24 27, 24 13)))",
                "B: MULTICURVE (COMPOUNDCURVE ((17 23, 27 23, 27 17, 17 17)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "Intersection coord1 (24.000, 17.000), tids A, B",
                "Intersection coord1 (24.000, 23.000), tids A, B");
    }

    @Test
    public void polygonWithHoleOverlayInnerBoundary() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 30, 30), rect(14, 14, 26, 26))),
                toJts("B", p(rect(17, 17, 26, 23))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((26 26, 26 14)))",
                "B: MULTICURVE (COMPOUNDCURVE ((17 23, 26 23, 26 17, 17 17)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "Intersection coord1 (26.000, 17.000), tids A, B",
                "Intersection coord1 (26.000, 23.000), tids A, B",
                "Overlay coord1 (26.000, 17.000), coord2 (26.000, 23.000), tids A, B");
    }

    @Test
    public void polygonWithHoleThatTouchesOuterBoundary() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 30, 30), b(c(15, 15), c(25, 15), c(30, 30), c(15, 25), c(15, 15)))),
                toJts("B", p(b(c(15, 15), c(30, 30), c(25, 15), c(15, 15)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void polygonWithSelfTouchingBoundary() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), c(30, 30), c(15, 25), c(15, 15), c(25, 15), c(30, 30), c(30, 10), c(10, 10)))),
                toJts("B", p(b(c(15, 15), c(30, 30), c(25, 15), c(15, 15)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void polygonWithSelfTouchingBoundaryOverlap() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), c(30, 30), c(15, 25), c(15, 15), c(30, 30), c(30, 10), c(10, 10)))),
                toJts("B", p(b(c(15, 15), c(30, 30), c(25, 15), c(15, 15)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((15 15, 30 30)))",
                "B: MULTICURVE (COMPOUNDCURVE ((15 15, 30 30, 25 15, 15 15)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void polygonWithSelfTouchingBoundaryOverlapStartInside() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(13, 27), c(20, 20), c(13, 20), c(13, 13), c(20, 20), c(27, 13), c(27, 20), c(20, 20), c(30, 27), c(30, 10), c(10, 10), c(10, 30), c(27, 30), c(20, 20), c(20, 27), c(13, 27)))),
                toJts("B", p(b(c(20, 20), c(20, 13), c(13, 13), c(20, 20)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((13 13, 20 20)))",
                "B: MULTICURVE (COMPOUNDCURVE ((20 20, 20 13, 13 13, 20 20)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void polygonWithHoleThatTouchesOuterBoundarySingleIntersectionPoint() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 30, 30), b(c(14, 14), c(26, 14), c(30, 30), c(14, 26), c(14, 14)))),
                toJts("B", p(b(c(16, 16), c(30, 30), c(24, 16), c(16, 16)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void polygonWithTwoHolesThatTouchesOuterBoundary() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(
                        rect(10, 10, 30, 30),
                        b(c(20, 20), c(27, 20), c(30, 30), c(20, 27), c(20, 20)),
                        rect(13, 13, 20, 20))),
                toJts("B", p(b(c(20, 20), c(30, 30), c(27, 20), c(20, 20)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void polygonWithTwoTouchingHoles() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(
                        rect(10, 10, 30, 30),
                        rect(20, 20, 27, 27),
                        rect(13, 13, 20, 20))),
                toJts("B", p(b(c(20, 20), c(27, 27), c(27, 20), c(20, 20)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void polygonInsideHole() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 40, 40), rect(20, 30, 30, 20))),
                toJts("B", p(rect(22, 22, 28, 28))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void polygonInsideHoleTouchesHoleInOnePoint() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 40, 40), rect(20, 30, 30, 20))),
                toJts("B", p(b(c(20, 20), c(23, 27), c(27, 23), c(20, 20)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void polygonInsideHoleTouchesReflexVertexOfHole() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 40, 40), b(c(20, 20), c(20, 30), c(30, 30), c(30, 20), c(25, 25), c(20, 20)))),
                toJts("B", p(b(c(25, 25), c(23, 28), c(27, 28), c(25, 25)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void polygonOutsideHoleTouchesReflexVertexOfHole() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 40, 40), b(c(20, 20), c(20, 30), c(30, 30), c(30, 20), c(25, 25), c(20, 20)))),
                toJts("B", p(b(c(25, 25), c(24, 22), c(26, 22), c(25, 25)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "B: MULTICURVE (COMPOUNDCURVE ((25 25, 24 22, 26 22, 25 25)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void polygonsInNestedHoles() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 40, 40), rect(15, 35, 35, 15))),
                toJts("B", p(rect(17, 17, 33, 33), rect(20, 30, 30, 20))),
                toJts("C", p(rect(22, 22, 28, 28))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void polygonFillsHoleSameOrientation() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 40, 40), rect(20, 20, 30, 30))),
                toJts("B", p(rect(20, 20, 30, 30))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void twoPolygonsFillHole() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 40, 40), b(c(20, 30), c(20, 20), c(25, 20), c(30, 20), c(30, 30), c(25, 30), c(20, 30)))),
                toJts("B", p(rect(20, 20, 25, 30))),
                toJts("C", p(rect(25, 20, 30, 30))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void twoPolygonsFillHoleWithoutVertexOnHole() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("B", p(rect(20, 20, 25, 30))),
                toJts("C", p(rect(25, 20, 30, 30))),
                toJts("A", p(rect(10, 10, 40, 40), rect(20, 30, 30, 20))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((20 20, 30 20)), COMPOUNDCURVE ((30 30, 20 30)))",
                "B: MULTICURVE (COMPOUNDCURVE ((20 30, 25 30)), COMPOUNDCURVE ((25 20, 20 20)))",
                "C: MULTICURVE (COMPOUNDCURVE ((25 30, 30 30)), COMPOUNDCURVE ((30 20, 25 20)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "Overlay coord1 (20.000, 20.000), coord2 (25.000, 20.000), tids A, B",
                "Overlay coord1 (20.000, 30.000), coord2 (25.000, 30.000), tids A, B",
                "Overlay coord1 (25.000, 20.000), coord2 (30.000, 20.000), tids A, C",
                "Overlay coord1 (25.000, 30.000), coord2 (30.000, 30.000), tids A, C");
    }

    @Test
    public void twoPolygonsFillSameHole() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 40, 40), rect(20, 30, 30, 20))),
                toJts("B", p(rect(20, 20, 30, 30))),
                toJts("C", p(rect(20, 20, 30, 30))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "B: MULTICURVE (COMPOUNDCURVE ((20 20, 20 30, 30 30, 30 20, 20 20)))",
                "C: MULTICURVE (COMPOUNDCURVE ((20 20, 20 30, 30 30, 30 20, 20 20)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 B, tid2 C");
    }

    @Test
    public void polygonOutsideHoleSharesHoleEdge() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 40, 40), rect(20, 30, 30, 20))),
                toJts("B", p(rect(30, 20, 35, 30))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((30 20, 30 30)))",
                "B: MULTICURVE (COMPOUNDCURVE ((30 20, 30 30, 35 30, 35 20, 30 20)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 B");
    }

    @Test
    public void polygonsInsideAndOutsideTouchHoleThatTouchesOuterBoundary() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(
                        b(c(10, 10), c(10, 40), c(40, 40), c(40, 25), c(40, 10), c(10, 10)),
                        b(c(40, 25), c(25, 20), c(25, 30), c(40, 25)))),
                toJts("B", p(b(c(40, 25), c(30, 22), c(30, 28), c(40, 25)))),
                toJts("C", p(b(c(40, 25), c(45, 30), c(45, 20), c(40, 25)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void threeEqualSegments() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 20, 30))),
                toJts("B", p(rect(20, 10, 30, 30))),
                toJts("C", p(b(c(20, 30), c(15, 25), c(15, 15), c(20, 10), c(20, 30)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "A: MULTICURVE (COMPOUNDCURVE ((20 30, 20 10)))",
                "C: MULTICURVE (COMPOUNDCURVE ((20 30, 15 25, 15 15, 20 10, 20 30)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 A, tid2 C");
    }

    @Test
    public void threeEqualSegmentsLeft() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 20, 30))),
                toJts("B", p(rect(20, 10, 30, 30))),
                toJts("C", p(b(c(20, 30), c(25, 25), c(25, 15), c(20, 10), c(20, 30)))));

        assertThat(getInvalidLines(areaValidator), containsInAnyOrder(
                "B: MULTICURVE (COMPOUNDCURVE ((20 10, 20 30)))",
                "C: MULTICURVE (COMPOUNDCURVE ((20 30, 25 25, 25 15, 20 10, 20 30)))"));

        assertIntersectionExceptions(areaValidator.createIntersectionExceptions("TEST"),
                "polygons overlay tid1 B, tid2 C");
    }

    @Test
    public void polygonWithPermissibleOverlap() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 20, 30))),
                toJts("B", p(b(c(20, 10), c(20, 30), a(25, 19, 30, 20), c(30, 10), c(20, 10)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void polygonWithPermissibleOverlapRingStartAtOverlappingArc() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 20, 30))),
                toJts("B", p(b(c(20, 30), a(25, 19, 30, 20), c(30, 10), c(20, 10), c(20, 30)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void polygonWithPermissibleOverlapOverlapsArcOneIntersection() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 20), a(20, 21, 30, 10), c(10, 10)))),
                toJts("B", p(b(c(10, 23), c(10, 30), a(20, 24, 30, 10), a(20, 23, 10, 23)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void onePermissibleArcOverlapsAtControlPoint() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 20), a(20, 21, 30, 10), c(10, 10)))),
                toJts("B", p(b(c(10, 23), c(10, 30), a(20, 27, 30, 10), a(20, 20, 10, 23)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void polygonWithPermissibleOverlapOverlapsArc() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 20), a(20, 21, 30, 10), c(10, 10)))),
                toJts("B", p(b(c(10, 23), c(10, 30), a(20, 22, 30, 10), a(20, 21, 10, 23)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void twoPermissibleArcOverlapsAtControlPoint() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 20), a(20, 21, 30, 10), c(10, 10)))),
                toJts("B", p(b(c(10, 23), c(10, 30), a(20, 24, 30, 10), a(20, 20, 10, 23)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void PolygonsWithMultiplePolylineRings() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(
                        IomObjectHelper.createMultiplePolylineBoundary(c(10, 10), c(10, 30), c(20, 30), c(20, 10), c(10, 10)),
                        IomObjectHelper.createMultiplePolylineBoundary(c(13, 13), c(13, 27), c(17, 27), c(17, 13), c(13, 13)))),
                toJts("B", p(
                        IomObjectHelper.createMultiplePolylineBoundary(c(20, 10), c(20, 30), c(30, 30), c(30, 10), c(20, 10)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void InvalidPolygonSelfIntersectionFalsePositive() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 20, 30))),
                toJts("B", p(b(c(20, 10), c(30, 30), c(20, 30), c(30, 10), c(20, 10)))));

        assertThat(getInvalidLines(areaValidator), is(not(empty())));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(not(empty())));
    }

    @Test
    public void InvalidPolygonSelfIntersectionFalseNegative() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(rect(10, 10, 20, 25))),
                toJts("B", p(b(c(10, 10), c(10, 25), c(20, 25), c(30, 10), c(30, 30), c(20, 10), c(10, 10)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    @Test
    public void parquetTiling() throws Exception {
        double x = 2480500;
        double y = 1070500;
        Iterator<IomObject> parquet = parquetGenerator(x, y, x + 200, y + 200, 10, 10, 5);
        List<Polygon> polygons = new ArrayList<Polygon>();
        int count = 0;
        while (parquet.hasNext()) {
            polygons.add(toJts("geom-" + count++, parquet.next()));
        }

        AreaValidator areaValidator = validate(polygons.toArray(new Polygon[0]));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
    }

    // gaps between the polygons (createGapExceptions)

    @Test
    public void adjacentSquares_NoGap() throws IoxException {
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(rect(0, 0, 10, 10))),
                toJts("B", p(rect(10, 0, 20, 10))));

        assertThat(gaps, is(empty()));
    }

    @Test
    public void gridWithoutCenter_OneGap() throws IoxException {
        List<Polygon> polygons = new ArrayList<Polygon>();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (x != 1 || y != 1) {
                    polygons.add(toJts("o" + x + y, p(rect(x * 10, y * 10, x * 10 + 10, y * 10 + 10))));
                }
            }
        }

        List<IoxInvalidDataException> gaps = findGaps(0.0, polygons.toArray(new Polygon[polygons.size()]));

        assertThat(gaps.size(), is(1));
        assertGap(gaps.get(0), 10, 10, "o01", "o10", "o12", "o21");
    }

    @Test
    public void enclaveNotFilled_OneGap() throws IoxException {
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(rect(0, 0, 30, 30), rect(10, 10, 20, 20))));

        assertThat(gaps.size(), is(1));
        assertGap(gaps.get(0), 10, 10, "A");
    }

    @Test
    public void enclaveFilled_NoGap() throws IoxException {
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(rect(0, 0, 30, 30), rect(10, 10, 20, 20))),
                toJts("B", p(rect(10, 10, 20, 20))));

        assertThat(gaps, is(empty()));
    }

    @Test
    public void enclavePartlyFilled_OneGap() throws IoxException {
        // B fills the lower half of the enclave; the enclave ring is split at (10,15) and (20,15)
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(rect(0, 0, 30, 30), b(c(10, 10), c(10, 15), c(10, 20), c(20, 20), c(20, 15), c(20, 10), c(10, 10)))),
                toJts("B", p(b(c(10, 10), c(20, 10), c(20, 15), c(10, 15), c(10, 10)))));

        assertThat(gaps.size(), is(1));
        assertGap(gaps.get(0), 10, 15, "A", "B");
    }

    @Test
    public void islandInEnclave_OneGap() throws IoxException {
        // the island does not touch the enclave; the gap is the space between them
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(rect(0, 0, 30, 30), rect(10, 10, 20, 20))),
                toJts("B", p(rect(12, 12, 18, 18))));

        assertThat(gaps.size(), is(1));
        assertGap(gaps.get(0), 10, 10, "A");
    }

    @Test
    public void separateIslands_NoGap() throws IoxException {
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(rect(0, 0, 10, 10))),
                toJts("B", p(rect(20, 0, 30, 10))));

        assertThat(gaps, is(empty()));
    }

    @Test
    public void touchingInOneCorner_NoGap() throws IoxException {
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(rect(0, 0, 10, 10))),
                toJts("B", p(rect(10, 10, 20, 20))));

        assertThat(gaps, is(empty()));
    }

    @Test
    public void sliver_OneGap() throws IoxException {
        // two squares 0.001 apart, closed by a bar at the bottom and at the top
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(rect(0, 1, 10, 11))),
                toJts("B", p(rect(10.001, 1, 20, 11))),
                toJts("C", p(b(c(0, 0), c(20, 0), c(20, 1), c(10.001, 1), c(10, 1), c(0, 1), c(0, 0)))),
                toJts("D", p(b(c(0, 11), c(10, 11), c(10.001, 11), c(20, 11), c(20, 12), c(0, 12), c(0, 11)))));

        assertThat(gaps.size(), is(1));
        assertGap(gaps.get(0), 10, 1, "A", "B", "C", "D");
    }

    @Test
    public void gapTouchingOutsideInOnePoint_OneGap() throws IoxException {
        // the gap (10,10)-(20,20) touches the unbounded outside only in (20,20)
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(b(c(0, 0), c(20, 0), c(20, 10), c(10, 10), c(10, 20), c(0, 20), c(0, 0)))),
                toJts("B", p(b(c(20, 0), c(30, 0), c(30, 20), c(20, 20), c(20, 10), c(20, 0)))),
                toJts("C", p(b(c(0, 20), c(10, 20), c(20, 20), c(20, 30), c(0, 30), c(0, 20)))));

        assertThat(gaps.size(), is(1));
        assertGap(gaps.get(0), 10, 10, "A", "B", "C");
    }

    @Test
    public void twoGapsTouchingInOnePoint_TwoGaps() throws IoxException {
        // 3x3 grid without the center and without the upper right cell, the upper right cell is closed by D and E;
        // both gaps touch in (20,20)
        List<IoxInvalidDataException> gaps = sortByCoord(findGaps(0.0,
                toJts("o00", p(rect(0, 0, 10, 10))),
                toJts("o10", p(rect(10, 0, 20, 10))),
                toJts("o20", p(rect(20, 0, 30, 10))),
                toJts("o01", p(rect(0, 10, 10, 20))),
                toJts("o21", p(rect(20, 10, 30, 20))),
                toJts("o02", p(rect(0, 20, 10, 30))),
                toJts("o12", p(rect(10, 20, 20, 30))),
                toJts("D", p(b(c(30, 10), c(40, 10), c(40, 40), c(30, 40), c(30, 30), c(30, 20), c(30, 10)))),
                toJts("E", p(b(c(10, 30), c(20, 30), c(30, 30), c(30, 40), c(10, 40), c(10, 30))))));

        assertThat(gaps.size(), is(2));
        assertGap(gaps.get(0), 10, 10, "o01", "o10", "o12", "o21");
        assertGap(gaps.get(1), 20, 20, "D", "E", "o12", "o21");
    }

    @Test
    public void sharedArc_NoGap() throws IoxException {
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(b(c(0, 0), c(10, 0), a(12, 5, 10, 10), c(0, 10), c(0, 0)))),
                toJts("B", p(b(c(10, 0), c(20, 0), c(20, 10), c(10, 10), a(12, 5, 10, 0)))));

        assertThat(gaps, is(empty()));
    }

    @Test
    public void lensBetweenArcAndStraight_OneGap() throws IoxException {
        // the arc of A bulges inwards (to x=8), the edge of B is straight
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(b(c(0, 0), c(10, 0), a(8, 5, 10, 10), c(0, 10), c(0, 0)))),
                toJts("B", p(rect(10, 0, 20, 10))));

        assertThat(gaps.size(), is(1));
        assertGap(gaps.get(0), 10, 0, "A", "B");
    }

    @Test
    public void lensBetweenArcAndStraight_WithinOverlapTolerance_OneGap() throws IoxException {
        // the arc of A bulges inwards by 0.001; the overlap tolerance does not make the two segments correspond
        List<IoxInvalidDataException> gaps = findGaps(0.002,
                toJts("A", p(b(c(0, 0), c(10, 0), a(9.999, 5, 10, 10), c(0, 10), c(0, 0)))),
                toJts("B", p(rect(10, 0, 20, 10))));

        assertThat(gaps.size(), is(1));
        assertGap(gaps.get(0), 10, 0, "A", "B");
    }

    @Test
    public void lensBetweenArcAndStraightBelowLinearisation_OneGap() throws IoxException {
        // arc height 0.0001 is below the linearisation precision of arcs
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(b(c(0, 0), c(10, 0), a(9.9999, 5, 10, 10), c(0, 10), c(0, 0)))),
                toJts("B", p(rect(10, 0, 20, 10))));

        assertThat(gaps.size(), is(1));
        assertGap(gaps.get(0), 10, 0, "A", "B");
    }

    @Test
    public void lensBetweenArcWithShortChordAndStraight_OneGap() throws IoxException {
        // the chord (0.08) is shorter than CompoundCurve.ARC_MIN_DIRECTION_PT; the arc of A bulges 0.001 into A
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(b(c(0, 0), c(10, 0), c(10, 5), a(9.999, 5.04, 10, 5.08), c(10, 10), c(0, 10), c(0, 0)))),
                toJts("B", p(b(c(10, 0), c(10, 5), c(10, 5.08), c(10, 10), c(20, 10), c(20, 0), c(10, 0)))));

        assertThat(gaps.size(), is(1));
        assertGap(gaps.get(0), 10, 5, "A", "B");
    }

    @Test
    public void lensBetweenTwoArcs_WithinOverlapTolerance_OneGap() throws IoxException {
        // the mid points differ by 0.001; the overlap tolerance does not make the two arcs correspond
        List<IoxInvalidDataException> gaps = findGaps(0.002,
                toJts("A", p(b(c(0, 0), c(10, 0), a(12, 5, 10, 10), c(0, 10), c(0, 0)))),
                toJts("B", p(b(c(10, 0), c(20, 0), c(20, 10), c(10, 10), a(12.001, 5, 10, 0)))));

        assertThat(gaps.size(), is(1));
        assertGap(gaps.get(0), 10, 0, "A", "B");
    }

    @Test
    public void gapNextToArcsOverlappingWithinTolerance_OneGap() throws IoxException {
        // the arcs of arcSegmentsOverlapOneIntersectionAtControlPointOverlapLowerThanMaxOverlap: A and B share (10,30)
        // and overlap by 1.28 up to the crossing at (20,20); C closes the gap beyond the crossing
        List<IoxInvalidDataException> gaps = findGaps(3.0,
                toJts("A", p(b(c(10, 10), c(10, 30), a(20, 20, 25, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 15), a(20, 20, 10, 30), c(30, 30)))),
                toJts("C", p(b(c(25, 10), c(30, 10), c(30, 15), c(25, 10)))));

        assertThat(gaps.size(), is(1));
        // the boundary of the gap runs along both arcs up to (10,30)
        assertGap(gaps.get(0), 10, 30, "A", "B", "C");
    }

    @Test
    public void lensBetweenArcsWithSameRadiusOnDifferentCircles_OneGap() throws IoxException {
        // both arcs connect (10,10) and (18,10) with radius 5: A ends at the short way round the circle with centre
        // (14,7), B starts at the long way round the circle with centre (14,13)
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(b(c(0, 0), c(30, 0), c(30, 10), c(18, 10), a(14, 12, 10, 10), c(0, 10), c(0, 0)))),
                toJts("B", p(b(c(0, 10), c(10, 10), a(14, 18, 18, 10), c(30, 10), c(30, 30), c(0, 30), c(0, 10)))));

        assertThat(gaps.size(), is(1));
        assertGap(gaps.get(0), 10, 10, "A", "B");
    }

    @Test
    public void arcTangentToIncomingEdge_OneGap() throws IoxException {
        // at (0,0), the arc of B leaves with the same tangent as the top edge of A arrives from (10,0);
        // the gap between them must be traced on its own, separate from the outer boundary
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(rect(0, -10, 10, 0))),
                // quarter circle around (0,5) from (0,0) to (5,5)
                toJts("B", p(b(c(0, 0), a(5.0 * Math.sin(Math.PI / 4.0), 5.0 - 5.0 * Math.cos(Math.PI / 4.0), 5, 5), c(10, 5), c(10, 20), c(0, 20), c(0, 0)))),
                toJts("C", p(b(c(10, -10), c(20, -10), c(20, 20), c(10, 20), c(10, 5), c(10, 0), c(10, -10)))));

        assertThat(gaps.size(), is(1));
        assertGap(gaps.get(0), 0, 0, "A", "B", "C");
    }

    @Test
    public void outerBoundaryWithArcs_NoGap() throws IoxException {
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(b(c(0, 0), a(5, -2, 10, 0), c(10, 10), a(5, 12, 0, 10), c(0, 0)))),
                toJts("B", p(b(c(10, 0), a(15, -3, 20, 0), c(20, 10), c(10, 10), c(10, 0)))));

        assertThat(gaps, is(empty()));
    }

    @Test
    public void arcWithPartLongerThanSemicircle_NoGap() throws IoxException {
        // A runs clockwise from (25,20) via (13,20) to (20,25), 289 degrees round (19,19), and back straight;
        // the mid point is 199 degrees from the start point
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(b(c(25, 20), a(13, 20, 20, 25), c(25, 20)))));

        assertThat(gaps, is(empty()));
    }

    @Test
    public void sliverAtLv95Coordinates_OneGap() throws IoxException {
        // 0.1 mm x 1 m sliver; it must not get lost in the rounding of large coordinates
        double x = 2600000.0;
        double y = 1200000.0;
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(rect(x, y + 1, x + 10, y + 2))),
                toJts("B", p(rect(x + 10.0001, y + 1, x + 20, y + 2))),
                toJts("C", p(b(c(x, y), c(x + 20, y), c(x + 20, y + 1), c(x + 10.0001, y + 1), c(x + 10, y + 1), c(x, y + 1), c(x, y)))),
                toJts("D", p(b(c(x, y + 2), c(x + 10, y + 2), c(x + 10.0001, y + 2), c(x + 20, y + 2), c(x + 20, y + 3), c(x, y + 3), c(x, y + 2)))));

        assertThat(gaps.size(), is(1));
        assertGap(gaps.get(0), x + 10, y + 1, "A", "B", "C", "D");
    }

    @Test
    public void tinyIslandAtLv95Coordinates_NoGap() throws IoxException {
        // 1 cm x 1 cm island; a rounding error in its area must not turn it into a gap
        double x = 2600000.0;
        double y = 1200000.0;
        List<IoxInvalidDataException> gaps = findGaps(0.0,
                toJts("A", p(rect(x, y, x + 10, y + 10))),
                toJts("B", p(rect(x + 20, y + 20, x + 20.01, y + 20.01))));

        assertThat(gaps, is(empty()));
    }

    @Test
    public void gapsWithOverlap_Fail() throws IoxException {
        // the boundary of the union is only known for a valid area partition
        AreaValidator areaValidator = AreaValidator.validate(Arrays.asList(
                toJts("A", p(rect(0, 0, 10, 10))),
                toJts("B", p(rect(5, 0, 15, 10)))), 0.0);

        assertThat(areaValidator.isValid(), is(false));
        try {
            areaValidator.createGapExceptions("TEST");
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void noPolygons_NoGap() {
        AreaValidator areaValidator = AreaValidator.validate(new ArrayList<Polygon>(), 0.0);

        assertThat(areaValidator.isValid(), is(true));
        assertThat(areaValidator.createGapExceptions("TEST"), is(empty()));
    }

    // Functions to create geometries in a short and concise way
    private IomObject a(double a1, double a2, double c1, double c2) {
        return IomObjectHelper.createArc(Double.toString(a1), Double.toString(a2), Double.toString(c1), Double.toString(c2));
    }

    private IomObject c(double c1, double c2) {
        return IomObjectHelper.createCoord(Double.toString(c1), Double.toString(c2));
    }

    private IomObject b(IomObject... segments) {
        return IomObjectHelper.createBoundary(segments);
    }

    private IomObject p(IomObject... boundary) {
        return IomObjectHelper.createPolygonFromBoundaries(boundary);
    }

    private IomObject rect(double x1, double y1, double x2, double y2) {
        return IomObjectHelper.createRectangleBoundary(Double.toString(x1), Double.toString(y1), Double.toString(x2), Double.toString(y2));
    }

    private Polygon toJts(String tid, IomObject polygon) throws IoxException {
        Polygon jtsPolygon = Iox2jtsext.surface2JTS(polygon, 0.0);
        jtsPolygon.setUserData(tid);
        return jtsPolygon;
    }

    private Iterator<IomObject> parquetGenerator(final double x1, final double y1, final double x2, final double y2, final double width, final int length, final int subdivision) {
        return new Iterator<IomObject>() {
            double rowOriginX = x1;
            double rowOriginY = y1;
            double x = x1;
            double y = y1;
            boolean flipped = false;
            boolean horizontal = true;
            boolean done = false;

            @Override
            public boolean hasNext() {
                return !done;
            }

            @Override
            public IomObject next() {
                if (done) throw new NoSuchElementException();

                IomObject nextObject = createLPiece(x, y, length, width, flipped, subdivision);

                x += width * 2;
                y += width * 2;

                if (x >= x2 || y >= y2) {
                    if (horizontal) {
                        // adjust row origin horizontally
                        if (flipped) {
                            rowOriginY -= width * length;
                            while (rowOriginY < y1) {
                                rowOriginX += width * 2;
                                rowOriginY += width * 2;
                            }
                        } else {
                            rowOriginX += width * (length - 1);
                        }
                    } else {
                        // adjust row origin vertically
                        if (flipped) {
                            rowOriginX -= width * (length - 1);
                            while (rowOriginX < x1) {
                                rowOriginX += width * 2;
                                rowOriginY += width * 2;
                            }
                        } else {
                            rowOriginY += width * length;
                        }
                    }
                    flipped = !flipped;

                    if (rowOriginX >= x2) {
                        // switch from horizontal to vertical
                        horizontal = false;
                        flipped = true;
                        rowOriginX = x1;
                        rowOriginY = y1 + width * length;
                    }
                    if (rowOriginY >= y2) {
                        done = true;
                    }

                    x = rowOriginX;
                    y = rowOriginY;
                }

                return nextObject;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }

    private IomObject createLPiece(double originX, double originY, int length, double width, boolean flipped, int subdivision) {
        IomObject[] points = new IomObject[(4 * length - 2) * subdivision + 3];

        double delta = flipped ? -width : width;
        double x = originX;
        double y = originY;
        int index = 0;

        points[index++] = c(x, y);
        for (int i = 0; i < length; i++, index += subdivision)
            System.arraycopy(createConnection(x, y, x, y += delta, subdivision, true), 0, points, index, subdivision);
        points[index++] = createArcConnection(x, y, x += delta, y);
        for (int i = 0; i < length - 1; i++, index += subdivision)
            System.arraycopy(createConnection(x, y, x, y -= delta, subdivision, false), 0, points, index, subdivision);
        for (int i = 0; i < length - 1; i++, index += subdivision)
            System.arraycopy(createConnection(x, y, x += delta, y, subdivision, false), 0, points, index, subdivision);
        points[index++] = createArcConnection(x, y, x, y -= delta);
        for (int i = 0; i < length; i++, index += subdivision)
            System.arraycopy(createConnection(x, y, x -= delta, y, subdivision, true), 0, points, index, subdivision);

        return p(b(points));
    }

    private IomObject createArcConnection(double x1, double y1, double x2, double y2) {
        double deltaX = (x2 - x1);
        double deltaY = (y2 - y1);
        return a(x1 + 0.5 * deltaX + 0.2 * deltaY, y1 + 0.5 * deltaY - 0.2 * deltaX, x2, y2);
    }

    private IomObject[] createConnection(double x1, double y1, double x2, double y2, int subdivisions, boolean offset) {
        IomObject[] points = new IomObject[subdivisions];
        double deltaX = (x2 - x1) / subdivisions;
        double deltaY = (y2 - y1) / subdivisions;
        double x = offset ? x1 + (deltaY / 2) : x1;
        double y = offset ? y1 - (deltaX / 2) : y1;
        for (int i = 0; i < subdivisions - 1; i++) {
            x += deltaX;
            y += deltaY;
            points[i] = c(x, y);
        }
        points[subdivisions - 1] = c(x2, y2);
        return points;
    }

    private void assertIntersectionExceptions(List<IoxInvalidDataException> exceptions, String... expectedMessages) {
        String[] actualMessages = new String[exceptions.size()];
        for (int i = 0; i < exceptions.size(); i++) {
            IoxInvalidDataException exception = exceptions.get(i);
            if (exception instanceof IoxIntersectionException) {
                actualMessages[i] = ((IoxIntersectionException) exception).getIntersection().toShortString();
            } else {
                actualMessages[i] = exception.getMessage();
            }
        }

        assertThat(actualMessages, arrayContainingInAnyOrder(expectedMessages));
    }

    private AreaValidator validate(Polygon... polygons) {
        AreaValidator areaValidator = AreaValidator.validate(Arrays.asList(polygons), 3);
        assertThat(areaValidator.isValid(), is(areaValidator.createIntersectionExceptions("TEST").isEmpty()));
        return areaValidator;
    }

    private List<String> getInvalidLines(AreaValidator areaValidator) {
        List<String> invalidLines = new ArrayList<String>();
        List<MultiLineString> invalidMultiLines = areaValidator.gatherInvalidGeometry();
        for (MultiLineString invalidMultiLine : invalidMultiLines) {
            String invalidGeometryWKT = new WKTWriterJtsext().write(invalidMultiLine);
            String tid = invalidMultiLine.getUserData().toString();

            invalidLines.add(tid + ": " + invalidGeometryWKT);
        }

        return invalidLines;
    }

    private List<IoxInvalidDataException> findGaps(double maxOverlap, Polygon... polygons) {
        List<Polygon> polygonList = Arrays.asList(polygons);
        List<IoxInvalidDataException> gaps = validateAndFindGaps(polygonList, maxOverlap);

        // the gaps must not depend on the order of the polygons, only the order in which they are reported may
        List<Polygon> reversed = new ArrayList<Polygon>(polygonList);
        Collections.reverse(reversed);
        assertSameGaps(gaps, validateAndFindGaps(reversed, maxOverlap));
        List<Polygon> rotated = new ArrayList<Polygon>(polygonList);
        Collections.rotate(rotated, 1);
        assertSameGaps(gaps, validateAndFindGaps(rotated, maxOverlap));

        return gaps;
    }

    private static List<IoxInvalidDataException> validateAndFindGaps(List<Polygon> polygons, double maxOverlap) {
        AreaValidator areaValidator = AreaValidator.validate(polygons, maxOverlap);
        assertThat("test data must satisfy the area condition", areaValidator.isValid(), is(true));
        return areaValidator.createGapExceptions("TEST");
    }

    private static void assertSameGaps(List<IoxInvalidDataException> expected, List<IoxInvalidDataException> actual) {
        assertThat(actual.size(), is(expected.size()));
        expected = sortByCoord(expected);
        actual = sortByCoord(actual);
        for (int i = 0; i < expected.size(); i++) {
            assertThat(actual.get(i).getRawMessage(), is(expected.get(i).getRawMessage()));
            assertThat(actual.get(i).getTid(), is(expected.get(i).getTid()));
            assertThat(actual.get(i).getGeom().getattrvalue("C1"), is(expected.get(i).getGeom().getattrvalue("C1")));
            assertThat(actual.get(i).getGeom().getattrvalue("C2"), is(expected.get(i).getGeom().getattrvalue("C2")));
        }
    }

    /**
     * @return the gaps sorted by their coordinate (and message, for gaps at the same coordinate)
     */
    private static List<IoxInvalidDataException> sortByCoord(List<IoxInvalidDataException> gaps) {
        List<IoxInvalidDataException> sorted = new ArrayList<IoxInvalidDataException>(gaps);
        Collections.sort(sorted, new Comparator<IoxInvalidDataException>() {
            @Override
            public int compare(IoxInvalidDataException gap1, IoxInvalidDataException gap2) {
                int result = Double.compare(Double.parseDouble(gap1.getGeom().getattrvalue("C1")), Double.parseDouble(gap2.getGeom().getattrvalue("C1")));
                if (result == 0) {
                    result = Double.compare(Double.parseDouble(gap1.getGeom().getattrvalue("C2")), Double.parseDouble(gap2.getGeom().getattrvalue("C2")));
                }
                return result != 0 ? result : gap1.getRawMessage().compareTo(gap2.getRawMessage());
            }
        });
        return sorted;
    }

    private static void assertGap(IoxInvalidDataException gap, double x, double y, String... tids) {
        IomObject coord = gap.getGeom();
        assertThat(Double.parseDouble(coord.getattrvalue("C1")), closeTo(x, EPSILON));
        assertThat(Double.parseDouble(coord.getattrvalue("C2")), closeTo(y, EPSILON));
        assertThat(gap.getTid(), is(tids[0]));
        assertThat(gap.getRawMessage(), endsWith(", " + IoxInvalidDataException.formatTids(tids)));
    }
}
