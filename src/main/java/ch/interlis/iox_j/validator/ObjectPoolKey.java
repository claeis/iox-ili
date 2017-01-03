package ch.interlis.iox_j.validator;

import ch.interlis.ili2c.metamodel.Viewable;

public class ObjectPoolKey {

	private String oid;
	private Viewable classValue;
	
	private ObjectPoolKey(){}
	
	public ObjectPoolKey(String oid, Viewable classValue){
		this.oid = oid;
		this.classValue = classValue;
	}

	private String getOid() {
		return oid;
	}

	private Viewable getClassValue() {
		return classValue;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classValue == null) ? 0 : classValue.hashCode());
		result = prime * result + ((oid == null) ? 0 : oid.hashCode());
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
		if (classValue == null) {
			if (other.classValue != null)
				return false;
		} else if (!classValue.equals(other.classValue))
			return false;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}
}