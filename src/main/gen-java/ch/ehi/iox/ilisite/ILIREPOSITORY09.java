package ch.ehi.iox.ilisite;
public class ILIREPOSITORY09{
  private ILIREPOSITORY09() {}
  public final static String MODEL= "IliRepository09";
  public final static String RepositoryIndex= "IliRepository09.RepositoryIndex";
  public static ch.interlis.iom_j.xtf.XtfModel getXtfModel(){ return new ch.interlis.iom_j.xtf.XtfModel("IliRepository09","mailto:ce@eisenhutinformatik.ch","2009-12-01"); }
  static public ch.interlis.iox.IoxFactory getIoxFactory()
  {
    return new ch.interlis.iox.IoxFactory(){
      public ch.interlis.iom.IomObject createIomObject(String type,String oid) throws ch.interlis.iox.IoxException {
      if(type.equals("IliRepository09.WebService_"))return new ch.ehi.iox.ilisite.IliRepository09.WebService_();
      if(type.equals("IliRepository09.WebSite_"))return new ch.ehi.iox.ilisite.IliRepository09.WebSite_();
      if(type.equals("IliRepository09.RepositoryIndex.ModelMetadata"))return new ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata(oid);
      if(type.equals("IliRepository09.ModelName_"))return new ch.ehi.iox.ilisite.IliRepository09.ModelName_();
      return null;
      }
    };
  }
  static public ch.interlis.iom_j.ViewableProperties getIoxMapping()
  {
    ch.interlis.iom_j.ViewableProperties mapping=new ch.interlis.iom_j.ViewableProperties();
    mapping.defineClass("IliRepository09.WebService_", new String[]{   "value"
      });
    mapping.defineClass("IliRepository09.WebSite_", new String[]{   "value"
      });
    mapping.defineClass("IliRepository09.RepositoryIndex.ModelMetadata", new String[]{   "Name"
      ,"SchemaLanguage"
      ,"File"
      ,"Version"
      ,"VersionComment"
      ,"publishingDate"
      ,"Original"
      ,"dependsOnModel"
      ,"precursorVersion"
      ,"followupModel"
      ,"derivedModel"
      ,"Title"
      ,"shortDescription"
      ,"Tags"
      ,"Issuer"
      ,"technicalContact"
      ,"furtherInformation"
      ,"furtherMetadata"
      ,"knownWMS"
      ,"knownWFS"
      ,"knownPortal"
      ,"browseOnly"
      ,"md5"
      });
    mapping.defineClass("IliRepository09.ModelName_", new String[]{   "value"
      });
    return mapping;
  }
}
