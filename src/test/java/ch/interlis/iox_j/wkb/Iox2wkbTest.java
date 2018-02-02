package ch.interlis.iox_j.wkb;

import static org.junit.Assert.*;
import org.junit.Test;

import com.vividsolutions.jts.geom.MultiPolygon;

import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;


public class Iox2wkbTest {
    private Iox2wkb conv=new Iox2wkb(2,java.nio.ByteOrder.BIG_ENDIAN);

    @Test
    public void surface_Ok() throws Exception{
        IomObject multiSurface=new Iom_jObject("MULTISURFACE", null);
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
            IomObject endSegment2=segments.addattrobj("segment", "COORD");
            endSegment2.setattrvalue("C1", "550000.000");
            endSegment2.setattrvalue("C2", "90000.000");
            IomObject endSegment3=segments.addattrobj("segment", "COORD");
            endSegment3.setattrvalue("C1", "480000.000");
            endSegment3.setattrvalue("C2", "70000.000");
        }
        // convert
        byte wkb[]=conv.surface2wkb(multiSurface, true, 0.0);
        // verify
        String wkbText=javax.xml.bind.DatatypeConverter.printHexBinary(wkb);
        assertEquals("000000000A00000001000000000900000001000000000200000004411D4C000000000040F1170000000000411E84800000000040F38800000000004120C8E00000000040F5F90000000000411D4C000000000040F1170000000000",wkbText);
    }
    @Test
    public void surfaceMultiplePoylines_Ok() throws Exception{
        IomObject multiSurface=new Iom_jObject("MULTISURFACE", null);
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
        }
        // convert
        byte wkb[]=conv.surface2wkb(multiSurface, true, 0.0);
        // verify
        String wkbText=javax.xml.bind.DatatypeConverter.printHexBinary(wkb);
        assertEquals("000000000A00000001000000000900000001000000000200000004411D4C000000000040F1170000000000411E84800000000040F38800000000004120C8E00000000040F5F90000000000411D4C000000000040F1170000000000",wkbText);
    }

}
