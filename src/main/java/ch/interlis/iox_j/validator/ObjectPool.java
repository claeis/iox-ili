package ch.interlis.iox_j.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ch.ehi.basics.types.OutParam;
import ch.ehi.iox.objpool.ObjectPoolManager;
import ch.ehi.iox.objpool.impl.IomObjectSerializer;
import ch.ehi.iox.objpool.impl.JavaSerializer;
import ch.interlis.ili2c.metamodel.AbstractClassDef;
import ch.interlis.ili2c.metamodel.AssociationDef;
import ch.interlis.ili2c.metamodel.Domain;
import ch.interlis.ili2c.metamodel.PredefinedModel;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.ili2c.metamodel.ViewableTransferElement;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxLogging;
import ch.interlis.iox_j.logging.LogEventFactory;

// the ObjectPool manages all objects
//   - check If Object Is Unique
//   - add object into map
//   - check object version on BasketId and iliVersion 1.0 / 2.3
//   - return objects of given BasketId
//   - return keys of collection
//   - return tid of association

public class ObjectPool {
	private boolean doItfOidPerTable;
	private HashMap<String,Object> tag2class;
	private ObjectPoolManager objPoolManager=null;
	private Map<String, Map<ObjectPoolKey, IomObject>> collectionOfBaskets = new java.util.HashMap<String, Map<ObjectPoolKey, IomObject>>();
	private java.util.TreeSet<String> bids=new java.util.TreeSet<String>();
	
	public ObjectPool(boolean doItfOidPerTable, IoxLogging errs, LogEventFactory errFact, HashMap<String,Object> tag2class,ObjectPoolManager objPoolManager){
		this.doItfOidPerTable = doItfOidPerTable;
		this.tag2class = tag2class;
		this.objPoolManager=objPoolManager;
	}
	public static String getAssociationId(IomObject iomObj, AssociationDef assocDef) {
		if(assocDef==null){
			throw new IllegalArgumentException("assocDef==null");
		}
		String tag=assocDef.getScopedName(null);
		String tid=null;
		Iterator<ViewableTransferElement> rolei=assocDef.getAttributesAndRoles2();
		tid=tag;
		boolean missingRef=false;
		while(rolei.hasNext()){
			ViewableTransferElement prop=rolei.next();
			if(prop.obj instanceof RoleDef && !prop.embedded){
				String roleName=((RoleDef) prop.obj).getName();
				IomObject refObj=iomObj.getattrobj(roleName, 0);
				String ref=null;
				if(refObj!=null){
					ref=refObj.getobjectrefoid();
				}
				if(ref!=null){
					tid=tid+":"+ref;
				}else{
			 		//throw new IllegalStateException("REF required ("+tag+"/"+roleName+")");
			 		missingRef=true;
				}
			}
		}
		if(missingRef) {
		    return null;
		}
		return tid;
	}

	public IomObject addObject(IomObject iomObj, String currentBasketId){
		String oid = iomObj.getobjectoid();
		Object modelEle = tag2class.get(iomObj.getobjecttag());
        ObjectPoolKey key = null;
		if(oid==null){
			oid=getAssociationId(iomObj, (AssociationDef)modelEle);
			if(oid==null) {
			    throw new IllegalStateException("Association with missing REF "+iomObj.getobjecttag());
			}
		}else {
		    if(modelEle instanceof AbstractClassDef) {
	            Domain oidType=((AbstractClassDef) modelEle).getOid();
	            if(oidType==PredefinedModel.getInstance().UUIDOID) {
	                oid=Validator.normalizeUUID(oid);
	            }
		    }
		}
		if(doItfOidPerTable){
			key=new ObjectPoolKey(oid, (Viewable<?>)modelEle, currentBasketId);
		} else {
			key=new ObjectPoolKey(oid, null, currentBasketId);
		}
		Map<ObjectPoolKey, IomObject> collectionOfObjects =null;
		if(collectionOfBaskets.containsKey(currentBasketId)){
			collectionOfObjects=collectionOfBaskets.get(currentBasketId);
		} else {
			collectionOfObjects=objPoolManager.newObjectPoolImpl2(this.getClass().getSimpleName(),new IomObjectSerializer()); // new HashMap<ObjectPoolKey, IomObject>();
			collectionOfBaskets.put(currentBasketId, collectionOfObjects);
			bids.add(currentBasketId);
		}
		if(collectionOfObjects.containsKey(key)){
			return collectionOfObjects.get(key);
		}
		{
	        OutParam<String> bidOfTargetObj = new OutParam<String>();
	        ArrayList<Viewable> destinationClasses=new ArrayList<Viewable>();
	        destinationClasses.add((Viewable<?>)modelEle);
	        IomObject existingObj = getObject(oid, destinationClasses, bidOfTargetObj);
		    if(existingObj!=null) {
		        return existingObj;
		    }
		}
			collectionOfObjects.put(key,iomObj);
		return null;
	}
	
	public ch.ehi.iox.objpool.impl.ObjPoolImpl2 getObjectsOfBasketId(String basketId){
		return (ch.ehi.iox.objpool.impl.ObjPoolImpl2)collectionOfBaskets.get(basketId);
	}
	
	public Set<String> getBasketIds(){
		return bids;
	}
	
	public String getBidOfObject(String oid, Viewable classObj){
	    if(classObj instanceof AbstractClassDef) {
	        Domain oidType=((AbstractClassDef) classObj).getOid();
	        if(oidType==PredefinedModel.getInstance().UUIDOID) {
	            oid=Validator.normalizeUUID(oid);
	        }
	    }
		for(String basketId : bids){
			Map<ObjectPoolKey, IomObject> collectionOfObjects = collectionOfBaskets.get(basketId);
			if(doItfOidPerTable){
				IomObject object = collectionOfObjects.get(new ObjectPoolKey(oid, classObj, basketId));
				if(object != null){
					return basketId;
				}
			} else {
				IomObject object = collectionOfObjects.get(new ObjectPoolKey(oid, null, basketId));
				if(object != null){
					return basketId;
				}
			}
		}
		return null;
	}
	
	public IomObject getObject(String oid, ArrayList<Viewable> ili1Tables, OutParam<String> retBasketId) {
		for(String basketId : bids){
			Map<ObjectPoolKey, IomObject> collectionOfObjects = collectionOfBaskets.get(basketId);
			if(doItfOidPerTable){
				for(Viewable aClass : ili1Tables){
					IomObject object = collectionOfObjects.get(new ObjectPoolKey(oid, aClass, basketId));
					if(object != null){
						if(retBasketId!=null){
							retBasketId.value=basketId;
						}
						return object;
					}
				}
			} else {
				IomObject object = collectionOfObjects.get(new ObjectPoolKey(oid, null, basketId));
				if(object==null) {
	                object = collectionOfObjects.get(new ObjectPoolKey(Validator.normalizeUUID(oid), null, basketId));
				    if(object!=null) {
				        Object modelEle = tag2class.get(object.getobjecttag());
                        Domain oidType=((AbstractClassDef) modelEle).getOid();
                        if(oidType!=PredefinedModel.getInstance().UUIDOID) {
                            object=null; // oid is not an uuid; wrong to normalize it
                        }
				    }
				}
				if(object != null){
					if(retBasketId!=null){
						retBasketId.value=basketId;
					}
					return object;
				}
			}
		}
		if(retBasketId!=null) {
	        retBasketId.value=null;
		}
		return null;
	}
    public void startNewTransfer() {
        // TODO clear/remove transient TIDs
    }
}