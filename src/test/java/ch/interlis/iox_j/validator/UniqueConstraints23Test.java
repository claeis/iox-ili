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

public class UniqueConstraints23Test {

	private TransferDescription td=null;
	// BID
	private final static String OID1 = "o1";
	private final static String OID2 = "o2";
	private final static String OID3 = "o3";
	private final static String OID4 = "o4";
	private final static String OID5 = "o5";
	private final static String OID6 = "o6";
	// BID
	private final static String BID = "b1";
	// TOPIC
	private final static String TOPIC="UniqueConstraints23.Topic";
	// ASSOCIATION
	private final static String ASSOCA=TOPIC+".assoA";
	private final static String ASSOCB=TOPIC+".assoB";
	private final static String ASSOCC=TOPIC+".assoC";
	// CLASS
	private final static String CLASSA=TOPIC+".ClassA";
	private final static String CLASSB=TOPIC+".ClassB";
	private final static String CLASSBP=TOPIC+".ClassBp";
	private final static String CLASSB0=TOPIC+".ClassB0";
	private final static String CLASSC=TOPIC+".ClassC";
	private final static String CLASSD=TOPIC+".ClassD";
	private final static String CLASSE=TOPIC+".ClassE";
	private final static String CLASSG=TOPIC+".ClassG";
	private final static String CLASSH=TOPIC+".ClassH";
	private final static String CLASSK=TOPIC+".ClassK";
	private final static String CLASSM=TOPIC+".ClassM";
	private final static String CLASSN=TOPIC+".ClassN";
	private final static String CLASSN3=TOPIC+".ClassN3";
	private final static String CLASSO=TOPIC+".ClassO";
	private final static String CLASSO2=TOPIC+".ClassO2";
	private final static String CLASSP=TOPIC+".ClassP";
	private final static String CLASSA1=TOPIC+".ClassA1";
	private final static String CLASSB1=TOPIC+".ClassB1";
	private final static String CLASSC1=TOPIC+".ClassC1";
	private final static String CLASSD1=TOPIC+".ClassD1";
	private final static String CLASSE1=TOPIC+".ClassE1";
	private final static String CLASSF1=TOPIC+".ClassF1";
	private final static String CLASSG1=TOPIC+".ClassG1";
	private final static String CLASSH1=TOPIC+".ClassH1";
	private final static String CLASSI1=TOPIC+".ClassI1";
	// STRUCTURE
	private final static String STRUCTA=TOPIC+".StructA";
	private final static String STRUCTB=TOPIC+".StructB";
	private final static String STRUCTC=TOPIC+".StructC";
	private final static String STRUCTD=TOPIC+".StructD";
	private final static String STRUCTE=TOPIC+".StructE";
	private final static String STRUCTF=TOPIC+".StructF";
	private final static String STRUCTG=TOPIC+".StructG";
	private final static String STRUCTH=TOPIC+".StructH";
	private final static String STRUCTI=TOPIC+".StructI";
	private final static String STRUCTJ=TOPIC+".StructJ";
	private final static String STRUCTK=TOPIC+".StructK";
	private final static String STRUCTO=TOPIC+".StructO";
	private final static String STRUCTP=TOPIC+".StructP";
	private final static String UNDEFINED=TOPIC+".ClassUndefined";
	private final static String EMPTYTEXT=TOPIC+".ClassEmptyText";
	
	@Before
	public void setUp() throws Exception {
		// read ili-file
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/UniqueConstraints23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	//############################################################/
	//########## SUCCESSFUL TESTS ################################/
	//############################################################/

	// Es wird getestet ob ein Fehler ausgegeben wird, wenn kein unique festgelegt wurde. Jedoch die Values von attr1 zwei Mal erstellt wurden.
	@Test
	public void noAttrsAreUnique_Attr1ValuesExistTwice_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSA,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject(CLASSA,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn kein Unique definiert wurde, jedoch das attr2 der beiden Objekte die gleichen Werte enthaelt.
	@Test
	public void noAttrsAreUnique_Attr2ValueExistTwice_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSA,OID1);
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSA,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn kein Unique definiert wurde und weder Attr1, noch Attr2 zweimal erstellt wurden.
	@Test
	public void noAttrsAreUnique_DifferentValues_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSA,OID1);
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject(CLASSA,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn kein Unique definiert wurde, jedoch die Werte Attr1 und Attr2 der beiden Objekte, doppelt erstellt wurde.
	@Test
	public void noAttrsAreUnique__Attr1Attr2ValuesExistTwice_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSA,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSA,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}

	// Es wird getestet ob ein Fehler ausgegeben wird, wenn (Attr1 und Attr2) Unique sind, jedoch Attr2 identische Werte enthaelt.
	@Test
	public void only1AttributeValue_Attr2ExistTwice_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSB,OID1);
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSB,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn (attr1, attr2) Unique definiert sind und beide Werte unterschiedlich definiert sind.
	@Test
	public void attrValuesDifferent_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSB,OID1);
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject(CLASSB,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn (attr1, attr2) Unique definiert sind und beide Werte unterschiedlich definiert sind.
	// Die Klasse: ClassB wird von der Klasse: ClassBP erweitert.
	@Test
	public void subClassAttrValuesDifferent_Ok(){
		// Set object.
		Iom_jObject iomObjClassBP=new Iom_jObject(CLASSBP,OID1);
		iomObjClassBP.setattrvalue("attr1", "Anna");
		iomObjClassBP.setattrvalue("attr2", "15");
		Iom_jObject iomObjClassBP2=new Iom_jObject(CLASSBP,OID2);
		iomObjClassBP2.setattrvalue("attr1", "Ralf");
		iomObjClassBP2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjClassBP));
		validator.validate(new ObjectEvent(iomObjClassBP2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn attr2 Unique definiert ist und die Attributewerte von attr1 identisch sind.
	@Test
	public void nonUniqueAttrValueAttr1ExistTwice_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSC,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject(CLASSC,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn die Nummer Unique definiert ist und nichts identisch ist.
	@Test
	public void uniqueAttrValuesAreDifferent_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSC,OID1);
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject(CLASSC,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn der Text Unique definiert ist und die Nummer identisch ist.
	@Test
	public void nonUniqueAttrValueAttr2ExistTwice_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSB0,OID1);
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSB0,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn der Text Unique definiert ist und nichts identisch ist.
	@Test
	public void uniqueAttrValueAttr1Different_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSB0,OID1);
		obj1.setattrvalue("attr1", "Anna");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject(CLASSB0,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}

	// Es wird getestet ob ein Fehler ausgegeben wird, wenn Text Unique und Nummer Unique separat sind und nicht identisch ist.
	@Test
	public void eachUniqueAttrValuesAreDifferent_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSD,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSD,OID2);
		obj2.setattrvalue("attr1", "Anna");
		obj2.setattrvalue("attr2", "30");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}

	// Es wird getestet ob ein Fehler ausgegeben wird, wenn Text und Nummer Unique sind, die Nummer einzeln Unique ist und die Texte gleich ist.
	@Test
	public void oneOfTwoUniqueAttrValuesAttr1ExistTwice_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSE,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSE,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "15");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
    // Es handelt sich hierbei um einen LOCAL Uniqueness Constraint.
    // In einem Objekt sind zwei Strukturelemente mit unterschiedlichem Wert. 
    // Somit muss eine Fehlermeldung ausgegeben werden.
	@Test
	public void local_oneAttr_oneObj_Unique_Ok(){
		Iom_jObject struct1=new Iom_jObject(STRUCTA, null);
		struct1.setattrvalue("attr1", "2");
		Iom_jObject obj1=new Iom_jObject(CLASSG, OID1);
		obj1.addattrobj("attr2", struct1);
		Iom_jObject struct2=new Iom_jObject(STRUCTA, null);
		struct2.setattrvalue("attr1", "1");
		obj1.addattrobj("attr2", struct2);
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es handelt sich hierbei um einen LOCAL Uniqueness Constraint.
	// In beiden Objekten wird je auf ein attribute einer Struktur verwiesen, in attr1 wurden die Attribute mit den Selben Values erstellt.
	// Es darf keine Fehlermeldung ausgegeben werden, da Unique mit (attr1 und attr2) erstellt wurde.
	@Test
	public void local_twoAttr_Unique_Ok(){
		Iom_jObject struct1=new Iom_jObject(STRUCTA, null);
		struct1.setattrvalue("attr1", "1");
		struct1.setattrvalue("attr2", "5");
		Iom_jObject obj1=new Iom_jObject(CLASSH, OID1);
		obj1.addattrobj("attr2", struct1);
		Iom_jObject struct2=new Iom_jObject(STRUCTA, null);
		struct2.setattrvalue("attr1", "1");
		struct2.setattrvalue("attr2", "2");
		Iom_jObject obj2=new Iom_jObject(CLASSH, OID2);
		obj2.addattrobj("attr2", struct2);
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(struct1));
		validator.validate(new ObjectEvent(struct2));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es handelt sich hierbei um einen LOCAL Uniqueness Constraint.
	// In beiden Objekten wird je auf ein attribute einer Struktur verwiesen, in attr2 wurden die Attribute mit den Selben Values erstellt.
	// Es darf keine Fehlermeldung ausgegeben werden, da Unique mit (attr1 und attr2) erstellt wurde.
	@Test
	public void local_twoAttr_Unique2_Ok(){
		Iom_jObject struct1=new Iom_jObject(STRUCTA, null);
		struct1.setattrvalue("attr1", "6");
		struct1.setattrvalue("attr2", "2");
		Iom_jObject obj1=new Iom_jObject(CLASSH, OID1);
		obj1.addattrobj("attr2", struct1);
		Iom_jObject struct2=new Iom_jObject(STRUCTA, null);
		struct2.setattrvalue("attr1", "1");
		struct2.setattrvalue("attr2", "2");
		Iom_jObject obj2=new Iom_jObject(CLASSH, OID2);
		obj2.addattrobj("attr2", struct2);
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(struct1));
		validator.validate(new ObjectEvent(struct2));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
		
	// In beiden Objekten wird je ueber eine Structure auf ein attribute einer SURFACE verwiesen, Beide Attribute wurden mit unterschiedlichen Values erstellt.
	// Somit darf keine Fehlermeldung ausgegeben werden.
	@Test
	public void local_oneSurfaceAttr_Unique_Ok(){
		// Set object.
		Iom_jObject struct1=new Iom_jObject(STRUCTO, null);
		{
			IomObject multisurfaceValue=struct1.addattrobj("surface2d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "480000.000");
			startSegment.setattrvalue("C2", "70000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "500000.000");
			endSegment.setattrvalue("C2", "80000.000");
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2=segments2.addattrobj("segment", "COORD");
			startSegment2.setattrvalue("C1", "500000.000");
			startSegment2.setattrvalue("C2", "80000.000");
			IomObject endSegment2=segments2.addattrobj("segment", "COORD");
			endSegment2.setattrvalue("C1", "550000.000");
			endSegment2.setattrvalue("C2", "90000.000");
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3=segments3.addattrobj("segment", "COORD");
			startSegment3.setattrvalue("C1", "550000.000");
			startSegment3.setattrvalue("C2", "90000.000");
			IomObject endSegment3=segments3.addattrobj("segment", "COORD");
			endSegment3.setattrvalue("C1", "480000.000");
			endSegment3.setattrvalue("C2", "70000.000");
		}
		Iom_jObject struct2=new Iom_jObject(STRUCTO, null);
		{
			IomObject multisurfaceValue=struct2.addattrobj("surface2d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "500000.000");
			startSegment.setattrvalue("C2", "70000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "520000.000");
			endSegment.setattrvalue("C2", "80000.000");
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2=segments2.addattrobj("segment", "COORD");
			startSegment2.setattrvalue("C1", "520000.000");
			startSegment2.setattrvalue("C2", "80000.000");
			IomObject endSegment2=segments2.addattrobj("segment", "COORD");
			endSegment2.setattrvalue("C1", "550000.000");
			endSegment2.setattrvalue("C2", "90000.000");
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3=segments3.addattrobj("segment", "COORD");
			startSegment3.setattrvalue("C1", "550000.000");
			startSegment3.setattrvalue("C2", "90000.000");
			IomObject endSegment3=segments3.addattrobj("segment", "COORD");
			endSegment3.setattrvalue("C1", "500000.000");
			endSegment3.setattrvalue("C2", "70000.000");
		}
		Iom_jObject obj1=new Iom_jObject(CLASSO2,OID1);
		obj1.addattrobj("attro2", struct1);
		Iom_jObject obj2=new Iom_jObject(CLASSO2,OID2);
		obj2.addattrobj("attro2", struct2);
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
		
	// In beiden Objekten einer Association werden auf die Attribute: attr1 verwiesen, Beide Attribute wurden mit unterschiedlichen Values erstellt.
	// Somit darf keine Fehlermeldung ausgegeben werden.
	@Test
	public void uniqueAttrValueAttr1_InStandAloneAssociationIsDifferent_Ok(){
		Iom_jObject iomObjE=new Iom_jObject(CLASSA1,OID1);
		Iom_jObject iomObjF=new Iom_jObject(CLASSB1,OID2);
		Iom_jObject iomLinkEF=new Iom_jObject(ASSOCA, OID3);
		iomLinkEF.addattrobj("a1", "REF").setobjectrefoid(OID1);
		iomLinkEF.addattrobj("b1", "REF").setobjectrefoid(OID2);
		iomLinkEF.setattrvalue("attr1", "text");
		Iom_jObject iomLinkEF2=new Iom_jObject(ASSOCA, null);
		iomLinkEF2.addattrobj("a1", "REF").setobjectrefoid(OID1);
		iomLinkEF2.addattrobj("b1", "REF").setobjectrefoid(OID2);
		iomLinkEF2.setattrvalue("attr1", "otherText");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new ObjectEvent(iomObjF));
		validator.validate(new ObjectEvent(iomLinkEF));
		validator.validate(new ObjectEvent(iomLinkEF2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}	
	
	// In beiden Objekten einer Association werden auf die Rolle: c1 verwiesen. Es wird nur von 1 Objekt auf die Rolle c1 verweisen.
	// Somit darf keine Fehlermeldung ausgegeben werden, da c1 nicht ueber 2 Mal angesprochen wird.
	@Test
	public void uniqueAttrValuesOfRoleC1_InStandAloneAssociationAreDifferent_Ok(){
		Iom_jObject iomObjE=new Iom_jObject(CLASSC1,OID1);
		Iom_jObject iomObjF=new Iom_jObject(CLASSD1,OID2);
		Iom_jObject iomObjG=new Iom_jObject(CLASSC1,OID3);
		Iom_jObject iomObjH=new Iom_jObject(CLASSD1,OID4);
		Iom_jObject iomLinkEF=new Iom_jObject(ASSOCB, null);
		iomLinkEF.addattrobj("c1", "REF").setobjectrefoid(OID1);
		iomLinkEF.addattrobj("d1", "REF").setobjectrefoid(OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new ObjectEvent(iomObjF));
		validator.validate(new ObjectEvent(iomObjG));
		validator.validate(new ObjectEvent(iomObjH));
		validator.validate(new ObjectEvent(iomLinkEF));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}	
	
	// Die Objekte referenzieren auf die Rolle: c1. Es wird nur von 1 Objekt auf die Rolle c1 verweisen.
	// Somit darf keine Fehlermeldung ausgegeben werden, da c1 nicht ueber 2 Mal angesprochen wird.
	@Test
	public void uniqueAttrValuesOfRoleC1_InEmbeddedAssociationIsDifferent_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSC1,OID1);
		Iom_jObject iomObjB=new Iom_jObject(CLASSD1,OID2);
		iomObjB.addattrobj("c1", "REF").setobjectrefoid(OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn eine Konstante in  einem UniquenessConstraint undefiniert (gar nicht erstellt) ist.
	// attr1 ist UNIQUE und noch einmal UNIQUE mit attr2 zusammen. Jedoch wird attr1 nicht erstellt.
	// Es soll keine Fehlermeldung ausgegeben werden.
	@Test
	public void nonUniqueAttrValuesOfAttr1_AreUndefined_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(UNDEFINED, OID1);
		iomObjA.setattrvalue("attr2", "20");
		Iom_jObject iomObjB=new Iom_jObject(UNDEFINED, OID2);
		iomObjB.setattrvalue("attr2", "15");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn eine Konstante in  einem UniquenessConstraint undefiniert (gar nicht erstellt) ist.
	// attr1 ist UNIQUE und noch einmal UNIQUE mit attr2 zusammen. Jedoch wird attr1 nicht erstellt.
	// Es soll keine Fehlermeldung fuer die Unique Verletzung von attr2 ausgegeben werden.
	@Test
	public void nonUniqueAttr1IsUndefined_UniqueAttrValueOfAttr2ExistTwice_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(UNDEFINED, OID1);
		iomObjA.setattrvalue("attr2", "20");
		Iom_jObject iomObjB=new Iom_jObject(UNDEFINED, OID2);
		iomObjB.setattrvalue("attr2", "20");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn eine Konstante in einem UniquenessConstraint leeren Text ist.
	// Es soll als normaler Inhalt angeschaut werden und keine Fehlermeldung ausgeben, da attr2 unterschiedliche Werte definiert hat.
	@Test
	public void attrValuesOfAttr1IsEmpty_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(EMPTYTEXT, OID1);
		iomObjA.setattrvalue("attr1", "");
		iomObjA.setattrvalue("attr2", "text1");
		Iom_jObject iomObjB=new Iom_jObject(EMPTYTEXT, OID2);
		iomObjB.setattrvalue("attr1", "");
		iomObjB.setattrvalue("attr2", "text2");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es handelt sich hierbei um einen LOCAL Uniqueness Constraint.
	// In beiden Objekten wird je auf ein attribute einer unterschiedlichen Struktur verwiesen, Beide Attribute wurden mit unterschiedlichen Values erstellt.
	// Somit darf keine Fehlermeldung ausgegeben werden.
	@Test
	public void local_subStruct_oneAttr_Unique_Ok(){
		// object 1
		Iom_jObject struct3=new Iom_jObject(STRUCTJ, null);
		struct3.setattrvalue("attr1j", "8");
		Iom_jObject struct2=new Iom_jObject(STRUCTI, null);
		struct2.addattrobj("attr1i", struct3);
		Iom_jObject struct1=new Iom_jObject(STRUCTH, null);
		struct1.addattrobj("attr1h", struct2);
		Iom_jObject obj1=new Iom_jObject(CLASSM, OID1);
		obj1.addattrobj("attr1", struct1);
		// object 2
		Iom_jObject struct30=new Iom_jObject(STRUCTJ, null);
		struct30.setattrvalue("attr1j", "5");
		Iom_jObject struct20=new Iom_jObject(STRUCTI, null);
		struct20.addattrobj("attr1i", struct30);
		Iom_jObject struct10=new Iom_jObject(STRUCTH, null);
		struct10.addattrobj("attr1h", struct20);
		Iom_jObject obj2=new Iom_jObject(CLASSM, OID2);
		obj2.addattrobj("attr1", struct10);
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(struct1));
		validator.validate(new ObjectEvent(struct2));
		validator.validate(new ObjectEvent(struct3));
		validator.validate(new ObjectEvent(struct10));
		validator.validate(new ObjectEvent(struct20));
		validator.validate(new ObjectEvent(struct30));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// In beiden Objekten wird je auf ein attribute einer SURFACE verwiesen, Beide Attribute wurden mit unterschiedlichen Values erstellt.
	// Somit darf keine Fehlermeldung ausgegeben werden.
	@Test
	public void uniqueAttrValuesOfSurfaceAreDifferent_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSO,OID1);
		{
			IomObject multisurfaceValue=obj1.addattrobj("surface2d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "480000.000");
			startSegment.setattrvalue("C2", "70000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "500000.000");
			endSegment.setattrvalue("C2", "80000.000");
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2=segments2.addattrobj("segment", "COORD");
			startSegment2.setattrvalue("C1", "500000.000");
			startSegment2.setattrvalue("C2", "80000.000");
			IomObject endSegment2=segments2.addattrobj("segment", "COORD");
			endSegment2.setattrvalue("C1", "550000.000");
			endSegment2.setattrvalue("C2", "90000.000");
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3=segments3.addattrobj("segment", "COORD");
			startSegment3.setattrvalue("C1", "550000.000");
			startSegment3.setattrvalue("C2", "90000.000");
			IomObject endSegment3=segments3.addattrobj("segment", "COORD");
			endSegment3.setattrvalue("C1", "480000.000");
			endSegment3.setattrvalue("C2", "70000.000");
		}
		Iom_jObject obj2=new Iom_jObject(CLASSO,OID2);
		{
			IomObject multisurfaceValue=obj2.addattrobj("surface2d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "500000.000");
			startSegment.setattrvalue("C2", "70000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "520000.000");
			endSegment.setattrvalue("C2", "80000.000");
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2=segments2.addattrobj("segment", "COORD");
			startSegment2.setattrvalue("C1", "520000.000");
			startSegment2.setattrvalue("C2", "80000.000");
			IomObject endSegment2=segments2.addattrobj("segment", "COORD");
			endSegment2.setattrvalue("C1", "550000.000");
			endSegment2.setattrvalue("C2", "90000.000");
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3=segments3.addattrobj("segment", "COORD");
			startSegment3.setattrvalue("C1", "550000.000");
			startSegment3.setattrvalue("C2", "90000.000");
			IomObject endSegment3=segments3.addattrobj("segment", "COORD");
			endSegment3.setattrvalue("C1", "500000.000");
			endSegment3.setattrvalue("C2", "70000.000");
		}
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// In beiden Objekten wird je auf ein attribute einer AREA verwiesen, Beide Attribute wurden mit unterschiedlichen Values erstellt.
	// Somit darf keine Fehlermeldung ausgegeben werden.
	@Test
	public void uniqueAttrValuesOfAreaAreDifferent_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSP,OID1);
		{
			IomObject multisurfaceValue=obj1.addattrobj("area2d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "480000.000");
			startSegment.setattrvalue("C2", "70000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "500000.000");
			endSegment.setattrvalue("C2", "80000.000");
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2=segments2.addattrobj("segment", "COORD");
			startSegment2.setattrvalue("C1", "500000.000");
			startSegment2.setattrvalue("C2", "80000.000");
			IomObject endSegment2=segments2.addattrobj("segment", "COORD");
			endSegment2.setattrvalue("C1", "550000.000");
			endSegment2.setattrvalue("C2", "90000.000");
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3=segments3.addattrobj("segment", "COORD");
			startSegment3.setattrvalue("C1", "550000.000");
			startSegment3.setattrvalue("C2", "90000.000");
			IomObject endSegment3=segments3.addattrobj("segment", "COORD");
			endSegment3.setattrvalue("C1", "480000.000");
			endSegment3.setattrvalue("C2", "70000.000");
		}
		Iom_jObject obj2=new Iom_jObject(CLASSP,OID2);
		{
			IomObject multisurfaceValue=obj2.addattrobj("area2d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "500000.000");
			startSegment.setattrvalue("C2", "70000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "520000.000");
			endSegment.setattrvalue("C2", "80000.000");
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2=segments2.addattrobj("segment", "COORD");
			startSegment2.setattrvalue("C1", "520000.000");
			startSegment2.setattrvalue("C2", "80000.000");
			IomObject endSegment2=segments2.addattrobj("segment", "COORD");
			endSegment2.setattrvalue("C1", "550000.000");
			endSegment2.setattrvalue("C2", "90000.000");
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3=segments3.addattrobj("segment", "COORD");
			startSegment3.setattrvalue("C1", "550000.000");
			startSegment3.setattrvalue("C2", "90000.000");
			IomObject endSegment3=segments3.addattrobj("segment", "COORD");
			endSegment3.setattrvalue("C1", "500000.000");
			endSegment3.setattrvalue("C2", "70000.000");
		}
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn bei der Funktion: inEnumRange das ZielAttribute zwischen min und max enum in der Enumeration sich befindet.
	// und die Attribute unterschiedliche Values enthalten, jedoch PreCondition False ist.
	@Test
	public void preConditionFunctionIsTrue_UniqueAttrValuesAreDifferent_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSN3, OID1);
		iomObjA.setattrvalue("attr01", "zwei");
		iomObjA.setattrvalue("attr02", "eins");
		iomObjA.setattrvalue("attr03", "vier");
		iomObjA.setattrvalue("attr04", "gleich");
		Iom_jObject iomObjB=new Iom_jObject(CLASSN3, OID2);
		iomObjB.setattrvalue("attr01", "zwei");
		iomObjB.setattrvalue("attr02", "eins");
		iomObjB.setattrvalue("attr03", "vier");
		iomObjB.setattrvalue("attr04", "ungleich");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn bei der Funktion: inEnumRange das ZielAttribute nicht zwischen min und max enum in der Enumeration sich befindet.
	// und die Attribute die Selben Values enthalten, jedoch preCondition False ist.
	@Test
	public void preConditionIsFalse_UniqueAttrValuesExistTwice_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSN3, OID1);
		iomObjA.setattrvalue("attr01", "eins");
		iomObjA.setattrvalue("attr02", "zwei");
		iomObjA.setattrvalue("attr03", "vier");
		iomObjA.setattrvalue("attr04", "gleich");
		Iom_jObject iomObjB=new Iom_jObject(CLASSN3, OID2);
		iomObjB.setattrvalue("attr01", "eins");
		iomObjB.setattrvalue("attr02", "zwei");
		iomObjB.setattrvalue("attr03", "vier");
		iomObjB.setattrvalue("attr04", "gleich");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn bei der Funktion: inEnumRange das ZielAttribute nicht zwischen min und max enum in der Enumeration sich befindet.
	// und die Attribute unterschiedliche Values enthalten, jedoch PreCondition False ist.
	@Test
	public void preConditionIsFalse_UniqueAttrValuesAreDifferent_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSN3, OID1);
		iomObjA.setattrvalue("attr01", "eins");
		iomObjA.setattrvalue("attr02", "zwei");
		iomObjA.setattrvalue("attr03", "vier");
		iomObjA.setattrvalue("attr04", "gleich");
		Iom_jObject iomObjB=new Iom_jObject(CLASSN3, OID2);
		iomObjB.setattrvalue("attr01", "eins");
		iomObjB.setattrvalue("attr02", "zwei");
		iomObjB.setattrvalue("attr03", "vier");
		iomObjB.setattrvalue("attr04", "ungleich");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird,
	// wenn die PreCondition true ist und die Attribute welche unique sind, mit unterschiedlichen Values erstellt wurden.
	@Test
	public void preConditionIsTrue_UniqueAttrValuesAreDifferent_Ok(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSN,OID1);
		obj1.setattrvalue("attrw1", "5");
		obj1.setattrvalue("attrw2", "3");
		obj1.setattrvalue("attrw3", "2");
		Iom_jObject obj2=new Iom_jObject(CLASSN,OID2);
		obj2.setattrvalue("attrw1", "9");
		obj2.setattrvalue("attrw2", "6");
		obj2.setattrvalue("attrw3", "1");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==0);
	}
	
	//############################################################/
	//########## FAILING TESTS ###################################/
	//############################################################/

	// Es wird getestet ob ein Fehler ausgegeben wird, wenn (attr1, attr2) Unique definiert sind und beide Werte gleich sind.
	// Die Klasse: ClassB wird von der Klasse: ClassBP erweitert.
	@Test
	public void subClassAttrValuesDifferent_Fail(){
		// Set object.
		Iom_jObject iomObjClassBP=new Iom_jObject(CLASSBP,OID1);
		iomObjClassBP.setattrvalue("attr1", "Anna");
		iomObjClassBP.setattrvalue("attr2", "20");
		Iom_jObject iomObjClassBP2=new Iom_jObject(CLASSBP,OID2);
		iomObjClassBP2.setattrvalue("attr1", "Anna");
		iomObjClassBP2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjClassBP));
		validator.validate(new ObjectEvent(iomObjClassBP2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values Anna, 20 already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn die Nummer Unique und identisch ist.
	@Test
	public void uniqueAttrValuesOfAttr2ExistTwice_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSB,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject objA=new Iom_jObject(CLASSB,OID2);
		objA.setattrvalue("attr1", "Ralf");
		objA.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(objA));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values Ralf, 20 already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn der Text Unique und identisch ist.
	@Test
	public void uniqueAttrValuesOfAttr1ExistTwice_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSB0,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "15");
		Iom_jObject obj2=new Iom_jObject(CLASSB0,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values Ralf already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}

	// Es wird getestet ob ein Fehler ausgegeben wird, wenn Number Unique ist und beide Values identisch sind.
	@Test
	public void uniqueAttr1AndUniqueAttr2ExistTwice_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSC,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSC,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values 20 already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}

	// Es wird getestet ob ein Fehler ausgegeben wird, wenn Text Unique und Nummber Unique ist. Dabei die Nummer identisch ist.
	@Test
	public void uniqueAttr1AndAttr2ExistTwice_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSD,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSD,OID2);
		obj2.setattrvalue("attr1", "Anna");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values 20 already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn der Text Unique und die Nummer Unique ist. Dabei ist der Text identisch und die Nummer verschieden.
	@Test
	public void uniqueAttr1ExistsTwice_uniqueAttr2Different_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSD,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSD,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "10");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values Ralf already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn der Text Unique ist und die Nummer Unique ist. Dabei beide Values identisch sind.
	@Test
	public void uniqueAttrValuesAttr1AndAttr2ExistTwice_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSD,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSD,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==2);
		assertEquals("Unique is violated! Values Ralf already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
		assertEquals("Unique is violated! Values 20 already exist in Object: o1", logger.getErrs().get(1).getEventMsg());
	}
	
	// Es wird getestet ob ein Fehler ausgegeben wird, wenn der Text und die Nummer Unique sind und die Nummer Unique ist. Dabei sind die Values der Nummer identisch.
	@Test
	public void uniqueAttrValuesAttr1AndAttr2ExistTwice_UniqueAttr2ExistTwice_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSE,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSE,OID2);
		obj2.setattrvalue("attr1", "Anna");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values 20 already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}

	// Es wird getestet ob ein Fehler ausgegeben wird, wenn der Text Unique ist und die Nummer Unique ist. Dabei beide Values identisch sind.
	@Test
	public void uniqueAttr1ExistTwice_uniqueAttr2ExistTwice_Fail(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSD,OID1);
		obj1.setattrvalue("attr1", "Ralf");
		obj1.setattrvalue("attr2", "20");
		Iom_jObject obj2=new Iom_jObject(CLASSD,OID2);
		obj2.setattrvalue("attr1", "Ralf");
		obj2.setattrvalue("attr2", "20");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==2);
		assertEquals("Unique is violated! Values Ralf already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
		assertEquals("Unique is violated! Values 20 already exist in Object: o1", logger.getErrs().get(1).getEventMsg());
	}
	
	// Es handelt sich hierbei um einen LOCAL Uniqueness Constraint.
	// In einem Objekt sind zwei Strukturelemente mit dem selben Wert. 
	// Somit muss eine Fehlermeldung ausgegeben werden.
	@Test
	public void local_oneAttr_oneObj_Duplicate_False(){
		Iom_jObject obj1=new Iom_jObject(CLASSG, OID1);
		{
	        Iom_jObject struct1=new Iom_jObject(STRUCTA, null);
	        struct1.setattrvalue("attr1", "1");
	        obj1.addattrobj("attr2", struct1);
		}
		{
	        Iom_jObject struct2=new Iom_jObject(STRUCTA, null);
	        struct2.setattrvalue("attr1", "1");
	        obj1.addattrobj("attr2", struct2);
		}
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertEquals(1,logger.getErrs().size());
		assertEquals("Unique is violated! Values 1 already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
    // Es handelt sich hierbei um einen LOCAL Uniqueness Constraint.
    // In beiden Objekten ist ein Strukturattribut mit dem selben Wert (was bei einem LOCAL Unique zulaessig ist).
    @Test
    public void local_oneAttr_twoObj_Duplicate_Ok(){
        Iom_jObject struct1=new Iom_jObject(STRUCTA, null);
        struct1.setattrvalue("attr1", "1");
        Iom_jObject obj1=new Iom_jObject(CLASSG, OID1);
        obj1.addattrobj("attr2", struct1);
        Iom_jObject struct2=new Iom_jObject(STRUCTA, null);
        struct2.setattrvalue("attr1", "1");
        Iom_jObject obj2=new Iom_jObject(CLASSG, OID2);
        obj2.addattrobj("attr2", struct2);
        // Create and run validator.
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC,BID));
        validator.validate(new ObjectEvent(obj1));
        validator.validate(new ObjectEvent(obj2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts.
        assertEquals(0,logger.getErrs().size());
    }
	
	// Es handelt sich hierbei um einen LOCAL Uniqueness Constraint.
	// In beiden Objekten wird je auf ein attribute einer Struktur verwiesen, welche die selben 2 Values der Attribute beinhalten.
	// Somit muss eine Fehlermeldung ausgegeben werden, welche besagt, dass beide Attribute-Values, bereits im vorherigen Objekt erstellt wurden.
	@Test
	public void local_twoAttr_Duplicate_False(){
		Iom_jObject struct1=new Iom_jObject(STRUCTA, null);
		struct1.setattrvalue("attr1", "1");
		struct1.setattrvalue("attr2", "2");
		Iom_jObject obj1=new Iom_jObject(CLASSH, OID1);
		obj1.addattrobj("attr2", struct1);
		Iom_jObject struct2=new Iom_jObject(STRUCTA, null);
		struct2.setattrvalue("attr1", "1");
		struct2.setattrvalue("attr2", "2");
		obj1.addattrobj("attr2", struct2);
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(struct1));
		validator.validate(new ObjectEvent(struct2));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertEquals(1,logger.getErrs().size());
		assertEquals("Unique is violated! Values 1, 2 already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
		
	// Es handelt sich hierbei um einen LOCAL Uniqueness Constraint und einen GLOBAL Uniqueness Constraint.
	// In beiden Objekten ist ein Strukturelement mit den selben Werten, was fuer ein lokal Unique ok ist.
    // In beiden Objekten ist bei einem globalen Unique der selbe Wert, was falsch ist.
	@Test
	public void local_multipleConstraints_oneAttr_oneObj_Duplicate_False(){
		Iom_jObject struct2=new Iom_jObject(STRUCTE, null);
		struct2.setattrvalue("attr1", "1");
		struct2.setattrvalue("attr2", "2");
		Iom_jObject struct1=new Iom_jObject(STRUCTE, null);
		struct1.setattrvalue("attr1", "1");
		struct1.setattrvalue("attr2", "2");
		Iom_jObject obj1=new Iom_jObject(CLASSK, OID1);
		obj1.addattrobj("attr1", struct1);
        obj1.addattrobj("attr1", struct2);
		obj1.setattrvalue("attr2", "8");
		Iom_jObject obj2=new Iom_jObject(CLASSK, OID2);
		obj2.setattrvalue("attr2", "8");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertEquals(2,logger.getErrs().size());
        assertEquals("Unique is violated! Values 1 already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
		assertEquals("Unique is violated! Values 8 already exist in Object: o1", logger.getErrs().get(1).getEventMsg());
	}
	
	// Es handelt sich hierbei um einen LOCAL Uniqueness Constraint und einen GLOBAL Uniqueness Constraint.
	// In beiden Objekten sind im Strukturelement die selben Werte, was bei einem lokalen Unique ok ist.
	@Test
	public void local_multipleConstraints_oneAttr_twoObj_Duplicate_Ok(){
        Iom_jObject obj1=new Iom_jObject(CLASSK, OID1);
        obj1.setattrvalue("attr2", "8");
	    {
	        Iom_jObject struct1=new Iom_jObject(STRUCTE, null);
	        struct1.setattrvalue("attr1", "1");
	        struct1.setattrvalue("attr2", "2");
	        obj1.addattrobj("attr1", struct1);
	    }
		Iom_jObject obj2=new Iom_jObject(CLASSK, OID2);
		obj2.setattrvalue("attr2", "6");
        {
            Iom_jObject struct2=new Iom_jObject(STRUCTE, null);
            struct2.setattrvalue("attr1", "1");
            struct2.setattrvalue("attr2", "2");
            obj2.addattrobj("attr1", struct2);
        }
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertEquals(0,logger.getErrs().size());
	}
	
	// In beiden Objekten wird je auf ein attribute einer AREA verwiesen, Beide Attribute wurden mit den Selben Values erstellt.
	// Somit muss eine Fehlermeldung ausgegeben werden.
	@Test
	public void UniqueAttrValuesOfAreaExistsTwice_False(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSP,OID1);
		{
			IomObject multisurfaceValue=obj1.addattrobj("area2d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "480000.000");
			startSegment.setattrvalue("C2", "70000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "500000.000");
			endSegment.setattrvalue("C2", "80000.000");
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2=segments2.addattrobj("segment", "COORD");
			startSegment2.setattrvalue("C1", "500000.000");
			startSegment2.setattrvalue("C2", "80000.000");
			IomObject endSegment2=segments2.addattrobj("segment", "COORD");
			endSegment2.setattrvalue("C1", "550000.000");
			endSegment2.setattrvalue("C2", "90000.000");
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3=segments3.addattrobj("segment", "COORD");
			startSegment3.setattrvalue("C1", "550000.000");
			startSegment3.setattrvalue("C2", "90000.000");
			IomObject endSegment3=segments3.addattrobj("segment", "COORD");
			endSegment3.setattrvalue("C1", "480000.000");
			endSegment3.setattrvalue("C2", "70000.000");
		}
		Iom_jObject obj2=new Iom_jObject(CLASSP,OID2);
		{
			IomObject multisurfaceValue=obj2.addattrobj("area2d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "480000.000");
			startSegment.setattrvalue("C2", "70000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "500000.000");
			endSegment.setattrvalue("C2", "80000.000");
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2=segments2.addattrobj("segment", "COORD");
			startSegment2.setattrvalue("C1", "500000.000");
			startSegment2.setattrvalue("C2", "80000.000");
			IomObject endSegment2=segments2.addattrobj("segment", "COORD");
			endSegment2.setattrvalue("C1", "550000.000");
			endSegment2.setattrvalue("C2", "90000.000");
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3=segments3.addattrobj("segment", "COORD");
			startSegment3.setattrvalue("C1", "550000.000");
			startSegment3.setattrvalue("C2", "90000.000");
			IomObject endSegment3=segments3.addattrobj("segment", "COORD");
			endSegment3.setattrvalue("C1", "480000.000");
			endSegment3.setattrvalue("C2", "70000.000");
		}
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.000, C2 70000.000}, COORD {C1 500000.000, C2 80000.000}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 500000.000, C2 80000.000}, COORD {C1 550000.000, C2 90000.000}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 550000.000, C2 90000.000}, COORD {C1 480000.000, C2 70000.000}]}}]}}} already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es handelt sich hierbei um einen LOCAL Uniqueness Constraint.
	// In beiden Objekten wird je auf ein attribute einer unterschiedlichen Struktur verwiesen, Beide Attribute wurden mit den Selben Values erstellt.
	// Somit muss eine Fehlermeldung ausgegeben werden. Da attr1j als Unique definiert wurde.
	@Test
	public void local_subStruct_oneAttr_Duplicate_False(){
		// object 1
        Iom_jObject obj1=new Iom_jObject(CLASSM, OID1);
        {
            Iom_jObject struct3=new Iom_jObject(STRUCTJ, null);
            struct3.setattrvalue("attr1j", "5");
            Iom_jObject struct2=new Iom_jObject(STRUCTI, null);
            struct2.addattrobj("attr1i", struct3);
            Iom_jObject struct1=new Iom_jObject(STRUCTH, null);
            struct1.addattrobj("attr1h", struct2);
            obj1.addattrobj("attr1", struct1);
        }
        {
            Iom_jObject struct30=new Iom_jObject(STRUCTJ, null);
            struct30.setattrvalue("attr1j", "5");
            Iom_jObject struct20=new Iom_jObject(STRUCTI, null);
            struct20.addattrobj("attr1i", struct30);
            Iom_jObject struct10=new Iom_jObject(STRUCTH, null);
            struct10.addattrobj("attr1h", struct20);
            obj1.addattrobj("attr1", struct10);
        }
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertEquals(1,logger.getErrs().size());
		assertEquals("Unique is violated! Values 5 already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// In beiden Objekten einer Association werden auf die Rolle: c1 verwiesen. Beide Objekte verweisen 2 Mal ueber die Rolle c1 auf die Klasse C.
	// Somit muss eine Fehlermeldung ausgegeben werden, da c1 nur maximal 1 Mal angesprochen werden darf. Da diese Unique ist.
	//@Test
	public void uniqueAttrValuesOfRoleC1_InStandAloneAssociation_ExistTwice_False(){
		Iom_jObject iomObjE=new Iom_jObject(CLASSC1,OID1);
		Iom_jObject iomObjF=new Iom_jObject(CLASSD1,OID2);
		Iom_jObject iomObjH=new Iom_jObject(CLASSD1,OID4);
		iomObjF.addattrobj("c1", "REF").setobjectrefoid(OID1);
		iomObjH.addattrobj("c1", "REF").setobjectrefoid(OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new ObjectEvent(iomObjF));
		validator.validate(new ObjectEvent(iomObjH));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values REF {} already exist in Object: UniqueConstraints23.Topic.assoB", logger.getErrs().get(0).getEventMsg());
	}
		
	// In beiden Objekten einer Association werden auf die Attribute: attr1 verwiesen, Beide Attribute wurden mit den Selben Values: text erstellt.
	// Somit muss eine Fehlermeldung ausgegeben werden.
	@Test
	public void uniqueAttrValuesAttr1_InStandAloneAssociationExistsTwice_False(){
		Iom_jObject iomObjE=new Iom_jObject(CLASSA1,OID1);
		Iom_jObject iomObjF=new Iom_jObject(CLASSB1,OID2);
		Iom_jObject iomLinkEF=new Iom_jObject(ASSOCA, OID3);
		iomLinkEF.addattrobj("a1", "REF").setobjectrefoid(OID1);
		iomLinkEF.addattrobj("b1", "REF").setobjectrefoid(OID2);
		iomLinkEF.setattrvalue("attr1", "text");
		Iom_jObject iomLinkEF2=new Iom_jObject(ASSOCA, null);
		iomLinkEF2.addattrobj("a1", "REF").setobjectrefoid(OID1);
		iomLinkEF2.addattrobj("b1", "REF").setobjectrefoid(OID2);
		iomLinkEF2.setattrvalue("attr1", "text");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new ObjectEvent(iomObjF));
		validator.validate(new ObjectEvent(iomLinkEF));
		validator.validate(new ObjectEvent(iomLinkEF2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values text already exist in Object: o3", logger.getErrs().get(0).getEventMsg());
	}
	
	// In beiden Objekten wird je ueber eine Structure auf ein attribute einer SURFACE verwiesen, Beide Attribute wurden mit den Selben Values erstellt.
	// Somit muss eine Fehlermeldung ausgegeben werden.
	@Test
	public void local_oneSurfaceAttr_Duplicate_False(){
		// Set object.
		Iom_jObject struct1=new Iom_jObject(STRUCTO, null);
		{
			IomObject multisurfaceValue=struct1.addattrobj("surface2d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "480000.000");
			startSegment.setattrvalue("C2", "70000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "500000.000");
			endSegment.setattrvalue("C2", "80000.000");
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2=segments2.addattrobj("segment", "COORD");
			startSegment2.setattrvalue("C1", "500000.000");
			startSegment2.setattrvalue("C2", "80000.000");
			IomObject endSegment2=segments2.addattrobj("segment", "COORD");
			endSegment2.setattrvalue("C1", "550000.000");
			endSegment2.setattrvalue("C2", "90000.000");
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3=segments3.addattrobj("segment", "COORD");
			startSegment3.setattrvalue("C1", "550000.000");
			startSegment3.setattrvalue("C2", "90000.000");
			IomObject endSegment3=segments3.addattrobj("segment", "COORD");
			endSegment3.setattrvalue("C1", "480000.000");
			endSegment3.setattrvalue("C2", "70000.000");
		}
		Iom_jObject struct2=new Iom_jObject(STRUCTO, null);
		{
			IomObject multisurfaceValue=struct2.addattrobj("surface2d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "480000.000");
			startSegment.setattrvalue("C2", "70000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "500000.000");
			endSegment.setattrvalue("C2", "80000.000");
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2=segments2.addattrobj("segment", "COORD");
			startSegment2.setattrvalue("C1", "500000.000");
			startSegment2.setattrvalue("C2", "80000.000");
			IomObject endSegment2=segments2.addattrobj("segment", "COORD");
			endSegment2.setattrvalue("C1", "550000.000");
			endSegment2.setattrvalue("C2", "90000.000");
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3=segments3.addattrobj("segment", "COORD");
			startSegment3.setattrvalue("C1", "550000.000");
			startSegment3.setattrvalue("C2", "90000.000");
			IomObject endSegment3=segments3.addattrobj("segment", "COORD");
			endSegment3.setattrvalue("C1", "480000.000");
			endSegment3.setattrvalue("C2", "70000.000");
		}
		Iom_jObject obj1=new Iom_jObject(CLASSO2,OID1);
		obj1.addattrobj("attro2", struct1);
		obj1.addattrobj("attro2", struct2);
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertEquals(1,logger.getErrs().size());
		assertEquals("Unique is violated! Values MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.000, C2 70000.000}, COORD {C1 500000.000, C2 80000.000}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 500000.000, C2 80000.000}, COORD {C1 550000.000, C2 90000.000}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 550000.000, C2 90000.000}, COORD {C1 480000.000, C2 70000.000}]}}]}}} already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// In beiden Objekten wird je auf ein attribute einer SURFACE verwiesen, Beide Attribute wurden mit den Selben Values erstellt.
	// Somit muss eine Fehlermeldung ausgegeben werden.
	@Test
	public void uniqueAttrValuesOfSurfaceExistTwice_False(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSO,OID1);
		{
			IomObject multisurfaceValue=obj1.addattrobj("surface2d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "480000.000");
			startSegment.setattrvalue("C2", "70000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "500000.000");
			endSegment.setattrvalue("C2", "80000.000");
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2=segments2.addattrobj("segment", "COORD");
			startSegment2.setattrvalue("C1", "500000.000");
			startSegment2.setattrvalue("C2", "80000.000");
			IomObject endSegment2=segments2.addattrobj("segment", "COORD");
			endSegment2.setattrvalue("C1", "550000.000");
			endSegment2.setattrvalue("C2", "90000.000");
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3=segments3.addattrobj("segment", "COORD");
			startSegment3.setattrvalue("C1", "550000.000");
			startSegment3.setattrvalue("C2", "90000.000");
			IomObject endSegment3=segments3.addattrobj("segment", "COORD");
			endSegment3.setattrvalue("C1", "480000.000");
			endSegment3.setattrvalue("C2", "70000.000");
		}
		Iom_jObject obj2=new Iom_jObject(CLASSO,OID2);
		{
			IomObject multisurfaceValue=obj2.addattrobj("surface2d", "MULTISURFACE");
			IomObject surfaceValue = multisurfaceValue.addattrobj("surface", "SURFACE");
			IomObject outerBoundary = surfaceValue.addattrobj("boundary", "BOUNDARY");
			// polyline
			IomObject polylineValue = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment=segments.addattrobj("segment", "COORD");
			startSegment.setattrvalue("C1", "480000.000");
			startSegment.setattrvalue("C2", "70000.000");
			IomObject endSegment=segments.addattrobj("segment", "COORD");
			endSegment.setattrvalue("C1", "500000.000");
			endSegment.setattrvalue("C2", "80000.000");
			// polyline 2
			IomObject polylineValue2 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments2=polylineValue2.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment2=segments2.addattrobj("segment", "COORD");
			startSegment2.setattrvalue("C1", "500000.000");
			startSegment2.setattrvalue("C2", "80000.000");
			IomObject endSegment2=segments2.addattrobj("segment", "COORD");
			endSegment2.setattrvalue("C1", "550000.000");
			endSegment2.setattrvalue("C2", "90000.000");
			// polyline 3
			IomObject polylineValue3 = outerBoundary.addattrobj("polyline", "POLYLINE");
			IomObject segments3=polylineValue3.addattrobj("sequence", "SEGMENTS");
			IomObject startSegment3=segments3.addattrobj("segment", "COORD");
			startSegment3.setattrvalue("C1", "550000.000");
			startSegment3.setattrvalue("C2", "90000.000");
			IomObject endSegment3=segments3.addattrobj("segment", "COORD");
			endSegment3.setattrvalue("C1", "480000.000");
			endSegment3.setattrvalue("C2", "70000.000");
		}
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline [POLYLINE {sequence SEGMENTS {segment [COORD {C1 480000.000, C2 70000.000}, COORD {C1 500000.000, C2 80000.000}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 500000.000, C2 80000.000}, COORD {C1 550000.000, C2 90000.000}]}}, POLYLINE {sequence SEGMENTS {segment [COORD {C1 550000.000, C2 90000.000}, COORD {C1 480000.000, C2 70000.000}]}}]}}} already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn bei der Funktion: inEnumRange das ZielAttribute zwischen min und max enum in der Enumeration sich befindet.
	// und die Attribute die Selben Values enthalten, jedoch preCondition True ist.
	@Test
	public void preConditionIsTrue_AttrValuesExistTwice_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(CLASSN3, OID1);
		iomObjA.setattrvalue("attr01", "zwei");
		iomObjA.setattrvalue("attr02", "eins");
		iomObjA.setattrvalue("attr03", "vier");
		iomObjA.setattrvalue("attr04", "gleich");
		Iom_jObject iomObjB=new Iom_jObject(CLASSN3, OID2);
		iomObjB.setattrvalue("attr01", "zwei");
		iomObjB.setattrvalue("attr02", "eins");
		iomObjB.setattrvalue("attr03", "vier");
		iomObjB.setattrvalue("attr04", "gleich");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values gleich already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird,
	// wenn die PreCondition true ist und die Attribute welche unique sind, mit den Selben Values erstellt wurden.
	// Jedoch PreCondition True definiert ist.
	@Test
	public void preConditionIsTrue_UniqueAttrValuesOfAttrw3ExistsTwice_False(){
		// Set object.
		Iom_jObject obj1=new Iom_jObject(CLASSN,OID1);
		obj1.setattrvalue("attrw1", "5");
		obj1.setattrvalue("attrw2", "3");
		obj1.setattrvalue("attrw3", "1");
		Iom_jObject obj2=new Iom_jObject(CLASSN,OID2);
		obj2.setattrvalue("attrw1", "9");
		obj2.setattrvalue("attrw2", "6");
		obj2.setattrvalue("attrw3", "1");
		// Create and run validator.
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(obj1));
		validator.validate(new ObjectEvent(obj2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts.
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Unique is violated! Values 1 already exist in Object: o1", logger.getErrs().get(0).getEventMsg());
	}
}