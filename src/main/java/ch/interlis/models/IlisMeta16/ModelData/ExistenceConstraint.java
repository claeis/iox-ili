package ch.interlis.models.IlisMeta16.ModelData;
public class ExistenceConstraint extends ch.interlis.models.IlisMeta16.ModelData.Constraint
{
  public final static String tag= "IlisMeta16.ModelData.ExistenceConstraint";
  public ExistenceConstraint(String oid) {
    super(oid);
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
  public final static String tag_RequiredIn="RequiredIn";
  public int sizeRequiredIn() {return getattrvaluecount("RequiredIn");}
  public ch.interlis.models.IlisMeta16.ModelData.ExistenceDef[] getRequiredIn() {
    int size=getattrvaluecount("RequiredIn");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.ExistenceDef value[]=new ch.interlis.models.IlisMeta16.ModelData.ExistenceDef[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta16.ModelData.ExistenceDef)getattrobj("RequiredIn",i);
    }
    return value;
  }
  public void addRequiredIn(ch.interlis.models.IlisMeta16.ModelData.ExistenceDef value) {
    addattrobj("RequiredIn", value);
  }
}
