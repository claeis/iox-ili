package ch.interlis.models.IlisMeta16.ModelData;
public class ExistenceDef extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.ExistenceDef";
  public ExistenceDef(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Attr="Attr";
  public int sizeAttr() {return getattrvaluecount("Attr");}
  public ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor getAttr() {
    int size=getattrvaluecount("Attr");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor value=(ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor)getattrobj("Attr",0);
    return value;
  }
  public void setAttr(ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor value) {
    if(getattrvaluecount("Attr")>0){
      changeattrobj("Attr",0, value);
    }else{
      addattrobj("Attr", value);
    }
  }
}
