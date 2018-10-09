package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

public class Numeric23Test {
	
	// ili names
	private static final String NUMERICINT="numericInt";
	private static final String NUMERICINT2="numericInt2";
	private static final String NUMERICINT3="numericInt3";
	private static final String NUMERICDEC1="numericDec";
	private static final String NUMERICDEC2="numericDec2";
	private static final String NUMERICDEC3="numericDec3";
	
	private TransferDescription td=null;
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/Datatypes23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	// Die kleinste Nummer wird getestet.
	@Test
	public void negative_Min_NoDecimalPlaces_Ok(){
		Iom_jObject objMinLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinLength.setattrvalue(NUMERICINT, "-10");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinLength));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird eine Fehlermeldung erwartet, da der gueltige Bereich unterschritten wird.
	@Test
	public void negative_Min_NoDecimalPlaces_False(){
		Iom_jObject objMinLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinLength.setattrvalue(NUMERICINT, "-11");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinLength));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value -11 is out of range in attribute numericInt", logger.getErrs().get(0).getEventMsg());
	}
	
	// Die groesste Numnern Angabe wird getestet.
	@Test
	public void positive_Max_NoDecimalPlaces_Ok(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue(NUMERICINT, "10");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMaxLength));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird eine Fehlermeldung erwartet, da der gueltige Bereich ueberschritten wird.
	@Test
	public void positive_Max_NoDecimalPlaces_False(){
		Iom_jObject objMinLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinLength.setattrvalue(NUMERICINT, "11");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinLength));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 11 is out of range in attribute numericInt", logger.getErrs().get(0).getEventMsg());
	}
	
	// Die kleinste Dezimale Angabe wird getestet.
	@Test
	public void negative_Min_WithDecimalPlaces_Ok(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue(NUMERICDEC1, "-10.0");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMaxLength));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird eine Fehlermeldung erwartet, da der gueltige Bereich unterschritten wird.
	@Test
	public void negative_Min_WithDecimalPlaces_False(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue(NUMERICDEC1, "-10.1");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMaxLength));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value -10.1 is out of range in attribute numericDec", logger.getErrs().get(0).getEventMsg());
	}
	
	// Die groesste Dezimale Zahl wird getestet.
	@Test
	public void positive_Max_WithDecimalPlaces_Ok(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue(NUMERICDEC1, "10.0");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMaxLength));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird eine Fehlermeldung erwartet, da der gueltige Bereich ueberschritten wird.
	@Test
	public void positive_Max_WithDecimalPlaces_False(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue(NUMERICDEC1, "10.1");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMaxLength));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 10.1 is out of range in attribute numericDec", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird eine Fehlermeldung erwartet, da der eingegebene Wert nicht dem Format entspricht.
	@Test
	public void numericTypeWrongFormatFail(){
		Iom_jObject objWrongFormat=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objWrongFormat.setattrvalue(NUMERICINT, "a");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objWrongFormat));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <a> is not a number", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird eine Fehlermeldung erwartet, da der eingegebene Wert nicht dem Format entspricht.
	@Test
	public void format_Wrong_Fail(){
		Iom_jObject objWrongFormat=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objWrongFormat.setattrvalue(NUMERICDEC1, "a");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objWrongFormat));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <a> is not a number", logger.getErrs().get(0).getEventMsg());
	}

	// prueft, ob 0.0(6) erfolgreich auf 0.1 aufrundet
	// und das Resultat erfolgreich in der precision:1 zurueckgegeben wird.
	@Test
	public void positive_Round_Over2DecimalPlaces_Up_Fail(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICDEC1, "10.06");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 10.1 is out of range in attribute numericDec", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob 4(5) erfolgreich auf 50 aufrundet
	// und das Resultat erfolgreich in der precision:1 zurueckgegeben wird.
	@Test
	public void positive_Round_Over2DecimalPlaces_UpFrom5_Fail(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICDEC1, "10.05");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 10.1 is out of range in attribute numericDec", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob 4(4) erfolgreich auf 40 abrundet
	// und das Resultat erfolgreich in der precision:1 zurueckgegeben wird.
	@Test
	public void positive_Round_Over2DecimalPlaces_Down_Ok(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICDEC1, "10.04");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft, ob die erste Zahl: 1, erfolgreich durch das Runden der Zahlen: 9, zu 2 aufgerundet wird
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void positive_Round_SerevalDecimalPlaces_Up_Fail(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICINT3, "199999.99999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 200000 is out of range in attribute numericInt3", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob die erste Zahl: (minus) -1, erfolgreich durch das Runden der Zahlen: 9, zu (minus) -2 abgerundet wird
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void negative_Round_SerevalDecimalPlaces_Down_Fail(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICINT3, "-199999.99999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value -200000 is out of range in attribute numericInt3", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob die Zahl erfolgreich durch das Aufrunden der Zahlen: 9, den Amount um 1 Zahl erweitert
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void positive_Round_ResultProduceOnePlaceMore_Up_Fail(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICINT2, "9.99999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 10 is out of range in attribute numericInt2", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob die (minus) Zahl erfolgreich durch das Abrunden der Zahlen: 9, den Amount um 1 Zahl erweitert
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void negative_Round_ResultProduceOnePlaceMore_Down_Fail(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICINT2, "-9.99999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value -10 is out of range in attribute numericInt2", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob 0.6 erfolgreich auf 1.0 aufrundet
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void positive_Round_NoDecimalPlaces_Up_Fail(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICINT, "10.6");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 11 is out of range in attribute numericInt", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob 0.4 erfolgreich auf 0.0 abrundet
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void positive_Round_NoDecimalPlaces_Down_Ok(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICINT, "10.4");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft, ob 0.5 erfolgreich auf 1.0 aufrundet
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void positive_Round_NoDecimalPlaces_UpFrom5_Fail(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICINT, "10.5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 11 is out of range in attribute numericInt", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob 0.0016 erfolgreich auf 0.002 aufrundet
	// und das Resultat erfolgreich in der precision:3 zurueckgegeben wird.
	@Test
	public void positive_Round_WithDecimalPlaces_Up_Fail(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICDEC2, "0.0016");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 0.002 is out of range in attribute numericDec2", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob 0.0014 erfolgreich auf 0.001 abrundet
	// und das Resultat erfolgreich in der precision:3 zurueckgegeben wird.
	@Test
	public void positive_Round_WithDecimalPlaces_Down_Ok(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICDEC2, "0.0014");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft, ob 0.0015 erfolgreich auf 0.002 aufrundet
	// und das Resultat erfolgreich in der precision:3 zurueckgegeben wird.
	@Test
	public void positive_Round_WithDecimalPlaces_UpFrom5_Fail(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICDEC2, "0.0015");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 0.002 is out of range in attribute numericDec2", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob (minus) -0.6 erfolgreich auf (minus) -1.0 abrundet
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void negative_Round_NoDecimalPlaces_Down_Fail(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICINT, "-10.6");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value -11 is out of range in attribute numericInt", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob (minus) -0.4 erfolgreich auf (minus) -0.0 aufrundet
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void negative_Round_NoDecimalPlaces_Up_Ok(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICINT, "-10.4");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft, ob (minus) -0.5 erfolgreich auf (minus) -0.0 aufrundet
	// und das Resultat erfolgreich in der precision:0 zurueckgegeben wird.
	@Test
	public void negative_Round_NoDecimalPlaces_UpFrom5_Ok(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICINT, "-10.5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft, ob (minus) -0.0016 erfolgreich auf (minus) -0.002 abrundet
	// und das Resultat erfolgreich in der precision:3 zurueckgegeben wird.
	@Test
	public void negative_Round_WithDecimalPlaces_Down_Fail(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICDEC2, "-0.0016");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value -0.002 is out of range in attribute numericDec2", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob (minus) -0.0014 erfolgreich auf (minus) -0.001 aufrundet
	// und das Resultat erfolgreich in der precision:3 zurueckgegeben wird.
	@Test
	public void negative_Round_WithDecimalPlaces_Up_Ok(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICDEC2, "-0.0014");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft, ob (minus) -0.0015 erfolgreich auf (minus) -0.001 aufrundet
	// und das Resultat erfolgreich in der precision:3 zurueckgegeben wird.
	@Test
	public void negative_Round_WithDecimalPlaces_UpFrom5_Ok(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICDEC2, "-0.0015");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft, ob 0.999999999994 erfolgreich nicht gerundet wird.
	// und das Resultat erfolgreich in der precision:12 zurueckgegeben wird.
	@Test
	public void positive_Round_WithSerevalDecimalPlaces_NotRounded_Ok(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICDEC3, "1.999999999999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft, ob (minus) -0.999999999996 erfolgreich nicht gerundet wird.
	// und das Resultat erfolgreich in der precision:12 zurueckgegeben wird.
	@Test
	public void negative_Round_WithSerevalDecimalPlaces_NotRounded_Ok(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		obj1.setattrvalue(NUMERICDEC3, "-1.999999999999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
}