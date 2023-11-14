package ch.interlis.models.IlisMeta16.ModelData;
public class Constraint extends ch.interlis.models.IlisMeta16.ModelData.MetaElement
{
  public final static String tag= "IlisMeta16.ModelData.Constraint";
  public Constraint(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_ToClass="ToClass";
  public String getToClass() {
    ch.interlis.iom.IomObject value=getattrobj("ToClass",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setToClass(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("ToClass",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("ToClass","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("ToClass",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_ToDomain="ToDomain";
  public String getToDomain() {
    ch.interlis.iom.IomObject value=getattrobj("ToDomain",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setToDomain(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("ToDomain",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("ToDomain","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("ToDomain",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
