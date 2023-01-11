package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.TypeAlias;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

public class MultiPolyline24Test {

    private final static String ILI_MODEL="MultiPolyline24";
    private final static String ILI_TOPIC=ILI_MODEL+".TestA";
    private final static String ILI_CLASS_A1=ILI_TOPIC+".ClassA1";
    private final static String ILI_ATTR_GEOM1="geomAttr1";
	private TransferDescription td=null;
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/MultiPolyline24.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	
	@Test
	public void geom_Ok(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASS_A1, "o1");
        Iom_jObject multipolyline=new Iom_jObject("MULTIPOLYLINE",null);
        {
            Iom_jObject polyline=new Iom_jObject("POLYLINE",null);
            IomObject segments=new Iom_jObject("SEGMENTS",null);
            polyline.addattrobj("sequence", segments);
            Iom_jObject coord=null;
            coord=new Iom_jObject("COORD",null);
            coord.setattrvalue("C1", "2530000");
            coord.setattrvalue("C2", "1150000");
            coord.setattrvalue("C3", "467.012");
            segments.addattrobj("segment", coord);
            coord=new Iom_jObject("COORD",null);
            coord.setattrvalue("C1", "2530010");
            coord.setattrvalue("C2", "1150000");
            coord.setattrvalue("C3", "468.124");
            segments.addattrobj("segment", coord);
            coord=new Iom_jObject("COORD",null);
            coord.setattrvalue("C1", "2530010");
            coord.setattrvalue("C2", "1150010");
            coord.setattrvalue("C3", "469.577");
            segments.addattrobj("segment", coord);
            multipolyline.addattrobj("polyline", polyline);
        }
        {
            Iom_jObject polyline=new Iom_jObject("POLYLINE",null);
            IomObject segments=new Iom_jObject("SEGMENTS",null);
            polyline.addattrobj("sequence", segments);
            Iom_jObject coord=null;
            coord=new Iom_jObject("COORD",null);
            coord.setattrvalue("C1", "2530070.010");
            coord.setattrvalue("C2", "1150050.010");
            coord.setattrvalue("C3", "467.010");
            segments.addattrobj("segment", coord);
            coord=new Iom_jObject("ARC",null);
            coord.setattrvalue("C1", "2530060.010");
            coord.setattrvalue("C2", "1150045.010");
            coord.setattrvalue("C3", "466.010");
            coord.setattrvalue("A1", "2530060.010");
            coord.setattrvalue("A2", "1150040.010");
            coord.setattrvalue("R", "3.010");
            segments.addattrobj("segment", coord);
            coord=new Iom_jObject("COORD",null);
            coord.setattrvalue("C1", "2530010.010");
            coord.setattrvalue("C2", "1150045.010");
            coord.setattrvalue("C3", "465.010");
            segments.addattrobj("segment", coord);
            multipolyline.addattrobj("polyline", polyline);
        }
		iomObj.addattrobj(ILI_ATTR_GEOM1, multipolyline);
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
		assertTrue(logger.getErrs().size()==0);
	}
    @Test
    public void geomOutOfRange_Fail(){
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASS_A1, "o1");
        Iom_jObject multipolyline=new Iom_jObject("MULTIPOLYLINE",null);
        {
            Iom_jObject polyline=new Iom_jObject("POLYLINE",null);
            IomObject segments=new Iom_jObject("SEGMENTS",null);
            polyline.addattrobj("sequence", segments);
            Iom_jObject coord=null;
            coord=new Iom_jObject("COORD",null);
            coord.setattrvalue("C1", "2530000");
            coord.setattrvalue("C2", "1150000");
            coord.setattrvalue("C3", "467.012");
            segments.addattrobj("segment", coord);
            coord=new Iom_jObject("COORD",null);
            coord.setattrvalue("C1", "2530010");
            coord.setattrvalue("C2", "1150000");
            coord.setattrvalue("C3", "468.124");
            segments.addattrobj("segment", coord);
            coord=new Iom_jObject("COORD",null);
            coord.setattrvalue("C1", "2530010");
            coord.setattrvalue("C2", "1150010");
            coord.setattrvalue("C3", "469.577");
            segments.addattrobj("segment", coord);
            multipolyline.addattrobj("polyline", polyline);
        }
        {
            Iom_jObject polyline=new Iom_jObject("POLYLINE",null);
            IomObject segments=new Iom_jObject("SEGMENTS",null);
            polyline.addattrobj("sequence", segments);
            Iom_jObject coord=null;
            coord=new Iom_jObject("COORD",null);
            coord.setattrvalue("C1", "2530070.010");
            coord.setattrvalue("C2", "1150050.010");
            coord.setattrvalue("C3", "467.010");
            segments.addattrobj("segment", coord);
            coord=new Iom_jObject("ARC",null);
            coord.setattrvalue("C1", "2530060.010");
            coord.setattrvalue("C2", "1150045.010");
            coord.setattrvalue("C3", "466.010");
            coord.setattrvalue("A1", "2530060.010");
            coord.setattrvalue("A2", "221150040.010");
            coord.setattrvalue("R", "3.010");
            segments.addattrobj("segment", coord);
            coord=new Iom_jObject("COORD",null);
            coord.setattrvalue("C1", "2530010.010");
            coord.setattrvalue("C2", "1150045.010");
            coord.setattrvalue("C3", "465.010");
            segments.addattrobj("segment", coord);
            multipolyline.addattrobj("polyline", polyline);
        }
        iomObj.addattrobj(ILI_ATTR_GEOM1, multipolyline);
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
        assertEquals("value 221150040.010 is out of range in attribute geomAttr1", logger.getErrs().get(0).getEventMsg());
    }

	@Test
	public void simpleValue_Fail() {
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASS_A1, "o1");
		// Set Attributes
		iomObj.setattrvalue(ILI_ATTR_GEOM1, "textValue");
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
		assertEquals("The value <textValue> is not a Polyline in attribute geomAttr1", logger.getErrs().get(0).getEventMsg());
	}
    @Test
    public void multipolylinex_Fail(){
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASS_A1, "o1");
        Iom_jObject multicoord=new Iom_jObject("MULTIPOLYLINEX",null);
        iomObj.addattrobj(ILI_ATTR_GEOM1, multicoord);
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
        assertEquals("unexpected Type MULTIPOLYLINEX; MULTIPOLYLINE expected", logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void multipolylineEmpty_Fail(){
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASS_A1, "o1");
        Iom_jObject multicoord=new Iom_jObject("MULTIPOLYLINE",null);
        iomObj.addattrobj(ILI_ATTR_GEOM1, multicoord);
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
        assertEquals("invalid number of POLYLINEs in MULTIPOLYLINE", logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void polylinex_Fail(){
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASS_A1, "o1");
        Iom_jObject multipolyline=new Iom_jObject("MULTIPOLYLINE",null);
        Iom_jObject polyline=new Iom_jObject("POLYLINEX",null);
        multipolyline.addattrobj("polyline", polyline);
        iomObj.addattrobj(ILI_ATTR_GEOM1, multipolyline);
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
        assertEquals("unexpected Type POLYLINEX; POLYLINE expected", logger.getErrs().get(0).getEventMsg());
    }

}