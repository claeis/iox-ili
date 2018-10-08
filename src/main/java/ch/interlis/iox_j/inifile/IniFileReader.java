package ch.interlis.iox_j.inifile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import ch.interlis.iox_j.validator.ValidationConfig;

public class IniFileReader {
    static public ValidationConfig readFile(java.io.File file) throws Exception {
        ValidationConfig config = null;
        config = new ValidationConfig();
        mergeIniFile(config, file);
        return config;
    }

    private static void mergeIniFile(ValidationConfig config, File file) throws Exception {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String key = null;
            String value = null;
            String header = null;
            int numberOfLine = 0;
            String line = null;
            while ((line = br.readLine()) != null) {
                String lineWithoutCommnet = removeComments(line);
                if (lineWithoutCommnet.isEmpty()) {
                    numberOfLine++;
                    continue;
                }
                if (lineWithoutCommnet.startsWith("[")) {
                    if (isValidHeader(lineWithoutCommnet)) {
                        header = getHeaderValue(lineWithoutCommnet.substring(1, lineWithoutCommnet.length() - 1));
                    } else {
                        throw new Exception(
                                "\"There is an error at the Header format! ([) or (]) missing! Line number: "
                                        + numberOfLine);
                    }

                } else {
                    String[] keyAndValue = getKeyAndValue(lineWithoutCommnet);
                    if (keyAndValue != null) {
                        key = keyAndValue[0];
                        value = keyAndValue[1];
                    } else {
                        throw new Exception("\"There is an error at the Item format! (=) must be sein! Line number: "
                                + numberOfLine);
                    }
                }

                if (header != null && key != null && value != null) {
                    config.setConfigValue(header, key, value);
                    header = key = value = null;
                }
                numberOfLine++;
            }
        } catch (UnsupportedEncodingException e) {
            throw new Exception(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new Exception(e.getMessage());
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        } finally {
            if (br != null) {
                br.close();
            }

        }
    }

    private static String stripQuotes(String line) {
        return (line.startsWith("\"") && line.endsWith("\"")) == true ? line.substring(1, line.length() - 1) : line;
    }

    private static String[] getKeyAndValue(String line) {
        String values[] = line.split("=");
        values[0] = stripQuotes(values[0]);
        values[1] = stripQuotes(values[1]);
        return values;
    }

    private static String getHeaderValue(String headerValue) {
        return (headerValue.startsWith("\"") && headerValue.endsWith("\"")) == true
                ? headerValue.substring(1, headerValue.length() - 1)
                : headerValue;
    }

    private static String removeComments(String currentLine) {
        StringBuilder text = new StringBuilder();
        int numberOfquotes = 0;
        int numberOfBrackets = 0;
        for (int i = 0; i < currentLine.length(); i++) {
            char c = currentLine.charAt(i);
            if (c == '#' && i == 0) {
                break;
            }
            if (c == '[' || c == ']') {
                numberOfBrackets++;
            }
            if (c == '"') {
                numberOfquotes++;
            }
            if (numberOfBrackets > 0) {
                if ((numberOfBrackets % 2) == 0 && c == '#') {
                    break;
                }
            }

            if ((numberOfquotes % 2) == 0 && c == '#') {
                break;
            }

            text.append(c);
        }
        return text.toString().trim();
    }

    private static boolean isValidHeader(String line) {
        return line.startsWith("[") == true && line.endsWith("]") == true ? true : false;
    }
}
