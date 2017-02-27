package eu.dnetlib.monitoring.core.sensors.collection;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.Lists;

import eu.dnetlib.monitoring.core.sensors.Sensor;
import eu.dnetlib.monitoring.model.Observation;

public class SensorWrapper extends Sensor {

	private final Log log = LogFactory.getLog(SensorWrapper.class);

	private List<Sensor> sensorList;

	public SensorWrapper() {
		sensorList = Lists.newArrayList();
	}

	public final void measure() {
		List<Observation> recordBurst = getObservations();
		for (Observation record : recordBurst) {
			getClient().deliverObservation(record);
		}
	}

	protected List<Observation> getObservations() {
		List<Observation> observationList = Lists.newArrayList();
		long now = System.currentTimeMillis();
		for (Sensor s : sensorList) {
			observationList.addAll(((CollectionSensor) s).getObservations());
		}
		/* Sync observations with the same Timestamp */
		for (Observation o : observationList) {
			log.debug(o.getMetric());
			o.setTimestamp(now);
		}
		return observationList;
	}

	public SensorWrapper registerSensor(final Sensor s) {
		sensorList.add(s);
		return this;
	}

	@Override
	protected void init() {}

}
