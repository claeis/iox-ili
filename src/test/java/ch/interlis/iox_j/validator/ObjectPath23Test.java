package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
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

public class ObjectPath23Test {
    // OID
    private final static String OID1 = "o1";
    private final static String OID2 = "o2";

    // MODEL.TOPIC
    private final static String TOPIC = "ObjectPath23.Topic";

    // CLASSES
    private final static String CLASSB = TOPIC + ".ClassB";
    private final static String CLASSE = TOPIC + ".ClassE";
    private final static String CLASSF = TOPIC + ".ClassF";
    private final static String CLASSG = TOPIC + ".ClassG";
    private final static String CLASSH = TOPIC + ".ClassH";
    private final static String CLASSC1 = TOPIC + ".ClassC1";
    
    // ASSOCIATION
    private static final String ASSOC_MANY2MANY = TOPIC + ".many2many";

    // STRUCTURE
    private final static String STRUCTD1 = TOPIC + ".StructD1";
    private final static String STRUCTS = TOPIC + ".StructS";
    private final static String STRUCTS3 = TOPIC + ".SubSubStruct";

    // TD
    private TransferDescription td = null;

    // START EVENT BASKET
    private final static String BID = "b1";

    @Before
    public void setUp() throws Exception {
        // ili-datei lesen
        Configuration ili2cConfig = new Configuration();
        FileEntry iliFile = new FileEntry("src/test/data/validator/ObjectPath23.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(iliFile);
        td = ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    
    //############################################################/
    //########## SUCCESSFUL TESTS ################################/
    //############################################################/
    
    @Test
    public void simpleTypeAttribute_OK() throws Exception {
        Iom_jObject objB = new Iom_jObject(CLASSB, OID1);
        Iom_jObject structD1 = (Iom_jObject) objB.addattrobj("attrB2", STRUCTD1);
        structD1.setattrvalue("attrD1", "REF");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(objB));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }    
    
    @Test
    public void simpleTypeListAttribute_OK() throws Exception {
        Iom_jObject objB = new Iom_jObject(CLASSB, OID1);
        Iom_jObject structD1 = (Iom_jObject) objB.addattrobj("attrB3", STRUCTD1);
        structD1.setattrvalue("attrD1", "REF");
        Iom_jObject structD2 = (Iom_jObject) objB.addattrobj("attrB3", STRUCTD1);
        structD2.setattrvalue("attrD1", "REF");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(objB));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }    
    
    @Test
    public void coord_OK() throws Exception {
        Iom_jObject objC1 = new Iom_jObject(CLASSC1, OID2);
        objC1.setattrvalue("attrC1", "C1");

        Iom_jObject objE1 = new Iom_jObject(CLASSF, OID1);
        Iom_jObject objStructD1 = (Iom_jObject) objE1.addattrobj("attrF2", STRUCTD1);
        Iom_jObject objStructS3 = (Iom_jObject) objStructD1.addattrobj("attrD5", "COORD");
        objStructS3.setattrvalue("C1", "5.000");
        objStructS3.setattrvalue("C2", "8.000");

        Iom_jObject objStructS4 = (Iom_jObject) objStructD1.addattrobj("attrD6", "COORD");
        objStructS4.setattrvalue("C1", "5.000");
        objStructS4.setattrvalue("C2", "8.000");

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(objE1));
        validator.validate(new ObjectEvent(objC1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void refernceAttribute_OK() throws Exception {
        Iom_jObject objC1 = new Iom_jObject(CLASSC1, OID2);
        objC1.setattrvalue("attrC1", "REF");

        Iom_jObject objG = new Iom_jObject(CLASSG, OID1);
        Iom_jObject structD1 = (Iom_jObject) objG.addattrobj("attrB2", STRUCTD1);
        Iom_jObject referenceToC1 = (Iom_jObject) structD1.addattrobj("attrD2", "REF");
        referenceToC1.setobjectrefoid(objC1.getobjectoid());
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(objG));
        validator.validate(new ObjectEvent(objC1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void assoc_one2many_OK() throws Exception {
        Iom_jObject iomObjH = new Iom_jObject(CLASSH, OID1);
        Iom_jObject iomObjC1 = new Iom_jObject(CLASSC1, OID2);
        iomObjC1.addattrobj("roleH_1", "REF").setobjectrefoid(iomObjH.getobjectoid());
        iomObjH.setattrvalue("attrH1", "H1");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(iomObjH));
        validator.validate(new ObjectEvent(iomObjC1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void assoc_many2many_OK() throws Exception {
        Iom_jObject iomObjH = new Iom_jObject(CLASSH, OID1);
        Iom_jObject iomObjC1 = new Iom_jObject(CLASSC1, OID2);
        iomObjH.setattrvalue("attrH1", "H1");
        Iom_jObject iomObjmany2many = new Iom_jObject(ASSOC_MANY2MANY, null);
        iomObjmany2many.addattrobj("roleH_2", "REF").setobjectrefoid(iomObjH.getobjectoid());
        iomObjmany2many.addattrobj("roleC_2", "REF").setobjectrefoid(iomObjC1.getobjectoid());

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(iomObjH));
        validator.validate(new ObjectEvent(iomObjC1));
        validator.validate(new ObjectEvent(iomObjmany2many));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void structure_OK() throws Exception {
        Iom_jObject objE1 = new Iom_jObject(CLASSE, OID1);
        Iom_jObject objStructD1 = (Iom_jObject) objE1.addattrobj("attrE2", STRUCTD1);
        Iom_jObject objStructS3 = (Iom_jObject) objStructD1.addattrobj("attrD3", STRUCTS);
        Iom_jObject objStructS8 = (Iom_jObject) objStructS3.addattrobj("attrS3", STRUCTS3);
        objStructS8.setattrvalue("attrS8", "S1");
        
        Iom_jObject objStructS4 = (Iom_jObject) objStructD1.addattrobj("attrD4", STRUCTS);
        Iom_jObject objStructS9 = (Iom_jObject) objStructS4.addattrobj("attrS3", STRUCTS3);
        objStructS9.setattrvalue("attrS8", "S1");

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(objE1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void structure_OK_AttrValues_NULL() throws Exception {
        Iom_jObject objE1 = new Iom_jObject(CLASSE, OID1);
        Iom_jObject objStructD1 = (Iom_jObject) objE1.addattrobj("attrE2", STRUCTD1);
        Iom_jObject objStructS3 = (Iom_jObject) objStructD1.addattrobj("attrD3", STRUCTS);
        objStructS3.setattrvalue("attrS1", "");

        Iom_jObject objStructS4 = (Iom_jObject) objStructD1.addattrobj("attrD4", STRUCTS);
        objStructS4.setattrvalue("attrS1", "");

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(objE1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    //############################################################/
    //########## FAILING TESTS ###################################/
    //############################################################/    

    @Test
    public void simpleTypeAttribute_Fail() throws Exception {
        Iom_jObject objB = new Iom_jObject(CLASSB, OID1);
        Iom_jObject structD1 = (Iom_jObject) objB.addattrobj("attrB2", STRUCTD1);
        structD1.setattrvalue("attrD1", "C1");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(objB));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint ObjectPath23.Topic.ClassB.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void simpleTypeListAttribute_Fail() throws Exception {
        Iom_jObject objB = new Iom_jObject(CLASSB, OID1);
        Iom_jObject structD1 = (Iom_jObject) objB.addattrobj("attrB2", STRUCTD1);
        structD1.setattrvalue("attrD1", "REF");
        Iom_jObject structD2 = (Iom_jObject) objB.addattrobj("attrB3", STRUCTD1);
        structD2.setattrvalue("attrD1", "D2");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(objB));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1,logger.getErrs().size());
        assertEquals("Mandatory Constraint ObjectPath23.Topic.ClassB.Constraint2 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void structure_Fail() throws Exception {
        Iom_jObject objE1 = new Iom_jObject(CLASSE, OID1);
        Iom_jObject objStructD1 = (Iom_jObject) objE1.addattrobj("attrE2", STRUCTD1);
        Iom_jObject objStructS3 = (Iom_jObject) objStructD1.addattrobj("attrD3", STRUCTS);
        Iom_jObject objStructS8 = (Iom_jObject) objStructS3.addattrobj("attrS3", STRUCTS3);
        objStructS8.setattrvalue("attrS8", "S1");

        Iom_jObject objStructS4 = (Iom_jObject) objStructD1.addattrobj("attrD4", STRUCTS);
        Iom_jObject objStructS9 = (Iom_jObject) objStructS4.addattrobj("attrS3", STRUCTS3);
        objStructS9.setattrvalue("attrS8", "S2");       

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(objE1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size() == 1);
        assertEquals("Mandatory Constraint ObjectPath23.Topic.ClassE.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void structure_Fail_AttrValue_NULL() throws Exception {
        Iom_jObject objE1 = new Iom_jObject(CLASSE, OID1);
        Iom_jObject objStructD1 = (Iom_jObject) objE1.addattrobj("attrE2", STRUCTD1);
        Iom_jObject objStructS3 = (Iom_jObject) objStructD1.addattrobj("attrD3", STRUCTS);
        objStructS3.setattrvalue("attrS1", "S1");

        Iom_jObject objStructS4 = (Iom_jObject) objStructD1.addattrobj("attrD4", STRUCTS);
        objStructS4.setattrvalue("attrS2", "");

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(objE1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size() == 1);
        assertEquals("Mandatory Constraint ObjectPath23.Topic.ClassE.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void coord_Fail() throws Exception {
        Iom_jObject objC1 = new Iom_jObject(CLASSC1, OID2);
        objC1.setattrvalue("attrC1", "C1");

        Iom_jObject objE1 = new Iom_jObject(CLASSF, OID1);
        Iom_jObject objStructD1 = (Iom_jObject) objE1.addattrobj("attrF2", STRUCTD1);
        Iom_jObject objStructS3 = (Iom_jObject) objStructD1.addattrobj("attrD5", "COORD");
        objStructS3.setattrvalue("C1", "5.000");
        objStructS3.setattrvalue("C2", "8.000");

        Iom_jObject objStructS4 = (Iom_jObject) objStructD1.addattrobj("attrD6", "COORD");
        objStructS4.setattrvalue("C1", "5.000");
        objStructS4.setattrvalue("C2", "9.000");

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(objE1));
        validator.validate(new ObjectEvent(objC1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size() == 1);
        assertEquals("Mandatory Constraint ObjectPath23.Topic.ClassF.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void refernceAttribute_Fail() throws Exception {
        Iom_jObject objC1 = new Iom_jObject(CLASSC1, OID2);
        objC1.setattrvalue("attrC1", "C1");

        Iom_jObject objG = new Iom_jObject(CLASSG, OID1);
        Iom_jObject structD1 = (Iom_jObject) objG.addattrobj("attrB2", STRUCTD1);
        Iom_jObject referenceToC1 = (Iom_jObject) structD1.addattrobj("attrD2", "REF");
        referenceToC1.setobjectrefoid(objC1.getobjectoid());
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(objG));
        validator.validate(new ObjectEvent(objC1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size() == 1);
        assertEquals("Mandatory Constraint ObjectPath23.Topic.ClassG.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void assoc_one2many_Fail() throws Exception {
        Iom_jObject iomObjH = new Iom_jObject(CLASSH, OID1);
        Iom_jObject iomObjC1 = new Iom_jObject(CLASSC1, OID2);
        iomObjC1.addattrobj("roleH_1", "REF").setobjectrefoid(iomObjH.getobjectoid());
        iomObjH.setattrvalue("attrH1", "H9");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(iomObjH));
        validator.validate(new ObjectEvent(iomObjC1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint ObjectPath23.Topic.ClassC1.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void assoc_many2many_Fail() throws Exception {
        Iom_jObject iomObjH = new Iom_jObject(CLASSH, OID1);
        Iom_jObject iomObjC1 = new Iom_jObject(CLASSC1, OID2);
        iomObjH.setattrvalue("attrH1", "H9");
        Iom_jObject iomObjmany2many = new Iom_jObject(ASSOC_MANY2MANY, null);
        iomObjmany2many.addattrobj("roleH_2", "REF").setobjectrefoid(iomObjH.getobjectoid());
        iomObjmany2many.addattrobj("roleC_2", "REF").setobjectrefoid(iomObjC1.getobjectoid());

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(iomObjH));
        validator.validate(new ObjectEvent(iomObjC1));
        validator.validate(new ObjectEvent(iomObjmany2many));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint ObjectPath23.Topic.many2many.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
}