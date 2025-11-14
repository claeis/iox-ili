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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		StraightSegment that = (StraightSegment) o;

		return (coords[0].equals(that.coords[0]) && coords[1].equals(that.coords[1]))
				|| (coords[0].equals(that.coords[1]) && coords[1].equals(that.coords[0]));
	}

	@Override
	public int hashCode() {
		Coordinate p0;
		Coordinate p1;
		if (coords[0].compareTo(coords[1]) > 0) {
			p0 = coords[0];
			p1 = coords[1];
		} else {
			p0 = coords[1];
			p1 = coords[0];
		}

		int result = p0.hashCode();
		result = 31 * result + p1.hashCode();
		return result;
	}

	@Override
	public boolean equals2D(CurveSegment other, double tolerance) {
		if (this.getClass() != other.getClass()) {
			return false;
		}

		Coordinate startA = getStartPoint();
		Coordinate endA = getEndPoint();
		Coordinate startB = other.getStartPoint();
		Coordinate endB = other.getEndPoint();

		return (startA.equals2D(startB, tolerance) && endA.equals2D(endB, tolerance))
				|| (startA.equals2D(endB, tolerance) && endA.equals2D(startB, tolerance));
	}
}
