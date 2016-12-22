package ch.interlis.iox_j.validator;

import java.util.HashMap;

import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iom.IomObject;

public class ObjectKeyPool {
	private IomObject iomObject;
	private HashMap<String,Object> tag2class;
	
	private ObjectKeyPool(){
	}
	
	public ObjectKeyPool(IomObject iomObject, HashMap<String,Object> tag2class){
		this.iomObject = iomObject;
		this.tag2class = tag2class;
	}
	
	public HashMap<String, Viewable> getKeyMapOfObjects() {
		HashMap<String, Viewable> key = new HashMap<String, Viewable>();
		key.put(getOid(), getClassName());
		return key;
	}
	
	public String getOid(){
		return iomObject.getobjectoid();
	}
	
	private Viewable getClassName(){
		Viewable classValue=(Viewable) tag2class.get(iomObject.getobjecttag());
		return classValue;
	}
}