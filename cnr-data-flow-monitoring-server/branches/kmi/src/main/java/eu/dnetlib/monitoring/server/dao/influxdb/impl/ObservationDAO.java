package eu.dnetlib.monitoring.server.dao.influxdb.impl;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;
import eu.dnetlib.monitoring.model.Observation;
import eu.dnetlib.monitoring.server.dao.GenericObservationDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;
import org.springframework.beans.factory.annotation.Required;

/**
 * Created by andrea on 24/05/16.
 */
public class ObservationDAO implements GenericObservationDAO {

	private static final Log log = LogFactory.getLog(ObservationDAO.class);

	/* InfluxDB client */
	private InfluxDB influxDB;
	private String influxDbName;
	private String influxDbHost;
	private String influxDbPort;
	private String influxDbPassword;
	private String influxDbUsername;


	public void init() {
		influxDB = InfluxDBFactory.connect("http://" + influxDbHost + ":" + influxDbPort, influxDbUsername, influxDbPassword);
		influxDB.createDatabase(influxDbName);
		influxDB.enableBatch(1000, 200, TimeUnit.MILLISECONDS);
	}

	@Override
	public boolean create(final Observation observation) {
		Point point = Point.measurement(observation.getMetric())
				.time(observation.getTimestamp(), TimeUnit.MILLISECONDS)
				.addField("value", observation.getValue())
				.tag("monitoringScenario", observation.getMonitoringScenario())
				.tag("sensorType", observation.getSensorType())
				.tag("sensorId", observation.getSensorId())
				.tag(observation.getLabels())
				.build();
		influxDB.write(influxDbName, "default", point);
		return true;
	}

	@Override
	public Observation getById(final String id) {
		return null;
	}

	@Override
	public List<Observation> findObservations(final String scenario, final String metric) {
		String queryString = String.format("select * from \"%s\" where monitoringScenario = '%s' order by time desc limit 1000", metric, scenario);
		return Lists.reverse(queryForObservationList(queryString));
	}

	@Override
	public List<Observation> findObservations(final String scenario, final String metric, final String labelName) {
		String queryString =
				String.format("select * from \"%s\" where monitoringScenario = '%s' and \"%s\" != '' order by time desc limit 1000", metric, scenario,
						labelName);
		return Lists.reverse(queryForObservationList(queryString));
	}

	@Override
	public List<Observation> findKLastObservations(final String scenario, final String metric, final int k) {
		String queryString = String.format("select * from \"%s\" where monitoringScenario = '%s' order by time desc limit %s", metric, scenario, k);
		return Lists.reverse(queryForObservationList(queryString));
	}

	@Override
	public List<Observation> findKLastObservations(final String scenario, final String metric, final String labelName, final int k) {
		String queryString =
				String.format("select * from \"%s\" where monitoringScenario = '%s' and \"%s\" != '' order by time desc limit %s", metric, scenario, labelName,
						k);
		return Lists.reverse(queryForObservationList(queryString));
	}

	@Override
	public List<Observation> findKLastObservations(final String scenario, final String metric, final String labelName, final String labelValue, final int k) {
		String queryString =
				String.format("select * from \"%s\" where monitoringScenario = '%s' and \"%s\" = '%s' order by time desc limit %s", metric, scenario, labelName,
						labelValue, k);
		return Lists.reverse(queryForObservationList(queryString));
	}

	@Override
	public List<Observation> findKLastObservations(final String scenario, final String metric, final Map<String, String> labelsConditions, final int k) {
		String conditions = "";
		for (Entry entry : labelsConditions.entrySet()) {
			conditions += String.format("\"%s\" = '%s' and ", entry.getKey(), entry.getValue());
		}
		String queryString = String.format("select * from \"%s\" where monitoringScenario = '%s' and %s order by time desc limit %s", metric, scenario,
				conditions.substring(0, conditions.length() - 4), k);
		return Lists.reverse(queryForObservationList(queryString));
	}

	private List<Observation> queryForObservationList(String queryString) {
		Query query = new Query(queryString, influxDbName);
		final List<Result> results = influxDB.query(query, TimeUnit.MILLISECONDS).getResults();
		List observations = Lists.newArrayList();
		try {
			final Series series = results.get(0).getSeries().get(0);
			final List<String> columns = series.getColumns();
			final ListIterator<List<Object>> iterator = series.getValues().listIterator();
			while (iterator.hasNext()) {
				observations.add(influxPointToObservation(iterator.next(), columns, series.getName()));
			}
		} catch (NullPointerException npe) {
			log.error("Cannot retrieve observations.. returning an empty list");
			return Lists.newArrayList();
		} finally {
			return observations;
		}
	}

	private Observation influxPointToObservation(List<Object> point, List<String> columns, String metric) {
		Observation observation = new Observation();

		for (String column : columns) {
			switch (column) {
			case "time":
				observation.setTimestamp(new Double((Double) point.get(columns.indexOf(column))).longValue());
				break;
			case "value":
				observation.setValue((Double) point.get(columns.indexOf(column)));
				break;
			case "monitoringScenario":
				observation.setMonitoringScenario((String) point.get(columns.indexOf(column)));
				break;
			case "sensorId":
				observation.setSensorId((String) point.get(columns.indexOf(column)));
				break;
			case "sensorType":
				observation.setSensorType((String) point.get(columns.indexOf(column)));
				break;
			default:
				observation.addLabel(column, (String) point.get(columns.indexOf(column)));
			}
			observation.setMetric(metric);
		}
		return observation;
	}

	@Override
	public int countObservations(final String scenario) {
		final List<String> metrics = listTrackedMetrics(scenario);
		int total = 0;
		for (String metric : metrics) {
			total += findObservations(scenario, metric).size();
		}
		return total;
	}

	@Override
	public List<String> listTrackedMetrics(final String scenario) {
		final List<Result> results = influxDB.query(new Query("SHOW MEASUREMENTS", influxDbName)).getResults();
		try {
			final Series series = results.get(0).getSeries().get(0);

			List metrics = Lists.newArrayList();
			final ListIterator<List<Object>> iterator = series.getValues().listIterator();
			while (iterator.hasNext()) {
				metrics.add(iterator.next().get(0));
			}
			return metrics;
		} catch (NullPointerException npe) {
			return Lists.newArrayList();
		}
	}

	public String getInfluxDbName() {
		return influxDbName;
	}

	@Required
	public void setInfluxDbName(final String influxDbName) {
		this.influxDbName = influxDbName;
	}

	public String getInfluxDbHost() {
		return influxDbHost;
	}

	@Required
	public void setInfluxDbHost(final String influxDbHost) {
		this.influxDbHost = influxDbHost;
	}

	public String getInfluxDbPort() {
		return influxDbPort;
	}

	@Required
	public void setInfluxDbPort(final String influxDbPort) {
		this.influxDbPort = influxDbPort;
	}

	public String getInfluxDbPassword() {
		return influxDbPassword;
	}

	@Required
	public void setInfluxDbPassword(final String influxDbPassword) {
		this.influxDbPassword = influxDbPassword;
	}

	public String getInfluxDbUsername() {
		return influxDbUsername;
	}

	@Required
	public void setInfluxDbUsername(final String influxDbUsername) {
		this.influxDbUsername = influxDbUsername;
	}

}
