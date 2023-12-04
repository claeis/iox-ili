package ch.interlis.models.IlisMeta16.ModelData;
public class AxisSpec extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.AxisSpec";
  public AxisSpec() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
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
  public final static String tag_Axis="Axis";
  public String getAxis() {
    ch.interlis.iom.IomObject value=getattrobj("Axis",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setAxis(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Axis",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Axis","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Axis",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
    return oldoid;
  }
}
