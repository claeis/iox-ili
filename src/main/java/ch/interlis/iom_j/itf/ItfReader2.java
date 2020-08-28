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
import ch.ehi.iox.objpool.ObjectPoolManager;
import ch.ehi.iox.objpool.impl.IomObjectSerializer;
import ch.ehi.iox.objpool.impl.JavaSerializer;
import ch.interlis.iox_j.*;
import ch.interlis.iox_j.logging.Log2EhiLogger;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.ValidationConfig;
import ch.interlis.iox.IoxDataPool;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iom_j.itf.impl.LineobjectIterator;
import ch.interlis.iom_j.itf.impl.ItfAreaLinetable2Polygon;
import ch.interlis.iom_j.itf.impl.ItfSurfaceLinetable2Polygon;
import ch.interlis.iom.*;
import ch.interlis.ili2c.metamodel.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/** This class implements an INTERLIS 1 reader.
 * @author ce
 * @version $Revision: 1.0 $ $Date: 23.06.2006 $
 */
public class ItfReader2 implements ch.interlis.iox.IoxReader,IoxIliReader{

	static final public String SAVED_GEOREF_PREFIX="_itf_";
	static final public int POLYGON_BUILDING_ERRORS_ON=0;
    static final public int POLYGON_BUILDING_ERRORS_OFF=1;
    static final public int POLYGON_BUILDING_ERRORS_WARNING=1;
	
	private ItfReader rawReader=null;
	// maintable object buffer
	private java.util.Map<String,IomObject> currentMainObjs=null;
    private LineobjectIterator currentAreaAttrsIt=null;
    private LineobjectIterator currentSurfaceAttrsIt=null;
	private HashMap<AttributeDef,ItfSurfaceLinetable2Polygon> currentSurfaceAttrs=null;
	private HashMap<AttributeDef,ItfAreaLinetable2Polygon> currentAreaAttrs=null;
	private ObjectPoolManager objPool=null;
	private ArrayList<IoxInvalidDataException> dataerrs=new ArrayList<IoxInvalidDataException>();
	private int ignorePolygonBuildingErrors=POLYGON_BUILDING_ERRORS_ON;
	private boolean readLinetables=false;
	private boolean allowItfAreaHoles=true; // default is like Interlis2 (not exactly according to Interlis1 spec)
	private PipelinePool ioxDataPool=null;
	private LogEventFactory errFact=null;

	private java.util.Set<String> unexpectedLinetables=new java.util.HashSet<String>();
	/** Creates a new reader.
	 * @param in Input stream to read from.
	 * @throws IoxException
	 */
	public ItfReader2(java.io.InputStream in,boolean ignorePolygonBuildingErrors1)
	throws IoxException
	{
		initErrFact(null);
		rawReader=new ItfReader(in,errFact);
		init(ignorePolygonBuildingErrors1);
	}
	public ItfReader2(java.io.InputStream in,LogEventFactory errFact1,boolean ignorePolygonBuildingErrors1)
	throws IoxException
	{
		initErrFact(errFact1);
		rawReader=new ItfReader(in,errFact);
		init(ignorePolygonBuildingErrors1);
	}
	/** Creates a new reader.
	 * @param in File to read from.
	 * @throws IoxException
	 */
	public ItfReader2(java.io.File in,boolean ignorePolygonBuildingErrors1)
	throws IoxException
	{
		initErrFact(null);
		rawReader=new ItfReader(in,errFact);
		init(ignorePolygonBuildingErrors1);
	}
	public ItfReader2(java.io.File in,LogEventFactory errFact1,boolean ignorePolygonBuildingErrors1)
	throws IoxException
	{
		initErrFact(errFact1);
		rawReader=new ItfReader(in,errFact);
		init(ignorePolygonBuildingErrors1);
	}
	private void init(boolean ignorePolygonBuildingErrors1){
		objPool=new ObjectPoolManager();
		ignorePolygonBuildingErrors=ignorePolygonBuildingErrors1?POLYGON_BUILDING_ERRORS_OFF:POLYGON_BUILDING_ERRORS_ON;
	}
	private void initErrFact(LogEventFactory errFact) {
		if(errFact==null){
			this.errFact=new LogEventFactory();
			this.errFact.setLogger(new Log2EhiLogger());
		}else{
			this.errFact=errFact;
			if(errFact.getLogger()==null){
				this.errFact.setLogger(new Log2EhiLogger());
			}
		}
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
		    if(readLinetables) {
		        if(currentAreaAttrsIt.hasNext()) {
		            IomObject lineObj=currentAreaAttrsIt.next();
	                return new ObjectEvent(lineObj);
		        }
		        // return surface linetable objects after main objects
		    }
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
            if(readLinetables) {
                if(currentSurfaceAttrsIt.hasNext()) {
                    IomObject lineObj=currentSurfaceAttrsIt.next();
                    return new ObjectEvent(lineObj);
                }
            }
			closePolygonizers();
			currentMainObjs=null;
			currentSurfaceAttrs=null;
            currentSurfaceAttrsIt=null;
			currentAreaAttrs=null;
            currentAreaAttrsIt=null;
		}

		IoxEvent rawEvent=null;
		while(true) {
			rawEvent=nextEvent();
			if(rawEvent instanceof ObjectEvent){
				rawEvent = handleObject(rawEvent);
				if(rawEvent==null) {
					continue;
				}
				return rawEvent;
			}else if(rawEvent instanceof EndTransferEvent){
			    if(ignorePolygonBuildingErrors!=POLYGON_BUILDING_ERRORS_OFF) {
	                if(dataerrs.size()>0){
	                    for(IoxInvalidDataException dataerr:dataerrs){
	                        if(ignorePolygonBuildingErrors==POLYGON_BUILDING_ERRORS_WARNING) {
	                            errFact.addEvent(errFact.logWarning(dataerr));
	                        }else {
	                            errFact.addEvent(errFact.logError(dataerr));
	                        }
	                    }
	                    if(ignorePolygonBuildingErrors==POLYGON_BUILDING_ERRORS_ON) {
	                        throw new IoxInvalidDataException("failed to build polygons");
	                    }else if(ignorePolygonBuildingErrors==POLYGON_BUILDING_ERRORS_WARNING) {
                                errFact.addEvent(errFact.logWarning(new IoxInvalidDataException("failed to build polygons")));
	                    }
	                }
			    }
			}
			break;
		}
		return rawEvent;
	}
	protected IoxEvent handleObject(IoxEvent rawEvent) throws IoxException {
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
				if(polygonizer==null) {
					if(!unexpectedLinetables.contains(iliQName)) {
						dataerrs.add( new IoxInvalidDataException("unexpected linetable "+iliQName));
						unexpectedLinetables.add(iliQName);
					}
					ItfSurfaceLinetable2Polygon surfacePolygonizer=currentSurfaceAttrs.get(surfaceOrAreaAttr);
					if(surfacePolygonizer!=null) {
						surfacePolygonizer.addItfLinetableObject(rawObj);
					}
					return null;
				}
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
			currentMainObjs=objPool.newObjectPoolImpl2(new IomObjectSerializer());
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
			// FOR all area attrs: polygonize
			for(AttributeDef areaAttr : currentAreaAttrs.keySet()){
				String areaAttrName=areaAttr.getName();
				setTopologyValidationDone(areaAttr);
				ItfAreaLinetable2Polygon polygonizer=currentAreaAttrs.get(areaAttr);
				try {
					polygonizer.buildSurfaces();
					ArrayList<IoxInvalidDataException> dataerrs2 = polygonizer.getDataerrs();
					dataerrs.addAll(dataerrs2);
					if(dataerrs2.size()>0){
	                    dataerrs.add(new IoxInvalidDataException("failed to build polygons of "+aclass.getScopedName(null)+"."+areaAttrName));
						setTopologyValidationFailed(areaAttr);
					}
				} catch (Exception e) {
					ArrayList<IoxInvalidDataException> dataerrs2 = polygonizer.getDataerrs();
					dataerrs.addAll(dataerrs2);
					dataerrs.add(new IoxInvalidDataException("failed to build polygons of "+aclass.getScopedName(null)+"."+areaAttrName,e));
					setTopologyValidationFailed(areaAttr);
					continue;
				}
			}
			// FOR all surface attrs: polygonize
			for(AttributeDef surfaceAttr : currentSurfaceAttrs.keySet()){
				String surfaceAttrName=surfaceAttr.getName();
				setTopologyValidationDone(surfaceAttr);
				ItfSurfaceLinetable2Polygon polygonizer=currentSurfaceAttrs.get(surfaceAttr);
				try {
					polygonizer.buildSurfaces();
					ArrayList<IoxInvalidDataException> dataerrs2 = polygonizer.getDataerrs();
					dataerrs.addAll(dataerrs2);
					if(dataerrs2.size()>0){
						setTopologyValidationFailed(surfaceAttr);
	                    dataerrs.add( new IoxInvalidDataException("failed to build polygons of "+aclass.getScopedName(null)+"."+surfaceAttrName));
					}
				} catch (Exception e) {
					ArrayList<IoxInvalidDataException> dataerrs2 = polygonizer.getDataerrs();
					dataerrs.addAll(dataerrs2);
					dataerrs.add( new IoxInvalidDataException("failed to build polygons of "+aclass.getScopedName(null)+"."+surfaceAttrName,e));
					setTopologyValidationFailed(surfaceAttr);
					continue;
				}
			}
			if(currentMainObjs.keySet().size()==0) {
				dataerrs.add( new IoxInvalidDataException("no main objects of "+aclass.getScopedName()+" but linetable objects"));
				return null;
			}
			if(readLinetables) {
	            currentAreaAttrsIt=new LineobjectIterator(currentAreaAttrs);
                currentSurfaceAttrsIt=new LineobjectIterator(currentSurfaceAttrs);
	            if(currentAreaAttrsIt.hasNext()) {
	                IomObject lineObj=currentAreaAttrsIt.next();
	                return new ObjectEvent(lineObj);
	            }
	            // do not return surface line table object here
	            // return surface linetable objects after main objects
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
		return rawEvent;
	}
	private void setTopologyValidationDone(AttributeDef attr) {
		if(ioxDataPool!=null){
			ioxDataPool.setIntermediateValue(attr, ValidationConfig.TOPOLOGY, this);
		}
	}
	private void setTopologyValidationFailed(AttributeDef attr) {
		if(ioxDataPool!=null){
			ioxDataPool.setIntermediateValue(attr, ValidationConfig.TOPOLOGY_VALIDATION_OK, new Boolean(false));
		}
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
		if(georef!=null){
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
			    ItfSurfaceLinetable2Polygon polygonBuilder=new ItfSurfaceLinetable2Polygon(attr,ignorePolygonBuildingErrors);
                polygonBuilder.setKeepLinetables(readLinetables,ModelUtilities.getHelperTableMainTableRef(attr),ModelUtilities.getHelperTableMainTableRef2(attr));
				attrs_surfaceAttrs.put(attr, polygonBuilder);
			}else if(type instanceof AreaType){
				ItfAreaLinetable2Polygon polygonBuilder = new ItfAreaLinetable2Polygon(attr,ignorePolygonBuildingErrors);
				polygonBuilder.setAllowItfAreaHoles(allowItfAreaHoles);
                polygonBuilder.setKeepLinetables(readLinetables,ModelUtilities.getHelperTableMainTableRef(attr),ModelUtilities.getHelperTableMainTableRef2(attr));
				attrs_areaAttrs.put(attr, polygonBuilder);
			}
		  }
		}
		
		
	}
	@Override
	public void setModel(TransferDescription td)
	{
		rawReader.setModel(td);
	}
	public void setBidPrefix(String prefix)
	{
		rawReader.setBidPrefix(prefix);
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
    public void setReadLinetables(boolean val){
        readLinetables=val;
    }
    public boolean isReadLinetables(){
        return readLinetables;
    }

	public ArrayList<IoxInvalidDataException> getDataErrs()
	{
		return dataerrs;
	}
	public void clearDataErrs()
	{
		dataerrs.clear();
	}
	public IoxDataPool getIoxDataPool() {
		return ioxDataPool;
	}
	public void setIoxDataPool(IoxDataPool ioxDataPool) {
		this.ioxDataPool = (PipelinePool)ioxDataPool;
	}
	public boolean isAllowItfAreaHoles() {
		return allowItfAreaHoles;
	}
	public void setAllowItfAreaHoles(boolean allowItfAreaHoles) {
		this.allowItfAreaHoles = allowItfAreaHoles;
	}
    public int getIgnorePolygonBuildingErrors() {
        return ignorePolygonBuildingErrors;
    }
    public void setIgnorePolygonBuildingErrors(int ignorePolygonBuildingErrors) {
        this.ignorePolygonBuildingErrors = ignorePolygonBuildingErrors;
    }
    @Override
    public void setTopicFilter(String[] topicNames) {
        rawReader.setTopicFilter(topicNames);
    }
    @Override
    public String getMimeType() {
        return ITF_10;
    }
}
