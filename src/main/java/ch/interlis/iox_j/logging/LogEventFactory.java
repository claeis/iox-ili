package ch.interlis.iox_j.logging;

import java.util.Date;
import java.util.Locale;

import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxLogEvent;
import ch.interlis.iox.IoxLogging;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox_j.IoxInvalidDataException;
import ch.interlis.iox_j.validator.ValidationConfig;

public class LogEventFactory {
	private String dataSource=null;
	private IomObject dataObj=null;
	private IoxLogging logger=null;
	private String tid=null;
	private String iliqname=null;
	private Double coordX=null;
	private Double coordY=null;
	private Double coordZ=null;
	IoxValidationConfig validConfig=null;
	public LogEventFactory()
	{
	}
	public LogEventFactory(IoxValidationConfig validConfig)
	{
		this.validConfig=validConfig;
	}
	public void setValidationConfig(IoxValidationConfig validConfig){
		this.validConfig=validConfig;
	}
	public IoxValidationConfig getValidationConfig(){
		return validConfig;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public void setDataObj(IomObject dataObj) {
		this.dataObj = dataObj;
		coordX=null;
		coordY=null;
		coordZ=null;
		iliqname=null;
		tid=null;
	}
	public void setTid(String tid1) {
		dataObj=null;
		this.tid = tid1;
	}
	public void setIliqname(String iliqname1) {
		dataObj=null;
		this.iliqname = iliqname1;
	}
	public void setDefaultCoord(IomObject coord) {
		if(coord!=null){
			String c1=coord.getattrvalue("C1");
			String c2=coord.getattrvalue("C2");
			String c3=coord.getattrvalue("C3");
			if(c1!=null && c2!=null){
				this.coordX=Double.valueOf(c1);
				this.coordY=Double.valueOf(c2);
				if(c3!=null){
					this.coordZ=Double.valueOf(c3);
				}
			}
		}else{
			this.coordX=null;
			this.coordY=null;
			this.coordZ=null;
		}
	}
	public void setDefaultCoord(Double c1, Double c2, Double c3) {
		this.coordX=c1;
		this.coordY=c2;
		this.coordZ=c3;
	}
	public IoxLogEvent logErrorMsg(String msg,String... args){
		String eventId="codeInternal";
		if(dataObj!=null){
			return new LogEventImpl(dataSource,new Date(),eventId,IoxLogEvent.ERROR,formatMessage(msg,dataObj,args),null,dataObj.getobjectline(),dataObj.getobjecttag(),getObjectTechId(dataObj),getObjectUsrId(dataObj),dataObj.getobjectoid(),null,coordX,coordY,coordZ,getCallerOrigin());
		}
		return new LogEventImpl(dataSource,new Date(),eventId,IoxLogEvent.ERROR,formatMessage(msg,null,args),null,null,iliqname,null,null,tid,null,coordX,coordY,coordZ,getCallerOrigin());
	}
	public IoxLogEvent logErrorMsg(Throwable ex,String msg,String... args){
		String eventId="codeInternal";
		if(dataObj!=null){
			return new LogEventImpl(dataSource,new Date(),eventId,IoxLogEvent.ERROR,formatMessage(msg,dataObj,args),ex,dataObj.getobjectline(),dataObj.getobjecttag(),getObjectTechId(dataObj),getObjectUsrId(dataObj),dataObj.getobjectoid(),null,coordX,coordY,coordZ,getCallerOrigin());
		}
		return new LogEventImpl(dataSource,new Date(),eventId,IoxLogEvent.ERROR,formatMessage(msg,null,args),ex,null,iliqname,null,null,tid,null,coordX,coordY,coordZ,getCallerOrigin());
	}
	public IoxLogEvent logWarningMsg(String msg,String... args){
		String eventId="codeInternal";
		if(dataObj!=null){
			return new LogEventImpl(dataSource,new Date(),eventId,IoxLogEvent.WARNING,formatMessage(msg,dataObj,args),null,dataObj.getobjectline(),dataObj.getobjecttag(),getObjectTechId(dataObj),getObjectUsrId(dataObj),dataObj.getobjectoid(),null,coordX,coordY,coordZ,getCallerOrigin());
		}
		return new LogEventImpl(dataSource,new Date(),eventId,IoxLogEvent.WARNING,formatMessage(msg,null,args),null,null,iliqname,null,null,tid,null,coordX,coordY,coordZ,getCallerOrigin());
	}
	public IoxLogEvent logInfoMsg(String msg,String... args){
		String eventId="codeInternal";
		return new LogEventImpl(dataSource,new Date(),eventId,IoxLogEvent.INFO,formatMessage(msg,null,args),null,null,null,null,null,null,null,null,null,null,getCallerOrigin());
	}
	public IoxLogEvent logDetailInfoMsg(String msg,String... args){
		String eventId="codeInternal";
		return new LogEventImpl(dataSource,new Date(),eventId,IoxLogEvent.DETAIL_INFO,formatMessage(msg,null,args),null,null,null,null,null,null,null,null,null,null,getCallerOrigin());
	}
	public IoxLogEvent logErrorById(String eventId,String... args){
		if(dataObj!=null){
			return new LogEventImpl(dataSource,new Date(),eventId,IoxLogEvent.ERROR,formatMessageId(eventId,dataObj,args),null,dataObj.getobjectline(),dataObj.getobjecttag(),getObjectTechId(dataObj),getObjectUsrId(dataObj),dataObj.getobjectoid(),null,coordX,coordY,coordZ,getCallerOrigin());
		}
		return new LogEventImpl(dataSource,new Date(),eventId,IoxLogEvent.ERROR,formatMessageId(eventId,null,args),null,null,iliqname,null,null,tid,null,coordX,coordY,coordZ,getCallerOrigin());
	}
	public IoxLogEvent logError(IoxInvalidDataException ex) {
		return logError(IoxLogEvent.ERROR,ex);
	}
	public IoxLogEvent logWarning(IoxInvalidDataException ex) {
		return logError(IoxLogEvent.WARNING,ex);
	}
	private IoxLogEvent logError(int eventKind,IoxInvalidDataException ex) {
		String eventId="codeInternal";
		String msg=ex.getRawMessage();
		Integer lineNumber=null;
		if(ex.getLineNumber()!=-1){
			lineNumber=ex.getLineNumber();
		}else{
			lineNumber=null;
		}
		String tid=ex.getTid();
		Double coordX=null;
		Double coordY=null;
		Double coordZ=null;
		IomObject geom=ex.getGeom();
		if(geom!=null && geom.getobjecttag().equals("COORD")){
			String c1=geom.getattrvalue("C1");
			String c2=geom.getattrvalue("C2");
			String c3=geom.getattrvalue("C3");
			if(c1!=null && c2!=null){
				coordX=Double.valueOf(c1);
				coordY=Double.valueOf(c2);
				if(c3!=null){
					coordZ=Double.valueOf(c3);
				}
			}
		}
		String iliqName=ex.getIliqname();
		
		return new LogEventImpl(dataSource,new Date(),eventId,eventKind,msg,ex.getCause(),lineNumber,iliqName,null,null,tid,null,coordX,coordY,coordZ,getCallerOrigin());
	}
	private String getObjectUsrId(IomObject iomObj) {
		String keymsg=null;
		String keyMsgWithLang = ValidationConfig.KEYMSG+"_" + Locale.getDefault().getLanguage();
		if(validConfig!=null){
		    keymsg=validConfig.getConfigValue(iomObj.getobjecttag(), keyMsgWithLang);
		    if (keymsg == null) {
		        keymsg=validConfig.getConfigValue(iomObj.getobjecttag(), ValidationConfig.KEYMSG);
		    }
		}
		if(keymsg!=null){
			return formatMessage(keymsg, iomObj);
		}
		return null;
	}
	private String getObjectTechId(IomObject iomObj) {
		// TODO Auto-generated method stub
		return null;
	}
	public static String formatMessage(String rawMsg, IomObject iomObj,String...args) {
		// TODO resolve placeholders
		int startPos=rawMsg.indexOf('{');
		if(startPos==-1){
			return rawMsg;
		}
		StringBuffer msg=new StringBuffer();
		StringBuffer param=null;
		for(int idx=0;idx<rawMsg.length();idx++){
			char c=rawMsg.charAt(idx);
			if(param==null){
				if(c=='{'){
					param=new StringBuffer();
				}else{
					msg.append(c);
				}
			}else{
				if(c=='}'){
					// resolve param
					try{
						int argi=Integer.parseInt(param.toString());
						if(args!=null && argi<args.length){
							msg.append(args[argi]);
						}
					}catch(NumberFormatException e){
						String value=iomObj.getattrvalue(param.toString());
						if(value!=null){
							msg.append(value);
						}
					}
					param=null;
				}else{
					param.append(c);
				}
			}
		}
		return msg.toString();
	}
	private String formatMessageId(String eventId, IomObject dataObj2,String...args) {
		// TODO get message from ressource bundle and resolve placeholders
		return null;
	}
	/** gets the origin of a call to logError() functions.
	 */
	static private StackTraceElement getCallerOrigin(){
		Throwable tr=new Throwable();
		StackTraceElement stack[]=tr.getStackTrace();
		// stack[0]: getOrigin()
		// stack[1]: logError()
		// stack[2]: user code
		if(2<stack.length){
			StackTraceElement st=stack[2]; 
			return st;
		}
		return null;
	}
	public void addEvent(IoxLogEvent ex)
	{
		if(logger==null){
			throw new IllegalStateException("logger must not be null");
		}
		logger.addEvent(ex);
	}
	public IoxLogging getLogger() {
		return logger;
	}
	public void setLogger(IoxLogging logger) {
		this.logger = logger;
	}
}
