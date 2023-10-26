package ch.interlis.models.IlisMeta16.ModelData;
public class SetConstraint extends ch.interlis.models.IlisMeta16.ModelData.Constraint
{
  public final static String tag= "IlisMeta16.ModelData.SetConstraint";
  public SetConstraint(String oid) {
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
  public final static String tag_PerBasket="PerBasket";
  public Boolean getPerBasket() {
    if(getattrvaluecount("PerBasket")==0)return null;
    String value=getattrvalue("PerBasket");
    return value!=null && value.equals("true");
  }
  public void setPerBasket(Boolean value) {
    if(value==null){setattrundefined("PerBasket");return;}
    setattrvalue("PerBasket", value?"true":"false");
  }
  public final static String tag_Constraint="Constraint";
  public int sizeConstraint() {return getattrvaluecount("Constraint");}
  public ch.interlis.models.IlisMeta16.ModelData.Expression getConstraint() {
    int size=getattrvaluecount("Constraint");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.Expression value=(ch.interlis.models.IlisMeta16.ModelData.Expression)getattrobj("Constraint",0);
    return value;
  }
  public void setConstraint(ch.interlis.models.IlisMeta16.ModelData.Expression value) {
    if(getattrvaluecount("Constraint")>0){
      changeattrobj("Constraint",0, value);
    }else{
      addattrobj("Constraint", value);
    }
  }
}
