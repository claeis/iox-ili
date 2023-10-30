package ch.interlis.models.IlisMeta16.ModelData;
public class TypeRestriction extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.TypeRestriction";
  public TypeRestriction() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_TRTR="TRTR";
  public String getTRTR() {
    ch.interlis.iom.IomObject value=getattrobj("TRTR",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setTRTR(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("TRTR",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("TRTR","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("TRTR",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_TypeRestriction="TypeRestriction";
  public String getTypeRestriction() {
    ch.interlis.iom.IomObject value=getattrobj("TypeRestriction",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setTypeRestriction(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("TypeRestriction",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("TypeRestriction","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("TypeRestriction",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
