package ch.interlis.iom_j.itf.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxException;
import ch.interlis.iom_j.itf.impl.IoxAssert;

public class ItfSurfaceLinetable2PolygonOverlapsTest {

	
	private void addCoord(IomObject polyline,double x, double y){
		IomObject sequence=polyline.getattrobj("sequence",0);
		IomObject ret=new ch.interlis.iom_j.Iom_jObject("COORD",null);
		ret.setattrvalue("C1", Double.toString(x));
		ret.setattrvalue("C2", Double.toString(y));
		sequence.addattrobj("segment",ret);
	}
	private void addArc(IomObject polyline,double ax, double ay,double x, double y){
		IomObject sequence=polyline.getattrobj("sequence",0);
		IomObject ret=new ch.interlis.iom_j.Iom_jObject("ARC",null);
		ret.setattrvalue("C1", Double.toString(x));
		ret.setattrvalue("C2", Double.toString(y));
		ret.setattrvalue("A1", Double.toString(ax));
		ret.setattrvalue("A2", Double.toString(ay));
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
	public void testSimpleLineOhneOverlaps() throws IoxException {
		String refAttr="_itf_ref_TableA";
		String geomAttr="_itf_geom_TableA";
		String tableName="Test1.TopicA.TableA_Form";
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(refAttr,geomAttr,0.1,0.001);
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  120.0);
		addArc  (polyline,121.0,  117.0 ,125.0,  115.0);
		
		addCoord(polyline,125.0,  135.0);
		addArc  (polyline,121.0,  133.0 ,120.0,  130.0);
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 130.0}, ARC {A1 121.0, A2 133.0, C1 125.0, C2 135.0}, COORD {C1 125.0, C2 115.0}, ARC {A1 121.0, A2 117.0, C1 120.0, C2 120.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}",polygon.toString());
		
	}
	@Test
	public void testSimpleLineMitOverlaps() throws IoxException {
		String refAttr="_itf_ref_TableA";
		String geomAttr="_itf_geom_TableA";
		String tableName="Test1.TopicA.TableA_Form";
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(refAttr,geomAttr,0.1,0.001);
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  120.0);
		addArc  (polyline,120.99,  116.0 ,124.99,  114.0);
		
		addCoord(polyline,125.0,  135.0);
		addArc  (polyline,120.99,  134.0 ,119.99,  131.0);
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",tableName,refAttr,geomAttr,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		//EhiLogger.getInstance().setTraceFilter(false);
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 119.99, C2 131.0}, COORD {C1 121.98958463592862, C2 134.76494889987706}, ARC {A1 123.47042933431564, A2 135.19450319710128, C1 125.0, C2 135.0}, COORD {C1 124.99, C2 114.0}, ARC {A1 123.41282964438186, A2 114.26933535900471, C1 121.99540012764511, C2 115.01156270378142}, COORD {C1 120.0, C2 120.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}",polygon.toString());
		
	}

}
