package eu.dnetlib.monitoring.core.sensors.datum.xml;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import eu.dnetlib.monitoring.core.sensors.datum.DatumSensor;
import eu.dnetlib.monitoring.model.Metric;
import eu.dnetlib.monitoring.model.Observation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class XMLCompletenessSensor extends DatumSensor<String> {

	private final Log log = LogFactory.getLog(XMLCompletenessSensor.class);
	private SAXReader reader = new SAXReader();

	public XMLCompletenessSensor() throws IOException {
		super();
	}

	public final List<Observation> getObservations(final String testedXml) {
		List<Observation> resultList = Lists.newArrayList();
		try {
			final Document doc = reader.read(new StringReader(testedXml));
			for (Metric metric : getConfiguration().getParams().getMetrics().values()) {
				final ArrayList<String> xpaths = new Gson().fromJson(metric.getParams().get("xpaths"), ArrayList.class);
				int counter = 0;
				for (String xpath : xpaths) {
					if (doc.selectNodes(xpath).size() > 0) {
						counter++;
					}
				}
				resultList.add(buildObservation(metric, (double) counter / xpaths.size()));
				}
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
