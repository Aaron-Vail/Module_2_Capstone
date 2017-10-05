package com.techelevator;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.model.Park;
import com.techelevator.model.ParkDao;
import com.techelevator.model.jdbc.ParkJDBCDAO;
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
	private static final String SEARCH_CRITERIA_ARRIVAL = "What is the arrival date? (yyyy-mm-dd)";
	private static final String SEARCH_CRITERIA_DEPARTURE = "What is the departure date? (yyyy-mm-dd)";
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
		
		// create your DAOs here
		parkDao = new ParkJDBCDAO(datasource);
	}
	
	public void run() {
		
		boolean mainMenu = true;
		while (mainMenu) {
			
			System.out.print("\n" + MAIN_MENU_OPTION_DISPLAY_PARKS_DETAILS + ": \n");

			//add quit			
			Park choice = (Park) menu.getChoiceFromOptions(parkDao.getAllParks().toArray());
			
			
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
					
					boolean subMenu1 = true;
					while (subMenu1) {
						String choice2 = (String) menu.getChoiceFromOptions(SELECT_A_COMMAND_OPTIONS);
						if (choice2.equals(SELECT_A_COMMAND_VIEW_CAMPS)) {
							Long longPark = choice.getParkId();
							parkDao.getAllCampsByPark(longPark);
							//this is where we left off
						
							
						} else if (choice2.equals(SELECT_A_COMMAND_SEARCH_RESERVATION)) {
							
						} else { //SELECT_A_COMMAND_RETURN_PREVIOUS_MENU
							subMenu1 = false;
						}
					}
				}
			}
		}
	} 		
}
