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

public class ReferenceType24Test {
	
	private TransferDescription td=null;
    private final static String MODEL="ReferenceType24";
	// TOPIC
	private final static String TOPIC=MODEL+".Topic";
	// CLASSES
	private final static String ILI_CLASSA=TOPIC+".ClassA";
    private final static String ILI_CLASSB=TOPIC+".ClassB";
	private final static String ILI_CLASSF=TOPIC+".ClassF";
	private static final String ILI_CLASSF_ATTRF1 = "attrF1";
	private final static String ILI_CLASSH=TOPIC+".ClassH";
	private static final String ILI_CLASSH_ATTRH1 = "attrH1";
	
	// OID
	private static final String OID1 = "o1";
	private static final String OID2 = "o2";
	private static final String OID3 = "o3";
	private static final String OID5 = "o5";
	// BID
	private static final String BID1 = "b1";
	private static final String BID2 = "b2";
    
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/ReferenceType24.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	//############################################################
	//################ SUCCESS TESTS #############################
	//############################################################
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn der Referenztype ok ist.
	@Test
	public void referenceType_Ok(){
		String objTargetId=OID1;
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSF, OID2);
		iomObj.addattrobj(ILI_CLASSF_ATTRF1, o1Ref);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new ObjectEvent(iomObjtarget));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
    // Es wird getestet, ob ein Fehler ausgegeben wird, wenn die Referenz External true ist und die Klasse A gefunden wird.
	@Test
	public void external_otherBasketTargetObj_Ok(){
		String objTargetId=OID1;
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSH, OID2);
		iomObj.addattrobj(ILI_CLASSH_ATTRH1, o1Ref);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObjtarget));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID2));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	// Es wird getestet, ob kein Fehler ausgegeben wird, wenn die Referenz External true ist.
	@Test
	public void external_externalTargetObj_Ok(){
		String objTargetId=OID1;
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSH, OID2);
		iomObj.addattrobj(ILI_CLASSH_ATTRH1, o1Ref);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
    // Es wird getestet, ob ein Fehler ausgegeben wird, wenn die Referenz External ist und das Objekt fehlt.
    @Test
    public void external_allObjectsAccessible_externalTargetObj_Fail(){
        String objTargetId=OID1;
        Iom_jObject o1Ref=new Iom_jObject("REF", null);
        o1Ref.setobjectrefoid(objTargetId);
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASSH, OID2);
        iomObj.addattrobj(ILI_CLASSH_ATTRH1, o1Ref);
        ValidationConfig modelConfig=new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, ValidationConfig.TRUE);
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC,BID1));
        validator.validate(new ObjectEvent(iomObj));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1,logger.getErrs().size());
        assertEquals("No object found with OID o1.", logger.getErrs().get(0).getEventMsg());
    }
    
	
	
	
	
	
	//############################################################
	//################ FAIL TESTS ################################
	//############################################################
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn ein Attribute welches mandatory ist, nicht erstellt wurde.
	@Test
	public void mandatoryAttributeUndefined_Fail(){
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSF, OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Attribute attrF1 requires a value", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn die targetclass nicht gefunden werden kann.
	@Test
	public void wrongTargetClass_Fail(){
		String objTargetId=OID1;
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSB, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSF, OID2);
		iomObj.addattrobj(ILI_CLASSF_ATTRF1, o1Ref);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID1));
		validator.validate(new ObjectEvent(iomObj));
		validator.validate(new ObjectEvent(iomObjtarget));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("wrong class ReferenceType24.Topic.ClassB of target object o1 for reference attr ReferenceType24.Topic.ClassF.attrF1.", logger.getErrs().get(0).getEventMsg());
	}
}