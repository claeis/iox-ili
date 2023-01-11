package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
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

public class MultiSurfaceArea24Test {

    private final static String ILI_MODEL="MultiSurfaceArea24";
    private final static String ILI_TOPIC=ILI_MODEL+".TestA";
    private final static String ILI_CLASS_A1=ILI_TOPIC+".ClassA1";
    private final static String ILI_CLASS_A2=ILI_TOPIC+".ClassA2";
    private final static String ILI_ATTR_GEOM1="geomAttr1";
	private TransferDescription td=null;
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/MultiSurfaceArea24.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	
	@Test
	public void geom_Ok(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASS_A1, "o1");
        Iom_jObject multisurface=new Iom_jObject("MULTISURFACE",null);
        {
            Iom_jObject surface=new Iom_jObject("SURFACE",null);
            multisurface.addattrobj("surface",surface);
            { // exterior
                Iom_jObject boundary=new Iom_jObject("BOUNDARY",null);
                surface.addattrobj("boundary", boundary);
                Iom_jObject polyline=new Iom_jObject("POLYLINE",null);
                boundary.addattrobj("polyline", polyline);
                IomObject segments=new Iom_jObject("SEGMENTS",null);
                polyline.addattrobj("sequence", segments);
                segments.addattrobj("segment",newCOORD("2530000", "1150000"));
                segments.addattrobj("segment",newCOORD("2530010", "1150000"));
                segments.addattrobj("segment",newCOORD("2530010", "1150010"));
                segments.addattrobj("segment",newCOORD("2530000", "1150010"));
                segments.addattrobj("segment",newCOORD("2530000", "1150000"));
            }
            { // interior
                Iom_jObject boundary=new Iom_jObject("BOUNDARY",null);
                surface.addattrobj("boundary", boundary);
                Iom_jObject polyline=new Iom_jObject("POLYLINE",null);
                boundary.addattrobj("polyline", polyline);
                IomObject segments=new Iom_jObject("SEGMENTS",null);
                polyline.addattrobj("sequence", segments);
                segments.addattrobj("segment",newCOORD("2530003", "1150003"));
                segments.addattrobj("segment",newCOORD("2530006", "1150003"));
                segments.addattrobj("segment",newCOORD("2530006", "1150006"));
                segments.addattrobj("segment",newCOORD("2530003", "1150006"));
                segments.addattrobj("segment",newCOORD("2530003", "1150003"));
            }
        }
        {
            Iom_jObject surface=new Iom_jObject("SURFACE",null);
            multisurface.addattrobj("surface",surface);
            { // exterior
                Iom_jObject boundary=new Iom_jObject("BOUNDARY",null);
                surface.addattrobj("boundary", boundary);
                Iom_jObject polyline=new Iom_jObject("POLYLINE",null);
                boundary.addattrobj("polyline", polyline);
                IomObject segments=new Iom_jObject("SEGMENTS",null);
                polyline.addattrobj("sequence", segments);
                segments.addattrobj("segment",newCOORD("2540000", "1150000"));
                segments.addattrobj("segment",newCOORD("2540010", "1150000"));
                segments.addattrobj("segment",newCOORD("2540010", "1150010"));
                segments.addattrobj("segment",newCOORD("2540000", "1150010"));
                segments.addattrobj("segment",newCOORD("2540000", "1150000"));
            }
            { // interior
                Iom_jObject boundary=new Iom_jObject("BOUNDARY",null);
                surface.addattrobj("boundary", boundary);
                Iom_jObject polyline=new Iom_jObject("POLYLINE",null);
                boundary.addattrobj("polyline", polyline);
                IomObject segments=new Iom_jObject("SEGMENTS",null);
                polyline.addattrobj("sequence", segments);
                segments.addattrobj("segment",newCOORD("2540003", "1150003"));
                segments.addattrobj("segment",newCOORD("2540006", "1150003"));
                segments.addattrobj("segment",newCOORD("2540006", "1150006"));
                segments.addattrobj("segment",newCOORD("2540003", "1150006"));
                segments.addattrobj("segment",newCOORD("2540003", "1150003"));
            }
        }
		iomObj.addattrobj(ILI_ATTR_GEOM1, multisurface);
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
    public void intersection_Fail(){
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASS_A1, "o1");
        Iom_jObject multisurface=new Iom_jObject("MULTISURFACE",null);
        {
            Iom_jObject surface=new Iom_jObject("SURFACE",null);
            multisurface.addattrobj("surface",surface);
            { // exterior
                Iom_jObject boundary=new Iom_jObject("BOUNDARY",null);
                surface.addattrobj("boundary", boundary);
                Iom_jObject polyline=new Iom_jObject("POLYLINE",null);
                boundary.addattrobj("polyline", polyline);
                IomObject segments=new Iom_jObject("SEGMENTS",null);
                polyline.addattrobj("sequence", segments);
                segments.addattrobj("segment",newCOORD("2530000", "1150000"));
                segments.addattrobj("segment",newCOORD("2530010", "1150000"));
                segments.addattrobj("segment",newCOORD("2530010", "1150010"));
                segments.addattrobj("segment",newCOORD("2530000", "1150010"));
                segments.addattrobj("segment",newCOORD("2530000", "1150000"));
            }
            { // interior
                Iom_jObject boundary=new Iom_jObject("BOUNDARY",null);
                surface.addattrobj("boundary", boundary);
                Iom_jObject polyline=new Iom_jObject("POLYLINE",null);
                boundary.addattrobj("polyline", polyline);
                IomObject segments=new Iom_jObject("SEGMENTS",null);
                polyline.addattrobj("sequence", segments);
                segments.addattrobj("segment",newCOORD("2530003", "1150003"));
                segments.addattrobj("segment",newCOORD("2530006", "1150003"));
                segments.addattrobj("segment",newCOORD("2530006", "1150006"));
                segments.addattrobj("segment",newCOORD("2530003", "1150006"));
                segments.addattrobj("segment",newCOORD("2530003", "1150003"));
            }
        }
        {
            Iom_jObject surface=new Iom_jObject("SURFACE",null);
            multisurface.addattrobj("surface",surface);
            { // exterior
                Iom_jObject boundary=new Iom_jObject("BOUNDARY",null);
                surface.addattrobj("boundary", boundary);
                Iom_jObject polyline=new Iom_jObject("POLYLINE",null);
                boundary.addattrobj("polyline", polyline);
                IomObject segments=new Iom_jObject("SEGMENTS",null);
                polyline.addattrobj("sequence", segments);
                segments.addattrobj("segment",newCOORD("2530009", "1150009"));
                segments.addattrobj("segment",newCOORD("2530011", "1150009"));
                segments.addattrobj("segment",newCOORD("2530011", "1150011"));
                segments.addattrobj("segment",newCOORD("2530009", "1150011"));
                segments.addattrobj("segment",newCOORD("2530009", "1150009"));
            }
        }
        iomObj.addattrobj(ILI_ATTR_GEOM1, multisurface);
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
        assertTrue(logger.getErrs().size()==2);
        assertEquals("Intersection coord1 (2530010.000, 1150009.000), tids o1, o1", logger.getErrs().get(0).getEventMsg());
        assertEquals("Intersection coord1 (2530009.000, 1150010.000), tids o1, o1", logger.getErrs().get(1).getEventMsg());
    }
    private Iom_jObject newCOORD(String c1, String c2) {
        Iom_jObject coord=new Iom_jObject("COORD",null);
        coord.setattrvalue("C1", c1);
        coord.setattrvalue("C2", c2);
        return coord;
    }


    @Test
    public void geomOutOfRange_Fail(){
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASS_A1, "o1");
        Iom_jObject multisurface=new Iom_jObject("MULTISURFACE",null);
        {
            Iom_jObject surface=new Iom_jObject("SURFACE",null);
            multisurface.addattrobj("surface",surface);
            { // exterior
                Iom_jObject boundary=new Iom_jObject("BOUNDARY",null);
                surface.addattrobj("boundary", boundary);
                Iom_jObject polyline=new Iom_jObject("POLYLINE",null);
                boundary.addattrobj("polyline", polyline);
                IomObject segments=new Iom_jObject("SEGMENTS",null);
                polyline.addattrobj("sequence", segments);
                segments.addattrobj("segment",newCOORD("2530000", "1150000"));
                segments.addattrobj("segment",newCOORD("2530010", "1150000"));
                segments.addattrobj("segment",newCOORD("2530010", "1150010"));
                segments.addattrobj("segment",newCOORD("2530000", "1150010"));
                segments.addattrobj("segment",newCOORD("2530000", "1150000"));
            }
            { // interior
                Iom_jObject boundary=new Iom_jObject("BOUNDARY",null);
                surface.addattrobj("boundary", boundary);
                Iom_jObject polyline=new Iom_jObject("POLYLINE",null);
                boundary.addattrobj("polyline", polyline);
                IomObject segments=new Iom_jObject("SEGMENTS",null);
                polyline.addattrobj("sequence", segments);
                segments.addattrobj("segment",newCOORD("2530000", "1150000"));
                segments.addattrobj("segment",newCOORD("2530003", "1150000"));
                segments.addattrobj("segment",newCOORD("2530003", "1150006"));
                segments.addattrobj("segment",newCOORD("2530000", "1150006"));
                segments.addattrobj("segment",newCOORD("2530000", "1150000"));
            }
        }
        {
            Iom_jObject surface=new Iom_jObject("SURFACE",null);
            multisurface.addattrobj("surface",surface);
            { // exterior
                Iom_jObject boundary=new Iom_jObject("BOUNDARY",null);
                surface.addattrobj("boundary", boundary);
                Iom_jObject polyline=new Iom_jObject("POLYLINE",null);
                boundary.addattrobj("polyline", polyline);
                IomObject segments=new Iom_jObject("SEGMENTS",null);
                polyline.addattrobj("sequence", segments);
                segments.addattrobj("segment",newCOORD("2540000", "1150000"));
                segments.addattrobj("segment",newCOORD("2540010", "1150000"));
                segments.addattrobj("segment",newCOORD("2540010", "1150010"));
                segments.addattrobj("segment",newCOORD("2540000", "1150010"));
                segments.addattrobj("segment",newCOORD("2540000", "1150000"));
            }
            { // interior
                Iom_jObject boundary=new Iom_jObject("BOUNDARY",null);
                surface.addattrobj("boundary", boundary);
                Iom_jObject polyline=new Iom_jObject("POLYLINE",null);
                boundary.addattrobj("polyline", polyline);
                IomObject segments=new Iom_jObject("SEGMENTS",null);
                polyline.addattrobj("sequence", segments);
                segments.addattrobj("segment",newCOORD("2540000", "1150000"));
                segments.addattrobj("segment",newCOORD("2540003", "1150000"));
                segments.addattrobj("segment",newCOORD("2540003", "1150006"));
                segments.addattrobj("segment",newCOORD("2540000", "1150006"));
                segments.addattrobj("segment",newCOORD("540000", "1150000")); // C1 out of range
            }
        }
        iomObj.addattrobj(ILI_ATTR_GEOM1, multisurface);
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
        assertEquals("value 540000.000 is out of range in attribute geomAttr1", logger.getErrs().get(0).getEventMsg());
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
		assertEquals("The value <textValue> is not a Polygon in attribute geomAttr1", logger.getErrs().get(0).getEventMsg());
	}
    @Test
    public void multisurfacex_Fail(){
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASS_A1, "o1");
        Iom_jObject multisurface=new Iom_jObject("MULTISURFACEX",null);
        iomObj.addattrobj(ILI_ATTR_GEOM1, multisurface);
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
        assertEquals("unexpected Type MULTISURFACEX; MULTISURFACE expected", logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void multisurfaceEmpty_Fail(){
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASS_A1, "o1");
        Iom_jObject multisurface=new Iom_jObject("MULTISURFACE",null);
        iomObj.addattrobj(ILI_ATTR_GEOM1, multisurface);
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
        assertEquals("invalid number of SURFACEs in MULTISURFACE", logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void noBoundary_Fail(){
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASS_A1, "o1");
        Iom_jObject multisurface=new Iom_jObject("MULTISURFACE",null);
        {
            Iom_jObject surface=new Iom_jObject("SURFACE",null);
            multisurface.addattrobj("surface",surface);
            { // exterior
                Iom_jObject boundary=new Iom_jObject("BOUNDARY",null);
                surface.addattrobj("boundary", boundary);
                Iom_jObject polyline=new Iom_jObject("POLYLINE",null);
                boundary.addattrobj("polyline", polyline);
                IomObject segments=new Iom_jObject("SEGMENTS",null);
                polyline.addattrobj("sequence", segments);
                segments.addattrobj("segment",newCOORD("2530000", "1150000"));
                segments.addattrobj("segment",newCOORD("2530010", "1150000"));
                segments.addattrobj("segment",newCOORD("2530010", "1150010"));
                segments.addattrobj("segment",newCOORD("2530000", "1150010"));
                segments.addattrobj("segment",newCOORD("2530000", "1150000"));
            }
            { // interior
                Iom_jObject boundary=new Iom_jObject("BOUNDARY",null);
                surface.addattrobj("boundary", boundary);
                Iom_jObject polyline=new Iom_jObject("POLYLINE",null);
                boundary.addattrobj("polyline", polyline);
                IomObject segments=new Iom_jObject("SEGMENTS",null);
                polyline.addattrobj("sequence", segments);
                segments.addattrobj("segment",newCOORD("2530000", "1150000"));
                segments.addattrobj("segment",newCOORD("2530003", "1150000"));
                segments.addattrobj("segment",newCOORD("2530003", "1150006"));
                segments.addattrobj("segment",newCOORD("2530000", "1150006"));
                segments.addattrobj("segment",newCOORD("2530000", "1150000"));
            }
        }
        {
            Iom_jObject surface=new Iom_jObject("SURFACE",null);
            multisurface.addattrobj("surface",surface);
        }
        iomObj.addattrobj(ILI_ATTR_GEOM1, multisurface);
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
        assertEquals("missing outerboundary in geomAttr1 of object o1.", logger.getErrs().get(0).getEventMsg());
    }

}