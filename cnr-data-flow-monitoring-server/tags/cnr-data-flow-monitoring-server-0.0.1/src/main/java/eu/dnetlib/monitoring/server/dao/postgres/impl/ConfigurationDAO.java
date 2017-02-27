package eu.dnetlib.monitoring.server.dao.postgres.impl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import eu.dnetlib.monitoring.model.SensorConfiguration;
import eu.dnetlib.monitoring.server.dao.GenericConfigurationDAO;

public class ConfigurationDAO implements GenericConfigurationDAO {

	private static final Log log = LogFactory.getLog(ConfigurationDAO.class);

	private JdbcTemplate jdbcTemplate;
	private TransactionTemplate transactionTemplate;

	@Override
	public List<SensorConfiguration> listConfigurations(final String scenario) {
		log.info("List configurations for scenario " + scenario);
		String query = "SELECT params FROM configurations WHERE params->>'monitoringScenario' = ?";
		return queryForConfigurationList(query, scenario);
	}

	@Override
	public SensorConfiguration getConfiguration(final String scenario, final String configurationName) {
		log.info("Get configurations " + scenario + "/" + configurationName);
		String query = "SELECT params FROM configurations WHERE params->>'monitoringScenario' = ? AND name = ?";
		return queryForConfigurationList(query, scenario, configurationName).get(0);
	}

	private List<SensorConfiguration> queryForConfigurationList(final String query, final Object... queryParams) {
		List<SensorConfiguration> results = Lists.newArrayList();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, queryParams);
		for (Map<?, ?> row : rows) {
			results.add(new SensorConfiguration(PGJsonToMap(row.get("params"))));
		}
		return results;
	}

	private Map<String, Object> PGJsonToMap(final Object obj) {
		Type listType = new TypeToken<Map<String, Object>>() {}.getType();
		if (obj != null) {
			return new Gson().fromJson(((PGobject) obj).getValue(), listType);
		} else {
			return new HashMap<String, Object>();
		}
	}

	@Override
	public List<String> listMetrics(final String scenario) {
		List<String> queryForList =
				jdbcTemplate
						.queryForList(
								"SELECT DISTINCT json_object_keys(params->'querySet') AS m FROM configurations WHERE params->>'monitoringScenario'=? ORDER BY m ASC ;",
								String.class, scenario);
		return queryForList;
	}

	@Override
	public List<String> listLabelNames(final String scenario, final String metric) {
		List<String> queryForList =
				jdbcTemplate
				.queryForList(
						"SELECT DISTINCT json_object_keys(params->'labels') AS l FROM configurations WHERE params->>'monitoringScenario'=? AND params->'querySet'->>? != '' ORDER BY l ASC;",
						String.class, scenario, metric);
		return queryForList;
	}

	@Override
	public List<String> listLabelValues(final String scenario, final String metric, final String labenName) {
		List<String> queryForList =
				jdbcTemplate
				.queryForList(
						"SELECT DISTINCT params->'labels'->>? AS v FROM configurations WHERE params->>'monitoringScenario'=? AND params->'querySet'->>? != '' ORDER BY v ASC;",
						String.class, labenName, scenario, metric);
		return queryForList;
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
