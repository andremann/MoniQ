package eu.dnetlib.monitoring.controls.analyzers.impl;

import java.util.List;

import eu.dnetlib.monitoring.controls.analyzers.Analyzer;
import eu.dnetlib.monitoring.rmi.Observation;

public class AreEqual extends Analyzer {

	@Override
	public boolean analyze(final List<Observation> observations) {
		if (observations.size() > 1) {
			Double testLog = observations.get(0).getLog();
			for (int i = 0; i < (observations.size()); i++) {
				if (observations.get(i).getLog() == testLog) { return false; }
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
