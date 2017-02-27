package eu.dnetlib.monitoring.controls.analyzers.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import eu.dnetlib.monitoring.model.Observation;

public class SameTrendTest {

	private SameTrend sameTrendAnalyzer = new SameTrend();
	private Map<String, String> solrLabels = Maps.newHashMap();
	private Map<String, String> redisLabels = Maps.newHashMap();

	@Before
	public void setUp() throws Exception {
		solrLabels.put("collectionType", "solr");
		redisLabels.put("collectionType", "redis");
	}

	@Test
	public void testAnalyzeSameTrend() {
		List<Observation> observations = Lists.newArrayList();
		observations.add(createObservation(solrLabels, 1.0, "theMetric"));
		observations.add(createObservation(solrLabels, 2.0, "theMetric"));
		observations.add(createObservation(solrLabels, 2.1, "theMetric"));

		observations.add(createObservation(redisLabels, 1.5, "anotherMetric"));
		observations.add(createObservation(redisLabels, 12.0, "anotherMetric"));
		observations.add(createObservation(redisLabels, 21.1, "anotherMetric"));

		assertTrue(sameTrendAnalyzer.analyze(observations));
	}

	@Test
	public void testAnalyzeNotSameTrend() {
		List<Observation> observations = Lists.newArrayList();
		observations.add(createObservation(solrLabels, 1.0, "theMetric"));
		observations.add(createObservation(solrLabels, 2.0, "theMetric"));
		observations.add(createObservation(solrLabels, 2.1, "theMetric"));

		observations.add(createObservation(redisLabels, 1.5, "theMetric"));
		observations.add(createObservation(redisLabels, 12.0, "theMetric"));
		observations.add(createObservation(redisLabels, 1.1, "theMetric"));

		assertFalse(sameTrendAnalyzer.analyze(observations));
	}

	@Test
	public void testAnalyzeNotTrends() {
		List<Observation> observations = Lists.newArrayList();
		observations.add(createObservation(solrLabels, 1.0, "theMetric"));
		observations.add(createObservation(solrLabels, 2.0, "theMetric"));
		observations.add(createObservation(solrLabels, 1.1, "theMetric"));

		observations.add(createObservation(redisLabels, 31.5, "theMetric"));
		observations.add(createObservation(redisLabels, 12.0, "theMetric"));
		observations.add(createObservation(redisLabels, 50.1, "theMetric"));

		assertTrue(sameTrendAnalyzer.analyze(observations));
	}

	private Observation createObservation(final Map<String, String> labels, final Double observationValue, final String metric) {
		Observation o = new Observation();
		o.setLabels(labels);
		o.setLog(observationValue);
		o.setMetric(metric);
		return o;
	}

}
