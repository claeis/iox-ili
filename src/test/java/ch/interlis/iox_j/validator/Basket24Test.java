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
import ch.interlis.iox.IoxValidationDataPool;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

public class Basket24Test {
	private final static String TOPIC_A="Basket24.TopicA";
    private final static String TOPIC_B="Basket24.TopicB";
    private final static String TOPIC_C="Basket24.TopicC";

	private TransferDescription td=null;

		
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry iliFile=new FileEntry("src/test/data/validator/Basket24.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(iliFile);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}

    @Test
    public void validateAllTopics_Ok() throws Exception {
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_A,"b1"));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent(TOPIC_B,"b2"));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent(TOPIC_C,"b3"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0,logger.getErrs().size());
	}
    @Test
    public void validateMandatoryTopics_Ok() throws Exception {
        ValidationConfig modelConfig=new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.MANDATORY_BASKETS,TOPIC_A);
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_A,"b1"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0,logger.getErrs().size());
    }
    @Test
    public void validateMandatoryTopics_Fail() throws Exception {
        ValidationConfig modelConfig=new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.MANDATORY_BASKETS,TOPIC_A+";"+TOPIC_B);
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_A,"b1"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1,logger.getErrs().size());
        assertEquals("basket Basket24.TopicB is mandatory in transfer", logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void validateBannedTopics_Fail() throws Exception {
        ValidationConfig modelConfig=new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.BANNED_BASKETS,TOPIC_B+";"+TOPIC_C);
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_C,"b2"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1,logger.getErrs().size());
        assertEquals("basket Basket24.TopicC is banned from transfer", logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void validateBannedTopics_Ok() throws Exception {
        ValidationConfig modelConfig=new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.BANNED_BASKETS,TOPIC_B+";"+TOPIC_C);
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_A,"b1"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0,logger.getErrs().size());
    }
    @Test
    public void validateOptionalTopics_Ok() throws Exception {
        ValidationConfig modelConfig=new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.OPTIONAL_BASKETS,TOPIC_A+";"+TOPIC_B);
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_B,"b2"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0,logger.getErrs().size());
    }
    @Test
    public void validateOptionalTopics_Fail() throws Exception {
        ValidationConfig modelConfig=new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.OPTIONAL_BASKETS,TOPIC_A+";"+TOPIC_B);
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_A,"b1"));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent(TOPIC_C,"b2"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1,logger.getErrs().size());
        assertEquals("basket Basket24.TopicC is banned from transfer", logger.getErrs().get(0).getEventMsg());
    }
}