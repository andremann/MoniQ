package eu.dnetlib.monitoring.aspects;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.google.gson.Gson;

import eu.dnetlib.monitoring.core.sensors.SensorCreationException;
import eu.dnetlib.monitoring.core.sensors.SensorFactory;
import eu.dnetlib.monitoring.core.sensors.collection.CollectionSensor;
import eu.dnetlib.monitoring.core.sensors.collection.SensorWrapper;
import eu.dnetlib.monitoring.core.sensors.collection.index.solr.SolrSensor;
import eu.dnetlib.monitoring.core.sensors.collection.kv.redis.RedisSensor;
import eu.dnetlib.monitoring.model.SensorConfiguration;

@Aspect
public class SolrIndexAspect {

	private final Log log = LogFactory.getLog(SolrIndexAspect.class);

	private SolrSensor solrSensor;
	private RedisSensor redisSensor;
	private SensorWrapper wrapper;

	public SolrIndexAspect() throws IOException, InstantiationException, IllegalAccessException, InterruptedException {
		log.info("Initializing aspect: " + this.getClass().getCanonicalName());

		/* Configuring SOLR sensor */
		String conf = IOUtils.toString(getClass().getResourceAsStream("/eu/dnetlib/monitoring/aspects/solr-conf.json"));
		SensorConfiguration solrConf = new SensorConfiguration(new Gson().fromJson(conf, HashMap.class));
		try {
			solrSensor = (SolrSensor) SensorFactory.createSensorWithConfiguration(CollectionSensor.class, "prepublic", "solr");
		} catch (SensorCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* Configuring Redis sensor */
		conf = IOUtils.toString(getClass().getResourceAsStream("/eu/dnetlib/monitoring/aspects/redis-conf.json"));
		SensorConfiguration redisConf = new SensorConfiguration(new Gson().fromJson(conf, HashMap.class));
		try {
			redisSensor = (RedisSensor) SensorFactory.createSensorWithConfiguration(CollectionSensor.class, "prepublic", "redis");
		} catch (SensorCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* Wrapping the two sensors together */
		wrapper = (SensorWrapper) SensorFactory.createSensor(SensorWrapper.class);
		wrapper.registerSensor(solrSensor);
		wrapper.registerSensor(redisSensor);

	}

	@Pointcut("execution(* eu.dnetlib.functionality.index.actors.IndexFeedActorImpl.feedIndex(..))")
	private void postFeedIndex() {};

	@AfterReturning("postFeedIndex()")
	public void executeAfter() {
		log.info("Executing aspect postFeedIndex()");
		wrapper.measure();
	}

}
