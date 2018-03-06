package ch.interlis.iom_j.itf.impl.jtsext.algorithm;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.precision.EnhancedPrecisionOp;

import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;
import ch.interlis.iox.IoxException;

public class CurveSegmentIntersectorTest {

	private static final double EPS = 0.00000001;

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
	@Test
	public void testCircleCirleEndptTolerance() {
		// Testet den Grenzwert HrgUTility.CIRCIR_ENDPT_TOL
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		ArcSegment s0=null;
		ArcSegment s1=null; 
		s0=new ArcSegment(new Coordinate(645175.553, 248745.374),new Coordinate( 645092.332, 248711.677),new Coordinate( 645009.11, 248677.98)); 
		s1=new ArcSegment(new Coordinate(645009.11, 248677.98), new Coordinate(644926.69, 248644.616),new Coordinate( 644844.269, 248611.253));
		
		
		
		li.computeIntersection(s0, s1);
		assertTrue(li.hasIntersection());
		assertTrue(li.getIntersectionNum()==1);
		Coordinate is=li.getIntersection(0);
		assertTrue(is.x==645009.110);
		assertTrue(is.y==248677.980);
	}
	
	@Test
	public void differentResultInQgis() throws IoxException{
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		// straight
		StraightSegment s0=new StraightSegment(
			new Coordinate(2625269.2470, 1238678.7230),
			new Coordinate(2625285.5890, 1238688.2690)
		);
		// arc
		ArcSegment s1=new ArcSegment(
			new Coordinate(2625285.5890, 1238688.2690), 
			new Coordinate(2625280.7330, 1238687.6640), 
			new Coordinate(2625277.0910, 1238690.9320)
		);
		// intersection test
		li.computeIntersection(s0, s1);
        assertTrue(li.getIntersectionNum()==2);
		assertTrue(li.hasIntersection());
		// coord1
		Coordinate is=li.getIntersection(0);
		assertEquals(is.x,2625285.5890,EPS);
		assertEquals(is.y,1238688.2690,EPS);
		// coord2
		Coordinate is2=li.getIntersection(1);
        // Strange: QGIS shows: 2625285.3642271 1238688.13770137 as intersection pt!!! but see cross check below
       final  double targetX = 2625285.3554505454;
       final double targetY = 1238688.1325746486;
        assertEquals(is2.x,targetX,EPS);
        assertEquals(is2.y,targetY,EPS);
        // cross check
        ArcSegment s1b=new ArcSegment(
                new Coordinate(2625285.5890, 1238688.2690), 
                new Coordinate(targetX, targetY), 
                new Coordinate(2625277.0910, 1238690.9320)
            );
        li.computeIntersection(s0, s1b);
        Coordinate is1b=li.getIntersection(0);
        assertEquals(is1b.x,2625285.5890,EPS);
        assertEquals(is1b.y,1238688.2690,EPS);
        Coordinate is2b=li.getIntersection(1);
        assertEquals(is2b.x,targetX,EPS);
        assertEquals(is2b.y,targetY,EPS);
	}
}
