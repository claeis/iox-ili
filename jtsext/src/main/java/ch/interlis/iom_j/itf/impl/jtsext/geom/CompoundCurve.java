package ch.interlis.iom_j.itf.impl.jtsext.geom;

import java.util.ArrayList;
import java.util.List;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iom_j.itf.impl.hrg.HrgUtility;
import ch.interlis.iom_j.itf.impl.jtsext.algorithm.CurveSegmentIntersector;
import ch.interlis.iom_j.itf.impl.jtsext.io.WKTWriterJtsext;

import com.vividsolutions.jts.algorithm.CGAlgorithms;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateArrays;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTWriter;

public class CompoundCurve extends LineString {

	public static final String OVERLAP_TID_TAG = "overlap-";
	public static final String MODIFIED_TID_TAG = "modified-";
	private ArrayList<CurveSegment> segments=null;
	public static final double ARC_MIN_DIRECTION_PT = 0.1;
	public CompoundCurve(List<CurveSegment> segments, GeometryFactory factory) {
		super(derivePoints(segments, factory), factory);
		this.segments=new ArrayList<CurveSegment>(segments);
	}

	private static CoordinateSequence derivePoints(
			List<CurveSegment> segments, GeometryFactory factory) {
		com.vividsolutions.jts.geom.CoordinateList ret=new com.vividsolutions.jts.geom.CoordinateList();
		for(CurveSegment seg:segments){
			ret.add(seg.getCoordinates(),false);
		}
		return factory.getCoordinateSequenceFactory().create(ret.toCoordinateArray());
	}

	public ArrayList<CurveSegment> getSegments() {
		return segments;
	}

	public int getNumSegments() {
		return segments.size();
	}

	@Override
	public Point getStartPoint() {
		return factory.createPoint(segments.get(0).getStartPoint());
	}

	@Override
	public Point getEndPoint() {
		return factory.createPoint(segments.get(segments.size()-1).getEndPoint());
	}

	@Override
	public Geometry reverse() {
    	ArrayList<CurveSegment> newsegv=new ArrayList<CurveSegment>();
		for (int i = segments.size()-1; i >= 0; i--) {
			CurveSegment seg=segments.get(i);
			if(seg instanceof ArcSegment){
				newsegv.add(new ArcSegment(seg.getEndPoint(),((ArcSegment) seg).getMidPoint(),seg.getStartPoint()));
			}else{
				newsegv.add(new StraightSegment(seg.getEndPoint(),seg.getStartPoint()));
			}
		}
		return ((JtsextGeometryFactory) factory).createCompoundCurve(newsegv);
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
	    
	    for(CurveSegment seg:segments){
	    	ret=seg.expandEnvelope(ret);
	    }
	      return ret;
	  }

	/** remove overlap in given segment.
	 * @param atStartOfLine true if overlap is at start of line, else false. First (true) or last (false) must be an ArcSegment.
	 * @param otherSegment segment that overlaps with this. If an ArcSegment, its radius must be greather than radius of this. 
	 * @param newVertexOffset used to calculate new vertex
	 * @return true if line has changed
	 */
	public boolean removeOverlap(ArcSegment thisSegment, CurveSegment otherSegment,
			double newVertexOffset) {
		//CurveSegment thisSegmentO=segments.get(atStartOfLine ? 0 : getNumSegments()-1);
		int thisSegmentIdx=segments.indexOf(thisSegment);
		boolean atStartOfLine=thisSegment.getStartPoint().equals2D(otherSegment.getStartPoint()) || thisSegment.getStartPoint().equals2D(otherSegment.getEndPoint()); 
		CurveSegment newSegment=null;
		Coordinate newVertexPt=null;
		boolean replaceArcWithStraight = false;
		if(otherSegment instanceof ArcSegment){
			// move otherSegment in direction of thisSegment center
			// by increasing or decreasing its radius
			double otherRadius=((ArcSegment) otherSegment).getRadius();
			if(thisSegment.getRadius()>otherRadius){
				throw new IllegalStateException("thisSegment.radius>otherSegment.radius");
				
			}
			double newRadius=0.0;
			double segCenterDist = CurveSegment.dist(((ArcSegment) otherSegment).getCenterPoint().x,((ArcSegment) otherSegment).getCenterPoint().y,thisSegment.getCenterPoint().x,thisSegment.getCenterPoint().y);
			if(segCenterDist>otherRadius){
				newRadius=otherRadius+newVertexOffset;
			}else{
				newRadius=otherRadius-newVertexOffset;
			}
			Coordinate otherSegCenter=((ArcSegment) otherSegment).getCenterPoint();
			Coordinate newStartPt;
			Coordinate newMidPt;
			Coordinate newEndPt;
			newStartPt = newRadius(newRadius, otherSegment.getStartPoint(), otherSegCenter);
			newMidPt = newRadius(newRadius, ((ArcSegment) otherSegment).getMidPoint(), otherSegCenter);
			newEndPt = newRadius(newRadius, otherSegment.getEndPoint(), otherSegCenter);
			ArcSegment helperSeg=new ArcSegment(newStartPt,newMidPt,newEndPt);
			
			// calculate new intersectionPt
			CurveSegmentIntersector li = new CurveSegmentIntersector();
			  li.computeIntersection(thisSegment, helperSeg);
			  if(!li.hasIntersection()){
				  // replace arc with a straight
				  replaceArcWithStraight = true;
			  }else	if(li.getIntersectionNum()!=1){
				throw new IllegalStateException("unexpected number of intersections");
			  }else{
					Coordinate is=li.getIntersection(0);
					newVertexPt=new Coordinate(is);
			  }
			
			if(!replaceArcWithStraight){
				// calulate mid pt of new arc
				Coordinate newArcMidPt=null;
				{
					if(!atStartOfLine){
						// modify end of segment
						double s = CurveSegment.dist(newVertexPt,thisSegment.getEndPoint());
						double s2=s/2.0;
						double h=otherRadius-Math.sqrt(otherRadius*otherRadius-s2*s2);
						if(CGAlgorithms.computeOrientation(newVertexPt,thisSegment.getEndPoint(), otherSegCenter)>0){
							newArcMidPt=calcKleinp(newVertexPt,thisSegment.getEndPoint(),s2,-h);						
						}else{
							newArcMidPt=calcKleinp(newVertexPt,thisSegment.getEndPoint(),s2,h);						
						}
					}else{
						// modify start of segment
						double s = CurveSegment.dist(thisSegment.getStartPoint(),newVertexPt);
						double s2=s/2.0;
						double h=otherRadius-Math.sqrt(otherRadius*otherRadius-s2*s2);
						if(CGAlgorithms.computeOrientation(thisSegment.getStartPoint(),newVertexPt, otherSegCenter)>0){
							newArcMidPt=calcKleinp(thisSegment.getStartPoint(),newVertexPt,s2,-h);
						}else{
							newArcMidPt=calcKleinp(thisSegment.getStartPoint(),newVertexPt,s2,h);
						}
					}
				}
				
				// adjust thisSegment		
				if(!atStartOfLine){
					// modify end of segment
					newSegment=new ArcSegment(OVERLAP_TID_TAG+thisSegment.getUserData(),newVertexPt,newArcMidPt,thisSegment.getEndPoint());
					if(((ArcSegment)newSegment).isStraight()) {
	                    newSegment=new StraightSegment(OVERLAP_TID_TAG+thisSegment.getUserData(),newVertexPt,thisSegment.getEndPoint());
					}
				}else{
					// modify start of segment
					newSegment=new ArcSegment(OVERLAP_TID_TAG+thisSegment.getUserData(),thisSegment.getStartPoint(),newArcMidPt,newVertexPt);
                    if(((ArcSegment)newSegment).isStraight()) {
                        newSegment=new StraightSegment(OVERLAP_TID_TAG+thisSegment.getUserData(),thisSegment.getStartPoint(),newVertexPt);
                    }
				}
			}
		}else{
			// otherSegment is a StraightSegment
			// move otherSegment in direction of thisSegment center
			double s;
			Coordinate newStartPt=null;
			Coordinate newEndPt=null;
			s = CurveSegment.dist(otherSegment.getStartPoint().x,otherSegment.getStartPoint().y,otherSegment.getEndPoint().x,otherSegment.getEndPoint().y);
			if(CGAlgorithms.computeOrientation(otherSegment.getStartPoint(),otherSegment.getEndPoint(), thisSegment.getCenterPoint())>0){
				newStartPt=calcKleinp(otherSegment.getStartPoint(),otherSegment.getEndPoint(),0.0,newVertexOffset);
				newEndPt=calcKleinp(otherSegment.getStartPoint(),otherSegment.getEndPoint(),s,newVertexOffset);
			}else{
				newStartPt=calcKleinp(otherSegment.getStartPoint(),otherSegment.getEndPoint(),0.0,-newVertexOffset);
				newEndPt=calcKleinp(otherSegment.getStartPoint(),otherSegment.getEndPoint(),s,-newVertexOffset);
				
			}

			CurveSegment movedOtherSeg=new StraightSegment(newStartPt,newEndPt);						
			
			// calculate new intersectionPt
			CurveSegmentIntersector li = new CurveSegmentIntersector();
			  li.computeIntersection(thisSegment, movedOtherSeg);
			  if(!li.hasIntersection()){
				  // replace arc with a straight
				  replaceArcWithStraight = true;
			  }else if(li.getIntersectionNum()!=1){
				throw new IllegalStateException("unexpected number of intersections");
			}else{

				Coordinate is=li.getIntersection(0);
				newVertexPt=new Coordinate(is);
				
				// adjust thisSegment		
				if(!atStartOfLine){
					// modify end of segment
					newSegment=new StraightSegment(OVERLAP_TID_TAG+thisSegment.getUserData(),newVertexPt,thisSegment.getEndPoint());
				}else{
					// modify start of segment
					newSegment=new StraightSegment(OVERLAP_TID_TAG+thisSegment.getUserData(),thisSegment.getStartPoint(),newVertexPt);
				}
			}
			
			
		}
		if(replaceArcWithStraight){
			StraightSegment modifiedSegment=new StraightSegment(MODIFIED_TID_TAG+thisSegment.getUserData(),thisSegment.getStartPoint(),thisSegment.getEndPoint());
			segments.set(thisSegmentIdx, modifiedSegment);
		}else{
			ArcSegment modifiedSegment=null;
			if(!atStartOfLine){
				// make sure, midpt is on new arc
				Coordinate midPt=thisSegment.getMidPoint();
				{
					Coordinate start=thisSegment.getStartPoint();
					Coordinate end=newVertexPt;
					Coordinate center=thisSegment.getCenterPoint();
					double r=thisSegment.getRadius();
					double sign=thisSegment.sign;
					midPt = adjustMidPt(start, midPt, end, center, r, sign);
				}
				// modified next to last segment
				modifiedSegment=new ArcSegment(MODIFIED_TID_TAG+thisSegment.getUserData(),thisSegment.getStartPoint(),midPt,newVertexPt);
				segments.set(thisSegmentIdx, modifiedSegment);
				segments.add(thisSegmentIdx+1,newSegment);
			}else{
				// make sure, midpt is on new arc
				Coordinate midPt=thisSegment.getMidPoint();
				{
					Coordinate start=newVertexPt;
					Coordinate end=thisSegment.getEndPoint();
					Coordinate center=thisSegment.getCenterPoint();
					double r=thisSegment.getRadius();
					double sign=thisSegment.sign;
					midPt = adjustMidPt(start, midPt, end, center, r, sign);
				}
				// modified second segment
				modifiedSegment=new ArcSegment(MODIFIED_TID_TAG+thisSegment.getUserData(),newVertexPt,midPt,thisSegment.getEndPoint());
				segments.set(thisSegmentIdx, modifiedSegment);
				segments.add(thisSegmentIdx,newSegment);
			}
		}
		
		// recalc points
		points=derivePoints(segments, factory);
		envelope=null;
		// line changed
		return true;
	}

	private Coordinate newRadius(double newRadius, Coordinate oldPt, Coordinate center) {
		Coordinate newPt=new Coordinate();
		// Richtung des Punktes 
		double alpha=Math.atan2(oldPt.x-center.x,oldPt.y-center.y);
		newPt.x=center.x + newRadius * Math.sin(alpha);
		newPt.y=center.y + newRadius * Math.cos(alpha);
		return newPt;
	}

	private Coordinate adjustMidPt(Coordinate start, Coordinate midPt,
			Coordinate end, Coordinate center, double radius, double sign) {
		double ALFA =  HrgUtility.PSECOS (start.x,start.y,center.x,center.y,end.x,end.y);
		double BETA =  HrgUtility.PSECOS (start.x,start.y,center.x,center.y,midPt.x,midPt.y);
		if( ALFA == BETA || Math.signum(BETA-ALFA)==sign){
			// old midpt lies inside the new arc
		}else{
			midPt = ArcSegment.calcArcPt(start, end, center, radius, sign);
		}
		return midPt;
	}

	/** calculate a point from a vector
	 * @param p0 start point
	 * @param p1 end point
	 * @param u length on given vector
	 * @param v offset from given vector. positive is left, negative is right
	 * @return
	 */
	public static Coordinate calcKleinp(Coordinate p0,Coordinate p1,double u, double v)
	{
		double s=CurveSegment.dist(p0.x,p0.y,p1.x,p1.y);
		double o=(p1.y-p0.y)/s;
		double a=(p1.x-p0.x)/s;
		Coordinate ret=new Coordinate();
		ret.x=p0.x+a*u-o*v;
		ret.y=p0.y+o*u+a*v;
		return ret;
	}

	public void setSegmentsUserData(Object obj) {
		for(CurveSegment seg:segments){
			seg.setUserData(obj);
		}
		
	}
	public String[] getSegmentTids() {
		java.util.HashSet<String> uniqueTids=new java.util.HashSet<String>();
		for(CurveSegment seg:getSegments()){
			if(seg.getUserData()!=null){
				String tid=seg.getUserData().toString();
				if(!uniqueTids.contains(tid)){
					uniqueTids.add(tid);
				}
			}
		}
		return uniqueTids.toArray(new String[uniqueTids.size()]);
	}
	public void dumpLineAsJava(String segs) {
		for(CurveSegment seg:segments){
			if(seg instanceof StraightSegment){
				StraightSegment s=(StraightSegment) seg;
				EhiLogger.debug(segs+".add(new StraightSegment(\""+s.getUserData()+"\",new Coordinate("+s.getStartPoint().x+", "+s.getStartPoint().y+"),new Coordinate( "+s.getEndPoint().x+", "+s.getEndPoint().y+")));");
			}else{
				ArcSegment s=(ArcSegment) seg;
				EhiLogger.debug(segs+".add(new ArcSegment(\""+s.getUserData()+"\",new Coordinate("+s.getStartPoint().x+", "+s.getStartPoint().y+"),new Coordinate("+s.getMidPoint().x+", "+s.getMidPoint().y+"),new Coordinate( "+s.getEndPoint().x+", "+s.getEndPoint().y+")));");
			}
		}
		
	}

	public static Coordinate getDirectionPt(LineString line, boolean atStart,double dist) {
		if(line instanceof CompoundCurveRing){
			if(atStart){
				line=((CompoundCurveRing)line).getLines().get(0);
			}else{
				ArrayList<CompoundCurve> lines = ((CompoundCurveRing)line).getLines();
				line=lines.get(lines.size()-1);
			}
		}
		if(line instanceof CompoundCurve){
			ArrayList<CurveSegment> segs = ((CompoundCurve) line).getSegments();
			CurveSegment seg=null;
			if(atStart){
				seg=segs.get(0);
				if(seg instanceof StraightSegment){
					return seg.getEndPoint();
				}
			}else{
				seg=segs.get(segs.size()-1);
				if(seg instanceof StraightSegment){
					return seg.getStartPoint();
				}
			}
			if(dist==0.0) {
				dist=ARC_MIN_DIRECTION_PT;
			}
			Coordinate directionPt=((ArcSegment)seg).getDirectionPt(atStart,dist);
			return directionPt;
		}	
		
	    Coordinate[] linePts = CoordinateArrays.removeRepeatedPoints(line.getCoordinates());
		if(atStart){
		    Coordinate startPt = linePts[1];
		    return startPt;
		}else{
		    Coordinate endPt = linePts[linePts.length - 2];			    
		    return endPt;
		}
	}
	public static boolean isArc(LineString line, boolean atStart) {
		if(line instanceof CompoundCurveRing){
			if(atStart){
				line=((CompoundCurveRing)line).getLines().get(0);
			}else{
				ArrayList<CompoundCurve> lines = ((CompoundCurveRing)line).getLines();
				line=lines.get(lines.size()-1);
			}
		}
		if(line instanceof CompoundCurve){
			ArrayList<CurveSegment> segs = ((CompoundCurve) line).getSegments();
			CurveSegment seg=null;
			if(atStart){
				seg=segs.get(0);
				if(seg instanceof StraightSegment){
					return false;
				}
			}else{
				seg=segs.get(segs.size()-1);
				if(seg instanceof StraightSegment){
					return false;
				}
			}
			return true;
		}	
		return false;
	}

}
