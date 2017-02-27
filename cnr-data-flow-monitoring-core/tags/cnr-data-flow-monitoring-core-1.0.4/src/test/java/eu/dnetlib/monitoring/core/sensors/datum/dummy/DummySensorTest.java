package eu.dnetlib.monitoring.core.sensors.datum.dummy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import eu.dnetlib.monitoring.core.exceptions.SensorCreationException;
import eu.dnetlib.monitoring.core.exceptions.SensorInitException;
import eu.dnetlib.monitoring.core.sensors.SensorFactory;
import eu.dnetlib.monitoring.model.Params;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by andrea on 24/05/16.
 */
public class DummySensorTest {

	DummySensor dummySensor1;
	DummySensor dummySensor2;

	public DummySensorTest() throws SensorCreationException, SensorInitException, IOException {
		dummySensor1 = (DummySensor) SensorFactory.createSensor(DummySensor.class);
		String s1 = Resources.toString(getClass().getResource("test1.json"), Charset.defaultCharset());
		Params params1 = new Gson().fromJson(s1, Params.class);
		dummySensor1.getConfiguration().setParams(params1);

		dummySensor2 = (DummySensor) SensorFactory.createSensor(DummySensor.class);
		String s2 = Resources.toString(getClass().getResource("test2.json"), Charset.defaultCharset());
		Params params2 = new Gson().fromJson(s2, Params.class);
		dummySensor2.getConfiguration().setParams(params2);
	}

	@Ignore
	@Test
	public void testTwoRandomSensors() throws InterruptedException {
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 50; i++) {
					dummySensor1.measure(new Random().nextDouble());
				}
			}
		});

		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 50; i++) {
					dummySensor2.measure(new Random().nextDouble());
				}
			}
		});

		t1.start();
		t2.start();

		t1.join();
		t2.join();

	}

}