package eu.dnetlib.monitoring.metrics.rest;

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
import eu.dnetlib.monitoring.server.dao.GenericConfigurationDAO;
import eu.dnetlib.monitoring.server.dao.GenericStashDAO;

@Controller
@RequestMapping("/scenarios/{scenario}/metrics")
public class MetricsController {

	private static final Logger log = Logger.getLogger(MetricsController.class);

	@Autowired
	@Qualifier(value = "chosenDao")
	private GenericStashDAO observationDao;

	@Autowired
	private GenericConfigurationDAO confDao;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<String> getAllMetrics(@PathVariable final String scenario) {
		List<String> metrics = confDao.listMetrics(scenario);
		return metrics;
	}

	@RequestMapping(value = "/{metric}/labels", method = RequestMethod.GET)
	public @ResponseBody List<String> listLabelNameByMetric(@PathVariable final String scenario, @PathVariable final String metric) {
		return confDao.listLabelNames(scenario, metric);
	}

	@RequestMapping(value = "/{metric}/labels/{label}", method = RequestMethod.GET)
	public @ResponseBody List<String> listLabelValuesByMetric(@PathVariable final String scenario, @PathVariable final String metric,
			@PathVariable final String label) {
		return confDao.listLabelValues(scenario, metric, label);
	}

	public @ResponseBody List<Observation> getObservationsByMetric(@PathVariable final String scenario,
			@PathVariable final String metric,
			@RequestParam(required = false) final String label) {
		if (label != null) {
			return observationDao.findObservations(scenario, metric, label);
		} else {
			return observationDao.findObservations(scenario, metric);
		}
	}

	@RequestMapping(value = "/{metric}/observations", method = RequestMethod.GET)
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
			}
		}

		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("observations", outerMap.values());
		retMap.put("labelValues", foundLabelValues);
		return retMap;
	}
}
