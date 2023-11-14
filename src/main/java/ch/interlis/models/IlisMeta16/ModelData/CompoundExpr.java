package ch.interlis.models.IlisMeta16.ModelData;
public class CompoundExpr extends ch.interlis.models.IlisMeta16.ModelData.Expression
{
  public final static String tag= "IlisMeta16.ModelData.CompoundExpr";
  public CompoundExpr() {
    super();
  }
  protected CompoundExpr(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Operation="Operation";
  public CompoundExpr_Operation getOperation() {
    if(getattrvaluecount("Operation")==0)return null;
    String value=getattrvalue("Operation");
    return CompoundExpr_Operation.parseXmlCode(value);
  }
  public void setOperation(CompoundExpr_Operation value) {
    if(value==null){setattrundefined("Operation");return;}
    setattrvalue("Operation", CompoundExpr_Operation.toXmlCode(value));
  }
  public final static String tag_SubExpressions="SubExpressions";
  public int sizeSubExpressions() {return getattrvaluecount("SubExpressions");}
  public ch.interlis.models.IlisMeta16.ModelData.Expression[] getSubExpressions() {
    int size=getattrvaluecount("SubExpressions");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.Expression value[]=new ch.interlis.models.IlisMeta16.ModelData.Expression[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta16.ModelData.Expression)getattrobj("SubExpressions",i);
    }
    return value;
  }
  public void addSubExpressions(ch.interlis.models.IlisMeta16.ModelData.Expression value) {
    addattrobj("SubExpressions", value);
  }
}
