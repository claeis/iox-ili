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
	private final static String OBJ_OID5 ="o5";
	private final static String OBJ_OID6 ="o6";
	private final static String OBJ_OID7 ="o7";
	// MODEL.TOPIC
	private final static String ILI_TOPIC="Association23.Topic";
	// CLASS
	private final static String ILI_CLASSA=ILI_TOPIC+".ClassA";
	private final static String ILI_CLASSB=ILI_TOPIC+".ClassB";
	private final static String ILI_CLASSC=ILI_TOPIC+".ClassC";
	private final static String ILI_CLASSD=ILI_TOPIC+".ClassD";
	private final static String ILI_CLASSG=ILI_TOPIC+".ClassG";
	private final static String ILI_CLASSH=ILI_TOPIC+".ClassH";
	// CLASS EXTEND
	private final static String ILI_CLASSAP=ILI_TOPIC+".ClassAp";
	private final static String ILI_CLASSBP=ILI_TOPIC+".ClassBp";
	private final static String ILI_CLASSCP=ILI_TOPIC+".ClassCp";
	// ASSOCIATION
	private final static String ILI_ASSOC_AB1_A1="a1";
	private final static String ILI_ASSOC_AB1_B1="b1";
	
	private final static String ILI_ASSOC_AB2_A2="a2";
	private final static String ILI_ASSOC_AB2_B2="b2";
	
	private final static String ILI_ASSOC_AB3_A3="a3";
	private final static String ILI_ASSOC_AB3_B3="b3";
	
	private final static String ILI_ASSOC_ABP1_AP1="ap1";
	private final static String ILI_ASSOC_ABP1_BP1="bp1";
	
	private final static String ILI_ASSOC_ABP2_AP2="ap2";
	private final static String ILI_ASSOC_ABP2_BP2="bp2";
	
	private final static String ILI_ASSOC_ABP3_BP3="bp3";
	private final static String ILI_ASSOC_ABP3_AP3="ap3";
	
	private final static String ILI_ASSOC_ABD1_AD1="ad1";
	private final static String ILI_ASSOC_ABD1_BD1="bd1";
	
	private final static String ILI_ASSOC_ABD2_AD2="ad2";
	private final static String ILI_ASSOC_ABD2_BD2="bd2";
	
	private final static String ILI_ASSOC_ABD3_AD3="ad3";
	private final static String ILI_ASSOC_ABD3_BD3="bd3";
	
	// ASSOCIATION CLASS
	private final static String ILI_ASSOC_AB2=ILI_TOPIC+".ab2";
	private final static String ILI_ASSOC_ABP2=ILI_TOPIC+".abp2";
	private final static String ILI_ASSOC_ABD2=ILI_TOPIC+".abd2";
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
	//########### SUCCESS EMBEDDED CARDINALITY ################//
	//#########################################################//
	//########### SUCCESS TARGET CLASS/ES #####################//
	//#########################################################//
	
	// ab1 --> a1 (+b1)
	@Test
	public void embedded_CLASSBassociatetoClassA_ab1_0to1_Ok(){
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
	
	// ab3 --> a3 (+b3)
	@Test
	public void embedded_CLASSBassociatetoClassA_ab3_0to1_Ok(){
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
	
	// abp1 --> ap1 (+bp1)
	@Test
	public void embedded_CLASSBPassociatetoClassAP_abp1_0to1_Ok(){
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
	
	// abp3 --> ap3 (+bp3)
	@Test
	public void embedded_CLASSBPassociatetoClassAP_abp3_0to1_Ok(){
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
	
	// abd1 --> ad1 (+bd1)
	@Test
	public void embedded_CLASSBassociatetoClassAorClassD_abd1_0to1_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSD, OBJ_OID3);
		iomObjB.addattrobj(ILI_ASSOC_ABD1_AD1, "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjB2=new Iom_jObject(ILI_CLASSB, OBJ_OID4);
		iomObjB2.addattrobj(ILI_ASSOC_ABD1_AD1, "REF").setobjectrefoid(OBJ_OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjB2));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// abd3 --> ad3 (+bd3)
	@Test
	public void embedded_CLASSBassociatetoClassAorClassD_abd3_0to1_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSD, OBJ_OID3);
		iomObjB.addattrobj(ILI_ASSOC_ABD3_AD3, "REF").setobjectrefoid(OBJ_OID1);
		iomObjB.addattrobj(ILI_ASSOC_ABD3_AD3, "REF").setobjectrefoid(OBJ_OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	//#########################################################//
	//########## SUCCESS STAND ALONE CARDINALITY ##############//
	//#########################################################//	
	
	// ab2 --> a2, b2
	@Test
	public void standAlone_ASSOCAB2_0toN_Ok(){
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
	
	// abp2 --> ap2, bp2
	@Test
	public void standAlone_ASSOCABP2_0toN_Ok(){	
		Iom_jObject iomObjAp=new Iom_jObject(ILI_CLASSAP, OBJ_OID1);
		Iom_jObject iomObjBp=new Iom_jObject(ILI_CLASSBP, OBJ_OID2);
		Iom_jObject iomObjABP=new Iom_jObject(ILI_ASSOC_ABP2, null);
		iomObjABP.addattrobj(ILI_ASSOC_ABP2_AP2, "REF").setobjectrefoid(OBJ_OID1);
		iomObjABP.addattrobj(ILI_ASSOC_ABP2_BP2, "REF").setobjectrefoid(OBJ_OID2);
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
	
	// abd2 --> ad2, bd2
	@Test
	public void standAlone_ASSOCABD2_0toN_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		Iom_jObject iomObjAB=new Iom_jObject(ILI_ASSOC_ABD2, null);
		iomObjAB.addattrobj(ILI_ASSOC_ABD2_AD2, "REF").setobjectrefoid(OBJ_OID1);
		iomObjAB.addattrobj(ILI_ASSOC_ABD2_BD2, "REF").setobjectrefoid(OBJ_OID2);
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
	
	
	//#########################################################//
	//############# FAIL EMBEDDED CARDINALITY #################//
	//#########################################################//		
	
	// ab1 --> a1
	@Test
	public void embedded_CLASSBassociatetoClassA_ab1_0to1_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB1=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB1.addattrobj(ILI_ASSOC_AB1_A1, "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjB2=new Iom_jObject(ILI_CLASSB, OBJ_OID3);
		iomObjB2.addattrobj(ILI_ASSOC_AB1_A1, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB1));
		validator.validate(new ObjectEvent(iomObjB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("b1 should associate 0 to 1 target objects (instead of 2)", logger.getErrs().get(0).getEventMsg());
	}
	
	// ab3 --> a3
	@Test
	public void embedded_CLASSBassociatetoClassA_ab3_0to1_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB1=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB1.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjB2=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB2.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB1));
		validator.validate(new ObjectEvent(iomObjB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("The OID o2 of object 'Association23.Topic.ClassB oid o2 {a3 -> o1 REF {}}' already exists in CLASS Association23.Topic.ClassB.", logger.getErrs().get(0).getEventMsg());
		assertEquals("a3 should associate 0 to 1 target objects (instead of 2)", logger.getErrs().get(1).getEventMsg());
	}
	
	// abp1 --> ap1
	@Test
	public void embedded_CLASSBPassociatetoClassAP_abp1_0to1_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSAP, OBJ_OID1);
		Iom_jObject iomObjB1=new Iom_jObject(ILI_CLASSBP, OBJ_OID2);
		iomObjB1.addattrobj(ILI_ASSOC_ABP1_AP1, "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjB2=new Iom_jObject(ILI_CLASSBP, OBJ_OID3);
		iomObjB2.addattrobj(ILI_ASSOC_ABP1_AP1, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB1));
		validator.validate(new ObjectEvent(iomObjB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("bp1 should associate 0 to 1 target objects (instead of 2)", logger.getErrs().get(0).getEventMsg());
	}
	
	// abp3 --> ap3
	@Test
	public void embedded_CLASSBPassociatetoClassAP_abp3_0to1_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSAP, OBJ_OID1);
		Iom_jObject iomObjB1=new Iom_jObject(ILI_CLASSBP, OBJ_OID2);
		iomObjB1.addattrobj(ILI_ASSOC_ABP3_AP3, "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjB2=new Iom_jObject(ILI_CLASSBP, OBJ_OID2);
		iomObjB2.addattrobj(ILI_ASSOC_ABP3_AP3, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB1));
		validator.validate(new ObjectEvent(iomObjB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("The OID o2 of object 'Association23.Topic.ClassBp oid o2 {ap3 -> o1 REF {}}' already exists in CLASS Association23.Topic.ClassBp.", logger.getErrs().get(0).getEventMsg());
		assertEquals("ap3 should associate 0 to 1 target objects (instead of 2)", logger.getErrs().get(1).getEventMsg());
	}
	
	// abd1 --> ad1
	@Test
	public void embedded_CLASSBassociatetoClassAorClassD_abd1_0to1_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSC, OBJ_OID6);
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSD, OBJ_OID4);
		Iom_jObject iomObjB1=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB1.addattrobj(ILI_ASSOC_ABD1_AD1, "REF").setobjectrefoid(OBJ_OID1);
		iomObjB1.addattrobj(ILI_ASSOC_ABD1_AD1, "REF").setobjectrefoid(OBJ_OID4);
		Iom_jObject iomObjB2=new Iom_jObject(ILI_CLASSB, OBJ_OID3);
		iomObjB2.addattrobj(ILI_ASSOC_ABD1_AD1, "REF").setobjectrefoid(OBJ_OID1);
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
		validator.validate(new ObjectEvent(iomObjC));
		validator.validate(new ObjectEvent(iomObjB1));
		validator.validate(new ObjectEvent(iomObjB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("bd1 should associate 0 to 1 target objects (instead of 2)", logger.getErrs().get(0).getEventMsg());
	}
	
	// abd3 --> ad3
	@Test
	public void embedded_CLASSBassociatetoClassAorClassD_abd3_0to1_Fail(){
		Iom_jObject iomObjA1=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		// 1. reference success (0..1 == 1)
		iomObjB.addattrobj(ILI_ASSOC_ABD3_AD3, "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjB2=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		// 2. reference fail (0..1 == 2)
		iomObjB2.addattrobj(ILI_ASSOC_ABD3_AD3, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA1));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("The OID o2 of object 'Association23.Topic.ClassB oid o2 {ad3 -> o1 REF {}}' already exists in CLASS Association23.Topic.ClassB.", logger.getErrs().get(0).getEventMsg());
		assertEquals("ad3 should associate 0 to 1 target objects (instead of 2)", logger.getErrs().get(1).getEventMsg());
	}
		
	//#########################################################//
	//################# FAIL TARGETCLASS TEST #################//
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
	
	@Test
	public void wrongTargetClass1toNCardinalityFail(){
		Iom_jObject iomObjF=new Iom_jObject("Association23.Topic.ClassF", OBJ_OID2);
		Iom_jObject iomObjAp=new Iom_jObject("Association23.Topic.zz1", null);
		iomObjAp.addattrobj("z9", "REF").setobjectrefoid(OBJ_OID1);
		iomObjAp.addattrobj("y9", "REF").setobjectrefoid(OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjF));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("z9 should associate 1 to * target objects (instead of 0)", logger.getErrs().get(0).getEventMsg());
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
		iomObjAp.addattrobj(ILI_ASSOC_ABP2_BP2, "REF").setobjectrefoid(OBJ_OID3);
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
	
	@Test
	public void oneGObjectAnd5HObjectsInCard1To1Fail(){
		Iom_jObject iomObjG=new Iom_jObject(ILI_CLASSG, OBJ_OID1);
		Iom_jObject iomObjH1=new Iom_jObject(ILI_CLASSH, OBJ_OID2);
		iomObjH1.addattrobj("g1", "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjH2=new Iom_jObject(ILI_CLASSH, OBJ_OID3);
		iomObjH2.addattrobj("g1", "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjH3=new Iom_jObject(ILI_CLASSH, OBJ_OID4);
		iomObjH3.addattrobj("g1", "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjH4=new Iom_jObject(ILI_CLASSH, OBJ_OID5);
		iomObjH4.addattrobj("g1", "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjH5=new Iom_jObject(ILI_CLASSH, OBJ_OID6);
		iomObjH5.addattrobj("g1", "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjG));
		validator.validate(new ObjectEvent(iomObjH1));
		validator.validate(new ObjectEvent(iomObjH2));
		validator.validate(new ObjectEvent(iomObjH3));
		validator.validate(new ObjectEvent(iomObjH4));
		validator.validate(new ObjectEvent(iomObjH5));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("h1 should associate 1 to 1 target objects (instead of 5)", logger.getErrs().get(0).getEventMsg());
	}
}