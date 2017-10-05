package com.techelevator.model;

import java.util.List;

public interface ParkDao {

	public List<Park> getAllParks();
	public List<Park> searchParkByName(String nameSearch);
	public Park getParkById(Long parkId); 
}
