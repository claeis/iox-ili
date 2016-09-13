package ch.interlis.iom_j.itf.impl.jtsext.algorithm;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;
import ch.interlis.iom_j.itf.impl.jtsext.noding.Intersection;

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
	public void testStartPtEquals() {
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		StraightSegment s0=new StraightSegment(new Coordinate(110.0, 110.0), 
				new Coordinate(120.0,110.0));
		StraightSegment s1=new StraightSegment(new Coordinate(110.0, 110.0), 
				new Coordinate(110.0,140.0));
		li.computeIntersection(s0, s1);
		assertTrue(li.hasIntersection());
		assertTrue(li.getIntersectionNum()==1);
		Coordinate is=li.getIntersection(0);
		assertTrue(is.x==110.0);
		assertTrue(is.y==110.0);
	}

	
}
