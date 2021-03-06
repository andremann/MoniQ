package eu.dnetlib.monitoring.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;

public class Observation implements Serializable {

	private long timestamp;
	private String monitoringScenario;
	private String sensorType;
	private String sensorId;
	private String metric;
	private Double value;
	private Map labels = Maps.newHashMap();

	public Observation() {
		setTimestamp(System.currentTimeMillis());
	}

	public Observation(final long timeMillis) {
		setTimestamp(timeMillis);
	}

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(final String sensorId) {
		this.sensorId = sensorId;
	}

	public String getSensorType() {
		return sensorType;
	}

	public void setSensorType(final String sensorType) {
		this.sensorType = sensorType;
	}

	public String getMetric() {
		return metric;
	}

	public void setMetric(final String metric) {
		this.metric = metric;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(final Double value) {
		this.value = value;
	}

	public Map<String, String> getLabels() {
		return labels;
	}

	public void setLabels(final Map<String, String> labels) {
		this.labels = new HashMap<String, String>(labels);
	}

	public String getMonitoringScenario() {
		return monitoringScenario;
	}

	public void setMonitoringScenario(final String monitoringScenario) {
		this.monitoringScenario = monitoringScenario;
	}

	@Override
	public String toString() {
		return "Observation{" +
				"timestamp=" + timestamp +
				", monitoringScenario='" + monitoringScenario + '\'' +
				", sensorType='" + sensorType + '\'' +
				", sensorId='" + sensorId + '\'' +
				", metric='" + metric + '\'' +
				", value=" + value +
				", labels=" + labels +
				'}';
	}
}
