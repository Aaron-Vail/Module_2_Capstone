package com.techelevator.model;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface ParkDao {

	public List<Park> getAllParks();
	public Park mapRowToPark(SqlRowSet results);
}
