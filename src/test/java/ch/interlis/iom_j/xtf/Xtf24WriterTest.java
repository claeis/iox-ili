package ch.interlis.iom_j.xtf;

import static org.junit.Assert.*;

import java.io.*;
import java.util.Scanner;

import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.ViewableProperties;
import ch.interlis.iom_j.xtf.impl.Xtf24WriterAlt;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iox.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.jts.Iox2jtsException;
import org.junit.Before;
import org.junit.Test;

public class Xtf24WriterTest {

    private final static String TEST_IN="src/test/data/Xtf24Writer/dataSection";
    private TransferDescription td=null;
    private IoxFactoryCollection factory;

    @Before
    public void setup() throws Ili2cFailure, IoxException {
        // compile model
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry("src/test/data/Xtf24Reader/dataSection/DataTest1.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        assertNotNull(td);

        factory = new ch.interlis.iox_j.DefaultIoxFactoryCollection();
    }

    @Test
    public void writePolylineObjectEvent() throws IOException, IoxException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream, "UTF-8");

        IomObject coord1 = factory.createIomObject("COORD", null);
        coord1.setattrvalue("C1", "480000.000");
        coord1.setattrvalue("C2", "70000.000");
        coord1.setattrvalue("C3", "5000.000");

        IomObject coord2 = factory.createIomObject("COORD", null);
        coord2.setattrvalue("C1", "490000.000");
        coord2.setattrvalue("C2", "80000.000");
        coord2.setattrvalue("C3", "5000.000");

        IomObject segments = factory.createIomObject("SEGMENTS", null);
        segments.addattrobj("segment", coord1);
        segments.addattrobj("segment", coord2);

        IomObject polyline = factory.createIomObject("POLYLINE", null);
        polyline.addattrobj("sequence", segments);

        IomObject classN = factory.createIomObject("DataTest1.TopicB.ClassN", "oidN");
        classN.addattrobj("attrN1", polyline);

        Xtf24WriterAlt xtfWriter = new Xtf24WriterAlt(outputStreamWriter, Ili2cUtility.getIoxMappingTable(td));
        xtfWriter.writeStartTransfer(null, null, Ili2cUtility.buildModelList(td));
        xtfWriter.writeStartBasket("DataTest1.TopicB", "bidB");
        xtfWriter.writeObject(classN);
        xtfWriter.writeEndBasket();
        xtfWriter.writeEndTransfer();

        String expected = new Scanner(new File(TEST_IN,"PolylineWithStraights.xtf")).useDelimiter("\\Z").next();
        assertEquals(expected, byteArrayOutputStream.toString());
    }

    @Test
    public void writeMultiCoordObjectEvent() throws IOException, IoxException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream, "UTF-8");

        IomObject coord1 = factory.createIomObject("COORD", null);
        coord1.setattrvalue("C1", "480000.111");
        coord1.setattrvalue("C2", "70000.111");
        coord1.setattrvalue("C3", "5000.111");

        IomObject coord2 = factory.createIomObject("COORD", null);
        coord2.setattrvalue("C1", "480000.222");
        coord2.setattrvalue("C2", "70000.222");
        coord2.setattrvalue("C3", "5000.222");

        IomObject coord3 = factory.createIomObject("COORD", null);
        coord3.setattrvalue("C1", "480000.333");
        coord3.setattrvalue("C2", "70000.333");
        coord3.setattrvalue("C3", "5000.333");

        IomObject multicoord = factory.createIomObject("MULTICOORD", null);
        multicoord.addattrobj("coord", coord1);
        multicoord.addattrobj("coord", coord2);
        multicoord.addattrobj("coord", coord3);

        IomObject multicoordClass = factory.createIomObject("DataTest1.TopicB.MultiCoord", "mOid");
        multicoordClass.addattrobj("attr4", multicoord);

        Xtf24WriterAlt xtfWriter = new Xtf24WriterAlt(outputStreamWriter, Ili2cUtility.getIoxMappingTable(td));
        xtfWriter.writeStartTransfer(null, null, Ili2cUtility.buildModelList(td));
        xtfWriter.writeStartBasket("DataTest1.TopicB", "bidB");
        xtfWriter.writeObject(multicoordClass);
        xtfWriter.writeEndBasket();
        xtfWriter.writeEndTransfer();

        String expected = new Scanner(new File(TEST_IN,"MultiCoord.xtf")).useDelimiter("\\Z").next();
        assertEquals(expected, byteArrayOutputStream.toString());
    }

    @Test
    public void writeMultiPolylineObjectEvent() throws IOException, IoxException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream, "UTF-8");

        IomObject coord1 = factory.createIomObject("COORD", null);
        coord1.setattrvalue("C1", "480000.111");
        coord1.setattrvalue("C2", "70000.111");
        coord1.setattrvalue("C3", "5000.111");

        IomObject coord2 = factory.createIomObject("COORD", null);
        coord2.setattrvalue("C1", "480000.222");
        coord2.setattrvalue("C2", "70000.222");
        coord2.setattrvalue("C3", "5000.222");

        IomObject coord3 = factory.createIomObject("COORD", null);
        coord3.setattrvalue("C1", "480000.333");
        coord3.setattrvalue("C2", "70000.333");
        coord3.setattrvalue("C3", "5000.333");

        IomObject segments = factory.createIomObject("SEGMENTS", null);
        segments.addattrobj("segment", coord1);
        segments.addattrobj("segment", coord2);
        segments.addattrobj("segment", coord3);

        IomObject polyline = factory.createIomObject("POLYLINE", null);
        polyline.addattrobj("sequence", segments);

        IomObject multipolyline = factory.createIomObject("MULTIPOLYLINE", null);
        multipolyline.addattrobj("polyline", polyline);
        multipolyline.addattrobj("polyline", polyline);

        IomObject multipolylineClass = factory.createIomObject("DataTest1.TopicB.ClassPM", "mOid");
        multipolylineClass.addattrobj("attrPM", multipolyline);

        Xtf24WriterAlt xtfWriter = new Xtf24WriterAlt(outputStreamWriter, Ili2cUtility.getIoxMappingTable(td));
        xtfWriter.writeStartTransfer(null, null, Ili2cUtility.buildModelList(td));
        xtfWriter.writeStartBasket("DataTest1.TopicB", "bidB");
        xtfWriter.writeObject(multipolylineClass);
        xtfWriter.writeEndBasket();
        xtfWriter.writeEndTransfer();

        String expected = new Scanner(new File(TEST_IN,"MultiPolyline.xtf")).useDelimiter("\\Z").next();
        assertEquals(expected, byteArrayOutputStream.toString());
    }
}
