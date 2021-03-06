package eu.dnetlib.monitoring.core.connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.google.gson.Gson;

import eu.dnetlib.monitoring.core.properties.PropertyFetcher;
import eu.dnetlib.monitoring.core.sensors.SensorCreationException;
import eu.dnetlib.monitoring.model.Observation;
import eu.dnetlib.monitoring.model.SensorConfiguration;

public class DataFlowMonitoringClient {

	private static final int BACKOFF_INIT = 2000;
	private static final int BACKOFF_LIMIT = 256000;
	private static final Log log = LogFactory.getLog(DataFlowMonitoringClient.class);

	/* Kryo client */
	private Client kryoClient;
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

	public void deliverObservation(final Observation observation) {
		if ((observation != null) && kryoClient.isConnected()) {
			kryoClient.sendTCP(observation);
		}
	}

	public SensorConfiguration askConfiguration(final String scenario, final String confName) throws SensorCreationException {
		String confUrl = "http://" + dfmServerAddress + ":" + dfmRestPort + "/dfm/ajax/scenarios/" + scenario + "/configurations/" + confName;
		log.info("Fetching sensor configuration from " + confUrl);
		HttpGet getRequest = new HttpGet(confUrl);
		try {
			HttpResponse response = client.execute(getRequest);
			String confString = IOUtils.toString(response.getEntity().getContent());
			log.debug("Configuration fetched: " + confString);
			SensorConfiguration conf = new Gson().fromJson(confString, SensorConfiguration.class);
			return conf;
		} catch (Throwable e) {
			throw new SensorCreationException(scenario, confName, e);
		}
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
