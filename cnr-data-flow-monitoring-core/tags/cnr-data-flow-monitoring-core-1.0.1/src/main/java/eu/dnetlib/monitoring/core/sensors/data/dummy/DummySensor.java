package eu.dnetlib.monitoring.core.sensors.data.dummy;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import eu.dnetlib.monitoring.core.sensors.data.DatumSensor;
import eu.dnetlib.monitoring.model.Observation;

/**
 * Created by andrea on 24/05/16.
 */
public class DummySensor extends DatumSensor<Double> {

	@Override
	public List<Observation> getObservations(final Double datum) {
		Observation observation = new Observation();
		observation.setTimestamp(System.nanoTime());
		observation.setValue(datum);
		observation.setMetric((String) getConfiguration().getParam("metric"));
		observation.setMonitoringScenario((String) getConfiguration().getParam("monitoringScenario"));
		observation.setSensorId(id);
		observation.setSensorType(this.getClass().getSimpleName());
		observation.setLabels((Map<String, String>) getConfiguration().getParam("labels"));

		return Lists.newArrayList(observation);
	}

	@Override
	protected void init() {

	}
}
