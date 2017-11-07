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

public class MandatoryConstraints23 {
	private TransferDescription td=null;
	// OID
	private final static String OID ="o1";
	// MODEL.TOPIC
	private final static String TOPIC="MandatoryConstraints23.Topic";
	// CONSTANT SUCCESS
	private final static String ILI_CLASSCONSTANTA=TOPIC+".ClassConstantA";
	private final static String ILI_CLASSCONSTANTB=TOPIC+".ClassConstantB";
	private final static String ILI_CLASSCONSTANTD=TOPIC+".ClassConstantD";
	private final static String ILI_CLASSCONSTANTE=TOPIC+".ClassConstantE";
	private final static String ILI_CLASSCONSTANTF=TOPIC+".ClassConstantF";
	private final static String ILI_CLASSCONSTANTG=TOPIC+".ClassConstantG";
	private final static String ILI_CLASSCONSTANTJ=TOPIC+".ClassConstantJ";
	// ATTRIBUTES EQUALATION (==) SUCCESS AND FAIL
	private final static String ILI_CLASSEQUALATIONA=TOPIC+".ClassEqualA";
	private final static String ILI_CLASSEQUALATIONB=TOPIC+".ClassEqualB";
	private final static String ILI_CLASSEQUALATIONC=TOPIC+".ClassEqualC";
	private final static String ILI_CLASSEQUALATIOND=TOPIC+".ClassEqualD";
	private final static String ILI_CLASSEQUALATIONE=TOPIC+".ClassEqualE";
	private final static String ILI_CLASSEQUALATIONF=TOPIC+".ClassEqualF";
	private final static String ILI_CLASSEQUALATIONF2=TOPIC+".ClassEqualF2";
	private final static String ILI_CLASSEQUALATIONF3=TOPIC+".ClassEqualF3";
	private final static String ILI_CLASSEQUALATIONG=TOPIC+".ClassEqualG";
	private final static String ILI_CLASSEQUALATIONH=TOPIC+".ClassEqualH";
	private final static String ILI_CLASSEQUALATIONI=TOPIC+".ClassEqualI";
	// ATTRIBUTES INEQUALATION (!=), (<>) SUCCESS AND FAIL
	private final static String ILI_CLASSINEQUALATIONA=TOPIC+".ClassUnEqualA";
	private final static String ILI_CLASSINEQUALATIONB=TOPIC+".ClassUnEqualB";
	private final static String ILI_CLASSINEQUALATIONC=TOPIC+".ClassUnEqualC";
	private final static String ILI_CLASSINEQUALATIOND=TOPIC+".ClassUnEqualD";
	private final static String ILI_CLASSINEQUALATIONE=TOPIC+".ClassUnEqualE";
	private final static String ILI_CLASSINEQUALATIONF=TOPIC+".ClassUnEqualF";
	private final static String ILI_CLASSINEQUALATIONG=TOPIC+".ClassUnEqualG";
	private final static String ILI_CLASSINEQUALATIONH=TOPIC+".ClassUnEqualH";
	private final static String ILI_CLASSINEQUALATIONI=TOPIC+".ClassUnEqualI";
	// ATTRIBUTES GREATER THAN (>) SUCCESS AND FAIL
	private final static String ILI_CLASSGREATERTHANA=TOPIC+".ClassGreaterThanA";
	private final static String ILI_CLASSGREATERTHANB=TOPIC+".ClassGreaterThanB";
	private final static String ILI_CLASSGREATERTHANC=TOPIC+".ClassGreaterThanC";
	// ATTRIBUTES SMALLER THAN (<) SUCCESS AND FAIL
	private final static String ILI_CLASSLESSTHANA=TOPIC+".ClassLessThanA";
	private final static String ILI_CLASSLESSTHANB=TOPIC+".ClassLessThanB";
	private final static String ILI_CLASSLESSTHANC=TOPIC+".ClassLessThanC";
	// ATTRIBUTES GREATER OR EQUAL THAN (>=) SUCCESS AND FAIL
	private final static String ILI_CLASSGREATERTHANOREQUALA=TOPIC+".ClassGreaterThanOrEqualA";
	private final static String ILI_CLASSGREATERTHANOREQUALB=TOPIC+".ClassGreaterThanOrEqualB";
	private final static String ILI_CLASSGREATERTHANOREQUALC=TOPIC+".ClassGreaterThanOrEqualC";
	// ATTRIBUTES GREATER OR EQUAL THAN (<=) SUCCESS AND FAIL
	private final static String ILI_CLASSLESSTHANOREQUALA=TOPIC+".ClassLessThanOrEqualA";
	private final static String ILI_CLASSLESSTHANOREQUALB=TOPIC+".ClassLessThanOrEqualB";
	private final static String ILI_CLASSLESSTHANOREQUALC=TOPIC+".ClassLessThanOrEqualC";
	// ATTRIBUTES GREATER OR EQUAL THAN (<=) SUCCESS AND FAIL
	private final static String ILI_CLASSDEFINEDA=TOPIC+".ClassDefinedA";
	private final static String ILI_CLASSDEFINEDB=TOPIC+".ClassDefinedB";
	// ATTRIBUTES FORMATTED TYPE EQUAL (==) SUCCESS AND FAIL
	private final static String ILI_CLASSFORMATTEDTYPEA=TOPIC+".ClassFormattedTypeA";
	// START BASKET EVENT
	private final static String BID="b1";
	
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
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn eine Konstante auf true gesetzt wird.
	@Test
	public void constantTrue_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCONSTANTA, OID);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	@Test
	public void constantEnumerationSub_Ok(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSCONSTANTJ, OID);
		objValue.setattrvalue("aufzaehlung1", "mehr.vier");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn eine Konstante true ergibt oder not false ist.
	@Test
	public void constantNegation_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCONSTANTB, OID);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn eine Konstante definiert ist.
	@Test
	public void constantDefined_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCONSTANTD, OID);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn alle Konstanten die mit AND verbunden sind, true ergeben.
	@Test
	public void constantAND_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCONSTANTE, OID);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn alle Konstanten welche mit OR verbunden sind, true ergeben.
	@Test
	public void constantOR_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCONSTANTF, OID);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Konstante in einem Vergleich zu einer anderen Konstante true ergibt.
	@Test
	public void constantEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCONSTANTG, OID);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die 2 Attribute von boolean in einem Vergleich true sind.
	@Test
	public void booleanEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONI, OID);
		iomObjA.setattrvalue("attr1", "true");
		iomObjA.setattrvalue("attr2", "true");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn diese 2 Strings in einem Vergleich uebereinstimmen.
	@Test
	public void textEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONG, OID);
		iomObjA.setattrvalue("attr1", "Bernhard");
		iomObjA.setattrvalue("attr2", "Bernhard");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die 2 Numerischen Werte uebereinstimmen.
	@Test
	public void numericEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONF, OID);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die 2 Numerischen Werte uebereinstimmen.
	@Test
	public void decEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONF, OID);
		iomObjA.setattrvalue("attr1", "5.0");
		iomObjA.setattrvalue("attr2", "5.0");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}	
	
	@Test
	public void decEqualConstant_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONF2, OID);
		iomObjA.setattrvalue("attr1", "2.0");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}	
	@Test
	public void decDomainEqualConstant_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONF3, OID);
		iomObjA.setattrvalue("attr1", "2.0");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}	
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Aufzaehlungen mit der Unter-Hierarchie uebereinstimmen.
	@Test
	public void subEnumerationEqual_Ok(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSEQUALATIONH, OID);
		objValue.setattrvalue("aufzaehlung1", "mehr.vier");
		objValue.setattrvalue("aufzaehlung2", "mehr.vier");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden Koordinaten uebereinstimmen.
	@Test
	public void coordsEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONE, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 2d Polylines nur aus Geraden uebereinstimmen.
	@Test
	public void polylines2dStrainghtsEqual_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSEQUALATIOND, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die 2 3d Polylines aus Geraden und Kurven uebereinstimmen.
	@Test
	public void polylines3dStraightsArcs_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSEQUALATIONA, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 2d Oberflaechen uebereinstimmen.
	@Test
	public void surface2dEqual_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSEQUALATIONB, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// test in equal (==)
	@Test
	public void mandatoryConstraintEqualityAreaTwice2DOk(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSEQUALATIONC, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden Texte ungleich sind.
	@Test
	public void textUnEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONA, OID);
		iomObjA.setattrvalue("attr1", "Bernhard");
		iomObjA.setattrvalue("attr2", "Hannes");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Nummern ungleich sind.
	@Test
	public void numericUnEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONB, OID);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "10");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Nummern ungleich sind.
	@Test
	public void decimalUnEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONB, OID);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "10.1958764");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden Aufzaehlungen ungleich sind.
	@Test
	public void enumerationUnEqual_Ok(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSINEQUALATIONC, OID);
		objValue.setattrvalue("aufzaehlung1", "null");
		objValue.setattrvalue("aufzaehlung2", "eins");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Aufzaehlungen mit der Unter-Hierarchy ungleich sind.
	@Test
	public void enumerationSubUnEqual_Ok(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSINEQUALATIONC, OID);
		objValue.setattrvalue("aufzaehlung1", "mehr.vier");
		objValue.setattrvalue("aufzaehlung2", "drei");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Coords ungleich sind.
	@Test
	public void coordsUnEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIOND, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 2d Polylines aus Geraden ungleich zu einander sind.
	@Test
	public void polylines2dStraights_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSINEQUALATIONE, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Polylines 3d mit Geraden und Kurven ungleich sind.
	@Test
	public void polylines3dStraightsArcsUnEqual_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSINEQUALATIONF, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden Oberflaechen in 2d ungleich zu einander stehen.
	@Test
	public void surface2dUnEqual_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSINEQUALATIONG, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden Areas in 2d ungleich sind.
	@Test
	public void area2dUnEqual_Ok(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSINEQUALATIONH, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden Booleans ungleich sind.
	@Test
	public void booleanUnEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONI, OID);
		iomObjA.setattrvalue("attr1", "true");
		iomObjA.setattrvalue("attr2", "false");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Nummer aus attr1 groesser als die Nummer aus attr2 ist.
	@Test
	public void numericGreaterThan_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANA, OID);
		iomObjA.setattrvalue("attr1", "6");
		iomObjA.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Nummer aus attr1 groesser als die Nummer aus attr2 ist.
	@Test
	public void decimalGreaterThan_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANA, OID);
		iomObjA.setattrvalue("attr1", "6");
		iomObjA.setattrvalue("attr2", "5.9");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Aufzaehlung groesser als die zweite Aufzaehlung ist.
	@Test
	public void enumerationGreaterThan_Ok(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSGREATERTHANB, OID);
		objValue.setattrvalue("aufzaehlung1", "eins");
		objValue.setattrvalue("aufzaehlung2", "null");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn attr1 groesser als attr2 ist.
	@Test
	public void textGreaterThan_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANC, OID);
		iomObjA.setattrvalue("attr1", "Aaaab");
		iomObjA.setattrvalue("attr2", "Aaaaa");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn attr1 kleiner als attr2 in Numerischen Zahlen ist.
	@Test
	public void numericLessThan_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANA, OID);
		iomObjA.setattrvalue("attr1", "4");
		iomObjA.setattrvalue("attr2", "6");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Aufzaehlung1 kleiner als die Aufzaehlung2 ist.
	@Test
	public void enumerationLessThan_Ok(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSLESSTHANB, OID);
		objValue.setattrvalue("aufzaehlung1", "eins");
		objValue.setattrvalue("aufzaehlung2", "drei");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn attr1 kleiner als attr2 ist.
	@Test
	public void textLessTahn_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANC, OID);
		iomObjA.setattrvalue("attr1", "Aaaaa");
		iomObjA.setattrvalue("attr2", "Aaaab");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn attr1 groesser oder gleich attr2 ist.
	@Test
	public void numericGreaterThanOrEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALA, OID);
		iomObjA.setattrvalue("attr1", "6");
		iomObjA.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn attr1 groesser oder gleich wie attr2 ist.
	@Test
	public void numericGreaterOrEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALA, OID);
		iomObjA.setattrvalue("attr1", "6");
		iomObjA.setattrvalue("attr2", "6");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn attr1 groesser oder gleich attr2 ist.
	@Test
	public void decimalGreaterThanOrEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALA, OID);
		iomObjA.setattrvalue("attr1", "6.9");
		iomObjA.setattrvalue("attr2", "5.8");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn attr1 groesser oder gleich wie attr2 ist.
	@Test
	public void decimalGreaterOrEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALA, OID);
		iomObjA.setattrvalue("attr1", "6.001");
		iomObjA.setattrvalue("attr2", "6.001");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Aufzaehlung1 groesser oder gleich der Aufzaehlung2 ist.
	@Test
	public void enumerationGreaterOrEqual_Ok(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALB, OID);
		objValue.setattrvalue("aufzaehlung1", "eins");
		objValue.setattrvalue("aufzaehlung2", "null");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Aufzaehlung1 groesser oder gleich der Aufzaehlung2 ist. 
	@Test
	public void enumerationGreaterOrEqual2_Ok(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALB, OID);
		objValue.setattrvalue("aufzaehlung1", "null");
		objValue.setattrvalue("aufzaehlung2", "null");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der Text aus attr1 groesser oder gleich dem Text aus attr2 ist.
	@Test
	public void textGreaterOrEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALC, OID);
		iomObjA.setattrvalue("attr1", "Aaaab");
		iomObjA.setattrvalue("attr2", "Aaaaa");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der Text aus attr1 groesser oder gleich dem Text aus attr2 ist
	@Test
	public void textGreaterOrEqual2_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALC, OID);
		iomObjA.setattrvalue("attr1", "Aaaab");
		iomObjA.setattrvalue("attr2", "Aaaab");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der Text aus attr1 kleiner oder gleich dem Text aus attr2 ist.
	@Test
	public void textLessThanOrEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANOREQUALA, OID);
		iomObjA.setattrvalue("attr1", "4");
		iomObjA.setattrvalue("attr2", "6");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der Text aus attr1 kleiner oder gleich dem Text aus attr2 ist
	@Test
	public void numericLessThanOrEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANOREQUALA, OID);
		iomObjA.setattrvalue("attr1", "6");
		iomObjA.setattrvalue("attr2", "6");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Aufzaehlung1 kleiner oder gleich gross der Aufzaehlung2 ist.
	@Test
	public void enumerationLessThanOrEqual_Ok(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSLESSTHANOREQUALB, OID);
		objValue.setattrvalue("aufzaehlung1", "eins");
		objValue.setattrvalue("aufzaehlung2", "drei");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Aufzaehlung1 kleiner oder gleich der Aufzaehlung2 ist.
	@Test
	public void enumerationLessThanOrEqual2_Ok(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSLESSTHANOREQUALB, OID);
		objValue.setattrvalue("aufzaehlung1", "drei");
		objValue.setattrvalue("aufzaehlung2", "drei");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der erste Text kleiner oder gleich dem zweiten Text ist.
	@Test
	public void textLessThanOrEqual2_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANOREQUALC, OID);
		iomObjA.setattrvalue("attr1", "Aaaaa");
		iomObjA.setattrvalue("attr2", "Aaaab");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der erste Text kleiner oder gleich dem zweiten Text ist.
	@Test
	public void textLessTahnOrEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANOREQUALC, OID);
		iomObjA.setattrvalue("attr1", "Aaaab");
		iomObjA.setattrvalue("attr2", "Aaaab");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}	
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die 2d Polyline nur mit Geraden definiert wurde.
	@Test
	public void polyline2dStraightsDefined_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSDEFINEDA, OID);
		IomObject polylineValue=objStraightsSuccess.addattrobj("straightsarcs1", "POLYLINE");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der formattedType definiert wurde.
	@Test
	public void formattedTypeDefined_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSFORMATTEDTYPEA, OID);
		objStraightsSuccess.setattrvalue("attr1", "2005-12-31T23:59:59.999");
		objStraightsSuccess.setattrvalue("attr2", "2005-12-31T23:59:59.999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die verschiedenen Expressions alle stimmen.
	@Test
	public void differentExpressions_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(TOPIC+".ClassDiffExpressions", OID);
		objStraightsSuccess.setattrvalue("Geometrie_Polygon", "true");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	//#########################################################//
	//########### FAIL MANDATORY CONSTRAINTS ##################//
	//#########################################################//
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die boolean nicht uebereinstimmen.	
	@Test
	public void booleanEqual_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONI, OID);
		iomObjA.setattrvalue("attr1", "true");
		iomObjA.setattrvalue("attr2", "false");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassEqualI.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Texte nicht uebereinstimmen.
	@Test
	public void textNotEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONG, OID);
		iomObjA.setattrvalue("attr1", "Bernhard");
		iomObjA.setattrvalue("attr2", "Albert");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassEqualG.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Nummern nicht uebereinstimmen.
	@Test
	public void numericNotEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONF, OID);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "4");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassEqualF.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Nummern nicht uebereinstimmen.
	@Test
	public void decimalNotEqual_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONF, OID);
		iomObjA.setattrvalue("attr1", "5.000001");
		iomObjA.setattrvalue("attr2", "4.999999");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassEqualF.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Coords nicht uebereinstimmen.
	@Test
	public void coordsNotEqual_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONE, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassEqualE.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die 2d Polylines nicht uebereinstimmen.
	@Test
	public void polylines2dNotEqual_False(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSEQUALATIOND, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassEqualD.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die 3d Polylines nich uebereinstimmen.
	@Test
	public void polyline3dNotEqual_False(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSEQUALATIONA, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassEqualA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die 2d Oberflaechen nicht uebereinstimmen.
	@Test
	public void polyline2dNotEqual_False(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSEQUALATIONB, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassEqualB.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die 2d Areas nicht uebereinstimmen.
	@Test
	public void area2dNotEqual_False(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSEQUALATIONC, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassEqualC.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden Texte uebereinstimmen.
	@Test
	public void textIsEqual_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONA, OID);
		iomObjA.setattrvalue("attr1", "Bernhard");
		iomObjA.setattrvalue("attr2", "Bernhard");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassUnEqualA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}

	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden Texte uebereinstimmen.
	@Test
	public void numberUnEqual_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONB, OID);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassUnEqualB.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden Texte uebereinstimmen.
	@Test
	public void decimalUnEqual_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONB, OID);
		iomObjA.setattrvalue("attr1", "5.222");
		iomObjA.setattrvalue("attr2", "5.222");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassUnEqualB.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden Aufzaehlungen nicht ungleich sind.
	@Test
	public void enumerationUnEqual_False(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSINEQUALATIONC, OID);
		objValue.setattrvalue("aufzaehlung1", "null");
		objValue.setattrvalue("aufzaehlung2", "null");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassUnEqualC.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden Aufzaehlungen mit ihrer Sub-Hierarchie nicht uebereinstimmen.
	@Test
	public void subEnumerationUnEqual_False(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSINEQUALATIONC, OID);
		objValue.setattrvalue("aufzaehlung1", "mehr.vier");
		objValue.setattrvalue("aufzaehlung2", "mehr.vier");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassUnEqualC.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden Coords nicht uebereinstimmen.
	@Test
	public void coordsUnEqual_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIOND, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassUnEqualD.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 2d Polylines nur aus Geraden, nicht uebereinstimmen.
	@Test
	public void polyline2dStraightsUnEqual_False(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSINEQUALATIONE, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassUnEqualE.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die 3d Polylines aus Geraden und Kurven nicht uebereinstimmen.
	@Test
	public void polyline3dStraightsArcsUnEqual_False(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSINEQUALATIONF, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassUnEqualF.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 2d Oberflaechen nicht ungleich zueinander sind.
	@Test
	public void surface2dUnEqual_False(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSINEQUALATIONG, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassUnEqualG.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 2d Polygone nicht ungleich sind.
	@Test
	public void area2DUnEqual_False(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSINEQUALATIONH, OID);
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
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassUnEqualH.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden Booleans nicht ungleich sind.
	@Test
	public void booleanNotUnEqual_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONI, OID);
		iomObjA.setattrvalue("attr1", "true");
		iomObjA.setattrvalue("attr2", "true");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassUnEqualI.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn das erste Attribute nicht groesser als das Zweite Attribute ist.
	@Test
	public void numericGreaterThan_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANA, OID);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "6");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassGreaterThanA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die attr1 nicht groesser als attr2 ist. 
	@Test
	public void numericNotGreaterThan_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANA, OID);
		iomObjA.setattrvalue("attr1", "6");
		iomObjA.setattrvalue("attr2", "6");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassGreaterThanA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn das erste Attribute nicht groesser als das Zweite Attribute ist.
	@Test
	public void decimalGreaterThan_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANA, OID);
		iomObjA.setattrvalue("attr1", "5.8");
		iomObjA.setattrvalue("attr2", "5.9");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassGreaterThanA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die attr1 nicht groesser als attr2 ist. 
	@Test
	public void decimalNotGreaterThan_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANA, OID);
		iomObjA.setattrvalue("attr1", "6.0");
		iomObjA.setattrvalue("attr2", "6.0");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassGreaterThanA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Aufzaehlung1 nicht groesser als die Aufzaehlung2 ist.
	@Test
	public void enumerationNotGreaterThan_False(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSGREATERTHANB, OID);
		objValue.setattrvalue("aufzaehlung1", "null");
		objValue.setattrvalue("aufzaehlung2", "eins");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassGreaterThanB.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden Aufzaehlungen nicht ungleich sind.
	@Test
	public void enumerationUnEqual2_False(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSGREATERTHANB, OID);
		objValue.setattrvalue("aufzaehlung1", "null");
		objValue.setattrvalue("aufzaehlung2", "null");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassGreaterThanB.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn attr1 nicht groesser als attr2 ist.
	@Test
	public void textNotGreaterThan_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANC, OID);
		iomObjA.setattrvalue("attr1", "Aaaaa");
		iomObjA.setattrvalue("attr2", "Aaaab");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassGreaterThanC.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn attr1 nicht groesser als attr2 ist.
	@Test
	public void textNotGreaterThan2_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANC, OID);
		iomObjA.setattrvalue("attr1", "Aaaab");
		iomObjA.setattrvalue("attr2", "Aaaab");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassGreaterThanC.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn  die attr1 Nummer nicht kleiner als die attr2 Nummer ist.
	@Test
	public void numericNotLessThan_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANA, OID);
		iomObjA.setattrvalue("attr1", "6");
		iomObjA.setattrvalue("attr2", "4");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassLessThanA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn attr1 nicht kleiner als attr2 ist.
	@Test
	public void numericNotLess_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANA, OID);
		iomObjA.setattrvalue("attr1", "6");
		iomObjA.setattrvalue("attr2", "6");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassLessThanA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn  die attr1 Nummer nicht kleiner als die attr2 Nummer ist.
	@Test
	public void decimalNotLessThan_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANA, OID);
		iomObjA.setattrvalue("attr1", "4.301");
		iomObjA.setattrvalue("attr2", "4.202");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassLessThanA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn attr1 nicht kleiner als attr2 ist.
	@Test
	public void decimalNotLess_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANA, OID);
		iomObjA.setattrvalue("attr1", "6.0");
		iomObjA.setattrvalue("attr2", "6.0");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassLessThanA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Aufzaehlung1 nicht kleiner als Aufzaehlung2 ist.
	@Test
	public void enumerationNotLessThan_False(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSLESSTHANB, OID);
		objValue.setattrvalue("aufzaehlung1", "drei");
		objValue.setattrvalue("aufzaehlung2", "eins");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassLessThanB.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Aufzaehlung1 nicht kleiner als die Aufzaehlung ist.
	@Test
	public void enumeriationsNotLessThan_False(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSLESSTHANB, OID);
		objValue.setattrvalue("aufzaehlung1", "drei");
		objValue.setattrvalue("aufzaehlung2", "drei");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassLessThanB.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der Text aus attr1 nicht kleiner als attr2 ist.
	@Test
	public void textNotLessThan_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANC, OID);
		iomObjA.setattrvalue("attr1", "Aaaab");
		iomObjA.setattrvalue("attr2", "Aaaaa");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassLessThanC.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn attr1 nicht kleiner als attr2 ist.
	@Test
	public void textLessThan_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANC, OID);
		iomObjA.setattrvalue("attr1", "Aaaab");
		iomObjA.setattrvalue("attr2", "Aaaab");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassLessThanC.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn attr1 nicht groesser oder gleich attr2 ist.
	@Test
	public void numericNotGreaterThanOrEqual_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALA, OID);
		iomObjA.setattrvalue("attr1", "5");
		iomObjA.setattrvalue("attr2", "6");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassGreaterThanOrEqualA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn attr1 nicht groesser oder gleich attr2 ist.
	@Test
	public void decimalNotGreaterThanOrEqual_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALA, OID);
		iomObjA.setattrvalue("attr1", "6.0124");
		iomObjA.setattrvalue("attr2", "6.0125");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassGreaterThanOrEqualA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Aufzaehlung1 nicht groesser oder gleich die Aufzaehlung2 ist.
	@Test
	public void enumeriationNotGreaterThanOrEqual_False(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALB, OID);
		objValue.setattrvalue("aufzaehlung1", "null");
		objValue.setattrvalue("aufzaehlung2", "eins");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassGreaterThanOrEqualB.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der Text aus attr1 nicht groesser oder gleich dem Text aus attr2 ist.
	@Test
	public void textNotGreaterThanOrEqual_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALC, OID);
		iomObjA.setattrvalue("attr1", "Aaaaa");
		iomObjA.setattrvalue("attr2", "Aaaab");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassGreaterThanOrEqualC.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Nummer aus attr1 nicht kleiner der Nummer aus attr2 ist.
	@Test
	public void numericNotLessThan2_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANOREQUALA, OID);
		iomObjA.setattrvalue("attr1", "6");
		iomObjA.setattrvalue("attr2", "4");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassLessThanOrEqualA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Nummer aus attr1 nicht kleiner der Nummer aus attr2 ist.
	@Test
	public void decimalNotLessThan2_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANOREQUALA, OID);
		iomObjA.setattrvalue("attr1", "6.4");
		iomObjA.setattrvalue("attr2", "4.5");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassLessThanOrEqualA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Aufzaehlung1 nicht kleiner oder gleich der Aufzaehlung2 ist.
	@Test
	public void enumerationNotLessThanOrEqual_False(){
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSLESSTHANOREQUALB, OID);
		objValue.setattrvalue("aufzaehlung1", "drei");
		objValue.setattrvalue("aufzaehlung2", "eins");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objValue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassLessThanOrEqualB.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der Text aus attr1, nicht kleiner oder gleich dem Text aus attr2 ist.
	@Test
	public void textNotLessThanOrEqual_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANOREQUALC, OID);
		iomObjA.setattrvalue("attr1", "Aaaab");
		iomObjA.setattrvalue("attr2", "Aaaaa");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassLessThanOrEqualC.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der FormattedType aus attr1, dem FormattedType aus attr2 entspricht.
	@Test
	public void formattedTypeUnEqual_Fail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSFORMATTEDTYPEA, OID);
		objStraightsSuccess.setattrvalue("attr1", "2005-12-31T23:59:59.999");
		objStraightsSuccess.setattrvalue("attr2", "2005-12-31T23:59:59.888");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassFormattedTypeA.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn alle 3 Constraints definierte Attribute enthalten.
	@Test
	public void unDefinedAttrs_Fail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSDEFINEDB, OID);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==3);
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassDefinedB.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassDefinedB.Constraint2 is not true.", logger.getErrs().get(1).getEventMsg());
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassDefinedB.Constraint3 is not true.", logger.getErrs().get(2).getEventMsg());
	}
}