package ch.interlis.iox_j.wkb;

import com.vividsolutions.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A LineSegment represents a List of Coordinates representing a Segment of a Polyline with matching WKB type.
 * The class supports splitting the Segment in two parts.
 */
public class LineSegment implements Iterable<Coordinate> {
    private int wkbType = WKBConstants.wkbLineString;
    private final List<Coordinate> coordinates;
    private final Map<Coordinate, Integer> coordinatesMap;

    public LineSegment() {
        this.coordinates = new ArrayList<Coordinate>();
        this.coordinatesMap = new HashMap<Coordinate, Integer>();
    }

    public LineSegment(int wkbType) {
        this();
        this.wkbType = wkbType;
    }

    public int getWkbType() {
        return wkbType;
    }

    public boolean trySetWkbType(int wkbType) {
        if (coordinates.size() > 1){
            return this.wkbType == wkbType;
        }
        else {
            this.wkbType = wkbType;
            return true;
        }
    }

    public void add(Coordinate coordinate){
        coordinatesMap.put(coordinate,coordinates.size());
        coordinates.add(coordinate);
    }

    public Coordinate get(int index){
        return coordinates.get(index);
    }

    public Coordinate getLast() {
        return get(size() - 1);
    }

    public int size(){
        return coordinates.size();
    }

    /**
     * Splits & returns the tail of this segment after the provided Coorinate.
     * @param coordinate Coordinate to split the tail.
     * @return New LineSegment inclusive coordinate or null if only coorinate in tail.
     */
    public LineSegment splitTailAt(Coordinate coordinate){
        int pos = coordinatesMap.get(coordinate);
        if (pos == coordinates.size() - 1) return null;
        if(pos!=0 && pos%2==0 && wkbType==WKBConstants.wkbCircularString) {
            throw new IllegalArgumentException("A mid-point of an ARC can not start a ring "+coordinate.toString());
        }
        
        List<Coordinate> tail = coordinates.subList(pos, coordinates.size());
        LineSegment result = new LineSegment(wkbType);

        for (Coordinate coord: tail) {
            coordinatesMap.remove(coord);
            result.add(coord);
        }

        tail.clear();

        return result;
    }

    @Override
    public Iterator<Coordinate> iterator() {
        return coordinates.iterator();
    }

    public boolean contains(Coordinate coordinate) {
        return coordinatesMap.containsKey(coordinate);
    }
}
