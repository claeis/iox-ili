package ch.interlis.iox_j.validator;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Translation24Test {
    private final static String ILI_DE_MODEL = "Translation24_de";
    private final static String ILI_FR_MODEL = "Translation24_fr";
    private final static String ILI_DE_TOPIC_A = ILI_DE_MODEL + ".TestA_de";
    private final static String ILI_DE_CLASS_A1 = ILI_DE_TOPIC_A + ".ClassA1_de";
    private final static String ILI_FR_TOPIC_A = ILI_FR_MODEL + ".TestA_fr";
    private final static String ILI_FR_CLASS_A1 = ILI_FR_TOPIC_A + ".ClassA1_fr";
    private final static String ILI_DE_TOPIC_B = ILI_DE_MODEL + ".TestB_de";
    private final static String ILI_DE_STRUCT_B0 = ILI_DE_TOPIC_B + ".StructB0_de";
    private final static String ILI_DE_CLASS_B1 = ILI_DE_TOPIC_B + ".ClassB1_de";
    private final static String ILI_FR_TOPIC_B = ILI_FR_MODEL + ".TestB_fr";
    private final static String ILI_FR_STRUCT_B0 = ILI_FR_TOPIC_B + ".StructB0_fr";
    private final static String ILI_FR_CLASS_B1 = ILI_FR_TOPIC_B + ".ClassB1_fr";
    private final static String ILI_DE_ROLE_A = "a_de";
    private final static String ILI_FR_ROLE_A = "a_fr";
    private final static String ILI_DE_REF_A = "refA_de";
    private final static String ILI_FR_REF_A = "refA_fr";
    private final static String ILI_DE_ATTRREF = "attrRef_de";
    private final static String ILI_FR_ATTRREF = "attrRef_fr";
    private TransferDescription td = null;

    @Before
    public void setUp() throws Exception {
        // ili-datei lesen
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry("src/test/data/validator/Translation24.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        td = ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        assertNotNull(td);
    }

    @Test
    public void validateTranslatedBasket_Ok() {
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_DE_TOPIC_A, "b1"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    @Test
    public void validateTranslatedBasket_Fail() {
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_FR_TOPIC_A, "b1"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("INTERLIS 2.4 transfers must be in the base language. The basket Translation24_fr.TestA_fr has language (fr), the base has (de)", logger.getErrs().get(0).getEventMsg());
    }
}
