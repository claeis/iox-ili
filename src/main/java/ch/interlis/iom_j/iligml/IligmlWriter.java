package ch.interlis.iom_j.iligml;

import java.io.File;
import java.io.OutputStreamWriter;

import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iox.IoxException;

public class IligmlWriter extends Iligml10Writer {

	public IligmlWriter(OutputStreamWriter buffer, TransferDescription td)
			throws IoxException {
		super(buffer, td);
	}

	public IligmlWriter(File buffer, TransferDescription td)
			throws IoxException {
		super(buffer, td);
	}

}
