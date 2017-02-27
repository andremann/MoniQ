package eu.dnetlib.monitoring.controls.analyzers.impl;

import java.util.List;

import eu.dnetlib.monitoring.controls.analyzers.Analyzer;
import eu.dnetlib.monitoring.rmi.Observation;

public class IsMonotonicIncreasing extends Analyzer {

	@Override
	public boolean analyze(final List<Observation> observations) {
		if (observations.size() > 0) {
			Double pivot = observations.get(0).getLog();
			for (int i = 0; i < (observations.size()); i++) {
				if (observations.get(i).getLog() == pivot) {
					return false;
				} else {
					pivot = observations.get(i).getLog();
				}
			}
		}

		return true;
	}

	@Override
	public String getName() {
		return "Is monotonic increasing";
	}

	@Override
	public String getDescription() {
		return "Make sense for 1 selector only. Checks if the points are monotonic increasing.";
	}

}
