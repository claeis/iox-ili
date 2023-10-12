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
	private ArrayList<IoxLogEvent> warn=new ArrayList<IoxLogEvent>();
	private ArrayList<IoxLogEvent> info=new ArrayList<IoxLogEvent>();

	@Override
	public void addEvent(IoxLogEvent event) {
		EhiLogger.getInstance().logEvent((LogEvent) event);
		switch (event.getEventKind()) {
			case IoxLogEvent.ERROR:
				errs.add(event);
				break;
			case IoxLogEvent.WARNING:
				warn.add(event);
				break;
			case IoxLogEvent.INFO:
				info.add(event);
				break;
		}
	}

	public ArrayList<IoxLogEvent> getErrs() {
		return errs;
	}
	
	public ArrayList<IoxLogEvent> getWarn() {
		return warn;
	}

	public ArrayList<IoxLogEvent> getInfo() {
		return info;
	}

	public void clear() {
		errs.clear();
		warn.clear();
		info.clear();
	}
}
