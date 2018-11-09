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

public class ExistenceConstraints23Test {
	
	private TransferDescription td=null;
	// OID
	private final static String OID1 ="o1";
	private final static String OID2 ="o2";
	private final static String OID3 ="o3";
	// START BASKET EVENT
	private final static String BID1="b1";
	private final static String BID2="b2";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntryConditionClass=new FileEntry("src/test/data/validator/ExistenceConstraints23Condition.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntryConditionClass);
		FileEntry fileEntry=new FileEntry("src/test/data/validator/ExistenceConstraints23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		FileEntry fileEntryCoords=new FileEntry("src/test/data/validator/ExistenceConstraints23Coords.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntryCoords);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	//############################################################/
	//########## SUCCESSFUL TESTS ################################/
	//############################################################/
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn das Attr5 der KlasseA ueber die
	// Existence Constraint auf das Attr1 der Klasse ConditionClass verweist und dieselbe Value hat.
	@Test
	public void existenceConstraintToClass_Ok() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClass", OID1);
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", OID2);
		objA.setattrvalue("attr5", "lars");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID1));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn das Attr5 der KlasseA ueber die
	// Existence Constraint auf das Attr1 der Klasse ConditionClass verweist und dieselbe Value hat.
	// Die Klasse: ClassA wird von der Klasse: Class AP erweitert.
	// Die Klasse: ConditionClass wird von der Klasse: ConditionClassX erweitert.
	@Test
	public void subClassExistenceConstraintToClass_Ok() throws Exception{
		Iom_jObject objConditionX=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClassX", OID1);
		objConditionX.setattrvalue("attr1", "lars");
		Iom_jObject objAP=new Iom_jObject("ExistenceConstraints23.Topic.ClassAp", OID2);
		objAP.setattrvalue("attr5", "lars");
		objAP.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID1));
		validator.validate(new ObjectEvent(objConditionX));
		validator.validate(new ObjectEvent(objAP));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn das Attr5 der KlasseA ueber die
	// Existence Constraint auf das Attr1 der Klasse ConditionClass verweist, welche von der Klasse ConditionClassX extended wird und dieselbe Value hat.
	@Test
	public void existenceConstraintViaExtendedClass_Ok() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClassX", OID1);
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", OID2);
		objA.setattrvalue("attr5", "lars");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID1));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn das Attr1 der KlasseB ueber die
	// Existence Constraint auf das Attr1 der Klasse ConditionClass verweist, welche sich in einer anderen Basket befindet und dieselbe Value hat.
	@Test
	public void existenceConstraintViaDiffModelDiffBasket_Ok() throws Exception {
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClass", OID1);
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objB=new Iom_jObject("ExistenceConstraints23.Topic.ClassB", OID2);
		objB.setattrvalue("attr1", "lars");
		objB.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID2));
		validator.validate(new ObjectEvent(objB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn das Attr1 der KlasseB ueber die
	// Existence Constraint auf das Attr1 der Klasse ConditionClass eines anderen Models verweist und dieselbe Value hat.
	@Test
	public void existenceConstraintViaDiffModel_Ok() throws Exception {
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClass", OID1);
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objB=new Iom_jObject("ExistenceConstraints23.Topic.ClassB", OID2);
		objB.setattrvalue("attr1", "lars");
		objB.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID1));
		validator.validate(new ObjectEvent(objB));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn das Attr1 der ClassCoord1d ueber die
	// Existence Constraint auf das Attr0 der Klasse ConditionClassCoord verweist und dieselbe Value hat.
	@Test
	public void existenceConstraintViaDiffModel1dCoord_Ok(){
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", OID1);
		IomObject conditionValue=conditionObj.addattrobj("attr0", "COORD");
		conditionValue.setattrvalue("C1", "480000.000");
		Iom_jObject objCoord=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassCoord1d", OID2);
		IomObject coordValue=objCoord.addattrobj("attr1", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objCoord));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn das Attr1 der ClassCoord2d ueber die
	// Existence Constraint auf das Attr1 der Klasse ConditionClassCoord verweist und dieselben Values hat.
	@Test
	public void existenceConstraintViaDiffModel2dCoords_Ok(){
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", OID1);
		IomObject conditionValue=conditionObj.addattrobj("attr1", "COORD");
		conditionValue.setattrvalue("C1", "480000.000");
		conditionValue.setattrvalue("C2", "70000.000");
		Iom_jObject objCoord=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassCoord2d", OID2);
		IomObject coordValue=objCoord.addattrobj("attr1", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objCoord));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn das Attr1 der ClassCoord3d ueber die
	// Existence Constraint auf das Attr2 der Klasse ConditionClassCoord verweist und dieselben Values hat.
	@Test
	public void existenceConstraintDiffModel3dCoords_Ok(){
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", OID1);
		IomObject conditionValue=conditionObj.addattrobj("attr2", "COORD");
		conditionValue.setattrvalue("C1", "480000.000");
		conditionValue.setattrvalue("C2", "70000.000");
		conditionValue.setattrvalue("C3", "4000.000");
		Iom_jObject objCoord=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassCoord3d", OID2);
		IomObject coordValue=objCoord.addattrobj("attr1", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "70000.000");
		coordValue.setattrvalue("C3", "4000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objCoord));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn straights2d der Klasse ClassLine2d ueber die
	// Existence Constraint auf das straights2d der Klasse ConditionClassLine verweist und dieselbe Value hat.
	@Test
	public void diffModel2dpolylineStraights_Ok(){
		// Polyline in Condition Class
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassLine", OID1);
		IomObject conditionPolyline=conditionObj.addattrobj("straights2d", "POLYLINE");
		IomObject conditionSegment=conditionPolyline.addattrobj("sequence", "SEGMENTS");
		IomObject ConditionCoordStart=conditionSegment.addattrobj("segment", "COORD");
		ConditionCoordStart.setattrvalue("C1", "480000.000");
		ConditionCoordStart.setattrvalue("C2", "70000.000");
		IomObject conditionCoordEnd=conditionSegment.addattrobj("segment", "COORD");
		conditionCoordEnd.setattrvalue("C1", "490000.000");
		conditionCoordEnd.setattrvalue("C2", "70000.000");
		// Polyline in Class
		Iom_jObject obj=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassLine2d", OID2);
		IomObject polylineValue=obj.addattrobj("straights2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coordStart=segments.addattrobj("segment", "COORD");
		coordStart.setattrvalue("C1", "480000.000");
		coordStart.setattrvalue("C2", "70000.000");
		IomObject coordEnd=segments.addattrobj("segment", "COORD");
		coordEnd.setattrvalue("C1", "490000.000");
		coordEnd.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(obj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn das straights3d der ClassLine3d ueber die
	// Existence Constraint auf das straights3d der Klasse ConditionClassLine verweist und dieselbe Value hat.
	@Test
	public void diffModel3dPolylineStraights_Ok(){
		// Polyline in Condition Class
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassLine", OID1);
		IomObject conditionPolyline=conditionObj.addattrobj("straights3d", "POLYLINE");
		IomObject conditionSegment=conditionPolyline.addattrobj("sequence", "SEGMENTS");
		IomObject ConditionCoordStart=conditionSegment.addattrobj("segment", "COORD");
		ConditionCoordStart.setattrvalue("C1", "480000.000");
		ConditionCoordStart.setattrvalue("C2", "70000.000");
		ConditionCoordStart.setattrvalue("C3", "5000.000");
		IomObject conditionCoordEnd=conditionSegment.addattrobj("segment", "COORD");
		conditionCoordEnd.setattrvalue("C1", "490000.000");
		conditionCoordEnd.setattrvalue("C2", "70000.000");
		conditionCoordEnd.setattrvalue("C3", "5000.000");
		// Polyline in Class
		Iom_jObject obj=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassLine3d", OID2);
		IomObject polylineValue=obj.addattrobj("straights3d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coordStart=segments.addattrobj("segment", "COORD");
		coordStart.setattrvalue("C1", "480000.000");
		coordStart.setattrvalue("C2", "70000.000");
		coordStart.setattrvalue("C3", "5000.000");
		IomObject coordEnd=segments.addattrobj("segment", "COORD");
		coordEnd.setattrvalue("C1", "490000.000");
		coordEnd.setattrvalue("C2", "70000.000");
		coordEnd.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(obj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn das arcs2d der ClassLine2dArcs ueber die
	// Existence Constraint auf das arcs2d der Klasse ConditionClassLine verweist und dieselbe Value hat.
	@Test
	public void diffModel2dPolylineArcs_Ok(){
		Iom_jObject objStraightsCondition=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassLine", OID1);
		IomObject polylineValueCondition=objStraightsCondition.addattrobj("arcs2d", "POLYLINE");
		IomObject segmentsCondition=polylineValueCondition.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentCondition=segmentsCondition.addattrobj("segment", "COORD");
		startSegmentCondition.setattrvalue("C1", "480000.000");
		startSegmentCondition.setattrvalue("C2", "70000.000");
		IomObject arcSegmentCondition=segmentsCondition.addattrobj("segment", "ARC");
		arcSegmentCondition.setattrvalue("A1", "480000.000");
		arcSegmentCondition.setattrvalue("A2", "300000.000");
		arcSegmentCondition.setattrvalue("C1", "480000.000");
		arcSegmentCondition.setattrvalue("C2", "70000.000");
		Iom_jObject objStraights=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassLine2dArcs", OID2);
		IomObject polylineValue=objStraights.addattrobj("arcs2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "480000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "480000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objStraightsCondition));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objStraights));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn das arcs3d der ClassLine3dArcs ueber die
	// Existence Constraint auf das arcs3d der Klasse ConditionClassLine verweist und dieselbe Value hat.
	@Test
	public void diffModel3dPolylineArcs_Ok(){
		Iom_jObject objStraightsCondition=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassLine", OID1);
		IomObject polylineValueCondition=objStraightsCondition.addattrobj("arcs3d", "POLYLINE");
		IomObject segmentsCondition=polylineValueCondition.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentCondition=segmentsCondition.addattrobj("segment", "COORD");
		startSegmentCondition.setattrvalue("C1", "480000.000");
		startSegmentCondition.setattrvalue("C2", "70000.000");
		startSegmentCondition.setattrvalue("C3", "4000.000");
		IomObject arcSegmentCondition=segmentsCondition.addattrobj("segment", "ARC");
		arcSegmentCondition.setattrvalue("A1", "480000.000");
		arcSegmentCondition.setattrvalue("A2", "300000.000");
		arcSegmentCondition.setattrvalue("C1", "480000.000");
		arcSegmentCondition.setattrvalue("C2", "70000.000");
		arcSegmentCondition.setattrvalue("C3", "4000.000");
		Iom_jObject objStraights=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassLine3dArcs", OID2);
		IomObject polylineValue=objStraights.addattrobj("arcs3d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "4000.000");
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "480000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "480000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		arcSegment.setattrvalue("C3", "4000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objStraightsCondition));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objStraights));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn das arcsstraights3d der ClassLine3dArcsStraights ueber die
	// Existence Constraint auf das arcsstraights3d der Klasse ConditionClassLine verweist und dieselbe Value hat.
	@Test
	public void diffModel3dPolylineStraightsArcs_Ok(){
		Iom_jObject objStraightsCondition=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassLine", OID1);
		IomObject polylineValueCondition=objStraightsCondition.addattrobj("arcsstraights3d", "POLYLINE");
		IomObject segmentsCondition=polylineValueCondition.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentCondition=segmentsCondition.addattrobj("segment", "COORD");
		startSegmentCondition.setattrvalue("C1", "480000.000");
		startSegmentCondition.setattrvalue("C2", "70000.000");
		startSegmentCondition.setattrvalue("C3", "4000.000");
		IomObject endSegmentCondition=segmentsCondition.addattrobj("segment", "COORD");
		endSegmentCondition.setattrvalue("C1", "490000.000");
		endSegmentCondition.setattrvalue("C2", "70000.000");
		endSegmentCondition.setattrvalue("C3", "4000.000");
		IomObject arcSegmentCondition=segmentsCondition.addattrobj("segment", "ARC");
		arcSegmentCondition.setattrvalue("A1", "500000.000");
		arcSegmentCondition.setattrvalue("A2", "300000.000");
		arcSegmentCondition.setattrvalue("C1", "550000.000");
		arcSegmentCondition.setattrvalue("C2", "70000.000");
		arcSegmentCondition.setattrvalue("C3", "4000.000");
		Iom_jObject objStraights=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassLine3dArcsStraights", OID2);
		IomObject polylineValue=objStraights.addattrobj("arcsstraights3d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "4000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "490000.000");
		endSegment.setattrvalue("C2", "70000.000");
		endSegment.setattrvalue("C3", "4000.000");
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "500000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "550000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		arcSegment.setattrvalue("C3", "4000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objStraightsCondition));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objStraights));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn das arcsstraights2d der ClassLine2dArcsStraights ueber die
	// Existence Constraint auf das arcsstraights2d der Klasse ConditionClassLine verweist und dieselbe Value hat.
	@Test
	public void diffModel2dPolylineStraightsArcs_Ok(){
		Iom_jObject objStraightsCondition=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassLine", OID1);
		IomObject polylineValueCondition=objStraightsCondition.addattrobj("arcsstraights2d", "POLYLINE");
		IomObject segmentsCondition=polylineValueCondition.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentCondition=segmentsCondition.addattrobj("segment", "COORD");
		startSegmentCondition.setattrvalue("C1", "480000.000");
		startSegmentCondition.setattrvalue("C2", "70000.000");
		IomObject endSegmentCondition=segmentsCondition.addattrobj("segment", "COORD");
		endSegmentCondition.setattrvalue("C1", "490000.000");
		endSegmentCondition.setattrvalue("C2", "70000.000");
		IomObject arcSegmentCondition=segmentsCondition.addattrobj("segment", "ARC");
		arcSegmentCondition.setattrvalue("A1", "500000.000");
		arcSegmentCondition.setattrvalue("A2", "300000.000");
		arcSegmentCondition.setattrvalue("C1", "550000.000");
		arcSegmentCondition.setattrvalue("C2", "70000.000");
		Iom_jObject objStraights=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassLine2dArcsStraights", OID2);
		IomObject polylineValue=objStraights.addattrobj("arcsstraights2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "490000.000");
		endSegment.setattrvalue("C2", "70000.000");
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "500000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "550000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objStraightsCondition));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objStraights));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn das surface2d der ClassSurface2d ueber die
	// Existence Constraint auf das surface2d der Klasse ConditionClassSurface verweist und dieselbe Value hat.
	@Test
	public void diffModel2dSurface_Ok(){
		Iom_jObject objSurfaceSuccessCondition=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassSurface", OID1);
		IomObject multisurfaceValueCondtition=objSurfaceSuccessCondition.addattrobj("surface2d", "MULTISURFACE");
		IomObject surfaceValueCondition = multisurfaceValueCondtition.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValueCondition.addattrobj("boundary", "BOUNDARY");
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
		endSegment3.setattrvalue("A1", "480000.000");
		endSegment3.setattrvalue("A2", "80000.000");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		Iom_jObject objSurfaceSuccess=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassSurface2d", OID2);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
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
		arcSegment2.setattrvalue("A1", "480000.000");
		arcSegment2.setattrvalue("A2", "80000.000");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objSurfaceSuccessCondition));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn das surface3d der ClassSurface3d ueber die
	// Existence Constraint auf das surface3d der Klasse ConditionClassSurface verweist und dieselbe Value hat.
	@Test
	public void diffModel3dSurface_Ok(){
		Iom_jObject objSurfaceSuccessCondition=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassSurface", OID1);
		IomObject multisurfaceValueCondtition=objSurfaceSuccessCondition.addattrobj("surface3d", "MULTISURFACE");
		IomObject surfaceValueCondition = multisurfaceValueCondtition.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValueCondition.addattrobj("boundary", "BOUNDARY");
		// polyline 1
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "1000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		endSegment.setattrvalue("C3", "1000.000");
		// polyline 2
		IomObject polylineValue11 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments11=polylineValue11.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment11=segments11.addattrobj("segment", "COORD");
		startSegment11.setattrvalue("C1", "500000.000");
		startSegment11.setattrvalue("C2", "80000.000");
		startSegment11.setattrvalue("C3", "1000.000");
		IomObject endSegment11=segments11.addattrobj("segment", "COORD");
		endSegment11.setattrvalue("C1", "520000.000");
		endSegment11.setattrvalue("C2", "85000.000");
		endSegment11.setattrvalue("C3", "1000.000");
		// arc 1
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "520000.000");
		startSegment3.setattrvalue("C2", "85000.000");
		startSegment3.setattrvalue("C3", "1000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "ARC");
		endSegment3.setattrvalue("A1", "480000.000");
		endSegment3.setattrvalue("A2", "80000.000");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		endSegment3.setattrvalue("C3", "1000.000");
		Iom_jObject objSurfaceSuccess=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassSurface3d", OID2);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// 1. polyline
		IomObject polylineValue1 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments1=polylineValue1.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment1=segments1.addattrobj("segment", "COORD");
		startSegment1.setattrvalue("C1", "480000.000");
		startSegment1.setattrvalue("C2", "70000.000");
		startSegment1.setattrvalue("C3", "1000.000");
		IomObject endSegment1=segments1.addattrobj("segment", "COORD");
		endSegment1.setattrvalue("C1", "500000.000");
		endSegment1.setattrvalue("C2", "80000.000");
		endSegment1.setattrvalue("C3", "1000.000");
		// 2. polyline
		IomObject polylineValue12 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments12=polylineValue12.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment12=segments12.addattrobj("segment", "COORD");
		startSegment12.setattrvalue("C1", "500000.000");
		startSegment12.setattrvalue("C2", "80000.000");
		startSegment12.setattrvalue("C3", "1000.000");
		IomObject endSegment12=segments12.addattrobj("segment", "COORD");
		endSegment12.setattrvalue("C1", "520000.000");
		endSegment12.setattrvalue("C2", "85000.000");
		endSegment12.setattrvalue("C3", "1000.000");
		// arc 1
		IomObject arcValue=outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject arcSegments2=arcValue.addattrobj("sequence", "SEGMENTS");
		IomObject arcStartSegment2=arcSegments2.addattrobj("segment", "COORD");
		arcStartSegment2.setattrvalue("C1", "520000.000");
		arcStartSegment2.setattrvalue("C2", "85000.000");
		arcStartSegment2.setattrvalue("C3", "1000.000");
		IomObject arcSegment2=arcSegments2.addattrobj("segment", "ARC");
		arcSegment2.setattrvalue("A1", "480000.000");
		arcSegment2.setattrvalue("A2", "80000.000");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		arcSegment2.setattrvalue("C3", "1000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objSurfaceSuccessCondition));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn das area2d der ClassArea2d ueber die
	// Existence Constraint auf das area2d der Klasse ConditionClassArea verweist und dieselbe Value hat.
	@Test
	public void diffModel2dArea_Ok(){
		Iom_jObject objSurfaceSuccessCondition=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassArea", OID1);
		IomObject multisurfaceValueCondtition=objSurfaceSuccessCondition.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValueCondition = multisurfaceValueCondtition.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValueCondition.addattrobj("boundary", "BOUNDARY");
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
		endSegment3.setattrvalue("A1", "480000.000");
		endSegment3.setattrvalue("A2", "80000.000");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		Iom_jObject objSurfaceSuccess=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassArea2d", OID2);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
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
		arcSegment2.setattrvalue("A1", "480000.000");
		arcSegment2.setattrvalue("A2", "80000.000");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objSurfaceSuccessCondition));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn das area3d der ClassArea3d ueber die
	// Existence Constraint auf das area3d der Klasse ConditionClassArea verweist und dieselbe Value hat.
	@Test
	public void diffModel3dSurfaceArea_Ok(){
		Iom_jObject objSurfaceSuccessCondition=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassArea", OID1);
		IomObject multisurfaceValueCondtition=objSurfaceSuccessCondition.addattrobj("area3d", "MULTISURFACE");
		IomObject surfaceValueCondition = multisurfaceValueCondtition.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValueCondition.addattrobj("boundary", "BOUNDARY");
		// polyline 1
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "1000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		endSegment.setattrvalue("C3", "1000.000");
		// polyline 2
		IomObject polylineValue11 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments11=polylineValue11.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment11=segments11.addattrobj("segment", "COORD");
		startSegment11.setattrvalue("C1", "500000.000");
		startSegment11.setattrvalue("C2", "80000.000");
		startSegment11.setattrvalue("C3", "1000.000");
		IomObject endSegment11=segments11.addattrobj("segment", "COORD");
		endSegment11.setattrvalue("C1", "520000.000");
		endSegment11.setattrvalue("C2", "85000.000");
		endSegment11.setattrvalue("C3", "1000.000");
		// arc 1
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "520000.000");
		startSegment3.setattrvalue("C2", "85000.000");
		startSegment3.setattrvalue("C3", "1000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "ARC");
		endSegment3.setattrvalue("A1", "480000.000");
		endSegment3.setattrvalue("A2", "80000.000");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		endSegment3.setattrvalue("C3", "1000.000");
		Iom_jObject objSurfaceSuccess=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassArea3d", OID2);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// 1. polyline
		IomObject polylineValue1 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments1=polylineValue1.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment1=segments1.addattrobj("segment", "COORD");
		startSegment1.setattrvalue("C1", "480000.000");
		startSegment1.setattrvalue("C2", "70000.000");
		startSegment1.setattrvalue("C3", "1000.000");
		IomObject endSegment1=segments1.addattrobj("segment", "COORD");
		endSegment1.setattrvalue("C1", "500000.000");
		endSegment1.setattrvalue("C2", "80000.000");
		endSegment1.setattrvalue("C3", "1000.000");
		// 2. polyline
		IomObject polylineValue12 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments12=polylineValue12.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment12=segments12.addattrobj("segment", "COORD");
		startSegment12.setattrvalue("C1", "500000.000");
		startSegment12.setattrvalue("C2", "80000.000");
		startSegment12.setattrvalue("C3", "1000.000");
		IomObject endSegment12=segments12.addattrobj("segment", "COORD");
		endSegment12.setattrvalue("C1", "520000.000");
		endSegment12.setattrvalue("C2", "85000.000");
		endSegment12.setattrvalue("C3", "1000.000");
		// arc 1
		IomObject arcValue=outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject arcSegments2=arcValue.addattrobj("sequence", "SEGMENTS");
		IomObject arcStartSegment2=arcSegments2.addattrobj("segment", "COORD");
		arcStartSegment2.setattrvalue("C1", "520000.000");
		arcStartSegment2.setattrvalue("C2", "85000.000");
		arcStartSegment2.setattrvalue("C3", "1000.000");
		IomObject arcSegment2=arcSegments2.addattrobj("segment", "ARC");
		arcSegment2.setattrvalue("A1", "480000.000");
		arcSegment2.setattrvalue("A2", "80000.000");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		arcSegment2.setattrvalue("C3", "1000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objSurfaceSuccessCondition));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn 2 Objekte erstellt werden, wenn eines der Objekte das Attr1 der ClassCooord2d ueber die
	// Existence Constraint auf das Attr1 der ConditionClassCoord verweist und dieselbe Value hat.
	@Test
	public void diffModel2ObjsOneContainsConditionAttrs_Ok(){
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", OID1);
		IomObject coordValue=conditionObj.addattrobj("attr1", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "70000.000");
		Iom_jObject conditionObj2=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", OID3);
		Iom_jObject objCoord=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassCoord2d", OID2);
		coordValue=objCoord.addattrobj("attr1", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new ObjectEvent(conditionObj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objCoord));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn kein Attribute das Condition attr2 besitzt.
	@Test
	public void diffModelNooneContainsConditionAttrs_Ok(){
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", OID1);
		IomObject conditionValue=conditionObj.addattrobj("attr2", "COORD");
		conditionValue.setattrvalue("C1", "480000.000");
		conditionValue.setattrvalue("C2", "70000.000");
		conditionValue.setattrvalue("C3", "4000.000");
		Iom_jObject objCoord=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassCoord3d", OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objCoord));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn beide Objekte keine Attribute fuer den Constraint definiert haben.
	@Test
	public void diffModelBothObjectsWithoutConditionValues_Ok(){
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", OID1);
		Iom_jObject objCoord=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassCoord3d", OID2);
		objCoord.setattrvalue("attr2", "lars");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objCoord));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob innerhalb einer Struktur eine Existence Constraint geprueft wird.
	// Es darf keine Fehlermeldung ausgegeben werden, da die Attributewerte miteinander uebereinstimmen.
	@Test
	public void existenceConstraintInStructure_Ok() throws Exception {
		Iom_jObject conditionClass=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClass", OID1);
		conditionClass.setattrvalue("attr1", "lars");
		Iom_jObject baseClassStruct=new Iom_jObject("ExistenceConstraints23.Topic.structureBase", null);
		baseClassStruct.setattrvalue("attr1", "lars");
		Iom_jObject baseClassD=new Iom_jObject("ExistenceConstraints23.Topic.ClassD", OID2);
		baseClassD.addattrobj("attr1", baseClassStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID1));
		validator.validate(new ObjectEvent(conditionClass));
		validator.validate(new ObjectEvent(baseClassD));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	// Es wird getestet, dass das Constraint innerhlab einer Struktur ueberlesen wird, falls faelschlicherweise kein Strukturelement vorhanden ist
	@Test
	public void existenceConstraintInStructureButPrimitiveValue_Fail() throws Exception {
		Iom_jObject conditionClass=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClass", OID1);
		conditionClass.setattrvalue("attr1", "lars");
		Iom_jObject baseClassD=new Iom_jObject("ExistenceConstraints23.Topic.ClassD", OID2);
		baseClassD.setattrvalue("attr1", "lars"); // sollte eigentlich ein Strukturelement sein, darf nicht zum Absturz fuehren
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID1));
		validator.validate(new ObjectEvent(conditionClass));
		validator.validate(new ObjectEvent(baseClassD));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn
	// - innerhalb einer Klasse, auf eine Struktur in einem anderen Model verwiesen wird.
	// - Die Existence-Constraint auf eine Klasse in einem anderen Model verweist.
	// - Die Attribute-Werte stimmen uebereinstimmen.
	@Test
	public void existenceConstraint_structuredValue_Ok() throws Exception {
		Iom_jObject conditionStruct=new Iom_jObject("ExistenceConstraints23Condition.structure", null);
		conditionStruct.setattrvalue("attr1", "lars");
		Iom_jObject conditionClass=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassStructure", OID1);
		conditionClass.addattrobj("attr1", conditionStruct);
		Iom_jObject objC=new Iom_jObject("ExistenceConstraints23.Topic.ClassStructureOtherModel", OID2);
		// base class references to condition struct.
		objC.addattrobj("attr1", conditionStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(conditionClass));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID2));
		validator.validate(new ObjectEvent(objC));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn
	// - innerhalb einer Klasse, ueber mehrere Strukturen in einem anderen Model verwiesen wird.
	// - Die Existence-Constraint auf eine Klasse in einem anderen Model verweist.
	// - Die Attribute-Werte stimmen uebereinstimmen.
	@Test
	public void existenceConstraint_nestedStructures_Ok() throws Exception {
		Iom_jObject conditionStruct=new Iom_jObject("ExistenceConstraints23Condition.structure", null);
		conditionStruct.setattrvalue("attr1", "lars");
		Iom_jObject conditionStruct2=new Iom_jObject("ExistenceConstraints23Condition.structure2", null);
		IomObject conditionCoordValue=conditionStruct2.addattrobj("coord", "COORD");
		conditionCoordValue.setattrvalue("C1", "480000.000");
		conditionCoordValue.setattrvalue("C2", "70000.000");
		conditionStruct2.addattrobj("attr1", conditionStruct);
		Iom_jObject conditionClass=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassStructure2", OID1);
		conditionClass.addattrobj("attr1", conditionStruct2);
		Iom_jObject objC=new Iom_jObject("ExistenceConstraints23.Topic.ClassStructureOtherModel2", OID2);
		// base class references to condition struct.
		objC.addattrobj("attr1", conditionStruct2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(conditionClass));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID2));
		validator.validate(new ObjectEvent(objC));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	//############################################################
	//################ FAIL TESTS ################################
	//############################################################

	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die beiden constraint Attribute nicht uebereinstimmen.
	@Test
	public void sameModelDifferentAttrs_Fail() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClass", OID1);
		objBedingung.setattrvalue("attr1", "other");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", OID2);
		objA.setattrvalue("attr5", "lars");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID1));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr5 of ExistenceConstraints23.Topic.ClassA was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn das Attr5 der KlasseA ueber die
	// Existence Constraint auf das Attr1 der Klasse ConditionClass verweist und beide unterschiedliche Values enthalten.
	// Die Klasse: ClassA wird von der Klasse: Class AP erweitert.
	// Die Klasse: ConditionClass wird von der Klasse: ConditionClassX erweitert.
	@Test
	public void subClassSameModelDifferentAttrs_Fail() throws Exception{
		Iom_jObject objConditionX=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClassX", OID1);
		objConditionX.setattrvalue("attr1", "other");
		Iom_jObject objAP=new Iom_jObject("ExistenceConstraints23.Topic.ClassAp", OID2);
		objAP.setattrvalue("attr5", "lars");
		objAP.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID1));
		validator.validate(new ObjectEvent(objConditionX));
		validator.validate(new ObjectEvent(objAP));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr5 of ExistenceConstraints23.Topic.ClassAp was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die beiden constraint Attribute, welche in 2 unterschiedlichen Model sich befinden nicht uebereinstimmen.
	@Test
	public void diffModelConstraintValuesDifferent_Fail() throws Exception {
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClass", OID1);
		objBedingung.setattrvalue("attr1", "other");
		Iom_jObject objB=new Iom_jObject("ExistenceConstraints23.Topic.ClassB", OID2);
		objB.setattrvalue("attr1", "lars");
		objB.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID2));
		validator.validate(new ObjectEvent(objB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr1 of ExistenceConstraints23.Topic.ClassB was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die beiden constraint Attribute nicht uebereinstimmen.
	@Test
	public void sameModelConstraintAttrsDifferent_Fail() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClass2", OID1);
		objBedingung.setattrvalue("attr1", "ben");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", OID2);
		objA.setattrvalue("attr5", "lars");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID1));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr5 of ExistenceConstraints23.Topic.ClassA was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}

	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn das Condition Attributes welche ueber eine weitere Klasse extended wird, mit dem Attribute in ClassA uebereinstimmt.
	@Test
	public void sameModelOverExtendedClass_Fail() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClassX", OID1);
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", OID2);
		objA.setattrvalue("attr5", "ben");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID1));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr5 of ExistenceConstraints23.Topic.ClassA was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}

	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 1d Coord constraint Attribute nicht uebereinstimmen.
	@Test
	public void diffModelDiff1dCoords_Fail(){
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", OID1);
		IomObject conditionValue=conditionObj.addattrobj("attr0", "COORD");
		conditionValue.setattrvalue("C1", "480000.000");
		Iom_jObject objCoord=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassCoord1d", OID2);
		IomObject coordValue=objCoord.addattrobj("attr1", "COORD");
		coordValue.setattrvalue("C1", "480001.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objCoord));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr1 of ExistenceConstraints23Coords.Topic.ClassCoord1d was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}

	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 2d Coords constraint Attribute nicht uebereinstimmen. 
	@Test
	public void diffModel2dCoordDifferent_Fail(){
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", OID1);
		IomObject conditionValue=conditionObj.addattrobj("attr1", "COORD");
		conditionValue.setattrvalue("C1", "480000.000");
		conditionValue.setattrvalue("C2", "70000.000");
		Iom_jObject objCoord=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassCoord2d", OID2);
		IomObject coordValue=objCoord.addattrobj("attr1", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "70001.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objCoord));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr1 of ExistenceConstraints23Coords.Topic.ClassCoord2d was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 3d Coord constraint Attribute nicht uebereinstimmen.
	@Test
	public void diffModel3dCoordsDiff_Fail(){
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", OID1);
		IomObject conditionValue=conditionObj.addattrobj("attr2", "COORD");
		conditionValue.setattrvalue("C1", "480000.000");
		conditionValue.setattrvalue("C2", "70000.000");
		conditionValue.setattrvalue("C3", "4000.000");
		Iom_jObject objCoord=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassCoord3d", OID2);
		IomObject coordValue=objCoord.addattrobj("attr1", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "70000.000");
		coordValue.setattrvalue("C3", "4001.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objCoord));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr1 of ExistenceConstraints23Coords.Topic.ClassCoord3d was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 2d Polyline constraint Attribute nicht uebereinstimmen.
	@Test
	public void diffModel2dPolylineStraights_Fail(){
		// Polyline in Condition Class
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassLine", OID1);
		IomObject conditionPolyline=conditionObj.addattrobj("straights2d", "POLYLINE");
		IomObject conditionSegment=conditionPolyline.addattrobj("sequence", "SEGMENTS");
		IomObject ConditionCoordStart=conditionSegment.addattrobj("segment", "COORD");
		ConditionCoordStart.setattrvalue("C1", "480000.000");
		ConditionCoordStart.setattrvalue("C2", "70000.000");
		IomObject conditionCoordEnd=conditionSegment.addattrobj("segment", "COORD");
		conditionCoordEnd.setattrvalue("C1", "500000.000");
		conditionCoordEnd.setattrvalue("C2", "70000.000");
		// Polyline in Class
		Iom_jObject obj=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassLine2d", OID2);
		IomObject polylineValue=obj.addattrobj("straights2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coordStart=segments.addattrobj("segment", "COORD");
		coordStart.setattrvalue("C1", "480000.000");
		coordStart.setattrvalue("C2", "70000.000");
		IomObject coordEnd=segments.addattrobj("segment", "COORD");
		coordEnd.setattrvalue("C1", "500000.000");
		coordEnd.setattrvalue("C2", "70001.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(obj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute straights2d of ExistenceConstraints23Coords.Topic.ClassLine2d was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 3d Polyline constraint Attribute nicht uebereinstimmen.
	@Test
	public void diffModels3dPolylineStraights_Fail(){
		// Polyline in Condition Class
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassLine", OID1);
		IomObject conditionPolyline=conditionObj.addattrobj("straights3d", "POLYLINE");
		IomObject conditionSegment=conditionPolyline.addattrobj("sequence", "SEGMENTS");
		IomObject ConditionCoordStart=conditionSegment.addattrobj("segment", "COORD");
		ConditionCoordStart.setattrvalue("C1", "480000.000");
		ConditionCoordStart.setattrvalue("C2", "70000.000");
		ConditionCoordStart.setattrvalue("C3", "5000.000");
		IomObject conditionCoordEnd=conditionSegment.addattrobj("segment", "COORD");
		conditionCoordEnd.setattrvalue("C1", "500000.000");
		conditionCoordEnd.setattrvalue("C2", "70000.000");
		conditionCoordEnd.setattrvalue("C3", "4999.000");
		// Polyline in Class
		Iom_jObject obj=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassLine3d", OID2);
		IomObject polylineValue=obj.addattrobj("straights3d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coordStart=segments.addattrobj("segment", "COORD");
		coordStart.setattrvalue("C1", "480000.000");
		coordStart.setattrvalue("C2", "70000.000");
		coordStart.setattrvalue("C3", "5000.000");
		IomObject coordEnd=segments.addattrobj("segment", "COORD");
		coordEnd.setattrvalue("C1", "500000.000");
		coordEnd.setattrvalue("C2", "70000.000");
		coordEnd.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(obj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute straights3d of ExistenceConstraints23Coords.Topic.ClassLine3d was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 2d Polyline mit Arcs constraint Attributes nicht uebereinstimmen.
	@Test
	public void diffModels2dPolylineArcs_Fail(){
		Iom_jObject objStraightsCondition=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassLine", OID1);
		IomObject polylineValueCondition=objStraightsCondition.addattrobj("arcs2d", "POLYLINE");
		IomObject segmentsCondition=polylineValueCondition.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentCondition=segmentsCondition.addattrobj("segment", "COORD");
		startSegmentCondition.setattrvalue("C1", "480000.000");
		startSegmentCondition.setattrvalue("C2", "70000.000");
		IomObject arcSegmentCondition=segmentsCondition.addattrobj("segment", "ARC");
		arcSegmentCondition.setattrvalue("A1", "500000.000");
		arcSegmentCondition.setattrvalue("A2", "300000.000");
		arcSegmentCondition.setattrvalue("C1", "480000.000");
		arcSegmentCondition.setattrvalue("C2", "70000.000");
		Iom_jObject objStraights=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassLine2dArcs", OID2);
		IomObject polylineValue=objStraights.addattrobj("arcs2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "500000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "480000.000");
		arcSegment.setattrvalue("C2", "71111.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objStraightsCondition));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objStraights));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute arcs2d of ExistenceConstraints23Coords.Topic.ClassLine2dArcs was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 3d Polyline mit Arcs constraint Attributes nicht uebereinstimmen.
	@Test
	public void diffModels3dPolylineArcs_Fail(){
		Iom_jObject objStraightsCondition=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassLine", OID1);
		IomObject polylineValueCondition=objStraightsCondition.addattrobj("arcs3d", "POLYLINE");
		IomObject segmentsCondition=polylineValueCondition.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentCondition=segmentsCondition.addattrobj("segment", "COORD");
		startSegmentCondition.setattrvalue("C1", "480000.000");
		startSegmentCondition.setattrvalue("C2", "70000.000");
		startSegmentCondition.setattrvalue("C3", "4000.000");
		IomObject arcSegmentCondition=segmentsCondition.addattrobj("segment", "ARC");
		arcSegmentCondition.setattrvalue("A1", "480000.000");
		arcSegmentCondition.setattrvalue("A2", "300000.000");
		arcSegmentCondition.setattrvalue("C1", "480000.000");
		arcSegmentCondition.setattrvalue("C2", "70000.000");
		arcSegmentCondition.setattrvalue("C3", "4000.000");
		Iom_jObject objStraights=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassLine3dArcs", OID2);
		IomObject polylineValue=objStraights.addattrobj("arcs3d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "4000.000");
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "480000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "480000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		arcSegment.setattrvalue("C3", "4111.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objStraightsCondition));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objStraights));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute arcs3d of ExistenceConstraints23Coords.Topic.ClassLine3dArcs was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 3d Polyline mit Straights und Arcs constraint Attributes nicht uebereinstimmen.
	@Test
	public void diffModels3dPolylineStraightsArcs_Fail(){
		Iom_jObject objStraightsCondition=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassLine", OID1);
		IomObject polylineValueCondition=objStraightsCondition.addattrobj("arcsstraights3d", "POLYLINE");
		IomObject segmentsCondition=polylineValueCondition.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentCondition=segmentsCondition.addattrobj("segment", "COORD");
		startSegmentCondition.setattrvalue("C1", "480000.000");
		startSegmentCondition.setattrvalue("C2", "70000.000");
		startSegmentCondition.setattrvalue("C3", "4000.000");
		IomObject endSegmentCondition=segmentsCondition.addattrobj("segment", "COORD");
		endSegmentCondition.setattrvalue("C1", "490000.000");
		endSegmentCondition.setattrvalue("C2", "70000.000");
		endSegmentCondition.setattrvalue("C3", "4000.000");
		IomObject arcSegmentCondition=segmentsCondition.addattrobj("segment", "ARC");
		arcSegmentCondition.setattrvalue("A1", "500000.000");
		arcSegmentCondition.setattrvalue("A2", "300000.000");
		arcSegmentCondition.setattrvalue("C1", "550000.000");
		arcSegmentCondition.setattrvalue("C2", "70000.000");
		arcSegmentCondition.setattrvalue("C3", "4000.000");
		Iom_jObject objStraights=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassLine3dArcsStraights", OID2);
		IomObject polylineValue=objStraights.addattrobj("arcsstraights3d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "4000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "490000.000");
		endSegment.setattrvalue("C2", "70000.000");
		endSegment.setattrvalue("C3", "4000.000");
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "500000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "550000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		arcSegment.setattrvalue("C3", "4111.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objStraightsCondition));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objStraights));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute arcsstraights3d of ExistenceConstraints23Coords.Topic.ClassLine3dArcsStraights was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 2d Polyline mit Straights und Arcs constraint Attributes nicht uebereinstimmen.
	@Test
	public void diffModels2dPolylineStraightsArcs_Fail(){
		Iom_jObject objStraightsCondition=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassLine", OID1);
		IomObject polylineValueCondition=objStraightsCondition.addattrobj("arcsstraights2d", "POLYLINE");
		IomObject segmentsCondition=polylineValueCondition.addattrobj("sequence", "SEGMENTS");
		IomObject startSegmentCondition=segmentsCondition.addattrobj("segment", "COORD");
		startSegmentCondition.setattrvalue("C1", "480000.000");
		startSegmentCondition.setattrvalue("C2", "70000.000");
		IomObject endSegmentCondition=segmentsCondition.addattrobj("segment", "COORD");
		endSegmentCondition.setattrvalue("C1", "490000.000");
		endSegmentCondition.setattrvalue("C2", "70000.000");
		IomObject arcSegmentCondition=segmentsCondition.addattrobj("segment", "ARC");
		arcSegmentCondition.setattrvalue("A1", "500000.000");
		arcSegmentCondition.setattrvalue("A2", "300000.000");
		arcSegmentCondition.setattrvalue("C1", "550000.000");
		arcSegmentCondition.setattrvalue("C2", "70000.000");
		Iom_jObject objStraights=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassLine2dArcsStraights", OID2);
		IomObject polylineValue=objStraights.addattrobj("arcsstraights2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "490000.000");
		endSegment.setattrvalue("C2", "70000.000");
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "500000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "550000.000");
		arcSegment.setattrvalue("C2", "71111.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objStraightsCondition));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objStraights));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute arcsstraights2d of ExistenceConstraints23Coords.Topic.ClassLine2dArcsStraights was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}

	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 2d Oberflaechen constraint Attributes nicht uebereinstimmen.
	@Test
	public void diffModels2dSurface_Fail(){
		Iom_jObject objSurfaceSuccessCondition=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassSurface", OID1);
		IomObject multisurfaceValueCondtition=objSurfaceSuccessCondition.addattrobj("surface2d", "MULTISURFACE");
		IomObject surfaceValueCondition = multisurfaceValueCondtition.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValueCondition.addattrobj("boundary", "BOUNDARY");
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
		endSegment3.setattrvalue("A1", "480000.000");
		endSegment3.setattrvalue("A2", "80000.000");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		Iom_jObject objSurfaceSuccess=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassSurface2d", OID2);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
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
		endSegment12.setattrvalue("C1", "600000.000");
		endSegment12.setattrvalue("C2", "85000.000");
		// arc 1
		IomObject arcValue=outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject arcSegments2=arcValue.addattrobj("sequence", "SEGMENTS");
		IomObject arcStartSegment2=arcSegments2.addattrobj("segment", "COORD");
		arcStartSegment2.setattrvalue("C1", "600000.000");
		arcStartSegment2.setattrvalue("C2", "85000.000");
		IomObject arcSegment2=arcSegments2.addattrobj("segment", "ARC");
		arcSegment2.setattrvalue("A1", "480000.000");
		arcSegment2.setattrvalue("A2", "80000.000");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objSurfaceSuccessCondition));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute surface2d of ExistenceConstraints23Coords.Topic.ClassSurface2d was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 3d Oberflaechen constraint Attribute nicht uebereinstimmen.
	@Test
	public void diffModels3dSurface_Fail(){
		Iom_jObject objSurfaceSuccessCondition=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassSurface", OID1);
		IomObject multisurfaceValueCondtition=objSurfaceSuccessCondition.addattrobj("surface3d", "MULTISURFACE");
		IomObject surfaceValueCondition = multisurfaceValueCondtition.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValueCondition.addattrobj("boundary", "BOUNDARY");
		// polyline 1
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "1000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		endSegment.setattrvalue("C3", "1000.000");
		// polyline 2
		IomObject polylineValue11 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments11=polylineValue11.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment11=segments11.addattrobj("segment", "COORD");
		startSegment11.setattrvalue("C1", "500000.000");
		startSegment11.setattrvalue("C2", "80000.000");
		startSegment11.setattrvalue("C3", "1000.000");
		IomObject endSegment11=segments11.addattrobj("segment", "COORD");
		endSegment11.setattrvalue("C1", "600000.000");
		endSegment11.setattrvalue("C2", "85000.000");
		endSegment11.setattrvalue("C3", "1000.000");
		// arc 1
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "600000.000");
		startSegment3.setattrvalue("C2", "85000.000");
		startSegment3.setattrvalue("C3", "1000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "ARC");
		endSegment3.setattrvalue("A1", "480000.000");
		endSegment3.setattrvalue("A2", "80000.000");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		endSegment3.setattrvalue("C3", "1000.000");
		Iom_jObject objSurfaceSuccess=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassSurface3d", OID2);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("surface3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// 1. polyline
		IomObject polylineValue1 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments1=polylineValue1.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment1=segments1.addattrobj("segment", "COORD");
		startSegment1.setattrvalue("C1", "480000.000");
		startSegment1.setattrvalue("C2", "70000.000");
		startSegment1.setattrvalue("C3", "1000.000");
		IomObject endSegment1=segments1.addattrobj("segment", "COORD");
		endSegment1.setattrvalue("C1", "500000.000");
		endSegment1.setattrvalue("C2", "80000.000");
		endSegment1.setattrvalue("C3", "1000.000");
		// 2. polyline
		IomObject polylineValue12 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments12=polylineValue12.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment12=segments12.addattrobj("segment", "COORD");
		startSegment12.setattrvalue("C1", "500000.000");
		startSegment12.setattrvalue("C2", "80000.000");
		startSegment12.setattrvalue("C3", "1000.000");
		IomObject endSegment12=segments12.addattrobj("segment", "COORD");
		endSegment12.setattrvalue("C1", "520000.000");
		endSegment12.setattrvalue("C2", "85000.000");
		endSegment12.setattrvalue("C3", "1000.000");
		// arc 1
		IomObject arcValue=outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject arcSegments2=arcValue.addattrobj("sequence", "SEGMENTS");
		IomObject arcStartSegment2=arcSegments2.addattrobj("segment", "COORD");
		arcStartSegment2.setattrvalue("C1", "520000.000");
		arcStartSegment2.setattrvalue("C2", "85000.000");
		arcStartSegment2.setattrvalue("C3", "1000.000");
		IomObject arcSegment2=arcSegments2.addattrobj("segment", "ARC");
		arcSegment2.setattrvalue("A1", "480000.000");
		arcSegment2.setattrvalue("A2", "80000.000");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		arcSegment2.setattrvalue("C3", "1000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objSurfaceSuccessCondition));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute surface3d of ExistenceConstraints23Coords.Topic.ClassSurface3d was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 2d Polygone constraint Attribute nicht uebereinstimmen.
	@Test
	public void diffModels2dArea_Fail(){
		Iom_jObject objSurfaceSuccessCondition=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassArea", OID1);
		IomObject multisurfaceValueCondtition=objSurfaceSuccessCondition.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValueCondition = multisurfaceValueCondtition.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValueCondition.addattrobj("boundary", "BOUNDARY");
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
		endSegment3.setattrvalue("A1", "480000.000");
		endSegment3.setattrvalue("A2", "80000.000");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		Iom_jObject objSurfaceSuccess=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassArea2d", OID2);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area2d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
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
		endSegment12.setattrvalue("C1", "600000.000");
		endSegment12.setattrvalue("C2", "85000.000");
		// arc 1
		IomObject arcValue=outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject arcSegments2=arcValue.addattrobj("sequence", "SEGMENTS");
		IomObject arcStartSegment2=arcSegments2.addattrobj("segment", "COORD");
		arcStartSegment2.setattrvalue("C1", "600000.000");
		arcStartSegment2.setattrvalue("C2", "85000.000");
		IomObject arcSegment2=arcSegments2.addattrobj("segment", "ARC");
		arcSegment2.setattrvalue("A1", "480000.000");
		arcSegment2.setattrvalue("A2", "80000.000");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objSurfaceSuccessCondition));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute area2d of ExistenceConstraints23Coords.Topic.ClassArea2d was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die beiden 3d Area constraint Attribute nicht uebereinstimmen.
	@Test
	public void diffModels3dArea_Fail(){
		Iom_jObject objSurfaceSuccessCondition=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassArea", OID1);
		IomObject multisurfaceValueCondtition=objSurfaceSuccessCondition.addattrobj("area3d", "MULTISURFACE");
		IomObject surfaceValueCondition = multisurfaceValueCondtition.addattrobj("surface", "SURFACE");
		IomObject outerBoundary = surfaceValueCondition.addattrobj("boundary", "BOUNDARY");
		// polyline 1
		IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "1000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "500000.000");
		endSegment.setattrvalue("C2", "80000.000");
		endSegment.setattrvalue("C3", "1000.000");
		// polyline 2
		IomObject polylineValue11 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments11=polylineValue11.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment11=segments11.addattrobj("segment", "COORD");
		startSegment11.setattrvalue("C1", "500000.000");
		startSegment11.setattrvalue("C2", "80000.000");
		startSegment11.setattrvalue("C3", "1000.000");
		IomObject endSegment11=segments11.addattrobj("segment", "COORD");
		endSegment11.setattrvalue("C1", "520000.000");
		endSegment11.setattrvalue("C2", "85000.000");
		endSegment11.setattrvalue("C3", "1000.000");
		// arc 1
		IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
		IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment3=segments3.addattrobj("segment", "COORD");
		startSegment3.setattrvalue("C1", "520000.000");
		startSegment3.setattrvalue("C2", "85000.000");
		startSegment3.setattrvalue("C3", "1000.000");
		IomObject endSegment3=segments3.addattrobj("segment", "ARC");
		endSegment3.setattrvalue("A1", "480000.000");
		endSegment3.setattrvalue("A2", "80000.000");
		endSegment3.setattrvalue("C1", "480000.000");
		endSegment3.setattrvalue("C2", "70000.000");
		endSegment3.setattrvalue("C3", "1000.000");
		Iom_jObject objSurfaceSuccess=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassArea3d", OID2);
		IomObject multisurfaceValue=objSurfaceSuccess.addattrobj("area3d", "MULTISURFACE");
		IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
		IomObject outerBoundary2 = surfaceValue.addattrobj("boundary", "BOUNDARY");
		// 1. polyline
		IomObject polylineValue1 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments1=polylineValue1.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment1=segments1.addattrobj("segment", "COORD");
		startSegment1.setattrvalue("C1", "480000.000");
		startSegment1.setattrvalue("C2", "70000.000");
		startSegment1.setattrvalue("C3", "1000.000");
		IomObject endSegment1=segments1.addattrobj("segment", "COORD");
		endSegment1.setattrvalue("C1", "500000.000");
		endSegment1.setattrvalue("C2", "80000.000");
		endSegment1.setattrvalue("C3", "1000.000");
		// 2. polyline
		IomObject polylineValue12 = outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject segments12=polylineValue12.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment12=segments12.addattrobj("segment", "COORD");
		startSegment12.setattrvalue("C1", "500000.000");
		startSegment12.setattrvalue("C2", "80000.000");
		startSegment12.setattrvalue("C3", "1000.000");
		IomObject endSegment12=segments12.addattrobj("segment", "COORD");
		endSegment12.setattrvalue("C1", "600000.000");
		endSegment12.setattrvalue("C2", "85000.000");
		endSegment12.setattrvalue("C3", "1000.000");
		// arc 1
		IomObject arcValue=outerBoundary2.addattrobj("polyline", "POLYLINE");
		IomObject arcSegments2=arcValue.addattrobj("sequence", "SEGMENTS");
		IomObject arcStartSegment2=arcSegments2.addattrobj("segment", "COORD");
		arcStartSegment2.setattrvalue("C1", "600000.000");
		arcStartSegment2.setattrvalue("C2", "85000.000");
		arcStartSegment2.setattrvalue("C3", "1000.000");
		IomObject arcSegment2=arcSegments2.addattrobj("segment", "ARC");
		arcSegment2.setattrvalue("A1", "480000.000");
		arcSegment2.setattrvalue("A2", "80000.000");
		arcSegment2.setattrvalue("C1", "480000.000");
		arcSegment2.setattrvalue("C2", "70000.000");
		arcSegment2.setattrvalue("C3", "1000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objSurfaceSuccessCondition));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic",BID2));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute area3d of ExistenceConstraints23Coords.Topic.ClassArea3d was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob innerhalb einer Struktur eine Existence Constraint erstellt werden kann.
	// Es muss eine Fehlermeldung ausgegeben werden, da die Attributewerte nicht uebereinstimmen.
	@Test
	public void existenceConstraintInStructure_Fail() throws Exception {
		Iom_jObject conditionClass=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClass", OID1);
		conditionClass.setattrvalue("attr1", "lars");
		Iom_jObject baseClassStruct=new Iom_jObject("ExistenceConstraints23.Topic.structureBase", null);
		baseClassStruct.setattrvalue("attr1", "urs");
		Iom_jObject baseClassD=new Iom_jObject("ExistenceConstraints23.Topic.ClassD", OID2);
		baseClassD.addattrobj("attr1", baseClassStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID1));
		validator.validate(new ObjectEvent(conditionClass));
		validator.validate(new ObjectEvent(baseClassD));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("The value of the attribute attr1 of ExistenceConstraints23.Topic.structureBase was not found in the condition class.",logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn
	// - innerhalb einer Klasse, auf eine Struktur in einem anderen Model verwiesen wird.
	// - Die Existence-Constraint auf eine Klasse in einem anderen Model verweist.
	// - Die Attribute-Werte nicht miteinander uebereinstimmen.
	@Test
	public void existenceConstraint_structuredValue_Fail() throws Exception {
		Iom_jObject structureValueBedingung=new Iom_jObject("ExistenceConstraints23Condition.structure", null);
		structureValueBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objB=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassStructure", OID1);
		objB.addattrobj("attr1", structureValueBedingung);
		Iom_jObject structureValueBedingung2=new Iom_jObject("ExistenceConstraints23Condition.structure", null);
		structureValueBedingung2.setattrvalue("attr1", "urs");
		Iom_jObject objC=new Iom_jObject("ExistenceConstraints23.Topic.ClassStructureOtherModel", OID2);
		objC.addattrobj("attr1", structureValueBedingung2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(objB));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID2));
		validator.validate(new ObjectEvent(objC));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr1 of ExistenceConstraints23.Topic.ClassStructureOtherModel was not found in the condition class.",logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn
	// - innerhalb einer Klasse, ueber mehrere Strukturen in einem anderen Model verwiesen wird.
	// - Die Existence-Constraint auf eine Klasse in einem anderen Model verweist.
	// - Die Attribute-Werte nicht miteinander uebereinstimmen.
	@Test
	public void existenceConstraint_nestedStructures_Fail() throws Exception {
		Iom_jObject conditionStruct=new Iom_jObject("ExistenceConstraints23Condition.structure", null);
		conditionStruct.setattrvalue("attr1", "lars");
		Iom_jObject conditionStruct3=new Iom_jObject("ExistenceConstraints23Condition.structure", null);
		conditionStruct3.setattrvalue("attr1", "urs");
		Iom_jObject conditionStruct4=new Iom_jObject("ExistenceConstraints23Condition.structure2", null);
		conditionStruct4.addattrobj("attr1", conditionStruct3);
		Iom_jObject conditionStruct2=new Iom_jObject("ExistenceConstraints23Condition.structure2", null);
		conditionStruct2.addattrobj("attr1", conditionStruct);
		Iom_jObject conditionClass=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassStructure2", OID1);
		conditionClass.addattrobj("attr1", conditionStruct2);
		Iom_jObject objC=new Iom_jObject("ExistenceConstraints23.Topic.ClassStructureOtherModel2", OID2);
		// base class references to condition struct.
		objC.addattrobj("attr1", conditionStruct4);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic",BID1));
		validator.validate(new ObjectEvent(conditionClass));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID2));
		validator.validate(new ObjectEvent(objC));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr1 of ExistenceConstraints23.Topic.ClassStructureOtherModel2 was not found in the condition class.",logger.getErrs().get(0).getEventMsg());
	}
	
	// Prueft die Konfiguration: constraint validation.
	// Die Konfiguration ist nicht gesetzt.
	// Es wird eine Fehlermeldung erwartet.
	@Test
	public void sameModelDifferentAttrs_ConstraintDisableSet_NotSet_Fail() throws Exception {
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClass", OID1);
		objBedingung.setattrvalue("attr1", "other");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", OID2);
		objA.setattrvalue("attr5", "lars");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID1));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr5 of ExistenceConstraints23.Topic.ClassA was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Prueft die Konfiguration: constraint validation.
	// Die Konfiguration ist Eingeschaltet.
	// Es wird eine Fehlermeldung erwartet.
	@Test
	public void sameModelDifferentAttrs_ConstraintDisableSet_ON_Fail() throws Exception {
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClass", OID1);
		objBedingung.setattrvalue("attr1", "other");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", OID2);
		objA.setattrvalue("attr5", "lars");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.CONSTRAINT_VALIDATION, ValidationConfig.ON);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID1));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr5 of ExistenceConstraints23.Topic.ClassA was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Prueft die Konfiguration: constraint validation.
	// Die Konfiguration ist Ausgeschaltet.
	// Es wird erwartet dass keine Fehlermeldung ausgegeben wird.
	@Test
	public void sameModelDifferentAttrs_ConstraintDisableSet_OFF_Ok() throws Exception {
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClass", OID1);
		objBedingung.setattrvalue("attr1", "other");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", OID2);
		objA.setattrvalue("attr5", "lars");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.CONSTRAINT_VALIDATION, ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23.Topic",BID1));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
}