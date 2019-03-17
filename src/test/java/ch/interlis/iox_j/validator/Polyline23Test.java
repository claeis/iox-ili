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
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

public class Polyline23Test {

	private TransferDescription td=null;
	// OID
	private final static String OBJ_OID1 ="o1";
	// MODEL.TOPIC
	private final static String ILI_TOPIC="Datatypes23.Topic";
	// CLASS
	private final static String ILI_CLASSBDIRECTED=ILI_TOPIC+".ClassBDirected";
	private final static String ILI_CLASSB=ILI_TOPIC+".ClassB";
	// START BASKET EVENT
	private final static String BID="b1";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/Datatypes23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////// SUCCESSFUL Tests ////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	
	//  Es wird getestet ob eine 2d Linie erstelt werden kann.
	@Test
	public void straight2dPolyline_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("straights2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coordStart=segments.addattrobj("segment", "COORD");
		IomObject coordEnd=segments.addattrobj("segment", "COORD");
		coordStart.setattrvalue("C1", "480000.000");
		coordStart.setattrvalue("C2", "70000.000");
		coordEnd.setattrvalue("C1", "480001.000");
		coordEnd.setattrvalue("C2", "70001.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob eine 3d Linie erstellt werden kann.
	@Test
	public void straight3dPolyline_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("straights3d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coordStart=segments.addattrobj("segment", "COORD");
		coordStart.setattrvalue("C1", "480000.000");
		coordStart.setattrvalue("C2", "70000.000");
		coordStart.setattrvalue("C3", "4000.000");
		IomObject coordEnd=segments.addattrobj("segment", "COORD");
		coordEnd.setattrvalue("C1", "490000.000");
		coordEnd.setattrvalue("C2", "70000.000");
		coordEnd.setattrvalue("C3", "4000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob eine 2d Linie und ein 2d Kreisbogen erstellt werden kann.
	@Test
	public void straightAndArc2dPolyline_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("straightsarcs2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "490000.000");
		endSegment.setattrvalue("C2", "70000.000");
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "500000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "480000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob eine 3d Linie und ein 3d Kreisbogen erstellt werden kann.
	@Test
	public void straightAndArc3dPolyline_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("straightsarcs3d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject secondSegment=segments.addattrobj("segment", "COORD");
		secondSegment.setattrvalue("C1", "490000.000");
		secondSegment.setattrvalue("C2", "70000.000");
		secondSegment.setattrvalue("C3", "5000.000");
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "480000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "500000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		arcSegment.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob ein 2d Kreisbogen erstellt werden kann.
	@Test
	public void arc2dPolyline_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("arcs2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "480000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "480000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob eine 3d Linie erstellt werden kann.
	@Test
	public void arc3dPolyline_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("arcs3d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		startSegment.setattrvalue("C3", "5000.000");
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "480000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "480000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		arcSegment.setattrvalue("C3", "5000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob die Linienpunkte aneinander gehaengt werden koennen.
	@Test
	public void createASeriesOf2dStraightLines_Ok(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("straights2dWithoutOverlaps", "POLYLINE");
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
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	// Es wird getestet ob die Linienpunkte aneinander gehaengt werden koennen.
//	@Test
//	public void createASeriesOf3dStraightLines_Ok(){
//		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
//		IomObject polylineValue=objStraightsSuccess.addattrobj("straights3dWithoutOverlaps", "POLYLINE");
//		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
//		IomObject coord=null;
//		coord=segments.addattrobj("segment", "COORD");
//		coord.setattrvalue("C1", "480000.000");
//		coord.setattrvalue("C2", "70000.000");
//		coord.setattrvalue("C3", "5000.000");
//		coord=segments.addattrobj("segment", "COORD");
//		coord.setattrvalue("C1", "480010.000");
//		coord.setattrvalue("C2", "70000.000");
//		coord.setattrvalue("C3", "5000.000");
//		coord=segments.addattrobj("segment", "COORD");
//		coord.setattrvalue("C1", "480020.000");
//		coord.setattrvalue("C2", "70000.000");
//		coord.setattrvalue("C3", "5000.000");
//		coord=segments.addattrobj("segment", "COORD");
//		coord.setattrvalue("C1", "480030.000");
//		coord.setattrvalue("C2", "70000.000");
//		coord.setattrvalue("C3", "5000.000");
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
//		validator.validate(new ObjectEvent(objStraightsSuccess));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertEquals(0,logger.getErrs().size());
//	}
	
	///////////////////////////////// END POLYLINE ///////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////// FAILING Tests ///////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////	
	
	// Es wird  getestet ob der Type von straights2d einer Linie vom Typ POLYLINE entspricht.
	@Test
	public void typeOfPolylineUnvalid_Fail(){
		Iom_jObject objStraightsFail=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
		IomObject polylineValue=objStraightsFail.addattrobj("straights2d", "LINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coordStart=segments.addattrobj("segment", "COORD");
		coordStart.setattrvalue("C1", "480000.000");
		coordStart.setattrvalue("C2", "70000.000");
		IomObject coordEnd=segments.addattrobj("segment", "COORD");
		coordEnd.setattrvalue("C1", "500000.000");
		coordEnd.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsFail));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("unexpected Type LINE; POLYLINE expected", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob die Sequenz dem Typ SEQUENCE entspricht.
	@Test
	public void sequenceTypeWrong_Fail(){
		Iom_jObject objStraightsFail=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
		IomObject polylineValue=objStraightsFail.addattrobj("straights2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "COORD");
		IomObject coordStart=segments.addattrobj("segment", "COORD");
		IomObject coordEnd=segments.addattrobj("segment", "COORD");
		coordStart.setattrvalue("C1", "480000.000");
		coordEnd.setattrvalue("C1", "480000.000");
		coordStart.setattrvalue("C2", "70000.000");
		coordEnd.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsFail));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("unexpected Type COORD", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob der Name der Koordinate gefunden wird.
	@Test
	public void coordNameNotFound_Fail(){
		Iom_jObject objStraightsFail=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
		IomObject polylineValue=objStraightsFail.addattrobj("straights2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coordStart=segments.addattrobj("segment", "POLYLINE");
		IomObject coordEnd=segments.addattrobj("segment", "COORD");
		coordStart.setattrvalue("C1", "480000.000");
		coordEnd.setattrvalue("C1", "480000.000");
		coordStart.setattrvalue("C2", "70000.000");
		coordEnd.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsFail));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("unexpected Type POLYLINE", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob bei einem Kompletten Polsline Type, zwei Sequenzen erstellt werden koennen.
	@Test
	public void completeBy2Sequences_Fail(){
		Iom_jObject objStraightsFail=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
		IomObject polylineValue=objStraightsFail.addattrobj("straights2d", "POLYLINE");
		polylineValue.setobjectconsistency(IomConstants.IOM_COMPLETE);
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject segment2=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject coordStart=segments.addattrobj("segment", "COORD");
		IomObject coordEnd=segments.addattrobj("segment", "COORD");
		coordStart.setattrvalue("C1", "480000.000");
		coordEnd.setattrvalue("C1", "480000.000");
		coordStart.setattrvalue("C2", "70000.000");
		coordEnd.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsFail));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("invalid number of sequences in COMPLETE basket", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob bei einem STRAIGHT, ein ARC definiert werden kann.
	@Test
	public void unexpectedTypeARC_Fail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("straightsarcs2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "480000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "480000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "480000.000");
		endSegment.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("unexpected ARC", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob bei einem ARC, ein STRAIGHT gesetzt werden kann.
	@Test
	public void unexpectedStraight_Fail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("arcs2d", "POLYLINE");
		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
		IomObject startSegment=segments.addattrobj("segment", "COORD");
		startSegment.setattrvalue("C1", "480000.000");
		startSegment.setattrvalue("C2", "70000.000");
		IomObject endSegment=segments.addattrobj("segment", "COORD");
		endSegment.setattrvalue("C1", "480000.000");
		endSegment.setattrvalue("C2", "70000.000");
		IomObject arcSegment=segments.addattrobj("segment", "ARC");
		arcSegment.setattrvalue("A1", "480000.000");
		arcSegment.setattrvalue("A2", "300000.000");
		arcSegment.setattrvalue("C1", "480000.000");
		arcSegment.setattrvalue("C2", "70000.000");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("unexpected COORD", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob Linienueberschneidungen zu einem Fehler fuehren. Ja.
	@Test
	public void intersectionBy2dPolylines_Fail(){
		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
		IomObject polylineValue=objStraightsSuccess.addattrobj("straights2dWithoutOverlaps", "POLYLINE");
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
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
		validator.validate(new ObjectEvent(objStraightsSuccess));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(1,logger.getErrs().size());
		assertEquals("Attribute straights2dWithoutOverlaps has an invalid self-intersection at (480005.0, 70000.0)", logger.getErrs().get(0).getEventMsg());
	}
	
    @Test
    public void notApolyline_Fail(){
        Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
        objStraightsSuccess.setattrvalue("straights2d", "5");
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
        validator.validate(new ObjectEvent(objStraightsSuccess));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals("The value <5> is not a Polyline in attribute straights2d", logger.getErrs().get(0).getEventMsg());
    }
	
	// Es wird getestet ob bei 3d Polylines, Fehler bei ueberschneidungen ausgegeben werden.
//	@Test
//	public void intersectionBy3dPolylines_Fail(){
//		Iom_jObject objStraightsSuccess=new Iom_jObject(ILI_CLASSB, OBJ_OID1);
//		IomObject polylineValue=objStraightsSuccess.addattrobj("straights3dWithoutOverlaps", "POLYLINE");
//		IomObject segments=polylineValue.addattrobj("sequence", "SEGMENTS");
//		IomObject coord=null;
//		coord=segments.addattrobj("segment", "COORD");
//		coord.setattrvalue("C1", "480000.000");
//		coord.setattrvalue("C2", "70000.000");
//		coord.setattrvalue("C3", "5000.000");
//		coord=segments.addattrobj("segment", "COORD");
//		coord.setattrvalue("C1", "480010.000");
//		coord.setattrvalue("C2", "70000.000");
//		coord.setattrvalue("C3", "5000.000");
//		coord=segments.addattrobj("segment", "COORD");
//		coord.setattrvalue("C1", "480010.000");
//		coord.setattrvalue("C2", "70010.000");
//		coord.setattrvalue("C3", "5000.000");
//		coord=segments.addattrobj("segment", "COORD");
//		coord.setattrvalue("C1", "480005.000");
//		coord.setattrvalue("C2", "70000.000");
//		coord.setattrvalue("C3", "5000.000");
//		ValidationConfig modelConfig=new ValidationConfig();
//		LogCollector logger=new LogCollector();
//		LogEventFactory errFactory=new LogEventFactory();
//		Settings settings=new Settings();
//		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
//		validator.validate(new StartTransferEvent());
//		validator.validate(new StartBasketEvent(ILI_TOPIC,BID));
//		validator.validate(new ObjectEvent(objStraightsSuccess));
//		validator.validate(new EndBasketEvent());
//		validator.validate(new EndTransferEvent());
//		// Asserts
//		assertEquals(1,logger.getErrs().size());
//		assertEquals("Attribute straights3dWithoutOverlaps has an invalid self-intersection at (480005.0, 70000.0)", logger.getErrs().get(0).getEventMsg());
//	}
}