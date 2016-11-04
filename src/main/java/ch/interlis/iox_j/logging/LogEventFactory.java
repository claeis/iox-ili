package ch.interlis.iox_j.logging;

import java.util.Date;

import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxLogEvent;

public class LogEventFactory {
	private String dataSource=null;
	private IomObject dataObj=null;
	private Double coordX=null;
	private Double coordY=null;
	private Double coordZ=null;
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public void setDataObj(IomObject dataObj) {
		this.dataObj = dataObj;
		this.coordX=null;
		this.coordY=null;
		this.coordZ=null;
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
	public IoxLogEvent logErrorMsg(String msg,String... args){
		String eventId="codeInternal";
		if(dataObj!=null){
			return new LogEventImpl(dataSource,new Date(),eventId,IoxLogEvent.ERROR,formatMessage(msg,dataObj,args),null,dataObj.getobjectline(),dataObj.getobjecttag(),getObjectTechId(dataObj),getObjectUsrId(dataObj),dataObj.getobjectoid(),null,coordX,coordY,coordZ,getCallerOrigin());
		}
		return new LogEventImpl(dataSource,new Date(),eventId,IoxLogEvent.ERROR,formatMessage(msg,null),null,null,null,null,null,null,null,coordX,coordY,coordZ,getCallerOrigin());
	}
	public IoxLogEvent logWarningMsg(String msg,String... args){
		String eventId="codeInternal";
		if(dataObj!=null){
			return new LogEventImpl(dataSource,new Date(),eventId,IoxLogEvent.WARNING,formatMessage(msg,dataObj,args),null,dataObj.getobjectline(),dataObj.getobjecttag(),getObjectTechId(dataObj),getObjectUsrId(dataObj),dataObj.getobjectoid(),null,coordX,coordY,coordZ,getCallerOrigin());
		}
		return new LogEventImpl(dataSource,new Date(),eventId,IoxLogEvent.WARNING,formatMessage(msg,null),null,null,null,null,null,null,null,coordX,coordY,coordZ,getCallerOrigin());
	}
	public IoxLogEvent logErrorById(String eventId,String... args){
		if(dataObj!=null){
			return new LogEventImpl(dataSource,new Date(),eventId,IoxLogEvent.ERROR,formatMessageId(eventId,dataObj,args),null,dataObj.getobjectline(),dataObj.getobjecttag(),getObjectTechId(dataObj),getObjectUsrId(dataObj),dataObj.getobjectoid(),null,coordX,coordY,coordZ,getCallerOrigin());
		}
		return new LogEventImpl(dataSource,new Date(),eventId,IoxLogEvent.ERROR,formatMessageId(eventId,null,args),null,null,null,null,null,null,null,coordX,coordY,coordZ,getCallerOrigin());
	}
	private String getObjectUsrId(IomObject iomObj) {
		// TODO Auto-generated method stub
		return null;
	}
	private String getObjectTechId(IomObject iomObj) {
		// TODO Auto-generated method stub
		return null;
	}
	private String formatMessage(String rawMsg, IomObject iomObj,String...args) {
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
						msg.append(args[argi]);
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
}
