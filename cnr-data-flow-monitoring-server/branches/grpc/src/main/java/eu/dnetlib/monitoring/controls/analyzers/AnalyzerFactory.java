package eu.dnetlib.monitoring.controls.analyzers;


public class AnalyzerFactory {

	public static Analyzer createAnalyzer(final String analyzerClass) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return (Analyzer) Class.forName(analyzerClass).newInstance();
	}

}
