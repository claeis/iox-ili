package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.IoxLogging;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

public class ErrorMsg23Test {

	private TransferDescription td=null;
	private final static String ILI_TOPIC="ErrorMsgTest23.Topic";
	private final static String ILI_CLASSA="ErrorMsgTest23.Topic.ClassA";
	private static final String ILI_CLASSA_ATTRA = "attrA";
	private final static String ILI_CLASSA_POINT="point";
	private final static String ILI_CLASSA_LINE="line";
	private final static String ILI_CLASSA_SURFACE="surface";
	private static final String ILI_STRUCTB = "ErrorMsgTest23.Topic.StructB";
	private static final String ILI_STRUCTB_POINT = "point";
	private static final String ILI_CLASSC = "ErrorMsgTest23.Topic.ClassC";
	private static final String ILI_CLASSC_ATTRC1 = "attrC1";
	private static final String ILI_CLASSC_ATTRC2 = "attrC2";
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/ErrorMsgTest23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	@Test
	public void noCoord(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSA, "o1");
		iomObj.setattrvalue(ILI_CLASSA_ATTRA, "true");
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals(null,logger.getErrs().get(0).getGeomC1());
		assertEquals(null,logger.getErrs().get(0).getGeomC2());
	}
	@Test
	public void coordFromPoint(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSA, "o1");
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals(new Double(480001.000),logger.getErrs().get(0).getGeomC1());
		assertEquals(new Double(70001.000),logger.getErrs().get(0).getGeomC2());
	}
	@Test
	public void coordFromLine(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSA, "o1");
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals(new Double(480001.000),logger.getErrs().get(0).getGeomC1());
		assertEquals(new Double(70001.000),logger.getErrs().get(0).getGeomC2());
	}
	@Test
	public void coordFromSurface(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSA, "o1");
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
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals(new Double(480001.000),logger.getErrs().get(0).getGeomC1());
		assertEquals(new Double(70001.000),logger.getErrs().get(0).getGeomC2());
	}
	@Test
	public void coordFromStructAttrPoint(){
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTB, null);
		Iom_jObject iomCoord=new Iom_jObject("COORD", null);
		iomCoord.setattrvalue("C1", "480001.000");
		iomCoord.setattrvalue("C2", "70001.000");
		iomStruct.addattrobj(ILI_STRUCTB_POINT, iomCoord);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSC, "o1");
		iomObj.setattrvalue(ILI_CLASSC_ATTRC1, "true");
		iomObj.addattrobj(ILI_CLASSC_ATTRC2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,"b1"));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals(new Double(480001.000),logger.getErrs().get(0).getGeomC1());
		assertEquals(new Double(70001.000),logger.getErrs().get(0).getGeomC2());
	}
	
}