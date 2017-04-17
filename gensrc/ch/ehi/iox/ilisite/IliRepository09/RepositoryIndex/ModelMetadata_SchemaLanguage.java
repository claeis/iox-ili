package ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex;
public class ModelMetadata_SchemaLanguage{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private ModelMetadata_SchemaLanguage(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(ModelMetadata_SchemaLanguage value) {
     return value.value;
  }
  static public ModelMetadata_SchemaLanguage parseXmlCode(String value) {
     return (ModelMetadata_SchemaLanguage)valuev.get(value);
  }
  static public ModelMetadata_SchemaLanguage ili1=new ModelMetadata_SchemaLanguage("ili1");
  static public ModelMetadata_SchemaLanguage ili2_2=new ModelMetadata_SchemaLanguage("ili2_2");
  static public ModelMetadata_SchemaLanguage ili2_3=new ModelMetadata_SchemaLanguage("ili2_3");
}
