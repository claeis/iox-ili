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
                    header = parseSectionHeader(pushbackReader);
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

    private static String parseSectionHeader(PushbackReader line) throws IOException {
        int c;
        skipSpaces(line);
        c = line.read();
        if (c == -1) {
            return null;
        }
        if (c != '[') {
            throw new IOException("unexpected character " + quoteChar(c) + " in section header");
        }
        skipSpaces(line);
        String headerName = parseName(line);
        skipSpaces(line);
        c = line.read();
        if (c != ']') {
            throw new IOException("unexpected character " + quoteChar(c) + " in section header");
        }
        parseComment(line);
        return headerName;
    }

    private static String quoteChar(int c) {
        if(c==-1) {
            return "EOL";
        }
        if(Character.isISOControl(c) || !Character.isDefined(c) || Character.isWhitespace(c)) {
            return "'\\u"+Integer.toHexString(c)+"'";
        }
        return "'"+Character.toString((char)c)+"'";
    }

    private static void parseComment(PushbackReader line) {
        // no need to read comment
    }
    private static String parseName(PushbackReader line) throws IOException {
        return parseValue(line,true);
    }
    private static String parseValue(PushbackReader line) throws IOException {
        return parseValue(line,false);
    }

    private static String parseValue(PushbackReader line,boolean parseName) throws IOException {
        String ret=null;
        int c = line.read();
        if (c == '"') {
            StringBuilder value = new StringBuilder();
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
                        throw new IOException("unexpected end of "+(parseName?"name":"value"));
                    }else if(c=='\\' || c=='"') {
                        value.append((char) c);
                    }else {
                        throw new IOException("unexpected character "+quoteChar(c)+" after escape");
                    }
                }else {
                    value.append((char) c);
                }
                // read next
                c = line.read();
            }
            ret=value.toString();
        } else {
            StringBuilder value = new StringBuilder();
            // unquoted name/value
            while (true) {
                // end of line?
                if (c == -1) {
                    break;
                }
                if(parseName) {
                    // space?
                    if (c == ' ' || c == '\t') {
                        break;
                    }
                    // any non name character?
                    if (!Character.isDigit(c) && !Character.isLetter(c) && c != '_' && c != '.' && c != '-' && c != '?'
                            && c != ':' && c != '}' && c != '{') {
                            break;
                    }
                }else {
                    if (c == '#') {
                        break;
                    }
                }
                // a valid character, append it
                value.append((char) c);
                // read next
                c = line.read();
            }
            if (c != -1) {
                line.unread(c);
            }
            ret=value.toString().trim();
        }
        if (parseName && ret.length() == 0) {
            throw new IOException("unexpected end of name");
        }
        return ret;
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
            throw new IOException("unexpected character " + quoteChar(c) + " in key value line");
        }
        skipSpaces(line);
        String value = parseValue(line);
        skipSpaces(line);
        parseComment(line);
        keyAndValue[0] = key;
        keyAndValue[1] = value;
        return keyAndValue;
    }

}
