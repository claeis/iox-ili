package ch.interlis.iom_j.itf.impl.jtsext.geom;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import ch.interlis.iox.IoxException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class ArcArcRemoveOverlapTest {
	static final double EPSILON=0.00000001;

	@Test
	public void startCWotherCW() throws IoxException {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segments=new ArrayList<CurveSegment>();
		Coordinate startPt=new Coordinate(0.0,0.0);
		Coordinate midPt=new Coordinate(0.0,6.0);
		Coordinate endArcPt=new Coordinate(4.0,8.0);
		ArcSegment s0=new ArcSegment(startPt,midPt,endArcPt);
		assertEquals(1.0, s0.getSign(),EPSILON);
		System.out.println("r "+s0.getRadius()+"; center "+s0.getCenterPoint()+"; sign "+s0.sign);
		segments.add(s0);
		CompoundCurve line0=new CompoundCurve(segments,fact);
		System.out.println(line0);
		
		ArcSegment s1=new ArcSegment(startPt,new Coordinate(0.0,6.0),new Coordinate(Math.sqrt(100.0-9.0),13.0));
		assertEquals(1.0, s1.getSign(),EPSILON);
		System.out.println("s1 r "+s1.getRadius()+"; center "+s1.getCenterPoint()+"; sign "+s1.sign);
		boolean modified=line0.removeOverlap(s0, s1, 1.0);
		assertTrue(modified);
		System.out.println(line0);
		ArcSegment s00=(ArcSegment) line0.getSegments().get(0);
		System.out.println("newseg start "+s00.getStartPoint()+"; end "+s00.getEndPoint());
		System.out.println("newseg r "+s00.getRadius()+"; center "+s00.getCenterPoint()+"; sign "+s00.sign);
		assertEquals(1.0, s00.sign,EPSILON);
		assertEquals(0.0,s00.getStartPoint().x,EPSILON);
		assertEquals(0.0,s00.getStartPoint().y,EPSILON);
		assertEquals(9.862958587323327,s00.getCenterPoint().x,EPSILON);
		assertEquals(1.6498629957500195,s00.getCenterPoint().y,EPSILON);
		assertEquals(10.0,s00.getRadius(),EPSILON);
		ArcSegment s01=(ArcSegment) line0.getSegments().get(1);
		System.out.println("modseg r "+s01.getRadius()+"; center "+s01.getCenterPoint()+"; sign "+s01.sign);
		assertEquals(5.0, s01.getRadius(),EPSILON);
		assertEquals(1.0, s01.sign,EPSILON);
		assertEquals(4.0,s01.getCenterPoint().x,EPSILON);
		assertEquals(3.0,s01.getCenterPoint().y,EPSILON);
	}
	@Test
	public void startCWotherCCW() throws IoxException {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segments=new ArrayList<CurveSegment>();
		Coordinate startPt=new Coordinate(0.0,0.0);
		Coordinate midPt=new Coordinate(0.0,6.0);
		Coordinate endArcPt=new Coordinate(4.0,8.0);
		ArcSegment s0=new ArcSegment(startPt,midPt,endArcPt);
		assertEquals(1.0, s0.getSign(),EPSILON);
		System.out.println("r "+s0.getRadius()+"; center "+s0.getCenterPoint()+"; sign "+s0.sign);
		segments.add(s0);
		CompoundCurve line0=new CompoundCurve(segments,fact);
		System.out.println(line0);
		
		ArcSegment s1=new ArcSegment(new Coordinate(Math.sqrt(100.0-9.0),13.0),new Coordinate(0.0,6.0),startPt);
		assertEquals(-1.0, s1.getSign(),EPSILON);
		System.out.println("s1 r "+s1.getRadius()+"; center "+s1.getCenterPoint()+"; sign "+s1.sign);
		boolean modified=line0.removeOverlap(s0, s1, 1.0);
		assertTrue(modified);
		System.out.println(line0);
		ArcSegment s00=(ArcSegment) line0.getSegments().get(0);
		System.out.println("newseg start "+s00.getStartPoint()+"; end "+s00.getEndPoint());
		System.out.println("newseg r "+s00.getRadius()+"; center "+s00.getCenterPoint()+"; sign "+s00.sign);
		assertEquals(1.0, s00.sign,EPSILON);
		assertEquals(0.0,s00.getStartPoint().x,EPSILON);
		assertEquals(0.0,s00.getStartPoint().y,EPSILON);
		assertEquals(9.862958587323327,s00.getCenterPoint().x,EPSILON);
		assertEquals(1.6498629957500195,s00.getCenterPoint().y,EPSILON);
		assertEquals(10.0,s00.getRadius(),EPSILON);
		ArcSegment s01=(ArcSegment) line0.getSegments().get(1);
		System.out.println("modseg r "+s01.getRadius()+"; center "+s01.getCenterPoint()+"; sign "+s01.sign);
		assertEquals(5.0, s01.getRadius(),EPSILON);
		assertEquals(1.0, s01.sign,EPSILON);
		assertEquals(4.0,s01.getCenterPoint().x,EPSILON);
		assertEquals(3.0,s01.getCenterPoint().y,EPSILON);
	}
	@Test
	public void startCCWotherCW() throws IoxException {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segments=new ArrayList<CurveSegment>();
		Coordinate startPt=new Coordinate(0.0,0.0);
		Coordinate midPt=new Coordinate(0.0,6.0);
		Coordinate endArcPt=new Coordinate(-4.0,8.0);
		ArcSegment s0=new ArcSegment(startPt,midPt,endArcPt);
		assertEquals(-1.0, s0.getSign(),EPSILON);
		System.out.println("r "+s0.getRadius()+"; center "+s0.getCenterPoint()+"; sign "+s0.sign);
		segments.add(s0);
		CompoundCurve line0=new CompoundCurve(segments,fact);
		System.out.println(line0);
		
		ArcSegment s1=new ArcSegment(startPt,new Coordinate(0.0,6.0),new Coordinate(Math.sqrt(100.0-9.0),13.0));
		assertEquals(1.0, s1.getSign(),EPSILON);
		System.out.println("s1 r "+s1.getRadius()+"; center "+s1.getCenterPoint()+"; sign "+s1.sign);
		boolean modified=line0.removeOverlap(s0, s1, 1.0);
		assertTrue(modified);
		System.out.println(line0);
		ArcSegment newseg=(ArcSegment) line0.getSegments().get(0);
		System.out.println("newseg start "+newseg.getStartPoint()+"; end "+newseg.getEndPoint());
		System.out.println("newseg r "+newseg.getRadius()+"; center "+newseg.getCenterPoint()+"; sign "+newseg.sign);
		ArcSegment modseg=(ArcSegment) line0.getSegments().get(1);
		System.out.println("modseg r "+modseg.getRadius()+"; center "+modseg.getCenterPoint()+"; sign "+modseg.sign);
		assertEquals(1.0, newseg.sign,EPSILON);
		assertEquals(0.0,newseg.getStartPoint().x,EPSILON);
		assertEquals(0.0,newseg.getStartPoint().y,EPSILON);
		assertEquals(8.944512924738119,newseg.getCenterPoint().x,EPSILON);
		assertEquals(4.471653893045913,newseg.getCenterPoint().y,EPSILON);
		assertEquals(10.0,newseg.getRadius(),EPSILON);
		assertEquals(5.0, modseg.getRadius(),EPSILON);
		assertEquals(-1.0, modseg.sign,EPSILON);
		assertEquals(-4.0,modseg.getCenterPoint().x,EPSILON);
		assertEquals(3.0,modseg.getCenterPoint().y,EPSILON);
	}
	@Test
	public void startCCWotherCCW() throws IoxException {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segments=new ArrayList<CurveSegment>();
		Coordinate startPt=new Coordinate(0.0,0.0);
		Coordinate midPt=new Coordinate(0.0,6.0);
		Coordinate endArcPt=new Coordinate(-4.0,8.0);
		ArcSegment s0=new ArcSegment(startPt,midPt,endArcPt);
		assertEquals(-1.0, s0.getSign(),EPSILON);
		System.out.println("r "+s0.getRadius()+"; center "+s0.getCenterPoint()+"; sign "+s0.sign);
		segments.add(s0);
		CompoundCurve line0=new CompoundCurve(segments,fact);
		System.out.println(line0);
		
		ArcSegment s1=new ArcSegment(new Coordinate(Math.sqrt(100.0-9.0),13.0),new Coordinate(0.0,6.0),startPt);
		assertEquals(-1.0, s1.getSign(),EPSILON);
		System.out.println("s1 r "+s1.getRadius()+"; center "+s1.getCenterPoint()+"; sign "+s1.sign);
		boolean modified=line0.removeOverlap(s0, s1, 1.0);
		assertTrue(modified);
		System.out.println(line0);
		ArcSegment newseg=(ArcSegment) line0.getSegments().get(0);
		System.out.println("newseg start "+newseg.getStartPoint()+"; end "+newseg.getEndPoint());
		System.out.println("newseg r "+newseg.getRadius()+"; center "+newseg.getCenterPoint()+"; sign "+newseg.sign);
		ArcSegment modseg=(ArcSegment) line0.getSegments().get(1);
		System.out.println("modseg r "+modseg.getRadius()+"; center "+modseg.getCenterPoint()+"; sign "+modseg.sign);
		assertEquals(1.0, newseg.sign,EPSILON);
		assertEquals(0.0,newseg.getStartPoint().x,EPSILON);
		assertEquals(0.0,newseg.getStartPoint().y,EPSILON);
		assertEquals(8.944512924738115,newseg.getCenterPoint().x,EPSILON);
		assertEquals(4.471653893045911,newseg.getCenterPoint().y,EPSILON);
		assertEquals(10.0,newseg.getRadius(),EPSILON);
		assertEquals(5.0, modseg.getRadius(),EPSILON);
		assertEquals(-1.0, modseg.sign,EPSILON);
		assertEquals(-4.0,modseg.getCenterPoint().x,EPSILON);
		assertEquals(3.0,modseg.getCenterPoint().y,EPSILON);
	}

	
	@Test
	public void ConcaveStartCWotherCW() throws IoxException {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segments=new ArrayList<CurveSegment>();
		Coordinate startPt=new Coordinate(0.0,0.0);
		Coordinate midPt=new Coordinate(0.0,6.0);
		Coordinate endArcPt=new Coordinate(4.0,8.0);
		ArcSegment s0=new ArcSegment(startPt,midPt,endArcPt);
		assertEquals(1.0, s0.getSign(),EPSILON);
		System.out.println("r "+s0.getRadius()+"; center "+s0.getCenterPoint()+"; sign "+s0.sign);
		segments.add(s0);
		CompoundCurve line0=new CompoundCurve(segments,fact);
		System.out.println(line0);
		
		ArcSegment s1=new ArcSegment(new Coordinate(-Math.sqrt(100.0-9.0),13.0),new Coordinate(0.0,6.0),startPt);
		assertEquals(1.0, s1.getSign(),EPSILON);
		System.out.println("s1 r "+s1.getRadius()+"; center "+s1.getCenterPoint()+"; sign "+s1.sign);
		boolean modified=line0.removeOverlap(s0, s1, 1.0);
		assertTrue(modified);
		System.out.println(line0);
		ArcSegment newseg=(ArcSegment) line0.getSegments().get(0);
		System.out.println("newseg start "+newseg.getStartPoint()+"; end "+newseg.getEndPoint());
		System.out.println("newseg r "+newseg.getRadius()+"; center "+newseg.getCenterPoint()+"; sign "+newseg.sign);
		ArcSegment modseg=(ArcSegment) line0.getSegments().get(1);
		System.out.println("modseg r "+modseg.getRadius()+"; center "+modseg.getCenterPoint()+"; sign "+modseg.sign);
		assertEquals(-1.0, newseg.sign,EPSILON);
		assertEquals(0.0,newseg.getStartPoint().x,EPSILON);
		assertEquals(0.0,newseg.getStartPoint().y,EPSILON);
		assertEquals(-8.944512924738115,newseg.getCenterPoint().x,EPSILON);
		assertEquals(4.471653893045911,newseg.getCenterPoint().y,EPSILON);
		assertEquals(10.0,newseg.getRadius(),EPSILON);
		assertEquals(5.0, modseg.getRadius(),EPSILON);
		assertEquals(1.0, modseg.sign,EPSILON);
		assertEquals(4.0,modseg.getCenterPoint().x,EPSILON);
		assertEquals(3.0,modseg.getCenterPoint().y,EPSILON);
	}
	
	
	@Test
	public void endCCWotherCW() throws IoxException {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segments=new ArrayList<CurveSegment>();
		Coordinate startArcPt=new Coordinate(4.0,8.0);
		Coordinate midPt=new Coordinate(0.0,6.0);
		Coordinate endPt=new Coordinate(0.0,0.0);
		ArcSegment s0=new ArcSegment(startArcPt,midPt,endPt);
		assertEquals(-1.0, s0.getSign(),EPSILON);
		System.out.println("r "+s0.getRadius()+"; center "+s0.getCenterPoint()+"; sign "+s0.sign);
		segments.add(s0);
		CompoundCurve line0=new CompoundCurve(segments,fact);
		System.out.println(line0);
		
		ArcSegment s1=new ArcSegment(new Coordinate(0.0,0.0),new Coordinate(0.0,6.0),new Coordinate(Math.sqrt(100.0-9.0),13.0));
		assertEquals(1.0, s1.getSign(),EPSILON);
		System.out.println("s1 r "+s1.getRadius()+"; center "+s1.getCenterPoint()+"; sign "+s1.sign);
		boolean modified=line0.removeOverlap(s0, s1, 1.0);
		assertTrue(modified);
		System.out.println(line0);
		ArcSegment modseg=(ArcSegment) line0.getSegments().get(0);
		System.out.println("modseg r "+modseg.getRadius()+"; center "+modseg.getCenterPoint()+"; sign "+modseg.sign);
		ArcSegment newseg=(ArcSegment) line0.getSegments().get(1);
		System.out.println("newseg start "+newseg.getStartPoint()+"; end "+newseg.getEndPoint());
		System.out.println("newseg r "+newseg.getRadius()+"; center "+newseg.getCenterPoint()+"; sign "+newseg.sign);
		assertEquals(5.0, modseg.getRadius(),EPSILON);
		assertEquals(-1.0, modseg.sign,EPSILON);
		assertEquals(4.0,modseg.getCenterPoint().x,EPSILON);
		assertEquals(3.0,modseg.getCenterPoint().y,EPSILON);
		assertEquals(-1.0, newseg.sign,EPSILON);
		assertEquals(0.0,newseg.getEndPoint().x,EPSILON);
		assertEquals(0.0,newseg.getEndPoint().y,EPSILON);
		assertEquals(9.862958587323327,newseg.getCenterPoint().x,EPSILON);
		assertEquals(1.6498629957500193,newseg.getCenterPoint().y,EPSILON);
		assertEquals(10.0,newseg.getRadius(),EPSILON);
	}
	@Test
	public void endCCWotherCCW() throws IoxException {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segments=new ArrayList<CurveSegment>();
		Coordinate startArcPt=new Coordinate(4.0,8.0);
		Coordinate midPt=new Coordinate(0.0,6.0);
		Coordinate endPt=new Coordinate(0.0,0.0);
		ArcSegment s0=new ArcSegment(startArcPt,midPt,endPt);
		assertEquals(-1.0, s0.getSign(),EPSILON);
		System.out.println("r "+s0.getRadius()+"; center "+s0.getCenterPoint()+"; sign "+s0.sign);
		segments.add(s0);
		CompoundCurve line0=new CompoundCurve(segments,fact);
		System.out.println(line0);
		
		ArcSegment s1=new ArcSegment(new Coordinate(Math.sqrt(100.0-9.0),13.0),new Coordinate(0.0,6.0),new Coordinate(0.0,0.0));
		assertEquals(-1.0, s1.getSign(),EPSILON);
		System.out.println("s1 r "+s1.getRadius()+"; center "+s1.getCenterPoint()+"; sign "+s1.sign);
		boolean modified=line0.removeOverlap(s0, s1, 1.0);
		assertTrue(modified);
		System.out.println(line0);
		ArcSegment modseg=(ArcSegment) line0.getSegments().get(0);
		System.out.println("modseg r "+modseg.getRadius()+"; center "+modseg.getCenterPoint()+"; sign "+modseg.sign);
		ArcSegment newseg=(ArcSegment) line0.getSegments().get(1);
		System.out.println("newseg start "+newseg.getStartPoint()+"; end "+newseg.getEndPoint());
		System.out.println("newseg r "+newseg.getRadius()+"; center "+newseg.getCenterPoint()+"; sign "+newseg.sign);
		assertEquals(5.0, modseg.getRadius(),EPSILON);
		assertEquals(-1.0, modseg.sign,EPSILON);
		assertEquals(4.0,modseg.getCenterPoint().x,EPSILON);
		assertEquals(3.0,modseg.getCenterPoint().y,EPSILON);
		assertEquals(-1.0, newseg.sign,EPSILON);
		assertEquals(0.0,newseg.getEndPoint().x,EPSILON);
		assertEquals(0.0,newseg.getEndPoint().y,EPSILON);
		assertEquals(9.862958587323327,newseg.getCenterPoint().x,EPSILON);
		assertEquals(1.6498629957500195,newseg.getCenterPoint().y,EPSILON);
		assertEquals(10.0,newseg.getRadius(),EPSILON);
	}
	@Test
	public void endCWotherCW() throws IoxException {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segments=new ArrayList<CurveSegment>();
		Coordinate startArcPt=new Coordinate(-4.0,8.0);
		Coordinate midPt=new Coordinate(0.0,6.0);
		Coordinate endPt=new Coordinate(0.0,0.0);
		ArcSegment s0=new ArcSegment(startArcPt,midPt,endPt);
		assertEquals(1.0, s0.getSign(),EPSILON);
		System.out.println("r "+s0.getRadius()+"; center "+s0.getCenterPoint()+"; sign "+s0.sign);
		segments.add(s0);
		CompoundCurve line0=new CompoundCurve(segments,fact);
		System.out.println(line0);
		
		ArcSegment s1=new ArcSegment(new Coordinate(0.0,0.0),new Coordinate(0.0,6.0),new Coordinate(Math.sqrt(100.0-9.0),13.0));
		assertEquals(1.0, s1.getSign(),EPSILON);
		System.out.println("s1 r "+s1.getRadius()+"; center "+s1.getCenterPoint()+"; sign "+s1.sign);
		boolean modified=line0.removeOverlap(s0, s1, 1.0);
		assertTrue(modified);
		System.out.println(line0);
		ArcSegment modseg=(ArcSegment) line0.getSegments().get(0);
		System.out.println("modseg r "+modseg.getRadius()+"; center "+modseg.getCenterPoint()+"; sign "+modseg.sign);
		ArcSegment newseg=(ArcSegment) line0.getSegments().get(1);
		System.out.println("newseg start "+newseg.getStartPoint()+"; end "+newseg.getEndPoint());
		System.out.println("newseg r "+newseg.getRadius()+"; center "+newseg.getCenterPoint()+"; sign "+newseg.getSign());
		assertEquals(5.0, modseg.getRadius(),EPSILON);
		assertEquals(1.0, modseg.sign,EPSILON);
		assertEquals(-4.0,modseg.getCenterPoint().x,EPSILON);
		assertEquals(3.0,modseg.getCenterPoint().y,EPSILON);
		assertEquals(-1.0, newseg.sign,EPSILON);
		assertEquals(0.0,newseg.getEndPoint().x,EPSILON);
		assertEquals(0.0,newseg.getEndPoint().y,EPSILON);
		assertEquals(8.944512924738119,newseg.getCenterPoint().x,EPSILON);
		assertEquals(4.471653893045913,newseg.getCenterPoint().y,EPSILON);
		assertEquals(10.0,newseg.getRadius(),EPSILON);
	}
	@Test
	public void endCWotherCCW() throws IoxException {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segments=new ArrayList<CurveSegment>();
		Coordinate startArcPt=new Coordinate(-4.0,8.0);
		Coordinate midPt=new Coordinate(0.0,6.0);
		Coordinate endPt=new Coordinate(0.0,0.0);
		ArcSegment s0=new ArcSegment(startArcPt,midPt,endPt);
		assertEquals(1.0, s0.getSign(),EPSILON);
		System.out.println("r "+s0.getRadius()+"; center "+s0.getCenterPoint()+"; sign "+s0.sign);
		segments.add(s0);
		CompoundCurve line0=new CompoundCurve(segments,fact);
		System.out.println(line0);
		
		ArcSegment s1=new ArcSegment(new Coordinate(Math.sqrt(100.0-9.0),13.0),new Coordinate(0.0,6.0),new Coordinate(0.0,0.0));
		assertEquals(-1.0, s1.getSign(),EPSILON);
		System.out.println("s1 r "+s1.getRadius()+"; center "+s1.getCenterPoint()+"; sign "+s1.sign);
		boolean modified=line0.removeOverlap(s0, s1, 1.0);
		assertTrue(modified);
		System.out.println(line0);
		ArcSegment modseg=(ArcSegment) line0.getSegments().get(0);
		System.out.println("modseg r "+modseg.getRadius()+"; center "+modseg.getCenterPoint()+"; sign "+modseg.sign);
		ArcSegment newseg=(ArcSegment) line0.getSegments().get(1);
		System.out.println("newseg start "+newseg.getStartPoint()+"; end "+newseg.getEndPoint());
		System.out.println("newseg r "+newseg.getRadius()+"; center "+newseg.getCenterPoint()+"; sign "+newseg.sign);
		assertEquals(5.0, modseg.getRadius(),EPSILON);
		assertEquals(1.0, modseg.sign,EPSILON);
		assertEquals(-4.0,modseg.getCenterPoint().x,EPSILON);
		assertEquals(3.0,modseg.getCenterPoint().y,EPSILON);
		assertEquals(-1.0, newseg.sign,EPSILON);
		assertEquals(0.0,newseg.getEndPoint().x,EPSILON);
		assertEquals(0.0,newseg.getEndPoint().y,EPSILON);
		assertEquals(8.944512924738115,newseg.getCenterPoint().x,EPSILON);
		assertEquals(4.471653893045911,newseg.getCenterPoint().y,EPSILON);
		assertEquals(10.0,newseg.getRadius(),EPSILON);
	}
	
    @Test
    public void overlapReplacedByStraight() throws IoxException {
        Coordinate startCoord = new Coordinate(2759364.607, 1221799.353);
        Coordinate middleCoord = new Coordinate(2759364.449, 1221799.343);
        Coordinate endCoord = new Coordinate(2759364.345, 1221799.224);
        
        ArcSegment thisSegment = new ArcSegment(startCoord, middleCoord, endCoord);
        ArcSegment otherSegment = new ArcSegment(new Coordinate(2759364.948, 1221801.003),new Coordinate(2759364.647, 1221800.113),new Coordinate(2759364.345, 1221799.224));
        
        ArrayList<CurveSegment> segments = new ArrayList<CurveSegment>();
        segments.add(thisSegment);

        CompoundCurve compCurve = new CompoundCurve(segments, new GeometryFactory());
        double newVertexOffset = 0.002;
        boolean removeOverlap = compCurve.removeOverlap(thisSegment, otherSegment, newVertexOffset);
        assertTrue(removeOverlap);
        
        if (!(compCurve.getSegments().get(0) instanceof ArcSegment)) {
            fail("First Segment in Segments must be instance of ArcSegment sein.");
        }
        ArcSegment modseg=(ArcSegment) compCurve.getSegments().get(0);
        if (!(compCurve.getSegments().get(1) instanceof StraightSegment)) {
            fail("Second Segment in Segments must be instance of Straight Segment sein.");
        }
        StraightSegment newseg=(StraightSegment) compCurve.getSegments().get(1);
        
        assertEquals(modseg.getEndPoint().x, newseg.getStartPoint().x, EPSILON);
        assertEquals(modseg.getEndPoint().y, newseg.getStartPoint().y, EPSILON);
        
        assertEquals(endCoord.x, newseg.getEndPoint().x, EPSILON);
        assertEquals(endCoord.y, newseg.getEndPoint().y, EPSILON);
        
        assertEquals(startCoord.x, modseg.getStartPoint().x, EPSILON);
        assertEquals(startCoord.y, modseg.getStartPoint().y, EPSILON);
        
        assertEquals(middleCoord.x, modseg.getMidPoint().x, EPSILON);
        assertEquals(middleCoord.y, modseg.getMidPoint().y, EPSILON);
     
    }
}
