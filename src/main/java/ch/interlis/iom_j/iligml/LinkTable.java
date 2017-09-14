package ch.interlis.iom_j.iligml;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import ch.interlis.ili2c.metamodel.RoleDef;

public class LinkTable {
	
	private RoleDef role1=null;
	private RoleDef role2=null;
	public Set<Pair> idPair=new HashSet<Pair>();
	
	public Iterator<Pair> getIterator(){
		return idPair.iterator();
	}
	
	public LinkTable(RoleDef role1, RoleDef role2){
		this.role1=role1;
		this.role2=role2;
	}
	
	public void addReference(String id1, RoleDef role, String id2){
		if(role==role1){
			Pair pair=new Pair(id1, id2);
			idPair.add(pair);
		}else{
			Pair pair=new Pair(id2, id1);
			idPair.add(pair);
		}
	}

	public RoleDef getRole1() {
		return role1;
	}
	
	public RoleDef getRole2() {
		return role2;
	}
}