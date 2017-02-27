package eu.dnetlib.monitoring.model;

import java.io.Serializable;

public class SensorConfiguration implements Serializable {

	private int id;
	private String name;
	private Params params;
	private long lastModified;
	private String status;

	public SensorConfiguration() {
		params = new Params();
	}

	public Params getParams() {
		return params;
	}

	public void setParams(final Params params) {
		this.params = params;
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
