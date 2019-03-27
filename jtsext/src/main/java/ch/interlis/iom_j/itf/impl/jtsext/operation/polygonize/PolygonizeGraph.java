package ch.interlis.iom_j.itf.impl.jtsext.operation.polygonize;

import java.util.*;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iom_j.itf.impl.jtsext.algorithm.CurveSegmentIntersector;
import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurvePolygon;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;
import ch.interlis.iom_j.itf.impl.jtsext.noding.Intersection;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jts.planargraph.*;

/**
 * Represents a planar graph of edges that can be used to compute a
 * polygonization, and implements the algorithms to compute the
 * {@link EdgeRings} formed by the graph.
 * <p>
 * The marked flag on {@link DirectedEdge}s is used to indicate that a directed edge
 * has be logically deleted from the graph.
 *
 */
class PolygonizeGraph
    extends PlanarGraph
{
	final boolean doDetailTrace=false;

  private static int getDegreeNonDeleted(Node node)
  {
    List edges = node.getOutEdges().getEdges();
    int degree = 0;
    for (Iterator i = edges.iterator(); i.hasNext(); ) {
      PolygonizeDirectedEdge de = (PolygonizeDirectedEdge) i.next();
      if (! de.isMarked()) degree++;
    }
    return degree;
  }

  private static int getDegree(Node node, long label)
  {
    List edges = node.getOutEdges().getEdges();
    int degree = 0;
    for (Iterator i = edges.iterator(); i.hasNext(); ) {
      PolygonizeDirectedEdge de = (PolygonizeDirectedEdge) i.next();
      if (de.getLabel() == label) degree++;
    }
    return degree;
  }

  /**
   * Deletes all edges at a node
   */
  public static void deleteAllEdges(Node node)
  {
    List edges = node.getOutEdges().getEdges();
    for (Iterator i = edges.iterator(); i.hasNext(); ) {
      PolygonizeDirectedEdge de = (PolygonizeDirectedEdge) i.next();
      de.setMarked(true);
      PolygonizeDirectedEdge sym = (PolygonizeDirectedEdge) de.getSym();
      if (sym != null)
        sym.setMarked(true);
    }
  }

  private GeometryFactory factory;

  //private List labelledRings;
 
  /**
   * Create a new polygonization graph.
   */
  public PolygonizeGraph(GeometryFactory factory)
  {
    this.factory = factory;
  }

  /**
   * Add a {@link LineString} forming an edge of the polygon graph.
   * @param line the line to add
   */
  public void addEdge(LineString line)
  {
    if (line.isEmpty()) { return; }
    
    Coordinate[] linePts = CoordinateArrays.removeRepeatedPoints(line.getCoordinates());
    
    if (linePts.length < 2) { return; }
    
    Coordinate startPt = line.getStartPoint().getCoordinate();
    Coordinate endPt = line.getEndPoint().getCoordinate();

    Node nStart = getNode(startPt);
    Node nEnd = getNode(endPt);

    double maxOverlap=0.0;
    DirectedEdge de0 = new PolygonizeDirectedEdge(nStart, nEnd, CompoundCurve.getDirectionPt(line,true,0.0), true);
    DirectedEdge de1 = new PolygonizeDirectedEdge(nEnd, nStart, CompoundCurve.getDirectionPt(line,false,0.0), false);
    Edge edge = new PolygonizeEdge(line);
    edge.setDirectedEdges(de0, de1);
    add(edge);
  }

  private Node getNode(Coordinate pt)
  {
    Node node = findNode(pt);
    if (node == null) {
      node = new Node(pt);
      // ensure node is only added once to graph
      add(node);
    }
    return node;
  }

  private void computeNextCWEdges()
  {
    // set the next pointers for the edges around each node
    for (Iterator iNode = nodeIterator(); iNode.hasNext(); ) {
      Node node = (Node) iNode.next();
      computeNextCWEdges(node);
      if(doDetailTrace) {
          DirectedEdgeStar deStar = node.getOutEdges();
          List<PolygonizeDirectedEdge> edges = getActiveOutgoingEdges(deStar);
          printEdges("computeNextCWEdges "+node.getCoordinate()+": ",edges);
      }
    }
  }

  /**
   * Convert the maximal edge rings found by the initial graph traversal
   * into the minimal edge rings required by JTS polygon topology rules.
   *
   * @param ringEdges the list of start edges for the edgeRings to convert.
   */
  private void convertMaximalToMinimalEdgeRings(List ringEdges)
  {
    for (Iterator i = ringEdges.iterator(); i.hasNext(); ) {
      PolygonizeDirectedEdge de = (PolygonizeDirectedEdge) i.next();
      long label = de.getLabel();
      List intNodes = findIntersectionNodes(de, label);

      if (intNodes == null) continue;
      // flip the next pointers on the intersection nodes to create minimal edge rings
      for (Iterator iNode = intNodes.iterator(); iNode.hasNext(); ) {
        Node node = (Node) iNode.next();
        computeNextCCWEdges(node, label);
      }
    }
  }

  /**
   * Finds all nodes in a maximal edgering which are self-intersection nodes
   * @param startDE
   * @param label
   * @return the list of intersection nodes found,
   * or <code>null</code> if no intersection nodes were found
   */
  private static List findIntersectionNodes(PolygonizeDirectedEdge startDE, long label)
  {
    PolygonizeDirectedEdge de = startDE;
    List intNodes = null;
    do {
      Node node = de.getFromNode();
      if (getDegree(node, label) > 1) {
        if (intNodes == null)
          intNodes = new ArrayList();
        intNodes.add(node);
      }

      de = de.getNext();
      Assert.isTrue(de != null, "found null DE in ring");
      Assert.isTrue(de == startDE || ! de.isInRing(), "found DE already in ring");
    } while (de != startDE);

    return intNodes;
  }

  public void removeOverlaps(double newVertexOffset)
  {
		CurveSegmentIntersector li = new CurveSegmentIntersector();
	    for (Iterator iNode = nodeIterator(); iNode.hasNext(); ) {
	        Node node = (Node) iNode.next();
	        //computeNextCWEdges(node);
            DirectedEdgeStar deStar = node.getOutEdges();
	        boolean overlapRemoved=false;
            do{
    	        overlapRemoved=false;
                PolygonizeDirectedEdge startDE = null;
                PolygonizeDirectedEdge prevDE = null;
                List<PolygonizeDirectedEdge> edges = getActiveOutgoingEdges(deStar);
                if(doDetailTrace)printEdges("removeOverlaps "+node.getCoordinate()+": ",edges);
                int edgec=edges.size();
                double maxDist=0.0;
                // calculate distance for evaluation of curve direction
                for(int i=0;i<edgec;i++){
                    PolygonizeDirectedEdge currentDE = edges.get(i);
                    for(int j=i+1;j<edgec;j++){
                        PolygonizeDirectedEdge nextDE = edges.get(j);
                        if((currentDE.isArc() || nextDE.isArc()) && Math.abs(currentDE.getAngle()-nextDE.getAngle())<0.0001){
                        	maxDist=2*newVertexOffset;
                        	if(doDetailTrace)System.out.println("  angleDiff "+(currentDE.getAngle()-nextDE.getAngle()));
                        }
                        Coordinate is=getIntersection(li,node.getCoordinate(),currentDE, nextDE);
                        if(is!=null){
                        	double dist=CurveSegment.dist(node.getCoordinate(),is);
                        	if(dist>maxDist){
                        		maxDist=dist;
                        	}
                        }
                    }
                }
                // reorder edges based on curve direction at given distance to node 
                if(maxDist>0){
                	maxDist=maxDist*1.1;
                	if(doDetailTrace)System.out.println("  maxDist "+maxDist);
                    for(int i=0;i<edgec;i++){
                        PolygonizeDirectedEdge currentDE = edges.get(i);
                        currentDE.adjustDirectionPt(maxDist);
                    }
        		    Collections.sort(deStar.getEdges());
                    edges = getActiveOutgoingEdges(deStar);
                    if(doDetailTrace)printEdges("   reordered ",edges);
                }
                
                // the edges are stored in CCW order around the star
                for (Iterator i = deStar.getEdges().iterator(); i.hasNext(); ) {
                  PolygonizeDirectedEdge currentDE = (PolygonizeDirectedEdge) i.next();
                  if (currentDE.isMarked()) continue;
                  if (startDE == null){
                    startDE = currentDE;
                  }
                  if (prevDE != null) {
                	  // check outDE,prevDE
                	  boolean removed=removeOverlap(li,node.getCoordinate(),prevDE, currentDE,newVertexOffset);
                	  overlapRemoved=overlapRemoved || removed;
                  }
                  prevDE = currentDE;
                }
                if (prevDE != null) {
                	  // check prevDE,startDE
                	boolean removed=removeOverlap(li,node.getCoordinate(),prevDE, startDE,newVertexOffset);
                	overlapRemoved=overlapRemoved || removed;
                }
                if(overlapRemoved){
                	if(doDetailTrace)printEdges("   overlapRemoved ",edges);
                }
            }while(overlapRemoved);
	      }
	  
  }

void printEdges(String tag,List<PolygonizeDirectedEdge> edges) {
		System.out.print(tag);
		int edgec=edges.size();
		for(int i=0;i<edgec;i++){
		    PolygonizeDirectedEdge currentDE = edges.get(i);
		    System.out.print(((PolygonizeEdge)currentDE.getEdge()).getLine().getUserData()+"{a "+currentDE.getAngle()+"}, ");
		}            	
		System.out.println();System.out.flush();
}

private List<PolygonizeDirectedEdge> getActiveOutgoingEdges(
		DirectedEdgeStar deStar) {
	List<PolygonizeDirectedEdge> edges=new ArrayList<PolygonizeDirectedEdge>();
	for (Iterator i = deStar.getEdges().iterator(); i.hasNext(); ) {
	  PolygonizeDirectedEdge currentDE = (PolygonizeDirectedEdge) i.next();
	  if (currentDE.isMarked()) continue;
	  edges.add(currentDE);
	}
	return edges;
}

private boolean removeOverlap(CurveSegmentIntersector li,Coordinate node,
		PolygonizeDirectedEdge de0, PolygonizeDirectedEdge de1,double newVertexOffset) {
	boolean overlapRemoved=false;
	PolygonizeEdge edge0=(PolygonizeEdge)de0.getEdge();
	  CompoundCurve line0 = (CompoundCurve)edge0.getLine();
	  CurveSegment s0=null;
		int s0idx = 0;
		if (de0.getEdgeDirection() == true) {
			s0idx = 0;
		} else {
			s0idx = line0.getNumSegments() - 1;

		}
		s0 = line0.getSegments().get(s0idx);
	  boolean line0AtStart=false;
	  if(s0.getStartPoint().equals2D(node)){
		  line0AtStart=true;
	  }
	  PolygonizeEdge edge1=(PolygonizeEdge)de1.getEdge();
	  CompoundCurve line1 = (CompoundCurve)edge1.getLine();
	  CurveSegment s1=null;
	  int s1idx=0;
		if (de1.getEdgeDirection() == true) {
			s1idx = 0;
		} else {
			s1idx = line1.getNumSegments() - 1;
		}
		s1=line1.getSegments().get(s1idx);
		  boolean line1AtStart=false;
		  if(s1.getStartPoint().equals2D(node)){
			  line1AtStart=true;
		  }
	if(s0 instanceof StraightSegment && s1 instanceof StraightSegment){
		// could not intersect, if correct noded
	}else{
		if(s0.getStartPoint().equals2D(s1.getStartPoint()) && s0.getEndPoint().equals2D(s1.getEndPoint())
				|| s0.getStartPoint().equals2D(s1.getEndPoint()) && s0.getEndPoint().equals2D(s1.getStartPoint())){
			// eye (two lines with only one segment (Arc and one Straight/Arc))
			// if eye with lines that have more than one segment, this case should have been already detected, because then it is not correct noded 
		}else{
			  li.computeIntersection(s0, s1);
			  if(li.hasIntersection()){
				  // remove it
				  Intersection is=null;
				  if(li.getIntersectionNum()==1){
					  if(!li.isIntersection(node)){
						  throw new IllegalStateException("unexpected overlap tid1 "+is.getCurve1().getUserData()+", tid2 "+is.getCurve2().getUserData()+", coord "+is.getPt()[0].toString()+(is.getPt().length==2?(", coord2 "+is.getPt()[1].toString()):""));
					  }
				  }else{
					  li.isOverlay(); // TODO ceis 20180501 is overlay a special case here?
					  Coordinate is0=li.getIntersection(0);
					  Coordinate is1=li.getIntersection(1);
					  if(is0.equals2D(node)){
			    		  is=new Intersection(is1,line0,line1,s0,s1,li.getOverlap());
					  }else if(is1.equals2D(node)){
			    		  is=new Intersection(is0,line0,line1,s0,s1,li.getOverlap());
					  }else{
						  throw new IllegalStateException("unexpected overlap "+is.toString());
					  }
					  if(is!=null){
							EhiLogger.traceState(CurvePolygon.VALID_OVERLAP+" "+is.toString());
					  }
		    		  if(s0 instanceof StraightSegment){
		    			  if(line1.removeOverlap((ArcSegment) s1,s0,newVertexOffset)){
				    		  de1.adjustDirectionPt();
				    		  overlapRemoved=true;
		    			  }
		    		  }else if(s1 instanceof StraightSegment){
		    			  if(line0.removeOverlap((ArcSegment) s0,s1,newVertexOffset)){
				    		  de0.adjustDirectionPt();
				    		  overlapRemoved=true;
		    			  }
		    		  }else{
		    			  // both segments are arcs
			    		  if(((ArcSegment) s0).getRadius()>((ArcSegment) s1).getRadius()){
			    			  if(line1.removeOverlap((ArcSegment) s1,s0,newVertexOffset)){
					    		  de1.adjustDirectionPt();
					    		  overlapRemoved=true;
			    			  }
			    		  }else{
			    			  if(line0.removeOverlap((ArcSegment) s0,s1,newVertexOffset)){
					    		  de0.adjustDirectionPt();
					    		  overlapRemoved=true;
			    			  }
			    		  }
		    		  }
				  }
			
			  }
		}
	}
	return  overlapRemoved;
}
private Coordinate getIntersection(CurveSegmentIntersector li,Coordinate node,
		PolygonizeDirectedEdge de0, PolygonizeDirectedEdge de1) {
	PolygonizeEdge edge0=(PolygonizeEdge)de0.getEdge();
	  CompoundCurve line0 = (CompoundCurve)edge0.getLine();
	  CurveSegment s0=null;
		int s0idx = 0;
		if (de0.getEdgeDirection() == true) {
			s0idx = 0;
		} else {
			s0idx = line0.getNumSegments() - 1;

		}
		s0 = line0.getSegments().get(s0idx);
	  PolygonizeEdge edge1=(PolygonizeEdge)de1.getEdge();
	  CompoundCurve line1 = (CompoundCurve)edge1.getLine();
	  CurveSegment s1=null;
	  int s1idx=0;
		if (de1.getEdgeDirection() == true) {
			s1idx = 0;
		} else {
			s1idx = line1.getNumSegments() - 1;
		}
		s1=line1.getSegments().get(s1idx);
	if(s0 instanceof StraightSegment && s1 instanceof StraightSegment){
		// could not intersect, if correct noded
	}else{
		if(s0.getStartPoint().equals2D(s1.getStartPoint()) && s0.getEndPoint().equals2D(s1.getEndPoint())
				|| s0.getStartPoint().equals2D(s1.getEndPoint()) && s0.getEndPoint().equals2D(s1.getStartPoint())){
			// eye (two lines with only one segment (Arc and one Straight/Arc))
			// if eye with lines that have more than one segment, this case should have been already detected, because then it is not correct noded 
		}else{
			  li.computeIntersection(s0, s1);
			  if(li.hasIntersection()){
				  if(li.getIntersectionNum()==1){
					  if(!li.isIntersection(node)){
						  throw new IllegalStateException("unexpected overlap");
					  }
				  }else{
					  li.isOverlay(); // TODO ceis 20180501 is overlay a special case here?
					  Coordinate is0=li.getIntersection(0);
					  Coordinate is1=li.getIntersection(1);
					  if(is0.equals2D(node)){
			    		  return is1;
					  }else if(is1.equals2D(node)){
			    		  return is0;
					  }else{
						  throw new IllegalStateException("unexpected overlap");
					  }
				  }
			
			  }
		}
	}
	return  null;
}
  /**
   * Computes the minimal EdgeRings formed by the edges in this graph.
   * @return a list of the {@link EdgeRing}s found by the polygonization process.
   */
  public List getEdgeRings()
  {
    // maybe could optimize this, since most of these pointers should be set correctly already
    // by deleteCutEdges()
	// 20150709 CEIS do not remove, because removeOverlas() changes the ordering
    computeNextCWEdges();
    // clear labels of all edges in graph
    label(dirEdges, -1);
    List maximalRings = findLabeledEdgeRings(dirEdges);
    convertMaximalToMinimalEdgeRings(maximalRings);

    // find all edgerings (which will now be minimal ones, as required)
    List edgeRingList = new ArrayList();
    for (Iterator i = dirEdges.iterator(); i.hasNext(); ) {
      PolygonizeDirectedEdge de = (PolygonizeDirectedEdge) i.next();
      if (de.isMarked()) continue;
      if (de.isInRing()) continue;

      EdgeRing er = findEdgeRing(de);
      if(doDetailTrace) {
    	  printEdges("getEdgeRings:", (List<PolygonizeDirectedEdge>)er.getEdges());
      }
      edgeRingList.add(er);
    }
    return edgeRingList;
  }

  /**
   * Finds and labels all edgerings in the graph.
   * The edge rings are labelling with unique integers.
   * The labelling allows detecting cut edges.
   * 
   * @param dirEdges a List of the DirectedEdges in the graph
   * @return a List of DirectedEdges, one for each edge ring found
   */
  private List findLabeledEdgeRings(Collection dirEdges)
  {
    List edgeRingStarts = new ArrayList();
    // label the edge rings formed
    long currLabel = 1;
    for (Iterator i = dirEdges.iterator(); i.hasNext(); ) {
      PolygonizeDirectedEdge de = (PolygonizeDirectedEdge) i.next();
      if (de.isMarked()) continue;
      if (de.getLabel() >= 0) continue;

      edgeRingStarts.add(de);
      List edges = findDirEdgesInRing(de);
      if(doDetailTrace) {
    	  printEdges("findLabeledEdgeRings:",edges);
      }
      label(edges, currLabel);
      currLabel++;
    }
    return edgeRingStarts;
  }

  /**
   * Finds and removes all cut edges from the graph.
   * @return a list of the {@link LineString}s forming the removed cut edges
   */
  public List deleteCutEdges()
  {
    computeNextCWEdges();
    // label the current set of edgerings
    findLabeledEdgeRings(dirEdges);

    /**
     * Cut Edges are edges where both dirEdges have the same label.
     * Delete them, and record them
     */
    List cutLines = new ArrayList();
    for (Iterator i = dirEdges.iterator(); i.hasNext(); ) {
      PolygonizeDirectedEdge de = (PolygonizeDirectedEdge) i.next();
      if (de.isMarked()) continue;

      PolygonizeDirectedEdge sym = (PolygonizeDirectedEdge) de.getSym();

      if (de.getLabel() == sym.getLabel()) {
        de.setMarked(true);
        sym.setMarked(true);

        // save the line as a cut edge
        PolygonizeEdge e = (PolygonizeEdge) de.getEdge();
        cutLines.add(e.getLine());
      }
    }
    return cutLines;
  }

  private static void label(Collection dirEdges, long label)
  {
    for (Iterator i = dirEdges.iterator(); i.hasNext(); ) {
      PolygonizeDirectedEdge de = (PolygonizeDirectedEdge) i.next();
      de.setLabel(label);
    }
  }
  private static void computeNextCWEdges(Node node)
  {
    DirectedEdgeStar deStar = node.getOutEdges();
    PolygonizeDirectedEdge startDE = null;
    PolygonizeDirectedEdge prevDE = null;

    // the edges are stored in CCW order around the star
    for (Iterator i = deStar.getEdges().iterator(); i.hasNext(); ) {
      PolygonizeDirectedEdge outDE = (PolygonizeDirectedEdge) i.next();
      if (outDE.isMarked()) continue;

      if (startDE == null)
        startDE = outDE;
      if (prevDE != null) {
        PolygonizeDirectedEdge sym = (PolygonizeDirectedEdge) prevDE.getSym();
        sym.setNext(outDE);
      }
      prevDE = outDE;
    }
    if (prevDE != null) {
      PolygonizeDirectedEdge sym = (PolygonizeDirectedEdge) prevDE.getSym();
      sym.setNext(startDE);
    }
  }
  /**
   * Computes the next edge pointers going CCW around the given node, for the
   * given edgering label.
   * This algorithm has the effect of converting maximal edgerings into minimal edgerings
   */
  private static void computeNextCCWEdges(Node node, long label)
  {
    DirectedEdgeStar deStar = node.getOutEdges();
    //PolyDirectedEdge lastInDE = null;
    PolygonizeDirectedEdge firstOutDE = null;
    PolygonizeDirectedEdge prevInDE = null;

    // the edges are stored in CCW order around the star
    List edges = deStar.getEdges();
    //for (Iterator i = deStar.getEdges().iterator(); i.hasNext(); ) {
    for (int i = edges.size() - 1; i >= 0; i--) {
      PolygonizeDirectedEdge de = (PolygonizeDirectedEdge) edges.get(i);
      PolygonizeDirectedEdge sym = (PolygonizeDirectedEdge) de.getSym();

      PolygonizeDirectedEdge outDE = null;
      if (  de.getLabel() == label) outDE = de;
      PolygonizeDirectedEdge inDE = null;
      if (  sym.getLabel() == label) inDE =  sym;

      if (outDE == null && inDE == null) continue;  // this edge is not in edgering

      if (inDE != null) {
        prevInDE = inDE;
      }

      if (outDE != null) {
        if (prevInDE != null) {
          prevInDE.setNext(outDE);
          prevInDE = null;
        }
        if (firstOutDE == null)
          firstOutDE = outDE;
      }
    }
    if (prevInDE != null) {
      Assert.isTrue(firstOutDE != null);
      prevInDE.setNext(firstOutDE);
    }
  }

  /**
   * Traverses a ring of DirectedEdges, accumulating them into a list.
   * This assumes that all dangling directed edges have been removed
   * from the graph, so that there is always a next dirEdge.
   *
   * @param startDE the DirectedEdge to start traversing at
   * @return a List of DirectedEdges that form a ring
   */
  private static List findDirEdgesInRing(PolygonizeDirectedEdge startDE)
  {
    PolygonizeDirectedEdge de = startDE;
    List edges = new ArrayList();
    do {
      edges.add(de);
      de = de.getNext();
      Assert.isTrue(de != null, "found null DE in ring");
      Assert.isTrue(de == startDE || ! de.isInRing(), "found DE already in ring");
    } while (de != startDE);

    return edges;
  }

  private EdgeRing findEdgeRing(PolygonizeDirectedEdge startDE)
  {
    PolygonizeDirectedEdge de = startDE;
    EdgeRing er = new EdgeRing(factory);
    do {
      er.add(de);
      de.setRing(er);
      de = de.getNext();
      Assert.isTrue(de != null, "found null DE in ring");
      Assert.isTrue(de == startDE || ! de.isInRing(), "found DE already in ring");
    } while (de != startDE);

    return er;
  }

  /**
   * Marks all edges from the graph which are "dangles".
   * Dangles are which are incident on a node with degree 1.
   * This process is recursive, since removing a dangling edge
   * may result in another edge becoming a dangle.
   * In order to handle large recursion depths efficiently,
   * an explicit recursion stack is used
   *
   * @return a List containing the {@link LineString}s that formed dangles
   */
  public Collection deleteDangles()
  {
    List nodesToRemove = findNodesOfDegree(1);
    Set dangleLines = new HashSet();

    Stack nodeStack = new Stack();
    for (Iterator i = nodesToRemove.iterator(); i.hasNext(); ) {
      nodeStack.push(i.next());
    }

    while (! nodeStack.isEmpty()) {
      Node node = (Node) nodeStack.pop();

      deleteAllEdges(node);
      List nodeOutEdges = node.getOutEdges().getEdges();
      for (Iterator i = nodeOutEdges.iterator(); i.hasNext(); ) {
        PolygonizeDirectedEdge de = (PolygonizeDirectedEdge) i.next();
        // delete this edge and its sym
        de.setMarked(true);
        PolygonizeDirectedEdge sym = (PolygonizeDirectedEdge) de.getSym();
        if (sym != null)
          sym.setMarked(true);

        // save the line as a dangle
        PolygonizeEdge e = (PolygonizeEdge) de.getEdge();
        dangleLines.add(e.getLine());

        Node toNode = de.getToNode();
        // add the toNode to the list to be processed, if it is now a dangle
        if (getDegreeNonDeleted(toNode) == 1)
          nodeStack.push(toNode);
      }
    }
    return dangleLines;
  }
  
  /**
   * Traverses the polygonized edge rings in the graph
   * and computes the depth parity (odd or even)
   * relative to the exterior of the graph.
   * If the client has requested that the output
   * be polygonally valid, only odd polygons will be constructed. 
   *
   */
  public void computeDepthParity()
  {
    while (true) {
      PolygonizeDirectedEdge de = null; //findLowestDirEdge();
      if (de == null)
        return;
      computeDepthParity(de);
    }
  }
  
  /**
   * Traverses all connected edges, computing the depth parity
   * of the associated polygons.
   * 
   * @param de
   */
  private void computeDepthParity(PolygonizeDirectedEdge de)
  {
    
  }
  
}
