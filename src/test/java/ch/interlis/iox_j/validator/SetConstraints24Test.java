package ch.interlis.iox_j.validator;

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
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;

public class SetConstraints24Test {

    private TransferDescription td=null;
    private Validator validator=null;
    private LogCollector logger=null;

    private ObjectEvent CreateClassA(int attr1){
        Iom_jObject iomJObject = new Iom_jObject("ModelA.TopicA.ClassA", UUID.randomUUID().toString());
        iomJObject.addattrvalue("attr1", Integer.toString(attr1));
        return new ObjectEvent(iomJObject);
    }

    @Before
    public void setUp() throws Exception {
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry("src/test/data/validator/SetConstraints24.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        assertNotNull(td);

        ValidationConfig modelConfig=new ValidationConfig();
        logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        validator=new Validator(td, modelConfig, logger, errFactory, settings);
    }

    @Test
    public void SetConstraintGlobal_Ok(){
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("ModelA.TopicA", UUID.randomUUID().toString()));
        validator.validate(CreateClassA(1));
        validator.validate(CreateClassA(2));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent("ModelA.TopicA", UUID.randomUUID().toString()));
        validator.validate(CreateClassA(3));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        LogCollectorAssertions.AssertContainsError("SetConstraintGlobal",0, logger);
    }

    @Test
    public void SetConstraintGlobal_Fail(){
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("ModelA.TopicA", UUID.randomUUID().toString()));
        validator.validate(CreateClassA(1));
        validator.validate(CreateClassA(2));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent("ModelA.TopicA", UUID.randomUUID().toString()));
        validator.validate(CreateClassA(3));
        validator.validate(CreateClassA(4));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        LogCollectorAssertions.AssertContainsError("SetConstraintGlobal", 1, logger);
    }

    @Test
    public void SetConstraintBasket_Ok(){
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("ModelA.TopicA", UUID.randomUUID().toString()));
        validator.validate(CreateClassA(1));
        validator.validate(CreateClassA(2));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent("ModelA.TopicA", UUID.randomUUID().toString()));
        validator.validate(CreateClassA(1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        LogCollectorAssertions.AssertContainsError("SetConstraintBasket",0, logger);
    }

    @Test
    public void SetConstraintBasket_Fail(){
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("ModelA.TopicA", UUID.randomUUID().toString()));
        validator.validate(CreateClassA(1));
        validator.validate(CreateClassA(2));
        validator.validate(CreateClassA(3));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent("ModelA.TopicA", UUID.randomUUID().toString()));
        validator.validate(CreateClassA(1));
        validator.validate(CreateClassA(2));
        validator.validate(CreateClassA(3));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        LogCollectorAssertions.AssertContainsError("SetConstraintBasket", 2, logger);
    }
}