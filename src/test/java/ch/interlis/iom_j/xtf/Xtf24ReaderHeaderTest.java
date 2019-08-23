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
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.StartTransferEvent;
import ch.interlis.iox_j.IoxSyntaxException;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class Xtf24ReaderHeaderTest {

	private final static String TEST_IN_HEADER="src/test/data/Xtf24Reader/headerSection";
	private final static String TEST_IN_MODEL="src/test/data/Xtf24Reader";
	private static final String SPACE=" ";
	private static final String START_ELE_FAIL="unexpected start element"+SPACE;
	private static final String END_ELE_FAIL="unexpected end element"+SPACE;
	private static final String CHAR_ELE_FAIL="unexpected characters"+SPACE;
	private TransferDescription td=null;
	
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN_MODEL+"/Test1.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	// Es wird im Header ein Comments erstellt.
	@Test
	public void testCommentsInFile_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"CommentsInFile.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Im Header wird 1 Mal Models und 1 Mal Comments erstellt.
	@Test
	public void testHeaderComments_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"HeaderModelsAndComments.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Im Header wird 1 Mal Models und 1 Mal Sender erstellt.
	@Test
	public void testHeaderSender_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"HeaderModelsAndSender.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Im Header wird 1 Mal Models, 1 Mal Sender und 1 Mal Comments erstellt.
	@Test
	public void testHeaderSenderAndComments_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"HeaderModelsSenderComments.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Im Header wird 1 Mal Models, 1 Mal Sender und 1 Mal Comments auf einer Linie erstellt.
	@Test
	public void xml1Line_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"Xml1Line.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es werden mehrere Model innerhalb von Models erstellt.
	@Test
	public void testHeaderMultipleModelDefined_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"MultipleModelDefined.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}	
	
	// Im Header wird Comments vor Sender erstellt.
	@Test
	public void testHeaderCommentsBeforeSender_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"CommentsBeforeSender.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof StartTransferEvent);
		assertTrue(reader.read() instanceof EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// models muss mindestens 1 Mal vorkommen
	@Test
	public void testNoModelsDefined_Fail()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"NoModelsDefined.xml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains(END_ELE_FAIL+"headersection"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// DataSection wird innerhalb von HeaderSection erstellt.
	@Test
	public void testDataSectionInsideHeaderSection_Fail()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"DataInsideHeaderSection.xml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains(START_ELE_FAIL+"datasection"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// DataSection wird ueber dem HeaderSection definiert.
	@Test
	public void testDataBeforeHeaderSection_Fail()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"DataBeforeHeaderSection.xml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains(START_ELE_FAIL+"datasection"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// HeaderSection darf nur 1 Mal erstellt werden.
	@Test
	public void test_MultipleHeaderSection_Fail()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"MultipleHeaderSectionDefined.xml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains(END_ELE_FAIL+"models"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// <ili:models> darf nur 1 Mal existieren.
	@Test
	public void test_MultipleModels_Fail()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"MultipleModelsDefined.xml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains(CHAR_ELE_FAIL+"Test1"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// <ili:sender> darf maximal 1 Mal existieren. Ist jedoch optional.
	@Test
	public void test_MultipleSender_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"HeaderMultipleSender.xml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains(START_ELE_FAIL+"sender"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// <ili:comments> darf maximal 1 Mal existieren. Ist jedoch optional.
	@Test
	public void test_MultipleComments_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"HeaderMultipleComments.xml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains(START_ELE_FAIL+"comment"));
	        assertTrue(ioxEx instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
	
	// Im Header wird Sender ueber Models erstellt.
	@Test
	public void testHeaderSenderBeforeModels_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"SenderBeforeModels.xml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains(START_ELE_FAIL+"sender"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// Im Header wird Comments ueber Models erstellt.
	@Test
	public void testHeaderCommentsBeforeModels_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"CommentsBeforeModels.xml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains(START_ELE_FAIL+"comments"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// Der falsche Typ: Sender wird innerhalb von Models erstellt.
	@Test
	public void testHeaderWrongTypeInsideModels_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"WrongTypeInModels.xml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains(START_ELE_FAIL+"sender"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// Es wurde kein Model innerhalb von Models erstellt.
	@Test
	public void testHeaderNoModelInsideModelsDefined_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"NoModelInsideModelsDefined.xml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains(END_ELE_FAIL+"models"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// Es wurde kein Model innerhalb von Model erstellt.
	@Test
	public void testNoModelInsideModelDefined_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"NoModelInsideModelDefined.xml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("expected at least 1 model."));
	        assertTrue(ioxEx instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
	
	// Sender wurde innerhalb von Model erstellt.
	@Test
	public void testWrongTypeInModel_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"WrongTypeInModel.xml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains(START_ELE_FAIL+"sender"));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// Es wurde 1 MODEL innerhalb von MODELS erstellt. Jedoch wurde der Name des MODEL nicht gefunden.
	@Test
	public void testNoModelnameFound_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"NoModelNameFound.xml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("expected at least 1 model."));
	        assertTrue(ioxEx instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
	
	// SENDER wurde im header erstellt, jedoch wurde keinen Namen des Senders gefunden.
	@Test
	public void testNoSendernameFound_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"NoSenderNameFound.xml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("sender defined, but empty."));
	        assertTrue(ioxEx instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
	
	// COMMENTS wurde im header erstellt, jedoch wurde keinen COMMENTS Namen gefunden.
	@Test
	public void testNoCommentsnameFound_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN_HEADER,"NoCommentsNameFound.xml"));
		reader.setModel(td);
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("comments defined, but empty."));
	        assertTrue(ioxEx instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
}