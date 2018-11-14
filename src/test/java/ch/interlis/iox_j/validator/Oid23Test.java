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
	// OID
    private final static String OID1 = "o1";
    private final static String OID2 = "o2";
    private final static String OID3 = "o3";
    private final static String OID4 = "o4";
    private final static String OID5 = "o5";
	// MODEL.TOPIC
	private final static String TOPIC="Oid23.Topic";
	private final static String TOPIC3="Oid23.Topic3";
	// CLASSES
	private final static String CLASSA=TOPIC3+".ClassA";
	private final static String CLASSB3=TOPIC3+".ClassB3";
	private final static String CLASSB=TOPIC+".ClassB";
	private final static String CLASSC=TOPIC+".ClassC";
	// ASSOCIATION
	private final static String ASSOCIATIONB2=TOPIC+".bc2";
	private final static String ASSOCIATIONB3=TOPIC+".bc3";
	private final static String ASSOCIATIONB4=TOPIC+".bc4";
	// STRUCTURE
	private final static String STRUCTA=TOPIC+".StructA";
	// TD
	private TransferDescription td=null;
	// START EVENT BASKET
	private final static String BID = "b1";
		
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry iliFile=new FileEntry("src/test/data/validator/Oid23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(iliFile);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}

	//#############################################################//
	//######################## SUCCESS ############################//
	//#############################################################//
		
    @Test
    public void textOidAttrValue_Ok() throws Exception {
        Iom_jObject objA1=new Iom_jObject(CLASSA, OID1);
        objA1.setattrvalue("TextID", "_bcdefghilmno16");
        Iom_jObject objA2=new Iom_jObject(CLASSA, OID2);
        objA2.setattrvalue("TextID", "bcdefg_hilmno16");
        Iom_jObject objA3=new Iom_jObject(CLASSA, OID3);
        objA3.setattrvalue("TextID", "bcdefg1hilmno16");
        Iom_jObject objA4=new Iom_jObject(CLASSA, OID4);
        objA4.setattrvalue("TextID", "bcdefg-hilmno16");
        Iom_jObject objA5=new Iom_jObject(CLASSA, OID5);
        objA5.setattrvalue("TextID", "bcdefg.hilmno16");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC3,BID));
        validator.validate(new ObjectEvent(objA1));
        validator.validate(new ObjectEvent(objA2));
        validator.validate(new ObjectEvent(objA3));
        validator.validate(new ObjectEvent(objA4));
        validator.validate(new ObjectEvent(objA5));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void textOidClassValue_Ok() throws Exception {
        Iom_jObject objB1=new Iom_jObject(CLASSB3, "_bcdefghilmno16");
        Iom_jObject objB2=new Iom_jObject(CLASSB3, "bcdefg_hilmno16");
        Iom_jObject objB3=new Iom_jObject(CLASSB3, "bcdefg1hilmno16");
        Iom_jObject objB4=new Iom_jObject(CLASSB3, "bcdefg-hilmno16");
        Iom_jObject objB5=new Iom_jObject(CLASSB3, "bcdefg.hilmno16");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC3,BID));
        validator.validate(new ObjectEvent(objB1));
        validator.validate(new ObjectEvent(objB2));
        validator.validate(new ObjectEvent(objB3));
        validator.validate(new ObjectEvent(objB4));
        validator.validate(new ObjectEvent(objB5));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
	
	// In diesem Test werden verschiedenen Object ID's getestet.
	// Es darf keine Fehlermeldung ausgegeben werden.
    @Test
    public void validateOid_Ok() throws Exception {
        Iom_jObject objB1=new Iom_jObject(CLASSB, "1");
        Iom_jObject objB2=new Iom_jObject(CLASSB, "x1");
        Iom_jObject objB3=new Iom_jObject(CLASSB, "1b");
        Iom_jObject objB4=new Iom_jObject(CLASSB, "_b1");
        Iom_jObject objB5=new Iom_jObject(CLASSB, "_b1.");
        Iom_jObject objB6=new Iom_jObject(CLASSB, "b1-");
        Iom_jObject objB7=new Iom_jObject(CLASSB, "1b_");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC,BID));
        validator.validate(new ObjectEvent(objB1));
        validator.validate(new ObjectEvent(objB2));
        validator.validate(new ObjectEvent(objB3));
        validator.validate(new ObjectEvent(objB4));
        validator.validate(new ObjectEvent(objB5));
        validator.validate(new ObjectEvent(objB6));
        validator.validate(new ObjectEvent(objB7));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==0);
    }
	
	// Es wird getestet ob die Definition von 2 unterschiedlichen Oid's moeglich ist.
	@Test
	public void differentOid_Ok() throws Exception {
		Iom_jObject objB1=new Iom_jObject(CLASSB, OID1);
		Iom_jObject objB2=new Iom_jObject(CLASSB, OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob die Definition eines Objectes welches die Oid ueber eine Referenz einer Association enthaelt, moeglich ist.
	@Test
	public void definitionOfEmbeddedAssociatianWithoutId_Ok(){
		final String OBJ_B1="o_b1";
		final String OBJ_C1="o_c1";
		Iom_jObject objB1=new Iom_jObject(CLASSB, OBJ_B1);
		Iom_jObject objC1=new Iom_jObject(CLASSC, OBJ_C1);
		objC1.addattrobj("b1", "REF").setobjectrefoid(OBJ_B1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob die Definition einer Association ohne Oid moeglich ist.
	@Test
	public void associatianWithoutId_Ok(){
		final String OBJ_B1="o_b1";
		final String OBJ_C1="o_c1";
		Iom_jObject objB1=new Iom_jObject(CLASSB, OBJ_B1);
		Iom_jObject objC1=new Iom_jObject(CLASSC, OBJ_C1);
		Iom_jObject objBC=new Iom_jObject(ASSOCIATIONB2, null);
		objBC.addattrobj("b2", "REF").setobjectrefoid(OBJ_B1);
		objBC.addattrobj("c2", "REF").setobjectrefoid(OBJ_C1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new ObjectEvent(objBC));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob die Definition einer Association mit einer Oid moeglich ist.
	@Test
	public void associatianWithId_Ok(){
		final String OBJ_B1="o_b1";
		final String OBJ_C1="o_c1";
		Iom_jObject objB1=new Iom_jObject(CLASSB, OBJ_B1);
		Iom_jObject objC1=new Iom_jObject(CLASSC, OBJ_C1);
		Iom_jObject objBC=new Iom_jObject(ASSOCIATIONB3, "bc1");
		objBC.addattrobj("b3", "REF").setobjectrefoid(OBJ_B1);
		objBC.addattrobj("c3", "REF").setobjectrefoid(OBJ_C1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new ObjectEvent(objBC));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob die Definition einer Association mit einer uuoid als Oid moeglich ist.
	@Test
	public void associatianWithOidUUOID_Ok(){
		final String OBJ_B1="o_b1";
		final String OBJ_C1="o_c1";
		Iom_jObject objB1=new Iom_jObject(CLASSB, OBJ_B1);
		Iom_jObject objC1=new Iom_jObject(CLASSC, OBJ_C1);
		Iom_jObject objBC=new Iom_jObject(ASSOCIATIONB4, "123e4567-e89b-12d3-a456-426655440000");
		objBC.addattrobj("b4", "REF").setobjectrefoid(OBJ_B1);
		objBC.addattrobj("c4", "REF").setobjectrefoid(OBJ_C1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new ObjectEvent(objBC));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob eine Klasse mit einer Oid, eine BAG einer Struktur erstellen kann.
	@Test
	public void struct_Ok(){
		final String OBJ_B1="o_b1";
		Iom_jObject objB1=new Iom_jObject(CLASSB, OBJ_B1);
		objB1.addattrobj("attrB2", STRUCTA);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	//#############################################################//
	//######################### FAIL ##############################//
	//#############################################################//
	
    // Als Syntax gibt es ein Fehler beim Object ID.
    // Da die OID " o1" ist Falsch als Syntax, muss eine Fehlermeldung ausgegeben werden.
    @Test
    public void validateOid_Fail() throws Exception {
        Iom_jObject objB1=new Iom_jObject(CLASSB, " o1");
        Iom_jObject objB2=new Iom_jObject(CLASSB, OID2);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC,BID));
        validator.validate(new ObjectEvent(objB1));
        validator.validate(new ObjectEvent(objB2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("value < o1> is not a valid OID", logger.getErrs().get(0).getEventMsg());
    }
	
    // Da die OID hat kein Wert, muss eine Fehlermeldung ausgegeben werden.
    @Test
    public void validateOidNull_Fail() throws Exception {
        Iom_jObject objB1=new Iom_jObject(CLASSB, null);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC,BID));
        validator.validate(new ObjectEvent(objB1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("Class Oid23.Topic.ClassB has to have an OID", logger.getErrs().get(0).getEventMsg());
    }
	
	// Es wird getestet ob die Definition von zwei gleichen Klassen und oids moeglich ist.
	@Test
	public void duplicateOidsOfSameTable_Fail() throws Exception {
		final String OBJ_B1="o_b1";
		Iom_jObject objB1=new Iom_jObject(CLASSB, OBJ_B1);
		Iom_jObject objB2=new Iom_jObject(CLASSB, OBJ_B1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("OID o_b1 of object Oid23.Topic.ClassB already exists in CLASS Oid23.Topic.ClassB.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die Definition von einer gleichen Oid in verschiedenen Tables moeglich ist.
	@Test
	public void duplicateOidDifferentTable_Fail() throws Exception {
		final String OBJ_B1="o_b1";
		Iom_jObject objB1=new Iom_jObject(CLASSB, OBJ_B1);
		Iom_jObject objB2=new Iom_jObject(CLASSC, OBJ_B1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("OID o_b1 of object Oid23.Topic.ClassC already exists in CLASS Oid23.Topic.ClassB.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die Definition einer undefinierten oid welche zu einer Klasse refereziert wird, moeglich sein kann.
	@Test
	public void undefinedOid_Fail(){
		final String OBJ_B1="o_b1";
		final String OBJ_B2=null;
		Iom_jObject objB1=new Iom_jObject(CLASSB, OBJ_B1);
		Iom_jObject objB2=new Iom_jObject(CLASSB, OBJ_B2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Class Oid23.Topic.ClassB has to have an OID", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob eine Association ohne Oid erstellt werden kann, wenn diese Stand Alone ist.
	@Test
	public void associatianWithId_Fail(){
		final String OBJ_B1="o_b1";
		final String OBJ_C1="o_c1";
		Iom_jObject objB1=new Iom_jObject(CLASSB, OBJ_B1);
		Iom_jObject objC1=new Iom_jObject(CLASSC, OBJ_C1);
		Iom_jObject objBC=new Iom_jObject(ASSOCIATIONB3, null);
		objBC.addattrobj("b3", "REF").setobjectrefoid(OBJ_B1);
		objBC.addattrobj("c3", "REF").setobjectrefoid(OBJ_C1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new ObjectEvent(objBC));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Association Oid23.Topic.bc3 has to have an OID", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob eine Association ohne Oid erstellt werden kann, wenn diese Stand Alone ist.
	@Test
	public void associatianWithOid_Fail(){
		final String OBJ_B1="o_b1";
		final String OBJ_C1="o_c1";
		Iom_jObject objB1=new Iom_jObject(CLASSB, OBJ_B1);
		Iom_jObject objC1=new Iom_jObject(CLASSC, OBJ_C1);
		Iom_jObject objBC=new Iom_jObject(ASSOCIATIONB4, null);
		objBC.addattrobj("b4", "REF").setobjectrefoid(OBJ_B1);
		objBC.addattrobj("c4", "REF").setobjectrefoid(OBJ_C1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new ObjectEvent(objBC));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Association Oid23.Topic.bc4 has to have an OID", logger.getErrs().get(0).getEventMsg());
	}
    
    @Test
    public void validateOidFirstLetterisStartWithNull_Fail() throws Exception {
        Iom_jObject objB1=new Iom_jObject(CLASSB, " 1abcdefghilmno16");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC,BID));
        validator.validate(new ObjectEvent(objB1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("value < 1abcdefghilmno16> is not a valid OID", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void validateOidThereisASpaceInSentences_Fail() throws Exception {
        Iom_jObject objB1=new Iom_jObject(CLASSB, "abcdefg hilmno16");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC,BID));
        validator.validate(new ObjectEvent(objB1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("value <abcdefg hilmno16> is not a valid OID", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void textOidAttrValueStartWithNumber_Fail() throws Exception {
        Iom_jObject objB1=new Iom_jObject(CLASSA, OID1);
        objB1.setattrvalue("TextID", "1bcdefg_hilmno16");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC3,BID));
        validator.validate(new ObjectEvent(objB1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("value <1bcdefg_hilmno16> is not a valid OID", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void textOidAttrValueStartWithSpace_Fail() throws Exception {
        Iom_jObject objB1=new Iom_jObject(CLASSA, OID1);
        objB1.setattrvalue("TextID", " bcdefg_hilmno16");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC3,BID));
        validator.validate(new ObjectEvent(objB1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("value < bcdefg_hilmno16> is not a valid OID", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void textOidClassValueStartWithNumber_Fail() throws Exception {
        Iom_jObject objB1=new Iom_jObject(CLASSB3, "1bcdefg_hilmno16");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC3,BID));
        validator.validate(new ObjectEvent(objB1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("value <1bcdefg_hilmno16> is not a valid OID", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void textOidClassValueStartWithMinus_Fail() throws Exception {
        Iom_jObject objB1=new Iom_jObject(CLASSB3, "-bcdefg_hilmno16");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC3,BID));
        validator.validate(new ObjectEvent(objB1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("value <-bcdefg_hilmno16> is not a valid OID", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void textOidClassValueStartWithSpace_Fail() throws Exception {
        Iom_jObject objB1=new Iom_jObject(CLASSB3, " bcdefg_hilmno16");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC3,BID));
        validator.validate(new ObjectEvent(objB1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("value < bcdefg_hilmno16> is not a valid OID", logger.getErrs().get(0).getEventMsg());
    }

}