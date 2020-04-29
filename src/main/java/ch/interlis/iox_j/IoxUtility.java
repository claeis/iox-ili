package ch.interlis.iox_j;

import java.util.ArrayList;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.xtf.Xtf24Reader;
import ch.interlis.iom_j.xtf.XtfStartTransferEvent;
import ch.interlis.iom_j.xtf.impl.MyHandler;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxReader;
import ch.interlis.iox.StartBasketEvent;
import ch.interlis.iox_j.utility.ReaderFactory;

@Deprecated
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
	/** Gets the models used in an XTF file.
	 * @param xtffile path of XTF file
	 * @return list of model names (list&lt;String modelname&gt;) 
	 * @throws ch.interlis.iox.IoxException
	 */
	public static java.util.List<String> getModels(java.io.File xtffile)
		throws ch.interlis.iox.IoxException
	{
		ArrayList<String> ret=new ArrayList<String>();
		IoxReader reader=null;
		try{
			reader=new ReaderFactory().createReader(xtffile, null);
			IoxEvent event=null;
			try{
				while((event=reader.read())!=null){
					if(event instanceof StartBasketEvent){
						String topic=((StartBasketEvent)event).getType();
						String model[]=topic.split("\\.");
						addModel(ret,model[0]);
						return ret;
					}else if(event instanceof XtfStartTransferEvent){
						XtfStartTransferEvent xtfStart=(XtfStartTransferEvent)event;
						addModels(ret, xtfStart);
						if(reader instanceof Xtf24Reader) {
						    return ret;
						}
					}
				}
			}catch(ch.interlis.iox.IoxException ex){
				EhiLogger.logAdaption("failed to get modelnames from transferfile ("+ex.toString()+")");
			}
		}finally{
			if(reader!=null){
				reader.close();
			}
			reader=null;
		}
		return ret;
	}
	private static void addModels(ArrayList<String> ret, XtfStartTransferEvent xtfStart) {
		java.util.HashMap<String, IomObject> objs=xtfStart.getHeaderObjects();
		if(objs!=null){
			for(String tid:objs.keySet()){
				IomObject obj=objs.get(tid);
				if(obj.getobjecttag().equals(MyHandler.HEADER_OBJECT_MODELENTRY)){
					addModel(ret,obj.getattrvalue(MyHandler.HEADER_OBJECT_MODELENTRY_NAME));
				}
			}
		}
	}
	private static void addModel(ArrayList<String> ret, String model) {
		if(ret.contains(model)){
			return;
		}
		ret.add(model);
	}
	@Deprecated
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
	@Deprecated
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

	private static String version = null;

	public static String getVersion() {
		if (version == null) {
			java.util.ResourceBundle resVersion = java.util.ResourceBundle.getBundle("ch/interlis/iox_j/Version");
			StringBuffer ret = new StringBuffer(20);
			ret.append(resVersion.getString("version"));
			ret.append('-');
			ret.append(resVersion.getString("versionCommit"));
			version = ret.toString();
		}
		return version;
	}
}
