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
    private final static String STRUCTAP="StructAttr23.StructAp";
    private final static String MODELB_TOPICB_STRUCTB="ModelB.TopicB.StructB";
	private static final String STRUCTA_ATTRA = "attrA";
	private final static String CLASSB=TOPIC+".ClassB";
	private static final String CLASSB_ATTRB2 = "attrB2";
    private final static String MODELB_TOPICB_CLASSB="ModelB.TopicB.ClassB";
    private final static String MODELB_TOPICB="ModelB.TopicB";
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
	
	// ein normales, gueltiges Strukturattribut
	@Test
	public void structEle_Ok(){
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
    @Test
    public void structEleSubTypeFromSameModel_Ok(){
        Iom_jObject iomStruct=new Iom_jObject(STRUCTAP, null);
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
    @Test
    public void structEleSubTypeFromDifferentModel_Fail(){
        Iom_jObject iomStruct=new Iom_jObject(MODELB_TOPICB_STRUCTB, null);
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
        assertTrue(logger.getErrs().size()==1);
        assertEquals("Attribute attrB2 requires a structure StructAttr23.Topic.StructA",logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void structEleSubTypeFromDifferentModel_Ok(){
        Iom_jObject iomStruct=new Iom_jObject(MODELB_TOPICB_STRUCTB, null);
        iomStruct.setattrvalue(STRUCTA_ATTRA, "1.0");
        Iom_jObject iomObj=new Iom_jObject(MODELB_TOPICB_CLASSB, OID);
        iomObj.addattrobj(CLASSB_ATTRB2, iomStruct);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(MODELB_TOPICB,BID));
        validator.validate(new ObjectEvent(iomObj));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==0);
    }
	
	// ein Primitivwert, statt ein Strukturelement
	@Test
	public void primitiveValueAsStructEle_Fail(){
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
	
	// Es wird getestet, ob ein Attribute innerhalb einer Struktur geprueft wird
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
	
	// Kardinalitaet bei BAG OF
	@Test
	public void structEleCardinality_Fail(){
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