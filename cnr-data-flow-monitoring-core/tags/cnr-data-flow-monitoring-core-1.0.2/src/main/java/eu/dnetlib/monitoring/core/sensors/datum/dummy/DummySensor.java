package eu.dnetlib.monitoring.core.sensors.datum.dummy;

import java.util.List;

import com.google.common.collect.Lists;
import eu.dnetlib.monitoring.core.sensors.datum.DatumSensor;
import eu.dnetlib.monitoring.model.Metric;
import eu.dnetlib.monitoring.model.Observation;

/**
 * Created by andrea on 24/05/16.
 */
public class DummySensor extends DatumSensor<Double> {

	@Override
	public List<Observation> getObservations(final Double datum) {
		final List<Observation> observations = Lists.newArrayList();
		for (Metric metric : getConfiguration().getParams().getMetrics().values()) {
			Observation observation = new Observation();
			observation.setValue(datum);
			observation.setMetric(metric.getName());
			observation.setMonitoringScenario(getConfiguration().getParams().getMonitoringScenario());
			observation.setSensorId(id);
			observation.setSensorType(this.getClass().getSimpleName());
			observation.setLabels(metric.getLabels());
			observation.addLabels(getConfiguration().getParams().getGlobalLabels());
			observations.add(observation);
		}
		return observations;
	}

	@Override
	protected void init() {

	}
}
