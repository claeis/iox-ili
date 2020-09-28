package ch.interlis.iom_j.itf.impl.jtsext.geom;

import static org.junit.Assert.assertEquals;

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

}
