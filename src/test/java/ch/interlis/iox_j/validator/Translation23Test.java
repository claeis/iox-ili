package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.TypeAlias;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

public class Translation23Test {

    private final static String ILI_DE_MODEL="Translation23_de";
    private final static String ILI_FR_MODEL="Translation23_fr";
    private final static String ILI_DE_TOPIC_A=ILI_DE_MODEL+".TestA_de";
    private final static String ILI_FR_TOPIC_A=ILI_FR_MODEL+".TestA_fr";
    private final static String ILI_DE_CLASS_A1=ILI_DE_TOPIC_A+".ClassA1_de";
    private final static String ILI_FR_CLASS_A1=ILI_FR_TOPIC_A+".ClassA1_fr";
    private final static String ILI_DE_TOPIC_B=ILI_DE_MODEL+".TestB_de";
    private final static String ILI_FR_TOPIC_B=ILI_FR_MODEL+".TestB_fr";
    private final static String ILI_DE_STRUCT_B0=ILI_DE_TOPIC_B+".StructB0_de";
    private final static String ILI_FR_STRUCT_B0=ILI_FR_TOPIC_B+".StructB0_fr";
    private final static String ILI_DE_CLASS_B1=ILI_DE_TOPIC_B+".ClassB1_de";
    private final static String ILI_FR_CLASS_B1=ILI_FR_TOPIC_B+".ClassB1_fr";
    private final static String ILI_DE_ROLE_A="a_de";
    private final static String ILI_FR_ROLE_A="a_fr";
    private final static String ILI_DE_REF_A="refA_de";
    private final static String ILI_FR_REF_A="refA_fr";
    private final static String ILI_DE_ATTRREF="attrRef_de";
    private final static String ILI_FR_ATTRREF="attrRef_fr";
	private TransferDescription td=null;
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/Translation23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
    @Test
    public void assoc_root2root_Ok(){
        Iom_jObject iomObjA=new Iom_jObject(ILI_DE_CLASS_A1, "f72512fe-6813-4f00-9564-8682cdc8d5e6");
        Iom_jObject iomObjB=new Iom_jObject(ILI_DE_CLASS_B1, "82e60911-1779-4a4e-bcd4-ce3f3573f410");
        Iom_jObject iomRef=new Iom_jObject(Iom_jObject.REF, null);
        iomRef.setobjectrefoid(iomObjA.getobjectoid());
        iomObjB.addattrobj(ILI_DE_ROLE_A, iomRef);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_DE_TOPIC_A,"b1"));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent(ILI_DE_TOPIC_B,"b2"));
        validator.validate(new ObjectEvent(iomObjB));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==0);
    }
    @Test
    public void assoc_translated2translated_Ok(){
        Iom_jObject iomObjA=new Iom_jObject(ILI_FR_CLASS_A1, "f72512fe-6813-4f00-9564-8682cdc8d5e6");
        Iom_jObject iomObjB=new Iom_jObject(ILI_FR_CLASS_B1, "82e60911-1779-4a4e-bcd4-ce3f3573f410");
        Iom_jObject iomRef=new Iom_jObject(Iom_jObject.REF, null);
        iomRef.setobjectrefoid(iomObjA.getobjectoid());
        iomObjB.addattrobj(ILI_FR_ROLE_A, iomRef);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_FR_TOPIC_A,"b1"));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent(ILI_FR_TOPIC_B,"b2"));
        validator.validate(new ObjectEvent(iomObjB));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==0);
    }
    @Test
    public void assoc_translated2root_Ok(){
        Iom_jObject iomObjA=new Iom_jObject(ILI_DE_CLASS_A1, "f72512fe-6813-4f00-9564-8682cdc8d5e6");
        Iom_jObject iomObjB=new Iom_jObject(ILI_FR_CLASS_B1, "82e60911-1779-4a4e-bcd4-ce3f3573f410");
        Iom_jObject iomRef=new Iom_jObject(Iom_jObject.REF, null);
        iomRef.setobjectrefoid(iomObjA.getobjectoid());
        iomObjB.addattrobj(ILI_FR_ROLE_A, iomRef);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_DE_TOPIC_A,"b1"));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent(ILI_FR_TOPIC_B,"b2"));
        validator.validate(new ObjectEvent(iomObjB));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==0);
    }
    @Test
    public void assoc_root2translated_Ok(){
        Iom_jObject iomObjA=new Iom_jObject(ILI_FR_CLASS_A1, "f72512fe-6813-4f00-9564-8682cdc8d5e6");
        Iom_jObject iomObjB=new Iom_jObject(ILI_DE_CLASS_B1, "82e60911-1779-4a4e-bcd4-ce3f3573f410");
        Iom_jObject iomRef=new Iom_jObject(Iom_jObject.REF, null);
        iomRef.setobjectrefoid(iomObjA.getobjectoid());
        iomObjB.addattrobj(ILI_DE_ROLE_A, iomRef);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_DE_TOPIC_A,"b1"));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent(ILI_FR_TOPIC_B,"b2"));
        validator.validate(new ObjectEvent(iomObjB));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==0);
    }
	
	@Test
	public void refattr_root2root_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_DE_CLASS_A1, "f72512fe-6813-4f00-9564-8682cdc8d5e6");
        Iom_jObject iomObjB=new Iom_jObject(ILI_DE_CLASS_B1, "82e60911-1779-4a4e-bcd4-ce3f3573f410");
        Iom_jObject iomObjB0=new Iom_jObject(ILI_DE_STRUCT_B0, null);
        Iom_jObject iomRef=new Iom_jObject(Iom_jObject.REF, null);
        iomRef.setobjectrefoid(iomObjA.getobjectoid());
        iomObjB0.addattrobj(ILI_DE_REF_A, iomRef);
        iomObjB.addattrobj(ILI_DE_ATTRREF, iomObjB0);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_DE_TOPIC_A,"b1"));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent(ILI_DE_TOPIC_B,"b2"));
        validator.validate(new ObjectEvent(iomObjB));
        validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
    @Test
    public void refattr_translated2translated_Ok(){
        Iom_jObject iomObjA=new Iom_jObject(ILI_FR_CLASS_A1, "f72512fe-6813-4f00-9564-8682cdc8d5e6");
        Iom_jObject iomObjB=new Iom_jObject(ILI_FR_CLASS_B1, "82e60911-1779-4a4e-bcd4-ce3f3573f410");
        Iom_jObject iomObjB0=new Iom_jObject(ILI_FR_STRUCT_B0, null);
        Iom_jObject iomRef=new Iom_jObject(Iom_jObject.REF, null);
        iomRef.setobjectrefoid(iomObjA.getobjectoid());
        iomObjB0.addattrobj(ILI_FR_REF_A, iomRef);
        iomObjB.addattrobj(ILI_FR_ATTRREF, iomObjB0);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_FR_TOPIC_A,"b1"));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent(ILI_FR_TOPIC_B,"b2"));
        validator.validate(new ObjectEvent(iomObjB));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==0);
    }
    @Test
    public void refattr_translated2root_Ok(){
        Iom_jObject iomObjA=new Iom_jObject(ILI_DE_CLASS_A1, "f72512fe-6813-4f00-9564-8682cdc8d5e6");
        Iom_jObject iomObjB=new Iom_jObject(ILI_FR_CLASS_B1, "82e60911-1779-4a4e-bcd4-ce3f3573f410");
        Iom_jObject iomObjB0=new Iom_jObject(ILI_FR_STRUCT_B0, null);
        Iom_jObject iomRef=new Iom_jObject(Iom_jObject.REF, null);
        iomRef.setobjectrefoid(iomObjA.getobjectoid());
        iomObjB0.addattrobj(ILI_FR_REF_A, iomRef);
        iomObjB.addattrobj(ILI_FR_ATTRREF, iomObjB0);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_DE_TOPIC_A,"b1"));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent(ILI_FR_TOPIC_B,"b2"));
        validator.validate(new ObjectEvent(iomObjB));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==0);
    }
    @Test
    public void refattr_root2translated_Ok(){
        Iom_jObject iomObjA=new Iom_jObject(ILI_FR_CLASS_A1, "f72512fe-6813-4f00-9564-8682cdc8d5e6");
        Iom_jObject iomObjB=new Iom_jObject(ILI_DE_CLASS_B1, "82e60911-1779-4a4e-bcd4-ce3f3573f410");
        Iom_jObject iomObjB0=new Iom_jObject(ILI_DE_STRUCT_B0, null);
        Iom_jObject iomRef=new Iom_jObject(Iom_jObject.REF, null);
        iomRef.setobjectrefoid(iomObjA.getobjectoid());
        iomObjB0.addattrobj(ILI_DE_REF_A, iomRef);
        iomObjB.addattrobj(ILI_DE_ATTRREF, iomObjB0);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_DE_TOPIC_A,"b1"));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent(ILI_FR_TOPIC_B,"b2"));
        validator.validate(new ObjectEvent(iomObjB));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==0);
    }

}