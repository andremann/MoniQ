package eu.dnetlib.monitoring.core.sensors.flow;

import java.util.List;

import eu.dnetlib.monitoring.core.sensors.Sensor;
import eu.dnetlib.monitoring.model.Observation;

public abstract class FlowSensor<T> extends Sensor {

	public final void measure(final T obj) {
		List<Observation> recordBurst = getObservations(obj);
		for (Observation record : recordBurst) {
			getClient().deliverObservation(record);
		}
	}

	protected List<Observation> getObservations(final T obj) {
		return null;
	}

}
