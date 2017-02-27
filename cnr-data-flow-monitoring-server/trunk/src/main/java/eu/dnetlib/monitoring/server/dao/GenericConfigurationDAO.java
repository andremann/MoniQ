package eu.dnetlib.monitoring.server.dao;

import java.util.List;

import eu.dnetlib.monitoring.model.SensorConfiguration;
import eu.dnetlib.monitoring.server.dao.exceptions.ConfigurationNotFoundException;

public interface GenericConfigurationDAO {

	List<String> listMetrics(String scenario);

	List<String> listLabelNames(String scenario, String metric);

	List<String> listLabelValues(String scenario, String metric, String labelName);

	SensorConfiguration getConfiguration(String scenario, String configurationName) throws ConfigurationNotFoundException;

	List<SensorConfiguration> listConfigurations(String scenario);

	void delete(int id);

	int create(SensorConfiguration configuration);

	int update(SensorConfiguration configuration);

}
