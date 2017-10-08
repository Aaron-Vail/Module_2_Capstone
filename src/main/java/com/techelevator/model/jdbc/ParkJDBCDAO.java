package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Camp;
import com.techelevator.model.Park;
import com.techelevator.model.ParkDao;


public class ParkJDBCDAO implements ParkDao {

	private JdbcTemplate jdbcTemplate;
	
	public ParkJDBCDAO (DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}


	public List<Park> getAllParks() {
		ArrayList<Park> parkList = new ArrayList<>();
		String sqlGetAllParks = "SELECT * FROM park";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllParks);
		while(results.next()) {
			Park thePark = mapRowToPark(results);
			parkList.add(thePark);
		}
		return parkList;
	}
		
	public List<Park> searchParkByName(String nameSearch) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Park getParkById(Long parkId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	private Park mapRowToPark(SqlRowSet results) {
		Park thePark;
		thePark = new Park();
		thePark.setParkId(results.getLong("park_id"));
		thePark.setParkName(results.getString("name"));
		thePark.setLocation(results.getString("location"));
		thePark.setDateEstablished(results.getDate("establish_date").toLocalDate());
		thePark.setArea(results.getInt("area"));
		thePark.setVisitors(results.getInt("visitors"));
		thePark.setDescription(results.getString("description"));
		
		return thePark;
	}
	
//	public void getExactPark () {
//		String sqlGetExactPark = "SELECT * FROM park";
//		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetExactPark);
//		while(results.next()) {
//			Park thePark = mapRowToPark(results);
//		}
//	}
	
	


}
