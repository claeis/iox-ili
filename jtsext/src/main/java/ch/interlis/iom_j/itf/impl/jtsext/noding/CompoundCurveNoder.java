package ch.interlis.iom_j.itf.impl.jtsext.noding;

import java.util.*;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.iox.objpool.ObjectPoolManager;
import ch.ehi.iox.objpool.impl.CompoundCurveSerializer;
import ch.ehi.iox.objpool.impl.FileBasedCollection;
import ch.ehi.iox.objpool.impl.SortedSetIntegerSerializer;
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

  private List<? extends CompoundCurve> segStrings;
  private List<CompoundCurve> nodedStrings;
  private List<Intersection> segInt=null;
  private Map<CompoundCurve,SortedSet<Integer>> nodes=null;
  private boolean isNoded=false;
  private boolean validateOnly=false;
  private boolean enableCommonSegments=false;
  
  /**
   * Creates a new noding validator for a given set of linework.
   * 
   * @param segStrings a collection of {@link CompoundCurve}s
   * @param validateOnly if true, no noding is done. New nodes are only created at start/end of segements.
   */
  public CompoundCurveNoder(List segStrings,boolean validateOnly)
  {
    this.segStrings = segStrings;
    this.validateOnly=validateOnly;
    nodes=new HashMap<CompoundCurve,SortedSet<Integer>>();
	nodedStrings=new java.util.ArrayList<CompoundCurve>();
  }
  public CompoundCurveNoder(ObjectPoolManager objPoolManager,List segStrings,boolean validateOnly)
  {
    this.segStrings = segStrings;
    this.validateOnly=validateOnly;
    nodes=objPoolManager.newObjectPool2(this.getClass().getSimpleName(),new CompoundCurveSerializer(),new SortedSetIntegerSerializer());
	nodedStrings=new FileBasedCollection<CompoundCurve>(objPoolManager,this.getClass().getSimpleName(),new CompoundCurveSerializer());
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

  private HashMap<CurvePairInt,HashSet<Coordinate>> compared=new HashMap<CurvePairInt,HashSet<Coordinate>>();
  private void checkIntersections()
  {
	segInt=new java.util.ArrayList<Intersection>();
	STRtree polyidx=new STRtree();
    for (int i=0;i<segStrings.size();i++) {
    	CompoundCurve e0 =  segStrings.get(i);
    	Envelope env=new Envelope(e0.getEnvelopeInternal());
		polyidx.insert(env, i);
    }
    for (int i=0;i<segStrings.size();i++) {
    	CompoundCurve e0 =  segStrings.get(i);
		List<Integer> hits=polyidx.query(e0.getEnvelopeInternal());
        for (int e1_i :  hits) {
        	CurvePairInt pair=new CurvePairInt(i,e1_i);
        	if(!compared.containsKey(pair)){
    	    	CompoundCurve e1 =  segStrings.get(e1_i);
        		compared.put(pair,new HashSet<Coordinate>());
                computeIntersects(i,e0, e1_i,e1);
        	}
        }
      }
  }

	/**
	 * Returns the lines, cut at the node points.
	 * If  no nodes were calculated beforehand by calling {@link #getIntersections} or {@link #isValid}, the lines are returned unchanged.
	 * The result is cached and not recalculated even if {@link #getIntersections} or {@link #isValid} is called after calling this method.
	 */
  public Collection<? extends CompoundCurve> getNodedSubstrings()
  {
	  if(isNoded){
		  return nodedStrings;
	  }
	  for(CompoundCurve line:segStrings){
		  if(!nodes.containsKey(line)){
			  nodedStrings.add(line);
		  }else{
			  int startSeg=0;
			  JtsextGeometryFactory fact=(JtsextGeometryFactory) line.getFactory();
			  for(int endSeg:nodes.get(line)){
				  CompoundCurve newLine=null;
					newLine = fact.createCompoundCurve(line.getSegments().subList(startSeg, endSeg));
					newLine.setUserData(line.getUserData());
					nodedStrings.add(newLine);
				  startSeg=endSeg;
			  }
			  CompoundCurve newLine=null;
				newLine = fact.createCompoundCurve(line.getSegments().subList(startSeg, line.getNumSegments()));
                newLine.setUserData(line.getUserData());
				nodedStrings.add(newLine);
		  }
	  }
	  isNoded=true;
	  return nodedStrings;
  }
private void computeIntersects(int ss0Idx,CompoundCurve ss0, int ss1Idx,CompoundCurve ss1) {
    Coordinate[] endPts1=new Coordinate[2];
    Coordinate[] endPts2=new Coordinate[2];
    endPts1[0]=ss0.getStartPoint().getCoordinate();
    endPts1[1]=ss0.getEndPoint().getCoordinate();
    endPts2[0]=ss1.getStartPoint().getCoordinate();
    endPts2[1]=ss1.getEndPoint().getCoordinate();
	
	STRtree index0 = new STRtree();
	for (int i = 0; i < ss0.getNumSegments(); i++) {
		CurveSegment seg = ss0.getSegments().get(i);
		index0.insert(seg.computeEnvelopeInternal(), i);
	}
	if (ss0 == ss1) {
		for (int i = 0; i < ss1.getNumSegments(); i++) {
			CurveSegment seg = ss1.getSegments().get(i);
			List<Integer> hits = index0.query(seg.computeEnvelopeInternal());
			Collections.sort(hits); // Necessary because segIntAdd might lose intersections if they are not added in order
			for (int hit : hits) {
				if (hit > i) {
					checkInteriorIntersections(ss0Idx,ss0, hit, ss1Idx,ss1, i,endPts1,endPts2);
				}
			}
		}
	} else {
		for (int i = 0; i < ss1.getNumSegments(); i++) {
			CurveSegment seg = ss1.getSegments().get(i);
			List<Integer> hits = index0.query(seg.computeEnvelopeInternal());
			Collections.sort(hits); // Necessary because segIntAdd might lose intersections if they are not added in order
			for (int hit : hits) {
				checkInteriorIntersections(ss0Idx,ss0, hit, ss1Idx,ss1, i,endPts1,endPts2);
			}
		}
	}
}
	private void checkInteriorIntersections(int e0_i,CompoundCurve e0, int segIndex0, int e1_i,CompoundCurve e1, int segIndex1,Coordinate endPts1[],Coordinate endPts2[])
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
					  segIntAdd(li.getIntersection(0),e0_i,e0,e1_i,e1,s0,s1,li.getOverlap());
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
							  segIntAdd(li.getIntersection(0),e0_i,e0,e1_i,e1,s0,s1,li.getOverlap());
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
							  segIntAdd(li.getIntersection(0),e0_i,e0,e1_i,e1,s0,s1,li.getOverlap());
						  }
					  }else{
						  // schnitt innerhalb von segment a und/oder b
						  segIntAdd(li.getIntersection(0),e0_i,e0,e1_i,e1,s0,s1,li.getOverlap());
					  }
				  }
			  }
		  }else{
			 // two intersection points 
			  if(li.isOverlay()) {
                  if(validateOnly){
                      segIntAdd(li.getIntersection(0),li.getIntersection(1),e0_i,e0,e1_i,e1,s0,s1,li.getOverlap(),true);
                  }else {
                      if(enableCommonSegments) {
                          // wenn es zwei Schnittpunkte gibt, muessen die Start und Endpunkt beider Segmente 
                          // sein Schnittpunkt sein, um diese Schnittpunkte als neue Knoten einfuegen zu koennen 
                          if((li.isIntersection(p00) && li.isIntersection(p01))  // start/end-punkt segment a
                                    && (li.isIntersection(p10) && li.isIntersection(p11))){ // start/end-punkt segment b
                                      createNode(e0,segIndex0);
                                      createNode(e0,segIndex0+1);
                                      createNode(e1,segIndex1);
                                      createNode(e1,segIndex1+1);
                          }else{
                              segIntAdd(li.getIntersection(0),li.getIntersection(1),e0_i,e0,e1_i,e1,s0,s1,li.getOverlap(),true);
                          }
                      }else {
                          segIntAdd(li.getIntersection(0),li.getIntersection(1),e0_i,e0,e1_i,e1,s0,s1,li.getOverlap(),true);
                      }
                  }
			  }else if((e0!=e1 && e0.getNumSegments()==1 && e1.getNumSegments()==1) // Zwei Linien, je ein Segment, die zusammen ein Ring bilden
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
					  segIntAdd(li.getIntersection(0),li.getIntersection(1),e0_i,e0,e1_i,e1,s0,s1,li.getOverlap(),false);
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
						  segIntAdd(li.getIntersection(0),li.getIntersection(1),e0_i,e0,e1_i,e1,s0,s1,li.getOverlap(),false);
					  }
				  }
			  }
		  }
	  }
	}


	private void segIntAdd(Coordinate intersection, Coordinate intersection2,
			int e0_i,CompoundCurve e0, int e1_i,CompoundCurve e1, CurveSegment s0,
			CurveSegment s1, Double overlap,boolean isOverlay) {
		HashSet<Coordinate> isList=compared.get(new CurvePairInt(e0_i, e1_i));
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
			Intersection is=new Intersection(intersection, intersection2, e0, e1, s0, s1, overlap,isOverlay);
			segInt.add(is);
		}
		
	}

	private void segIntAdd(Coordinate intersection, int e0_i,CompoundCurve e0,
			int e1_i,CompoundCurve e1, CurveSegment s0, CurveSegment s1, Double overlap) {
		HashSet<Coordinate> isList=compared.get(new CurvePairInt(e0_i, e1_i));
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
		nodes.put(line, toNodeIdx); // save modified list
	}


	public void checkValid() {
		if(!isValid()){
			throw new IllegalArgumentException("intersections");
		}
	}
    public boolean isEnableCommonSegments() {
        return enableCommonSegments;
    }
    public void setEnableCommonSegments(boolean removeCommonSegments) {
        this.enableCommonSegments = removeCommonSegments;
    }
}