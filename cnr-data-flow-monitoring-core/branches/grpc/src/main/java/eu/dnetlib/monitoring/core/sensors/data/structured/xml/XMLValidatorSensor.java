package eu.dnetlib.monitoring.core.sensors.data.structured.xml;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.google.common.collect.Lists;

import eu.dnetlib.monitoring.core.sensors.data.DataSensor;
import eu.dnetlib.monitoring.rmi.Observation;

public class XMLValidatorSensor extends DataSensor<String> {

	private final Log log = LogFactory.getLog(XMLValidatorSensor.class);
	private final SAXReader reader = new SAXReader();

	public XMLValidatorSensor() throws IOException {
		super();
	}

	@Override
	public final List<Observation> getObservations(final String str) {
		final Observation.Builder record = Observation.newBuilder();
		record.setSensorType(this.getClass().getSimpleName());
		record.setSensorId(id);
		try {
			reader.read(new StringReader(str));
			record.setMetric("well_formedness");
			record.setLog(1);
			log.debug("XML valid");
		} catch (DocumentException e) {
			log.debug("XML invalid");
		}

		List<Observation> resultList = Lists.newArrayList();
		resultList.add(record.build());
		return resultList;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

}
