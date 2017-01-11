package ch.interlis.iox_j;

import java.util.ArrayList;
import java.util.List;

import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxException;

public class IoxInvalidDataException extends IoxException {
	private IomObject geom=null;
	private String tid=null;
	private String iliqname=null;
	private String rawMessage=null;
	public IoxInvalidDataException() {
	}

	public IoxInvalidDataException(String message) {
		super(message);
		rawMessage=message;
	}

	public IoxInvalidDataException(Throwable cause) {
		super(cause);
	}

	public IoxInvalidDataException(int lineNumber, String message) {
		super(lineNumber, message);
		rawMessage=message;
	}

	public IoxInvalidDataException(int lineNumber, Throwable cause) {
		super(lineNumber, cause);
	}

	public IoxInvalidDataException(String message, Throwable cause) {
		super(message, cause);
		rawMessage=message;
	}

	public IoxInvalidDataException(int lineNumber, String message,
			Throwable cause) {
		super(lineNumber, message, cause);
		rawMessage=message;
	}

	public IoxInvalidDataException(String message, String iliqname1,String tid1,IomObject geom1) {
		super(message);
		rawMessage=message;
		tid=tid1;
		geom=geom1;
		iliqname=iliqname1;
	}
	public IoxInvalidDataException(String message, String iliqname1, IomObject geom1) {
		super(message);
		rawMessage=message;
		geom=geom1;
		iliqname=iliqname1;
	}

	@Override
	public String getMessage() {
		StringBuilder ret=new StringBuilder();
		if(iliqname!=null){
			ret.append(iliqname);
			ret.append(": ");
		}
		ret.append(super.getMessage());
		if(geom!=null){
		}
		return ret.toString();
	}
	public static String formatTids(String tids[]) {
		StringBuilder ret=new StringBuilder();
		if(tids!=null){
			if(tids.length>1){
				ret.append("tids ");
			}else{
				ret.append("tid ");
			}
			String sep="";
			for(String tid:tids){
				ret.append(sep);
				ret.append(tid);
				sep=", ";
			}
		}
		return ret.toString();
	}

	public IomObject getGeom() {
		return geom;
	}

	public String getTid() {
		return tid;
	}

	public String getIliqname() {
		return iliqname;
	}

	public String getRawMessage() {
		if(rawMessage!=null){
			return rawMessage;
		}
		return super.getMessage();
	}


}
