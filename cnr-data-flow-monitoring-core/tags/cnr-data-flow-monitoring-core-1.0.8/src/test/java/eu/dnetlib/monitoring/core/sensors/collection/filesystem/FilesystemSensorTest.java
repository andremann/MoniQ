package eu.dnetlib.monitoring.core.sensors.collection.filesystem;

import java.io.IOException;
import java.nio.charset.Charset;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import eu.dnetlib.monitoring.core.exceptions.SensorCreationException;
import eu.dnetlib.monitoring.core.exceptions.SensorInitException;
import eu.dnetlib.monitoring.core.sensors.SensorFactory;
import eu.dnetlib.monitoring.core.sensors.collection.CollectionSensor;
import eu.dnetlib.monitoring.model.Params;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by andrea on 24/06/16.
 */
public class FilesystemSensorTest {

	final CollectionSensor sensor;

	public FilesystemSensorTest() throws SensorCreationException, SensorInitException, IOException {
		sensor = (CollectionSensor) SensorFactory.createSensor(FilesystemSensor.class);
		String s = Resources.toString(getClass().getResource("filesystem.json"), Charset.defaultCharset());
		Params params = new Gson().fromJson(s, Params.class);
		sensor.getConfiguration().setParams(params);
	}

	@Ignore
	@Test
	public void test() {
		sensor.measure();
	}

}