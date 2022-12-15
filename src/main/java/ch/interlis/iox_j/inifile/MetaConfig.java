package ch.interlis.iox_j.inifile;

import ch.ehi.basics.settings.Settings;

public class MetaConfig {
    public static final String NULL = "NULL";
    public static final String CONFIGURATION = "CONFIGURATION";
    public static final String CONFIG_VALIDATOR_CONFIG = "org.interlis2.validator.config";
    public static final String CONFIG_REFERENCE_DATA = "ch.interlis.referenceData";
    public static final String CONFIG_BASE_CONFIG = "baseConfig";

    private MetaConfig() {
    };
    public static void mergeSettings(Settings metaSettings, Settings settings) {
        for(String key:metaSettings.getValues()) {
            String value=metaSettings.getValue(key);
            String preferedValue=settings.getValue(key);
            if(preferedValue==null) {
                if(value.equals(NULL)) {
                    // ignore
                }else {
                    settings.setValue(key, value);
                }
            }
        }
        for(String key:metaSettings.getTransientValues()) {
            Object value=metaSettings.getTransientObject(key);
            Object preferedValue=settings.getTransientObject(key);
            if(preferedValue==null) {
                if(value.equals(NULL)) {
                    // ignore
                }else {
                    settings.setTransientObject(key, value);
                }
            }
        }
    }
    public static void removeNullFromSettings(Settings settings) {
        // iterate over a copy of the key set, so that we can remove values
        for(String key:new java.util.HashSet<String>(settings.getValues())) {
            String value=settings.getValue(key);
            if(value.equals(NULL)) {
                settings.setValue(key, null); // remove the value
            }
        }
        // iterate over a copy of the key set, so that we can remove values
        for(String key:new java.util.HashSet<String>(settings.getTransientValues())) {
            Object value=settings.getTransientObject(key);
            if(value.equals(NULL)) {
                settings.setTransientObject(key, null); // remove the value
            }
        }
    }

}
