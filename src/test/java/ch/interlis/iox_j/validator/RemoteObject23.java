package ch.interlis.iox_j.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.gui.UserSettings;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ilirepository.IliManager;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

public class RemoteObject23 {

    // OID
    private static final String OID1 = "o1";
    private static final String OID2 = "o2";
    private static final String OID3 = "o3";

    // TOPIC
    private static final String TOPIC = "RemoteObject23.Topic";

    // CLASS
    private static final String ILI_CLASSB = TOPIC + ".ClassB";
    private static final String ILI_CLASSD = TOPIC + ".ClassD";
    private static final String ILI_CLASSE = TOPIC + ".ClassE";

    // STRUCTURE
    private static final String STRUCTURE_C = TOPIC + ".StructC";
    private static final String STRUCTURE_E = TOPIC + ".StructE";

    // ASSOCIATION
    private static final String ILI_ASSOC_AB1_A1 = "a1";
    private static final String ILI_ASSOC_NOT_EMBEDDED = TOPIC + ".notEmbedded";

    // TD
    private TransferDescription td = null;

    // START EVENT BASKET
    private static final String BID1 = "bid1";

    // XTF Path
    private static final String TEST_REPOS1 = "src/test/data/validator/RemoteObject23/repos1";

    @Before
    public void setUp() throws Exception {
        // ili-datei lesen
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry("src/test/data/validator/RemoteObject23.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        td = ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        assertNotNull(td);
    }

    // ############################################################/
    // ########## SUCCESSFUL TESTS ################################/
    // ############################################################/

    @Test
    public void catalogReference_remoteObject_OK() {
        Iom_jObject iomObjE = new Iom_jObject(ILI_CLASSE, OID2);
        IomObject iomStruct = iomObjE.addattrobj("attrE", STRUCTURE_E);
        iomStruct.addattrobj("attrE", "REF").setobjectrefoid(OID1);

        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE,
                ValidationConfig.TRUE);
        modelConfig.mergeIliMetaAttrs(td);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();

        IliManager reposManager = new IliManager();
        reposManager.setRepositories(new String[] { TEST_REPOS1 });
        settings.setTransientObject(UserSettings.CUSTOM_ILI_MANAGER, reposManager);
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());

        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjE));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void referenceAttribute_remoteObject_OK() {
        Iom_jObject iomObjD = new Iom_jObject(ILI_CLASSD, OID2);
        IomObject iomStruct = iomObjD.addattrobj("attrD", STRUCTURE_C);
        iomStruct.addattrobj("attrC", "REF").setobjectrefoid(OID1);

        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE,
                ValidationConfig.TRUE);
        modelConfig.mergeIliMetaAttrs(td);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();

        IliManager reposManager = new IliManager();
        reposManager.setRepositories(new String[] { TEST_REPOS1 });
        settings.setTransientObject(UserSettings.CUSTOM_ILI_MANAGER, reposManager);
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());

        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjD));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void linkObjAsso_remoteObject_OK() {
        Iom_jObject iomObjB = new Iom_jObject(ILI_CLASSB, OID2);

        Iom_jObject iomObjNotEmbedded = new Iom_jObject(ILI_ASSOC_NOT_EMBEDDED, null);
        iomObjNotEmbedded.addattrobj("a2", "REF").setobjectrefoid(OID1);
        iomObjNotEmbedded.addattrobj("b2", "REF").setobjectrefoid(iomObjB.getobjectoid());

        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, ValidationConfig.TRUE);
        modelConfig.mergeIliMetaAttrs(td);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();

        IliManager reposManager = new IliManager();
        reposManager.setRepositories(new String[] { TEST_REPOS1 });
        settings.setTransientObject(UserSettings.CUSTOM_ILI_MANAGER, reposManager);
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());

        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjB));
        validator.validate(new ObjectEvent(iomObjNotEmbedded));
        validator.validate(new EndBasketEvent());

        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void embeddedAsso_remoteObject_OK() {
        Iom_jObject iomObjB = new Iom_jObject(ILI_CLASSB, OID2);
        iomObjB.addattrobj(ILI_ASSOC_AB1_A1, "REF").setobjectrefoid(OID1);
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, ValidationConfig.TRUE);
        modelConfig.mergeIliMetaAttrs(td);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();

        IliManager reposManager = new IliManager();
        reposManager.setRepositories(new String[] { TEST_REPOS1 });
        settings.setTransientObject(UserSettings.CUSTOM_ILI_MANAGER, reposManager);
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());

        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjB));
        validator.validate(new EndBasketEvent());

        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }

    // ############################################################/
    // ########## FAILING TESTS ###################################/
    // ############################################################/

    @Test
    public void catalogReference_remoteObject_Fail() {
        Iom_jObject iomObjE = new Iom_jObject(ILI_CLASSE, OID2);
        IomObject iomStruct = iomObjE.addattrobj("attrE", STRUCTURE_E);
        iomStruct.addattrobj("attrE", "REF").setobjectrefoid(OID3);

        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, ValidationConfig.TRUE);
        modelConfig.mergeIliMetaAttrs(td);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();

        IliManager reposManager = new IliManager();
        reposManager.setRepositories(new String[] { TEST_REPOS1 });
        settings.setTransientObject(UserSettings.CUSTOM_ILI_MANAGER, reposManager);
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());

        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjE));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("No object found with OID o3.", logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void embeddedAsso_remoteObject_Fail() {
        Iom_jObject iomObjB = new Iom_jObject(ILI_CLASSB, OID2);
        iomObjB.addattrobj(ILI_ASSOC_AB1_A1, "REF").setobjectrefoid(OID3);
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, ValidationConfig.TRUE);
        modelConfig.mergeIliMetaAttrs(td);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();

        IliManager reposManager = new IliManager();
        reposManager.setRepositories(new String[] { TEST_REPOS1 });
        settings.setTransientObject(UserSettings.CUSTOM_ILI_MANAGER, reposManager);
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());

        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjB));
        validator.validate(new EndBasketEvent());

        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("No object found with OID o3.", logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void linkObjAsso_remoteObject_Fail() {
        Iom_jObject iomObjB = new Iom_jObject(ILI_CLASSB, OID2);

        Iom_jObject iomObjNotEmbedded = new Iom_jObject(ILI_ASSOC_NOT_EMBEDDED, null);
        iomObjNotEmbedded.addattrobj("a2", "REF").setobjectrefoid(OID3);
        iomObjNotEmbedded.addattrobj("b2", "REF").setobjectrefoid(iomObjB.getobjectoid());

        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, ValidationConfig.TRUE);
        modelConfig.mergeIliMetaAttrs(td);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();

        IliManager reposManager = new IliManager();
        reposManager.setRepositories(new String[] { TEST_REPOS1 });
        settings.setTransientObject(UserSettings.CUSTOM_ILI_MANAGER, reposManager);
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());

        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjB));
        validator.validate(new ObjectEvent(iomObjNotEmbedded));
        validator.validate(new EndBasketEvent());

        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("No object found with OID o3.", logger.getErrs().get(0).getEventMsg());
    }

    @Test
    public void referenceAttribute_remoteObject_Fail() {
        Iom_jObject iomObjD = new Iom_jObject(ILI_CLASSD, OID2);
        IomObject iomStruct = iomObjD.addattrobj("attrD", STRUCTURE_C);
        iomStruct.addattrobj("attrC", "REF").setobjectrefoid(OID3);

        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, ValidationConfig.TRUE);
        modelConfig.mergeIliMetaAttrs(td);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();

        IliManager reposManager = new IliManager();
        reposManager.setRepositories(new String[] { TEST_REPOS1 });
        settings.setTransientObject(UserSettings.CUSTOM_ILI_MANAGER, reposManager);
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());

        validator.validate(new StartBasketEvent(TOPIC, BID1));
        validator.validate(new ObjectEvent(iomObjD));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("No object found with OID o3.", logger.getErrs().get(0).getEventMsg());
    }
}
