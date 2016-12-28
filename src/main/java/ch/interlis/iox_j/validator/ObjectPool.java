package ch.interlis.iox_j.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iom.IomObject;

public class ObjectPool {
	
	private HashMap<HashMap<String, Viewable>, IomObject> ili10Objects = new HashMap<HashMap<String, Viewable>, IomObject>();
	private HashMap<String, IomObject> ili23Objects = new HashMap<String, IomObject>();
	private boolean doItfOidPerTable;
	
	
	public ObjectPool(boolean doItfOidPerTable){
		this.doItfOidPerTable = doItfOidPerTable;
	}

	public void addObject(IomObject iomObj, HashMap<String,Object> tag2class){
		String oid = iomObj.getobjectoid();
		if(doItfOidPerTable){
			HashMap<String, Viewable> keyOfIli10Objects = new HashMap<String, Viewable>();
			Object modelElement=tag2class.get(iomObj.getobjecttag());
			Viewable classValue= (Viewable) modelElement;
			keyOfIli10Objects.put(oid, classValue);
			if(!ili10Objects.containsKey(keyOfIli10Objects)){
				ili10Objects.put(keyOfIli10Objects, iomObj);
			}
		} else {
			if(!ili23Objects.containsKey(oid)){
				ili23Objects.put(oid, iomObj);
			}	
		}
	}
	
	public HashMap getAllObjects(){
		if(doItfOidPerTable){
			return ili10Objects;
		} else {
			return ili23Objects;
		}
	}
	

	public IomObject getObject(String oid, ArrayList<Viewable> classes) {
		
		if(doItfOidPerTable) {
			for(Viewable aClass : classes){
				HashMap<String, Viewable> keyOfIli10Objects = new HashMap<String, Viewable>();
				keyOfIli10Objects.put(oid, aClass);
				if(ili10Objects.containsKey(keyOfIli10Objects)){
					return ili10Objects.get(keyOfIli10Objects);
				}
			}
			return null;
		} else {
			return ili23Objects.get(oid);
		}
		
	}
}