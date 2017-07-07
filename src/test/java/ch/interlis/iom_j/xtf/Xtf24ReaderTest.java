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
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class Xtf24ReaderTest {

	private final static String TEST_IN="src/test/data/Xtf24Reader";
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
	
	@Test
	public void testEmptyTransfer() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"EmptyTransfer.xml"));
		reader.close();
		reader=null;
	}
	@Test
	public void testEmptyBasket() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"EmptyBasket.xml"));
		reader.close();
		reader=null;
	}
	@Test
	public void testEmptyObjects() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"EmptyObjects.xml"));
		reader.close();
		reader=null;
	}
	
}
