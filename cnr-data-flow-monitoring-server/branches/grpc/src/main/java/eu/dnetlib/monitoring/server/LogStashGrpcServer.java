package eu.dnetlib.monitoring.server;

import io.grpc.ServerImpl;
import io.grpc.stub.StreamObserver;
import io.grpc.transport.netty.NettyServerBuilder;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import eu.dnetlib.monitoring.rmi.GenericResponse;
import eu.dnetlib.monitoring.rmi.LogStashServiceGrpc;
import eu.dnetlib.monitoring.rmi.Observation;
import eu.dnetlib.monitoring.rmi.ObservationBatch;
import eu.dnetlib.monitoring.server.dao.GenericStashDAO;

public class LogStashGrpcServer {

	private static final Logger log = Logger.getLogger(LogStashGrpcServer.class);

	private GenericStashDAO dao;

	/* The port on which the server should run */
	private int port = 50051;
	private ServerImpl server;

	public void initialize() throws IOException {
		server = NettyServerBuilder.forPort(port)
				.addService(LogStashServiceGrpc.bindService(new LogStashServiceImpl()))
				.build().start();
		log.info("Server started, listening on " + port);
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				// Use stderr here since the logger may have been reset by its JVM shutdown hook.
				log.info("*** shutting down gRPC server since JVM is shutting down");
				LogStashGrpcServer.this.stop();
				log.info("*** server shut down");
			}
		});
	}

	protected void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	private class LogStashServiceImpl implements LogStashServiceGrpc.LogStashService {

		@Override
		public void deliverObservation(final Observation observation, final StreamObserver<GenericResponse> responseObserver) {
			responseObserver.onValue(GenericResponse.newBuilder().setSuccess(dao.create(observation)).build());
			responseObserver.onCompleted();

		}

		@Override
		public void deliverObservationBatch(final ObservationBatch observationBatch, final StreamObserver<GenericResponse> responseObserver) {
			dao.createBatchObservations(observationBatch.getObservationList());
			responseObserver.onValue(GenericResponse.newBuilder().setSuccess(true).build());
			responseObserver.onCompleted();
		}
	}

	public GenericStashDAO getDao() {
		return dao;
	}

	@Required
	public void setDao(final GenericStashDAO dao) {
		this.dao = dao;
	}
}
