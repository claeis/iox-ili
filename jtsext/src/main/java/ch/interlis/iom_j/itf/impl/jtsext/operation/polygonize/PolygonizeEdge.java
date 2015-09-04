package ch.interlis.iom_j.itf.impl.jtsext.operation.polygonize;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.planargraph.*;

/**
 * An edge of a polygonization graph.
 */
class PolygonizeEdge
    extends Edge
{
  private LineString line;

  public PolygonizeEdge(LineString line)
  {
    this.line = line;
  }
  public LineString getLine() { return line; }
}
