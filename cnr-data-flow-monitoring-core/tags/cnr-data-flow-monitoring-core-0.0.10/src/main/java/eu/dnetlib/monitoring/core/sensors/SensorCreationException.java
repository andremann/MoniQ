package eu.dnetlib.monitoring.core.sensors;

public class SensorCreationException extends Exception {

	private static final long serialVersionUID = -2992912899730311918L;

	public SensorCreationException(final String scenario, final String configurationName) {
		super(String.format("Cannot create a sensor with configuration %s for scenario %s", configurationName, scenario));
	}

	public SensorCreationException(final String scenario, final String configurationName, final Throwable e) {
		super(String.format("Cannot create a sensor with configuration %s for scenario %s", configurationName, scenario), e);
	}
}
