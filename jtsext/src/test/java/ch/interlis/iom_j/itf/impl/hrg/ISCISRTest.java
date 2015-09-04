package ch.interlis.iom_j.itf.impl.hrg;

import static org.junit.Assert.*;

import org.junit.Test;

public class ISCISRTest {
	static final double EPSILON=0.00000001;
	// Kreis und Gerade ueberlappen sich nicht
	@Test
	public void test1a(){
		double[] AV1I={0.0, 0.0,      5.0,      0.0      };
		double[] AV2I={0.0,      5.0,      0.0,      -5.0};
		double[] AW1I={0.0,20.0,     20.0      };
		double[] AW2I={0.0,      5.0,      -5.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCISR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(0,NHO[0]);
	}
	// Kreis und Gerade ueberlappen sich in einem Punkt
	@Test
	public void test2a(){
		// Schnittpunkt ist bei beiden Abschnitten Definitionspunkt
		double[] AV1I={0.0, 0.0,      5.0,      0.0      };
		double[] AV2I={0.0,      5.0,      0.0,      -5.0};
		double[] AW1I={0.0, 5.0,      5.0      };
		double[] AW2I={0.0,      5.0,     0.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCISR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(1,NHO[0]);
		assertEquals(5.0,H1O[1],EPSILON);
		assertEquals(0.0,H2O[1],EPSILON);
		assertEquals(0.0,OVERLAP[0],EPSILON);
	}
	@Test
	public void test2b(){
		// Schnittpunkt ist bei Kreis auch Definitionspunkt
		double[] AV1I={0.0, 0.0,      5.0,      0.0      };
		double[] AV2I={0.0,      5.0,      0.0,      -5.0};
		double[] AW1I={0.0, 5.0,      5.0      };
		double[] AW2I={0.0,      5.0,     -5.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCISR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(1,NHO[0]);
		assertEquals(5.0,H1O[1],EPSILON);
		assertEquals(0.0,H2O[1],EPSILON);
		assertEquals(0.0,OVERLAP[0],EPSILON);
	}
	@Test
	public void test2c(){
		// Schnittpunkt ist bei Gerade auch Definitionspunkt
		double[] AV1I={0.0, 0.0,      4.0,      0.0      };
		double[] AV2I={0.0,      5.0,      3.0,      -5.0};
		double[] AW1I={0.0, 5.0,      5.0      };
		double[] AW2I={0.0,      5.0,      0.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCISR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(1,NHO[0]);
		assertEquals(5.0,H1O[1],EPSILON);
		assertEquals(0.0,H2O[1],EPSILON);
		assertEquals(0.0,OVERLAP[0],EPSILON);
	}
	@Test
	public void test2d(){
		// Schnittpunkt ist bei beiden Abschnitten kein Definitionspunkt
		double[] AV1I={0.0, 0.0,      4.0,      0.0      };
		double[] AV2I={0.0,      5.0,      3.0,      -5.0};
		double[] AW1I={0.0, 5.0,      5.0      };
		double[] AW2I={0.0,      5.0,     -5.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCISR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(1,NHO[0]);
		assertEquals(5.0,H1O[1],EPSILON);
		assertEquals(0.0,H2O[1],EPSILON);
		assertEquals(0.0,OVERLAP[0],EPSILON);
	}
	// Kreis und Gerade ueberlappen sich in zwei Punkten
	@Test
	public void test3a(){
		// beide Schnittpunkte sind auf beiden Abschnitten und beide Schnittpunkte sind keine Definitionspunkte
		double[] AV1I={0.0, 0.0,      5.0,      0.0      };
		double[] AV2I={0.0,      5.0,      0.0,      -5.0};
		double[] AW1I={0.0, 4.0,      4.0      };
		double[] AW2I={0.0,      5.0,     -5.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCISR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(2,NHO[0]);
		assertEquals(4.0,H1O[1],EPSILON);
		assertEquals(-3.0,H2O[1],EPSILON);
		assertEquals(4.0,H1O[2],EPSILON);
		assertEquals(3.0,H2O[2],EPSILON);
		assertEquals(1.0,OVERLAP[0],EPSILON);
	}
	@Test
	public void test3b(){
		// beide Schnittpunkte sind nicht auf dem Kreisabschnitt und beide Schnittpunkte sind keine Definitionspunkte
		double[] AV1I={0.0, 0.0,      5.0,      0.0      };
		double[] AV2I={0.0,      5.0,      0.0,      -5.0};
		double[] AW1I={0.0, -4.0,      -4.0      };
		double[] AW2I={0.0,      5.0,     -5.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCISR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(0,NHO[0]);
	}
	@Test
	public void test3c(){
		// beide Schnittpunkte sind nicht auf dem Geradenabschnitt und beide Schnittpunkte sind keine Definitionspunkte
		double[] AV1I={0.0, 0.0,      5.0,      0.0      };
		double[] AV2I={0.0,      5.0,      0.0,      -5.0};
		double[] AW1I={0.0, 4.0,      4.0      };
		double[] AW2I={0.0,     10.0,      5.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCISR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(0,NHO[0]);
	}
	@Test
	public void test3d(){
		// ein Schnittpunkt ist nicht auf dem Kreisabschnitt und beide Schnittpunkte sind keine Definitionspunkte
		double[] AV1I={0.0, 0.0,      3.0,      5.0      };
		double[] AV2I={0.0,      5.0,      4.0,       0.0};
		double[] AW1I={0.0, 4.0,      4.0      };
		double[] AW2I={0.0,      5.0,     -5.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCISR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(1,NHO[0]);
		assertEquals(4.0,H1O[1],EPSILON);
		assertEquals(3.0,H2O[1],EPSILON);
		assertEquals(1.0,OVERLAP[0],EPSILON);
	}
	@Test
	public void test3e(){
		// ein Schnittpunkt ist nicht auf dem Geradenabschnitt und beide Schnittpunkte sind keine Definitionspunkte
		double[] AV1I={0.0, 0.0,      5.0,      0.0      };
		double[] AV2I={0.0,      5.0,      0.0,      -5.0};
		double[] AW1I={0.0, 4.0,      4.0      };
		double[] AW2I={0.0,      5.0,      0.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCISR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(1,NHO[0]);
		assertEquals(4.0,H1O[1],EPSILON);
		assertEquals(3.0,H2O[1],EPSILON);
		assertEquals(1.0,OVERLAP[0],EPSILON);
	}
}
