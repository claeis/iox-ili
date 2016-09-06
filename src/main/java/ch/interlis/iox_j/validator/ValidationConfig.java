package ch.interlis.iox_j.validator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.Constraint;
import ch.interlis.ili2c.metamodel.Container;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.TransferDescription;

import com.moandjiezana.toml.Toml;


public class ValidationConfig implements ch.interlis.iox.IoxValidationConfig {
	private HashMap<String,HashMap<String,String>> data=new HashMap<String,HashMap<String,String>>();
	public static final String MULTIPLICITY="multiplicity";
	public static final String TYPE="type";
	public static final String WARNING="warning";
	public static final String OFF="off"; 
	public static final String ILI_METAATTR_PREFIX="ilivalid.";
	public static final String PARAMETER = "PARAMETER";
	public static final String VALIDATION = "validation";
	public void mergeIliMetaAttrs(TransferDescription td){
		mergeIliMetaAttrsHelper(td);
	}
	private void mergeIliMetaAttrsHelper(Element ele){
		ch.ehi.basics.settings.Settings metaValues=ele.getMetaValues();
		String iliQName=null;
		if(ele instanceof AttributeDef){
			iliQName=ele.getContainer().getScopedName(null)+"."+ele.getName();
		}else if(ele instanceof RoleDef){
				iliQName=ele.getContainer().getScopedName(null)+"."+ele.getName();
		}else if(ele instanceof Constraint){
			String constraintName=ele.getName();
			if(constraintName==null){
				constraintName=getConstraintName((Constraint)ele);
			}
			iliQName=ele.getContainer().getScopedName(null)+"."+constraintName;
		}else{
			iliQName=ele.getScopedName(null);
		}
		if(iliQName!=null){
			for(String name:metaValues.getValues()){
				if(name.startsWith(ILI_METAATTR_PREFIX)){
					String paramName=name.substring(ILI_METAATTR_PREFIX.length());
					String paramValue=metaValues.getValue(name);
					setConfigValue(iliQName,paramName,paramValue);
				}
			}
		}
		if(ele instanceof Container<?>){
			java.util.Iterator subElei=((Container<?>) ele).iterator();
			while(subElei.hasNext()){
				Element subEle=(Element) subElei.next();
				mergeIliMetaAttrsHelper(subEle);
			}
		}
	}
	private String getConstraintName(Constraint ele) {
		String name=ele.getMetaValue("name");
		if(name!=null){
			return name;
		}
		Container<?> container=ele.getContainer();
		int cnstrIdx=0;
		for( Iterator it =  container.iterator(); it.hasNext(); ) {
			Element element = (Element)it.next();
			if(element==ele){
				return "CONSTRAINT"+cnstrIdx;
			}
			if ( element instanceof Constraint ) {
				cnstrIdx++;
			}
		}
		return null;
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
		EhiLogger.traceState("modelele <"+iliQName+">, param <"+configParam+">, value <"+value+">");
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
