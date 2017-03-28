package ch.interlis.iox_j.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

public class PlausibilityConstraints23Test {
	private TransferDescription td=null;
	// OID
	private final static String OID1 ="o1";
	private final static String OID2 ="o2";
	private final static String OID3 ="o3";
	private final static String OID4 ="o4";
	private final static String OID5 ="o5";
	// MODEL.TOPIC
	private final static String TOPIC="PlausibilityConstraint23.Topic";
	// CONSTANT SUCCESS
	private final static String ILI_CLASSA=TOPIC+".ClassA";
	private final static String ILI_CLASSB=TOPIC+".ClassB";
	private final static String ILI_CLASSC=TOPIC+".ClassC";
	private final static String ILI_CLASSD=TOPIC+".ClassD";
	private final static String ILI_CLASSE=TOPIC+".ClassE";
	private final static String ILI_CLASSF=TOPIC+".ClassF";
	private final static String ILI_CLASSG=TOPIC+".ClassG";
	// START BASKET EVENT
	private final static String BID="b1";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/PlausibilityConstraint23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}

	//#########################################################//
	//######## SUCCESS PLAUSIBILITY CONSTRAINTS ###############//
	//#########################################################//	
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil bei 100% liegt und >= 50% erreicht werden muss.
	@Test
	public void targetResultEqualsPercentage_1object_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OID1);
		iomObjA.setattrvalue("attr1", "7");
		iomObjA.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil bei 80% liegt und >= 60% erreicht werden muss.
	@Test
	public void targetResultGreaterThanPercentage_5objects_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSB, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OID2);
		iomObjB.setattrvalue("attr1", "7");
		iomObjB.setattrvalue("attr2", "5");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSB, OID3);
		iomObjC.setattrvalue("attr1", "7");
		iomObjC.setattrvalue("attr2", "5");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSB, OID4);
		iomObjD.setattrvalue("attr1", "7");
		iomObjD.setattrvalue("attr2", "5");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSB, OID5);
		iomObjE.setattrvalue("attr1", "7");
		iomObjE.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjC));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil bei 60% liegt und >= 60% erreicht werden muss.
	@Test
	public void targetResultEqualsPercentage_5objects_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSB, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OID2);
		iomObjB.setattrvalue("attr1", "5");
		iomObjB.setattrvalue("attr2", "7");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSB, OID3);
		iomObjC.setattrvalue("attr1", "7");
		iomObjC.setattrvalue("attr2", "5");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSB, OID4);
		iomObjD.setattrvalue("attr1", "7");
		iomObjD.setattrvalue("attr2", "5");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSB, OID5);
		iomObjE.setattrvalue("attr1", "7");
		iomObjE.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjC));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil des
	// Constraint1 >= 80% liegt und der
	// Constraint2 >= 60% liegt und dabei
	// die prozentuale Richtigkeit bei 80% liegt.
	@Test
	public void resultsInValidRange_5objects_2Constraints_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSC, OID1);
		iomObjA.setattrvalue("attr1", "7");
		iomObjA.setattrvalue("attr2", "5");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSC, OID2);
		iomObjB.setattrvalue("attr1", "7");
		iomObjB.setattrvalue("attr2", "5");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSC, OID3);
		iomObjC.setattrvalue("attr1", "7");
		iomObjC.setattrvalue("attr2", "5");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSC, OID4);
		iomObjD.setattrvalue("attr1", "5");
		iomObjD.setattrvalue("attr2", "7");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSC, OID5);
		iomObjE.setattrvalue("attr1", "7");
		iomObjE.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjC));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil des
	// Constraint1 >= 80% liegt und der
	// Constraint2 >= 60% liegt und dabei
	// die prozentuale Richtigkeit bei 80% liegt.
	@Test
	public void firstEqualsSecondGreater_5objects_2Constraints_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSC, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSC, OID2);
		iomObjB.setattrvalue("attr1", "7");
		iomObjB.setattrvalue("attr2", "5");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSC, OID3);
		iomObjC.setattrvalue("attr1", "7");
		iomObjC.setattrvalue("attr2", "5");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSC, OID4);
		iomObjD.setattrvalue("attr1", "7");
		iomObjD.setattrvalue("attr2", "5");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSC, OID5);
		iomObjE.setattrvalue("attr1", "7");
		iomObjE.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjC));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil bei 60% liegt und <= 60% erreicht werden muss.
	@Test
	public void targetResultEqualsPercentage_5objects_LessThanTest_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSE, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSE, OID2);
		iomObjB.setattrvalue("attr1", "5");
		iomObjB.setattrvalue("attr2", "7");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSE, OID3);
		iomObjC.setattrvalue("attr1", "5");
		iomObjC.setattrvalue("attr2", "7");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSE, OID4);
		iomObjD.setattrvalue("attr1", "7");
		iomObjD.setattrvalue("attr2", "5");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSE, OID5);
		iomObjE.setattrvalue("attr1", "7");
		iomObjE.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjC));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil bei 0% liegt und <= 50% erreicht werden muss.
	@Test
	public void targetResultLessThanPercentage_1object_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSD, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil bei 40% liegt und <= 60% erreicht werden muss.
	@Test
	public void targetResultLessThanPercentage_5objects_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSE, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSE, OID2);
		iomObjB.setattrvalue("attr1", "5");
		iomObjB.setattrvalue("attr2", "7");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSE, OID3);
		iomObjC.setattrvalue("attr1", "5");
		iomObjC.setattrvalue("attr2", "7");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSE, OID4);
		iomObjD.setattrvalue("attr1", "7");
		iomObjD.setattrvalue("attr2", "5");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSE, OID5);
		iomObjE.setattrvalue("attr1", "7");
		iomObjE.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjC));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil des
	// Constraint1 <= 80% liegt und der
	// Constraint2 <= 60% liegt und dabei
	// die prozentuale Richtigkeit bei 60% liegt.
	@Test
	public void firstLessSecondEquals_5objects_2Constraints_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSF, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSF, OID2);
		iomObjB.setattrvalue("attr1", "5");
		iomObjB.setattrvalue("attr2", "7");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSF, OID3);
		iomObjC.setattrvalue("attr1", "7");
		iomObjC.setattrvalue("attr2", "5");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSF, OID4);
		iomObjD.setattrvalue("attr1", "7");
		iomObjD.setattrvalue("attr2", "5");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSF, OID5);
		iomObjE.setattrvalue("attr1", "7");
		iomObjE.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjC));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil des
	// Constraint1 <= 80% liegt und der
	// Constraint2 <= 60% liegt und dabei
	// die prozentuale Richtigkeit bei 40% liegt.
	@Test
	public void bothConstraintsLessThanPercentage_5objects_2Constraints_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSF, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSF, OID2);
		iomObjB.setattrvalue("attr1", "5");
		iomObjB.setattrvalue("attr2", "7");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSF, OID3);
		iomObjC.setattrvalue("attr1", "5");
		iomObjC.setattrvalue("attr2", "7");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSF, OID4);
		iomObjD.setattrvalue("attr1", "7");
		iomObjD.setattrvalue("attr2", "5");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSF, OID5);
		iomObjE.setattrvalue("attr1", "7");
		iomObjE.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjC));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil des
	// Constraint1 >= 80% liegt und der
	// Constraint2 >= 60% liegt und dabei
	// Constraint2 >= 40% liegt und dabei
	// Constraint2 >= 20% liegt und dabei
	// die prozentuale Richtigkeit bei 100% liegt.
	@Test
	public void targetResultLessThanPercentage_4Objects_4Constraints_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSG, OID1);
		iomObjA.setattrvalue("attr1", "10");
		iomObjA.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	//#########################################################//
	//########### FAIL PLAUSIBILITY CONSTRAINTS ##################//
	//#########################################################//
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil bei 0% liegt und >= 50% erreicht werden muss.
	@Test
	public void targetResultLessThanPercentage_1object_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Plausibility Constraint PlausibilityConstraint23.Topic.ClassA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil bei 40% liegt und >= 60% erreicht werden muss.
	@Test
	public void targetResultLessThanPercentage_5objects_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSB, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OID2);
		iomObjB.setattrvalue("attr1", "5");
		iomObjB.setattrvalue("attr2", "7");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSB, OID3);
		iomObjC.setattrvalue("attr1", "5");
		iomObjC.setattrvalue("attr2", "7");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSB, OID4);
		iomObjD.setattrvalue("attr1", "7");
		iomObjD.setattrvalue("attr2", "5");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSB, OID5);
		iomObjE.setattrvalue("attr1", "7");
		iomObjE.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjC));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Plausibility Constraint PlausibilityConstraint23.Topic.ClassB.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil des
	// Constraint1 >= 80% liegt und der
	// Constraint2 >= 60% liegt und dabei
	// die prozentuale Richtigkeit bei 60% liegt.
	@Test
	public void firstLessSecondEquals_5objects_2Constraints_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSC, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSC, OID2);
		iomObjB.setattrvalue("attr1", "5");
		iomObjB.setattrvalue("attr2", "7");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSC, OID3);
		iomObjC.setattrvalue("attr1", "7");
		iomObjC.setattrvalue("attr2", "5");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSC, OID4);
		iomObjD.setattrvalue("attr1", "7");
		iomObjD.setattrvalue("attr2", "5");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSC, OID5);
		iomObjE.setattrvalue("attr1", "7");
		iomObjE.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjC));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Plausibility Constraint PlausibilityConstraint23.Topic.ClassC.Constraint2 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil des
	// Constraint1 >= 80% liegt und der
	// Constraint2 >= 60% liegt und dabei
	// die prozentuale Richtigkeit bei 40% liegt.
	@Test
	public void bothConstraintsLessThanPercentage_5objects_2Constraints_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSC, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSC, OID2);
		iomObjB.setattrvalue("attr1", "5");
		iomObjB.setattrvalue("attr2", "7");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSC, OID3);
		iomObjC.setattrvalue("attr1", "5");
		iomObjC.setattrvalue("attr2", "7");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSC, OID4);
		iomObjD.setattrvalue("attr1", "7");
		iomObjD.setattrvalue("attr2", "5");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSC, OID5);
		iomObjE.setattrvalue("attr1", "7");
		iomObjE.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjC));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("Plausibility Constraint PlausibilityConstraint23.Topic.ClassC.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
		assertEquals("Plausibility Constraint PlausibilityConstraint23.Topic.ClassC.Constraint2 is not true.", logger.getErrs().get(1).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil bei 100% liegt und <= 50% erreicht werden muss.
	@Test
	public void targetResultEqualsPercentage_1object_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSD, OID1);
		iomObjA.setattrvalue("attr1", "7");
		iomObjA.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Plausibility Constraint PlausibilityConstraint23.Topic.ClassD.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil bei 80% liegt und <= 60% erreicht werden muss.
	@Test
	public void targetResultGreaterThanPercentage_5objects_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSE, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSE, OID2);
		iomObjB.setattrvalue("attr1", "7");
		iomObjB.setattrvalue("attr2", "5");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSE, OID3);
		iomObjC.setattrvalue("attr1", "7");
		iomObjC.setattrvalue("attr2", "5");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSE, OID4);
		iomObjD.setattrvalue("attr1", "7");
		iomObjD.setattrvalue("attr2", "5");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSE, OID5);
		iomObjE.setattrvalue("attr1", "7");
		iomObjE.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjC));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Plausibility Constraint PlausibilityConstraint23.Topic.ClassE.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil des
	// Constraint1 <= 80% liegt und der
		// Constraint2 <= 60% liegt und dabei
		// die prozentuale Richtigkeit bei 80% liegt.
	@Test
	public void resultsInValidRange_5objects_2Constraints_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSF, OID1);
		iomObjA.setattrvalue("attr1", "7");
		iomObjA.setattrvalue("attr2", "5");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSF, OID2);
		iomObjB.setattrvalue("attr1", "7");
		iomObjB.setattrvalue("attr2", "5");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSF, OID3);
		iomObjC.setattrvalue("attr1", "7");
		iomObjC.setattrvalue("attr2", "5");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSF, OID4);
		iomObjD.setattrvalue("attr1", "5");
		iomObjD.setattrvalue("attr2", "7");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSF, OID5);
		iomObjE.setattrvalue("attr1", "7");
		iomObjE.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjC));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Plausibility Constraint PlausibilityConstraint23.Topic.ClassF.Constraint2 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil des
	// Constraint1 <= 80% liegt und der
	// Constraint2 <= 60% liegt und dabei
	// die prozentuale Richtigkeit bei 100% liegt.
	@Test
	public void firstEqualsSecondGreater_5objects_2Constraints_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSF, OID1);
		iomObjA.setattrvalue("attr1", "7");
		iomObjA.setattrvalue("attr2", "5");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSF, OID2);
		iomObjB.setattrvalue("attr1", "7");
		iomObjB.setattrvalue("attr2", "5");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSF, OID3);
		iomObjC.setattrvalue("attr1", "7");
		iomObjC.setattrvalue("attr2", "5");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSF, OID4);
		iomObjD.setattrvalue("attr1", "7");
		iomObjD.setattrvalue("attr2", "5");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSF, OID5);
		iomObjE.setattrvalue("attr1", "7");
		iomObjE.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjC));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("Plausibility Constraint PlausibilityConstraint23.Topic.ClassF.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
		assertEquals("Plausibility Constraint PlausibilityConstraint23.Topic.ClassF.Constraint2 is not true.", logger.getErrs().get(1).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil des
	// Constraint1 >= 80% liegt und der
	// Constraint2 >= 60% liegt und dabei
	// Constraint2 >= 40% liegt und dabei
	// Constraint2 >= 20% liegt und dabei
	// die prozentuale Richtigkeit bei 0% liegt.
	@Test
	public void targetResultLessThanPercentage_4Objects_4Constraints_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSG, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "10");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==4);
		assertEquals("Plausibility Constraint PlausibilityConstraint23.Topic.ClassG.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
		assertEquals("Plausibility Constraint PlausibilityConstraint23.Topic.ClassG.Constraint2 is not true.", logger.getErrs().get(1).getEventMsg());
		assertEquals("Plausibility Constraint PlausibilityConstraint23.Topic.ClassG.Constraint3 is not true.", logger.getErrs().get(2).getEventMsg());
		assertEquals("Plausibility Constraint PlausibilityConstraint23.Topic.ClassG.Constraint4 is not true.", logger.getErrs().get(3).getEventMsg());
	}
}
