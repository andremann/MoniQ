package eu.dnetlib.monitoring.server.dao.exceptions;

public class ConfigurationNotFoundException extends Exception {

	private static final long serialVersionUID = -8579379111105372484L;

	public ConfigurationNotFoundException(final String scenario, final String configurationName) {
		super(String.format("Configuration %s for scenario %s not found", configurationName, scenario));
	}

}
