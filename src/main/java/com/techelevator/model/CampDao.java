package com.techelevator.model;

import java.util.List;

public interface CampDao {
	
	public List<Camp> getAllCamps();
	public List<Camp> searchCampByName(String nameSearch);
	public Camp getCampById(Long campId); 

}
