package ch.interlis.iom_j.itf.impl.jtsext.algorithm;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import com.vividsolutions.jts.geom.Coordinate;
import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;
import ch.interlis.iox.IoxException;

public class CurveSegmentIntersectorTest {

	private static final double EPS = 0.00000001;

    @Test
	public void testFastGerade() {
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		StraightSegment s0=new StraightSegment(
				new Coordinate(611613.84, 233467.819), 
				new Coordinate(611610.392, 233468.995));
		ArcSegment s1=new ArcSegment(
				new Coordinate(611770.424, 234251.322), 
				new Coordinate(611770.171, 234250.059), 
				new Coordinate(611769.918, 234248.796));
		li.computeIntersection(s0, s1);
		assertFalse(li.isOverlay());
		assertFalse(li.hasIntersection());
	}
    
	@Test
	public void testStartPtEquals() {
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		StraightSegment s0=new StraightSegment(
				new Coordinate(110.0, 110.0), 
				new Coordinate(120.0,110.0));
		StraightSegment s1=new StraightSegment(
				new Coordinate(110.0, 110.0), 
				new Coordinate(110.0,140.0));
		li.computeIntersection(s0, s1);
		assertTrue(li.hasIntersection());
		assertTrue(li.getIntersectionNum()==1);
		assertFalse(li.isOverlay());
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
		s0=new ArcSegment(
				new Coordinate(645175.553, 248745.374),
				new Coordinate( 645092.332, 248711.677),
				new Coordinate( 645009.11, 248677.98)); 
		s1=new ArcSegment(
				new Coordinate(645009.11, 248677.98),
				new Coordinate(644926.69, 248644.616),
				new Coordinate( 644844.269, 248611.253));
		li.computeIntersection(s0, s1);
		assertTrue(li.hasIntersection());
		assertTrue(li.getIntersectionNum()==1);
		assertFalse(li.isOverlay());
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
        assertFalse(li.isOverlay());
        Coordinate is1b=li.getIntersection(0);
        assertEquals(is1b.x,2625285.5890,EPS);
        assertEquals(is1b.y,1238688.2690,EPS);
        Coordinate is2b=li.getIntersection(1);
        assertEquals(is2b.x,targetX,EPS);
        assertEquals(is2b.y,targetY,EPS);
	}
	
	// two straight lines with same line length.
	// startPoints and endPoints are same.
	@Test
	public void overlayTwoStraightLines_SameEndPoints_SameDirection() {
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		StraightSegment s0=new StraightSegment(new Coordinate(100.0, 100.0),new Coordinate(100.0,200.0));
		StraightSegment s1=new StraightSegment(new Coordinate(100.0, 100.0),new Coordinate(100.0,200.0));
		li.computeIntersection(s0, s1);
		assertTrue(li.isOverlay());
		assertTrue(li.hasIntersection());
		assertTrue(li.getIntersectionNum()==2);
		// intSeg1
		Coordinate is=li.getIntersection(0);
		assertEquals(100.0,is.x,EPS);
		assertEquals(100.0,is.y,EPS);
		// intSeg2
		Coordinate is2=li.getIntersection(1);
		assertEquals(100.0,is2.x,EPS);
		assertEquals(200.0,is2.y,EPS);
	}
	
	// two straight lines with same line length.
	// startPoint1 is equal to endPoint2, startPoint2 is equal to endPoint1.
	@Test
	public void overlayTwoStraightLines_SameLineLength_OtherDirection() {
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		StraightSegment s0=new StraightSegment(new Coordinate(100.0, 100.0),new Coordinate(100.0,200.0));
		StraightSegment s1=new StraightSegment(new Coordinate(100.0, 200.0),new Coordinate(100.0,100.0));
		li.computeIntersection(s0, s1);
		assertTrue(li.hasIntersection());
		assertTrue(li.getIntersectionNum()==2);
		assertTrue(li.isOverlay());
		// intSeg1
		Coordinate is=li.getIntersection(0);
		assertEquals(100.0,is.x,EPS);
		assertEquals(200.0,is.y,EPS);
		// intSeg2
		Coordinate is2=li.getIntersection(1);
		assertEquals(100.0,is2.x,EPS);
		assertEquals(100.0,is2.y,EPS);
	}
	
	// two straight lines. line1 is longer than line2.
	// startPoints are different. endPoints are same.
	@Test
	public void overlayTwoStraightLines_DifferentStartPoints_SameDirection_DifferentLength() {
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		StraightSegment s0=new StraightSegment(new Coordinate(100.0, 100.0),new Coordinate(100.0,200.0));
		StraightSegment s1=new StraightSegment(new Coordinate(100.0, 150.0),new Coordinate(100.0,200.0));
		li.computeIntersection(s0, s1);
		assertTrue(li.hasIntersection());
		assertTrue(li.getIntersectionNum()==2);
		assertTrue(li.isOverlay());
		// intSeg1
		Coordinate is=li.getIntersection(0);
		assertEquals(100.0,is.x,EPS);
		assertEquals(150.0,is.y,EPS);
		// intSeg2
		Coordinate is2=li.getIntersection(1);
		assertEquals(100.0,is2.x,EPS);
		assertEquals(200.0,is2.y,EPS);
	}
	
	// two straight lines. line1 is longer than line2.
	// startPoints and endPoints are different. both lines in same direction
	@Test
	public void overlayTwoStraightLines_SameEndPoints_OtherDirection_DifferentLength() {
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		StraightSegment s0=new StraightSegment(new Coordinate(100.0, 100.0),new Coordinate(100.0,200.0));
		StraightSegment s1=new StraightSegment(new Coordinate(100.0, 150.0),new Coordinate(100.0,100.0));
		li.computeIntersection(s0, s1);
		assertTrue(li.hasIntersection());
		assertTrue(li.getIntersectionNum()==2);
		assertTrue(li.isOverlay());
		// intSeg1
		Coordinate is=li.getIntersection(0);
		assertEquals(100.0,is.x,EPS);
		assertEquals(150.0,is.y,EPS);
		// intSeg2
		Coordinate is2=li.getIntersection(1);
		assertEquals(100.0,is2.x,EPS);
		assertEquals(100.0,is2.y,EPS);
	}
	
	// two straight lines. line1 is longer than line2.
	// startPoints and endPoints are different. each line in different direction
	@Test
	public void overlayTwoStraightLines_DifferentEndPoints_SameDirection_DifferentLength() {
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		StraightSegment s0=new StraightSegment(new Coordinate(100.0, 100.0),new Coordinate(100.0,200.0));
		StraightSegment s1=new StraightSegment(new Coordinate(100.0, 120.0),new Coordinate(100.0,180.0));
		li.computeIntersection(s0, s1);
		assertTrue(li.isOverlay());
		assertTrue(li.hasIntersection());
		assertTrue(li.getIntersectionNum()==2);
		// intSeg1
		Coordinate is=li.getIntersection(0);
		assertEquals(100.0,is.x,EPS);
		assertEquals(120.0,is.y,EPS);
		// intSeg2
		Coordinate is2=li.getIntersection(1);
		assertEquals(100.0,is2.x,EPS);
		assertEquals(180.0,is2.y,EPS);
	}
	
	// two straight lines. line1 is longer than line2.
	// startPoints and endPoints are different. lines in different directory
	@Test
	public void overlayTwoStraightLines_DifferentEndPoints_OtherDirection_DifferentLength() {
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		StraightSegment s0=new StraightSegment(new Coordinate(100.0, 100.0),new Coordinate(100.0,200.0));
		StraightSegment s1=new StraightSegment(new Coordinate(100.0, 180.0),new Coordinate(100.0,120.0));
		li.computeIntersection(s0, s1);
		assertTrue(li.isOverlay());
		assertTrue(li.hasIntersection());
		assertTrue(li.getIntersectionNum()==2);
		// intSeg1
		Coordinate is=li.getIntersection(0);
		assertEquals(100.0,is.x,EPS);
		assertEquals(180.0,is.y,EPS);
		// intSeg2
		Coordinate is2=li.getIntersection(1);
		assertEquals(100.0,is2.x,EPS);
		assertEquals(120.0,is2.y,EPS);
	}
	
	// two arcs with same arcPoint and radius.
	// startPoints and endPoints are same. lines are in same direction
	@Test
	public void overlayTwoARCS_SameEndPoints_SameDirection() {
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		ArcSegment s0=new ArcSegment(new Coordinate(100.0, 100.0),new Coordinate(120,150.0),new Coordinate(100.0,200.0));
		ArcSegment s1=new ArcSegment(new Coordinate(100.0, 100.0),new Coordinate(120,150.0),new Coordinate(100.0,200.0));
		li.computeIntersection(s0, s1);
		assertTrue(li.hasIntersection());
		assertTrue(li.getIntersectionNum()==2);
		assertTrue(li.isOverlay());
		// intSeg1
		Coordinate is=li.getIntersection(0);
        assertEquals(100.0,is.x,EPS);
        assertEquals(200.0,is.y,EPS);
		// intSeg2
		Coordinate is2=li.getIntersection(1);
        assertEquals(100.0,is2.x,EPS);
        assertEquals(100.0,is2.y,EPS);
	}
	
	// two arcs with different arcPoint (on same arcLine) and same radius length.
	// startPoints and endPoints are same. lines are in same direction.
	@Test
	public void overlayTwoARCS_DifferentArcPointOnSameArcLine_SameDirection() {
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		ArcSegment s0=new ArcSegment(new Coordinate(0.0, 10.0),new Coordinate(4.0,8.0),new Coordinate(0.0,0.0));
		ArcSegment s1=new ArcSegment(new Coordinate(0.0, 10.0),new Coordinate(4.0,2.0),new Coordinate(0.0,0.0));
		li.computeIntersection(s0, s1);
        assertTrue(li.isOverlay());
		assertTrue(li.hasIntersection());
		assertTrue(li.getIntersectionNum()==2);
        // intSeg1
        Coordinate is=li.getIntersection(1);
        assertEquals(0.0,is.x,EPS);
        assertEquals(0.0,is.y,EPS);
        // intSeg2
        Coordinate is2=li.getIntersection(0);
        assertEquals(0.0,is2.x,EPS);
        assertEquals(10.0,is2.y,EPS);
	}
	
	// two arcs with same arcPoint (on same arcLine) and same radius length.
	// one arc line is longer than the other arc line.
	// startPoints is same, endPoints are different. lines are in same direction.
	@Test
	public void overlayTwoARCS_SameArcPointOnSameArcLine_OneArcLineIsLonger() {
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		ArcSegment s0=new ArcSegment(new Coordinate(0.0, 10.0),new Coordinate(4.0,8.0),new Coordinate(0.0,0.0));
		ArcSegment s1=new ArcSegment(new Coordinate(0.0, 10.0),new Coordinate(4.0,8.0),new Coordinate(4.0,2.0));
		li.computeIntersection(s0, s1);
		assertTrue(li.hasIntersection());
		assertTrue(li.getIntersectionNum()==2);
		assertTrue(li.isOverlay());
        Coordinate is=li.getIntersection(0);
        assertEquals(0.0,is.x,EPS);
        assertEquals(10.0,is.y,EPS);
        Coordinate is2=li.getIntersection(1);
        assertEquals(4.0,is2.x,EPS);
        assertEquals(2.0,is2.y,EPS);
	}
	
	// two arcs with different arcPoint (on same arcLine) and same radius length.
	// one arc line is longer than the other arc line.
	// startPoints are equals, endPoints are different. arcPoints are different. lines are in same direction.
	@Test
	public void overlayTwoARCS_DifferentArcPointOnSameArcLine_OneArcLineIsLonger() {
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		ArcSegment s0=new ArcSegment(new Coordinate(0.0, 10.0),new Coordinate(4.0,8.0),new Coordinate(0.0,0.0));
		ArcSegment s1=new ArcSegment(new Coordinate(0.0, 10.0),new Coordinate(5.0,5.0),new Coordinate(4.0,2.0));
		li.computeIntersection(s0, s1);
		assertTrue(li.hasIntersection());
		assertTrue(li.getIntersectionNum()==2);
		assertTrue(li.isOverlay());
        Coordinate is=li.getIntersection(0);
        assertEquals(0.0,is.x,EPS);
        assertEquals(10.0,is.y,EPS);
        Coordinate is2=li.getIntersection(1);
        assertEquals(4.0,is2.x,EPS);
        assertEquals(2.0,is2.y,EPS);
	}
	
	// two arcs with same arcPoint and radius
	// startPoint1 is equal to endPoint2, startPoint2 is equal to endPoint1.
	@Test
	public void overlayTwoARCS_SameEndPoints_OtherDirection() {
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		ArcSegment s0=new ArcSegment(new Coordinate(100.0, 100.0),new Coordinate(80.0,150.0),new Coordinate(100.0,200.0));
		ArcSegment s1=new ArcSegment(new Coordinate(100.0, 200.0),new Coordinate(80.0,150.0),new Coordinate(100.0,100.0));
		li.computeIntersection(s0, s1);
		assertTrue(li.isOverlay());
		assertTrue(li.hasIntersection());
		assertTrue(li.getIntersectionNum()==2);
		// intSeg1
		Coordinate is=li.getIntersection(0);
        assertEquals(100.0,is.x,EPS);
        assertEquals(200.0,is.y,EPS);
		// intSeg2
		Coordinate is2=li.getIntersection(1);
        assertEquals(100.0,is2.x,EPS);
        assertEquals(100.0,is2.y,EPS);
	}
	
	// two arcs. ArcPoint is equal. different angle.
	// startPoints are different. endPoints are same.
	@Test
	public void overlayTwoARCS_DifferentStartPoints_SameDirection_DifferentLength() {
        CurveSegmentIntersector li=new CurveSegmentIntersector();
        ArcSegment s0=new ArcSegment(new Coordinate(70.0, 60.0),new Coordinate(50.0,100.0),new Coordinate(60.0,130.0));
        ArcSegment s1=new ArcSegment(new Coordinate(60.0, 70.0),new Coordinate(50.0,100.0),new Coordinate(60.0,130.0));
        li.computeIntersection(s0, s1);
        assertTrue(li.isOverlay());
        assertTrue(li.hasIntersection());
        assertTrue(li.getIntersectionNum()==2);
        // intSeg1
        Coordinate is=li.getIntersection(1);
        assertEquals(60.0,is.x,EPS);
        assertEquals(130.0,is.y,EPS);
        // intSeg2
        Coordinate is2=li.getIntersection(0);
        assertEquals(60.0,is2.x,EPS);
        assertEquals(70.0,is2.y,EPS);
	}
	
	// two arcs. ArcPoint is equal. different angle.
	// startPoints are same. endPoints are same.
	@Test
	public void overlayTwoARCS_DifferentStartEndPoints_OtherDirection_DifferentLength() {
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		ArcSegment s0=new ArcSegment(new Coordinate(70.0, 60.0),new Coordinate(50.0,100.0),new Coordinate(70.0,140.0));
		ArcSegment s1=new ArcSegment(new Coordinate(60.0,130.0),new Coordinate(50.0,100.0),new Coordinate(60.0, 70.0));
		li.computeIntersection(s0, s1);
		assertTrue(li.isOverlay());
		assertTrue(li.hasIntersection());
		assertTrue(li.getIntersectionNum()==2);
		// intSeg1
		Coordinate is=li.getIntersection(0);
		assertEquals(60.0,is.x,EPS);
		assertEquals(130.0,is.y,EPS);
		// intSeg2
		Coordinate is2=li.getIntersection(1);
		assertEquals(60.0,is2.x,EPS);
		assertEquals(70.0,is2.y,EPS);
	}
	
    // two arcs. ArcPoint is equal. different angle.
    // startPoints are same. endPoints are same.
    @Test
    public void overlayTwoARCS_DifferentStartEndPoints_OtherDirection_DifferentLength_Test2() {
        CurveSegmentIntersector li=new CurveSegmentIntersector();
        // CIRCULARSTRING (2759364.356398068 1221799.2512748044, 2759364.350699179 1221799.2376373415, 2759364.345 1221799.224)
        ArcSegment s0=new ArcSegment(new Coordinate(2759364.948, 1221801.003),new Coordinate(2759364.647,1221800.113),new Coordinate(2759364.345, 1221799.224));
        ArcSegment s1=new ArcSegment(new Coordinate(2759364.607, 1221799.353),new Coordinate(2759364.449,1221799.343),new Coordinate(2759364.345, 1221799.224));
        li.computeIntersection(s0, s1);
        /*
        assertTrue(li.isOverlay());
        assertTrue(li.hasIntersection());
        assertTrue(li.getIntersectionNum()==2);
        // intSeg1
        Coordinate is=li.getIntersection(0);
        assertEquals(60,is.x,EPS);
        assertEquals(130,is.y,EPS);
        // intSeg2
        Coordinate is2=li.getIntersection(1);
        assertEquals(70,is2.x,EPS);
        assertEquals(60,is2.y,EPS);
        */
    }
	
	// two arcs. ArcPoint is equal. different angle.
	// startPoints and endPoints are different.
	@Test
	public void overlayTwoARCS_DifferentEndPoints_SameDirection_DifferentLength() {
        CurveSegmentIntersector li=new CurveSegmentIntersector();
        ArcSegment s0=new ArcSegment(new Coordinate(70.0, 60.0),new Coordinate(50.0,100.0),new Coordinate(70.0,140.0));
        ArcSegment s1=new ArcSegment(new Coordinate(70.0, 60.0),new Coordinate(50.0,100.0),new Coordinate(60.0,130.0));
        li.computeIntersection(s0, s1);
        assertTrue(li.isOverlay());
        assertTrue(li.hasIntersection());
        assertTrue(li.getIntersectionNum()==2);
        // intSeg1
        Coordinate is=li.getIntersection(1);
        assertEquals(70.0,is.x,EPS);
        assertEquals(60.0,is.y,EPS);
        // intSeg2
        Coordinate is2=li.getIntersection(0);
        assertEquals(60.0,is2.x,EPS);
        assertEquals(130.0,is2.y,EPS);
	}
	
	// two arcs. ArcPoint is equal. different angle.
	// startPoints and endPoints are different.
	@Test
	public void overlayTwoARCS_DifferentEndPoints_OtherDirection_DifferentLength() {
		CurveSegmentIntersector li=new CurveSegmentIntersector();
        ArcSegment s0=new ArcSegment(new Coordinate(70.0, 60.0),new Coordinate(50.0,100.0),new Coordinate(70.0,140.0));
        ArcSegment s1=new ArcSegment(new Coordinate(60.0,130.0),new Coordinate(50.0,100.0),new Coordinate(70.0, 60.0));
		li.computeIntersection(s0, s1);
		assertTrue(li.isOverlay());
		assertTrue(li.hasIntersection());
		assertTrue(li.getIntersectionNum()==2);
		// intSeg1
		Coordinate is=li.getIntersection(1);
        assertEquals(70.0,is.x,EPS);
        assertEquals(60.0,is.y,EPS);
		// intSeg2
		Coordinate is2=li.getIntersection(0);
        assertEquals(60.0,is2.x,EPS);
        assertEquals(130.0,is2.y,EPS);
	}
	
	// two arcs with same center and radius that don't touch each other.
	@Test
	public void twoARCS_SameRadiusAndCenter_DontOverlay() {
		CurveSegmentIntersector li=new CurveSegmentIntersector();
        ArcSegment s0=new ArcSegment(new Coordinate(70.0, 60.0),new Coordinate(50.0,100.0),new Coordinate(70.0,140.0));
        ArcSegment s1=new ArcSegment(new Coordinate(140.0, 70.0),new Coordinate(150.0,100.0),new Coordinate(140.0,130.0));
		li.computeIntersection(s0, s1);
		assertFalse(li.isOverlay());
		assertFalse(li.hasIntersection());
	}
    // two arcs with same center 100/100 and radius 50 that touch each other at the endpoints.
    @Test
    public void twoARCS_SameRadiusAndCenter_Touch_DontOverlay() {
        CurveSegmentIntersector li=new CurveSegmentIntersector();
        ArcSegment s0=new ArcSegment(new Coordinate(50.0, 100.0),new Coordinate(100.0,150.0),new Coordinate(150.0,100.0));
        //System.out.println(s0.getCenterPoint()+" r "+s0.getRadius()+" "+s0.getSign());
        ArcSegment s1=new ArcSegment(new Coordinate(150.0, 100.0),new Coordinate(100.0,50.0),new Coordinate(50.0,100.0));
        //System.out.println(s1.getCenterPoint()+" r "+s1.getRadius()+" "+s1.getSign());
        li.computeIntersection(s0, s1);
        assertFalse(li.isOverlay());
        assertFalse(li.hasIntersection());
    }
    @Test
    public void twoARCS_SameRadiusAndCenter_Touch_DontOverlay_real() {
        CurveSegmentIntersector li=new CurveSegmentIntersector();
        ArcSegment s0=new ArcSegment(new Coordinate(2654828.912, 1223354.671),new Coordinate(2654829.982, 1223353.601),new Coordinate(2654831.052, 1223354.671));
        //System.out.println(s0.getCenterPoint()+" r "+s0.getRadius()+" "+s0.getSign());
        ArcSegment s1=new ArcSegment(new Coordinate(2654831.052, 1223354.671),new Coordinate(2654829.982, 1223355.741),new Coordinate(2654828.912, 1223354.671));
        //System.out.println(s1.getCenterPoint()+" r "+s1.getRadius()+" "+s1.getSign());
        li.computeIntersection(s0, s1);
        assertFalse(li.isOverlay());
        assertFalse(li.hasIntersection());
    }
}