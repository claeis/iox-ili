package ch.interlis.iox_j.validator;

import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom_j.Iom_jObject;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class ObjectPool24Test {
    private final static String MODEL = "ObjectPool24_Test";
    private final static String TOPIC = MODEL + ".Topic";
    private final static String CLASS_A = TOPIC + ".ClassA";
    private final static String CLASS_B = TOPIC + ".ClassB";
    private final static String CLASS_C = TOPIC + ".ClassC";
    private final static String CLASS_COALESCE_NUMERIC_TEST = TOPIC + ".CoalesceTest";
    private TransferDescription td;

    @Before
    public void setUp() {
        Configuration ili2cConfig = new Configuration();
        FileEntry functionIli = new FileEntry("src/test/data/validator/ObjectPool.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(functionIli);

        FileEntry modelIli = new FileEntry("src/test/data/validator/ObjectPool24_Test.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(modelIli);

        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }

    @Test
    public void allObjects() {
        Iom_jObject objectA1 = new Iom_jObject(CLASS_A, "o1");
        Iom_jObject objectA2 = new Iom_jObject(CLASS_A, "o2");
        Iom_jObject objectB = new Iom_jObject(CLASS_B, "o3");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, objectA1, objectB, objectA2);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void allObjectsNoInstances() {
        Iom_jObject objectB = new Iom_jObject(CLASS_B, "o1");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, objectB);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint ObjectPool24_Test.Topic.ClassB.allOfClassA is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void allObjectsInvalidCount() {
        Iom_jObject objectB = new Iom_jObject(CLASS_B, "o1");
        Iom_jObject objectA1 = new Iom_jObject(CLASS_A, "o2");
        Iom_jObject objectA2 = new Iom_jObject(CLASS_A, "o3");
        Iom_jObject objectA3 = new Iom_jObject(CLASS_A, "o4");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, objectA1, objectA2, objectB, objectA3);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint ObjectPool24_Test.Topic.ClassB.allOfClassA is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void allObjectsView() {
        Iom_jObject objectC = new Iom_jObject(CLASS_C, "o1");
        Iom_jObject objectA1 = createObjectA("o2", "test");
        Iom_jObject objectA2 = createObjectA("o3", "test2");
        Iom_jObject objectA3 = createObjectA("o4", "test3");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, objectC, objectA1, objectA2, objectA3);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getErrs(), is(empty()));
    }

    @Test
    public void allObjectsViewInvalidCount() {
        Iom_jObject objectC = new Iom_jObject(CLASS_C, "o1");
        Iom_jObject objectA1 = createObjectA("o2", "test");
        Iom_jObject objectA2 = createObjectA("o3", "");
        Iom_jObject objectA3 = createObjectA("o4", "test");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, objectA1, objectA2, objectC, objectA3);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint ObjectPool24_Test.Topic.ClassC.allOfViewA is not true.");
        assertThat(logger.getWarn(), is(empty()));
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

            LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);

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

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, iomObj);

        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint ObjectPool24_Test.Topic.CoalesceTest.coalesceNumericTest is not true.");
    }

    private Iom_jObject createObjectA(String oid, String attrValue) {
        Iom_jObject object = new Iom_jObject(CLASS_A, oid);
        object.setattrvalue("attrA", attrValue);
        return object;
    }
}
