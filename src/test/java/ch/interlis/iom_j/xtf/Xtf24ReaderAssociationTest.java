package ch.interlis.iom_j.xtf;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Before;
import org.junit.Test;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iox.EndBasketEvent;
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.ObjectEvent;
import ch.interlis.iox.StartBasketEvent;
import ch.interlis.iox.StartTransferEvent;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class Xtf24ReaderAssociationTest {

	private final static String TEST_IN="src/test/data/Xtf24Reader/associations";
	private TransferDescription td=null;
	
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"/Association.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}

	// return new ch.interlis.iox_j.ObjectEvent(Association.Mensch.Mann oid oid1 {})
	// return new ch.interlis.iox_j.ObjectEvent(Association.Mensch.Frau oid oid2 {bezMann -> oid1 REF {}})
	@Test
	public void embedded_0to1_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Embedded_0to1.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es soll keine Referenz erstellt werden.
	// return new ch.interlis.iox_j.ObjectEvent(Association.Mensch.Mann oid oid1 {})
	// return new ch.interlis.iox_j.ObjectEvent(Association.Mensch.Frau oid oid2 {})
	@Test
	public void embedded_0to0_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Embedded_0_0.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}

	// return new ch.interlis.iox_j.ObjectEvent(Association.Autos.Auto oid oid1 {})
	// return new ch.interlis.iox_j.ObjectEvent(Association.Autos.Garage oid oid2 {bezAuto -> oid1 REF {}})
	@Test
	public void embedded_1to1_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Embedded_1to1.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// return new ch.interlis.iox_j.ObjectEvent(Association.Schule.Kinder oid oid1 {})
	// return new ch.interlis.iox_j.ObjectEvent(Association.Schule.Lehrer oid oid2 {bezKinder -> oid1 REF {}})
	@Test
	public void embedded_1toN_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Embedded_1toN.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// return new ch.interlis.iox_j.ObjectEvent(Association.Firma.Admin oid oid1 {bezUser -> oid2 REF {}})
	// return new ch.interlis.iox_j.ObjectEvent(Association.Firma.User oid oid2 {})
	@Test
	public void embedded_Nto1_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Embedded_Nto1.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// return new ch.interlis.iox_j.ObjectEvent(Association.Atom.Proton oid oidP {})
	// return new ch.interlis.iox_j.ObjectEvent(Association.Atom.Neutron oid oidN {electronA -> oidP REF {}})
	@Test
	public void embedded2_1to1_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Embedded2_1to1.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// return new ch.interlis.iox_j.ObjectEvent(Association.Transport.Schiff oid oid1 {})
	// return new ch.interlis.iox_j.ObjectEvent(Association.Transport.Hafen oid oid2 {bezSchiff -> oid1, ORDER_POS 6 REF {}})
	@Test
	public void embedded_1to1_OrderPos_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Embedded_1to1_OrderPos.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// return new ch.interlis.iox_j.ObjectEvent(Association.Baum.Ast oid oid1 {})
	// return new ch.interlis.iox_j.ObjectEvent(Association.Baum.Blatt oid oid2 {})
	// return new ch.interlis.iox_j.ObjectEvent(Association.Baum.Beziehung {bezAst -> oid1 REF {}, bezBlatt -> oid2 REF {}})
	@Test
	public void alone_NtoN_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Alone_NtoN.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Delete Object mit einer tid und einer Reference erstellt werden kann.
	@Test
	public void testDeleteObjectWithRef_Fail()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Embedded_1to1_DeleteRef.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Autos.Auto oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Autos.Garage oid oid2 {bezAuto -> oid1 REF {}}
		assertTrue(reader.read() instanceof  ObjectEvent); // ili:delete tid=22.
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("ili:delete references are not yet implemented."));
	        assertTrue(ioxEx instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
	
	// return new ch.interlis.iox_j.ObjectEvent(Association.Autos.Auto oid oid1 {})
	// return new ch.interlis.iox_j.ObjectEvent(Association.Autos.Garage oid oid2 {})
	// return new ch.interlis.iox_j.ObjectEvent(Association.Autos.Beziehung {bezAuto -> oid1 REF {}})
	//@Test
	public void embedded_1to1_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Embedded_1to1_Fail.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	} // --> is not stand alone!
}