package eu.dnetlib.monitoring.controls.analyzers.impl;

import eu.dnetlib.monitoring.controls.analyzers.Analyzer;
import eu.dnetlib.monitoring.model.Observation;

import java.util.List;

public class AboveThreshold extends Analyzer {

    @Override
    public boolean analyze(final List<Observation> observations) {
        for (Observation observation : observations) {
            if (observation.getValue() < 0.9)
                return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return "Above 0.9 threshold";
    }

    @Override
    public String getDescription() {
        return "Works with 1 selector. Checks if ALL the provided observations are above 0.9 threshold.";
    }

}
