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
}
