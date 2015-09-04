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
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.index.strtree.STRtree;
import com.vividsolutions.jts.noding.FastNodingValidator;
import com.vividsolutions.jts.noding.BasicSegmentString;
import com.vividsolutions.jts.noding.FastSegmentSetIntersectionFinder;
import com.vividsolutions.jts.operation.IsSimpleOp;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;
import com.vividsolutions.jts.operation.valid.IsValidOp;
import com.vividsolutions.jts.operation.valid.TopologyValidationError;

import ch.ehi.basics.logging.EhiLogger;
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
	private ArrayList<IomObject> lines=new ArrayList<IomObject>(); 
	private boolean surfacesBuilt=false;
	private String helperTableGeomAttrName=null;
	private Table linattrTab=null;
	private double maxOverlaps=0.0;
	private double newVertexOffset=0.0;
	private JtsextGeometryFactory jtsFact=new JtsextGeometryFactory();
	public ItfAreaLinetable2Polygon(AttributeDef surfaceAttr)
	{
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
	}
	public ItfAreaLinetable2Polygon(String geomAttr)
	{
		helperTableGeomAttrName=geomAttr;
		
	}
	public void addItfLinetableObject(IomObject iomObj)
	{
		lines.add(iomObj);
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
			//long startMem=getUsedMemory();
			LineSet lineset=new LineSet(false,linattrTab,helperTableGeomAttrName);

			ArrayList<CompoundCurve> segv=lineset.buildBoundaries(lines,jtsFact);
			lineset=null;
			//EhiLogger.debug("Memory segv "+(getUsedMemory()-startMem));
			
			EhiLogger.traceState("validate noding..."+helperTableGeomAttrName+", maxOverlaps "+maxOverlaps+", offset "+newVertexOffset);
				CompoundCurveNoder validator=new CompoundCurveNoder(segv,true);
				if(!validator.isValid()){
					boolean hasIntersections=false;
					for(Intersection is:validator.getIntersections()){
						boolean seg1WithValidOverlap=ItfSurfaceLinetable2Polygon.isFirstOrLastSegment(is.getCurve1(),is.getSegment1());
						boolean seg2WithValidOverlap=ItfSurfaceLinetable2Polygon.isFirstOrLastSegment(is.getCurve2(),is.getSegment2());
						if(seg1WithValidOverlap && seg2WithValidOverlap && is.getOverlap()!=null && is.getOverlap()<maxOverlaps){
							// valid overlap, ignore for now, will be removed later in IoxPolygonizer
						}else{
							EhiLogger.logError("intersection tid1 "+is.getCurve1().getUserData()+", tid2 "+is.getCurve2().getUserData()+", coord "+is.getPt()[0].toString()+(is.getPt().length==2?(", coord2 "+is.getPt()[1].toString()):""));
							EhiLogger.traceState("overlap "+is.getOverlap()+", seg1 "+is.getSegment1()+", seg2 "+is.getSegment2());
							hasIntersections=true;
						}
					}
					if(hasIntersections){
						throw new IoxException("intersections");
					}
				}
				validator=null;
			
			EhiLogger.traceState("polygonize..."+helperTableGeomAttrName);
			//System.out.println("polygonizer.lines");
			//Polygonizer polygonizer=new Polygonizer();
			IoxPolygonizer polygonizer=new IoxPolygonizer(newVertexOffset);
			for(CompoundCurve boundary:segv){
				//System.out.println(boundary);
				polygonizer.add(boundary);
			}
			Collection cutEdges = polygonizer.getCutEdges();
			if(!cutEdges.isEmpty()){
				for(Object edge:cutEdges){
					EhiLogger.logError("cut edge: tids "+getSegmentTids((CompoundCurve) edge)+", "+edge);
				}
				throw new IoxInvalidDataException("cut edges");
			}
			Collection dangles=polygonizer.getDangles();
			if(!dangles.isEmpty()){
				for(Object dangle:dangles){
					EhiLogger.logError("dangle: tids "+getSegmentTids((CompoundCurve) dangle)+", "+dangle);
				}
				throw new IoxInvalidDataException("dangles");
			}
			Collection invalidRingLines=polygonizer.getInvalidRingLines();
			if(!invalidRingLines.isEmpty()){
				for(Object invalidRingLine:invalidRingLines){
					EhiLogger.logError("invalid ring line: tids "+getSegmentTids((CompoundCurve) invalidRingLine)+", "+invalidRingLine);
					//dumpLine((CompoundCurve) invalidRingLine);
				}
				throw new IoxInvalidDataException("invalid ring lines");
			}
			Collection<Polygon> polys = polygonizer.getPolygons();
			EhiLogger.traceState("georef polygons..."+helperTableGeomAttrName);
			STRtree polyidx=new STRtree();
			//System.out.println("polygonizer.getPolygons()");
			for(Polygon poly:polys){
				polyidx.insert(poly.getEnvelopeInternal(), poly);
				//System.out.println(poly);
			}
			HashSet<Polygon> hitPolys=new HashSet<Polygon>();
			for(String tid:mainTids.keySet()){
				IomObject georef=mainTids.get(tid);
				Coordinate coord=Iox2jtsext.coord2JTS(georef);
				Point point=jtsFact.createPoint(coord);
				List<Polygon> hits=polyidx.query(new Envelope(coord));
				Polygon hit=null;
				for(Polygon candHit:hits){
					if(candHit.contains(point)){
						hit=candHit;
					}
				}
				if(hit==null){
					throw new IoxInvalidDataException("no polygon for "+tid);
				}
				if(hitPolys.contains(hit)){
					throw new IoxInvalidDataException("multiple polygon-refs to polygon "+hit);
				}
				polygons.put(tid, hit);
				hitPolys.add(hit);
			}
			
			// only ITF
			for(Polygon poly:polys){
				if(!hitPolys.contains(poly)){
					throw new IoxInvalidDataException("no polygon-ref to polygon "+poly);
				}
			}
		}
	private void dumpLine(CompoundCurve line) {
		for(CurveSegment seg:line.getSegments()){
			if(seg instanceof StraightSegment){
				StraightSegment s=(StraightSegment) seg;
				EhiLogger.debug("l.add(new StraightSegment(\""+s.getUserData()+"\",new Coordinate("+s.getStartPoint().x+", "+s.getStartPoint().y+"),new Coordinate( "+s.getEndPoint().x+", "+s.getEndPoint().y+")));");
			}else{
				ArcSegment s=(ArcSegment) seg;
				EhiLogger.debug("l.add(new ArcSegment(\""+s.getUserData()+"\",new Coordinate("+s.getStartPoint().x+", "+s.getStartPoint().y+"),new Coordinate("+s.getMidPoint().x+", "+s.getMidPoint().y+"),new Coordinate( "+s.getEndPoint().x+", "+s.getEndPoint().y+")));");
			}
		}
		
	}
	public static String getSegmentTids(CompoundCurve line) {
		StringBuilder str=new StringBuilder();
		String sep="";
		for(CurveSegment seg:line.getSegments()){
			str.append(sep);
			str.append(seg.getUserData());
			sep=", ";
		}
		return str.toString();
	}
}
