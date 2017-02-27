package eu.dnetlib.monitoring.core.sensors.collection;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import eu.dnetlib.monitoring.core.sensors.Sensor;
import eu.dnetlib.monitoring.rmi.Observation;
import eu.dnetlib.monitoring.rmi.Observation.Builder;

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

	private Function<Observation, Observation.Builder> toMutable() {
		return new Function<Observation, Observation.Builder>() {

			@Override
			public Builder apply(final Observation immutableObservation) {
				return Observation.newBuilder(immutableObservation);
			}

		};

	}

	protected List<Observation> getObservations() {
		List<Observation> observationList = Lists.newArrayList();
		long now = System.currentTimeMillis();
		for (Sensor s : sensorList) {
			List<Observation> observations = ((CollectionSensor) s).getObservations();
			for (Observation.Builder ob : Iterables.transform(observations, toMutable())) {
				log.debug(ob.getMetric());
				ob.setTimestamp(now);
				observationList.add(ob.build());
			}
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
