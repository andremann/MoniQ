package eu.dnetlib.monitoring.core.client;

import io.grpc.ChannelImpl;
import io.grpc.transport.netty.NegotiationType;
import io.grpc.transport.netty.NettyChannelBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import eu.dnetlib.monitoring.core.properties.PropertyFetcher;
import eu.dnetlib.monitoring.model.SensorConfiguration;
import eu.dnetlib.monitoring.rmi.LogStashServiceGrpc;
import eu.dnetlib.monitoring.rmi.LogStashServiceGrpc.LogStashServiceBlockingStub;
import eu.dnetlib.monitoring.rmi.LogStashServiceGrpc.LogStashServiceStub;
import eu.dnetlib.monitoring.rmi.Observation;

public class DataFlowMonitoringGrpcClient {

	private static final Log log = LogFactory.getLog(DataFlowMonitoringGrpcClient.class);

	private static final int BATCH_SIZE = 100;
	private static final int FLUSH_INTERVAL_IN_MILLIS = 300;

	/* Rest client */
	private HttpClient client;

	private final ChannelImpl channel;
	private final LogStashServiceBlockingStub blockingStub;
	private final LogStashServiceStub asyncStub;

	private String dfmServerAddress;
	private int dfmRestPort;
	private int dfmBinaryPort;

	BatchProcessor batchProcessor;

	public DataFlowMonitoringGrpcClient() {
		client = new DefaultHttpClient();
		dfmServerAddress = PropertyFetcher.getPropertyAsString("dfm.server.address");
		dfmRestPort = PropertyFetcher.getPropertyAsInt("dfm.server.rest.port");
		dfmBinaryPort = PropertyFetcher.getPropertyAsInt("dfm.server.binary.port");

		channel = NettyChannelBuilder.forAddress(dfmServerAddress, dfmBinaryPort)
				.negotiationType(NegotiationType.PLAINTEXT)
				.build();
		blockingStub = LogStashServiceGrpc.newBlockingStub(channel);
		asyncStub = LogStashServiceGrpc.newStub(channel);

		batchProcessor = BatchProcessor.builder(this).batchSize(BATCH_SIZE).interval(FLUSH_INTERVAL_IN_MILLIS, TimeUnit.MILLISECONDS).build();
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTerminated(5, TimeUnit.SECONDS);
	}

	public void deliverObservation(final Observation observation) {
		batchProcessor.put(observation);
	}

	public SensorConfiguration askConfiguration(final String scenario, final String confName) throws ClientProtocolException, IOException {
		String confUrl = "http://" + dfmServerAddress + ":" + dfmRestPort + "/dfm/ajax/scenarios/" + scenario + "/configurations/" + confName;
		log.info("Fetching sensor configuration from " + confUrl);
		HttpGet getRequest = new HttpGet(confUrl);
		HttpResponse response = client.execute(getRequest);
		String confString = IOUtils.toString(response.getEntity().getContent());
		log.debug("Configuration fetched: " + confString);
		SensorConfiguration conf = new Gson().fromJson(confString, SensorConfiguration.class);
		return conf;
	}

	public LogStashServiceBlockingStub getBlockingStub() {
		return blockingStub;
	}

	public LogStashServiceStub getAsyncStub() {
		return asyncStub;
	}
}
