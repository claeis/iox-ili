package ch.interlis.models.IlisMeta16.ModelData;
public class ActualArgument extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.ActualArgument";
  public ActualArgument() {
    super(tag,null);
  }
  protected ActualArgument(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_FormalArgument="FormalArgument";
  public String getFormalArgument() {
    ch.interlis.iom.IomObject value=getattrobj("FormalArgument",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setFormalArgument(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("FormalArgument","REF");
    structvalue.setobjectrefoid(oid);
  }
  public final static String tag_Kind="Kind";
  public ActualArgument_Kind getKind() {
    String value=getattrvalue("Kind");
    return ActualArgument_Kind.parseXmlCode(value);
  }
  public void setKind(ActualArgument_Kind value) {
    setattrvalue("Kind", ActualArgument_Kind.toXmlCode(value));
  }
  public final static String tag_Expression="Expression";
  public int sizeExpression() {return getattrvaluecount("Expression");}
  public ch.interlis.models.IlisMeta16.ModelData.Expression getExpression() {
    int size=getattrvaluecount("Expression");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.Expression value=(ch.interlis.models.IlisMeta16.ModelData.Expression)getattrobj("Expression",0);
    return value;
  }
  public void setExpression(ch.interlis.models.IlisMeta16.ModelData.Expression value) {
    if(getattrvaluecount("Expression")>0){
      changeattrobj("Expression",0, value);
    }else{
      addattrobj("Expression", value);
    }
  }
  public final static String tag_ObjectClasses="ObjectClasses";
  public int sizeObjectClasses() {return getattrvaluecount("ObjectClasses");}
  public ch.interlis.models.IlisMeta16.ModelData.ClassRef[] getObjectClasses() {
    int size=getattrvaluecount("ObjectClasses");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.ClassRef value[]=new ch.interlis.models.IlisMeta16.ModelData.ClassRef[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta16.ModelData.ClassRef)getattrobj("ObjectClasses",i);
    }
    return value;
  }
  public void addObjectClasses(ch.interlis.models.IlisMeta16.ModelData.ClassRef value) {
    addattrobj("ObjectClasses", value);
  }
}
