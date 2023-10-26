package ch.interlis.models.IlisMeta16.ModelData;
public class Type extends ch.interlis.models.IlisMeta16.ModelData.ExtendableME
{
  public final static String tag= "IlisMeta16.ModelData.Type";
  public Type(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_LFTParent="LFTParent";
  public String getLFTParent() {
    ch.interlis.iom.IomObject value=getattrobj("LFTParent",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setLFTParent(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("LFTParent",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("LFTParent","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("LFTParent",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_LTParent="LTParent";
  public String getLTParent() {
    ch.interlis.iom.IomObject value=getattrobj("LTParent",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setLTParent(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("LTParent",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("LTParent","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("LTParent",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
