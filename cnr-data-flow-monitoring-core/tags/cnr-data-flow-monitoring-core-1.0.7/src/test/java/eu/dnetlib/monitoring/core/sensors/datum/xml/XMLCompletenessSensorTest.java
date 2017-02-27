package eu.dnetlib.monitoring.core.sensors.datum.xml;

import java.io.IOException;
import java.nio.charset.Charset;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import eu.dnetlib.monitoring.core.exceptions.SensorCreationException;
import eu.dnetlib.monitoring.core.exceptions.SensorInitException;
import eu.dnetlib.monitoring.core.sensors.SensorFactory;
import eu.dnetlib.monitoring.core.sensors.datum.DatumSensor;
import eu.dnetlib.monitoring.model.Params;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by andrea on 25/06/16.
 */
public class XMLCompletenessSensorTest {

	final DatumSensor<String> sensor;

	public XMLCompletenessSensorTest() throws SensorCreationException, SensorInitException, IOException {
		sensor = (DatumSensor) SensorFactory.createSensor(XMLCompletenessSensor.class);
		String s = Resources.toString(getClass().getResource("configuration.json"), Charset.defaultCharset());
		Params params = new Gson().fromJson(s, Params.class);
		sensor.getConfiguration().setParams(params);
	}

	@Ignore
	@Test
	public void testXmlCompleteness() throws IOException {
		String xml = Resources.toString(getClass().getResource("tested.xml"), Charset.defaultCharset());
		sensor.measure(xml);
	}

}