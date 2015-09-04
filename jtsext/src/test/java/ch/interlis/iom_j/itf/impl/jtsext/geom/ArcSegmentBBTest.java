package ch.interlis.iom_j.itf.impl.jtsext.geom;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

public class ArcSegmentBBTest {
	static final double EPSILON=0.00000001;

	@Test
	public void gleicherQuadrantLinksDrehend() {
		final double start_x=4.8989794855663561963945681494118;
		ArcSegment seg=new ArcSegment(new Coordinate(start_x,1.0),new Coordinate(4.0,3.0),new Coordinate(3.0,4.0));
		//System.out.println(seg.getCenterPoint());
		Envelope bb=seg.expandEnvelope(new Envelope());
		//System.out.println(bb);
		assertEquals(3.0,bb.getMinX(),EPSILON);
		assertEquals(1.0,bb.getMinY(),EPSILON);
		assertEquals(start_x,bb.getMaxX(),EPSILON);
		assertEquals(4.0,bb.getMaxY(),EPSILON);
	}
	@Test
	public void gleicherQuadrantRechtsDrehend() {
		final double start_x=4.8989794855663561963945681494118;
		ArcSegment seg=new ArcSegment(new Coordinate(3.0,4.0),new Coordinate(4.0,3.0),new Coordinate(start_x,1.0));
		//System.out.println(seg.getCenterPoint());
		Envelope bb=seg.expandEnvelope(new Envelope());
		//System.out.println(bb);
		assertEquals(3.0,bb.getMinX(),EPSILON);
		assertEquals(1.0,bb.getMinY(),EPSILON);
		assertEquals(start_x,bb.getMaxX(),EPSILON);
		assertEquals(4.0,bb.getMaxY(),EPSILON);
	}
	@Test
	public void zweiQuadrantObenRechtsDrehend() {
		ArcSegment seg=new ArcSegment(new Coordinate(-3.0,4.0),new Coordinate(3.0,4.0),new Coordinate(4.0,3.0));
		//System.out.println(seg.getCenterPoint());
		Envelope bb=seg.expandEnvelope(new Envelope());
		//System.out.println(bb);
		assertEquals(-3.0,bb.getMinX(),EPSILON);
		assertEquals(3.0,bb.getMinY(),EPSILON);
		assertEquals(4.0,bb.getMaxX(),EPSILON);
		assertEquals(5.0,bb.getMaxY(),EPSILON);
	}
	@Test
	public void zweiQuadrantUntenLinksDrehend() {
		ArcSegment seg=new ArcSegment(new Coordinate(-3.0,-4.0),new Coordinate(3.0,-4.0),new Coordinate(4.0,-3.0));
		//System.out.println(seg.getCenterPoint());
		Envelope bb=seg.expandEnvelope(new Envelope());
		//System.out.println(bb);
		assertEquals(-3.0,bb.getMinX(),EPSILON);
		assertEquals(-5.0,bb.getMinY(),EPSILON);
		assertEquals(4.0,bb.getMaxX(),EPSILON);
		assertEquals(-3.0,bb.getMaxY(),EPSILON);
	}
	@Test
	public void zweiQuadrantLinksLinksDrehend() {
		ArcSegment seg=new ArcSegment(new Coordinate(-3.0,4.0),new Coordinate(-4.0,-3.0),new Coordinate(-3.0,-4.0));
		//System.out.println(seg.getCenterPoint());
		Envelope bb=seg.expandEnvelope(new Envelope());
		//System.out.println(bb);
		assertEquals(-5.0,bb.getMinX(),EPSILON);
		assertEquals(-4.0,bb.getMinY(),EPSILON);
		assertEquals(-3.0,bb.getMaxX(),EPSILON);
		assertEquals(4.0,bb.getMaxY(),EPSILON);
	}
	@Test
	public void zweiQuadrantRechtsLinksDrehend() {
		ArcSegment seg=new ArcSegment(new Coordinate(3.0,-4.0),new Coordinate(4.0,-3.0),new Coordinate(3.0,4.0));
		//System.out.println(seg.getCenterPoint());
		Envelope bb=seg.expandEnvelope(new Envelope());
		//System.out.println(bb);
		assertEquals(3.0,bb.getMinX(),EPSILON);
		assertEquals(-4.0,bb.getMinY(),EPSILON);
		assertEquals(5.0,bb.getMaxX(),EPSILON);
		assertEquals(4.0,bb.getMaxY(),EPSILON);
	}
	@Test
	public void dreiQuadrantRechtsObenLinksDrehend() {
		ArcSegment seg=new ArcSegment(new Coordinate(3.0,-4.0),new Coordinate(4.0,-3.0),new Coordinate(-3.0,4.0));
		//System.out.println(seg.getCenterPoint());
		Envelope bb=seg.expandEnvelope(new Envelope());
		//System.out.println(bb);
		assertEquals(-3.0,bb.getMinX(),EPSILON);
		assertEquals(-4.0,bb.getMinY(),EPSILON);
		assertEquals(5.0,bb.getMaxX(),EPSILON);
		assertEquals(5.0,bb.getMaxY(),EPSILON);
	}
	@Test
	public void vierQuadrantRechtsDrehend() {
		ArcSegment seg=new ArcSegment(new Coordinate(3.0,-4.0),new Coordinate(-3.0,4.0),new Coordinate(4.0,-3.0));
		//System.out.println(seg.getCenterPoint());
		Envelope bb=seg.expandEnvelope(new Envelope());
		//System.out.println(bb);
		assertEquals(-5.0,bb.getMinX(),EPSILON);
		assertEquals(-5.0,bb.getMinY(),EPSILON);
		assertEquals(5.0,bb.getMaxX(),EPSILON);
		assertEquals(5.0,bb.getMaxY(),EPSILON);
	}

}
