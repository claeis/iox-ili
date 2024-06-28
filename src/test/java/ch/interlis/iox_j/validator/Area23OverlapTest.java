package ch.interlis.iox_j.validator;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom_j.xtf.XtfReader;
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.PipelinePool;
import ch.interlis.iox_j.logging.LogEventFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.*;

public class Area23OverlapTest {
	private TransferDescription td=null;
	private static final String TEST_IN="src/test/data/Xtf23Overlap/";
	
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"Overlap23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	private void runValidation(File xtffile, LogCollector logger) throws IoxException
	{	
		EhiLogger.getInstance().setTraceFilter(false);
		XtfReader reader=new XtfReader(xtffile);
		Settings settings=new Settings();
		ch.interlis.iox_j.validator.Validator validator=null;
		LogEventFactory errFactory=new LogEventFactory();
		PipelinePool pool=new PipelinePool();
		ValidationConfig modelConfig=new ValidationConfig();
		validator=new ch.interlis.iox_j.validator.Validator(td,modelConfig, logger, errFactory, pool,settings);
		IoxEvent event=null;
		do{
			event=reader.read();
			validator.validate(event);
		}while(!(event instanceof EndTransferEvent));
	}
	
	// Es soll keine Fehlermeldung ausgegeben werden,
	// da keine Ueberschneidungen gemacht werden.
	@Test
	public void areaSimple_Ok() throws IoxException {
		LogCollector logger=new LogCollector();
		runValidation(new File(TEST_IN+"AreaSimple.xtf"), logger);
		// asserts
		assertEquals(0, logger.getErrs().size());
	}
	
	// Es darf keine Fehlermeldung ausgegeben werden,
	// da der maximal zulaessige Overlap von: 0.05 nicht ueberstiegen wird.
	@Test
	public void area1Boundary_NoOverlap_Ok() throws IoxException {
		LogCollector logger=new LogCollector();
		runValidation(new File(TEST_IN+"Area_1Boundary_NoOverlap.xtf"), logger);
		// asserts
		assertEquals(0, logger.getErrs().size());
	}
	
	// Es darf keine Fehlermeldung ausgegeben werden,
	// da die Innerboundary, die Outerboundary um den Wert: 0.02 ueberschneidet
	// und dabei der maximal zulaessige Overlap von: 0.05 nicht ueberschritten wird.
	@Test
	public void area1Boundary_PermissibleOverlap_Ok() throws IoxException {
		LogCollector logger=new LogCollector();
		runValidation(new File(TEST_IN+"Area_1Boundary_PermissibleOverlap.xtf"), logger);
		// asserts
		assertEquals(0, logger.getErrs().size());
	}
    // Es darf keine Fehlermeldung ausgegeben werden,
    // da der Overlap 0.0006 ist
    // und somit der maximal zulaessige Overlap von: 0.05 nicht ueberschritten wird.
    // siehe auch issue ilivalidator#366
    @Test
    public void area3Boundary_PermissibleOverlap_GH366_Ok() throws IoxException {
        LogCollector logger=new LogCollector();
        runValidation(new File(TEST_IN+"Area_3Boundaries_PermissibleOverlap_GH366.xtf"), logger);
        // asserts
        assertEquals(0, logger.getErrs().size());
    }
	
	// Es soll eine Fehlermeldung ausgegeben werden,
	// da die Innerboundary, die Outerboundary um den Wert: 0.10 ueberschneidet.
	// Der maximal zulaessige Overlap von: 0.05 wird dabei ueberstiegen.
	@Test
	public void area1Boundary_NotAllowedOverlap_Fail() throws IoxException {
		LogCollector logger=new LogCollector();
		runValidation(new File(TEST_IN+"Area_1Boundary_NotAllowedOverlap.xtf"), logger);
		// asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Intersection overlap 0.10000000547560717, coord1 (2600000.000, 1049997.179), coord2 (2600000.000, 1050000.000), tids 1, 1");
	}
	
	// Es soll eine Fehlermeldung ausgegeben werden,
	// da die Innerboundary die Outerboundary nicht an einem Stuetzpunkt beruehrt.
	@Test
	public void area2Boundaries_NoOverlap_Ok() throws IoxException {
		LogCollector logger=new LogCollector();
		runValidation(new File(TEST_IN+"Area_2Boundaries_NoOverlap.xtf"), logger);
		// asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Intersection coord1 (2600000.100, 1050011.411), tids 1, 1");
	}
	
	// Es soll eine Fehlermeldung ausgegeben werden,
	// da die Innerboundary, die Outerboundary um den Wert: 0.02 ueberschneidet und keinen gemeinsamen
	// Stuetzpunkt hat. Die Ueberschneidung ist kleiner als der maximal zulaessige Overlap von: 0.05.
	@Test
	public void area2Boundaries_PermissibleOverlap_Ok() throws IoxException {
		LogCollector logger=new LogCollector();
		runValidation(new File(TEST_IN+"Area_2Boundaries_PermissibleOverlap.xtf"), logger);
		// asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Intersection coord1 (2600000.020, 1050010.632), tids 1, 1");
	}
	
	// Es soll eine Fehlermeldung ausgegeben werden,
	// da die Innerboundary, die Outerboundary um den Wert: 0.10 ueberschneidet.
	// Der maximal zulaessige Overlap von: 0.05 wird dabei ueberstiegen.
	@Test
	public void area2Boundaries_NotAllowedOverlap_Fail() throws IoxException {
		LogCollector logger=new LogCollector();
		runValidation(new File(TEST_IN+"Area_2Boundaries_NotAllowedOverlap.xtf"), logger);
		// asserts
		LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
				"Intersection overlap 0.10000000473705839, coord1 (2600000.100, 1050008.590), coord2 (2600000.100, 1050011.411), tids 1, 1");
	}

	@Test
	public void Area_3Boundaries_NoOverlap() throws Exception {
		LogCollector logger=new LogCollector();
		runValidation(new File(TEST_IN+"Area_3Boundaries_NoOverlap.xtf"), logger);

		assertThat(logger.getErrs(), is(empty()));
	}

	@Test
	public void Area_3Boundaries_PermissibleArcOverlaps() throws Exception {
		LogCollector logger=new LogCollector();
		runValidation(new File(TEST_IN+"Area_3Boundaries_PermissibleArcOverlaps.xtf"), logger);

		assertThat(logger.getErrs(), is(empty()));
	}
}
