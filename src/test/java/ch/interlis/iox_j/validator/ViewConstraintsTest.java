package ch.interlis.iox_j.validator;

import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import org.junit.Before;
import org.junit.Test;

import static ch.interlis.iox_j.validator.LogCollectorAssertions.AssertAllEventMessages;
import static org.junit.Assert.assertEquals;

public class ViewConstraintsTest {
    private static final String ILI_FILE = "src/test/data/validator/ViewConstraints.ili";
    private static final String BASKET_ID = "b1";
    private static final String MAIN_TOPIC = "ViewConstraints.TopicA";
    private static final String CLASS_MANDATORY = MAIN_TOPIC + ".ClassMandatory";
    private static final String CLASS_PLAUSIBILITY = MAIN_TOPIC + ".ClassPlausibility";
    private static final String CLASS_EXISTENCE_A = MAIN_TOPIC + ".ClassExistenceA";
    private static final String CLASS_EXISTENCE_B = MAIN_TOPIC + ".ClassExistenceB";
    private static final String CLASS_UNIQUE = MAIN_TOPIC + ".ClassUnique";
    private static final String SET_TOPIC = "ViewConstraintsSet.TopicA";
    private static final String CLASS_SET = SET_TOPIC + ".ClassSet";
    private static final String EXTENDING_TOPIC = "ViewConstraintsExtending.ExtendingTopicA";
    private static final String CLASS_EXTENDING = EXTENDING_TOPIC + ".ClassExtending";

    private TransferDescription td;

    @Before
    public void setUp() {
        td = ValidatorTestHelper.compileIliFile(ILI_FILE);
    }

    @Test
    public void mandatoryConstraintMainModelOk() {
        LogCollector logger = validateObjects(
                MAIN_TOPIC,
                createTestObjectName(CLASS_MANDATORY, "o1", "Name"),
                createTestObjectName(CLASS_MANDATORY, "o2", "Some longer name")
        );

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void mandatoryConstraintMainModelTooShort() {
        LogCollector logger = validateObjects(
                MAIN_TOPIC,
                createTestObjectName(CLASS_MANDATORY, "o1", ""),
                createTestObjectName(CLASS_MANDATORY, "o2", "A name that is long enough"),
                createTestObjectName(CLASS_MANDATORY, "o3", "abc")
        );

        AssertAllEventMessages(
                logger.getErrs(),
                "Mandatory Constraint ViewConstraints.TopicA.ViewMandatory.NameLength is not true.",
                "Mandatory Constraint ViewConstraints.TopicA.ViewMandatory.NameLength is not true."
        );
    }

    @Test
    public void mandatoryConstraintAdditionalModelMoreErrors() {
        LogCollector logger = validateObjectsWithAdditionalModel(
                MAIN_TOPIC,
                "AdditionalModelMandatory",
                createTestObjectName(CLASS_MANDATORY, "o1", "Too long for additional model"),
                createTestObjectName(CLASS_MANDATORY, "o2", "A OK"),
                createTestObjectName(CLASS_MANDATORY, "o3", "abc")
        );

        AssertAllEventMessages(
                logger.getErrs(),
                "Mandatory Constraint ViewConstraints.TopicA.ViewMandatory.NameLength is not true.",
                "Mandatory Constraint AdditionalModelMandatory.ViewTopicA.AdditionalViewMandatory.AdditionalNameLength is not true."
        );
    }

    @Test
    public void plausibilityConstraintOk() {
        LogCollector logger = validateObjects(
                MAIN_TOPIC,
                createTestObjectNumber(CLASS_PLAUSIBILITY, "o1", 12),
                createTestObjectNumber(CLASS_PLAUSIBILITY, "o2", 1),
                createTestObjectNumber(CLASS_PLAUSIBILITY, "o3", 1000),
                createTestObjectNumber(CLASS_PLAUSIBILITY, "o4", 2)
        );

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void plausibilityConstraintLessThan100() {
        LogCollector logger = validateObjects(
                MAIN_TOPIC,
                createTestObjectNumber(CLASS_PLAUSIBILITY, "o1", 120),
                createTestObjectNumber(CLASS_PLAUSIBILITY, "o2", 1),
                createTestObjectNumber(CLASS_PLAUSIBILITY, "o3", 1000),
                createTestObjectNumber(CLASS_PLAUSIBILITY, "o4", 20)
        );

        AssertAllEventMessages(
                logger.getErrs(),
                "Plausibility Constraint ViewConstraints.TopicA.ViewPlausibility.LessThan100 is not true."
        );
    }

    @Test
    public void existenceConstraintOk() {
        LogCollector logger = validateObjects(
                MAIN_TOPIC,
                createTestObjectName(CLASS_EXISTENCE_A, "o1", "A"),
                createTestObjectName(CLASS_EXISTENCE_B, "o2", "Name"),
                createTestObjectName(CLASS_EXISTENCE_A, "o3", "Name"),
                createTestObjectName(CLASS_EXISTENCE_B, "o4", "A"),
                createTestObjectName(CLASS_EXISTENCE_B, "o5", "Something else")
        );

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void existenceConstraintMissing() {
        LogCollector logger = validateObjects(
                MAIN_TOPIC,
                createTestObjectName(CLASS_EXISTENCE_A, "o1", "Missing"),
                createTestObjectName(CLASS_EXISTENCE_B, "o2", "Name"),
                createTestObjectName(CLASS_EXISTENCE_A, "o3", "Name"),
                createTestObjectName(CLASS_EXISTENCE_B, "o4", "A")
        );

        AssertAllEventMessages(
                logger.getErrs(),
                "Existence constraint ViewConstraints.TopicA.ViewExistence.ExistsInB is violated! The value of the attribute Name of ViewConstraints.TopicA.ClassExistenceA was not found in the condition class."
        );
    }

    @Test
    public void uniqueConstraintOk() {
        LogCollector logger = validateObjects(
                MAIN_TOPIC,
                createTestObjectName(CLASS_UNIQUE, "o1", "A"),
                createTestObjectName(CLASS_UNIQUE, "o2", "Name"),
                createTestObjectName(CLASS_UNIQUE, "o3", "Test")
        );

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void uniqueConstraintFails() {
        LogCollector logger = validateObjects(
                MAIN_TOPIC,
                createTestObjectName(CLASS_UNIQUE, "o1", "A"),
                createTestObjectName(CLASS_UNIQUE, "o2", "Name"),
                createTestObjectName(CLASS_UNIQUE, "o3", "A")
        );

        AssertAllEventMessages(
                logger.getErrs(),
                "Unique constraint ViewConstraints.TopicA.ViewUnique.NameUnique is violated! Values A already exist in Object: o1"
        );
    }

    @Test
    public void setConstraintOk() {
        LogCollector logger = validateObjects(
                SET_TOPIC,
                createTestObjectName(CLASS_SET, "o1", "A"),
                createTestObjectName(CLASS_SET, "o2", "B"),
                createTestObjectName(CLASS_SET, "o3", "C"),
                createTestObjectName(CLASS_SET, "o4", "D")
        );

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void setConstraintNotEnoughObjects() {
        LogCollector logger = validateObjects(
                SET_TOPIC,
                createTestObjectName(CLASS_SET, "o1", "A"),
                createTestObjectName(CLASS_SET, "o2", "B")
        );

        AssertAllEventMessages(
                logger.getErrs(),
                "Set Constraint ViewConstraintsSet.TopicA.ViewSet.ObjectCount is not true."
        );
    }

    @Test
    public void setConstraintEmptySet() {
        LogCollector logger = validateObjects(
                SET_TOPIC
        );

        AssertAllEventMessages(
                logger.getErrs(),
                "Set Constraint ViewConstraintsSet.TopicA.ViewSet.ObjectCount is not true."
        );
    }

    @Test
    public void extendingConstraintOk() {
        LogCollector logger = validateObjects(
                EXTENDING_TOPIC,
                createTestObjectNumberAndText(CLASS_EXTENDING, "o1", 12, "Some value"),
                createTestObjectNumberAndText(CLASS_EXTENDING, "o2", 250, "Another value"),
                createTestObjectNumberAndText(CLASS_EXTENDING, "o3", 876, "Some other value")
        );

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void extendingConstraintFails() {
        LogCollector logger = validateObjects(
                EXTENDING_TOPIC,
                createTestObjectNumberAndText(CLASS_EXTENDING, "o1", 12, "Some value"),
                createTestObjectNumberAndText(CLASS_EXTENDING, "o2", 250, "A"),
                createTestObjectNumberAndText(CLASS_EXTENDING, "o3", 150, "1234")
        );

        AssertAllEventMessages(
                logger.getErrs(),
                "Mandatory Constraint ViewConstraints.TopicA.ViewBase.LessThan100OrMoreThan200 is not true.",
                "Mandatory Constraint ViewConstraintsExtending.ExtendingTopicA.ViewExtending.NewTextLength is not true."
        );
    }

    private IomObject createTestObjectName(String className, String oid, String name) {
        IomObject iomObj = new Iom_jObject(className, oid);
        iomObj.setattrvalue("Name", name);
        return iomObj;
    }

    private IomObject createTestObjectNumber(String className, String oid, double number) {
        IomObject iomObj = new Iom_jObject(className, oid);
        iomObj.setattrvalue("Number", Double.toString(number));
        return iomObj;
    }

    private IomObject createTestObjectNumberAndText(String className, String oid, double number, String text) {
        IomObject iomObj = createTestObjectNumber(className, oid, number);
        iomObj.setattrvalue("Text", text);
        return iomObj;
    }

    private LogCollector validateObjects(String topic, IomObject... objects) {
        return ValidatorTestHelper.validateObjects(td, topic, objects);
    }

    private LogCollector validateObjectsWithAdditionalModel(String topic, String models, IomObject... objects) {
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ADDITIONAL_MODELS, models);
        return ValidatorTestHelper.validateObjects(td, topic, BASKET_ID, modelConfig, objects);
    }
}
