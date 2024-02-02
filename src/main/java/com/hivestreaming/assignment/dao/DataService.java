package com.hivestreaming.assignment.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hivestreaming.assignment.pojos.Usage;

public interface DataService {

	public void putUsage(Usage usage);
	
	public List<Map<String, List<Usage>>> getUsage(Date from, Date to, int threshold);
	
}
