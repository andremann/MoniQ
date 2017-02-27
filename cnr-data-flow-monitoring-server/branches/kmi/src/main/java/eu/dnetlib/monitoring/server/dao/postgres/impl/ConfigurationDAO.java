package eu.dnetlib.monitoring.server.dao.postgres.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import eu.dnetlib.monitoring.model.Params;
import eu.dnetlib.monitoring.model.SensorConfiguration;
import eu.dnetlib.monitoring.server.dao.GenericConfigurationDAO;
import eu.dnetlib.monitoring.server.dao.exceptions.ConfigurationNotFoundException;
import eu.dnetlib.monitoring.server.dao.grafana.DashboardDAO;
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

	private DashboardDAO dashboardDao;

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
				ps.setObject(2, paramsToPGJson(configuration.getParams()));
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
				ps.setObject(2, paramsToPGJson(configuration.getParams()));
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
			SensorConfiguration configuration = new SensorConfiguration();
			configuration.setId((Integer) row.get("id"));
			configuration.setName((String) row.get("name"));
			configuration.setParams(pgJsonToParams(row.get("params")));
			configuration.setStatus((String) row.get("status"));
			configuration.setLastModified(((Timestamp) row.get("lastmodified")).getTime());
			results.add(configuration);
		}
		return results;
	}

	private Params pgJsonToParams(final Object obj) {
		if (obj != null)
			return new Gson().fromJson(((PGobject) obj).getValue(), Params.class);
		else
			return new Params();
	}

	private PGobject paramsToPGJson(final Params params) throws SQLException {
		PGobject jsonObject = new PGobject();
		jsonObject.setType("json");
		jsonObject.setValue(new Gson().toJson(params));
		return jsonObject;
	}

	@Override
	public List<String> listMetrics(final String scenario) {
		log.debug("List metrics for scenario " + scenario);
		return jdbcTemplate
				.queryForList(
						"(SELECT DISTINCT json_object_keys(params->'metrics') AS m FROM configurations WHERE params->>'monitoringScenario'=?)",
						String.class, scenario);
	}

	@Override
	public List<String> listLabelNames(final String scenario, final String metric) {
		log.debug("List label names for scenario " + scenario + " and metric " + metric);
		return jdbcTemplate
				.queryForList(
						"SELECT DISTINCT json_object_keys FROM "
								+ "(SELECT json_object_keys(params->'metrics'->?->'labels') FROM configurations WHERE params->>'monitoringScenario'=? "
								+ "UNION "
								+ "SELECT json_object_keys(params->'globalLabels') FROM configurations WHERE params->>'monitoringScenario'=? AND params->'metrics'->?->>'name' != '') AS t ORDER BY json_object_keys ASC",
						String.class, metric, scenario, scenario, metric);
	}

	@Override
	public List<String> listLabelValues(final String scenario, final String metric, final String labelName) {
		log.debug("List label names for scenario " + scenario + " and metric " + metric + " on label " + labelName);
		return jdbcTemplate
				.queryForList(
						"SELECT DISTINCT l FROM "
								+ "(SELECT params->'metrics'->?->'labels'->>? AS l FROM configurations WHERE params->>'monitoringScenario'=? "
								+ "UNION "
								+ "SELECT params->'globalLabels'->>? AS l FROM configurations WHERE params->>'monitoringScenario'=?) AS t ORDER BY l ASC",
						String.class, metric, labelName, scenario, labelName, scenario);
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

	public DashboardDAO getDashboardDao() {
		return dashboardDao;
	}

	public void setDashboardDao(final DashboardDAO dashboardDao) {
		this.dashboardDao = dashboardDao;
	}
}
