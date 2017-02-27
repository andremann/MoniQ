package eu.dnetlib.monitoring.controls;

public class Selector {

	private String metric;
	private String labelName;
	private String labelValue;
	private Integer samples;

	public Selector() {

	}

	public Selector(final String metric, final String labelName, final String labelValue) {
		this.metric = metric;
		this.labelName = labelName;
		this.labelValue = labelValue;
	}

	public String getMetric() {
		return metric;
	}

	public void setMetric(final String metric) {
		this.metric = metric;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(final String labelName) {
		this.labelName = labelName;
	}

	public String getLabelValue() {
		return labelValue;
	}

	public void setLabelValue(final String labelValue) {
		this.labelValue = labelValue;
	}

	public Integer getSamples() {
		return samples;
	}

	public void setSamples(final Integer samples) {
		this.samples = samples;
	}

}
