package ch.interlis.iox_j.wkb;

import com.vividsolutions.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * RingCollector is used to collect & repair rings or lines in iox format. If repairSelfTouchingRing flag is set the
 * RingCollector extracts inner rings into separate rings to fulfill OCG validity criteria "Rings may not self-intersect".
 * Inner ring extraction may only work, if the first added coordinate is on the outer ring.
 * RingCollector does create LineSegments with matching WKB type automatically.
 */
public class RingCollector {

    private final boolean repairSelfTouchingRing;
    private final LinkedList<List<LineSegment>> rings;
    private final Map<Coordinate, Integer> coordinates2Segment;
    private int currentRingIndex = -1;
    private Coordinate start;
    private Coordinate carryOverCoordinate;

    public RingCollector (boolean repairSelfTouchingRing){
        this.repairSelfTouchingRing = repairSelfTouchingRing;
        this.rings = new LinkedList();
        this.coordinates2Segment = new HashMap<Coordinate, Integer>();
    }

    private List<LineSegment> getCurrentRing(){
        return currentRingIndex >= 0 ? rings.get(currentRingIndex) : null;
    }

    private LineSegment getCurrentSegment(){
        List<LineSegment> currentRing = getCurrentRing();
        return currentRing != null  ? currentRing.get(currentRing.size() - 1) : null;
    }

    /**
     * Initiates switch to new ring / line.
     */
    public void startNewRing() {
        rings.addLast(new ArrayList<LineSegment>());
        rings.getLast().add(new LineSegment());
        currentRingIndex = rings.size() - 1;
        coordinates2Segment.clear();
        start =  null;
        carryOverCoordinate = null;
    }

    /**
     * Adds coordinate to current ring / line. Ensures segment with matching WKB type.
     * @param coordinate Coordinate to add to current ring.
     * @param WkbType Required type for the LineSegment the coordinate is added to.
     */
    public void add(Coordinate coordinate, int WkbType){
        if (carryOverCoordinate != null && repairSelfTouchingRing){
            Coordinate carry = carryOverCoordinate;
            startNewRing();
            getCurrentSegment().add(carry);
            start = carry;
            coordinates2Segment.put(carry, 0);
        }

        List<LineSegment> ring = getCurrentRing();
        LineSegment segment = getCurrentSegment();

        if (!segment.trySetWkbType(WkbType)) {
            Coordinate lastCoord = segment.getLast();
            segment = new LineSegment(WkbType);
            segment.add(lastCoord);
            ring.add(segment);
        }

        if (start == null) {
            start = coordinate;
        }
        else if (start.equals(coordinate)){
            carryOverCoordinate = coordinate;
        }
        else if (repairSelfTouchingRing && coordinates2Segment.containsKey(coordinate) && !start.equals(coordinate)){
            extractInnerRing(coordinate);
        }

        segment = getCurrentSegment();
        segment.add(coordinate);
        coordinates2Segment.put(coordinate, getCurrentRing().size() - 1);
    }

    private void extractInnerRing(Coordinate coordinate) {
        List<LineSegment> ring = getCurrentRing();
        int segmentIdx = coordinates2Segment.get(coordinate);
        LineSegment containingSegment = ring.get(segmentIdx);
        ArrayList<LineSegment> extractedRing = new ArrayList<LineSegment>();

        LineSegment tailSegment = containingSegment.splitTailAt(coordinate);
        if (tailSegment != null){
            extractedRing.add(tailSegment);
        }

        while (segmentIdx + 1 < ring.size()){
            LineSegment segment = ring.remove(segmentIdx + 1);
            extractedRing.add(segment);
        }

        extractedRing.get(extractedRing.size() - 1).add(coordinate);

        rings.add(extractedRing);
        removeRingFromMap(extractedRing);
    }

    private void removeRingFromMap(List<LineSegment> ring){
        for (LineSegment segment : ring){
            for (Coordinate c : segment){
                coordinates2Segment.remove(c);
            }
        }
    }

    public List<List<LineSegment>> getRings() {
        return rings;
    }

    public Coordinate getLastCoordinate() {
        List<LineSegment> ring = getCurrentRing();
        LineSegment segment = ring.get(ring.size() - 1);
        return segment.getLast();
    }
}
