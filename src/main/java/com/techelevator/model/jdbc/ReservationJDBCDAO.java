package com.techelevator.model.jdbc;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDao;

public class ReservationJDBCDAO implements ReservationDao {
	
private JdbcTemplate jdbcTemplate;
	
	public ReservationJDBCDAO (DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public long setReservation(Reservation reservation) {
	String sqlInsertReservation = "INSERT INTO reservtion (site_id, name, from_date, to_date) VALUES (?, ?, ?, ?) RETURNING reservation_id";
	Long newReservation = (long) jdbcTemplate.update(sqlInsertReservation, reservation.getSiteId(), reservation.getReservationName(), reservation.getStart(), reservation.getEnd(), long.class);
	
		return newReservation;
	}

}
