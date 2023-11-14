package ch.interlis.models.IlisMeta16.ModelData;
public class AttrOrParam extends ch.interlis.models.IlisMeta16.ModelData.ExtendableME
{
  public final static String tag= "IlisMeta16.ModelData.AttrOrParam";
  public AttrOrParam(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_SubdivisionKind="SubdivisionKind";
  /** MetaElement.Name := AttributeName, ParameterName
   * as defined in the INTERLIS-Model
   */
  public AttrOrParam_SubdivisionKind getSubdivisionKind() {
    if(getattrvaluecount("SubdivisionKind")==0)return null;
    String value=getattrvalue("SubdivisionKind");
    return AttrOrParam_SubdivisionKind.parseXmlCode(value);
  }
  /** MetaElement.Name := AttributeName, ParameterName
   * as defined in the INTERLIS-Model
   */
  public void setSubdivisionKind(AttrOrParam_SubdivisionKind value) {
    if(value==null){setattrundefined("SubdivisionKind");return;}
    setattrvalue("SubdivisionKind", AttrOrParam_SubdivisionKind.toXmlCode(value));
  }
  public final static String tag_Transient="Transient";
  public Boolean getTransient() {
    if(getattrvaluecount("Transient")==0)return null;
    String value=getattrvalue("Transient");
    return value!=null && value.equals("true");
  }
  public void setTransient(Boolean value) {
    if(value==null){setattrundefined("Transient");return;}
    setattrvalue("Transient", value?"true":"false");
  }
  public final static String tag_Derivates="Derivates";
  public int sizeDerivates() {return getattrvaluecount("Derivates");}
  public ch.interlis.models.IlisMeta16.ModelData.Expression[] getDerivates() {
    int size=getattrvaluecount("Derivates");
    if(size==0)return null;
    ch.interlis.models.IlisMeta16.ModelData.Expression value[]=new ch.interlis.models.IlisMeta16.ModelData.Expression[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta16.ModelData.Expression)getattrobj("Derivates",i);
    }
    return value;
  }
  public void addDerivates(ch.interlis.models.IlisMeta16.ModelData.Expression value) {
    addattrobj("Derivates", value);
  }
  public final static String tag_AttrParent="AttrParent";
  public String getAttrParent() {
    ch.interlis.iom.IomObject value=getattrobj("AttrParent",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setAttrParent(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=getattrobj("AttrParent",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("AttrParent","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("AttrParent",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
    return oldoid;
  }
  public final static String tag_ParamParent="ParamParent";
  public String getParamParent() {
    ch.interlis.iom.IomObject value=getattrobj("ParamParent",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setParamParent(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=getattrobj("ParamParent",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("ParamParent","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("ParamParent",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
    return oldoid;
  }
  public final static String tag_Type="Type";
  public String getType() {
    ch.interlis.iom.IomObject value=getattrobj("Type",0);
    if(value==null)return null;
    String oid=value.getobjectrefoid();
    if(oid==null)return null;
    return oid;
  }
  public String setType(String oid) {
    ch.interlis.iom.IomObject structvalue=getattrobj("Type",0);
    if(structvalue==null){
      if(oid==null)return null;
      structvalue=addattrobj("Type","REF");
    }else{
      if(oid==null){
        String oldoid=structvalue.getobjectrefoid();
        deleteattrobj("Type",0);
        return oldoid;
      }
    }
    String oldoid=structvalue.getobjectrefoid();
    structvalue.setobjectrefoid(oid);
    return oldoid;
  }
}
