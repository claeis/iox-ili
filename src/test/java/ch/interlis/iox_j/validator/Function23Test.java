package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;
import org.junit.Before;
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
	private final static String ILI_CLASSUA=ILI_TOPIC+".ClassUA";
	private final static String ILI_CLASSV=ILI_TOPIC+".ClassV";
	private final static String ILI_CLASSW=ILI_TOPIC+".ClassW";
	private final static String ILI_CLASSWA=ILI_TOPIC+".ClassWA";
	private final static String ILI_CLASSWB=ILI_TOPIC+".ClassWB";
	private final static String ILI_CLASSX=ILI_TOPIC+".ClassX";
	private final static String ILI_CLASSY=ILI_TOPIC+".ClassY";
	private final static String ILI_CLASSZ=ILI_TOPIC+".ClassZ";
	private final static String ILI_CLASSZA=ILI_TOPIC+".ClassZA";
	private final static String ILI_CLASSZB=ILI_TOPIC+".ClassZB";
	private final static String ILI_CLASSZC=ILI_TOPIC+".ClassZC";
	private final static String ILI_CLASSZD=ILI_TOPIC+".ClassZD";
	private final static String ILI_CLASSZE=ILI_TOPIC+".ClassZE";
	private final static String ILI_CLASSZF=ILI_TOPIC+".ClassZF";
	private final static String ILI_CLASSZG=ILI_TOPIC+".ClassZG";
	private final static String ILI_CLASSZH=ILI_TOPIC+".ClassZH";
	private final static String ILI_CLASSZI=ILI_TOPIC+".ClassZI";
	private final static String ILI_CLASSXA=ILI_TOPIC+".ClassXA";
	private final static String ILI_CLASSXB=ILI_TOPIC+".ClassXB";
	// STRUCTURE
	private final static String ILI_STRUCTA=ILI_TOPIC+".StructA";
	private final static String ILI_STRUCTB=ILI_TOPIC+".StructB";
	private final static String ILI_STRUCTF=ILI_TOPIC+".StructF";
	private final static String ILI_STRUCTAP=ILI_TOPIC+".StructAp";
	private final static String ILI_STRUCTBP=ILI_TOPIC+".StructBp";
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
	
	@Test
	public void elementCount3_Ok(){
		Iom_jObject iomObjM=new Iom_jObject(ILI_STRUCTM, null);
		Iom_jObject iomObjM2=new Iom_jObject(ILI_STRUCTM, null);
		Iom_jObject iomObjM3=new Iom_jObject(ILI_STRUCTM, null);
		Iom_jObject iomObjN=new Iom_jObject(ILI_CLASSN, OBJ_OID2);
		iomObjN.addattrobj("attrbag1", iomObjM);
		iomObjN.addattrobj("attrbag1", iomObjM2);
		iomObjN.addattrobj("attrbag1", iomObjM3);
		iomObjN.setattrvalue("attr2", "3");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObjM));
		validator.validate(new ObjectEvent(iomObjM2));
		validator.validate(new ObjectEvent(iomObjM3));
		validator.validate(new ObjectEvent(iomObjN));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void elementCount_Ok(){
		Iom_jObject iomObjM=new Iom_jObject(ILI_STRUCTM, null);
		Iom_jObject iomObjN=new Iom_jObject(ILI_CLASSN, OBJ_OID2);
		iomObjN.addattrobj("attrbag1", iomObjM);
		iomObjN.setattrvalue("attr2", "1");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObjM));
		validator.validate(new ObjectEvent(iomObjN));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void elementCount5Lists_Ok(){
		Iom_jObject iomObjM=new Iom_jObject(ILI_STRUCTM, null);
		Iom_jObject iomObjM2=new Iom_jObject(ILI_STRUCTM, null);
		Iom_jObject iomObjM3=new Iom_jObject(ILI_STRUCTM, null);
		Iom_jObject iomObjM4=new Iom_jObject(ILI_STRUCTM, null);
		Iom_jObject iomObjM5=new Iom_jObject(ILI_STRUCTM, null);
		Iom_jObject iomObjO=new Iom_jObject(ILI_CLASSO, OBJ_OID2);
		iomObjO.addattrobj("attrlist1", iomObjM);
		iomObjO.addattrobj("attrlist1", iomObjM2);
		iomObjO.addattrobj("attrlist1", iomObjM3);
		iomObjO.addattrobj("attrlist1", iomObjM4);
		iomObjO.addattrobj("attrlist1", iomObjM5);
		iomObjO.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObjM));
		validator.validate(new ObjectEvent(iomObjM2));
		validator.validate(new ObjectEvent(iomObjM3));
		validator.validate(new ObjectEvent(iomObjM4));
		validator.validate(new ObjectEvent(iomObjM5));
		validator.validate(new ObjectEvent(iomObjO));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void elementCountList_Ok(){
		Iom_jObject iomObjM=new Iom_jObject(ILI_STRUCTM, null);
		Iom_jObject iomObjO=new Iom_jObject(ILI_CLASSO, OBJ_OID2);
		iomObjO.addattrobj("attrlist1", iomObjM);
		iomObjO.setattrvalue("attr2", "1");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObjM));
		validator.validate(new ObjectEvent(iomObjO));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
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
	
	@Test
	public void isOfClassWithRef_Ok(){
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
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void isOfClassWithRefAndMyClass_Ok(){
		String objTargetId=OBJ_OID1;
		Iom_jObject iomObjAP=new Iom_jObject(ILI_STRUCTAP, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomObjU=new Iom_jObject(ILI_CLASSUA, OBJ_OID1);
		iomObjU.addattrobj("attrU1", iomObjAP);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObjU));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void isSubClassConstants_Ok(){
		Iom_jObject iomObjW=new Iom_jObject(ILI_CLASSW, OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObjW));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void myClass_Ok(){
		Iom_jObject iomObjS1=new Iom_jObject(ILI_STRUCTB, null);
		Iom_jObject iomObjU=new Iom_jObject(ILI_CLASSY, OBJ_OID1);
		iomObjU.addattrobj("attrY1", iomObjS1);
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
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void isSubClassWithRefAndMyClass_Ok(){
		String objTargetId=OBJ_OID1;
		Iom_jObject iomObjS1=new Iom_jObject(ILI_STRUCTB, null);
		Iom_jObject iomObjAP=new Iom_jObject(ILI_STRUCTBP, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomObjU=new Iom_jObject(ILI_CLASSWA, OBJ_OID1);
		iomObjU.addattrobj("attrWA1", iomObjAP);
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
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void objectCountRole_Ok(){
		// erstes S->T
		Iom_jObject iomObjS1=new Iom_jObject(ILI_CLASSS, OBJ_OID1);
		Iom_jObject iomObjT1=new Iom_jObject(ILI_CLASST, OBJ_OID3);
		Iom_jObject iomObjST1=new Iom_jObject(ILI_ASSOC_ST1, null);
		iomObjST1.addattrobj(ILI_ASSOC_ST1_S1, "REF").setobjectrefoid(OBJ_OID1);
		iomObjST1.addattrobj(ILI_ASSOC_ST1_T1, "REF").setobjectrefoid(OBJ_OID3);
		// zweites S->T
		Iom_jObject iomObjS2=new Iom_jObject(ILI_CLASSS, OBJ_OID2);
		Iom_jObject iomObjT2=new Iom_jObject(ILI_CLASST, OBJ_OID4);
		Iom_jObject iomObjST2=new Iom_jObject(ILI_ASSOC_ST1, null);
		iomObjST2.addattrobj(ILI_ASSOC_ST1_S1, "REF").setobjectrefoid(OBJ_OID2);
		iomObjST2.addattrobj(ILI_ASSOC_ST1_T1, "REF").setobjectrefoid(OBJ_OID4);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObjS1));
		validator.validate(new ObjectEvent(iomObjS2));
		validator.validate(new ObjectEvent(iomObjT1));
		validator.validate(new ObjectEvent(iomObjT2));
		validator.validate(new ObjectEvent(iomObjST1));
		validator.validate(new ObjectEvent(iomObjST2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// 2 objects. Objects with Class implementation
	@Test
	public void areArea_ObjectsWithAll_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSZA, OBJ_OID1);
		// Geometrie 1
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("Geometrie", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "483000.000");
		endSegment.setattrvalue("C2", "70000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "483000.000");
		startSegment2.setattrvalue("C2", "70000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "480000.000");
		endSegment2.setattrvalue("C2", "73000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "480000.000");
		startSegment3.setattrvalue("C2", "73000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// Geometrie 2
		Iom_jObject objSurfaceSuccess2=new Iom_jObject(ILI_CLASSZA, OBJ_OID2);
		IomObject multisurfaceValue2=objSurfaceSuccess2.addattrobj("Geometrie", "MULTISURFACE");
		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue5 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments5=polylineValue5.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment5=segments5.addattrobj("segment", "COORD");
		startSegment5.setattrvalue("C1", "484000.000");
		startSegment5.setattrvalue("C2", "70000.000");
		IomObject endSegment5=segments5.addattrobj("segment", "COORD");
		endSegment5.setattrvalue("C1", "484000.000");
		endSegment5.setattrvalue("C2", "72500.000");
		// polyline 2
		IomObject polylineValue4 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments4=polylineValue4.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment4=segments4.addattrobj("segment", "COORD");
		startSegment4.setattrvalue("C1", "484000.000");
		startSegment4.setattrvalue("C2", "72500.000");
		IomObject endSegment4=segments4.addattrobj("segment", "COORD");
		endSegment4.setattrvalue("C1", "488000.000");
		endSegment4.setattrvalue("C2", "70500.000");
		// polyline 3
		IomObject polylineValue6 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments6=polylineValue6.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment6=segments6.addattrobj("segment", "COORD");
		startSegment6.setattrvalue("C1", "488000.000");
		startSegment6.setattrvalue("C2", "70500.000");
		IomObject endSegment6=segments6.addattrobj("segment", "COORD");
		endSegment6.setattrvalue("C1", "484000.000");
		endSegment6.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new ObjectEvent(objSurfaceSuccess2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// 2 objects. Objects with Class implementation
	@Test
	public void areArea_ObjectsWithClass_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSZB, OBJ_OID1);
		// Geometrie 1
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("Geometrie", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "483000.000");
		endSegment.setattrvalue("C2", "70000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "483000.000");
		startSegment2.setattrvalue("C2", "70000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "480000.000");
		endSegment2.setattrvalue("C2", "73000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "480000.000");
		startSegment3.setattrvalue("C2", "73000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// Geometrie 2
		Iom_jObject objSurfaceSuccess2=new Iom_jObject(ILI_CLASSZB, OBJ_OID2);
		IomObject multisurfaceValue2=objSurfaceSuccess2.addattrobj("Geometrie", "MULTISURFACE");
		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue5 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments5=polylineValue5.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment5=segments5.addattrobj("segment", "COORD");
		startSegment5.setattrvalue("C1", "484000.000");
		startSegment5.setattrvalue("C2", "70000.000");
		IomObject endSegment5=segments5.addattrobj("segment", "COORD");
		endSegment5.setattrvalue("C1", "484000.000");
		endSegment5.setattrvalue("C2", "72500.000");
		// polyline 2
		IomObject polylineValue4 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments4=polylineValue4.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment4=segments4.addattrobj("segment", "COORD");
		startSegment4.setattrvalue("C1", "484000.000");
		startSegment4.setattrvalue("C2", "72500.000");
		IomObject endSegment4=segments4.addattrobj("segment", "COORD");
		endSegment4.setattrvalue("C1", "488000.000");
		endSegment4.setattrvalue("C2", "70500.000");
		// polyline 3
		IomObject polylineValue6 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments6=polylineValue6.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment6=segments6.addattrobj("segment", "COORD");
		startSegment6.setattrvalue("C1", "488000.000");
		startSegment6.setattrvalue("C2", "70500.000");
		IomObject endSegment6=segments6.addattrobj("segment", "COORD");
		endSegment6.setattrvalue("C1", "484000.000");
		endSegment6.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new ObjectEvent(objSurfaceSuccess2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}	
	
	// 1 object. Objects=ALL, SurfaceBAG=BAG count, SurfaceAttr=surface of other structure
	@Test
	public void areArea_SurfaceAttrOfStruct_Ok(){
		Iom_jObject iomObjStruct=new Iom_jObject(ILI_STRUCTF, null);
		// Geometrie 1
		IomObject multisurfaceValue=iomObjStruct.addattrobj("Surface", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "483000.000");
		endSegment.setattrvalue("C2", "70000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "483000.000");
		startSegment2.setattrvalue("C2", "70000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "480000.000");
		endSegment2.setattrvalue("C2", "73000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "480000.000");
		startSegment3.setattrvalue("C2", "73000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSZF, OBJ_OID1);
		objSurfaceSuccess.addattrobj("Numbers", iomObjStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjStruct));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// 1 object. Objects=Class, SurfaceBAG=BAG count, SurfaceAttr=surface of other structure
	@Test
	public void areArea_ObjectsClassAndSurfaceBAGDefined_Ok(){
		Iom_jObject iomObjStruct=new Iom_jObject(ILI_STRUCTF, null);
		// Geometrie 1
		IomObject multisurfaceValue=iomObjStruct.addattrobj("Surface", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "483000.000");
		endSegment.setattrvalue("C2", "70000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "483000.000");
		startSegment2.setattrvalue("C2", "70000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "480000.000");
		endSegment2.setattrvalue("C2", "73000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "480000.000");
		startSegment3.setattrvalue("C2", "73000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSZI, OBJ_OID1);
		objSurfaceSuccess.addattrobj("Numbers", iomObjStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjStruct));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}	
	
	@Test
	public void areArea_SurfaceAttrNoSurfaceFound_Ok(){
		Iom_jObject iomObjStruct=new Iom_jObject(ILI_STRUCTF, null);
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSZG, OBJ_OID1);
		objSurfaceSuccess.addattrobj("Numbers", iomObjStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjStruct));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
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
	
	@Test
	public void elementCount_CountOfBagNotEqual_Fail(){
		Iom_jObject iomObjM=new Iom_jObject(ILI_STRUCTM, null);
		Iom_jObject iomObjM2=new Iom_jObject(ILI_STRUCTM, null);
		Iom_jObject iomObjM3=new Iom_jObject(ILI_STRUCTM, null);
		Iom_jObject iomObjN=new Iom_jObject(ILI_CLASSN, OBJ_OID2);
		iomObjN.addattrobj("attrbag1", iomObjM);
		iomObjN.addattrobj("attrbag1", iomObjM2);
		iomObjN.addattrobj("attrbag1", iomObjM3);
		iomObjN.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObjM));
		validator.validate(new ObjectEvent(iomObjM2));
		validator.validate(new ObjectEvent(iomObjM3));
		validator.validate(new ObjectEvent(iomObjN));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void elementCount_ListCountNotEqual_Fail(){
		Iom_jObject iomObjM=new Iom_jObject(ILI_STRUCTM, null);
		Iom_jObject iomObjM2=new Iom_jObject(ILI_STRUCTM, null);
		Iom_jObject iomObjM3=new Iom_jObject(ILI_STRUCTM, null);
		Iom_jObject iomObjM4=new Iom_jObject(ILI_STRUCTM, null);
		Iom_jObject iomObjM5=new Iom_jObject(ILI_STRUCTM, null);
		Iom_jObject iomObjO=new Iom_jObject(ILI_CLASSO, OBJ_OID2);
		iomObjO.addattrobj("attrlist1", iomObjM);
		iomObjO.addattrobj("attrlist1", iomObjM2);
		iomObjO.addattrobj("attrlist1", iomObjM3);
		iomObjO.addattrobj("attrlist1", iomObjM4);
		iomObjO.addattrobj("attrlist1", iomObjM5);
		iomObjO.setattrvalue("attr2", "6");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObjM));
		validator.validate(new ObjectEvent(iomObjM2));
		validator.validate(new ObjectEvent(iomObjM3));
		validator.validate(new ObjectEvent(iomObjM4));
		validator.validate(new ObjectEvent(iomObjM5));
		validator.validate(new ObjectEvent(iomObjO));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
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
	
	@Test
	public void objectCountRole_RoleCountNotEqual_Fail(){
		// erstes S->T
		Iom_jObject iomObjS1=new Iom_jObject(ILI_CLASSS, OBJ_OID1);
		Iom_jObject iomObjT1=new Iom_jObject(ILI_CLASST, OBJ_OID3);
		Iom_jObject iomObjST1=new Iom_jObject(ILI_ASSOC_ST1, null);
		iomObjST1.addattrobj(ILI_ASSOC_ST1_S1, "REF").setobjectrefoid(OBJ_OID1);
		iomObjST1.addattrobj(ILI_ASSOC_ST1_T1, "REF").setobjectrefoid(OBJ_OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObjS1));
		validator.validate(new ObjectEvent(iomObjT1));
		validator.validate(new ObjectEvent(iomObjST1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void isOfClass_ParentClassNotValid_Fail(){
		String objTargetId=OBJ_OID1;
		Iom_jObject iomObjB=new Iom_jObject(ILI_STRUCTB, null);
		Iom_jObject iomObjBP=new Iom_jObject(ILI_STRUCTBP, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomObjV=new Iom_jObject(ILI_CLASSV, OBJ_OID1);
		iomObjV.addattrobj("attrV1", iomObjBP);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObjV));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void isOfClass_ChildClassNotValid_Fail(){
		String objTargetId=OBJ_OID1;
		Iom_jObject iomObjB=new Iom_jObject(ILI_STRUCTB, null);
		Iom_jObject iomObjBP=new Iom_jObject(ILI_STRUCTAP, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomObjV=new Iom_jObject(ILI_CLASSV, OBJ_OID1);
		iomObjV.addattrobj("attrV1", iomObjBP);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObjV));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("Attribute attrV1 requires a structure Function23.Topic.StructB", logger.getErrs().get(0).getEventMsg());
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(1).getEventMsg());
	}
	
	@Test
	public void isSubClassConstants_SubClassNotValid_Fail(){
		Iom_jObject iomObjW=new Iom_jObject(ILI_CLASSX, OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObjW));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void myClass_WrongClass_Fail(){
		Iom_jObject iomObjS1=new Iom_jObject(ILI_STRUCTB, null);
		Iom_jObject iomObjU=new Iom_jObject(ILI_CLASSZ, OBJ_OID1);
		iomObjU.addattrobj("attrZ1", iomObjS1);
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
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void isOfClass_MyClassNotValid_Fail(){
		String objTargetId=OBJ_OID1;
		Iom_jObject iomObjS1=new Iom_jObject(ILI_STRUCTA, null);
		Iom_jObject iomObjAP=new Iom_jObject(ILI_STRUCTAP, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomObjU=new Iom_jObject(ILI_CLASSWB, OBJ_OID1);
		iomObjU.addattrobj("attrWA1", iomObjAP);
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// 2 objects == 4 intersection fails and 2 mandatoryConstraint fails.
	@Test
	public void areArea_ObjectsWithAll_IntersectionAreas_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSZA, OBJ_OID1);
		// Geometrie 1
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("Geometrie", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "483000.000");
		endSegment.setattrvalue("C2", "70000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "483000.000");
		startSegment2.setattrvalue("C2", "70000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "480000.000");
		endSegment2.setattrvalue("C2", "73000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "480000.000");
		startSegment3.setattrvalue("C2", "73000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// Geometrie 2
		Iom_jObject objSurfaceSuccess2=new Iom_jObject(ILI_CLASSZA, OBJ_OID2);
		IomObject multisurfaceValue2=objSurfaceSuccess2.addattrobj("Geometrie", "MULTISURFACE");
		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue5 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments5=polylineValue5.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment5=segments5.addattrobj("segment", "COORD");
		startSegment5.setattrvalue("C1", "484000.000");
		startSegment5.setattrvalue("C2", "70000.000");
		IomObject endSegment5=segments5.addattrobj("segment", "COORD");
		endSegment5.setattrvalue("C1", "484000.000");
		endSegment5.setattrvalue("C2", "72500.000");
		// polyline 2
		IomObject polylineValue4 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments4=polylineValue4.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment4=segments4.addattrobj("segment", "COORD");
		startSegment4.setattrvalue("C1", "484000.000");
		startSegment4.setattrvalue("C2", "72500.000");
		IomObject endSegment4=segments4.addattrobj("segment", "COORD");
		endSegment4.setattrvalue("C1", "480500.000");
		endSegment4.setattrvalue("C2", "70500.000");
		// polyline 3
		IomObject polylineValue6 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments6=polylineValue6.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment6=segments6.addattrobj("segment", "COORD");
		startSegment6.setattrvalue("C1", "480500.000");
		startSegment6.setattrvalue("C2", "70500.000");
		IomObject endSegment6=segments6.addattrobj("segment", "COORD");
		endSegment6.setattrvalue("C1", "484000.000");
		endSegment6.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new ObjectEvent(objSurfaceSuccess2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// 2 objects == 2 mandatoryConstraint fails.
	@Test
	public void areArea_ObjectsWithClass_IntersectionAreas_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSZB, OBJ_OID1);
		// Geometrie 1
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("Geometrie", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "483000.000");
		endSegment.setattrvalue("C2", "70000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "483000.000");
		startSegment2.setattrvalue("C2", "70000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "480000.000");
		endSegment2.setattrvalue("C2", "73000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "480000.000");
		startSegment3.setattrvalue("C2", "73000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// Geometrie 2
		Iom_jObject objSurfaceSuccess2=new Iom_jObject(ILI_CLASSZB, OBJ_OID2);
		IomObject multisurfaceValue2=objSurfaceSuccess2.addattrobj("Geometrie", "MULTISURFACE");
		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue5 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments5=polylineValue5.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment5=segments5.addattrobj("segment", "COORD");
		startSegment5.setattrvalue("C1", "484000.000");
		startSegment5.setattrvalue("C2", "70000.000");
		IomObject endSegment5=segments5.addattrobj("segment", "COORD");
		endSegment5.setattrvalue("C1", "484000.000");
		endSegment5.setattrvalue("C2", "72500.000");
		// polyline 2
		IomObject polylineValue4 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments4=polylineValue4.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment4=segments4.addattrobj("segment", "COORD");
		startSegment4.setattrvalue("C1", "484000.000");
		startSegment4.setattrvalue("C2", "72500.000");
		IomObject endSegment4=segments4.addattrobj("segment", "COORD");
		endSegment4.setattrvalue("C1", "480500.000");
		endSegment4.setattrvalue("C2", "70500.000");
		// polyline 3
		IomObject polylineValue6 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments6=polylineValue6.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment6=segments6.addattrobj("segment", "COORD");
		startSegment6.setattrvalue("C1", "480500.000");
		startSegment6.setattrvalue("C2", "70500.000");
		IomObject endSegment6=segments6.addattrobj("segment", "COORD");
		endSegment6.setattrvalue("C1", "484000.000");
		endSegment6.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new ObjectEvent(objSurfaceSuccess2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// 2 objects == Class in Objects --> not valid
	@Test
	public void areArea_ClassOfObjectsNotValid_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSZC, OBJ_OID1);
		// Geometrie 1
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("Geometrie", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "483000.000");
		endSegment.setattrvalue("C2", "70000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "483000.000");
		startSegment2.setattrvalue("C2", "70000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "480000.000");
		endSegment2.setattrvalue("C2", "73000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "480000.000");
		startSegment3.setattrvalue("C2", "73000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// Geometrie 2
		Iom_jObject objSurfaceSuccess2=new Iom_jObject(ILI_CLASSZC, OBJ_OID2);
		IomObject multisurfaceValue2=objSurfaceSuccess2.addattrobj("Geometrie", "MULTISURFACE");
		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue5 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments5=polylineValue5.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment5=segments5.addattrobj("segment", "COORD");
		startSegment5.setattrvalue("C1", "484000.000");
		startSegment5.setattrvalue("C2", "70000.000");
		IomObject endSegment5=segments5.addattrobj("segment", "COORD");
		endSegment5.setattrvalue("C1", "484000.000");
		endSegment5.setattrvalue("C2", "72500.000");
		// polyline 2
		IomObject polylineValue4 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments4=polylineValue4.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment4=segments4.addattrobj("segment", "COORD");
		startSegment4.setattrvalue("C1", "484000.000");
		startSegment4.setattrvalue("C2", "72500.000");
		IomObject endSegment4=segments4.addattrobj("segment", "COORD");
		endSegment4.setattrvalue("C1", "488000.000");
		endSegment4.setattrvalue("C2", "70500.000");
		// polyline 3
		IomObject polylineValue6 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments6=polylineValue6.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment6=segments6.addattrobj("segment", "COORD");
		startSegment6.setattrvalue("C1", "488000.000");
		startSegment6.setattrvalue("C2", "70500.000");
		IomObject endSegment6=segments6.addattrobj("segment", "COORD");
		endSegment6.setattrvalue("C1", "484000.000");
		endSegment6.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new ObjectEvent(objSurfaceSuccess2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(1).getEventMsg());
	}
	
	// 2 objects == second mandatory: class in Objects = fail
	@Test
	public void areArea_SecondClassOfObjectsNotValid_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSZD, OBJ_OID1);
		// Geometrie 1
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("Geometrie", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "483000.000");
		endSegment.setattrvalue("C2", "70000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "483000.000");
		startSegment2.setattrvalue("C2", "70000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "480000.000");
		endSegment2.setattrvalue("C2", "73000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "480000.000");
		startSegment3.setattrvalue("C2", "73000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// Geometrie 2
		Iom_jObject objSurfaceSuccess2=new Iom_jObject(ILI_CLASSZD, OBJ_OID2);
		IomObject multisurfaceValue2=objSurfaceSuccess2.addattrobj("Geometrie", "MULTISURFACE");
		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue5 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments5=polylineValue5.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment5=segments5.addattrobj("segment", "COORD");
		startSegment5.setattrvalue("C1", "484000.000");
		startSegment5.setattrvalue("C2", "70000.000");
		IomObject endSegment5=segments5.addattrobj("segment", "COORD");
		endSegment5.setattrvalue("C1", "484000.000");
		endSegment5.setattrvalue("C2", "72500.000");
		// polyline 2
		IomObject polylineValue4 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments4=polylineValue4.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment4=segments4.addattrobj("segment", "COORD");
		startSegment4.setattrvalue("C1", "484000.000");
		startSegment4.setattrvalue("C2", "72500.000");
		IomObject endSegment4=segments4.addattrobj("segment", "COORD");
		endSegment4.setattrvalue("C1", "488000.000");
		endSegment4.setattrvalue("C2", "70500.000");
		// polyline 3
		IomObject polylineValue6 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments6=polylineValue6.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment6=segments6.addattrobj("segment", "COORD");
		startSegment6.setattrvalue("C1", "488000.000");
		startSegment6.setattrvalue("C2", "70500.000");
		IomObject endSegment6=segments6.addattrobj("segment", "COORD");
		endSegment6.setattrvalue("C1", "484000.000");
		endSegment6.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new ObjectEvent(objSurfaceSuccess2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("Mandatory Constraint Constraint2 is not true.", logger.getErrs().get(0).getEventMsg());
		assertEquals("Mandatory Constraint Constraint2 is not true.", logger.getErrs().get(1).getEventMsg());
	}
	
	// 2 objects == first mandatory: class in Objects = fail
	@Test
	public void areArea_FirstClassOfObjectsNotValid_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSZE, OBJ_OID1);
		// Geometrie 1
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("Geometrie", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "483000.000");
		endSegment.setattrvalue("C2", "70000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "483000.000");
		startSegment2.setattrvalue("C2", "70000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "480000.000");
		endSegment2.setattrvalue("C2", "73000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "480000.000");
		startSegment3.setattrvalue("C2", "73000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// Geometrie 2
		Iom_jObject objSurfaceSuccess2=new Iom_jObject(ILI_CLASSZE, OBJ_OID2);
		IomObject multisurfaceValue2=objSurfaceSuccess2.addattrobj("Geometrie", "MULTISURFACE");
		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue5 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments5=polylineValue5.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment5=segments5.addattrobj("segment", "COORD");
		startSegment5.setattrvalue("C1", "484000.000");
		startSegment5.setattrvalue("C2", "70000.000");
		IomObject endSegment5=segments5.addattrobj("segment", "COORD");
		endSegment5.setattrvalue("C1", "484000.000");
		endSegment5.setattrvalue("C2", "72500.000");
		// polyline 2
		IomObject polylineValue4 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments4=polylineValue4.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment4=segments4.addattrobj("segment", "COORD");
		startSegment4.setattrvalue("C1", "484000.000");
		startSegment4.setattrvalue("C2", "72500.000");
		IomObject endSegment4=segments4.addattrobj("segment", "COORD");
		endSegment4.setattrvalue("C1", "488000.000");
		endSegment4.setattrvalue("C2", "70500.000");
		// polyline 3
		IomObject polylineValue6 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments6=polylineValue6.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment6=segments6.addattrobj("segment", "COORD");
		startSegment6.setattrvalue("C1", "488000.000");
		startSegment6.setattrvalue("C2", "70500.000");
		IomObject endSegment6=segments6.addattrobj("segment", "COORD");
		endSegment6.setattrvalue("C1", "484000.000");
		endSegment6.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new ObjectEvent(objSurfaceSuccess2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(1).getEventMsg());
	}
	
	// 1 object. Objects=ALL, SurfaceBAG=BAG count, SurfaceAttr=surface of other structure
	@Test
	public void areArea_SurfaceBAGAttrNameInExists_Fail(){
		Iom_jObject iomObjStruct=new Iom_jObject(ILI_STRUCTF, null);
		// Geometrie 1
		IomObject multisurfaceValue=iomObjStruct.addattrobj("Surface", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "483000.000");
		endSegment.setattrvalue("C2", "70000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "483000.000");
		startSegment2.setattrvalue("C2", "70000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "480000.000");
		endSegment2.setattrvalue("C2", "73000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "480000.000");
		startSegment3.setattrvalue("C2", "73000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSZF, OBJ_OID1);
		objSurfaceSuccess.addattrobj("Illegal", iomObjStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjStruct));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("Attribute Numbers has wrong number of values", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void function_notYetImplemented_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSZH, OBJ_OID1);
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
		assertEquals("Function is not yet implemented.", logger.getErrs().get(0).getEventMsg());
	}
}