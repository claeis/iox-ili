package ch.interlis.iom_j.itf.impl.jtsext.noding;

import java.util.Arrays;

import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iox_j.IoxInvalidDataException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

public class Intersection {
	Coordinate pt[];
	CompoundCurve curve1;
	CompoundCurve curve2;
	CurveSegment seg1;
	CurveSegment seg2;
	Double overlap;
	boolean overlay=false;
	public Intersection(Coordinate pt1,CompoundCurve g1,CompoundCurve g2,CurveSegment s1,CurveSegment s2,Double overlap){
		pt=new Coordinate[1];
		pt[0]=new Coordinate(pt1);
		curve1=g1;
		curve2=g2;
		seg1=s1;
		seg2=s2;
		this.overlap=overlap;
	}
	public Intersection(Coordinate pt1,Coordinate pt2,CompoundCurve g1,CompoundCurve g2,CurveSegment s1,CurveSegment s2,Double overlap,boolean isOverlay){
		pt=new Coordinate[2];
		pt[0]=new Coordinate(pt1);
		pt[1]=new Coordinate(pt2);
		curve1=g1;
		curve2=g2;
		seg1=s1;
		seg2=s2;
		this.overlap=overlap;
		overlay=isOverlay;
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
	    Object seg1UserData=seg1.getUserData();
        Object seg2UserData=seg2.getUserData();
        if(isOverlay()) {
    		return "Overlay"
    				+" coord1 " + pt[0].toString()+", coord2 "+pt[1].toString()
    				+ ", tid1 " + (seg1UserData!=null?seg1UserData:curve1.getUserData())
    				+ ", tid2 " + (seg2UserData!=null?seg2UserData:curve2.getUserData())
    				+ ", idx1 "+curve1.getSegments().indexOf(seg1)+", idx2 "+curve2.getSegments().indexOf(seg2)
    				+ ", seg1 " + seg1 + ", seg2 " + seg2
    				;        	
        }
		return "Intersection"
				+ (pt.length==2?" overlap " + overlap+",":"")
				+" coord1 " + pt[0].toString()+(pt.length==2?(", coord2 "+pt[1].toString()):"") 
				+ ", tid1 " + (seg1UserData!=null?seg1UserData:curve1.getUserData())
				+ ", tid2 " + (seg2UserData!=null?seg2UserData:curve2.getUserData())
				+ ", idx1 "+curve1.getSegments().indexOf(seg1)+", idx2 "+curve2.getSegments().indexOf(seg2)
				+ ", seg1 " + seg1 + ", seg2 " + seg2
				;
		
	}
    public String toShortString() {
        Object seg1UserData=seg1.getUserData();
        Object seg2UserData=seg2.getUserData();
        String tids[]=new String[2];
        tids[0]=(String) (seg1UserData!=null?seg1UserData:curve1.getUserData());
        tids[1]=(String) (seg2UserData!=null?seg2UserData:curve2.getUserData());
        if(isOverlay()) {
            Coordinate coord0=pt[0];
            Coordinate coord1=pt[1];
            if(coord0.compareTo(coord1)>0) {
                coord0=pt[1];
                coord1=pt[0];
            }
            return "Overlay"
                    +" coord1 " + toString(coord0)+", coord2 "+toString(coord1) 
                    + ", " + IoxInvalidDataException.formatTids(tids)
                    ;
        }
        return "Intersection"
                + (pt.length==2?" overlap " + overlap+",":"")
                +" coord1 " + toString(pt[0])+(pt.length==2?(", coord2 "+toString(pt[1])):"") 
                + ", " + IoxInvalidDataException.formatTids(tids)
                ;
        
    }
    public String toString(Coordinate coord) {
        java.util.Formatter txt=new java.util.Formatter();
        return txt.format("(%.3f, %.3f)", coord.x,coord.y).toString();
        //return Double.coord.toString();
    }
	public boolean isOverlay() {
		return overlay;
	}
}