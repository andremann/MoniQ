package eu.dnetlib.monitoring.controls.analyzers.rest;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import eu.dnetlib.monitoring.controls.analyzers.Analyzer;

@Controller
@RequestMapping("/analyzers")
public class AnalyzerController {

	private static final Logger log = Logger.getLogger(AnalyzerController.class);

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<Analyzer> listAnalyzers() throws InstantiationException, IllegalAccessException {
		log.info("Listing analyzers");
		List<Analyzer> list = Lists.newArrayList();
		Reflections reflections = new Reflections("eu.dnetlib.monitoring.controls.analyzers.impl");
		Set<Class<? extends Analyzer>> subTypes = reflections.getSubTypesOf(Analyzer.class);

		for (Class<? extends Analyzer> c : subTypes) {
			Analyzer a = c.newInstance();
			list.add(c.newInstance());
		}

		return list;
	}
}
