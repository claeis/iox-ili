package ch.ehi.iox.ilisite.IliSite09.SiteMetadata;
public class Site extends ch.interlis.iom_j.Iom_jObject
{
  private final static String tag= "IliSite09.SiteMetadata.Site";
  public Site(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  /** Name of site, e.g. "swisstopo"
   */
  public String getName() {
    String value=getattrvalue("Name");
    return value;
  }
  /** Name of site, e.g. "swisstopo"
   */
  public void setName(String value) {
    setattrvalue("Name", value);
  }
  /** Title of site, e.g. "Geoinformationszentrum des Bundes"
   */
  public String getTitle() {
    String value=getattrvalue("Title");
    return value;
  }
  /** Title of site, e.g. "Geoinformationszentrum des Bundes"
   */
  public void setTitle(String value) {
    setattrvalue("Title", value);
  }
  /** Short description of site.
   */
  public String getshortDescription() {
    String value=getattrvalue("shortDescription");
    return value;
  }
  /** Short description of site.
   */
  public void setshortDescription(String value) {
    setattrvalue("shortDescription", value);
  }
  /** Comma seperated list of terms associated with this site
   */
  public String getTags() {
    String value=getattrvalue("Tags");
    return value;
  }
  /** Comma seperated list of terms associated with this site
   */
  public void setTags(String value) {
    setattrvalue("Tags", value);
  }
  /** Who owns this site?
   */
  public String getOwner() {
    String value=getattrvalue("Owner");
    return value;
  }
  /** Who owns this site?
   */
  public void setOwner(String value) {
    setattrvalue("Owner", value);
  }
  /** Who should be adressed with technical questions e.g. "mailto:infovd@swisstopo.ch"
   */
  public String gettechnicalContact() {
    String value=getattrvalue("technicalContact");
    return value;
  }
  /** Who should be adressed with technical questions e.g. "mailto:infovd@swisstopo.ch"
   */
  public void settechnicalContact(String value) {
    setattrvalue("technicalContact", value);
  }
  /** Reference to document or website with further information.
   */
  public String getfurtherInformation() {
    String value=getattrvalue("furtherInformation");
    return value;
  }
  /** Reference to document or website with further information.
   */
  public void setfurtherInformation(String value) {
    setattrvalue("furtherInformation", value);
  }
  /** Reference to machine readable data with further information about the repository.
   */
  public String getfurtherMetadata() {
    String value=getattrvalue("furtherMetadata");
    return value;
  }
  /** Reference to machine readable data with further information about the repository.
   */
  public void setfurtherMetadata(String value) {
    setattrvalue("furtherMetadata", value);
  }
  /** More general sites. Should be included during default model searches.
   */
  public int sizeparentSite() {return getattrvaluecount("parentSite");}
  public ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_[] getparentSite() {
    int size=getattrvaluecount("parentSite");
    ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_ value[]=new ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_)getattrobj("parentSite",i);
    }
    return value;
  }
  /** More general sites. Should be included during default model searches.
   */
  public void addparentSite(ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_ value) {
    addattrobj("parentSite", value);
  }
  /** More specific sites.  Should be included during default model searches.
   */
  public int sizesubsidiarySite() {return getattrvaluecount("subsidiarySite");}
  public ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_[] getsubsidiarySite() {
    int size=getattrvaluecount("subsidiarySite");
    ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_ value[]=new ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_)getattrobj("subsidiarySite",i);
    }
    return value;
  }
  /** More specific sites.  Should be included during default model searches.
   */
  public void addsubsidiarySite(ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_ value) {
    addattrobj("subsidiarySite", value);
  }
  /** Other similar sites (at the same organisational level).  Should be included during default model searches.
   */
  public int sizepeerSite() {return getattrvaluecount("peerSite");}
  public ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_[] getpeerSite() {
    int size=getattrvaluecount("peerSite");
    ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_ value[]=new ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_)getattrobj("peerSite",i);
    }
    return value;
  }
  /** Other similar sites (at the same organisational level).  Should be included during default model searches.
   */
  public void addpeerSite(ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_ value) {
    addattrobj("peerSite", value);
  }
  /** Any other known sites. Should be ignored during default model searches.
   */
  public int sizeknownOtherSite() {return getattrvaluecount("knownOtherSite");}
  public ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_[] getknownOtherSite() {
    int size=getattrvaluecount("knownOtherSite");
    ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_ value[]=new ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_)getattrobj("knownOtherSite",i);
    }
    return value;
  }
  /** Any other known sites. Should be ignored during default model searches.
   */
  public void addknownOtherSite(ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_ value) {
    addattrobj("knownOtherSite", value);
  }
  /** Any known mirror sites. Might be used if this site is offline.
   */
  public int sizemirrorSite() {return getattrvaluecount("mirrorSite");}
  public ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_[] getmirrorSite() {
    int size=getattrvaluecount("mirrorSite");
    ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_ value[]=new ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_)getattrobj("mirrorSite",i);
    }
    return value;
  }
  /** Any known mirror sites. Might be used if this site is offline.
   */
  public void addmirrorSite(ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_ value) {
    addattrobj("mirrorSite", value);
  }
}
