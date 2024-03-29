package ch.interlis.models.IlisMeta16.ModelData;
public class CompoundExpr_Operation{
  static protected java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  protected CompoundExpr_Operation(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(CompoundExpr_Operation value) {
     return value.value;
  }
  static public CompoundExpr_Operation parseXmlCode(String value) {
     return (CompoundExpr_Operation)valuev.get(value);
  }
  static public CompoundExpr_Operation Implication=new CompoundExpr_Operation("Implication");
  public final static String tag_Implication="Implication";
  static public CompoundExpr_Operation And=new CompoundExpr_Operation("And");
  public final static String tag_And="And";
  static public CompoundExpr_Operation Or=new CompoundExpr_Operation("Or");
  public final static String tag_Or="Or";
  static public CompoundExpr_Operation Mult=new CompoundExpr_Operation("Mult");
  public final static String tag_Mult="Mult";
  static public CompoundExpr_Operation Div=new CompoundExpr_Operation("Div");
  public final static String tag_Div="Div";
  static public CompoundExpr_Operation Add=new CompoundExpr_Operation("Add");
  public final static String tag_Add="Add";
  static public CompoundExpr_Operation Sub=new CompoundExpr_Operation("Sub");
  public final static String tag_Sub="Sub";
  static public CompoundExpr_Operation Relation_Equal=new CompoundExpr_Operation("Relation.Equal");
  public final static String tag_Relation_Equal="Relation.Equal";
  static public CompoundExpr_Operation Relation_NotEqual=new CompoundExpr_Operation("Relation.NotEqual");
  public final static String tag_Relation_NotEqual="Relation.NotEqual";
  static public CompoundExpr_Operation Relation_LessOrEqual=new CompoundExpr_Operation("Relation.LessOrEqual");
  public final static String tag_Relation_LessOrEqual="Relation.LessOrEqual";
  static public CompoundExpr_Operation Relation_GreaterOrEqual=new CompoundExpr_Operation("Relation.GreaterOrEqual");
  public final static String tag_Relation_GreaterOrEqual="Relation.GreaterOrEqual";
  static public CompoundExpr_Operation Relation_Less=new CompoundExpr_Operation("Relation.Less");
  public final static String tag_Relation_Less="Relation.Less";
  static public CompoundExpr_Operation Relation_Greater=new CompoundExpr_Operation("Relation.Greater");
  public final static String tag_Relation_Greater="Relation.Greater";
}
