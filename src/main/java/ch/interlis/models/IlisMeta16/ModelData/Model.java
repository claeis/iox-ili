package ch.interlis.models.IlisMeta16.ModelData;
public class Model extends ch.interlis.models.IlisMeta16.ModelData.Package
{
  public final static String tag= "IlisMeta16.ModelData.Model";
  public Model(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_iliVersion="iliVersion";
  /** MetaElement.Name := ModelName as defined in the INTERLIS-Model
   */
  public String getiliVersion() {
    String value=getattrvalue("iliVersion");
    return value;
  }
  /** MetaElement.Name := ModelName as defined in the INTERLIS-Model
   */
  public void setiliVersion(String value) {
    setattrvalue("iliVersion", value);
  }
  public final static String tag_Contracted="Contracted";
  public Boolean getContracted() {
    if(getattrvaluecount("Contracted")==0)return null;
    String value=getattrvalue("Contracted");
    return value!=null && value.equals("true");
  }
  public void setContracted(Boolean value) {
    if(value==null){setattrundefined("Contracted");return;}
    setattrvalue("Contracted", value?"true":"false");
  }
  public final static String tag_Kind="Kind";
  public Model_Kind getKind() {
    String value=getattrvalue("Kind");
    return Model_Kind.parseXmlCode(value);
  }
  public void setKind(Model_Kind value) {
    setattrvalue("Kind", Model_Kind.toXmlCode(value));
  }
  public final static String tag_Language="Language";
  public String getLanguage() {
    if(getattrvaluecount("Language")==0)return null;
    String value=getattrvalue("Language");
    return value;
  }
  public void setLanguage(String value) {
    if(value==null){setattrundefined("Language");return;}
    setattrvalue("Language", value);
  }
  public final static String tag_At="At";
  public String getAt() {
    if(getattrvaluecount("At")==0)return null;
    String value=getattrvalue("At");
    return value;
  }
  public void setAt(String value) {
    if(value==null){setattrundefined("At");return;}
    setattrvalue("At", value);
  }
  public final static String tag_Version="Version";
  public String getVersion() {
    if(getattrvaluecount("Version")==0)return null;
    String value=getattrvalue("Version");
    return value;
  }
  public void setVersion(String value) {
    if(value==null){setattrundefined("Version");return;}
    setattrvalue("Version", value);
  }
  public final static String tag_NoIncrementalTransfer="NoIncrementalTransfer";
  public Boolean getNoIncrementalTransfer() {
    if(getattrvaluecount("NoIncrementalTransfer")==0)return null;
    String value=getattrvalue("NoIncrementalTransfer");
    return value!=null && value.equals("true");
  }
  public void setNoIncrementalTransfer(Boolean value) {
    if(value==null){setattrundefined("NoIncrementalTransfer");return;}
    setattrvalue("NoIncrementalTransfer", value?"true":"false");
  }
  public final static String tag_CharSetIANAName="CharSetIANAName";
  public String getCharSetIANAName() {
    if(getattrvaluecount("CharSetIANAName")==0)return null;
    String value=getattrvalue("CharSetIANAName");
    return value;
  }
  public void setCharSetIANAName(String value) {
    if(value==null){setattrundefined("CharSetIANAName");return;}
    setattrvalue("CharSetIANAName", value);
  }
  public final static String tag_xmlns="xmlns";
  public String getxmlns() {
    if(getattrvaluecount("xmlns")==0)return null;
    String value=getattrvalue("xmlns");
    return value;
  }
  public void setxmlns(String value) {
    if(value==null){setattrundefined("xmlns");return;}
    setattrvalue("xmlns", value);
  }
  public final static String tag_ili1Transfername="ili1Transfername";
  public String getili1Transfername() {
    if(getattrvaluecount("ili1Transfername")==0)return null;
    String value=getattrvalue("ili1Transfername");
    return value;
  }
  public void setili1Transfername(String value) {
    if(value==null){setattrundefined("ili1Transfername");return;}
    setattrvalue("ili1Transfername", value);
  }
  public final static String tag_ili1Format="ili1Format";
  public int sizeili1Format() {return getattrvaluecount("ili1Format");}
  public ch.interlis.models.IlisMeta16.ModelData.Ili1Format getili1Format() {
    int size=getattrvaluecount("ili1Format");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.Ili1Format value=(ch.interlis.models.IlisMeta16.ModelData.Ili1Format)getattrobj("ili1Format",0);
    return value;
  }
  public void setili1Format(ch.interlis.models.IlisMeta16.ModelData.Ili1Format value) {
    if(getattrvaluecount("ili1Format")>0){
      changeattrobj("ili1Format",0, value);
    }else{
      addattrobj("ili1Format", value);
    }
  }
}
