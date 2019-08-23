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
package ch.interlis.iom_j.itf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.metamodel.*;
import ch.interlis.iom_j.itf.impl.ItfScanner;

/** Utilities related to ITF files.
 * @author ce
 * @version $Revision: 1.0 $ $Date: 27.07.2006 $
 */
public class ModelUtilities {
	public static final String HELPER_TABLE_MAIN_TABLE_REF_PREFIX="_itf_ref_";
    public static final String HELPER_TABLE_MAIN_TABLE_REF2_PREFIX="_itf_ref2_";
	public static final String HELPER_TABLE_GEOM_ATTR_PREFIX="_itf_geom_";
	/** Utility, no instances.
	 */
	private ModelUtilities(){};
	/** Gets list of mappings from transfer-table name to model-table or model-attribute.
	 * Tags as they appear in the ITF file and are used by ItfReader and ItfWriter.
	 * @param td model as read by the compiler (ili2c)
	 * @return list of mappings. map&lt;String iliQName,Viewable|AttributeDef modelele&gt;
	 * @see getTagMap2()
	 */
	public static HashMap getTagMap(TransferDescription td){
		HashMap ret=new HashMap();
		Iterator modeli = td.iterator ();
		while (modeli.hasNext ())
		{
		  Object mObj = modeli.next ();
		  if(mObj instanceof Model){
			Model model=(Model)mObj;
			if(model instanceof TypeModel){
				continue;
			}
			Iterator topici=model.iterator();
			while(topici.hasNext()){
			  Object tObj=topici.next();
			  if (tObj instanceof Topic){
				  Topic topic=(Topic)tObj;
				  Iterator iter = topic.getViewables().iterator();
				  while (iter.hasNext())
				  {
					Object obj = iter.next();
					if(obj instanceof Viewable){
						Viewable v=(Viewable)obj;
						//log.logMessageString("getTransferViewables() leave <"+v+">",IFMELogFile.FME_INFORM);
						String className=v.getScopedName(null);
						ret.put(className,v);
						// add surface and area attributes
						Iterator attri = v.getAttributes ();
						while (attri.hasNext ()){
						  Object attrObj = attri.next();
						  if (attrObj instanceof AttributeDef)
						  {
							AttributeDef attr = (AttributeDef) attrObj;
							Type type = Type.findReal (attr.getDomain());
							if(type instanceof SurfaceOrAreaType){
							  String name=v.getContainer().getScopedName(null)+"."+v.getName()+"_"+attr.getName();
							  ret.put(name,attr);
							}
						  }
						}
					}
				  }
			  }else if(tObj instanceof Viewable){
				  Viewable v=(Viewable)tObj;
				  //log.logMessageString("getTransferViewables() leave <"+v+">",IFMELogFile.FME_INFORM);
				  ret.put(v.getScopedName(null),v);
					// add surface and area attributes
					Iterator attri = v.getAttributes ();
					while (attri.hasNext ()){
					  Object attrObj = attri.next();
					  if (attrObj instanceof AttributeDef)
					  {
						AttributeDef attr = (AttributeDef) attrObj;
						Type type = Type.findReal (attr.getDomain());
						if(type instanceof SurfaceOrAreaType){
						  String name=v.getContainer().getScopedName(null)+"."+v.getName()+"_"+attr.getName();
						  ret.put(name,attr);
						}
					  }
					}
			  }
			}
		  }
		}
		return ret;
	}
	/** Gets list of mappings from transfer-table name to model-table or model-attribute.
	 * Tags as they are used by ItfReader2 and ItfWriter2.
	 * @param td model as read by the compiler (ili2c)
	 * @return list of mappings. map&lt;String iliQName,Viewable modelele&gt;
	 */
	public static HashMap getTagMap2(TransferDescription td){
		HashMap ret=new HashMap();
		Iterator modeli = td.iterator ();
		while (modeli.hasNext ())
		{
		  Object mObj = modeli.next ();
		  if(mObj instanceof Model){
			Model model=(Model)mObj;
			if(model instanceof TypeModel){
				continue;
			}
			Iterator topici=model.iterator();
			while(topici.hasNext()){
			  Object tObj=topici.next();
			  if (tObj instanceof Topic){
				  Topic topic=(Topic)tObj;
				  Iterator iter = topic.getViewables().iterator();
				  while (iter.hasNext())
				  {
					Object obj = iter.next();
					if(obj instanceof Viewable){
						Viewable v=(Viewable)obj;
						//log.logMessageString("getTransferViewables() leave <"+v+">",IFMELogFile.FME_INFORM);
						String className=v.getScopedName(null);
						ret.put(className,v);
						// no need to add surface and area attributes
					}
				  }
			  }else if(tObj instanceof Viewable){
				  Viewable v=(Viewable)tObj;
				  //log.logMessageString("getTransferViewables() leave <"+v+">",IFMELogFile.FME_INFORM);
				  ret.put(v.getScopedName(null),v);
					// no need to add surface and area attributes
			  }
			}
		  }
		}
		return ret;
	}
	/** Gets list of ITF tables of a given topic.
	 * @param td model as read by the compiler (ili2c)
	 * @param topic ili topic
	 * @return list of tables. array&lt;Viewable|AttributeDef modelele&gt;
	 */
	public static ArrayList getItfTables(TransferDescription td,Topic topic){
		ArrayList ret=new ArrayList();
		  Iterator iter = topic.getViewables().iterator();
		  while (iter.hasNext())
		  {
			Object obj = iter.next();
			if(obj instanceof Viewable){
				Viewable v=(Viewable)obj;
				if((v instanceof Table) && !((Table)v).isIdentifiable()){
					// STRUCTURE
					continue;
				}
				if(isPureRefAssoc(v)){
					continue;
				}
				Iterator attri =null;
				
				// add helper tables of area attributes
				attri = v.getAttributes();
				while (attri.hasNext()) {
					Object attrObj = attri.next();
					if (attrObj instanceof AttributeDef) {
						AttributeDef attr = (AttributeDef) attrObj;
						Type type = Type.findReal(attr.getDomain());
						if (type instanceof AreaType) {
							// area helper table
							ret.add(attr);
						}
					}
				}
				
				// main table
				ret.add(v);
				
				// add helper tables of surface attributes
				attri = v.getAttributes();
				while (attri.hasNext()) {
					Object attrObj = attri.next();
					if (attrObj instanceof AttributeDef) {
						AttributeDef attr = (AttributeDef) attrObj;
						Type type = Type.findReal(attr.getDomain());
						if (type instanceof SurfaceType) {
							// surface helper table
							ret.add(attr);
						}
					}
				}
				
			}
		  }
		  return ret;
	}
	/** Gets list of ITF tables of a given topic.
	 * @param td model as read by the compiler (ili2c)
	 * @param modelName name of ili model
	 * @param topicName name of ili topic
	 * @return list of tables. array&lt;Viewable|AttributeDef modelele&gt;
	 */
	public static ArrayList getItfTables(TransferDescription td,String modelName,String topicName){
		Iterator modeli = td.iterator ();
		while (modeli.hasNext ())
		{
		  Object mObj = modeli.next ();
		  if(mObj instanceof Model){
			Model model=(Model)mObj;
			if(!model.getName().equals(modelName)){
				continue;
			}
			Iterator topici=model.iterator();
			while(topici.hasNext()){
			  Object tObj=topici.next();
			  if (tObj instanceof Topic){
				  Topic topic=(Topic)tObj;
					if(!topic.getName().equals(topicName)){
						continue;
					}
					return getItfTables(td,topic);
			  }
			}
		  }
		}
		return null;
	}
	/** Gets list of POLYLINE attributes.
	 * TODO move to compiler
	 */
	public static ArrayList getPolylineAttrs(AbstractClassDef table)
	{
		ArrayList ret=new ArrayList();
		Iterator iter = table.getAttributes ();
		while (iter.hasNext ()){
		  AttributeDef attr=(AttributeDef) iter.next ();
		  Type type = Type.findReal (attr.getDomain());
		  if(type instanceof PolylineType){
			ret.add(attr);
		  }
		}
		return ret;
	}
	/** Builds list of attributes and keep Ili1 source ordering.
	 * TODO move to compiler
	 * @return list<ViewableTransferElement>
	 */
	public static ArrayList getIli1AttrList(AbstractClassDef table)
	{
		ArrayList attrlist=new ArrayList();
		Iterator iter = table.getAttributesAndRoles ();
		while (iter.hasNext ()){
			Object obj = iter.next();
			if (obj instanceof AttributeDef){
			  attrlist.add(new ViewableTransferElement(obj));
			}else if(obj instanceof RoleDef && !((AssociationDef)table).isLightweight()){
			  attrlist.add(new ViewableTransferElement(obj,false));
			}
		}
		  java.util.ArrayList rolesSorted=new java.util.ArrayList(table.getLightweightAssociations());
		  java.util.Collections.sort(rolesSorted,new java.util.Comparator(){
			public int compare(Object o1,Object o2){
			  int idx1=((RoleDef)o1).getIli1AttrIdx();
			  int idx2=((RoleDef)o2).getIli1AttrIdx();
			  if(idx1==idx2)return 0;
			  if(idx1==-1)return 1;
			  if(idx2==-1)return -1;
			  if(idx1<idx2)return -1;
			  return 1;
			}
		  });
		  iter=rolesSorted.iterator();
		  while (iter.hasNext()){
			RoleDef role = (RoleDef) iter.next();
			RoleDef oppend = role.getOppEnd();
			if(role.getIli1AttrIdx()==-1){
			  attrlist.add(new ViewableTransferElement(oppend,true));
			}else{
			  attrlist.add(role.getIli1AttrIdx(),new ViewableTransferElement(oppend,true));
			}
		  }
		  return attrlist;
	}
	
	/** Builds set of enumeration types.
	 * TODO move to compiler
	 * @param td model as read by the compiler (ili2c)
	 * @return set of enumeration types. set&lt;DomainDef|AttributeDef modelele&gt;
	 */
	public static java.util.HashSet getEnumTypes(TransferDescription td){
		java.util.HashSet ret=new java.util.HashSet();
		Iterator modeli = td.iterator ();
		while (modeli.hasNext ())
		{
		  Object mObj = modeli.next ();
		  if(mObj instanceof Model){
			Model model=(Model)mObj;
			Iterator topici=model.iterator();
			while(topici.hasNext()){
			  Object tObj=topici.next();
			  if(tObj instanceof Domain){
				  Domain domain=(Domain)tObj;
				  Type type=domain.getType();
				  if(type instanceof EnumerationType){
					ret.add(domain);
				  }
			  }else if (tObj instanceof Topic){
				  Topic topic=(Topic)tObj;
				  Iterator iter = topic.getViewables().iterator();
				  while (iter.hasNext())
				  {
					Object obj = iter.next();
					if(obj instanceof Domain){
						Domain domain=(Domain)obj;
						Type type=domain.getType();
						if(type instanceof EnumerationType){
						  ret.add(domain);
						}
					}else if(obj instanceof Viewable){
						Viewable v=(Viewable)obj;
						// add attributes with anonymous enum type
						Iterator attri = v.getAttributes ();
						while (attri.hasNext ()){
						  Object attrObj = attri.next();
						  if (attrObj instanceof AttributeDef)
						  {
							AttributeDef attr = (AttributeDef) attrObj;
							Type type = attr.getDomain();
							if(type instanceof EnumerationType){
							  ret.add(attr);
							}
						  }
						}
					}
				  }
			  }else if(tObj instanceof Viewable){
				  Viewable v=(Viewable)tObj;
				  //log.logMessageString("getTransferViewables() leave <"+v+">",IFMELogFile.FME_INFORM);
				// add attributes with anonymous enum type
					Iterator attri = v.getAttributes ();
					while (attri.hasNext ()){
					  Object attrObj = attri.next();
					  if (attrObj instanceof AttributeDef)
					  {
						AttributeDef attr = (AttributeDef) attrObj;
						Type type = attr.getDomain();
						if(type instanceof EnumerationType){
						  ret.add(attr);
						}
					  }
					}
			  }
			}
		  }
		}
		return ret;
	}
	/** Translates a ITF character code to a JAVA String.
	 */
	public static String code2string(int code)
		throws java.io.UnsupportedEncodingException
	{
		byte bytes[]=new byte[1];
		bytes[0]=(byte)code;
		return new String(bytes,ItfScanner.ITF_CHARSET);
	}
	/** Builds a list of elements of an enumeration.
	 * 	<pre><code>
	 *    java.util.ArrayList ev=new java.util.ArrayList();
	 *    ModelUtilities.buildEnumList(ev,"",type.getConsolidatedEnumeration());
	 *  </code></pre>
	 *  The resulting list contains the element names, so that the first entry 
	 *  represents ITF code 0 of the given enumeration.
	 * @param accu list to populate
	 * @param prefix1 prefix to add in front of each element
	 * @param enumer enumeration to collect elements of.
	 */
	public static void buildEnumList(java.util.List accu,String prefix1,ch.interlis.ili2c.metamodel.Enumeration enumer){
		Iterator iter = enumer.getElements();
		String prefix="";
		if(prefix1.length()>0){
		  prefix=prefix1+".";
		}
		while (iter.hasNext()) {
		  ch.interlis.ili2c.metamodel.Enumeration.Element ee=(ch.interlis.ili2c.metamodel.Enumeration.Element) iter.next();
		  ch.interlis.ili2c.metamodel.Enumeration subEnum = ee.getSubEnumeration();
		  if (subEnum != null)
		  {
			// ee is not leaf, add its name to prefix and add sub elements to accu
			buildEnumList(accu,prefix+ee.getName(),subEnum);
		  }else{
			// ee is a leaf, add it to accu
			accu.add(prefix+ee.getName());
		  }
		}
	}
	/** Builds a list of elements of an enumeration.
	 * 	<pre><code>
	 *    java.util.List<java.util.Map.Entry<String,ch.interlis.ili2c.metamodel.Enumeration.Element>> ev=new java.util.ArrayList<java.util.Map.Entry<String,ch.interlis.ili2c.metamodel.Enumeration.Element>>();
	 *    ModelUtilities.buildEnumElementList(ev,"",type.getConsolidatedEnumeration());
	 *  </code></pre>
	 *  The resulting list contains the qualified element names with the elements, so that the first entry 
	 *  represents ITF code 0 of the given enumeration.
	 * @param accu list to populate
	 * @param prefix1 prefix to add in front of each element
	 * @param enumer enumeration to collect elements of.
	 */
	public static void buildEnumElementList(java.util.List<java.util.Map.Entry<String,ch.interlis.ili2c.metamodel.Enumeration.Element>> accu,String prefix1,ch.interlis.ili2c.metamodel.Enumeration enumer){
		Iterator iter = enumer.getElements();
		String prefix="";
		if(prefix1.length()>0){
		  prefix=prefix1+".";
		}
		while (iter.hasNext()) {
		  ch.interlis.ili2c.metamodel.Enumeration.Element ee=(ch.interlis.ili2c.metamodel.Enumeration.Element) iter.next();
		  ch.interlis.ili2c.metamodel.Enumeration subEnum = ee.getSubEnumeration();
		  if (subEnum != null)
		  {
			// ee is not leaf, add its name to prefix and add sub elements to accu
			buildEnumElementList(accu,prefix+ee.getName(),subEnum);
		  }else{
			// ee is a leaf, add it to accu
			accu.add(new java.util.AbstractMap.SimpleEntry<String,ch.interlis.ili2c.metamodel.Enumeration.Element>(prefix+ee.getName(),ee));
		  }
		}
	}
	public static String getHelperTableMainTableRef(AttributeDef attr)
	{
		return HELPER_TABLE_MAIN_TABLE_REF_PREFIX+attr.getContainer().getName();
	}
    public static String getHelperTableMainTableRef2(AttributeDef attr)
    {
        return HELPER_TABLE_MAIN_TABLE_REF2_PREFIX+attr.getContainer().getName();
    }
	public static String getHelperTableGeomAttrName(AttributeDef attr)
	{
		return HELPER_TABLE_GEOM_ATTR_PREFIX+attr.getContainer().getName();
	}
	public static boolean isPureRefAssoc(Viewable v) {
		if(!(v instanceof AssociationDef)){
			return false;
		}
		AssociationDef assoc=(AssociationDef)v;
		// embedded and no attributes/embedded links?
		if(assoc.isLightweight() && 
			!assoc.getAttributes().hasNext()
			&& !assoc.getLightweightAssociations().iterator().hasNext()
			) {
			return true;
		}
		return false;
	}

}
