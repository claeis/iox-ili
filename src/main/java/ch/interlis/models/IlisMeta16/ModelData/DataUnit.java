package ch.interlis.models.IlisMeta16.ModelData;
public class DataUnit extends ch.interlis.models.IlisMeta16.ModelData.ExtendableME
{
  public final static String tag= "IlisMeta16.ModelData.DataUnit";
  public DataUnit(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_ViewUnit="ViewUnit";
  public boolean getViewUnit() {
    String value=getattrvalue("ViewUnit");
    return value!=null && value.equals("true");
  }
  public void setViewUnit(boolean value) {
    setattrvalue("ViewUnit", value?"true":"false");
  }
  public final static String tag_DataUnitName="DataUnitName";
  public String getDataUnitName() {
    String value=getattrvalue("DataUnitName");
    return value;
  }
  public void setDataUnitName(String value) {
    setattrvalue("DataUnitName", value);
  }
  public final static String tag_Oid="Oid";
  public String getOid() {
    ch.interlis.iom.IomObject value=getattrobj("Oid",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setOid(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Oid",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Oid","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Oid",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
