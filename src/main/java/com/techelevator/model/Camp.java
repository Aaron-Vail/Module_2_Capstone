package com.techelevator.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Camp {
	
	private Long campId;
	private Long parkId;
	private String campName;
	private LocalDate openMonth;
	private LocalDate closeMonth;
	private BigDecimal dailyFee;
	
	public Long getCampId() {
		return campId;
	}
	public void setCampId(Long campId) {
		this.campId = campId;
	}
	public Long getParkId() {
		return parkId;
	}
	public void setParkId(Long parkId) {
		this.parkId = parkId;
	}
	public String getCampName() {
		return campName;
	}
	public void setCampName(String campName) {
		this.campName = campName;
	}
	public LocalDate getOpenMonth() {
		return openMonth;
	}
	public void setOpenMonth(LocalDate openMonth) {
		this.openMonth = openMonth;
	}
	public LocalDate getCloseMonth() {
		return closeMonth;
	}
	public void setCloseMonth(LocalDate closeMonth) {
		this.closeMonth = closeMonth;
	}
	public BigDecimal getDailyFee() {
		return dailyFee;
	}
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	} 
}

	
	