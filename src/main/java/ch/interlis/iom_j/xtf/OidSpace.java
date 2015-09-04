package ch.interlis.iom_j.xtf;

public class OidSpace {
	private String name=null;
	private String oiddomain=null;
	public OidSpace(String name,String oiddomain){
		this.name=name;
		this.oiddomain=oiddomain;
	}
	public String getName(){
		return name;
	}
	public String getOiddomain(){
		return oiddomain;
	}
}