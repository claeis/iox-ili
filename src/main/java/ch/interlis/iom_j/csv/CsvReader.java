package ch.interlis.iom_j.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iox.IoxReader;
import ch.interlis.iox_j.IoxIliReader;

public class CsvReader implements IoxReader,IoxIliReader {
	// state
	private static int state;
	private static final int START=0;
	private static final int INSIDE_TRANSFER=1;
	private static final int INSIDE_BASKET=2;
	private static final int INSIDE_OBJECT=3;
	private static final int INSIDE_RECORD_DELIMITER=4;
	private static final int INSIDE_DELIMITER=5;
	private static final int INSIDE_NO_DELIMITER=6;
	private static final int END_DELIMITER=7;
	private static final int END_RECORD_DELIMITER=8;
	private static final int INSIDE_NEWLINE=9;
	private static final int END_OBJECT=10;
	private static final int END_BASKET=11;
	private static final int END_TRANSFER=12;
	private static final int END=13;
	// csv parameter
	private static final String HEADER_PRESENT="present";
	private static final String HEADER_ABSENT="absent";
	private static final String DEFAULT_DELIMITER="\"";
	private static final String DEFAULT_RECORD_DELIMITER=",";
	private static final char NEWLINE_CARRIAGERETURN='\r';
	private static final char NEWLINE_LINEFEED='\n';
	// iox reader parameter
	private IoxFactoryCollection factory;
	private static FileReader inputReader=null;
	private static TransferDescription td=null;
	private static File inputFile=null;
	private static int increasingNumber=1;
	private static BufferedReader reader = null;
	private static String headerDefinition=null;
	private static String userDefined_Delimiter=null;
	private static String userDefined_Record_Delimiter=null;
	// model, topic, class
	private static String MODEL=null;
	private static String TOPIC="Topic";
	private static String CLASS="Class"+increasingNumber;
	// delimiter
	private static char currentDelimiter;
	private static char currentRecordDelimiter;
	
	public CsvReader(File csvFile)throws IoxException{
		state=START;
		try{
			inputFile=new File(csvFile.getPath());
			inputReader=new FileReader(inputFile);
			init(inputReader);
		}catch(java.io.IOException ex){
			throw new IoxException(ex);
		}
	}
	
	private void init(FileReader in) throws IoxException, FileNotFoundException{
		reader= new BufferedReader(in);
		factory=new ch.interlis.iox_j.DefaultIoxFactoryCollection();
	}
	
	public void setModel(TransferDescription td){
		CsvReader.td=td;
	}

	/**
	 * read the optional header and the records of csv file.
	 * @return IoxEvent
	 * @throws IoxException
	 */
	@Override
    public IoxEvent read() throws IoxException{
		IomObject iomObj;
		if(state==START){
			state=INSIDE_TRANSFER;
			return new ch.interlis.iox_j.StartTransferEvent();
		}
		if(state==INSIDE_TRANSFER){
			currentDelimiter=(char) getDelimiter().charAt(0);
			currentRecordDelimiter=(char) getRecordDelimiter().charAt(0);
			state=INSIDE_BASKET;
		}
		if(state==INSIDE_BASKET){
			MODEL=getModelName();
			String bid="b"+getCount();
			if(isHeaderDefined()){
				// read header.
				List<String> records;
				try {
					state=INSIDE_OBJECT;
					records = readChars(reader, currentRecordDelimiter, currentDelimiter);
				} catch (IOException e) {
					throw new IoxException(e);
				}
			}
			state=INSIDE_OBJECT;
			return new ch.interlis.iox_j.StartBasketEvent(MODEL+"."+TOPIC, bid);
		}
		if(state==INSIDE_OBJECT){
			try {
				while(reader.ready()){
	    			iomObj=createIomObject(MODEL+"."+TOPIC+"."+CLASS, null);
					// read every record.
					List<String> records;
					records = readChars(reader, currentRecordDelimiter, currentDelimiter);	
					int count=1;
					for(String record : records){
						iomObj.setattrvalue("attr"+count, record);
						count+=1;
					}
					return new ch.interlis.iox_j.ObjectEvent(iomObj);
				}
			} catch (IOException e) {
				throw new IoxException(e);
			}
			state=END_BASKET;
		}
		if(state==END_BASKET){
			state=END_TRANSFER;
			return new ch.interlis.iox_j.EndBasketEvent();
		}
		if(state==END_TRANSFER){
			state=END;
			return new ch.interlis.iox_j.EndTransferEvent();
		}
        return null;
    }
	
	/**
	 * read the path of input csv file and get the single name of csv file.
	 * @return modelname
	 * @throws IoxException
	 */
	private String getModelName() throws IoxException{
		// get path of csv file
		String path=inputFile.getPath();
		if(path!=null){
			String[] pathParts=path.split("\\\\");
			int partLength=pathParts.length;
			String file=pathParts[partLength-1];
			String[] fileParts=file.split(".csv");
			file=fileParts[0];
			return file;
		}else{
			throw new IoxException("expected csv file");
		}
	}

	/**
	 * read chars of each record and returns a list of attributes and values of each record.
	 * @param bufferedReader
	 * @param separator
	 * @param limiter
	 * @return List<String>
	 * @throws IOException
	 * @throws IoxException
	 */
	private static List<String> readChars(BufferedReader bufferedReader, char separator, char limiter) throws IOException, IoxException{
        List<String> result = new ArrayList<String>();
        StringBuffer currentValue = new StringBuffer();
        // buffered reader is empty/null/!has next
        if (!bufferedReader.ready()){
        	return result;
        }
        int value=0;
        // reads to the end of the stream 
        while((value = bufferedReader.read()) != -1){
           // converts int to character
           char currentChar = (char)value;
           if(state==INSIDE_OBJECT){
	           if(currentChar==currentDelimiter){
	        	   state=INSIDE_RECORD_DELIMITER;
	           }else{
	        	   state=INSIDE_NO_DELIMITER;
	           }
	           if(state==INSIDE_RECORD_DELIMITER){
	        	   state=INSIDE_DELIMITER;
	        	   continue;
	           }
           }
           if(state==INSIDE_DELIMITER){
        	   if(currentChar==currentDelimiter){
        		   state=END_DELIMITER;
        		   continue;
        	   }else{
        		   currentValue.append(currentChar);
        		   continue;
        	   }
           }
           // no delimiter defined
           if(state==INSIDE_NO_DELIMITER){
        	   if(currentChar==currentDelimiter){
        		   state=END_DELIMITER;
        		   continue;
        	   }else if(currentChar==currentRecordDelimiter){
        		   result.add(currentValue.toString());
        		   int charCount=currentValue.length();
    			   currentValue.delete(0, charCount);
    			   state=INSIDE_OBJECT;
    			   continue;
        	   }else if(currentChar==NEWLINE_LINEFEED){
        		   state=INSIDE_NEWLINE;
        	   }else if(currentChar==NEWLINE_CARRIAGERETURN){
        		   continue;
        	   }else{
        		   currentValue.append(currentChar);
        		   continue;
        	   }
           }
           if(state==INSIDE_NEWLINE){
        	   // CRLF no delimiter defined
	           result.add(currentValue.toString());
			   int charCount=currentValue.length();
			   currentValue.delete(0, charCount);
			   state=END_OBJECT;
           }
           if(state==END_DELIMITER){
        	   if(currentChar==currentDelimiter){
        		   currentValue.append(currentChar);
        		   state=INSIDE_DELIMITER;
        		   continue;
        	   }else if(currentChar==currentRecordDelimiter){
        		   result.add(currentValue.toString());
        		   int charCount=currentValue.length();
    			   currentValue.delete(0, charCount);
        		   state=END_RECORD_DELIMITER;
        		   continue;
        	   }else if(currentChar==NEWLINE_CARRIAGERETURN){
        		   result.add(currentValue.toString());
        		   int charCount=currentValue.length();
    			   currentValue.delete(0, charCount);
        		   state=END_OBJECT;
        		   continue;
        	   }else if(currentChar==NEWLINE_LINEFEED){
        		   result.add(currentValue.toString());
        		   int charCount=currentValue.length();
    			   currentValue.delete(0, charCount);
        		   state=END_OBJECT;
        	   }else{
        		   currentValue.append(currentChar);
        		   continue;
        	   }
           }
           if(state==END_RECORD_DELIMITER){
        	   if(currentChar==currentDelimiter){
        		   state=INSIDE_DELIMITER;
        		   continue;
        	   }else if(currentChar==NEWLINE_CARRIAGERETURN || currentChar==NEWLINE_LINEFEED){
        		   state=END_OBJECT;
        	   }else{
        		   state=INSIDE_NO_DELIMITER;
        		   currentValue.append(currentChar);
        		   continue;
        	   }
           }
           if(state==END_OBJECT){
        	   state=INSIDE_OBJECT;
        	   return result;
           }
           currentValue.append(currentChar);
       }
       // end of records
       result.add(currentValue.toString());
       state=END_BASKET;
	   return result;
    }

    @Override
	public IomObject createIomObject(String type, String oid)throws IoxException{
    	if(oid==null){
			oid="o"+getCount();
    	}
		return factory.createIomObject(type, oid);
	}
    
    /**
     * increase count is used to increase object/basket id's.
     * @return
     */
    private String getCount(){
    	int count=increasingNumber;
    	increasingNumber+=1;
    	return String.valueOf(count);
    }
	
	@Override
	public void close() throws IoxException{
		reader=null;
		if(inputFile!=null){
			userDefined_Delimiter=DEFAULT_DELIMITER;
			userDefined_Record_Delimiter=DEFAULT_RECORD_DELIMITER;
			CsvReader.currentDelimiter=DEFAULT_DELIMITER.charAt(0);
			CsvReader.currentRecordDelimiter=DEFAULT_RECORD_DELIMITER.charAt(0);
			inputReader=null;
			td=null;
			inputFile=null;
			increasingNumber=1;
			reader = null;
			headerDefinition=null;
		}
	}
	
	@Override
	public IoxFactoryCollection getFactory() throws IoxException{
		return factory;
	}
	
	@Override
	public void setFactory(IoxFactoryCollection factory) throws IoxException{
		this.factory=factory;
	}
	
	private static Boolean isHeaderDefined() throws IoxException{
		if(headerDefinition!=null){
			if(headerDefinition.contains(HEADER_PRESENT)){
				return true;
			}else if(headerDefinition.contains(HEADER_ABSENT)){
				return false;
			}else{
				throw new IoxException("expected present or absent, unexpected "+headerDefinition);
			}
		}
		return false;
	}
	
	/**
	 * set header is present or header is absent.
	 * @param headerState
	 */
	public void setHeader(String headerState){
		CsvReader.headerDefinition = headerState;
	}
	
	private String getDelimiter() throws IoxException{
		if(userDefined_Delimiter!=null){
			return userDefined_Delimiter;
		}else{
			return DEFAULT_DELIMITER;
		}
	}
	
	/**
	 * set user defined delimiter.
	 * @param definition
	 */
	public static void setDelimiter(String definition){
		userDefined_Delimiter = definition;
	}
	
	private String getRecordDelimiter() throws IoxException{
		if(userDefined_Record_Delimiter!=null){
			return userDefined_Record_Delimiter;
		}else{
			return DEFAULT_RECORD_DELIMITER;
		}
	}
	
	/**
	 * set user defined record delimiter.
	 * @param definition
	 */
	public void setRecordDelimiter(String definition){
		CsvReader.userDefined_Record_Delimiter = definition;
	}
}