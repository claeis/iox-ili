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

public class ReferenceType23Test {
	
	private TransferDescription td=null;
	private final static String ILI_TOPIC="ReferenceType23.Topic";
	private final static String ILI_CLASSA=ILI_TOPIC+".ClassA";
	private final static String ILI_CLASSAP=ILI_TOPIC+".ClassAp";
	private final static String ILI_CLASSAQ=ILI_TOPIC+".ClassAq";
	private final static String ILI_STRUCTC=ILI_TOPIC+".StructC";
	private static final String ILI_STRUCTC_ATTRC2 = "attrC2";
	private static final String ILI_STRUCTC_ATTRC3 = "attrC3";
	private static final String ILI_STRUCTC_ATTRC4 = "attrC4";
	private final static String ILI_CLASSB=ILI_TOPIC+".ClassB";
	private final static String ILI_CLASSD=ILI_TOPIC+".ClassD";
	private static final String ILI_CLASSD_ATTRD2 = "attrD2";
	private final static String ILI_STRUCTE=ILI_TOPIC+".StructE";
	private final static String ILI_CLASSF=ILI_TOPIC+".ClassF";
	private static final String ILI_CLASSF_ATTRF2 = "attrF2";
	private final static String ILI_STRUCTG=ILI_TOPIC+".StructG";
	private static final String ILI_STRUCTG_ATTRG2 = "attrG2";
	private final static String ILI_CLASSH=ILI_TOPIC+".ClassH";
	private static final String ILI_CLASSH_ATTRH2 = "attrH2";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/ReferenceType23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	//############################################################
	//################ SUCCESS TESTS #############################
	//############################################################
	@Test
	public void referenceTypeOk(){
		String objTargetId="o1";
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC2, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, "o2");
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new ObjectEvent(iomObjtarget));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	@Test
	public void referenceTypeBasketExternalOk(){
		String objTargetId="o1";
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTG, null);
		iomStruct.addattrobj(ILI_STRUCTG_ATTRG2, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSH, "o2");
		iomObj.addattrobj(ILI_CLASSH_ATTRH2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObjtarget));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b2"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	@Test
	public void referenceTypeFileExternalOk(){
		String objTargetId="o1";
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTG, null);
		iomStruct.addattrobj(ILI_STRUCTG_ATTRG2, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSH, "o2");
		iomObj.addattrobj(ILI_CLASSH_ATTRH2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b2"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	@Test
	public void referenceTypeUndefinedOk(){
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, "o2");
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
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
	public void referenceExtendedTargetOk(){
		String objTargetId="o1";
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSAP, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC2, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, "o2");
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new ObjectEvent(iomObjtarget));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void referenceExtendedTypeOk(){
		String objTargetId="o1";
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSAP, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC3, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, "o2");
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new ObjectEvent(iomObjtarget));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void referenceRestrictedTypeOk(){
		String objTargetId="o1";
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSAP, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC4, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, "o2");
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new ObjectEvent(iomObjtarget));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	//############################################################
	//################ FAIL TESTS ################################
	//############################################################	
	@Test
	public void referenceTypeMandatoryUndefinedFaild(){
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTE, null);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSF, "o2");
		iomObj.addattrobj(ILI_CLASSF_ATTRF2, iomStruct);
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
		assertEquals("Attribute attrE2 requires a value", logger.getErrs().get(0).getEventMsg());
	}
	@Test
	public void referenceTypeDifferentBasketOk(){ //--> Fail
		String objTargetId="o1";
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC2, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, "o2");
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObjtarget));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b2"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void referenceTypeOidNotEqualFail(){
		String objTargetId="o1";
		String objTargetId2="o5";
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, objTargetId2);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC2, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, "o2");
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new ObjectEvent(iomObjtarget));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("attribute attrC2 references an inexistent object with OID o1.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void wrongTargetTypeFail(){
		String objTargetId="o1";
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSB, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC2, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, "o2");
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new ObjectEvent(iomObjtarget));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("object ReferenceType23.Topic.ClassB with OID o1 referenced by attrC2 is not an instance of ReferenceType23.Topic.ClassA.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void referenceExtendedTypeBaseTargetFail(){
		String objTargetId="o1";
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC3, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, "o2");
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new ObjectEvent(iomObjtarget));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("object ReferenceType23.Topic.ClassA with OID o1 referenced by attrC3 is not an instance of ReferenceType23.Topic.ClassAp.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void referenceRestrictedTypeBaseTargetFail(){
		String objTargetId="o1";
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC4, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, "o2");
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new ObjectEvent(iomObjtarget));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("object ReferenceType23.Topic.ClassA with OID o1 referenced by attrC4 is not an instance of ReferenceType23.Topic.ClassAp.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void referenceRestrictedTypeWrongExtensionFail(){
		String objTargetId="o1";
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSAQ, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC4, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, "o2");
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new ObjectEvent(iomObjtarget));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("object ReferenceType23.Topic.ClassAq with OID o1 referenced by attrC4 is not an instance of ReferenceType23.Topic.ClassAp.", logger.getErrs().get(0).getEventMsg());
	}
}
