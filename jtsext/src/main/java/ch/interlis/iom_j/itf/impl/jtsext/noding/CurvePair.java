package ch.interlis.iom_j.itf.impl.jtsext.noding;

import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;

class CurvePair {
	  public CompoundCurve c0=null;
	  public CompoundCurve c1=null;
	  public CurvePair(CompoundCurve e0,CompoundCurve e1)
	  {
		  if(e0.hashCode()>e1.hashCode()){
			  c0=e1;
			  c1=e0;
		  }else{
			  c0=e0;
			  c1=e1;
		  }
	  }
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurvePair other = (CurvePair) obj;
		if(c0==other.c0){
			if(c1==other.c1){
				return true;
			}
			return false;
		}
		if(c0==other.c1){
			if(c1==other.c0){
				return true;
			}
			return false;
		}
		return false;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((c0 == null) ? 0 : c0.hashCode());
		result = prime * result + ((c1 == null) ? 0 : c1.hashCode());
		return result;
	}
  }