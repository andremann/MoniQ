package eu.dnetlib.monitoring.core.exceptions;

public class MonitoringException extends RuntimeException {

	private static final long serialVersionUID = -6746764747800506203L;

	public MonitoringException(final String msg) {
		super(msg);
	}

	public MonitoringException(final String msg, final Throwable e) {
		super(msg, e);
	}

}
