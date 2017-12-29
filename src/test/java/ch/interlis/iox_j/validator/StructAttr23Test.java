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

public class StructAttr23Test {

	private TransferDescription td=null;
	private final static String TOPIC="StructAttr23.Topic";
	private final static String STRUCTA=TOPIC+".StructA";
	private static final String STRUCTA_ATTRA = "attrA";
	private final static String CLASSB=TOPIC+".ClassB";
	private static final String CLASSB_ATTRB2 = "attrB2";
	// BID
	private final static String BID = "b1";
	// OID
	private final static String OID = "o1";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/StructAttr23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	// Es wird ein Structur Attribute gestestet
	@Test
	public void structElement_Ok(){
		Iom_jObject iomStruct=new Iom_jObject(STRUCTA, null);
		iomStruct.setattrvalue(STRUCTA_ATTRA, "1.0");
		Iom_jObject iomObj=new Iom_jObject(CLASSB, OID);
		iomObj.addattrobj(CLASSB_ATTRB2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob ein Strukturattribut eine Struktur ist (und nicht ein Primitivwert)
	@Test
	public void structElement_Fail(){
		Iom_jObject iomObj=new Iom_jObject(CLASSB, OID);
		iomObj.setattrvalue(CLASSB_ATTRB2, "true");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
	}
	
	// Es wird getestet ob ein numerisches Attribute innerhalb einer Struktur geprueft wird
	@Test
	public void wrongAttrValueInStruct_Fail(){
		Iom_jObject iomStruct=new Iom_jObject(STRUCTA, null);
		iomStruct.setattrvalue(STRUCTA_ATTRA, "true");
		Iom_jObject iomObj=new Iom_jObject(CLASSB, OID);
		iomObj.addattrobj(CLASSB_ATTRB2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("value <true> is not a number",logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Objekt der Klasse B, ohne erstellung einer Struktur in der BAG des attribute attrB2, erstellt werden kann.
	@Test
	public void missingStructEle_Fail(){
		Iom_jObject iomObj=new Iom_jObject(CLASSB, OID);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Attribute attrB2 has wrong number of values",logger.getErrs().get(0).getEventMsg());
	}
}