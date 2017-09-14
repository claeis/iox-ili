package ch.interlis.iox_j.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.CompositionType;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.Enumeration;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.ObjectType;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.SurfaceOrAreaType;
import ch.interlis.ili2c.metamodel.SurfaceType;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Type;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.ili2c.metamodel.ViewableTransferElement;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.itf.ModelUtilities;
import ch.interlis.iox.EndBasketEvent;
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxLogging;
import ch.interlis.iox.IoxValidationDataPool;
import ch.interlis.iox.ObjectEvent;
import ch.interlis.iox.StartBasketEvent;
import ch.interlis.iox.StartTransferEvent;
import ch.interlis.iox_j.validator.Validator;

public class TranslateToOrigin implements IoxFilter {

	private IoxLogging loggingHandler=null;
	private TransferDescription td=null;
	private boolean doItfLineTables=false;
	/** mappings from xml-tags to Viewable|AttributeDef
	 */
	private HashMap<String,Object> tag2class=null;
	
	public TranslateToOrigin(TransferDescription td,Settings config)
	{
		this.td=td;
		this.doItfLineTables = Validator.CONFIG_DO_ITF_LINETABLES_DO.equals(config.getValue(Validator.CONFIG_DO_ITF_LINETABLES));
		if(doItfLineTables){
			tag2class=ch.interlis.iom_j.itf.ModelUtilities.getTagMap(td);
		}else{
			tag2class=ch.interlis.ili2c.generator.XSDGenerator.getTagMap(td);
		}
		
	}
	
	@Override
	public IoxEvent filter(IoxEvent event) throws IoxException {
		 if(event instanceof StartTransferEvent){
		}else if(event instanceof StartBasketEvent){
		}else if(event instanceof ObjectEvent){
			translateObject(((ObjectEvent) event).getIomObject());
		}else if(event instanceof EndBasketEvent){
		}else if(event instanceof EndTransferEvent){
		}
		return event;
	}

	private void translateObject(IomObject iomObj) {
		Element modelElement = (Element)tag2class.get(iomObj.getobjecttag());
		Element destModelEle=modelElement.getTranslationOfOrSame();
		if(destModelEle==modelElement){
			// no translation required
			return;
		}
		// is it a SURFACE or AREA line table?
		if(doItfLineTables && modelElement instanceof AttributeDef){
			AttributeDef attr=(AttributeDef)modelElement;
			AttributeDef destAttr=(AttributeDef)destModelEle;
			Element table=destAttr.getContainer();
			String destName=table.getScopedName()+"_"+destAttr.getName();
			iomObj.setobjecttag(destName);
			SurfaceOrAreaType saType=(SurfaceOrAreaType)attr.getDomainResolvingAliases();
			if(saType instanceof SurfaceType){
				String maintableref=ModelUtilities.getHelperTableMainTableRef(attr);
				String destMaintableref=ModelUtilities.getHelperTableMainTableRef(destAttr);
				if(iomObj.getattrvaluecount(maintableref)>0){
					IomObject structvalue = iomObj.getattrobj(maintableref, 0);
					iomObj.deleteattrobj(maintableref, 0);
					iomObj.addattrobj(destMaintableref, structvalue);
				}
			}
			// handle geom attr
			String geom=ModelUtilities.getHelperTableGeomAttrName(attr);
			String destGeom=ModelUtilities.getHelperTableGeomAttrName(destAttr);
			if(iomObj.getattrvaluecount(geom)>0){
				IomObject structvalue = iomObj.getattrobj(geom, 0);
				iomObj.deleteattrobj(geom, 0);
				iomObj.addattrobj(destGeom, structvalue);
			}
			Table lineAttrTable=saType.getLineAttributeStructure();
			if(lineAttrTable!=null){
				ModelUtilities.getIli1AttrList(lineAttrTable);
				// TODO handle line attrs
			}
		}else{
			Viewable aclass=(Viewable)modelElement;
			Viewable destClass=(Viewable)destModelEle;
			String destName=destClass.getScopedName();
			iomObj.setobjecttag(destName);
			
			// handle attrs
			Iterator iter = aclass.getAttributesAndRoles2();
			while (iter.hasNext()) {
				ViewableTransferElement obj = (ViewableTransferElement)iter.next();
				if (obj.obj instanceof AttributeDef) {
					AttributeDef attr = (AttributeDef) obj.obj;
					if(!attr.isTransient()){
						Type proxyType=attr.getDomain();
						if(proxyType!=null && (proxyType instanceof ObjectType)){
							// skip implicit particles (base-viewables) of views
						}else{
							translateAttrValue(iomObj,attr);
						}
					}
				}
				if (obj.obj instanceof RoleDef) {
					RoleDef role = (RoleDef) obj.obj;
					RoleDef destRole=(RoleDef)role.getTranslationOfOrSame();
					String roleName = role.getName();
					if(iomObj.getattrvaluecount(roleName)>0){
						IomObject structvalue = iomObj.getattrobj(roleName, 0);
						iomObj.deleteattrobj(roleName, 0);
						iomObj.addattrobj(destRole.getName(), structvalue);
					}
				}
			}
			
		}
	}

	private void translateAttrValue(IomObject iomObj, AttributeDef attr) {
		String attrName=attr.getName();
		int attrc=iomObj.getattrvaluecount(attrName);
		if(attrc==0){
			return;
		}
		boolean isCompType=attr.getDomain() instanceof CompositionType ? true :false;
		boolean isEnumType=attr.getDomainResolvingAliases() instanceof EnumerationType ? true : false;
		EnumerationType enumType=null;
		if(isEnumType){
			enumType=(EnumerationType)attr.getDomainResolvingAliases();
		}
		AttributeDef destAttr=(AttributeDef)attr.getTranslationOfOrSame();
		String destAttrName=destAttr.getName();
		ArrayList<Object> attrValues=new ArrayList<Object>();
		for(int attri=0;attri<attrc;attri++){
			String attrValue=iomObj.getattrprim(attrName,attri);
			if(attrValue!=null){
				attrValues.add(attrValue);
			}else{
				IomObject structValue=iomObj.getattrobj(attrName,attri);
				attrValues.add(structValue);
			}
		}
		iomObj.setattrundefined(attrName);
		for(int attri=0;attri<attrc;attri++){
			Object attrValue=attrValues.get(attri);
			if(attrValue!=null){
				if(attrValue instanceof String){
					if(isEnumType){
						attrValue=translateEnumValue((String)attrValue,enumType,(EnumerationType)destAttr.getDomainResolvingAliases());
					}
					iomObj.setattrvalue(destAttrName, (String)attrValue);
				}else{
					IomObject structValue=(IomObject)attrValue;
					if(isCompType){
						translateObject(structValue);
					}
					iomObj.addattrobj(destAttrName,structValue);
				}
			}
		}
	}

	private String translateEnumValue(String attrValue, EnumerationType enumType,EnumerationType destEnumType) {
		Map<String,String> src2dest=getEnumMapping(enumType,destEnumType);
		String destValue=src2dest.get(attrValue);
		return destValue;
	}


	Map<EnumerationType,Map<String,String>> src2destEles=new HashMap<EnumerationType,Map<String,String>>();
	private Map<String, String> getEnumMapping(
			EnumerationType enumType,EnumerationType destEnumType) {
		Map<String,String> src2dest=src2destEles.get(enumType);
		if(src2dest==null){
			Enumeration eles=enumType.getConsolidatedEnumeration();
			src2dest=new HashMap<String,String>(); 
			List<String> srcVals=enumType.getValues();
			List<String> destVals=destEnumType.getValues();
			for(int i=0;i<srcVals.size();i++){
				src2dest.put(srcVals.get(i),destVals.get(i));
			}
			src2destEles.put(enumType,src2dest);
		}
		return src2dest;
	}
	

	@Override
	public void close() {
		loggingHandler=null;
	}

	@Override
	public IoxLogging getLoggingHandler() {
		return loggingHandler;
	}

	@Override
	public void setLoggingHandler(IoxLogging errs) {
		loggingHandler=errs;
	}

	@Override
	public IoxValidationDataPool getDataPool() {
		return null;
	}
}
