package ch.interlis.iom_j.itf.impl.jtsext.geom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateArrays;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

public class ArcSegmentTest {
	static final double EPSILON=0.00000001;

	@Test
	public void linksDrehend() {

		Coordinate arcpt = new Coordinate(115.0,  108.0);
		ArcSegment seg=new ArcSegment(new Coordinate(110,110),arcpt,new Coordinate(120.0,  110.0));
		//System.out.println(seg.getCenterPoint());
		Coordinate coords[]=seg.getCoordinates();
		//System.out.println(new CoordinateArraySequence(coords));
		int arcPtIdx=CoordinateArrays.indexOf(arcpt, coords);

		assertEquals(110.17641444784455,coords[1].x,EPSILON); 
		assertEquals(109.83746617368195,coords[1].y,EPSILON);
		
		assertEquals(114.7601593127208, coords[arcPtIdx-1].x,EPSILON);
		assertEquals(108.00396822773143,coords[arcPtIdx-1].y,EPSILON);

		assertEquals(115.2398406872792,coords[arcPtIdx+1].x,EPSILON);
		assertEquals(108.00396822773143,coords[arcPtIdx+1].y,EPSILON); 
		
		assertEquals(119.82358555215545,coords[coords.length-2].x,EPSILON);
		assertEquals(109.83746617368195,coords[coords.length-2].y,EPSILON);
	}
	@Test
	public void rechtsDrehend() {
		Coordinate arcpt=new Coordinate(115.0,  112.0);
		ArcSegment seg=new ArcSegment(new Coordinate(110,110),arcpt,new Coordinate(120.0,  110.0));
		//System.out.println(seg.getCenterPoint());
		Coordinate coords[]=seg.getCoordinates();
		//System.out.println(new CoordinateArraySequence(coords));
		int arcPtIdx=CoordinateArrays.indexOf(arcpt, coords);
		
		assertEquals(110.17641444784455,coords[1].x,EPSILON); 
		assertEquals(110.16253382631805,coords[1].y,EPSILON);
		
		assertEquals(114.7601593127208, coords[arcPtIdx-1].x,EPSILON);
		assertEquals(111.99603177226857,coords[arcPtIdx-1].y,EPSILON);

		assertEquals(115.2398406872792,coords[arcPtIdx+1].x,EPSILON);
		assertEquals(111.99603177226857,coords[arcPtIdx+1].y,EPSILON); 
		
		assertEquals(119.82358555215545,coords[coords.length-2].x,EPSILON);
		assertEquals(110.16253382631805,coords[coords.length-2].y,EPSILON);
	}
	
    @Test
    public void straightArc() {
        Coordinate startpt=new Coordinate(2608687.275,1250626.582);
        Coordinate endpt=new Coordinate(2608687.409,1250626.468);
        //Coordinate arcpt=new Coordinate(2608687.3416875135,1250626.5246326916);
        Coordinate arcpt=new Coordinate(2608687.342,1250626.525);
        // ARC {A1 2608687.342, A2 1250626.525, C1 2608687.409, C2 1250626.468}
        ArcSegment seg=new ArcSegment(startpt,arcpt,endpt);
        Coordinate coords[]=seg.getCoordinates();
        //System.out.println(new CoordinateArraySequence(coords));
	    assertEquals(startpt,coords[0]);
        assertEquals(arcpt,coords[1]);
        assertEquals(endpt,coords[2]);
	}

	@Test
	public void calculateMidPointArcUpToSemicircle() {
		// circle with centre (0,0) and radius 5: the middle of the arc from (5,0) to (0,5) is at 45 degrees
		ArcSegment seg=new ArcSegment(new Coordinate(5,0),new Coordinate(4,3),new Coordinate(0,5));
		assertCoordinate(5/Math.sqrt(2),5/Math.sqrt(2),seg.calculateMidPoint());
	}

	@Test
	public void calculateMidPointArcLongerThanSemicircle() {
		// the long way from (5,0) to (0,5) on the circle with centre (0,0) and radius 5: the middle is at 225 degrees
		ArcSegment seg=new ArcSegment(new Coordinate(5,0),new Coordinate(-4,-3),new Coordinate(0,5));
		assertCoordinate(-5/Math.sqrt(2),-5/Math.sqrt(2),seg.calculateMidPoint());
		ArcSegment reversed=new ArcSegment(new Coordinate(0,5),new Coordinate(-4,-3),new Coordinate(5,0));
		assertCoordinate(-5/Math.sqrt(2),-5/Math.sqrt(2),reversed.calculateMidPoint());
	}

	@Test
	public void calculateMidPointSemicircle() {
		// circle with centre (3.1,4.2) and radius 5; the mid point (-0.9,7.2) is the middle of the arc
		ArcSegment seg=new ArcSegment(new Coordinate(0.1,0.2),new Coordinate(-0.9,7.2),new Coordinate(6.1,8.2));
		assertCoordinate(-0.9,7.2,seg.calculateMidPoint());
	}

	@Test
	public void calculateMidPointStraightArc() {
		// all three points on one line: the middle of the line, not the given mid point (0.5,0.5)
		ArcSegment seg=new ArcSegment(new Coordinate(0,0),new Coordinate(0.5,0.5),new Coordinate(2,2));
		assertCoordinate(1,1,seg.calculateMidPoint());
	}

	@Test
	public void equals2DArcsLongerThanSemicircle() {
		// both arcs take the long way from (5,0) to (0,5): a on the circle with centre (0,0), b on the circle with
		// centre (-0.25,-0.25); the middles of the arcs, (-3.54,-3.54) and (-3.97,-3.97), differ by 0.43 in x and y
		ArcSegment a=new ArcSegment(new Coordinate(5,0),new Coordinate(-4,-3),new Coordinate(0,5));
		ArcSegment b=new ArcSegment(new Coordinate(5,0),new Coordinate(-2.5,-5),new Coordinate(0,5));
		assertFalse(a.equals2D(b,0.4));
		assertTrue(a.equals2D(b,0.45));
	}

	private static void assertCoordinate(double x,double y,Coordinate actual) {
		assertEquals(x,actual.x,EPSILON);
		assertEquals(y,actual.y,EPSILON);
	}
}
