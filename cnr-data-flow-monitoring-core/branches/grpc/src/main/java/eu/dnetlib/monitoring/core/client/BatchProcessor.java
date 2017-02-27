package eu.dnetlib.monitoring.core.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Preconditions;

import eu.dnetlib.monitoring.rmi.Observation;
import eu.dnetlib.monitoring.rmi.ObservationBatch;

public class BatchProcessor {

	private static final Log log = LogFactory.getLog(BatchProcessor.class);

	private DataFlowMonitoringGrpcClient client;
	private ObservationBatch.Builder batch;
	protected BlockingQueue<Observation> queue = new LinkedBlockingQueue<Observation>();
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	private final int batchSize;
	private final TimeUnit flushIntervalUnit;
	private final int flushInterval;

	private BatchProcessor(final DataFlowMonitoringGrpcClient client, final int batchSize, final int flushInterval, final TimeUnit flushIntervalUnit) {
		super();
		this.client = client;
		this.batchSize = batchSize;
		this.flushIntervalUnit = flushIntervalUnit;
		this.flushInterval = flushInterval;

		// Flush at specified Rate
		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				flush();
			}
		}, this.flushInterval, this.flushInterval, this.flushIntervalUnit);
	}

	public static Builder builder(final DataFlowMonitoringGrpcClient client) {
		return new Builder(client);
	}

	/**
	 * The Builder to create a BatchProcessor instance.
	 */
	public static final class Builder {

		private final DataFlowMonitoringGrpcClient client;
		private int batchSize;
		private TimeUnit flushIntervalUnit;
		private int flushInterval;

		public Builder(final DataFlowMonitoringGrpcClient client) {
			this.client = client;
		}

		/**
		 * The number of observations after which a batch-write must be performed.
		 *
		 * @param batchsize
		 *            number of {@link Observation} written after which a write must happen.
		 * @return this Builder to use it fluent
		 */
		public Builder batchSize(final int batchsize) {
			this.batchSize = batchsize;
			return this;
		}

		/**
		 * The interval at which at least should issued a write.
		 *
		 * @param interval
		 *            the interval
		 * @param unit
		 *            the TimeUnit of the interval
		 *
		 * @return this Builder to use it fluent
		 */
		public Builder interval(final int interval, final TimeUnit unit) {
			flushInterval = interval;
			flushIntervalUnit = unit;
			return this;
		}

		/**
		 * Create the BatchProcessor.
		 *
		 * @return the BatchProcessor instance.
		 */
		public BatchProcessor build() {
			Preconditions.checkNotNull(batchSize, "batchSize may not be null");
			Preconditions.checkNotNull(flushInterval, "flushInterval may not be null");
			Preconditions.checkNotNull(flushIntervalUnit, "flushIntervalUnit may not be null");
			return new BatchProcessor(client, batchSize, flushInterval, flushIntervalUnit);
		}
	}

	protected void flush() {
		if (queue.isEmpty()) {
			return;
		} else {
			log.debug("flushing batch");
			batch = ObservationBatch.newBuilder();
			List<Observation> batchEntries = new ArrayList<Observation>();
			queue.drainTo(batchEntries);
			client.getBlockingStub().deliverObservationBatch(batch.addAllObservation(batchEntries).build());
		}
	}

	/**
	 * Adds a single observation to the batch
	 *
	 * @param observation
	 *            the observation to add
	 */
	public void put(final Observation observation) {
		if (observation != null) {
			queue.add(observation);
			log.debug("adding to batch");
			if (queue.size() >= batchSize) {
				flush();
			}
		}
	}
}
