package com.techelevator;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Camp;
import com.techelevator.model.CampDao;
import com.techelevator.model.Park;
import com.techelevator.model.ParkDao;

import com.techelevator.model.Site;

import com.techelevator.model.jdbc.CampJDBCDAO;
import com.techelevator.model.jdbc.ParkJDBCDAO;

import com.techelevator.view.Menu;

public class CampgroundCLI {
//***CONSTANTS***
	private static final String MAIN_MENU_OPTION_DISPLAY_PARKS_DETAILS = "Select a Park to View Park Details";
	private static final String SELECT_A_COMMAND_VIEW_CAMPS = "View Campgrounds";
	private static final String SELECT_A_COMMAND_SEARCH_RESERVATION = "Search for Reservation";
	private static final String SELECT_A_COMMAND_RETURN_PREVIOUS_MENU = "Return to previous menu";
	private static final String[] SELECT_CAMP_OPTIONS = { SELECT_A_COMMAND_VIEW_CAMPS, 
														SELECT_A_COMMAND_SEARCH_RESERVATION, 
														SELECT_A_COMMAND_RETURN_PREVIOUS_MENU
														};
	private static final String SEARCH_CRITERIA_ARRIVAL = "What is the arrival date? (yyyy-mm-dd) >>>";
	private static final String SEARCH_CRITERIA_DEPARTURE = "What is the departure date? (yyyy-mm-dd) >>>";
	private static final String RESERVATION_DETAILS_SITE_NUMBER = "Which site should be reserved? (enter 0 to cancel)";
	private static final String RESERVATION_DETAILS_ENTER_NAME = "What name should the reservation be made under?";
//***VARIABLES***
	private Menu menu;
	private ParkDao parkDao;
	private CampDao campDao;
	private JdbcTemplate jdbcTemplate;
//***MAIN METHOD***
	public static void main(String[] args) {
		BasicDataSource datasource = new BasicDataSource();
		datasource.setUrl("jdbc:postgresql://localhost:5432/campground");
		datasource.setUsername("postgres");
		datasource.setPassword("postgres1");
		
		Menu menu = new Menu(System.in, System.out);
		CampgroundCLI application = new CampgroundCLI(datasource, menu); 
		application.run();
		
	}
//***CLI CONSTRUCTOR***
	public CampgroundCLI(DataSource datasource, Menu menu) {
		this.menu = menu;
		this.jdbcTemplate = new JdbcTemplate(datasource);
		parkDao = new ParkJDBCDAO(datasource);
		campDao = new CampJDBCDAO(datasource);

	}
//***RUN***	
	public void run() {
//displays application banner (method at the end)
			displayApplicationBanner();	
//Consolidates park avail. park options with a quit option
			Park [] parkOptionsPlusQuit = new Park[parkDao.getAllParks().toArray().length+1];
			Park quit = new Park();
			quit.setParkName("Quit");
			for (int i = 0; i<parkDao.getAllParks().toArray().length; i++) {
				parkOptionsPlusQuit[i] = (Park) parkDao.getAllParks().toArray()[i];
			}
			parkOptionsPlusQuit[parkDao.getAllParks().toArray().length] = quit;
//Receives park choice from user		
			Park choice = (Park) menu.getChoiceFromOptions(parkOptionsPlusQuit);
//Quits program
			if (choice.equals(quit)) {
				System.exit(0);
			}
//Points to Park Details method
			displayParkDetails(choice);
	}
//Based on Park choice, displays details about that park
	private void displayParkDetails(Park choice) {
		for (int i = 0; i < parkDao.getAllParks().size(); i++){
			Long tempInt = parkDao.getAllParks().get(i).getParkId();
			if (tempInt.equals(choice.getParkId())) {
				System.out.println(
						"\n" + choice.getParkName() + " National Park\n"
						+ String.format("%-25s","\nLocation: ") + choice.getLocation() 
						+ String.format("%-25s","\nEstablished: ") + choice.getDateEstablished() 
						+ String.format("%-25s","\nArea in Sq km: ") + NumberFormat.getNumberInstance(Locale.US).format((choice.getArea())) 
						+ String.format("%-25s","\nAnnual Visitors: ") + NumberFormat.getNumberInstance(Locale.US).format((choice.getVisitors())) 
						+ "\n\n" + paragraph(choice.getDescription()) //rebuilds description into paragraph-like form (method at the end)
						);
//Points to Camp Choices method that handles options based on 
				handleCampMenu(choice);
				;
			}
		}	
	}
//1) View camps 2) Search for reservation 3) Return to previous menu	
	private void handleCampMenu(Park choice) {
			String choice2 = (String) menu.getChoiceFromOptions(SELECT_CAMP_OPTIONS);
			if (choice2.equals(SELECT_A_COMMAND_VIEW_CAMPS)) {
//1) Prints all camps within a park (method at end)
				Long longPark = choice.getParkId();
				printCampgrounds(getAllCampsByPark(longPark), choice);
				handleCampMenu(choice);
			} else if (choice2.equals(SELECT_A_COMMAND_SEARCH_RESERVATION)) {
//2) Points to method that handles the camp choice process
				handleCampChoices(choice);
			} else {
//3) Restarts program
				run();
			}
	}
//After displaying camp choices, this method takes the camp choice3
	private void handleCampChoices(Park choice) {
//Prints headers for Camp(s) given the Park choice
		System.out.println("\nSearch for Camp Reservation:"
				+ "\n\n" + choice.getParkName() + " National Park Campgrounds"
				+ "\n\n" + String.format("%-43s", "Name")
				+ String.format("%-10s", "Open")
				+ String.format("%-20s", "Close")
				+ "Daily Fee"
				);
//Sets Camp to choice3 from all avail, within Park choice
		Camp choice3 = (Camp) menu.getChoiceFromOptions(campDao.getAllCamps().toArray());
//Points to method that takes reservation dates 
		handleReservationDates(choice, choice3);
	}
//Prompts user for desired start and end dates for their reservation
	@SuppressWarnings("resource")
	private void handleReservationDates(Park choice, Camp choice3) {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
		LocalDate startReservationChoice = LocalDate.parse("1000-10-10");
		LocalDate endReservationChoice = LocalDate.parse("1000-10-10");
		int totalDays = 0;
//Takes dates		
		try{
		Scanner input = new Scanner(System.in);
		System.out.println(SEARCH_CRITERIA_ARRIVAL);
		startReservationChoice = LocalDate.parse(input.nextLine(), formatter);
		System.out.println(SEARCH_CRITERIA_DEPARTURE);
		endReservationChoice = LocalDate.parse(input.nextLine(), formatter);
//calculates reservation length in days		
		totalDays = getTotalDays(startReservationChoice, endReservationChoice);
//date can't be a single day because camping is an overnight event. Also, can't reserve a starting date after a ending date
		if (totalDays <= 0) {
			System.out.println("Sorry, there appears to be a conflict with your dates, please try again");
			handleReservationDates(choice,choice3);
		}
//Does not allow user to type in any date except in the required ISO format, restarts method
		} catch (DateTimeException e){
			System.out.println("Sorry, please enter a valid date in YYYY-MM-DD format");
			handleReservationDates(choice,choice3);
		}
//Prints sites based on given dates
		printSites(getValidReservationsForSites(choice3, startReservationChoice, endReservationChoice), choice3, totalDays);
//Points to method that makes the reservation
		handleMakeReservation(choice, choice3, startReservationChoice, endReservationChoice);
	}
//Gets available sites given the choice of dates
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

	@SuppressWarnings("resource")
	private void handleMakeReservation(Park choice, Camp choice3, LocalDate start, LocalDate end) {
		int result = 0;
//Takes desired site number		
		Scanner input = new Scanner(System.in);
		System.out.println(RESERVATION_DETAILS_SITE_NUMBER);
		Long requestedSiteNumber = input.nextLong();
		if (requestedSiteNumber == 0) {
			run();
		}
//Takes name for reservation		
		System.out.println(RESERVATION_DETAILS_ENTER_NAME);
		String requester = input.next();
//Makes reservation and sets reservation ID to result if there isn't already a reservation at the given site
//If there is a reservation already made there, it asks the user to try a different date range
		try {				
		result = makeReservation(requestedSiteNumber, requester, choice, choice3, start, end);
		if (result == 0) {
			System.out.println("Sorry, there appears to a reservation on these dates, please try again");
			handleReservationDates(choice,choice3);			
		}	
		} catch (Exception e) {
			System.out.println("Sorry, there appears to be a conflict with your dates, please try again");
			handleReservationDates(choice,choice3);		
		}
//Prints reservation ID to customer and returns to 
		System.out.print("Congratulations! Your reservation has been made!! Here is your reservation!!! --> " + result);
		run();
	}
//Makes reservation in the date base
	private int makeReservation(Long siteNumber, String requester, Park choice, Camp choice3, LocalDate start, LocalDate end) {
//Determines site_id to be used to make reservation given Park choice, Camp choice3, Site number and dates
		String sqlGetSiteId = "SELECT site_id FROM site s JOIN campground c ON s.campground_id = c.campground_id "
				+ "JOIN park p ON c.park_id = p.park_id WHERE p.park_id=? AND c.campground_id=? AND s.site_number=?";
		SqlRowSet result1 = jdbcTemplate.queryForRowSet(sqlGetSiteId, choice.getParkId(), choice3.getCampId(), siteNumber);
		Long siteId = null;
		while (result1.next()){
		siteId = result1.getLong("site_id");
		}
////Checks reservation with determined site_id and given dates. If there's an overlap at that site, it returns a 0
////that is caught in an if statement above that asks the user to try different dates		
//		String sqlCheckReservation = "Select site_id FROM reservation WHERE site_id =? AND to_date BETWEEN ? AND ? OR from_date BETWEEN ? AND ?";
//		SqlRowSet result3 = jdbcTemplate.queryForRowSet(sqlCheckReservation, Integer.class, siteId, start, end, start, end);
//		Long check = null;
//		while(result3.next()) {
//		check = result3.getLong("site_id");	
//			if (check == siteId) {
//				return 0;
//			}
//		}
//Makes reservation with determined site_id	and returns the reservation ID		
		String sqlMakeReservation = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date) VALUES (?,?,?,?,?) "
				+ "RETURNING reservation_id";
		int result2 =  jdbcTemplate.queryForObject(sqlMakeReservation, Integer.class, siteId, requester, start, end, LocalDate.now());
		int newReservationId = result2;
		return newReservationId;
	}
//***PRINTERS***
//Displays application banner
	private void displayApplicationBanner() {
		System.out.print("\n" + MAIN_MENU_OPTION_DISPLAY_PARKS_DETAILS + ": \n");		
	}	
//Prints a list of Camp(s)
	private void printCampgrounds(List<Camp> campsToBeDisplayed, Park choice) {
		System.out.println();
		if(campsToBeDisplayed.size() > 0) {
			System.out.println(
						choice.getParkName() + " Park Campgrounds\n\n"
						+ String.format("%-35s", "Name") 
						+ String.format("%-10s", "Open")
						+ String.format("%-15s","Close")
						+ "Daily Fee\n");
			for(Camp camp : campsToBeDisplayed) {
				System.out.println(
						String.format("%-35s", camp.getCampName()) 
						+ String.format("%-10s", showMonth(camp.getOpenMonth())) 
						+ String.format("%-15s", showMonth(camp.getCloseMonth()))
						+ "$" + camp.getDailyFee() 
						);
			}
		} else {
			System.out.println("\n*** No results ***");
		}		
	}
//Prints a list of Site(s)	
	private void printSites(List<Site> sitesToBeDisplayed, Camp choice3, int totalDays) {
		System.out.println();
		if(sitesToBeDisplayed.size() > 0) {
			System.out.print(
					"\n"
					+ String.format("%-15s", "Site No.")
					+ String.format("%-15s", "Max Occup.")
					+ String.format("%-15s", "Accessible?")
					+ String.format("%-15s", "Max RV Length")
					+ String.format("%-15s", "Utility")
					+ String.format("%-15s", "Cost") + "\n"
					);
			for(Site site : sitesToBeDisplayed) {
				System.out.println(
						String.format("%-15s", site.getSiteNumber())
						+ String.format("%-15s", site.getMaxOccupancy()) 
						+ String.format("%-15s", (site.isAccessible() == true) ? "Yes" : "No")
						+ String.format("%-15s", (site.getMaxRVLength() == 0) ? "N/A" : site.getMaxRVLength())
						+ String.format("%-15s", (site.isUtilities()== true) ? "Yes" : "N/A")
						+ "$" + choice3.getDailyFee().setScale(2).multiply(new BigDecimal(totalDays))
						);
			}
		} else {
			System.out.println("\n*** No results ***");
		}				
	}
	
//***MAPPERS**
//Maps camp data to a Camp from SQL	
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
//Maps site data to a Site from SQL
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
	
//***MISC METHODS***
//Converts integer representation of a month to string
	private String showMonth(Long numberOfMonth) {
		return new DateFormatSymbols().getMonths()[(int) (numberOfMonth-1)];
	}
//Finds all Camp(s) within a given Park	
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
//Calculates the length of reservation in days
	private int getTotalDays(LocalDate start, LocalDate end) {
		int totalDays = end.compareTo(start);
		return totalDays;
	}
//Takes a long string (description) and returns it in paragraph-like form
	private String paragraph(String description) {
		String descriptionMultiLine = "";
		String descriptionLine = "";
		int charCount = 0;
		
		for(String word : description.split(" ")) {
			descriptionLine += word + " ";
			charCount+= word.length();
			if(charCount >=60) {
				descriptionMultiLine += descriptionLine + "\n";
				descriptionLine = "";
				charCount = 0;
			}
		} return descriptionMultiLine + descriptionLine;
	}
}