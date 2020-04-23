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

public class ReferenceType23Test {
	
	private TransferDescription td=null;
	// TOPIC
	private final static String TOPIC="ReferenceType23.Topic";
	// CLASSES
	private final static String ILI_CLASSA=TOPIC+".ClassA";
	private final static String ILI_CLASSAP=TOPIC+".ClassAp";
	private final static String ILI_CLASSAQ=TOPIC+".ClassAq";
	private final static String ILI_CLASSB=TOPIC+".ClassB";
	private final static String ILI_CLASSD=TOPIC+".ClassD";
	private static final String ILI_CLASSD_ATTRD2 = "attrD2";
	private final static String ILI_CLASSF=TOPIC+".ClassF";
	private static final String ILI_CLASSF_ATTRF2 = "attrF2";
	private final static String ILI_CLASSH=TOPIC+".ClassH";
	private static final String ILI_CLASSH_ATTRH2 = "attrH2";
    private final static String ILI_CLASSJ=TOPIC+".ClassJ";
    private static final String ILI_CLASSJ_ATTRJ2 = "attrJ2";
	// STRUCTS
	private final static String ILI_STRUCTC=TOPIC+".StructC";
	private static final String ILI_STRUCTC_ATTRC2 = "attrC2";
	private static final String ILI_STRUCTC_ATTRC3 = "attrC3";
	private static final String ILI_STRUCTC_ATTRC4 = "attrC4";
	private final static String ILI_STRUCTE=TOPIC+".StructE";
    private final static String ILI_STRUCTE_ATTRE2="attrE2";
	private final static String ILI_STRUCTG=TOPIC+".StructG";
	private static final String ILI_STRUCTG_ATTRG2 = "attrG2";
    private final static String ILI_STRUCTI=TOPIC+".StructI";
    private static final String ILI_STRUCTI_ATTRI2 = "attrI2";
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
		FileEntry fileEntry=new FileEntry("src/test/data/validator/ReferenceType23.ili", FileEntryKind.ILIMODELFILE);
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
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC2, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, OID2);
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
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
	public void referenceTypeBasketExternal_Ok(){
		String objTargetId=OID1;
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTG, null);
		iomStruct.addattrobj(ILI_STRUCTG_ATTRG2, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSH, OID2);
		iomObj.addattrobj(ILI_CLASSH_ATTRH2, iomStruct);
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
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn die Referenz External true ist.
	@Test
	public void referenceTypeFileExternal_Ok(){
		String objTargetId=OID1;
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTG, null);
		iomStruct.addattrobj(ILI_STRUCTG_ATTRG2, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSH, OID2);
		iomObj.addattrobj(ILI_CLASSH_ATTRH2, iomStruct);
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
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn die Referenzierten Klassen nicht erstellt/gefunden wurden.
	@Test
	public void referenceTypeUndefined_Ok(){
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, OID1);
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
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
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn die Referenz nicht External ist und eine weitere Klasse extended.
	@Test
	public void referenceExtendedTarget_Ok(){
		String objTargetId=OID1;
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSAP, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC2, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, OID2);
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
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
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn die Ref Role External false ist und die Extended Klasse gueltig ist.
	@Test
	public void referenceExtendedType_Ok(){
		String objTargetId=OID1;
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSAP, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC3, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, OID2);
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
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
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn in der Role die Restriction in der Struktur eine gueltige Klasse findet.
	@Test
	public void referenceRestrictedType_Ok(){
		String objTargetId=OID1;
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSAP, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC4, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, OID2);
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
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
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn die folgenden Einstellungen gemacht werden:
	// - Das Zielobjekt: classA,o1 befindet sich in der Basket bid2 und wird durch den Resolver gefunden.
	// - External ist true gesetzt.
	// - ExtObjectFound=true;
	// - AllObjectsAccessible ist true gesetzt.
	@Test
	public void allObjectsAccessible_external_TargetObjFound_Ok(){
		String objTargetId=ExternalObjResolverMock.OID1;
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, OID1);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTG, null);
		iomStruct.addattrobj(ILI_STRUCTG_ATTRG2, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSH, OID2);
		iomObj.addattrobj(ILI_CLASSH_ATTRH2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, ValidationConfig.TRUE);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		List<Class> resolverClasses=new ArrayList<Class>();
		resolverClasses.add(ExternalObjResolverMock.class);
		settings.setTransientObject(Validator.CONFIG_OBJECT_RESOLVERS, resolverClasses);
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
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn die folgenden Einstellungen gemacht werden:
	// - Das Zielobjekt: classA,o1 befindet sich in der Basket bid2 und wird durch den Resolver gefunden.
	// - External ist true gesetzt.
	// - ExtObjectFound=true.
	// - AllObjectsAccessible ist false gesetzt.
	@Test
	public void external_TargetObjFound_Ok(){
		String objTargetId=ExternalObjResolverMock.OID1;
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, OID1);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTG, null);
		iomStruct.addattrobj(ILI_STRUCTG_ATTRG2, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSH, OID2);
		iomObj.addattrobj(ILI_CLASSH_ATTRH2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		List<Class> resolverClasses=new ArrayList<Class>();
		resolverClasses.add(ExternalObjResolverMock.class);
		settings.setTransientObject(Validator.CONFIG_OBJECT_RESOLVERS, resolverClasses);
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
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn die folgenden Einstellungen gemacht werden:
	// - Das Zielobjekt: classA,o3 existiert nicht und wird durch den Resolver auch nicht gefunden.
	// - External ist true gesetzt.
	// - ExtObjectFound=false.
	// - AllObjectsAccessible ist false gesetzt.
	@Test
	public void external_ExtObjResolver_TargetObjNotFound_Ok(){
		String objTargetId=OID1;
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, OID3);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTG, null);
		iomStruct.addattrobj(ILI_STRUCTG_ATTRG2, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSH, OID2);
		iomObj.addattrobj(ILI_CLASSH_ATTRH2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		List<Class> resolverClasses=new ArrayList<Class>();
		resolverClasses.add(ExternalObjResolverMock.class);
		settings.setTransientObject(Validator.CONFIG_OBJECT_RESOLVERS, resolverClasses);
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
	
	//############################################################
	//################ FAIL TESTS ################################
	//############################################################
	
	// - Das Zielobjekt: classA,o1 existiert nicht
	// - AllObjectsAccessible ist true.
	@Test
	public void allObjectsAccessible_external_ExtObjResolver_TargetObjNotFound_Fail(){
		String objTargetId=OID1;
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, OID3);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTG, null);
		iomStruct.addattrobj(ILI_STRUCTG_ATTRG2, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSH, OID2);
		iomObj.addattrobj(ILI_CLASSH_ATTRH2, iomStruct);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, ValidationConfig.TRUE);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		List<Class> resolverClasses=new ArrayList<Class>();
		resolverClasses.add(ExternalObjResolverMock.class);
		settings.setTransientObject(Validator.CONFIG_OBJECT_RESOLVERS, resolverClasses);
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
		assertEquals(1,logger.getErrs().size());
		assertEquals("No object found with OID o1.", logger.getErrs().get(0).getEventMsg());
	}
    // - keine Referenz auf Zielobjekt vorhanden
    // - AllObjectsAccessible ist true.
    @Test
    public void allObjectsAccessible_external_ExtObjResolver_optionalRefAttr_noref_Ok(){
        Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, OID3);
        Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTI, null);
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASSJ, OID2);
        iomObj.addattrobj(ILI_CLASSJ_ATTRJ2, iomStruct);
        ValidationConfig modelConfig=new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, ValidationConfig.TRUE);
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        //List<Class> resolverClasses=new ArrayList<Class>();
        //resolverClasses.add(ExternalObjResolverMock.class);
        //settings.setTransientObject(Validator.CONFIG_OBJECT_RESOLVERS, resolverClasses);
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
    @Test
    public void allObjectsAccessible_external_optionalRefAttr_noref_Ok(){
        Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, OID3);
        Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTI, null);
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASSJ, OID2);
        iomObj.addattrobj(ILI_CLASSJ_ATTRJ2, iomStruct);
        ValidationConfig modelConfig=new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, ValidationConfig.TRUE);
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        List<Class> resolverClasses=new ArrayList<Class>();
        resolverClasses.add(ExternalObjResolverMock.class);
        settings.setTransientObject(Validator.CONFIG_OBJECT_RESOLVERS, resolverClasses);
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
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn ein Attribute welches mandatory ist, nicht erstellt wurde.
	@Test
	public void mandatoryAttributeUndefined_Fail(){
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTE, null);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSF, OID1);
		iomObj.addattrobj(ILI_CLASSF_ATTRF2, iomStruct);
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
		assertEquals("Attribute attrF2[0]/attrE2 requires a value", logger.getErrs().get(0).getEventMsg());
	}
    @Test
    public void mandatoryAttributeUndefinedMissingRef_Fail(){
        Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTE, null);
        iomStruct.addattrobj(ILI_STRUCTE_ATTRE2, "REF");
        Iom_jObject iomObj=new Iom_jObject(ILI_CLASSF, OID1);
        iomObj.addattrobj(ILI_CLASSF_ATTRF2, iomStruct);
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
        assertEquals("Attribute attrF2[0]/attrE2 requires a value", logger.getErrs().get(0).getEventMsg());
    }
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn die Rolle nicht External ist und die Klasse sich in einer anderen Bid befindet.
	@Test
	public void differentBasket_Fail(){
		String objTargetId=OID1;
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC2, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, OID2);
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
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
		assertTrue(logger.getErrs().size()==1);
		assertEquals("No object found with OID o1 in basket b2.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn das Referenzierte Objekt nicht gefunden werden kann.
	@Test
	public void attrReferencesToInexistentObject_Fail(){
		String objTargetId=OID1;
		String objTargetId2=OID5;
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, objTargetId2);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC2, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, OID2);
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
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
		assertEquals("No object found with OID o1.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn die targetclass nicht gefunden werden kann.
	@Test
	public void wrongTargetClass_Fail(){
		String objTargetId=OID1;
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSB, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC2, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, OID2);
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
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
		assertEquals("wrong class ReferenceType23.Topic.ClassB of target object o1 for reference attr ReferenceType23.Topic.StructC.attrC2.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn die Klasse welche durch eine andere Klasse extended wird, nicht gefunden wird.
	@Test
	public void referenceExtendedTypeBaseTarget_Fail(){
		String objTargetId=OID1;
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC3, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, OID2);
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
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
		assertEquals("wrong class ReferenceType23.Topic.ClassA of target object o1 for reference attr ReferenceType23.Topic.StructC.attrC3.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn die Beschraenkung einer Klasse nicht die Ziel Klasse ist.
	@Test
	public void referenceRestrictedTypeBaseTarget_Fail(){
		String objTargetId=OID1;
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSA, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC4, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, OID2);
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
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
		assertEquals("wrong class ReferenceType23.Topic.ClassA of target object o1 for reference attr ReferenceType23.Topic.StructC.attrC4.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet, ob ein Fehler ausgegeben wird, wenn eine ungueltige Zielklasse sich in der Referenz befindet.
	@Test
	public void referenceRestrictedTypeWrongExtension_Fail(){
		String objTargetId=OID1;
		Iom_jObject iomObjtarget=new Iom_jObject(ILI_CLASSAQ, objTargetId);
		Iom_jObject o1Ref=new Iom_jObject("REF", null);
		o1Ref.setobjectrefoid(objTargetId);
		Iom_jObject iomStruct=new Iom_jObject(ILI_STRUCTC, null);
		iomStruct.addattrobj(ILI_STRUCTC_ATTRC4, o1Ref);
		Iom_jObject iomObj=new Iom_jObject(ILI_CLASSD, OID2);
		iomObj.addattrobj(ILI_CLASSD_ATTRD2, iomStruct);
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
		assertEquals("wrong class ReferenceType23.Topic.ClassAq of target object o1 for reference attr ReferenceType23.Topic.StructC.attrC4.", logger.getErrs().get(0).getEventMsg());
	}
}