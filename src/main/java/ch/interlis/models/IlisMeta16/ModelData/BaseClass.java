package ch.interlis.models.IlisMeta16.ModelData;
public class BaseClass extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.BaseClass";
  public BaseClass() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_CRT="CRT";
  public String getCRT() {
    ch.interlis.iom.IomObject value=getattrobj("CRT",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setCRT(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("CRT",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("CRT","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("CRT",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_BaseClass="BaseClass";
  public String getBaseClass() {
    ch.interlis.iom.IomObject value=getattrobj("BaseClass",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setBaseClass(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("BaseClass",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("BaseClass","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("BaseClass",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
