package ch.interlis.iox_j.wkb;

import com.vividsolutions.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RingCollector {

    private final boolean repairSelfTouchingRing;
    private final LinkedList<List<LineSegment>> rings;
    private final Map<Coordinate, Integer> coordinates2Segment;
    private int currentRingIndex = -1;
    private Coordinate start;

    public RingCollector (boolean repairSelfTouchingRing){
        this.repairSelfTouchingRing = repairSelfTouchingRing;
        this.rings = new LinkedList();
        this.coordinates2Segment = new HashMap<Coordinate, Integer>();
    }

    private List<LineSegment> getCurrentRing(){
        return currentRingIndex >= 0 ? rings.get(currentRingIndex) : null;
    }

    public void startNewRing() {
        rings.addLast(new ArrayList<LineSegment>());
        rings.getLast().add(new LineSegment());
        currentRingIndex = rings.size() - 1;
        coordinates2Segment.clear();
        start =  null;
    }

    public void add(Coordinate coordinate, int WkbType){
        if (currentRingIndex < 0) {
            startNewRing();
        }

        if (start == null) {
            start = coordinate;
        }

        List<LineSegment> ring = getCurrentRing();
        LineSegment segment = ring.get(ring.size() - 1);

        if (!segment.trySetWkbType(WkbType)) {
            Coordinate lastCoord = segment.get(segment.size() - 1);
            segment = new LineSegment(WkbType);
            segment.add(lastCoord);
            ring.add(segment);
        }

        if (repairSelfTouchingRing && coordinates2Segment.containsKey(coordinate) && !start.equals(coordinate)){
            extractInnerRing(coordinate);
        }
        segment = ring.get(ring.size() - 1);
        segment.add(coordinate);
        coordinates2Segment.put(coordinate, ring.size() - 1);
    }

    private void extractInnerRing(Coordinate coordinate) {
        List<LineSegment> ring = getCurrentRing();
        int segmentIdx = coordinates2Segment.get(coordinate);
        LineSegment containingSegment = ring.get(segmentIdx);
        ArrayList<LineSegment> extractedRing = new ArrayList<LineSegment>();

        LineSegment tailSegment = containingSegment.splitTailAt(coordinate);
        if (tailSegment != null){
            extractedRing.add(tailSegment);
        } else if (containingSegment == tailSegment){
            ring.remove(tailSegment);
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

    public Coordinate getLastCooridnate() {
        List<LineSegment> ring = getCurrentRing();
        LineSegment segment = ring.get(ring.size() - 1);
        return segment.get(segment.size() - 1);
    }
}
