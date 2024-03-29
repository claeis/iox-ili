package ch.interlis.models.IlisMeta16.ModelData;
public class UniqueConstraint extends ch.interlis.models.IlisMeta16.ModelData.Constraint
{
  public final static String tag= "IlisMeta16.ModelData.UniqueConstraint";
  public UniqueConstraint(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Where="Where";
  public int sizeWhere() {return getattrvaluecount("Where");}
  public ch.interlis.models.IlisMeta16.ModelData.Expression getWhere() {
    int size=getattrvaluecount("Where");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.Expression value=(ch.interlis.models.IlisMeta16.ModelData.Expression)getattrobj("Where",0);
    return value;
  }
  public void setWhere(ch.interlis.models.IlisMeta16.ModelData.Expression value) {
    if(getattrvaluecount("Where")>0){
      changeattrobj("Where",0, value);
    }else{
      addattrobj("Where", value);
    }
  }
  public final static String tag_Kind="Kind";
  public UniqueConstraint_Kind getKind() {
    String value=getattrvalue("Kind");
    return UniqueConstraint_Kind.parseXmlCode(value);
  }
  public void setKind(UniqueConstraint_Kind value) {
    setattrvalue("Kind", UniqueConstraint_Kind.toXmlCode(value));
  }
  public final static String tag_UniqueDef="UniqueDef";
  public int sizeUniqueDef() {return getattrvaluecount("UniqueDef");}
  public ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor[] getUniqueDef() {
    int size=getattrvaluecount("UniqueDef");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor value[]=new ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor)getattrobj("UniqueDef",i);
    }
    return value;
  }
  public void addUniqueDef(ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor value) {
    addattrobj("UniqueDef", value);
  }
}
