package ch.interlis.iox_j.wkb;

import com.vividsolutions.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PolylineCoordList implements Iterable<Coordinate> {
    private int ringId = -1;
    private int wkbType = WKBConstants.wkbLineString;
    private final List<Coordinate> coordinates;
    private final Map<Coordinate, Integer> coordinatesMap;

    public PolylineCoordList() {
        this.coordinates = new ArrayList<Coordinate>();
        this.coordinatesMap = new HashMap<Coordinate, Integer>();
    }

    public PolylineCoordList(int wkbType) {
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

    public boolean contains(Coordinate coordinate){
        return coordinatesMap.containsKey(coordinate);
    }

    public int getPosition(Coordinate coordinate){
        return coordinatesMap.get(coordinate);
    }

    public void add(Coordinate coordinate){
        coordinatesMap.put(coordinate,coordinates.size());
        coordinates.add(coordinate);
    }

    public Coordinate get(int index){
        return coordinates.get(index);
    }

    public int size(){
        return coordinates.size();
    }

    public PolylineCoordList splitTailAt(Coordinate coordinate){
        Integer pos = coordinatesMap.get(coordinate);
        List<Coordinate> tail = coordinates.subList(pos, coordinates.size());
        PolylineCoordList result = new PolylineCoordList(wkbType);

        for (Coordinate coord: tail) {
            coordinatesMap.remove(coord);
            result.add(coord);
        }

        tail.clear();
        return result;
    }

    public int getRingId() {
        return ringId;
    }

    public void setRingId(int ringId) {
        this.ringId = ringId;
    }

    @Override
    public Iterator<Coordinate> iterator() {
        return coordinates.iterator();
    }
}
