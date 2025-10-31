package ch.interlis.iox_j.validator;

import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    private final static String TOPIC_FILTER = MODEL + ".FilterTopic";
    private final static String CLASS_FILTER_TEST = TOPIC_FILTER + ".FilterTest";
    private final static String CLASS_FILTER_ALL_TEST = TOPIC_FILTER + ".FilterAllObjectsTest";
    private final static String STRUCT_FILTER_BASE = TOPIC_FILTER + ".StructBase";
    private final static String STRUCT_FILTER_A = TOPIC_FILTER + ".StructA";
    private final static String STRUCT_FILTER_B = TOPIC_FILTER + ".StructB";
    private final static String CLASS_FILTER_SOME_CLASS = TOPIC_FILTER + ".SomeClass";
    private TransferDescription td;

    @Before
    public void setUp() {
        Configuration ili2cConfig = new Configuration();
        FileEntry functionIli = new FileEntry("src/test/data/validator/ObjectPool_V1_0.ili", FileEntryKind.ILIMODELFILE);
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
    public void filter() {
        Iom_jObject testObject = new Iom_jObject(CLASS_FILTER_TEST, "o1");
        Iom_jObject inputObject1 = new Iom_jObject(STRUCT_FILTER_BASE, null);
        inputObject1.setattrvalue("numericAttr", "5");
        testObject.addattrobj("inputObjects", inputObject1);
        Iom_jObject inputObject2 = new Iom_jObject(STRUCT_FILTER_BASE, null);
        inputObject2.setattrvalue("numericAttr", "42");
        testObject.addattrobj("inputObjects", inputObject2);
        testObject.setattrvalue("filterExpression", "numericAttr > 10");
        testObject.setattrvalue("expectedCount", "1");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_FILTER, testObject);
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void filterNoObjects() {
        Iom_jObject testObject = new Iom_jObject(CLASS_FILTER_TEST, "o1");
        testObject.setattrvalue("filterExpression", "numericAttr > 10");
        testObject.setattrvalue("expectedCount", "0");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_FILTER, testObject);
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void filterExtendedObjects() {
        Iom_jObject testObject = new Iom_jObject(CLASS_FILTER_TEST, "o1");
        Iom_jObject inputObject1 = new Iom_jObject(STRUCT_FILTER_A, null);
        inputObject1.setattrvalue("numericAttr", "70");
        inputObject1.setattrvalue("textAttr", "test");
        testObject.addattrobj("inputObjects", inputObject1);
        Iom_jObject inputObject2 = new Iom_jObject(STRUCT_FILTER_B, null);
        inputObject2.setattrvalue("numericAttr", "35");
        inputObject2.setattrvalue("enumAttr", "red");
        testObject.addattrobj("inputObjects", inputObject2);
        testObject.setattrvalue("filterExpression", "numericAttr == 35");
        testObject.setattrvalue("expectedCount", "1");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_FILTER, testObject);
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void filterReferencingCurrentObjectNumericAttr() {
        Iom_jObject testObject = new Iom_jObject(CLASS_FILTER_TEST, "o1");
        testObject.addattrobj("inputObjects", new Iom_jObject(STRUCT_FILTER_BASE, null));
        Iom_jObject inputObject1 = new Iom_jObject(STRUCT_FILTER_BASE, null);
        inputObject1.setattrvalue("numericAttr", "5");
        testObject.addattrobj("inputObjects", inputObject1);
        Iom_jObject inputObject2 = new Iom_jObject(STRUCT_FILTER_BASE, null);
        inputObject2.setattrvalue("numericAttr", "42");
        testObject.addattrobj("inputObjects", inputObject2);
        testObject.setattrvalue("filterNumber", "42");
        testObject.setattrvalue("filterExpression", "numericAttr == {filterNumber}");
        testObject.setattrvalue("expectedCount", "1");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_FILTER, testObject);
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void filterReferencingCurrentObjectTextAttr() {
        Iom_jObject testObject = new Iom_jObject(CLASS_FILTER_TEST, "o1");
        testObject.addattrobj("inputObjects", new Iom_jObject(STRUCT_FILTER_A, null));
        Iom_jObject inputObject1 = new Iom_jObject(STRUCT_FILTER_A, null);
        inputObject1.setattrvalue("textAttr", "ALABSI");
        testObject.addattrobj("inputObjects", inputObject1);
        Iom_jObject inputObject2 = new Iom_jObject(STRUCT_FILTER_A, null);
        inputObject2.setattrvalue("textAttr", "VELAGE");
        testObject.addattrobj("inputObjects", inputObject2);
        testObject.setattrvalue("filterText", "VELAGE");
        testObject.setattrvalue("filterExpression", "textAttr == {filterText}");
        testObject.setattrvalue("expectedCount", "1");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_FILTER, testObject);
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void filterReferencingCurrentObjectEnumAttr() {
        Iom_jObject testObject = new Iom_jObject(CLASS_FILTER_TEST, "o1");
        testObject.addattrobj("inputObjects", new Iom_jObject(STRUCT_FILTER_B, null));
        Iom_jObject inputObject1 = new Iom_jObject(STRUCT_FILTER_B, null);
        inputObject1.setattrvalue("enumAttr", "red");
        testObject.addattrobj("inputObjects", inputObject1);
        Iom_jObject inputObject2 = new Iom_jObject(STRUCT_FILTER_B, null);
        inputObject2.setattrvalue("enumAttr", "green");
        testObject.addattrobj("inputObjects", inputObject2);
        testObject.setattrvalue("filterEnum", "green");
        testObject.setattrvalue("filterExpression", "enumAttr == {filterEnum}");
        testObject.setattrvalue("expectedCount", "1");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_FILTER, testObject);
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void filterReferencingCurrentObjectUnsupported() {
        Iom_jObject testObject = new Iom_jObject(CLASS_FILTER_TEST, "o1");
        Iom_jObject inputObject = new Iom_jObject(STRUCT_FILTER_BASE, null);
        inputObject.setattrvalue("blackboxAttr", "SU5URVJMSVM=");
        testObject.addattrobj("inputObjects", inputObject);
        testObject.setattrvalue("filterExpression", "blackboxAttr == {filterBlackbox}");
        testObject.setattrvalue("expectedCount", "1");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_FILTER, testObject);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Unsupported attribute type for attribute 'filterBlackbox' in class 'ObjectPool24_Test.FilterTopic.FilterTest'",
                "Mandatory Constraint ObjectPool24_Test.FilterTopic.FilterTest.FilterTest is not true.");
    }

    @Test
    public void filterInvalidExpression() {
        Iom_jObject testObject = new Iom_jObject(CLASS_FILTER_TEST, "o1");
        Iom_jObject inputObject = new Iom_jObject(STRUCT_FILTER_BASE, null);
        inputObject.setattrvalue("numericAttr", "1");
        testObject.addattrobj("inputObjects", inputObject);
        testObject.setattrvalue("filterExpression", "INVALID_&");
        testObject.setattrvalue("expectedCount", "1");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_FILTER, testObject);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Failed to parse filter expression in ObjectPool24_Test.FilterTopic.FilterTest.FilterTest: line 1:9: unexpected char: '&'",
                "Mandatory Constraint ObjectPool24_Test.FilterTopic.FilterTest.FilterTest is not true.");
    }

    @Test
    public void filterUnknownAttribute() {
        Iom_jObject testObject = new Iom_jObject(CLASS_FILTER_TEST, "o1");
        Iom_jObject inputObject = new Iom_jObject(STRUCT_FILTER_BASE, null);
        inputObject.setattrvalue("numericAttr", "1");
        testObject.addattrobj("inputObjects", inputObject);
        testObject.setattrvalue("filterExpression", "nonExistingAttr == 10");
        testObject.setattrvalue("expectedCount", "1");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_FILTER, testObject);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Failed to parse filter expression in ObjectPool24_Test.FilterTopic.FilterTest.FilterTest: parse failed <nonExistingAttr == 10>",
                "Mandatory Constraint ObjectPool24_Test.FilterTopic.FilterTest.FilterTest is not true.");
    }

    @Test
    public void filterNonLogicExpression() {
        Iom_jObject testObject = new Iom_jObject(CLASS_FILTER_TEST, "o1");
        Iom_jObject inputObject = new Iom_jObject(STRUCT_FILTER_BASE, null);
        inputObject.setattrvalue("numericAttr", "1");
        testObject.addattrobj("inputObjects", inputObject);
        testObject.setattrvalue("filterExpression", "3 + 5");
        testObject.setattrvalue("expectedCount", "1");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_FILTER, testObject);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Failed to parse filter expression in ObjectPool24_Test.FilterTopic.FilterTest.FilterTest: parse failed <3 + 5>",
                "Mandatory Constraint ObjectPool24_Test.FilterTopic.FilterTest.FilterTest is not true.");
    }

    @Test
    public void filterFail() {
        Iom_jObject testObject = new Iom_jObject(CLASS_FILTER_TEST, "o1");
        testObject.setattrvalue("filterExpression", "numericAttr > 10");
        testObject.setattrvalue("expectedCount", "2");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_FILTER, testObject);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint ObjectPool24_Test.FilterTopic.FilterTest.FilterTest is not true.");
    }

    @Test
    public void filterCombinedWithAllObjects() {
        List<IomObject> inputObjects = new ArrayList<IomObject>();
        for (int i = 0; i < 10; i++) {
            Iom_jObject obj = new Iom_jObject(CLASS_FILTER_SOME_CLASS, "o" + i);
            obj.setattrvalue("numericAttr", Integer.toString(i * 10));
            inputObjects.add(obj);
        }

        Iom_jObject testObj = new Iom_jObject(CLASS_FILTER_ALL_TEST, "o-test");
        testObj.setattrvalue("filterExpression", "numericAttr < 45");
        testObj.setattrvalue("expectedCount", "5");
        inputObjects.add(testObj);

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, inputObjects.toArray(new IomObject[0]));
        assertEquals(0, logger.getErrs().size());
    }

    private Iom_jObject createObjectA(String oid, String attrValue) {
        Iom_jObject object = new Iom_jObject(CLASS_A, oid);
        object.setattrvalue("attrA", attrValue);
        return object;
    }
}
