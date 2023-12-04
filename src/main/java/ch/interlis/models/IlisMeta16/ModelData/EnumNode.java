package ch.interlis.models.IlisMeta16.ModelData;
public class EnumNode extends ch.interlis.models.IlisMeta16.ModelData.ExtendableME
{
  public final static String tag= "IlisMeta16.ModelData.EnumNode";
  public EnumNode(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_EnumType="EnumType";
  public String getEnumType() {
    ch.interlis.iom.IomObject value=getattrobj("EnumType",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setEnumType(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=getattrobj("EnumType",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("EnumType","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("EnumType",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
    return oldoid;
  }
  public final static String tag_ParentNode="ParentNode";
  public String getParentNode() {
    ch.interlis.iom.IomObject value=getattrobj("ParentNode",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setParentNode(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=getattrobj("ParentNode",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("ParentNode","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("ParentNode",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
    return oldoid;
  }
}
