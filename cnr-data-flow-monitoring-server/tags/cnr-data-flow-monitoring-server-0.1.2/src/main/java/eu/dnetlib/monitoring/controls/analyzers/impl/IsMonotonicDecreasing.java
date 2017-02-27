package eu.dnetlib.monitoring.controls.analyzers.impl;

import java.util.List;

import eu.dnetlib.monitoring.controls.analyzers.Analyzer;
import eu.dnetlib.monitoring.model.Observation;

public class IsMonotonicDecreasing extends Analyzer {

	@Override
	public boolean analyze(final List<Observation> observations) {
		if (observations.size() > 0) {
			Double pivot = observations.get(0).getLog();
			for (Observation o : observations) {
				if (o.getLog().compareTo(pivot) > 0) return false;
				else {
					pivot = o.getLog();
				}
			}
		}

		return true;
	}

	@Override
	public String getName() {
		return "Is monotonic decreasing";
	}

	@Override
	public String getDescription() {
		return "Make sense for 1 selector only. Checks if the points are monotonic decreasing.";
	}

}
