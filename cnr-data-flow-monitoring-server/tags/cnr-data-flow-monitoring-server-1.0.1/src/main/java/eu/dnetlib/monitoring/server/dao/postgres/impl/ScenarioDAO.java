package eu.dnetlib.monitoring.server.dao.postgres.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import eu.dnetlib.monitoring.scenarios.Scenario;
import eu.dnetlib.monitoring.server.dao.GenericScenarioDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

public class ScenarioDAO implements GenericScenarioDAO {

	private static final Log log = LogFactory.getLog(ScenarioDAO.class);

	private JdbcTemplate jdbcTemplate;
	private TransactionTemplate transactionTemplate;

	@Override
	public List<Scenario> listScenarios() {
		log.debug("List scenarios");
		List<Scenario> results = Lists.newArrayList();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM Scenarios");
		for (Map<?, ?> row : rows) {
			Scenario s = new Scenario();
			s.setName((String) row.get("name"));
			s.setDescription((String) row.get("description"));
			s.setStatus((String) row.get("status"));
			s.setLastModified(((Timestamp) row.get("lastmodified")).getTime());
			results.add(s);
		}
		return results;
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
