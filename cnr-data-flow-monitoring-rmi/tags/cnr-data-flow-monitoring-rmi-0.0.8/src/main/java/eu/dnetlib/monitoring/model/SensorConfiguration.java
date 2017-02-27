package eu.dnetlib.monitoring.model;

import java.util.HashMap;
import java.util.Map;

public class SensorConfiguration {

	private int id;
	private String name;
	private Map<String, Object> params;
	private long lastModified;
	private String status;

	public SensorConfiguration() {
		params = new HashMap<String, Object>();
	}

	public SensorConfiguration(final Map<String, Object> map) {
		params = new HashMap<String, Object>();
		params.putAll(map);
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public SensorConfiguration setParam(final String name, final Object obj) {
		params.put(name, obj);
		return this;
	}

	public Object getParam(final String name) {
		return params.get(name);
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(final long lastModified) {
		this.lastModified = lastModified;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

}
