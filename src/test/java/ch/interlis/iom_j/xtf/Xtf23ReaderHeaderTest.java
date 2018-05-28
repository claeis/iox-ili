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

public class Xtf23ReaderHeaderTest {

	private final static String TEST_IN="src/test/data/Xtf23Reader/headerSection";
	private TransferDescription td=null;
	
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/Xtf23Reader/Test1.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	// prueft ob eine XTF Datei mit einer gueltigen: HEADERSECTION
	// erstellt werden kann.
	@Test
	public void test_ValidHeaderSection_Ok() throws IoxException {
		XtfReader reader=new XtfReader(new File(TEST_IN,"ValidHeaderSection.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// prueft ob eine Fehlermeldung ausgegeben wird, wenn die HEADERSECTION
	// nicht vorhanden ist.
	@Test
	public void test_FileWithoutHeaderSection_False() throws IoxException {
		XtfReader reader=new XtfReader(new File(TEST_IN,"FileWithoutHeaderSection.xtf"));
		try {
			reader.read();
			fail();
		}catch(IoxException ex) {
			assertTrue((ex).getMessage().contains("Expected HEADERSECTION, but DATASECTION found."));
			assertTrue(ex instanceof IoxException);
		}finally {
			reader.close();
			reader=null;
		}
	}
}