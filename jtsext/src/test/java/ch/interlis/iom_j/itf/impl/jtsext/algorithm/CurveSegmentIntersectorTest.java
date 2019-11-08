package ch.interlis.iom_j.itf.impl.jtsext.algorithm;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.vividsolutions.jts.algorithm.RobustLineIntersector;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;

import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;
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
		Coordinate is=li.getIntersection(1);
		assertEquals(100.0,is.x,EPS);
		assertEquals(200.0,is.y,EPS);
		// intSeg2
		Coordinate is2=li.getIntersection(0);
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
		Coordinate is=li.getIntersection(1);
		assertEquals(100.0,is.x,EPS);
		assertEquals(150.0,is.y,EPS);
		// intSeg2
		Coordinate is2=li.getIntersection(0);
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
		Coordinate is=li.getIntersection(1);
		assertEquals(100.0,is.x,EPS);
		assertEquals(180.0,is.y,EPS);
		// intSeg2
		Coordinate is2=li.getIntersection(0);
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
		Coordinate is=li.getIntersection(1);
        assertEquals(100.0,is.x,EPS);
        assertEquals(200.0,is.y,EPS);
		// intSeg2
		Coordinate is2=li.getIntersection(0);
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
        Coordinate is=li.getIntersection(0);
        assertEquals(0.0,is.x,EPS);
        assertEquals(0.0,is.y,EPS);
        // intSeg2
        Coordinate is2=li.getIntersection(1);
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
		Coordinate is=li.getIntersection(1);
        assertEquals(100.0,is.x,EPS);
        assertEquals(200.0,is.y,EPS);
		// intSeg2
		Coordinate is2=li.getIntersection(0);
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
		Coordinate is=li.getIntersection(1);
		assertEquals(60.0,is.x,EPS);
		assertEquals(130.0,is.y,EPS);
		// intSeg2
		Coordinate is2=li.getIntersection(0);
		assertEquals(60.0,is2.x,EPS);
		assertEquals(70.0,is2.y,EPS);
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
        Coordinate is2=li.getIntersection(0);
        assertEquals(70.0,is.x,EPS);
        assertEquals(60.0,is.y,EPS);
		// intSeg2
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
    

    private Coordinate calcCircle(double x1,double y1, double x2, double y2, double x3, double y3)
    {
        double dx=x1;
        double dy=y1;
        Coordinate c=calcCircle_(x1-dx, y1-dy, x2-dx, y2-dy, x3-dx, y3-dy);
        c.x+=dx;
        c.y+=dy;
        return c;
    }
    private Coordinate calcCircle_(double x1,double y1, double x2, double y2, double x3, double y3)
    {
        double A=x1*(y2-y3)-y1*(x2-x3)+x2*y3-x3*y2;
        double B=(x1*x1+y1*y1)*(y3-y2)+(x2*x2+y2*y2)*(y1-y3)+(x3*x3+y3*y3)*(y2-y1);
        double C=(x1*x1+y1*y1)*(x2-x3)+(x2*x2+y2*y2)*(x3-x1)+(x3*x3+y3*y3)*(x1-x2);
        double D=(x1*x1+y1*y1)*(x3*y2-x2*y3)+(x2*x2+y2*y2)*(x1*y3-x3*y1)+(x3*x3+y3*y3)*(x2*y1-x1*y2);
        double x=-B/2.0/A;
        double y=-C/2.0/A;
        //double r=Math.sqrt((B*B+C*C-4.0*A*D)/(4.0*A*A));
        double r=Math.hypot(x-x1, y-y1);
        return new Coordinate(x,y);
    }
    
    @Test
    @Ignore("ilivalidator#186")
    public void twoARCS_intersect0() {
        // compile group: 'org.apfloat', name: 'apfloat', version: '1.8.3'
        CurveSegmentIntersector li=new CurveSegmentIntersector();
        ArcSegment s0=new ArcSegment(new Coordinate(2658317.225,1250832.586),new Coordinate(2658262.543,1250774.465),new Coordinate(2658210.528,1250713.944));
        // center (2659581.37 , 1249587.16)
        // radius 1774.43
        System.out.println(s0.getCenterPoint()+" r "+s0.getRadius()+" "+s0.getSign()+" theta "+s0.getTheta());
        System.out.println("s0 dist "+CurveSegment.dist(s0.getStartPoint(),s0.getEndPoint()));
        //Coordinate c0=calcCircle(s0.getStartPoint().x,s0.getStartPoint().y,s0.getMidPoint().x,s0.getMidPoint().y,s0.getEndPoint().x,s0.getEndPoint().y);
        //System.out.println(c0);
        //System.out.println("start r "+CurveSegment.dist(c0, s0.getStartPoint()));
        //System.out.println("mid   r "+CurveSegment.dist(c0, s0.getMidPoint()));
        //System.out.println("end   r "+CurveSegment.dist(c0, s0.getEndPoint()));
        //calcCircle(s0.getStartPoint().x,s0.getStartPoint().y,s0.getMidPoint().x,s0.getMidPoint().y,s0.getEndPoint().x,s0.getEndPoint().y);
        ArcSegment s1=new ArcSegment(new Coordinate(2658211.456,1250715.072),new Coordinate(2658161.386,1250651.279),new Coordinate(2658114.283,1250585.266));
        System.out.println(s1.getCenterPoint()+" r "+s1.getRadius()+" "+s1.getSign()+" theta "+s1.getTheta());
        System.out.println("s1 dist "+CurveSegment.dist(s1.getStartPoint(),s1.getEndPoint()));
        Coordinate c1=new Coordinate(2659582.4124795417,1249587.8392729152);
        
        //calcCircle(s1.getStartPoint().x,s1.getStartPoint().y,s1.getMidPoint().x,s1.getMidPoint().y,s1.getEndPoint().x,s1.getEndPoint().y);
        li.computeIntersection(s0, s1);
        assertFalse(li.isOverlay());
        assertTrue(li.hasIntersection());
        assertTrue(li.getIntersectionNum()==1);
    }
    @Test
    @Ignore("ilivalidator#186")
    public void twoARCS_intersect() {
        final double RDIFF=0.197;
        final double R0=1800.0;
        final double R1=R0+RDIFF;
        final double DVW=0.196;
        final double C_X=0.0;
        final double C_Y=0.0;
        final double THETA=0.048;
        
        CurveSegmentIntersector li=new CurveSegmentIntersector();
        final Coordinate startPoint0 = new Coordinate(C_X,C_Y+R0);
        ArcSegment s0=new ArcSegment(startPoint0,
                new Coordinate(startPoint0.x-R0*Math.sin(THETA),startPoint0.y-R0+R0*Math.cos(THETA)),
                new Coordinate(startPoint0.x-R0*Math.sin(2*THETA),startPoint0.y-R0+R0*Math.cos(2*THETA)));
        // center (2659581.37 , 1249587.16)
        // radius 1774.43
        System.out.println("s0 "+s0.getStartPoint()+" "+s0.getMidPoint()+" "+s0.getEndPoint());
        System.out.println("s0 "+s0.getCenterPoint()+" r "+s0.getRadius()+" "+s0.getSign()+" theta "+s0.getTheta());
        System.out.println("s0 dist "+CurveSegment.dist(s0.getStartPoint(),s0.getEndPoint()));
        final Coordinate startPoint1 = new Coordinate(C_X-DVW,C_Y+R0);
        ArcSegment s1=new ArcSegment(startPoint1,
                new Coordinate(startPoint1.x+R1*Math.sin(THETA),startPoint1.y-R1+R1*Math.cos(THETA)),
                new Coordinate(startPoint1.x+R1*Math.sin(2*THETA),startPoint1.y-R1+R1*Math.cos(2*THETA)));
        System.out.println("s1 "+s1.getStartPoint()+" "+s1.getMidPoint()+" "+s1.getEndPoint());
        System.out.println("s1 "+s1.getCenterPoint()+" r "+s1.getRadius()+" "+s1.getSign()+" theta "+s1.getTheta());
        System.out.println("s1 dist "+CurveSegment.dist(s1.getStartPoint(),s1.getEndPoint()));
        li.computeIntersection(s0, s1);
        assertFalse(li.isOverlay());
        assertTrue(li.hasIntersection());
        assertEquals(1,li.getIntersectionNum());
    }
    @Test
    @Ignore("ili2db#308")
    public void twoARCS_issue308() {
        CurveSegmentIntersector li=new CurveSegmentIntersector();
        ArcSegment s0=new ArcSegment(new Coordinate(2653134.354, 1227788.188),
                new Coordinate(2653137.455, 1227797.289),
                new Coordinate(2653140.555, 1227806.391));
        
        System.out.println(s0.getCenterPoint()+" r "+s0.getRadius()+" "+s0.getSign()+" theta "+s0.getTheta());
        System.out.println("s0 dist "+CurveSegment.dist(s0.getStartPoint(),s0.getEndPoint()));
        //Coordinate c0=calcCircle(s0.getStartPoint().x,s0.getStartPoint().y,s0.getMidPoint().x,s0.getMidPoint().y,s0.getEndPoint().x,s0.getEndPoint().y);
        //System.out.println(c0);
        //System.out.println("start r "+CurveSegment.dist(c0, s0.getStartPoint()));
        //System.out.println("mid   r "+CurveSegment.dist(c0, s0.getMidPoint()));
        //System.out.println("end   r "+CurveSegment.dist(c0, s0.getEndPoint()));
        //calcCircle(s0.getStartPoint().x,s0.getStartPoint().y,s0.getMidPoint().x,s0.getMidPoint().y,s0.getEndPoint().x,s0.getEndPoint().y);
        ArcSegment s1=new ArcSegment(new Coordinate(2653135.557, 1227789.0),
                new Coordinate(2653134.819, 1227788.796),
                new Coordinate(2653134.354, 1227788.188));
        System.out.println(s1.getCenterPoint()+" r "+s1.getRadius()+" "+s1.getSign()+" theta "+s1.getTheta());
        System.out.println("s1 dist "+CurveSegment.dist(s1.getStartPoint(),s1.getEndPoint()));
        Coordinate c1=new Coordinate(2659582.4124795417,1249587.8392729152);
        
        JtsextGeometryFactory fact=new JtsextGeometryFactory();
        LineString l0=fact.createLineString(s0.getCoordinates());
        LineString l1=fact.createLineString(s1.getCoordinates());
        System.out.println("intersect "+ l0.intersection(l1));
        //calcCircle(s1.getStartPoint().x,s1.getStartPoint().y,s1.getMidPoint().x,s1.getMidPoint().y,s1.getEndPoint().x,s1.getEndPoint().y);
        li.computeIntersection(s1, s0);
        if(li.hasIntersection()) {
            if(li.getIntersectionNum()>=1) {
                System.out.println(li.getIntersection(0));
                if(li.getIntersectionNum()==2) {
                    System.out.println(li.getIntersection(1));
                }
            }
        }
        assertFalse(li.isOverlay());
        assertTrue(li.hasIntersection());
        assertTrue(li.getIntersectionNum()==1);
    }
    
}