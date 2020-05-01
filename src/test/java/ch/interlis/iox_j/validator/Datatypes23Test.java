package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
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

public class Datatypes23Test {

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
	
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////// SUCCESSFUL Tests ////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////

	// Es wird getestet ob der boolean true eine Fehler gibt.
	@Test
	public void booleanTrueOk(){
		Iom_jObject objTrue=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objTrue.setattrvalue("aBoolean", "true");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objTrue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Kann boolean false eingegeben werden.
	@Test
	public void booleanFalseOk(){
		Iom_jObject objFalse=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objFalse.setattrvalue("aBoolean", "false");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objFalse));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
    @Test
    public void blackboxBinaryOk(){
        Iom_jObject objTrue=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        objTrue.setattrvalue("boxBin", "AAAA");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
        validator.validate(new ObjectEvent(objTrue));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==0);
    }

	// Eine richtige Eingabe einer uuid.
	@Test
	public void uuidExampleOk(){
		Iom_jObject objNormal=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objNormal.setattrvalue("aUuid", "123e4567-e89b-12d3-a456-426655440000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objNormal));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// eine richtige Eingabe der minimalen Laenge einer uuid.
	@Test
	public void uuidMinLengthOk(){
		Iom_jObject objMin=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMin.setattrvalue("aUuid", "00000000-0000-1000-8080-808080808080");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMin));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Eine maximale Eingabe einer uuid.
	@Test
	public void uuidMaxLengthOk(){
		Iom_jObject objMax=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMax.setattrvalue("aUuid", "ffffffff-ffff-1fff-bf7f-7f7f7f7f7f7f");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMax));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Maximale Eingabe der Laenge einer Standardid.
	@Test
	public void standardidMaxLengthOk(){
		Iom_jObject objMax=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMax.setattrvalue("aStandardid", "aaaaabbbbbcccccf");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMax));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Maximale Eingabe einer i32 id.
	@Test
	public void i32idMaxLengthOk(){
		Iom_jObject objMax=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMax.setattrvalue("aI32id", "2147483647");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMax));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Das kleinste Jahr welches noch gueltig ist, wird getestet.
	@Test
	public void dateMinYearOk(){
		Iom_jObject objMinYear=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinYear.setattrvalue("aDate", "1582-1-30");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinYear));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

    @Test
    public void formattedTypeValidOk(){
        Iom_jObject objGeorgianDatum=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        objGeorgianDatum.setattrvalue("gDatum", "2017:01:03");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
        validator.validate(new ObjectEvent(objGeorgianDatum));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==0);
    }
	
	// Dies ist ein Muster Datum, welches funktionieren muss.
	@Test
	public void dateValidOk(){
		Iom_jObject objMinYear=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinYear.setattrvalue("aDate", "2017-06-15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinYear));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Dieses Datum wird ueber Interlis.xmlDate ausgefuehrt.
	@Test
	public void formatXMLDateOk(){
		Iom_jObject objMinYear=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinYear.setattrvalue("anInterlisXMLDateFormat", "2017-06-15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinYear));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Das hoechste Jahr wird getetstet.
	@Test
	public void dateMaxYearOk(){
		Iom_jObject objMaxYear=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxYear.setattrvalue("aDate", "2999-1-30");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMaxYear));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die Eingabe des kleinsten Monates wird getestet.
	@Test
	public void dateMinMonthOk(){
		Iom_jObject objMinMonth=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinMonth.setattrvalue("aDate", "2016-1-30");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinMonth));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die Eingabe des groessten Monats wird getestet.
	@Test
	public void dateMaxMonthOk(){
		Iom_jObject objMaxMonth=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxMonth.setattrvalue("aDate", "2016-12-30");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMaxMonth));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Der kleinste Tag eines Datums wird getestet.
	@Test
	public void dateMinDayOk(){
		Iom_jObject objMinDay=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinDay.setattrvalue("aDate", "2016-1-1");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Der groesste Tag eines Datums wird getestet.
	@Test
	public void dateMaxDayOk(){
		Iom_jObject objMaxDay=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxDay.setattrvalue("aDate", "2016-1-31");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMaxDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Die minimale Eingabe der Stunden wird getestet.
	@Test
	public void timeMinHourOk(){
		Iom_jObject objMinHour=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinHour.setattrvalue("aTime", "0:30:30.123");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinHour));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die maximale Eingabe der Stunde wird getestet.
	@Test
	public void timeMaxHourOk(){
		Iom_jObject objMaxHour=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxHour.setattrvalue("aTime", "23:30:30.123");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMaxHour));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
    @Test
    public void enumerationTypeAllOffTest_OnlyNodeWihtoutSubEnumerationOk(){
        Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        objMaxLength.setattrvalue("aufzaehlungAll", "Werktage");
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
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void enumerationTypeAllOffTest_SubSubSubEnumerationOK(){
        Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        objMaxLength.setattrvalue("aufzaehlungAll", "Werktage.Montag.Busy.FullDay");
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
        assertEquals(0, logger.getErrs().size());
    }
	
	// Die kleinste Minuten Zeit Angabe wird getestet.
	@Test
	public void timeMinMinutesOk(){
		Iom_jObject objMinMinute=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinMinute.setattrvalue("aTime", "10:0:30.123");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinMinute));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die hoechste Minuten Zeit Angabe wird getestet.
	@Test
	public void timeMaxMinutesOk(){
		Iom_jObject objMaxMinute=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxMinute.setattrvalue("aTime", "10:59:30.123");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMaxMinute));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die kleinste Sekunden Eingabe einer Zeit wird getestet.
	@Test
	public void timeMinSecondsOk(){
		Iom_jObject objMinSecond=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinSecond.setattrvalue("aTime", "10:30:0.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinSecond));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die groesste Sekunden Eingabe einer Zeit wird getestet.
	@Test
	public void timeMaxSecondsOk(){
		Iom_jObject objMaxSecond=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxSecond.setattrvalue("aTime", "10:30:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMaxSecond));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Die kleinst moeglichste Angabe des Datums und der Zeit des Jahres wird getestet.
	@Test
	public void dateTimeMinYearOk(){
		Iom_jObject objMinYear=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinYear.setattrvalue("aDateTime", "1582-5-15T12:30:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinYear));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die Eingabe der maximalsten Datum und Zeit des Jahres Angabe wird getestet.
	@Test
	public void dateTimeMaxYearOk(){
		Iom_jObject objMaxYear=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxYear.setattrvalue("aDateTime", "2999-5-15T12:30:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMaxYear));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die hoechste Angabe des Monats bei Datum und Zeit wird getestet.
	@Test
	public void dateTimeMaxMonthOk(){
		Iom_jObject objMaxMonth=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxMonth.setattrvalue("aDateTime", "2016-12-15T12:30:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMaxMonth));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die kleinste Angabe des Monats bei Datum und Zeit wird getestet.
	@Test
	public void dateTimeMinMonthOk(){
		Iom_jObject objMinMonth=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinMonth.setattrvalue("aDateTime", "2016-1-15T12:30:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinMonth));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die groesste Tages Angabe von Datum und Zeit wird getestet.
	@Test
	public void dateTimeMaxDayOk(){
		Iom_jObject objMaxDay=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxDay.setattrvalue("aDateTime", "2016-5-31T12:30:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMaxDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die kleinste Tages Angabe von Datum und Zeit wird getestet.
	@Test
	public void dateTimeMinDayOk(){
		Iom_jObject objMinDay=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinDay.setattrvalue("aDateTime", "2016-5-1T12:30:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die hoechste Angabe der Stunde von Datum und Zeit wird getestet.
	@Test
	public void dateTimeMaxHourOk(){
		Iom_jObject objMaxHour=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxHour.setattrvalue("aDateTime", "2016-5-15T23:30:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMaxHour));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die kleinste Angabe der Stunde von Datum und Zeit wird getestet.
	@Test
	public void dateTimeMinHourOk(){
		Iom_jObject objMinHour=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinHour.setattrvalue("aDateTime", "2016-5-15T0:30:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinHour));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
    @Test
    public void enumerationTypeAllOffTest_SubSubEnumerationOK(){
        Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        objMaxLength.setattrvalue("aufzaehlungAll", "Werktage.Montag.Frei");
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
        assertEquals(0, logger.getErrs().size());
    }
	
	// Die hoechste Minuten Angabe von Datum und Zeit wird getestet.
	@Test
	public void dateTimeMaxMinuteOk(){
		Iom_jObject objMaxMinute=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxMinute.setattrvalue("aDateTime", "2016-5-15T12:59:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMaxMinute));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die niedrigste Minuten Angabe von Datum und Zeit wird getestet.
	@Test
	public void dateTimeMinMinuteOk(){
		Iom_jObject objMinMinute=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinMinute.setattrvalue("aDateTime", "2016-5-15T12:1:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinMinute));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die hoechste Sekunden Angabe von Datum und Zeit wird getestet.
	@Test
	public void dateTimeMaxSecondOk(){
		Iom_jObject objMaxSecond=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxSecond.setattrvalue("aDateTime", "2016-5-15T12:30:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMaxSecond));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die niedrigste Sekunden Angabe von Datum und Zeit wird getestet.
	@Test
	public void dateTimeMinSecondOk(){
		Iom_jObject objMinSecond=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinSecond.setattrvalue("aDateTime", "2016-5-15T12:30:0.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinSecond));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}

	// Das kleinste Element einer Aufzaehlung wird getestet.
	@Test
	public void enumerationTypeMinOk(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("aufzaehlung", "null");
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
	
	// Das erste Element einer Aufzaehlung wird getestet.
	@Test
	public void enumerationTypeEinsOk(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("aufzaehlung", "eins");
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
	
	// Eine Verschachtelung einer Aufzaehlung wird getestet.
	@Test
	public void enumerationTypeVierOk(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("aufzaehlung", "mehr.vier");
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
	
	// Eine Verschachtelung einer Aufzaehlung wird getestet.
	@Test
	public void enumerationTypeDreiOk(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("aufzaehlung", "mehr.zehn");
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
		
	// Eine geordnete Aufzaehlung wird getestet.
	@Test
	public void enumerationTypeCircularOk(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("aufzaehlungOrdered", "unten");
		objMaxLength.setattrvalue("aufzaehlungOrdered", "unten");
		objMaxLength.setattrvalue("aufzaehlungOrdered", "oben");
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
	
	// Eine geordnete Aufzaehlung wird getestet.
	@Test
	public void enumerationTypeOrderedOk(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("aufzaehlungCircular", "Sonntag");
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
	
	// Die Horizontale Ausrichtung links wird getestet.
	@Test
	public void horizAlignmentLeftOk(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objHighestDay.setattrvalue("horizAlignment", "Left");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die horizontale Ausrichtung mitte wird getestet.
	@Test
	public void horizAlignmentCenterOk(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objHighestDay.setattrvalue("horizAlignment", "Center");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die horizontale Ausrichtung rechts wird getestet.
	@Test
	public void horizAlignmentRightOk(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objHighestDay.setattrvalue("horizAlignment", "Right");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Die vertikale Ausrichtung top wird getestet.
	@Test
	public void vertAlignmentTopOk(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objHighestDay.setattrvalue("vertAlignment", "Top");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
    @Test
    public void enumerationTypeAllOffTest_SubEnumerationOK(){
        Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        objMaxLength.setattrvalue("aufzaehlungAll", "Werktage.Montag");
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
        assertEquals(0, logger.getErrs().size());
    }
	
	// Die vertikale Ausrichtung Cap wird getestet.
	@Test
	public void vertAlignmentCapOk(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objHighestDay.setattrvalue("vertAlignment", "Cap");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die vertikale Ausrichtung Half wird getestet.
	@Test
	public void vertAlignmentHalfOk(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objHighestDay.setattrvalue("vertAlignment", "Half");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die vertikale Ausrichtung Base wird getestet.
	@Test
	public void vertAlignmentBaseOk(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objHighestDay.setattrvalue("vertAlignment", "Base");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die vertikale Ausrichtung Bottom wird getestet.
	@Test
	public void vertAlignmentBottomOk(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objHighestDay.setattrvalue("vertAlignment", "Bottom");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Die Limite des Texted wird getestet.
	@Test
	public void textTypeTextLimitedMaxOk(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("textLimited", "aaaaabbbbb");
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
	
	// Die Laenge eines unlimitierten Textes wird mit einer sehr hohen Laenge getestet.
	@Test
	public void textTypeTextUnLimitedMaxOk(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("textUnlimited", ch.ehi.basics.tools.StringUtility.STRING(20000, ' '));
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
	
	// Die Limitte eines mTextes wird getestet.
	@Test
	public void textTypeMTextLimitedMaxOk(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("mtextLimited", "aaaaabbbbb");
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
	
	// Der MText wird mit Zeichenumbruechen getestet.
	@Test
	public void textTypeMTextLimitedSpecialCharacterOk(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("mtextLimited", "\n");
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
	
	// Der MText wird auf die Laenge bei unlimited getestet.
	@Test
	public void textTypeMTextUnLimitedSpecialCharacterOk(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("mtextUnlimited", ch.ehi.basics.tools.StringUtility.STRING(20000, '\n'));
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
	
	// Die maximale Laenge eines Textes wird mit einer grossen Laenge getestet.
	@Test
	public void textTypeNameMaxLengthOk(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("nametext", ch.ehi.basics.tools.StringUtility.STRING(255, 'a'));
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
	
	// Ein Uri Text wird auf die Maximale Laenge getestet.
	@Test
	public void textTypeUriMaxLengthOk(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("uritext", "id:"+ch.ehi.basics.tools.StringUtility.STRING(1020, 'a'));
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

	// eine eindimensionale Koordinate wird getestet.
	@Test
	public void coordType1DOk(){
		Iom_jObject objSuccessFormat=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		IomObject coordValue=objSuccessFormat.addattrobj("scoord", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objSuccessFormat));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die zweidimensionale Koordinate wird getestet.
	@Test
	public void coordType2DOk(){
		Iom_jObject objSuccessFormat=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		IomObject coordValue=objSuccessFormat.addattrobj("lcoord", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objSuccessFormat));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die dreidimensionale Koordinate wird getestet.
	@Test
	public void coordType3DOk(){
		Iom_jObject objSuccessFormat=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		IomObject coordValue=objSuccessFormat.addattrobj("hcoord", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "70000.000");
		coordValue.setattrvalue("C3", "0.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objSuccessFormat));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////// FAILING Tests //////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void formattedTypeValueHasInvalidFormatFail(){
        Iom_jObject objGeorgianDatum=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        objGeorgianDatum.setattrvalue("gDatum", "2017-01-01");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
        validator.validate(new ObjectEvent(objGeorgianDatum));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Attribute <gDatum> has a invalid value <2017-01-01>", logger.getErrs().get(0).getEventMsg());
    }
	
    @Test
    public void formattedTypeValueHasInvalidValueFail(){
        Iom_jObject objGeorgianDatum=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        objGeorgianDatum.setattrvalue("gDatum", "1");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
        validator.validate(new ObjectEvent(objGeorgianDatum));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Attribute <gDatum> has a invalid value <1>", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void formattedTypeValueIsOutOfRangeMinFail(){
        Iom_jObject objGeorgianDatum=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        objGeorgianDatum.setattrvalue("gDatum", "2016:01:01");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
        validator.validate(new ObjectEvent(objGeorgianDatum));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Value <2016:01:01> is a out of range in attribute <gDatum>", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void formattedTypeValueIsOutOfRangeMaxFail(){
        Iom_jObject objGeorgianDatum=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        objGeorgianDatum.setattrvalue("gDatum", "2018:01:01");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
        validator.validate(new ObjectEvent(objGeorgianDatum));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Value <2018:01:01> is a out of range in attribute <gDatum>", logger.getErrs().get(0).getEventMsg());
    }
	
	// Es wird getestet, ob true auch gross geschrieben werden kann.
	@Test
	public void booleanUppercaseFail(){
		Iom_jObject objUppercase=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objUppercase.setattrvalue("aBoolean", "TRUE");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objUppercase));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <TRUE> is not a BOOLEAN in attribute aBoolean", logger.getErrs().get(0).getEventMsg());
	}
	
	// Wenn Value kein Booean ist, wird eine Fehlermeldung ausgegeben.
	@Test
	public void booleanNumberFail(){
		Iom_jObject objNumber=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objNumber.setattrvalue("aBoolean", "8");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objNumber));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <8> is not a BOOLEAN in attribute aBoolean", logger.getErrs().get(0).getEventMsg());
	}
    @Test
    public void blackboxBinaryFail(){
        Iom_jObject objTrue=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        objTrue.setattrvalue("boxBin", "http://");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
        validator.validate(new ObjectEvent(objTrue));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("Attribute <boxBin> has a invalid value <http://>", logger.getErrs().get(0).getEventMsg());
    }

	// Es wird getestet ob die horizontale Ansicht: top beinhaltet.
	@Test
	public void horizAlignmentNotTypeOfEnumerationFail(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objHighestDay.setattrvalue("horizAlignment", "Top");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value Top is not a member of the enumeration in attribute horizAlignment", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob left bei einer vertikalen Ansicht eingegeben werden kann.
	@Test
	public void vertAlignmentNotTypeOfEnumerationFail(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objHighestDay.setattrvalue("vertAlignment", "Left");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objHighestDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value Left is not a member of the enumeration in attribute vertAlignment", logger.getErrs().get(0).getEventMsg());
	}

	// Es wird getestet ob die ungueltige Eingabe einer uuid mit der richtigen Laenge eingegeben werden kann.
	@Test
	public void uuidNotAllowedCharFail(){
		Iom_jObject objNotAllowedChar=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objNotAllowedChar.setattrvalue("aUuid", "123e4567-e89b-12d3-z456-426655440000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objNotAllowedChar));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <123e4567-e89b-12d3-z456-426655440000> is not a valid UUID in attribute aUuid", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob eine uuid welche zu kurz ist, einen Fehler ausgibt.
	@Test
	public void uuidLengthToShortFail(){
		Iom_jObject objLengthToShort=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objLengthToShort.setattrvalue("aUuid", "123e4567-e89b-12d3-b456-42665544000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objLengthToShort));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <123e4567-e89b-12d3-b456-42665544000> is not a valid UUID in attribute aUuid", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob eine zu lange Eingabe einer uuid zu einem Fehler fuehrt.
	@Test
	public void uuidLengthToLongFail(){
		Iom_jObject objLengthToLong=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		// Set Attributes
		objLengthToLong.setattrvalue("aUuid", "123e4567-e89b-12d3-b456-4266554400000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objLengthToLong));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <123e4567-e89b-12d3-b456-4266554400000> is not a valid UUID in attribute aUuid", logger.getErrs().get(0).getEventMsg());
	}

	// Es wird getestet ob das Datum im gueltigen Bereich ist. Minimum Test.
	@Test
	public void dateYearToLowFail(){
		Iom_jObject objYearToLow=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objYearToLow.setattrvalue("aDate", "1580-2-15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objYearToLow));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("date value <1580-2-15> is not in range in attribute aDate", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die Eingabe des Jahres in einem gueltigen Bereich ist. Maximaler Test.
	@Test
	public void dateYearToHighFail(){
		Iom_jObject objYearToHigh=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objYearToHigh.setattrvalue("aDate", "3000-2-15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objYearToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("date value <3000-2-15> is not in range in attribute aDate", logger.getErrs().get(0).getEventMsg());
	}
	
	// Monat von Datum zu klein.
	@Test
	public void dateMonthToLowFail(){
		Iom_jObject objMonthToLow=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMonthToLow.setattrvalue("aDate", "2016-0-15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMonthToLow));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("date value <2016-0-15> is not in range in attribute aDate", logger.getErrs().get(0).getEventMsg());
	}
	
	// Monat von Datum zu gross.
	@Test
	public void dateMonthToHighFail(){
		Iom_jObject objMonthToHigh=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMonthToHigh.setattrvalue("aDate", "2016-13-15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMonthToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("date value <2016-13-15> is not in range in attribute aDate", logger.getErrs().get(0).getEventMsg());
	}
	
	// Tag von Datum zu klein.
	@Test
	public void dateDayToLowFail(){
		Iom_jObject objDayToLow=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objDayToLow.setattrvalue("aDate", "2016-2-0");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objDayToLow));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("date value <2016-2-0> is not in range in attribute aDate", logger.getErrs().get(0).getEventMsg());
	}
	
	// Tag von Datum zu gross.
	@Test
	public void dateDayToHighFail(){
		Iom_jObject objDayToHigh=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objDayToHigh.setattrvalue("aDate", "2016-2-32");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objDayToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("date value <2016-2-32> is not in range in attribute aDate", logger.getErrs().get(0).getEventMsg());
	}
	
	// Datenformat mit Punkten unzulaessig.
	@Test
	public void dateFormatWithDotsFail(){
		Iom_jObject objFormatWithDots=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objFormatWithDots.setattrvalue("aDate", "2016.2.15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objFormatWithDots));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of date value <2016.2.15> in attribute aDate", logger.getErrs().get(0).getEventMsg());
	}
	
	// Datenformat mit Slash unzulaessig.
	@Test
	public void dateFormatWithSlashFail(){
		Iom_jObject objFormatSlash=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objFormatSlash.setattrvalue("aDate", "2016/2/15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objFormatSlash));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of date value <2016/2/15> in attribute aDate", logger.getErrs().get(0).getEventMsg());
	}
	
	// Eingabe des Datum mit zu kleinem Jahresdatum (jjjj) unzulaessig.
	@Test
	public void dateLengthToShortFail(){
		Iom_jObject objLengthToShort=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objLengthToShort.setattrvalue("aDate", "216-2-2");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objLengthToShort));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("date value <216-2-2> is not in range in attribute aDate", logger.getErrs().get(0).getEventMsg());
	}
	
	// Eingabe des Datum Jahres zu lang (jjjj). Eingabe unzulaessig.
	@Test
	public void dateLengthToLongFail(){
		Iom_jObject objLengthToLong=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		// Set Attributes
		objLengthToLong.setattrvalue("aDate", "20016-12-15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objLengthToLong));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("date value <20016-12-15> is not in range in attribute aDate", logger.getErrs().get(0).getEventMsg());
	}

	// Zeitangabe Stunde zu lang. unzulaessig.
	@Test
	public void timeHourToHighFail(){
		Iom_jObject objHourToHigh=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objHourToHigh.setattrvalue("aTime", "24:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objHourToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("time value <24:59:59.999> is not in range in attribute aTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// Zeitangabe Minute zu gross. Fehler.
	@Test
	public void timeMinuteToHighFail(){
		Iom_jObject objMinuteToHigh=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinuteToHigh.setattrvalue("aTime", "23:60:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinuteToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("time value <23:60:59.999> is not in range in attribute aTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// Zeitangabe Sekunde zu gross. Fehler.
	@Test
	public void timeSecondToHighFail(){
		Iom_jObject objSecondToHigh=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objSecondToHigh.setattrvalue("aTime", "23:59:60.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objSecondToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("time value <23:59:60.000> is not in range in attribute aTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// Zeitangabe allgemein zu kurz. Fehler.
	@Test
	public void timeLengthToShortFail(){
		Iom_jObject objTimeToShort=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objTimeToShort.setattrvalue("aTime", "5:5:5.55");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objTimeToShort));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of time value <5:5:5.55> in attribute aTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// Zeitangabe zu lang. Fehler.
	@Test
	public void timeLengthToLongFail(){
		Iom_jObject objTimeToLong=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objTimeToLong.setattrvalue("aTime", "23:59:59.9990");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objTimeToLong));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of time value <23:59:59.9990> in attribute aTime", logger.getErrs().get(0).getEventMsg());
	}

	// Dieses Datum wird ueber Interlis.xmlDate ausgefuehrt und ist kleiner als der definierte Bereich.
	@Test
	public void formatDateToSmallFail(){
		Iom_jObject objMinYear=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinYear.setattrvalue("anInterlisXMLDateFormat", "2000-06-15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinYear));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("date value <2000-06-15> is not in range in attribute anInterlisXMLDateFormat", logger.getErrs().get(0).getEventMsg());
	}
	
	// Jahr Format bei DatumZeit zu kurz. Fehler.
	@Test
	public void dateTimeYearToLowFail(){
		Iom_jObject objYearToLow=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objYearToLow.setattrvalue("aDateTime", "1581-2-29T12:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objYearToLow));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("datetime value <1581-2-29T12:59:59.999> is not in range in attribute aDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// Jahr Format bie DatumZeit zu lang. Fehler.
	@Test
	public void dateTimeYearToHighFail(){
		Iom_jObject objYearToHigh=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objYearToHigh.setattrvalue("aDateTime", "3000-2-29T12:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objYearToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("datetime value <3000-2-29T12:59:59.999> is not in range in attribute aDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// Monat bei DatumZeit zu kurz.
	@Test
	public void dateTimeMonthToLowFail(){
		Iom_jObject objMonthToLow=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMonthToLow.setattrvalue("aDateTime", "2016-0-29T12:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMonthToLow));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("datetime value <2016-0-29T12:59:59.999> is not in range in attribute aDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit Monats Angabe zu gross.
	@Test
	public void dateTimeMonthToHighFail(){
		Iom_jObject objMonthToHigh=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMonthToHigh.setattrvalue("aDateTime", "2016-13-29T12:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMonthToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("datetime value <2016-13-29T12:59:59.999> is not in range in attribute aDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit Tages Angabe zu klein.
	@Test
	public void dateTimeDayToLowFail(){
		Iom_jObject objDayToLow=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objDayToLow.setattrvalue("aDateTime", "2016-2-0T12:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objDayToLow));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("datetime value <2016-2-0T12:59:59.999> is not in range in attribute aDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit Tages Angabe zu gross.
	@Test
	public void dateTimeDayToHighFail(){
		Iom_jObject objDayToHigh=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objDayToHigh.setattrvalue("aDateTime", "2016-2-32T12:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objDayToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("datetime value <2016-2-32T12:59:59.999> is not in range in attribute aDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit Stunde zu gross.
	@Test
	public void dateTimeHourToHighFail(){
		Iom_jObject objHourToHigh=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objHourToHigh.setattrvalue("aDateTime", "2016-2-29T24:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objHourToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("datetime value <2016-2-29T24:59:59.999> is not in range in attribute aDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit Minute zu gross.
	@Test
	public void dateTimeMinuteToHighFail(){
		Iom_jObject objMinuteToHigh=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMinuteToHigh.setattrvalue("aDateTime", "2016-2-29T12:60:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objMinuteToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("datetime value <2016-2-29T12:60:59.999> is not in range in attribute aDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit Sekunden Angabe zu gross.
	@Test
	public void dateTimeSecondToHighFail(){
		Iom_jObject objSecondToHigh=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objSecondToHigh.setattrvalue("aDateTime", "2016-2-29T12:59:60.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objSecondToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("datetime value <2016-2-29T12:59:60.000> is not in range in attribute aDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit Laenge zu kurz.
	@Test
	public void dateTimeLengthToShortFail(){
		Iom_jObject objLengthToShort=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objLengthToShort.setattrvalue("aDateTime", "2016-2-2T2:2:2.99");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objLengthToShort));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of datetime value <2016-2-2T2:2:2.99> in attribute aDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit Laenge zu gross.
	@Test
	public void dateTimeLengthToHighFail(){
		Iom_jObject objLengthToHigh=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objLengthToHigh.setattrvalue("aDateTime", "2016-12-29T12:59:59.9999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objLengthToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of datetime value <2016-12-29T12:59:59.9999> in attribute aDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit Format mit Punkten unzulaessig.
	@Test
	public void dateTimeFormatWithDotsFail(){
		Iom_jObject objFormatWithDots=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objFormatWithDots.setattrvalue("aDateTime", "2016.2.29T12:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objFormatWithDots));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of datetime value <2016.2.29T12:59:59.999> in attribute aDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit ohne T unzulaessig.
	@Test
	public void dateTimeFormatWithoutTFail() {
		Iom_jObject objFormatWithoutT=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		// Set Attributes
		objFormatWithoutT.setattrvalue("aDateTime", "2016-2-29V12:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objFormatWithoutT));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of datetime value <2016-2-29V12:59:59.999> in attribute aDateTime", logger.getErrs().get(0).getEventMsg());
	}

	// Es wird die Eingabe einer ungueltigen Sub Value getestet.
	@Test
	public void enumerationTypeWrongSubValueFail(){
		Iom_jObject objWrongFormat=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objWrongFormat.setattrvalue("aufzaehlung", "mehr.elf");
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
		assertEquals("value mehr.elf is not a member of the enumeration in attribute aufzaehlung", logger.getErrs().get(0).getEventMsg());
	}
	
	// Ungueltige Eingabe einer Aufzaehlung.
	@Test
	public void enumerationTypeWrongValueFail(){
		Iom_jObject objWrongFormat=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objWrongFormat.setattrvalue("aufzaehlung", "5");
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
		assertEquals("value 5 is not a member of the enumeration in attribute aufzaehlung", logger.getErrs().get(0).getEventMsg());
	}

	// Test ob die Laenge des Texted bei textLimited begraenzt ist und einen Fehler ausgibt.
	@Test
	public void textTypeTextLimitedToLongFail(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("textLimited", "aaaaabbbbbc");
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
		assertEquals("Attribute textLimited is length restricted to 10", logger.getErrs().get(0).getEventMsg());
	}
	
	// Wird ein Fehler ausgegeben wenn bei einem limitierten Text eine neue Zeile gesetzt wird.
	@Test
	public void textTypeTextLimitedWrongFormatFail(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("textLimited", "\n");
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
		assertEquals("Attribute textLimited must not contain control characters", logger.getErrs().get(0).getEventMsg());
	}
	
	// bei unlimited Text muss es Buchstaben enthalten, sonst ist das Format leider falsche.
	@Test
	public void textTypeTextUnLimitedWrongFormatFail(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("textUnlimited", "\n");
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
		assertEquals("Attribute textUnlimited must not contain control characters", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die Laenge des mTextedLimited bei einer zu langen Eingabe einen Fehler ausgibt.
	@Test
	public void textTypeMTextLimitedToHighFail(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("mtextLimited", "aaaaabbbbbc");
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
		assertEquals("Attribute mtextLimited is length restricted to 10", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob eine Fehlermeldung auftritt, wenn der Text zu lang ist.
	@Test
	public void textTypeNameLengthToHighFail(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("nametext", ch.ehi.basics.tools.StringUtility.STRING(256, 'a'));
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
		assertEquals("Attribute nametext is length restricted to 255", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob bei uriText die maximale Laenge 1024 nicht ueberschreitet. Dabei ist 0, die erste Nummer.
	@Test
	public void textTypeUriLengthToHighFail(){
		Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objMaxLength.setattrvalue("uritext", ch.ehi.basics.tools.StringUtility.STRING(1024, 'a'));
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
		assertTrue(logger.getErrs().size()>=1);
		assertEquals("Attribute uritext is length restricted to 1023", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void coordType2DRangeFail(){
		Iom_jObject obj1=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		IomObject coordValue=obj1.addattrobj("lcoord", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "10000.000");
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
		assertEquals("value 10000.000 is out of range in attribute lcoord", logger.getErrs().get(0).getEventMsg());
	}
	// Es wird ein Fehler ausgegeben, weil die Koordinate nicht 4 Dimensional sein darf.
	@Test
	public void coordType4DimensionsFail(){
		Iom_jObject objWrongFormat=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		IomObject coordValue=objWrongFormat.addattrobj("hcoord", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "70000.000");
		coordValue.setattrvalue("C3", "6.000");
		coordValue.setattrvalue("C4", "2.000");
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
		assertEquals("Wrong COORD structure, unknown property <C4>", logger.getErrs().get(0).getEventMsg());
	}
	
	// Die dreidimensionale Koordinate muss aus einem c1,c2 und einem c3 bestehen, sonst ist die 3d Koordinate zu zweidimensional. 
	@Test
	public void coordType3DMissingC3Fail(){
		Iom_jObject objWrongFormat=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		IomObject coordValue=objWrongFormat.addattrobj("hcoord", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "70000.000");
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
		assertEquals("Wrong COORD structure, C3 expected", logger.getErrs().get(0).getEventMsg());
	}
	
	// Bei Config ON muss ein Fehler ausgegeben werden.
	@Test
	public void booleanTestConfigONFail(){
		Iom_jObject objTrue=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objTrue.setattrvalue("aBoolean", "undecided");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objTrue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <undecided> is not a BOOLEAN in attribute aBoolean", logger.getErrs().get(0).getEventMsg());
	}
	
	// eine Fehlermeldung wird erwartet, da 9(4) auf 90 abgerundet werden soll,
	// und somit der gueltige Bereich unterschritten wird.
	@Test
	public void coordType_Rounding_Down_Fail(){
		Iom_jObject objWrongFormat=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		IomObject coordValue=objWrongFormat.addattrobj("lcoord", "COORD");
		coordValue.setattrvalue("C1", "479999.9994");
		coordValue.setattrvalue("C2", "70000.000");
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
		assertEquals("value 479999.999 is out of range in attribute lcoord", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft, ob 9(5) erfolgreich auf 100 aufgerundet wird.
	@Test
	public void coordType_Rounding_UpFrom5_Ok(){
		Iom_jObject objWrongFormat=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		IomObject coordValue=objWrongFormat.addattrobj("lcoord", "COORD");
		coordValue.setattrvalue("C1", "479999.9995");
		coordValue.setattrvalue("C2", "70000.000");
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
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft, ob 9(6) erfolgreich auf 100 aufgerundet wird.
	@Test
	public void coordType_Rounding_Up_Ok(){
		Iom_jObject objWrongFormat=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		IomObject coordValue=objWrongFormat.addattrobj("lcoord", "COORD");
		coordValue.setattrvalue("C1", "479999.9996");
		coordValue.setattrvalue("C2", "70000.000");
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
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Eine gueltige Name mit der maximalen Anzahl an Zeichen wird erstellt.
	@Test
	public void nameTypeIsValidOk(){
		Iom_jObject iomObj=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		iomObj.setattrvalue("nametext", ch.ehi.basics.tools.StringUtility.STRING(255, 'a'));
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die Gueltigkeit einer NAME wird auf einen leeren Wert getestet.
	@Test
	public void nameTypeIsEmptyFail(){
		Iom_jObject iomObj=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		iomObj.setattrvalue("nametext", "");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of INTERLIS.NAME value <> in attribute nametext", logger.getErrs().get(0).getEventMsg());
	}
	
	// Das erste Zeichen der Value ist kein Buchstabe. Somit soll eine Fehlermeldung ausgegeben werden.
	@Test
	public void nameTypeValueStartsWithNumberFail(){
		Iom_jObject iomObj=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		iomObj.setattrvalue("nametext", "5NameText");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of INTERLIS.NAME value <5NameText> in attribute nametext", logger.getErrs().get(0).getEventMsg());
	}
	
	// Der Wert beinhaltet das gueltige Zeichen underline '_'.
	@Test
	public void nameTypeValueContainsValidSpecialCharOk(){
		Iom_jObject iomObj=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		iomObj.setattrvalue("nametext", "Name_Text");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Der Wert beinhaltet ein ungueltiges Zeichen '-'. Somit soll eine Fehlermeldung ausgegeben werden.
	@Test
	public void nameTypeValueContainsInvalidSpecialCharsFail(){
		Iom_jObject iomObj=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		iomObj.setattrvalue("nametext", "Name-Text");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of INTERLIS.NAME value <Name-Text> in attribute nametext", logger.getErrs().get(0).getEventMsg());
	}
	
	// Der Wert ist ein KeyWord. Somit soll eine Fehlermeldung ausgegeben werden.
	@Test
	public void nameTypeValueContainsKeyWordFail(){
		Iom_jObject iomObj=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		iomObj.setattrvalue("nametext", "ANYSTRUCTURE");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <ANYSTRUCTURE> is a keyword in attribute nametext", logger.getErrs().get(0).getEventMsg());
	}
	
    // Eine gueltige URI wird erstellt.
    @Test
    public void uriTypeIsValidOk(){
        Iom_jObject iomObj=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        iomObj.setattrvalue("uritext", "mailto:ce@localhost");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
        validator.validate(new ObjectEvent(iomObj));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==0);
    }
    
    // Eine ungueltige URI wird getestet.
    @Test
    public void uriTypeValueFail(){
        Iom_jObject iomObj=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        iomObj.setattrvalue("uritext", "ce@localhost");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
        validator.validate(new ObjectEvent(iomObj));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("invalid format of INTERLIS.URI value <ce@localhost> in attribute uritext", logger.getErrs().get(0).getEventMsg());
    }
    
    // Die Gueltigkeit einer URI wird auf einen leeren Wert getestet.
    @Test
    public void uriTypeIsEmptyFail(){
        Iom_jObject iomObj=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        iomObj.setattrvalue("uritext", "");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
        validator.validate(new ObjectEvent(iomObj));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("invalid format of INTERLIS.URI value <> in attribute uritext", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void enumerationTypeOnlyMehrFail(){
        Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        objMaxLength.setattrvalue("aufzaehlung", "mehr");
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
        assertEquals(1, logger.getErrs().size());
        assertEquals("value mehr is not a member of the enumeration in attribute aufzaehlung", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void enumerationTypeOnlyZehnFail(){
        Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        objMaxLength.setattrvalue("aufzaehlung", "zehn");
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
        assertEquals(1, logger.getErrs().size());
        assertEquals("value zehn is not a member of the enumeration in attribute aufzaehlung", logger.getErrs().get(0).getEventMsg());
    }
        
    @Test
    public void enumerationTypeAllOffTest_SubSubSubEnumerationFail(){
        Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        objMaxLength.setattrvalue("aufzaehlungAll", "Montag.Busy.FullDay");
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
        assertEquals(1, logger.getErrs().size());
        assertEquals("value Montag.Busy.FullDay is not a member of the enumeration in attribute aufzaehlungAll", logger.getErrs().get(0).getEventMsg());
    }
        
    @Test
    public void enumerationTypeAllOffTest_SubSubEnumerationFail(){
        Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        objMaxLength.setattrvalue("aufzaehlungAll", "Frei");
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
        assertEquals(1, logger.getErrs().size());
        assertEquals("value Frei is not a member of the enumeration in attribute aufzaehlungAll", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void enumerationTypeAllOffTest_SubEnumerationFail(){
        Iom_jObject objMaxLength=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        objMaxLength.setattrvalue("aufzaehlungAll", "Montag");
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
        assertEquals(1, logger.getErrs().size());
        assertEquals("value Montag is not a member of the enumeration in attribute aufzaehlungAll", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void coordType_notAcoord_Fail(){
        Iom_jObject objSuccessFormat=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
        objSuccessFormat.setattrvalue("lcoord", "5");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
        validator.validate(new ObjectEvent(objSuccessFormat));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("The value <5> is not a Coord in attribute lcoord", logger.getErrs().get(0).getEventMsg());
    }
}