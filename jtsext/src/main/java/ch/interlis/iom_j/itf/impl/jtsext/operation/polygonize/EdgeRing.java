
package ch.interlis.iom_j.itf.impl.jtsext.operation.polygonize;

import java.util.*;

import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurveRing;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;

import com.vividsolutions.jts.algorithm.*;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.planargraph.*;

/**
 * Represents a ring of {@link PolygonizeDirectedEdge}s which form
 * a ring of a polygon.  The ring may be either an outer shell or a hole.
 */
class EdgeRing {

  /**
   * Find the innermost enclosing shell EdgeRing containing the argument EdgeRing, if any.
   * The innermost enclosing ring is the <i>smallest</i> enclosing ring.
   * The algorithm used depends on the fact that:
   * <br>
   *  ring A contains ring B iff envelope(ring A) contains envelope(ring B)
   * <br>
   * This routine is only safe to use if the chosen point of the hole
   * is known to be properly contained in a shell
   * (which is guaranteed to be the case if the hole does not touch its shell)
   *
   * @return containing EdgeRing, if there is one
   * or null if no containing EdgeRing is found
   */
  public static EdgeRing findEdgeRingContaining(EdgeRing testEr, List shellList)
  {
    LinearRing testRing = testEr.getRing();
    Envelope testEnv = testRing.getEnvelopeInternal();
    Coordinate testPt = testRing.getCoordinateN(0);

    EdgeRing minShell = null;
    Envelope minShellEnv = null;
    for (Iterator it = shellList.iterator(); it.hasNext(); ) {
      EdgeRing tryShell = (EdgeRing) it.next();
      LinearRing tryShellRing = tryShell.getRing();
      Envelope tryShellEnv = tryShellRing.getEnvelopeInternal();
      // the hole envelope cannot equal the shell envelope
      // (also guards against testing rings against themselves)
      if(isSameRing(tryShell,testEr)) continue;
      if (tryShellEnv.equals(testEnv)) continue;
      // hole must be contained in shell
      if (! tryShellEnv.contains(testEnv)) continue;
      
      testPt = CoordinateArrays.ptNotInList(testRing.getCoordinates(), tryShellRing.getCoordinates());
      boolean isContained = false;
      if (CGAlgorithms.isPointInRing(testPt, tryShellRing.getCoordinates()) )
        isContained = true;

      // check if this new containing ring is smaller than the current minimum ring
      if (isContained) {
        if (minShell == null
            || minShellEnv.contains(tryShellEnv)) {
          minShell = tryShell;
          minShellEnv = minShell.getRing().getEnvelopeInternal();
        }
      }
    }
    return minShell;
  }

  private static boolean isSameRing(EdgeRing tryShell, EdgeRing testEr) {
	  List edge1=DirectedEdge.toEdges(tryShell.deList);
	  List edge2=DirectedEdge.toEdges(testEr.deList);
	  int trySize=edge1.size();
	  if(trySize!=edge2.size()){
		  return false;
	  }
	  if(edge1.get(0)==edge2.get(0)){
		  for(int i=0;i<trySize;i++){
			  if(edge1.get(i)!=edge2.get(i)){
				  return false;
			  }
		  }
	  }else{
		  for(int i=0;i<trySize;i++){
			  if(edge1.get(i)!=edge2.get(trySize-i-1)){
				  return false;
			  }
		  }
	  }
	return true;
}

/**
   * Finds a point in a list of points which is not contained in another list of points
   * @param testPts the {@link Coordinate}s to test
   * @param pts an array of {@link Coordinate}s to test the input points against
   * @return a {@link Coordinate} from <code>testPts</code> which is not in <code>pts</code>,
   * or null if there is no coordinate not in the list
   * 
   * @deprecated Use CoordinateArrays.ptNotInList instead
   */
  public static Coordinate ptNotInList(Coordinate[] testPts, Coordinate[] pts)
  {
    for (int i = 0; i < testPts.length; i++) {
      Coordinate testPt = testPts[i];
      if (! isInList(testPt, pts))
          return testPt;
    }
    return null;
  }

  /**
   * Tests whether a given point is in an array of points.
   * Uses a value-based test.
   *
   * @param pt a {@link Coordinate} for the test point
   * @param pts an array of {@link Coordinate}s to test
   * @return <code>true</code> if the point is in the array
   * 
   * @deprecated
   */
  public static boolean isInList(Coordinate pt, Coordinate[] pts)
  {
    for (int i = 0; i < pts.length; i++) {
        if (pt.equals(pts[i]))
            return true;
    }
    return false;
  }
  private GeometryFactory factory;


  private List<DirectedEdge> deList = new ArrayList<DirectedEdge>();

  // cache the following data for efficiency
  private LinearRing ring = null;

  //private Coordinate[] ringPts = null;
  private CompoundCurve ringPts = null;
  private List holes;

  public EdgeRing(GeometryFactory factory)
  {
    this.factory = factory;
  }

  /**
   * Adds a {@link DirectedEdge} which is known to form part of this ring.
   * @param de the {@link DirectedEdge} to add.
   */
  public void add(DirectedEdge de)
  {
    deList.add(de);
  }

  /**
   * Tests whether this ring is a hole.
   * Due to the way the edges in the polyongization graph are linked,
   * a ring is a hole if it is oriented counter-clockwise.
   * @return <code>true</code> if this ring is a hole
   */
  public boolean isHole()
  {
    LinearRing ring = getRing();
	return CompoundCurveRing.isCCW(ring);
  }

/**
   * Adds a hole to the polygon formed by this ring.
   * @param hole the {@link LinearRing} forming the hole.
   */
  public void addHole(LinearRing hole) {
    if (holes == null)
      holes = new ArrayList();
    holes.add(hole);
  }

  /**
   * Computes the {@link Polygon} formed by this ring and any contained holes.
   *
   * @return the {@link Polygon} formed by this ring and its holes.
   */
  public Polygon getPolygon()
  {
    LinearRing[] holeLR = null;
    if (holes != null) {
      holeLR = new LinearRing[holes.size()];
      for (int i = 0; i < holes.size(); i++) {
        holeLR[i] = (LinearRing) holes.get(i);
      }
    }
    Polygon poly = factory.createPolygon(ring, holeLR);
    return poly;
  }

  /**
   * Tests if the {@link LinearRing} ring formed by this edge ring is topologically valid.
   * 
   * @return true if the ring is valid
   */
  public boolean isValid()
  {
    getCoordinates();
    if (ringPts.getCoordinates().length <= 3){
    	return false;
    }
    try{
    	getRing();
    }catch(Exception e){
    	return false;
    }
    return ring!=null && ring.isValid();
  }

  /**
   * Computes the list of coordinates which are contained in this ring.
   * The coordinatea are computed once only and cached.
   *
   * @return an array of the {@link Coordinate}s in this ring
   */
  private Coordinate[] getCoordinates()
  {
    if (ringPts == null) {
      java.util.ArrayList<CurveSegment> coordList = new java.util.ArrayList<CurveSegment>();
      for (Iterator i = deList.iterator(); i.hasNext(); ) {
        DirectedEdge de = (DirectedEdge) i.next();
        PolygonizeEdge edge = (PolygonizeEdge) de.getEdge();
        addEdge(edge.getLine(), de.getEdgeDirection(), coordList);
      }
      ringPts = ((JtsextGeometryFactory) factory).createCompoundCurve(coordList);
    }
    return ringPts.getCoordinates();
  }

  /**
   * Gets the coordinates for this ring as a {@link LineString}.
   * Used to return the coordinates in this ring
   * as a valid geometry, when it has been detected that the ring is topologically
   * invalid.
   * @return a {@link LineString} containing the coordinates in this ring
   */
  public LineString getLineString()
  {
    getCoordinates();
    return ringPts;
  }

  /**
   * Returns this ring as a {@link LinearRing}, or null if an Exception occurs while
   * creating it (such as a topology problem). Details of problems are written to
   * standard output.
   */
  public LinearRing getRing()
  {
    if (ring != null) return ring;
    getCoordinates();
    if (ringPts.getCoordinates().length < 3){
    	throw new IllegalStateException("not a ring "+ringPts);
    }
    ring = ((JtsextGeometryFactory) factory).createCompoundCurveRing(ringPts);
    return ring;
  }

  private static void addEdge(LineString lineString, boolean isForward, ArrayList<CurveSegment> newSegs)
  {
		if(lineString instanceof CompoundCurve){
		    if (isForward) {
		    	int segi=0;
		    	for(CurveSegment seg:((CompoundCurve) lineString).getSegments()){
	    			if(segi==0 && newSegs.size()>0){
		    			if(!newSegs.get(newSegs.size()-1).getEndPoint().equals2D(seg.getStartPoint())){
		    				throw new IllegalStateException("Start!=Last");
		    			}
		    		}
	    			newSegs.add(seg);
		    		segi++;
		    	}
		    }else{
		    	// Backward
		    	ArrayList<CurveSegment> segv = ((CompoundCurve) lineString).getSegments();
		        for (int segi = segv.size()-1; segi >= 0; segi--) {
		        	CurveSegment seg=segv.get(segi);
	    			if(segi==0 && newSegs.size()>0){
		    			if(!newSegs.get(newSegs.size()-1).getEndPoint().equals2D(seg.getEndPoint())){
		    				throw new IllegalStateException("Start!=Last");
		    			}
	    			}
	    			if(seg instanceof ArcSegment){
	    				ArcSegment newSeg=new ArcSegment(seg.getEndPoint(),((ArcSegment) seg).getMidPoint(),seg.getStartPoint());
	    				newSeg.setUserData(seg.getUserData());
		    			newSegs.add(newSeg);
	    			}else{
	    				StraightSegment newSeg=new StraightSegment(seg.getEndPoint(),seg.getStartPoint());
	    				newSeg.setUserData(seg.getUserData());
		    			newSegs.add(newSeg);
	    			}
		        }
		    }
		}else if(lineString instanceof CompoundCurveRing){
			throw new IllegalArgumentException("lineString instanceof CompoundCurveRing");
		}else{
			Coordinate[] coords = lineString.getCoordinates();
		    if (isForward) {
		        for (int i = 0; i < coords.length-1; i++) {
		    		if(newSegs.size()>0 && (i==0)){
		    			if(!newSegs.get(newSegs.size()-1).getEndPoint().equals2D(coords[i])){
		    				throw new IllegalStateException("Start!=Last");
		    			}
		    		}
	    			newSegs.add(new StraightSegment(coords[i],coords[i+1]));
		        }
		      }
		      else {
		        for (int i = coords.length - 1; i > 0; i--) {
	    			if(i==1){
		    			if(!newSegs.get(newSegs.size()-1).getEndPoint().equals2D(coords[i-1])){
		    				throw new IllegalStateException("Start!=Last");
		    			}
	    			}
	    			newSegs.add(new StraightSegment(coords[i],coords[i-1]));
		        }
		      }
			
		}
  }

public List<? extends DirectedEdge> getEdges() {
	return deList;
}
}
