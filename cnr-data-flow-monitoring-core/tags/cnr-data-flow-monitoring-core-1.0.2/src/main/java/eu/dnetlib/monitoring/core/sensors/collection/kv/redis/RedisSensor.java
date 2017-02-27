package eu.dnetlib.monitoring.core.sensors.collection.kv.redis;

import java.util.Map;

import eu.dnetlib.monitoring.core.exceptions.MonitoringException;
import eu.dnetlib.monitoring.core.sensors.collection.CollectionSensor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisSensor extends CollectionSensor {

    private final Log log = LogFactory.getLog(RedisSensor.class);

    private String redisEndpoint;
    private Map<String, String> redisMap;

    @Override
    protected void init() {
        try {
            redisEndpoint = getConfiguration().getParams().getGlobalParams().get("endpoint");
            Jedis jedisCore = new Jedis(redisEndpoint);
            String redisCollectionName = getConfiguration().getParams().getGlobalParams().get("collection");
            redisMap = jedisCore.hgetAll(redisCollectionName);
            jedisCore.close();
            if ((redisMap == null) || (redisMap.keySet().size() == 0))
                throw new MonitoringException(String.format("The collection %s does not exist (or is empty) on redis server %s", redisCollectionName,
                        redisEndpoint));
        } catch (JedisConnectionException e) {
            log.error(String.format("Impossible to reach Redis server: %s", redisEndpoint), e);
            throw new MonitoringException(String.format("Impossible to reach Redis server: %s", redisEndpoint), e);
        }
    }

    @Override
    protected Double produceObservationValue(final Map<String, String> allParams) {
        String query = allParams.get("queryString");
        try {
            String value = redisMap.get(query);
            log.debug("Redis sensor observed: " + value);
            return value == null ? null : Double.valueOf(value.replace(",", ""));
        } catch (Throwable e) {
            log.error(String.format("A problem occurred when monitoring key '%s'", query), e);
            throw new MonitoringException(String.format("A problem occurred when monitoring key '%s'", query), e);
        }

    }

    @Override
    protected void shutdown() {
    }

}
