package eu.dnetlib.monitoring.observations.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.dnetlib.monitoring.model.Observation;
import eu.dnetlib.monitoring.server.dao.GenericStashDAO;

@Controller
@RequestMapping("/scenarios/{scenario}/metrics/{metric}/observations")
public class ObservationsController {

	private static final Logger log = Logger.getLogger(ObservationsController.class);

	@Autowired
	@Qualifier(value = "chosenDao")
	private GenericStashDAO observationDao;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getObservationsByMetricInC3ReadyFormat(@PathVariable final String scenario,
			@PathVariable final String metric,
			@RequestParam(required = false) final String label) {
		List<Observation> observations = getObservationsByMetric(scenario, metric, label);

		/* Digest the result in a C3.js-friendly format */
		Map<Long, Map<String, Object>> outerMap = new LinkedHashMap<Long, Map<String, Object>>();
		Set<String> foundLabelValues = new HashSet<String>();
		for (Observation o : observations) {
			Long timestamp = o.getTimestamp();
			Map<String, Object> innerMap;
			if (!outerMap.containsKey(timestamp)) {
				innerMap = new HashMap<String, Object>();
				innerMap.put("timestamp", timestamp);
			} else {
				innerMap = outerMap.get(timestamp);
			}

			String labelValue = o.getLabels().get(label);
			if (labelValue != null) {
				foundLabelValues.add(labelValue);
				innerMap.put(labelValue, o.getLog());
				outerMap.put(timestamp, innerMap);
			} else {
				foundLabelValues.add("undefined");
				innerMap.put("undefined", o.getLog());
				outerMap.put(timestamp, innerMap);
			}
		}

		Map<String, Object> retMap = new HashMap<String, Object>();
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
