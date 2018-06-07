package ch.interlis.iom_j.itf.impl.jtsext.geom;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

import ch.interlis.iom_j.itf.impl.jtsext.noding.CurvePairInt;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class CurvePolygonOverlayTest {
	
	private com.vividsolutions.jts.geom.Coordinate[] coords=null;

	// es wird false erwartet, da eine Polygon, welche genau ueber einer InnerBoundary
	// einer anderen Polygon liegt, erstellt wird.
	@Test
	public void twoPolygon_Polygon2ExactlyOverInnerBoundaryOfPolygon1_False() throws IoxException {
		LinearRing shell=null;
		LinearRing[] holes=null;
		holes=new LinearRing[1];
		Polygon polygon1=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("100000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("200000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("200000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[4]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("140000.000"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("560000.000"), new Double("140000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("560000.000"), new Double("160000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("160000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("140000.000"));
			coords[4]=coord5;
			LinearRing ring=new LinearRing(coords, new PrecisionModel(), 2056);
			holes[0]=ring;
		}
		polygon1=new Polygon(shell, holes, new PrecisionModel(), 2056);
		
		Polygon polygon2=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("140000.000"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("560000.000"), new Double("140000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("560000.000"), new Double("160000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("160000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("140000.000"));
			coords[4]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		polygon2=new Polygon(shell, null, new PrecisionModel(), 2056);

		boolean isOverlay=CurvePolygon.polygonOverlays(polygon1, polygon2);
		////System.out.println(isOverlay);
		assertEquals(false, isOverlay);
	}
	
	// es wird true erwartet, da ein Polygon, nicht genau
	// ueber einer InnerBoundary einer anderen Polygon liegt.
	@Test
	public void twoPolygon_Polygon2OverlapsInnerBoundaryOfPolygon1_True() throws IoxException, Iox2jtsException {
		LinearRing shell=null;
		LinearRing[] holes=null;
		holes=new LinearRing[1];
		Polygon polygon1=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("100000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("200000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("200000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[4]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("140000.000"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("560000.000"), new Double("140000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("560000.000"), new Double("160000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("160000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("140000.000"));
			coords[4]=coord5;
			LinearRing ring=new LinearRing(coords, new PrecisionModel(), 2056);
			holes[0]=ring;
		}
		polygon1=new Polygon(shell, holes, new PrecisionModel(), 2056);
		
		Polygon polygon2=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("140000.000"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("560000.000"), new Double("140000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("580000.000"), new Double("180000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("160000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("140000.000"));
			coords[4]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		polygon2=new Polygon(shell, null, new PrecisionModel(), 2056);

		boolean isOverlay=CurvePolygon.polygonOverlays(polygon1, polygon2);
		//System.out.println(isOverlay);
		assertEquals(true, isOverlay);
	}
	
	// es wird false erwartet, da 2 polygone welche sich nicht beruehren, erstellt werden.
	@Test
	public void twoPolygon_Separate_False() throws IoxException {
		LinearRing shell=null;
		Polygon polygon1=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord1=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[0]=coord1;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("100000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("200000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("200000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[4]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		polygon1=new Polygon(shell, null, new PrecisionModel(), 2056);
		
		Polygon polygon2=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord1=new com.vividsolutions.jts.geom.Coordinate(new Double("700000.000"), new Double("100000.000"));
			coords[0]=coord1;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("800000.000"), new Double("100000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("800000.000"), new Double("200000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("700000.000"), new Double("200000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("700000.000"), new Double("100000.000"));
			coords[4]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		polygon2=new Polygon(shell, null, new PrecisionModel(), 2056);

		boolean isOverlay=CurvePolygon.polygonOverlays(polygon1, polygon2);
		//System.out.println(isOverlay);
		assertEquals(false, isOverlay);
	}
	
	// es wird false erwartet, da polygon1 mit einer Linie, eine Linie der polygon2 ueberdeckt.
	// beide Linien haben die selben punkte.
	@Test
	public void twoPolygon_OverlayOn1Line_False() throws IoxException {
		LinearRing shell=null;
		Polygon polygon1=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord1=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[0]=coord1;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("100000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("200000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("200000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[4]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		polygon1=new Polygon(shell, null, new PrecisionModel(), 2056);
		
		Polygon polygon2=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord1=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("100000.000"));
			coords[0]=coord1;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("800000.000"), new Double("100000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("800000.000"), new Double("200000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("200000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("100000.000"));
			coords[4]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		polygon2=new Polygon(shell, null, new PrecisionModel(), 2056);

		boolean isOverlay=CurvePolygon.polygonOverlays(polygon1, polygon2);
		//System.out.println(isOverlay);
		assertEquals(false, isOverlay);
	}
	
	// es wird true erwartet, da polygon1 mit seiner Flaeche, die Flaeche von polygon2 ueberdeckt.
	@Test
	public void twoPolygon_OverlayEachOther_True() throws IoxException {
		LinearRing shell=null;
		Polygon polygon1=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord1=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[0]=coord1;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("100000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("200000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("200000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[4]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		polygon1=new Polygon(shell, null, new PrecisionModel(), 2056);
		
		Polygon polygon2=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord1=new com.vividsolutions.jts.geom.Coordinate(new Double("550000.000"), new Double("120000.000"));
			coords[0]=coord1;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("800000.000"), new Double("120000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("800000.000"), new Double("180000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("550000.000"), new Double("180000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("550000.000"), new Double("120000.000"));
			coords[4]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		polygon2=new Polygon(shell, null, new PrecisionModel(), 2056);

		boolean isOverlay=CurvePolygon.polygonOverlays(polygon1, polygon2);
		//System.out.println(isOverlay);
		assertEquals(true, isOverlay);
	}

	// es wird false erwartet, da eine Polygon, welche innerhalb der InnerBoundary
	// einer anderen Polygon liegt, erstellt wird.
	@Test
	public void twoPolygon_Polygon2LiesInsideInnerBoundaryOfPolygon1_False() throws IoxException {
		LinearRing shell=null;
		LinearRing[] holes=null;
		holes=new LinearRing[1];
		Polygon polygon1=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("100000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("200000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("200000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[4]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("140000.000"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("560000.000"), new Double("140000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("560000.000"), new Double("160000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("160000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("140000.000"));
			coords[4]=coord5;
			LinearRing ring=new LinearRing(coords, new PrecisionModel(), 2056);
			holes[0]=ring;
		}
		polygon1=new Polygon(shell, holes, new PrecisionModel(), 2056);
		
		Polygon polygon2=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("545000.000"), new Double("145000.000"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("555000.000"), new Double("145000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("555000.000"), new Double("155000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("545000.000"), new Double("155000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("545000.000"), new Double("145000.000"));
			coords[4]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		polygon2=new Polygon(shell, null, new PrecisionModel(), 2056);

		boolean isOverlay=CurvePolygon.polygonOverlays(polygon1, polygon2);
		//System.out.println(isOverlay);
		assertEquals(false, isOverlay);
	}
	
	// es wird false erwartet, da eine Linie von polygon1, eine Linie von polygon2 ueberdeckt.
	// beide Linien haben die selben punkte.
	@Test
	public void twoPolygon_Polygon2LiesOn1LineOfInnerboundaryOfPolygon1_False() throws IoxException {
		LinearRing shell=null;
		LinearRing[] holes=null;
		holes=new LinearRing[1];
		Polygon polygon1=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("100000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("200000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("200000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[4]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("140000.000"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("560000.000"), new Double("140000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("560000.000"), new Double("160000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("160000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("140000.000"));
			coords[4]=coord5;
			LinearRing ring=new LinearRing(coords, new PrecisionModel(), 2056);
			holes[0]=ring;
		}
		polygon1=new Polygon(shell, holes, new PrecisionModel(), 2056);
		
		Polygon polygon2=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("550000.000"), new Double("145000.000"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("560000.000"), new Double("145000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("560000.000"), new Double("155000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("550000.000"), new Double("155000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("550000.000"), new Double("145000.000"));
			coords[4]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		polygon2=new Polygon(shell, null, new PrecisionModel(), 2056);

		boolean isOverlay=CurvePolygon.polygonOverlays(polygon1, polygon2);
		//System.out.println(isOverlay);
		assertEquals(false, isOverlay);
	}
	
	// es wird false erwartet, da ein Polygon, nicht genau
	// ueber einer InnerBoundary einer anderen Polygon liegt.
	@Test
	public void twoPolygon_Polygon2OverlayOn1LineOfInnerboundaryOfPolygon1_True() throws IoxException, Iox2jtsException {
		LinearRing shell=null;
		LinearRing[] holes=null;
		holes=new LinearRing[1];
		Polygon polygon1=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("100000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("200000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("200000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[4]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("140000.000"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("560000.000"), new Double("140000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("560000.000"), new Double("160000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("160000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("540000.000"), new Double("140000.000"));
			coords[4]=coord5;
			LinearRing ring=new LinearRing(coords, new PrecisionModel(), 2056);
			holes[0]=ring;
		}
		polygon1=new Polygon(shell, holes, new PrecisionModel(), 2056);
		
		Polygon polygon2=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("550000.000"), new Double("145000.000"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("580000.000"), new Double("145000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("580000.000"), new Double("155000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("550000.000"), new Double("155000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("550000.000"), new Double("145000.000"));
			coords[4]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		polygon2=new Polygon(shell, null, new PrecisionModel(), 2056);

		boolean isOverlay=CurvePolygon.polygonOverlays(polygon1, polygon2);
		//System.out.println(isOverlay);
		assertEquals(true, isOverlay);
	}
	
	// es wird true erwartet, da beide Polygone sich exakt uebereinander befinden.
	@Test
	public void twoPolygon_Equal_True() throws IoxException, Iox2jtsException {
		LinearRing shell=null;
		Polygon polygon1=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("100000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("200000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("200000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[4]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		polygon1=new Polygon(shell, null, new PrecisionModel(), 2056);
		
		Polygon polygon2=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[5];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("100000.000"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("200000.000"));
			coords[2]=coord3;
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("200000.000"));
			coords[3]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[4]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		polygon2=new Polygon(shell, null, new PrecisionModel(), 2056);

		boolean isOverlay=CurvePolygon.polygonOverlays(polygon1, polygon2);
		//System.out.println(isOverlay);
		assertEquals(true, isOverlay);
	}
	
	// es wird false erwartet, da beide Arcs der Polygone aufeinander liegen.
	@Test
	public void twoPolygon_2ArcsLieOnEachOther_FromDifferentPolygons_False() throws IoxException {
		LinearRing shell=null;
		Polygon polygon1=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[7];
			com.vividsolutions.jts.geom.Coordinate coord1=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[0]=coord1;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("550000.000"), new Double("100000.000"));
			coords[1]=coord2;
			// coords[2]= ARC:
			// ARC  (new Double("575000.000"), new Double("150000.000"))
			// COORD(new Double("550000.000"), new Double("200000.000"))
			
			Coordinate arcpt=new Coordinate(new Double("575000.000"), new Double("150000.000"));
			ArcSegment seg=new ArcSegment(new Coordinate(550000.000,100000.000),arcpt,new Coordinate(550000.000,200000.000));
			coords[2]=seg.getStartPoint();
			coords[3]=seg.getMidPoint();
			coords[4]=seg.getEndPoint();
			
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("200000.000"));
			coords[5]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.000"), new Double("100000.000"));
			coords[6]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		polygon1=new Polygon(shell, null, new PrecisionModel(), 2056);
		
		Polygon polygon2=null;
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[7];
			com.vividsolutions.jts.geom.Coordinate coord1=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("100000.000"));
			coords[0]=coord1;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("550000.000"), new Double("100000.000"));
			coords[1]=coord2;
			// coords[2]= ARC:
			// ARC  (new Double("575000.000"), new Double("150000.000"))
			// COORD(new Double("550000.000"), new Double("200000.000"))
			
			Coordinate arcpt=new Coordinate(new Double("575000.000"), new Double("150000.000"));
			ArcSegment seg=new ArcSegment(new Coordinate(550000.000,100000.000),arcpt,new Coordinate(550000.000,200000.000));

			coords[2]=seg.getStartPoint();
			coords[3]=seg.getMidPoint();
			coords[4]=seg.getEndPoint();
			
			com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("200000.000"));
			coords[5]=coord4;
			com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("600000.000"), new Double("100000.000"));
			coords[6]=coord5;
			shell=new LinearRing(coords, new PrecisionModel(), 2056);
		}
		polygon2=new Polygon(shell, null, new PrecisionModel(), 2056);

		boolean isOverlay=CurvePolygon.polygonOverlays(polygon1, polygon2);
		//System.out.println(isOverlay);
		assertEquals(false, isOverlay);
	}
}