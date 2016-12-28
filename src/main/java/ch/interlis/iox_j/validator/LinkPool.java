package ch.interlis.iox_j.validator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import ch.interlis.ili2c.metamodel.AbstractClassDef;
import ch.interlis.ili2c.metamodel.AssociationDef;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iom.IomObject;

public class LinkPool {

	Map<LinkPoolKey, Integer> collectionOfReferenceObj = new HashMap<LinkPoolKey, Integer>();
	
	// returns the number of referenced objects consisting of OID and role-name.
	public int getTargetObjectCount(IomObject iomObj, RoleDef role) {
		
		Integer ret=collectionOfReferenceObj.get(new LinkPoolKey(iomObj.getobjectoid(), role.getName()));
		
		if(ret==null){
			return 0;
		}
		return ret;
	}

	public void addLink(IomObject iomObj, RoleDef role, String targetOid) {
		String oid = iomObj.getobjectoid();
		String roleName = role.getName();
		// if embedded
		if(((AssociationDef) role.getContainer()).isLightweight()){
			// addLink() is called once per association; once for the embedded end
			increaseCounter(iomObj.getobjectoid(),role.getName());
			RoleDef oppRole=role.getOppEnd();
			increaseCounter(targetOid,oppRole.getName());
		}else{
			// stand-alone
			// addLink() is called twice per association; once for each end
			RoleDef oppRole=role.getOppEnd();
			String sourceOid=iomObj.getattrvalue(oppRole.getName());
			increaseCounter(sourceOid,role.getName());
		}
	}

	private void increaseCounter(String oid, String roleName) {
		LinkPoolKey key=new LinkPoolKey(oid, roleName);
		if(collectionOfReferenceObj.containsKey(key)){
			int counter=collectionOfReferenceObj.get(key);
			counter+=1;
			collectionOfReferenceObj.put(key,counter);
		}else{
			collectionOfReferenceObj.put(key,1);
		}
	}
}