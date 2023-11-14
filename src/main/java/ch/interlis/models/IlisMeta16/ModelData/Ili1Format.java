package ch.interlis.models.IlisMeta16.ModelData;
public class Ili1Format extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.Ili1Format";
  public Ili1Format() {
    super(tag,null);
  }
  protected Ili1Format(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_isFree="isFree";
  public boolean getisFree() {
    String value=getattrvalue("isFree");
    return value!=null && value.equals("true");
  }
  public void setisFree(boolean value) {
    setattrvalue("isFree", value?"true":"false");
  }
  public final static String tag_LineSize="LineSize";
  public Integer getLineSize() {
    if(getattrvaluecount("LineSize")==0)return null;
    String value=getattrvalue("LineSize");
    return Integer.parseInt(value);
  }
  public void setLineSize(Integer value) {
    if(value==null){setattrundefined("LineSize");return;}
    setattrvalue("LineSize", Integer.toString(value));
  }
  public final static String tag_tidSize="tidSize";
  public Integer gettidSize() {
    if(getattrvaluecount("tidSize")==0)return null;
    String value=getattrvalue("tidSize");
    return Integer.parseInt(value);
  }
  public void settidSize(Integer value) {
    if(value==null){setattrundefined("tidSize");return;}
    setattrvalue("tidSize", Integer.toString(value));
  }
  public final static String tag_blankCode="blankCode";
  public int getblankCode() {
    String value=getattrvalue("blankCode");
    return Integer.parseInt(value);
  }
  public void setblankCode(int value) {
    setattrvalue("blankCode", Integer.toString(value));
  }
  public final static String tag_undefinedCode="undefinedCode";
  public int getundefinedCode() {
    String value=getattrvalue("undefinedCode");
    return Integer.parseInt(value);
  }
  public void setundefinedCode(int value) {
    setattrvalue("undefinedCode", Integer.toString(value));
  }
  public final static String tag_continueCode="continueCode";
  public int getcontinueCode() {
    String value=getattrvalue("continueCode");
    return Integer.parseInt(value);
  }
  public void setcontinueCode(int value) {
    setattrvalue("continueCode", Integer.toString(value));
  }
  public final static String tag_Font="Font";
  public String getFont() {
    String value=getattrvalue("Font");
    return value;
  }
  public void setFont(String value) {
    setattrvalue("Font", value);
  }
  public final static String tag_tidKind="tidKind";
  public Ili1Format_tidKind gettidKind() {
    String value=getattrvalue("tidKind");
    return Ili1Format_tidKind.parseXmlCode(value);
  }
  public void settidKind(Ili1Format_tidKind value) {
    setattrvalue("tidKind", Ili1Format_tidKind.toXmlCode(value));
  }
  public final static String tag_tidExplanation="tidExplanation";
  public String gettidExplanation() {
    if(getattrvaluecount("tidExplanation")==0)return null;
    String value=getattrvalue("tidExplanation");
    return value;
  }
  public void settidExplanation(String value) {
    if(value==null){setattrundefined("tidExplanation");return;}
    setattrvalue("tidExplanation", value);
  }
}
