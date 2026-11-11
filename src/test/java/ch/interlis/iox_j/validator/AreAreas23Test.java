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

    // MODEL.TOPIC, CLASS / STRUCTURE (overlap tolerance)
    private final static String OVERLAP_TOPIC = "AreAreas23.OverlapTolerance";
    private final static String BASE = OVERLAP_TOPIC + ".Base";
    private final static String STRICT = OVERLAP_TOPIC + ".Strict";
    private final static String PART = OVERLAP_TOPIC + ".Part";
    private final static String STRICT_PART = OVERLAP_TOPIC + ".StrictPart";
    private final static String HOLDER = OVERLAP_TOPIC + ".Holder";
    private final static String UNPARSABLE_TEXT_PATH = OVERLAP_TOPIC + ".UnparsableTextPath";

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
    
    @Test
    public void classE_allObjectE_DegeneratedArcValidationOff_Ok() {
        Iom_jObject classE1 = new Iom_jObject(CLASSE, OID1);
        classE1.setattrvalue("art", "a");
        classE1.addattrobj("flaeche", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("480000.000", "70000.000"),
                        IomObjectHelper.createArc("480005.000", "70000.000", "480010.000", "70000.000"),
                        IomObjectHelper.createCoord("480010.000", "70010.000"),
                        IomObjectHelper.createCoord("480000.000", "70010.000"),
                        IomObjectHelper.createCoord("480000.000", "70000.000"))));

        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DEGENERATED_ARC_VALIDATION, ValidationConfig.OFF);
        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, BID, modelConfig, classE1);
        // Asserts.
        assertEquals(0, logger.getErrs().size());
        assertEquals(0, logger.getWarn().size());
    }

    @Test
    public void overlapBelowTolerance_Ok() {
        LogCollector logger = ValidatorTestHelper.validateObjects(td, OVERLAP_TOPIC,
                createGeomObject(BASE, "o1", createArcPolygon("75010.010")),
                createGeomObject(BASE, "o2", createStraightPolygon()));

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void strictSubclassLast_UsesLenientBaseTolerance_Ok() {
        LogCollector logger = ValidatorTestHelper.validateObjects(td, OVERLAP_TOPIC,
                createGeomObject(BASE, "o1", createStraightPolygon()),
                createGeomObject(STRICT, "o2", createArcPolygon("75010.010")));

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void strictSubclassFirst_UsesLenientBaseTolerance_Ok() {
        LogCollector logger = ValidatorTestHelper.validateObjects(td, OVERLAP_TOPIC,
                createGeomObject(STRICT, "o1", createArcPolygon("75010.010")),
                createGeomObject(BASE, "o2", createStraightPolygon()));

        assertEquals(0, logger.getErrs().size());
    }

    // The path names the base structure (Part->Geom) explicitly. This checks that an extended structure
    // with a stricter tolerance in the bag does not make the check stricter.
    @Test
    public void bagOfStructure_WithExtendedStructure_OverlapBelowTolerance_Ok() {
        Iom_jObject holder1 = new Iom_jObject(HOLDER, "o1");
        holder1.addattrobj("Parts", createGeomObject(STRICT_PART, null, createArcPolygon("75010.010")));
        Iom_jObject holder2 = new Iom_jObject(HOLDER, "o2");
        holder2.addattrobj("Parts", createGeomObject(PART, null, createStraightPolygon()));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, OVERLAP_TOPIC, holder1, holder2);

        assertEquals(0, logger.getErrs().size());
    }

    // The unparsable path is logged by the function; no surfaces are found, so the constraint is true (as before #468).
    @Test
    public void areAreas3_UnparsableTextPath_Ok() {
        LogCollector logger = ValidatorTestHelper.validateObjects(td, OVERLAP_TOPIC,
                createGeomObject(UNPARSABLE_TEXT_PATH, "o1", createArcPolygon("75010.100")),
                createGeomObject(UNPARSABLE_TEXT_PATH, "o2", createStraightPolygon()));

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

    @Test
    public void InvalidInputPolygon() {
        Iom_jObject classE1Valid = new Iom_jObject(CLASSE, OID1);
        classE1Valid.setattrvalue("art", "a");
        classE1Valid.addattrobj("flaeche", IomObjectHelper.createRectangleGeometry("480010.000", "70010.000", "480020.000", "70030.000"));

        Iom_jObject classE2Invalid = new Iom_jObject(CLASSE, OID2);
        classE2Invalid.setattrvalue("art", "a");
        classE2Invalid.addattrobj("flaeche", IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("480020.000", "70010.000"),
                        IomObjectHelper.createCoord("480030.000", "70030.000"),
                        IomObjectHelper.createCoord("480020.000", "70030.000"),
                        IomObjectHelper.createCoord("480030.000", "70010.000"),
                        IomObjectHelper.createCoord("480020.000", "70030.000"))));

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(classE1Valid));
        validator.validate(new ObjectEvent(classE2Invalid));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Intersection coord1 (480025.000, 70020.000), tids o2, o2",
                "Overlay coord1 (480020.000, 70030.000), coord2 (480030.000, 70010.000), tids o2, o2",
                "Set Constraint AreAreas23.Topic.ClassE.Constraint1 is not true.");
    }

    @Test
    public void overlapAboveTolerance_Fail() {
        LogCollector logger = ValidatorTestHelper.validateObjects(td, OVERLAP_TOPIC,
                createGeomObject(BASE, "o1", createArcPolygon("75010.100")),
                createGeomObject(BASE, "o2", createStraightPolygon()));

        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Intersection overlap 0.10005944515626197, coord1 (486010.238, 75010.000), coord2 (486020.000, 75010.000), tids o1/Geom[1], o2/Geom[1]",
                "Intersection overlap 0.10005944515626197, coord1 (486010.238, 75010.000), coord2 (486020.000, 75010.000), tids o1/Geom[1], o2/Geom[1]",
                "Set Constraint AreAreas23.OverlapTolerance.Base.GeomAreAreas is not true.",
                "Set Constraint AreAreas23.OverlapTolerance.Base.GeomAreAreas3 is not true.");
    }

    // The constraints are defined in Base (0.05), but they check Strict objects (0.001) only.
    @Test
    public void strictSubclassOnly_UsesStrictTolerance_Fail() {
        LogCollector logger = ValidatorTestHelper.validateObjects(td, OVERLAP_TOPIC,
                createGeomObject(STRICT, "o1", createArcPolygon("75010.010")),
                createGeomObject(STRICT, "o2", createStraightPolygon()));

        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Intersection overlap 0.010416655723815893, coord1 (486011.667, 75010.000), coord2 (486020.000, 75010.000), tids o1/Geom[1], o2/Geom[1]",
                "Intersection overlap 0.010416655723815893, coord1 (486011.667, 75010.000), coord2 (486020.000, 75010.000), tids o1/Geom[1], o2/Geom[1]",
                "Set Constraint AreAreas23.OverlapTolerance.Base.GeomAreAreas is not true.",
                "Set Constraint AreAreas23.OverlapTolerance.Base.GeomAreAreas3 is not true.");
    }

    @Test
    public void bagOfStructure_OverlapAboveTolerance_Fail() {
        Iom_jObject holder1 = new Iom_jObject(HOLDER, "o1");
        holder1.addattrobj("Parts", createGeomObject(PART, null, createArcPolygon("75010.100")));
        Iom_jObject holder2 = new Iom_jObject(HOLDER, "o2");
        holder2.addattrobj("Parts", createGeomObject(PART, null, createStraightPolygon()));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, OVERLAP_TOPIC, holder1, holder2);

        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Intersection overlap 0.10005944515626197, coord1 (486010.238, 75010.000), coord2 (486020.000, 75010.000), tids o1/Parts[1]/Geom[1], o2/Parts[1]/Geom[1]",
                "Set Constraint AreAreas23.OverlapTolerance.Holder.PartsGeomAreAreas is not true.");
    }

    // The path names the base structure Part (0.05), but the bag holds StrictPart elements (0.001) only.
    @Test
    public void bagOfStructure_StrictStructureOnly_UsesStrictTolerance_Fail() {
        Iom_jObject holder1 = new Iom_jObject(HOLDER, "o1");
        holder1.addattrobj("Parts", createGeomObject(STRICT_PART, null, createArcPolygon("75010.010")));
        Iom_jObject holder2 = new Iom_jObject(HOLDER, "o2");
        holder2.addattrobj("Parts", createGeomObject(STRICT_PART, null, createStraightPolygon()));

        LogCollector logger = ValidatorTestHelper.validateObjects(td, OVERLAP_TOPIC, holder1, holder2);

        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Intersection overlap 0.010416655723815893, coord1 (486011.667, 75010.000), coord2 (486020.000, 75010.000), tids o1/Parts[1]/Geom[1], o2/Parts[1]/Geom[1]",
                "Set Constraint AreAreas23.OverlapTolerance.Holder.PartsGeomAreAreas is not true.");
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
        return IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createMultiplePolylineBoundary(
                        IomObjectHelper.createCoord(x1, y1),
                        IomObjectHelper.createCoord(x1, y2),
                        IomObjectHelper.createCoord(x2, y2),
                        IomObjectHelper.createCoord(x2, y1),
                        IomObjectHelper.createCoord(x1, y1)));
    }

    private static IomObject createGeomObject(String tag, String oid, IomObject geom) {
        Iom_jObject obj = new Iom_jObject(tag, oid);
        obj.addattrobj("Geom", geom);
        return obj;
    }

    /**
     * Rectangle (486010,75000)-(486020,75010) whose top edge is an arc from (486020,75010) through
     * (486015,bulgeTopY) to (486010,75009.990). The arc overlaps {@link #createStraightPolygon()}, starting at
     * the common vertex (486020,75010).
     */
    private static IomObject createArcPolygon(String bulgeTopY) {
        return IomObjectHelper.createPolygonFromBoundaries(
                IomObjectHelper.createBoundary(
                        IomObjectHelper.createCoord("486010.000", "75000.000"),
                        IomObjectHelper.createCoord("486020.000", "75000.000"),
                        IomObjectHelper.createCoord("486020.000", "75010.000"),
                        IomObjectHelper.createArc("486015.000", bulgeTopY, "486010.000", "75009.990"),
                        IomObjectHelper.createCoord("486010.000", "75000.000")));
    }

    private static IomObject createStraightPolygon() {
        return IomObjectHelper.createRectangleGeometry("486010.000", "75010.000", "486020.000", "75020.000");
    }
}
