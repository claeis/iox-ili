package ch.ehi.iox.objpool.impl;

import java.util.ArrayList;
import java.util.Comparator;

import com.vividsolutions.jts.geom.Coordinate;

import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;

public class CompoundCurveComparator implements Comparator<CompoundCurve> {

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

}
