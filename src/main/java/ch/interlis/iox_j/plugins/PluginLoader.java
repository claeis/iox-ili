package ch.interlis.iox_j.plugins;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iox_j.validator.InterlisFunction;

/**
 * @author ce
 *
 */
public class PluginLoader {
	/** name suffix of plugin classes.
	 */
	public final static String IOX_PLUGIN="IoxPlugin";
	private java.util.ArrayList<JARClassLoader.JAR> jars=new java.util.ArrayList<JARClassLoader.JAR>();
	/**
	 * Loads all plugins in a directory.
	 */
	public void loadPlugins(File dir)
	{
		if(!(dir.exists() && dir.isDirectory()))
			return;
		File[] plugins = dir.listFiles();
		if(plugins == null)
			return;
	
		for(int i = 0; i < plugins.length; i++)
		{
			File plugin = plugins[i];
			if(!plugin.getName().toLowerCase().endsWith(".jar"))
				continue;
	
			String path = plugin.getAbsolutePath();
			//System.err.println(path);
			try
			{
				JARClassLoader loader=new JARClassLoader(path,this);
				loader.loadAllPlugins();
			}
			catch(java.io.IOException io)
			{
				EhiLogger.logError("Cannot load plugin " + path,io);
			}
		}
	}
	public java.util.List<IoxPlugin> getAllPlugins(){
		java.util.ArrayList<IoxPlugin> ret=new java.util.ArrayList<IoxPlugin>();
		for(int i = 0; i < jars.size(); i++)
		{
			ret.addAll(java.util.Arrays.asList(((JARClassLoader.JAR)jars.get(i)).getPlugins()));
		}
		return ret;
	}
	public static Map<String,Class> getInterlisFunctions(List<IoxPlugin> plugins)
	{
		Map<String,Class> funcs=new HashMap<String,Class>();
		for(IoxPlugin plugin:plugins){
			if(plugin instanceof InterlisFunction){
				funcs.put(((InterlisFunction) plugin).getQualifiedIliName(),plugin.getClass());
			}
		}
		return funcs;
	}
	protected void addPluginJAR(JARClassLoader.JAR jar){
		jars.add(jar);
	}

}
