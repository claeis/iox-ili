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

public class UniqueConstraints23Test {

	private TransferDescription td=null;
	// BID
	private final static String OID1 = "o1";
	private final static String OID2 = "o2";
	// BID
	private final static String BID = "b1";
	// TOPIC
	private final static String TOPIC="UniqueConstraints23.Topic";
	// CLASS
	private final static String CLASSA=TOPIC+".ClassA";
	private final static String CLASSB=TOPIC+".ClassB";
	private final static String CLASSB0=TOPIC+".ClassB0";
	private final static String CLASSC=TOPIC+".ClassC";
	private final static String CLASSD=TOPIC+".ClassD";
	private final static String CLASSE=TOPIC+".ClassE";
	
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

	// Es wird getestet ob ein Fehler ausgegeben wird, wenn kein unique festgelegt wurde. Jedoch die Values von attr1 identisch sind.
	@Test
	public void noUniqueSameText_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSA,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject(CLASSA,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn kein Unique definiert wurde, jedoch die Nummern identisch ist.
	@Test
	public void noUniqueSameNumber_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSA,OID1);
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSA,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn kein Unique definiert wurde und weder Text, noch Nummer identisch sind.
	@Test
	public void noUniqueBothDifferent_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSA,OID1);
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject(CLASSA,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn kein Unique definiert wurde, jedoch Text und Nummern identisch sind.
	@Test
	public void noUniqueSameTextAndNumber_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSA,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSA,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}

	// Es wird getestet ob ein Fehler ausgegeben wird, wenn (Text und Nummer) Unique sind, jedoch Nummer identisch ist.
	@Test
	public void bothUniqueTogetherAndSameNumber_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSB,OID1);
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSB,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn beide Unique definiert sind und nichts identisch ist.
	@Test
	public void bothUniqueTogether_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSB,OID1);
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject(CLASSB,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn die Nummer Unique definiert ist und der Text identisch ist.
	@Test
	public void numberUniqueSameText_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSC,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject(CLASSC,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn die Nummer Unique definiert ist und nichts identisch ist.
	@Test
	public void numberUnique_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSC,OID1);
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject(CLASSC,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn der Text Unique definiert ist und die Nummer identisch ist.
	@Test
	public void textUniqueSameNumber_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSB0,OID1);
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSB0,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn der Text Unique definiert ist und nichts identisch ist.
	@Test
	public void textUnique_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSB0,OID1);
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject(CLASSB0,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}

	// Es wird getestet ob ein Fehler ausgegeben wird, wenn Text Unique und Nummer Unique separat sind und nicht identisch ist.
	@Test
	public void textUniqueNumberUnique_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSD,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSD,OID2);
		obj2.setattrvalue("attr1", "Anna");
		obj2.setattrvalue("attr2", "30");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}

	// Es wird getestet ob ein Fehler ausgegeben wird, wenn Text und Nummer Unique sind und die Nummer Unique ist.
	@Test
	public void textNumberUniqueNumberUnique_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSE,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSE,OID2);
		obj2.setattrvalue("attr1", "Anna");
		obj2.setattrvalue("attr2", "30");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn Text und Nummer Unique sind und Nummer Unique ist. Dabei ist der Text identisch.
	@Test
	public void textNumberUniqueNumberUniqueSameText_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSE,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSE,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "30");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	//############################################################/
	//########## FAILING TESTS ###################################/
	//############################################################/

	// Es wird getestet ob ein Fehler ausgegeben wird, wenn die Nummer Unique und identisch ist.
	@Test
	public void numberUniqueSameNumber_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSB,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject objA=new Iom_jObject(CLASSB,OID2);
		objA.setattrvalue("attr1", "Ralf");
		objA.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values Ralf, 20 already exist in Object: o2", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigens erstellte Fehlermeldung ausgegeben wird, wenn die Nummer Unique und identisch ist, wenn validationConfig msg nicht leer ist.
	@Test
	public void numberUniqueSameNumber_MSGNotEmpty_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSB,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject objA=new Iom_jObject(CLASSB,OID2);
		objA.setattrvalue("attr1", "Ralf");
		objA.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSB+".Constraint1", ValidationConfig.MSG, "My own Set Constraint.");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("My own Set Constraint.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigens erstellte Fehlermeldung ausgegeben wird, wenn die Nummer Unique und identisch ist, wenn validationConfig msg leer ist.
	@Test
	public void numberUniqueSameNumber_MSGIsEmpty_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSB,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject objA=new Iom_jObject(CLASSB,OID2);
		objA.setattrvalue("attr1", "Ralf");
		objA.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSB, ValidationConfig.MSG, "");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values Ralf, 20 already exist in Object: o2", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn der Text Unique und identisch ist.
	@Test
	public void textUniqueSameText_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSB0,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject(CLASSB0,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values Ralf already exist in Object: o2", logger.getErrs().get(0).getEventMsg());
	}

	// Es wird getestet ob ein Fehler ausgegeben wird, wenn Number Unique ist und beide Values identisch sind.
	@Test
	public void numberUniqueBothSame_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSC,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSC,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values 20 already exist in Object: o2", logger.getErrs().get(0).getEventMsg());
	}

	// Es wird getestet ob ein Fehler ausgegeben wird, wenn Text Unique und Nummber Unique ist. Dabei die Nummer identisch ist.
	@Test
	public void textUniqueNumberUniqueNumberSame_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSD,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSD,OID2);
		obj2.setattrvalue("attr1", "Anna");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values 20 already exist in Object: o2", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn der Text Unique und die Nummer Unique ist. Dabei ist der Text identisch und die Nummer verschieden.
	@Test
	public void textUniqueNumberUniqueSameText_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSD,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSD,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "10");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values Ralf already exist in Object: o2", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn der Text Unique ist und die Nummer Unique ist. Dabei beide Values identisch sind.
	@Test
	public void textUniqueNumberUniqueBothSame_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSD,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSD,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==2);
		assertEquals("Unique is violated! Values Ralf already exist in Object: o2", logger.getErrs().get(0).getEventMsg());
		assertEquals("Unique is violated! Values 20 already exist in Object: o2", logger.getErrs().get(1).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn der Text und die Nummer Unique sind und die Nummer Unique ist. Dabei sind die Values der Nummer identisch.
	@Test
	public void textNumberUniqueNumberUniqueSameNumber_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSE,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSE,OID2);
		obj2.setattrvalue("attr1", "Anna");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values 20 already exist in Object: o2", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn der Text und die Nummer Unique sind und die Nummer Unique ist. Dabei beide Values identisch sind.
	@Test
	public void textNumberUniqueNumberUniqueBothSame_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSE,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSE,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==2);
		assertEquals("Unique is violated! Values Ralf, 20 already exist in Object: o2", logger.getErrs().get(0).getEventMsg());
		assertEquals("Unique is violated! Values 20 already exist in Object: o2", logger.getErrs().get(1).getEventMsg());
	}

	// Es wird getestet ob ein Fehler ausgegeben wird, wenn der Text Unique ist und die Nummer Unique ist. Dabei beide Values identisch sind.
	@Test
	public void textUniqueNumberUniqueTextSameNumberSame_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSD,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSD,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==2);
		assertEquals("Unique is violated! Values Ralf already exist in Object: o2", logger.getErrs().get(0).getEventMsg());
		assertEquals("Unique is violated! Values 20 already exist in Object: o2", logger.getErrs().get(1).getEventMsg());
	}
	
	// Es wird getestet ob eine Warning anstelle einer Fehlermeldung ausgegeben wird, wenn die Nummer Unique und identisch ist und validationConfig check auf off geschalten ist.
	@Test
	public void numberUniqueSameNumber_ConfigCheckOFF_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSB,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject objA=new Iom_jObject(CLASSB,OID2);
		objA.setattrvalue("attr1", "Ralf");
		objA.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSB+".Constraint1", ValidationConfig.CHECK,ValidationConfig.OFF);
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
		Iom_jObject obj1=new Iom_jObject(CLASSB,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject objA=new Iom_jObject(CLASSB,OID2);
		objA.setattrvalue("attr1", "Ralf");
		objA.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSB+".Constraint1", ValidationConfig.CHECK,ValidationConfig.WARNING);
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
		assertEquals("Unique is violated! Values Ralf, 20 already exist in Object: o2", logger.getWarn().get(0).getEventMsg());
	}
}