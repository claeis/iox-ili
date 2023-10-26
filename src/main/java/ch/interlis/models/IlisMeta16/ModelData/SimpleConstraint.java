package ch.interlis.models.IlisMeta16.ModelData;
public class SimpleConstraint extends ch.interlis.models.IlisMeta16.ModelData.Constraint
{
  public final static String tag= "IlisMeta16.ModelData.SimpleConstraint";
  public SimpleConstraint(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Kind="Kind";
  public SimpleConstraint_Kind getKind() {
    if(getattrvaluecount("Kind")==0)return null;
    String value=getattrvalue("Kind");
    return SimpleConstraint_Kind.parseXmlCode(value);
  }
  public void setKind(SimpleConstraint_Kind value) {
    if(value==null){setattrundefined("Kind");return;}
    setattrvalue("Kind", SimpleConstraint_Kind.toXmlCode(value));
  }
  public final static String tag_Percentage="Percentage";
  public Double getPercentage() {
    if(getattrvaluecount("Percentage")==0)return null;
    String value=getattrvalue("Percentage");
    return Double.parseDouble(value);
  }
  public void setPercentage(Double value) {
    if(value==null){setattrundefined("Percentage");return;}
    setattrvalue("Percentage", Double.toString(value));
  }
  public final static String tag_LogicalExpression="LogicalExpression";
  public int sizeLogicalExpression() {return getattrvaluecount("LogicalExpression");}
  public ch.interlis.models.IlisMeta16.ModelData.Expression getLogicalExpression() {
    int size=getattrvaluecount("LogicalExpression");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.Expression value=(ch.interlis.models.IlisMeta16.ModelData.Expression)getattrobj("LogicalExpression",0);
    return value;
  }
  public void setLogicalExpression(ch.interlis.models.IlisMeta16.ModelData.Expression value) {
    if(getattrvaluecount("LogicalExpression")>0){
      changeattrobj("LogicalExpression",0, value);
    }else{
      addattrobj("LogicalExpression", value);
    }
  }
}
