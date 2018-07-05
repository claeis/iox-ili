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

public class XtfReaderTest {

	private final static String TEST_IN="src/test/data/Xtf23Reader";
	private TransferDescription td=null;
	
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"/Test1.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	// prueft ob das Dokument ohne Fehler validiert werden kann.
	@Test
	public void transferElement_Ok() throws Iox2jtsException, IoxException {
		XtfReader reader=new XtfReader(new File(TEST_IN,"ValidTransferElement.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn end-TRANSFER-tag
	// falsch geschrieben wurde: 'TRANSFERS'.
	@Test
	public void test_WrongSpelledEndTransferElement_False() throws IoxException {
		XtfReader reader=new XtfReader(new File(TEST_IN,"WrongSpelledEndTransferElement.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		try {
			reader.read();
			fail();
		}catch(IoxException ex) {
			assertTrue((ex).getMessage().contains("The end-tag for element type \"TRANSFER\" must end with a '>' delimiter."));
			assertTrue(ex instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn das start-TRANSFER-tag
	// falsch geschrieben wurde: 'TRANSFERS'.
	@Test
	public void test_WrongSpelledStartTransferElement_False() throws IoxException {
		XtfReader reader=new XtfReader(new File(TEST_IN,"WrongSpelledStartTransferElement.xtf"));
		try {
			reader.read();
			fail();
		}catch(IoxException ex) {
			assertTrue((ex).getMessage().contains("Expected TRANSFER, but TRANSFERS found."));
			assertTrue(ex instanceof IoxException);
		}finally {
			reader.close();
			reader=null;
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn beim start-TRANSFER-tag: 'RIVELLA'
	// und beim end-TRANSFER-tag: 'RIVELLAS' geschrieben wurde.
	@Test
	public void test_CompleteOtherSpelledStartTransferElement_False() throws IoxException {
		XtfReader reader=new XtfReader(new File(TEST_IN,"CompleteOtherSpelledStartTransferElement.xtf"));
		try {
			reader.read();
			fail();
		}catch(IoxException ex) {
			assertTrue((ex).getMessage().contains("Expected TRANSFER, but RIVELLA found."));
			assertTrue(ex instanceof IoxException);
		}finally {
			reader.close();
			reader=null;
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn das start-TRANSFER-tag
	// und das end-TRANSFER-tag falsch geschrieben wurde: 'TRANSFERS'.
	// Zusaetzlich stimmt das start-tag mit dem end-tag ueberein.
	@Test
	public void test_WrongSpelledStartAndEndTransferElement_False() throws IoxException {
		XtfReader reader=new XtfReader(new File(TEST_IN,"WrongSpelledStartAndEndTransferElement.xtf"));
		try {
			reader.read();
			fail();
		}catch(IoxException ex) {
			assertTrue((ex).getMessage().contains("Expected TRANSFER, but TRANSFERS found."));
			assertTrue(ex instanceof IoxException);
		}finally {
			reader.close();
			reader=null;
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn der Text
	// vom start-TRANSFER-tag und vom end-TRANSFER-tag klein geschrieben wurde.
	@Test
	public void test_WrongCaseSensitiveTransferElement_False() throws IoxException {
		XtfReader reader=new XtfReader(new File(TEST_IN,"WrongCaseSensitiveTransferElement.xtf"));
		try {
			reader.read();
			fail();
		}catch(IoxException ex) {
			assertTrue((ex).getMessage().contains("Expected TRANSFER, but transfer found."));
			assertTrue(ex instanceof IoxException);
		}finally {
			reader.close();
			reader=null;
		}
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn das end-tag: TRANSFER
	// nicht mit einem '>' geschlossen wird.
	@Test
	public void test_InvalidFormatOfTransferElement_False() throws IoxException {
		XtfReader reader=new XtfReader(new File(TEST_IN,"InvalidFormatOfTransferElement.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		try {
			reader.read();
			fail();
		}catch(IoxException ex) {
			assertTrue((ex).getMessage().contains("XML document structures must start and end within the same entity."));
			assertTrue(ex instanceof IoxException);
		}finally {
			reader.close();
			reader=null;
		}
	}
}