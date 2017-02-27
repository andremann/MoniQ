package eu.dnetlib.monitoring.server.impl;

import com.caucho.hessian.server.HessianServlet;
import com.google.gson.Gson;
import eu.dnetlib.monitoring.exception.DataFlowMonitoringServiceRuntimeException;
import eu.dnetlib.monitoring.model.Observation;
import eu.dnetlib.monitoring.model.SensorConfiguration;
import eu.dnetlib.monitoring.rmi.DataFlowMonitoringAPI;
import eu.dnetlib.monitoring.server.dao.GenericConfigurationDAO;
import eu.dnetlib.monitoring.server.dao.GenericObservationDAO;
import eu.dnetlib.monitoring.server.dao.exceptions.ConfigurationNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

public class DataFlowMonitoringService extends HessianServlet implements DataFlowMonitoringAPI {

	private static final Log log = LogFactory.getLog(DataFlowMonitoringService.class);

	private GenericObservationDAO observationDAO;
	private GenericConfigurationDAO configurationDAO;

	@Override
	public boolean storeObservation(final Observation observation) {
		return observationDAO.create(observation);
	}

	@Override
	public SensorConfiguration getConfigurationByName(final String scenario, final String name) {
		try {
			return configurationDAO.getConfiguration(scenario, name);
		} catch (ConfigurationNotFoundException e) {
			throw new DataFlowMonitoringServiceRuntimeException("Cannot find the configuration", e);
		}
	}

	@Override
	public String getConfigurationTemplateByName(final String scenario, final String name) {
		return new Gson().toJson(getConfigurationByName(scenario, name));
	}

	public GenericObservationDAO getObservationDAO() {
		return observationDAO;
	}

	@Required
	public void setObservationDAO(final GenericObservationDAO observationDAO) {
		this.observationDAO = observationDAO;
	}

	public GenericConfigurationDAO getConfigurationDAO() {
		return configurationDAO;
	}

	@Required
	public void setConfigurationDAO(final GenericConfigurationDAO configurationDAO) {
		this.configurationDAO = configurationDAO;
	}
}
