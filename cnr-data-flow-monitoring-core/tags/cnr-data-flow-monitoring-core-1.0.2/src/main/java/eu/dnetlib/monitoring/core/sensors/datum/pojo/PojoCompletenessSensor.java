package eu.dnetlib.monitoring.core.sensors.datum.pojo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import eu.dnetlib.monitoring.core.exceptions.SensorInitException;
import eu.dnetlib.monitoring.core.sensors.datum.DatumSensor;
import eu.dnetlib.monitoring.model.Metric;
import eu.dnetlib.monitoring.model.Observation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by andrea on 28/06/16.
 */
public class PojoCompletenessSensor<T> extends DatumSensor<T> {

	private final Log log = LogFactory.getLog(PojoCompletenessSensor.class);


	@Override
	public List<Observation> getObservations(final T datum) {
		log.debug(new Gson().toJson(datum));
		List<Observation> observations = Lists.newArrayList();

		for (Metric metric : getConfiguration().getParams().getMetrics().values()) {
			final ArrayList<String> testFields = new Gson().fromJson(metric.getParams().get("fields"), ArrayList.class);

			int emptyCounter = 0;
			for (Field f : datum.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				try {
					if (testFields.contains(f.getName()) && (f.get(datum) == "" || f.get(datum) == null)) {
						emptyCounter++;
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

			Observation observation = new Observation();
			observation.setSensorType(this.getClass().getSimpleName());
			observation.setSensorId(id);
			observation.setMonitoringScenario(getConfiguration().getParams().getMonitoringScenario());
			observation.setMetric(metric.getName());
			observation.setLabels(metric.getLabels());
			observation.addLabels(getConfiguration().getParams().getGlobalLabels());
			observation.setValue(1 - (double) emptyCounter / testFields.size());
			observations.add(observation);
		}

		return observations;
	}

	@Override
	protected void init() throws SensorInitException {

	}
}
