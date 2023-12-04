package ch.interlis.models.IlisMeta16.ModelData;
public class LineType extends ch.interlis.models.IlisMeta16.ModelData.DomainType
{
  public final static String tag= "IlisMeta16.ModelData.LineType";
  public LineType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Kind="Kind";
  public LineType_Kind getKind() {
    String value=getattrvalue("Kind");
    return LineType_Kind.parseXmlCode(value);
  }
  public void setKind(LineType_Kind value) {
    setattrvalue("Kind", LineType_Kind.toXmlCode(value));
  }
  public final static String tag_MaxOverlap="MaxOverlap";
  public String getMaxOverlap() {
    if(getattrvaluecount("MaxOverlap")==0)return null;
    String value=getattrvalue("MaxOverlap");
    return value;
  }
  public void setMaxOverlap(String value) {
    if(value==null){setattrundefined("MaxOverlap");return;}
    setattrvalue("MaxOverlap", value);
  }
  public final static String tag_Multi="Multi";
  public Boolean getMulti() {
    if(getattrvaluecount("Multi")==0)return null;
    String value=getattrvalue("Multi");
    return value!=null && value.equals("true");
  }
  public void setMulti(Boolean value) {
    if(value==null){setattrundefined("Multi");return;}
    setattrvalue("Multi", value?"true":"false");
  }
  public final static String tag_CoordType="CoordType";
  public String getCoordType() {
    ch.interlis.iom.IomObject value=getattrobj("CoordType",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setCoordType(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("CoordType",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("CoordType","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("CoordType",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
  public final static String tag_LAStructure="LAStructure";
  public String getLAStructure() {
    ch.interlis.iom.IomObject value=getattrobj("LAStructure",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setLAStructure(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("LAStructure",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("LAStructure","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("LAStructure",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
