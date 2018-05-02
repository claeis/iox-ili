package ch.interlis.iom_j.itf.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxException;
import ch.interlis.iom_j.itf.impl.IoxAssert;

public class ItfSurfaceLinetable2PolygonTest {

	
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
			String refAttr, String geomAttr, String mainObjectTid, IomObject polyline) {
		IomObject ret=new ch.interlis.iom_j.Iom_jObject(tableName,lineObjectTid);
		ret.addattrobj(geomAttr,polyline);
		IomObject ref=new ch.interlis.iom_j.Iom_jObject("REF",null);
		ref.setobjectrefoid(mainObjectTid);
		ret.addattrobj(refAttr, ref);
		return ret;
	}
	// Test1.TopicA.TableA_Form oid 1 {_itf_ref_TableA -> 10 REF {}, _itf_geom_TableA POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 110.0, C2 110.0}]}}}

	@Test
	public void testSimpleLine() throws IoxException {
		String refAttr="_itf_ref_TableA";
		String geomAttr="_itf_geom_TableA";
		String tableName="Test1.TopicA.TableA_Form";
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(refAttr,geomAttr);
		IomObject polyline=newPolyline();
		addCoord(polyline,110,110);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}",polygon.toString());
		
	}
	@Test
	public void test1Rand2Linien() throws IoxException {
		String refAttr="_itf_ref_TableA";
		String geomAttr="_itf_geom_TableA";
		String tableName="Test1.TopicA.TableA_Form";
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(refAttr,geomAttr);
		IomObject polyline=newPolyline();
		addCoord(polyline,110,110);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		builder.addItfLinetableObject(linetableObj);
		polyline=newPolyline();
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}",polygon.toString());
		
	}
	@Test
	public void test2Rand2Linien() throws IoxException {
		String refAttr="_itf_ref_TableA";
		String geomAttr="_itf_geom_TableA";
		String tableName="Test1.TopicA.TableA_Form";
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(refAttr,geomAttr);
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("3",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}, BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 115.0, C2 115.0}, COORD {C1 115.0, C2 120.0}, COORD {C1 112.0, C2 120.0}, COORD {C1 110.0, C2 110.0}]}}}]}}",polygon.toString());
		
	}
	@Test
	public void testZweiPolygone() throws IoxException {
		String refAttr="_itf_ref_TableA";
		String geomAttr="_itf_geom_TableA";
		String tableName="Test1.TopicA.TableA_Form";
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(refAttr,geomAttr);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",tableName,refAttr,geomAttr,"11",polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}",polygon.toString());
		
	}
	@Test
	public void testInnererRand2Linien() throws IoxException {
		String refAttr="_itf_ref_TableA";
		String geomAttr="_itf_geom_TableA";
		String tableName="Test1.TopicA.TableA_Form";
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(refAttr,geomAttr);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}, BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 115.0, C2 115.0}, COORD {C1 115.0, C2 120.0}, COORD {C1 112.0, C2 120.0}, COORD {C1 110.0, C2 110.0}]}}}]}}",polygon.toString());
		
	}
	@Test
	public void testInnererRand2LinienOhneGemeinsamerStart() throws IoxException {
		String refAttr="_itf_ref_TableA";
		String geomAttr="_itf_geom_TableA";
		String tableName="Test1.TopicA.TableA_Form";
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(refAttr,geomAttr);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		IomObject linetableObj=createLinetableObj("1",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		linetableObj=createLinetableObj("2",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}, BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 115.0, C2 115.0}, COORD {C1 115.0, C2 120.0}, COORD {C1 112.0, C2 120.0}, COORD {C1 110.0, C2 110.0}]}}}]}}",polygon.toString());
		
	}
	@Test
	public void testInnererRand1Linie() throws IoxException {
		String refAttr="_itf_ref_TableA";
		String geomAttr="_itf_geom_TableA";
		String tableName="Test1.TopicA.TableA_Form";
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(refAttr,geomAttr);
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
		IomObject linetableObj=createLinetableObj("1",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}, BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 115.0, C2 115.0}, COORD {C1 115.0, C2 120.0}, COORD {C1 112.0, C2 120.0}, COORD {C1 110.0, C2 110.0}]}}}]}}",polygon.toString());
		
	}
	@Test
	public void testGemeinsameRandstrecke() throws IoxException {
		String refAttr="_itf_ref_TableA";
		String geomAttr="_itf_geom_TableA";
		String tableName="Test1.TopicA.TableA_Form";
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(refAttr,geomAttr);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersections", ex.getMessage());
		}
		//IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		
	}
	@Test
	public void testInnererRandSchneidetAeussererRand() throws IoxException {
		String refAttr="_itf_ref_TableA";
		String geomAttr="_itf_geom_TableA";
		String tableName="Test1.TopicA.TableA_Form";
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(refAttr,geomAttr);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,130.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersection", ex.getMessage());
		}
		//IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		
	}
	@Test
	public void testInnererRandZweiGemeinsamePunkte() throws IoxException {
		String refAttr="_itf_ref_TableA";
		String geomAttr="_itf_geom_TableA";
		String tableName="Test1.TopicA.TableA_Form";
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(refAttr,geomAttr);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("multipolygon",ex.getMessage());
		}
		//IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		
	}
	@Test // Problem
	public void testInnererRandZweiGemeinsameEndPunkte() throws IoxException {
		String refAttr="_itf_ref_TableA";
		String geomAttr="_itf_geom_TableA";
		String tableName="Test1.TopicA.TableA_Form";
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(refAttr,geomAttr);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		IomObject linetableObj=createLinetableObj("1a",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("1b",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,120.0,  140.0); 
		linetableObj=createLinetableObj("2a",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2b",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("multipolygon",ex.getMessage());
		}
		//IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		
	}
	@Test
	public void testAeussererRand2Linien() throws IoxException {
		String refAttr="_itf_ref_TableA";
		String geomAttr="_itf_geom_TableA";
		String tableName="Test1.TopicA.TableA_Form";
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(refAttr,geomAttr);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,120.0,  140.0);
		addCoord(polyline,125.0,  140.0); 
		addCoord(polyline,125.0,  145.0); 
		addCoord(polyline,120.0,  145.0); 
		addCoord(polyline,120.0,  140.0);
		linetableObj=createLinetableObj("2",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			//System.out.println(ex.getMessage());
			//IoxAssert.assertStartsWith("Rand nicht geschlossen",ex.getMessage());
			IoxAssert.assertStartsWith("multipolygon",ex.getMessage());
		}
		
	}
	@Test
	public void testAeussererRand2LinienOhneGemeinsamerStart() throws IoxException {
		String refAttr="_itf_ref_TableA";
		String geomAttr="_itf_geom_TableA";
		String tableName="Test1.TopicA.TableA_Form";
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(refAttr,geomAttr);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		IomObject linetableObj=createLinetableObj("1",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,125.0,  140.0); 
		addCoord(polyline,125.0,  145.0); 
		addCoord(polyline,120.0,  145.0); 
		addCoord(polyline,120.0,  140.0);
		addCoord(polyline,125.0,  140.0); 
		linetableObj=createLinetableObj("2",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			//System.out.println(ex.getMessage());
			//IoxAssert.assertStartsWith("Rand nicht geschlossen",ex.getMessage());
			IoxAssert.assertStartsWith("multipolygon",ex.getMessage());
		}
		
	}
	@Test
	public void testRandNichtGeschlossen() throws IoxException {
		String refAttr="_itf_ref_TableA";
		String geomAttr="_itf_geom_TableA";
		String tableName="Test1.TopicA.TableA_Form";
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(refAttr,geomAttr);
		IomObject polyline=newPolyline();
		addCoord(polyline,110,110);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			//System.out.println(ex.getMessage());
			//IoxAssert.assertStartsWith("Rand nicht geschlossen",ex.getMessage());
			IoxAssert.assertStartsWith("dangles",ex.getMessage());
		}
		//IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		
	}

}
