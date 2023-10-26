package ch.interlis.models.IlisMeta16.ModelData;
public class DomainType extends ch.interlis.models.IlisMeta16.ModelData.Type
{
  public final static String tag= "IlisMeta16.ModelData.DomainType";
  public DomainType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Mandatory="Mandatory";
  /** MetaElement.Name :=
   * DomainName if defined explicitly as a domain,
   * "Type" if defined within an attribute definition
   */
  public boolean getMandatory() {
    String value=getattrvalue("Mandatory");
    return value!=null && value.equals("true");
  }
  /** MetaElement.Name :=
   * DomainName if defined explicitly as a domain,
   * "Type" if defined within an attribute definition
   */
  public void setMandatory(boolean value) {
    setattrvalue("Mandatory", value?"true":"false");
  }
  public final static String tag_Context="Context";
  public String getContext() {
    ch.interlis.iom.IomObject value=getattrobj("Context",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setContext(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Context",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Context","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Context",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
