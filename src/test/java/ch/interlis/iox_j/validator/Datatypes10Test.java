package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

public class Datatypes10Test {

	private TransferDescription td=null;
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili1cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/Datatypes10.ili", FileEntryKind.ILIMODELFILE);
		ili1cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili1cConfig);
		assertNotNull(td);
	}

	//############################################################/
	//########## SUCCESSFUL TESTS ################################/
	//############################################################/
	
	// Die maximale Länge des Textes wird getestet.
	@Test
	public void textMaximum_Ok(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("text", "aabbccddee");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird die minimale Länge des Textes getestet.
	@Test
	public void textMinimumOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("text", "a");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob der Text alle Buchstaben des Alphabetes in klein und gross Schreibung enthalten kann.
	@Test
	public void text2Alphabetica_zA_ZOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("text2", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob die Zahlen 0-9 in dieser Numeric vorkommen können.
	@Test
	public void text2Numeric0_9Ok(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("text2", "0123456789");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob diese Befehl Wörter Auswirkungen auf die Eingabe haben.
	@Test
	public void text2ValOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("text2", "NUL ETX DEL ESC SP");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob diese Zeichen im Text vorkommen können.
	@Test
	public void text2SignsOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("text2", "{|}~`_^][\\@?<=>;:/.-,+*()'&%$#\"!");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob es möglich ist, 2 Wörter zu schreiben.
	@Test
	public void text2WordSeparationOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("text2", "I_am_the_2_Test");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Test ob dieser Zeichensatz im Text vorkommen kann.
	@Test
	public void text2ZeichensatzContentOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("text2", "ÄâäàáæÇçÉêëèéîïìíÑñÖôöòóÜûüùú");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Dezimal 9.9 wird gerundet auf (10.0).
	// 10.4 gerundet = 10. valid.
	// 10.5 gerundet = 11. unvalid.
	@Test
	public void bereichMaximumOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("bereich", "10.4");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Dezimal 0.0 wird gerundet auf (0).
	// -0.4 gerundet =  0. valid.
	// -0.5 gerundet = -1. unvalid.
	@Test
	public void bereichMinimumOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("bereich", "-0.4");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die maximale Eingabe wird getestet.
	@Test
	public void bereich2MaximumOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("bereich2", "10.499");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die minimale Eingabe wird getestet.
	@Test
	public void bereich2MinimumOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("bereich2", "-0.499");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die maximale Aufzählung wird hier getestet.
	@Test
	public void aufzaehlungMaximumOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("aufzaehlung", "mehr.zehn");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die minimale Aufzählung wird getestet.
	@Test
	public void aufzaehlungMinimumOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("aufzaehlung", "null");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob diese Aufzählung in einer Verschachtelung funktioniert.
	@Test
	public void aufzaehlungVerschachtelungOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("aufzaehlungVerschachtelung", "null.eins.zwei.drei.vier.fuenf");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die maximale Länge dieser Numeric wird getetstet.
	@Test
	public void laengeMaximumOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("laenge", "10.4");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die minimale Länge der Numeric wird getestet
	@Test
	public void laengeMinimumOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("laenge", "0.5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// 3 Stellen nach dem Komma werden getestet auf die Zulässigkeit.
	@Test
	public void laenge2ThirdPositionAfterDotOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("laenge2", "1000.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Das Maximum der Fläche wird getestet
	@Test
	public void flaecheMaximumOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("flaeche", "10.4");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Das Minimum der Fläche wird getestet.
	@Test
	public void flaecheMinimumOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("flaeche", "0.5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// 3 Stellen nach dem Komma werden getestet auf die Fläche.
	@Test
	public void flaeche2ThirdPositionAfterDotOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("flaeche2", "99.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Der Maximale Radius wird hier getestet.
	@Test
	public void winkelRadianMaximumOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("radians", "10.4");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Der minimale Radius wird hier getestet.
	@Test
	public void winkelRadianMinimumOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("radians", "0.5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Der maximale grad wird getestet.
	@Test
	public void winkelGradsMaximumOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("grads", "10.4");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die minimale Gradeingabe wird getestet.
	@Test
	public void winkelGradsMinimumOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("grads", "0.5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die maximale degrees Eingabe wird getestet.
	@Test
	public void winkelDegreesMaximumOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("degrees", "10.4");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die minimale Degrees Eingabe wird getestet.
	@Test
	public void winkelDegreesMinimumOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("degrees", "0.5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Das minimale Datum, welches möglich ist, wird getestet.
	@Test
	public void datumLowestYearOk(){
		Iom_jObject objLowestYear=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objLowestYear.setattrvalue("datum", "15821225");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objLowestYear));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die Eingabe des maximalen Datums wird getestet.
	@Test
	public void datumHighestYearOk(){
		Iom_jObject objHighestYear=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestYear.setattrvalue("datum", "29991225");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestYear));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Der minimale Monat Januar wird getestet.
	@Test
	public void datumLowestMonthOk(){
		Iom_jObject objLowestMonth=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objLowestMonth.setattrvalue("datum", "20160125");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objLowestMonth));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die Eingabe des maximales Monats  12 wird getestet.
	@Test
	public void datumHighestMonthOk(){
		Iom_jObject objHighestMonth=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestMonth.setattrvalue("datum", "20161225");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestMonth));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die minimale Eingabe des Tages 01 wird getestet.
	@Test
	public void datumLowestDayOk(){
		Iom_jObject objLowestDay=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objLowestDay.setattrvalue("datum", "20161201");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objLowestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die maximal Eingabe des Tages 31 wird getestet.
	@Test
	public void datumHighestDayOk(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestDay.setattrvalue("datum", "20161231");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Horizontale Lage: links wird getestet.
	@Test
	public void horizAlignmentLeftOk(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestDay.setattrvalue("horizAlignment", "Left");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// horizontale Lage: mittig wird getestet.
	@Test
	public void horizAlignmentCenterOk(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestDay.setattrvalue("horizAlignment", "Center");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Horizontale Lage: rechts wird getestet.
	@Test
	public void horizAlignmentRightOk(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestDay.setattrvalue("horizAlignment", "Right");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die vertikale Lage: oben wird getestet.
	@Test
	public void vertAlignmentTopOk(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestDay.setattrvalue("vertAlignment", "Top");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die vertikale Lage: Cap wird getestet.
	@Test
	public void vertAlignmentCapOk(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestDay.setattrvalue("vertAlignment", "Cap");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die vertikale Lage: half wird getestet.
	@Test
	public void vertAlignmentHalfOk(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestDay.setattrvalue("vertAlignment", "Half");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die vertikale Lage: Base wird getestet.
	@Test
	public void vertAlignmentBaseOk(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestDay.setattrvalue("vertAlignment", "Base");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die vertikale Lage: Bottom wird getestet.
	@Test
	public void vertAlignmentBottomOk(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestDay.setattrvalue("vertAlignment", "Bottom");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die Eingabe von 2d Koordinaten wird getestet.
	@Test
	public void koord2Ok(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestDay.setattrvalue("koord2", "5.55 200.6");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die Koordinate als undefiniert markiert wird getestet.
	@Test
	public void koord2UndefinedOk(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestDay.setattrvalue("koord2", "@");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die Koordinate als 3d Eingabe wird getestet.
	@Test
	public void koord3Ok(){
		Iom_jObject objSuccessFormat=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objSuccessFormat.setattrvalue("koord3", "5.55, 200.6 9999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objSuccessFormat));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die Koordinate in 3d als undefiniert wird gestetet.
	@Test
	public void koord3UndefinedOk(){
		Iom_jObject objSuccessFormat=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objSuccessFormat.setattrvalue("koord3", "@");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objSuccessFormat));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	//############################################################/
	//########## FAILING TESTS ###################################/
	//############################################################/
	
	// Testet die Fehlerausgabe, wenn der Text grösser ist, als zulässig.
	@Test
	public void textGreaterThanMaximumFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("text", "aabbccddeef");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Attribute text is length restricted to 10", logger.getErrs().get(0).getEventMsg());
	}
	
	// Testet die Fehlerausgabe, wenn der Typ: Ztext nicht erkannt wird.
	@Test
	public void textUnknownPropertyFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("Ztext", "a");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("unknown property <Ztext>", logger.getErrs().get(0).getEventMsg());
	}
	
	// testet, die Eingabe von nicht gültigen Zeichen.
	@Test
	public void textContainUnvalidCharactersFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("text", "\n\t");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Attribute text must not contain control characters", logger.getErrs().get(0).getEventMsg());
	}

	// Testet die Eingabe von Buchstaben in einem Bereich für Zahlen.
	@Test
	public void bereichContainLettersFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("bereich", "test");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <test> is not a number", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn die Zahleneingabe grösser ist, als erlaubt.
	@Test
	public void bereichGreaterThanMaximumFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("bereich", "10.5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 10.5 is out of range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn die Zahleneingabe kleiner ist, als definiert wurde.
	@Test
	public void bereichLessThanMinimumFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("bereich", "-0.5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value -0.5 is out of range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn der Bereich (auf 3 Kommastellen gerundet) grösser ist als definiert.
	@Test
	public void bereich2GreaterThanMaximumFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("bereich2", "10.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 10.500 is out of range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt eine Fehlermeldung aus, wenn der Bereich kleiner ist (auf 3 Stellen gerundet), als definiert.
	@Test
	public void bereich2LessThanMinimumFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("bereich2", "-0.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value -0.500 is out of range", logger.getErrs().get(0).getEventMsg());
	}

	// Gibt einen Fehler aus, wenn die Hierarchie nicht richtig eingegeben wurde.
	@Test
	public void aufzaehlungInvalidHierarchicalLevelFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("aufzaehlung", "mehr.mehr.vier");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value mehr.mehr.vier is not a member of the enumeration", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn der Typ afuzaelung nicht richtig geschrieben wurde.
	@Test
	public void aufzaehlungNotTypeOfEnumerationFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("aufzaelung", "eins");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("unknown property <aufzaelung>", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn die Aufzählung nicht in der gleichen Enumeration sich befindet.
	@Test
	public void aufzaehlungNotInSameEnumerationFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("aufzaehlung2Oberauszaehlungen", "meter.ml");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value meter.ml is not a member of the enumeration", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn  die Aufzählung nicht in der selber Enumeratiobn sich befindet. Test 2.
	@Test
	public void aufzaehlungNotInSameEnumeration2Fail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("aufzaehlung2Oberauszaehlungen", "liter.mm");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value liter.mm is not a member of the enumeration", logger.getErrs().get(0).getEventMsg());
	}

	// Gibt einen Fehler aus, wenn die Länge zu hoch ist.
	@Test
	public void laengeToHighFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("laenge", "10.5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 10.5 is out of range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn die Länge zu kurz ist.
	@Test
	public void laengeToLowFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("laenge", "0.4");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 0.4 is out of range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn die Länge nicht numerisch ist.
	@Test
	public void laengeNotValidFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("laenge", "abc");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <abc> is not a number", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn die Eingabe nicht Numerisch ist. Test mit Koordinaten.
	@Test
	public void laengeWith2DezFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("laenge", "2.0 5.2");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <2.0 5.2> is not a number", logger.getErrs().get(0).getEventMsg());
	}

	// Gibt einen Fehler aus, wenn die Flächendefinition zu gross ist.
	@Test
	public void flaecheToHighFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("flaeche", "10.5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 10.5 is out of range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn die Flächendefinition zu klein ist.
	@Test
	public void flaecheToLowFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("flaeche", "0.4");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 0.4 is out of range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn die Fläche nicht numerisch, sondern aus Buchstaben besteht.
	@Test
	public void flaecheNotValidFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("flaeche", "abc");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <abc> is not a number", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn die Fläche nicht numerisch, sondern aus koordinaten besteht aus.
	@Test
	public void FlaecheWith2DezFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("flaeche", "2.0 5.2");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <2.0 5.2> is not a number", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn der Radian grösser ist, als die Definition des Maximums.
	@Test
	public void radiansGreaterThanMaxFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("radians", "10.5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 10.5 is out of range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn der Radian kleiner ist, als erlaubt.
	@Test
	public void radiansLessThanMinFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("radians", "0.4");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 0.4 is out of range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn grads grösser definiert wurde, als erlaubt.
	@Test
	public void gradsGreaterThanMaxFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("grads", "10.5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 10.5 is out of range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn grads kleiner definiert wurde, als erlaubt.
	@Test
	public void gradsLessThanMinFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("grads", "0.4");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 0.4 is out of range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn degrees grösser ist, als erlaubt.
	@Test
	public void degreesGreaterThanMaxFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("degrees", "10.5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 10.5 is out of range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn degrees kleiner ist, als erlaubt.
	@Test
	public void degreesLessThanMinFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("degrees", "0.4");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value 0.4 is out of range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn radian Type: abc keine Nummer ist.
	@Test
	public void winkelTypeNotValidFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("radians", "abc");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <abc> is not a number", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn der Radian keine Nummer ist. Test2
	@Test
	public void winkelNotValidRFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("radians", "1.5 5.2");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <1.5 5.2> is not a number", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn Grads nicht valid ist.
	@Test
	public void winkelNotValidGFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("grads", "1.5 5.2");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <1.5 5.2> is not a number", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn degrees nicht valid ist.
	@Test
	public void winkelNotValidDFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("degrees", "1.5 5.2");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <1.5 5.2> is not a number", logger.getErrs().get(0).getEventMsg());
	}

	// Gibt einen Fehler aus, wenn das Datum des Jahres zu klein ist.
	@Test
	public void datumYearToLowFail(){
		Iom_jObject objYearToLow=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objYearToLow.setattrvalue("datum", "15801225");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objYearToLow));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <15801225> is not in range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn das Datum die maximale Definition übersteigt.
	@Test
	public void datumYearToHighFail(){
		Iom_jObject objYearToHigh=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objYearToHigh.setattrvalue("datum", "30001225");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objYearToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
				assertTrue(logger.getErrs().size()==1);
		assertEquals("value <30001225> is not in range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn das Datum des Monats zu tief ist.
	@Test
	public void datumMonthToLowFail(){
		Iom_jObject objMonthToLow=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objMonthToLow.setattrvalue("datum", "20160025");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objMonthToLow));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <20160025> is not in range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn das Datum des Monats zu hoch ist.
	@Test
	public void datumMonthToHighFail(){
		Iom_jObject objMonthToHigh=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objMonthToHigh.setattrvalue("datum", "20161325");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objMonthToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <20161325> is not in range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn das Datum des Tages zu tief ist.
	@Test
	public void datumDayToLowFail(){
		Iom_jObject objDayToLow=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objDayToLow.setattrvalue("datum", "20161200");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objDayToLow));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <20161200> is not in range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Gibt einen Fehler aus, wenn das Datum des Tages zu hoch ist.
	@Test
	public void datumDayToHighFail(){
		Iom_jObject objDayToHigh=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objDayToHigh.setattrvalue("datum", "20161232");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objDayToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <20161232> is not in range", logger.getErrs().get(0).getEventMsg());
	}
	
	// Datum darf nicht mit Dots getrennt werden, sonst wird ein Fehler ausgegeben.
	@Test
	public void datumFormatWithDotsFail(){
		Iom_jObject objFormatWithDots=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objFormatWithDots.setattrvalue("datum", "2016.12.25");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objFormatWithDots));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <2016.12.25> is not a valid Date", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird ein Fehler ausgegeben, wenn das Datum zu kurz geschrieben wird.
	@Test
	public void datumLengthToShortFail(){
		Iom_jObject objLengthToShort=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objLengthToShort.setattrvalue("datum", "2016125");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objLengthToShort));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <2016125> is not a valid Date", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird ein Fehler ausgegeben, wenn das Datum zu lang geschrieben wurde.
	@Test
	public void datumLengthToHighFail(){
		Iom_jObject objLengthToLong=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objLengthToLong.setattrvalue("datum", "201612251");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objLengthToLong));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <201612251> is not a valid Date", logger.getErrs().get(0).getEventMsg());
	}

	// Es wird ein Fehler ausgegeben, wenn die Horizontale nicht existiert.
	@Test
	public void horizAlignmentNotTypeOfEnumerationFail(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestDay.setattrvalue("horizAlignment", "Top");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value Top is not a member of the enumeration", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird ein Fehler ausgegeben, wenn die Vertikale nicht existiert.
	@Test
	public void vertAlignmentNotTypeOfEnumerationFail(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestDay.setattrvalue("vertAlignment", "Left");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value Left is not a member of the enumeration", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird ein Fehler ausgegeben, wenn die Koordinate eine falsche dimension aufweist.
	@Test
	public void koord2WrongDimensionsFail(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		IomObject coordValue=objHighestDay.addattrobj("koord2", "COORD");
		coordValue.setattrvalue("C1", "5.55");
		coordValue.setattrvalue("C2", "200.6");
		coordValue.setattrvalue("C3", "9999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Wrong COORD structure, C3 not expected", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird ein Fehler ausgegeben, wenn es sich nicht um eine Koordinate handelt.
	@Test
	public void koord2WrongTypeFail(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestDay.setattrvalue("koord2", "5.55 200.6 9999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Es wird ein Fehler ausgegeben, wenn dei Koordinate auf 2d geschrieben wird, obwohl sie eine 3d Definition hat.
	@Test
	public void koord3WrongDimensionsFail(){
		Iom_jObject objSuccessFormat=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		IomObject coordValue=objSuccessFormat.addattrobj("koord3", "COORD");
		coordValue.setattrvalue("C1", "5.55");
		coordValue.setattrvalue("C2", "200.6");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objSuccessFormat));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Wrong COORD structure, C3 expected", logger.getErrs().get(0).getEventMsg());
	}

	// Wenn Type=OFF, werden keine Warnungen oder Fehler ausgegeben.
	@Test
	public void configOFFtextTextIsNotANumberOk(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("grads", "1.5 5.2");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Datatypes10.Topic.Table.grads", ValidationConfig.TYPE,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==0);
	}
	
	// Wenn Type validierung auf OFF steht, aber nur Multplicity auf OFF stehen darf, werden trotzdem Typfehler ausgegeben.
	@Test
	public void configOFFtextTextIsNotANumberFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("grads", "1.5 5.2");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Datatypes10.Topic.Table.grads", ValidationConfig.TYPE,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ALLOW_ONLY_MULTIPLICITY_REDUCTION,ValidationConfig.ON);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <1.5 5.2> is not a number",logger.getErrs().get(0).getEventMsg());
	}
	
	// Eine Warnung wird ausgegeben, wenn Grads keine Nummer ist, wenn config Type eingeschalten wurde und ValidationConfig auf Warning steht.
	@Test
	public void configWARNINGtextTextIsNotANumberFail(){
		Iom_jObject objTest=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objTest.setattrvalue("grads", "1.5 5.2");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Datatypes10.Topic.Table.grads", ValidationConfig.TYPE,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes10.Topic","b1"));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getWarn().size()==1);
		assertEquals("value <1.5 5.2> is not a number", logger.getWarn().get(0).getEventMsg());
	}
}