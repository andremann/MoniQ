package eu.dnetlib.monitoring.configurations.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.dnetlib.monitoring.model.SensorConfiguration;
import eu.dnetlib.monitoring.server.dao.GenericConfigurationDAO;

@Controller
@RequestMapping("/scenarios/{scenario}/configurations")
public class ConfigurationController {

	private static final Logger log = Logger.getLogger(ConfigurationController.class);

	@Autowired
	private GenericConfigurationDAO configurationDao;

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody int createConfiguration(@PathVariable final String scenario, @RequestBody final SensorConfiguration configuration) {
		int configurationId = configurationDao.create(configuration);
		log.info("Created configuration - Id: " + configurationId);
		return configurationId;
	}

	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody int updateConfiguration(@PathVariable final String scenario, @RequestBody final SensorConfiguration configuration) {
		int configurationId = configurationDao.update(configuration);
		log.info("Updated configuration - Id: " + configurationId);
		return configurationId;
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<SensorConfiguration> ListConfigurationsByScenario(@PathVariable final String scenario) {
		log.info("Listing available configurations for scenario " + scenario);
		List<SensorConfiguration> listConfigurations = configurationDao.listConfigurations(scenario);
		return listConfigurations;
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public @ResponseBody SensorConfiguration getConfigurationByName(@PathVariable final String scenario, @PathVariable final String name) {
		log.info("Returning configuration " + name + " for scenario " + scenario);
		SensorConfiguration configuration = configurationDao.getConfiguration(scenario, name);
		return configuration;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public @ResponseBody String deleteControl(@PathVariable final String scenario, @PathVariable final String id) {
		log.info("Delete configuration - Name: " + id);
		configurationDao.delete(Integer.parseInt(id));
		return id;
	}

}
