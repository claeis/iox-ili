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

public class Function23Test {
	private TransferDescription td=null;
	// OID
	private final static String OBJ_OID1 ="o1";
	private final static String OBJ_OID2 ="o2";
	private final static String OBJ_OID3 ="o3";
	private final static String OBJ_OID4 ="o4";
	// MODEL
	private final static String ILI_TOPIC="Function23.Topic";
	// CLASS
	private final static String ILI_CLASSA1=ILI_TOPIC+".ClassA1";
	private final static String ILI_CLASSA2=ILI_TOPIC+".ClassA2";
	private final static String ILI_CLASSA3=ILI_TOPIC+".ClassA3";
	private final static String ILI_CLASSB1=ILI_TOPIC+".ClassB1";
	private final static String ILI_CLASSB2=ILI_TOPIC+".ClassB2";
	private final static String ILI_CLASSB3=ILI_TOPIC+".ClassB3";
	private final static String ILI_CLASSC1=ILI_TOPIC+".ClassC1";
	private final static String ILI_CLASSC2=ILI_TOPIC+".ClassC2";
	private final static String ILI_CLASSC3=ILI_TOPIC+".ClassC3";
	private final static String ILI_CLASSD1=ILI_TOPIC+".ClassD1";
	private final static String ILI_CLASSD2=ILI_TOPIC+".ClassD2";
	private final static String ILI_CLASSD3=ILI_TOPIC+".ClassD3";
	private final static String ILI_CLASSG=ILI_TOPIC+".ClassG";
	private final static String ILI_CLASSH=ILI_TOPIC+".ClassH";
	private final static String ILI_CLASSI=ILI_TOPIC+".ClassI";
	private final static String ILI_CLASSJ=ILI_TOPIC+".ClassJ";
	private final static String ILI_CLASSK=ILI_TOPIC+".ClassK";
	private final static String ILI_CLASSL=ILI_TOPIC+".ClassL";
	private final static String ILI_STRUCTM=ILI_TOPIC+".StructM";
	private final static String ILI_CLASSN=ILI_TOPIC+".ClassN";
	private final static String ILI_CLASSO=ILI_TOPIC+".ClassO";
	private final static String ILI_CLASSP=ILI_TOPIC+".ClassP";
	private final static String ILI_CLASSQ=ILI_TOPIC+".ClassQ";
	private final static String ILI_CLASSR=ILI_TOPIC+".ClassR";
	private final static String ILI_CLASSS=ILI_TOPIC+".ClassS";
	private final static String ILI_CLASST=ILI_TOPIC+".ClassT";
	private final static String ILI_CLASSU=ILI_TOPIC+".ClassU";
	// STRUCTURE
	private final static String ILI_STRUCTA=ILI_TOPIC+".StructA";
	private final static String ILI_STRUCTAP=ILI_TOPIC+".StructAp";
	// ASSOCIATION
	private final static String ILI_ASSOC_QR1_Q1="q1";
	private final static String ILI_ASSOC_QR1_R1="r1";
	private final static String ILI_ASSOC_ST1_S1="s1";
	private final static String ILI_ASSOC_ST1_T1="t1";
	// ASSOCIATION CLASS
	private final static String ILI_ASSOC_QR1=ILI_TOPIC+".QR1";
	private final static String ILI_ASSOC_ST1=ILI_TOPIC+".ST1";
	// ATTR
	private final static String OBJ_ATTR1 ="attr1";
	private final static String OBJ_ATTR2 ="attr2";
	private final static String OBJ_ATTR3 ="attr3";
	private final static String OBJ_ATTR4 ="attr4";
	private final static String OBJ_ATTR5 ="attr5";
	private final static String OBJ_ATTR6 ="attr6";
	// START BASKET EVENT
	private final static String BID1="b1";
	private final static String BID2="b2";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/Function23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}

	//#########################################################//
	//######## SUCCESS FUNCTIONS ##############################//
	//#########################################################//	
	@Test
	public void len_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA1, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR1, "abcdefghij");
		iomObjA.setattrvalue(OBJ_ATTR2, "10");
		ValidationConfig modelConfig=new ValidationConfig();
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
	
	@Test
	public void lenWithText_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA2, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR2, "1");
		ValidationConfig modelConfig=new ValidationConfig();
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
	
	@Test
	public void lenTrimAttr1_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA3, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR1, "abcdefghij");
		iomObjA.setattrvalue(OBJ_ATTR2, "10");
		ValidationConfig modelConfig=new ValidationConfig();
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
	
	@Test
	public void lenM_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSB1, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR1, "abcdef\nhij");
		iomObjA.setattrvalue(OBJ_ATTR2, "10");
		ValidationConfig modelConfig=new ValidationConfig();
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
	
	@Test
	public void lenMLenConstant_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSB2, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR2, "1");
		ValidationConfig modelConfig=new ValidationConfig();
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
	
	@Test
	public void lenMTrimConstant_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSB3, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR1, "abcdef\nhij");
		iomObjA.setattrvalue(OBJ_ATTR2, "10");
		ValidationConfig modelConfig=new ValidationConfig();
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
	
	@Test
	public void trim_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSC1, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR1, " abcdefghij ");
		iomObjA.setattrvalue(OBJ_ATTR2, "abcdefghij");
		ValidationConfig modelConfig=new ValidationConfig();
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
	
	@Test
	public void trimConstant_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSC2, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR2, "abcdefghij");
		ValidationConfig modelConfig=new ValidationConfig();
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
	
	@Test
	public void trimTrimAttr1_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSC3, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR1, " abcdefghij ");
		iomObjA.setattrvalue(OBJ_ATTR2, "abcdefghij");
		ValidationConfig modelConfig=new ValidationConfig();
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
	
	@Test
	public void trimM_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSD1, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR1, " abcdef\nhij ");
		iomObjA.setattrvalue(OBJ_ATTR2, "abcdef\nhij");
		ValidationConfig modelConfig=new ValidationConfig();
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
	
	@Test
	public void trimMConstant_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSD2, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR2, "abcdef\\\\nhij");
		ValidationConfig modelConfig=new ValidationConfig();
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
	
	@Test
	public void trimTrimAttr_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSD3, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR1, " abcdef\nhij ");
		iomObjA.setattrvalue(OBJ_ATTR2, "abcdef\nhij");
		ValidationConfig modelConfig=new ValidationConfig();
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

	@Test
	public void isEnumSubVal_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSG, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR1, "mehr");
		iomObjA.setattrvalue(OBJ_ATTR2, "mehr.vier");
		ValidationConfig modelConfig=new ValidationConfig();
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
	
	@Test
	public void inEnumRange_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSH, OBJ_OID1);
		iomObjA.setattrvalue("attr01", "drei");
		iomObjA.setattrvalue("attr02", "zwei");
		iomObjA.setattrvalue("attr03", "vier");
		ValidationConfig modelConfig=new ValidationConfig();
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
	
	@Test
	public void inEnumRangeSubEnum_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSI, OBJ_OID1);
		iomObjA.setattrvalue("attr11", "zwei.zwei");
		iomObjA.setattrvalue("attr12", "eins.zwei");
		iomObjA.setattrvalue("attr13", "drei.zwei");
		ValidationConfig modelConfig=new ValidationConfig();
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
	
//	@Test
//	public void elementCount3_Ok(){
//		Iom_jObject iomObjM=new Iom_jObject(ILI_STRUCTM, null);
//		Iom_jObject iomObjM2=new Iom_jObject(ILI_STRUCTM, null);
//		Iom_jObject iomObjM3=new Iom_jObject(ILI_STRUCTM, null);
//		Iom_jObject iomObjN=new Iom_jObject(ILI_CLASSN, OBJ_OID2);
//		iomObjN.addattrobj("attrbag1", iomObjM);
//		iomObjN.addattrobj("attrbag1", iomObjM2);
//		iomObjN.addattrobj("attrbag1", iomObjM3);
//		iomObjN.setattrvalue("attr2", "3");
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
//		validator.validate(new ObjectEvent(iomObjM));
//		validator.validate(new ObjectEvent(iomObjM2));
//		validator.validate(new ObjectEvent(iomObjM3));
//		validator.validate(new ObjectEvent(iomObjN));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==0);
//	}
	
//	@Test
//	public void elementCount_Ok(){
//		Iom_jObject iomObjM=new Iom_jObject(ILI_STRUCTM, null);
//		Iom_jObject iomObjN=new Iom_jObject(ILI_CLASSN, OBJ_OID2);
//		iomObjN.addattrobj("attrbag1", iomObjM);
//		iomObjN.setattrvalue("attr2", "1");
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
//		validator.validate(new ObjectEvent(iomObjM));
//		validator.validate(new ObjectEvent(iomObjN));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==0);
//	}
	
//	@Test
//	public void elementCount5Lists_Ok(){
//		Iom_jObject iomObjM=new Iom_jObject(ILI_STRUCTM, null);
//		Iom_jObject iomObjM2=new Iom_jObject(ILI_STRUCTM, null);
//		Iom_jObject iomObjM3=new Iom_jObject(ILI_STRUCTM, null);
//		Iom_jObject iomObjM4=new Iom_jObject(ILI_STRUCTM, null);
//		Iom_jObject iomObjM5=new Iom_jObject(ILI_STRUCTM, null);
//		Iom_jObject iomObjO=new Iom_jObject(ILI_CLASSO, OBJ_OID2);
//		iomObjO.addattrobj("attrlist1", iomObjM);
//		iomObjO.addattrobj("attrlist1", iomObjM2);
//		iomObjO.addattrobj("attrlist1", iomObjM3);
//		iomObjO.addattrobj("attrlist1", iomObjM4);
//		iomObjO.addattrobj("attrlist1", iomObjM5);
//		iomObjO.setattrvalue("attr2", "5");
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
//		validator.validate(new ObjectEvent(iomObjM));
//		validator.validate(new ObjectEvent(iomObjM2));
//		validator.validate(new ObjectEvent(iomObjM3));
//		validator.validate(new ObjectEvent(iomObjM4));
//		validator.validate(new ObjectEvent(iomObjM5));
//		validator.validate(new ObjectEvent(iomObjO));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==0);
//	}
	
//	@Test
//	public void elementCountList_Ok(){
//		Iom_jObject iomObjM=new Iom_jObject(ILI_STRUCTM, null);
//		Iom_jObject iomObjO=new Iom_jObject(ILI_CLASSO, OBJ_OID2);
//		iomObjO.addattrobj("attrlist1", iomObjM);
//		iomObjO.setattrvalue("attr2", "1");
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
//		validator.validate(new ObjectEvent(iomObjM));
//		validator.validate(new ObjectEvent(iomObjO));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==0);
//	}
	
	@Test
	public void objectCountALL_Ok(){
		Iom_jObject iomObjQ1=new Iom_jObject(ILI_CLASSQ, OBJ_OID1);
		Iom_jObject iomObjQ2=new Iom_jObject(ILI_CLASSQ, OBJ_OID2);
		Iom_jObject iomObjR1=new Iom_jObject(ILI_CLASSR, OBJ_OID3);
		Iom_jObject iomObjR2=new Iom_jObject(ILI_CLASSR, OBJ_OID4);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObjQ1));
		validator.validate(new ObjectEvent(iomObjQ2));
		validator.validate(new ObjectEvent(iomObjR1));
		validator.validate(new ObjectEvent(iomObjR2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
//	@Test
//	public void isOfClassWithRef_Ok(){
//		String objTargetId=OBJ_OID1;
//		Iom_jObject iomObjS1=new Iom_jObject(ILI_STRUCTA, null);
//		Iom_jObject iomObjAP=new Iom_jObject(ILI_STRUCTAP, objTargetId);
//		Iom_jObject o1Ref=new Iom_jObject("REF", null);
//		o1Ref.setobjectrefoid(objTargetId);
//		Iom_jObject iomObjU=new Iom_jObject(ILI_CLASSU, OBJ_OID1);
//		iomObjU.addattrobj("attrU1", iomObjAP);
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
//		validator.validate(new ObjectEvent(iomObjU));
//		validator.validate(new ObjectEvent(iomObjS1));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==0);
//	}
	
//	@Test
//	public void objectCountRole_Ok(){
//		// erstes S->T
//		Iom_jObject iomObjS1=new Iom_jObject(ILI_CLASSS, OBJ_OID1);
//		Iom_jObject iomObjT1=new Iom_jObject(ILI_CLASST, OBJ_OID3);
//		Iom_jObject iomObjST1=new Iom_jObject(ILI_ASSOC_ST1, null);
//		iomObjST1.addattrobj(ILI_ASSOC_ST1_S1, "REF").setobjectrefoid(OBJ_OID1);
//		iomObjST1.addattrobj(ILI_ASSOC_ST1_T1, "REF").setobjectrefoid(OBJ_OID3);
//		// zweites S->T
//		Iom_jObject iomObjS2=new Iom_jObject(ILI_CLASSS, OBJ_OID2);
//		Iom_jObject iomObjT2=new Iom_jObject(ILI_CLASST, OBJ_OID4);
//		Iom_jObject iomObjST2=new Iom_jObject(ILI_ASSOC_ST1, null);
//		iomObjST2.addattrobj(ILI_ASSOC_ST1_S1, "REF").setobjectrefoid(OBJ_OID2);
//		iomObjST2.addattrobj(ILI_ASSOC_ST1_T1, "REF").setobjectrefoid(OBJ_OID4);
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
//		validator.validate(new ObjectEvent(iomObjS1));
//		validator.validate(new ObjectEvent(iomObjS2));
//		validator.validate(new ObjectEvent(iomObjT1));
//		validator.validate(new ObjectEvent(iomObjT2));
//		validator.validate(new ObjectEvent(iomObjST1));
//		validator.validate(new ObjectEvent(iomObjST2));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==1);
//	}
	
	//#########################################################//
	//######## FAIL FUNCTIONS #################################//
	//#########################################################//
	@Test
	public void isEnumSubVal_MehrVierIsNotSubValOfEins_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSG, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR1, "eins");
		iomObjA.setattrvalue(OBJ_ATTR2, "mehr.vier");
		ValidationConfig modelConfig=new ValidationConfig();
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
	public void inEnumRange_EnumerationNotOrdered_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSL, OBJ_OID1);
		iomObjA.setattrvalue("attr01", "drei");
		iomObjA.setattrvalue("attr02", "zwei");
		iomObjA.setattrvalue("attr03", "vier");
		ValidationConfig modelConfig=new ValidationConfig();
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
	public void inEnumRange_EnumWithSubEnumNotBetween_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSI, OBJ_OID1);
		iomObjA.setattrvalue("attr12", "zwei.zwei");
		iomObjA.setattrvalue("attr11", "eins.zwei");
		iomObjA.setattrvalue("attr13", "drei.zwei");
		ValidationConfig modelConfig=new ValidationConfig();
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
	public void inEnumRange_EnumerationsNotSame_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSK, OBJ_OID1);
		iomObjA.setattrvalue("attr11", "zwei");
		iomObjA.setattrvalue("attr21", "eins.zwei");
		iomObjA.setattrvalue("attr31", "drei.zwei");
		ValidationConfig modelConfig=new ValidationConfig();
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
	public void len_numberOrCharNotEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA1, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR1, "abcdefghij");
		iomObjA.setattrvalue(OBJ_ATTR2, "9");
		ValidationConfig modelConfig=new ValidationConfig();
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
	public void len_StringLenthNotEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA2, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR2, "2");
		ValidationConfig modelConfig=new ValidationConfig();
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
	public void len_TrimLengthNotEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA3, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR1, "abcdefghijk");
		iomObjA.setattrvalue(OBJ_ATTR2, "10");
		ValidationConfig modelConfig=new ValidationConfig();
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
	public void lenM_lengthNotEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSB1, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR1, "abdef\nhij");
		iomObjA.setattrvalue(OBJ_ATTR2, "10");
		ValidationConfig modelConfig=new ValidationConfig();
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
	public void lenM_ConstantLengthNotEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSB2, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR2, "2");
		ValidationConfig modelConfig=new ValidationConfig();
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
	public void lenM_TrimLengthNotEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSB3, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR1, "abcdef\nhi");
		iomObjA.setattrvalue(OBJ_ATTR2, "10");
		ValidationConfig modelConfig=new ValidationConfig();
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
	public void trim_LengthNotEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSC1, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR1, " abcdefghi ");
		iomObjA.setattrvalue(OBJ_ATTR2, "abcdefghij");
		ValidationConfig modelConfig=new ValidationConfig();
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
	public void trim_ConstantLengthNotEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSC2, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR2, "abcdefghi");
		ValidationConfig modelConfig=new ValidationConfig();
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
	public void trim_TrimedTextLengthNotEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSC3, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR1, " abdefghij ");
		iomObjA.setattrvalue(OBJ_ATTR2, "abcdefghij");
		ValidationConfig modelConfig=new ValidationConfig();
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
	public void trimM_MTrimmedLengthNotEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSD1, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR1, " abcdef\nhij ");
		iomObjA.setattrvalue(OBJ_ATTR2, "bcdef\nhij");
		ValidationConfig modelConfig=new ValidationConfig();
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
	public void trimM_ConstantLengthNotEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSD2, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR2, "abcdef\\\\nhj");
		ValidationConfig modelConfig=new ValidationConfig();
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
	public void trimM_TrimmedLengthNotEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSD3, OBJ_OID1);
		iomObjA.setattrvalue(OBJ_ATTR1, " abcdef\nij ");
		iomObjA.setattrvalue(OBJ_ATTR2, "abcdef\nhij");
		ValidationConfig modelConfig=new ValidationConfig();
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
	public void inEnumRange_1IsNotBetween2And4_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSH, OBJ_OID1);
		iomObjA.setattrvalue("attr01", "eins");
		iomObjA.setattrvalue("attr02", "zwei");
		iomObjA.setattrvalue("attr03", "vier");
		ValidationConfig modelConfig=new ValidationConfig();
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
	
//	@Test
//	public void elementCount_CountOfBagNotEqual_Fail(){
//		Iom_jObject iomObjM=new Iom_jObject(ILI_STRUCTM, null);
//		Iom_jObject iomObjM2=new Iom_jObject(ILI_STRUCTM, null);
//		Iom_jObject iomObjM3=new Iom_jObject(ILI_STRUCTM, null);
//		Iom_jObject iomObjN=new Iom_jObject(ILI_CLASSN, OBJ_OID2);
//		iomObjN.addattrobj("attrbag1", iomObjM);
//		iomObjN.addattrobj("attrbag1", iomObjM2);
//		iomObjN.addattrobj("attrbag1", iomObjM3);
//		iomObjN.setattrvalue("attr2", "5");
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
//		validator.validate(new ObjectEvent(iomObjM));
//		validator.validate(new ObjectEvent(iomObjM2));
//		validator.validate(new ObjectEvent(iomObjM3));
//		validator.validate(new ObjectEvent(iomObjN));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==1);
//		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
//	}
	
//	@Test
//	public void elementCount_ListCountNotEqual_Fail(){
//		Iom_jObject iomObjM=new Iom_jObject(ILI_STRUCTM, null);
//		Iom_jObject iomObjM2=new Iom_jObject(ILI_STRUCTM, null);
//		Iom_jObject iomObjM3=new Iom_jObject(ILI_STRUCTM, null);
//		Iom_jObject iomObjM4=new Iom_jObject(ILI_STRUCTM, null);
//		Iom_jObject iomObjM5=new Iom_jObject(ILI_STRUCTM, null);
//		Iom_jObject iomObjO=new Iom_jObject(ILI_CLASSO, OBJ_OID2);
//		iomObjO.addattrobj("attrlist1", iomObjM);
//		iomObjO.addattrobj("attrlist1", iomObjM2);
//		iomObjO.addattrobj("attrlist1", iomObjM3);
//		iomObjO.addattrobj("attrlist1", iomObjM4);
//		iomObjO.addattrobj("attrlist1", iomObjM5);
//		iomObjO.setattrvalue("attr2", "6");
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
//		validator.validate(new ObjectEvent(iomObjM));
//		validator.validate(new ObjectEvent(iomObjM2));
//		validator.validate(new ObjectEvent(iomObjM3));
//		validator.validate(new ObjectEvent(iomObjM4));
//		validator.validate(new ObjectEvent(iomObjM5));
//		validator.validate(new ObjectEvent(iomObjO));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==1);
//		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
//	}
	
	@Test
	public void objectCountALL_ObjectCountNotEqual_Fail(){
		Iom_jObject iomObjQ1=new Iom_jObject(ILI_CLASSQ, OBJ_OID1);
		Iom_jObject iomObjR1=new Iom_jObject(ILI_CLASSR, OBJ_OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObjQ1));
		validator.validate(new ObjectEvent(iomObjR1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
//	@Test
//	public void objectCountRole_RoleCountNotEqual_Fail(){
//		// erstes S->T
//		Iom_jObject iomObjS1=new Iom_jObject(ILI_CLASSS, OBJ_OID1);
//		Iom_jObject iomObjT1=new Iom_jObject(ILI_CLASST, OBJ_OID3);
//		Iom_jObject iomObjST1=new Iom_jObject(ILI_ASSOC_ST1, null);
//		iomObjST1.addattrobj(ILI_ASSOC_ST1_S1, "REF").setobjectrefoid(OBJ_OID1);
//		iomObjST1.addattrobj(ILI_ASSOC_ST1_T1, "REF").setobjectrefoid(OBJ_OID3);
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
//		validator.validate(new ObjectEvent(iomObjS1));
//		validator.validate(new ObjectEvent(iomObjT1));
//		validator.validate(new ObjectEvent(iomObjST1));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==1);
//		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
//	}
	
	@Test
	public void isOfClass_NotYetImplemented_Fail(){
		String objTargetId=OBJ_OID1;
		Iom_jObject iomObjS1=new Iom_jObject(ILI_STRUCTA, null);
		Iom_jObject iomObjAP=new Iom_jObject(ILI_STRUCTAP, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomObjU=new Iom_jObject(ILI_CLASSU, OBJ_OID1);
		iomObjU.addattrobj("attrU1", iomObjAP);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObjU));
		validator.validate(new ObjectEvent(iomObjS1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Function isOfClass is not yet implemented.", logger.getErrs().get(0).getEventMsg());
	}
}