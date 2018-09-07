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
package ch.interlis.iom_j.itf.impl;

import ch.ehi.basics.logging.EhiLogger;
import java.util.HashMap;

import ch.interlis.iox.IoxException;


/** Scanner of ITF files, that recognizes logical lines.
 * @author ce
 * @version $Revision: 1.0 $ $Date: 23.06.2006 $
 */
public class ItfScanner {
	private java.io.LineNumberReader is=null;
	private HashMap str2lk=null;
	public final static String ITF_CHARSET="ISO-8859-1";
	public ItfScanner(java.io.InputStream in)
	throws java.io.UnsupportedEncodingException
	,java.io.IOException
	{
		setupStr2lk();
		is=new java.io.LineNumberReader(new java.io.InputStreamReader(in,ITF_CHARSET));
	}
	public void close()
	throws IoxException
	{
		is=null;
		str2lk=null;
	}
	public boolean read(ItfLineCursor cursor)
	throws IoxException
	{
		String str;
		cursor.setKind(ItfLineKind.EOF);
		try{
			if(lookAheadDone){
				str=lookAheadStr;
                cursor.setLineNumber(lookAheadLineNr);
				lookAheadDone=false;
			}else{
				do{
					str=is.readLine();
			        cursor.setLineNumber(is.getLineNumber());
				}while(str!=null && str.length()==0); // skip empty lines
			}
		}catch(java.io.IOException ex){
			throw new IoxException("failed to read physical line",ex);
		}
		if(str==null){
			cursor.setKind(ItfLineKind.EOF);
			return false;
		}
		if(str.startsWith("MOTR") || str.startsWith("SCNT")){
			int kind=detLineKind(is.getLineNumber(),new StringBuffer(str));
			StringBuffer line=new StringBuffer();
			while(true){
				try{
					str=is.readLine();
				}catch(java.io.IOException ex){
					throw new IoxException("failed to read physical line",ex);
				}
				if(str==null){
					throw new IoxException("end of file reached without line with label ////");
				}else if(str.startsWith("////")){
					break;
				}else{
					line.append(str);
				}
			}
			cursor.setContent(line.toString());
			cursor.setKind(kind);
		}else{
			int contPos=-1;
			StringBuffer line=new StringBuffer();
			while(str!=null && (contPos=getContPos(str))>=0){
				// Zeile mit Fortsetzungszeichen
				line.append(str.substring(0,contPos));
				// naechste Zeile einlesen
				try{
					str=is.readLine();
				}catch(java.io.IOException ex){
					throw new IoxException("failed to read physical line",ex);
				}
				if(str==null){
					throw new IoxException("end of file reached without line with label CONT");
				}else if(!str.startsWith("CONT")){
					throw new IoxException(is.getLineNumber(),"line with label CONT expected");
				}else{
					// Kennung fuer Fortsetzungszeile <CONT > am 
					// Zeilenanfang entfernen
					if(str.length()>5){
						str = str.substring(5);
					}else{
						str="";
					}
				}
			}
			line.append(str);
			int kind=detLineKind(is.getLineNumber(),line);
			if(line.length()>5){
				cursor.setContent(line.substring(5));
			}else{
				cursor.setContent(null);
			}
			cursor.setKind(kind);
		}
		return true;
	}
	private boolean lookAheadDone=false;
	private int lookAheadKind=0;
	private String lookAheadStr=null;
	private int lookAheadLineNr=0;
	public int nextKind()
	throws IoxException
	{
		if(lookAheadDone){
			return lookAheadKind;
		}
		try{
			do{
				lookAheadStr=is.readLine();
				lookAheadLineNr=is.getLineNumber();
			}while(lookAheadStr!=null && lookAheadStr.length()==0); // skip empty lines
			lookAheadDone=true;
		}catch(java.io.IOException ex){
			throw new IoxException("failed to read physical line",ex);
		}
		lookAheadKind=detLineKind(is.getLineNumber(),new StringBuffer(lookAheadStr));
		return lookAheadKind;
	}
	private char continueCode='\\';
	public void setContinueCode(char c)
	{
		continueCode=c;
	}
	private int getContPos(String str)
	{
		int pos=str.lastIndexOf(continueCode);
		if(pos==-1)return -1;
		int len=str.length();
		for(int i=pos+1;i<len;i++){
			char c=str.charAt(i);
			if(c!=' ' && c!='\n' && c!='\r')return -1;
		}
		return pos;
	}
	private int detLineKind(int lineNumber,StringBuffer line)
	throws IoxException
	{
		if(line.length()<4){
			throw new IoxException(lineNumber,"line without valid tag <"+line+">");
		}
		if(line.length()>4 && line.charAt(4)!=' ' && line.charAt(4)!='\t'){
			throw new IoxException(lineNumber,"line without valid tag <"+line.substring(0,5)+">");
		}
		String tag=line.substring(0,4);
		if(!str2lk.containsKey(tag)){
			throw new IoxException(lineNumber,"line with invalid tag <"+tag+">");
		}
		return ((Integer)str2lk.get(tag)).intValue();
	}
	private void setupStr2lk()
	{
		str2lk=new HashMap();
		str2lk.put("SCNT",new Integer(ItfLineKind.SCNT));
		str2lk.put("MOTR",new Integer(ItfLineKind.MOTR));
		str2lk.put("MTID",new Integer(ItfLineKind.MTID));
		str2lk.put("ENDE",new Integer(ItfLineKind.ENDE));
		str2lk.put("////",new Integer(ItfLineKind.FOUR_SLASH));
		str2lk.put("MODL",new Integer(ItfLineKind.MODL));
		str2lk.put("EMOD",new Integer(ItfLineKind.EMOD));
		str2lk.put("TOPI",new Integer(ItfLineKind.TOPI));
		str2lk.put("ETOP",new Integer(ItfLineKind.ETOP));
		str2lk.put("TABL",new Integer(ItfLineKind.TABL));
		str2lk.put("ETAB",new Integer(ItfLineKind.ETAB));
		str2lk.put("OBJE",new Integer(ItfLineKind.OBJE));
		str2lk.put("PERI",new Integer(ItfLineKind.PERI));
		str2lk.put("STPT",new Integer(ItfLineKind.STPT));
		str2lk.put("ELIN",new Integer(ItfLineKind.ELIN));
		str2lk.put("LIPT",new Integer(ItfLineKind.LIPT));
		str2lk.put("ARCP",new Integer(ItfLineKind.ARCP));
		str2lk.put("EFLA",new Integer(ItfLineKind.EFLA));
		str2lk.put("EDGE",new Integer(ItfLineKind.EDGE));
		str2lk.put("EEDG",new Integer(ItfLineKind.EEDG));
		str2lk.put("LATT",new Integer(ItfLineKind.LATT));
		str2lk.put("TABA",new Integer(ItfLineKind.TABA));
		str2lk.put("SOBJ",new Integer(ItfLineKind.SOBJ));
		str2lk.put("ETBA",new Integer(ItfLineKind.ETBA));
		str2lk.put("!!!!",new Integer(ItfLineKind.CMT));
	}
	public static void main(String args[])
	{
		EhiLogger.getInstance().setTraceFilter(false);
		try{
			ItfScanner scanner=new ItfScanner(new java.io.FileInputStream(args[0]));
			ItfLineCursor cursor=new ItfLineCursor();
			while(scanner.read(cursor)){
				System.out.println("kind "+cursor.getKind()+", content <"+cursor.getContent()+">");
			}
		}catch(Exception ex){
			EhiLogger.logError(ex);
		}
	}
}
