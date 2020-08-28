package ch.interlis.iom_j.itf.impl.jtsext.geom;

import java.util.ArrayList;
import java.util.Arrays;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iom_j.itf.impl.jtsext.io.WKTWriterJtsext;

import com.vividsolutions.jts.algorithm.CGAlgorithms;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateArrays;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

public class CurvePolygon extends Polygon {

	public static final String VALID_OVERLAP = "valoverlap";

    public CurvePolygon(LinearRing shell, LinearRing[] holes,
			GeometryFactory factory) {
		super(shell, holes, factory);
	}

	@Override
	public void normalize() {
	    normalize(shell, true);
	    for (int i = 0; i < holes.length; i++) {
	      normalize(holes[i], false);
	    }
	    Arrays.sort(holes);
	}

	@Override
	public Geometry reverse() {
		// TODO Auto-generated method stub
		return super.reverse();
	}

	// @Override super.normalize(LinearRing ring, boolean clockwise) not visible
	  private void normalize(LinearRing ring, boolean clockwise) {
		    if (ring.isEmpty()) {
		      return;
		    }
		    if(ring instanceof CompoundCurveRing){
		    	CompoundCurveRing cring=(CompoundCurveRing)ring;
		    	ArrayList<CurveSegment> segv=new ArrayList<CurveSegment>();
		    	for(CompoundCurve line:cring.getLines()){
		    		for(CurveSegment seg:line.getSegments()){
		    			segv.add(seg);
		    		}
		    	}
		    	ArrayList<CurveSegment> newsegv=CompoundCurveRing.normalizeRing(clockwise, segv);
			    CompoundCurve newline=null;
				newline = ((JtsextGeometryFactory) this.factory).createCompoundCurve(newsegv);
			    cring.setLines(newline);
		    }else{
			    Coordinate[] uniqueCoordinates = new Coordinate[ring.getCoordinates().length - 1];
			    System.arraycopy(ring.getCoordinates(), 0, uniqueCoordinates, 0, uniqueCoordinates.length);
			    Coordinate minCoordinate = CoordinateArrays.minCoordinate(ring.getCoordinates());
			    CoordinateArrays.scroll(uniqueCoordinates, minCoordinate);
			    System.arraycopy(uniqueCoordinates, 0, ring.getCoordinates(), 0, uniqueCoordinates.length);
			    ring.getCoordinates()[uniqueCoordinates.length] = uniqueCoordinates[0];
			    if (CGAlgorithms.isCCW(ring.getCoordinates()) == clockwise) {
			      CoordinateArrays.reverse(ring.getCoordinates());
			    }
		    }
		  }
		@Override
		public String toText() {
		    WKTWriterJtsext writer = new WKTWriterJtsext();
		    return writer.write(this);
		}
		public void dumpPolygonAsJava(String poly) {
			EhiLogger.debug("CompoundCurveRing shell=null;");
		    dumpRingAsJava(shell,"shell");
		    if(holes.length>0){
				EhiLogger.debug("CompoundCurveRing holes[]=new CompoundCurveRing["+holes.length+"];");
			    for (int i = 0; i < holes.length; i++) {
			      dumpRingAsJava(holes[i],"holes[i]");
			    }
		    }
		    if(holes.length>0){
				EhiLogger.debug(poly+"=new CurvePolygon(shell,holes);");
		    }else{
				EhiLogger.debug(poly+"=new CurvePolygon(shell);");
		    }
		}

		public void dumpRingAsJava(LinearRing shell,String ring) {
			EhiLogger.debug("ArrayList<CurveSegment> segs = new ArrayList<CurveSegment>();");
			if(shell instanceof CompoundCurveRing){
				CompoundCurveRing ringo=(CompoundCurveRing )shell;
				for(CompoundCurve line:ringo.getLines()){
					line.dumpLineAsJava("segs");
				}
			}else{
				throw new IllegalArgumentException("not yet implemented");
			}
			EhiLogger.debug("CompoundCurve line=new CompoundCurve(segs);");
			EhiLogger.debug(ring+"=new CompoundCurveRing(line);");
		}

		public static boolean polygonOverlays(Polygon e0, Polygon e1) {
			return e0.equals(e1) || e0.overlaps(e1) || e0.within(e1) || e1.within(e0);
		}
}
