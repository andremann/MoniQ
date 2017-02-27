package eu.dnetlib.monitoring.core.sensors.flow.structured.xml;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.google.common.collect.Lists;

import eu.dnetlib.monitoring.core.sensors.flow.FlowSensor;
import eu.dnetlib.monitoring.model.Observation;

public class XMLDataSensor extends FlowSensor<String> {

	private final Log log = LogFactory.getLog(XMLDataSensor.class);
	private int count = 0;
	private SAXReader reader = new SAXReader();

	public XMLDataSensor() throws IOException {
		super();
	}

	@Override
	public final List<Observation> getObservations(final String str) {
		try {
			Document doc = reader.read(new StringReader(str));
			if (doc.valueOf("//material").equals("Limestone")) {
				count++;
			}
			log.info(count);
			Observation record = new Observation();
			record.setLog((double) count);

			List<Observation> resultList = Lists.newArrayList();
			resultList.add(record);
			return resultList;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

}
