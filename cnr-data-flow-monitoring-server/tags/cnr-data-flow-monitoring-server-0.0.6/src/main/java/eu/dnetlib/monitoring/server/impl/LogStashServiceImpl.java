package eu.dnetlib.monitoring.server.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import eu.dnetlib.monitoring.model.Observation;
import eu.dnetlib.monitoring.rmi.LogStashService;
import eu.dnetlib.monitoring.server.dao.GenericStashDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

public class LogStashServiceImpl implements LogStashService {

	private static final Log log = LogFactory.getLog(LogStashServiceImpl.class);

	private GenericStashDAO dao;

	public void init() throws IOException {
		Server server = new Server();
		server.bind(54555, 54777);
		Kryo kryo = server.getKryo();
		kryo.register(Observation.class);
		kryo.register(Map.class);
		kryo.register(HashMap.class);
		server.addListener(new Listener() {

			@Override
			public void received(final Connection connection, final Object object) {
				if (object instanceof Observation) {
					boolean ret = stashRecord((Observation) object);
					if (!ret) {
						log.info(String.format("Cannot stash point -> %s(%s = %s)", ((Observation) object).getSensorType(), ((Observation) object).getMetric(),
								((Observation) object).getLog()));
					}
				} else {
					log.debug("Unknown message -> " + object.getClass());
				}
			}

			@Override
			public void connected(final Connection connection) {
				log.info("Client connected from " + connection.getRemoteAddressTCP());
			}

			@Override
			public void disconnected(final Connection connection) {
				log.info("Client disconnected..");
			}
		});
		server.start();
	}

	@Override
	public boolean stashRecord(final Observation record) {
		return dao.create(record);
	}

	public GenericStashDAO getDao() {
		return dao;
	}

	@Required
	public void setDao(final GenericStashDAO dao) {
		this.dao = dao;
	}

}
