package ch.interlis.iom_j.itf.impl.jtsext.algorithm;

import com.vividsolutions.jts.algorithm.RobustLineIntersector;
import com.vividsolutions.jts.geom.Coordinate;
import ch.interlis.iom_j.itf.impl.hrg.HrgUtility;
import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;

public class CurveSegmentIntersector {
	RobustLineIntersector li=new RobustLineIntersector();
	private boolean hasIntersection_;
	private boolean isOverlay_;
	private int isNum;
	private Coordinate[] is=new Coordinate[2];
	private Double overlap=null;

	public void computeIntersection(CurveSegment seg1,
			CurveSegment seg2) {
		isOverlay_=false;
		overlap=null;
		boolean seg1isArc=isTrueArc(seg1);
		boolean seg2isArc=isTrueArc(seg2);
		if(seg1isArc && seg2isArc){
			Coordinate startPt1 = ((ArcSegment) seg1).getNormalizedStartPoint();
			Coordinate endPt1 = ((ArcSegment) seg1).getNormalizedEndPoint();
			Coordinate startPt2 = ((ArcSegment) seg2).getNormalizedStartPoint();
			Coordinate endPt2 = ((ArcSegment) seg2).getNormalizedEndPoint();
			double[] AV1I=null;
			double[] AV2I=null;
			double[] AW1I=null;
			double[] AW2I=null;
			if(((ArcSegment) seg1).getRadius()<((ArcSegment) seg2).getRadius()) {
	            AV1I=new double[]{0.0, startPt1.x,((ArcSegment) seg1).getMidPoint().x,endPt1.x};
	            AV2I=new double[]{0.0, startPt1.y,((ArcSegment) seg1).getMidPoint().y,endPt1.y};
	            AW1I=new double[]{0.0, startPt2.x,((ArcSegment) seg2).getMidPoint().x,endPt2.x};
	            AW2I=new double[]{0.0, startPt2.y,((ArcSegment) seg2).getMidPoint().y,endPt2.y};
			}else {
                AV1I=new double[]{0.0, startPt2.x,((ArcSegment) seg2).getMidPoint().x,endPt2.x};
                AV2I=new double[]{0.0, startPt2.y,((ArcSegment) seg2).getMidPoint().y,endPt2.y};
                AW1I=new double[]{0.0, startPt1.x,((ArcSegment) seg1).getMidPoint().x,endPt1.x};
                AW2I=new double[]{0.0, startPt1.y,((ArcSegment) seg1).getMidPoint().y,endPt1.y};
			}
			int[] NHO=new int[1];
			double[] H1O=new double[3];
			double[] H2O=new double[3];
			double[] OVERLAP=new double[1];
			HrgUtility.ISCICR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
			if(NHO[0]==1){
				hasIntersection_=true;
				isNum=1;
				is[0]=new Coordinate(H1O[1],H2O[1]);
				overlap=OVERLAP[0];
			}else if(NHO[0]==2){
				hasIntersection_=true;
				isNum=2;
				is[0]=new Coordinate(H1O[1],H2O[1]);
				is[1]=new Coordinate(H1O[2],H2O[2]);
	            normalizeIs();
				overlap=OVERLAP[0];
	        }else if(NHO[0]==3){
	            isOverlay_=true;
	            hasIntersection_=true;
	            isNum=2;
	            is[0]=new Coordinate(H1O[1],H2O[1]);
	            is[1]=new Coordinate(H1O[2],H2O[2]);
	            normalizeIs();
	            overlap=OVERLAP[0];
			}else{
				hasIntersection_=false;
				isNum=0;
			}
		}else if(seg1isArc){
			intersectArcStraight((ArcSegment)seg1, seg2);
		}else if(seg2isArc){
			intersectArcStraight((ArcSegment)seg2, seg1);
		}else{
			// seg1 instanceof StraightSegment && seg2 instanceof StraightSegment
			Coordinate startPt1;
			Coordinate endPt1;
			Coordinate startPt2;
			Coordinate endPt2;
			startPt1 = seg1.getStartPoint();
			endPt1 = seg1.getEndPoint();
			startPt2 = seg2.getStartPoint();
			endPt2 = seg2.getEndPoint();
			li.computeIntersection(startPt1, endPt1, startPt2,endPt2);
			hasIntersection_=li.hasIntersection();
			isNum=li.getIntersectionNum();
			isOverlay_=hasIntersection_ ? isNum==2 : false;
			for(int i=0;i<isNum;i++){
				is[i]=li.getIntersection(i);
			}
			normalizeIs();
		}
	}

    private void normalizeIs() {
        if(isNum>1) {
            if(is[0].compareTo(is[1])>0) {
                Coordinate t=is[0];
                is[0]=is[1];
                is[1]=t;
            }
        }
    }

	public boolean isOverlay() {
		return isOverlay_;
	}
	
	public static boolean isTrueArc(CurveSegment seg) {
		if(seg instanceof ArcSegment){
			if(!((ArcSegment) seg).isStraight()){
				return true;
			}
		}
		return false;
	}

	public void intersectArcStraight(ArcSegment seg1, CurveSegment seg2) {
		Coordinate startPt1 = seg1.getNormalizedStartPoint();
		Coordinate endPt1 = seg1.getNormalizedEndPoint();
		Coordinate startPt2 = seg2.getStartPoint();
		Coordinate endPt2 = seg2.getEndPoint();
		double[] AV1I={0.0, startPt1.x,seg1.getMidPoint().x,endPt1.x};
		double[] AV2I={0.0, startPt1.y,seg1.getMidPoint().y,endPt1.y};
		double[] AW1I={0.0, startPt2.x,endPt2.x};
		double[] AW2I={0.0, startPt2.y,endPt2.y};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCISR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		if(NHO[0]==1){
			hasIntersection_=true;
			isNum=1;
			is[0]=new Coordinate(H1O[1],H2O[1]);
			overlap=OVERLAP[0];
		}else if(NHO[0]==2){
			hasIntersection_=true;
			isNum=2;
			is[0]=new Coordinate(H1O[1],H2O[1]);
			is[1]=new Coordinate(H1O[2],H2O[2]);
            normalizeIs();
			overlap=OVERLAP[0];
		}else{
			hasIntersection_=false;
			isNum=0;
		}
	}

	public boolean hasIntersection() {
		return hasIntersection_;
	}

	public int getIntersectionNum() {
		return isNum;
	}

	public boolean isIntersection(Coordinate p00) {
		if(isNum>0){
			if(p00.equals2D(is[0])){
				return true;
			}
			if(isNum>1){
				if(p00.equals2D(is[1])){
					return true;
				}
			}
		}
		return false;
	}

	public Coordinate getIntersection(int i) {
		if(i<0 || i>=isNum){
			throw new IllegalArgumentException("i "+i+", isNum "+isNum);
		}
		return is[i];
	}
	public Double getOverlap()
	{
		return overlap;
	}
}