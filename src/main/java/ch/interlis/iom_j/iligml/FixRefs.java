package ch.interlis.iom_j.iligml;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iom.IomObject;

public class FixRefs {

	private String rootTid=null;
	private Map<String,Set<String>> refs=new HashMap<String,Set<String>>();
	public FixRefs(String rootObjTid) {
		if(rootObjTid==null){
			throw new IllegalArgumentException("Source TID of FixRef must not be null");
		}
		rootTid=rootObjTid;
	}

	public String getSourceTid(){
		return rootTid;
	}
	public void addRef(String roleName,String targetTid) {
		Set<String> targetTids=null;
		if(refs.containsKey(roleName)){
			targetTids=refs.get(roleName);
		}else{
			targetTids=new HashSet<String>();
			refs.put(roleName, targetTids);
		}
		targetTids.add(targetTid);
	}

	public java.util.Collection<String> getTargetRoles() {
		return refs.keySet();
	}

	public java.util.Collection<String> getTargetTids(String roleName) {
		return refs.get(roleName);
	}

}
