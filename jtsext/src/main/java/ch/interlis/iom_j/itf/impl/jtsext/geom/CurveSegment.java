package ch.interlis.iom_j.itf.impl.jtsext.geom;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

abstract public class CurveSegment implements java.io.Serializable {
	protected Object userData=null;
	public CurveSegment() {
	}

	abstract public Coordinate getStartPoint();
	abstract public Coordinate getEndPoint();
	abstract public Coordinate[] getCoordinates();

	Envelope expandEnvelope(Envelope env){
		env.expandToInclude(getStartPoint());
		env.expandToInclude(getEndPoint());
		return env;
	}
	public Envelope computeEnvelopeInternal() {
		Envelope env=new Envelope();
		return expandEnvelope(env);
	}
	public static double dist(double re1,double ho1,double re2,double ho2)
	{
		double ret;
		ret=Math.hypot(re2-re1,ho2-ho1);
		return ret;
	}
	public static double dist(Coordinate p1,Coordinate p2)
	{
		double ret;
		ret=Math.hypot(p2.x-p1.x,p2.y-p1.y);
		return ret;
	}

	public Object getUserData() {
		return userData;
	}

	public void setUserData(Object userData) {
		this.userData = userData;
	}
}
