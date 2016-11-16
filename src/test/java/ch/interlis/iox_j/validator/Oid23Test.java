package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;

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

public class Oid23Test {

private TransferDescription td=null;
	
@Before
public void setUp() throws Exception {
	// ili-datei lesen
	Configuration ili2cConfig=new Configuration();
	FileEntry existenceConstraintsConditionClass=new FileEntry("src/test/data/validator/ExistenceConstraints23Condition.ili", FileEntryKind.ILIMODELFILE);
	ili2cConfig.addFileEntry(existenceConstraintsConditionClass);
	FileEntry existenceConstraints23=new FileEntry("src/test/data/validator/ExistenceConstraints23.ili", FileEntryKind.ILIMODELFILE);
	ili2cConfig.addFileEntry(existenceConstraints23);
	FileEntry existenceConstraints23Coords=new FileEntry("src/test/data/validator/ExistenceConstraints23Coords.ili", FileEntryKind.ILIMODELFILE);
	ili2cConfig.addFileEntry(existenceConstraints23Coords);
	FileEntry association=new FileEntry("src/test/data/validator/Association23.ili", FileEntryKind.ILIMODELFILE);
	ili2cConfig.addFileEntry(association);
	FileEntry datatypes23=new FileEntry("src/test/data/validator/Datatypes23.ili", FileEntryKind.ILIMODELFILE);
	ili2cConfig.addFileEntry(datatypes23);
	FileEntry referenceType23=new FileEntry("src/test/data/validator/ReferenceType23.ili", FileEntryKind.ILIMODELFILE);
	ili2cConfig.addFileEntry(referenceType23);
	td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
	assertNotNull(td);
}

	@Test
	public void ok_DifferentOIDDefined() throws Exception {
		Iom_jObject objBedingung=new Iom_jObject("ExistenceConstraints23Condition.Topic.ConditionClass", "a1");
		objBedingung.setattrvalue("attr1", "lars");
		Iom_jObject objB=new Iom_jObject("ExistenceConstraints23.Topic.ClassB", "b2");
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
	public void ok_ClassOIDDefined(){
		Iom_jObject objTrue=new Iom_jObject("Datatypes23.Topic.ClassA", "o1");
		objTrue.setattrvalue("aBoolean", "true");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objTrue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	@Test
	public void ok_AssociatianOIDDefined(){
		final String OBJ_A1="a1";
		final String OBJ_B1="b1";
		Iom_jObject iomObjA=new Iom_jObject("Association23.Topic.ClassA", "a1");
		Iom_jObject iomObjB=new Iom_jObject("Association23.Topic.ClassB", "b1");
		Iom_jObject iomObjAB=new Iom_jObject("Association23.Topic.ab2", "c1");
		iomObjAB.addattrobj("a2", "REF").setobjectrefoid(OBJ_A1);
		iomObjAB.addattrobj("b2", "REF").setobjectrefoid(OBJ_B1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Association23.Topic","b2"));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjAB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	@Test
	public void ok_AssociatianOIDUndefined(){
		final String OBJ_A1="a1";
		final String OBJ_B1="b1";
		Iom_jObject iomObjA=new Iom_jObject("Association23.Topic.ClassA", "a1");
		Iom_jObject iomObjB=new Iom_jObject("Association23.Topic.ClassB", "b1");
		Iom_jObject iomObjAB=new Iom_jObject("Association23.Topic.ab2", null);
		iomObjAB.addattrobj("a2", "REF").setobjectrefoid(OBJ_A1);
		iomObjAB.addattrobj("b2", "REF").setobjectrefoid(OBJ_B1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Association23.Topic","b2"));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjAB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	

	@Test
	public void ok_ReferenceOIDDefined(){
		String objTargetId="o1";
		Iom_jObject iomObjtarget=new Iom_jObject("ReferenceType23.Topic.ClassAp", objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", "d1");
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject("ReferenceType23.Topic.StructC", null);
		iomStruct.addattrobj("attrC2", o1Ref);
		Iom_jObject iomObj=new Iom_jObject("ReferenceType23.Topic.ClassD", "o2");
		iomObj.addattrobj("attrD2", iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ReferenceType23.Topic","b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new ObjectEvent(iomObjtarget));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void ok_ReferenceOIDUnDefined(){
		String objTargetId="o1";
		Iom_jObject iomObjtarget=new Iom_jObject("ReferenceType23.Topic.ClassAp", objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject("ReferenceType23.Topic.StructC", null);
		iomStruct.addattrobj("attrC2", o1Ref);
		Iom_jObject iomObj=new Iom_jObject("ReferenceType23.Topic.ClassD", "o2");
		iomObj.addattrobj("attrD2", iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ReferenceType23.Topic","b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new ObjectEvent(iomObjtarget));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void ok_CompositionStructOIDDefined(){
		String objTargetId="o1";
		Iom_jObject iomObjtarget=new Iom_jObject("ReferenceType23.Topic.ClassAp", objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject("ReferenceType23.Topic.StructC", "f1");
		iomStruct.addattrobj("attrC2", o1Ref);
		Iom_jObject iomObj=new Iom_jObject("ReferenceType23.Topic.ClassD", "o2");
		iomObj.addattrobj("attrD2", iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ReferenceType23.Topic","b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new ObjectEvent(iomObjtarget));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	@Test
	public void ok_CompositionStructOIDUnDefined(){
		String objTargetId="o1";
		Iom_jObject iomObjtarget=new Iom_jObject("ReferenceType23.Topic.ClassAp", objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject("ReferenceType23.Topic.StructC", null);
		iomStruct.addattrobj("attrC2", o1Ref);
		Iom_jObject iomObj=new Iom_jObject("ReferenceType23.Topic.ClassD", "o2");
		iomObj.addattrobj("attrD2", iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ReferenceType23.Topic","b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new ObjectEvent(iomObjtarget));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	
	
	@Test
	public void fail_SameOIDDefined() throws Exception {
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
		assertEquals("The OID number: o1 of CLASS ExistenceConstraints23.Topic.ClassB has already been defined by CLASS ExistenceConstraints23Condition.Topic.ConditionClass.", logger.getErrs().get(0).getEventMsg());
	}
	
	@Test
	public void fail_ClassOIDUnDefined(){
		Iom_jObject objTrue=new Iom_jObject("Datatypes23.Topic.ClassA", null);
		objTrue.setattrvalue("aBoolean", "true");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("Datatypes23.Topic","b1"));
		validator.validate(new ObjectEvent(objTrue));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("An OID for class CLASS Datatypes23.Topic.ClassA is mandatory.", logger.getErrs().get(0).getEventMsg());
	}
}