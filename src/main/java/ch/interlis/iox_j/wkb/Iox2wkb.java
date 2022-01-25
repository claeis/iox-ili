/* This file is part of the iox-ili project.
 * For more information, please see <http://www.eisenhutinformatik.ch/iox-ili/>.
 *
 * Copyright (c) 2006 Eisenhut Informatik AG
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */
package ch.interlis.iox_j.wkb;

import java.io.IOException;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.itf.impl.hrg.HrgUtility;
import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iox_j.jts.Iox2jtsException;

/** Utility to convert from INTERLIS to WKB geometry.
 * @author ceis
 * 
 * 
<point binary representation> ::=
	<byte order> <wkbpoint> [ <wkbpoint binary> ]

<linestring binary representation> ::=
	<byte order> <wkblinestring> [ <num> <wkbpoint binary>... ]

<circularstring binary representation> ::=
	<byte order> <wkbcircularstring> [ <num> <wkbpoint binary>... ]

<compoundcurve binary representation> ::=
	<byte order> <wkbcompoundcurve> [ <num> <wkbcurve binary>... ]

<curvepolygon binary representation> ::=
	<byte order> <wkbcurvepolygon> [ <num> <wkbring binary>... ]
	| <polygon binary representation>
	
<polygon binary representation> ::=
	<byte order> <wkbpolygon> [ <num> <wkblinearring binary>... ]
	| <triangle binary representation>

<triangle binary representation> ::=
	<byte order> <wkbtriangle>
	[ <wkbpoint binary> <wkbpoint binary> <wkbpoint binary> ]	

<wkbring binary> ::=
	<linestring binary representation>
	| <circularstring binary representation>
	| <compoundcurve binary representation>

<wkblinearring> ::= <num> <wkbpoint binary>...

<wkbcurve binary> ::=
	<linestring binary representation>
	| <circularstring binary representation>

<wkbpoint binary> ::= <wkbx> <wkby>	
 */
public class Iox2wkb {
    private int outputDimension = 2;
    private ByteArrayOutputStream os = null;
    private boolean asEWKB = true; 
	// utility, no instances
	private Iox2wkb(){}
    public Iox2wkb(int outputDimension) {
        this(outputDimension, java.nio.ByteOrder.BIG_ENDIAN, true);
    }
    public Iox2wkb(int outputDimension, java.nio.ByteOrder byteOrder) {
        this(outputDimension, byteOrder, true);
    }
    public Iox2wkb(int outputDimension, java.nio.ByteOrder byteOrder, boolean asEWKB) {
        this.outputDimension = outputDimension;
        this.asEWKB=asEWKB;
        os = new ByteArrayOutputStream(byteOrder);
        
        if (outputDimension < 2 || outputDimension > 3)
          throw new IllegalArgumentException("Output dimension must be 2 or 3");
    }

	public static String bytesToHex(byte[] bytes)
	  {
	    StringBuffer buf = new StringBuffer();
	    for (int i = 0; i < bytes.length; i++) {
	      byte b = bytes[i];
	      buf.append(toHexDigit((b >> 4) & 0x0F));
	      buf.append(toHexDigit(b & 0x0F));
	    }
	    return buf.toString();
	  }

	  private static char toHexDigit(int n)
	  {
	    if (n < 0 || n > 15)
	      throw new IllegalArgumentException("Nibble value out of range: " + n);
	    if (n <= 9)
	      return (char) ('0' + n);
	    return (char) ('A' + (n - 10));
	  }
	
	/** Converts a COORD to a JTS Coordinate.
	 * @param value INTERLIS COORD structure.
	 * @return JTS Coordinate.
	 * @throws Iox2wkbException
	 */
	public byte[] coord2wkb(IomObject value) 
	throws Iox2wkbException 
	{
		if(value==null){
			return null;
		}
	    try {
	        os.reset();
	        writeByteOrder();
	        writeGeometryType(WKBConstants.wkbPoint);
			writeCoord(value);
	      }
	      catch (IOException ex) {
	        throw new RuntimeException("Unexpected IO exception: " + ex.getMessage());
	      }
	      return os.toByteArray();
	}
	public byte[] multicoord2wkb(IomObject obj)
	throws Iox2wkbException
	{
		if(obj==null){
			return null;
		}
	    try {
			writeByteOrder();
			writeGeometryType(WKBConstants.wkbMultiPoint);
			int coordc=obj.getattrvaluecount(Wkb2iox.ATTR_COORD);
			os.writeInt(coordc);

			for(int coordi=0;coordi<coordc;coordi++){
				IomObject coord=obj.getattrobj(Wkb2iox.ATTR_COORD,coordi);
                Iox2wkb helper=new Iox2wkb(outputDimension,os.order(),asEWKB);
				os.write(helper.coord2wkb(coord));
			}
		} catch (IOException e) {
	        throw new RuntimeException("Unexpected IO exception: " + e.getMessage());
		}
		return os.toByteArray();
	}
	private void writeCoord(IomObject value) throws Iox2wkbException {
		String c1=value.getattrvalue("C1");
		String c2=value.getattrvalue("C2");
		String c3=value.getattrvalue("C3");
		double xCoord;
		try{
			xCoord = Double.parseDouble(c1);
		}catch(Exception ex){
			throw new Iox2wkbException("failed to read C1 <"+c1+">",ex);
		}
		double yCoord;
		try{
			yCoord = Double.parseDouble(c2);
		}catch(Exception ex){
			throw new Iox2wkbException("failed to read C2 <"+c2+">",ex);
		}
		double zCoord=0.0;
		if(outputDimension==3){
			if(c3!=null){
				try{
					zCoord = Double.parseDouble(c3);
				}catch(Exception ex){
					throw new Iox2wkbException("failed to read C3 <"+c3+">",ex);
				}
			}else{
				throw new Iox2wkbException("missing C3");
			}
		}
		writeCoord(xCoord,yCoord,zCoord);
	}

	private static Coordinate arcSupportingCoord2JTS(IomObject value) throws Iox2wkbException {
		String a1=value.getattrvalue("A1");
		String a2=value.getattrvalue("A2");
		double arcPt_re;
			try{
				arcPt_re = Double.parseDouble(a1);
			}catch(Exception ex){
				throw new Iox2wkbException("failed to read A1 <"+a1+">",ex);
			}
			double arcPt_ho;
			try{
				arcPt_ho = Double.parseDouble(a2);
			}catch(Exception ex){
				throw new Iox2wkbException("failed to read A2 <"+a2+">",ex);
			}
			return new Coordinate(arcPt_re, arcPt_ho);
	}

	private static void arc2JTS(com.vividsolutions.jts.geom.CoordinateList ret,IomObject value,double p)
	throws Iox2wkbException
	{
		if(value!=null){
			String c1=value.getattrvalue("C1");
			String c2=value.getattrvalue("C2");
			String c3=value.getattrvalue("C3");

			double pt2_re;
			try{
				pt2_re = Double.parseDouble(c1);
			}catch(Exception ex){
				throw new Iox2wkbException("failed to read C1 <"+c1+">",ex);
			}
			double pt2_ho;
			try{
				pt2_ho = Double.parseDouble(c2);
			}catch(Exception ex){
				throw new Iox2wkbException("failed to read C2 <"+c2+">",ex);
			}
			Coordinate arcSupport = arcSupportingCoord2JTS(value);
			if(p==0.0){
				ret.add(arcSupport);
				ret.add(new com.vividsolutions.jts.geom.Coordinate(pt2_re, pt2_ho));
				return;
			}
			int lastCoord=ret.size();
			com.vividsolutions.jts.geom.Coordinate p1=null;
			p1=ret.getCoordinate(lastCoord-1);
			ArcSegment arc=new ArcSegment(p1, arcSupport,new Coordinate(pt2_re, pt2_ho),p);
			Coordinate[] coords = arc.getCoordinates();
			for(int i=1;i<coords.length;i++) {
	            ret.add(coords[i]);
			}
		}
	}
	private int arc2JTS(IomObject startPt,IomObject value,double p)
	throws Iox2wkbException
	{
		if(value==null){
			return 0;
		}
			int pointc=0;
			String c1=value.getattrvalue("C1");
			String c2=value.getattrvalue("C2");
			String c3=value.getattrvalue("C3");
			String a1=value.getattrvalue("A1");
			String a2=value.getattrvalue("A2");
			double pt2_re;
			try{
				pt2_re = Double.parseDouble(c1);
			}catch(Exception ex){
				throw new Iox2wkbException("failed to read C1 <"+c1+">",ex);
			}
			double pt2_ho;
			try{
				pt2_ho = Double.parseDouble(c2);
			}catch(Exception ex){
				throw new Iox2wkbException("failed to read C2 <"+c2+">",ex);
			}
			double pt2_z=0.0;
			if(c3!=null){
				try{
					pt2_z = Double.parseDouble(c3);
				}catch(Exception ex){
					throw new Iox2wkbException("failed to read C3 <"+c3+">",ex);
				}
			}
			double arcPt_re;
			try{
				arcPt_re = Double.parseDouble(a1);
			}catch(Exception ex){
				throw new Iox2wkbException("failed to read A1 <"+a1+">",ex);
			}
			double arcPt_ho;
			try{
				arcPt_ho = Double.parseDouble(a2);
			}catch(Exception ex){
				throw new Iox2wkbException("failed to read A2 <"+a2+">",ex);
			}
			if(p==0.0){
				throw new Iox2wkbException("illegal p");
			}

			String p1c1=startPt.getattrvalue("C1");
			String p1c2=startPt.getattrvalue("C2");
			String p1c3=startPt.getattrvalue("C3");
			double pt1_re;
			try{
				pt1_re = Double.parseDouble(p1c1);
			}catch(Exception ex){
				throw new Iox2wkbException("failed to read C1 <"+p1c1+">",ex);
			}
			double pt1_ho;
			try{
				pt1_ho = Double.parseDouble(p1c2);
			}catch(Exception ex){
				throw new Iox2wkbException("failed to read C2 <"+p1c2+">",ex);
			}
			double pt1_z=0.0;
			if(p1c3!=null){
				try{
					pt1_z = Double.parseDouble(p1c3);
				}catch(Exception ex){
					throw new Iox2wkbException("failed to read C3 <"+p1c3+">",ex);
				}
			}

            ArcSegment arc=new ArcSegment(new Coordinate(pt1_re,pt1_ho), new Coordinate(arcPt_re, arcPt_ho),new Coordinate(pt2_re, pt2_ho),p);
            Coordinate[] coords = arc.getCoordinates();
            for(int i=1;i<coords.length;i++) {
                writeCoord(coords[i].x,coords[i].y,0.0);
                pointc++;
            }
			
			return pointc;
	}
	/** Converts a COORD to a JTS Coordinate.
	 * @param value INTERLIS COORD structure.
	 * @return JTS Coordinate.
	 * @throws Iox2jtsException
	 */
	private static com.vividsolutions.jts.geom.Coordinate coord2JTS(IomObject value) 
	throws Iox2wkbException 
	{
		if(value==null){
			return null;
		}
		String c1=value.getattrvalue("C1");
		String c2=value.getattrvalue("C2");
		String c3=value.getattrvalue("C3");
		double xCoord;
		try{
			xCoord = Double.parseDouble(c1);
		}catch(Exception ex){
			throw new Iox2wkbException("failed to read C1 <"+c1+">",ex);
		}
		double yCoord;
		try{
			yCoord = Double.parseDouble(c2);
		}catch(Exception ex){
			throw new Iox2wkbException("failed to read C2 <"+c2+">",ex);
		}
		com.vividsolutions.jts.geom.Coordinate coord=null;
		if(c3==null){
			coord=new com.vividsolutions.jts.geom.Coordinate(xCoord, yCoord);
		}else{
			double zCoord;
			try{
				zCoord = Double.parseDouble(c3);
			}catch(Exception ex){
				throw new Iox2wkbException("failed to read C3 <"+c3+">",ex);
			}
			coord=new com.vividsolutions.jts.geom.Coordinate(xCoord, yCoord,zCoord);
		}
		return coord;
	}
	/** Converts a POLYLINE to a JTS CoordinateList.
	 * @param polylineObj INTERLIS POLYLINE structure
	 * @param isSurfaceOrArea true if called as part of a SURFACE conversion.
	 * @param p maximum stroke to use when removing ARCs
	 * @return JTS CoordinateList
	 * @throws Iox2wkbException
	 */
    public byte[] polyline2wkb(IomObject polylineObj,boolean isSurfaceOrArea,boolean asCompoundCurve,double p)
    throws Iox2wkbException
    {
        if(polylineObj==null){
            return null;
        }
        IomObject polylineObjs[]=new IomObject[] {polylineObj};
        return polyline2wkb(polylineObjs, isSurfaceOrArea, asCompoundCurve, p);
    }
	public byte[] polyline2wkb(IomObject polylineObjs[],boolean isSurfaceOrArea,boolean asCompoundCurve,double p)
	throws Iox2wkbException
	{
		os.reset();
		byte[][] lines = polyline2wkb(polylineObjs, isSurfaceOrArea, asCompoundCurve, p, false);
		for (byte[] line : lines) {
			try {
				os.write(line);
			} catch (IOException e) {
				throw new RuntimeException("Unexpected IO exception: " + e.getMessage());
			}
		}

		return os.toByteArray();
	}


	private byte[][] polyline2wkb(IomObject polylineObjs[], boolean isSurfaceOrArea, boolean asCompoundCurve, double p, boolean repairTouchingLine)
	throws Iox2wkbException {
		if (polylineObjs == null) {
			return new byte[0][0];
		}

		List<PolylineCoordList> lines = new ArrayList<PolylineCoordList>();

		for (IomObject polylineObj : polylineObjs) {
			if (polylineObj == null) {
				continue;
			}
			// is POLYLINE?
			if (isSurfaceOrArea && polylineObj.getattrobj("lineattr", 0) != null) {
				throw new Iox2wkbException("Lineattributes not supported");
			}
			if (polylineObj.getobjectconsistency() == IomConstants.IOM_INCOMPLETE) {
				throw new Iox2wkbException("clipped polyline not supported");
			}

			for(int sequencei=0; sequencei<polylineObj.getattrvaluecount("sequence"); sequencei++) {
				IomObject sequence=polylineObj.getattrobj("sequence",sequencei);
				int segmentc=sequence.getattrvaluecount("segment");

				PolylineCoordList currentLine = new PolylineCoordList();
				lines.add(currentLine);

				for(int segmenti=0; segmenti<segmentc; segmenti++){
					IomObject segment = sequence.getattrobj("segment", segmenti);
					Coordinate segmentCoordinate = coord2JTS(segment);

					if((segment.getobjecttag().equals("COORD") && !currentLine.trySetWkbType(WKBConstants.wkbLineString))
							|| (segment.getobjecttag().equals("ARC") && !currentLine.trySetWkbType(WKBConstants.wkbCircularString)))
					{
						Coordinate lastPoint = currentLine.get(currentLine.size()-1);
						currentLine = new PolylineCoordList();
						currentLine.add(lastPoint);
						lines.add(currentLine);
					}

					if (repairTouchingLine && currentLine.contains(segmentCoordinate) && !currentLine.get(0).equals(segmentCoordinate)){
						PolylineCoordList splitLine = currentLine.splitTailAt(segmentCoordinate);
						if (splitLine.getWkbType() == WKBConstants.wkbCircularString){
							splitLine.add(arcSupportingCoord2JTS(segment));
						}
						splitLine.add(segmentCoordinate);
						lines.add(splitLine);
					}

					if(segment.getobjecttag().equals("COORD") && currentLine.trySetWkbType(WKBConstants.wkbLineString)) {
						currentLine.add(segmentCoordinate);
					}
					else if(segment.getobjecttag().equals("ARC") && !asCompoundCurve && currentLine.trySetWkbType(WKBConstants.wkbLineString)){
						if(p==0.0){
							currentLine.add(arcSupportingCoord2JTS(segment));
							currentLine.add(segmentCoordinate);
						} else {
							Coordinate lastCoordinate = currentLine.get(currentLine.size() - 1);
							ArcSegment arc = new ArcSegment(lastCoordinate, arcSupportingCoord2JTS(segment), segmentCoordinate, p);
							for (Coordinate c: arc.getCoordinates()) {
								currentLine.add(c);
							}
						}
					}
					else if (segment.getobjecttag().equals("ARC") && currentLine.trySetWkbType(WKBConstants.wkbCircularString))
					{
						currentLine.add(arcSupportingCoord2JTS(segment));
						currentLine.add(segmentCoordinate);
					}
					else {
						throw new Iox2wkbException("custom line form not supported");
					}
				}
			}
		}

		byte[][] result = new byte[lines.size()][];
        try {
			for (int linec = 0; linec < lines.size(); linec++) {
				PolylineCoordList line = lines.get(linec);
				os.reset();
				if (asCompoundCurve || !isSurfaceOrArea)
				{
					writeByteOrder();
					writeGeometryType(line.getWkbType());
				}
				os.writeInt(line.size());
				for (int i = 0; i < line.size(); i++) {
					writeCoord(line.get(i));
				}
				result[linec] = os.toByteArray();
			}
		} catch (IOException e) {
	        throw new RuntimeException("Unexpected IO exception: " + e.getMessage());
		}
		return result;
	}

	private static com.vividsolutions.jts.geom.CoordinateList polyline2coordlist(IomObject polylineObj,boolean isSurfaceOrArea,double p)
	throws Iox2wkbException
	{
		if(polylineObj==null){
			return null;
		}
		com.vividsolutions.jts.geom.CoordinateList ret=new com.vividsolutions.jts.geom.CoordinateList();
		// is POLYLINE?
		if(isSurfaceOrArea){
			IomObject lineattr=polylineObj.getattrobj("lineattr",0);
			if(lineattr!=null){
				//writeAttrs(out,lineattr);
				throw new Iox2wkbException("Lineattributes not supported");							
			}
		}
		boolean clipped=polylineObj.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
		if(clipped){
			throw new Iox2wkbException("clipped polyline not supported");
		}
		for(int sequencei=0;sequencei<polylineObj.getattrvaluecount("sequence");sequencei++){
			if(clipped){
				//out.startElement(tags::get_CLIPPED(),0,0);
			}else{
				// an unclipped polyline should have only one sequence element
				if(sequencei>0){
					throw new Iox2wkbException("unclipped polyline with multi 'sequence' elements");
				}
			}
			IomObject sequence=polylineObj.getattrobj("sequence",sequencei);
			for(int segmenti=0;segmenti<sequence.getattrvaluecount("segment");segmenti++){
				IomObject segment=sequence.getattrobj("segment",segmenti);
				//EhiLogger.debug("segmenttag "+segment.getobjecttag());
				if(segment.getobjecttag().equals("COORD")){
					// COORD
					ret.add(coord2JTS(segment));
				}else if(segment.getobjecttag().equals("ARC")){
					// ARC
					arc2JTS(ret,segment,p);
				}else{
					// custum line form
					throw new Iox2wkbException("custom line form not supported");
					//out.startElement(segment->getTag(),0,0);
					//writeAttrs(out,segment);
					//out.endElement(/*segment*/);
				}

			}
			if(clipped){
				//out.endElement(/*CLIPPED*/);
			}
		}
		return ret;
	}
	/** Converts a SURFACE to a JTS Polygon.
	 * @param obj INTERLIS SURFACE structure
	 * @param strokeP maximum stroke to use when removing ARCs
	 * @return JTS Polygon
	 * @throws Iox2wkbException
	 */
	public byte[] surface2wkb(IomObject obj,boolean asCurvePolygon,double strokeP) //SurfaceOrAreaType type)
	throws Iox2wkbException
	{
		if(obj==null){
			return null;
		}
	    try {
			writeByteOrder();
			if(asCurvePolygon){
				writeGeometryType(WKBConstants.wkbCurvePolygon);
			}else{
				writeGeometryType(WKBConstants.wkbPolygon);
			}

			//IFMEFeatureVector bndries=session.createFeatureVector();
			boolean clipped=obj.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
			if(clipped){
				throw new Iox2wkbException("clipped surface not supported");
			}
			for(int surfacei=0;surfacei<obj.getattrvaluecount("surface");surfacei++){
				if(clipped){
					//out.startElement("CLIPPED",0,0);
				}else{
					// an unclipped surface should have only one surface element
					if(surfacei>0){
						throw new Iox2wkbException("unclipped surface with multi 'surface' elements");
					}
				}
				IomObject surface=obj.getattrobj("surface",surfacei);

				int boundaryc=surface.getattrvaluecount("boundary");
				
			    os.writeInt(boundaryc);
			    
				for(int boundaryi=0;boundaryi<boundaryc;boundaryi++){
					IomObject boundary=surface.getattrobj("boundary",boundaryi);
					int polylinec = boundary.getattrvaluecount("polyline");
                    if(asCurvePolygon){
                        Iox2wkb helper=new Iox2wkb(outputDimension,os.order(),asEWKB);
						if(polylinec==1) {
                            IomObject polyline=boundary.getattrobj("polyline",0);
                            os.write(helper.polyline2wkb(polyline,true,asCurvePolygon,strokeP));
						}else {
						    IomObject polylines[]=new IomObject[polylinec];
	                        for(int polylinei=0;polylinei<polylinec;polylinei++){
	                            IomObject polyline=boundary.getattrobj("polyline",polylinei);
	                            polylines[polylinei]=polyline;
	                        }
                            os.write(helper.polyline2wkb(polylines,true,asCurvePolygon,strokeP));
						}
					}else{
						//IFMEFeature fmeLine=session.createFeature();
						com.vividsolutions.jts.geom.CoordinateList jtsLine=new com.vividsolutions.jts.geom.CoordinateList();
						for(int polylinei=0;polylinei<polylinec;polylinei++){
							IomObject polyline=boundary.getattrobj("polyline",polylinei);
							jtsLine.addAll(polyline2coordlist(polyline,true,strokeP));
						}
						jtsLine.closeRing();
						os.writeInt(jtsLine.size());
						for(Iterator coordi=jtsLine.iterator();coordi.hasNext();){
							com.vividsolutions.jts.geom.Coordinate coord=(com.vividsolutions.jts.geom.Coordinate)coordi.next();
						    os.writeDouble(coord.x);
						    os.writeDouble(coord.y);
						    if (outputDimension==3) {
						      os.writeDouble(coord.z);
						    }
						}
					}

				}
				if(clipped){
					//out.endElement(/*CLIPPED*/);
				}
			}
		} catch (IOException e) {
	        throw new RuntimeException("Unexpected IO exception: " + e.getMessage());
		}
		return os.toByteArray();
	}
	public byte[] multisurface2wkb(IomObject obj,boolean asCurvePolygon,double strokeP) //SurfaceOrAreaType type)
	throws Iox2wkbException
	{
		if(obj==null){
			return null;
		}
	    try {
			writeByteOrder();
			if(asCurvePolygon){
				writeGeometryType(WKBConstants.wkbMultiSurface);
			}else{
				writeGeometryType(WKBConstants.wkbMultiPolygon);
			}
			int surfacec=obj.getattrvaluecount("surface");
			os.writeInt(surfacec);

			for(int surfacei=0;surfacei<surfacec;surfacei++){
				IomObject surface=obj.getattrobj("surface",surfacei);
				IomObject iomSurfaceClone=new ch.interlis.iom_j.Iom_jObject("MULTISURFACE",null);
				iomSurfaceClone.addattrobj("surface",surface);
                Iox2wkb helper=new Iox2wkb(outputDimension,os.order(),asEWKB);
				os.write(helper.surface2wkb(iomSurfaceClone,asCurvePolygon,strokeP));
			}
		} catch (IOException e) {
	        throw new RuntimeException("Unexpected IO exception: " + e.getMessage());
		}
		return os.toByteArray();
	}
	public byte[] multiline2wkb(IomObject obj,boolean asCurve,double strokeP)
	throws Iox2wkbException
	{
		if(obj==null){
			return null;
		}
	    try {
			writeByteOrder();
			if(asCurve){
				writeGeometryType(WKBConstants.wkbMultiCurve);
			}else{
				writeGeometryType(WKBConstants.wkbMultiLineString);
			}
			int polylinec=obj.getattrvaluecount(Wkb2iox.ATTR_POLYLINE);
			os.writeInt(polylinec);

			for(int polylinei=0;polylinei<polylinec;polylinei++){
				IomObject polyline=obj.getattrobj("polyline",polylinei);
                Iox2wkb helper=new Iox2wkb(outputDimension,os.order(),asEWKB);
				os.write(helper.polyline2wkb(polyline,false,asCurve,strokeP));
			}
		} catch (IOException e) {
	        throw new RuntimeException("Unexpected IO exception: " + e.getMessage());
		}
		return os.toByteArray();
	}

	  private void writeByteOrder() throws IOException
	  {
	    if (os.order().equals(java.nio.ByteOrder.LITTLE_ENDIAN)){
	      os.write(WKBConstants.wkbNDR);
	    }else{
	      os.write(WKBConstants.wkbXDR);
	    }
	  }

	  private void writeGeometryType(int geometryType)
	      throws IOException
	  {
        int flagIncludeZ = asEWKB? WKBConstants.ewkbIncludesZ : WKBConstants.wkbIncludesZ;
        int flag3D = (outputDimension == 3) ? flagIncludeZ : 0;
        int typeInt = geometryType + flag3D;
	    os.writeInt(typeInt);
	  }

	  private void writeCoord(Coordinate coordinate){
		writeCoord(coordinate.x, coordinate.y, coordinate.z);
	  }

	  private void writeCoord(double xCoord,double yCoord,double zCoord)
	  {
		    os.writeDouble(xCoord);
		    os.writeDouble(yCoord);
		    if(outputDimension==3){
			    os.writeDouble(zCoord);
		    }
	  }
}
