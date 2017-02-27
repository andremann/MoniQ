package eu.dnetlib.monitoring.server.dao.grafana;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import eu.dnetlib.monitoring.model.SensorConfiguration;
import eu.dnetlib.monitoring.server.dao.postgres.impl.ConfigurationDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Created by andrea on 07/07/16.
 */
public class DashboardDAO {

	private static final Log log = LogFactory.getLog(DashboardDAO.class);

	private String host;
	private String port;
	private String apiKey;

	private ConfigurationDAO configurationDAO;

	private CloseableHttpClient client;

	public DashboardDAO() {
		init();
	}

	public void init() {
		client = HttpClientBuilder.create().build();
	}

	public void postDashboard(String monitoringScenario) throws IOException {
		HttpResponse response;
		response = client.execute(requestFactory("GET", monitoringScenario));
		final String dashboard = readResponse(response);

		final List<SensorConfiguration> configurations = configurationDAO.listConfigurations(monitoringScenario);

		HttpPost request = (HttpPost) requestFactory("POST", null);
		request.setEntity(new StringEntity(dashboard));
		response = client.execute(request);
		readResponse(response);
	}

	public void deleteDashboard(String monitoringScenario) {

	}

	private HttpRequestBase requestFactory(String method, String slug) {
		HttpRequestBase request;
		switch (method) {
		case "GET":
			request = new HttpGet(String.format("http://%s:%s/api/dashboards/db/%s", host, port, slug));
			break;
		case "POST":
			request = new HttpPost(String.format("http://%s:%s/api/dashboards/db", host, port));
			break;
		case "DELETE":
			request = new HttpDelete(String.format("http://%s:%s/api/dashboards/db/%s", host, port, slug));
			break;
		default:
			request = null;
		}
		request.addHeader("Accept", "application/json");
		request.addHeader("Content-Type", "application/json");
		request.addHeader("Authorization", "Bearer " + apiKey);
		return request;
	}

	private String readResponse(HttpResponse response) throws IOException {
		log.info("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		log.info(result.toString());
		return result.toString();
	}

	public String getHost() {

		return host;
	}

	public void setHost(final String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(final String port) {
		this.port = port;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(final String apiKey) {
		this.apiKey = apiKey;
	}

	public ConfigurationDAO getConfigurationDAO() {
		return configurationDAO;
	}

	public void setConfigurationDAO(final ConfigurationDAO configurationDAO) {
		this.configurationDAO = configurationDAO;
	}
}
