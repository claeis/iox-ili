package ch.interlis.models.IlisMeta16.ModelData;
public class Graphic extends ch.interlis.models.IlisMeta16.ModelData.ExtendableME
{
  public final static String tag= "IlisMeta16.ModelData.Graphic";
  public Graphic(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Where="Where";
  /** MetaElement.Name := Name as defined in the INTERLIS-Model
   */
  public int sizeWhere() {return getattrvaluecount("Where");}
  public ch.interlis.models.IlisMeta16.ModelData.Expression getWhere() {
    int size=getattrvaluecount("Where");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.Expression value=(ch.interlis.models.IlisMeta16.ModelData.Expression)getattrobj("Where",0);
    return value;
  }
  /** MetaElement.Name := Name as defined in the INTERLIS-Model
   */
  public void setWhere(ch.interlis.models.IlisMeta16.ModelData.Expression value) {
    if(getattrvaluecount("Where")>0){
      changeattrobj("Where",0, value);
    }else{
      addattrobj("Where", value);
    }
  }
  public final static String tag_Base="Base";
  public String getBase() {
    ch.interlis.iom.IomObject value=getattrobj("Base",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setBase(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Base",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Base","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Base",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
