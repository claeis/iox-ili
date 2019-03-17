package ch.interlis.iox_j.utility;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

import ch.interlis.iom_j.csv.CsvReader;
import ch.interlis.iom_j.iligml.Iligml20Reader;
import ch.interlis.iom_j.itf.ItfReader2;
import ch.interlis.iom_j.xtf.Xtf24Reader;
import ch.interlis.iom_j.xtf.XtfReader;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxReader;
import ch.interlis.iox_j.IoxUtility;

public class GetModelsTest {
	
	@Test
	public void itfReader2_Ok() throws IoxException {
		List<String> models=IoxUtility.getModels(new File("src/test/data/ItfReader2/SurfaceBasic.itf"));
		java.util.ArrayList<String> expectedModels=new java.util.ArrayList<String>();
		expectedModels.add("Test1");
		assertEquals(expectedModels,models);
	}
	
	
	@Test
	public void xtf23Reader_Ok() throws IoxException {
		List<String> models=IoxUtility.getModels(new File("src/test/data/Xtf23Reader/dataSection/SimpleCoord23a.xtf"));
		java.util.ArrayList<String> expectedModels=new java.util.ArrayList<String>();
		expectedModels.add("SimpleCoord23");
		assertEquals(expectedModels,models);
	}
	
	@Test
	public void xtf24Reader_Ok() throws IoxException {
		List<String> models=IoxUtility.getModels(new File("src/test/data/Xtf24Reader/EmptyObjects.xml"));
		java.util.ArrayList<String> expectedModels=new java.util.ArrayList<String>();
		expectedModels.add("Test1");
		assertEquals(expectedModels,models);
	}
	
	
	@Test
	public void csvReader_Ok() throws IoxException {
		List<String> models=IoxUtility.getModels(new File("src/test/data/CsvReader/TextType.csv"));
		java.util.ArrayList<String> expectedModels=new java.util.ArrayList<String>();
		expectedModels.add("TextType");
		assertEquals(expectedModels,models);
	}
	
	@Test
	public void gml20Reader_Ok() throws IoxException {
		List<String> models=IoxUtility.getModels(new File("src/test/data/Ili23gml20Reader/EmptyBasket.gml"));
		java.util.ArrayList<String> expectedModels=new java.util.ArrayList<String>();
		expectedModels.add("Ili23");
		assertEquals(expectedModels,models);
	}
	
}