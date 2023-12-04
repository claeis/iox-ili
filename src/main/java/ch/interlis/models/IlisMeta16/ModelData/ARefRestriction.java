package ch.interlis.models.IlisMeta16.ModelData;
public class ARefRestriction extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.ARefRestriction";
  public ARefRestriction() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_ARef="ARef";
  public String getARef() {
    ch.interlis.iom.IomObject value=getattrobj("ARef",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setARef(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("ARef",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("ARef","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("ARef",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_Type="Type";
  public String getType() {
    ch.interlis.iom.IomObject value=getattrobj("Type",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setType(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Type",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Type","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Type",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
