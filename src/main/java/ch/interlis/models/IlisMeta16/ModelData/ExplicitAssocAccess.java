package ch.interlis.models.IlisMeta16.ModelData;
public class ExplicitAssocAccess extends ch.interlis.models.IlisMeta16.ModelData.ExtendableME
{
  public final static String tag= "IlisMeta16.ModelData.ExplicitAssocAccess";
  public ExplicitAssocAccess(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_AssocAccOf="AssocAccOf";
  public String getAssocAccOf() {
    ch.interlis.iom.IomObject value=getattrobj("AssocAccOf",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setAssocAccOf(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=getattrobj("AssocAccOf",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("AssocAccOf","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("AssocAccOf",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
    return oldoid;
  }
  public final static String tag_OriginRole="OriginRole";
  public String getOriginRole() {
    ch.interlis.iom.IomObject value=getattrobj("OriginRole",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setOriginRole(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("OriginRole",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("OriginRole","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("OriginRole",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_TargetRole="TargetRole";
  public String getTargetRole() {
    ch.interlis.iom.IomObject value=getattrobj("TargetRole",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setTargetRole(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("TargetRole",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("TargetRole","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("TargetRole",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
