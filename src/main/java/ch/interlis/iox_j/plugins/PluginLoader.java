package ch.interlis.iox_j.plugins;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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
    public final static String IOX_PLUGIN_CLASSES="ch.ehi.iox-ili.pluginClasses";
    public static final String systemPropertyPluginClasses=System.getProperty(IOX_PLUGIN_CLASSES); // static, so that it can be initialized at graalvm native-image build time
	public void loadPlugins()
	{
	    if(systemPropertyPluginClasses!=null) {
	        String pluginClasses[]=systemPropertyPluginClasses.split(",");
	        for(String name:pluginClasses) {
	            if(name!=null) {
	                name=name.trim();
	                if(name.length()>0) {
	                    loadPluginClass(name);
	                }
	            }
	        }
	    }
	}
    public void loadPluginClass(String className) {
        try
        {
            Class clazz = Class.forName(className); // loadClass(className);
            IoxPlugin plugin=(IoxPlugin)clazz.newInstance();
            addPlugin(plugin);
        }
        catch(Throwable t)
        {
            ch.ehi.basics.logging.EhiLogger.logError("Error while starting plugin " + className,t);

        }
    }
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
				JARClassLoader loader=new JARClassLoader(path);
				loader.loadAllPlugins(this);
			}
			catch(java.io.IOException io)
			{
				EhiLogger.logError("Cannot load plugin " + path,io);
			}
		}
	}
    private java.util.ArrayList<IoxPlugin> plugins=new java.util.ArrayList<IoxPlugin>();
    public void addPlugin(IoxPlugin plugin)
    {
        EhiLogger.traceState("IoxPlugin <"+plugin.getClass().getName()+">");
        plugins.add(plugin);
    }

	public java.util.List<IoxPlugin> getAllPlugins(){
		return plugins;
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
}
