package ch.interlis.iox_j.wkb;

import static org.junit.Assert.*;
import org.junit.Test;

import com.vividsolutions.jts.geom.MultiPolygon;

import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import javax.xml.bind.DatatypeConverter;


public class Iox2wkbTest {
    private Iox2wkb conv=new Iox2wkb(2,java.nio.ByteOrder.BIG_ENDIAN);

    @Test
    public void arcSegmented_Ok() throws Exception{
        IomObject polylineValue=new Iom_jObject("POLYLINE", null);
        {
            IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
            IomObject startSegment=segments.addattrobj("segment", "COORD");
            startSegment.setattrvalue("C1", "2631293.029");
            startSegment.setattrvalue("C2", "1226007.765");
            IomObject endSegment=segments.addattrobj("segment", "ARC");
            endSegment.setattrvalue("A1", "2631297.503");
            endSegment.setattrvalue("A2", "1226017.040");
            endSegment.setattrvalue("C1", "2631296.768");
            endSegment.setattrvalue("C2", "1226027.312");
        }
        // convert
        byte wkb[]=conv.polyline2wkb(polylineValue, false,false, 0.01);
        // verify
        String wkbText=DatatypeConverter.printHexBinary(wkb);;
        assertEquals("0000000002000000144144133E83B645A24132B517C3D70A3D4144133E83B645A24132B517C3D70A3D4144133EE0F273164132B518A9FCED9B4144133F375EB8974132B5199A894FB04144133F86B101464132B51A94ADFDF24144133FCEA54EA74132B51B97948C50414413400EFDF2E44132B51CA25F0CFD414413404783C5AF4132B51DB428CEB0414413407806538A4132B51ECC0720A741441340A05C074F4132B51FE90A1BDD41441340C0624DD34132B5210A3D70A441441340D7FDFC7A4132B5222EAD89EF41441340E71A4DBF4132B523555B797C41441340EDAA4D564132B5247D4A9EAC41441340EBA85AFF4132B525A57D458041441340E1162F544132B526CCF5801B41441340CDFCDA534132B527F2B6007141441340B26CBB964132B52915C2F16F414413408E7D744A4132B52A3522CEE441441340624DD2F24132B52B4FDF3B640000000002000000144144133E83B645A24132B517C3D70A3D4144133E83B645A24132B517C3D70A3D4144133EE0F273164132B518A9FCED9B4144133F375EB8974132B5199A894FB04144133F86B101464132B51A94ADFDF24144133FCEA54EA74132B51B97948C50414413400EFDF2E44132B51CA25F0CFD414413404783C5AF4132B51DB428CEB0414413407806538A4132B51ECC0720A741441340A05C074F4132B51FE90A1BDD41441340C0624DD34132B5210A3D70A441441340D7FDFC7A4132B5222EAD89EF41441340E71A4DBF4132B523555B797C41441340EDAA4D564132B5247D4A9EAC41441340EBA85AFF4132B525A57D458041441340E1162F544132B526CCF5801B41441340CDFCDA534132B527F2B6007141441340B26CBB964132B52915C2F16F414413408E7D744A4132B52A3522CEE441441340624DD2F24132B52B4FDF3B64",wkbText);
    }
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
        String wkbText=DatatypeConverter.printHexBinary(wkb);;
        assertEquals("000000000A00000001000000000200000004411D4C000000000040F1170000000000411E84800000000040F38800000000004120C8E00000000040F5F90000000000411D4C000000000040F1170000000000",wkbText);
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
        byte wkb[]=conv.surface2wkb(multiSurface, true, 0.0, false);
        // verify
        String wkbText=DatatypeConverter.printHexBinary(wkb);;
        assertEquals("000000000A00000003000000000200000002411D4C000000000040F1170000000000411E84800000000040F3880000000000000000000200000002411E84800000000040F38800000000004120C8E00000000040F5F900000000000000000002000000024120C8E00000000040F5F90000000000411D4C000000000040F1170000000000",wkbText);
    }

    private IomObject getSurfaceZ() {
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
            startSegment.setattrvalue("C3", "1");
            IomObject endSegment=segments.addattrobj("segment", "COORD");
            endSegment.setattrvalue("C1", "500000.000");
            endSegment.setattrvalue("C2", "80000.000");
            endSegment.setattrvalue("C3", "1");
            IomObject endSegment2=segments.addattrobj("segment", "COORD");
            endSegment2.setattrvalue("C1", "550000.000");
            endSegment2.setattrvalue("C2", "90000.000");
            endSegment2.setattrvalue("C3", "1");
            IomObject endSegment3=segments.addattrobj("segment", "COORD");
            endSegment3.setattrvalue("C1", "480000.000");
            endSegment3.setattrvalue("C2", "70000.000");
            endSegment3.setattrvalue("C3", "1");
        }
        return multiSurface;
    }

    @Test
    public void surfaceZToStandardWkb() throws Exception {
        IomObject multiSurface=getSurfaceZ();
        Iox2wkb convWkb=new Iox2wkb(3,java.nio.ByteOrder.LITTLE_ENDIAN, false);
        byte wkb[]=convWkb.surface2wkb(multiSurface, true, 0.0);
        // verify
        String wkbText=DatatypeConverter.printHexBinary(wkb);;
        assertEquals("01F20300000100000001EA0300000400000000000000004C1D41000000000017F140000000000000F03F0000000080841E41000000000088F340000000000000F03F00000000E0C820410000000000F9F540000000000000F03F00000000004C1D41000000000017F140000000000000F03F",wkbText);
    }
    
    @Test
    public void surfaceZToExtendedWkb() throws Exception {
        IomObject multiSurface=getSurfaceZ();
        Iox2wkb convEwkb=new Iox2wkb(3,java.nio.ByteOrder.LITTLE_ENDIAN, true);
        byte ewkb[]=convEwkb.surface2wkb(multiSurface, true, 0.0);
        // verify
        String ewkbText=DatatypeConverter.printHexBinary(ewkb);
        assertEquals("010A0000800100000001020000800400000000000000004C1D41000000000017F140000000000000F03F0000000080841E41000000000088F340000000000000F03F00000000E0C820410000000000F9F540000000000000F03F00000000004C1D41000000000017F140000000000000F03F",ewkbText);
    }
    
    private IomObject getMultiCoordZ() {
        IomObject multiPoint=new Iom_jObject("MULTIPOINT", null);
        {
            IomObject startSegment=multiPoint.addattrobj("coord", "COORD");
            startSegment.setattrvalue("C1", "963340.00");
            startSegment.setattrvalue("C2", "1077418.00");
            startSegment.setattrvalue("C3", "1");
            IomObject endSegment=multiPoint.addattrobj("coord", "COORD");
            endSegment.setattrvalue("C1", "963344.00");
            endSegment.setattrvalue("C2", "1077411.00");
            endSegment.setattrvalue("C3", "1");
            IomObject endSegment2=multiPoint.addattrobj("coord", "COORD");
            endSegment2.setattrvalue("C1", "963348.00");
            endSegment2.setattrvalue("C2", "1077413.00");
            endSegment2.setattrvalue("C3", "2");
            IomObject endSegment3=multiPoint.addattrobj("coord", "COORD");
            endSegment3.setattrvalue("C1", "963343.00");
            endSegment3.setattrvalue("C2", "1077420.00");
            endSegment3.setattrvalue("C3", "2");
        }
        return multiPoint;
    }
    
    @Test
    public void multiCoordZToStandardWkb() throws Exception{
        IomObject multiPoint = getMultiCoordZ();
        Iox2wkb convWkb=new Iox2wkb(3,java.nio.ByteOrder.LITTLE_ENDIAN, false);
        byte wkb[]=convWkb.multicoord2wkb(multiPoint);
        // verify
        String wkbText=DatatypeConverter.printHexBinary(wkb);;
        assertEquals("01EC0300000400000001E90300000000000018662D4100000000AA703041000000000000F03F01E90300000000000020662D4100000000A3703041000000000000F03F01E90300000000000028662D4100000000A5703041000000000000004001E9030000000000001E662D4100000000AC7030410000000000000040",wkbText);
    }

    @Test
    public void multiCoordZToExtendedWkb() throws Exception{
        IomObject multiPoint = getMultiCoordZ();
        Iox2wkb convEwkb=new Iox2wkb(3,java.nio.ByteOrder.LITTLE_ENDIAN, true);
        byte ewkb[]=convEwkb.multicoord2wkb(multiPoint);
        // verify
        String ewkbText=DatatypeConverter.printHexBinary(ewkb);;
        assertEquals("01040000800400000001010000800000000018662D4100000000AA703041000000000000F03F01010000800000000020662D4100000000A3703041000000000000F03F01010000800000000028662D4100000000A570304100000000000000400101000080000000001E662D4100000000AC7030410000000000000040",ewkbText);
    }
    
    private IomObject getMultiPolyLineZ() {
        IomObject multiPolyLine=new Iom_jObject("MULTILINESTRING", null);
        {
            // polyline
            IomObject polylineValue = multiPolyLine.addattrobj("polyline", "POLYLINE");
            IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
            IomObject startSegment=segments.addattrobj("segment", "COORD");
            startSegment.setattrvalue("C1", "963340.00");
            startSegment.setattrvalue("C2", "1077418.00");
            startSegment.setattrvalue("C3", "1");
            IomObject endSegment=segments.addattrobj("segment", "COORD");
            endSegment.setattrvalue("C1", "963344.00");
            endSegment.setattrvalue("C2", "1077411.00");
            endSegment.setattrvalue("C3", "1");
            IomObject endSegment2=segments.addattrobj("segment", "COORD");
            endSegment2.setattrvalue("C1", "963348.00");
            endSegment2.setattrvalue("C2", "1077413.00");
            endSegment2.setattrvalue("C3", "1");
            // polyline 2
            IomObject polylineValue2 = multiPolyLine.addattrobj("polyline", "POLYLINE");
            IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
            
            IomObject startSegment2=segments2.addattrobj("segment", "COORD");
            startSegment2.setattrvalue("C1", "963348.00");
            startSegment2.setattrvalue("C2", "1077413.00");
            startSegment2.setattrvalue("C3", "2");
            
            IomObject endSegment3=segments2.addattrobj("segment", "COORD");
            endSegment3.setattrvalue("C1", "963343.00");
            endSegment3.setattrvalue("C2", "1077420.00");
            endSegment3.setattrvalue("C3", "2");
            
            IomObject endSegment4=segments2.addattrobj("segment", "COORD");
            endSegment4.setattrvalue("C1", "963340.00");
            endSegment4.setattrvalue("C2", "1077418.00");
            endSegment4.setattrvalue("C3", "2");
        }
        return multiPolyLine;
    }

    @Test
    public void multiPolyLineZToStandardWkb() throws Exception{
        IomObject multiPolyLine=getMultiPolyLineZ();
        Iox2wkb convWkb=new Iox2wkb(3,java.nio.ByteOrder.LITTLE_ENDIAN, false);
        byte wkb[]=convWkb.multiline2wkb(multiPolyLine, false, 0.0);
        // verify
        String wkbText=DatatypeConverter.printHexBinary(wkb);;
        assertEquals("01ED0300000200000001EA030000030000000000000018662D4100000000AA703041000000000000F03F0000000020662D4100000000A3703041000000000000F03F0000000028662D4100000000A5703041000000000000F03F01EA030000030000000000000028662D4100000000A57030410000000000000040000000001E662D4100000000AC70304100000000000000400000000018662D4100000000AA7030410000000000000040",wkbText);
    }

    @Test
    public void multiPolyLineZToExtendedWkb() throws Exception{
        IomObject multiPolyLine=getMultiPolyLineZ();
        Iox2wkb convEwkb=new Iox2wkb(3,java.nio.ByteOrder.LITTLE_ENDIAN, true);
        byte ewkb[]=convEwkb.multiline2wkb(multiPolyLine, false, 0.0);
        // verify
        String ewkbText=DatatypeConverter.printHexBinary(ewkb);;
        assertEquals("0105000080020000000102000080030000000000000018662D4100000000AA703041000000000000F03F0000000020662D4100000000A3703041000000000000F03F0000000028662D4100000000A5703041000000000000F03F0102000080030000000000000028662D4100000000A57030410000000000000040000000001E662D4100000000AC70304100000000000000400000000018662D4100000000AA7030410000000000000040",ewkbText);
    }

    private IomObject getMultiSurfaceZ() {
        IomObject multiSurface=new Iom_jObject("MULTISURFACE", null);
        {
            IomObject surfaceValue = multiSurface.addattrobj("surface", "SURFACE");
            // outer boundary
            IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
            // polyline
            IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
            
            IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
            IomObject startSegment=segments.addattrobj("segment", "COORD");
            
            startSegment.setattrvalue("C1", "963340.00");
            startSegment.setattrvalue("C2", "1077418.00");
            startSegment.setattrvalue("C3", "1");
            IomObject endSegment=segments.addattrobj("segment", "COORD");
            endSegment.setattrvalue("C1", "963344.00");
            endSegment.setattrvalue("C2", "1077411.00");
            endSegment.setattrvalue("C3", "1");
            IomObject endSegment2=segments.addattrobj("segment", "COORD");
            endSegment2.setattrvalue("C1", "963348.00");
            endSegment2.setattrvalue("C2", "1077413.00");
            endSegment2.setattrvalue("C3", "2");
            IomObject endSegment3=segments.addattrobj("segment", "COORD");
            endSegment3.setattrvalue("C1", "963343.00");
            endSegment3.setattrvalue("C2", "1077420.00");
            endSegment3.setattrvalue("C3", "2");
        }
        return multiSurface;
    }
    
    @Test
    public void multiSurfaceZToStandardWkb() throws Exception{
        IomObject multiSurface=getMultiSurfaceZ();
        Iox2wkb convWkb=new Iox2wkb(3,java.nio.ByteOrder.LITTLE_ENDIAN, false);
        byte wkb[]=convWkb.multisurface2wkb(multiSurface, false, 0.0);
        // verify
        String wkbText=DatatypeConverter.printHexBinary(wkb);;
        assertEquals("01EE0300000100000001EB03000001000000040000000000000018662D4100000000AA703041000000000000F03F0000000020662D4100000000A3703041000000000000F03F0000000028662D4100000000A57030410000000000000040000000001E662D4100000000AC7030410000000000000040",wkbText);
    }
    
    @Test
    public void multiSurfaceZToExtendedWkb() throws Exception{
        IomObject multiSurface=getMultiSurfaceZ();
        Iox2wkb convEwkb=new Iox2wkb(3,java.nio.ByteOrder.LITTLE_ENDIAN, true);
        byte ewkb[]=convEwkb.multisurface2wkb(multiSurface, false, 0.0);
        // verify
        String ewkbText=DatatypeConverter.printHexBinary(ewkb);
        assertEquals("010600008001000000010300008001000000040000000000000018662D4100000000AA703041000000000000F03F0000000020662D4100000000A3703041000000000000F03F0000000028662D4100000000A57030410000000000000040000000001E662D4100000000AC7030410000000000000040",ewkbText);
    }

    @Test
    public void surfaceRepairTouchingRing_Ok() throws Exception {
        IomObject multiSurface=getBananaPolygon();
        Iox2wkb convWkb=new Iox2wkb(2, java.nio.ByteOrder.BIG_ENDIAN, false);
        byte wkb[]=convWkb.surface2wkb(multiSurface, false, 0.0, true);
        // verify

        String wkbText=DatatypeConverter.printHexBinary(wkb);
        assertEquals("00000000030000000200000006414434F80000000041330F7400000000414434F8000000004133057C0000000041443DB3000000004133057C0000000041443DB30000000041330A4D0000000041443DB30000000041330F7400000000414434F80000000041330F74000000000000000541443DB30000000041330A4D0000000041443C6A00000000413308EC0000000041443B408000000041330A4D0000000041443C6A0000000041330BDC0000000041443DB30000000041330A4D00000000",wkbText);
    }

        @Test
    public void surfaceRepairTouchingRingDisabled_Ok() throws Exception {
        IomObject multiSurface=getBananaPolygon();
        Iox2wkb convWkb=new Iox2wkb(2, java.nio.ByteOrder.BIG_ENDIAN, false);
        byte wkb[]=convWkb.surface2wkb(multiSurface, false, 0.0, false);
        // verify

        String wkbText=DatatypeConverter.printHexBinary(wkb);
        assertEquals("0000000003000000010000000A414434F80000000041330F7400000000414434F8000000004133057C0000000041443DB3000000004133057C0000000041443DB30000000041330A4D0000000041443C6A00000000413308EC0000000041443B408000000041330A4D0000000041443C6A0000000041330BDC0000000041443DB30000000041330A4D0000000041443DB30000000041330F7400000000414434F80000000041330F7400000000",wkbText);
    }

    private IomObject getBananaPolygon() {
        IomObject multiSurface=new Iom_jObject("MULTISURFACE", null);
        IomObject surfaceValue = multiSurface.addattrobj("surface", "SURFACE");
        // outer boundary
        IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
        // polyline
        IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");

        IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");

        double[] coords = new double[]{2648560, 1249140, 2648560, 1246588, 2653030, 1246588, 2653030, 1247821, 2652372, 1247468, 2651777, 1247821, 2652372, 1248220, 2653030, 1247821, 2653030, 1249140, 2648560, 1249140};
        for (int i = 0; i < coords.length; i+=2) {
            IomObject coord = segments.addattrobj("segment", "COORD");
            coord.setattrvalue("C1", Double.toString(coords[i]));
            coord.setattrvalue("C2", Double.toString(coords[i+1]));
        }
        return multiSurface;
    }
}
