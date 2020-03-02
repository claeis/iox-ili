package ch.interlis.iom_j.itf.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;
import ch.interlis.iom_j.itf.impl.jtsext.noding.CompoundCurveNoder;
import ch.interlis.iox.IoxException;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class CompoundCurveNoderTest {
    JtsextGeometryFactory fact = null;
    
	@Before
	public void setup()
	{
	    fact=new JtsextGeometryFactory();
	}

	@Test
	public void Line1simple() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktB = "LINESTRING(10 10, 20 10, 20 40, 10 40, 10 10)";
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,false);
		validator.checkValid();
		Collection<? extends CompoundCurve> noded=validator.getNodedSubstrings();
		//System.out.println(noded);
		assertEquals("[COMPOUNDCURVE ((10 10, 20 10, 20 40, 10 40, 10 10))]", noded.toString());
	}
	@Test
	public void Line2commonSegment() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    LineString l[]=new LineString[2];
	    String wktA = "LINESTRING(50 10, 20 10, 20 40, 50 40)";
	    String wktB = "LINESTRING(10 10, 20 10, 20 40, 10 40, 10 10)";
	    CompoundCurve A = fact.createCompoundCurve((LineString)wktRdr.read(wktA));
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(A);
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,false);
		validator.setEnableCommonSegments(true);
        validator.checkValid();
        Collection<? extends CompoundCurve> noded=validator.getNodedSubstrings();
        assertEquals("[COMPOUNDCURVE ((50 10, 20 10)), COMPOUNDCURVE ((20 10, 20 40)), COMPOUNDCURVE ((20 40, 50 40)), COMPOUNDCURVE ((10 10, 20 10)), COMPOUNDCURVE ((20 10, 20 40)), COMPOUNDCURVE ((20 40, 10 40, 10 10))]", noded.toString());
	}
	@Test
	public void Line1open() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktB = "LINESTRING(10 10, 20 10, 20 40, 10 40)";
	    
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,false);
		validator.checkValid();
		Collection<? extends CompoundCurve> noded=validator.getNodedSubstrings();
		//System.out.println(noded);
		assertEquals("[COMPOUNDCURVE ((10 10, 20 10, 20 40, 10 40))]", noded.toString());
	}
	@Test
	public void Line1endptOnPt() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktB = "LINESTRING(0 10, 10 10, 20 10, 20 40, 10 40, 10 10)";
	    
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,false);
		validator.checkValid();
		Collection<? extends CompoundCurve> noded=validator.getNodedSubstrings();
		//System.out.println(noded);
		assertEquals("[COMPOUNDCURVE ((0 10, 10 10)), COMPOUNDCURVE ((10 10, 20 10, 20 40, 10 40, 10 10))]", noded.toString());
	}
	@Test
	public void Line2endptOnPt() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktA = "LINESTRING(0 10, 10 10, 20 10)";
	    String wktB = "LINESTRING(20 40, 10 40, 10 10)";
	    
	    CompoundCurve A = fact.createCompoundCurve((LineString)wktRdr.read(wktA));
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(A);
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,false);
		validator.checkValid();
		Collection<? extends CompoundCurve> noded=validator.getNodedSubstrings();
		//System.out.println(noded);
		assertEquals("[COMPOUNDCURVE ((0 10, 10 10)), COMPOUNDCURVE ((10 10, 20 10)), COMPOUNDCURVE ((20 40, 10 40, 10 10))]", noded.toString());
	}
	@Test
	public void Line1endptOnLineFail() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktB = "LINESTRING(10 10, 20 10, 20 40, 10 40, 15 10)";
	    
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    ArrayList<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,false);
		try{
			validator.checkValid();
			fail();
		}catch(RuntimeException ex){
			assertTrue(ex.getMessage().startsWith("intersection"));
		}
	}
	@Test
	public void Line2endptOnLineFail() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktA = "LINESTRING(10 10, 20 10, 20 40)";
	    String wktB = "LINESTRING(10 40, 15 10)";
	    
	    CompoundCurve A = fact.createCompoundCurve((LineString)wktRdr.read(wktA));
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(A);
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,false);
		try{
			validator.checkValid();
			fail();
		}catch(RuntimeException ex){
			assertTrue(ex.getMessage().startsWith("intersection"));
		}
	}
	@Test
	public void Line1twoRing() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktB = "LINESTRING(20 10, 15 40, 10 10, 15 0, 20 10, 25 40, 30 10, 25 0, 20 10)";
	    
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,false);
		validator.checkValid();
		Collection<? extends CompoundCurve> noded=validator.getNodedSubstrings();
		//System.out.println(noded);
		assertEquals("[COMPOUNDCURVE ((20 10, 15 40, 10 10, 15 0, 20 10)), COMPOUNDCURVE ((20 10, 25 40, 30 10, 25 0, 20 10))]", noded.toString());
	}
	@Test
	public void Line2twoRing() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktA = "LINESTRING(20 10, 15 40, 10 10, 15 0, 20 10)";
	    String wktB = "LINESTRING(20 10, 25 40, 30 10, 25 0, 20 10)";
	    
	    CompoundCurve A = fact.createCompoundCurve((LineString)wktRdr.read(wktA));
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(A);
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,false);
		validator.checkValid();
		Collection<? extends CompoundCurve> noded=validator.getNodedSubstrings();
		assertEquals("[COMPOUNDCURVE ((20 10, 15 40, 10 10, 15 0, 20 10)), COMPOUNDCURVE ((20 10, 25 40, 30 10, 25 0, 20 10))]", noded.toString());
	}
	@Test
	public void Line1innerRing() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktB = "LINESTRING(20 10, 15 30, 15 10, 20 10, 20 40, 10 40, 10 0, 20 0, 20 10)";
	    
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,false);
		validator.checkValid();
		Collection<? extends CompoundCurve> noded=validator.getNodedSubstrings();
		assertEquals("[COMPOUNDCURVE ((20 10, 15 30, 15 10, 20 10)), COMPOUNDCURVE ((20 10, 20 40, 10 40, 10 0, 20 0, 20 10))]", noded.toString());
	}
	@Test
	public void Line2innerRing() throws ParseException, IoxException
	{
		
	    ArrayList<CurveSegment> l=new ArrayList<CurveSegment>();
	    l.add(new ArcSegment(new Coordinate(110, 110),new Coordinate(115, 108),new Coordinate(120,110)));
	    l.add(new StraightSegment(new Coordinate(120, 110),new Coordinate(120,140)));
	    l.add(new StraightSegment(new Coordinate(120, 140),new Coordinate(110,140)));
	    l.add(new StraightSegment(new Coordinate(110, 140),new Coordinate(110,110)));
	    CompoundCurve B = fact.createCompoundCurve(l);
		
	    l=new ArrayList<CurveSegment>();
	    l.add(new StraightSegment(new Coordinate(110, 110),new Coordinate(115,115)));
	    l.add(new StraightSegment(new Coordinate(115, 115),new Coordinate(115,120)));
	    l.add(new StraightSegment(new Coordinate(115, 120),new Coordinate(112,120)));
	    l.add(new StraightSegment(new Coordinate(112, 120),new Coordinate(110,110)));
	    CompoundCurve A = fact.createCompoundCurve(l);

	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
	    segs.add(A);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,false);
		validator.checkValid();
		Collection<? extends CompoundCurve> noded=validator.getNodedSubstrings();
		assertEquals("[COMPOUNDCURVE (CIRCULARSTRING (110 110, 115 108, 120 110), (120 110, 120 140, 110 140, 110 110)), COMPOUNDCURVE ((110 110, 115 115, 115 120, 112 120, 110 110))]", noded.toString());
	}
}
