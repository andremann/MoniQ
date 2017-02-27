package eu.dnetlib.monitoring.server.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by andrea on 26/05/16.
 */
@Controller
@RequestMapping("/servers")
public class ServerController {

	@Value("${data.flow.monitoring.backend.postgres.host}")
	private String postgresHost;

	@Value("${data.flow.monitoring.backend.influxdb.host}")
	private String influxHost;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public Map<String, String> listServers() {
		Map<String, String> map = new HashMap<>();
		map.put("postgres", postgresHost);
		map.put("influxdb", influxHost);
		return map;
	}

}
