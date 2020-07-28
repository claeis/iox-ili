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
package ch.interlis.iom_j.iligml;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.iox.objpool.ObjectPoolManager;
import ch.ehi.iox.objpool.impl.IomObjectSerializer;
import ch.interlis.iom.*;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.EndBasketEvent;
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iox.StartBasketEvent;
import ch.interlis.iox.StartTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.jts.Iox2jtsext;
import ch.interlis.iox_j.jts.Jtsext2iox;
import ch.interlis.iom_j.ViewableProperties;
import ch.interlis.iom_j.ViewableProperty;
import ch.interlis.iom_j.itf.ModelUtilities;
import ch.interlis.iom_j.itf.impl.ItfAreaPolygon2Linetable;
import ch.interlis.iom_j.xtf.Ili2cUtility;
import ch.interlis.iom_j.xtf.XtfModel;
import ch.interlis.ili2c.generator.Iligml20Generator;
import ch.interlis.ili2c.metamodel.AreaType;
import ch.interlis.ili2c.metamodel.AssociationDef;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.CompositionType;
import ch.interlis.ili2c.metamodel.CoordType;
import ch.interlis.ili2c.metamodel.Element;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

	/** Writer of an INTERLIS 2 transfer file.
	 * @author ceis
	 */
    public class Iligml20Writer implements ch.interlis.iox.IoxWriter
    {
        private XMLStreamWriter xout=null;
    	private ObjectPoolManager recman = null;
        private ViewableProperties mapping = null;
    	private XtfModel xtfModels[]=null;
        private java.util.HashMap nameMapping=null;
    	/** mappings from ili-qualified names to Viewable|AttributeDef
    	 */
    	private HashMap tag2class=null;
        
        private HashSet unkClsv = new HashSet();
    	private long maxOid=0;
    	private String currentTopic=null;
    	private TransferDescription td=null;
    	private IoxFactoryCollection factory=new  ch.interlis.iox_j.DefaultIoxFactoryCollection();
    	private String defaultCrs="EPSG:21781";
        
    	public static final String INTERNAL_T_ID="_t_id";
        public static final String iligmlBase=Iligml20Generator.ILIGML_XMLNSBASE;
        public static final String xmlns_ili=iligmlBase+"/INTERLIS";
        public static final String xmlns_gml="http://www.opengis.net/gml/3.2";
        public static final String xmlns_xlink="http://www.w3.org/1999/xlink"; 
       	public static final String xmlns_xsi="http://www.w3.org/2001/XMLSchema-instance"; 

        /** Creates a new writer.
         * @param buffer Writer to write to
         * @throws IoxException
         */
        public Iligml20Writer(java.io.OutputStreamWriter buffer,TransferDescription td)
		throws IoxException
        { 
        	init(td,buffer);
        }
        /** Creates a new writer.
         * @param buffer Writer to write to
         * @throws IoxException
         */
        public Iligml20Writer(java.io.File buffer,TransferDescription td)
		throws IoxException
        { 
			String encoding="UTF-8";
			try{
	        	init(td,new java.io.OutputStreamWriter(new java.io.FileOutputStream(buffer),encoding));
			} catch (UnsupportedEncodingException ex) {
				throw new IoxException(ex);
			} catch (FileNotFoundException ex) {
				throw new IoxException(ex);
			}
        }
    	private void init(TransferDescription td,java.io.OutputStreamWriter buffer) throws IoxException{
        	this.td=td;
			tag2class=ch.interlis.ili2c.generator.XSDGenerator.getTagMap(td);
            mapping = createMapping(td);
            nameMapping=ch.interlis.ili2c.generator.Gml32Generator.createName2NameMapping(td);
            xtfModels=Ili2cUtility.buildModelList(td);
			recman=new ObjectPoolManager();
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			//if (useRepairing) {
			//	outputFactory.setProperty("javax.xml.stream.isRepairingNamespaces", Boolean.TRUE);
			//}
			try{
				xout = outputFactory.createXMLStreamWriter(new java.io.BufferedWriter(buffer));
				// buffer.getEncoding() may return a historial name like "UTF8", translate this 
				// to a canonical one like "UTF-8"
				String encoding=java.nio.charset.Charset.forName(buffer.getEncoding()).name();
				xout.writeStartDocument(encoding,"1.0");
				//out.setPrefix("xtf",iliNs);
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}
    	}
        private ViewableProperties createMapping(TransferDescription td) {
    		ViewableProperties mapping=new ViewableProperties();
        	java.util.HashMap tagv=Iligml20Generator.createDef2NameMapping(td);
			Iterator tagi=tagv.keySet().iterator();
			for(;tagi.hasNext();){
				Element ili2cEle=(Element)tagi.next();
				String tag=null;
				ArrayList propv=null; // ViewableProperty
				if(ili2cEle instanceof AttributeDef){
					AttributeDef attr=(AttributeDef)ili2cEle;
					tag=ili2cEle.getContainer().getScopedName(null)+"."+ili2cEle.getName();
					propv=Ili2cUtility.mapLinetable(attr);
				}else if(ili2cEle instanceof Viewable){
					propv=new ArrayList(); // ViewableProperty
					Viewable v=(Viewable)ili2cEle;
					tag=v.getScopedName(null);
					Iterator iter = Iligml20Generator.getAttributesAndRoles2(v);
					while (iter.hasNext()) {
						ViewableTransferElement obj = (ViewableTransferElement)iter.next();
						ViewableProperty prop=Ili2cUtility.mapViewableTransferElement( v, obj);
						propv.add(prop);
					}
					
				}
				if(tag!=null){
					mapping.defineClass(tag, (ViewableProperty[])propv.toArray(new ViewableProperty[propv.size()]));
				}
			}
			return mapping;
		}
		public XMLStreamWriter getXout()
        {
        	return xout;
        }
        @Override
        public void close() 
        	throws IoxException
        {
        	flush();
        	if(xout!=null){
				try{
					xout.flush();
				}catch(XMLStreamException ex){
					throw new IoxException(ex);
				}
				xout=null;
        	}
        	if(fixrefs!=null){
        		fixrefs=null;
        	}
        	if(pool!=null){
        		pool.clear();
        		pool=null;
        	}
    		if(recman!=null){
				recman.close();
    		}
    		recman=null;
        }
        @Override
		public void flush() 
			throws IoxException
		{
			if(xout!=null){
				try{
					xout.flush();
				}catch(XMLStreamException ex){
					throw new IoxException(ex);
				}
			}
		}
		
		/** current line seperator
		 *
		 */
		static private String nl=null;
		public void newline()
		throws IoxException
		{
			if(nl==null)nl=System.getProperty("line.separator");
			try{
				xout.writeCharacters(nl);
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}
		}
        private void writeStartTransfer(String sender, String comment,XtfModel models[])
		throws IoxException
        {
			try{
				xout.setPrefix("ili",xmlns_ili);
				xout.writeStartElement(xmlns_ili,Iligml20Generator.TRANSFER);
				xout.writeNamespace("ili",xmlns_ili);
				xout.writeNamespace("gml", xmlns_gml);xout.setPrefix("gml",xmlns_gml);
				xout.writeNamespace("xlink", xmlns_xlink);xout.setPrefix("xlink",xmlns_xlink);
				xout.writeNamespace("xsi", xmlns_xsi);xout.setPrefix("xsi",xmlns_xsi);

				// any other required namespaces
				for(int i=0;i<models.length;i++){
					XtfModel model=models[i];
					if(i+1==models.length){
						xout.writeDefaultNamespace(iligmlBase+"/"+model.getName());
						xout.setDefaultNamespace(iligmlBase+"/"+model.getName());
					}else{
						xout.writeNamespace(model.getName(), iligmlBase+"/"+model.getName());
						xout.setPrefix(model.getName(),iligmlBase+"/"+model.getName());
					}
				}
				
				xout.writeAttribute(xmlns_gml,"id",getNewTid());
				newline();

			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}

        }
        private void writeEndTransfer()
		throws IoxException
        {
			try{
				xout.writeEndElement(); // TRANSFER
				xout.writeEndDocument();
				xout.flush();
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}
            xout=null;
        }
        private void writeStartBasket(String type, String bid)
		throws IoxException
        {
            writeStartBasket(type,bid,IomConstants.IOM_COMPLETE, IomConstants.IOM_FULL,null,null, null);
        }
        private String lastBasketNs=null;
        private void writeStartBasket(String type,String bid,int consistency, int kind,String startstate,String endstate, String[] topicv)
		throws IoxException
        {
        	pool=recman.newObjectPool(new IomObjectSerializer());
        	fixrefs=new HashMap<String,FixRefs>();
        	currentTopic=type;
			try{
				xout.writeStartElement(xmlns_ili,Iligml20Generator.TRANSFERMEMBER);
				newline();
				lastBasketNs=getXmlNs(type);
				xout.writeStartElement(lastBasketNs,getXmlName(type));
				xout.writeAttribute(xmlns_gml,"id",makeOid(bid));
				if (kind != IomConstants.IOM_FULL && kind != IomConstants.IOM_INITIAL)
				{
					throw new IllegalArgumentException();
				}
				if(consistency!=IomConstants.IOM_COMPLETE){
					throw new IllegalArgumentException();
				}
				newline();
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}

        }
        private void writeEndBasket()
		throws IoxException
        {
        	String modelName=null;
        	String topicName=null;
        	int mdlNameSep=currentTopic.indexOf('.');
        	modelName=currentTopic.substring(0,mdlNameSep);
        	topicName=currentTopic.substring(mdlNameSep+1);
        	fixReverseLinks();
        	fixrefs=null;
        	flushBasket(modelName,topicName);
        	pool.clear();
        	pool=null;
			try{
				xout.writeEndElement();
				newline();
				xout.writeEndElement(); // baskets
				newline();
				lastBasketNs=null;
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}
        }
        private void fixReverseLinks() throws IoxException {
        	for(String tid:fixrefs.keySet()){
        		FixRefs fixref=fixrefs.get(tid);
        		IomObject sourceObj=pool.get(fixref.getSourceTid());
        		if(sourceObj==null){
    				throw new IoxException("unknown object with OID "+fixref.getSourceTid());
        		}
        		for(String targetRole:fixref.getTargetRoles()){
        			for(String targetTid:fixref.getTargetTids(targetRole)){
        				sourceObj.addattrobj(targetRole, "REF").setobjectrefoid(targetTid);
        			}
        		}
        	}
		}

		java.util.Map<String, IomObject> pool=null;
        private void flushBasket(String modelName, String topicName) throws IoxException {
			for(String objId : pool.keySet()){
				IomObject iomObj=pool.get(objId);
				writeInternalObject(iomObj);
			}
		}
		private void writeObject(IomObject iomObj)
		throws IoxException
        {
			String currentTid=iomObj.getobjectoid();
			String iliQName=iomObj.getobjecttag();
			// save object
			if(pool.containsKey(currentTid)){
				throw new IoxException("duplicate tid "+currentTid+" in "+iliQName);
			}else{
				pool.put(currentTid, iomObj);
				// add ref in referenced objects
				addReferencesInReferencedObjs(currentTid,iomObj);
			}
			try {
				int tidInt=Integer.parseInt(currentTid);
				if(tidInt>maxOid){
					maxOid=tidInt;
				}
			} catch (NumberFormatException e) {
				// ignore it
			}
        }
		private java.util.Map<String, FixRefs> fixrefs=null;
		private void addReferencesInReferencedObjs(String tid,IomObject iomObj) {
			allReferencesKnownHelper(tid,iomObj);
		}
		private void allReferencesKnownHelper(String thisOid,IomObject iomObj) {
			
			String tag=iomObj.getobjecttag();
			//EhiLogger.debug("tag "+tag);
			Object modelele=tag2class.get(tag);
			if(modelele==null){
				return;
			}
			// ASSERT: an ordinary class/table
			Viewable aclass=(Viewable)modelele;		
					Iterator iter = aclass.getAttributesAndRoles2();
					while (iter.hasNext()) {
						ViewableTransferElement obj = (ViewableTransferElement)iter.next();
						if (obj.obj instanceof AttributeDef) {
							// no gml reverse-links for ili-ref-attrs
						}
						if(obj.obj instanceof RoleDef){
							RoleDef role = (RoleDef) obj.obj;
							if(role.getExtending()==null){
								String roleName=role.getName();
								// a role of an embedded association?
								if(obj.embedded){
									AssociationDef roleOwner = (AssociationDef) role.getContainer();
									if(roleOwner.getDerivedFrom()==null){
										RoleDef oppend=role.getOppEnd();
										if(!oppend.isExternal()){
											 IomObject structvalue=iomObj.getattrobj(roleName,0);
												if(structvalue!=null){
													String refoid=structvalue.getobjectrefoid();
													Viewable targetClass=role.getDestination();
													IomObject targetObj=getIomObject(refoid);
													if(targetObj!=null){
														// add reference in target to this
														targetObj.addattrobj(oppend.getName(), "REF").setobjectrefoid(thisOid);;
													}else{
														addRefFix(refoid,oppend.getName(),thisOid);
													}
												}
										}
									}
								 }else{
									 if(role.hasOneOppEnd()){
											RoleDef oppend=role.getOppEnd();
											if(!oppend.isExternal()){
												String oppendName=oppend.getName();
												 String oppendoid=iomObj.getattrobj(oppendName,0).getobjectrefoid();
												 IomObject structvalue=iomObj.getattrobj(roleName,0);
												 String refoid=structvalue.getobjectrefoid();
												IomObject targetObj=getIomObject(refoid);
												if(targetObj!=null){
													// add reference in target to opposide
													targetObj.addattrobj(oppendName, "REF").setobjectrefoid(oppendoid);;
												}else{
													addRefFix(refoid,oppend.getName(),oppendoid);
												}
											}
									 }
								 }
							}
						 }
					}
		}
		private void addRefFix(String fromOid, String byRoleName, String toOid) {
			FixRefs fixref=fixrefs.get(fromOid);
			if(fixref==null){
				fixref=new FixRefs(fromOid);
				fixrefs.put(fromOid, fixref);
			}
			fixref.addRef(byRoleName, toOid);
		}
		private IomObject getIomObject(String oid) {
			// save object
			if(pool.containsKey(oid)){
				return pool.get(oid);
			}
			return null;
		}
		
		private void writeInternalObject(IomObject iomObj)
		throws IoxException
        {
			String currentTid=iomObj.getobjectoid();
			String iliQName=iomObj.getobjecttag();
			try{
				xout.writeStartElement(lastBasketNs,Iligml20Generator.BASKETMEMBER);
				xout.writeStartElement(getXmlNs(iliQName),getXmlName(iliQName));
				xout.writeAttribute(xmlns_gml,"id",makeOid(currentTid));
				//writeAttributeStringOptional("BID", makeOid(obj.getobjectbid()));
				//writeAttributeStringOptional("OPERATION", encodeOperation(obj.getobjectoperation()));
				//writeAttributeStringOptional("CONSISTENCY", encodeConsistency(obj.getobjectconsistency()));
				writeObjAttrs(iomObj);
				xout.writeEndElement(); 
				xout.writeEndElement(); 
				newline();
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}
        }
        private void writeObjAttrs(IomObject obj)
		throws IoxException
        {
            String tag = obj.getobjecttag();
            // class known?
            if (mapping!=null && mapping.existsClass(tag))
            {
                ViewableProperty[] attrv = mapping.getClassVProperties(tag);
                for (int i = 0; i < attrv.length; i++)
                {
                    writeObjAttr(obj, attrv[i]);
                }
            }
            else
            {
                // unknown class; dump all attributes in undefined order
                // new unknown class?
                if (!unkClsv.contains(tag))
                {
                    EhiLogger.logError("unknown class " + tag);
                    // add it to list of unknown classes
                    unkClsv.add(tag);
                }
                for (int i = 0; i < obj.getattrcount(); i++)
                {
                    writeObjAttr(obj, new ViewableProperty(obj.getattrname(i)));
                }
            }
        }
        private void writeObjAttr(IomObject obj, ViewableProperty attr)
		throws IoxException
        {
        	String xmlns_attr=getXmlNs(obj.getobjecttag());
        	String baseAttrInClass=attr.getBaseDefInClass();
        	if(baseAttrInClass!=null){
            	xmlns_attr=getXmlNs(baseAttrInClass);
        	}
        	String attrName=attr.getName();
			try{
				int valueCount=obj.getattrvaluecount(attrName);
				if(valueCount>0){
					String val=obj.getattrprim(attrName,0);
					// not a primitive?
					if(val==null){
						for(int valuei=0;valuei<valueCount;valuei++) {
							IomObject child=obj.getattrobj(attrName,0);
							if (child != null)
							{
								// some special cases
								if (child.getobjecttag().equals("COORD"))
								{
									// COORD
									xout.writeStartElement(xmlns_attr,attrName);
									writeCoord(child,getDefaultCrs());
									xout.writeEndElement(/*attr*/);
									if (valueCount > 1)
									{
										throw new IoxException("max one COORD value allowed ("+attrName+")");
									}
								}
								else if (child.getobjecttag().equals("POLYLINE"))
								{
									// POLYLINE
									if(attrName.startsWith(ModelUtilities.HELPER_TABLE_GEOM_ATTR_PREFIX)){
										xout.writeStartElement(xmlns_attr,"geometry");
									}else{
										xout.writeStartElement(xmlns_attr,attrName);
									}
									writePolyline(child, false,getDefaultCrs());
									xout.writeEndElement(/*attr*/);
									if (valueCount > 1)
									{
										throw new IoxException("max one POLYLINE value allowed ("+attrName+")");
									}
								}
								else if (child.getobjecttag().equals("MULTISURFACE"))
								{
									// MULTISURFACE
									xout.writeStartElement(xmlns_attr,attrName);
									writeSurface(child,getDefaultCrs());
									xout.writeEndElement(/*attr*/);
									if (valueCount > 1)
									{
										throw new IoxException("max one MULTISURFACE value allowed ("+attrName+")");
									}
								}
								else
								{
									// normal case
									String aref = child.getobjectrefoid();
									boolean isRef = aref != null ? true : false;
									// Reference-attribute or Role or EmbeddedLink?
									if (isRef)
									{
										String orderpos = null;
										if (child.getobjectreforderpos() > 0)
										{
											orderpos = Long.toString(child.getobjectreforderpos());
										}
										String extref = null;
										String bid = child.getobjectrefbid();
										if (bid != null)
										{
											extref = aref;
											aref = null;
										}
										xout.writeStartElement(xmlns_attr,attrName);
										if (aref != null)
										{
											xout.writeAttribute(xmlns_xlink,"href", "#"+makeOid(aref));
										}
										else
										{
											// TODO how to encode extref
											xout.writeAttribute(xmlns_xlink,"href", "#"+makeOid(extref));
											//xout.writeAttribute("BID", makeOid(bid));
										}
										writeAttributeStringOptional(xmlns_ili,Iligml20Generator.ORDER_POS, orderpos);
										xout.writeEndElement(/*attr*/);
										if (child.getattrcount() > 0)
										{
											xout.writeStartElement(xmlns_attr,attrName+"."+Iligml20Generator.LINK_DATA);
											String structType=child.getobjecttag();
											String structTid=child.getobjectoid();
											if(structTid==null){
												structTid=getNewTid();
											}
											xout.writeStartElement(getXmlNs(structType),getXmlName(structType));
											xout.writeAttribute(xmlns_gml,"id",structTid);
											writeObjAttrs(child);
											xout.writeEndElement(/*child*/);
											xout.writeEndElement(/*attr*/);
										}
									}
									else
									{
										// struct
										xout.writeStartElement(xmlns_attr,attrName);
										String structType=child.getobjecttag();
										String structTid=child.getobjectoid();
										if(structTid==null){
											structTid=getNewTid();
										}
										xout.writeStartElement(getXmlNs(structType),getXmlName(structType));
										xout.writeAttribute(xmlns_gml,"id",structTid);
										writeObjAttrs(child);
										xout.writeEndElement(/*child*/);
										xout.writeEndElement(/*attr*/);
									}
								}
							}
						}
					}else{
						if(attr.getEnumType()!=null && !attr.isTypeFinal()){
							val=ch.ehi.basics.tools.StringUtility.purge(val);
							if(val!=null){
								xout.writeStartElement(xmlns_attr,attrName);
								String type=attr.getEnumType();
								String codeSpace=null;
								int pos=type.lastIndexOf(':');
								if(pos>0){
									String enumAttrDef=type.substring(pos+1);
									type=type.substring(0,pos);
									codeSpace=getXmlNs(type)+"/"+getXmlName(type)+"/"+enumAttrDef;
								}else{
									codeSpace=getXmlNs(type)+"/"+getXmlName(type);
								}
								
								xout.writeAttribute("codeSpace", codeSpace);
								xout.writeCharacters(val);
								xout.writeEndElement(); // attrName
							}
						}else{
							writeElementStringOptional(xmlns_attr,attrName,val);
						}
						if(valueCount>1){
							throw new IoxException("max one primitive-type value allowed ("+attrName+")");
						}
					}
				}
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}
        }
		/** writes a coord value or a coord segment.
         */
        private void writeCoord(IomObject obj,String srs)
		throws IoxException
        {
        /*
             object: COORD
               C1
                 102.0
               C2
                 402.0
	        <COORD><C1>102.0</C1><C2>402.0</C2></COORD>
        */
		try{
			xout.writeStartElement(xmlns_gml,"Point");
			String structTid=obj.getobjectoid();
			if(structTid==null){
				structTid=getNewTid();
			}
			xout.writeAttribute(xmlns_gml,"id",structTid);
			if(srs!=null){
				xout.writeAttribute("srsName",srs);
			}
			xout.writeStartElement(xmlns_gml,"pos");
			writePosContent(obj);
			xout.writeEndElement(/*gml:pos*/);
			xout.writeEndElement(/*gml:Point*/);
		}catch(XMLStreamException ex){
			throw new IoxException(ex);
		}

        }
		private void writePosContent(IomObject obj) throws XMLStreamException {
			String c1=obj.getattrprim("C1",0);
			xout.writeCharacters(c1);
			String c2=obj.getattrprim("C2",0);
			if(c2!=null){
				xout.writeCharacters(" ");
				xout.writeCharacters(c2);
				String c3=obj.getattrprim("C3",0);
				if(c3!=null){
					xout.writeCharacters(" ");
					xout.writeCharacters(c3);
				}
			}
		}

        /** writes a polyline value.
         */
        private void writePolyline(IomObject obj,boolean hasLineAttr,String srs)
		throws IoxException
        {
        /*
             object: POLYLINE [INCOMPLETE]
               lineattr
                 object: Model.Topic.LineAttr
                   attr00
                     11
               sequence // if incomplete; multi sequence values
                 object: SEGMENTS
                   segment
                     object: COORD
                       C1
                         102.0
                       C2
                         402.0
                   segment
                     object: ARC
                       C1
                         103.0
                       C2
                         403.0
                       A1
                         104.0
                       A2
                         404.0
                   segment
                     object: Model.SplineParam
                       SegmentEndPoint
                         object: COORD
                           C1
                             103.0
                           C2
                             403.0
                       p0
                         1.0
                       p1
                         2.0

		        <POLYLINE>
			        <LINEATTR>
				        <Model.Topic.LineAttr>
					        <attr00>11</attr00>
				        </Model.Topic.LineAttr>
			        </LINEATTR>
			        <COORD>
				        <C1>101.0</C1>
				        <C2>401.0</C2>
			        </COORD>
			        <COORD>
				        <C1>102.0</C1>
				        <C2>402.0</C2>
			        </COORD>
			        <Model.SplineParam>
				        <SegmentEndPoint>
					        <COORD>
						        <C1>103.0</C1>
						        <C2>403.0</C2>
					        </COORD>
				        </SegmentEndPoint>
				        <p0>1.0</p0>
				        <p1>2.0</p1>
			        </Model.SplineParam>
		        </POLYLINE>
        */
		try{
			xout.writeStartElement(xmlns_gml,"Curve");
			String structTid=obj.getobjectoid();
			if(structTid==null){
				structTid=getNewTid();
			}
			xout.writeAttribute(xmlns_gml,"id",structTid);
			if(srs!=null){
				xout.writeAttribute("srsName",srs);
			}
			xout.writeStartElement(xmlns_gml,"segments");
			if(hasLineAttr){
				IomObject lineattr=obj.getattrobj("lineattr",0);
				if(lineattr!=null){
					xout.writeStartElement(xmlns_ili,"LINEATTR");
					xout.writeStartElement(xmlns_ili,lineattr.getobjecttag());
					writeObjAttrs(lineattr);
					xout.writeEndElement(/*lineattr*/);
					xout.writeEndElement(/*LINEATTR*/);
				}
			}
			boolean clipped=obj.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
			if(clipped){
				throw new IoxException("clipped polyline not supported");
			}
			for(int sequencei=0;sequencei<obj.getattrvaluecount("sequence");sequencei++){
				if(!clipped){
					// an unclipped polyline should have only one sequence element
					if(sequencei>0){
						throw new IllegalArgumentException("unclipped polyline with multi 'sequence' elements");
					}
				}
				IomObject sequence=obj.getattrobj("sequence",sequencei);
				int segmentc=sequence.getattrvaluecount("segment");
				for(int segmenti=0;segmenti<segmentc;segmenti++){
					IomObject segment=sequence.getattrobj("segment",segmenti);
					if(segment.getobjecttag().equals("COORD")){
						// COORD
						if(segmenti==0 && !sequence.getattrobj("segment",segmenti+1).getobjecttag().equals("COORD")){
							// just start point, will not be a LineStringSegment
							// skip it
						}else{
							// first point of a LineStringSegment?
							if(segmenti==0 || !sequence.getattrobj("segment",segmenti-1).getobjecttag().equals("COORD")){
								xout.writeStartElement(xmlns_gml,"LineStringSegment");
								xout.writeAttribute("interpolation","linear");
								xout.writeStartElement(xmlns_gml,"posList");
								// not first segment
								if(segmenti>0){
									// add endpoint of last segemtn as new start point
									IomObject lastSegment=sequence.getattrobj("segment",segmenti-1);
									writePosContent(lastSegment);
									// add seperator
									xout.writeCharacters(" ");
								}
							}else{
								// add seperator
								xout.writeCharacters(" ");
							}
							writePosContent(segment);
							// last point of a LineStringSegment?
							if(segmenti+1==segmentc || !sequence.getattrobj("segment",segmenti+1).getobjecttag().equals("COORD")){
								// finish LineStringSegment
								xout.writeEndElement(); // posList
								xout.writeEndElement(); // LineStringSegment
							}
						}
					}else if(segment.getobjecttag().equals("ARC")){
						// ARC
						xout.writeStartElement(xmlns_gml,"Arc");
						xout.writeAttribute("numArc","1");
						xout.writeAttribute("interpolation","circularArc3Points");
						xout.writeStartElement(xmlns_gml,"posList");
						// start point
						IomObject lastSegment=sequence.getattrobj("segment",segmenti-1);
						writePosContent(lastSegment);
						xout.writeCharacters(" ");

						// mid point
						String a1=segment.getattrprim("A1",0);
						String a2=segment.getattrprim("A2",0);
						//String r=segment.getattrprim("R",0);
						xout.writeCharacters(" ");
						xout.writeCharacters(a1);
						xout.writeCharacters(" ");
						xout.writeCharacters(a2);
						xout.writeCharacters(" ");
						
						// end point
						writePosContent(segment);
						
						xout.writeEndElement(); // posList
						xout.writeEndElement(); // Arc
					}else{
						// custom line form
						throw new IoxException("custum line form not supported ("+segment.getobjecttag()+")");
					}

				}
			}
			xout.writeEndElement(/*segments*/);
			xout.writeEndElement(/*Curve*/);
		}catch(XMLStreamException ex){
			throw new IoxException(ex);
		}
        }

        /** writes a surface value.
         */
        private void writeSurface(IomObject obj,String srs)
		throws IoxException
        {
        /*
             object: MULTISURFACE [INCOMPLETE]
               surface // if incomplete; multi surface values
                 object: SURFACE
                   boundary
                     object: BOUNDARY
                       polyline
                         object: POLYLINE

        	
	        <SURFACE>
		        <BOUNDARY>
			        <POLYLINE .../>
			        <POLYLINE .../>
		        </BOUNDARY>
		        <BOUNDARY>
			        <POLYLINE .../>
			        <POLYLINE .../>
		        </BOUNDARY>
	        </SURFACE>
        */
		try{
			xout.writeStartElement(xmlns_gml,"Polygon");
			String structTid=obj.getobjectoid();
			if(structTid==null){
				structTid=getNewTid();
			}
			xout.writeAttribute(xmlns_gml,"id",structTid);
			if(srs!=null){
				xout.writeAttribute("srsName",srs);
			}
			boolean clipped=obj.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
			if(clipped){
				throw new IoxException("clipped surface not supported");
			}
			for(int surfacei=0;surfacei<obj.getattrvaluecount("surface");surfacei++){
				if(!clipped){
					// an unclipped surface should have only one surface element
					if(surfacei>0){
						throw new IllegalArgumentException("unclipped surface with multi 'surface' elements");
					}
				}
				IomObject surface=obj.getattrobj("surface",surfacei);
				for(int boundaryi=0;boundaryi<surface.getattrvaluecount("boundary");boundaryi++){
					IomObject boundary=surface.getattrobj("boundary",boundaryi);
					if(boundaryi==0){
						xout.writeStartElement(xmlns_gml,"exterior");
						xout.writeStartElement(xmlns_gml,"Ring");
					}else{
						xout.writeStartElement(xmlns_gml,"interior");
						xout.writeStartElement(xmlns_gml,"Ring");
					}
					for(int polylinei=0;polylinei<boundary.getattrvaluecount("polyline");polylinei++){
						IomObject polyline=boundary.getattrobj("polyline",polylinei);
						xout.writeStartElement(xmlns_gml,"curveMember");						
						writePolyline(polyline,true,/* srs */ null);
						xout.writeEndElement(); // curveMember						
					}
					xout.writeEndElement(); // Ring
					xout.writeEndElement(); // exterior/interior
				}
			}
			xout.writeEndElement(); // Polygon
		}catch(XMLStreamException ex){
			throw new IoxException(ex);
		}
        }

        private void writeElementStringOptional(String nsuri,String name, String value)
		throws IoxException
        {
			try{
				if(value!=null){
					String v=value.trim();
					if(v.length()>0){
						xout.writeStartElement(nsuri,name);
						xout.writeCharacters(v);
						xout.writeEndElement();
					}
				}
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}
        }
        private void writeAttributeStringOptional(String nsuri,String name, String value)
		throws IoxException
        {
			try{
				if (value != null)
				{
					String v = value.trim();
					if (v.length() > 0)
					{
						xout.writeAttribute(nsuri,name, v);
					}
				}
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}
        }
		private String getXmlNs(String type)
		{
			int pos=type.indexOf('.');
			String ret=iligmlBase+"/"+type.substring(0,pos);
			return ret;
		}
		private String getXmlName(String type)
		{
			if(!nameMapping.containsKey(type)){
				throw new IllegalArgumentException();
			}
			String ret=(String)nameMapping.get(type);
			return ret;
		}

        private String makeOid(String value)
        {
            if (value != null)
            {
                String v = value.trim();
                if (v.length() > 0)
                {
                	if(java.lang.Character.isDigit(v.charAt(0))){
                        return "x" + v;
                	}
            		return v;
                }
            }
            return null;
        }
        private int tid=1;
        private String getNewTid()
        {
        	return "iox"+Integer.toString(tid++);
        }
    	private void getPolygonAttrs(Viewable aclass,ArrayList<AttributeDef> attrs_areaAttrs) {
    		Iterator<?> attri = aclass.getAttributes ();
    		while (attri.hasNext ()){
    		  Object attrObj = attri.next();
    		  if (attrObj instanceof AttributeDef)
    		  {
    			AttributeDef attr = (AttributeDef) attrObj;
    			Type type = Type.findReal (attr.getDomain());
    			if(type instanceof AreaType){
    				attrs_areaAttrs.add(attr);
    			}
    		  }
    		}
    		
    		
    	}
		@Override
		public IomObject createIomObject(String type, String oid)
				throws IoxException {
			return factory.createIomObject(type, oid);
		}
		@Override
		public IoxFactoryCollection getFactory() throws IoxException {
			return factory;
		}
		@Override
		public void setFactory(IoxFactoryCollection arg0) throws IoxException {
			this.factory=factory;
		}
		@Override
		public void write(IoxEvent event) throws IoxException {
			if(event instanceof StartTransferEvent){
				StartTransferEvent e=(StartTransferEvent)event;
				writeStartTransfer(e.getSender(),e.getComment(),xtfModels);
			}else if(event instanceof StartBasketEvent){
				StartBasketEvent e=(StartBasketEvent)event;
				writeStartBasket(e.getType(),e.getBid(),e.getConsistency(),e.getKind(),e.getStartstate(),e.getEndstate(),e.getTopicv());
			}else if(event instanceof ObjectEvent){
				ObjectEvent e=(ObjectEvent)event;
				writeObject(e.getIomObject());
			}else if(event instanceof EndBasketEvent){
				writeEndBasket();
			}else if(event instanceof EndTransferEvent){
				writeEndTransfer();
			}else{
				throw new IoxException("unknown event type "+event.getClass().getName());
			}
			
		}
		public void setDefaultCrs(String defaultCrs) {
			this.defaultCrs = defaultCrs;
		}
        public String getDefaultCrs() {
			return defaultCrs;
		}
        
    }
