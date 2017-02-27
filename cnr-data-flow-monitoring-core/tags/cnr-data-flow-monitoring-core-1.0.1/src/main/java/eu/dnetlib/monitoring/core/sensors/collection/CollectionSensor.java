package eu.dnetlib.monitoring.core.sensors.collection;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import eu.dnetlib.monitoring.core.sensors.Sensor;
import eu.dnetlib.monitoring.model.Observation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class CollectionSensor extends Sensor {
    private static final Log log = LogFactory.getLog(CollectionSensor.class);

    public final void measure() {
        List<Observation> observationBurst = getObservations();
        for (Observation observation : observationBurst) {
            getClient().deliverObservation(observation);
        }
    }

    protected List<Observation> getObservations() {
        long currentTimeMillis = System.currentTimeMillis();

        List<Observation> observationList = Lists.newArrayList();
        for (Entry<?, ?> e : ((Map<?, ?>) getConfiguration().getParam("querySet")).entrySet()) {
            Double value = produceObservationValue((String) e.getValue());

			/* Prepare Observation to return */
            Observation observation = new Observation(currentTimeMillis);
            observation.setSensorType(this.getClass().getSimpleName());
            observation.setSensorId(id);
            observation.setValue(value);
            observation.setMetric((String) e.getKey());
            observation.setLabels((Map<String, String>) getConfiguration().getParam("labels"));
            observation.setMonitoringScenario((String) getConfiguration().getParam("monitoringScenario"));
            /* Add it to the list */
            observationList.add(observation);
        }
        return observationList;
    }

    protected abstract Double produceObservationValue(String... params);
}
