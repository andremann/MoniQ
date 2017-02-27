package eu.dnetlib.monitoring.core.sensors.collection.kv.redis;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import eu.dnetlib.monitoring.core.exceptions.MonitoringException;
import eu.dnetlib.monitoring.core.sensors.collection.CollectionSensor;

public class RedisSensor extends CollectionSensor {

	private final Log log = LogFactory.getLog(RedisSensor.class);

	private Jedis jedisCore;
	private String redisEndpoint;
	private String redisCollectionName;
	private Map<String, String> redisMap;

	@Override
	protected void init() {
		try {
			redisEndpoint = (String) getConfiguration().getParam("endpoint");
			jedisCore = new Jedis(redisEndpoint);
			redisCollectionName = (String) getConfiguration().getParam("collection");
			redisMap = jedisCore.hgetAll(redisCollectionName);
			jedisCore.close();
			if ((redisMap == null) || (redisMap.keySet().size() == 0))
				throw new MonitoringException(String.format("The collection %s does not exist (or is empty) on redis server %s", redisCollectionName,
						redisEndpoint));
		} catch (JedisConnectionException e) {
			log.error(String.format("Impossible to reach Redis server: %s", redisEndpoint), e);
		}
	}

	@Override
	protected Double produceObservationValue(final String... params) {
		try {
			String value = redisMap.get(params[0]);
			log.debug("Redis sensor observed: " + value);
			return value == null ? null : Double.valueOf(value.replace(",", ""));
		} catch (Throwable e) {
			throw new MonitoringException(String.format("A problem occurred when monitoring key '%s'", params[0]), e);
		}

	}

}
