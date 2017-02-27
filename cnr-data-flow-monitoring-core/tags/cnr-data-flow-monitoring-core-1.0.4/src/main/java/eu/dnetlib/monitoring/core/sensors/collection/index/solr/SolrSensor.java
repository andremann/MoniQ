package eu.dnetlib.monitoring.core.sensors.collection.index.solr;

import java.util.Map;

import eu.dnetlib.monitoring.core.exceptions.MonitoringException;
import eu.dnetlib.monitoring.core.sensors.collection.CollectionSensor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

public class SolrSensor extends CollectionSensor {

    private final Log log = LogFactory.getLog(SolrSensor.class);

    private CloudSolrServer solrCore;

    @Override
    protected void init() {
        solrCore = new CloudSolrServer(getConfiguration().getParams().getGlobalParams().get("endpoint"));
        solrCore.setDefaultCollection(getConfiguration().getParams().getGlobalParams().get("collection"));
    }

    @Override
    protected Double produceObservationValue(final Map<String, String> allParams) {
        String query = allParams.get("queryString");
        try {
            SolrQuery solrQuery = new SolrQuery(query);
            QueryResponse response = solrCore.query(solrQuery);
            log.debug("Response received: " + response);
            SolrDocumentList results = response.getResults();
            return (double) results.getNumFound();
        } catch (Throwable e) {
            throw new MonitoringException(String.format("SOLR sensor reported an error when monitoring query '%s'", query), e);
        }
    }

    @Override
    protected void shutdown() {
        solrCore.shutdown();
    }

}
