package ch.interlis.iom_j.xtf;

import ch.interlis.iom.IomObject;
import ch.interlis.iox_j.StartTransferEvent;

public class XtfStartTransferEvent extends StartTransferEvent {
	public XtfStartTransferEvent() {
	}

	public XtfStartTransferEvent(String sender) {
		super(sender);
	}

	public XtfStartTransferEvent(String sender, String comment) {
		super(sender, comment);
	}

	public XtfStartTransferEvent(String sender, String comment, String version) {
		super(sender, comment, version);
	}
	private java.util.List<OidSpace> oidspaces=new java.util.ArrayList<OidSpace>();
	public void addOidSpace(OidSpace oidspace){
		oidspaces.add(oidspace);	
	}
	public java.util.List<OidSpace> getOidSpaces(){
		return oidspaces;	
	}
	public void setOidSpaces(java.util.List<OidSpace> oidspaces){
		this.oidspaces=oidspaces;	
	}
	private java.util.HashMap<String, IomObject> headerObjects=null;
	public java.util.HashMap<String, IomObject> getHeaderObjects() {
		return headerObjects;
	}
	public void setHeaderObjects(java.util.HashMap<String, IomObject> headerObjects) {
		this.headerObjects = headerObjects;
	}
	
}
