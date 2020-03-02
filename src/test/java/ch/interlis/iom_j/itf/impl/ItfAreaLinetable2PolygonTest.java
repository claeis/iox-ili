package ch.interlis.iom_j.itf.impl;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;
import com.vividsolutions.jts.geom.Polygon;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.IoxInvalidDataException;
import ch.interlis.iox_j.jts.Iox2jts;
import ch.interlis.iox_j.jts.Iox2jtsException;

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
	// Test1.TopicA.TableA_Form oid 1 {_itf_geom_TableA POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 110.0, C2 110.0}]}}}

	// prueft ob eine polygon mit einem Rand erstellt werden kann.
	@Test
	public void test_1Polygon_1Rand_SimpleLine_Ok() throws IoxException {
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
	
	// prueft ob die dangles der polygon erkannt werden und eine Fehlermeldung ausgegeben wird.
	@Test
	public void test_1Polygon_1Rand_DanglesLine_Fail() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		IomObject polyline=newPolyline();
		addCoord(polyline,100.0,100.0);
		addCoord(polyline,400.0,100.0); 
		addCoord(polyline,400.0,400.0); 
		addCoord(polyline,100.0,400.0); 
		addCoord(polyline,100.0,100.0);
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,400.0,100.0);
		addCoord(polyline,300.0,200.0); 
		IomObject linetableObj2=createLinetableObj("2",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj2);
		builder.addGeoRef(mainObjectTid, newCoord(240,240));
		//System.out.println(polygon);
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("dangles",ex.getMessage());
		}
	}
	
	// prueft ob eine polygon mit einem Aussen Rand und 2 polylines erstellt werden kann.
	@Test
	public void test_1Polygon_1Rand2Linien_Ok() throws IoxException {
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
	
	// prueft ob eine Polygon mit einem aeusseren und einem inneren Rand erstellt werden kann.
	@Test
	public void test_1Polygon_2Rand3Linien_Ok() throws IoxException {
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

	// prueft ob eine Polygon mit 2 Randlinien und jeweils 1 polyline erstellt werden kann.
	@Test
	public void test_1Polygon_2Rand2Linien_Ok() throws IoxException {
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
		Polygon jtsPolygon;
		try {
			jtsPolygon = Iox2jts.surface2JTS(polygon, 0.0);
			//System.out.println(jtsPolygon.toText());
		} catch (Iox2jtsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary ["
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}, "
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 115.0, C2 115.0}, COORD {C1 115.0, C2 120.0}, COORD {C1 112.0, C2 120.0}, COORD {C1 110.0, C2 110.0}]}}}"
				+ "]}}",polygon.toString());
	}
	
	// prueft ob eine Polygon mit 2 Randlinien und 1 polyline erstellt werden kann.
	@Test
	public void test_1Polygon_2Rand1Linie_Ok() throws IoxException {
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
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn fuer eine Polygon keine Referenz erstellt wurde.
	@Test
	public void test_1Polygon_FlaecheOhneRef_Fail() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		builder.setAllowItfAreaHoles(false);
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
		
		String lineTid="2";
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj(lineTid,tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		builder.addGeoRef(mainObjectTid, newCoord(119,111));
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("no area-ref to polygon of lines "+lineTid,ex.getMessage());
		}
	}

	// prueft ob eine Fehlermeldung ausgegeben wird, wenn sich 2 Linien aus unterschiedlichen Randlinien ueberlagern.
	@Test
	public void test_1Polygon_2Randstrecken_Aufeinander_Fail() throws IoxException {
		// gemeinsame Randstrecke zwischen aeusserem und innerem Rand
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		// outer boundary
		IomObject polyline=newPolyline();
		{
			addCoord(polyline,100.0,  100.0); // B1: 1
			addCoord(polyline,400.0,  100.0); // B1: 2
			addCoord(polyline,400.0,  400.0); // B1: 3
			addCoord(polyline,100.0,  400.0); // B1: 4
			addCoord(polyline,100.0,  100.0); // B1: 5
			IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// inner boundary
		IomObject innerpolyline=newPolyline();
		{
			addCoord(innerpolyline,100.0,  100.0); // B2: 1
			addCoord(innerpolyline,100.0,  400.0); // B2: 2
			addCoord(innerpolyline, 20.0,  400.0); // B2: 3
			addCoord(innerpolyline, 20.0,  100.0); // B2: 4
			addCoord(innerpolyline,100.0,  100.0); // B2: 5
			IomObject linetableObj=createLinetableObj("2",tableName,geomAttr,innerpolyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// polygon reference point
		builder.addGeoRef("o1", newCoord(114,119));
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
			ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
            assertEquals(1, errs.size());
			assertEquals("Overlay coord1 (100.000, 100.000), coord2 (100.000, 400.000), tids 1, 2", errs.get(0).getLocalizedMessage());
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn der innere Rand einer Polygon,
	// den aeusseren Rand der Polygon ueberschneidet.
	@Test
	public void test_1Polygon_InnererRandUeberSchneidetAeussererRand_Fail() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		// outer boundary
		IomObject polyline=newPolyline();
		{
			addCoord(polyline,100.0,  100.0); // B1: 1
			addCoord(polyline,400.0,  100.0); // B1: 2
			addCoord(polyline,400.0,  400.0); // B1: 3
			addCoord(polyline,100.0,  400.0); // B1: 4
			addCoord(polyline,100.0,  100.0); // B1: 5
			IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// inner boundary
		IomObject innerpolyline=newPolyline();
		{
			addCoord(innerpolyline,160.0,  300.0); // B2: 1
			addCoord(innerpolyline,340.0,  300.0); // B2: 2
			addCoord(innerpolyline,340.0,  480.0); // B2: 3
			addCoord(innerpolyline,160.0,  480.0); // B2: 4
			addCoord(innerpolyline,160.0,  300.0); // B2: 5
			IomObject linetableObj=createLinetableObj("2",tableName,geomAttr,innerpolyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// polygon reference point
		builder.addGeoRef("o1", newCoord(240,240));
		try{
			builder.buildSurfaces();
			fail(); 
			// muss fehler liefern, dass 2 Flaechen schneiden
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
			ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
            assertEquals(2, errs.size());
			assertEquals("Intersection coord1 (340.000, 400.000), tids 1, 2", errs.get(0).getLocalizedMessage());
			assertEquals("Intersection coord1 (160.000, 400.000), tids 1, 2", errs.get(1).getLocalizedMessage());
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn ein Punkt des inneren Randes,
	// eine Linie des aeusseren Randes schneidet und 1 Linie des inneren Randes,
	// auf einer Linie des aeusseren Randes liegt.
	@Test
	public void test_1Polygon_PunktVonInnererRandSchneidetpolyline_Fail() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		// outer boundary
		IomObject polyline=newPolyline();
		{
			addCoord(polyline,100.0,  100.0); // B1: 1
			addCoord(polyline,400.0,  100.0); // B1: 2
			addCoord(polyline,400.0,  400.0); // B1: 3
			addCoord(polyline,100.0,  400.0); // B1: 4
			addCoord(polyline,100.0,  100.0); // B1: 5
			IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// inner boundary
		IomObject innerpolyline=newPolyline();
		{
			addCoord(innerpolyline,100.0,  300.0); // B2: 1
			addCoord(innerpolyline,100.0,  100.0); // B2: 2
			addCoord(innerpolyline,200.0,  200.0); // B2: 3
			addCoord(innerpolyline,100.0,  300.0); // B2: 4
			IomObject linetableObj=createLinetableObj("2",tableName,geomAttr,innerpolyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// polygon reference point
		builder.addGeoRef("o1", newCoord(240,240));
		try{
			builder.buildSurfaces();
			fail(); 
			// muss fehler liefern, overlay und intersection
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
			ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
            assertEquals(1, errs.size());
			assertEquals("Overlay coord1 (100.000, 100.000), coord2 (100.000, 300.000), tids 1, 2", errs.get(0).getLocalizedMessage());
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn sich 2 innere Raender ueberschneiden.
	@Test
	public void test_1Polygon_2RandlinienAufeinander_InnerRandNichtValidRing_Fail() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		// outer boundary
		IomObject polyline=newPolyline();
		{
			addCoord(polyline,100.0,  100.0); // B1: 1
			addCoord(polyline,400.0,  100.0); // B1: 2
			addCoord(polyline,400.0,  400.0); // B1: 3
			addCoord(polyline,100.0,  400.0); // B1: 4
			addCoord(polyline,100.0,  100.0); // B1: 5
			IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// inner boundary
		IomObject innerpolyline=newPolyline();
		{
			addCoord(innerpolyline,100.0,  300.0); // B2: 1
			addCoord(innerpolyline,100.0,  100.0); // B2: 2
			addCoord(innerpolyline,200.0,  200.0); // B2: 3
			addCoord(innerpolyline,140.0,  300.0); // B2: 4
			IomObject linetableObj=createLinetableObj("2",tableName,geomAttr,innerpolyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// polygon reference point
		builder.addGeoRef("o1", newCoord(240,240));
		try{
			builder.buildSurfaces();
			fail(); 
			// muss fehler: overlay liefern und nicht dangles.
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
			ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
            assertEquals(1, errs.size());
			assertEquals("Overlay coord1 (100.000, 100.000), coord2 (100.000, 300.000), tids 1, 2", errs.get(0).getLocalizedMessage());
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn der innere Rand ueber dem
	// aeusseren Rand auf einer Linie liegt, der innere Rand nicht vollstaendig
	// geschlossen wird und 2 dangle Linien existieren.
	@Test
	public void test_1Polygon_2RandlinienAufeinander_2DangleLinien_Fail() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		// outer boundary
		IomObject polyline=newPolyline();
		{
			addCoord(polyline,100.0,  100.0); // B1: 1
			addCoord(polyline,400.0,  100.0); // B1: 2
			addCoord(polyline,400.0,  400.0); // B1: 3
			addCoord(polyline,100.0,  400.0); // B1: 4
			addCoord(polyline,100.0,  100.0); // B1: 5
			IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// inner boundary
		IomObject innerpolyline=newPolyline();
		{
			addCoord(innerpolyline,200.0,  320.0); // B2: 1
			addCoord(innerpolyline,100.0,  400.0); // B2: 2
			addCoord(innerpolyline,100.0,  100.0); // B2: 3
			addCoord(innerpolyline,200.0,  220.0); // B2: 4
			IomObject linetableObj=createLinetableObj("2",tableName,geomAttr,innerpolyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		builder.addGeoRef("o1", newCoord(240,240));
		try{
			builder.buildSurfaces();
			fail(); 
			// muss fehler: overlay liefern und keine dangles.
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
			ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
            assertEquals(1, errs.size());
			assertEquals("Overlay coord1 (100.000, 100.000), coord2 (100.000, 400.000), tids 1, 2", errs.get(0).getLocalizedMessage());
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn 1 Linie die Polygon
	// in 2 Haelften spaltet. Diese wird doppelt erstellt.
	@Test
	public void test_1Polygon_2Randlinien_1LinieSpaltetPolygon_Fail() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		// outer boundary
		IomObject polyline=newPolyline();
		{
			addCoord(polyline,100.0,  100.0); // B1: 1
			addCoord(polyline,400.0,  100.0); // B1: 2
			addCoord(polyline,400.0,  400.0); // B1: 3
			addCoord(polyline,100.0,  400.0); // B1: 4
			addCoord(polyline,100.0,  100.0); // B1: 5
			IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// inner boundary
		IomObject innerpolyline=newPolyline();
		{
			addCoord(innerpolyline,200.0,  220.0); // B2: 1
			addCoord(innerpolyline,300.0,  220.0); // B2: 2
			addCoord(innerpolyline,300.0,  360.0); // B2: 3
			addCoord(innerpolyline,260.0,  360.0); // B2: 4
			addCoord(innerpolyline,260.0,  400.0); // B2: 5
			addCoord(innerpolyline,260.0,  360.0); // B2: 6
			addCoord(innerpolyline,200.0,  360.0); // B2: 7
			addCoord(innerpolyline,200.0,  220.0); // B2: 8
			IomObject linetableObj=createLinetableObj("2",tableName,geomAttr,innerpolyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// polygon reference point
		builder.addGeoRef("o1", newCoord(240,240));
		try{
			builder.buildSurfaces();
			fail(); 
			// muss fehler: overlay liefern
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
			ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
            assertEquals(2, errs.size());
			assertEquals("Intersection coord1 (260.000, 400.000), tids 1, 2:1", errs.get(0).getLocalizedMessage());
			assertEquals("Overlay coord1 (260.000, 360.000), coord2 (260.000, 400.000), tids 2:1, 2:1", errs.get(1).getLocalizedMessage());
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn keine Referenz auf das Polygon
	// erstellt wurde.
	@Test
	public void test_1Polygon_InnererRandZweiGemeinsamePunkte_Fail() throws IoxException {
		String geomAttr="_itf_geom_TableA";
		String tableName="Test1.TopicA.TableA_Form";
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		builder.setAllowItfAreaHoles(false);
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
			IoxAssert.assertStartsWith("no area-ref",ex.getMessage());
			//IoxAssert.assertStartsWith("intersections",ex.getMessage());
		}
		//IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn der aeussere Rand nicht geschlossen wurde.
	@Test
	public void test_1Polygon_RandNichtGeschlossen_Fail() throws IoxException {
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
	
	// prueft ob ein Polygon erstellt werden kann, wenn 1 Punkt des inneren Randes,
	// einen Punkt des aeusseren Randes beruehrt.
	@Test
	public void test_1Polygon_InnererRandBeruehrtAussenrandAnEinerStelle_Fail() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		// outer boundary
		IomObject polyline=newPolyline();
		{	
			addCoord(polyline,120.0,  80.0); // B1: 1
			addCoord(polyline,400.0,  80.0); // B1: 2
			addCoord(polyline,260.0,  400.0); // B1: 3
			addCoord(polyline,120.0,  80.0); // B1: 4
			IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// inner boundary
		IomObject innerpolyline=newPolyline();
		{
			addCoord(innerpolyline,260.0,  80.0); // B2: 1
			addCoord(innerpolyline,320.0,  200.0); // B2: 2
			addCoord(innerpolyline,200.0,  200.0); // B2: 3
			addCoord(innerpolyline,260.0,  80.0); // B2: 4
			IomObject linetableObj=createLinetableObj("2",tableName,geomAttr,innerpolyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// polygon reference point
		builder.addGeoRef("o1", newCoord(260,240));
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
			ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
            assertEquals(1, errs.size());
			assertEquals("Intersection coord1 (260.000, 80.000), tids 1, 2", errs.get(0).getLocalizedMessage());
		}
	}
	
	// Es soll getestet werden, ob eine Intersection Fehlermeldung ausgegeben wird,
	// wenn 2 gerade Linien von je einem inneren Rand, uebereinander liegen.
	@Test
	public void test_1Polygon_ZweiLinienVonJe2InnerenRaenderLiegen_Aufeinander_Fail() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		// outer boundary
		IomObject polyline=newPolyline();
		{
			addCoord(polyline,120.0,  120.0); // B1: 1
			addCoord(polyline,320.0,  120.0); // B1: 2
			addCoord(polyline,220.0,  380.0); // B1: 3
			addCoord(polyline,120.0,  120.0); // B1: 4
			IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// inner boundary
		IomObject innerpolyline=newPolyline();
		{
			addCoord(innerpolyline,200.0,  240.0); // B2: 1
			addCoord(innerpolyline,240.0,  240.0); // B2: 2
			addCoord(innerpolyline,220.0,  320.0); // B2: 3
			addCoord(innerpolyline,200.0,  240.0); // B2: 4
			IomObject linetableObj=createLinetableObj("2",tableName,geomAttr,innerpolyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// inner boundary 2
		IomObject innerpolyline2=newPolyline();
		{
			addCoord(innerpolyline2,200.0,  240.0); // B3: 1
			addCoord(innerpolyline2,240.0,  240.0); // B3: 2
			addCoord(innerpolyline2,220.0,  160.0); // B3: 3
			addCoord(innerpolyline2,200.0,  240.0); // B3: 4
			IomObject linetableObj=createLinetableObj("3",tableName,geomAttr,innerpolyline2);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// polygon reference point
		builder.addGeoRef("o1", newCoord(114,119));
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
			ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
            assertEquals(1, errs.size());
			assertEquals("Overlay coord1 (200.000, 240.000), coord2 (240.000, 240.000), tids 2, 3", errs.get(0).getLocalizedMessage());
		}
	}
	
	// Es wird getestet, ob eine Polygon erstellt werden kann,
	// wenn die 2 inneren Randlinien einander ueberschneiden.
	@Test
	public void test_1Polygon_2InnereRaenderUeberschneidenSich_Fail() {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		// outer boundary
		IomObject polyline=newPolyline();
		{
			addCoord(polyline,120.0,  120.0); // B1: 1
			addCoord(polyline,320.0,  120.0); // B1: 2
			addCoord(polyline,220.0,  380.0); // B1: 3
			addCoord(polyline,120.0,  120.0); // B1: 4
			IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// inner boundary
		IomObject innerpolyline=newPolyline();
		{
			addCoord(innerpolyline,200.0,  240.0); // B2: 1
			addCoord(innerpolyline,240.0,  240.0); // B2: 2
			addCoord(innerpolyline,220.0,  320.0); // B2: 3
			addCoord(innerpolyline,200.0,  240.0); // B2: 4
			IomObject linetableObj=createLinetableObj("2",tableName,geomAttr,innerpolyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// inner boundary 2
		IomObject innerpolyline2=newPolyline();
		{
			addCoord(innerpolyline2,200.0,  180.0); // B3: 1
			addCoord(innerpolyline2,240.0,  180.0); // B3: 2
			addCoord(innerpolyline2,220.0,  260.0); // B3: 3
			addCoord(innerpolyline2,200.0,  180.0); // B3: 4
			IomObject linetableObj=createLinetableObj("3",tableName,geomAttr,innerpolyline2);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// polygon reference point
		builder.addGeoRef("o1", newCoord(114,119));
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
			ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
            assertEquals(2, errs.size());
			assertEquals("Intersection coord1 (225.000, 240.000), tids 2, 3", errs.get(0).getLocalizedMessage());
			assertEquals("Intersection coord1 (215.000, 240.000), tids 2, 3", errs.get(1).getLocalizedMessage());
		}
	}
	
	// Es wird getestet, ob eine Polygon erstellt werden kann,
	// wenn die 2 inneren Raender einander an einem Punkt beruehren.
	@Test
	public void test_1Polygon_2InnereRaenderBeruehrenSichAn1Punkt_Ok() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		// outer boundary
		IomObject polyline=newPolyline();
		{
			addCoord(polyline,120.0,  120.0); // B1: 1
			addCoord(polyline,320.0,  120.0); // B1: 2
			addCoord(polyline,220.0,  380.0); // B1: 3
			addCoord(polyline,120.0,  120.0); // B1: 4
			IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// inner boundary
		IomObject innerpolyline=newPolyline();
		{
			addCoord(innerpolyline,200.0,  160.0); // B2: 1
			addCoord(innerpolyline,240.0,  160.0); // B2: 2
			addCoord(innerpolyline,220.0,  200.0); // B2: 3
			addCoord(innerpolyline,200.0,  160.0); // B2: 4
			IomObject linetableObj=createLinetableObj("2",tableName,geomAttr,innerpolyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// inner boundary 2
		IomObject innerpolyline2=newPolyline();
		{
			addCoord(innerpolyline2,220.0,  200.0); // B3: 1
			addCoord(innerpolyline2,240.0,  240.0); // B3: 2
			addCoord(innerpolyline2,200.0,  240.0); // B3: 3
			addCoord(innerpolyline2,220.0,  200.0); // B3: 4
			IomObject linetableObj=createLinetableObj("3",tableName,geomAttr,innerpolyline2);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// polygon reference point
		builder.addGeoRef("o1", newCoord(220,260));
		builder.buildSurfaces();
		// polygon
		IomObject polygon=builder.getSurfaceObject("o1");
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary ["
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 120.0, C2 120.0}, COORD {C1 220.0, C2 380.0}, COORD {C1 320.0, C2 120.0}, COORD {C1 120.0, C2 120.0}]}}}, "
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 200.0, C2 160.0}, COORD {C1 240.0, C2 160.0}, COORD {C1 220.0, C2 200.0}, COORD {C1 200.0, C2 160.0}]}}}, "
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 200.0, C2 240.0}, COORD {C1 220.0, C2 200.0}, COORD {C1 240.0, C2 240.0}, COORD {C1 200.0, C2 240.0}]}}}]}}" 
				,polygon.toString());
	}
	
	// prueft ob 2 Polygone, welche aneinander liegen, erstellt werden koennen.
	@Test
	public void test_2Polygon_ZweiAneinanderliegendePolygone_Ok() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
/*
 MULTISURFACE {surface SURFACE {
	boundary BOUNDARY {
		polyline POLYLINE {sequence SEGMENTS {segment [
			COORD {C1 110.0, C2 110.0}, 
			COORD {C1 110.0, C2 140.0}, 
			COORD {C1 120.0, C2 140.0}, 
			COORD {C1 150.0, C2 140.0}, falscher Stuetzpunkt (zuviel)
			COORD {C1 150.0, C2 110.0}, falscher Stuetzpunkt (zuviel)
			COORD {C1 120.0, C2 110.0}, 
			COORD {C1 110.0, C2 110.0}]}}}}}

MULTISURFACE {surface SURFACE {
	boundary BOUNDARY {
		polyline POLYLINE {sequence SEGMENTS {segment [
			COORD {C1 120.0, C2 110.0}, 
			COORD {C1 120.0, C2 140.0}, 
			COORD {C1 150.0, C2 140.0}, 
			COORD {C1 150.0, C2 110.0}, 
			COORD {C1 120.0, C2 110.0}]}}}}}
	
 */
		
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
	
	// prueft ob 2 Polygone, welche in sich geschachtelt sind, erstellt werden koennen.
	@Test
	public void test_2Polygon_ZweiGeschachteltePolygone_Ok() throws IoxException {
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
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn 2 Polygone
	// (mit je 3 Linien) genau aufeinander liegen.
	@Test
	public void test_2Polygon_ExaktUebereinander_Fail() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,100.0,  100.0);
		addCoord(polyline,400.0,  100.0); 
		addCoord(polyline,400.0,  400.0); 
		addCoord(polyline,100.0,  400.0); 
		addCoord(polyline,100.0,  100.0);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObjectTid="10";
		builder.addGeoRef(mainObjectTid, newCoord(119,111));
		
		polyline=newPolyline();
		addCoord(polyline,100.0,  100.0);
		addCoord(polyline,400.0,  100.0); 
		addCoord(polyline,400.0,  400.0); 
		addCoord(polyline,100.0,  400.0); 
		addCoord(polyline,100.0,  100.0);
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObject2Tid="20";
		builder.addGeoRef(mainObject2Tid, newCoord(121,111));
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
			ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
            assertEquals(3, errs.size());
			assertEquals("Overlay coord1 (100.000, 100.000), coord2 (400.000, 100.000), tids 1, 2", errs.get(0).getLocalizedMessage());
			assertEquals("Overlay coord1 (400.000, 100.000), coord2 (400.000, 400.000), tids 1, 2", errs.get(1).getLocalizedMessage());
			assertEquals("Overlay coord1 (100.000, 400.000), coord2 (400.000, 400.000), tids 1, 2", errs.get(2).getLocalizedMessage());
		}
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn 2 Polygone
	// genau aufeinander liegen und die zweite Polygon,
	// je 1 Punkt pro Linie mehr hat.
	@Test
	public void test_2Polygon_Uebereinander_Polygon1Hat3PunkteMehr_Fail() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,100.0,  100.0); // B1: 1
		addCoord(polyline,260.0,  100.0); // B1: 2
		addCoord(polyline,400.0,  100.0); // B1: 3
		addCoord(polyline,400.0,  260.0); // B1: 4
		addCoord(polyline,400.0,  400.0); // B1: 5
		addCoord(polyline,260.0,  400.0); // B1: 6
		addCoord(polyline,100.0,  400.0); // B1: 7
		addCoord(polyline,120.0,  260.0); // B1: 8
		addCoord(polyline,100.0,  100.0); // B1: 9
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObjectTid="10";
		builder.addGeoRef(mainObjectTid, newCoord(260,260));
		
		IomObject polyline2=newPolyline();
		addCoord(polyline2,100.0,  100.0); // B1: 1
		addCoord(polyline2,400.0,  100.0); // B1: 2
		addCoord(polyline2,400.0,  400.0); // B1: 3
		addCoord(polyline2,100.0,  400.0); // B1: 4
		addCoord(polyline2,100.0,  100.0); // B1: 5
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline2);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObject2Tid="20";
		builder.addGeoRef(mainObject2Tid, newCoord(260,260));
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
			ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
            assertEquals(6, errs.size());
			assertEquals("Overlay coord1 (100.000, 100.000), coord2 (260.000, 100.000), tids 1, 2", errs.get(0).getLocalizedMessage());
			assertEquals("Overlay coord1 (260.000, 100.000), coord2 (400.000, 100.000), tids 1, 2", errs.get(1).getLocalizedMessage());
			assertEquals("Overlay coord1 (400.000, 100.000), coord2 (400.000, 260.000), tids 1, 2", errs.get(2).getLocalizedMessage());
			assertEquals("Overlay coord1 (400.000, 260.000), coord2 (400.000, 400.000), tids 1, 2", errs.get(3).getLocalizedMessage());
			assertEquals("Overlay coord1 (260.000, 400.000), coord2 (400.000, 400.000), tids 1, 2", errs.get(4).getLocalizedMessage());
			assertEquals("Overlay coord1 (100.000, 400.000), coord2 (260.000, 400.000), tids 1, 2", errs.get(5).getLocalizedMessage());
		}
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn 2 Polygone mit je einem Kreisbogen
	// genau uebereinander liegen.
	@Test
	public void test_2Polygon_Je1Kreisbogen_LiegenExaktAufeinander_Fail() {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr,0.05,3);
		
		IomObject polyline=newPolyline();
		addCoord(polyline, 20.0, 160.0);
		addCoord(polyline,240.0, 160.0);
		addArc(polyline, 200.0, 260.0, 240.0, 360.0);
		addCoord(polyline,20.0, 360.0);
		addCoord(polyline,20.0, 160.0);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObj10="10";
		builder.addGeoRef(mainObj10, newCoord(100.0, 260.0));
		
		IomObject polyline2=newPolyline();
		addCoord(polyline2,20.0, 160.0);
		addCoord(polyline2,240.0, 160.0);
		addArc(polyline2, 200.0, 260.0, 240.0, 360.0);
		addCoord(polyline2,20.0, 360.0);
		addCoord(polyline2,20.0, 160.0);
		IomObject linetableObj2=createLinetableObj("2",tableName,geomAttr,polyline2);
		//System.out.println(linetableObj2);
		builder.addItfLinetableObject(linetableObj2);
		String mainObj11="11";
		builder.addGeoRef(mainObj11, newCoord(100.0, 260.0));
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
			ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
			assertEquals(3, errs.size());
			assertEquals("Overlay coord1 (20.000, 160.000), coord2 (240.000, 160.000), tids 1, 2", errs.get(0).getLocalizedMessage());
			assertEquals("Overlay coord1 (240.000, 160.000), coord2 (240.000, 360.000), tids 1, 2", errs.get(1).getLocalizedMessage());
			assertEquals("Overlay coord1 (20.000, 360.000), coord2 (240.000, 360.000), tids 1, 2", errs.get(2).getLocalizedMessage());
		}
	}
	
	// prueft, ob 2 Polygone mit je einem Kreisbogen, welche beide
	// unterschiedliche Kreisbogenpunkte haben, und uebereinander liegen,
	// erstellt werden koennen.
	@Test
	public void test_2Polygon_MitJe1KreisbogenLiegenNichtGenauAufeinander_Fail() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr,0.05,3);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,20.0, 160.0);
		addCoord(polyline,240.0, 160.0);
		addArc(polyline, 180.0, 200.0, 240.0, 360.0);
		addCoord(polyline,20.0, 360.0);
		addCoord(polyline,20.0, 160.0);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObj10="10";
		builder.addGeoRef(mainObj10, newCoord(100.0, 260.0));
		
		IomObject polyline2=newPolyline();
		addCoord(polyline2,20.0, 160.0);
		addCoord(polyline2,240.0, 160.0);
		addArc(polyline2, 160.0, 240.0, 240.0, 360.0);
		addCoord(polyline2,20.0, 360.0);
		addCoord(polyline2,20.0, 160.0);
		IomObject linetableObj2=createLinetableObj("2",tableName,geomAttr,polyline2);
		//System.out.println(linetableObj2);
		builder.addItfLinetableObject(linetableObj2);
		String mainObj11="11";
		builder.addGeoRef(mainObj11, newCoord(100.0, 260.0));
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
			ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
            assertEquals(2, errs.size());
			assertEquals("Overlay coord1 (20.000, 160.000), coord2 (240.000, 160.000), tids 1, 2", errs.get(0).getLocalizedMessage());
			assertEquals("Overlay coord1 (20.000, 360.000), coord2 (240.000, 360.000), tids 1, 2", errs.get(1).getLocalizedMessage());
		}
	}
	
	// 2 Polygone beruehren sich an einer RandLinie. Randlinie 1 hat 2 Punkte welche sich
	// an Randlinie 2 beruehren. Randlinie 2 hat einen Punkt mehr.
	@Test
	public void test_2Polygon_2RandStreckenAufeinander_AnzahlPunkteUnterschiedlich_Fail() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,10.0,   8.0);
		addCoord(polyline,40.0,   8.0); 
		addCoord(polyline,40.0,  40.0);
		addCoord(polyline,10.0,  40.0); 
		addCoord(polyline,10.0,  26.0);
		addCoord(polyline,10.0,   8.0); 
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObjectTid="10";
		builder.addGeoRef(mainObjectTid, newCoord(26,26));
		
		polyline=newPolyline();
		addCoord(polyline,10.0,   8.0);
		addCoord(polyline,10.0,  40.0);
		addCoord(polyline, 2.0,  40.0);
		addCoord(polyline, 2.0,   8.0);
		addCoord(polyline,10.0,   8.0);
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObject2Tid="20";
		builder.addGeoRef(mainObject2Tid, newCoord(6,26));
		// polygon reference point
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
			ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
            assertEquals(2, errs.size());
			assertEquals("Overlay coord1 (10.000, 26.000), coord2 (10.000, 40.000), tids 1, 2", errs.get(0).getLocalizedMessage());
			assertEquals("Overlay coord1 (10.000, 8.000), coord2 (10.000, 26.000), tids 1, 2", errs.get(1).getLocalizedMessage());
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn 2 Randstrecken von 2 Polygonen
	// sich uebereinander befinden.
	@Test
	public void test_2Polygon_2Randstrecken_Aufeinander_Fail() throws IoxException {
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
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
			ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
            assertEquals(1, errs.size());
			assertEquals("Overlay coord1 (120.000, 110.000), coord2 (120.000, 140.000), tids 1, 2", errs.get(0).getLocalizedMessage());
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn eine Linie, aufgeteilt in 2 Linien
	// auf einer Linie einer anderen Polygon erstellt wird.
	@Test
	public void test_2Polygon_2RandstreckenTeile_Aufeinander_Fail() throws IoxException {
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
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
			ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
            assertEquals(2, errs.size());
			assertEquals("Intersection coord1 (120.000, 111.000), tids 1, 2", errs.get(0).getLocalizedMessage());
			assertEquals("Overlay coord1 (120.000, 111.000), coord2 (120.000, 139.000), tids 1, 2", errs.get(1).getLocalizedMessage());
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn Polygon1 den inneren Rand
	// und den aeusseren Rand von Polygon2 ueberschneidet.
	@Test
	public void test_2Polygon_Poligon1Ueberschneidet_InnererRandVonPolygon2_Fail() throws IoxException {
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,160.0,  300.0);
		addCoord(polyline,340.0,  300.0); 
		addCoord(polyline,340.0,  480.0); 
		addCoord(polyline,160.0,  480.0); 
		addCoord(polyline,160.0,  300.0);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObjectTid="10";
		builder.addGeoRef(mainObjectTid, newCoord(119,111));
		
		// aeusserer Rand
		polyline=newPolyline();
		addCoord(polyline,100.0,  100.0);
		addCoord(polyline,400.0,  100.0); 
		addCoord(polyline,400.0,  400.0); 
		addCoord(polyline,100.0,  400.0); 
		addCoord(polyline,100.0,  100.0);
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		// innerer Rand
		polyline=newPolyline();
		addCoord(polyline,200.0,  220.0);
		addCoord(polyline,300.0,  220.0); 
		addCoord(polyline,300.0,  360.0); 
		addCoord(polyline,200.0,  360.0); 
		addCoord(polyline,200.0,  220.0);
		linetableObj=createLinetableObj("3",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObject2Tid="20";
		builder.addGeoRef(mainObject2Tid, newCoord(121,111));
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
			ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
            assertEquals(4, errs.size());
			assertEquals("Intersection coord1 (340.000, 400.000), tids 1, 2", errs.get(0).getLocalizedMessage());
			assertEquals("Intersection coord1 (160.000, 400.000), tids 1, 2", errs.get(1).getLocalizedMessage());
			assertEquals("Intersection coord1 (300.000, 300.000), tids 1, 3", errs.get(2).getLocalizedMessage());
			assertEquals("Intersection coord1 (200.000, 300.000), tids 1, 3", errs.get(3).getLocalizedMessage());
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn ein Punkt einer Polygon
	// eine Randlinie einer anderen Polygon beruehrt.
	@Test
	public void test_2Polygon_PunktAufRand_Fail() throws IoxException {
		// Punkt 120,115 auf Rand 120,110 -> 120,140
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
		addCoord(polyline,150.0,  140.0); 
		addCoord(polyline,150.0,  110.0); 
		addCoord(polyline,120.0,  115.0);
		addCoord(polyline,150.0,  140.0); 
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObject2Tid="20";
		builder.addGeoRef(mainObject2Tid, newCoord(121,115));
		try{
			builder.buildSurfaces();
			fail();
		}catch(IoxException ex){
			IoxAssert.assertStartsWith("intersections",ex.getMessage());
			ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
            assertEquals(1, errs.size());
			assertEquals("Intersection coord1 (120.000, 115.000), tids 1, 2", errs.get(0).getLocalizedMessage());
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn ein Punkt einer Polygon
	// auf einem Punkt einer anderen Polygon liegt.
	@Test
	public void test_2Polygon_PunktAufPunkt_Ok() throws IoxException {
		// Polygone haben gemeinsamen Randpunkt 120,115 
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(geomAttr);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  115.0);
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObjectTid="10";
		builder.addGeoRef(mainObjectTid, newCoord(119,111));
		
		polyline=newPolyline();
		addCoord(polyline,150.0,  140.0); 
		addCoord(polyline,150.0,  110.0); 
		addCoord(polyline,120.0,  115.0);
		addCoord(polyline,150.0,  140.0); 
		linetableObj=createLinetableObj("2",tableName,geomAttr,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		String mainObject2Tid="20";
		builder.addGeoRef(mainObject2Tid, newCoord(121,115));
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		IomObject polygon2=builder.getSurfaceObject(mainObject2Tid);
		//System.out.println(polygon);
		//System.out.println(polygon2);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 115.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}",polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 120.0, C2 115.0}, COORD {C1 150.0, C2 140.0}, COORD {C1 150.0, C2 110.0}, COORD {C1 120.0, C2 115.0}]}}}}}",polygon2.toString());
	}
}