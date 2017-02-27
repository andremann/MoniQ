package eu.dnetlib.monitoring.metrics.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.dnetlib.monitoring.controls.Control;
import eu.dnetlib.monitoring.controls.Selector;
import eu.dnetlib.monitoring.controls.analyzers.AnalyzerFactory;
import eu.dnetlib.monitoring.model.Observation;
import eu.dnetlib.monitoring.server.dao.GenericControlDAO;
import eu.dnetlib.monitoring.server.dao.GenericStashDAO;

@Controller
@RequestMapping("/scenarios/{scenario}/reports")
public class ReportController {

	private static final Logger log = Logger.getLogger(ReportController.class);

	@Autowired
	@Qualifier(value = "chosenDao")
	private GenericStashDAO observationDao;

	@Autowired
	private GenericControlDAO controlDao;

	@RequestMapping(value = "/{metric}", method = RequestMethod.GET)
	public @ResponseBody List<Control> getControlsByMetric(@PathVariable final String scenario, @PathVariable final String metric)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		/* Pull controls from the DB */
		List<Control> controls = controlDao.listControls(scenario, metric);

		for (Control ctrl : controls) {
			List<Observation> observations = new ArrayList<Observation>();
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
