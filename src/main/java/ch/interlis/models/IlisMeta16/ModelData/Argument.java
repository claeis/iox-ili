package ch.interlis.models.IlisMeta16.ModelData;
public class Argument extends ch.interlis.models.IlisMeta16.ModelData.MetaElement
{
  public final static String tag= "IlisMeta16.ModelData.Argument";
  public Argument(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Kind="Kind";
  /** MetaElement.Name := ArgumentName as defined in the INTERLIS-Model
   */
  public Argument_Kind getKind() {
    String value=getattrvalue("Kind");
    return Argument_Kind.parseXmlCode(value);
  }
  /** MetaElement.Name := ArgumentName as defined in the INTERLIS-Model
   */
  public void setKind(Argument_Kind value) {
    setattrvalue("Kind", Argument_Kind.toXmlCode(value));
  }
  public final static String tag_Function="Function";
  public String getFunction() {
    ch.interlis.iom.IomObject value=getattrobj("Function",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setFunction(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Function",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Function","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Function",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
    return oldoid;
  }
  public final static String tag_Type="Type";
  public String getType() {
    ch.interlis.iom.IomObject value=getattrobj("Type",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setType(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Type",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Type","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Type",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
