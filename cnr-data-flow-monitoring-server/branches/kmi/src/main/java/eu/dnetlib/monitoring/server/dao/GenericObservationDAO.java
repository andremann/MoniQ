package eu.dnetlib.monitoring.server.dao;

import java.util.List;
import java.util.Map;

import eu.dnetlib.monitoring.model.Observation;

public interface GenericObservationDAO {

	boolean create(Observation record);

	Observation getById(String id);

	List<Observation> findObservations(String scenario, String metric); // TODO unify methods down here

	List<Observation> findObservations(String scenario, String metric, String labelName);

	List<Observation> findKLastObservations(String scenario, String metric, int k);

	List<Observation> findKLastObservations(String scenario, String metric, String labelName, int k);

	List<Observation> findKLastObservations(String scenario, String metric, String labelName, String labelValue, int k);

	List<Observation> findKLastObservations(String scenario, String metric, Map<String, String> labelsConditions, int k);

	int countObservations(String scenario);

	List<String> listTrackedMetrics(String scenario);

}
