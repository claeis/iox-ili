package ch.interlis.models.IlisMeta16.ModelData;
public class ExtendableME extends ch.interlis.models.IlisMeta16.ModelData.MetaElement
{
  public final static String tag= "IlisMeta16.ModelData.ExtendableME";
  public ExtendableME(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Abstract="Abstract";
  public boolean getAbstract() {
    String value=getattrvalue("Abstract");
    return value!=null && value.equals("true");
  }
  public void setAbstract(boolean value) {
    setattrvalue("Abstract", value?"true":"false");
  }
  public final static String tag_Generic="Generic";
  public boolean getGeneric() {
    String value=getattrvalue("Generic");
    return value!=null && value.equals("true");
  }
  public void setGeneric(boolean value) {
    setattrvalue("Generic", value?"true":"false");
  }
  public final static String tag_Final="Final";
  public boolean getFinal() {
    String value=getattrvalue("Final");
    return value!=null && value.equals("true");
  }
  public void setFinal(boolean value) {
    setattrvalue("Final", value?"true":"false");
  }
  public final static String tag_Super="Super";
  public String getSuper() {
    ch.interlis.iom.IomObject value=getattrobj("Super",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setSuper(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Super",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Super","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Super",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
