package com.challenge.Survey;

import org.testng.annotations.Test;

public class ChallengeTest {

	/**
	 * Re-calculates statistics for last 60 seconds based on new received
	 * transaction data. Transaction data summary (sum, max, min, count) is kept
	 * inside map, for each second of last minute new summary entry is created
	 * or existing one is updated with latest statistics for that second, if
	 * previously inserted entry is outdated then it will be overwritten by new
	 * one(s) (if no new transaction entry is received for that second then
	 * it'll be simply ignored during statistics retrieval).
	 *
	 * Application holds constant memory storage (at max 60 entries) about
	 * statistics of last minute, which means memory complexity is O(1)
	 *
	 * @param transaction
	 *            new transaction data
	 */
	@Override
	public void computeStatistics(Transaction transaction) {
		logger.info(
				"Computing statistics based on new received transaction => {}",
				transaction);

		if ((System.currentTimeMillis() - transaction.getTimestamp()) / 1000 < SECONDS_STAT) {
			int second = LocalDateTime.ofInstant(
					Instant.ofEpochMilli(transaction.getTimestamp()),
					ZoneId.systemDefault()).getSecond();

			statisticsForLastMin
					.compute(
							second,
							(k, v) -> {
								if (v == null
										|| (System.currentTimeMillis() - v
												.getTimestamp()) / 1000 >= SECONDS_STAT) {
									v = new Statistics();
									v.setTimestamp(transaction.getTimestamp());
									v.setSum(transaction.getAmount());
									v.setMax(transaction.getAmount());
									v.setMin(transaction.getAmount());
									v.setCount(1l);
									return v;
								}

								v.setCount(v.getCount() + 1);
								v.setSum(v.getSum() + transaction.getAmount());
								if (Double.compare(transaction.getAmount(),
										v.getMax()) > 0)
									v.setMax(transaction.getAmount());
								if (Double.compare(transaction.getAmount(),
										v.getMin()) < 0)
									v.setMin(transaction.getAmount());
								return v;
							});
		}
	}

	/**
	 * Calculates and returns combined statistics summary based on statistics
	 * map (statisticsForLastMin). During calculation outdated statistics are
	 * ignored.
	 *
	 * Calculation is made in constant time by only combining already calculated
	 * statistics, which means method runs with O(1) complexity
	 *
	 * @return combined statistics summary
	 */
	public ComputedStatistics getStatistics() {
		DoubleSummaryStatistics summary = oneMinuteStatistics
				.values()
				.parallelStream()
				.filter(s -> (System.currentTimeMillis() - s.getTimestamp()) / 1000 < DURATION_STATS)
				.collect(Collectors.summarizingDouble(Statistics::getSum));
	}

	private static final Map<Integer, Statistics> statisticsForLastMin = new ConcurrentHashMap<>(
			SECONDS_STAT);

}
