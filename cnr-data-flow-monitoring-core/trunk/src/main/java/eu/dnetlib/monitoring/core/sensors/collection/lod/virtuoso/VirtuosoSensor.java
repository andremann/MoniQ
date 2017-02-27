package eu.dnetlib.monitoring.core.sensors.collection.lod.virtuoso;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import eu.dnetlib.monitoring.core.exceptions.MonitoringException;
import eu.dnetlib.monitoring.core.sensors.collection.CollectionSensor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Created by andrea on 07/10/2016.
 */
public class VirtuosoSensor extends CollectionSensor {

	final static String endpoint = "http://beta.lod.openaire.eu/sparql";
	private static final Log log = LogFactory.getLog(VirtuosoSensor.class);
	private HttpClient httpClient = HttpClientBuilder.create().build();

	@Override
	protected void init() {
	}

	@Override
	protected Double produceObservationValue(final String... params) {
		try {
			URI uri = new URIBuilder(endpoint)
					.addParameter("query", params[0])
					.addParameter("format", "application/json")
					.build();
			HttpGet getRequest = new HttpGet(uri);
			HttpResponse response = httpClient.execute(getRequest);
			String json = IOUtils.toString(response.getEntity().getContent());
			getRequest.releaseConnection();
			log.debug("LOD json response = " + json);
			JsonElement jElement = new JsonParser().parse(json);
			try {
				String value = jElement.getAsJsonObject().get("results").getAsJsonObject()
						.get("bindings").getAsJsonArray().get(0).getAsJsonObject()
						.get("count").getAsJsonObject()
						.get("value").getAsString();
				return new Double(value);
			} catch (Exception e) {
				throw new MonitoringException(String.format("JSON format returned from virtuoso is different from what was expected", params[0]), e);
			}
		} catch (URISyntaxException e) {
			throw new MonitoringException(String.format("LOD sensor reported an error when monitoring query '%s'", params[0]), e);
		} catch (ClientProtocolException e) {
			throw new MonitoringException(String.format("LOD sensor reported an error when monitoring query '%s'", params[0]), e);
		} catch (IOException e) {
			throw new MonitoringException(String.format("LOD sensor reported an error when monitoring query '%s'", params[0]), e);
		}
	}
}
