package ch.interlis.iom_j.itf.impl.jtsext.noding;

import java.util.Arrays;

import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

public class Intersection {
	Coordinate pt[];
	CompoundCurve curve1;
	CompoundCurve curve2;
	CurveSegment seg1;
	CurveSegment seg2;
	Double overlap;
	public Intersection(Coordinate pt1,CompoundCurve g1,CompoundCurve g2,CurveSegment s1,CurveSegment s2,Double overlap){
		pt=new Coordinate[1];
		pt[0]=new Coordinate(pt1);
		curve1=g1;
		curve2=g2;
		seg1=s1;
		seg2=s2;
		this.overlap=overlap;
	}
	public Intersection(Coordinate pt1,Coordinate pt2,CompoundCurve g1,CompoundCurve g2,CurveSegment s1,CurveSegment s2,Double overlap){
		pt=new Coordinate[2];
		pt[0]=new Coordinate(pt1);
		pt[1]=new Coordinate(pt2);
		curve1=g1;
		curve2=g2;
		seg1=s1;
		seg2=s2;
		this.overlap=overlap;
	}
	public Coordinate[] getPt() {
		return pt;
	}
	public CompoundCurve getCurve1() {
		return curve1;
	}
	public CompoundCurve getCurve2() {
		return curve2;
	}
	public CurveSegment getSegment1(){
		return seg1;
	}
	public CurveSegment getSegment2(){
		return seg2;
	}
	public Double getOverlap()
	{
		return overlap;
	}
	public boolean isIntersection(Coordinate p00) {
		for(Coordinate p:pt){
			if(p.equals2D(p00)){
				return true;
			}
		}
		return false;
	}
	@Override
	public String toString() {
		return "Intersection"
				+ " overlap " + overlap
				+", coord1 " + pt[0].toString()+(pt.length==2?(", coord2 "+pt[1].toString()):"") 
				+ ", tid1 " + seg1.getUserData()
				+ ", tid2 " + seg2.getUserData()
				+ ", idx1 "+curve1.getSegments().indexOf(seg1)+", idx2 "+curve2.getSegments().indexOf(seg2)
				+ ", seg1 " + seg1 + ", seg2 " + seg2
				;
		
	}
}