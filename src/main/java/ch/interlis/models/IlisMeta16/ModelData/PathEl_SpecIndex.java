package ch.interlis.models.IlisMeta16.ModelData;
public class PathEl_SpecIndex{
  static protected java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  protected PathEl_SpecIndex(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(PathEl_SpecIndex value) {
     return value.value;
  }
  static public PathEl_SpecIndex parseXmlCode(String value) {
     return (PathEl_SpecIndex)valuev.get(value);
  }
  static public PathEl_SpecIndex First=new PathEl_SpecIndex("First");
  public final static String tag_First="First";
  static public PathEl_SpecIndex Last=new PathEl_SpecIndex("Last");
  public final static String tag_Last="Last";
}
