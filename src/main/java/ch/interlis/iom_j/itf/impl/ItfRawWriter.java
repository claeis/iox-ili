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

import java.io.IOException;

import ch.interlis.iom_j.itf.ModelUtilities;
import ch.interlis.iox.IoxException;

/** Raw writer of ITF files.
 * @author ce
 * @version $Revision: 1.0 $ $Date: 27.07.2006 $
 */
public class ItfRawWriter {
	private java.io.Writer os=null;
	private String undefinedCode="@";
	private char continueCode='\\';
	private boolean isFormatFree=true;
	private int fixedTidSize;
	private int fixedLineSize;
	private int currentOutputColumn;
	public ItfRawWriter(java.io.OutputStream out,ch.interlis.ili2c.metamodel.Ili1Format format)
	throws java.io.UnsupportedEncodingException
	,java.io.IOException
	{
		this.os=new java.io.BufferedWriter(new java.io.OutputStreamWriter(out,ItfScanner.ITF_CHARSET));
		if(format==null){
			undefinedCode=ModelUtilities.code2string(ch.interlis.ili2c.metamodel.Ili1Format.DEFAULT_UNDEFINED_CODE);
			continueCode=ModelUtilities.code2string(ch.interlis.ili2c.metamodel.Ili1Format.DEFAULT_CONTINUE_CODE).charAt(0);
			isFormatFree=true;			
		}else{
			undefinedCode=ModelUtilities.code2string(format.undefinedCode);
			continueCode=ModelUtilities.code2string(format.continueCode).charAt(0);
			isFormatFree=format.isFree;			
		}
		if(!isFormatFree){
			fixedTidSize=format.tidSize;
			fixedLineSize=format.lineSize;
			if(fixedTidSize+5+2>fixedLineSize){
				throw new IllegalArgumentException("fixedTidSize+5+2>fixedLineSize");
			}
		}
	}
	public void close()
	throws IoxException 
	{
		flush();
		os=null;
	}
	public void flush()
		throws IoxException 
	{
		if(os!=null){
			try{
				os.flush();
			}catch(IOException ex){
				throw new IoxException("failed to flush output stream",ex);
			}
		}
	}

	/** current line seperator
	 *
	 */
	static private String nl=null;
	
	/** current line number
	 *
	 */
	private int lineNumber=0;

	/** write a line seperator
	 *
	 */
	private void newline()
	throws java.io.IOException
	{
		if(nl==null)nl=System.getProperty("line.separator");
		os.write(nl);lineNumber++;
	}
	public void writeRawText(String text)
	throws java.io.IOException
	{
		os.write(text);
		currentOutputColumn+=text.length();
	}
	public void writeNewline()
		throws java.io.IOException
	{
		if(!isFormatFree){
			os.write(ch.ehi.basics.tools.StringUtility.STRING(fixedLineSize-currentOutputColumn,' '));
		}
		newline();
		currentOutputColumn=0;
	}
	public void writeTid(String tid)
		throws java.io.IOException
	{
		writeValue(tid,true,fixedTidSize);
	}
	public void writeValue(String value,boolean isNum,int size)
		throws java.io.IOException
	{
		if(isFormatFree){
			if(value!=null){
				os.write(" "+value);
				currentOutputColumn+=1+value.length();
			}else{
				os.write(" "+undefinedCode);
				currentOutputColumn+=2;
			}
		}else{
			if(currentOutputColumn+2+size>fixedLineSize){
				os.write(ch.ehi.basics.tools.StringUtility.STRING(fixedLineSize-currentOutputColumn,' ')+continueCode);
				newline();
				os.write("CONT");
				currentOutputColumn=4;
			}
			if(value!=null){
				if(isNum){
					value=ch.ehi.basics.tools.StringUtility.STRING(size,' ')+value;
				}else{
					value=value+ch.ehi.basics.tools.StringUtility.STRING(size,' ');
				}
				value=value.substring(0,size);
				os.write("  "+value);
				currentOutputColumn+=2+size;
			}else{
				os.write(" "+undefinedCode+ch.ehi.basics.tools.StringUtility.STRING(size,' '));
				currentOutputColumn+=2+size;
			}
		}
	}
	public void writeExplanation(String explanation)
	throws IOException
	{
		if(explanation==null)return;
		explanation=explanation.trim();
		if(explanation.length()==0)return;
		java.io.LineNumberReader lines=new java.io.LineNumberReader(new java.io.StringReader(explanation));
		String line;
		while((line=lines.readLine())!=null){
			os.write(line);
			newline();
		}
	}

}
