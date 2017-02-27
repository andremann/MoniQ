package eu.dnetlib.monitoring.controls.analyzers.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import eu.dnetlib.monitoring.controls.analyzers.Analyzer;
import eu.dnetlib.monitoring.model.Observation;

public class SameTrend extends Analyzer {

	public enum Trend {
		INCR, DECR, UNKNOWN, UNDEFINED
	}

	private IsMonotonicIncreasing helperAnalyzerIncreasing = new IsMonotonicIncreasing();
	private IsMonotonicDecreasing helperAnalyzerDecreasing = new IsMonotonicDecreasing();

	/*
	 * Selectors (metric, labelName, labelValue, #samples)
	 */
	@Override
	public boolean analyze(final List<Observation> observations) {
		Multimap<String, Observation> observationsBySelector = ArrayListMultimap.create();
		for (Observation o : observations) {
			observationsBySelector.put(Joiner.on("-").join(o.getMetric(), getLabelValue(o)), o);
		}
		Trend expectedTrend = Trend.UNDEFINED;
		for (Collection<Observation> obs : observationsBySelector.asMap().values()) {
			List<Observation> obsList = Lists.newArrayList(obs);
			switch (expectedTrend) {
			case INCR:
				if (!helperAnalyzerIncreasing.analyze(obsList)) return false;
				break;
			case DECR:
				if (!helperAnalyzerDecreasing.analyze(obsList)) return false;
				break;
			case UNKNOWN:
				if (helperAnalyzerIncreasing.analyze(obsList) || helperAnalyzerDecreasing.analyze(obsList)) return false;
				break;
			// base case: UNDEFINED -- the first analysis
			default:
				if (helperAnalyzerIncreasing.analyze(obsList)) {
					expectedTrend = Trend.INCR;
				} else {
					if (helperAnalyzerDecreasing.analyze(obsList)) {
						expectedTrend = Trend.DECR;
					} else {
						expectedTrend = Trend.UNKNOWN;
					}
				}
			}
		}
		return true;
	}

	@Override
	public String getName() {
		return "Have same trend";
	}

	@Override
	public String getDescription() {
		return "Works with 2+ selectors. Checks if the two selectors have the same trend (increasing or decreasing).";
	}

	private String getLabelValue(final Observation o) {
		Iterator<String> values = o.getLabels().values().iterator();
		if (values.hasNext()) return values.next();
		else return null;
	}
}
