package ch.interlis.iox_j.inifile;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.ehi.basics.settings.Settings;

public class MetaConfigTest {
    @Test
    public void removeNullFromSettings() throws Exception {
        Settings test=new Settings();
        test.setValue("key1", "NULL");
        test.setValue("key2", "Value2");
        test.setTransientValue("key3", "NULL");
        test.setTransientValue("key4", "Value4");
        MetaConfig.removeNullFromSettings(test);
        assertNull(test.getValue("test1"));
        assertEquals("Value2",test.getValue("key2"));
        assertNull(test.getTransientValue("test3"));
        assertEquals("Value4",test.getTransientValue("key4"));
    }
}
