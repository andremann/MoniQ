package eu.dnetlib.monitoring.core.sensors.data.structured.xml;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import eu.dnetlib.monitoring.core.sensors.data.DataSensor;
import eu.dnetlib.monitoring.model.Observation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class XMLCoverageSensor extends DataSensor<String> {

	private final Log log = LogFactory.getLog(XMLCoverageSensor.class);
	private SAXReader reader = new SAXReader();

	public XMLCoverageSensor() throws IOException {
		super();
	}

	@Override
	public final List<Observation> getObservations(final String xmlString) {
		List<Observation> resultList = Lists.newArrayList();
		final Observation observation = new Observation();
		try {
			final Document doc = reader.read(new StringReader(xmlString));
			int counter = 0;
			for (String xpath : (List<String>) getConfiguration().getParam("xpaths")) {
				observation.setSensorType(this.getClass().getSimpleName());
				observation.setSensorId(id);
				if (!Strings.isNullOrEmpty(doc.valueOf(xpath))) {
					counter++;
				}
			}
			observation.setMetric((String) getConfiguration().getParam("metric"));
			observation.setLabels((Map<String, String>) getConfiguration().getParam("labels"));
			observation.setLog(((double) counter / ((List<String>) getConfiguration().getParam("xpaths")).size()));
			observation.setMonitoringScenario((String) getConfiguration().getParam("monitoringScenario"));
			log.debug("Coverage measured: " + observation.getLog());
			resultList.add(observation);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resultList;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

}
