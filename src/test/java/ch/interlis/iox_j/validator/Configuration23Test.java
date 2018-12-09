package ch.interlis.iox_j.validator;

import org.junit.Before;
import org.junit.Test;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Locale;

import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxLogEvent;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

// ValidationConfig.MSG
// ValidationConfig.TYPE
// ValidationConfig.WARNING
// ValidationConfig.OFF
// ValidationConfig.CHECK
// ValidationConfig.TARGET
// ValidationConfig.MULTIPLICITY
// ValidationConfig.PARAMETER

public class Configuration23Test {
	
	private TransferDescription td=null;
	// OID
	private final static String OID1 ="o1";
	private final static String OID2 ="o2";
	private final static String OID3 ="o3";
	private final static String OID4 ="o4";
	private final static String OID5 ="o5";
	// MODEL.TOPIC
	private final static String TOPIC="Configuration23.Topic";
	// MODEL.TOPIC.CLASS
	private final static String CONDITIONTEXTCLASS=TOPIC+".ConditionTextClass";
	private final static String CLASSA=TOPIC+".ClassA";
	private final static String CLASSB=TOPIC+".ClassB";
	private final static String CLASSC=TOPIC+".ClassC";
	private final static String CLASSD=TOPIC+".ClassD";
	private final static String CLASSE=TOPIC+".ClassE";
	private final static String CLASSF=TOPIC+".ClassF";
	private final static String CLASSG=TOPIC+".ClassG";
	private final static String CLASSH=TOPIC+".ClassH";
	private final static String CLASSI=TOPIC+".ClassI";
	private final static String CLASSJ=TOPIC+".ClassJ";
	private final static String CLASSK=TOPIC+".ClassK";
	private final static String CLASSL=TOPIC+".ClassL";
	private final static String CLASSN=TOPIC+".ClassN";
	private final static String CLASSO=TOPIC+".ClassO";
	private final static String CLASSP=TOPIC+".ClassP";
	private final static String CLASSQ=TOPIC+".ClassQ";
	private final static String CLASSR=TOPIC+".ClassR";
	private final static String CLASSS=TOPIC+".ClassS";
	private final static String CLASST=TOPIC+".ClassT";
	// ASSOCIATION
	private final static String ASSOC_QR1_Q1="q1";
	private final static String ASSOC_QR1_R1="r1";
	// STRUCTURE
	private final static String STRUCTA=TOPIC+".StructA";
	// START BASKET EVENT
	private final static String BID1="b1";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/Configuration23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn das Attribut nicht stimmt und check=off definiert ist.
	@Test
	public void existenceConstraint_WrongAttrs_CheckOFF() throws Exception{
		Iom_jObject objCondition=new Iom_jObject(CONDITIONTEXTCLASS, OID1);
		objCondition.setattrvalue("attrText", "other");
		Iom_jObject objC=new Iom_jObject(CLASSA, OID2);
		objC.setattrvalue("attrText", "this");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Configuration23.Topic.ClassA.Constraint1", ValidationConfig.CHECK,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Configuration23.Topic",BID1));
		validator.validate(new ObjectEvent(objCondition));
		validator.validate(new ObjectEvent(objC));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Config CHECK=warning. Es darf keine Fehlermeldung ausgegeben werden. Es soll eine Warnung  ausgegeben werden.
	@Test
	public void existenceConstraint_ExpectedWarningMessage_CheckWarn() throws Exception {
		Iom_jObject objBedingung=new Iom_jObject(CONDITIONTEXTCLASS, OID1);
		objBedingung.setattrvalue("attrText", "other");
		Iom_jObject objA=new Iom_jObject(CLASSA, OID2);
		objA.setattrvalue("attrText", "lars");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSA+".Constraint1", ValidationConfig.CHECK,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==1);
		assertEquals("The value of the attribute attrText of Configuration23.Topic.ClassA was not found in the condition class.", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob die eigens definierte Fehlermeldung ausgegeben wird, wenn die beiden constraint Attribute nicht uebereinstimmen und validationConfig msg nicht leer ist.
	@Test
	public void existenceConstraint_AttrsNotEqual_MSGNotEmpty_Fail() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject(CONDITIONTEXTCLASS, OID1);
		objBedingung.setattrvalue("attrText", "other");
		Iom_jObject objA=new Iom_jObject(CLASSA, OID2);
		objA.setattrvalue("attrText", "lars");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSA+".Constraint1", ValidationConfig.MSG, "My own error message.");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("My own error message.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob die eigens definierte Fehlermeldung ausgegeben wird, wenn die beiden constraint Attribute nicht uebereinstimmen, check=OFF und msg=NotEmpty.
	@Test
	public void existenceConstraint_AttrsNotEqual_MSGNotEmpty__CheckOFF_Fail() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject(CONDITIONTEXTCLASS, OID1);
		objBedingung.setattrvalue("attrText", "other");
		Iom_jObject objA=new Iom_jObject(CLASSA, OID2);
		objA.setattrvalue("attrText", "lars");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSA+".Constraint1", ValidationConfig.MSG, "My own error message.");
		modelConfig.setConfigValue(CLASSA+".Constraint1", ValidationConfig.CHECK,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Es soll getestet werden, ob die eigens definierte Fehlermeldung ausgegeben wird, wenn die beiden constraint Attribute nicht uebereinstimmen und validationConfig msg leer ist.
	@Test
	public void existenceConstraint_DifferentAttrs_MSGIsEmpty_Fail() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject(CONDITIONTEXTCLASS, OID1);
		objBedingung.setattrvalue("attrText", "other");
		Iom_jObject objA=new Iom_jObject(CLASSA, OID2);
		objA.setattrvalue("attrText", "lars");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSA+".Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attrText of Configuration23.Topic.ClassA was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob die eigens definierte Fehlermeldung ausgegeben wird,
	// wenn die beiden constraint Attribute nicht uebereinstimmen, check=WARNING und msg=NotEmpty.
	@Test
	public void existenceConstraint_MSGNotEmptyAndWarning_Fail() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject(CONDITIONTEXTCLASS, OID1);
		objBedingung.setattrvalue("attrText", "other");
		Iom_jObject objA=new Iom_jObject(CLASSA, OID2);
		objA.setattrvalue("attrText", "lars");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSA+".Constraint1", ValidationConfig.CHECK,ValidationConfig.WARNING);
		modelConfig.setConfigValue(CLASSA+".Constraint1", ValidationConfig.MSG, "My own error message.");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getWarn().size()==1);
		assertEquals("My own error message.", logger.getWarn().get(0).getEventMsg());
	}

	// Es soll getestet werden, ob die eigens definierte Fehlermeldung ausgegeben wird
	// wenn die beiden constraint Attribute nicht uebereinstimmen, validationConfig msg leer und check auf warning konfiguriert ist.
	@Test
	public void existenceConstraint_MSGEmptyAndWarning_Fail() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject(CONDITIONTEXTCLASS, OID1);
		objBedingung.setattrvalue("attrText", "other");
		Iom_jObject objA=new Iom_jObject(CLASSA, OID2);
		objA.setattrvalue("attrText", "lars");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSA+".Constraint1", ValidationConfig.CHECK, ValidationConfig.WARNING);
		modelConfig.setConfigValue(CLASSA+".Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getWarn().size()==1);
		assertEquals("The value of the attribute attrText of Configuration23.Topic.ClassA was not found in the condition class.", logger.getWarn().get(0).getEventMsg());
	}
	
	// Config CHECK=warning. Es darf keine Fehlermeldung ausgegeben werden. Es sollen 4 Warnungen: Constraint 1-4 ausgegeben werden.
	@Test
	public void existenceConstraint_Expected4WarnMessages_CheckWarn() throws Exception{
		Iom_jObject objCondition=new Iom_jObject(CONDITIONTEXTCLASS, OID1);
		objCondition.setattrvalue("attrText", "other");
		Iom_jObject objC=new Iom_jObject(CLASSB, OID3);
		objC.setattrvalue("attrText1", "this");
		objC.setattrvalue("attrText2", "this");
		objC.setattrvalue("attrText3", "this");
		objC.setattrvalue("attrText4", "this");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSB+".Constraint1", ValidationConfig.CHECK,ValidationConfig.WARNING);
		modelConfig.setConfigValue(CLASSB+".Constraint2", ValidationConfig.CHECK,ValidationConfig.WARNING);
		modelConfig.setConfigValue(CLASSB+".Constraint3", ValidationConfig.CHECK,ValidationConfig.WARNING);
		modelConfig.setConfigValue(CLASSB+".Constraint4", ValidationConfig.CHECK,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Configuration23.Topic",BID1));
		validator.validate(new ObjectEvent(objCondition));
		validator.validate(new ObjectEvent(objC));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getWarn().size()==4);
		assertTrue(logger.getErrs().size()==0);
		assertEquals("The value of the attribute attrText1 of Configuration23.Topic.ClassB was not found in the condition class.", logger.getWarn().get(0).getEventMsg());
		assertEquals("The value of the attribute attrText2 of Configuration23.Topic.ClassB was not found in the condition class.", logger.getWarn().get(1).getEventMsg());
		assertEquals("The value of the attribute attrText3 of Configuration23.Topic.ClassB was not found in the condition class.", logger.getWarn().get(2).getEventMsg());
		assertEquals("The value of the attribute attrText4 of Configuration23.Topic.ClassB was not found in the condition class.", logger.getWarn().get(3).getEventMsg());
	}
	
	// Config Target=warning. Eine Warnung muss ausgegeben werden.
	@Test
	public void association_NoTargetObjectFound_TargetWARN(){
		Iom_jObject iomObj1=new Iom_jObject(CLASSC, OID1);
		Iom_jObject iomObj2=new Iom_jObject(CLASSD, OID2);
		Iom_jObject iomObj3=new Iom_jObject(CLASSA, OID3);
		iomObj2.addattrobj("c1", "REF").setobjectrefoid(OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Configuration23.Topic.cd1.c1", ValidationConfig.TARGET,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObj1));
		validator.validate(new ObjectEvent(iomObj2));
		validator.validate(new ObjectEvent(iomObj3));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==1);
		assertEquals("wrong class Configuration23.Topic.ClassA of target object o3 for role Configuration23.Topic.cd1.c1.", logger.getWarn().get(0).getEventMsg());
	}
	
	// Config MULTIPLICITY=warning. Es soll eine Warnung ausgegeben werden.
	@Test
	public void association_WrongReference_MultiplicityWARNING(){
		Iom_jObject iomObj1=new Iom_jObject(CLASSC, OID1);
		Iom_jObject iomObj2=new Iom_jObject(CLASSD, OID2);
		iomObj2.addattrobj("c1", "REF").setobjectrefoid(OID1);
		Iom_jObject iomObj3=new Iom_jObject(CLASSD, OID3);
		iomObj3.addattrobj("c1", "REF").setobjectrefoid(OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Configuration23.Topic.cd1.d1", ValidationConfig.MULTIPLICITY,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObj1));
		validator.validate(new ObjectEvent(iomObj2));
		validator.validate(new ObjectEvent(iomObj3));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==1);
		assertEquals("d1 should associate 0 to 1 target objects (instead of 2)", logger.getWarn().get(0).getEventMsg());
	}
	
	// Config MULTIPLICITY=off. Es darf keine Fehlermeldung geworfen werden.
	@Test
	public void association_WrongReference_MultiplicityOFF(){
		Iom_jObject iomObjC=new Iom_jObject(CLASSC, OID1);
		Iom_jObject iomObjD=new Iom_jObject(CLASSD, OID2);
		iomObjD.addattrobj("c1", "REF").setobjectrefoid(OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Configuration23.Topic..c1", ValidationConfig.MULTIPLICITY,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjC));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Config Target=off. Es darf keine Fehlermeldung geworfen werden.
	@Test
	public void association_NoTargetObjectFound_TargetOFF(){
		Iom_jObject iomObj1=new Iom_jObject(CLASSC, OID1);
		Iom_jObject iomObj2=new Iom_jObject(CLASSD, OID2);
		Iom_jObject iomObj3=new Iom_jObject(CLASSA, OID3);
		iomObj2.addattrobj("c1", "REF").setobjectrefoid(OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Configuration23.Topic.cd1.c1", ValidationConfig.TARGET,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObj1));
		validator.validate(new ObjectEvent(iomObj2));
		validator.validate(new ObjectEvent(iomObj3));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn config auf off gestellt wurde und die Booleans nicht uebereinstimmen.
	@Test
	public void mandatoryConstraint_BooleanInEqual_CheckOFF(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSE, OID1);
		iomObjA.setattrvalue("attr1", "true");
		iomObjA.setattrvalue("attr2", "false");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSE+".Constraint1", ValidationConfig.CHECK,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird wenn:
	// config: check=OFF
	// config: msg=NotEmpty
	// Es soll keine Nachricht ausgegeben werden, da check=off ist.
	@Test
	public void mandatoryConstraint_Boolean_CheckOFF_MSGNotEmpty(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSE, OID1);
		iomObjA.setattrvalue("attr1", "true");
		iomObjA.setattrvalue("attr2", "false");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSE+".Constraint1", ValidationConfig.CHECK,ValidationConfig.OFF);
		modelConfig.setConfigValue(CLASSE+".Constraint1", ValidationConfig.MSG, "This is my own error message!");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn config auf Warning eingestellt ist und die boolean nicht uebereinstimmen.
	@Test
	public void mandatoryConstraint_BooleanNotEqual_CheckWarning(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSE, OID1);
		iomObjA.setattrvalue("attr1", "true");
		iomObjA.setattrvalue("attr2", "false");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSE+".Constraint1", ValidationConfig.CHECK,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==1);
		assertEquals("Mandatory Constraint Configuration23.Topic.ClassE.Constraint1 is not true.", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob die eigen erstellte Fehlermeldung ausgegeben wird, wenn ValidationConfig.MSG nicht leer ist.
	@Test
	public void mandatoryConstraint_OwnMessageOutput_MSGNotEmpty(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSE, OID1);
		iomObjA.setattrvalue("attr1", "true");
		iomObjA.setattrvalue("attr2", "false");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSE+".Constraint1", ValidationConfig.MSG, "This is my own error message!");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("This is my own error message!", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob die eigen erstellte Fehlermeldung ausgegeben wird, wenn ValidationConfig.MSG leer ist.
	@Test
	public void mandatoryConstraint__OwnMessageEmpty_MSGEmpty(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSE, OID1);
		iomObjA.setattrvalue("attr1", "true");
		iomObjA.setattrvalue("attr2", "false");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSE+".Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Configuration23.Topic.ClassE.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil bei 0% liegt und >= 50% erreicht werden muss und ValidationConfig WARNING eingeschalten ist.
	@Test
	public void plausibilityConstraint_TargetResultLessThanPercentage_CheckWARNING(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSF, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSF+".Constraint1", ValidationConfig.CHECK,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==1);
		assertEquals("Plausibility Constraint Configuration23.Topic.ClassF.Constraint1 is not true.", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob die eigen erstellte Fehlermeldung ausgegeben wird,
	// wenn ValidationConfig.MSG leer und check auf Warning konfiguriert ist.
	@Test
	public void mandatoryConstraint_MSGEmptyAndWarning_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSE, OID1);
		iomObjA.setattrvalue("attr1", "true");
		iomObjA.setattrvalue("attr2", "false");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSE+".Constraint1", ValidationConfig.CHECK, ValidationConfig.WARNING);
		modelConfig.setConfigValue(CLASSE+".Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getWarn().size()==1);
		assertEquals("Mandatory Constraint Configuration23.Topic.ClassE.Constraint1 is not true.", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird,
	// wenn der prozentual richtige Anteil bei 0% liegt und >= 50% erreicht werden muss,
	// ValidationConfig check auf WARNING und MSGEmpty konfiguriert ist.
	@Test
	public void plausibilityConstraint_MSGEmptyAndWarning_False(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSF, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSF+".Constraint1", ValidationConfig.CHECK, ValidationConfig.WARNING);
		modelConfig.setConfigValue(CLASSF+".Constraint1", ValidationConfig.MSG,"");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==1);
		assertEquals("Plausibility Constraint Configuration23.Topic.ClassF.Constraint1 is not true.", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil bei 0% liegt und >= 50% erreicht werden muss und ValidationConfig MSG nicht leer ist.
	@Test
	public void plausibilityConstraint_TargetResultLessThanPercentage_MSGNotEmpty(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSF, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSF+".Constraint1", ValidationConfig.MSG, "This is my own error message!");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("This is my own error message!", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil bei 0% liegt und >= 50% erreicht werden muss, check=off, msg=notEmpty.
	@Test
	public void plausibilityConstraint_TargetResultLessThanPercentage_CheckOFF_msgNotEmpty(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSF, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSF+".Constraint1", ValidationConfig.MSG, "This is my own error message!");
		modelConfig.setConfigValue(CLASSF+".Constraint1", ValidationConfig.CHECK,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil bei 0% liegt und >= 50% erreicht werden muss und ValidationConfig MSG definiert ist, jedoch nicht leer ist.
	@Test
	public void plausibilityConstraint_TargetResultLessThanPercentage_MSGEmpty(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSF, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSF+".Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Plausibility Constraint Configuration23.Topic.ClassF.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der prozentual richtige Anteil bei 0% liegt und >= 50% erreicht werden muss und ValidationConfig auf OFF steht.
	@Test
	public void plausibilityConstraint_TargetResultLessThanPercentage_CheckOFF(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSF, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "7");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSF+".Constraint1", ValidationConfig.CHECK,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet die eigens erstellte Fehlermeldung ausgegeben wird, wenn der Pre-Constraint true ist und die Funktion: ObjectCount(ALL) false ist und die Config msg nicht leer ist.
	@Test
	public void setConstraint_SecondConstraintFalse_MSGNotEmpty(){
		Iom_jObject iomObj=new Iom_jObject(CLASSG, OID1);
		iomObj.setattrvalue("Art", "a");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSG+".Constraint1", ValidationConfig.MSG, "My own Set Constraint.");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("My own Set Constraint.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet die eigens erstellte Fehlermeldung ausgegeben wird, wenn der Pre-Constraint true ist und die Funktion: ObjectCount(ALL) false ist und die Config msg definiert, jedoch leer ist.
	@Test
	public void setConstraint_SecondConstraintFalse_MSGEmpty(){
		Iom_jObject iomObj=new Iom_jObject(CLASSG, OID1);
		iomObj.setattrvalue("Art", "a");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSG+".Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Set Constraint Configuration23.Topic.ClassG.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet die eigens erstellte Fehlermeldung ausgegeben wird,
	// wenn der Pre-Constraint true ist und die Funktion: ObjectCount(ALL) false ist,
	// die Config msg nicht leer ist und die Config Check auf warning konfiguriert ist.
	@Test
	public void setConstraint_MSGNotEmptyAndWarning_False(){
		Iom_jObject iomObj=new Iom_jObject(CLASSG, OID1);
		iomObj.setattrvalue("Art", "a");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSG+".Constraint1", ValidationConfig.CHECK, ValidationConfig.WARNING);
		modelConfig.setConfigValue(CLASSG+".Constraint1", ValidationConfig.MSG, "My own Set Constraint.");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getWarn().size()==1);
		assertEquals("My own Set Constraint.", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet die eigens erstellte Fehlermeldung ausgegeben wird,
	// wenn der Pre-Constraint true ist und die Funktion: ObjectCount(ALL) false ist,
	// die Config msg definiert, jedoch leer ist und die Config check auf Warning konfiguriert ist.
	@Test
	public void setConstraint_MSGEmptyAndWarning(){
		Iom_jObject iomObj=new Iom_jObject(CLASSG, OID1);
		iomObj.setattrvalue("Art", "a");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSG+".Constraint1", ValidationConfig.CHECK, ValidationConfig.WARNING);
		modelConfig.setConfigValue(CLASSG+".Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getWarn().size()==1);
		assertEquals("Set Constraint Configuration23.Topic.ClassG.Constraint1 is not true.", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn der Pre-Constraint true ist und die Funktion: ObjectCount(ALL) false ist und die Config check=off definiert ist.
	@Test
	public void setConstraint_SecondConstraintFalse_CheckOFF(){
		Iom_jObject iomObj=new Iom_jObject(CLASSG, OID1);
		iomObj.setattrvalue("Art", "a");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSG+".Constraint1", ValidationConfig.CHECK, ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn der Pre-Constraint true ist und die Funktion: ObjectCount(ALL) false ist und die Config check=off und msgNotEmpty definiert ist.
	@Test
	public void setConstraint_SecondConstraintFalse_CheckOFF_msgNotEmpty(){
		Iom_jObject iomObj=new Iom_jObject(CLASSG, OID1);
		iomObj.setattrvalue("Art", "a");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSG+".Constraint1", ValidationConfig.CHECK, ValidationConfig.OFF);
		modelConfig.setConfigValue(CLASSG+".Constraint1", ValidationConfig.MSG, "My own Set Constraint.");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet eine Fehlermeldung ausgegeben wird, wenn der Pre-Constraint true ist und die Funktion: ObjectCount(ALL) false ist und die Config check=warning definiert ist.
	@Test
	public void setConstraint_SecondConstraintFalse_CheckWarn(){
		Iom_jObject iomObj=new Iom_jObject(CLASSG, OID1);
		iomObj.setattrvalue("Art", "a");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSG+".Constraint1", ValidationConfig.CHECK,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==1);
		assertEquals("Set Constraint Configuration23.Topic.ClassG.Constraint1 is not true.", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigens erstellte Fehlermeldung ausgegeben wird, wenn die Nummer Unique und identisch ist, wenn validationConfig msg nicht leer ist.
	@Test
	public void uniqueConstraint_SameNumber_MSGNotEmpty(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSH,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject objA=new Iom_jObject(CLASSH,OID2);
		objA.setattrvalue("attr1", "Ralf");
		objA.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSH+".Constraint1", ValidationConfig.MSG, "My own Set Constraint.");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("My own Set Constraint.", logger.getErrs().get(0).getEventMsg());
	}
	
    @Test
    public void keymsgParam_Fail(){
        Iom_jObject objClassH1=new Iom_jObject(CLASSH, OID1);
        objClassH1.setattrvalue("attr1", "Key");
        objClassH1.setattrvalue("attr2", "20");
        
        Iom_jObject objClassH2=new Iom_jObject(CLASSH, OID2);
        objClassH2.setattrvalue("attr1", "Key");
        objClassH2.setattrvalue("attr2", "20");
        
        ValidationConfig modelConfig=new ValidationConfig();
        modelConfig.setConfigValue(CLASSH, ValidationConfig.KEYMSG, "Key {attr2}");
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC,BID1));
        validator.validate(new ObjectEvent(objClassH1));
        validator.validate(new ObjectEvent(objClassH2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        IoxLogEvent err = logger.getErrs().get(0);
        assertEquals("Key 20",err.getSourceObjectUsrId());
    }
    
    @Test
    public void keymsg_withLanguage_Param_Fail(){
        Iom_jObject objClassH1=new Iom_jObject(CLASSH, OID1);
        objClassH1.setattrvalue("attr1", "Key");
        objClassH1.setattrvalue("attr2", "20");
        
        Iom_jObject objClassH2=new Iom_jObject(CLASSH, OID2);
        objClassH2.setattrvalue("attr1", "Key");
        objClassH2.setattrvalue("attr2", "20");
        
        ValidationConfig modelConfig=new ValidationConfig();
        String actualLanguage = Locale.getDefault().getLanguage();
        // default message
        modelConfig.setConfigValue(CLASSH, ValidationConfig.KEYMSG, "KEYMsg {attr2}");
        // de spezifische message
        modelConfig.setConfigValue(CLASSH, ValidationConfig.KEYMSG+"_"+actualLanguage, "KEYMsg_lang {attr2}");
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC,BID1));
        validator.validate(new ObjectEvent(objClassH1));
        validator.validate(new ObjectEvent(objClassH2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        IoxLogEvent err = logger.getErrs().get(0);
        assertEquals("Unique is violated! Values Key, 20 already exist in Object: KEYMsg_lang 20",err.getEventMsg());
        assertEquals("KEYMsg_lang 20",err.getSourceObjectUsrId());
    }
	
	// Es wird getestet ob die eigens erstellte Fehlermeldung ausgegeben wird, wenn die Nummer Unique und identisch ist, wenn validationConfig msg leer ist.
	@Test
	public void uniqueConstraint_NumberUniqueSameNumber_MSGEmpty(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSH,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject objA=new Iom_jObject(CLASSH,OID2);
		objA.setattrvalue("attr1", "Ralf");
		objA.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSH, ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values Ralf, 20 already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigens erstellte Fehlermeldung ausgegeben wird,
	// wenn die Nummer Unique und identisch ist,
	// wenn validationConfig msg nicht leer ist und check auf Warning konfiguriert ist.
	@Test
	public void uniqueConstraint_MSGNotEmptyAndWarning(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSH,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject objA=new Iom_jObject(CLASSH,OID2);
		objA.setattrvalue("attr1", "Ralf");
		objA.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSH+".Constraint1", ValidationConfig.CHECK, ValidationConfig.WARNING);
		modelConfig.setConfigValue(CLASSH+".Constraint1", ValidationConfig.MSG, "My own Set Constraint.");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getWarn().size()==1);
		assertEquals("My own Set Constraint.", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigens erstellte Fehlermeldung ausgegeben wird,
	// wenn die Nummer Unique und identisch ist, wenn validationConfig msg leer ist und check auf warning konfiguriert ist.
	@Test
	public void uniqueConstraint_MSGEmptyAndWarning(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSH,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject objA=new Iom_jObject(CLASSH,OID2);
		objA.setattrvalue("attr1", "Ralf");
		objA.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSH+".Constraint1", ValidationConfig.CHECK, ValidationConfig.WARNING);
		modelConfig.setConfigValue(CLASSH+".Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getWarn().size()==1);
		assertEquals("Unique is violated! Values Ralf, 20 already exist in Object: o1", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet ob eine Warning anstelle einer Fehlermeldung ausgegeben wird, wenn die Nummer Unique und identisch ist und validationConfig check auf off geschalten ist.
	@Test
	public void uniqueConstraint_NumberUniqueSameNumber_CheckOFF(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSH,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject objA=new Iom_jObject(CLASSH,OID2);
		objA.setattrvalue("attr1", "Ralf");
		objA.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSH+".Constraint1", ValidationConfig.CHECK,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==0);
	}
	
	// Es wird getestet ob eine Warning anstelle einer Fehlermeldung ausgegeben wird, wenn die Nummer Unique und identisch ist und validationConfig check=off und msgNotEmpty geschalten ist.
	@Test
	public void uniqueConstraint_NumberUniqueSameNumber_checkOFF_msgNotEmpty(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSH,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject objA=new Iom_jObject(CLASSH,OID2);
		objA.setattrvalue("attr1", "Ralf");
		objA.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSH+".Constraint1", ValidationConfig.MSG, "My own Set Constraint.");
		modelConfig.setConfigValue(CLASSH+".Constraint1", ValidationConfig.CHECK,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==0);
	}
	
	// Es wird getestet ob eine Warning anstelle einer Fehlermeldung ausgegeben wird, wenn die Nummer Unique und identisch ist und validationConfig check auf warning geschalten ist.
	@Test
	public void uniqueConstraint_NumberUniqueSameNumber_CheckWARN(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSH,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject objA=new Iom_jObject(CLASSH,OID2);
		objA.setattrvalue("attr1", "Ralf");
		objA.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSH+".Constraint1", ValidationConfig.CHECK,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==1);
		assertEquals("Unique is violated! Values Ralf, 20 already exist in Object: o1", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn in einer VIEW ausserhalb des Models
	// ein MandatoryConstraint false ergibt und validationConfig check=off definiert ist.
	@Test
	public void additionalConstraint_mandatoryConstraint_NotEqual_CheckOFF(){
		Iom_jObject obj1=new Iom_jObject(CLASSI, OID1);
		obj1.setattrvalue("attr1", "5");
		obj1.setattrvalue("attr2", "10");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("AddManConModel.AddManConTopic.AddManConView"+".Constraint1", ValidationConfig.CHECK, ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AddManConModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn in einer VIEW ausserhalb des Models
	// ein MandatoryConstraint false ergibt und validationConfig check=Warning definiert ist.
	@Test
	public void additionalConstraint_mandatoryConstraint_NotEqual_CheckWarn(){
		Iom_jObject obj1=new Iom_jObject(CLASSI, OID1);
		obj1.setattrvalue("attr1", "5");
		obj1.setattrvalue("attr2", "10");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("AddManConModel.AddManConTopic.AddManConView"+".Constraint1", ValidationConfig.CHECK, ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AddManConModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==1);
		assertEquals("Mandatory Constraint AddManConModel.AddManConTopic.AddManConView.Constraint1 is not true.", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn in einer VIEW ausserhalb des Models
	// ein MandatoryConstraint false ergibt und validationConfig msg nicht leer ist.
	@Test
	public void additionalConstraint_mandatoryConstraint_NotEqual_MSGNotEmpty(){
		Iom_jObject obj1=new Iom_jObject(CLASSI, OID1);
		obj1.setattrvalue("attr1", "5");
		obj1.setattrvalue("attr2", "10");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("AddManConModel.AddManConTopic.AddManConView"+".Constraint1", ValidationConfig.MSG, "This is my own error message!");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AddManConModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
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
	public void additionalConstraint_MandatoryConstraint_NotEqual_MSGIsEmpty(){
		Iom_jObject obj1=new Iom_jObject(CLASSI, OID1);
		obj1.setattrvalue("attr1", "5");
		obj1.setattrvalue("attr2", "10");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("AddManConModel.AddManConTopic.AddManConView"+".Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AddManConModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint AddManConModel.AddManConTopic.AddManConView.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigens erstellte Fehlermeldung ausgegeben wird, wenn in einer VIEW ausserhalb des Models
	// ein SetConstraint false ergibt und validationConfig msg definiert ist, jedoch keinen Text enthaelt.
	@Test
	public void additionalConstraint_SetConstraint_BagOfStructWrongNumber_MSGEmpty(){
		Iom_jObject iomObjStruct=new Iom_jObject(STRUCTA, null);
		Iom_jObject iomObjStruct2=new Iom_jObject(STRUCTA, null);
		Iom_jObject iomObjStruct3=new Iom_jObject(STRUCTA, null);
		Iom_jObject iomObj=new Iom_jObject(CLASSJ, OID1);
		iomObj.addattrobj("Numbers", iomObjStruct);
		iomObj.addattrobj("Numbers", iomObjStruct2);
		iomObj.addattrobj("Numbers", iomObjStruct3);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("AddSetConModel.AddSetConTopic.AddSetConView"+".Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AddSetConModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjStruct));
		validator.validate(new ObjectEvent(iomObjStruct2));
		validator.validate(new ObjectEvent(iomObjStruct3));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Set Constraint AddSetConModel.AddSetConTopic.AddSetConView.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigens erstellte Fehlermeldung ausgegeben wird, wenn in einer VIEW ausserhalb des Models
	// ein SetConstraint false ergibt und validationConfig msg definiert ist.
	@Test
	public void additionalConstraint_SetConstraint_BagOfStructWrongNumber_MSGNotEmpty(){
		Iom_jObject iomObjStruct=new Iom_jObject(STRUCTA, null);
		Iom_jObject iomObjStruct2=new Iom_jObject(STRUCTA, null);
		Iom_jObject iomObjStruct3=new Iom_jObject(STRUCTA, null);
		Iom_jObject iomObj=new Iom_jObject(CLASSJ, OID1);
		iomObj.addattrobj("Numbers", iomObjStruct);
		iomObj.addattrobj("Numbers", iomObjStruct2);
		iomObj.addattrobj("Numbers", iomObjStruct3);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("AddSetConModel.AddSetConTopic.AddSetConView"+".Constraint1", ValidationConfig.MSG, "This is my ErrorMessage.");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AddSetConModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjStruct));
		validator.validate(new ObjectEvent(iomObjStruct2));
		validator.validate(new ObjectEvent(iomObjStruct3));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("This is my ErrorMessage.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigen erstellte Fehlermeldung ausgegeben wird, wenn die Value des Subattrs nicht in der View gefunden werden kann und validationConfig msg nicht leer ist.
	@Test
	public void additionalConstraint_ExistenceConstraint_AttrsNotEqual_MSGNotEmpty() throws Exception{
		Iom_jObject conditionObj=new Iom_jObject(CLASSK, OID1);
		conditionObj.setattrvalue("superAttr", "lars");
		Iom_jObject obj1=new Iom_jObject(CLASSL, OID2);
		obj1.setattrvalue("subAttr", "Andreas");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("AddExConModel.AddExConTopic.AddExConView"+".Constraint1", ValidationConfig.MSG, "This is my own Existence Constraint Error Message.");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AddExConModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("This is my own Existence Constraint Error Message.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigen erstellte Fehlermeldung ausgegeben wird, wenn die Value des Subattrs nicht in der View gefunden werden kann und validationConfig msg definiert, jedoch leer ist.
	@Test
	public void additionalConstraint_ExistenceConstraint_AttrsNotEqual_MSGEmpty() throws Exception{
		Iom_jObject conditionObj=new Iom_jObject(CLASSK, OID1);
		conditionObj.setattrvalue("superAttr", "lars");
		Iom_jObject obj1=new Iom_jObject(CLASSL, OID2);
		obj1.setattrvalue("subAttr", "Andreas");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("AddExConModel.AddExConTopic.AddExConView"+".Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AddExConModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute subAttr of Configuration23.Topic.ClassL was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigen erstellte Fehlermeldung ausgegeben wird, wenn die Value des Subattrs nicht in der View gefunden werden kann und validationConfig msg nicht leer ist.
	@Test
	public void additionalConstraint_UniquenessConstraint_AttrsNotEqual_MSGNotEmpty() throws Exception{
		Iom_jObject obj1=new Iom_jObject("Configuration23.Topic.ClassM", OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject("Configuration23.Topic.ClassM", OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("AddUnConModel.AddUnConTopic.AddUnConView.Constraint1", ValidationConfig.MSG, "My Error Message.");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AddUnConModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("My Error Message.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigen erstellte Fehlermeldung ausgegeben wird,
	// wenn die Value des Subattrs nicht in der View gefunden werden kann,
	// validationConfig MSG leer ist und check als Warning konfiguriert wird.
	@Test
	public void additionalConstraint_MsgEmptyAndWarning() throws Exception{
		Iom_jObject conditionObj=new Iom_jObject(CLASSK, OID1);
		conditionObj.setattrvalue("superAttr", "lars");
		Iom_jObject obj1=new Iom_jObject(CLASSL, OID2);
		obj1.setattrvalue("subAttr", "Andreas");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("AddExConModel.AddExConTopic.AddExConView"+".Constraint1", ValidationConfig.CHECK, ValidationConfig.WARNING);
		modelConfig.setConfigValue("AddExConModel.AddExConTopic.AddExConView"+".Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AddExConModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getWarn().size()==1);
		assertEquals("The value of the attribute subAttr of Configuration23.Topic.ClassL was not found in the condition class.", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigen erstellte Fehlermeldung ausgegeben wird,
	// wenn die Value des Subattrs nicht in der View gefunden werden kann,
	// validationConfig MSG nicht leer ist und Check als Warning konfiguriert wird.
	@Test
	public void additionalConstraint_MsgAndWarning() throws Exception{
		Iom_jObject obj1=new Iom_jObject("Configuration23.Topic.ClassM", OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject("Configuration23.Topic.ClassM", OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("AddUnConModel.AddUnConTopic.AddUnConView.Constraint1", ValidationConfig.CHECK, ValidationConfig.WARNING);
		modelConfig.setConfigValue("AddUnConModel.AddUnConTopic.AddUnConView.Constraint1", ValidationConfig.MSG, "My Error Message.");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AddUnConModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getWarn().size()==1);
		assertEquals("My Error Message.", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigen erstellte Fehlermeldung ausgegeben wird, wenn die Value des Subattrs nicht in der View gefunden werden kann, msg=NotEmpty, check=off.
	@Test
	public void additionalConstraint_UniquenessConstraint_AttrsNotEqual_msgNotEmpty_checkOFF() throws Exception{
		Iom_jObject obj1=new Iom_jObject("Configuration23.Topic.ClassM", OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject("Configuration23.Topic.ClassM", OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("AddUnConModel.AddUnConTopic.AddUnConView.Constraint1", ValidationConfig.MSG, "My Error Message.");
		modelConfig.setConfigValue("AddUnConModel.AddUnConTopic.AddUnConView"+".Constraint1", ValidationConfig.CHECK, ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AddUnConModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob die eigen erstellte Fehlermeldung ausgegeben wird, wenn die Value des Subattrs nicht in der View gefunden werden kann und validationConfig msg leer ist.
	@Test
	public void additionalConstraint_UniquenessConstraint_AttrsNotEqual_MSGEmpty() throws Exception{
		Iom_jObject obj1=new Iom_jObject("Configuration23.Topic.ClassM", OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject("Configuration23.Topic.ClassM", OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("AddUnConModel.AddUnConTopic.AddUnConView.Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AddUnConModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values Ralf already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigen erstellte Fehlermeldung ausgegeben wird, wenn bei der Funktion: isEnumSubVal, die subValue nicht mit der hoeheren Hierarchie nicht uebereinstimmt und validationConfig.MSG nicht leer ist.
	@Test
	public void function_isEnumSubVal_MehrVierIsNotSubValOfEins_MSGNotEmpty_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSN, OID1);
		iomObjA.setattrvalue("attr1", "eins");
		iomObjA.setattrvalue("attr2", "mehr.vier");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSN+".Constraint1", ValidationConfig.MSG, "This Function Error is written by my own.");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("This Function Error is written by my own.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigen erstellte Fehlermeldung ausgegeben wird,
	// wenn die Value des Subattrs nicht in der View gefunden werden kann,
	// validationConfig msg leer ist und check auf Warning konfiguriert ist.
	@Test
	public void additionalConstraint_MSGEmptyAndWarning() throws Exception{
		Iom_jObject obj1=new Iom_jObject("Configuration23.Topic.ClassM", OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject("Configuration23.Topic.ClassM", OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("AddUnConModel.AddUnConTopic.AddUnConView.Constraint1", ValidationConfig.CHECK, ValidationConfig.WARNING);
		modelConfig.setConfigValue("AddUnConModel.AddUnConTopic.AddUnConView.Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AddUnConModel");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getWarn().size()==1);
		assertEquals("Unique is violated! Values Ralf already exist in Object: o1", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigen erstellte Fehlermeldung ausgegeben wird
	// wenn bei der Funktion: isEnumSubVal, die subValue nicht mit der hoeheren,
	// Hierarchie nicht uebereinstimmt und validationConfig.MSG nicht leer ist und Check auf Warning konfiguriert ist.
	@Test
	public void function_isEnumSubVal_MSGNotEmptyAndWarning_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSN, OID1);
		iomObjA.setattrvalue("attr1", "eins");
		iomObjA.setattrvalue("attr2", "mehr.vier");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSN+".Constraint1", ValidationConfig.CHECK, ValidationConfig.WARNING);
		modelConfig.setConfigValue(CLASSN+".Constraint1", ValidationConfig.MSG, "This Function Error is written by my own.");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getWarn().size()==1);
		assertEquals("This Function Error is written by my own.", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigen erstellte Fehlermeldung ausgegeben wird
	// wenn bei der Funktion: isEnumSubVal, die subValue nicht mit der hoeheren,
	// Hierarchie nicht uebereinstimmt und validationConfig.MSG leer ist und Check auf Warning konfiguriert ist.
	@Test
	public void function_MSGEmptyAndWarning_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSN, OID1);
		iomObjA.setattrvalue("attr1", "eins");
		iomObjA.setattrvalue("attr2", "mehr.vier");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSN+".Constraint1", ValidationConfig.CHECK, ValidationConfig.WARNING);
		modelConfig.setConfigValue(CLASSN+".Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getWarn().size()==1);
		assertEquals("Mandatory Constraint Configuration23.Topic.ClassN.Constraint1 is not true.", logger.getWarn().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigen erstellte Fehlermeldung ausgegeben wird, wenn bei der Funktion: isEnumSubVal, die subValue nicht mit der hoeheren Hierarchie nicht uebereinstimmt und validationConfig.MSG leer ist.
	@Test
	public void function_isEnumSubVal_MehrVierIsNotSubValOfEins_MSGEmpty(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSN, OID1);
		iomObjA.setattrvalue("attr1", "eins");
		iomObjA.setattrvalue("attr2", "mehr.vier");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSN+".Constraint1", ValidationConfig.MSG, "");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Configuration23.Topic.ClassN.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Wenn ValidationConfig auf TYPE und auf OFF steht, darf kein Fehler ausgegeben werden.
	@Test
	public void datatype_WrongFormat_TypeOFF(){
		Iom_jObject objTrue=new Iom_jObject(CLASSO, OID1);
		objTrue.setattrvalue("aBoolean", "undecided");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSO+".aBoolean", ValidationConfig.TYPE,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC, BID1));
		validator.validate(new ObjectEvent(objTrue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Wenn ValidationConfig TYPE und WARNING definiert wurde, wird kein Fehler, sondern eine Warnung ausgegeben.
	@Test
	public void datatype_WrongFormat_TypeWarn(){
		Iom_jObject objTrue=new Iom_jObject(CLASSO, OID1);
		objTrue.setattrvalue("aBoolean", "undecided");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(CLASSO+".aBoolean", ValidationConfig.TYPE,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC, BID1));
		validator.validate(new ObjectEvent(objTrue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getWarn().size()==1);
		assertEquals("value <undecided> is not a BOOLEAN in attribute aBoolean", logger.getWarn().get(0).getEventMsg());
	}
	
	// parameter=default=off
	@Test
	public void datatype__coordType2DRangeConfigGeomDefaultOff(){
		Iom_jObject obj1=new Iom_jObject(CLASSP, OID1);
		IomObject coordValue=obj1.addattrobj("lcoord", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "10000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.DEFAULT_GEOMETRY_TYPE_VALIDATION, ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC, BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// parameter=default=warning 
	@Test
	public void datatype__coordType2DRangeConfigGeomDefaultWarning(){
		Iom_jObject obj1=new Iom_jObject(CLASSP, OID1);
		IomObject coordValue=obj1.addattrobj("lcoord", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "10000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.DEFAULT_GEOMETRY_TYPE_VALIDATION, ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC, BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getWarn().size()==1);
		assertEquals("value 10000.000 is out of range in attribute lcoord",logger.getWarn().get(0).getEventMsg());
	}
	
	// target=on. Erwarte den Fehler:
	// t1 should associate 1 to 1 target objects (instead of 3)
	@Test
	public void association_TargetAndCardinalityWrong_TargetON(){
		Iom_jObject iomObjG=new Iom_jObject(CLASSS, OID1);
		Iom_jObject iomObjH1=new Iom_jObject(CLASST, OID2);
		iomObjH1.addattrobj("s1", "REF").setobjectrefoid(OID1);
		Iom_jObject iomObjH2=new Iom_jObject(CLASST, OID3);
		iomObjH2.addattrobj("s1", "REF").setobjectrefoid(OID1);
		Iom_jObject iomObjH3=new Iom_jObject(CLASST, OID4);
		iomObjH3.addattrobj("s1", "REF").setobjectrefoid(OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjG));
		validator.validate(new ObjectEvent(iomObjH1));
		validator.validate(new ObjectEvent(iomObjH2));
		validator.validate(new ObjectEvent(iomObjH3));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("t1 should associate 1 to 1 target objects (instead of 3)",logger.getErrs().get(0).getEventMsg());
	}
	
	// target=WARNING.
	// Es soll keine Fehlermeldung ausgegeben werden.
	// Es soll eine Warnung ausgegeben werden.
	@Test
	public void association_noTargetObject_TargetWARN(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSQ, OID1);
		Iom_jObject iomObjB=new Iom_jObject(CLASSR, OID2);
		iomObjB.addattrobj(ASSOC_QR1_Q1, "REF").setobjectrefoid(OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Configuration23.Topic.qr1.q1", ValidationConfig.TARGET,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==1);
		assertEquals("No object found with OID o3 in basket b1.", logger.getWarn().get(0).getEventMsg());
	}
	
	// target=OFF.
	// folgende Info muss ausgegeben werden:
	// Info: Configuration23.Topic.qr1.q1 not validated, validation configuration target=off
	@Test
	public void association_noTargetObject_TargetOFF(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSQ, OID1);
		Iom_jObject iomObjB=new Iom_jObject(CLASSR, OID2);
		iomObjB.addattrobj(ASSOC_QR1_Q1, "REF").setobjectrefoid(OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Configuration23.Topic.qr1.q1", ValidationConfig.TARGET,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==0);
	}
	
	// target=on. Erwarte den Fehler:
	// t1 should associate 1 to 1 target objects (instead of 3)
	@Test
	public void association_TargetAndCardinalityWrong_MultiplicityON(){
		Iom_jObject iomObjG=new Iom_jObject(CLASSS, OID1);
		Iom_jObject iomObjH1=new Iom_jObject(CLASST, OID2);
		iomObjH1.addattrobj("s1", "REF").setobjectrefoid(OID1);
		Iom_jObject iomObjH2=new Iom_jObject(CLASST, OID3);
		iomObjH2.addattrobj("s1", "REF").setobjectrefoid(OID1);
		Iom_jObject iomObjH3=new Iom_jObject(CLASST, OID4);
		iomObjH3.addattrobj("s1", "REF").setobjectrefoid(OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjG));
		validator.validate(new ObjectEvent(iomObjH1));
		validator.validate(new ObjectEvent(iomObjH2));
		validator.validate(new ObjectEvent(iomObjH3));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("t1 should associate 1 to 1 target objects (instead of 3)",logger.getErrs().get(0).getEventMsg());
	}
	
	// target=WARNING.
	// Es soll keine Fehlermeldung ausgegeben werden.
	// Es soll eine Warnung ausgegeben werden.
	@Test
	public void association_noTargetObject_MultiplicityWARN(){
		Iom_jObject iomObjG=new Iom_jObject(CLASSS, OID1);
		Iom_jObject iomObjH1=new Iom_jObject(CLASST, OID2);
		iomObjH1.addattrobj("s1", "REF").setobjectrefoid(OID1);
		Iom_jObject iomObjH2=new Iom_jObject(CLASST, OID3);
		iomObjH2.addattrobj("s1", "REF").setobjectrefoid(OID1);
		Iom_jObject iomObjH3=new Iom_jObject(CLASST, OID4);
		iomObjH3.addattrobj("s1", "REF").setobjectrefoid(OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Configuration23.Topic.st1.t1", ValidationConfig.MULTIPLICITY,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjG));
		validator.validate(new ObjectEvent(iomObjH1));
		validator.validate(new ObjectEvent(iomObjH2));
		validator.validate(new ObjectEvent(iomObjH3));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==1);
		assertEquals("t1 should associate 1 to 1 target objects (instead of 3)", logger.getWarn().get(0).getEventMsg());
	}
	
	// target=OFF.
	// folgende Info muss ausgegeben werden:
	// Info: Configuration23.Topic.st1.t1 not validated, validation configuration multiplicity=off
	@Test
	public void association_noTargetObject_MultiplicityOFF(){
		Iom_jObject iomObjG=new Iom_jObject(CLASSS, OID1);
		Iom_jObject iomObjH1=new Iom_jObject(CLASST, OID2);
		iomObjH1.addattrobj("s1", "REF").setobjectrefoid(OID1);
		Iom_jObject iomObjH2=new Iom_jObject(CLASST, OID3);
		iomObjH2.addattrobj("s1", "REF").setobjectrefoid(OID1);
		Iom_jObject iomObjH3=new Iom_jObject(CLASST, OID4);
		iomObjH3.addattrobj("s1", "REF").setobjectrefoid(OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Configuration23.Topic.st1.t1", ValidationConfig.MULTIPLICITY,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjG));
		validator.validate(new ObjectEvent(iomObjH1));
		validator.validate(new ObjectEvent(iomObjH2));
		validator.validate(new ObjectEvent(iomObjH3));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==0);
	}		
	
	// wenn die configuration: multiplicity=off und target=off gemacht wird, duerfen keine Fehler und Warnungen ausgegeben werden.
	// Es muessen 2 Info-Meldungen ausgegeben werden.
	@Test
	public void association_TargetAndCardinalityWrong_MultiplicityOFF_TargetOFF(){
		Iom_jObject iomObjG=new Iom_jObject(CLASSS, OID1);
		Iom_jObject iomObjH1=new Iom_jObject(CLASST, OID2);
		iomObjH1.addattrobj("s1", "REF").setobjectrefoid(OID1);
		Iom_jObject iomObjH2=new Iom_jObject(CLASST, OID3);
		iomObjH2.addattrobj("s1", "REF").setobjectrefoid(OID1);
		Iom_jObject iomObjH3=new Iom_jObject(CLASST, OID4);
		iomObjH3.addattrobj("s1", "REF").setobjectrefoid(OID5);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Configuration23.Topic.st1.s1", ValidationConfig.TARGET,ValidationConfig.OFF);
		modelConfig.setConfigValue("Configuration23.Topic.st1.t1", ValidationConfig.MULTIPLICITY,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjG));
		validator.validate(new ObjectEvent(iomObjH1));
		validator.validate(new ObjectEvent(iomObjH2));
		validator.validate(new ObjectEvent(iomObjH3));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==0);
	}
	
	// wenn die configuration: multiplicity=warning und target=warning configuriert wird, duerfen keine Fehler ausgegeben werden.
	// Es muessen 2 Warningen ausgegeben werden:
	// Info: No object found with OID o5.
	// Info: t1 should associate 1 to 1 target objects (instead of 2)
	@Test
	public void association_TargetAndCardinalityWrong_MultiplicityWARN_TargetWARN(){
		Iom_jObject iomObjG=new Iom_jObject(CLASSS, OID1);
		Iom_jObject iomObjH1=new Iom_jObject(CLASST, OID2);
		iomObjH1.addattrobj("s1", "REF").setobjectrefoid(OID1);
		Iom_jObject iomObjH2=new Iom_jObject(CLASST, OID3);
		iomObjH2.addattrobj("s1", "REF").setobjectrefoid(OID1);
		Iom_jObject iomObjH3=new Iom_jObject(CLASST, OID4);
		iomObjH3.addattrobj("s1", "REF").setobjectrefoid(OID5);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Configuration23.Topic.st1.s1", ValidationConfig.TARGET,ValidationConfig.WARNING);
		modelConfig.setConfigValue("Configuration23.Topic.st1.t1", ValidationConfig.MULTIPLICITY,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjG));
		validator.validate(new ObjectEvent(iomObjH1));
		validator.validate(new ObjectEvent(iomObjH2));
		validator.validate(new ObjectEvent(iomObjH3));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
		assertTrue(logger.getWarn().size()==2);
		assertEquals("t1 should associate 1 to 1 target objects (instead of 2)",logger.getWarn().get(0).getEventMsg());
		assertEquals("No object found with OID o5 in basket b1.",logger.getWarn().get(1).getEventMsg());
	}
}