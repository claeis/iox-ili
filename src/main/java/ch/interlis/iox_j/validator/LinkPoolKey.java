package ch.interlis.iox_j.validator;

public class LinkPoolKey {
	
	private String oid;
	private String className;
	private String roleName;
	
	private LinkPoolKey() {}
	
	public LinkPoolKey(String oid, String className, String roleName){
		super();
		this.oid = oid;
		this.className = className;
		this.roleName = roleName;
	}
	
	public String getOid() {
		return oid;
	}
	public String getClassName() {
		return className;
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
		if(className!=null){
			result = prime * result + ((className == null) ? 0 : className.hashCode());
		}
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