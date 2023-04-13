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
    private int currentRingIndex = -1;
    private Coordinate startOfCurrentRing;
    private Coordinate carryOverCoordinate;

    public RingCollector (boolean repairSelfTouchingRing){
        this.repairSelfTouchingRing = repairSelfTouchingRing;
        this.rings = new LinkedList<List<LineSegment>>();
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
        startOfCurrentRing =  null;
        carryOverCoordinate = null;
    }

    /**
     * Adds coordinate to current ring / line. Ensures segment with matching WKB type.
     * @param coordinate Coordinate to add to current ring.
     * @param WkbType Required type for the LineSegment the coordinate is added to.
     */
    private void add(Coordinate coordinate, int WkbType){
        if (carryOverCoordinate != null && repairSelfTouchingRing){
            Coordinate carry = carryOverCoordinate;
            startNewRing();
            getCurrentSegment().add(carry);
            startOfCurrentRing = carry;
        }

        List<LineSegment> ring = getCurrentRing();
        LineSegment segment = getCurrentSegment();

        // different/new segment required?
        if (!segment.trySetWkbType(WkbType)) {
            Coordinate lastCoord = segment.getLast();
            segment = new LineSegment(WkbType);
            segment.add(lastCoord);
            ring.add(segment);
        }

        if (startOfCurrentRing == null) {
            // new ring
            startOfCurrentRing = coordinate;
        }
        else if (startOfCurrentRing.equals(coordinate)){
            // not a new ring, but same coord as start of current ring
            // finish current ring, and keep same coord as start of a new ring
            carryOverCoordinate = coordinate;
        }
        else if (repairSelfTouchingRing && getSegmentIdx(getCurrentRing(),coordinate)!=null && !startOfCurrentRing.equals(coordinate)){
            // not a new ring, and coord on current ring but not the start of current ring
            extractInnerRing(coordinate);
        }

        segment = getCurrentSegment();
        segment.add(coordinate);
    }

    private void extractInnerRing(Coordinate coordinate) {
        List<LineSegment> ring = getCurrentRing();
        int segmentIdx = getSegmentIdx(ring,coordinate);
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
    }

    private Integer getSegmentIdx(List<LineSegment> ring, Coordinate coordinate) {
        for (int idx=0;idx<ring.size();idx++){
            LineSegment segment=ring.get(idx);
            if(segment.contains(coordinate)) {
                return idx;
            }
        }
        return null;
    }

    public List<List<LineSegment>> getRings() {
        return rings;
    }

    public Coordinate getLastCoordinate() {
        List<LineSegment> ring = getCurrentRing();
        if(ring==null) {
            return null;
        }
        LineSegment segment = ring.get(ring.size() - 1);
        if(segment.size()==0) {
            return null;
        }
        return segment.getLast();
    }

    public void addStraight(Coordinate coord) {
        add(coord, WKBConstants.wkbLineString);
    }

    public void addArc(Coordinate midPt, Coordinate endPt) {
        add(midPt, WKBConstants.wkbCircularString);
        add(endPt, WKBConstants.wkbCircularString);
    }
}
