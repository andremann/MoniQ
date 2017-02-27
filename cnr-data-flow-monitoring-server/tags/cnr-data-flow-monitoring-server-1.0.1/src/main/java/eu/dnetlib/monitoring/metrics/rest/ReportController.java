package eu.dnetlib.monitoring.metrics.rest;

import java.util.ArrayList;
import java.util.List;

import eu.dnetlib.monitoring.controls.Control;
import eu.dnetlib.monitoring.controls.Selector;
import eu.dnetlib.monitoring.controls.analyzers.AnalyzerFactory;
import eu.dnetlib.monitoring.model.Observation;
import eu.dnetlib.monitoring.server.dao.GenericControlDAO;
import eu.dnetlib.monitoring.server.dao.GenericObservationDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/scenarios/{scenario}/reports")
public class ReportController {

	private static final Log log = LogFactory.getLog(ReportController.class);

	@Autowired
	@Qualifier(value = "chosenDao")
	private GenericObservationDAO observationDao;

	@Autowired
	private GenericControlDAO controlDao;

	@ResponseBody
	@RequestMapping(value = "/{metric}", method = RequestMethod.GET)
	public List<Control> listReportsByMetric(@PathVariable final String scenario, @PathVariable final String metric)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		log.info(String.format("Get reports for scenario (%s) and metric (%s)", scenario, metric));

		/* Pull controls from the DB */
		List<Control> controls = controlDao.listActiveControls(scenario, metric);

		for (Control ctrl : controls) {
			List<Observation> observations = new ArrayList<>();
			for (Selector s : ctrl.getSelectors()) {
				observations
						.addAll(observationDao.findKLastObservations(ctrl.getMonitoringScenario(), s.getMetric(), s.getLabelName(), s.getLabelValue(),
								s.getSamples()));
			}
			ctrl.setResult(AnalyzerFactory.createAnalyzer(ctrl.getAnalyzerClass()).analyze(observations));
		}

		return controls;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public List<Control> listAllReports(@PathVariable final String scenario) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		log.info(String.format("Get all reports for scenario (%s)", scenario));

		/* Pull controls from the DB */
		List<Control> controls = controlDao.listActiveControls(scenario);

		for (Control ctrl : controls) {
			List<Observation> observations = new ArrayList<>();
			for (Selector s : ctrl.getSelectors()) {
				observations
						.addAll(observationDao.findKLastObservations(ctrl.getMonitoringScenario(), s.getMetric(), s.getLabelName(), s.getLabelValue(),
								s.getSamples()));
			}
			ctrl.setResult(AnalyzerFactory.createAnalyzer(ctrl.getAnalyzerClass()).analyze(observations));
		}

		return controls;
	}

}
