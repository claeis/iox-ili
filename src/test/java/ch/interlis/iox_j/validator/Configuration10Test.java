package ch.interlis.iox_j.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

public class Configuration10Test {
	
	private TransferDescription td=null;
	// OID
	private final static String OID1 ="o1";
	private final static String OID2 ="o2";
	private final static String OID3 ="o3";
	// MODEL.TOPIC
	private final static String TOPIC="Configuration10.Topic";
	// CLASS
	private final static String CLASSA=TOPIC+".ClassA";
	private final static String CLASSC=TOPIC+".ClassC";
	private final static String CLASSD=TOPIC+".ClassD";
	private final static String CLASSE=TOPIC+".ClassE";
	private final static String CLASSF=TOPIC+".ClassF";
	// START BASKET EVENT
	private final static String BID1="b1";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/Configuration10.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn config target auf OFF steht und die eigene oid als Klassenreferenz angegeben wird.
	@Test
	public void referenceType_NoClassFoundInRef_TagetOFF() throws Exception {
		Iom_jObject objC1=new Iom_jObject(CLASSC, OID1);
		objC1.addattrobj("attrC1", "REF").setobjectrefoid(OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSC+"attrC1.attrC1", ValidationConfig.TARGET,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0, logger.getErrs().size());
	}
	
	// Es wird getestet ob eine Fehlermeldung oder eine Warnung ausgegeben wird, wenn die Konfiguration Warning ausgeschalten wurde und die referenzierte Klasse nicht gefunden wurde.
	// Bei eingeschaltenem Warning bei Konfiguration target, soll, falls es zu einem Referenz-Fehler kommt, nur eine Warning und keinen Fehler ausgegeben werden.
	@Test
	public void referenceType_NoClassFound_TargetWARN() throws Exception {
		Iom_jObject objC1=new Iom_jObject(CLASSC, OID1);
		objC1.addattrobj("attrC1", "REF").setobjectrefoid(OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSC+"attrC1.attrC1", ValidationConfig.TARGET,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0, logger.getErrs().size());
		assertEquals(1, logger.getWarn().size());
		assertEquals("No object found with OID o1 in basket b1.", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler geworfen wird, wenn die Klasse nicht gefunden werden kann und die multiplicitaet auf off gestellt ist.
	@Test
	public void referencetype_ClassNotFound_MultiplicityOFF() throws Exception {
		Iom_jObject iomObjI=new Iom_jObject(CLASSA,OID1);
		Iom_jObject iomObjJ=new Iom_jObject(CLASSD,OID2); // wird nicht in Basket ausgefuehrt.
		iomObjJ.addattrobj("attrC1", "REF").setobjectrefoid(OID1); // wird nicht in Basket ausgefuehrt.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSD+"attrC1.attrC1", ValidationConfig.MULTIPLICITY,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjI));
		// CLASSD with Ref attrC1 --> oid1
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0, logger.getErrs().size());
	}
	
	// Es wird getestet ob eine Fehlermeldung oder eine Warnung geworfen wird, wenn bei der Konfiguration multiplicity, warning eingeschalten wurde und es einen Fehler gibt.
	// Bei eingeschaltenem Warning bei Konfiguration multiplicity, soll, falls es zu einem Multiplizitaeten-Fehler kommt, nur eine Warning und keinen Fehler ausgegeben werden.
	@Test
	public void referenceType_ToFewReferenceClasses_MultiplicityWARNING() throws Exception {
		Iom_jObject iomObjA=new Iom_jObject(CLASSA,OID1); // wird nicht in Basket ausgefuehrt.
		Iom_jObject iomObjI=new Iom_jObject(CLASSD,OID2);
		// wird nicht gefunden (iomObjI.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID1))
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSD+"attrC1.attrC1", ValidationConfig.MULTIPLICITY,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjI));
		// iomObjA with ClassA.
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0, logger.getErrs().size());
		assertEquals(1, logger.getWarn().size());
		assertEquals("attrC1 should associate 1 to 1 target objects (instead of 0)", logger.getWarn().get(0).getEventMsg());
	}
	
	// Wenn Type=OFF, werden keine Warnungen oder Fehler ausgegeben.
	@Test
	public void datatype_WrongFormat_TypeOFF(){
		Iom_jObject objTest=new Iom_jObject(CLASSE, OID1);
		objTest.setattrvalue("attr1", "1.5 5.2");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSE+".attr1", ValidationConfig.TYPE,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==0);
	}
	
	// Eine Warnung wird ausgegeben, wenn Grads keine Nummer ist, wenn config Type eingeschalten wurde und ValidationConfig auf Warning steht.
	@Test
	public void datatype_WrongFormat_TypeWARN(){
		Iom_jObject objTest=new Iom_jObject(CLASSE, OID1);
		objTest.setattrvalue("attr1", "1.5 5.2");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSE+".attr1", ValidationConfig.TYPE,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==1);
		assertEquals("value <1.5 5.2> is not a number", logger.getWarn().get(0).getEventMsg());
	}
	
	// Wenn Type validierung auf OFF steht, aber nur Multplicity auf OFF stehen darf, werden trotzdem Typfehler ausgegeben.
	@Test
	public void datatype_WrongFormat_TypeOFF_ParameterAllowON(){
		Iom_jObject objTest=new Iom_jObject(CLASSE, OID1);
		objTest.setattrvalue("attr1", "1.5 5.2");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Datatypes10.Topic.Table.grads", ValidationConfig.TYPE,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ALLOW_ONLY_MULTIPLICITY_REDUCTION,ValidationConfig.ON);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(objTest));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <1.5 5.2> is not a number",logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob der eigens erstellte Fehler ausgegeben wird, wenn beide Values von einem Unique Attribute identisch sind und validationConfig msg nicht leer ist.
	@Test
	public void uniqueConstraint_uniqueValueExistsTwice_MSGNotEmpty(){
		Iom_jObject objA1=new Iom_jObject(CLASSF, OID1);
		objA1.setattrvalue("a1", "Anna");
		Iom_jObject objA2=new Iom_jObject(CLASSF, OID2);
		objA2.setattrvalue("a1", "Anna");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSF+".Constraint1", ValidationConfig.MSG, "My own Set Constraint.");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
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
	public void uniqueConstraint_uniqueValueExistsTwice_MSGIsEmpty(){
		Iom_jObject objA1=new Iom_jObject(CLASSF, OID1);
		objA1.setattrvalue("a1", "Anna");
		Iom_jObject objA2=new Iom_jObject(CLASSF, OID2);
		objA2.setattrvalue("a1", "Anna");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSF+".Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objA2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values Anna already exist in Object: o1",logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob eine Warning anstelle einer Fehlermeldung ausgegeben wird, wenn die Nummer Unique und identisch ist und validationConfig check auf off geschalten ist.
	@Test
	public void uniqueConstraint_numberUniqueSameNumber_CheckOFF(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSF,OID1);
		obj1.setattrvalue("a1", "Ralf");
		Iom_jObject objA=new Iom_jObject(CLASSF,OID2);
		objA.setattrvalue("a1", "Ralf");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSF+".Constraint1", ValidationConfig.CHECK,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
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
	public void uniqueConstraint_numberUniqueSameNumber_CheckWARN(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSF,OID1);
		obj1.setattrvalue("a1", "Ralf");
		Iom_jObject objA=new Iom_jObject(CLASSF,OID2);
		objA.setattrvalue("a1", "Ralf");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSF+".Constraint1", ValidationConfig.CHECK,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==1);
		assertEquals("Unique is violated! Values Ralf already exist in Object: o1", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet ob der eigens erstellte Fehler ausgegeben wird,
	// wenn beide Values von einem Unique Attribute identisch sind,
	// validationConfig msg nicht leer ist und check auf warning konfiguriert ist.
	@Test
	public void uniqueConstraint_MSGNotEmptyAndWarning(){
		Iom_jObject objA1=new Iom_jObject(CLASSF, OID1);
		objA1.setattrvalue("a1", "Anna");
		Iom_jObject objA2=new Iom_jObject(CLASSF, OID2);
		objA2.setattrvalue("a1", "Anna");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSF+".Constraint1", ValidationConfig.CHECK, ValidationConfig.WARNING);
		modelConfig.setConfigValue(CLASSF+".Constraint1", ValidationConfig.MSG, "My own Set Constraint.");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objA2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getWarn().size()==1);
		assertEquals("My own Set Constraint.",logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet ob der eigens erstellte Fehler ausgegeben wird
	// wenn beide Values von einem Unique Attribute identisch sind,
	// validationConfig msg leer ist und check auf warning konfiguriert ist.
	@Test
	public void uniqueConstraint_MSGEmptyAndWarning(){
		Iom_jObject objA1=new Iom_jObject(CLASSF, OID1);
		objA1.setattrvalue("a1", "Anna");
		Iom_jObject objA2=new Iom_jObject(CLASSF, OID2);
		objA2.setattrvalue("a1", "Anna");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSF+".Constraint1", ValidationConfig.CHECK, ValidationConfig.WARNING);
		modelConfig.setConfigValue(CLASSF+".Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objA2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getWarn().size()==1);
		assertEquals("Unique is violated! Values Anna already exist in Object: o1",logger.getWarn().get(0).getEventMsg());
	}
}