package ch.interlis.models.IlisMeta16.ModelData;
public class DrawingRule extends ch.interlis.models.IlisMeta16.ModelData.ExtendableME
{
  public final static String tag= "IlisMeta16.ModelData.DrawingRule";
  public DrawingRule(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Rule="Rule";
  /** MetaElement.Name := Name as defined in the INTERLIS-Model
   */
  public int sizeRule() {return getattrvaluecount("Rule");}
  public ch.interlis.models.IlisMeta16.ModelData.CondSignParamAssignment[] getRule() {
    int size=getattrvaluecount("Rule");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.CondSignParamAssignment value[]=new ch.interlis.models.IlisMeta16.ModelData.CondSignParamAssignment[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta16.ModelData.CondSignParamAssignment)getattrobj("Rule",i);
    }
    return value;
  }
  /** MetaElement.Name := Name as defined in the INTERLIS-Model
   */
  public void addRule(ch.interlis.models.IlisMeta16.ModelData.CondSignParamAssignment value) {
    addattrobj("Rule", value);
  }
  public final static String tag__class="Class";
  public String get_class() {
    ch.interlis.iom.IomObject value=getattrobj("Class",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String set_class(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Class",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Class","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Class",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_Graphic="Graphic";
  public String getGraphic() {
    ch.interlis.iom.IomObject value=getattrobj("Graphic",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setGraphic(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Graphic",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Graphic","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Graphic",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
