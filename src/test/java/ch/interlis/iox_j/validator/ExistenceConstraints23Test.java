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

public class ExistenceConstraints23Test {
	
	private TransferDescription td=null;
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntryConditionClass=new FileEntry("src/test/data/validator/ExistenceConstraints23Condition.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntryConditionClass);
		FileEntry fileEntry=new FileEntry("src/test/data/validator/ExistenceConstraints23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	//############################################################/
	//########## SUCCESSFUL TESTS ################################/
	//############################################################/
	//////////////// CLASS A //////////////////////////////////////
	@Test
	public void existenceConstraintInSameModel() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClass", "o1");
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", "o2");
		objA.setattrvalue("attr5", "lars");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	@Test
	public void existenceConstraintInSameModelX() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClassX", "o1");
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", "o2");
		objA.setattrvalue("attr5", "lars");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	@Test
	public void existenceConstraintInSameModel2() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClass2", "o1");
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", "o2");
		objA.setattrvalue("attr5", "lars");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void existenceConstraintInSameModelR() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClass", "o1");
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", "o2");
		objA.setattrvalue("attr5", "lars");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b1"));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	////////////////// END CLASS A ///////////////////////////////
	////////////////// START CLASS B /////////////////////////////
	@Test
	public void existenceConstraintInOtherModel() throws Exception {
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClass", "o1");
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objB=new Iom_jObject("ExistenceConstraints23.Topic.ClassB", "o2");
		objB.setattrvalue("attr1", "lars");
		objB.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b2"));
		validator.validate(new ObjectEvent(objB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	////////////////// END CLASS A ///////////////////////////////
	////////////////// START CLASS B /////////////////////////////
	@Test
	public void existenceConstraintInOtherModelR() throws Exception {
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClass", "o1");
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objB=new Iom_jObject("ExistenceConstraints23.Topic.ClassB", "o2");
		objB.setattrvalue("attr1", "lars");
		objB.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b2"));
		validator.validate(new ObjectEvent(objB));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	//////////////////// END CLASS B /////////////////////////////
	
	//############################################################
	//################ FAIL TESTS ################################
	//############################################################
	//////////////////// START CLASS A ///////////////////////////
	@Test
	public void existenceConstraintFalseInSameModel() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClass", "o1");
		objBedingung.setattrvalue("attr1", "other");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", "o2");
		objA.setattrvalue("attr5", "lars");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Attributevalue of attribute attr5 not found", logger.getErrs().get(0).getEventMsg());
	}
	//////////////////// END CLASS A ///////////////////////////
	//////////////////// START CLASS B /////////////////////////
	@Test
	public void existenceConstraintExistenceFalseOtherModel() throws Exception {
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClass", "o1");
		objBedingung.setattrvalue("attr1", "other");
		Iom_jObject objB=new Iom_jObject("ExistenceConstraints23.Topic.ClassB", "o2");
		objB.setattrvalue("attr1", "lars");
		objB.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b2"));
		validator.validate(new ObjectEvent(objB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Attributevalue of attribute attr1 not found", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void existenceConstraintSameOIDo1() throws Exception {
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClass", "o1");
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objB=new Iom_jObject("ExistenceConstraints23.Topic.ClassB", "o1");
		objB.setattrvalue("attr1", "lars");
		objB.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b2"));
		validator.validate(new ObjectEvent(objB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Oid cant exist twice.", logger.getErrs().get(0).getEventMsg());
	}
	///////////////////////////////// END CLASS B ////////////////////
}
