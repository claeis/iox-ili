package ch.interlis.iom_j.itf.impl.jtsext.geom;

import java.util.ArrayList;
import java.util.List;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iom_j.itf.impl.jtsext.noding.CompoundCurveNoder;
import ch.interlis.iom_j.itf.impl.jtsext.noding.Intersection;

import com.vividsolutions.jts.algorithm.CGAlgorithms;
import com.vividsolutions.jts.algorithm.LineIntersector;
import com.vividsolutions.jts.algorithm.RobustLineIntersector;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
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
		//if(ret==false){
		//	List<Intersection> isv = validator.getIntersections();
		//	for(Intersection is:isv){
		//		EhiLogger.debug("is "+is.getPt()[0]);
		//	}
		//}
		return ret;
	}

}
