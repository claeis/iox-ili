package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
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
	// CONSTANT SUCCESS
	private final static String ILI_CLASSCONSTANTA=ILI_TOPIC+".ClassConstantA";
	private final static String ILI_CLASSCONSTANTB=ILI_TOPIC+".ClassConstantB";
	private final static String ILI_CLASSCONSTANTD=ILI_TOPIC+".ClassConstantD";
	private final static String ILI_CLASSCONSTANTE=ILI_TOPIC+".ClassConstantE";
	private final static String ILI_CLASSCONSTANTF=ILI_TOPIC+".ClassConstantF";
	private final static String ILI_CLASSCONSTANTG=ILI_TOPIC+".ClassConstantG";
	// ATTRIBUTES EQUALATION (==) SUCCESS AND FAIL
	private final static String ILI_CLASSEQUALATIONA=ILI_TOPIC+".ClassEqualationA";
	private final static String ILI_CLASSEQUALATIONB=ILI_TOPIC+".ClassEqualationB";
	private final static String ILI_CLASSEQUALATIONC=ILI_TOPIC+".ClassEqualationC";
	private final static String ILI_CLASSEQUALATIOND=ILI_TOPIC+".ClassEqualationD";
	private final static String ILI_CLASSEQUALATIONE=ILI_TOPIC+".ClassEqualationE";
	private final static String ILI_CLASSEQUALATIONF=ILI_TOPIC+".ClassEqualationF";
	private final static String ILI_CLASSEQUALATIONG=ILI_TOPIC+".ClassEqualationG";
	private final static String ILI_CLASSEQUALATIONH=ILI_TOPIC+".ClassEqualationH";
	private final static String ILI_CLASSEQUALATIONI=ILI_TOPIC+".ClassEqualationI";
	// ATTRIBUTES INEQUALATION (!=), (<>) SUCCESS AND FAIL
	private final static String ILI_CLASSINEQUALATIONA=ILI_TOPIC+".ClassInEqualationA";
	private final static String ILI_CLASSINEQUALATIONB=ILI_TOPIC+".ClassInEqualationB";
	private final static String ILI_CLASSINEQUALATIONC=ILI_TOPIC+".ClassInEqualationC";
	private final static String ILI_CLASSINEQUALATIOND=ILI_TOPIC+".ClassInEqualationD";
	private final static String ILI_CLASSINEQUALATIONE=ILI_TOPIC+".ClassInEqualationE";
	private final static String ILI_CLASSINEQUALATIONF=ILI_TOPIC+".ClassInEqualationF";
	private final static String ILI_CLASSINEQUALATIONG=ILI_TOPIC+".ClassInEqualationG";
	private final static String ILI_CLASSINEQUALATIONH=ILI_TOPIC+".ClassInEqualationH";
	private final static String ILI_CLASSINEQUALATIONI=ILI_TOPIC+".ClassInEqualationI";
	// ATTRIBUTES GREATER THAN (>) SUCCESS AND FAIL
	private final static String ILI_CLASSGREATERTHANA=ILI_TOPIC+".ClassGreaterThanA";
	private final static String ILI_CLASSGREATERTHANB=ILI_TOPIC+".ClassGreaterThanB";
	private final static String ILI_CLASSGREATERTHANC=ILI_TOPIC+".ClassGreaterThanC";
	// ATTRIBUTES SMALLER THAN (<) SUCCESS AND FAIL
	private final static String ILI_CLASSLESSTHANA=ILI_TOPIC+".ClassLessThanA";
	private final static String ILI_CLASSLESSTHANB=ILI_TOPIC+".ClassLessThanB";
	private final static String ILI_CLASSLESSTHANC=ILI_TOPIC+".ClassLessThanC";
	// ATTRIBUTES GREATER OR EQUAL THAN (>=) SUCCESS AND FAIL
	private final static String ILI_CLASSGREATERTHANOREQUALA=ILI_TOPIC+".ClassGreaterThanOrEqualA";
	private final static String ILI_CLASSGREATERTHANOREQUALB=ILI_TOPIC+".ClassGreaterThanOrEqualB";
	private final static String ILI_CLASSGREATERTHANOREQUALC=ILI_TOPIC+".ClassGreaterThanOrEqualC";
	// ATTRIBUTES GREATER OR EQUAL THAN (<=) SUCCESS AND FAIL
	private final static String ILI_CLASSLESSTHANOREQUALA=ILI_TOPIC+".ClassLessThanOrEqualA";
	private final static String ILI_CLASSLESSTHANOREQUALB=ILI_TOPIC+".ClassLessThanOrEqualB";
	private final static String ILI_CLASSLESSTHANOREQUALC=ILI_TOPIC+".ClassLessThanOrEqualC";
	// ATTRIBUTES GREATER OR EQUAL THAN (<=) SUCCESS AND FAIL
	private final static String ILI_CLASSDEFINEDA=ILI_TOPIC+".ClassDefinedA";
	private final static String ILI_CLASSDEFINEDB=ILI_TOPIC+".ClassDefinedB";
	// ATTRIBUTES FORMATTED TYPE EQUAL (==) SUCCESS AND FAIL
	private final static String ILI_CLASSFORMATTEDTYPEA=ILI_TOPIC+".ClassFormattedTypeA";
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
	public void mandatoryConstraintConstantTrueOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCONSTANTA, OBJ_OID1);
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
	public void mandatoryConstraintConstantNegationOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCONSTANTB, OBJ_OID1);
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
	public void mandatoryConstraintConstantDefinedOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCONSTANTD, OBJ_OID1);
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
	public void mandatoryConstraintConstantANDOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCONSTANTE, OBJ_OID1);
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
	public void mandatoryConstraintConstantOROk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCONSTANTF, OBJ_OID1);
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
	public void mandatoryConstraintConstantEqualOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCONSTANTG, OBJ_OID1);
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
	
	// test in equal (==)
	@Test
	public void mandatoryConstraintEqualationBooleanOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONI, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "true");
		iomObjA.setattrvalue("attr2", "true");
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
	public void mandatoryConstraintEqualationTextOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONG, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "Bernhard");
		iomObjA.setattrvalue("attr2", "Bernhard");
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
	public void mandatoryConstraintEqualationNumericOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONF, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "5");
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
	public void mandatoryConstraintEnumerationEqualityOk(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSEQUALATIONH, OBJ_OID1);
		objValue.setattrvalue("aufzaehlung1", "null");
		objValue.setattrvalue("aufzaehlung2", "null");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in equal (==).
	@Test
	public void mandatoryConstraintEnumerationEqualitySubValueOk(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSEQUALATIONH, OBJ_OID1);
		objValue.setattrvalue("aufzaehlung1", "mehr.vier");
		objValue.setattrvalue("aufzaehlung2", "mehr.vier");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	
	// test in equal (==)
	@Test
	public void mandatoryConstraintEqualityCoordTypeOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONE, OBJ_OID1);
		IomObject coordValue=iomObjA.addattrobj("attr1", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "70000.000");
		IomObject coordValue2=iomObjA.addattrobj("attr2", "COORD");
		coordValue2.setattrvalue("C1", "480000.000");
		coordValue2.setattrvalue("C2", "70000.000");
		IomObject coordValue3=iomObjA.addattrobj("attr3", "COORD");
		coordValue3.setattrvalue("C1", "480000.000");
		coordValue3.setattrvalue("C2", "70000.000");
		IomObject coordValue4=iomObjA.addattrobj("attr4", "COORD");
		coordValue4.setattrvalue("C1", "480000.000");
		coordValue4.setattrvalue("C2", "70000.000");
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
	
	// test in equal (==)
	@Test
	public void mandatoryConstraintEqualityPolylineTypeStraights2dOk(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSEQUALATIOND, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("straights2d1", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coordStart=segments.addattrobj("segment", "COORD");
		IomObject coordEnd=segments.addattrobj("segment", "COORD");
		coordStart.setattrvalue("C1", "480000.000");
		coordStart.setattrvalue("C2", "70000.000");
		coordEnd.setattrvalue("C1", "480010.000");
		coordEnd.setattrvalue("C2", "70000.000");
		IomObject polylineValue2=objStraightsSuccess.addattrobj("straights2d2", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject coordStart2=segments2.addattrobj("segment", "COORD");
		IomObject coordEnd2=segments2.addattrobj("segment", "COORD");
		coordStart2.setattrvalue("C1", "480000.000");
		coordStart2.setattrvalue("C2", "70000.000");
		coordEnd2.setattrvalue("C1", "480010.000");
		coordEnd2.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in equal (==)
	@Test
	public void mandatoryConstraintEqualityPolylineTypeSTRAIGHTSARCS3DOk(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSEQUALATIONA, OBJ_OID1);
		// line 1
		IomObject polylineValue=objStraightsSuccess.addattrobj("straightsarcs1", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "481000.000");
		endSegment.setattrvalue("C2", "70000.000");
		endSegment.setattrvalue("C3", "5000.000");
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "480000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "482000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		arcSegment.setattrvalue("C3", "5000.000");
		// line 2
		IomObject polylineValue2=objStraightsSuccess.addattrobj("arcsstraights2", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "480000.000");
		startSegment2.setattrvalue("C2", "70000.000");
		startSegment2.setattrvalue("C3", "5000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "481000.000");
		endSegment2.setattrvalue("C2", "70000.000");
		endSegment2.setattrvalue("C3", "5000.000");
		IomObject arcSegment2=segments2.addattrobj("segment", "ARC");
		arcSegment2.setattrvalue("A1", "480000.000");
		arcSegment2.setattrvalue("A2", "300000.000");
		arcSegment2.setattrvalue("C1", "482000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		arcSegment2.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in equal (==)
	@Test
	public void mandatoryConstraintEqualitySurfaceTwice2DOk(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSEQUALATIONB, OBJ_OID1);
		// surface 1
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface1", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline 1
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		// polyline 2
		IomObject polylineValue11 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments11=polylineValue11.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment11=segments11.addattrobj("segment", "COORD");
		startSegment11.setattrvalue("C1", "500000.000");
		startSegment11.setattrvalue("C2", "80000.000");
		IomObject endSegment11=segments11.addattrobj("segment", "COORD");
		endSegment11.setattrvalue("C1", "520000.000");
		endSegment11.setattrvalue("C2", "85000.000");
		// arc 1
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "520000.000");
		startSegment3.setattrvalue("C2", "85000.000");
		IomObject arcSegment3=segments3.addattrobj("segment", "ARC");
		arcSegment3.setattrvalue("A1", "485000.000");
		arcSegment3.setattrvalue("A2", "70000.000");
		arcSegment3.setattrvalue("C1", "480000.000");
		arcSegment3.setattrvalue("C2", "70000.000");
		// surface 2
		IomObject multisurfaceValue2=objSurfaceSuccess.addattrobj("surface2", "MULTISURFACE");
		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		// 1. polyline
		IomObject polylineValue1 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments1=polylineValue1.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment1=segments1.addattrobj("segment", "COORD");
		startSegment1.setattrvalue("C1", "480000.000");
		startSegment1.setattrvalue("C2", "70000.000");
		IomObject endSegment1=segments1.addattrobj("segment", "COORD");
		endSegment1.setattrvalue("C1", "500000.000");
		endSegment1.setattrvalue("C2", "80000.000");
		// 2. polyline
		IomObject polylineValue12 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments12=polylineValue12.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment12=segments12.addattrobj("segment", "COORD");
		startSegment12.setattrvalue("C1", "500000.000");
		startSegment12.setattrvalue("C2", "80000.000");
		IomObject endSegment12=segments12.addattrobj("segment", "COORD");
		endSegment12.setattrvalue("C1", "520000.000");
		endSegment12.setattrvalue("C2", "85000.000");
		// arc 1
		IomObject arcValue=outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject arcSegments2=arcValue.addattrobj("sequence", "SEGMENTS");
		IomObject arcStartSegment2=arcSegments2.addattrobj("segment", "COORD");
		arcStartSegment2.setattrvalue("C1", "520000.000");
		arcStartSegment2.setattrvalue("C2", "85000.000");
		IomObject arcSegment2=arcSegments2.addattrobj("segment", "ARC");
		arcSegment2.setattrvalue("A1", "485000.000");
		arcSegment2.setattrvalue("A2", "70000.000");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in equal (==)
	@Test
	public void mandatoryConstraintEqualityAreaTwice2DOk(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSEQUALATIONC, OBJ_OID1);
		// surface 1
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area1", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline 1
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		// polyline 2
		IomObject polylineValue11 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments11=polylineValue11.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment11=segments11.addattrobj("segment", "COORD");
		startSegment11.setattrvalue("C1", "500000.000");
		startSegment11.setattrvalue("C2", "80000.000");
		IomObject endSegment11=segments11.addattrobj("segment", "COORD");
		endSegment11.setattrvalue("C1", "520000.000");
		endSegment11.setattrvalue("C2", "85000.000");
		// arc 1
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "520000.000");
		startSegment3.setattrvalue("C2", "85000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "ARC");
		endSegment3.setattrvalue("A1", "485000.000");
		endSegment3.setattrvalue("A2", "70000.000");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// surface 2
		IomObject multisurfaceValue2=objSurfaceSuccess.addattrobj("area2", "MULTISURFACE");
		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		// 1. polyline
		IomObject polylineValue1 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments1=polylineValue1.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment1=segments1.addattrobj("segment", "COORD");
		startSegment1.setattrvalue("C1", "480000.000");
		startSegment1.setattrvalue("C2", "70000.000");
		IomObject endSegment1=segments1.addattrobj("segment", "COORD");
		endSegment1.setattrvalue("C1", "500000.000");
		endSegment1.setattrvalue("C2", "80000.000");
		// 2. polyline
		IomObject polylineValue12 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments12=polylineValue12.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment12=segments12.addattrobj("segment", "COORD");
		startSegment12.setattrvalue("C1", "500000.000");
		startSegment12.setattrvalue("C2", "80000.000");
		IomObject endSegment12=segments12.addattrobj("segment", "COORD");
		endSegment12.setattrvalue("C1", "520000.000");
		endSegment12.setattrvalue("C2", "85000.000");
		// arc 1
		IomObject arcValue=outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject arcSegments2=arcValue.addattrobj("sequence", "SEGMENTS");
		IomObject arcStartSegment2=arcSegments2.addattrobj("segment", "COORD");
		arcStartSegment2.setattrvalue("C1", "520000.000");
		arcStartSegment2.setattrvalue("C2", "85000.000");
		IomObject arcSegment2=arcSegments2.addattrobj("segment", "ARC");
		arcSegment2.setattrvalue("A1", "485000.000");
		arcSegment2.setattrvalue("A2", "70000.000");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in (!=), (<>).
	@Test
	public void mandatoryConstraintInEqualationTextOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONA, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "Bernhard");
		iomObjA.setattrvalue("attr2", "Hannes");
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

	// test in (!=), (<>).
	@Test
	public void mandatoryConstraintInEqualationNumericOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONB, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "10");
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
	
	// test in (!=), (<>).
	@Test
	public void mandatoryConstraintInEqualationEnumerationOk(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSINEQUALATIONC, OBJ_OID1);
		objValue.setattrvalue("aufzaehlung1", "null");
		objValue.setattrvalue("aufzaehlung2", "eins");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in (!=), (<>).
	@Test
	public void mandatoryConstraintInEqualationEnumerationSubValueOk(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSINEQUALATIONC, OBJ_OID1);
		objValue.setattrvalue("aufzaehlung1", "mehr.vier");
		objValue.setattrvalue("aufzaehlung2", "drei");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	
	// test in (!=), (<>).
	@Test
	public void mandatoryConstraintInEqualationCoordTypeOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIOND, OBJ_OID1);
		IomObject coordValue=iomObjA.addattrobj("attr1", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "70000.000");
		IomObject coordValue2=iomObjA.addattrobj("attr2", "COORD");
		coordValue2.setattrvalue("C1", "480000.000");
		coordValue2.setattrvalue("C2", "70000.000");
		IomObject coordValue3=iomObjA.addattrobj("attr3", "COORD");
		coordValue3.setattrvalue("C1", "480000.000");
		coordValue3.setattrvalue("C2", "70000.000");
		IomObject coordValue4=iomObjA.addattrobj("attr4", "COORD");
		coordValue4.setattrvalue("C1", "480000.000");
		coordValue4.setattrvalue("C2", "88888.000");
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
	// test in (!=), (<>).
	@Test
	public void mandatoryConstraintInEqualationPolylineTypeStraights2dOk(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSINEQUALATIONE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("straights2d1", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coordStart=segments.addattrobj("segment", "COORD");
		IomObject coordEnd=segments.addattrobj("segment", "COORD");
		coordStart.setattrvalue("C1", "480000.000");
		coordStart.setattrvalue("C2", "70000.000");
		coordEnd.setattrvalue("C1", "490000.000");
		coordEnd.setattrvalue("C2", "70000.000");
		IomObject polylineValue2=objStraightsSuccess.addattrobj("straights2d2", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject coordStart2=segments2.addattrobj("segment", "COORD");
		IomObject coordEnd2=segments2.addattrobj("segment", "COORD");
		coordStart2.setattrvalue("C1", "490000.000");
		coordStart2.setattrvalue("C2", "80000.000");
		coordEnd2.setattrvalue("C1", "480000.000");
		coordEnd2.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	// test in (!=), (<>).
	@Test
	public void mandatoryConstraintInEqualationPolylineTypeSTRAIGHTSARCS3DOk(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSINEQUALATIONF, OBJ_OID1);
		// line 1
		IomObject polylineValue=objStraightsSuccess.addattrobj("straightsarcs1", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "481000.000");
		endSegment.setattrvalue("C2", "70000.000");
		endSegment.setattrvalue("C3", "5000.000");
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "580000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "480000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		arcSegment.setattrvalue("C3", "5000.000");
		// line 2
		IomObject polylineValue2=objStraightsSuccess.addattrobj("arcsstraights2", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "480000.000");
		startSegment2.setattrvalue("C2", "70000.000");
		startSegment2.setattrvalue("C3", "5000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "481000.000");
		endSegment2.setattrvalue("C2", "70000.000");
		endSegment2.setattrvalue("C3", "5000.000");
		IomObject arcSegment2=segments2.addattrobj("segment", "ARC");
		arcSegment2.setattrvalue("A1", "580000.000");
		arcSegment2.setattrvalue("A2", "300000.000");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "88888.000");
		arcSegment2.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	// test in (!=), (<>).
	@Test
	public void mandatoryConstraintInEqualationSurface2DOk(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSINEQUALATIONG, OBJ_OID1);
		// surface 1
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface1", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline 1
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "550000.000");
		endSegment.setattrvalue("C2", "80000.000");
		// polyline 2
		IomObject polylineValue11 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments11=polylineValue11.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment11=segments11.addattrobj("segment", "COORD");
		startSegment11.setattrvalue("C1", "550000.000");
		startSegment11.setattrvalue("C2", "80000.000");
		IomObject endSegment11=segments11.addattrobj("segment", "COORD");
		endSegment11.setattrvalue("C1", "520000.000");
		endSegment11.setattrvalue("C2", "85000.000");
		// arc 1
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "520000.000");
		startSegment3.setattrvalue("C2", "85000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// surface 2
		IomObject multisurfaceValue2=objSurfaceSuccess.addattrobj("surface2", "MULTISURFACE");
		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		// 1. polyline
		IomObject polylineValue1 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments1=polylineValue1.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment1=segments1.addattrobj("segment", "COORD");
		startSegment1.setattrvalue("C1", "480000.000");
		startSegment1.setattrvalue("C2", "70000.000");
		IomObject endSegment1=segments1.addattrobj("segment", "COORD");
		endSegment1.setattrvalue("C1", "500000.000");
		endSegment1.setattrvalue("C2", "80000.000");
		// 2. polyline
		IomObject polylineValue12 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments12=polylineValue12.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment12=segments12.addattrobj("segment", "COORD");
		startSegment12.setattrvalue("C1", "500000.000");
		startSegment12.setattrvalue("C2", "80000.000");
		IomObject endSegment12=segments12.addattrobj("segment", "COORD");
		endSegment12.setattrvalue("C1", "520000.000");
		endSegment12.setattrvalue("C2", "85000.000");
		// arc 1
		IomObject arcValue=outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject arcSegments2=arcValue.addattrobj("sequence", "SEGMENTS");
		IomObject arcStartSegment2=arcSegments2.addattrobj("segment", "COORD");
		arcStartSegment2.setattrvalue("C1", "520000.000");
		arcStartSegment2.setattrvalue("C2", "85000.000");
		IomObject arcSegment2=arcSegments2.addattrobj("segment", "COORD");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	// test in (!=), (<>).
	@Test
	public void mandatoryConstraintInEqualationArea2DOk(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSINEQUALATIONH, OBJ_OID1);
		// surface 1
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area1", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline 1
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "550000.000");
		endSegment.setattrvalue("C2", "80000.000");
		// polyline 2
		IomObject polylineValue11 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments11=polylineValue11.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment11=segments11.addattrobj("segment", "COORD");
		startSegment11.setattrvalue("C1", "550000.000");
		startSegment11.setattrvalue("C2", "80000.000");
		IomObject endSegment11=segments11.addattrobj("segment", "COORD");
		endSegment11.setattrvalue("C1", "520000.000");
		endSegment11.setattrvalue("C2", "85000.000");
		// arc 1
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "520000.000");
		startSegment3.setattrvalue("C2", "85000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// surface 2
		IomObject multisurfaceValue2=objSurfaceSuccess.addattrobj("area2", "MULTISURFACE");
		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		// 1. polyline
		IomObject polylineValue1 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments1=polylineValue1.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment1=segments1.addattrobj("segment", "COORD");
		startSegment1.setattrvalue("C1", "480000.000");
		startSegment1.setattrvalue("C2", "70000.000");
		IomObject endSegment1=segments1.addattrobj("segment", "COORD");
		endSegment1.setattrvalue("C1", "500000.000");
		endSegment1.setattrvalue("C2", "80000.000");
		// 2. polyline
		IomObject polylineValue12 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments12=polylineValue12.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment12=segments12.addattrobj("segment", "COORD");
		startSegment12.setattrvalue("C1", "500000.000");
		startSegment12.setattrvalue("C2", "80000.000");
		IomObject endSegment12=segments12.addattrobj("segment", "COORD");
		endSegment12.setattrvalue("C1", "520000.000");
		endSegment12.setattrvalue("C2", "85000.000");
		// arc 1
		IomObject arcValue=outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject arcSegments2=arcValue.addattrobj("sequence", "SEGMENTS");
		IomObject arcStartSegment2=arcSegments2.addattrobj("segment", "COORD");
		arcStartSegment2.setattrvalue("C1", "520000.000");
		arcStartSegment2.setattrvalue("C2", "85000.000");
		IomObject arcSegment2=arcSegments2.addattrobj("segment", "COORD");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in attr boolean
	@Test
	public void mandatoryConstraintInEqualationBooleanOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONI, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "true");
		iomObjA.setattrvalue("attr2", "false");
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
	
	// test in (>).
	@Test
	public void mandatoryConstraintGreaterThanNumericOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANA, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "6");
		iomObjA.setattrvalue("attr2", "5");
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
	
	// test in (>).
	@Test
	public void mandatoryConstraintGreaterThanEnumerationOk(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSGREATERTHANB, OBJ_OID1);
		objValue.setattrvalue("aufzaehlung1", "eins");
		objValue.setattrvalue("aufzaehlung2", "null");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in (>).
	@Test
	public void mandatoryConstraintGreaterThanTextOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANC, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "Aaaab");
		iomObjA.setattrvalue("attr2", "Aaaaa");
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
	
	// test in (<).
	@Test
	public void mandatoryConstraintLessThanNumericOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANA, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "4");
		iomObjA.setattrvalue("attr2", "6");
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
	
	// test in (<).
	@Test
	public void mandatoryConstraintLessThanEnumerationOk(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSLESSTHANB, OBJ_OID1);
		objValue.setattrvalue("aufzaehlung1", "eins");
		objValue.setattrvalue("aufzaehlung2", "drei");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in (<).
	@Test
	public void mandatoryConstraintLessThanTextOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANC, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "Aaaaa");
		iomObjA.setattrvalue("attr2", "Aaaab");
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
	
	// test in (>=).
	@Test
	public void mandatoryConstraintGreaterThanOrEqualNumericOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALA, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "6");
		iomObjA.setattrvalue("attr2", "5");
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
	
	// test in (>=).
	@Test
	public void mandatoryConstraintGreaterThanOrEqualNumericEqualOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALA, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "6");
		iomObjA.setattrvalue("attr2", "6");
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
	
	// test in (>=).
	@Test
	public void mandatoryConstraintGreaterThanOrEqualEnumerationOk(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALB, OBJ_OID1);
		objValue.setattrvalue("aufzaehlung1", "eins");
		objValue.setattrvalue("aufzaehlung2", "null");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in (>=).
	@Test
	public void mandatoryConstraintGreaterThanOrEqualEnumerationEqualOk(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALB, OBJ_OID1);
		objValue.setattrvalue("aufzaehlung1", "null");
		objValue.setattrvalue("aufzaehlung2", "null");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in (>=).
	@Test
	public void mandatoryConstraintGreaterThanOrEqualTextOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALC, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "Aaaab");
		iomObjA.setattrvalue("attr2", "Aaaaa");
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
	
	// test in (>=).
	@Test
	public void mandatoryConstraintGreaterThanOrEqualTextEqualOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALC, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "Aaaab");
		iomObjA.setattrvalue("attr2", "Aaaab");
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
	
	// test in (<=).
	@Test
	public void mandatoryConstraintLessThanOrEqualNumericOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANOREQUALA, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "4");
		iomObjA.setattrvalue("attr2", "6");
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
	
	// test in (<=).
	@Test
	public void mandatoryConstraintLessThanOrEqualNumericEqualOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANOREQUALA, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "6");
		iomObjA.setattrvalue("attr2", "6");
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
	
	// test in (<=).
	@Test
	public void mandatoryConstraintLessThanOrEqualEnumerationOk(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSLESSTHANOREQUALB, OBJ_OID1);
		objValue.setattrvalue("aufzaehlung1", "eins");
		objValue.setattrvalue("aufzaehlung2", "drei");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in (<=).
	@Test
	public void mandatoryConstraintLessThanOrEqualEnumerationEqualOk(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSLESSTHANOREQUALB, OBJ_OID1);
		objValue.setattrvalue("aufzaehlung1", "drei");
		objValue.setattrvalue("aufzaehlung2", "drei");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in (<=).
	@Test
	public void mandatoryConstraintLessThanOrEqualTextOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANOREQUALC, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "Aaaaa");
		iomObjA.setattrvalue("attr2", "Aaaab");
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
	
	// test in (<=).
	@Test
	public void mandatoryConstraintLessThanOrEqualTextEqualOk(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANOREQUALC, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "Aaaab");
		iomObjA.setattrvalue("attr2", "Aaaab");
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
	
	// test in defined (defined)
	@Test
	public void mandatoryConstraintDefinedPolylineTypeStraights2dOk(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSDEFINEDA, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("straightsarcs1", "POLYLINE");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in formattedtype defined (defined)
	@Test
	public void mandatoryConstraintEqualationFormattedTypeOk(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSFORMATTEDTYPEA, OBJ_OID1);
		objStraightsSuccess.setattrvalue("attr1", "2005-12-31T23:59:59.999");
		objStraightsSuccess.setattrvalue("attr2", "2005-12-31T23:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void mandatoryConstraintSomeDifferentExpressions_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_TOPIC+".ClassDiffExpressions", OBJ_OID1);
		//objStraightsSuccess.setattrvalue("Geometrie_Punkt", "true");
		objStraightsSuccess.setattrvalue("Geometrie_Polygon", "true");
		//objStraightsSuccess.setattrvalue("Geometrie_Linie", "true");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	//#########################################################//
	//########### FAIL MANDATORY CONSTRAINTS ##################//
	//#########################################################//
	
	// test in equal (==)
	@Test
	public void mandatoryConstraintEqualationBooleanFalse(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONI, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "true");
		iomObjA.setattrvalue("attr2", "false");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in equal (==).
	@Test
	public void mandatoryConstraintEqualationTextFail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONG, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "Bernhard");
		iomObjA.setattrvalue("attr2", "Albert");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in equal (==).
	@Test
	public void mandatoryConstraintEqualationNumericFail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONF, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "4");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in attr boolean
	@Test
	public void mandatoryConstraintEqualationCoordValue4False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONE, OBJ_OID1);
		IomObject coordValue=iomObjA.addattrobj("attr1", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "70000.000");
		IomObject coordValue2=iomObjA.addattrobj("attr2", "COORD");
		coordValue2.setattrvalue("C1", "480000.000");
		coordValue2.setattrvalue("C2", "70000.000");
		IomObject coordValue3=iomObjA.addattrobj("attr3", "COORD");
		coordValue3.setattrvalue("C1", "480000.000");
		coordValue3.setattrvalue("C2", "70000.000");
		IomObject coordValue4=iomObjA.addattrobj("attr4", "COORD");
		coordValue4.setattrvalue("C1", "480000.000");
		coordValue4.setattrvalue("C2", "88888.888");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void mandatoryConstraintEqualationPolyline2dFalse(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSEQUALATIOND, OBJ_OID1);
		// polyline 1
		IomObject polylineValue=objStraightsSuccess.addattrobj("straights2d1", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "500000.000");
		startSegment.setattrvalue("C2", "80000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "550000.000");
		endSegment.setattrvalue("C2", "90000.000");
		// polyline 2
		IomObject polylineValue2=objStraightsSuccess.addattrobj("straights2d2", "POLYLINE");
		IomObject segments1=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment1=segments1.addattrobj("segment", "COORD");
		startSegment1.setattrvalue("C1", "480000.000");
		startSegment1.setattrvalue("C2", "70000.000");
		IomObject endSegment1=segments1.addattrobj("segment", "COORD");
		endSegment1.setattrvalue("C1", "500000.000");
		endSegment1.setattrvalue("C2", "80000.000");
		// arc 1
		IomObject arcValue=objStraightsSuccess.addattrobj("arcs2d1", "POLYLINE");
		IomObject segments2=arcValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "500000.000");
		startSegment2.setattrvalue("C2", "80000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "ARC");
		endSegment2.setattrvalue("A1", "530000.000");
		endSegment2.setattrvalue("A2", "90000.000");
		endSegment2.setattrvalue("C1", "550000.000");
		endSegment2.setattrvalue("C2", "90000.000");
		// arc 1
		IomObject arcValue2=objStraightsSuccess.addattrobj("arcs2d2", "POLYLINE");
		IomObject segments3=arcValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "500000.000");
		startSegment3.setattrvalue("C2", "80000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "ARC");
		endSegment3.setattrvalue("A1", "530000.000");
		endSegment3.setattrvalue("A2", "90000.000");
		endSegment3.setattrvalue("C1", "550000.000");
		endSegment3.setattrvalue("C2", "90000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void mandatoryConstraintEqualationPolyline2ARCS3DFalse(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSEQUALATIONA, OBJ_OID1);
		// line 1
		IomObject polylineValue=objStraightsSuccess.addattrobj("straightsarcs1", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "481000.000");
		endSegment.setattrvalue("C2", "70000.000");
		endSegment.setattrvalue("C3", "5000.000");
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "580000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "480000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		arcSegment.setattrvalue("C3", "5000.000");
		// line 2
		IomObject polylineValue2=objStraightsSuccess.addattrobj("arcsstraights2", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "480000.000");
		startSegment2.setattrvalue("C2", "70000.000");
		startSegment2.setattrvalue("C3", "5000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "481000.000");
		endSegment2.setattrvalue("C2", "70000.000");
		endSegment2.setattrvalue("C3", "5000.000");
		IomObject arcSegment2=segments2.addattrobj("segment", "ARC");
		arcSegment2.setattrvalue("A1", "580000.000");
		arcSegment2.setattrvalue("A2", "299999.999");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		arcSegment2.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void mandatoryConstraintEqualation2DSurfaceFalse(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSEQUALATIONB, OBJ_OID1);
		// surface 1
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface1", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline 1
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "490000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "550000.000");
		endSegment.setattrvalue("C2", "80000.000");
		// polyline 2
		IomObject polylineValue11 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments11=polylineValue11.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment11=segments11.addattrobj("segment", "COORD");
		startSegment11.setattrvalue("C1", "550000.000");
		startSegment11.setattrvalue("C2", "80000.000");
		IomObject endSegment11=segments11.addattrobj("segment", "COORD");
		endSegment11.setattrvalue("C1", "520000.000");
		endSegment11.setattrvalue("C2", "85000.000");
		// arc 1
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "520000.000");
		startSegment3.setattrvalue("C2", "85000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "COORD");
		endSegment3.setattrvalue("C1", "490000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// surface 2
		IomObject multisurfaceValue2=objSurfaceSuccess.addattrobj("surface2", "MULTISURFACE");
		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		// 1. polyline
		IomObject polylineValue1 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments1=polylineValue1.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment1=segments1.addattrobj("segment", "COORD");
		startSegment1.setattrvalue("C1", "480000.000");
		startSegment1.setattrvalue("C2", "70000.000");
		IomObject endSegment1=segments1.addattrobj("segment", "COORD");
		endSegment1.setattrvalue("C1", "500000.000");
		endSegment1.setattrvalue("C2", "80000.000");
		// 2. polyline
		IomObject polylineValue12 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments12=polylineValue12.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment12=segments12.addattrobj("segment", "COORD");
		startSegment12.setattrvalue("C1", "500000.000");
		startSegment12.setattrvalue("C2", "80000.000");
		IomObject endSegment12=segments12.addattrobj("segment", "COORD");
		endSegment12.setattrvalue("C1", "520000.000");
		endSegment12.setattrvalue("C2", "85000.000");
		// arc 1
		IomObject arcValue=outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject arcSegments2=arcValue.addattrobj("sequence", "SEGMENTS");
		IomObject arcStartSegment2=arcSegments2.addattrobj("segment", "COORD");
		arcStartSegment2.setattrvalue("C1", "520000.000");
		arcStartSegment2.setattrvalue("C2", "85000.000");
		IomObject arcSegment2=arcSegments2.addattrobj("segment", "COORD");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void mandatoryConstraintEqualationArea2DValueFalse(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSEQUALATIONC, OBJ_OID1);
		// surface 1
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area1", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline 1
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "510000.000");
		endSegment.setattrvalue("C2", "81000.000");
		// polyline 2
		IomObject polylineValue11 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments11=polylineValue11.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment11=segments11.addattrobj("segment", "COORD");
		startSegment11.setattrvalue("C1", "510000.000");
		startSegment11.setattrvalue("C2", "81000.000");
		IomObject endSegment11=segments11.addattrobj("segment", "COORD");
		endSegment11.setattrvalue("C1", "520000.000");
		endSegment11.setattrvalue("C2", "85000.000");
		// arc 1
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "520000.000");
		startSegment3.setattrvalue("C2", "85000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "ARC");
		endSegment3.setattrvalue("A1", "580000.000");
		endSegment3.setattrvalue("A2", "70000.000");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// surface 2
		IomObject multisurfaceValue2=objSurfaceSuccess.addattrobj("area2", "MULTISURFACE");
		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		// 1. polyline
		IomObject polylineValue1 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments1=polylineValue1.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment1=segments1.addattrobj("segment", "COORD");
		startSegment1.setattrvalue("C1", "480000.000");
		startSegment1.setattrvalue("C2", "70000.000");
		IomObject endSegment1=segments1.addattrobj("segment", "COORD");
		endSegment1.setattrvalue("C1", "500000.000");
		endSegment1.setattrvalue("C2", "80000.000");
		// 2. polyline
		IomObject polylineValue12 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments12=polylineValue12.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment12=segments12.addattrobj("segment", "COORD");
		startSegment12.setattrvalue("C1", "500000.000");
		startSegment12.setattrvalue("C2", "80000.000");
		IomObject endSegment12=segments12.addattrobj("segment", "COORD");
		endSegment12.setattrvalue("C1", "520000.000");
		endSegment12.setattrvalue("C2", "85000.000");
		// arc 1
		IomObject arcValue=outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject arcSegments2=arcValue.addattrobj("sequence", "SEGMENTS");
		IomObject arcStartSegment2=arcSegments2.addattrobj("segment", "COORD");
		arcStartSegment2.setattrvalue("C1", "520000.000");
		arcStartSegment2.setattrvalue("C2", "85000.000");
		IomObject arcSegment2=arcSegments2.addattrobj("segment", "ARC");
		arcSegment2.setattrvalue("A1", "580000.000");
		arcSegment2.setattrvalue("A2", "70000.000");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (!=), (<>).
	@Test
	public void mandatoryConstraintInEqualationTextFalse(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONA, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "Bernhard");
		iomObjA.setattrvalue("attr2", "Bernhard");
		ValidationConfig modelConfig=new ValidationConfig();
//		modelConfig.setConfigValue("MandatoryConstraints23.Topic.ClassInEqualationA.attr1", ValidationConfig.CHECK,ValidationConfig.OFF);
//		modelConfig.setConfigValue("MandatoryConstraints23.Topic.ClassInEqualationA.attr2", ValidationConfig.CHECK,ValidationConfig.OFF);
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}

	// test in (!=), (<>).
	@Test
	public void unequalNumbersEXP_SameNumbersACT_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONB, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "5");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (!=), (<>).
	@Test
	public void mandatoryConstraintInEqualationEnumerationFalse(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSINEQUALATIONC, OBJ_OID1);
		objValue.setattrvalue("aufzaehlung1", "null");
		objValue.setattrvalue("aufzaehlung2", "null");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (!=), (<>).
	@Test
	public void mandatoryConstraintInEqualationEnumerationSubValueFalse(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSINEQUALATIONC, OBJ_OID1);
		objValue.setattrvalue("aufzaehlung1", "mehr.vier");
		objValue.setattrvalue("aufzaehlung2", "mehr.vier");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (!=), (<>).
	@Test
	public void mandatoryConstraintInEqualationCoordTypeFalse(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIOND, OBJ_OID1);
		IomObject coordValue=iomObjA.addattrobj("attr1", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "70000.000");
		IomObject coordValue2=iomObjA.addattrobj("attr2", "COORD");
		coordValue2.setattrvalue("C1", "480000.000");
		coordValue2.setattrvalue("C2", "70000.000");
		IomObject coordValue3=iomObjA.addattrobj("attr3", "COORD");
		coordValue3.setattrvalue("C1", "480000.000");
		coordValue3.setattrvalue("C2", "70000.000");
		IomObject coordValue4=iomObjA.addattrobj("attr4", "COORD");
		coordValue4.setattrvalue("C1", "480000.000");
		coordValue4.setattrvalue("C2", "70000.000");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	// test in (!=), (<>).
	@Test
	public void mandatoryConstraintInEqualationPolylineTypeStraights2dFalse(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSINEQUALATIONE, OBJ_OID1);
		// polyline 1
		IomObject polylineValue=objStraightsSuccess.addattrobj("straights2d1", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		// polyline 1
		IomObject polylineValue2=objStraightsSuccess.addattrobj("straights2d2", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "480000.000");
		startSegment2.setattrvalue("C2", "70000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "500000.000");
		endSegment2.setattrvalue("C2", "80000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	// test in (!=), (<>).
	@Test
	public void mandatoryConstraintInEqualationPolylineTypeSTRAIGHTSARCS3DFalse(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSINEQUALATIONF, OBJ_OID1);
		// line 1
		IomObject polylineValue=objStraightsSuccess.addattrobj("straightsarcs1", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "490000.000");
		endSegment.setattrvalue("C2", "70000.000");
		endSegment.setattrvalue("C3", "5000.000");
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "500000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "480000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		arcSegment.setattrvalue("C3", "5000.000");
		// line 2
		IomObject polylineValue2=objStraightsSuccess.addattrobj("arcsstraights2", "POLYLINE");
		IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment2=segments2.addattrobj("segment", "COORD");
		startSegment2.setattrvalue("C1", "480000.000");
		startSegment2.setattrvalue("C2", "70000.000");
		startSegment2.setattrvalue("C3", "5000.000");
		IomObject endSegment2=segments2.addattrobj("segment", "COORD");
		endSegment2.setattrvalue("C1", "490000.000");
		endSegment2.setattrvalue("C2", "70000.000");
		endSegment2.setattrvalue("C3", "5000.000");
		IomObject arcSegment2=segments2.addattrobj("segment", "ARC");
		arcSegment2.setattrvalue("A1", "500000.000");
		arcSegment2.setattrvalue("A2", "300000.000");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		arcSegment2.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	// test in (!=), (<>).
	@Test
	public void mandatoryConstraintInEqualationSurface2DFalse(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSINEQUALATIONG, OBJ_OID1);
		// surface 1
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface1", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline 1
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		// polyline 2
		IomObject polylineValue11 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments11=polylineValue11.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment11=segments11.addattrobj("segment", "COORD");
		startSegment11.setattrvalue("C1", "500000.000");
		startSegment11.setattrvalue("C2", "80000.000");
		IomObject endSegment11=segments11.addattrobj("segment", "COORD");
		endSegment11.setattrvalue("C1", "520000.000");
		endSegment11.setattrvalue("C2", "85000.000");
		// arc 1
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "520000.000");
		startSegment3.setattrvalue("C2", "85000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "ARC");
		endSegment3.setattrvalue("A1", "580000.000");
		endSegment3.setattrvalue("A2", "70000.000");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// surface 2
		IomObject multisurfaceValue2=objSurfaceSuccess.addattrobj("surface2", "MULTISURFACE");
		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		// 1. polyline
		IomObject polylineValue1 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments1=polylineValue1.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment1=segments1.addattrobj("segment", "COORD");
		startSegment1.setattrvalue("C1", "480000.000");
		startSegment1.setattrvalue("C2", "70000.000");
		IomObject endSegment1=segments1.addattrobj("segment", "COORD");
		endSegment1.setattrvalue("C1", "500000.000");
		endSegment1.setattrvalue("C2", "80000.000");
		// 2. polyline
		IomObject polylineValue12 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments12=polylineValue12.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment12=segments12.addattrobj("segment", "COORD");
		startSegment12.setattrvalue("C1", "500000.000");
		startSegment12.setattrvalue("C2", "80000.000");
		IomObject endSegment12=segments12.addattrobj("segment", "COORD");
		endSegment12.setattrvalue("C1", "520000.000");
		endSegment12.setattrvalue("C2", "85000.000");
		// arc 1
		IomObject arcValue=outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject arcSegments2=arcValue.addattrobj("sequence", "SEGMENTS");
		IomObject arcStartSegment2=arcSegments2.addattrobj("segment", "COORD");
		arcStartSegment2.setattrvalue("C1", "520000.000");
		arcStartSegment2.setattrvalue("C2", "85000.000");
		IomObject arcSegment2=arcSegments2.addattrobj("segment", "ARC");
		arcSegment2.setattrvalue("A1", "580000.000");
		arcSegment2.setattrvalue("A2", "70000.000");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	// test in (!=), (<>).
	@Test
	public void mandatoryConstraintInEqualationArea2DFalse(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSINEQUALATIONH, OBJ_OID1);
		// surface 1
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area1", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// polyline 1
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		// polyline 2
		IomObject polylineValue11 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments11=polylineValue11.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment11=segments11.addattrobj("segment", "COORD");
		startSegment11.setattrvalue("C1", "500000.000");
		startSegment11.setattrvalue("C2", "80000.000");
		IomObject endSegment11=segments11.addattrobj("segment", "COORD");
		endSegment11.setattrvalue("C1", "520000.000");
		endSegment11.setattrvalue("C2", "85000.000");
		// arc 1
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "520000.000");
		startSegment3.setattrvalue("C2", "85000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "ARC");
		endSegment3.setattrvalue("A1", "580000.000");
		endSegment3.setattrvalue("A2", "70000.000");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		// surface 2
		IomObject multisurfaceValue2=objSurfaceSuccess.addattrobj("area2", "MULTISURFACE");
		IomObject surfaceValue2 = multisurfaceValue2.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue2.addattrobj("boundary", "BOUNDARY");
		// 1. polyline
		IomObject polylineValue1 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments1=polylineValue1.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment1=segments1.addattrobj("segment", "COORD");
		startSegment1.setattrvalue("C1", "480000.000");
		startSegment1.setattrvalue("C2", "70000.000");
		IomObject endSegment1=segments1.addattrobj("segment", "COORD");
		endSegment1.setattrvalue("C1", "500000.000");
		endSegment1.setattrvalue("C2", "80000.000");
		// 2. polyline
		IomObject polylineValue12 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments12=polylineValue12.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment12=segments12.addattrobj("segment", "COORD");
		startSegment12.setattrvalue("C1", "500000.000");
		startSegment12.setattrvalue("C2", "80000.000");
		IomObject endSegment12=segments12.addattrobj("segment", "COORD");
		endSegment12.setattrvalue("C1", "520000.000");
		endSegment12.setattrvalue("C2", "85000.000");
		// arc 1
		IomObject arcValue=outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject arcSegments2=arcValue.addattrobj("sequence", "SEGMENTS");
		IomObject arcStartSegment2=arcSegments2.addattrobj("segment", "COORD");
		arcStartSegment2.setattrvalue("C1", "520000.000");
		arcStartSegment2.setattrvalue("C2", "85000.000");
		IomObject arcSegment2=arcSegments2.addattrobj("segment", "ARC");
		arcSegment2.setattrvalue("A1", "580000.000");
		arcSegment2.setattrvalue("A2", "70000.000");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in attr boolean
	@Test
	public void mandatoryConstraintInEqualationBooleanFalse(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONI, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "true");
		iomObjA.setattrvalue("attr2", "true");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (>).
	@Test
	public void mandatoryConstraintGreaterThanNumericFalse(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANA, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "6");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (>).
	@Test
	public void mandatoryConstraintGreaterThanNumericEqualFalse(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANA, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "6");
		iomObjA.setattrvalue("attr2", "6");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (>).
	@Test
	public void mandatoryConstraintGreaterThanEnumerationFalse(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSGREATERTHANB, OBJ_OID1);
		objValue.setattrvalue("aufzaehlung1", "null");
		objValue.setattrvalue("aufzaehlung2", "eins");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (>).
	@Test
	public void mandatoryConstraintGreaterThanEnumerationEqualFalse(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSGREATERTHANB, OBJ_OID1);
		objValue.setattrvalue("aufzaehlung1", "null");
		objValue.setattrvalue("aufzaehlung2", "null");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (>).
	@Test
	public void mandatoryConstraintGreaterThanTextFalse(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANC, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "Aaaaa");
		iomObjA.setattrvalue("attr2", "Aaaab");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (>).
	@Test
	public void mandatoryConstraintGreaterThanTextEqualFalse(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANC, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "Aaaab");
		iomObjA.setattrvalue("attr2", "Aaaab");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (<).
	@Test
	public void mandatoryConstraintLessThanNumericFalse(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANA, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "6");
		iomObjA.setattrvalue("attr2", "4");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (<).
	@Test
	public void mandatoryConstraintLessThanNumericEqualFalse(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANA, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "6");
		iomObjA.setattrvalue("attr2", "6");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (<).
	@Test
	public void mandatoryConstraintLessThanEnumerationFalse(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSLESSTHANB, OBJ_OID1);
		objValue.setattrvalue("aufzaehlung1", "drei");
		objValue.setattrvalue("aufzaehlung2", "eins");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (<).
	@Test
	public void mandatoryConstraintLessThanEnumerationEqualFalse(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSLESSTHANB, OBJ_OID1);
		objValue.setattrvalue("aufzaehlung1", "drei");
		objValue.setattrvalue("aufzaehlung2", "drei");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (<).
	@Test
	public void mandatoryConstraintLessThanTextFalse(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANC, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "Aaaab");
		iomObjA.setattrvalue("attr2", "Aaaaa");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (<).
	@Test
	public void mandatoryConstraintLessThanTextEqualFalse(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANC, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "Aaaab");
		iomObjA.setattrvalue("attr2", "Aaaab");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (>=).
	@Test
	public void mandatoryConstraintGreaterThanOrEqualNumericFalse(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALA, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "6");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (>=).
	@Test
	public void mandatoryConstraintGreaterThanOrEqualEnumerationFalse(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALB, OBJ_OID1);
		objValue.setattrvalue("aufzaehlung1", "null");
		objValue.setattrvalue("aufzaehlung2", "eins");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (>=).
	@Test
	public void mandatoryConstraintGreaterThanOrEqualTextFalse(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALC, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "Aaaaa");
		iomObjA.setattrvalue("attr2", "Aaaab");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (<=).
	@Test
	public void mandatoryConstraintLessThanOrEqualNumericFalse(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANOREQUALA, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "6");
		iomObjA.setattrvalue("attr2", "4");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (<=).
	@Test
	public void mandatoryConstraintLessThanOrEqualEnumerationFalse(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSLESSTHANOREQUALB, OBJ_OID1);
		objValue.setattrvalue("aufzaehlung1", "drei");
		objValue.setattrvalue("aufzaehlung2", "eins");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in (<=).
	@Test
	public void mandatoryConstraintLessThanOrEqualTextFalse(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANOREQUALC, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "Aaaab");
		iomObjA.setattrvalue("attr2", "Aaaaa");
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in formattedtype defined (defined)
	@Test
	public void mandatoryConstraintEqualationFormattedTypeFail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSFORMATTEDTYPEA, OBJ_OID1);
		objStraightsSuccess.setattrvalue("attr1", "2005-12-31T23:59:59.999");
		objStraightsSuccess.setattrvalue("attr2", "2005-12-31T23:59:59.888");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// test in undefined (undefined)
	@Test
	public void unDefinedAttrEXP_DefinedAttrACT_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSDEFINEDB, OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,START_BASKET_EVENT));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void configOFFMandatoryConstraintEqualationBooleanTrue(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONI, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "true");
		iomObjA.setattrvalue("attr2", "false");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("MandatoryConstraints23.Topic.ClassEqualationI", ValidationConfig.CHECK,ValidationConfig.OFF);
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
	
	@Test
	public void configWARNINGMandatoryConstraintEqualationBooleanFalse(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONI, OBJ_OID1);
		iomObjA.setattrvalue("attr1", "true");
		iomObjA.setattrvalue("attr2", "false");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("MandatoryConstraints23.Topic.ClassEqualationI", ValidationConfig.CHECK,ValidationConfig.WARNING);
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
		assertTrue(logger.getWarn().size()==1);
		assertEquals("Mandatory Constraint Constraint1 is not true.", logger.getWarn().get(0).getEventMsg());
	}
	
}