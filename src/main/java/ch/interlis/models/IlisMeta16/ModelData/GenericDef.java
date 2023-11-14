package ch.interlis.models.IlisMeta16.ModelData;
public class GenericDef extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.GenericDef";
  public GenericDef(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Context="Context";
  public String getContext() {
    ch.interlis.iom.IomObject value=getattrobj("Context",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setContext(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Context",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Context","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Context",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_GenericDomain="GenericDomain";
  public String getGenericDomain() {
    ch.interlis.iom.IomObject value=getattrobj("GenericDomain",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setGenericDomain(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("GenericDomain",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("GenericDomain","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("GenericDomain",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
