package ch.interlis.models.IlisMeta16.ModelData;
public class ClassRestriction extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.ClassRestriction";
  public ClassRestriction(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_CRTR="CRTR";
  public String getCRTR() {
    ch.interlis.iom.IomObject value=getattrobj("CRTR",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setCRTR(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("CRTR",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("CRTR","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("CRTR",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_ClassRestriction="ClassRestriction";
  public String getClassRestriction() {
    ch.interlis.iom.IomObject value=getattrobj("ClassRestriction",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setClassRestriction(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("ClassRestriction",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("ClassRestriction","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("ClassRestriction",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
