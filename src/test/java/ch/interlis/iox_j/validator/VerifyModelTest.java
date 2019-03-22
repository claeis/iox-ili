package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.logging.LogEvent;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxReader;
import ch.interlis.iox.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.logging.LogEventImpl;
import ch.interlis.iox_j.utility.ReaderFactory;

public class VerifyModelTest {
    
    // Config File
    private static final String CONFIG_FILE = "test/data/inconsistentModelVersion/ConfigFile.toml";
    public static final String SETTING_CONFIGFILE = "org.interlis2.validator.configfile";
    public static final String TEST_IN = "src/test/data/validator/inconsistentModelVersion";
    
    // TD
    private TransferDescription td=null;
    
    @Before
    public void setUp() throws Exception {
        Configuration ili2cConfig=new Configuration();
        FileEntry iliFile=new FileEntry(TEST_IN + "/DataIdx16.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(iliFile);
        td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        assertNotNull(td);
    }

    @Test
    public void verifyModel_ConsistentVersion_OK() {
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        
        ch.interlis.iom_j.itf.LogCollector logCollector = new ch.interlis.iom_j.itf.LogCollector();
        EhiLogger.getInstance().addListener(logCollector);
        
        Settings settings = new Settings();
        settings.setValue(SETTING_CONFIGFILE, CONFIG_FILE); 
        settings.setValue(ch.interlis.iox_j.validator.Validator.CONFIG_DO_XTF_VERIFYMODEL, ch.interlis.iox_j.validator.Validator.CONFIG_DO_XTF_VERIFYMODEL_DO);
        
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        assertNotNull(validator);
        
        IoxReader reader=null;
        try {
            reader=new ReaderFactory().createReader(new java.io.File(TEST_IN, "ConsistentIlidata.xml"), errFactory);
            assertNotNull(reader);
            IoxEvent event = reader.read();
            
            assertTrue(event instanceof StartTransferEvent);
            validator.validate(event);
            
            assertFalse(hasAnInfoLogMsgForTheInconsistentVersion(logCollector));
        } catch (IoxException e) {
            assertTrue(e.getMessage(), true);
            assertTrue(e instanceof IoxException);
        } finally {
            try {
                reader.close();
            } catch (IoxException e) {
                assertTrue(e.getMessage(), true);
                assertTrue(e instanceof IoxException);
            }
        }
    }
    
    @Test
    public void verifyModel_InconsistentVersion_OK() {
        ValidationConfig modelConfig=new ValidationConfig();
        LogCollector logger=new LogCollector();
        LogEventFactory errFactory=new LogEventFactory();
        
        ch.interlis.iom_j.itf.LogCollector logCollector = new ch.interlis.iom_j.itf.LogCollector();
        EhiLogger.getInstance().addListener(logCollector);
        
        Settings settings = new Settings();
        settings.setValue(SETTING_CONFIGFILE, CONFIG_FILE); 
        settings.setValue(ch.interlis.iox_j.validator.Validator.CONFIG_DO_XTF_VERIFYMODEL, ch.interlis.iox_j.validator.Validator.CONFIG_DO_XTF_VERIFYMODEL_DO);
        
        Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
        assertNotNull(validator);
        
        IoxReader reader=null;
        try {
            reader=new ReaderFactory().createReader(new java.io.File(TEST_IN, "InconsistentIlidata.xml"), errFactory);
            assertNotNull(reader);
            IoxEvent event = reader.read();
            
            assertTrue(event instanceof StartTransferEvent);
            validator.validate(event);
            
            assertTrue(hasAnInfoLogMsgForTheInconsistentVersion(logCollector));
        } catch (IoxException e) {
            assertTrue(e.getMessage(), true);
            assertTrue(e instanceof IoxException);
        } finally {
            try {
                reader.close();
            } catch (IoxException e) {
                assertTrue(e.getMessage(), true);
                assertTrue(e instanceof IoxException);
            }
        }
    }
    
    private boolean hasAnInfoLogMsgForTheInconsistentVersion(ch.interlis.iom_j.itf.LogCollector logCollector) {
        ArrayList<LogEvent> errs = logCollector.getErrs();
        for (LogEvent err : errs) {
            if (err instanceof LogEventImpl) {
                String infoMsg = "The VERSION in model (Beispiel2) and transferfile do not match (2011-12-22!=2011-12-23)";
                if (err.getEventMsg().equals(infoMsg)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    
}
