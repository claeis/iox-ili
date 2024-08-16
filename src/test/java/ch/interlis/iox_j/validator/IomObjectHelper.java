package ch.interlis.iox_j.validator;

import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;

public class IomObjectHelper {

    /**
     * Create an IomObject containing a rectangle surface geometry with the specified corner coordinates.
     */
    public static IomObject createRectangleGeometry(String x1, String y1, String x2, String y2) {
        return createPolygonFromBoundaries(createRectangleBoundary(x1, y1, x2, y2));
    }

    public static IomObject createRectangleBoundary(String x1, String y1, String x2, String y2) {
        return createBoundary(
                createCoord(x1, y1),
                createCoord(x1, y2),
                createCoord(x2, y2),
                createCoord(x2, y1),
                createCoord(x1, y1));
    }

    public static IomObject createPolygonFromBoundaries(IomObject... boundary) {
        IomObject surfaceValue = new Iom_jObject("SURFACE", null);
        for (IomObject b : boundary) {
            surfaceValue.addattrobj("boundary", b);
        }

        IomObject multisurface = new Iom_jObject("MULTISURFACE", null);
        multisurface.addattrobj("surface", surfaceValue);

        return multisurface;
    }

    /**
     * Create a BOUNDARY object consisting of one POLYLINE object with the specified segments.
     */
    public static IomObject createBoundary(IomObject... segments) {
        IomObject polyline = createPolyline(segments);

        IomObject boundary = new Iom_jObject("BOUNDARY", null);
        boundary.addattrobj("polyline", polyline);
        return boundary;
    }

    /**
     * Create a MULTIPOLYLINE object from the specified polylines.
     */
    public static IomObject createMultiPolyline(IomObject... polylines) {
        IomObject multiPolyline = new Iom_jObject("MULTIPOLYLINE", null);
        for (IomObject polyline : polylines) {
            multiPolyline.addattrobj("polyline", polyline);
        }

        return multiPolyline;
    }

    /**
     * Create a POLYLINE object with the specified segments.
     */
    public static IomObject createPolyline(IomObject... segments) {
        IomObject polylineSegments = new Iom_jObject("SEGMENTS", null);
        for (IomObject segment : segments) {
            polylineSegments.addattrobj("segment", segment);
        }

        IomObject polyline = new Iom_jObject("POLYLINE", null);
        polyline.addattrobj("sequence", polylineSegments);

        return polyline;
    }

    /**
     * Create a BOUNDARY object consisting of POLYLINE object for each segment.
     */
    public static IomObject createMultiplePolylineBoundary(IomObject... segments) {
        IomObject boundary = new Iom_jObject("BOUNDARY", null);

        for (int i = 1; i < segments.length; i++) {
            IomObject lineSegment = new Iom_jObject("SEGMENTS", null);

            if (segments[i - 1].getattrvalue("C3") == null) {
                lineSegment.addattrobj("segment", createCoord(
                        segments[i - 1].getattrvalue("C1"),
                        segments[i - 1].getattrvalue("C2")));
            } else {
                lineSegment.addattrobj("segment", createCoord(
                        segments[i - 1].getattrvalue("C1"),
                        segments[i - 1].getattrvalue("C2"),
                        segments[i - 1].getattrvalue("C3")));
            }
            lineSegment.addattrobj("segment", segments[i]);

            IomObject polyline = new Iom_jObject("POLYLINE", null);
            polyline.addattrobj("sequence", lineSegment);

            boundary.addattrobj("polyline", polyline);
        }

        return boundary;
    }

    public static IomObject createCoord(String c1, String c2) {
        IomObject coord = new Iom_jObject("COORD", null);
        coord.setattrvalue("C1", c1);
        coord.setattrvalue("C2", c2);
        return coord;
    }

    public static IomObject createCoord(String c1, String c2, String c3) {
        IomObject coord = createCoord(c1, c2);
        coord.setattrvalue("C3", c3);
        return coord;
    }

    public static IomObject createArc(String a1, String a2, String c1, String c2) {
        IomObject arc = new Iom_jObject("ARC", null);
        arc.setattrvalue("A1", a1);
        arc.setattrvalue("A2", a2);
        arc.setattrvalue("C1", c1);
        arc.setattrvalue("C2", c2);
        return arc;
    }

    public static IomObject createArc(String a1, String a2, String c1, String c2, String c3) {
        IomObject arc = createArc(a1, a2, c1, c2);
        arc.setattrvalue("C3", c3);
        return arc;
    }
}
