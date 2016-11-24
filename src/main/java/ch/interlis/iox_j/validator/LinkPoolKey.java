package ch.interlis.iox_j.validator;

public class LinkPoolKey {
	
	private String oid;
	private String roleName;
	
	private LinkPoolKey() {}
	
	public LinkPoolKey(String oid, String roleName) {
		super();
		this.oid = oid;
		this.roleName = roleName;
	}
	public String getOid() {
		return oid;
	}
	public String getRoleName() {
		return roleName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((oid == null) ? 0 : oid.hashCode());
		result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
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
		LinkPoolKey other = (LinkPoolKey) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		return true;
	}
}