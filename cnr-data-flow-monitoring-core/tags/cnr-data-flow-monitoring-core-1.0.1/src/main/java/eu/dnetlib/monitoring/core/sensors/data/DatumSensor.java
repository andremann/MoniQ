package eu.dnetlib.monitoring.core.sensors.data;

import java.util.List;

import eu.dnetlib.monitoring.core.sensors.Sensor;
import eu.dnetlib.monitoring.model.Observation;

public abstract class DatumSensor<T> extends Sensor {

	public final void measure(final T datum) {
		List<Observation> observationsBurst = getObservations(datum);
		for (Observation observation : observationsBurst) {
			getClient().deliverObservation(observation);
		}
	}

	public abstract List<Observation> getObservations(final T datum);

}

