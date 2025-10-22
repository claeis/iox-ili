package ch.interlis.iox_j.validator;

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
import ch.interlis.iox_j.validator.ValidationConfig;
import ch.interlis.iox_j.validator.Validator;

import org.junit.Before;

import org.junit.Assert;

import static org.junit.Assert.assertEquals;

import java.util.Map;

public class ElementsFunctionsTest {
    private static final String TEST_WERT = "Test-Wert";
    private static final String ILI_MODEL="Elements_V1_0_Test";
    private static final String ILI_TOPIC_A=ILI_MODEL+".TopicA";
    private static final String ILI_A_LOKALISATION_LOKALISATION_NAME = "LokalisationName";
    private static final String ILI_A_LOKALISATIONSNAME_NAME = "Name";
    private static final String ILI_A_LOKALISATION = ILI_TOPIC_A+".Lokalisation";
    private static final String ILI_A_LOKALISATIONSNAME = ILI_TOPIC_A+".Lokalisationsname";

    private static final String ILI_TOPIC_B=ILI_MODEL+".TopicB";
    private static final String ILI_B_LOKALISATION_LOKALISATION_NAME = "LokalisationName";
    private static final String ILI_B_LOKALISATIONSNAME_NAME = "Name";
    private static final String ILI_B_GEBAEUDEEINGANG = ILI_TOPIC_B+".Gebaeudeeingang";
    private static final String ILI_B_GEBAEUDEEINGANG_LOKALISATION = "Lokalisation";
    private static final String ILI_B_LOKALISATION = ILI_TOPIC_B+".Lokalisation";
    private static final String ILI_B_LOKALISATIONSNAME = ILI_TOPIC_B+".Lokalisationsname";

    private static final String ILI_TOPIC_C=ILI_MODEL+".TopicC";
    private static final String ILI_C_LOKALISATION1_NAME = "Name";
    private static final String ILI_C_LOKALISATION1 = ILI_TOPIC_C+".Lokalisation1";
    private static final String ILI_C_LOKALISATION2_NAME = "Name";
    private static final String ILI_C_LOKALISATION2 = ILI_TOPIC_C+".Lokalisation2";
    
    private static final String ILI_TOPIC_D=ILI_MODEL+".TopicD";
    private final static String CLASS_COALESCE_NUMERIC_TEST = ILI_TOPIC_D + ".CoalesceTest";
    
    
    private TransferDescription td=null;
    @Before
    public void setUp() throws Exception {
        // ili-datei lesen
        Configuration ili2cConfig=new Configuration();
        ili2cConfig.addFileEntry(new FileEntry("src/test/data/validator/Elements_V1_0.ili", FileEntryKind.ILIMODELFILE));
        ili2cConfig.addFileEntry(new FileEntry("src/test/data/validator/Elements_V1_0_Test.ili", FileEntryKind.ILIMODELFILE));
        td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        Assert.assertNotNull(td);
    }
    @Test
    public void valuesOfPath_attr_value1_Ok() {
        Iom_jObject iomObj1=new Iom_jObject(ILI_A_LOKALISATION, "o1");
        {
            Iom_jObject iomObj0=new Iom_jObject(ILI_A_LOKALISATIONSNAME,null);
            iomObj0.setattrvalue(ILI_A_LOKALISATIONSNAME_NAME,TEST_WERT);
            iomObj1.addattrobj(ILI_A_LOKALISATION_LOKALISATION_NAME,iomObj0);
        }
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC_A,"b1"));
        validator.validate(new ObjectEvent(iomObj1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        Assert.assertEquals(0,logger.getErrs().size());
    }
    @Test
    public void valuesOfPath_attr_value1_Fail() {
        Iom_jObject iomObj1=new Iom_jObject(ILI_A_LOKALISATION, "o1");
        {
            Iom_jObject iomObj0=new Iom_jObject(ILI_A_LOKALISATIONSNAME,null);
            iomObj0.setattrvalue(ILI_A_LOKALISATIONSNAME_NAME,"anderer Test-Wert");
            iomObj1.addattrobj(ILI_A_LOKALISATION_LOKALISATION_NAME,iomObj0);
        }
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC_A,"b1"));
        validator.validate(new ObjectEvent(iomObj1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        Assert.assertEquals(1,logger.getErrs().size());
        Assert.assertEquals("Mandatory Constraint Elements_V1_0_Test.TopicA.Lokalisation.LOK01 is not true.",logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void valuesOfPath_attr_value0_Fail() {
        Iom_jObject iomObj1=new Iom_jObject(ILI_A_LOKALISATION, "o1");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC_A,"b1"));
        validator.validate(new ObjectEvent(iomObj1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        Assert.assertEquals(1,logger.getErrs().size());
        Assert.assertEquals("Mandatory Constraint Elements_V1_0_Test.TopicA.Lokalisation.LOK01 is not true.",logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void valuesOfPath_attr_value2_Ok() {
        Iom_jObject iomObj1=new Iom_jObject(ILI_A_LOKALISATION, "o1");
        {
            Iom_jObject iomObj0=new Iom_jObject(ILI_A_LOKALISATIONSNAME,null);
            iomObj0.setattrvalue(ILI_A_LOKALISATIONSNAME_NAME,TEST_WERT);
            iomObj1.addattrobj(ILI_A_LOKALISATION_LOKALISATION_NAME,iomObj0);
        }
        {
            Iom_jObject iomObj0=new Iom_jObject(ILI_A_LOKALISATIONSNAME,null);
            iomObj0.setattrvalue(ILI_A_LOKALISATIONSNAME_NAME,TEST_WERT);
            iomObj1.addattrobj(ILI_A_LOKALISATION_LOKALISATION_NAME,iomObj0);
        }
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC_A,"b1"));
        validator.validate(new ObjectEvent(iomObj1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        Assert.assertEquals(0,logger.getErrs().size());
    }
    @Test
    public void valuesOfPath_attr_value2_Fail() {
        Iom_jObject iomObj1=new Iom_jObject(ILI_A_LOKALISATION, "o1");
        {
            Iom_jObject iomObj0=new Iom_jObject(ILI_A_LOKALISATIONSNAME,null);
            iomObj0.setattrvalue(ILI_A_LOKALISATIONSNAME_NAME,"anderer Test-Wert");
            iomObj1.addattrobj(ILI_A_LOKALISATION_LOKALISATION_NAME,iomObj0);
        }
        {
            Iom_jObject iomObj0=new Iom_jObject(ILI_A_LOKALISATIONSNAME,null);
            iomObj0.setattrvalue(ILI_A_LOKALISATIONSNAME_NAME,"anderer Test-Wert");
            iomObj1.addattrobj(ILI_A_LOKALISATION_LOKALISATION_NAME,iomObj0);
        }
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC_A,"b1"));
        validator.validate(new ObjectEvent(iomObj1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        Assert.assertEquals(1,logger.getErrs().size());
        Assert.assertEquals("Mandatory Constraint Elements_V1_0_Test.TopicA.Lokalisation.LOK01 is not true.",logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void valuesOfPath_role1_value1_Ok() {
        Iom_jObject iomObj1=new Iom_jObject(ILI_B_LOKALISATION, "o1");
        {
            Iom_jObject iomObj0=new Iom_jObject(ILI_B_LOKALISATIONSNAME,null);
            iomObj0.setattrvalue(ILI_B_LOKALISATIONSNAME_NAME,TEST_WERT);
            iomObj1.addattrobj(ILI_B_LOKALISATION_LOKALISATION_NAME,iomObj0);
        }
        Iom_jObject iomObj2=new Iom_jObject(ILI_B_GEBAEUDEEINGANG, "o2");
        {
            Iom_jObject iomObj0=new Iom_jObject(Iom_jObject.REF,null);
            iomObj0.setobjectrefoid(iomObj1.getobjectoid());
            iomObj2.addattrobj(ILI_B_GEBAEUDEEINGANG_LOKALISATION,iomObj0);
        }
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC_B,"b1"));
        validator.validate(new ObjectEvent(iomObj1));
        validator.validate(new ObjectEvent(iomObj2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        Assert.assertEquals(0,logger.getErrs().size());
    }
    @Test
    public void valuesOfPath_role1_value1_Fail() {
        Iom_jObject iomObj1=new Iom_jObject(ILI_B_LOKALISATION, "o1");
        {
            Iom_jObject iomObj0=new Iom_jObject(ILI_B_LOKALISATIONSNAME,null);
            iomObj0.setattrvalue(ILI_B_LOKALISATIONSNAME_NAME,"anderer Test-Wert");
            iomObj1.addattrobj(ILI_B_LOKALISATION_LOKALISATION_NAME,iomObj0);
        }
        Iom_jObject iomObj2=new Iom_jObject(ILI_B_GEBAEUDEEINGANG, "o2");
        {
            Iom_jObject iomObj0=new Iom_jObject(Iom_jObject.REF,null);
            iomObj0.setobjectrefoid(iomObj1.getobjectoid());
            iomObj2.addattrobj(ILI_B_GEBAEUDEEINGANG_LOKALISATION,iomObj0);
        }
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC_B,"b1"));
        validator.validate(new ObjectEvent(iomObj1));
        validator.validate(new ObjectEvent(iomObj2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        Assert.assertEquals(1,logger.getErrs().size());
        Assert.assertEquals("Mandatory Constraint Elements_V1_0_Test.TopicB.Gebaeudeeingang.GEB01 is not true.",logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void valuesOfPath_role1_value0_Fail() {
        Iom_jObject iomObj1=new Iom_jObject(ILI_B_LOKALISATION, "o1");
        Iom_jObject iomObj2=new Iom_jObject(ILI_B_GEBAEUDEEINGANG, "o2");
        {
            Iom_jObject iomObj0=new Iom_jObject(Iom_jObject.REF,null);
            iomObj0.setobjectrefoid(iomObj1.getobjectoid());
            iomObj2.addattrobj(ILI_B_GEBAEUDEEINGANG_LOKALISATION,iomObj0);
        }
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC_B,"b1"));
        validator.validate(new ObjectEvent(iomObj1));
        validator.validate(new ObjectEvent(iomObj2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        Assert.assertEquals(1,logger.getErrs().size());
        Assert.assertEquals("Mandatory Constraint Elements_V1_0_Test.TopicB.Gebaeudeeingang.GEB01 is not true.",logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void valuesOfPath_role0_Fail() {
        Iom_jObject iomObj2=new Iom_jObject(ILI_B_GEBAEUDEEINGANG, "o2");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC_B,"b1"));
        validator.validate(new ObjectEvent(iomObj2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        Assert.assertEquals(1,logger.getErrs().size());
        Assert.assertEquals("Mandatory Constraint Elements_V1_0_Test.TopicB.Gebaeudeeingang.GEB01 is not true.",logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void values_attr_value1_Ok() {
        Iom_jObject iomObj1=new Iom_jObject(ILI_C_LOKALISATION1, "o1");
        iomObj1.setattrvalue(ILI_C_LOKALISATION1_NAME,TEST_WERT);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC_A,"b1"));
        validator.validate(new ObjectEvent(iomObj1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        Assert.assertEquals(0,logger.getErrs().size());
    }
    @Test
    public void values_attr_value1_Fail() {
        Iom_jObject iomObj1=new Iom_jObject(ILI_C_LOKALISATION1, "o1");
        iomObj1.setattrvalue(ILI_C_LOKALISATION1_NAME,"x"+TEST_WERT);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC_A,"b1"));
        validator.validate(new ObjectEvent(iomObj1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        Assert.assertEquals(1,logger.getErrs().size());
        Assert.assertEquals("Mandatory Constraint Elements_V1_0_Test.TopicC.Lokalisation1.LOK01 is not true.",logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void values_attr_value2_Ok() {
        Iom_jObject iomObj1=new Iom_jObject(ILI_C_LOKALISATION2, "o1");
        iomObj1.setattrvalue(ILI_C_LOKALISATION2_NAME,TEST_WERT);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC_A,"b1"));
        validator.validate(new ObjectEvent(iomObj1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        Assert.assertEquals(0,logger.getErrs().size());
    }
    @Test
    public void values_attr_value2_Fail() {
        Iom_jObject iomObj1=new Iom_jObject(ILI_C_LOKALISATION2, "o1");
        iomObj1.setattrvalue(ILI_C_LOKALISATION2_NAME,"x"+TEST_WERT);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC_A,"b1"));
        validator.validate(new ObjectEvent(iomObj1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        Assert.assertEquals(1,logger.getErrs().size());
        Assert.assertEquals("Mandatory Constraint Elements_V1_0_Test.TopicC.Lokalisation2.LOK02 is not true.",logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void coalesceN() {
        String[][] testCases = {
                { "Input is defined", "42", "7", "42" },
                { "Input UNDEFINED", null, "7", "7" },
                { "Default UNDEFINED", "19", null, "19"},
                { "Both UNDEFINED", null, null, null },
        };

        for (int i = 0; i < testCases.length; i++) {
            String[] testCase = testCases[i];
            Iom_jObject iomObj = new Iom_jObject(CLASS_COALESCE_NUMERIC_TEST, "o" + i);
            if (testCase[1] != null) iomObj.setattrvalue("InputValue", testCase[1]);
            if (testCase[2] != null) iomObj.setattrvalue("DefaultValue", testCase[2]);
            if (testCase[3] != null) iomObj.setattrvalue("Expected", testCase[3]);

            LogCollector logger = ValidatorTestHelper.validateObjects(td, ILI_TOPIC_D, iomObj);

            assertEquals("Test case: " + testCase[0] + " (coalesceN(" + testCase[1] + ", " + testCase[2] + ") = " + testCase[3] + ")", 0, logger.getErrs().size());
        }
    }

    /**
     * Test case that ensures that coalesceN (coalesceNumeric) is actually executed and can fail.
     */
    @Test
    public void coalesceNFail() {
        Iom_jObject iomObj = new Iom_jObject(CLASS_COALESCE_NUMERIC_TEST, "o1");
        iomObj.setattrvalue("InputValue", "42");
        iomObj.setattrvalue("DefaultValue", "7");
        iomObj.setattrvalue("Expected", "67");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, ILI_TOPIC_D, iomObj);

        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint Elements_V1_0_Test.TopicD.CoalesceTest.coalesceNumericTest is not true.");
    }
}
