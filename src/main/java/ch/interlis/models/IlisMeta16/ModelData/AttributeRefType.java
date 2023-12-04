package ch.interlis.models.IlisMeta16.ModelData;
public class AttributeRefType extends ch.interlis.models.IlisMeta16.ModelData.DomainType
{
  public final static String tag= "IlisMeta16.ModelData.AttributeRefType";
  public AttributeRefType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Of="Of";
  public String getOf() {
    ch.interlis.iom.IomObject value=getattrobj("Of",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setOf(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Of",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Of","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Of",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
