package com.hivestreaming.assignment.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.hivestreaming.assignment.dao.DataService;
import com.hivestreaming.assignment.pojos.Usage;

@Component
public class Consumer {

	private Logger logger = LoggerFactory.getLogger(Consumer.class);

	@Autowired
	@Qualifier("SQLDataService")
	private DataService dataService;

	@JmsListener(destination = "${com.hivestreaming.assignment.queue}", containerFactory = "defaultFactory")
	public void receiveMessage(byte[] bytes) {
		Usage usage = new Usage(bytes);

		logger.info("Usage Received: " + usage);

		thread(new Runnable() {
			@Override
			public void run() {
				Consumer.this.dataService.putUsage(usage);
			}
		}, false);
	}

	public static void thread(Runnable runnable, boolean daemon) {
		Thread thread = new Thread(runnable);
		thread.setDaemon(daemon);
		thread.start();
	}

}
