package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.IoxLogEvent;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

public class ErrorMsg23Test {
	// TID
	private TransferDescription td=null;
	// TOPIC
	private final static String ILI_TOPIC="ErrorMsgTest23.Topic";
	// CLASSES
	private final static String ILI_CLASSA="ErrorMsgTest23.Topic.ClassA";
	private static final String ILI_CLASSA_ATTRA = "attrA";
	private static final String ILI_CLASSA_ATTRA2 = "attrA2";
	private final static String ILI_CLASSA_POINT="point";
	private final static String ILI_CLASSA_LINE="line";
	private final static String ILI_CLASSA_SURFACE="surface";
	private static final String ILI_CLASSC = "ErrorMsgTest23.Topic.ClassC";
	private static final String ILI_CLASSC_ATTRC1 = "attrC1";
	private static final String ILI_CLASSC_ATTRC2 = "attrC2";
	private static final String ILI_CLASSC_ATTRC3 = "attrC3";
	private final static String ILI_CLASSD="ErrorMsgTest23.Topic.ClassD";
	private static final String ILI_CLASSD_ATTRA = "attrA";
	private static final String ILI_CLASSD_ATTRA2 = "attrA2";
	private static final String ILI_CLASSD_CONSTRA = ILI_CLASSD+".constrA";
	private static final String ILI_CLASSE = "ErrorMsgTest23.Topic.ClassE";
	// STRUCTS
	private static final String ILI_STRUCTB = "ErrorMsgTest23.Topic.StructB";
	private static final String ILI_STRUCTB_POINT = "point";
	// OID
	private final static String OID="o1";
	// START BASKET EVENT
	private final static String BID="b1";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/FunctionsExt23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		FileEntry fileEntry2=new FileEntry("src/test/data/validator/ErrorMsgTest23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry2);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	// Hier wird getestet ob die beiden Koordinaten: C1 und C2 in der Fehlermeldung vorkommen,
	// wenn dieser Test eine Fehlermeldung ausgibt, jedoch kein Objekt mit Koordinaten erstellt wurde. 
	@Test
	public void noCoord_Fail(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSA, OID);
		iomObj.setattrvalue(ILI_CLASSA_ATTRA, "true");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals(null,logger.getErrs().get(0).getGeomC1());
		assertEquals(null,logger.getErrs().get(0).getGeomC2());
	}
	
	// Hier wird getestet ob die beiden Koordinaten: C1 und C2 in der Fehlermeldung vorkommen,
	// wenn die beiden Koordinaten die vordefinierte maximale Zahl des Punktes ueberschreiten.
	@Test
	public void coordFromPoint_Fail(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSA, OID);
		iomObj.setattrvalue(ILI_CLASSA_ATTRA, "true");
		Iom_jObject iomCoord=new Iom_jObject("COORD", null);
		iomCoord.setattrvalue("C1", "480001.000");
		iomCoord.setattrvalue("C2", "70001.000");
		iomObj.addattrobj(ILI_CLASSA_POINT, iomCoord);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals(new Double(480001.000),logger.getErrs().get(0).getGeomC1());
		assertEquals(new Double(70001.000),logger.getErrs().get(0).getGeomC2());
	}
	
	// Hier wird getestet ob die beiden Koordinaten: C1 und C2 in der Fehlermeldung vorkommen,
	// wenn die beiden Koordinaten die vordefinierte maximale Zahl der Linie ueberschreiten.
	@Test
	public void coordFromLine_Fail(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSA, OID);
		iomObj.setattrvalue(ILI_CLASSA_ATTRA, "true");
		IomObject iomSequence=new Iom_jObject("SEGMENTS", null);
		IomObject iomCoord=null;
		iomCoord=new Iom_jObject("COORD", null);
		iomCoord.setattrvalue("C1", "480001.000");
		iomCoord.setattrvalue("C2", "70001.000");
		iomSequence.addattrobj("segment", iomCoord);
		iomCoord=new Iom_jObject("COORD", null);
		iomCoord.setattrvalue("C1", "480011.000");
		iomCoord.setattrvalue("C2", "70001.000");
		iomSequence.addattrobj("segment", iomCoord);
		Iom_jObject iomPolyline=new Iom_jObject("POLYLINE", null);
		iomPolyline.addattrobj("sequence", iomSequence);
		iomObj.addattrobj(ILI_CLASSA_LINE, iomPolyline);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals(new Double(480001.000),logger.getErrs().get(0).getGeomC1());
		assertEquals(new Double(70001.000),logger.getErrs().get(0).getGeomC2());
	}
	
	// Hier wird getestet ob die beiden Koordinaten: C1 und C2 in der Fehlermeldung vorkommen,
	// wenn die beiden Koordinaten die vordefinierte maximale Zahl der Oberflaeche ueberschreiten.
	@Test
	public void coordFromSurface_Fail(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSA, OID);
		iomObj.setattrvalue(ILI_CLASSA_ATTRA, "true");
		IomObject iomSequence=new Iom_jObject("SEGMENTS", null);
		IomObject iomCoord=null;
		iomCoord=new Iom_jObject("COORD", null);
		iomCoord.setattrvalue("C1", "480001.000");
		iomCoord.setattrvalue("C2", "70001.000");
		iomSequence.addattrobj("segment", iomCoord);
		iomCoord=new Iom_jObject("COORD", null);
		iomCoord.setattrvalue("C1", "480011.000");
		iomCoord.setattrvalue("C2", "70001.000");
		iomSequence.addattrobj("segment", iomCoord);
		iomCoord=new Iom_jObject("COORD", null);
		iomCoord.setattrvalue("C1", "480011.000");
		iomCoord.setattrvalue("C2", "70011.000");
		iomSequence.addattrobj("segment", iomCoord);
		iomCoord=new Iom_jObject("COORD", null);
		iomCoord.setattrvalue("C1", "480001.000");
		iomCoord.setattrvalue("C2", "70001.000");
		iomSequence.addattrobj("segment", iomCoord);
		Iom_jObject iomPolyline=new Iom_jObject("POLYLINE", null);
		iomPolyline.addattrobj("sequence", iomSequence);
		IomObject iomSurface=new Iom_jObject("MULTISURFACE",null);
		iomSurface.addattrobj("surface", "SURFACE").addattrobj("boundary", "BOUNDARY").addattrobj("polyline", iomPolyline);
		iomObj.addattrobj(ILI_CLASSA_SURFACE, iomSurface);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals(new Double(480001.000),logger.getErrs().get(0).getGeomC1());
		assertEquals(new Double(70001.000),logger.getErrs().get(0).getGeomC2());
	}
	
	// Hier wird getestet ob die Koordinate aus einem Strukturattribut in der Fehlermeldung vorkommt
	@Test
	public void coordFromStructAttrPoint_Fail(){
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTB, null);
		Iom_jObject iomCoord=new Iom_jObject("COORD", null);
		iomCoord.setattrvalue("C1", "480001.000");
		iomCoord.setattrvalue("C2", "70001.000");
		iomStruct.addattrobj(ILI_STRUCTB_POINT, iomCoord);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSC, OID);
		iomObj.setattrvalue(ILI_CLASSC_ATTRC1, "true");
		iomObj.addattrobj(ILI_CLASSC_ATTRC2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals(new Double(480001.000),logger.getErrs().get(0).getGeomC1());
		assertEquals(new Double(70001.000),logger.getErrs().get(0).getGeomC2());
	}
	// Hier wird getestet, dass bei fehlerhaftem Strukturattribut keine Koordinate gesucht wird
	@Test
	public void noCoordFromStructAttrPoint_Fail(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSC, OID);
		iomObj.setattrvalue(ILI_CLASSC_ATTRC1, "true");
		iomObj.setattrvalue(ILI_CLASSC_ATTRC2, "true"); // waere eigentlich ein Strukturattribut
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
	}
	
	// Es wird getestet ob der erstellte Key, mit dem Attribute Wert: TestKey in der Fehlermeldung ausgegeben wird.
	// zweitens wird getestet ob dieser Wert innerhalb der BenutzerID abgelegt wird.
	@Test
	public void keymsgParam_Fail(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSA, OID);
		iomObj.setattrvalue(ILI_CLASSA_ATTRA, "true");
		iomObj.setattrvalue(ILI_CLASSA_ATTRA2, "TestKey");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(ILI_CLASSA, ValidationConfig.KEYMSG, "Key {"+ILI_CLASSA_ATTRA2+"}");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		IoxLogEvent err = logger.getErrs().get(0);
		assertEquals("Key TestKey",err.getSourceObjectUsrId());
	}
	
	// Es wird getestet ob die erstellte Message, mit dem Attribute Wert: TestKey in der Fehlermeldung ausgegeben wird.
	// zweitens wird getestet ob dieser Wert innerhalb der Event Message abgelegt wird.
	@Test
	public void msgParam_Fail(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, OID);
		iomObj.setattrvalue(ILI_CLASSD_ATTRA, "0");
		iomObj.setattrvalue(ILI_CLASSD_ATTRA2, "TestKey");
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(ILI_CLASSD_CONSTRA, ValidationConfig.MSG, "Msg {"+ILI_CLASSD_ATTRA2+"}");
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		IoxLogEvent err = logger.getErrs().get(0);
		assertEquals("Msg TestKey",err.getEventMsg());
	}
	
    @Test
    public void msg_de_Param_Fail(){
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, OID);
        iomObj.setattrvalue(ILI_CLASSD_ATTRA, "0");
        iomObj.setattrvalue(ILI_CLASSD_ATTRA2, "TestKey");
        ValidationConfig modelConfig=new ValidationConfig();
        String actualLanguage = Locale.getDefault().getLanguage();
        // default message
        modelConfig.setConfigValue(ILI_CLASSD_CONSTRA, ValidationConfig.MSG, "Msg {"+ILI_CLASSD_ATTRA2+"}");
        // de spezifische message
        modelConfig.setConfigValue(ILI_CLASSD_CONSTRA, ValidationConfig.MSG+"_"+actualLanguage, "Msg_lang {"+ILI_CLASSD_ATTRA2+"}");
        
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
        validator.validate(new ObjectEvent(iomObj));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        IoxLogEvent err = logger.getErrs().get(0);
        assertEquals("Msg_lang TestKey",err.getEventMsg());
    }

	// die Geometry Error Message muss die unten genannten Inhalte der Koordinaten: C1 und C2 enthalten.
	@Test
	public void geometryErrorMessage_Fail(){
		Iom_jObject objSurfaceSuccess=new Iom_jObject(ILI_CLASSE, OID);
		IomObject coord=objSurfaceSuccess.addattrobj("Geometry", "COORD");
		coord.setattrvalue("C1", "510000.000");
		coord.setattrvalue("C2", "80000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Map<String,Class> newFunctions=new HashMap<String,Class>();
		newFunctions.put("FunctionsExt23.subText",SubText.class);
		settings.setTransientObject(Validator.CONFIG_CUSTOM_FUNCTIONS, newFunctions);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objSurfaceSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals(new Double(510000.000),logger.getErrs().get(0).getGeomC1());
		assertEquals(new Double(80000.000),logger.getErrs().get(0).getGeomC2());
	}	
}