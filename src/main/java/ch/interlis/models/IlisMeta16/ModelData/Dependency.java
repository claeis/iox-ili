package ch.interlis.models.IlisMeta16.ModelData;
public class Dependency extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.Dependency";
  public Dependency() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Using="Using";
  public String getUsing() {
    ch.interlis.iom.IomObject value=getattrobj("Using",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setUsing(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Using",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Using","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Using",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_Dependent="Dependent";
  public String getDependent() {
    ch.interlis.iom.IomObject value=getattrobj("Dependent",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setDependent(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Dependent",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Dependent","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Dependent",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
