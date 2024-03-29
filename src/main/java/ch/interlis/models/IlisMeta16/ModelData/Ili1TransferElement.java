package ch.interlis.models.IlisMeta16.ModelData;
public class Ili1TransferElement extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.Ili1TransferElement";
  public Ili1TransferElement() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Ili1TransferClass="Ili1TransferClass";
  public String getIli1TransferClass() {
    ch.interlis.iom.IomObject value=getattrobj("Ili1TransferClass",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setIli1TransferClass(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Ili1TransferClass",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Ili1TransferClass","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Ili1TransferClass",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_Ili1RefAttr="Ili1RefAttr";
  public String getIli1RefAttr() {
    ch.interlis.iom.IomObject value=getattrobj("Ili1RefAttr",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setIli1RefAttr(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Ili1RefAttr",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Ili1RefAttr","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Ili1RefAttr",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
    return oldoid;
  }
}
