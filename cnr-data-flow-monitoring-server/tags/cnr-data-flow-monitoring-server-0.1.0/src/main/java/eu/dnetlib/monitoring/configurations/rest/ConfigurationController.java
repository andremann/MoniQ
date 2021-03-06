package eu.dnetlib.monitoring.configurations.rest;

import java.util.List;

import eu.dnetlib.monitoring.model.SensorConfiguration;
import eu.dnetlib.monitoring.server.dao.GenericConfigurationDAO;
import eu.dnetlib.monitoring.server.dao.exceptions.ConfigurationNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/scenarios/{scenario}/configurations")
public class ConfigurationController {

	private static final Log log = LogFactory.getLog(ConfigurationController.class);

	@Autowired
	private GenericConfigurationDAO configurationDao;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public int createConfiguration(@PathVariable final String scenario, @RequestBody final SensorConfiguration configuration) {
		int configurationId = configurationDao.create(configuration);
		log.info("Created configuration - Id: " + configurationId);
		return configurationId;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT)
	public int updateConfiguration(@PathVariable final String scenario, @RequestBody final SensorConfiguration configuration) {
		int configurationId = configurationDao.update(configuration);
		log.info("Updated configuration - Id: " + configurationId);
		return configurationId;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public List<SensorConfiguration> ListConfigurationsByScenario(@PathVariable final String scenario) {
		log.info("Listing available configurations for scenario " + scenario);
		return configurationDao.listConfigurations(scenario);
	}

	@ResponseBody
	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public SensorConfiguration getConfigurationByName(@PathVariable final String scenario, @PathVariable final String name)
			throws ConfigurationNotFoundException {
		log.info("Returning configuration " + name + " for scenario " + scenario);
		return configurationDao.getConfiguration(scenario, name);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public String deleteControl(@PathVariable final String scenario, @PathVariable final String id) {
		log.info("Delete configuration - Name: " + id);
		configurationDao.delete(Integer.parseInt(id));
		return id;
	}

}
