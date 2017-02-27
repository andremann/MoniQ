package eu.dnetlib.monitoring.aspects;

import eu.dnetlib.monitoring.core.sensors.Sensor;


public aspect ProfilingAspect {
//	private EchoSensor s;
	
	pointcut wfNodeExecution() : execution(* eu.dnetlib.msro.workflows.nodes.SarasvatiJobNode+.execute(..));
	
	Object around() : wfNodeExecution() {
		long start = System.currentTimeMillis();
		Object ret = proceed();
		long end = System.currentTimeMillis();
		System.out.println();
//		s.measure(thisJoinPointStaticPart.getSignature() + " took " + (end-start) + " milliseconds");
		return ret;
	}

}
