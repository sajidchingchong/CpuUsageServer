package com.hivestreaming.assignment.webservice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hivestreaming.assignment.dao.DataService;
import com.hivestreaming.assignment.pojos.MyError;
import com.hivestreaming.assignment.pojos.Usage;

@RestController
public class MyRestController {

	@Autowired
	@Qualifier("SQLDataService")
	private DataService dataService;

	private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@GetMapping("/ping")
	public String ping() {
		return "ping";
	}

	@GetMapping("/usage")
	public List<Map<String, List<Usage>>> tax(@RequestParam String from, @RequestParam String to,
			@RequestParam int threshold) throws ParseException {
		Date fromDate = new Date();
		if (from != null) {
			fromDate = format.parse(from);
		}

		Date toDate = new Date();
		if (to != null) {
			toDate = format.parse(to);
		}

		return dataService.getUsage(fromDate, toDate, threshold);
	}

	@ExceptionHandler({ Exception.class })
	public MyError exception(Exception e) {
		return new MyError(e.getLocalizedMessage());
	}

}
