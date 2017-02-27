package eu.dnetlib.monitoring.core.exceptions;

public class SensorInitException extends Exception {

	private static final long serialVersionUID = -2992912899730311918L;

	public SensorInitException(final String message) {
		super(message);
	}

	public SensorInitException(final String message, final Throwable e) {
		super(message, e);
	}

	public SensorInitException(final String scenario, final String configurationName, final Throwable e) {
		super(String.format("Something went south during the initialization of the sensor with configuration %s for scenario %s", configurationName, scenario),
				e);
	}

}
