package ch.interlis.iom_j.itf.impl.jtsext.algorithm;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;
import ch.interlis.iox.IoxException;

public class CurveSegmentIntersectorTest {

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

	// Es wird getestet ob die 2.te Koordinate der Fehlermeldung der self-intersection richtig berechnet wird.
	// expected: coord (0.0, 0.0, NaN),coord2 (6.0, 0.0, NaN)
	// Die 2.te Koordinate soll auf den Schnittpunkt zeigen.
	@Test
	public void intersectionCoordCalculation() throws IoxException{
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		// straight
		StraightSegment s0=new StraightSegment(
			new Coordinate(0.0, 0.0), 
			new Coordinate(12.0, 0.0)
		);
		// arc
		ArcSegment s1=new ArcSegment(
			new Coordinate(0.0, 0.0), 
			new Coordinate(3.0, 0.0), 
			new Coordinate(6.0, 0.0)
		);
		// intersection test
		li.computeIntersection(s0, s1);
		assertTrue(li.hasIntersection());
		// coord1
		Coordinate is=li.getIntersection(0);
		assertTrue(is.x==0.0);
		assertTrue(is.y==0.0);
		// coord2
		Coordinate is2=li.getIntersection(1);
		assertTrue(is2.x==6.0);
		assertTrue(is2.y==0.0);
	}
	
	// Es wird getestet ob die 2.te Koordinate der Fehlermeldung der self-intersection richtig berechnet wird.
	// Dabei sollen 10 Stellen nach dem Komma erstellt werden.
	// expected: coord (0.0001234567, 0.0001234567, NaN),coord2 (600.0001234567, 0.0001234567, NaN)
	// Die 2.te Koordinate soll auf den Schnittpunkt zeigen.
	@Test
	public void calculateDigitsAfterDecimalPoint() throws IoxException{
		CurveSegmentIntersector li=new CurveSegmentIntersector();
		// straight
		StraightSegment s0=new StraightSegment(
			new Coordinate(0.0001234567, 0.0001234567), 
			new Coordinate(1200.0001234567, 0.0001234567)
		);
		// arc
		ArcSegment s1=new ArcSegment(
			new Coordinate(0.0001234567, 0.0001234567), 
			new Coordinate(300.0001234567, 0.0001234567), 
			new Coordinate(600.0001234567, 0.0001234567)
		);
		// intersection test
		li.computeIntersection(s0, s1);
		assertTrue(li.hasIntersection());
		// coord1
		Coordinate is=li.getIntersection(0);
		assertTrue(is.x==0.0001234567);
		assertTrue(is.y==0.0001234567);
		// coord2
		Coordinate is2=li.getIntersection(1);
		assertTrue(is2.x==600.0001234567);
		assertTrue(is2.y==0.0001234567);
	}
	
	// Es wird getestet ob die 2.te Koordinate der Fehlermeldung der self-intersection richtig berechnet wird.
	// Dabei werden grosse Zahlen verwendet.
	@Ignore //FIXME calculation of is2 is wrong.
	public void intersectionCoordCalculationOrigData() throws IoxException{
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
		assertTrue(li.hasIntersection());
		// coord1
		Coordinate is=li.getIntersection(0);
		assertTrue(is.x==2625285.5890);
		assertTrue(is.y==1238688.2690);
		// coord2
		Coordinate is2=li.getIntersection(1);
		// actual x: 2625285.3554505454
		// actual y: 1238688.1325746486
	}
}
