package ch.interlis.iom_j.itf.impl.jtsext.geom;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

public class ArcSegmentDirectionPtTest {
	static final double EPSILON=0.00000001;

	@Test
	public void bogenStartLinksDrehendOhneOverlap() {
		ArcSegment seg=new ArcSegment(new Coordinate(5.0,0.0),new Coordinate(4.0,3.0),new Coordinate(3.0,4.0));
		//System.out.println(seg.getCenterPoint());
		Coordinate directionPt=seg.getDirectionPt(true,0.0);
		//System.out.println(directionPt);
		assertEquals(5.0,directionPt.x,EPSILON);
		assertEquals(1.0,directionPt.y,EPSILON);
	}
	@Test
	public void bogenStartRechtsDrehendOhneOverlap() {
		ArcSegment seg=new ArcSegment(new Coordinate(5.0,0.0),new Coordinate(4.0,-3.0),new Coordinate(3.0,-4.0));
		Coordinate directionPt=seg.getDirectionPt(true,0.0);
		assertEquals(5.0,directionPt.x,EPSILON);
		assertEquals(-1.0,directionPt.y,EPSILON);
	}
	@Test
	public void bogenEndeRechtsDrehendOhneOverlap() {
		ArcSegment seg=new ArcSegment(new Coordinate(3.0,4.0),new Coordinate(4.0,3.0),new Coordinate(5.0,0.0));
		//System.out.println(seg.getCenterPoint());
		Coordinate directionPt=seg.getDirectionPt(false,0.0);
		//System.out.println(directionPt);
		assertEquals(5.0,directionPt.x,EPSILON);
		assertEquals(1.0,directionPt.y,EPSILON);
	}
	@Test
	public void bogenEndeLinksDrehendOhneOverlap() {
		ArcSegment seg=new ArcSegment(new Coordinate(3.0,-4.0),new Coordinate(4.0,-3.0),new Coordinate(5.0,0.0));
		Coordinate directionPt=seg.getDirectionPt(false,0.0);
		assertEquals(5.0,directionPt.x,EPSILON);
		assertEquals(-1.0,directionPt.y,EPSILON);
	}
////
	private double px=4.9;
	private double py=0.9949874371066197;
	@Test
	public void bogenStartLinksDrehendMitDist() {
		ArcSegment seg=new ArcSegment(new Coordinate(5.0,0.0),new Coordinate(4.0,3.0),new Coordinate(3.0,4.0));
		//System.out.println(seg.getCenterPoint());
		Coordinate directionPt=seg.getDirectionPt(true,1.0);
		//System.out.println(directionPt);
		assertEquals(px,directionPt.x,EPSILON);
		assertEquals(py,directionPt.y,EPSILON);
	}
	@Test
	public void bogenStartRechtsDrehendMitDist() {
		ArcSegment seg=new ArcSegment(new Coordinate(5.0,0.0),new Coordinate(4.0,-3.0),new Coordinate(3.0,-4.0));
		Coordinate directionPt=seg.getDirectionPt(true,1.0);
		assertEquals(px,directionPt.x,EPSILON);
		assertEquals(-py,directionPt.y,EPSILON);
	}
	@Test
	public void bogenEndeRechtsDrehendMitDist() {
		ArcSegment seg=new ArcSegment(new Coordinate(3.0,4.0),new Coordinate(4.0,3.0),new Coordinate(5.0,0.0));
		//System.out.println(seg.getCenterPoint());
		Coordinate directionPt=seg.getDirectionPt(false,1.0);
		//System.out.println(directionPt);
		assertEquals(px,directionPt.x,EPSILON);
		assertEquals(py,directionPt.y,EPSILON);
	}
	@Test
	public void bogenEndeLinksDrehendMitDist() {
		ArcSegment seg=new ArcSegment(new Coordinate(3.0,-4.0),new Coordinate(4.0,-3.0),new Coordinate(5.0,0.0));
		Coordinate directionPt=seg.getDirectionPt(false,1.0);
		assertEquals(px,directionPt.x,EPSILON);
		assertEquals(-py,directionPt.y,EPSILON);
	}
    @Test
    public void bogenDistGroesserDurchmesser() {
        final double END_X=3.0;
        final double END_Y=4.0;
        ArcSegment seg=new ArcSegment(new Coordinate(5.0,0.0),new Coordinate(4.0,3.0),new Coordinate(END_X,END_Y));
        //System.out.println(seg.getRadius());
        Coordinate directionPt=seg.getDirectionPt(true,12.0);
        //System.out.println(directionPt);
        assertEquals(END_X,directionPt.x,EPSILON);
        assertEquals(END_Y,directionPt.y,EPSILON);
    }
    @Test
    public void bogenDistGroesserSehne() {
        final double END_X=3.0;
        final double END_Y=4.0;
        ArcSegment seg=new ArcSegment(new Coordinate(5.0,0.0),new Coordinate(4.0,3.0),new Coordinate(END_X,END_Y));
        //System.out.println(seg.getRadius());
        Coordinate directionPt=seg.getDirectionPt(true,5.0);
        System.out.println(directionPt);
        assertEquals(END_X,directionPt.x,EPSILON);
        assertEquals(END_Y,directionPt.y,EPSILON);
    }

}
