package ch.interlis.iom_j.itf.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vividsolutions.jts.geom.Polygon;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.jts.Iox2jts;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class ItfAreaLinetable2PolygonOverlapsTest {

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
			String geomAttr, IomObject polyline) {
		IomObject ret=new ch.interlis.iom_j.Iom_jObject(tableName,lineObjectTid);
		ret.addattrobj(geomAttr,polyline);
		return ret;
	}

	@Test
	public void testArcArcValidOverlap() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr,0.05,3);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,2663329.760, 1229645.570);
		addArc(polyline,2663333.121, 1229644.966,2663336.501, 1229644.484);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,2663336.501, 1229644.484);
		addCoord(polyline,2663338.593, 1229644.126);
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,2663332.691, 1229643.889);
		addArc(polyline,2663334.540, 1229644.547,2663336.501, 1229644.484);
		linetableObj=createLinetableObj("3",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		// Abschlusslinie 1
		polyline=newPolyline();
		addCoord(polyline,2663329.760, 1229645.570);
		addCoord(polyline,2663338.593, 1229645.570);
		addCoord(polyline,2663338.593, 1229644.126);
		linetableObj=createLinetableObj("4",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		// Abschlusslinie 1
		polyline=newPolyline();
		addCoord(polyline,2663332.691, 1229643.889);
		addCoord(polyline,2663338.593, 1229643.889);
		addCoord(polyline,2663338.593, 1229644.126);
		linetableObj=createLinetableObj("5",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);

		
		String mainObj10="10";
		builder.addGeoRef(mainObj10, newCoord(2663338.000, 1229645.000));
		String mainObj11="11";
		builder.addGeoRef(mainObj11, newCoord(2663338.000, 1229644.000));
		builder.buildSurfaces();
		IomObject polygon10=builder.getSurfaceObject(mainObj10);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2663329.76, C2 1229645.57}, COORD {C1 2663338.593, C2 1229645.57}, COORD {C1 2663338.593, C2 1229644.126}, COORD {C1 2663336.501, C2 1229644.484}, ARC {A2 1229644.966, A1 2663333.121, C1 2663329.76, C2 1229645.57}]}}}}}",polygon10.toString());
		IomObject polygon11=builder.getSurfaceObject(mainObj11);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2663332.691, C2 1229643.889}, ARC {A2 1229644.547, A1 2663334.54, C1 2663335.5347424787, C2 1229644.607058301}, ARC {A2 1229644.5442832266, A1 2663336.0177125637, C1 2663336.501, C2 1229644.484}, COORD {C1 2663338.593, C2 1229644.126}, COORD {C1 2663338.593, C2 1229643.889}, COORD {C1 2663332.691, C2 1229643.889}]}}}}}",polygon11.toString());
		              
	}
	@Test
	public void testArcArcNoOverlap() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr,0.002,3);
		
		IomObject polyline=newPolyline();
		addCoord(polyline, 618785.994, 257507.811);
		addArc(polyline,618785.295, 257505.453,618785.805, 257503.048);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,618785.805, 257503.048); 
		addArc(polyline,618785.922, 257502.846,618786.047, 257502.648);
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,618785.805, 257503.048);
		addArc(polyline,618788.254, 257500.579,618791.639, 257499.785);
		linetableObj=createLinetableObj("3",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		// Abschlusslinie 1
		polyline=newPolyline();
		addCoord(polyline,618785.994, 257507.811);
		addCoord(polyline,618786.047, 257507.811);
		addCoord(polyline,618786.047, 257502.648);
		linetableObj=createLinetableObj("4",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		// Abschlusslinie 2
		polyline=newPolyline();
		addCoord(polyline,618786.047, 257502.648);
		addCoord(polyline,618791.639, 257502.648);
		addCoord(polyline,618791.639, 257499.785);
		linetableObj=createLinetableObj("5",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		// Abschlusslinie 3
		polyline=newPolyline();
		addCoord(polyline,618791.639, 257499.785);
		addCoord(polyline,618791.639, 257498.000);
		addCoord(polyline,618784.000, 257498.000);
		addCoord(polyline,618784.000, 257507.811);
		addCoord(polyline,618785.994, 257507.811);
		linetableObj=createLinetableObj("5",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		String mainObj10="10";
		builder.addGeoRef(mainObj10, newCoord(618786.010, 257504.000));
		String mainObj11="11";
		builder.addGeoRef(mainObj11, newCoord(618791.000, 257501.000));
		String mainObj12="12";
		builder.addGeoRef(mainObj11, newCoord(618785.000, 257506.000));
		builder.buildSurfaces();
		IomObject polygon10=builder.getSurfaceObject(mainObj10);
		IomObject polygon11=builder.getSurfaceObject(mainObj11);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 618785.805, C2 257503.048}, ARC {A2 257505.453, A1 618785.295, C1 618785.994, C2 257507.811}, COORD {C1 618786.047, C2 257507.811}, COORD {C1 618786.047, C2 257502.648}, ARC {A2 257502.846, A1 618785.922, C1 618785.805, C2 257503.048}]}}}}}",polygon10.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 618784.0, C2 257498.0}, COORD {C1 618784.0, C2 257507.811}, COORD {C1 618785.994, C2 257507.811}, ARC {A2 257505.453, A1 618785.295, C1 618785.805, C2 257503.048}, ARC {A2 257500.579, A1 618788.254, C1 618791.639, C2 257499.785}, COORD {C1 618791.639, C2 257498.0}, COORD {C1 618784.0, C2 257498.0}]}}}}}",polygon11.toString());
	}
	@Test
	public void testArcStraightValidOverlap() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr,0.002,3);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,619031.547, 257871.968);
		addArc(polyline,619031.100, 257870.757, 619030.058, 257869.994);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,619031.385, 257869.356);
		addCoord(polyline, 619031.547, 257871.968);
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline, 619031.547, 257871.968);
		addCoord(polyline, 619031.670, 257873.948);
		linetableObj=createLinetableObj("3",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		// Abschlusslinie 1
		polyline=newPolyline();
		addCoord(polyline,619030.058, 257869.994);
		addCoord(polyline,619030.058, 257869.356);
		addCoord(polyline,619031.385, 257869.356);
		linetableObj=createLinetableObj("4",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		// Abschlusslinie 2
		polyline=newPolyline();
		addCoord(polyline,619031.385, 257869.356);
		addCoord(polyline,619031.670, 257869.356);
		addCoord(polyline, 619031.670, 257873.948);
		linetableObj=createLinetableObj("5",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);

		
		String mainObj10="10";
		builder.addGeoRef(mainObj10, newCoord(619031.000, 257869.800));
		String mainObj11="11";
		builder.addGeoRef(mainObj11, newCoord(619031.500, 257870.000));
		builder.buildSurfaces();
		IomObject polygon10=builder.getSurfaceObject(mainObj10);
		System.out.println(polygon10);
		IomObject polygon11=builder.getSurfaceObject(mainObj11);
		System.out.println(polygon11);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 619030.058, C2 257869.356}, COORD {C1 619030.058, C2 257869.994}, ARC {A2 257870.757, A1 619031.1, C1 619031.5390297333, C2 257871.8718006256}, COORD {C1 619031.547, C2 257871.968}, COORD {C1 619031.385, C2 257869.356}, COORD {C1 619030.058, C2 257869.356}]}}}}}",polygon10.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 619031.385, C2 257869.356}, COORD {C1 619031.547, C2 257871.968}, COORD {C1 619031.67, C2 257873.948}, COORD {C1 619031.67, C2 257869.356}, COORD {C1 619031.385, C2 257869.356}]}}}}}",polygon11.toString());
		              
	}
	@Test
	public void testArcStraightTangentialNoOverlap() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr,0.002,3);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,605384.196, 259724.372);
		addArc(polyline,605385.346, 259719.764, 605389.486, 259717.436);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,605389.486, 259717.436);
		addCoord(polyline,605414.858, 259714.759);
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,605389.486, 259717.436); // tangente zu kreisbogen
		addCoord(polyline,605374.443, 259719.026);
		linetableObj=createLinetableObj("3",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		// Abschlusslinie 1
		polyline=newPolyline();
		addCoord(polyline,605384.196, 259724.372);
		addCoord(polyline,605414.858, 259724.372);
		addCoord(polyline,605414.858, 259714.759);
		linetableObj=createLinetableObj("4",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		// Abschlusslinie 2
		polyline=newPolyline();
		addCoord(polyline,605414.858, 259714.759);
		addCoord(polyline,605374.443, 259714.759);
		addCoord(polyline,605374.443, 259719.026);
		linetableObj=createLinetableObj("5",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);

		// Abschlusslinie 3
		polyline=newPolyline();
		addCoord(polyline,605374.443, 259719.026);
		addCoord(polyline,605374.443, 259724.372);
		addCoord(polyline,605384.196, 259724.372);
		linetableObj=createLinetableObj("6",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		String mainObj10="10";
		builder.addGeoRef(mainObj10, newCoord(605414.000, 259724.000));
		String mainObj11="11";
		builder.addGeoRef(mainObj11, newCoord(605375.000, 259715.000));
		String mainObj12="12";
		builder.addGeoRef(mainObj12, newCoord(605375.000, 259720.000));
		builder.buildSurfaces();
		IomObject polygon10=builder.getSurfaceObject(mainObj10);
		IomObject polygon11=builder.getSurfaceObject(mainObj11);
		IomObject polygon12=builder.getSurfaceObject(mainObj12);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 605384.196, C2 259724.372}, COORD {C1 605414.858, C2 259724.372}, COORD {C1 605414.858, C2 259714.759}, COORD {C1 605389.486, C2 259717.436}, ARC {A2 259719.764, A1 605385.346, C1 605384.196, C2 259724.372}]}}}}}",polygon10.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 605374.443, C2 259714.759}, COORD {C1 605374.443, C2 259719.026}, COORD {C1 605389.486, C2 259717.436}, COORD {C1 605414.858, C2 259714.759}, COORD {C1 605374.443, C2 259714.759}]}}}}}",polygon11.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 605374.443, C2 259719.026}, COORD {C1 605374.443, C2 259724.372}, COORD {C1 605384.196, C2 259724.372}, ARC {A2 259719.764, A1 605385.346, C1 605389.486, C2 259717.436}, COORD {C1 605374.443, C2 259719.026}]}}}}}",polygon12.toString());
		              
	}
	@Test
	public void testArcArcTangentialValidOverlap() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr,0.002,3);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,610970.061, 224500.782);
		addCoord(polyline,610969.663 ,224506.444);
		IomObject linetableObj=createLinetableObj("136",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,610970.087, 224500.378);
		addArc(polyline,610970.075, 224500.580, // overlap mit zweiten bogen von 4
						610970.061, 224500.782);
		linetableObj=createLinetableObj("139",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,610962.445, 224499.816);
		addCoord(polyline,610970.087, 224500.378);
		linetableObj=createLinetableObj("140",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);

		polyline=newPolyline();
		addCoord(polyline,610977.910, 224447.300);
		addCoord(polyline,610975.063, 224453.534);
		addArc(polyline,610973.439, 224465.726,
						610972.045, 224477.946);
		addArc(polyline,610970.953, 224489.355,  
						610970.061, 224500.782);
		linetableObj=createLinetableObj("155",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		// Abschlusslinie 1
		polyline=newPolyline();
		addCoord(polyline,610969.663, 224506.444);
		addCoord(polyline,610977.910, 224447.300);
		linetableObj=createLinetableObj("100",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		// Abschlusslinie 2
		polyline=newPolyline();
		addCoord(polyline,610977.910, 224447.300);
		addCoord(polyline,610967.680, 224444.770);
		linetableObj=createLinetableObj("200",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);

		// Abschlusslinie 3
		polyline=newPolyline();
		addCoord(polyline,610967.680, 224444.770);
		addCoord(polyline,610970.087, 224500.378);
		linetableObj=createLinetableObj("300",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);

		// Abschlusslinie 4
		polyline=newPolyline();
		addCoord(polyline,610962.445, 224499.816);
		addCoord(polyline,610969.663, 224506.444);
		linetableObj=createLinetableObj("500",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		String mainObj10="100x";
		builder.addGeoRef(mainObj10, newCoord(610976.000, 224453.000));
		String mainObj11="300x";
		builder.addGeoRef(mainObj11, newCoord(610968.000, 224445.000));
		String mainObj12="500x";
		builder.addGeoRef(mainObj12, newCoord(610969.000, 224505.000));
		builder.buildSurfaces();
		IomObject polygon10=builder.getSurfaceObject(mainObj10);
		IomObject polygon11=builder.getSurfaceObject(mainObj11);
		IomObject polygon12=builder.getSurfaceObject(mainObj12);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 610969.663, C2 224506.444}, COORD {C1 610977.91, C2 224447.3}, COORD {C1 610975.063, C2 224453.534}, ARC {A2 224465.726, A1 610973.439, C1 610972.045, C2 224477.946}, ARC {A2 224489.355, A1 610970.953, C1 610970.061, C2 224500.782}, COORD {C1 610969.663, C2 224506.444}]}}}}}",polygon10.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 610967.68, C2 224444.77}, COORD {C1 610970.087, C2 224500.378}, ARC {A2 224500.38124136152, A1 610970.0868232136, C1 610970.0866459147, C2 224500.38448269508}, ARC {A2 224500.58323939552, A1 610970.0737927004, C1 610970.061, C2 224500.782}, ARC {A2 224489.355, A1 610970.953, C1 610972.045, C2 224477.946}, ARC {A2 224465.726, A1 610973.439, C1 610975.063, C2 224453.534}, COORD {C1 610977.91, C2 224447.3}, COORD {C1 610967.68, C2 224444.77}]}}}}}",polygon11.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 610962.445, C2 224499.816}, COORD {C1 610969.663, C2 224506.444}, COORD {C1 610970.061, C2 224500.782}, ARC {A2 224500.58323939552, A1 610970.0737927004, C1 610970.0866459147, C2 224500.38448269508}, ARC {A2 224500.38124136152, A1 610970.0868232136, C1 610970.087, C2 224500.378}, COORD {C1 610962.445, C2 224499.816}]}}}}}",polygon12.toString());
		              
	}

}
