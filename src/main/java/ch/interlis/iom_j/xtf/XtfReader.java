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

package ch.interlis.iom_j.xtf;

import javax.xml.stream.events.*;

import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.xtf.impl.MyHandler;
import ch.interlis.iom_j.xtf.impl.XtfWriterAlt;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iox.IoxReader;
import ch.interlis.iox_j.IoxIliReader;
import ch.ehi.basics.logging.EhiLogger;

/** This class implements a INTERLIS 2 reader.
 * @author ce
 * @version $Revision: 1.0 $ $Date: 08.02.2007 $
 */
public class XtfReader implements IoxReader, IoxIliReader {
    public static final String XMLNS_XTF22=XtfWriterAlt.ili22Ns;
    public static final String XMLNS_XTF23=XtfWriterAlt.ili23Ns;
	private ch.interlis.iom_j.xtf.impl.MyHandler handler = null;
	private java.io.InputStream inputFile=null;
	private javax.xml.stream.XMLEventReader reader=null;
	private IoxFactoryCollection factory=new  ch.interlis.iox_j.DefaultIoxFactoryCollection();
	
	/** Creates a new reader.
	 * @param in Input stream to read from
	 * @throws IoxException
	 */
	public XtfReader(java.io.InputStream in)
	throws IoxException
	{
		init(in);
	}
	
	private void init(java.io.InputStream in) 
		throws IoxException 
	{
		handler = new MyHandler();
		handler.setFactory(factory);
		javax.xml.stream.XMLInputFactory inputFactory = javax.xml.stream.XMLInputFactory.newInstance();
		try{
			reader=inputFactory.createXMLEventReader(in);
		}catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
	}
	
	/** Creates a new reader.
	 * @param in Input reader to read from
	 * @throws IoxException
	 */
	public XtfReader(java.io.InputStreamReader in)
	throws IoxException
	{
	}
	
	/** Creates a new reader.
	 * @param xtffile File to read from
	 * @throws IoxException
	 */
	public XtfReader(java.io.File xtffile)
	throws IoxException
	{
		try{
			inputFile=new java.io.FileInputStream(xtffile);
			init(inputFile);
		}catch(java.io.IOException ex){
			throw new IoxException(ex);
		}
	}
	public void close() throws IoxException {
		reader=null;
		handler=null;
		if(inputFile!=null){
			try{
				inputFile.close();
			}catch(java.io.IOException ex){
				throw new IoxException(ex);
			}
			inputFile=null;
		}
	}

	public IoxEvent read() throws IoxException {
		while(reader.hasNext()){
			javax.xml.stream.events.XMLEvent event=null;
			try{
				event=reader.nextEvent();
			}catch(javax.xml.stream.XMLStreamException ex){
				throw new IoxException(ex);
			}
			//EhiLogger.debug(event.toString());
			handler.stopParser=false;
			if(event instanceof StartElement){
				handler.startElement((StartElement)event);
			}else if(event instanceof EndElement){
				handler.endElement((EndElement)event);
			}else if(event instanceof Characters){
				handler.characters((Characters)event);
			}else{
				handler.otherEvents(event);
			}
			if(handler.stopParser){
				//EhiLogger.debug(handler.returnObject.toString());
				return handler.returnObject;
			}
		}
		return null;
	}
	public IomObject createIomObject(String type, String oid) throws IoxException {
		return factory.createIomObject(type, oid);
	}
	public IoxFactoryCollection getFactory() throws IoxException {
		return factory;
	}
	public void setFactory(IoxFactoryCollection factory) throws IoxException {
		this.factory=factory;
		handler.setFactory(factory);
	}

    @Override
    public void setTopicFilter(String[] topicNames) {
        handler.setTopicFilter(topicNames);
    }

    @Override
    public void setModel(TransferDescription td) {
    }

    @Override
    public String getMimeType() {
        if(handler!=null && handler.isIli22()) {
            return XTF_22;
        }
        return XTF_23;
    }
}
