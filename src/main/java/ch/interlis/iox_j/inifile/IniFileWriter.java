package ch.interlis.iox_j.inifile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import ch.interlis.iox_j.validator.ValidationConfig;

public class IniFileWriter {
    private IniFileWriter() {
        
    }
    public static void writeIniFile(File metaConfigFile, ValidationConfig metaConfig) throws IOException{
        java.io.BufferedWriter writer=null;
        try {
            writer=new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(metaConfigFile),Charset.forName("UTF-8")));
            java.util.Set<String> sectionsS=metaConfig.getIliQnames();
            if(sectionsS!=null) {
                java.util.List<String> sections=new java.util.ArrayList<String>(sectionsS);
                sections.sort(null);
                for(String section:sections) {
                    writer.write("["+section+"]");writer.newLine();
                    java.util.Set<String> paramsS=metaConfig.getConfigParams(section);
                    if(paramsS!=null) {
                        java.util.List<String> params=new java.util.ArrayList<String>(paramsS);
                        params.sort(null);
                        for(String param:params) {
                            String value=metaConfig.getConfigValue(section, param);
                            if(value!=null) {
                                writer.write(param+"=");writer.write(quoteValue(value));writer.newLine();
                            }
                        }
                    }
                }
            }
        }finally {
            if(writer!=null) {
                writer.close();
                writer=null;
            }
        }
        
    }
    private static String quoteValue(String value) {
        if(value==null){
            return null;
        }
        for(int idx=0;idx<value.length();idx++) {
            if(!Character.isLetterOrDigit(value.charAt(idx))) {
                StringBuffer ret=new StringBuffer();
                ret.append("\"");
                for(int j=0;j<value.length();j++) {
                    char c=value.charAt(j);
                    if(c=='\\' || c=='"') {
                        ret.append('\\');
                    }
                    ret.append(c);
                }
                ret.append("\"");
                return ret.toString();
            }
        }
        return value;
    }
}
