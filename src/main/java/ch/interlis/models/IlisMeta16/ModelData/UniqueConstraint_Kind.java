package ch.interlis.models.IlisMeta16.ModelData;
public class UniqueConstraint_Kind{
  static protected java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  protected UniqueConstraint_Kind(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(UniqueConstraint_Kind value) {
     return value.value;
  }
  static public UniqueConstraint_Kind parseXmlCode(String value) {
     return (UniqueConstraint_Kind)valuev.get(value);
  }
  static public UniqueConstraint_Kind GlobalU=new UniqueConstraint_Kind("GlobalU");
  public final static String tag_GlobalU="GlobalU";
  static public UniqueConstraint_Kind BasketU=new UniqueConstraint_Kind("BasketU");
  public final static String tag_BasketU="BasketU";
  static public UniqueConstraint_Kind LocalU=new UniqueConstraint_Kind("LocalU");
  public final static String tag_LocalU="LocalU";
}
