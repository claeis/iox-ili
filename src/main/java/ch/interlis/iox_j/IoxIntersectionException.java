package ch.interlis.iox_j;

import ch.interlis.iom_j.itf.impl.jtsext.noding.Intersection;

public class IoxIntersectionException extends IoxInvalidDataException {
	Intersection is=null;
	
	public IoxIntersectionException(Intersection is) {
		this.is=is;
	}
	public Intersection getIntersection()
	{
		return is;
	}
}