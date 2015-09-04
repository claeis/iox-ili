package ch.interlis.iox_j.db;

import java.sql.ResultSet;
import ch.ehi.basics.logging.EhiLogger;

/**
 * @author ce
 * @version $Revision: 1.0 $ $Date: 01.06.2006 $
 */
public class DbUtility {
	private DbUtility(){
	}
	public static java.sql.Date getDate(ResultSet rs,String colName,boolean isOptional,String recInfo)
	{
		java.sql.Date ret=null;
		try{
			ret=rs.getDate(colName);
			if(rs.wasNull() && !isOptional){
				EhiLogger.logError(recInfo+": missing value for column "+colName);
			}
		}
		catch(java.sql.SQLException ex){
			EhiLogger.logError(recInfo+": failed to query column "+colName,ex);
		}
		return ret;
	}
	public static String getString(ResultSet rs,String colName,boolean isOptional,String recInfo)
	{
		String ret=null;
		try{
			ret=rs.getString(colName);
			if(rs.wasNull() && !isOptional){
				EhiLogger.logError(recInfo+": missing value for column "+colName);
			}
		}
		catch(java.sql.SQLException ex){
			EhiLogger.logError(recInfo+": failed to query column "+colName,ex);
		}
		return ret;
	}
	public static Integer getInt(ResultSet rs,String colName,boolean isOptional,String recInfo)
	{
		Integer ret=null;
		try{
			ret=rs.getInt(colName);
			if(rs.wasNull() && !isOptional){
				EhiLogger.logError(recInfo+": missing value for column "+colName);
				ret=null;
			}
		}
		catch(java.sql.SQLException ex){
			EhiLogger.logError(recInfo+": failed to query column "+colName,ex);
		}
		return ret;
	}
	public static Double getDouble(ResultSet rs,String colName,boolean isOptional,String recInfo)
	{
		Double ret=null;
		try{
			ret=rs.getDouble(colName);
			if(rs.wasNull() && !isOptional){
				EhiLogger.logError(recInfo+": missing value for column "+colName);
				ret=null;
			}
		}
		catch(java.sql.SQLException ex){
			EhiLogger.logError(recInfo+": failed to query column "+colName,ex);
		}
		return ret;
	}
	public static String getStringOrNull(ResultSet rs,String colName,boolean isOptional,String recInfo)
	{
		String val=DbUtility.getString(rs,colName,isOptional,recInfo);
		if(val!=null){
			val=val.trim();
			if(val.length()==0){
				val=null;
			}
		}
		return val;
	}
	private static java.nio.charset.Charset utf16=null;
	public static String getStringFromBinary(ResultSet rs,String colName,boolean isOptional,String recInfo)
	{
		String ret=null;
		try{
			byte retAsBytes[]=(byte[])rs.getObject(colName);
			if(utf16==null){
				utf16=java.nio.charset.Charset.forName("UTF-16LE");
			}
			ret=utf16.decode(java.nio.ByteBuffer.wrap(retAsBytes)).toString();
			if(rs.wasNull() && !isOptional){
				EhiLogger.logError(recInfo+": missing value for column "+colName);
			}
		}
		catch(java.sql.SQLException ex){
			EhiLogger.logError(recInfo+": failed to query column "+colName,ex);
		}
		return ret;
	}
 	static public String getRef(ResultSet rs,String colName,boolean isOptional,String recInfo)
	{
		String val=DbUtility.getString(rs,colName,isOptional,recInfo);
		if(val!=null){
			val=val.trim();
			if(val.length()==0 || val.equals("0")){
				val=null;
			}
		}
		return val;
	}
}
