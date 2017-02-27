package eu.dnetlib.monitoring.core.sensors;

import java.util.UUID;

import eu.dnetlib.monitoring.core.connection.DataFlowMonitoringClient;
import eu.dnetlib.monitoring.core.exceptions.SensorInitException;
import eu.dnetlib.monitoring.model.Metric;
import eu.dnetlib.monitoring.model.Observation;
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

	protected Observation buildObservation(Metric metric, double value) {
		Observation observation = new Observation();
		observation.setSensorType(this.getClass().getSimpleName());
		observation.setSensorId(getConfiguration().getName());
		observation.setValue(value);
		observation.setMetric(metric.getName());
		observation.setLabels(metric.getLabels());
		observation.addLabels(getConfiguration().getParams().getGlobalLabels());
		observation.setMonitoringScenario(getConfiguration().getParams().getMonitoringScenario());
		return observation;
	}

	protected Observation buildObservation(long currentTimeMillis, Metric metric, double value) {
		Observation observation = new Observation(currentTimeMillis);
		observation.setSensorType(this.getClass().getSimpleName());
		observation.setSensorId(getConfiguration().getName());
		observation.setValue(value);
		observation.setMetric(metric.getName());
		observation.setLabels(metric.getLabels());
		observation.addLabels(getConfiguration().getParams().getGlobalLabels());
		observation.setMonitoringScenario(getConfiguration().getParams().getMonitoringScenario());
		return observation;
	}

}
