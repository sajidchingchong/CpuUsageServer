package com.hivestreaming.assignment.pojos;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Usage {

	@JsonIgnore
	private String cliendId;

	@JsonIgnore
	private Date fromDate;

	private Date date;

	private int percent;

	public Usage() {

	}

	public Usage(String cliendId, Date fromDate, Date date, int percent) {
		this.cliendId = cliendId;
		this.fromDate = fromDate;
		this.date = date;
		this.percent = percent;
	}

	public Usage(long timeStamp, int percent) {
		this(null, null, new Date(timeStamp), percent);
	}

	public Usage(byte[] bytes) {
		this(new String(bytes, 0, 8).trim(), null, new Date(new BigInteger(bytes, 8, 8).longValue()),
				new BigInteger(bytes, 16, 4).intValue());
	}

	public String getCliendId() {
		return cliendId;
	}

	public void setCliendId(String cliendId) {
		this.cliendId = cliendId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	@Override
	public String toString() {
		return "Usage [cliendId=" + cliendId + ", fromDate=" + fromDate + ", date=" + date + ", percent=" + percent
				+ "]";
	}

}
