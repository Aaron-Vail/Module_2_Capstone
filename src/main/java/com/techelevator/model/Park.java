package com.techelevator.model;

import java.time.LocalDate;

public class Park {

	private Long parkId;
	private String parkName;
	private String location;
	private LocalDate dateEstablished;
	private int area;
	private int visitors;
	private String description;
	
	//getters and setters
	public Long getParkId() {
		return parkId;
	}
	public void setParkId(Long parkId) {
		this.parkId = parkId;
	}
	public String getParkName() {
		return parkName;
	}
	public void setParkName(String parkeName) {
		this.parkName = parkeName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public LocalDate getDateEstablished() {
		return dateEstablished;
	}
	public void setDateEstablished(LocalDate dateEstablished) {
		this.dateEstablished = dateEstablished;
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	public int getVisitors() {
		return visitors;
	}
	public void setVisitors(int visitors) {
		this.visitors = visitors;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String toString() {
		return parkName;
	}
}
