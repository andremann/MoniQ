package eu.dnetlib.monitoring.core.sensors;

import java.util.UUID;

import eu.dnetlib.monitoring.core.client.DataFlowMonitoringGrpcClient;
import eu.dnetlib.monitoring.model.SensorConfiguration;

public abstract class Sensor {

	protected String id = "sId-" + UUID.randomUUID();

	private DataFlowMonitoringGrpcClient client;

	private SensorConfiguration configuration;

	protected DataFlowMonitoringGrpcClient getClient() {
		return client;
	}

	protected void setClient(final DataFlowMonitoringGrpcClient client) {
		this.client = client;
	}

	public void setConfiguration(final SensorConfiguration config) {
		this.configuration = config;
	}

	public SensorConfiguration getConfiguration() {
		return this.configuration;
	}

	protected abstract void init();

	public void reinitialize() {
		init();
	}

}
