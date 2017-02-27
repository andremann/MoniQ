package eu.dnetlib.monitoring.controls;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andrea
 *
 */
public class Control {

	private int id;
	private String name;
	private String monitoringScenario;
	private List<Selector> selectors;
	private String analyzerClass;
	private String status;
	private long lastModified;
	private boolean result;

	public Control() {
		this.selectors = new ArrayList<Selector>();
	}

	public Control(final String analyzerClass) {
		this.selectors = new ArrayList<Selector>();
		this.analyzerClass = analyzerClass;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Control addSelector(final Selector s) {
		this.selectors.add(s);
		return this;
	}

	public String getAnalyzerClass() {
		return analyzerClass;
	}

	public void setAnalyzerClass(final String analyzerClass) {
		this.analyzerClass = analyzerClass;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(final boolean result) {
		this.result = result;
	}

	public String getMonitoringScenario() {
		return monitoringScenario;
	}

	public void setMonitoringScenario(final String monitoringScenario) {
		this.monitoringScenario = monitoringScenario;
	}

	public List<Selector> getSelectors() {
		return selectors;
	}

	public void setSelectors(final List<Selector> selectors) {
		this.selectors = selectors;
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(final long lastModified) {
		this.lastModified = lastModified;
	}

}
