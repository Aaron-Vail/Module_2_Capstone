package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Camp;
import com.techelevator.model.CampDao;
import com.techelevator.model.Park;


public class CampJDBCDAO implements CampDao {
	
private JdbcTemplate jdbcTemplate;
	
	public CampJDBCDAO (DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Camp> getAllCamps() {
		ArrayList<Camp> campList = new ArrayList<>();
		String sqlGetAllCamps = "SELECT * FROM campground";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCamps);
		while(results.next()) {
			Camp theCamp = mapRowToCamp(results);
			campList.add(theCamp);
		}
		return campList;	
	}

	@Override
	public List<Camp> searchCampByName(String nameSearch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Camp getCampById(Long campId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Camp mapRowToCamp(SqlRowSet results) {
		Camp theCamp;
		theCamp = new Camp();
		theCamp.setCampId(results.getLong("campground_id"));
		theCamp.setCampId(results.getLong("park_id"));
		theCamp.setCampName(results.getString("name"));
		theCamp.setOpenMonth(results.getLong("open_from_mm"));
		theCamp.setCloseMonth(results.getLong("open_to_mm"));
		theCamp.setDailyFee(results.getBigDecimal("daily_fee"));
		
		return theCamp;
	}

}
