package eu.dnetlib.monitoring.server.dao.postgres.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

import eu.dnetlib.monitoring.rmi.Observation;
import eu.dnetlib.monitoring.server.dao.GenericStashDAO;

public class ObservationDAO implements GenericStashDAO {

	private static final Log log = LogFactory.getLog(ObservationDAO.class);

	private JdbcTemplate jdbcTemplate;
	private TransactionTemplate transactionTemplate;

	@Override
	public boolean create(final Observation observation) {
		log.debug("Serving stash record from " + observation.getSensorType());
		// if (observation.getLog() == 0) { return false; }
		try {
			jdbcTemplate.update(
					"INSERT INTO logstash(TimeMarker, Metric, Labels, SensorType, SensorId, Log, MonitoringScenario) VALUES (?, ?, ?, ?, ?, ?, ?)",
					new Timestamp(observation.getTimestamp()),
					observation.getMetric(),
					MapToPGJson(observation.getLabels()),
					observation.getSensorType(),
					observation.getSensorId(),
					observation.getLog(),
					observation.getMonitoringScenario());
		} catch (SQLException e) {
			log.error("Error during labels conversion", e);
			return false;
		}
		return true;
	}

	@Override
	public void createBatchObservations(final List<Observation> observations) {
		log.info("Creating batch of observations");
		String sql = "INSERT INTO logstash(TimeMarker, Metric, Labels, SensorType, SensorId, Log, MonitoringScenario) VALUES (?, ?, ?, ?, ?, ?, ?)";

		getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(final PreparedStatement ps, final int i) throws SQLException {
				Observation observation = observations.get(i);
				ps.setObject(1, new Timestamp(observation.getTimestamp()));
				ps.setString(2, observation.getMetric());
				ps.setObject(3, MapToPGJson(observation.getLabels()));
				ps.setString(4, observation.getSensorType());
				ps.setString(5, observation.getSensorId());
				ps.setDouble(6, observation.getLog());
				ps.setString(7, observation.getMonitoringScenario());
			}

			@Override
			public int getBatchSize() {
				return observations.size();
			}
		});
	}

	private PGobject MapToPGJson(final Map map) throws SQLException {
		PGobject jsonObject = new PGobject();
		jsonObject.setType("json");
		jsonObject.setValue(new Gson().toJson(map));
		return jsonObject;
	}

	private Map PGJsonToMap(final Object obj) {
		if (obj != null) {
			return new Gson().fromJson(((PGobject) obj).getValue(), Map.class);
		} else {
			return new HashMap();
		}
	}

	@Override
	public Observation getById(final String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Observation> findObservations(final String scenario, final String metric) {
		String query = "SELECT * FROM logstash WHERE monitoringscenario = ? AND metric = ?";
		return queryForObservationList(query, scenario, metric);
	}

	@Override
	public List<Observation> findObservations(final String scenario, final String metric, final String labelName) {
		String query = "SELECT * FROM logstash WHERE monitoringscenario = ? AND metric = ? AND labels ->> ? != ''";
		return queryForObservationList(query, scenario, metric, labelName);
	}

	@Override
	public List<String> listMetrics(final String scenario) {
		List<String> queryForList =
				jdbcTemplate.queryForList(
						"SELECT DISTINCT metric FROM logstash WHERE monitoringscenario = '{s}' ORDER BY metric ASC;".replace("{s}", scenario),
						String.class);
		return queryForList;
	}

	@Override
	public List<Observation> findKLastObservations(final String scenario, final String metric, final int k) {
		String query =
				"SELECT * from logstash where monitoringscenario = ? AND metric = ? and timemarker in (SELECT DISTINCT timemarker FROM logstash ORDER BY timemarker DESC LIMIT ?) ORDER BY timemarker ASC";
		return queryForObservationList(query, scenario, metric, k);
	}

	@Override
	public List<Observation> findKLastObservations(final String scenario, final String metric, final String labelName, final int k) {
		String query =
				"SELECT * FROM logstash WHERE monitoringscenario = ? AND metric = ? AND labels ->> ? != '' AND timemarker IN (SELECT DISTINCT timemarker FROM logstash ORDER BY timemarker DESC LIMIT ?) ORDER BY timemarker ASC";
		return queryForObservationList(query, scenario, metric, labelName, k);
	}

	@Override
	public List<Observation> findKLastObservations(final String scenario, final String metric, final String labelName, final String labelVelue, final int k) {
		String query =
				"SELECT * FROM logstash WHERE monitoringscenario = ? AND metric = ? AND labels ->> ? = ? AND timemarker IN (SELECT DISTINCT timemarker FROM logstash ORDER BY timemarker DESC LIMIT ?) ORDER BY timemarker ASC";
		return queryForObservationList(query, scenario, metric, labelName, labelVelue, k);
	}

	private List<Observation> queryForObservationList(final String query, final Object... params) {
		List<Observation> results = Lists.newArrayList();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, params);
		for (Map<?, ?> row : rows) {
			Observation.Builder obs = Observation.newBuilder();
			obs.setTimestamp(((Timestamp) row.get("TimeMarker")).getTime());
			obs.setMetric((String) row.get("Metric"));
			obs.setLog((Double) row.get("Log"));
			obs.setSensorType((String) row.get("SensorType"));
			obs.setSensorId((String) row.get("SensorId"));
			obs.getMutableLabels().putAll(PGJsonToMap(row.get("Labels")));
			obs.setMonitoringScenario((String) row.get("monitoringScenario"));
			results.add(obs.build());
		}
		return results;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Required
	public void setJdbcTemplate(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	@Required
	public void setTransactionTemplate(final TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	@Override
	public int countObservations(final String scenario) {
		return jdbcTemplate.queryForObject("SELECT count(id) FROM logstash WHERE monitoringscenario = ?", Integer.class, scenario);
	}

}
