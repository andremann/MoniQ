package eu.dnetlib.monitoring.core.sensors.flow.dummy;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.dnetlib.monitoring.core.sensors.flow.FlowSensor;
import eu.dnetlib.monitoring.model.Observation;

public class EchoSensor extends FlowSensor<String> {

	private final Log log = LogFactory.getLog(EchoSensor.class);

	public EchoSensor() throws IOException {
		super();
	}

	@Override
	public List<Observation> getObservations(final String str) {
		log.info(str);
		return null;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

}
