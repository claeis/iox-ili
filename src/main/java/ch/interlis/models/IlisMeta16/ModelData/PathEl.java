package ch.interlis.models.IlisMeta16.ModelData;
public class PathEl extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.PathEl";
  public PathEl() {
    super(tag,null);
  }
  protected PathEl(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Kind="Kind";
  public PathEl_Kind getKind() {
    if(getattrvaluecount("Kind")==0)return null;
    String value=getattrvalue("Kind");
    return PathEl_Kind.parseXmlCode(value);
  }
  public void setKind(PathEl_Kind value) {
    if(value==null){setattrundefined("Kind");return;}
    setattrvalue("Kind", PathEl_Kind.toXmlCode(value));
  }
  public final static String tag_Ref="Ref";
  public String getRef() {
    ch.interlis.iom.IomObject value=getattrobj("Ref",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setRef(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Ref","REF");
    structvalue.setobjectrefoid(oid);
  }
  public final static String tag_NumIndex="NumIndex";
  public Integer getNumIndex() {
    if(getattrvaluecount("NumIndex")==0)return null;
    String value=getattrvalue("NumIndex");
    return Integer.parseInt(value);
  }
  public void setNumIndex(Integer value) {
    if(value==null){setattrundefined("NumIndex");return;}
    setattrvalue("NumIndex", Integer.toString(value));
  }
  public final static String tag_SpecIndex="SpecIndex";
  public PathEl_SpecIndex getSpecIndex() {
    if(getattrvaluecount("SpecIndex")==0)return null;
    String value=getattrvalue("SpecIndex");
    return PathEl_SpecIndex.parseXmlCode(value);
  }
  public void setSpecIndex(PathEl_SpecIndex value) {
    if(value==null){setattrundefined("SpecIndex");return;}
    setattrvalue("SpecIndex", PathEl_SpecIndex.toXmlCode(value));
  }
}
