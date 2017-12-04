package ch.interlis.iox_j.validator;
import java.util.ArrayList;
import java.util.Arrays;

import ch.interlis.iom.IomObject;

public class AttributeArray {
	private Object[] values;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		for(Object obj:values){
			if(obj==null){
				result = prime*result + 0;
			}else if(obj instanceof IomObject){
				result = prime*result + hashCodeIomObj((IomObject)obj);
			}else{
				result = prime*result + obj.hashCode();
			}

		}
		return result;
	}

	private int hashCodeIomObj(IomObject thisObj) {
		final int prime = 31;
		int result = 1;
		result = prime*result + thisObj.getobjecttag().hashCode();
		String refoid=thisObj.getobjectrefoid();
		if(refoid!=null){
			result = prime*result + refoid.hashCode();
		}
		// compare attrs
		int attrc=thisObj.getattrcount();
		for(int attri=0;attri<attrc;attri++){
			String attrName=thisObj.getattrname(attri);
			int valuec=thisObj.getattrvaluecount(attrName);
			for(int valuei=0;valuei<valuec;valuei++){
				IomObject valueObj=thisObj.getattrobj(attrName, valuei);
				if(valueObj!=null){
					result = prime*result + hashCodeIomObj(valueObj);
				}else {
					String valueStr=thisObj.getattrprim(attrName, valuei);
					if(valueStr!=null){
						result = prime*result + valueStr.hashCode();
					}
				}
			}
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttributeArray other = (AttributeArray) obj;
		if(values.length==other.values.length){
			for(int i=0;i<values.length;i++){
				Object object=values[i];
				Object otherObject=other.values[i];
				// iomObject
				if(object instanceof IomObject && otherObject instanceof IomObject){
					IomObject thisIomObj = (IomObject) object;
					IomObject otherIomObj = (IomObject) otherObject;
					if(!equalsIomObj(thisIomObj,otherIomObj)){
						return false;
					}
				} else {
					// String
					if(!object.equals(otherObject)){
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	private boolean equalsIomObj(IomObject thisObj, IomObject otherObj) {
		// ignore oid, because only structEles that should never have an oid
		
		// compare class/assoc/coord/...
		if(!thisObj.getobjecttag().equals(otherObj.getobjecttag())){
			return false;
		}
		
		// compare ref
		String refoid=thisObj.getobjectrefoid();
		String otherRefoid=otherObj.getobjectrefoid();
		if(refoid==null && otherRefoid==null){
			// equal
		}else if(refoid!=null && otherRefoid!=null && !refoid.equals(otherRefoid)){
			return false;
		}else{
			return false;
		}
		
		// compare attrs
		int attrc=thisObj.getattrcount();
		if(attrc!=otherObj.getattrcount()){
			return false;
		}
		for(int attri=0;attri<attrc;attri++){
			String attrName=thisObj.getattrname(attri);
			int valuec=thisObj.getattrvaluecount(attrName);
			if(valuec!=otherObj.getattrvaluecount(attrName)){
				return false;
			}
			for(int valuei=0;valuei<valuec;valuei++){
				IomObject valueObj=thisObj.getattrobj(attrName, valuei);
				if(valueObj!=null && !equalsIomObj(valueObj,otherObj.getattrobj(attrName, valuei))){
					return false;
				}
				String valueStr=thisObj.getattrprim(attrName, valuei);
				if(valueStr!=null && !valueStr.equals(otherObj.getattrprim(attrName, valuei))){
					return false;
				}
			}
		}
		return true;
	}

	public AttributeArray(ArrayList<Object> values){
		this.values = values.toArray(new Object[values.size()]);
	}
	
	private Object[] getValues() {
		return values;
	}
	
	public String valuesAsString() {
		StringBuilder ret=new StringBuilder();
		String sep="";
		for(Object v: values){
			ret.append(sep);
			ret.append(v);
			sep=", ";
		}
		return ret.toString();
	}
}