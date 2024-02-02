package com.hivestreaming.assignment.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.hivestreaming.assignment.pojos.Usage;

@Component("SQLDataService")
public class SQLDataService implements DataService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${com.hivestreaming.assignment.poolsize}")
	private int threadPoolSize;

	private Set<String> clients = new HashSet<>();

	private Logger logger = LoggerFactory.getLogger(SQLDataService.class);

	@Override
	public void putUsage(Usage usage) {
		String clientId = usage.getCliendId();
		if (!this.clients.contains(clientId)) {
			this.createUsageTable(clientId);
			this.clients.add(clientId);
		}

		this.jdbcTemplate
				.execute(String.format(Queries.INSERT_USAGE, clientId, usage.getDate().getTime(), usage.getPercent()));
	}

	@Override
	public List<Map<String, List<Usage>>> getUsage(Date from, Date to, int threshold) {
		// Add table names in the clients set
		this.clients.addAll(this.getAllTableNames());

		// Retreive usage from all tables concurrently
		Map<String, Future<List<Usage>>> futureMap = new HashMap<>();
		ExecutorService service = Executors.newFixedThreadPool(this.threadPoolSize);
		for (String clientId : this.clients) {
			futureMap.put(clientId, service.submit(new UsageRetreiver(new Usage(clientId, from, to, threshold))));
		}

		// Initialize the final list
		List<Map<String, List<Usage>>> finalList = new LinkedList<>();

		// Accumulate the maps in the list
		Iterator<Map.Entry<String, Future<List<Usage>>>> iterator = futureMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Future<List<Usage>>> entry = iterator.next();
			Map<String, List<Usage>> map = new HashMap<>();
			try {
				map.put(entry.getKey(), entry.getValue().get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			finalList.add(map);
		}

		// Shut down the executor service
		service.shutdown();

		// Return the final list
		return finalList;
	}

	private List<String> getAllTableNames() {
		return this.jdbcTemplate.query(Queries.ALL_TABLE_NAMES, (rs, rn) -> rs.getString(1));
	}

	private void createUsageTable(String clientId) {
		this.jdbcTemplate.execute(String.format(Queries.CREATE_TABLE, clientId));
	}

	private List<Usage> getUsages(Usage usage) {
		return this.jdbcTemplate.query(
				String.format(Queries.SELECT_USAGE, usage.getCliendId(), usage.getFromDate().getTime(),
						usage.getDate().getTime(), usage.getPercent()),
				(rs, rn) -> new Usage(rs.getLong("timestamp"), rs.getInt("percent")));
	}

	private class UsageRetreiver implements Callable<List<Usage>> {

		private Usage usage;

		public UsageRetreiver(Usage usage) {
			this.usage = usage;
		}

		@Override
		public List<Usage> call() throws Exception {
			logger.info("Getting usages for: " + this.usage.getCliendId());

			return SQLDataService.this.getUsages(this.usage);
		}

	}

}
