package ch.interlis.models.IlisMeta16.ModelData;
public class FormattedType extends ch.interlis.models.IlisMeta16.ModelData.NumType
{
  public final static String tag= "IlisMeta16.ModelData.FormattedType";
  public FormattedType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Format="Format";
  public String getFormat() {
    String value=getattrvalue("Format");
    return value;
  }
  public void setFormat(String value) {
    setattrvalue("Format", value);
  }
  public final static String tag_Struct="Struct";
  public String getStruct() {
    ch.interlis.iom.IomObject value=getattrobj("Struct",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setStruct(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Struct",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Struct","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Struct",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
