package eu.dnetlib.monitoring.model;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Created by andrea on 21/06/16.
 */
public class Metric implements Serializable {

	private String name;
	private String description;
	private Map<String, String> labels;
	private Map<String, String> params;

	public Metric() {
		labels = Maps.newHashMap();
		params = Maps.newHashMap();
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Map<String, String> getLabels() {
		return labels;
	}

	public void setLabels(final Map labels) {
		this.labels = labels;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(final Map params) {
		this.params = params;
	}
}
