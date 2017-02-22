package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;
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

// EMBEDDED ASSOCIATION (Eingebettete Verbindung)
// bsp.
// a1 -- {0..1} ClassA;
// a1 (Rollenname)
// {} (Beinhaltet die Kardinalität)
// 0 (Die Minimal gesetzte Zulässige Kardinalität.
// 1 (Die Maximal gesetzte Zulässige Kardinalität.
// ClassA (Die Klasse, welche durch die Rolle a1, minimal 0 und maximal 1 Mal von einer anderen Klasse referenziert werden darf.
// Die Beziehung einer beliebigen Klasse: Klasse_A kann über den Rollenaufruf_a1 von der Klasse_B minimal_0 Mal, bis und mit maximal_1 Mal bestehen, sonst wird ein Fehler ausgegeben.
//
// STAND ALONE ASSOCIATION (Eigenständige Verbindung) Besteht immer aus 2 Rollen.
// bsp.
// a1 -- {0..1} ClassA;
// b1 -- {0..1} ClassB;
// In diesem Falle müssen die Klassen ClassA und ClassB Verbindungen zueinander erstellen.
// Dabei kann die Beziehung der Klasse_A über den Rollennamen: a1, minimal_0 Mal und maximal_1 Mal bestehen.
// Die Beziehung der Klasse_B über den Rollennamen: b1 kann somit minimal_0 Mal und maximal_1 Mal bestehen.

public class Association23Test {

	private TransferDescription td=null;
	// OID
	private final static String OBJ_OID1 ="o1";
	private final static String OBJ_OID2 ="o2";
	private final static String OBJ_OID3 ="o3";
	private final static String OBJ_OID4 ="o4";
	private final static String OBJ_OID5 ="o5";
	private final static String OBJ_OID6 ="o6";
	private final static String OBJ_OID7 ="o7";
	// MODEL.TOPIC
	private final static String ILI_TOPIC="Association23.Topic";
	private final static String ILI_TOPICB="Association23.TopicB";
	// CLASS
	private final static String ILI_CLASSA=ILI_TOPIC+".ClassA";
	private final static String ILI_CLASSB=ILI_TOPIC+".ClassB";
	private final static String ILI_CLASSD=ILI_TOPIC+".ClassD";
	private final static String ILI_CLASSE=ILI_TOPIC+".ClassE";
	private final static String ILI_CLASSF=ILI_TOPIC+".ClassF";
	private final static String ILI_CLASSG=ILI_TOPIC+".ClassG";
	private final static String ILI_CLASSH=ILI_TOPIC+".ClassH";
	private final static String ILI_CLASSI=ILI_TOPIC+".ClassI";
	private final static String ILI_CLASSJ=ILI_TOPIC+".ClassJ";
	private final static String ILI_TOPICB_CLASSE=ILI_TOPICB+".ClassE";
	private final static String ILI_TOPICB_CLASSF=ILI_TOPICB+".ClassF";
	private final static String ILI_TOPICB_CLASSG=ILI_TOPICB+".ClassG";
	private final static String ILI_TOPICB_CLASSH=ILI_TOPICB+".ClassH";
	// CLASS EXTEND
	private final static String ILI_CLASSAP=ILI_TOPIC+".ClassAp";
	private final static String ILI_CLASSBP=ILI_TOPIC+".ClassBp";
	private final static String ILI_CLASSCP=ILI_TOPIC+".ClassCp";
	// ASSOCIATION
	private final static String ILI_ASSOC_AB1_A1="a1";
	private final static String ILI_ASSOC_AB1_B1="b1";
	
	private final static String ILI_ASSOC_AB2_A2="a2";
	private final static String ILI_ASSOC_AB2_B2="b2";
	
	private final static String ILI_ASSOC_AB3_A3="a3";
	private final static String ILI_ASSOC_AB3_B3="b3";
	
	private final static String ILI_ASSOC_AB4_A4="a4";
	private final static String ILI_ASSOC_AB4_B4="b4";
	
	private final static String ILI_ASSOC_ABP1_AP1="ap1";
	private final static String ILI_ASSOC_ABP1_BP1="bp1";
	
	private final static String ILI_ASSOC_ABP2_AP2="ap2";
	private final static String ILI_ASSOC_ABP2_BP2="bp2";
	
	private final static String ILI_ASSOC_ABP3_BP3="bp3";
	private final static String ILI_ASSOC_ABP3_AP3="ap3";
	
	private final static String ILI_ASSOC_ABD1_AD1="ad1";
	private final static String ILI_ASSOC_ABD1_BD1="bd1";
	
	private final static String ILI_ASSOC_ABD2_AD2="ad2";
	private final static String ILI_ASSOC_ABD2_BD2="bd2";
	
	private final static String ILI_ASSOC_ABD3_AD3="ad3";
	private final static String ILI_ASSOC_ABD3_BD3="bd3";
	
	private final static String ILI_ASSOC_EF1_E1="e1";
	private final static String ILI_ASSOC_EF1_F1="f1";
	
	// ASSOCIATION CLASS
	private final static String ILI_ASSOC_AB2=ILI_TOPIC+".ab2";
	private final static String ILI_ASSOC_ABP2=ILI_TOPIC+".abp2";
	private final static String ILI_ASSOC_ABD2=ILI_TOPIC+".abd2";
	private final static String ILI_ASSOC_EF1=ILI_TOPIC+".ef1";
	private final static String ILI_TOPICB_ASSOC_EF1=ILI_TOPICB+".ef1";
	private final static String ILI_TOPICB_ASSOC_EF1_E1="e1";
	private final static String ILI_TOPICB_ASSOC_EF1_F1="f1";
	
	// START BASKET EVENT
	private final static String BASKET_ID1="b1";
	private final static String BASKET_ID2="b2";
	
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/validator/Association23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	//#########################################################//
	//########### SUCCESS EMBEDDED CARDINALITY ################//
	//#########################################################//

	// Wenn von der KlasseB eine Beziehung zur KlasseA über den Rollennamen: a1, 1 Mal besteht soll keine Fehlermeldung ausgegeben werden. 
	@Test
	public void aEmbeddedAssociation_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_AB1_A1, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Wenn von der KlasseB, welche über KlasseBP eine Beziehung zur KlasseA über die KlasseAP über den Rollennamen: a1,
	// 1 Mal besteht soll keine Fehlermeldung ausgegeben werden.
	@Test
	public void aEmbeddedAssociationOverRestriction_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSAP, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSBP, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_ABP1_AP1, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}	
	
	// Wenn von der KlasseB eine Beziehung zur KlasseA über den Rollennamen: ad1,
	// Von der KlasseB eine Beziehung zur KlasseD über den Rollennamen: ad1,
	// je, 0-1 Mal besteht und eine davon richtig ist, soll keine Fehlermeldung ausgegeben werden.
	@Test
	public void embeddedAssociationWithOR_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSD, OBJ_OID3);
		iomObjB.addattrobj(ILI_ASSOC_ABD1_AD1, "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjB2=new Iom_jObject(ILI_CLASSB, OBJ_OID4);
		iomObjB2.addattrobj(ILI_ASSOC_ABD1_AD1, "REF").setobjectrefoid(OBJ_OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjB2));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Wenn von der KlasseH eine Beziehung zur KlasseG über den Rollennamen: g1 (Welcher EXTERNAL true ist),
	// 1 Mal besteht soll. Die Klasse jedoch in unterschiedlichen Baskets sich befinden.
	// keine Fehlermeldung ausgegeben werden.
	@Test
	public void embedeedExternalRoleDifferentBaskets_Ok(){
		Iom_jObject iomObjG=new Iom_jObject(ILI_TOPICB_CLASSG, OBJ_OID1);
		Iom_jObject iomObjH1=new Iom_jObject(ILI_TOPICB_CLASSH, OBJ_OID2);
		iomObjH1.addattrobj("g1", "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPICB,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjG));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent(ILI_TOPICB,BASKET_ID2));
		validator.validate(new ObjectEvent(iomObjH1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}

	// Wenn in einer Stand Alone Association von der KlasseF eine Beziehung zur KlasseE über den Rollennamen: e1,
	// Von der KlasseE eine Beziehung zur KlasseF über den Rollennamen: f1,
	// je, 1 Mal besteht soll eine Fehlermeldung ausgegeben werden,
	// wenn die Objekte sich in unterschiedlichen Baskets befinden und External false ist.
	@Test
	public void standAloneExternalFalse_False(){ //FIXME Sollte False ergeben.
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSE, OBJ_OID1);
		Iom_jObject iomObjF=new Iom_jObject(ILI_CLASSF, OBJ_OID2);
		Iom_jObject iomLinkEF=new Iom_jObject(ILI_ASSOC_EF1, null);
		iomLinkEF.addattrobj(ILI_ASSOC_EF1_E1, "REF").setobjectrefoid(OBJ_OID1);
		iomLinkEF.addattrobj(ILI_ASSOC_EF1_F1, "REF").setobjectrefoid(OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID2));
		validator.validate(new ObjectEvent(iomObjF));
		validator.validate(new ObjectEvent(iomLinkEF));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	// Wenn in einer Stand Alone Association von der KlasseB eine Beziehung zur KlasseA über den Rollennamen: a1,
	// Von der Klasse A eine Beziehung zur KlasseB über den Rollennamen: b1,
	// je, 1 Mal besteht soll keine Fehlermeldung ausgegeben werden.
	@Test
	public void standAlone0toN_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		Iom_jObject iomObjAB=new Iom_jObject(ILI_ASSOC_AB2, null);
		iomObjAB.addattrobj(ILI_ASSOC_AB2_A2, "REF").setobjectrefoid(OBJ_OID1);
		iomObjAB.addattrobj(ILI_ASSOC_AB2_B2, "REF").setobjectrefoid(OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjAB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Wenn in einer Stand Alone Association von der KlasseF eine Beziehung zur KlasseE über den Rollennamen: e1,
	// Von der KlasseE eine Beziehung zur KlasseF über den Rollennamen: f1,
	// je, 1 Mal besteht soll keine Fehlermeldung ausgegeben werden.
	@Test
	public void standAlone1toN_Ok(){
		Iom_jObject iomObjE=new Iom_jObject(ILI_CLASSE, OBJ_OID1);
		Iom_jObject iomObjF=new Iom_jObject(ILI_CLASSF, OBJ_OID2);
		Iom_jObject iomLinkEF=new Iom_jObject(ILI_ASSOC_EF1, null);
		iomLinkEF.addattrobj(ILI_ASSOC_EF1_E1, "REF").setobjectrefoid(OBJ_OID1);
		iomLinkEF.addattrobj(ILI_ASSOC_EF1_F1, "REF").setobjectrefoid(OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new ObjectEvent(iomObjF));
		validator.validate(new ObjectEvent(iomLinkEF));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	// Wenn in einer Stand Alone Association von der KlasseF eine Beziehung zur KlasseE über den Rollennamen: e1,
	// Von der KlasseE eine Beziehung zur KlasseF über den Rollennamen: f1,
	// je, 1 Mal besteht soll keine Fehlermeldung ausgegeben werden,
	// wenn die Objekte sich in der gleichen Basket befinden und External true ist.	
	@Test
	public void standAloneExternalTrueSameBasket_Ok(){
		Iom_jObject iomObjE=new Iom_jObject(ILI_TOPICB_CLASSE, OBJ_OID1);
		Iom_jObject iomObjF=new Iom_jObject(ILI_TOPICB_CLASSF, OBJ_OID2);
		Iom_jObject iomLinkEF=new Iom_jObject(ILI_TOPICB_ASSOC_EF1, null);
		iomLinkEF.addattrobj(ILI_TOPICB_ASSOC_EF1_E1, "REF").setobjectrefoid(OBJ_OID1);
		iomLinkEF.addattrobj(ILI_TOPICB_ASSOC_EF1_F1, "REF").setobjectrefoid(OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPICB,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new ObjectEvent(iomObjF));
		validator.validate(new ObjectEvent(iomLinkEF));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	// Wenn in einer Embedded Association von der KlasseH eine Beziehung zur KlasseG über den Rollennamen: g1,
	// 1 Mal besteht soll eine Fehlermeldung ausgegeben werden,
	// wenn die Objekte sich in unterschiedlichen Baskets befinden und External false ist.
	@Test
	public void embeddedExternalFalseDiffBasket_False(){ //FIXME Soll false sein.
		Iom_jObject iomObjG=new Iom_jObject(ILI_CLASSG, OBJ_OID1);
		Iom_jObject iomObjH1=new Iom_jObject(ILI_CLASSH, OBJ_OID2);
		iomObjH1.addattrobj("g1", "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjG));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID2));
		validator.validate(new ObjectEvent(iomObjH1));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	// Wenn in einer Stand Alone Association von der KlasseF eine Beziehung zur KlasseE über den Rollennamen: e1,
	// Von der KlasseE eine Beziehung zur KlasseF über den Rollennamen: f1,
	// je, 1 Mal besteht soll keine Fehlermeldung ausgegeben werden,
	// wenn die Objekte sich in unterschiedlichen Baskets befinden und External true ist.
	@Test
	public void standAloneExternalTrueDiffBasket_Ok(){
		Iom_jObject iomObjE=new Iom_jObject(ILI_TOPICB_CLASSE, OBJ_OID1);
		Iom_jObject iomObjF=new Iom_jObject(ILI_TOPICB_CLASSF, OBJ_OID2);
		Iom_jObject iomLinkEF=new Iom_jObject(ILI_TOPICB_ASSOC_EF1, null);
		iomLinkEF.addattrobj(ILI_TOPICB_ASSOC_EF1_E1, "REF").setobjectrefoid(OBJ_OID1);
		iomLinkEF.addattrobj(ILI_TOPICB_ASSOC_EF1_F1, "REF").setobjectrefoid(OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPICB,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjE));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent(ILI_TOPICB,BASKET_ID2));
		validator.validate(new ObjectEvent(iomObjF));
		validator.validate(new ObjectEvent(iomLinkEF));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	// Wenn in einer Stand Alone Association von der KlasseF eine Beziehung zur KlasseE über den Rollennamen: e1,
	// Von der KlasseE eine Beziehung zur KlasseF über den Rollennamen: f1,
	// je, 1 Mal besteht soll keine Fehlermeldung ausgegeben werden,
	// wenn die Objekte sich im gleichen Basket befindet und External true ist.
	@Test
	public void standAloneExternalTrueSameBasketNtoN_Ok(){
		Iom_jObject iomObjF=new Iom_jObject(ILI_TOPICB_CLASSF, OBJ_OID2);
		Iom_jObject iomLinkEF=new Iom_jObject(ILI_TOPICB_ASSOC_EF1, null);
		iomLinkEF.addattrobj(ILI_TOPICB_ASSOC_EF1_E1, "REF").setobjectrefoid(OBJ_OID1);
		iomLinkEF.addattrobj(ILI_TOPICB_ASSOC_EF1_F1, "REF").setobjectrefoid(OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPICB,BASKET_ID2));
		validator.validate(new ObjectEvent(iomObjF));
		validator.validate(new ObjectEvent(iomLinkEF));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertEquals(0,logger.getErrs().size());
	}
	
	// Wenn von einer Stand Alone Association der KlasseB, welche über KlasseBP eine Beziehung zur KlasseA über die KlasseAP über den Rollennamen: a1,
	// von der KlasseA, welche über KlasseAP eine Beziehung zur KlasseB über die KlasseBP über den Rollennamen: b1,
	// je, 1 Mal besteht soll keine Fehlermeldung ausgegeben werden. Gleicher Basket, External false.
	@Test
	public void standAloneRestriction_Ok(){	
		Iom_jObject iomObjAp=new Iom_jObject(ILI_CLASSAP, OBJ_OID1);
		Iom_jObject iomObjBp=new Iom_jObject(ILI_CLASSBP, OBJ_OID2);
		Iom_jObject iomObjABP=new Iom_jObject(ILI_ASSOC_ABP2, null);
		iomObjABP.addattrobj(ILI_ASSOC_ABP2_AP2, "REF").setobjectrefoid(OBJ_OID1);
		iomObjABP.addattrobj(ILI_ASSOC_ABP2_BP2, "REF").setobjectrefoid(OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjAp));
		validator.validate(new ObjectEvent(iomObjBp));
		validator.validate(new ObjectEvent(iomObjABP));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Wenn von einer Stand Alone Association von der KlasseB eine Beziehung zur KlasseA über den Rollennamen: ad2,
	// Von der KlasseB eine Beziehung zur KlasseD über den Rollennamen: ad2,
	// je, 1 Mal besteht und eine davon richtig ist, soll keine Fehlermeldung ausgegeben werden.
	@Test
	public void standAloneWithOR_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		Iom_jObject iomObjAB=new Iom_jObject(ILI_ASSOC_ABD2, null);
		iomObjAB.addattrobj(ILI_ASSOC_ABD2_AD2, "REF").setobjectrefoid(OBJ_OID1);
		iomObjAB.addattrobj(ILI_ASSOC_ABD2_BD2, "REF").setobjectrefoid(OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjAB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	//#########################################################//
	//############# FAIL EMBEDDED CARDINALITY #################//
	//#########################################################//		
	
	// Wenn von der KlasseB eine Beziehung zur KlasseA über den Rollennamen: a1, 0 bis 1 Mal besteht soll keine Fehlermeldung ausgegeben werden. 
	@Test
	public void embeddedAssociationCardinality_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB1=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB1.addattrobj(ILI_ASSOC_AB1_A1, "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjB2=new Iom_jObject(ILI_CLASSB, OBJ_OID3);
		iomObjB2.addattrobj(ILI_ASSOC_AB1_A1, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB1));
		validator.validate(new ObjectEvent(iomObjB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("b1 should associate 0 to 1 target objects (instead of 2)", logger.getErrs().get(0).getEventMsg());
	}
	
	// Wenn von der KlasseB eine Beziehung zur KlasseA über den Rollennamen: a3, 0 bis 1 Mal besteht soll keine Fehlermeldung ausgegeben werden. 
	@Test
	public void embeddedAssociationCardinality2_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB1=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB1.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjB2=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB2.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB1));
		validator.validate(new ObjectEvent(iomObjB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("The OID o2 of object 'Association23.Topic.ClassB oid o2 {a3 -> o1 REF {}}' already exists in CLASS Association23.Topic.ClassB.", logger.getErrs().get(0).getEventMsg());
		assertEquals("a3 should associate 0 to 1 target objects (instead of 2)", logger.getErrs().get(1).getEventMsg());
	}
	
	// Wenn von der KlasseB, welche über KlasseBP eine Beziehung zur KlasseA über die KlasseAP über den Rollennamen: a1,
	// 1 Mal besteht soll keine Fehlermeldung ausgegeben werden.
	@Test
	public void embeddedAssociationCardinalityExtendedClass_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSAP, OBJ_OID1);
		Iom_jObject iomObjB1=new Iom_jObject(ILI_CLASSBP, OBJ_OID2);
		iomObjB1.addattrobj(ILI_ASSOC_ABP1_AP1, "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjB2=new Iom_jObject(ILI_CLASSBP, OBJ_OID3);
		iomObjB2.addattrobj(ILI_ASSOC_ABP1_AP1, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB1));
		validator.validate(new ObjectEvent(iomObjB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("bp1 should associate 0 to 1 target objects (instead of 2)", logger.getErrs().get(0).getEventMsg());
	}
	
	// Wenn von der KlasseB, welche über KlasseBP eine Beziehung zur KlasseA über die KlasseAP über den Rollennamen: a1,
	// 1 Mal besteht soll keine Fehlermeldung ausgegeben werden.
	@Test
	public void embeddedAssociationCardinalityExtendedClass2_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSAP, OBJ_OID1);
		Iom_jObject iomObjB1=new Iom_jObject(ILI_CLASSBP, OBJ_OID2);
		iomObjB1.addattrobj(ILI_ASSOC_ABP3_AP3, "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjB2=new Iom_jObject(ILI_CLASSBP, OBJ_OID2);
		iomObjB2.addattrobj(ILI_ASSOC_ABP3_AP3, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB1));
		validator.validate(new ObjectEvent(iomObjB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("The OID o2 of object 'Association23.Topic.ClassBp oid o2 {ap3 -> o1 REF {}}' already exists in CLASS Association23.Topic.ClassBp.", logger.getErrs().get(0).getEventMsg());
		assertEquals("ap3 should associate 0 to 1 target objects (instead of 2)", logger.getErrs().get(1).getEventMsg());
	}
	
	// Wenn von der KlasseB eine Beziehung zur KlasseA über den Rollennamen: ad1,
	// Von der KlasseB eine Beziehung zur KlasseD über den Rollennamen: ad1,
	// je, 0-1 Mal besteht und eine davon richtig ist, soll keine Fehlermeldung ausgegeben werden.
	@Test
	public void embeddedAssociationWithOR_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSD, OBJ_OID4);
		Iom_jObject iomObjB2=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB2.addattrobj(ILI_ASSOC_ABD1_AD1, "REF").setobjectrefoid(OBJ_OID1);
		iomObjB2.addattrobj(ILI_ASSOC_ABD1_AD1, "REF").setobjectrefoid(OBJ_OID4);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("ad1 should associate 0 to 1 target objects (instead of 2)", logger.getErrs().get(0).getEventMsg());
	}
	
	// Wenn von der KlasseB eine Beziehung zur KlasseA über den Rollennamen: ad3,
	// Von der KlasseB eine Beziehung zur KlasseD über den Rollennamen: ad3,
	// je, 0-1 Mal besteht und eine davon richtig ist, soll keine Fehlermeldung ausgegeben werden.
	@Test
	public void embeddedAssociationWithOR2_Fail(){
		Iom_jObject iomObjA1=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		// 1. reference success (0..1 == 1)
		iomObjB.addattrobj(ILI_ASSOC_ABD3_AD3, "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjB2=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		// 2. reference fail (0..1 == 2)
		iomObjB2.addattrobj(ILI_ASSOC_ABD3_AD3, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA1));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("The OID o2 of object 'Association23.Topic.ClassB oid o2 {ad3 -> o1 REF {}}' already exists in CLASS Association23.Topic.ClassB.", logger.getErrs().get(0).getEventMsg());
		assertEquals("ad3 should associate 0 to 1 target objects (instead of 2)", logger.getErrs().get(1).getEventMsg());
	}
		
	//#########################################################//
	//################# FAIL TARGETCLASS TEST #################//
	//#########################################################//	
	
	// Wenn von der KlasseB eine Beziehung zur KlasseA über den Rollennamen: a1, 0 bis 1 Mal besteht soll keine Fehlermeldung ausgegeben werden. 
	@Test
	public void wrongTargetClass_Fail(){
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSD, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_AB1_A1, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Object Association23.Topic.ClassD with OID o1 must be of Association23.Topic.ClassA", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird eine Fehlermeldung ausgegeben wenn in einer StandAlone Association eine falsche Kardinalität von e1 erstellt wurde.
	@Test
	public void standAloneAssociationWrongCardinality_Fail(){
		Iom_jObject iomObjF=new Iom_jObject(ILI_CLASSF, OBJ_OID2);
		Iom_jObject iomObjAp=new Iom_jObject(ILI_ASSOC_EF1, null);
		iomObjAp.addattrobj(ILI_ASSOC_EF1_F1, "REF").setobjectrefoid(OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjF));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("e1 should associate 1 to * target objects (instead of 0)", logger.getErrs().get(0).getEventMsg());
	}
	
	// Die OID a1 der Klasse A, welche von der Klasse B mit der Rolle b1 Verbunden wird, existiert nicht.
	@Test
	public void noTargetObject_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("No object found with OID o3.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Die KlasseA mit der OID a1, verbindet über die Klasse: classBp mit der OID b1.
	// Es soll eine Fehlermeldung ausgegeben werden, wenn diese Klasse nicht existiert.
	@Test
	public void wrongExtendedClass_Fail(){
		Iom_jObject iomObjC=new Iom_jObject(ILI_CLASSCP, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSBP, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_ABP1_AP1, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjC));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("Object Association23.Topic.ClassCp with OID o1 must be of Association23.Topic.ClassA", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es wird getestet ob eine Fehlermeldung ausgegeben wird, wenn die Klasse der referenzierten oid: oid3, der Association abp3, nicht exisitiert.
	@Test
	public void noTargetObjectOfExtendedClassFound_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSAP, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSBP, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_ABP3_AP3, "REF").setobjectrefoid(OBJ_OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("No object found with OID o3.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Die Klasse der von der Association referenzierten oid2, existiert nicht.
	@Test
	public void noTargetObjectOfExtendedClassFound2_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSAP, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSBP, OBJ_OID2);
		Iom_jObject iomObjAp=new Iom_jObject(ILI_ASSOC_ABP2, null);
		iomObjAp.addattrobj(ILI_ASSOC_ABP2_AP2, "REF").setobjectrefoid(OBJ_OID1);
		iomObjAp.addattrobj(ILI_ASSOC_ABP2_BP2, "REF").setobjectrefoid(OBJ_OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new ObjectEvent(iomObjAp));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("No object found with OID o3.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die Multiplizität der Rolle Maximal 1 sein darf, jedoch 5 Objekte erstellt wurden.
	@Test
	public void objectRangeOfMultiplicityExceeded_Fail(){
		Iom_jObject iomObjG=new Iom_jObject(ILI_CLASSG, OBJ_OID1);
		Iom_jObject iomObjH1=new Iom_jObject(ILI_CLASSH, OBJ_OID2);
		iomObjH1.addattrobj("g1", "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjH2=new Iom_jObject(ILI_CLASSH, OBJ_OID3);
		iomObjH2.addattrobj("g1", "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjH3=new Iom_jObject(ILI_CLASSH, OBJ_OID4);
		iomObjH3.addattrobj("g1", "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjH4=new Iom_jObject(ILI_CLASSH, OBJ_OID5);
		iomObjH4.addattrobj("g1", "REF").setobjectrefoid(OBJ_OID1);
		Iom_jObject iomObjH5=new Iom_jObject(ILI_CLASSH, OBJ_OID6);
		iomObjH5.addattrobj("g1", "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjG));
		validator.validate(new ObjectEvent(iomObjH1));
		validator.validate(new ObjectEvent(iomObjH2));
		validator.validate(new ObjectEvent(iomObjH3));
		validator.validate(new ObjectEvent(iomObjH4));
		validator.validate(new ObjectEvent(iomObjH5));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("h1 should associate 1 to 1 target objects (instead of 5)", logger.getErrs().get(0).getEventMsg());
	}
	
	//#########################################################//
	//################# CONFIG ON/OFF TEST ####################//
	//#########################################################//
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn Multiplicity eingeschalten ist und die Referenz einen Fehler ergibt.
	@Test
	public void configMultiplicityONAssociationFail_Fail(){
		Iom_jObject iomObjI=new Iom_jObject(ILI_CLASSI, OBJ_OID1);
		Iom_jObject iomObjJ=new Iom_jObject(ILI_CLASSJ, OBJ_OID2);
		iomObjJ.addattrobj(ILI_ASSOC_AB4_A4, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjI));
		validator.validate(new ObjectEvent(iomObjJ));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==2);
		assertEquals("unknown property <a4>", logger.getErrs().get(0).getEventMsg());
		assertEquals("a4 should associate 1 to 5 target objects (instead of 0)", logger.getErrs().get(1).getEventMsg());
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die Konfiguration Target Objects eingeschalten ist und das Target Object nicht gefunden werden kann.
	@Test
	public void configTargetONNoTargetObjectFound_Fail(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("No object found with OID o3.", logger.getErrs().get(0).getEventMsg());
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die Konfiguration Multiplicity ausgeschalten wird und die Anzahl die referenzierten Objekte nicht stimmt. 
	@Test
	public void configMultiplicityOFFObjectCountWrong_Ok(){
		Iom_jObject iomObjI=new Iom_jObject(ILI_CLASSI, OBJ_OID1);
		Iom_jObject iomObjJ=new Iom_jObject(ILI_CLASSJ, OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Association23.Topic.ab4.a4", ValidationConfig.MULTIPLICITY,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjI));
		validator.validate(new ObjectEvent(iomObjJ));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die Konfiguration Target Objects ausgeschalten worden ist und keine referenzierten Objekte gefunden worden sind.
	@Test
	public void configTargetOFFNoTargetObjectFound_Ok(){
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID1);
		Iom_jObject iomObjB=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
		iomObjB.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID3);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Association23.Topic.ab3.a3", ValidationConfig.TARGET,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die Multiplicity und die TargetObjects Konfiguration ausgeschalten worden ist und
	// es einen Fehler in der Objektmenge und der Referenz Objekte gibt.
	@Test
	public void configTargetAndMultiplicityOFFEmbeddedFail_Ok(){
		Iom_jObject iomObjD=new Iom_jObject(ILI_CLASSD, OBJ_OID1);
		Iom_jObject iomObjA=new Iom_jObject(ILI_CLASSA, OBJ_OID2);
		Iom_jObject iomObjB2=new Iom_jObject(ILI_CLASSB, OBJ_OID3);
		iomObjB2.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID2);
		Iom_jObject iomObjB3=new Iom_jObject(ILI_CLASSB, OBJ_OID5);
		iomObjB3.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID6);
		Iom_jObject iomObjB4=new Iom_jObject(ILI_CLASSB, OBJ_OID7);
		iomObjB4.addattrobj(ILI_ASSOC_AB3_A3, "REF").setobjectrefoid(OBJ_OID1);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Association23.Topic.ab3.a3", ValidationConfig.MULTIPLICITY,ValidationConfig.OFF);
		modelConfig.setConfigValue("Association23.Topic.ab3.a3", ValidationConfig.TARGET,ValidationConfig.OFF);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjD));
		validator.validate(new ObjectEvent(iomObjA));
		validator.validate(new ObjectEvent(iomObjB2));
		validator.validate(new ObjectEvent(iomObjB3));
		validator.validate(new ObjectEvent(iomObjB4));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
	// Es soll getestet werden, ob eine Fehlermeldung ausgegeben wird, wenn die Multiplicity auf Warning gestellt wird und das referenzierte Objekt nicht gefunden werden kann.
	@Test
	public void configMultiplicityWarningONFail_Fail(){
		Iom_jObject iomObjI=new Iom_jObject(ILI_CLASSI, OBJ_OID1);
		Iom_jObject iomObjJ=new Iom_jObject(ILI_CLASSJ, OBJ_OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		modelConfig.setConfigValue("Association23.Topic.ab4.a4", ValidationConfig.MULTIPLICITY,ValidationConfig.WARNING);
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(ILI_TOPIC,BASKET_ID1));
		validator.validate(new ObjectEvent(iomObjI));
		validator.validate(new ObjectEvent(iomObjJ));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getWarn().size()==1);
		assertTrue(logger.getErrs().size()==0);
		assertEquals("a4 should associate 1 to 5 target objects (instead of 0)", logger.getWarn().get(0).getEventMsg());
	}
}