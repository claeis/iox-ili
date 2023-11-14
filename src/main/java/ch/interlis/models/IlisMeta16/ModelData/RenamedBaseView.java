package ch.interlis.models.IlisMeta16.ModelData;
public class RenamedBaseView extends ch.interlis.models.IlisMeta16.ModelData.ExtendableME
{
  public final static String tag= "IlisMeta16.ModelData.RenamedBaseView";
  public RenamedBaseView(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_OrNull="OrNull";
  /** MetaElement.Name := Name as defined in the INTERLIS-Model
   */
  public Boolean getOrNull() {
    if(getattrvaluecount("OrNull")==0)return null;
    String value=getattrvalue("OrNull");
    return value!=null && value.equals("true");
  }
  /** MetaElement.Name := Name as defined in the INTERLIS-Model
   */
  public void setOrNull(Boolean value) {
    if(value==null){setattrundefined("OrNull");return;}
    setattrvalue("OrNull", value?"true":"false");
  }
  public final static String tag_BaseView="BaseView";
  public String getBaseView() {
    ch.interlis.iom.IomObject value=getattrobj("BaseView",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setBaseView(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("BaseView",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("BaseView","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("BaseView",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_View="View";
  public String getView() {
    ch.interlis.iom.IomObject value=getattrobj("View",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setView(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=getattrobj("View",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("View","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("View",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
    return oldoid;
  }
}
