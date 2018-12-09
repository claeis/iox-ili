package ch.interlis.iox_j.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

public class Topic23Test {

    // MODEL.TOPIC
    private final static String TOPIC = "Topic23.Topic";
    private final static String TOPIC_UUID_OID = "Topic23.TopicUuidOid";
    private final static String TOPIC_STANDART_OID = "Topic23.TopicStandartOID";
    private final static String TOPIC_TEXT_OID = "Topic23.TopicTextOID";
    
    // START EVENT BASKET
    private final static String BID = "b1";
    private final static String BID_UUID_OID = "c91766ee-ff86-4d5a-8cbf-9fb5620a4ee4";
    private final static String BID_STANDART_OID = "deg5mQXX2000004a";
    private final static String BID_TEXT_OID = "bcdefg1hilmno16";
    
    // TD
    private TransferDescription td = null;
    
    @Before
    public void setUp() throws Exception {
        Configuration ili2cConfig = new Configuration();
        FileEntry iliFile = new FileEntry("src/test/data/validator/Topic23.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(iliFile);
        td = ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    
    //#########################################################//
    //####################### SUCCESS #########################//
    //#########################################################//

    @Test
    public void bidUuidOid_OK() throws Exception {
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_UUID_OID, BID_UUID_OID));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void bidStandartOid_OK() throws Exception {
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_STANDART_OID, BID_STANDART_OID));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void bidTextOid_OK() throws Exception {
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_TEXT_OID, BID_TEXT_OID));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void basketElementName_OK() throws Exception {
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    //#############################################################//
    //######################### FAIL ##############################//
    //#############################################################//
    
    @Test
    public void bidStandartOidLength16Character_Fail() throws Exception {
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_STANDART_OID, BID_STANDART_OID + "x"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("value <deg5mQXX2000004ax> is not a valid BID", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void bidStandartOidStartWithSpace_Fail() throws Exception {
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_STANDART_OID, " eg5mQXX2000004a"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("value < eg5mQXX2000004a> is not a valid BID", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void bidStandartOidStartWithNumber_Fail() throws Exception {
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_STANDART_OID, "1eg5mQXX2000004a"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("value <1eg5mQXX2000004a> is not a valid BID", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void bidTextOidStartWithNumber_Fail() throws Exception {
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_TEXT_OID, "1eg5mQXX2000004a"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("value <1eg5mQXX2000004a> is not a valid BID", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void bidTextOidStartWithSpace_Fail() throws Exception {
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_TEXT_OID, " eg5mQXX2000004a"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("value < eg5mQXX2000004a> is not a valid BID", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void bidTextOidStartWithMinus_Fail() throws Exception {
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_TEXT_OID, "-eg5mQXX2000004a"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("value <-eg5mQXX2000004a> is not a valid BID", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void bidUuidOidLength36Character_Fail() throws Exception {
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_UUID_OID, BID_UUID_OID + "x"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts 
        assertEquals(1, logger.getErrs().size());
        assertEquals("value <c91766ee-ff86-4d5a-8cbf-9fb5620a4ee4x> is not a valid BID", logger.getErrs().get(0).getEventMsg());
    }    
    
    @Test
    public void bidUuidOidStartWithSpace_Fail() throws Exception {
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_UUID_OID, " 91766ee-ff86-4d5a-8cbf-9fb5620a4ee4"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts 
        assertEquals(1, logger.getErrs().size());
        assertEquals("value < 91766ee-ff86-4d5a-8cbf-9fb5620a4ee4> is not a valid BID", logger.getErrs().get(0).getEventMsg());
    }
    
    
    @Test
    public void basketElementName_Fail() throws Exception {
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC+".x", BID));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Invalid basket element name Topic23.Topic.x", logger.getErrs().get(0).getEventMsg());
    }

}
