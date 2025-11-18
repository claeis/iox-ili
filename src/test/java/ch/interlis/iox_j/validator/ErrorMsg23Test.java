package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
	private static final String ILI_CLASSH_WITH_ALL_FAILING_CONSTRAINTS = "ErrorMsgTest23.Topic.ClassH";
	private static final String ILI_CLASSI_WITH_ALL_FAILING_CONSTRAINTS_ILIVALID_MSG = "ErrorMsgTest23.Topic.ClassI";
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
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, ValidationConfig.TRUE);

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
		object_1.addattrobj("Geometry", IomObjectHelper.createRectangleGeometry("500000", "70000", "600000", "80000"));

		Iom_jObject object_2 = new Iom_jObject(ILI_CLASSF_WITH_AREA, OID_2);
		object_2.addattrobj("Geometry", IomObjectHelper.createRectangleGeometry("550000", "75000", "650000", "85000"));

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
		object_1.addattrobj("Geometry", IomObjectHelper.createRectangleGeometry("500000", "70000", "600000", "80000"));

		Iom_jObject object_2 = new Iom_jObject(ILI_CLASSG_WITH_AREAREAS_CONSTRAINT, OID_2);
		object_2.addattrobj("Geometry", IomObjectHelper.createRectangleGeometry("550000", "75000", "650000", "85000"));

		validateObjects(object_1, object_2);

		// Asserts
		assertEquals(3, logger.getErrs().size());
		assertErrorLogWithGeometry("Intersection coord1 (550000.000, 80000.000), tids o1/Geometry[1], o2/Geometry[1]", 550000.0, 80000.0, logger.getErrs().get(0));
		assertErrorLogWithGeometry("Intersection coord1 (600000.000, 75000.000), tids o1/Geometry[1], o2/Geometry[1]", 600000.0, 75000.0, logger.getErrs().get(1));
		assertEquals("Set Constraint ErrorMsgTest23.Topic.ClassG.Constraint1 is not true.", logger.getErrs().get(2).getEventMsg());
	}

	@Test
	public void constraintMessage_Fail() {
		fillAndValidateClassWithConstraints(ILI_CLASSH_WITH_ALL_FAILING_CONSTRAINTS);

		// Asserts
		assertLogContainsMessage(logger.getInfo(), "validate mandatory constraint ErrorMsgTest23.Topic.ClassH.Constraint2...");
		assertLogContainsMessage(logger.getInfo(), "validate unique constraint ErrorMsgTest23.Topic.ClassH.Constraint3...");
		assertLogContainsMessage(logger.getInfo(), "validate existence constraint ErrorMsgTest23.Topic.ClassH.Constraint6...");
		assertLogContainsMessage(logger.getInfo(), "validate set constraint ErrorMsgTest23.Topic.ClassH.Constraint1...");
		assertLogContainsMessage(logger.getInfo(), "validate plausibility constraint ErrorMsgTest23.Topic.ClassH.Constraint4...");
		assertLogContainsMessage(logger.getInfo(), "validate plausibility constraint ErrorMsgTest23.Topic.ClassH.Constraint5...");

		assertEquals(8, logger.getErrs().size());
		assertLogContainsMessage(logger.getErrs(), "Mandatory Constraint ErrorMsgTest23.Topic.ClassH.Constraint2 is not true.");
		assertLogContainsMessage(logger.getErrs(), "Existence constraint ErrorMsgTest23.Topic.ClassH.Constraint6 is violated! The value of the attribute Attr of ErrorMsgTest23.Topic.ClassH was not found in the condition class.");
		assertLogContainsMessage(logger.getErrs(), "Unique constraint ErrorMsgTest23.Topic.ClassH.Constraint3 is violated! Values OLOGBENS already exist in Object: o1");
		assertLogContainsMessage(logger.getErrs(), "Set Constraint ErrorMsgTest23.Topic.ClassH.Constraint1 is not true.");
		assertLogContainsMessage(logger.getErrs(), "Plausibility Constraint ErrorMsgTest23.Topic.ClassH.Constraint4 is not true.");
		assertLogContainsMessage(logger.getErrs(), "Plausibility Constraint ErrorMsgTest23.Topic.ClassH.Constraint5 is not true.");
	}

	@Test
	public void constraintMessageVerbose_Fail() {
		settings.setTransientValue(Validator.CONFIG_VERBOSE, ValidationConfig.TRUE);
		fillAndValidateClassWithConstraints(ILI_CLASSH_WITH_ALL_FAILING_CONSTRAINTS);

		// Asserts
		assertLogContainsMessage(logger.getInfo(), "validate mandatory constraint ErrorMsgTest23.Topic.ClassH.Constraint2...");
		assertLogContainsMessage(logger.getInfo(), "validate unique constraint ErrorMsgTest23.Topic.ClassH.Constraint3...");
		assertLogContainsMessage(logger.getInfo(), "validate existence constraint ErrorMsgTest23.Topic.ClassH.Constraint6...");
		assertLogContainsMessage(logger.getInfo(), "validate set constraint ErrorMsgTest23.Topic.ClassH.Constraint1...");
		assertLogContainsMessage(logger.getInfo(), "validate plausibility constraint ErrorMsgTest23.Topic.ClassH.Constraint4...");
		assertLogContainsMessage(logger.getInfo(), "validate plausibility constraint ErrorMsgTest23.Topic.ClassH.Constraint5...");

		assertEquals(8, logger.getErrs().size());
		assertLogContainsMessage(logger.getErrs(), "Mandatory Constraint ErrorMsgTest23.Topic.ClassH.Constraint2 (MANDATORY CONSTRAINT Attr <> Attr;) is not true.");
		assertLogContainsMessage(logger.getErrs(), "Unique constraint ErrorMsgTest23.Topic.ClassH.Constraint3 (UNIQUE Attr;) is violated! Values OLOGBENS already exist in Object: o1");
		assertLogContainsMessage(logger.getErrs(), "Existence constraint ErrorMsgTest23.Topic.ClassH.Constraint6 (EXISTENCE CONSTRAINT Attr REQUIRED IN ErrorMsgTest23.Topic.ClassA:attrA2;) is violated! The value of the attribute Attr of ErrorMsgTest23.Topic.ClassH was not found in the condition class.");
		assertLogContainsMessage(logger.getErrs(), "Set Constraint ErrorMsgTest23.Topic.ClassH.Constraint1 (SET CONSTRAINT Attr <> Attr;) is not true.");
		assertLogContainsMessage(logger.getErrs(), "Plausibility Constraint ErrorMsgTest23.Topic.ClassH.Constraint4 (CONSTRAINT <= 50.0% Attr == Attr;) is not true.");
		assertLogContainsMessage(logger.getErrs(), "Plausibility Constraint ErrorMsgTest23.Topic.ClassH.Constraint5 (CONSTRAINT >= 50.0% Attr <> Attr;) is not true.");
	}

	@Test
	public void constraintMessageIlivalidMsg_Fail() {
		modelConfig.mergeIliMetaAttrs(td);
		fillAndValidateClassWithConstraints(ILI_CLASSI_WITH_ALL_FAILING_CONSTRAINTS_ILIVALID_MSG);

		// Asserts
		assertLogContainsMessage(logger.getInfo(), "validate mandatory constraint ErrorMsgTest23.Topic.ClassI.ENSINEPR...");
		assertLogContainsMessage(logger.getInfo(), "validate unique constraint ErrorMsgTest23.Topic.ClassI.UPENDESA...");
		assertLogContainsMessage(logger.getInfo(), "validate existence constraint ErrorMsgTest23.Topic.ClassI.ORTERINE...");
		assertLogContainsMessage(logger.getInfo(), "validate set constraint ErrorMsgTest23.Topic.ClassI.DOROHIGE...");
		assertLogContainsMessage(logger.getInfo(), "validate plausibility constraint ErrorMsgTest23.Topic.ClassI.BROLETON...");
		assertLogContainsMessage(logger.getInfo(), "validate plausibility constraint ErrorMsgTest23.Topic.ClassI.LDESCREF...");

		assertEquals(9, logger.getErrs().size());
		assertLogContainsMessage(logger.getErrs(), "This is the custom message for object with Attr OLOGBENS and mandatory constraint.");
		assertLogContainsMessage(logger.getErrs(), "This is the custom message for object with Attr OLOGBENS and existence constraint.");
		assertLogContainsMessage(logger.getErrs(), "This is the custom message for object with Attr OLOGBENS and unique.");
		assertLogContainsMessage(logger.getErrs(), "This is the custom message for object with Attr OLOGBENS and set constraint.");
		assertLogContainsMessage(logger.getErrs(), "This is the custom message and plausibility constraint (<=).");
		assertLogContainsMessage(logger.getErrs(), "This is the custom message and plausibility constraint (>=).");
	}

	@Test
	public void constraintMessageIlivalidMsgVerbose_Fail() {
		settings.setTransientValue(Validator.CONFIG_VERBOSE, ValidationConfig.TRUE);
		modelConfig.mergeIliMetaAttrs(td);
		fillAndValidateClassWithConstraints(ILI_CLASSI_WITH_ALL_FAILING_CONSTRAINTS_ILIVALID_MSG);

		// Asserts
		assertLogContainsMessage(logger.getInfo(), "validate mandatory constraint ErrorMsgTest23.Topic.ClassI.ENSINEPR...");
		assertLogContainsMessage(logger.getInfo(), "validate unique constraint ErrorMsgTest23.Topic.ClassI.UPENDESA...");
		assertLogContainsMessage(logger.getInfo(), "validate existence constraint ErrorMsgTest23.Topic.ClassI.ORTERINE...");
		assertLogContainsMessage(logger.getInfo(), "validate set constraint ErrorMsgTest23.Topic.ClassI.DOROHIGE...");
		assertLogContainsMessage(logger.getInfo(), "validate plausibility constraint ErrorMsgTest23.Topic.ClassI.BROLETON...");
		assertLogContainsMessage(logger.getInfo(), "validate plausibility constraint ErrorMsgTest23.Topic.ClassI.LDESCREF...");

		assertEquals(9, logger.getErrs().size());
		assertLogContainsMessage(logger.getErrs(), "This is the custom message for object with Attr OLOGBENS and mandatory constraint. ErrorMsgTest23.Topic.ClassI.ENSINEPR (MANDATORY CONSTRAINT Attr <> Attr;)");
		assertLogContainsMessage(logger.getErrs(), "This is the custom message for object with Attr OLOGBENS and existence constraint. ErrorMsgTest23.Topic.ClassI.ORTERINE (EXISTENCE CONSTRAINT Attr REQUIRED IN ErrorMsgTest23.Topic.ClassA:attrA2;)");
		assertLogContainsMessage(logger.getErrs(), "This is the custom message for object with Attr OLOGBENS and unique. ErrorMsgTest23.Topic.ClassI.UPENDESA (UNIQUE Attr;)");
		assertLogContainsMessage(logger.getErrs(), "This is the custom message for object with Attr OLOGBENS and set constraint. ErrorMsgTest23.Topic.ClassI.DOROHIGE (SET CONSTRAINT Attr <> Attr;)");
		assertLogContainsMessage(logger.getErrs(), "This is the custom message and plausibility constraint (<=). ErrorMsgTest23.Topic.ClassI.BROLETON (CONSTRAINT <= 50.0% Attr == Attr;)");
		assertLogContainsMessage(logger.getErrs(), "This is the custom message and plausibility constraint (>=). ErrorMsgTest23.Topic.ClassI.LDESCREF (CONSTRAINT >= 50.0% Attr <> Attr;)");
	}

	private void fillAndValidateClassWithConstraints(String clsssId) {
		// attribute value is always the same that the unique constraint fails.
		String attrValue = "OLOGBENS";

		Iom_jObject object_1 = new Iom_jObject(clsssId, OID);
		object_1.setattrvalue("Attr", attrValue);

		Iom_jObject object_2 = new Iom_jObject(clsssId, OID_2);
		object_2.setattrvalue("Attr", attrValue);

		validateObjects(object_1, object_2);
	}

	private void assertErrorLogWithGeometry(String expectedMessage, double expectedC1, double expectedC2, IoxLogEvent logEvent) {
		assertEquals(expectedMessage, logEvent.getEventMsg());
		assertEquals((Double)expectedC1, logEvent.getGeomC1());
		assertEquals((Double)expectedC2, logEvent.getGeomC2());
		assertEquals((Double)Double.NaN, logEvent.getGeomC3());
	}

	private void assertLogContainsMessage(ArrayList<IoxLogEvent> logEvents, String expectedMessage) {
		for(IoxLogEvent logEvent : logEvents) {
			if(expectedMessage.equals(logEvent.getEventMsg())) {
				return;
			}
		}

		fail(String.format("Log events did not contain the expected message <%s>", expectedMessage));
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
