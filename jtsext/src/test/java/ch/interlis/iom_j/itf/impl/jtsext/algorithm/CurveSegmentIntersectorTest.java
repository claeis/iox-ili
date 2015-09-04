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
		//CIRCULARSTRING (606959.432 232126.214, 606959.442 232125.874, 606959.471 232125.535), seg2 (606959.471 232125.535, 606958.009 232138.34)
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		ArcSegment s0=new ArcSegment(new Coordinate(606959.432, 232126.214), 
				new Coordinate(606959.442, 232125.874),
				new Coordinate(606959.471, 232125.535));
		StraightSegment s1=new StraightSegment(new Coordinate(606959.471, 232125.535), 
				new Coordinate(606958.009, 232138.34));
		li.computeIntersection(s0, s1);
		System.out.println(li.getIntersectionNum());
		for(int i=0;i<li.getIntersectionNum();i++){
			System.out.println(li.getIntersection(i));
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
