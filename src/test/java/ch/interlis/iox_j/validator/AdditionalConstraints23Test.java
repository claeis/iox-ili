package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
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
	private final static String OID1 ="o1";
	private final static String OID2 ="o2";
	// MODEL.TOPIC
	private final static String TOPIC="AdditionalConstraints23.Topic";
	// CLASS
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
	// STRUCT
	private final static String STRUCTA=TOPIC+".StructA";	
	private final static String STRUCTB=TOPIC+".StructB";
	// BID
	private final static String BID1="b1";
	
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
	// ein PlausibilityContraint true ergibt.
	@Test
	public void plausibilityConstraintTrue_Ok(){
		Iom_jObject obj1=new Iom_jObject(CLASSF, OID1);
		obj1.setattrvalue("attr1", "7");
		obj1.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelG;");
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
	// ein MandatoryConstraint true ergibt.
	@Test
	public void mandatoryConstraintTrue_Ok(){
		Iom_jObject obj1=new Iom_jObject(CLASSB, OID1);
		obj1.setattrvalue("attr1", "5");
		obj1.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelC;");
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
	// ein MandatoryConstraint InEqualation true ergibt.
	@Test
	public void mandatoryConstraint_InEqualTrue_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSE, OID1);
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
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelC;");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
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
		Iom_jObject iomObjA=new Iom_jObject(CLASSE, OID1);
		IomObject coordValue3=iomObjA.addattrobj("attr3", "COORD");
		coordValue3.setattrvalue("C1", "480000.000");
		coordValue3.setattrvalue("C2", "70000.000");
		coordValue3.setattrvalue("C3", "5000.000");
		IomObject coordValue4=iomObjA.addattrobj("attr4", "COORD");
		coordValue4.setattrvalue("C1", "480000.000");
		coordValue4.setattrvalue("C2", "88888.000");
		coordValue4.setattrvalue("C3", "5000.000");
		Iom_jObject iomObjB=new Iom_jObject(CLASSB, OID2);
		iomObjB.setattrvalue("attr1", "5");
		iomObjB.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelD;AdditionalModelC;");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
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
		Iom_jObject iomObjStruct=new Iom_jObject(STRUCTA, null);
		Iom_jObject iomObjStruct2=new Iom_jObject(STRUCTA, null);
		Iom_jObject iomObj=new Iom_jObject(CLASSC, OID1);
		iomObj.addattrobj("Numbers", iomObjStruct);
		iomObj.addattrobj("Numbers", iomObjStruct2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelF;");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
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
		Iom_jObject conditionObj=new Iom_jObject(CLASSH, OID1);
		conditionObj.setattrvalue("superAttr", "lars");
		Iom_jObject obj1=new Iom_jObject(CLASSG, OID2);
		obj1.setattrvalue("subAttr", "lars");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelB;");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob die eigen erstellte Fehlermeldung ausgegeben wird, wenn die Value des Subattrs nicht in der View gefunden werden kann und validationConfig msg nicht leer ist.
	@Test
	public void uniquenessConstraint_AttrsNotEqual_Ok() throws Exception {
		Iom_jObject obj1=new Iom_jObject(CLASSA, OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSA, OID2);
		obj2.setattrvalue("attr1", "Anna");
		obj2.setattrvalue("attr2", "10");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelE");
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
	
	
	//#########################################################//
	//########### FAIL ADDITIONAL CONSTRAINTS #################//
	//#########################################################//
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn in einer VIEW ausserhalb des Models
	// ein PlausibilityContraint false ergibt.
	@Test
	public void plausibilityConstraintFale_False(){
		Iom_jObject obj1=new Iom_jObject(CLASSF, OID1);
		obj1.setattrvalue("attr1", "5");
		obj1.setattrvalue("attr2", "7");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelG;");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Plausibility Constraint AdditionalModelG.AdditionalTopicG.AdditionalClassG.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn in einer VIEW ausserhalb des Models
	// ein MandatoryConstraint false ergibt.
	@Test
	public void mandatoryConstraint_NotEqual_Fail(){
		Iom_jObject obj1=new Iom_jObject(CLASSB, OID1);
		obj1.setattrvalue("attr1", "5");
		obj1.setattrvalue("attr2", "10");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelC;");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint AdditionalModelC.AdditionalTopicC.AdditionalClassC.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
    @Test
    public void mandatoryConstraint_NotEqual_DuplicateModel_Fail(){
        Iom_jObject obj1=new Iom_jObject(CLASSB, OID1);
        obj1.setattrvalue("attr1", "5");
        obj1.setattrvalue("attr2", "10");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelC;AdditionalConstraints23");
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC,BID1));
        validator.validate(new ObjectEvent(obj1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("Mandatory Constraint AdditionalModelC.AdditionalTopicC.AdditionalClassC.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
    }
	

	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn die VIEW ausserhalb des Models nicht gefunden wird.
	@Test
	public void mandatoryConstraint_ConfigConstraintModelNameNotExist_False(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSB, OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "10");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalConstraints23Zusatz99999999");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
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
		Iom_jObject iomObjA=new Iom_jObject(CLASSE, OID1);
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
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelD");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint AdditionalModelD.AdditionalTopicD.AdditionalClassD.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn in 2 VIEW's ausserhalb des Models
	// ein MandatoryConstraint aus einer VIEW mit einer Coord Equalation false ergibt.
	@Test
	public void mandatoryConstraint_2Models_CoordIsEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSE, OID1);
		IomObject coordValue3=iomObjA.addattrobj("attr3", "COORD");
		coordValue3.setattrvalue("C1", "480000.000");
		coordValue3.setattrvalue("C2", "70000.000");
		coordValue3.setattrvalue("C3", "5000.000");
		IomObject coordValue4=iomObjA.addattrobj("attr4", "COORD");
		coordValue4.setattrvalue("C1", "480000.000");
		coordValue4.setattrvalue("C2", "70000.000");
		coordValue4.setattrvalue("C3", "5000.000");
		Iom_jObject iomObjB=new Iom_jObject(CLASSB, OID2);
		iomObjB.setattrvalue("attr1", "5");
		iomObjB.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelD;AdditionalModelC");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint AdditionalModelD.AdditionalTopicD.AdditionalClassD.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn in einer VIEW ausserhalb des Models
	// ein SetConstraint false ergibt.
	@Test
	public void setConstraint_BagOfStructWrongNumber_Fail(){
		Iom_jObject iomObjStruct=new Iom_jObject(STRUCTA, null);
		Iom_jObject iomObjStruct2=new Iom_jObject(STRUCTA, null);
		Iom_jObject iomObjStruct3=new Iom_jObject(STRUCTA, null);
		Iom_jObject iomObj=new Iom_jObject(CLASSC, OID1);
		iomObj.addattrobj("Numbers", iomObjStruct);
		iomObj.addattrobj("Numbers", iomObjStruct2);
		iomObj.addattrobj("Numbers", iomObjStruct3);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelF");
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
		assertEquals("Set Constraint AdditionalModelF.AdditionalTopicF.AdditionalClassF.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn die Value des Subattrs nicht in der View gefunden werden kann.
	@Test
	public void existenceConstraint_AttrsNotEqual_Fail() throws Exception{
		Iom_jObject conditionObj=new Iom_jObject(CLASSH, OID1);
		conditionObj.setattrvalue("superAttr", "lars");
		Iom_jObject obj1=new Iom_jObject(CLASSG, OID2);
		obj1.setattrvalue("subAttr", "Andreas");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelB");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute subAttr of AdditionalConstraints23.Topic.ClassG was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die eigen erstellte Fehlermeldung ausgegeben wird, wenn die Value des Subattrs nicht in der View gefunden werden kann und validationConfig msg nicht leer ist.
	@Test
	public void uniquenessConstraint_AttrsNotEqual_Fail() throws Exception {
		Iom_jObject obj1=new Iom_jObject(CLASSA, OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSA, OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelE");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values Ralf, 20 already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn bei die Funktion: areArea,
	// ueber eine additional constraint via set constraint ausgefuehrt werden kann.
	// 1 object. Objects=ALL, SurfaceBAG=UNDEFINED, SurfaceAttr=Geometrie.
	@Test
	public void mandatoryConstraint_FunctionAreArea_Ok(){
		Iom_jObject function1=new Iom_jObject(CLASSI, OID1);
		// Geometrie 1
		IomObject multisurfaceValue=function1.addattrobj("Geometrie", "MULTISURFACE");
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
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelH");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(function1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn bei die Funktion: areArea,
	// ueber eine additional constraint via set constraint ausgefuehrt werden kann und die Objekte verschiedene Values enthalten.
	// 1 object. Objects=ALL, SurfaceBAG=>> Numbers, SurfaceAttr=>> AdditionalConstraints23.Topic.StructD->Surface.
	@Test
	public void mandatoryConstraint_FunctionAreArea_Fail(){
		Iom_jObject iomObjStruct=new Iom_jObject(STRUCTB, null);
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
		// Geometrie 2
		Iom_jObject iomObjStruct2=new Iom_jObject(STRUCTB, null);
		IomObject multisurfaceValue2=iomObjStruct2.addattrobj("Surface", "MULTISURFACE");
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
		Iom_jObject objSurfaceSuccess=new Iom_jObject(CLASSD, OID1);
		objSurfaceSuccess.addattrobj("Numbers", iomObjStruct);
		objSurfaceSuccess.addattrobj("Numbers", iomObjStruct2);
		objSurfaceSuccess.setattrvalue("Art", "a");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelI");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjStruct));
		validator.validate(new ObjectEvent(iomObjStruct2));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Set Constraint AdditionalModelI.AdditionalTopicI.AdditionalClassI.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob eine Intersection Fehlermeldung ausgegeben wird,
	// wenn sich die Boundaries der Surface selbst ueberschneiden.
	@Test
	public void mandatoryConstraint_OverlappedSurface_False(){
	    EhiLogger.getInstance().setTraceFilter(false);
		Iom_jObject function1=new Iom_jObject(CLASSI, OID1);
		// Geometrie 1
		IomObject multisurfaceValue=function1.addattrobj("Geometrie", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		// outer boundary
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "480000.000");
		endSegment.setattrvalue("C2", "250000.000");
		// polyline 2
		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "480000.000");
		startSegment2.setattrvalue("C2", "250000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "600000.000");
		endSegment2.setattrvalue("C2", "250000.000");
		// polyline 3
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "600000.000");
		startSegment3.setattrvalue("C2", "250000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// inner boundary
		IomObject innerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline
		IomObject polylineValueInner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segmentsInner=polylineValueInner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		startSegmentInner.setattrvalue("C1", "500000.000");
		startSegmentInner.setattrvalue("C2", "150000.000");
		IomObject endSegmentInner=segmentsInner.addattrobj("segment", "COORD");
		endSegmentInner.setattrvalue("C1", "500000.000");
		endSegmentInner.setattrvalue("C2", "230000.000");
		// polyline 2
		IomObject polylineValue2Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments2Inner=polylineValue2Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		startSegment2Inner.setattrvalue("C1", "500000.000");
		startSegment2Inner.setattrvalue("C2", "230000.000");
		IomObject endSegment2Inner=segments2Inner.addattrobj("segment", "COORD");
		endSegment2Inner.setattrvalue("C1", "600000.000");
		endSegment2Inner.setattrvalue("C2", "230000.000");
		// polyline 3
		IomObject polylineValue3Inner = innerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3Inner=polylineValue3Inner.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		startSegment3Inner.setattrvalue("C1", "600000.000");
		startSegment3Inner.setattrvalue("C2", "230000.000");
		IomObject endSegment3Inner=segments3Inner.addattrobj("segment", "COORD");
		endSegment3Inner.setattrvalue("C1", "500000.000");
		endSegment3Inner.setattrvalue("C2", "150000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelH");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(function1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(3,logger.getErrs().size());
		assertEquals("Intersection coord1 (571428.571, 207142.857), tids o1, o1", logger.getErrs().get(0).getEventMsg());
		assertEquals("Intersection coord1 (586666.667, 230000.000), tids o1, o1", logger.getErrs().get(1).getEventMsg());
		assertEquals("Set Constraint AdditionalModelH.AdditionalTopicH.AdditionalClassH.Constraint1 is not true.", logger.getErrs().get(2).getEventMsg());
	}
	
	// Prueft die Konfiguration: constraint validation.
	// Die Konfiguration ist nicht gesetzt.
	// Es wird eine Fehlermeldung erwartet.
	@Test
	public void plausibilityConstraintFail_ConstraintDisableSet_NotSet_False(){
		Iom_jObject obj1=new Iom_jObject(CLASSF, OID1);
		obj1.setattrvalue("attr1", "5");
		obj1.setattrvalue("attr2", "7");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelG;");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Plausibility Constraint AdditionalModelG.AdditionalTopicG.AdditionalClassG.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Prueft die Konfiguration: constraint validation.
	// Die Konfiguration ist Eingeschaltet.
	// Es wird eine Fehlermeldung erwartet.
	@Test
	public void plausibilityConstraintFail_ConstraintDisableSet_ON_False(){
		Iom_jObject obj1=new Iom_jObject(CLASSF, OID1);
		obj1.setattrvalue("attr1", "5");
		obj1.setattrvalue("attr2", "7");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.CONSTRAINT_VALIDATION, ValidationConfig.ON);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelG;");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Plausibility Constraint AdditionalModelG.AdditionalTopicG.AdditionalClassG.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Prueft die Konfiguration: constraint validation.
	// Die Konfiguration ist Ausgeschaltet.
	// Es wird erwartet dass keine Fehlermeldung ausgegeben wird.
	@Test
	public void plausibilityConstraintFail_ConstraintDisableSet_OFF_Ok(){
		Iom_jObject obj1=new Iom_jObject(CLASSF, OID1);
		obj1.setattrvalue("attr1", "5");
		obj1.setattrvalue("attr2", "7");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.CONSTRAINT_VALIDATION, ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ADDITIONAL_MODELS, "AdditionalModelG;");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
}