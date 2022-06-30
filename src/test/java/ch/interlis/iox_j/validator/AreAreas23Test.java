package ch.interlis.iox_j.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.IoxLogEvent;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

public class AreAreas23Test {

    private TransferDescription td = null;
    // OID
    private final static String OID1 = "o1";
    private final static String OID2 = "o2";

    // MODEL.TOPIC
    private final static String TOPIC = "AreAreas23.Topic";

    // CLASS
    private final static String CLASSD1 = TOPIC + ".ClassD1";
    private final static String CLASSD2 = TOPIC + ".ClassD2";
    private final static String CLASSD3 = TOPIC + ".ClassD3";
    private final static String CLASSE = TOPIC + ".ClassE";
    private final static String CLASSF = TOPIC + ".ClassF";

    // START BASKET EVENT
    private final static String BID = "b1";

    // STRUCTURE
    private final static String STRUCTC = TOPIC + ".StructC";
    private final static String STRUCTB = TOPIC + ".StructB";
    private final static String STRUCTA = TOPIC + ".StructA";

    // #########################################################//
    // ####################### SUCCESS #########################//
    // #########################################################//

    @Test
    public void classD1_perObjectD1_TwoObject_SubStructOverlap_Ok() {

        Iom_jObject structA = createStructA("486000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structB = new Iom_jObject(STRUCTB, null);
        structB.addattrobj("attr2", structA);

        Iom_jObject structC = new Iom_jObject(STRUCTC, null);
        structC.addattrobj("attr3", structB);

        Iom_jObject classD1 = new Iom_jObject(CLASSD1, OID1);
        classD1.addattrobj("attr4", structC);

        Iom_jObject structA2 = createStructA("488000.000", "75000.000", "494000.000", "80000.000"); // overlaps structA, but ok because different object

        Iom_jObject structB2 = new Iom_jObject(STRUCTB, null);
        structB2.addattrobj("attr2", structA2);

        Iom_jObject structC2 = new Iom_jObject(STRUCTC, null);
        structC2.addattrobj("attr3", structB2);

        Iom_jObject classD2 = new Iom_jObject(CLASSD1, OID2);
        classD2.addattrobj("attr4", structC2);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classD1));
        validator.validate(new ObjectEvent(classD2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void classD1_perObjectD1_TwoObject_SubStructNoOverlap_Ok() {

        Iom_jObject structA = createStructA("486000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structB = new Iom_jObject(STRUCTB, null);
        structB.addattrobj("attr2", structA);

        Iom_jObject structC = new Iom_jObject(STRUCTC, null);
        structC.addattrobj("attr3", structB);

        Iom_jObject classD1 = new Iom_jObject(CLASSD1, OID1);
        classD1.addattrobj("attr4", structC);

        Iom_jObject structA2 = createStructA("490000.000", "75000.000", "494000.000", "80000.000"); // no overlap with structA

        Iom_jObject structB2 = new Iom_jObject(STRUCTB, null);
        structB2.addattrobj("attr2", structA2);

        Iom_jObject structC2 = new Iom_jObject(STRUCTC, null);
        structC2.addattrobj("attr3", structB2);

        Iom_jObject classD2 = new Iom_jObject(CLASSD1, OID2);
        classD2.addattrobj("attr4", structC2);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classD1));
        validator.validate(new ObjectEvent(classD2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void classD2_perStructC_TwoObjectNoOverlap_Ok() {

        Iom_jObject structA1_1 = createStructA("480000.000", "75000.000", "482000.000", "80000.000");

        Iom_jObject structA1_2 = createStructA("483000.000", "75000.000", "486000.000", "80000.000");

        Iom_jObject structB1 = new Iom_jObject(STRUCTB, null);
        structB1.addattrobj("attr2", structA1_1);
        structB1.addattrobj("attr2", structA1_2);

        Iom_jObject structC1 = new Iom_jObject(STRUCTC, null);
        structC1.addattrobj("attr3", structB1);

        Iom_jObject structA2 = createStructA("488000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structB2 = new Iom_jObject(STRUCTB, null);
        structB2.addattrobj("attr2", structA2);

        Iom_jObject structC2 = new Iom_jObject(STRUCTC, null);
        structC2.addattrobj("attr3", structB2);

        Iom_jObject classD1 = new Iom_jObject(CLASSD2, OID1);
        classD1.addattrobj("attr4", structC1);
        classD1.addattrobj("attr4", structC2);

        // Create an Another IomObjD
        Iom_jObject structA3 = createStructA("484000.000", "75000.000", "488000.000", "80000.000"); // overlaps structA1_2, but ok, because different object

        Iom_jObject structB3 = new Iom_jObject(STRUCTB, null);
        structB3.addattrobj("attr2", structA3);

        Iom_jObject structC3 = new Iom_jObject(STRUCTC, null);
        structC3.addattrobj("attr3", structB3);

        Iom_jObject classD2 = new Iom_jObject(CLASSD2, OID2);
        classD2.addattrobj("attr4", structC3);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DISABLE_AREAREAS_MESSAGES, ValidationConfig.TRUE);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classD1));
        validator.validate(new ObjectEvent(classD2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void classD3_allObjectD3_TwoObject_NoOverlap_Ok() {

        Iom_jObject structA = createStructA("486000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structB = new Iom_jObject(STRUCTB, null);
        structB.addattrobj("attr2", structA);

        Iom_jObject structC = new Iom_jObject(STRUCTC, null);
        structC.addattrobj("attr3", structB);

        Iom_jObject classD1 = new Iom_jObject(CLASSD3, OID1);
        classD1.addattrobj("attr4", structC);

        Iom_jObject structA2 = createStructA("492000.000", "75000.000", "494000.000", "80000.000"); // no overlap with structA

        Iom_jObject structB2 = new Iom_jObject(STRUCTB, null);
        structB2.addattrobj("attr2", structA2);

        Iom_jObject structC2 = new Iom_jObject(STRUCTC, null);
        structC2.addattrobj("attr3", structB2);

        Iom_jObject classD2 = new Iom_jObject(CLASSD3, OID2);
        classD2.addattrobj("attr4", structC2);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classD1));
        validator.validate(new ObjectEvent(classD2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void classE_allObjectE_TwoObject_NoOverlap_Ok() {

        IomObject structA = createRectangle("486000.000", "75000.000", "490000.000", "80000.000");
        Iom_jObject classE1 = new Iom_jObject(CLASSE, OID1);
        classE1.setattrvalue("art", "a");
        classE1.addattrobj("flaeche", structA);

        IomObject structA2 = createRectangle("490000.000", "75000.000", "494000.000", "80000.000"); // no overlap with structA
        Iom_jObject classE2 = new Iom_jObject(CLASSE, OID2);
        classE2.setattrvalue("art", "a");
        classE2.addattrobj("flaeche", structA2);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classE1));
        validator.validate(new ObjectEvent(classE2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void classF_allObjectF_TwoObject_NoOverlap_Ok() {

        IomObject structA = createStructA("486000.000", "75000.000", "490000.000", "80000.000");
        Iom_jObject classF1 = new Iom_jObject(CLASSF, OID1);
        classF1.setattrvalue("art", "a");
        classF1.addattrobj("attr2", structA);

        IomObject structA2 = createStructA("490000.000", "75000.000", "494000.000", "80000.000"); // no overlap with structA
        Iom_jObject classF2 = new Iom_jObject(CLASSF, OID2);
        classF2.setattrvalue("art", "a");
        classF2.addattrobj("attr2", structA2);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classF1));
        validator.validate(new ObjectEvent(classF2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(0, logger.getErrs().size());
    }
    
    // #############################################################//
    // ######################### FAIL ##############################//
    // #############################################################//

    @Test
    public void classD2_perStructC_OneObject_TwoStructOverlap_Fail() {

        Iom_jObject structA1 = createStructA("486000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structA2 = createStructA("488000.000", "75000.000", "494000.000", "80000.000"); // overlaps structA1

        Iom_jObject structB2_1 = new Iom_jObject(STRUCTB, null);
        structB2_1.addattrobj("attr2", structA1);

        Iom_jObject structB2_2 = new Iom_jObject(STRUCTB, null);
        structB2_2.addattrobj("attr2", structA2);
        
        Iom_jObject structC1 = new Iom_jObject(STRUCTC, null);
        structC1.addattrobj("attr3", structB2_1);       
        structC1.addattrobj("attr3", structB2_2);

        Iom_jObject classD2 = new Iom_jObject(CLASSD2, OID2);
        classD2.addattrobj("attr4", structC1);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DISABLE_AREAREAS_MESSAGES, ValidationConfig.TRUE);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classD2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint AreAreas23.Topic.ClassD2.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void classD1_perObjectD1_OneObject_StructOverlap_Fail() {

        Iom_jObject structA1 = createStructA("486000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structA2 = createStructA("488000.000", "75000.000", "494000.000", "80000.000"); // overlaps structA1

        Iom_jObject structB1 = new Iom_jObject(STRUCTB, null);
        structB1.addattrobj("attr2", structA1);

        Iom_jObject structB2 = new Iom_jObject(STRUCTB, null);
        structB2.addattrobj("attr2", structA2);

        Iom_jObject structC1 = new Iom_jObject(STRUCTC, null);
        structC1.addattrobj("attr3", structB1);

        Iom_jObject structC2 = new Iom_jObject(STRUCTC, null);
        structC2.addattrobj("attr3", structB2);

        Iom_jObject classD1 = new Iom_jObject(CLASSD1, OID2);
        classD1.addattrobj("attr4", structC1);
        classD1.addattrobj("attr4", structC2);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DISABLE_AREAREAS_MESSAGES, ValidationConfig.TRUE);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classD1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint AreAreas23.Topic.ClassD1.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void classD1_perObjectD1_OneObject_SubStructOverlap_Fail() {

        Iom_jObject structA1 = createStructA("486000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structA2 = createStructA("488000.000", "75000.000", "494000.000", "80000.000"); // overlaps structA1

        Iom_jObject structB1 = new Iom_jObject(STRUCTB, null);
        structB1.addattrobj("attr2", structA1);
        structB1.addattrobj("attr2", structA2);

        Iom_jObject structC1 = new Iom_jObject(STRUCTC, null);
        structC1.addattrobj("attr3", structB1);

        Iom_jObject classD1 = new Iom_jObject(CLASSD1, OID2);
        classD1.addattrobj("attr4", structC1);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DISABLE_AREAREAS_MESSAGES, ValidationConfig.TRUE);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classD1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint AreAreas23.Topic.ClassD1.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void classD2_perStructC_OneObject_TwoSubStructOverlap_Fail() {

        Iom_jObject structA1 = createStructA("486000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structA2 = createStructA("488000.000", "75000.000", "494000.000", "80000.000"); // overlaps structA1

        Iom_jObject structB1 = new Iom_jObject(STRUCTB, null);
        structB1.addattrobj("attr2", structA1);
        structB1.addattrobj("attr2", structA2);

        Iom_jObject structC1 = new Iom_jObject(STRUCTC, null);
        structC1.addattrobj("attr3", structB1);

        Iom_jObject classD1 = new Iom_jObject(CLASSD2, OID2);
        classD1.addattrobj("attr4", structC1);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DISABLE_AREAREAS_MESSAGES, ValidationConfig.TRUE);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classD1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint AreAreas23.Topic.ClassD2.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void classD2_perStructC_TwoObject_OneStructValid_OneStructOverlap_Fail() {

        // valid object
        Iom_jObject structA1 = createStructA("500000.000", "75000.000", "510000.000", "80000.000");

        Iom_jObject structB1 = new Iom_jObject(STRUCTB, null);
        structB1.addattrobj("attr2", structA1);

        Iom_jObject structC1 = new Iom_jObject(STRUCTC, null);
        structC1.addattrobj("attr3", structB1);

        Iom_jObject classD1 = new Iom_jObject(CLASSD2, OID1);
        classD1.addattrobj("attr4", structC1);

        // object with overlap
        Iom_jObject structA2_1 = createStructA("486000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structA2_2 = createStructA("488000.000", "75000.000", "494000.000", "80000.000"); // overlaps structA2_1

        Iom_jObject structB2_1 = new Iom_jObject(STRUCTB, null);
        structB2_1.addattrobj("attr2", structA2_1);

        Iom_jObject structB2_2 = new Iom_jObject(STRUCTB, null);
        structB2_2.addattrobj("attr2", structA2_2);

        Iom_jObject structC2 = new Iom_jObject(STRUCTC, null);
        structC2.addattrobj("attr3", structB2_1);
        structC2.addattrobj("attr3", structB2_2);

        Iom_jObject classD2 = new Iom_jObject(CLASSD2, OID2);
        classD2.addattrobj("attr4", structC2);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DISABLE_AREAREAS_MESSAGES, ValidationConfig.TRUE);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        settings.setValue(Validator.CONFIG_DEBUG_XTFOUT, "build/areAreas.xtf");
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classD1));
        validator.validate(new ObjectEvent(classD2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        validator.close();
        // Asserts.
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint AreAreas23.Topic.ClassD2.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void classD3_allObjectD3_TwoObject_Overlap_Fail() {

        Iom_jObject structA = createStructA("486000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structB = new Iom_jObject(STRUCTB, null);
        structB.addattrobj("attr2", structA);

        Iom_jObject structC = new Iom_jObject(STRUCTC, null);
        structC.addattrobj("attr3", structB);

        Iom_jObject classD1 = new Iom_jObject(CLASSD3, OID1);
        classD1.addattrobj("attr4", structC);

        Iom_jObject structA2 = createStructA("488000.000", "75000.000", "494000.000", "80000.000"); // overlaps structA

        Iom_jObject structB2 = new Iom_jObject(STRUCTB, null);
        structB2.addattrobj("attr2", structA2);

        Iom_jObject structC2 = new Iom_jObject(STRUCTC, null);
        structC2.addattrobj("attr3", structB2);

        Iom_jObject classD2 = new Iom_jObject(CLASSD3, OID2);
        classD2.addattrobj("attr4", structC2);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DISABLE_AREAREAS_MESSAGES, ValidationConfig.TRUE);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classD1));
        validator.validate(new ObjectEvent(classD2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(1, logger.getErrs().size());
        assertEquals("Set Constraint AreAreas23.Topic.ClassD3.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void classE_allObjectE_TwoObject_Overlap_Fail() {

        IomObject structA = createRectangle("486000.000", "75000.000", "490000.000", "80000.000");
        Iom_jObject classE1 = new Iom_jObject(CLASSE, OID1);
        classE1.setattrvalue("art", "a");
        classE1.addattrobj("flaeche", structA);

        IomObject structA2 = createRectangle("488000.000", "75000.000", "494000.000", "80000.000"); // overlaps structA
        Iom_jObject classE2 = new Iom_jObject(CLASSE, OID2);
        classE2.setattrvalue("art", "a");
        classE2.addattrobj("flaeche", structA2);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DISABLE_AREAREAS_MESSAGES, ValidationConfig.TRUE);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classE1));
        validator.validate(new ObjectEvent(classE2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(1, logger.getErrs().size());
        assertEquals("Set Constraint AreAreas23.Topic.ClassE.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void classE_allObjectE_TwoObject_Overlap_DetailMsgs_Fail() {

        IomObject structA = createRectangle("486000.000", "75000.000", "490000.000", "80000.000");
        Iom_jObject classE1 = new Iom_jObject(CLASSE, OID1);
        classE1.setattrvalue("art", "a");
        classE1.addattrobj("flaeche", structA);

        IomObject structA2 = createRectangle("488000.000", "77000.000", "494000.000", "78000.000"); // overlaps structA
        Iom_jObject classE2 = new Iom_jObject(CLASSE, OID2);
        classE2.setattrvalue("art", "a");
        classE2.addattrobj("flaeche", structA2);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classE1));
        validator.validate(new ObjectEvent(classE2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(3, logger.getErrs().size());
        java.util.ArrayList<String> errs=new java.util.ArrayList<String>();
        for(IoxLogEvent err:logger.getErrs()) {
            errs.add(err.getEventMsg());
        }
        Collections.sort(errs);
        assertEquals("Intersection coord1 (490000.000, 77000.000), tids o1/flaeche[1], o2/flaeche[1]",errs.get(0).trim());
        assertEquals("Intersection coord1 (490000.000, 78000.000), tids o1/flaeche[1], o2/flaeche[1]",errs.get(1).trim());
        assertEquals("Set Constraint AreAreas23.Topic.ClassE.Constraint1 is not true.",errs.get(2));
    }

    @Test
    public void classE_allObjectE_TwoIdenticalObjects_Overlap_Fail() {
        String x1 = "486000.000";
        String y1 = "75000.000";
        String x2 = "490000.000";
        String y2 = "80000.000";

        IomObject attrFlaeche = createRectangle(x1, y1, x2, y2);
        Iom_jObject classE1 = new Iom_jObject(CLASSE, OID1);
        classE1.setattrvalue("art", "a");
        classE1.addattrobj("flaeche", attrFlaeche);

        IomObject attrFlaeche2 = createRectangle(x1, y1, x2, y2); // equal to attrFlaeche
        Iom_jObject classE2 = new Iom_jObject(CLASSE, OID2);
        classE2.setattrvalue("art", "a");
        classE2.addattrobj("flaeche", attrFlaeche2);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DISABLE_AREAREAS_MESSAGES, ValidationConfig.TRUE);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classE1));
        validator.validate(new ObjectEvent(classE2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(1, logger.getErrs().size());
        assertEquals("Set Constraint AreAreas23.Topic.ClassE.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void classE_allObjectE_ObjectWithinObject_Overlap_Fail() {
        IomObject attrFlaeche = createRectangle("490000.000", "70000.000", "500000.000", "80000.000");
        Iom_jObject classE1 = new Iom_jObject(CLASSE, OID1);
        classE1.setattrvalue("art", "a");
        classE1.addattrobj("flaeche", attrFlaeche);

        IomObject attrFlaeche2 = createRectangle("492000.000", "72000.000", "498000.000", "78000.000"); // completely within attrFlaeche
        Iom_jObject classE2 = new Iom_jObject(CLASSE, OID2);
        classE2.setattrvalue("art", "a");
        classE2.addattrobj("flaeche", attrFlaeche2);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DISABLE_AREAREAS_MESSAGES, ValidationConfig.TRUE);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classE1));
        validator.validate(new ObjectEvent(classE2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(1, logger.getErrs().size());
        assertEquals("Set Constraint AreAreas23.Topic.ClassE.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void classF_allObjectF_TwoObject_Overlap_Fail() {

        IomObject structA = createStructA("486000.000", "75000.000", "490000.000", "80000.000");
        Iom_jObject classF1 = new Iom_jObject(CLASSF, OID1);
        classF1.setattrvalue("art", "a");
        classF1.addattrobj("attr2", structA);

        IomObject structA2 = createStructA("488000.000", "75000.000", "494000.000", "80000.000"); // overlaps structA
        Iom_jObject classF2 = new Iom_jObject(CLASSF, OID2);
        classF2.setattrvalue("art", "a");
        classF2.addattrobj("attr2", structA2);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DISABLE_AREAREAS_MESSAGES, ValidationConfig.TRUE);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classF1));
        validator.validate(new ObjectEvent(classF2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(1, logger.getErrs().size());
        assertEquals("Set Constraint AreAreas23.Topic.ClassF.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void classF_allObjectF_TwoIdenticalObjects_Overlap_Fail() {
         String x1 = "486000.000";
         String y1 = "75000.000";
         String x2 = "490000.000";
         String y2 = "80000.000";

        IomObject structA = createStructA(x1, y1, x2, y2);
        Iom_jObject classF1 = new Iom_jObject(CLASSF, OID1);
        classF1.setattrvalue("art", "a");
        classF1.addattrobj("attr2", structA);

        IomObject structA2 = createStructA(x1, y1, x2, y2); // equal to structA
        Iom_jObject classF2 = new Iom_jObject(CLASSF, OID2);
        classF2.setattrvalue("art", "a");
        classF2.addattrobj("attr2", structA2);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DISABLE_AREAREAS_MESSAGES, ValidationConfig.TRUE);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classF1));
        validator.validate(new ObjectEvent(classF2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(1, logger.getErrs().size());
        assertEquals("Set Constraint AreAreas23.Topic.ClassF.Constraint1 is not true.",
                     logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void classF_allObjectF_ObjectWithinObject_Overlap_Fail() {
        IomObject structA = createStructA("490000.000", "70000.000", "500000.000", "80000.000");
        Iom_jObject classF1 = new Iom_jObject(CLASSF, OID1);
        classF1.setattrvalue("art", "a");
        classF1.addattrobj("attr2", structA);

        IomObject structA2 = createStructA("492000.000", "72000.000", "498000.000", "78000.000"); // completely within structA
        Iom_jObject classF2 = new Iom_jObject(CLASSF, OID2);
        classF2.setattrvalue("art", "a");
        classF2.addattrobj("attr2", structA2);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DISABLE_AREAREAS_MESSAGES, ValidationConfig.TRUE);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classF1));
        validator.validate(new ObjectEvent(classF2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(1, logger.getErrs().size());
        assertEquals("Set Constraint AreAreas23.Topic.ClassF.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    // #############################################################//
    // ######################### GENEREL ###########################//
    // #############################################################//
    
    @Before
    public void setUp() throws Exception {
        // ili-datei lesen
        Configuration ili2cConfig = new Configuration();
        {
            FileEntry fileEntry = new FileEntry("src/test/data/validator/INTERLIS_ext.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
        }
        {
            FileEntry fileEntry = new FileEntry("src/test/data/validator/AreAreas23.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
        }
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }

    public Iom_jObject createStructA(String x1, String y1, String x2, String y2) {
        Iom_jObject struct = new Iom_jObject(STRUCTA, null);
        {
            IomObject multisurfaceValue = createRectangle(x1, y1, x2, y2);
            struct.addattrobj("flaeche", multisurfaceValue);        }
        return struct;
    }

    public IomObject createRectangle(String x1, String y1, String x2, String y2) {
        IomObject multisurfaceValue = new Iom_jObject("MULTISURFACE",null);
        IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
        IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
        // polyline
        IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
        IomObject segments = polylineValue.addattrobj("sequence", "SEGMENTS");
        IomObject startSegment = segments.addattrobj("segment", "COORD");
        startSegment.setattrvalue("C1", x1);
        startSegment.setattrvalue("C2", y1);
        IomObject endSegment = segments.addattrobj("segment", "COORD");
        endSegment.setattrvalue("C1", x1);
        endSegment.setattrvalue("C2", y2);
        // polyline 2
        IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
        IomObject segments2 = polylineValue2.addattrobj("sequence", "SEGMENTS");
        IomObject startSegment2 = segments2.addattrobj("segment", "COORD");
        startSegment2.setattrvalue("C1", x1);
        startSegment2.setattrvalue("C2", y2);
        IomObject endSegment2 = segments2.addattrobj("segment", "COORD");
        endSegment2.setattrvalue("C1", x2);
        endSegment2.setattrvalue("C2", y2);
        // polyline 3
        IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
        IomObject segments3 = polylineValue3.addattrobj("sequence", "SEGMENTS");
        IomObject startSegment3 = segments3.addattrobj("segment", "COORD");
        startSegment3.setattrvalue("C1", x2);
        startSegment3.setattrvalue("C2", y2);
        IomObject endSegment3 = segments3.addattrobj("segment", "COORD");
        endSegment3.setattrvalue("C1", x2);
        endSegment3.setattrvalue("C2", y1);
        // polyline 4
        IomObject polylineValue4 = outerBoundary.addattrobj("polyline", "POLYLINE");
        IomObject segments4 = polylineValue4.addattrobj("sequence", "SEGMENTS");
        IomObject startSegment4 = segments4.addattrobj("segment", "COORD");
        startSegment4.setattrvalue("C1", x2);
        startSegment4.setattrvalue("C2", y1);
        IomObject endSegment4 = segments4.addattrobj("segment", "COORD");
        endSegment4.setattrvalue("C1", x1);
        endSegment4.setattrvalue("C2", y1);
        return multisurfaceValue;
    }
}
