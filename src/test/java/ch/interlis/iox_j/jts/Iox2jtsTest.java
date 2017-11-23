package ch.interlis.iox_j.jts;

import static org.junit.Assert.*;
import org.junit.Test;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;

public class Iox2jtsTest{
		
	private Iox2jts conv=new Iox2jts();
	
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
		// convert
		MultiPoint jtsMultiPoint=conv.multicoord2JTS(multicoord);
		// test
		assertEquals(2,jtsMultiPoint.getNumPoints());
		assertEquals("POINT (10 11)", jtsMultiPoint.getGeometryN(0).toString());
		assertEquals("POINT (20 21)", jtsMultiPoint.getGeometryN(1).toString());
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
		// convert
		MultiPoint jtsMultiPoint=conv.multicoord2JTS(multicoord);
		// test
		assertEquals(2,jtsMultiPoint.getNumPoints());
		Coordinate[] coordinates=jtsMultiPoint.getCoordinates();
		assertEquals(new com.vividsolutions.jts.geom.Coordinate(10.0, 11.0, 12.0),coordinates[0]);
		assertEquals(new com.vividsolutions.jts.geom.Coordinate(20.0, 21.0, 22.0),coordinates[1]);
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
		// convert
		MultiLineString jtsMultiLineString=conv.multipolyline2JTS(multiPolyline, 0);
		// test
		assertEquals(3,jtsMultiLineString.getNumGeometries());
		assertEquals("LINESTRING (10 11, 20 21)", jtsMultiLineString.getGeometryN(0).toString());
		assertEquals("LINESTRING (30 31, 40 41)", jtsMultiLineString.getGeometryN(1).toString());
		assertEquals("LINESTRING (50 51, 60 61)", jtsMultiLineString.getGeometryN(2).toString());
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
		// convert
		MultiPolygon jtsMultipolygon=conv.multisurface2JTS(multiSurface, 0, 2056);
		// test
		assertEquals(2,jtsMultipolygon.getNumGeometries());
		assertEquals("POLYGON ((480000 70000, 500000 80000, 500000 80000, 550000 90000, 550000 90000, 480000 70000), (500000 77000, 500000 78000, 500000 78000, 505000 78000, 505000 78000, 500000 77000))", jtsMultipolygon.getGeometryN(0).toString());
		assertEquals("POLYGON ((480000.001 70000.001, 500000.001 80000.001, 500000.001 80000.001, 550000.001 90000.001, 550000.001 90000.001, 480000.001 70000.001), (500000.001 77000.001, 500000.001 78000.001, 500000.001 78000.001, 505000.001 78000.001, 505000.001 78000.001, 500000.001 77000.001))", jtsMultipolygon.getGeometryN(1).toString());
	}
}