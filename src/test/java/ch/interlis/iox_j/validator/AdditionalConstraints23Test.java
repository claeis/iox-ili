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
	private final static String ILI_CLASSGEBAEUDE=ILI_TOPIC+".Gebaeude";
	private final static String ILI_CLASSBANK=ILI_TOPIC+".Bank";
	private final static String ILI_CLASSZA=ILI_TOPIC+".ClassZA";
	private final static String ILI_STRUCTC=ILI_TOPIC+".StructC";
	private final static String ILI_CLASSC=ILI_TOPIC+".ClassC";
	private final static String ILI_CLASSA=ILI_TOPIC+".ClassA";
	private final static String ILI_CLASSE=ILI_TOPIC+".ClassE";
	private final static String ILI_CLASSCONDITION=ILI_TOPIC+".ConditionClass";
	// START BASKET EVENT
	private final static String BID="b1";
	private final static String BID2="b2";
	
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
	@Test
	public void mandatoryConstraintsInEqualation_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGEBAEUDE, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AdditionalConstraints23Zusatz;");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in (!=), (<>).
	@Test
	public void mandatoryConstraintInEqualationCoordTypeOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSBANK, OBJ_OID1);
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
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AdditionalConstraints23Zusatz2");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void mandatoryConstraint_2Models_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSBANK, OBJ_OID1);
		IomObject coordValue3=iomObjA.addattrobj("attr3", "COORD");
		coordValue3.setattrvalue("C1", "480000.000");
		coordValue3.setattrvalue("C2", "70000.000");
		coordValue3.setattrvalue("C3", "5000.000");
		IomObject coordValue4=iomObjA.addattrobj("attr4", "COORD");
		coordValue4.setattrvalue("C1", "480000.000");
		coordValue4.setattrvalue("C2", "88888.000");
		coordValue4.setattrvalue("C3", "5000.000");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSGEBAEUDE, OBJ_OID2);
		iomObjB.setattrvalue("attr1", "5");
		iomObjB.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AdditionalConstraints23Zusatz2;AdditionalConstraints23Zusatz");
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
	
	// 2 objects. Objects with Class implementation
//	@Test
//	public void mandatoryConstraint_FunctionAreArea_Ok(){
//		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSZA, OBJ_OID1);
//		// Geometrie 1
//		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("Geometrie", "MULTISURFACE");
//		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
//		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
//		// polyline
//		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
//		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
//		IomObject startSegment=segments.addattrobj("segment", "COORD");
//		startSegment.setattrvalue("C1", "480000.000");
//		startSegment.setattrvalue("C2", "70000.000");
//		IomObject endSegment=segments.addattrobj("segment", "COORD");
//		endSegment.setattrvalue("C1", "483000.000");
//		endSegment.setattrvalue("C2", "70000.000");
//		// polyline 2
//		IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
//		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
//		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
//		startSegment2.setattrvalue("C1", "483000.000");
//		startSegment2.setattrvalue("C2", "70000.000");
//		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
//		endSegment2.setattrvalue("C1", "480000.000");
//		endSegment2.setattrvalue("C2", "73000.000");
//		// polyline 3
//		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
//		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
//		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
//		startSegment3.setattrvalue("C1", "480000.000");
//		startSegment3.setattrvalue("C2", "73000.000");
//		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
//		endSegment3.setattrvalue("C1", "480000.000");
//		endSegment3.setattrvalue("C2", "70000.000");
//		// Geometrie 2
//		Iom_jObject objSurfaceSuccess2=new Iom_jObject(ILI_CLASSZA, OBJ_OID2);
//		IomObject multisurfaceValue2=objSurfaceSuccess2.addattrobj("Geometrie", "MULTISURFACE");
//		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
//		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
//		// polyline
//		IomObject polylineValue5 = outerBoundary2.addattrobj("polyline", "POLYLINE");
//		IomObject segments5=polylineValue5.addattrobj("sequence", "SEGMENTS");
//		IomObject startSegment5=segments5.addattrobj("segment", "COORD");
//		startSegment5.setattrvalue("C1", "484000.000");
//		startSegment5.setattrvalue("C2", "70000.000");
//		IomObject endSegment5=segments5.addattrobj("segment", "COORD");
//		endSegment5.setattrvalue("C1", "484000.000");
//		endSegment5.setattrvalue("C2", "72500.000");
//		// polyline 2
//		IomObject polylineValue4 = outerBoundary2.addattrobj("polyline", "POLYLINE");
//		IomObject segments4=polylineValue4.addattrobj("sequence", "SEGMENTS");
//		IomObject startSegment4=segments4.addattrobj("segment", "COORD");
//		startSegment4.setattrvalue("C1", "484000.000");
//		startSegment4.setattrvalue("C2", "72500.000");
//		IomObject endSegment4=segments4.addattrobj("segment", "COORD");
//		endSegment4.setattrvalue("C1", "488000.000");
//		endSegment4.setattrvalue("C2", "70500.000");
//		// polyline 3
//		IomObject polylineValue6 = outerBoundary2.addattrobj("polyline", "POLYLINE");
//		IomObject segments6=polylineValue6.addattrobj("sequence", "SEGMENTS");
//		IomObject startSegment6=segments6.addattrobj("segment", "COORD");
//		startSegment6.setattrvalue("C1", "488000.000");
//		startSegment6.setattrvalue("C2", "70500.000");
//		IomObject endSegment6=segments6.addattrobj("segment", "COORD");
//		endSegment6.setattrvalue("C1", "484000.000");
//		endSegment6.setattrvalue("C2", "70000.000");
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AdditionalConstraints23Zusatz3");
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
//		validator.validate(new ObjectEvent(objSurfaceSuccess));
//		validator.validate(new ObjectEvent(objSurfaceSuccess2));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==0);
//	}
	
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
	
	@Test
	public void uniqueContraints_SameNumbersDifferentTextBOk(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(ILI_CLASSA, OBJ_OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AdditionalConstraints23Zusatz5");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
//	@Test
//	public void existenceConstraintInSameModel_Ok() throws Exception{
//		Iom_jObject objBedingung=new Iom_jObject(ILI_CLASSCONDITION, "o1");
//		objBedingung.setattrvalue("attr1", "lars");
//		Iom_jObject objA=new Iom_jObject(ILI_CLASSE, "o2");
//		objA.setattrvalue("attr5", "lars");
//		objA.setattrvalue("attr2", "20");
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AdditionalConstraints23Zusatz6");
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
//		validator.validate(new ObjectEvent(objBedingung));
//		validator.validate(new ObjectEvent(objA));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==0);
//	}
	
	//#########################################################//
	//########### FAIL ADDITIONAL CONSTRAINTS #################//
	//#########################################################//
	@Test
	public void mandatoryConstraint_NotEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGEBAEUDE, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "10");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AdditionalConstraints23Zusatz");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void mandatoryConstraint_WrongConfigConstraintModelName_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGEBAEUDE, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "10");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AdditionalConstraints23Zusatz2");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Referenced class Bank of additionalModels not found.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void mandatoryConstraint_ConfigConstraintModelNameNotExist_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGEBAEUDE, OBJ_OID1);
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
		assertEquals("Defined AdditionalModel AdditionalConstraints23Zusatz99999999 not exist.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (!=), (<>).
	@Test
	public void mandatoryConstraint_CoordsAreEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSBANK, OBJ_OID1);
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
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AdditionalConstraints23Zusatz2");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void mandatoryConstraint_2Models_CoordIsEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSBANK, OBJ_OID1);
		IomObject coordValue3=iomObjA.addattrobj("attr3", "COORD");
		coordValue3.setattrvalue("C1", "480000.000");
		coordValue3.setattrvalue("C2", "70000.000");
		coordValue3.setattrvalue("C3", "5000.000");
		IomObject coordValue4=iomObjA.addattrobj("attr4", "COORD");
		coordValue4.setattrvalue("C1", "480000.000");
		coordValue4.setattrvalue("C2", "70000.000");
		coordValue4.setattrvalue("C3", "5000.000");
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSGEBAEUDE, OBJ_OID2);
		iomObjB.setattrvalue("attr1", "5");
		iomObjB.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AdditionalConstraints23Zusatz;AdditionalConstraints23Zusatz2");
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
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
		assertEquals("Set Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
//	@Test
//	public void uniqueContraints_SameNumbersSameText_False(){
//		// Set object.
//		Iom_jObject obj1=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
//		obj1.setattrvalue("attr1", "ruth");
//		obj1.setattrvalue("attr2", "20");
//		Iom_jObject obj2=new Iom_jObject(ILI_CLASSA, OBJ_OID2);
//		obj2.setattrvalue("attr1", "Anna");
//		obj2.setattrvalue("attr2", "15");
//		// Create and run validator.
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		settings.setValue(Validator.CONFIG_ADDITIONAL_MODELS, "AdditionalConstraints23Zusatz5");
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
//		validator.validate(new ObjectEvent(obj1));
//		validator.validate(new ObjectEvent(obj2));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts.
//		assertTrue(logger.getErrs().size()==1);
//		assertEquals("UniqueConstraint is not true.", logger.getErrs().get(0).getEventMsg());
//	}
	
	
}