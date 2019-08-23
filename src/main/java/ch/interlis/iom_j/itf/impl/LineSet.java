package ch.interlis.iom_j.itf.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.valid.IsValidOp;
import com.vividsolutions.jts.operation.valid.TopologyValidationError;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.IoxInvalidDataException;
import ch.interlis.iox_j.jts.Iox2jts;
import ch.interlis.iox_j.jts.Iox2jtsException;
import ch.interlis.iox_j.jts.Iox2jtsext;
import ch.interlis.iox_j.jts.Jtsext2iox;

public class LineSet {

	private boolean isSurface=true;
	private String helperTableGeomAttrName=null;
	private Table linattrTab=null;
	
	public LineSet(boolean isSurface,Table lineattrTab,String geomAttr) {
		this.isSurface=isSurface;
		helperTableGeomAttrName=geomAttr;
		linattrTab=lineattrTab;
	}
	
	public ArrayList<CompoundCurve> buildBoundaries(Map<String,IomObject> lines, JtsextGeometryFactory jtsFact) throws IoxException
	{
		ArrayList<ArrayList<CurveSegment>> boundaries=new ArrayList<ArrayList<CurveSegment>>();
		ArrayList<String> boundaryTids=new ArrayList<String>();
		//FORALL: alle Linien
		ArrayList<CurveSegment> currentBoundary=null;
		String currentBoundaryTid=null;
		for(String line_tid:lines.keySet()){
			IomObject polyline=lines.get(line_tid).getattrobj(helperTableGeomAttrName, 0);
			if(polyline==null){
				throw new IoxException("undefined polyline");
			}
			com.vividsolutions.jts.geom.Coordinate currentSegmentStartpoint=null;
			{
				IomObject sequence=polyline.getattrobj("sequence",0);
				IomObject segment=sequence.getattrobj("segment",0);
				currentSegmentStartpoint=Iox2jtsext.coord2JTS(segment);
			}
			  if(currentBoundary!=null && currentBoundary.size()>0){
		            // alter Rand abschliessen
					boundaries.add(currentBoundary);
					boundaryTids.add(currentBoundaryTid);
			  }
			  // neuer Rand
			currentBoundaryTid=line_tid;
			  currentBoundary=new ArrayList<CurveSegment>();
			if(linattrTab!=null){
				// TODO handle lineattr
			}
			boolean clipped=polyline.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
			if(clipped){
				throw new IoxException("clipped polyline not supported");
			}
			  //FORALL: alle Kurvenstuecke (Gerade: neuer Punkt; Kreisbogen: neuer Punkt + Zwischenpunkt)
			for(int sequencei=0;sequencei<polyline.getattrvaluecount("sequence");sequencei++){
				if(clipped){
					//out.startElement(tags::get_CLIPPED(),0,0);
				}else{
					// an unclipped polyline should have only one sequence element
					if(sequencei>0){
						throw new IoxException("unclipped polyline with multi 'sequence' elements");
					}
				}
				com.vividsolutions.jts.geom.Coordinate lastSegmentEndpoint=null;
				IomObject sequence=polyline.getattrobj("sequence",sequencei);
				for(int segmenti=0;segmenti<sequence.getattrvaluecount("segment");segmenti++){
					IomObject segment=sequence.getattrobj("segment",segmenti);
					//EhiLogger.debug("segmenttag "+segment.getobjecttag());
					CurveSegment curve=null;
					if(segment.getobjecttag().equals("COORD")){
						// COORD
						if(lastSegmentEndpoint==null){
							curve=null;
							lastSegmentEndpoint=Iox2jtsext.coord2JTS(segment);
						}else{
							Coordinate newSegEndPt=Iox2jtsext.coord2JTS(segment);
							curve=new StraightSegment(lastSegmentEndpoint,newSegEndPt);
						}
					}else if(segment.getobjecttag().equals("ARC")){
						// ARC
						//arc2JTS(ret,segment,p);
						Coordinate newSegMidPt=Iox2jtsext.getArcMidPt(segment);
						Coordinate newSegEndPt=Iox2jtsext.getArcEndPt(segment);
						curve=new ArcSegment(lastSegmentEndpoint,newSegMidPt,newSegEndPt);
						if(((ArcSegment) curve).isStraight()){
							EhiLogger.logAdaption("arc converted to straight at tid "+currentBoundaryTid+", "+((ArcSegment) curve).getMidPoint());
							curve=new StraightSegment(curve.getStartPoint(),curve.getEndPoint());
						}
					}else{
						// custum line form
						throw new IoxException("custom line form not supported");
						//out.startElement(segment->getTag(),0,0);
						//writeAttrs(out,segment);
						//out.endElement(/*segment*/);
					}
					if(curve!=null){
						/*IF neuer Punkt weniger als zwei Einheiten entfernt von altem Punkt
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
						*/
						int endPointIdx=findEndpointOnBoundary(currentBoundary,curve);
						// IF neuer Punkt identisch mit erstem Punkt auf bisherigem Rand
						if(endPointIdx==0){
							// Rand fertig; Rand in Zwischenspeicher
							currentBoundary.add(curve);
							boundaries.add(currentBoundary);
							boundaryTids.add(currentBoundaryTid);
							// neuen Rand anfangen
							currentBoundary=new ArrayList<CurveSegment>();
						}else if(endPointIdx>0){
							// IF neuer Punkt identisch mit Punkt auf bisherigem Rand
							// THEN neuer Rand (von Punkt bis neur Punkt) in Zwischenspeicher; bisheriger Randanfang abschliessen und neuen Rand anfrangen
							currentBoundary.add(curve);
							java.util.List<CurveSegment> sub=currentBoundary.subList(endPointIdx, currentBoundary.size());
							ArrayList<CurveSegment> boundary=new ArrayList<CurveSegment>();
							boundary.addAll(sub);
							boundaries.add(boundary);
							boundaryTids.add(currentBoundaryTid);
							sub.clear(); // remove boundary from currentBoundary
							// bisheriger Randanfang abschliessen (so dass es einen Knoten hat, und allenfalls ein overlap beim polygonieren entfernt wird)
							boundaries.add(currentBoundary);
							boundaryTids.add(currentBoundaryTid);
							// neuen Rand anfangen
							currentBoundary=new ArrayList<CurveSegment>();
							
							
						}else if(false){
							// TODO IF neuer Punkt liegt auf bisherigem Rand
							// THEN Fehler; Abbruch
						}else{
							// Rand normal fortsetzen
							currentBoundary.add(curve);
						}
						lastSegmentEndpoint=curve.getEndPoint();
					}
				}
				if(clipped){
					//out.endElement(/*CLIPPED*/);
				}
			}
		}
		if(currentBoundary!=null && currentBoundary.size()>0){
			boundaries.add(currentBoundary);
			boundaryTids.add(currentBoundaryTid);
		}
		
		HashMap<String,Integer> tidCount=new HashMap<String,Integer>();
        HashMap<String,Integer> tidIdxs=new HashMap<String,Integer>();
		for(String tid:boundaryTids) {
		    Integer count=tidCount.get(tid);
		    if(count==null) {
		        tidCount.put(tid, 1);
                tidIdxs.put(tid, 1);
		    }else {
		        count+=1;
		        tidCount.put(tid, count);
		    }
		}
		ArrayList<CompoundCurve> segv=new ArrayList<CompoundCurve>(); 
		java.util.Iterator<String> tids=boundaryTids.iterator();
		for(ArrayList<CurveSegment> boundaryLine:boundaries){
            String tid=tids.next();
            int tidIdx=tidIdxs.get(tid);
            tidIdxs.put(tid,tidIdx+1);
            if(tidCount.get(tid)>1) {
                tid=tid+":"+tidIdx;
            }
            CompoundCurve boundary=null;
            try {
                boundary=jtsFact.createCompoundCurve(boundaryLine);
            }catch(RuntimeException ex) {
                throw new IoxInvalidDataException("failed to create line "+tid,ex);
            }
			boundary.setUserData(tid);
			boundary.setSegmentsUserData(tid);
			segv.add(boundary);
		}
		
		return segv;
		
	}
	private int findEndpointOnBoundary(ArrayList<CurveSegment> currentBoundary,
			CurveSegment curve) throws IoxException {
		for(int i=0;i<currentBoundary.size();i++){
			if(curve.getEndPoint().equals2D(currentBoundary.get(i).getStartPoint())){
				return i;
			}
		}
		return -1;
	}

}
