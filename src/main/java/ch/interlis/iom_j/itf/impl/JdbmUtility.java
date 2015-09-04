package ch.interlis.iom_j.itf.impl;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.RecordManagerOptions;

public class JdbmUtility {
	private JdbmUtility(){}

	public static void removeRecordManagerFiles(String fileBasename)
	{
	
		String[] exts= { ".idr",
		 ".idf",
		 ".dbr",
		 ".dbf"};
	    String TransactionManagerExtension = ".t";
		
	    for(String ext : exts){
	    	String f1=fileBasename+ext;	    	
			File f1t = new File(f1+TransactionManagerExtension);
			if(f1t.exists()){
				f1t.delete();
			}
			for(int i=0;;i++){
				File f1d = new File(f1+"."+i);
				if(!f1d.exists()){
					break;
				}
				f1d.delete();
			}
	    }
	}

	static public RecordManager createRecordManager(String fileBasename) throws IOException {
		java.util.Properties props=new java.util.Properties();
		props.setProperty(RecordManagerOptions.APPEND_TO_END,"true");
		props.setProperty(RecordManagerOptions.DISABLE_TRANSACTIONS,"true");
		props.setProperty(RecordManagerOptions.CACHE_TYPE,"none");
		props.setProperty(RecordManagerOptions.AUTO_COMMIT,"true");
		return RecordManagerFactory.createRecordManager( fileBasename,props);
	}

	static public String getCacheTmpFilename() {
		long n=new java.util.Random().nextLong();
		if (n == Long.MIN_VALUE) {
			n = 0;
		} else {
			n = Math.abs(n);
		}
		String tmp=System.getProperty("java.io.tmpdir");
		return new File(tmp,"ioxtmp"+n).getPath();
	};
	

}
