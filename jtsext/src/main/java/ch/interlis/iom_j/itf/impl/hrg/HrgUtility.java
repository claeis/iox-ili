package ch.interlis.iom_j.itf.impl.hrg;

import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;

public class HrgUtility {
	//static final double EPS=0.001; // epsilon, linear tolerance (1mm in military coordinates)
	//static final double EPS100=EPS;
	static final double EPS=0.00000001;
	static final double EPS100=EPS*190.0;
	// Toleranz um berechnete Schnittpunkte (bei Bogen/Bogen Schnitt)
	//   mit dem Start/Endpunkt der Boegen zu vergleichen
	static final double CIRCIR_ENDPT_TOL=5E-5;

	public static void CTRC3P(double P1I,double P2I,double  S1I,double S2I,double Q1I,double Q2I,double Z1O[],double Z2O[],double DETAO[],double SIGNO[])
	{
		double dx=P1I;
		double dy=P2I;
		double P1I_=P1I-dx;
		double P2I_=P2I-dy;
		double S1I_=S1I-dx;
		double S2I_=S2I-dy;
		double Q1I_=Q1I-dx;
		double Q2I_=Q2I-dy;
		CTRC3P_(P1I_,P2I_,S1I_,S2I_,Q1I_,Q2I_,Z1O,Z2O,DETAO,SIGNO);
		Z1O[0]+=dx;
		Z2O[0]+=dy;
		
	}

public static void CTRC3P_(double P1I,double P2I,double  S1I,double S2I,double Q1I,double Q2I,double Z1O[],double Z2O[],double DETAO[],double SIGNO[])
{ ///////////////////////////////////////////////////////
	//	Center point of a circle defined by 3 points
	//P1I,P2I I-arguments: mil coord of 1st circle def pt
	//Q1I,Q2I I-arguments:mil coord of 3rd circle def pt
	//Sli,S2I I-arguments:mil coord of 2nd circle def pt
	//Z1O,Z2O O-arguments: mil coord of circle center z
	//DETAO   O-argument: determinant of matrix A
	//SIGNO   O-argument: sign of convexity type of the
	// circle relative to the boundary of which
	// lit is part. values:
	// =-1	concave situation, Z at left of PSQ
	// =+l 	convex situation, Z at right of PSQ
	// =0	convexity not defined (strigt line or full circle
	 
	//	local variables	 
	double A11,A12,A21,A22; // elements of matrix A
	double D1,D2; // difference vector z - p = z - v(l)
	double G1,G2; // right side vector of linear equation system
	double L1,L2; // factors for the vectors n,m to point to Z
	double M1,M2,N1,N2; // components of normal vectors m,n
	double X1,X2,Y1,Y2; // mil coord of points X andY
	 
	 
	// 	points X and Y in the middle of the stright line segments PS and SQ
	// 	respectively (P := V(l), S := V(2), Q := V(3) ).
	X1 = (P1I + S1I)/ 2.00;
	X2 = (P2I + S2I)/ 2.00;
	Y1 = (S1I + Q1I)/ 2.00;
	Y2 = (S2I + Q2I)/ 2.00;
	 
	// 	vectors n and m orthogonal to PS = s - p and SQ =  q - s respectively
	N1 = - S2I + P2I;
	N2 =   S1I - P1I;
	M1 = - Q2I + S2I;
	M2 =   Q1I - S1I;

	// 	linear equation system
	A11 = N1;
	A21 = N2;
	A12 = - M1;
	A22 =  - M2;
	G1 = Y1 - X1;
	G2 = Y2 - X2;
	DETAO[0] = A11*A22  - A21*A12;

	// 	DO CASE	intersection type
	// 	CASE	DetA =/= O, m and n are not parallel
	if(!(DETAO[0]==0.0 || Math.abs(DETAO[0])<EPS)){ // CEIS: 20150615 check also near equal to 0
		//or select only one
		//of the 2 possibilities 
		//to calculate
		//point z
	    // lambda1 und lambda2 (=Radius minus Pfeilhoehe auf die beiden Sehnen))
		L1 = (G1*A22 - A12*G2)/ DETAO[0];
		L2 = (A11*G2 - G1*A21)/ DETAO[0];
		Z1O[0] =   (X1 + L1*N1 + Y1 + L2*M1)/ 2.00;
		Z2O[0] =   (X2 + L1*N2 + Y2 + L2*M2)/ 2.00;
		D1 = Z1O[0] - P1I;
		D2 =  Z2O[0] - P2I;
		SIGNO[0] = 1.00;
		 
		if(L1>0.0 || L2>0.0){
			SIGNO[0] = -1.00;
		}
	}else{
		if(P1I==Q1I && P2I==Q2I){
			//	CASE 	DetA == 0 with P == Q. This is equivalent to m = k*n with
			// 	a real k > 0. In this case, a full circle is defined with center
			// Z in the middle between P = Q and S and for the tangent vector
			// in P holds t = m and in Q holds t = n = -m
				Z1O[0] = (P1I + S1I)/ 2.0;
				Z2O[0] = (P2I + S2I)/ 2.0;
				SIGNO[0] = 0.0;
				DETAO[0]=HrgUtility.TWO_PI; // CEIS: 20150615 set to TWO_PI, so that caller can easily detect full circle
		}else{
			// 	CASE	DetA == 0 with P =I= Q. This is equivalent to m =  k*n with
			// 	a real k > 0. In this case, the points P,Q,S lie on the same
			// 	stright line, the circle is degenerated to a stright line.
			Z1O[0] = S1I;
			Z2O[0] = S2I;
			SIGNO[0] = 0.0;
			DETAO[0]=0.0; // CEIS: 20150615 set to 0, because of above check to near equal to 0
		}
	}
	//	END CASE
	
}

/** Curvature calculation of a line segment
 * 
 * @param V1I V1I(3),V2I(3) coord-re of 1st 3 vert of line
 * @param V2I V1I(3),V2I(3) coord-ho of 1st 3 vert of line
 * @param TYLI type of line, values: ' '= stright line, 'A'= arc, 'H' =help stli
 * @param AZITAI azi of tangent vector (pseudocos) in P
 * @return curvature
 */
private static double CURVF(double V1I[],double V2I[],char TYLI,double AZITAI)
{
	// AZITAI	I-arg:azi of tangent vector (pseudocos) in P 
	// TYLI	I-arg:type of line, values:
	//                      ' '= stright line, 'A'= arc, 'H' =help stli
	// V1I(3),V2I(3) I-arg: mil coord of 1st 3 vert of line
	 
	double	ALFA; //angle in P between tang vect and vect to cic
	double AZICIC; //   azi of vect to circ center from P (pseudocos)
	double N1,N2;  //   point in direction N from P
	double RADIUS;  //    radius of the circle
	double SIGN=0.0;    // sign of convexity type of the arc.values
						// = +1 	concave arc, Z at left of PSQ
						// = -1 : convex arc, Z at right of PSQ
						// =	0	convexity not defined, either stright
						// 	line or full circle.
	double	Z1=0.0,Z2=0.0;  // circle center
	 
	if(TYLI==' '){
		//  CASE stright line
		return 0.0;
	}
	if(TYLI!='A'){
		//CASE	undefined line  segment type
		return 0.0;
	}
	//	CASE	arc with defined center
	{
		double Z1O[]=new double[1];
		double Z2O[]=new double[1];
		double DETAO[]=new double[1];
		double SIGNO[]=new double[1];
		CTRC3P(V1I[1],V2I[1],V1I[2],V2I[2],V1I[3],V2I[3],Z1O,Z2O,DETAO,SIGNO);
		Z1=Z1O[0];
		Z2=Z2O[0];
		SIGN=SIGNO[0];
	}
	if(SIGN!=0.0){
		RADIUS= DISTDF(V1I[1],V2I[1],Z1,Z2);
		if(RADIUS==0.0){
			return SIGN * 999999.999;
		}
		return SIGN / RADIUS;
	}
	
	if(V1I[1]!=V1I[3] ||V2I[1]!=V2I[3]){
		//CASE	stright line defined as circle
		return 0.0;
	}
	
	//CASE	full circle with P==Q 
	N1 =  V1I[1];
	N2 = V2I[1] + Math.max (Math.abs(V1I[2]-V1I[1]),Math.abs(V2I[2] - V2I[1]));
	AZICIC = PSECOS(V1I[2],V2I[2],V1I[1],V2I[1],N1,N2);
	ALFA = PSECIV(AZICIC)- PSECIV (AZITAI);
	ALFA = ANGLNR (ALFA);
	SIGN = 1.0; // ALFA araound PI/2
	if(ALFA>Math.PI){
		SIGN = -1.0; // ALFA around 3*PI/2
	}
	RADIUS= DISTDF(V1I[1],V2I[1],Z1,Z2);
	if(RADIUS==0.0){
		return SIGN * 999999.999;
	}
	return SIGN / RADIUS;

	
}


	public static final double TWO_PI=Math.PI+Math.PI;

  private static double ANGLNR(double betai) {
	if(betai<0.0){
		while(betai<0.0){
			betai+=TWO_PI;
		}
	}else{
		while(betai>=TWO_PI){
			betai-=TWO_PI;
		}
	}
	return betai;
}

	static public double PSECIV( double PCOSI){
		//	Pseudocosine inverse function: Returns for a given pseudocosine-value
		//	PCOSI the corresponding angle between 0 and 2*PI
		if(PCOSI<1.0){
			return Math.acos(-PCOSI);
		}
		return Math.acos(- PCOSI + 2.0) + Math.PI;
	}

	public static double PSECOS(double P1I,double P2I,double S1I,double S2I,double Q1I,double Q2I) {

		//	Calculates the pseudocosinus of angle theta between the 2 vectors
		//     P-S and Q-S (theta= angle PSQ). The pseudocosinus (definition see 
		//     downstairs in the program text) is a monotone function of the angle 
		//     with values between -1 (included) and 3 (excluded). Its calculation
		//     needs only one square root evaluation and no inversion of angular
		//     functions (like arcsin or arccos). The pseudocosinus of angles can
		//	   be used to order them. 

		 
		// P1I, P2I I-argument: coordinates pt P
		//	Q1I,Q2I " 	" 	" 	Q
		//	S1I,S2I " 	"	"	s, "Scheitel11     of angle theta
		 
		double 	COS;     // cosine of angle theta
		double 	DP1,DP2; // coord of vector P-S (differences)
		double 	DQ1,DQ2; // coord of vector Q-S (differences)
		double 	H;       // help variable len(P-S) * len(Q-S)
		double 	SIN;     // sine of theta

		DP1 =  P1I - S1I;
		DP2 =  P2I - S2I;
		DQ1 =  Q1I - S1I;
		DQ2 =  Q2I - S2I;
		H =  Math.sqrt((DP1*DP1 + DP2*DP2) * (DQ1*DQ1 + DQ2*DQ2));
		 
		if(H==0.0){
			// vector P-S and/or Q-S has length 0
			return -1.0;
		}
		
		// calculate angular functions for theta
		COS = (DP1*DQ1 + DP2*DQ2)/ H; // definition of scalar product
		SIN =   (DP1*DQ2 - DP2*DQ1) / H; // definition of vector product
		if(SIN>=0.0){
			// 0 <= 	Theta 	<= Pi
			return -COS;
		}
		// Pi 	< 	Theta 	<	2*Pi
		return COS + 2.0;
	}

private static double DISTDF(double a1, double b1, double a2, double b2) {
	double D1 = a2 - a1;
	double D2 = b2-b1;
	double RV =  Math.hypot(D1,D2);
	return RV;
}

/** intersection of 2 circles.
 * @param AV1I [3] arc with center V (coord of 1st 3 vert)
 * @param AV2I
 * @param AW1I [3] arc with center W (coord of 1st 3 vert)
 * @param AW2I
 * @param NHO nr of hits = nr of intersection point
 * @param H1O [2] coord of hit (= intersc) pt
 * @param H2O
 */
public static void ISCICR(double AV1I[],double AV2I[],double AW1I[],double AW2I[],int NHO[],double H1O[],double H2O[],double overlap[])
{
	double dx=Math.min(AV1I[1],AW1I[1]);
	double dy=Math.min(AV2I[1],AW2I[1]);
	double[] AV1I_={AV1I[0]-dx, AV1I[1]-dx,AV1I[2]-dx,AV1I[3]-dx};
	double[] AV2I_={AV2I[0]-dy, AV2I[1]-dy,AV2I[2]-dy,AV2I[3]-dy};
	double[] AW1I_={AW1I[0]-dx, AW1I[1]-dx,AW1I[2]-dx,AW1I[3]-dx};
	double[] AW2I_={AW2I[0]-dy, AW2I[1]-dy,AW2I[2]-dy,AW2I[3]-dy};
	ISCICR_(AV1I_,AV2I_,AW1I_,AW2I_,NHO,H1O,H2O,overlap,dx,dy);
	for(int i=1;i<=NHO[0] && i<=2;i++){
		H1O[i]+=dx;
		H2O[i]+=dy;
	}
}
public static void ISCICR_(double AV1I[],double AV2I[],double AW1I[],double AW2I[],int NHO[],double H1O[],double H2O[],double overlap[],double _dx,double _dy)
	{
	//ENTRY ISCICR (AV1I,AV2I,AW1I,AW2I,NHO,H10,H20,MSGIO,Y2IPDO)
	// 	intersection of 2 circles, R*8 arguments
		//double AV1I[] = null,AV2I[]=null; //[3] I-arguments:  mil coord of 1st 3 vert 
		//double AW1I[]=null,AW2I[]=null; //[3] ces of 2 arcs with center V and W re 
		//int NHO;		// O-arg: nr of hits = nr of intersection point
		//double H1O[]=null,H2O[]=null; //[2] O-arg: mil coord of hit (= intersc) pt
		


		double ALFA=0.0; // angle between the vectors from circle cente
						// to the end points of the arc (in pseudocos)
		double BETA=0.0; // angle between the vectors from circle cente
						// to the start pt of the arc and the IP (psc)
		double D1=0.0,D2=0.0; // differences, help variables
		double DVW=0.0; 	// distance between circle centers V and W
		double DVWSQ=0.0; // square of dist between circle centers
		//double DETA=0.0;	// determinant from calculation of circle cent
		double EFP=0.0; 	// distance between foot point F and intsc.pt. 
		//double F1=0.0,F2=0.0;	// mil coord of foot pt, mid pt between IPs 
		int J = 0,J1=0,J2=0;	// indices of DO-loops
		int JH,JP;		// index of intersection points
		int JV,JW;		// indices of vertices on arcs with centers V,
		int NP=0;
		double P1[]=new double[3],P2[]=new double[3]; //[2]  mil coordinates of intersection points
		double RDIF=0.0,RSUM=0.0;	// difference and sum of radii
		double RV=0.0,RW=0.0;	// radii of circles with center V and W
		//double RVSQ=0.0,RWSQ=0.0; // squares of circle radii
		double SIGNV=0.0,SIGNW=0.0;	//sign of circle center (see CTRC3P) 
		double V1=0.0,V2=0.0;	// mil coordinates of circle center V 
		double W1=0.0,W2=0.0;	// mil coordinates of circle center W 
		double XVF=0.0;		// distance between V and foot point F 
		boolean YCHKEX=false;	// check, if new IPs equal existing IPs? Y=.T.
		
		 
		// 	initialise
		NHO[0] = 0;
		
		// 	check if the circles are well defined
		for(J1=1;J1<=2;J1++){
			for(J2 =  J1+1;J2<=3;J2++){
				if(AV1I[J1]==AV1I[J2] && AV2I[J1]==AV2I[J2]){
					// 	definition of V is wrong
					char TYCIR =	'V';
					double PA1 = AV1I[J1];
					double PA2 = AV2I[J1];
					NHO[0]=-1;
					throw new IllegalArgumentException("definition of arc V is wrong "+PA1+", "+PA2);
				}
				if(AW1I[J1]==AW1I[J2] && AW2I[J1]==AW2I[J2]){
					// 	definition of W is wrong
					char TYCIR =	'W';
					double PA1 = AW1I[J1];
					double PA2 = AW2I[J1];
					NHO[0]=-1;
					throw new IllegalArgumentException("definition of arc W is wrong "+PA1+", "+PA2);
				}
			}
		}
		
		// check if definition points are equal
		for(JV=1;JV<=3;JV++){
			for(JW=1;JW<=3;JW++){
				if(AV1I[JV]==AW1I[JW] && AV2I[JV]==AW2I[JW]){
					if(NHO[0]>=2){
						// 2 identical circles
	                    H1O[NHO[0]] = AV1I[JV];
	                    H2O[NHO[0]] = AV2I[JV];
						NHO[0]=3;
						return;
					}
					NHO[0] =  NHO[0] + 1;
					H1O[NHO[0]] = AV1I[JV];
					H2O[NHO[0]] = AV2I[JV];
				}
			}
		}
		 
		// calculate circle centers V and W
		{
			double Z1O[]=new double[1];
			double Z2O[]=new double[1];
			double DETAO[]=new double[1];
			double SIGNO[]=new double[1];
			
			CTRC3P(AV1I[1],AV2I[1],AV1I[2],AV2I[2],AV1I[3],AV2I[3],Z1O,Z2O,DETAO,SIGNO);
			V1=Z1O[0];
			V2=Z2O[0];
			SIGNV=SIGNO[0];
			if(DETAO[0]==0){
				throw new IllegalArgumentException("unexpected straight line");
			}
			CTRC3P(AW1I[1],AW2I[1],AW1I[2],AW2I[2],AW1I[3],AW2I[3],Z1O,Z2O,DETAO,SIGNO);
			W1=Z1O[0];
			W2=Z2O[0];
			SIGNW=SIGNO[0];
			if(DETAO[0]==0){
				throw new IllegalArgumentException("unexpected straight line");
			}
		}
		 
		// radii of circles with center V and W
		D1 = AV1I[1] - V1;
		D2 = AV2I[1] - V2;
		//RVSQ =  D1*D1 + D2*D2;
		RV =   Math.hypot(D1, D2); // Math.sqrt(RVSQ);
		D1 = AW1I[1] - W1;
		D2 = AW2I[1] - W2;
		//RWSQ =  D1*D1 + D2*D2;
		RW = Math.hypot(D1,D2); // Math.sqrt(RWSQ);
		RSUM = RV + RW;
		RDIF =  Math.abs(RV - RW);
		
		// 	distance between circle centers v and W
		D1 = W1 - V1;
		D2 = W2 - V2;
		DVWSQ =  D1*D1 + D2*D2;
		DVW =   Math.hypot(D1, D2); // Math.sqrt(DVWSQ);
		
		if(RDIF<EPS && DVW < EPS){
			// CASE identical circles
            NHO[0] = 0;
            if(SIGNV==SIGNW) {
                // same direction
                // calc start of overlay
                if(AV1I[1]==AW1I[1] && AV2I[1]==AW2I[1]) {
                    // same start point
                    NHO[0] = NHO[0]+1;
                    H1O[NHO[0]] =  AV1I[1];
                    H2O[NHO[0]] = AV2I[1];
                }else {
                    double ALFA_V = PSECOS (AV1I[1],AV2I[1],V1,V2,AV1I[3],AV2I[3]);
                    double BETA_W1= PSECOS(AV1I[1],AV2I[1],V1,V2,AW1I[1],AW2I[1]);
                    // is start of W outside of V?
                    if( (AV1I[3]==AW1I[1] && AV2I[3]==AW2I[1]) // is end of V == start of W 
                            || ( ALFA_V != BETA_W1 && Math.signum(BETA_W1-ALFA_V) != SIGNV)){ // war ==SIGNV
                        // AW1I[1],AW2I[1] lies outside the arc with center V
                        double ALFA_W =  PSECOS (AW1I[1],AW2I[1],W1,W2,AW1I[3],AW2I[3]); 
                        double BETA_V1= PSECOS (AW1I[1],AW2I[1],W1,W2,AV1I[1],AV2I[1]);
                        // is end of W outside start of V?
                        if( (AV1I[1]==AW1I[3] && AV2I[1]==AW2I[3]) // is end of W == start of V
                                || (ALFA_W != BETA_V1 && Math.signum(BETA_V1-ALFA_W) != SIGNW)){ // war ==SIGNW
                            // AV1I[1],AV2I[1] lies outside the arc with center W
                            // same circle, but no overlay
                            NHO[0] = 0;
                            return;
                        }else {
                            // AV1I[1],AV2I[1] lies inside the arc with center W
                            NHO[0] = NHO[0]+1;
                            H1O[NHO[0]] =  AV1I[1];
                            H2O[NHO[0]] = AV2I[1];
                        }
                    }else {
                        // AW1I[1],AW2I[1] lies inside the arc with center V
                        NHO[0] = NHO[0]+1;
                        H1O[NHO[0]] =  AW1I[1];
                        H2O[NHO[0]] = AW2I[1];
                    }
                    
                }
                
                // calc end of overlay
                if(AV1I[3]==AW1I[3] && AV2I[3]==AW2I[3]) {
                    // same end point
                    NHO[0] = NHO[0]+1;
                    H1O[NHO[0]] =  AV1I[3];
                    H2O[NHO[0]] = AV2I[3];
                }else {
                    double ALFA_V = PSECOS (AV1I[1],AV2I[1],V1,V2,AV1I[3],AV2I[3]);
                    double BETA_W3= PSECOS(AV1I[1],AV2I[1],V1,V2,AW1I[3],AW2I[3]);
                    if( ALFA_V != BETA_W3 && Math.signum(BETA_W3-ALFA_V) != SIGNV){ // war ==SIGNV
                        // AW1I[3],AW2I[3] lies outside the arc with center V
                        double ALFA_W =  PSECOS (AW1I[1],AW2I[1],W1,W2,AW1I[3],AW2I[3]); 
                        double BETA_V3= PSECOS (AW1I[1],AW2I[1],W1,W2,AV1I[3],AV2I[3]);
                        if( ALFA_W != BETA_V3 && Math.signum(BETA_V3-ALFA_W) != SIGNW){ // war ==SIGNW
                            // AV1I[3],AV2I[3] lies outside the arc with center W
                            // same circle, but no overlay
                            NHO[0] = 0;
                            return;
                        }else {
                            // AV1I[3],AV2I[3] lies inside the arc with center W
                            NHO[0] = NHO[0]+1;
                            H1O[NHO[0]] =  AV1I[3];
                            H2O[NHO[0]] = AV2I[3];
                        }
                    }else {
                        // AW1I[3],AW2I[3] lies inside the arc with center V
                        NHO[0] = NHO[0]+1;
                        H1O[NHO[0]] =  AW1I[3];
                        H2O[NHO[0]] = AW2I[3];
                    }
                }
            }else {
                // opposide direction
                // calc start of overlay
                if(AV1I[1]==AW1I[3] && AV2I[1]==AW2I[3]) {
                    // same start point
                    NHO[0] = NHO[0]+1;
                    H1O[NHO[0]] =  AV1I[1];
                    H2O[NHO[0]] = AV2I[1];
                }else {
                    double ALFA_V = PSECOS (AV1I[1],AV2I[1],V1,V2,AV1I[3],AV2I[3]);
                    double BETA_W3= PSECOS(AV1I[1],AV2I[1],V1,V2,AW1I[3],AW2I[3]);
                    if( (AV1I[3]==AW1I[3] && AV2I[3]==AW2I[3]) // is end of V == start of W
                            || (ALFA_V != BETA_W3 && Math.signum(BETA_W3-ALFA_V) != SIGNV)){ // war ==SIGNV
                        // AW1I[3],AW2I[3] lies outside the arc with center V
                        double ALFA_W =  PSECOS (AW1I[1],AW2I[1],W1,W2,AW1I[3],AW2I[3]); 
                        double BETA_V1= PSECOS (AW1I[1],AW2I[1],W1,W2,AV1I[1],AV2I[1]);
                        if( (AV1I[1]==AW1I[1] && AV2I[1]==AW2I[1]) // is end of W == start of V
                                || (ALFA_W != BETA_V1 && Math.signum(BETA_V1-ALFA_W) != SIGNW)){ // war ==SIGNW
                            // AV1I[1],AV2I[1] lies outside the arc with center W
                            // same circle, but no overlay
                            NHO[0] = 0;
                            return;
                        }else {
                            // AV1I[1],AV2I[1] lies inside the arc with center W
                            NHO[0] = NHO[0]+1;
                            H1O[NHO[0]] =  AV1I[1];
                            H2O[NHO[0]] = AV2I[1];
                        }
                    }else {
                        // AW1I[3],AW2I[3] lies inside the arc with center V
                        NHO[0] = NHO[0]+1;
                        H1O[NHO[0]] =  AW1I[3];
                        H2O[NHO[0]] = AW2I[3];
                    }
                    
                }
                
                // calc end of overlay
                if(AV1I[3]==AW1I[1] && AV2I[3]==AW2I[1]) {
                    // same end point
                    NHO[0] = NHO[0]+1;
                    H1O[NHO[0]] =  AV1I[3];
                    H2O[NHO[0]] = AV2I[3];
                }else {
                    double ALFA_V = PSECOS (AV1I[1],AV2I[1],V1,V2,AV1I[3],AV2I[3]);
                    double BETA_W1= PSECOS(AV1I[1],AV2I[1],V1,V2,AW1I[1],AW2I[1]);
                    if( ALFA_V != BETA_W1 && Math.signum(BETA_W1-ALFA_V) != SIGNV){ // war ==SIGNV
                        // AW1I[1],AW2I[1] lies outside the arc with center V
                        double ALFA_W =  PSECOS (AW1I[1],AW2I[1],W1,W2,AW1I[3],AW2I[3]); 
                        double BETA_V1= PSECOS (AW1I[1],AW2I[1],W1,W2,AV1I[1],AV2I[1]);
                        if( ALFA_W != BETA_V1 && Math.signum(BETA_V1-ALFA_W) != SIGNW){ // war ==SIGNW
                            // AV1I[1],AV2I[1] lies outside the arc with center W
                            // same circle, but no overlay
                            NHO[0] = 0;
                            return;
                        }else {
                            // AV1I[1],AV2I[1] lies inside the arc with center W
                            NHO[0] = NHO[0]+1;
                            H1O[NHO[0]] =  AV1I[3];
                            H2O[NHO[0]] = AV2I[3];
                        }
                    }else {
                        // AW1I[1],AW2I[1] lies inside the arc with center V
                        NHO[0] = NHO[0]+1;
                        H1O[NHO[0]] =  AW1I[1];
                        H2O[NHO[0]] = AW2I[1];
                    }
                }
                
            }
            if(NHO[0]==2) {
                NHO[0]=3;
            }else if(NHO[0]!=0) {
                throw new IllegalStateException("unexpected number ("+NHO[0]+") of common points");
            }
			return;
		}else if(DVW >= RDIF+EPS && DVW <= RSUM-EPS){
			//	CASE 	circles meet in two different points
			// 	calculate x, e and the 2 intersection points 
			XVF =   (RV*RV + DVWSQ - RW*RW) / (2.0 * DVW);
			EFP = Math.sqrt (RV*RV - XVF*XVF);
			D1 =  W1 - V1;
			D2 =  W2 - V2;
			 
			if(DVW<Math.max(RV, RW)){
				overlap[0]=DVW-RDIF;
			}else{
				overlap[0]=RSUM-DVW;
			}

			NP=2;
			//System.out.println("RV "+RV+", RW "+RW);
            //System.out.println("V1 "+ V1 +", XVF*D1/DVW "+ (XVF*D1/DVW)+", EFP*D2/DVW " + (EFP*D2/DVW));
            //System.out.println("V2 "+ V2 +", XVF*D2/DVW "+ (XVF*D2/DVW) +",EFP*D1/DVW "+ (EFP*D1/DVW));
			
			P1[1] = V1 + XVF*D1/DVW + EFP*D2/DVW;
			P2[1] = V2 + XVF*D2/DVW - EFP*D1/DVW;
			P1[2] = V1 + XVF*D1/DVW - EFP*D2/DVW; 
			P2[2] = V2 + XVF*D2/DVW + EFP*D1/DVW;
			//System.out.println("p1 ("+(P1[1]+_dx)+","+(P2[1]+_dy)+"), p2 (" +(P1[2]+_dx)+","+(P2[2]+_dy)+")");
			YCHKEX = true;
		}else{
			if(DVW >= RSUM+EPS){
				// CASE 	circles disjoint, one outside of the other, escape, no IP 
				return;
			}
			if(DVW < RDIF-EPS){
				//CASE 	circles disjoint, one of both inside of the other, esc, no IP 
				return;
			}
			if(NHO[0]>0){
				//CASE 	circles meet tangentially in one ''double" point still known
				H1O[2] = H1O[1];
				H2O[2] = H2O[1];
				NHO[0] =  1; // CE war NHO[0] =  2; aber da nur ein Treffer auf 1 geaendert
				overlap[0]=0.0;
				return;
			}
			 
			// CASE 	circles meet tangentially in a "double" point not yet known
			NP=1;
			P1[1]= 	V1 + (W1 - V1)*RV/RSUM;
			P2[1] = V2 + (W2 - V2)*RV/RSUM;
			//P1[2] =  H1O[1];  // CEIS: H1O/H2O sind hier nicht richtig (auf dem Kreis)
			//P2[2] =  H2O[1];
			overlap[0]=0.0;
			YCHKEX = false;
		}

		 

		// compare with still defined IPs and check if inside arcs
		// DO 250 JP =   1,2
		for(JP=1;JP<=NP;JP++){
			if(YCHKEX){
				// compare with still found IPs
				//DO 240 JH =   l,NHO
				boolean continueJP=false;
				for(JH=1;JH<=NHO[0];JH++){
					if(Math.abs(P1[JP] - H1O[JH]) > CIRCIR_ENDPT_TOL){
						continue;
					}
					if(Math.abs(P2[JP] - H2O[JH]) > CIRCIR_ENDPT_TOL){
						continue;
					}
					// THEN 	check next still existing IP
					// ELSE 	new IP equals sill existing IP, test next new
					continueJP=true;
					break;
				}
				if(continueJP){
					continue;
				}
			}
			
			//check if the IP lies on the arcs defining the intersecting circles
			ALFA = PSECOS (AV1I[1],AV2I[1],V1,V2,AV1I[3],AV2I[3]);
			BETA= PSECOS(AV1I[1],AV2I[1],V1,V2,P1[JP],P2[JP]);
			if(	ALFA != BETA && Math.signum(BETA-ALFA) != SIGNV){ // war ==SIGNV
				// THEN next DO, IP lies outside the arc with center V
				continue;
			}
			ALFA =  PSECOS (AW1I[1],AW2I[1],W1,W2,AW1I[3],AW2I[3]); 
			BETA= PSECOS (AW1I[1],AW2I[1],W1,W2,P1[JP],P2[JP]);
			if( ALFA != BETA && Math.signum(BETA-ALFA) != SIGNW){ // war ==SIGNW
				// THEN next DO, IP lies outside the arc with center W
				continue;
			}
						 
			if(NHO[0] >= 2){
				// THEN 	escape with error message, more than 2 IPs (possible, if EPS,EPS100 to small) 
				throw new IllegalStateException("more than 2 IPs ("+H1O[1]+", "+H2O[1]+"), ("+H1O[2]+", "+H2O[2]+"), ("+P1[JP]+", "+P2[JP]+")");
			}
			NHO[0] = NHO[0]+1;
			H1O[NHO[0]] =  P1[JP];
			H2O[NHO[0]] = P2[JP];
//Line250:
		}
		return;
		
	}

/** intersection of a circle with a straight line
 * 
 * @param AV1I [3] circle (coord of 1st 3 vert)
 * @param AV2I [3] 
 * @param SL1I [2] straight line (coord of 1st 2 vert)
 * @param SL2I [2] 
 * @param NHO nr of hits = nr of intersection point
 * @param H1O [2] coord of hit (= intersc) pt
 * @param H2O
 */
public static void ISCISR(double AV1I[],double AV2I[],double SL1I[],double SL2I[],int NHO[],double H1O[],double H2O[],double overlap[])
{
	double dx=AV1I[1];
	double dy=AV2I[1];
	double[] AV1I_={AV1I[0]-dx, AV1I[1]-dx,AV1I[2]-dx,AV1I[3]-dx};
	double[] AV2I_={AV2I[0]-dy, AV2I[1]-dy,AV2I[2]-dy,AV2I[3]-dy};
	double[] SL1I_={SL1I[0]-dx, SL1I[1]-dx,SL1I[2]-dx};
	double[] SL2I_={SL2I[0]-dy, SL2I[1]-dy,SL2I[2]-dy};
	ISCISR_(AV1I_,AV2I_,SL1I_,SL2I_,NHO,H1O,H2O,overlap);
	for(int i=1;i<=NHO[0];i++){
		H1O[i]+=dx;
		H2O[i]+=dy;
	}
	
}
public static void ISCISR_(double AV1I[],double AV2I[],double SL1I[],double SL2I[],int NHO[],double H1O[],double H2O[],double overlap[])
	{
		//double AV1I[] = null,AV2I[]=null; //[3] I-arguments:  mil coord of 1st 3 vert 
		//double SL1I[]=null,SL2I[]=null; //[3] I-arg: mil coord of 2 vertices defi
		//int NHO;		// O-arg: nr of hits = nr of intersection point
		//double H1O[]=null,H2O[]=null; //[2] O-arg: mil coord of hit (= intersc) pt
		// ning the stright line. The 3rd array element
		// is not used but necessary because of call pa


		double ALFA=0.0; // angle between the vectors from circle cente
						// to the end points of the arc (in pseudocos)
		double BETA=0.0; // angle between the vectors from circle cente
						// to the start pt of the arc and the IP (psc)
		double D1=0.0,D2=0.0; // differences, help variables
		double EFP=0.0; 	// distance between foot point F and intsc.pt. 
		double F1=0.0,F2=0.0;	// mil coord of foot pt, mid pt between IPs 
		double G1=0.0,G2=0.0; 	// vector defining the stright line ("Gerade") 
		double GL=0.0;		// length of vector g
		double GLSQ=0.0;	// length of vector g, square
		int J1=0,J2=0;	// indices of DO-loops
		int JH,JP;		// index of intersection points
		int JL;			// index of vertices on stright line segment
		int JV;	    	// indices of vertices on arcs with centers V,
		int NP;
		double P1[]=new double[3],P2[]=new double[3]; //[2]  mil coordinates of intersection points
		double RV=0.0;	// radii of circles with center V and W
		double RVSQ=0.0; // squares of circle radii
		double SCAP=0.0;	// scalar product (v-a,g)
		double SIGNV=0.0;	//sign of circle center (see CTRC3P) 
		double V1=0.0,V2=0.0;	// mil coordinates of circle center V 
		double XVF=0.0;		// distance between V and foot point F 
		double XVFSQ=0.0;	// dist betw V and foot point F, square
		boolean YCHKEX=false;	// check, if new IPs equal existing IPs? Y=.T.

		
		//ENTRY ISCISR (AV1I,AV2I,SL1I,SL2I,NHO,Hl0,H20,MSGIO,Y2IPDO)
		// intersection of a circle with a stright line, R*8 arguments WRITE 
		NHO[0] = 0;

		 
		// 	check if circle and stright line are well defined
		for(J1=1;J1<=2;J1++){
			for(J2=J1+1;J2<=3;J2++){
				if(AV1I[J1]==AV1I[J2] && AV2I[J1]==AV2I[J2]){
					// escape, because definition of V is wrong
					char TYCIR =	'V';
					double PA1 = AV1I[J1];
					double PA2 = AV2I[J1];
					NHO[0]=-1;
					throw new IllegalArgumentException("definition of arc W is wrong "+PA1+", "+PA2);
				}
			}
		}
		if(SL1I[1]==SL1I[2] && SL2I[1] == SL2I[2]){
			// escape, because definition of stright line is wrong
			char TYCIR =   'L';
			double PA1 = SL1I[1];
			double PA2 = SL2I[1];
			NHO[0]=-1;
			throw new IllegalArgumentException("definition of straight L is wrong "+PA1+", "+PA2);
		}

		//check if definition points are equal
		for(JV =   1;JV<=3;JV++){
			for(JL=1;JL<=2;JL++){
				if(AV1I[JV]==SL1I[JL] && AV2I[JV]==SL2I[JL]){
					if(NHO[0]>=2){
						// 	escape with message: 2 identical stright lines
						NHO[0]=3;
						return;
					}
					NHO[0] =  NHO[0] + 1;
					H1O[NHO[0]] =  AV1I[JV];
					H2O[NHO[0]] =  AV2I[JV];
				}
			}
		}


		if(NHO[0]>=2){
			// normal end, all IPs are common definition pts
			return;
		}

		// calculate circle center V 	,
		{
			double Z1O[]=new double[1];
			double Z2O[]=new double[1];
			double DETAO[]=new double[1];
			double SIGNO[]=new double[1];
			CTRC3P (AV1I[1],AV2I[1],AV1I[2],AV2I[2],AV1I[3],AV2I[3],Z1O,Z2O,DETAO,SIGNO);
			V1=Z1O[0];
			V2=Z2O[0];
			SIGNV=SIGNO[0];
			if(DETAO[0]==0){
				throw new IllegalArgumentException("unexpected straight line");
			}
		}
		 
		// 	radius of circle with center V 
		D1 = AV1I[1] - V1;
		D2 = AV2I[1] - V2;
		RVSQ =  D1*D1 + D2*D2;
		RV =  Math.sqrt(RVSQ);
		
		// distance x between circle center V and foot point F 
		G1 =  SL1I[2] - SL1I[1];
		G2 = SL2I[2] - SL2I[1];
		SCAP = (V1- SL1I[1])*G1 +  (V2 - SL2I[1])*G2;
		GLSQ = G1*G1 + G2*G2;
		GL = Math.sqrt(GLSQ);
		F1 = SL1I[1] +  G1*SCAP/GLSQ;
		F2 = SL2I[1] + G2*SCAP/GLSQ;
		D1 = V1 - F1;
		D2 = V2 - F2;
		XVFSQ =  D1*D1 + D2*D2;
		XVF =  Math.sqrt (XVFSQ);

		//DO CASE 	radius compared with distance between v and F
		if(XVF < RV-EPS){
			//CASE 	circle and stright line meet in two different points
			//calculate e and the 2 intersection points
			EFP =   Math.sqrt (RVSQ - XVFSQ);
			NP=2;
			P1[1] = F1 + G1*EFP/GL;
			P2[1] = F2 + G2*EFP/GL;
			P1[2] = F1 - G1*EFP/GL;
			P2[2] = F2 - G2*EFP/GL;
			overlap[0]=RV-XVF;
			YCHKEX =	true;
		}else if(XVF>RV+EPS){
			//<---CASE	circle and stright line don't meet, escape, no IP
			return; 
		}else if(NHO[0] > 0){
			//CASE 	circle and stright line meet tangentially in 1 double pt already known.
			H1O[2] = H1O[1]; 
			H2O[2] = H2O[1];
			overlap[0]=0.0;
			NHO[0] =  1; // CEIS: Nur ein Schnittpunkt
			return;
		}else{
			// CASE 	cir. and stright line meet in 1 "double" point not yet known
			//Line420:
			NP=1; // CEIS: nur ein Schnittpunkt
			P1[1] =  F1;
			P2[1] =  F2;
			P1[2] =  F1;
			P2[2] =  F2;
			overlap[0]=0.0;
			YCHKEX =  false;
		}
		//Line430:

		//compare with already defined IPs and check if inside line segments
		//DO 450 JP =   1,2
		for(JP=1;JP<=NP;JP++){
			if(YCHKEX){
				// compare with already found IPs
				boolean continueJP=false;
				//DO 440 JH =   1,NHO
				for(JH=1;JH<=NHO[0];JH++){
					if(Math.abs(P1[JP] - H1O[JH]) <= EPS100 && Math.abs(P2[JP] - H2O[JH]) <= EPS100){
						// ELSE	new IP equals already existing IP, test next new
						continueJP=true;
						break;
					}
					// THEN	check next already existing IP
				}
				if(continueJP){
					continue;
				}
			}
			//check if the IP lies on the arc defining the intersecting circle
			ALFA =  PSECOS (AV1I[1],AV2I[1],V1,V2,AV1I[3],AV2I[3]);
			BETA= PSECOS (AV1I[1],AV2I[1],V1,V2,P1[JP],P2[JP]); 
			if( ALFA != BETA && Math.signum(BETA-ALFA)!=SIGNV){
				// THEN next DO, IP lies outside the arc with center V
				continue;
			}
			// check if the IP lies on the stright line segment
			if(SL1I[1]!=P1[JP]){
				//THEN 	!.1st coord usable to test
				if(SL1I[1] < SL1I[2]){
					if(P1[JP] < SL1I[1] || SL1I[2]<P1[JP]){
						// next DO, IP lies outside segment
						continue;
					}
				}else{
					if(P1[JP]<SL1I[2]|| SL1I[1]<P1[JP]){
						// next DO, IP   lies outside segment
						continue;
					}
				}
			}else{
				// 2nd coord must be used
				if(SL2I[1]<SL2I[2]){
					if(P2[JP]<SL2I[1] || SL2I[2]<P2[JP]){
						// next DO, IP lies outside segment
						continue;
					}
				}else{
					if(P2[JP]<SL2I[2] || SL2I[1]<P2[JP]){
						// next DO, IP lies outside segment
						continue;
					}
				}
			}
			if(NHO[0]>=2){
				// escape with error message, more than 2 IPs (possible, if EPS,EPS100 to small) 
				throw new IllegalStateException("more than 2 IPs ("+H1O[1]+", "+H2O[1]+"), ("+H1O[2]+", "+H2O[2]+"), ("+P1[JP]+", "+P2[JP]+")");
			}
			NHO[0] = NHO[0] +  1;
			H1O[NHO[0]] = P1[JP]; 
			H2O[NHO[0]] = P2[JP];
		}
//Line450:		CONTINUE 	.
		return;
		
	}
static public void main(String args[])
{ 
	double Z1O[]=new double[1];
	double Z2O[]=new double[1];
	double DETAO[]=new double[1];
	double SIGNO[]=new double[1];
				
	  CTRC3P(0.0,0.0,  1.0,1.0, 2.0,0.0, Z1O,Z2O,DETAO,SIGNO);
	  System.out.println(" "+Z1O[0]+", "+Z2O[0]+", DETA "+DETAO[0]+", SIGN "+SIGNO[0]);
	  CTRC3P(0.0,0.0,  1.0,-1.0, 2.0,0.0, Z1O,Z2O,DETAO,SIGNO);
	  System.out.println(" "+Z1O[0]+", "+Z2O[0]+", DETA "+DETAO[0]+", SIGN "+SIGNO[0]);
	  CTRC3P(0.0,0.0,  1.0,0.0, 2.0,0.0, Z1O,Z2O,DETAO,SIGNO);  // Gerade
	  System.out.println(" "+Z1O[0]+", "+Z2O[0]+", DETA "+DETAO[0]+", SIGN "+SIGNO[0]);
	  CTRC3P(0.0,0.0,  2.0,0.0, 0.0,0.0, Z1O,Z2O,DETAO,SIGNO);  // voller Kreis
	  System.out.println(" "+Z1O[0]+", "+Z2O[0]+", DETA "+DETAO[0]+", SIGN "+SIGNO[0]);
}

}
