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

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;

import ch.ehi.basics.types.OutParam;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurveRing;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.logging.Log2EhiLogger;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.ValidationConfig;

/** Utility to convert from INTERLIS to JTS geometry types.
 * @author ceis
 */
public class Iox2jtsext {
	// utility, no instances
	private Iox2jtsext(){}
	/** Converts a COORD to a OGC WKB hex string.
	 * @param value INTERLIS COORD structure.
	 * @return WKB as hex encoded string.
	 * @throws Iox2jtsException
	 */
	public static String coord2hexwkb(IomObject value)
	throws IoxException
	{
		if(value!=null){
			com.vividsolutions.jts.geom.Geometry geom = new JtsextGeometryFactory().createPoint(coord2JTS(value));
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
	throws IoxException
	{
		if(value!=null){
			com.vividsolutions.jts.geom.Geometry geom = polyline2JTS(value,false,p);
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
	throws IoxException
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
	throws IoxException 
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
			throw new IoxException("failed to read C1 <"+c1+">",ex);
		}
		double yCoord;
		try{
			yCoord = Double.parseDouble(c2);
		}catch(Exception ex){
			throw new IoxException("failed to read C2 <"+c2+">",ex);
		}
		com.vividsolutions.jts.geom.Coordinate coord=null;
		if(c3==null){
			coord=new com.vividsolutions.jts.geom.Coordinate(xCoord, yCoord);
		}else{
			double zCoord;
			try{
				zCoord = Double.parseDouble(c3);
			}catch(Exception ex){
				throw new IoxException("failed to read C3 <"+c3+">",ex);
			}
			coord=new com.vividsolutions.jts.geom.Coordinate(xCoord, yCoord,zCoord);
		}
		return coord;
	}
	public static Coordinate getArcEndPt(IomObject segment) throws IoxException {
		return coord2JTS(segment);
	}
	public static Coordinate getArcMidPt(IomObject segment) throws IoxException {
		String a1=segment.getattrvalue("A1");
		String a2=segment.getattrvalue("A2");
		double arcPt_re;
		try{
			arcPt_re = Double.parseDouble(a1);
		}catch(Exception ex){
			throw new IoxException("failed to read A1 <"+a1+">",ex);
		}
		double arcPt_ho;
		try{
			arcPt_ho = Double.parseDouble(a2);
		}catch(Exception ex){
			throw new IoxException("failed to read A2 <"+a2+">",ex);
		}
		Coordinate midPt=new Coordinate(arcPt_re, arcPt_ho);
		return midPt;
	}
	
	
	
	/** Converts a POLYLINE to a JTS CoordinateList.
	 * @param polylineObj INTERLIS POLYLINE structure
	 * @param isSurfaceOrArea true if called as part of a SURFACE conversion.
	 * @param p not used anymore
	 * @return JTS CoordinateList
	 * @throws Iox2jtsException
	 */
	public static CompoundCurve polyline2JTS(IomObject polylineObj,boolean isSurfaceOrArea,double p)
	throws IoxException
	{
		if(polylineObj==null){
			return null;
		}
		Log2EhiLogger logger=new Log2EhiLogger();
		LogEventFactory errs=new LogEventFactory();
		errs.setLogger(logger);
		OutParam<Boolean> foundErrs=new OutParam<Boolean>();
		return polyline2JTS(polylineObj, isSurfaceOrArea, p,foundErrs,errs,0.0,ValidationConfig.WARNING,ValidationConfig.WARNING);
	}
	public static CompoundCurve polyline2JTS(IomObject polylineObj,boolean isSurfaceOrArea,double p,OutParam<Boolean> foundErrs,LogEventFactory errs,double tolerance,String validationType,String degeneratedArcValidationType)
	throws IoxException
	{
		foundErrs.value=false;
		if(polylineObj==null){
			return null;
		}
		ArrayList<CurveSegment> ret=new ArrayList<CurveSegment>();
		// is POLYLINE?
		if(isSurfaceOrArea){
			IomObject lineattr=polylineObj.getattrobj("lineattr",0);
			if(lineattr!=null){
				//writeAttrs(out,lineattr);
				throw new IoxException("Lineattributes not supported");							
			}
		}
		boolean clipped=polylineObj.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
		if(clipped){
			throw new IoxException("clipped polyline not supported");
		}
		for(int sequencei=0;sequencei<polylineObj.getattrvaluecount("sequence");sequencei++){
			if(clipped){
				//out.startElement(tags::get_CLIPPED(),0,0);
			}else{
				// an unclipped polyline should have only one sequence element
				if(sequencei>0){
					throw new IoxException("unclipped polyline with multi 'sequence' elements");
				}
			}
			com.vividsolutions.jts.geom.Coordinate lastSegmentEndpoint=null;
			IomObject sequence=polylineObj.getattrobj("sequence",sequencei);
			for(int segmenti=0;segmenti<sequence.getattrvaluecount("segment");segmenti++){
				IomObject segment=sequence.getattrobj("segment",segmenti);
				//EhiLogger.debug("segmenttag "+segment.getobjecttag());
				CurveSegment curve=null;
				if(segment.getobjecttag().equals("COORD")){
					// COORD
					if(lastSegmentEndpoint==null){
						lastSegmentEndpoint=coord2JTS(segment);
					}else{
						Coordinate newSegEndPt=coord2JTS(segment);
						if(lastSegmentEndpoint.equals2D(newSegEndPt, tolerance)){
							foundErrs.value = foundErrs.value || logMsg(errs,validationType,"duplicate coord at {0}",newSegEndPt.toString());
						}else{
							curve=new StraightSegment(lastSegmentEndpoint,newSegEndPt);
							ret.add(curve);
							lastSegmentEndpoint=curve.getEndPoint();
						}
					}
				}else if(segment.getobjecttag().equals("ARC")){
					// ARC
					//arc2JTS(ret,segment,p);
					Coordinate newSegMidPt=getArcMidPt(segment);
					Coordinate newSegEndPt=getArcEndPt(segment);
					if(lastSegmentEndpoint==null){
						throw new IoxException("unexpected ARC");
					}
					if(lastSegmentEndpoint.equals2D(newSegMidPt, tolerance)){
						if(newSegMidPt.equals2D(newSegEndPt, tolerance)){
							foundErrs.value = foundErrs.value || logMsg(errs,validationType,"duplicate coord at {0}",newSegEndPt.toString());
						}else{
							foundErrs.value = foundErrs.value || logMsg(errs,validationType,"duplicate coord at {0}",newSegMidPt.toString());
							curve=new StraightSegment(lastSegmentEndpoint,newSegEndPt);
							ret.add(curve);
							lastSegmentEndpoint=curve.getEndPoint();
						}
					}else if(newSegMidPt.equals2D(newSegEndPt, tolerance)){
						foundErrs.value = foundErrs.value || logMsg(errs,validationType,"duplicate coord at {0}",newSegMidPt.toString());
						curve=new StraightSegment(lastSegmentEndpoint,newSegMidPt);
						ret.add(curve);
						lastSegmentEndpoint=curve.getEndPoint();
					}else{
						curve=new ArcSegment(lastSegmentEndpoint,newSegMidPt,newSegEndPt);
						if(((ArcSegment) curve).isStraight()){
							foundErrs.value = foundErrs.value || logMsg(errs,degeneratedArcValidationType,"arc is straight at {0}",((ArcSegment) curve).getMidPoint().toString());
							curve=new StraightSegment(curve.getStartPoint(),curve.getEndPoint());
						}
						ret.add(curve);
						lastSegmentEndpoint=curve.getEndPoint();
					}
				}else{
					// custum line form
					throw new IoxException("custom line form not supported");
					//out.startElement(segment->getTag(),0,0);
					//writeAttrs(out,segment);
					//out.endElement(/*segment*/);
				}
			}
			if(clipped){
				//out.endElement(/*CLIPPED*/);
			}
		}
		return new CompoundCurve(ret,new JtsextGeometryFactory());
	}
	private static boolean logMsg(LogEventFactory errs,String validateKind,String msg,String... args){
		 if(ValidationConfig.WARNING.equals(validateKind)){
			 errs.addEvent(errs.logWarningMsg(msg, args));
			 return false;
		 }else{
			 errs.addEvent(errs.logErrorMsg(msg, args));
			 return true;
		 }
	}
	/** Converts a SURFACE to a JTS Polygon.
	 * @param obj INTERLIS SURFACE structure
	 * @param strokeP not used any more
	 * @return JTS Polygon
	 * @throws Iox2jtsException
	 */
	public static com.vividsolutions.jts.geom.Polygon surface2JTS(IomObject obj,double strokeP) //SurfaceOrAreaType type)
	throws IoxException
	{
		Log2EhiLogger logger=new Log2EhiLogger();
		LogEventFactory errs=new LogEventFactory();
		errs.setLogger(logger);
		OutParam<Boolean> foundErrs=new OutParam<Boolean>();
		return surface2JTS(obj,strokeP,foundErrs,errs,0.0,ValidationConfig.WARNING);
	}
	public static com.vividsolutions.jts.geom.Polygon surface2JTS(IomObject obj,double strokeP, OutParam<Boolean> foundErrs,LogEventFactory errs,double tolerance,String validationType) //SurfaceOrAreaType type)
	throws IoxException
	{
		foundErrs.value=false;
		if(obj==null){
			return null;
		}
		com.vividsolutions.jts.geom.Polygon ret=null;
		//IFMEFeatureVector bndries=session.createFeatureVector();
		boolean clipped=obj.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
		if(clipped){
			throw new IoxException("clipped surface not supported");
		}
		String tag=obj.getobjecttag();
		if(!"MULTISURFACE".equals(tag)){
			throw new IoxException("unexpected Type "+tag+"; MULTISURFACE expected");
		}
		int surfacec=obj.getattrvaluecount("surface");
		if(surfacec==0){
			throw new IoxException("at least one element surface expected");
		}
		for(int surfacei=0;surfacei<surfacec;surfacei++){
			if(clipped){
				//out.startElement("CLIPPED",0,0);
			}else{
				// an unclipped surface should have only one surface element
				if(surfacei>0){
					throw new IoxException("unclipped surface with multi 'surface' elements");
				}
			}
			IomObject surface=obj.getattrobj("surface",surfacei);
			CompoundCurveRing shell=null;
			CompoundCurveRing holes[]=null;
			int boundaryc=surface.getattrvaluecount("boundary");
			if(boundaryc>1){
				holes=new CompoundCurveRing[boundaryc-1];				
			}
			for(int boundaryi=0;boundaryi<boundaryc;boundaryi++){
				IomObject boundary=surface.getattrobj("boundary",boundaryi);
				ArrayList<CompoundCurve> jtsLines=new ArrayList<CompoundCurve>();
				for(int polylinei=0;polylinei<boundary.getattrvaluecount("polyline");polylinei++){
					IomObject polyline=boundary.getattrobj("polyline",polylinei);
					OutParam<Boolean> lineErrs=new OutParam<Boolean>();
					CompoundCurve jtsLine=polyline2JTS(polyline,true,strokeP,lineErrs,errs,tolerance,validationType,ValidationConfig.WARNING);
					if(lineErrs.value){
						foundErrs.value=foundErrs.value || lineErrs.value;
					}
					jtsLines.add(jtsLine);
				}
				if(boundaryi==0){
					shell=new CompoundCurveRing(jtsLines,new JtsextGeometryFactory());
				}else{
					holes[boundaryi-1]=new CompoundCurveRing(jtsLines,new JtsextGeometryFactory());
				}
				//bndries.append(fmeLine);
			}
			ret=new JtsextGeometryFactory().createPolygon(shell,holes);
			if(clipped){
				//out.endElement(/*CLIPPED*/);
			}
		}
		return ret;
	}
	
	public static ArrayList<CompoundCurve> surface2JTSCompoundCurves(IomObject obj,String validationType,double tolerance,LogEventFactory errFact) throws IoxException {
		if(obj==null){
			return null;
		}
		boolean clipped=obj.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
		if(clipped){
			throw new IoxException("clipped surface not supported");
		}
		String tag=obj.getobjecttag();
		if(!"MULTISURFACE".equals(tag)){
			throw new IoxException("unexpected Type "+tag+"; MULTISURFACE expected");
		}
		int surfacec=obj.getattrvaluecount("surface");
		if(surfacec==0){
			throw new IoxException("at least one element surface expected");
		}
		ArrayList<CompoundCurve> jtsLines=new ArrayList<CompoundCurve>();
		for(int surfacei=0;surfacei<surfacec;surfacei++){
			if(clipped){
				//out.startElement("CLIPPED",0,0);
			}else{
				// an unclipped surface should have only one surface element
				if(surfacei>0){
					throw new IoxException("unclipped surface with multi 'surface' elements");
				}
			}
			IomObject surface=obj.getattrobj("surface",surfacei);
			for(int boundaryi=0;boundaryi<surface.getattrvaluecount("boundary");boundaryi++){
				IomObject boundary=surface.getattrobj("boundary",boundaryi);
				for(int polylinei=0;polylinei<boundary.getattrvaluecount("polyline");polylinei++){
					IomObject polyline=boundary.getattrobj("polyline",polylinei);
					CompoundCurve jtsLine=Iox2jtsext.polyline2JTS(polyline, false, 0.0,new OutParam<Boolean>(),errFact,tolerance,validationType, ValidationConfig.WARNING);
					jtsLines.add(jtsLine);
				}
			}
		}
		return jtsLines;
	}
}