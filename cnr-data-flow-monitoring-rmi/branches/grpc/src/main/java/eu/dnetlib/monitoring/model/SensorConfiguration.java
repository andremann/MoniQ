package eu.dnetlib.monitoring.model;

import java.util.HashMap;
import java.util.Map;

public class SensorConfiguration {

	private Map<String, Object> params;

	public SensorConfiguration() {
		params = new HashMap<String, Object>();
	}

	public SensorConfiguration(final Map<String, Object> map) {
		params = new HashMap<String, Object>();
		params.putAll(map);
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public SensorConfiguration setParam(final String name, final Object obj) {
		params.put(name, obj);
		return this;
	}

	public Object getParam(final String name) {
		return params.get(name);
	}

}
