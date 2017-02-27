package eu.dnetlib.monitoring.metrics.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.dnetlib.monitoring.server.dao.GenericConfigurationDAO;

@Controller
@RequestMapping("/scenarios/{scenario}/metrics")
public class MetricsController {

	private static final Logger log = Logger.getLogger(MetricsController.class);

	@Autowired
	private GenericConfigurationDAO confDao;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<String> getAllMetrics(@PathVariable final String scenario) {
		List<String> metrics = confDao.listMetrics(scenario);
		return metrics;
	}

	@RequestMapping(value = "/{metric}/labels", method = RequestMethod.GET)
	public @ResponseBody List<String> listLabelNameByMetric(@PathVariable final String scenario, @PathVariable final String metric) {
		log.info(String.format("List labels of (%s) in scenario (%s)", metric, scenario));
		return confDao.listLabelNames(scenario, metric);
	}

	@RequestMapping(value = "/{metric}/labels/{label}", method = RequestMethod.GET)
	public @ResponseBody List<String> listLabelValuesByMetric(@PathVariable final String scenario, @PathVariable final String metric,
			@PathVariable final String label) {
		log.info(String.format("List values of (%s) (%s) in scenario (%s)", metric, label, scenario));
		return confDao.listLabelValues(scenario, metric, label);
	}

}
