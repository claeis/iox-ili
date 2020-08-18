package ch.interlis.iox_j.inifile;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import ch.interlis.iox_j.validator.ValidationConfig;

public class IniFileReaderTest {
    @Test
    public void comments_OK() throws Exception {
        ValidationConfig ini = IniFileReader.readFile(new File("src/test/data/inifile/comments.ini"));
        assertNotNull(ini.getConfigParams("Beispiel1.Bodenbedeckung.Gebaeude"));
        assertEquals("Assekuranz-Nr {AssNr}", ini.getConfigValue("Beispiel1.Bodenbedeckung.Gebaeude", "keymsg.de"));
    }

    @Test
    public void withQuotes_OK() throws Exception {
        ValidationConfig ini = IniFileReader.readFile(new File("src/test/data/inifile/withQuotes.ini"));
        assertNotNull(ini.getConfigParams("Beispiel1.Bodenbedeckung.Gebaeude"));
        assertEquals("Assekuranz-Nr {AssNr}", ini.getConfigValue("Beispiel1.Bodenbedeckung.Gebaeude", "keymsg.de"));
    }

    @Test
    public void noQuotes_OK() throws Exception {
        ValidationConfig ini = IniFileReader.readFile(new File("src/test/data/inifile/noQuotes.ini"));
        assertNotNull(ini.getConfigParams("Beispiel1.Bodenbedeckung.Gebaeude"));
        assertEquals("Assekuranz-Nr", ini.getConfigValue("Beispiel1.Bodenbedeckung.Gebaeude", "keymsg.de"));
    }  
    
    @Test
    public void equalsAndHashtagInsideTheQuotedValue_OK() throws Exception {
        ValidationConfig ini = IniFileReader.readFile(new File("src/test/data/inifile/equalsAndHashtagInsideTheQuotedValue.ini"));
        assertNotNull(ini.getConfigParams("Beispiel1.Bodenbedeckung.Gebaeude"));
        assertEquals("Assekuranz-Nr = {#AssNr}", ini.getConfigValue("Beispiel1.Bodenbedeckung.Gebaeude", "keymsg.de=#"));
    }
    
    @Test
    public void withUmlaute_OK() throws Exception {
        ValidationConfig ini = IniFileReader.readFile(new File("src/test/data/inifile/withUmlaute.ini"));
        assertNotNull(ini.getConfigParams("Beispiel1.Bodenbedeckung.Gebaeude"));
        assertEquals("\u00F6ssekuranz-Nr {AssNr}", ini.getConfigValue("Beispiel1.Bodenbedeckung.Gebaeude", "keymsg.de"));
    } 
    
    @Test
    public void withNestedQuotesHeader_OK() throws Exception {
        ValidationConfig ini = IniFileReader.readFile(new File("src/test/data/inifile/withNestedQuotesHeader.ini"));
        assertNotNull(ini.getConfigParams("Beispiel1.Bod-\"TEST-enbedeckung.Gebaeude"));
        assertEquals("Assekuranz-Nr", ini.getConfigValue("Beispiel1.Bod-\"TEST-enbedeckung.Gebaeude", "keymsg.de"));
    }
    @Test
    public void withNestedQuotesKey_OK() throws Exception {
        ValidationConfig ini = IniFileReader.readFile(new File("src/test/data/inifile/withNestedQuotesKey.ini"));
        assertNotNull(ini.getConfigParams("Beispiel1.Bod-\"TEST-enbedeckung.Gebaeude"));
        assertEquals("Assekuranz-Nr{AssNr}", ini.getConfigValue("Beispiel1.Bod-\"TEST-enbedeckung.Gebaeude", "keym\"TEST1=#=sg.de"));
    }
    @Test
    public void withNestedQuotesValue_OK() throws Exception {
        ValidationConfig ini = IniFileReader.readFile(new File("src/test/data/inifile/withNestedQuotesValue.ini"));
        assertNotNull(ini.getConfigParams("Beispiel1.Bod-\"TEST-enbedeckung.Gebaeude"));
        assertEquals("Asse\"TEST1=#kuranz-Nr {AssNr}", ini.getConfigValue("Beispiel1.Bod-\"TEST-enbedeckung.Gebaeude", "keymsg.de"));
    }    
    
    @Test
    public void moreThanOneKeyValuePerHeader() throws Exception {
        ValidationConfig ini = IniFileReader.readFile(new File("src/test/data/inifile/moreThanOneKeyValuePerHeader.ini"));
        assertNotNull(ini.getConfigParams("Beispiel1.Bodenbedeckung.Gebaeude"));
        assertEquals("Assekuranz-Nr{AssNr}", ini.getConfigValue("Beispiel1.Bodenbedeckung.Gebaeude", "keymsg.de"));
        assertEquals("Insurance-Nr{AssNr}", ini.getConfigValue("Beispiel1.Bodenbedeckung.Gebaeude", "keymsg.en"));
        assertEquals("Assurance-Nr{AssNr}", ini.getConfigValue("Beispiel1.Bodenbedeckung.Gebaeude", "keymsg.fr"));
    }  
    
    @Test
    public void noQuotesSpace() throws Exception {
        ValidationConfig ini = IniFileReader.readFile(new File("src/test/data/inifile/noQuotesSpace.ini"));
        assertNotNull(ini.getConfigParams("Beispiel1.Bodenbedeckung.Gebaeude"));
        assertEquals("Assekuranz-Nr {AssNr}",ini.getConfigValue("Beispiel1.Bodenbedeckung.Gebaeude", "keymsg.de"));
    }
//          ----------------------->>>>>>>>>>>>> Errors <<<<<<<<<<<<<<---------------------------------
//          ----------------------->>>>>>>>>>>>> Errors <<<<<<<<<<<<<<---------------------------------
    
    @Test
    public void lastQuotesKeyValueIsMissing_Fail() throws Exception {
        ValidationConfig ini = null;
        try {
            ini = IniFileReader.readFile(new File("src/test/data/inifile/lastQuotesKeyValueIsMissing_Fail.ini"));
        } catch (Exception e) {
            assertEquals("unexpected character 'A' in key value line",e.getMessage());
        }
    }
    @Test
    public void lastQuotesHeaderIsMissing_Fail() throws Exception {
        ValidationConfig ini = null;
        try {
            ini = IniFileReader.readFile(new File("src/test/data/inifile/lastQuotesHeaderIsMissing_Fail.ini"));
        } catch (Exception e) {
            assertEquals("unexpected character EOL in section header",e.getMessage());
        }
    }
    @Test
    public void equalsIsMissing_Fail() throws Exception {
        ValidationConfig ini = null;
        try {
            ini = IniFileReader.readFile(new File("src/test/data/inifile/equalsIsMissing_Fail.ini"));
        } catch (Exception e) {
            assertEquals("unexpected character '\"' in key value line",e.getMessage());
        }
    }
}
