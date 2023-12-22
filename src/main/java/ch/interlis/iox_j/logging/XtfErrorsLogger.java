package ch.interlis.iox_j.logging;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.interlis2.validator.models.ILIVERRORS;
import org.interlis2.validator.models.IliVErrors.ErrorLog.Error_Type;

import ch.ehi.basics.logging.AbstractStdListener;
import ch.ehi.basics.logging.LogEvent;
import ch.ehi.basics.logging.LogListener;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iom_j.xtf.XtfModel;
import ch.interlis.iom_j.xtf.XtfWriterBase;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxLogEvent;
import ch.interlis.iox.IoxWriter;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;

public class XtfErrorsLogger extends AbstractXtfErrorsLogger {
    IoxWriter out=null;
	public XtfErrorsLogger(File logFile,String sender)
	{
		try {
			out=new XtfWriterBase(logFile, ILIVERRORS.getIoxMapping(), "2.3");
			((XtfWriterBase)out).setModels(new XtfModel[]{ILIVERRORS.getXtfModel()});
			StartTransferEvent startTransferEvent = new StartTransferEvent();
			startTransferEvent.setSender(sender);
			out.write(startTransferEvent);
			StartBasketEvent startBasketEvent = new StartBasketEvent( ILIVERRORS.ErrorLog, "b1" );
			out.write( startBasketEvent );
		} catch (IoxException e) {
			throw new IllegalStateException(e);
		}
	}
	@Override
    public void writeObject(org.interlis2.validator.models.IliVErrors.ErrorLog.Error iomObj) {
        try {
			out.write(new ObjectEvent(iomObj));
		} catch (IoxException e) {
			throw new IllegalStateException(e);
		}
    }
	@Override
	public void close()
	{
		if(out!=null){
			try {
				out.write( new EndBasketEvent() );
				out.write( new EndTransferEvent() );
				out.flush();
				out.close();
			} catch (IoxException e) {
				throw new IllegalStateException(e);
			}
			out=null;
		}
	}
}
