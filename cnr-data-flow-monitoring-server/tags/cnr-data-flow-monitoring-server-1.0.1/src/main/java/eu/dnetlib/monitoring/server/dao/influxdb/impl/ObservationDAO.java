package eu.dnetlib.monitoring.server.dao.influxdb.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ListIterator;
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
		influxDB.enableBatch(1000, 100, TimeUnit.NANOSECONDS);
	}

	@Override
	public boolean create(final Observation observation) {
		Point point = Point.measurement(observation.getMetric())
				.time(observation.getTimestamp(), TimeUnit.NANOSECONDS)
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
	public List<String> listMetrics(final String scenario) {
		final List<Result> results = influxDB.query(new Query("SHOW MEASUREMENTS", influxDbName)).getResults();
		final Series series = results.get(0).getSeries().get(0);

		List metrics = Lists.newArrayList();
		final ListIterator<List<Object>> iterator = series.getValues().listIterator();
		while (iterator.hasNext()) {
			metrics.add(iterator.next().get(0));
		}
		return metrics;
	}

	@Override
	public List<Observation> findObservations(final String scenario, final String metric) {
		String queryString = String.format("select * from \"%s\" where monitoringScenario = '%s' order by time desc", metric, scenario);
		return queryForObservationList(queryString);
	}

	@Override
	public List<Observation> findObservations(final String scenario, final String metric, final String labelName) {
		String queryString =
				String.format("select * from \"%s\" where monitoringScenario = '%s' and \"%s\" != '' order by time desc", metric, scenario, labelName);
		return queryForObservationList(queryString);
	}

	@Override
	public List<Observation> findKLastObservations(final String scenario, final String metric, final int k) {
		String queryString = String.format("select * from \"%s\" where monitoringScenario = '%s' order by time desc limit %s", metric, scenario, k);
		return queryForObservationList(queryString);
	}

	@Override
	public List<Observation> findKLastObservations(final String scenario, final String metric, final String labelName, final int k) {
		String queryString =
				String.format("select * from \"%s\" where monitoringScenario = '%s' and \"%s\" != '' order by time desc limit %s", metric, scenario, labelName,
						k);
		return queryForObservationList(queryString);
	}

	@Override
	public List<Observation> findKLastObservations(final String scenario, final String metric, final String labelName, final String labelValue, final int k) {
		String queryString =
				String.format("select * from \"%s\" where monitoringScenario = '%s' and \"%s\" != '%s' order by time desc limit %s", metric, scenario,
						labelName, labelValue, k);
		return queryForObservationList(queryString);
	}

	private List<Observation> queryForObservationList(String queryString) {
		Query query = new Query(queryString, influxDbName);
		final List<Result> results = influxDB.query(query).getResults();
		final Series series = results.get(0).getSeries().get(0);

		final List<String> columns = series.getColumns();

		List observations = Lists.newArrayList();
		final ListIterator<List<Object>> iterator = series.getValues().listIterator();
		while (iterator.hasNext()) {
			observations.add(influxValueToObservation(iterator.next(), columns, series.getName()));
		}
		return observations;
	}

	private Observation influxValueToObservation(List<Object> value, List<String> columns, String metric) {
		Observation observation = new Observation();

		try {
			observation.setTimestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse((String) value.get(columns.indexOf("time"))).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		observation.setValue((Double) value.get(columns.indexOf("value")));
		observation.setMetric(metric);
		observation.setMonitoringScenario((String) value.get(columns.indexOf("monitoringScenario")));
		observation.setSensorType((String) value.get(columns.indexOf("sensorType")));
		observation.setSensorId((String) value.get(columns.indexOf("sensorId")));

		return observation;
	}

	@Override
	public int countObservations(final String scenario) {
		final List<String> metrics = listMetrics(scenario);
		int total = 0;
		for (String metric : metrics) {
			total += findObservations(scenario, metric).size();
		}
		return total;
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
