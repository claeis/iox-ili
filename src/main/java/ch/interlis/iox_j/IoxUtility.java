package ch.interlis.iox_j;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iox.IoxReader;

public class IoxUtility {
	private IoxUtility(){}
	public static boolean isItfFilename(String filename)
	{
		String xtfExt=ch.ehi.basics.view.GenericFileFilter.getFileExtension(new java.io.File(filename)).toLowerCase();
		if("itf".equals(xtfExt)){
			return true;
		}
		return false;
	}
	public static boolean isXtfFilename(String filename)
	{
		String xtfExt=ch.ehi.basics.view.GenericFileFilter.getFileExtension(new java.io.File(filename)).toLowerCase();
		if("xtf".equals(xtfExt)){
			return true;
		}
		return false;
	}
	public static boolean isIliFilename(String filename)
	{
		String xtfExt=ch.ehi.basics.view.GenericFileFilter.getFileExtension(new java.io.File(filename)).toLowerCase();
		if("ili".equals(xtfExt)){
			return true;
		}
		return false;
	}
	public static String getModelFromXtf(String filename)
	{
		ch.interlis.iox.StartBasketEvent be=null;
		try{
			IoxReader ioxReader=null;
			if(isItfFilename(filename)){
				ioxReader=new ch.interlis.iom_j.itf.ItfReader(new java.io.File(filename));
			}else{
				ioxReader=new ch.interlis.iom_j.xtf.XtfReader(new java.io.File(filename));
			}
			// get first basket
			ch.interlis.iox.IoxEvent event;
			do{
				event=ioxReader.read();
				if(event instanceof ch.interlis.iox.StartBasketEvent){
					be=(ch.interlis.iox.StartBasketEvent)event;
					break;
				}
			}while(!(event instanceof ch.interlis.iox.EndTransferEvent));
			ioxReader.close();
			ioxReader=null;
		}catch(ch.interlis.iox.IoxException ex){
			EhiLogger.logError("failed to read model from xml file "+filename,ex);
			return null;
		}
		// no baskets?
		if(be==null){
			// no model
			return null;
		}
		String qtopic[]=be.getType().split("\\.");
		String model=qtopic[0];
		//EhiLogger.debug("model from xtf <"+model+">");
		return model;
	}
	public static String getModelFromXtf(java.io.InputStream f,String filename)
	{
		ch.interlis.iox.StartBasketEvent be=null;
		try{
			IoxReader ioxReader=null;
			if(isItfFilename(filename)){
				ioxReader=new ch.interlis.iom_j.itf.ItfReader(f);
			}else{
				ioxReader=new ch.interlis.iom_j.xtf.XtfReader(f);
			}
			// get first basket
			ch.interlis.iox.IoxEvent event;
			do{
				event=ioxReader.read();
				if(event instanceof ch.interlis.iox.StartBasketEvent){
					be=(ch.interlis.iox.StartBasketEvent)event;
					break;
				}
			}while(!(event instanceof ch.interlis.iox.EndTransferEvent));
			ioxReader.close();
			ioxReader=null;
		}catch(ch.interlis.iox.IoxException ex){
			EhiLogger.logError("failed to read model from xml file "+filename,ex);
			return null;
		}
		// no baskets?
		if(be==null){
			// no model
			return null;
		}
		String qtopic[]=be.getType().split("\\.");
		String model=qtopic[0];
		//EhiLogger.debug("model from xtf <"+model+">");
		return model;
	}

	private static String version=null;
	public static String getVersion() {
		  if(version==null){
		java.util.ResourceBundle resVersion = java.util.ResourceBundle.getBundle("ch/interlis/iox_j/Version");
			// Major version numbers identify significant functional changes.
			// Minor version numbers identify smaller extensions to the functionality.
			// Micro versions are even finer grained versions.
			StringBuffer ret=new StringBuffer(20);
		ret.append(resVersion.getString("versionMajor"));
			ret.append('.');
		ret.append(resVersion.getString("versionMinor"));
			ret.append('.');
		ret.append(resVersion.getString("versionMicro"));
		ret.append('-');
		if(resVersion.containsKey("versionBranch")){
            String branch=ch.ehi.basics.tools.StringUtility.purge(resVersion.getString("versionBranch"));
            if(branch!=null){
               ret.append(branch);
               ret.append('-');
            }
		}
		ret.append(resVersion.getString("versionDate"));
			version=ret.toString();
		  }
		  return version;
	}
}
