/* This file is part of the iox-ili project.
 * For more information, please see <http://www.eisenhutinformatik.ch/iox-ili/>.
 *
 * Copyright (c) 2006 Eisenhut Informatik AG
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */
package ch.interlis.iom.dumpbasketj;

import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox_j.*;
import ch.interlis.iom_j.xtf.XtfReader;

import ch.ehi.basics.logging.EhiLogger;

/**
 * @author ce
 */
public class Main {

	static boolean doout=true; // set to false to measure runtime without output
	static int ind=0;
	static void inc_ind()
	{
		ind++;
	}
	static void dec_ind()
	{
		ind--;
	}
	static String get_ind()
	{
		StringBuffer buffer=new StringBuffer(100);
		for(int i=0;i<ind;i++){
			buffer.append(' ');
			buffer.append(' ');
		}
		return buffer.toString();
	}

	static String encodeConsistency(int consistency)
	{
		String ret;
		switch(consistency){
		case IomConstants.IOM_INCOMPLETE:
			ret="INCOMPLETE";
			break;
		case IomConstants.IOM_INCONSISTENT:
			ret="INCONSISTENT";
			break;
		case IomConstants.IOM_ADAPTED:
			ret="ADAPTED";
			break;
		case IomConstants.IOM_COMPLETE:
		default:
			ret=null;
			break;
		}
		return ret;
	}

	static void dumpobjhead(String prefix,IomObject obj)
	{
		String tag=obj.getobjecttag();
		String oid=obj.getobjectoid();
		String consistency=encodeConsistency(obj.getobjectconsistency());
		if(doout)System.out.print(get_ind()+prefix+": "+tag);
		if(oid!=null)if(doout)System.out.print(", oid <"+oid+">");

		if(consistency!=null)if(doout)System.out.print(", consistency <"+consistency+">");
		if(doout)System.out.println();
	}

	static void dumpobject(IomObject obj)
	{
		int i;
		IomObject structvalue;
		String tag;
		String value;
		String refoid;
		long orderPos;
		inc_ind();
		for(i=0;i<obj.getattrcount();i++){
		   tag=obj.getattrname(i);
		   value=obj.getattrvalue(tag);
		   if(value==null){
			   for(int attri=0;attri<obj.getattrvaluecount(tag);attri++){
					structvalue=obj.getattrobj(tag, attri);
					refoid=structvalue.getobjectrefoid();
					if(refoid!=null){
						orderPos=structvalue.getobjectreforderpos();
						inc_ind();
						if(orderPos!=0){
							if(doout)System.out.println(get_ind()+tag+"=REF "+refoid+", ORDER_POS "+orderPos);
						}else{
							if(doout)System.out.println(get_ind()+tag+"=REF "+refoid);
						}
						if(structvalue.getobjecttag()!=null){
							dumpobject(structvalue);
						}
						dec_ind();
					}else if(structvalue.getobjecttag()!=null){
						inc_ind();
						dumpobjhead(tag,structvalue);
						dumpobject(structvalue);
						dec_ind();
					}
			   }
		   }else{
			   inc_ind();
			   if(doout)System.out.println(get_ind()+tag+"="+value);
			   dec_ind();
		   }
		}
		dec_ind();

	}

//	   sample to show access to interlis objects and various tech infos
	public static void main(String[] args)
	{

	 

		int startxml;
		String xmlFile;
		String oid;
		boolean dumpall;
		String cp;
		EhiLogger.getInstance().setTraceFilter(false);
		
		long startTime=System.currentTimeMillis();

		if(args.length<1){
			System.err.println("dumbaskets [-oid id] file.xml");
			return;
		}
		startxml=0;
		oid=null;
		dumpall=false;
		if(args[0].equals("-oid")){
			startxml+=2;
			oid=args[1];
		}
		if(args[0].equals("-all")){
			startxml+=1;
			dumpall=true;
		}

		xmlFile=args[startxml];

		// no custom error listener; default listener dumps all errors to stderr
		// Iom.addErrListener(listener);

		// read data
		XtfReader reader=null;
		try{
			reader=new XtfReader(new java.io.File (xmlFile));
			/*
			cp=fh.getheadversion();
			System.out.println("version <"+(cp!=null?cp:"")+">");

			cp=fh.getheadsender();
			System.out.println("sender <"+(cp!=null?cp:"")+">");

			cp=fh.getheadcomment();
			System.out.println("comment <"+(cp!=null?cp:"")+">");
			*/
			IoxEvent event=reader.read();
			StartBasketEvent basket=null;
			while(event!=null){

				if(event instanceof StartTransferEvent){
					if(dumpall){
						if(doout)System.out.println(get_ind()+"StartTransferEvent");
						inc_ind();
					}
				}else if(event instanceof EndTransferEvent){
					if(dumpall){
						dec_ind();
						if(doout)System.out.println(get_ind()+"EndTransferEvent");
					}
				}else if(event instanceof StartBasketEvent){
					basket=(StartBasketEvent)event;
					if(oid!=null && oid.equals(basket.getBid())){
						if(doout)System.out.println(get_ind()+"StartBasketEvent");
					}else{
						if(doout)System.out.println(get_ind()+"StartBasketEvent "+basket.getType()+", bid "+basket.getBid());
						basket=null;
					}
					inc_ind();
				}else if(event instanceof EndBasketEvent){
					dec_ind();
					if(dumpall || basket!=null){
						if(doout)System.out.println(get_ind()+"EndBasketEvent");
					}
					basket=null;
				}else if(event instanceof ObjectEvent){
					IomObject obj=((ObjectEvent)event).getIomObject();
					//System.out.println("oid "+obj.getobjectoid());
					if(dumpall){
						inc_ind();
						dumpobjhead("object",obj);
						dumpobject(obj);
						dec_ind();
					}else if(basket!=null){
						inc_ind();
						dumpobjhead("object",obj);
						dec_ind();
					}else if(oid!=null && oid.equals(obj.getobjectoid())){
						inc_ind();
						dumpobjhead("object",obj);
						dumpobject(obj);
						dec_ind();
					}
				}
				event=reader.read();
			}
		}catch(ch.interlis.iox.IoxException ex){
			EhiLogger.logError(ex);
		}catch(java.lang.Throwable ex){
			EhiLogger.logError(ex);
		}finally{
			if(reader!=null){
				try{
					reader.close();
				}catch(ch.interlis.iox.IoxException ex){
					EhiLogger.logError(ex);
				}
				reader=null;
			}
		}
	
		long endTime=System.currentTimeMillis();
		System.out.println((endTime-startTime)+"ms");
	}
}
