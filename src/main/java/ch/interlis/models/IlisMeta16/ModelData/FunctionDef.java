package ch.interlis.models.IlisMeta16.ModelData;
public class FunctionDef extends ch.interlis.models.IlisMeta16.ModelData.MetaElement
{
  public final static String tag= "IlisMeta16.ModelData.FunctionDef";
  public FunctionDef(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Explanation="Explanation";
  /** MetaElement.Name := FunctionName as defined in the INTERLIS-Model
   */
  public String getExplanation() {
    if(getattrvaluecount("Explanation")==0)return null;
    String value=getattrvalue("Explanation");
    return value;
  }
  /** MetaElement.Name := FunctionName as defined in the INTERLIS-Model
   */
  public void setExplanation(String value) {
    if(value==null){setattrundefined("Explanation");return;}
    setattrvalue("Explanation", value);
  }
  public final static String tag_ResultType="ResultType";
  public String getResultType() {
    ch.interlis.iom.IomObject value=getattrobj("ResultType",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setResultType(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("ResultType",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("ResultType","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("ResultType",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
