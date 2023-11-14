package ch.interlis.models.IlisMeta16.ModelData;
public class MetaObjectDef extends ch.interlis.models.IlisMeta16.ModelData.MetaElement
{
  public final static String tag= "IlisMeta16.ModelData.MetaObjectDef";
  public MetaObjectDef(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_IsRefSystem="IsRefSystem";
  /** MetaElement.Name := MetaObjectName as defined in the INTERLIS-Model
   */
  public boolean getIsRefSystem() {
    String value=getattrvalue("IsRefSystem");
    return value!=null && value.equals("true");
  }
  /** MetaElement.Name := MetaObjectName as defined in the INTERLIS-Model
   */
  public void setIsRefSystem(boolean value) {
    setattrvalue("IsRefSystem", value?"true":"false");
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
  public final static String tag_MetaBasketDef="MetaBasketDef";
  public String getMetaBasketDef() {
    ch.interlis.iom.IomObject value=getattrobj("MetaBasketDef",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setMetaBasketDef(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("MetaBasketDef",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("MetaBasketDef","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("MetaBasketDef",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
