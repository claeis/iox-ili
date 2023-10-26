package ch.interlis.models.IlisMeta16.ModelData;
public class AssocAcc extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.AssocAcc";
  public AssocAcc(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag__class="Class";
  public String get_class() {
    ch.interlis.iom.IomObject value=getattrobj("Class",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String set_class(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Class",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Class","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Class",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_AssocAcc="AssocAcc";
  public String getAssocAcc() {
    ch.interlis.iom.IomObject value=getattrobj("AssocAcc",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setAssocAcc(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("AssocAcc",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("AssocAcc","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("AssocAcc",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
