package ch.interlis.models.IlisMeta16.ModelData;
public class Unit extends ch.interlis.models.IlisMeta16.ModelData.ExtendableME
{
  public final static String tag= "IlisMeta16.ModelData.Unit";
  public Unit(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Kind="Kind";
  /** MetaElement.Name := ShortName as defined in the INTERLIS-Model
   */
  public Unit_Kind getKind() {
    String value=getattrvalue("Kind");
    return Unit_Kind.parseXmlCode(value);
  }
  /** MetaElement.Name := ShortName as defined in the INTERLIS-Model
   */
  public void setKind(Unit_Kind value) {
    setattrvalue("Kind", Unit_Kind.toXmlCode(value));
  }
  public final static String tag_Definition="Definition";
  public int sizeDefinition() {return getattrvaluecount("Definition");}
  public ch.interlis.models.IlisMeta16.ModelData.Expression getDefinition() {
    int size=getattrvaluecount("Definition");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.Expression value=(ch.interlis.models.IlisMeta16.ModelData.Expression)getattrobj("Definition",0);
    return value;
  }
  public void setDefinition(ch.interlis.models.IlisMeta16.ModelData.Expression value) {
    if(getattrvaluecount("Definition")>0){
      changeattrobj("Definition",0, value);
    }else{
      addattrobj("Definition", value);
    }
  }
}
