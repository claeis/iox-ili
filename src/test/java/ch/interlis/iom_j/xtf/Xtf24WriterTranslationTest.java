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
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iom_j.ViewableProperties;
import ch.interlis.iom_j.xtf.impl.Xtf24WriterAlt;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iox.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.jts.Iox2jtsException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

public class Xtf24WriterTranslationTest {

    private final static String TEST_IN="src/test/data/Xtf24Writer";
    private TransferDescription td=null;
    private IoxFactoryCollection factory;

    @Before
    public void setup() throws Ili2cFailure, IoxException {
        // compile model
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry("src/test/data/Xtf24Writer/Translation24.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        assertNotNull(td);

        factory = new ch.interlis.iox_j.DefaultIoxFactoryCollection();
    }

    @Test
    public void writeBasket_Ok() throws IOException, IoxException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream, "UTF-8");

        Xtf24WriterAlt xtfWriter = new Xtf24WriterAlt(outputStreamWriter, Ili2cUtility.getIoxMappingTable(td));
        xtfWriter.writeStartTransfer(null, null, Ili2cUtility.buildModelList(td));
        xtfWriter.writeStartBasket("Translation24_de.TestB_de", "bidB");
        xtfWriter.writeEndBasket();
        xtfWriter.writeEndTransfer();
    }
    @Test
    public void writeTranslatedBasket_Fail() throws IOException, IoxException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream, "UTF-8");

        Xtf24WriterAlt xtfWriter = new Xtf24WriterAlt(outputStreamWriter, Ili2cUtility.getIoxMappingTable(td));
        xtfWriter.writeStartTransfer(null, null, Ili2cUtility.buildModelList(td));
        try {
            xtfWriter.writeStartBasket("Translation24_fr.TestB_fr", "bidB");
            Assert.fail();
        }catch(IllegalArgumentException e) {
            ;
        }
    }
}
