package eu.dnetlib.monitoring.core.sensors;

import java.util.UUID;

import eu.dnetlib.monitoring.core.connection.DataFlowMonitoringClient;
import eu.dnetlib.monitoring.model.SensorConfiguration;

public abstract class Sensor {

	protected String id = "sId-" + UUID.randomUUID();

	private DataFlowMonitoringClient client;

	private SensorConfiguration configuration;

	protected DataFlowMonitoringClient getClient() {
		return client;
	}

	protected void setClient(final DataFlowMonitoringClient client) {
		this.client = client;
	}

	public void setConfiguration(final SensorConfiguration config) {
		this.configuration = config;
	}

	public SensorConfiguration getConfiguration() {
		return this.configuration;
	}

	protected abstract void init();

}
