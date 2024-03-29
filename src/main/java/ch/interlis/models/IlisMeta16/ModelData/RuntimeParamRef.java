package ch.interlis.models.IlisMeta16.ModelData;
public class RuntimeParamRef extends ch.interlis.models.IlisMeta16.ModelData.Factor
{
  public final static String tag= "IlisMeta16.ModelData.RuntimeParamRef";
  public RuntimeParamRef() {
    super();
  }
  protected RuntimeParamRef(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_RuntimeParam="RuntimeParam";
  public String getRuntimeParam() {
    ch.interlis.iom.IomObject value=getattrobj("RuntimeParam",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setRuntimeParam(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("RuntimeParam","REF");
    structvalue.setobjectrefoid(oid);
  }
}
