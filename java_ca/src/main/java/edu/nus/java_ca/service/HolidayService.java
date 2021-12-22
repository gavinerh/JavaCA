package edu.nus.java_ca.service;

import java.time.LocalDate;
import java.util.ArrayList;

import edu.nus.java_ca.model.Holidays;
import edu.nus.java_ca.repository.HolidayRepo;

public interface HolidayService {
	
	ArrayList<Holidays> findAll();
	
	Holidays findById(Integer i);
	
	Holidays findByHoliday(LocalDate d);
	
	void deleteById(Integer i);
	
	void createHoliday(Holidays h);

}
