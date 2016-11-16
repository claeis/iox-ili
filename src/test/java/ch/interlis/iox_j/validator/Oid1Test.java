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

	private final static String OID1_TOPIC="Oid1.Topic";
	private final static String OID1_CLASSB=OID1_TOPIC+".ClassB";
	private final static String OID1_CLASSC=OID1_TOPIC+".ClassC";
	
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

	@Test
	public void ok_DifferentOid() throws Exception {
		final String OBJ_B1="b1";
		final String OBJ_B2="b2";
		Iom_jObject objB1=new Iom_jObject(OID1_CLASSB, OBJ_B1);
		Iom_jObject objB2=new Iom_jObject(OID1_CLASSB, OBJ_B2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(OID1_TOPIC,"x1"));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}


	@Test
	public void ok_DifferentTableDuplicateOid() throws Exception {
		final String OBJ_B1="b1";
		final String OBJ_B2="b2";
		final String OBJ_C1="b1";
		Iom_jObject objB1=new Iom_jObject(OID1_CLASSB, OBJ_B1);
		Iom_jObject objB2=new Iom_jObject(OID1_CLASSB, OBJ_B2);
		Iom_jObject objC1=new Iom_jObject(OID1_CLASSC, OBJ_C1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(OID1_TOPIC,"x1"));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void fail_DuplicateOid() throws Exception {
		final String OBJ_B1="b1";
		final String OBJ_B2="b1";
		Iom_jObject objB1=new Iom_jObject(OID1_CLASSB, OBJ_B1);
		Iom_jObject objB2=new Iom_jObject(OID1_CLASSB, OBJ_B2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(OID1_TOPIC,"x1"));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
	}
	
}