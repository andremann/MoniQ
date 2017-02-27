package eu.dnetlib.monitoring.core.sensors;

import eu.dnetlib.monitoring.core.exceptions.SensorCreationException;
import eu.dnetlib.monitoring.model.SensorConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SensorFactory {

	private static final Log log = LogFactory.getLog(SensorFactory.class);

	public static Sensor createSensor(final Class<? extends Sensor> clazz) throws InstantiationException, IllegalAccessException {

		Sensor sensor = clazz.newInstance();
		return sensor;
	}

	public static Sensor createSensorWithConfiguration(
			final Class<? extends Sensor> clazz,
			final String scenario, final String configurationName) throws InstantiationException, IllegalAccessException, SensorCreationException {

		Sensor sensor = clazz.newInstance();
		SensorConfiguration configuration = sensor.getClient().askConfiguration(scenario, configurationName);
		sensor.setConfiguration(configuration);
		sensor.init();
		return sensor;
	}

}
