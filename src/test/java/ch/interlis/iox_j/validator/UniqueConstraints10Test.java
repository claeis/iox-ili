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

public class UniqueConstraints10Test {

	private TransferDescription td=null;
	// BID
	private final static String OID1 = "o1";
	private final static String OID2 = "o2";
	private final static String OID3 = "o3";
	private final static String OID4 = "o4";
	// BID
	private final static String BID = "b1";
	// TOPIC
	private final static String TOPIC="UniqueConstraints10.Topic";
	// CLASS
	private final static String TABLEA=TOPIC+".TableA";
	private final static String TABLEB=TOPIC+".TableB";
	
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

	// Es wird getestet ob ein Fehler ausgegeben wird, wenn die Values eines UniqueConstraints unterschiedlich sind.
	@Test
	public void differentValues_Ok(){
		Iom_jObject objA1=new Iom_jObject(TABLEA, OID1);
		objA1.setattrvalue("a1", "Anna");
		Iom_jObject objA2=new Iom_jObject(TABLEA, OID2);
		objA2.setattrvalue("a1", "Berta");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objA2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn bei einem optionalen Attribute keine Value erstellt wurde.
	@Test
	public void valueUndefined_Ok(){
		Iom_jObject objA1=new Iom_jObject(TABLEA, OID1);
		objA1.setattrvalue("a1", "Anna");
		Iom_jObject objA2=new Iom_jObject(TABLEA, OID2);
		objA2.setattrvalue("a1", "Berta");
		Iom_jObject objA3=new Iom_jObject(TABLEA, OID3);
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objA2));
		validator.validate(new ObjectEvent(objA3));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn die Attributnamen identisch sind, jedoch die referenzierten Values unterschiedlich.
	@Test
	public void differentRefValues_Ok(){
		Iom_jObject objA1=new Iom_jObject(TABLEA, OID1);
		objA1.setattrvalue("a1", "Anna");
		Iom_jObject objA2=new Iom_jObject(TABLEA, OID2);
		objA2.setattrvalue("a1", "Berta");
		Iom_jObject objB1=new Iom_jObject(TABLEB, OID3);
		objB1.addattrobj("b2", "REF").setobjectrefoid(OID1);
		Iom_jObject objB2=new Iom_jObject(TABLEB, OID4);
		objB2.addattrobj("b2", "REF").setobjectrefoid(OID2);
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objA2));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn die Referenzierte Value, null ergibt.
	@Test
	public void refValueIsNull_Ok(){
		Iom_jObject objA1=new Iom_jObject(TABLEA, OID1);
		objA1.setattrvalue("a1", "Anna");
		Iom_jObject objB1=new Iom_jObject(TABLEB, OID2);
		objB1.addattrobj("b2", "REF").setobjectrefoid(OID1);
		Iom_jObject objB2=new Iom_jObject(TABLEB, OID3);
		Iom_jObject objB3=new Iom_jObject(TABLEB, OID4);
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new ObjectEvent(objB3));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn beide Values von einem Unique Attribute identisch sind.
	@Test
	public void uniqueValueExistsTwice_Fail(){
		Iom_jObject objA1=new Iom_jObject(TABLEA, OID1);
		objA1.setattrvalue("a1", "Anna");
		Iom_jObject objA2=new Iom_jObject(TABLEA, OID2);
		objA2.setattrvalue("a1", "Anna");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objA2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values Anna already exist in Object: o1",logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob der eigens erstellte Fehler ausgegeben wird, wenn beide Values von einem Unique Attribute identisch sind und validationConfig msg nicht leer ist.
	@Test
	public void uniqueValueExistsTwice_MSGNotEmpty_Fail(){
		Iom_jObject objA1=new Iom_jObject(TABLEA, OID1);
		objA1.setattrvalue("a1", "Anna");
		Iom_jObject objA2=new Iom_jObject(TABLEA, OID2);
		objA2.setattrvalue("a1", "Anna");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(TABLEA+".Constraint1", ValidationConfig.MSG, "My own Set Constraint.");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objA2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("My own Set Constraint.",logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob der eigens erstellte Fehler ausgegeben wird, wenn beide Values von einem Unique Attribute identisch sind und validationConfig msg leer ist.
	@Test
	public void uniqueValueExistsTwice_MSGIsEmpty_Fail(){
		Iom_jObject objA1=new Iom_jObject(TABLEA, OID1);
		objA1.setattrvalue("a1", "Anna");
		Iom_jObject objA2=new Iom_jObject(TABLEA, OID2);
		objA2.setattrvalue("a1", "Anna");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(TABLEA+".Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objA2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values Anna already exist in Object: o1",logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn die Referenz auf ein bereits existierendes unique Attribute verweist.
	@Test
	public void refOfUniqueValueExistsTwice_Fail(){
		Iom_jObject objA1=new Iom_jObject(TABLEA, OID1);
		objA1.setattrvalue("a1", "Anna");
		Iom_jObject objA2=new Iom_jObject(TABLEA, OID2);
		objA2.setattrvalue("a1", "Berta");
		Iom_jObject objB1=new Iom_jObject(TABLEB, OID3);
		objB1.addattrobj("b2", "REF").setobjectrefoid(OID1);
		Iom_jObject objB2=new Iom_jObject(TABLEB, OID4);
		objB2.addattrobj("b2", "REF").setobjectrefoid(OID1);
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objA2));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values o1 already exist in Object: o3",logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob eine Warning anstelle einer Fehlermeldung ausgegeben wird, wenn die Nummer Unique und identisch ist und validationConfig check auf off geschalten ist.
	@Test
	public void numberUniqueSameNumber_ConfigCheckOFF_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(TABLEA,OID1);
		obj1.setattrvalue("a1", "Ralf");
		Iom_jObject objA=new Iom_jObject(TABLEA,OID2);
		objA.setattrvalue("a1", "Ralf");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(TABLEA+".Constraint1", ValidationConfig.CHECK,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==0);
	}
	
	// Es wird getestet ob eine Warning anstelle einer Fehlermeldung ausgegeben wird, wenn die Nummer Unique und identisch ist und validationConfig check auf warning geschalten ist.
	@Test
	public void numberUniqueSameNumber_ConfigCheckWARN_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(TABLEA,OID1);
		obj1.setattrvalue("a1", "Ralf");
		Iom_jObject objA=new Iom_jObject(TABLEA,OID2);
		objA.setattrvalue("a1", "Ralf");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(TABLEA+".Constraint1", ValidationConfig.CHECK,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==1);
		assertEquals("Unique is violated! Values Ralf already exist in Object: o1", logger.getWarn().get(0).getEventMsg());
	}
}