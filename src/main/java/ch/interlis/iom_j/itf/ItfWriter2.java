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
import ch.interlis.iox_j.*;
import ch.interlis.iox_j.jts.Iox2jtsext;
import ch.interlis.iox_j.jts.Jtsext2iox;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iom.*;
import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.iox.objpool.ObjectPoolManager;
import ch.ehi.iox.objpool.impl.IomObjectSerializer;
import ch.interlis.ili2c.metamodel.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iom_j.itf.impl.ItfAreaPolygon2Linetable;

/** This class implements an INTERLIS 1 writer.
 * @author ce
 * @version $Revision: 1.0 $ $Date: 17.07.2006 $
 */
public class ItfWriter2 implements ch.interlis.iox.IoxWriter {
	private TransferDescription td=null;
	private ItfWriter out=null;
	private ObjectPoolManager recman = null;
	private ArrayList itftablev=null; // Array<Viewable|AttributeDef itfTable> list of tables according to ITF
	private HashMap tag2class=null;
	private HashSet topics=null;
	private long maxOid=0;
	public static final String INTERNAL_T_ID="_t_id";

	/*
	private String modelName=null;
	private String topicName=null;
	private String className=null;
	private int currentItfTable=-1;
	private int state=0;
	private ItfStartTransferEvent itfStart=null;
	private char blankCode='_';
	java.io.OutputStream outStream=null;
	private IoxFactoryCollection factory=new  ch.interlis.iox_j.DefaultIoxFactoryCollection();
	*/
	private HashMap<String,StartBasketEvent> startBasketEvents=new HashMap<String,StartBasketEvent>();
	private HashMap<String,EndBasketEvent> endBasketEvents=new HashMap<String,EndBasketEvent>();
	private String currentTopic=null;
	private Model currentModel=null;
	
	/** Creates a new writer.
	 * @param out Output stream to write to
	 * @param td model of data to write as read by the compiler 
	 * @throws IoxException
	 */
	public ItfWriter2(java.io.OutputStream out,TransferDescription td)
	throws IoxException 
	//java.io.UnsupportedEncodingException
	//,java.io.IOException
	{
		this.td=td;
		this.out=new ItfWriter(out,td);
		init();
	}
	/** Creates a new writer.
	 * @param outFile file to write to
	 * @param td model of data to write as read by the compiler 
	 * @throws IoxException
	 */
	public ItfWriter2(java.io.File outFile,TransferDescription td)
	throws IoxException 
	{
		this.td=td;
		this.out=new ItfWriter(outFile,td);
		init();
	}
	private void init() throws IoxException{
		recman=new ObjectPoolManager();
	}
	public void close()
		throws IoxException 
	{
		flush();
		
    	for(Map<String, IomObject> m:pools.values()){
    		m.clear();
    	}
    	pools=null;
		
		if(out!=null){
			out.close();
			out=null;
		}
		if(recman!=null){
			recman.close();
		}
		recman=null;
		td=null;
	}
	public void flush()
		throws IoxException 
	{
		if(out!=null){
			out.flush();
		}
	}

	public void write(ch.interlis.iox.IoxEvent event) 
		throws IoxException 
	{
		if(event instanceof StartTransferEvent){
        	pools=new java.util.HashMap<String, java.util.Map<String,IomObject>>();
			out.write(event);
		}else if(event instanceof StartBasketEvent){
			// save event
			currentTopic = ((StartBasketEvent)event).getType();
			if(topics==null){
				topics=new HashSet<String>();
				String currentModelName=currentTopic.substring(0,currentTopic.indexOf('.'));
				currentModel=(Model) td.getElement(Model.class,currentModelName);
				// FORALL topics
				Iterator topici=currentModel.iterator();
				while(topici.hasNext()){
				  Object tObj=topici.next();
				  if (tObj instanceof Topic){
					  Topic topic=(Topic)tObj;
					  String topicQName=topic.getScopedName(null);
					  topics.add(topicQName);
				  }
				}
			}
			if(!topics.contains(currentTopic)){
				throw new IoxException("unknown topic <"+currentTopic+">");
			}
			startBasketEvents.put(currentTopic,(StartBasketEvent)event);
		}else if(event instanceof ObjectEvent){
			if(currentTopic==null){
				throw new IoxException("object unexpected outside of a basket");
			}
			IomObject iomObj=((ObjectEvent)event).getIomObject();
			String iliQName=iomObj.getobjecttag();
			if(tag2class==null){
				tag2class=ModelUtilities.getTagMap(td);
			}
			if(!tag2class.containsKey(iliQName)){
				throw new IoxException("unknown class <"+iliQName+">");
			}
			String currentTid=iomObj.getobjectoid();
			// save object
			java.util.Map<String, IomObject> pool=getObjectPool(iliQName);
			if(pool.containsKey(currentTid)){
				throw new IoxException("duplicate tid "+currentTid+" in "+iliQName);
			}else{
				pool.put(currentTid, iomObj);
			}
			try {
				int tidInt=Integer.parseInt(currentTid);
				if(tidInt>maxOid){
					maxOid=tidInt;
				}
			} catch (NumberFormatException e) {
				// ignore it
			}
		}else if(event instanceof EndBasketEvent){
			// save event
			endBasketEvents.put(currentTopic,(EndBasketEvent)event);
			currentTopic=null;
		}else if(event instanceof EndTransferEvent){
			if(currentModel!=null){
				String modelName=currentModel.getName();
				// FORALL topics
				Iterator topici=currentModel.iterator();
				while(topici.hasNext()){
				  Object tObj=topici.next();
				  if (tObj instanceof Topic){
					  Topic topic=(Topic)tObj;
					  String topicName=topic.getName();
					  String topicQName=topic.getScopedName(null);
					  if(startBasketEvents.containsKey(topicQName)){
							// write saved StartBasketEvent
						  	out.write(startBasketEvents.get(topicQName));
							// setup list of itf tables
							itftablev=ModelUtilities.getItfTables(td,modelName,topicName);
							// FORALL tables
							for(Object tableo:itftablev){
								if(!(tableo instanceof Table)){
									continue;
								}
								Table table=(Table)tableo;
								String tableQName=table.getScopedName(null);
								ArrayList<AttributeDef> areaAttrs=new ArrayList<AttributeDef>();
								ArrayList<AttributeDef> surfaceAttrs=new ArrayList<AttributeDef>();
								getPolygonAttrs(table,areaAttrs, surfaceAttrs);
								// IF Table without surface or area attrs
								if(areaAttrs.size()==0 && surfaceAttrs.size()==0){
									// write objects
									java.util.Map<String, IomObject> pool=getObjectPool(tableQName);
									for(String poolId : pool.keySet()){
										IomObject iomObj=pool.get(poolId);
										out.write(new ObjectEvent(iomObj));
									}
								}else{
									// write area helper tables
									// FORALL area attrs
									for(AttributeDef attr:areaAttrs){
										String attrName=attr.getName();
										String lineTableName=tableQName+"_"+attrName;
										EhiLogger.logState("build linetable "+lineTableName+"...");
										ItfAreaPolygon2Linetable allLines=new ItfAreaPolygon2Linetable(tableQName, recman);
										// FORALL main objects
										java.util.Map<String, IomObject> pool=getObjectPool(tableQName);
										for(String mainObjTid : pool.keySet()){
											IomObject iomObj=pool.get(mainObjTid);
											IomObject iomPolygon=iomObj.getattrobj(attrName, 0);
											if(iomPolygon!=null){
												String internalTid=iomObj.getattrvalue(INTERNAL_T_ID);
												// get lines
												ArrayList<IomObject> lines=ItfAreaPolygon2Linetable.getLinesFromPolygon(iomPolygon);
												allLines.addLines(mainObjTid,internalTid,lines);
											}
										}
										// nodes it / check noding
										// polygonize
										// write line objects
										for(IomObject line:allLines.getLines()){
											IomObject lineTableObj=new Iom_jObject(lineTableName, Long.toString(++maxOid));
											String iomAttrName=ch.interlis.iom_j.itf.ModelUtilities.getHelperTableGeomAttrName(attr);
											lineTableObj.addattrobj(iomAttrName, line);
											out.write(new ObjectEvent(lineTableObj));
										}
									}
									// write main table
									{
										// FORALL main objects
										java.util.Map<String, IomObject> pool=getObjectPool(tableQName);
										for(String poolId : pool.keySet()){
											IomObject iomObj=pool.get(poolId);
											// FORALL area attrs
											for(AttributeDef attr:areaAttrs){
												String attrName=attr.getName();
												// set georef into main object
												IomObject iomPolygon=iomObj.getattrobj(attrName, 0);
												if(iomPolygon!=null){
													// calc geo ref
													Polygon polygon=Iox2jtsext.surface2JTS(iomPolygon, 0.0);
													Coordinate geoRef=polygon.getInteriorPoint().getCoordinate();
													iomObj.changeattrobj(attrName, 0,Jtsext2iox.JTS2coord(geoRef));
												}
											}
											// write main object
											out.write(new ObjectEvent(iomObj));
										}
									}
									// write surface helper tables
									// FORALL surface attrs
									for(AttributeDef attr:surfaceAttrs){
										String attrName=attr.getName();
										String lineTableName=tableQName+"_"+attrName;
										EhiLogger.logState("build linetable "+lineTableName+"...");
										// FORALL main objects
										java.util.Map<String, IomObject> pool=getObjectPool(tableQName);
										for(String poolId : pool.keySet()){
											IomObject iomObj=pool.get(poolId);
											// write line objects
											IomObject polygon=iomObj.getattrobj(attrName, 0);
											if(polygon!=null){
												ArrayList<IomObject> lines=ItfAreaPolygon2Linetable.getLinesFromPolygon(polygon);
												for(IomObject line:lines){
													IomObject lineTableObj=new Iom_jObject(lineTableName, Long.toString(++maxOid));
													String iomAttrName=ch.interlis.iom_j.itf.ModelUtilities.getHelperTableGeomAttrName(attr);
													String fkName=ch.interlis.iom_j.itf.ModelUtilities.getHelperTableMainTableRef(attr);
													lineTableObj.addattrobj(iomAttrName, line);
													IomObject refvalue=lineTableObj.addattrobj(fkName,"REF");
													refvalue.setobjectrefoid(iomObj.getobjectoid());
													out.write(new ObjectEvent(lineTableObj));
												}
											}
										}
									}
								}
							}
							// write saved EndBasketEvent
						  	out.write(endBasketEvents.get(topicQName));
					  }
				  }
				}
			}
			// write EndTransferEvent
			out.write(event);
		}
	}
    java.util.HashMap<String,java.util.Map<String, IomObject>> pools=null;
	private java.util.Map<String, IomObject> getObjectPool(String classQName) throws IoxException {
		java.util.Map<String, IomObject> m=null;
		m=pools.get(classQName);
		if(m==null){
    		m = recman.newObjectPool(new IomObjectSerializer());
			pools.put(classQName,m);
		}
		return m;
		
	}
	private void getPolygonAttrs(AbstractClassDef<?> aclass,ArrayList<AttributeDef> attrs_areaAttrs,ArrayList<AttributeDef> attrs_surfaceAttrs) {
		Iterator<?> attri = aclass.getAttributes ();
		while (attri.hasNext ()){
		  Object attrObj = attri.next();
		  if (attrObj instanceof AttributeDef)
		  {
			AttributeDef attr = (AttributeDef) attrObj;
			Type type = Type.findReal (attr.getDomain());
			if(type instanceof SurfaceType){
				attrs_surfaceAttrs.add(attr);
			}else if(type instanceof AreaType){
				attrs_areaAttrs.add(attr);
			}
		  }
		}
		
		
	}
	public IomObject createIomObject(String type, String oid) throws IoxException {
		return out.createIomObject(type, oid);
	}
	public IoxFactoryCollection getFactory() throws IoxException {
		return out.getFactory();
	}
	public void setFactory(IoxFactoryCollection factory) throws IoxException {
		out.setFactory(factory);
	}

}
