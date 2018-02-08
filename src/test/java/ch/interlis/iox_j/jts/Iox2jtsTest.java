package ch.interlis.iox_j.jts;

import static org.junit.Assert.*;
import org.junit.Test;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.IoxException;

public class Iox2jtsTest{
		
	private Iox2jts conv=new Iox2jts();
	private com.vividsolutions.jts.geom.Coordinate[] coords=null;
	
	// Es wird getestet ob ein iox Multicoord(2d) erfolgreich in ein JTS MultiPoint(2d) konvertiert werden kann.
	@Test
	public void convert_Multicoord2MultiPoint2d_Ok() throws Exception{
		// interlis object
		IomObject multicoord=new Iom_jObject("multicoord", "MULTICOORD");
		IomObject coordStart=multicoord.addattrobj("coord", "COORD");
		IomObject coordEnd=multicoord.addattrobj("coord", "COORD");
		coordStart.setattrvalue("C1", "10");
		coordStart.setattrvalue("C2", "11");
		coordEnd.setattrvalue("C1", "20");
		coordEnd.setattrvalue("C2", "21");
		try {
			// convert
			MultiPoint jtsMultiPoint=conv.multicoord2JTS(multicoord);
			// test
			assertEquals(2,jtsMultiPoint.getNumPoints());
			Coordinate[] coordinates=jtsMultiPoint.getCoordinates();
			assertEquals(new com.vividsolutions.jts.geom.Coordinate(10.0, 11.0),coordinates[0]);
			assertEquals(new com.vividsolutions.jts.geom.Coordinate(20.0, 21.0),coordinates[1]);
		}catch(Iox2jtsException e) {
			throw new IoxException(e);
		}
	}
	
	// Es wird getestet ob ein iox Multicoord(3d) erfolgreich in ein JTS MultiPoint(3d) konvertiert werden kann.
	// ACHTUNG: In einem Point Element ist nur die x und y Dimension sichtbar. Um die z Dimension zu erhalten, muss getCoordinates() aufgerufen werden.
	@Test
	public void convert_Multicoord2MultiPoint3d_Ok() throws Exception{
		// interlis object
		IomObject multicoord=new Iom_jObject("multicoord", "MULTICOORD");
		IomObject coordStart=multicoord.addattrobj("coord", "COORD");
		IomObject coordEnd=multicoord.addattrobj("coord", "COORD");
		coordStart.setattrvalue("C1", "10");
		coordStart.setattrvalue("C2", "11");
		coordStart.setattrvalue("C3", "12");
		coordEnd.setattrvalue("C1", "20");
		coordEnd.setattrvalue("C2", "21");
		coordEnd.setattrvalue("C3", "22");
		try {
			// convert
			MultiPoint jtsMultiPoint=conv.multicoord2JTS(multicoord);
			// test
			assertEquals(2,jtsMultiPoint.getNumPoints());
			Coordinate[] coordinates=jtsMultiPoint.getCoordinates();
			assertEquals(new com.vividsolutions.jts.geom.Coordinate(10.0, 11.0, 12.0),coordinates[0]);
			assertEquals(new com.vividsolutions.jts.geom.Coordinate(20.0, 21.0, 22.0),coordinates[1]);
		}catch(Iox2jtsException e) {
			throw new IoxException(e);
		}
	}
	
	// Es wird getestet ob ein iox Multipolyline erfolgreich in ein JTS MultiLineString konvertiert werden kann.
	@Test
	public void convert_Multipolyline2MultiLineString_Ok() throws Exception{
		// multipolyline
		IomObject multiPolyline = new Iom_jObject("model.topic.class1", "oid1");
		// polyline
		IomObject polylineValue = multiPolyline.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "10");
		startSegment.setattrvalue("C2", "11");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "20");
		endSegment.setattrvalue("C2", "21");
		// polyline 2
		IomObject polylineValue2 = multiPolyline.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "30");
		startSegment2.setattrvalue("C2", "31");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "40");
		endSegment2.setattrvalue("C2", "41");
		// polyline 3
		IomObject polylineValue3 = multiPolyline.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "50");
		startSegment3.setattrvalue("C2", "51");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "60");
		endSegment3.setattrvalue("C2", "61");
		try {
			// convert
			MultiLineString jtsMultiLineString=conv.multipolyline2JTS(multiPolyline, 0);
			// test
			assertEquals(3,jtsMultiLineString.getNumGeometries());
	
			Geometry line1=jtsMultiLineString.getGeometryN(0);
			coords=line1.getCoordinates();
			assertEquals(1, line1.getNumGeometries());
			{
				com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("10"), new Double("11"));
				assertEquals(coord, coords[0]);
				com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("20"), new Double("21"));
				assertEquals(coord2, coords[1]);
			}
			Geometry line2=jtsMultiLineString.getGeometryN(1);
			coords=line2.getCoordinates();
			assertEquals(1, line2.getNumGeometries());
			{
				com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("30"), new Double("31"));
				assertEquals(coord, coords[0]);
				com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("40"), new Double("41"));
				assertEquals(coord2, coords[1]);
			}
			Geometry line3=jtsMultiLineString.getGeometryN(2);
			coords=line3.getCoordinates();
			assertEquals(1, line3.getNumGeometries());
			{
				com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("50"), new Double("51"));
				assertEquals(coord, coords[0]);
				com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("60"), new Double("61"));
				assertEquals(coord2, coords[1]);
			}
		}catch(Iox2jtsException e) {
			throw new IoxException(e);
		}
	}

	// Es wird getestet ob ein iox Multisurface erfolgreich in ein JTS MultiPolygon konvertiert werden kann.
	@Test
	public void convert_Multisurface2MultiPolygon_Ok() throws Exception{
		IomObject multiSurfaceValue=new Iom_jObject("model.topic.class1", "oid1");
		IomObject multiSurface = multiSurfaceValue.addattrobj("multisurface", "MULTISURFACE");
		{
			IomObject surfaceValue = multiSurface.addattrobj("surface", "SURFACE");
			// outer boundary
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "480000.000");
			startSegment.setattrvalue("C2", "70000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "500000.000");
			endSegment.setattrvalue("C2", "80000.000");
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2=segments2.addattrobj("segment", "COORD");
			startSegment2.setattrvalue("C1", "500000.000");
			startSegment2.setattrvalue("C2", "80000.000");
			IomObject endSegment2=segments2.addattrobj("segment", "COORD");
			endSegment2.setattrvalue("C1", "550000.000");
			endSegment2.setattrvalue("C2", "90000.000");
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3=segments3.addattrobj("segment", "COORD");
			startSegment3.setattrvalue("C1", "550000.000");
			startSegment3.setattrvalue("C2", "90000.000");
			IomObject endSegment3=segments3.addattrobj("segment", "COORD");
			endSegment3.setattrvalue("C1", "480000.000");
			endSegment3.setattrvalue("C2", "70000.000");
			// inner boundary
			IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
			IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
			startSegmentInner.setattrvalue("C1", "500000.000");
			startSegmentInner.setattrvalue("C2", "77000.000");
			IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
			endSegmentInner.setattrvalue("C1", "500000.000");
			endSegmentInner.setattrvalue("C2", "78000.000");
			// polyline 2
			IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
			startSegment2Inner.setattrvalue("C1", "500000.000");
			startSegment2Inner.setattrvalue("C2", "78000.000");
			IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
			endSegment2Inner.setattrvalue("C1", "505000.000");
			endSegment2Inner.setattrvalue("C2", "78000.000");
			// polyline 3
			IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
			startSegment3Inner.setattrvalue("C1", "505000.000");
			startSegment3Inner.setattrvalue("C2", "78000.000");
			IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
			endSegment3Inner.setattrvalue("C1", "500000.000");
			endSegment3Inner.setattrvalue("C2", "77000.000");
		}
		IomObject surfaceValue = multiSurface.addattrobj("surface", "SURFACE");
		{
			// outer boundary
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "480000.001");
			startSegment.setattrvalue("C2", "70000.001");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "500000.001");
			endSegment.setattrvalue("C2", "80000.001");
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2=segments2.addattrobj("segment", "COORD");
			startSegment2.setattrvalue("C1", "500000.001");
			startSegment2.setattrvalue("C2", "80000.001");
			IomObject endSegment2=segments2.addattrobj("segment", "COORD");
			endSegment2.setattrvalue("C1", "550000.001");
			endSegment2.setattrvalue("C2", "90000.001");
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3=segments3.addattrobj("segment", "COORD");
			startSegment3.setattrvalue("C1", "550000.001");
			startSegment3.setattrvalue("C2", "90000.001");
			IomObject endSegment3=segments3.addattrobj("segment", "COORD");
			endSegment3.setattrvalue("C1", "480000.001");
			endSegment3.setattrvalue("C2", "70000.001");
			// inner boundary
			IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
			IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
			startSegmentInner.setattrvalue("C1", "500000.001");
			startSegmentInner.setattrvalue("C2", "77000.001");
			IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
			endSegmentInner.setattrvalue("C1", "500000.001");
			endSegmentInner.setattrvalue("C2", "78000.001");
			// polyline 2
			IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
			startSegment2Inner.setattrvalue("C1", "500000.001");
			startSegment2Inner.setattrvalue("C2", "78000.001");
			IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
			endSegment2Inner.setattrvalue("C1", "505000.001");
			endSegment2Inner.setattrvalue("C2", "78000.001");
			// polyline 3
			IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
			startSegment3Inner.setattrvalue("C1", "505000.001");
			startSegment3Inner.setattrvalue("C2", "78000.001");
			IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
			endSegment3Inner.setattrvalue("C1", "500000.001");
			endSegment3Inner.setattrvalue("C2", "77000.001");
		}
		try {
			// convert
			MultiPolygon jtsMultipolygon=conv.multisurface2JTS(multiSurface, 0, 2056);
			// multi polygon
			assertEquals(2,jtsMultipolygon.getNumGeometries());
			// polygon1
			Geometry polygon1=jtsMultipolygon.getGeometryN(0);
			assertEquals(1,polygon1.getNumGeometries());
			coords=polygon1.getCoordinates();
			{
				com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("480000"), new Double("70000"));
				assertEquals(coord, coords[0]);
				com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("500000"), new Double("80000"));
				assertEquals(coord2, coords[1]);
				com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("500000"), new Double("80000"));
				assertEquals(coord3, coords[2]);
				com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("550000"), new Double("90000"));
				assertEquals(coord4, coords[3]);
				com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("550000"), new Double("90000"));
				assertEquals(coord5, coords[4]);
				com.vividsolutions.jts.geom.Coordinate coord6=new com.vividsolutions.jts.geom.Coordinate(new Double("480000"), new Double("70000"));
				assertEquals(coord6, coords[5]);
				com.vividsolutions.jts.geom.Coordinate coord7=new com.vividsolutions.jts.geom.Coordinate(new Double("500000"), new Double("77000"));
				assertEquals(coord7, coords[6]);
				com.vividsolutions.jts.geom.Coordinate coord8=new com.vividsolutions.jts.geom.Coordinate(new Double("500000"), new Double("78000"));
				assertEquals(coord8, coords[7]);
				com.vividsolutions.jts.geom.Coordinate coord9=new com.vividsolutions.jts.geom.Coordinate(new Double("500000"), new Double("78000"));
				assertEquals(coord9, coords[8]);
				com.vividsolutions.jts.geom.Coordinate coord10=new com.vividsolutions.jts.geom.Coordinate(new Double("505000"), new Double("78000"));
				assertEquals(coord10, coords[9]);
				com.vividsolutions.jts.geom.Coordinate coord11=new com.vividsolutions.jts.geom.Coordinate(new Double("505000"), new Double("78000"));
				assertEquals(coord11, coords[10]);
				com.vividsolutions.jts.geom.Coordinate coord12=new com.vividsolutions.jts.geom.Coordinate(new Double("500000"), new Double("77000"));
				assertEquals(coord12, coords[11]);
			}
			// polygon2
			Geometry polygon2=jtsMultipolygon.getGeometryN(1);
			assertEquals(1,polygon2.getNumGeometries());
			coords=polygon2.getCoordinates();
			{
				com.vividsolutions.jts.geom.Coordinate coord=new com.vividsolutions.jts.geom.Coordinate(new Double("480000.001"), new Double("70000.001"));
				assertEquals(coord, coords[0]);
				com.vividsolutions.jts.geom.Coordinate coord2=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.001"), new Double("80000.001"));
				assertEquals(coord2, coords[1]);
				com.vividsolutions.jts.geom.Coordinate coord3=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.001"), new Double("80000.001"));
				assertEquals(coord3, coords[2]);
				com.vividsolutions.jts.geom.Coordinate coord4=new com.vividsolutions.jts.geom.Coordinate(new Double("550000.001"), new Double("90000.001"));
				assertEquals(coord4, coords[3]);
				com.vividsolutions.jts.geom.Coordinate coord5=new com.vividsolutions.jts.geom.Coordinate(new Double("550000.001"), new Double("90000.001"));
				assertEquals(coord5, coords[4]);
				com.vividsolutions.jts.geom.Coordinate coord6=new com.vividsolutions.jts.geom.Coordinate(new Double("480000.001"), new Double("70000.001"));
				assertEquals(coord6, coords[5]);
				com.vividsolutions.jts.geom.Coordinate coord7=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.001"), new Double("77000.001"));
				assertEquals(coord7, coords[6]);
				com.vividsolutions.jts.geom.Coordinate coord8=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.001"), new Double("78000.001"));
				assertEquals(coord8, coords[7]);
				com.vividsolutions.jts.geom.Coordinate coord9=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.001"), new Double("78000.001"));
				assertEquals(coord9, coords[8]);
				com.vividsolutions.jts.geom.Coordinate coord10=new com.vividsolutions.jts.geom.Coordinate(new Double("505000.001"), new Double("78000.001"));
				assertEquals(coord10, coords[9]);
				com.vividsolutions.jts.geom.Coordinate coord11=new com.vividsolutions.jts.geom.Coordinate(new Double("505000.001"), new Double("78000.001"));
				assertEquals(coord11, coords[10]);
				com.vividsolutions.jts.geom.Coordinate coord12=new com.vividsolutions.jts.geom.Coordinate(new Double("500000.001"), new Double("77000.001"));
				assertEquals(coord12, coords[11]);
			}
		}catch(Iox2jtsException e) {
			throw new IoxException(e);
		}
	}
}