package ch.interlis.iox_j.wkb;

import com.vividsolutions.jts.geom.Coordinate;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;

import java.util.ArrayList;
import java.util.List;

/**
 * RingCollector is used to collect & repair rings or lines in iox format. If repairSelfTouchingRing flag is set the
 * RingCollector extracts inner rings into separate rings to fulfill OCG validity criteria "Rings may not self-intersect".
 * Inner ring extraction may only work, if the first added coordinate is on the outer ring.
 * RingCollector does create LineSegments with matching WKB type automatically.
 */
public class RingCollector {

    private CompoundCurve shell=null;
    private List<CompoundCurve> rings=new ArrayList<CompoundCurve>();
    private List<CurveSegment> segs=new ArrayList<CurveSegment>();
    private List<Integer> starts=new ArrayList<Integer>();

    public RingCollector (){
    }


    private void addSegment(CurveSegment seg){
        int startSegOfRing=getSegIndexInLastPolyline(seg.getEndPoint());
        segs.add(seg);
        // Ring zu Ende?
        if(startSegOfRing>=0) {
            CompoundCurve ring=new CompoundCurve(segs.subList(startSegOfRing,segs.size()),new JtsextGeometryFactory());
            rings.add(ring);
            segs=segs.subList(0,startSegOfRing);
            // Start des Randes==Start der Linie?
            if(startSegOfRing==starts.get(starts.size()-1)) {
                // Linie zu Ende
                starts.remove(starts.size()-1);
            }
        }
    }

    private int getSegIndexInLastPolyline(Coordinate endPoint) {
        for(int idx=starts.get(starts.size()-1);idx<segs.size();idx++) {
            if(segs.get(idx).getStartPoint().equals(endPoint)) {
                return idx;
            }
        }
        return -1;
    }


    private void buildRings() {
        if(shell==null) {
            if(true) {
                if(starts.size()==1 && segs.size()>1){
                    // letzte Linie ist nicht geschlossen
                    // letzte Linie offen hinzufuegen
                    CompoundCurve ring=new CompoundCurve(segs,new JtsextGeometryFactory());
                    rings.add(ring);
                }else if(starts.size()==0 && segs.size()==0){
                    // ok
                }else {
                    throw new IllegalStateException("starts.size() "+starts.size()+", segs.size() "+segs.size());
                }
            }else {
                if(starts.size()==1 && segs.size()>1){
                    // letzte Linie ist nicht geschlossen
                    // letzte Linie abschliessen
                    addSegment(new StraightSegment(segs.get(segs.size()-1).getEndPoint(),segs.get(0).getStartPoint()));
                }else if(starts.size()==0 && segs.size()==0){
                    // ok
                }else {
                    throw new IllegalStateException("starts.size() "+starts.size()+", segs.size() "+segs.size());
                }
            }
            for(CompoundCurve ring:rings){
                if(shell==null) {
                    shell=ring;
                }else {
                    if(ring.getEnvelopeInternal().contains(shell.getEnvelopeInternal())) {
                        shell=ring;
                    }
                }
            }
            rings.remove(shell);
            rings.add(0,shell);
        }
    }
    public List<CompoundCurve> getRings() {
        buildRings();
        return rings;
    }

    public void addLine(CompoundCurve poly) {
        // neue Linie?
        if(segs.size()==0 || !segs.get(segs.size()-1).getEndPoint().equals(poly.getSegments().get(0).getStartPoint())) {
            starts.add(segs.size());
        }else {
            // Fortsetzung der letzten Linie
        }
        for(int idx=0;idx<poly.getSegments().size();idx++) {
            CurveSegment seg=poly.getSegments().get(idx);
            addSegment(seg);
            if(starts.size()==0 && idx+1<poly.getSegments().size()) {
                // neuer Rand
                starts.add(0);
            }
        }
    }
}
