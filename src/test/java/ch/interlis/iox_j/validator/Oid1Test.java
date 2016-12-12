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

public class Oid1Test {
	// OID
	private final static String OID1="o1";
	private final static String OID2="o2";
	// MODEL.TOPIC
	private final static String OID1_TOPIC="Oid1.Topic";
	// CLASS
	private final static String OID1_CLASSB=OID1_TOPIC+".ClassB";
	private final static String OID1_CLASSC=OID1_TOPIC+".ClassC";
	// START BASKET EVENT
	private final static String START_BASKET_EVENT = "x1";
	// TD
	private TransferDescription td=null;
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry iliFile=new FileEntry("src/test/data/validator/Oid1.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(iliFile);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}

	//#########################################################//
	//####################### SUCCESS #########################//
	//#########################################################//
	@Test
	public void differentOidOk() throws Exception {
		Iom_jObject objB1=new Iom_jObject(OID1_CLASSB, OID1);
		Iom_jObject objB2=new Iom_jObject(OID1_CLASSB, OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(OID1_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	//#########################################################//
	//######################### FAIL ##########################//
	//#########################################################//
	@Test
	public void duplicateOidFail() throws Exception {
		Iom_jObject objB1=new Iom_jObject(OID1_CLASSB, OID1);
		Iom_jObject objB2=new Iom_jObject(OID1_CLASSB, OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(OID1_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The OID o1 of object 'Oid1.Topic.ClassB oid o1 {}' already exists in CLASS Oid1.Topic.ClassB.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void differentTableDuplicateOidOk() throws Exception {
		Iom_jObject objB1=new Iom_jObject(OID1_CLASSB, OID1);
		Iom_jObject objB2=new Iom_jObject(OID1_CLASSB, OID2);
		Iom_jObject objC1=new Iom_jObject(OID1_CLASSC, OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(OID1_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The OID o1 of object 'Oid1.Topic.ClassC oid o1 {}' already exists in CLASS Oid1.Topic.ClassB.", logger.getErrs().get(0).getEventMsg());
	}
}