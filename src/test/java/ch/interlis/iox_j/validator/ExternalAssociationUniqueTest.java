package ch.interlis.iox_j.validator;

import ch.ehi.basics.settings.Settings;
import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox_j.validator.LogCollector;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.PipelinePool;
import ch.interlis.iom_j.xtf.Xtf24Reader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExternalAssociationUniqueTest {
    private static final String TEST_DIR = "src/test/data/validator";
    private static final String ILI_FILE = TEST_DIR + "/ExternalAssociationUnique.ili";
    private static final String XTF_FILE = TEST_DIR + "/ExternalAssociationUnique.xtf";

    private TransferDescription td;

    @Before
    public void setUp() {
        td = ValidatorTestHelper.compileIliFile(ILI_FILE);
    }

    @Test
    public void uniqueExternalRole_allObjectsAccessible_Fail() throws IoxException {
        File xtfFile = new File(XTF_FILE);
        Settings settings = new Settings();
        List<Class> resolverClasses = new ArrayList<Class>();
        resolverClasses.add(ExternalObjResolverMock.class);
        settings.setTransientObject(Validator.CONFIG_OBJECT_RESOLVERS, resolverClasses);

        LogCollector logger = validateWithConfig(xtfFile, settings, ValidationConfig.TRUE);

        assertEquals(1, logger.getErrs().size());
        assertEquals("Unique constraint ExternalAssociationUnique.T.Ownership.ConstraintOwnership is violated! Values ExternalObjResolverMock.1, ExternalAssociationUnique.T.Item oid i1 {Code abc} already exist in Object: link1", logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void uniqueExternalRole_allObjectsNotAccessible_Ok() throws IoxException {
        File xtfFile = new File(XTF_FILE);
        Settings settings = new Settings();
        List<Class> resolverClasses = new ArrayList<Class>();
        resolverClasses.add(ExternalObjResolverMock.class);
        settings.setTransientObject(Validator.CONFIG_OBJECT_RESOLVERS, resolverClasses);

        LogCollector logger = validateWithConfig(xtfFile, settings, ValidationConfig.FALSE);

        assertEquals(0, logger.getErrs().size());
    }

    private LogCollector validateWithConfig(File xtfFile, Settings settings, String allObjectsAccessible) throws IoxException {
        EhiLogger.getInstance().setTraceFilter(false);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        PipelinePool pool = new PipelinePool();
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, allObjectsAccessible);
        Validator validator = new Validator(td, modelConfig, logger, errFactory, pool, settings);

        Xtf24Reader reader = new Xtf24Reader(xtfFile);
        reader.setModel(td);
        IoxEvent event;
        do {
            event = reader.read();
            validator.validate(event);
        } while (!(event instanceof ch.interlis.iox.EndTransferEvent));

        return logger;
    }
}
