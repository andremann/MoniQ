package eu.dnetlib.monitoring.controls.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.dnetlib.monitoring.controls.Control;
import eu.dnetlib.monitoring.server.dao.GenericControlDAO;

@Controller
@RequestMapping("/scenarios/{scenario}/controls")
public class ControlController {

	private static final Logger log = Logger.getLogger(ControlController.class);

	@Autowired
	private GenericControlDAO controlDao;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<Control> listControls(@PathVariable final String scenario) {
		log.info("List controls");
		return controlDao.listControls(scenario);
	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody int createControl(@PathVariable final String scenario, @RequestBody final Control control) {
		int controlId = controlDao.create(control);
		log.info("Created control - Id: " + controlId);
		return controlId;
	}

	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody int updateControl(@PathVariable final String scenario, @RequestBody final Control control) {
		int controlId = controlDao.update(control);
		log.info("Updated control - Id: " + controlId);
		return controlId;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{controlId}")
	public @ResponseBody String deleteControl(@PathVariable final String scenario, @PathVariable final String controlId) {
		log.info("Delete control - Id: " + controlId);
		controlDao.delete(Integer.valueOf(controlId));
		return controlId;
	}

}
