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
        for(String key:settings.getValues()) {
            String value=settings.getValue(key);
            if(value.equals(NULL)) {
                settings.setValue(key, null);
            }
        }
        for(String key:settings.getTransientValues()) {
            Object value=settings.getTransientObject(key);
            if(value.equals(NULL)) {
                settings.setTransientObject(key, null);
            }
        }
    }

}
