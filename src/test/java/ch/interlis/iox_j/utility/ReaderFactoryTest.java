package ch.interlis.iox_j.utility;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Test;

import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.csv.CsvReader;
import ch.interlis.iom_j.iligml.Iligml20Reader;
import ch.interlis.iom_j.itf.ItfReader2;
import ch.interlis.iom_j.xtf.Xtf23Reader;
import ch.interlis.iom_j.xtf.Xtf24Reader;
import ch.interlis.iom_j.xtf.XtfReader;
import ch.interlis.iox.EndBasketEvent;
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxReader;
import ch.interlis.iox.ObjectEvent;
import ch.interlis.iox.StartBasketEvent;
import ch.interlis.iox.StartTransferEvent;

public class ReaderFactoryTest {
	
	// Es wird getestet ob der passende Reader: ITFReader zurueckgegeben wird.
	@Test
	public void itfReader2_Ok() throws IoxException {
		IoxReader reader=null;
		new ReaderFactory();
		reader=new ReaderFactory().createReader(new File("src/test/data/ItfReader2/SurfaceBasic.itf"),null);
		assertTrue(reader instanceof ItfReader2);
	}
	
	// Es wird getestet ob der passende Reader: ITFReader zurueckgegeben wird.
	// Wenn die ITF Datei die Endung: txt aufweist.
	@Test
	public void itfReader2_txtExtension_Ok() throws IoxException {
		IoxReader reader=null;
		new ReaderFactory();
		reader=new ReaderFactory().createReader(new File("src/test/data/ReaderFactory/SurfaceBasic.txt"),null);
		assertTrue(reader instanceof ItfReader2);
	}
	
	// Es wird getestet ob der passende Reader: ITFReader zurueckgegeben wird.
	// Wenn die ITF Datei die Endung: csv aufweist.
	@Test
	public void itfReader2_csvExtension_Ok() throws IoxException {
		IoxReader reader=null;
		new ReaderFactory();
		reader=new ReaderFactory().createReader(new File("src/test/data/ReaderFactory/SurfaceBasic.csv"),null);
		assertTrue(reader instanceof ItfReader2);
	}
	
	// Es wird getestet ob der passende Reader: CsvReader zurueckgegeben wird.
	// Wenn keine Steuerzeichen und keine defaultDelimiter oder defaultRecordDelimiter vorhanden sind.
	@Test
	public void csvReader_itfExtension_fail() {
		IoxReader reader=null;
		    try {
                reader=new ReaderFactory().createReader(new File("src/test/data/ReaderFactory/UnknownFile.itf"),null);
                fail();
            } catch (IoxException e) {
                assertEquals("no reader found",e.getMessage());
            }
		
	}
	
	// Es wird getestet ob der passende Reader: CsvReader zurueckgegeben wird.
	// Wenn die Datei leer ist.
	@Test
	public void csvReader_EmptyCsvFile_Ok() throws IoxException {
		IoxReader reader=null;
		new ReaderFactory();
		reader=new ReaderFactory().createReader(new File("src/test/data/ReaderFactory/UnknownFile4.csv"),null);
		assertTrue(reader instanceof CsvReader);
	}
	
	// Es wird getestet ob der passende Reader: XtfReader zurueckgegeben wird.
	@Test
	public void xtf23Reader_Ok() throws IoxException {
		IoxReader reader=null;
		new ReaderFactory();
		reader=new ReaderFactory().createReader(new File("src/test/data/Xtf23Reader/dataSection/SimpleCoord23a.xtf"),null);
		assertTrue(reader instanceof XtfReader);
	}
	
	// Es wird getestet ob der passende Reader: XtfReader zurueckgegeben wird.
	// Wenn die Datei Endung: txt aufweist.
	@Test
	public void xtf23Reader_txtExtension_Ok() throws IoxException {
		IoxReader reader=null;
		new ReaderFactory();
		reader=new ReaderFactory().createReader(new File("src/test/data/ReaderFactory/SimpleCoord23a.txt"),null);
		assertTrue(reader instanceof XtfReader);
	}
	
	// Es wird getestet ob der passende Reader: Xtf24Reader zurueckgegeben wird.
	@Test
	public void xtf24Reader_Ok() throws IoxException {
		IoxReader reader=null;
		new ReaderFactory();
		reader=new ReaderFactory().createReader(new File("src/test/data/Xtf24Reader/EmptyObjects.xml"),null);
		assertTrue(reader instanceof Xtf24Reader);
	}
	
	// Es wird getestet ob der passende Reader: Xtf24Reader zurueckgegeben wird.
	// Wenn die Datei Endung: txt aufweist.
	@Test
	public void xtf24Reader_txtExtension_Ok() throws IoxException {
		IoxReader reader=null;
		new ReaderFactory();
		reader=new ReaderFactory().createReader(new File("src/test/data/ReaderFactory/Coord.txt"),null);
		assertTrue(reader instanceof Xtf24Reader);
	}
	
	// Es wird getestet ob der passende Reader: CsvReader zurueckgegeben wird.
	@Test
	public void csvReader_Ok() throws IoxException {
		IoxReader reader=null;
		new ReaderFactory();
		reader=new ReaderFactory().createReader(new File("src/test/data/CsvReader/TextType.csv"),null);
		assertTrue(reader instanceof CsvReader);
		try {
			IoxEvent event=reader.read();
			assertTrue(event instanceof StartTransferEvent);
			event=reader.read();
			assertTrue(event instanceof StartBasketEvent);
			event=reader.read();
			assertTrue(event instanceof ObjectEvent);
			ObjectEvent objectEvent=(ObjectEvent)event;
			IomObject iomObj=(IomObject) objectEvent.getIomObject();
			assertTrue(iomObj.getattrcount()==3);
			assertTrue(iomObj.getattrvalue("attr1").equals("10"));
			assertTrue(iomObj.getattrvalue("attr2").equals("AU"));
			assertTrue(iomObj.getattrvalue("attr3").equals("Australia"));
			event=reader.read();
			assertTrue(event instanceof EndBasketEvent);
			event=reader.read();
			assertTrue(event instanceof EndTransferEvent);
		}catch(IoxException e) {
			throw new IoxException(e);
		}finally {
			reader.close();
			reader=null;
		}
	}
	
	// Es wird getestet ob der passende Reader: CsvReader zurueckgegeben wird.
	// Wenn die Dateiendung: txt aufweist.
	@Test
	public void csvReader_txtExtension_fail() {
		IoxReader reader=null;
		try {
            reader=new ReaderFactory().createReader(new File("src/test/data/ReaderFactory/TextType.txt"),null);
            fail();
        } catch (IoxException e) {
            assertEquals("no reader found",e.getMessage());
        }
	}
	
	// Es wird getestet ob der passende Reader: Iligml20Reader zurueckgegeben wird.
	@Test
	public void gml20Reader_ili10_Ok() throws IoxException {
		IoxReader reader=null;
		new ReaderFactory();
		reader=new ReaderFactory().createReader(new File("src/test/data/Ili10gml20Reader/Area.gml"),null);
		assertTrue(reader instanceof Iligml20Reader);
	}
	
	// Es wird getestet ob der passende Reader: Iligml20Reader zurueckgegeben wird.
	// Wenn die Dateiendung: csv aufweist.
	@Test
	public void gml20Reader_ili10_csvFile_Ok() throws IoxException {
		IoxReader reader=null;
		new ReaderFactory();
		reader=new ReaderFactory().createReader(new File("src/test/data/ReaderFactory/Area.csv"),null);
		assertTrue(reader instanceof Iligml20Reader);
	}
	
	// Es wird auf einem ili10 Model getestet ob der passende Reader: Iligml20Reader zurueckgegeben wird.
	// Wenn die Dateiendung: txt aufweist.
	@Test
	public void gml20Reader_ili10_txtExtension_Ok() throws IoxException {
		IoxReader reader=null;
		new ReaderFactory();
		reader=new ReaderFactory().createReader(new File("src/test/data/ReaderFactory/Area.txt"),null);
		assertTrue(reader instanceof Iligml20Reader);
	}
	
	// Es wird auf einem ili23 Model getestet ob der passende Reader: Iligml20Reader zurueckgegeben wird.
	// Wenn die Dateiendung: gml aufweist.
	@Test
	public void gml20Reader_ili23_Ok() throws IoxException {
		IoxReader reader=null;
		new ReaderFactory();
		reader=new ReaderFactory().createReader(new File("src/test/data/Ili23gml20Reader/Area.gml"),null);
		assertTrue(reader instanceof Iligml20Reader);
	}
	
	// Es wird auf einem ili23 Model getestet ob der passende Reader: Iligml20Reader zurueckgegeben wird.
	// Wenn die Dateiendung: txt aufweist.
	@Test
	public void gml20Reader_ili23_txtExtension_Ok() throws IoxException {
		IoxReader reader=null;
		new ReaderFactory();
		reader=new ReaderFactory().createReader(new File("src/test/data/ReaderFactory/Coord2.txt"),null);
		assertTrue(reader instanceof Iligml20Reader);
	}
}