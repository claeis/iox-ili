package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
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

public class RuntimeSystem23Test {
    private static final String ILI_TOPICA = "RuntimeSystem23.TopicA";
	private static final String CLASSA = ILI_TOPICA+".ClassA";
    private static final String CLASSA_ATTRA = "attrA";
    private static final String ILI_TOPICB = "RuntimeSystem23.TopicB";
    private static final String CLASSB = ILI_TOPICB+".ClassB";
    private static final String CLASSB_ATTRA = "attrA";
    private TransferDescription td=null;
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry("src/test/data/validator/MinimalRuntimeSystem01.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
		fileEntry=new FileEntry("src/test/data/validator/RuntimeSystem23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}

	@Test
	public void param(){
		Iom_jObject objClassA=new Iom_jObject(CLASSA, "o1");
		objClassA.setattrvalue(CLASSA_ATTRA, Integer.toString("2020-11-28T09:20:33.444".length())); 
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPICA,"b1"));
		validator.validate(new ObjectEvent(objClassA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
    @Test
    public void param_Fail(){
        Iom_jObject objClassA=new Iom_jObject(CLASSA, "o1");
        objClassA.setattrvalue(CLASSA_ATTRA, "1"); 
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPICA,"b1"));
        validator.validate(new ObjectEvent(objClassA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("Mandatory Constraint RuntimeSystem23.TopicA.ClassA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void param_getvalue(){
        Iom_jObject objClassA=new Iom_jObject(CLASSB, "o1");
        objClassA.setattrvalue(CLASSB_ATTRA, Integer.toString("2020-11-28T09:20:33.444".length())); 
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPICB,"b1"));
        validator.validate(new ObjectEvent(objClassA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==0);
    }
    @Test
    public void param_getvalue_Fail(){
        Iom_jObject objClassA=new Iom_jObject(CLASSB, "o1");
        objClassA.setattrvalue(CLASSB_ATTRA, "1"); 
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPICB,"b1"));
        validator.validate(new ObjectEvent(objClassA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("Mandatory Constraint RuntimeSystem23.TopicB.ClassB.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
    }
	
}