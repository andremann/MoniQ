package eu.dnetlib.monitoring.core.sensors.collection.index.solr;

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
        solrCore = new CloudSolrServer((String) getConfiguration().getParam("endpoint"));
        solrCore.setDefaultCollection((String) getConfiguration().getParam("collection"));
    }

    @Override
    protected Double produceObservationValue(final String... params) {
        try {
            SolrQuery solrQuery = new SolrQuery(params[0]);
            QueryResponse response = solrCore.query(solrQuery);
            log.debug("Response received: " + response);
            SolrDocumentList results = response.getResults();
            return (double) results.getNumFound();
        } catch (Throwable e) {
            throw new MonitoringException(String.format("SOLR sensor reported an error when monitoring query '%s'", params[0]), e);
        }
    }

}
