package eu.dnetlib.monitoring.scenarios;

public class Scenario {

	private String name;
	private String description;
	private String status;
	private long lastModified;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(final long lastModified) {
		this.lastModified = lastModified;
	}
}
