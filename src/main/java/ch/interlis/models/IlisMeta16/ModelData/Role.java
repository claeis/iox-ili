package ch.interlis.models.IlisMeta16.ModelData;
public class Role extends ch.interlis.models.IlisMeta16.ModelData.ReferenceType
{
  public final static String tag= "IlisMeta16.ModelData.Role";
  public Role(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Strongness="Strongness";
  /** MetaElement.Name := RoleName as defined in the INTERLIS-Model
   */
  public Role_Strongness getStrongness() {
    String value=getattrvalue("Strongness");
    return Role_Strongness.parseXmlCode(value);
  }
  /** MetaElement.Name := RoleName as defined in the INTERLIS-Model
   */
  public void setStrongness(Role_Strongness value) {
    setattrvalue("Strongness", Role_Strongness.toXmlCode(value));
  }
  public final static String tag_Ordered="Ordered";
  public boolean getOrdered() {
    String value=getattrvalue("Ordered");
    return value!=null && value.equals("true");
  }
  public void setOrdered(boolean value) {
    setattrvalue("Ordered", value?"true":"false");
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
  public final static String tag_Derivates="Derivates";
  public int sizeDerivates() {return getattrvaluecount("Derivates");}
  public ch.interlis.models.IlisMeta16.ModelData.Expression[] getDerivates() {
    int size=getattrvaluecount("Derivates");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.Expression value[]=new ch.interlis.models.IlisMeta16.ModelData.Expression[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta16.ModelData.Expression)getattrobj("Derivates",i);
    }
    return value;
  }
  public void addDerivates(ch.interlis.models.IlisMeta16.ModelData.Expression value) {
    addattrobj("Derivates", value);
  }
  public final static String tag_EmbeddedTransfer="EmbeddedTransfer";
  public boolean getEmbeddedTransfer() {
    String value=getattrvalue("EmbeddedTransfer");
    return value!=null && value.equals("true");
  }
  public void setEmbeddedTransfer(boolean value) {
    setattrvalue("EmbeddedTransfer", value?"true":"false");
  }
  public final static String tag_Association="Association";
  public String getAssociation() {
    ch.interlis.iom.IomObject value=getattrobj("Association",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setAssociation(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Association",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Association","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Association",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
    return oldoid;
  }
}
