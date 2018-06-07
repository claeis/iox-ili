package ch.interlis.iom_j.itf.impl.jtsext.noding;

public class CurvePairInt {
	  public int c0=-1;
	  public int c1=-1;
	  public CurvePairInt(int e0,int e1)
	  {
		  if(e0>e1){
			  c0=e1;
			  c1=e0;
		  }else{
			  c0=e0;
			  c1=e1;
		  }
	  }
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + c0;
		result = prime * result + c1;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurvePairInt other = (CurvePairInt) obj;
		if (c0 != other.c0)
			return false;
		if (c1 != other.c1)
			return false;
		return true;
	}
  }