package ch.interlis.models.IlisMeta16.ModelData;
public class CoordType extends ch.interlis.models.IlisMeta16.ModelData.DomainType
{
  public final static String tag= "IlisMeta16.ModelData.CoordType";
  public CoordType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_NullAxis="NullAxis";
  public Integer getNullAxis() {
    if(getattrvaluecount("NullAxis")==0)return null;
    String value=getattrvalue("NullAxis");
    return Integer.parseInt(value);
  }
  public void setNullAxis(Integer value) {
    if(value==null){setattrundefined("NullAxis");return;}
    setattrvalue("NullAxis", Integer.toString(value));
  }
  public final static String tag_PiHalfAxis="PiHalfAxis";
  public Integer getPiHalfAxis() {
    if(getattrvaluecount("PiHalfAxis")==0)return null;
    String value=getattrvalue("PiHalfAxis");
    return Integer.parseInt(value);
  }
  public void setPiHalfAxis(Integer value) {
    if(value==null){setattrundefined("PiHalfAxis");return;}
    setattrvalue("PiHalfAxis", Integer.toString(value));
  }
  public final static String tag_Multi="Multi";
  public Boolean getMulti() {
    if(getattrvaluecount("Multi")==0)return null;
    String value=getattrvalue("Multi");
    return value!=null && value.equals("true");
  }
  public void setMulti(Boolean value) {
    if(value==null){setattrundefined("Multi");return;}
    setattrvalue("Multi", value?"true":"false");
  }
}
