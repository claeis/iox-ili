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
import ch.interlis.iox_j.PipelinePool;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

public class Function24Test {
	private TransferDescription td=null;
	private final static String ILI_TOPIC="Function24.ElementCount";
	private final static String ILI_CLASSA=ILI_TOPIC+".ClassA";
    private final static String ILI_CLASSA_NAME="Name";
    @Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/Function24.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}

	@Test
	public void elementCount_3values_Ok(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSA, "o1");
		iomObj.addattrvalue(ILI_CLASSA_NAME, "a");
        iomObj.addattrvalue(ILI_CLASSA_NAME, "b");
        iomObj.addattrvalue(ILI_CLASSA_NAME, "c");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void elementCount_0values_Fail(){
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASSA, "o1");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("Mandatory Constraint Function24.ElementCount.ClassA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
    @Test
    public void elementCount_1values_Fail(){
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASSA, "o1");
        iomObj.addattrvalue(ILI_CLASSA_NAME, "a");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
        validator.validate(new ObjectEvent(iomObj));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("Mandatory Constraint Function24.ElementCount.ClassA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void elementCount_4values_Fail(){
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASSA, "o1");
        iomObj.addattrvalue(ILI_CLASSA_NAME, "a");
        iomObj.addattrvalue(ILI_CLASSA_NAME, "b");
        iomObj.addattrvalue(ILI_CLASSA_NAME, "c");
        iomObj.addattrvalue(ILI_CLASSA_NAME, "d");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
        validator.validate(new ObjectEvent(iomObj));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("Mandatory Constraint Function24.ElementCount.ClassA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
    }
	
}
