package eu.dnetlib.monitoring.core.exceptions;

public class SensorCreationException extends Exception {

	private static final long serialVersionUID = -2992912899730311918L;

	public SensorCreationException(final String message) {
		super(message);
	}

	public SensorCreationException(final String message, final Throwable e) {
		super(message, e);
	}

	public SensorCreationException(final String scenario, final String configurationName, final Throwable e) {
		super(String.format("Cannot create a sensor with configuration %s for scenario %s", configurationName, scenario), e);
	}

}
