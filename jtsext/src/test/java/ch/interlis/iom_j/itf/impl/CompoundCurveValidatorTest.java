package ch.interlis.iom_j.itf.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurveRing;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;
import ch.interlis.iom_j.itf.impl.jtsext.noding.CompoundCurveNoder;
import ch.interlis.iom_j.itf.impl.jtsext.noding.Intersection;
import ch.interlis.iox.IoxException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.noding.BasicSegmentString;
import com.vividsolutions.jts.noding.SegmentString;

public class CompoundCurveValidatorTest {
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
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		validator.checkValid();
	}
	@Test
	public void Line1simpleArc() throws ParseException, IoxException {

	    ArrayList<CurveSegment> l=new ArrayList<CurveSegment>();
	    l.add(new ArcSegment(new Coordinate(10, 10),new Coordinate(15, 8),new Coordinate(20,10)));
	    l.add(new StraightSegment(new Coordinate(20, 10),new Coordinate(20,40)));
	    l.add(new StraightSegment(new Coordinate(20, 40),new Coordinate(10,40)));
	    l.add(new StraightSegment(new Coordinate(10, 40),new Coordinate(10,10)));
	    CompoundCurve B = fact.createCompoundCurve(l);
	    
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		validator.checkValid();
	}
	@Test
	public void Line2commonSegment() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktA = "LINESTRING(50 10, 20 10, 20 40, 50 40)";
	    String wktB = "LINESTRING(10 10, 20 10, 20 40, 10 40, 10 10)";
	    CompoundCurve A = fact.createCompoundCurve((LineString)wktRdr.read(wktA));
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(A);
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		try{
			validator.checkValid();
			fail();
		}catch(RuntimeException ex){
			assertTrue(ex.getMessage().startsWith("intersection"));
		}
	    
	}
	@Test
	public void Line2commonSegmentArc() throws ParseException, IoxException {

	    ArrayList<CurveSegment> l=new ArrayList<CurveSegment>();
	    l.add(new StraightSegment(new Coordinate(50, 10),new Coordinate(20,10)));
	    l.add(new ArcSegment(new Coordinate(20, 10),new Coordinate(25, 25),new Coordinate(20,40)));
	    l.add(new StraightSegment(new Coordinate(20, 40),new Coordinate(50,40)));
	    CompoundCurve A = fact.createCompoundCurve(l);
	    
	    l=new ArrayList<CurveSegment>();
	    l.add(new StraightSegment(new Coordinate(10, 10),new Coordinate(20,10)));
	    l.add(new ArcSegment(new Coordinate(20, 10),new Coordinate(25, 25),new Coordinate(20,40)));
	    l.add(new StraightSegment(new Coordinate(20, 40),new Coordinate(10,40)));
	    l.add(new StraightSegment(new Coordinate(10, 40),new Coordinate(10,10)));
	    CompoundCurve B = fact.createCompoundCurve(l);
	    
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(A);
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		try{
			validator.checkValid();
			fail();
		}catch(RuntimeException ex){
			assertTrue(ex.getMessage().startsWith("intersection"));
		}
	    
	}
	@Test
	public void Line1openRing() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktB = "LINESTRING(10 10, 20 10, 20 40, 10 40)";
	    
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		validator.checkValid();
		assertFalse(B.isClosed());
	}
	@Test
	public void Line1openRingArc() throws ParseException, IoxException  {
	    ArrayList<CurveSegment> l = new ArrayList<CurveSegment>();
	    l.add(new StraightSegment(new Coordinate(10, 10),new Coordinate(20,10)));
	    l.add(new ArcSegment(new Coordinate(20, 10),new Coordinate(25, 25),new Coordinate(20,40)));
	    l.add(new StraightSegment(new Coordinate(20, 40),new Coordinate(10,40)));
	    CompoundCurve B = fact.createCompoundCurve(l);
	    
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		validator.checkValid();
		assertFalse(B.isClosed());
	}
	@Test
	public void Line1endptOnPt() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktB = "LINESTRING(0 10, 10 10, 20 10, 20 40, 10 40, 10 10)";
	    
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		try{
			validator.checkValid();
			fail();
		}catch(RuntimeException ex){
			assertTrue(ex.getMessage().startsWith("intersection"));
		}
	}
	@Test
	public void Line1endptOnPtArc() throws ParseException, IoxException {
	    
	    ArrayList<CurveSegment> l = new ArrayList<CurveSegment>();
	    l.add(new ArcSegment(new Coordinate(0, 10),new Coordinate(5, 13),new Coordinate(10,10)));
	    l.add(new StraightSegment(new Coordinate(10, 10),new Coordinate(20,10)));
	    l.add(new StraightSegment(new Coordinate(20, 10),new Coordinate(20,40)));
	    l.add(new StraightSegment(new Coordinate(20, 40),new Coordinate(10,40)));
	    l.add(new StraightSegment(new Coordinate(10, 40),new Coordinate(10,10)));
	    CompoundCurve B = fact.createCompoundCurve(l);
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		try{
			validator.checkValid();
			fail();
		}catch(RuntimeException ex){
			assertTrue(ex.getMessage().startsWith("intersection"));
		}
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
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		try{
			validator.checkValid();
			fail();
		}catch(RuntimeException ex){
			assertTrue(ex.getMessage().startsWith("intersection"));
		}
	}
	@Test
	public void Line2endptOnPtArc() throws ParseException, IoxException {
	    
	    ArrayList<CurveSegment> l = new ArrayList<CurveSegment>();
	    l.add(new ArcSegment(new Coordinate(0, 10),new Coordinate(5, 13),new Coordinate(10,10)));
	    l.add(new StraightSegment(new Coordinate(10, 10),new Coordinate(20,10)));
	    CompoundCurve B = fact.createCompoundCurve(l);

	    l = new ArrayList<CurveSegment>();
	    l.add(new StraightSegment(new Coordinate(20, 40),new Coordinate(10,40)));
	    l.add(new StraightSegment(new Coordinate(10, 40),new Coordinate(10,10)));
	    CompoundCurve A = fact.createCompoundCurve(l);
	    
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(A);
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		try{
			validator.checkValid();
			fail();
		}catch(RuntimeException ex){
			assertTrue(ex.getMessage().startsWith("intersection"));
		}
	}
	@Test
	public void Line1endptOnLine() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktB = "LINESTRING(10 10, 20 10, 20 40, 10 40, 15 10)";
	    
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		try{
			validator.checkValid();
			fail();
		}catch(RuntimeException ex){
			assertTrue(ex.getMessage().startsWith("intersection"));
		}
	}
	@Test
	public void Line1endptOnLineArc() throws ParseException, IoxException {
	    
	    ArrayList<CurveSegment> l = new ArrayList<CurveSegment>();
	    l.add(new ArcSegment(new Coordinate(12, 10),new Coordinate(16, 12),new Coordinate(20,10)));
	    l.add(new StraightSegment(new Coordinate(20, 10),new Coordinate(20,40)));
	    l.add(new StraightSegment(new Coordinate(20, 40),new Coordinate(10,40)));
	    l.add(new StraightSegment(new Coordinate(10, 40),new Coordinate(13,11)));
	    CompoundCurve B = fact.createCompoundCurve(l);
	    
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		try{
			validator.checkValid();
			fail();
		}catch(RuntimeException ex){
			assertTrue(ex.getMessage().startsWith("intersection"));
		}
	}
	@Test
	public void Line2endptOnLine() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktA = "LINESTRING(10 10, 20 10, 20 40)";
	    String wktB = "LINESTRING(10 40, 15 10)";
	    
	    CompoundCurve A = fact.createCompoundCurve((LineString)wktRdr.read(wktA));
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(A);
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		try{
			validator.checkValid();
			fail();
		}catch(RuntimeException ex){
			assertTrue(ex.getMessage().startsWith("intersection"));
		}
	}
	@Test
	public void Line2endptOnLineArc() throws ParseException, IoxException {

	    ArrayList<CurveSegment> l = new ArrayList<CurveSegment>();
	    l.add(new ArcSegment(new Coordinate(12, 10),new Coordinate(16, 12),new Coordinate(20,10)));
	    l.add(new StraightSegment(new Coordinate(20, 10),new Coordinate(20,40)));
	    CompoundCurve A = fact.createCompoundCurve(l);
	    
	    l = new ArrayList<CurveSegment>();
	    l.add(new StraightSegment(new Coordinate(10, 40),new Coordinate(13,11)));
	    CompoundCurve B = fact.createCompoundCurve(l);
	    
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(A);
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
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
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		try{
			validator.checkValid();
			fail();
		}catch(RuntimeException ex){
			assertTrue(ex.getMessage().startsWith("intersection"));
		}
	}
	@Test
	public void Line1twoRingArc() throws ParseException, IoxException {
		
	    ArrayList<CurveSegment> l = new ArrayList<CurveSegment>();
	    l.add(new StraightSegment(new Coordinate(20, 10),new Coordinate(15,40)));
	    l.add(new StraightSegment(new Coordinate(15, 40),new Coordinate(10,10)));
	    l.add(new StraightSegment(new Coordinate(10, 10),new Coordinate(15, 0)));
	    l.add(new ArcSegment(new Coordinate(15,  0),new Coordinate(18, 9),new Coordinate(20,10)));
	    l.add(new ArcSegment(new Coordinate(20, 10),new Coordinate(25,39),new Coordinate(25,40)));
	    l.add(new StraightSegment(new Coordinate(25, 40),new Coordinate(30,10)));
	    l.add(new StraightSegment(new Coordinate(30, 10),new Coordinate(25, 0)));
	    l.add(new StraightSegment(new Coordinate(25,  0),new Coordinate(20,10)));
	    CompoundCurve B = fact.createCompoundCurve(l);
	    
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		try{
			validator.checkValid();
			fail();
		}catch(RuntimeException ex){
			assertTrue(ex.getMessage().startsWith("intersection"));
		}
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
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		validator.checkValid();
	}
	@Test
	public void Line2twoRingArc() throws ParseException, IoxException {
	    
	    ArrayList<CurveSegment> l = new ArrayList<CurveSegment>();
	    l.add(new StraightSegment(new Coordinate(20, 10),new Coordinate(15,40)));
	    l.add(new StraightSegment(new Coordinate(15, 40),new Coordinate(10,10)));
	    l.add(new StraightSegment(new Coordinate(10, 10),new Coordinate(15, 0)));
	    l.add(new ArcSegment(new Coordinate(15,  0),new Coordinate(18, 9),new Coordinate(20,10)));
	    CompoundCurve A = fact.createCompoundCurve(l);
	    
	    l = new ArrayList<CurveSegment>();
	    l.add(new ArcSegment(new Coordinate(20, 10),new Coordinate(25,39),new Coordinate(25,40)));
	    l.add(new StraightSegment(new Coordinate(25, 40),new Coordinate(30,10)));
	    l.add(new StraightSegment(new Coordinate(30, 10),new Coordinate(25, 0)));
	    l.add(new StraightSegment(new Coordinate(25,  0),new Coordinate(20,10)));
	    CompoundCurve B = fact.createCompoundCurve(l);
	    
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(A);
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		validator.checkValid();
	}
	@Test
	public void Line1innerRing() throws ParseException {
	    WKTReader wktRdr = new WKTReader(fact);

	    String wktB = "LINESTRING(20 10, 15 30, 15 10, 20 10, 20 40, 10 40, 10 0, 20 0, 20 10)";
	    
	    CompoundCurve B = fact.createCompoundCurve((LineString)wktRdr.read(wktB));
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		try{
			validator.checkValid();
			fail();
		}catch(RuntimeException ex){
			assertTrue(ex.getMessage().startsWith("intersection"));
		}
	}
	@Test
	public void Line1innerRingArc() throws ParseException, IoxException {
	    
	    ArrayList<CurveSegment> l = new ArrayList<CurveSegment>();
	    l.add(new ArcSegment(new Coordinate(20, 10),new Coordinate(19, 12),new Coordinate(15,30)));
	    l.add(new StraightSegment(new Coordinate(15, 30),new Coordinate(15,10)));
	    l.add(new StraightSegment(new Coordinate(15, 10),new Coordinate(20,10)));
	    l.add(new StraightSegment(new Coordinate(20, 10),new Coordinate(20,40)));
	    l.add(new StraightSegment(new Coordinate(20, 40),new Coordinate(10,40)));
	    l.add(new StraightSegment(new Coordinate(10, 40),new Coordinate(10, 0)));
	    l.add(new StraightSegment(new Coordinate(10,  0),new Coordinate(20, 0)));
	    l.add(new StraightSegment(new Coordinate(20,  0),new Coordinate(20,10)));
	    CompoundCurve B = fact.createCompoundCurve(l);
	    
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		try{
			validator.checkValid();
			fail();
		}catch(RuntimeException ex){
			assertTrue(ex.getMessage().startsWith("intersection"));
		}
	}
	@Test
	public void Line1Eye() throws ParseException, IoxException {
	    
	    ArrayList<CurveSegment> l = new ArrayList<CurveSegment>();
	    l.add(new ArcSegment(new Coordinate(0, 0),new Coordinate(5, 5),new Coordinate(10,0)));
	    l.add(new StraightSegment(new Coordinate(10, 0),new Coordinate(0,0)));
	    CompoundCurve B = fact.createCompoundCurve(l);
	    
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		assertTrue(validator.isValid());
	}
	@Test
	public void Line1FalseEye() throws ParseException, IoxException {
	    
	    ArrayList<CurveSegment> l = new ArrayList<CurveSegment>();
	    l.add(new StraightSegment(new Coordinate(0, 0),new Coordinate(10,0)));
	    l.add(new StraightSegment(new Coordinate(10, 0),new Coordinate(0,0)));
	    CompoundCurve B = fact.createCompoundCurve(l);
	    
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(B);
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		assertTrue(!validator.isValid());
	}
	@Test
	public void Line2Eye() throws ParseException, IoxException {
	    
	    ArrayList<CurveSegment> l1 = new ArrayList<CurveSegment>();
	    l1.add(new ArcSegment(new Coordinate(0, 0),new Coordinate(5, 5),new Coordinate(10,0)));
	    ArrayList<CurveSegment> l2 = new ArrayList<CurveSegment>();
	    l2.add(new StraightSegment(new Coordinate(10, 0),new Coordinate(0,0)));
	    List<CompoundCurve> segs=new ArrayList<CompoundCurve>();
	    segs.add(fact.createCompoundCurve(l1));
	    segs.add(fact.createCompoundCurve(l2));
		CompoundCurveNoder validator=new CompoundCurveNoder(segs,true);
		assertTrue(validator.isValid());
	}
	
}
