package ch.interlis.models.IlisMeta16.ModelData;
public class Class extends ch.interlis.models.IlisMeta16.ModelData.Type
{
  public final static String tag= "IlisMeta16.ModelData.Class";
  public Class(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Kind="Kind";
  /** MetaElement.Name := StructureName, ClassName,
   * AssociationName, ViewName
   * as defined in the INTERLIS-Model
   */
  public Class_Kind getKind() {
    String value=getattrvalue("Kind");
    return Class_Kind.parseXmlCode(value);
  }
  /** MetaElement.Name := StructureName, ClassName,
   * AssociationName, ViewName
   * as defined in the INTERLIS-Model
   */
  public void setKind(Class_Kind value) {
    setattrvalue("Kind", Class_Kind.toXmlCode(value));
  }
  public final static String tag_Multiplicity="Multiplicity";
  public int sizeMultiplicity() {return getattrvaluecount("Multiplicity");}
  public ch.interlis.models.IlisMeta16.ModelData.Multiplicity getMultiplicity() {
    int size=getattrvaluecount("Multiplicity");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.Multiplicity value=(ch.interlis.models.IlisMeta16.ModelData.Multiplicity)getattrobj("Multiplicity",0);
    return value;
  }
  public void setMultiplicity(ch.interlis.models.IlisMeta16.ModelData.Multiplicity value) {
    if(getattrvaluecount("Multiplicity")>0){
      changeattrobj("Multiplicity",0, value);
    }else{
      addattrobj("Multiplicity", value);
    }
  }
  public final static String tag_EmbeddedRoleTransfer="EmbeddedRoleTransfer";
  public boolean getEmbeddedRoleTransfer() {
    String value=getattrvalue("EmbeddedRoleTransfer");
    return value!=null && value.equals("true");
  }
  public void setEmbeddedRoleTransfer(boolean value) {
    setattrvalue("EmbeddedRoleTransfer", value?"true":"false");
  }
  public final static String tag_ili1OptionalTable="ili1OptionalTable";
  public Boolean getili1OptionalTable() {
    if(getattrvaluecount("ili1OptionalTable")==0)return null;
    String value=getattrvalue("ili1OptionalTable");
    return value!=null && value.equals("true");
  }
  public void setili1OptionalTable(Boolean value) {
    if(value==null){setattrundefined("ili1OptionalTable");return;}
    setattrvalue("ili1OptionalTable", value?"true":"false");
  }
  public final static String tag_Oid="Oid";
  public String getOid() {
    ch.interlis.iom.IomObject value=getattrobj("Oid",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setOid(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Oid",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Oid","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Oid",0);
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
  public String setView(String oid) {
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
    return oldoid;
  }
}
