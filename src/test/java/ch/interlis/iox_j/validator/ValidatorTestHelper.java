package ch.interlis.iox_j.validator;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.xtf.Xtf24Reader;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.PipelinePool;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;
import org.junit.Assert;

import java.io.File;

import static org.junit.Assert.assertNotNull;

public class ValidatorTestHelper {
    private static final String DEFAULT_BASKET_ID = "b1";

    public static TransferDescription compileIliFile(String filename) {
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(filename, FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        try {
            td = ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        } catch (Ili2cFailure e) {
            Assert.fail("Could not compile ili file <" + filename + ">: " + e.getMessage());
        }
        assertNotNull(td);

        return td;
    }

    public static LogCollector validateObjects(TransferDescription td, String topic, IomObject... objects) {
        return validateObjects(td, topic, DEFAULT_BASKET_ID, new ValidationConfig(), objects);
    }

    public static LogCollector validateObjects(TransferDescription td, String topic, String baskedId, ValidationConfig modelConfig, IomObject... objects) {
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        PipelinePool pool = new PipelinePool();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, pool, settings);

        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(topic, baskedId));
        for (IomObject object : objects) {
            validator.validate(new ObjectEvent(object));
        }
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        return logger;
    }

    public static LogCollector validateObjectsFromXtf24(TransferDescription td, File xtfFile) throws IoxException {
        EhiLogger.getInstance().setTraceFilter(false);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        PipelinePool pool = new PipelinePool();
        Settings settings = new Settings();
        ValidationConfig modelConfig = new ValidationConfig();
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
