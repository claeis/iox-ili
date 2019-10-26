package ch.interlis.iom_j.itf.impl.jtsext.geom;

import java.util.ArrayList;
import java.util.List;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iom_j.itf.impl.jtsext.io.WKTWriterJtsext;
import ch.interlis.iom_j.itf.impl.jtsext.noding.CompoundCurveNoder;
import ch.interlis.iom_j.itf.impl.jtsext.noding.Intersection;

import com.vividsolutions.jts.algorithm.CGAlgorithms;
import com.vividsolutions.jts.algorithm.LineIntersector;
import com.vividsolutions.jts.algorithm.RobustLineIntersector;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geomgraph.GeometryGraph;
import com.vividsolutions.jts.operation.valid.IsValidOp;

public class CompoundCurveRing extends LinearRing {

	private ArrayList<CompoundCurve> lines=null;
	public CompoundCurveRing(CompoundCurve line, GeometryFactory factory) {
		super(line.getCoordinateSequence(), factory);
		lines=new ArrayList<CompoundCurve>(1);
		lines.add(line);
	}
	public CompoundCurveRing(ArrayList<CompoundCurve> lines, GeometryFactory factory) {
		super(derivePoints(lines,factory), factory);
		this.lines=new ArrayList<CompoundCurve>(lines);
	}
	private static CoordinateSequence derivePoints(ArrayList<CompoundCurve> lines, GeometryFactory factory)
	{
		com.vividsolutions.jts.geom.CoordinateList ret=new com.vividsolutions.jts.geom.CoordinateList();
		for(CompoundCurve line:lines){
			ret.add(line.getCoordinates(),false);
		}
		return factory.getCoordinateSequenceFactory().create(ret.toCoordinateArray());
	}
	public ArrayList<CompoundCurve> getLines() {
		return lines;
	}
	public void setLines(CompoundCurve newline) {
		lines=new ArrayList<CompoundCurve>(1);
		lines.add(newline);
		points=derivePoints(lines,factory);
	}
	public static ArrayList<CurveSegment> normalizeRing(boolean clockwise, ArrayList<CurveSegment> segv) {
		// find ring orientation
		ArrayList<Coordinate> coordv=new ArrayList<Coordinate>();
		for(CurveSegment seg:segv){
			Coordinate coord=null;
			coord = seg.getStartPoint();
			coordv.add(coord);
			if(seg instanceof ArcSegment){
				coord = ((ArcSegment) seg).getMidPoint();
				coordv.add(coord);
			}
		}
		// close ring
		coordv.add(segv.get(0).getStartPoint());
		
		Coordinate[] coords=coordv.toArray(new Coordinate[coordv.size()]);
		ArrayList<CurveSegment> newsegv=new ArrayList<CurveSegment>();
		if (CGAlgorithms.isCCW(coords) == clockwise) {
			// reverse and scroll
			// find new start point
			Coordinate minCoord=null;
			int newStartSeg=0;
			int li=0;
			for(CurveSegment seg:segv){
				Coordinate coord=null;
				coord = seg.getEndPoint();
				if(minCoord==null || minCoord.compareTo(coord) > 0){
					minCoord=new Coordinate(coord);
					newStartSeg=li;
				}
				li++;
			}
			for (int i = newStartSeg; i >= 0; i--) {
				CurveSegment seg=segv.get(i);
				if(seg instanceof ArcSegment){
					newsegv.add(new ArcSegment(seg.getEndPoint(),((ArcSegment) seg).getMidPoint(),seg.getStartPoint()));
				}else{
					newsegv.add(new StraightSegment(seg.getEndPoint(),seg.getStartPoint()));
				}
			}
			for (int i = segv.size()-1; i > newStartSeg; i--) {
				CurveSegment seg=segv.get(i);
				if(seg instanceof ArcSegment){
					newsegv.add(new ArcSegment(seg.getEndPoint(),((ArcSegment) seg).getMidPoint(),seg.getStartPoint()));
				}else{
					newsegv.add(new StraightSegment(seg.getEndPoint(),seg.getStartPoint()));
				}
			}
		}else{
			// only scroll
			// find new start point
			Coordinate minCoord=null;
			int newStartSeg=0;
			int li=0;
			for(CurveSegment seg:segv){
				Coordinate coord=null;
				coord = seg.getStartPoint();
				if(minCoord==null || minCoord.compareTo(coord) > 0){
					minCoord=new Coordinate(coord);
					newStartSeg=li;
				}
				li++;
			}
			for (int i = newStartSeg; i <= segv.size()-1; i++) {
				CurveSegment seg=segv.get(i);
				newsegv.add(seg);
			}
			for (int i = 0; i < newStartSeg; i++) {
				CurveSegment seg=segv.get(i);
				newsegv.add(seg);
			}
		}
		return newsegv;
	}
	@Override
	public boolean isValid() {
		for(Coordinate coord:getCoordinates()){
		    if(!IsValidOp.isValid(coord)){
		    	return false;
		    }
		}
	    if(!isClosed()){
	    	return false;
	    }
	    if(getCoordinates().length<4){
	    	return false;
	    }
	    CompoundCurveNoder validator=new CompoundCurveNoder(lines, true);
		boolean ret= validator.isValid();
		if(ret==false){
			List<Intersection> isv = validator.getIntersections();
			for(Intersection is:isv){
				EhiLogger.traceState("invalid CompoundCurveRing: "+is.toString());
			}
		}
		return ret;
	}
	@Override
	public String toText() {
	    WKTWriterJtsext writer = new WKTWriterJtsext();
	    return writer.write(this);
	}
	@Override
	  protected Envelope computeEnvelopeInternal() {
		Envelope ret= new Envelope();
	    if (isEmpty()) {
	        return ret;
	      }
	    
		for(CompoundCurve line:lines){
		    for(CurveSegment seg:line.getSegments()){
		    	ret=seg.expandEnvelope(ret);
		    }
		}
	      return ret;
	  }
	public static boolean isCCW(LinearRing ring) {
	    if(ring instanceof CompoundCurveRing) {
	    	if(false) {
	    		// find ring orientation
	        	ArrayList<CompoundCurve> lines = ((CompoundCurveRing)ring).getLines();
	    		ArrayList<Coordinate> coordv=new ArrayList<Coordinate>();
	        	for(CompoundCurve line:lines) {
	        		for(CurveSegment seg:line.getSegments()) {
	        			Coordinate coord=null;
	        			coord = seg.getStartPoint();
	        			coordv.add(coord);
	        			if(seg instanceof ArcSegment){
	        				coord = ((ArcSegment) seg).getMidPoint();
	        				coordv.add(coord);
	        			}
	        		}
	        	}
	    		// close ring
	    		coordv.add(coordv.get(0));
	    		Coordinate[] coords=coordv.toArray(new Coordinate[coordv.size()]);
	    	    return CGAlgorithms.isCCW(coords);
	    		
	    	}else if(true){
	    		ArrayList<CurveSegment> segs=new ArrayList<CurveSegment>();
	    		for(CompoundCurve line:((CompoundCurveRing)ring).getLines()) {
	    			segs.addAll(line.getSegments());
	    		}
	    		int topIdx = 0;
	    		Envelope topEnv= segs.get(topIdx).computeEnvelopeInternal();
	    		for(int i=1;i<segs.size();i++) {
	    			CurveSegment seg=segs.get(i);
	    			Envelope segEnv= seg.computeEnvelopeInternal();
	    			if(segEnv.getMaxY()>topEnv.getMaxY()) {
	    				topIdx=i;
	    				topEnv=segEnv;
	    			}
	    		}
	    		CurveSegment top=segs.get(topIdx);
	    		if(top instanceof ArcSegment && ((ArcSegment) top).getSign()!=0.0 && top.getStartPoint().y<topEnv.getMaxY() && top.getEndPoint().y<topEnv.getMaxY()) {
	    			// maxY ist Zwischenpunkt auf dem Bogen
	    			Coordinate[] coords=new Coordinate[4];
	    			coords[0]=top.getStartPoint();
	    			coords[1]=((ArcSegment) top).getMidPoint();
	    			coords[2]=top.getEndPoint();
	    			coords[3]=top.getStartPoint();
	    		    return CGAlgorithms.isCCW(coords);
	    		}
	    		CurveSegment seg0=null;
	    		CurveSegment seg1=null;
	    		if(top.getStartPoint().y==topEnv.getMaxY()) {
	    			// segment davor relevant
	    			int seg0Idx=topIdx-1;
	    			do {
		    			if(seg0Idx<0) {
		    				seg0Idx=segs.size()-1;
		    			}
		    			seg0=segs.get(seg0Idx);
		    			seg0Idx--;
	    			}while(seg0.getStartPoint().y==topEnv.getMaxY() && seg0 instanceof StraightSegment); // falls seg0 eine horizontale Gerade naechstes davor liegendes Segment
	    			seg1=top;
	    		}else {
	    			// segmment danach relevant
	    			int seg1Idx=topIdx+1;
	    			do {
		    			if(seg1Idx>=segs.size()) {
		    				seg1Idx=0;
		    			}
		    			seg1=segs.get(seg1Idx);
		    			seg1Idx++;
	    			}while(seg1.getEndPoint().y==topEnv.getMaxY() && seg1 instanceof StraightSegment); // falls seg1 eine horizontale Gerade naechstes danach liegendes Segment
	    			seg0=top;
	    		}
	    		Coordinate[] coords=null;
	    		if(seg0.getStartPoint().equals2D(seg1.getEndPoint())) {
	                coords=new Coordinate[4];
		    		coords[0]=seg0.getStartPoint();
		    		coords[1]=(seg0 instanceof StraightSegment)?seg0.getEndPoint():((ArcSegment) seg0).getDirectionPt(false,CompoundCurve.ARC_MIN_DIRECTION_PT);
		    		coords[2]=(seg1 instanceof StraightSegment)?seg1.getStartPoint():((ArcSegment) seg1).getDirectionPt(true,CompoundCurve.ARC_MIN_DIRECTION_PT);
		    		coords[3]=coords[0];
	    		}else {
                    coords=new Coordinate[5];
		    		coords[0]=(seg0 instanceof StraightSegment)?seg0.getStartPoint():((ArcSegment) seg0).getDirectionPt(false,CompoundCurve.ARC_MIN_DIRECTION_PT);
		    		coords[1]=seg0.getEndPoint();
                    coords[2]=seg1.getStartPoint();
		    		coords[3]=(seg1 instanceof StraightSegment)?seg1.getEndPoint():((ArcSegment) seg1).getDirectionPt(true,CompoundCurve.ARC_MIN_DIRECTION_PT);
		    		coords[4]=coords[0];
	    		}
	    		return CGAlgorithms.isCCW(coords);
	        }
	    }
	    return CGAlgorithms.isCCW(ring.getCoordinates());
	}

}
