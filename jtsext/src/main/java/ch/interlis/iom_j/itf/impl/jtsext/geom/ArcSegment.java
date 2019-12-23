package ch.interlis.iom_j.itf.impl.jtsext.geom;

import ch.interlis.iom_j.itf.impl.hrg.HrgUtility;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequences;
import com.vividsolutions.jts.geom.Envelope;

public class ArcSegment extends CurveSegment {

	private Coordinate startPoint=null;
	private Coordinate midPoint=null;
	private Coordinate endPoint=null;
	private Coordinate centerPoint=null;
	private com.vividsolutions.jts.geom.Coordinate[] ret=null;
	private static final double EPSILON=0.00000001;

	public ArcSegment(Object userData,Coordinate startPoint, Coordinate midPoint,
			Coordinate endPoint) {
		this.userData=userData;
		this.startPoint=new Coordinate(startPoint);
		this.midPoint=new Coordinate(midPoint);
		this.endPoint=new Coordinate(endPoint);
		if(this.midPoint.z==Double.NaN && this.endPoint.z!=Double.NaN){
			this.midPoint.z=(this.endPoint.z+this.startPoint.z)/2.0;
		}
	}
	public ArcSegment(Coordinate startPoint, Coordinate midPoint,
			Coordinate endPoint) {
		this(null,startPoint,midPoint,endPoint);
	}

	@Override
	public Coordinate getEndPoint()  {
		return endPoint;
	}
	public Coordinate getNormalizedEndPoint()  {
		return isArcNormalized() ? endPoint : startPoint;
	}
	@Override
	public Coordinate getStartPoint()  {
		return startPoint;
	}
	public Coordinate getNormalizedStartPoint()  {
		return isArcNormalized() ? startPoint : endPoint;
	}
	public Coordinate getMidPoint()  {
		return midPoint;
	}

	private static com.vividsolutions.jts.geom.Coordinate[] arc2straight(com.vividsolutions.jts.geom.Coordinate startPoint, com.vividsolutions.jts.geom.Coordinate midPoint,com.vividsolutions.jts.geom.Coordinate endPoint,double radius, com.vividsolutions.jts.geom.Coordinate centerPoint, double sign, double deta, double p)
	//throws IoxException
	{
	        com.vividsolutions.jts.geom.CoordinateList ret=new com.vividsolutions.jts.geom.CoordinateList();
		
			com.vividsolutions.jts.geom.Coordinate p1=null;
			p1=startPoint;
			double pt1_re=p1.x;
			double pt1_ho=p1.y;

			double arcPt_re=midPoint.x;
			double arcPt_ho=midPoint.y;

			double pt2_re=endPoint.x;
			double pt2_ho=endPoint.y;

			// Distanz zwischen Bogenanfanspunkt und Zwischenpunkt
			double a=CurveSegment.dist(pt1_re,pt1_ho,arcPt_re,arcPt_ho);
			// Distanz zwischen Zwischenpunkt und Bogenendpunkt 
			double b=CurveSegment.dist(arcPt_re,arcPt_ho,pt2_re,pt2_ho);
			
			double r=radius;
			// Kreismittelpunkt
			double thetaM=deta;
			double reM=centerPoint.x;
			double hoM=centerPoint.y;

			// mindest Winkelschrittweite
			double theta=2*Math.acos(1-p/Math.abs(r));

			ret.add(new com.vividsolutions.jts.geom.Coordinate(pt1_re, pt1_ho));
			
			if(a>2*p){
				// Zentriwinkel zwischen pt1 und arcPt
				double alpha=2.0*Math.asin(a/2.0/Math.abs(r));
				// anzahl Schritte
				int alphan=(int)Math.ceil(alpha/theta);
				// Winkelschrittweite
				double alphai=alpha/(alphan*(sign));
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
				double betai=beta/(betan*(sign));
				double ri=Math.atan2(arcPt_re-reM,arcPt_ho-hoM);
				for(int i=1;i<betan;i++){
					ri += betai;
					double pti_re=reM + Math.abs(r) * Math.sin(ri);
					double pti_ho=hoM + Math.abs(r) * Math.cos(ri);
					ret.add(new com.vividsolutions.jts.geom.Coordinate(pti_re, pti_ho));
				}
			}
			ret.add(new com.vividsolutions.jts.geom.Coordinate(pt2_re, pt2_ho));
			return ret.toCoordinateArray();
	}

	@Override
	public Coordinate[] getCoordinates() {
		if(ret==null){
		    getCenterPoint();
		    if(isArcNormalized()) {
	            com.vividsolutions.jts.geom.Coordinate[] ret=arc2straight(startPoint,midPoint,endPoint,radius,centerPoint,sign,deta,0.001);
	            this.ret=ret;
		    }else {
                com.vividsolutions.jts.geom.Coordinate[] ret=arc2straight(endPoint,midPoint,startPoint,radius,centerPoint,-sign,deta,0.001);
                com.vividsolutions.jts.geom.impl.CoordinateArraySequence tmp=new com.vividsolutions.jts.geom.impl.CoordinateArraySequence(ret);
                CoordinateSequences.reverse(tmp);
                this.ret=tmp.toCoordinateArray();
		    }
		}
		return ret;
	}

	double radius;
	//SIGNO   O-argument: sign of convexity type of the
	// circle relative to the boundary of which
	// lit is part. values:
	// =-1	concave situation, Z at left of PSQ
	// =+l 	convex situation, Z at right of PSQ
	// =0	convexity not defined (strigt line or full circle
	double sign=0.0;
	double deta=0.0;
	public Coordinate getCenterPoint() {
		if(centerPoint==null){
			double Z1O[]=new double[1];
			double Z2O[]=new double[1];
			double DETAO[]=new double[1];
			double SIGNO[]=new double[1];
			if(isArcNormalized()){
				  HrgUtility.CTRC3P(startPoint.x,startPoint.y,  midPoint.x,midPoint.y, endPoint.x,endPoint.y, Z1O,Z2O,DETAO,SIGNO);
				  centerPoint=new Coordinate(Z1O[0],Z2O[0]);
				  sign=SIGNO[0];
				  deta=DETAO[0];
				  radius=CurveSegment.dist(startPoint.x,startPoint.y,centerPoint.x,centerPoint.y);
			}else{
				  HrgUtility.CTRC3P( endPoint.x,endPoint.y,  midPoint.x,midPoint.y,startPoint.x,startPoint.y, Z1O,Z2O,DETAO,SIGNO);
				  centerPoint=new Coordinate(Z1O[0],Z2O[0]);
				  sign=-SIGNO[0];
				  deta=DETAO[0];
				  radius=CurveSegment.dist(endPoint.x,endPoint.y,centerPoint.x,centerPoint.y);
			}
			  
		}
		return centerPoint;
	}
	public boolean isArcNormalized() {
		return startPoint.compareTo(endPoint)>=0;
	}
	public double getRadius() {
		if(centerPoint==null){
			getCenterPoint();
		}
		return radius;
	}
	public boolean isStraight()
	{
		if(centerPoint==null){
			getCenterPoint();
		}
		return sign==0.0 && deta==0.0;
	}
	private boolean isFullCircle() {
		if(centerPoint==null){
			getCenterPoint();
		}
		return sign==0.0 && deta==HrgUtility.TWO_PI;
	}
	@Override
	  Envelope expandEnvelope(Envelope env)
	  {
		  env=super.expandEnvelope(env);
		  Coordinate center=getCenterPoint();
		  if(isStraight()){
			  return env;
		  }
		  if(isFullCircle()){
			  double radius=getRadius();
			  env.expandToInclude(center.x-radius,center.y-radius);
			  env.expandToInclude(center.x+radius,center.y+radius);
			  return env;
		  }

		  Coordinate start=getStartPoint();
		  Coordinate end=getEndPoint();
			//check if the max pts lies on the arc
			double ALFA =  HrgUtility.PSECOS (start.x,start.y,center.x,center.y,end.x,end.y);
			Coordinate axpts[]={
					new Coordinate(center.x+radius,center.y),
					new Coordinate(center.x       ,center.y+radius),
					new Coordinate(center.x-radius,center.y),
					new Coordinate(center.x       ,center.y-radius),
					
			};
			for(int i=0;i<axpts.length;i++){
				double BETA =  HrgUtility.PSECOS (start.x,start.y,center.x,center.y,axpts[i].x,axpts[i].y);
				if( ALFA == BETA || Math.signum(BETA-ALFA)==sign){
					// axpts lies inside the arc
					env.expandToInclude(axpts[i].x,axpts[i].y);
				}
				
			}
			
		  return env;
	  }


	@Override
	public String toString()
	{
		return "CIRCULARSTRING ("+startPoint.x+" "+startPoint.y+", "+midPoint.x+" "+midPoint.y+", "+endPoint.x+" "+endPoint.y+")";
	}
	public double getSign() {
		if(centerPoint==null){
			getCenterPoint();
		}
		return sign;
	}
	public double getTheta() {
		if(centerPoint==null){
			getCenterPoint();
		}
		return deta;
	}
	public Coordinate getDirectionPt(boolean atStart,double dist) {
		if(dist>0){
			double radius=getRadius();
			Coordinate center=getCenterPoint();
			if(atStart){
	            if(dist/2.0>Math.abs(radius) || dist>CurveSegment.dist(startPoint.x,startPoint.y,endPoint.x,endPoint.y)) {
	                return endPoint;
	            }
				// Zentriwinkel zwischen start und directionPt
				double alpha=2.0*Math.asin(dist/2.0/Math.abs(radius));
				double ri=Math.atan2(startPoint.x-center.x,startPoint.y-center.y);
				ri=ri+alpha*sign;
				double pti_re=center.x + Math.abs(radius) * Math.sin(ri);
				double pti_ho=center.y + Math.abs(radius) * Math.cos(ri);
				Coordinate directionPt=new Coordinate(pti_re,pti_ho);
				return directionPt;
			}else{
                if(dist/2.0>Math.abs(radius) || dist>CurveSegment.dist(endPoint.x,endPoint.y,startPoint.x,startPoint.y)) {
                    return startPoint;
                }
				// Zentriwinkel zwischen end und directionPt
				double alpha=2.0*Math.asin(dist/2.0/Math.abs(radius));
				double ri=Math.atan2(endPoint.x-center.x,endPoint.y-center.y)-alpha*sign;
				double pti_re=center.x + Math.abs(radius) * Math.sin(ri);
				double pti_ho=center.y + Math.abs(radius) * Math.cos(ri);
				Coordinate directionPt=new Coordinate(pti_re,pti_ho);
				return directionPt;
			}
			
		}
		if(atStart){
			Coordinate directionPt=CompoundCurve.calcKleinp(getCenterPoint(), startPoint, radius, 1.0*-sign);
			return directionPt;
		}else{
			Coordinate directionPt=CompoundCurve.calcKleinp(getCenterPoint(), endPoint, radius, -1.0*-sign);
			return directionPt;
		}
	}
	public static Coordinate calcArcPt(Coordinate start, Coordinate end,
			Coordinate center, double radius, double sign) {
		Coordinate midPt;
		// calulate new mid pt
		// Zentriwinkel zwischen start und end
		double a=CurveSegment.dist(start.x,start.y,end.x,end.y);
		// Richtung des Punktes auf dem halben Bogen 
		double alpha=Math.atan2(start.x-center.x,start.y-center.y)+sign*Math.asin(a/2.0/radius);
		midPt=new Coordinate();
		midPt.x=center.x + radius * Math.sin(alpha);
		midPt.y=center.y + radius * Math.cos(alpha);
		return midPt;
	}
}
