package ch.interlis.iom_j.itf.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Polygon;
import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.types.OutParam;
import ch.ehi.iox.objpool.ObjectPoolManager;
import ch.ehi.iox.objpool.impl.IomObjectArraySerializer;
import ch.ehi.iox.objpool.impl.IomObjectSerializer;
import ch.ehi.iox.objpool.impl.JavaSerializer;
import ch.ehi.iox.objpool.impl.PolygonSerializer;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.CoordType;
import ch.interlis.ili2c.metamodel.NumericType;
import ch.interlis.ili2c.metamodel.NumericalType;
import ch.interlis.ili2c.metamodel.PrecisionDecimal;
import ch.interlis.ili2c.metamodel.SurfaceOrAreaType;
import ch.interlis.ili2c.metamodel.SurfaceType;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.itf.ItfReader2;
import ch.interlis.iom_j.itf.impl.jtsext.algorithm.CurveSegmentIntersector;
import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
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
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.ValidationConfig;

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

public class ItfSurfaceLinetable2Polygon implements Linetable2Polygon {
	private Map<String,Polygon> polygons=null;
	private Set<String> mainTids=new java.util.HashSet<String>();
    private Map<String,String> lineTid2mainTid=new java.util.HashMap<String,String>();
	private Map<String,java.util.List<IomObject>> linepool=null;
	private boolean surfacesBuilt=false;
	private String helperTableMainTableRef=null;
	private String helperTableGeomAttrName=null;
	private Table linattrTab=null;
	private JtsextGeometryFactory jtsFact=new JtsextGeometryFactory();
	private double maxOverlaps=0.0;
	private double newVertexOffset=0.0;
	private ObjectPoolManager objPool=null;
    private boolean keepLinetables=false;
	private int ignorePolygonBuildingErrors;
	ArrayList<IoxInvalidDataException> dataerrs=new ArrayList<IoxInvalidDataException>();
	private String linetableIliqname=null;
	private String geomattrIliqname=null;
    public ItfSurfaceLinetable2Polygon(AttributeDef surfaceAttr,boolean ignorePolygonBuildingErrors1)
    {
        this(surfaceAttr,ignorePolygonBuildingErrors1?ItfReader2.POLYGON_BUILDING_ERRORS_OFF:ItfReader2.POLYGON_BUILDING_ERRORS_ON);
    }
	public ItfSurfaceLinetable2Polygon(AttributeDef surfaceAttr,int ignorePolygonBuildingErrors1)
	{
		linetableIliqname=surfaceAttr.getContainer().getScopedName(null)+"_"+surfaceAttr.getName();
		geomattrIliqname=surfaceAttr.getContainer().getScopedName(null)+"."+surfaceAttr.getName();
	    ignorePolygonBuildingErrors=ignorePolygonBuildingErrors1;

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
		objPool=new ObjectPoolManager();
		polygons=objPool.newObjectPoolImpl2(new PolygonSerializer());
	}
	public ItfSurfaceLinetable2Polygon(String tableRef, String geomAttr)
	{
		helperTableMainTableRef=tableRef;
		helperTableGeomAttrName=geomAttr;
		objPool=new ObjectPoolManager();
		polygons=objPool.newObjectPoolImpl2(new PolygonSerializer());
	}
	public ItfSurfaceLinetable2Polygon(String tableRef, String geomAttr,double maxOverlaps,double accuracy)
	{
		helperTableMainTableRef=tableRef;
		helperTableGeomAttrName=geomAttr;
		objPool=new ObjectPoolManager();
		polygons=objPool.newObjectPoolImpl2(new PolygonSerializer());
		this.maxOverlaps=maxOverlaps;
		if(accuracy>0){
			newVertexOffset=2*Math.pow(10, -accuracy);
		}
		
	}
	@Override
	public void close()
	{
		if(objPool!=null){
			linepool=null;
			polygons=null;
			mainTids=null;
			objPool.close();
			objPool=null;
		}
	}
	@Override
	public void addItfLinetableObject(IomObject iomObj)
	{
		if(linepool==null){
			linepool=objPool.newObjectPoolImpl2(new IomObjectArraySerializer());
		}
		IomObject structvalue=iomObj.getattrobj(helperTableMainTableRef,0);
		String mainTid=null;
		if(structvalue!=null) {
	        mainTid=structvalue.getobjectrefoid();
		}
		if(mainTid==null) {
			dataerrs.add(new IoxInvalidDataException("boundary line without reference to main table",linetableIliqname,iomObj.getobjectoid(),iomObj));
			return;
		}
		java.util.List<IomObject> lines=null;
		if(linepool.containsKey(mainTid)){
            lines=new java.util.ArrayList<IomObject>(linepool.get(mainTid));
			lines.add(iomObj);
			linepool.put(mainTid,lines);
		}else{
			lines=new java.util.ArrayList<IomObject>();
			lines.add(iomObj);
			linepool.put(mainTid,lines);
		}
        String lineTid=iomObj.getobjectoid();
        lineTid2mainTid.put(lineTid, mainTid);
	}
	public void addMainObjectTid(String tid)
	{
		mainTids.add(tid);
	}
	@Override
	public Iterator<String> mainTableTidIterator()
	{
		return mainTids.iterator();
	}
	@Override
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
	@Override
	public void buildSurfaces() throws IoxException
	{
		surfacesBuilt=true;
		if(linepool==null){
			linepool=new HashMap<String,java.util.List<IomObject>>();
		}
		int totalObj=linepool.keySet().size();
		int objc=1;
		EhiLogger.traceState("build surfaces..."+helperTableGeomAttrName+", maxOverlaps "+maxOverlaps);
		boolean isDisconnected=false;
		for(String mainTid:linepool.keySet()){
			//EhiLogger.debug("tid <"+mainTid+"> "+objc+"/"+totalObj);objc++;
			java.util.List<IomObject> lines1=linepool.get(mainTid);
			HashMap<String,IomObject> lines=new HashMap<String,IomObject>();
			for(IomObject line:lines1){
				IomObject polyline=line.getattrobj(helperTableGeomAttrName, 0);
				if(polyline==null){
					dataerrs.add(new IoxInvalidDataException("empty line",linetableIliqname,line.getobjectoid(),line));
				}else{
					lines.put(line.getobjectoid(), line);
				}
			}
			LineSet lineset=new LineSet(true,linattrTab,helperTableGeomAttrName);
			
			ArrayList<CompoundCurve> segv=lineset.buildBoundaries(lines,jtsFact);
			OutParam<Polygon> poly=new OutParam<Polygon>();
			createPolygon(mainTid, segv,maxOverlaps,newVertexOffset,dataerrs,linetableIliqname,geomattrIliqname,poly);
			if(poly.value!=null) {
	            polygons.put(mainTid, poly.value);
			}
		}
	}
	public static boolean validatePolygon(String mainTid,AttributeDef surfaceAttr,IomObject polygon,LogEventFactory errFact,String validationType) 
			throws IoxException
	{
		String linetableIliqname=surfaceAttr.getContainer().getScopedName(null)+"."+surfaceAttr.getName();
		boolean polygonValid=true;
		double maxOverlaps=0.0;
		double newVertexOffset=0.0;
		PrecisionDecimal overlapDef=((SurfaceOrAreaType)surfaceAttr.getDomainResolvingAliases()).getMaxOverlap();
		if(overlapDef!=null){
			maxOverlaps=overlapDef.doubleValue();
			if(maxOverlaps>0){
			    NumericalType[] dimensions = ((CoordType) ((SurfaceOrAreaType)surfaceAttr.getDomainResolvingAliases()).getControlPointDomain().getType()).getDimensions();
				double size=((NumericType)dimensions[0]).getMinimum().getAccuracy();
				if(size>0){
					newVertexOffset=2*Math.pow(10, -size);
				}
			}
		}
		
		JtsextGeometryFactory jtsFact=new JtsextGeometryFactory();
		ArrayList<CompoundCurve> segv=createLineset(polygon,validationType,0.0,errFact);
		if(segv==null){
			return true;
		}
		for(CompoundCurve seg:segv) {
		    seg.setUserData(mainTid);
		}
		
		OutParam<Polygon> poly=new OutParam<Polygon>();
		ArrayList<IoxInvalidDataException> dataerrs=new ArrayList<IoxInvalidDataException>();
		try {
	        createPolygon(mainTid, segv,maxOverlaps,newVertexOffset,dataerrs,linetableIliqname,null,poly);
		}finally {
			if(dataerrs.size()>0) {
				polygonValid=false;
			}
	        if(ValidationConfig.WARNING.equals(validationType)){
	               for(IoxInvalidDataException err:dataerrs){
	                   errFact.addEvent(errFact.logWarning(err));
	               }
	        }else{
	               for(IoxInvalidDataException err:dataerrs){
	                   errFact.addEvent(errFact.logError(err));
	               }
	        } 
		}
		return polygonValid;
	}
	private static ArrayList<CompoundCurve> createLineset(IomObject obj,String validationType,double tolerance,LogEventFactory errFact) throws IoxException {
		return Iox2jtsext.surface2JTSCompoundCurves(obj, validationType, tolerance, errFact);
	}
	private static void createPolygon(String mainTid,
			ArrayList<CompoundCurve> segv,double maxOverlaps,double newVertexOffset,ArrayList<IoxInvalidDataException> dataerrs,
			String linetableIliqname,String geomattrIliqname,OutParam<Polygon> returnPolygon) 
					throws IoxInvalidDataException 
	{
		boolean hasIntersections=false;
		boolean isDisconnected=false;
		for(CompoundCurve seg : segv){
			removeValidSelfIntersections(seg,maxOverlaps,newVertexOffset);
		}
		
		// ASSERT: segv might contain rings, but not nested rings
		CompoundCurveNoder validator=new CompoundCurveNoder(segv,false);
		if(!validator.isValid()){
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
                    String []tids=new String[2];
                    tids[0]=(String) is.getCurve1().getUserData();
                    tids[1]=(String) is.getCurve2().getUserData();
                    if(tids[0].equals(tids[1])) {
                        tids[1]=null;
                    }
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
					throw new IllegalStateException("unexpected overlap; should have been removed before;"+is);
				}else{
					String []tids=new String[2];
					tids[0]=(String) is.getCurve1().getUserData();
					tids[1]=(String) is.getCurve2().getUserData();
					
                    dataerrs.add(new IoxInvalidDataException(is.toShortString(),linetableIliqname,null,Jtsext2iox.JTS2coord(is.getPt()[0])));
					hasIntersections=true;
				}
			}
			if(hasIntersections){
                return;
			}
		}
		IoxPolygonizer polygonizer=new IoxPolygonizer(newVertexOffset);
		//com.vividsolutions.jts.operation.polygonize.Polygonizer polygonizer=new com.vividsolutions.jts.operation.polygonize.Polygonizer();
		//for(CompoundCurve boundary:segv){
		for(CompoundCurve boundary:validator.getNodedSubstrings()){
			//System.out.println(boundary);
			polygonizer.add(boundary);
		}
		Collection cutEdges = polygonizer.getCutEdges();
		if(!cutEdges.isEmpty()){
			for(Object edge:cutEdges){
				try {
					dataerrs.add(new IoxInvalidDataException("cut edge "+IoxInvalidDataException.formatTids((CompoundCurve) edge),linetableIliqname,null,Jtsext2iox.JTS2polyline((CompoundCurve)edge)));
				} catch (Iox2jtsException e) {
					throw new IllegalStateException(e);
				}
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
		}
		Collection invalidRingLines=polygonizer.getInvalidRingLines();
		if(!invalidRingLines.isEmpty()){
			for(Object invalidRingLine:invalidRingLines){
				try {
					dataerrs.add(new IoxInvalidDataException("invald ring line"+IoxInvalidDataException.formatTids((CompoundCurve) invalidRingLine),linetableIliqname,null,Jtsext2iox.JTS2polyline((CompoundCurve)invalidRingLine)));
				} catch (Iox2jtsException e) {
					throw new IllegalStateException(e);
				}
			}
		}
		Collection<Polygon> polys = polygonizer.getPolygons();
		if(polys.isEmpty()){
            dataerrs.add(new IoxInvalidDataException("no polygon"));
            return;
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
				if(holePoly==poly){
					continue;
				}
				Envelope holeEnv=holePoly.getEnvelopeInternal();
				if(shell.contains(holeEnv) && !shell.equals(holeEnv)){
				}else{
					isDisconnected=true;
					try {
						dataerrs.add(new IoxInvalidDataException("superfluous outerboundary "+IoxInvalidDataException.formatTids(new String[] {mainTid}),geomattrIliqname,mainTid,Jtsext2iox.JTS2surface(holePoly)));
					} catch (Iox2jtsException e) {
						throw new IllegalStateException(e);
					}
				}
			}
			if(isDisconnected) {
                dataerrs.add(new IoxInvalidDataException("multipolygon detected"));
                return;
			}
		}else{
			poly=polys.iterator().next();
		}
		poly.normalize();
		returnPolygon.value=poly;
	}
	public static void removeValidSelfIntersections(CompoundCurve seg,double maxOverlaps, double newVertexOffset) {

		if(seg.getNumSegments()==1){
			return;
		}
		for(int segIndex0=0;segIndex0<seg.getNumSegments();segIndex0++){
			int segIndex1=segIndex0+1;
			if(segIndex1==seg.getNumSegments()){
				segIndex1=0;
			}
			CurveSegment seg0=seg.getSegments().get(segIndex0);
			CurveSegment seg1=seg.getSegments().get(segIndex1);
			CurveSegmentIntersector li = new CurveSegmentIntersector();
			li.computeIntersection(seg0, seg1);
			if(li.hasIntersection()){
				if(li.getIntersectionNum()==2){ 
					if(li.isOverlay()) {
						// hier ignorieren; wird danach im CompoundCurveNoder rapportiert
					}else if(seg.getNumSegments()==2 && seg0.getStartPoint().equals2D(seg1.getEndPoint())){
						// Ring als eine Linie, zwei Segmente
					}else if(li.getOverlap()!=null && li.getOverlap()<maxOverlaps){
						// aufeinanderfolgende Segmente der selben Linie
						Intersection is = new Intersection(
								li.getIntersection(0), li.getIntersection(1),
								seg, seg, seg0, seg1, li.getOverlap(),false);
						EhiLogger.traceState(CurvePolygon.VALID_OVERLAP +" "+ is.toString());
						
						  // overlap entfernen
						  if(seg0 instanceof StraightSegment){
							  seg.removeOverlap((ArcSegment) seg1, seg0, newVertexOffset);
							  //segIndex0++;
						  }else if(seg1 instanceof StraightSegment){
							  seg.removeOverlap((ArcSegment) seg0, seg1, newVertexOffset);
						  }else if(((ArcSegment) seg0).getRadius()>((ArcSegment) seg1).getRadius()){
							  seg.removeOverlap((ArcSegment) seg1, seg0, newVertexOffset);
							  //segIndex0++;
						  }else{
							  // seg1.getRadius() > seg0.getRadius()
							  seg.removeOverlap((ArcSegment) seg0, seg1, newVertexOffset);
						  }
					}
				}else if(li.getIntersectionNum()==1){
					// endPt==startPt
				}else{
					throw new IllegalArgumentException("seg0 and seg1 are not connected");
				}
			}
		}
		
	}
	@Override
	public ArrayList<IoxInvalidDataException> getDataerrs() {
		return dataerrs;
	}
	@Override
    public boolean isKeepLinetables() {
        return keepLinetables;
    }
	@Override
    public void setKeepLinetables(boolean keepLinetables, String ref1, String ref2) {
        this.keepLinetables = keepLinetables;
    }
    @Override
    public Iterator<String> lineTableTidIterator() {
        return lineTid2mainTid.keySet().iterator();
    }
    @Override
    public IomObject getLineObject(String lineTid) {
        String mainTid=lineTid2mainTid.get(lineTid);
        java.util.List<IomObject> lines=linepool.get(mainTid);
        for(IomObject line:lines){
            if(lineTid.equals(line.getobjectoid())) {
                return line;
            }
        }
        return null;
    }
}

