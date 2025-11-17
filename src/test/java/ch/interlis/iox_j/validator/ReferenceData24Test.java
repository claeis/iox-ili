package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

public class ReferenceData24Test {
	
	private TransferDescription td=null;
    private final static String MODEL="ReferenceData24";
	// TOPIC
	private final static String ILI_TOPICA=MODEL+".TopicA";
    private final static String ILI_TOPICB=MODEL+".TopicB";
    private final static String ILI_TOPICC=MODEL+".TopicC";
	// CLASSES
	private final static String ILI_A_CLASSA=ILI_TOPICA+".ClassA";
    private final static String ILI_A_CLASSB=ILI_TOPICA+".ClassB";
    private final static String ILI_A_CLASSC=ILI_TOPICA+".ClassC";
    private final static String ILI_B_CLASSD=ILI_TOPICB+".ClassD";
    private final static String ILI_C_CLASSA=ILI_TOPICC+".ClassA";
    private final static String ILI_C_CLASSB=ILI_TOPICC+".ClassB";
    
    private final static String ILI_C_CLASSA_ATTRA1="attrA1";
    private final static String ILI_C_CLASSB_ATTRB1="attrB1";
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/ReferenceData24.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
    @Test
    public void superfluouesAttr_Ok(){
        Iom_jObject iomObj=new Iom_jObject(ILI_A_CLASSA, "o1");
        iomObj.setattrvalue("attr","5");
        iomObj.setattrvalue("illegalAttr","test"); // triggers an error, if not added as reference data
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.addReferenceData(new StartTransferEvent());
        validator.addReferenceData(new StartBasketEvent(ILI_TOPICA,"b1"));
        validator.addReferenceData(new ObjectEvent(iomObj));
        validator.addReferenceData(new EndBasketEvent());
        validator.addReferenceData(new EndTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPICA,"b2"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0,logger.getErrs().size());
    }
    @Test
    public void missingAttr_Ok(){
        Iom_jObject iomObj=new Iom_jObject(ILI_A_CLASSA, "o1");
        //iomObj.setattrvalue("attr","5"); // missing attr, triggers an error, if not added as reference data
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.addReferenceData(new StartTransferEvent());
        validator.addReferenceData(new StartBasketEvent(ILI_TOPICA,"b1"));
        validator.addReferenceData(new ObjectEvent(iomObj));
        validator.addReferenceData(new EndBasketEvent());
        validator.addReferenceData(new EndTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPICA,"b2"));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0,logger.getErrs().size());
    }
    @Test
    public void areaType_Ok(){
        Iom_jObject iomObjRefData=new Iom_jObject(ILI_A_CLASSB, "o1");
        iomObjRefData.addattrobj("geom", IomObjectHelper.createRectangleGeometry("2460100", "1045100", "2460120", "1045120"));
        Iom_jObject iomObjData=new Iom_jObject(ILI_A_CLASSB, "o2");
        iomObjData.addattrobj("geom", IomObjectHelper.createRectangleGeometry("2460110", "1045110", "2460130", "1045130"));
        
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.addReferenceData(new StartTransferEvent());
        validator.addReferenceData(new StartBasketEvent(ILI_TOPICA,"b1"));
        validator.addReferenceData(new ObjectEvent(iomObjRefData));
        validator.addReferenceData(new EndBasketEvent());
        validator.addReferenceData(new EndTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPICA,"b2"));
        validator.validate(new ObjectEvent(iomObjData));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0,logger.getErrs().size());
    }
    @Test
    public void setConstraint_Ok(){
        Iom_jObject iomObjRefData=new Iom_jObject(ILI_A_CLASSC, "o1");
        iomObjRefData.addattrobj("geom", IomObjectHelper.createRectangleGeometry("2460100", "1045100", "2460120", "1045120"));
        Iom_jObject iomObjData=new Iom_jObject(ILI_A_CLASSC, "o2");
        iomObjData.addattrobj("geom", IomObjectHelper.createRectangleGeometry("2460110", "1045110", "2460130", "1045130"));
        
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.addReferenceData(new StartTransferEvent());
        validator.addReferenceData(new StartBasketEvent(ILI_TOPICA,"b1"));
        validator.addReferenceData(new ObjectEvent(iomObjRefData));
        validator.addReferenceData(new EndBasketEvent());
        validator.addReferenceData(new EndTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPICA,"b2"));
        validator.validate(new ObjectEvent(iomObjData));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0,logger.getErrs().size());
    }
    @Test
    public void cardinalityRefData_Ok(){
        String objTargetId="o1";
        Iom_jObject iomObjtarget=new Iom_jObject(ILI_B_CLASSD, objTargetId);
        Iom_jObject o1Ref=new Iom_jObject("REF", null);
        o1Ref.setobjectrefoid(objTargetId);
        Iom_jObject iomObj1=new Iom_jObject(ILI_B_CLASSD, "o2_1");
        iomObj1.addattrobj("d1", o1Ref);
        Iom_jObject iomObj2=new Iom_jObject(ILI_B_CLASSD, "o2_2");
        iomObj2.addattrobj("d1", o1Ref);
        Iom_jObject iomObj3=new Iom_jObject(ILI_B_CLASSD, "o2_3");
        iomObj3.addattrobj("d1", o1Ref);
        ValidationConfig modelConfig=new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, ValidationConfig.TRUE);
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.addReferenceData(new StartTransferEvent());
        validator.addReferenceData(new StartBasketEvent(ILI_TOPICB,"b1"));
        validator.addReferenceData(new ObjectEvent(iomObjtarget)); // would fail, because of missing role d1
        validator.addReferenceData(new EndBasketEvent());
        validator.addReferenceData(new EndTransferEvent());
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPICB,"b2"));
        validator.validate(new ObjectEvent(iomObj1));
        validator.validate(new ObjectEvent(iomObj2));
        validator.validate(new ObjectEvent(iomObj3));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0,logger.getErrs().size());
    }
    @Test
    public void cardinalityData_Ok(){
        String objTargetId="o1";
        Iom_jObject o1Ref=new Iom_jObject("REF", null);
        o1Ref.setobjectrefoid(objTargetId);
        Iom_jObject iomObjtarget=new Iom_jObject(ILI_B_CLASSD, objTargetId);
        iomObjtarget.addattrobj("d1", o1Ref);
        Iom_jObject iomObj1=new Iom_jObject(ILI_B_CLASSD, "o2_1");
        iomObj1.addattrobj("d1", o1Ref);
        Iom_jObject iomObj2=new Iom_jObject(ILI_B_CLASSD, "o2_2");
        iomObj2.addattrobj("d1", o1Ref);
        Iom_jObject iomObj3=new Iom_jObject(ILI_B_CLASSD, "o2_3");
        iomObj3.addattrobj("d1", o1Ref);
        ValidationConfig modelConfig=new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, ValidationConfig.TRUE);
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPICB,"b1"));
        validator.validate(new ObjectEvent(iomObjtarget)); // should fail, because of 3 role dN (but only 2 valid)
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        validator.addReferenceData(new StartTransferEvent());
        validator.addReferenceData(new StartBasketEvent(ILI_TOPICB,"b2"));
        validator.addReferenceData(new ObjectEvent(iomObj1));
        validator.addReferenceData(new ObjectEvent(iomObj2));
        validator.addReferenceData(new ObjectEvent(iomObj3));
        validator.addReferenceData(new EndBasketEvent());
        validator.addReferenceData(new EndTransferEvent());
        // Asserts
        assertEquals(0,logger.getErrs().size());
        //assertEquals("No object found with OID o1.", logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void existenceConstraint_data_allObjects_Ok(){
        String value1="value1";
        Iom_jObject iomObjA1=new Iom_jObject(ILI_C_CLASSA, "a1");
        iomObjA1.setattrvalue(ILI_C_CLASSA_ATTRA1,value1);
        Iom_jObject iomObjA2=new Iom_jObject(ILI_C_CLASSA, "a2");
        iomObjA2.setattrvalue(ILI_C_CLASSA_ATTRA1,value1+"x");
        Iom_jObject iomObjB1=new Iom_jObject(ILI_C_CLASSB, "b1");
        iomObjB1.setattrvalue(ILI_C_CLASSB_ATTRB1,value1);
        ValidationConfig modelConfig=new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, ValidationConfig.TRUE);
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPICC,"bid1"));
        validator.validate(new ObjectEvent(iomObjA1)); 
        validator.validate(new ObjectEvent(iomObjA2)); 
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPICC,"bid2"));
        validator.validate(new ObjectEvent(iomObjB1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0,logger.getErrs().size());
        //assertEquals("No object found with OID o1.", logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void existenceConstraint_refdata_allObjects_Ok(){
        String value1="value1";
        Iom_jObject iomObjA1=new Iom_jObject(ILI_C_CLASSA, "a1");
        iomObjA1.setattrvalue(ILI_C_CLASSA_ATTRA1,value1);
        Iom_jObject iomObjA2=new Iom_jObject(ILI_C_CLASSA, "a2");
        iomObjA2.setattrvalue(ILI_C_CLASSA_ATTRA1,value1+"x");
        Iom_jObject iomObjB1=new Iom_jObject(ILI_C_CLASSB, "b1");
        iomObjB1.setattrvalue(ILI_C_CLASSB_ATTRB1,value1);
        ValidationConfig modelConfig=new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, ValidationConfig.TRUE);
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.addReferenceData(new StartTransferEvent());
        validator.addReferenceData(new StartBasketEvent(ILI_TOPICC,"bid1"));
        validator.addReferenceData(new ObjectEvent(iomObjA1)); 
        validator.addReferenceData(new ObjectEvent(iomObjA2)); 
        validator.addReferenceData(new EndBasketEvent());
        validator.addReferenceData(new EndTransferEvent());
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPICC,"bid2"));
        validator.validate(new ObjectEvent(iomObjB1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0,logger.getErrs().size());
        //assertEquals("No object found with OID o1.", logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void existenceConstraint_refdata_allObjects_Fail(){
        String value1="value1";
        Iom_jObject iomObjA1=new Iom_jObject(ILI_C_CLASSA, "a1");
        iomObjA1.setattrvalue(ILI_C_CLASSA_ATTRA1,value1+"y");
        Iom_jObject iomObjA2=new Iom_jObject(ILI_C_CLASSA, "a2");
        iomObjA2.setattrvalue(ILI_C_CLASSA_ATTRA1,value1+"x");
        Iom_jObject iomObjB1=new Iom_jObject(ILI_C_CLASSB, "b1");
        iomObjB1.setattrvalue(ILI_C_CLASSB_ATTRB1,value1);
        ValidationConfig modelConfig=new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, ValidationConfig.TRUE);
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.addReferenceData(new StartTransferEvent());
        validator.addReferenceData(new StartBasketEvent(ILI_TOPICC,"bid1"));
        validator.addReferenceData(new ObjectEvent(iomObjA1)); 
        validator.addReferenceData(new ObjectEvent(iomObjA2)); 
        validator.addReferenceData(new EndBasketEvent());
        validator.addReferenceData(new EndTransferEvent());
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPICC,"bid2"));
        validator.validate(new ObjectEvent(iomObjB1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1,logger.getErrs().size());
        assertEquals("Existence constraint ReferenceData24.TopicC.ClassB.Constraint1 is violated! The value of the attribute attrB1 of ReferenceData24.TopicC.ClassB was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
    }
    @Test
    public void existenceConstraint_refdata_notAllObjects_Ok(){
        String value1="value1";
        Iom_jObject iomObjA1=new Iom_jObject(ILI_C_CLASSA, "a1");
        iomObjA1.setattrvalue(ILI_C_CLASSA_ATTRA1,value1+"y");
        Iom_jObject iomObjA2=new Iom_jObject(ILI_C_CLASSA, "a2");
        iomObjA2.setattrvalue(ILI_C_CLASSA_ATTRA1,value1+"x");
        Iom_jObject iomObjB1=new Iom_jObject(ILI_C_CLASSB, "b1");
        iomObjB1.setattrvalue(ILI_C_CLASSB_ATTRB1,value1);
        ValidationConfig modelConfig=new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, ValidationConfig.FALSE);
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.addReferenceData(new StartTransferEvent());
        validator.addReferenceData(new StartBasketEvent(ILI_TOPICC,"bid1"));
        validator.addReferenceData(new ObjectEvent(iomObjA1)); 
        validator.addReferenceData(new ObjectEvent(iomObjA2)); 
        validator.addReferenceData(new EndBasketEvent());
        validator.addReferenceData(new EndTransferEvent());
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPICC,"bid2"));
        validator.validate(new ObjectEvent(iomObjB1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(0,logger.getErrs().size());
        //assertEquals("Existence constraint ReferenceData24.TopicC.ClassB.Constraint1 is violated! The value of the attribute attrB1 of ReferenceData24.TopicC.ClassB was not found in the condition class.", logger.getErrs().get(0).getEventMsg());
    }
}