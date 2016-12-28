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
	// MODEL.TOPIC
	private final static String REFERENCETYPE10_TOPICA="ReferenceType10.TopicA";
	// CLASS
	private final static String REFERENCETYPE10_CLASSA=REFERENCETYPE10_TOPICA+".ClassA";
	private final static String REFERENCETYPE10_CLASSB=REFERENCETYPE10_TOPICA+".ClassB";
	private final static String REFERENCETYPE10_CLASSC=REFERENCETYPE10_TOPICA+".ClassC";
	private final static String REFERENCETYPE10_CLASSD=REFERENCETYPE10_TOPICA+".ClassD";
	private final static String ATTR_C1="attrC1";
	private final static String ATTR_C2="attrC2";
	// BID
	private final static String BID1 = "x1";
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
	@Test
	public void referenceOk() throws Exception {
		Iom_jObject objA1=new Iom_jObject(REFERENCETYPE10_CLASSA, OID1);
		Iom_jObject objB1=new Iom_jObject(REFERENCETYPE10_CLASSB, OID1);
		Iom_jObject objC1=new Iom_jObject(REFERENCETYPE10_CLASSC, OID1);
		objC1.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID1);
		objC1.addattrobj(ATTR_C2, "REF").setobjectrefoid(OID1);
		Iom_jObject objD1=new Iom_jObject(REFERENCETYPE10_CLASSD, OID1);
		objD1.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_DO_ITF_OIDPERTABLE, Validator.CONFIG_DO_ITF_OIDPERTABLE_DO);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(REFERENCETYPE10_TOPICA,BID1));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new ObjectEvent(objD1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	//#########################################################//
	//######################### FAIL ##########################//
	//#########################################################//
	@Test
	public void danglingReferenceFail() throws Exception {
		Iom_jObject objA1=new Iom_jObject(REFERENCETYPE10_CLASSA, OID1);
		Iom_jObject objB1=new Iom_jObject(REFERENCETYPE10_CLASSB, OID2);
		Iom_jObject objC1=new Iom_jObject(REFERENCETYPE10_CLASSC, OID1);
		objC1.addattrobj(ATTR_C1, "REF").setobjectrefoid(OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		settings.setValue(Validator.CONFIG_DO_ITF_OIDPERTABLE, Validator.CONFIG_DO_ITF_OIDPERTABLE_DO);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(REFERENCETYPE10_TOPICA,BID1));
		validator.validate(new ObjectEvent(objA1));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new ObjectEvent(objC1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("OID o2 not found in target class ReferenceType10.TopicA.ClassA.", logger.getErrs().get(0).getEventMsg());
	}
}