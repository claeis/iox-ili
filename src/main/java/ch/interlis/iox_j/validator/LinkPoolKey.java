package ch.interlis.iox_j.validator;

public class LinkPoolKey {
	
	private String oid;
	private String className;
	private String roleName;
	private boolean doItfOidPerTable;
	
	private LinkPoolKey() {}
	
	public LinkPoolKey(String oid, String roleName, String className, boolean doItfOidPerTable) {
		super();
		this.oid = oid;
		this.className = className;
		this.roleName = roleName;
		this.doItfOidPerTable = doItfOidPerTable;
	}
	
	public LinkPoolKey(String oid, String roleName, boolean doItfOidPerTable) {
		super();
		this.oid = oid;
		this.roleName = roleName;
		this.doItfOidPerTable = doItfOidPerTable;
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
	public boolean getDoItOidPerTable() {
		return doItfOidPerTable;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((oid == null) ? 0 : oid.hashCode());
		result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
		if(doItfOidPerTable){
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
		if(doItfOidPerTable){
			if (className == null) {
				if (other.className != null)
					return false;
			} else if (!className.equals(other.className))
				return false;
		}
		return true;
	}
}