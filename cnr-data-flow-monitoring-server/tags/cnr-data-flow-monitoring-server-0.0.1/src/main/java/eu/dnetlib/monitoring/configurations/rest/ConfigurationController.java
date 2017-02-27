package eu.dnetlib.monitoring.configurations.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.dnetlib.monitoring.model.SensorConfiguration;
import eu.dnetlib.monitoring.server.dao.GenericConfigurationDAO;

@Controller
@RequestMapping("/scenarios/{scenario}/configurations")
public class ConfigurationController {

	@Autowired
	private GenericConfigurationDAO confDao;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<SensorConfiguration> ListConfigurationByScneario(@PathVariable final String scenario) {
		List<SensorConfiguration> listConfigurations = confDao.listConfigurations(scenario);
		return listConfigurations;
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public @ResponseBody SensorConfiguration getConfigurationByName(@PathVariable final String scenario, @PathVariable final String name) {
		SensorConfiguration configuration = confDao.getConfiguration(scenario, name);
		return configuration;
	}

}
