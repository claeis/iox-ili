package ch.interlis.models.IlisMeta16.ModelData;
public class TypeRelatedType extends ch.interlis.models.IlisMeta16.ModelData.DomainType
{
  public final static String tag= "IlisMeta16.ModelData.TypeRelatedType";
  public TypeRelatedType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_BaseType="BaseType";
  public String getBaseType() {
    ch.interlis.iom.IomObject value=getattrobj("BaseType",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setBaseType(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("BaseType",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("BaseType","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("BaseType",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
