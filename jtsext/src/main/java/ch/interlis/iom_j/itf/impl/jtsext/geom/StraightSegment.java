package ch.interlis.iom_j.itf.impl.jtsext.geom;


import com.vividsolutions.jts.geom.Coordinate;

public class StraightSegment extends CurveSegment {

	private Coordinate coords[]=new Coordinate[2];

	public StraightSegment(Object userData,Coordinate startPoint, Coordinate endPoint) {
		this.userData=userData;
		this.coords[0]=new Coordinate(startPoint);
		this.coords[1]=new Coordinate(endPoint);
	}
	public StraightSegment(Coordinate startPoint, Coordinate endPoint) {
		this(null,startPoint,endPoint);
	}

	@Override
	public Coordinate getEndPoint() {
		return coords[1];
	}

	@Override
	public Coordinate getStartPoint() {
		return coords[0];
	}

	@Override
	public Coordinate[] getCoordinates() {
		return coords;
	}
	@Override
	public String toString()
	{
		return "("+coords[0].x+" "+coords[0].y+", "+coords[1].x+" "+coords[1].y+")";
	}

}
