package ch.interlis.iox_j.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
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
    public void perObject_TwoClassDHasTwoSubStructureOverlap_Ok() {

        Iom_jObject structA = createStructureWithSurface("486000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structB = new Iom_jObject(STRUCTB, null);
        structB.addattrobj("attr2", structA);

        Iom_jObject structC = new Iom_jObject(STRUCTC, null);
        structC.addattrobj("attr3", structB);

        Iom_jObject classD1 = new Iom_jObject(CLASSD1, OID1);
        classD1.addattrobj("attr4", structC);

        Iom_jObject structA2 = createStructureWithSurface("488000.000", "75000.000", "494000.000", "80000.000");

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
    public void perObject_TwoClassDHasTwoSubStructureANotOverlap_Ok() {

        Iom_jObject structA = createStructureWithSurface("486000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structB = new Iom_jObject(STRUCTB, null);
        structB.addattrobj("attr2", structA);

        Iom_jObject structC = new Iom_jObject(STRUCTC, null);
        structC.addattrobj("attr3", structB);

        Iom_jObject classD1 = new Iom_jObject(CLASSD1, OID1);
        classD1.addattrobj("attr4", structC);

        Iom_jObject structA2 = createStructureWithSurface("490000.000", "75000.000", "494000.000", "80000.000");

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
    public void perStructure_TwoClassDWithMoreThanOneSubStructureOverlap_Ok() {

        Iom_jObject structA1_1 = createStructureWithSurface("410000.000", "75000.000", "420000.000", "80000.000");

        Iom_jObject structA1_2 = createStructureWithSurface("483000.000", "75000.000", "486000.000", "80000.000");

        Iom_jObject structB1 = new Iom_jObject(STRUCTB, null);
        structB1.addattrobj("attr2", structA1_1);
        structB1.addattrobj("attr2", structA1_2);

        Iom_jObject structC1 = new Iom_jObject(STRUCTC, null);
        structC1.addattrobj("attr3", structB1);

        Iom_jObject structA2 = createStructureWithSurface("484000.000", "75000.000", "488000.000", "80000.000");

        Iom_jObject structB2 = new Iom_jObject(STRUCTB, null);
        structB2.addattrobj("attr2", structA2);

        Iom_jObject structC2 = new Iom_jObject(STRUCTC, null);
        structC2.addattrobj("attr3", structB2);

        Iom_jObject classD1 = new Iom_jObject(CLASSD2, OID2);
        classD1.addattrobj("attr4", structC1);
        classD1.addattrobj("attr4", structC2);

        // Create an Another IomObjD
        Iom_jObject structA3 = createStructureWithSurface("486000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structB3 = new Iom_jObject(STRUCTB, null);
        structB3.addattrobj("attr2", structA3);

        Iom_jObject structC3 = new Iom_jObject(STRUCTC, null);
        structC3.addattrobj("attr3", structB3);

        Iom_jObject classD2 = new Iom_jObject(CLASSD2, OID2);
        classD2.addattrobj("attr4", structC3);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
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
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void areaPerAllObject_TwoClassDTheyAreNotOverlapping_Ok() {

        Iom_jObject structA = createStructureWithSurface("486000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structB = new Iom_jObject(STRUCTB, null);
        structB.addattrobj("attr2", structA);

        Iom_jObject structC = new Iom_jObject(STRUCTC, null);
        structC.addattrobj("attr3", structB);

        Iom_jObject classD1 = new Iom_jObject(CLASSD3, OID1);
        classD1.addattrobj("attr4", structC);

        Iom_jObject structA2 = createStructureWithSurface("492000.000", "75000.000", "494000.000", "80000.000");

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

    // #############################################################//
    // ######################### FAIL ##############################//
    // #############################################################//

    @Test
    public void perStructure_OneClassDHasTwoSubStructureB_BHasTwoSubStrAOverlap_Fail() {

        Iom_jObject structA1 = createStructureWithSurface("486000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structA2 = createStructureWithSurface("488000.000", "75000.000", "494000.000", "80000.000");

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
    public void perObject_OneClassDHasTwiceSubElementStructAOverlap_Fail() {

        Iom_jObject structA1 = createStructureWithSurface("486000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structA2 = createStructureWithSurface("488000.000", "75000.000", "494000.000", "80000.000");

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
    public void perObject_OneClassDHasTwoSubStructureAOverlap_Fail() {

        Iom_jObject structA1 = createStructureWithSurface("486000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structA2 = createStructureWithSurface("488000.000", "75000.000", "494000.000", "80000.000");

        Iom_jObject structB1 = new Iom_jObject(STRUCTB, null);
        structB1.addattrobj("attr2", structA1);
        structB1.addattrobj("attr2", structA2);

        Iom_jObject structC1 = new Iom_jObject(STRUCTC, null);
        structC1.addattrobj("attr3", structB1);

        Iom_jObject classD1 = new Iom_jObject(CLASSD1, OID2);
        classD1.addattrobj("attr4", structC1);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
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
    public void perStructure_OneClassDHasTwoSubStructureAOverlap_Fail() {

        Iom_jObject structA1 = createStructureWithSurface("486000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structA2 = createStructureWithSurface("488000.000", "75000.000", "494000.000", "80000.000");

        Iom_jObject structB1 = new Iom_jObject(STRUCTB, null);
        structB1.addattrobj("attr2", structA1);
        structB1.addattrobj("attr2", structA2);

        Iom_jObject structC1 = new Iom_jObject(STRUCTC, null);
        structC1.addattrobj("attr3", structB1);

        Iom_jObject classD1 = new Iom_jObject(CLASSD2, OID2);
        classD1.addattrobj("attr4", structC1);

        // Create and run validator.
        ValidationConfig modelConfig = new ValidationConfig();
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
    public void areaPerAllObject_TwoClassDTheyAreOverlapping_Fail() {

        Iom_jObject structA = createStructureWithSurface("486000.000", "75000.000", "490000.000", "80000.000");

        Iom_jObject structB = new Iom_jObject(STRUCTB, null);
        structB.addattrobj("attr2", structA);

        Iom_jObject structC = new Iom_jObject(STRUCTC, null);
        structC.addattrobj("attr3", structB);

        Iom_jObject classD1 = new Iom_jObject(CLASSD3, OID1);
        classD1.addattrobj("attr4", structC);

        Iom_jObject structA2 = createStructureWithSurface("488000.000", "75000.000", "494000.000", "80000.000");

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
        assertEquals(1, logger.getErrs().size());
        assertEquals("Set Constraint AreAreas23.Topic.ClassD3.Constraint1 is not true.",
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

    public Iom_jObject createStructureWithSurface(String x1, String y1, String x2, String y2) {
        Iom_jObject struct = new Iom_jObject(STRUCTA, null);
        {
            IomObject multisurfaceValue = struct.addattrobj("flaeche", "MULTISURFACE");
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
        }
        return struct;
    }
}
