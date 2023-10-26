package ch.interlis.models.IlisMeta16.ModelData;
public class NumsRefSys extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.NumsRefSys";
  public NumsRefSys(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Axis="Axis";
  public Integer getAxis() {
    if(getattrvaluecount("Axis")==0)return null;
    String value=getattrvalue("Axis");
    return Integer.parseInt(value);
  }
  public void setAxis(Integer value) {
    if(value==null){setattrundefined("Axis");return;}
    setattrvalue("Axis", Integer.toString(value));
  }
}
