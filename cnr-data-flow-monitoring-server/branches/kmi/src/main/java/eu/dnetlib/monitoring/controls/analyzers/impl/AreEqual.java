package eu.dnetlib.monitoring.controls.analyzers.impl;

import java.util.List;

import eu.dnetlib.monitoring.controls.analyzers.Analyzer;
import eu.dnetlib.monitoring.model.Observation;

public class AreEqual extends Analyzer {

	@Override
	public boolean analyze(final List<Observation> observations) {
		if (observations.size() > 1) {
			Double testValue = observations.get(0).getValue();
			for (Observation o : observations) {
				if (o.getValue().compareTo(testValue) != 0) return false;
			}
		}
		return true;
	}

	@Override
	public String getName() {
		return "Are equal";
	}

	@Override
	public String getDescription() {
		return "Works with 1+ selectors. Checks if ALL the provided points are equal.";
	}

}
