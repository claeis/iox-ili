package ch.interlis.iox_j.validator;

import java.util.ArrayList;
import java.util.Collection;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.logging.LogEvent;
import ch.interlis.ili2c.metamodel.Container;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.iox.IoxLogEvent;


public class LogCollector implements ch.interlis.iox.IoxLogging {
	private ArrayList<IoxLogEvent> errs=new ArrayList<IoxLogEvent>();

	@Override
	public void addEvent(IoxLogEvent event) {
		EhiLogger.getInstance().logEvent((LogEvent) event);
		errs.add(event);
	}

	public ArrayList<IoxLogEvent> getErrs() {
		return errs;
	}


}
