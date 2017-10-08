package com.techelevator.model;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface CampDao {
	
	public List<Camp> getAllCamps();
	public Camp mapRowToCamp(SqlRowSet results);
}
