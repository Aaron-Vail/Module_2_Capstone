package com.techelevator.model;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.time.LocalDate;

public class Camp {
	
	private Long campId;
	private Long parkId;
	private String campName;
	private Long openMonth;
	private Long closeMonth;
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
	public Long getOpenMonth() {
		return openMonth;
	}
	public void setOpenMonth(Long openMonth) {
		this.openMonth = openMonth;
	}
	public Long getCloseMonth() {
		return closeMonth;
	}
	public void setCloseMonth(Long closeMonth) {
		this.closeMonth = closeMonth;
	}
	public BigDecimal getDailyFee() {
		return dailyFee;
	}
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	} 
	public String toString() {
		String campStuff = campName +"      "+ showMonth(openMonth) +"      "+ showMonth(closeMonth) +"      $"+ dailyFee.setScale(2);
		
		
		return campStuff;
	}
	private String showMonth(Long numberOfMonth) {
		return new DateFormatSymbols().getMonths()[(int) (numberOfMonth-1)];
	}
}

	
	