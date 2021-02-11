package ch.interlis.iox_j.wkb;

import org.junit.Assert;
import org.junit.Test;

import ch.interlis.iom.IomObject;

public class Wkb2ioxTest {
    @Test
    public void wkbType238() throws Exception {
        byte cb[]=new byte[] {1, -18, 3, 0, 0, 1, 0, 0, 0, 1, -21, 3, 0, 0, 1, 0, 0, 0, 5, 0, 0, 0, 82, 57, 5, 113, -101, -125, 49, 65, -70, -35, -75, -44, -21, 19, 43, 65, 0, 0, 0, 0, 0, 0, 0, 0, -98, 121, 86, -22, -111, -125, 49, 65, -17, -66, 112, -92, -21, 19, 43, 65, 0, 0, 0, 0, 0, 0, 0, 0, -88, -70, 87, -44, -111, -125, 49, 65, -31, -127, 69, -6, -13, 19, 43, 65, 0, 0, 0, 0, 0, 0, 0, 0, -50, -63, 50, 101, -101, -125, 49, 65, 42, 113, -37, 62, -12, 19, 43, 65, 0, 0, 0, 0, 0, 0, 0, 0, 82, 57, 5, 113, -101, -125, 49, 65, -70, -35, -75, -44, -21, 19, 43, 65, 0, 0, 0, 0, 0, 0, 0, 0};
        Wkb2iox wkb2iox=new Wkb2iox();
        IomObject iomObj = wkb2iox.read(cb);
        Assert.assertEquals("MULTISURFACE", iomObj.getobjecttag());
    }
    @Test
    public void multisurfaceWithCircularstring() throws Exception {
        // SELECT encode(st_asbinary(ST_GeomFromText('MULTISURFACE(CURVEPOLYGON(CIRCULARSTRING(0 0, 4 0, 4 4, 0 4, 0 0),(1 1, 3 3, 3 1, 1 1)))')),'base64') as geom;
        byte cb[]=net.iharder.Base64.decode("AQwAAAABAAAAAQoAAAACAAAAAQgAAAAFAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBAAAAAAAAA\r\n" + 
        "AAAAAAAAAAAQQAAAAAAAABBAAAAAAAAAAAAAAAAAAAAQQAAAAAAAAAAAAAAAAAAAAAABAgAAAAQA\r\n" + 
        "AAAAAAAAAADwPwAAAAAAAPA/AAAAAAAACEAAAAAAAAAIQAAAAAAAAAhAAAAAAAAA8D8AAAAAAADw\r\n" + 
        "PwAAAAAAAPA/");
        
        Wkb2iox wkb2iox=new Wkb2iox();
        IomObject iomObj = wkb2iox.read(cb);
        Assert.assertEquals("MULTISURFACE", iomObj.getobjecttag());
        Assert.assertEquals("MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 0.0, C2 0.0}, ARC {A1 4.0, A2 0.0, C1 4.0, C2 4.0}, ARC {A1 0.0, A2 4.0, C1 0.0, C2 0.0}]}}}, BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 1.0, C2 1.0}, COORD {C1 3.0, C2 3.0}, COORD {C1 3.0, C2 1.0}, COORD {C1 1.0, C2 1.0}]}}}]}}",iomObj.toString());
    }

}
