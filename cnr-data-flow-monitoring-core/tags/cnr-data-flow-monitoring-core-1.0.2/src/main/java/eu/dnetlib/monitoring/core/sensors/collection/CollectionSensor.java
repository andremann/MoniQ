package eu.dnetlib.monitoring.core.sensors.collection;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import eu.dnetlib.monitoring.core.sensors.Sensor;
import eu.dnetlib.monitoring.model.Metric;
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
        shutdown();
    }

    protected List<Observation> getObservations() {
        List<Observation> observationList = Lists.newArrayList();
        long currentTimeNano = System.nanoTime();

        for (Metric metric : getConfiguration().getParams().getMetrics().values()) {
            Map<String, String> allParams = Maps.newHashMap();
            allParams.putAll(metric.getParams());
            allParams.putAll(getConfiguration().getParams().getGlobalParams());
            Double value = produceObservationValue(allParams);

			/* Prepare Observation to return */
            Observation observation = new Observation(currentTimeNano);
            observation.setSensorType(this.getClass().getSimpleName());
            observation.setSensorId(id);
            observation.setValue(value);
            observation.setMetric(metric.getName());
            observation.setLabels(metric.getLabels());
            observation.addLabels(getConfiguration().getParams().getGlobalLabels());
            observation.setMonitoringScenario(getConfiguration().getParams().getMonitoringScenario());
            /* Add it to the list */
            observationList.add(observation);
        }
        return observationList;
    }

    protected abstract Double produceObservationValue(Map<String, String> allParams);

    protected abstract void shutdown();
}