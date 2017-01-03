package ch.interlis.iox_j.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.interlis.ili2c.metamodel.AssociationDef;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iom.IomObject;

public class ObjectPool {
	
	private boolean doItfOidPerTable;
	Map<ObjectPoolKey, IomObject> collectionOfObjects = new HashMap<ObjectPoolKey, IomObject>();
	
	
	public ObjectPool(boolean doItfOidPerTable){
		this.doItfOidPerTable = doItfOidPerTable;
	}

	public void addObject(IomObject iomObj, HashMap<String,Object> tag2class){
		String oid = iomObj.getobjectoid();
		Object modelEle = tag2class.get(iomObj.getobjecttag());
		Viewable classValue = (Viewable) modelEle;
		ObjectPoolKey key = null;
		if(doItfOidPerTable){
			key=new ObjectPoolKey(oid, classValue);
		} else {
			key=new ObjectPoolKey(oid, null);
		}
		if(!collectionOfObjects.containsKey(key)){
			collectionOfObjects.put(key,iomObj);
		}
	}
	
	public Map getAllObjects(){
		return collectionOfObjects;
	}
	
	
	public IomObject getObject(String oid, Viewable classValue){
		IomObject key = null;
		if(doItfOidPerTable){			
			IomObject objectPoolKey = collectionOfObjects.get(new ObjectPoolKey(oid, classValue));
			if(objectPoolKey != null){
				key = objectPoolKey;
			}
		} else {
			IomObject objectPoolKey = collectionOfObjects.get(new ObjectPoolKey(oid, null));
			if(objectPoolKey != null){
				key = objectPoolKey;
			}
		}
		return key;
	}
	
	
	public IomObject getObject(String oid, ArrayList<Viewable> classes) {
		IomObject key = null;
		if(doItfOidPerTable){
			for(Viewable aClass : classes){				
				IomObject objectPoolKey = collectionOfObjects.get(new ObjectPoolKey(oid, aClass));
				if(objectPoolKey != null){
					key = objectPoolKey;
				}
			}
		} else {
			for(Viewable aClass : classes){				
				IomObject objectPoolKey = collectionOfObjects.get(new ObjectPoolKey(oid, null));
				if(objectPoolKey != null){
					key = objectPoolKey;
				}
			}
		}
		return key;
	}
}