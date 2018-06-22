package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import org.junit.Test;

public class RoundNumeric23Test {
	// prueft, ob 4(6) erfolgreich auf 50 aufrundet
	// und das Resultat erfolgreich in der precision:1 zurueckgegeben wird.
	@Test
	public void positive_RoundOver2DecimalPlaces_Up(){
		BigDecimal result=Validator.roundNumeric(1, "100000.46");
		assertEquals("100000.5", result.toString());
	}
	
	// prueft, ob 4(5) erfolgreich auf 50 aufrundet
	// und das Resultat erfolgreich in der precision:1 zurueckgegeben wird.
	@Test
	public void positive_RoundOver2DecimalPlaces_UpFrom5(){
		BigDecimal result=Validator.roundNumeric(1, "100000.45");
		assertEquals("100000.5", result.toString());
	}
	
	// prueft, ob 4(4) erfolgreich auf 40 abrundet
	// und das Resultat erfolgreich in der precision:1 zurueckgegeben wird.
	@Test
	public void positive_RoundOver2DecimalPlaces_Down(){
		BigDecimal result=Validator.roundNumeric(1, "100000.44");
		assertEquals("100000.4", result.toString());
	}
	
	// prueft, ob die erste Zahl: 1, erfolgreich durch das Runden der Zahlen: 9, zu 2 aufgerundet wird
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void positive_RoundSerevalDecimalPlaces_Up(){
		BigDecimal result=Validator.roundNumeric(0, "199999.99999");
		assertEquals("200000", result.toString());
	}
	
	// prueft, ob die erste Zahl: (minus) -1, erfolgreich durch das Runden der Zahlen: 9, zu (minus) -2 abgerundet wird
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void negative_RoundSerevalDecimalPlaces_Down(){
		BigDecimal result=Validator.roundNumeric(0, "-199999.99999");
		assertEquals("-200000", result.toString());
	}
	
	// prueft, ob die Zahl erfolgreich durch das Aufrunden der Zahlen: 9, den Amount um 1 Zahl erweitert
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void positive_ResultProduceOnePlaceMore_Up(){
		BigDecimal result=Validator.roundNumeric(0, "999999.99999");
		assertEquals("1000000", result.toString());
	}
	
	// prueft, ob die (minus) Zahl erfolgreich durch das Abrunden der Zahlen: 9, den Amount um 1 Zahl erweitert
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void negative_ResultProduceOnePlaceMore_Down(){
		BigDecimal result=Validator.roundNumeric(0, "-999999.99999");
		assertEquals("-1000000", result.toString());
	}
	
	// prueft, ob 0.6 erfolgreich auf 1.0 aufrundet
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void positive_NoDecimalPlaces_Up(){
		BigDecimal result=Validator.roundNumeric(0, "100000.6");
		assertEquals("100001", result.toString());
	}
	
	// prueft, ob 0.4 erfolgreich auf 0.0 abrundet
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void positive_NoDecimalPlaces_Down(){
		BigDecimal result=Validator.roundNumeric(0, "100000.4");
		assertEquals("100000", result.toString());
	}
	
	// prueft, ob 0.5 erfolgreich auf 1.0 aufrundet
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void positive_NoDecimalPlaces_UpFrom5(){
		BigDecimal result=Validator.roundNumeric(0, "100000.5");
		assertEquals("100001", result.toString());
	}
	
	// prueft, ob 0.0016 erfolgreich auf 0.002 aufrundet
	// und das Resultat erfolgreich in der precision:3 zurueckgegeben wird.
	@Test
	public void positive_WithDecimalPlaces_Up(){
		BigDecimal result=Validator.roundNumeric(3, "100000.0016");
		assertEquals("100000.002", result.toString());
	}
	
	// prueft, ob 0.0014 erfolgreich auf 0.001 abrundet
	// und das Resultat erfolgreich in der precision:3 zurueckgegeben wird.
	@Test
	public void positive_WithDecimalPlaces_Down(){
		BigDecimal result=Validator.roundNumeric(3, "100000.0014");
		assertEquals("100000.001", result.toString());
	}
	
	// prueft, ob 0.0015 erfolgreich auf 0.002 aufrundet
	// und das Resultat erfolgreich in der precision:3 zurueckgegeben wird.
	@Test
	public void positive_WithDecimalPlaces_UpFrom5(){
		BigDecimal result=Validator.roundNumeric(3, "100000.0015");
		assertEquals("100000.002", result.toString());
	}
	
	// prueft, ob (minus) -0.6 erfolgreich auf (minus) -1.0 abrundet
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void negative_NoDecimalPlaces_Down(){
		BigDecimal result=Validator.roundNumeric(0, "-100000.6");
		assertEquals("-100001", result.toString());
	}
	
	// prueft, ob (minus) -0.4 erfolgreich auf (minus) -0.0 aufrundet
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void negative_NoDecimalPlaces_Up(){
		BigDecimal result=Validator.roundNumeric(0, "-100000.4");
		assertEquals("-100000", result.toString());
	}
	
	// prueft, ob (minus) -0.5 erfolgreich auf (minus) -0.0 aufrundet
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void negative_NoDecimalPlaces_UpFrom5(){
		BigDecimal result=Validator.roundNumeric(0, "-100000.5");
		assertEquals("-100000", result.toString());
	}
	
	// prueft, ob (minus) -0.0016 erfolgreich auf (minus) -0.002 abrundet
	// und das Resultat erfolgreich in der precision:3 zurueckgegeben wird.
	@Test
	public void negative_WithDecimalPlaces_Down(){
		BigDecimal result=Validator.roundNumeric(3, "-100000.0016");
		assertEquals("-100000.002", result.toString());
	}
	
	// prueft, ob (minus) -0.0014 erfolgreich auf (minus) -0.001 aufrundet
	// und das Resultat erfolgreich in der precision:3 zurueckgegeben wird.
	@Test
	public void negative_WithDecimalPlaces_Up(){
		BigDecimal result=Validator.roundNumeric(3, "-100000.0014");
		assertEquals("-100000.001", result.toString());
	}
	
	// prueft, ob (minus) -0.0015 erfolgreich auf (minus) -0.001 aufrundet
	// und das Resultat erfolgreich in der precision:3 zurueckgegeben wird.
	@Test
	public void negative_WithDecimalPlaces_UpFrom5(){
		BigDecimal result=Validator.roundNumeric(3, "-100000.0015");
		assertEquals("-100000.001", result.toString());
	}
	
	// prueft, ob 0.999999999994 erfolgreich nicht gerundet wird.
	// und das Resultat erfolgreich in der precision:12 zurueckgegeben wird.
	@Test
	public void positive_WithSerevalDecimalPlaces_NotRounded(){
		BigDecimal result=Validator.roundNumeric(12, "0.999999999994");
		assertEquals("0.999999999994", result.toString());
	}
	
	// prueft, ob (minus) -0.999999999996 erfolgreich nicht gerundet wird.
	// und das Resultat erfolgreich in der precision:12 zurueckgegeben wird.
	@Test
	public void negative_WithSerevalDecimalPlaces_NotRounded(){
		BigDecimal result=Validator.roundNumeric(12, "-0.999999999996");
		assertEquals("-0.999999999996", result.toString());
	}
}