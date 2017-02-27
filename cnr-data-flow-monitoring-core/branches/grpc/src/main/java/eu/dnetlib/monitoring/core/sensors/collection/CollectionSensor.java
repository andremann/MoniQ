package eu.dnetlib.monitoring.core.sensors.collection;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

import eu.dnetlib.monitoring.core.sensors.Sensor;
import eu.dnetlib.monitoring.rmi.Observation;

public abstract class CollectionSensor extends Sensor {

	private String[] observationParams;

	public final void measure() {
		List<Observation> recordBurst = getObservations();
		for (Observation record : recordBurst) {
			getClient().deliverObservation(record);
		}
	}

	protected List<Observation> getObservations() {
		long currentTimeMillis = System.currentTimeMillis();

		List<Observation> observationList = Lists.newArrayList();
		for (Entry<?, ?> e : ((Map<?, ?>) getConfiguration().getParam("querySet")).entrySet()) {
			Double value = produceObservationValue((String) e.getValue());

			/* Prepare Observation to return */
			Observation.Builder observation = Observation.newBuilder();
			observation.setSensorType(this.getClass().getSimpleName());
			observation.setSensorId(id);
			observation.setLog(value);
			observation.setMetric((String) e.getKey());
			observation.getMutableLabels().putAll((Map) getConfiguration().getParam("labels"));
			observation.setMonitoringScenario((String) getConfiguration().getParam("monitoringScenario"));
			/* Add it to the list */
			observationList.add(observation.build());
		}
		return observationList;
	}

	protected abstract Double produceObservationValue(String... params);

	public String[] getObservationParams() {
		return observationParams;
	}

	public void setObservationParams(final String[] observationParams) {
		this.observationParams = observationParams;
	}

}
