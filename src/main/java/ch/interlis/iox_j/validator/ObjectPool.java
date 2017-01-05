package ch.interlis.iox_j.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.ws.Holder;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iom.IomObject;

public class ObjectPool {
	
	private boolean doItfOidPerTable;
	Map<String, Map<ObjectPoolKey, IomObject>> collectionOfBaskets = new HashMap<String, Map<ObjectPoolKey, IomObject>>();
	
	public ObjectPool(boolean doItfOidPerTable){
		this.doItfOidPerTable = doItfOidPerTable;
	}

	public void addObject(IomObject iomObj, HashMap<String,Object> tag2class, String basketId){
		String oid = iomObj.getobjectoid();
		Object modelEle = tag2class.get(iomObj.getobjecttag());
		Viewable classValue = (Viewable) modelEle;
		ObjectPoolKey key = null;
		if(doItfOidPerTable){
			key=new ObjectPoolKey(oid, classValue, basketId);
		} else {
			key=new ObjectPoolKey(oid, null, basketId);
		}
		Map<ObjectPoolKey, IomObject> collectionOfObjects =null;
		if(collectionOfBaskets.containsKey(basketId)){
			collectionOfObjects=collectionOfBaskets.get(basketId);
		} else {
			collectionOfObjects=new HashMap<ObjectPoolKey, IomObject>();
			collectionOfBaskets.put(basketId, collectionOfObjects);
		}
		if(!collectionOfObjects.containsKey(key)){
			collectionOfObjects.put(key,iomObj);
		}
		
	}
	
	public Map getAllObjects(){
		Map<ObjectPoolKey, IomObject> collection = new HashMap<ObjectPoolKey, IomObject>();
		for (Map<ObjectPoolKey, IomObject> hashMap : collectionOfBaskets.values()){
			collection.putAll(hashMap);
		}
		return collection;
	}
	
	public IomObject getObject(String oid, Viewable aClass, Holder<String> retBasketId){
		for(String basketId : collectionOfBaskets.keySet()){
			Map<ObjectPoolKey, IomObject> collectionOfObjects = collectionOfBaskets.get(basketId);
			if(doItfOidPerTable){			
				IomObject object = collectionOfObjects.get(new ObjectPoolKey(oid, aClass, basketId));
				if(object != null){
					retBasketId.value=basketId;
					return object;
				}
			} else {
				IomObject object = collectionOfObjects.get(new ObjectPoolKey(oid, null, basketId));
				if(object != null){
					retBasketId.value=basketId;
					return object;
				}
			}
		}
		retBasketId.value=null;
		return null;
	}

	public IomObject getObject(String oid, ArrayList<Viewable> classes, Holder<String> retBasketId) {
		for(String basketId : collectionOfBaskets.keySet()){
			Map<ObjectPoolKey, IomObject> collectionOfObjects = collectionOfBaskets.get(basketId);
			if(doItfOidPerTable){
				for(Viewable aClass : classes){
					IomObject object = collectionOfObjects.get(new ObjectPoolKey(oid, aClass, basketId));
					if(object != null){
						retBasketId.value=basketId;
						return object;
					}
				}
			} else {
				for(Viewable aClass : classes){		
					IomObject object = collectionOfObjects.get(new ObjectPoolKey(oid, null, basketId));
					if(object != null){
						retBasketId.value=basketId;
						return object;
					}
				}
			}
		}
		retBasketId.value=null;
		return null;
	}
}