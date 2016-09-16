package ch.interlis.iom_j.itf.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;



import com.vividsolutions.jts.algorithm.BoundaryNodeRule;
import com.vividsolutions.jts.algorithm.locate.SimplePointInAreaLocator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Location;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.TopologyException;
import com.vividsolutions.jts.index.strtree.STRtree;
import com.vividsolutions.jts.noding.FastNodingValidator;
import com.vividsolutions.jts.noding.BasicSegmentString;
import com.vividsolutions.jts.noding.FastSegmentSetIntersectionFinder;
import com.vividsolutions.jts.operation.IsSimpleOp;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;
import com.vividsolutions.jts.operation.valid.IsValidOp;
import com.vividsolutions.jts.operation.valid.TopologyValidationError;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.iox.objpool.ObjectPoolManager;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.AreaType;
import ch.interlis.ili2c.metamodel.CoordType;
import ch.interlis.ili2c.metamodel.NumericType;
import ch.interlis.ili2c.metamodel.NumericalType;
import ch.interlis.ili2c.metamodel.SurfaceType;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurveRing;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurvePolygon;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;
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

public class ItfAreaLinetable2Polygon {
	private Map<String,Polygon> polygons=new HashMap<String,Polygon>();
	private Map<String,IomObject> mainTids=new HashMap<String,IomObject>();
	private Map<String,IomObject> lines=null; 
	private boolean surfacesBuilt=false;
	private String helperTableGeomAttrName=null;
	private Table linattrTab=null;
	private double maxOverlaps=0.0;
	private double newVertexOffset=0.0;
	private JtsextGeometryFactory jtsFact=new JtsextGeometryFactory();
	private ObjectPoolManager objPool = null;
	private boolean ignorePolygonBuildingErrors=false;
	ArrayList<IoxInvalidDataException> dataerrs=new ArrayList<IoxInvalidDataException>(); 
	private String linetableIliqname=null;
	private String geomattrIliqname=null;

	public ItfAreaLinetable2Polygon(AttributeDef surfaceAttr,boolean ignorePolygonBuildingErrors1)
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
	public void close()
	{
		if(objPool!=null){
			lines=null;
			mainTids=null;
			objPool.close();
			objPool=null;
		}
	}
	public void addItfLinetableObject(IomObject iomObj)
	{
		if(lines==null){
			lines=objPool.newObjectPool();
		}
		lines.put(iomObj.getobjectoid(),iomObj);
	}
	public void addGeoRef(String tid,IomObject iomCoord)
	{
		mainTids.put(tid, iomCoord);
	}
	public Iterator<String> mainTableTidIterator()
	{
		return mainTids.keySet().iterator();
	}
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
			objPool.close();
			objPool=null;

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
						if(e0!=e1 && 
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
							String []tids=new String[2];
							tids[0]=(String) is.getCurve1().getUserData();
							tids[1]=(String) is.getCurve2().getUserData();
							dataerrs.add(new IoxInvalidDataException("intersection",linetableIliqname,tids,Jtsext2iox.JTS2coord(is.getPt()[0])));
							if(is.getPt().length==2){
								dataerrs.add(new IoxInvalidDataException("intersection",linetableIliqname,tids,Jtsext2iox.JTS2coord(is.getPt()[1])));
							}
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
			for(CompoundCurve boundary:validator.getNodedSubstrings()){
				//System.out.println(boundary);
				polygonizer.add(boundary);
			}
			validator=null;
			Collection cutEdges = polygonizer.getCutEdges();
			if(!cutEdges.isEmpty()){
				for(Object edge:cutEdges){
					try {
						dataerrs.add(new IoxInvalidDataException("cut edge",linetableIliqname,((CompoundCurve) edge).getSegmentTids(),Jtsext2iox.JTS2polyline((CompoundCurve)edge)));
					} catch (Iox2jtsException e) {
						throw new IllegalStateException(e);
					}
				}
				if(!ignorePolygonBuildingErrors){
					throw new IoxInvalidDataException("cut edges");
				}
			}
			Collection dangles=polygonizer.getDangles();
			if(!dangles.isEmpty()){
				for(Object dangle:dangles){
						try {
							dataerrs.add(new IoxInvalidDataException("dangle",linetableIliqname,((CompoundCurve) dangle).getSegmentTids(),Jtsext2iox.JTS2polyline((CompoundCurve)dangle)));
						} catch (Iox2jtsException e) {
							throw new IllegalStateException(e);
						}
				}
				if(!ignorePolygonBuildingErrors){
					throw new IoxInvalidDataException("dangles");
				}
			}
			Collection invalidRingLines=polygonizer.getInvalidRingLines();
			if(!invalidRingLines.isEmpty()){
				for(Object invalidRingLine:invalidRingLines){
					try {
						dataerrs.add(new IoxInvalidDataException("invald ring line",linetableIliqname,((CompoundCurve) invalidRingLine).getSegmentTids(),Jtsext2iox.JTS2polyline((CompoundCurve)invalidRingLine)));
					} catch (Iox2jtsException e) {
						throw new IllegalStateException(e);
					}
				}
				if(!ignorePolygonBuildingErrors){
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
					String tids[]=new String[1];
					tids[0]=tid;
					IoxInvalidDataException ex=new IoxInvalidDataException("no polygon for tid",geomattrIliqname,tids,Jtsext2iox.JTS2coord(coord));
					if(ignorePolygonBuildingErrors){
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
							ex=new IoxInvalidDataException("multiple area-refs to polygon",geomattrIliqname,tids,Jtsext2iox.JTS2surface(hit));
						} catch (Iox2jtsException e) {
							throw new IllegalStateException(e);
						}
						if(ignorePolygonBuildingErrors){
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
			for(Polygon poly:polys){
				if(!hitPolys.containsKey(poly)){
					IoxInvalidDataException ex=null;
					try {
						ex=new IoxInvalidDataException("no area-ref to polygon",geomattrIliqname,Jtsext2iox.JTS2surface(poly));
					} catch (Iox2jtsException e) {
						throw new IllegalStateException(e);
					}
					if(ignorePolygonBuildingErrors){
						dataerrs.add(ex);
					}else{
						throw ex;
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
	public ArrayList<IoxInvalidDataException> getDataerrs() {
		return dataerrs;
	}
}
