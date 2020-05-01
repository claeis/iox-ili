package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
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
	private final static String OID1 ="o1";
    private final static String OID2 ="o2";
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
	private final static String ILI_CLASSCONSTANTJP=TOPIC+".ClassConstantJp";
	private final static String ILI_CLASSCONSTANTJP3=TOPIC+".ClassConstantJp3";
	// ATTRIBUTES EQUALATION (==) SUCCESS AND FAIL
	private final static String ILI_CLASSEQUALATIONE=TOPIC+".ClassEqualE";
	private final static String ILI_CLASSEQUALATIONF=TOPIC+".ClassEqualF";
	private final static String ILI_CLASSEQUALATIONF2=TOPIC+".ClassEqualF2";
	private final static String ILI_CLASSEQUALATIONF3=TOPIC+".ClassEqualF3";
	private final static String ILI_CLASSEQUALATIONG=TOPIC+".ClassEqualG";
	private final static String ILI_CLASSEQUALATIONH=TOPIC+".ClassEqualH";
	private final static String ILI_CLASSEQUALATIONI=TOPIC+".ClassEqualI";
	private final static String ILI_CLASSEQUALATIONIP=TOPIC+".ClassEqualIp";
	// ATTRIBUTES INEQUALATION (!=), (<>) SUCCESS AND FAIL
	private final static String ILI_CLASSINEQUALATIONA=TOPIC+".ClassUnEqualA";
	private final static String ILI_CLASSINEQUALATIONB=TOPIC+".ClassUnEqualB";
	private final static String ILI_CLASSINEQUALATIONC=TOPIC+".ClassUnEqualC";
	private final static String ILI_CLASSINEQUALATIOND=TOPIC+".ClassUnEqualD";
	private final static String ILI_CLASSINEQUALATIONI=TOPIC+".ClassUnEqualI";
	// ATTRIBUTES GREATER THAN (>) SUCCESS AND FAIL
	private final static String ILI_CLASSGREATERTHANA=TOPIC+".ClassGreaterThanA";
	private final static String ILI_CLASSGREATERTHANB=TOPIC+".ClassGreaterThanB";
	// ATTRIBUTES SMALLER THAN (<) SUCCESS AND FAIL
	private final static String ILI_CLASSLESSTHANA=TOPIC+".ClassLessThanA";
	private final static String ILI_CLASSLESSTHANB=TOPIC+".ClassLessThanB";
	// ATTRIBUTES GREATER OR EQUAL THAN (>=) SUCCESS AND FAIL
	private final static String ILI_CLASSGREATERTHANOREQUALA=TOPIC+".ClassGreaterThanOrEqualA";
	private final static String ILI_CLASSGREATERTHANOREQUALB=TOPIC+".ClassGreaterThanOrEqualB";
	// ATTRIBUTES GREATER OR EQUAL THAN (<=) SUCCESS AND FAIL
	private final static String ILI_CLASSLESSTHANOREQUALA=TOPIC+".ClassLessThanOrEqualA";
	private final static String ILI_CLASSLESSTHANOREQUALB=TOPIC+".ClassLessThanOrEqualB";
	// ATTRIBUTES GREATER OR EQUAL THAN (<=) SUCCESS AND FAIL
	private final static String ILI_CLASSDEFINEDA=TOPIC+".ClassDefinedA";
	private final static String ILI_CLASSDEFINEDB=TOPIC+".ClassDefinedB";
    private final static String ILI_CLASSDEFINEDV=TOPIC+".ClassDefinedV";
    private final static String ILI_CLASSDEFINEDW=TOPIC+".ClassDefinedW";
    private final static String ILI_CLASS_URSPRUNG=TOPIC+".ClassUrsprung";
    private final static String ILI_CLASS_HINWEIS=TOPIC+".ClassHinweis";
    // ASSOCIATION CLASS
    private final static String ILI_ASSOC_AB=TOPIC+".HinweisWeitereDokumente";
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCONSTANTA, OID1);
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
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn die aufzaehlung1 auf mehr.vier gesetzt wird.
	@Test
	public void constantEnumeration_Ok(){
		Iom_jObject objClassConstantJ=new Iom_jObject(ILI_CLASSCONSTANTJ, OID1);
		objClassConstantJ.setattrvalue("aufzaehlung1", "mehr.vier");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objClassConstantJ));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn die aufzaehlung1 auf mehr.vier gesetzt wird
	// und die Klasse: ClassConstantJP die Klasse: ClassConstant erweitert.
	@Test
	public void constantEnumerationSub_Ok(){
		Iom_jObject objClassConstantJP=new Iom_jObject(ILI_CLASSCONSTANTJP, OID1);
		objClassConstantJP.setattrvalue("aufzaehlung1", "mehr.vier");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objClassConstantJP));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn die aufzaehlung1 auf mehr.vier gesetzt wird
	// und die Klasse: ClassConstant ueber die Klassen: ClassConstantJP3 --> ClassConstantJP2 --> ClassConstantJP erweitert wird.
	@Test
	public void multipleSubClassesEnumeration_Ok(){
		Iom_jObject objClassConstantJP3=new Iom_jObject(ILI_CLASSCONSTANTJP3, OID1);
		objClassConstantJP3.setattrvalue("aufzaehlung1", "mehr.vier");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objClassConstantJP3));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn eine Konstante true ergibt oder not false ist.
	@Test
	public void constantNegation_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCONSTANTB, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCONSTANTD, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCONSTANTE, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCONSTANTF, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSCONSTANTG, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONI, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONG, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONF, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONF, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONF2, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONF3, OID1);
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
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSEQUALATIONH, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONE, OID1);
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
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden Texte ungleich sind.
	@Test
	public void textUnEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONA, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONB, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONB, OID1);
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
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSINEQUALATIONC, OID1);
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
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSINEQUALATIONC, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIOND, OID1);
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
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden Booleans ungleich sind.
	@Test
	public void booleanUnEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONI, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANA, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANA, OID1);
		iomObjA.setattrvalue("attr1", "5.9");
		iomObjA.setattrvalue("attr2", "5.4");
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
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSGREATERTHANB, OID1);
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
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn attr1 kleiner als attr2 in Numerischen Zahlen ist.
	@Test
	public void numericLessThan_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANA, OID1);
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
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSLESSTHANB, OID1);
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
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn attr1 groesser oder gleich attr2 ist.
	@Test
	public void numericGreaterThanOrEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALA, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALA, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALA, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALA, OID1);
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
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALB, OID1);
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
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALB, OID1);
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
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der Text aus attr1 kleiner oder gleich dem Text aus attr2 ist.
	@Test
	public void textLessThanOrEqual_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANOREQUALA, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANOREQUALA, OID1);
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
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSLESSTHANOREQUALB, OID1);
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
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSLESSTHANOREQUALB, OID1);
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
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die 2d Polyline nur mit Geraden definiert wurde.
	@Test
	public void polyline2dStraightsDefined_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSDEFINEDA, OID1);
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
	
	@Test
	public void assoc_UnEquals_OK() throws Exception {
        Iom_jObject iomObj_Ursprung = new Iom_jObject(ILI_CLASS_URSPRUNG, OID1);
        iomObj_Ursprung.setattrvalue("attrA1", "Attr1"); 
        Iom_jObject iomObj_Hinweis = new Iom_jObject(ILI_CLASS_HINWEIS, OID2);
        iomObj_Hinweis.setattrvalue("attrB1", "Attr2");
        Iom_jObject iomObj_Assoc=new Iom_jObject(ILI_ASSOC_AB, null);
        iomObj_Assoc.addattrobj("Ursprung", "REF").setobjectrefoid(iomObj_Ursprung.getobjectoid());
        iomObj_Assoc.addattrobj("Hinweis", "REF").setobjectrefoid(iomObj_Hinweis.getobjectoid());
        
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(iomObj_Ursprung));
        validator.validate(new ObjectEvent(iomObj_Hinweis));
        validator.validate(new ObjectEvent(iomObj_Assoc));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());	    
	}
	
    @Test
    public void embedded_Defined_Ok() throws Exception {
        Iom_jObject iomObjV = new Iom_jObject(ILI_CLASSDEFINEDV, OID1);
        Iom_jObject iomObjW = new Iom_jObject(ILI_CLASSDEFINEDW, OID2);
        IomObject vw=iomObjW.addattrobj("role_v", "REF");
        vw.setobjectrefoid(iomObjV.getobjectoid());
        vw.setattrvalue("attr", "Attr"); 
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(iomObjV));
        validator.validate(new ObjectEvent(iomObjW));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0, logger.getErrs().size());
    }


	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der formattedType definiert wurde.
	@Test
	public void formattedTypeDefined_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSFORMATTEDTYPEA, OID1);
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
		Iom_jObject objStraightsSuccess=new Iom_jObject(TOPIC+".ClassDiffExpressions", OID1);
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

    @Test
    public void embedded_Defined_Fail() throws Exception {
        Iom_jObject iomObjV = new Iom_jObject(ILI_CLASSDEFINEDV, OID1);
        Iom_jObject iomObjW = new Iom_jObject(ILI_CLASSDEFINEDW, OID2);
        IomObject vw=iomObjW.addattrobj("role_v", "REF");
        vw.setobjectrefoid(iomObjV.getobjectoid());
        //vw.setattrvalue("attr", "Attr"); //should fail because attr is UNDEFINED
        ValidationConfig modelConfig = new ValidationConfig();
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Validator validator = new Validator(td, modelConfig, logger, errFactory, settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC, BID));
        validator.validate(new ObjectEvent(iomObjV));
        validator.validate(new ObjectEvent(iomObjW));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.DefinedVw.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
    }
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die boolean nicht uebereinstimmen.	
	@Test
	public void booleanEqual_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONI, OID1);
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
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn die aufzaehlung1 auf eins gesetzt wird
	// und die Klasse: ClassConstant ueber die Klassen: ClassConstantJP3 --> ClassConstantJP2 --> ClassConstantJP erweitert wird.
	@Test
	public void multipleSubClassesEnumeration_Fail(){
		Iom_jObject objClassConstantJP3=new Iom_jObject(ILI_CLASSCONSTANTJP3, OID1);
		objClassConstantJP3.setattrvalue("aufzaehlung1", "eins");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objClassConstantJP3));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassConstantJ.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn die aufzaehlung1 auf eins gesetzt wird,
	// die Klasse: ClassConstantJP die Klasse: ClassConstant erweitert und der constraint false ist.
	@Test
	public void constantEnumerationSub_Fail(){
		Iom_jObject objClassConstantJP=new Iom_jObject(ILI_CLASSCONSTANTJP, OID1);
		objClassConstantJP.setattrvalue("aufzaehlung1", "eins");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objClassConstantJP));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("Mandatory Constraint MandatoryConstraints23.Topic.ClassConstantJ.Constraint1 is not true.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die boolean nicht uebereinstimmen.	
	@Test
	public void subClassBooleanEqual_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONIP, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONG, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONF, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONF, OID1);
		iomObjA.setattrvalue("attr1", "4.444444");
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONE, OID1);
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
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden Texte uebereinstimmen.
	@Test
	public void textIsEqual_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONA, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONB, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONB, OID1);
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
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSINEQUALATIONC, OID1);
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
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSINEQUALATIONC, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIOND, OID1);
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
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die beiden Booleans nicht ungleich sind.
	@Test
	public void booleanNotUnEqual_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSINEQUALATIONI, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANA, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANA, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANA, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANA, OID1);
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
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSGREATERTHANB, OID1);
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
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSGREATERTHANB, OID1);
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
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn  die attr1 Nummer nicht kleiner als die attr2 Nummer ist.
	@Test
	public void numericNotLessThan_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANA, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANA, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANA, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANA, OID1);
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
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSLESSTHANB, OID1);
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
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSLESSTHANB, OID1);
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
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn attr1 nicht groesser oder gleich attr2 ist.
	@Test
	public void numericNotGreaterThanOrEqual_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALA, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALA, OID1);
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
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSGREATERTHANOREQUALB, OID1);
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
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Nummer aus attr1 nicht kleiner der Nummer aus attr2 ist.
	@Test
	public void numericNotLessThan2_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANOREQUALA, OID1);
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
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSLESSTHANOREQUALA, OID1);
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
		Iom_jObject objValue=new Iom_jObject(ILI_CLASSLESSTHANOREQUALB, OID1);
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
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn der FormattedType aus attr1, dem FormattedType aus attr2 entspricht.
	@Test
	public void formattedTypeUnEqual_Fail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSFORMATTEDTYPEA, OID1);
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
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSDEFINEDB, OID1);
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
	
	// Prueft die Konfiguration: constraint validation.
	// Die Konfiguration ist nicht gesetzt.
	// Es wird eine Fehlermeldung erwartet.
	@Test
	public void valuesNotEqual_ConstraintDisableSet_NotSet_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONI, OID1);
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
	
	// Prueft die Konfiguration: constraint validation.
	// Die Konfiguration ist Eingeschaltet.
	// Es wird eine Fehlermeldung erwartet.
	@Test
	public void valuesNotEqual_ConstraintDisableSet_ON_False(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONI, OID1);
		iomObjA.setattrvalue("attr1", "true");
		iomObjA.setattrvalue("attr2", "false");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.CONSTRAINT_VALIDATION, ValidationConfig.ON);
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
	
	// Prueft die Konfiguration: constraint validation.
	// Die Konfiguration ist Ausgeschaltet.
	// Es wird erwartet dass keine Fehlermeldung ausgegeben wird.
	@Test
	public void valuesNotEqual_ConstraintDisableSet_OFF_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSEQUALATIONI, OID1);
		iomObjA.setattrvalue("attr1", "true");
		iomObjA.setattrvalue("attr2", "false");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER,ValidationConfig.CONSTRAINT_VALIDATION, ValidationConfig.OFF);
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
}