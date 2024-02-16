package ch.interlis.iom_j.itf.impl.jtsext.noding;

import ch.interlis.iom_j.itf.impl.ItfSurfaceLinetable2Polygon;
import ch.interlis.iom_j.itf.impl.jtsext.algorithm.CurveSegmentIntersector;
import ch.interlis.iom_j.itf.impl.jtsext.geom.*;
import ch.interlis.iox_j.IoxIntersectionException;
import ch.interlis.iox_j.IoxInvalidDataException;
import com.vividsolutions.jts.algorithm.locate.IndexedPointInAreaLocator;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.index.strtree.STRtree;

import java.util.*;
import java.util.List;

/**
 * Validates that a collection of {@link CurvePolygon}s satisfies the area condition.
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

    public AreaValidator(JtsextGeometryFactory factory, double maxOverlap) {
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
        if (polygons.isEmpty()) {
            return Collections.emptyList();
        }

        JtsextGeometryFactory factory = (JtsextGeometryFactory) polygons.iterator().next().getFactory();
        AreaValidator areaValidator = new AreaValidator(factory, maxOverlap);

        areaValidator.addPolygons(polygons);
        areaValidator.validateAll();
        return areaValidator.createIntersectionExceptions(iliQualifiedName);
    }

    public void addPolygons(Collection<Polygon> polygons) {
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
                areaEdge = new Edge(segment);
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
                } else {
                    List<Edge> adjNode = nodes.get(edgeB.fromNode);
                    adjNode.remove(edgeB);
                    adjNode.add(edgeA);
                    edgeA.fromNode = edgeB.fromNode;
                }
            }
        }
    }

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

    /**
     * Validates all polygons. Get the result with {@link #createIntersectionExceptions(String)} or {@link #gatherInvalidGeometry()}.
     */
    public void validateAll() {
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

            intersector.computeIntersection(previous, next);
            if (intersector.getIntersectionNum() == 2 && !(intersector.isIntersection(next.getStartPoint()) && intersector.isIntersection(next.getEndPoint()))) {
                distance = Math.max(distance, intersector.getIntersection(0).distance(intersector.getIntersection(1)));
            }

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

    private static boolean isOnInside(Coordinate point, CurveSegment testSegment, CurveSegment firstSegment, CurveSegment secondSegment, double minDirectionPointDistance) {
        Coordinate first = getDirectionPoint(point, firstSegment, minDirectionPointDistance);
        Coordinate second = getDirectionPoint(point, secondSegment, minDirectionPointDistance);
        Coordinate test = getDirectionPoint(point, testSegment, minDirectionPointDistance);

        return isOnInside(point, first, second, test);
    }

    private static boolean isOnInside(Coordinate origin, Coordinate p0, Coordinate p1, Coordinate testPoint) {
        double first = getAngle(origin, p0);
        double second = getAngle(origin, p1);
        double test = getAngle(origin, testPoint);

        if (first < second) {
            return second < test || test < first;
        } else {
            return second < test && test < first;
        }
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
     * Represents a shared edge between {@link EdgeRing}s.
     */
    private static class Edge {
        Coordinate fromNode;
        Coordinate toNode;

        final List<EdgeRing> edgeRings = new ArrayList<EdgeRing>();
        final List<EdgeRing> reversedEdgeRings = new ArrayList<EdgeRing>();

        public Edge(CurveSegment segment) {
            this.fromNode = segment.getStartPoint();
            this.toNode = segment.getEndPoint();
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
}
