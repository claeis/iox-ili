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

public class UniqueConstraints10Test {

	private TransferDescription td=null;
	
	@Before
	public void setUp() throws Exception {
		// read ili-file
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/UniqueConstraints10.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	//############################################################/
	//########## SUCCESSFUL TESTS ################################/
	//############################################################/
	@Test
	public void valueOk(){
		Iom_jObject objA1=new Iom_jObject("UniqueConstraints10.Topic.TableA", "oida1");
		objA1.setattrvalue("a1", "Anna");
		Iom_jObject objA2=new Iom_jObject("UniqueConstraints10.Topic.TableA", "oida2");
		objA2.setattrvalue("a1", "Berta");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("UniqueConstraints10.Topic","tidb1"));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objA2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	@Test
	public void valueNullOk(){
		Iom_jObject objA1=new Iom_jObject("UniqueConstraints10.Topic.TableA", "oida1");
		objA1.setattrvalue("a1", "Anna");
		Iom_jObject objA2=new Iom_jObject("UniqueConstraints10.Topic.TableA", "oida2");
		objA2.setattrvalue("a1", "Berta");
		Iom_jObject objA3=new Iom_jObject("UniqueConstraints10.Topic.TableA", "oida3");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("UniqueConstraints10.Topic","tidb1"));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objA2));
		validator.validate(new ObjectEvent(objA3));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	@Test
	public void refOk(){
		Iom_jObject objA1=new Iom_jObject("UniqueConstraints10.Topic.TableA", "oida1");
		objA1.setattrvalue("a1", "Anna");
		Iom_jObject objA2=new Iom_jObject("UniqueConstraints10.Topic.TableA", "oida2");
		objA2.setattrvalue("a1", "Berta");
		Iom_jObject objB1=new Iom_jObject("UniqueConstraints10.Topic.TableB", "oidb1");
		objB1.addattrobj("b2", "REF").setobjectrefoid("oida1");
		Iom_jObject objB2=new Iom_jObject("UniqueConstraints10.Topic.TableB", "oidb2");
		objB2.addattrobj("b2", "REF").setobjectrefoid("oida2");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("UniqueConstraints10.Topic","tidb1"));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objA2));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	@Test
	public void refNullOk(){
		Iom_jObject objA1=new Iom_jObject("UniqueConstraints10.Topic.TableA", "a1");
		objA1.setattrvalue("a1", "Anna");
		Iom_jObject objB1=new Iom_jObject("UniqueConstraints10.Topic.TableB", "b1");
		objB1.addattrobj("b2", "REF").setobjectrefoid("a1");
		Iom_jObject objB2=new Iom_jObject("UniqueConstraints10.Topic.TableB", "b2");
		Iom_jObject objB3=new Iom_jObject("UniqueConstraints10.Topic.TableB", "b3");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("UniqueConstraints10.Topic","b1"));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new ObjectEvent(objB3));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	@Test
	public void valueFail(){
		Iom_jObject objA1=new Iom_jObject("UniqueConstraints10.Topic.TableA", "oida1");
		objA1.setattrvalue("a1", "Anna");
		Iom_jObject objA2=new Iom_jObject("UniqueConstraints10.Topic.TableA", "oida2");
		objA2.setattrvalue("a1", "Anna");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("UniqueConstraints10.Topic","tidb1"));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objA2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
	}
	@Test
	public void refFail(){
		Iom_jObject objA1=new Iom_jObject("UniqueConstraints10.Topic.TableA", "oida1");
		objA1.setattrvalue("a1", "Anna");
		Iom_jObject objA2=new Iom_jObject("UniqueConstraints10.Topic.TableA", "oida2");
		objA2.setattrvalue("a1", "Berta");
		Iom_jObject objB1=new Iom_jObject("UniqueConstraints10.Topic.TableB", "oidb1");
		objB1.addattrobj("b2", "REF").setobjectrefoid("oida1");
		Iom_jObject objB2=new Iom_jObject("UniqueConstraints10.Topic.TableB", "oidb2");
		objB2.addattrobj("b2", "REF").setobjectrefoid("oida1");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("UniqueConstraints10.Topic","tidb1"));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objA2));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
	}
}
