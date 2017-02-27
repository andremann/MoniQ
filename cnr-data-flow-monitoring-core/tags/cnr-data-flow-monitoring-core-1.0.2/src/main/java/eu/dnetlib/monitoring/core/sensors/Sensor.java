package eu.dnetlib.monitoring.core.sensors;

import java.util.UUID;

import eu.dnetlib.monitoring.core.connection.DataFlowMonitoringClient;
import eu.dnetlib.monitoring.core.exceptions.SensorInitException;
import eu.dnetlib.monitoring.model.SensorConfiguration;

public abstract class Sensor {

	protected String id = "sId-" + UUID.randomUUID();

	private SensorConfiguration configuration;

	public SensorConfiguration getConfiguration() {
		return this.configuration;
	}

	public void setConfiguration(final SensorConfiguration config) {
		this.configuration = config;
	}

	public DataFlowMonitoringClient getClient() {
		return DataFlowMonitoringClient.getInstance();
	}

	protected abstract void init() throws SensorInitException;

	public void reinitialize() throws SensorInitException {
		init();
	}

}
