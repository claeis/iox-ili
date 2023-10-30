package ch.interlis.models.IlisMeta16.ModelData;
public class LinesForm extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.LinesForm";
  public LinesForm() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_LineType="LineType";
  public String getLineType() {
    ch.interlis.iom.IomObject value=getattrobj("LineType",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setLineType(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("LineType",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("LineType","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("LineType",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_LineForm="LineForm";
  public String getLineForm() {
    ch.interlis.iom.IomObject value=getattrobj("LineForm",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setLineForm(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("LineForm",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("LineForm","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("LineForm",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
