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
	private static final String ILI_CLASSF_WITH_AREA = "ErrorMsgTest23.Topic.ClassF";
	private static final String ILI_CLASSG_WITH_AREAREAS_CONSTRAINT = "ErrorMsgTest23.Topic.ClassG";
	// STRUCTS
	private static final String ILI_STRUCTB = "ErrorMsgTest23.Topic.StructB";
	private static final String ILI_STRUCTB_POINT = "point";
	// OID
	private final static String OID="o1";
	private final static String OID_2 = "o2";
	// START BASKET EVENT
	private final static String BID="b1";

	private LogCollector logger;
	private LogEventFactory errFactory;
	private Settings settings;
	private ValidationConfig modelConfig;

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

		// setup
		logger = new LogCollector();
		errFactory = new LogEventFactory();
		settings = new Settings();
		modelConfig = new ValidationConfig();

		Map<String,Class> newFunctions=new HashMap<String,Class>();
		newFunctions.put("FunctionsExt23.subText",SubText.class);
		settings.setTransientObject(Validator.CONFIG_CUSTOM_FUNCTIONS, newFunctions);
	}
	
	// Hier wird getestet ob die beiden Koordinaten: C1 und C2 in der Fehlermeldung vorkommen,
	// wenn dieser Test eine Fehlermeldung ausgibt, jedoch kein Objekt mit Koordinaten erstellt wurde. 
	@Test
	public void noCoord_Fail(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSA, OID);
		iomObj.setattrvalue(ILI_CLASSA_ATTRA, "true");

		validateObjects(iomObj);

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

		validateObjects(iomObj);

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

		validateObjects(iomObj);

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

		validateObjects(iomObj);

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

		validateObjects(iomObj);

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

		validateObjects(iomObj);

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

		modelConfig.setConfigValue(ILI_CLASSA, ValidationConfig.KEYMSG, "Key {"+ILI_CLASSA_ATTRA2+"}");

		validateObjects(iomObj);

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

		modelConfig.setConfigValue(ILI_CLASSD_CONSTRA, ValidationConfig.MSG, "Msg {"+ILI_CLASSD_ATTRA2+"}");

		validateObjects(iomObj);

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

        String actualLanguage = Locale.getDefault().getLanguage();
        // default message
        modelConfig.setConfigValue(ILI_CLASSD_CONSTRA, ValidationConfig.MSG, "Msg {"+ILI_CLASSD_ATTRA2+"}");
        // de spezifische message
        modelConfig.setConfigValue(ILI_CLASSD_CONSTRA, ValidationConfig.MSG+"_"+actualLanguage, "Msg_lang {"+ILI_CLASSD_ATTRA2+"}");
        
        validateObjects(iomObj);

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

		validateObjects(objSurfaceSuccess);

		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals(new Double(510000.000),logger.getErrs().get(0).getGeomC1());
		assertEquals(new Double(80000.000),logger.getErrs().get(0).getGeomC2());
	}

	/**
	 * Tests that the error message coordinate fields (C1, C2, C3) are set when two features of an AREA intersect
	 */
	@Test
	public void geometryAreaIntersectionErrorMessage_Fail() {
		Iom_jObject object_1 = new Iom_jObject(ILI_CLASSF_WITH_AREA, OID);
		object_1.addattrobj("Geometry", createRectangleGeometry("500000", "70000", "600000", "80000"));

		Iom_jObject object_2 = new Iom_jObject(ILI_CLASSF_WITH_AREA, OID_2);
		object_2.addattrobj("Geometry", createRectangleGeometry("550000", "75000", "650000", "85000"));

		validateObjects(object_1, object_2);

		// Asserts
		assertEquals(3, logger.getErrs().size());
		assertErrorLogWithGeometry("Intersection coord1 (550000.000, 80000.000), tids o1, o2", 550000.0, 80000.0, logger.getErrs().get(0));
		assertErrorLogWithGeometry("Intersection coord1 (600000.000, 75000.000), tids o1, o2", 600000.0, 75000.0, logger.getErrs().get(1));
		assertEquals("failed to validate AREA ErrorMsgTest23.Topic.ClassF.Geometry", logger.getErrs().get(2).getEventMsg());
	}

	/**
	 * Tests that the error message coordinate fields (C1, C2, C3) are set when two features in an areAreas constraint intersect.
	 */
	@Test
	public void geometryAreAreasConstraintIntersectionErrorMessage_Fail() {
		Iom_jObject object_1 = new Iom_jObject(ILI_CLASSG_WITH_AREAREAS_CONSTRAINT, OID);
		object_1.addattrobj("Geometry", createRectangleGeometry("500000", "70000", "600000", "80000"));

		Iom_jObject object_2 = new Iom_jObject(ILI_CLASSG_WITH_AREAREAS_CONSTRAINT, OID_2);
		object_2.addattrobj("Geometry", createRectangleGeometry("550000", "75000", "650000", "85000"));

		validateObjects(object_1, object_2);

		// Asserts
		assertEquals(3, logger.getErrs().size());
		assertErrorLogWithGeometry("Intersection coord1 (550000.000, 80000.000), tids o1/Geometry[1], o2/Geometry[1]", 550000.0, 80000.0, logger.getErrs().get(0));
		assertErrorLogWithGeometry("Intersection coord1 (600000.000, 75000.000), tids o1/Geometry[1], o2/Geometry[1]", 600000.0, 75000.0, logger.getErrs().get(1));
		assertEquals("Set Constraint ErrorMsgTest23.Topic.ClassG.Constraint1 is not true.", logger.getErrs().get(2).getEventMsg());
	}

	private void assertErrorLogWithGeometry(String expectedMessage, double expectedC1, double expectedC2, IoxLogEvent logEvent) {
		assertEquals(expectedMessage, logEvent.getEventMsg());
		assertEquals((Double)expectedC1, logEvent.getGeomC1());
		assertEquals((Double)expectedC2, logEvent.getGeomC2());
		assertEquals((Double)Double.NaN, logEvent.getGeomC3());
	}

	private static IomObject createRectangleGeometry(String x1, String y1, String x2, String y2) {
		IomObject startSegment = new Iom_jObject("COORD", null);
		startSegment.setattrvalue("C1", x1);
		startSegment.setattrvalue("C2", y1);

		IomObject straightSegment1 = new Iom_jObject("COORD", null);
		straightSegment1.setattrvalue("C1", x1);
		straightSegment1.setattrvalue("C2", y2);

		IomObject straightSegment2 = new Iom_jObject("COORD", null);
		straightSegment2.setattrvalue("C1", x2);
		straightSegment2.setattrvalue("C2", y2);

		IomObject straightSegment3 = new Iom_jObject("COORD", null);
		straightSegment3.setattrvalue("C1", x2);
		straightSegment3.setattrvalue("C2", y1);

		IomObject straightSegment4 = new Iom_jObject("COORD", null);
		straightSegment4.setattrvalue("C1", x1);
		straightSegment4.setattrvalue("C2", y1);

		IomObject segment = new Iom_jObject("SEGMENTS", null);
		segment.addattrobj("segment", startSegment);
		segment.addattrobj("segment", straightSegment1);
		segment.addattrobj("segment", straightSegment2);
		segment.addattrobj("segment", straightSegment3);
		segment.addattrobj("segment", straightSegment4);

		IomObject polyline = new Iom_jObject("POLYLINE", null);
		polyline.addattrobj("sequence", segment);

		IomObject outerBoundary = new Iom_jObject("BOUNDARY", null);
		outerBoundary.addattrobj("polyline", polyline);

		IomObject surfaceValue = new Iom_jObject("SURFACE", null);
		surfaceValue.addattrobj("boundary", outerBoundary);

		IomObject multisurface = new Iom_jObject("MULTISURFACE", null);
		multisurface.addattrobj("surface", surfaceValue);

		return multisurface;
	}

	private void validateObjects(IomObject... iomObjects) {
		Validator validator = new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		for(IomObject iomObject : iomObjects) {
			validator.validate(new ObjectEvent(iomObject));
		}
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
	}
}
