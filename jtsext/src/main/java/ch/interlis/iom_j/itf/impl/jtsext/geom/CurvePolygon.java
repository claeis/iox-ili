package ch.interlis.iom_j.itf.impl.jtsext.geom;

import java.util.ArrayList;
import java.util.Arrays;

import com.vividsolutions.jts.algorithm.CGAlgorithms;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateArrays;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

public class CurvePolygon extends Polygon {

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
}
