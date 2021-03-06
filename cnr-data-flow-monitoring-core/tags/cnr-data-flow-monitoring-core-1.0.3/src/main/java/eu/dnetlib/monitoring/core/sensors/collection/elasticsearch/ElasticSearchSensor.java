package eu.dnetlib.monitoring.core.sensors.collection.elasticsearch;

import java.util.Map;

import eu.dnetlib.monitoring.core.exceptions.SensorInitException;
import eu.dnetlib.monitoring.core.sensors.collection.CollectionSensor;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * Created by andrea on 03/06/16.
 */
public class ElasticSearchSensor extends CollectionSensor {

	Client elasticSearchClient;

	@Override
	protected void init() throws SensorInitException {
		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "core_cluster").build();
		elasticSearchClient = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(
				getConfiguration().getParams().getGlobalParams().get("endpoint"),
				Integer.parseInt(getConfiguration().getParams().getGlobalParams().get("port"))));
	}

	@Override
	protected Double produceObservationValue(final Map<String, String> allParams) {
		String queryString = String.format(allParams.get("queryString"));
		QueryBuilder query = QueryBuilders.queryString(queryString);
		final CountResponse response = elasticSearchClient.prepareCount(allParams.get("index")).setQuery(query).execute().actionGet();
		return (double) response.getCount();
	}

	@Override
	protected void shutdown() {

	}

}
