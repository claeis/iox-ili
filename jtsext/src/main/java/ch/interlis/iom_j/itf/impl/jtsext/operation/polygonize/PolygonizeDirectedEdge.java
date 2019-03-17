package ch.interlis.iom_j.itf.impl.jtsext.operation.polygonize;

import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateArrays;
import com.vividsolutions.jts.geomgraph.Quadrant;
import com.vividsolutions.jts.planargraph.DirectedEdge;
import com.vividsolutions.jts.planargraph.Node;

/**
 * A {@link DirectedEdge} of a {@link PolygonizeGraph}, which represents
 * an edge of a polygon formed by the graph.
 * May be logically deleted from the graph by setting the <code>marked</code> flag.
 */
class PolygonizeDirectedEdge
    extends DirectedEdge
{

  private EdgeRing edgeRing = null;
  private PolygonizeDirectedEdge next = null;
  private long label = -1;

  /**
   * Constructs a directed edge connecting the <code>from</code> node to the
   * <code>to</code> node.
   *
   * @param directionPt
   *                  specifies this DirectedEdge's direction (given by an imaginary
   *                  line from the <code>from</code> node to <code>directionPt</code>)
   * @param edgeDirection
   *                  whether this DirectedEdge's direction is the same as or
   *                  opposite to that of the parent Edge (if any)
   */
  public PolygonizeDirectedEdge(Node from, Node to, Coordinate directionPt,
      boolean edgeDirection)
  {
    super(from, to, directionPt, edgeDirection);
  }
  @Override
    public double getAngle() {
        double ret=super.getAngle();
        if(Double.isNaN(ret)) {
            throw new IllegalStateException("angle is NaN");
        }
        return ret;
    }
  protected void adjustDirectionPt(double dist)
  {
		Coordinate directionPt = null;
		PolygonizeEdge edge = (PolygonizeEdge) getEdge();
		CompoundCurve line = (CompoundCurve) edge.getLine();
		directionPt = CompoundCurve.getDirectionPt(line, getEdgeDirection(),dist);

		p1 = new Coordinate(directionPt);
		double dx = p1.x - p0.x;
		double dy = p1.y - p0.y;
		quadrant = Quadrant.quadrant(dx, dy);
		angle = Math.atan2(dy, dx);
	  
  }
  protected void adjustDirectionPt()
  {
	  if(true){
		  Coordinate directionPt=null;
			 PolygonizeEdge edge=(PolygonizeEdge)getEdge();
			 CompoundCurve line = (CompoundCurve)edge.getLine();
		  directionPt=CompoundCurve.getDirectionPt(line,getEdgeDirection(),0.0);		  
		  
		  p1=new Coordinate(directionPt);
		    double dx = p1.x - p0.x;
		    double dy = p1.y - p0.y;
		    quadrant = Quadrant.quadrant(dx, dy);
		    angle = Math.atan2(dy, dx);
		  
	  }
  }
  public boolean isArc() {
		 PolygonizeEdge edge=(PolygonizeEdge)getEdge();
		 CompoundCurve line = (CompoundCurve)edge.getLine();
		return CompoundCurve.isArc(line,getEdgeDirection());
	}
  /**
   * Returns the identifier attached to this directed edge.
   */
  public long getLabel() { return label; }
  /**
   * Attaches an identifier to this directed edge.
   */
  public void setLabel(long label) { this.label = label; }
  /**
   * Returns the next directed edge in the EdgeRing that this directed edge is a member
   * of.
   */
  public PolygonizeDirectedEdge getNext()  {    return next;  }
  /**
   * Sets the next directed edge in the EdgeRing that this directed edge is a member
   * of.
   */
  public void setNext(PolygonizeDirectedEdge next)  {   this.next = next;  }
  /**
   * Returns the ring of directed edges that this directed edge is
   * a member of, or null if the ring has not been set.
   * @see #setRing(EdgeRing)
   */
  public boolean isInRing() { return edgeRing != null; }
  /**
   * Sets the ring of directed edges that this directed edge is
   * a member of.
   */
  public void setRing(EdgeRing edgeRing)
  {
      this.edgeRing = edgeRing;
  }

}
