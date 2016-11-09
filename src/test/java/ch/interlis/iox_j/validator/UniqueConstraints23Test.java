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

public class UniqueConstraints23Test {

	private TransferDescription td=null;
	
	@Before
	public void setUp() throws Exception {
		// read ili-file
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/UniqueConstraints23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	//############################################################/
	//########## SUCCESSFUL TESTS ################################/
	//############################################################/
	//////////////// CLASS A //////////////////////////////////////
	@Test
	public void withoutUniqueContraintsSameTextDifferentNumbersAOk(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassA", "o1");
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassA", "o2");
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void withoutUniqueContraintsDifferentTextSameNumbersAOk(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassA", "o1");
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassA", "o2");
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void withoutUniqueContraintsDifferentTextDifferentNumbersAOk(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassA", "o1");
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassA", "o2");
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void withoutUniqueContraintsSameTextSameNumbersAOk(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassA", "o1");
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassA", "o2");
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	/////////////// END CLASS A ///////////////////////////////////
	/////////////// CLASS B ///////////////////////////////////////
	@Test
	public void uniqueContraintsSameNumbersDifferentTextBOk(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassB", "o1");
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassB", "o2");
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void uniqueContraintsDifferentNumbersDifferentTextBOk(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassB", "o1");
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassB", "o2");
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	/////////////// END CLASS B ///////////////////////////////////
	/////////////// CLASS C ///////////////////////////////////////
	@Test
	public void uniqueContraintsDifferentTextSameNumbersCOk(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassC", "o1");
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassC", "o2");
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void uniqueContraintsSameTextDifferentNumbersCOk(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassC", "o1");
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassC", "o2");
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void uniqueContraintsDifferentTextDifferentNumbersCOk(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassC", "o1");
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassC", "o2");
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	/////////////// END CLASS C ///////////////////////////////////
	/////////////// CLASS D ///////////////////////////////////////
	@Test
	public void uniqueConstraintTextNrDifferentDOk(){
	// Set object.
	Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassD", "o1");
	obj1.setattrvalue("attr1", "Ralf");
	obj1.setattrvalue("attr2", "20");
	Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassD", "o2");
	obj2.setattrvalue("attr1", "Anna");
	obj2.setattrvalue("attr2", "30");
	// Create and run validator.
	ValidationConfig modelConfig=new ValidationConfig();
	LogCollector logger=new LogCollector();
	LogEventFactory errFactory=new LogEventFactory();
	Settings settings=new Settings();
	Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
	validator.validate(new StartTransferEvent());
	validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
	validator.validate(new ObjectEvent(obj1));
	validator.validate(new ObjectEvent(obj2));
	validator.validate(new EndBasketEvent());
	validator.validate(new EndTransferEvent());
	// Asserts.
	assertTrue(logger.getErrs().size()==0);
	}
	/////////////// END CLASS D ///////////////////////////////////
	/////////////// CLASS E ///////////////////////////////////////
	@Test
	public void uniqueConstraintTextNrDifferentEOk(){
	// Set object.
	Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassE", "o1");
	obj1.setattrvalue("attr1", "Ralf");
	obj1.setattrvalue("attr2", "20");
	Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassE", "o2");
	obj2.setattrvalue("attr1", "Anna");
	obj2.setattrvalue("attr2", "30");
	// Create and run validator.
	ValidationConfig modelConfig=new ValidationConfig();
	LogCollector logger=new LogCollector();
	LogEventFactory errFactory=new LogEventFactory();
	Settings settings=new Settings();
	Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
	validator.validate(new StartTransferEvent());
	validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
	validator.validate(new ObjectEvent(obj1));
	validator.validate(new ObjectEvent(obj2));
	validator.validate(new EndBasketEvent());
	validator.validate(new EndTransferEvent());
	// Asserts.
	assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void uniqueConstraintTextSameNrDifferentEOk(){
	// Set object.
	Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassE", "o1");
	obj1.setattrvalue("attr1", "Ralf");
	obj1.setattrvalue("attr2", "20");
	Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassE", "o2");
	obj2.setattrvalue("attr1", "Ralf");
	obj2.setattrvalue("attr2", "30");
	// Create and run validator.
	ValidationConfig modelConfig=new ValidationConfig();
	LogCollector logger=new LogCollector();
	LogEventFactory errFactory=new LogEventFactory();
	Settings settings=new Settings();
	Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
	validator.validate(new StartTransferEvent());
	validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
	validator.validate(new ObjectEvent(obj1));
	validator.validate(new ObjectEvent(obj2));
	validator.validate(new EndBasketEvent());
	validator.validate(new EndTransferEvent());
	// Asserts.
	assertTrue(logger.getErrs().size()==0);
	}
	/////////////// END CLASS E ///////////////////////////////////
	
	//############################################################/
	//########## FAILING TESTS ###################################/
	//############################################################/
	
	/////////////// CLASS B ///////////////////////////////////////
	@Test
	public void uniqueContraintsSameTextSameNumbersBFail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassB", "o1");
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassB", "o2");
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values Ralf already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void uniqueContraintsSameTextDifferentNumbersBFail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassB", "o1");
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassB", "o2");
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "15");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values Ralf already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
	/////////////// END CLASS B ///////////////////////////////////
	/////////////// CLASS C ///////////////////////////////////////
	@Test
	public void uniqueContraintsSameTextSameNumbersCFail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassC", "o1");
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassC", "o2");
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values Ralf, 20 already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
	/////////////// END CLASS C ///////////////////////////////////
	/////////////// CLASS D ///////////////////////////////////////
	@Test
	public void uniqueConstraintTextDifferentNrSameDFail(){
	// Set object.
	Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassD", "o1");
	obj1.setattrvalue("attr1", "Ralf");
	obj1.setattrvalue("attr2", "20");
	Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassD", "o2");
	obj2.setattrvalue("attr1", "Anna");
	obj2.setattrvalue("attr2", "20");
	// Create and run validator.
	ValidationConfig modelConfig=new ValidationConfig();
	LogCollector logger=new LogCollector();
	LogEventFactory errFactory=new LogEventFactory();
	Settings settings=new Settings();
	Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
	validator.validate(new StartTransferEvent());
	validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
	validator.validate(new ObjectEvent(obj1));
	validator.validate(new ObjectEvent(obj2));
	validator.validate(new EndBasketEvent());
	validator.validate(new EndTransferEvent());
	// Asserts.
	assertTrue(logger.getErrs().size()==1);
	assertEquals("Unique is violated! Values 20 already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void uniqueConstraintTextSameNrDifferentDFail(){
	// Set object.
	Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassD", "o1");
	obj1.setattrvalue("attr1", "Ralf");
	obj1.setattrvalue("attr2", "20");
	Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassD", "o2");
	obj2.setattrvalue("attr1", "Ralf");
	obj2.setattrvalue("attr2", "10");
	// Create and run validator.
	ValidationConfig modelConfig=new ValidationConfig();
	LogCollector logger=new LogCollector();
	LogEventFactory errFactory=new LogEventFactory();
	Settings settings=new Settings();
	Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
	validator.validate(new StartTransferEvent());
	validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
	validator.validate(new ObjectEvent(obj1));
	validator.validate(new ObjectEvent(obj2));
	validator.validate(new EndBasketEvent());
	validator.validate(new EndTransferEvent());
	// Asserts.
	assertTrue(logger.getErrs().size()==1);
	assertEquals("Unique is violated! Values Ralf already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void uniqueConstraintTextNrSameDFail(){
	// Set object.
	Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassD", "o1");
	obj1.setattrvalue("attr1", "Ralf");
	obj1.setattrvalue("attr2", "20");
	Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassD", "o2");
	obj2.setattrvalue("attr1", "Ralf");
	obj2.setattrvalue("attr2", "20");
	// Create and run validator.
	ValidationConfig modelConfig=new ValidationConfig();
	LogCollector logger=new LogCollector();
	LogEventFactory errFactory=new LogEventFactory();
	Settings settings=new Settings();
	Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
	validator.validate(new StartTransferEvent());
	validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
	validator.validate(new ObjectEvent(obj1));
	validator.validate(new ObjectEvent(obj2));
	validator.validate(new EndBasketEvent());
	validator.validate(new EndTransferEvent());
	// Asserts.
	assertTrue(logger.getErrs().size()==2);
	assertEquals("Unique is violated! Values Ralf already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	assertEquals("Unique is violated! Values 20 already exist in Object: o1", logger.getErrs().get(1).getEventMsg());
	}
	/////////////// END CLASS D ///////////////////////////////////
	/////////////// CLASS E ///////////////////////////////////////
	
	@Test
	public void uniqueConstraintTextDifferentNrSameEFail(){
	// Set object.
	Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassE", "o1");
	obj1.setattrvalue("attr1", "Ralf");
	obj1.setattrvalue("attr2", "20");
	Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassE", "o2");
	obj2.setattrvalue("attr1", "Anna");
	obj2.setattrvalue("attr2", "20");
	// Create and run validator.
	ValidationConfig modelConfig=new ValidationConfig();
	LogCollector logger=new LogCollector();
	LogEventFactory errFactory=new LogEventFactory();
	Settings settings=new Settings();
	Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
	validator.validate(new StartTransferEvent());
	validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
	validator.validate(new ObjectEvent(obj1));
	validator.validate(new ObjectEvent(obj2));
	validator.validate(new EndBasketEvent());
	validator.validate(new EndTransferEvent());
	// Asserts.
	assertTrue(logger.getErrs().size()==1);
	assertEquals("Unique is violated! Values 20 already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void uniqueConstraintTextNrSameEFail(){
	// Set object.
	Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassE", "o1");
	obj1.setattrvalue("attr1", "Ralf");
	obj1.setattrvalue("attr2", "20");
	Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassE", "o2");
	obj2.setattrvalue("attr1", "Ralf");
	obj2.setattrvalue("attr2", "20");
	// Create and run validator.
	ValidationConfig modelConfig=new ValidationConfig();
	LogCollector logger=new LogCollector();
	LogEventFactory errFactory=new LogEventFactory();
	Settings settings=new Settings();
	Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
	validator.validate(new StartTransferEvent());
	validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
	validator.validate(new ObjectEvent(obj1));
	validator.validate(new ObjectEvent(obj2));
	validator.validate(new EndBasketEvent());
	validator.validate(new EndTransferEvent());
	// Asserts.
	assertTrue(logger.getErrs().size()==2);
	assertEquals("Unique is violated! Values Ralf, 20 already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	assertEquals("Unique is violated! Values 20 already exist in Object: o1", logger.getErrs().get(1).getEventMsg());
	}
	/////////////// END CLASS E ///////////////////////////////////
	@Test
	public void uniqueConstraintTextNrSameExFail(){
	// Set object.
	Iom_jObject obj1=new Iom_jObject("UniqueConstraints23.Topic.ClassD", "o1");
	obj1.setattrvalue("attr1", "Ralf");
	obj1.setattrvalue("attr2", "20");
	Iom_jObject obj2=new Iom_jObject("UniqueConstraints23.Topic.ClassE", "o2");
	obj2.setattrvalue("attr1", "Ralf");
	obj2.setattrvalue("attr2", "20");
	// Create and run validator.
	ValidationConfig modelConfig=new ValidationConfig();
	LogCollector logger=new LogCollector();
	LogEventFactory errFactory=new LogEventFactory();
	Settings settings=new Settings();
	Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
	validator.validate(new StartTransferEvent());
	validator.validate(new StartBasketEvent("UniqueConstraints23.Topic","b1"));
	validator.validate(new ObjectEvent(obj1));
	validator.validate(new ObjectEvent(obj2));
	validator.validate(new EndBasketEvent());
	validator.validate(new EndTransferEvent());
	// Asserts.
	assertTrue(logger.getErrs().size()==0);
	}
}
