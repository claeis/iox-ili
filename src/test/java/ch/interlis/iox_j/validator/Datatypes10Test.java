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
import ch.interlis.iox_j.ObjectEvent;
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

	// SUCCESSFUL Tests.
	// These Tests which follow were successful.
	
	@Test
	public void datumLowestYear(){
		Iom_jObject objLowestYear=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objLowestYear.setattrvalue("datum", "15821225");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new ObjectEvent(objLowestYear));
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void datumHighestYear(){
		Iom_jObject objHighestYear=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestYear.setattrvalue("datum", "29991225");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new ObjectEvent(objHighestYear));
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void datumLowestMonth(){
		Iom_jObject objLowestMonth=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objLowestMonth.setattrvalue("datum", "20160125");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new ObjectEvent(objLowestMonth));
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void datumHighestMonth(){
		Iom_jObject objHighestMonth=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestMonth.setattrvalue("datum", "20161225");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new ObjectEvent(objHighestMonth));
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void datumLowestDay(){
		Iom_jObject objLowestDay=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objLowestDay.setattrvalue("datum", "20161201");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new ObjectEvent(objLowestDay));
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void datumHighestDay(){
		Iom_jObject objHighestDay=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objHighestDay.setattrvalue("datum", "20161231");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new ObjectEvent(objHighestDay));
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// FAILING Tests.
	// The following Tests which are given, they throw 1 Error/Test.
	
	@Test
	public void datumYearToLow(){
		Iom_jObject objYearToLow=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objYearToLow.setattrvalue("datum", "15801225");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new ObjectEvent(objYearToLow));
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <15801225> is not in range", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void datumYearToHigh(){
		Iom_jObject objYearToHigh=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objYearToHigh.setattrvalue("datum", "30001225");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new ObjectEvent(objYearToHigh));
		// Asserts
				assertTrue(logger.getErrs().size()==1);
		assertEquals("value <30001225> is not in range", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void datumMonthToLow(){
		Iom_jObject objMonthToLow=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objMonthToLow.setattrvalue("datum", "20160025");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new ObjectEvent(objMonthToLow));
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <20160025> is not in range", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void datumMonthToHigh(){
		Iom_jObject objMonthToHigh=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objMonthToHigh.setattrvalue("datum", "20161325");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new ObjectEvent(objMonthToHigh));
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <20161325> is not in range", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void datumDayToLow(){
		Iom_jObject objDayToLow=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objDayToLow.setattrvalue("datum", "20161200");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new ObjectEvent(objDayToLow));
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <20161200> is not in range", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void datumDayToHigh(){
		Iom_jObject objDayToHigh=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objDayToHigh.setattrvalue("datum", "20161232");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new ObjectEvent(objDayToHigh));
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <20161232> is not in range", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void datumFormatWithDots(){
		Iom_jObject objFormatWithDots=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objFormatWithDots.setattrvalue("datum", "2016.12.25");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new ObjectEvent(objFormatWithDots));
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <2016.12.25> is not a valid Date", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void datumLengthToShort(){
		Iom_jObject objLengthToShort=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objLengthToShort.setattrvalue("datum", "2016125");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new ObjectEvent(objLengthToShort));
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <2016125> is not a valid Date", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void datumLengthToHigh(){
		Iom_jObject objLengthToLong=new Iom_jObject("Datatypes10.Topic.Table", "o1");
		objLengthToLong.setattrvalue("datum", "201612251");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new ObjectEvent(objLengthToLong));
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <201612251> is not a valid Date", logger.getErrs().get(0).getEventMsg());
	}
}