package ch.interlis.iox_j.inifile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.StringReader;
import java.nio.charset.Charset;

import ch.interlis.iox_j.validator.ValidationConfig;

public class IniFileReader {

    public static ValidationConfig readFile(java.io.File file) throws IOException {
        ValidationConfig config = null;
        config = new ValidationConfig();
        mergeIniFile(config, file);
        return config;
    }

    public static void mergeIniFile(ValidationConfig config, File file) throws IOException {
        BufferedReader br = null;
        PushbackReader pushbackReader = null;
        StringReader stringReader = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));
            String line = null;
            String key = null;
            String value = null;
            String header = null;
            while ((line = br.readLine()) != null) {
                stringReader = new StringReader(line);
                pushbackReader = new PushbackReader(stringReader);
                skipSpaces(pushbackReader);
                int c = pushbackReader.read();
                // end of line?
                if (c == -1) {
                    // read next line
                    continue;
                }
                // Header line
                if (c == '[') {
                    pushbackReader.unread(c);
                    header = parseHeaderLine(pushbackReader);
                } else if (c == '#') {
                    pushbackReader.unread(c);
                    parseComment(pushbackReader);
                } else {
                    pushbackReader.unread(c);
                    // Key And Value line
                    String[] keyAndValue = parseKeyValueLine(pushbackReader);
                    key = keyAndValue[0];
                    value = keyAndValue[1];
                    config.setConfigValue(header, key, value);
                }
            }
        } finally {
            if (br != null) {
                br.close();
            }
            if (pushbackReader != null) {
                pushbackReader.close();
            }
            if (stringReader != null) {
                stringReader.close();
            }
        }
    }

    private static String parseHeaderLine(PushbackReader line) throws IOException {
        int c;
        skipSpaces(line);
        c = line.read();
        if (c == -1) {
            return null;
        }
        if (c != '[') {
            throw new IOException("unexpected char '" + (char) c + "' in header value");
        }
        skipSpaces(line);
        String headerName = parseName(line);
        skipSpaces(line);
        c = line.read();
        if (c != ']') {
            throw new IOException("unexpected char '" + (char) c + "' in header value");
        }
        parseComment(line);
        return headerName;
    }

    private static void parseComment(PushbackReader line) {
        // no need to read comment
    }

    private static String parseName(PushbackReader line) throws IOException {
        StringBuilder value = new StringBuilder();
        int c = line.read();
        if (c == '"') {
            // quoted name
            c = line.read();
            while (true) {
                // end of line?
                if (c == -1) {
                    break;
                }
                // end quote character?
                if (c == '"') {
                    break;
                }else if(c=='\\') {
                    c=line.read();
                    if (c == -1) {
                        throw new IOException("unexpected end of name");
                    }else if(c=='\\' || c=='"') {
                        value.append((char) c);
                    }else {
                        throw new IOException("unexpected character '"+(char)c+"' after escape");
                    }
                }else {
                    value.append((char) c);
                }
                // read next
                c = line.read();
            }
        } else {
            // unquoted name
            while (true) {
                // end of line?
                if (c == -1) {
                    break;
                }
                // space?
                if (c == ' ' || c == '\t') {
                    break;
                }
                // any non name character?
                if (!Character.isDigit(c) && !Character.isLetter(c) && c != '_' && c != '.' && c != '-' && c != '?'
                        && c != ':' && c != '}' && c != '{') {
                        break;
                }
                // a name character, append it
                value.append((char) c);
                // read next
                c = line.read();
            }
            if (c != -1) {
                line.unread(c);
            }
            if (value.length() == 0) {
                throw new IOException("unexpected end of name");
            }
        }
        return value.toString();
    }

    private static void skipSpaces(PushbackReader line) throws IOException {
        int c = line.read();
        while (c == ' ' || c == '\t') {
            c = line.read();
        }
        if (c != -1) {
            line.unread(c);
        }
    }

    private static String[] parseKeyValueLine(PushbackReader line) throws IOException {
        int c;
        skipSpaces(line);
        String[] keyAndValue = new String[2];
        String key = parseName(line);
        skipSpaces(line);
        c = line.read();
        if (c != '=') {
            throw new IOException("unexpected char '" + (char) c + "' in key value");
        }
        skipSpaces(line);
        String value = parseName(line);
        skipSpaces(line);
        parseComment(line);
        keyAndValue[0] = key;
        keyAndValue[1] = value;
        return keyAndValue;
    }

}
