package ch.interlis.iox_j.validator;

import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import org.junit.Before;
import org.junit.Test;

import static ch.interlis.iox_j.validator.LogCollectorAssertions.AssertAllEventMessages;
import static org.junit.Assert.assertEquals;

public class ViewSelectionTest {
    private static final String ILI_FILE = "src/test/data/validator/ViewSelection.ili";
    private static final String MAIN_TOPIC = "ViewSelection.TopicA";
    private static final String CLASS_MANDATORY = MAIN_TOPIC + ".ClassMandatory";
    private static final String CLASS_PLAUSIBILITY = MAIN_TOPIC + ".ClassPlausibility";
    private static final String CLASS_EXISTENCE_A = MAIN_TOPIC + ".ClassExistenceA";
    private static final String CLASS_EXISTENCE_B = MAIN_TOPIC + ".ClassExistenceB";
    private static final String CLASS_UNIQUE = MAIN_TOPIC + ".ClassUnique";
    private static final String CLASS_MULTIPLE_WHERE = MAIN_TOPIC + ".ClassMultipleWhere";
    private static final String SET_TOPIC = "ViewSelectionSet.TopicA";
    private static final String CLASS_SET = SET_TOPIC + ".ClassSet";

    private TransferDescription td;

    @Before
    public void setUp() {
        td = ValidatorTestHelper.compileIliFile(ILI_FILE);
    }

    @Test
    public void mandatoryConstraintOk() {
        LogCollector logger = validateObjects(
                MAIN_TOPIC,
                createTestObjectCodeAndName(CLASS_MANDATORY, "o1", "codeA", "Name for A"),
                createTestObjectCodeAndName(CLASS_MANDATORY, "o2", "codeB", "B"),
                createTestObjectCodeAndName(CLASS_MANDATORY, "o3", "codeC", "Name for C")
        );

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void mandatoryConstraintNameTooShort() {
        LogCollector logger = validateObjects(
                MAIN_TOPIC,
                createTestObjectCodeAndName(CLASS_MANDATORY, "o1", "codeA", "A"),
                createTestObjectCodeAndName(CLASS_MANDATORY, "o2", "codeB", "Name for B"),
                createTestObjectCodeAndName(CLASS_MANDATORY, "o3", "codeC", "C")
        );

        AssertAllEventMessages(
                logger.getErrs(),
                "Mandatory Constraint ViewSelection.TopicA.ViewMandatory.NameLength is not true."
        );
    }

    @Test
    public void plausibilityConstraintOk() {
        LogCollector logger = validateObjects(
                MAIN_TOPIC,
                createTestObjectCodeAndName(CLASS_PLAUSIBILITY, "o1", "codeA", "A"),
                createTestObjectCodeAndName(CLASS_PLAUSIBILITY, "o2", "codeB", "B"),
                createTestObjectCodeAndName(CLASS_PLAUSIBILITY, "o3", "codeB", "Name for B"),
                createTestObjectCodeAndName(CLASS_PLAUSIBILITY, "o4", "codeC", "C"),
                createTestObjectCodeAndName(CLASS_PLAUSIBILITY, "o5", "codeA", "Other Name")
        );

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void plausibilityConstraintNamesTooShort() {
        LogCollector logger = validateObjects(
                MAIN_TOPIC,
                createTestObjectCodeAndName(CLASS_PLAUSIBILITY, "o1", "codeA", "A"),
                createTestObjectCodeAndName(CLASS_PLAUSIBILITY, "o2", "codeB", "B"),
                createTestObjectCodeAndName(CLASS_PLAUSIBILITY, "o3", "codeB", "Name for B"),
                createTestObjectCodeAndName(CLASS_PLAUSIBILITY, "o4", "codeC", "C"),
                createTestObjectCodeAndName(CLASS_PLAUSIBILITY, "o5", "codeA", "Other Name"),
                createTestObjectCodeAndName(CLASS_PLAUSIBILITY, "o6", "codeA", "abc")
        );

        AssertAllEventMessages(
                logger.getErrs(),
                "Plausibility Constraint ViewSelection.TopicA.ViewPlausibility.NameLengthPlausibility is not true."
        );
    }

    @Test
    public void existenceConstraintOk() {
        LogCollector logger = validateObjects(
                MAIN_TOPIC,
                createTestObjectCodeAndName(CLASS_EXISTENCE_A, "o1", "codeA", "Name matches"),
                createTestObjectCodeAndName(CLASS_EXISTENCE_A, "o2", "codeB", "Not existing Name with code B"),
                createTestObjectCodeAndName(CLASS_EXISTENCE_A, "o3", "codeC", "C"),
                createTestObjectCodeAndName(CLASS_EXISTENCE_B, "o4", "codeB", "Name matches"),
                createTestObjectCodeAndName(CLASS_EXISTENCE_B, "o5", "codeA", "Other Name in class B")
        );

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void existenceConstraintMissing() {
        LogCollector logger = validateObjects(
                MAIN_TOPIC,
                createTestObjectCodeAndName(CLASS_EXISTENCE_A, "o1", "codeA", "Name matches"),
                createTestObjectCodeAndName(CLASS_EXISTENCE_A, "o2", "codeB", "Not existing Name with code B"),
                createTestObjectCodeAndName(CLASS_EXISTENCE_A, "o3", "codeA", "Missing"),
                createTestObjectCodeAndName(CLASS_EXISTENCE_B, "o4", "codeB", "Name matches")
        );

        AssertAllEventMessages(
                logger.getErrs(),
                "Existence constraint ViewSelection.TopicA.ViewExistence.ExistsInB is violated! The value of the attribute Name of ViewSelection.TopicA.ClassExistenceA was not found in the condition class."
        );
    }

    @Test
    public void uniqueConstraintOk() {
        LogCollector logger = validateObjects(
                MAIN_TOPIC,
                createTestObjectCodeAndName(CLASS_UNIQUE, "o1", "codeA", "Same Name"),
                createTestObjectCodeAndName(CLASS_UNIQUE, "o2", "codeB", "Name for B"),
                createTestObjectCodeAndName(CLASS_UNIQUE, "o3", "codeB", "Name for B"),
                createTestObjectCodeAndName(CLASS_UNIQUE, "o4", "codeC", "Same Name"),
                createTestObjectCodeAndName(CLASS_UNIQUE, "o5", "codeA", "Other Name")
        );

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void uniqueConstraintFails() {
        LogCollector logger = validateObjects(
                MAIN_TOPIC,
                createTestObjectCodeAndName(CLASS_UNIQUE, "o1", "codeB", "Name for B"),
                createTestObjectCodeAndName(CLASS_UNIQUE, "o2", "codeC", "Same Name"),
                createTestObjectCodeAndName(CLASS_UNIQUE, "o3", "codeA", "Same Name"),
                createTestObjectCodeAndName(CLASS_UNIQUE, "o4", "codeA", "Other Name"),
                createTestObjectCodeAndName(CLASS_UNIQUE, "o5", "codeA", "Same Name")
        );

        AssertAllEventMessages(
                logger.getErrs(),
                "Unique constraint ViewSelection.TopicA.ViewUnique.NameUniqueCodeA is violated! Values Same Name already exist in Object: o3"
        );
    }

    @Test
    public void multipleWhereOk() {
        LogCollector logger = validateObjects(
                MAIN_TOPIC,
                createTestObjectCodeAndName(CLASS_MULTIPLE_WHERE, "o1", "codeA", "Same Name"),
                createTestObjectCodeAndName(CLASS_MULTIPLE_WHERE, "o2", "codeB", "Same Name"),
                createTestObjectCodeAndName(CLASS_MULTIPLE_WHERE, "o3", "codeA", "abc"),
                createTestObjectCodeAndName(CLASS_MULTIPLE_WHERE, "o4", "codeA", "abc"),
                createTestObjectCodeAndName(CLASS_MULTIPLE_WHERE, "o5", "codeB", "abc")
        );

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void multipleWhereFails() {
        LogCollector logger = validateObjects(
                MAIN_TOPIC,
                createTestObjectCodeAndName(CLASS_MULTIPLE_WHERE, "o1", "codeA", "Same Name"),
                createTestObjectCodeAndName(CLASS_MULTIPLE_WHERE, "o2", "codeB", "Same Name"),
                createTestObjectCodeAndName(CLASS_MULTIPLE_WHERE, "o3", "codeA", "Same Name"),
                createTestObjectCodeAndName(CLASS_MULTIPLE_WHERE, "o4", "codeA", "abc"),
                createTestObjectCodeAndName(CLASS_MULTIPLE_WHERE, "o5", "codeA", "abc"),
                createTestObjectCodeAndName(CLASS_MULTIPLE_WHERE, "o6", "codeB", "abc")
        );

        AssertAllEventMessages(
                logger.getErrs(),
                "Unique constraint ViewSelection.TopicA.ViewMultipleWhere.NameUnique is violated! Values Same Name already exist in Object: o1"
        );
    }

    @Test
    public void setConstraintOk() {
        LogCollector logger = validateObjects(
                SET_TOPIC,
                createTestObjectCodeAndName(CLASS_SET, "o1", "codeA", "Some Name"),
                createTestObjectCodeAndName(CLASS_SET, "o2", "codeB", "Name for B"),
                createTestObjectCodeAndName(CLASS_SET, "o3", "codeB", "Other Name for B"),
                createTestObjectCodeAndName(CLASS_SET, "o4", "codeC", "Some Name"),
                createTestObjectCodeAndName(CLASS_SET, "o5", "codeA", "Other Name")
        );

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void setConstraintNotEnoughObjects() {
        LogCollector logger = validateObjects(
                SET_TOPIC,
                createTestObjectCodeAndName(CLASS_SET, "o1", "codeA", "Some Name"),
                createTestObjectCodeAndName(CLASS_SET, "o2", "codeB", "Name for B")
        );

        AssertAllEventMessages(
                logger.getErrs(),
                "Set Constraint ViewSelectionSet.TopicA.ViewSet.ObjectCount is not true."
        );
    }

    private IomObject createTestObjectCodeAndName(String className, String oid, String code, String name) {
        IomObject iomObj = new Iom_jObject(className, oid);
        iomObj.setattrvalue("Code", code);
        iomObj.setattrvalue("Name", name);
        return iomObj;
    }

    private LogCollector validateObjects(String topic, IomObject... objects) {
        return ValidatorTestHelper.validateObjects(td, topic, objects);
    }
}
