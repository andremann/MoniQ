package eu.dnetlib.monitoring.server.dao.postgres.impl;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.dnetlib.monitoring.model.SensorConfiguration;
import eu.dnetlib.monitoring.server.dao.GenericConfigurationDAO;
import eu.dnetlib.monitoring.server.dao.exceptions.ConfigurationNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

public class ConfigurationDAO implements GenericConfigurationDAO {

	private static final Log log = LogFactory.getLog(ConfigurationDAO.class);

	private JdbcTemplate jdbcTemplate;
	private TransactionTemplate transactionTemplate;

	@Override
	public int create(final SensorConfiguration configuration) {
		final String query = "INSERT INTO configurations(name, params, status, lastmodified) VALUES (?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection)
					throws SQLException {
				PreparedStatement ps = connection.prepareStatement(query, new String[] { "id" });
				ps.setString(1, configuration.getName());
				ps.setObject(2, mapToPGJson(configuration.getParams()));
				ps.setString(3, configuration.getStatus());
				ps.setTimestamp(4, new Timestamp(configuration.getLastModified()));
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public int update(final SensorConfiguration configuration) {
		log.debug("Update configuration " + configuration.getId());
		final String query = "UPDATE configurations SET (name, params, status, lastmodified) = (?, ?, ?, ?) WHERE id = ?";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection)
					throws SQLException {
				PreparedStatement ps = connection.prepareStatement(query, new String[] { "id" });
				ps.setString(1, configuration.getName());
				ps.setObject(2, mapToPGJson(configuration.getParams()));
				ps.setString(3, configuration.getStatus());
				ps.setTimestamp(4, new Timestamp(configuration.getLastModified()));
				ps.setInt(5, configuration.getId());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public List<SensorConfiguration> listConfigurations(final String scenario) {
		log.debug("List configurations for scenario " + scenario);
		String query = "SELECT * FROM configurations WHERE params->>'monitoringScenario' = ?";
		return queryForConfigurationList(query, scenario);
	}

	@Override
	public SensorConfiguration getConfiguration(final String scenario, final String configurationName) throws ConfigurationNotFoundException {
		log.debug("Get configurations " + scenario + "/" + configurationName);
		String query = "SELECT * FROM configurations WHERE params->>'monitoringScenario' = ? AND name = ?";
		List<SensorConfiguration> configurations = queryForConfigurationList(query, scenario, configurationName);
		if (configurations.size() == 0) throw new ConfigurationNotFoundException(scenario, configurationName);
		else return configurations.get(0);
	}

	private List<SensorConfiguration> queryForConfigurationList(final String query, final Object... queryParams) {
		List<SensorConfiguration> results = Lists.newArrayList();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, queryParams);
		for (Map<?, ?> row : rows) {
			SensorConfiguration configuration = new SensorConfiguration(pgJsonToMap(row.get("params")));
			configuration.setId((Integer) row.get("id"));
			configuration.setName((String) row.get("name"));
			configuration.setStatus((String) row.get("status"));
			configuration.setLastModified(((Timestamp) row.get("lastmodified")).getTime());
			results.add(configuration);
		}
		return results;
	}

	private Map<String, Object> pgJsonToMap(final Object obj) {
		Type listType = new TypeToken<Map<String, Object>>() {
		}.getType();
		if (obj != null) return new Gson().fromJson(((PGobject) obj).getValue(), listType);
		else return new HashMap<>();
	}

	private PGobject mapToPGJson(final Map map) throws SQLException {
		PGobject jsonObject = new PGobject();
		jsonObject.setType("json");
		jsonObject.setValue(new Gson().toJson(map));
		return jsonObject;
	}

	@Override
	public List<String> listMetrics(final String scenario) {
		log.debug("List metrics for scenario " + scenario);
		return jdbcTemplate
				.queryForList(
						"(SELECT DISTINCT json_object_keys(params->'querySet') AS m FROM configurations WHERE params->>'monitoringScenario'=?) "
								+ "UNION "
								+ "(SELECT DISTINCT params->>'metric' AS m FROM configurations WHERE params->>'monitoringScenario'=? AND (params->>'metric') IS NOT NULL) ORDER BY m ASC",
						String.class, scenario, scenario);
	}

	@Override
	public List<String> listLabelNames(final String scenario, final String metric) {
		log.debug("List label names for scenario " + scenario + " and metric " + metric);
		return jdbcTemplate
				.queryForList(
						"SELECT DISTINCT json_object_keys(params->'labels') AS l FROM configurations WHERE params->>'monitoringScenario'=? ORDER BY l ASC;",
						String.class, scenario);
	}

	@Override
	public List<String> listLabelValues(final String scenario, final String metric, final String labelName) {
		log.debug("List label names for scenario " + scenario + " and metric " + metric + " on label " + labelName);
		return jdbcTemplate
				.queryForList(
						"SELECT DISTINCT params->'labels'->>? AS v FROM configurations WHERE params->>'monitoringScenario'=? ORDER BY v ASC;",
						String.class, labelName, scenario);
	}

	@Override
	public void delete(final int id) {
		log.debug("Deleting configuration id: " + id);
		final String query = "DELETE FROM configurations WHERE id=?";
		jdbcTemplate.update(query, id);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(final TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

}
