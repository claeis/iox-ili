package ch.interlis.iom_j.itf.impl.jtsext.geom;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import ch.interlis.iox.IoxException;

import com.vividsolutions.jts.geom.Coordinate;

public class StraightArcRemoveOverlapTest {
	static final double EPSILON=0.00000001;

	@Test
	public void startSegmentNoMidPtMove() throws IoxException {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segments=new ArrayList<CurveSegment>();
		Coordinate startPt=new Coordinate(0.0,0.0);
		Coordinate midPt=new Coordinate(1.0,7.0);
		Coordinate endArcPt=new Coordinate(4.0,8.0);
		Coordinate endPt=new Coordinate(0.0,10.0);
		ArcSegment s0=new ArcSegment(startPt,midPt,endArcPt);
		System.out.println("r "+s0.getRadius()+"; center "+s0.getCenterPoint()+"; sign "+s0.sign);
		segments.add(s0);
		segments.add(new StraightSegment(endArcPt,endPt));
		CompoundCurve line0=new CompoundCurve(segments,fact);
		StraightSegment s1=new StraightSegment(startPt,endPt);
		System.out.println(line0);
		boolean modified=line0.removeOverlap(s0, s1, 0.2);
		assertTrue(modified);
		System.out.println(line0);
		StraightSegment s00=(StraightSegment) line0.getSegments().get(0);
		System.out.println("start "+s00.getStartPoint()+"; end "+s00.getEndPoint());
		assertEquals(0.0,s00.getStartPoint().x,EPSILON);
		assertEquals(0.0,s00.getStartPoint().y,EPSILON);
		assertEquals(0.2,s00.getEndPoint().x,EPSILON);
		assertEquals(6.249615361854384,s00.getEndPoint().y,EPSILON);
		ArcSegment s01=(ArcSegment) line0.getSegments().get(1);
		System.out.println("r "+s01.getRadius()+"; center "+s01.getCenterPoint()+"; sign "+s01.sign);
		assertEquals(5.0, s01.getRadius(),EPSILON);
		assertEquals(1.0, s01.sign,EPSILON);
		assertEquals(4.0,s01.getCenterPoint().x,EPSILON);
		assertEquals(3.0,s01.getCenterPoint().y,EPSILON);
	}
	@Test
	public void startSegmentWithMidPtMove() throws IoxException {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segments=new ArrayList<CurveSegment>();
		Coordinate startPt=new Coordinate(0.0,0.0);
		Coordinate midPt=new Coordinate(-1.0,3.0);
		Coordinate endArcPt=new Coordinate(4.0,8.0);
		Coordinate endPt=new Coordinate(0.0,10.0);
		ArcSegment s0=new ArcSegment(startPt,midPt,endArcPt);
		System.out.println("r "+s0.getRadius()+"; center "+s0.getCenterPoint()+"; sign "+s0.sign);
		segments.add(s0);
		segments.add(new StraightSegment(endArcPt,endPt));
		CompoundCurve line0=new CompoundCurve(segments,fact);
		StraightSegment s1=new StraightSegment(startPt,endPt);
		System.out.println(line0);
		boolean modified=line0.removeOverlap(s0, s1, 0.2);
		assertTrue(modified);
		System.out.println(line0);
		StraightSegment s00=(StraightSegment) line0.getSegments().get(0);
		System.out.println("start "+s00.getStartPoint()+"; end "+s00.getEndPoint());
		assertEquals(0.0,s00.getStartPoint().x,EPSILON);
		assertEquals(0.0,s00.getStartPoint().y,EPSILON);
		assertEquals(0.2,s00.getEndPoint().x,EPSILON);
		assertEquals(6.249615361854384,s00.getEndPoint().y,EPSILON);
		ArcSegment s01=(ArcSegment) line0.getSegments().get(1);
		System.out.println("r "+s01.getRadius()+"; center "+s01.getCenterPoint()+"; sign "+s01.sign);
		assertEquals(5.0, s01.getRadius(),EPSILON);
		assertEquals(1.0, s01.sign,EPSILON);
		assertEquals(4.0,s01.getCenterPoint().x,EPSILON);
		assertEquals(3.0,s01.getCenterPoint().y,EPSILON);
	}
	@Test
	public void endSegmentNoMidPtMove() throws IoxException {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segments=new ArrayList<CurveSegment>();
		Coordinate startPt=new Coordinate(0.0,10.0);
		Coordinate startArcPt=new Coordinate(4.0,8.0);
		Coordinate midPt=new Coordinate(1.0,7.0);
		Coordinate endPt=new Coordinate(0.0,0.0);
		ArcSegment s0=new ArcSegment(startArcPt,midPt,endPt);
		System.out.println("r "+s0.getRadius()+"; center "+s0.getCenterPoint()+"; sign "+s0.sign);
		segments.add(new StraightSegment(startPt,startArcPt));
		segments.add(s0);
		CompoundCurve line0=new CompoundCurve(segments,fact);
		StraightSegment s1=new StraightSegment(startPt,endPt);
		System.out.println(line0);
		boolean modified=line0.removeOverlap(s0, s1, 0.2);
		assertTrue(modified);
		System.out.println(line0);
		StraightSegment s00=(StraightSegment) line0.getSegments().get(2);
		System.out.println("start "+s00.getStartPoint()+"; end "+s00.getEndPoint());
		assertEquals(0.2,s00.getStartPoint().x,EPSILON);
		assertEquals(6.249615361854384,s00.getStartPoint().y,EPSILON);
		assertEquals(0.0,s00.getEndPoint().x,EPSILON);
		assertEquals(0.0,s00.getEndPoint().y,EPSILON);
		ArcSegment s01=(ArcSegment) line0.getSegments().get(1);
		System.out.println("r "+s01.getRadius()+"; center "+s01.getCenterPoint()+"; sign "+s01.sign);
		assertEquals(5.0, s01.getRadius(),EPSILON);
		assertEquals(-1.0, s01.sign,EPSILON);
		assertEquals(4.0,s01.getCenterPoint().x,EPSILON);
		assertEquals(3.0,s01.getCenterPoint().y,EPSILON);
	}
	@Test
	public void endSegmentWithMidPtMove() throws IoxException {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segments=new ArrayList<CurveSegment>();
		Coordinate startPt=new Coordinate(0.0,10.0);
		Coordinate startArcPt=new Coordinate(4.0,8.0);
		Coordinate midPt=new Coordinate(-1.0,3.0);
		Coordinate endPt=new Coordinate(0.0,0.0);
		ArcSegment s0=new ArcSegment(startArcPt,midPt,endPt);
		System.out.println("r "+s0.getRadius()+"; center "+s0.getCenterPoint()+"; sign "+s0.sign);
		segments.add(new StraightSegment(startPt,startArcPt));
		segments.add(s0);
		CompoundCurve line0=new CompoundCurve(segments,fact);
		StraightSegment s1=new StraightSegment(startPt,endPt);
		System.out.println(line0);
		boolean modified=line0.removeOverlap(s0, s1, 0.2);
		assertTrue(modified);
		System.out.println(line0);
		StraightSegment s00=(StraightSegment) line0.getSegments().get(2);
		System.out.println("start "+s00.getStartPoint()+"; end "+s00.getEndPoint());
		assertEquals(0.2,s00.getStartPoint().x,EPSILON);
		assertEquals(6.249615361854384,s00.getStartPoint().y,EPSILON);
		assertEquals(0.0,s00.getEndPoint().x,EPSILON);
		assertEquals(0.0,s00.getEndPoint().y,EPSILON);
		ArcSegment s01=(ArcSegment) line0.getSegments().get(1);
		System.out.println("r "+s01.getRadius()+"; center "+s01.getCenterPoint()+"; sign "+s01.sign);
		assertEquals(5.0, s01.getRadius(),EPSILON);
		assertEquals(-1.0, s01.sign,EPSILON);
		assertEquals(4.0,s01.getCenterPoint().x,EPSILON);
		assertEquals(3.0,s01.getCenterPoint().y,EPSILON);
	}

}
