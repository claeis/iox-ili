package ch.interlis.iom_j.itf.impl.hrg;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

public class CTRC3PTest {
	static final double EPSILON=0.00000001;
	@Test
	public void testZentrumRechts(){
		double Z1O[]=new double[1];
		double Z2O[]=new double[1];
		double DETAO[]=new double[1];
		double SIGNO[]=new double[1];
		  HrgUtility.CTRC3P(0.0,0.0,  1.0,1.0, 2.0,0.0, Z1O,Z2O,DETAO,SIGNO);
		  //System.out.println(" "+Z1O[0]+", "+Z2O[0]+", DETA "+DETAO[0]+", SIGN "+SIGNO[0]);
		  assertEquals(1.0,Z1O[0],EPSILON);
		  assertEquals(0.0,Z2O[0],EPSILON);
		  assertEquals(2.0,DETAO[0],EPSILON);
		  assertEquals(1.0,SIGNO[0],EPSILON);
	}
	@Test
	public void testZentrumLinks(){
		double Z1O[]=new double[1];
		double Z2O[]=new double[1];
		double DETAO[]=new double[1];
		double SIGNO[]=new double[1];
		  HrgUtility.CTRC3P(0.0,0.0,  1.0,-1.0, 2.0,0.0, Z1O,Z2O,DETAO,SIGNO);
		  //System.out.println(" "+Z1O[0]+", "+Z2O[0]+", DETA "+DETAO[0]+", SIGN "+SIGNO[0]);
			  assertEquals(1.0,Z1O[0],EPSILON);
			  assertEquals(0.0,Z2O[0],EPSILON);
			  assertEquals(-2.0,DETAO[0],EPSILON);
			  assertEquals(-1.0,SIGNO[0],EPSILON);
	}
	@Test
	public void testGerade(){
		double Z1O[]=new double[1];
		double Z2O[]=new double[1];
		double DETAO[]=new double[1];
		double SIGNO[]=new double[1];
			 HrgUtility.CTRC3P(0.0,0.0,  1.0,0.0, 2.0,0.0, Z1O,Z2O,DETAO,SIGNO);  // Gerade
		  //System.out.println(" "+Z1O[0]+", "+Z2O[0]+", DETA "+DETAO[0]+", SIGN "+SIGNO[0]);
			  assertEquals(1.0,Z1O[0],EPSILON);
			  assertEquals(0.0,Z2O[0],EPSILON);
			  assertEquals(0.0,DETAO[0],EPSILON);
			  assertEquals(0.0,SIGNO[0],EPSILON);
	}
	@Test
	public void testFastGerade(){
		double Z1O[]=new double[1];
		double Z2O[]=new double[1];
		double DETAO[]=new double[1];
		double SIGNO[]=new double[1];
			 HrgUtility.CTRC3P(611770.424, 234251.322, 
				611770.171, 234250.059, 
				611769.918, 234248.796, Z1O,Z2O,DETAO,SIGNO);  // FastGerade
		  //System.out.println(" "+Z1O[0]+", "+Z2O[0]+", DETA "+DETAO[0]+", SIGN "+SIGNO[0]);
			  //assertEquals(1.0,Z1O[0],EPSILON);
			  //assertEquals(0.0,Z2O[0],EPSILON);
			  assertEquals(0.0,DETAO[0],EPSILON);
			  assertEquals(0.0,SIGNO[0],EPSILON);
	}
	@Test
	public void testVollerKreis(){
		double Z1O[]=new double[1];
		double Z2O[]=new double[1];
		double DETAO[]=new double[1];
		double SIGNO[]=new double[1];
			 HrgUtility.CTRC3P(0.0,0.0,  2.0,0.0, 0.0,0.0, Z1O,Z2O,DETAO,SIGNO);  // voller Kreis
		  //System.out.println(" "+Z1O[0]+", "+Z2O[0]+", DETA "+DETAO[0]+", SIGN "+SIGNO[0]);
		  assertEquals(1.0,Z1O[0],EPSILON);
		  assertEquals(0.0,Z2O[0],EPSILON);
		  assertEquals(HrgUtility.TWO_PI,DETAO[0],EPSILON);
		  assertEquals(0.0,SIGNO[0],EPSILON);
		
	}
}
