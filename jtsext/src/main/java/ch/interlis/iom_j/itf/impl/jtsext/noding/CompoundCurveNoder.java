package ch.interlis.iom_j.itf.impl.jtsext.noding;

import java.util.*;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iom_j.itf.impl.jtsext.algorithm.CurveSegmentIntersector;
import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.index.strtree.STRtree;

/**
 * Validates that a collection of {@link CompoundCurve}s is correctly noded.
 */
public class CompoundCurveNoder 
{
	private CurveSegmentIntersector li = new CurveSegmentIntersector();

  private Collection<? extends CompoundCurve> segStrings;
  private Collection<? extends CompoundCurve> nodedStrings;
  private List<Intersection> segInt=null;
  private HashMap<CompoundCurve,SortedSet<Integer>> nodes=new HashMap<CompoundCurve,SortedSet<Integer>>();

  private boolean validateOnly=false;
  
  /**
   * Creates a new noding validator for a given set of linework.
   * 
   * @param segStrings a collection of {@link CompoundCurve}s
   * @param validateOnly if true, no noding is done
   */
  public CompoundCurveNoder(Collection<? extends CompoundCurve> segStrings,boolean validateOnly)
  {
    this.segStrings = segStrings;
    this.validateOnly=validateOnly;
  }
  
  public List<Intersection> getIntersections()
  {
	execute();
    return segInt;
  }

  /**
   * Checks for an intersection and 
   * reports if one is found.
   * 
   * @return true if the arrangement contains an interior intersection
   */
  public boolean isValid()
  {
  	execute();
  	return segInt.size()==0;
  }

  private void execute()
  {
  	if (segInt == null){
  		checkIntersections();
  	}
  }

  private HashMap<CurvePair,HashSet<Coordinate>> compared=new HashMap<CurvePair,HashSet<Coordinate>>();
  private void checkIntersections()
  {
	segInt=new java.util.ArrayList<Intersection>();
	if(true){
		STRtree polyidx=new STRtree();
	    for (CompoundCurve e0 :  segStrings) {
	    	Envelope env=new Envelope(e0.getEnvelopeInternal());
			polyidx.insert(env, e0);
	    }
	    for (CompoundCurve e0 :  segStrings) {
			List<CompoundCurve> hits=polyidx.query(e0.getEnvelopeInternal());
	        for (CompoundCurve e1 :  hits) {
	        	CurvePair pair=new CurvePair(e0,e1);
	        	if(!compared.containsKey(pair)){
	        		compared.put(pair,new HashSet<Coordinate>());
	                computeIntersects(e0, e1);
	        	}
	        }
	      }

	}else{
	    for (CompoundCurve e0 :  segStrings) {
	        for (CompoundCurve e1 :  segStrings) {
	        	CurvePair pair=new CurvePair(e0,e1);
	        	if(!compared.containsKey(pair)){
	        		compared.put(pair,new HashSet<Coordinate>());
	                computeIntersects(e0, e1);
	        	}
	        }
	      }
		
	}
  }

  public Collection<? extends CompoundCurve> getNodedSubstrings()
  {
	  if(nodedStrings!=null){
		  return nodedStrings;
	  }
	  java.util.ArrayList<CompoundCurve> ret=new java.util.ArrayList<CompoundCurve>();
	  for(CompoundCurve line:segStrings){
		  if(!nodes.containsKey(line)){
			  ret.add(line);
		  }else{
			  int startSeg=0;
			  JtsextGeometryFactory fact=(JtsextGeometryFactory) line.getFactory();
			  for(int endSeg:nodes.get(line)){
				  CompoundCurve newLine=null;
					newLine = fact.createCompoundCurve(line.getSegments().subList(startSeg, endSeg));
				  ret.add(newLine);
				  startSeg=endSeg;
			  }
			  CompoundCurve newLine=null;
				newLine = fact.createCompoundCurve(line.getSegments().subList(startSeg, line.getNumSegments()));
			  ret.add(newLine);
		  }
	  }
	  nodedStrings=ret;
	  return ret;
  }
private void computeIntersects(CompoundCurve ss0, CompoundCurve ss1) {
    Coordinate[] endPts1=new Coordinate[2];
    Coordinate[] endPts2=new Coordinate[2];
    endPts1[0]=ss0.getStartPoint().getCoordinate();
    endPts1[1]=ss0.getEndPoint().getCoordinate();
    endPts2[0]=ss1.getStartPoint().getCoordinate();
    endPts2[1]=ss1.getEndPoint().getCoordinate();
    if(ss0==ss1){
    	// diese optimierung bringt
        // 149188 ms
        // 106694 ms
        for (int i0 = 0; i0 < ss0.getNumSegments(); i0++) {
            for (int i1 = i0+1; i1 < ss1.getNumSegments(); i1++) {
              checkInteriorIntersections(ss0, i0, ss1, i1,endPts1,endPts2);
            }
          }
    }else{
        for (int i0 = 0; i0 < ss0.getNumSegments(); i0++) {
            for (int i1 = 0; i1 < ss1.getNumSegments(); i1++) {
              checkInteriorIntersections(ss0, i0, ss1, i1,endPts1,endPts2);
            }
          }
    }
	
}
	private void checkInteriorIntersections(CompoundCurve e0, int segIndex0, CompoundCurve e1, int segIndex1,Coordinate endPts1[],Coordinate endPts2[])
	{
	  if (e0 == e1 && segIndex0 == segIndex1) return;
	Coordinate p00;
	Coordinate p01;
	Coordinate p10;
	Coordinate p11;
	p00 = e0.getSegments().get(segIndex0).getStartPoint();
	p01 = e0.getSegments().get(segIndex0).getEndPoint();
	p10 = e1.getSegments().get(segIndex1).getStartPoint();
	p11 = e1.getSegments().get(segIndex1).getEndPoint();
	
	CurveSegment s0=e0.getSegments().get(segIndex0);
	CurveSegment s1=e1.getSegments().get(segIndex1);
	  li.computeIntersection(s0, s1);
	  if (li.hasIntersection()) {
		  if(li.getIntersectionNum()==1){
			  if(e0==e1 && (
					  Math.abs(segIndex0-segIndex1)==1 
					  || Math.abs(segIndex0-segIndex1)==e0.getNumSegments()-1  ) // bei Ring: letztes Segment und Erstes Segment
					  && (li.isIntersection(p00) || li.isIntersection(p01))
					  && (li.isIntersection(p10) || li.isIntersection(p11))){
				  // aufeinanderfolgende Segmente der selben Linie
				  // ok
			  }else if(e0!=e1 && (li.isIntersection(endPts1[0]) || li.isIntersection(endPts1[1]))
					  && (li.isIntersection(endPts2[0]) || li.isIntersection(endPts2[1]))){
				  // verschiedene Linien, die sich in ihren Enden Treffen
				  // ok
			  }else{
				  // Intersection
				  if(validateOnly){
					  segIntAdd(li.getIntersection(0),e0,e1,s0,s1,li.getOverlap());
				  }else{
					  //if((li.isIntersection(p00) || li.isIntersection(p01))  // start/end-punkt segment a
						//&& (li.isIntersection(p10) || li.isIntersection(p11))){ // start/end-punkt segment b
					  if(li.isIntersection(p00)){
						  if(li.isIntersection(p10)){
							  // schnittpunkt ist startpt segment a und startpt segment b
							  createNode(e0,segIndex0);
							  createNode(e1,segIndex1);
						  }else if(li.isIntersection(p11)){
							  // schnittpunkt ist startpt segment a und endpt segment b
							  createNode(e0,segIndex0);
							  createNode(e1,segIndex1+1);
						  }else{
							  // schnitt innerhalb von segment b
							  segIntAdd(li.getIntersection(0),e0,e1,s0,s1,li.getOverlap());
						  }
					  }else if(li.isIntersection(p01)){
						  if(li.isIntersection(p10)){
							  // schnittpunkt ist endpt segment a und startpt segment b
							  createNode(e0,segIndex0+1);
							  createNode(e1,segIndex1);
						  }else if(li.isIntersection(p11)){
							  // schnittpunkt ist endpt segment a und endpt segment b
							  createNode(e0,segIndex0+1);
							  createNode(e1,segIndex1+1);
						  }else{
							  // schnitt innerhalb von segment b
							  segIntAdd(li.getIntersection(0),e0,e1,s0,s1,li.getOverlap());
						  }
					  }else{
						  // schnitt innerhalb von segment a und/oder b
						  segIntAdd(li.getIntersection(0),e0,e1,s0,s1,li.getOverlap());
					  }
				  }
			  }
		  }else{
			 // two intersection points 
			  if((e0!=e1 && e0.getNumSegments()==1 && e1.getNumSegments()==1) // Zwei Linien, je ein Segment, die zusammen ein Ring bilden
					  && (li.isIntersection(endPts1[0]) && li.isIntersection(endPts1[1]))
					  && (li.isIntersection(endPts2[0]) && li.isIntersection(endPts2[1]))){
				  // ok (Schnittpunkte sind Endpunkte)
			  }else if((e0==e1 && e0.getNumSegments()==2) // Ring als eine Linie, zwei Segmente
						  && endPts1[0].equals2D(endPts1[1])
								 && (s0 instanceof ArcSegment || s1 instanceof ArcSegment) // TODO sollte nicht die selbe spur sein
					
						  ){
					  // ok (Schnittpunkte sind Endpunkte)
			  }else{
				  if(validateOnly){
					  segIntAdd(li.getIntersection(0),li.getIntersection(1),e0,e1,s0,s1,li.getOverlap());
					  
				  }else{
					  // wenn es zwei Schnittpunkte gibt, muessen die Start und Endpunkt beider Segmente 
					  // sein Schnittpunkt sein, um diese Schnittpunkte als neue Knoten einfuegen zu koennen 
					  if((li.isIntersection(p00) && li.isIntersection(p01))  // start/end-punkt segment a
								&& (li.isIntersection(p10) && li.isIntersection(p11))){ // start/end-punkt segment b
								  createNode(e0,segIndex0);
								  createNode(e0,segIndex0+1);
								  createNode(e1,segIndex1);
								  createNode(e1,segIndex1+1);
					  }else{
						  segIntAdd(li.getIntersection(0),li.getIntersection(1),e0,e1,s0,s1,li.getOverlap());
					  }
				  }
			  }
		  }
	  }
	}


	private void segIntAdd(Coordinate intersection, Coordinate intersection2,
			CompoundCurve e0, CompoundCurve e1, CurveSegment s0,
			CurveSegment s1, Double overlap) {
		HashSet<Coordinate> isList=compared.get(new CurvePair(e0, e1));
		boolean added=false;
		if(!isList.contains(intersection)){
			added=true;
			isList.add(intersection);
		}
		if(!isList.contains(intersection2)){
			added=true;
			isList.add(intersection2);
		}
		if(added){
			Intersection is=new Intersection(intersection, intersection2, e0, e1, s0, s1, overlap);
			segInt.add(is);
		}
		
	}

	private void segIntAdd(Coordinate intersection, CompoundCurve e0,
			CompoundCurve e1, CurveSegment s0, CurveSegment s1, Double overlap) {
		HashSet<Coordinate> isList=compared.get(new CurvePair(e0, e1));
		if(!isList.contains(intersection)){
			isList.add(intersection);
			Intersection is=new Intersection(intersection, e0, e1, s0, s1, overlap);
			segInt.add(is);
		}
	}

	private void createNode(CompoundCurve line, int nodeSegIndex) {
		if(nodeSegIndex==0){ // first segment
			return;
		}
		if(nodeSegIndex==line.getNumSegments()){ // last segment
			return;
		}
		SortedSet<Integer> toNodeIdx=null;
		if(nodes.containsKey(line)){
			toNodeIdx=nodes.get(line);
		}else{
			toNodeIdx=new TreeSet<Integer>();
			nodes.put(line, toNodeIdx);
		}
		toNodeIdx.add(nodeSegIndex);
	}


	public void checkValid() {
		if(!isValid()){
			throw new IllegalArgumentException("intersections");
		}
	}
}