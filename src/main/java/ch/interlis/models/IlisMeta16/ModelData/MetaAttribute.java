package ch.interlis.models.IlisMeta16.ModelData;
public class MetaAttribute extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.MetaAttribute";
  public MetaAttribute(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Name="Name";
  /** OID: <Parent-OID>.METAOBJECT.Name
   */
  public String getName() {
    String value=getattrvalue("Name");
    return value;
  }
  /** OID: <Parent-OID>.METAOBJECT.Name
   */
  public void setName(String value) {
    setattrvalue("Name", value);
  }
  public final static String tag_Value="Value";
  public String getValue() {
    String value=getattrvalue("Value");
    return value;
  }
  public void setValue(String value) {
    setattrvalue("Value", value);
  }
  public final static String tag_MetaElement="MetaElement";
  public String getMetaElement() {
    ch.interlis.iom.IomObject value=getattrobj("MetaElement",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setMetaElement(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("MetaElement",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("MetaElement","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("MetaElement",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
