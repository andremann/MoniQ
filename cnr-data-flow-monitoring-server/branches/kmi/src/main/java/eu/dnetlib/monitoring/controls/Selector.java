package eu.dnetlib.monitoring.controls;

import java.util.Map;

import com.google.common.collect.Maps;

public class Selector {

	private String metric;
	private Map<String, String> labelsConditions;
	private Integer samples;

	public Selector() {
	}

	public Selector(final String metric, final String labelName, final String labelValue) {
		this.metric = metric;
		labelsConditions = Maps.newHashMap();
	}

	public String getMetric() {
		return metric;
	}

	public void setMetric(final String metric) {
		this.metric = metric;
	}

	public Integer getSamples() {
		return samples;
	}

	public void setSamples(final Integer samples) {
		this.samples = samples;
	}

	public Map<String, String> getLabelsConditions() {
		return labelsConditions;
	}

	public void setLabelsConditions(final Map<String, String> labelsConditions) {
		this.labelsConditions = labelsConditions;
	}
}
