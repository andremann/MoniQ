package eu.dnetlib.monitoring.core.sensors.data;

import java.util.List;

import com.google.gson.Gson;
import eu.dnetlib.monitoring.core.sensors.Sensor;
import eu.dnetlib.monitoring.model.Observation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class DataSensor<T> extends Sensor {
	private static final Log log = LogFactory.getLog(DataSensor.class);

	public final void measure(final T data) {
		List<Observation> observations = getObservations(data);
		for (Observation observation : observations) {
			getClient().deliverObservation(observation);
			log.info(new Gson().toJson(observation));
		}
	}

	protected List<Observation> getObservations(final T obj) {
		return null;
	}

}
