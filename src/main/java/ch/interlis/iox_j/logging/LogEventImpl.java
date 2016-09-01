package ch.interlis.iox_j.logging;

import java.util.Date;

import ch.ehi.basics.logging.LogEvent;
import ch.interlis.iox.IoxLogEvent;

public class LogEventImpl implements IoxLogEvent, ch.ehi.basics.logging.LogEvent {

	public LogEventImpl(String dataSource, Date eventDateTime, String eventId,
			int eventKind, String eventMsg, Throwable exception,
			Integer sourceLineNr, String sourceObjectTag,
			String sourceObjectTechId, String sourceObjectUsrId,
			String sourceObjectXtfId, String modelEleQName,
			Double geomC1, Double geomC2, Double geomC3,
			StackTraceElement origin) {
		super();
		this.dataSource = dataSource;
		this.eventDateTime = eventDateTime;
		this.eventId = eventId;
		this.eventKind = eventKind;
		this.eventMsg = eventMsg;
		this.exception = exception;
		this.sourceLineNr = sourceLineNr;
		this.sourceObjectTag = sourceObjectTag;
		this.sourceObjectTechId = sourceObjectTechId;
		this.sourceObjectUsrId = sourceObjectUsrId;
		this.sourceObjectXtfId = sourceObjectXtfId;
		this.origin = origin;
		this.modelEleQName=modelEleQName;
		this.geomC1=geomC1;
		this.geomC2=geomC2;
		this.geomC3=geomC3;
	}

	private String dataSource=null;
	private Date eventDateTime=null;
	private String eventId=null;
	private int eventKind=LogEvent.ERROR;
	private String eventMsg=null;
	private Throwable exception=null;
	private Integer sourceLineNr=null;
	private String sourceObjectTag=null;
	private String sourceObjectTechId=null;
	private String sourceObjectUsrId=null;
	private String sourceObjectXtfId=null;
	private StackTraceElement origin=null;
	private String modelEleQName=null;
	private Double geomC1=null;
	private Double geomC2=null;
	private Double geomC3=null;
	@Override
	public String getDataSource() {
		return dataSource;
	}

	@Override
	public Date getEventDateTime() {
		return eventDateTime;
	}

	@Override
	public String getEventId() {
		return eventId;
	}

	@Override
	public int getEventKind() {
		return eventKind;
	}

	@Override
	public String getEventMsg() {
		return eventMsg;
	}
	@Override
	public String getRawEventMsg() {
		return eventMsg;
	}

	@Override
	public Throwable getException() {
		return exception;
	}

	@Override
	public StackTraceElement getOrigin() {
		return origin;
	}

	@Override
	public Integer getSourceLineNr() {
		return sourceLineNr;
	}

	@Override
	public String getSourceObjectTag() {
		return sourceObjectTag;
	}

	@Override
	public String getSourceObjectTechId() {
		return sourceObjectTechId;
	}

	@Override
	public String getSourceObjectUsrId() {
		return sourceObjectUsrId;
	}

	@Override
	public String getSourceObjectXtfId() {
		return sourceObjectXtfId;
	}

	@Override
	public int getCustomLevel() {
		return 0;
	}

	@Override
	public String getModelEleQName() {
		return modelEleQName;
	}

	@Override
	public Double getGeomC1() {
		return geomC1;
	}

	@Override
	public Double getGeomC2() {
		return geomC2;
	}

	@Override
	public Double getGeomC3() {
		return geomC3;
	}


}
