package ch.ehi.iox.ilisite.IliRepository09;
public class WebSite_ extends ch.interlis.iom_j.Iom_jObject
{
  private final static String tag= "IliRepository09.WebSite_";
  public WebSite_() {
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
