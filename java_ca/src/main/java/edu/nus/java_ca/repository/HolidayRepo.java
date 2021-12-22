package edu.nus.java_ca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.nus.java_ca.model.Holidays;

public interface HolidayRepo extends JpaRepository<Holidays, Integer> {
	
	Holidays findByHoliday(Holidays holiday);

}
