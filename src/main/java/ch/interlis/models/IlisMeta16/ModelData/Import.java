package ch.interlis.models.IlisMeta16.ModelData;
public class Import extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.Import";
  public Import() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_ImportingP="ImportingP";
  public String getImportingP() {
    ch.interlis.iom.IomObject value=getattrobj("ImportingP",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setImportingP(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("ImportingP",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("ImportingP","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("ImportingP",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_ImportedP="ImportedP";
  public String getImportedP() {
    ch.interlis.iom.IomObject value=getattrobj("ImportedP",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setImportedP(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("ImportedP",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("ImportedP","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("ImportedP",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
