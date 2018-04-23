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

public class SetConstraint23Test {
	
	private static final String TEST_OUT="src/test/data/validator/";
	private TransferDescription td=null;
	// oid
	private final static String OID1 ="o1";
	private final static String OID2 ="o2";
	private final static String OID3 ="o3";
	private final static String OID4 ="o4";
	// bid
	private final static String BID1="b1";
	// topicA
	private final static String TOPICA="SetConstraint23.TopicA";
	private static final String TOPICA_CLASS1=TOPICA+".Class1";
	private final static String TOPICA_CLASS2=TOPICA+".Class2";
	// association
	private final static String TOPICA_ASSOC_ASSOC1=TOPICA+".assoc1";
	private final static String TOPICA_ASSOC_ASSOC1_R1="r1";
	private final static String TOPICA_ASSOC_ASSOC1_R2="r2";
	// topicB
	private final static String TOPICB="SetConstraint23.TopicB";
	private static final String TOPICB_CLASS1=TOPICB+".Class1";
		
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"SetConstraint23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}

	// prueft ob die setConstraint Bedingung erfuellt wird,
	// wenn die Anzahl der erstellten Objekte, mit der Anzahl der Bedingung uebereinstimmt. 
	@Test
	public void objectCount_IsEqualToConditionCount_Ok(){
		Iom_jObject iomObj1=new Iom_jObject(TOPICA_CLASS1, OID1);
		Iom_jObject iomObj2=new Iom_jObject(TOPICA_CLASS2, OID3);
		// association 1
		Iom_jObject iomObjAssoc1=new Iom_jObject(TOPICA_ASSOC_ASSOC1, null);
		iomObjAssoc1.addattrobj(TOPICA_ASSOC_ASSOC1_R1, "REF").setobjectrefoid(OID1);
		iomObjAssoc1.addattrobj(TOPICA_ASSOC_ASSOC1_R2, "REF").setobjectrefoid(OID3);
		Iom_jObject iomObj3=new Iom_jObject(TOPICA_CLASS1, OID2);
		Iom_jObject iomObj4=new Iom_jObject(TOPICA_CLASS2, OID4);
		// association 2
		Iom_jObject iomObjAssoc2=new Iom_jObject(TOPICA_ASSOC_ASSOC1, null);
		iomObjAssoc2.addattrobj(TOPICA_ASSOC_ASSOC1_R1, "REF").setobjectrefoid(OID2);
		iomObjAssoc2.addattrobj(TOPICA_ASSOC_ASSOC1_R2, "REF").setobjectrefoid(OID4);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPICA,BID1));
		validator.validate(new ObjectEvent(iomObj1));
		validator.validate(new ObjectEvent(iomObj2));
		validator.validate(new ObjectEvent(iomObjAssoc1));
		validator.validate(new ObjectEvent(iomObj3));
		validator.validate(new ObjectEvent(iomObj4));
		validator.validate(new ObjectEvent(iomObjAssoc2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		assertTrue(logger.getErrs().size()==0);
	}
	
	// prueft ob die setConstraint Bedingung eine Fehlermeldung ausgibt,
	// wenn die Anzahl der erstellten Objekte zu klein ist, um die Bedingung zu erfuellen.
	@Test
	public void objectCount_IsLessThanConditionCount_Fail(){
		Iom_jObject iomObj1=new Iom_jObject(TOPICA_CLASS1, OID1);
		Iom_jObject iomObj2=new Iom_jObject(TOPICA_CLASS2, OID2);
		// association 1
		Iom_jObject iomObjAssoc1=new Iom_jObject(TOPICA_ASSOC_ASSOC1, null);
		iomObjAssoc1.addattrobj(TOPICA_ASSOC_ASSOC1_R1, "REF").setobjectrefoid(OID1);
		iomObjAssoc1.addattrobj(TOPICA_ASSOC_ASSOC1_R2, "REF").setobjectrefoid(OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPICA,BID1));
		validator.validate(new ObjectEvent(iomObj1));
		validator.validate(new ObjectEvent(iomObj2));
		validator.validate(new ObjectEvent(iomObjAssoc1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Set Constraint SetConstraint23.TopicA.Class1.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// prueft ob die setConstraint Bedingung mit einer WHERE Klausel erstellt werde kann,
	// wenn die Anzahl der erstellten Objekte, mit der Anzahl der Bedingung uebereinstimmt
	// und die PreCondition erfuellt wird.
	@Test
	public void countOfValidPreConditionObjs_ValidToSecondCondition_Fail(){
		Iom_jObject iomObj1=new Iom_jObject(TOPICB_CLASS1, OID1);
		iomObj1.setattrvalue("attr1", "a");
		Iom_jObject iomObj2=new Iom_jObject(TOPICB_CLASS1, OID2);
		iomObj2.setattrvalue("attr1", "a");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPICB,BID1));
		validator.validate(new ObjectEvent(iomObj1));
		validator.validate(new ObjectEvent(iomObj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		assertTrue(logger.getErrs().size()==0);
	}

	// prueft ob die setConstraint Bedingung mit der WHERE Klausel eine Fehlermeldung ausgibt,
	// wenn die Anzahl der Objekte welche die PreCondition erfuellen, nicht der Anzahl der
	// zweiten Bedingung entspricht.
	@Test
	public void countOfValidPreConditionObjs_InvalidToSecondCondition_Fail(){
		Iom_jObject iomObj1=new Iom_jObject(TOPICB_CLASS1, OID1);
		iomObj1.setattrvalue("attr1", "a");
		Iom_jObject iomObj2=new Iom_jObject(TOPICB_CLASS1, OID2);
		iomObj2.setattrvalue("attr1", "b");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPICB,BID1));
		validator.validate(new ObjectEvent(iomObj1));
		validator.validate(new ObjectEvent(iomObj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Set Constraint SetConstraint23.TopicB.Class1.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
}