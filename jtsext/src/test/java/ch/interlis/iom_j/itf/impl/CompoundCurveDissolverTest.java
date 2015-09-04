package ch.interlis.iom_j.itf.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;
import ch.interlis.iom_j.itf.impl.jtsext.noding.CompoundCurveDissolver;
import ch.interlis.iom_j.itf.impl.jtsext.noding.CompoundCurveNoder;
import ch.interlis.iox.IoxException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.noding.BasicSegmentString;
import com.vividsolutions.jts.noding.SegmentString;

public class CompoundCurveDissolverTest {
    JtsextGeometryFactory fact = null;
    
	@Before
	public void setup()
	{
	    fact=new JtsextGeometryFactory();
	}

	@Test
	public void lineNormalization() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktB = "LINESTRING(20 10, 20 40, 10 40, 10 10)";
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    
	    Collection<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveDissolver dissolver=new CompoundCurveDissolver();
		dissolver.dissolve(segs);
		segs=dissolver.getDissolved();
		System.out.println(segs);
		assertEquals("[COMPOUNDCURVE ((10 10, 10 40, 20 40, 20 10))]", segs.toString());
	}
	@Test
	public void ringCounterClockwiseNormalization() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktB = "LINESTRING(10 10, 20 10, 20 40, 10 40, 10 10)";
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    
	    Collection<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveDissolver dissolver=new CompoundCurveDissolver();
		dissolver.dissolve(segs);
		segs=dissolver.getDissolved();
		//System.out.println(segs);
		assertEquals("[COMPOUNDCURVE ((10 10, 10 40, 20 40, 20 10, 10 10))]", segs.toString());
	}
	@Test
	public void ringClockwiseNormalization() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktB = "LINESTRING(10 40, 20 40, 20 10, 10 10, 10 40)";
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    
	    Collection<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveDissolver dissolver=new CompoundCurveDissolver();
		dissolver.dissolve(segs);
		segs=dissolver.getDissolved();
		System.out.println(segs);
		assertEquals("[COMPOUNDCURVE ((10 10, 10 40, 20 40, 20 10, 10 10))]", segs.toString());
	}
	@Test
	public void duplicateLine() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktA = "LINESTRING (10 10, 10 40, 20 40, 20 10)";
	    CompoundCurve A = fact.createCompoundCurve((LineString)wktRdr.read(wktA));
	    String wktB = "LINESTRING(20 10, 20 40, 10 40, 10 10)";
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    
	    Collection<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(A);
	    segs.add(B);
		CompoundCurveDissolver dissolver=new CompoundCurveDissolver();
		dissolver.dissolve(segs);
		segs=dissolver.getDissolved();
		System.out.println(segs);
		assertEquals("[COMPOUNDCURVE ((10 10, 10 40, 20 40, 20 10))]", segs.toString());
	}
	@Test
	public void duplicateRing() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktA = "LINESTRING(10 10, 20 10, 20 40, 10 40, 10 10)";
	    CompoundCurve A = fact.createCompoundCurve((LineString)wktRdr.read(wktA));
	    String wktB = "LINESTRING (20 10, 10 10, 10 40, 20 40, 20 10)";
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    
	    Collection<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(A);
	    segs.add(B);
		CompoundCurveDissolver dissolver=new CompoundCurveDissolver();
		dissolver.dissolve(segs);
		segs=dissolver.getDissolved();
		System.out.println(segs);
		assertEquals("[COMPOUNDCURVE ((10 10, 10 40, 20 40, 20 10, 10 10))]", segs.toString());
	}
	@Test
	public void duplicateArc() throws ParseException, IoxException {
	    ArrayList<CurveSegment> l=new ArrayList<CurveSegment>();
	    l.add(new StraightSegment(new Coordinate(10, 10),new Coordinate(20,10)));
	    l.add(new ArcSegment(new Coordinate(20, 10),new Coordinate(21, 11),new Coordinate(28,10)));
	    l.add(new StraightSegment(new Coordinate(28, 10),new Coordinate(40,10)));
	    CompoundCurve A = fact.createCompoundCurve(l);
	    
	    l=new ArrayList<CurveSegment>();
	    l.add(new StraightSegment(new Coordinate(40, 10),new Coordinate(28,10)));
	    l.add(new ArcSegment(new Coordinate(28, 10),new Coordinate(24, 12),new Coordinate(20,10)));
	    l.add(new StraightSegment(new Coordinate(20, 10),new Coordinate(10,10)));
	    CompoundCurve B = fact.createCompoundCurve(l);
	    
	    Collection<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(A);
	    segs.add(B);
		CompoundCurveDissolver dissolver=new CompoundCurveDissolver();
		dissolver.dissolve(segs);
		segs=dissolver.getDissolved();
		System.out.println(segs);
		assertEquals("[COMPOUNDCURVE ((10 10, 20 10), CIRCULARSTRING (20 10, 21 11, 28 10), (28 10, 40 10))]", segs.toString());
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

	    Collection<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
	    segs.add(A);
		CompoundCurveDissolver dissolver=new CompoundCurveDissolver();
		dissolver.dissolve(segs);
		segs=dissolver.getDissolved();
		System.out.println(segs);
		assertEquals("[COMPOUNDCURVE ((110 110, 110 140, 120 140, 120 110), CIRCULARSTRING (120 110, 115 108, 110 110)), COMPOUNDCURVE ((110 110, 112 120, 115 120, 115 115, 110 110))]", segs.toString());
		
	}

}
