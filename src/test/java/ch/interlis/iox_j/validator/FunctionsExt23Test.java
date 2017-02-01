package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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

public class FunctionsExt23Test {
	private TransferDescription td=null;
	// OID
	private final static String OBJ_OID1 ="o1";
	// MODEL
	private final static String ILI_TOPIC="FunctionsExt23.Topic";
	// CLASS
	private final static String ILI_CLASSA=ILI_TOPIC+".ClassA";
	private final static String ILI_CLASSB=ILI_TOPIC+".ClassB";
	private final static String ILI_CLASSC=ILI_TOPIC+".ClassC";
	// START BASKET EVENT
	private final static String BID1="b1";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/FunctionsExt23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}

	//#########################################################//
	//######## SUCCESS FUNCTIONS ##############################//
	//#########################################################//	
	@Test
	public void subString_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		iomObjA.setattrvalue("text", "0123456789");
		iomObjA.setattrvalue("from", "2");
		iomObjA.setattrvalue("to", "8");
		iomObjA.setattrvalue("attr2", "234567");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.mergeIliMetaAttrs(td);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	//#########################################################//
	//######## FAILING FUNCTIONS ##############################//
	//#########################################################//	
	@Test
	public void subString_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		iomObjA.setattrvalue("text", "123456789");
		iomObjA.setattrvalue("from", "2");
		iomObjA.setattrvalue("to", "8");
		iomObjA.setattrvalue("attr2", "234567");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.mergeIliMetaAttrs(td);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void subString_IliValidClassNotExists_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
		iomObjA.setattrvalue("text", "0123456789");
		iomObjA.setattrvalue("from", "2");
		iomObjA.setattrvalue("to", "8");
		iomObjA.setattrvalue("attr2", "234567");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.mergeIliMetaAttrs(td);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Function is not yet implemented.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void subString_IliValidClassNotDefined_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSC, OBJ_OID1);
		iomObjA.setattrvalue("text", "0123456789");
		iomObjA.setattrvalue("from", "2");
		iomObjA.setattrvalue("to", "8");
		iomObjA.setattrvalue("attr2", "234567");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.mergeIliMetaAttrs(td);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Function is not yet implemented.", logger.getErrs().get(0).getEventMsg());
	}
}
