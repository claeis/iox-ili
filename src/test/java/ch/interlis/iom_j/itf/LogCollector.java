package ch.interlis.iom_j.itf;

import java.util.ArrayList;

import ch.ehi.basics.logging.LogEvent;


public class LogCollector implements ch.ehi.basics.logging.LogListener {
	private ArrayList<LogEvent> errs=new ArrayList<LogEvent>();


	@Override
	public void logEvent(LogEvent event) {
		errs.add(event);
	}
	
	public ArrayList<LogEvent> getErrs() {
		return errs;
	}



}
