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

public class Association23Test {

	private TransferDescription td=null;
	// OID
	private final static String OBJ_OID1 ="o1";
	private final static String OBJ_OID2 ="o2";
	private final static String OBJ_OID3 ="o3";
	private final static String OBJ_OID4 ="o4";
	// MODEL.TOPIC
	private final static String ILI_TOPIC="Association23.Topic";
	// CLASS
	private final static String ILI_CLASSA=ILI_TOPIC+".ClassA";
	private final static String ILI_CLASSB=ILI_TOPIC+".ClassB";
	private final static String ILI_CLASSD=ILI_TOPIC+".ClassD";
	// CLASS EXTEND
	private final static String ILI_CLASSAP=ILI_TOPIC+".ClassAp";
	private final static String ILI_CLASSBP=ILI_TOPIC+".ClassBp";
	private final static String ILI_CLASSCP=ILI_TOPIC+".ClassCp";
	// ASSOCIATION
	private final static String ILI_ASSOC_AB1_A1="a1";
	private final static String ILI_ASSOC_AB2_A2="a2";
	private final static String ILI_ASSOC_AB2_B2="b2";
	private final static String ILI_ASSOC_AB3_A3="a3";
	private final static String ILI_ASSOC_ABP1_AP1="ap1";
	private final static String ILI_ASSOC_ABP2_AP2="ap2";
	private final static String ILI_ASSOC_ABP3_BP2="bp2";
	private final static String ILI_ASSOC_ABP3_AP3="ap3";
	private final static String ILI_ASSOC_ABD1_AD1="ad1";
	// ASSOCIATION CLASS
	private final static String ILI_ASSOC_AB2=ILI_TOPIC+".ab2";
	private final static String ILI_ASSOC_ABP2=ILI_TOPIC+".abp2";
	// START BASKET EVENT
	private final static String START_BASKET_EVENT="b1";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/Association23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	//#########################################################//
	//######################### SUCCESS #######################//
	//#########################################################//
	// classB with OID b1, association to classA with OID a1
	@Test
	public void classBAssociateClassA1to1Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_AB1_A1, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	// classB with OID b1, association to classA with OID a1
	@Test
	public void classBAssociateClassA1toNOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	// classB with OID b1, association to classA with OID a1 with a RESTRICTION
	@Test
	public void classBpAssociateClassAp1to1Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSAP, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSBP, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_ABP1_AP1, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	// classB with OID b1, association to classA with OID a1 with a RESTRICTION
	@Test
	public void classBpAssociateClassAp1toNOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSAP, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSBP, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_ABP3_AP3, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	// classB with OID b1, association to [classA with OID a1] OR [classD with OID d1]
	@Test
	public void classBAssociateClassAORClassD1to1Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSD, OBJ_OID4);
		Iom_jObject iomObjB1=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB1.addattrobj(ILI_ASSOC_ABD1_AD1, "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjB2=new Iom_jObject(ILI_CLASSB, OBJ_OID3);
		iomObjB2.addattrobj(ILI_ASSOC_ABD1_AD1, "REF").setobjectrefoid(OBJ_OID4);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjB1));
		validator.validate(new ObjectEvent(iomObjB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	//#########################################################//
	//############## N TO N ASSOCIATION SUCCESS ###############//
	//#########################################################//
	@Test
	public void assoClassAb2AssociateClassAandClassBNtoNOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		Iom_jObject iomObjAB=new Iom_jObject(ILI_ASSOC_AB2, null);
		iomObjAB.addattrobj(ILI_ASSOC_AB2_A2, "REF").setobjectrefoid(OBJ_OID1);
		iomObjAB.addattrobj(ILI_ASSOC_AB2_B2, "REF").setobjectrefoid(OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjAB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	@Test
	public void assoClassBpAssociateClassApNtoNOk(){
		Iom_jObject iomObjAp=new Iom_jObject(ILI_CLASSAP, OBJ_OID1);
		Iom_jObject iomObjBp=new Iom_jObject(ILI_CLASSBP, OBJ_OID2);
		Iom_jObject iomObjABP=new Iom_jObject(ILI_ASSOC_ABP2, null);
		iomObjABP.addattrobj(ILI_ASSOC_ABP2_AP2, "REF").setobjectrefoid(OBJ_OID1);
		iomObjABP.addattrobj(ILI_ASSOC_ABP3_BP2, "REF").setobjectrefoid(OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjAp));
		validator.validate(new ObjectEvent(iomObjBp));
		validator.validate(new ObjectEvent(iomObjABP));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	//#########################################################//
	//######################### FAIL ##########################//
	//#########################################################//	
	// classB with OID b1, association to classA with oid d1
	// classA with OID d1, associated by classB with OID b1, does not exist
	@Test
	public void wrongTargetClass1to1Fail(){
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSD, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_AB1_A1, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Object Association23.Topic.ClassD with OID o1 must be of Association23.Topic.ClassA", logger.getErrs().get(0).getEventMsg());
	}
	// classB with OID b1 associate to classA with OID a1
	// OID a1 of classA, associated by classB with OID b1, does not exist
	@Test
	public void noTargetObject1toNFail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("No object found with OID o3.", logger.getErrs().get(0).getEventMsg());
	}
	// classBp extends to classB with OID b1, associate to classA with OID a1, with a restriction to classAp, which extends classA
	// classA with OID a1, associated by classBp with OID b1, does not exist
	@Test
	public void wrongTargetClassAOfExtendedClasses1to1Fail(){
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSCP, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSBP, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_ABP1_AP1, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjC));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Object Association23.Topic.ClassCp with OID o1 must be of Association23.Topic.ClassA", logger.getErrs().get(0).getEventMsg());
	}
	// classBp extends to classB with OID b1, associate to classA with OID a1, with a restriction to classAp, which extends classA
	// OID z1 of classA, associated by classB with OID b1, does not exist
	@Test
	public void noTargetObjectOfExtendedClasses1toNFail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSAP, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSBP, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_ABP3_AP3, "REF").setobjectrefoid(OBJ_OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("No object found with OID o3.", logger.getErrs().get(0).getEventMsg());
	}
	// classBp extends to classB with OID b1, associate to classA with OID a1, with a restriction to classAp, which extends classA
	// OID z1 of classA, associated by classB with OID b1, does not exist
	@Test
	public void noTargetObjectOfExtendedClassesNtoNFail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSAP, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSBP, OBJ_OID2);
		Iom_jObject iomObjAp=new Iom_jObject(ILI_ASSOC_ABP2, null);
		iomObjAp.addattrobj(ILI_ASSOC_ABP2_AP2, "REF").setobjectrefoid(OBJ_OID1);
		iomObjAp.addattrobj(ILI_ASSOC_ABP3_BP2, "REF").setobjectrefoid(OBJ_OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjAp));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("No object found with OID o3.", logger.getErrs().get(0).getEventMsg());
	}
}