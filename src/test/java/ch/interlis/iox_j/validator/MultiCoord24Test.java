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

public class MultiCoord24Test {

    private final static String ILI_MODEL="MultiCoord24";
    private final static String ILI_TOPIC=ILI_MODEL+".TestA";
    private final static String ILI_CLASS_A1=ILI_TOPIC+".ClassA1";
    private final static String ILI_ATTR_GEOM1="geomAttr1";
	private TransferDescription td=null;
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/MultiCoord24.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	
	@Test
	public void geom_Ok(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASS_A1, "o1");
        Iom_jObject multicoord=new Iom_jObject("MULTICOORD",null);
        Iom_jObject coord1=new Iom_jObject("COORD",null);
        coord1.setattrvalue("C1", "2530001");
        coord1.setattrvalue("C2", "1150002");
        multicoord.addattrobj("coord", coord1);
        Iom_jObject coord2=new Iom_jObject("COORD",null);
        coord2.setattrvalue("C1", "2540003");
        coord2.setattrvalue("C2", "1160004");
        multicoord.addattrobj("coord", coord2);
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
		assertTrue(logger.getErrs().size()==0);
	}
    @Test
    public void geomOutOfRange_Fail(){
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASS_A1, "o1");
        Iom_jObject multicoord=new Iom_jObject("MULTICOORD",null);
        Iom_jObject coord1=new Iom_jObject("COORD",null);
        coord1.setattrvalue("C1", "2530001");
        coord1.setattrvalue("C2", "1150002");
        multicoord.addattrobj("coord", coord1);
        Iom_jObject coord2=new Iom_jObject("COORD",null);
        coord2.setattrvalue("C1", "2540003");
        coord2.setattrvalue("C2", "4");
        multicoord.addattrobj("coord", coord2);
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
        assertEquals("value 4.000 is out of range in attribute geomAttr1", logger.getErrs().get(0).getEventMsg());
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
		assertEquals("The value <textValue> is not a Coord in attribute geomAttr1", logger.getErrs().get(0).getEventMsg());
	}
    @Test
    public void multicoordx_Fail(){
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASS_A1, "o1");
        Iom_jObject multicoord=new Iom_jObject("MULTICOORDX",null);
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
        assertEquals("unexpected Type MULTICOORDX; MULTICOORD expected", logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void multicoordEmpty_Fail(){
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASS_A1, "o1");
        Iom_jObject multicoord=new Iom_jObject("MULTICOORD",null);
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
        assertEquals("invalid number of COORDs in MULTICOORD", logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void coordx_Fail(){
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASS_A1, "o1");
        Iom_jObject multicoord=new Iom_jObject("MULTICOORD",null);
        Iom_jObject coord1=new Iom_jObject("COORDX",null);
        multicoord.addattrobj("coord", coord1);
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
        assertEquals("unexpected Type COORDX; COORD expected", logger.getErrs().get(0).getEventMsg());
    }

}