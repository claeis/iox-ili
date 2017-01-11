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
	private final static String OBJ_OID8 ="o8";
	// MODEL.TOPIC
	private final static String ILI_TOPIC="Association23.Topic";
	private final static String ILI_TOPICB="Association23.TopicB";
	// CLASS
	private final static String ILI_CLASSA=ILI_TOPIC+".ClassA";
	private final static String ILI_CLASSB=ILI_TOPIC+".ClassB";
	private final static String ILI_CLASSC=ILI_TOPIC+".ClassC";
	private final static String ILI_CLASSD=ILI_TOPIC+".ClassD";
	private final static String ILI_CLASSE=ILI_TOPIC+".ClassE";
	private final static String ILI_CLASSF=ILI_TOPIC+".ClassF";
	private final static String ILI_CLASSG=ILI_TOPIC+".ClassG";
	private final static String ILI_CLASSH=ILI_TOPIC+".ClassH";
	private final static String ILI_CLASSI=ILI_TOPIC+".ClassI";
	private final static String ILI_CLASSJ=ILI_TOPIC+".ClassJ";
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
	
	private final static String ILI_ASSOC_AB4_A4="a4";
	private final static String ILI_ASSOC_AB4_B4="b4";
	
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
	
	private final static String ILI_ASSOC_EF1_E1="e1";
	private final static String ILI_ASSOC_EF1_F1="f1";
	
	// ASSOCIATION CLASS
	private final static String ILI_ASSOC_AB2=ILI_TOPIC+".ab2";
	private final static String ILI_ASSOC_ABP2=ILI_TOPIC+".abp2";
	private final static String ILI_ASSOC_ABD2=ILI_TOPIC+".abd2";
	private final static String ILI_ASSOC_EF1=ILI_TOPIC+".ef1";
	
	private final static String ILI_TOPICB_CLASSE=ILI_TOPICB+".ClassE";
	private final static String ILI_TOPICB_CLASSF=ILI_TOPICB+".ClassF";
	private final static String ILI_TOPICB_CLASSG=ILI_TOPICB+".ClassG";
	private final static String ILI_TOPICB_CLASSH=ILI_TOPICB+".ClassH";
	private final static String ILI_TOPICB_ASSOC_EF1=ILI_TOPICB+".ef1";
	private final static String ILI_TOPICB_ASSOC_EF1_E1="e1";
	private final static String ILI_TOPICB_ASSOC_EF1_F1="f1";
	
	// START BASKET EVENT
	private final static String BASKET_ID1="b1";
	private final static String BASKET_ID2="b2";
	
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void oneBasketExternalGObjectAnd5HObjectsInCard1To1Ok(){
		Iom_jObject iomObjG=new Iom_jObject(ILI_TOPICB_CLASSG, OBJ_OID1);
		Iom_jObject iomObjH1=new Iom_jObject(ILI_TOPICB_CLASSH, OBJ_OID2);
		iomObjH1.addattrobj("g1", "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPICB,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjG));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent(ILI_TOPICB,BASKET_ID2));
		validator.validate(new ObjectEvent(iomObjH1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	@Test
	public void oneFileExternalGObjectAndHObjectsInCard1To1Ok(){
		Iom_jObject iomObjG1=new Iom_jObject(ILI_TOPICB_CLASSG, OBJ_OID1);
		Iom_jObject iomObjH1=new Iom_jObject(ILI_TOPICB_CLASSH, OBJ_OID2);
		iomObjH1.addattrobj("g1", "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPICB,BASKET_ID2));
		validator.validate(new ObjectEvent(iomObjG1));
		validator.validate(new ObjectEvent(iomObjH1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	//#########################################################//
	//########## SUCCESS STAND ALONE CARDINALITY ##############//
	//#########################################################//	
	
	@Test
	public void standAlone_ASSOC_NtoNexternal_Ok(){
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSE, OBJ_OID1);
		Iom_jObject iomObjF=new Iom_jObject(ILI_CLASSF, OBJ_OID2);
		Iom_jObject iomLinkEF=new Iom_jObject(ILI_ASSOC_EF1, null);
		iomLinkEF.addattrobj(ILI_ASSOC_EF1_E1, "REF").setobjectrefoid(OBJ_OID1);
		iomLinkEF.addattrobj(ILI_ASSOC_EF1_F1, "REF").setobjectrefoid(OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID2));
		validator.validate(new ObjectEvent(iomObjF));
		validator.validate(new ObjectEvent(iomLinkEF));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjAB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	@Test
	public void standAlone_ASSOC_NtoN_Ok(){
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSE, OBJ_OID1);
		Iom_jObject iomObjF=new Iom_jObject(ILI_CLASSF, OBJ_OID2);
		Iom_jObject iomLinkEF=new Iom_jObject(ILI_ASSOC_EF1, null);
		iomLinkEF.addattrobj(ILI_ASSOC_EF1_E1, "REF").setobjectrefoid(OBJ_OID1);
		iomLinkEF.addattrobj(ILI_ASSOC_EF1_F1, "REF").setobjectrefoid(OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new ObjectEvent(iomObjF));
		validator.validate(new ObjectEvent(iomLinkEF));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	@Test
	public void standAlone_ExternalASSOC_NtoN_Ok(){
		Iom_jObject iomObjE=new Iom_jObject(ILI_TOPICB_CLASSE, OBJ_OID1);
		Iom_jObject iomObjF=new Iom_jObject(ILI_TOPICB_CLASSF, OBJ_OID2);
		Iom_jObject iomLinkEF=new Iom_jObject(ILI_TOPICB_ASSOC_EF1, null);
		iomLinkEF.addattrobj(ILI_TOPICB_ASSOC_EF1_E1, "REF").setobjectrefoid(OBJ_OID1);
		iomLinkEF.addattrobj(ILI_TOPICB_ASSOC_EF1_F1, "REF").setobjectrefoid(OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPICB,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new ObjectEvent(iomObjF));
		validator.validate(new ObjectEvent(iomLinkEF));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	@Test
	public void oneExternalGObjectAndHObjectsInCard1To1Ok(){
		Iom_jObject iomObjG=new Iom_jObject(ILI_CLASSG, OBJ_OID1);
		Iom_jObject iomObjH1=new Iom_jObject(ILI_CLASSH, OBJ_OID2);
		iomObjH1.addattrobj("g1", "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjG));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID2));
		validator.validate(new ObjectEvent(iomObjH1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	@Test
	public void standAlone_BasketExternalASSOC_NtoN_Ok(){
		Iom_jObject iomObjE=new Iom_jObject(ILI_TOPICB_CLASSE, OBJ_OID1);
		Iom_jObject iomObjF=new Iom_jObject(ILI_TOPICB_CLASSF, OBJ_OID2);
		Iom_jObject iomLinkEF=new Iom_jObject(ILI_TOPICB_ASSOC_EF1, null);
		iomLinkEF.addattrobj(ILI_TOPICB_ASSOC_EF1_E1, "REF").setobjectrefoid(OBJ_OID1);
		iomLinkEF.addattrobj(ILI_TOPICB_ASSOC_EF1_F1, "REF").setobjectrefoid(OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPICB,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent(ILI_TOPICB,BASKET_ID2));
		validator.validate(new ObjectEvent(iomObjF));
		validator.validate(new ObjectEvent(iomLinkEF));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	@Test
	public void standAlone_FileExternalASSOC_NtoN_Ok(){
		Iom_jObject iomObjF=new Iom_jObject(ILI_TOPICB_CLASSF, OBJ_OID2);
		Iom_jObject iomLinkEF=new Iom_jObject(ILI_TOPICB_ASSOC_EF1, null);
		iomLinkEF.addattrobj(ILI_TOPICB_ASSOC_EF1_E1, "REF").setobjectrefoid(OBJ_OID1);
		iomLinkEF.addattrobj(ILI_TOPICB_ASSOC_EF1_F1, "REF").setobjectrefoid(OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPICB,BASKET_ID2));
		validator.validate(new ObjectEvent(iomObjF));
		validator.validate(new ObjectEvent(iomLinkEF));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Object Association23.Topic.ClassD with OID o1 must be of Association23.Topic.ClassA", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void standAlone1toNCardinalityFail(){
		Iom_jObject iomObjF=new Iom_jObject(ILI_CLASSF, OBJ_OID2);
		Iom_jObject iomObjAp=new Iom_jObject(ILI_ASSOC_EF1, null);
		iomObjAp.addattrobj(ILI_ASSOC_EF1_F1, "REF").setobjectrefoid(OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjF));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("e1 should associate 1 to * target objects (instead of 0)", logger.getErrs().get(0).getEventMsg());
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
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
	
	//#########################################################//
	//################# CONFIG ON/OFF TEST ####################//
	//#########################################################//
	
	@Test
	public void configMultiplicityON_AssociationRefFail(){
		Iom_jObject iomObjI=new Iom_jObject(ILI_CLASSI, OBJ_OID1);
		Iom_jObject iomObjJ=new Iom_jObject(ILI_CLASSJ, OBJ_OID2);
		iomObjJ.addattrobj(ILI_ASSOC_AB4_A4, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjI));
		validator.validate(new ObjectEvent(iomObjJ));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("unknown property <a4>", logger.getErrs().get(0).getEventMsg());
		assertEquals("a4 should associate 1 to 5 target objects (instead of 0)", logger.getErrs().get(1).getEventMsg());
	}
	
	@Test
	public void configTargetON_NoTargetObject1toNFail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("No object found with OID o3.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void configTargetON_WrongTargetClass1to1Fail(){
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSD, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_AB1_A1, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Object Association23.Topic.ClassD with OID o1 must be of Association23.Topic.ClassA", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void configTargetAndMultiplicityON_Embedded_CLASSBassociatetoClassA_ab3_0to1_Fail(){
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSD, OBJ_OID1);
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID2);
		Iom_jObject iomObjB1=new Iom_jObject(ILI_CLASSB, OBJ_OID3);
		iomObjB1.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID2);
		Iom_jObject iomObjB2=new Iom_jObject(ILI_CLASSB, OBJ_OID3);
		iomObjB2.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID2);
		Iom_jObject iomObjB3=new Iom_jObject(ILI_CLASSB, OBJ_OID5);
		iomObjB3.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID6);
		Iom_jObject iomObjB4=new Iom_jObject(ILI_CLASSB, OBJ_OID7);
		iomObjB4.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB1));
		validator.validate(new ObjectEvent(iomObjB2));
		validator.validate(new ObjectEvent(iomObjB3));
		validator.validate(new ObjectEvent(iomObjB4));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==4);
		assertEquals("The OID o3 of object 'Association23.Topic.ClassB oid o3 {a3 -> o2 REF {}}' already exists in CLASS Association23.Topic.ClassB.", logger.getErrs().get(0).getEventMsg());
		assertEquals("Object Association23.Topic.ClassD with OID o1 must be of Association23.Topic.ClassA", logger.getErrs().get(1).getEventMsg());
		assertEquals("No object found with OID o6.", logger.getErrs().get(2).getEventMsg());
		assertEquals("a3 should associate 0 to 1 target objects (instead of 2)", logger.getErrs().get(3).getEventMsg());
	}
	
	@Test
	public void configMultiplicityOFF_AssociationRefOk(){
		Iom_jObject iomObjI=new Iom_jObject(ILI_CLASSI, OBJ_OID1);
		Iom_jObject iomObjJ=new Iom_jObject(ILI_CLASSJ, OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Association23.Topic.ab4.a4", ValidationConfig.MULTIPLICITY,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjI));
		validator.validate(new ObjectEvent(iomObjJ));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void configTargetOFF_NoTargetObject1toNOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Association23.Topic.ab3.a3", ValidationConfig.TARGET,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void configTargetOFF_WrongTargetClass1to1Ok(){
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSD, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_AB1_A1, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Association23.Topic.ab1.a1", ValidationConfig.TARGET,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void configTargetAndMultiplicityOFF_Embedded_CLASSBassociatetoClassA_ab3_0to1_Ok(){
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSD, OBJ_OID1);
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID2);
		Iom_jObject iomObjB2=new Iom_jObject(ILI_CLASSB, OBJ_OID3);
		iomObjB2.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID2);
		Iom_jObject iomObjB3=new Iom_jObject(ILI_CLASSB, OBJ_OID5);
		iomObjB3.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID6);
		Iom_jObject iomObjB4=new Iom_jObject(ILI_CLASSB, OBJ_OID7);
		iomObjB4.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Association23.Topic.ab3.a3", ValidationConfig.MULTIPLICITY,ValidationConfig.OFF);
		modelConfig.setConfigValue("Association23.Topic.ab3.a3", ValidationConfig.TARGET,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB2));
		validator.validate(new ObjectEvent(iomObjB3));
		validator.validate(new ObjectEvent(iomObjB4));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void configMultiplicityWARNING_AssociationRefFail(){
		Iom_jObject iomObjI=new Iom_jObject(ILI_CLASSI, OBJ_OID1);
		Iom_jObject iomObjJ=new Iom_jObject(ILI_CLASSJ, OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Association23.Topic.ab4.a4", ValidationConfig.MULTIPLICITY,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjI));
		validator.validate(new ObjectEvent(iomObjJ));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getWarn().size()==1);
		assertEquals("a4 should associate 1 to 5 target objects (instead of 0)", logger.getWarn().get(0).getEventMsg());
	}
}