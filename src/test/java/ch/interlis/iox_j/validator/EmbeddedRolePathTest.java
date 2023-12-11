package ch.interlis.iox_j.validator;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom_j.xtf.XtfReader;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.PipelinePool;
import ch.interlis.iox_j.logging.LogEventFactory;
import org.junit.Test;

import java.io.File;

import static ch.interlis.iox_j.validator.LogCollectorAssertions.AssertContainsError;
import static ch.interlis.iox_j.validator.LogCollectorAssertions.AssertContainsInfo;

public class EmbeddedRolePathTest {
    private static final String DATA_DIRECTORY = "src/test/data/validator/";

    @Test
    public void multipleAssociationsWithConstraints() throws IoxException {
        TransferDescription td = ValidatorTestHelper.compileIliFile(DATA_DIRECTORY + "EmbeddedRolePath.ili");
        XtfReader reader = new XtfReader(new File(DATA_DIRECTORY, "EmbeddedRolePath.xtf"));

        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ADDITIONAL_MODELS, "ModelA_AddChecks");

        LogCollector logger = new LogCollector();
        Validator validator = new Validator(td, modelConfig, logger, new LogEventFactory(), new PipelinePool(), new Settings());

        IoxEvent event;
        while ((event = reader.read()) != null) {
            validator.validate(event);
        }
        reader.close();

        assertMandatoryConstraint("ModelA_AddChecks.ModelA_AddCheck.ViewA.Constraint-A-Defined", 1, logger);
        assertMandatoryConstraint("ModelA_AddChecks.ModelA_AddCheck.ViewA.Constraint-A-Count", 1, logger);
        assertMandatoryConstraint("ModelA_AddChecks.ModelA_AddCheck.ViewB.Constraint-B-Defined", 0, logger);
        assertMandatoryConstraint("ModelA_AddChecks.ModelA_AddCheck.ViewB.Constraint-B-Count", 0, logger);
    }

    private static void assertMandatoryConstraint(String constraintName, int errorCount, LogCollector logger) {
        AssertContainsInfo("validate mandatory constraint " + constraintName + "...", 1, logger);
        AssertContainsError("Mandatory Constraint " + constraintName + " is not true.", errorCount, logger);
    }
}
