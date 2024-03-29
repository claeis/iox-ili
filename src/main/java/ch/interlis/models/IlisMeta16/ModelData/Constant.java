package ch.interlis.models.IlisMeta16.ModelData;
public class Constant extends ch.interlis.models.IlisMeta16.ModelData.Factor
{
  public final static String tag= "IlisMeta16.ModelData.Constant";
  public Constant() {
    super();
  }
  protected Constant(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Value="Value";
  public String getValue() {
    String value=getattrvalue("Value");
    return value;
  }
  public void setValue(String value) {
    setattrvalue("Value", value);
  }
  public final static String tag_Type="Type";
  public Constant_Type getType() {
    String value=getattrvalue("Type");
    return Constant_Type.parseXmlCode(value);
  }
  public void setType(Constant_Type value) {
    setattrvalue("Type", Constant_Type.toXmlCode(value));
  }
}
