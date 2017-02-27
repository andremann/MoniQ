package eu.dnetlib.monitoring.controls.analyzers;

import java.util.List;

import eu.dnetlib.monitoring.model.Observation;

public abstract class Analyzer {

	public abstract boolean analyze(List<Observation> observations);

	public abstract String getName();

	public abstract String getDescription();

	public String getCanonicalName() {
		return this.getClass().getCanonicalName();
	}

}
