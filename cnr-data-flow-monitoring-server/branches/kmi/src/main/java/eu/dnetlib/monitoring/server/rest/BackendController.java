package eu.dnetlib.monitoring.server.rest;

import java.io.Serializable;
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
@RequestMapping("/backends")
public class BackendController {

	@Value("${data.flow.monitoring.backend.postgres.host}")
	private String postgresHost;
	@Value("${data.flow.monitoring.backend.postgres.port}")
	private String postgresPort;

	@Value("${data.flow.monitoring.backend.influxdb.host}")
	private String influxHost;
	@Value("${data.flow.monitoring.backend.influxdb.admin.port}")
	private String influxAdminPort;

	@Value("${data.flow.monitoring.backend.grafana.host}")
	private String grafanaHost;
	@Value("${data.flow.monitoring.backend.grafana.port}")
	private String grafanaPort;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public Map<String, HostPortCouple> listServers() {
		Map<String, HostPortCouple> map = new HashMap<>();
		map.put("postgres", new HostPortCouple(postgresHost, postgresPort));
		map.put("influxdb", new HostPortCouple(influxHost, influxAdminPort));
		map.put("grafana", new HostPortCouple(grafanaHost, grafanaPort));
		return map;
	}

	private class HostPortCouple implements Serializable {

		private String host;
		private String port;

		public HostPortCouple(final String host, final String port) {
			this.host = host;
			this.port = port;
		}

		public String getPort() {
			return port;
		}

		public void setPort(final String port) {
			this.port = port;
		}

		public String getHost() {
			return host;
		}

		public void setHost(final String host) {
			this.host = host;
		}
	}

}
