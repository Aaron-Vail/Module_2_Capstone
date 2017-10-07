package com.techelevator;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Camp;
import com.techelevator.model.CampDao;
import com.techelevator.model.Park;
import com.techelevator.model.ParkDao;
import com.techelevator.model.ReservationDao;
import com.techelevator.model.Site;
import com.techelevator.model.SiteDao;
import com.techelevator.model.jdbc.CampJDBCDAO;
import com.techelevator.model.jdbc.ParkJDBCDAO;
import com.techelevator.model.jdbc.ReservationJDBCDAO;
import com.techelevator.model.jdbc.SiteJDBCDAO;
import com.techelevator.view.Menu;

public class CampgroundCLI {
	
	private static final String MAIN_MENU_OPTION_DISPLAY_PARKS_DETAILS = "Select a Park to View Park Details";
	private static final String MAIN_MENU_OPTION_QUIT = "Quit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_PARKS_DETAILS, 
														MAIN_MENU_OPTION_QUIT
														};
	
	private static final String SELECT_A_COMMAND_VIEW_CAMPS = "View Campgrounds";
	private static final String SELECT_A_COMMAND_SEARCH_RESERVATION = "Search Reservation";
	private static final String SELECT_A_COMMAND_RETURN_PREVIOUS_MENU = "Return to previous menu";
	private static final String[] SELECT_A_COMMAND_OPTIONS = { SELECT_A_COMMAND_VIEW_CAMPS, 
														SELECT_A_COMMAND_SEARCH_RESERVATION, 
														SELECT_A_COMMAND_RETURN_PREVIOUS_MENU
														};

	private static final String RESERVATION_MENU_SEARCH = "Search for available reservation";
	private static final String RESERVATION_MENU_RETURN_TO_PREVIOUS = "Return to previous screen";
	private static final String[] RESERVATION_MENU_OPTIONS = { RESERVATION_MENU_SEARCH, 
														RESERVATION_MENU_RETURN_TO_PREVIOUS
														};
	
	private static final String SEARCH_CRITERIA_CAMPGROUND = "Which campground? (enter 0 to cancel)";
	private static final String SEARCH_CRITERIA_ARRIVAL = "What is the arrival date? (yyyy-mm-dd) >>>";
	private static final String SEARCH_CRITERIA_DEPARTURE = "What is the departure date? (yyyy-mm-dd) >>>";
	private static final String[] SEARCH_CRITERIA_OPTIONS = { SEARCH_CRITERIA_CAMPGROUND, 
														SEARCH_CRITERIA_ARRIVAL, 
														SEARCH_CRITERIA_DEPARTURE
														};
			
	private static final String RESERVATION_DETAILS_SITE_NUMBER = "Which site should be reserved? (enter 0 to cancel)";
	private static final String RESERVATION_DETAILS_ENTER_NAME = "What name should the reservation be made under?";
	private static final String[] RESERVATION_DETAILS_OPTIONS = { RESERVATION_DETAILS_SITE_NUMBER,
														RESERVATION_DETAILS_ENTER_NAME
														};
	
	
	private Menu menu;
	private ParkDao parkDao;
	private CampDao campDao;
	private SiteDao siteDao;
	private ReservationDao reservationDao;
	private JdbcTemplate jdbcTemplate;
	

	public static void main(String[] args) {
		BasicDataSource datasource = new BasicDataSource();
		datasource.setUrl("jdbc:postgresql://localhost:5432/campground");
		datasource.setUsername("postgres");
		datasource.setPassword("postgres1");
		
		Menu menu = new Menu(System.in, System.out);
		CampgroundCLI application = new CampgroundCLI(datasource, menu); 
		application.run();
		
	}

	public CampgroundCLI(DataSource datasource, Menu menu) {
		this.menu = menu;
		this.jdbcTemplate = new JdbcTemplate(datasource);
		// create your DAOs here
		parkDao = new ParkJDBCDAO(datasource);
		campDao = new CampJDBCDAO(datasource);
		siteDao = new SiteJDBCDAO(datasource);

		reservationDao = new ReservationJDBCDAO(datasource);
	}
	
	public void run() {
		
		boolean mainMenu = true;
		while (mainMenu) {
			displayApplicationBanner();
			//add quit			
			Park choice = (Park) menu.getChoiceFromOptions(parkDao.getAllParks().toArray());
			displayParkDetails(choice);
				}
			}
	

	private void displayParkDetails(Park choice) {
		for (int i = 0; i < parkDao.getAllParks().size(); i++){
			Long tempInt = parkDao.getAllParks().get(i).getParkId();
			if (tempInt.equals(choice.getParkId())) {
				System.out.println(
						"\nPark Name: " + choice.getParkName() +
						"\nPark I.D: " + choice.getParkId() + 
						"\nLocation: " + choice.getLocation() +
						"\nEstablished: " + choice.getDateEstablished() +
						"\nArea: " + choice.getArea() +
						"\nVisitors: " + choice.getVisitors() +
						"\n\nDescription: " + choice.getDescription()
						);
				handleCampChoices(choice);
				
			}
		}	
	}

	private void handleCampChoices(Park choice) {
		boolean subMenu1 = true;
		while (subMenu1) {
			String choice2 = (String) menu.getChoiceFromOptions(SELECT_A_COMMAND_OPTIONS);
			if (choice2.equals(SELECT_A_COMMAND_VIEW_CAMPS)) {
				Long longPark = choice.getParkId();
				printCampgrounds(getAllCampsByPark(longPark));
			} else if (choice2.equals(SELECT_A_COMMAND_SEARCH_RESERVATION)) {
				handleReservation(choice);
			} else {
				run();
			}
		}
	}

	private void handleReservation(Park choice) {
		System.out.println("Search for Camp Reservation:"
				+ "\n Name     Open       Close       Daily Fee");
		Camp choice3 = (Camp) menu.getChoiceFromOptions(campDao.getAllCamps().toArray());
		handleCampgroundChoice(choice, choice3);
		
		
	}

	@SuppressWarnings("resource")
	private void handleCampgroundChoice(Park choice, Camp choice3) {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
		
		//what is the arrival date
//		try{
		Scanner input = new Scanner(System.in);
		System.out.println(SEARCH_CRITERIA_ARRIVAL);
		LocalDate startReservationChoice = LocalDate.parse(input.nextLine(), formatter);
		System.out.println(SEARCH_CRITERIA_DEPARTURE);
		LocalDate endReservationChoice = LocalDate.parse(input.nextLine(), formatter);
		
		int totalDays = getTotalDays(startReservationChoice, endReservationChoice);
//		} catch (DateTimeException e){
//			
//		}
		printSites(getValidReservationsForSites(choice3, startReservationChoice, endReservationChoice), choice3, totalDays);
		handleMakeReservation(choice, choice3, startReservationChoice, endReservationChoice);

	}

	@SuppressWarnings("resource")
	private void handleMakeReservation(Park choice, Camp choice3, LocalDate start, LocalDate end) {
		Scanner input = new Scanner(System.in);
		System.out.println(RESERVATION_DETAILS_SITE_NUMBER);
		Long requestedSiteNumber = input.nextLong();
		System.out.println(RESERVATION_DETAILS_ENTER_NAME);
		String requester = input.next();
		Long result = makeReservation(requestedSiteNumber, requester, choice, choice3, start, end);
		System.out.print(result);
	}

	private long makeReservation(Long siteNumber, String requester, Park choice, Camp choice3, LocalDate start, LocalDate end) {
		String sqlGetSiteId = "SELECT site_id FROM site s JOIN campground c ON s.campground_id = c.campground_id "
				+ "JOIN park p ON c.park_id = p.park_id WHERE p.park_id=? AND c.campground_id=? AND s.site_number=?";
		SqlRowSet result1 = jdbcTemplate.queryForRowSet(sqlGetSiteId, choice.getParkId(), choice3.getCampId(), siteNumber);
		Long siteId = result1.getLong("site_id");
		
		
		String sqlMakeReservation = "INSERT INTO reservation SET (site_id, name, from_date, to_date, create_date "
				+ "RETURNING reservation_id";
		Long result2 = jdbcTemplate.queryForObject(sqlMakeReservation, long.class, siteId, requester, start, end, LocalDate.now());
		return result2;
	}

	private int getTotalDays(LocalDate start, LocalDate end) {
		int totalDays = end.compareTo(start);
		return totalDays;
	}

	private void printSites(List<Site> sitesToBeDisplayed, Camp choice3, int totalDays) {
		System.out.println();
		if(sitesToBeDisplayed.size() > 0) {
			for(Site site : sitesToBeDisplayed) {
				System.out.println(
						"\nSite Number: " + site.getSiteNumber() +
						"\nMax Occupancy: " + site.getMaxOccupancy() + 
						"\nAccessible?: " + site.isAccessible() +
						"\nMax RV Length: " + site.getMaxRVLength() +
						"\nUtility: " + site.isUtilities() +
						"\nCost: " + "$" + choice3.getDailyFee().setScale(2).multiply(new BigDecimal(totalDays))
						);
			}
		} else {
			System.out.println("\n*** No results ***");
		}				
	}

	private List<Site> getValidReservationsForSites(Camp choice3, LocalDate start, LocalDate end) {
		
		Long campID = choice3.getCampId();
		ArrayList<Site> siteList = new ArrayList<>();
		String sqlGetAllSites = 
				"SELECT * FROM site s WHERE campground_id = ? AND s.site_id NOT IN "
				+ "(SELECT s.site_id FROM site s JOIN reservation r ON s.site_id = r.site_id WHERE campground_id =? "
				+ "AND (to_date BETWEEN ? AND ? OR from_date BETWEEN ? AND ? OR (from_date <= ? AND to_date >= ?))) LIMIT 5";
	
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllSites, campID, campID, start, end, start, end, start, end);
		while(results.next()) {
			Site theSite = mapRowToSite(results);
			siteList.add(theSite);
		}
		return siteList;
	}

	private void printCampgrounds(List<Camp> campsToBeDisplayed) {
		System.out.println();
		if(campsToBeDisplayed.size() > 0) {
			for(Camp camp : campsToBeDisplayed) {
				System.out.println(
						"\nCamp Name: " + camp.getCampName() +
						"\nOpen: " + showMonth(camp.getOpenMonth()) + 
						"\nClose: " + showMonth(camp.getCloseMonth()) +
						"\nDaily Fee: " + "$" + camp.getDailyFee() 
						);
			}
		} else {
			System.out.println("\n*** No results ***");
		}		
	}

	private String showMonth(Long numberOfMonth) {
		return new DateFormatSymbols().getMonths()[(int) (numberOfMonth-1)];
	}

	private void displayApplicationBanner() {
		System.out.print("\n" + MAIN_MENU_OPTION_DISPLAY_PARKS_DETAILS + ": \n");		
	}
	
	public List<Camp> getAllCampsByPark(Long longPark) {
		ArrayList<Camp> campList = new ArrayList<>();
		String sqlGetAllCamps = "SELECT * FROM campground WHERE park_id =?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCamps, longPark);
		while(results.next()) {
			Camp theCamp = mapRowToCamp(results);
			campList.add(theCamp);
		}
		return campList;	
	}
	
	public Camp mapRowToCamp(SqlRowSet results) {
		Camp theCamp;
		theCamp = new Camp();
		theCamp.setCampId(results.getLong("campground_id"));
		theCamp.setCampId(results.getLong("park_id"));
		theCamp.setCampName(results.getString("name"));
		theCamp.setOpenMonth(results.getLong("open_from_mm"));
		theCamp.setCloseMonth(results.getLong("open_to_mm"));
		theCamp.setDailyFee(results.getBigDecimal("daily_fee").setScale(2));
		
		return theCamp;
	}
	
	public Site mapRowToSite(SqlRowSet results) {
		Site theSite;
		theSite = new Site();
		theSite.setSiteId(results.getLong("site_id"));
		theSite.setCampId(results.getLong("campground_id"));
		theSite.setSiteNumber(results.getLong("site_number"));
		theSite.setMaxOccupancy(results.getLong("max_occupancy"));
		theSite.setAccessible(results.getBoolean("accessible"));
		theSite.setMaxRVLength(results.getLong("max_rv_length"));
		
		return theSite;
	}
}
