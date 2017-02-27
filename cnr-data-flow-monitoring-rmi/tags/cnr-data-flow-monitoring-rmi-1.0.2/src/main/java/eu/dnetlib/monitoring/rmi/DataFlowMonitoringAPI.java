package eu.dnetlib.monitoring.rmi;

import eu.dnetlib.monitoring.model.Observation;
import eu.dnetlib.monitoring.model.SensorConfiguration;

public interface DataFlowMonitoringAPI {

	/**
	 * Save a record into DFM backend
	 * @param observation
	 * @return
	 */
	boolean storeObservation(Observation observation);

	SensorConfiguration getConfigurationByName(final String scenario, final String name);

	String getConfigurationTemplateByName(String scenario, String name);
}
