package ch.interlis.models.IlisMeta16.ModelData;
public class MetaElement extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.MetaElement";
  public MetaElement(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Name="Name";
  /** OID: <Parent-OID>.Name
   */
  public String getName() {
    String value=getattrvalue("Name");
    return value;
  }
  /** OID: <Parent-OID>.Name
   */
  public void setName(String value) {
    setattrvalue("Name", value);
  }
  public final static String tag_Documentation="Documentation";
  public int sizeDocumentation() {return getattrvaluecount("Documentation");}
  public ch.interlis.models.IlisMeta16.ModelData.DocText[] getDocumentation() {
    int size=getattrvaluecount("Documentation");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.DocText value[]=new ch.interlis.models.IlisMeta16.ModelData.DocText[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta16.ModelData.DocText)getattrobj("Documentation",i);
    }
    return value;
  }
  public void addDocumentation(ch.interlis.models.IlisMeta16.ModelData.DocText value) {
    addattrobj("Documentation", value);
  }
  public final static String tag_ElementInPackage="ElementInPackage";
  public String getElementInPackage() {
    ch.interlis.iom.IomObject value=getattrobj("ElementInPackage",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setElementInPackage(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("ElementInPackage",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("ElementInPackage","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("ElementInPackage",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
