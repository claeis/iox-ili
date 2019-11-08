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
    private final static String OID3 = "o3";
    private final static String OID4 = "o4";
    private final static String OID5 = "o5";
    private static final String OID6 = "o6";
    private static final String OID7 = "o7";
    private static final String OID8 = "o8";

    // MODEL.TOPIC
    private final static String TOPIC = "ObjectPath23.Topic";
    private final static String TOPIC_BACKWARD_DIRECTION = "ObjectPath23.BackwardDirection";
    private final static String TOPIC_RECURSIVELY = "ObjectPath23.RekursivelyObject";
    private final static String TOPIC_FORWARD_WITH_MORETHANONE_LINKOBJ = "ObjectPath23.ForwardDirectionWithMoreThanOnelinkObj";
    private final static String TOPIC_EXPRESSION_SINGLE_VALUE = "ObjectPath23.ExpressionSingleValue";
    
    // CLASSES
    private final static String CLASSA = TOPIC_RECURSIVELY + ".ClassA";
    private final static String CLASSB = TOPIC + ".ClassB";
    private final static String CLASSE = TOPIC + ".ClassE";
    private final static String CLASSF = TOPIC + ".ClassF";
    private final static String CLASSG = TOPIC + ".ClassG";
    private final static String CLASSH = TOPIC + ".ClassH";
    private final static String CLASSS = TOPIC + ".ClassS";
    private final static String CLASST = TOPIC + ".ClassT";
    private final static String CLASSU = TOPIC + ".ClassU";
    private final static String CLASSV = TOPIC + ".ClassV";
    private final static String CLASSW = TOPIC + ".ClassW";
    private final static String CLASSC1 = TOPIC + ".ClassC1";
    private final static String CLASSS1 = TOPIC_BACKWARD_DIRECTION + ".ClassS";
    private final static String CLASST1 = TOPIC_BACKWARD_DIRECTION + ".ClassT";
    private final static String CLASSV_BACKWARD = TOPIC_BACKWARD_DIRECTION + ".ClassV";
    private final static String CLASSW_BACKWARD = TOPIC_BACKWARD_DIRECTION + ".ClassW";
    private final static String CLASSX = TOPIC_FORWARD_WITH_MORETHANONE_LINKOBJ + ".ClassX";
    private final static String CLASSY = TOPIC_FORWARD_WITH_MORETHANONE_LINKOBJ + ".ClassY";
    private final static String CLASSZ = TOPIC_FORWARD_WITH_MORETHANONE_LINKOBJ + ".ClassZ";
    private final static String CLASSY1 = TOPIC_FORWARD_WITH_MORETHANONE_LINKOBJ + ".ClassY1";
    private final static String CLASSU2 = TOPIC_EXPRESSION_SINGLE_VALUE + ".ClassU";
    private final static String CLASSS2 = TOPIC_EXPRESSION_SINGLE_VALUE + ".ClassS";
    private final static String CLASST2 = TOPIC_EXPRESSION_SINGLE_VALUE + ".ClassT";
    
    // ASSOCIATION
    private static final String ASSOC_MANY2MANY = TOPIC + ".many2many";
    private static final String ASSOC_ST1_TOPIC = TOPIC + ".st1";
    private static final String ASSOC_ST1 = TOPIC_BACKWARD_DIRECTION + ".st1";
    private static final String ASSOC_ST1_V2 = TOPIC_EXPRESSION_SINGLE_VALUE + ".st1";
    private static final String ASSOC_XY = TOPIC_FORWARD_WITH_MORETHANONE_LINKOBJ + ".x2y";
    private static final String ASSOC_YZ = TOPIC_FORWARD_WITH_MORETHANONE_LINKOBJ + ".y2z";
    private static final String ASSOC_A2A = TOPIC_RECURSIVELY + ".a2a";

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
    public void embedded_forwardDirection_OK() throws Exception {
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
    public void embedded_forwardDirection_EmbeddedConstraint_OK() throws Exception {
        Iom_jObject iomObjV = new Iom_jObject(CLASSV, OID1);
        Iom_jObject iomObjW = new Iom_jObject(CLASSW, OID2);
        iomObjW.addattrobj("role_v", "REF").setobjectrefoid(iomObjV.getobjectoid());
        iomObjV.setattrvalue("v1", "V1");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(iomObjV));
        validator.validate(new ObjectEvent(iomObjW));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    @Test
    public void embedded_backwardDirection_EmbeddedConstraint_OK() throws Exception {
        Iom_jObject iomObjV = new Iom_jObject(CLASSV_BACKWARD, OID1);
        Iom_jObject iomObjW = new Iom_jObject(CLASSW_BACKWARD, OID2);
        iomObjW.addattrobj("role_v", "REF").setobjectrefoid(iomObjV.getobjectoid());
        iomObjW.setattrvalue("w1", "W1");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(iomObjV));
        validator.validate(new ObjectEvent(iomObjW));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void linkObj_forwardDirection_OK() throws Exception {
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
    
    @Test
    public void embedded_backwardDirection_OK() throws Exception {
        Iom_jObject iomObjS = new Iom_jObject(CLASSS1, OID1);
        Iom_jObject iomObjT = new Iom_jObject(CLASST1, OID2);
        iomObjT.addattrobj("role_s2", "REF").setobjectrefoid(iomObjS.getobjectoid());
        iomObjT.setattrvalue("t1", "value_t1");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_BACKWARD_DIRECTION, BID));
        validator.validate(new ObjectEvent(iomObjS));
        validator.validate(new ObjectEvent(iomObjT));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts 
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void linkObj_backwardDirection_toLinkObj_OK() throws Exception {
        Iom_jObject iomObjS = new Iom_jObject(CLASSS1, OID1);
        Iom_jObject iomObjT = new Iom_jObject(CLASST1, OID2);
        iomObjT.setattrvalue("t1", "value_t1");
        Iom_jObject iomObjST1 = new Iom_jObject(ASSOC_ST1, null);
        iomObjST1.addattrobj("role_s1", "REF").setobjectrefoid(iomObjS.getobjectoid());
        iomObjST1.addattrobj("role_t1", "REF").setobjectrefoid(iomObjT.getobjectoid());
        iomObjST1.setattrvalue("st_1", "value_st1");
        
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(iomObjS));
        validator.validate(new ObjectEvent(iomObjT));
        validator.validate(new ObjectEvent(iomObjST1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void forwardDirectionWithMoreThanOnelinkObj_OK() throws Exception {
        Iom_jObject iomObjX = new Iom_jObject(CLASSX, OID1);
        Iom_jObject iomObjY = new Iom_jObject(CLASSY, OID2);
        Iom_jObject iomObjXY = new Iom_jObject(ASSOC_XY, null);
        iomObjXY.addattrobj("role_x", "REF").setobjectrefoid(iomObjX.getobjectoid());
        iomObjXY.addattrobj("role_y", "REF").setobjectrefoid(iomObjY.getobjectoid());
        
        Iom_jObject iomObjZ1 = new Iom_jObject(CLASSZ, OID3);
        Iom_jObject iomObjYZ1 = new Iom_jObject(ASSOC_YZ, null);
        iomObjYZ1.addattrobj("role_y", "REF").setobjectrefoid(iomObjY.getobjectoid());
        iomObjYZ1.addattrobj("role_z", "REF").setobjectrefoid(iomObjZ1.getobjectoid());
        
        Iom_jObject iomObjZ2 = new Iom_jObject(CLASSZ, OID4);
        Iom_jObject iomObjYZ2 = new Iom_jObject(ASSOC_YZ, null);
        iomObjYZ2.addattrobj("role_y", "REF").setobjectrefoid(iomObjY.getobjectoid());
        iomObjYZ2.addattrobj("role_z", "REF").setobjectrefoid(iomObjZ2.getobjectoid());
        
        Iom_jObject iomObjY1_1 = new Iom_jObject(CLASSY1, OID5);
        Iom_jObject iomObjXY2 = new Iom_jObject(ASSOC_XY, null);
        iomObjXY2.addattrobj("role_x", "REF").setobjectrefoid(iomObjX.getobjectoid());
        iomObjXY2.addattrobj("role_y", "REF").setobjectrefoid(iomObjY1_1.getobjectoid());
        
        Iom_jObject iomObjY1_2 = new Iom_jObject(CLASSY1, OID6);
        Iom_jObject iomObjXY3 = new Iom_jObject(ASSOC_XY, null);
        iomObjXY3.addattrobj("role_x", "REF").setobjectrefoid(iomObjX.getobjectoid());
        iomObjXY3.addattrobj("role_y", "REF").setobjectrefoid(iomObjY1_2.getobjectoid());
        
        Iom_jObject iomObjZ3 = new Iom_jObject(CLASSZ, OID7);
        Iom_jObject iomObjYZ3 = new Iom_jObject(ASSOC_YZ, null);
        iomObjYZ3.addattrobj("role_y", "REF").setobjectrefoid(iomObjY1_2.getobjectoid());
        iomObjYZ3.addattrobj("role_z", "REF").setobjectrefoid(iomObjZ3.getobjectoid());
        
        Iom_jObject iomObjZ4 = new Iom_jObject(CLASSZ, OID8);
        Iom_jObject iomObjYZ4 = new Iom_jObject(ASSOC_YZ, null);
        iomObjYZ4.addattrobj("role_y", "REF").setobjectrefoid(iomObjY1_2.getobjectoid());
        iomObjYZ4.addattrobj("role_z", "REF").setobjectrefoid(iomObjZ4.getobjectoid());

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_FORWARD_WITH_MORETHANONE_LINKOBJ, BID));
        validator.validate(new ObjectEvent(iomObjX));
        
        validator.validate(new ObjectEvent(iomObjY));
        validator.validate(new ObjectEvent(iomObjZ1));
        validator.validate(new ObjectEvent(iomObjZ2));
        validator.validate(new ObjectEvent(iomObjXY));
        validator.validate(new ObjectEvent(iomObjYZ1));
        validator.validate(new ObjectEvent(iomObjYZ2));

        validator.validate(new ObjectEvent(iomObjY1_1));
        validator.validate(new ObjectEvent(iomObjXY2));
        
        validator.validate(new ObjectEvent(iomObjY1_2));
        validator.validate(new ObjectEvent(iomObjXY3));
        validator.validate(new ObjectEvent(iomObjYZ3));
        validator.validate(new ObjectEvent(iomObjYZ4));
        validator.validate(new ObjectEvent(iomObjZ3));
        validator.validate(new ObjectEvent(iomObjZ4));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts  
        assertEquals(0, logger.getErrs().size());
    }
    
    @Test
    public void recursivelyObject_OK() throws Exception {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA, OID1);
        iomObjA.setattrvalue("isLeaf", "false");
        Iom_jObject iomObjA2 = new Iom_jObject(CLASSA, OID2);
        iomObjA2.setattrvalue("isLeaf", "true");
        
        Iom_jObject iomObjA2A = new Iom_jObject(ASSOC_A2A, null);
        iomObjA2A.addattrobj("role_parent", "REF").setobjectrefoid(iomObjA.getobjectoid());
        iomObjA2A.addattrobj("role_child", "REF").setobjectrefoid(iomObjA.getobjectoid());

        Iom_jObject iomObjA2A2 = new Iom_jObject(ASSOC_A2A, null);
        iomObjA2A2.addattrobj("role_parent", "REF").setobjectrefoid(iomObjA.getobjectoid());
        iomObjA2A2.addattrobj("role_child", "REF").setobjectrefoid(iomObjA2.getobjectoid());
        
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_RECURSIVELY, BID));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new ObjectEvent(iomObjA2));
        validator.validate(new ObjectEvent(iomObjA2A));
        validator.validate(new ObjectEvent(iomObjA2A2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts  
        assertEquals(0, logger.getErrs().size());
    }
    
    //############################################################/
    //########## FAILING TESTS ###################################/
    //############################################################/    
    
    @Test
    public void embedded_backwardAnd_forwardDirectionExpressionSigleValue_Fail() throws Exception {

        Iom_jObject iomObjU = new Iom_jObject(CLASSU2, OID1);

        Iom_jObject iomObjS = new Iom_jObject(CLASSS2, OID2);
        iomObjS.addattrobj("role_u3", "REF").setobjectrefoid(iomObjU.getobjectoid());

        Iom_jObject iomObjT = new Iom_jObject(CLASST2, OID3);
        iomObjT.setattrvalue("t1", "value_t1");
        Iom_jObject iomObjST1 = new Iom_jObject(ASSOC_ST1_V2, null);
        iomObjST1.addattrobj("role_s1", "REF").setobjectrefoid(iomObjS.getobjectoid());
        iomObjST1.addattrobj("role_t1", "REF").setobjectrefoid(iomObjT.getobjectoid());

        Iom_jObject iomObjT4 = new Iom_jObject(CLASST2, OID4);
        iomObjT4.setattrvalue("t1", "value_t1");
        Iom_jObject iomObjST3 = new Iom_jObject(ASSOC_ST1_V2, null);
        iomObjST3.addattrobj("role_s1", "REF").setobjectrefoid(iomObjS.getobjectoid());
        iomObjST3.addattrobj("role_t1", "REF").setobjectrefoid(iomObjT4.getobjectoid());

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_EXPRESSION_SINGLE_VALUE, BID));
        validator.validate(new ObjectEvent(iomObjS));
        validator.validate(new ObjectEvent(iomObjU));
        validator.validate(new ObjectEvent(iomObjT));
        validator.validate(new ObjectEvent(iomObjT4));
        validator.validate(new ObjectEvent(iomObjST1));
        validator.validate(new ObjectEvent(iomObjST3));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts 
        assertEquals(1, logger.getErrs().size());
        assertEquals("expression role_t1->t1 must evaluate to a single value.", logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void embedded_backwardDirection_EmbeddedConstraint_Fail() throws Exception {
        Iom_jObject iomObjV = new Iom_jObject(CLASSV_BACKWARD, OID1);
        Iom_jObject iomObjW = new Iom_jObject(CLASSW_BACKWARD, OID2);
        iomObjW.addattrobj("role_v", "REF").setobjectrefoid(iomObjV.getobjectoid());
        //iomObjW.setattrvalue("w1", "W1"); should fail because w1 is UNDEFINED
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(iomObjV));
        validator.validate(new ObjectEvent(iomObjW));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts 
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint ObjectPath23.BackwardDirection.vw.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void embedded_forwardDirection_EmbeddedConstraint_Fail() throws Exception {
        Iom_jObject iomObjV = new Iom_jObject(CLASSV, OID1);
        Iom_jObject iomObjW = new Iom_jObject(CLASSW, OID2);
        iomObjW.addattrobj("role_v", "REF").setobjectrefoid(iomObjV.getobjectoid());
        // iomObjV.setattrvalue("v1", "V1"); should fail because v1 is UNDEFINED
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(iomObjV));
        validator.validate(new ObjectEvent(iomObjW));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts 
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint ObjectPath23.Topic.vw.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
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
    public void embedded_forwardDirection_Fail() throws Exception {
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
    public void linkObj_forwardDirection_Fail() throws Exception {
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
    
    @Test
    public void linkObj_backwardDirection_toLinkObj_Fail() throws Exception {
        Iom_jObject iomObjS = new Iom_jObject(CLASSS1, OID1);
        Iom_jObject iomObjT = new Iom_jObject(CLASST1, OID2);
        iomObjT.setattrvalue("t1", "value_t1");
        Iom_jObject iomObjST1 = new Iom_jObject(ASSOC_ST1, null);
        iomObjST1.addattrobj("role_s1", "REF").setobjectrefoid(iomObjS.getobjectoid());
        iomObjST1.addattrobj("role_t1", "REF").setobjectrefoid(iomObjT.getobjectoid());
        iomObjST1.setattrvalue("st_1", "xxx");
        
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_BACKWARD_DIRECTION, BID));
        validator.validate(new ObjectEvent(iomObjS));
        validator.validate(new ObjectEvent(iomObjT));
        validator.validate(new ObjectEvent(iomObjST1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint ObjectPath23.BackwardDirection.ClassS.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void embedded_backwardDirection_Fail() throws Exception {
        Iom_jObject iomObjS = new Iom_jObject(CLASSS1, OID1);
        Iom_jObject iomObjT = new Iom_jObject(CLASST1, OID2);
        iomObjT.addattrobj("role_s2", "REF").setobjectrefoid(iomObjS.getobjectoid());
        iomObjT.setattrvalue("t1", "xxx");
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_BACKWARD_DIRECTION, BID));
        validator.validate(new ObjectEvent(iomObjS));
        validator.validate(new ObjectEvent(iomObjT));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts 
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint ObjectPath23.BackwardDirection.ClassS.Constraint2 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void embedded_backwardAnd_forwardDirectionWithMoreThanOne_linkObj_Fail() throws Exception {
        // U<-S<->T
        Iom_jObject iomObjU = new Iom_jObject(CLASSU, OID1);
        
        Iom_jObject iomObjS = new Iom_jObject(CLASSS, OID2);
        iomObjS.addattrobj("role_u3", "REF").setobjectrefoid(iomObjU.getobjectoid());

        Iom_jObject iomObjS7 = new Iom_jObject(CLASSS, OID7);
        iomObjS7.addattrobj("role_u3", "REF").setobjectrefoid(iomObjU.getobjectoid());

        Iom_jObject iomObjT = new Iom_jObject(CLASST, OID3);
        iomObjT.setattrvalue("t1", "value_t1");
        Iom_jObject iomObjST1 = new Iom_jObject(ASSOC_ST1_TOPIC, null);
        iomObjST1.addattrobj("role_s1", "REF").setobjectrefoid(iomObjS.getobjectoid());
        iomObjST1.addattrobj("role_t1", "REF").setobjectrefoid(iomObjT.getobjectoid());
        
        Iom_jObject iomObjT4=new Iom_jObject(CLASST, OID4);
        iomObjT4.setattrvalue("t1", "value_t1");
        Iom_jObject iomObjST3=new Iom_jObject(ASSOC_ST1_TOPIC, null);
        iomObjST3.addattrobj("role_s1", "REF").setobjectrefoid(iomObjS.getobjectoid());
        iomObjST3.addattrobj("role_t1", "REF").setobjectrefoid(iomObjT4.getobjectoid());
        
        Iom_jObject iomObjT5=new Iom_jObject(CLASST, OID5);
        Iom_jObject iomObjST4=new Iom_jObject(ASSOC_ST1_TOPIC, null);
        iomObjST4.addattrobj("role_s1", "REF").setobjectrefoid(iomObjS7.getobjectoid());
        iomObjST4.addattrobj("role_t1", "REF").setobjectrefoid(iomObjT5.getobjectoid());
        
        Iom_jObject iomObjT6=new Iom_jObject(CLASST, OID6);

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(iomObjS));
        validator.validate(new ObjectEvent(iomObjU));
        validator.validate(new ObjectEvent(iomObjT));
        validator.validate(new ObjectEvent(iomObjS7));
        validator.validate(new ObjectEvent(iomObjT4));
        validator.validate(new ObjectEvent(iomObjT5));
        validator.validate(new ObjectEvent(iomObjT6));
        validator.validate(new ObjectEvent(iomObjST1));
        validator.validate(new ObjectEvent(iomObjST3));
        validator.validate(new ObjectEvent(iomObjST4));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts  
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint ObjectPath23.Topic.ClassU.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
        
    @Test
    public void forwardDirectionWithMoreThanOnelinkObj_Fail() throws Exception {
        Iom_jObject iomObjX = new Iom_jObject(CLASSX, OID1);
        Iom_jObject iomObjY = new Iom_jObject(CLASSY, OID2);
        Iom_jObject iomObjXY = new Iom_jObject(ASSOC_XY, null);
        iomObjXY.addattrobj("role_x", "REF").setobjectrefoid(iomObjX.getobjectoid());
        iomObjXY.addattrobj("role_y", "REF").setobjectrefoid(iomObjY.getobjectoid());
        
        Iom_jObject iomObjZ1 = new Iom_jObject(CLASSZ, OID3);
        Iom_jObject iomObjYZ1 = new Iom_jObject(ASSOC_YZ, null);
        iomObjYZ1.addattrobj("role_y", "REF").setobjectrefoid(iomObjY.getobjectoid());
        iomObjYZ1.addattrobj("role_z", "REF").setobjectrefoid(iomObjZ1.getobjectoid());
        
        Iom_jObject iomObjZ2 = new Iom_jObject(CLASSZ, OID4);
        Iom_jObject iomObjYZ2 = new Iom_jObject(ASSOC_YZ, null);
        iomObjYZ2.addattrobj("role_y", "REF").setobjectrefoid(iomObjY.getobjectoid());
        iomObjYZ2.addattrobj("role_z", "REF").setobjectrefoid(iomObjZ2.getobjectoid());
        
        Iom_jObject iomObjY1_1 = new Iom_jObject(CLASSY1, OID5);
        Iom_jObject iomObjXY2 = new Iom_jObject(ASSOC_XY, null);
        iomObjXY2.addattrobj("role_x", "REF").setobjectrefoid(iomObjX.getobjectoid());
        iomObjXY2.addattrobj("role_y", "REF").setobjectrefoid(iomObjY1_1.getobjectoid());
        
        Iom_jObject iomObjY1_2 = new Iom_jObject(CLASSY1, OID6);
        Iom_jObject iomObjXY3 = new Iom_jObject(ASSOC_XY, null);
        iomObjXY3.addattrobj("role_x", "REF").setobjectrefoid(iomObjX.getobjectoid());
        iomObjXY3.addattrobj("role_y", "REF").setobjectrefoid(iomObjY1_2.getobjectoid());
        
        Iom_jObject iomObjZ3 = new Iom_jObject(CLASSZ, OID7);
        Iom_jObject iomObjYZ3 = new Iom_jObject(ASSOC_YZ, null);
        iomObjYZ3.addattrobj("role_y", "REF").setobjectrefoid(iomObjY1_2.getobjectoid());
        iomObjYZ3.addattrobj("role_z", "REF").setobjectrefoid(iomObjZ3.getobjectoid());
        
        Iom_jObject iomObjZ4 = new Iom_jObject(CLASSZ, OID8);

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_FORWARD_WITH_MORETHANONE_LINKOBJ, BID));
        validator.validate(new ObjectEvent(iomObjX));
        
        validator.validate(new ObjectEvent(iomObjY));
        validator.validate(new ObjectEvent(iomObjZ1));
        validator.validate(new ObjectEvent(iomObjZ2));
        validator.validate(new ObjectEvent(iomObjXY));
        validator.validate(new ObjectEvent(iomObjYZ1));
        validator.validate(new ObjectEvent(iomObjYZ2));

        validator.validate(new ObjectEvent(iomObjY1_1));
        validator.validate(new ObjectEvent(iomObjXY2));
        
        validator.validate(new ObjectEvent(iomObjY1_2));
        validator.validate(new ObjectEvent(iomObjXY3));
        validator.validate(new ObjectEvent(iomObjYZ3));
        validator.validate(new ObjectEvent(iomObjZ3));
        validator.validate(new ObjectEvent(iomObjZ4));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts  
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint ObjectPath23.ForwardDirectionWithMoreThanOnelinkObj.ClassX.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
    
    @Test
    public void recursivelyObject_Fail() throws Exception {
        Iom_jObject iomObjA = new Iom_jObject(CLASSA, OID1);
        iomObjA.setattrvalue("isLeaf", "false");
        Iom_jObject iomObjA2 = new Iom_jObject(CLASSA, OID2);
        iomObjA2.setattrvalue("isLeaf", "true");
        Iom_jObject iomObjA3 = new Iom_jObject(CLASSA, OID3);
        iomObjA3.setattrvalue("isLeaf", "true");
        
        Iom_jObject iomObjA2A = new Iom_jObject(ASSOC_A2A, null);
        iomObjA2A.addattrobj("role_parent", "REF").setobjectrefoid(iomObjA.getobjectoid());
        iomObjA2A.addattrobj("role_child", "REF").setobjectrefoid(iomObjA.getobjectoid());

        Iom_jObject iomObjA2A2 = new Iom_jObject(ASSOC_A2A, null);
        iomObjA2A2.addattrobj("role_parent", "REF").setobjectrefoid(iomObjA.getobjectoid());
        iomObjA2A2.addattrobj("role_child", "REF").setobjectrefoid(iomObjA2.getobjectoid());
        
        Iom_jObject iomObjA2A3 = new Iom_jObject(ASSOC_A2A, null);
        iomObjA2A3.addattrobj("role_parent", "REF").setobjectrefoid(iomObjA.getobjectoid());
        iomObjA2A3.addattrobj("role_child", "REF").setobjectrefoid(iomObjA3.getobjectoid());
        
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC_RECURSIVELY, BID));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new ObjectEvent(iomObjA2));
        validator.validate(new ObjectEvent(iomObjA3));
        validator.validate(new ObjectEvent(iomObjA2A));
        validator.validate(new ObjectEvent(iomObjA2A2));
        validator.validate(new ObjectEvent(iomObjA2A3));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts  
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint ObjectPath23.RekursivelyObject.ClassA.Constraint1 is not true.",
                logger.getErrs().get(0).getEventMsg());
    }
}