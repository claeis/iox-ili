package ch.interlis.iom_j.xtf;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iox.IoxReader;

public class Xtf24Reader implements IoxReader {
	private java.io.InputStream inputFile=null;
	private javax.xml.stream.XMLEventReader reader=null;
	private IoxFactoryCollection factory=new  ch.interlis.iox_j.DefaultIoxFactoryCollection();
	private TransferDescription td=null;
	public Xtf24Reader(java.io.InputStream in)
	throws IoxException
	{
		init(in);
	}
	
	/** Creates a new reader.
	 * @param in Input reader to read from
	 * @throws IoxException
	 */
	public Xtf24Reader(java.io.InputStreamReader in)
	throws IoxException
	{
	}
	
	/** Creates a new reader.
	 * @param xtffile File to read from
	 * @throws IoxException
	 */
	public Xtf24Reader(java.io.File xtffile)
	throws IoxException
	{
		try{
			inputFile=new java.io.FileInputStream(xtffile);
			init(inputFile);
		}catch(java.io.IOException ex){
			throw new IoxException(ex);
		}
	}
	
	private void init(java.io.InputStream in) 
		throws IoxException 
	{
		javax.xml.stream.XMLInputFactory inputFactory = javax.xml.stream.XMLInputFactory.newInstance();
		try{
			reader=inputFactory.createXMLEventReader(in);
		}catch(javax.xml.stream.XMLStreamException ex){
			throw new IoxException(ex);
		}
	}
	public void setModel(TransferDescription td)
	{
		this.td=td;
	}

	@Override
	public void close() throws IoxException {
		reader=null;
		if(inputFile!=null){
			try{
				inputFile.close();
			}catch(java.io.IOException ex){
				throw new IoxException(ex);
			}
			inputFile=null;
		}
	}

	@Override
	public IoxEvent read() throws IoxException {
		while(reader.hasNext()){
			javax.xml.stream.events.XMLEvent event=null;
			try{
				event=reader.nextEvent();
			}catch(javax.xml.stream.XMLStreamException ex){
				throw new IoxException(ex);
			}
			//EhiLogger.debug(event.toString());
			if(event instanceof StartElement){
				// TODO
			}else if(event instanceof EndElement){
				// TODO
			}else if(event instanceof Characters){
				// TODO
			}else{
				// TODO
			}
		}
		return null;
	}
	
	@Override
	public IomObject createIomObject(String type, String oid)
			throws IoxException {
		return factory.createIomObject(type, oid);
	}

	@Override
	public IoxFactoryCollection getFactory() throws IoxException {
		return factory;
	}

	@Override
	public void setFactory(IoxFactoryCollection factory) throws IoxException {
		this.factory=factory;
	}

}
