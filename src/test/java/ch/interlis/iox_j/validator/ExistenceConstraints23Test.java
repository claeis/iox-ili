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
	//////////////// CONDITIONCLASS + CLASS A /////////////////////
	@Test
	public void existenceConstraintInSameModel() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClass", "o1");
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", "o2");
		objA.setattrvalue("attr5", "lars");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	@Test
	public void existenceConstraintInSameModelR() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClass", "o1");
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", "o2");
		objA.setattrvalue("attr5", "lars");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b1"));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	////////////////END CONDITIONCLASS + CLASS A //////////////////
	////////////////CONDITIONCLASS2 + CLASS A /////////////////////
	@Test
	public void existenceConstraintInSameModel2() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClass2", "o1");
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", "o2");
		objA.setattrvalue("attr5", "lars");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	//////////////// END CONDITIONCLASS2 + CLASS A ////////////////
	////////////////START CONDITIONCLASSX + CLASS A ///////////////
	@Test
	public void existenceConstraintInSameModelX() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClassX", "o1");
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", "o2");
		objA.setattrvalue("attr5", "lars");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	////////////////END CONDITIONCLASSX + CLASS A /////////////////
	////////////////// START CONDITIONCLASS + CLASSB //////////////
	@Test
	public void existenceConstraintInOtherModel() throws Exception {
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClass", "o1");
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objB=new Iom_jObject("ExistenceConstraints23.Topic.ClassB", "o2");
		objB.setattrvalue("attr1", "lars");
		objB.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b2"));
		validator.validate(new ObjectEvent(objB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	@Test
	public void existenceConstraintInOtherModelR() throws Exception {
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClass", "o1");
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objB=new Iom_jObject("ExistenceConstraints23.Topic.ClassB", "o2");
		objB.setattrvalue("attr1", "lars");
		objB.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b2"));
		validator.validate(new ObjectEvent(objB));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	////////////////// END CONDITIONCLASS + CLASSB ///////////////
	//////////////////// START STRUCTURE /////////////////////////
//	@Test
//	public void existenceConstraintStructure() throws Exception {
//		Iom_jObject structureValueBedingung=new Iom_jObject("ExistenceConstraints23Condition.structure", null);
//		structureValueBedingung.setattrvalue("attr1", "lars");
//		structureValueBedingung.setattrvalue("attr2", "andre");
//		structureValueBedingung.setattrvalue("attr3", "james");
//		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassStructure", "o1");
//		objBedingung.addattrobj("attr1", structureValueBedingung);
//		Iom_jObject structureValueB=new Iom_jObject("ExistenceConstraints23Condition.structure", null);
//		structureValueB.setattrvalue("attr1", "lars");
//		structureValueB.setattrvalue("attr2", "andre");
//		structureValueB.setattrvalue("attr3", "james");
//		Iom_jObject objB=new Iom_jObject("ExistenceConstraints23.Topic.ClassStructureOtherModel", "o2");
//		objB.addattrobj("attr1", structureValueB);
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b1"));
//		validator.validate(new ObjectEvent(objB));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b2"));
//		validator.validate(new ObjectEvent(objBedingung));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==0);
//	}
	////////////////// END STRUCTURE ///////////////
	////////////////// START COORDTYPE 1D //////////
	@Test
	public void ExistenceConstraintCoordType1d(){
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", "o1");
		IomObject conditionValue=conditionObj.addattrobj("attr0", "COORD");
		conditionValue.setattrvalue("C1", "480000.000");
		Iom_jObject objCoord=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassCoord1d", "o2");
		IomObject coordValue=objCoord.addattrobj("attr1", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic","b2"));
		validator.validate(new ObjectEvent(objCoord));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	////////////////// END COORDTYPE 1D ///////////////
	////////////////// START COORDTYPE 2D //////////
	@Test
	public void ExistenceConstraintCoordType2d(){
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", "o1");
		IomObject conditionValue=conditionObj.addattrobj("attr1", "COORD");
		conditionValue.setattrvalue("C1", "480000.000");
		conditionValue.setattrvalue("C2", "70000.000");
		Iom_jObject objCoord=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassCoord2d", "o2");
		IomObject coordValue=objCoord.addattrobj("attr1", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic","b2"));
		validator.validate(new ObjectEvent(objCoord));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	////////////////// END COORDTYPE 2D ///////////////
	////////////////// START COORDTYPE 3D /////////////
	@Test
	public void ExistenceConstraintCoordType3d(){
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", "o1");
		IomObject conditionValue=conditionObj.addattrobj("attr2", "COORD");
		conditionValue.setattrvalue("C1", "480000.000");
		conditionValue.setattrvalue("C2", "70000.000");
		conditionValue.setattrvalue("C3", "4000.000");
		Iom_jObject objCoord=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassCoord3d", "o2");
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
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic","b2"));
		validator.validate(new ObjectEvent(objCoord));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	////////////////// END COORDTYPE 3D /////////////////////////
	////////////////// START POLYLINETYPE STRAIGHTS 2D //////////
	@Test
	public void polylineTypeStraights2dOk(){
		// Polyline in Condition Class
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassLine", "o1");
		IomObject conditionPolyline=conditionObj.addattrobj("straights2d", "POLYLINE");
		IomObject conditionSegment=conditionPolyline.addattrobj("sequence", "SEGMENTS");
		IomObject ConditionCoordStart=conditionSegment.addattrobj("segment", "COORD");
		ConditionCoordStart.setattrvalue("C1", "480000.000");
		ConditionCoordStart.setattrvalue("C2", "70000.000");
		IomObject conditionCoordEnd=conditionSegment.addattrobj("segment", "COORD");
		conditionCoordEnd.setattrvalue("C1", "480000.000");
		conditionCoordEnd.setattrvalue("C2", "70000.000");
		// Polyline in Class
		Iom_jObject obj=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassLine2d", "o2");
		IomObject polylineValue=obj.addattrobj("straights2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coordStart=segments.addattrobj("segment", "COORD");
		coordStart.setattrvalue("C1", "480000.000");
		coordStart.setattrvalue("C2", "70000.000");
		IomObject coordEnd=segments.addattrobj("segment", "COORD");
		coordEnd.setattrvalue("C1", "480000.000");
		coordEnd.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic","b2"));
		validator.validate(new ObjectEvent(obj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	////////////////// END POLYLINETYPE STRAIGHTS 2D /////////////
	////////////////// START POLYLINETYPE STRAIGHTS 3D //////////
	@Test
	public void polylineTypeStraights3dOk(){
		// Polyline in Condition Class
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassLine", "o1");
		IomObject conditionPolyline=conditionObj.addattrobj("straights3d", "POLYLINE");
		IomObject conditionSegment=conditionPolyline.addattrobj("sequence", "SEGMENTS");
		IomObject ConditionCoordStart=conditionSegment.addattrobj("segment", "COORD");
		ConditionCoordStart.setattrvalue("C1", "480000.000");
		ConditionCoordStart.setattrvalue("C2", "70000.000");
		ConditionCoordStart.setattrvalue("C3", "5000.000");
		IomObject conditionCoordEnd=conditionSegment.addattrobj("segment", "COORD");
		conditionCoordEnd.setattrvalue("C1", "480000.000");
		conditionCoordEnd.setattrvalue("C2", "70000.000");
		conditionCoordEnd.setattrvalue("C3", "5000.000");
		// Polyline in Class
		Iom_jObject obj=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassLine3d", "o2");
		IomObject polylineValue=obj.addattrobj("straights3d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coordStart=segments.addattrobj("segment", "COORD");
		coordStart.setattrvalue("C1", "480000.000");
		coordStart.setattrvalue("C2", "70000.000");
		coordStart.setattrvalue("C3", "5000.000");
		IomObject coordEnd=segments.addattrobj("segment", "COORD");
		coordEnd.setattrvalue("C1", "480000.000");
		coordEnd.setattrvalue("C2", "70000.000");
		coordEnd.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic","b2"));
		validator.validate(new ObjectEvent(obj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	////////////////// END POLYLINETYPE STRAIGHTS 3D /////////////
	
	//############################################################
	//################ FAIL TESTS ################################
	//############################################################
	//////////////////// START CONDITIONCLASS + CLASS A //////////
	@Test
	public void existenceConstraintFalseInSameModel() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClass", "o1");
		objBedingung.setattrvalue("attr1", "other");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", "o2");
		objA.setattrvalue("attr5", "lars");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr5 of ExistenceConstraints23.Topic.ClassA was not found in the conditional class.", logger.getErrs().get(0).getEventMsg());
	}
	//////////////////// END CONDITIONCLASS + CLASS A //////////
	//////////////////// START CONDITIONCLASS + CLASS B ////////
	@Test
	public void existenceConstraintExistenceFalseOtherModel() throws Exception {
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClass", "o1");
		objBedingung.setattrvalue("attr1", "other");
		Iom_jObject objB=new Iom_jObject("ExistenceConstraints23.Topic.ClassB", "o2");
		objB.setattrvalue("attr1", "lars");
		objB.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b2"));
		validator.validate(new ObjectEvent(objB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr1 of ExistenceConstraints23.Topic.ClassB was not found in the conditional class.", logger.getErrs().get(0).getEventMsg());
	}
	@Test
	public void existenceConstraintSameOIDo1() throws Exception {
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClass", "o1");
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objB=new Iom_jObject("ExistenceConstraints23.Topic.ClassB", "o1");
		objB.setattrvalue("attr1", "lars");
		objB.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b2"));
		validator.validate(new ObjectEvent(objB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Oid cant exist twice.", logger.getErrs().get(0).getEventMsg());
	}
	//////////////////// END CONDITIONCLASS + CLASS B ///////////
	////////////////CONDITIONCLASS2 + CLASS A /////////////////////
	@Test
	public void existenceConstraintInSameModel2Fail() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClass2", "o1");
		objBedingung.setattrvalue("attr1", "ben");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", "o2");
		objA.setattrvalue("attr5", "lars");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr5 of ExistenceConstraints23.Topic.ClassA was not found in the conditional class.", logger.getErrs().get(0).getEventMsg());
	}
	//////////////// END CONDITIONCLASS2 + CLASS A ////////////////
	////////////////START CONDITIONCLASSX + CLASS A ///////////////
	@Test
	public void existenceConstraintInSameModelXFail() throws Exception{
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23.Topic.ConditionClassX", "o1");
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objA=new Iom_jObject("ExistenceConstraints23.Topic.ClassA", "o2");
		objA.setattrvalue("attr5", "ben");
		objA.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr5 of ExistenceConstraints23.Topic.ClassA was not found in the conditional class.", logger.getErrs().get(0).getEventMsg());
	}
	////////////////END CONDITIONCLASSX + CLASS A /////////////////
	////////////////// START CONDITIONCLASS + CLASSB //////////////
	@Test
	public void existenceConstraintInOtherModelFail() throws Exception {
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClass", "o1");
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objB=new Iom_jObject("ExistenceConstraints23.Topic.ClassB", "o2");
		objB.setattrvalue("attr1", "ben");
		objB.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b2"));
		validator.validate(new ObjectEvent(objB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr1 of ExistenceConstraints23.Topic.ClassB was not found in the conditional class.", logger.getErrs().get(0).getEventMsg());
	}
	@Test
	public void existenceConstraintInOtherModelRFail() throws Exception {
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClass", "o1");
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objB=new Iom_jObject("ExistenceConstraints23.Topic.ClassB", "o2");
		objB.setattrvalue("attr1", "ben");
		objB.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b2"));
		validator.validate(new ObjectEvent(objB));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(objBedingung));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr1 of ExistenceConstraints23.Topic.ClassB was not found in the conditional class.", logger.getErrs().get(0).getEventMsg());
	}
	////////////////// END CONDITIONCLASS + CLASSB ///////////////
	//////////////////// START STRUCTURE /////////////////////////
//	@Test
//	public void existenceConstraintStructureFail() throws Exception {
//		Iom_jObject structureValueBedingung=new Iom_jObject("ExistenceConstraints23Condition.structure", null);
//		structureValueBedingung.setattrvalue("attr1", "lars");
//		structureValueBedingung.setattrvalue("attr2", "andre");
//		structureValueBedingung.setattrvalue("attr3", "james");
//		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassStructure", "o1");
//		objBedingung.addattrobj("attr1", structureValueBedingung);
//		Iom_jObject structureValueB=new Iom_jObject("ExistenceConstraints23Condition.structure", null);
//		structureValueB.setattrvalue("attr1", "lars");
//		structureValueB.setattrvalue("attr2", "andre");
//		structureValueB.setattrvalue("attr3", "ben");
//		Iom_jObject objB=new Iom_jObject("ExistenceConstraints23.Topic.ClassStructureOtherModel", "o2");
//		objB.addattrobj("attr1", structureValueB);
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent("ExistenceContraints23.Topic","b1"));
//		validator.validate(new ObjectEvent(objB));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b2"));
//		validator.validate(new ObjectEvent(objBedingung));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertTrue(logger.getErrs().size()==1);
//		assertEquals("The value of the attribute attr5 of CLASS ExistenceConstraints23.Topic.ClassA was not found in the conditional CLASS ExistenceConstraints23.Topic.ConditionClass2.", logger.getErrs().get(0).getEventMsg());
//	}
	////////////////// END STRUCTURE ///////////////
	////////////////// START COORDTYPE 1D //////////
	@Test
	public void ExistenceConstraintCoordType1dFail(){
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", "o1");
		IomObject conditionValue=conditionObj.addattrobj("attr0", "COORD");
		conditionValue.setattrvalue("C1", "480000.000");
		Iom_jObject objCoord=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassCoord1d", "o2");
		IomObject coordValue=objCoord.addattrobj("attr1", "COORD");
		coordValue.setattrvalue("C1", "480001.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic","b2"));
		validator.validate(new ObjectEvent(objCoord));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr1 of ExistenceConstraints23Coords.Topic.ClassCoord1d was not found in the conditional class.", logger.getErrs().get(0).getEventMsg());
	}
	////////////////// END COORDTYPE 1D ///////////////
	////////////////// START COORDTYPE 2D /////////////
	@Test
	public void ExistenceConstraintCoordType2dFail(){
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", "o1");
		IomObject conditionValue=conditionObj.addattrobj("attr1", "COORD");
		conditionValue.setattrvalue("C1", "480000.000");
		conditionValue.setattrvalue("C2", "70000.000");
		Iom_jObject objCoord=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassCoord2d", "o2");
		IomObject coordValue=objCoord.addattrobj("attr1", "COORD");
		coordValue.setattrvalue("C1", "480000.000");
		coordValue.setattrvalue("C2", "70001.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic","b2"));
		validator.validate(new ObjectEvent(objCoord));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr1 of ExistenceConstraints23Coords.Topic.ClassCoord2d was not found in the conditional class.", logger.getErrs().get(0).getEventMsg());
	}
	////////////////// END COORDTYPE 2D ///////////////
	////////////////// START COORDTYPE 3D /////////////
	@Test
	public void ExistenceConstraintCoordType3dFail(){
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", "o1");
		IomObject conditionValue=conditionObj.addattrobj("attr2", "COORD");
		conditionValue.setattrvalue("C1", "480000.000");
		conditionValue.setattrvalue("C2", "70000.000");
		conditionValue.setattrvalue("C3", "4000.000");
		Iom_jObject objCoord=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassCoord3d", "o2");
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
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic","b2"));
		validator.validate(new ObjectEvent(objCoord));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute attr1 of ExistenceConstraints23Coords.Topic.ClassCoord3d was not found in the conditional class.", logger.getErrs().get(0).getEventMsg());
	}
	////////////////// END COORDTYPE 3D ////////////////////////////
	////////////////// START CONDITION ATTRS UNDEFINED /////////////
	@Test
	public void ExistenceConstraintConditionUndefinedFail(){
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", "o1");
		Iom_jObject objCoord=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassCoord3d", "o2");
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
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic","b2"));
		validator.validate(new ObjectEvent(objCoord));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Object ExistenceConstraints23Condition.Topic.ConditionClassCoord oid o1 {} contains no valid content.", logger.getErrs().get(0).getEventMsg());
	}
	////////////////// END CONDITION ATTRS UNDEFINED //
	////////////////// START CLASS ATTRS UNDEFINED ////
	@Test
	public void ExistenceConstraintClassUndefinedFail(){
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", "o1");
		IomObject conditionValue=conditionObj.addattrobj("attr2", "COORD");
		conditionValue.setattrvalue("C1", "480000.000");
		conditionValue.setattrvalue("C2", "70000.000");
		conditionValue.setattrvalue("C3", "4000.000");
		Iom_jObject objCoord=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassCoord3d", "o2");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic","b2"));
		validator.validate(new ObjectEvent(objCoord));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Object ExistenceConstraints23Coords.Topic.ClassCoord3d oid o2 {} contains no valid content.", logger.getErrs().get(0).getEventMsg());
	}
	////////////////// END CLASS ATTRS UNDEFINED ////////////////
	////////////////// START ALL ATTRS UNDEFINED ////////////////
	@Test
	public void ExistenceConstraintAllUndefinedFail(){
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassCoord", "o1");
		Iom_jObject objCoord=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassCoord3d", "o2");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic","b2"));
		validator.validate(new ObjectEvent(objCoord));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Object ExistenceConstraints23Condition.Topic.ConditionClassCoord oid o1 {} and Object ExistenceConstraints23Coords.Topic.ClassCoord3d oid o2 {} contain no valid content.", logger.getErrs().get(0).getEventMsg());
	}
	////////////////// END ALL ATTRS UNDEFINED //////////////////
	////////////////// START POLYLINETYPE STRAIGHTS 2D //////////
	@Test
	public void polylineTypeStraights2dOkFail(){
		// Polyline in Condition Class
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassLine", "o1");
		IomObject conditionPolyline=conditionObj.addattrobj("straights2d", "POLYLINE");
		IomObject conditionSegment=conditionPolyline.addattrobj("sequence", "SEGMENTS");
		IomObject ConditionCoordStart=conditionSegment.addattrobj("segment", "COORD");
		ConditionCoordStart.setattrvalue("C1", "480000.000");
		ConditionCoordStart.setattrvalue("C2", "70000.000");
		IomObject conditionCoordEnd=conditionSegment.addattrobj("segment", "COORD");
		conditionCoordEnd.setattrvalue("C1", "480000.000");
		conditionCoordEnd.setattrvalue("C2", "70000.000");
		// Polyline in Class
		Iom_jObject obj=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassLine2d", "o2");
		IomObject polylineValue=obj.addattrobj("straights2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coordStart=segments.addattrobj("segment", "COORD");
		coordStart.setattrvalue("C1", "480000.000");
		coordStart.setattrvalue("C2", "70000.000");
		IomObject coordEnd=segments.addattrobj("segment", "COORD");
		coordEnd.setattrvalue("C1", "480000.000");
		coordEnd.setattrvalue("C2", "70001.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic","b2"));
		validator.validate(new ObjectEvent(obj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute straights2d of ExistenceConstraints23Coords.Topic.ClassLine2d was not found in the conditional class.", logger.getErrs().get(0).getEventMsg());
	}
	////////////////// END POLYLINETYPE STRAIGHTS 2D /////////////
	////////////////// START POLYLINETYPE STRAIGHTS 3D //////////
	@Test
	public void polylineTypeStraights3dOkFail(){
		// Polyline in Condition Class
		Iom_jObject conditionObj=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClassLine", "o1");
		IomObject conditionPolyline=conditionObj.addattrobj("straights3d", "POLYLINE");
		IomObject conditionSegment=conditionPolyline.addattrobj("sequence", "SEGMENTS");
		IomObject ConditionCoordStart=conditionSegment.addattrobj("segment", "COORD");
		ConditionCoordStart.setattrvalue("C1", "480000.000");
		ConditionCoordStart.setattrvalue("C2", "70000.000");
		ConditionCoordStart.setattrvalue("C3", "5000.000");
		IomObject conditionCoordEnd=conditionSegment.addattrobj("segment", "COORD");
		conditionCoordEnd.setattrvalue("C1", "480000.000");
		conditionCoordEnd.setattrvalue("C2", "70000.000");
		conditionCoordEnd.setattrvalue("C3", "4999.000");
		// Polyline in Class
		Iom_jObject obj=new Iom_jObject("ExistenceConstraints23Coords.Topic.ClassLine3d", "o2");
		IomObject polylineValue=obj.addattrobj("straights3d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coordStart=segments.addattrobj("segment", "COORD");
		coordStart.setattrvalue("C1", "480000.000");
		coordStart.setattrvalue("C2", "70000.000");
		coordStart.setattrvalue("C3", "5000.000");
		IomObject coordEnd=segments.addattrobj("segment", "COORD");
		coordEnd.setattrvalue("C1", "480000.000");
		coordEnd.setattrvalue("C2", "70000.000");
		coordEnd.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Condition.Topic","b1"));
		validator.validate(new ObjectEvent(conditionObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent("ExistenceConstraints23Coords.Topic","b2"));
		validator.validate(new ObjectEvent(obj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("The value of the attribute straights3d of ExistenceConstraints23Coords.Topic.ClassLine3d was not found in the conditional class.", logger.getErrs().get(0).getEventMsg());
	}
	////////////////// END POLYLINETYPE STRAIGHTS 3D /////////////
}
