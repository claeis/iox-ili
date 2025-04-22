package ch.interlis.iox_j.wkb;

import static org.junit.Assert.*;

import java.util.List;

import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKBReader;
import org.junit.Test;

import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.jts.Iox2jtsext;
import ch.interlis.iox_j.jts.Jtsext2iox;


public class RingCollectorTest {
    private JtsextGeometryFactory fact=new JtsextGeometryFactory();
    
    private List<CompoundCurve> calcRings(IomObject polygonIom) throws IoxException {
        Polygon polygon=Iox2jtsext.surface2JTS(polygonIom,0.0);
        RingCollector ringCollector=new RingCollector();
        ringCollector.addLine(fact.createCompoundCurve(polygon.getExteriorRing()));
        for(int i=0;i<polygon.getNumInteriorRing();i++) {
            ringCollector.addLine(fact.createCompoundCurve(polygon.getInteriorRingN(i)));
        }
        List<CompoundCurve> rings=ringCollector.getRings();
        return rings;
    }
    
    @Test
    public void surfaceRepairTouchingRing_Ok() throws Exception {
        IomObject polygonIom=getBananaPolygon();
        List<CompoundCurve> rings = calcRings(polygonIom);
        assertEquals(2,rings.size());
        assertEquals("POLYLINE {sequence SEGMENTS {segment [COORD {C1 2648560.0, C2 1249140.0}, COORD {C1 2648560.0, C2 1246588.0}, COORD {C1 2653030.0, C2 1246588.0}, COORD {C1 2653030.0, C2 1247821.0}, COORD {C1 2653030.0, C2 1249140.0}, COORD {C1 2648560.0, C2 1249140.0}]}}",Jtsext2iox.JTS2polyline(rings.get(0)).toString());
        assertEquals("POLYLINE {sequence SEGMENTS {segment [COORD {C1 2653030.0, C2 1247821.0}, COORD {C1 2652372.0, C2 1247468.0}, COORD {C1 2651777.0, C2 1247821.0}, COORD {C1 2652372.0, C2 1248220.0}, COORD {C1 2653030.0, C2 1247821.0}]}}",Jtsext2iox.JTS2polyline(rings.get(1)).toString());
    }


    @Test
    public void surfaceRepairTouchingRingInStart_Ok() throws Exception {
        IomObject multiSurface=getBananaPolygonStartOnOverlap();
        List<CompoundCurve> rings = calcRings(multiSurface);
        assertEquals(2,rings.size());
        assertEquals("POLYLINE {sequence SEGMENTS {segment [COORD {C1 2653030.0, C2 1247821.0}, COORD {C1 2653030.0, C2 1249140.0}, COORD {C1 2648560.0, C2 1249140.0}, COORD {C1 2648560.0, C2 1246588.0}, COORD {C1 2653030.0, C2 1246588.0}, COORD {C1 2653030.0, C2 1247821.0}]}}",Jtsext2iox.JTS2polyline(rings.get(0)).toString());
        assertEquals("POLYLINE {sequence SEGMENTS {segment [COORD {C1 2653030.0, C2 1247821.0}, COORD {C1 2652372.0, C2 1247468.0}, COORD {C1 2651777.0, C2 1247821.0}, COORD {C1 2652372.0, C2 1248220.0}, COORD {C1 2653030.0, C2 1247821.0}]}}",Jtsext2iox.JTS2polyline(rings.get(1)).toString());
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
        List<CompoundCurve> rings = calcRings(multiSurface);
        assertEquals(3,rings.size());
        assertEquals("POLYLINE {sequence SEGMENTS {segment [COORD {C1 2648560.0, C2 1249140.0}, COORD {C1 2648560.0, C2 1246588.0}, COORD {C1 2653030.0, C2 1246588.0}, COORD {C1 2653030.0, C2 1247821.0}, COORD {C1 2653030.0, C2 1249140.0}, COORD {C1 2648560.0, C2 1249140.0}]}}",Jtsext2iox.JTS2polyline(rings.get(0)).toString());
        assertEquals("POLYLINE {sequence SEGMENTS {segment [COORD {C1 2651777.0, C2 1247821.0}, COORD {C1 2651200.0, C2 1247428.0}, COORD {C1 2650613.0, C2 1247879.0}, COORD {C1 2651207.0, C2 1248246.0}, COORD {C1 2651777.0, C2 1247821.0}]}}",Jtsext2iox.JTS2polyline(rings.get(1)).toString());
        assertEquals("POLYLINE {sequence SEGMENTS {segment [COORD {C1 2653030.0, C2 1247821.0}, COORD {C1 2652372.0, C2 1247468.0}, COORD {C1 2651777.0, C2 1247821.0}, COORD {C1 2652372.0, C2 1248220.0}, COORD {C1 2653030.0, C2 1247821.0}]}}",Jtsext2iox.JTS2polyline(rings.get(2)).toString());

    }

    @Test
    public void surfaceReapirTouchingRingMultipleInnerRingSegments() throws Exception {
        IomObject multiSurface=getBananaPolygonWithInnerArcs();
        List<CompoundCurve> rings = calcRings(multiSurface);
        assertEquals(2,rings.size());
        assertEquals("POLYLINE {sequence SEGMENTS {segment [COORD {C1 2645116.0, C2 1247903.0}, COORD {C1 2650896.0, C2 1245677.0}, COORD {C1 2650921.0, C2 1247915.0}, COORD {C1 2650921.0, C2 1250572.0}, COORD {C1 2645116.0, C2 1247903.0}]}}",Jtsext2iox.JTS2polyline(rings.get(0)).toString());
        assertEquals("POLYLINE {sequence SEGMENTS {segment [COORD {C1 2650921.0, C2 1247915.0}, ARC {A1 2649853.0, A2 1247123.0, C1 2648651.0, C2 1247238.0}, COORD {C1 2648618.0, C2 1248913.0}, ARC {A1 2649677.0, A2 1248889.0, C1 2650921.0, C2 1247915.0}]}}",Jtsext2iox.JTS2polyline(rings.get(1)).toString());
    }
    @Test
    public void surfaceReapirTouchingRingWithArcs() throws Exception {
        IomObject multiSurface=getBananaPolygonWithArcs();

        List<CompoundCurve> rings = calcRings(multiSurface);
        assertEquals(2,rings.size());
        assertEquals("POLYLINE {sequence SEGMENTS {segment [COORD {C1 2755031.988, C2 1245955.668}, ARC {A1 2755031.058, A2 1245955.258, C1 2755030.142, C2 1245954.817}, COORD {C1 2755020.0, C2 1245926.0}, COORD {C1 2755053.689, C2 1245956.226}, ARC {A1 2755042.786, A2 1245957.99, C1 2755031.988, C2 1245955.668}]}}",Jtsext2iox.JTS2polyline(rings.get(0)).toString());
        assertEquals("POLYLINE {sequence SEGMENTS {segment [COORD {C1 2755025.29, C2 1245931.463}, ARC {A1 2755026.933, A2 1245944.037, C1 2755031.988, C2 1245955.668}, COORD {C1 2755036.86, C2 1245951.575}, COORD {C1 2755025.29, C2 1245931.463}]}}",Jtsext2iox.JTS2polyline(rings.get(1)).toString());
    }
    
    @Test
    public void surfaceRepairDuplicateCoordMidPt_Ok() throws Exception {
        IomObject multiSurface=null;
        {
            multiSurface=new Iom_jObject("MULTISURFACE", null);
            IomObject surfaceValue = multiSurface.addattrobj("surface", "SURFACE");
            // outer boundary
            IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
            // polyline
            IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");

            IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
            IomObject coord =null;
            coord = segments.addattrobj("segment", "COORD");
            coord.setattrvalue("C1", "2724229.757");
            coord.setattrvalue("C2", "1261159.278");
            coord = segments.addattrobj("segment", "COORD");
            coord.setattrvalue("C1", "2724229.841");
            coord.setattrvalue("C2", "1261159.345");
            coord = segments.addattrobj("segment", "ARC");
            coord.setattrvalue("C1", "2724229.921");
            coord.setattrvalue("C2", "1261159.418");
            coord.setattrvalue("A1", "2724229.841");  /* wrong: repeats previous COORD */
            coord.setattrvalue("A2", "1261159.345");
            coord = segments.addattrobj("segment", "COORD");
            coord.setattrvalue("C1", "2724226.223");
            coord.setattrvalue("C2", "1261161.926");
            coord = segments.addattrobj("segment", "COORD");
            coord.setattrvalue("C1", "2724229.757");
            coord.setattrvalue("C2", "1261159.278");
        }

        List<CompoundCurve> rings = calcRings(multiSurface);
        assertEquals(1,rings.size());
        assertEquals("POLYLINE {sequence SEGMENTS {segment [COORD {C1 2724229.757, C2 1261159.278}, COORD {C1 2724229.841, C2 1261159.345}, COORD {C1 2724229.921, C2 1261159.418}, COORD {C1 2724226.223, C2 1261161.926}, COORD {C1 2724229.757, C2 1261159.278}]}}",Jtsext2iox.JTS2polyline(rings.get(0)).toString());
        
    }
    @Test
    public void surfaceRepairDuplicateCoordArc_Ok() throws Exception {
        IomObject multiSurface=null;
        {
            multiSurface=new Iom_jObject("MULTISURFACE", null);
            IomObject surfaceValue = multiSurface.addattrobj("surface", "SURFACE");
            // outer boundary
            IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
            // polyline
            IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");

            IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
            IomObject coord =null;
            coord = segments.addattrobj("segment", "COORD");
            coord.setattrvalue("C1", "2571002.385");
            coord.setattrvalue("C2", "1163696.294");
            coord = segments.addattrobj("segment", "COORD");
            coord.setattrvalue("C1", "2571002.449");
            coord.setattrvalue("C2", "1163696.277");
            coord = segments.addattrobj("segment", "ARC");
            coord.setattrvalue("C1", "2571002.450");  /* wrong: midpt and endpt the same */
            coord.setattrvalue("C2", "1163696.277");
            coord.setattrvalue("A1", "2571002.450");
            coord.setattrvalue("A2", "1163696.277");
            coord = segments.addattrobj("segment", "COORD");
            coord.setattrvalue("C1", "2571002.385");
            coord.setattrvalue("C2", "1163696.294");
        }
        List<CompoundCurve> rings = calcRings(multiSurface);
        assertEquals(1,rings.size());
        assertEquals("POLYLINE {sequence SEGMENTS {segment [COORD {C1 2571002.385, C2 1163696.294}, COORD {C1 2571002.449, C2 1163696.277}, COORD {C1 2571002.45, C2 1163696.277}, COORD {C1 2571002.385, C2 1163696.294}]}}",Jtsext2iox.JTS2polyline(rings.get(0)).toString());
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


    private IomObject getBananaPolygonWithInnerArcs() {
        IomObject multiSurface=new Iom_jObject(Iom_jObject.MULTISURFACE, null);
        IomObject surfaceValue = multiSurface.addattrobj(Iom_jObject.MULTISURFACE_SURFACE, Iom_jObject.SURFACE);
        // outer boundary
        IomObject outerBoundary = surfaceValue.addattrobj(Iom_jObject.SURFACE_BOUNDARY, Iom_jObject.BOUNDARY);
        IomObject polylineValue = outerBoundary.addattrobj(Iom_jObject.BOUNDARY_POLYLINE, Iom_jObject.POLYLINE);

        IomObject segments=polylineValue.addattrobj(Iom_jObject.POLYLINE_SEQUENCE, Iom_jObject.SEGMENTS);
        addCoord(segments,"2645116.000","1247903.000");
        addCoord(segments,"2650896.000","1245677.000");
        addCoord(segments,"2650921.000","1247915.000");
        addArc(segments,"2648651.000","1247238.000","2649853.000","1247123.000");
        addCoord(segments,"2648618.000","1248913.000");
        addArc(segments,"2650921.000","1247915.000","2649677.000","1248889.000");
        addCoord(segments,"2650921.000","1250572.000");
        addCoord(segments,"2645116.000","1247903.000");
        return multiSurface;
    }
    private IomObject getBananaPolygonWithArcs() {
        IomObject multiSurface=new Iom_jObject(Iom_jObject.MULTISURFACE, null);
        IomObject surfaceValue = multiSurface.addattrobj(Iom_jObject.MULTISURFACE_SURFACE, Iom_jObject.SURFACE);
        // outer boundary
        IomObject outerBoundary = surfaceValue.addattrobj(Iom_jObject.SURFACE_BOUNDARY, Iom_jObject.BOUNDARY);
        IomObject polylineValue = outerBoundary.addattrobj(Iom_jObject.BOUNDARY_POLYLINE, Iom_jObject.POLYLINE);

      IomObject segments=polylineValue.addattrobj(Iom_jObject.POLYLINE_SEQUENCE, Iom_jObject.SEGMENTS);
      
      addCoord(segments,"2755025.290","1245931.463"); // startpunkt letztes segmend innerer Rand
      addArc(segments,"2755031.988","1245955.668", // endpunkt innerer Rand/startpunkt aeusserer Rand
        "2755026.933", // letzter Bogenzwischenpunkt innerer Rand
        "1245944.037");
      
      
      addArc(segments,    //  aeusserer Rand
        "2755030.142",
        "1245954.817",
        "2755031.058",
        "1245955.258");
      addCoord(segments,
        "2755020.000",  // tiefe linke ecke
        "1245926.000");
      addCoord(segments,
        "2755053.689",
        "1245956.226");
      addArc(segments, // letztes Segment aeusserer Rand
        "2755031.988", // endpunkt aeusserer Rand/startpunkt innerer Rand
        "1245955.668",
        "2755042.786", // Bogenzwischenpunkt aeusserer Rand
        "1245957.990");
      
      addCoord(segments, // innerer Rand -->
        "2755036.860", // endpunkt erstes Segment innerer Rand
        "1245951.575");
      addCoord(segments,
        "2755025.290", //startpunkt letztes segmend innerer Rand, tiefe linke ecke
        "1245931.463");
        
        return multiSurface;
    }
    private void addArc(IomObject segments, String c1, String c2, String a1, String a2) {
        IomObject obj=segments.addattrobj(Iom_jObject.SEGMENTS_SEGMENT, Iom_jObject.ARC);
        obj.setattrvalue(Iom_jObject.COORD_C1, c1);
        obj.setattrvalue(Iom_jObject.COORD_C2, c2);
        obj.setattrvalue(Iom_jObject.ARC_A1, a1);
        obj.setattrvalue(Iom_jObject.ARC_A2, a2);
    }
    private void addCoord(IomObject segments, String c1, String c2) {
        IomObject obj=segments.addattrobj(Iom_jObject.SEGMENTS_SEGMENT, Iom_jObject.COORD);
        obj.setattrvalue(Iom_jObject.COORD_C1, c1);
        obj.setattrvalue(Iom_jObject.COORD_C2, c2);
    }
}
