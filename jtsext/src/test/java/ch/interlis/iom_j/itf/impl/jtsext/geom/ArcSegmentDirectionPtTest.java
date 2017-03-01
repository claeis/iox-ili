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
		Coordinate directionPt=seg.getDirectionPt(true);
		//System.out.println(directionPt);
		assertEquals(5.0,directionPt.x,EPSILON);
		assertEquals(1.0,directionPt.y,EPSILON);
	}
	@Test
	public void bogenStartRechtsDrehendOhneOverlap() {
		ArcSegment seg=new ArcSegment(new Coordinate(5.0,0.0),new Coordinate(4.0,-3.0),new Coordinate(3.0,-4.0));
		Coordinate directionPt=seg.getDirectionPt(true);
		assertEquals(5.0,directionPt.x,EPSILON);
		assertEquals(-1.0,directionPt.y,EPSILON);
	}
	@Test
	public void bogenEndeRechtsDrehendOhneOverlap() {
		ArcSegment seg=new ArcSegment(new Coordinate(3.0,4.0),new Coordinate(4.0,3.0),new Coordinate(5.0,0.0));
		//System.out.println(seg.getCenterPoint());
		Coordinate directionPt=seg.getDirectionPt(false);
		//System.out.println(directionPt);
		assertEquals(5.0,directionPt.x,EPSILON);
		assertEquals(1.0,directionPt.y,EPSILON);
	}
	@Test
	public void bogenEndeLinksDrehendOhneOverlap() {
		ArcSegment seg=new ArcSegment(new Coordinate(3.0,-4.0),new Coordinate(4.0,-3.0),new Coordinate(5.0,0.0));
		Coordinate directionPt=seg.getDirectionPt(false);
		assertEquals(5.0,directionPt.x,EPSILON);
		assertEquals(-1.0,directionPt.y,EPSILON);
	}

}
