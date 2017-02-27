package eu.dnetlib.monitoring.core.sensors;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.gson.Gson;
import eu.dnetlib.monitoring.core.exceptions.SensorCreationException;
import eu.dnetlib.monitoring.core.exceptions.SensorInitException;
import eu.dnetlib.monitoring.model.SensorConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SensorFactory {

	private static final Log log = LogFactory.getLog(SensorFactory.class);

	private static MustacheFactory mf = new DefaultMustacheFactory();

	public static Sensor createSensor(final Class<? extends Sensor> clazz) throws SensorCreationException, SensorInitException {

		Sensor sensor;
		try {
			sensor = clazz.newInstance();
			sensor.setConfiguration(new SensorConfiguration());
			sensor.init();
		} catch (InstantiationException e) {
			throw new SensorCreationException("Cannot instantiate the sensor", e);
		} catch (IllegalAccessException e) {
			throw new SensorCreationException("Cannot instantiate the sensor", e);
		}
		return sensor;
	}

	public static Sensor createSensor(final Class<? extends Sensor> clazz, final Map<String, String> runtimeContextParams)
			throws SensorCreationException, SensorInitException {
		Sensor sensor = createSensor(clazz);
		sensor.getConfiguration().getParams().setGlobalParams(runtimeContextParams);
		return sensor;
	}

	public static Sensor createSensorWithConfiguration(final Class<? extends Sensor> clazz, final String scenario, final String configurationName)
			throws SensorCreationException, SensorInitException {
		try {
			Sensor sensor = clazz.newInstance();
			SensorConfiguration configuration = sensor.getClient().askConfiguration(scenario, configurationName);
			sensor.setConfiguration(configuration);
			sensor.init();
			return sensor;
		} catch (IllegalAccessException e) {
			throw new SensorCreationException(scenario, configurationName, e);
		} catch (InstantiationException e) {
			throw new SensorCreationException(scenario, configurationName, e);
		}
	}

	public static Sensor createSensorWithConfiguration(final Class<? extends Sensor> clazz,
			final String scenario,
			final String configurationName,
			final Map<String, String> runtimeContextParams) throws SensorCreationException, SensorInitException {
		try {
			Sensor sensor = clazz.newInstance();
			String configurationTemplate = sensor.getClient().askConfigurationTemplate(scenario, configurationName);
			Mustache mustache = mf.compile(new StringReader(configurationTemplate), "configuration");
			Writer writer = new StringWriter();
			mustache.execute(writer, runtimeContextParams);
			sensor.setConfiguration(new Gson().fromJson(writer.toString(), SensorConfiguration.class));
			sensor.init();
			return sensor;
		} catch (IllegalAccessException e) {
			throw new SensorCreationException(scenario, configurationName, e);
		} catch (InstantiationException e) {
			throw new SensorCreationException(scenario, configurationName, e);
		}
	}
}
