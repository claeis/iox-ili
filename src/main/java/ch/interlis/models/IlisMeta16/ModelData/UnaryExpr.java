package ch.interlis.models.IlisMeta16.ModelData;
public class UnaryExpr extends ch.interlis.models.IlisMeta16.ModelData.Expression
{
  public final static String tag= "IlisMeta16.ModelData.UnaryExpr";
  public UnaryExpr() {
    super();
  }
  protected UnaryExpr(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Operation="Operation";
  public UnaryExpr_Operation getOperation() {
    if(getattrvaluecount("Operation")==0)return null;
    String value=getattrvalue("Operation");
    return UnaryExpr_Operation.parseXmlCode(value);
  }
  public void setOperation(UnaryExpr_Operation value) {
    if(value==null){setattrundefined("Operation");return;}
    setattrvalue("Operation", UnaryExpr_Operation.toXmlCode(value));
  }
  public final static String tag_SubExpression="SubExpression";
  public int sizeSubExpression() {return getattrvaluecount("SubExpression");}
  public ch.interlis.models.IlisMeta16.ModelData.Expression getSubExpression() {
    int size=getattrvaluecount("SubExpression");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.Expression value=(ch.interlis.models.IlisMeta16.ModelData.Expression)getattrobj("SubExpression",0);
    return value;
  }
  public void setSubExpression(ch.interlis.models.IlisMeta16.ModelData.Expression value) {
    if(getattrvaluecount("SubExpression")>0){
      changeattrobj("SubExpression",0, value);
    }else{
      addattrobj("SubExpression", value);
    }
  }
}
