package ch.ehi.iox.ilisite.IliSite09;
public class RepositoryLocation_ extends ch.interlis.iom_j.Iom_jObject
{
  private final static String tag= "IliSite09.RepositoryLocation_";
  public RepositoryLocation_() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public String getvalue() {
    String value=getattrvalue("value");
    return value;
  }
  public void setvalue(String value) {
    setattrvalue("value", value);
  }
}
