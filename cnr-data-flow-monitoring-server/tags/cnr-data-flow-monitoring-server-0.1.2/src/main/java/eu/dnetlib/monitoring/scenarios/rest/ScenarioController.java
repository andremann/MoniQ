package eu.dnetlib.monitoring.scenarios.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.dnetlib.monitoring.scenarios.Scenario;
import eu.dnetlib.monitoring.server.dao.GenericConfigurationDAO;
import eu.dnetlib.monitoring.server.dao.GenericControlDAO;
import eu.dnetlib.monitoring.server.dao.GenericScenarioDAO;
import eu.dnetlib.monitoring.server.dao.GenericStashDAO;
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
@RequestMapping("/scenarios")
public class ScenarioController {

	private static final Log log = LogFactory.getLog(ScenarioController.class);

	@Autowired
	private GenericScenarioDAO scenarioDao;

	@Autowired
	private GenericControlDAO controlDao;

	@Autowired
	private GenericConfigurationDAO configurationDao;

	@Autowired
	@Qualifier(value = "chosenDao")
	private GenericStashDAO observationDao;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public List<Scenario> listScenarios() {
		log.info("List scenarios");
		return scenarioDao.listScenarios();
	}

	@ResponseBody
	@RequestMapping(value = "/{scenario}")
	public Map<String, Object> getScenarioSummary(@PathVariable final String scenario) {
		log.info("List facts for scenario " + scenario);
		Map<String, Object> map = new HashMap<>();
		map.put("# of Controls", controlDao.listControls(scenario).size());
		map.put("# of Metrics", observationDao.listMetrics(scenario).size());
		map.put("# of Observations", observationDao.countObservations(scenario));
		map.put("# of Configurations", configurationDao.listConfigurations(scenario).size());
		return map;
	}
}
