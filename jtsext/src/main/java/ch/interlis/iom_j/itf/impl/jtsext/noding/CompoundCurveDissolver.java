package ch.interlis.iom_j.itf.impl.jtsext.noding;

import java.util.*;

import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurveRing;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.noding.OrientedCoordinateArray;

public class CompoundCurveDissolver
{

  private Set<CompoundCurve> ocaMap = new TreeSet<CompoundCurve>(new Comparator<CompoundCurve>() {

	@Override
	public int compare(CompoundCurve arg0, CompoundCurve arg1) {
		int numSegs=arg0.getNumSegments();
		int numSegs1=arg1.getNumSegments();
		if(numSegs<numSegs1){
			return 1;
		}else if(numSegs>numSegs1){
			return -1;
		}
		// ASSERT: number of Segments are equal
		Coordinate startPt0=arg0.getStartPoint().getCoordinate();
		Coordinate startPt1=arg1.getStartPoint().getCoordinate();
		Coordinate endPt0=arg0.getEndPoint().getCoordinate();
		Coordinate endPt1=arg1.getEndPoint().getCoordinate();
		int ret=startPt0.compareTo(startPt1);
		if(ret==0){
			// start pts are equal
			ArrayList<CurveSegment> segs0 = arg0.getSegments();
			ArrayList<CurveSegment> segs1 = arg1.getSegments();
			for(int i=0;i<numSegs;i++){
				CurveSegment seg0=segs0.get(i);
				CurveSegment seg1=segs1.get(i);
				ret=seg0.getEndPoint().compareTo(seg1.getEndPoint());
				if(ret!=0){
					return ret;
				}
				if(seg0 instanceof ArcSegment){
					if(seg1 instanceof ArcSegment){
					// compare center point
						ret=((ArcSegment) seg0).getCenterPoint().compareTo(((ArcSegment) seg1).getCenterPoint());
						if(ret!=0){
							return ret;
						}
					}else{
						// seg1 is a StraightSegment
						return -1;
					}
				}else{
					// seg0 is a StraightSegment
					if(seg1 instanceof ArcSegment){
						return 1;
					}
				}
			}
		}
		return ret;
	}
});



  public CompoundCurveDissolver() {
  }

  /**
   * Dissolve all {@link CompoundCurve}s in the input {@link Collection}
   * @param segStrings
   */
  public void dissolve(Collection<? extends CompoundCurve> segStrings)
  {
    for (Iterator<? extends CompoundCurve> i = segStrings.iterator(); i.hasNext(); ) {
      dissolve(i.next());
    }
  }

  /**
   * Dissolve the given {@link CompoundCurve}.
   *
   * @param segString the string to dissolve
   */
  public void dissolve(CompoundCurve segString)
  {
	//segString.normalize();
	if(segString.getStartPoint().getCoordinate().equals2D(segString.getEndPoint().getCoordinate())){
		// it is a ring
		// make same startpt, and same direction
    	ArrayList<CurveSegment> segv=new ArrayList<CurveSegment>();
		for(CurveSegment seg:segString.getSegments()){
			segv.add(seg);
		}
    	ArrayList<CurveSegment> newsegv=CompoundCurveRing.normalizeRing(true, segv);
		segString = ((JtsextGeometryFactory) segString.getFactory()).createCompoundCurve(newsegv);
	}else{
		// not a ring
		if(segString.getStartPoint().compareTo(segString.getEndPoint())>0){
			segString=(CompoundCurve) segString.reverse();
		}
	}
    if (!ocaMap.contains(segString)) {
      ocaMap.add(segString);
    }
  }

  /**
   * Gets the collection of dissolved (i.e. unique) {@link CompoundCurve}s
   *
   * @return the unique {@link CompoundCurve}s
   */
  public Collection<CompoundCurve> getDissolved() { return ocaMap; }
}



