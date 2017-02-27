package eu.dnetlib.monitoring.core.sensors.collection.lod.virtuoso;

import java.util.List;

import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by andrea on 07/10/2016.
 */
public class VirtuosoSensorTest {

	private static final Log log = LogFactory.getLog(VirtuosoSensorTest.class);

	private static final List<String> queries = Lists.newArrayList(
			"PREFIX oav: <http://lod.openaire.eu/vocab/> SELECT (COUNT(DISTINCT ?instance) AS ?count) where{ ?instance oav:resultType \"publication\"}",
			"PREFIX oav: <http://lod.openaire.eu/vocab/> SELECT (COUNT(DISTINCT ?instance) AS ?count) where{ ?instance oav:resultType \"dataset\"}",
			"PREFIX oav: <http://lod.openaire.eu/vocab/> SELECT (COUNT(DISTINCT ?instance) AS ?count) where{ ?instance rdf:type cerif:ResultEntity}",
			"PREFIX oav: <http://lod.openaire.eu/vocab/> SELECT (COUNT(DISTINCT ?instance) AS ?count) where{ ?instance rdf:Type foaf:Organization}",
			"PREFIX oav: <http://lod.openaire.eu/vocab/> SELECT (COUNT(DISTINCT ?instance) AS ?count) where{ ?instance rdf:Type prov:Entity}",
			"PREFIX oav: <http://lod.openaire.eu/vocab/> SELECT (COUNT(DISTINCT ?instance) AS ?count) where{ ?instance rdf:Type cerif:Project}",
			"PREFIX oav: <http://lod.openaire.eu/vocab/> SELECT (COUNT(DISTINCT ?instance) AS ?count) where{ ?instance rdf:Type foaf:Person}");


	@Test
	@Ignore
	public void testLodSensor() throws Exception {
		VirtuosoSensor s = new VirtuosoSensor();
		for (String q : queries) {
			log.debug("q = " + q);
			log.debug(s.produceObservationValue(q));
		}
	}
}