package eu.dnetlib.monitoring.core.sensors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.dnetlib.monitoring.core.connection.DataFlowMonitoringClient;
import eu.dnetlib.monitoring.model.SensorConfiguration;

public class SensorFactory {

	private static final Log log = LogFactory.getLog(SensorFactory.class);

	private static DataFlowMonitoringClient dataFlowMonitoringClient;

	public static Sensor createSensor(final Class<? extends Sensor> clazz) throws InstantiationException, IllegalAccessException {
		if (dataFlowMonitoringClient == null) {
			log.info("Creating dataFlowMonitoringClient");
			dataFlowMonitoringClient = new DataFlowMonitoringClient();
		}

		Sensor sensor = clazz.newInstance();
		sensor.setClient(dataFlowMonitoringClient);
		return sensor;
	}

	public static Sensor createSensorWithConfiguration(
			final Class<? extends Sensor> clazz,
			final String scenario, final String configurationName) throws InstantiationException, IllegalAccessException, SensorCreationException {
		if (dataFlowMonitoringClient == null) {
			log.info("Creating dataFlowMonitoringClient");
			dataFlowMonitoringClient = new DataFlowMonitoringClient();
		}

		Sensor sensor = clazz.newInstance();
		sensor.setClient(dataFlowMonitoringClient);
		SensorConfiguration askConfiguration;
		askConfiguration = sensor.getClient().askConfiguration(scenario, configurationName);
		sensor.setConfiguration(askConfiguration);
		sensor.init();
		return sensor;
	}
}
