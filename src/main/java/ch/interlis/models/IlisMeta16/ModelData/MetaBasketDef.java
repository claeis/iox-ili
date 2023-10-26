package ch.interlis.models.IlisMeta16.ModelData;
public class MetaBasketDef extends ch.interlis.models.IlisMeta16.ModelData.ExtendableME
{
  public final static String tag= "IlisMeta16.ModelData.MetaBasketDef";
  public MetaBasketDef(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Kind="Kind";
  /** MetaElement.Name := BasketName as defined in the INTERLIS-Model
   */
  public MetaBasketDef_Kind getKind() {
    String value=getattrvalue("Kind");
    return MetaBasketDef_Kind.parseXmlCode(value);
  }
  /** MetaElement.Name := BasketName as defined in the INTERLIS-Model
   */
  public void setKind(MetaBasketDef_Kind value) {
    setattrvalue("Kind", MetaBasketDef_Kind.toXmlCode(value));
  }
  public final static String tag_MetaDataTopic="MetaDataTopic";
  public String getMetaDataTopic() {
    ch.interlis.iom.IomObject value=getattrobj("MetaDataTopic",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setMetaDataTopic(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("MetaDataTopic",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("MetaDataTopic","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("MetaDataTopic",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
