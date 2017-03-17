package ch.interlis.iom_j.itf.impl.jtsext.geom;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

public class KleinpTest {
	static final double EPS=0.00000001;

	@Test
	public void startLinks() {
		Coordinate p0=new Coordinate(0.0,0.0);
		Coordinate p1=new Coordinate(0.0,5.0);
		Coordinate ret=CompoundCurve.calcKleinp(p0, p1, 0.0, 1.0);
		assertEquals(ret.x,-1.0,EPS);
		assertEquals(ret.y,0.0,EPS);
	}
	@Test
	public void startRechts() {
		Coordinate p0=new Coordinate(0.0,0.0);
		Coordinate p1=new Coordinate(0.0,5.0);
		Coordinate ret=CompoundCurve.calcKleinp(p0, p1, 0.0, -1.0);
		assertEquals(ret.x,1.0,EPS);
		assertEquals(ret.y,0.0,EPS);
	}
	@Test
	public void mitteLinks() {
		Coordinate p0=new Coordinate(0.0,0.0);
		Coordinate p1=new Coordinate(0.0,5.0);
		Coordinate ret=CompoundCurve.calcKleinp(p0, p1, 2.0, 1.0);
		assertEquals(ret.x,-1.0,EPS);
		assertEquals(ret.y,2.0,EPS);
	}
	@Test
	public void mitteRechts() {
		Coordinate p0=new Coordinate(0.0,0.0);
		Coordinate p1=new Coordinate(0.0,5.0);
		Coordinate ret=CompoundCurve.calcKleinp(p0, p1, 2.0, -1.0);
		assertEquals(ret.x,1.0,EPS);
		assertEquals(ret.y,2.0,EPS);
	}
	@Test
	public void endRechts() {
		Coordinate p0=new Coordinate(0.0,0.0);
		Coordinate p1=new Coordinate(0.0,5.0);
		Coordinate ret=CompoundCurve.calcKleinp(p0, p1, 5.0, -1.0);
		assertEquals(ret.x,1.0,EPS);
		assertEquals(ret.y,5.0,EPS);
	}
	@Test
	public void endLinks() {
		Coordinate p0=new Coordinate(0.0,0.0);
		Coordinate p1=new Coordinate(0.0,5.0);
		Coordinate ret=CompoundCurve.calcKleinp(p0, p1, 5.0, 1.0);
		assertEquals(ret.x,-1.0,EPS);
		assertEquals(ret.y,5.0,EPS);
	}

}
