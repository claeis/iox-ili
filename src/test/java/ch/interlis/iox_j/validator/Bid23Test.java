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
import ch.interlis.iox.IoxValidationDataPool;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;

public class Bid23Test {
	// OID
	private final static String OID1="o1";
	private final static String OID2="o2";
	private final static String OID3="o3";
	private final static String OID4="o4";
	// MODEL.TOPIC
	private final static String TOPIC="Oid23.Topic";
	private final static String TOPIC2="Oid23.Topic2";
	// CLASSES
	private final static String CLASSB=TOPIC+".ClassB";
	private final static String CLASSBEXT=TOPIC2+".ClassB";
	// TD
	private TransferDescription td=null;
	// START EVENT BASKET
	private final static String BID = "b1";
	private final static String BID2 = "b2";
		
	@Before
	public void setUp() throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry iliFile=new FileEntry("src/test/data/validator/Oid23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(iliFile);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}

	//#############################################################//
	//######################## SUCCESS ############################//
	//#############################################################//
	
	// In diesem Test werden verschiedenen Basket ID's getestet.
	// Es darf keine Fehlermeldung ausgegeben werden.
    @Test
    public void validateBid_Ok() throws Exception {
        Iom_jObject objB1=new Iom_jObject(CLASSB, OID1);
        Iom_jObject objB2=new Iom_jObject(CLASSB, OID2);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC,"1"));
        validator.validate(new ObjectEvent(objB1));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent(TOPIC,"b1"));
        validator.validate(new ObjectEvent(objB1));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent(TOPIC,"1b"));
        validator.validate(new ObjectEvent(objB1));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent(TOPIC,"_b1"));
        validator.validate(new ObjectEvent(objB2));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent(TOPIC,"_b1."));
        validator.validate(new ObjectEvent(objB2));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent(TOPIC,"b1-"));
        validator.validate(new ObjectEvent(objB2));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent(TOPIC,"1b_"));
        validator.validate(new ObjectEvent(objB2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==0);
    }
    
	// In diesem Test werden 2 unterschiedliche Basket Id's erstellt.
	// Es darf keine Fehlermeldung ausgegeben werden.
	@Test
	public void differentBid_Ok() throws Exception {
		Iom_jObject objB1=new Iom_jObject(CLASSB, OID1);
		Iom_jObject objB2=new Iom_jObject(CLASSB, OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID2));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}
	
    @Test
    public void errorMsgAddressedWrongBid_Ok() throws Exception {
        Iom_jObject objB1=new Iom_jObject(CLASSB, OID1);
        Iom_jObject objB2=new Iom_jObject(CLASSB, OID2);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC,BID));
        validator.validate(new ObjectEvent(objB1));
        validator.validate(new EndBasketEvent());
        validator.validate(new StartBasketEvent(TOPIC,BID2+" "));
        validator.validate(new ObjectEvent(objB2));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1, logger.getErrs().size());
        assertEquals(BID2+" ", logger.getErrs().get(0).getSourceObjectXtfId());
        assertEquals(TOPIC, logger.getErrs().get(0).getSourceObjectTag());
    }
	
	// Es werden die selben BID's innerhalb verschiedener TransferEvents erstellt.
	// Beim Start eines TransferEvents wird die Map geloescht.
	// Somit soll keine Fehlermeldung ausgegeben werden.
	@Test
	public void differentTransferEvents_Ok() throws Exception {
		Iom_jObject objB1=new Iom_jObject(CLASSB, OID1);
		Iom_jObject objB2=new Iom_jObject(CLASSB, OID2);
		Iom_jObject objBExt=new Iom_jObject(CLASSBEXT, OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID2));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// new transferEvent
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC2,BID));
		validator.validate(new ObjectEvent(objBExt));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==0);
	}

	//#############################################################//
	//######################### FAIL ##############################//
	//#############################################################//
	
    // Es muss ein Fehler geben, da die OID nicht verschieden zu der BID ist
    @Test
    public void validateBIDequalsOID_Fail() throws Exception {
        Iom_jObject objB1=new Iom_jObject(CLASSB, OID1);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC,OID1));
        validator.validate(new ObjectEvent(objB1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertEquals(1,logger.getErrs().size());
        assertEquals("OID <o1> is equal to a BID", logger.getErrs().get(0).getEventMsg());
    }
	
    // Als Syntax gibt es ein Fehler beim Basket ID.
    // Da die BID " 123" ist Falsch als Syntax, muss eine Fehlermeldung ausgegeben werden.
    @Test
    public void validateBid_Fail() throws Exception {
        Iom_jObject objB1=new Iom_jObject(CLASSB, OID1);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC," 123"));
        validator.validate(new ObjectEvent(objB1));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);//
        assertEquals("value < 123> is not a valid BID", logger.getErrs().get(0).getEventMsg());
    }
	
    // Da die BID hat kein Wert, muss eine Fehlermeldung ausgegeben werden.
    @Test
    public void validateBidNull_Fail() throws Exception {
        Iom_jObject objB1=new Iom_jObject(CLASSB, OID1);
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        Settings settings=new Settings();
        
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(TOPIC,null));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());
        // Asserts
        assertTrue(logger.getErrs().size()==1);
        assertEquals("value <> is not a valid BID", logger.getErrs().get(0).getEventMsg());
    }
	
	// In diesem Test wird die Basket id, 2 Mal erstellt.
	// Da die BID b1 bereits existiert, muss eine Fehlermeldung ausgegeben werden.
	@Test
	public void sameBid_Fail() throws Exception {
		Iom_jObject objB1=new Iom_jObject(CLASSB, OID1);
		Iom_jObject objB2=new Iom_jObject(CLASSB, OID2);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("BID b1 of Oid23.Topic already exists in Oid23.Topic", logger.getErrs().get(0).getEventMsg());
	}
	
	// Die  BID b1 wird zwei Mal innerhalb unterschiedlicher Baskets erstellt.
	// Es muss eine Fehlermeldung ausgegeben werden, weil die BID innerhalb des Transferfiles, unique sein muss.
	@Test
	public void sameBidDiffTopics_Fail() throws Exception {
		Iom_jObject objB1=new Iom_jObject(CLASSB, OID1);
		Iom_jObject objB2=new Iom_jObject(CLASSB, OID2);
		Iom_jObject objBExt=new Iom_jObject(CLASSBEXT, OID3);
		Iom_jObject objBExt2=new Iom_jObject(CLASSBEXT, OID4);
		ValidationConfig modelConfig=new ValidationConfig();
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		Settings settings=new Settings();
		Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
		validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID));
		validator.validate(new ObjectEvent(objB1));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent(TOPIC,BID2));
		validator.validate(new ObjectEvent(objB2));
		validator.validate(new EndBasketEvent());
		validator.validate(new StartBasketEvent(TOPIC2,BID));
		validator.validate(new ObjectEvent(objBExt));
		validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
		// Asserts
		assertTrue(logger.getErrs().size()==1);
		assertEquals("BID b1 of Oid23.Topic2 already exists in Oid23.Topic", logger.getErrs().get(0).getEventMsg());
	}
}