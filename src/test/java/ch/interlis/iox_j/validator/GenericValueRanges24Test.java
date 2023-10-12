package ch.interlis.iox_j.validator;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;
import org.junit.Test;

import java.util.HashMap;

import static ch.interlis.iox_j.validator.LogCollectorAssertions.AssertContainsError;
import static org.junit.Assert.assertEquals;

public class GenericValueRanges24Test {

    private static final String DATA_DIRECTORY = "src/test/data/validator/GenericValueRanges/";

    private IomObject createTestClassIomObjectLv03(String tag) {
        IomObject iomObj = new Iom_jObject(tag, "o1");
        iomObj.addattrobj("attr1", IomObjectHelper.createCoord("460002.0", "45002.0"));

        return iomObj;
    }

    @Test
    public void missingContext() {
        TransferDescription td = ValidatorTestHelper.compileIliFile(DATA_DIRECTORY + "MissingContext24.ili");

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("ModelA.TestA", "b1"));
        validator.validate(new ObjectEvent(createTestClassIomObjectLv03("ModelA.TestA.ClassA")));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());

        assertEquals(1, logger.getErrs().size());
        assertEquals("The Context for DOMAIN ModelA.Coord is missing", logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void nonExistentDomainInBasket() {
        TransferDescription td = ValidatorTestHelper.compileIliFile(DATA_DIRECTORY + "GenericValueRanges24.ili");
        HashMap<String, String> genericDomains = new HashMap<String, String>();
        genericDomains.put("MultiCrs24.Coord", "notExistingDomain");

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("MultiCrs24.TestA", "b1", genericDomains));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());

        assertEquals(1, logger.getErrs().size());
        assertEquals("Concrete domain notExistingDomain of generic domain MultiCrs24.Coord not found", logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void invalidDomainInBasket() {
        TransferDescription td = ValidatorTestHelper.compileIliFile(DATA_DIRECTORY + "GenericValueRanges24.ili");
        HashMap<String, String> genericDomains = new HashMap<String, String>();
        genericDomains.put("MultiCrs24.Coord", "MultiCrs24.Coord_Other");

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("MultiCrs24.TestA", "b1", genericDomains));
        validator.validate(new ObjectEvent(createTestClassIomObjectLv03("MultiCrs24.TestA.ClassA")));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());

        assertEquals(1, logger.getErrs().size());
        assertEquals("The concrete DOMAIN MultiCrs24.Coord_Other is not valid for generic DOMAIN MultiCrs24.Coord", logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void domainInBasket() {
        TransferDescription td = ValidatorTestHelper.compileIliFile(DATA_DIRECTORY + "GenericValueRanges24.ili");
        HashMap<String, String> genericDomains = new HashMap<String, String>();
        genericDomains.put("MultiCrs24.Coord", "MultiCrs24.Coord_LV03");

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("MultiCrs24.TestA", "b1", genericDomains));
        validator.validate(new ObjectEvent(createTestClassIomObjectLv03("MultiCrs24.TestA.ClassA")));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void oneDomainInContext() {
        TransferDescription td = ValidatorTestHelper.compileIliFile(DATA_DIRECTORY + "GenericValueRangesMultipleModels24.ili");

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("ModelB.TestB", "b1"));
        validator.validate(new ObjectEvent(createTestClassIomObjectLv03("ModelB.TestB.ClassB")));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void deferredDomainMissingInTransfer() {
        TransferDescription td = ValidatorTestHelper.compileIliFile(DATA_DIRECTORY + "GenericValueRanges24.ili");

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("MultiCrs24.TestA", "b1"));
        validator.validate(new ObjectEvent(createTestClassIomObjectLv03("MultiCrs24.TestA.ClassA")));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());

        assertEquals(1, logger.getErrs().size());
        assertEquals("Could not choose between multiple concrete domains for generic DOMAIN MultiCrs24.Coord", logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void wrongObjectCoordinates() {
        TransferDescription td = ValidatorTestHelper.compileIliFile(DATA_DIRECTORY + "GenericValueRanges24.ili");
        HashMap<String, String> genericDomains = new HashMap<String, String>();
        genericDomains.put("MultiCrs24.Coord", "MultiCrs24.Coord_LV95");

        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent("MultiCrs24.TestA", "b1", genericDomains));
        validator.validate(new ObjectEvent(createTestClassIomObjectLv03("MultiCrs24.TestA.ClassA")));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());

        assertEquals(2, logger.getErrs().size());
        AssertContainsError("value 460002.000 is out of range in attribute attr1", 1, logger);
        AssertContainsError("value 45002.000 is out of range in attribute attr1", 1, logger);
    }
}
