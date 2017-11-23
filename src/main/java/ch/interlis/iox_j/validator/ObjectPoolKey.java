package ch.interlis.iox_j.validator;

import ch.interlis.ili2c.metamodel.Viewable;

public class ObjectPoolKey implements java.lang.Comparable {

	private String oid;
	private Viewable classValue;
	private String basketId;
	
	private ObjectPoolKey(){}
	
	public ObjectPoolKey(String oid, Viewable classValue, String basketId){
		this.setOid(oid);
		this.setClassValue(classValue);
		this.setBasketId(basketId);
	}

	@Override
	public int compareTo(Object obj) {
		if (this == obj)
			return 0;
		if (obj == null)
			return -1;
		if (getClass() != obj.getClass())
			return -1;
		ObjectPoolKey other = (ObjectPoolKey) obj;
		if (getBasketId() == null) {
			if (other.getBasketId() != null) {
				return -1;
			}
			// both basketId==null 
		} else {
			int c=getBasketId().compareTo(other.getBasketId());
			if(c!=0) {
				return c;
			}
		}
		if (getClassValue() == null) {
			if (other.getClassValue() != null)
				return -1;
		} else {
			int c=getClassValue().getScopedName().compareTo(other.getClassValue().getScopedName());
			if(c!=0) {
				return c;
			}
		}
		if (getOid() == null) {
			if (other.getOid() != null)
				return -1;
		} else {
			int c=getOid().compareTo(other.getOid());
			if(c!=0) {
				return c;
			}
		}
		return 0;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getBasketId() == null) ? 0 : getBasketId().hashCode());
		result = prime * result + ((getClassValue() == null) ? 0 : getClassValue().hashCode());
		result = prime * result + ((getOid() == null) ? 0 : getOid().hashCode());
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
		ObjectPoolKey other = (ObjectPoolKey) obj;
		if (getBasketId() == null) {
			if (other.getBasketId() != null)
				return false;
		} else if (!getBasketId().equals(other.getBasketId()))
			return false;
		if (getClassValue() == null) {
			if (other.getClassValue() != null)
				return false;
		} else if (!getClassValue().equals(other.getClassValue()))
			return false;
		if (getOid() == null) {
			if (other.getOid() != null)
				return false;
		} else if (!getOid().equals(other.getOid()))
			return false;
		return true;
	}

	private String getOid() {
		return oid;
	}

	private void setOid(String oid) {
		this.oid = oid;
	}

	private Viewable getClassValue() {
		return classValue;
	}

	private void setClassValue(Viewable classValue) {
		this.classValue = classValue;
	}

	public String getBasketId() {
		return basketId;
	}

	private void setBasketId(String basketId) {
		this.basketId = basketId;
	}

}