package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.IoxLogging;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

public class StructAttr23Test {

	private TransferDescription td=null;
	private final static String ILI_TOPIC="StructAttr23.Topic";
	private final static String ILI_STRUCTA=ILI_TOPIC+".StructA";
	private static final String ILI_STRUCTA_ATTRA = "attrA";
	private final static String ILI_CLASSB=ILI_TOPIC+".ClassB";
	private static final String ILI_CLASSB_ATTRB1 = "attrB1";
	private static final String ILI_CLASSB_ATTRB2 = "attrB2";
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/StructAttr23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	@Test
	public void structEleOk(){
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTA, null);
		iomStruct.setattrvalue(ILI_STRUCTA_ATTRA, "1.0");
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSB, "o1");
		iomObj.addattrobj(ILI_CLASSB_ATTRB2, iomStruct);
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
	public void wrongAttrValueInStructFail(){
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTA, null);
		iomStruct.setattrvalue(ILI_STRUCTA_ATTRA, "true");
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSB, "o1");
		iomObj.addattrobj(ILI_CLASSB_ATTRB2, iomStruct);
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
		assertEquals("value <true> is not a number",logger.getErrs().get(0).getEventMsg());
	}
	@Test
	public void missingStructEleFail(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSB, "o1");
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
		assertEquals("Attribute attrB2 has wrong number of values",logger.getErrs().get(0).getEventMsg());
	}
	
}