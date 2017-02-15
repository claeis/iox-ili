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

public class ErrorMsg23Test {
	// TID
	private TransferDescription td=null;
	// TOPIC
	private final static String ILI_TOPIC="ErrorMsgTest23.Topic";
	// CLASSES
	private final static String ILI_CLASSA="ErrorMsgTest23.Topic.ClassA";
	private static final String ILI_CLASSA_ATTRA = "attrA";
	private final static String ILI_CLASSA_POINT="point";
	private final static String ILI_CLASSA_LINE="line";
	private final static String ILI_CLASSA_SURFACE="surface";
	private static final String ILI_CLASSC = "ErrorMsgTest23.Topic.ClassC";
	private static final String ILI_CLASSC_ATTRC1 = "attrC1";
	private static final String ILI_CLASSC_ATTRC2 = "attrC2";
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
		FileEntry fileEntry=new FileEntry("src/test/data/validator/ErrorMsgTest23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	// Es wird getestet ob es möglich ist, einen anderen Wert als eine Nummer zu definieren.
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
	
	// Es wird getestet ob es möglich ist, einen anderen Wert des Punktes LKoord zu definieren, welche keine Nummer ist.
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
	
	// Es wird getestet ob die falsche Definition eines Koords innerhalb einer Linie zu einem Fehler führt.
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
	
	// Es wird getestet ob die falsche Definition eines Koords in einer Oberfläche zu einem Fehler führt.
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
	
	// Es wird getestet ob die falsche Definition einer Koord innerhalb einer Struktur zu einem Fehler führt.
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
}