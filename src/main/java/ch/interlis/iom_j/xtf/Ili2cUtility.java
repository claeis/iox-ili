/* This file is part of the iox-ili project.
 * For more information, please see <http://www.eisenhutinformatik.ch/iox-ili/>.
 *
 * Copyright (c) 2006 Eisenhut Informatik AG
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */
package ch.interlis.iom_j.xtf;

import java.util.ArrayList;
import java.util.Iterator;

import ch.interlis.ili2c.generator.XSD24Generator;
import ch.interlis.ili2c.metamodel.AreaType;
import ch.interlis.ili2c.metamodel.AssociationDef;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.PredefinedModel;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.ili2c.metamodel.ViewableTransferElement;
import ch.interlis.iom_j.ViewableProperties;
import ch.interlis.iom_j.ViewableProperty;
import ch.interlis.iom_j.itf.ModelUtilities;

/** Utility functions related to the INTERLIS compiler (ili2c).
 * @author ceis
 */
public class Ili2cUtility {
	private Ili2cUtility(){};
	/** Converts models as read by the compiler to the lightweight representation.
	 */
	static public ViewableProperties getIoxMappingTable(TransferDescription td)
	{
		ViewableProperties mapping=new ViewableProperties();
		if(td.getLastModel().getIliVersion().equals("2.4")){
			// map 2.4 model
			mapping.setXtf24nameMapping(XSD24Generator.createName2NameMapping(td));
			java.util.HashMap tagv=XSD24Generator.createDef2NameMapping(td);
			Iterator tagi=tagv.keySet().iterator();
			for(;tagi.hasNext();){
				Element ili2cEle=(Element)tagi.next();
				String tag=null;
				ArrayList propv=null; // ViewableProperty
				if(ili2cEle instanceof AttributeDef){
					AttributeDef attr=(AttributeDef)ili2cEle;
					tag=ili2cEle.getContainer().getScopedName(null)+"."+ili2cEle.getName();
					propv=mapLinetable(attr);
				}else if(ili2cEle instanceof Viewable){
					propv=new ArrayList(); // ViewableProperty
					Viewable v=(Viewable)ili2cEle;
					tag=v.getScopedName(null);
					Iterator iter = v.getAttributesAndRoles2();
					while (iter.hasNext()) {
						ViewableTransferElement obj = (ViewableTransferElement)iter.next();
						ViewableProperty prop=mapViewableTransferElement( v, obj);
						propv.add(prop);
					}
					
				}
				// is an object possible in the transfer file?
				if(propv!=null) {
				    // define the "class" of that object
	                mapping.defineClass(tag, (ViewableProperty[])propv.toArray(new ViewableProperty[propv.size()]));
				}
				
			}
		}else{
			java.util.HashMap tagv=ch.interlis.ili2c.generator.XSDGenerator.getTagMap(td);
			Iterator tagi=tagv.keySet().iterator();
			for(;tagi.hasNext();){
				String tag=(String)tagi.next();
				Viewable v=(Viewable)tagv.get(tag);
				Iterator iter = v.getAttributesAndRoles2();
				ArrayList propv=new ArrayList(); // ViewableProperty
				while (iter.hasNext()) {
					ViewableTransferElement obj = (ViewableTransferElement)iter.next();
					ViewableProperty prop=mapViewableTransferElement( v, obj);
					propv.add(prop);
				}
				
				mapping.defineClass(tag, (ViewableProperty[])propv.toArray(new ViewableProperty[propv.size()]));
			}
			
		}
		
		return mapping;
	}
	public static ArrayList mapLinetable(AttributeDef attr) {
		ArrayList propv=new ArrayList();
		Table linattrTab=((AreaType)attr.getDomainResolvingAliases()).getLineAttributeStructure();
		if(linattrTab!=null){
			Iterator iter = linattrTab.getAttributesAndRoles2();
			while (iter.hasNext()) {
				String propName=null;
				ViewableTransferElement obj = (ViewableTransferElement)iter.next();
				if (obj.obj instanceof AttributeDef) {
					propName = ((AttributeDef) obj.obj).getName();
				}
				if(obj.obj instanceof RoleDef){
					RoleDef role = (RoleDef) obj.obj;
					// a role of an embedded association?
					if(obj.embedded){
						AssociationDef roleOwner = (AssociationDef) role.getContainer();
						if(roleOwner.getDerivedFrom()==null){
							propName=role.getName();
						}
					}
				}
				if(propName!=null){
					ViewableProperty prop=new ViewableProperty(propName);
					propv.add(prop);
				}
			}
		}

		
		ViewableProperty prop=new ViewableProperty(ModelUtilities.getHelperTableGeomAttrName(attr));
		if(attr.isFinal()){
			prop.setTypeFinal(true);
		}
		propv.add(prop);
		return propv;
	}
	public static ViewableProperty mapViewableTransferElement(Viewable v,
			ViewableTransferElement obj) {
		ViewableProperty prop=null;
		if (obj.obj instanceof AttributeDef) {
			AttributeDef attr = (AttributeDef) obj.obj;
			prop=new ViewableProperty(attr.getName());
			ch.interlis.ili2c.metamodel.Type type=attr.getDomain();
			if(type instanceof ch.interlis.ili2c.metamodel.TypeAlias){
				ch.interlis.ili2c.metamodel.Domain domainDef=((ch.interlis.ili2c.metamodel.TypeAlias)type).getAliasing();
				if(domainDef.getType() instanceof ch.interlis.ili2c.metamodel.EnumerationType){
					prop.setEnumType(domainDef.getScopedName(null));
				}
				if(domainDef.isFinal()){
					prop.setTypeFinal(true);
				}
			}else{
				if(type instanceof ch.interlis.ili2c.metamodel.EnumerationType){
					prop.setEnumType(attr.getContainer().getScopedName(null)+":"+attr.getName());
				}						
				if(attr.isFinal()){
					prop.setTypeFinal(true);
				}
			}
			if(attr.getDomainResolvingAliases() instanceof ch.interlis.ili2c.metamodel.OIDType){
				prop.setTypeOid(true);
			}else if(attr.getDomainResolvingAliases() instanceof ch.interlis.ili2c.metamodel.BlackboxType){
				ch.interlis.ili2c.metamodel.BlackboxType bbType=(ch.interlis.ili2c.metamodel.BlackboxType)attr.getDomainResolvingAliases();
				if(bbType.getKind()==ch.interlis.ili2c.metamodel.BlackboxType.eBINARY){
					prop.setTypeBlackboxBin(true);
				}else{
					prop.setTypeBlackboxXml(true);
				}
			}
			AttributeDef baseAttr = attr;
			if(attr.getExtending()!=null){
				baseAttr = (AttributeDef)attr.getExtending();
				while(baseAttr.getExtending()!=null){
					baseAttr = (AttributeDef)baseAttr.getExtending();
				}
			}
			if(baseAttr.getContainer()!=v){
				prop.setBaseDefInClass(baseAttr.getContainer().getScopedName(null));
			}
		}
		if(obj.obj instanceof RoleDef){
			RoleDef role = (RoleDef) obj.obj;
			
			// a role of an embedded association?
			if(obj.embedded){
				AssociationDef roleOwner = (AssociationDef) role.getContainer();
				if(roleOwner.getDerivedFrom()==null){
					// role is oppend;
					prop=new ViewableProperty(role.getName());
					RoleDef baseOppRole=role.getOppEnd().getRootExtending();
					if(baseOppRole==null)baseOppRole=role.getOppEnd();
					if(baseOppRole.getDestination()!=v){
						prop.setBaseDefInClass(baseOppRole.getDestination().getScopedName(null));
					}
				}
			}else {
                prop=new ViewableProperty(role.getName());
                RoleDef baseRole=role.getRootExtending();
                if(baseRole==null)baseRole=role;
                if(baseRole.getContainer()!=v){
                    prop.setBaseDefInClass(baseRole.getContainer().getScopedName(null));
                }
			}
		}
		return prop;
	}
	public static XtfModel[] buildModelList(TransferDescription td){
		ArrayList modelv=new ArrayList();
		Iterator modeli=td.iterator();
		while(modeli.hasNext()){
			Object modelo=modeli.next();
			if(modelo instanceof PredefinedModel){
				continue;
			}
			if(modelo instanceof Model){
				modelv.add(modelo);
			}
		}
		XtfModel[] ret=new XtfModel[modelv.size()];
		for(int i=0;i<modelv.size();i++){
			Model model=(Model)modelv.get(i);
			ret[i]=new XtfModel();
			ret[i].setName(model.getName());
			String version=model.getModelVersion();
			ret[i].setVersion(version==null?"":version);
			String issuer=model.getIssuer();
			ret[i].setUri(issuer==null?"":issuer);
		}
		return ret;
	}
	
}
