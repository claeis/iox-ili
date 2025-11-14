package ch.interlis.iox_j.plugins;

import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.tools.StringUtility;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.JarURLConnection;
import java.net.MalformedURLException;

/**
 * @author ce
 */
class JARClassLoader extends URLClassLoader
{
	public static final String IOX_CLASS_PATH = "iox-class-path";
    private ArrayList<String> pluginClassnames = new ArrayList<String>();
    private String pluginClassPath=null;
    private String pluginPath=null;

	public JARClassLoader(String path)
		throws IOException
	{

		super(new URL[] { new URL("file","",path) });
		pluginPath=path;
		URL u = new URL("jar", "", new URL("file","",path) + "!/");
		JarURLConnection uc = (JarURLConnection)u.openConnection();

		JarFile jarFile = uc.getJarFile();

		Enumeration entires = jarFile.entries();
		while(entires.hasMoreElements())
		{
			ZipEntry entry = (ZipEntry)entires.nextElement();
			String name = entry.getName();
			//System.err.println(name);
			if(name.endsWith(PluginLoader.IOX_PLUGIN+".class")){
	            name = fileToClass(name);
				pluginClassnames.add(name);
			}
		}
		Manifest manifest=jarFile.getManifest();
		Attributes attrs=manifest.getMainAttributes();
		pluginClassPath=StringUtility.purge(attrs.getValue(IOX_CLASS_PATH));
	}
    public boolean isIoxPluginJar()
    {
        return !pluginClassnames.isEmpty();
    }
	void loadAllPlugins(PluginLoader pluginLoader)
	{
        EhiLogger.traceState("load plugins from <" + pluginPath+">");
		for(int i = 0; i < pluginClassnames.size(); i++)
		{
			String name = (String)pluginClassnames.get(i);
			try
			{
				Class clazz = loadClass(name);
				IoxPlugin plugin=(IoxPlugin)clazz.newInstance();
				pluginLoader.addPlugin(plugin);
			}
			catch(Throwable t)
			{
				ch.ehi.basics.logging.EhiLogger.logError("Error while starting plugin " + name,t);

			}
		}
	}
	/**
	 * Converts a file name to a class name. All slash characters are
	 * replaced with periods and the trailing '.class' is removed.
	 */
	public static String fileToClass(String name)
	{
		char[] clsName = name.toCharArray();
		for(int i = clsName.length - 6; i >= 0; i--)
			if(clsName[i] == '/')
				clsName[i] = '.';
		return new String(clsName,0,clsName.length - 6);
	}
    public void loadDependencies() throws IOException
    {
        if(pluginClassPath!=null) {
            String basePath=new java.io.File(pluginPath).getParent();
            String dependents[]=pluginClassPath.split(" ");
            for(String dependent:dependents) {
                dependent=basePath+"/"+dependent;
                EhiLogger.traceState("load plugin lib <" + dependent+">");
                addURL(new URL("file","",dependent));
            }
        }
        
    }
}
