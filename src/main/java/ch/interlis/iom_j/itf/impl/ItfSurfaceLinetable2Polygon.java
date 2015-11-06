package ch.interlis.iom_j.itf.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.noding.BasicSegmentString;
import com.vividsolutions.jts.operation.valid.IsValidOp;
import com.vividsolutions.jts.operation.valid.TopologyValidationError;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.CoordType;
import ch.interlis.ili2c.metamodel.NumericType;
import ch.interlis.ili2c.metamodel.NumericalType;
import ch.interlis.ili2c.metamodel.PrecisionDecimal;
import ch.interlis.ili2c.metamodel.SurfaceType;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurveRing;
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
FOREACH: Line-IomObject    
	save Line to Line-Pool    
    add TID of Line-IomObject to collection of MainObj-TID
FORALL: Main-Obj-TIDs
 	get all Lines from Line-Pool
 	build polygon
 	save polygon to polygon-pool
 */
/* SURFACE:
FORALL: alle Linien zu einem Hauptobjekt
  IF Rand angefangen
  THEN
		IF neuer startPunkt!=endPunkt bisheriger Rand
		THEN Linie zurueck in Buffer
		IF keine weitere Linie und Rand nicht geschlossen
		THEN Fehler; Abbruch
  ELSE neuer Rand

  FORALL: alle Kurvenstuecke (Gerade: neuer Punkt; Kreisbogen: neuer Punkt + Zwischenpunkt)
		IF neuer Punkt weniger als zwei Einheiten entfernt von altem Punkt
		THEN Fehler; Abbruch
		IF neue Gerade
		  IF Gerade in gleicher Richtung rueckwaerts wie alte Gerade
		  THEN Fehler; Abbruch
		IF neue Kreis
		  IF Kreis in gleicher Richtung rueckwaerts wie alter Kreis
		  THEN Fehler; Abbruch
		IF neues Stueck schneidet bisherigen Rand 
		THEN:
			IF Schnittpunkt nicht beim letzten Stueck
			THEN Fehler; Abbruch
			If zu grosser Overlaps (Pfeilhoehe des Kreisbogen bis Schnittpunkt)
			THEN Fehler; Abbruch
			IF Gerade-Kreisbogen mit Overlaps THEN
			  neuer Zwischenpunkt bei Kreisbogen einrechnen
			ELSIF Kreisbogen-Gerade mit Overlaps THEN
			  neuer Zwischenpunkt bei (altem) Kreisbogen einrechnen
			ELSIF grosserKresibogen-kleiner Kreisbogen mit Overlaps THEN
			  neuer Zwischenpunkt bei kleinem Kreisbogen einrechnen
			ELSIF kleinerKresibogen-grosserKreisbogen mit Overlaps THEN
			  neuer Zwischenpunkt bei kleinem Kreisbogen einrechnen
		IF neuer Punkt identisch mit erstem Punkt auf bisherigem Rand
		THEN Rand fertig; Rand in Zwischenspeicher
		ELSIF neuer Punkt identisch mit Punkt auf bisherigem Rand
		THEN neuer Rand (von Punkt bis neur Punkt) in Zwischenspeicher; bisheriger Randanfang fortsetzen
		ELSIF neuer Punkt liegt auf bisherigem Rand
		THEN Fehler; Abbruch
Aeusseren Rand ermitteln
IF: ein innerer Rand liegt nicht innerhalb des aeusseren Randes
THEN: Fehler; Abbruch
IF: ein innerer Rand liegt auf einem anderen inneren Rand
THEN: Fehler; Abbruch
Polygon aus Raendern zusammensetzen   
*/	

public class ItfSurfaceLinetable2Polygon {
	private Map<String,Polygon> polygons=new HashMap<String,Polygon>();
	private Set<String> mainTids=new java.util.HashSet<String>();
	private Map<String,java.util.ArrayList<IomObject>> linepool=new HashMap<String,java.util.ArrayList<IomObject>>();
	private boolean surfacesBuilt=false;
	private String helperTableMainTableRef=null;
	private String helperTableGeomAttrName=null;
	private Table linattrTab=null;
	private JtsextGeometryFactory jtsFact=new JtsextGeometryFactory();
	private double maxOverlaps=0.0;
	private double newVertexOffset=0.0;
	public ItfSurfaceLinetable2Polygon(AttributeDef surfaceAttr)
	{
		PrecisionDecimal overlapDef=((SurfaceType)surfaceAttr.getDomainResolvingAliases()).getMaxOverlap();
		if(overlapDef!=null){
			maxOverlaps=overlapDef.doubleValue();
			if(maxOverlaps>0){
			    NumericalType[] dimensions = ((CoordType) ((SurfaceType)surfaceAttr.getDomainResolvingAliases()).getControlPointDomain().getType()).getDimensions();
				double size=((NumericType)dimensions[0]).getMinimum().getAccuracy();
				if(size>0){
					newVertexOffset=2*Math.pow(10, -size);
				}
			}
		}
		linattrTab=((SurfaceType)surfaceAttr.getDomainResolvingAliases()).getLineAttributeStructure();
		helperTableMainTableRef=ch.interlis.iom_j.itf.ModelUtilities.getHelperTableMainTableRef(surfaceAttr);
		helperTableGeomAttrName=ch.interlis.iom_j.itf.ModelUtilities.getHelperTableGeomAttrName(surfaceAttr);
	}
	public ItfSurfaceLinetable2Polygon(String tableRef, String geomAttr)
	{
		helperTableMainTableRef=tableRef;
		helperTableGeomAttrName=geomAttr;
		
	}
	public void addItfLinetableObject(IomObject iomObj)
	{
		IomObject structvalue=iomObj.getattrobj(helperTableMainTableRef,0);
		String refoid=structvalue.getobjectrefoid();
		java.util.ArrayList<IomObject> lines=null;
		if(linepool.containsKey(refoid)){
			lines=linepool.get(refoid);
		}else{
			lines=new java.util.ArrayList<IomObject>();
			linepool.put(refoid,lines);
		}
		lines.add(iomObj);
	}
	public void addMainObjectTid(String tid)
	{
		mainTids.add(tid);
	}
	public Iterator<String> mainTableTidIterator()
	{
		return mainTids.iterator();
	}
	public IomObject getSurfaceObject(String mainObjectTid) throws IoxException
	{
		if(!surfacesBuilt){
			buildSurfaces();
		}
		if(polygons.containsKey(mainObjectTid)){
			try {
				return Jtsext2iox.JTS2surface(polygons.get(mainObjectTid));
			} catch (Iox2jtsException e) {
				throw new IoxException(e);
			}
		}
		return null;
	}
	public void buildSurfaces() throws IoxException
	{
		int totalObj=linepool.keySet().size();
		int objc=1;
		EhiLogger.traceState("build surfaces..."+helperTableGeomAttrName+", maxOverlaps "+maxOverlaps);

		boolean isDisconnected=false;
		for(String mainTid:linepool.keySet()){
			//EhiLogger.debug("tid <"+mainTid+"> "+objc+"/"+totalObj);objc++;
			ArrayList<IomObject> lines=linepool.get(mainTid);
			
			LineSet lineset=new LineSet(true,linattrTab,helperTableGeomAttrName);
			
			ArrayList<CompoundCurve> segv=lineset.buildBoundaries(lines,jtsFact);
			// ASSERT: segv might contain rings, but not nested rings
			CompoundCurveNoder validator=new CompoundCurveNoder(segv,true);
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
					if((segIndex0==0 || segIndex0==e0.getSegments().size()-1) && (segIndex1==0 || segIndex1==e1.getSegments().size()-1) && is.getOverlap()!=null && is.getOverlap()<maxOverlaps){
						// valid overlap, ignore for now, will be removed later in IoxPolygonizer
					}else if(e0==e1 && (
								  Math.abs(segIndex0-segIndex1)==1 
								  || Math.abs(segIndex0-segIndex1)==e0.getNumSegments()-1  ) // bei Ring: letztes Segment und Erstes Segment
								  && (is.isIntersection(p00) || is.isIntersection(p01))
								  && (is.isIntersection(p10) || is.isIntersection(p11))){
							// aufeinanderfolgende Segmente der selben Linie
							  // overlap entfernen
							  Coordinate newVertexPt=new Coordinate();
							  if(seg0 instanceof StraightSegment){
								  e0.removeOverlap((ArcSegment) seg1, seg0, newVertexPt, newVertexOffset);
							  }else if(seg1 instanceof StraightSegment){
								  e0.removeOverlap((ArcSegment) seg0, seg1, newVertexPt, newVertexOffset);
							  }else if(((ArcSegment) seg0).getRadius()>((ArcSegment) seg1).getRadius()){
								  e0.removeOverlap((ArcSegment) seg1, seg0, newVertexPt, newVertexOffset);
							  }else{
								  // seg1.getRadius() > seg0.getRadius()
								  e0.removeOverlap((ArcSegment) seg0, seg1, newVertexPt, newVertexOffset);
							  }
					}else{
						EhiLogger.logError("intersection tid1 "+is.getCurve1().getUserData()+", tid2 "+is.getCurve2().getUserData()+", coord "+is.getPt()[0].toString()+(is.getPt().length==2?(", coord2 "+is.getPt()[1].toString()):""));
						EhiLogger.traceState(is.toString());
						hasIntersections=true;
					}
				}
				if(hasIntersections){
					throw new IoxException("intersections");
				}
			}
			IoxPolygonizer polygonizer=new IoxPolygonizer(newVertexOffset);
			for(CompoundCurve boundary:segv){
				//System.out.println(boundary);
				polygonizer.add(boundary);
			}
			Collection cutEdges = polygonizer.getCutEdges();
			if(!cutEdges.isEmpty()){
				for(Object edge:cutEdges){
					EhiLogger.logError("cut edge: tids "+((CompoundCurve) edge).getSegmentTids()+", "+edge);
				}
				throw new IoxInvalidDataException("cut edges");
			}
			Collection dangles=polygonizer.getDangles();
			if(!dangles.isEmpty()){
				for(Object dangle:dangles){
					EhiLogger.logError("dangle: tids "+((CompoundCurve) dangle).getSegmentTids()+", "+dangle);
				}
				throw new IoxInvalidDataException("dangles");
			}
			Collection invalidRingLines=polygonizer.getInvalidRingLines();
			if(!invalidRingLines.isEmpty()){
				for(Object invalidRingLine:invalidRingLines){
					EhiLogger.logError("invalid ring line: tids "+((CompoundCurve) invalidRingLine).getSegmentTids()+", "+invalidRingLine);
					//((CompoundCurve) invalidRingLine).dumpLineAsJava();
				}
				throw new IoxInvalidDataException("invalid ring lines");
			}
			Collection<Polygon> polys = polygonizer.getPolygons();
			if(polys.isEmpty()){
				throw new IoxInvalidDataException("no polygon");
			}
			Polygon poly=null;
			if(polys.size()>1){
				Iterator<Polygon> pi=polys.iterator();
				poly=pi.next();
				Envelope shell=poly.getEnvelopeInternal();
				while(pi.hasNext()){
					Polygon nextPoly=pi.next();
					Envelope nextEnv=nextPoly.getEnvelopeInternal();
					if(nextEnv.contains(shell)){
						poly=nextPoly;
						shell=nextEnv;
					}
				}
				pi=polys.iterator();
				while(pi.hasNext()){
					Polygon holePoly=pi.next();
					Envelope holeEnv=holePoly.getEnvelopeInternal();
					if(shell.contains(holeEnv)){
					}else{
						isDisconnected=true;
						EhiLogger.logError("multipolygon with tid "+mainTid+", polygonN "+holePoly);
					}
				}
				
			}else{
				poly=polys.iterator().next();
			}
			poly.normalize();
			polygons.put(mainTid, poly);
		}
		if(isDisconnected){
			throw new IoxInvalidDataException("multipolygon detected");
		}
		surfacesBuilt=true;
	}
}

