package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

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

public class FunctionsExt23Test {
	private TransferDescription td=null;
	// OID
	private final static String OBJ_OID1 ="o1";
	// MODEL
	private final static String ILI_TOPIC="FunctionsExt23.Topic";
	// CLASS
	private final static String ILI_CLASSA=ILI_TOPIC+".ClassA";
	private final static String ILI_CLASSB=ILI_TOPIC+".ClassB";
	// START BASKET EVENT
	private final static String BID1="b1";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/FunctionsExt23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}

	//#########################################################//
	//######## SUCCESS FUNCTIONS ##############################//
	//#########################################################//	
	
	// testet eine Benutzerdefinierte Funktion mit einfachen (String, Zahlen) Argumenten
	@Test
	public void simpleArguments_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		iomObjA.setattrvalue("text", "0123456789");
		iomObjA.setattrvalue("from", "2");
		iomObjA.setattrvalue("to", "8");
		iomObjA.setattrvalue("attr2", "234567");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.mergeIliMetaAttrs(td);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Map<String,Class> newFunctions=new HashMap<String,Class>();
		newFunctions.put("FunctionsExt23.subText",SubText.class);
		settings.setTransientObject(Validator.CONFIG_CUSTOM_FUNCTIONS, newFunctions);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// testet eine Benutzerdefinierte Funktion mit Strukturen als Argument
	@Test
	public void structureArguments_Ok(){
		Iom_jObject iomObjM=new Iom_jObject("FunctionsExt23.Topic.StructA", null);
		Iom_jObject iomObjM2=new Iom_jObject("FunctionsExt23.Topic.StructA", null);
		Iom_jObject iomObjM3=new Iom_jObject("FunctionsExt23.Topic.StructA", null);
		Iom_jObject iomObjN=new Iom_jObject("FunctionsExt23.Topic.ClassD", "o2");
		iomObjN.setattrvalue("attr2", "true");
		iomObjN.addattrobj("attr3", iomObjM);
		iomObjN.addattrobj("attr3", iomObjM2);
		iomObjN.addattrobj("attr3", iomObjM3);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.mergeIliMetaAttrs(td);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Map<String,Class> newFunctions=new HashMap<String,Class>();
		newFunctions.put("FunctionsExt23.elementCount",ElementCount.class);
		settings.setTransientObject(Validator.CONFIG_CUSTOM_FUNCTIONS, newFunctions);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjM));
		validator.validate(new ObjectEvent(iomObjM2));
		validator.validate(new ObjectEvent(iomObjM3));
		validator.validate(new ObjectEvent(iomObjN));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	//#########################################################//
	//######## FAILING FUNCTIONS ##############################//
	//#########################################################//	
	
	// testet ob der Rueckgabewert der Benutzedefinierten Funktion ausgewertet wird
	@Test
	public void returnsFalse_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		iomObjA.setattrvalue("text", "123456789");
		iomObjA.setattrvalue("from", "2");
		iomObjA.setattrvalue("to", "8");
		iomObjA.setattrvalue("attr2", "234567");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.mergeIliMetaAttrs(td);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Map<String,Class> newFunctions=new HashMap<String,Class>();
		newFunctions.put("FunctionsExt23.subText",SubText.class);
		settings.setTransientObject(Validator.CONFIG_CUSTOM_FUNCTIONS, newFunctions);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint FunctionsExt23.Topic.ClassA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// testet eine Funktion, die im ili-Modell definiert wurde, aber keine Implementierung hat.
	@Test
	public void noImplmenetation_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
		iomObjA.setattrvalue("text", "0123456789");
		iomObjA.setattrvalue("from", "2");
		iomObjA.setattrvalue("to", "8");
		iomObjA.setattrvalue("attr2", "234567");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.mergeIliMetaAttrs(td);
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
		assertTrue(logger.getWarn().size()==1);
		assertEquals("MandatoryConstraint FunctionsExt23.Topic.ClassB.Constraint1 of FunctionsExt23.Topic.ClassB is not yet implemented.", logger.getWarn().get(0).getEventMsg());
	}
	
}