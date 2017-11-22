package ch.interlis.iox_j.jts;

import static org.junit.Assert.*;
import org.junit.Assert;
import org.junit.Test;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import ch.interlis.iom.IomObject;

public class Jts2ioxTest {
	
	private com.vividsolutions.jts.geom.Coordinate[] coords=null;
	
	// Es wird getestet ob ein JTS MultiPoint(2d) erfolgreich in ein iox MultiCoord(2d) konvertiert werden kann.
	@Test
	public void convert_Jts2Multicoord_Ok() throws Exception{
		coords=new com.vividsolutions.jts.geom.Coordinate[3];
		com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("10"), new Double("11"));
		coords[0]=coord;
		com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("20"), new Double("21"));
		coords[1]=coord2;
		com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("30"), new Double("31"));
		coords[2]=coord3;
		// convert
		Jts2iox conv=new Jts2iox();
		IomObject multiCoord=conv.JTS2multicoord(coords);
		// test
		Assert.assertEquals(3,multiCoord.getattrvaluecount("coord"));
		IomObject iliCoord1=multiCoord.getattrobj("coord", 0);
		Assert.assertEquals("10.0", iliCoord1.getattrvalue("C1"));
		Assert.assertEquals("11.0", iliCoord1.getattrvalue("C2"));
		
		IomObject iliCoord2=multiCoord.getattrobj("coord", 1);
		Assert.assertEquals("20.0", iliCoord2.getattrvalue("C1"));
		Assert.assertEquals("21.0", iliCoord2.getattrvalue("C2"));
		
		IomObject iliCoord3=multiCoord.getattrobj("coord", 2);
		Assert.assertEquals("30.0", iliCoord3.getattrvalue("C1"));
		Assert.assertEquals("31.0", iliCoord3.getattrvalue("C2"));
	}
	
	// Es wird getestet ob ein JTS MultiPoint(3d) erfolgreich in ein iox MultiCoord(3d) konvertiert werden kann.
	@Test
	public void convert_Jts2Multicoord3d_Ok() throws Exception{
		coords=new com.vividsolutions.jts.geom.Coordinate[3];
		com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("10"), new Double("11"), new Double("12"));
		coords[0]=coord;
		com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("20"), new Double("21"), new Double("22"));
		coords[1]=coord2;
		com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("30"), new Double("31"), new Double("32"));
		coords[2]=coord3;
		// convert
		Jts2iox conv=new Jts2iox();
		IomObject multiCoord=conv.JTS2multicoord(coords);
		// test
		Assert.assertEquals(3,multiCoord.getattrvaluecount("coord"));
		IomObject iliCoord1=multiCoord.getattrobj("coord", 0);
		Assert.assertEquals("10.0", iliCoord1.getattrvalue("C1"));
		Assert.assertEquals("11.0", iliCoord1.getattrvalue("C2"));
		Assert.assertEquals("12.0", iliCoord1.getattrvalue("C3"));
		
		IomObject iliCoord2=multiCoord.getattrobj("coord", 1);
		Assert.assertEquals("20.0", iliCoord2.getattrvalue("C1"));
		Assert.assertEquals("21.0", iliCoord2.getattrvalue("C2"));
		Assert.assertEquals("22.0", iliCoord2.getattrvalue("C3"));
		
		IomObject iliCoord3=multiCoord.getattrobj("coord", 2);
		Assert.assertEquals("30.0", iliCoord3.getattrvalue("C1"));
		Assert.assertEquals("31.0", iliCoord3.getattrvalue("C2"));
		Assert.assertEquals("32.0", iliCoord3.getattrvalue("C3"));
	}
	
	// Es wird getestet ob ein JTS MultiLineString erfolgreich in ein iox Multipolyline konvertiert werden kann.
	@Test
	public void convert_Jts2MultiLineString_Ok() throws Exception{
		Jts2iox conv=new Jts2iox();
		LineString[] lines=new LineString[2];
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[3];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("10"), new Double("11"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("20"), new Double("21"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("30"), new Double("31"));
			coords[2]=coord3;
			LineString line=new LineString(coords, new PrecisionModel(), 2056);
			lines[0]=line;
		}
		{
			coords=new com.vividsolutions.jts.geom.Coordinate[3];
			com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("40"), new Double("41"));
			coords[0]=coord;
			com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("50"), new Double("51"));
			coords[1]=coord2;
			com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("60"), new Double("61"));
			coords[2]=coord3;
			LineString line=new LineString(coords, new PrecisionModel(), 2056);
			lines[1]=line;
		}
		// convert
		MultiLineString multiLineString=new MultiLineString(lines, new PrecisionModel(), 2056);
		IomObject multiPolyline=conv.JTS2multipolyline(multiLineString);
		// test
		Assert.assertEquals(2,multiPolyline.getattrvaluecount("polyline"));
		IomObject iliLine=multiPolyline.getattrobj("polyline", 0);
		{
			Assert.assertEquals(1,iliLine.getattrvaluecount("sequence"));
			IomObject iliSequence=iliLine.getattrobj("sequence", 0);
			
			Assert.assertEquals(3,iliSequence.getattrvaluecount("segment"));
			IomObject iliCoord1=iliSequence.getattrobj("segment", 0);
			Assert.assertEquals("10.0", iliCoord1.getattrvalue("C1"));
			Assert.assertEquals("11.0", iliCoord1.getattrvalue("C2"));
			IomObject iliCoord2=iliSequence.getattrobj("segment", 1);
			Assert.assertEquals("20.0", iliCoord2.getattrvalue("C1"));
			Assert.assertEquals("21.0", iliCoord2.getattrvalue("C2"));
			IomObject iliCoord3=iliSequence.getattrobj("segment", 2);
			Assert.assertEquals("30.0", iliCoord3.getattrvalue("C1"));
			Assert.assertEquals("31.0", iliCoord3.getattrvalue("C2"));
		}
		IomObject iliLine2=multiPolyline.getattrobj("polyline", 1);
		{
			Assert.assertEquals(1,iliLine2.getattrvaluecount("sequence"));
			IomObject iliSequence=iliLine2.getattrobj("sequence", 0);
			
			Assert.assertEquals(3,iliSequence.getattrvaluecount("segment"));
			IomObject iliCoord1=iliSequence.getattrobj("segment", 0);
			Assert.assertEquals("40.0", iliCoord1.getattrvalue("C1"));
			Assert.assertEquals("41.0", iliCoord1.getattrvalue("C2"));
			IomObject iliCoord2=iliSequence.getattrobj("segment", 1);
			Assert.assertEquals("50.0", iliCoord2.getattrvalue("C1"));
			Assert.assertEquals("51.0", iliCoord2.getattrvalue("C2"));
			IomObject iliCoord3=iliSequence.getattrobj("segment", 2);
			Assert.assertEquals("60.0", iliCoord3.getattrvalue("C1"));
			Assert.assertEquals("61.0", iliCoord3.getattrvalue("C2"));
		}
	}
	
	// Es wird getestet ob ein JTS MultiPolygon erfolgreich in ein iox Multisurface konvertiert werden kann.
	@Test
	public void convert_Jts2Polygon_Ok() throws Exception{
		Jts2iox conv=new Jts2iox();
		Polygon[] polygons=new Polygon[2];
		{
			LinearRing shell=null;
			LinearRing[] holes=null;
			holes=new LinearRing[1];
			Polygon polygon1=null;
			{
				coords=new com.vividsolutions.jts.geom.Coordinate[8];
				com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("10"), new Double("11"));
				coords[0]=coord;
				com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("20"), new Double("21"));
				coords[1]=coord2;
				com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("20"), new Double("21"));
				coords[2]=coord3;
				com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("30"), new Double("31"));
				coords[3]=coord4;
				com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("30"), new Double("31"));
				coords[4]=coord5;
				com.vividsolutions.jts.geom.Coordinate coord6=new com.vividsolutions.jts.geom.Coordinate(new Double("40"), new Double("41"));
				coords[5]=coord6;
				com.vividsolutions.jts.geom.Coordinate coord7=new com.vividsolutions.jts.geom.Coordinate(new Double("40"), new Double("41"));
				coords[6]=coord7;
				com.vividsolutions.jts.geom.Coordinate coord8=new com.vividsolutions.jts.geom.Coordinate(new Double("10"), new Double("11"));
				coords[7]=coord8;
				shell=new LinearRing(coords, new PrecisionModel(), 2056);
			}
			{
				coords=new com.vividsolutions.jts.geom.Coordinate[8];
				com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("100"), new Double("110"));
				coords[0]=coord;
				com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("200"), new Double("210"));
				coords[1]=coord2;
				com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("200"), new Double("210"));
				coords[2]=coord3;
				com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("300"), new Double("310"));
				coords[3]=coord4;
				com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("300"), new Double("310"));
				coords[4]=coord5;
				com.vividsolutions.jts.geom.Coordinate coord6=new com.vividsolutions.jts.geom.Coordinate(new Double("400"), new Double("410"));
				coords[5]=coord6;
				com.vividsolutions.jts.geom.Coordinate coord7=new com.vividsolutions.jts.geom.Coordinate(new Double("400"), new Double("410"));
				coords[6]=coord7;
				com.vividsolutions.jts.geom.Coordinate coord8=new com.vividsolutions.jts.geom.Coordinate(new Double("100"), new Double("110"));
				coords[7]=coord8;
				LinearRing ring=new LinearRing(coords, new PrecisionModel(), 2056);
				holes[0]=ring;
			}
			polygon1=new Polygon(shell, holes, new PrecisionModel(), 2056);
			polygons[0]=polygon1;
		}
		{
			LinearRing shell=null;
			LinearRing[] holes=null;
			holes=new LinearRing[1];
			Polygon polygon1=null;
			{
				coords=new com.vividsolutions.jts.geom.Coordinate[8];
				com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("10"), new Double("11"));
				coords[0]=coord;
				com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("20"), new Double("21"));
				coords[1]=coord2;
				com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("20"), new Double("21"));
				coords[2]=coord3;
				com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("30"), new Double("31"));
				coords[3]=coord4;
				com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("30"), new Double("31"));
				coords[4]=coord5;
				com.vividsolutions.jts.geom.Coordinate coord6=new com.vividsolutions.jts.geom.Coordinate(new Double("40"), new Double("41"));
				coords[5]=coord6;
				com.vividsolutions.jts.geom.Coordinate coord7=new com.vividsolutions.jts.geom.Coordinate(new Double("40"), new Double("41"));
				coords[6]=coord7;
				com.vividsolutions.jts.geom.Coordinate coord8=new com.vividsolutions.jts.geom.Coordinate(new Double("10"), new Double("11"));
				coords[7]=coord8;
				shell=new LinearRing(coords, new PrecisionModel(), 2056);
			}
			{
				coords=new com.vividsolutions.jts.geom.Coordinate[8];
				com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("100"), new Double("110"));
				coords[0]=coord;
				com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("200"), new Double("210"));
				coords[1]=coord2;
				com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("200"), new Double("210"));
				coords[2]=coord3;
				com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("300"), new Double("310"));
				coords[3]=coord4;
				com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("300"), new Double("310"));
				coords[4]=coord5;
				com.vividsolutions.jts.geom.Coordinate coord6=new com.vividsolutions.jts.geom.Coordinate(new Double("400"), new Double("410"));
				coords[5]=coord6;
				com.vividsolutions.jts.geom.Coordinate coord7=new com.vividsolutions.jts.geom.Coordinate(new Double("400"), new Double("410"));
				coords[6]=coord7;
				com.vividsolutions.jts.geom.Coordinate coord8=new com.vividsolutions.jts.geom.Coordinate(new Double("100"), new Double("110"));
				coords[7]=coord8;
				LinearRing ring=new LinearRing(coords, new PrecisionModel(), 2056);
				holes[0]=ring;
			}
			polygon1=new Polygon(shell, holes, new PrecisionModel(), 2056);
			polygons[1]=polygon1;
		}
		MultiPolygon multiPolygon=new MultiPolygon(polygons, new PrecisionModel(), 2056);
		// convert
		IomObject iomObj=conv.JTS2multisurface(multiPolygon);
		// test
		IomObject surfaceValue = iomObj.getattrobj("surface",0);	
		
		// outer boundary
		IomObject outerBoundary = surfaceValue.getattrobj("boundary",0);
		{
			// polyline1
			IomObject polylineValue = outerBoundary.getattrobj("polyline",0);
			{
				IomObject segments=polylineValue.getattrobj("sequence",0);
				IomObject startSegment1=segments.getattrobj("segment",0);
				assertEquals("10.0",startSegment1.getattrvalue("C1"));
				assertEquals("11.0",startSegment1.getattrvalue("C2"));
				IomObject endSegment2=segments.getattrobj("segment",1);
				assertEquals("20.0",endSegment2.getattrvalue("C1"));
				assertEquals("21.0",endSegment2.getattrvalue("C2"));
				IomObject startSegment3=segments.getattrobj("segment",2);
				assertEquals("20.0",startSegment3.getattrvalue("C1"));
				assertEquals("21.0",startSegment3.getattrvalue("C2"));
				IomObject endSegment4=segments.getattrobj("segment",3);
				assertEquals("30.0",endSegment4.getattrvalue("C1"));
				assertEquals("31.0",endSegment4.getattrvalue("C2"));
				IomObject startSegment5=segments.getattrobj("segment",4);
				assertEquals("30.0",startSegment5.getattrvalue("C1"));
				assertEquals("31.0",startSegment5.getattrvalue("C2"));
				IomObject endSegment6=segments.getattrobj("segment",5);
				assertEquals("40.0",endSegment6.getattrvalue("C1"));
				assertEquals("41.0",endSegment6.getattrvalue("C2"));
				IomObject startSegment7=segments.getattrobj("segment",6);
				assertEquals("40.0",startSegment7.getattrvalue("C1"));
				assertEquals("41.0",startSegment7.getattrvalue("C2"));
				IomObject endSegment8=segments.getattrobj("segment",7);
				assertEquals("10.0",endSegment8.getattrvalue("C1"));
				assertEquals("11.0",endSegment8.getattrvalue("C2"));
			}
			IomObject outerBoundary2 = surfaceValue.getattrobj("boundary",1);
			// polyline 2
			IomObject polylineValue2 = outerBoundary2.getattrobj("polyline",0);
			{
				IomObject segments=polylineValue2.getattrobj("sequence",0);
				IomObject startSegment1=segments.getattrobj("segment",0);
				assertEquals("100.0",startSegment1.getattrvalue("C1"));
				assertEquals("110.0",startSegment1.getattrvalue("C2"));
				IomObject endSegment2=segments.getattrobj("segment",1);
				assertEquals("200.0",endSegment2.getattrvalue("C1"));
				assertEquals("210.0",endSegment2.getattrvalue("C2"));
				IomObject startSegment3=segments.getattrobj("segment",2);
				assertEquals("200.0",startSegment3.getattrvalue("C1"));
				assertEquals("210.0",startSegment3.getattrvalue("C2"));
				IomObject endSegment4=segments.getattrobj("segment",3);
				assertEquals("300.0",endSegment4.getattrvalue("C1"));
				assertEquals("310.0",endSegment4.getattrvalue("C2"));
				IomObject startSegment5=segments.getattrobj("segment",4);
				assertEquals("300.0",startSegment5.getattrvalue("C1"));
				assertEquals("310.0",startSegment5.getattrvalue("C2"));
				IomObject endSegment6=segments.getattrobj("segment",5);
				assertEquals("400.0",endSegment6.getattrvalue("C1"));
				assertEquals("410.0",endSegment6.getattrvalue("C2"));
				IomObject startSegment7=segments.getattrobj("segment",6);
				assertEquals("400.0",startSegment7.getattrvalue("C1"));
				assertEquals("410.0",startSegment7.getattrvalue("C2"));
				IomObject endSegment8=segments.getattrobj("segment",7);
				assertEquals("100.0",endSegment8.getattrvalue("C1"));
				assertEquals("110.0",endSegment8.getattrvalue("C2"));
			}
		}
		IomObject surfaceValue2 = iomObj.getattrobj("surface",1);	
		// outer boundary
		IomObject outerBoundary2 = surfaceValue2.getattrobj("boundary",0);
		{
			// polyline1
			IomObject polylineValue = outerBoundary2.getattrobj("polyline",0);
			{
				IomObject segments=polylineValue.getattrobj("sequence",0);
				IomObject startSegment1=segments.getattrobj("segment",0);
				assertEquals("10.0",startSegment1.getattrvalue("C1"));
				assertEquals("11.0",startSegment1.getattrvalue("C2"));
				IomObject endSegment2=segments.getattrobj("segment",1);
				assertEquals("20.0",endSegment2.getattrvalue("C1"));
				assertEquals("21.0",endSegment2.getattrvalue("C2"));
				IomObject startSegment3=segments.getattrobj("segment",2);
				assertEquals("20.0",startSegment3.getattrvalue("C1"));
				assertEquals("21.0",startSegment3.getattrvalue("C2"));
				IomObject endSegment4=segments.getattrobj("segment",3);
				assertEquals("30.0",endSegment4.getattrvalue("C1"));
				assertEquals("31.0",endSegment4.getattrvalue("C2"));
				IomObject startSegment5=segments.getattrobj("segment",4);
				assertEquals("30.0",startSegment5.getattrvalue("C1"));
				assertEquals("31.0",startSegment5.getattrvalue("C2"));
				IomObject endSegment6=segments.getattrobj("segment",5);
				assertEquals("40.0",endSegment6.getattrvalue("C1"));
				assertEquals("41.0",endSegment6.getattrvalue("C2"));
				IomObject startSegment7=segments.getattrobj("segment",6);
				assertEquals("40.0",startSegment7.getattrvalue("C1"));
				assertEquals("41.0",startSegment7.getattrvalue("C2"));
				IomObject endSegment8=segments.getattrobj("segment",7);
				assertEquals("10.0",endSegment8.getattrvalue("C1"));
				assertEquals("11.0",endSegment8.getattrvalue("C2"));
			}
			IomObject outerBoundary3 = surfaceValue2.getattrobj("boundary",1);
			// polyline 2
			IomObject polylineValue3 = outerBoundary3.getattrobj("polyline",0);
			{
				IomObject segments=polylineValue3.getattrobj("sequence",0);
				IomObject startSegment1=segments.getattrobj("segment",0);
				assertEquals("100.0",startSegment1.getattrvalue("C1"));
				assertEquals("110.0",startSegment1.getattrvalue("C2"));
				IomObject endSegment2=segments.getattrobj("segment",1);
				assertEquals("200.0",endSegment2.getattrvalue("C1"));
				assertEquals("210.0",endSegment2.getattrvalue("C2"));
				IomObject startSegment3=segments.getattrobj("segment",2);
				assertEquals("200.0",startSegment3.getattrvalue("C1"));
				assertEquals("210.0",startSegment3.getattrvalue("C2"));
				IomObject endSegment4=segments.getattrobj("segment",3);
				assertEquals("300.0",endSegment4.getattrvalue("C1"));
				assertEquals("310.0",endSegment4.getattrvalue("C2"));
				IomObject startSegment5=segments.getattrobj("segment",4);
				assertEquals("300.0",startSegment5.getattrvalue("C1"));
				assertEquals("310.0",startSegment5.getattrvalue("C2"));
				IomObject endSegment6=segments.getattrobj("segment",5);
				assertEquals("400.0",endSegment6.getattrvalue("C1"));
				assertEquals("410.0",endSegment6.getattrvalue("C2"));
				IomObject startSegment7=segments.getattrobj("segment",6);
				assertEquals("400.0",startSegment7.getattrvalue("C1"));
				assertEquals("410.0",startSegment7.getattrvalue("C2"));
				IomObject endSegment8=segments.getattrobj("segment",7);
				assertEquals("100.0",endSegment8.getattrvalue("C1"));
				assertEquals("110.0",endSegment8.getattrvalue("C2"));
			}
		}
	}	
}