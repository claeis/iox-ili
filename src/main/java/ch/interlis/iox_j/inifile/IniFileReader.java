package ch.interlis.iox_j.inifile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Properties;

import ch.interlis.iox_j.validator.ValidationConfig;

public class IniFileReader {
    static public ValidationConfig readFile(java.io.File file) throws FileNotFoundException {
        ValidationConfig config = null;
        try {
            config = new ValidationConfig();
            mergeIniFile(config, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    private static void mergeIniFile(ValidationConfig config, File file) throws Exception {
        try {
            int i = 0;
            String key = null;
            String value = null;
            String header = null;
            Properties properties = new Properties();
            properties.load(new FileInputStream(file));
            for (Map.Entry<Object, Object> e : properties.entrySet()) {
                i++;
                if (e.getKey().toString().startsWith("[") && e.getKey().toString().endsWith("]")) {
                    header = getHeaderValue(e.getKey().toString().substring(1, e.getKey().toString().length() - 1));
                } else if (!(e.getKey().toString().startsWith("[")) && !(e.getKey().toString().endsWith("]"))) {
                    key = getKeyValue((String) e.getKey());
                    value = getValue((String) e.getValue());
                } else {
                    throw new Exception("There is an error at the Header format! ([) or (]) missing!");
                }
                if (i == properties.size()) {
                    config.setConfigValue(header, key, value);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String getValue(String value) {
        return stripQuotes(splitComment(value));
    }

    private static String getKeyValue(String value) {
        return stripQuotes(value);
    }

    private static String getHeaderValue(String value) {
        value = stripQuotes(splitComment(value));
        return value;
    }

    private static String splitComment(String value) {
        if (value.contains("#")) {
            String[] newValue = value.split("#");
            value = newValue[0].trim();
        }
        return value;
    }

    private static String stripQuotes(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        } 
        return value;
    }
}
