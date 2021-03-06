package eu.dnetlib.monitoring.core.sensors.collection.kv.redis;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;
import eu.dnetlib.monitoring.core.sensors.collection.CollectionSensor;

public class RedisSensor extends CollectionSensor {

	private final Log log = LogFactory.getLog(RedisSensor.class);

	private Jedis jedisCore;
	private Map<String, String> hgetAll;

	@Override
	protected void init() {
		jedisCore = new Jedis((String) getConfiguration().getParam("endpoint"));
		hgetAll = jedisCore.hgetAll((String) getConfiguration().getParam("collection"));
		jedisCore.close();
	}

	@Override
	protected Double produceObservationValue(final String... params) {
		String value = hgetAll.get(params[0]);
		log.debug("Redis observer received: " + value);
		return value == null ? null : Double.valueOf(value.replace(",", ""));
	}

}
