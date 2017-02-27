package eu.dnetlib.monitoring.core.sensors.collection.filesystem;

import java.io.File;
import java.util.Map;

import eu.dnetlib.monitoring.core.exceptions.SensorInitException;
import eu.dnetlib.monitoring.core.sensors.collection.CollectionSensor;

/**
 * Created by andrea on 21/06/16.
 */
public class FilesystemSensor extends CollectionSensor {

	@Override
	protected void init() throws SensorInitException {

	}

	@Override
	protected void shutdown() {

	}

	@Override
	protected Double produceObservationValue(final Map<String, String> allParams) {
		String path = allParams.get("path");
		int count = new File(path).list().length;
		return Double.valueOf(count);
	}

}
