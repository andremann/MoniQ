package eu.dnetlib.monitoring.core.sensors.datum.pojo;

import java.io.IOException;
import java.nio.charset.Charset;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import eu.dnetlib.monitoring.core.exceptions.SensorCreationException;
import eu.dnetlib.monitoring.core.exceptions.SensorInitException;
import eu.dnetlib.monitoring.core.sensors.SensorFactory;
import eu.dnetlib.monitoring.core.sensors.datum.DatumSensor;
import eu.dnetlib.monitoring.model.Params;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by andrea on 28/06/16.
 */
public class PojoCompletenessSensorTest {

	private DatumSensor<APojo> sensor;

	public PojoCompletenessSensorTest() throws SensorCreationException, SensorInitException, IOException {
		sensor = (DatumSensor) SensorFactory.createSensor(PojoCompletenessSensor.class);
		String s = Resources.toString(getClass().getResource("testpojo.json"), Charset.defaultCharset());
		Params params = new Gson().fromJson(s, Params.class);
		sensor.getConfiguration().setParams(params);
	}

	@Ignore
	@Test
	public void testPojoSensor() {
		APojo pojo = new APojo("foo");
		sensor.measure(pojo);
	}

	private class APojo {

		private String str1;
		private String str2;
		private String empty;

		public APojo(final String str) {
			this.str1 = str;
			this.str2 = str;
			this.empty = "";
		}

		public String getEmpty() {
			return empty;
		}

		public void setEmpty(final String empty) {
			this.empty = empty;
		}

		public String getStr1() {
			return str1;
		}

		public void setStr1(final String str1) {
			this.str1 = str1;
		}

		public String getStr2() {
			return str2;
		}

		public void setStr2(final String str2) {
			this.str2 = str2;
		}
	}

}