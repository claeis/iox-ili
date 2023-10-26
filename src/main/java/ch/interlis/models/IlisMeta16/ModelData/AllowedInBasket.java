package ch.interlis.models.IlisMeta16.ModelData;
public class AllowedInBasket extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.AllowedInBasket";
  public AllowedInBasket(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_OfDataUnit="OfDataUnit";
  public String getOfDataUnit() {
    ch.interlis.iom.IomObject value=getattrobj("OfDataUnit",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setOfDataUnit(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("OfDataUnit",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("OfDataUnit","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("OfDataUnit",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_ClassInBasket="ClassInBasket";
  public String getClassInBasket() {
    ch.interlis.iom.IomObject value=getattrobj("ClassInBasket",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setClassInBasket(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("ClassInBasket",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("ClassInBasket","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("ClassInBasket",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
