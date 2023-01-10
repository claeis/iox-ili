package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.TypeAlias;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

public class DateTime24Test {

    private final static String ILI_MODEL="DateTime24";
    private final static String ILI_TOPIC=ILI_MODEL+".TopicA";
    private final static String ILI_CLASS_INLINE=ILI_TOPIC+".ClassInline";
    private final static String ILI_CLASS_DOMAIN=ILI_TOPIC+".ClassDomain";
    private final static String ILI_ATTR_ATTRDATE="attrDate";
    private final static String ILI_ATTR_ATTRDATETIME="attrDateTime";
    private final static String ILI_ATTR_ATTRTIMEOFDAY="attrTimeOfDay";
	private TransferDescription td=null;
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/DateTime24.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	
	// Dies ist ein Muster Datum, welches funktionieren muss.
	@Test
	public void dateValidOk(){
        //AttributeDef attrDate=(AttributeDef) td.getElement(ILI_CLASS_INLINE+"."+ILI_ATTR_ATTRDATE);
        //assertEquals(td.INTERLIS.XmlDate,((TypeAlias)(attrDate.getDomain())).getAliasing());
		Iom_jObject objMinYear=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMinYear.setattrvalue(ILI_ATTR_ATTRDATE, "2017-06-15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMinYear));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Dieses Datum wird ueber Interlis.xmlDate ausgefuehrt.
	@Test
	public void formatXMLDateOk(){
		Iom_jObject objMinYear=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMinYear.setattrvalue(ILI_ATTR_ATTRDATE, "2017-06-15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMinYear));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Das hoechste Jahr wird getetstet.
	@Test
	public void dateMaxYearOk(){
		Iom_jObject objMaxYear=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMaxYear.setattrvalue(ILI_ATTR_ATTRDATE, "2999-01-30");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMaxYear));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die Eingabe des kleinsten Monates wird getestet.
	@Test
	public void dateMinMonthOk(){
		Iom_jObject objMinMonth=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMinMonth.setattrvalue(ILI_ATTR_ATTRDATE, "2016-01-30");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMinMonth));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die Eingabe des groessten Monats wird getestet.
	@Test
	public void dateMaxMonthOk(){
		Iom_jObject objMaxMonth=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMaxMonth.setattrvalue(ILI_ATTR_ATTRDATE, "2016-12-30");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMaxMonth));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Der kleinste Tag eines Datums wird getestet.
	@Test
	public void dateMinDayOk(){
		Iom_jObject objMinDay=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMinDay.setattrvalue(ILI_ATTR_ATTRDATE, "2016-01-01");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMinDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Der groesste Tag eines Datums wird getestet.
	@Test
	public void dateMaxDayOk(){
		Iom_jObject objMaxDay=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMaxDay.setattrvalue(ILI_ATTR_ATTRDATE, "2016-01-31");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMaxDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Die minimale Eingabe der Stunden wird getestet.
	@Test
	public void timeMinHourOk(){
		Iom_jObject objMinHour=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMinHour.setattrvalue(ILI_ATTR_ATTRTIMEOFDAY, "0:30:30.123");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMinHour));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die maximale Eingabe der Stunde wird getestet.
	@Test
	public void timeMaxHourOk(){
		Iom_jObject objMaxHour=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMaxHour.setattrvalue(ILI_ATTR_ATTRTIMEOFDAY, "23:30:30.123");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMaxHour));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	
	// Die kleinste Minuten Zeit Angabe wird getestet.
	@Test
	public void timeMinMinutesOk(){
		Iom_jObject objMinMinute=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMinMinute.setattrvalue(ILI_ATTR_ATTRTIMEOFDAY, "10:0:30.123");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMinMinute));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die hoechste Minuten Zeit Angabe wird getestet.
	@Test
	public void timeMaxMinutesOk(){
		Iom_jObject objMaxMinute=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMaxMinute.setattrvalue(ILI_ATTR_ATTRTIMEOFDAY, "10:59:30.123");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMaxMinute));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die kleinste Sekunden Eingabe einer Zeit wird getestet.
	@Test
	public void timeMinSecondsOk(){
		Iom_jObject objMinSecond=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMinSecond.setattrvalue(ILI_ATTR_ATTRTIMEOFDAY, "10:30:0.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMinSecond));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die groesste Sekunden Eingabe einer Zeit wird getestet.
	@Test
	public void timeMaxSecondsOk(){
		Iom_jObject objMaxSecond=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMaxSecond.setattrvalue(ILI_ATTR_ATTRTIMEOFDAY, "10:30:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMaxSecond));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Die kleinst moeglichste Angabe des Datums und der Zeit des Jahres wird getestet.
	@Test
	public void dateTimeMinYearOk(){
		Iom_jObject objMinYear=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMinYear.setattrvalue(ILI_ATTR_ATTRDATETIME, "1582-5-15T12:30:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMinYear));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die Eingabe der maximalsten Datum und Zeit des Jahres Angabe wird getestet.
	@Test
	public void dateTimeMaxYearOk(){
		Iom_jObject objMaxYear=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMaxYear.setattrvalue(ILI_ATTR_ATTRDATETIME, "2999-5-15T12:30:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMaxYear));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die hoechste Angabe des Monats bei Datum und Zeit wird getestet.
	@Test
	public void dateTimeMaxMonthOk(){
		Iom_jObject objMaxMonth=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMaxMonth.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-12-15T12:30:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMaxMonth));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die kleinste Angabe des Monats bei Datum und Zeit wird getestet.
	@Test
	public void dateTimeMinMonthOk(){
		Iom_jObject objMinMonth=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMinMonth.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-1-15T12:30:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMinMonth));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die groesste Tages Angabe von Datum und Zeit wird getestet.
	@Test
	public void dateTimeMaxDayOk(){
		Iom_jObject objMaxDay=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMaxDay.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-5-31T12:30:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMaxDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die kleinste Tages Angabe von Datum und Zeit wird getestet.
	@Test
	public void dateTimeMinDayOk(){
		Iom_jObject objMinDay=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMinDay.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-5-1T12:30:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMinDay));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die hoechste Angabe der Stunde von Datum und Zeit wird getestet.
	@Test
	public void dateTimeMaxHourOk(){
		Iom_jObject objMaxHour=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMaxHour.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-5-15T23:30:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMaxHour));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die kleinste Angabe der Stunde von Datum und Zeit wird getestet.
	@Test
	public void dateTimeMinHourOk(){
		Iom_jObject objMinHour=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMinHour.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-5-15T0:30:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMinHour));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	
	// Die hoechste Minuten Angabe von Datum und Zeit wird getestet.
	@Test
	public void dateTimeMaxMinuteOk(){
		Iom_jObject objMaxMinute=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMaxMinute.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-5-15T12:59:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMaxMinute));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die niedrigste Minuten Angabe von Datum und Zeit wird getestet.
	@Test
	public void dateTimeMinMinuteOk(){
		Iom_jObject objMinMinute=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMinMinute.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-5-15T12:1:30.500");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMinMinute));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die hoechste Sekunden Angabe von Datum und Zeit wird getestet.
	@Test
	public void dateTimeMaxSecondOk(){
		Iom_jObject objMaxSecond=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMaxSecond.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-5-15T12:30:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMaxSecond));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Die niedrigste Sekunden Angabe von Datum und Zeit wird getestet.
	@Test
	public void dateTimeMinSecondOk(){
		Iom_jObject objMinSecond=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMinSecond.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-5-15T12:30:0.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMinSecond));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Assert
		assertTrue(logger.getErrs().size()==0);
	}


	// Es wird getestet ob das Datum im gueltigen Bereich ist. Minimum Test.
	@Test
	public void dateYearToLowFail(){
		Iom_jObject objYearToLow=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objYearToLow.setattrvalue(ILI_ATTR_ATTRDATE, "1580-02-15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objYearToLow));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("date value <1580-02-15> is not in range in attribute attrDate", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die Eingabe des Jahres in einem gueltigen Bereich ist. Maximaler Test.
	@Test
	public void dateYearToHighFail(){
		Iom_jObject objYearToHigh=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objYearToHigh.setattrvalue(ILI_ATTR_ATTRDATE, "3000-02-15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objYearToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("date value <3000-02-15> is not in range in attribute attrDate", logger.getErrs().get(0).getEventMsg());
	}
	
	// Monat von Datum zu klein.
	@Test
	public void dateMonthToLowFail(){
		Iom_jObject objMonthToLow=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMonthToLow.setattrvalue(ILI_ATTR_ATTRDATE, "2016-00-15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMonthToLow));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("date value <2016-00-15> is not in range in attribute attrDate", logger.getErrs().get(0).getEventMsg());
	}
	
	// Monat von Datum zu gross.
	@Test
	public void dateMonthToHighFail(){
		Iom_jObject objMonthToHigh=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMonthToHigh.setattrvalue(ILI_ATTR_ATTRDATE, "2016-13-15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMonthToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("date value <2016-13-15> is not in range in attribute attrDate", logger.getErrs().get(0).getEventMsg());
	}
	
	// Tag von Datum zu klein.
	@Test
	public void dateDayToLowFail(){
		Iom_jObject objDayToLow=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objDayToLow.setattrvalue(ILI_ATTR_ATTRDATE, "2016-02-00");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objDayToLow));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("date value <2016-02-00> is not in range in attribute attrDate", logger.getErrs().get(0).getEventMsg());
	}
	
	// Tag von Datum zu gross.
	@Test
	public void dateDayToHighFail(){
		Iom_jObject objDayToHigh=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objDayToHigh.setattrvalue(ILI_ATTR_ATTRDATE, "2016-02-32");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objDayToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("date value <2016-02-32> is not in range in attribute attrDate", logger.getErrs().get(0).getEventMsg());
	}
	
	// Datenformat mit Punkten unzulaessig.
	@Test
	public void dateFormatWithDotsFail(){
		Iom_jObject objFormatWithDots=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objFormatWithDots.setattrvalue(ILI_ATTR_ATTRDATE, "2016.02.15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objFormatWithDots));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of date value <2016.02.15> in attribute attrDate", logger.getErrs().get(0).getEventMsg());
	}
	
	// Datenformat mit Slash unzulaessig.
	@Test
	public void dateFormatWithSlashFail(){
		Iom_jObject objFormatSlash=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objFormatSlash.setattrvalue(ILI_ATTR_ATTRDATE, "2016/02/15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objFormatSlash));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of date value <2016/02/15> in attribute attrDate", logger.getErrs().get(0).getEventMsg());
	}
	
	// Eingabe des Datum mit zu kleinem Jahresdatum (jjjj) unzulaessig.
	@Test
	public void dateLengthToShortFail(){
		Iom_jObject objLengthToShort=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objLengthToShort.setattrvalue(ILI_ATTR_ATTRDATE, "216-2-2");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objLengthToShort));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of date value <216-2-2> in attribute attrDate", logger.getErrs().get(0).getEventMsg());
	}

	// Eingabe des Datums zu kurz (jjjj-mm-dd). Eingabe unzulaessig.
	@Test
	public void dateWithinRangeLengthToShortFail(){
		Iom_jObject objLengthToLong=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		// Set Attributes
		objLengthToLong.setattrvalue(ILI_ATTR_ATTRDATE, "2021-9-7");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objLengthToLong));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of date value <2021-9-7> in attribute attrDate", logger.getErrs().get(0).getEventMsg());
	}

	// Eingabe des Datum Jahres zu lang (jjjj). Eingabe unzulaessig.
	@Test
	public void dateLengthToLongFail(){
		Iom_jObject objLengthToLong=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		// Set Attributes
		objLengthToLong.setattrvalue(ILI_ATTR_ATTRDATE, "20016-12-15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objLengthToLong));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of date value <20016-12-15> in attribute attrDate", logger.getErrs().get(0).getEventMsg());
	}

	// Zeitangabe Stunde zu lang. unzulaessig.
	@Test
	public void timeHourToHighFail(){
		Iom_jObject objHourToHigh=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objHourToHigh.setattrvalue(ILI_ATTR_ATTRTIMEOFDAY, "24:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objHourToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("time value <24:59:59.999> is not in range in attribute attrTimeOfDay", logger.getErrs().get(0).getEventMsg());
	}
	
	// Zeitangabe Minute zu gross. Fehler.
	@Test
	public void timeMinuteToHighFail(){
		Iom_jObject objMinuteToHigh=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMinuteToHigh.setattrvalue(ILI_ATTR_ATTRTIMEOFDAY, "23:60:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMinuteToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("time value <23:60:59.999> is not in range in attribute attrTimeOfDay", logger.getErrs().get(0).getEventMsg());
	}
	
	// Zeitangabe Sekunde zu gross. Fehler.
	@Test
	public void timeSecondToHighFail(){
		Iom_jObject objSecondToHigh=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objSecondToHigh.setattrvalue(ILI_ATTR_ATTRTIMEOFDAY, "23:59:60.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objSecondToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("time value <23:59:60.000> is not in range in attribute attrTimeOfDay", logger.getErrs().get(0).getEventMsg());
	}
	
	// Zeitangabe allgemein zu kurz. Fehler.
	@Test
	public void timeLengthToShortFail(){
		Iom_jObject objTimeToShort=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objTimeToShort.setattrvalue(ILI_ATTR_ATTRTIMEOFDAY, "5:5:5.55");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objTimeToShort));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of time value <5:5:5.55> in attribute attrTimeOfDay", logger.getErrs().get(0).getEventMsg());
	}
	
	// Zeitangabe zu lang. Fehler.
	@Test
	public void timeLengthToLongFail(){
		Iom_jObject objTimeToLong=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objTimeToLong.setattrvalue(ILI_ATTR_ATTRTIMEOFDAY, "23:59:59.9990");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objTimeToLong));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of time value <23:59:59.9990> in attribute attrTimeOfDay", logger.getErrs().get(0).getEventMsg());
	}

	// Jahr Format bei DatumZeit zu kurz. Fehler.
	@Test
	public void dateTimeYearToLowFail(){
		Iom_jObject objYearToLow=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objYearToLow.setattrvalue(ILI_ATTR_ATTRDATETIME, "1581-2-29T12:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objYearToLow));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("datetime value <1581-2-29T12:59:59.999> is not in range in attribute attrDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// Jahr Format bie DatumZeit zu lang. Fehler.
	@Test
	public void dateTimeYearToHighFail(){
		Iom_jObject objYearToHigh=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objYearToHigh.setattrvalue(ILI_ATTR_ATTRDATETIME, "3000-2-29T12:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objYearToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("datetime value <3000-2-29T12:59:59.999> is not in range in attribute attrDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// Monat bei DatumZeit zu kurz.
	@Test
	public void dateTimeMonthToLowFail(){
		Iom_jObject objMonthToLow=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMonthToLow.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-0-29T12:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMonthToLow));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("datetime value <2016-0-29T12:59:59.999> is not in range in attribute attrDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit Monats Angabe zu gross.
	@Test
	public void dateTimeMonthToHighFail(){
		Iom_jObject objMonthToHigh=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMonthToHigh.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-13-29T12:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMonthToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("datetime value <2016-13-29T12:59:59.999> is not in range in attribute attrDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit Tages Angabe zu klein.
	@Test
	public void dateTimeDayToLowFail(){
		Iom_jObject objDayToLow=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objDayToLow.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-2-0T12:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objDayToLow));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("datetime value <2016-2-0T12:59:59.999> is not in range in attribute attrDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit Tages Angabe zu gross.
	@Test
	public void dateTimeDayToHighFail(){
		Iom_jObject objDayToHigh=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objDayToHigh.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-2-32T12:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objDayToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("datetime value <2016-2-32T12:59:59.999> is not in range in attribute attrDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit Stunde zu gross.
	@Test
	public void dateTimeHourToHighFail(){
		Iom_jObject objHourToHigh=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objHourToHigh.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-2-29T24:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objHourToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("datetime value <2016-2-29T24:59:59.999> is not in range in attribute attrDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit Minute zu gross.
	@Test
	public void dateTimeMinuteToHighFail(){
		Iom_jObject objMinuteToHigh=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objMinuteToHigh.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-2-29T12:60:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objMinuteToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("datetime value <2016-2-29T12:60:59.999> is not in range in attribute attrDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit Sekunden Angabe zu gross.
	@Test
	public void dateTimeSecondToHighFail(){
		Iom_jObject objSecondToHigh=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objSecondToHigh.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-2-29T12:59:60.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objSecondToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("datetime value <2016-2-29T12:59:60.000> is not in range in attribute attrDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit Laenge zu kurz.
	@Test
	public void dateTimeLengthToShortFail(){
		Iom_jObject objLengthToShort=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objLengthToShort.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-2-2T2:2:2.99");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objLengthToShort));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of datetime value <2016-2-2T2:2:2.99> in attribute attrDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit Laenge zu gross.
	@Test
	public void dateTimeLengthToHighFail(){
		Iom_jObject objLengthToHigh=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objLengthToHigh.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-12-29T12:59:59.9999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objLengthToHigh));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of datetime value <2016-12-29T12:59:59.9999> in attribute attrDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit Format mit Punkten unzulaessig.
	@Test
	public void dateTimeFormatWithDotsFail(){
		Iom_jObject objFormatWithDots=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		objFormatWithDots.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016.2.29T12:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objFormatWithDots));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of datetime value <2016.2.29T12:59:59.999> in attribute attrDateTime", logger.getErrs().get(0).getEventMsg());
	}
	
	// DatumZeit ohne T unzulaessig.
	@Test
	public void dateTimeFormatWithoutTFail() {
		Iom_jObject objFormatWithoutT=new Iom_jObject(ILI_CLASS_INLINE, "o1");
		// Set Attributes
		objFormatWithoutT.setattrvalue(ILI_ATTR_ATTRDATETIME, "2016-2-29V12:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(objFormatWithoutT));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid format of datetime value <2016-2-29V12:59:59.999> in attribute attrDateTime", logger.getErrs().get(0).getEventMsg());
	}

}