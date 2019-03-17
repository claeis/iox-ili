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
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2663329.76, C2 1229645.57}, COORD {C1 2663338.593, C2 1229645.57}, COORD {C1 2663338.593, C2 1229644.126}, COORD {C1 2663336.501, C2 1229644.484}, ARC {A1 2663333.121, A2 1229644.966, C1 2663329.76, C2 1229645.57}]}}}}}",polygon10.toString());
		IomObject polygon11=builder.getSurfaceObject(mainObj11);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2663332.691, C2 1229643.889}, ARC {A1 2663334.54, A2 1229644.547, C1 2663335.5347424787, C2 1229644.607058301}, ARC {A1 2663336.0177125637, A2 1229644.5442832266, C1 2663336.501, C2 1229644.484}, COORD {C1 2663338.593, C2 1229644.126}, COORD {C1 2663338.593, C2 1229643.889}, COORD {C1 2663332.691, C2 1229643.889}]}}}}}",polygon11.toString());
		              
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
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 618785.805, C2 257503.048}, ARC {A1 618785.295, A2 257505.453, C1 618785.994, C2 257507.811}, COORD {C1 618786.047, C2 257507.811}, COORD {C1 618786.047, C2 257502.648}, ARC {A1 618785.922, A2 257502.846, C1 618785.805, C2 257503.048}]}}}}}",polygon10.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 618784.0, C2 257498.0}, COORD {C1 618784.0, C2 257507.811}, COORD {C1 618785.994, C2 257507.811}, ARC {A1 618785.295, A2 257505.453, C1 618785.805, C2 257503.048}, ARC {A1 618788.254, A2 257500.579, C1 618791.639, C2 257499.785}, COORD {C1 618791.639, C2 257498.0}, COORD {C1 618784.0, C2 257498.0}]}}}}}",polygon11.toString());
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
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 619030.058, C2 257869.356}, COORD {C1 619030.058, C2 257869.994}, ARC {A1 619031.1, A2 257870.757, C1 619031.5390297333, C2 257871.8718006256}, COORD {C1 619031.547, C2 257871.968}, COORD {C1 619031.385, C2 257869.356}, COORD {C1 619030.058, C2 257869.356}]}}}}}",polygon10.toString());
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
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 605384.196, C2 259724.372}, COORD {C1 605414.858, C2 259724.372}, COORD {C1 605414.858, C2 259714.759}, COORD {C1 605389.486, C2 259717.436}, ARC {A1 605385.346, A2 259719.764, C1 605384.196, C2 259724.372}]}}}}}",polygon10.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 605374.443, C2 259714.759}, COORD {C1 605374.443, C2 259719.026}, COORD {C1 605389.486, C2 259717.436}, COORD {C1 605414.858, C2 259714.759}, COORD {C1 605374.443, C2 259714.759}]}}}}}",polygon11.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 605374.443, C2 259719.026}, COORD {C1 605374.443, C2 259724.372}, COORD {C1 605384.196, C2 259724.372}, ARC {A1 605385.346, A2 259719.764, C1 605389.486, C2 259717.436}, COORD {C1 605374.443, C2 259719.026}]}}}}}",polygon12.toString());
		              
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
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 610969.663, C2 224506.444}, COORD {C1 610977.91, C2 224447.3}, COORD {C1 610975.063, C2 224453.534}, ARC {A1 610973.439, A2 224465.726, C1 610972.045, C2 224477.946}, ARC {A1 610970.953, A2 224489.355, C1 610970.061, C2 224500.782}, COORD {C1 610969.663, C2 224506.444}]}}}}}",polygon10.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 610967.68, C2 224444.77}, COORD {C1 610970.087, C2 224500.378}, ARC {A1 610970.0868232136, A2 224500.38124136152, C1 610970.0866459147, C2 224500.38448269508}, ARC {A1 610970.0737927004, A2 224500.58323939552, C1 610970.061, C2 224500.782}, ARC {A1 610970.953, A2 224489.355, C1 610972.045, C2 224477.946}, ARC {A1 610973.439, A2 224465.726, C1 610975.063, C2 224453.534}, COORD {C1 610977.91, C2 224447.3}, COORD {C1 610967.68, C2 224444.77}]}}}}}",polygon11.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 610962.445, C2 224499.816}, COORD {C1 610969.663, C2 224506.444}, COORD {C1 610970.061, C2 224500.782}, ARC {A1 610970.0737927004, A2 224500.58323939552, C1 610970.0866459147, C2 224500.38448269508}, ARC {A1 610970.0868232136, A2 224500.38124136152, C1 610970.087, C2 224500.378}, COORD {C1 610962.445, C2 224499.816}]}}}}}",polygon12.toString());
		              
	}
	@Test
	public void testStraightArcTangentialMultiValidOverlap() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr,0.002,3);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,641096.061, 245172.460);
		addCoord(polyline,641085.027, 245162.258);
		IomObject linetableObj=createLinetableObj("8665",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,641096.061, 245172.460);
		addArc(polyline,641095.220, 245171.941,
						641094.243, 245171.796);  // overlap mit 8665
		linetableObj=createLinetableObj("8999",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,641096.061, 245172.460);
		addArc(polyline,641096.514, 245172.993,
						641096.797, 245173.632); // overlap mit 8664
		linetableObj=createLinetableObj("9004",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);

		polyline=newPolyline();
		addCoord(polyline,641100.934, 245176.966);
		addCoord(polyline,641096.061, 245172.460);
		linetableObj=createLinetableObj("8664",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		// Abschlusslinie 1
		polyline=newPolyline();
		addCoord(polyline,641085.027, 245162.258);
		addCoord(polyline,641094.243, 245171.796);
		linetableObj=createLinetableObj("a1",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		// Abschlusslinie 2
		polyline=newPolyline();
		addCoord(polyline,641094.243, 245171.796);
		addCoord(polyline,641096.797, 245173.632);
		linetableObj=createLinetableObj("a2",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);

		// Abschlusslinie 3
		polyline=newPolyline();
		addCoord(polyline,641096.797, 245173.632);
		addCoord(polyline,641100.934, 245176.966);
		linetableObj=createLinetableObj("a3",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);

		
		String mainObj10="p1";
		builder.addGeoRef(mainObj10, newCoord(641094.000, 245171.000));
		String mainObj11="p2";
		builder.addGeoRef(mainObj11, newCoord(641096.000, 245173.000));
		String mainObj12="p3";
		builder.addGeoRef(mainObj12, newCoord(641097.000, 245173.500));
		builder.buildSurfaces();
		IomObject polygon10=builder.getSurfaceObject(mainObj10);
		IomObject polygon11=builder.getSurfaceObject(mainObj11);
		IomObject polygon12=builder.getSurfaceObject(mainObj12);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 641085.027, C2 245162.258}, COORD {C1 641094.243, C2 245171.796}, ARC {A1 641095.22, A2 245171.941, C1 641095.9677911773, C2 245172.37654330902}, COORD {C1 641096.061, C2 245172.46}, COORD {C1 641085.027, C2 245162.258}]}}}}}",polygon10.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 641094.243, C2 245171.796}, COORD {C1 641096.797, C2 245173.632}, ARC {A1 641096.514, A2 245172.993, C1 641096.1862836669, C2 245172.57857218612}, COORD {C1 641096.061, C2 245172.46}, COORD {C1 641095.9677911773, C2 245172.37654330902}, ARC {A1 641095.22, A2 245171.941, C1 641094.243, C2 245171.796}]}}}}}",polygon11.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 641096.061, C2 245172.46}, COORD {C1 641096.1862836669, C2 245172.57857218612}, ARC {A1 641096.514, A2 245172.993, C1 641096.797, C2 245173.632}, COORD {C1 641100.934, C2 245176.966}, COORD {C1 641096.061, C2 245172.46}]}}}}}",polygon12.toString());
		              
	}

}
