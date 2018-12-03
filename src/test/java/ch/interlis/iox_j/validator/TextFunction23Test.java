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

public class TextFunction23Test {

    // TRANSFER DESCRIPTION
    private TransferDescription td = null;

    // START BASKET EVENT
    private final static String BID1 = "b1";

    // MODEL
    private final static String TOPIC = "TextFunction23.Topic";

    // OID
    private final static String OID1 = "o1";

    // CLASS
    private final static String CLASSA1 = TOPIC + ".ClassA1";
    private final static String CLASSA2 = TOPIC + ".ClassA2";
    private final static String CLASSA3 = TOPIC + ".ClassA3";
    private final static String CLASSA4 = TOPIC + ".ClassA4";
    private final static String CLASSA5 = TOPIC + ".ClassA5";
    private final static String CLASSA5_2 = TOPIC + ".ClassA5_2";
    private final static String CLASSA6 = TOPIC + ".ClassA6";
    private final static String CLASSA7 = TOPIC + ".ClassA7";
    private final static String CLASSA8 = TOPIC + ".ClassA8";
    private final static String CLASSA9 = TOPIC + ".ClassA9";
    private final static String CLASSA10 = TOPIC + ".ClassA10";
    private final static String CLASSA11 = TOPIC + ".ClassA11";
    private final static String CLASSA12 = TOPIC + ".ClassA12";
    private final static String CLASSA13 = TOPIC + ".ClassA13";

    // ATTR
    private final static String ATTR1 = "attr1";
    private final static String ATTR2 = "attr2";
    private final static String ATTR3 = "attr3";

    // #############################################################//
    // ######################### GENEREL ###########################//
    // #############################################################//

    @Before
    public void setUp() throws Exception {
        // ili-datei lesen
        Configuration ili2cConfig = new Configuration();
        {
            FileEntry fileEntry = new FileEntry("src/test/data/validator/Text.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
        }
        {
            FileEntry fileEntry = new FileEntry("src/test/data/validator/TextFunction23.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
        }
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }

    @Test
    public void compareToIgnoreCase_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA1, OID1);
        iomObjA.setattrvalue(ATTR1, "abcdefg");
        iomObjA.setattrvalue(ATTR2, "ABCDEFG");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void concat_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA2, OID1);
        iomObjA.setattrvalue(ATTR1, "concat");
        iomObjA.setattrvalue(ATTR2, "CONCAT");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void endsWith_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA3, OID1);
        iomObjA.setattrvalue(ATTR1, "Das ist eine Test Klasse");
        iomObjA.setattrvalue(ATTR2, "Klasse");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void equalsIgnoreCase_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA4, OID1);
        iomObjA.setattrvalue(ATTR1, "equals");
        iomObjA.setattrvalue(ATTR2, "EQUALS");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void indexOf_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA5, OID1);
        iomObjA.setattrvalue(ATTR1, "aBcdef");
        iomObjA.setattrvalue(ATTR2, "B");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void indexOfWithFromIndexParameter_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA5_2, OID1);
        iomObjA.setattrvalue(ATTR1, "abcdef");
        iomObjA.setattrvalue(ATTR2, "e");
        iomObjA.setattrvalue(ATTR3, "2");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size() == 0);
    }
    
    @Test
    public void lastIndexOf_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA6, OID1);
        iomObjA.setattrvalue(ATTR1, "aaaaabbbb");
        iomObjA.setattrvalue(ATTR2, "a");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void matches_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA7, OID1);
        iomObjA.setattrvalue(ATTR1, "Java String Methods");
        iomObjA.setattrvalue(ATTR2, "(.*)String(.*)");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void replace_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA8, OID1);
        iomObjA.setattrvalue(ATTR1, "abcde");
        iomObjA.setattrvalue(ATTR2, "c");
        iomObjA.setattrvalue(ATTR3, "b");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void startsWith_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA9, OID1);
        iomObjA.setattrvalue(ATTR1, "start with");
        iomObjA.setattrvalue(ATTR2, "start");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void substring_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA10, OID1);
        iomObjA.setattrvalue(ATTR1, "das ist ein Text");
        iomObjA.setattrvalue(ATTR2, "4");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void substringWithFromAndTOParameter_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA11, OID1);
        iomObjA.setattrvalue(ATTR1, "das ist ein Text");
        iomObjA.setattrvalue(ATTR2, "4");
        iomObjA.setattrvalue(ATTR3, "7");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
        
    @Test
    public void toLowerCase_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA12, OID1);
        iomObjA.setattrvalue(ATTR1, "ABC IS NOT EQUAL TO XYZ");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void toUpperCase_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA13, OID1);
        iomObjA.setattrvalue(ATTR1, "this is a test string");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void concat_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA2, OID1);
        iomObjA.setattrvalue(ATTR1, "concat");
        iomObjA.setattrvalue(ATTR2, "xxx");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint TextFunction23.Topic.ClassA2.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void endsWith_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA3, OID1);
        iomObjA.setattrvalue(ATTR1, "Das ist eine Test Klasse");
        iomObjA.setattrvalue(ATTR2, "Test");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint TextFunction23.Topic.ClassA3.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void equalsIgnoreCase_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA4, OID1);
        iomObjA.setattrvalue(ATTR1, "equals");
        iomObjA.setattrvalue(ATTR2, "EQUAL");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint TextFunction23.Topic.ClassA4.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void indexOf_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA5, OID1);
        iomObjA.setattrvalue(ATTR1, "aBcdef");
        iomObjA.setattrvalue(ATTR2, "b");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint TextFunction23.Topic.ClassA5.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void lastIndexOf_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA6, OID1);
        iomObjA.setattrvalue(ATTR1, "aaaabbbbb");
        iomObjA.setattrvalue(ATTR2, "a");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint TextFunction23.Topic.ClassA6.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void matches_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA7, OID1);
        iomObjA.setattrvalue(ATTR1, "Java String Methods");
        iomObjA.setattrvalue(ATTR2, "(.*)Strings(.*)");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint TextFunction23.Topic.ClassA7.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }    
    
    @Test
    public void replace_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA8, OID1);
        iomObjA.setattrvalue(ATTR1, "abcde");
        iomObjA.setattrvalue(ATTR2, "c");
        iomObjA.setattrvalue(ATTR3, "f");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint TextFunction23.Topic.ClassA8.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void startsWith_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA9, OID1);
        iomObjA.setattrvalue(ATTR1, "start with");
        iomObjA.setattrvalue(ATTR2, "starts");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint TextFunction23.Topic.ClassA9.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void substring_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA10, OID1);
        iomObjA.setattrvalue(ATTR1, "das ist ein Text");
        iomObjA.setattrvalue(ATTR2, "5");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint TextFunction23.Topic.ClassA10.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void substringWithFromAndTOParameter_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA11, OID1);
        iomObjA.setattrvalue(ATTR1, "das ist ein Text");
        iomObjA.setattrvalue(ATTR2, "4");
        iomObjA.setattrvalue(ATTR3, "6");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint TextFunction23.Topic.ClassA11.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
}
