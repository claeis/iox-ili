package ch.interlis.iom_j.itf.impl.jtsext.noding;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iom_j.itf.impl.ItfSurfaceLinetable2Polygon;
import ch.interlis.iom_j.itf.impl.jtsext.algorithm.CurveSegmentIntersector;
import ch.interlis.iom_j.itf.impl.jtsext.geom.*;
import ch.interlis.iox_j.IoxIntersectionException;
import ch.interlis.iox_j.IoxInvalidDataException;
import ch.interlis.iox_j.jts.Jtsext2iox;
import com.vividsolutions.jts.algorithm.locate.IndexedPointInAreaLocator;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.index.strtree.STRtree;

import java.util.*;
import java.util.List;

/**
 * Validates that a collection of {@link CurvePolygon}s satisfies the area condition.
 * <p>
 * Use {@link #validateArea(Collection, double, String)} if only the violations are needed. Otherwise
 * {@link #validate(Collection, double)} returns the validated instance, whose results can be queried:
 * <pre>
 * AreaValidator areaValidator = AreaValidator.validate(polygons, maxOverlap);
 * if (areaValidator.isValid()) {
 *     List&lt;IoxInvalidDataException&gt; gaps = areaValidator.createGapExceptions(iliQualifiedName);
 * } else {
 *     List&lt;IoxInvalidDataException&gt; violations = areaValidator.createIntersectionExceptions(iliQualifiedName);
 * }
 * </pre>
 */
public class AreaValidator {
    private static final byte NOT_SET = -1;
    private static final byte UNKNOWN = 0;
    private static final byte INVALID = 1;
    private static final byte MATCHED = 2;
    private static final byte VALID = 3;

    private final CurveSegmentIntersector intersector = new CurveSegmentIntersector();
    private final JtsextGeometryFactory factory;
    private final double maxOverlap;
    private final List<Face> faces = new ArrayList<Face>();
    private final List<EdgeRing> rings = new ArrayList<EdgeRing>();
    private final List<Intersection> intersections = new ArrayList<Intersection>();
    private final Map<String, Set<String>> overlaps = new HashMap<String, Set<String>>();

    private AreaValidator(JtsextGeometryFactory factory, double maxOverlap) {
        this.factory = factory;
        this.maxOverlap = maxOverlap;
    }

    /**
     * Validates that a collection of {@link CurvePolygon}s satisfies the area condition.
     * <p>
     * Invalid input polygons might lead to an incorrect result.
     *
     * @param polygons The {@link Polygon}s to validate
     * @param maxOverlap The maximum allowed overlap for {@link ArcSegment}s sharing one control point
     * @param iliQualifiedName The qualified name of the INTERLIS class used for generation of {@link IoxInvalidDataException}s
     *
     * @return A list of {@link IoxInvalidDataException}s with an entry for each violation.
     */
    public static List<IoxInvalidDataException> validateArea(Collection<Polygon> polygons, double maxOverlap, String iliQualifiedName) {
        return validate(polygons, maxOverlap).createIntersectionExceptions(iliQualifiedName);
    }

    /**
     * Validates that a collection of {@link CurvePolygon}s satisfies the area condition.
     * <p>
     * Invalid input polygons might lead to an incorrect result.
     *
     * @param polygons The {@link Polygon}s to validate, with the tid as user data
     * @param maxOverlap The maximum allowed overlap for {@link ArcSegment}s sharing one control point
     *
     * @return the validated {@link AreaValidator}, to query with {@link #isValid()},
     * {@link #createIntersectionExceptions(String)}, {@link #gatherInvalidGeometry()} and
     * {@link #createGapExceptions(String)}
     */
    public static AreaValidator validate(Collection<Polygon> polygons, double maxOverlap) {
        JtsextGeometryFactory factory = polygons.isEmpty() ? null : (JtsextGeometryFactory) polygons.iterator().next().getFactory();
        AreaValidator areaValidator = new AreaValidator(factory, maxOverlap);

        areaValidator.addPolygons(polygons);
        areaValidator.validateAll();
        return areaValidator;
    }

    private void addPolygons(Collection<Polygon> polygons) {
        final Map<Edge, CurveSegment> segmentsMap = new HashMap<Edge, CurveSegment>();
        final Map<Coordinate, List<Edge>> nodes = new HashMap<Coordinate, List<Edge>>();

        for (Polygon polygon : polygons) {
            addPolygon(polygon, segmentsMap, nodes);
        }

        mergeLines(nodes);
    }

    private void addPolygon(Polygon polygon, Map<Edge, CurveSegment> segmentsMap, Map<Coordinate, List<Edge>> nodes) {
        Face face = new Face();
        faces.add(face);
        face.polygon = polygon;

        CompoundCurveRing shell = (CompoundCurveRing)polygon.getExteriorRing();
        face.shell = addRing(shell, true, polygon.getUserData(), segmentsMap, nodes);

        face.holes = new EdgeRing[polygon.getNumInteriorRing()];
        for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
            CompoundCurveRing hole = (CompoundCurveRing)polygon.getInteriorRingN(i);
            face.holes[i] = addRing(hole, false, polygon.getUserData(), segmentsMap, nodes);
        }
    }

    private EdgeRing addRing(CompoundCurveRing linearRing, boolean isShell, Object userData, Map<Edge, CurveSegment> segmentsMap, Map<Coordinate, List<Edge>> nodes) {
        EdgeRing edgeRing = new EdgeRing();
        edgeRing.userData = userData;
        rings.add(edgeRing);

        CompoundCurve ring;
        if (linearRing.getLines().size() == 1) {
            ring = linearRing.getLines().get(0);
        } else {
            List<CurveSegment> segments = new ArrayList<CurveSegment>();
            for (CompoundCurve line : linearRing.getLines()) {
                segments.addAll(line.getSegments());
            }
            ring = factory.createCompoundCurve(segments);
        }

        ring.setUserData(userData);
        edgeRing.lines = ring;
        edgeRing.isRightInside = isShell ^ isCCW(ring);
        addLine(ring, edgeRing, segmentsMap, nodes);

        return edgeRing;
    }

    private boolean isCCW(CompoundCurve ring) {
        // fix valid self-intersections of arc segments that could lead to an incorrect result
        CompoundCurve fixedCurve = factory.createCompoundCurve(ring);
        ItfSurfaceLinetable2Polygon.removeValidSelfIntersections(fixedCurve, maxOverlap, CompoundCurve.ARC_MIN_DIRECTION_PT);
        return CompoundCurveRing.isCCW(factory.createCompoundCurveRing(fixedCurve));
    }

    private void addLine(CompoundCurve compoundCurve, EdgeRing edgeRing, Map<Edge, CurveSegment> segmentsMap, Map<Coordinate, List<Edge>> nodes) {
        ArrayList<CurveSegment> segments = compoundCurve.getSegments();
        for (int i = 0; i < compoundCurve.getNumSegments(); i++) {
            CurveSegment segment = segments.get(i);

            // get or create start node
            List<Edge> startNode = nodes.get(segment.getStartPoint());
            if (startNode == null) {
                startNode = new ArrayList<Edge>();
                nodes.put(segment.getStartPoint(), startNode);
            }

            // get or create end node
            List<Edge> endNode = nodes.get(segment.getEndPoint());
            if (endNode == null) {
                endNode = new ArrayList<Edge>();
                nodes.put(segment.getEndPoint(), endNode);
            }

            Edge areaEdge = null;
            for (Edge edge : endNode) {
                if (isControlPoint (edge.fromNode, segment) || isControlPoint (edge.toNode, segment)) {
                    if (segment.equals(segmentsMap.get(edge))) {
                        areaEdge = edge;
                        break;
                    }
                }
            }

            if (areaEdge == null) {
                areaEdge = new Edge(segment, i, edgeRing.next(i));
                segmentsMap.put(areaEdge, segment);
                startNode.add(areaEdge);
                endNode.add(areaEdge);
            }

            if (areaEdge.fromNode.equals2D(segment.getStartPoint())) {
                areaEdge.edgeRings.add(edgeRing);
            } else {
                areaEdge.reversedEdgeRings.add(edgeRing);
            }

            edgeRing.edges.add(areaEdge);
        }
    }

    private void mergeLines(Map<Coordinate, List<Edge>> nodes) {
        for (Map.Entry<Coordinate, List<Edge>> node : nodes.entrySet()) {
            Coordinate point = node.getKey();
            List<Edge> edges = node.getValue();

            // remove nodes with degree 2
            if (edges.size() == 2) {
                Edge edgeA = edges.get(0);
                Edge edgeB = edges.get(1);

                if (edgeA == edgeB) {
                    continue;
                }

                // remove edgeB
                for (EdgeRing edgeRing : edgeB.edgeRings) {
                    edgeRing.edges.remove(edgeB);
                }
                for (EdgeRing edgeRing : edgeB.reversedEdgeRings) {
                    edgeRing.edges.remove(edgeB);
                }

                // update edgeA to include edgeB
                if (edgeB.fromNode.equals2D(point)) {
                    List<Edge> adjNode = nodes.get(edgeB.toNode);
                    adjNode.remove(edgeB);
                    adjNode.add(edgeA);
                    edgeA.toNode = edgeB.toNode;
                    edgeA.toIndex = edgeB.toIndex;
                } else {
                    List<Edge> adjNode = nodes.get(edgeB.fromNode);
                    adjNode.remove(edgeB);
                    adjNode.add(edgeA);
                    edgeA.fromNode = edgeB.fromNode;
                    edgeA.fromIndex = edgeB.fromIndex;
                }
            }
        }
    }

    /**
     * @return true if the polygons satisfy the area condition
     */
    public boolean isValid() {
        return intersections.isEmpty() && overlaps.isEmpty();
    }

    /**
     * @param iliQualifiedName The qualified name of the INTERLIS class used for generation of {@link IoxInvalidDataException}s
     * @return A list of {@link IoxInvalidDataException}s with an entry for each violation; empty if {@link #isValid()}
     */
    public List<IoxInvalidDataException> createIntersectionExceptions(String iliQualifiedName) {
        List<IoxInvalidDataException> invalidDataExceptions = new ArrayList<IoxInvalidDataException>();

        for (Intersection intersection : intersections) {
            invalidDataExceptions.add(new IoxIntersectionException(iliQualifiedName, intersection.getCurve1().getUserData().toString(), intersection));
        }
        if (intersections.isEmpty()) {
            for (Map.Entry<String, Set<String>> overlapping : overlaps.entrySet()) {
                String tid1 = overlapping.getKey();
                for (String tid2 : overlapping.getValue()) {
                    invalidDataExceptions.add(new IoxInvalidDataException("polygons overlay tid1 " + tid1 + ", tid2 " + tid2));
                }
            }
        }
        return invalidDataExceptions;
    }

    /**
     * @return the segments that violate the area condition, as one {@link MultiLineString} per polygon (with the tid
     * as user data); empty if {@link #isValid()}
     */
    public List<MultiLineString> gatherInvalidGeometry() {
        List<MultiLineString> invalidMultiLines = new ArrayList<MultiLineString>();
        for (Face face : faces) {
            List<CompoundCurve> invalidLines = new ArrayList<CompoundCurve>(gatherInvalidGeometry(face.shell));
            for (EdgeRing edgeRing : face.holes) {
                invalidLines.addAll(gatherInvalidGeometry(edgeRing));
            }

            if (!invalidLines.isEmpty()) {
                MultiLineString invalidMultiLine = factory.createMultiLineString(invalidLines.toArray(new CompoundCurve[0]));
                invalidMultiLine.setUserData(face.polygon.getUserData());
                invalidMultiLines.add(invalidMultiLine);
            }
        }

        return invalidMultiLines;
    }

    private List<CompoundCurve> gatherInvalidGeometry(EdgeRing edgeRing) {
        List<CompoundCurve> invalidLines = new ArrayList<CompoundCurve>();
        List<CurveSegment> invalidSegments = new ArrayList<CurveSegment>();
        boolean lastSegmentValid = true;
        for (int i = 0; i < edgeRing.getNumSegments(); i++) {
            if (edgeRing.state[i] == INVALID) {
                invalidSegments.add(edgeRing.getSegment(i));
                lastSegmentValid = false;
            } else {
                if (!lastSegmentValid) {
                    CompoundCurve invalidLine = factory.createCompoundCurve(invalidSegments);
                    invalidLines.add(invalidLine);
                    invalidSegments.clear();
                }
                lastSegmentValid = true;
            }
        }

        if (!invalidSegments.isEmpty()) {
            CompoundCurve invalidLine = factory.createCompoundCurve(invalidSegments);
            invalidLines.add(invalidLine);
        }

        return invalidLines;
    }

    private void validateAll() {
        initializeEdgeState();

        STRtree index = new STRtree();

        for (Face face : faces) {
            index.insert(face.polygon.getEnvelopeInternal(), face);
        }

        for (Face face : faces) {
            List<Face> adjacentFaces = index.query(face.polygon.getEnvelopeInternal());
            adjacentFaces.remove(face);
            validateFace(face, adjacentFaces);
        }
    }

    /**
     * Finds the gaps: the bounded faces that no polygon covers, e.g. a hole between neighbours or an enclave that no
     * other polygon fills. The outside and the space between separate islands are not gaps.
     * <p>
     * The edges contained in one ring only form the boundary of the union of all polygons. They are walked with the
     * covered side on the left, turning at each node as sharply as possible to the right, so that faces touching in a
     * single node stay separate; a clockwise ring encloses a gap. Arcs are not linearised. Where two segments overlap
     * within the tolerance, the ring crosses itself; the loop up to the crossing is covered twice and does not count.
     *
     * @param iliQualifiedName The qualified name of the INTERLIS class used for generation of {@link IoxInvalidDataException}s
     * @return one {@link IoxInvalidDataException} per gap, at the smallest control point of its boundary and with the
     * tids of the polygons around it, in the order of the first polygon on their boundary
     * @throws IllegalStateException if the polygons do not satisfy the area condition (see {@link #isValid()})
     */
    public List<IoxInvalidDataException> createGapExceptions(String iliQualifiedName) {
        if (!isValid()) {
            throw new IllegalStateException("gaps can only be found if the polygons satisfy the area condition");
        }
        List<Edge> boundary = new ArrayList<Edge>();
        Map<Coordinate, List<Edge>> outgoing = new HashMap<Coordinate, List<Edge>>();
        for (EdgeRing ring : rings) {
            for (Edge edge : ring.edges) {
                if (!edge.isOnBoundary()) {
                    continue;
                }
                boundary.add(edge);
                List<Edge> edges = outgoing.get(edge.getFrom());
                if (edges == null) {
                    edges = new ArrayList<Edge>();
                    outgoing.put(edge.getFrom(), edges);
                }
                edges.add(edge);
            }
        }

        // start the rings in the order of the polygons, so that the gaps are reported in a deterministic order
        Set<Edge> used = new HashSet<Edge>();
        List<IoxInvalidDataException> gaps = new ArrayList<IoxInvalidDataException>();
        for (Edge first : boundary) {
            if (used.contains(first)) {
                continue;
            }
            List<Edge> ring = traceRing(first, outgoing, used);
            if (ring == null) {
                EhiLogger.traceState("boundary of the area partition is not closed, starting at " + first.getFrom() + ", tid " + first.getFirstRing().userData);
                continue;
            }
            if (getSignedArea(ring) < 0.0) {
                gaps.add(createGapException(ring, iliQualifiedName));
            }
        }
        return gaps;
    }

    /**
     * @param first an edge that is not used yet
     * @param used the edges already assigned to a ring; the edges of this ring are added
     * @return the closed ring starting with first, or null if the boundary is not closed (inconsistent input)
     */
    private List<Edge> traceRing(Edge first, Map<Coordinate, List<Edge>> outgoing, Set<Edge> used) {
        List<Edge> ring = new ArrayList<Edge>();
        Edge current = first;
        // each pass adds an unused edge to used, so the walk ends after at most as many passes as there are edges
        do {
            used.add(current);
            ring.add(current);
            current = getNext(current, outgoing.get(current.getTo()));
        } while (current != null && current != first && !used.contains(current));
        return current == first ? ring : null;
    }

    /**
     * Returns the outgoing edge that keeps the uncovered side on the right with the sharpest right turn:
     * the first one counterclockwise from the reverse direction of the incoming edge.
     * All candidates take part (also used ones), so that the choice does not depend on the order of tracing.
     */
    private Edge getNext(Edge incoming, List<Edge> candidates) {
        if (candidates == null) {
            return null;
        }
        if (candidates.size() == 1) {
            return candidates.get(0);
        }
        Coordinate node = incoming.getTo();
        // the incoming edge, walked backwards from the node
        CurveSegment back = incoming.getLastSegment();
        List<CurveSegment> segments = new ArrayList<CurveSegment>();
        segments.add(back);
        for (Edge candidate : candidates) {
            segments.add(candidate.getFirstSegment());
        }
        double overlapLength = 0.0;
        for (int i = 0; i < segments.size(); i++) {
            for (int j = i + 1; j < segments.size(); j++) {
                overlapLength = Math.max(overlapLength, getOverlapLength(segments.get(i), segments.get(j)));
            }
        }

        Edge best = null;
        for (Edge candidate : candidates) {
            if (best == null || NodeDirection.isCounterclockwiseBefore(node, back, candidate.getFirstSegment(), best.getFirstSegment(), overlapLength)) {
                best = candidate;
            }
        }
        return best;
    }

    /**
     * @return the length of the overlap of two segments that share a point: the distance to their second intersection,
     * unless that is the other control point of both; 0.0 if there is none
     */
    private double getOverlapLength(CurveSegment segment, CurveSegment other) {
        intersector.computeIntersection(segment, other);
        if (intersector.getIntersectionNum() == 2 && !(intersector.isIntersection(other.getStartPoint()) && intersector.isIntersection(other.getEndPoint()))) {
            return intersector.getIntersection(0).distance(intersector.getIntersection(1));
        }
        return 0.0;
    }

    /**
     * @return the end of the overlap of two segments that meet at the node, i.e. their second intersection, or null if
     * they do not overlap (see {@link #getOverlapLength(CurveSegment, CurveSegment)})
     */
    private Coordinate getOverlapEnd(CurveSegment segment, CurveSegment other, Coordinate node) {
        if (getOverlapLength(segment, other) == 0.0) {
            return null;
        }
        // the intersector still holds both intersections: the node and the end of the overlap
        Coordinate first = intersector.getIntersection(0);
        Coordinate second = intersector.getIntersection(1);
        return first.distance(node) > second.distance(node) ? first : second;
    }

    /**
     * @return the signed area of the ring without the loops of overlaps within the tolerance; positive for
     * counterclockwise rings
     */
    private double getSignedArea(List<Edge> ring) {
        // the segments of the ring in walking order: segment i is walked from starts[i] to ends[i], in the direction of
        // the segment if forward[i]
        List<CurveSegment> segments = new ArrayList<CurveSegment>();
        List<Boolean> forward = new ArrayList<Boolean>();
        List<Coordinate> starts = new ArrayList<Coordinate>();
        List<Coordinate> ends = new ArrayList<Coordinate>();
        for (Edge edge : ring) {
            List<CurveSegment> edgeSegments = edge.getSegments();
            if (!edge.isForward()) {
                Collections.reverse(edgeSegments);
            }
            for (CurveSegment segment : edgeSegments) {
                segments.add(segment);
                forward.add(edge.isForward());
                starts.add(edge.isForward() ? segment.getStartPoint() : segment.getEndPoint());
                ends.add(edge.isForward() ? segment.getEndPoint() : segment.getStartPoint());
            }
        }
        // consecutive segments that overlap within the tolerance cross each other; the loop between their shared
        // point and the crossing is covered twice and has the opposite orientation, so the ring is cut at the crossing
        int n = segments.size();
        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            Coordinate crossing = getOverlapEnd(segments.get(i), segments.get(j), ends.get(i));
            if (crossing != null) {
                ends.set(i, crossing);
                starts.set(j, crossing);
            }
        }

        // relative to a node of the ring, so that large (e.g. LV95) coordinates do not cancel out small areas
        Coordinate origin = ring.get(0).getFrom();
        double area = 0.0;
        for (int i = 0; i < n; i++) {
            area += getSignedArea(segments.get(i), forward.get(i), starts.get(i), ends.get(i), origin);
        }
        return area;
    }

    /**
     * @param forward true if the segment is walked from its start point to its end point
     * @return the contribution of the part of the segment from the point from to the point to (both on the segment)
     * to the signed area of a ring (shoelace term of the chord relative to the origin plus the circular segment of an
     * arc); positive for counterclockwise rings
     */
    private static double getSignedArea(CurveSegment segment, boolean forward, Coordinate from, Coordinate to, Coordinate origin) {
        double area = ((from.x - origin.x) * (to.y - origin.y) - (to.x - origin.x) * (from.y - origin.y)) / 2.0;
        if (CurveSegmentIntersector.isTrueArc(segment)) {
            ArcSegment arc = (ArcSegment) segment;
            Coordinate center = arc.getCenterPoint();
            double radius = CurveSegment.dist(center, segment.getStartPoint());
            // the sign of an arc is negative if it runs counterclockwise from its start point to its end point;
            // walked backwards, it runs the other way round
            boolean ccw = (arc.getSign() < 0.0) == forward;
            double theta = Math.atan2(to.y - center.y, to.x - center.x) - Math.atan2(from.y - center.y, from.x - center.x);
            if (!ccw) {
                theta = -theta;
            }
            // difference of two atan2 results: between -2 pi and 2 pi, so adding 2 pi once gives (0, 2 pi]
            if (theta <= 0.0) {
                theta += 2.0 * Math.PI;
            }
            double circularSegment = radius * radius / 2.0 * (theta - Math.sin(theta));
            // a counterclockwise arc bulges to the right of its chord, i.e. away from the ring interior
            area += ccw ? circularSegment : -circularSegment;
        }
        return area;
    }

    /**
     * @return the smallest control point of the ring, independent of where its tracing started
     */
    private static Coordinate getSmallestControlPoint(List<Edge> ring) {
        Coordinate smallest = null;
        for (Edge edge : ring) {
            for (CurveSegment segment : edge.getSegments()) {
                Coordinate point = edge.isForward() ? segment.getStartPoint() : segment.getEndPoint();
                if (smallest == null || point.compareTo(smallest) < 0) {
                    smallest = point;
                }
            }
        }
        return smallest;
    }

    private static IoxInvalidDataException createGapException(List<Edge> ring, String iliQualifiedName) {
        Set<String> tids = new TreeSet<String>();
        for (Edge edge : ring) {
            Object tid = edge.getFirstRing().userData;
            if (tid != null) {
                tids.add(tid.toString());
            }
        }
        Coordinate coord = getSmallestControlPoint(ring);
        String message = String.format("Gap coord (%.3f, %.3f), %s", coord.x, coord.y,
                IoxInvalidDataException.formatTids(tids.toArray(new String[tids.size()])));
        return new IoxInvalidDataException(message, iliQualifiedName, tids.isEmpty() ? null : tids.iterator().next(),
                Jtsext2iox.JTS2coord(new Coordinate(coord.x, coord.y)));
    }

    /**
     * Initializes the state of the segments of the {@link EdgeRing}s that record is the segment is valid or violates
     * the area condition.
     * <p>
     * Segments on a shared edge that is oriented in the opposite direction are marked as {@link #MATCHED}. If the edges
     * are oriented in the same direction, the polygons overlap and the segments are marked as {@link #INVALID}.
     * <p>
     * Segments with a known state can be skipped in further checks.
     */
    private void initializeEdgeState() {
        for (EdgeRing ring : rings) {
            ring.state = new byte[ring.getNumSegments()];
            
            int segmentIndex = 0;
            for (Edge edge : ring.edges) {
                if (edge.edgeRings.size() + edge.reversedEdgeRings.size() < 2) {
                    continue;
                }
                
                // check if there is an offending ring
                boolean reversed = edge.reversedEdgeRings.contains(ring);
                boolean ringOnRight = ring.isRightInside ^ reversed;
                byte state = MATCHED;
                for (EdgeRing edgeRing : edge.edgeRings) {
                    if (edgeRing != ring && edgeRing.isRightInside == ringOnRight) {
                        recordOverlap(ring.userData.toString(), edgeRing.userData.toString());
                        state = INVALID;
                        break;
                    }
                }
                if (state != INVALID) {
                    for (EdgeRing edgeRing : edge.reversedEdgeRings) {
                        if (edgeRing != ring && edgeRing.isRightInside != ringOnRight) {
                            recordOverlap(ring.userData.toString(), edgeRing.userData.toString());
                            state = INVALID;
                            break;
                        }
                    }
                }
                
                Coordinate firstNode = reversed ? edge.toNode : edge.fromNode;
                Coordinate lastNode = reversed ? edge.fromNode : edge.toNode;
                
                int fromIndex = ring.indexOfNextSegment(firstNode, segmentIndex);
                int toIndex = ring.indexOfNextSegment(lastNode, fromIndex);
                segmentIndex = toIndex;
                
                if (fromIndex < toIndex) {
                    Arrays.fill(ring.state, fromIndex, toIndex, state);
                } else {
                    Arrays.fill(ring.state, fromIndex, ring.getNumSegments(), state);
                    Arrays.fill(ring.state, 0, toIndex, state);
                }
            }
        }
    }

    private void validateFace(Face currentFace, List<Face> adjacentFaces) {
        validateEdgeRing(currentFace.shell, adjacentFaces);

        for (EdgeRing edgeRing : currentFace.holes) {
            validateEdgeRing(edgeRing, adjacentFaces);
        }
    }

    private void validateEdgeRing(EdgeRing edgeRing, List<Face> adjacentFaces) {
        if (edgeRing.isAllKnown()) {
            return;
        }

        for (int i = 0; i < edgeRing.getNumSegments(); i++) {
            if (edgeRing.isKnown(i)) {
                continue;
            }

            // check intersections with adjacent edge rings
            byte allValid = NOT_SET;
            for (Face adjacentFace : adjacentFaces) {
                EdgeRing shell = adjacentFace.shell;
                byte segmentInsideShell = validateSegmentAgainstRing(edgeRing, i, shell);

                byte segmentInsideHole = UNKNOWN;
                for (EdgeRing hole : adjacentFace.holes) {
                    byte holeState = validateSegmentAgainstRing(edgeRing, i, hole);
                    if (holeState == VALID) {
                        segmentInsideHole = VALID;
                    } else if (holeState == INVALID && segmentInsideHole == UNKNOWN) {
                        segmentInsideHole = INVALID;
                    }
                }

                if (segmentInsideShell == VALID || segmentInsideHole == VALID) {
                    if (allValid == NOT_SET) allValid = VALID;
                } else if (segmentInsideShell == INVALID || segmentInsideHole == INVALID) {
                    allValid = INVALID;
                    edgeRing.setState(i, INVALID);
                    recordOverlap(
                            edgeRing.userData.toString(),
                            shell.userData.toString());
                } else {
                    if (allValid != INVALID) allValid = UNKNOWN;
                }
            }

            if (allValid == VALID) {
                edgeRing.setState(i, VALID);
            }

            if (edgeRing.isKnown(i)) {
                continue;
            }

            // check if edge is inside an adjacent face
            CurveSegment thisSegment = edgeRing.getSegment(i);
            for (Face adjacentFace : adjacentFaces) {
                if (isPointInsideFace(thisSegment.getEndPoint(), adjacentFace)) {
                    recordOverlap(edgeRing.userData.toString(), adjacentFace.polygon.getUserData().toString());

                    edgeRing.setState(i, INVALID);
                    edgeRing.setState(edgeRing.next(i), INVALID);
                    break;
                }
            }
        }
    }

    /**
     * Test if one segment is invalid because of another {@link EdgeRing}. If the segment intersects the other ring in
     * an invalid way, it is marked as invalid. If the segment touches the ring in a control point, the return value
     * reflects whether the segment is inside ({@link #INVALID}) or outside ({@link #VALID}) the other ring.
     *
     * @param testRing The {@link EdgeRing} of the segment to test
     * @param testSegmentIndex The index of the segment to test
     * @param otherRing The other {@link EdgeRing} to test against
     *
     * @return Indication if the segment is inside ({@link #INVALID}) or outside ({@link #VALID}) the other ring or
     * {@link #UNKNOWN} if the segment does not touch the other ring anywhere.
     */
    private byte validateSegmentAgainstRing(EdgeRing testRing, int testSegmentIndex, EdgeRing otherRing) {
        CurveSegment testSegment = testRing.getSegment(testSegmentIndex);
        STRtree index = otherRing.getIndex();
        Map<Integer, Double> pointsToCheck = new HashMap<Integer, Double>();

        List<Integer> otherSegments = index.query(testSegment.computeEnvelopeInternal());
        for (int i : otherSegments) {
            CurveSegment otherSegment = otherRing.getSegment(i);

            intersector.computeIntersection(testSegment, otherSegment);
            if (intersector.hasIntersection()) {
                if (isInvalidProperIntersection(intersector, testSegment, otherSegment, maxOverlap)) {
                    testRing.setState(testSegmentIndex, INVALID);
                    otherRing.setState(i, INVALID);
                    recordIntersection(intersector, testRing.lines, otherRing.lines, testSegment, otherSegment);
                } else {
                    // remember intersection for later checking
                    if (intersector.getIntersectionNum() == 2) {
                        if (intersector.isIntersection(otherSegment.getStartPoint())) {
                            if (intersector.isIntersection(otherSegment.getEndPoint())) {
                                mergeMax(pointsToCheck, i, 0.0);
                                mergeMax(pointsToCheck, otherRing.next(i), 0.0);
                            } else {
                                mergeMax(pointsToCheck, i, intersector.getIntersection(0).distance(intersector.getIntersection(1)));
                            }
                        } else {
                            mergeMax(pointsToCheck, otherRing.next(i), intersector.getIntersection(0).distance(intersector.getIntersection(1)));
                        }
                    } else if (intersector.getIntersectionNum() == 1) {
                        if (intersector.isIntersection(otherSegment.getStartPoint())) {
                            mergeMax(pointsToCheck, i, 0.0);
                        } else {
                            mergeMax(pointsToCheck, otherRing.next(i), 0.0);
                        }
                    }
                }
            }
        }

        byte state = UNKNOWN;
        for (Map.Entry<Integer, Double> point : pointsToCheck.entrySet()) {
            int i = point.getKey();
            double distance = point.getValue();

            CurveSegment next = otherRing.getSegment(i);
            CurveSegment previous = otherRing.getSegment(otherRing.previous(i));

            distance = Math.max(distance, getOverlapLength(previous, next));

            // the segment might still be valid even if it is inside the shell, when it is also inside a hole
            boolean onInside = isOnInside(next.getStartPoint(), testSegment, previous, next, distance) ^ otherRing.isRightInside;
            if (onInside) {
                return INVALID;
            } else {
                state = VALID;
            }
        }

        return state;
    }

    private <K> void mergeMax(Map<K, Double> map, K key, double value) {
        Double previous = map.get(key);
        if (previous == null) {
            map.put(key, value);
        } else {
            map.put(key, Math.max(previous, value));
        }
    }

    private boolean isPointInsideFace(Coordinate point, Face face) {
        if (face.indexedPointInAreaLocator == null) {
            face.indexedPointInAreaLocator = new IndexedPointInAreaLocator(face.polygon);
        }

        return face.indexedPointInAreaLocator.locate(point) == Location.INTERIOR;
    }

    private void recordIntersection(CurveSegmentIntersector intersector, CompoundCurve curveA, CompoundCurve curveB, CurveSegment segmentA, CurveSegment segmentB) {
        if (intersector.getIntersectionNum() == 1) {
            intersections.add(new Intersection(intersector.getIntersection(0), curveA, curveB, segmentA, segmentB, intersector.getOverlap()));
        } else if (intersector.getIntersectionNum() == 2) {
            intersections.add(new Intersection(intersector.getIntersection(0), intersector.getIntersection(1), curveA, curveB, segmentA, segmentB, intersector.getOverlap(), intersector.isOverlay()));
        }
    }

    private void recordOverlap(String tid1, String tid2) {
        if (tid1.compareTo(tid2) > 0) {
            String temp = tid1;
            tid1 = tid2;
            tid2 = temp;
        }

        Set<String> overlapings = overlaps.get(tid1);
        if (overlapings == null) {
            overlapings = new HashSet<String>();
        }
        overlapings.add(tid2);
        overlaps.put(tid1, overlapings);
    }

    /**
     * check if the intersection is in a way that makes the segments invalid for sure.
     */
    private static boolean isInvalidProperIntersection(CurveSegmentIntersector intersector, CurveSegment segmentA, CurveSegment segmentB, double maxOverlap) {
        if (intersector.isOverlay()) {
            return true;
        }

        if (intersector.getIntersectionNum() == 2) {
            Coordinate p0 = intersector.getIntersection(0);
            Coordinate p1 = intersector.getIntersection(1);

            boolean p0AtControlPoints = isControlPoint(p0, segmentA) && isControlPoint(p0, segmentB);
            boolean p1AtControlPoints = isControlPoint(p1, segmentA) && isControlPoint(p1, segmentB);

            if (!p0AtControlPoints && !p1AtControlPoints) {
                // invalid if both intersection points are not at shared segment control points
                return true;
            } else if (p0AtControlPoints && p1AtControlPoints) {
                // both intersection points are at shared segment control points, overlap does not matter
                return false;
            } else {
                return intersector.getOverlap() != null && intersector.getOverlap() > maxOverlap;
            }
        } else if (intersector.getIntersectionNum() == 1) {
            // invalid if the intersection point is not at segment control points
            Coordinate p = intersector.getIntersection(0);
            return !(isControlPoint(p, segmentA) && isControlPoint(p, segmentB));
        }

        return false;
    }

    private static boolean isControlPoint(Coordinate point, CurveSegment segment) {
        return point.equals2D(segment.getStartPoint()) || point.equals2D(segment.getEndPoint());
    }

    /**
     * Checks if the test segment leaves the point outside of the angle from the first to the second segment, turning
     * counterclockwise.
     *
     * @param overlapLength the length of an overlap within the tolerance that starts at the point, or 0.0
     */
    private static boolean isOnInside(Coordinate point, CurveSegment testSegment, CurveSegment firstSegment, CurveSegment secondSegment, double overlapLength) {
        return !NodeDirection.isCounterclockwiseBefore(point, firstSegment, testSegment, secondSegment, overlapLength);
    }
    
    /**
     * Represents a shared edge between {@link EdgeRing}s.
     */
    private static class Edge {
        Coordinate fromNode;
        Coordinate toNode;

        final List<EdgeRing> edgeRings = new ArrayList<EdgeRing>();
        final List<EdgeRing> reversedEdgeRings = new ArrayList<EdgeRing>();

        /**
         * The segments of the edge in its first ring (see {@link #getFirstRing()}): from fromIndex up to toIndex
         * (exclusive, wrapping around the end of the ring; all segments if both are equal).
         */
        int fromIndex;
        int toIndex;

        public Edge(CurveSegment segment, int fromIndex, int toIndex) {
            this.fromNode = segment.getStartPoint();
            this.toNode = segment.getEndPoint();
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
        }

        /**
         * @return the ring that created the edge, in the direction of the edge; the only ring of an edge on the
         * boundary
         */
        EdgeRing getFirstRing() {
            return edgeRings.get(0);
        }

        /**
         * @return true if only one ring contains the edge, i.e. the edge is on the boundary of the union of all
         * polygons
         */
        boolean isOnBoundary() {
            return edgeRings.size() + reversedEdgeRings.size() == 1;
        }

        /**
         * @return true if the boundary edge is walked in the direction of its ring, i.e. the ring has the covered side
         * on the left
         */
        boolean isForward() {
            return !getFirstRing().isRightInside;
        }

        /**
         * @return the start node of the boundary edge, walked with the covered side on the left
         */
        Coordinate getFrom() {
            return isForward() ? fromNode : toNode;
        }

        /**
         * @return the end node of the boundary edge, walked with the covered side on the left
         */
        Coordinate getTo() {
            return isForward() ? toNode : fromNode;
        }

        /**
         * @return the first segment of the boundary edge, walked with the covered side on the left
         */
        CurveSegment getFirstSegment() {
            EdgeRing ring = getFirstRing();
            return ring.getSegment(isForward() ? fromIndex : ring.previous(toIndex));
        }

        /**
         * @return the last segment of the boundary edge, walked with the covered side on the left
         */
        CurveSegment getLastSegment() {
            EdgeRing ring = getFirstRing();
            return ring.getSegment(isForward() ? ring.previous(toIndex) : fromIndex);
        }

        /**
         * @return the segments of the boundary edge, in the direction of its ring
         */
        List<CurveSegment> getSegments() {
            List<CurveSegment> segments = new ArrayList<CurveSegment>();
            EdgeRing ring = getFirstRing();
            int i = fromIndex;
            do {
                segments.add(ring.getSegment(i));
                i = ring.next(i);
            } while (i != toIndex);
            return segments;
        }
    }
    
    /**
     * Represents a ring of a {@link Face}.
     */
    private static class EdgeRing {
        public final List<Edge> edges = new ArrayList<Edge>();
        public CompoundCurve lines;

        public boolean isRightInside;
        public Object userData;
        public byte[] state;
        private STRtree index;

        public STRtree getIndex() {
            if (index == null) {
                index = new STRtree();
                for (int i = 0; i < lines.getSegments().size(); i++) {
                    index.insert(lines.getSegments().get(i).computeEnvelopeInternal(), i);
                }
                index.build();
            }
            return index;
        }

        public int getNumSegments() {
            return lines.getNumSegments();
        }

        public CurveSegment getSegment(int index) {
            return lines.getSegments().get(index);
        }

        public int next(int index) {
            return index == getNumSegments() - 1 ? 0 : index + 1;
        }

        public int previous(int index) {
            return index == 0 ? getNumSegments() - 1 : index - 1;
        }

        public void setState(int index, byte newState) {
            if (state[index] == UNKNOWN) {
                state[index] = newState;
            }
        }

        public boolean isAllKnown() {
            for (int i = 0; i < lines.getNumSegments(); i++) {
                if (!isKnown(i)) {
                    return false;
                }
            }
            return true;
        }

        public boolean isKnown(int index) {
            return state[index] != UNKNOWN;
        }
        
        /**
         * Search the index of the segment whose start point is the given point.
         */
        public int indexOfNextSegment(Coordinate point, int startIndex) {
            ArrayList<CurveSegment> segments = lines.getSegments();
            int index = startIndex;
            while (index < getNumSegments()) {
                if (segments.get(index).getStartPoint().equals2D(point)) {
                    return index;
                }
                index++;
            }
            index = 0;
            while (index < startIndex) {
                if (segments.get(index).getStartPoint().equals2D(point)) {
                    return index;
                }
                index++;
            }

            throw new IllegalArgumentException(String.format("point %s is not the start point of any segment of %s", point, lines));
        }
    }

    /**
     * Represents a face of a {@link Polygon} with its shell and holes.
     */
    private static class Face {
        public EdgeRing shell;
        public EdgeRing[] holes;
        public Polygon polygon;
        public IndexedPointInAreaLocator indexedPointInAreaLocator;
    }

    /**
     * The direction in which a segment leaves a node: the angle of its tangent and its curvature. The curvature orders
     * segments that leave the node with the same tangent, e.g. an arc that touches a straight segment.
     * <p>
     * Tangents count as the same if they differ by no more than the rounding of the coordinates can turn them:
     * {@link #ROUNDING_ULPS} units in the last place of the node, over the distance that determines the direction.
     */
    private static class NodeDirection {
        /** the rounding error of the coordinates and the centre of an arc, in units in the last place */
        private static final double ROUNDING_ULPS = 16.0;

        private final double angle;
        private final double curvature;
        private final double angleTolerance;

        private NodeDirection(double angle, double curvature, double angleTolerance) {
            this.angle = angle;
            this.curvature = curvature;
            this.angleTolerance = angleTolerance;
        }

        /**
         * Turning counterclockwise around the node from the start segment, checks whether the first segment is reached
         * before the second one. A segment with the same tangent as the start segment is reached right away if it bends
         * more to the left than the start segment, otherwise last. Of two segments with the same tangent, the one
         * bending more to the right is reached first.
         *
         * @param node the start or end point of all three segments
         * @return true if first is reached before second
         */
        static boolean isCounterclockwiseBefore(Coordinate node, CurveSegment start, CurveSegment first, CurveSegment second) {
            NodeDirection startDirection = leaving(start, node);
            NodeDirection firstDirection = leaving(first, node);
            NodeDirection secondDirection = leaving(second, node);
            double firstAngle = startDirection.getAngleTo(firstDirection);
            double secondAngle = startDirection.getAngleTo(secondDirection);
            if (Math.abs(firstAngle - secondAngle) <= firstDirection.angleTolerance + secondDirection.angleTolerance) {
                return firstDirection.curvature < secondDirection.curvature;
            }
            return firstAngle < secondAngle;
        }

        /**
         * Like {@link #isCounterclockwiseBefore(Coordinate, CurveSegment, CurveSegment, CurveSegment)}, for segments
         * that may overlap within the tolerance from the node on. Within such an overlap the segments may lie on the
         * wrong side of each other, so the directions to points beyond the overlap are compared instead of the
         * tangents.
         *
         * @param overlapLength the length of the longest overlap of the segments within the tolerance that starts at
         * the node, or 0.0 to compare the tangents
         */
        static boolean isCounterclockwiseBefore(Coordinate node, CurveSegment start, CurveSegment first, CurveSegment second, double overlapLength) {
            if (overlapLength <= 0.0) {
                return isCounterclockwiseBefore(node, start, first, second);
            }
            double startAngle = getAngle(node, getDirectionPoint(node, start, overlapLength));
            double firstAngle = getAngle(node, getDirectionPoint(node, first, overlapLength)) - startAngle;
            double secondAngle = getAngle(node, getDirectionPoint(node, second, overlapLength)) - startAngle;
            return (firstAngle < 0.0 ? firstAngle + 2.0 * Math.PI : firstAngle) < (secondAngle < 0.0 ? secondAngle + 2.0 * Math.PI : secondAngle);
        }

        private static Coordinate getDirectionPoint(Coordinate origin, CurveSegment segment, double minDirectionPointDistance) {
            boolean originAtStart = segment.getStartPoint().equals2D(origin);

            if (segment instanceof ArcSegment) {
                return ((ArcSegment) segment).getDirectionPt(originAtStart, minDirectionPointDistance + CompoundCurve.ARC_MIN_DIRECTION_PT);
            } else {
                return originAtStart ? segment.getEndPoint() : segment.getStartPoint();
            }
        }

        private static double getAngle(Coordinate origin, Coordinate point) {
            return Math.atan2(point.y - origin.y, point.x - origin.x);
        }

        /**
         * @param segment the segment leaving the node
         * @param node the start or end point of the segment
         * @return the direction in which the segment leaves the node
         */
        private static NodeDirection leaving(CurveSegment segment, Coordinate node) {
            boolean atStart = segment.getStartPoint().equals2D(node);
            if (CurveSegmentIntersector.isTrueArc(segment)) {
                ArcSegment arc = (ArcSegment) segment;
                Coordinate center = arc.getCenterPoint();
                double rx = node.x - center.x;
                double ry = node.y - center.y;
                double curvature = 1.0 / Math.hypot(rx, ry);
                // the tangent is determined by the three points of the arc
                Coordinate mid = arc.getMidPoint();
                double tolerance = getAngleTolerance(node, Math.min(mid.distance(arc.getStartPoint()), mid.distance(arc.getEndPoint())));
                // the sign of an arc is negative if it runs counterclockwise from its start point to its end point
                boolean counterclockwise = (arc.getSign() < 0.0) == atStart;
                // walking counterclockwise around the center, the tangent is the radius turned to the left
                return counterclockwise
                        ? new NodeDirection(Math.atan2(rx, -ry), curvature, tolerance)
                        : new NodeDirection(Math.atan2(-rx, ry), -curvature, tolerance);
            }
            Coordinate other = atStart ? segment.getEndPoint() : segment.getStartPoint();
            return new NodeDirection(Math.atan2(other.y - node.y, other.x - node.x), 0.0, getAngleTolerance(node, node.distance(other)));
        }

        /**
         * @param distance the distance between the points that determine the direction
         * @return the angle by which the rounding of the coordinates at the node can turn the direction
         */
        private static double getAngleTolerance(Coordinate node, double distance) {
            double rounding = Math.ulp(Math.max(1.0, Math.max(Math.abs(node.x), Math.abs(node.y))));
            return ROUNDING_ULPS * rounding / distance;
        }

        /**
         * @return the angle by which other is turned counterclockwise from this direction, between 0 and 2 pi
         */
        private double getAngleTo(NodeDirection other) {
            // difference of two atan2 results: between -2 pi and 2 pi, so adding 2 pi once gives [0, 2 pi)
            double angleTo = other.angle - angle;
            if (angleTo < 0.0) {
                angleTo += 2.0 * Math.PI;
            }
            double tolerance = angleTolerance + other.angleTolerance;
            if (angleTo <= tolerance || angleTo >= 2.0 * Math.PI - tolerance) {
                return other.curvature > curvature ? 0.0 : 2.0 * Math.PI;
            }
            return angleTo;
        }
    }
}
