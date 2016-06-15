package ch.interlis.iox_j.logging;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.logging.LogEvent;
import ch.interlis.iox.IoxLogEvent;


public class Log2EhiLogger implements ch.interlis.iox.IoxLogging {


	@Override
	public void addEvent(IoxLogEvent event) {
		EhiLogger.getInstance().logEvent((LogEvent) event);
	}


}
