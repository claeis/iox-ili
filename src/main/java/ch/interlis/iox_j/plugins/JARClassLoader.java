package ch.interlis.iox_j.plugins;

import java.io.*;
import java.lang.reflect.Modifier;
import java.net.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;

import ch.interlis.iox_j.validator.InterlisFunction;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.JarURLConnection;

/**
 * @author ce
 */
class JARClassLoader extends URLClassLoader
{
	private String path;
	private JAR jar;
	private ArrayList<String> pluginClasses = new ArrayList<String>();


	public JARClassLoader(String path,PluginLoader pluginLoader)
		throws IOException
	{

		super(new URL[] { new URL("file","",path) });

		URL u = new URL("jar", "", new URL("file","",path) + "!/");
		JarURLConnection uc = (JarURLConnection)u.openConnection();

		JarFile zipFile = uc.getJarFile();

		jar = new JAR(path,this);

		Enumeration entires = zipFile.entries();
		while(entires.hasMoreElements())
		{
			ZipEntry entry = (ZipEntry)entires.nextElement();
			String name = entry.getName();
			//System.err.println(name);
			if(name.endsWith(PluginLoader.IOX_PLUGIN+".class")){
				pluginClasses.add(name);
			}
		}

		pluginLoader.addPluginJAR(jar);

	}
	void loadAllPlugins()
	{

		boolean ok = true;

		for(int i = 0; i < pluginClasses.size(); i++)
		{
			String name = (String)pluginClasses.get(i);
			name = fileToClass(name);
			try
			{
				Class clazz = loadClass(name);
				IoxPlugin plugin=(IoxPlugin)clazz.newInstance();
				jar.addPlugin(plugin);
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

	/**
	 * A JAR file.
	 */
	public static class JAR
	{
		public JARClassLoader getClassLoader()
		{
			return classLoader;
		}
	
	
		private void addPlugin(IoxPlugin plugin)
		{
			plugins.addElement(plugin);
		}
	
		public IoxPlugin[] getPlugins()
		{
			IoxPlugin[] array = new IoxPlugin[plugins.size()];
			plugins.copyInto(array);
			return array;
		}
	
		public JAR(String path, JARClassLoader classLoader)
		{
			this.path = path;
			this.classLoader = classLoader;
			plugins = new Vector<IoxPlugin>();
		}
		
		// private members
		private String path;
		private JARClassLoader classLoader;
		private Vector<IoxPlugin> plugins;
	}

}
