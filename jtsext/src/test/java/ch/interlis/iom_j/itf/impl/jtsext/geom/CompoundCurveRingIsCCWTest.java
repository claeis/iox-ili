package ch.interlis.iom_j.itf.impl.jtsext.geom;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxException;

import com.vividsolutions.jts.geom.Coordinate;

public class CompoundCurveRingIsCCWTest {
	static final double EPSILON=0.00000001;
	class StartSegment extends StraightSegment {

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

	@Test
	public void isCW() {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segs=new ArrayList<CurveSegment>();
		addCoord(segs,120.0,  110.0); 
		addCoord(segs,110.0,  110.0); // hoechstes Segment
		addCoord(segs,110.0,  140.0); 
		addCoord(segs,120.0,  140.0); 
		addCoord(segs,150.0,  140.0); 
		addCoord(segs,150.0,  110.0); 
		addCoord(segs,120.0,  110.0);
		CompoundCurve line=new CompoundCurve(segs,fact);
		CompoundCurveRing ring=fact.createCompoundCurveRing(line);
		assertEquals(false,CompoundCurveRing.isCCW(ring));
	}
	@Test
	public void isCWdreieck() {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segs=new ArrayList<CurveSegment>();
		
		addCoord(segs,110.0,  110.0); // hoechstes Segment
		addCoord(segs,110.0,  140.0); 
		addCoord(segs,120.0,  140.0); 
		addCoord(segs,110.0,  110.0);

		CompoundCurve line=new CompoundCurve(segs,fact);
		CompoundCurveRing ring=fact.createCompoundCurveRing(line);
		assertEquals(false,CompoundCurveRing.isCCW(ring));
	}
	@Test
	public void isCWAuge() {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segs=new ArrayList<CurveSegment>();
		
		addCoord(segs,110.0,  110.0); // hoechstes Segment
		addCoord(segs,114.0,  113.0); 
		addArc(segs,  114.0,  110.0, 110.0,  110.0);

		CompoundCurve line=new CompoundCurve(segs,fact);
		CompoundCurveRing ring=fact.createCompoundCurveRing(line);
		assertEquals(false,CompoundCurveRing.isCCW(ring));
	}
	@Test
	public void isCWerstesSegmentIstHoechsteGerade() {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segs=new ArrayList<CurveSegment>();
		addCoord(segs,120.0,  140.0); // hoechstes Segment
		addCoord(segs,150.0,  140.0); 
		addCoord(segs,150.0,  110.0); 
		addCoord(segs,120.0,  110.0);
		addCoord(segs,110.0,  110.0);
		addCoord(segs,110.0,  140.0); // letztes Segment ist eine horizontale Gerade gleich hoch wie erstes Segment
		addCoord(segs,120.0,  140.0); 
		CompoundCurve line=new CompoundCurve(segs,fact);
		CompoundCurveRing ring=fact.createCompoundCurveRing(line);
		assertEquals(false,CompoundCurveRing.isCCW(ring));
	}
    @Test
    public void isCWHoechsteGerade() {
        JtsextGeometryFactory fact=new JtsextGeometryFactory();
        ArrayList<CurveSegment> segs=new ArrayList<CurveSegment>();
        
        addCoord(segs,110.0,  139.0);  
        addCoord(segs,110.0,  140.0); // hoechstes Segment
        addCoord(segs,120.0,  140.0);
        addCoord(segs,109.0,  110.0); // links vom Anfang des hoechsten Segments
        addCoord(segs,110.0,  139.0); 

        CompoundCurve line=new CompoundCurve(segs,fact);
        CompoundCurveRing ring=fact.createCompoundCurveRing(line);
        assertEquals(false,CompoundCurveRing.isCCW(ring));
    }
    @Test
    public void isCCWerstesSegmentIstHoechsteGeradeZwischeZweiHalbkreisboegen() {
        JtsextGeometryFactory fact=new JtsextGeometryFactory();
        ArrayList<CurveSegment> segs=new ArrayList<CurveSegment>();

        addCoord(segs, 120.0, 100.0); // oben rechts, aeusserer Bogen
        addCoord(segs, 118.0, 100.0); // oben rechts, innerer Bogen
        addArc(segs,   110.0, 92.0, 102.0, 100.0); // oben links, innerer Bogen
        addCoord(segs, 100.0, 100.0);  // oben links, aeusserer Bogen
        addArc(segs,   110.0, 90.0, 120.0, 100.0); // oben rechts, aeusserer Bogen
        
        CompoundCurve line=new CompoundCurve(segs,fact);
        CompoundCurveRing ring=fact.createCompoundCurveRing(line);
        assertEquals(true,CompoundCurveRing.isCCW(ring));
    }
    @Test
    public void isCWerstesSegmentIstHoechsteGeradeZwischeZweiHalbkreisboegen() {
        JtsextGeometryFactory fact=new JtsextGeometryFactory();
        ArrayList<CurveSegment> segs=new ArrayList<CurveSegment>();

        addCoord(segs, 120.0, 100.0); // oben rechts, aeusserer Bogen
        addArc(segs,   110.0, 90.0, 100.0, 100.0); // oben links, aeusserer Bogen
        addCoord(segs, 102.0, 100.0);  // oben links, innerer Bogen
        addArc(segs,   110.0, 92.0, 118.0, 100.0); // oben rechts, innerer Bogen
        addCoord(segs, 120.0, 100.0); // oben rechts, innerer Bogen
        
        
        CompoundCurve line=new CompoundCurve(segs,fact);
        CompoundCurveRing ring=fact.createCompoundCurveRing(line);
        assertEquals(false,CompoundCurveRing.isCCW(ring));
    }
    
    
	@Test
	public void isCCW() {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segs=new ArrayList<CurveSegment>();
		
		addCoord(segs,120.0,  110.0);
		addCoord(segs,150.0,  110.0); // hoechstes Segment
		addCoord(segs,150.0,  140.0); 
		addCoord(segs,120.0,  140.0); 
		addCoord(segs,110.0,  140.0); 
		addCoord(segs,110.0,  110.0);
		addCoord(segs,120.0,  110.0); 

		CompoundCurve line=new CompoundCurve(segs,fact);
		CompoundCurveRing ring=fact.createCompoundCurveRing(line);
		assertEquals(true,CompoundCurveRing.isCCW(ring));
	}
	@Test
	public void isCCWDreieck() {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segs=new ArrayList<CurveSegment>();
		
		addCoord(segs,110.0,  110.0); // hoechstes Segment
		addCoord(segs,120.0,  140.0); 
		addCoord(segs,110.0,  140.0); 
		addCoord(segs,110.0,  110.0);

		CompoundCurve line=new CompoundCurve(segs,fact);
		CompoundCurveRing ring=fact.createCompoundCurveRing(line);
		assertEquals(true,CompoundCurveRing.isCCW(ring));
	}
	@Test
	public void isCCWAuge() {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segs=new ArrayList<CurveSegment>();
		
		addCoord(segs,110.0,  110.0); 
		addArc(segs,  114.0,  110.0, 114.0,  113.0); // hoechstes Segment
		addCoord(segs,110.0,  110.0);

		CompoundCurve line=new CompoundCurve(segs,fact);
		CompoundCurveRing ring=fact.createCompoundCurveRing(line);
		assertEquals(true,CompoundCurveRing.isCCW(ring));
	}
	@Test
	public void isCCWerstesSegmentIstHoechsteGerade() {
		JtsextGeometryFactory fact=new JtsextGeometryFactory();
		ArrayList<CurveSegment> segs=new ArrayList<CurveSegment>();
		
		addCoord(segs,120.0,  140.0);  // hoechstes Segment
		addCoord(segs,110.0,  140.0); 
		addCoord(segs,110.0,  110.0);
		addCoord(segs,120.0,  110.0);
		addCoord(segs,150.0,  110.0); 
		addCoord(segs,150.0,  140.0); // letztes Segment ist eine horizontale Gerade gleich hoch wie erstes Segment
		addCoord(segs,120.0,  140.0); 

		CompoundCurve line=new CompoundCurve(segs,fact);
		CompoundCurveRing ring=fact.createCompoundCurveRing(line);
		assertEquals(true,CompoundCurveRing.isCCW(ring));
	}
    @Test
    public void isCCWHoechsteGerade() {
        JtsextGeometryFactory fact=new JtsextGeometryFactory();
        ArrayList<CurveSegment> segs=new ArrayList<CurveSegment>();
        
        addCoord(segs,120.0,  139.0);  
        addCoord(segs,120.0,  140.0); // hoechstes Segment
        addCoord(segs,110.0,  140.0);
        addCoord(segs,121.0,  110.0); // rechts vom Anfang des hoechsten Segments
        
        addCoord(segs,120.0,  139.0); 

        CompoundCurve line=new CompoundCurve(segs,fact);
        CompoundCurveRing ring=fact.createCompoundCurveRing(line);
        assertEquals(true,CompoundCurveRing.isCCW(ring));
    }

}
