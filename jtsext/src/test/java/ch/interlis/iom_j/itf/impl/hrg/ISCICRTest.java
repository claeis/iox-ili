package ch.interlis.iom_j.itf.impl.hrg;

import static org.junit.Assert.*;

import org.junit.Test;

public class ISCICRTest {
	static final double EPSILON=0.00000001;
	// Kreise ueberlappen sich nicht
	@Test
	public void test1(){
		double[] AV1I={0.0, 0.0,      5.0,      0.0      };
		double[] AV2I={0.0,      5.0,      0.0,      -5.0};
		double[] AW1I={0.0,20.0,     15.0,     20.0      };
		double[] AW2I={0.0,      5.0,      0.0,      -5.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCICR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(0,NHO[0]);
	}
	// Kreise ueberlappen sich in einem Punkt
	@Test
	public void test2a(){
		// Schnittpunkt ist auf beiden Kurvenabschnitten ein Definitionspunkt
		double[] AV1I={0.0, 0.0,      5.0,      0.0      };
		double[] AV2I={0.0,      5.0,      0.0,      -5.0};
		double[] AW1I={0.0,10.0,      5.0,     10.0      };
		double[] AW2I={0.0,      5.0,      0.0,      -5.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCICR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(1,NHO[0]);
		assertEquals(5.0,H1O[1],EPSILON);
		assertEquals(0.0,H2O[1],EPSILON);
	}
	@Test
	public void test2b(){
		// Schnittpunkt ist auf beiden Kurvenabschnitten kein Definitionspunkt
		double[] AV1I={0.0, 0.0,      4.0,      0.0      };
		double[] AV2I={0.0,      5.0,      3.0,      -5.0};
		double[] AW1I={0.0,10.0,      6.0,     10.0      };
		double[] AW2I={0.0,      5.0,      3.0,      -5.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCICR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(1,NHO[0]);
		assertEquals(5.0,H1O[1],EPSILON);
		assertEquals(0.0,H2O[1],EPSILON);
	}
	// Kreise ueberlappen sich in zwei Punkten
	@Test
	public void test3a(){
		// beide Schnittpunkte sind auf beiden Kreisenabschnitten
		double[] AV1I={0.0, 0.0,      5.0,      0.0      };
		double[] AV2I={0.0,      5.0,      0.0,      -5.0};
		double[] AW1I={0.0, 8.0,      3.0,      8.0      };
		double[] AW2I={0.0,      5.0,      0.0,      -5.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCICR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(2,NHO[0]);
		assertEquals(4.0,H1O[1],EPSILON);
		assertEquals(-3.0,H2O[1],EPSILON);
		assertEquals(4.0,H1O[2],EPSILON);
		assertEquals(3.0,H2O[2],EPSILON);
	}
	@Test
	public void test3b(){
		// beide Schnittpunkte sind NICHT auf dem 1. Kreisenabschnitt
		double[] AV1I={0.0, 0.0,      -5.0,      0.0      };
		double[] AV2I={0.0,      5.0,      0.0,      -5.0};
		double[] AW1I={0.0, 8.0,      3.0,      8.0      };
		double[] AW2I={0.0,      5.0,      0.0,      -5.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCICR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(0,NHO[0]);
	}
	@Test
	public void test3c(){
		// beide Schnittpunkte sind NICHT auf dem 2. Kreisenabschnitt
		double[] AV1I={0.0, 0.0,      -5.0,      0.0      };
		double[] AV2I={0.0,      5.0,      0.0,      -5.0};
		double[] AW1I={0.0, 8.0,     13.0,      8.0      };
		double[] AW2I={0.0,      5.0,      0.0,      -5.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCICR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(0,NHO[0]);
	}
	@Test
	public void test3d(){
		// ein Schnittpunkt ist NICHT auf dem 1. Kreisenabschnitt
		double[] AV1I={0.0, 5.0,      3.0,      0.0      };
		double[] AV2I={0.0,      0.0,     -4.0,      -5.0};
		double[] AW1I={0.0, 8.0,      3.0,      8.0      };
		double[] AW2I={0.0,      5.0,      0.0,      -5.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCICR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(1,NHO[0]);
		assertEquals(4.0,H1O[1],EPSILON);
		assertEquals(-3.0,H2O[1],EPSILON);
	}
	@Test
	public void test3e(){
		// ein Schnittpunkt ist NICHT auf dem 2. Kreisenabschnitt
		double[] AV1I={0.0, 0.0,      5.0,      0.0      };
		double[] AV2I={0.0,      5.0,      0.0,      -5.0};
		double[] AW1I={0.0, 3.0,      5.0,      8.0      };
		double[] AW2I={0.0,      0.0,     -4.0,      -5.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCICR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(1,NHO[0]);
		assertEquals(4.0,H1O[1],EPSILON);
		assertEquals(-3.0,H2O[1],EPSILON);
	}
	// identische Kreise
	@Test
	public void test4a(){
		// beide Schnittpunkte sind auf beiden Kreisenabschnitten, Endpunkte nicht identisch (2. Abschnitt ist Teil vom 1. Abschnitt)
		double[] AV1I={0.0, 0.0,      5.0,      0.0      };
		double[] AV2I={0.0,      5.0,      0.0,      -5.0};
		double[] AW1I={0.0, 4.0,      5.0,      4.0      };
		double[] AW2I={0.0,      3.0,      0.0,      -3.0};
		int[] NHO=new int[1];
		double[] H1O=new double[3];
		double[] H2O=new double[3];
		double[] OVERLAP=new double[1];
		HrgUtility.ISCICR(AV1I, AV2I, AW1I, AW2I, NHO, H1O, H2O,OVERLAP);
		assertEquals(3,NHO[0]);
		// Keine Resultatpunkte!
	}
	
}
