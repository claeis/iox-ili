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

public class SetConstraint23Test {
	private TransferDescription td=null;
	// OID
	private final static String OID1 ="o1";
	private final static String OID2 ="o2";
	private final static String OID3 ="o3";
	private final static String OID4 ="o4";
	private final static String OID5 ="o5";
	// MODEL
	private final static String TOPIC="SetConstraint23.Topic";
	// STRUCTURE
	private final static String ILI_STRUCTC=TOPIC+".StructC";
	private final static String ILI_STRUCTD=TOPIC+".StructD";
	// CLASS
	private final static String ILI_CLASSA=TOPIC+".ClassA";
	private final static String ILI_CLASSB=TOPIC+".ClassB";
	private final static String ILI_CLASSC=TOPIC+".ClassC";
	private final static String ILI_CLASSD=TOPIC+".ClassD";
	private final static String ILI_CLASSE=TOPIC+".ClassE";
	// START BASKET EVENT
	private final static String BID1="b1";
	private final static String BID2="b2";
		
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/SetConstraint23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}

	//#########################################################//
	//######## SUCCESS FUNCTIONS ##############################//
	//#########################################################//
	
	// Es wird getestet, was geschieht, wenn die Pre-Bedingung und die second Bedingung wahr ist.
	@Test
	public void preAndSecondConstraintAreTrue_Ok(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSA, OID1);
		iomObj.setattrvalue("Art", "b");
		Iom_jObject iomObj2=new Iom_jObject(ILI_CLASSA, OID2);
		iomObj2.setattrvalue("Art", "c");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new ObjectEvent(iomObj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler auftritt, wenn der Pre-Constraint nicht wahr ist.
	@Test
	public void noObjectFoundInPreConstraint_Ok(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSA, OID1);
		iomObj.setattrvalue("Art", "a");
		ValidationConfig modelConfig=new ValidationConfig();
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
	
	// Es wird getestet, wenn der Pre-Constraint true ist und die Funktion: objectCount(ALL) wahr ist, ein Fehler ausgegeben wird.
	@Test
	public void preConstraintAndObjectCountTrue_Ok(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSB, OID1);
		iomObj.setattrvalue("Art", "a");
		Iom_jObject iomObj2=new Iom_jObject(ILI_CLASSB, OID2);
		iomObj2.setattrvalue("Art", "a");
		Iom_jObject iomObj3=new Iom_jObject(ILI_CLASSB, OID3);
		iomObj3.setattrvalue("Art", "a");
		Iom_jObject iomObj4=new Iom_jObject(ILI_CLASSB, OID4);
		iomObj4.setattrvalue("Art", "a");
		Iom_jObject iomObj5=new Iom_jObject(ILI_CLASSB, OID5);
		iomObj5.setattrvalue("Art", "a");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new ObjectEvent(iomObj2));
		validator.validate(new ObjectEvent(iomObj3));
		validator.validate(new ObjectEvent(iomObj4));
		validator.validate(new ObjectEvent(iomObj5));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn die Funktion: objectCount in der richtigen Menge definiert wurde und der Pre-Constraint false ist.
	// Wenn der Pre-Constraint false ist, wird der second Constraint nicht mehr ausgewertet. Somit sollte kein Fehler entstehen.
	@Test
	public void preConstraintWrong_Ok(){
		Iom_jObject iomObjStruct=new Iom_jObject(ILI_STRUCTC, null);
		Iom_jObject iomObjStruct2=new Iom_jObject(ILI_STRUCTC, null);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSC, OID1);
		iomObj.addattrobj("Numbers", iomObjStruct);
		iomObj.addattrobj("Numbers", iomObjStruct2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
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
	
	// Wenn der Pre-Constraint true ist und die Funktion areAreas true ist, ein Fehler ausgegeben wird.
	@Test
	public void preConstraintAndAreAreasTrue_Ok(){
		Iom_jObject iomObjStruct=new Iom_jObject(ILI_STRUCTD, null);
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
		Iom_jObject iomObjStruct2=new Iom_jObject(ILI_STRUCTD, null);
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
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("Numbers", iomObjStruct);
		objSurfaceSuccess.addattrobj("Numbers", iomObjStruct2);
		objSurfaceSuccess.setattrvalue("Art", "a");
		ValidationConfig modelConfig=new ValidationConfig();
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
		assertTrue(logger.getErrs().size()==0);
	}
	
	//#########################################################//
	//######## FAIL FUNCTIONS #################################//
	//#########################################################//
	
	// Es wird getestet ein Fehler ausgegeben wird, wenn der Pre-Constraint true ist und die Funktion: ObjectCount(ALL) false ist.
	@Test
	public void secondConstraintFalse_Fail(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSB, OID1);
		iomObj.setattrvalue("Art", "a");
		ValidationConfig modelConfig=new ValidationConfig();
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
		assertEquals("Set Constraint SetConstraint23.Topic.ClassB.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ein Fehler ausgegeben wird, wenn die Anzahl von objectCount nicht wahr ist.
	@Test
	public void secondConstraintWrongCount_Fail(){
		Iom_jObject iomObjStruct=new Iom_jObject(ILI_STRUCTC, null);
		Iom_jObject iomObjStruct2=new Iom_jObject(ILI_STRUCTC, null);
		Iom_jObject iomObjStruct3=new Iom_jObject(ILI_STRUCTC, null);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSC, OID1);
		iomObj.addattrobj("Numbers", iomObjStruct);
		iomObj.addattrobj("Numbers", iomObjStruct2);
		iomObj.addattrobj("Numbers", iomObjStruct3);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
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
		assertEquals("Set Constraint SetConstraint23.Topic.ClassC.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ein Fehler ausgegeben wird, wenn der Pre-Constraint true ist und die Area der Funktion: AreAreas nicht korrekt definiert wurde.
	@Test
	public void secondConstraintAreAreaFalse_Fail(){
		Iom_jObject iomObjStruct=new Iom_jObject(ILI_STRUCTD, null);
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
		Iom_jObject iomObjStruct2=new Iom_jObject(ILI_STRUCTD, null);
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
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSD, OID1);
		objSurfaceSuccess.addattrobj("Numbers", iomObjStruct);
		objSurfaceSuccess.addattrobj("Numbers", iomObjStruct2);
		objSurfaceSuccess.setattrvalue("Art", "a");
		ValidationConfig modelConfig=new ValidationConfig();
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
		assertEquals("Set Constraint SetConstraint23.Topic.ClassD.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
}