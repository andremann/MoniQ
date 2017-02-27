package eu.dnetlib.monitoring.core.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropertyFetcher {

	private static final Log log = LogFactory.getLog(PropertyFetcher.class);

	private static final String propertiesFile = "/eu/dnetlib/monitoring/dfm-client.properties";
	private static final String overridesFile = "/var/lib/dfm/dfm-client-overrides.properties";
	private static Properties props;

	public PropertyFetcher() {
		props = new Properties();
		InputStream is = getClass().getResourceAsStream(propertiesFile);
		if (is != null) {
			try {
				log.info("Loading default properties for data flow monitoring client from " + propertiesFile);
				props.load(is);
				is.close();
			} catch (IOException e) {
				log.error("Cannot read property file " + propertiesFile);
			}
		} else {
			log.error("Property file " + propertiesFile + " not found!");
		}
		// Load eventual overrides
		InputStream isover;
		try {
			isover = new FileInputStream(overridesFile);
			log.info("Loading overrides properties for data flow monitoring client from " + overridesFile);
			props.load(isover);
			isover.close();
		} catch (FileNotFoundException e1) {
			log.error("Overrides file " + overridesFile + " not found! Using defaults..");
		} catch (IOException e) {
			log.error("Cannot read overrides file " + overridesFile);
		}
	}

	public static String getPropertyAsString(final String key) {
		return props.getProperty(key);
	}

	public static int getPropertyAsInt(final String key) {
		return Integer.valueOf((props.getProperty(key)));
	}
}
