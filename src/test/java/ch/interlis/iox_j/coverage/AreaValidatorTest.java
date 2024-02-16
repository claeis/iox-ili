package ch.interlis.iox_j.coverage;

import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;
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
import static org.junit.Assert.assertThat;

public class AreaValidatorTest {
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
    public void oneCommonArcWithDifferentMidPoints() throws IoxException {
        AreaValidator areaValidator = validate(
                toJts("A", p(b(c(10, 10), c(10, 30), a(15, 20, 30, 10), c(10, 10)))),
                toJts("B", p(b(c(30, 30), c(30, 10), a(20, 15, 10, 30), c(30, 30)))));

        assertThat(getInvalidLines(areaValidator), is(empty()));
        assertThat(areaValidator.createIntersectionExceptions("TEST"), is(empty()));
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
        AreaValidator areaValidator = new AreaValidator((JtsextGeometryFactory) polygons[0].getFactory(), 3);
        areaValidator.addPolygons(Arrays.asList(polygons));
        areaValidator.validateAll();
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
}
