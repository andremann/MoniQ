package eu.dnetlib.monitoring.rmi;

import eu.dnetlib.monitoring.model.Observation;

public interface LogStashService {

	/**
	 * Put a record into the stash
	 *
	 * @param record
	 * @return
	 */
	public boolean stashRecord(Observation record);

}
