package ch.interlis.iox_j.wkb;

import static org.junit.Assert.*;

import com.vividsolutions.jts.io.WKBReader;
import org.junit.Test;

import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import net.iharder.Base64;


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
        String wkbText=Iox2wkb.bytesToHex(wkb);
        assertEquals("0000000002000000134144133E83B645A24132B517C3D70A3D4144133EE0F273164132B518A9FCED9B4144133F375EB8974132B5199A894FB04144133F86B101464132B51A94ADFDF24144133FCEA54EA74132B51B97948C50414413400EFDF2E44132B51CA25F0CFD414413404783C5AF4132B51DB428CEB0414413407806538A4132B51ECC0720A741441340A05C074F4132B51FE90A1BDD41441340C0624DD34132B5210A3D70A441441340D7FDFC7A4132B5222EAD89EF41441340E71A4DBF4132B523555B797C41441340EDAA4D564132B5247D4A9EAC41441340EBA85AFF4132B525A57D458041441340E1162F544132B526CCF5801B41441340CDFCDA534132B527F2B6007141441340B26CBB964132B52915C2F16F414413408E7D744A4132B52A3522CEE441441340624DD2F24132B52B4FDF3B64",wkbText);

        String wktText = new WKBReader().read(wkb).toText();
        assertEquals("LINESTRING (2631293.029 1226007.765, 2631293.757398973 1226008.6640156272, 2631294.432578157 1226009.6036577038, 2631295.052276763 1226010.5807799068, 2631295.6144197765 1226011.592110414, 2631296.117124902 1226012.6342628591, 2631296.5587088685 1226013.703747671, 2631296.937693064 1226014.7969837578, 2631297.2528084884 1226015.9103104987, 2631297.503 1226017.04, 2631297.687438545 1226018.182335492, 2631297.805490225 1226019.3334270408, 2631297.8567597074 1226020.489419858, 2631297.8410753007 1226021.6464427412, 2631297.7584895287 1226022.800621039, 2631297.609278956 1226023.948089626, 2631297.3939432604 1226025.0850058456, 2631297.11320356 1226026.2075623805, 2631296.768 1226027.312)", wktText);
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
        String wkbText=Iox2wkb.bytesToHex(wkb);
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
        byte wkb[]=conv.surface2wkb(multiSurface, true, 0.0, false);
        // verify
        String wkbText=Iox2wkb.bytesToHex(wkb);
        assertEquals("000000000A00000003000000000900000001000000000200000002411D4C000000000040F1170000000000411E84800000000040F3880000000000000000000900000001000000000200000002411E84800000000040F38800000000004120C8E00000000040F5F900000000000000000009000000010000000002000000024120C8E00000000040F5F90000000000411D4C000000000040F1170000000000",wkbText);
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
        String wkbText=Iox2wkb.bytesToHex(wkb);
        assertEquals("01F20300000100000001F10300000100000001EA0300000400000000000000004C1D41000000000017F140000000000000F03F0000000080841E41000000000088F340000000000000F03F00000000E0C820410000000000F9F540000000000000F03F00000000004C1D41000000000017F140000000000000F03F",wkbText);
    }
    
    @Test
    public void surfaceZToExtendedWkb() throws Exception {
        IomObject multiSurface=getSurfaceZ();
        Iox2wkb convEwkb=new Iox2wkb(3,java.nio.ByteOrder.LITTLE_ENDIAN, true);
        byte ewkb[]=convEwkb.surface2wkb(multiSurface, true, 0.0);
        // verify
        String ewkbText=Iox2wkb.bytesToHex(ewkb);
        assertEquals("010A0000800100000001090000800100000001020000800400000000000000004C1D41000000000017F140000000000000F03F0000000080841E41000000000088F340000000000000F03F00000000E0C820410000000000F9F540000000000000F03F00000000004C1D41000000000017F140000000000000F03F",ewkbText);
    }

    @Test
    public void surfaceWithArcToWkb() throws Exception {
        IomObject multiSurface=getSurfaceWithArc();
        Iox2wkb convEwkb=new Iox2wkb(2,java.nio.ByteOrder.LITTLE_ENDIAN, true);
        byte ewkb[]=convEwkb.surface2wkb(multiSurface, true, 0.001, false);
        // verify
        String ewkbText=Iox2wkb.bytesToHex(ewkb);
        assertEquals("010A00000001000000010900000003000000010200000002000000000000808E4D444100000000E4113341000000808E4D444100000000EC043341010800000005000000000000808E4D444100000000EC043341000000802F53444100000000EC04334100000080825244410000000021123341000000804E53444100000000F414334100000000B04F4441000000009817334101020000000200000000000000B04F44410000000098173341000000808E4D444100000000E4113341",ewkbText);
    }

    @Test
    public void surfaceWithArcToWkbWithoutCurvePolygon() throws Exception {
        IomObject multiSurface=getSurfaceWithArc();
        Iox2wkb convEwkb=new Iox2wkb(2,java.nio.ByteOrder.LITTLE_ENDIAN, true);
        byte ewkb[]=convEwkb.surface2wkb(multiSurface, false, 0.0, false);
        // verify
        String ewkbText=Iox2wkb.bytesToHex(ewkb);
        assertEquals("01030000000100000007000000000000808E4D444100000000E4113341000000808E4D444100000000EC043341000000802F53444100000000EC04334100000080825244410000000021123341000000804E53444100000000F414334100000000B04F44410000000098173341000000808E4D444100000000E4113341",ewkbText);

        String wktText = new WKBReader().read(ewkb).toText();
        assertEquals("POLYGON ((2661149 1249764, 2661149 1246444, 2664031 1246444, 2663685 1249825, 2664093 1250548, 2662240 1251224, 2661149 1249764))", wktText);
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
        String wkbText=Iox2wkb.bytesToHex(wkb);
        assertEquals("01EC0300000400000001E90300000000000018662D4100000000AA703041000000000000F03F01E90300000000000020662D4100000000A3703041000000000000F03F01E90300000000000028662D4100000000A5703041000000000000004001E9030000000000001E662D4100000000AC7030410000000000000040",wkbText);
    }

    @Test
    public void multiCoordZToExtendedWkb() throws Exception{
        IomObject multiPoint = getMultiCoordZ();
        Iox2wkb convEwkb=new Iox2wkb(3,java.nio.ByteOrder.LITTLE_ENDIAN, true);
        byte ewkb[]=convEwkb.multicoord2wkb(multiPoint);
        // verify
        String ewkbText=Iox2wkb.bytesToHex(ewkb);
        assertEquals("01040000800400000001010000800000000018662D4100000000AA703041000000000000F03F01010000800000000020662D4100000000A3703041000000000000F03F01010000800000000028662D4100000000A570304100000000000000400101000080000000001E662D4100000000AC7030410000000000000040",ewkbText);

        String wktText =new WKBReader().read(ewkb).toText();
        assertEquals("MULTIPOINT ((963340 1077418), (963344 1077411), (963348 1077413), (963343 1077420))", wktText);
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
        String wkbText=Iox2wkb.bytesToHex(wkb);
        assertEquals("01ED0300000200000001EA030000030000000000000018662D4100000000AA703041000000000000F03F0000000020662D4100000000A3703041000000000000F03F0000000028662D4100000000A5703041000000000000F03F01EA030000030000000000000028662D4100000000A57030410000000000000040000000001E662D4100000000AC70304100000000000000400000000018662D4100000000AA7030410000000000000040",wkbText);
    }

    @Test
    public void multiPolyLineZToExtendedWkb() throws Exception{
        IomObject multiPolyLine=getMultiPolyLineZ();
        Iox2wkb convEwkb=new Iox2wkb(3,java.nio.ByteOrder.LITTLE_ENDIAN, true);
        byte ewkb[]=convEwkb.multiline2wkb(multiPolyLine, false, 0.0);
        // verify
        String ewkbText=Iox2wkb.bytesToHex(ewkb);
        assertEquals("0105000080020000000102000080030000000000000018662D4100000000AA703041000000000000F03F0000000020662D4100000000A3703041000000000000F03F0000000028662D4100000000A5703041000000000000F03F0102000080030000000000000028662D4100000000A57030410000000000000040000000001E662D4100000000AC70304100000000000000400000000018662D4100000000AA7030410000000000000040",ewkbText);

        String wktText =new WKBReader().read(ewkb).toText();
        assertEquals("MULTILINESTRING ((963340 1077418, 963344 1077411, 963348 1077413), (963348 1077413, 963343 1077420, 963340 1077418))", wktText);
    }

    @Test
    public void polyLineCurveWkb() throws Exception{
        IomObject multiPolyLine=getPolylineWithArc();
        Iox2wkb convEwkb=new Iox2wkb(2,java.nio.ByteOrder.LITTLE_ENDIAN, true);
        byte ewkb[]=convEwkb.polyline2wkb(multiPolyLine, false, true, 0.0);
        // verify
        String ewkbText=Iox2wkb.bytesToHex(ewkb);
        assertEquals("010900000003000000010200000002000000000000808E4D444100000000E4113341000000808E4D444100000000EC043341010800000005000000000000808E4D444100000000EC043341000000802F53444100000000EC04334100000080825244410000000021123341000000804E53444100000000F414334100000000B04F4441000000009817334101020000000200000000000000B04F44410000000098173341000000808E4D444100000000E4113341",ewkbText);
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
        String wkbText=Iox2wkb.bytesToHex(wkb);
        assertEquals("01EE0300000100000001EB03000001000000040000000000000018662D4100000000AA703041000000000000F03F0000000020662D4100000000A3703041000000000000F03F0000000028662D4100000000A57030410000000000000040000000001E662D4100000000AC7030410000000000000040",wkbText);
    }
    
    @Test
    public void multiSurfaceZToExtendedWkb() throws Exception{
        IomObject multiSurface=getMultiSurfaceZ();
        Iox2wkb convEwkb=new Iox2wkb(3,java.nio.ByteOrder.LITTLE_ENDIAN, true);
        byte ewkb[]=convEwkb.multisurface2wkb(multiSurface, false, 0.0);
        // verify
        String ewkbText=Iox2wkb.bytesToHex(ewkb);
        assertEquals("010600008001000000010300008001000000040000000000000018662D4100000000AA703041000000000000F03F0000000020662D4100000000A3703041000000000000F03F0000000028662D4100000000A57030410000000000000040000000001E662D4100000000AC7030410000000000000040",ewkbText);

        String wktText =new WKBReader().read(ewkb).toText();
        assertEquals("MULTIPOLYGON (((963340 1077418, 963344 1077411, 963348 1077413, 963343 1077420, 963340 1077418)))", wktText);
    }

    @Test
    public void surfaceRepairTouchingRing_Ok() throws Exception {
        IomObject multiSurface=getBananaPolygon();
        Iox2wkb convWkb=new Iox2wkb(2, java.nio.ByteOrder.BIG_ENDIAN, false);
        byte wkb[]=convWkb.surface2wkb(multiSurface, false, 0.0, true);
        // verify

        String wkbText=Iox2wkb.bytesToHex(wkb);
        assertEquals("00000000030000000200000006414434F80000000041330F7400000000414434F8000000004133057C0000000041443DB3000000004133057C0000000041443DB30000000041330A4D0000000041443DB30000000041330F7400000000414434F80000000041330F74000000000000000541443DB30000000041330A4D0000000041443C6A00000000413308EC0000000041443B408000000041330A4D0000000041443C6A0000000041330BDC0000000041443DB30000000041330A4D00000000",wkbText);
    }

    @Test
    public void surfaceRepairTouchingRingInStart_Ok() throws Exception {
        IomObject multiSurface=getBananaPolygonStartOnOverlap();
        Iox2wkb convWkb=new Iox2wkb(2, java.nio.ByteOrder.BIG_ENDIAN, false);
        byte wkb[]=convWkb.surface2wkb(multiSurface, false, 0.0, true);
        // verify

        String wkbText=Iox2wkb.bytesToHex(wkb);
        assertEquals("0000000003000000020000000641443DB30000000041330A4D0000000041443DB30000000041330F7400000000414434F80000000041330F7400000000414434F8000000004133057C0000000041443DB3000000004133057C0000000041443DB30000000041330A4D000000000000000541443DB30000000041330A4D0000000041443C6A00000000413308EC0000000041443B408000000041330A4D0000000041443C6A0000000041330BDC0000000041443DB30000000041330A4D00000000",wkbText);
    }

    @Test
    public void surfaceTouchingRingInStartNoRepair_Ok() throws Exception {
        IomObject multiSurface=getBananaPolygonStartOnOverlap();
        Iox2wkb convWkb=new Iox2wkb(2, java.nio.ByteOrder.BIG_ENDIAN, false);
        byte wkb[]=convWkb.surface2wkb(multiSurface, false, 0.0, false);
        // verify

        String wkbText=Iox2wkb.bytesToHex(wkb);
        assertEquals("0000000003000000010000000A41443DB30000000041330A4D0000000041443DB30000000041330F7400000000414434F80000000041330F7400000000414434F8000000004133057C0000000041443DB3000000004133057C0000000041443DB30000000041330A4D0000000041443C6A00000000413308EC0000000041443B408000000041330A4D0000000041443C6A0000000041330BDC0000000041443DB30000000041330A4D00000000",wkbText);
    }

    @Test
    public void surfaceRepairTouchingRingWithTwoInnerRings_Ok() throws Exception {
        IomObject multiSurface=new Iom_jObject("MULTISURFACE", null);
        IomObject surfaceValue = multiSurface.addattrobj("surface", "SURFACE");
        // outer boundary
        IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
        // polyline
        IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");

        IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");

        double[] coords = new double[]{2648560, 1249140, 2648560, 1246588, 2653030, 1246588, 2653030, 1247821, 2652372, 1247468, 2651777, 1247821, 2651200, 1247428, 2650613, 1247879, 2651207, 1248246, 2651777, 1247821, 2652372, 1248220, 2653030, 1247821, 2653030, 1249140, 2648560, 1249140};
        for (int i = 0; i < coords.length; i+=2) {
            IomObject coord = segments.addattrobj("segment", "COORD");
            coord.setattrvalue("C1", Double.toString(coords[i]));
            coord.setattrvalue("C2", Double.toString(coords[i+1]));
        }
        Iox2wkb convWkb=new Iox2wkb(2, java.nio.ByteOrder.BIG_ENDIAN, false);
        byte wkb[]=convWkb.surface2wkb(multiSurface, false, 0.0, true);
        byte wkbCurve[]=convWkb.surface2wkb(multiSurface, true, 0.0, true);

        String wkbText=Iox2wkb.bytesToHex(wkb);
        assertEquals("00000000030000000300000006414434F80000000041330F7400000000414434F8000000004133057C0000000041443DB3000000004133057C0000000041443DB30000000041330A4D0000000041443DB30000000041330F7400000000414434F80000000041330F74000000000000000541443B408000000041330A4D0000000041443A2000000000413308C400000000414438FA8000000041330A870000000041443A238000000041330BF60000000041443B408000000041330A4D000000000000000541443DB30000000041330A4D0000000041443C6A00000000413308EC0000000041443B408000000041330A4D0000000041443C6A0000000041330BDC0000000041443DB30000000041330A4D00000000",wkbText);

        String wkbCurveText = Iox2wkb.bytesToHex(wkbCurve);
        assertEquals("000000000A00000003000000000900000001000000000200000006414434F80000000041330F7400000000414434F8000000004133057C0000000041443DB3000000004133057C0000000041443DB30000000041330A4D0000000041443DB30000000041330F7400000000414434F80000000041330F740000000000000000090000000100000000020000000541443B408000000041330A4D0000000041443A2000000000413308C400000000414438FA8000000041330A870000000041443A238000000041330BF60000000041443B408000000041330A4D0000000000000000090000000100000000020000000541443DB30000000041330A4D0000000041443C6A00000000413308EC0000000041443B408000000041330A4D0000000041443C6A0000000041330BDC0000000041443DB30000000041330A4D00000000", wkbCurveText);
    }

    @Test
    public void surfaceRepairTouchingRingDisabled_Ok() throws Exception {
        IomObject multiSurface=getBananaPolygon();
        Iox2wkb convWkb=new Iox2wkb(2, java.nio.ByteOrder.BIG_ENDIAN, false);
        byte wkb[]=convWkb.surface2wkb(multiSurface, false, 0.0, false);
        // verify

        String wkbText=Iox2wkb.bytesToHex(wkb);
        assertEquals("0000000003000000010000000A414434F80000000041330F7400000000414434F8000000004133057C0000000041443DB3000000004133057C0000000041443DB30000000041330A4D0000000041443C6A00000000413308EC0000000041443B408000000041330A4D0000000041443C6A0000000041330BDC0000000041443DB30000000041330A4D0000000041443DB30000000041330F7400000000414434F80000000041330F7400000000",wkbText);
    }

    @Test
    public void surfaceReapirTouchingRingMultipleInnerRingSegments() throws Exception {
        IomObject multiSurface=getBananaPolygonWithInnerArcs();
        Iox2wkb convWkb=new Iox2wkb(2, java.nio.ByteOrder.BIG_ENDIAN, false);
        byte wkb[]=convWkb.surface2wkb(multiSurface, true, 0.0, true);
        // verify

        String wkbText=Iox2wkb.bytesToHex(wkb);
        assertEquals("000000000A0000000200000000090000000100000000020000000641442E3E0000000041330A9F000000004144398800000000413301ED00000000414439948000000041330AAB00000000414439948000000041330AAB0000000041443994800000004133150C0000000041442E3E0000000041330A9F00000000000000000900000003000000000800000003414439948000000041330AAB000000004144377E8000000041330793000000004144352580000000413308060000000000000000020000000241443525800000004133080600000000414435150000000041330E9100000000000000000800000003414435150000000041330E9100000000414437268000000041330E7900000000414439948000000041330AAB00000000",wkbText);
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

    private IomObject getBananaPolygonStartOnOverlap(){
        IomObject multiSurface=new Iom_jObject("MULTISURFACE", null);
        IomObject surfaceValue = multiSurface.addattrobj("surface", "SURFACE");
        // outer boundary
        IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
        // polyline
        IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");

        IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");

        double[] coords = new double[]{ 2653030, 1247821,
                                        2653030, 1249140,
                                        2648560, 1249140,
                                        2648560, 1246588,
                                        2653030, 1246588,
                                        2653030, 1247821,
                                        2652372, 1247468,
                                        2651777, 1247821,
                                        2652372, 1248220,
                                        2653030, 1247821};
        for (int i = 0; i < coords.length; i+=2) {
            IomObject coord = segments.addattrobj("segment", "COORD");
            coord.setattrvalue("C1", Double.toString(coords[i]));
            coord.setattrvalue("C2", Double.toString(coords[i+1]));
        }
        return multiSurface;
    }

    private IomObject getSurfaceWithArc() {
        IomObject multiSurface=new Iom_jObject("MULTISURFACE", null);
        IomObject surfaceValue = multiSurface.addattrobj("surface", "SURFACE");
        // outer boundary
        IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
        outerBoundary.addattrobj("polyline", getPolylineWithArc());

        return multiSurface;
    }

    private IomObject getPolylineWithArc(){
        IomObject polylineValue = new Iom_jObject("POLYLINE", null);
        IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
        IomObject c1=segments.addattrobj("segment", "COORD");
        IomObject c2=segments.addattrobj("segment", "COORD");
        IomObject a3=segments.addattrobj("segment", "ARC");
        IomObject a4=segments.addattrobj("segment", "ARC");
        IomObject c4=segments.addattrobj("segment", "COORD");



        c1.setattrvalue("C1", "2661149.000");
        c1.setattrvalue("C2", "1249764.000");

        c2.setattrvalue("C1", "2661149.000");
        c2.setattrvalue("C2", "1246444.000");

        a3.setattrvalue("C1", "2663685.000");
        a3.setattrvalue("C2", "1249825.000");
        a3.setattrvalue("A1", "2664031.000");
        a3.setattrvalue("A2", "1246444.000");

        a4.setattrvalue("C1", "2662240.000");
        a4.setattrvalue("C2", "1251224.000");
        a4.setattrvalue("A1", "2664093.000");
        a4.setattrvalue("A2", "1250548.000");

        c4.setattrvalue("C1", "2661149.000");
        c4.setattrvalue("C2", "1249764.000");

        return  polylineValue;
    }

    private IomObject getBananaPolygonWithInnerArcs() {
        IomObject multiSurface=new Iom_jObject("MULTISURFACE", null);
        IomObject surfaceValue = multiSurface.addattrobj("surface", "SURFACE");
        // outer boundary
        IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
        IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");

        IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
        IomObject c1=segments.addattrobj("segment", "COORD");
        IomObject c2=segments.addattrobj("segment", "COORD");
        IomObject c3=segments.addattrobj("segment", "COORD");
        IomObject a1=segments.addattrobj("segment", "ARC");
        IomObject c4=segments.addattrobj("segment", "COORD");
        IomObject a2=segments.addattrobj("segment", "ARC");
        IomObject c5=segments.addattrobj("segment", "COORD");
        IomObject c6=segments.addattrobj("segment", "COORD");

        c1.setattrvalue("C1", "2645116.000");
        c1.setattrvalue("C2", "1247903.000");

        c2.setattrvalue("C1", "2650896.000");
        c2.setattrvalue("C2", "1245677.000");

        c3.setattrvalue("C1", "2650921.000");
        c3.setattrvalue("C2", "1247915.000");

        a1.setattrvalue("C1", "2648651.000");
        a1.setattrvalue("C2", "1247238.000");
        a1.setattrvalue("A1", "2649853.000");
        a1.setattrvalue("A2", "1247123.000");

        c4.setattrvalue("C1", "2648618.000");
        c4.setattrvalue("C2", "1248913.000");

        a2.setattrvalue("C1", "2650921.000");
        a2.setattrvalue("C2", "1247915.000");
        a2.setattrvalue("A1", "2649677.000");
        a2.setattrvalue("A2", "1248889.000");

        c5.setattrvalue("C1", "2650921.000");
        c5.setattrvalue("C2", "1250572.000");

        c6.setattrvalue("C1", "2645116.000");
        c6.setattrvalue("C2", "1247903.000");

        return multiSurface;
    }
}
