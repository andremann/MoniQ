package eu.dnetlib.monitoring.exception;

/**
 * Created by andrea on 26/05/16.
 */
public class DataFlowMonitoringServiceRuntimeException extends RuntimeException {

	public DataFlowMonitoringServiceRuntimeException() {
	}

	public DataFlowMonitoringServiceRuntimeException(final Throwable e) {
		super(e);
	}

	public DataFlowMonitoringServiceRuntimeException(String message, final Throwable e) {
		super(message, e);
	}
}
