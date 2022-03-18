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
        String wkbText=Base64.encodeBytes(wkb);
        assertEquals("AAAAAAIAAAATQUQTPoO2RaJBMrUXw9cKPUFEEz7g8nMWQTK1GKn87ZtBRBM/N164l0EytRmaiU+wQUQTP4axAUZBMrUalK398kFEEz/OpU6nQTK1G5eUjFBBRBNADv3y5EEytRyiXwz9QUQTQEeDxa9BMrUdtCjOsEFEE0B4BlOKQTK1HswHIKdBRBNAoFwHT0EytR/pChvdQUQTQMBiTdNBMrUhCj1wpEFEE0DX/fx6QTK1Ii6tie9BRBNA5xpNv0EytSNVW3l8QUQTQO2qTVZBMrUkfUqerEFEE0DrqFr/QTK1JaV9RYBBRBNA4RYvVEEytSbM9YAbQUQTQM382lNBMrUn8rYAcUFEE0CybLuWQTK1KRXC8W9BRBNAjn10SkEytSo1Is7kQUQTQGJN0vJBMrUrT987ZA==",wkbText);

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
        String wkbText=Base64.encodeBytes(wkb);
        assertEquals("AAAAAAoAAAABAAAAAAkAAAABAAAAAAIAAAAEQR1MAAAAAABA8RcAAAAAAEEehIAAAAAAQPOIAAAAAABBIMjgAAAAAED1+QAAAAAAQR1MAAAAAABA8RcAAAAAAA==",wkbText);
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
        String wkbText=Base64.encodeBytes(wkb);
        assertEquals("AAAAAAoAAAADAAAAAAkAAAABAAAAAAIAAAACQR1MAAAAAABA8RcAAAAAAEEehIAAAAAAQPOIAAAAAAAAAAAACQAAAAEAAAAAAgAAAAJBHoSAAAAAAEDziAAAAAAAQSDI4AAAAABA9fkAAAAAAAAAAAAJAAAAAQAAAAACAAAAAkEgyOAAAAAAQPX5AAAAAABBHUwAAAAAAEDxFwAAAAAA",wkbText);
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
        String wkbText=Base64.encodeBytes(wkb);
        assertEquals("AfIDAAABAAAAAfEDAAABAAAAAeoDAAAEAAAAAAAAAABMHUEAAAAAABfxQAAAAAAAAPA/AAAAAICEHkEAAAAAAIjzQAAAAAAAAPA/AAAAAODIIEEAAAAAAPn1QAAAAAAAAPA/AAAAAABMHUEAAAAAABfxQAAAAAAAAPA/",wkbText);
    }
    
    @Test
    public void surfaceZToExtendedWkb() throws Exception {
        IomObject multiSurface=getSurfaceZ();
        Iox2wkb convEwkb=new Iox2wkb(3,java.nio.ByteOrder.LITTLE_ENDIAN, true);
        byte ewkb[]=convEwkb.surface2wkb(multiSurface, true, 0.0);
        // verify
        String ewkbText=Base64.encodeBytes(ewkb);
        assertEquals("AQoAAIABAAAAAQkAAIABAAAAAQIAAIAEAAAAAAAAAABMHUEAAAAAABfxQAAAAAAAAPA/AAAAAICEHkEAAAAAAIjzQAAAAAAAAPA/AAAAAODIIEEAAAAAAPn1QAAAAAAAAPA/AAAAAABMHUEAAAAAABfxQAAAAAAAAPA/",ewkbText);
    }

    @Test
    public void surfaceWithArcToWkb() throws Exception {
        IomObject multiSurface=getSurfaceWithArc();
        Iox2wkb convEwkb=new Iox2wkb(2,java.nio.ByteOrder.LITTLE_ENDIAN, true);
        byte ewkb[]=convEwkb.surface2wkb(multiSurface, true, 0.001, false);
        // verify
        String ewkbText=Base64.encodeBytes(ewkb);
        assertEquals("AQoAAAABAAAAAQkAAAADAAAAAQIAAAACAAAAAAAAgI5NREEAAAAA5BEzQQAAAICOTURBAAAAAOwEM0EBCAAAAAUAAAAAAACAjk1EQQAAAADsBDNBAAAAgC9TREEAAAAA7AQzQQAAAICCUkRBAAAAACESM0EAAACATlNEQQAAAAD0FDNBAAAAALBPREEAAAAAmBczQQECAAAAAgAAAAAAAACwT0RBAAAAAJgXM0EAAACAjk1EQQAAAADkETNB",ewkbText);
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
        String wkbText=Base64.encodeBytes(wkb);
        assertEquals("AewDAAAEAAAAAekDAAAAAAAAGGYtQQAAAACqcDBBAAAAAAAA8D8B6QMAAAAAAAAgZi1BAAAAAKNwMEEAAAAAAADwPwHpAwAAAAAAAChmLUEAAAAApXAwQQAAAAAAAABAAekDAAAAAAAAHmYtQQAAAACscDBBAAAAAAAAAEA=",wkbText);
    }

    @Test
    public void multiCoordZToExtendedWkb() throws Exception{
        IomObject multiPoint = getMultiCoordZ();
        Iox2wkb convEwkb=new Iox2wkb(3,java.nio.ByteOrder.LITTLE_ENDIAN, true);
        byte ewkb[]=convEwkb.multicoord2wkb(multiPoint);
        // verify
        String ewkbText=Base64.encodeBytes(ewkb);
        assertEquals("AQQAAIAEAAAAAQEAAIAAAAAAGGYtQQAAAACqcDBBAAAAAAAA8D8BAQAAgAAAAAAgZi1BAAAAAKNwMEEAAAAAAADwPwEBAACAAAAAAChmLUEAAAAApXAwQQAAAAAAAABAAQEAAIAAAAAAHmYtQQAAAACscDBBAAAAAAAAAEA=",ewkbText);

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
        String wkbText=Base64.encodeBytes(wkb);
        assertEquals("Ae0DAAACAAAAAeoDAAADAAAAAAAAABhmLUEAAAAAqnAwQQAAAAAAAPA/AAAAACBmLUEAAAAAo3AwQQAAAAAAAPA/AAAAAChmLUEAAAAApXAwQQAAAAAAAPA/AeoDAAADAAAAAAAAAChmLUEAAAAApXAwQQAAAAAAAABAAAAAAB5mLUEAAAAArHAwQQAAAAAAAABAAAAAABhmLUEAAAAAqnAwQQAAAAAAAABA",wkbText);
    }

    @Test
    public void multiPolyLineZToExtendedWkb() throws Exception{
        IomObject multiPolyLine=getMultiPolyLineZ();
        Iox2wkb convEwkb=new Iox2wkb(3,java.nio.ByteOrder.LITTLE_ENDIAN, true);
        byte ewkb[]=convEwkb.multiline2wkb(multiPolyLine, false, 0.0);
        // verify
        String ewkbText=Base64.encodeBytes(ewkb);
        assertEquals("AQUAAIACAAAAAQIAAIADAAAAAAAAABhmLUEAAAAAqnAwQQAAAAAAAPA/AAAAACBmLUEAAAAAo3AwQQAAAAAAAPA/AAAAAChmLUEAAAAApXAwQQAAAAAAAPA/AQIAAIADAAAAAAAAAChmLUEAAAAApXAwQQAAAAAAAABAAAAAAB5mLUEAAAAArHAwQQAAAAAAAABAAAAAABhmLUEAAAAAqnAwQQAAAAAAAABA",ewkbText);

        String wktText =new WKBReader().read(ewkb).toText();
        assertEquals("MULTILINESTRING ((963340 1077418, 963344 1077411, 963348 1077413), (963348 1077413, 963343 1077420, 963340 1077418))", wktText);
    }

    @Test
    public void polyLineCurveWkb() throws Exception{
        IomObject multiPolyLine=getPolylineWithArc();
        Iox2wkb convEwkb=new Iox2wkb(2,java.nio.ByteOrder.LITTLE_ENDIAN, true);
        byte ewkb[]=convEwkb.polyline2wkb(multiPolyLine, false, true, 0.0);
        // verify
        String ewkbText=Base64.encodeBytes(ewkb);
        assertEquals("AQkAAAADAAAAAQIAAAACAAAAAAAAgI5NREEAAAAA5BEzQQAAAICOTURBAAAAAOwEM0EBCAAAAAUAAAAAAACAjk1EQQAAAADsBDNBAAAAgC9TREEAAAAA7AQzQQAAAICCUkRBAAAAACESM0EAAACATlNEQQAAAAD0FDNBAAAAALBPREEAAAAAmBczQQECAAAAAgAAAAAAAACwT0RBAAAAAJgXM0EAAACAjk1EQQAAAADkETNB",ewkbText);
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
        String wkbText=Base64.encodeBytes(wkb);
        assertEquals("Ae4DAAABAAAAAesDAAABAAAABAAAAAAAAAAYZi1BAAAAAKpwMEEAAAAAAADwPwAAAAAgZi1BAAAAAKNwMEEAAAAAAADwPwAAAAAoZi1BAAAAAKVwMEEAAAAAAAAAQAAAAAAeZi1BAAAAAKxwMEEAAAAAAAAAQA==",wkbText);
    }
    
    @Test
    public void multiSurfaceZToExtendedWkb() throws Exception{
        IomObject multiSurface=getMultiSurfaceZ();
        Iox2wkb convEwkb=new Iox2wkb(3,java.nio.ByteOrder.LITTLE_ENDIAN, true);
        byte ewkb[]=convEwkb.multisurface2wkb(multiSurface, false, 0.0);
        // verify
        String ewkbText=Base64.encodeBytes(ewkb);
        assertEquals("AQYAAIABAAAAAQMAAIABAAAABAAAAAAAAAAYZi1BAAAAAKpwMEEAAAAAAADwPwAAAAAgZi1BAAAAAKNwMEEAAAAAAADwPwAAAAAoZi1BAAAAAKVwMEEAAAAAAAAAQAAAAAAeZi1BAAAAAKxwMEEAAAAAAAAAQA==",ewkbText);

        String wktText =new WKBReader().read(ewkb).toText();
        assertEquals("MULTIPOLYGON (((963340 1077418, 963344 1077411, 963348 1077413, 963343 1077420, 963340 1077418)))", wktText);
    }

    @Test
    public void surfaceRepairTouchingRing_Ok() throws Exception {
        IomObject multiSurface=getBananaPolygon();
        Iox2wkb convWkb=new Iox2wkb(2, java.nio.ByteOrder.BIG_ENDIAN, false);
        byte wkb[]=convWkb.surface2wkb(multiSurface, false, 0.0, true);
        // verify

        String wkbText=Base64.encodeBytes(wkb);
        assertEquals("AAAAAAMAAAACAAAABkFENPgAAAAAQTMPdAAAAABBRDT4AAAAAEEzBXwAAAAAQUQ9swAAAABBMwV8AAAAAEFEPbMAAAAAQTMKTQAAAABBRD2zAAAAAEEzD3QAAAAAQUQ0+AAAAABBMw90AAAAAAAAAAVBRD2zAAAAAEEzCk0AAAAAQUQ8agAAAABBMwjsAAAAAEFEO0CAAAAAQTMKTQAAAABBRDxqAAAAAEEzC9wAAAAAQUQ9swAAAABBMwpNAAAAAA==",wkbText);
    }

    @Test
    public void surfaceRepairTouchingRingInStart_Ok() throws Exception {
        IomObject multiSurface=getBananaPolygonStartOnOverlap();
        Iox2wkb convWkb=new Iox2wkb(2, java.nio.ByteOrder.BIG_ENDIAN, false);
        byte wkb[]=convWkb.surface2wkb(multiSurface, false, 0.0, true);
        // verify

        String wkbText=Base64.encodeBytes(wkb);
        assertEquals("AAAAAAMAAAACAAAABkFEPbMAAAAAQTMKTQAAAABBRD2zAAAAAEEzD3QAAAAAQUQ0+AAAAABBMw90AAAAAEFENPgAAAAAQTMFfAAAAABBRD2zAAAAAEEzBXwAAAAAQUQ9swAAAABBMwpNAAAAAAAAAAVBRD2zAAAAAEEzCk0AAAAAQUQ8agAAAABBMwjsAAAAAEFEO0CAAAAAQTMKTQAAAABBRDxqAAAAAEEzC9wAAAAAQUQ9swAAAABBMwpNAAAAAA==",wkbText);
    }

    @Test
    public void surfaceTouchingRingInStartNoRepair_Ok() throws Exception {
        IomObject multiSurface=getBananaPolygonStartOnOverlap();
        Iox2wkb convWkb=new Iox2wkb(2, java.nio.ByteOrder.BIG_ENDIAN, false);
        byte wkb[]=convWkb.surface2wkb(multiSurface, false, 0.0, false);
        // verify

        String wkbText=Base64.encodeBytes(wkb);
        assertEquals("AAAAAAMAAAABAAAACkFEPbMAAAAAQTMKTQAAAABBRD2zAAAAAEEzD3QAAAAAQUQ0+AAAAABBMw90AAAAAEFENPgAAAAAQTMFfAAAAABBRD2zAAAAAEEzBXwAAAAAQUQ9swAAAABBMwpNAAAAAEFEPGoAAAAAQTMI7AAAAABBRDtAgAAAAEEzCk0AAAAAQUQ8agAAAABBMwvcAAAAAEFEPbMAAAAAQTMKTQAAAAA=",wkbText);
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

        String wkbText=Base64.encodeBytes(wkb);
        assertEquals("AAAAAAMAAAADAAAABkFENPgAAAAAQTMPdAAAAABBRDT4AAAAAEEzBXwAAAAAQUQ9swAAAABBMwV8AAAAAEFEPbMAAAAAQTMKTQAAAABBRD2zAAAAAEEzD3QAAAAAQUQ0+AAAAABBMw90AAAAAAAAAAVBRDtAgAAAAEEzCk0AAAAAQUQ6IAAAAABBMwjEAAAAAEFEOPqAAAAAQTMKhwAAAABBRDojgAAAAEEzC/YAAAAAQUQ7QIAAAABBMwpNAAAAAAAAAAVBRD2zAAAAAEEzCk0AAAAAQUQ8agAAAABBMwjsAAAAAEFEO0CAAAAAQTMKTQAAAABBRDxqAAAAAEEzC9wAAAAAQUQ9swAAAABBMwpNAAAAAA==",wkbText);

        String wkbCurveText = Base64.encodeBytes(wkbCurve);
        assertEquals("AAAAAAoAAAADAAAAAAkAAAABAAAAAAIAAAAGQUQ0+AAAAABBMw90AAAAAEFENPgAAAAAQTMFfAAAAABBRD2zAAAAAEEzBXwAAAAAQUQ9swAAAABBMwpNAAAAAEFEPbMAAAAAQTMPdAAAAABBRDT4AAAAAEEzD3QAAAAAAAAAAAkAAAABAAAAAAIAAAAFQUQ7QIAAAABBMwpNAAAAAEFEOiAAAAAAQTMIxAAAAABBRDj6gAAAAEEzCocAAAAAQUQ6I4AAAABBMwv2AAAAAEFEO0CAAAAAQTMKTQAAAAAAAAAACQAAAAEAAAAAAgAAAAVBRD2zAAAAAEEzCk0AAAAAQUQ8agAAAABBMwjsAAAAAEFEO0CAAAAAQTMKTQAAAABBRDxqAAAAAEEzC9wAAAAAQUQ9swAAAABBMwpNAAAAAA==", wkbCurveText);
    }

    @Test
    public void surfaceRepairTouchingRingDisabled_Ok() throws Exception {
        IomObject multiSurface=getBananaPolygon();
        Iox2wkb convWkb=new Iox2wkb(2, java.nio.ByteOrder.BIG_ENDIAN, false);
        byte wkb[]=convWkb.surface2wkb(multiSurface, false, 0.0, false);
        // verify

        String wkbText=Base64.encodeBytes(wkb);
        assertEquals("AAAAAAMAAAABAAAACkFENPgAAAAAQTMPdAAAAABBRDT4AAAAAEEzBXwAAAAAQUQ9swAAAABBMwV8AAAAAEFEPbMAAAAAQTMKTQAAAABBRDxqAAAAAEEzCOwAAAAAQUQ7QIAAAABBMwpNAAAAAEFEPGoAAAAAQTML3AAAAABBRD2zAAAAAEEzCk0AAAAAQUQ9swAAAABBMw90AAAAAEFENPgAAAAAQTMPdAAAAAA=",wkbText);
    }

    @Test
    public void surfaceReapirTouchingRingMultipleInnerRingSegments() throws Exception {
        IomObject multiSurface=getBananaPolygonWithInnerArcs();
        Iox2wkb convWkb=new Iox2wkb(2, java.nio.ByteOrder.BIG_ENDIAN, false);
        byte wkb[]=convWkb.surface2wkb(multiSurface, true, 0.0, true);
        // verify

        String wkbText=Base64.encodeBytes(wkb);
        assertEquals("AAAAAAoAAAACAAAAAAkAAAABAAAAAAIAAAAGQUQuPgAAAABBMwqfAAAAAEFEOYgAAAAAQTMB7QAAAABBRDmUgAAAAEEzCqsAAAAAQUQ5lIAAAABBMwqrAAAAAEFEOZSAAAAAQTMVDAAAAABBRC4+AAAAAEEzCp8AAAAAAAAAAAkAAAADAAAAAAgAAAADQUQ5lIAAAABBMwqrAAAAAEFEN36AAAAAQTMHkwAAAABBRDUlgAAAAEEzCAYAAAAAAAAAAAIAAAACQUQ1JYAAAABBMwgGAAAAAEFENRUAAAAAQTMOkQAAAAAAAAAACAAAAANBRDUVAAAAAEEzDpEAAAAAQUQ3JoAAAABBMw55AAAAAEFEOZSAAAAAQTMKqwAAAAA=",wkbText);
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
