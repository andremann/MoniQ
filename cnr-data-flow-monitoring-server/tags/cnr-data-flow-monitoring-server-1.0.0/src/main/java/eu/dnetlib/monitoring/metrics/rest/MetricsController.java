package eu.dnetlib.monitoring.metrics.rest;

import java.util.List;

import eu.dnetlib.monitoring.server.dao.GenericConfigurationDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/scenarios/{scenario}/metrics")
public class MetricsController {

	private static final Log log = LogFactory.getLog(MetricsController.class);

	@Autowired
	private GenericConfigurationDAO confDao;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public List<String> getAllMetrics(@PathVariable final String scenario) {
		log.info(String.format("List metrics for scenario(%s)", scenario));
		return confDao.listMetrics(scenario);
	}

	@ResponseBody
	@RequestMapping(value = "/{metric}/labels", method = RequestMethod.GET)
	public List<String> listLabelNameByMetric(@PathVariable final String scenario, @PathVariable final String metric) {
		log.info(String.format("List labels of (%s) in scenario (%s)", metric, scenario));
		return confDao.listLabelNames(scenario, metric);
	}

	@ResponseBody
	@RequestMapping(value = "/{metric}/labels/{label}", method = RequestMethod.GET)
	public List<String> listLabelValuesByMetric(@PathVariable final String scenario, @PathVariable final String metric, @PathVariable final String label) {
		log.info(String.format("List values of (%s) (%s) in scenario (%s)", metric, label, scenario));
		return confDao.listLabelValues(scenario, metric, label);
	}

}
