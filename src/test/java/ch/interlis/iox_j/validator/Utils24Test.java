package ch.interlis.iox_j.validator;

import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom_j.Iom_jObject;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class Utils24Test {
    private final static String MODEL = "Utils24_Test";
    private final static String TOPIC = MODEL + ".Topic";
    private final static String CLASS_A = TOPIC + ".ClassA";
    private final static String CLASS_B = TOPIC + ".ClassB";
    private TransferDescription td;

    @Before
    public void setUp() {
        Configuration ili2cConfig = new Configuration();
        FileEntry functionIli = new FileEntry("src/test/data/validator/Utils.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(functionIli);

        FileEntry modelIli = new FileEntry("src/test/data/validator/Utils24_Test.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(modelIli);

        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }

    @Test
    public void findObjects() {
        Iom_jObject objectA1 = new Iom_jObject(CLASS_A, "o1");
        Iom_jObject objectA2 = new Iom_jObject(CLASS_A, "o2");
        Iom_jObject objectB = new Iom_jObject(CLASS_B, "o3");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, objectA1, objectB, objectA2);
        assertThat(logger.getErrs(), is(empty()));
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void findObjectsNoInstances() {
        Iom_jObject objectB = new Iom_jObject(CLASS_B, "o1");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, objectB);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint Utils24_Test.Topic.ClassB.findClassA is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void findObjectsInvalidCount() {
        Iom_jObject objectB = new Iom_jObject(CLASS_B, "o1");
        Iom_jObject objectA1 = new Iom_jObject(CLASS_A, "o2");
        Iom_jObject objectA2 = new Iom_jObject(CLASS_A, "o3");
        Iom_jObject objectA3 = new Iom_jObject(CLASS_A, "o4");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, objectA1, objectA2, objectB, objectA3);
        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint Utils24_Test.Topic.ClassB.findClassA is not true.");
        assertThat(logger.getWarn(), is(empty()));
    }
}
