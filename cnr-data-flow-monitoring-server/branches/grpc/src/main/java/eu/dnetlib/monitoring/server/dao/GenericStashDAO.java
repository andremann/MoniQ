package eu.dnetlib.monitoring.server.dao;

import java.util.List;

import eu.dnetlib.monitoring.rmi.Observation;

public interface GenericStashDAO {

	boolean create(Observation record);

	void createBatchObservations(List<Observation> observations);

	Observation getById(String id);

	List<String> listMetrics(String scenario);

	List<Observation> findObservations(String scenario, String metric); // TODO unify methods down here

	List<Observation> findObservations(String scenario, String metric, String labelName);

	List<Observation> findKLastObservations(String scenario, String metric, int k);

	List<Observation> findKLastObservations(String scenario, String metric, String labelName, int k);

	List<Observation> findKLastObservations(String scenario, String metric, String labelName, String labelValue, int k);

	int countObservations(String scenario);

}
