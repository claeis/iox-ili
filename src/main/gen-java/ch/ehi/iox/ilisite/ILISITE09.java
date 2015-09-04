package ch.ehi.iox.ilisite;
public class ILISITE09{
  private ILISITE09() {}
  public final static String MODEL= "IliSite09";
  public final static String SiteMetadata= "IliSite09.SiteMetadata";
  public static ch.interlis.iom_j.xtf.XtfModel getXtfModel(){ return new ch.interlis.iom_j.xtf.XtfModel("IliSite09","mailto:ce@eisenhutinformatik.ch","2009-11-19"); }
  static public ch.interlis.iox.IoxFactory getIoxFactory()
  {
    return new ch.interlis.iox.IoxFactory(){
      public ch.interlis.iom.IomObject createIomObject(String type,String oid) throws ch.interlis.iox.IoxException {
      if(type.equals("IliSite09.SiteMetadata.Site"))return new ch.ehi.iox.ilisite.IliSite09.SiteMetadata.Site(oid);
      if(type.equals("IliSite09.RepositoryLocation_"))return new ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_();
      return null;
      }
    };
  }
  static public ch.interlis.iom_j.ViewableProperties getIoxMapping()
  {
    ch.interlis.iom_j.ViewableProperties mapping=new ch.interlis.iom_j.ViewableProperties();
    mapping.defineClass("IliSite09.SiteMetadata.Site", new String[]{   "Name"
      ,"Title"
      ,"shortDescription"
      ,"Tags"
      ,"Owner"
      ,"technicalContact"
      ,"furtherInformation"
      ,"furtherMetadata"
      ,"parentSite"
      ,"subsidiarySite"
      ,"peerSite"
      ,"knownOtherSite"
      ,"mirrorSite"
      });
    mapping.defineClass("IliSite09.RepositoryLocation_", new String[]{   "value"
      });
    return mapping;
  }
}
