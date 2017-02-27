package eu.dnetlib.monitoring.server.dao.postgres.impl;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import eu.dnetlib.monitoring.controls.Control;
import eu.dnetlib.monitoring.controls.Selector;
import eu.dnetlib.monitoring.server.dao.GenericControlDAO;

public class ControlDAO implements GenericControlDAO {

	private static final Log log = LogFactory.getLog(ControlDAO.class);

	private JdbcTemplate jdbcTemplate;
	private TransactionTemplate transactionTemplate;

	@Override
	public int create(final Control control) {
		final String query = "INSERT INTO controls(name, monitoringscenario, selectors, analyzerclass, status, lastmodified) VALUES (?, ?, ?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection)
					throws SQLException {
				PreparedStatement ps = connection.prepareStatement(query, new String[] { "id" });
				ps.setString(1, control.getName());
				ps.setString(2, control.getMonitoringScenario());
				ps.setObject(3, listToPGJson(control.getSelectors()));
				ps.setString(4, control.getAnalyzerClass());
				ps.setString(5, control.getStatus());
				ps.setTimestamp(6, new Timestamp(control.getLastModified()));
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public int update(final Control control) {
		log.debug("Update control " + control.getId());
		final String query = "UPDATE controls SET (name, monitoringscenario, selectors, analyzerclass, status, lastmodified) = (?, ?, ?, ?, ?, ?) WHERE id = ?";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection)
					throws SQLException {
				PreparedStatement ps = connection.prepareStatement(query, new String[] { "id" });
				ps.setString(1, control.getName());
				ps.setString(2, control.getMonitoringScenario());
				ps.setObject(3, listToPGJson(control.getSelectors()));
				ps.setString(4, control.getAnalyzerClass());
				ps.setString(5, control.getStatus());
				ps.setTimestamp(6, new Timestamp(control.getLastModified()));
				ps.setInt(7, control.getId());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public void delete(final int id) {
		log.debug("Delete control " + id);
		final String query = "DELETE FROM controls WHERE id = ?";
		jdbcTemplate.update(query, id);
	}

	@Override
	public Control get(final String scenario, final String controlName) {
		log.debug("Get control " + controlName + " from scenario " + scenario);
		String query = "SELECT * FROM controls WHERE monitoringscenario = ? and name = ?";
		return queryForControlList(query, scenario, controlName).get(0);
	}

	@Override
	public List<Control> listControls(final String scenario) {
		log.debug("List controls in scenario " + scenario);
		String query = "SELECT * FROM controls WHERE monitoringscenario = ?";
		return queryForControlList(query, scenario);
	}

	@Override
	public List<Control> listControls(final String scenario, final String metric) {
		log.debug("List controls in scenario " + scenario + " for metric " + metric);
		String query =
				"SELECT * FROM controls c1 WHERE monitoringscenario = ? AND ? in (SELECT json_array_elements(selectors)->>'metric' FROM controls c2 WHERE c1.id = c2.id);";
		return queryForControlList(query, scenario, metric);
	}

	private List<Control> queryForControlList(final String query, final Object... params) {
		List<Control> results = Lists.newArrayList();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, params);
		for (Map<?, ?> row : rows) {
			Control ctrl = new Control();
			ctrl.setId((Integer) row.get("id"));
			ctrl.setName((String) row.get("Name"));
			ctrl.setMonitoringScenario((String) row.get("monitoringScenario"));
			ctrl.setAnalyzerClass((String) row.get("analyzerclass"));
			ctrl.setSelectors(pgJsonToList(row.get("selectors")));
			ctrl.setStatus((String) row.get("status"));
			ctrl.setLastModified(((Timestamp) row.get("lastmodified")).getTime());
			results.add(ctrl);
		}
		return results;
	}

	private PGobject listToPGJson(final List list) throws SQLException {
		PGobject jsonObject = new PGobject();
		jsonObject.setType("json");
		jsonObject.setValue(new Gson().toJson(list));
		return jsonObject;
	}

	private List<Selector> pgJsonToList(final Object obj) {
		Type listType = new TypeToken<ArrayList<Selector>>() {}.getType();
		if (obj != null) return new Gson().fromJson(((PGobject) obj).getValue(), listType);
		else return new ArrayList<Selector>();
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
