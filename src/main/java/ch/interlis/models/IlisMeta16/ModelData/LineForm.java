package ch.interlis.models.IlisMeta16.ModelData;
public class LineForm extends ch.interlis.models.IlisMeta16.ModelData.MetaElement
{
  public final static String tag= "IlisMeta16.ModelData.LineForm";
  public LineForm(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Structure="Structure";
  public String getStructure() {
    ch.interlis.iom.IomObject value=getattrobj("Structure",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setStructure(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Structure",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Structure","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Structure",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
