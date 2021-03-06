package eu.dnetlib.monitoring.core.connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.google.gson.Gson;

import eu.dnetlib.monitoring.core.properties.PropertyFetcher;
import eu.dnetlib.monitoring.model.Observation;
import eu.dnetlib.monitoring.model.SensorConfiguration;

public class DataFlowMonitoringClient {

	private static final int BACKOFF_INIT = 2000;
	private static final int BACKOFF_LIMIT = 256000;
	private static final Log log = LogFactory.getLog(DataFlowMonitoringClient.class);

	/* Kryo client */
	private Client kryoClient;
	private PropertyFetcher propertyFetcher = new PropertyFetcher();
	private String dfmServerAddress;
	private int kryonetUdpPort;
	private int kryonetTcpPort;
	private int kryonetTimeout;
	private int dfmRestPort;

	/* Rest client */
	private HttpClient client = new DefaultHttpClient();

	public DataFlowMonitoringClient() {
		dfmServerAddress = PropertyFetcher.getPropertyAsString("dfm.server.address");
		kryonetUdpPort = PropertyFetcher.getPropertyAsInt("dfm.server.kryonet.port.udp");
		kryonetTcpPort = PropertyFetcher.getPropertyAsInt("dfm.server.kryonet.port.tcp");
		kryonetTimeout = PropertyFetcher.getPropertyAsInt("dfm.server.kryonet.timeout");
		dfmRestPort = PropertyFetcher.getPropertyAsInt("dfm.server.rest.port");

		kryoClient = new Client();
		Kryo kryo = kryoClient.getKryo();
		kryo.register(Observation.class);
		kryo.register(Map.class);
		kryo.register(HashMap.class);
		kryoClient.addListener(new Listener() {

			@Override
			public void connected(final Connection connection) {
				log.info("Connected to Data Flow Monitoring Server on " + connection.getRemoteAddressTCP());
			}

			@Override
			public void disconnected(final Connection connection) {
				log.info("Ouch! Disconnected from Data Flow Monitoring Server..");
				new Thread(new RetryHandler()).start();
			}

		});

		kryoClient.start();
		try {
			kryoClient.connect(kryonetTimeout, dfmServerAddress, kryonetTcpPort, kryonetUdpPort);
		} catch (IOException e) {
			new Thread(new RetryHandler()).start();
		}
	}

	public void deliverObservation(final Observation message) {
		if ((message != null) && kryoClient.isConnected()) {
			kryoClient.sendTCP(message);
		}
	}

	public SensorConfiguration askConfiguration(final String confName) throws ClientProtocolException, IOException {
		HttpGet getRequest = new HttpGet("http://" + dfmServerAddress + ":" + dfmRestPort + "/dfm/ajax/scenarios/prepublic/configurations/" + confName);
		HttpResponse response = client.execute(getRequest);
		SensorConfiguration conf = new Gson().fromJson(IOUtils.toString(response.getEntity().getContent()), SensorConfiguration.class);
		return conf;
	}

	private class RetryHandler implements Runnable {

		int backoffMillis;

		@Override
		public void run() {
			backoffMillis = BACKOFF_INIT;
			while (!kryoClient.isConnected()) {
				try {
					log.error("Cannot reach Data Flow Monitoring Server @" + dfmServerAddress + ".. retrying in " + (backoffMillis / 1000) + " seconds");
					Thread.sleep(backoffMillis);
					try {
						kryoClient.connect(kryonetTimeout, dfmServerAddress, kryonetTcpPort, kryonetUdpPort);
					} catch (IOException e) {
						if (backoffMillis <= BACKOFF_LIMIT) { // Stops binary backoff when reach about 4 minutes
							backoffMillis = backoffMillis * 2;
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	};
}
