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


import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iox_j.*;
import ch.interlis.iox_j.jts.Iox2jtsException;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iom_j.itf.impl.ItfAreaLinetable2Polygon;
import ch.interlis.iom_j.itf.impl.ItfLineCursor;
import ch.interlis.iom_j.itf.impl.ItfLineKind;
import ch.interlis.iom_j.itf.impl.ItfScanner;
import ch.interlis.iom_j.itf.impl.ItfSurfaceLinetable2Polygon;
import ch.interlis.iom_j.itf.impl.JdbmUtility;
import ch.interlis.iom_j.itf.impl.ObjectPoolManager;
import ch.interlis.iom.*;
import ch.interlis.ili2c.metamodel.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;

import javax.smartcardio.ATR;

import jdbm.PrimaryTreeMap;
import jdbm.RecordManager;

/** This class implements an INTERLIS 1 reader.
 * @author ce
 * @version $Revision: 1.0 $ $Date: 23.06.2006 $
 */
public class ItfReader2 implements ch.interlis.iox.IoxReader{

	static final public String SAVED_GEOREF_PREFIX="_itf_";
	
	private ItfReader rawReader=null;
	// maintable object buffer
	private java.util.Map<String,IomObject> currentMainObjs=null;
	private HashMap<AttributeDef,ItfSurfaceLinetable2Polygon> currentSurfaceAttrs=null;
	private HashMap<AttributeDef,ItfAreaLinetable2Polygon> currentAreaAttrs=null;
	private ObjectPoolManager objPool=null;
	private ArrayList<IoxInvalidDataException> dataerrs=new ArrayList<IoxInvalidDataException>();
	private boolean ignorePolygonBuildingErrors=false;
	/** Creates a new reader.
	 * @param in Input stream to read from.
	 * @throws IoxException
	 */
	public ItfReader2(java.io.InputStream in,boolean ignorePolygonBuildingErrors1)
	throws IoxException
	{
		rawReader=new ItfReader(in);
		init(ignorePolygonBuildingErrors1);
	}
	/** Creates a new reader.
	 * @param in File to read from.
	 * @throws IoxException
	 */
	public ItfReader2(java.io.File in,boolean ignorePolygonBuildingErrors1)
	throws IoxException
	{
		rawReader=new ItfReader(in);
		init(ignorePolygonBuildingErrors1);
	}
	private void init(boolean ignorePolygonBuildingErrors1){
		objPool=new ObjectPoolManager<IomObject>();
		ignorePolygonBuildingErrors=ignorePolygonBuildingErrors1;
	}
	@Override
	public void close() throws IoxException {
		if(rawReader!=null){
			rawReader.close();
		}
		rawReader=null;
		closePolygonizers();
		if(objPool!=null){
			objPool.close();
			objPool=null;
		}
	}
	@Override
	public IomObject createIomObject(String type, String oid)
			throws IoxException {
		return rawReader.createIomObject(type, oid);
	}
	@Override
	public IoxFactoryCollection getFactory() throws IoxException {
		return rawReader.getFactory();
	}
	@Override
	public void setFactory(IoxFactoryCollection factory) throws IoxException {
		rawReader.setFactory(factory);
		
	}
	@Override
	public IoxEvent read() throws IoxException {
		
		// more buffered main objects?
		if(currentMainObjs!=null){
			if(currentMainObjs.size()>0){
				String nextTid=currentMainObjs.keySet().iterator().next();
				IomObject nextObj=currentMainObjs.get(nextTid);
				currentMainObjs.remove(nextTid);
				// FOR all area attrs: add polygon to main object
				for(AttributeDef areaAttr : currentAreaAttrs.keySet()){
					String areaAttrName=areaAttr.getName();
					ItfAreaLinetable2Polygon polygonizer=currentAreaAttrs.get(areaAttr);
					mergeAreaGeomToMainObj(areaAttrName, polygonizer, nextTid,nextObj);
				}
				// FOR all surface attrs: add polygon to main object
				for(AttributeDef surfaceAttr : currentSurfaceAttrs.keySet()){
					String surfaceAttrName=surfaceAttr.getName();
					ItfSurfaceLinetable2Polygon polygonizer=currentSurfaceAttrs.get(surfaceAttr);
					mergeSurfaceGeomToMainObj(surfaceAttrName, polygonizer,nextTid, nextObj);
				}
				return new ObjectEvent(nextObj);
			}
			closePolygonizers();
			currentMainObjs=null;
			currentSurfaceAttrs=null;
			currentAreaAttrs=null;
		}

		IoxEvent rawEvent=nextEvent();
		if(rawEvent instanceof ObjectEvent){
			IomObject rawObj=((ObjectEvent)rawEvent).getIomObject();
			String iliQName=rawObj.getobjecttag();
			Object aclassObj=rawReader.mapIliQName2Class(iliQName);
			AbstractClassDef aclass=null;
			AttributeDef surfaceOrAreaAttr=null;
			if(aclassObj instanceof AbstractClassDef){
				// main table
				aclass=(AbstractClassDef)aclassObj;
			}else{
				// SURFACE or AREA helper table
				surfaceOrAreaAttr=(AttributeDef)aclassObj;
				aclass=(AbstractClassDef)surfaceOrAreaAttr.getContainer();
			}
			currentSurfaceAttrs=getSurfaceAttrs(aclass);
			currentAreaAttrs=getAreaAttrs(aclass);
			// IF no SURFACE or AREA attributes?
			if(currentSurfaceAttrs.size()==0 && currentAreaAttrs.size()==0){
				// no buffering required
			}else{
				// main object read?
				if(surfaceOrAreaAttr==null){
					// push back object
					pushBackEvent(rawEvent);
				}
				// WHILE another area linetable
				while(surfaceOrAreaAttr!=null){
					// collect linetable objects
					ItfAreaLinetable2Polygon polygonizer=currentAreaAttrs.get(surfaceOrAreaAttr);
					while(true){
						polygonizer.addItfLinetableObject(rawObj);
						rawEvent=nextEvent();
						if(rawEvent instanceof ObjectEvent){
							rawObj=((ObjectEvent)rawEvent).getIomObject();
							// different linetable or maintable object?
							if(!iliQName.equals(rawObj.getobjecttag())){
								iliQName=rawObj.getobjecttag();
								break;
							}
						}else{
							rawObj=null;
							iliQName=null;
							// push back object
							pushBackEvent(rawEvent);
							break;
						}
					}
					surfaceOrAreaAttr=null;
					if(iliQName!=null){
						aclassObj=rawReader.mapIliQName2Class(iliQName);
						if(aclassObj instanceof AbstractClassDef){
							// main table
							if(aclassObj!=aclass){
								throw new IoxException("unexpected table "+iliQName);
							}
							// push back main table object
							pushBackEvent(rawEvent);
						}else{
							// next AREA helper table
							surfaceOrAreaAttr=(AttributeDef)aclassObj;
							if(surfaceOrAreaAttr.getContainer()!=aclass){
								throw new IoxException("unexpected linetable "+iliQName);
							}
						}
					}
				}
				// maintable
				surfaceOrAreaAttr=null;
				//mainObjs=new HashMap<String,IomObject>();
				currentMainObjs=objPool.newObjectPool();
				iliQName=aclass.getScopedName(null);
				while(true){
					  // collect objects
					rawEvent=nextEvent();
					if(rawEvent instanceof ObjectEvent){
						rawObj=((ObjectEvent)rawEvent).getIomObject();
						if(!iliQName.equals(rawObj.getobjecttag())){
							// object from different table; no longer main table
							aclassObj=rawReader.mapIliQName2Class(rawObj.getobjecttag());
							if(aclassObj instanceof AbstractClassDef){
								// another main table
								pushBackEvent(rawEvent);
							}else{
								// next SURFACE helper table or AREA helper table of new main table
								surfaceOrAreaAttr=(AttributeDef)aclassObj;
								if(surfaceOrAreaAttr.getContainer()!=aclass){
									// area helper table of another main table
									surfaceOrAreaAttr=null;
									pushBackEvent(rawEvent);
								}else{
									iliQName=rawObj.getobjecttag();
								}
							}
							break;
						}else{
							String oid=rawObj.getobjectoid();
							currentMainObjs.put(oid,rawObj);
							// FOR all area attrs: add georef to polygonizer
							for(AttributeDef areaAttr : currentAreaAttrs.keySet()){
								ItfAreaLinetable2Polygon polygonizer=currentAreaAttrs.get(areaAttr);
								IomObject georef=rawObj.getattrobj(areaAttr.getName(), 0);
								if(georef!=null){
									polygonizer.addGeoRef(oid, georef);
								}
							}
							// FOR all surface attrs: add mainobj-tid to polygonizer
							for(AttributeDef surfaceAttr : currentSurfaceAttrs.keySet()){
								ItfSurfaceLinetable2Polygon polygonizer=currentSurfaceAttrs.get(surfaceAttr);
								polygonizer.addMainObjectTid(oid);
							}
						}
					}else{
						pushBackEvent(rawEvent);
						break;
					}
				}
				// WHILE another surface linetable
				while(surfaceOrAreaAttr!=null){
					// collect linetable objects
					ItfSurfaceLinetable2Polygon polygonizer=currentSurfaceAttrs.get(surfaceOrAreaAttr);
					while(true){
						polygonizer.addItfLinetableObject(rawObj);
						rawEvent=nextEvent();
						if(rawEvent instanceof ObjectEvent){
							rawObj=((ObjectEvent)rawEvent).getIomObject();
							if(!iliQName.equals(rawObj.getobjecttag())){
								// object from another table
								iliQName=rawObj.getobjecttag();
								break;
							}
						}else{
							rawObj=null;
							iliQName=null;
							pushBackEvent(rawEvent);
							break;
						}
					}
					surfaceOrAreaAttr=null;
					if(iliQName!=null){
						aclassObj=rawReader.mapIliQName2Class(iliQName);
						if(aclassObj instanceof AbstractClassDef){
							// another main table
							surfaceOrAreaAttr=null;
							pushBackEvent(rawEvent);
							break;
						}else{
							// next SURFACE helper table or AREA helper table of new main table
							surfaceOrAreaAttr=(AttributeDef)aclassObj;
							if(surfaceOrAreaAttr.getContainer()!=aclass){
								// area helper table of another main table
								surfaceOrAreaAttr=null;
								pushBackEvent(rawEvent);
								break;
							}
						}
					}
				}
				// polygonize all collected linetables
				// FOR all area attrs: polygonize and add polygon to main object
				for(AttributeDef areaAttr : currentAreaAttrs.keySet()){
					String areaAttrName=areaAttr.getName();
					ItfAreaLinetable2Polygon polygonizer=currentAreaAttrs.get(areaAttr);
					try {
						polygonizer.buildSurfaces();
						dataerrs.addAll(polygonizer.getDataerrs());
					} catch (IoxInvalidDataException e) {
						dataerrs.addAll(polygonizer.getDataerrs());
						dataerrs.add(new IoxInvalidDataException("failed to build polygons of "+aclass.getScopedName(null)+"."+areaAttrName,e));
						continue;
					}
				}
				// FOR all surface attrs: polygonize and add polygon to main object
				for(AttributeDef surfaceAttr : currentSurfaceAttrs.keySet()){
					String surfaceAttrName=surfaceAttr.getName();
					ItfSurfaceLinetable2Polygon polygonizer=currentSurfaceAttrs.get(surfaceAttr);
					try {
						polygonizer.buildSurfaces();
						dataerrs.addAll(polygonizer.getDataerrs());
					} catch (IoxInvalidDataException e) {
						dataerrs.addAll(polygonizer.getDataerrs());
						dataerrs.add( new IoxInvalidDataException("failed to build polygons of "+aclass.getScopedName(null)+"."+surfaceAttrName,e));
						continue;
					}
				}
				String nextTid=currentMainObjs.keySet().iterator().next();
				IomObject nextObj=currentMainObjs.get(nextTid);
				currentMainObjs.remove(nextTid);
				// FOR all area attrs: add polygon to main object
				for(AttributeDef areaAttr : currentAreaAttrs.keySet()){
					String areaAttrName=areaAttr.getName();
					ItfAreaLinetable2Polygon polygonizer=currentAreaAttrs.get(areaAttr);
					mergeAreaGeomToMainObj(areaAttrName, polygonizer, nextTid,nextObj);
				}
				// FOR all surface attrs: add polygon to main object
				for(AttributeDef surfaceAttr : currentSurfaceAttrs.keySet()){
					String surfaceAttrName=surfaceAttr.getName();
					ItfSurfaceLinetable2Polygon polygonizer=currentSurfaceAttrs.get(surfaceAttr);
					mergeSurfaceGeomToMainObj(surfaceAttrName, polygonizer,nextTid, nextObj);
				}
				return new ObjectEvent(nextObj);
			}
			
			
		}else if(rawEvent instanceof EndTransferEvent){
			if(dataerrs.size()>0){
        		for(IoxInvalidDataException dataerr:dataerrs){
        			EhiLogger.logError(dataerr);
        		}
				throw new IoxInvalidDataException("failed to build polygons");
			}
		}
		return rawEvent;
	}
	private void closePolygonizers() {
		if(currentAreaAttrs!=null){
			// FOR all area attrs: cleanup
			for(AttributeDef areaAttr : currentAreaAttrs.keySet()){
				String areaAttrName=areaAttr.getName();
				ItfAreaLinetable2Polygon polygonizer=currentAreaAttrs.get(areaAttr);
				polygonizer.close();
			}
			
		}
		if(currentSurfaceAttrs!=null){
			// FOR all surface attrs: cleanup
			for(AttributeDef surfaceAttr : currentSurfaceAttrs.keySet()){
				String surfaceAttrName=surfaceAttr.getName();
				ItfSurfaceLinetable2Polygon polygonizer=currentSurfaceAttrs.get(surfaceAttr);
				polygonizer.close();
			}
		}
	}
	private void mergeSurfaceGeomToMainObj(String surfaceAttrName,
			ItfSurfaceLinetable2Polygon polygonizer, String mainObjOid,
			IomObject mainObj) throws IoxException {
		IomObject polygon=null;
		try {
			polygon = polygonizer.getSurfaceObject(mainObjOid);
			if(polygon!=null){
				mainObj.addattrobj(surfaceAttrName, polygon);
			}
		} catch (IoxInvalidDataException e) {
			dataerrs.add(e);
		}
	}
	private void mergeAreaGeomToMainObj(String areaAttrName,
			ItfAreaLinetable2Polygon polygonizer, String mainObjOid,
			IomObject mainObj) throws IoxException {
		IomObject georef=mainObj.getattrobj(areaAttrName, 0);
		mainObj.deleteattrobj(areaAttrName, 0);
		mainObj.addattrobj(SAVED_GEOREF_PREFIX+areaAttrName, georef);
		IomObject polygon=null;
		try {
			polygon = polygonizer.getSurfaceObject(mainObjOid);
			if(polygon!=null){
				mainObj.addattrobj(areaAttrName, polygon);
			}
		} catch (IoxInvalidDataException e) {
			dataerrs.add(e);
		}
	}
	private IoxEvent _next_Event=null;
	private void pushBackEvent(IoxEvent rawEvent) {
		if(_next_Event!=null){
			throw new IllegalStateException("more than one push back event");
		}
		_next_Event=rawEvent;
	}
	private IoxEvent nextEvent() throws IoxException {
		if(_next_Event==null){
			return rawReader.read();
		}
		IoxEvent rawEvent=_next_Event;
		_next_Event=null;
		return rawEvent;
	}
	private AbstractClassDef attrs_currentClass=null;
	private HashMap<AttributeDef, ItfAreaLinetable2Polygon> attrs_areaAttrs=null;
	private HashMap<AttributeDef, ItfSurfaceLinetable2Polygon> attrs_surfaceAttrs=null;

	private HashMap<AttributeDef, ItfAreaLinetable2Polygon> getAreaAttrs(
			AbstractClassDef aclass) {
		if(attrs_currentClass!=aclass){
			initAttrs(aclass);
		}
		return attrs_areaAttrs;
	}
	private HashMap<AttributeDef, ItfSurfaceLinetable2Polygon> getSurfaceAttrs(
			AbstractClassDef aclass) {
		if(attrs_currentClass!=aclass){
			initAttrs(aclass);
		}
		return attrs_surfaceAttrs;
	}
	private void initAttrs(AbstractClassDef<?> aclass) {
		attrs_currentClass=aclass;
		attrs_areaAttrs=new HashMap<AttributeDef, ItfAreaLinetable2Polygon>();
		attrs_surfaceAttrs=new HashMap<AttributeDef, ItfSurfaceLinetable2Polygon>();
		Iterator<?> attri = aclass.getAttributes ();
		while (attri.hasNext ()){
		  Object attrObj = attri.next();
		  if (attrObj instanceof AttributeDef)
		  {
			AttributeDef attr = (AttributeDef) attrObj;
			Type type = Type.findReal (attr.getDomain());
			if(type instanceof SurfaceType){
				attrs_surfaceAttrs.put(attr, new ItfSurfaceLinetable2Polygon(attr,ignorePolygonBuildingErrors));
			}else if(type instanceof AreaType){
				attrs_areaAttrs.put(attr, new ItfAreaLinetable2Polygon(attr,ignorePolygonBuildingErrors));
			}
		  }
		}
		
		
	}
	public void setModel(TransferDescription td)
	{
		rawReader.setModel(td);
	}
	public boolean isRenumberTids() {
		return rawReader.isRenumberTids();
	}
	public void setRenumberTids(boolean renumberTids) {
		rawReader.setRenumberTids(renumberTids);
	}
	public void setReadEnumValAsItfCode(boolean val){
		rawReader.setReadEnumValAsItfCode(val);
	}
	public boolean isReadEnumValAsItfCode(){
		return rawReader.isReadEnumValAsItfCode();
	}

	public ArrayList<IoxInvalidDataException> getDataErrs()
	{
		return dataerrs;
	}
	public void clearDataErrs()
	{
		dataerrs.clear();
	}
}
