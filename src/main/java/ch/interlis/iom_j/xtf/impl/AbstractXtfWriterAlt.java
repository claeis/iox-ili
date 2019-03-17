package ch.interlis.iom_j.xtf.impl;

import java.util.HashSet;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.xtf.XtfModel;
import ch.interlis.iom_j.xtf.XtfStartTransferEvent;
import ch.interlis.iox.IoxException;

public abstract class AbstractXtfWriterAlt {

	protected XMLStreamWriter xout = null;

	public abstract void writeObject(IomObject obj) throws IoxException;

	public abstract void writeEndBasket() throws IoxException;

	public abstract void writeStartBasket(String type, String bid,
			int consistency, int kind, String startstate, String endstate, String[] topicv,String domains)
			throws IoxException;

	public abstract void writeStartBasket(String type, String bid)
			throws IoxException;

	public abstract void writeEndTransfer() throws IoxException;

	public abstract void writeStartTransfer(String sender, String comment,
			XtfModel models[], XtfStartTransferEvent e) throws IoxException;

	public abstract void writeStartTransfer(String sender, String comment,
			XtfModel models[]) throws IoxException;

	protected HashSet unkClsv = new HashSet();
	/** current line seperator
	 *
	 */
	private static String nl = null;

	public XMLStreamWriter getXout() {
		return xout;
	}

	public void close() throws IoxException {
		if(xout!=null){
			try{
				xout.flush();
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}
			xout=null;
		}
	}

	public void flush() throws IoxException {
		if(xout!=null){
			try{
				xout.flush();
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}
		}
	}

	public void newline() throws IoxException {
		if(nl==null)nl=System.getProperty("line.separator");
		try{
			xout.writeCharacters(nl);
		}catch(XMLStreamException ex){
			throw new IoxException(ex);
		}
	}

}