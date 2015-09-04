package ch.interlis.iom_j.itf.impl.jtsext.geom;

import ch.interlis.iom_j.itf.impl.hrg.HrgUtility;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

public class ArcSegment extends CurveSegment {

	private Coordinate startPoint=null;
	private Coordinate midPoint=null;
	private Coordinate endPoint=null;
	private Coordinate centerPoint=null;
	private com.vividsolutions.jts.geom.CoordinateList ret=null;
	

	public ArcSegment(Object userData,Coordinate startPoint, Coordinate midPoint,
			Coordinate endPoint) {
		this.userData=userData;
		this.startPoint=new Coordinate(startPoint);
		this.midPoint=new Coordinate(midPoint);
		this.endPoint=new Coordinate(endPoint);
	}
	public ArcSegment(Coordinate startPoint, Coordinate midPoint,
			Coordinate endPoint) {
		this(null,startPoint,midPoint,endPoint);
	}

	@Override
	public Coordinate getEndPoint()  {
		return endPoint;
	}
	@Override
	public Coordinate getStartPoint()  {
		return startPoint;
	}
	public Coordinate getMidPoint()  {
		return midPoint;
	}

	private void arc2straight(double p)
	//throws IoxException
	{
		    ret=new com.vividsolutions.jts.geom.CoordinateList();
		
			com.vividsolutions.jts.geom.Coordinate p1=null;
			p1=startPoint;
			double pt1_re=p1.x;
			double pt1_ho=p1.y;

			double arcPt_re=midPoint.x;
			double arcPt_ho=midPoint.y;

			double pt2_re=endPoint.x;
			double pt2_ho=endPoint.y;
			
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
			double a=CurveSegment.dist(pt1_re,pt1_ho,arcPt_re,arcPt_ho);
			// Distanz zwischen Zwischenpunkt und Bogenendpunkt 
			double b=CurveSegment.dist(arcPt_re,arcPt_ho,pt2_re,pt2_ho);

			// Zwischenpunkte erzeugen, so dass maximale Pfeilhöhe nicht 
			// überschritten wird
			// Distanz zwischen Bogenanfanspunkt und Bogenendpunkt 
			double c=CurveSegment.dist(pt1_re,pt1_ho,pt2_re,pt2_ho);
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

			ret.add(new com.vividsolutions.jts.geom.Coordinate(pt1_re, pt1_ho));
			
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

	@Override
	public Coordinate[] getCoordinates() {
		if(ret==null){
			arc2straight(0.001);
		}
		return ret.toCoordinateArray();
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
			  HrgUtility.CTRC3P(startPoint.x,startPoint.y,  midPoint.x,midPoint.y, endPoint.x,endPoint.y, Z1O,Z2O,DETAO,SIGNO);
			  centerPoint=new Coordinate(Z1O[0],Z2O[0]);
			  sign=SIGNO[0];
			  deta=DETAO[0];
			  radius=CurveSegment.dist(startPoint.x,startPoint.y,centerPoint.x,centerPoint.y);
		}
		return centerPoint;
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
}
