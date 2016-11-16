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

public class Oid23Test {

	private final static String OID23_TOPIC="Oid23.Topic";
	private final static String OID23_STRUCTA=OID23_TOPIC+".StructA";
	private final static String OID23_CLASSB=OID23_TOPIC+".ClassB";
	private final static String OID23_CLASSC=OID23_TOPIC+".ClassC";
	
private TransferDescription td=null;
	
@Before
public void setUp() throws Exception {
	// ili-datei lesen
	Configuration ili2cConfig=new Configuration();
	FileEntry iliFile=new FileEntry("src/test/data/validator/Oid23.ili", FileEntryKind.ILIMODELFILE);
	ili2cConfig.addFileEntry(iliFile);
	td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
	assertNotNull(td);
}

	@Test
	public void ok_DifferentOid() throws Exception {
		final String OBJ_B1="b1";
		final String OBJ_B2="b2";
		Iom_jObject objB1=new Iom_jObject(OID23_CLASSB, OBJ_B1);
		Iom_jObject objB2=new Iom_jObject(OID23_CLASSB, OBJ_B2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(OID23_TOPIC,"x1"));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}


	@Test
	public void ok_EmbeddedAssociatianWithoutId(){
		final String OBJ_B1="b1";
		final String OBJ_C1="c1";
		Iom_jObject objB1=new Iom_jObject(OID23_CLASSB, OBJ_B1);
		Iom_jObject objC1=new Iom_jObject(OID23_CLASSC, OBJ_C1);
		objC1.addattrobj("b1", "REF").setobjectrefoid(OBJ_B1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(OID23_TOPIC,"x1"));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	@Test
	public void ok_AssociatianWithoutId(){
		final String OBJ_B1="b1";
		final String OBJ_C1="c1";
		Iom_jObject objB1=new Iom_jObject(OID23_CLASSB, OBJ_B1);
		Iom_jObject objC1=new Iom_jObject(OID23_CLASSC, OBJ_C1);
		Iom_jObject objBC=new Iom_jObject(OID23_TOPIC+".bc2", null);
		objBC.addattrobj("b2", "REF").setobjectrefoid(OBJ_B1);
		objBC.addattrobj("c2", "REF").setobjectrefoid(OBJ_C1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(OID23_TOPIC,"x1"));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new ObjectEvent(objBC));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	@Test
	public void ok_AssociatianWithId(){
		final String OBJ_B1="b1";
		final String OBJ_C1="c1";
		Iom_jObject objB1=new Iom_jObject(OID23_CLASSB, OBJ_B1);
		Iom_jObject objC1=new Iom_jObject(OID23_CLASSC, OBJ_C1);
		Iom_jObject objBC=new Iom_jObject(OID23_TOPIC+".bc3", "bc1");
		objBC.addattrobj("b3", "REF").setobjectrefoid(OBJ_B1);
		objBC.addattrobj("c3", "REF").setobjectrefoid(OBJ_C1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(OID23_TOPIC,"x1"));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new ObjectEvent(objBC));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	@Test
	public void ok_AssociatianWithOid(){
		final String OBJ_B1="b1";
		final String OBJ_C1="c1";
		Iom_jObject objB1=new Iom_jObject(OID23_CLASSB, OBJ_B1);
		Iom_jObject objC1=new Iom_jObject(OID23_CLASSC, OBJ_C1);
		Iom_jObject objBC=new Iom_jObject(OID23_TOPIC+".bc4", "842FCD8B-2543-4d2f-837F-EAF813E125B2");
		objBC.addattrobj("b4", "REF").setobjectrefoid(OBJ_B1);
		objBC.addattrobj("c4", "REF").setobjectrefoid(OBJ_C1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(OID23_TOPIC,"x1"));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new ObjectEvent(objBC));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	
	@Test
	public void ok_Struct(){
		final String OBJ_B1="b1";
		Iom_jObject objB1=new Iom_jObject(OID23_CLASSB, OBJ_B1);
		objB1.addattrobj("attrB2", OID23_STRUCTA);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(OID23_TOPIC,"x1"));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	
	@Test
	public void fail_DuplicateOid() throws Exception {
		final String OBJ_B1="b1";
		final String OBJ_B2="b1";
		Iom_jObject objB1=new Iom_jObject(OID23_CLASSB, OBJ_B1);
		Iom_jObject objB2=new Iom_jObject(OID23_CLASSB, OBJ_B2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(OID23_TOPIC,"x1"));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
	}
	
	@Test
	public void fail_UndefinedOid(){
		final String OBJ_B1="b1";
		final String OBJ_B2=null;
		Iom_jObject objB1=new Iom_jObject(OID23_CLASSB, OBJ_B1);
		Iom_jObject objB2=new Iom_jObject(OID23_CLASSB, OBJ_B2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(OID23_TOPIC,"x1"));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
	}
	@Test
	public void fail_AssociatianWithId(){
		final String OBJ_B1="b1";
		final String OBJ_C1="c1";
		Iom_jObject objB1=new Iom_jObject(OID23_CLASSB, OBJ_B1);
		Iom_jObject objC1=new Iom_jObject(OID23_CLASSC, OBJ_C1);
		Iom_jObject objBC=new Iom_jObject(OID23_TOPIC+".bc3", null);
		objBC.addattrobj("b3", "REF").setobjectrefoid(OBJ_B1);
		objBC.addattrobj("c3", "REF").setobjectrefoid(OBJ_C1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(OID23_TOPIC,"x1"));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new ObjectEvent(objBC));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
	}
	@Test
	public void fail_AssociatianWithOid(){
		final String OBJ_B1="b1";
		final String OBJ_C1="c1";
		Iom_jObject objB1=new Iom_jObject(OID23_CLASSB, OBJ_B1);
		Iom_jObject objC1=new Iom_jObject(OID23_CLASSC, OBJ_C1);
		Iom_jObject objBC=new Iom_jObject(OID23_TOPIC+".bc4", null);
		objBC.addattrobj("b4", "REF").setobjectrefoid(OBJ_B1);
		objBC.addattrobj("c4", "REF").setobjectrefoid(OBJ_C1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(OID23_TOPIC,"x1"));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new ObjectEvent(objBC));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
	}
}