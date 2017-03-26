package ch.interlis.iox_j.db;

import java.sql.ResultSet;
import ch.interlis.iom.IomObject;
import ch.ehi.basics.logging.EhiLogger;

/**
 * @author ce
 * @version $Revision: 1.0 $ $Date: 01.06.2006 $
 */
public class Db2Xtf {
	private Db2Xtf(){
	}
	static public String getString(ResultSet rs,String colName,boolean isOptional,String recInfo,IomObject iomObj,String iliAttrName)
	{
		String val=DbUtility.getStringOrNull(rs,colName,isOptional,recInfo);
		if(val!=null){
			iomObj.setattrvalue(iliAttrName,val);
		}
		return val;
	}
	static public Integer getInt(ResultSet rs,String colName,boolean isOptional,String recInfo,IomObject iomObj,String iliAttrName)
	{
		Integer val=DbUtility.getInt(rs,colName,isOptional,recInfo);
		if(val!=null){
			iomObj.setattrvalue(iliAttrName,val.toString());
		}
		return val;
	}
	static public Double getDouble(ResultSet rs,String colName,boolean isOptional,String recInfo,IomObject iomObj,String iliAttrName)
	{
		Double val=DbUtility.getDouble(rs,colName,isOptional,recInfo);
		if(val!=null){
			iomObj.setattrvalue(iliAttrName,val.toString());
		}
		return val;
	}
	static public java.sql.Date getXtfDate(ResultSet rs,String colName,boolean isOptional,String recInfo,IomObject iomObj,String iliAttrName)
	{
		java.sql.Date val=DbUtility.getDate(rs,colName,isOptional,recInfo);
		if(val!=null){
			java.text.SimpleDateFormat fmt=new java.text.SimpleDateFormat("yyyy-MM-dd");
			iomObj.setattrvalue(iliAttrName,fmt.format(val));
		}
		return val;
	}
	static public String getDate(ResultSet rs,String colName,boolean isOptional,String recInfo,IomObject iomObj,String iliAttrName)
	{
		String val=DbUtility.getStringOrNull(rs,colName,isOptional,recInfo);
		if(val!=null){
			int startPos=val.indexOf('.');
			int startPos2=val.indexOf('.',startPos+1);
			java.text.DecimalFormat digit4 = new java.text.DecimalFormat("0000");
			java.text.DecimalFormat digit2 = new java.text.DecimalFormat("00");
			if(startPos!=-1 && startPos2!=-1){
				String year=val.substring(0, startPos);
				String month=val.substring(startPos+1, startPos2);
				String date=val.substring(startPos2+1);
				try {
					int yearNum=Integer.parseInt(year);
					if(yearNum<10){
						yearNum+=2000;
					}else if(yearNum<100){
						yearNum+=1900;
					}
					val=digit4.format(yearNum)+"-"+digit2.format(Integer.parseInt(month))+"-"+digit2.format(Integer.parseInt(date));
				} catch (NumberFormatException e) {
					val=null;
				}
			}else if(val.length()==8){
				String year=val.substring(0, 4);
				String month=val.substring(4, 6);
				String date=val.substring(6, 8);
				try {
					val=digit4.format(Integer.parseInt(year))+"-"+digit2.format(Integer.parseInt(month))+"-"+digit2.format(Integer.parseInt(date));
				} catch (NumberFormatException e) {
					val=null;
				}
			}
			if(val==null){
				EhiLogger.logError(recInfo+": illegal value <"+val+"> for column "+colName+"; ignored");
			}else{
				iomObj.setattrvalue(iliAttrName,val);
			}
		}
		return val;
	}
	static public String getMultiLineString(ResultSet rs,String colName,boolean isOptional,String recInfo,IomObject iomObj,String iliAttrName)
	{
		String val=DbUtility.getString(rs,colName,isOptional,recInfo);
		if(val!=null){
			val=val.replace('\n',' ');
			val=val.replace('\r',' ');
			val=val.replace('\t',' ');
			val=val.trim();
			if(val.length()==0){
				val=null;
			}
		}
		if(val!=null){
			iomObj.setattrvalue(iliAttrName,val);
		}
		return val;
	}
	static public void getCoordFromWKT(ResultSet rs,String colName,boolean isOptional,String recInfo,IomObject iomObj,String iliAttrName)
	{
		String val=DbUtility.getStringFromBinary(rs,colName,isOptional,recInfo);
		if(val!=null){
			val=val.trim();
			if(val.length()==0){
				val=null;
			}
		}
		if(val!=null){
			String c1=null;
			String c2=null;
			if(val.startsWith("POINT")){
				int startC1=val.indexOf('(',5);
				if(startC1!=-1){
					int endC2=val.indexOf(')',startC1);
					if(endC2!=-1){
						String c1c2=val.substring(startC1+1,endC2).trim();
						int startC2=c1c2.indexOf(' ');
						if(startC2!=-1){
							c1=c1c2.substring(0,startC2);
							c2=c1c2.substring(startC2+1);
							IomObject coord=iomObj.addattrobj(iliAttrName,"COORD");
							coord.setattrvalue("C1",c1);
							coord.setattrvalue("C2",c2);
							return;
						}
					}
				}
				EhiLogger.logError(recInfo+": failed to parse WKT column "+colName);
			}
		}
	}
	static public Boolean getBoolean(ResultSet rs,String colName,boolean isOptional,String recInfo,IomObject iomObj,String iliAttrName)
	{
		String val=DbUtility.getString(rs,colName,isOptional,recInfo);
		if(val!=null){
			val=val.trim();
			if(val.length()==0){
				val=null;
			}
		}
		if(val!=null){
			if(val.equals("1")){
				iomObj.setattrvalue(iliAttrName,"true");
				return new Boolean(true);
			}else{
				iomObj.setattrvalue(iliAttrName,"false");
				return new Boolean(false);
			}
		}
		return null;
	}
	static public String getEnum(ResultSet rs,String colName,boolean isOptional,String recInfo,EnumMapper enumMapper,IomObject iomObj,String iliAttrName)
	{
		String val=DbUtility.getString(rs,colName,isOptional,recInfo);
		if(val!=null){
			val=val.trim();
			if(val.length()==0){
				val=null;
			}
		}
		if(val!=null){
			String iliCode=enumMapper.mapToIliCode(recInfo,val);
			iomObj.setattrvalue(iliAttrName,iliCode);
			return iliCode;
		}
		return null;
	}
	static public String getRef(ResultSet rs,String colName,boolean isOptional,String recInfo,IdMapper idMapper,String idSpace,IomObject iomObj,String iliAttrName,String iliAssocName)
	{
		String val=DbUtility.getString(rs,colName,isOptional,recInfo);
		if(val!=null){
			val=val.trim();
			if(val.length()==0 || val.equals("0")){
				val=null;
			}
		}
		if(val!=null){
			IomObject ref=iomObj.addattrobj(iliAttrName,iliAssocName);
			val=idMapper.mapId(idSpace,val);
			ref.setobjectrefoid(val);
		}
		return val;
	}
}
