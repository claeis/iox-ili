package ch.interlis.models.IlisMeta16.ModelData;
public class NumType extends ch.interlis.models.IlisMeta16.ModelData.DomainType
{
  public final static String tag= "IlisMeta16.ModelData.NumType";
  public NumType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Min="Min";
  /** MetaElement.Name :=
   * DomainName if defined explicitly as a domain,
   * "Type" if defined within an attribute definition,
   * "C1", "C2", "C3" if defined within a coordinate type
   */
  public String getMin() {
    if(getattrvaluecount("Min")==0)return null;
    String value=getattrvalue("Min");
    return value;
  }
  /** MetaElement.Name :=
   * DomainName if defined explicitly as a domain,
   * "Type" if defined within an attribute definition,
   * "C1", "C2", "C3" if defined within a coordinate type
   */
  public void setMin(String value) {
    if(value==null){setattrundefined("Min");return;}
    setattrvalue("Min", value);
  }
  public final static String tag_Max="Max";
  public String getMax() {
    if(getattrvaluecount("Max")==0)return null;
    String value=getattrvalue("Max");
    return value;
  }
  public void setMax(String value) {
    if(value==null){setattrundefined("Max");return;}
    setattrvalue("Max", value);
  }
  public final static String tag_Circular="Circular";
  public Boolean getCircular() {
    if(getattrvaluecount("Circular")==0)return null;
    String value=getattrvalue("Circular");
    return value!=null && value.equals("true");
  }
  public void setCircular(Boolean value) {
    if(value==null){setattrundefined("Circular");return;}
    setattrvalue("Circular", value?"true":"false");
  }
  public final static String tag_Clockwise="Clockwise";
  public Boolean getClockwise() {
    if(getattrvaluecount("Clockwise")==0)return null;
    String value=getattrvalue("Clockwise");
    return value!=null && value.equals("true");
  }
  public void setClockwise(Boolean value) {
    if(value==null){setattrundefined("Clockwise");return;}
    setattrvalue("Clockwise", value?"true":"false");
  }
  public final static String tag_RefSys="RefSys";
  public String getRefSys() {
    ch.interlis.iom.IomObject value=getattrobj("RefSys",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setRefSys(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("RefSys",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("RefSys","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("RefSys",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_Unit="Unit";
  public String getUnit() {
    ch.interlis.iom.IomObject value=getattrobj("Unit",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setUnit(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Unit",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Unit","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Unit",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
