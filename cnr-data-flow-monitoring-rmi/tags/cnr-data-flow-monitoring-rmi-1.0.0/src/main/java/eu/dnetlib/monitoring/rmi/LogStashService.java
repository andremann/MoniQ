package eu.dnetlib.monitoring.rmi;

import eu.dnetlib.monitoring.model.Observation;

public interface LogStashService {

	/**
	 * Save a record into DFM backend
	 * @param observation
	 * @return
	 */
	public boolean storeObservation(Observation observation);

}
