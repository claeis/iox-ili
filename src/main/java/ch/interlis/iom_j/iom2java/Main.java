package ch.interlis.iom_j.iom2java;

import java.io.File;
import ch.ehi.basics.logging.EhiLogger;

/** main program of iom2java tool.
 * read from set Xtf file and write down a java formatted test to console.
 */
public class Main {
	/** name of application as shown to user.
	 */
	public static final String APP_NAME="iom2java";
	/** name of jar file.
	 */
	public static final String APP_JAR="iom2java.jar";
	/** version of application.
	 */
	private static String version=null;
	/** main program entry.
	 * @param args command line arguments.
	 */
	public static void main(String args[]){
		if(args.length==0){
			return;
		}
		int argi=0;
		for(;argi<args.length;argi++){
			String arg=args[argi];
			if(arg.equals("--version")){
				printVersion();
				return;
			}else if(arg.equals("--help")){
					printVersion();
					System.err.println();
					printDescription ();
					System.err.println();
					printUsage ();
					System.err.println();
					System.err.println("OPTIONS");
					System.err.println("  --help                Display this help text.");
					System.err.println("  --version             Display the version of "+APP_NAME+".");
					System.err.println();
					return;
			}else if(arg.startsWith("-")){
				EhiLogger.logAdaption(arg+": unknown option; ignored");
			}else{
				break;
			}
		}
		int dataFileCount=args.length-argi;
		if(dataFileCount>0) {
			File xtfFile = new File(getDataFiles(args, argi, dataFileCount));
			Iom2JavaTool iom2java=new Iom2JavaTool();
			try {
				iom2java.xtf2java(xtfFile);
			}catch(Exception e) {
				System.exit(1);
			}
		}else{
			EhiLogger.logError(APP_NAME+": wrong number of arguments");
			System.exit(2);
		}
		
	}
	private static String getDataFiles(String[] args, int argi, int dataFileCount) {
		String xtfFile = null;
		while(argi<args.length){
			xtfFile=args[argi];
			argi++;
		}
		return xtfFile;
	}
	
	/** Prints program version.
	 */
	protected static void printVersion ()
	{
	  System.err.println(APP_NAME+", Version "+getVersion());
	  System.err.println("  Developed by Eisenhut Informatik AG, CH-3400 Burgdorf");
	}

	/** Prints program description.
	 */
	protected static void printDescription ()
	{
	  System.err.println("DESCRIPTION");
	  System.err.println("  read from set Xtf file and write down a java formatted test to console.");
	}

	/** Prints program usage.
	 */
	protected static void printUsage()
	{
	  System.err.println ("USAGE");
	  System.err.println("  java -jar "+APP_JAR+" [Options] in.xtf");
	}
	
	/** Gets version of program.
	 * @return version e.g. "1.0.0"
	 */
	public static String getVersion() {
		if (version == null) {
			java.util.ResourceBundle resVersion = java.util.ResourceBundle.getBundle("ch/interlis/iox_j/Version");
			StringBuffer ret = new StringBuffer(20);
			ret.append(resVersion.getString("version"));
			ret.append('-');
			ret.append(resVersion.getString("versionCommit"));
			version = ret.toString();
		}
		return version;
	}
}