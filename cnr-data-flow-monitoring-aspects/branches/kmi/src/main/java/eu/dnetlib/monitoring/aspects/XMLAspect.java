package eu.dnetlib.monitoring.aspects;

import eu.dnetlib.monitoring.core.exceptions.SensorCreationException;
import eu.dnetlib.monitoring.core.exceptions.SensorInitException;
import eu.dnetlib.monitoring.core.sensors.SensorFactory;
import eu.dnetlib.monitoring.core.sensors.datum.xml.XMLCompletenessSensor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class XMLAspect {
	private final Log log = LogFactory.getLog(XMLAspect.class);

	private XMLCompletenessSensor s;

	public XMLAspect() throws InstantiationException, IllegalAccessException, InterruptedException, SensorInitException {
		log.info("Initializing aspect: " + this.getClass().getCanonicalName());
		try {
			s = (XMLCompletenessSensor) SensorFactory.createSensorWithConfiguration(XMLCompletenessSensor.class, "native", "xml");
		} catch (SensorCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterReturning(pointcut = "execution(* eu.dnetlib.enabling.resultset.client.ResultSetClientIterator.next())", returning = "str")
	public void resultSetNext(final String str) {
		s.measure(str);
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		log.info("Destroying aspect: " + this.getClass().getCanonicalName());
		super.finalize();
	}

}
