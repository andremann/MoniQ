package eu.dnetlib.monitoring.observations.rest;

import java.util.*;

import eu.dnetlib.monitoring.model.Observation;
import eu.dnetlib.monitoring.server.dao.GenericStashDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/scenarios/{scenario}/metrics/{metric}/observations")
public class ObservationsController {

	private static final Log log = LogFactory.getLog(ObservationsController.class);

	@Autowired
	@Qualifier(value = "chosenDao")
	private GenericStashDAO observationDao;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public void createObservation(@RequestBody Observation observation){
		if (!observationDao.create(observation)) {
			log.info(String.format("Cannot stash observation -> %s(%s = %s)", observation.getSensorType(), observation.getMetric(),
					observation.getValue()));
		}
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public Map<String, Object> getObservationsByMetricInC3ReadyFormat( @PathVariable final String scenario, @PathVariable final String metric,
			@RequestParam(required = false) final String label) {
		log.info(String.format("Retrieve observations for metric (%s) (%s) in scenario (%s)", metric, label, scenario));

		List<Observation> observations = getObservationsByMetric(scenario, metric, label);

		/* Digest the result in a C3.js-friendly format */
		Map<Long, Map<String, Object>> outerMap = new LinkedHashMap<>();
		Set<String> foundLabelValues = new HashSet<>();
		for (Observation o : observations) {
			Long timestamp = o.getTimestamp();
			Map<String, Object> innerMap;
			if (!outerMap.containsKey(timestamp)) {
				innerMap = new HashMap<>();
				innerMap.put("timestamp", timestamp);
			} else {
				innerMap = outerMap.get(timestamp);
			}

			String labelValue = o.getLabels().get(label);
			if (labelValue != null) {
				foundLabelValues.add(labelValue);
				innerMap.put(labelValue, o.getValue());
				outerMap.put(timestamp, innerMap);
			} else {
				foundLabelValues.add("undefined");
				innerMap.put("undefined", o.getValue());
				outerMap.put(timestamp, innerMap);
			}
		}

		Map<String, Object> retMap = new HashMap<>();
		retMap.put("observations", outerMap.values());
		retMap.put("labelValues", foundLabelValues);
		return retMap;
	}

	public List<Observation> getObservationsByMetric(final String scenario, final String metric, final String label) {
		if (label != null) {
			log.info(String.format("Retrieve observations for metric (%s) (%s) in scenario (%s)", metric, label, scenario));
			return observationDao.findObservations(scenario, metric, label);
		} else {
			log.info(String.format("Retrieve observations for metric (%s) in scenario (%s)", metric, scenario));
			return observationDao.findObservations(scenario, metric);
		}
	}

}
