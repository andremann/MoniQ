package eu.dnetlib.monitoring.core.connection;

import java.net.MalformedURLException;

import com.caucho.hessian.client.HessianProxyFactory;
import eu.dnetlib.monitoring.core.exceptions.SensorCreationException;
import eu.dnetlib.monitoring.core.properties.PropertyFetcher;
import eu.dnetlib.monitoring.exception.DataFlowMonitoringServiceRuntimeException;
import eu.dnetlib.monitoring.model.Observation;
import eu.dnetlib.monitoring.model.SensorConfiguration;
import eu.dnetlib.monitoring.rmi.DataFlowMonitoringAPI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DataFlowMonitoringClient {

	private static final Log log = LogFactory.getLog(DataFlowMonitoringClient.class);

	/* Singleton instance */
	private static DataFlowMonitoringClient instance = null;
	/* The service */
	DataFlowMonitoringAPI api;
	private String dfmServerHost;
	private int dfmRestPort;
	private String dfmServerContext;

	private DataFlowMonitoringClient() {
		dfmServerHost = PropertyFetcher.getPropertyAsString("dfm.server.host");
		dfmRestPort = PropertyFetcher.getPropertyAsInt("dfm.server.port");
		dfmServerContext = PropertyFetcher.getPropertyAsString("dfm.server.context");

		/* Hessian */
		HessianProxyFactory factory = new HessianProxyFactory();
		String url = "http://" + dfmServerHost + ":" + dfmRestPort + "/" + dfmServerContext + "/hessian/";

		try {
			api = (DataFlowMonitoringAPI) factory.create(DataFlowMonitoringAPI.class, url);
		} catch (MalformedURLException e) {
			log.error(e);
		}
	}

	/* Singleton getInstance */
	public static synchronized DataFlowMonitoringClient getInstance() {
		if (instance == null) {
			instance = new DataFlowMonitoringClient();
		}
		return instance;
	}

	public void deliverObservation(final Observation observation) {
		if ((observation != null)) {
			api.storeObservation(observation);
		}
	}

	public SensorConfiguration askConfiguration(final String scenario, final String confName) throws SensorCreationException {
		log.info(String.format("Fetching sensor configuration (%s) for scenario (%s)", confName, scenario));
		try {
			SensorConfiguration configuration = api.getConfigurationByName(scenario, confName);
			if ("inactive".equals(configuration.getStatus()))
				throw new SensorCreationException("The sensor is inactive and cannot be instantiated");
			return configuration;
		} catch (DataFlowMonitoringServiceRuntimeException e) {
			throw new SensorCreationException(scenario, confName, e);
		}
	}

	public String askConfigurationTemplate(final String scenario, final String confName) {
		log.info(String.format("Fetching sensor configuration template (%s) for scenario (%s)", confName, scenario));
		return api.getConfigurationTemplateByName(scenario, confName);
	}
}
