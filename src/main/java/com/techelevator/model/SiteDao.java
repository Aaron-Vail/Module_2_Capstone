package com.techelevator.model;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface SiteDao {
	
	public List<Site> getAllSites();
	public Site mapRowToSite(SqlRowSet results);

}
