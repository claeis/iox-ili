package ch.interlis.models.IlisMeta16.ModelTranslation;
public class METranslation extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelTranslation.METranslation";
  public METranslation() {
    super(tag,null);
  }
  protected METranslation(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Of="Of";
  public String getOf() {
    ch.interlis.iom.IomObject value=getattrobj("Of",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setOf(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Of","REF");
    structvalue.setobjectrefoid(oid);
  }
  public final static String tag_TranslatedName="TranslatedName";
  public String getTranslatedName() {
    if(getattrvaluecount("TranslatedName")==0)return null;
    String value=getattrvalue("TranslatedName");
    return value;
  }
  public void setTranslatedName(String value) {
    if(value==null){setattrundefined("TranslatedName");return;}
    setattrvalue("TranslatedName", value);
  }
  public final static String tag_TranslatedDoc="TranslatedDoc";
  public int sizeTranslatedDoc() {return getattrvaluecount("TranslatedDoc");}
  public ch.interlis.models.IlisMeta16.ModelTranslation.DocTextTranslation[] getTranslatedDoc() {
    int size=getattrvaluecount("TranslatedDoc");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelTranslation.DocTextTranslation value[]=new ch.interlis.models.IlisMeta16.ModelTranslation.DocTextTranslation[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta16.ModelTranslation.DocTextTranslation)getattrobj("TranslatedDoc",i);
    }
    return value;
  }
  public void addTranslatedDoc(ch.interlis.models.IlisMeta16.ModelTranslation.DocTextTranslation value) {
    addattrobj("TranslatedDoc", value);
  }
}
