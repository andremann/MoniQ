package eu.dnetlib.monitoring.core.sensors.data;

import java.util.List;

import eu.dnetlib.monitoring.core.sensors.Sensor;
import eu.dnetlib.monitoring.rmi.Observation;

public abstract class DataSensor<T> extends Sensor {

	public final void measure(final T data) {
		List<Observation> recordBurst = getObservations(data);
		for (Observation record : recordBurst) {
			getClient().deliverObservation(record);
		}
	}

	protected List<Observation> getObservations(final T obj) {
		return null;
	}

}
