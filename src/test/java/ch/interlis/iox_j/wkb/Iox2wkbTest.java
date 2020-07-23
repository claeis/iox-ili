package ch.interlis.iox_j.wkb;

import static org.junit.Assert.*;
import org.junit.Test;

import com.vividsolutions.jts.geom.MultiPolygon;

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
        assertEquals("AAAAAAIAAAATQUQTPoO2RaJBMrUXw9cKPUFEEz7g8nMWQTK1GKn87ZtBRBM/N164lkEytRmaiU+wQUQTP4axAUZBMrUalK398kFEEz/OpU6nQTK1G5eUjFBBRBNADv3y5EEytRyiXwz9QUQTQEeDxa9BMrUdtCjOsEFEE0B4BlOKQTK1HswHIKhBRBNAoFwHT0EytR/pChvdQUQTQMBiTdNBMrUhCj1wpEFEE0DX/fx6QTK1Ii6tie9BRBNA5xpNvkEytSNVW3l8QUQTQO2qTVZBMrUkfUqerEFEE0DrqFr/QTK1JaV9RYBBRBNA4RYvVEEytSbM9YAbQUQTQM382lNBMrUn8rYAcUFEE0CybLuVQTK1KRXC8XBBRBNAjn10SkEytSo1Is7kQUQTQGJN0vJBMrUrT987ZA==",wkbText);
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
        byte wkb[]=conv.surface2wkb(multiSurface, true, 0.0);
        // verify
        String wkbText=Base64.encodeBytes(wkb);
        assertEquals("AAAAAAoAAAABAAAAAAkAAAABAAAAAAIAAAAEQR1MAAAAAABA8RcAAAAAAEEehIAAAAAAQPOIAAAAAABBIMjgAAAAAED1+QAAAAAAQR1MAAAAAABA8RcAAAAAAA==",wkbText);
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
        assertEquals("Ae4DAAABAAAAAesDAAABAAAABQAAAAAAAAAYZi1BAAAAAKpwMEEAAAAAAADwPwAAAAAgZi1BAAAAAKNwMEEAAAAAAADwPwAAAAAoZi1BAAAAAKVwMEEAAAAAAAAAQAAAAAAeZi1BAAAAAKxwMEEAAAAAAAAAQAAAAAAYZi1BAAAAAKpwMEEAAAAAAADwPw==",wkbText);
    }
    
    @Test
    public void multiSurfaceZToExtendedWkb() throws Exception{
        IomObject multiSurface=getMultiSurfaceZ();
        Iox2wkb convEwkb=new Iox2wkb(3,java.nio.ByteOrder.LITTLE_ENDIAN, true);
        byte ewkb[]=convEwkb.multisurface2wkb(multiSurface, false, 0.0);
        // verify
        String ewkbText=Base64.encodeBytes(ewkb);
        assertEquals("AQYAAIABAAAAAQMAAIABAAAABQAAAAAAAAAYZi1BAAAAAKpwMEEAAAAAAADwPwAAAAAgZi1BAAAAAKNwMEEAAAAAAADwPwAAAAAoZi1BAAAAAKVwMEEAAAAAAAAAQAAAAAAeZi1BAAAAAKxwMEEAAAAAAAAAQAAAAAAYZi1BAAAAAKpwMEEAAAAAAADwPw==",ewkbText);
    }
}
