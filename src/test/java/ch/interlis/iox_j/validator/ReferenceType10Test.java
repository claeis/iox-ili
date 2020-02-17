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

public class ReferenceType10Test {
	// OID
	private final static String OID1="o1";
	private final static String OID2="o2";
	private final static String OID3="o3";
	private final static String OID4="o4";
	// MODEL.TOPIC
	private final static String TOPICA="ReferenceType10.TopicA";
	// CLASS
	private final static String CLASSA=TOPICA+".ClassA";
	private final static String CLASSB=TOPICA+".ClassB";
	private final static String CLASSC=TOPICA+".ClassC";
	private final static String CLASSD=TOPICA+".ClassD";
	private final static String ATTR_C1="attrC1";
	private final static String ATTR_C2="attrC2";
	// BID
	private final static String BID1 = "b1";
	private TransferDescription td=null;
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry iliFile=new FileEntry("src/test/data/validator/ReferenceType10.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(iliFile);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}

	//#########################################################//
	//####################### SUCCESS #########################//
	//#########################################################//
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn 2 Objekte, unterschiedlicher Klasse, die gleiche Referenzklasse haben.
	@Test
	public void twoObjectsWithSameReferenceClass_Ok() throws Exception {
		Iom_jObject objA1=new Iom_jObject(CLASSA, OID1);
		Iom_jObject objB1=new Iom_jObject(CLASSB, OID1);
		Iom_jObject objC1=new Iom_jObject(CLASSC, OID1);
		objC1.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID1);
		objC1.addattrobj(ATTR_C2, "REF").setobjectrefoid(OID1);
		Iom_jObject objD1=new Iom_jObject(CLASSD, OID1);
		objD1.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPICA,BID1));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new ObjectEvent(objD1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn 3 Klassen eine Referenz zur selben Klasse haben.
	@Test
	public void threeReferencesTo1Class_Ok() throws Exception {
		Iom_jObject objA1=new Iom_jObject(CLASSA, OID1);
		Iom_jObject objC1=new Iom_jObject(CLASSC, OID1);
		objC1.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID1);
		Iom_jObject objC2=new Iom_jObject(CLASSC, OID2);
		objC2.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID1);
		Iom_jObject objC3=new Iom_jObject(CLASSC, OID4);
		objC3.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPICA,BID1));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new ObjectEvent(objC2));
		validator.validate(new ObjectEvent(objC3));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
		

	
	//#########################################################//
	//######################### FAIL ##########################//
	//#########################################################//
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn eine falsche Zielklasse referenziert wird.
	@Test
	public void wrongTargetClass_Fail() throws Exception {
		Iom_jObject objB1=new Iom_jObject(CLASSB, OID1);
		Iom_jObject objC1=new Iom_jObject(CLASSC, OID1);
		objC1.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPICA,BID1));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1, logger.getErrs().size());
		assertEquals("No object found with OID o1 in basket b1.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn die Referenzklasse falsch ist.
	@Test
	public void danglingReference_Fail() throws Exception {
		Iom_jObject objA1=new Iom_jObject(CLASSA, OID1);
		Iom_jObject objB1=new Iom_jObject(CLASSB, OID2);
		Iom_jObject objC1=new Iom_jObject(CLASSC, OID1);
		objC1.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPICA,BID1));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1, logger.getErrs().size());
		assertEquals("No object found with OID o2 in basket b1.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn eine Klasse mit einer Referenz zu 3 Mal der gleichen Klasse referenziert.
	@Test
	public void wrongReference_Fail() throws Exception {
		Iom_jObject objA1=new Iom_jObject(CLASSA, OID1);
		Iom_jObject objA2=new Iom_jObject(CLASSA, OID2);
		Iom_jObject objA3=new Iom_jObject(CLASSA, OID3);
		Iom_jObject objC1=new Iom_jObject(CLASSC, OID1);
		objC1.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID1);
		objC1.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID2);
		objC1.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPICA,BID1));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objA2));
		validator.validate(new ObjectEvent(objA3));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("Role ReferenceType10.TopicA.ClassCattrC1.attrC1 requires only one reference property",logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn config target auf ON steht und die eigene oid als Klassenreferenz angegeben wird.
	@Test
	public void configTargetON_NoClassFoundInRef_Fail() throws Exception {
		Iom_jObject objC1=new Iom_jObject(CLASSC, OID1);
		objC1.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPICA,BID1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1, logger.getErrs().size());
		assertEquals("No object found with OID o1 in basket b1.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler geworfen wird, wenn multiplicity eingeschalten wird und die Oid's identisch sind.
	@Test
	public void configMultiplicityON_TwoReferencedClassesWithSameOid_Fail() throws Exception {
		Iom_jObject iomObjI=new Iom_jObject(CLASSA, OID1);
		Iom_jObject iomObjJ=new Iom_jObject(CLASSC,OID2);
		iomObjJ.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID1);
		Iom_jObject iomObjJ2=new Iom_jObject(CLASSC,OID2);
		iomObjJ2.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPICA,BID1));
		validator.validate(new ObjectEvent(iomObjI));
		validator.validate(new ObjectEvent(iomObjJ));
		validator.validate(new ObjectEvent(iomObjJ2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(2, logger.getErrs().size());
		assertEquals("OID o2 of object ReferenceType10.TopicA.ClassC already exists in CLASS ReferenceType10.TopicA.ClassC.", logger.getErrs().get(0).getEventMsg());
		assertEquals("attrC1 should associate 1 to 1 target objects (instead of 2)", logger.getErrs().get(1).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler geworfen wird, wenn multiplicity eingeschalten ist und die referenzierten Klassen die Selbe oid haben und nicht existieren.
	@Test
	public void configAndMultiplicityON_TwoReferencedClassesWithSameOid_Fail() throws Exception {
		Iom_jObject iomObjI=new Iom_jObject(CLASSA, OID3);
		Iom_jObject iomObjJ=new Iom_jObject(CLASSC,OID2);
		iomObjJ.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID1);
		Iom_jObject iomObjJ2=new Iom_jObject(CLASSC,OID2);
		iomObjJ2.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPICA,BID1));
		validator.validate(new ObjectEvent(iomObjI));
		validator.validate(new ObjectEvent(iomObjJ));
		validator.validate(new ObjectEvent(iomObjJ2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(3, logger.getErrs().size());
		assertEquals("OID o2 of object ReferenceType10.TopicA.ClassC already exists in CLASS ReferenceType10.TopicA.ClassC.", logger.getErrs().get(0).getEventMsg());
		assertEquals("No object found with OID o1 in basket b1.", logger.getErrs().get(1).getEventMsg());
		assertEquals("attrC1 should associate 1 to 1 target objects (instead of 2)", logger.getErrs().get(2).getEventMsg());
	}
}