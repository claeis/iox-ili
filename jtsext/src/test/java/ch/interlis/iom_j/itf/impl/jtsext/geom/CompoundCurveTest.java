package ch.interlis.iom_j.itf.impl.jtsext.geom;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class CompoundCurveTest {

	private JtsextGeometryFactory fact = null;
	
	private class StartSegment extends StraightSegment {
		public StartSegment(Coordinate startPoint) {
			super(startPoint, startPoint);
		}
	}
	
	private void addCoord(ArrayList<CurveSegment> polyline, double x, double y) {
		if(polyline.size()==0) {
			Coordinate pt=new Coordinate(x,y);
			polyline.add(new StartSegment(pt));
		}else if(polyline.size()==1 && polyline.get(0) instanceof StartSegment) {
			StraightSegment seg0=(StraightSegment) polyline.get(0);
			Coordinate pt=new Coordinate(x,y);
			polyline.set(0, new StraightSegment(seg0.getStartPoint(), pt));
		}else {
			CurveSegment segn=(CurveSegment) polyline.get(polyline.size()-1);
			Coordinate pt=new Coordinate(x,y);
			polyline.add(new StraightSegment(segn.getEndPoint(), pt));
		}
	}
	
	private void addArc(ArrayList<CurveSegment> polyline, double ax, double ay,double x, double y) {
		if(polyline.size()==0) {
			throw new IllegalArgumentException();
		}else if(polyline.size()==1 && polyline.get(0) instanceof StartSegment) {
			StraightSegment seg0=(StraightSegment) polyline.get(0);
			Coordinate apt=new Coordinate(ax,ay);
			Coordinate pt=new Coordinate(x,y);
			polyline.set(0, new ArcSegment(seg0.getStartPoint(), apt,pt));
		}else {
			CurveSegment segn=(CurveSegment) polyline.get(polyline.size()-1);
			Coordinate apt=new Coordinate(ax,ay);
			Coordinate pt=new Coordinate(x,y);
			polyline.add(new ArcSegment(segn.getEndPoint(), apt,pt));
		}
	}
	
	@Before
	public void setup(){
	    fact=new JtsextGeometryFactory();
	}
	  
	// prueft, ob eine geschlossene CompoundCurve erstellt werden kann.
	@Test
	public void isClosed_Ok() throws ParseException {
		ArrayList<CurveSegment> segs=new ArrayList<CurveSegment>();
		addCoord(segs,10.0, 10.0);
		addCoord(segs,20.0, 10.0); 
		addArc(  segs,20.0, 20.0, 16.0, 14.0);
		addCoord(segs,10.0, 10.0);
		JtsextGeometryFactory geomFact=new JtsextGeometryFactory();
		CompoundCurve compoundCurve=new CompoundCurve(segs,geomFact);
		assertEquals(true,compoundCurve.isClosed());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn
	// die CompoundCurve nicht geschlossen wird.
	@Test
	public void isClosed_False() throws ParseException {
		ArrayList<CurveSegment> segs=new ArrayList<CurveSegment>();
		addCoord(segs,10.0, 10.0);
		addCoord(segs,20.0, 10.0); 
		addArc(  segs,20.0, 20.0, 16.0, 14.0);
		JtsextGeometryFactory geomFact=new JtsextGeometryFactory();
		CompoundCurve compoundCurve=new CompoundCurve(segs,geomFact);
		assertEquals(false,compoundCurve.isClosed());
	}
	
	// prueft, ob eine CompoundCurve aus einem
	// geschlossenen LineString erstellt werden kann.
	@Test
	public void isClosed_OnlyStraights_Ok() throws ParseException {
		WKTReader wktReader = new WKTReader(fact);
	    String wktCompoundCurve = "LINESTRING(10 10, 20 10, 20 40, 10 10)";
	    CompoundCurve compoundCurve = fact.createCompoundCurve((LineString)wktReader.read(wktCompoundCurve));
		assertEquals(true,compoundCurve.isClosed());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn
	// die CompoundCurve aus einem nicht geschlossenen LineString erstellt wird.
	@Test
	public void isClosed_OnlyStraights_False() throws ParseException {
		WKTReader wktReader = new WKTReader(fact);
	    String wktCompoundCurve = "LINESTRING(10 10, 20 10, 20 40)";
	    CompoundCurve compoundCurve = fact.createCompoundCurve((LineString)wktReader.read(wktCompoundCurve));
		assertEquals(false,compoundCurve.isClosed());
	}
}