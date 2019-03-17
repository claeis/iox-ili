package ch.interlis.iom_j.itf.impl.jtsext.geom;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

public class JtsextGeometryFactory extends GeometryFactory {

	public JtsextGeometryFactory(PrecisionModel precisionModel, int SRID,
			CoordinateSequenceFactory coordinateSequenceFactory) {
		super(precisionModel, SRID, coordinateSequenceFactory);
	}

	public JtsextGeometryFactory(
			CoordinateSequenceFactory coordinateSequenceFactory) {
		super(coordinateSequenceFactory);
	}

	public JtsextGeometryFactory(PrecisionModel precisionModel) {
		super(precisionModel);
	}

	public JtsextGeometryFactory(PrecisionModel precisionModel, int SRID) {
		super(precisionModel, SRID);
	}

	public JtsextGeometryFactory() {
	}
	public CurveSegment createCurveSegment(CurveSegment src) {
		CurveSegment ret=null;
		if(src instanceof StraightSegment){
			ret=new StraightSegment(src.getStartPoint(),src.getEndPoint());
		}else if(src instanceof ArcSegment){
			ret=new ArcSegment(src.getStartPoint(),((ArcSegment) src).getMidPoint(),src.getEndPoint());
		}else{
			throw new IllegalArgumentException(src.getClass().getName());
		}
		return ret;
	}
	public CompoundCurve createCompoundCurve(java.util.List<CurveSegment> boundaryLine) 
	{
		return new CompoundCurve(boundaryLine,this);
	}
	public CompoundCurve createCompoundCurve(LineString srcLine) 
	{
		java.util.ArrayList<CurveSegment> newline=new java.util.ArrayList<CurveSegment>();
		if(srcLine instanceof CompoundCurve){
			CompoundCurve line=(CompoundCurve)srcLine;
			for(CurveSegment seg:line.getSegments()){
				CurveSegment newseg=createCurveSegment(seg);
				newline.add(newseg);
			}
		}else{
			Coordinate[] coords = srcLine.getCoordinates();
			for(int coordi=1;coordi<coords.length;coordi++){
				newline.add(new StraightSegment(coords[coordi-1],coords[coordi]));
			}
		}
		return new CompoundCurve(newline,this);
	}

	public CompoundCurveRing createCompoundCurveRing(CompoundCurve line){
		return new CompoundCurveRing(line, this);
	}
	public CurvePolygon createCurvePolygon(LinearRing shell, LinearRing[] holes) {
		return new CurvePolygon(shell,holes,this);
	}
	public CurvePolygon createCurvePolygon(LinearRing shell) {
		return new CurvePolygon(shell,null,this);
	}

	@Override
	public Polygon createPolygon(LinearRing shell, LinearRing[] holes) {
		if(shell instanceof CompoundCurveRing){
			return new CurvePolygon(shell,holes,this);
		}
		if(holes!=null){
			for(LinearRing hole:holes){
				if(hole instanceof CompoundCurveRing){
					return new CurvePolygon(shell,holes,this);
				}
			}
			
		}
		return super.createPolygon(shell, holes);
	}

	@Override
	public Polygon createPolygon(LinearRing shell) {
		if(shell instanceof CompoundCurveRing){
			return new CurvePolygon(shell,null,this);
		}
		return super.createPolygon(shell);
	}

	public LinearRing createRing(LineString line) {
		if(line instanceof CompoundCurve){
			return new CompoundCurveRing((CompoundCurve)line, this);
			
		}
		return createLinearRing(line.getCoordinates());
	}
}
