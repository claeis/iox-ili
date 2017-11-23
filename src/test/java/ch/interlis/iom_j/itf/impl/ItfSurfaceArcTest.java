package ch.interlis.iom_j.itf.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxException;

public class ItfSurfaceArcTest {

	
	private void addArc(IomObject polyline,double xa, double ya,double x, double y){
		IomObject sequence=polyline.getattrobj("sequence",0);
		IomObject ret=new ch.interlis.iom_j.Iom_jObject("ARC",null);
		ret.setattrvalue("C1", Double.toString(x));
		ret.setattrvalue("C2", Double.toString(y));
		ret.setattrvalue("A1", Double.toString(xa));
		ret.setattrvalue("A2", Double.toString(ya));
		sequence.addattrobj("segment",ret);
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
		addArc(polyline,115.0,  108.0,120.0,  110.0); 
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
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, ARC {A1 115.0, A2 108.0, C1 110.0, C2 110.0}]}}}}}"
				,polygon.toString());
		
	}

}
