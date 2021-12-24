package edu.nus.java_ca.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import edu.nus.java_ca.model.Holidays;
import edu.nus.java_ca.repository.HolidayRepo;

@Service
public class HolidayServiceImpl implements HolidayService {
	@Autowired
	HolidayRepo hrepo;

	@Override
	@Transactional
	public ArrayList<Holidays> findAll() {
		// TODO Auto-generated method stub
		return (ArrayList<Holidays>) hrepo.findAll();
	}

	@Override
	@Transactional
	public Holidays findById(Integer i) {
		// TODO Auto-generated method stub
		return hrepo.findById(i).orElse(null);
	}

	@Override
	@Transactional
	public Holidays findByHoliday(Integer i) {
		// TODO Auto-generated method stub
		return hrepo.findById(i).orElse(null);
	}

	@Override
	@Transactional
	@Modifying
	public void deleteById(Integer i) {
		// TODO Auto-generated method stub
		hrepo.deleteById(i);
	}

	@Override
	@Transactional
	public void createHoliday(Holidays h) {
		// TODO Auto-generated method stub
		hrepo.saveAndFlush(h);
	}

	@Override
	public ArrayList<LocalDate> findHolidays() {
		ArrayList<Holidays> ho = (ArrayList<Holidays>) hrepo.findAll();
		ArrayList<LocalDate> d = new ArrayList<>();
		for(Holidays h: ho) {
			d.addAll(h.getStartDate().datesUntil(h.getEndDate().plusDays(1))
					.collect(Collectors
		                    .toCollection(ArrayList::new)));
		}
		return d;
	}

}
