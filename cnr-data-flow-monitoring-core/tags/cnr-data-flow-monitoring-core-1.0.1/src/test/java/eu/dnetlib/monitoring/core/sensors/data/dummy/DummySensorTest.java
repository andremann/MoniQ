package eu.dnetlib.monitoring.core.sensors.data.dummy;

import java.util.Random;

import eu.dnetlib.monitoring.core.exceptions.SensorCreationException;
import eu.dnetlib.monitoring.core.sensors.SensorFactory;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by andrea on 24/05/16.
 */
public class DummySensorTest {

	DummySensor dummySensor;

	public DummySensorTest() throws IllegalAccessException, SensorCreationException, InstantiationException {
		dummySensor = (DummySensor) SensorFactory.createSensorWithConfiguration(DummySensor.class, "native", "random");
	}

	@Ignore
	@Test
	public void test() throws InterruptedException {
		for (int i = 0; i < 5_000; i++) {
			dummySensor.measure(new Random().nextDouble());
		}
	}

}