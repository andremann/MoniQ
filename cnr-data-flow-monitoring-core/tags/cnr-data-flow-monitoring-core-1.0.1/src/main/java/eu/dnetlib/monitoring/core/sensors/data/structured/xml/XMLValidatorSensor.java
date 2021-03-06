package eu.dnetlib.monitoring.core.sensors.data.structured.xml;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import com.google.common.collect.Lists;
import eu.dnetlib.monitoring.core.sensors.data.DatumSensor;
import eu.dnetlib.monitoring.model.Observation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class XMLValidatorSensor extends DatumSensor<String> {

	private final Log log = LogFactory.getLog(XMLValidatorSensor.class);
	private final SAXReader reader = new SAXReader();

	public XMLValidatorSensor() throws IOException {
		super();
	}

	public final List<Observation> getObservations(final String str) {
		final Observation record = new Observation();
		record.setSensorType(this.getClass().getSimpleName());
		record.setSensorId(id);
		try {
			reader.read(new StringReader(str));
			record.setMetric("well_formed");
			record.setValue((double) 1);
			log.debug("XML valid");
		} catch (DocumentException e) {
			log.debug("XML invalid");
		}

		List<Observation> resultList = Lists.newArrayList();
		resultList.add(record);
		return resultList;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

}
