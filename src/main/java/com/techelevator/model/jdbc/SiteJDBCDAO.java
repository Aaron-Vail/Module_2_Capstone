package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Site;
import com.techelevator.model.SiteDao;



public class SiteJDBCDAO implements SiteDao {
	
private JdbcTemplate jdbcTemplate;
	
	public SiteJDBCDAO (DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Site> getAllSites() {
		ArrayList<Site> siteList = new ArrayList<>();
		String sqlGetAllSites = "SELECT * FROM campground";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllSites);
		while(results.next()) {
			Site theCamp = mapRowToSite(results);
			siteList.add(theCamp);
		}
		return siteList;	
	}

	
	
	public Site mapRowToSite(SqlRowSet results) {
		Site theSite;
		theSite = new Site();
		theSite.setSiteId(results.getLong("site_id"));
		theSite.setCampId(results.getLong("camp_id"));
		theSite.setSiteNumber(results.getLong("site_number"));
		theSite.setMaxOccupancy(results.getLong("max_occupancy"));
		theSite.setAccessible(results.getBoolean("accessible"));
		theSite.setMaxRVLength(results.getLong("max_rv_length"));
		
		return theSite;
	}

}
