package ch.interlis.models.IlisMeta16.ModelData;
public class Multiplicity extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.Multiplicity";
  public Multiplicity() {
    super(tag,null);
  }
  protected Multiplicity(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Min="Min";
  public int getMin() {
    String value=getattrvalue("Min");
    return Integer.parseInt(value);
  }
  public void setMin(int value) {
    setattrvalue("Min", Integer.toString(value));
  }
  public final static String tag_Max="Max";
  public Integer getMax() {
    if(getattrvaluecount("Max")==0)return null;
    String value=getattrvalue("Max");
    return Integer.parseInt(value);
  }
  public void setMax(Integer value) {
    if(value==null){setattrundefined("Max");return;}
    setattrvalue("Max", Integer.toString(value));
  }
}
