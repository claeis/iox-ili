package ch.interlis.iom_j.iligml;

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

public class Iligml20ReaderTest {

	private final static String TEST_IN="src/test/data/Iligml20Reader";
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
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"EmptyTransfer.gml"));
		reader.close();
		reader=null;
	}
	@Test
	public void testWrongTopEleName() throws Iox2jtsException, IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"WrongTopEleName.gml"));
		try{
			reader.read();
			fail();
		}catch(IoxException ex){
			
		}
		reader.close();
		reader=null;
	}
	@Test
	public void testWrongTopEleNamespace() throws Iox2jtsException, IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"WrongTopEleNamespace.gml"));
		try{
			reader.read();
			fail();
		}catch(IoxException ex){
			
		}
		reader.close();
		reader=null;
	}
	@Test
	public void testEmptyBasket() throws Iox2jtsException, IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"EmptyBasket.gml"));
		reader.close();
		reader=null;
	}
	@Test
	public void testEmptyObjects() throws Iox2jtsException, IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"EmptyObjects.gml"));
		reader.close();
		reader=null;
	}
	@Test
	public void testUndefinedSurface() throws Iox2jtsException, IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"UndefinedSurface.gml"));
		reader.close();
		reader=null;
	}
	@Test
	public void testSurface() throws Iox2jtsException, IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Surface.gml"));
		reader.close();
		reader=null;
	}
	@Test
	public void testUndefinedArea() throws Iox2jtsException, IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"UndefinedArea.gml"));
		reader.close();
		reader=null;
	}
	@Test
	public void testArea() throws Iox2jtsException, IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Area.gml"));
		reader.close();
		reader=null;
	}
	@Test
	public void testUndefinedReference() throws Iox2jtsException, IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"UndefinedReference.gml"));
		reader.close();
		reader=null;
	}
	@Test
	public void testReference() throws Iox2jtsException, IoxException {
		Iligml20Reader reader=new Iligml20Reader(new File(TEST_IN,"Reference.gml"));
		reader.close();
		reader=null;
	}

	
}
