package ch.interlis.iom_j.itf.impl.jtsext.algorithm;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;

public class CurveSegmentIntersectorTest {

	@Test
	public void testFastGerade() {
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		StraightSegment s0=new StraightSegment(new Coordinate(611613.84, 233467.819), 
				new Coordinate(611610.392, 233468.995));
		ArcSegment s1=new ArcSegment(new Coordinate(611770.424, 234251.322), 
				new Coordinate(611770.171, 234250.059), 
				new Coordinate(611769.918, 234248.796));
		li.computeIntersection(s0, s1);
		assertFalse(li.hasIntersection());
		
	}
	@Test
	public void xx()
	{
		//intersection tid1 39, tid2 39, coord (623968.142, 236523.414, NaN), coord2 (623968.140910249, 236523.41334073577, NaN)
		//Info: buildSurfaces(): overlap 6.762210347233122E-8, seg1 (623963.955 236520.881, 623968.142 236523.414), seg2 CIRCULARSTRING (623968.142 236523.414, 623965.805 236523.085, 623963.954 236524.548) (ItfSurfaceLinetable2Polygon.java:191)

		CurveSegmentIntersector li=new CurveSegmentIntersector();
		StraightSegment s0=new StraightSegment(new Coordinate(623963.955, 236520.881), 
				new Coordinate(623968.142, 236523.414));
		ArcSegment s1=new ArcSegment(new Coordinate(623968.142, 236523.414), 
				new Coordinate(623965.805, 236523.085),
				new Coordinate(623963.954, 236524.548));
		li.computeIntersection(s0, s1);
		System.out.println(li.getIntersectionNum());
		for(int i=0;i<li.getIntersectionNum();i++){
			System.out.println(li.getOverlap()+", "+li.getIntersection(i));
		}
		
	}
	@Test
	public void xx2()
	{

		/*
		Info: buildSurfaces(): overlap 0.0, seg1 CIRCULARSTRING (624293.067 237338.324, 624295.785 237339.83, 624296.54 237342.845), seg2 CIRCULARSTRING (624291.0 237345.721, 624289.23 237344.003, 624288.844 237341.567) (ItfAreaLinetable2Polygon.java:154)
		intersection tid1 2177, tid2 2177, coord (624292.6917079942, 237342.20607120814, NaN)
		Info: buildSurfaces(): overlap 0.0, seg1 CIRCULARSTRING (624296.54 237342.845, 624294.489 237345.668, 624291.0 237345.721), seg2 CIRCULARSTRING (624288.844 237341.567, 624290.316 237339.113, 624293.067 237338.324) (ItfAreaLinetable2Polygon.java:154)
		intersection tid1 2195, tid2 2195, coord (624086.8242185982, 236441.7953386792, NaN)
		Info: buildSurfaces(): overlap 0.0, seg1 CIRCULARSTRING (624089.542 236440.792, 624088.3 236442.368, 624086.307 236442.139), seg2 CIRCULARSTRING (624086.583 236437.853, 624087.576 236438.227, 624088.535 236438.681) (ItfAreaLinetable2Polygon.java:154)

		 */
		
		
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		ArcSegment s0=new ArcSegment(new Coordinate(624293.067, 237338.324), 
				new Coordinate(624295.785, 237339.83),
				new Coordinate(624296.54, 237342.845));
		ArcSegment s1=new ArcSegment(new Coordinate(624291.0, 237345.721), 
				new Coordinate(624289.23, 237344.003),
				new Coordinate(624288.844, 237341.567));
		li.computeIntersection(s0, s1);
		System.out.println(li.getIntersectionNum());
		for(int i=0;i<li.getIntersectionNum();i++){
			System.out.println(li.getOverlap()+", "+li.getIntersection(i));
		}
		
	}
	public void calcULP() {
		double a=6000000.0;
		double diff=1.0;
		while(a+diff!=a){
			double diff2 = diff /2.0;
			if(diff2==0.0){
				break;
			}
			diff=diff2;
		}
		System.out.println(Math.ulp(a));
		System.out.println(diff);
		System.out.println(Long.toHexString(Double.doubleToRawLongBits(diff)));
		System.out.println(Long.toHexString(Double.doubleToRawLongBits(a)));
	}

	
}
