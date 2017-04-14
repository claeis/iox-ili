package ch.interlis.iom_j.itf.impl.jtsext.noding;

import java.util.*;

import ch.ehi.iox.objpool.impl.CompoundCurveComparator;
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

  private Set<CompoundCurve> ocaMap = new TreeSet<CompoundCurve>(new CompoundCurveComparator());




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



