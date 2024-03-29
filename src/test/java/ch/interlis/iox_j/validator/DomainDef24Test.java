package ch.interlis.iox_j.validator;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.PipelinePool;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static ch.interlis.iox_j.validator.LogCollectorAssertions.*;

public class DomainDef24Test {
    private TransferDescription td = null;
    private Validator validator;
    private LogCollector logger = null;

    @Before
    public void setUp() throws Exception {
        td = ValidatorTestHelper.compileIliFile("src/test/data/validator/DomainDef.ili");

        ValidationConfig modelConfig = new ValidationConfig();
        logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        validator = new Validator(td, modelConfig, logger, errFactory, new PipelinePool(), settings);
    }

    @Test
    public void domainText() {
        final String domainText = "DomainText";
        validateObjectUnderTest(domainText, "This String has 24 chars");
        AssertContainsError(domainText, 0, logger);

        logger.clear();
        validateObjectUnderTest("DomainText", "This String has over 30 characters.");
        AssertContainsError("Attribute DomainText is length restricted to 30", 1, logger);

    }

    @Test
    public void domainTextRestrictedThis() {
        final String domainTextRestrictedThis = "DomainTextRestrictedThis";
        validateObjectUnderTest(domainTextRestrictedThis, "SomeConstant");
        AssertContainsError(domainTextRestrictedThis, 0, logger);

        logger.clear();
        validateObjectUnderTest(domainTextRestrictedThis, "Not the required Constant");
        AssertContainsError("Attribute DomainTextRestrictedThis does not satisfy the domain constraint ModelA.TopicA.DomainTextRestrictedThis.Values", 1, logger);
    }

    @Test
    public void domainConstraintThisFunction() {
        final String domainConstraintThisFunction = "DomainConstraintThisFunction";
        validateObjectUnderTest(domainConstraintThisFunction, "This String has 24 chars");
        AssertContainsError(domainConstraintThisFunction, 0, logger);

        logger.clear();
        validateObjectUnderTest(domainConstraintThisFunction, "Too few chars");
        AssertContainsError("Attribute DomainConstraintThisFunction does not satisfy the domain constraint ModelA.TopicA.DomainConstraintThisFunction.Length", 1, logger);
    }

    @Test
    public void domainMultiConstraintsNumeric() {
        final String domainMultiConstraintsNumeric = "DomainMultiConstraintsNumeric";
        validateObjectUnderTest(domainMultiConstraintsNumeric, "20");
        AssertContainsError(domainMultiConstraintsNumeric, 0, logger);

        logger.clear();
        validateObjectUnderTest(domainMultiConstraintsNumeric, "9");
        AssertContainsError("Attribute DomainMultiConstraintsNumeric does not satisfy the domain constraint ModelA.TopicA.DomainMultiConstraintsNumeric.Min", 1, logger);

        logger.clear();
        validateObjectUnderTest(domainMultiConstraintsNumeric, "101");
        AssertContainsError("Attribute DomainMultiConstraintsNumeric does not satisfy the domain constraint ModelA.TopicA.DomainMultiConstraintsNumeric.Max100", 1, logger);

        logger.clear();
        validateObjectUnderTest(domainMultiConstraintsNumeric, "201");
        AssertContainsError("value 201 is out of range in attribute DomainMultiConstraintsNumeric", 1, logger);
        AssertContainsError("Attribute DomainMultiConstraintsNumeric does not satisfy the domain constraint ModelA.TopicA.DomainMultiConstraintsNumeric.Max100", 1, logger);
    }

    @Test
    public void domainExtends() {
        final String domainExtends = "DomainExtends";
        validateObjectUnderTest(domainExtends, "20");
        AssertContainsError(domainExtends, 0, logger);

        logger.clear();
        validateObjectUnderTest(domainExtends, "9");
        AssertContainsError("Attribute DomainExtends does not satisfy the domain constraint ModelA.TopicA.DomainMultiConstraintsNumeric.Min", 1, logger);

        logger.clear();
        validateObjectUnderTest(domainExtends, "51");
        AssertContainsError("Attribute DomainExtends does not satisfy the domain constraint ModelA.TopicA.DomainExtends.Max50", 1, logger);

        logger.clear();
        validateObjectUnderTest(domainExtends, "101");
        AssertContainsError("Attribute DomainExtends does not satisfy the domain constraint ModelA.TopicA.DomainExtends.Max50", 1, logger);
        AssertContainsError("Attribute DomainExtends does not satisfy the domain constraint ModelA.TopicA.DomainMultiConstraintsNumeric.Max100", 1, logger);

        logger.clear();
        validateObjectUnderTest(domainExtends, "151");
        AssertContainsError("value 151 is out of range in attribute DomainExtends", 1, logger);
        AssertContainsError("Attribute DomainExtends does not satisfy the domain constraint ModelA.TopicA.DomainExtends.Max50", 1, logger);
        AssertContainsError("Attribute DomainExtends does not satisfy the domain constraint ModelA.TopicA.DomainMultiConstraintsNumeric.Max100", 1, logger);
    }

    private void validateObjectUnderTest(String attributeName, String attributeValue) {
        Iom_jObject iomObj = new Iom_jObject("ModelA.TopicA.ObjectUnderTest", UUID.randomUUID().toString());
        iomObj.setattrvalue(attributeName, attributeValue);

        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("ModelA.TopicA", UUID.randomUUID().toString()));
        validator.validate(new ObjectEvent(iomObj));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
    }

}
