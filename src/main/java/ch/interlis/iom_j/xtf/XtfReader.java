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
    private static final String ILI_XTF23_READER = "ILI_XTF23_READER";
    
    private IoxReader reader=null;
	
	/** Creates a new reader.
	 * @param in Input stream to read from
	 * @throws IoxException
	 */
	public XtfReader(java.io.InputStream in)
	throws IoxException
	{
	    if(Xtf23Reader0.class.getName().equals(System.getenv(ILI_XTF23_READER))){
	        reader=new Xtf23Reader0(in);
	    }else {
	        reader=new Xtf23Reader(in);
	    }
	}
	
	/** Creates a new reader.
	 * @param in Input reader to read from
	 * @throws IoxException
	 */
	public XtfReader(java.io.InputStreamReader in)
	throws IoxException
	{
        if(Xtf23Reader0.class.getName().equals(System.getenv(ILI_XTF23_READER))){
            reader=new Xtf23Reader0(in);
        }else {
            reader=new Xtf23Reader(in);
        }
	}
	
	/** Creates a new reader.
	 * @param xtffile File to read from
	 * @throws IoxException
	 */
	public XtfReader(java.io.File in)
	throws IoxException
	{
        if(Xtf23Reader0.class.getName().equals(System.getenv(ILI_XTF23_READER))){
            reader=new Xtf23Reader0(in);
        }else {
            reader=new Xtf23Reader(in);
        }
	}
	public void close() throws IoxException {
		reader.close();
	}

	public IoxEvent read() throws IoxException {
	    return reader.read();
	}
	public IomObject createIomObject(String type, String oid) throws IoxException {
		return reader.createIomObject(type, oid);
	}
	public IoxFactoryCollection getFactory() throws IoxException {
		return reader.getFactory();
	}
	public void setFactory(IoxFactoryCollection factory) throws IoxException {
		reader.setFactory(factory);
	}

    @Override
    public void setTopicFilter(String[] topicNames) {
        ((IoxIliReader)reader).setTopicFilter(topicNames);
    }

    @Override
    public void setSkipTopicFilter(String[] topicNames) {
        ((IoxIliReader)reader).setSkipTopicFilter(topicNames);
    }

    @Override
    public void setModel(TransferDescription td) {
        ((IoxIliReader)reader).setModel(td);
    }

    @Override
    public String getMimeType() {
        return ((IoxIliReader)reader).getMimeType();
    }
}
