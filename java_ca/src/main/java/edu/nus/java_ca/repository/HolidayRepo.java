package edu.nus.java_ca.repository;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.nus.java_ca.model.Holidays;

public interface HolidayRepo extends JpaRepository<Holidays, Integer> {
	
	/*
	 * Holidays findBy(LocalDate holiday);
	 */
}
