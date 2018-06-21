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
	private final static String OID1 ="o1";
	private final static String OID2 ="o2";
	private final static String OID3 ="o3";
	private final static String OID4 ="o4";
	private final static String OID5 ="o5";
	private final static String TOPIC="PlausibilityConstraint23.Topic";
	private final static String ILI_CLASSA=TOPIC+".ClassA";
	private final static String ILI_CLASSB=TOPIC+".ClassB";
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

	// Es soll keine Fehlermeldung ausgegeben werden.
	// Gueltige Objekte: 80%.
	// Gueltigkeitsbereich: 60%-100%.
	@Test
	public void percentageGreaterThanMinScope_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OID1);
		iomObjA.setattrvalue("attr1", "7");
		iomObjA.setattrvalue("attr2", "5");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSA, OID2);
		iomObjB.setattrvalue("attr1", "7");
		iomObjB.setattrvalue("attr2", "5");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSA, OID3);
		iomObjC.setattrvalue("attr1", "7");
		iomObjC.setattrvalue("attr2", "5");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSA, OID4);
		iomObjD.setattrvalue("attr1", "7");
		iomObjD.setattrvalue("attr2", "5");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSA, OID5);
		iomObjE.setattrvalue("attr1", "5");
		iomObjE.setattrvalue("attr2", "7");
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
	
	// Eine Fehlermeldung soll ausgegeben werden.
	// Gueltige Objekte: 40%.
	// Gueltigkeitsbereich: 60%-100%.
	@Test
	public void percentageLessThanMinScope_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OID1);
		iomObjA.setattrvalue("attr1", "7");
		iomObjA.setattrvalue("attr2", "5");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSA, OID2);
		iomObjB.setattrvalue("attr1", "7");
		iomObjB.setattrvalue("attr2", "5");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSA, OID3);
		iomObjC.setattrvalue("attr1", "5");
		iomObjC.setattrvalue("attr2", "7");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSA, OID4);
		iomObjD.setattrvalue("attr1", "5");
		iomObjD.setattrvalue("attr2", "7");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSA, OID5);
		iomObjE.setattrvalue("attr1", "5");
		iomObjE.setattrvalue("attr2", "7");
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
		assertEquals("Plausibility Constraint PlausibilityConstraint23.Topic.ClassA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll keine Fehlermeldung ausgegeben werden.
	// Gueltige Objekte: 40%.
	// Gueltigkeitsbereich: 0%-60%.
	@Test
	public void percentageLessThanMaxScope_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSB, OID1);
		iomObjA.setattrvalue("attr1", "7");
		iomObjA.setattrvalue("attr2", "5");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OID2);
		iomObjB.setattrvalue("attr1", "7");
		iomObjB.setattrvalue("attr2", "5");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSB, OID3);
		iomObjC.setattrvalue("attr1", "5");
		iomObjC.setattrvalue("attr2", "7");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSB, OID4);
		iomObjD.setattrvalue("attr1", "5");
		iomObjD.setattrvalue("attr2", "7");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSB, OID5);
		iomObjE.setattrvalue("attr1", "5");
		iomObjE.setattrvalue("attr2", "7");
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
	
	// Eine Fehlermeldung soll ausgegeben werden.
	// Gueltige Objekte: 80%.
	// Gueltigkeitsbereich: 0%-60%.
	@Test
	public void percentageGreaterThanMaxScope_Fail(){
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Plausibility Constraint PlausibilityConstraint23.Topic.ClassB.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Prueft die Konfiguration: constraint validation.
	// Die Konfiguration ist nicht gesetzt.
	// Es wird eine Fehlermeldung erwartet.
	@Test
	public void percentageLessThanMinScope_ConstraintDisableSet_NotSet_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OID1);
		iomObjA.setattrvalue("attr1", "7");
		iomObjA.setattrvalue("attr2", "5");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSA, OID2);
		iomObjB.setattrvalue("attr1", "7");
		iomObjB.setattrvalue("attr2", "5");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSA, OID3);
		iomObjC.setattrvalue("attr1", "5");
		iomObjC.setattrvalue("attr2", "7");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSA, OID4);
		iomObjD.setattrvalue("attr1", "5");
		iomObjD.setattrvalue("attr2", "7");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSA, OID5);
		iomObjE.setattrvalue("attr1", "5");
		iomObjE.setattrvalue("attr2", "7");
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
		assertEquals("Plausibility Constraint PlausibilityConstraint23.Topic.ClassA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Prueft die Konfiguration: constraint validation.
	// Die Konfiguration ist Eingeschaltet.
	// Es wird eine Fehlermeldung erwartet.
	@Test
	public void percentageLessThanMinScope_ConstraintDisableSet_ON_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OID1);
		iomObjA.setattrvalue("attr1", "7");
		iomObjA.setattrvalue("attr2", "5");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSA, OID2);
		iomObjB.setattrvalue("attr1", "7");
		iomObjB.setattrvalue("attr2", "5");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSA, OID3);
		iomObjC.setattrvalue("attr1", "5");
		iomObjC.setattrvalue("attr2", "7");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSA, OID4);
		iomObjD.setattrvalue("attr1", "5");
		iomObjD.setattrvalue("attr2", "7");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSA, OID5);
		iomObjE.setattrvalue("attr1", "5");
		iomObjE.setattrvalue("attr2", "7");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.CONSTRAINT_VALIDATION, ValidationConfig.ON);
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
		assertEquals("Plausibility Constraint PlausibilityConstraint23.Topic.ClassA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Prueft die Konfiguration: constraint validation.
	// Die Konfiguration ist Ausgeschaltet.
	// Es wird erwartet dass keine Fehlermeldung ausgegeben wird.
	@Test
	public void percentageLessThanMinScope_ConstraintDisableSet_OFF_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OID1);
		iomObjA.setattrvalue("attr1", "7");
		iomObjA.setattrvalue("attr2", "5");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSA, OID2);
		iomObjB.setattrvalue("attr1", "7");
		iomObjB.setattrvalue("attr2", "5");
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSA, OID3);
		iomObjC.setattrvalue("attr1", "5");
		iomObjC.setattrvalue("attr2", "7");
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSA, OID4);
		iomObjD.setattrvalue("attr1", "5");
		iomObjD.setattrvalue("attr2", "7");
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSA, OID5);
		iomObjE.setattrvalue("attr1", "5");
		iomObjE.setattrvalue("attr2", "7");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.CONSTRAINT_VALIDATION, ValidationConfig.OFF);
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
}