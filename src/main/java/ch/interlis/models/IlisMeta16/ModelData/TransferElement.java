package ch.interlis.models.IlisMeta16.ModelData;
public class TransferElement extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.TransferElement";
  public TransferElement() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_TransferClass="TransferClass";
  public String getTransferClass() {
    ch.interlis.iom.IomObject value=getattrobj("TransferClass",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setTransferClass(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("TransferClass",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("TransferClass","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("TransferClass",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_TransferElement="TransferElement";
  public String getTransferElement() {
    ch.interlis.iom.IomObject value=getattrobj("TransferElement",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setTransferElement(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=getattrobj("TransferElement",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("TransferElement","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("TransferElement",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
    return oldoid;
  }
}
