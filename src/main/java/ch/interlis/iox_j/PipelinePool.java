package ch.interlis.iox_j;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.interlis.iox.IoxDataPool;

public class PipelinePool implements IoxDataPool {
	private HashMap<Object,Map<String,Object>> elements=new HashMap<Object,Map<String,Object>>();
	@Override
	public Collection<Object> getElements() {
		return elements.keySet();
	}

	@Override
	public Collection<String> getIntermediateValueNames(Object ele) {
		if(!elements.containsKey(ele)){
			return null;
		}
		return elements.get(ele).keySet();
	}
	
	@Override
	public Object getIntermediateValue(Object ele, String valueName) {
		if(!elements.containsKey(ele)){
			return null;
		}
		Map<String,Object> values=elements.get(ele);
		return values.get(valueName);
	}


	@Override
	public void setIntermediateValue(Object ele, String valueName, Object value) {
		Map<String,Object> values=null;
		if(!elements.containsKey(ele)){
			values=new HashMap<String,Object>();
			elements.put(ele, values);
		}else{
			values=elements.get(ele);
		}
		values.put(valueName,value);
	}
}
