package ch.interlis.iom_j.itf.impl;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.IoxInvalidDataException;
import ch.interlis.iom_j.itf.impl.IoxAssert;

public class ItfSurfaceLinetable2PolygonTest {

	private static final String REFATTR="_itf_ref_TableA";
	private static final String GEOMATTR="_itf_geom_TableA";
	private static final String TABLENAME="Test1.TopicA.TableA_Form";
	
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

	// prueft ob eine polygon mit einem Rand erstellt werden kann.
	@Test
	public void test_1Polygon_1Rand_SimpleLine_Ok() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		IomObject polyline=newPolyline();
		addCoord(polyline,110,110);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}",polygon.toString());            
	}
	
	// prueft ob die dangles der polygon erkannt werden und eine Fehlermeldung ausgegeben wird.
	@Test
	public void test_1Polygon_1Rand_DanglesLine_Fail() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		IomObject polyline=newPolyline();
		addCoord(polyline,100.0,100.0);
		addCoord(polyline,400.0,100.0); 
		addCoord(polyline,400.0,400.0); 
		addCoord(polyline,100.0,400.0); 
		addCoord(polyline,100.0,100.0);
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,400.0,100.0);
		addCoord(polyline,300.0,200.0); 
		IomObject linetableObj2=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj2);
		//System.out.println(polygon);
        builder.buildSurfaces();
        ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
        assertEquals("dangle tid 2", errs.get(0).getLocalizedMessage());
        assertEquals(1, errs.size());
	}
	
	// prueft ob eine polygon mit einem Aussenrand und 2 polylines erstellt werden kann.
	@Test
	public void test_1Polygon_1Rand2Linien_Ok() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0);
		addCoord(polyline,120.0,  140.0);
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		builder.addItfLinetableObject(linetableObj);
		polyline=newPolyline();
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}",polygon.toString());
	}
	
	// prueft ob eine Polygon mit einem aeusseren und einem inneren Rand erstellt werden kann.
	@Test
	public void test_1Polygon_2Rand3Linien_Ok() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("3",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		builder.addItfLinetableObject(linetableObj);
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
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary ["
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}, "
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 115.0, C2 115.0}, COORD {C1 115.0, C2 120.0}, COORD {C1 112.0, C2 120.0}, COORD {C1 110.0, C2 110.0}]}}}"
				+ "]}}",polygon.toString());
	}
	
	// prueft ob eine Polygon mit 2 Randlinien und 1 polyline erstellt werden kann.
	@Test
	public void test_1Polygon_2Rand1Linie_Ok() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
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
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary ["
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}, "
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 115.0, C2 115.0}, COORD {C1 115.0, C2 120.0}, COORD {C1 112.0, C2 120.0}, COORD {C1 110.0, C2 110.0}]}}}"
				+ "]}}",polygon.toString());
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn fuer einen aeusseren Rand keine Referenz erstellt wurde.
	@Test
	public void test_1Polygon_RandlinieOhneRef_Fail() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,null,polyline);
		
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		ArrayList<IoxInvalidDataException> dataErrs=builder.getDataerrs();
        assertEquals(1, dataErrs.size());
        assertEquals("boundary line without reference to main table", dataErrs.get(0).getMessage());
	}

	// prueft ob eine Fehlermeldung ausgegeben wird, wenn sich 2 Linien aus unterschiedlichen Randlinien ueberlagern.
	@Test
	public void test_1Polygon_2Randstrecken_Aufeinander_Fail() throws IoxException {
		// gemeinsame Randstrecke zwischen aeusserem und innerem Rand
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		String mainObjectTid="10";
		// aussenrand
		IomObject polyline=newPolyline();
		{
			addCoord(polyline,100.0,  100.0); // B1: 1
			addCoord(polyline,400.0,  100.0); // B1: 2
			addCoord(polyline,400.0,  400.0); // B1: 3
			addCoord(polyline,100.0,  400.0); // B1: 4
			addCoord(polyline,100.0,  100.0); // B1: 5
			IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// innenrand
		IomObject innerpolyline=newPolyline();
		IomObject linetableObj=null;
		{
			addCoord(innerpolyline,100.0,  100.0); // B2: 1
			addCoord(innerpolyline,100.0,  400.0); // B2: 2
			addCoord(innerpolyline, 20.0,  400.0); // B2: 3
			addCoord(innerpolyline, 20.0,  100.0); // B2: 4
			addCoord(innerpolyline,100.0,  100.0); // B2: 5
			linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,innerpolyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		builder.addItfLinetableObject(linetableObj);
        builder.buildSurfaces();
        ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
        assertEquals(1, errs.size());
        assertEquals("Overlay coord1 (100.000, 100.000), coord2 (100.000, 400.000), tids 1, 2", errs.get(0).getLocalizedMessage());
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn der innere Rand einer Polygon,
	// den aeusseren Rand der Polygon ueberschneidet.
	@Test
	public void test_1Polygon_InnererRandUeberSchneidetAeussererRand_Fail() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		// aussenrand
		IomObject polyline=newPolyline();
		String mainObjectTid="10";
		{
			addCoord(polyline,100.0,  100.0); // B1: 1
			addCoord(polyline,400.0,  100.0); // B1: 2
			addCoord(polyline,400.0,  400.0); // B1: 3
			addCoord(polyline,100.0,  400.0); // B1: 4
			addCoord(polyline,100.0,  100.0); // B1: 5
			IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// innenrand
		IomObject innerpolyline=newPolyline();
		IomObject linetableObj=null;
		{
			addCoord(innerpolyline,160.0,  300.0); // B2: 1
			addCoord(innerpolyline,340.0,  300.0); // B2: 2
			addCoord(innerpolyline,340.0,  480.0); // B2: 3
			addCoord(innerpolyline,160.0,  480.0); // B2: 4
			addCoord(innerpolyline,160.0,  300.0); // B2: 5
			linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,innerpolyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		builder.addItfLinetableObject(linetableObj);
        builder.buildSurfaces();
        ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
        assertEquals(2, errs.size());
        assertEquals("Intersection coord1 (340.000, 400.000), tids 1, 2", errs.get(0).getLocalizedMessage());
        assertEquals("Intersection coord1 (160.000, 400.000), tids 1, 2", errs.get(1).getLocalizedMessage());
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn ein Punkt des inneren Randes,
	// eine Linie des aeusseren Randes schneidet und 1 Linie des inneren Randes,
	// auf einer Linie des aeusseren Randes liegt.
	@Test
	public void test_1Polygon_PunktVonInnererRandlinieSchneidetAussenRandlinie_Fail() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		// aussenrand
		IomObject polyline=newPolyline();
		String mainObjectTid="10";
		{
			addCoord(polyline,100.0,  100.0); // B1: 1
			addCoord(polyline,400.0,  100.0); // B1: 2
			addCoord(polyline,400.0,  400.0); // B1: 3
			addCoord(polyline,100.0,  400.0); // B1: 4
			addCoord(polyline,100.0,  100.0); // B1: 5
			IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// innenrand
		IomObject innerpolyline=newPolyline();
		IomObject linetableObj=null;
		{
			addCoord(innerpolyline,100.0,  300.0); // B2: 1
			addCoord(innerpolyline,100.0,  100.0); // B2: 2
			addCoord(innerpolyline,200.0,  200.0); // B2: 3
			addCoord(innerpolyline,100.0,  300.0); // B2: 4
			linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,innerpolyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		
		builder.addItfLinetableObject(linetableObj);
        builder.buildSurfaces();
        // muss fehler overlay liefern.
        ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
        assertEquals(1, errs.size());
        assertEquals("Overlay coord1 (100.000, 100.000), coord2 (100.000, 300.000), tids 1, 2", errs.get(0).getLocalizedMessage());
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn sich 2 innere Raender ueberschneiden.
	@Test
	public void test_1Polygon_2RandlinienAufeinander_InnerRandRingNichtGueltig_Fail() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		// aussenrand
		IomObject polyline=newPolyline();
		String mainObjectTid="10";
		{
			addCoord(polyline,100.0,  100.0); // B1: 1
			addCoord(polyline,400.0,  100.0); // B1: 2
			addCoord(polyline,400.0,  400.0); // B1: 3
			addCoord(polyline,100.0,  400.0); // B1: 4
			addCoord(polyline,100.0,  100.0); // B1: 5
			IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// innenrand
		IomObject innerpolyline=newPolyline();
		IomObject linetableObj=null;
		{
			addCoord(innerpolyline,100.0,  300.0); // B2: 1
			addCoord(innerpolyline,100.0,  100.0); // B2: 2
			addCoord(innerpolyline,200.0,  200.0); // B2: 3
			addCoord(innerpolyline,140.0,  300.0); // B2: 4
			linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,innerpolyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		
		builder.addItfLinetableObject(linetableObj);
        builder.buildSurfaces();
        // muss fehler: overlay liefern und nicht dangles.
        ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
        assertEquals(1, errs.size());
        assertEquals("Overlay coord1 (100.000, 100.000), coord2 (100.000, 300.000), tids 1, 2", errs.get(0).getLocalizedMessage());
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn der innere Rand ueber dem
	// aeusseren Rand auf einer Linie liegt, der innere Rand nicht vollstaendig
	// geschlossen wird und 2 dangle Linien existieren.
	@Test
	public void test_1Polygon_2RandlinienAufeinander_2DangleLinien_Fail() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		// aussenrand
		IomObject polyline=newPolyline();
		String mainObjectTid="10";
		{
			addCoord(polyline,100.0,  100.0); // B1: 1
			addCoord(polyline,400.0,  100.0); // B1: 2
			addCoord(polyline,400.0,  400.0); // B1: 3
			addCoord(polyline,100.0,  400.0); // B1: 4
			addCoord(polyline,100.0,  100.0); // B1: 5
			IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// innenrand
		IomObject innerpolyline=newPolyline();
		IomObject linetableObj=null;
		{
			addCoord(innerpolyline,200.0,  320.0); // B2: 1
			addCoord(innerpolyline,100.0,  400.0); // B2: 2
			addCoord(innerpolyline,100.0,  100.0); // B2: 3
			addCoord(innerpolyline,200.0,  220.0); // B2: 4
			linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,innerpolyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		builder.addItfLinetableObject(linetableObj);
        builder.buildSurfaces();
        // muss fehler: overlay liefern und keine dangles.
        ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
        assertEquals(1, errs.size());
        assertEquals("Overlay coord1 (100.000, 100.000), coord2 (100.000, 400.000), tids 1, 2", errs.get(0).getLocalizedMessage());
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn 1 Linie des inneren Randes einer Polygon
	// zum Aussenrand laeuft und von dort wieder zurueck zum inneren Rand (Doppelspurig).
	@Test
	public void test_1Polygon_2Randlinien_1LinieSpaltetPolygon_Fail() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		// aussenrand
		IomObject polyline=newPolyline();
		String mainObjectTid="2";
		{
			addCoord(polyline,100.0,  100.0); // B1: 1
			addCoord(polyline,400.0,  100.0); // B1: 2
			addCoord(polyline,400.0,  400.0); // B1: 3
			addCoord(polyline,100.0,  400.0); // B1: 4
			addCoord(polyline,100.0,  100.0); // B1: 5
			IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// innenrand
		IomObject innerpolyline=newPolyline();
		IomObject linetableObj=null;
		{
			addCoord(innerpolyline,200.0,  220.0); // B2: 1
			addCoord(innerpolyline,300.0,  220.0); // B2: 2
			addCoord(innerpolyline,300.0,  360.0); // B2: 3
			addCoord(innerpolyline,260.0,  360.0); // B2: 4
			addCoord(innerpolyline,260.0,  400.0); // B2: 5
			addCoord(innerpolyline,260.0,  360.0); // B2: 6
			addCoord(innerpolyline,200.0,  360.0); // B2: 7
			addCoord(innerpolyline,200.0,  220.0); // B2: 8
			linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,innerpolyline);
			//System.out.println(linetableObj);
		}
		builder.addItfLinetableObject(linetableObj);
        builder.buildSurfaces();
        // muss fehler: overlay und intersections liefern
        ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
        assertEquals(2, errs.size());
        assertEquals("Intersection coord1 (260.000, 400.000), tids 1, 2:1", errs.get(0).getLocalizedMessage());
        assertEquals("Overlay coord1 (260.000, 360.000), coord2 (260.000, 400.000), tids 2:1, 2:1", errs.get(1).getLocalizedMessage());
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn keine Referenz auf das Polygon
	// erstellt wurde.
	@Test
	public void test_1Polygon_InnererRandKeineRefAufPolygon_Fail() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,null,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		ArrayList<IoxInvalidDataException> dataErrs=builder.getDataerrs();
		assertEquals("boundary line without reference to main table", dataErrs.get(0).getMessage());
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn der aeussere Rand nicht geschlossen wurde.
	@Test
	public void test_1Polygon_RandNichtGeschlossen_Fail() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
        builder.buildSurfaces();
        ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
        assertEquals(2, errs.size());
        assertEquals("dangle tid 1", errs.get(0).getLocalizedMessage());
        assertEquals("no polygon", errs.get(1).getLocalizedMessage());
	}
	
	// prueft ob ein Polygon erstellt werden kann, wenn 1 Punkt des inneren Randes,
	// einen Punkt des aeusseren Randes beruehrt.
	@Test
	public void test_1Polygon_InnererRandBeruehrtAussenrandAnEinemPunkt_Ok() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		// aussenrand
		IomObject polyline=newPolyline();
		String mainObjectTid="10";
		{	
			addCoord(polyline,500.0,  100.0); // B1: 1
			addCoord(polyline,500.0,  200.0); // B1: 2
			addCoord(polyline,600.0,  200.0); // B1: 3
			addCoord(polyline,600.0,  100.0); // B1: 4
			addCoord(polyline,500.0,  100.0); // B1: 5
			IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// innenrand
		IomObject innerpolyline=newPolyline();
		IomObject linetableObj=null;
		{
			addCoord(innerpolyline,500.0,  100.0); // B2: 1
			addCoord(innerpolyline,540.0,  180.0); // B2: 2
			addCoord(innerpolyline,540.0,  120.0); // B2: 3
			addCoord(innerpolyline,500.0,  100.0); // B2: 4
			linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,innerpolyline);
			//System.out.println(linetableObj);
		}
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 500.0, C2 100.0}, COORD {C1 500.0, C2 200.0}, COORD {C1 600.0, C2 200.0}, COORD {C1 600.0, C2 100.0}, COORD {C1 500.0, C2 100.0}]}}}, BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 500.0, C2 100.0}, COORD {C1 540.0, C2 120.0}, COORD {C1 540.0, C2 180.0}, COORD {C1 500.0, C2 100.0}]}}}]}}",polygon.toString());
	}
	
	// prueft ob ein Polygon erstellt werden kann, wenn 1 Punkt des inneren Randes,
	// den aeusseren Rand beruehrt.
	@Test
	public void test_1Polygon_InnererPunktBeruehrtAussenrandAnEinemPunkt_Ok() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		// aussenrand
		IomObject polyline=newPolyline();
		String mainObjectTid="10";
		{	
			addCoord(polyline,500.0,  100.0); // B1: 1
			addCoord(polyline,500.0,  200.0); // B1: 2
			addCoord(polyline,600.0,  200.0); // B1: 3
			addCoord(polyline,600.0,  100.0); // B1: 4
			addCoord(polyline,500.0,  100.0); // B1: 5
			IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// innenrand
		IomObject innerpolyline=newPolyline();
		IomObject linetableObj=null;
		{
			addCoord(innerpolyline,500.0,  150.0); // B2: 1
			addCoord(innerpolyline,540.0,  180.0); // B2: 2
			addCoord(innerpolyline,540.0,  120.0); // B2: 3
			addCoord(innerpolyline,500.0,  150.0); // B2: 4
			linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,innerpolyline);
			//System.out.println(linetableObj);
		}
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 500.0, C2 150.0}, COORD {C1 540.0, C2 180.0}, COORD {C1 540.0, C2 120.0}, COORD {C1 500.0, C2 150.0}]}}}}}",polygon.toString());
	}
	
	// Es soll getestet werden, ob eine Intersection Fehlermeldung ausgegeben wird,
	// wenn 2 gerade Linien von je einem inneren Rand, uebereinander liegen.
	@Test
	public void test_1Polygon_ZweiLinienVonJe2InnerenRaenderLiegen_Aufeinander_Fail() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		String mainObjectTid="10";
		// aussenrand
		IomObject polyline=newPolyline();
		{
			addCoord(polyline,120.0,  120.0); // B1: 1
			addCoord(polyline,320.0,  120.0); // B1: 2
			addCoord(polyline,220.0,  380.0); // B1: 3
			addCoord(polyline,120.0,  120.0); // B1: 4
			IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// innenrand
		IomObject innerpolyline=newPolyline();
		{
			addCoord(innerpolyline,200.0,  240.0); // B2: 1
			addCoord(innerpolyline,240.0,  240.0); // B2: 2
			addCoord(innerpolyline,220.0,  320.0); // B2: 3
			addCoord(innerpolyline,200.0,  240.0); // B2: 4
			IomObject linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,innerpolyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// innenrand 2
		IomObject innerpolyline2=newPolyline();
		IomObject linetableObj=null;
		{
			addCoord(innerpolyline2,200.0,  240.0); // B3: 1
			addCoord(innerpolyline2,240.0,  240.0); // B3: 2
			addCoord(innerpolyline2,220.0,  160.0); // B3: 3
			addCoord(innerpolyline2,200.0,  240.0); // B3: 1
			linetableObj=createLinetableObj("3",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,innerpolyline2);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
        builder.buildSurfaces();
        ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
        assertEquals(1, errs.size());
        assertEquals("Overlay coord1 (200.000, 240.000), coord2 (240.000, 240.000), tids 2, 3", errs.get(0).getLocalizedMessage());
	}
	
	// Es wird getestet, ob eine Polygon erstellt werden kann,
	// wenn die 2 inneren Randlinien einander ueberschneiden.
	@Test
	public void test_1Polygon_2InnereRaenderUeberschneidenSich_Fail() throws Exception {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		String mainObjectTid="10";
		// aussenrand
		IomObject polyline=newPolyline();
		{
			addCoord(polyline,500.0,  100.0); // B1: 1
			addCoord(polyline,500.0,  200.0); // B1: 2
			addCoord(polyline,600.0,  200.0); // B1: 3
			addCoord(polyline,600.0,  100.0); // B1: 4
			addCoord(polyline,500.0,  100.0); // B1: 5
			IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// innenrand
		IomObject innerpolyline=newPolyline();
		{
			addCoord(innerpolyline,540.0,  180.0); // B2: 1
			addCoord(innerpolyline,560.0,  180.0); // B2: 2
			addCoord(innerpolyline,550.0,  140.0); // B2: 3
			addCoord(innerpolyline,540.0,  180.0); // B2: 4
			IomObject linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,innerpolyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// innenrand 2
		IomObject innerpolyline2=newPolyline();
		IomObject linetableObj=null;
		{
			addCoord(innerpolyline2,540.0,  120.0); // B3: 1
			addCoord(innerpolyline2,560.0,  120.0); // B3: 2
			addCoord(innerpolyline2,550.0,  160.0); // B3: 3
			addCoord(innerpolyline2,540.0,  120.0); // B3: 4
			linetableObj=createLinetableObj("3",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,innerpolyline2);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
        builder.buildSurfaces();
        ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
        assertEquals(2, errs.size());
        assertEquals("Intersection coord1 (552.500, 150.000), tids 2, 3", errs.get(0).getLocalizedMessage());
        assertEquals("Intersection coord1 (547.500, 150.000), tids 2, 3", errs.get(1).getLocalizedMessage());
	}
	
	// Es wird getestet, ob eine Polygon erstellt werden kann,
	// wenn die 2 inneren Raender einander an einem Punkt beruehren.
	@Test
	public void test_1Polygon_2InnereRaenderBeruehrenSichAn1Punkt_Ok() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		// aussenrand
		IomObject polyline=newPolyline();
		String mainTid="10";
		{
			addCoord(polyline,120.0,  120.0); // B1: 1
			addCoord(polyline,320.0,  120.0); // B1: 2
			addCoord(polyline,220.0,  380.0); // B1: 3
			addCoord(polyline,120.0,  120.0); // B1: 4
			IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainTid,polyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// innenrand
		IomObject innerpolyline=newPolyline();
		{
			addCoord(innerpolyline,200.0,  160.0); // B2: 1
			addCoord(innerpolyline,240.0,  160.0); // B2: 2
			addCoord(innerpolyline,220.0,  200.0); // B2: 3
			addCoord(innerpolyline,200.0,  160.0); // B2: 4
			IomObject linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainTid,innerpolyline);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		// innenrand 2
		IomObject innerpolyline2=newPolyline();
		IomObject linetableObj=null;
		{
			addCoord(innerpolyline2,220.0,  200.0); // B3: 1
			addCoord(innerpolyline2,240.0,  240.0); // B3: 2
			addCoord(innerpolyline2,200.0,  240.0); // B3: 3
			addCoord(innerpolyline2,220.0,  200.0); // B3: 4
			linetableObj=createLinetableObj("3",TABLENAME,REFATTR,GEOMATTR,mainTid,innerpolyline2);
			//System.out.println(linetableObj);
			builder.addItfLinetableObject(linetableObj);
		}
		builder.buildSurfaces();
		// polygon
		IomObject polygon=builder.getSurfaceObject(mainTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary ["
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 120.0, C2 120.0}, COORD {C1 220.0, C2 380.0}, COORD {C1 320.0, C2 120.0}, COORD {C1 120.0, C2 120.0}]}}}, "
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 200.0, C2 160.0}, COORD {C1 240.0, C2 160.0}, COORD {C1 220.0, C2 200.0}, COORD {C1 200.0, C2 160.0}]}}}, "
				+ "BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 200.0, C2 240.0}, COORD {C1 220.0, C2 200.0}, COORD {C1 240.0, C2 240.0}, COORD {C1 200.0, C2 240.0}]}}}]}}" 
				,polygon.toString());
	}
	
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn ein weiterer Aussenrand
	// durch 2 Linien, welche sich nicht wieder schliessen, entsteht.
	@Test
	public void test_1Polygon_AeussererRand2LinienOhneGemeinsamerStart_Fail() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR,GEOMATTR);
		String mainObjectTid="10";
		
		IomObject polyline=newPolyline();
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,125.0,  140.0); 
		addCoord(polyline,125.0,  145.0); 
		addCoord(polyline,120.0,  145.0); 
		addCoord(polyline,120.0,  140.0);
		addCoord(polyline,125.0,  140.0); 
		linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
        builder.buildSurfaces();
        ArrayList<IoxInvalidDataException> errs=builder.getDataerrs();
        assertEquals(2, errs.size());
        assertEquals("superfluous outerboundary tid 10", errs.get(0).getLocalizedMessage());
        assertEquals("multipolygon detected", errs.get(1).getLocalizedMessage());
	}
	
	// prueft ob 2 Polygone, welche aneinander liegen, erstellt werden koennen.
	@Test
	public void test_2Polygon_ZweiAneinanderliegendePolygone_Ok() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,500.0,  100.0);
		addCoord(polyline,500.0,  200.0);
		addCoord(polyline,600.0,  200.0);
		addCoord(polyline,600.0,  100.0);
		addCoord(polyline,500.0,  100.0);
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,600.0,  100.0);
		addCoord(polyline,600.0,  200.0); 
		addCoord(polyline,700.0,  200.0); 
		addCoord(polyline,700.0,  100.0);
		addCoord(polyline,600.0,  100.0);
		String mainObject2Tid="11";
		linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObject2Tid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 500.0, C2 100.0}, COORD {C1 500.0, C2 200.0}, COORD {C1 600.0, C2 200.0}, COORD {C1 600.0, C2 100.0}, COORD {C1 500.0, C2 100.0}]}}}}}",polygon.toString());
		IomObject polygon2=builder.getSurfaceObject(mainObject2Tid);
		//System.out.println(polygon2);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 600.0, C2 100.0}, COORD {C1 600.0, C2 200.0}, COORD {C1 700.0, C2 200.0}, COORD {C1 700.0, C2 100.0}, COORD {C1 600.0, C2 100.0}]}}}}}",polygon2.toString());
		
	}
	
	// prueft ob 2 Polygone, welche in sich geschachtelt sind, erstellt werden koennen.
	@Test
	public void test_2Polygon_ZweiGeschachteltePolygone_Ok() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		String mainObject2Tid="20";
		linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObject2Tid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}",polygon.toString());
		IomObject polygon2=builder.getSurfaceObject(mainObject2Tid);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 112.0, C2 120.0}, COORD {C1 115.0, C2 120.0}, COORD {C1 115.0, C2 115.0}, COORD {C1 110.0, C2 110.0}]}}}}}",polygon2.toString());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn 2 Polygone
	// (mit je 3 Linien) genau aufeinander liegen.
	@Test
	public void test_2Polygon_ExaktUebereinander_Ok() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,100.0,  100.0);
		addCoord(polyline,400.0,  100.0); 
		addCoord(polyline,400.0,  400.0); 
		addCoord(polyline,100.0,  400.0); 
		addCoord(polyline,100.0,  100.0);
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,100.0,  100.0);
		addCoord(polyline,400.0,  100.0); 
		addCoord(polyline,400.0,  400.0); 
		addCoord(polyline,100.0,  400.0); 
		addCoord(polyline,100.0,  100.0);
		String mainObject2Tid="20";
		linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObject2Tid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		IomObject polygon10=builder.getSurfaceObject(mainObjectTid);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 100.0, C2 100.0}, COORD {C1 100.0, C2 400.0}, COORD {C1 400.0, C2 400.0}, COORD {C1 400.0, C2 100.0}, COORD {C1 100.0, C2 100.0}]}}}}}",polygon10.toString());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn 2 Polygone
	// genau aufeinander liegen und die zweite Polygon,
	// je 1 Punkt pro Linie mehr hat.
	@Test
	public void test_2Polygon_Uebereinander_Polygon1Hat3PunkteMehr_Ok() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		
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
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		IomObject polyline2=newPolyline();
		addCoord(polyline2,100.0,  100.0); // B1: 1
		addCoord(polyline2,400.0,  100.0); // B1: 2
		addCoord(polyline2,400.0,  400.0); // B1: 3
		addCoord(polyline2,100.0,  400.0); // B1: 4
		addCoord(polyline2,100.0,  100.0); // B1: 5
		String mainObject2Tid="20";
		linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObject2Tid,polyline2);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		IomObject polygon10=builder.getSurfaceObject(mainObjectTid);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 100.0, C2 100.0}, COORD {C1 120.0, C2 260.0}, COORD {C1 100.0, C2 400.0}, COORD {C1 260.0, C2 400.0}, COORD {C1 400.0, C2 400.0}, COORD {C1 400.0, C2 260.0}, COORD {C1 400.0, C2 100.0}, COORD {C1 260.0, C2 100.0}, COORD {C1 100.0, C2 100.0}]}}}}}",polygon10.toString());
		IomObject polygon11=builder.getSurfaceObject(mainObject2Tid);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 100.0, C2 100.0}, COORD {C1 100.0, C2 400.0}, COORD {C1 400.0, C2 400.0}, COORD {C1 400.0, C2 100.0}, COORD {C1 100.0, C2 100.0}]}}}}}",polygon11.toString());
	}
	
	// prueft, ob 2 Polygone mit je einem Kreisbogen, welche beide exakt uebereinander liegen,
	// erstellt werden koennen.
	@Test
	public void test_2Polygon_MitJe1KreisbogenLiegenGenau_Aufeinander_Ok() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR,GEOMATTR,0.05,3);
		
		IomObject polyline=newPolyline();
		addCoord(polyline, 20.0, 160.0);
		addCoord(polyline,240.0, 160.0);
		addArc(polyline, 200.0, 260.0, 240.0, 360.0);
		addCoord(polyline,20.0, 360.0);
		addCoord(polyline,20.0, 160.0);
		String mainObj10="10";
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObj10,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		IomObject polyline2=newPolyline();
		addCoord(polyline2,20.0, 160.0);
		addCoord(polyline2,240.0, 160.0);
		addArc(polyline2, 200.0, 260.0, 240.0, 360.0);
		addCoord(polyline2,20.0, 360.0);
		addCoord(polyline2,20.0, 160.0);
		String mainObj11="11";
		IomObject linetableObj2=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObj11,polyline2);
		//System.out.println(linetableObj2);
		builder.addItfLinetableObject(linetableObj2);
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		IomObject polygon10=builder.getSurfaceObject(mainObj10);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 20.0, C2 160.0}, COORD {C1 20.0, C2 360.0}, COORD {C1 240.0, C2 360.0}, ARC {A1 200.0, A2 260.0, C1 240.0, C2 160.0}, COORD {C1 20.0, C2 160.0}]}}}}}",polygon10.toString());
		IomObject polygon11=builder.getSurfaceObject(mainObj11);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 20.0, C2 160.0}, COORD {C1 20.0, C2 360.0}, COORD {C1 240.0, C2 360.0}, ARC {A1 200.0, A2 260.0, C1 240.0, C2 160.0}, COORD {C1 20.0, C2 160.0}]}}}}}",polygon11.toString());
	}
	
	// prueft, ob 2 Polygone mit je einem Kreisbogen, welche beide
	// unterschiedliche Kreisbogenpunkte haben und uebereinander liegen,
	// erstellt werden koennen.
	@Test
	public void test_2Polygon_MitJe1KreisbogenLiegenNichtGenauAufeinander_Ok() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR,GEOMATTR,0.05,3);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,20.0, 160.0);
		addCoord(polyline,240.0, 160.0);
		addArc(polyline, 180.0, 200.0, 240.0, 360.0);
		addCoord(polyline,20.0, 360.0);
		addCoord(polyline,20.0, 160.0);
		String mainObj10="10";
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObj10,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		IomObject polyline2=newPolyline();
		addCoord(polyline2,20.0, 160.0);
		addCoord(polyline2,240.0, 160.0);
		addArc(polyline2, 160.0, 240.0, 240.0, 360.0);
		addCoord(polyline2,20.0, 360.0);
		addCoord(polyline2,20.0, 160.0);
		String mainObj11="11";
		IomObject linetableObj2=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObj11,polyline2);
		//System.out.println(linetableObj2);
		builder.addItfLinetableObject(linetableObj2);
		builder.buildSurfaces();
		IomObject polygon10=builder.getSurfaceObject(mainObj10);
		//System.out.println(polygon10);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 20.0, C2 160.0}, COORD {C1 20.0, C2 360.0}, COORD {C1 240.0, C2 360.0}, ARC {A1 180.0, A2 200.0, C1 240.0, C2 160.0}, COORD {C1 20.0, C2 160.0}]}}}}}",polygon10.toString());
		IomObject polygon11=builder.getSurfaceObject(mainObj11);
		//System.out.println(polygon11);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 20.0, C2 160.0}, COORD {C1 20.0, C2 360.0}, COORD {C1 240.0, C2 360.0}, ARC {A1 160.0, A2 240.0, C1 240.0, C2 160.0}, COORD {C1 20.0, C2 160.0}]}}}}}",polygon11.toString());
	}
	
	// 2 Polygone beruehren sich an einer RandLinie. Randlinie 1 hat 2 Punkte welche
	// Randlinie 2 beruehren. Randlinie 2 hat einen Punkt mehr.
	@Test
	public void test_2Polygon_2RandStreckenAufeinander_AnzahlPunkteUnterschiedlich_Ok() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,10.0,   8.0);
		addCoord(polyline,40.0,   8.0); 
		addCoord(polyline,40.0,  40.0);
		addCoord(polyline,10.0,  40.0); 
		addCoord(polyline,10.0,  26.0);
		addCoord(polyline,10.0,   8.0); 
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,10.0,   8.0);
		addCoord(polyline,10.0,  40.0);
		addCoord(polyline, 2.0,  40.0);
		addCoord(polyline, 2.0,   8.0);
		addCoord(polyline,10.0,   8.0);
		String mainObject2Tid="20";
		linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObject2Tid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		IomObject polygon10=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon10);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 10.0, C2 8.0}, COORD {C1 10.0, C2 26.0}, COORD {C1 10.0, C2 40.0}, COORD {C1 40.0, C2 40.0}, COORD {C1 40.0, C2 8.0}, COORD {C1 10.0, C2 8.0}]}}}}}",polygon10.toString());
		IomObject polygon11=builder.getSurfaceObject(mainObject2Tid);
		//System.out.println(polygon11);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2.0, C2 8.0}, COORD {C1 2.0, C2 40.0}, COORD {C1 10.0, C2 40.0}, COORD {C1 10.0, C2 8.0}, COORD {C1 2.0, C2 8.0}]}}}}}",polygon11.toString());
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn 2 Randstrecken von 2 Polygonen
	// sich uebereinander befinden.
	@Test
	public void test_2Polygon_2Randstrecken_Aufeinander_Ok() throws IoxException {
		// gemeinsamer Rand 120,110 -> 120,140
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,120.0,  110.0);
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,150.0,  140.0); 
		addCoord(polyline,150.0,  110.0); 
		addCoord(polyline,120.0,  110.0);
		String mainObject2Tid="20";
		linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObject2Tid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		IomObject polygon10=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon10);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}",polygon10.toString());
		IomObject polygon11=builder.getSurfaceObject(mainObject2Tid);
		//System.out.println(polygon11);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 120.0, C2 110.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 150.0, C2 140.0}, COORD {C1 150.0, C2 110.0}, COORD {C1 120.0, C2 110.0}]}}}}}",polygon11.toString());
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn Polygon1 den inneren Rand
	// und den aeusseren Rand von Polygon2 ueberschneidet.
	@Test
	public void test_2Polygon_Poligon1Ueberschneidet_InnererRandVonPolygon2_Ok() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,160.0,  300.0);
		addCoord(polyline,340.0,  300.0); 
		addCoord(polyline,340.0,  480.0); 
		addCoord(polyline,160.0,  480.0); 
		addCoord(polyline,160.0,  300.0);
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		// aeusserer Rand
		polyline=newPolyline();
		addCoord(polyline,100.0,  100.0);
		addCoord(polyline,400.0,  100.0); 
		addCoord(polyline,400.0,  400.0); 
		addCoord(polyline,100.0,  400.0); 
		addCoord(polyline,100.0,  100.0);
		String mainObject2Tid="20";
		linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObject2Tid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		// innerer Rand
		polyline=newPolyline();
		addCoord(polyline,200.0,  220.0);
		addCoord(polyline,300.0,  220.0); 
		addCoord(polyline,300.0,  360.0); 
		addCoord(polyline,200.0,  360.0); 
		addCoord(polyline,200.0,  220.0);
		linetableObj=createLinetableObj("3",TABLENAME,REFATTR,GEOMATTR,mainObject2Tid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		IomObject polygon10=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon10);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 160.0, C2 300.0}, COORD {C1 160.0, C2 480.0}, COORD {C1 340.0, C2 480.0}, COORD {C1 340.0, C2 300.0}, COORD {C1 160.0, C2 300.0}]}}}}}",polygon10.toString());
		IomObject polygon11=builder.getSurfaceObject(mainObject2Tid);
		//System.out.println(polygon11);
		assertEquals("MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 100.0, C2 100.0}, COORD {C1 100.0, C2 400.0}, COORD {C1 400.0, C2 400.0}, COORD {C1 400.0, C2 100.0}, COORD {C1 100.0, C2 100.0}]}}}, BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 200.0, C2 220.0}, COORD {C1 300.0, C2 220.0}, COORD {C1 300.0, C2 360.0}, COORD {C1 200.0, C2 360.0}, COORD {C1 200.0, C2 220.0}]}}}]}}",polygon11.toString());
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn ein Punkt einer Polygon
	// eine Randlinie einer anderen Polygon beruehrt.
	@Test
	public void test_2Polygon_PunktAufRand_Ok() throws IoxException {
		// Punkt 120,115 auf Rand 120,110 -> 120,140
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,150.0,  140.0); 
		addCoord(polyline,150.0,  110.0); 
		addCoord(polyline,120.0,  115.0);
		addCoord(polyline,150.0,  140.0); 
		String mainObject2Tid="20";
		linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObject2Tid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		IomObject polygon10=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon10);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}",polygon10.toString());
		IomObject polygon11=builder.getSurfaceObject(mainObject2Tid);
		//System.out.println(polygon11);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 120.0, C2 115.0}, COORD {C1 150.0, C2 140.0}, COORD {C1 150.0, C2 110.0}, COORD {C1 120.0, C2 115.0}]}}}}}",polygon11.toString());
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn ein Punkt einer Polygon
	// auf einem Punkt einer anderen Polygon liegt.
	@Test
	public void test_2Polygon_PunktAufPunkt_Ok() throws IoxException {
		// Polygone haben gemeinsamen Randpunkt 120,115 
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  115.0);
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,150.0,  140.0); 
		addCoord(polyline,150.0,  110.0); 
		addCoord(polyline,120.0,  115.0);
		addCoord(polyline,150.0,  140.0); 
		String mainObject2Tid="20";
		linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObject2Tid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 115.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}",polygon.toString());
		IomObject polygon2=builder.getSurfaceObject(mainObject2Tid);
		//System.out.println(polygon2);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 120.0, C2 115.0}, COORD {C1 150.0, C2 140.0}, COORD {C1 150.0, C2 110.0}, COORD {C1 120.0, C2 115.0}]}}}}}",polygon2.toString());
	}
	
	// Es wird getestet, ob 2 Polygone erstellt werden koennen,
	// wenn 2 Polygone exakt uebereinander liegen und einer
	// der beiden Polygone einen inneren Rand hat.
	@Test
	public void test_2Polygon_1InnenRand_2PolygoneUeberdeckenSich_Ok() throws IoxException {
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(REFATTR, GEOMATTR);
		
		IomObject polyline=newPolyline();
		addCoord(polyline,100.0,  100.0);
		addCoord(polyline,400.0,  100.0); 
		addCoord(polyline,400.0,  400.0); 
		addCoord(polyline,100.0,  400.0); 
		addCoord(polyline,100.0,  100.0);
		String mainObjectTid="10";
		IomObject linetableObj=createLinetableObj("1",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		IomObject innerpolyline=newPolyline();
		addCoord(innerpolyline,120.0,  120.0);
		addCoord(innerpolyline,140.0,  120.0); 
		addCoord(innerpolyline,140.0,  180.0); 
		addCoord(innerpolyline,120.0,  180.0); 
		addCoord(innerpolyline,120.0,  120.0);
		linetableObj=createLinetableObj("2",TABLENAME,REFATTR,GEOMATTR,mainObjectTid,innerpolyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		
		polyline=newPolyline();
		addCoord(polyline,100.0,  100.0);
		addCoord(polyline,400.0,  100.0); 
		addCoord(polyline,400.0,  400.0); 
		addCoord(polyline,100.0,  400.0); 
		addCoord(polyline,100.0,  100.0);
		String mainObject2Tid="20";
		linetableObj=createLinetableObj("3",TABLENAME,REFATTR,GEOMATTR,mainObject2Tid,polyline);
		//System.out.println(linetableObj);
		builder.addItfLinetableObject(linetableObj);
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject(mainObjectTid);
		//System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 100.0, C2 100.0}, COORD {C1 100.0, C2 400.0}, COORD {C1 400.0, C2 400.0}, COORD {C1 400.0, C2 100.0}, COORD {C1 100.0, C2 100.0}]}}}, BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 120.0, C2 120.0}, COORD {C1 140.0, C2 120.0}, COORD {C1 140.0, C2 180.0}, COORD {C1 120.0, C2 180.0}, COORD {C1 120.0, C2 120.0}]}}}]}}",polygon.toString());
		IomObject polygon2=builder.getSurfaceObject(mainObject2Tid);
		//System.out.println(polygon2);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 100.0, C2 100.0}, COORD {C1 100.0, C2 400.0}, COORD {C1 400.0, C2 400.0}, COORD {C1 400.0, C2 100.0}, COORD {C1 100.0, C2 100.0}]}}}}}",polygon2.toString());
	}
}