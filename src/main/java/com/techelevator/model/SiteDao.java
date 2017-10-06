package com.techelevator.model;

import java.util.List;

public interface SiteDao {
	
	public List<Site> getAllSites();
	public List<Site> searchSiteByName(String nameSearch);
	public Site getSiteById(Long siteId); 
}
