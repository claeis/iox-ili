package ch.interlis.iom_j.itf.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vividsolutions.jts.geom.TopologyException;

import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxException;

public class ItfAreaLinetable2PolygonTest {

	private static final String geomAttr="_itf_geom_TableA";
	private static final String tableName="Test1.TopicA.TableA_Form";
	
	private IomObject newCoord(double x, double y){
		IomObject ret=new ch.interlis.iom_j.Iom_jObject("COORD",null);
		ret.setattrvalue("C1", Double.toString(x));
		ret.setattrvalue("C2", Double.toString(y));
		return ret;
	}
	private void addCoord(IomObject polyline,double x, double y){
		IomObject sequence=polyline.getattrobj("sequence",0);
		IomObject ret=new ch.interlis.iom_j.Iom_jObject("COORD",null);
		ret.setattrvalue("C1", Double.toString(x));
		ret.setattrvalue("C2", Double.toString(y));
		sequence.addattrobj("segment",ret);
	}
	private IomObject newPolyline()
	{
		IomObject ret=new ch.interlis.iom_j.Iom_jObject("POLYLINE",null);
		IomObject sequence=new ch.interlis.iom_j.Iom_jObject("SEGMENTS",null);
		ret.addattrobj("sequence",sequence);
		return ret;
	}
	private IomObject createLinetableObj(String lineObjectTid,String tableName,
			String geomAttr, IomObject polyline) {
		IomObject ret=new ch.interlis.iom_j.Iom_jObject(tableName,lineObjectTid);
		ret.addattrobj(geomAttr,polyline);
		return ret;
	}
	// Test1.TopicA.TableA_Form oid 1 {_itf_geom_TableA POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 110.0, C2 110.0}]}}}

	@Test
	public void testSimpleLine() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		IomObject polyline=newPolyline();
		addCoord(polyline,110,110);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		builder.addGeoRef(mainObjectTid, newCoord(115,115));
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}",polygon.toString());
		              
	}
	@Test
	public void test1Rand2Linien() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		IomObject polyline=newPolyline();
		addCoord(polyline,110,110);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		polyline=newPolyline();
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		builder.addGeoRef(mainObjectTid, newCoord(115,115));
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}",polygon.toString());
	}
	@Test
	public void test2Rand3Linien() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("3",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		builder.addGeoRef(mainObjectTid, newCoord(119,111));
		builder.addGeoRef("20", newCoord(114,119));
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary ["
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}, "
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 115.0, C2 115.0}, COORD {C1 115.0, C2 120.0}, COORD {C1 112.0, C2 120.0}, COORD {C1 110.0, C2 110.0}]}}}"
				+ "]}}",polygon.toString());
	}
	@Test
	public void testZweiAneinanderliegendePolygone() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		
		
		IomObject polyline=newPolyline();
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,120.0,  140.0); 
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,120.0,  110.0);
		addCoord(polyline,150.0,  110.0); 
		addCoord(polyline,150.0,  140.0); 
		addCoord(polyline,120.0,  140.0); 
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);

		polyline=newPolyline();
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		linetableObj=createLinetableObj("3",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		
		String mainObjectTid="10";
		builder.addGeoRef(mainObjectTid, newCoord(119,111));
		String mainObject2Tid="20";
		builder.addGeoRef(mainObject2Tid, newCoord(121,111));
		
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		IomObject polygon2=builder.getSurfaceObject(mainObject2Tid);
		//System.out.println(polygon2);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}",polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 120.0, C2 110.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 150.0, C2 140.0}, COORD {C1 150.0, C2 110.0}, COORD {C1 120.0, C2 110.0}]}}}}}",polygon2.toString());
		
	}
	@Test
	public void testZweiGeschachteltePolygone() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObjectTid="10";
		builder.addGeoRef(mainObjectTid, newCoord(119,111));
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObject2Tid="20";
		builder.addGeoRef(mainObject2Tid, newCoord(114,119));
		
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		IomObject polygon2=builder.getSurfaceObject(mainObject2Tid);
		assertEquals("MULTISURFACE {surface SURFACE {boundary ["
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}, "
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 115.0, C2 115.0}, COORD {C1 115.0, C2 120.0}, COORD {C1 112.0, C2 120.0}, COORD {C1 110.0, C2 110.0}]}}}"
				+ "]}}",polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary "
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 112.0, C2 120.0}, COORD {C1 115.0, C2 120.0}, COORD {C1 115.0, C2 115.0}, COORD {C1 110.0, C2 110.0}]}}}"
				+ "}}",polygon2.toString());
	}
	@Test
	public void test2Rand2Linien() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);

		builder.addGeoRef(mainObjectTid, newCoord(119,111));
		builder.addGeoRef("20", newCoord(114,119));
		
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary ["
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}, "
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 115.0, C2 115.0}, COORD {C1 115.0, C2 120.0}, COORD {C1 112.0, C2 120.0}, COORD {C1 110.0, C2 110.0}]}}}"
				+ "]}}",polygon.toString());
	}
	@Test
	public void test2Rand1Linie() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		builder.addGeoRef(mainObjectTid, newCoord(119,111));
		builder.addGeoRef("20", newCoord(114,119));
		
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary ["
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}, "
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 115.0, C2 115.0}, COORD {C1 115.0, C2 120.0}, COORD {C1 112.0, C2 120.0}, COORD {C1 110.0, C2 110.0}]}}}"
				+ "]}}",polygon.toString());
		
	}
	@Test
	public void testFlaecheOhneRef() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);

		builder.addGeoRef(mainObjectTid, newCoord(119,111));
		
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("no polygon-ref to polygon",ex.getMessage());
		}
	}
	@Test
	public void testDoppelteRandstreckeZweiGetrenntePolygone() throws IoxException {
		// gemeinsamer Rand 120,110 -> 120,140
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObjectTid="10";
		builder.addGeoRef(mainObjectTid, newCoord(119,111));
		
		polyline=newPolyline();
		addCoord(polyline,120.0,  110.0);
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,150.0,  140.0); 
		addCoord(polyline,150.0,  110.0); 
		addCoord(polyline,120.0,  110.0);
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObject2Tid="20";
		builder.addGeoRef(mainObject2Tid, newCoord(121,111));
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersection",ex.getMessage());
		}
	}
	@Test
	public void testDoppelteTeilRandstreckeZweiGetrenntePolygone() throws IoxException {
		// gemeinsamer Rand 120,111 -> 120,139
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  111.0); 
		addCoord(polyline,120.0,  139.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObjectTid="10";
		builder.addGeoRef(mainObjectTid, newCoord(119,111));
		
		polyline=newPolyline();
		addCoord(polyline,120.0,  110.0);
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,150.0,  140.0); 
		addCoord(polyline,150.0,  110.0); 
		addCoord(polyline,120.0,  110.0);
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObject2Tid="20";
		builder.addGeoRef(mainObject2Tid, newCoord(121,111));
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersection",ex.getMessage());
		}
	}
	@Test
	public void testDoppelteRandstreckeEinPolygon() throws IoxException {
		// gemeinsame Randstrecke zwischen aeusserem und innerem Rand
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		builder.addGeoRef(mainObjectTid, newCoord(119,139));
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersection",ex.getMessage());
		//}catch(IoxException ex){
		//	assertTrue(ex.getMessage().startsWith("side location conflict"));
		}
		//IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		
	}
	@Test
	public void testDoppelteTeilRandstreckeEinPolygon() throws IoxException {
		// gemeinsame Randstrecke zwischen aeusserem und innerem Rand
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,111.0,  110.0);
		addCoord(polyline,119.0,  110.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,111.0,  110.0);
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		builder.addGeoRef(mainObjectTid, newCoord(119,139));
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersection",ex.getMessage());
		//}catch(IoxException ex){
		//	assertTrue(ex.getMessage().startsWith("side location conflict"));
		}
		//IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		
	}
	@Test
	public void testInnererRandSchneidetAeussererRand() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,130.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);

		builder.addGeoRef(mainObjectTid, newCoord(119,139));
		
		try{
			builder.buildSurfaces();
			fail(); 
			// muss fehler liefern, dass 2 Flaechen schneiden
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersection",ex.getMessage());
		}
	}
	@Test
	public void testInnererRandZweiGemeinsamePunkte() throws IoxException {
		String geomAttr="_itf_geom_TableA";
		String tableName="Test1.TopicA.TableA_Form";
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);

		builder.addGeoRef(mainObjectTid, newCoord(119,111));
		builder.addGeoRef("11", newCoord(114,115));

		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
		}
		//IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		
	}
	@Test
	public void testRandNichtGeschlossen() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		builder.addGeoRef(mainObjectTid, newCoord(119,139));
		
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("dangles",ex.getMessage());
		}
		//IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		
	}

}
