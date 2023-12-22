package org.interlis2.validator.models.IliVErrors.ErrorLog;
public class Error extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IliVErrors.ErrorLog.Error";
  public Error(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Message="Message";
  public String getMessage() {
    String value=getattrvalue("Message");
    return value;
  }
  public void setMessage(String value) {
    setattrvalue("Message", value);
  }
  public final static String tag_Type="Type";
  public Error_Type getType() {
    String value=getattrvalue("Type");
    return Error_Type.parseXmlCode(value);
  }
  public void setType(Error_Type value) {
    setattrvalue("Type", Error_Type.toXmlCode(value));
  }
  public final static String tag_ObjTag="ObjTag";
  public String getObjTag() {
    if(getattrvaluecount("ObjTag")==0)return null;
    String value=getattrvalue("ObjTag");
    return value;
  }
  public void setObjTag(String value) {
    if(value==null){setattrundefined("ObjTag");return;}
    setattrvalue("ObjTag", value);
  }
  public final static String tag_Tid="Tid";
  public String getTid() {
    if(getattrvaluecount("Tid")==0)return null;
    String value=getattrvalue("Tid");
    return value;
  }
  public void setTid(String value) {
    if(value==null){setattrundefined("Tid");return;}
    setattrvalue("Tid", value);
  }
  public final static String tag_TechId="TechId";
  public String getTechId() {
    if(getattrvaluecount("TechId")==0)return null;
    String value=getattrvalue("TechId");
    return value;
  }
  public void setTechId(String value) {
    if(value==null){setattrundefined("TechId");return;}
    setattrvalue("TechId", value);
  }
  public final static String tag_UserId="UserId";
  public String getUserId() {
    if(getattrvaluecount("UserId")==0)return null;
    String value=getattrvalue("UserId");
    return value;
  }
  public void setUserId(String value) {
    if(value==null){setattrundefined("UserId");return;}
    setattrvalue("UserId", value);
  }
  public final static String tag_IliQName="IliQName";
  public String getIliQName() {
    if(getattrvaluecount("IliQName")==0)return null;
    String value=getattrvalue("IliQName");
    return value;
  }
  public void setIliQName(String value) {
    if(value==null){setattrundefined("IliQName");return;}
    setattrvalue("IliQName", value);
  }
  public final static String tag_DataSource="DataSource";
  public String getDataSource() {
    if(getattrvaluecount("DataSource")==0)return null;
    String value=getattrvalue("DataSource");
    return value;
  }
  public void setDataSource(String value) {
    if(value==null){setattrundefined("DataSource");return;}
    setattrvalue("DataSource", value);
  }
  public final static String tag_Line="Line";
  public Integer getLine() {
    if(getattrvaluecount("Line")==0)return null;
    String value=getattrvalue("Line");
    return Integer.parseInt(value);
  }
  public void setLine(Integer value) {
    if(value==null){setattrundefined("Line");return;}
    setattrvalue("Line", Integer.toString(value));
  }
  public final static String tag_Geometry="Geometry";
  public int sizeGeometry() {return getattrvaluecount("Geometry");}
  public ch.interlis.iom.IomObject getGeometry() {
    int size=getattrvaluecount("Geometry");
    if(size==0)return null;
    ch.interlis.iom.IomObject value=(ch.interlis.iom.IomObject)getattrobj("Geometry",0);
    return value;
  }
  public void setGeometry(ch.interlis.iom.IomObject value) {
    if(getattrvaluecount("Geometry")>0){
      changeattrobj("Geometry",0, value);
    }else{
      addattrobj("Geometry", value);
    }
  }
  public final static String tag_TechDetails="TechDetails";
  public String getTechDetails() {
    if(getattrvaluecount("TechDetails")==0)return null;
    String value=getattrvalue("TechDetails");
    return value;
  }
  public void setTechDetails(String value) {
    if(value==null){setattrundefined("TechDetails");return;}
    setattrvalue("TechDetails", value);
  }
}
