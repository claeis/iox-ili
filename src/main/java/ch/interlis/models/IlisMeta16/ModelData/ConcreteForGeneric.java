package ch.interlis.models.IlisMeta16.ModelData;
public class ConcreteForGeneric extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.ConcreteForGeneric";
  public ConcreteForGeneric(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_GenericDef="GenericDef";
  public String getGenericDef() {
    ch.interlis.iom.IomObject value=getattrobj("GenericDef",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setGenericDef(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("GenericDef",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("GenericDef","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("GenericDef",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_ConcreteDomain="ConcreteDomain";
  public String getConcreteDomain() {
    ch.interlis.iom.IomObject value=getattrobj("ConcreteDomain",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setConcreteDomain(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("ConcreteDomain",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("ConcreteDomain","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("ConcreteDomain",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
