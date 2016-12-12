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

public class MandatoryConstraints23 {

	private TransferDescription td=null;
	
	// OID
	private final static String OBJ_OID1 ="o1";
	// MODEL.TOPIC
	private final static String ILI_TOPIC="MandatoryConstraints23.Topic";
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
	
	private final static String ILI_CLASSAF=ILI_TOPIC+".ClassAF";
	private final static String ILI_CLASSBF=ILI_TOPIC+".ClassBF";
	private final static String ILI_CLASSCF=ILI_TOPIC+".ClassCF";
	private final static String ILI_CLASSDF=ILI_TOPIC+".ClassDF";
	private final static String ILI_CLASSEF=ILI_TOPIC+".ClassEF";
	private final static String ILI_CLASSFF=ILI_TOPIC+".ClassFF";
	private final static String ILI_CLASSGF=ILI_TOPIC+".ClassGF";
	private final static String ILI_CLASSHF=ILI_TOPIC+".ClassHF";
	private final static String ILI_CLASSIF=ILI_TOPIC+".ClassIF";
	
	// START BASKET EVENT
	private final static String START_BASKET_EVENT="b1";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/MandatoryConstraints23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}

	//#########################################################//
	//######## SUCCESS MANDATORY CONSTRAINTS ##################//
	//#########################################################//	
	
	// test with simple constant
	@Test
	public void mandatoryConstraintConstOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in negation (NOT)
	@Test
	public void mandatoryConstraintNegationOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in attr boolean
	@Test
	public void mandatoryConstraintBooleanOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSC, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "true");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in defined
	@Test
	public void mandatoryConstraintDefinedOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSD, OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in conjunction (AND)
	@Test
	public void mandatoryConstraintANDOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSE, OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in disjunction (OR)
	@Test
	public void mandatoryConstraintOROk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSF, OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in equal (==).
	@Test
	public void mandatoryConstraintEqualOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSG, OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in not equal (!=).
//	@Test
//	public void mandatoryConstraintNotEqualOk(){
//		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSH, OBJ_OID1);
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
//		validator.validate(new ObjectEvent(iomObjA));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==0);
//	}
	
//	// test in not equal (<>).
//	@Test
//	public void mandatoryConstraintNotEqual2Ok(){
//		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSI, OBJ_OID1);
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
//		validator.validate(new ObjectEvent(iomObjA));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==0);
//	}
	
	//#########################################################//
	//########### FAIL MANDATORY CONSTRAINTS ##################//
	//#########################################################//
	
//	// test with simple constant
//	@Test
//	public void mandatoryConstraintConstFail(){
//		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSAF, OBJ_OID1);
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
//		validator.validate(new ObjectEvent(iomObjA));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==1);
//		assertEquals("Mandatory Constraint null is not true.", logger.getErrs().get(0).getEventMsg());
//	}
//	
//	// test in negation (NOT)
//	@Test
//	public void mandatoryConstraintNegationFail(){
//		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSBF, OBJ_OID1);
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
//		validator.validate(new ObjectEvent(iomObjA));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==1);
//		assertEquals("Mandatory Constraint null is not true.", logger.getErrs().get(0).getEventMsg());
//	}
//	
//	// test in attr boolean
//	@Test
//	public void mandatoryConstraintBooleanFail(){
//		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCF, OBJ_OID1);
//		iomObjA.setattrvalue("attr1", "false");
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
//		validator.validate(new ObjectEvent(iomObjA));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==1);
//		assertEquals("Mandatory Constraint null is not true.", logger.getErrs().get(0).getEventMsg());
//	}
//	
//	// test in conjunction (AND)
//	@Test
//	public void mandatoryConstraintANDFail(){
//		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEF, OBJ_OID1);
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
//		validator.validate(new ObjectEvent(iomObjA));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==1);
//		assertEquals("Mandatory Constraint null is not true.", logger.getErrs().get(0).getEventMsg());
//	}
//	
//	// test in disjunction (OR)
//	@Test
//	public void mandatoryConstraintORFail(){
//		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSFF, OBJ_OID1);
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
//		validator.validate(new ObjectEvent(iomObjA));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==1);
//		assertEquals("Mandatory Constraint null is not true.", logger.getErrs().get(0).getEventMsg());
//	}
	
	// test in equal (==).
//	@Test
//	public void mandatoryConstraintEqualFail(){
//		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGF, OBJ_OID1);
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
//		validator.validate(new ObjectEvent(iomObjA));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==1);
//		assertEquals("Mandatory Constraint null is not true.", logger.getErrs().get(0).getEventMsg());
//	}
	
	// test in not equal (!=).
//	@Test
//	public void mandatoryConstraintNotEqualFail(){
//		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSHF, OBJ_OID1);
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
//		validator.validate(new ObjectEvent(iomObjA));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==1);
//		assertEquals("Mandatory Constraint null is not true.", logger.getErrs().get(0).getEventMsg());
//	}
//	
//	// test in not equal (<>).
//	@Test
//	public void mandatoryConstraintNotEqual2Fail(){
//		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSIF, OBJ_OID1);
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
//		validator.validate(new ObjectEvent(iomObjA));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==1);
//		assertEquals("Mandatory Constraint null is not true.", logger.getErrs().get(0).getEventMsg());
//	}
}