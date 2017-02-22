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

public class AdditionalConstraints23Test {
	private TransferDescription td=null;
	// OID
	private final static String OBJ_OID1 ="o1";
	private final static String OBJ_OID2 ="o2";
	// MODEL.TOPIC
	private final static String ILI_TOPIC="AdditionalConstraints23.Bodenbedeckung";
	// CLASS
	private final static String ILI_MANDATORYCONSTRAINT_CLASS=ILI_TOPIC+".ManConClass";
	private final static String ILI_MANDATORYCONSTRAINT_COORDCLASS=ILI_TOPIC+".ManConClassCoord";
	private final static String ILI_STRUCTC=ILI_TOPIC+".StructC";
	private final static String ILI_CLASSC=ILI_TOPIC+".ClassC";
	private final static String ILI_EXISTENCECONSTRAINT_CLASS=ILI_TOPIC+".ExConClass";
	private final static String ILI_EXISTENCECONSTRAINT_CONDITION=ILI_TOPIC+".ExConCondition";
	// START BASKET EVENT
	private final static String BID="b1";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/AdditionalConstraints23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}

	//#########################################################//
	//######## SUCCESS ADDITIONAL CONSTRAINTS #################//
	//#########################################################//	
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn in einer VIEW ausserhalb des Models
	// ein MandatoryConstraint true ergibt.
	@Test
	public void mandatoryConstraintTrue_Ok(){
		Iom_jObject obj1=new Iom_jObject(ILI_MANDATORYCONSTRAINT_CLASS, OBJ_OID1);
		obj1.setattrvalue("attr1", "5");
		obj1.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AddManConModel;");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn in einer VIEW ausserhalb des Models
	// ein MandatoryConstraint InEqualation true ergibt.
	@Test
	public void mandatoryConstraint_InEqualationTrue_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_MANDATORYCONSTRAINT_COORDCLASS, OBJ_OID1);
		IomObject coordValue3=iomObjA.addattrobj("attr3", "COORD");
		coordValue3.setattrvalue("C1", "480000.000");
		coordValue3.setattrvalue("C2", "70000.000");
		coordValue3.setattrvalue("C3", "5000.000");
		IomObject coordValue4=iomObjA.addattrobj("attr4", "COORD");
		coordValue4.setattrvalue("C1", "480000.000");
		coordValue4.setattrvalue("C2", "88888.000");
		coordValue4.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AddManConCoordModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn Daten in 2 unterschiedlichen VIEW's ausserhalb des Models
	// in MandatoryConstraints true ergeben.
	@Test
	public void mandatoryConstraint_2ModelsBothTrue_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_MANDATORYCONSTRAINT_COORDCLASS, OBJ_OID1);
		IomObject coordValue3=iomObjA.addattrobj("attr3", "COORD");
		coordValue3.setattrvalue("C1", "480000.000");
		coordValue3.setattrvalue("C2", "70000.000");
		coordValue3.setattrvalue("C3", "5000.000");
		IomObject coordValue4=iomObjA.addattrobj("attr4", "COORD");
		coordValue4.setattrvalue("C1", "480000.000");
		coordValue4.setattrvalue("C2", "88888.000");
		coordValue4.setattrvalue("C3", "5000.000");
		Iom_jObject iomObjB=new Iom_jObject(ILI_MANDATORYCONSTRAINT_CLASS, OBJ_OID2);
		iomObjB.setattrvalue("attr1", "5");
		iomObjB.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AddManConCoordModel;AddManConModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn in einer VIEW ausserhalb des Models
	// ein SetConstraint true ist.
	@Test
	public void setConstraint_BagOfStruct_Ok(){
		Iom_jObject iomObjStruct=new Iom_jObject(ILI_STRUCTC, null);
		Iom_jObject iomObjStruct2=new Iom_jObject(ILI_STRUCTC, null);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSC, OBJ_OID1);
		iomObj.addattrobj("Numbers", iomObjStruct);
		iomObj.addattrobj("Numbers", iomObjStruct2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AdditionalConstraints23Zusatz4");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjStruct));
		validator.validate(new ObjectEvent(iomObjStruct2));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}	
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn in einer VIEW ausserhalb des Models
	// eine existenceContraint true ergibt.
	@Test
	public void existenceConstraintTrue_Ok() throws Exception{
		Iom_jObject conditionObj=new Iom_jObject(ILI_EXISTENCECONSTRAINT_CONDITION, OBJ_OID1);
		conditionObj.setattrvalue("superAttr", "lars");
		Iom_jObject obj1=new Iom_jObject(ILI_EXISTENCECONSTRAINT_CLASS, OBJ_OID2);
		obj1.setattrvalue("subAttr", "lars");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AddExConModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	//#########################################################//
	//########### FAIL ADDITIONAL CONSTRAINTS #################//
	//#########################################################//
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn in einer VIEW ausserhalb des Models
	// ein MandatoryConstraint false ergibt.
	@Test
	public void mandatoryConstraint_NotEqualation_Fail(){
		Iom_jObject obj1=new Iom_jObject(ILI_MANDATORYCONSTRAINT_CLASS, OBJ_OID1);
		obj1.setattrvalue("attr1", "5");
		obj1.setattrvalue("attr2", "10");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AddManConModel;");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint AddManConModel.AddManConTopic.AddManConView.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn in einer VIEW ausserhalb des Models
	// ein MandatoryConstraint false ergibt und validationConfig msg nicht leer ist.
	@Test
	public void mandatoryConstraint_NotEqualation_MSGNotEmpty_Fail(){
		Iom_jObject obj1=new Iom_jObject(ILI_MANDATORYCONSTRAINT_CLASS, OBJ_OID1);
		obj1.setattrvalue("attr1", "5");
		obj1.setattrvalue("attr2", "10");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("AddManConModel.AddManConTopic.AddManConView"+".Constraint1", ValidationConfig.MSG, "This is my own error message!");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AddManConModel;");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("This is my own error message!", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn in einer VIEW ausserhalb des Models
	// ein MandatoryConstraint false ergibt und validationConfig msg leer ist.
	@Test
	public void mandatoryConstraint_NotEqualation_MSGIsEmpty_Fail(){
		Iom_jObject obj1=new Iom_jObject(ILI_MANDATORYCONSTRAINT_CLASS, OBJ_OID1);
		obj1.setattrvalue("attr1", "5");
		obj1.setattrvalue("attr2", "10");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("AddManConModel.AddManConTopic.AddManConView"+".Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AddManConModel;");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint AddManConModel.AddManConTopic.AddManConView.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn die VIEW ausserhalb des Models nicht gefunden wird.
	@Test
	public void mandatoryConstraint_ConfigConstraintModelNameNotExist_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_MANDATORYCONSTRAINT_CLASS, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "10");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AdditionalConstraints23Zusatz99999999");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("required additional model AdditionalConstraints23Zusatz99999999 not found", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn in einer VIEW ausserhalb des Models
	// ein MandatoryConstraint false ergibt.
	@Test
	public void mandatoryConstraint_CoordsAreEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_MANDATORYCONSTRAINT_COORDCLASS, OBJ_OID1);
		IomObject coordValue3=iomObjA.addattrobj("attr3", "COORD");
		coordValue3.setattrvalue("C1", "480000.000");
		coordValue3.setattrvalue("C2", "70000.000");
		coordValue3.setattrvalue("C3", "5000.000");
		IomObject coordValue4=iomObjA.addattrobj("attr4", "COORD");
		coordValue4.setattrvalue("C1", "480000.000");
		coordValue4.setattrvalue("C2", "70000.000");
		coordValue4.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AddManConCoordModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint AddManConCoordModel.AddManConCoordTopic.AddManConCoordView.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn in 2 VIEW's ausserhalb des Models
	// ein MandatoryConstraint aus einer VIEW mit einer Coord Equalation false ergibt.
	@Test
	public void mandatoryConstraint_2Models_CoordIsEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_MANDATORYCONSTRAINT_COORDCLASS, OBJ_OID1);
		IomObject coordValue3=iomObjA.addattrobj("attr3", "COORD");
		coordValue3.setattrvalue("C1", "480000.000");
		coordValue3.setattrvalue("C2", "70000.000");
		coordValue3.setattrvalue("C3", "5000.000");
		IomObject coordValue4=iomObjA.addattrobj("attr4", "COORD");
		coordValue4.setattrvalue("C1", "480000.000");
		coordValue4.setattrvalue("C2", "70000.000");
		coordValue4.setattrvalue("C3", "5000.000");
		Iom_jObject iomObjB=new Iom_jObject(ILI_MANDATORYCONSTRAINT_CLASS, OBJ_OID2);
		iomObjB.setattrvalue("attr1", "5");
		iomObjB.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AddManConCoordModel;AddManConModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint AddManConCoordModel.AddManConCoordTopic.AddManConCoordView.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn in einer VIEW ausserhalb des Models
	// ein SetConstraint false ergibt.
	@Test
	public void setConstraint_BagOfStructWrongNumber_Fail(){
		Iom_jObject iomObjStruct=new Iom_jObject(ILI_STRUCTC, null);
		Iom_jObject iomObjStruct2=new Iom_jObject(ILI_STRUCTC, null);
		Iom_jObject iomObjStruct3=new Iom_jObject(ILI_STRUCTC, null);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSC, OBJ_OID1);
		iomObj.addattrobj("Numbers", iomObjStruct);
		iomObj.addattrobj("Numbers", iomObjStruct2);
		iomObj.addattrobj("Numbers", iomObjStruct3);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AdditionalConstraints23Zusatz4");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjStruct));
		validator.validate(new ObjectEvent(iomObjStruct2));
		validator.validate(new ObjectEvent(iomObjStruct3));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Set Constraint AdditionalConstraints23Zusatz4.BodenbedeckungZusatz.PrivatGebaeude.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn die Value des Subattrs nicht in der View gefunden werden kann.
	@Test
	public void existenceConstraint_AttrsNotEqual_Fail() throws Exception{
		Iom_jObject conditionObj=new Iom_jObject(ILI_EXISTENCECONSTRAINT_CONDITION, OBJ_OID1);
		conditionObj.setattrvalue("superAttr", "lars");
		Iom_jObject obj1=new Iom_jObject(ILI_EXISTENCECONSTRAINT_CLASS, OBJ_OID2);
		obj1.setattrvalue("subAttr", "Andreas");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AddExConModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute subAttr of AdditionalConstraints23.Bodenbedeckung.ExConClass was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
}