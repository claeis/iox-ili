package ch.interlis.iox_j.validator;

import java.util.HashMap;
import java.util.Map;
import ch.interlis.ili2c.metamodel.AssociationDef;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.iom.IomObject;

public class LinkPool {
	Map<LinkPoolKey, Integer> collectionOfReferenceObj = new HashMap<LinkPoolKey, Integer>();
	
	// returns the number of referenced objects consisting of OID and role-name.
	public int getTargetObjectCount(IomObject iomObj, RoleDef role, boolean doItfOidPerTable) {
		Integer ret = null;
		if(doItfOidPerTable){
			ret=collectionOfReferenceObj.get(new LinkPoolKey(iomObj.getobjectoid(), iomObj.getobjecttag(), role.getName()));
		} else {
			ret=collectionOfReferenceObj.get(new LinkPoolKey(iomObj.getobjectoid(), null, role.getName()));
		}
		if(ret==null){
			return 0;
		}
		return ret;
	}

	public void addLink(IomObject iomObj, RoleDef role, String targetOid, boolean doItfOidPerTable){
		// if embedded
		if(((AssociationDef) role.getContainer()).isLightweight()){
			// addLink() is called once per association; once for the embedded end
			increaseCounter(iomObj.getobjectoid(), iomObj.getobjecttag(), role, doItfOidPerTable);
			RoleDef oppRole=role.getOppEnd();
			increaseCounter(targetOid, iomObj.getobjecttag(),oppRole, doItfOidPerTable);
		}else{
			// stand-alone
			// addLink() is called twice per association; once for each end
			if(doItfOidPerTable!=false){
				// standalone association is only possible with ili2
				throw new IllegalArgumentException("doItfOidPerTable!=false");
			}
			if(role.hasOneOppEnd()) {
	            RoleDef oppRole=role.getOppEnd();
	            String sourceOid=iomObj.getattrobj(oppRole.getName(),0).getobjectrefoid();
	            increaseCounter(sourceOid, null, role, false);
			}
		}
	}

	private void increaseCounter(String oid, String className, RoleDef role, boolean doItfOidPerTable){
		LinkPoolKey key = null;
		if(doItfOidPerTable){
			key=new LinkPoolKey(oid, className, role.getName());
		} else {
			key=new LinkPoolKey(oid, null, role.getName());
		}
		if(collectionOfReferenceObj.containsKey(key)){
			int counter=collectionOfReferenceObj.get(key);
			counter+=1;
			collectionOfReferenceObj.put(key,counter);
		}else{
			collectionOfReferenceObj.put(key,1);
		}
	}
}