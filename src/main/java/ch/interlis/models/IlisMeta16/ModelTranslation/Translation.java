package ch.interlis.models.IlisMeta16.ModelTranslation;
public class Translation extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelTranslation.Translation";
  public Translation(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Language="Language";
  public String getLanguage() {
    String value=getattrvalue("Language");
    return value;
  }
  public void setLanguage(String value) {
    setattrvalue("Language", value);
  }
  public final static String tag_Translations="Translations";
  public int sizeTranslations() {return getattrvaluecount("Translations");}
  public ch.interlis.models.IlisMeta16.ModelTranslation.METranslation[] getTranslations() {
    int size=getattrvaluecount("Translations");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelTranslation.METranslation value[]=new ch.interlis.models.IlisMeta16.ModelTranslation.METranslation[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta16.ModelTranslation.METranslation)getattrobj("Translations",i);
    }
    return value;
  }
  public void addTranslations(ch.interlis.models.IlisMeta16.ModelTranslation.METranslation value) {
    addattrobj("Translations", value);
  }
}
