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
package ch.interlis.iox_j.jts;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.PrecisionModel;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iox_j.wkb.Wkb2iox;

/** Utility to convert from INTERLIS to JTS geometry types.
 * @author ceis
 */
public class Iox2jts {
	// utility, no instances
	Iox2jts(){}
	private static double dist(double re1,double ho1,double re2,double ho2)
	{
		double ret;
		ret=Math.hypot(re2-re1,ho2-ho1);
		return ret;
	}
	/** Converts a COORD to a OGC WKB hex string.
	 * @param value INTERLIS COORD structure.
	 * @return WKB as hex encoded string.
	 * @throws Iox2jtsException
	 */
	public static String coord2hexwkb(IomObject value)
	throws Iox2jtsException
	{
		if(value!=null){
			com.vividsolutions.jts.geom.Geometry geom = new com.vividsolutions.jts.geom.GeometryFactory().createPoint(coord2JTS(value));
			byte bv[]=new com.vividsolutions.jts.io.WKBWriter().write(geom);
			return com.vividsolutions.jts.io.WKBWriter.bytesToHex(bv);
		}
		return null;
	}
	/** Converts a POLYLINE to a OGC WKB hex string.
	 * @param value INTERLIS POLYLINE structure.
	 * @return WKB as hex encoded string.
	 * @throws Iox2jtsException
	 */
	public static String polyline2hexwkb(IomObject value,double p)
	throws Iox2jtsException
	{
		if(value!=null){
			com.vividsolutions.jts.geom.Geometry geom = new com.vividsolutions.jts.geom.GeometryFactory().createLineString(polyline2JTS(value,false,p).toCoordinateArray());
			byte bv[]=new com.vividsolutions.jts.io.WKBWriter().write(geom);
			return com.vividsolutions.jts.io.WKBWriter.bytesToHex(bv);
		}
		return null;
	}
	/** Converts a SURFACE to a OGC WKB hex string.
	 * @param value INTERLIS SURFACE structure.
	 * @return WKB as hex encoded string.
	 * @throws Iox2jtsException
	 */
	public static String surface2hexwkb(IomObject value,double strokeP)
	throws Iox2jtsException
	{
		if(value!=null){
			com.vividsolutions.jts.geom.Geometry geom = surface2JTS(value,strokeP);
			byte bv[]=new com.vividsolutions.jts.io.WKBWriter().write(geom);
			return com.vividsolutions.jts.io.WKBWriter.bytesToHex(bv);
		}
		return null;
	}
	/** Converts a COORD to a JTS Coordinate.
	 * @param value INTERLIS COORD structure.
	 * @return JTS Coordinate.
	 * @throws Iox2jtsException
	 */
	public static com.vividsolutions.jts.geom.Coordinate coord2JTS(IomObject value) 
	throws Iox2jtsException 
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
			throw new Iox2jtsException("failed to read C1 <"+c1+">",ex);
		}
		double yCoord;
		try{
			yCoord = Double.parseDouble(c2);
		}catch(Exception ex){
			throw new Iox2jtsException("failed to read C2 <"+c2+">",ex);
		}
		com.vividsolutions.jts.geom.Coordinate coord=null;
		if(c3==null){
			coord=new com.vividsolutions.jts.geom.Coordinate(xCoord, yCoord);
		}else{
			double zCoord;
			try{
				zCoord = Double.parseDouble(c3);
			}catch(Exception ex){
				throw new Iox2jtsException("failed to read C3 <"+c3+">",ex);
			}
			coord=new com.vividsolutions.jts.geom.Coordinate(xCoord, yCoord,zCoord);
		}
		return coord;
	}
	/** Converts a MULTICOORD to a JTS MultiPoint.
	 * @param obj INTERLIS MULTICOORD structure.
	 * @return JTS MultiPoint.
	 * @throws Iox2jtsException
	 */
	public static com.vividsolutions.jts.geom.MultiPoint multicoord2JTS(IomObject obj)
	throws Iox2jtsException 
	{
		int segmentCount=obj.getattrvaluecount(Wkb2iox.ATTR_COORD);
		com.vividsolutions.jts.geom.Point[] jtsPointArray=new com.vividsolutions.jts.geom.Point[segmentCount];
		for(int i=0;i<segmentCount;i++) {
			IomObject segment=obj.getattrobj(Wkb2iox.ATTR_COORD, i);
			Coordinate jtsCoordinate=coord2JTS(segment);
			com.vividsolutions.jts.geom.Point jtsPoint=new com.vividsolutions.jts.geom.Point(jtsCoordinate, new PrecisionModel(), 0);
			jtsPointArray[i]=jtsPoint;
		}
		com.vividsolutions.jts.geom.MultiPoint ret=new com.vividsolutions.jts.geom.MultiPoint(jtsPointArray, new PrecisionModel(), 0);
		return ret;
	}
	private static void arc2JTS(com.vividsolutions.jts.geom.CoordinateList ret,IomObject value,double p)
	throws Iox2jtsException
	{
		if(value!=null){
			String c1=value.getattrvalue("C1");
			String c2=value.getattrvalue("C2");
			String c3=value.getattrvalue("C3");
			String a1=value.getattrvalue("A1");
			String a2=value.getattrvalue("A2");
			double pt2_re;
			try{
				pt2_re = Double.parseDouble(c1);
			}catch(Exception ex){
				throw new Iox2jtsException("failed to read C1 <"+c1+">",ex);
			}
			double pt2_ho;
			try{
				pt2_ho = Double.parseDouble(c2);
			}catch(Exception ex){
				throw new Iox2jtsException("failed to read C2 <"+c2+">",ex);
			}
			double arcPt_re;
			try{
				arcPt_re = Double.parseDouble(a1);
			}catch(Exception ex){
				throw new Iox2jtsException("failed to read A1 <"+a1+">",ex);
			}
			double arcPt_ho;
			try{
				arcPt_ho = Double.parseDouble(a2);
			}catch(Exception ex){
				throw new Iox2jtsException("failed to read A2 <"+a2+">",ex);
			}
			if(p==0.0){
				ret.add(new com.vividsolutions.jts.geom.Coordinate(arcPt_re, arcPt_ho));
				ret.add(new com.vividsolutions.jts.geom.Coordinate(pt2_re, pt2_ho));
				return;
			}
			int lastCoord=ret.size();
			com.vividsolutions.jts.geom.Coordinate p1=null;
			p1=ret.getCoordinate(lastCoord-1);
			double pt1_re=p1.x;
			double pt1_ho=p1.y;
			//EhiLogger.debug("pt1 "+pt1_re+", "+pt1_ho);
			//EhiLogger.debug("arc "+arcPt_re+", "+arcPt_ho);
			//EhiLogger.debug("pt2 "+pt2_re+", "+pt2_ho);
			/*
			if(c3==null){
				ret.setDimension(IFMEFeature.FME_TWO_D);
				ret.add2DCoordinate(p2_x, p2_y);
			}else{
				double zCoord = Double.parseDouble(c3);
				ret.setDimension(IFMEFeature.FME_THREE_D);
				ret.add3DCoordinate(p2_x, p2_y, zCoord);
			}
			*/
			// letzter Punkt ein Bogenzwischenpunkt?
		
			// Zwischenpunkte erzeugen

			// Distanz zwischen Bogenanfanspunkt und Zwischenpunkt
			double a=dist(pt1_re,pt1_ho,arcPt_re,arcPt_ho);
			// Distanz zwischen Zwischenpunkt und Bogenendpunkt 
			double b=dist(arcPt_re,arcPt_ho,pt2_re,pt2_ho);

			// Zwischenpunkte erzeugen, so dass maximale Pfeilhoehe nicht 
			// ueberschritten wird
			// Distanz zwischen Bogenanfanspunkt und Bogenendpunkt 
			double c=dist(pt1_re,pt1_ho,pt2_re,pt2_ho);
			// Radius bestimmen
			double s=(a+b+c)/2.0;
			double ds=Math.atan2(pt2_re-arcPt_re,pt2_ho-arcPt_ho)-Math.atan2(pt1_re-arcPt_re,pt1_ho-arcPt_ho);
			double rSign=(Math.sin(ds)>0.0)?-1.0:1.0;
			double r=a*b*c/4.0/Math.sqrt(s*(s-a)*(s-b)*(s-c))*rSign;
			// Kreismittelpunkt
			double thetaM=Math.atan2(arcPt_re-pt1_re,arcPt_ho-pt1_ho)+Math.acos(a/2.0/r);
			double reM=pt1_re+r*Math.sin(thetaM);
			double hoM=pt1_ho+r*Math.cos(thetaM);

			// mindest Winkelschrittweite
			double theta=2*Math.acos(1-p/Math.abs(r));

			if(a>2*p){
				// Zentriwinkel zwischen pt1 und arcPt
				double alpha=2.0*Math.asin(a/2.0/Math.abs(r));
				// anzahl Schritte
				int alphan=(int)Math.ceil(alpha/theta);
				// Winkelschrittweite
				double alphai=alpha/(alphan*(r>0.0?1:-1));
				double ri=Math.atan2(pt1_re-reM,pt1_ho-hoM);
				for(int i=1;i<alphan;i++){
					ri += alphai;
					double pti_re=reM + Math.abs(r) * Math.sin(ri);
					double pti_ho=hoM + Math.abs(r) * Math.cos(ri);
					ret.add(new com.vividsolutions.jts.geom.Coordinate(pti_re, pti_ho));
				}
			}

			ret.add(new com.vividsolutions.jts.geom.Coordinate(arcPt_re, arcPt_ho));

			if(b>2*p){
				// Zentriwinkel zwischen arcPt und pt2
				double beta=2.0*Math.asin(b/2.0/Math.abs(r));
				// anzahl Schritte
				int betan=(int)Math.ceil((beta/theta));
				// Winkelschrittweite
				double betai=beta/(betan*(r>0.0?1:-1));
				double ri=Math.atan2(arcPt_re-reM,arcPt_ho-hoM);
				for(int i=1;i<betan;i++){
					ri += betai;
					double pti_re=reM + Math.abs(r) * Math.sin(ri);
					double pti_ho=hoM + Math.abs(r) * Math.cos(ri);
					ret.add(new com.vividsolutions.jts.geom.Coordinate(pti_re, pti_ho));
				}
			}
			ret.add(new com.vividsolutions.jts.geom.Coordinate(pt2_re, pt2_ho));
		}
	}
	/** Converts a POLYLINE to a JTS CoordinateList.
	 * @param polylineObj INTERLIS POLYLINE structure
	 * @param isSurfaceOrArea true if called as part of a SURFACE conversion.
	 * @param p maximum stroke to use when removing ARCs
	 * @return JTS CoordinateList
	 * @throws Iox2jtsException
	 */
	public static com.vividsolutions.jts.geom.CoordinateList polyline2JTS(IomObject polylineObj,boolean isSurfaceOrArea,double p)
	throws Iox2jtsException
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
				throw new Iox2jtsException("Lineattributes not supported");							
			}
		}
		boolean clipped=polylineObj.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
		if(clipped){
			throw new Iox2jtsException("clipped polyline not supported");
		}
		for(int sequencei=0;sequencei<polylineObj.getattrvaluecount("sequence");sequencei++){
			if(clipped){
				//out.startElement(tags::get_CLIPPED(),0,0);
			}else{
				// an unclipped polyline should have only one sequence element
				if(sequencei>0){
					throw new Iox2jtsException("unclipped polyline with multi 'sequence' elements");
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
					throw new Iox2jtsException("custom line form not supported");
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
	/** Converts a MULTIPOLYLINE to a JTS MultiLineString.
	 * @param obj INTERLIS MULTIPOLYLINE structure
	 * @param strokeP maximum stroke to use when removing ARCs
	 * @return JTS MultiLineString
	 * @throws Iox2jtsException
	 */
	public static com.vividsolutions.jts.geom.MultiLineString multipolyline2JTS(IomObject obj,double strokeP)
	throws Iox2jtsException
	{
		int polylineCount=obj.getattrvaluecount("polyline");
		com.vividsolutions.jts.geom.LineString[] jtsLineStringArray=new com.vividsolutions.jts.geom.LineString[polylineCount];
		for(int polylinei=0;polylinei<polylineCount;polylinei++) {
			IomObject polyline=obj.getattrobj("polyline", polylinei);
			CoordinateList jtsCoordinateList=polyline2JTS(polyline, false, strokeP);
			com.vividsolutions.jts.geom.LineString jtsLineString=new com.vividsolutions.jts.geom.LineString(jtsCoordinateList.toCoordinateArray(), new PrecisionModel(), 0);
			jtsLineStringArray[polylinei]=jtsLineString;
		}
		com.vividsolutions.jts.geom.MultiLineString ret=new com.vividsolutions.jts.geom.MultiLineString(jtsLineStringArray, new PrecisionModel(), 0);
		return ret;
	}
	/** Converts a SURFACE to a JTS Polygon.
	 * @param obj INTERLIS SURFACE structure
	 * @param strokeP maximum stroke to use when removing ARCs
	 * @return JTS Polygon
	 * @throws Iox2jtsException
	 */
	public static com.vividsolutions.jts.geom.Polygon surface2JTS(IomObject obj,double strokeP) //SurfaceOrAreaType type)
	throws Iox2jtsException
	{
		if(obj==null){
			return null;
		}
		com.vividsolutions.jts.geom.Polygon ret=null;
		//IFMEFeatureVector bndries=session.createFeatureVector();
		boolean clipped=obj.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
		if(clipped){
			throw new Iox2jtsException("clipped surface not supported");
		}
		for(int surfacei=0;surfacei<obj.getattrvaluecount("surface");surfacei++){
			if(clipped){
				//out.startElement("CLIPPED",0,0);
			}else{
				// an unclipped surface should have only one surface element
				if(surfacei>0){
					throw new Iox2jtsException("unclipped surface with multi 'surface' elements");
				}
			}
			IomObject surface=obj.getattrobj("surface",surfacei);
			com.vividsolutions.jts.geom.LinearRing shell=null;
			com.vividsolutions.jts.geom.LinearRing holes[]=null;
			int boundaryc=surface.getattrvaluecount("boundary");
			if(boundaryc>1){
				holes=new com.vividsolutions.jts.geom.LinearRing[boundaryc-1];				
			}
			for(int boundaryi=0;boundaryi<boundaryc;boundaryi++){
				IomObject boundary=surface.getattrobj("boundary",boundaryi);
				//IFMEFeature fmeLine=session.createFeature();
				com.vividsolutions.jts.geom.CoordinateList jtsLine=new com.vividsolutions.jts.geom.CoordinateList();
				for(int polylinei=0;polylinei<boundary.getattrvaluecount("polyline");polylinei++){
					IomObject polyline=boundary.getattrobj("polyline",polylinei);
					jtsLine.addAll(polyline2JTS(polyline,true,strokeP));
				}
				jtsLine.closeRing();
				if(boundaryi==0){
					shell=new com.vividsolutions.jts.geom.GeometryFactory().createLinearRing(jtsLine.toCoordinateArray());
				}else{
					holes[boundaryi-1]=new com.vividsolutions.jts.geom.GeometryFactory().createLinearRing(jtsLine.toCoordinateArray());
				}
				//bndries.append(fmeLine);
			}
			ret=new com.vividsolutions.jts.geom.GeometryFactory().createPolygon(shell,holes);
			if(clipped){
				//out.endElement(/*CLIPPED*/);
			}
		}
		return ret;
	}
	/** Converts a MULTISURFACE to a JTS MultiPolygon.
	 * @param obj INTERLIS MULTISURFACE structure
	 * @param strokeP maximum stroke to use when removing ARCs
	 * @return JTS MultiPolygon
	 * @throws Iox2jtsException
	 */
	public static com.vividsolutions.jts.geom.MultiPolygon multisurface2JTS(IomObject obj,double strokeP,int srid) //SurfaceOrAreaType type)
	throws Iox2jtsException
	{
		com.vividsolutions.jts.geom.Polygon[] jtsPolygons=null;
		com.vividsolutions.jts.geom.Polygon jtsPolygon=null;
		int surfaceCount=obj.getattrvaluecount("surface");
		jtsPolygons=new com.vividsolutions.jts.geom.Polygon[surfaceCount];
		for(int surfacei=0;surfacei<surfaceCount;surfacei++){
			IomObject surface=obj.getattrobj("surface",surfacei);
			com.vividsolutions.jts.geom.LinearRing jtsShell=null;
			com.vividsolutions.jts.geom.LinearRing jtsHoles[]=null;
			int boundarycount=surface.getattrvaluecount("boundary");
			if(boundarycount>1){
				jtsHoles=new com.vividsolutions.jts.geom.LinearRing[boundarycount-1];				
			}
			for(int boundaryi=0;boundaryi<boundarycount;boundaryi++){
				IomObject boundary=surface.getattrobj("boundary",boundaryi);
				com.vividsolutions.jts.geom.CoordinateList jtsLine=new com.vividsolutions.jts.geom.CoordinateList();
				for(int polylinei=0;polylinei<boundary.getattrvaluecount("polyline");polylinei++){
					IomObject polyline=boundary.getattrobj("polyline",polylinei);
					jtsLine.addAll(polyline2JTS(polyline,true,strokeP));
				}
				jtsLine.closeRing();
				if(boundaryi==0){
					jtsShell=new com.vividsolutions.jts.geom.GeometryFactory().createLinearRing(jtsLine.toCoordinateArray());
				}else{
					jtsHoles[boundaryi-1]=new com.vividsolutions.jts.geom.GeometryFactory().createLinearRing(jtsLine.toCoordinateArray());
				}
			}
			jtsPolygon=new com.vividsolutions.jts.geom.GeometryFactory().createPolygon(jtsShell,jtsHoles);
			jtsPolygons[surfacei]=jtsPolygon;
		}
		com.vividsolutions.jts.geom.MultiPolygon ret=new com.vividsolutions.jts.geom.MultiPolygon(jtsPolygons, new PrecisionModel(), srid);
		return ret;
	}
}