package ch.interlis.iox_j.validator;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.AssociationDef;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.Cardinality;
import ch.interlis.ili2c.metamodel.CompositionType;
import ch.interlis.ili2c.metamodel.CoordType;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.NumericType;
import ch.interlis.ili2c.metamodel.ObjectType;
import ch.interlis.ili2c.metamodel.PolylineType;
import ch.interlis.ili2c.metamodel.ReferenceType;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.SurfaceOrAreaType;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Type;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.ili2c.metamodel.ViewableTransferElement;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxLogging;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox_j.IoxInvalidDataException;
import ch.interlis.iox_j.logging.LogEventFactory;


public class Validator implements ch.interlis.iox.IoxValidator {
	private ch.interlis.iox.IoxValidationConfig validationConfig=null;
	private IoxLogging errs=null;
	private LogEventFactory errFact=null;
	private TransferDescription td=null;
	private boolean doItfLineTables=false;
	private Settings config=null;

	public Validator(TransferDescription td, IoxValidationConfig validationConfig,
			IoxLogging errs, LogEventFactory errFact, Settings config) {
		super();
		this.td = td;
		this.validationConfig = validationConfig;
		this.errs = errs;
		this.errFact = errFact;
		this.config=config;
		this.doItfLineTables = false;
		if(doItfLineTables){
			tag2class=ch.interlis.iom_j.itf.ModelUtilities.getTagMap(td);
		}else{
			tag2class=ch.interlis.ili2c.generator.XSDGenerator.getTagMap(td);
		}
		unknownTypev=new HashSet<String>();
	}

	/** mappings from xml-tags to Viewable|AttributeDef
	 */
	private HashMap<String,Object> tag2class=null;
	/** list of seen but unknown types; maintained to prevent duplicate error messages
	 */
	private HashSet<String> unknownTypev=null;
	
	@Override
	public void close() {
	}

	@Override
	public ch.interlis.iox.IoxValidationDataPool getDataPool() {
		return null;
	}

	@Override
	public IoxLogging getLoggingHandler() {
		return errs;
	}
	@Override
	public void setLoggingHandler(IoxLogging handler) {
		errs=handler;
	}

	@Override
	public void validate(ch.interlis.iox.IoxEvent event) {
		if(event instanceof ch.interlis.iox.ObjectEvent){
			IomObject iomObj=((ch.interlis.iox.ObjectEvent)event).getIomObject();
			checkObject(iomObj,null);
		}
		
	}

	private void checkObject(IomObject iomObj,String attrPath) {
		boolean isObject= attrPath==null;
		if(isObject){
			errFact.setDataObj(iomObj);
		}
		String tag=iomObj.getobjecttag();
		//EhiLogger.debug("tag "+tag);
		Object modelele=tag2class.get(tag);
		if(modelele==null){
			if(!unknownTypev.contains(tag)){
				errs.addEvent(errFact.logErrorMsg("unknown type <{0}>",tag));
			}
			return;
		}
		if(isObject){
			// is it a SURFACE or AREA line table?
			if(doItfLineTables && modelele instanceof AttributeDef){
				checkItfLineTableObject(iomObj,(AttributeDef)modelele);
				return;
			}
		}
		// ASSERT: an ordinary class/table
		Viewable aclass1=(Viewable)modelele;		
		Iterator iter = aclass1.getAttributesAndRoles2();
		while (iter.hasNext()) {
			ViewableTransferElement obj = (ViewableTransferElement)iter.next();
			if (obj.obj instanceof AttributeDef) {
				AttributeDef attr = (AttributeDef) obj.obj;
				if(!attr.isTransient()){
					Type proxyType=attr.getDomain();
					if(proxyType!=null && (proxyType instanceof ObjectType)){
						// skip implicit particles (base-viewables) of views
					}else{
						checkAttrValue(iomObj,attr,null);
					}
				}
			}
			if (isObject && obj.obj instanceof RoleDef) {
				RoleDef role = (RoleDef) obj.obj;
				if (role.getExtending() == null) {

						String refoid = null;
						String roleName = role.getName();
						// a role of an embedded association?
						if (obj.embedded) {
							AssociationDef roleOwner = (AssociationDef) role
									.getContainer();
							if (roleOwner.getDerivedFrom() == null) {
								// not just a link?
								IomObject structvalue = iomObj.getattrobj(
										roleName, 0);
								if (roleOwner.getAttributes().hasNext()
										|| roleOwner
												.getLightweightAssociations()
												.iterator().hasNext()) {
									// TODO handle attributes of link
								}
								if (structvalue != null) {
									refoid = structvalue.getobjectrefoid();
									long orderPos = structvalue
											.getobjectreforderpos();
									if (orderPos != 0) {
										// refoid,orderPos
										// ret.setStringAttribute(roleName,
										// refoid);
										// ret.setStringAttribute(roleName+".orderPos",
										// Long.toString(orderPos));
									} else {
										// refoid
										// ret.setStringAttribute(roleName,
										// refoid);
									}
								} else {
									refoid = null;
								}
							}
						} else {
							IomObject structvalue = iomObj.getattrobj(
									roleName, 0);
							refoid = structvalue.getobjectrefoid();
							long orderPos = structvalue
									.getobjectreforderpos();
							if (orderPos != 0) {
								// refoid,orderPos
								// ret.setStringAttribute(roleName, refoid);
								// ret.setStringAttribute(roleName+".orderPos",
								// Long.toString(orderPos));
							} else {
								// refoid
								// ret.setStringAttribute(roleName, refoid);
							}
						}
						String targetClass = null;
						if (refoid != null) {
							// TODO check target opbject
						}
				}
			 }
		}
	}

	private void checkAttrValue(IomObject iomObj, AttributeDef attr,String attrPath) {
		 String attrName=attr.getName();
		 String attrQName=attr.getContainer().getScopedName(null)+"."+attrName;
		 if(attrPath==null){
			 attrPath=attrName;
		 }else{
			 attrPath=attrPath+"/"+attrName;
		 }
		 
			Type type = attr.getDomainResolvingAliases();
			if (type instanceof CompositionType){
				 int structc=iomObj.getattrvaluecount(attrName);
				 Cardinality card = ((CompositionType)type).getCardinality();
				 if(structc<card.getMinimum() || structc>card.getMaximum()){
					 errs.addEvent(errFact.logErrorMsg("Attribute {0} has wrong number of values", attrPath));
				 }
				 for(int structi=0;structi<structc;structi++){
					 IomObject structEle=iomObj.getattrobj(attrName, structi);
					 checkObject(structEle, attrPath+"["+structi+"]");
				 }
			}else{
				 int structc=iomObj.getattrvaluecount(attrName);
				 if(type.isMandatory() && structc==0){
					 String param=validationConfig.getConfigValue(attrQName, ValidationConfig.MULTIPLICITY);
					 if(ValidationConfig.WARNING.equals(param)){
						 errs.addEvent(errFact.logWarningMsg("Attribute {0} requires a value", attrPath));
					 }else if(ValidationConfig.OFF.equals(param)){
						 // skip it
					 }else{
						 errs.addEvent(errFact.logErrorMsg("Attribute {0} requires a value", attrPath));
					 }
				 }
			}
			
				//if( Ili2cUtility.isBoolean(td,attr)) {
				//}else if(Ili2cUtility.isUuidOid(td, attr)){
				//}else if( Ili2cUtility.isIli1Date(td,attr)) {
				//}else if( Ili2cUtility.isIli2Date(td,attr)) {
				//}else if( Ili2cUtility.isIli2Time(td,attr)) {
				//}else if( Ili2cUtility.isIli2DateTime(td,attr)) {
				//}else{
					//Type type = attr.getDomainResolvingAliases();
					//if (type instanceof CompositionType){
					//}else if (type instanceof PolylineType){
					//}else if(type instanceof SurfaceOrAreaType){
					//}else if(type instanceof CoordType){
					//}else if(type instanceof NumericType){
					//}else if(type instanceof EnumerationType){
					//}else if(type instanceof ReferenceType){
					//}else{
					//}
				//}
		
	}

	private void checkItfLineTableObject(IomObject iomObj, AttributeDef modelele) {
		// TODO Auto-generated method stub
		
	}


}
