package ch.interlis.iom_j.itf.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.vividsolutions.jts.algorithm.locate.SimplePointInAreaLocator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Location;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.TopologyException;
import com.vividsolutions.jts.index.strtree.STRtree;
import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.iox.objpool.ObjectPoolManager;
import ch.ehi.iox.objpool.impl.IomObjectSerializer;
import ch.ehi.iox.objpool.impl.JavaSerializer;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.AreaType;
import ch.interlis.ili2c.metamodel.CoordType;
import ch.interlis.ili2c.metamodel.NumericType;
import ch.interlis.ili2c.metamodel.NumericalType;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iom_j.itf.ItfReader2;
import ch.interlis.iom_j.itf.ModelUtilities;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurveRing;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurvePolygon;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;
import ch.interlis.iom_j.itf.impl.jtsext.noding.CompoundCurveNoder;
import ch.interlis.iom_j.itf.impl.jtsext.noding.Intersection;
import ch.interlis.iom_j.itf.impl.jtsext.operation.polygonize.IoxPolygonizer;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.IoxInvalidDataException;
import ch.interlis.iox_j.jts.Iox2jtsException;
import ch.interlis.iox_j.jts.Iox2jtsext;
import ch.interlis.iox_j.jts.Jtsext2iox;


/*
FORALL: Line-IomObject    
	save Line to Line-Pool    
FORALL: Main-IomObject
 	get all Lines from Line-Pool
 	build rings
 	save polygon to polygon-pool
 */
/* AREA:
FORALL: alle Linien
	Raender ermitteln wie fuer SURFACE
	
com.vividsolutions.jts.operation.polygonize.Polygonizer um Polygone zu ermitteln
System.out.println(point.within(polygon));
use STRTree and PreparedPolygon to implement Point-In-Polygon test

*/	

public class ItfAreaLinetable2Polygon implements Linetable2Polygon {
	private Map<String,Polygon> polygons=new HashMap<String,Polygon>();
	private Map<String,IomObject> mainTids=new HashMap<String,IomObject>();
	private Map<String,IomObject> lines=null; 
    private Map<String,IomObject> lineattrs=null; 
	private boolean surfacesBuilt=false;
	private String helperTableGeomAttrName=null;
	private Table linattrTab=null;
	private double maxOverlaps=0.0;
	private double newVertexOffset=0.0;
	private JtsextGeometryFactory jtsFact=new JtsextGeometryFactory();
	private ObjectPoolManager objPool = null;
	private int ignorePolygonBuildingErrors;
	ArrayList<IoxInvalidDataException> dataerrs=new ArrayList<IoxInvalidDataException>(); 
	private String linetableIliqname=null;
	private String geomattrIliqname=null;
	private boolean allowItfAreaHoles=true; // default is like Interlis2 (not exactly according to Interlis1 spec)
    private boolean keepLinetables=false;
    private String mainTableRef1=null;
    private String mainTableRef2=null;

    public ItfAreaLinetable2Polygon(AttributeDef surfaceAttr,boolean ignorePolygonBuildingErrors1)
    {
        this(surfaceAttr,ignorePolygonBuildingErrors1?ItfReader2.POLYGON_BUILDING_ERRORS_OFF:ItfReader2.POLYGON_BUILDING_ERRORS_ON);
    }
	public ItfAreaLinetable2Polygon(AttributeDef surfaceAttr,int ignorePolygonBuildingErrors1)
	{
		linetableIliqname=surfaceAttr.getContainer().getScopedName(null)+"_"+surfaceAttr.getName();
		geomattrIliqname=surfaceAttr.getContainer().getScopedName(null)+"."+surfaceAttr.getName();
        ignorePolygonBuildingErrors=ignorePolygonBuildingErrors1;
		maxOverlaps=((AreaType)surfaceAttr.getDomainResolvingAliases()).getMaxOverlap().doubleValue();
		if(maxOverlaps>0){
		    NumericalType[] dimensions = ((CoordType) ((AreaType)surfaceAttr.getDomainResolvingAliases()).getControlPointDomain().getType()).getDimensions();
			double size=((NumericType)dimensions[0]).getMinimum().getAccuracy();
			if(size>0){
				newVertexOffset=2*Math.pow(10, -size);
			}
		}
		linattrTab=((AreaType)surfaceAttr.getDomainResolvingAliases()).getLineAttributeStructure();
		helperTableGeomAttrName=ch.interlis.iom_j.itf.ModelUtilities.getHelperTableGeomAttrName(surfaceAttr);
		objPool=new ObjectPoolManager();
	}
	public ItfAreaLinetable2Polygon(String geomAttr)
	{
		helperTableGeomAttrName=geomAttr;
		objPool=new ObjectPoolManager();
		
	}
	public ItfAreaLinetable2Polygon(String geomAttr,double maxOverlaps1,double accuracy)
	{
		helperTableGeomAttrName=geomAttr;
		objPool=new ObjectPoolManager();
		maxOverlaps=maxOverlaps1;
		if(maxOverlaps>0){
			if(accuracy>0){
				newVertexOffset=2*Math.pow(10, -accuracy);
			}
		}
		
	}
	@Override
	public void close()
	{
        lines=null;
        lineattrs=null;
        mainTids=null;
		if(objPool!=null){
			objPool.close();
			objPool=null;
		}
	}
	@Override
	public void addItfLinetableObject(IomObject iomObj)
	{
		if(lines==null){
			lines=objPool.newObjectPoolImpl2(new IomObjectSerializer());
            lineattrs=objPool.newObjectPoolImpl2(new IomObjectSerializer());
		}
		IomObject polyline=iomObj.getattrobj(helperTableGeomAttrName, 0);
		if(polyline==null){
			dataerrs.add(new IoxInvalidDataException("empty line",linetableIliqname,iomObj.getobjectoid(),iomObj));
		}else{
			lines.put(iomObj.getobjectoid(),iomObj);
			Iom_jObject lineattr=new Iom_jObject(iomObj);
			lineattr.setattrundefined(helperTableGeomAttrName);
            lineattrs.put(iomObj.getobjectoid(),lineattr);
		}
	}
	public void addGeoRef(String tid,IomObject iomCoord)
	{
		mainTids.put(tid, iomCoord);
	}
	@Override
	public Iterator<String> mainTableTidIterator()
	{
		return mainTids.keySet().iterator();
	}
	@Override
    public Iterator<String> lineTableTidIterator()
    {
        if(!keepLinetables || lines==null) {
            throw new IllegalStateException("no linetable kept");
        }
        return lines.keySet().iterator();
    }
	@Override
	public IomObject getSurfaceObject(String mainObjectTid) throws IoxException
	{
		if(!surfacesBuilt){
			buildSurfaces();
		}
		if(polygons.containsKey(mainObjectTid)){
			Polygon poly=polygons.get(mainObjectTid);
			poly.normalize();
			try {
				return Jtsext2iox.JTS2surface(poly);
			} catch (Iox2jtsException e) {
				throw new IoxException(e);
			}
		}
		return null;
	}
	  private static long getUsedMemory()
	  {
	      System.gc();
	      return Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory();
	  }  
	@Override
	public void buildSurfaces() throws IoxException
	{
			surfacesBuilt=true;
			// optional AREA, no lines at all?
			if(lines==null){
				return;
			}
			//long startMem=getUsedMemory();
			LineSet lineset=new LineSet(false,linattrTab,helperTableGeomAttrName);

			ArrayList<CompoundCurve> segv=lineset.buildBoundaries(lines,jtsFact);
			lineset=null;
            lines=null;     

			EhiLogger.traceState("validate noding..."+helperTableGeomAttrName+", maxOverlaps "+maxOverlaps+", offset "+newVertexOffset);
			for(CompoundCurve seg : segv){
				ItfSurfaceLinetable2Polygon.removeValidSelfIntersections(seg,maxOverlaps,newVertexOffset);
			}
			
				CompoundCurveNoder validator=new CompoundCurveNoder(segv,false);
				if(!validator.isValid()){
					boolean hasIntersections=false;
					for(Intersection is:validator.getIntersections()){
						CompoundCurve e0=is.getCurve1();
						CompoundCurve e1=is.getCurve2();
						CurveSegment seg0=is.getSegment1();
						CurveSegment seg1=is.getSegment2();
						int segIndex0=e0.getSegments().indexOf(is.getSegment1());
						int segIndex1=e1.getSegments().indexOf(is.getSegment2());
						Coordinate p00;
						Coordinate p01;
						Coordinate p10;
						Coordinate p11;
						p00 = e0.getSegments().get(segIndex0).getStartPoint();
						p01 = e0.getSegments().get(segIndex0).getEndPoint();
						p10 = e1.getSegments().get(segIndex1).getStartPoint();
						p11 = e1.getSegments().get(segIndex1).getEndPoint();
						if(is.isOverlay()) {
							dataerrs.add(new IoxInvalidDataException(is.toShortString(),linetableIliqname,null,Jtsext2iox.JTS2coord(is.getPt()[0])));
							hasIntersections=true;
						}else if(e0!=e1 && 
								(segIndex0==0 || segIndex0==e0.getSegments().size()-1) 
								&& (segIndex1==0 || segIndex1==e1.getSegments().size()-1) 
								&& is.getOverlap()!=null && is.getOverlap()<maxOverlaps){
							// Ende- bzw. Anfangs-Segment verschiedener Linien
							// valid overlap, ignore for now, will be removed later in IoxPolygonizer
						}else if(e0==e1 && (
								  Math.abs(segIndex0-segIndex1)==1 
								  || Math.abs(segIndex0-segIndex1)==e0.getNumSegments()-1  ) // bei Ring: letztes Segment und Erstes Segment
								  && (is.isIntersection(p00) || is.isIntersection(p01))
								  && (is.isIntersection(p10) || is.isIntersection(p11))
								  && is.getOverlap()!=null && is.getOverlap()<maxOverlaps){
							// aufeinanderfolgende Segmente der selben Linie
							throw new IllegalStateException("unexpected overlap; should have been removed before; "+is);
						}else{
							dataerrs.add(new IoxInvalidDataException(is.toShortString(),linetableIliqname,null,Jtsext2iox.JTS2coord(is.getPt()[0])));
							hasIntersections=true;
						}
					}
					if(hasIntersections){
						throw new IoxInvalidDataException("intersections");
					}
				}
			
			EhiLogger.traceState("polygonize..."+helperTableGeomAttrName);
			//System.out.println("polygonizer.lines");
			//Polygonizer polygonizer=new Polygonizer();
			IoxPolygonizer polygonizer=new IoxPolygonizer(newVertexOffset);
			//for(CompoundCurve boundary:segv){
			if(keepLinetables) {
	            lines=objPool.newObjectPoolImpl2(new IomObjectSerializer());
			}
            try {
                HashMap<String,Integer> tidCount=new HashMap<String,Integer>();
                HashMap<String,Integer> tidIdxs=new HashMap<String,Integer>();
                for(CompoundCurve boundary:validator.getNodedSubstrings()) {
                    String tid=(String)boundary.getUserData();
                    Integer count=tidCount.get(tid);
                    if(count==null) {
                        tidCount.put(tid, 1);
                        tidIdxs.put(tid, 1);
                    }else {
                        count+=1;
                        tidCount.put(tid, count);
                    }
                }
                for(CompoundCurve boundary:validator.getNodedSubstrings()){
                    //System.out.println(boundary);
                    String lineTid=(String)boundary.getUserData();
                    int tidIdx=tidIdxs.get(lineTid);
                    tidIdxs.put(lineTid,tidIdx+1);
                    if(tidCount.get(lineTid)>1) {
                        lineTid=lineTid+":"+tidIdx;
                    }
                    if(keepLinetables) {
                        IomObject iomLine=Jtsext2iox.JTS2polyline(boundary);
                        IomObject iomLinetableObj=new Iom_jObject(linetableIliqname,lineTid);
                        iomLinetableObj.addattrobj(helperTableGeomAttrName, iomLine);
                        lines.put(lineTid,iomLinetableObj);
                    }
                    boundary.setUserData(lineTid);
                    boundary.setSegmentsUserData(lineTid);
                    polygonizer.add(boundary);
                }
            } catch (Iox2jtsException e) {
                throw new IllegalStateException(e);
            }
			validator=null;
			Collection cutEdges = polygonizer.getCutEdges();
			if(!cutEdges.isEmpty()){
				for(Object edge:cutEdges){
					try {
						dataerrs.add(new IoxInvalidDataException("cut edge "+IoxInvalidDataException.formatTids((CompoundCurve) edge),linetableIliqname,null,Jtsext2iox.JTS2polyline((CompoundCurve)edge)));
					} catch (Iox2jtsException e) {
						throw new IllegalStateException(e);
					}
				}
				if(ignorePolygonBuildingErrors==ItfReader2.POLYGON_BUILDING_ERRORS_ON){
					throw new IoxInvalidDataException("cut edges");
				}
			}
			Collection dangles=polygonizer.getDangles();
			if(!dangles.isEmpty()){
				for(Object dangle:dangles){
						try {
							dataerrs.add(new IoxInvalidDataException("dangle "+IoxInvalidDataException.formatTids((CompoundCurve) dangle),linetableIliqname,null,Jtsext2iox.JTS2polyline((CompoundCurve)dangle)));
						} catch (Iox2jtsException e) {
							throw new IllegalStateException(e);
						}
				}
                if(ignorePolygonBuildingErrors==ItfReader2.POLYGON_BUILDING_ERRORS_ON){
					throw new IoxInvalidDataException("dangles");
				}
			}
			Collection invalidRingLines=polygonizer.getInvalidRingLines();
			if(!invalidRingLines.isEmpty()){
				for(Object invalidRingLine:invalidRingLines){
					try {
						dataerrs.add(new IoxInvalidDataException("invald ring line "+IoxInvalidDataException.formatTids((CompoundCurve) invalidRingLine),linetableIliqname,null,Jtsext2iox.JTS2polyline((CompoundCurve)invalidRingLine)));
					} catch (Iox2jtsException e) {
						throw new IllegalStateException(e);
					}
				}
                if(ignorePolygonBuildingErrors==ItfReader2.POLYGON_BUILDING_ERRORS_ON){
					throw new IoxInvalidDataException("invalid ring lines");
				}
			}
			Collection<Polygon> polys = polygonizer.getPolygons();
			EhiLogger.traceState("georef polygons..."+helperTableGeomAttrName);
			STRtree polyidx=new STRtree();
			//System.out.println("polygonizer.getPolygons()");
			for(Polygon poly:polys){
				polyidx.insert(poly.getEnvelopeInternal(), poly);
				//System.out.println(poly);
			}
			HashMap<Polygon,String> hitPolys=new HashMap<Polygon,String>();
			for(String tid:mainTids.keySet()){
				IomObject georef=mainTids.get(tid);
				Coordinate coord=Iox2jtsext.coord2JTS(georef);
				Point point=jtsFact.createPoint(coord);
				List<Polygon> hits=polyidx.query(new Envelope(coord));
				Polygon hit=null;
				for(Polygon candHit:hits){
					if(SimplePointInAreaLocator.locate(coord, candHit)==Location.INTERIOR){
						hit=candHit;
					}
					if(false){
					try{
						if(candHit.contains(point)){
							hit=candHit;
						}
					}catch(TopologyException ex)
					{
						EhiLogger.traceState("side location conflict tid "+tid+", coord "+coord+", polygon "+candHit);
						EhiLogger.logError(ex);
						((CurvePolygon) candHit).dumpPolygonAsJava("poly");
						hit=candHit;
					}
					}
				}
				if(hit==null){
					IoxInvalidDataException ex=new IoxInvalidDataException("no polygon for tid "+tid,geomattrIliqname,tid,Jtsext2iox.JTS2coord(coord));
	                if(ignorePolygonBuildingErrors!=ItfReader2.POLYGON_BUILDING_ERRORS_ON){
						dataerrs.add(ex);
					}else{
						throw ex;
					}
				}else{
					if(hitPolys.containsKey(hit)){
						//EhiLogger.traceState("multiple polygon-refs ("+coord+") and ("+hitPolys.get(hit)+") to polygon "+hit);
						//((CurvePolygon) hit).dumpPolygonAsJava("poly");
						String tid2=hitPolys.get(hit);
						IomObject georef2=mainTids.get(tid2);
						Coordinate coord2=Iox2jtsext.coord2JTS(georef2);
						String hitTids=getTids(hit);
						String tids[]=new String[2];
						tids[0]=tid;
						tids[1]=tid2;
						IoxInvalidDataException ex=null;
						try {
							ex=new IoxInvalidDataException("multiple area-refs to polygon "+IoxInvalidDataException.formatTids(tids),geomattrIliqname,null,Jtsext2iox.JTS2surface(hit));
						} catch (Iox2jtsException e) {
							throw new IllegalStateException(e);
						}
		                if(ignorePolygonBuildingErrors!=ItfReader2.POLYGON_BUILDING_ERRORS_ON){
							dataerrs.add(ex);
						}else{
							throw ex;
						}
					}
					polygons.put(tid, hit);
					hitPolys.put(hit,tid);
				}
			}
			
			// only ITF
			if(!allowItfAreaHoles) {
				for(Polygon poly:polys){
					if(!hitPolys.containsKey(poly)){
						IoxInvalidDataException ex=null;
						try {
							ex=new IoxInvalidDataException("no area-ref to polygon of lines "+getTids(poly),geomattrIliqname,Jtsext2iox.JTS2surface(poly));
						} catch (Iox2jtsException e) {
							throw new IllegalStateException(e);
						}
		                if(ignorePolygonBuildingErrors!=ItfReader2.POLYGON_BUILDING_ERRORS_ON){
							dataerrs.add(ex);
						}else{
							throw ex;
						}
					}
				}
			}
			if(keepLinetables) {
	            for(String tid:mainTids.keySet()){
	                Polygon poly=polygons.get(tid);
	                for(String lineTid:getTidAsArray(poly)){
	                    IomObject line=getLineObject(lineTid);
                        if(line==null) {
                            throw new IllegalStateException();
                        }
	                    if(line.getattrvaluecount(mainTableRef1)==0) {
	                        IomObject ref=new Iom_jObject("REF",null);
	                        ref.setobjectrefoid(tid);
	                        line.addattrobj(mainTableRef1, ref);
	                    }else if(line.getattrvaluecount(mainTableRef2)==0) {
	                        IomObject ref=line.getattrobj(mainTableRef1,0);
	                        String ref1=ref.getobjectrefoid();
	                        if(ref1.compareTo(tid)>0) {
	                            ref.setobjectrefoid(tid);
	                            ref=new Iom_jObject("REF",null);
	                            ref.setobjectrefoid(ref1);
	                            line.addattrobj(mainTableRef2, ref);
	                        }else {
                                ref=new Iom_jObject("REF",null);
                                ref.setobjectrefoid(tid);
                                line.addattrobj(mainTableRef2, ref);
	                        }
	                    }else {
	                        throw new IllegalStateException("more than two refs in line");
	                    }
	                    lines.put(lineTid, line); // keep file up-to-date
	                }
	            }
			}
		}
	public String getTids(Polygon hit) {
		StringBuilder hitTids=new StringBuilder();
		if(hit instanceof CurvePolygon){
			CurvePolygon cp=(CurvePolygon) hit;
			String sep="";
			ArrayList<CurveSegment> segs=new ArrayList<CurveSegment>();
			CompoundCurveRing c=(CompoundCurveRing) cp.getExteriorRing();
			for(CompoundCurve cv:c.getLines()){
				segs.addAll(cv.getSegments());
			}
			for(int ri=0;ri<cp.getNumInteriorRing();ri++){
				c=(CompoundCurveRing) cp.getInteriorRingN(ri);
				for(CompoundCurve cv:c.getLines()){
					segs.addAll(cv.getSegments());
				}
			}
			java.util.HashSet<String> uniqueTids=new java.util.HashSet<String>();
			for(CurveSegment seg:segs){
				String tidx=seg.getUserData().toString();
				if(!uniqueTids.contains(tidx)){
					hitTids.append(sep);
					hitTids.append(tidx);
					sep=", ";
					uniqueTids.add(tidx);
				}
			}
		}
		return hitTids.toString();
	}
    public String[] getTidAsArray(Polygon hit) {
        ArrayList<String> hitTids=new ArrayList<String>();
        if(hit instanceof CurvePolygon){
            CurvePolygon cp=(CurvePolygon) hit;
            ArrayList<CurveSegment> segs=new ArrayList<CurveSegment>();
            CompoundCurveRing c=(CompoundCurveRing) cp.getExteriorRing();
            for(CompoundCurve cv:c.getLines()){
                segs.addAll(cv.getSegments());
            }
            for(int ri=0;ri<cp.getNumInteriorRing();ri++){
                c=(CompoundCurveRing) cp.getInteriorRingN(ri);
                for(CompoundCurve cv:c.getLines()){
                    segs.addAll(cv.getSegments());
                }
            }
            java.util.HashSet<String> uniqueTids=new java.util.HashSet<String>();
            for(CurveSegment seg:segs){
                String tidx=seg.getUserData().toString();
                tidx = removeOverlapRemovalPrefix(tidx);
                if(!uniqueTids.contains(tidx)){
                    hitTids.add(tidx);
                    uniqueTids.add(tidx);
                }
            }
        }
        return hitTids.toArray(new String[hitTids.size()]);
    }
    private String removeOverlapRemovalPrefix(String tidx) {
        if(tidx.startsWith(CompoundCurve.MODIFIED_TID_TAG)) {
            tidx=tidx.substring(CompoundCurve.MODIFIED_TID_TAG.length());
        }else if(tidx.startsWith(CompoundCurve.OVERLAP_TID_TAG)) {
            tidx=tidx.substring(CompoundCurve.OVERLAP_TID_TAG.length());
        }
        return tidx;
    }
    @Override
	public ArrayList<IoxInvalidDataException> getDataerrs() {
		return dataerrs;
	}
	public boolean isAllowItfAreaHoles() {
		return allowItfAreaHoles;
	}
	public void setAllowItfAreaHoles(boolean allowItfAreaHoles) {
		this.allowItfAreaHoles = allowItfAreaHoles;
	}
	@Override
    public boolean isKeepLinetables() {
        return keepLinetables;
    }
	@Override
    public void setKeepLinetables(boolean keepLinetables,String ref1,String ref2) {
        this.keepLinetables = keepLinetables;
        this.mainTableRef1=ref1;
        this.mainTableRef2=ref2;
    }
    @Override
    public IomObject getLineObject(String lineTid) {
        if(!keepLinetables || lines==null) {
            throw new IllegalStateException("no linetable kept");
        }
        IomObject line= lines.get(lineTid);
        String pureLineTid=lineTid.replaceAll(":[0-9]+\\z", "");
        pureLineTid = removeOverlapRemovalPrefix(pureLineTid);
        IomObject lineattr=lineattrs.get(pureLineTid);
        mergeAttrs(line,lineattr);
        return line;
    }
    private void mergeAttrs(IomObject line, IomObject src) {
        int attrc=src.getattrcount();
        for(int attri=0;attri<attrc;attri++) {
            String attrName=src.getattrname(attri);
            int valuec=src.getattrvaluecount(attrName);
            for(int valuei=0;valuei<valuec;valuei++) {
                String valueStr=src.getattrprim(attrName, valuei);
                if(valueStr!=null) {
                    line.setattrvalue(attrName,valueStr);
                }
            }
        }
    }
}
