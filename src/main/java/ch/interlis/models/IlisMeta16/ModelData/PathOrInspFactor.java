package ch.interlis.models.IlisMeta16.ModelData;
public class PathOrInspFactor extends ch.interlis.models.IlisMeta16.ModelData.Factor
{
  public final static String tag= "IlisMeta16.ModelData.PathOrInspFactor";
  public PathOrInspFactor() {
    super();
  }
  protected PathOrInspFactor(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_PathEls="PathEls";
  public int sizePathEls() {return getattrvaluecount("PathEls");}
  public ch.interlis.models.IlisMeta16.ModelData.PathEl[] getPathEls() {
    int size=getattrvaluecount("PathEls");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.PathEl value[]=new ch.interlis.models.IlisMeta16.ModelData.PathEl[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta16.ModelData.PathEl)getattrobj("PathEls",i);
    }
    return value;
  }
  public void addPathEls(ch.interlis.models.IlisMeta16.ModelData.PathEl value) {
    addattrobj("PathEls", value);
  }
  public final static String tag_Inspection="Inspection";
  public String getInspection() {
    ch.interlis.iom.IomObject value=getattrobj("Inspection",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setInspection(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Inspection","REF");
    structvalue.setobjectrefoid(oid);
  }
}
