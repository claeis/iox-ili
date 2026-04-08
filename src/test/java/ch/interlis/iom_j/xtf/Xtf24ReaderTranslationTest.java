package ch.interlis.iom_j.xtf;

import static org.junit.Assert.*;
import java.io.File;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.EndBasketEvent;
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.ObjectEvent;
import ch.interlis.iox.StartBasketEvent;
import ch.interlis.iox.StartTransferEvent;
import ch.interlis.iox_j.IoxSyntaxException;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class Xtf24ReaderTranslationTest {

	private final static String TEST_IN="src/test/data/Xtf24Reader";
	private static final String SPACE=" ";
	private static final String CHAR_ELE_FAIL="unexpected characters"+SPACE;
	private TransferDescription td=null;
	
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"/Translation24.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	@Test
	public void TranslatedModelName_Ok() throws IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"TranslationTranslatedModelName.xml"));
		reader.setModel(td);
		try{
		    IoxEvent ev=null;
            do {
		        ev=reader.read();
		    }while(ev!=null && !(ev instanceof EndTransferEvent));
	    }catch(IoxException ioxEx){
	        EhiLogger.logError(ioxEx);
            fail();
	    }
		reader.close();
		reader=null;
	}
	

}