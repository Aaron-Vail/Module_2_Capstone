package com.techelevator;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.model.Reservation;
import com.techelevator.model.jdbc.ReservationJDBCDAO;

public class TESTreservationJDBCDAO {
	/* Using this particular implementation of DataSource so that
	 * every database interaction is part of the same database
	 * session and hence the same database transaction */
	private static SingleConnectionDataSource datasource;
	private JdbcTemplate jdbcTemplate;
	private ReservationJDBCDAO dao;
	
	public TESTreservationJDBCDAO (SingleConnectionDataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}
	/* Before any tests are run, this method initializes the datasource for testing. */
	@BeforeClass
	public static void setupDataSource() {
		datasource = new SingleConnectionDataSource();
		datasource.setUrl("jdbc:postgresql://localhost:5432/campground");
		datasource.setUsername("postgres");
		datasource.setPassword("postgres1");
		/* The following line disables autocommit for connections 
		 * returned by this DataSource. This allows us to rollback
		 * any changes after each test */
		datasource.setAutoCommit(false);
	}

	
	/* After all tests have finished running, this method will close the DataSource */
	@AfterClass
	public static void closeDataSource() throws SQLException {
		datasource.destroy();
	}

	/* After each test, we rollback any changes that were made to the database so that
	 * everything is clean for the next test */
	@After
	public void rollback() throws SQLException {
		datasource.getConnection().rollback();
	}
	
	/* This method provides access to the DataSource for subclasses so that 
	 * they can instantiate a DAO for testing */
	public DataSource getDataSource() {
		return datasource;
	}
	
	@Test
	public void setReservation () {
		//String sqlInsertReservation = "INSERT INTO reservation (site_id, name, from_date, to_date) VALUES (?,?,?,?) RETURNING reservation_id";
		//Long newReservation = (long) jdbcTemplate.update(sqlInsertReservation, long.class, res.getSiteId(), res.getReservationName(), res.getStart(), res.getEnd());
		
		
		LocalDate start = LocalDate.parse("1000-10-10");
		LocalDate end = LocalDate.parse("1000-10-11");

		Reservation test = new Reservation();
		test.setSiteId((long) 1);
		test.setReservationName("Gary");
		test.setStart(start);
		test.setEnd(end);
		
		dao.setReservation(test);
		
		String sql = "SELECT name FROM reservation WHERE name='Gary' RETURNING name";
		String gary = jdbcTemplate.queryForObject(sql, String.class);
		
		
		assertEquals("Gary", gary);
	}

}
