package ch.interlis.iox_j.validator;

import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;

public class IomObjectHelper {

    /**
     * Create an IomObject containing a rectangle surface geometry with the specified corner coordinates.
     */
    public static IomObject createRectangleGeometry(String x1, String y1, String x2, String y2) {
        IomObject startSegment = createCoord(x1, y1);
        IomObject straightSegment1 = createCoord(x1, y2);
        IomObject straightSegment2 = createCoord(x2, y2);
        IomObject straightSegment3 = createCoord(x2, y1);
        IomObject straightSegment4 = createCoord(x1, y1);

        IomObject segment = new Iom_jObject("SEGMENTS", null);
        segment.addattrobj("segment", startSegment);
        segment.addattrobj("segment", straightSegment1);
        segment.addattrobj("segment", straightSegment2);
        segment.addattrobj("segment", straightSegment3);
        segment.addattrobj("segment", straightSegment4);

        IomObject polyline = new Iom_jObject("POLYLINE", null);
        polyline.addattrobj("sequence", segment);

        IomObject outerBoundary = new Iom_jObject("BOUNDARY", null);
        outerBoundary.addattrobj("polyline", polyline);

        IomObject surfaceValue = new Iom_jObject("SURFACE", null);
        surfaceValue.addattrobj("boundary", outerBoundary);

        IomObject multisurface = new Iom_jObject("MULTISURFACE", null);
        multisurface.addattrobj("surface", surfaceValue);

        return multisurface;
    }

    public static Iom_jObject createCoord(String c1, String c2) {
        Iom_jObject coord = new Iom_jObject("COORD", null);
        coord.setattrvalue("C1", c1);
        coord.setattrvalue("C2", c2);
        return coord;
    }
}
