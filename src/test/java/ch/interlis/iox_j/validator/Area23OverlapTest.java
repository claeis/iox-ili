package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
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
	@Ignore("Es wird eine Fehlermeldung ausgegeben, obwohl kein Fehler vorliegt.")
	@Test
	public void area1Boundary_PermissibleOverlap_Ok() throws IoxException {
		LogCollector logger=new LogCollector();
		runValidation(new File(TEST_IN+"Area_1Boundary_PermissibleOverlap.xtf"), logger);
		// asserts
		assertEquals(0, logger.getErrs().size());
	}
	
	// Es soll eine Fehlermeldung ausgegeben werden,
	// da die Innerboundary, die Outerboundary um den Wert: 0.10 ueberschneidet.
	// Der maximal zulaessige Overlap von: 0.05 wird dabei ueberstiegen.
	@Ignore("Zwei intersection tids 1, 1 Meldungen werden ausgegeben. Aber nur eine erwartet.")
	@Test
	public void area1Boundary_NotAllowedOverlap_Fail() throws IoxException {
		LogCollector logger=new LogCollector();
		runValidation(new File(TEST_IN+"Area_1Boundary_NotAllowedOverlap.xtf"), logger);
		// asserts
		assertEquals(3,logger.getErrs().size());
		assertEquals("intersection tids 1, 1", logger.getErrs().get(0).getEventMsg());
		assertEquals("failed to validate polygon", logger.getErrs().get(1).getEventMsg());
		assertEquals("failed to validate AREA Overlap23.Topic.Area.area", logger.getErrs().get(2).getEventMsg());
	}
	
	// Es darf keine Fehlermeldung ausgegeben werden,
	// da der maximal zulaessige Overlap von: 0.05 nicht ueberstiegen wird.
	@Ignore("Es wird eine Fehlermeldung ausgegeben, obwohl keine ueberschneidung vorliegt.")
	@Test
	public void area2Boundaries_NoOverlap_Ok() throws IoxException {
		LogCollector logger=new LogCollector();
		runValidation(new File(TEST_IN+"Area_2Boundaries_NoOverlap.xtf"), logger);
		// asserts
		assertEquals(0, logger.getErrs().size());
	}
	
	// Es darf keine Fehlermeldung ausgegeben werden,
	// da die Innerboundary, die Outerboundary um den Wert: 0.02 ueberschneidet
	// und dabei der maximal zulaessige Overlap von: 0.05 nicht ueberschritten wird.
	@Ignore("Der vorhandene Overlap wird gemeldet, obwohl dieser im gueltigen Bereich ist. Ein weiterer Overlap null wird ausgegeben, obwohl kein weiterer Overlap vorhanden.")
	@Test
	public void area2Boundaries_PermissibleOverlap_Ok() throws IoxException {
		LogCollector logger=new LogCollector();
		runValidation(new File(TEST_IN+"Area_2Boundaries_PermissibleOverlap.xtf"), logger);
		// asserts
		assertEquals(0, logger.getErrs().size());
	}
	
	// Es soll eine Fehlermeldung ausgegeben werden,
	// da die Innerboundary, die Outerboundary um den Wert: 0.10 ueberschneidet.
	// Der maximal zulaessige Overlap von: 0.05 wird dabei ueberstiegen.
	@Ignore("Ein weiterer Overlap null wird ausgegeben, obwohl kein weiterer Overlap vorhanden.")
	@Test
	public void area2Boundaries_NotAllowedOverlap_Fail() throws IoxException {
		LogCollector logger=new LogCollector();
		runValidation(new File(TEST_IN+"Area_2Boundaries_NotAllowedOverlap.xtf"), logger);
		// asserts
		assertTrue(logger.getErrs().size()==3);
		assertEquals("intersection tids 1, 1", logger.getErrs().get(0).getEventMsg());
		assertEquals("failed to validate polygon", logger.getErrs().get(1).getEventMsg());
		assertEquals("failed to validate AREA Overlap23.Topic.Area.area", logger.getErrs().get(2).getEventMsg());
	}
}