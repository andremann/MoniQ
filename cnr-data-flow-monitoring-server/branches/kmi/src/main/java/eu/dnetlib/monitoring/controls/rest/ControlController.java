package eu.dnetlib.monitoring.controls.rest;

import java.util.List;

import eu.dnetlib.monitoring.controls.Control;
import eu.dnetlib.monitoring.server.dao.GenericControlDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/scenarios/{scenario}/controls")
public class ControlController {

	private static final Log log = LogFactory.getLog(ControlController.class);

	@Autowired
	private GenericControlDAO controlDao;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public List<Control> listControls(@PathVariable final String scenario) {
		log.info(String.format("List controls for scenario (%s)", scenario));
		return controlDao.listControls(scenario);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public int createControl(@PathVariable final String scenario, @RequestBody final Control control) {
		int controlId = controlDao.create(control);
		log.info("Created control - Id: " + controlId);
		return controlId;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT)
	public int updateControl(@PathVariable final String scenario, @RequestBody final Control control) {
		int controlId = controlDao.update(control);
		log.info("Updated control - Id: " + controlId);
		return controlId;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE, value = "/{controlId}")
	public String deleteControl(@PathVariable final String scenario, @PathVariable final String controlId) {
		log.info("Delete control - Id: " + controlId);
		controlDao.delete(Integer.valueOf(controlId));
		return controlId;
	}

}
