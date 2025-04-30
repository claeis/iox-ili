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

public class MathFunction23Test {

    // TRANSFER DESCRIPTION
    private TransferDescription td = null;

    // START BASKET EVENT
    private final static String BID1 = "b1";

    // MODEL
    private final static String TOPIC = "MathFunction23.Topic";

    // OID
    private final static String OID1 = "o1";

    // CLASS
    private final static String CLASSA1 = TOPIC + ".ClassA1";
    private final static String CLASSA2 = TOPIC + ".ClassA2";
    private final static String CLASSA3 = TOPIC + ".ClassA3";
    private final static String CLASSA4 = TOPIC + ".ClassA4";
    private final static String CLASSA5 = TOPIC + ".ClassA5";
    private final static String CLASSA6 = TOPIC + ".ClassA6";
    private final static String CLASSA7 = TOPIC + ".ClassA7";
    private final static String CLASSA8 = TOPIC + ".ClassA8";
    private final static String CLASSA9 = TOPIC + ".ClassA9";
    private final static String CLASSA10 = TOPIC + ".ClassA10";
    private final static String CLASSA11 = TOPIC + ".ClassA11";
    private final static String CLASSA12 = TOPIC + ".ClassA12";
    private final static String CLASSA13 = TOPIC + ".ClassA13";
    private final static String CLASSA14 = TOPIC + ".ClassA14";
    private final static String CLASSA15 = TOPIC + ".ClassA15";
    private final static String CLASSA16 = TOPIC + ".ClassA16";
    private final static String CLASSA17 = TOPIC + ".ClassA17";
    private final static String CLASSA18 = TOPIC + ".ClassA18";
    private final static String CLASSA19 = TOPIC + ".ClassA19";
    private final static String CLASSA20 = TOPIC + ".ClassA20";
    private final static String CLASSA21 = TOPIC + ".ClassA21";
    private final static String CLASSA22 = TOPIC + ".ClassA22";
    private final static String CLASSA23 = TOPIC + ".ClassA23";
    private final static String CLASSA24 = TOPIC + ".ClassA24";
    private final static String CLASSA25 = TOPIC + ".ClassA25";
    private final static String CLASSA26 = TOPIC + ".ClassA26";
    private final static String CLASSB = TOPIC + ".ClassB";
    private final static String CLASSC = TOPIC + ".ClassC";
    private final static String CLASSD = TOPIC + ".ClassD";
    private final static String CLASSE = TOPIC + ".ClassE";
    private final static String CLASSF = TOPIC + ".ClassF";
    
    // STRUCTURE
    private final static String STRUCTA = TOPIC + ".StructA";

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
            FileEntry fileEntry = new FileEntry("src/test/data/validator/Math.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
        }
        {
            FileEntry fileEntry = new FileEntry("src/test/data/validator/MathFunction23.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
        }
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    
    // #########################################################//
    // ####################### SUCCESS #########################//
    // #########################################################//

    @Test
    public void add_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA1, OID1);
        iomObjA.setattrvalue(ATTR1, "1");
        iomObjA.setattrvalue(ATTR2, "2");
        iomObjA.setattrvalue(ATTR3, "3");
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
    public void sub_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA2, OID1);
        iomObjA.setattrvalue(ATTR1, "6");
        iomObjA.setattrvalue(ATTR2, "1");
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
    public void mul_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA3, OID1);
        iomObjA.setattrvalue(ATTR1, "5");
        iomObjA.setattrvalue(ATTR2, "1");
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
    public void div_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA4, OID1);
        iomObjA.setattrvalue(ATTR1, "5");
        iomObjA.setattrvalue(ATTR2, "1");
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
    public void abs_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA5, OID1);
        iomObjA.setattrvalue(ATTR1, "-5");
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
    public void acos_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA6, OID1);
        double attr1 = Math.PI / 2;
        iomObjA.setattrvalue(ATTR1, String.valueOf(attr1));
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
    public void asin_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA7, OID1);
        double attr1 = Math.PI / 2;
        iomObjA.setattrvalue(ATTR1, String.valueOf(attr1));
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
    public void atan_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA8, OID1);
        double attr1 = Math.PI / 2;
        iomObjA.setattrvalue(ATTR1, String.valueOf(attr1));
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
    public void atan2_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA9, OID1);
        double attr1 = Math.PI / 2;
        double attr2 = Math.PI / 3;
        iomObjA.setattrvalue(ATTR1, String.valueOf(attr1));
        iomObjA.setattrvalue(ATTR2, String.valueOf(attr2));
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
    public void cbrt_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA10, OID1);
        iomObjA.setattrvalue(ATTR1, "125");
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
    public void cos_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA11, OID1);
        iomObjA.setattrvalue(ATTR1, "45");
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
    public void cosh_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA12, OID1);
        iomObjA.setattrvalue(ATTR1, "45");
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
    public void exp_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA13, OID1);
        iomObjA.setattrvalue(ATTR1, "5");
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
    public void hypot_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA14, OID1);
        iomObjA.setattrvalue(ATTR1, "60984.1");
        iomObjA.setattrvalue(ATTR2, "-497.99");
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
    public void log_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA15, OID1);
        iomObjA.setattrvalue(ATTR1, "60984.1");
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
    public void log10_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA16, OID1);
        iomObjA.setattrvalue(ATTR1, "60984.1");
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
    public void pow_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA17, OID1);
        iomObjA.setattrvalue(ATTR1, "2.0");
        iomObjA.setattrvalue(ATTR2, "5.4");
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
    public void round_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA18, OID1);
        iomObjA.setattrvalue(ATTR1, "100.675");
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
    public void signum_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA19, OID1);
        iomObjA.setattrvalue(ATTR1, "5");
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
    public void sin_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA20, OID1);
        iomObjA.setattrvalue(ATTR1, "45");
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
    public void sinh_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA21, OID1);
        iomObjA.setattrvalue(ATTR1, "45");
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
    public void sqrt_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA22, OID1);
        iomObjA.setattrvalue(ATTR1, "25");
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
    public void tan_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA23, OID1);
        iomObjA.setattrvalue(ATTR1, "45");
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
    public void tanh_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA24, OID1);
        iomObjA.setattrvalue(ATTR1, "45");
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
    public void max_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA25, OID1);
        iomObjA.setattrvalue(ATTR1, "5");
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
    public void min_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA26, OID1);
        iomObjA.setattrvalue(ATTR1, "7");
        iomObjA.setattrvalue(ATTR2, "3");
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
    public void avg_Ok() {
        Iom_jObject structB1 = new Iom_jObject(STRUCTA, null);
        structB1.setattrvalue("attrA", "6");
        Iom_jObject structB2 = new Iom_jObject(STRUCTA, null);
        structB2.setattrvalue("attrA", "3");
        Iom_jObject structB3 = new Iom_jObject(STRUCTA, null);
        structB3.setattrvalue("attrA", "3");
        Iom_jObject classB = new Iom_jObject(CLASSB, OID1);
        classB.addattrobj("attrb", structB1);
        classB.addattrobj("attrb", structB2);
        classB.addattrobj("attrb", structB3);
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(classB));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void max2_Ok() {
        Iom_jObject structA1 = new Iom_jObject(STRUCTA, null);
        structA1.setattrvalue("attrA", "1");
        Iom_jObject structA2 = new Iom_jObject(STRUCTA, null);
        structA2.setattrvalue("attrA", "3");
        Iom_jObject structA3 = new Iom_jObject(STRUCTA, null);
        structA3.setattrvalue("attrA", "5");
        Iom_jObject structA4 = new Iom_jObject(STRUCTA, null);
        structA4.setattrvalue("attrA", "8");
        Iom_jObject structA5 = new Iom_jObject(STRUCTA, null);
        structA5.setattrvalue("attrA", "12");
        Iom_jObject classC = new Iom_jObject(CLASSC, OID1);
        classC.addattrobj("attrc", structA1);
        classC.addattrobj("attrc", structA2);
        classC.addattrobj("attrc", structA3);
        classC.addattrobj("attrc", structA4);
        classC.addattrobj("attrc", structA5);
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(classC));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void min2_Ok() {
        Iom_jObject structA1 = new Iom_jObject(STRUCTA, null);
        structA1.setattrvalue("attrA", "7");
        Iom_jObject structA2 = new Iom_jObject(STRUCTA, null);
        structA2.setattrvalue("attrA", "9");
        Iom_jObject structA3 = new Iom_jObject(STRUCTA, null);
        structA3.setattrvalue("attrA", "3");
        Iom_jObject structA4 = new Iom_jObject(STRUCTA, null);
        structA4.setattrvalue("attrA", "4");
        Iom_jObject structA5 = new Iom_jObject(STRUCTA, null);
        structA5.setattrvalue("attrA", "5");
        Iom_jObject classD = new Iom_jObject(CLASSD, OID1);
        classD.addattrobj("attrd", structA1);
        classD.addattrobj("attrd", structA2);
        classD.addattrobj("attrd", structA3);
        classD.addattrobj("attrd", structA4);
        classD.addattrobj("attrd", structA5);
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(classD));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void sum_Ok() {
        Iom_jObject structA1 = new Iom_jObject(STRUCTA, null);
        structA1.setattrvalue("attrA", "2");
        Iom_jObject structA2 = new Iom_jObject(STRUCTA, null);
        structA2.setattrvalue("attrA", "4");
        Iom_jObject structA3 = new Iom_jObject(STRUCTA, null);
        structA3.setattrvalue("attrA", "3");
        Iom_jObject structA4 = new Iom_jObject(STRUCTA, null);
        structA4.setattrvalue("attrA", "10");
        Iom_jObject structA5 = new Iom_jObject(STRUCTA, null);
        structA5.setattrvalue("attrA", "1");
        Iom_jObject classE = new Iom_jObject(CLASSE, OID1);
        classE.addattrobj("attre", structA1);
        classE.addattrobj("attre", structA2);
        classE.addattrobj("attre", structA3);
        classE.addattrobj("attre", structA4);
        classE.addattrobj("attre", structA5);
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(classE));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void sum_ZeroObjects() {
        Iom_jObject classF = new Iom_jObject(CLASSF, OID1);
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(classF));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void sum_OneObject() {
        Iom_jObject structA1 = new Iom_jObject(STRUCTA, null);
        structA1.setattrvalue("attrA", "20");
        Iom_jObject classE = new Iom_jObject(CLASSE, OID1);
        classE.addattrobj("attre", structA1);
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(classE));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    // #############################################################//
    // ######################### FAIL ##############################//
    // #############################################################//
    
    @Test
    public void add_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA1, OID1);
        iomObjA.setattrvalue(ATTR1, "2");
        iomObjA.setattrvalue(ATTR2, "3");
        iomObjA.setattrvalue(ATTR3, "4");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA1.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void sub_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA2, OID1);
        iomObjA.setattrvalue(ATTR1, "9");
        iomObjA.setattrvalue(ATTR2, "1");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA2.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void mul_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA3, OID1);
        iomObjA.setattrvalue(ATTR1, "5");
        iomObjA.setattrvalue(ATTR2, "2");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA3.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void div_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA4, OID1);
        iomObjA.setattrvalue(ATTR1, "6");
        iomObjA.setattrvalue(ATTR2, "1");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA4.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void abs_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA5, OID1);
        iomObjA.setattrvalue(ATTR1, "-6");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA5.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void acos_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA6, OID1);
        iomObjA.setattrvalue(ATTR1, String.valueOf(Math.PI / 3));
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA6.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void asin_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA7, OID1);
        double attr1 = Math.PI / 3;
        iomObjA.setattrvalue(ATTR1, String.valueOf(attr1));
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA7.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void atan_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA8, OID1);
        double attr1 = Math.PI / 3;
        iomObjA.setattrvalue(ATTR1, String.valueOf(attr1));
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA8.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void atan2_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA9, OID1);
        double attr1 = Math.PI / 3;
        double attr2 = Math.PI / 3;
        iomObjA.setattrvalue(ATTR1, String.valueOf(attr1));
        iomObjA.setattrvalue(ATTR2, String.valueOf(attr2));
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA9.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void cbrt_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA10, OID1);
        iomObjA.setattrvalue(ATTR1, "64");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA10.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void cos_fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA11, OID1);
        iomObjA.setattrvalue(ATTR1, "60");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA11.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void cosh_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA12, OID1);
        iomObjA.setattrvalue(ATTR1, "49");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA12.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void exp_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA13, OID1);
        iomObjA.setattrvalue(ATTR1, "0.5");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA13.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void hypot_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA14, OID1);
        iomObjA.setattrvalue(ATTR1, "60984.1");
        iomObjA.setattrvalue(ATTR2, "498");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA14.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void log_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA15, OID1);
        iomObjA.setattrvalue(ATTR1, "84.1");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA15.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void log10_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA16, OID1);
        iomObjA.setattrvalue(ATTR1, "60984");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA16.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void pow_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA17, OID1);
        iomObjA.setattrvalue(ATTR1, "5.4");
        iomObjA.setattrvalue(ATTR2, "2.0");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA17.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void round_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA18, OID1);
        iomObjA.setattrvalue(ATTR1, "100.475");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA18.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void signum_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA19, OID1);
        iomObjA.setattrvalue(ATTR1, "-5");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA19.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void sin_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA20, OID1);
        iomObjA.setattrvalue(ATTR1, "-180");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA20.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void sinh_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA21, OID1);
        iomObjA.setattrvalue(ATTR1, "-180");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA21.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void sqrt_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA22, OID1);
        iomObjA.setattrvalue(ATTR1, "9");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA22.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void tan_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA23, OID1);
        iomObjA.setattrvalue(ATTR1, "44");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA23.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void tanh_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA24, OID1);
        iomObjA.setattrvalue(ATTR1, "-180");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA24.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void max_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA25, OID1);
        iomObjA.setattrvalue(ATTR1, "5");
        iomObjA.setattrvalue(ATTR2, "6");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA25.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void min_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA26, OID1);
        iomObjA.setattrvalue(ATTR1, "3");
        iomObjA.setattrvalue(ATTR2, "2");
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
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassA26.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void avg_Fail() {
        Iom_jObject structB1 = new Iom_jObject(STRUCTA, null);
        structB1.setattrvalue("attrA", "6");
        Iom_jObject structB2 = new Iom_jObject(STRUCTA, null);
        structB2.setattrvalue("attrA", "8");
        Iom_jObject structB3 = new Iom_jObject(STRUCTA, null);
        structB3.setattrvalue("attrA", "10");
        Iom_jObject classB = new Iom_jObject(CLASSB, OID1);
        classB.addattrobj("attrb", structB1);
        classB.addattrobj("attrb", structB2);
        classB.addattrobj("attrb", structB3);
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(classB));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassB.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void max2_Fail() {
        Iom_jObject structA1 = new Iom_jObject(STRUCTA, null);
        structA1.setattrvalue("attrA", "1");
        Iom_jObject structA2 = new Iom_jObject(STRUCTA, null);
        structA2.setattrvalue("attrA", "3");
        Iom_jObject structA3 = new Iom_jObject(STRUCTA, null);
        structA3.setattrvalue("attrA", "5");
        Iom_jObject structA4 = new Iom_jObject(STRUCTA, null);
        structA4.setattrvalue("attrA", "8");
        Iom_jObject structA5 = new Iom_jObject(STRUCTA, null);
        structA5.setattrvalue("attrA", "11");
        Iom_jObject classB = new Iom_jObject(CLASSC, OID1);
        classB.addattrobj("attrc", structA1);
        classB.addattrobj("attrc", structA2);
        classB.addattrobj("attrc", structA3);
        classB.addattrobj("attrc", structA4);
        classB.addattrobj("attrc", structA5);
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(classB));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassC.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void min2_Fail() {
        Iom_jObject structA1 = new Iom_jObject(STRUCTA, null);
        structA1.setattrvalue("attrA", "7");
        Iom_jObject structA2 = new Iom_jObject(STRUCTA, null);
        structA2.setattrvalue("attrA", "9");
        Iom_jObject structA3 = new Iom_jObject(STRUCTA, null);
        structA3.setattrvalue("attrA", "10");
        Iom_jObject structA4 = new Iom_jObject(STRUCTA, null);
        structA4.setattrvalue("attrA", "4");
        Iom_jObject structA5 = new Iom_jObject(STRUCTA, null);
        structA5.setattrvalue("attrA", "5");
        Iom_jObject classD = new Iom_jObject(CLASSD, OID1);
        classD.addattrobj("attrd", structA1);
        classD.addattrobj("attrd", structA2);
        classD.addattrobj("attrd", structA3);
        classD.addattrobj("attrd", structA4);
        classD.addattrobj("attrd", structA5);
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(classD));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassD.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void sum_Fail() {
        Iom_jObject structA1 = new Iom_jObject(STRUCTA, null);
        structA1.setattrvalue("attrA", "2");
        Iom_jObject structA2 = new Iom_jObject(STRUCTA, null);
        structA2.setattrvalue("attrA", "4");
        Iom_jObject structA3 = new Iom_jObject(STRUCTA, null);
        structA3.setattrvalue("attrA", "6");
        Iom_jObject structA4 = new Iom_jObject(STRUCTA, null);
        structA4.setattrvalue("attrA", "10");
        Iom_jObject structA5 = new Iom_jObject(STRUCTA, null);
        structA5.setattrvalue("attrA", "1");
        Iom_jObject classE = new Iom_jObject(CLASSE, OID1);
        classE.addattrobj("attre", structA1);
        classE.addattrobj("attre", structA2);
        classE.addattrobj("attre", structA3);
        classE.addattrobj("attre", structA4);
        classE.addattrobj("attre", structA5);
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(classE));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint MathFunction23.Topic.ClassE.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
}
