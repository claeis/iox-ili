package ch.interlis.models.IlisMeta16.ModelData;
public class TextType extends ch.interlis.models.IlisMeta16.ModelData.DomainType
{
  public final static String tag= "IlisMeta16.ModelData.TextType";
  public TextType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Kind="Kind";
  public TextType_Kind getKind() {
    String value=getattrvalue("Kind");
    return TextType_Kind.parseXmlCode(value);
  }
  public void setKind(TextType_Kind value) {
    setattrvalue("Kind", TextType_Kind.toXmlCode(value));
  }
  public final static String tag_MaxLength="MaxLength";
  public Integer getMaxLength() {
    if(getattrvaluecount("MaxLength")==0)return null;
    String value=getattrvalue("MaxLength");
    return Integer.parseInt(value);
  }
  public void setMaxLength(Integer value) {
    if(value==null){setattrundefined("MaxLength");return;}
    setattrvalue("MaxLength", Integer.toString(value));
  }
}
