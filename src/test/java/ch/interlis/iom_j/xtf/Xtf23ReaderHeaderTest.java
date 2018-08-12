package ch.interlis.iom_j.xtf;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Test;
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.StartTransferEvent;
import ch.interlis.iox_j.IoxSyntaxException;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class Xtf23ReaderHeaderTest {

	private final static String TEST_IN_HEADER="src/test/data/Xtf23Reader/headerSection";
	
	// prueft ob eine XTF Datei mit einer gueltigen: HEADERSECTION
	// erstellt werden kann.
	@Test
	public void test_ValidHeaderSection_Ok() throws IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN_HEADER,"ValidHeaderSection.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Innerhalb der HeaderSection werden diverse Kommentare erstellt.
	@Test
	public void test_CommentsInFile_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN_HEADER,"CommentsInFile.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es werden mehrere Model innerhalb von Models erstellt.
	@Test
	public void test_MultipleMODELDefined_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN_HEADER,"MultipleModelDefined.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}	
	
	// DataSection wird innerhalb von HeaderSection erstellt.
	@Test
	public void test_DataSectionInsideHeaderSection_Fail()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN_HEADER,"DataInsideHeaderSection.xtf"));
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("Unexpected XML event DATASECTION found."));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// DataSection wird ueber dem HeaderSection erstellt.
	@Test
	public void test_DataBeforeHeaderSection_Fail()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN_HEADER,"DataBeforeHeaderSection.xtf"));
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("Unexpected XML event DATASECTION found."));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// HeaderSection darf nur 1 Mal erstellt werden.
	@Test
	public void test_MultipleHeaderSection_Fail()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN_HEADER,"MultipleHeaderSectionDefined.xtf"));
		try{
			reader.read();
			fail();
		}catch(IoxSyntaxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("Unexpected XML event HEADERSECTION found."));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// MODELS darf nur 1 Mal existieren.
	@Test
	public void test_MultipleMODELSDefined_Fail()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN_HEADER,"MultipleModelsDefined.xtf"));
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("Unexpected XML event MODELS found."));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// Der falsche Typ: Models wird innerhalb von Models erstellt.
	@Test
	public void test_HeaderWrongTypeInsideModels_Fail() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN_HEADER,"WrongTypeInModels.xtf"));
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("Unexpected XML event MODELS found."));
	        assertTrue(ioxEx instanceof IoxSyntaxException);
		}
		reader.close();
		reader=null;
	}
	
	// Es wurde kein Model innerhalb von Models erstellt.
	@Test
	public void test_HeaderNoModelInsideModelsDefined_Fail() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN_HEADER,"NoModelInsideModelsDefined.xtf"));
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("expected at least 1 model."));
		}
		reader.close();
		reader=null;
	}
	
	// Es wurde kein Model innerhalb von Model erstellt.
	@Test
	public void test_NoModelInsideModelDefined_Fail() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN_HEADER,"NoModelInsideModelDefined.xtf"));
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("Unexpected XML event MODEL found."));
	        assertTrue(ioxEx instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
	
	// Es wurde 1 MODEL innerhalb von MODELS erstellt. Jedoch wurde der Name des MODEL's null gesetzt.
	@Test
	public void test_NoModelnameFound_Fail() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN_HEADER,"NoModelNameFound.xtf"));
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
		    String msg=(ioxEx).getMessage();
		    System.out.println(msg);
			assertTrue(msg.contains("Open quote is expected"));
		}
		reader.close();
		reader=null;
	}
	
	// prueft ob eine XTF Datei ohne einer gueltigen: VERSION
	// erstellt werden kann.
	@Test
	public void test_HeaderSectionWithoutVersion_Fail() throws IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN_HEADER,"HeaderSectionWithoutVersion.xtf"));
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("Unexpected XML event HEADERSECTION found."));
		}
		reader.close();
		reader=null;
	}
	
	// prueft ob eine XTF Datei ohne einem gueltigen: SENDER
	// erstellt werden kann.
	@Test
	public void test_HeaderSectionWithoutSender_Fail() throws IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN_HEADER,"HeaderSectionWithoutSender.xtf"));
		try{
			reader.read();
			fail();
		}catch(Exception ioxEx){
			assertTrue((ioxEx).getMessage().contains("Unexpected XML event HEADERSECTION found."));
		}
		reader.close();
		reader=null;
	}
	
	// prueft ob eine XTF Datei ohne einem gueltigen: SENDER und einer gueltigen VERSION
	// erstellt werden kann.
	@Test
	public void test_HeaderSectionWithoutSenderAndVersion_Fail() throws IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN_HEADER,"HeaderSectionWithoutSenderAndVersion.xtf"));
		try{
			reader.read();
			fail();
		}catch(Exception ioxEx){
			assertTrue((ioxEx).getMessage().contains("Unexpected XML event HEADERSECTION found."));
		}
		reader.close();
		reader=null;
	}
	
	// prueft ob eine XTF Datei ohne einem gueltigen: NAME innerhalb der Modeldefinition
	// erstellt werden kann.
	@Test
	public void test_HeaderSectionModelWithoutName_Fail() throws IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN_HEADER,"HeaderSectionModelWithoutName.xtf"));
		try{
			reader.read();
			fail();
		}catch(Exception ioxEx){
			assertTrue((ioxEx).getMessage().contains("Unexpected XML event MODEL found."));
		}
		reader.close();
		reader=null;
	}
	
	// prueft ob eine XTF Datei ohne einem gueltigen: VERSION innerhalb der Modeldefinition
	// erstellt werden kann.
	@Test
	public void test_HeaderSectionModelWithoutVersion_Fail() throws IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN_HEADER,"HeaderSectionModelWithoutVersion.xtf"));
		try{
			reader.read();
			fail();
		}catch(Exception ioxEx){
			assertTrue((ioxEx).getMessage().contains("Unexpected XML event MODEL found."));
		}
		reader.close();
		reader=null;
	}
	
	// prueft ob eine XTF Datei ohne einem gueltigen: URI innerhalb der Modeldefinition
	// erstellt werden kann.
	@Test
	public void test_HeaderSectionModelWithoutUri_Fail() throws IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN_HEADER,"HeaderSectionModelWithoutUri.xtf"));
		try{
			reader.read();
			fail();
		}catch(Exception ioxEx){
			assertTrue((ioxEx).getMessage().contains("Unexpected XML event MODEL found."));
		}
		reader.close();
		reader=null;
	}
	
	// prueft ob eine XTF Datei ohne: NAME, VERSION, URI innerhalb der Modeldefinition
	// erstellt werden kann.
	@Test
	public void test_HeaderSectionModelWithoutNameVersionUri_Fail() throws IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN_HEADER,"HeaderSectionModelWithoutNameVersionUri.xtf"));
		try{
			reader.read();
			fail();
		}catch(Exception ioxEx){
			assertTrue((ioxEx).getMessage().contains("Unexpected XML event MODEL found."));
		}
		reader.close();
		reader=null;
	}
}