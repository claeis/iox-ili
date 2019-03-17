package ch.interlis.iom_j.iom2java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.xtf.XtfReader;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;

/** read from set file and write down a java formatted test to console.
 */
public class Iom2JavaTool {
	
	private static final String INDENTATION="    ";
	private String bid=null;
	private HashMap<String, Integer> assuranceOfUniqueObjNames=new HashMap<String, Integer>();
	
	/** read from set Xtf file and write down a java formatted test to console.
	 * @param xtfFile to read from.
	 * @throws IoxException
	 */
	public void xtf2java(File xtfFile) throws IoxException {
		InputStream inputFile=null;
		try {
			inputFile=new java.io.FileInputStream(xtfFile);
		} catch (FileNotFoundException e) {
			throw new IoxException("file: '"+xtfFile.getAbsolutePath()+"' not found",e);
		}
		XtfReader reader=null;
		try {
			// create xtf reader
			reader=new XtfReader(inputFile);
		} catch (IoxException e) {
			throw new IoxException("failed to create XtfReader", e);
		}
		System.out.println("@Test");
		String[] fileParts=xtfFile.getName().split(".xtf");
		// create methode
		System.out.println("public void "+fileParts[0]+"() {");
		try{
			IoxEvent event=null;
			do{
				event=reader.read();
				// feed model, topics, object names to iom2java
				writeEventAsJava(event);
			}while(!(event instanceof EndTransferEvent));
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IoxException e) {
					EhiLogger.logError(e);
				}
				reader=null;
			}
		}
		// end methode
		System.out.println("}");
	}
	
	private void writeEventAsJava(IoxEvent event) throws IoxException {
		if(event instanceof StartTransferEvent) {
			System.out.println(INDENTATION+"ArrayList<IoxEvent> events=new ArrayList<IoxEvent>();");
			System.out.println(INDENTATION+"StartTransferEvent startTransferEvent=new StartTransferEvent();");
			System.out.println(INDENTATION+"events.add(startTransferEvent);");
		}else if(event instanceof StartBasketEvent) {
			StartBasketEvent startBasketEvent=(StartBasketEvent) event;
			bid=startBasketEvent.getBid();
			String bidNoSpecialChar = bid.replace(".","_");
			System.out.println(INDENTATION+"{");
			System.out.println(INDENTATION+INDENTATION+"// basket_"+bid);
			System.out.println(INDENTATION+INDENTATION+"StartBasketEvent startBasketEvent_"+bidNoSpecialChar+"=new StartBasketEvent(\""+startBasketEvent.getType()+"\",\""+bid+"\");");
			System.out.println(INDENTATION+INDENTATION+"events.add(startBasketEvent_"+bidNoSpecialChar+");");
		}else if(event instanceof ObjectEvent) {
			IomObject iomObj=((ObjectEvent)event).getIomObject();
			String oid=iomObj.getobjectoid();
			String oidNoSpecialChar = null;
			if(oid!=null) {
				oidNoSpecialChar = oid.replace(".","_");
				
			}
			System.out.println(INDENTATION+INDENTATION+"{");
			System.out.println(INDENTATION+INDENTATION+INDENTATION+"// object_"+oid);
			System.out.println(INDENTATION+INDENTATION+INDENTATION+"Iom_jObject object_"+oidNoSpecialChar+"=new Iom_jObject(\""+iomObj.getobjecttag()+"\",\""+oid+"\");");
			writeData(iomObj, "object_"+oidNoSpecialChar);
			System.out.println(INDENTATION+INDENTATION+INDENTATION+"ObjectEvent objEvent_"+oidNoSpecialChar+"=new ObjectEvent(object_"+oidNoSpecialChar+");");
			System.out.println(INDENTATION+INDENTATION+INDENTATION+"events.add(objEvent_"+oidNoSpecialChar+");");
			System.out.println(INDENTATION+INDENTATION+"}");
			assuranceOfUniqueObjNames.clear();
		}else if(event instanceof EndBasketEvent) {
			String bidNoSpecialChar = bid.replace(".","_");
			System.out.println(INDENTATION+INDENTATION+"EndBasketEvent endBasketEvent_"+bidNoSpecialChar+"=new EndBasketEvent();");
			System.out.println(INDENTATION+INDENTATION+"events.add(endBasketEvent_"+bidNoSpecialChar+");");
			System.out.println(INDENTATION+"}");
		}else if(event instanceof EndTransferEvent) {
			System.out.println(INDENTATION+"EndTransferEvent endTransferEvent=new EndTransferEvent();");
			System.out.println(INDENTATION+"events.add(endTransferEvent);");
		}
	}

	private void writeData(IomObject obj, String objName) {
		int attrc = obj.getattrcount();
		String propNames[]=new String[attrc];
		for(int j=0;j<attrc;j++){
			propNames[j]=obj.getattrname(j);
		}
		java.util.Arrays.sort(propNames);
		for(int i=0;i<attrc;i++){
		   String propName=propNames[i];
			int propc=obj.getattrvaluecount(propName);
			if(propc>0){
				for(int propi=0;propi<propc;propi++){
				    String value=obj.getattrprim(propName,propi);
				    if(value!=null){
				    	System.out.println(INDENTATION+INDENTATION+INDENTATION+
				    			objName+".setattrvalue(\""+propName+"\", \""+value+"\");");
				    }else{
				    	IomObject structvalue=obj.getattrobj(propName,propi);
						String subObjName="object_"+propName;
						if(assuranceOfUniqueObjNames.containsKey(subObjName)) {
							int oldCount=assuranceOfUniqueObjNames.get(subObjName);
							int newCount=oldCount+=1;
							assuranceOfUniqueObjNames.put(subObjName, newCount);
							subObjName=subObjName+newCount;
						}else {
							assuranceOfUniqueObjNames.put(subObjName, 1);
						}
						System.out.println(INDENTATION+INDENTATION+INDENTATION+
								"IomObject "+subObjName+"="+
								objName+".addattrobj(\""+propName+"\", \""+structvalue.getobjecttag()+"\");");
						writeData(structvalue, subObjName);
				    }
				}
			}
		}
	}
}