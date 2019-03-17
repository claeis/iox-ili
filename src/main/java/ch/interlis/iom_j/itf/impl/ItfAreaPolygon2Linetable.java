package ch.interlis.iom_j.itf.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.index.strtree.STRtree;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.types.OutParam;
import ch.ehi.iox.objpool.ObjectPoolManager;
import ch.ehi.iox.objpool.impl.CompoundCurveSerializer;
import ch.ehi.iox.objpool.impl.FileBasedCollection;
import ch.ehi.iox.objpool.impl.IomObjectSerializer;
import ch.ehi.iox.objpool.impl.JavaSerializer;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurvePolygon;
import ch.interlis.iom_j.itf.impl.jtsext.noding.CompoundCurveNoder;
import ch.interlis.iom_j.itf.impl.jtsext.noding.CurvePairInt;
import ch.interlis.iom_j.itf.impl.jtsext.noding.Intersection;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.IoxIntersectionException;
import ch.interlis.iox_j.IoxInvalidDataException;
import ch.interlis.iox_j.jts.Iox2jtsException;
import ch.interlis.iox_j.jts.Iox2jtsext;
import ch.interlis.iox_j.jts.Jtsext2iox;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.ValidationConfig;

public class ItfAreaPolygon2Linetable {
	private Collection<? extends CompoundCurve> lines=null;
	private List<Polygon> polygons=null;
	private Collection<IomObject> ioxlines=null;
	private ObjectPoolManager recman=null;
	private String iliqname=null;
	public ItfAreaPolygon2Linetable(String iliqname1, ObjectPoolManager recman1){
		lines=new FileBasedCollection<CompoundCurve>(recman1,new CompoundCurveSerializer());
		polygons=new java.util.ArrayList<Polygon>();
		recman=recman1;
		iliqname=iliqname1;
	}
	public void addLines(String mainObjTid,String internalTid,ArrayList<IomObject> ioxlines) throws IoxException {
		for(IomObject ioxline:ioxlines){
			CompoundCurve line=Iox2jtsext.polyline2JTS(ioxline, false, 0.0);
			if(internalTid!=null){
				line.setUserData(internalTid);
			}else{
				line.setUserData(mainObjTid);
			}
			((Collection)lines).add(line);
		}
	}
	public void addLines(String mainObjTid,String internalTid,ArrayList<IomObject> ioxlines,String validationType,LogEventFactory errs) throws IoxException {
		for(IomObject ioxline:ioxlines){
			OutParam<Boolean> foundErrs=new OutParam<Boolean>();
			CompoundCurve line=Iox2jtsext.polyline2JTS(ioxline, false, 0.0,foundErrs,errs,0.0,validationType,ValidationConfig.WARNING);
			if(line!=null){
				if(internalTid!=null){
					line.setUserData(internalTid);
				}else{
					line.setUserData(mainObjTid);
				}
				((Collection)lines).add(line);
			}
		}
	}
	public void addPolygon(String mainObjTid,String internalTid,IomObject iomPolygon,String validationType,LogEventFactory errs) throws IoxException {
		ArrayList<IomObject> ioxlines=ItfAreaPolygon2Linetable.getLinesFromPolygon(iomPolygon);
		OutParam<Boolean> foundErrs=new OutParam<Boolean>();
		Polygon polygon=Iox2jtsext.surface2JTS(iomPolygon, 0.0,foundErrs,errs,0.0,validationType);
		if(polygon!=null){
			if(internalTid!=null){
				polygon.setUserData(internalTid);
			}else{
				polygon.setUserData(mainObjTid);
			}
			polygons.add(polygon);
		}
		
		for(IomObject ioxline:ioxlines){
			CompoundCurve line=Iox2jtsext.polyline2JTS(ioxline, false, 0.0,foundErrs,errs,0.0,validationType,ValidationConfig.WARNING);
			if(line!=null){
				if(internalTid!=null){
					line.setUserData(internalTid);
				}else{
					line.setUserData(mainObjTid);
				}
				((Collection)lines).add(line);
			}
		}
	}
	
	public List<IoxInvalidDataException> validate()  {
		CompoundCurveNoder noder=new CompoundCurveNoder(recman,(java.util.List)lines,false);
		if(!noder.isValid()){
			List<IoxInvalidDataException> intersectionsWithoutCompleteOverlays=new ArrayList<IoxInvalidDataException>();
			List<Intersection> intersections=noder.getIntersections();
			Iterator<Intersection> intersectionIter=intersections.iterator();
			
			while(intersectionIter.hasNext()){
				Intersection is=intersectionIter.next();
				
				CompoundCurve e0=is.getCurve1();
				CompoundCurve e1=is.getCurve2();
				int segIndex0=e0.getSegments().indexOf(is.getSegment1());
				int segIndex1=e1.getSegments().indexOf(is.getSegment2());
				Coordinate p00;
				Coordinate p01;
				Coordinate p11;
				Coordinate p10;
				String tid1=(String) e0.getUserData();
				p00 = e0.getSegments().get(segIndex0).getStartPoint();
				p01 = e0.getSegments().get(segIndex0).getEndPoint();
				p10 = e1.getSegments().get(segIndex1).getStartPoint();
				p11 = e1.getSegments().get(segIndex1).getEndPoint();
				
				if(is.isOverlay() &&
				   is.isIntersection(p00) &&
				   is.isIntersection(p01) &&
				   is.isIntersection(p10) &&
				   is.isIntersection(p11)) {
				   // ignore intersections that are the result of identical segments
				}else {
					intersectionsWithoutCompleteOverlays.add(new IoxIntersectionException(iliqname, tid1, is));
				}
			}
			
			if(!intersectionsWithoutCompleteOverlays.isEmpty()) {
				return intersectionsWithoutCompleteOverlays;
			}
		
			if(polygons==null) {
				return null;
			}
			STRtree polyidx=new STRtree();
			
			// fill the polygon index
			for (int i=0;i<polygons.size();i++) {
				Polygon currentPolygon=polygons.get(i);
				if(currentPolygon!=null) {
					Envelope env=new Envelope(currentPolygon.getEnvelopeInternal());
					polyidx.insert(env, i);
				}
			}
			
			// check if the complete polygons overlay
			HashSet<CurvePairInt> compared=new HashSet<CurvePairInt>();
		    for (int i0=0;i0<polygons.size();i0++) {
		    	Polygon e0=polygons.get(i0);
				List<Integer> hits=polyidx.query(e0.getEnvelopeInternal());
    			for (int hitIdx = 0; hitIdx < hits.size(); hitIdx++) {
    				int i1=hits.get(hitIdx);
					if(i0==i1) {
    	        		continue;
    	        	}
    				CurvePairInt pair=new CurvePairInt(i0,i1);
    				if(!compared.contains(pair)) {
    					compared.add(pair);
        	        	Polygon e1 = polygons.get(i1);
    	            	if(CurvePolygon.polygonOverlays(e0, e1)){
    	            		String tid1=(String) e0.getUserData();
    	            		String tid2=(String) e1.getUserData();
    						intersectionsWithoutCompleteOverlays.add(new IoxInvalidDataException("polygons overlay tid1 "+tid1+", tid2 "+tid2));
    					}
    				}
				}
		    }
			if(!intersectionsWithoutCompleteOverlays.isEmpty()) {
				return intersectionsWithoutCompleteOverlays;
			}
		}
		return null;
	}
	
	public java.util.List<IomObject> getLines() throws IoxException {
		if(ioxlines==null){
			{
				CompoundCurveNoder noder=new CompoundCurveNoder(recman,(java.util.List)lines,false);
				if(!noder.isValid()){
					for(Intersection is:noder.getIntersections()){
						EhiLogger.logError("intersection tid1 "+is.getCurve1().getUserData()+", tid2 "+is.getCurve2().getUserData()+", coord "+is.getPt()[0].toString()+(is.getPt().length==2?(", coord2 "+is.getPt()[1].toString()):""));
						EhiLogger.traceState("overlap "+is.getOverlap()+", seg1 "+is.getSegment1()+", seg2 "+is.getSegment2());
					}
					throw new IoxException("intersections");
				}
				lines=noder.getNodedSubstrings();
				noder=null;
			}
			//CompoundCurveDissolver dissolver=new CompoundCurveDissolver();
			//dissolver.dissolve(lines);
			//lines=dissolver.getDissolved();
			//ioxlines=new ArrayList<IomObject>();
			ioxlines=new FileBasedCollection<IomObject>(recman,new IomObjectSerializer());
			for(CompoundCurve line:lines){
				IomObject ioxline;
				try {
					ioxline = Jtsext2iox.JTS2polyline(line);
				} catch (Iox2jtsException e) {
					throw new IoxException(e);
				}
				ioxlines.add(ioxline);
			}
		}
		return (List<IomObject>) ioxlines;
	}

	public static ArrayList<IomObject> getLinesFromPolygon(IomObject polygon)
	{
		ArrayList<IomObject> ret=new ArrayList<IomObject>();
		boolean clipped=polygon.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
		for(int surfacei=0;surfacei<polygon.getattrvaluecount("surface");surfacei++){
			if(clipped){
				throw new IllegalArgumentException("clipped surface not supported");
			}else{
				// an unclipped surface should have only one surface element
				if(surfacei>0){
					throw new IllegalArgumentException("unclipped surface with multi 'surface' elements");
				}
			}
			IomObject surface=polygon.getattrobj("surface",surfacei);
			for(int boundaryi=0;boundaryi<surface.getattrvaluecount("boundary");boundaryi++){
				IomObject boundary=surface.getattrobj("boundary",boundaryi);
				for(int polylinei=0;polylinei<boundary.getattrvaluecount("polyline");polylinei++){
					IomObject polyline=boundary.getattrobj("polyline",polylinei);
					ret.add(polyline);
				}
			}
			if(clipped){
			}
		}
		return ret;
	}
}