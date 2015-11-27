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
package ch.interlis.iom_j.iligml;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import ch.interlis.iox.*;
import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.ViewableProperties;
import ch.interlis.iom_j.xtf.XtfModel;

/** This class implements an INTERLIS 2 writer using a lightweight model representation.
 * @author ce
 * @version $Revision: 1.0 $ $Date: 24.07.2006 $
 */
public class Etf1WriterBase implements IoxWriter {
	private Etf1WriterAlt alt=null;
	private java.io.OutputStream outputFile=null;
	private IoxFactoryCollection factory=new  ch.interlis.iox_j.DefaultIoxFactoryCollection();
	
	/** Creates a new instance of a writer.
	 * @param buffer Output writer to write to
	 * @param mapping1 model of data to write
	 * @param version version of transfer format (2.2 or 2.3)
	 * @throws IoxException
	 */
	public Etf1WriterBase(java.io.OutputStreamWriter buffer,ViewableProperties mapping1,java.util.HashMap nameMapping,String version)
	throws IoxException
	{ 
		alt=new Etf1WriterAlt(buffer,mapping1,nameMapping,version);
	}
	
	/** Creates a new instance of a writer.
	 * @param outfile File to write to
	 * @param mapping1 model of data to write
	 * @param version version of transfer format (2.2 or 2.3)
	 * @throws IoxException
	 */
	public Etf1WriterBase(java.io.File outfile,ViewableProperties mapping1,java.util.HashMap nameMapping,String version)
	throws IoxException
	{
		try{
			java.io.File outdir=outfile.getParentFile();
			if(outdir!=null && !outdir.exists()){
				if(!outdir.mkdirs()){
					throw new java.io.IOException("failed to create directory "+outdir.getAbsolutePath());
				}
			}
			outputFile=new java.io.FileOutputStream(outfile);
			init(outputFile,mapping1,nameMapping,version);
		}catch(java.io.IOException ex){
			throw new IoxException(ex);
		}
	}
	
	/** Creates a new instance of a writer.
	 * @param buffer Output stream to write to
	 * @param mapping1 model of data to write
	 * @param version version of transfer format (2.2 or 2.3)
	 * @throws IoxException
	 */
	public Etf1WriterBase(java.io.OutputStream buffer,ViewableProperties mapping1,java.util.HashMap nameMapping,String version)
	throws IoxException
	{ 
		init(buffer, mapping1,nameMapping,version);
	}
	private void init(java.io.OutputStream buffer, ViewableProperties mapping1,java.util.HashMap nameMapping,String version)
		throws IoxException {
		try{
			alt=new Etf1WriterAlt(new java.io.OutputStreamWriter(buffer,"UTF-8"),mapping1,nameMapping,version);
		}catch(java.io.UnsupportedEncodingException ex){
			throw new IoxException(ex);
		}
	}
	public void write(IoxEvent event) throws IoxException {
			if(event instanceof StartTransferEvent){
				StartTransferEvent e=(StartTransferEvent)event;
				alt.writeStartTransfer(e.getSender(),e.getComment(),getModels());
			}else if(event instanceof StartBasketEvent){
				StartBasketEvent e=(StartBasketEvent)event;
				alt.writeStartBasket(e.getType(),e.getBid(),e.getConsistency(),e.getKind(),e.getStartstate(),e.getEndstate(),e.getTopicv());
			}else if(event instanceof ObjectEvent){
				ObjectEvent e=(ObjectEvent)event;
				alt.writeObject(e.getIomObject());
			}else if(event instanceof EndBasketEvent){
				alt.writeEndBasket();
			}else if(event instanceof EndTransferEvent){
				alt.writeEndTransfer();
			}else{
				throw new IoxException("unknown event type "+event.getClass().getName());
			}
	}
	public void close() throws IoxException {
		
		if(alt!=null){
			alt.close();
			alt=null;
		}
		if(outputFile!=null){
			try{
				outputFile.close();
			}catch(java.io.IOException ex){
				throw new IoxException(ex);
			}
			outputFile=null;
		}
	}
	public void flush() throws IoxException 
	{
		alt.flush();
	}
	public IomObject createIomObject(String type, String oid) throws IoxException {
		return factory.createIomObject(type, oid);
	}
	public IoxFactoryCollection getFactory() throws IoxException {
		return factory;
	}
	public void setFactory(IoxFactoryCollection factory) throws IoxException {
		this.factory=factory;
	}
	private XtfModel models[]=null;
	/** Gets the model descriptions.
	 */
	public XtfModel[] getModels(){
		return models;
	}
	/** Sets the model descriptions.
	 */
	public void setModels(XtfModel[] models){
		this.models=models;
	}
}
