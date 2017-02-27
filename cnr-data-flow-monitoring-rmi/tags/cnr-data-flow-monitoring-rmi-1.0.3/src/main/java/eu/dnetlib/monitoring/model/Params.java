package eu.dnetlib.monitoring.model;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Created by andrea on 21/06/16.
 */
public class Params implements Serializable {

	private String monitoringScenario;
	private Map<String, String> globalParams;
	private Map<String, String> globalLabels;
	private Map<String, Metric> metrics;

	public Params() {
		globalLabels = Maps.newHashMap();
		globalLabels = Maps.newHashMap();
		metrics = Maps.newHashMap();
	}

	public String getMonitoringScenario() {
		return monitoringScenario;
	}

	public void setMonitoringScenario(final String monitoringScenario) {
		this.monitoringScenario = monitoringScenario;
	}

	public Map<String, String> getGlobalParams() {
		return globalParams;
	}

	public void setGlobalParams(final Map globalParams) {
		this.globalParams = globalParams;
	}

	public Map<String, String> getGlobalLabels() {
		return globalLabels;
	}

	public void setGlobalLabels(final Map globalLabels) {
		this.globalLabels = globalLabels;
	}

	public Map<String, Metric> getMetrics() {
		return metrics;
	}

	public void setMetrics(final Map<String, Metric> metrics) {
		this.metrics = metrics;
	}
}
