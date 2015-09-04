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

import ch.interlis.iom.IomObject;

/** Utility to convert from JTS to INTERLIS IOM geometry types.
 * @author ceis
 */
public class Jts2iox {
	// utility, no instances
	private Jts2iox(){}
	/** Converts from a hex WKB to an INTERLIS COORD.
	 * @param value hex encoded OGC WKB
	 * @return COORD structure
	 * @throws Iox2jtsException
	 */
	static public IomObject hexwkb2coord(String value)
	throws Iox2jtsException
	{
		try{
			byte bv[]=com.vividsolutions.jts.io.WKBReader.hexToBytes(value);
			com.vividsolutions.jts.geom.Geometry geom =new com.vividsolutions.jts.io.WKBReader().read(bv);
			return JTS2coord(geom.getCoordinate());
		}catch(com.vividsolutions.jts.io.ParseException ex){
			throw new Iox2jtsException(ex);
		}
	}
	/** Converts from a hex WKB to an INTERLIS POLYLINE.
	 * @param value hex encoded OGC WKB
	 * @return POLYLINE structure
	 * @throws Iox2jtsException
	 */
	static public IomObject hexwkb2polyline(String value)
	throws Iox2jtsException
	{
		try{
			byte bv[]=com.vividsolutions.jts.io.WKBReader.hexToBytes(value);
			com.vividsolutions.jts.geom.Geometry geom =new com.vividsolutions.jts.io.WKBReader().read(bv);
			return JTS2polyline((com.vividsolutions.jts.geom.LineString)geom);
		}catch(com.vividsolutions.jts.io.ParseException ex){
			throw new Iox2jtsException(ex);
		}
	}
	/** Converts from a hex WKB to an INTERLIS SURFACE.
	 * @param value hex encoded OGC WKB
	 * @return SURFACE structure
	 * @throws Iox2jtsException
	 */
	static public IomObject hexwkb2surface(String value)
	throws Iox2jtsException
	{
		try{
			byte bv[]=com.vividsolutions.jts.io.WKBReader.hexToBytes(value);
			com.vividsolutions.jts.geom.Geometry geom =new com.vividsolutions.jts.io.WKBReader().read(bv);
			return JTS2surface((com.vividsolutions.jts.geom.Polygon)geom);
		}catch(com.vividsolutions.jts.io.ParseException ex){
			throw new Iox2jtsException(ex);
		}
	}
	/** Converts from a Coordinate to a INTERLIS COORD.
	 * @param value JTS Coordinate.
	 * @return INTERLIS COORD structure
	 */
	static public  IomObject JTS2coord(com.vividsolutions.jts.geom.Coordinate value) 
	{
		IomObject ret=new ch.interlis.iom_j.Iom_jObject("COORD",null);
		ret.setattrvalue("C1", Double.toString(value.x));
		ret.setattrvalue("C2", Double.toString(value.y));
		if(!Double.isNaN(value.z)){
			ret.setattrvalue("C3", Double.toString(value.z));
		}
		return ret;
	}
	/** Converts from a LineString to a INTERLIS POLYLINE.
	 * @param value JTS LineString
	 * @return INTERLIS POLYLINE structure
	 */
	static public  IomObject JTS2polyline(com.vividsolutions.jts.geom.LineString value) 
	{
		IomObject ret=new ch.interlis.iom_j.Iom_jObject("POLYLINE",null);
		IomObject sequence=new ch.interlis.iom_j.Iom_jObject("SEGMENTS",null);
		ret.addattrobj("sequence",sequence);
		int coordc=value.getNumPoints();
		for(int coordi=0;coordi<coordc;coordi++){
			sequence.addattrobj("segment", JTS2coord(value.getCoordinateN(coordi)));
		}
		return ret;
	}
	/** Converts from a Polygon to a INTERLIS SURFACE.
	 * @param value JTS Polygon
	 * @return INTERLIS SURFACE structure
	 */
	static public  IomObject JTS2surface(com.vividsolutions.jts.geom.Polygon value) 
	{
		IomObject ret=new ch.interlis.iom_j.Iom_jObject("MULTISURFACE",null);
		IomObject surface=new ch.interlis.iom_j.Iom_jObject("SURFACE",null);
		ret.addattrobj("surface",surface);

		// shell
		{
			com.vividsolutions.jts.geom.LineString shell=value.getExteriorRing();
			IomObject boundary=new ch.interlis.iom_j.Iom_jObject("BOUNDARY",null);
			surface.addattrobj("boundary",boundary);
			boundary.addattrobj("polyline", JTS2polyline(shell));
		}
		
		// holes
		int holec=value.getNumInteriorRing();
		for(int holei=0;holei<holec;holei++){
			com.vividsolutions.jts.geom.LineString hole=value.getInteriorRingN(holei);
			IomObject boundary=new ch.interlis.iom_j.Iom_jObject("BOUNDARY",null);
			surface.addattrobj("boundary",boundary);
			boundary.addattrobj("polyline", JTS2polyline(hole));
		}
		
		return ret;
	}

}
