package ch.interlis.iom_j.iligml;

public class Pair {
    private String role1Id;
	private String role2Id;
	
	public Pair(String roleId1, String roleId2){
		super();
		this.role1Id = roleId1;
		this.role2Id = roleId2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role1Id == null) ? 0 : role1Id.hashCode());
		result = prime * result + ((role2Id == null) ? 0 : role2Id.hashCode());
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
		Pair other = (Pair) obj;
		if (role1Id == null) {
			if (other.role1Id != null)
				return false;
		} else if (!role1Id.equals(other.role1Id))
			return false;
		if (role2Id == null) {
			if (other.role2Id != null)
				return false;
		} else if (!role2Id.equals(other.role2Id))
			return false;
		return true;
	}

	public String getRoleId1() {
		return role1Id;
	}

	public String getRoleId2() {
		return role2Id;
	}
}