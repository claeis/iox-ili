package ch.interlis.iox_j.validator;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
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

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class UniqueConstraints24Test {

    private static final String MODEL = "ModelA";
    private static final String TOPIC_CALCULATED_ATTRIBUTE = MODEL + ".UniqueCalculatedAttribute";

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
        FileEntry fileEntry2 = new FileEntry("src/test/data/validator/Text_V2.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry2);
        FileEntry fileEntry=new FileEntry("src/test/data/validator/UniqueConstraints24.ili", FileEntryKind.ILIMODELFILE);
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
    public void UniqueConstraintGlobal_Ok(){
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
        LogCollectorAssertions.AssertContainsError("UniqueConstraintGlobal",0, logger);
    }

    @Test
    public void UniqueConstraintGlobal_Fail(){
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("ModelA.TopicA", UUID.randomUUID().toString()));
        validator.validate(CreateClassA(1));
        validator.validate(CreateClassA(2));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent("ModelA.TopicA", UUID.randomUUID().toString()));
        validator.validate(CreateClassA(3));
        validator.validate(CreateClassA(1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        LogCollectorAssertions.AssertContainsError("UniqueConstraintGlobal", 1, logger);
    }

    @Test
    public void UniqueConstraintBasket_Ok(){
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("ModelA.TopicA", UUID.randomUUID().toString()));
        validator.validate(CreateClassA(1));
        validator.validate(CreateClassA(2));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent("ModelA.TopicA", UUID.randomUUID().toString()));
        validator.validate(CreateClassA(1));
        validator.validate(CreateClassA(2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        LogCollectorAssertions.AssertContainsError("UniqueConstraintBasket",0, logger);
    }

    @Test
    public void UniqueConstraintBasket_Fail(){
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("ModelA.TopicA", UUID.randomUUID().toString()));
        validator.validate(CreateClassA(1));
        validator.validate(CreateClassA(2));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent("ModelA.TopicA", UUID.randomUUID().toString()));
        validator.validate(CreateClassA(1));
        validator.validate(CreateClassA(1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        LogCollectorAssertions.AssertContainsError("UniqueConstraintBasket", 1, logger);
    }

    @Test
    public void UniqueConstraintCalculatedAttribute_Fail() {
        IomObject obj1 = new Iom_jObject(TOPIC_CALCULATED_ATTRIBUTE + ".A", "o1");
        obj1.setattrvalue("text", "SALAmander");
        IomObject obj2 = new Iom_jObject(TOPIC_CALCULATED_ATTRIBUTE + ".A", "o2");
        obj2.setattrvalue("text", "salaMANDER");

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_CALCULATED_ATTRIBUTE, obj1, obj2);
        LogCollectorAssertions.AssertContainsError("CalculatedUniqueOnClass", 1, logger);
        LogCollectorAssertions.AssertContainsError("CalculatedUniqueOnView", 1, logger);
        assertThat(logger.getWarn(), is(empty()));
    }

    @Test
    public void UniqueConstraintCalculatedStructAttribute_Fail() {
        IomObject struct1 = new Iom_jObject(TOPIC_CALCULATED_ATTRIBUTE + ".Struct", null);
        struct1.setattrvalue("badge", "1");
        IomObject obj1 = new Iom_jObject(TOPIC_CALCULATED_ATTRIBUTE + ".A", "o1");
        obj1.addattrobj("id", struct1);

        IomObject struct2 = new Iom_jObject(TOPIC_CALCULATED_ATTRIBUTE + ".Struct", null);
        struct2.setattrvalue("badge", "1");
        IomObject obj2 = new Iom_jObject(TOPIC_CALCULATED_ATTRIBUTE + ".A", "o2");
        obj2.addattrobj("id", struct2);

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC_CALCULATED_ATTRIBUTE, obj1, obj2);
        LogCollectorAssertions.AssertContainsError("UniqueIdBadgeOnClass", 1, logger);
        LogCollectorAssertions.AssertContainsError("UniqueIdBadgeOnView", 1, logger);
        assertThat(logger.getWarn(), is(empty()));
    }
}
