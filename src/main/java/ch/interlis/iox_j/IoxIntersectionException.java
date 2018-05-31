package ch.interlis.iox_j;

import ch.interlis.iom_j.itf.impl.jtsext.noding.Intersection;

public class IoxIntersectionException extends IoxInvalidDataException {
	Intersection is=null;
	public IoxIntersectionException(String iliqname1,String tid1,Intersection is1) {
		super(null, iliqname1, tid1, null);
		is=is1;
	}
	public Intersection getIntersection()
	{
		return is;
	}
}