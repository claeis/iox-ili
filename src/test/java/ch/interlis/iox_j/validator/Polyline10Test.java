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

public class Polyline10Test {

	private TransferDescription td=null;
	// OID
	private final static String OBJ_OID1 ="o1";
	// MODEL.TOPIC
	private final static String ILI_TOPIC="Datatypes10.Topic";
	// CLASS
	private final static String ILI_CLASSLINETABLE=ILI_TOPIC+".LineTable";
	// START BASKET EVENT
	private final static String BID="b1";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili1cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/Datatypes10.ili", FileEntryKind.ILIMODELFILE);
		ili1cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili1cConfig);
		assertNotNull(td);
	}

	//############################################################/
	//########## SUCCESSFUL TESTS ################################/
	//############################################################/

	// Es wird getestet ob ob eine 2d Linie erstellt werden kann.
	@Test
	public void straight2dPolyline_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "70000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480010.000");
		coord.setattrvalue("C2", "70000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480020.000");
		coord.setattrvalue("C2", "70000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480030.000");
		coord.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	// Es wird getestet ob eine 3d Linie erstellt werden kann.
	@Test
	public void straight3dPolyline_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps3d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480010.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480020.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480030.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	// Es wird getestet ob ein 2d Kreisbogen erstellt werden kann.
	@Test
	public void arc2dPolyline_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps2dArc", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "780000.000");
		coord.setattrvalue("C2", "70000.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("C1", "580000.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("A1", "480000.000");
		coord.setattrvalue("A2", "80000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	// Es wird getestet ob ein 3d Kreisbogen erstellt werden kann.
	@Test
	public void arc3dPolyline_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps3dArc", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "780000.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("C1", "580000.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord.setattrvalue("A1", "480000.000");
		coord.setattrvalue("A2", "80000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	// Es wird getestet ob eine 2d Linie und ein 2d Kreisbogen erstellt werden kann.
	@Test
	public void straightAndArc2dPolyline_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps2dArcStraights", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "780000.000");
		coord.setattrvalue("C2", "70000.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("C1", "580000.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("A1", "490000.000");
		coord.setattrvalue("A2", "75000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "500020.000");
		coord.setattrvalue("C2", "70000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480030.000");
		coord.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	// Es wird getestet ob eine 3d Linie und ein 3d Kreisbogen erstellt werden kann.
	@Test
	public void straightAndArc3dPolyline_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps3dArcStraights", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "780000.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("C1", "650010.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord.setattrvalue("A1", "480010.000");
		coord.setattrvalue("A2", "80000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "600020.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "580030.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	//########################################################
	//############## FAILING TESTS ###########################
	//########################################################
	
	// Es muss ein Fehler ausgegeben werden, wenn die 2d Linie von Ihren eigenen Linienpunkten ueberschnitten wird.
	@Test
	public void straight2dPolyline_WithOverlappedLines_Fail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "70000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480010.000");
		coord.setattrvalue("C2", "70000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480010.000");
		coord.setattrvalue("C2", "70010.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480005.000");
		coord.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("Attribute lineWithoutOverlaps2d has an invalid self-intersection at (480005.0, 70000.0)", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es muss ein Fehler ausgegeben werden, wenn die 3d Linie von Ihren eigenen Linienpunkten ueberschnitten wird.
	@Test
	public void straight3dPolyline_WithOverlappedLines_Fail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps3d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480010.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480010.000");
		coord.setattrvalue("C2", "70010.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480005.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("Attribute lineWithoutOverlaps3d has an invalid self-intersection at (480005.0, 70000.0)", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es muss ein Fehler ausgegeben werden, wenn der 2d Arc sich selber ueberschneidet.
	@Test
	public void arc2dPolyline_WithOverlappedLines_Fail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps2dArc", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "70000.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("A1", "482000.000");
		coord.setattrvalue("A2", "71500.000");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "73000.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("A1", "483000.000");
		coord.setattrvalue("A2", "72000.000");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "71000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("Attribute lineWithoutOverlaps2dArc has an invalid self-intersection at (480000.0, 73000.0), coord2 (481484.5360824742, 70340.20618556702)", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es muss ein Fehler ausgegeben werden, wenn der 3d Arc sich selber ueberschneidet.
	@Test
	public void arc3dPolyline_WithOverlappedLines_Fail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps3dArc", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "70000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("A1", "482000.000");
		coord.setattrvalue("A2", "71500.000");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "73000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("A1", "483000.000");
		coord.setattrvalue("A2", "72000.000");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "71000.000");
		coord.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("Attribute lineWithoutOverlaps3dArc has an invalid self-intersection at (480000.0, 73000.0), coord2 (481484.5360824742, 70340.20618556702)", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es muessen zwei Fehler ausgegeben werden, wenn ein 2d Bogen und eine 2d Linie sich selber ueberschneiden.
	@Test
	public void arcAndStraght2dPolyline_WithOverlappingLines_Fail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps2dArcStraights", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "72000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "486000.000");
		coord.setattrvalue("C2", "72000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "482500.000");
		coord.setattrvalue("C2", "71500.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("C1", "484500.000");
		coord.setattrvalue("C2", "71500.000");
		coord.setattrvalue("A1", "483500.000");
		coord.setattrvalue("A2", "73500.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(2,logger.getErrs().size());
		assertEquals("Attribute lineWithoutOverlaps2dArcStraights has an invalid self-intersection at (482275.25512860843, 72000.0), coord2 (484724.74487139157, 72000.0)", logger.getErrs().get(0).getEventMsg());
		assertEquals("Attribute lineWithoutOverlaps2dArcStraights has an invalid self-intersection at (482500.0, 71500.0), coord2 (484670.0, 71810.0)", logger.getErrs().get(1).getEventMsg());
	}
	
	// Es muessen zwei Fehler ausgegeben werden, wenn ein 3d Bogen und eine 3d Linie sich selber ueberschneiden.
	@Test
	public void arcAndStraights3dPolylines_WithOverlappingLines_Fail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSLINETABLE, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("lineWithoutOverlaps3dArcStraights", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coord=null;
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "480000.000");
		coord.setattrvalue("C2", "72000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "486000.000");
		coord.setattrvalue("C2", "72000.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "COORD");
		coord.setattrvalue("C1", "482500.000");
		coord.setattrvalue("C2", "71500.000");
		coord.setattrvalue("C3", "5000.000");
		coord=segments.addattrobj("segment", "ARC");
		coord.setattrvalue("C1", "484500.000");
		coord.setattrvalue("C2", "71500.000");
		coord.setattrvalue("C3", "5000.000");
		coord.setattrvalue("A1", "483500.000");
		coord.setattrvalue("A2", "73500.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
        Validator.initItfValidation(settings);
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(2,logger.getErrs().size());
		assertEquals("Attribute lineWithoutOverlaps3dArcStraights has an invalid self-intersection at (482275.25512860843, 72000.0), coord2 (484724.74487139157, 72000.0)", logger.getErrs().get(0).getEventMsg());
		assertEquals("Attribute lineWithoutOverlaps3dArcStraights has an invalid self-intersection at (482500.0, 71500.0), coord2 (484670.0, 71810.0)", logger.getErrs().get(1).getEventMsg());
	}
}
