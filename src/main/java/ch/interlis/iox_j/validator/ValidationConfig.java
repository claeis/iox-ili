package ch.interlis.iox_j.validator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.TransferDescription;

import com.moandjiezana.toml.Toml;


public class ValidationConfig implements ch.interlis.iox.IoxValidationConfig {
	private HashMap<String,HashMap<String,String>> data=new HashMap<String,HashMap<String,String>>();
	public static final String MULTIPLICITY="multiplicity";
	public static final String WARNING="warning";
	public static final String OFF="off"; 
	public void mergeIliMetaAttrs(TransferDescription td){
		
	}
	public void mergeConfigFile(java.io.File file) throws FileNotFoundException
	{
		InputStream in=null;
		try {
			in = new FileInputStream(file);
			Toml toml = new Toml().read(in);
			for (java.util.Map.Entry<String, Object> entry : toml.entrySet()) {
				  Object entryO=entry.getValue();
				  if(entryO instanceof Toml){
					  String iliQName=stripQuotes(entry.getKey());
					  Toml config=(Toml)entryO;
						for (java.util.Map.Entry<String, Object> configEntry : config.entrySet()) {
							String paramName=configEntry.getKey();
							if(configEntry.getValue() instanceof String){
								String paramValue=(String) configEntry.getValue();
								setConfigValue(iliQName,paramName,paramValue);
							}
						}
				  }
				}
			
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
				}
				in=null;
			}
		}
		
	}
	static public ValidationConfig readFromConfigFile(java.io.File file) throws FileNotFoundException
	{
		ValidationConfig ret=new ValidationConfig();
		ret.mergeConfigFile(file);
		return ret;
	}
	private static String stripQuotes(String key) {
		if(key.startsWith("\"") && key.endsWith("\"")){
			return key.substring(1, key.length()-1);
		}
		return key;
	}
	@Override
	public Set<String> getConfigParams(String iliQName) {
		HashMap<String,String> modelele=null;
		if(data.containsKey(iliQName)){
			modelele=data.get(iliQName);
		}
		if(modelele==null){
			return null;
		}
		return new HashSet<String>(modelele.keySet());
	}

	@Override
	public String getConfigValue(String iliQName, String configParam) {
		HashMap<String,String> modelele=null;
		if(data.containsKey(iliQName)){
			modelele=data.get(iliQName);
		}
		if(modelele==null){
			return null;
		}
		return modelele.get(configParam);
	}

	@Override
	public Set<String> getIliQnames() {
		return new HashSet<String>(data.keySet());
	}

	@Override
	public void setConfigValue(String iliQName, String configParam,String value) {
		HashMap<String,String> modelele=null;
		if(data.containsKey(iliQName)){
			modelele=data.get(iliQName);
		}else{
			modelele=new HashMap<String,String>();
			data.put(iliQName, modelele);
		}
		modelele.put(configParam, value);
	}

}
