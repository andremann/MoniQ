package eu.dnetlib.monitoring.core.sensors.collection.index.solr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import eu.dnetlib.monitoring.core.sensors.collection.CollectionSensor;

public class SolrSensor extends CollectionSensor {

	private final Log log = LogFactory.getLog(SolrSensor.class);

	private CloudSolrServer solrCore;

	@Override
	protected void init() {
		solrCore = new CloudSolrServer((String) getConfiguration().getParam("endpoint"));
		solrCore.setDefaultCollection((String) getConfiguration().getParam("collection"));
	}

	@Override
	protected Double produceObservationValue(final String... params) {
		try {
			SolrQuery solrQuery = new SolrQuery(params[0]);
			QueryResponse response = solrCore.query(solrQuery);
			log.debug("Query received: " + response);
			SolrDocumentList results = response.getResults();
			return (double) results.getNumFound();
		} catch (SolrServerException e) {
			log.error("SOLR rensor reported an error: \n", e);
			return null;
		}
	}

}
